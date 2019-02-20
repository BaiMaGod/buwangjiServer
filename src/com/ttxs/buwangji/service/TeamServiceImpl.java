/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.ttxs.buwangji.bean.Relation;
import com.ttxs.buwangji.bean.Team;
import com.ttxs.buwangji.dao.RelationDao;
import com.ttxs.buwangji.dao.TeamDao;
import com.ttxs.buwangji.exception.ServiceException;
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
			if(number == null  || name == null) {
				return false;
			}
			
			String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			Team team = new Team(Tools.getUUID(),name,number,now);
			
			num = teamDao.add(team);
			session.commit();
		}catch (Exception e) {
			e.printStackTrace();
			new ServiceException("TeamServiceImpl.add方法错误！");
		}
		
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateName(JSONObject jsonObject) {
		int num = 0; 
		try {
			String teamId = jsonObject.getString("teamId");
			String name ="";
			//用户要修改Team名
			if((name = jsonObject.getString("name")) !=null) {
				num = teamDao.updateName(teamId,name);
				session.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			new ServiceException("TeamServiceImpl.updateName方法错误！");
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
			//用户要修改在Team中的昵称
			if((nickName = jsonObject.getString("nickName")) !=null) {
				num = relationDao.updateNickName(number,teamId,nickName);
				session.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			new ServiceException("TeamServiceImpl.updateNickName方法错误！");
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
	public boolean exitTeam(JSONObject jsonObject) throws ServiceException {
		String number = jsonObject.getString("number");
		String teamId = jsonObject.getString("teamId");
		if(teamId == null || "".equals(teamId) || number == null || "".equals(number)) {
			return false;
		}
		
		int num = relationDao.deleteRelation(number,teamId);
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
		if(num > 0) {
			return true;
		}
		return false;
	}

}
