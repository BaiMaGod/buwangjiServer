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
	TeamService teamService = new TeamServiceImpl();
	
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
					//����ͬ��
					case "sync":
						jObject  = taskService.sync(jsonObject);
						jObject.put("handle", "sync");
						jObject.put("uuid", jsonObject.getString("uuid"));
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//�ͻ���ÿ����һ�����񣬾��ϴ��������
					case "addTask":
						isSuccess = taskService.add(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "addTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//�ͻ��˸�Team������
					case "sendTask":
						isSuccess = taskService.send(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "sendTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//�����ѯ
					case "findTask":
						jObject = taskService.findTask(jObject);
						jObject = new JSONObject();
						jObject.put("handle", "findTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//�����޸�	
					case "updateTask":
						isSuccess = taskService.update(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "updateTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//����ɾ��	
					case "deleteTask":
						isSuccess = taskService.delete(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "deleteTask");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//����Team	
					case "addTeam":
						isSuccess = teamService.add(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "addTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//�޸�Team��Ϣ	
					case "updateTeamName":
						isSuccess = teamService.updateName(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "updateTeamName");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//ɾ��Team
					case "deleteTeam":
						isSuccess = teamService.delete(jsonObject);
						jObject = new JSONObject();
						jObject.put("handle", "deleteTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//��ѯTeam
					case "findTeam":
						jObject = teamService.findTeamById(jsonObject);
						jObject.put("handle", "findTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//�˳�Team
					case "exitTeam":
						isSuccess = teamService.exitTeam(jsonObject);
						jObject.put("handle", "exitTeam");
						jObject.put("uuid", jsonObject.getString("uuid"));
						jObject.put("isSuccess", isSuccess);
						bWriter.write(jObject.toString()+"\n");
						bWriter.flush();
						break;
					//����Team����
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
