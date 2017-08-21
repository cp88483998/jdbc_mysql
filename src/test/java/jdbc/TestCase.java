package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;


import util.DBUtil;

public class TestCase {
	@Test
	public void conTest(){
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			String sql = "select * from cn_note";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString("cn_note_id"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
