package com.shaic.claim.registration;

import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;



public class GenerateCoveringLetterSearchTableDto extends AbstractTableDTO {
	private String lob;
	private String claimType;
	private String cpuCode;
	private String claimNumber;
	private String insuredPatientName;
	private String hospitalName;
	private String admissionDate;
	//private HumanTask humanTask;
	private ClaimDto claimDto;
	private String userId;
	private String password;
	
	// Added for legal process
	private Boolean isLegal;
	
	
	//Added for PA Claim
	private String intimationNo;
	
	private String policyNo;
	
	private String hospitalType;
	
	private String accedentDeath;
	
	private String claimStatus;	
	
	private List<DocumentCheckListDTO> paDocChecklist;
	
	private List<SelectValue> paDocContainerList;
	
	private String crmFlagged;
	
	private NewIntimationDto newIntimationDto;
	
	private PreauthDTO preauthDTO;
	
	private String crcFlaggedReason;
	
	private String crcFlaggedRemark;
	
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
	public String getAdmissionDate() {
		return admissionDate;
	}
	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}
	/*public HumanTask getHumanTask() {
		return humanTask;
	}
	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getIsLegal() {
		return isLegal;
	}
	public void setIsLegal(Boolean isLegal) {
		this.isLegal = isLegal;
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
	public String getAccedentDeath() {
		return accedentDeath;
	}
	public void setAccedentDeath(String accedentDeath) {
		this.accedentDeath = accedentDeath;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public List<DocumentCheckListDTO> getPaDocChecklist() {
		return paDocChecklist;
	}
	public void setPaDocChecklist(List<DocumentCheckListDTO> paDocChecklist) {
		this.paDocChecklist = paDocChecklist;
	}
	public List<SelectValue> getPaDocContainerList() {
		return paDocContainerList;
	}
	public void setPaDocContainerList(List<SelectValue> paDocContainerList) {
		this.paDocContainerList = paDocContainerList;
	}
	public String getCrmFlagged() {
		return crmFlagged;
	}
	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}
	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}
	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}
	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}
	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
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

}
