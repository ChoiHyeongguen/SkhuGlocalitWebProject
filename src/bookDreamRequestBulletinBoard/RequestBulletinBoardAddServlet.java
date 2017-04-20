package bookDreamRequestBulletinBoard;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookDreamDao.MySqlRequestInBookDreamDao;
import vo.RequestInBookDream;
/*
 	안드로이드 어플리케이션에서 추가해야할 게시판 글의 내용을 받아서
  	데이터베이스에 추가하는 서블릿(게시판 글 추가 역할)
 	초기화 코드의 주석과 중복되는 내용들은 생략
*/
@WebServlet("/bookdream/addRequestInfo")
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
		System.out.println("요청 게시판 글 추가 서블릿 동작");	

        try {
	    	ServletContext sc = this.getServletContext();
	    	MySqlRequestInBookDreamDao requestInBookDreamDao = (MySqlRequestInBookDreamDao)sc.getAttribute("requestInBookDreamDao");

	    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
	    	HashMap<String, String> requestDataMap = (HashMap<String, String>) ois.readObject();
	    	HashMap<String, String> responseDataMap = new HashMap<String, String>();

			if(requestInBookDreamDao.insert(new RequestInBookDream()
					.setNo(Integer.parseInt(requestDataMap.get("no")))
					.setTitle(requestDataMap.get("title"))
					.setUser(requestDataMap.get("user"))
					.setPeriod(requestDataMap.get("period"))
					.setContent(requestDataMap.get("content"))	)!=0) {
				responseDataMap = requestDataMap;
			}
			response.setContentType("application/octet-stream");	
			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos =new ObjectOutputStream(servletOutputStream);
			oos.writeObject(responseDataMap);
			oos.flush();
			oos.close();
			servletOutputStream.flush();

		} catch (Exception e) {
			System.out.println(e);	// 에러 출력용
			/*
			throw new ServletException(e);*/
			
		} 
	}
}