package noticeBoard;

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

import noticeBoardDao.MySqlFreeBoardDao;
import vo.FreeBoard;

// 자유게시판 삭제 서블릿
@WebServlet("/FreeNoticeBoard_Remove")
public class FreeBoard_Remove_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("자유 게시판 <글 삭제> 서블릿 동작");
		
		try {

	    	ServletContext sc = this.getServletContext();
	    	MySqlFreeBoardDao freeBoardDao = (MySqlFreeBoardDao)sc.getAttribute("freeboardDao");

	    	// 안드로이드로부터 삭제해야 할 묶여진 데이터를 받아옴.
			ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
			HashMap<String, String> dataMap = (HashMap<String, String>)ois.readObject();
			ois.close();
			int no = Integer.parseInt(dataMap.get("no"));
			
			FreeBoard freeboard = freeBoardDao.selectOne(no);
	    	
			freeBoardDao.delete(no);	// 해당 글 삭제
			freeBoardDao.update(no);	// 삭제한 데이터의 유니크 값 이후 행들의 유니크 넘버를 변경해준다.
			
			// 자유 게시판의 글을 삭제할 때 실제로 그림이 저장되어있기 때문에 서버에 저장된 그림 파일도 실제로 삭제해준다.
			String fileDes = freeboard.getImgDes();
			File f = new File(fileDes);		
			f.delete();
						
			
			// 삭제가 되고 바로 초기화 서블릿을 불러와서 초기화 해준다.
			RequestDispatcher rd =  getServletContext().getRequestDispatcher("FreeNoticeBoard_Init");
	        rd.forward(request, response);
		} catch (Exception e) {
				throw new ServletException(e);
		}
		
	}
}
