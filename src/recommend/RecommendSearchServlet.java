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

@WebServlet("/recommend/SearchRecommend")
public class RecommendSearchServlet extends HttpServlet{

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
				System.out.println("검색 서블릿 동작");
				
				try {
			    	ServletContext sc = this.getServletContext();
			    	MySqlRecommendDao recommendDao = (MySqlRecommendDao)sc.getAttribute("recommendDao");

			    	ObjectInputStream ois = new ObjectInputStream(request.getInputStream());	
					HashMap<String, String> searchDataMap = (HashMap<String, String>)ois.readObject();
					System.out.println(searchDataMap.toString());
					ois.close();
					
					Recommend recommend = new Recommend();
					recommend.setCategory(searchDataMap.get("category"));
					recommend.setTitle(searchDataMap.getOrDefault("title", null));
					recommend.setDelivery(searchDataMap.getOrDefault("delivery", null));
					int i = 0;
					if(recommend.getTitle()!=null && recommend.getDelivery()==null){
						i = 1;
					} else if(recommend.getTitle()==null && recommend.getDelivery()!=null){
						i = 2;
					} else if(recommend.getTitle()!=null && recommend.getDelivery()!=null){
						i = 3;
					}
//					System.out.println("인덱스"+i);
					HashMap<Integer, HashMap<String, String>> searchMap = recommendDao.search(i, recommend);
//					System.out.println(searchMap.toString());
					response.setContentType("application/octet-stream");
					ServletOutputStream servletOutputStream = response.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(servletOutputStream);
					oos.writeObject(searchMap);
					oos.flush();
					oos.close();
					servletOutputStream.flush();
					servletOutputStream.close();
					
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
			}
}
