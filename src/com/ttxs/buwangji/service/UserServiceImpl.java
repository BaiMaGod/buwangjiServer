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
	public JSONObject login(JSONObject jsonObject) {
		String number = jsonObject.getString("number");
		String password = jsonObject.getString("password");
		if(number == null || password == null) {
			return null;
		}
		//获取该账号的用户信息
		User user = userDao.findUserByNumber(number);
		//如果服务器数据库中没有该账号，或输入的密码与真实密码不一致，登陆失败
		if(user!=null && Tools.md5(password).equals(user.getPassword())) {
			user.setPassword(" ");
			String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			userDao.updateLoginTime(number,now);
			session.commit();
			return JSONObject.fromObject(user);
		}
		return null;
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
			session.commit();
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
			String number = jsonObject.getString("number");
			user.setNumber(number);
			
			//用户要修改昵称
			if(jsonObject.getString("name") !=null) {
				user.setName(jsonObject.getString("name"));
			}
			//用户要修改头像
			if(jsonObject.getString("headImage") !=null) {
				user.setHeadImage(jsonObject.getString("headImage"));
			}
			//用户要修改电话号码
			if(jsonObject.getString("tel") !=null) {
				user.setTel(jsonObject.getString("tel"));
			}
			num = userDao.update(user);
			session.commit();
		
		} catch (Exception e) {
			e.printStackTrace();
			new ServiceException("UserServiceImpl.update方法错误！");
		}
		
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateNumber(JSONObject jsonObject) throws ServiceException {
		int num = 0; 
		String number = jsonObject.getString("number");
		//--------------------
		/*
		 给该账号对应的邮箱发送修改账号链接 
		 */
		//-----------------
		
		String newNumber = jsonObject.getString("newNumber");
		num = userDao.updateNumber(number,newNumber);
		session.commit();
		
//			new ServiceException("UserServiceImpl.update方法错误！");
		
		if(num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updatePassword(JSONObject jsonObject) throws ServiceException {
		String number = jsonObject.getString("number");
		String type = jsonObject.getString("type");
		int num = 0;
		switch (type) {
		case "1":
			String realPassword = userDao.findPasswordByNumber(number);
			String password = jsonObject.getString("password");
			if(realPassword != null && password != null && realPassword.equals(Tools.md5(password))) {
				String newPassword = jsonObject.getString("newPassword");
				num = userDao.updatePassword(number,Tools.md5(newPassword));
				session.commit();
			}
			
			break;
		case "2":
			
			break;
		default:
			break;
		}
		if(num > 0) {
			return true;
		}
		return false;
	}
	

}
