/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.bean;

/**
 * @author Administrator
 *
 */
public class Team {
	private String id;
	private String name;
	private String teamImage;
	private String leaderNumber;
	private String createTime;
	private String updateTime;
	private String nickName;
	private int status;
	private String arg1;
	private String arg2;
	private String arg3;
	
	
	
	public Team() {
		super();
	}
	/**
	 * @param id
	 * @param name
	 * @param leaderNumber
	 * @param createTime
	 */
	public Team(String id, String name, String leaderNumber, String createTime) {
		super();
		this.id = id;
		this.name = name;
		this.leaderNumber = leaderNumber;
		this.createTime = createTime;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the teamImage
	 */
	public String getTeamImage() {
		return teamImage;
	}
	/**
	 * @param teamImage the teamImage to set
	 */
	public void setTeamImage(String teamImage) {
		this.teamImage = teamImage;
	}
	/**
	 * @return the leaderNumber
	 */
	public String getLeaderNumber() {
		return leaderNumber;
	}
	/**
	 * @param leaderNumber the leaderNumber to set
	 */
	public void setLeaderNumber(String leaderNumber) {
		this.leaderNumber = leaderNumber;
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

	
}
