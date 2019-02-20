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
public interface UserService {

	/**
	 * 登录操作
	 * @param number
	 * @param password
	 * @return boolean值，是否成功
	 */
	boolean login(JSONObject jsonObject) throws ServiceException;

	/**
	 * 注册操作
	 * @param number
	 * @param password
	 * @param name
	 * @return boolean值，是否成功
	 */
	boolean register(JSONObject jsonObject) throws ServiceException;

	/**
	 * 修改个人信息
	 * @param jsonObject
	 * @return Boolean值，是否成功
	 */
	boolean update(JSONObject jsonObject)throws ServiceException;

}
