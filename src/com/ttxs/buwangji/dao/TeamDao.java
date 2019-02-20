/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.dao;

import java.util.List;

import com.ttxs.buwangji.bean.Team;

/**
 * @author Administrator
 *
 */
public interface TeamDao {

	/**
	 * @param team
	 * @return
	 */
	int add(Team team);
	
	/**
	 * @param teamId
	 * @return
	 */
	Team findTeamById(String teamId);

	/**
	 * @param teamId
	 * @return
	 */
	List<Team> findTeamByNumber(String teamId);

	
	/**
	 * @param teamId
	 * @return
	 */
	int delete(String teamId);

	/**
	 * @param name2 
	 * @param team
	 * @return
	 */
	int updateName(String taemId, String name);

	
}
