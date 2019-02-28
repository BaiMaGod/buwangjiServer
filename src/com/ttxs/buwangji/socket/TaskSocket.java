/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.ttxs.buwangji.exception.ServiceException;
import com.ttxs.buwangji.service.TaskService;
import com.ttxs.buwangji.service.TaskServiceImpl;
import com.ttxs.buwangji.service.TeamService;
import com.ttxs.buwangji.service.TeamServiceImpl;
import com.ttxs.buwangji.service.UserService;
import com.ttxs.buwangji.service.UserServiceImpl;

import net.sf.json.JSONObject;


/**
 * @author yc
 *	处理客户端 任务操作 的请求
 *		包括任务转发、任务上传、任务查询等，并一直保持连接，服务器通过此套接字转发给客户端 任务信息
 */
public class TaskSocket {
	//监听的端口号
		public static final int PORT = 8886;
	/**
	 * 监听用户 进行任务操作 的套接字
	 */
	public static ServerSocket taskListenSocket;
	/**
	 * 任务操作过程的传输套接字
	 */
	private Socket taskSocket;
	//创建用户服务类（业务层）对象
	private UserService userService = new UserServiceImpl();
	private TaskService taskService = new TaskServiceImpl();
	private TeamService teamService = new TeamServiceImpl();
	
	public TaskSocket() {
		try {
			taskListenSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//开启线程，接受客户端进行笔记操作的连接请求
		new AcceptNoteThread().start();
	}
	
	class AcceptNoteThread extends Thread{
		public void run() {
			while(this.isAlive()) {
				try {
					//接受客户端的连接请求
					taskSocket=taskListenSocket.accept();
					
					if(taskSocket != null){
						//开启线程，处理该用户登录操作
						new TaskServiceThread(taskSocket).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 }
	class TaskServiceThread extends Thread{
		Socket taskSocket;

		public TaskServiceThread(Socket taskSocket ) {
			this.taskSocket = taskSocket;
		}

		@Override
		public void run() {
			
			BufferedReader bReader = null;
			BufferedWriter bWriter = null;
			JSONObject jObject = null ;
			String number = "";
			try {
				//获得通信套接字的输入流
				bReader = new BufferedReader(new InputStreamReader(taskSocket.getInputStream()));
				//获得通信套接字的输出流
				bWriter = new BufferedWriter(new OutputStreamWriter(taskSocket.getOutputStream()));
				//socket不主动关闭，循环接收客户端发来的消息
				while(this.isAlive()) {
					//读取输入流的内容
					String data = bReader.readLine();
					//将读取到的内容转换为JSON对象
					JSONObject jsonObject = JSONObject.fromObject(data);
					//客户端请求的操作是否成功的标识，默认不成功
					boolean isSuccess = false ;
					//获取JSON对象中handle对应的值，表示客户端请求的操作类型
					switch (jsonObject.getString("handle")) {
					//登录操作
					case "login":
						jObject = userService.login(jsonObject);
						//将操作结果以json格式返回给客户端
						jObject.put("handle", "login");
						jObject.put("uuid", jsonObject.getString("uuid"));
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						if(jObject != null && jObject.containsKey("number")) {
							//获取用户账号
							number = jObject.getString("number");
							if(OnlineSocket.onlineSockets.containsKey(number)){
								OnlineSocket.onlineSockets.replace(number, bWriter);
							}else {
								//将任务模块的通信套接字存到Map中，对应关键字为客户端账号
								OnlineSocket.onlineSockets.put(number, bWriter);
								
							}
						}
						break;
					//注册操作
					case "register":
						isSuccess = userService.register(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "register");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//修改个人基本信息操作
					case "update":
						isSuccess = userService.update(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "update");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//修改个人帐号
					case "updateNumber":
						isSuccess = userService.updateNumber(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "updateNumber");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//修改密码
					case "updatePassword":
						isSuccess = userService.updatePassword(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "updatePassword");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//任务同步
					case "sync":
						jObject  = taskService.sync(jsonObject);
						jObject.put("handle", "sync");
						jObject.put("uuid", jsonObject.getString("uuid"));
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//客户端每创建一个任务，就上传到服务端
					case "addTask":
						isSuccess = taskService.add(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "addTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//任务查询
					case "findTask":
						jObject = taskService.findTask(jObject);
						jObject = new JSONObject();
						jObject.put("handle", "findTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//任务修改	
					case "updateTask":
						isSuccess = taskService.update(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "updateTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//任务删除	
					case "deleteTask":
						isSuccess = taskService.delete(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "deleteTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//用户给Team发任务
					case "sendTask":
						isSuccess = teamService.send(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "sendTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//新增Team	
					case "addTeam":
						isSuccess = teamService.add(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "addTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//修改Team名
					case "updateTeamName":
						isSuccess = teamService.updateName(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "updateTeamName");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//删除Team
					case "deleteTeam":
						isSuccess = teamService.delete(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "deleteTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//查询Team
					case "findTeam":
						jObject = teamService.findTeamById(jsonObject);
						jObject.put("handle", "findTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//加入Team
					case "joinTeam":
						isSuccess = teamService.joinTeam(jsonObject);
						jObject.put("handle", "joinTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//退出Team
					case "exitTeam":
						isSuccess = teamService.exitTeam(jsonObject);
						jObject.put("handle", "exitTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//屏蔽Team任务
					case "screenTeam":
						isSuccess = teamService.screenTeam(jsonObject);
						jObject.put("handle", "screenTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					default:
						break;
					}
				}
			} catch (IOException|ServiceException e) {
				e.printStackTrace();
				jObject = new JSONObject();
				jObject.put("handle", "error");
				jObject.put("errorMassage", "服务器被玩坏了...");
				try {
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
				} catch (IOException e1) { //异常，说明客户端断开连接，即下线
					e1.printStackTrace();
					//将该账号从在线列表中移除
					OnlineSocket.onlineSockets.remove(number,bWriter);
					try {
						bWriter.close();
						taskSocket.close();
					} catch (IOException e11) {
						e11.printStackTrace();
						new ServiceException("TaskSocket错误！");
					}
					return;
				}
			}
		}
	}

}
