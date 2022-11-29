package com.shaic.claim.premedical.dto;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;

public class DiagnosisTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2227580013726645310L;

	private Long key;
	
	private Long preauthKey;
	
	private SelectValue diagnosis;
	
	private SelectValue icdChapter;
	
	private SelectValue icdBlock;
	
	private SelectValue icdCode;
	
	private SelectValue sublimitApplicable;
	
	private SelectValue sublimitName;
	
	private String sublimitApplicableFlag;
	
//	private String sublimtDesc;
	
	private Double sublimitAmt;
	
	private SelectValue considerForPayment;
	
	private SelectValue sumInsuredRestriction;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public SelectValue getSublimitApplicable() {
		return sublimitApplicable;
	}

	public void setSublimitApplicable(SelectValue sublimitApplicable) {
		this.sublimitApplicable = sublimitApplicable;
		this.sublimitApplicableFlag = this.sublimitApplicable != null && this.sublimitApplicable.getValue().toLowerCase().contains("yes") ? "Y" : "N";
	}

	public SelectValue getSublimitName() {
		return sublimitName;
	}

	public void setSublimitName(SelectValue sublimitName) {
		this.sublimitName = sublimitName;
	}

	public Double getSublimitAmt() {
		return sublimitAmt;
	}

	public void setSublimitAmt(Double sublimitAmt) {
		this.sublimitAmt = sublimitAmt;
	}

	public SelectValue getConsiderForPayment() {
		return considerForPayment;
	}

	public void setConsiderForPayment(SelectValue considerForPayment) {
		this.considerForPayment = considerForPayment;
	}

	public SelectValue getSumInsuredRestriction() {
		return sumInsuredRestriction;
	}

	public void setSumInsuredRestriction(SelectValue sumInsuredRestriction) {
		this.sumInsuredRestriction = sumInsuredRestriction;
	}

	public SelectValue getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(SelectValue diagnosis) {
		this.diagnosis = diagnosis;
	}

	public SelectValue getIcdChapter() {
		return icdChapter;
	}

	public void setIcdChapter(SelectValue icdChapter) {
		this.icdChapter = icdChapter;
	}

	public SelectValue getIcdBlock() {
		return icdBlock;
	}

	public void setIcdBlock(SelectValue icdBlock) {
		this.icdBlock = icdBlock;
	}

	public SelectValue getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(SelectValue icdCode) {
		this.icdCode = icdCode;
	}

	public String getSublimitApplicableFlag() {
		return sublimitApplicableFlag;
	}

	public void setSublimitApplicableFlag(String sublimitApplicableFlag) {
		this.sublimitApplicableFlag = sublimitApplicableFlag;
	}
}
