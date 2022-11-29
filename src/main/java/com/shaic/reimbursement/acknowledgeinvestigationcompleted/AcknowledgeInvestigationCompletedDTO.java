package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

public class AcknowledgeInvestigationCompletedDTO {
	
	private String dateOfCompletion;
	
	private String confirmedBy;
	
	private String investigationCompletionRemarks;

	public String getDateOfCompletion() {
		return dateOfCompletion;
	}

	public void setDateOfCompletion(String dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}

	public String getConfirmedBy() {
		return confirmedBy;
	}

	public void setConfirmedBy(String confirmedBy) {
		this.confirmedBy = confirmedBy;
	}

	public String getInvestigationCompletionRemarks() {
		return investigationCompletionRemarks;
	}

	public void setInvestigationCompletionRemarks(
			String investigationCompletionRemarks) {
		this.investigationCompletionRemarks = investigationCompletionRemarks;
	}
	
	

}
