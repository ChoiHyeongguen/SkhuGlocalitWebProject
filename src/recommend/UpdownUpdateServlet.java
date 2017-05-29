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

@WebServlet("/recommend/UpdownUpdate")
public class UpdownUpdateServlet extends HttpServlet{

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
				System.out.println("updown 수정 서블릿 동작");
				
				 try {
				    	ServletContext sc = this.getServletContext();
				    	MySqlRecommendDao recommendDao = (MySqlRecommendDao)sc.getAttribute("recommendDao");

				    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
						HashMap<String, String> updateDataMap = (HashMap<String, String>)ois.readObject();
//						System.out.println(updateDataMap.toString());
						ois.close();
						String id = updateDataMap.get("id");
						String state = updateDataMap.get("state");
						Recommend recommend = new Recommend();
						recommend.setTitle(updateDataMap.get("title"));
						recommend.setLongitude(updateDataMap.get("longitude"));
						recommend.setLatitude(updateDataMap.get("latitude"));
						recommend.setUp(updateDataMap.get("up"));
						recommend.setDown(updateDataMap.get("down"));
						recommendDao.updown_update(recommend);
						if(recommendDao.updown_load(recommend, id).equals("not")){
							recommendDao.updown_add(recommend, id, state);
						} else {
							recommendDao.updownState_update(recommend, id, state);
						}
						
						
				    } catch (Exception e) {
				      e.printStackTrace();
				    }
			}

}
