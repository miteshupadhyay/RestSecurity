package com.mitesh.security.RestSecurity.utils;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import com.auth0.jwt.JWT;
import com.mitesh.security.RestSecurity.security.SecurityConstants;

public class LibraryUtils {

	public static boolean doesStringValueExists(String str) {
		
		if(str!=null && str.trim().length()>0)
			return true;
		else
			return false;
	}

	public static boolean isUserAdmin(String bearerToken) {
		String role=JWT.require(HMAC512(SecurityConstants.SIGNING_SECRET.getBytes()))
                .build()
                .verify(bearerToken.replace(SecurityConstants.BEARER_TOKEN_PREFIX, ""))
                .getClaim("role").asString();
		
		return role.equals("ADMIN");
	}

	public static Integer getUserIdFromClaim(String bearerToken) {
		return JWT.require(HMAC512(SecurityConstants.SIGNING_SECRET.getBytes()))
                .build()
                .verify(bearerToken.replace(SecurityConstants.BEARER_TOKEN_PREFIX, ""))
                .getClaim("userId").asInt();	
	}
	
}
