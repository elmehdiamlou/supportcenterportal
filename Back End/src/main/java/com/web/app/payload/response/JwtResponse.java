package com.web.app.payload.response;



public class JwtResponse {
	private String accessToken;
	private String userName;
	private String role;
	
	public JwtResponse(String accessToken, String userName, String role) {
		super();
		this.accessToken = accessToken;
		this.userName = userName;
		this.role = role;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	


	


	

	
}
