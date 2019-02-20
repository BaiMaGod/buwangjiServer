/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.service;

import com.ttxs.buwangji.exception.ServiceException;

import net.sf.json.JSONObject;

/**
 * @author yc
 *
 */
public interface TeamService {

	/**
	 * 新增Team
	 * @param jsonObject
	 * @return
	 */
	boolean add(JSONObject jsonObject)	throws ServiceException;

	/**
	 * 修改Team名
	 * @param jsonObject
	 * @return
	 */
	boolean updateName(JSONObject jsonObject)throws ServiceException;

	/**
	 * 删除Team
	 * @param jsonObject
	 * @return
	 */
	boolean delete(JSONObject jsonObject)throws ServiceException;

	/**
	 * 查询Team
	 * @param jsonObject
	 * @return
	 */
	JSONObject findTeamById(JSONObject jsonObject)throws ServiceException;

	/**
	 * 退出Team
	 * @param jsonObject
	 * @return
	 */
	boolean exitTeam(JSONObject jsonObject)throws ServiceException;

	/**
	 * 屏蔽Team的一切任务
	 * @param jsonObject
	 * @return
	 */
	boolean screenTeam(JSONObject jsonObject)throws ServiceException;

	/**
	 * 解除屏蔽
	 * @param jsonObject
	 * @return
	 * @throws ServiceException
	 */
	boolean unScreenTeam(JSONObject jsonObject) throws ServiceException;

	/**
	 * @param jsonObject
	 * @return
	 */
	JSONObject findAllTeamByNumber(JSONObject jsonObject)throws ServiceException;

	/**
	 * @param jsonObject
	 * @return
	 */
	boolean updateNickName(JSONObject jsonObject);

}
