package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

public class InvestigationCompletionDetailsDTO {
	
	private Date dateOfCompletion;
	
	private SelectValue comfirmedBy;
	
	private String investigationCompletionRemarks;	
	
	private String userName;
	
	private String passWord;
	
	//private HumanTask humanTask;
	
	private Long rodKey;

	public Date getDateOfCompletion() {
		return dateOfCompletion;
	}

	public void setDateOfCompletion(Date dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}

	public SelectValue getComfirmedBy() {
		return comfirmedBy;
	}

	public void setComfirmedBy(SelectValue comfirmedBy) {
		this.comfirmedBy = comfirmedBy;
	}

	public String getInvestigationCompletionRemarks() {
		return investigationCompletionRemarks;
	}

	public void setInvestigationCompletionRemarks(
			String investigationCompletionRemarks) {
		this.investigationCompletionRemarks = investigationCompletionRemarks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
/*
	public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	
	

}
