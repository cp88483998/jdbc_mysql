package jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import dao.EmpDao;
import entity.Emp;
import util.DBUtil;

public class TestDay03 {
	
	/**
	 * ����EmpDao.delete()����
	 */
	@Test
	public void test9(){
		EmpDao dao = new EmpDao();
		dao.delete(8);
	}
	
	/**
	 * ����EmpDao.update()����
	 */
	//@Test
	public void test8(){
		EmpDao dao = new EmpDao();
		Emp e = new Emp();
		e.setEname("ħ��");
		e.setJob("����");
		e.setMgr(0);
		e.setHiredate(new Date(System.currentTimeMillis()));
		e.setSal(8000.0);
		e.setComm(400.0);
		e.setDeptno(2);
		e.setEmpno(1);
		dao.update(e);
	}
	
	/**
	 * ����EmpDao.findById()����
	 */
	//@Test
	public void test7(){
		int id = 1;
		EmpDao dao = new EmpDao();
		Emp e = dao.findById(id);
		System.out.println(e.getEname()+","+e.getDeptno());
	}
	
	/**
	 * ����EmpDao.findAll()����
	 */
	//@Test
	public void test6(){
		EmpDao dao = new EmpDao();
		List<Emp> list = dao.findAll();
		for(Emp e : list){
			System.out.println(e.getEmpno()+","+e.getEname());
		}
			
	}
	
	/**
	 * ����EmpDao.save()����
	 */
	//@Test
	public void test5(){
		Emp e  = new Emp();
		e.setEname("����");
		e.setJob("����ʦ");
		e.setMgr(0);
		e.setHiredate(new Date(System.currentTimeMillis()));
		e.setSal(5000.0);
		e.setComm(1000.0);
		e.setDeptno(1);
		EmpDao dao = new EmpDao();
		dao.save(e);
		
	}
	
	/**
	 * ��������EmpDao.findByPage()����
	 */
	@Test
	public void test4(){
		EmpDao dao = new EmpDao();
		List<Emp> list = dao.findByPage(1, 10);
		for(Emp e : list){
			System.out.println(e.getEmpno()+","+e.getEname());
		}
	}
	
	/**
	 * ��ҳ��ѯԱ��
	 */
	//@Test
	public void test3(){
		//��������涨��ÿҳ��ʾ10������
		int size =10;
		//�����û���ҳ���ϵ���˵���ҳ
		int page =3;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from ("
					   + "select e.*, rownum r from ("
					   + "select * from emps_cp order by empno) e)"
					   + "where r between ? and ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			//ͨ����ʽ���������ֵ
			int begin = (page-1)*size+1;//10
			int end = page*size;//20
			ps.setInt(1, begin);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getInt("empno")+","+
								   rs.getString("ename")+","+
								   rs.getString("job"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��ҳ��ѯԱ��ʧ��", e);
		}finally{
			DBUtil.close(conn);
		}
		
	}
	
	/**
	 * ��λ�ȡ�ող�������ʱ���� ��ID
	 * 1.�Ȳ��벿��
	 * 2.��ȡ����ID
	 * 3.ʹ�ò���ID���ٲ���Ա��
	 * 
	 */
	//@Test
	public void test2(){
		//����ҳ�洫����������£�
		//����
		String dname = "������";
		String loc = "����";
		//Ա��
		String ename = "����";
		String job = "����";
		int mgr = 0;
		Date hiredate = new Date(System.currentTimeMillis());
		Double sal = 8000.0;
		Double comm = 1000.0;
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
//			//1.���벿��
//			String sql = "INSERT INTO depts_cp VALUES(depts_seq_cp.NEXTVAL, ?, ?)";
//			//дһ�����飬����ps��Ҫ��¼���ֶ���
//			PreparedStatement ps = conn.prepareStatement(sql, new String[]{"deptno"});
//			ps.setString(1, dname);
//			ps.setString(2, loc);
//			ps.executeUpdate();
//			//2.��ps�л�ȡ����¼���ֶ�ֵ
//			ResultSet rs = ps.getGeneratedKeys();
//			rs.next();
//			//������д�ľ��Ǽ�¼��ֵ
//			//����ͨ���ֶε��������ȡ
//			int deptno = rs.getInt(1);
			//3.����Ա��
			String sql1 = "insert into emps_cp values(emps_seq_cp.nextval, ?, ?)";
			PreparedStatement ps1 = conn.prepareStatement(sql1, new String[]{"empno"});
			ps1.setString(1, ename);
			ps1.setString(2, job);
			ps1.executeUpdate();
			ResultSet rs1 = ps1.getGeneratedKeys();
			rs1.next();
			int empno = rs1.getInt(1);
//			System.out.println(deptno);
			System.out.println(empno);
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new RuntimeException("���Ӳ��ż�Ա��ʧ��", e);
		}
		finally{
			DBUtil.close(conn);
		}
	}
	
	/**
	 * ��ʾ��������108��Ա��
	 */
	//@Test
	public void test1(){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			//����һ��������ҵ��һ������
			conn.setAutoCommit(false);
			String sql = "INSERT INTO emps_cp VALUES(emps_seq_cp.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
			//һ��ps������һ��sql���ɣ���Ϊ����������ݵ�sql��һ����
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i=1;i<109;i++){
				//ÿ��ѭ����ps�ڴ�һ������
				ps.setString(1, "�ú�"+i);
				ps.setString(2, "���");
				ps.setInt(3, 0);
				ps.setDate(4, new Date(System.currentTimeMillis()));
				ps.setDouble(5, Math.random()*10000);
				ps.setDouble(6, Math.random()*1000);
				ps.setInt(7, 3);
				ps.addBatch();
				//��ps�д�����ݴﵽ50ʱ����������һ��
				if(i%50==0){
					ps.executeBatch();
					//����һ�κ���Щ������գ�������һ�ֵ���������
					ps.clearBatch();
				}
			}
			//Ϊ�˱�������©��8�����ݣ����ٵ�������һ��
			//��Ϊ�������һ�ַ��ͣ����������
			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new RuntimeException("�������Ա��ʧ��", e);
		}finally{
			DBUtil.close(conn);
		}
	}
}
