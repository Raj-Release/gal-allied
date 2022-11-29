package com.shaic.claim.preauth.wizard.dto;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;

public class IntimationDetailDTO {

	private String intimationNo;
	private String policyNo;
	private String dateAndTime;
	private String issueOffice;
	private Long cpuCode;
	private String productName;
	private String intimationMode;
	private String proposerName;
	private String intimatedBy;
	private String state;
	private String patientName;
	private String city;
	private Boolean isPatientCovered;
	private String area;
	private String healthCardNo;
	private String hospName;
	private String name;
	private String address;
	private String relationship;
	private String admissionDate;
	private String admissionType;
	private String inpatient;
	private String hospType;
	private String lateIntimation;
	private String hospCode;
	private String admissionReason;
	private String irdaHospCode;
	private String comments;
	private String smCode;
	private String smName;
	private String brokerCode;
	private String brokerName;
	
	public IntimationDetailDTO(Intimation intimation, Hospitals hospital, ViewClaimStatusDTO claimStatusDto) {
		this.intimationNo = intimation.getIntimationId();
		this.policyNo = intimation.getPolicyNumber();
		this.dateAndTime = intimation.getCreatedDate() != null ? SHAUtils.formatDateTime(intimation.getCreatedDate()) : "";
		this.issueOffice = intimation.getPolicy() != null ? intimation.getPolicy().getHomeOfficeCode() : "" ;
		this.cpuCode = intimation.getCpuCode() != null ? intimation.getCpuCode().getCpuCode() : 0L;
		this.productName = intimation.getPolicy() != null ? (intimation.getPolicy().getProductName() != null ? intimation.getPolicy().getProductName() : "") : "" ;
		this.intimationMode = intimation.getIntimationMode() != null ? intimation.getIntimationMode().getValue() : "";
		this.proposerName = intimation.getPolicy() != null ? intimation.getPolicy().getProposerFirstName() : "" ;
		this.intimatedBy = intimation.getIntimatedBy() != null ? intimation.getIntimatedBy().getValue() : "";
		this.brokerCode = intimation.getPolicy() != null ? intimation.getPolicy().getAgentCode() : "" ;
		this.brokerName = intimation.getPolicy() != null ? intimation.getPolicy().getAgentName() : "" ;
		this.smCode = intimation.getPolicy() != null ? intimation.getPolicy().getSmCode() : "" ;
		this.smName = intimation.getPolicy() != null ? intimation.getPolicy().getSmName() : "" ;
		this.comments = intimation.getHospitalComments();
		this.admissionReason = intimation.getAdmissionReason();
		this.lateIntimation = intimation.getLateIntimationReason();
		this.admissionDate = intimation.getAdmissionDate() != null ? SHAUtils.formatDate(intimation.getAdmissionDate()) : "";
		this.admissionType = intimation.getAdmissionType() != null ? intimation.getAdmissionType().getValue() :"";
		this.isPatientCovered = intimation.getPatientNotCovered() != null ? intimation.getPatientNotCovered() : Boolean.FALSE;
		if (claimStatusDto != null) {
			this.state = claimStatusDto.getState();
			this.area = claimStatusDto.getArea();
			this.city = claimStatusDto.getCity();
			this.relationship = claimStatusDto.getRelationshipWithInsuredId();
			this.patientName = claimStatusDto.getPatientName();
			this.healthCardNo = claimStatusDto.getHealthCardNo();
			this.inpatient = claimStatusDto.getInpatientNumber();
			this.name = claimStatusDto.getPatientNotCoveredName();
		}
				
		if (hospital != null) {
			this.address = hospital.getAddress();
			this.hospCode = hospital.getHospitalCode();
			this.irdaHospCode = hospital.getHospitalIrdaCode();
			this.hospName = hospital.getName();
			this.hospType = hospital.getHospitalType() != null ? hospital.getHospitalType().getValue() : "";
			
		}
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
	public String getDateAndTime() {
		return dateAndTime;
	}
	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	public String getIssueOffice() {
		return issueOffice;
	}
	public void setIssueOffice(String issueOffice) {
		this.issueOffice = issueOffice;
	}
	public Long getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getIntimationMode() {
		return intimationMode;
	}
	public void setIntimationMode(String intimationMode) {
		this.intimationMode = intimationMode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getIntiamtionMode() {
		return intimationMode;
	}
	public void setIntiamtionMode(String intimationMode) {
		this.intimationMode = intimationMode;
	}
	public String getProposerName() {
		return proposerName;
	}
	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}
	public String getIntimatedBy() {
		return intimatedBy;
	}
	public void setIntimatedBy(String intimatedBy) {
		this.intimatedBy = intimatedBy;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}	
	public Boolean getIsPatientCovered() {
		return isPatientCovered;
	}
	public void setIsPatientCovered(Boolean isPatientCovered) {
		this.isPatientCovered = isPatientCovered;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getHealthCardNo() {
		return healthCardNo;
	}
	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}
	public String getHospName() {
		return hospName;
	}
	public void setHospName(String hospName) {
		this.hospName = hospName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}
	public String getAdmissionType() {
		return admissionType;
	}
	public void setAdmissionType(String admissionType) {
		this.admissionType = admissionType;
	}
	public String getInpatient() {
		return inpatient;
	}
	public void setInpatient(String inpatient) {
		this.inpatient = inpatient;
	}
	public String getHospType() {
		return hospType;
	}
	public void setHospType(String hospType) {
		this.hospType = hospType;
	}
	public String getLateIntimation() {
		return lateIntimation;
	}
	public void setLateIntimation(String lateIntimation) {
		this.lateIntimation = lateIntimation;
	}
	public String getHospCode() {
		return hospCode;
	}
	public void setHospCode(String hospCode) {
		this.hospCode = hospCode;
	}
	public String getAdmissionReason() {
		return admissionReason;
	}
	public void setAdmissionReason(String admissionReason) {
		this.admissionReason = admissionReason;
	}
	public String getIrdaHospCode() {
		return irdaHospCode;
	}
	public void setIrdaHospCode(String irdaHospCode) {
		this.irdaHospCode = irdaHospCode;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getSmCode() {
		return smCode;
	}
	public void setSmCode(String smCode) {
		this.smCode = smCode;
	}
	public String getSmName() {
		return smName;
	}
	public void setSmName(String smName) {
		this.smName = smName;
	}
	public String getBrokerCode() {
		return brokerCode;
	}
	public void setBrokerCode(String brokerCode) {
		this.brokerCode = brokerCode;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}	
	
}
