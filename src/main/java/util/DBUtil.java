package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

public class DBUtil {
	//声明连接池
	private static BasicDataSource ds;
	static{
		//读取连接参数
		Properties p = new Properties();
		try {
			p.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
			//数据库连接参数
			String driver = p.getProperty("driver");
			String url = p.getProperty("url");
			String user = p.getProperty("user");
			String pwd = p.getProperty("pwd");
			//连接池的参数
			String initSize = p.getProperty("initSize");
			String maxSize = p.getProperty("maxSize");
			//创建连接池，并给它设置参数
			ds = new BasicDataSource();
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(user);
			ds.setPassword(pwd);
			ds.setInitialSize(Integer.parseInt(initSize));
			ds.setMaxActive(Integer.valueOf(maxSize));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("找不到db文件", e);
		} 
	}
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	/**
	 * 归还连接：
	 * 连接池创建的连接，其close方法不再是关闭连接，而是将连接归还给连接池，
	 * 连接池会将此链接数据清空并标识为空闲。
	 * @param conn
	 */
	public static void close(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("归还连接失败", e);
			}
		}
	}
}
