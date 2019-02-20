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
	 * ��¼����
	 * @param number
	 * @param password
	 * @return booleanֵ���Ƿ�ɹ�
	 */
	boolean login(JSONObject jsonObject) throws ServiceException;

	/**
	 * ע�����
	 * @param number
	 * @param password
	 * @param name
	 * @return booleanֵ���Ƿ�ɹ�
	 */
	boolean register(JSONObject jsonObject) throws ServiceException;

	/**
	 * �޸ĸ�����Ϣ
	 * @param jsonObject
	 * @return Booleanֵ���Ƿ�ɹ�
	 */
	boolean update(JSONObject jsonObject)throws ServiceException;

}
