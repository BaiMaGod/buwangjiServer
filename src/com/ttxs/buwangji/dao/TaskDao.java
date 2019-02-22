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
	 * 通过ID查询任务
	 * @param id
	 * @return Task，任务对象
	 */
	Task findTaskById(String id);

	/**
	 * 查找账号的所有任务
	 * @param number，账号
	 * @return List<Task>，任务列表
	 */
	List<Task> findAllMyTaskByNumber(String number);

	/**
	 * 查找账号所有发送给别人的任务
	 * @param number，账号
	 * @return List<Task>，任务列表
	 */
	List<Task> findAllSendTaskByNumber(String number);

	/**
	 * 查找账号所有接收到的任务
	 * @param number，账号
	 * @return List<Task>，任务列表
	 */
	List<Task> findAllReceiverTaskByNumber(String number);

	/**
	 * 查找账号的所有自己给自己创建的任务
	 * @param number，账号
	 * @return List<Task>，任务列表
	 */
	List<Task> findAllOwnTaskByNumber(String number);

	/**
	 * 查找账号所有创建的任务
	 * @param number，账号
	 * @return List<Task>，任务列表
	 */
	List<Task> findAllCreateTaskByNumber(String number);

	/**
	* 查询该账号的所有任务的状态码
	* @param number
	* @return Map<String, Integer>,id和状态码
	*/
	Map<String, Integer> findStatusCodeByNumber(String number);
	
	/**
	 * 通过id查询任务文件的路径
	 * @param id
	 * @return String：filePath
	 */
	String findFilePathById(String id);
	
	/**
	 * 添加任务到数据库,批量添加
	 * @return int值，表示操作影响的条数，0表示操作失败
	 */
	int add(List<Task> taskList);
	
	/**
	 * 更新任务信息，批量更新
	 * @return int值，表示操作影响的条数，0表示操作失败
	 */
	int update(List<Task> taskList);
	
	/**
	 * 删除任务信息，批量删除
	 * @return int值，表示操作影响的条数，0表示操作失败
	 */
	int delete(List<String> idList);

}
