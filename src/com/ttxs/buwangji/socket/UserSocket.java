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
import com.ttxs.buwangji.service.UserService;
import com.ttxs.buwangji.service.UserServiceImpl;

import net.sf.json.JSONObject;

/**
 * @author yc
 *	处理用户基本请求的socket
 *		包含登陆、注册、修改个人资料等，
 */
public class UserSocket {
	//监听的端口号
	public static final int PORT = 8880;
	/**
	 * 监听用户 基本请求 的套接字
	 */
	public static ServerSocket userListenSocket;
	/**
	 * 对应的传输套接字
	 */
	private Socket userSocket;
	//创建用户服务类（业务层）对象
	private UserService userService = new UserServiceImpl();

	
	public UserSocket() {
		try {
			userListenSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//开启线程，接受客户的连接请求
		new AcceptUserThread().start();
		
	}
	
	class AcceptUserThread extends Thread{
		public void run() {
			while(this.isAlive()) {
				try {
					//接受客户端的连接请求
					userSocket = userListenSocket.accept();
					//System.out.println("一个客户端已上线");
					
					if(userSocket != null){
						//开启线程，处理该用户的具体请求
						new UserServiceThread(userSocket).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 }
	class UserServiceThread extends Thread{
		Socket userSocket;

		public UserServiceThread(Socket userSocket ) {
			this.userSocket = userSocket;
		}

		@Override
		public void run() {
			BufferedReader bReader = null;
			BufferedWriter bWriter = null;
			JSONObject jObject = null ;
			try {
				//获得通信套接字的输入流
				bReader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
				//获得通信套接字的输出流
				bWriter = new BufferedWriter(new OutputStreamWriter(userSocket.getOutputStream()));
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
					isSuccess = userService.login(jsonObject);
					//将操作结果以json格式返回给客户端
					jObject = new JSONObject();
					jObject.put("handle", "login");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
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
				//修改个人信息操作
				case "update":
					isSuccess = userService.update(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "update");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				default:
					break;
				}
			} catch (IOException|ServiceException e) {
				//出现异常，操作失败，给客户端反馈错误信息
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
			}finally {
				//关闭资源
				try {
					if(bReader != null) {
						bReader.close();
					}
					if(bWriter != null) {
						bWriter.close();
					}
					if(userSocket != null) {
						userSocket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
