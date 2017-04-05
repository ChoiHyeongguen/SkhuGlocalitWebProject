package listeners;

// 페이지 컨트롤러 객체 준비
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import memberDao.MySqlMemberDao;
import bookDreamDao.MySqlRequestInBookDreamDao;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
	@Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      ServletContext sc = event.getServletContext();

      InitialContext initialContext = new InitialContext();
      DataSource glocalDs = (DataSource)initialContext.lookup(		// glocalit_db에 대한 ds
          "java:comp/env/jdbc/glocalit_db");

      DataSource bookdreamDs = (DataSource)initialContext.lookup(		// bookdreamDs에 대한 ds
          "java:comp/env/jdbc/bookdream_db");
      // 각가의 ds를 dao에 주입하고 그것을 ServletContext 저장한다.
      MySqlMemberDao memberDao = new MySqlMemberDao();
      memberDao.setDataSource(glocalDs);
      memberDao.init();
      sc.setAttribute("memberDao", memberDao);
      
      
      MySqlRequestInBookDreamDao requestInbookDreamDao = new MySqlRequestInBookDreamDao();
      requestInbookDreamDao.setDataSource(bookdreamDs);
      requestInbookDreamDao.init();
      sc.setAttribute("requestInBookDreamDao", requestInbookDreamDao);
      
      
      
    } catch(Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {}
}
