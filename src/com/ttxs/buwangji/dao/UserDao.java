/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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

	void updateLoginTime(@Param("number") String number, @Param("loginTime")String loginTime);

	int updateNumber(@Param("number")String number, @Param("newNumber")String newNumber);

	int updatePassword(@Param("number")String number, @Param("password")String password);
}
