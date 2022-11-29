package com.shaic.claim.pcc.dto;

import java.util.Date;
import java.util.List;

import com.shaic.claim.pcc.views.PCCUploadedFileDocsTable;

public class ViewPCCRemarksDTO {

	private Long pccKey;

	private String intimationId;

	private String userNameAndIdforDoct;

	private Date dateAndTimeforDoct;

	private String remarksTypeforDoct;

	private String remarksforDoct;

	private String userRoleforDoct;

	private String userNameAndId;

	private Date dateAndTime;

	private String remarksType;

	private String remarks;

	private String userRole;
	
	private List<PCCQueryDetailsTableDTO> queryDetails;

	private List<PCCReplyDetailsTableDTO> replyDetails;
	
	private List<PCCApproveDetailsTableDTO> pccDetails;
	
	private List<PCCUploadedFileDocsDTO> pccUploadedFileDetails;
	
	private String uploadDocFileName;

	public Long getPccKey() {
		return pccKey;
	}

	public void setPccKey(Long pccKey) {
		this.pccKey = pccKey;
	}

	public String getIntimationId() {
		return intimationId;
	}

	public void setIntimationId(String intimationId) {
		this.intimationId = intimationId;
	}

	public String getUserNameAndIdforDoct() {
		return userNameAndIdforDoct;
	}

	public void setUserNameAndIdforDoct(String userNameAndIdforDoct) {
		this.userNameAndIdforDoct = userNameAndIdforDoct;
	}

	public Date getDateAndTimeforDoct() {
		return dateAndTimeforDoct;
	}

	public void setDateAndTimeforDoct(Date dateAndTimeforDoct) {
		this.dateAndTimeforDoct = dateAndTimeforDoct;
	}

	public String getRemarksTypeforDoct() {
		return remarksTypeforDoct;
	}

	public void setRemarksTypeforDoct(String remarksTypeforDoct) {
		this.remarksTypeforDoct = remarksTypeforDoct;
	}

	public String getRemarksforDoct() {
		return remarksforDoct;
	}

	public void setRemarksforDoct(String remarksforDoct) {
		this.remarksforDoct = remarksforDoct;
	}

	public String getUserRoleforDoct() {
		return userRoleforDoct;
	}

	public void setUserRoleforDoct(String userRoleforDoct) {
		this.userRoleforDoct = userRoleforDoct;
	}

	public String getUserNameAndId() {
		return userNameAndId;
	}

	public void setUserNameAndId(String userNameAndId) {
		this.userNameAndId = userNameAndId;
	}

	public Date getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getRemarksType() {
		return remarksType;
	}

	public void setRemarksType(String remarksType) {
		this.remarksType = remarksType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public List<PCCQueryDetailsTableDTO> getQueryDetails() {
		return queryDetails;
	}

	public void setQueryDetails(List<PCCQueryDetailsTableDTO> queryDetails) {
		this.queryDetails = queryDetails;
	}

	public List<PCCReplyDetailsTableDTO> getReplyDetails() {
		return replyDetails;
	}

	public void setReplyDetails(List<PCCReplyDetailsTableDTO> replyDetails) {
		this.replyDetails = replyDetails;
	}

	public List<PCCApproveDetailsTableDTO> getPccDetails() {
		return pccDetails;
	}

	public void setPccDetails(List<PCCApproveDetailsTableDTO> pccDetails) {
		this.pccDetails = pccDetails;
	}

	public String getUploadDocFileName() {
		return uploadDocFileName;
	}

	public void setUploadDocFileName(String uploadDocFileName) {
		this.uploadDocFileName = uploadDocFileName;
	}

	public List<PCCUploadedFileDocsDTO> getPccUploadedFileDetails() {
		return pccUploadedFileDetails;
	}

	public void setPccUploadedFileDetails(
			List<PCCUploadedFileDocsDTO> pccUploadedFileDetails) {
		this.pccUploadedFileDetails = pccUploadedFileDetails;
	}
	
	

}
