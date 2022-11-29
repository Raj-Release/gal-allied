package com.shaic.claim.withdrawWizard;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessTableDTO;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class WithdrawPreauthPageDTO {
	
	@NotNull(message="Please Select Reason for Withdraw")
	private SelectValue reasonForWithdraw;
	
	@NotNull(message="Please Select Rejection Category")
	private SelectValue rejectionCategory;
	
	private String policyConditionNoReject;
	
	@NotNull(message="Please Select Medical Remarks")
	@Size (min = 1 ,message = "Please Select Medical Remarks ")
	private String medicalRemarks;
	
	private String doctorNote;
	
	private String referenceNo;
	
	private String referenceType;
	
	private Boolean isDownsizeOrEscalate;
	
	private String treatmentType;
	
	private Long requestedAmt;
	
	private Long approvedAmt;
	
	private Double totalApprovedAmt;
	
	private Boolean isEscalateClaim;
	
	private SelectValue escalateTo;
	
	@NotNull(message="Please Enter Escalate Remarks")
	private String escalateRemarks;
	
	@NotNull(message="Please Enter Withdrawal Remarks")
	@Size (min = 1 ,message = "Please Enter Withdrawal Remarks ")
	private String withdrawRemarks;
		
	private String insuredWithdrawRemarks;
	
	@NotNull(message="Please Enter Rejection Remarks")
	@Size (min = 1 ,message = "Please Enter Rejection Remarks ")
	private String rejectionRemarks;
	
	private String insuredRejectionRemarks;
	
	private SearchWithdrawCashLessProcessTableDTO tableDto;
	
	private BeanItemContainer<SelectValue> selectValueContainer;
	
	private BeanItemContainer<SelectValue> escalateContainer;
	
	private NewIntimationDto newIntimationDto;
	
	private List<PreviousPreAuthTableDTO> previousPreAuthTableDTO;
	
	private ClaimDto claimDto;
	
	private Preauth preauth;
	
	private PreauthDTO preauthDto;
	
	private Long statusKey;
	
//	@NotNull(message = "Please Enter Opinion Given Remarks.")
//	@Size(min=1 , message ="Please Enter Opinion Given Remarks.")
	private String remarksFromDeptHead;
	
	@NotNull(message = "Please Select Consulted with.")
	private Object userRoleMulti;
	
	private Object doctorName;
	
	private List<MedicalDecisionTableDTO> medicalDecisionDto;
	
	//@NotNull(message="Please Enter downsized Amount")
	private Double downSizedAmount;
	
	@NotNull(message="Please Select Reason for Downsize")
	private SelectValue reasonForDownSize;
	
	public SelectValue getReasonForWithdraw() {
		return reasonForWithdraw;
	}

	public void setReasonForWithdraw(SelectValue reasonForWithdraw) {
		this.reasonForWithdraw = reasonForWithdraw;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	public String getDoctorNote() {
		return doctorNote;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public Long getRequestedAmt() {
		return requestedAmt;
	}

	public void setRequestedAmt(Long requestedAmt) {
		this.requestedAmt = requestedAmt;
	}

	public Long getApprovedAmt() {
		return approvedAmt;
	}

	public void setApprovedAmt(Long approvedAmt) {
		this.approvedAmt = approvedAmt;
	}

	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}

	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public BeanItemContainer<SelectValue> getSelectValueContainer() {
		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getEscalateContainer() {
		return escalateContainer;
	}

	public void setEscalateContainer(
			BeanItemContainer<SelectValue> escalateContainer) {
		this.escalateContainer = escalateContainer;
	}

	public void setSelectValueContainer(
			BeanItemContainer<SelectValue> selectValueContainer) {
		this.selectValueContainer = selectValueContainer;
	}

	public SearchWithdrawCashLessProcessTableDTO getTableDto() {
		return tableDto;
	}

	public void setTableDto(SearchWithdrawCashLessProcessTableDTO tableDto) {
		this.tableDto = tableDto;
	}

	public List<PreviousPreAuthTableDTO> getPreviousPreAuthTableDTO() {
		return previousPreAuthTableDTO;
	}

	public void setPreviousPreAuthTableDTO(
			List<PreviousPreAuthTableDTO> previousPreAuthTableDTO) {
		this.previousPreAuthTableDTO = previousPreAuthTableDTO;
	}

	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public Double getDownSizedAmount() {
		downSizedAmount=0.0;
		return downSizedAmount;
	}

	public void setDownSizedAmount(Double downSizedAmount) {
		this.downSizedAmount = downSizedAmount;
	}

	public SelectValue getReasonForDownSize() {
		return reasonForDownSize;
	}

	public void setReasonForDownSize(SelectValue reasonForDownSize) {
		this.reasonForDownSize = reasonForDownSize;
	}

	public SelectValue getEscalateTo() {
		return escalateTo;
	}

	public void setEscalateTo(SelectValue escalateTo) {
		this.escalateTo = escalateTo;
	}

	public String getEscalateRemarks() {
		return escalateRemarks;
	}

	public void setEscalateRemarks(String escalateRemarks) {
		this.escalateRemarks = escalateRemarks;
	}

	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}

	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
	}

	public List<MedicalDecisionTableDTO> getMedicalDecisionDto() {
		return medicalDecisionDto;
	}

	public void setMedicalDecisionDto(
			List<MedicalDecisionTableDTO> medicalDecisionDto) {
		this.medicalDecisionDto = medicalDecisionDto;
	}

	public Double getTotalApprovedAmt() {
		return totalApprovedAmt;
	}

	public void setTotalApprovedAmt(Double totalApprovedAmt) {
		this.totalApprovedAmt = totalApprovedAmt;
	}

	public Boolean getIsDownsizeOrEscalate() {
		return isDownsizeOrEscalate;
	}

	public void setIsDownsizeOrEscalate(Boolean isDownsizeOrEscalate) {
		this.isDownsizeOrEscalate = isDownsizeOrEscalate;
	}

	public Boolean getIsEscalateClaim() {
		return isEscalateClaim;
	}

	public void setIsEscalateClaim(Boolean isEscalateClaim) {
		this.isEscalateClaim = isEscalateClaim;
	}

	public String getWithdrawRemarks() {
		return withdrawRemarks;
	}

	public void setWithdrawRemarks(String withdrawRemarks) {
		this.withdrawRemarks = withdrawRemarks;
	}

	public SelectValue getRejectionCategory() {
		return rejectionCategory;
	}

	public void setRejectionCategory(SelectValue rejectionCategory) {
		this.rejectionCategory = rejectionCategory;
	}

	public String getRejectionRemarks() {
		return rejectionRemarks;
	}

	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public String getInsuredWithdrawRemarks() {
		return insuredWithdrawRemarks;
	}

	public void setInsuredWithdrawRemarks(String insuredWithdrawRemarks) {
		this.insuredWithdrawRemarks = insuredWithdrawRemarks;
	}

	public String getInsuredRejectionRemarks() {
		return insuredRejectionRemarks;
	}

	public void setInsuredRejectionRemarks(String insuredRejectionRemarks) {
		this.insuredRejectionRemarks = insuredRejectionRemarks;
	}

	public String getPolicyConditionNoReject() {
		return policyConditionNoReject;
	}

	public void setPolicyConditionNoReject(String policyConditionNoReject) {
		this.policyConditionNoReject = policyConditionNoReject;
	}

	public String getRemarksFromDeptHead() {
		return remarksFromDeptHead;
	}

	public void setRemarksFromDeptHead(String remarksFromDeptHead) {
		this.remarksFromDeptHead = remarksFromDeptHead;
	}

	public Object getUserRoleMulti() {
		return userRoleMulti;
	}

	public void setUserRoleMulti(Object userRoleMulti) {
		this.userRoleMulti = userRoleMulti;
	}

	public Object getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(Object doctorName) {
		this.doctorName = doctorName;
	}	
	
}
