/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.dao;

import java.util.List;
import java.util.Map;

import com.ttxs.buwangji.bean.Group;

/**
 * @author Administrator
 *
 */
public interface GroupDao {

	/**
	 * @param groupList
	 * @return
	 */
	int add(List<Group> groupList);

	/**
	 * @param noteList
	 * @return
	 */
	int update(List<Group> noteList);

	/**
	 * @param number
	 * @return
	 */
	Map<String, Integer> findStatusCodeByNumber(String number);


	/**
	 * @param noteId
	 * @return
	 */
	Group findGroupById(String noteId);

	/**
	 * @param groupIdList
	 * @return
	 */
	int delete(List<String> groupIdList);

	/**
	 * @param number
	 * @return
	 */
	List<Group> findAllGroupByNumber(String number);

	/**
	 * @param number
	 * @param groupId
	 * @return
	 */
	List<Group> findGroupByNumberAndGroup(String number, String groupId);

}
