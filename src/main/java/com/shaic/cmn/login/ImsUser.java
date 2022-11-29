package com.shaic.cmn.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImsUser {
	
	private String userName;
	private String[] userRoleList;
	private Boolean authentication_flag;
	private String empName;
	
	public ImsUser(String userName, String[] userRoleList, Boolean authentication_flag){
		this.userName = userName;
		this.authentication_flag = authentication_flag;
		this.userRoleList = userRoleList;	
	}
	
	public ImsUser(){
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String[] getUserRoleList() {
		return userRoleList;
	}

	public List<String> getFilteredRoles()
	{
		List<String> rest = new ArrayList<String>(Arrays.asList(this.userRoleList));
		rest.remove("Admin");
		rest.remove("Internal/everyone");
		return rest;
	}
	public void setUserRoleList(String[] userRoleList) {
		this.userRoleList = userRoleList;
	}

	public Boolean getAuthentication_flag() {
		return authentication_flag;
	}

	public void setAuthentication_flag(Boolean authentication_flag) {
		this.authentication_flag = authentication_flag;
	}

	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
}
