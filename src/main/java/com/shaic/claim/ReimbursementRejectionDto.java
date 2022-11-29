package com.shaic.claim;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

/**
 * 
 * @author Lakshminarayana
 *
 */

public class ReimbursementRejectionDto extends AbstractTableDTO{

	private Long key;
	
	private ReimbursementDto reimbursementDto;
	
	private String letterDraftedUser;
	
	@NotNull(message = "Please Enter Rejection Remarks.")
	private String rejectionRemarks;
	
	@NotNull(message = "Please Enter Rejection Letter Remarks.")
	private String rejectionLetterRemarks;
	
	private String rejectionLetterRemarks2;
	
	private String rejectionRemarks2;
	
	private Date rejectionLetterDate;
	
	@NotNull(message = "Please Enter Disapprove Remarks.")
	private String disapprovedRemarks;
	
	private Date disapprovedDate;  
	
	@NotNull(message = "Please Enter Redraft Remarks.")
	private String redraftRemarks;
	
	@NotNull(message = "Please Select a value for Rejection Category Field.")
	private SelectValue rejCategSelectValue;

	private SelectValue rejSubCategSelectValue;
	
	private Date redraftDate;
	   
	private Date approvedRejectionDate;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String modifiedBy;
	
	private Date modifiedDate;
	
	private String username;
	
	private String password;
	
//	private HumanTask humanTask;
	
	private String docFilePath;
	private String docType;
	
	private String statusValue;

	private Long statusKey;
	
	boolean isHospitalizationRodSettled;
	
	private String allowReconsider;
	
	
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

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public String getRejectionLetterRemarks() {
		return rejectionLetterRemarks;
	}

	public void setRejectionLetterRemarks(String rejectionLetterRemarks) {
		this.rejectionLetterRemarks = rejectionLetterRemarks;
	}

	public Date getRejectionLetterDate() {
		return rejectionLetterDate;
	}

	public void setRejectionLetterDate(Date rejectionLetterDate) {
		this.rejectionLetterDate = rejectionLetterDate;
	}

	public String getDisapprovedRemarks() {
		return disapprovedRemarks;
	}

	public void setDisapprovedRemarks(String disapprovedRemarks) {
		this.disapprovedRemarks = disapprovedRemarks;
	}

	public Date getDisapprovedDate() {
		return disapprovedDate;
	}

	public void setDisapprovedDate(Date disapprovedDate) {
		this.disapprovedDate = disapprovedDate;
	}

	public String getRedraftRemarks() {
		return redraftRemarks;
	}

	public void setRedraftRemarks(String redraftRemarks) {
		this.redraftRemarks = redraftRemarks;
	}

	public Date getRedraftDate() {
		return redraftDate;
	}

	public void setRedraftDate(Date redraftDate) {
		this.redraftDate = redraftDate;
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
	}
*/
	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocFilePath() {
		return docFilePath;
	}

	public void setDocFilePath(String docFilePath) {
		this.docFilePath = docFilePath;
	}

	public String getLetterDraftedUser() {
		return letterDraftedUser;
	}

	public void setLetterDraftedUser(String letterDraftedUser) {
		this.letterDraftedUser = letterDraftedUser;

	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public SelectValue getRejCategSelectValue() {
		return rejCategSelectValue;
	}

	public void setRejCategSelectValue(SelectValue rejCategSelectValue) {
		this.rejCategSelectValue = rejCategSelectValue;
	}		


	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}
	
	public boolean isHospitalizationRodSettled() {
		return isHospitalizationRodSettled;
	}

	public void setHospitalizationRodSettled(boolean isHospitalizationRodSettled) {
		this.isHospitalizationRodSettled = isHospitalizationRodSettled;
	}

	public String getAllowReconsider() {
		return allowReconsider;
	}

	public void setAllowReconsider(String allowReconsider) {
		this.allowReconsider = allowReconsider;
	}

	public SelectValue getRejSubCategSelectValue() {
		return rejSubCategSelectValue;
	}

	public void setRejSubCategSelectValue(SelectValue rejSubCategSelectValue) {
		this.rejSubCategSelectValue = rejSubCategSelectValue;
	}

	public String getRejectionLetterRemarks2() {
		return rejectionLetterRemarks2;
	}

	public void setRejectionLetterRemarks2(String rejectionLetterRemarks2) {
		this.rejectionLetterRemarks2 = rejectionLetterRemarks2;
	}

	public String getRejectionRemarks2() {
		return rejectionRemarks2;
	}

	public void setRejectionRemarks2(String rejectionRemarks2) {
		this.rejectionRemarks2 = rejectionRemarks2;
	}
	
	
	
}
