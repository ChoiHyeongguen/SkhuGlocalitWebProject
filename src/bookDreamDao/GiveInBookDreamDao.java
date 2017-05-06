package bookDreamDao;


import java.util.HashMap;
import vo.GiveInBookDream;


public interface GiveInBookDreamDao {
	void init() throws Exception;
	HashMap<String,HashMap<String, String>> selectList() throws Exception;
	int insert(GiveInBookDream give) throws Exception;
	int delete(int no) throws Exception;
	public int delete(String user, String title) throws Exception;	// delete함수 오버로딩 << 이름과 과목으로 테이블 삭제하게 하려고 생성
	public int update(int no) throws Exception;
	GiveInBookDream selectOne(int no) throws Exception;
	//GiveInBookDream exist(String email, String password) throws Exception;
}
