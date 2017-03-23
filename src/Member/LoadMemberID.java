package Member;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/requestMemberID")
public class LoadMemberID extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("중복확인 위한 ID로드 동작");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String tableName = "member_table";
		String query;
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/glocalit_db",
					"project",
					"glocalit");
			stmt = conn.createStatement();
			query = "select * from " + tableName;
			rs = stmt.executeQuery(query);
			
			ArrayList<String> idList = new ArrayList<String>();
			while(rs.next()){
				idList.add(rs.getString("id"));
				System.out.println(idList.toString());
			}
			response.setContentType("application/octet-stream");
			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(servletOutputStream);
			oos.writeObject(idList);
			oos.flush();
			
			oos.close();
			servletOutputStream.flush();
			
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (conn != null) conn.close();} catch(Exception e) {}
		}
	}
	
	

}
