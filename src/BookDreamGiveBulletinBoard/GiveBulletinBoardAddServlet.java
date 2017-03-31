package BookDreamGiveBulletinBoard;

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

import dao.MySqlGiveInBookDreamDao;
import vo.GiveInBookDream;
/*
 	안드로이드 어플리케이션에서 추가해야할 게시판 글의 내용을 받아서
  	데이터베이스에 추가하는 서블릿(게시판 글 추가 역할)
  	또한 안드로이드 어플리케이션으로부터 byte배열을 받아 이걸 이용해 그림 파일들을
  	실제 서버에 지정된 경로에 저장합니다.
 	초기화 코드의 주석과 중복되는 내용들은 생략
*/
@WebServlet("/bookdream/addGiveInfo")
public class GiveBulletinBoardAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8"); 
		System.out.println("기부 게시판 글 추가 서블릿 동작");	

		String dirName ="/var/lib/tomcat8/webapps/bookdream/";		// 그림파일들이 실제로 저장될 경로 - 그림 임시 저장소
		System.out.println(dirName);
		/*
		System.out.println(System.getProperty("user.dir"));
		System.out.println(System.getProperty("user.home"));
		File dir = new File(dirName);
        if(!dir.exists()){
        	dir.mkdir();
        }*/
     
        try {
	    	ServletContext sc = this.getServletContext();
	    	MySqlGiveInBookDreamDao giveInBookDreamDao = (MySqlGiveInBookDreamDao)sc.getAttribute("giveInBookDreamDao");

	    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());
	    	HashMap<String, String> giveDataMap = (HashMap<String, String>) ois.readObject();
       		ArrayList<byte[]> giveImgDataList = (ArrayList<byte[]>) ois.readObject();

	    	HashMap<String, String> responseDataMap = new HashMap<String, String>();
       		ArrayList<byte[]> responseImgDataList = new ArrayList<byte[]>();
       		

			if(giveInBookDreamDao.insert(new GiveInBookDream()
					.setNo(Integer.parseInt(giveDataMap.get("no")))
					.setTitle(giveDataMap.get("title"))
					.setState(giveDataMap.get("state"))
					.setGrade(giveDataMap.get("grade"))
					.setUser(giveDataMap.get("user"))
					.setImgDes(dirName+giveDataMap.get("file_name"))	)!=0) {

			 	//byte배열을 이용해 png 형식의 이미지를 생성합니다.
				BufferedImage imag = ImageIO.read(new ByteArrayInputStream(giveImgDataList.get(0)));
				ImageIO.write(imag, "png", new File(dirName, giveDataMap.get("file_name")));
				responseDataMap = giveDataMap;
				responseImgDataList = giveImgDataList;
			}
			//response.setContentType("application/octet-stream");	
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
			/*
			throw new ServletException(e);*/
			
		} 
	}
}
