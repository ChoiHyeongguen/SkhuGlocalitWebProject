package noticeBoardDao;

import java.util.HashMap;

import vo.InfoBoard;

public interface InfoBoardDao {
	
	void init() throws Exception;
	HashMap<String,HashMap<String, String>> selectList() throws Exception;
	int insert(InfoBoard give) throws Exception;
	int delete(int no) throws Exception;
	public int delete(String user, String title) throws Exception;
	public int update(int no) throws Exception;
	InfoBoard selectOne(int no) throws Exception;
	int infoBoardUpdate(InfoBoard request) throws Exception;
	
}
