package noticeBoardDao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import vo.FreeBoard;


public class MySqlFreeBoardDao implements FreeBoardDao {

	DataSource ds;
	public void setDataSource(DataSource ds) {
	    this.ds = ds;
	}
	
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			connection = ds.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("show tables like " + "'freeboard_table'");
				System.out.println("freeboard_table 테이블 있는지 검색");
				
			if(rs.next() == false){
				System.out.println("자유게시판 테이블이 없음");
				String query = "CREATE TABLE freeboard_table" +
						"(b_no INT NOT NULL, "
						+ " b_title VARCHAR(20) NOT NULL,"
						+ " b_user VARCHAR(20) NOT NULL,"
						+ " b_content VARCHAR(255) NOT NULL, "
						+ " b_date VARCHAR(20) NOT NULL, "
						+ " b_img_des VARCHAR(255) NOT NULL, "
						+ " PRIMARY KEY b_no(b_no));";
				stmt.executeUpdate(query);
				System.out.println("freeboard_table 테이블 생성");
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
		// TODO Auto-generated method stub
	    Connection connection = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	      connection = ds.getConnection();
	      stmt = connection.createStatement();
	      rs = stmt.executeQuery(
	          "SELECT * " + " FROM freeboard_table" +
	          " ORDER BY b_no ASC");

	      HashMap<String, HashMap<String, String>> dataMap = new HashMap<String, HashMap<String, String>>();
	      while(rs.next()) {	
	    	  
	    	//데이터를 한번에 빠르게 보내기 위해 아래 HashMap들을 한번에 묶어서 보냅니다.
			HashMap<String, String> stringDataMap = new HashMap<String, String>();
			stringDataMap.put("no", rs.getInt("b_no")+"");
			stringDataMap.put("title", rs.getString("b_title"));
			stringDataMap.put("user", rs.getString("b_user"));
			stringDataMap.put("content", rs.getString("b_content"));
			stringDataMap.put("date", rs.getString("b_date"));
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
	public int insert(FreeBoard request) throws Exception {
		// TODO Auto-generated method stub
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    	System.out.println("자유게시판 insert진입");
	    	
	    try {
	    	System.out.println(request.getImgDes());
		    System.out.println(request.getUserName());
	      connection = ds.getConnection();
	      stmt = connection.prepareStatement(
	          "INSERT INTO freeboard_table(b_no, b_title, b_user, b_content, b_date, b_img_des)"
	              + " VALUES (?, ?, ?, ?, ?, ?)");
	      
	      stmt.setInt(1, request.getNo());
	      stmt.setString(2, request.getTitle());
	      stmt.setString(3, request.getUserName());
	      stmt.setString(4, request.getContent());
	      stmt.setString(5, request.getDate());
	      stmt.setString(6,  request.getImgDes());
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
		// TODO Auto-generated method stub
	    Connection connection = null;
	    Statement stmt = null;
	    	System.out.println("delete진입");
	    	
	    try {
	      connection = ds.getConnection();
	      stmt = connection.createStatement();
	      return stmt.executeUpdate(
	          "DELETE FROM freeboard_table WHERE b_no=" + no);

	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}

	@Override
	public FreeBoard selectOne(int no) throws Exception {
		// TODO Auto-generated method stub
	    Connection connection = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    	System.out.println("selectOne진입");
	    	
	    try {
	      connection = ds.getConnection();
	      stmt = connection.createStatement();
	      rs = stmt.executeQuery(
	          "SELECT * FROM freeboard_table" + " WHERE b_no=" + no);    
	      
	      if (rs.next()) {
	    	  FreeBoard freeBoard = new FreeBoard();
	    	  freeBoard.setNo(rs.getInt("b_no"));
	    	  freeBoard.setTitle(rs.getString("b_title"));
	    	  freeBoard.setUserName(rs.getString("b_user"));
	    	  freeBoard.setContent(rs.getString("b_content"));
	    	  freeBoard.setDate(rs.getString("b_date"));
	    	  freeBoard.setImgDes(rs.getString("b_img_des"));
	    	  
	        return freeBoard;
	        		
	    	     
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
		// TODO Auto-generated method stub
	    Connection connection = null;
	    PreparedStatement stmt = null;
	    	System.out.println("update진입");
	    	
	    try {
	      connection = ds.getConnection();
	      stmt = connection.prepareStatement(
					"UPDATE freeboard_table SET b_no=b_no-1" + " WHERE b_no > "+no);
	      return stmt.executeUpdate();

	    } catch (Exception e) {
	      throw e;

	    } finally {
	      try {if (stmt != null) stmt.close();} catch(Exception e) {}
	      try {if (connection != null) connection.close();} catch(Exception e) {}
	    }
	}
	
	@Override
	public int freeBoardUpdate(FreeBoard request) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement stmt = null;
			System.out.println("자유게시판 edit진입");
			
			try {
			      connection = ds.getConnection();
			      stmt = connection.prepareStatement(
			    		  "UPDATE freeboard_table SET b_img_des=?, b_title=?, b_content=?, b_date=? " 
			    				  						+ "WHERE b_no=? AND b_user=?;");
			    
			      stmt.setString(1, request.getImgDes());
				  stmt.setString(2, request.getTitle());
				  stmt.setString(3, request.getContent());
				  stmt.setString(4, request.getDate());
				  stmt.setInt(5, request.getNo());
				  stmt.setString(6, request.getUserName());
			   
			      return stmt.executeUpdate();

			    } catch (Exception e) {
			      throw e;

			    } finally {
			      try {if (stmt != null) stmt.close();} catch(Exception e) {}
			      try {if (connection != null) connection.close();} catch(Exception e) {}
			    }
	}
	
	@Override
	public int delete(String user, String title) throws Exception {
		// TODO Auto-generated method stub
		Connection connection = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try{
	    	connection = ds.getConnection();
		    stmt = connection.createStatement();
		    // 테이블을 유저정보와 타이틀 정보로 된 게시판을 검색한다.
		    rs = stmt.executeQuery(
			          "SELECT * FROM freeboard_table WHERE b_user='" + user +"'"
		    		  		+" AND b_title='" + title +"'");   
		    
		    if(rs.next()){
		    	int no = rs.getInt("b_no");		// 검색해서 찾은 번호를 이용해
		    	 int deleteCode = delete(no);	// 삭제하고
		    	 update(no);
		    	 return deleteCode;				//delete코드를 반환한다.
		    }
	    	return -1;
	    	
	    }catch (Exception e) {
		      throw e;

		    } finally {
		      try {if (stmt != null) stmt.close();} catch(Exception e) {}
		      try {if (rs != null) rs.close();} catch(Exception e) {}
		      try {if (connection != null) connection.close();} catch(Exception e) {}
		    }
	}

}