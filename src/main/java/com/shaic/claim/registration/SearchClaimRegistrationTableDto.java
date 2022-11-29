package com.shaic.claim.registration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.SearchTableDTO;

public class SearchClaimRegistrationTableDto extends SearchTableDTO{
	
	private String intimationNumber;
	private Date intimationDate;	
	private String intimationDateValue;
	private String hospitalType;
	private String status;	
	private NewIntimationDto newIntimationDto;
	//public HumanTask humanTask;
	private String userId;
	private String password;
	private RRCDTO rrcDTO;
	private Boolean isProceedFurther = true;
	private Boolean isCancelledPolicy = false;
	
	private Double claimedAmount = 0d;
		
	/**
	 * added for PA Claim Registration
	 * 
	 */
	private String policyNo;
	private String cpuCode;
	private String insuredPatientName;
	private String hospitalName;
	private String dateOfAdmissionValue;
	private String accidentOrDeath;
	private Double paAccProvAmt;
	private String topUpPolicyAlertFlag;
    private String topUpPolicyAlertMessage;
	
	public Double getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	private Map<String, String> popupMap;
	
	private Map<String,String> suspiciousPopUp;
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public Date getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(Date intimationDate) {
		this.intimationDate = intimationDate;
		if(intimationDate != null){
		this.intimationDateValue = new SimpleDateFormat("dd/MM/yyyy").format(intimationDate);
		}else{
			this.intimationDateValue = "";
		}
			
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}
	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
		this.newIntimationDto.setPolicy(newIntimationDto.getPolicy());
		setDateOfAdmissionValue(this.newIntimationDto.getAdmissionDate());
	}
	/*public HumanTask getHumanTask() {
		return this.humanTask;
	}
	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/
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
	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}
	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}
	public String getIntimationDateValue() {
		return intimationDateValue;
	}
	public void setIntimationDateValue(String intimationDateValue) {
		this.intimationDateValue = intimationDateValue;
	}
	public Map<String, String> getPopupMap() {
		return popupMap;
	}
	public void setPopupMap(Map<String, String> popupMap) {
		this.popupMap = popupMap;
	}
	public Map<String, String> getSuspiciousPopUp() {
		return suspiciousPopUp;
	}
	public void setSuspiciousPopUp(Map<String, String> suspiciousPopUp) {
		this.suspiciousPopUp = suspiciousPopUp;
	}
	public Boolean getIsProceedFurther() {
		return isProceedFurther;
	}
	public void setIsProceedFurther(Boolean isProceedFurther) {
		this.isProceedFurther = isProceedFurther;
	}
	public Boolean getIsCancelledPolicy() {
		return isCancelledPolicy;
	}
	public void setIsCancelledPolicy(Boolean isCancelledPolicy) {
		this.isCancelledPolicy = isCancelledPolicy;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
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
	public String getDateOfAdmissionValue() {
		return dateOfAdmissionValue;
	}
	public void setDateOfAdmissionValue(Date dateOfAdmission) {
		
		if(dateOfAdmission != null){
		this.dateOfAdmissionValue = new SimpleDateFormat("dd/MM/yyyy").format(dateOfAdmission);
		}else{
			this.dateOfAdmissionValue = "";
		}		
	}
	public String getAccidentOrDeath() {
		return accidentOrDeath;
	}
	public void setAccidentOrDeath(String accidentOrDeath) {
		this.accidentOrDeath = accidentOrDeath;
	}
	public Double getPaAccProvAmt() {
		return paAccProvAmt;
	}
	public void setPaAccProvAmt(Double paAccProvAmt) {
		this.paAccProvAmt = paAccProvAmt;
	}
	public String getTopUpPolicyAlertFlag() {
		return topUpPolicyAlertFlag;
	}
	public void setTopUpPolicyAlertFlag(String topUpPolicyAlertFlag) {
		this.topUpPolicyAlertFlag = topUpPolicyAlertFlag;
	}
	public String getTopUpPolicyAlertMessage() {
		return topUpPolicyAlertMessage;
	}
	public void setTopUpPolicyAlertMessage(String topUpPolicyAlertMessage) {
		this.topUpPolicyAlertMessage = topUpPolicyAlertMessage;
	}	
	
}
