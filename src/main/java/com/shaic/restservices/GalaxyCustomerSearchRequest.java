package com.shaic.restservices;

public class GalaxyCustomerSearchRequest {

	private String agentId;
	private String search;
	private String value;
	private Integer pageNo;
	private Integer offset;
	private String username;
	private String password;
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "GalaxyCustomerSearchRequest [agentId=" + agentId + ", search="
				+ search + ", value=" + value + ", pageNo=" + pageNo
				+ ", offset=" + offset + ", username=" + username
				+ ", password=" + password + "]";
	}
	
}