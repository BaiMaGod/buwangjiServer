/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.bean;

/**
 * @author Administrator
 *
 */
public class User {
	private String id;
	private String number;
	private String name;
	private String password;
	private String headImage;
	private String createTime;
	private String loginTime;
	private String tel;
	private int vip;
	private String arg1;
	private String arg2;
	private String arg3;
	
	
	public User() {
		super();
	}

	public User(String id, String name, String number, String password, String createTime, int vip) {
		super();
		this.id = id;
		this.name = name;
		this.number = number;
		this.password = password;
		this.createTime = createTime;
		this.vip = vip;
	}
	
	
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", number=" + number + ", createTime=" + createTime + ", vip="
				+ vip + "]";
	}
	

	
	/**
	 * @return the headImage
	 */
	public String getHeadImage() {
		return headImage;
	}

	/**
	 * @param headImage the headImage to set
	 */
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
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
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the vip
	 */
	public int getVip() {
		return vip;
	}
	/**
	 * @param vip the vip to set
	 */
	public void setVip(int vip) {
		this.vip = vip;
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
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the loginTime
	 */
	public String getLoginTime() {
		return loginTime;
	}
	/**
	 * @param loginTime the loginTime to set
	 */
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	
}
