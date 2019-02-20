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
		//��ȡ���˺ŵ��û���Ϣ
		User user = userDao.findUserByNumber(number);
		//������������ݿ���û�и��˺ţ����������������ʵ���벻һ�£���½ʧ��
		if(user!=null && Tools.md5(password).equals(user.getPassword())) {
			user.setPassword(" ");
			String now = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
			userDao.updateLoginTime(number,now);
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
		}catch (Exception e) {
			e.printStackTrace();
			new ServiceException("UserServiceImpl.register��������");
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
			
			//�û�Ҫ�޸��ǳ�
			if(jsonObject.getString("name") !=null) {
				user.setName(jsonObject.getString("name"));
			}
			//�û�Ҫ�޸�ͷ��
			if(jsonObject.getString("headImage") !=null) {
				user.setHeadImage(jsonObject.getString("headImage"));
			}
			//�û�Ҫ�޸ĵ绰����
			if(jsonObject.getString("tel") !=null) {
				user.setTel(jsonObject.getString("tel"));
			}
			num = userDao.update(user);
		
		} catch (Exception e) {
			e.printStackTrace();
			new ServiceException("UserServiceImpl.update��������");
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
		 �����˺Ŷ�Ӧ�����䷢���޸��˺����� 
		 */
		//-----------------
		
		String newNumber = jsonObject.getString("newNumber");
		num = userDao.updateNumber(number,newNumber);
		
//			new ServiceException("UserServiceImpl.update��������");
		
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
