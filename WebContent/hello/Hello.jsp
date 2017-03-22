<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="java.sql.*" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>드디어 접속이 되셨습니다. 축하합니다 ㅠㅠ </h1>
	바뀐상황입니다 ^^
	<h2><font color='red'>war 배치가 빨라졌어요!!!</font></h2>
	<h1>데이터 베이스 접속 결과입니다.</h1>
	<%
		Connection conn =null;
	 	try{
	        String url = "jdbc:mysql://localhost:3306/glocalit_db";//생성한 데이타베이스 이름을 넣는다.
	        String id = "project"; // db에 접속하는 계정
	        String pw = "glocalit"; // db에 접속하는 계정의 비밀번호
	 
	        Class.forName("com.mysql.jdbc.Driver");
	        conn=DriverManager.getConnection(url, id, pw);
	        out.println("success");  // mysql에 연결되면 성공!!
	    }catch(Exception e){
	        out.println("fail" + e); // mysql에 연결 실패!!
	    }
	%>

</body>
</html>