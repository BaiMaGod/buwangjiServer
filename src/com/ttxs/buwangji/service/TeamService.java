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
	 * 删除Team
	 * @param jsonObject
	 * @return
	 */
	boolean delete(JSONObject jsonObject)throws ServiceException;

	/**
	 * 修改Team名
	 * @param jsonObject
	 * @return
	 */
	boolean updateName(JSONObject jsonObject)throws ServiceException;

	/**
	 * 通过id查询单个Team记录
	 * @param jsonObject
	 * @return JSONObject
	 */
	JSONObject findTeamById(JSONObject jsonObject)throws ServiceException;
	
	/**
	 * 通过账号查询所有加入的Team
	 * @param jsonObject
	 * @return JSONObject
	 */
	JSONObject findAllTeamByNumber(JSONObject jsonObject)throws ServiceException;
	
	
	
	/**
	 * @param jsonObject
	 * @return
	 */
	boolean joinTeam(JSONObject jsonObject);

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
	boolean updateNickName(JSONObject jsonObject);

	/**
	 * 发送任务
	 * @param jsonObject
	 * @return boolean值，是否操作成功
	 */
	boolean send(JSONObject jsonObject)throws ServiceException;

}
