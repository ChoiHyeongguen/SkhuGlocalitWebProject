package members;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import memberDao.MySqlMemberDao;
import vo.Member;

@WebServlet("/member/compareId")
public class CheckOverlapIDServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("중복확인 위한 ID로드 동작");
		System.out.println("확인용도");
		
		try {

	    	ServletContext sc = this.getServletContext();
	    	MySqlMemberDao memberDao = (MySqlMemberDao)sc.getAttribute("memberDao");
	    	
	    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
			ArrayList<String> idInfo = (ArrayList<String>) ois.readObject();
			System.out.println("받은 데이터" + idInfo.get(0));
			Member member = memberDao.selectOne(idInfo.get(0));
			String result = "";
			if(member==null)
				result = "not exist";
			else
				result = "exist";

			ArrayList<String> resultInfo = new ArrayList<>();
			resultInfo.add(result);

			System.out.println("보낸 데이터" + resultInfo.get(0));
			response.setContentType("application/octet-stream");
			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(servletOutputStream);
			oos.writeObject(resultInfo);
			oos.flush();
			oos.close();
			servletOutputStream.flush();
			
		} catch (Exception e) {
			throw new ServletException(e);
		} 
	}
	
	

}
