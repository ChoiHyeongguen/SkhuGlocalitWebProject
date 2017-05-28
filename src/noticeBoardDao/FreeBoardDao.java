package noticeBoardDao;

import java.util.HashMap;

import vo.FreeBoard;


public interface FreeBoardDao {
	
	void init() throws Exception;
	HashMap<String,HashMap<String, String>> selectList() throws Exception;
	int insert(FreeBoard request) throws Exception;
	int delete(int no) throws Exception;
	public int delete(String user, String title) throws Exception;
	public int update(int no) throws Exception;
	FreeBoard selectOne(int no) throws Exception;
	int freeBoardUpdate(FreeBoard request) throws Exception;
	
}
