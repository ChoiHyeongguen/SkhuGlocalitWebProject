package recommend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import recommendDao.MySqlRecommendDao;
import vo.Recommend;


@WebServlet("/recommend/AddRecommend")
public class RecommendAddServlet extends HttpServlet{
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
				System.out.println("추천 추가 서블릿 동작");
				
			    try {
			    	ServletContext sc = this.getServletContext();
			    	MySqlRecommendDao recommendDao = (MySqlRecommendDao)sc.getAttribute("recommendDao");

			    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
					HashMap<String, String> recommendDataMap = (HashMap<String, String>)ois.readObject();
					ois.close();
					System.out.println(recommendDataMap.toString());
					Recommend recommend = new Recommend();
					recommend.setMemberId(recommendDataMap.get("id"));
					recommend.setCategory(recommendDataMap.get("category"));
					recommend.setTitle(recommendDataMap.get("title"));
					recommend.setCallNumber(recommendDataMap.getOrDefault("callNumber", "미지정"));
					recommend.setDelivery(recommendDataMap.get("delivery"));
					recommend.setReview(recommendDataMap.getOrDefault("review", "내용 없음"));
					recommend.setLongitude(recommendDataMap.get("longitude"));
					recommend.setLatitude(recommendDataMap.get("latitude"));
					recommend.setUp(recommendDataMap.get("up"));
					recommend.setDown(recommendDataMap.get("down"));
//					System.out.println(recommend.getMemberId() +
//							"-" + recommend.getCategory() 	+ "-" + recommend.getTitle() + 
//							"-" + recommend.getCallNumber() + "-" + recommend.getDelivery() + 
//							"-" + recommend.getReview() 	+ "-" + recommend.getLongitude() +
//							"-" + recommend.getLatitude()	+ "-" + recommend.getUp() + "-" + recommend.getDown());
					recommendDao.insert(recommend);
					
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
			}

}
