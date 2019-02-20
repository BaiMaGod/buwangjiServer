/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.dao;

import java.util.List;
import java.util.Map;

import com.ttxs.buwangji.bean.Note;

/**
 * @author Administrator
 *
 */
public interface NoteDao {

	/**
	 * ͨ��id��ѯ�����ļ���·��
	 * @param id
	 * @return String��filePath
	 */
	String findFilePathById(String id);

	/**
	* ��ѯ���˺ŵ����бʼǵ�״̬��
	* @param number
	* @return Map<String, Integer>,id��״̬��
	*/
	Map<String, Integer> findStatusCodeByNumber(String number);

	/**
	 * ����������ݿ�,�������
	 * @return intֵ����ʾ����Ӱ���������0��ʾ����ʧ��
	 */
	int add(List<Note> noteList);
	
	/**
	 * ����������Ϣ����������
	 * @return intֵ����ʾ����Ӱ���������0��ʾ����ʧ��
	 */
	int update(List<Note> noteList);

	/**
	 * @param noteId
	 * @return
	 */
	Note findNoteById(String noteId);

	/**
	 * @param number
	 * @return
	 */
	List<Note> findAllNoteByNumber(String number);

	/**
	 * @param number
	 * @param groupId
	 * @return
	 */
	List<Note> findNoteByNumberAndGroup(String number, String groupId);

	/**
	 * @param noteIdList
	 * @return
	 */
	int delete(List<String> noteIdList);
	
}
