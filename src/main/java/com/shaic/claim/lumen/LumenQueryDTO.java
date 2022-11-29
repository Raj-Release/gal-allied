package com.shaic.claim.lumen;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.LumenRequest;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

@SuppressWarnings("serial")
public class LumenQueryDTO extends AbstractTableDTO{

	private LumenRequest lumenRequest;
	private String queryType;
	private String queryRaisedBy;
	private String query;
	private Status status;
	private Stage stage;
	
	private Long queryKey;
	private Date queryRaisedDate;
	private String queryRaisedRole;
	
	private Date repliedDate;
	
	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}
	public String getQueryType() {
		return queryType;
	}
	public String getQueryRaisedBy() {
		return queryRaisedBy;
	}
	public String getQuery() {
		return query;
	}
	public Status getStatus() {
		return status;
	}
	public Stage getStage() {
		return stage;
	}
	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public void setQueryRaisedBy(String queryRaisedBy) {
		this.queryRaisedBy = queryRaisedBy;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public String getQueryRaisedRole() {
		return queryRaisedRole;
	}
	public void setQueryRaisedRole(String queryRaisedRole) {
		this.queryRaisedRole = queryRaisedRole;
	}
	public Long getQueryKey() {
		return queryKey;
	}
	public void setQueryKey(Long queryKey) {
		this.queryKey = queryKey;
	}
	public Date getQueryRaisedDate() {
		return queryRaisedDate;
	}
	public void setQueryRaisedDate(Date queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}
	public Date getRepliedDate() {
		return repliedDate;
	}
	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}
}
