/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.dao;

import java.util.List;

import com.ttxs.buwangji.bean.Team;

/**
 * @author yc
 *
 */
public interface TeamDao {
	
	/**
	 * ͨ��teamId��ѯTeam��¼
	 * @param teamId
	 * @return Team����
	 */
	Team findTeamById(String teamId);

	/**
	 * ͨ���ӳ��˺Ų�ѯTeam
	 * @param number
	 * @return List<Team>
	 */
	List<Team> findTeamByNumber(String number);
	
	/**
	 * ����Team��¼
	 * @param team����
	 * @return intֵ����ʾ����Ӱ���������0��ʾ����ʧ��
	 */
	int add(Team team);

	
	/**
	 * ɾ��Team
	 * @param teamId
	 * @return intֵ����ʾ����Ӱ���������0��ʾ����ʧ��
	 */
	int delete(String teamId);

	/**
	 * �޸�Team��
	 * @param taemId 
	 * @param name
	 * @return intֵ����ʾ����Ӱ���������0��ʾ����ʧ��
	 */
	int updateName(String taemId, String name);

	
}
