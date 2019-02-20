/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.service;

import com.ttxs.buwangji.exception.ServiceException;
import net.sf.json.JSONObject;

/**
 * @author Administrator
 *
 */
public interface TaskService {

	/**
	 * 任务添加
	 * @param jsonObject
	 * @return boolean值，是否操作成功
	 */
	boolean add(JSONObject jsonObject)throws ServiceException;

	/**
	 * 任务修改
	 * @param jsonObject
	 * @return boolean值，是否操作成功
	 */
	boolean update(JSONObject jsonObject)throws ServiceException;

	/**
	 * 任务同步
	 * @param jsonObject
	 * @return boolean值，是否操作成功
	 */
	JSONObject sync(JSONObject jsonObject)throws ServiceException;

	/**
	 * 发送任务
	 * @param jsonObject
	 * @return boolean值，是否操作成功
	 */
	boolean send(JSONObject jsonObject)throws ServiceException;

	/**
	 * 查询任务，包括了所有类别的查询
	 * @param jObject
	 * @return json对象
	 * @throws ServiceException
	 */
	JSONObject findTask(JSONObject jObject) throws ServiceException;

	/**
	 * 删除任务
	 * @param jsonObject
	 * @return jsonObject
	 */
	boolean delete(JSONObject jsonObject)throws ServiceException;

}
