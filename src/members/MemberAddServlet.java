package members;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import memberDao.MySqlMemberDao;
import vo.Member;

@WebServlet("/member/addMemberInfo")
public class MemberAddServlet extends HttpServlet{
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
		System.out.println("멤버 추가 서블릿 동작");
		
	    try {
	    	ServletContext sc = this.getServletContext();
	    	MySqlMemberDao memberDao = (MySqlMemberDao)sc.getAttribute("memberDao");

	    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
			HashMap<String, String> memberInfoDataMap = (HashMap<String, String>)ois.readObject();
			
			memberDao.insert(new Member()
					.setId(memberInfoDataMap.get("id"))
					.setPassword(memberInfoDataMap.get("pw"))
					.setName(memberInfoDataMap.get("name"))
					.setEmail(memberInfoDataMap.get("email")));

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
}
