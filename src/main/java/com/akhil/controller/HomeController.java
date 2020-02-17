package com.akhil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.akhil.MyUserDetailsService;
import com.akhil.modal.AuthenticationRequest;
import com.akhil.modal.AuthenticationResponce;
import com.akhil.util.JwtUtil;

@RestController
public class HomeController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired 
	private MyUserDetailsService userService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping("/")
	public String home() {
		return "<h1>Welcome</h1>";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "<h1>Welcome user</h1>";
	}
	
	@RequestMapping("/admin")
	public String admin() {
		return "<h1>Welcome admin</h1>";
	}
	
	
	@RequestMapping(value="/authenticate" , method = RequestMethod.POST)
	public ResponseEntity<AuthenticationResponce> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
		}
		catch (Exception e) {
			throw new Exception("Username / password incorrect");
		}
		
		UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUserName());
		String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponce(jwt));
	}
	
}
