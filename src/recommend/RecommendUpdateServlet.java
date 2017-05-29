package recommend;

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

import recommendDao.MySqlRecommendDao;
import vo.Recommend;

@WebServlet("/recommend/UpdateRecommend")
public class RecommendUpdateServlet extends HttpServlet{
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
				System.out.println("추천 수정 서블릿 동작");
				
				 try {
				    	ServletContext sc = this.getServletContext();
				    	MySqlRecommendDao recommendDao = (MySqlRecommendDao)sc.getAttribute("recommendDao");

				    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
						HashMap<String, String> updateDataMap = (HashMap<String, String>)ois.readObject();
						System.out.println(updateDataMap.toString());
						ois.close();
						
						Recommend recommend = new Recommend();
						recommend.setCategory(updateDataMap.get("category"));
						recommend.setTitle(updateDataMap.get("title"));
						recommend.setCallNumber(updateDataMap.getOrDefault("callNumber", "미지정"));
						recommend.setDelivery(updateDataMap.get("delivery"));
						recommend.setReview(updateDataMap.getOrDefault("review", "내용 없음"));
						recommend.setLongitude(updateDataMap.get("longitude"));
						recommend.setLatitude(updateDataMap.get("latitude"));
//						System.out.println(recommend.getCategory() 	+ "-" + recommend.getTitle() + 
//								"-" + recommend.getCallNumber() + "-" + recommend.getDelivery() + 
//								"-" + recommend.getReview() 	+ "-" + recommend.getLongitude() +
//								"-" + recommend.getLatitude());
						int value = recommendDao.update(recommend);
						HashMap<String, String> resultData = updateDataMap;
						if(value == 1){
							resultData.put("result", "success");
						}else {
							resultData.put("result", "fail");
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
