package bookDreamMatchInFCM;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import fcmConfigure.MySqlTokenInFcmDao;
import vo.TokenInFcm;
/*
  여기 디테일 하게 수정 후 
  공급게시판 매칭 구성하면 될 것 같음
 */
@WebServlet("/bookdream/cancelMatch")
public class CancelMatchInBookDreamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("토큰들어옴");
    	ServletContext sc = this.getServletContext();
    	MySqlTokenInFcmDao tokenInFcmDao = (MySqlTokenInFcmDao)sc.getAttribute("tokenInFcmDao");
	    String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);    //메시지 고유 ID
	    boolean SHOW_ON_IDLE = false;    //옙 활성화 상태일때 보여줄것인지
	    int LIVE_TIME = 1;    //옙 비활성화 상태일때 FCM가 메시지를 유효화하는 시간
	    int RETRY = 2;    //메시지 전송실패시 재시도 횟수

        System.out.println("토큰가능");
	    String simpleApiKey = "AAAAIb5usS0:APA91bHwQp4ONMOEFcc-IIOjC0ehgC0LJ9bSQGY9quJiTbCtA5kYziFORto_Js14-gYu_bsLWYy5uwVKoMzVrR-ofPnBlLsj8ZXlq6IbnL-adLe6UeND98R4b78j6EHyncAYbglDaprP";
	    String gcmURL = "https://android.googleapis.com/fcm/send";    
	    // 안드로이드로 부터 받은 데이터들..
		try {
	    ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
		HashMap<String, String> dataMap = (HashMap<String,String>)ois.readObject();
	    HashMap<String, String> stateDataMap = new HashMap<String, String>();
	    String giveUser = dataMap.get("giveUser");
	    String requestUser = dataMap.get("requestUser");
	    String reasonMsg =  dataMap.get("reason");
	    String msg = "정말 죄송합니다!" + requestUser +"님이 Book:Dream을 거절했습니다.!\n"
	    		+ "사유 : " + reasonMsg; 
	    msg = new String(msg.getBytes("UTF-8"), "UTF-8");   //메시지 한글깨짐 처리
	    requestUser = new String(requestUser.getBytes("UTF-8"), "UTF-8");  
	    giveUser = new String(giveUser.getBytes("UTF-8"), "UTF-8");  
	    reasonMsg =  new String(reasonMsg.getBytes("UTF-8"), "UTF-8"); 
	    
	    TokenInFcm tokenInFcm= tokenInFcmDao.selectOne(giveUser);
		String token ="";
		if(tokenInFcm != null)
		token = tokenInFcm.getToken();
	    
	    Sender sender = new Sender(simpleApiKey);
	    Message message = new Message.Builder()
	        .collapseKey(MESSAGE_ID)
	        .delayWhileIdle(SHOW_ON_IDLE)
	        .timeToLive(LIVE_TIME)
            .addData("message", msg)
            .addData("state", "cancel")
	        .build();
	    Result result = sender.send(message,token,RETRY);	// 요청은 한명에게만 하기때문에 Result를 사용함 여러명은 MultiResult
	    if (result.getErrorCodeName() != null) {
	    	System.out.println(result.getErrorCodeName()+"@@@"); 
	    	
	    } else {
	    	stateDataMap.put("state","success");
	    }


		//response.setContentType("application/octet-stream");	
		ServletOutputStream servletOutputStream = response.getOutputStream();
		ObjectOutputStream oos =new ObjectOutputStream(servletOutputStream);
		oos.writeObject(stateDataMap);
		oos.flush();
		oos.close();
		servletOutputStream.flush();

		
	} catch (Exception e) {
		System.out.println(e);
		System.out.println(e.getStackTrace());
	} 
}		
	     	

}
