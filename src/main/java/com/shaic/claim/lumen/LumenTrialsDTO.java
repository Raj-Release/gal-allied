package com.shaic.claim.lumen;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.LumenRequest;

@SuppressWarnings("serial")
public class LumenTrialsDTO extends AbstractTableDTO{

	private LumenRequest lumenRequest;
	private Intimation intimation;
	private String trialstatus;
	private String statusRemarks;
	
	//For Lumen trials table view....
	private String initiatedScreen;
	private String initiatedBy;
	private Date initiatedDate;
	private String response;
	private String comments;
	
	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}
	public Intimation getIntimation() {
		return intimation;
	}
	public String getTrialstatus() {
		return trialstatus;
	}
	public String getStatusRemarks() {
		return statusRemarks;
	}
	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}
	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}
	public void setTrialstatus(String trialstatus) {
		this.trialstatus = trialstatus;
	}
	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}
	public String getInitiatedScreen() {
		return initiatedScreen;
	}
	public void setInitiatedScreen(String initiatedScreen) {
		this.initiatedScreen = initiatedScreen;
	}
	public String getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}
	public Date getInitiatedDate() {
		return initiatedDate;
	}
	public void setInitiatedDate(Date initiatedDate) {
		this.initiatedDate = initiatedDate;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
