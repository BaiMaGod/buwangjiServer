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
	 * 添加任务到数据库,批量添加
	 * @return int值，表示操作影响的条数，0表示操作失败
	 */
	int add(List<Note> noteList);
	
	/**
	 * 更新任务信息，批量更新
	 * @return int值，表示操作影响的条数，0表示操作失败
	 */
	int update(List<Note> noteList);
	
	/**
	 * 批量删除笔记
	 * @param noteIdList
	 * @return
	 */
	int delete(List<String> noteIdList);

	/**
	 * 通过id查询任务文件的路径
	 * @param id
	 * @return String：filePath
	 */
	String findFilePathById(String id);
	
	/**
	 * 查询该账号的所有笔记的状态码
	 * @param number
	 * @return Map<String, Integer>,id和状态码
	 */
	Map<String, Integer> findStatusCodeByNumber(String number);
	
	/**
	 * 通过ID查找笔记
	 * @param noteId
	 * @return
	 */
	Note findNoteById(String noteId);

	/**
	 * 通过账号查询笔记
	 * @param number
	 * @return
	 */
	List<Note> findAllNoteByNumber(String number);

	/**
	 * 通过账号和分组查询笔记
	 * @param number
	 * @param groupId
	 * @return
	 */
	List<Note> findNoteByNumberAndGroup(String number, String groupId);


	/**
	 * 通过标题名查找笔记(模糊查询）
	 * @param string
	 * @return
	 */
	List<Note> findNoteByTitle(String title);
	
}
