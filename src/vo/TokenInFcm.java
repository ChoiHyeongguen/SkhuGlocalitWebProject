package vo;

import java.util.Date;

public class TokenInFcm {
	protected String 	token;	
	protected String 	user;
	protected Date  	createdDate;
	public String getToken() {
		return token;
	}
	public TokenInFcm setToken(String token) {
		this.token = token;
		return this;
	}
	public String getUser() {
		return user;
	}
	public TokenInFcm setUser(String user) {
		this.user = user;
		return this;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public TokenInFcm setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
		return this;
	}
}
