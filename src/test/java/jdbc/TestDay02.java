package jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


import org.junit.Test;

import util.DBUtil;
import util.DBUtil;

public class TestDay02 {
	
	/**
	 * 模拟转账的业务
	 * 1.巩固今天的内容
	 * 2.带出下节课要讲的内容
	 * 
	 * 付款方转N元给收款方
	 * 假设付款方已经登录了
	 * 
	 * 1.查询付款方账号，看余额够不够
	 * 2.查询收款方账号，看是否正确
	 * 3.修改付款方账号-1000
	 * 4.修改收款方账号+1000
	 */
	@Test
	public void test8(){
		//假设本次转账的相关信息如下：
		String payId = "00001";//付款账号
		String recId = "00002";//收款账号
		double mny = 1000.0;//转账金额
		//转账是一个完整的业务流程，所以需要在一个事务之内执行SQL，
		//因此，只需要一个连接
		Connection cnn = null;
		try {
			cnn = DBUtil.getConnection();
			//转账是一个完整的业务流程，应该在一个事务之内完成。
			//JDBC中需要手动管理事务才能解决这个问题。
			cnn.setAutoCommit(false);
			//1.查询付款方余额
			String sql = "SELECT * FROM accounts_cp WHERE id=?";
			PreparedStatement ps = cnn.prepareStatement(sql);
			ps.setString(1, payId);
			ResultSet rs = ps.executeQuery();
			//通过Id查询的结果最多只有一条
			double balPay = 0.0;
			if(rs.next()){
				balPay = rs.getDouble("money");
				if(balPay<mny){
					throw new SQLException("余额不足");
				}
			}
			//2.查询收款方账号是否存在
			String sql1 = "SELECT * FROM accounts_cp WHERE id=?";
			PreparedStatement ps1 = cnn.prepareStatement(sql1);
			ps1.setString(1, recId);
			ResultSet rs1 = ps1.executeQuery();
			double balRec = 0.0;
			if(!rs1.next()){ 
				//收款账号错误
				throw new SQLException("收款账号错误");
			}else{
				balRec = rs1.getDouble("money");
			}
			//3.付款方-1000
			String sql2 = "UPDATE accounts_cp SET money=? WHERE id=?";
			PreparedStatement ps2 = cnn.prepareStatement(sql2);
			ps2.setDouble(1, balPay-mny);
			ps2.setString(2, payId);
			ps2.executeUpdate();
			//4.收款方+1000
			String sql3 = "UPDATE accounts_cp SET money=? WHERE id=?";
			PreparedStatement ps3 = cnn.prepareStatement(sql3);
			ps3.setDouble(1, balRec+mny);
			ps3.setString(2, recId);
			ps3.executeUpdate();
			//整个业务流程都完成后提交一次事务
			cnn.commit();
		} catch (Exception e) {
			//无论捕获到什么异常，都代表失败，此时都要回滚
			try {
				cnn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.out.println("转账失败");
		}
		
	}
	
	/**
	 * 演示ResultSetMetaData的使用
	 */
	//@Test
	public void test7(){
		Connection cnn = null;
		try {
			cnn = DBUtil.getConnection();
			String sql = "SELECT * FROM emps_cp";
			Statement smt = cnn.createStatement();
			ResultSet rs = smt.executeQuery(sql);
			//获取结果集元数据，其中涵盖了对结果集的描述信息，如：多少列，列名等内容
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println(rsmd.getColumnCount());//一共多少列
			System.out.println(rsmd.getColumnName(1));//第一列的列名
			System.out.println(rsmd.getColumnTypeName(1));//第一列的数据类型
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询员工失败", e);
		}finally{
			DBUtil.close(cnn);
		}
		
	}
	
	/**
	 * 使用PreparedStatement执行查询，避免注入攻击
	 */
	//@Test
	public void test5(){
		//假设页面传入的数据是
		String name = "zhangsan";
		String pwd = "a' or 'b' = 'b";
		Connection cnn = null;
		try {
			cnn = DBUtil.getConnection();
			String sql = "SELECT * FROM users_cp WHERE username=? AND password=?";
			PreparedStatement ps = cnn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, pwd);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				System.out.println("登陆成功！");
			}else{
				System.out.println("登陆失败！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询用户失败", e);
		}finally{
			DBUtil.close(cnn);
		}
	}
	
	/**
	 * 演示使用preparedStatement来执行update语句
	 */
	//@Test
	public void test4(){
		String ename = "传奇";
		double sal = 5000.0;
		double comm = 2000.0;
		int empno = 11;
		Connection cnn = null;
		try {
			cnn = DBUtil.getConnection();
			String sql = "UPDATE emps_cp SET ename=?, sal=?, comm=? WHERE empno=?";
			PreparedStatement ps = cnn.prepareStatement(sql);
			ps.setString(1, ename);
			ps.setDouble(2, sal);
			ps.setDouble(3, comm);
			ps.setInt(4, empno);
			ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("修改员工信息失败", e);
		}
		
	}
	/**
	 * 演示使用preparedStatement来执行DML语句
	 */
	//@Test
	public void test3(){
		//假设传入了要增加的数据如下
		String name = "苍老师";
		String job = "演员";
		int mgr = 0;
		Date hiredate = new Date(System.currentTimeMillis());
		double sal = 8000.0;
		double comm = 9000.0;
		int deptno = 2;
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "INSERT INTO emps_cp VALUES(emps_seq_cp.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, job);
			ps.setInt(3, mgr);
			ps.setDate(4, hiredate);
			ps.setDouble(5, sal);
			ps.setDouble(6, comm);
			ps.setInt(7, deptno);
			ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("增加员工失败", e);
		}finally{
			DBUtil.close(conn);
		}
		
	}
	
	/**
	 * 演示使用Statement对象执行DQL语句
	 */
	//@Test
	public void test1(){
		//假设页面传入了如下查询条件：
		int empno = 1;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			Statement smt = conn.createStatement();
			//拼写SQL
			String sql = "SELECT * FROM emps_cp WHERE empno="+empno;
			//执行查询语句，返回结果集ResultSet
			//该对象采用迭代器模式设计的
			//通常都是采用while进行遍历
			ResultSet rs = smt.executeQuery(sql);
			while(rs.next()){
				//每次遍历得到了一行（包含多列）数据
				//分别获取该行中每一列数据
				//rs.get类型(列的索引)
				//rs.get类型(列名)
				System.out.println(rs.getInt("empno"));
				System.out.println(rs.getString("Ename"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("获取连接失败", e);
		}finally{
			DBUtil.close(conn);
		}
	}
	
	/**
	 * 使用PS来执行DQL
	 */
	//@Test
	public void test2(){
		Connection conn = null;
		try {
			//假设传入的参数为
			int empno = 2;
			conn = DBUtil.getConnection();
			String sql = "SELECT * FROM emps_cp WHERE empno=?";
			//创建PS时需要传入SQL，它会把SQL发送给数据库检查和编译
			PreparedStatement ps = conn.prepareStatement(sql);
			//设置SQL所需参数
			//ps.set类型(第几个问号, 问号的值)
			ps.setInt(1, empno);
			//执行查询：向数据库发送这些参数，从而促使数据库来运行执行计划
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getInt("empno"));
				System.out.println(rs.getString("ename"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询员工失败", e);
		}finally{
			DBUtil.close(conn);
		}
		
	}
	
}
