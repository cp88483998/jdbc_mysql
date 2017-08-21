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
	 * ģ��ת�˵�ҵ��
	 * 1.���̽��������
	 * 2.�����½ڿ�Ҫ��������
	 * 
	 * ���תNԪ���տ
	 * ���踶��Ѿ���¼��
	 * 
	 * 1.��ѯ����˺ţ���������
	 * 2.��ѯ�տ�˺ţ����Ƿ���ȷ
	 * 3.�޸ĸ���˺�-1000
	 * 4.�޸��տ�˺�+1000
	 */
	@Test
	public void test8(){
		//���豾��ת�˵������Ϣ���£�
		String payId = "00001";//�����˺�
		String recId = "00002";//�տ��˺�
		double mny = 1000.0;//ת�˽��
		//ת����һ��������ҵ�����̣�������Ҫ��һ������֮��ִ��SQL��
		//��ˣ�ֻ��Ҫһ������
		Connection cnn = null;
		try {
			cnn = DBUtil.getConnection();
			//ת����һ��������ҵ�����̣�Ӧ����һ������֮����ɡ�
			//JDBC����Ҫ�ֶ�����������ܽ��������⡣
			cnn.setAutoCommit(false);
			//1.��ѯ������
			String sql = "SELECT * FROM accounts_cp WHERE id=?";
			PreparedStatement ps = cnn.prepareStatement(sql);
			ps.setString(1, payId);
			ResultSet rs = ps.executeQuery();
			//ͨ��Id��ѯ�Ľ�����ֻ��һ��
			double balPay = 0.0;
			if(rs.next()){
				balPay = rs.getDouble("money");
				if(balPay<mny){
					throw new SQLException("����");
				}
			}
			//2.��ѯ�տ�˺��Ƿ����
			String sql1 = "SELECT * FROM accounts_cp WHERE id=?";
			PreparedStatement ps1 = cnn.prepareStatement(sql1);
			ps1.setString(1, recId);
			ResultSet rs1 = ps1.executeQuery();
			double balRec = 0.0;
			if(!rs1.next()){ 
				//�տ��˺Ŵ���
				throw new SQLException("�տ��˺Ŵ���");
			}else{
				balRec = rs1.getDouble("money");
			}
			//3.���-1000
			String sql2 = "UPDATE accounts_cp SET money=? WHERE id=?";
			PreparedStatement ps2 = cnn.prepareStatement(sql2);
			ps2.setDouble(1, balPay-mny);
			ps2.setString(2, payId);
			ps2.executeUpdate();
			//4.�տ+1000
			String sql3 = "UPDATE accounts_cp SET money=? WHERE id=?";
			PreparedStatement ps3 = cnn.prepareStatement(sql3);
			ps3.setDouble(1, balRec+mny);
			ps3.setString(2, recId);
			ps3.executeUpdate();
			//����ҵ�����̶���ɺ��ύһ������
			cnn.commit();
		} catch (Exception e) {
			//���۲���ʲô�쳣��������ʧ�ܣ���ʱ��Ҫ�ع�
			try {
				cnn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.out.println("ת��ʧ��");
		}
		
	}
	
	/**
	 * ��ʾResultSetMetaData��ʹ��
	 */
	//@Test
	public void test7(){
		Connection cnn = null;
		try {
			cnn = DBUtil.getConnection();
			String sql = "SELECT * FROM emps_cp";
			Statement smt = cnn.createStatement();
			ResultSet rs = smt.executeQuery(sql);
			//��ȡ�����Ԫ���ݣ����к����˶Խ������������Ϣ���磺�����У�����������
			ResultSetMetaData rsmd = rs.getMetaData();
			System.out.println(rsmd.getColumnCount());//һ��������
			System.out.println(rsmd.getColumnName(1));//��һ�е�����
			System.out.println(rsmd.getColumnTypeName(1));//��һ�е���������
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��ѯԱ��ʧ��", e);
		}finally{
			DBUtil.close(cnn);
		}
		
	}
	
	/**
	 * ʹ��PreparedStatementִ�в�ѯ������ע�빥��
	 */
	//@Test
	public void test5(){
		//����ҳ�洫���������
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
				System.out.println("��½�ɹ���");
			}else{
				System.out.println("��½ʧ�ܣ�");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��ѯ�û�ʧ��", e);
		}finally{
			DBUtil.close(cnn);
		}
	}
	
	/**
	 * ��ʾʹ��preparedStatement��ִ��update���
	 */
	//@Test
	public void test4(){
		String ename = "����";
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
			throw new RuntimeException("�޸�Ա����Ϣʧ��", e);
		}
		
	}
	/**
	 * ��ʾʹ��preparedStatement��ִ��DML���
	 */
	//@Test
	public void test3(){
		//���贫����Ҫ���ӵ���������
		String name = "����ʦ";
		String job = "��Ա";
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
			throw new RuntimeException("����Ա��ʧ��", e);
		}finally{
			DBUtil.close(conn);
		}
		
	}
	
	/**
	 * ��ʾʹ��Statement����ִ��DQL���
	 */
	//@Test
	public void test1(){
		//����ҳ�洫�������²�ѯ������
		int empno = 1;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			Statement smt = conn.createStatement();
			//ƴдSQL
			String sql = "SELECT * FROM emps_cp WHERE empno="+empno;
			//ִ�в�ѯ��䣬���ؽ����ResultSet
			//�ö�����õ�����ģʽ��Ƶ�
			//ͨ�����ǲ���while���б���
			ResultSet rs = smt.executeQuery(sql);
			while(rs.next()){
				//ÿ�α����õ���һ�У��������У�����
				//�ֱ��ȡ������ÿһ������
				//rs.get����(�е�����)
				//rs.get����(����)
				System.out.println(rs.getInt("empno"));
				System.out.println(rs.getString("Ename"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��ȡ����ʧ��", e);
		}finally{
			DBUtil.close(conn);
		}
	}
	
	/**
	 * ʹ��PS��ִ��DQL
	 */
	//@Test
	public void test2(){
		Connection conn = null;
		try {
			//���贫��Ĳ���Ϊ
			int empno = 2;
			conn = DBUtil.getConnection();
			String sql = "SELECT * FROM emps_cp WHERE empno=?";
			//����PSʱ��Ҫ����SQL�������SQL���͸����ݿ���ͱ���
			PreparedStatement ps = conn.prepareStatement(sql);
			//����SQL�������
			//ps.set����(�ڼ����ʺ�, �ʺŵ�ֵ)
			ps.setInt(1, empno);
			//ִ�в�ѯ�������ݿⷢ����Щ�������Ӷ���ʹ���ݿ�������ִ�мƻ�
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getInt("empno"));
				System.out.println(rs.getString("ename"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��ѯԱ��ʧ��", e);
		}finally{
			DBUtil.close(conn);
		}
		
	}
	
}
