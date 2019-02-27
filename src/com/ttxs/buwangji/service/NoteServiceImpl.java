/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Case;
import org.apache.ibatis.session.SqlSession;

import com.ttxs.buwangji.bean.Note;
import com.ttxs.buwangji.dao.NoteDao;
import com.ttxs.buwangji.exception.ServiceException;
import com.ttxs.buwangji.utils.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Administrator
 *
 */
public class NoteServiceImpl implements NoteService {
	SqlSession session;
	NoteDao noteDao;
	
	public NoteServiceImpl() {
		session = Tools.getSession();
		noteDao = session.getMapper(NoteDao.class);
	}

	@Override
	public boolean add(JSONObject jsonObject) throws ServiceException {
		//获取客户端传来的笔记列表，批量添加
		List<JSONObject> list = Tools.JsonArrayToList(jsonObject.getJSONArray("noteList"));
		if(list == null || list.isEmpty()) {
			return false;
		}
		List<Note> noteList = new ArrayList<>();
		Note note = null;
		for (JSONObject jObject : list) {
			String noteId = jObject.getString("noteId");
			String userNumber = jObject.getString("userNumber");
			String groupId = jObject.getString("groupId");
			String title = jObject.getString("title");
			String content = jObject.getString("filePath");
			String createTime = jObject.getString("createTime");
			String updateTime = jObject.getString("updateTime");
			int status = jObject.getInt("status");
			int isSync = jObject.getInt("isSync");
			
			String filePath = "F:\\com.buwangji.server\\Note\\"+userNumber+"\\"+createTime+".txt";
			File file = new File(filePath);
			try {
				//将笔记文件存到硬盘中
				FileWriter fWriter = new FileWriter(file);
				fWriter.write(content);
				fWriter.flush();
				fWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
				new ServiceException("TaskServiceImpl.add()出错！");
			} 
			
			note = new Note();
			note.setId(noteId);
			note.setUserNumber(userNumber);
			note.setGroupId(groupId);
			note.setTitle(title);
			note.setFilePath(filePath);
			note.setCreateTime(createTime);
			note.setUpdateTime(updateTime);
			note.setStatus(status);		
			note.setIsSync(isSync);
			noteList.add(note);
		}
		int num = noteDao.add(noteList);
		session.commit();
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean update(JSONObject jsonObject) throws ServiceException {
		List<JSONObject> list = Tools.JsonArrayToList(jsonObject.getJSONArray("noteList"));
		if(list == null || list.isEmpty()) {
			return false;
		}
		List<Note> noteList = new ArrayList<>();
		Note note = null;
		for (JSONObject jObject : list) {
			String id = jObject.getString("id");
			String groupId = jObject.getString("groupId");
			String title = jObject.getString("title");
			String content = jObject.getString("content");
			String updateTime = jObject.getString("updateTime");
			
			note = new Note();
			note.setId(id);
			if(groupId != null) {
				note.setGroupId(groupId);
			}
			if(title != null){
				note.setTitle(title);
			}
			if(updateTime != null) {
				note.setUpdateTime(updateTime);
			}
			if(jObject.containsKey("status")) {
				int status = jObject.getInt("status");
				note.setStatus(status);
			}
			if(content != null) {
				String filePath = noteDao.findFilePathById(id);
				File file = new File(filePath);
				try {
					//覆盖原文件中的内容
					FileWriter fWriter = new FileWriter(file);
					fWriter.write(content);
					fWriter.flush();
					fWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
					new ServiceException("TaskServiceImpl.update()出错！");
				} 
			}
			noteList.add(note);
		}
		int num = noteDao.update(noteList);
		session.commit();
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public JSONObject sync(JSONObject jsonObject) throws ServiceException {
		//同步操作，分为两步，第一步只上传头部
		// 		上传的头部，包含客户端本地每个笔记的id和状态码，与数据库对比，
		//		如果相同id的任务，本地笔记的状态码大，说明该笔记在本地被修改，需要修改服务器数据库中的内容，
		//		如果服务器数据库中的状态码大，则将此笔记内容发送到客户端，让客户端本地修改笔记内容
		//		如果本地客户端存在服务器没有的笔记id，则通知本地客户端上传该任务全部内容，
		//		如果服务器数据库中存在本地客户端中没有的笔记id，则将此笔记内容发送到客户端
		JSONObject jObject = new JSONObject();

		JSONArray jsonArray = jsonObject.getJSONArray("noteSyncList");
		JSONArray serverUpdateArray = new JSONArray();
		JSONArray serverAddArray = new JSONArray();
		JSONArray clientUpdateArray = new JSONArray();
		JSONArray clientAddArray = new JSONArray();
		String number = jsonObject.getString("number");
		Map<String, Integer> statusMap = noteDao.findStatusCodeByNumber(number);
		if(jsonArray != null) {
			for (Iterator<?> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObject2 = (JSONObject) iterator.next();
				
				String noteId = jsonObject2.getString("taskId");
				if(statusMap.containsKey(noteId)) {
					int serverStatus = statusMap.get(noteId);
					int clientStatus = jsonObject2.getInt("status");
					//服务器数据库中的状态码大，则将此任务内容添加到clientUpdateArray
					if(serverStatus > clientStatus) {
						Note note = noteDao.findNoteById(noteId);
						File file = new File(note.getFilePath());
						BufferedReader bReader = null ;
						try {
							bReader = new BufferedReader(new FileReader(file));
							StringBuilder content = new StringBuilder();
							String string = null;
							while((string=bReader.readLine())!=null) {
								content.append(string);
							}
							//将任务文件中的内容赋值给filePath属性
							note.setFilePath(content.toString());
						} catch (IOException e) {
							e.printStackTrace();
							new ServiceException("TaskServiceImpl.sync()出错！");
						}finally {
							if(bReader != null) {
								try {
									bReader.close();
								} catch (IOException e) {
									e.printStackTrace();
									new ServiceException("TaskServiceImpl.sync()出错！");
								}
							}
						}
						JSONObject jsonObject3 = JSONObject.fromObject(note);
						clientUpdateArray.add(jsonObject3);
						iterator.remove();
					}
					//本地任务的状态码大，则将此任务id添加到serverUpdateArray
					else if(serverStatus < clientStatus) {
						serverUpdateArray.add(noteId);
						iterator.remove();
					}else {
						//无论状态码谁大谁小或者相等，都将此任务id从statusMap移除
						iterator.remove();
					}
				}
				//statusMap中没有该任务id,将该任务id添加到serverAddArray
				else {
					serverAddArray.add(noteId);
					iterator.remove();
				}
				//此时statusMap中剩下的任务id，便是客户端没有的，将任务信息都添加到clientAddArray
				for (String id : statusMap.keySet()) {
					Note note = noteDao.findNoteById(id);
					File file = new File(note.getFilePath());
					BufferedReader bReader = null ;
					try {
						bReader = new BufferedReader(new FileReader(file));
						StringBuilder content = new StringBuilder();
						String string = null;
						while((string=bReader.readLine())!=null) {
							content.append(string);
						}
						//将任务文件中的内容赋值给filePath属性
						note.setFilePath(content.toString());
					} catch (IOException e) {
						e.printStackTrace();
						new ServiceException("TaskServiceImpl.sync()出错！");
					}finally {
						if(bReader != null) {
							try {
								bReader.close();
							} catch (IOException e) {
								e.printStackTrace();
								new ServiceException("TaskServiceImpl.sync()出错！");
							}
						}
					}
					JSONObject jsonObject3 = JSONObject.fromObject(note);
					clientAddArray.add(jsonObject3);
				}
			}
			jObject.put("serverUpdateNoteArray", serverUpdateArray);
			jObject.put("serverAddNoteArray", serverAddArray);
			jObject.put("clientUpdateNoteArray", clientUpdateArray);
			jObject.put("clientAddNoteArray", clientAddArray);
			
			GroupService groupService = new GroupServiceImpl();
			JSONObject jsonObject2 = groupService.sync(jsonObject);
			jObject.put("serverUpdateGroupArray", jsonObject2.getJSONArray("serverUpdateGroupArray"));
			jObject.put("serverAddGroupArray", jsonObject2.getJSONArray("serverAddGroupArray"));
			jObject.put("clientUpdateGroupArray", jsonObject2.getJSONArray("clientUpdateGroupArray"));
			jObject.put("clientAddGroupArray", jsonObject2.getJSONArray("clientAddGroupArray"));
		}

		return jObject;
	}

	@Override
	public boolean delete(JSONObject jsonObject) throws ServiceException {
		JSONArray jsonArray = jsonObject.getJSONArray("noteIdList");
		if(jsonArray == null) {
			return false;
		}
		List<String> noteIdList = new ArrayList<>();
		for (Object object : jsonArray) {
			noteIdList.add((String)object);
		}
		int num = noteDao.delete(noteIdList);
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public JSONObject findNote(JSONObject jsonObject) throws ServiceException {
		List<Note> notes = null;
		String number = jsonObject.getString("number");
		if(number != null && !"".equals(number)) {
			switch(jsonObject.getString("noteType")) {
			case "all":
				notes = noteDao.findAllNoteByNumber(number);
				break;
			case "group":
				String groupId = jsonObject.getString("groupId");
				notes = noteDao.findNoteByNumberAndGroup(number,groupId);
				break;
			case "title":
				String title = jsonObject.getString("title");
				notes = noteDao.findNoteByTitle("%%"+title+"%%");
			default:
				
			}
		}
		JSONArray jsonArray = Tools.ListToJsonArray(notes);
		JSONObject jObject = new JSONObject();
		jObject.put("noteList", jsonArray);
		
		return jObject;
	}

}
