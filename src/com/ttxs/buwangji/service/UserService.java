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
	JSONObject login(JSONObject jsonObject) throws ServiceException;

	/**
	 * 注册操作
	 * @param number
	 * @param password
	 * @param name
	 * @return boolean值，是否成功
	 */
	boolean register(JSONObject jsonObject) throws ServiceException;

	/**
	 * 修改个人基本信息
	 * @param jsonObject
	 * @return Boolean值，是否成功
	 */
	boolean update(JSONObject jsonObject)throws ServiceException;
    /**
	   * 修改账号，通过原账号邮箱验证，再输入新账号
	   * 目前未实现
	 * @param jsonObject
	 * @return Boolean值，是否成功
	 */
	boolean updateNumber(JSONObject jsonObject)throws ServiceException;
	/**
	 * 修改密码，两种方式，第一种：输入旧密码，改成新密码；第二种：邮箱验证（服务器给该邮箱发个链接，让其输入新密码）
	 * 目前只支持第第一种
	 * @param jsonObject
	 * @return Boolean值，是否成功
	 */
	boolean updatePassword(JSONObject jsonObject)throws ServiceException;

}
