package memberDao;

// MemberDao 인터페이스 정의 
import java.util.List;
import vo.Member;

public interface MemberDao {
	void init() throws Exception;
	List<Member> selectlList() throws Exception;
	void insert(Member member) throws Exception;
	Member selectOne(String id) throws Exception;
	int update(Member member) throws Exception;
	Member exist(String id, String password) throws Exception;
}
