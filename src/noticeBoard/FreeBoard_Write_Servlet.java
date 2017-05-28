package noticeBoard;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noticeBoardDao.MySqlFreeBoardDao;
import vo.FreeBoard;

// 안드로이드 어플리케이션에서 추가해야할 게시판 글의 데이터를 받아와 db에 추가하는 서블릿
// 자유게시판 글 추가 서블릿
@WebServlet("/FreeNoticeBoard_Write")
public class FreeBoard_Write_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				System.out.println("자유게시판 <글 추가> 서블릿 동작");
				
				String dirName ="/var/lib/tomcat8/webapps/freenoticeboard/"; // 이미지 저장 경로
				System.out.println(dirName);
		
		try {
	    	ServletContext sc = this.getServletContext();
	    	MySqlFreeBoardDao freeBoardDao = (MySqlFreeBoardDao)sc.getAttribute("freeboardDao");

	    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
	    	HashMap<String, String> requestDataMap = (HashMap<String, String>) ois.readObject();
	    	ArrayList<byte[]> giveImgDataList = (ArrayList<byte[]>) ois.readObject();
	    		
	    	HashMap<String, String> responseDataMap = new HashMap<String, String>();
	    	ArrayList<byte[]> responseImgDataList = new ArrayList<byte[]>();
	    	FreeBoard freeboard = new FreeBoard();
	    	freeboard.setNo(Integer.parseInt(requestDataMap.get("no")));
	    	freeboard.setTitle(requestDataMap.get("title"));
	    	freeboard.setUserName(requestDataMap.get("user"));
	    	freeboard.setDate(requestDataMap.get("date"));
	    	freeboard.setContent(requestDataMap.get("content")); 
	    	freeboard.setImgDes(dirName + requestDataMap.get("file_name"));
	    	
	    	System.out.println("글번호:"+freeboard.getNo()+"/" + "글제목:"+freeboard.getTitle()+"/" + 
					"유저:"+freeboard.getUserName()+"/" + "날짜:"+freeboard.getDate()+"/" + 
					"글내용:"+freeboard.getContent()+"/" + "이미지경로:"+freeboard.getImgDes());
	    	
	    	if(freeBoardDao.insert(freeboard) != 0){
	    		//byte배열을 이용해 png 형식의 이미지를 생성합니다.
				BufferedImage imag = ImageIO.read(new ByteArrayInputStream(giveImgDataList.get(0)));
				ImageIO.write(imag, "png", new File(dirName, requestDataMap.get("file_name")));
				responseDataMap = requestDataMap;
				responseImgDataList = giveImgDataList;
	    	}
	    	
	    	ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos =new ObjectOutputStream(servletOutputStream);
			oos.writeObject(responseDataMap);
			oos.flush();
			oos.writeObject(responseImgDataList);
			oos.flush();
			oos.close();
			servletOutputStream.flush();

		} catch (Exception e) {
			System.out.println(e);	// 에러 출력용
			
		} 
	
	}
}
