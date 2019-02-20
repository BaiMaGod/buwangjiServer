/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import com.ttxs.buwangji.bean.User;
import com.ttxs.buwangji.dao.UserDao;
import com.ttxs.buwangji.exception.ServiceException;
import com.ttxs.buwangji.utils.Tools;

import net.sf.json.JSONObject;

/**
 * @author yc
 *
 */
public class UserServiceImpl implements UserService {
	SqlSession session;
	UserDao userDao;
	
	public UserServiceImpl() {
		session = Tools.getSession();
		userDao = session.getMapper(UserDao.class);
	}
	
	@Override
	public boolean login(JSONObject jsonObject) {
		String number = jsonObject.getString("name");
		String password = jsonObject.getString("password");
		if(number == null || password == null) {
			return false;
		}
		
		String realPsw = userDao.findPasswordByNumber(number);
		session.close();
		
		if(Tools.md5(password).equals(realPsw)) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean register(JSONObject jsonObject) throws ServiceException {
		int num = 0;
		try {
			String number = jsonObject.getString("number");
			String name = jsonObject.getString("name");
			String password = jsonObject.getString("password");
			if(number == null || password == null || name == null) {
				return false;
			}
			
			String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			User user = new User(Tools.getUUID(),name,number,Tools.md5(password),now,0);
			
			num = userDao.add(user);
			session.close();
		}catch (Exception e) {
			e.printStackTrace();
			new ServiceException("UserServiceImpl.register方法错误！");
		}
		
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean update(JSONObject jsonObject) throws ServiceException {
		int num = 0; 
		try {
			
			User user = new User();
			user.setId(jsonObject.getString("number"));
			if(jsonObject.getString("newNumber") !=null) {
				user.setNumber(jsonObject.getString("newNumber"));
			}
			if(jsonObject.getString("name") !=null) {
				user.setName(jsonObject.getString("name"));
			}
			if(jsonObject.getString("password") !=null) {
				user.setPassword(jsonObject.getString("password"));
			}
			if(jsonObject.getString("headImage") !=null) {
				user.setHeadImage(jsonObject.getString("headImage"));
			}
			num = userDao.update(user);
			session.close();	
		
		} catch (Exception e) {
			e.printStackTrace();
			new ServiceException("UserServiceImpl.update方法错误！");
		}
		
		if(num > 0) {
			return true;
		}
		return false;
	}
	
	

}
