package com.shaic.claim.reimbursement.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ReimbursementDto;
import com.shaic.reimbursement.queryrejection.draftquery.search.DraftQueryLetterDetailTableDto;
/**
 * 
 * @author Lakshminarayana
 *
 */
public class ReimbursementQueryDto extends AbstractTableDTO{
	
	private Long key;

	private Long statusKey;
	
	private Long stageKey;
	
	private String claimId;
	
	private ReimbursementDto reimbursementDto;    
	
	@NotNull(message = "Please Enter Query Remarks.")
	private String queryRemarks; 
	
//	@NotNull(message = "Please Enter Query Letter Remarks.")
	private String queryLetterRemarks; 
	
	@NotNull(message = "Please Enter Redraft Remarks.")
	private String redraftRemarks; 
	
	@NotNull(message = "Please Enter Rejection Remarks.")
	private String rejectionRemarks;      
	
	private Date draftedDate;
	
	private Date reDraftDate;
	
	private Date approvedRejectionDate;
	
	private String createdBy;  
	
	private Date createdDate;  
	
	private String modifiedBy;  
	
	private Date modifiedDate;
	
	private String username;
	
	private String password;
	
	//private HumanTask humanTask;
	
	private String docFilePath;
	
	private String docType;
	
	private Integer reminderCount;
	
	private Date firstReminderDate;
	
	private Date secondReminderDate;
	
	private Date thirdReminderDate;
	
	private Date queryReplyReceivedDate;
	
	private List<DraftQueryLetterDetailTableDto> queryDarftList;
	
	private List<DraftQueryLetterDetailTableDto> redraftList;
	
	private List<DraftQueryLetterDetailTableDto> deletedList;
	
	private boolean generateDisVoucher;
	
	private String queryType; 
	
	private String dischargeVoucherFilePath;
	
	private String dischargeVoucherDocType;

	private String paApprovedAmtInwords;
	
	private Boolean isPolicyValidate;
	
	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public ReimbursementQueryDto(){
		this.deletedList = null;
		
	}
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	
	public ReimbursementDto getReimbursementDto() {
		return reimbursementDto;
	}

	public void setReimbursementDto(ReimbursementDto reimbursementDto) {
		this.reimbursementDto = reimbursementDto;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getQueryLetterRemarks() {
		return queryLetterRemarks;
	}

	public void setQueryLetterRemarks(String queryLetterRemarks) {
		this.queryLetterRemarks = queryLetterRemarks;
	}

	public String getRedraftRemarks() {
		return redraftRemarks;
	}

	public void setRedraftRemarks(String redraftRemarks) {
		this.redraftRemarks = redraftRemarks;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public Date getDraftedDate() {
		return draftedDate;
	}

	public void setDraftedDate(Date draftedDate) {
		this.draftedDate = draftedDate;
	}

	public Date getReDraftDate() {
		return reDraftDate;
	}

	public void setReDraftDate(Date reDraftDate) {
		this.reDraftDate = reDraftDate;
	}

	public Date getApprovedRejectionDate() {
		return approvedRejectionDate;
	}

	public void setApprovedRejectionDate(Date approvedRejectionDate) {
		this.approvedRejectionDate = approvedRejectionDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/

	public String getDocFilePath() {
		return docFilePath;
	}

	public void setDocFilePath(String docFilePath) {
		this.docFilePath = docFilePath;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public Integer getReminderCount() {
		return reminderCount;
	}

	public void setReminderCount(Integer reminderCount) {
		this.reminderCount = reminderCount;
	}

	public Date getFirstReminderDate() {
		return firstReminderDate;
	}

	public void setFirstReminderDate(Date firstReminderDate) {
		this.firstReminderDate = firstReminderDate;
	}

	public Date getSecondReminderDate() {
		return secondReminderDate;
	}

	public void setSecondReminderDate(Date secondReminderDate) {
		this.secondReminderDate = secondReminderDate;
	}

	public Date getThirdReminderDate() {
		return thirdReminderDate;
	}

	public void setThirdReminderDate(Date thirdReminderDate) {
		this.thirdReminderDate = thirdReminderDate;
	}

	public Date getQueryReplyReceivedDate() {
		return queryReplyReceivedDate;
	}

	public void setQueryReplyReceivedDate(Date queryReplyReceivedDate) {
		this.queryReplyReceivedDate = queryReplyReceivedDate;
	}

	public List<DraftQueryLetterDetailTableDto> getQueryDarftList() {
		return queryDarftList;
	}

	public void setQueryDarftList(
			List<DraftQueryLetterDetailTableDto> queryDarftList) {
		this.queryDarftList = queryDarftList;
	}

	public List<DraftQueryLetterDetailTableDto> getRedraftList() {
		return redraftList;
	}

	public void setRedraftList(List<DraftQueryLetterDetailTableDto> redraftList) {
		this.redraftList = redraftList;
	}

	public List<DraftQueryLetterDetailTableDto> getDeletedList() {
		return deletedList;
	}

	public void setDeletedList(List<DraftQueryLetterDetailTableDto> deletedList) {
		this.deletedList = deletedList;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public boolean getGenerateDisVoucher() {
		return generateDisVoucher;
	}

	public void setGenerateDisVoucher(boolean generateDisVoucher) {
		this.generateDisVoucher = generateDisVoucher;
	}

	public String getDischargeVoucherFilePath() {
		return dischargeVoucherFilePath;
	}

	public void setDischargeVoucherFilePath(String dischargeVoucherFilePath) {
		this.dischargeVoucherFilePath = dischargeVoucherFilePath;
	}

	public String getDischargeVoucherDocType() {
		return dischargeVoucherDocType;
	}

	public void setDischargeVoucherDocType(String dischargeVoucherDocType) {
		this.dischargeVoucherDocType = dischargeVoucherDocType;
	}

	public String getPaApprovedAmtInwords() {
		return paApprovedAmtInwords;
	}

	public void setPaApprovedAmtInwords(String paApprovedAmtInwords) {
		this.paApprovedAmtInwords = paApprovedAmtInwords;
	}

	public Boolean getIsPolicyValidate() {
		return isPolicyValidate;
	}

	public void setIsPolicyValidate(Boolean isPolicyValidate) {
		this.isPolicyValidate = isPolicyValidate;
	}
		
}
