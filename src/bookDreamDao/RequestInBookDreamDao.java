package bookDreamDao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vo.RequestInBookDream;


public interface RequestInBookDreamDao {
	void init() throws Exception;
	HashMap<String,HashMap<String, String>> selectList() throws Exception;
	int insert(RequestInBookDream request) throws Exception;
	int delete(int no) throws Exception;
	public int update(int no) throws Exception;
	RequestInBookDream selectOne(int no) throws Exception;
	//RequestInBookDream exist(String email, String password) throws Exception;
}
