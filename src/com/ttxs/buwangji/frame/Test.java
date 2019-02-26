/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.frame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ttxs.buwangji.utils.Tools;

import net.sf.json.JSONObject;

/**
 * @author Administrator
 *
 */
public class Test {
	Socket userSocket;
	Socket noteSocket;
	Socket taskSocket;
	
	BufferedReader bReader = null;
	BufferedWriter bWriter = null;
	
	public static void main(String[] args)  {
		Test test = new Test();
		
		test.connectTest(1);
		test.registerTest();
	}
	
	/**
	 * 连接服务器测试
	 * @param socket
	 * @param port
	 */
	public void connectTest(int i) {
		try {
			switch (i) {
			case 1:
				userSocket = new Socket("118.24.164.203", 8880);
				break;
			case 2:
				noteSocket = new Socket("118.24.164.203", 8883);
				break;
			case 3:
				taskSocket = new Socket("118.24.164.203", 8886);
				break;
			default:
				break;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("找不到服务器！");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("连接失败！");
		}
		System.out.println("连接服务器成功...");
	}
	
	/**
	 * 用户注册测试
	 */
	public void registerTest() {
		String number = "111111111@qq.com";
		String password = "123123123";
		String name = "床前明月光";
		String uuid1 = Tools.getUUID();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("handle", "register");
		jsonObject.put("uuid", uuid1);
		jsonObject.put("number", number);
		jsonObject.put("password", password);
		jsonObject.put("name", name);
		
		JSONObject jObject = null;
		try {
			jObject = sendAndReceive(userSocket, jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("注册消息发送到服务器失败！");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"register".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("接受到的消息格式错误，请重试!");
		}else {
			boolean isSuccess = jObject.getBoolean("isSuccess");
			if(isSuccess) {
				System.out.println("注册成功！");
			}else {
				System.out.println("注册失败！");
			}
		}
		
	}
	
	/**
	 * 发送给服务器消息，以及接收服务器的反馈消息
	 * @param socket
	 * @param jsonObject
	 * @return
	 * @throws IOException
	 */
	public JSONObject sendAndReceive(Socket socket,JSONObject jsonObject) throws IOException {
		//获得通信套接字的输出流
		bWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		System.out.println("获得输出流成功...");
		
		//发送
		bWriter.write(jsonObject.toString()+"\n");
		bWriter.flush();
		
		System.out.println("发送消息成功...");
		
		//获得通信套接字的输入流
		bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		System.out.println("获得输入流成功...");
		
		//读取输入流的内容
		String data = bReader.readLine();
		//将读取到的内容转换为JSON对象
		JSONObject jObject = JSONObject.fromObject(data);
		
		System.out.println("接受消息成功...");
		
		bWriter.close();
		bReader.close();
		return jObject;
	}
	
	public void fei() {
////		System.out.println(Tools.getUUID());
////		
////		Reader reader = Resources.getResourceAsReader("Configuration.xml");
////		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
//		SqlSession session = Tools.getSession();
//		UserDao userDao = session.getMapper(UserDao.class);
//		
//		List<User> users = userDao.findUserByName("%%员%%");
//		
////		System.out.println(user);
////
////				
////		User user = users.get(1);
//		User user = new User();
//		user.setId("33");
//		user.setName("123");
//		int i = userDao.add(user);
//		session.commit();
//		System.out.println(i+":"+user+"\n");
//////		
////		user.setName("咋啊啊");
////		userDao.update(user);
////		System.out.println(user);
//////		
//////		userDao.delete(user.getId());
//////		
//		List<User> users2 = userDao.findAll();
//		for (User user2 : users2) {
//			System.out.println(user2);
//		}
//		
//		session.close();
		
	}
}
