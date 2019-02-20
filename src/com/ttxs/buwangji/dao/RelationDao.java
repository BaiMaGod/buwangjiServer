/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.dao;

import java.util.List;

import com.ttxs.buwangji.bean.Relation;

/**
 * @author Administrator
 *
 */
public interface RelationDao {

	/**
	 * 删除用户与Team的关系记录
	 * @param number
	 * @param teamId
	 * @return
	 */
	int deleteRelation(String number, String teamId);

	/**
	 * 删除teamId对应的所有关系记录
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
	 * 修改关系的状态码为0
	 * @param number
	 * @param teamId
	 * @return
	 */
	int updateStatusRelationTo0(String number, String teamId);

	/**
	 * 修改关系的状态码为1
	 * @param number
	 * @param teamId
	 * @return
	 */
	int updateStatusRelationTo1(String number, String teamId);

	/**
	 * 查询该账号与Team的所有对应关系
	 * @param number
	 * @return
	 */
	List<Relation> findRelationByNumber(String number);



}
