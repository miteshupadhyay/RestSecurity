package com.mitesh.security.RestSecurity.user;

public enum Role {

	ADMIN("Admin"),
	USER("User");
	
	
	private String roleType;
	
	Role(String roleType){
		this.roleType=roleType;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
}
