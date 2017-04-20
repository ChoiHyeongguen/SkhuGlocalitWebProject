package fcmConfigure;


import java.util.ArrayList;
import java.util.HashMap;
import vo.TokenInFcm;

public interface TokenInFcmDao {
	void init() throws Exception;
	int insert(TokenInFcm token) throws Exception;
	int delete(String token) throws Exception;
	TokenInFcm selectOne(String token) throws Exception;
	public int update(String info, String token) throws Exception;
	//ArrayList<String> selectList() throws Exception;
	//GiveInBookDream exist(String email, String password) throws Exception;
}
