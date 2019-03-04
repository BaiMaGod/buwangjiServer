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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import net.sf.json.util.NewBeanInstanceStrategy;

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
		//��ȡ�ͻ��˴����ıʼ��б��������
		List<JSONObject> list = Tools.JsonArrayToList(jsonObject.getJSONArray("noteList"));
		if(list == null || list.isEmpty()) {
			return false;
		}
		List<Note> noteList = new ArrayList<>();
		Note note = null;
		for (JSONObject jObject : list) {
			String noteId = jObject.getString("id");
			String userNumber = jObject.getString("userNumber");
			String groupId = jObject.getString("groupId");
			String title = jObject.getString("title");
			String content = jObject.getString("filePath");
			String createTime = jObject.getString("createTime");
			int status = jObject.getInt("status");
			int isSync = jObject.getInt("isSync");
			
			String nowTime = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
			nowTime = nowTime.replace("-", "");
			
			String filePath = "F:\\com.buwangji.server\\Note\\"+userNumber;
			File file = new File(filePath);
			try {
				file.mkdir();
				file = new File(filePath+"\\"+nowTime+".txt");
				file.createNewFile();
			
				//���ʼ��ļ��浽Ӳ����
				FileWriter fWriter = new FileWriter(file);
				fWriter.write(content);
				fWriter.flush();
				fWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
				new ServiceException("TaskServiceImpl.add()����");
			} 
			
			note = new Note();
			note.setId(noteId);
			note.setUserNumber(userNumber);
			note.setGroupId(groupId);
			note.setTitle(title);
			note.setFilePath(filePath);
			note.setCreateTime(createTime);
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
					//����ԭ�ļ��е�����
					FileWriter fWriter = new FileWriter(file);
					fWriter.write(content);
					fWriter.flush();
					fWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
					new ServiceException("TaskServiceImpl.update()����");
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
		//ͬ����������Ϊ��������һ��ֻ�ϴ�ͷ��
		// 		�ϴ���ͷ���������ͻ��˱���ÿ���ʼǵ�id��״̬�룬�����ݿ�Աȣ�
		//		�����ͬid�����񣬱��رʼǵ�״̬���˵���ñʼ��ڱ��ر��޸ģ���Ҫ�޸ķ��������ݿ��е����ݣ�
		//		������������ݿ��е�״̬����򽫴˱ʼ����ݷ��͵��ͻ��ˣ��ÿͻ��˱����޸ıʼ�����
		//		������ؿͻ��˴��ڷ�����û�еıʼ�id����֪ͨ���ؿͻ����ϴ�������ȫ�����ݣ�
		//		������������ݿ��д��ڱ��ؿͻ�����û�еıʼ�id���򽫴˱ʼ����ݷ��͵��ͻ���
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
					//���������ݿ��е�״̬����򽫴�����������ӵ�clientUpdateArray
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
							//�������ļ��е����ݸ�ֵ��filePath����
							note.setFilePath(content.toString());
						} catch (IOException e) {
							e.printStackTrace();
							new ServiceException("TaskServiceImpl.sync()����");
						}finally {
							if(bReader != null) {
								try {
									bReader.close();
								} catch (IOException e) {
									e.printStackTrace();
									new ServiceException("TaskServiceImpl.sync()����");
								}
							}
						}
						JSONObject jsonObject3 = JSONObject.fromObject(note);
						clientUpdateArray.add(jsonObject3);
						iterator.remove();
					}
					//���������״̬����򽫴�����id��ӵ�serverUpdateArray
					else if(serverStatus < clientStatus) {
						serverUpdateArray.add(noteId);
						iterator.remove();
					}else {
						//����״̬��˭��˭С������ȣ�����������id��statusMap�Ƴ�
						iterator.remove();
					}
				}
				//statusMap��û�и�����id,��������id��ӵ�serverAddArray
				else {
					serverAddArray.add(noteId);
					iterator.remove();
				}
				//��ʱstatusMap��ʣ�µ�����id�����ǿͻ���û�еģ���������Ϣ����ӵ�clientAddArray
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
						//�������ļ��е����ݸ�ֵ��filePath����
						note.setFilePath(content.toString());
					} catch (IOException e) {
						e.printStackTrace();
						new ServiceException("TaskServiceImpl.sync()����");
					}finally {
						if(bReader != null) {
							try {
								bReader.close();
							} catch (IOException e) {
								e.printStackTrace();
								new ServiceException("TaskServiceImpl.sync()����");
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
