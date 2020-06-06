package com.studentApplication.studentApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.studentApplication.studentApp.model.AuthenticationRequest;
import com.studentApplication.studentApp.model.AuthenticationResponse;
import com.studentApplication.studentApp.service.UserDetailsServiceImpl;
import com.studentApplication.studentApp.utils.JwtUtil;

@RestController
public class LoginController {
	
	public static final Logger log = LoggerFactory.getLogger(LoginController.class);

	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) {

		try {
			if(request != null && request.getUsername() != null && request.getPassword() != null) {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
						);

			}else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			final UserDetails userDetails = userDetailsService
					.loadUserByUsername(request.getUsername());

			final String jwt = jwtTokenUtil.generateToken(userDetails);

			return ResponseEntity.ok(new AuthenticationResponse(jwt));
		}catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
		}catch (Exception e) {
			log.error("Error during authentication", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to process request");
		}
	}
}
