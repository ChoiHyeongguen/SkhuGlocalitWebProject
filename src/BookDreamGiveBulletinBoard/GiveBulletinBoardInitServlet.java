package BookDreamGiveBulletinBoard;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MySqlGiveInBookDreamDao;
/*
	데이터베이스에서 공급게시판을 구성하는 데이터를 불러와서
	안드로이드 어플리케이션으로 묶어서 전송하는 서블릿(게시판 초기화 역할)
	여기서는 수요게시판과 다르게 그림도 같이 전송한다.
*/
@WebServlet("/bookdream/initGiveInfo")
public class GiveBulletinBoardInitServlet extends HttpServlet {
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
	    	MySqlGiveInBookDreamDao giveInBookDreamDao = (MySqlGiveInBookDreamDao)sc.getAttribute("giveInBookDreamDao");
	    	HashMap<String,HashMap<String, String>> dataMap = giveInBookDreamDao.selectList();
			// 공급 게시판의 데이터를 검색합니다.
			HashMap<String,byte[]> imgByteMap = new HashMap<String, byte[]>();		// 그림도 전송하기 때문에 byte 배열들을 저장하는 ArrayList 생성
	
			for(int i=0; i<dataMap.size(); i++) {
				HashMap<String, String> map = dataMap.get(i+"");
				String imgDes = map.get("img_des");
				File imgFile= new File(imgDes); // 이미지 파일을 여는 File 객체 생성
				byte[] bytes = new byte[(int) imgFile.length()];	// 파일 길이를 토대로 byte 배열 생성
				DataInputStream in = new DataInputStream(new FileInputStream(imgFile));	// 파일을 byte로 변경시키기위해 DataInputStream으로 변환	
				in.readFully(bytes);	// byte로 변환
				in.close();
				imgByteMap.put(imgDes,bytes);
			}
			response.setContentType("application/octet-stream");	
			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos =new ObjectOutputStream(servletOutputStream);
			oos.writeObject(dataMap);
			oos.flush();
			oos.writeObject(imgByteMap);
			oos.flush();
			oos.close();
			servletOutputStream.flush();
			
		} catch (Exception e) {
				throw new ServletException(e);
		}
	}
}

