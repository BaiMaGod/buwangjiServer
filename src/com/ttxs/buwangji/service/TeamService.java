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
	 * ����Team
	 * @param jsonObject
	 * @return
	 */
	boolean add(JSONObject jsonObject)	throws ServiceException;
	
	/**
	 * ɾ��Team
	 * @param jsonObject
	 * @return
	 */
	boolean delete(JSONObject jsonObject)throws ServiceException;

	/**
	 * �޸�Team��
	 * @param jsonObject
	 * @return
	 */
	boolean updateName(JSONObject jsonObject)throws ServiceException;

	/**
	 * ͨ��id��ѯ����Team��¼
	 * @param jsonObject
	 * @return JSONObject
	 */
	JSONObject findTeamById(JSONObject jsonObject)throws ServiceException;
	
	/**
	 * ͨ���˺Ų�ѯ���м����Team
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
	 * �˳�Team
	 * @param jsonObject
	 * @return
	 */
	boolean exitTeam(JSONObject jsonObject)throws ServiceException;

	/**
	 * ����Team��һ������
	 * @param jsonObject
	 * @return
	 */
	boolean screenTeam(JSONObject jsonObject)throws ServiceException;

	/**
	 * �������
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
	 * ��������
	 * @param jsonObject
	 * @return booleanֵ���Ƿ�����ɹ�
	 */
	boolean send(JSONObject jsonObject)throws ServiceException;

}
