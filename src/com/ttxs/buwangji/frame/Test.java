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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.tools.Tool;

import com.mysql.cj.xdevapi.JsonArray;
import com.ttxs.buwangji.bean.Note;
import com.ttxs.buwangji.utils.Tools;

import net.sf.json.JSONArray;
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
//		test.registerTest();		//成功
		test.loginTest();			//成功
//		test.updateTest();			//成功
//		test.updatePasswordTest();	//成功
		
		
//		test.connectTest(2);
//		test.findNoteTest();		//成功
//		test.uploadNoteTest();
		
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
				userSocket = new Socket("118.24.164.203", 28880);
				break;
			case 2:
				noteSocket = new Socket("118.24.164.203", 28883);
				break;
			case 3:
				taskSocket = new Socket("118.24.164.203", 28886);
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
			System.out.println("接收到的消息格式错误，请重试!");
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
	 * 用户登录测试
	 */
	public void loginTest() {
		String number = "111111111@qq.com";
		String password = "123123123";
		String uuid1 = Tools.getUUID();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("handle", "login");
		jsonObject.put("uuid", uuid1);
		jsonObject.put("number", number);
		jsonObject.put("password", password);
		
		JSONObject jObject = null;
		try {
			jObject = sendAndReceive(userSocket, jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("注册消息发送到服务器失败！");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"login".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("接收到的消息格式错误，请重试!");
		}else {
			if(jObject.containsKey("name")) {
				System.out.println("登录成功！用户名："+jObject.getString("name"));
			}else {
				System.out.println("登录失败！");
			}
		}
		
	}
	
	/**
	 * 用户修改个人信息测试
	 */
	public void updateTest() {
		String number = "111111111@qq.com";
		String name = "疑是地上霜";
		String uuid1 = Tools.getUUID();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("handle", "update");
		jsonObject.put("uuid", uuid1);
		jsonObject.put("number", number);
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
		if (handle == null || !"update".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("接收到的消息格式错误，请重试!");
		}else {
			boolean isSuccess = jObject.getBoolean("isSuccess");
			if(isSuccess) {
				System.out.println("修改个人信息成功！");
			}else {
				System.out.println("修改个人信息失败！");
			}
		}
		
	}
	
	/**
	 * 用户修改密码测试
	 */
	public void updatePasswordTest() {
		String number = "111111111@qq.com";
		String password = "123123123";
		String newPassword = "000000000";
		
		String uuid1 = Tools.getUUID();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("handle", "updatePassword");
		jsonObject.put("uuid", uuid1);
		jsonObject.put("number", number);
		jsonObject.put("password", password);
		jsonObject.put("newPassword", newPassword);
		jsonObject.put("type", "1");
		
		JSONObject jObject = null;
		try {
			jObject = sendAndReceive(userSocket, jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("注册消息发送到服务器失败！");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"updatePassword".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("接收到的消息格式错误，请重试!");
		}else {
			boolean isSuccess = jObject.getBoolean("isSuccess");
			if(isSuccess) {
				System.out.println("修改密码成功！");
			}else {
				System.out.println("修改密码失败！");
			}
		}
		
	}
	
	/**
	 * 笔记查询测试
	 */
	public void findNoteTest() {
		String number = "111111111@qq.com";
		String noteType = "all";
		String uuid1 = Tools.getUUID();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("handle", "findNote");
		jsonObject.put("uuid", uuid1);
		jsonObject.put("number", number);
		jsonObject.put("noteType", noteType);
		
		JSONObject jObject = null;
		try {
			jObject = sendAndReceive(noteSocket, jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("注册消息发送到服务器失败！");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"findNote".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("接收到的消息格式错误，请重试!");
		}else {
			if(jObject.containsKey("noteList")) {
				System.out.println("笔记查询成功！");
				JSONArray jsonArray = jObject.getJSONArray("noteList");
				for (Object object : jsonArray) {
					System.out.println(object);
				}
			}else {
				System.out.println("笔记查询失败！");
			}
		}
		
	}
	
	/**
	 * 新增笔记上传测试
	 */
	public void uploadNoteTest() {
		String number = "111111111@qq.com";
		String uuid1 = Tools.getUUID();
		
		JSONArray noteList = new JSONArray();
		String content = "这是一个简单的测试文件";
		String nowTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		Note note = new Note(Tools.getUUID(),number,"0",content,"测试笔记",nowTime,1,1);
		JSONObject jsonObject2 = JSONObject.fromObject(note);
		noteList.add(jsonObject2);
		
		note.setId(Tools.getUUID());
		note.setTitle("第2个测试笔记");
		jsonObject2 = JSONObject.fromObject(note);
		noteList.add(jsonObject2);
		
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("handle", "uploadNote");
		jsonObject.put("uuid", uuid1);
		jsonObject.put("number", number);
		jsonObject.put("noteList", noteList);
		
		
		
		JSONObject jObject = null;
		try {
			jObject = sendAndReceive(noteSocket, jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("注册消息发送到服务器失败！");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"uploadNote".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("接收到的消息格式错误，请重试!");
		}else {
			boolean isSuccess = jObject.getBoolean("isSuccess");
			if(isSuccess) {
				System.out.println("新增笔记上传成功！");
			}else {
				System.out.println("新增笔记上传失败！");
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
		
		System.out.println("接收消息成功...");
		
		bWriter.close();
		bReader.close();
		return jObject;
	}
	
	
}
