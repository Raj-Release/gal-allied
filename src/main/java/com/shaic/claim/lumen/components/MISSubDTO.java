package com.shaic.claim.lumen.components;

import java.io.Serializable;
import java.util.Date;

import com.shaic.domain.LumenQuery;
import com.shaic.domain.LumenRequest;

@SuppressWarnings("serial")
public class MISSubDTO implements Serializable{
	
	private String queryRaisedBy;
	private Date queryRaisedDate;
	private String queryRemarks;
	private String replyRemarks;
	
	private LumenRequest lumenRequest;
	private LumenQuery lumenQuery;
	
	private Long lumenQueryDetailsKey;

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getReplyRemarks() {
		return replyRemarks;
	}

	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}

	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}

	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}

	public LumenQuery getLumenQuery() {
		return lumenQuery;
	}

	public void setLumenQuery(LumenQuery lumenQuery) {
		this.lumenQuery = lumenQuery;
	}

	public String getQueryRaisedBy() {
		return queryRaisedBy;
	}

	public void setQueryRaisedBy(String queryRaisedBy) {
		this.queryRaisedBy = queryRaisedBy;
	}

	public Date getQueryRaisedDate() {
		return queryRaisedDate;
	}

	public void setQueryRaisedDate(Date queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}

	public Long getLumenQueryDetailsKey() {
		return lumenQueryDetailsKey;
	}

	public void setLumenQueryDetailsKey(Long lumenQueryDetailsKey) {
		this.lumenQueryDetailsKey = lumenQueryDetailsKey;
	}
}
