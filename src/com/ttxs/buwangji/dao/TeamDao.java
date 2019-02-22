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
	 * 通过teamId查询Team记录
	 * @param teamId
	 * @return Team对象
	 */
	Team findTeamById(String teamId);

	/**
	 * 通过队长账号查询Team
	 * @param number
	 * @return List<Team>
	 */
	List<Team> findTeamByNumber(String number);
	
	/**
	 * 新增Team记录
	 * @param team对象
	 * @return int值，表示操作影响的条数，0表示操作失败
	 */
	int add(Team team);

	
	/**
	 * 删除Team
	 * @param teamId
	 * @return int值，表示操作影响的条数，0表示操作失败
	 */
	int delete(String teamId);

	/**
	 * 修改Team名
	 * @param taemId 
	 * @param name
	 * @return int值，表示操作影响的条数，0表示操作失败
	 */
	int updateName(String taemId, String name);

	
}
