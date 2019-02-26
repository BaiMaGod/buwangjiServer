/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.ttxs.buwangji.bean.Group;
import com.ttxs.buwangji.dao.GroupDao;
import com.ttxs.buwangji.exception.ServiceException;
import com.ttxs.buwangji.utils.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Administrator
 *
 */
public class GroupServiceImpl implements GroupService {
	SqlSession session;
	GroupDao groupDao;
	
	public GroupServiceImpl() {
		session = Tools.getSession();
		groupDao = session.getMapper(GroupDao.class);
	}

	@Override
	public boolean add(JSONObject jsonObject) throws ServiceException {
		//��ȡ�ͻ��˴����ıʼ��б��������
		List<JSONObject> list = Tools.JsonArrayToList(jsonObject.getJSONArray("noteList"));
		if(list == null || list.isEmpty()) {
			return false;
		}
		List<Group> groupList = new ArrayList<>();
		Group group = null;
		for (JSONObject jObject : list) {
			String groupId = jObject.getString("groupId");
			String userNumber = jObject.getString("userNumber");
			String parentGroupId = jObject.getString("parentGroupIds");
			String groupName = jObject.getString("groupName");
			String createTime = jObject.getString("createTime");
			String updateTime = jObject.getString("updateTime");
			int status = jObject.getInt("status");
			
			group = new Group();
			group.setId(groupId);
			group.setUserNumber(userNumber);
			group.setParentGroupId(parentGroupId);
			group.setGroupName(groupName);
			group.setCreateTime(createTime);
			group.setUpdateTime(updateTime);
			group.setStatus(status);			
			groupList.add(group);
		}
		int num = groupDao.add(groupList);
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
		List<Group> noteList = new ArrayList<>();
		Group note = null;
		for (JSONObject jObject : list) {
			String groupId = jObject.getString("groupId");
			String parentGroupId = jObject.getString("parentGroupIds");
			String groupName = jObject.getString("groupName");
			String updateTime = jObject.getString("updateTime");
			
			note = new Group();
			note.setId(groupId);
			if(parentGroupId != null){
				note.setParentGroupId(parentGroupId);
			}
			if(groupName != null){
				note.setGroupName(groupName);
			}
			if(updateTime != null) {
				note.setUpdateTime(updateTime);
			}
			if(jObject.containsKey("status")) {
				int status = jObject.getInt("status");
				note.setStatus(status+1);
			}
			noteList.add(note);
		}
		int num = groupDao.update(noteList);
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

		JSONArray jsonArray = jsonObject.getJSONArray("groupSyncList");
		JSONArray serverUpdateGroupArray = new JSONArray();
		JSONArray serverAddGroupArray = new JSONArray();
		JSONArray clientUpdateGroupArray = new JSONArray();
		JSONArray clientAddGroupArray = new JSONArray();
		String number = jsonObject.getString("number");
		Map<String, Integer> statusMap = groupDao.findStatusCodeByNumber(number);
		if(jsonArray != null) {
			for (Iterator<?> iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject jsonObject2 = (JSONObject) iterator.next();
				
				String noteId = jsonObject2.getString("taskId");
				if(statusMap.containsKey(noteId)) {
					int serverStatus = statusMap.get(noteId);
					int clientStatus = jsonObject2.getInt("status");
					//���������ݿ��е�״̬����򽫴�����������ӵ�clientUpdateArray
					if(serverStatus > clientStatus) {
						Group group = groupDao.findGroupById(noteId);
						JSONObject jsonObject3 = JSONObject.fromObject(group);
						clientUpdateGroupArray.add(jsonObject3);
						iterator.remove();
					}
					//���������״̬����򽫴�����id��ӵ�serverUpdateArray
					else if(serverStatus < clientStatus) {
						serverUpdateGroupArray.add(noteId);
						iterator.remove();
					}else {
						//����״̬��˭��˭С������ȣ�����������id��statusMap�Ƴ�
						iterator.remove();
					}
				}
				//statusMap��û�и�����id,��������id��ӵ�serverAddArray
				else {
					serverAddGroupArray.add(noteId);
					iterator.remove();
				}
				//��ʱstatusMap��ʣ�µ�����id�����ǿͻ���û�еģ���������Ϣ����ӵ�clientAddArray
				for (String id : statusMap.keySet()) {
					Group group = groupDao.findGroupById(id);
					JSONObject jsonObject3 = JSONObject.fromObject(group);
					clientAddGroupArray.add(jsonObject3);
				}
			}
			jObject.put("serverUpdateGroupArray", serverUpdateGroupArray);
			jObject.put("serverAddGroupArray", serverAddGroupArray);
			jObject.put("clientUpdateGroupArray", clientUpdateGroupArray);
			jObject.put("clientAddGroupArray", clientAddGroupArray);
			
		}

		return jObject;
	}

	@Override
	public boolean delete(JSONObject jsonObject) throws ServiceException {
		JSONArray jsonArray = jsonObject.getJSONArray("groupIdList");
		if(jsonArray == null) {
			return false;
		}
		List<String> groupIdList = new ArrayList<>();
		for (Object object : jsonArray) {
			groupIdList.add((String)object);
		}
		int num = groupDao.delete(groupIdList);
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public JSONObject findGroup(JSONObject jsonObject) throws ServiceException {
		List<Group> groups = null;
		String number = jsonObject.getString("number");
		if(number != null && !"".equals(number)) {
			switch(jsonObject.getString("noteType")) {
			case "all":
				groups = groupDao.findAllGroupByNumber(number);
				break;
//			case "group":
//				String groupId = jsonObject.getString("groupId");
//				groups = groupDao.findGroupByNumberAndGroup(number,groupId);
//				break;
			default:
				
			}
		}
		JSONArray jsonArray = Tools.ListToJsonArray(groups);
		JSONObject jObject = new JSONObject();
		jObject.put("groupList", jsonArray);
		
		return jObject;
	}


}
