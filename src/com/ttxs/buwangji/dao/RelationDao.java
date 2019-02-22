/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.dao;

import java.util.List;

import com.ttxs.buwangji.bean.Relation;
import com.ttxs.buwangji.bean.Team;

/**
 * @author Administrator
 *
 */
public interface RelationDao {

	/**
	 * ��ӹ�ϵ
	 * @param team
	 * @return
	 */
	int add(Relation relation);
	
	/**
	 * ɾ���û���Team�Ĺ�ϵ��¼
	 * @param number
	 * @param teamId
	 * @return
	 */
	int deleteRelation(String number, String teamId);

	/**
	 * ɾ��teamId��Ӧ�����й�ϵ��¼
	 * @param teamId
	 * @return
	 */
	int deletetAllRelation(String teamId);
	
	/**
	 * @param number
	 * @param teamId
	 * @param nickName
	 * @return
	 */
	int updateNickName(String number, String teamId, String nickName);

	/**
	 * �޸Ĺ�ϵ��״̬��Ϊ0
	 * @param number
	 * @param teamId
	 * @return
	 */
	int updateStatusRelationTo0(String number, String teamId);

	/**
	 * �޸Ĺ�ϵ��״̬��Ϊ1
	 * @param number
	 * @param teamId
	 * @return
	 */
	int updateStatusRelationTo1(String number, String teamId);

	/**
	 * ��ѯ���˺���Team�����ж�Ӧ��ϵ
	 * @param number
	 * @return
	 */
	List<Relation> findRelationByNumber(String number);

	/**
	 * ��ѯ����TeamId�������û��˺�
	 * @param receiverTeamId
	 * @return
	 */
	List<String> findAllUserNumberById(String teamId);



}
