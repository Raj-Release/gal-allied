package com.shaic.claim.pcc.dto;

import java.util.Date;

public class PCCQueryDetailsTableDTO {
	
	private String queryRemarks;

	private String queryRaiseRole;

	private String queryRaiseBy;

	private Date queryRaiseDate;
	
	private Long queryKey;


	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getQueryRaiseRole() {
		return queryRaiseRole;
	}

	public void setQueryRaiseRole(String queryRaiseRole) {
		this.queryRaiseRole = queryRaiseRole;
	}

	public String getQueryRaiseBy() {
		return queryRaiseBy;
	}

	public void setQueryRaiseBy(String queryRaiseBy) {
		this.queryRaiseBy = queryRaiseBy;
	}

	public Date getQueryRaiseDate() {
		return queryRaiseDate;
	}

	public void setQueryRaiseDate(Date queryRaiseDate) {
		this.queryRaiseDate = queryRaiseDate;
	}

	public Long getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}

}
