/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.bean;

/**
 * @author yc
 *
 */
public class Note {
	private String id;
	private String userNumber;
	private String groupId;
	private String filePath;
	private String title;
	private String createTime;
	private String updateTime;
	private int status;
	private int isSync;
	private String arg1;
	private String arg2;
	private String arg3;
	

	public Note() {
		super();
	}
	
	
	
	/**
	 * @param id
	 * @param userNumber
	 * @param groupId
	 * @param filePath
	 * @param title
	 * @param createTime
	 * @param status
	 * @param isSync
	 */
	public Note(String id, String userNumber, String groupId, String filePath, String title, String createTime,int status, int isSync) {
		super();
		this.id = id;
		this.userNumber = userNumber;
		this.groupId = groupId;
		this.filePath = filePath;
		this.title = title;
		this.createTime = createTime;
		this.status = status;
		this.isSync = isSync;
	}



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the userId
	 */
	public String getUserNumber() {
		return userNumber;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	/**
	 * @return the fileName
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the arg1
	 */
	public String getArg1() {
		return arg1;
	}
	/**
	 * @param arg1 the arg1 to set
	 */
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}
	/**
	 * @return the arg2
	 */
	public String getArg2() {
		return arg2;
	}
	/**
	 * @param arg2 the arg2 to set
	 */
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}
	/**
	 * @return the arg3
	 */
	public String getArg3() {
		return arg3;
	}
	/**
	 * @param arg3 the arg3 to set
	 */
	public void setArg3(String arg3) {
		this.arg3 = arg3;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the isSync
	 */
	public int getIsSync() {
		return isSync;
	}
	/**
	 * @param isSync the isSync to set
	 */
	public void setIsSync(int isSync) {
		this.isSync = isSync;
	}
	
	
}
