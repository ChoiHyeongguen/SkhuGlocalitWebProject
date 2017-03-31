package vo;

import java.util.Date;

public class GiveInBookDream {
	protected int 		no;	
	protected String 	title;
	protected Date  	createdDate;
	protected String 	state;
	protected String 	grade;
	protected String	user;
	protected String 	imgDes;
	
	public int getNo() {
		return no;
	}
	public GiveInBookDream setNo(int no) {
		this.no = no;
		return this;
	}
	public String getTitle() {
		return title;
	}
	public GiveInBookDream setTitle(String title) {
		this.title = title;
		return this;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public GiveInBookDream setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
		return this;
	}
	public String getState() {
		return state;
	}
	public GiveInBookDream setState(String state) {
		this.state = state;
		return this;
	}
	public String getGrade() {
		return grade;
	}
	public GiveInBookDream setGrade(String grade) {
		this.grade = grade;
		return this;
	}
	public String getUser() {
		return user;
	}
	public GiveInBookDream setUser(String user) {
		this.user = user;
		return this;
	}
	public String getImgDes() {
		return imgDes;
	}
	public GiveInBookDream setImgDes(String des) {
		this.imgDes = des;
		return this;
	}

}
