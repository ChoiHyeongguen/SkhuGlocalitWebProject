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

import noticeBoardDao.MySqlInfoBoardDao;
import vo.InfoBoard;

// 정보게시판 수정 서블릿
@WebServlet("/InfoNoticeBoard_Update")
public class InfoBoard_Update_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		System.out.println("정보게시판 <글 수정> 서블릿 동작");
		
		String dirName ="/var/lib/tomcat8/webapps/freenoticeboard/"; // 이미지 저장 경로
		
		try{
			ServletContext sc = this.getServletContext();
	    	MySqlInfoBoardDao infoBoardDao = (MySqlInfoBoardDao)sc.getAttribute("infoboardDao");
			
	    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
	    	HashMap<String, String> requestDataMap = (HashMap<String, String>) ois.readObject();
	    	ArrayList<byte[]> giveImgDataList = (ArrayList<byte[]>) ois.readObject();
	    		System.out.println(requestDataMap.toString());
			
	    	HashMap<String, String> responseDataMap = new HashMap<String, String>();
	    	ArrayList<byte[]> responseImgDataList = new ArrayList<byte[]>();
	    	InfoBoard infoboard = new InfoBoard();
	    	
	    	infoboard.setNo(Integer.parseInt(requestDataMap.get("no")));
	    	infoboard.setTitle(requestDataMap.get("title"));
	    	infoboard.setUserName(requestDataMap.get("user"));
	    	infoboard.setDate(requestDataMap.get("date"));
	    	infoboard.setContent(requestDataMap.get("content"));
	    	infoboard.setImgDes(dirName + requestDataMap.get("file_name"));
	    	
	    	System.out.println("글번호:"+infoboard.getNo()+"/" + "글제목:"+infoboard.getTitle()+"/" + 
	    						"유저:"+infoboard.getUserName()+"/" + "날짜:"+infoboard.getDate()+"/" + 
	    						"글내용:"+infoboard.getContent()+"/" + "이미지경로:"+infoboard.getImgDes());

	    	if(infoBoardDao.infoBoardUpdate(infoboard) != 0){
	    		//byte배열을 이용해 png 형식의 이미지를 생성합니다.
				BufferedImage imag = ImageIO.read(new ByteArrayInputStream(giveImgDataList.get(0)));
				ImageIO.write(imag, "png", new File(dirName, requestDataMap.get("file_name")));
				responseDataMap = requestDataMap;
				responseImgDataList = giveImgDataList;
	    	}

			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(servletOutputStream);
			oos.writeObject(responseDataMap);
			oos.flush();
			oos.writeObject(responseImgDataList);
			oos.flush();
			oos.close();
			servletOutputStream.flush();
			
		} catch (Exception e) {
			System.out.println(e);	// 에러 출력
		}
			
	}
	
}
