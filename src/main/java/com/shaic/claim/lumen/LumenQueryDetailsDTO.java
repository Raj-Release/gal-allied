package com.shaic.claim.lumen;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.LumenQuery;
import com.shaic.domain.LumenRequest;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

@SuppressWarnings("serial")
public class LumenQueryDetailsDTO extends AbstractTableDTO{
	//UI Table SerialNumber will display in QueryInitiatedToMIS Screen Wizard Page...
	private int sno;

	private LumenRequest lumenRequest;
	private LumenQuery lumenQuery;
	private String queryRemarks;
	private String replyRemarks;
	private String repliedBy;
	private Date repliedDate;
	private Status status;
	private Stage stage;
	
	private Long queryDetailsKey;
	private String replyRaisedRole;
	
	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}
	public LumenQuery getLumenQuery() {
		return lumenQuery;
	}
	public String getQueryRemarks() {
		return queryRemarks;
	}
	public String getReplyRemarks() {
		return replyRemarks;
	}
	public String getRepliedBy() {
		return repliedBy;
	}
	public Date getRepliedDate() {
		return repliedDate;
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
	public void setLumenQuery(LumenQuery lumenQuery) {
		this.lumenQuery = lumenQuery;
	}
	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}
	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}
	public void setRepliedBy(String repliedBy) {
		this.repliedBy = repliedBy;
	}
	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public String getReplyRaisedRole() {
		return replyRaisedRole;
	}
	public void setReplyRaisedRole(String replyRaisedRole) {
		this.replyRaisedRole = replyRaisedRole;
	}
	public Long getQueryDetailsKey() {
		return queryDetailsKey;
	}
	public void setQueryDetailsKey(Long queryDetailsKey) {
		this.queryDetailsKey = queryDetailsKey;
	}
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
}
