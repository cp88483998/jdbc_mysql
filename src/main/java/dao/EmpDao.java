package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Emp;
import util.DBUtil;

public class EmpDao{
	public List<Emp> findAll(){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from emps_cp";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Emp> list = new ArrayList<Emp>();
			while(rs.next()){
				Emp e = createEmp(rs);
				list.add(e);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��������Ա��ʧ��", e);
		}finally{
			DBUtil.close(conn);
		}
	}
	
	/**
	 * Alt+Shift+J
	 * ��ҳ��ѯԱ��
	 * @param page ��ǰҳ
	 * @param size ÿҳ��ʾ������
	 * @return
	 */
	public List<Emp> findByPage(int page, int size){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from ("
					   + "select e.*, rownum r from ("
					   + "select * from emps_cp order by empno) e)"
					   + "where r between ? and ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			//ͨ��ʽ���������ֵ
			int begin = (page-1)*size+1;//10
			int end = page*size;//20
			ps.setInt(1, begin);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
			List<Emp> list = new ArrayList<Emp>();
			while(rs.next()){
				Emp e = createEmp(rs);
				list.add(e);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("��ҳ��ѯԱ��ʧ��", e);
		}finally{
			DBUtil.close(conn);
		}
	}
	//Alt+Shift+M
	private Emp createEmp(ResultSet rs) throws SQLException {
		Emp e = new Emp();
		e.setEmpno(rs.getInt("empno"));
		e.setEname(rs.getString("ename"));
		e.setJob(rs.getString("job"));
		e.setMgr(rs.getInt("mgr"));
		e.setHiredate(rs.getDate("hiredate"));
		e.setSal(rs.getDouble("sal"));
		e.setComm(rs.getDouble("comm"));
		e.setDeptno(rs.getInt("deptno"));
		return e;
	}
	
	public Emp findById(int id){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "Select * from emps_cp where empno = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			List<Emp> list = new ArrayList<Emp>();
			Emp emp = new Emp();
			while(rs.next()){
				emp = createEmp(rs);
			}
			return emp;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}finally{
			DBUtil.close(conn);
		}
	}
	
	public void save(Emp emp){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "insert into emps_cp values(emps_seq_cp.nextval, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			Dml(emp, ps);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("����Ա��ʧ��", e);
		}finally{
			DBUtil.close(conn);
		}
		
	}

	private void Dml(Emp emp, PreparedStatement ps) throws SQLException {
		ps.setString(1, emp.getEname());
		ps.setString(2, emp.getJob());
		ps.setInt(3, emp.getMgr());
		ps.setDate(4, emp.getHiredate());
		ps.setDouble(5, emp.getSal());
		ps.setDouble(6, emp.getComm());
		ps.setInt(7, emp.getDeptno());
	}
	
	public void update(Emp emp){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "update emps_cp set ename=?,job=?,mgr=?,"
						+ "hiredate=?,sal=?,comm=?,deptno=? "
						+ "where empno = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			Dml(emp, ps);		
			ps.setInt(8, emp.getEmpno());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("�޸�Ա����Ϣʧ��", e);
		}
		finally{
			DBUtil.close(conn);
		}
		
	}
	
	public void delete(int id){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "delete from emps_cp where empno = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("ɾ��Ա��ʧ��", e);
		}finally{
			DBUtil.close(conn);
		}
	}
}
