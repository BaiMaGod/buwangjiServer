/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.dao;

import java.util.List;
import java.util.Map;

import com.ttxs.buwangji.bean.Task;

/**
 * @author Administrator
 *
 */
public interface TaskDao {
	
	/**
	 * ͨ��ID��ѯ����
	 * @return Task���������
	 */
	Task findTaskById(String id);

	/**
	 * �����˺ŵ���������
	 * @param number���˺�
	 * @return List<Task>�������б�
	 */
	List<Task> findAllMyTaskByNumber(String number);

	/**
	 * �����˺����з��͸����˵�����
	 * @param number���˺�
	 * @return List<Task>�������б�
	 */
	List<Task> findAllSendTaskByNumber(String number);

	/**
	 * �����˺����н��յ�������
	 * @param number���˺�
	 * @return List<Task>�������б�
	 */
	List<Task> findAllReceiverTaskByNumber(String number);

	/**
	 * �����˺ŵ������Լ����Լ�����������
	 * @param number���˺�
	 * @return List<Task>�������б�
	 */
	List<Task> findAllOwnTaskByNumber(String number);

	/**
	 * �����˺����д���������
	 * @param number���˺�
	 * @return List<Task>�������б�
	 */
	List<Task> findAllCreateTaskByNumber(String number);

	/**
	* ��ѯ���˺ŵ����������״̬��
	* @param number
	* @return Map<String, Integer>,id��״̬��
	*/
	Map<String, Integer> findStatusCodeByNumber(String number);
	
	/**
	 * ͨ��id��ѯ�����ļ���·��
	 * @param id
	 * @return String��filePath
	 */
	String findFilePathById(String id);
	
	/**
	 * ����������ݿ�,�������
	 * @return intֵ����ʾ����Ӱ���������0��ʾ����ʧ��
	 */
	int add(List<Task> taskList);
	
	/**
	 * ����������Ϣ����������
	 * @return intֵ����ʾ����Ӱ���������0��ʾ����ʧ��
	 */
	int update(List<Task> taskList);
	
	/**
	 * ɾ��������Ϣ������ɾ��
	 * @return intֵ����ʾ����Ӱ���������0��ʾ����ʧ��
	 */
	int delete(List<String> idList);

}
