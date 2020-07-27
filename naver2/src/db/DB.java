package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
	public static Connection conn() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PW);
		System.out.println("연결 성공");
		return conn;
	}
}
