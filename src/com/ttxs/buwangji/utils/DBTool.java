/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Administrator
 *
 */
public class DBTool {
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	
	
	static {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			//����ط�ʽ��������ļ���������
			is = DBTool.class.getClassLoader().getResourceAsStream("db.properties");
			//���������ļ�
			properties.load(is);
			
			driver = properties.getProperty("driver");
			url = properties.getProperty("url");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ������ݿ����Ӷ���
	 * @return Connection
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn =  DriverManager.getConnection(url,user,password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 	���µķ��ͷ���
	 */
	public static int update(String sql,Object...obj) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		
		if(obj != null) {
			for(int i = 0;i<obj.length;i++) {
				ps.setObject(i+1, obj[i]);
			}
		}
		
		int num = ps.executeUpdate();
		conn.close();
		return num;
	}
	
	/**
	 * ��ѯ�ķ��ͷ���
	 */
	public static <T> List<T> query(String sql,Class<T> clazz,Object...obj) throws SQLException{
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		
		List<T> list = new ArrayList<T>();
		
		if(obj != null) {
			for(int i = 0;i<obj.length;i++) {
				ps.setObject(i+1,obj[i]);
			}
		}
		
		ResultSet rs = ps.executeQuery();
		
		ResultSetMetaData metaData = rs.getMetaData();
		
		try {
			while(rs.next()) {
				//���ݷ��䴴����Ӧ��ʵ����󣨴����class��
				T t =  clazz.newInstance();
			
				for(int i = 1;i<=metaData.getColumnCount();i++) {
					
					String colName = metaData.getColumnLabel(i);
					Object colValue = rs.getObject(i);
					
					//�����ֶ����ҵ������������
					Field field = clazz.getDeclaredField(colName);
					field.setAccessible(true);
					
					//ʱ�����ͱ���ת����String
					if(colValue instanceof Date ){
						colValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(colValue);
					}
					if(colValue != null)
						field.set(t,colValue);
				}
				list.add(t);
			}
		} catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException|InstantiationException e) {
			e.printStackTrace();
		}finally {
			if(conn !=null) conn.close();
		}
		return list;
	}

}
