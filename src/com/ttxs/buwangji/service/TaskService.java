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
	 * �������
	 * @param jsonObject
	 * @return booleanֵ���Ƿ�����ɹ�
	 */
	boolean add(JSONObject jsonObject)throws ServiceException;

	/**
	 * �����޸�
	 * @param jsonObject
	 * @return booleanֵ���Ƿ�����ɹ�
	 */
	boolean update(JSONObject jsonObject)throws ServiceException;

	/**
	 * ����ͬ��
	 * @param jsonObject
	 * @return booleanֵ���Ƿ�����ɹ�
	 */
	JSONObject sync(JSONObject jsonObject)throws ServiceException;


	/**
	 * ��ѯ���񣬰������������Ĳ�ѯ
	 * @param jObject
	 * @return json����
	 * @throws ServiceException
	 */
	JSONObject findTask(JSONObject jObject) throws ServiceException;

	/**
	 * ɾ������
	 * @param jsonObject
	 * @return jsonObject
	 */
	boolean delete(JSONObject jsonObject)throws ServiceException;

}
