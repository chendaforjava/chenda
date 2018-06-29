package com.vein.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vein.bean.User;

public class JDBCUtil {

	private static Connection conn = null;

	private static String driver = "oracle.jdbc.driver.OracleDriver"; // 驱动

	private static String url = "jdbc:oracle:thin:@192.168.1.140:1521:orcl"; // 连接字符串

	private static String username = "veinsign"; // 用户名

	private static String password = "veinsign"; // 密码

	// 获得连接对象
	private static synchronized Connection getConn() {
		if (conn == null) {
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, username, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	// 执行查询语句
	public static User query(String uId) throws SQLException {
		User user = new User();
		PreparedStatement pstmt;
		String sql = "select * from tb_user where U_ID = ?";
		try {
			pstmt = getConn().prepareStatement(sql);
			pstmt.setString(1, uId);
			// 建立一个结果集，用来保存查询出来的结果
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				user.setuId(rs.getString(1));
				user.setCorId(rs.getString(5));
				user.setUjobNum(rs.getString(12));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
}
