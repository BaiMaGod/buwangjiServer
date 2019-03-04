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
//		test.registerTest();		//�ɹ�
		test.loginTest();			//�ɹ�
//		test.updateTest();			//�ɹ�
//		test.updatePasswordTest();	//�ɹ�
		
		
//		test.connectTest(2);
//		test.findNoteTest();		//�ɹ�
//		test.uploadNoteTest();
		
	}
	
	/**
	 * ���ӷ���������
	 * @param socket
	 * @param port
	 */
	public void connectTest(int i) {
		try {
			switch (i) {
			case 1:
				userSocket = new Socket("148.70.0.26", 28880);
				break;
			case 2:
				noteSocket = new Socket("148.70.0.26", 28883);
				break;
			case 3:
				taskSocket = new Socket("148.70.0.26", 28886);
				break;
			default:
				break;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("�Ҳ�����������");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("����ʧ�ܣ�");
		}
		System.out.println("���ӷ������ɹ�...");
	}
	
	/**
	 * �û�ע�����
	 */
	public void registerTest() {
		String number = "111111111@qq.com";
		String password = "123123123";
		String name = "��ǰ���¹�";
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
			System.out.println("ע����Ϣ���͵�������ʧ�ܣ�");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"register".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("���յ�����Ϣ��ʽ����������!");
		}else {
			boolean isSuccess = jObject.getBoolean("isSuccess");
			if(isSuccess) {
				System.out.println("ע��ɹ���");
			}else {
				System.out.println("ע��ʧ�ܣ�");
			}
		}
		
	}
	
	/**
	 * �û���¼����
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
			System.out.println("ע����Ϣ���͵�������ʧ�ܣ�");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"login".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("���յ�����Ϣ��ʽ����������!");
		}else {
			if(jObject.containsKey("name")) {
				System.out.println("��¼�ɹ����û�����"+jObject.getString("name"));
			}else {
				System.out.println("��¼ʧ�ܣ�");
			}
		}
		
	}
	
	/**
	 * �û��޸ĸ�����Ϣ����
	 */
	public void updateTest() {
		String number = "111111111@qq.com";
		String name = "���ǵ���˪";
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
			System.out.println("ע����Ϣ���͵�������ʧ�ܣ�");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"update".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("���յ�����Ϣ��ʽ����������!");
		}else {
			boolean isSuccess = jObject.getBoolean("isSuccess");
			if(isSuccess) {
				System.out.println("�޸ĸ�����Ϣ�ɹ���");
			}else {
				System.out.println("�޸ĸ�����Ϣʧ�ܣ�");
			}
		}
		
	}
	
	/**
	 * �û��޸��������
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
			System.out.println("ע����Ϣ���͵�������ʧ�ܣ�");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"updatePassword".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("���յ�����Ϣ��ʽ����������!");
		}else {
			boolean isSuccess = jObject.getBoolean("isSuccess");
			if(isSuccess) {
				System.out.println("�޸�����ɹ���");
			}else {
				System.out.println("�޸�����ʧ�ܣ�");
			}
		}
		
	}
	
	/**
	 * �ʼǲ�ѯ����
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
			System.out.println("ע����Ϣ���͵�������ʧ�ܣ�");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"findNote".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("���յ�����Ϣ��ʽ����������!");
		}else {
			if(jObject.containsKey("noteList")) {
				System.out.println("�ʼǲ�ѯ�ɹ���");
				JSONArray jsonArray = jObject.getJSONArray("noteList");
				for (Object object : jsonArray) {
					System.out.println(object);
				}
			}else {
				System.out.println("�ʼǲ�ѯʧ�ܣ�");
			}
		}
		
	}
	
	/**
	 * �����ʼ��ϴ�����
	 */
	public void uploadNoteTest() {
		String number = "111111111@qq.com";
		String uuid1 = Tools.getUUID();
		
		JSONArray noteList = new JSONArray();
		String content = "����һ���򵥵Ĳ����ļ�";
		String nowTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		Note note = new Note(Tools.getUUID(),number,"0",content,"���Աʼ�",nowTime,1,1);
		JSONObject jsonObject2 = JSONObject.fromObject(note);
		noteList.add(jsonObject2);
		
		note.setId(Tools.getUUID());
		note.setTitle("��2�����Աʼ�");
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
			System.out.println("ע����Ϣ���͵�������ʧ�ܣ�");
		}
		
		String handle = jObject.getString("handle");
		String uuid2 = jObject.getString("uuid");
		if (handle == null || !"uploadNote".equals(handle) || uuid2 == null || !uuid1.equals(uuid2)) {
			System.out.println("���յ�����Ϣ��ʽ����������!");
		}else {
			boolean isSuccess = jObject.getBoolean("isSuccess");
			if(isSuccess) {
				System.out.println("�����ʼ��ϴ��ɹ���");
			}else {
				System.out.println("�����ʼ��ϴ�ʧ�ܣ�");
			}
		}
		
	}
	
	
	
	/**
	 * ���͸���������Ϣ���Լ����շ������ķ�����Ϣ
	 * @param socket
	 * @param jsonObject
	 * @return
	 * @throws IOException
	 */
	public JSONObject sendAndReceive(Socket socket,JSONObject jsonObject) throws IOException {
		//���ͨ���׽��ֵ������
		bWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		System.out.println("���������ɹ�...");
		
		//����
		bWriter.write(jsonObject.toString()+"\n");
		bWriter.flush();
		
		System.out.println("������Ϣ�ɹ�...");
		
		//���ͨ���׽��ֵ�������
		bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		System.out.println("����������ɹ�...");
		
		//��ȡ������������
		String data = bReader.readLine();
		//����ȡ��������ת��ΪJSON����
		JSONObject jObject = JSONObject.fromObject(data);
		
		System.out.println("������Ϣ�ɹ�...");
		
		bWriter.close();
		bReader.close();
		return jObject;
	}
	
	
}
