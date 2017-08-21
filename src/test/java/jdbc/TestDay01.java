package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import util.DBUtil;


public class TestDay01 {
	/**
	 * ����DBUtil
	 */
	@Test
	public void test6(){
		Connection conn =null;
		try {
			conn = DBUtil.getConnection();
			System.out.println(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn);
		}
		
	}
	
	
	/**
	 * ��ʾ���ʹ��properties��ȡdb.properties����ļ�
	 * Properties�����Ͼ���һ�������ɢ�б?Map����ɢ�б���ص���Ƕ�ȡ�ٶȿ�
	 * ��ר��������ȡproperties�ļ�
	 */
	@Test
	public void test3(){
		Properties p = new Properties();
		try {
			//��ȡ�������ClassLoader
			//ʹ����ӱ���·��classes��
			//��ȡdb.properties
			p.load(TestDay01.class.getClassLoader().getResourceAsStream("db.properties"));
			System.out.println(p.getProperty("user"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("�Ҳ���db.properties�ļ�", e);
		}
	}
	
	/**
	 * ��δ�������
	 * ���ִ��DML
	 */
	@Test
	public void test1(){
		//1.ע����
		//����DriverManager���ĸ�jar��
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//2.�������ӣ�DriverManager���Զ����������еķ�������һ������
			//���������еķ�������һ������
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@172.17.2.200:1521:ora10g", "jsd1606", "jsd1606");
			System.out.println(conn);
			//3.����Statement
			Statement smt = conn.createStatement();
			//��Java��дSQL�����治Ҫд�ֺ�
			String sql = "insert into emps_cp "
					+ "values(emps_seq_cp.nextval, '��ɮ', '�ܼ�', 0, sysdate, 8000.0, 5000.0, 2)";
			//4.ִ��SQL
			//�÷�������ִ��SQL��Ӱ�������
			int rows = smt.executeUpdate(sql);
			System.out.println(rows);
		} catch (ClassNotFoundException e) {
			//��¼��־
			e.printStackTrace();
			//�����쳣
			//Ĭ�ϵ�ҵ���?�緵��Ĭ��ֵ��
			//���������׳��쳣
			throw new RuntimeException("�Ҳ�������", e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("������ݿ�ʧ��", e);
		}finally{
			//�ر�����
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException("�ر�����ʧ��", e);
				}
			}
		}
	}	
	@Test
	public void test2(){
		//1.ע����
		//����DriverManager���ĸ�jar��
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//2.�������ӣ�DriverManager���Զ����������еķ�������һ������
			//���������еķ�������һ������
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@172.17.2.200:1521:ora10g", "jsd1606", "jsd1606");
			System.out.println(conn);
			//3.����Statement
			Statement smt = conn.createStatement();
			//��Java��дSQL�����治Ҫд�ֺ�
			String sql = "update emps_cp set ename='���' where empno=7";
			//4.ִ��SQL
			//�÷�������ִ��SQL��Ӱ�������
			int rows = smt.executeUpdate(sql);
			System.out.println(rows);
		} catch (ClassNotFoundException e) {
			//��¼��־
			e.printStackTrace();
			//�����쳣
			//Ĭ�ϵ�ҵ���?�緵��Ĭ��ֵ��
			//���������׳��쳣
			throw new RuntimeException("�Ҳ�������", e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("������ݿ�ʧ��", e);
		}finally{
			//�ر�����
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException("�ر�����ʧ��", e);
				}
			}
		}
	}	
}
