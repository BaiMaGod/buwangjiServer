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
 *	�����û����������socket
 *		������½��ע�ᡢ�޸ĸ������ϵȣ�
 */
public class UserSocket {
	//�����Ķ˿ں�
	public static final int PORT = 28880;
	/**
	 * �����û� �������� ���׽���
	 */
	public static ServerSocket userListenSocket;
	/**
	 * ��Ӧ�Ĵ����׽���
	 */
	private Socket userSocket;
	//�����û������ࣨҵ��㣩����
	private UserService userService = new UserServiceImpl();

	
	public UserSocket() {
		try {
			//ʵ����ServerSocket
			userListenSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//�����̣߳����ܿͻ�����������
		new AcceptUserThread().start();
		
	}
	
	class AcceptUserThread extends Thread{
		public void run() {
			while(this.isAlive()) {
				try {
					//���ܿͻ��˵���������
					userSocket = userListenSocket.accept();
					System.out.println("һ���ͻ���������...");
					
					if(userSocket != null){
						//�����̣߳�������û��ľ�������
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
				//���ͨ���׽��ֵ�������
				bReader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
				//���ͨ���׽��ֵ������
				bWriter = new BufferedWriter(new OutputStreamWriter(userSocket.getOutputStream()));
				//��ȡ������������
				String data = bReader.readLine();
				//����ȡ��������ת��ΪJSON����
				JSONObject jsonObject = JSONObject.fromObject(data);
				//�ͻ�������Ĳ����Ƿ�ɹ��ı�ʶ��Ĭ�ϲ��ɹ�
				boolean isSuccess = false ;
				//��ȡJSON������handle��Ӧ��ֵ����ʾ�ͻ�������Ĳ�������
				switch (jsonObject.getString("handle")) {
				//��¼����
				case "login":
					jObject = userService.login(jsonObject);
					//�����������json��ʽ���ظ��ͻ���
					jObject.put("handle", "login");
					jObject.put("uuid", jsonObject.getString("uuid"));
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//ע�����
				case "register":
					isSuccess = userService.register(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "register");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//�޸ĸ��˻�����Ϣ����
				case "update":
					isSuccess = userService.update(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "update");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//�޸ĸ����ʺ�
				case "updateNumber":
					isSuccess = userService.updateNumber(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "updateNumber");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//�޸�����
				case "updatePassword":
					isSuccess = userService.updatePassword(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "updatePassword");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				default:
					break;
				}
			} catch (IOException|ServiceException e) {
				//�����쳣������ʧ�ܣ����ͻ��˷���������Ϣ
				e.printStackTrace();
				jObject = new JSONObject();
				jObject.put("handle", "error");
				jObject.put("errorMassage", "���������滵��...");
				try {
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}finally {
				//�ر���Դ
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
