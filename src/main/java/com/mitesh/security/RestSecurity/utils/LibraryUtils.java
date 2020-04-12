package com.mitesh.security.RestSecurity.utils;

public class LibraryUtils {

	public static boolean doesStringValueExists(String str) {
		
		if(str!=null && str.trim().length()>0)
			return true;
		else
			return false;
	}
	
}
