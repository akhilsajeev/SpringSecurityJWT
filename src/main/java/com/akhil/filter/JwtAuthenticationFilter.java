package com.akhil.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.akhil.MyUserDetailsService;
import com.akhil.util.JwtUtil;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private MyUserDetailsService userdetailsService;
	
	@Autowired
	private JwtUtil jwtutil;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String autherizationHeader = request.getHeader("Autherization");
		
		String jwt = null , username = null;
		
		if(autherizationHeader!=null && autherizationHeader.startsWith("Bearer ")) {
			jwt = autherizationHeader.substring(7);
			username = jwtutil.getUsernameFromToken(jwt);
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			 UserDetails details =  userdetailsService.loadUserByUsername(username) ;
			 
			 if(jwtutil.validateToken(jwt, details)) {
				 UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
				 usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				 SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				 
			 }
		}
		
		filterChain.doFilter(request, response);
		
	}

}
