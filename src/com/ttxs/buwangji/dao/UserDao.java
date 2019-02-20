/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.dao;

import java.util.List;

import com.ttxs.buwangji.bean.User;

/**
 * @author Administrator
 *
 */
public interface UserDao {
	List<User> findAll();
	
	User findUserById(String id);
	List<User> findUserByName(String name);
	User findUserByNumber(String number);
	
	String findPasswordByNumber(String number);
	
	
	int add(User user);
	int update(User user);
	int delete(String number);

	void updateLoginTime(String number, String loginTime);

	int updateNumber(String number, String newNumber);

	int updatePassword(String number, String password);
}
