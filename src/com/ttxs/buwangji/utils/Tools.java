/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Administrator
 *
 */
public class Tools {
	
	/**
	 * Mybatis,获得SqlSession对象
	 * @throws IOException 
	 */
	public static SqlSession getSession() {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader("Configuration.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session = sessionFactory.openSession();
		
		return session;
	}

	/**
	 * 生成随机ID
	 * @return id
	 */
	public static String getUUID() {
		String id =null;
		UUID uuid = UUID.randomUUID();
		id = uuid.toString();
		System.out.println(id);
		//去掉随机ID的短横线
		id = id.replace("-", "");
		System.out.println(id);
		
		//将随机ID换成数字
		int num = id.hashCode();
		System.out.println(num);
		//去绝对值
		num = num < 0 ? -num : num;
		
		id = String.valueOf(num);
		
		return id;
	}
	
	/**
	 * 密码加密方法
	 */
	public static String md5(String psw) {
		String password = null;
		
		//md5加密
		password = DigestUtils.md5Hex(psw);
		
		return password;
	}
	
	/**
	 * 将JSONArray转换为List<JSONObject>
	 */
	public static List<JSONObject> JsonArrayToList(JSONArray jsonArray){
		List<JSONObject> list = new ArrayList<>();
		
		if(jsonArray != null) {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				list.add(jsonObject);
			}
		}
		
		return list;
	}
	/**
	 * 将List转换为JSONArray
	 * @param <T>
	 */
	public static <T> JSONArray ListToJsonArray(List<T> list){
		JSONArray jsonArray = new JSONArray();
		
		if(list != null) {
			for (T t : list) {
				JSONObject jsonObject = JSONObject.fromObject(t);
				jsonArray.add(jsonObject);
			}
		}
		
		return jsonArray;
	}
}
