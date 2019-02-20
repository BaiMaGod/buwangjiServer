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
 *	����ͻ��� ������� ������
 *		��������ת���������ϴ��������ѯ�ȣ���һֱ�������ӣ�������ͨ�����׽���ת�����ͻ��� ������Ϣ
 */
public class TaskSocket {
	//�����Ķ˿ں�
		public static final int PORT = 8886;
	/**
	 * �����û� ����������� ���׽���
	 */
	public static ServerSocket taskListenSocket;
	/**
	 * ����������̵Ĵ����׽���
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
		
		//�����̣߳����ܿͻ��˽��бʼǲ�������������
		new AcceptNoteThread().start();
	}
	
	class AcceptNoteThread extends Thread{
		public void run() {
			while(this.isAlive()) {
				try {
					//���ܿͻ��˵���������
					taskSocket=taskListenSocket.accept();
					
					if(taskSocket != null){
						//�����̣߳�������û���¼����
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
				//���ͨ���׽��ֵ�������
				bReader = new BufferedReader(new InputStreamReader(taskSocket.getInputStream()));
				//���ͨ���׽��ֵ������
				bWriter = new BufferedWriter(new OutputStreamWriter(taskSocket.getOutputStream()));
				while(this.isAlive()) {
					//��ȡ������������
					String data = bReader.readLine();
					//����ȡ��������ת��ΪJSON����
					JSONObject jsonObject = JSONObject.fromObject(data);
					//������ģ���ͨ���׽��ִ浽Map�У���Ӧ�ؼ���Ϊ�ͻ����˺�
					OnlineSocket.onlineSockets.put(jsonObject.getString("number"), bWriter);
					//�ͻ�������Ĳ����Ƿ�ɹ��ı�ʶ��Ĭ�ϲ��ɹ�
					boolean isSuccess = false ;
					//��ȡJSON������handle��Ӧ��ֵ����ʾ�ͻ�������Ĳ�������
					switch (jsonObject.getString("handle")) {
					//�ͻ��˸����˷�����
					case "send":
						isSuccess = taskService.send(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "send");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//�ͻ���ÿ����һ�����񣬾��ϴ��������
					case "add":
						isSuccess = taskService.add(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "add");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//�����޸�	
					case "update":
						isSuccess = taskService.update(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "update");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//����ɾ��	
					case "delete":
						isSuccess = taskService.delete(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "delete");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//����ͬ��
					case "sync":
						jObject  = taskService.sync(jsonObject);
						jObject.put("handle", "sync");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//�����ѯ
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
				jObject.put("errorMassage", "���������滵��...");
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
