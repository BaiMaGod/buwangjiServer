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
		//获取客户端传来的笔记列表，批量添加
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
		//同步操作，分为两步，第一步只上传头部
		// 		上传的头部，包含客户端本地每个笔记的id和状态码，与数据库对比，
		//		如果相同id的任务，本地笔记的状态码大，说明该笔记在本地被修改，需要修改服务器数据库中的内容，
		//		如果服务器数据库中的状态码大，则将此笔记内容发送到客户端，让客户端本地修改笔记内容
		//		如果本地客户端存在服务器没有的笔记id，则通知本地客户端上传该任务全部内容，
		//		如果服务器数据库中存在本地客户端中没有的笔记id，则将此笔记内容发送到客户端
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
					//服务器数据库中的状态码大，则将此任务内容添加到clientUpdateArray
					if(serverStatus > clientStatus) {
						Group group = groupDao.findGroupById(noteId);
						JSONObject jsonObject3 = JSONObject.fromObject(group);
						clientUpdateGroupArray.add(jsonObject3);
						iterator.remove();
					}
					//本地任务的状态码大，则将此任务id添加到serverUpdateArray
					else if(serverStatus < clientStatus) {
						serverUpdateGroupArray.add(noteId);
						iterator.remove();
					}else {
						//无论状态码谁大谁小或者相等，都将此任务id从statusMap移除
						iterator.remove();
					}
				}
				//statusMap中没有该任务id,将该任务id添加到serverAddArray
				else {
					serverAddGroupArray.add(noteId);
					iterator.remove();
				}
				//此时statusMap中剩下的任务id，便是客户端没有的，将任务信息都添加到clientAddArray
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
