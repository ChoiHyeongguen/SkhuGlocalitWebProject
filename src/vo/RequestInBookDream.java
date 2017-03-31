package vo;

import java.io.Serializable;
import java.util.Date;

public class RequestInBookDream {

	protected int 		no;	
	protected String 	title;
	protected Date  	createdDate;
	protected String 	user;
	protected String 	period;
	protected String	content;
	public int getNo() {
		return no;
	}
	public RequestInBookDream setNo(int no) {
		this.no = no;
		return this;
	}
	public String getTitle() {
		return title;
	}
	public RequestInBookDream setTitle(String title) {
		this.title = title;
		return this;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public RequestInBookDream setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
		return this;
	}
	public String getUser() {
		return user;
	}
	public RequestInBookDream setUser(String user) {
		this.user = user;
		return this;
	}
	public String getPeriod() {
		return period;
	}
	public RequestInBookDream setPeriod(String period) {
		this.period = period;
		return this;
	}
	public String getContent() {
		return content;
	}
	public RequestInBookDream setContent(String content) {
		this.content = content;
		return this;
	}

}
