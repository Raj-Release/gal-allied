//package com.shaic.claim.preauth.wizard.dto;
//
//import java.io.File;
//
//import javax.validation.constraints.NotNull;
//
//import com.google.gwt.user.client.ui.FileUpload;
//import com.shaic.domain.MastersValue;
//import com.vaadin.v7.ui.ComboBox;
//import com.vaadin.v7.ui.Label;
//import com.vaadin.v7.ui.TextArea;
//import com.vaadin.v7.ui.TextField;
//
//public class MedicalDecisionDTO 
//{
//	@NotNull(message = "Please Enter Initial Approved Amount.")
//	private String initialApprovedAmt;
//	
//	@NotNull(message = "Please Enter Selected Copay.")
//	private String selectedCopay;
//	
//	private String initialTotalApprovedAmt;
//	
//	@NotNull(message = "Please Enter Approval Remarks.")
//	private String approvalRemarks;
//	
//	@NotNull(message = "Please Enter Query Remarks.")
//	private String queryRemarks;
//	
//	@NotNull(message = "Please Select Rejection Category.")
//	private MastersValue rejectionCategory;
//	
//	@NotNull(message = "Please Enter Rejection Remarks.")
//	private String rejectionRemarks;
//	
//	@NotNull(message = "Please Select Reason for Denial.")
//	private MastersValue reasonForDenial;
//	
//	@NotNull(message = "Please Enter Denial Remarks.")
//	private String denialRemarks;
//	
//	private MastersValue escalateTo;
//	
//	private String uploadFileName;
//	
//	private File uploadFile;
//	
//	@NotNull(message = "Please Enter Escalation Remarks.")
//	private String escalationRemarks;
//	
//	@NotNull(message = "Please Select Type of Coordinator Request.")
//	private MastersValue typeOfCoordinatorRequest;
//	
//	@NotNull(message = "Please Enter Reason for Refering.")
//	private String reasonForRefering;
//	
//	public String getInitialApprovedAmt() {
//		return initialApprovedAmt;
//	}
//	public void setInitialApprovedAmt(String initialApprovedAmt) {
//		this.initialApprovedAmt = initialApprovedAmt;
//	}
//	public String getSelectedCopay() {
//		return selectedCopay;
//	}
//	public void setSelectedCopay(String selectedCopay) {
//		this.selectedCopay = selectedCopay;
//	}
//	public String getInitialTotalApprovedAmt() {
//		return initialTotalApprovedAmt;
//	}
//	public void setInitialTotalApprovedAmt(String initialTotalApprovedAmt) {
//		this.initialTotalApprovedAmt = initialTotalApprovedAmt;
//	}
//	public String getApprovalRemarks() {
//		return approvalRemarks;
//	}
//	public void setApprovalRemarks(String approvalRemarks) {
//		this.approvalRemarks = approvalRemarks;
//	}
//	public String getQueryRemarks() {
//		return queryRemarks;
//	}
//	public void setQueryRemarks(String queryRemarks) {
//		this.queryRemarks = queryRemarks;
//	}
//	public MastersValue getRejectionCategory() {
//		return rejectionCategory;
//	}
//	public void setRejectionCategory(MastersValue rejectionCategory) {
//		this.rejectionCategory = rejectionCategory;
//	}
//	public String getRejectionRemarks() {
//		return rejectionRemarks;
//	}
//	public void setRejectionRemarks(String rejectionRemarks) {
//		this.rejectionRemarks = rejectionRemarks;
//	}
//	public MastersValue getReasonForDenial() {
//		return reasonForDenial;
//	}
//	public void setReasonForDenial(MastersValue reasonForDenial) {
//		this.reasonForDenial = reasonForDenial;
//	}
//	public String getDenialRemarks() {
//		return denialRemarks;
//	}
//	public void setDenialRemarks(String denialRemarks) {
//		this.denialRemarks = denialRemarks;
//	}
//	public MastersValue getEscalateTo() {
//		return escalateTo;
//	}
//	public void setEscalateTo(MastersValue escalateTo) {
//		this.escalateTo = escalateTo;
//	}
//	public String getUploadFileName() {
//		return uploadFileName;
//	}
//	public void setUploadFileName(String uploadFileName) {
//		this.uploadFileName = uploadFileName;
//	}
//	public File getUploadFile() {
//		return uploadFile;
//	}
//	public void setUploadFile(File uploadFile) {
//		this.uploadFile = uploadFile;
//	}
//	public String getEscalationRemarks() {
//		return escalationRemarks;
//	}
//	public void setEscalationRemarks(String escalationRemarks) {
//		this.escalationRemarks = escalationRemarks;
//	}
//	public MastersValue getTypeOfCoordinatorRequest() {
//		return typeOfCoordinatorRequest;
//	}
//	public void setTypeOfCoordinatorRequest(MastersValue typeOfCoordinatorRequest) {
//		this.typeOfCoordinatorRequest = typeOfCoordinatorRequest;
//	}
//	public String getReasonForRefering() {
//		return reasonForRefering;
//	}
//	public void setReasonForReferring(String reasonForRefering) {
//		this.reasonForRefering = reasonForRefering;
//	}
//
//}
