/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.bean;

/**
 * @author Administrator
 *
 */
public class Relation {
	private String id;
	private String userNumber;
	private String teamId;
	private String nickName;
	private int status;
	private String createTime;
	private String updateTime;
	
	
	
	
	public Relation() {
		super();
	}
	public Relation(String id, String userNumber, String teamId, String nickName, String createTime) {
		super();
		this.id = id;
		this.userNumber = userNumber;
		this.teamId = teamId;
		this.nickName = nickName;
		this.createTime = createTime;
		this.status = 1;
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
	 * @return the userNumber
	 */
	public String getUserNumber() {
		return userNumber;
	}
	/**
	 * @param userNumber the userNumber to set
	 */
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	/**
	 * @return the teamId
	 */
	public String getTeamId() {
		return teamId;
	}
	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	
	
}
