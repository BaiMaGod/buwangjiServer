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
 *	处理客户端 笔记相关的请求
 *		包括笔记上传、笔记同步等，一次操作完后及时关闭
 */
public class NoteSocket {
	//监听的端口号
	public static final int PORT = 8883;
	/**
	 * 监听用户 进行笔记操作 的套接字
	 */
	public static ServerSocket noteListenSocket;
	/**
	 * 笔记操作过程的传输套接字
	 * 	客户端可以通过此套接字同步笔记
	 */
	private Socket noteSocket;
	// 业务层对象
	NoteService noteService = new NoteServiceImpl();
	GroupService groupService = new GroupServiceImpl();
	
	public NoteSocket() {
		try {
			noteListenSocket = new ServerSocket(PORT);
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
					noteSocket=noteListenSocket.accept();
					
					if(noteSocket != null){
						//开启线程，处理该用户登录操作
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
				//云端笔记查询
				case "findNote":
					jObject = noteService.findNote(jsonObject);
					jObject.put("handle", "findNote");
					jObject.put("uuid", jsonObject.getString("uuid"));
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//笔记新增到服务端
				case "uploadNote":
					isSuccess = noteService.add(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "uploadNote");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//笔记删除	
				case "deleteNote":
					isSuccess = noteService.delete(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "deleteNote");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//云端笔记修改
				case "updateNote":
					isSuccess = noteService.update(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "updateNote");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//笔记和分组同步
				case "sync":
					jObject = noteService.sync(jsonObject);
					jObject.put("handle", "sync");
					jObject.put("uuid", jsonObject.getString("uuid"));
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
				//分组查询
				case "findGroup":
					jObject = groupService.findGroup(jsonObject);
					jObject.put("handle", "findGroup");
					jObject.put("uuid", jsonObject.getString("uuid"));
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
					//分组新增到服务端
				case "uploadGroup":
					isSuccess = groupService.add(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "uploadGroup");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
					//分组删除	
				case "deleteGroup":
					isSuccess = groupService.delete(jsonObject);
					jObject = new JSONObject();
					jObject.put("handle", "deleteGroup");
					jObject.put("uuid", jsonObject.getString("uuid"));
					jObject.put("isSuccess", isSuccess);
					bWriter.write(jObject.toString()+"\n");
					bWriter.flush();
					break;
					//分组修改
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
				jObject.put("errorMassage", "服务器被玩坏了...");
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
