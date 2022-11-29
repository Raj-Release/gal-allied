package com.shaic.claim.registration.convertClaim.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;

public class SearchConvertClaimTableDto extends AbstractTableDTO implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer srlNo;
	
	private String lob;

	private String claimType;

	private String cpuCode;

	private String claimNumber;

	private String insuredPatientName;

	private String hospitalName;

	private String dateOfAdmission;
	
	private Date dateOfAdmission1;

	private String claimStatus;
	
	private Long hospitalTypeId;
	
	private RRCDTO rrcDTO;
	
	private Long acknowledgementKey;
	
	private Long intimationKey;
	
	private Long rodKey;
	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	/**
	 * Following attributes added as part of PA Claim Conversion
	 * 
	 */
	private PreauthDTO preAuthDto = new PreauthDTO();
	
	private Long intimationkey;
	
	private String intimationNo;
	
	private String policyNo;
	
	private String hospitalType;
	
	private String accDeath;
	
	private String crmFlagged;
	
	private String crcFlaggedReason;
    
    private String crcFlaggedRemark;
    
    private String IsPreferredHospital;
    
    private Double InsuredAge;
	
	
	public PreauthDTO getPreAuthDto() {
		return preAuthDto;
	}

	public void setPreAuthDto(PreauthDTO preAuthDto) {
		this.preAuthDto = preAuthDto;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public String getCrcFlaggedReason() {
		return crcFlaggedReason;
	}

	public void setCrcFlaggedReason(String crcFlaggedReason) {
		this.crcFlaggedReason = crcFlaggedReason;
	}

	public String getCrcFlaggedRemark() {
		return crcFlaggedRemark;
	}

	public void setCrcFlaggedRemark(String crcFlaggedRemark) {
		this.crcFlaggedRemark = crcFlaggedRemark;
	}

	public String getIsPreferredHospital() {
		return IsPreferredHospital;
	}

	public void setIsPreferredHospital(String isPreferredHospital) {
		IsPreferredHospital = isPreferredHospital;
	}

	public Double getInsuredAge() {
		return InsuredAge;
	}

	public void setInsuredAge(Double insuredAge) {
		InsuredAge = insuredAge;
	}

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

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
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

	public String getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public Date getDateOfAdmission1() {
		
		return dateOfAdmission1;
		
	}

	public void setDateOfAdmission1(Date dateOfAdmission1) {
		String dateformate =new SimpleDateFormat("dd/MM/yyyy").format(dateOfAdmission1);
		setDateOfAdmission(dateformate);
		this.dateOfAdmission1 = dateOfAdmission1;
		
	}
	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
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

	public Long getAcknowledgementKey() {
		return acknowledgementKey;
	}

	public void setAcknowledgementKey(Long acknowledgementKey) {
		this.acknowledgementKey = acknowledgementKey;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getAccDeath() {
		return accDeath;
	}

	public void setAccDeath(String accDeath) {
		this.accDeath = accDeath;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Long getIntimationkey() {
		return intimationkey;
	}

	public void setIntimationkey(Long intimationkey) {
		this.intimationkey = intimationkey;
	}

}
