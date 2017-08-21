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
	 * 测试EmpDao.delete()方法
	 */
	@Test
	public void test9(){
		EmpDao dao = new EmpDao();
		dao.delete(8);
	}
	
	/**
	 * 测试EmpDao.update()方法
	 */
	//@Test
	public void test8(){
		EmpDao dao = new EmpDao();
		Emp e = new Emp();
		e.setEname("魔王");
		e.setJob("耕地");
		e.setMgr(0);
		e.setHiredate(new Date(System.currentTimeMillis()));
		e.setSal(8000.0);
		e.setComm(400.0);
		e.setDeptno(2);
		e.setEmpno(1);
		dao.update(e);
	}
	
	/**
	 * 测试EmpDao.findById()方法
	 */
	//@Test
	public void test7(){
		int id = 1;
		EmpDao dao = new EmpDao();
		Emp e = dao.findById(id);
		System.out.println(e.getEname()+","+e.getDeptno());
	}
	
	/**
	 * 测试EmpDao.findAll()方法
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
	 * 测试EmpDao.save()方法
	 */
	//@Test
	public void test5(){
		Emp e  = new Emp();
		e.setEname("李四");
		e.setJob("工程师");
		e.setMgr(0);
		e.setHiredate(new Date(System.currentTimeMillis()));
		e.setSal(5000.0);
		e.setComm(1000.0);
		e.setDeptno(1);
		EmpDao dao = new EmpDao();
		dao.save(e);
		
	}
	
	/**
	 * 用来测试EmpDao.findByPage()方法
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
	 * 分页查询员工
	 */
	//@Test
	public void test3(){
		//假设需求规定了每页显示10行数据
		int size =10;
		//假设用户在页面上点击了第三页
		int page =3;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from ("
					   + "select e.*, rownum r from ("
					   + "select * from emps_cp order by empno) e)"
					   + "where r between ? and ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			//通过公式计算出？的值
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
			throw new RuntimeException("分页查询员工失败", e);
		}finally{
			DBUtil.close(conn);
		}
		
	}
	
	/**
	 * 如何获取刚刚插入数据时生成 的ID
	 * 1.先插入部门
	 * 2.获取部门ID
	 * 3.使用部门ID，再插入员工
	 * 
	 */
	//@Test
	public void test2(){
		//假设页面传入的数据如下：
		//部门
		String dname = "行政部";
		String loc = "北京";
		//员工
		String ename = "张三";
		String job = "经理";
		int mgr = 0;
		Date hiredate = new Date(System.currentTimeMillis());
		Double sal = 8000.0;
		Double comm = 1000.0;
		
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
//			//1.插入部门
//			String sql = "INSERT INTO depts_cp VALUES(depts_seq_cp.NEXTVAL, ?, ?)";
//			//写一个数组，告诉ps需要记录的字段名
//			PreparedStatement ps = conn.prepareStatement(sql, new String[]{"deptno"});
//			ps.setString(1, dname);
//			ps.setString(2, loc);
//			ps.executeUpdate();
//			//2.从ps中获取它记录的字段值
//			ResultSet rs = ps.getGeneratedKeys();
//			rs.next();
//			//结果集中存的就是记录的值
//			//必须通过字段的序号来获取
//			int deptno = rs.getInt(1);
			//3.插入员工
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
			throw new RuntimeException("增加部门及员工失败", e);
		}
		finally{
			DBUtil.close(conn);
		}
	}
	
	/**
	 * 演示批量增加108个员工
	 */
	//@Test
	public void test1(){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			//这是一个完整的业务，一个事务
			conn.setAutoCommit(false);
			String sql = "INSERT INTO emps_cp VALUES(emps_seq_cp.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
			//一个ps对象发送一次sql即可，因为批量添加数据的sql是一样的
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i=1;i<109;i++){
				//每次循环向ps内存一条数据
				ps.setString(1, "好汉"+i);
				ps.setString(2, "打劫");
				ps.setInt(3, 0);
				ps.setDate(4, new Date(System.currentTimeMillis()));
				ps.setDouble(5, Math.random()*10000);
				ps.setDouble(6, Math.random()*1000);
				ps.setInt(7, 3);
				ps.addBatch();
				//当ps中存的数据达到50时就批量发送一次
				if(i%50==0){
					ps.executeBatch();
					//发送一次后将这些数据清空，进行下一轮的批量发送
					ps.clearBatch();
				}
			}
			//为了避免有遗漏（8条数据），再单独发送一次
			//因为这是最后一轮发送，不必清空了
			ps.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new RuntimeException("批量添加员工失败", e);
		}finally{
			DBUtil.close(conn);
		}
	}
}
