/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.ibatis.session.SqlSession;

import com.ttxs.buwangji.bean.Task;
import com.ttxs.buwangji.dao.TaskDao;
import com.ttxs.buwangji.exception.ServiceException;
import com.ttxs.buwangji.socket.OnlineSocket;
import com.ttxs.buwangji.utils.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Administrator
 *
 */
public class TaskServiceImpl implements TaskService {
	SqlSession session;
	TaskDao taskDao;
	
	public TaskServiceImpl() {
		session = Tools.getSession();
		taskDao = session.getMapper(TaskDao.class);
	}

	@Override
	public boolean add(JSONObject jsonObject) throws ServiceException {
		List<JSONObject> list = Tools.JsonArrayToList(jsonObject.getJSONArray("taskList"));
		if(list == null || list.isEmpty()) {
			return false;
		}
		List<Task> taskList = new ArrayList<>();
		Task task = null;
		for (JSONObject jObject : list) {
			if(jObject.containsKey("title")&&jObject.containsKey("content")&&jObject.containsKey("createTime")&&jObject.containsKey("endTime")&&jObject.containsKey("clockTime")&&jObject.containsKey("level")&&jObject.containsKey("creatorId")) {
				String title = jObject.getString("title");
				String content = jObject.getString("content");
				String createTime = jObject.getString("createTime");
				String endTime = jObject.getString("endTime");
				String clockTime = jObject.getString("clockTime");
				int level = jObject.getInt("level");
				String creatorNumber = jObject.getString("creatorNumber");
				
				String filePath = "F:\\com.buwangji.server\\Task\\"+creatorNumber+"\\"+createTime+".txt";
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
				
				task = new Task();
				task.setTitle(title);
				task.setFilePath(filePath);
				task.setCreateTime(createTime);
				task.setEndTime(endTime);
				task.setClockTime(clockTime);
				task.setLevel(level);			
				task.setCreatorNumber(creatorNumber);
				taskList.add(task);
			}
		}
		int num = taskDao.add(taskList);
		
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean update(JSONObject jsonObject) throws ServiceException {
		List<JSONObject> list = Tools.JsonArrayToList(jsonObject.getJSONArray("taskList"));
		if(list == null || list.isEmpty()) {
			return false;
		}
		List<Task> taskList = new ArrayList<>();
		Task task = null;
		for (JSONObject jObject : list) {
			String id = jObject.getString("id");
			String title = jObject.getString("title");
			String content = jObject.getString("content");
			String endTime = jObject.getString("endTime");
			String clockTime = jObject.getString("clockTime");
			
			task = new Task();
			task.setId(id);
			if(title != null){
				task.setTitle(title);
			}
			if(endTime != null) {
				task.setEndTime(endTime);
			}
			if(clockTime != null) {
				task.setClockTime(clockTime);
			}
			if(jObject.containsKey("level")) {
				int level = jObject.getInt("level");
				task.setLevel(level);
			}
			if(content != null) {
				String filePath = taskDao.findFilePathById(id);
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
			taskList.add(task);
		}
		int num = taskDao.update(taskList);
		
		if(num > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean delete(JSONObject jsonObject) throws ServiceException {
		JSONArray jsonArray = jsonObject.getJSONArray("taskIdList");
		if(jsonArray == null) {
			return false;
		}
		List<String> taskIdList = new ArrayList<>();
		for (Object objesct : jsonArray) {
			taskIdList.add((String)objesct);
		}
		int num = taskDao.delete(taskIdList);
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public JSONObject sync(JSONObject jsonObject) throws ServiceException {
		//ͬ����������Ϊ��������һ��ֻ�ϴ�ͷ��
		// 		�ϴ���ͷ���������ͻ��˱���ÿ�������id��״̬�룬�����ݿ�Աȣ�
		//		�����ͬid�����񣬱��������״̬���˵���������ڱ��ر��޸ģ���Ҫ�޸ķ��������ݿ��е����ݣ�
		//		������������ݿ��е�״̬����򽫴��������ݷ��͵��ͻ��ˣ��ÿͻ��˱����޸���������
		//		������ؿͻ��˴��ڷ�����û�е�����id����֪ͨ���ؿͻ����ϴ�������ȫ�����ݣ�
		//		������������ݿ��д��ڱ��ؿͻ�����û�е�����id���򽫴��������ݷ��͵��ͻ���
		JSONObject jObject = new JSONObject();

		JSONArray jsonArray = jsonObject.getJSONArray("syncList");
		JSONArray serverUpdateArray = new JSONArray();
		JSONArray serverAddArray = new JSONArray();
		JSONArray clientUpdateArray = new JSONArray();
		JSONArray clientAddArray = new JSONArray();
		String number = jsonObject.getString("number");
		Map<String, Integer> statusMap = taskDao.findStatusCodeByNumber(number);
		if(jsonArray != null) {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				String taskId = jsonObject2.getString("taskId");
				if(statusMap.containsKey(taskId)) {
					int serverStatus = statusMap.get(taskId);
					int clientStatus = jsonObject2.getInt("status");
					//���������ݿ��е�״̬����򽫴�����������ӵ�clientUpdateArray
					if(serverStatus > clientStatus) {
						Task task = taskDao.findTaskById(taskId);
						File file = new File(task.getFilePath());
						BufferedReader bReader = null ;
						try {
							bReader = new BufferedReader(new FileReader(file));
							StringBuilder content = new StringBuilder();
							String string = null;
							while((string=bReader.readLine())!=null) {
								content.append(string);
							}
							//�������ļ��е����ݸ�ֵ��filePath����
							task.setFilePath(content.toString());
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
						JSONObject jsonObject3 = JSONObject.fromObject(task);
						clientUpdateArray.add(jsonObject3);
						statusMap.remove(taskId);
					}
					//���������״̬����򽫴�����id��ӵ�serverUpdateArray
					else if(serverStatus < clientStatus) {
						serverUpdateArray.add(taskId);
						statusMap.remove(taskId);
					}else {
						//����״̬��˭��˭С������ȣ�����������id��statusMap�Ƴ�
						statusMap.remove(taskId);
					}
				}
				//statusMap��û�и�����id,��������id��ӵ�serverAddArray
				else {
					serverAddArray.add(taskId);
					statusMap.remove(taskId);
				}
				//��ʱstatusMap��ʣ�µ�����id�����ǿͻ���û�еģ���������Ϣ����ӵ�clientAddArray
				for (String id : statusMap.keySet()) {
					Task task = taskDao.findTaskById(id);
					File file = new File(task.getFilePath());
					BufferedReader bReader = null ;
					try {
						bReader = new BufferedReader(new FileReader(file));
						StringBuilder content = new StringBuilder();
						String string = null;
						while((string=bReader.readLine())!=null) {
							content.append(string);
						}
						//�������ļ��е����ݸ�ֵ��filePath����
						task.setFilePath(content.toString());
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
					JSONObject jsonObject3 = JSONObject.fromObject(task);
					clientAddArray.add(jsonObject3);
				}
			}
			jObject.put("serverUpdateArray", serverUpdateArray);
			jObject.put("serverAddArray", serverAddArray);
			jObject.put("clientUpdateArray", clientUpdateArray);
			jObject.put("clientAddArray", clientAddArray);
		}

		return jObject;
	}

	@Override
	public JSONObject findTask(JSONObject jObject) throws ServiceException {
		List<Task> tasks = null;
		String number = jObject.getString("number");
		if(number != null && !"".equals(number)) {
			switch(jObject.getString("taskType")) {
			case "myTask":
				tasks = taskDao.findAllMyTaskByNumber(number);
				break;
			case "sendTask":
				tasks = taskDao.findAllSendTaskByNumber(number);
				break;
			case "receiverTask":
				tasks = taskDao.findAllReceiverTaskByNumber(number);
				break;
			case "ownTask":
				tasks = taskDao.findAllOwnTaskByNumber(number);
				break;
			case "createTask":
				tasks = taskDao.findAllCreateTaskByNumber(number);
				break;
			}
		}
		JSONArray jsonArray = Tools.ListToJsonArray(tasks);
		jObject.put("taskList", jsonArray);
		
		return jObject;
	}

	
	

}
