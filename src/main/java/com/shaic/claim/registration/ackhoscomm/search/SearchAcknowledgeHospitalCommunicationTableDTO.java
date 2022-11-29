package com.shaic.claim.registration.ackhoscomm.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;

public class SearchAcknowledgeHospitalCommunicationTableDTO extends AbstractTableDTO implements Serializable {
	

	private static final long serialVersionUID = -1034081659170958078L;

	private Integer srlNo;
	
	private String lob;
	
	private String claimType;
	
	private String cpuCode;
	
	private String claimNo;
	
	private String insuredPatientName;
	
	private String hospitalName;
	
	private Date dateofAdmission;
	
	private String claimStatus;
	
	private String remarks;
	
	private Long hospitalTypeId;
	
	private RRCDTO rrcDTO;

	
	public Integer getSrlNo() {
		return srlNo;
	}

	public void setSrlNo(Integer srlNo) {
		this.srlNo = srlNo;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Date getDateofAdmission() {
		return dateofAdmission;
	}

	public void setDateofAdmission(Date dateofAdmission) {
		this.dateofAdmission = dateofAdmission;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}

	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}


	
	
	
	
	

}
