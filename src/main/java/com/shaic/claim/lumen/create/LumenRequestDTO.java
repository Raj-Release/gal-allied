package com.shaic.claim.lumen.create;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.lumen.LumenQueryDetailsDTO;
import com.shaic.claim.lumen.components.LetterDetailsDTO;
import com.shaic.claim.lumen.components.MISQueryReplyDTO;
import com.shaic.claim.lumen.querytomis.DocumentAckTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

@SuppressWarnings("serial")
public class LumenRequestDTO extends AbstractTableDTO{ 
	
	private Long requestIntiatedId;
	private String loginId;
	
	private String initiatedScreen;
	private String initiatedBy;
	private Date initiatedDate;
	private String response;
	private String comments;
	
	private Intimation intimation;

	private Policy policy;
	private Claim claim;
	private Status status;
	private Stage stage;
	private String caseReferenceNumber;
	
	// Search Lumen Request table properties	
	private String intimationNumber;
	private String cpuCode;
	private String cpuDesc;
	private String policyNumber;
	private String productName;
	private String insuredPatientName;
	private String claimType;
	private String lumenStatus;
	
	//Common carousel properties
	private String hospitalName;
	private String policyIssuingOffice;
	private String empName;
	
	private Long lumenTypeId;
	private Long errorTypeId;
	private String remarks;	
	private String level1ApprovalRemarks;
	private String level1RejectRemarks;
	private String level1ReplyRemarks;
	private String level1CloseRemarks;
	private String coordApprovalRemarks;
	private String coordReplyRemarks;
	private String generateLetter;
	private String level2ApprovalRemarks;
	private String level2RejectRemarks;
	private String level2ReplyRemarks;
	private String level2CloseRemarks;
	private Long lumenRequestKey;
	
	//lumen functional properties
	private List<String> listOfParticipants;
	private List<String> listOfLapse;
	private String userActionName;
	private String level1QueryRemarks;
	// The level1QueryTo, level1ReplyTo, level1ApproveTo will be common used in lumen process screen to hold To drop-down value. This is going to persist in Lumen Request table for dummy purpose.
	private String level1QueryTo;
	private String level1ReplyTo;
	private String level1ApproveTo;
	
	private List<Long> lumenQueryKey = new ArrayList<Long>();
	private List<String> misQryList= new ArrayList<String>();
	
	//getTask procedure Properties...
	private Long workFlowKey;
	private String outcome;
	private Long totalRecords;
	
	private String misQueryRaisedFrom;
	
	//Approval Remarks EmpName
	private String remarksEmpName;
	
	private DocumentDetails UserUploadedDocument;
	private List<DocumentDetails> listOfUserUploadedDocuments = new ArrayList<DocumentDetails>();
	//mis uploaded document table dto list.
	private List<DocumentAckTableDTO> listOfAckTableDTO = new ArrayList<DocumentAckTableDTO>();
	
	private String queryTableReplyRemarks;
	
	private List<LumenQueryDetailsDTO> updatedMISReplyRemarksList;
	
	private MISQueryReplyDTO misQueryObj;
	
	//generate Letter properties
	
	private Boolean generateLetterFlag;
	
	private List<LetterDetailsDTO> listOfApprovalLetters;
	
	private String patientStatus;
	
