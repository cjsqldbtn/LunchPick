package com.nh.lunch.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAccountCredentials {
	private String username; // -> 사실은 "Principal"에 해당
	private String password; // -> 사실은 "Credentail"에 해당
	
}
