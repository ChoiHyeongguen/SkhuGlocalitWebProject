package recommend;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import recommendDao.MySqlRecommendDao;
import vo.Recommend;

@WebServlet("/recommend/InitRecommendList")
public class RecommendInitServlet extends HttpServlet{
	
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
				System.out.println("위치 로드 서블릿 동작");
				
			    try {
			    	ServletContext sc = this.getServletContext();
			    	MySqlRecommendDao recommendDao = (MySqlRecommendDao)sc.getAttribute("recommendDao");

			    	boolean flag = true;
			    	List<Recommend> recommends = recommendDao.selectList();
			    	
			    	ServletOutputStream servletOutputStream = response.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(servletOutputStream);
			    	for(int i = 0; i < recommends.size();i++){
			    		HashMap<String, String> locationData = new HashMap<>();
			    		locationData.put("title", recommends.get(i).getTitle());
			    		locationData.put("longitude", recommends.get(i).getLongitude());
			    		locationData.put("latitude", recommends.get(i).getLatitude());
			    		oos.writeBoolean(flag);
			    		oos.writeObject(locationData);
			    		oos.flush();
//			    		System.out.println(flag + locationData.toString());
			    		locationData.clear();
			    	}
			    	flag = false;
			    	oos.writeBoolean(flag);
			    	oos.flush();
					oos.close();
					servletOutputStream.flush();
					servletOutputStream.close();

					
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
			}

}
