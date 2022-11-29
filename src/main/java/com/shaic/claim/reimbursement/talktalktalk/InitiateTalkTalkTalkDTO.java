package com.shaic.claim.reimbursement.talktalktalk;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class InitiateTalkTalkTalkDTO extends AbstractTableDTO  implements Serializable{
	
	private Long slNo;
	
	private String remarks;
	
	private SelectValue typeOfCommunication;
	
	private String processingUserName;
	
	private String talkSpokento;
	
	private Date talkSpokenDate;
	
	private String talkMobto;

	private NewIntimationDto newIntimationDTO;
	
	private ClaimDto claimDto;

	private String strUserName;

	private String strPassword;

	private PolicyDto policyDto;
	
	private String  employeeName;
	
	private String employeeID;
	
	private ImsUser userRole;
	
/*	private Long reimbursementKey;
	
	private Long claimKey;*/
	
	private Long intimationKey;
	
	private String intimationNumber;
	
	private int serialNumber;
	
	private String convoxid;
	
	private String extnCode;
	
	private Boolean isAlreadyLoggedIn = false;
	
	private String callStartTime;
	
	private String callEndTime;
	
	private String callDuration;
	
	private String referenceId;
	
	private String fileName;
	
	private String endCallRefId;
	
	private Date dialerCallStartTime;
	
	private Date dialerCallEndTime;
	
	private String action;
	
	private String endCallConvoxId;
	
	private String callHitRefNo;
	
	
	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	
	private Long key;
	
	
	public Long getSlNo() {
		return slNo;
	}

	public void setSlNo(Long slNo) {
		this.slNo = slNo;
	}

	public String getTalkSpokento() {
		return talkSpokento;
	}

	public void setTalkSpokento(String talkSpokento) {
		this.talkSpokento = talkSpokento;
	}

	public Date getTalkSpokenDate() {
		return talkSpokenDate;
	}

	public void setTalkSpokenDate(Date talkSpokenDate) {
		this.talkSpokenDate = talkSpokenDate;
	}

	public String getTalkMobto() {
		return talkMobto;
	}

	public void setTalkMobto(String talkMobto) {
		this.talkMobto = talkMobto;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public SelectValue getTypeOfCommunication() {
		return typeOfCommunication;
	}

	public void setTypeOfCommunication(SelectValue typeOfCommunication) {
		this.typeOfCommunication = typeOfCommunication;
	}

	public String getProcessingUserName() {
		return processingUserName;
	}

	public void setProcessingUserName(String processingUserName) {
		this.processingUserName = processingUserName;
	}

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public String getStrUserName() {
		return strUserName;
	}

	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public String getStrPassword() {
		return strPassword;
	}

	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}

	public PolicyDto getPolicyDto() {
		return policyDto;
	}

	public void setPolicyDto(PolicyDto policyDto) {
		this.policyDto = policyDto;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	
	public InitiateTalkTalkTalkDTO()
	{
		newIntimationDTO = new NewIntimationDto();
		claimDto = new ClaimDto();
	}

	public ImsUser getUserRole() {
		return userRole;
	}

	public void setUserRole(ImsUser userRole) {
		this.userRole = userRole;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getConvoxid() {
		return convoxid;
	}

	public void setConvoxid(String convoxid) {
		this.convoxid = convoxid;
	}

	public String getExtnCode() {
		return extnCode;
	}

	public void setExtnCode(String extnCode) {
		this.extnCode = extnCode;
	}

	public Boolean getIsAlreadyLoggedIn() {
		return isAlreadyLoggedIn;
	}

	public void setIsAlreadyLoggedIn(Boolean isAlreadyLoggedIn) {
		this.isAlreadyLoggedIn = isAlreadyLoggedIn;
	}

	public String getCallStartTime() {
		return callStartTime;
	}

	public void setCallStartTime(String callStartTime) {
		this.callStartTime = callStartTime;
	}

	public String getCallEndTime() {
		return callEndTime;
	}

	public void setCallEndTime(String callEndTime) {
		this.callEndTime = callEndTime;
	}

	public String getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getEndCallRefId() {
		return endCallRefId;
	}

	public void setEndCallRefId(String endCallRefId) {
		this.endCallRefId = endCallRefId;
	}

	public Date getDialerCallStartTime() {
		return dialerCallStartTime;
	}

	public void setDialerCallStartTime(Date dialerCallStartTime) {
		this.dialerCallStartTime = dialerCallStartTime;
	}

	public Date getDialerCallEndTime() {
		return dialerCallEndTime;
	}

	public void setDialerCallEndTime(Date dialerCallEndTime) {
		this.dialerCallEndTime = dialerCallEndTime;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEndCallConvoxId() {
		return endCallConvoxId;
	}

	public void setEndCallConvoxId(String endCallConvoxId) {
		this.endCallConvoxId = endCallConvoxId;
	}

	public String getCallHitRefNo() {
		return callHitRefNo;
	}

	public void setCallHitRefNo(String callHitRefNo) {
		this.callHitRefNo = callHitRefNo;
	}
	

/*	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}*/

	
	
}
