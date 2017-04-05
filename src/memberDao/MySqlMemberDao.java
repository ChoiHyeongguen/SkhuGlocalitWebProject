package memberDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import vo.Member;
public class MySqlMemberDao implements MemberDao {
	DataSource ds;
	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void init() throws Exception {

		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			connection = ds.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("show tables like " + "'members'");
			System.out.println("members 테이블 있는지 검색");
			if(rs.next() == false){
				System.out.println("없다");
				String query = "CREATE TABLE members" + 
						"(id VARCHAR(20)  NOT NULL, pw VARCHAR(30) NOT NULL, name VARCHAR(20) NOT NULL, email VARCHAR(40) NOT NULL,"
						+ "cre_date DATETIME NOT NULL);";
				stmt.executeUpdate(query);
				System.out.println("members 테이블 생성");
			}
			
		} catch(Exception e) {
			throw e;
		
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
		}
	}
	public Member selectOne(String id) throws Exception {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			connection = ds.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT * FROM members" + 
					" WHERE id=" + id);	
			if(rs.next() == false)
				return null;
			else {
				Member member = new Member ()
					.setId(rs.getString("id"))
					.setName(rs.getString("name"))
					.setEmail(rs.getString("email"))
					.setCreatedDate(rs.getDate("cre_data"));
				return member;
			}
		} catch(Exception e) {
			throw e;
		
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
	}
	}
	public Member exist(String id, String password) throws Exception {
		Connection connection =null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			connection = ds.getConnection();
			stmt = connection.prepareStatement(
				"SELECT id,name,email FROM members"
				+ " WHERE id=? AND pw=?");
			stmt.setString(1, id);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return new Member()
						.setId(rs.getString("id"))
						.setName(rs.getString("name"))
						.setEmail(rs.getString("email"));
			} else 
				return null;
		} catch(Exception e) {
			throw e;
		
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (connection != null) connection.close();} catch(Exception e) {}
		}
	}
	public int update(Member member) throws Exception {
		Connection connection =null;
		PreparedStatement stmt = null;
		try{	
			connection = ds.getConnection();
			stmt = connection.prepareStatement(
					"UPDATE members SET pw=?,name=?,email=?"
						+ " WHERE id=?");
			stmt.setString(1, member.getPassword());
			stmt.setString(2, member.getName());
			stmt.setString(3, member.getEmail());
			stmt.setString(4, member.getId());
			return stmt.executeUpdate();
		} catch(Exception e) {
			throw e;
		
		} finally {
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (connection != null) connection.close();} catch(Exception e) {}
		}
	}
	public void  insert(Member member) throws Exception {
		PreparedStatement stmt = null;
		Connection connection = null;
		try {
			connection = ds.getConnection();
			stmt = connection.prepareStatement(
					"INSERT INTO members(id, pw, name, email, cre_date)"
							+ " VALUES (?,?,?,?,NOW())");
			stmt.setString(1, member.getId());
			stmt.setString(2, member.getPassword());
			stmt.setString(3, member.getName());
			stmt.setString(4, member.getEmail());
			stmt.executeUpdate();
			}
		catch(Exception e) {
			throw e;
		} 	finally {
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (connection != null) connection.close();} catch(Exception e) {}
		}
	}
	public List<Member> selectlList() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		Connection connection = null;
		try{
			connection = ds.getConnection();
			stmt = connection.createStatement();
			rs=stmt.executeQuery("SELECT id, password, name,email, cre_date" +
					" FROM members");
			
			ArrayList<Member> members = new ArrayList<Member>();
			
			while(rs.next()){
				members.add(new Member()
						.setId(rs.getString("id"))
						.setPassword(rs.getString("pw"))
						.setName(rs.getString("name"))
						.setEmail(rs.getString("email"))
						.setCreatedDate(rs.getDate("cre_date")) );
				}
				return members;

		} catch(Exception e) {
			throw e;
		
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (connection != null) connection.close();} catch(Exception e) {}
		}
	}
}
