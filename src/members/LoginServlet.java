package members;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fcmConfigure.MySqlTokenInFcmDao;
import memberDao.MySqlMemberDao;
import vo.Member;
import vo.TokenInFcm;

@WebServlet("/member/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
			
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("로그인 서블릿 동작 중");
		System.out.println("변경확인용도");

		try {

	    	ServletContext sc = this.getServletContext();
	    	MySqlMemberDao memberDao = (MySqlMemberDao)sc.getAttribute("memberDao");
	    	MySqlTokenInFcmDao tokenInFcmDao = (MySqlTokenInFcmDao)sc.getAttribute("tokenInFcmDao");

	    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
			
			HashMap<String, String> memberInfoDataMap = (HashMap<String,String>)ois.readObject();
			HashMap<String, String> findMemberInfoDataMap = new HashMap<String, String>();
			
			if(memberInfoDataMap != null) {

				String token = memberInfoDataMap.get("token");
				if(token!=null) {
					Member member = memberDao.exist(memberInfoDataMap.get("id"),memberInfoDataMap.get("pw"));
					if(member!=null) {
						tokenInFcmDao.update(member.getId()+" " + member.getName() , token);
						findMemberInfoDataMap.put("id", member.getId());
						findMemberInfoDataMap.put("name", member.getName());
						findMemberInfoDataMap.put("email", member.getEmail());
					}
				} 
			}
			System.out.println("보냄!");
			response.setContentType("application/octet-stream");
			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(servletOutputStream);
			oos.writeObject(findMemberInfoDataMap);

			servletOutputStream.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		
		
		
		
	}
	
}
