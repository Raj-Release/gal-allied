package com.shaic.claim.reimbursement.viewPaymentAudit;

import com.shaic.arch.fields.dto.AbstractSearchDTO;

public class ViewPaymentAuditDTO extends AbstractSearchDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1525478209507569274L;

	private String stage;
	
	private String auditDate;

	public ViewPaymentAuditDTO(String stage, String auditDate) {
		super();
		this.stage = stage;
		this.auditDate = auditDate;
	}
	
	public ViewPaymentAuditDTO() {
		super();
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
}
