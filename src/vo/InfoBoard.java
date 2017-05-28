package vo;

// 정보게시판
public class InfoBoard {
	
	protected int no;
	protected String title;
	protected String userName;
	protected String date;
	protected String content;
	protected String imgDes;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImgDes() {
		return imgDes;
	}
	public void setImgDes(String imgDes) {
		this.imgDes = imgDes;
	}
	
	
}
