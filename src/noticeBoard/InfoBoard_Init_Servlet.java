package noticeBoard;

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

import noticeBoardDao.MySqlInfoBoardDao;

// db에서 자유게시판을 구성하는 데이터를 불러와 안드로이드로 데이터를 전송하는 서블릿
// 정보게시판 초기화 서블릿
@WebServlet("/InfoNoticeBoard_Init")
public class InfoBoard_Init_Servlet extends HttpServlet{
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
		System.out.println("정보 게시판 <초기화> 서블릿 동작");
		
		try {
			ServletContext sc = this.getServletContext();
			MySqlInfoBoardDao infoBoardDao = (MySqlInfoBoardDao)sc.getAttribute("infoboardDao");
			HashMap<String,HashMap<String, String>> dataMap = infoBoardDao.selectList();
			
			// 정보 게시판의 데이터를 검색합니다.
			HashMap<String,byte[]> imgByteMap = new HashMap<String, byte[]>(); // 그림도 전송하기 때문에 byte 배열들을 저장하는 ArrayList 생성
						
			for(int i=0; i<dataMap.size(); i++) {
				HashMap<String, String> map = dataMap.get(i+"");
				String imgDes = map.get("img_des");
				File imgFile= new File(imgDes); 					// 이미지 파일을 여는 File 객체 생성
				byte[] bytes = new byte[(int) imgFile.length()];	// 파일 길이를 토대로 byte 배열 생성
				DataInputStream in = new DataInputStream(new FileInputStream(imgFile));	// 파일을 byte로 변경시키기위해 DataInputStream으로 변환	
				in.readFully(bytes);	// byte로 변환
				in.close();
				imgByteMap.put(imgDes,bytes);
			}
				
			ServletOutputStream servletOutputStream = response.getOutputStream();
			ObjectOutputStream oos =new ObjectOutputStream(servletOutputStream);
			oos.writeObject(dataMap);
			oos.flush();
			oos.writeObject(imgByteMap);
			oos.flush();
			oos.close();
			servletOutputStream.flush();
			System.out.println(">>>>>>>>> 정보게시판 데이터 전송 완료!");
			
		}catch (Exception e) {
			System.out.println("에러 : "+e.getMessage());
			System.out.println("에러 : "+e.getStackTrace());
		}
		
	}
	
}
