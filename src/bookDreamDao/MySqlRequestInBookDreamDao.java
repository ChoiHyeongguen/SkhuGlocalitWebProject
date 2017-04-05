package bookDreamDao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import vo.RequestInBookDream;

public class MySqlRequestInBookDreamDao implements RequestInBookDreamDao {

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
			rs = stmt.executeQuery("show tables like " + "'request_bulletinboard'");
			System.out.println("request_bulletinboard 테이블 있는지 검색");
			if(rs.next() == false){
				System.out.println("없다");
				String query = "CREATE TABLE request_bulletinboard" +
								"(b_no INT NOT NULL, b_title VARCHAR(40)  NOT NULL, "
								+ "b_cre_date DATETIME NOT NULL, b_user VARCHAR(20) NOT NULL, b_period VARCHAR(20) NOT NULL, b_content VARCHAR(50) NOT NULL, "
								+ "PRIMARY KEY b_no(b_no));";
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
	public HashMap<String,HashMap<String, String>> selectList() throws Exception {
	    Connection connection = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	      connection = ds.getConnection();
	      stmt = connection.createStatement();
	      rs = stmt.executeQuery(
	          "SELECT * " + 
	              " FROM request_bulletinboard" +
	          " ORDER BY b_no ASC");

	      HashMap<String, HashMap<String, String>> dataMap = new HashMap<String, HashMap<String, String>>();
		
	      while(rs.next()) {		
				/*
				 	데이터를 한번에 빠르게 보내기 위해 
				 	아래 HashMap들을 한번에 묶어서 보냅니다.
				 */
			HashMap<String, String> stringDataMap = new HashMap<String, String>();
			stringDataMap.put("no", rs.getInt("b_no")+"");
			stringDataMap.put("title", rs.getString("b_title"));
			stringDataMap.put("date", rs.getDate("b_cre_date")+"");
			stringDataMap.put("user", rs.getString("b_user"));
			stringDataMap.put("period", rs.getString("b_period"));
			stringDataMap.put("content", rs.getString("b_content"));
			dataMap.put(rs.getInt("b_no")+"", stringDataMap);
			
	      }
	      return dataMap;

	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (rs != null) rs.close();} catch(Exception e) {}
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}

	@Override
	public int insert(RequestInBookDream request) throws Exception {
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    try {
	      connection = ds.getConnection();
	      stmt = connection.prepareStatement(
	          "INSERT INTO request_bulletinboard(b_no, b_title, b_cre_date, b_user, b_period, b_content)"
	              + " VALUES (?,?,NOW(),?,?,?)");
	      stmt.setInt(1, request.getNo());
	      stmt.setString(2, request.getTitle());
	      stmt.setString(3, request.getUser());
	      stmt.setString(4, request.getPeriod());
	      stmt.setString(5, request.getContent());
	      return stmt.executeUpdate();

	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}

	@Override
	public int delete(int no) throws Exception {
	    Connection connection = null;
	    Statement stmt = null;

	    try {
	      connection = ds.getConnection();
	      stmt = connection.createStatement();
	      return stmt.executeUpdate(
	          "DELETE FROM request_bulletinboard WHERE b_no=" + no);

	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}

	@Override
	public RequestInBookDream selectOne(int no) throws Exception {
	    Connection connection = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	      connection = ds.getConnection();
	      stmt = connection.createStatement();
	      rs = stmt.executeQuery(
	          "SELECT * FROM request_bulletinboard" + 
	              " WHERE b_no=" + no);    
	      if (rs.next()) {
	        return new RequestInBookDream()
	    	        .setNo(rs.getInt("b_no"))
	    	        .setTitle(rs.getString("b_title"))
	    	        .setCreatedDate(rs.getDate("b_cre_date"))
	    	        .setUser(rs.getString("b_user"))
	    	        .setPeriod(rs.getString("b_period"))
	    	        .setContent(rs.getString("b_content"));

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
	
	@Override
	public int update(int no) throws Exception {
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    try {
	      connection = ds.getConnection();
	      stmt = connection.prepareStatement(
					"UPDATE request_bulletinboard SET b_no=b_no-1"
					+" WHERE b_no > "+no);
	      return stmt.executeUpdate();

	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}
/*
	@Override
	public RequestInBookDream exist(String email, String password) throws Exception {
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	      connection = ds.getConnection();
	      stmt = connection.prepareStatement(
	          "SELECT MNAME,EMAIL FROM request_bulletinboard"
	              + " WHERE EMAIL=? AND PWD=?");
	      stmt.setString(1, email);
	      stmt.setString(2, password);
	      rs = stmt.executeQuery();
	      if (rs.next()) {
	        return new RequestInBookDream()
	          .setTitle(rs.getString("MNAME"))
	          .setContent(rs.getString("EMAIL"));
	      } else {
	        return null;
	      }
	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (rs != null) rs.close();} catch (Exception e) {}
	      try {if (stmt != null) stmt.close();} catch (Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}*/

}
