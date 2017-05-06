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

@WebServlet("/bookdream/giveMatch")
public class GiveMatchInBookDreamServlet extends HttpServlet {
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
	    String title = dataMap.get("title");
	    String giveUser = dataMap.get("giveUser");
	    String requestUser = dataMap.get("requestUser");
	    String date = dataMap.get("date");
	    String time = dataMap.get("time");
	    String where = dataMap.get("where");
	    String content = dataMap.get("content");
	    String phone = dataMap.get("phone");
	    /*
	     	code는 요청한 사람이 수락할 때 
	     	삭제해야할 게시판을 결정하기 위한 변수
	     	그래서 기부게시판에 요청한 사람에게 승인을 하면 
	     	code에 값이 들어가게 됩니다.
	     */
	    String code ="";
	    if(dataMap.get("state")!=null) 	
	    	code = dataMap.get("state");
	    String msg = giveUser + " 님이 "+ title+" 책을 DREAM합니다!";
	    if(giveUser==null || giveUser.equals("")){
	    	title="";
	        giveUser="";
		    requestUser = "";
		    date =  "";
		    time =  ""; 
		    where =  "";  
		    content =  "";  
		    phone =  "";   
	    }

        System.out.println("토큰들어옴"+requestUser);
	    giveUser = new String(giveUser.getBytes("UTF-8"), "UTF-8");   //메시지 한글깨짐 처리
	    requestUser = new String(requestUser.getBytes("UTF-8"), "UTF-8");  
	    date = new String(date.getBytes("UTF-8"), "UTF-8");  
	    time = new String(time.getBytes("UTF-8"), "UTF-8");  
	    where = new String(where.getBytes("UTF-8"), "UTF-8");  
	    content = new String(content.getBytes("UTF-8"), "UTF-8");  
	    phone = new String(phone.getBytes("UTF-8"), "UTF-8");  
	    msg = new String(msg.getBytes("UTF-8"), "UTF-8");  
	    
	    TokenInFcm tokenInFcm= tokenInFcmDao.selectOne(requestUser);
		String token ="";
		if(tokenInFcm != null)
		token = tokenInFcm.getToken();
		
		System.out.println(title +" " + giveUser +" "+where);
		System.out.println(requestUser +date+ " content");
	    Sender sender = new Sender(simpleApiKey);
	    Message message = new Message.Builder()
	        .collapseKey(MESSAGE_ID)
	        .delayWhileIdle(SHOW_ON_IDLE)
	        .timeToLive(LIVE_TIME)
            .addData("title", title)
            .addData("giveUser", giveUser)
            .addData("requestUser", requestUser)
            .addData("date", date)
            .addData("time", time)
            .addData("where", where)
            .addData("content", content)
            .addData("phone", phone)
            .addData("state", code+"giveMatch")
            .addData("message",msg)
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
