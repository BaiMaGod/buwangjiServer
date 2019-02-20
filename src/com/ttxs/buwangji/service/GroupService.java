/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.service;

import com.ttxs.buwangji.exception.ServiceException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Administrator
 *
 */
public interface GroupService {

	
	/**
	 * @param jsonObject
	 * @return
	 * @throws ServiceException
	 */
	boolean add(JSONObject jsonObject) throws ServiceException;

	/**
	 * @param jsonObject
	 * @return
	 * @throws ServiceException
	 */
	boolean update(JSONObject jsonObject) throws ServiceException;

	/**
	 * @param jsonObject
	 * @return
	 * @throws ServiceException
	 */
	boolean delete(JSONObject jsonObject) throws ServiceException;


	/**
	 * @param jsonObject
	 * @return
	 * @throws ServiceException
	 */
	JSONObject sync(JSONObject jsonObject) throws ServiceException;

	/**
	 * @param jsonObject
	 * @return
	 */
	JSONObject findGroup(JSONObject jsonObject)throws ServiceException;

}
