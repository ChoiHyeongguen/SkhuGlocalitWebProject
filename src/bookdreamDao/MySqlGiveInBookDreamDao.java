package bookdreamDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import vo.GiveInBookDream;
import vo.RequestInBookDream;

public class MySqlGiveInBookDreamDao implements GiveInBookDreamDao {

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
			rs = stmt.executeQuery("show tables like " + "'give_bulletinboard'");
			System.out.println("give_bulletinboard 테이블 있는지 검색");
			if(rs.next() == false){
				System.out.println("없다");

				String query = "CREATE TABLE give_bulletinboard"  +
						"(b_no INT NOT NULL, b_title VARCHAR(50)  NOT NULL, "
						+ "b_cre_date DATETIME NOT NULL, b_state VARCHAR(30) NOT NULL, b_grade VARCHAR(30) NOT NULL, "
						+ "b_user VARCHAR(40) NOT NULL, b_img_des VARCHAR(255) NOT NULL,"
						+ "PRIMARY KEY b_no(b_no));";
				stmt.executeUpdate(query);
				System.out.println("give_bulletinboard 테이블 생성");
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
	              " FROM give_bulletinboard" +
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
			stringDataMap.put("state", rs.getString("b_state"));
			stringDataMap.put("grade", rs.getString("b_grade"));
			stringDataMap.put("user", rs.getString("b_user"));
			stringDataMap.put("img_des", rs.getString("b_img_des"));
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
	public int insert(GiveInBookDream give) throws Exception {
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    try {
	      System.out.println(give.getImgDes());
	      System.out.println(give.getUser());
	      connection = ds.getConnection();
	      stmt = connection.prepareStatement(
	          "INSERT INTO give_bulletinboard(b_no, b_title, b_cre_date, b_state, b_grade, b_user, b_img_des)"
	              + " VALUES (?,?,NOW(),?,?,?,?)");
	      stmt.setInt(1, give.getNo());
	      stmt.setString(2,  give.getTitle());
	      stmt.setString(3,  give.getState());
	      stmt.setString(4,  give.getGrade());
	      stmt.setString(5,  give.getUser());
	      stmt.setString(6,  give.getImgDes());
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
	          "DELETE FROM give_bulletinboard WHERE b_no=" + no);

	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}

	@Override
	public GiveInBookDream selectOne(int no) throws Exception {
	    Connection connection = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	      connection = ds.getConnection();
	      stmt = connection.createStatement();
	      rs = stmt.executeQuery(
	          "SELECT * FROM give_bulletinboard" + 
	              " WHERE b_no=" + no);    
	      if (rs.next()) {
	        return new GiveInBookDream()
	    	        .setNo(rs.getInt("b_no"))
	    	        .setTitle(rs.getString("b_title"))
	    	        .setCreatedDate(rs.getDate("b_cre_date"))
	    	        .setState(rs.getString("b_state"))
	    	        .setGrade(rs.getString("b_grade"))
	    	        .setUser(rs.getString("b_user"))
	    	        .setImgDes(rs.getString("b_user"));


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
					"UPDATE give_bulletinboard SET b_no=b_no-1"
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
	public GiveInBookDream exist(String email, String password) throws Exception {
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
	}
*/
}
