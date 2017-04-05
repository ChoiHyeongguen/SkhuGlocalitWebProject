

import java.util.HashMap;
import vo.GiveInBookDream;


public interface GiveInBookDreamDao {
	void init() throws Exception;
	HashMap<String,HashMap<String, String>> selectList() throws Exception;
	int insert(GiveInBookDream give) throws Exception;
	int delete(int no) throws Exception;
	public int update(int no) throws Exception;
	GiveInBookDream selectOne(int no) throws Exception;
	//GiveInBookDream exist(String email, String password) throws Exception;
}
