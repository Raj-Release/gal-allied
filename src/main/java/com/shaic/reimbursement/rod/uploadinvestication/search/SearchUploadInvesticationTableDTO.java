/**
 * 
 */
package com.shaic.reimbursement.rod.uploadinvestication.search;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.uploadrodreports.UploadedDocumentsDTO;


/**
 * 
 *
 */
public class SearchUploadInvesticationTableDTO extends AbstractTableDTO  implements Serializable{

	private Long key;
	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String insuredPatientName;
	private String cpuCode;
	private String hospitalName;
	private String hospitalAddress;
	private String hospitalCity;
	private String reasonForAdmission;
	private String claimStatus;
	private Long hospitalNameID;
	private String hospitalType;
	private Long cpuId;
	private Long rodKey;
	private Long investigationKey;
	private Long investigationAssignedKey;
	private RRCDTO rrcDTO;
	private String claimType;
	private String claimBackgroundDetails;
	
	private String factsOfCase;
	private	List<DraftTriggerPointsToFocusDetailsTableDto> investigatorTriggerPointsList;
	private Object dbOutArray ;
	private String investigationCompletionRemarks;
	
//	private List<MultipleUploadDocumentDTO> documentListDto;	
	
	private List<UploadedDocumentsDTO> uploadedFileTableDto; 	
	
	private List<UploadedDocumentsDTO> uploadedNDeletedFileListDto;
	
	private PreauthDTO preauthDTO;
	
	private String crmFlagged;
	
	private String crcFlaggedReason;
	
	private String crcFlaggedRemark;
	
	private Boolean isGeoSame = Boolean.TRUE;
	
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(Long cpuCode) {
		this.cpuCode = ""+cpuCode;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public Long getHospitalNameID() {
		return hospitalNameID;
	}
	public void setHospitalNameID(Long hospitalNameID) {
		this.hospitalNameID = hospitalNameID;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	public Long getCpuId() {
		return cpuId;
	}
	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}
	public Long getInvestigationKey() {
		return investigationKey;
	}
	public void setInvestigationKey(Long investigationKey) {
		this.investigationKey = investigationKey;
	}
	
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}
	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}
	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}
	
	public String getClaimBackgroundDetails() {
		return claimBackgroundDetails;
	}
	public void setClaimBackgroundDetails(String claimBackgroundDetails) {
		this.claimBackgroundDetails = claimBackgroundDetails;
	}
	public String getFactsOfCase() {
		return factsOfCase;
	}
	public void setFactsOfCase(String factsOfCase) {
		this.factsOfCase = factsOfCase;
	}
	public List<DraftTriggerPointsToFocusDetailsTableDto> getInvestigatorTriggerPointsList() {
		return investigatorTriggerPointsList;
	}
	public void setInvestigatorTriggerPointsList(
			List<DraftTriggerPointsToFocusDetailsTableDto> investigatorTriggerPointsList) {
		this.investigatorTriggerPointsList = investigatorTriggerPointsList;
	}
	public Object getDbOutArray() {
		return dbOutArray;
	}
	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}
	public String getInvestigationCompletionRemarks() {
		return investigationCompletionRemarks;
	}
	public void setInvestigationCompletionRemarks(
			String investigationCompletionRemarks) {
		this.investigationCompletionRemarks = investigationCompletionRemarks;
	}
	public Long getInvestigationAssignedKey() {
		return investigationAssignedKey;
	}
	public void setInvestigationAssignedKey(Long investigationAssignedKey) {
		this.investigationAssignedKey = investigationAssignedKey;
	}
//	public List<MultipleUploadDocumentDTO> getDocumentListDto() {
//		return documentListDto;
//	}
//	public void setDocumentListDto(List<MultipleUploadDocumentDTO> documentListDto) {
//		this.documentListDto = documentListDto;
//	}
	public List<UploadedDocumentsDTO> getUploadedFileTableDto() {
		return uploadedFileTableDto;
	}
	public void setUploadedFileTableDto(
			List<UploadedDocumentsDTO> uploadedFileTableDto) {
		this.uploadedFileTableDto = uploadedFileTableDto;
	}
	public List<UploadedDocumentsDTO> getUploadedNDeletedFileListDto() {
		return uploadedNDeletedFileListDto;
	}
	public void setUploadedNDeletedFileListDto(
			List<UploadedDocumentsDTO> uploadedNDeletedFileListDto) {
		this.uploadedNDeletedFileListDto = uploadedNDeletedFileListDto;
	}
	public String getCrmFlagged() {
		return crmFlagged;
	}
	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
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
	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}
	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}
	public Boolean getIsGeoSame() {
		return isGeoSame;
	}
	public void setIsGeoSame(Boolean isGeoSame) {
		this.isGeoSame = isGeoSame;
	}
	
	

}
