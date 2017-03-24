package Member;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/addMemberInfo")
public class AddMember extends HttpServlet{
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
		System.out.println("회원 가입 서블릿 동작");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String tableName = "member_table";
		String query;
		try{
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/glocalit_db",
					"project",
					"glocalit");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("show tables like " + "'" + tableName + "'");
			System.out.println(tableName + " 테이블 있는지 검색");
			if(rs.next() == false){
				System.out.println("없다");
				query = "CREATE TABLE " + tableName + 
						"(id VARCHAR(20)  NOT NULL, pw VARCHAR(30) NOT NULL, name VARCHAR(20) NOT NULL, email VARCHAR(40) NOT NULL);";
				stmt.executeUpdate(query);
				System.out.println(tableName + " 테이블 생성");
			}
			
			ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
			HashMap<String, String> stringDataMap = (HashMap<String, String>)ois.readObject();
			
			query = "insert into " + tableName + "(id, pw, name, email) values(?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt = conn.prepareStatement(query);
			System.out.println("가입 아이디 : "+stringDataMap.get("id"));
			pstmt.setString(1, stringDataMap.get("id"));
			pstmt.setString(2, stringDataMap.get("pw"));
			pstmt.setString(3, stringDataMap.get("name"));
			pstmt.setString(4, stringDataMap.get("email"));
			pstmt.executeUpdate();

			System.out.println("서블릿 종료");
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (conn != null) conn.close();} catch(Exception e) {}
		}
	}
}
