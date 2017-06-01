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

@WebServlet("/recommend/SelectRecommend")
public class RecommendSelectServlet extends HttpServlet{

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
				System.out.println("선택 로드 서블릿 동작");
				
				try {
			    	ServletContext sc = this.getServletContext();
			    	MySqlRecommendDao recommendDao = (MySqlRecommendDao)sc.getAttribute("recommendDao");
			    	
			    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
					HashMap<String, String> selectDataMap = (HashMap<String, String>)ois.readObject();
					ois.close();
					
					Recommend recommend = new Recommend();
					recommend.setTitle(selectDataMap.get("title"));
					recommend.setLongitude(selectDataMap.get("longitude"));
					recommend.setLatitude(selectDataMap.get("latitude"));
					
					String state = recommendDao.updown_load(recommend, selectDataMap.get("id"));
					
					recommend = recommendDao.selectOne(recommend);
					HashMap<String, String> selectMap = new HashMap<>();
					selectMap.put("id", recommend.getMemberId());
					selectMap.put("name", recommend.getMemberName());
					selectMap.put("category", recommend.getCategory());
					selectMap.put("title", recommend.getTitle());
					selectMap.put("branch", recommend.getBranch());
					selectMap.put("callNumber", recommend.getCallNumber());
					selectMap.put("delivery", recommend.getDelivery());
					selectMap.put("review", recommend.getReview());
					selectMap.put("longitude", recommend.getLongitude());
					selectMap.put("latitude", recommend.getLatitude());
					selectMap.put("up", recommend.getUp());
					selectMap.put("down", recommend.getDown());
					selectMap.put("state", state);
					
					response.setContentType("application/octet-stream");
					ServletOutputStream servletOutputStream = response.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(servletOutputStream);
					oos.writeObject(selectMap);
					oos.flush();
					oos.close();
					servletOutputStream.flush();
					servletOutputStream.close();
					
				}catch (Exception e) {
				      e.printStackTrace();
				    }
			}

}
