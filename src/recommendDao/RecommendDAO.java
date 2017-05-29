package recommendDao;

import java.util.HashMap;
import java.util.List;
import vo.Recommend;

public interface RecommendDAO {
	void init() throws Exception;
	List<Recommend> selectList() throws Exception;
	Recommend selectOne(Recommend recommend) throws Exception;
	void insert(Recommend recommend) throws Exception;
	int update(Recommend recommend) throws Exception;
	int delete(Recommend recommend) throws Exception;
	void updown_update(Recommend recommend) throws Exception;
	void updown_add(Recommend recommend, String id, String state) throws Exception;
	void updownState_update(Recommend recommend, String id, String state) throws Exception; 
	String updown_load(Recommend recommend, String id) throws Exception;
	HashMap<Integer, HashMap<String, String>> search(int i, Recommend recommend) throws Exception;
}