	public String getInitiatedScreen() {
		return initiatedScreen;
	}
	public void setInitiatedScreen(String initiatedScreen) {
		this.initiatedScreen = initiatedScreen;
	}
	public String getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}
	public Date getInitiatedDate() {
		return initiatedDate;
	}
	public void setInitiatedDate(Date initiatedDate) {
		this.initiatedDate = initiatedDate;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getRequestIntiatedId() {
		return requestIntiatedId;
	}
	public void setRequestIntiatedId(Long requestIntiatedId) {
		this.requestIntiatedId = requestIntiatedId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	public Claim getClaim() {
		return claim;
	}
	public void setClaim(Claim claim) {
		this.claim = claim;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getCpuDesc() {
		if(StringUtils.isNotBlank(cpuCode)){
			return cpuCode;
		}else{
			if(StringUtils.isNotBlank(cpuDesc)){
				return cpuDesc;
			}else{
				return "";
			}
		}
	}
	public void setCpuDesc(String cpuDesc) {
		this.cpuDesc = cpuDesc;
	}
	public Intimation getIntimation() {
		return intimation;
	}
	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public void setLumenStatus(String lumenStatus) {
		this.lumenStatus = lumenStatus;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getLumenStatus() {
		return lumenStatus;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getPolicyIssuingOffice() {
		return policyIssuingOffice;
	}
	public void setPolicyIssuingOffice(String policyIssuingOffice) {
		this.policyIssuingOffice = policyIssuingOffice;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getCaseReferenceNumber() {
		return caseReferenceNumber;
	}
	public void setCaseReferenceNumber(String caseReferenceNumber) {
		this.caseReferenceNumber = caseReferenceNumber;
	}
	public String getRemarks() {
		return remarks;
	}
	public String getLevel1ApprovalRemarks() {
		return level1ApprovalRemarks;
	}
	public String getLevel1RejectRemarks() {
		return level1RejectRemarks;
	}
	public String getLevel1ReplyRemarks() {
		return level1ReplyRemarks;
	}
	public String getLevel1CloseRemarks() {
		return level1CloseRemarks;
	}
	public String getCoordApprovalRemarks() {
		return coordApprovalRemarks;
	}
	public String getCoordReplyRemarks() {
		return coordReplyRemarks;
	}
	public String getGenerateLetter() {
		return generateLetter;
	}
	public String getLevel2ApprovalRemarks() {
		return level2ApprovalRemarks;
	}
	public String getLevel2RejectRemarks() {
		return level2RejectRemarks;
	}
	public String getLevel2ReplyRemarks() {
		return level2ReplyRemarks;
	}
	public String getLevel2CloseRemarks() {
		return level2CloseRemarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public void setLevel1ApprovalRemarks(String level1ApprovalRemarks) {
		this.level1ApprovalRemarks = level1ApprovalRemarks;
	}
	public void setLevel1RejectRemarks(String level1RejectRemarks) {
		this.level1RejectRemarks = level1RejectRemarks;
	}
	public void setLevel1ReplyRemarks(String level1ReplyRemarks) {
		this.level1ReplyRemarks = level1ReplyRemarks;
	}
	public void setLevel1CloseRemarks(String level1CloseRemarks) {
		this.level1CloseRemarks = level1CloseRemarks;
	}
	public void setCoordApprovalRemarks(String coordApprovalRemarks) {
		this.coordApprovalRemarks = coordApprovalRemarks;
	}
	public void setCoordReplyRemarks(String coordReplyRemarks) {
		this.coordReplyRemarks = coordReplyRemarks;
	}
	public void setGenerateLetter(String generateLetter) {
		this.generateLetter = generateLetter;
	}
	public void setLevel2ApprovalRemarks(String level2ApprovalRemarks) {
		this.level2ApprovalRemarks = level2ApprovalRemarks;
	}
	public void setLevel2RejectRemarks(String level2RejectRemarks) {
		this.level2RejectRemarks = level2RejectRemarks;
	}
	public void setLevel2ReplyRemarks(String level2ReplyRemarks) {
		this.level2ReplyRemarks = level2ReplyRemarks;
	}
	public void setLevel2CloseRemarks(String level2CloseRemarks) {
		this.level2CloseRemarks = level2CloseRemarks;
	}
	public Long getLumenRequestKey() {
		return lumenRequestKey;
	}
	public void setLumenRequestKey(Long lumenRequestKey) {
		this.lumenRequestKey = lumenRequestKey;
	}
	public Long getLumenTypeId() {
		return lumenTypeId;
	}
	public Long getErrorTypeId() {
		return errorTypeId;
	}
	public void setLumenTypeId(Long lumenTypeId) {
		this.lumenTypeId = lumenTypeId;
	}
	public void setErrorTypeId(Long errorTypeId) {
		this.errorTypeId = errorTypeId;
	}
	public List<String> getListOfParticipants() {
		return listOfParticipants;
	}
	public void setListOfParticipants(List<String> listOfParticipants) {
		this.listOfParticipants = listOfParticipants;
	}
	public List<String> getListOfLapse() {
		return listOfLapse;
	}
	public void setListOfLapse(List<String> listOfLapse) {
		this.listOfLapse = listOfLapse;
	}
	public String getUserActionName() {
		return userActionName;
	}
	public void setUserActionName(String userActionName) {
		this.userActionName = userActionName;
	}
	public String getLevel1QueryRemarks() {
		return level1QueryRemarks;
	}
	public void setLevel1QueryRemarks(String level1QueryRemarks) {
		this.level1QueryRemarks = level1QueryRemarks;
	}
	public String getLevel1QueryTo() {
		return level1QueryTo;
	}
	public void setLevel1QueryTo(String level1QueryTo) {
		this.level1QueryTo = level1QueryTo;
	}
	public String getLevel1ReplyTo() {
		return level1ReplyTo;
	}
	public void setLevel1ReplyTo(String level1ReplyTo) {
		this.level1ReplyTo = level1ReplyTo;
	}
	public List<Long> getLumenQueryKey() {
		return lumenQueryKey;
	}
	public void setLumenQueryKey(List<Long> lumenQueryKey) {
		this.lumenQueryKey = lumenQueryKey;
	}
	public String getLevel1ApproveTo() {
		return level1ApproveTo;
	}
	public void setLevel1ApproveTo(String level1ApproveTo) {
		this.level1ApproveTo = level1ApproveTo;
	}
	public List<String> getMisQryList() {
		return misQryList;
	}
	public void setMisQryList(List<String> misQryList) {
		this.misQryList = misQryList;
	}
	public List<DocumentDetails> getListOfUserUploadedDocuments() {
		return listOfUserUploadedDocuments;
	}
	public void setListOfUserUploadedDocuments(
			List<DocumentDetails> listOfUserUploadedDocuments) {
		this.listOfUserUploadedDocuments = listOfUserUploadedDocuments;
	}
	public DocumentDetails getUserUploadedDocument() {
		return UserUploadedDocument;
	}
	public void setUserUploadedDocument(DocumentDetails userUploadedDocument) {
		UserUploadedDocument = userUploadedDocument;
	}
	public Long getWorkFlowKey() {
		return workFlowKey;
	}
	public void setWorkFlowKey(Long workFlowKey) {
		this.workFlowKey = workFlowKey;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public String getRemarksEmpName() {
		return remarksEmpName;
	}
	public void setRemarksEmpName(String remarksEmpName) {
		this.remarksEmpName = remarksEmpName;
	}
	public String getMisQueryRaisedFrom() {
		return misQueryRaisedFrom;
	}
	public void setMisQueryRaisedFrom(String misQueryRaisedFrom) {
		this.misQueryRaisedFrom = misQueryRaisedFrom;
	}
	public List<DocumentAckTableDTO> getListOfAckTableDTO() {
		return listOfAckTableDTO;
	}
	public void setListOfAckTableDTO(List<DocumentAckTableDTO> listOfAckTableDTO) {
		this.listOfAckTableDTO = listOfAckTableDTO;
	}
	public String getQueryTableReplyRemarks() {
		return queryTableReplyRemarks;
	}
	public void setQueryTableReplyRemarks(String queryTableReplyRemarks) {
		this.queryTableReplyRemarks = queryTableReplyRemarks;
	}
	public List<LumenQueryDetailsDTO> getUpdatedMISReplyRemarksList() {
		return updatedMISReplyRemarksList;
	}
	public void setUpdatedMISReplyRemarksList(
			List<LumenQueryDetailsDTO> updatedMISReplyRemarksList) {
		this.updatedMISReplyRemarksList = updatedMISReplyRemarksList;
	}
	public MISQueryReplyDTO getMisQueryObj() {
		return misQueryObj;
	}
	public void setMisQueryObj(MISQueryReplyDTO misQueryObj) {
		this.misQueryObj = misQueryObj;
	}
	public Boolean getGenerateLetterFlag() {
		return generateLetterFlag;
	}
	public void setGenerateLetterFlag(Boolean generateLetterFlag) {
		this.generateLetterFlag = generateLetterFlag;
	}
	public List<LetterDetailsDTO> getListOfApprovalLetters() {
		return listOfApprovalLetters;
	}
	public void setListOfApprovalLetters(List<LetterDetailsDTO> listOfApprovalLetters) {
		this.listOfApprovalLetters = listOfApprovalLetters;
	}
	public Long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}
	public String getPatientStatus() {
		return patientStatus;
	}
	public void setPatientStatus(String patientStatus) {
		this.patientStatus = patientStatus;
	}	
}
