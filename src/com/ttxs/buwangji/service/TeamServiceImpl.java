/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.ttxs.buwangji.bean.Relation;
import com.ttxs.buwangji.bean.Team;
import com.ttxs.buwangji.dao.RelationDao;
import com.ttxs.buwangji.dao.TeamDao;
import com.ttxs.buwangji.exception.ServiceException;
import com.ttxs.buwangji.socket.OnlineSocket;
import com.ttxs.buwangji.utils.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author yc
 *
 */
public class TeamServiceImpl implements TeamService {
	SqlSession session;
	TeamDao teamDao;
	RelationDao relationDao;
	
	public TeamServiceImpl() {
		session = Tools.getSession();
		teamDao = session.getMapper(TeamDao.class);
		relationDao = session.getMapper(RelationDao.class);
	}
	

	@Override
	public boolean add(JSONObject jsonObject) {
		int num = 0;
		try {
			String number = jsonObject.getString("number");
			String name = jsonObject.getString("name");
			if(number == null || "".equals(number) || name == null || "".equals(name)) {
				return false;
			}
			
			String nowTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			Team team = new Team(Tools.getUUID(),name,number,nowTime);
			
			num = teamDao.add(team);
			session.commit();
		}catch (Exception e) {
			e.printStackTrace();
			new ServiceException("TeamServiceImpl.add��������");
		}
		
		if(num > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean delete(JSONObject jsonObject) {
		String teamId = jsonObject.getString("teamId");
		if(teamId == null || "".equals(teamId)) {
			return false;
		}
		
		int num1 = teamDao.delete(teamId);
		int num2 = relationDao.deletetAllRelation(teamId);
		session.commit();
		if(num1 > 0 && num2 > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateName(JSONObject jsonObject) {
		int num = 0; 
		try {
			String teamId = jsonObject.getString("teamId");
			String name = jsonObject.getString("name");
			//�û�Ҫ�޸�Team��
			if(teamId !=null && !"".equals(teamId) && name != null && !"".equals(name)) {
				num = teamDao.updateName(teamId,name);
				session.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			new ServiceException("TeamServiceImpl.updateName��������");
		}
		
		if(num > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean updateNickName(JSONObject jsonObject) {
		int num = 0; 
		try {
			String number = jsonObject.getString("number");
			String teamId = jsonObject.getString("teamId");
			String nickName ="";
			//�û�Ҫ�޸���Team�е��ǳ�
			if((nickName = jsonObject.getString("nickName")) !=null) {
				num = relationDao.updateNickName(number,teamId,nickName);
				session.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			new ServiceException("TeamServiceImpl.updateNickName��������");
		}
		
		if(num > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public JSONObject findTeamById(JSONObject jsonObject) {
		String teamId = jsonObject.getString("teamId");
		if(teamId == null || "".equals(teamId)) {
			return null;
		}
		Team team =  teamDao.findTeamById(teamId);
		return JSONObject.fromObject(team);
	}
	
	@Override
	public JSONObject findAllTeamByNumber(JSONObject jsonObject) {
		String number = jsonObject.getString("number");
		if(number == null || "".equals(number)) {
			return null;
		}
		List<Relation> relations =  relationDao.findRelationByNumber(number);
		JSONArray jsonArray = new JSONArray();
		Team team = null;
		if(!relations.isEmpty()) {
			for (Relation relation : relations) {
				team = teamDao.findTeamById(relation.getTeamId());
				if(team != null) {
					team.setStatus(relation.getStatus());
					team.setNickName(relation.getNickName());
					JSONObject jObject = JSONObject.fromObject(team);
					jsonArray.add(jObject);
				}
			}
		}
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("teamArray", jsonArray);
		return jsonObject2;
	}

	@Override
	public boolean joinTeam(JSONObject jsonObject) {
		int num = 0;
		String number = jsonObject.getString("number");
		String teamId = jsonObject.getString("teamId");
		String nickName = jsonObject.getString("nickName");
		if(teamId == null || "".equals(teamId) || number == null || "".equals(number)) {
			return false;
		}
		if(nickName == null || "".equals(nickName)) {
			nickName = Tools.getUUID();
		}
		
		String nowTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		Relation relation = new Relation(Tools.getUUID(),number,teamId,nickName,nowTime);
		num = relationDao.add(relation);
		session.commit();
		if(num > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean exitTeam(JSONObject jsonObject) throws ServiceException {
		String number = jsonObject.getString("number");
		String teamId = jsonObject.getString("teamId");
		if(teamId == null || "".equals(teamId) || number == null || "".equals(number)) {
			return false;
		}
		
		int num = relationDao.deleteRelation(number,teamId);
		session.commit();
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean screenTeam(JSONObject jsonObject) throws ServiceException {
		String number = jsonObject.getString("number");
		String teamId = jsonObject.getString("teamId");
		if(teamId == null || "".equals(teamId) || number == null || "".equals(number)) {
			return false;
		}
		
		int num = relationDao.updateStatusRelationTo0(number,teamId);
		session.commit();
		if(num > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean unScreenTeam(JSONObject jsonObject) throws ServiceException {
		String number = jsonObject.getString("number");
		String teamId = jsonObject.getString("teamId");
		if(teamId == null || "".equals(teamId) || number == null || "".equals(number)) {
			return false;
		}
		
		int num = relationDao.updateStatusRelationTo1(number,teamId);
		session.commit();
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean send(JSONObject jsonObject) throws ServiceException {
		String receiverTeamId = jsonObject.getString("receiverTeamId");
		if(receiverTeamId != null && "".equals(receiverTeamId)) {
			List<String> userNumbers = relationDao.findAllUserNumberById(receiverTeamId);
			TaskService taskService = new TaskServiceImpl();
			BufferedWriter bWriter = null;
			for(String userNumber : userNumbers) {
				try {
					//�Ƚ�������Ϣ�浽���������ݿ�
					if(taskService.add(jsonObject)){
						//�������û��� �����б���
						if(OnlineSocket.onlineSockets.containsKey(userNumber)) {
							//��ȡ�����ߵ��׽��������
							bWriter = OnlineSocket.onlineSockets.get(userNumber);
							if(bWriter != null) {
								//��������Ϣת�������û�
								bWriter.write(jsonObject.toString()+"\n");
								bWriter.flush();
							}else { // bWriterΪnull�������˺Ŵ������б����Ƴ�
								OnlineSocket.onlineSockets.remove(userNumber);
							}
						}
					}
				}catch (IOException e) {	//�����쳣��˵���ͻ��˶Ͽ����ӣ�������
					e.printStackTrace();
					//�����˺Ŵ������б����Ƴ�
					OnlineSocket.onlineSockets.remove(userNumber,bWriter);
					try {
						bWriter.close();
					} catch (IOException e1) {
						e1.printStackTrace();
						new ServiceException("TeamServiceImpl.send��������");
					}
					continue;
				}
			}
			return true;
		}
		return false;
	}


}
