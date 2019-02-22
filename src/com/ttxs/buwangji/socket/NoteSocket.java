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
import com.ttxs.buwangji.service.GroupService;
import com.ttxs.buwangji.service.GroupServiceImpl;
import com.ttxs.buwangji.service.NoteService;
import com.ttxs.buwangji.service.NoteServiceImpl;

import net.sf.json.JSONObject;


/**
 * @author yc
 *	����ͻ��� �ʼ���ص�����
 *		�����ʼ��ϴ����ʼ�ͬ���ȣ�һ�β������ʱ�ر�
 */
public class NoteSocket {
	//�����Ķ˿ں�
	public static final int PORT = 8883;
	/**
	 * �����û� ���бʼǲ��� ���׽���
	 */
	public static ServerSocket noteListenSocket;
	/**
	 * �ʼǲ������̵Ĵ����׽���
	 * 	�ͻ��˿���ͨ�����׽���ͬ���ʼ�
	 */
	private Socket noteSocket;
	// ҵ������
	NoteService noteService = new NoteServiceImpl();
	GroupService groupService = new GroupServiceImpl();
	
	public NoteSocket() {
		try {
			noteListenSocket = new ServerSocket(PORT);
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
					noteSocket=noteListenSocket.accept();
					
					if(noteSocket != null){
						//�����̣߳�������û���¼����
						new NoteServiceThread(noteSocket).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 }
	class NoteServiceThread extends Thread{
		Socket noteSocket;

		public NoteServiceThread(Socket noteSocket ) {
			this.noteSocket = noteSocket;
		}

		@Override
		public void run() {
			BufferedReader bReader = null;
			BufferedWriter bWriter = null;
			JSONObject jObject = null ;
			try {
				bReader = new BufferedReader(new InputStreamReader(noteSocket.getInputStream()));
				bWriter = new BufferedWriter(new OutputStreamWriter(noteSocket.getOutputStream()));

				String data = bReader.readLine();
				JSONObject jsonObject = JSONObject.fromObject(data);
				
				boolean isSuccess = false ;
				switch (jsonObject.getString("handle")) {
				//�ƶ˱ʼǲ�ѯ
				case "findNote":
					jObject = noteService.findNote(jsonObject);
					jObject.put("handle", "findNote");
					jObject.put("uuid", jsonObject.getString("uuid"));
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//�ʼ������������
				case "uploadNote":
					isSuccess = noteService.add(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "uploadNote");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//�ʼ�ɾ��	
				case "deleteNote":
					isSuccess = noteService.delete(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "deleteNote");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//�ƶ˱ʼ��޸�
				case "updateNote":
					isSuccess = noteService.update(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "updateNote");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//�ʼǺͷ���ͬ��
				case "sync":
					jObject = noteService.sync(jsonObject);
					jObject.put("handle", "sync");
					jObject.put("uuid", jsonObject.getString("uuid"));
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//�����ѯ
				case "findGroup":
					jObject = groupService.findGroup(jsonObject);
					jObject.put("handle", "findGroup");
					jObject.put("uuid", jsonObject.getString("uuid"));
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
					//���������������
				case "uploadGroup":
					isSuccess = groupService.add(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "uploadGroup");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
					//����ɾ��	
				case "deleteGroup":
					isSuccess = groupService.delete(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "deleteGroup");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
					//�����޸�
				case "updateGroup":
					isSuccess = groupService.update(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "updateGroup");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				default:
					break;
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
			}finally {
				try {
					if(bReader != null) {
						bReader.close();
					}
					if(bWriter != null) {
						bWriter.close();
					}
					if(noteSocket != null) {
						noteSocket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
