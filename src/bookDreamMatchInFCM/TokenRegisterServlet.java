package bookDreamMatchInFCM;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookdreamDao.MySqlTokenInFcmDao;
import vo.TokenInFcm;

@WebServlet("/fcm/registerToken")
public class TokenRegisterServlet  extends HttpServlet {
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
		System.out.println("fcm에 토큰 등록");	
	    String token = null;
		try {

	    	ServletContext sc = this.getServletContext();
	    	MySqlTokenInFcmDao tokenInFcmDao = (MySqlTokenInFcmDao)sc.getAttribute("tokenInFcmDao");

	    	// 안드로이드로부터 토큰을 받는다.
	        token = request.getParameter("token");
	        
	        if( token.equals("") ){
	            System.out.println("토큰값이 전달 되지 않았습니다.");
	        }else{
	        	tokenInFcmDao.insert(new TokenInFcm()
	        			.setToken(token)
	        			.setUser(request.getParameter("user")));
	        }
		} catch (Exception e) {
			System.out.println("에러 : "+ e);
		}
	}
}
