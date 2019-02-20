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
	//
	TaskService taskService = new TaskServiceImpl();
	
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
			try {
				//获得通信套接字的输入流
				bReader = new BufferedReader(new InputStreamReader(taskSocket.getInputStream()));
				//获得通信套接字的输出流
				bWriter = new BufferedWriter(new OutputStreamWriter(taskSocket.getOutputStream()));
				while(this.isAlive()) {
					//读取输入流的内容
					String data = bReader.readLine();
					//将读取到的内容转换为JSON对象
					JSONObject jsonObject = JSONObject.fromObject(data);
					//将任务模块的通信套接字存到Map中，对应关键字为客户端账号
					OnlineSocket.onlineSockets.put(jsonObject.getString("number"), bWriter);
					//客户端请求的操作是否成功的标识，默认不成功
					boolean isSuccess = false ;
					//获取JSON对象中handle对应的值，表示客户端请求的操作类型
					switch (jsonObject.getString("handle")) {
					//客户端给别人发任务
					case "send":
						isSuccess = taskService.send(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "send");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//客户端每创建一个任务，就上传到服务端
					case "add":
						isSuccess = taskService.add(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "add");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//任务修改	
					case "update":
						isSuccess = taskService.update(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "update");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//任务删除	
					case "delete":
						isSuccess = taskService.delete(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "delete");
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
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//任务查询
					case "findTask":
						jObject = taskService.findTask(jObject);
						jObject = new JSONObject();
						jObject.put("handle", "findPage");
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
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
