package com.akhil.modal;

public class AuthenticationResponce {

	private String JWT;

	public AuthenticationResponce(String jWT) {
		super();
		JWT = jWT;
	}

	public String getJWT() {
		return JWT;
	}
	
}
