package com.shaic.claim.processtranslation.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;

public class SearchProcessTranslationTableDTO extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = -3499453965389974433L;

	private Integer sno;
	
	private Long decisionKey;

	private String intimationNo;

	private String claimNo;

	private String policyNo;

	private String insuredPatientName;

	private String lob;

	private String productCode;

	private String productName;
	
	private Long cpuCode;	
	private String cpuCodeName;
	
	private Long rodKey;
	
	private Long LOBID;
	
	private Long stageId;
	
	private RRCDTO rrcDTO;
	
	private String diagnosis;

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getDecisionKey() {
		return decisionKey;
	}

	public void setDecisionKey(Long decisionKey) {
		this.decisionKey = decisionKey;
	}

	
	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Long getLOBID() {
		return LOBID;
	}

	public void setLOBID(Long lOBID) {
		LOBID = lOBID;
	}

	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}

	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getCpuCodeName() {
		return cpuCode+"-"+cpuCodeName;
	}

	public void setCpuCodeName(String cpuCodeName) {
		this.cpuCodeName = cpuCodeName;
	}
	
}
