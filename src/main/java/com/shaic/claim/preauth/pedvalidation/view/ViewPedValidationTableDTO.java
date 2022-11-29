package com.shaic.claim.preauth.pedvalidation.view;

import java.io.Serializable;

public class ViewPedValidationTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String diagnosis;
	private String pedName;
	private String policyAgeing;
	private String consideredForExclusion;
	private String impactOnDiagnosis;
	private String remarks;
	private String icdChapterValue;
	private String icdBlockValue;
	private String icdCodeValue;

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getPedName() {
		return pedName;
	}

	public void setPedName(String pedName) {
		this.pedName = pedName;
	}

	public String getPolicyAgeing() {
		return policyAgeing;
	}

	public void setPolicyAgeing(String policyAgeing) {
		this.policyAgeing = policyAgeing;
	}

	public String getConsideredForExclusion() {
		return consideredForExclusion;
	}

	public void setConsideredForExclusion(String consideredForExclusion) {
		this.consideredForExclusion = consideredForExclusion;
	}

	public String getImpactOnDiagnosis() {
		return impactOnDiagnosis;
	}

	public void setImpactOnDiagnosis(String impactOnDiagnosis) {
		this.impactOnDiagnosis = impactOnDiagnosis;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getIcdChapterValue() {
		return icdChapterValue;
	}

	public String getIcdBlockValue() {
		return icdBlockValue;
	}

	public String getIcdCodeValue() {
		return icdCodeValue;
	}

	public void setIcdChapterValue(String icdChapterValue) {
		this.icdChapterValue = icdChapterValue;
	}

	public void setIcdBlockValue(String icdBlockValue) {
		this.icdBlockValue = icdBlockValue;
	}

	public void setIcdCodeValue(String icdCodeValue) {
		this.icdCodeValue = icdCodeValue;
	}
	
}
