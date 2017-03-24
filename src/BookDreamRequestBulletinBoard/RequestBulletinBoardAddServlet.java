package BookDreamRequestBulletinBoard;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
 	안드로이드 어플리케이션에서 추가해야할 게시판 글의 내용을 받아서
  	데이터베이스에 추가하는 서블릿(게시판 글 추가 역할)
 	초기화 코드의 주석과 중복되는 내용들은 생략
*/
@WebServlet("/addDemandBulletinBoardInfo")
public class RequestBulletinBoardAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{ 
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		System.out.println("수요 게시판 글 추가 서블릿 동작");	
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String tableName = "demand_bulletinboard_table";
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/bookdream_request_db", //JDBC URL
					"project",	// DBMS 사용자 아이디
					"glocalit");	// DBMS 사용자 암호
			stmt = conn.createStatement();
			rs= stmt.executeQuery("SHOW TABLES LIKE " + "'" + tableName+"'");
			if(rs.next() == false) {		// 테이블이 있는지 조회, 없다면 새로 생성합니다.
				stmt.executeUpdate("CREATE TABLE " + tableName + 
						"(b_no INT NOT NULL, b_title VARCHAR(40)  NOT NULL, "
						+ "b_date VARCHAR(10) NOT NULL, b_user VARCHAR(20) NOT NULL, b_period VARCHAR(20) NOT NULL, b_content VARCHAR(50) NOT NULL, "
						+ "PRIMARY KEY b_no(b_no));");
						System.out.println(tableName+" 테이블이 생성되었습니다. ");
			}
			// 안드로이드 프로그램으로 부터 데이터를 받고
			ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
			HashMap<String, String> stringDataMap = (HashMap<String, String>)ois.readObject();
			// 정보를 데이터베이스에 추가해준다.
			String sql = "INSERT INTO " + tableName +"(b_no, b_title, b_date, b_user, b_period, b_content) VALUES(?, ?, ?, ?, ?, ?)";
			PreparedStatement psmt = conn.prepareStatement(sql);
			psmt.setInt(1, Integer.parseInt(stringDataMap.get("no")));
			psmt.setString(2, stringDataMap.get("title"));
			psmt.setString(3, stringDataMap.get("date"));
			psmt.setString(4, stringDataMap.get("user"));
			psmt.setString(5, stringDataMap.get("period"));
			psmt.setString(6, stringDataMap.get("content"));
			psmt.executeUpdate();	
		} catch (Exception e) {
				throw new ServletException(e);
			} finally {
				try {if (rs != null) rs.close();} catch(Exception e) {}
				try {if (stmt != null) stmt.close();} catch(Exception e) {}
				try {if (conn != null) conn.close();} catch(Exception e) {}
			}
	}
}