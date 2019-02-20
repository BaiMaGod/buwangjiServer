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
public interface NoteService {

	/**
	 * 笔记新增操作
	 * @param jsonObject
	 * @return 是否成功
	 */
	boolean add(JSONObject jsonObject)throws ServiceException;

	/**
	 * 笔记修改操作
	 * @param jsonObject
	 * @return
	 */
	boolean update(JSONObject jsonObject)throws ServiceException;

	/**
	 * 笔记同步操作
	 * @param jsonObject
	 * @return
	 */
	JSONObject sync(JSONObject jsonObject)throws ServiceException;

	/**
	 * @param jsonObject
	 * @return
	 */
	boolean delete(JSONObject jsonObject)throws ServiceException;

	/**
	 * @param jsonObject
	 * @return
	 */
	JSONObject findNote(JSONObject jsonObject)throws ServiceException;

}
