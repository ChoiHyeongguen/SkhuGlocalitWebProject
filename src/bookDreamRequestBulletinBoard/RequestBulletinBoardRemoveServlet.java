package bookDreamRequestBulletinBoard;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookDreamDao.MySqlRequestInBookDreamDao;
import vo.RequestInBookDream;
/*
 	안드로이드 어플리케이션에서 삭제해야할 게시판 글의 번호를 받아서 
 	조회 후 그 게시글에 해당하는 정보를 삭제해버리는 서블릿(게시판 글 삭제 역할)
 	초기화 코드의 주석과 중복되는 내용들은 생략
*/
@WebServlet("/bookdream/removeRequestInfo")
public class RequestBulletinBoardRemoveServlet extends HttpServlet {
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
		System.out.println("요청 게시판 글 삭제 서블릿 동작");	
		try {

	    	ServletContext sc = this.getServletContext();
	    	MySqlRequestInBookDreamDao requestInBookDreamDao = (MySqlRequestInBookDreamDao)sc.getAttribute("requestInBookDreamDao");

			/*
			 	안드로이드로부터 삭제해야할 묶여진 데이터를 받습니다.
			 */
			ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
			HashMap<String, String> dataMap = (HashMap<String, String>)ois.readObject();
			ois.close();
			int no = Integer.parseInt(dataMap.get("no"));
			RequestInBookDream requestInBookDream = requestInBookDreamDao.selectOne(no);
	    	
	    	
	    	
			// 글을 삭제하고
			requestInBookDreamDao.delete(no);
			// 삭제한 데이터의 유니크 값 이후 행들의 유니크넘버를 변경해준다.(앞으로 땡겨주는 역할)
			requestInBookDreamDao.update(no);
			
			// 그리고 삭제가 되고 바로 초기화 서블릿을 불러와서 초기화 해준다.
			RequestDispatcher rd =  getServletContext().getRequestDispatcher("/bookdream/initGiveInfo");
	        rd.forward(request, response);
		} catch (Exception e) {
				throw new ServletException(e);
		}
	}
}