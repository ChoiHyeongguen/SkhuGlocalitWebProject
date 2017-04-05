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

import memberDao.MySqlMemberDao;
import vo.Member;

@WebServlet("/member/login")
public class LoginServlet extends HttpServlet {
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
		System.out.println("로그인 서블릿 동작 중");
		System.out.println("변경확인용도");

		try {

	    	ServletContext sc = this.getServletContext();
	    	MySqlMemberDao memberDao = (MySqlMemberDao)sc.getAttribute("memberDao");
	    	
	    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
			HashMap<String, String> memberInfoDataMap = (HashMap<String,String>)ois.readObject();
			
			Member member = memberDao.exist(memberInfoDataMap.get("id"),memberInfoDataMap.get("pw"));
			
			HashMap findMemberInfoDataMap = new HashMap<>();
			if(member!=null) {
				findMemberInfoDataMap.put("id", member.getId());
				findMemberInfoDataMap.put("name", member.getName());
				findMemberInfoDataMap.put("email", member.getEmail());
			}
			response.setContentType("application/octet-stream");
			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(servletOutputStream);
			oos.writeObject(findMemberInfoDataMap);

			servletOutputStream.flush();
			
/*			oos.writeObject(idList);
			oos.flush();
			System.out.println("아이디 리스트 보냄");
			oos.writeObject(pwList);
			oos.flush();
			System.out.println("비밀번호 리스트 보냄");
			oos.writeObject(nameList);
			oos.flush();
			System.out.println("이름 리스트 보냄");
			oos.writeObject(emailList);
			oos.flush();
			System.out.println("이메일 리스트 보냄");
			oos.close();
			*/
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		
		
		
		
		
	}
	
}
