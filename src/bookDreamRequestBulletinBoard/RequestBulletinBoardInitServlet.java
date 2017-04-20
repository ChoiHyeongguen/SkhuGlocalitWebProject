package bookDreamRequestBulletinBoard;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookDreamDao.MySqlGiveInBookDreamDao;
import bookDreamDao.MySqlRequestInBookDreamDao;
/*
	데이터베이스에서 수요게시판을 구성하는 데이터를 불러와서
	안드로이드 어플리케이션으로 묶어서 전송하는 서블릿(게시판 초기화 역할)
*/
@WebServlet("/bookdream/initRequestInfo")
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
		System.out.println("요청 게시판 초기화 서블릿 동작");
		
	    try {
	    	ServletContext sc = this.getServletContext();
	    	MySqlRequestInBookDreamDao requestInBookDreamDao = (MySqlRequestInBookDreamDao)sc.getAttribute("requestInBookDreamDao");
	    	HashMap<String,HashMap<String, String>> dataMap = requestInBookDreamDao.selectList();
		
			response.setContentType("application/octet-stream");	
			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos =new ObjectOutputStream(servletOutputStream);
			oos.writeObject(dataMap);
			oos.flush();
			oos.close();
			servletOutputStream.flush();
			
		} catch (Exception e) {
				throw new ServletException(e);
		}
	}
}