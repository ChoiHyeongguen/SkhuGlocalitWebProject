package recommendDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import vo.Recommend;

public class MySqlRecommendDao implements RecommendDAO {

	DataSource ds;

	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void init() throws Exception {

		String sql = "show tables like " + "'recommendlist'";
		try (Connection connection = ds.getConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			System.out.println("recommendlist 테이블 있는지 검색");
			if (rs.next() == false) {
				System.out.println("없다");
				sql = "CREATE TABLE recommendlist(category VARCHAR(20) NOT NULL, memberId VARCHAR(20) NOT NULL,"
						+ "title VARCHAR(45) NOT NULL, branch VARCHAR(20) NOT NULL, callNumber VARCHAR(20), "
						+ "delivery VARCHAR(10) NOT NULL, review VARCHAR(255) NOT NULL, "
						+ "longitude VARCHAR(15) NOT NULL, latitude VARCHAR(15) NOT NULL, "
						+ "up VARCHAR(10) NOT NULL, down VARCHAR(10) NOT NULL);";
				stmt.executeUpdate(sql);
				System.out.println("recommendlist 테이블 생성");
			}
		} catch (Exception e) {
			throw e;
		}
		sql = "show tables like " + "'updownlist'";
		try (Connection connection = ds.getConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			System.out.println("updownlist 테이블 있는지 검색");
			if (rs.next() == false) {
				System.out.println("없다");
				sql = "CREATE TABLE updownlist(title VARCHAR(45) NOT NULL, longitude VARCHAR(15) NOT NULL, "
						+ "latitude VARCHAR(15) NOT NULL, id VARCHAR(20) NOT NULL, state VARCHAR(10) NOT NULL);";
				stmt.executeUpdate(sql);
				System.out.println("updatelist 테이블 생성");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Recommend> selectList() throws Exception {
		System.out.println("selectList 진입");
		String sql = "SELECT title, longitude, latitude FROM recommendlist;";
		try (Connection connection = ds.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);
				ResultSet resultSet = stmt.executeQuery();) {

			ArrayList<Recommend> recommends = new ArrayList<Recommend>();

			while (resultSet.next()) {
				Recommend recommend = new Recommend();
				recommend.setTitle(resultSet.getString("title"));
				recommend.setLongitude(resultSet.getString("longitude"));
				recommend.setLatitude(resultSet.getString("latitude"));
				recommends.add(recommend);
			}

			return recommends;
		}
	}

	@Override
	public Recommend selectOne(Recommend recommend) throws Exception {
		System.out.println("selectOne 진입");
		String sql = "SELECT r.*, m.name FROM recommendlist r LEFT JOIN members m "
				+ "ON r.memberId = m.id WHERE title=? AND longitude=? AND latitude=?;";
		try (Connection connection = ds.getConnection(); 
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, recommend.getTitle());
			stmt.setString(2, recommend.getLongitude());
			stmt.setString(3, recommend.getLatitude());
			try (ResultSet resultSet = stmt.executeQuery();) {

				while (resultSet.next()) {
					recommend.setMemberId(resultSet.getString("memberId"));
					recommend.setCategory(resultSet.getString("category"));
					recommend.setTitle(resultSet.getString("title"));
					recommend.setBranch(resultSet.getString("branch"));
					recommend.setCallNumber(resultSet.getString("callNumber"));
					recommend.setDelivery(resultSet.getString("delivery"));
					recommend.setReview(resultSet.getString("review"));
					recommend.setLongitude(resultSet.getString("longitude"));
					recommend.setLatitude(resultSet.getString("latitude"));
					recommend.setUp(resultSet.getString("up"));
					recommend.setDown(resultSet.getString("down"));
					recommend.setMemberName(resultSet.getString("name"));
				}

				return recommend;
			}
		}
	}

	@Override
	public void insert(Recommend recommend) throws Exception {
		System.out.println("insert 진입");
		String sql = "INSERT INTO recommendlist(memberId, category, title, branch, callNumber, "
				+ "delivery, review, longitude, latitude, up, down) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
		try (Connection connection = ds.getConnection(); 
				PreparedStatement stmt = connection.prepareStatement(sql)) {

			stmt.setString(1, recommend.getMemberId());
			stmt.setString(2, recommend.getCategory());
			stmt.setString(3, recommend.getTitle());
			stmt.setString(4, recommend.getBranch());
			stmt.setString(5, recommend.getCallNumber());
			stmt.setString(6, recommend.getDelivery());
			stmt.setString(7, recommend.getReview());
			stmt.setString(8, recommend.getLongitude());
			stmt.setString(9, recommend.getLatitude());
			stmt.setString(10, recommend.getUp());
			stmt.setString(11, recommend.getDown());
			stmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	@Override
	public int update(Recommend recommend) throws Exception {
		System.out.println("update 진입");
		String sql = "UPDATE recommendlist SET category=?, callNumber=?, delivery=?,"
				+ " review=? WHERE title=? AND longitude=? AND latitude=?;";
		try (Connection connection = ds.getConnection(); 
				PreparedStatement stmt = connection.prepareStatement(sql)) {

			stmt.setString(1, recommend.getCategory());
			stmt.setString(2, recommend.getCallNumber());
			stmt.setString(3, recommend.getDelivery());
			stmt.setString(4, recommend.getReview());
			stmt.setString(5, recommend.getTitle());
			stmt.setString(6, recommend.getLongitude());
			stmt.setString(7, recommend.getLatitude());
			return stmt.executeUpdate();

			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	@Override
	public int delete(Recommend recommend) throws Exception {
		System.out.println("delete 진입");
		String sql = "DELETE FROM recommendlist WHERE title=? AND longitude=? AND latitude=?;";
		try (Connection connection = ds.getConnection(); 
				PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			stmt.setString(1, recommend.getTitle());
			stmt.setString(2, recommend.getLongitude());
			stmt.setString(3, recommend.getLatitude());
			return stmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;

		}
	}
	
	@Override
	public void updown_update(Recommend recommend) throws Exception {
		System.out.println("updown_update 진입");
		String sql = "UPDATE recommendlist SET up=?, down=? WHERE title=? AND longitude=? AND latitude=?;";
		try (Connection connection = ds.getConnection(); 
				PreparedStatement stmt = connection.prepareStatement(sql)) {

			stmt.setString(1, recommend.getUp());
			stmt.setString(2, recommend.getDown());
			stmt.setString(3, recommend.getTitle());
			stmt.setString(4, recommend.getLongitude());
			stmt.setString(5, recommend.getLatitude());
			stmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	@Override
	public void updown_add(Recommend recommend, String id, String state) throws Exception {
		System.out.println("updown_add 진입");
		String sql = "INSERT INTO updownlist (title, longitude, latitude, id, state) "
				+ "VALUES (?,?,?,?,?);";
		try (Connection connection = ds.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql)){
			stmt.setString(1, recommend.getTitle());
			stmt.setString(2, recommend.getLongitude());
			stmt.setString(3, recommend.getLatitude());
			stmt.setString(4, id);
			stmt.setString(5, state);
			stmt.executeUpdate();
			
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void updownState_update(Recommend recommend, String id, String state) throws Exception {
		System.out.println("updownState_update 진입");
		String sql = "UPDATE updownlist SET state=? WHERE title=? AND longitude=? AND latitude=? AND id=?";
		try (Connection connection = ds.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql)){
			stmt.setString(1, state);
			stmt.setString(2, recommend.getTitle());
			stmt.setString(3, recommend.getLongitude());
			stmt.setString(4, recommend.getLatitude());
			stmt.setString(5, id);
			stmt.executeUpdate();
		}
	}
	
	@Override
	public String updown_load(Recommend recommend, String id) throws Exception {
		System.out.println("updown_load 진입");
		String sql = "SELECT id, state FROM updownlist WHERE title=? AND longitude=? AND latitude=?";
		try(Connection connection = ds.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql)){
			stmt.setString(1, recommend.getTitle());
			stmt.setString(2, recommend.getLongitude());
			stmt.setString(3, recommend.getLatitude());
			try(ResultSet resultSet = stmt.executeQuery()){
				String state = "not";
				while(resultSet.next()){
					if(resultSet.getString("id").equals(id)){
						state = resultSet.getString("state");
					}
				}
				return state;
			}
			
		} catch(Exception e){
			throw e;
		}
	}

	@Override
	public HashMap<Integer, HashMap<String, String>> search(int i, Recommend recommend) throws Exception {
		System.out.println("search 진입");
		String[] sql = {"SELECT title, longitude, latitude, up, down FROM recommendlist WHERE category=?;",
						"SELECT title, longitude, latitude, up, down FROM recommendlist WHERE category=? AND title=?;",
						"SELECT title, longitude, latitude, up, down FROM recommendlist WHERE category=? AND delivery=?;",						
						"SELECT title, longitude, latitude, up, down FROM recommendlist WHERE category=? AND title=? AND delivery=?;"};
		try(Connection connection = ds.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql[i])){
			if(i == 0){
				stmt.setString(1, recommend.getCategory());
			}else if(i == 1){
				stmt.setString(1, recommend.getCategory());
				stmt.setString(2, recommend.getTitle());
			}else if(i == 2){
				stmt.setString(1, recommend.getCategory());
				stmt.setString(2, recommend.getDelivery());
			}else if(i == 3){
				stmt.setString(1, recommend.getCategory());
				stmt.setString(2, recommend.getTitle());
				stmt.setString(3, recommend.getDelivery());
			}
			try(ResultSet resultSet = stmt.executeQuery()){
				HashMap<Integer, HashMap<String, String>> searchMap = new HashMap<Integer, HashMap<String, String>>();

				int j = 0;
				while(resultSet.next()){
					HashMap<String, String> searchOne = new HashMap<String, String>();
					searchOne.put("title", resultSet.getString("title"));
					searchOne.put("longitude", resultSet.getString("longitude"));
					searchOne.put("latitude", resultSet.getString("latitude"));
					searchOne.put("up", resultSet.getString("up"));
					searchOne.put("down", resultSet.getString("down"));
					searchMap.put(j++, searchOne);
					
				}
				return searchMap;
			}
		}
	}
}
