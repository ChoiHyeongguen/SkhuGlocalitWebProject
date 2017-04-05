package bookDreamDao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.sql.DataSource;

import vo.RequestInBookDream;
import vo.TokenInFcm;

public class MySqlTokenInFcmDao implements TokenInFcmDao {
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
			rs = stmt.executeQuery("show tables like " + "'tokens'");
			System.out.println("tokens 테이블 있는지 검색");
			if(rs.next() == false){
				System.out.println("없다");
				String query = "CREATE TABLE tokens" +
								"(token VARCHAR(255)  NOT NULL,  user VARCHAR(30) NOT NULL, "
								+ "cre_date DATETIME NOT NULL, PRIMARY KEY token(token))";
				stmt.executeUpdate(query);
				System.out.println("request_bulletinboard 테이블 생성");
			}
			
		} catch(Exception e) {
			throw e;
		
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
		}

	}

	@Override
	public int insert(TokenInFcm token) throws Exception {
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    try {
	      connection = ds.getConnection();
	      stmt = connection.prepareStatement(
	          "INSERT INTO tokens(token, user, cre_date)"
	              + " VALUES (?,?,NOW())");
	      stmt.setString(1, token.getToken());
	      stmt.setString(2, token.getUser());
	      return stmt.executeUpdate();

	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}

	@Override
	public int delete(String token) throws Exception {
	    Connection connection = null;
	    Statement stmt = null;

	    try {
	      connection = ds.getConnection();
	      stmt = connection.createStatement();
	      return stmt.executeUpdate(
	          "DELETE FROM tokens WHERE token=" + token);

	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}

	@Override
	public TokenInFcm selectOne(String token) throws Exception {
	    Connection connection = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	      connection = ds.getConnection();
	      stmt = connection.createStatement();
	      rs = stmt.executeQuery(
	          "SELECT * FROM tokens" + 
	              " WHERE token=" + token);    
	      if (rs.next()) {
	        return new TokenInFcm()
	        		.setToken(rs.getString("token"))
	    	        .setUser(rs.getString("user"))
	    	        .setCreatedDate(rs.getDate("cre_date"));
	      } else {
	    	  	return null;
	      }

	    } catch (Exception e) {
	      throw e;
	    } finally {
	      try {if (rs != null) rs.close();} catch(Exception e) {}
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}
	

}
