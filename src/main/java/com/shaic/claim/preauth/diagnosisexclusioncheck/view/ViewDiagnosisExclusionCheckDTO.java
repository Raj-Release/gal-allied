package com.shaic.claim.preauth.diagnosisexclusioncheck.view;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewDiagnosisExclusionCheckDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String diagnosis;
	private String policyAgeing;
	private String impactOnDiagnosis;
	private String subLimit;
	private Long approvedAmt;

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getPolicyAgeing() {
		return policyAgeing;
	}

	public void setPolicyAgeing(String policyAgeing) {
		this.policyAgeing = policyAgeing;
	}

	public String getImpactOnDiagnosis() {
		return impactOnDiagnosis;
	}

	public void setImpactOnDiagnosis(String impactOnDiagnosis) {
		this.impactOnDiagnosis = impactOnDiagnosis;
	}

	public String getSubLimit() {
		return subLimit;
	}

	public void setSubLimit(String subLimit) {
		this.subLimit = subLimit;
	}

	public Long getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(Long approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

}
