package recommend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import recommendDao.MySqlRecommendDao;
import vo.Recommend;

@WebServlet("/recommend/DeleteRecommend")
public class RecommendDeleteServlet extends HttpServlet{
	
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
				System.out.println("추천 삭제 서블릿 동작");
				
				 try {
				    	ServletContext sc = this.getServletContext();
				    	MySqlRecommendDao recommendDao = (MySqlRecommendDao)sc.getAttribute("recommendDao");

				    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
						HashMap<String, String> recommendDataMap = (HashMap<String, String>)ois.readObject();
						System.out.println(recommendDataMap.toString());
						ois.close();
						
						Recommend recommend = new Recommend();
						recommend.setTitle(recommendDataMap.get("title"));
						recommend.setLongitude(recommendDataMap.get("longitude"));
						recommend.setLatitude(recommendDataMap.get("latitude"));
						
						int value = recommendDao.delete(recommend);
						ArrayList<String> resultData = new ArrayList<>();
						if(value == 1){
							resultData.add("success");
						}else {
							resultData.add("fail");
						}
						response.setContentType("application/octet-stream");
						ServletOutputStream servletOutputStream = response.getOutputStream();
						ObjectOutputStream oos = new ObjectOutputStream(servletOutputStream);
						oos.writeObject(resultData);
						oos.flush();
						oos.close();
						servletOutputStream.flush();
						servletOutputStream.close();
						
				    } catch (Exception e) {
				      e.printStackTrace();
				    }
			}

}
