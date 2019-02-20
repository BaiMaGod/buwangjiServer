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
	 * �ʼ���������
	 * @param jsonObject
	 * @return �Ƿ�ɹ�
	 */
	boolean add(JSONObject jsonObject)throws ServiceException;

	/**
	 * �ʼ��޸Ĳ���
	 * @param jsonObject
	 * @return
	 */
	boolean update(JSONObject jsonObject)throws ServiceException;

	/**
	 * �ʼ�ͬ������
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
