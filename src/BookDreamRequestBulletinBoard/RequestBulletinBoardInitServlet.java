package BookDreamRequestBulletinBoard;

import java.io.DataInputStream;
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
	데이터베이스에서 수요게시판을 구성하는 데이터를 불러와서
	안드로이드 어플리케이션으로 묶어서 전송하는 서블릿(게시판 초기화 역할)
*/
@WebServlet("/requestDemandBulletinBoardInitInfo")
public class RequestBulletinBoardInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{ 
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("수요 게시판 초기화 서블릿 동작");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String tableName = "demand_bulletinboard_table";	
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());		// 데이터 베이스 연결을 위한 드라이버 매니저 등록
			conn = DriverManager.getConnection(		// 데이터베이스 연결 정보 초기화
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
			// 수요 게시판의 데이터를 검색합니다.
			stmt = conn.createStatement();
			rs = stmt.executeQuery(
					"SELECT b_no, b_title, b_date, b_user, b_period, b_content" + 
					" FROM " + tableName+
					" ORDER BY b_no ASC");
			
			response.setContentType("text/html; charset=UTF-8");
			HashMap<String, HashMap<String, String>> dataMap = new HashMap<String, HashMap<String, String>>();
		
			while(rs.next()) {		
				/*
				 	데이터를 한번에 빠르게 보내기 위해 
				 	아래 HashMap들을 한번에 묶어서 보냅니다.
				 */
				HashMap<String, String> stringDataMap = new HashMap<String, String>();
				stringDataMap.put("title", rs.getString("b_title"));
				stringDataMap.put("date", rs.getString("b_date"));
				stringDataMap.put("user", rs.getString("b_user"));
				stringDataMap.put("period", rs.getString("b_period"));
				stringDataMap.put("content", rs.getString("b_content"));
				dataMap.put(rs.getInt("b_no")+"", stringDataMap);
			}
			response.setContentType("application/octet-stream");	
			/*
			  	Object로 한번에 보내서 데이터의 손실을 최소화
			  	여러개를 묶어서 보내는 거를 테스트했었을 때 제일 안정적으로 동작한다.
			 */
			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos =new ObjectOutputStream(servletOutputStream);
			oos.writeObject(dataMap);
			oos.flush();
			oos.close();
			servletOutputStream.flush();
			} catch (Exception e) {
				throw new ServletException(e);
			} finally {
				/*
				 	마지막에 ResultSet, 데이터베이스의 연결, Statement를 종료
				 */
				try {if (rs != null) rs.close();} catch(Exception e) {}
				try {if (stmt != null) stmt.close();} catch(Exception e) {}
				try {if (conn != null) conn.close();} catch(Exception e) {}
			}
		}
}