package com.shaic.claim.fvrdetails.view;

import java.util.Date;

import java.util.List;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.fvrdetailedview.FvrDetailedViewDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;



public class ViewFVRDTO extends AbstractTableDTO {
	
	
	private Long key;

	private String representativeName;

	private String representativeCode;

	private String representativeContactNo;

	private String hospitalName;

	private String hospitalVisitedDate;

	private String remarks;

	private Date fVRAssignedDate;
	
	private String fvrassignedDate;
	
	private Date fVRReceivedDate;
	
	private String fVRreceivedDate;

	private long fVRTAT;
	
	private String fvrTat;

	private String fVRStatus;
	
	private String fvrGrading;
	
	private String sno;
	
	private String status;
	
	private String category;
	
	private String applicability;
	
	private String patientVerified;
	
	private String intimationNo;
	
	private List<NewFVRGradingDTO> newFvrGradingDTO;
	
	private String fvrJson;
	
	private Integer rollNo;
	
	private SelectValue allocationTo;
	
	private SelectValue priority;
	
	private String fvrDocumentJson;
	
	private Long fvrStatusKey;
	private String fvrReceivedDate;
	
	private DMSDocumentDTO dmsDocumentList;
	
	private String gradingRemarks;
	
	private Boolean triggerPointsAlreadyExist = Boolean.FALSE;
	
	private Date createdDate;
	
	private String createdBy;
	
	private List<ViewFVRDTO> deletedList;
	private String existingTriggerPoint;

	private Long fvrNotRequiredKey;
	
	private Long stageKey;
	private Long statusKey;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getRepresentativeCode() {
		return representativeCode;
	}

	public void setRepresentativeCode(String representativeCode) {
		this.representativeCode = representativeCode;
	}

	public String getRepresentativeContactNo() {
		return representativeContactNo;
	}
	
	public void setRepresentativeContactNo(String representativeContactNo) {
		this.representativeContactNo = representativeContactNo;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalVisitedDate() {
		return hospitalVisitedDate;
	}

	public void setHospitalVisitedDate(String hospitalVisitedDate) {
		this.hospitalVisitedDate = hospitalVisitedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getfVRAssignedDate() {
		return fVRAssignedDate;
	}

	public void setfVRAssignedDate(Date date) {
		this.fVRAssignedDate = date;
	}

	public Date getfVRReceivedDate() {
		return fVRReceivedDate;
	}

	public void setfVRReceivedDate(Date fVRReceivedDate) {
		this.fVRReceivedDate = fVRReceivedDate;
	}

	public String getfVRreceivedDate() {
		return fVRreceivedDate;
	}

	public void setfVRreceivedDate(String fVRreceivedDate) {
		this.fVRreceivedDate = fVRreceivedDate;
	}

	public long getfVRTAT() {
		return fVRTAT;
	}

	public void setfVRTAT(long fVRTAT) {
		this.fVRTAT = fVRTAT;
	}

	public String getFvrTat() {
		return fvrTat;
	}

	public void setFvrTat(String fvrTat) {
		this.fvrTat = fvrTat;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getApplicability() {
		return applicability;
	}

	public void setApplicability(String applicability) {
		this.applicability = applicability;
	}

	public String getfVRStatus() {
		return fVRStatus;
	}

	public void setfVRStatus(String fVRStatus) {
		this.fVRStatus = fVRStatus;
	}

	public String getFvrassignedDate() {
		return fvrassignedDate;
	}

	public void setFvrassignedDate(String fvrassignedDate) {
		this.fvrassignedDate = fvrassignedDate;
	}

	public String getFvrGrading() {
		return fvrGrading;
	}

	public void setFvrGrading(String fvrGrading) {
		this.fvrGrading = fvrGrading;
	}

	public String getPatientVerified() {
		return patientVerified;
	}

	public void setPatientVerified(String patientVerified) {
		this.patientVerified = patientVerified;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public List<NewFVRGradingDTO> getNewFvrGradingDTO() {
		return newFvrGradingDTO;
	}

	public void setNewFvrGradingDTO(List<NewFVRGradingDTO> newFvrGradingDTO) {
		this.newFvrGradingDTO = newFvrGradingDTO;
	}

	public Integer getRollNo() {
		return rollNo;
	}

	public void setRollNo(Integer rollNo) {
		this.rollNo = rollNo;
	}

	public SelectValue getAllocationTo() {
		return allocationTo;
	}

	public void setAllocationTo(SelectValue allocationTo) {
		this.allocationTo = allocationTo;
	}

	public SelectValue getPriority() {
		return priority;
	}

	public void setPriority(SelectValue priority) {
		this.priority = priority;
	}	

	public String getFvrJson() {
		return fvrJson;
	}

	public void setFvrJson(String fvrJson) {
		this.fvrJson = fvrJson;
	}

	public String getFvrDocumentJson() {
		return fvrDocumentJson;
	}

	public void setFvrDocumentJson(String fvrDocumentJson) {
		this.fvrDocumentJson = fvrDocumentJson;
	}

	public Long getFvrStatusKey() {
		return fvrStatusKey;
	}

	public void setFvrStatusKey(Long fvrStatusKey) {
		this.fvrStatusKey = fvrStatusKey;
	}
	public DMSDocumentDTO getDmsDocumentList() {
		return dmsDocumentList;
	}

	public void setDmsDocumentList(DMSDocumentDTO dmsDocumentList) {
		this.dmsDocumentList = dmsDocumentList;
	}

	public String getFvrReceivedDate() {
		return fvrReceivedDate;
	}

	public void setFvrReceivedDate(String fvrReceivedDate) {
		this.fvrReceivedDate = fvrReceivedDate;
	}

	public String getGradingRemarks() {
		return gradingRemarks;
	}

	public void setGradingRemarks(String gradingRemarks) {
		this.gradingRemarks = gradingRemarks;
	}

	public Long getFvrNotRequiredKey() {
		return fvrNotRequiredKey;
	}

	public void setFvrNotRequiredKey(Long fvrNotRequiredKey) {
		this.fvrNotRequiredKey = fvrNotRequiredKey;
	}

	public Boolean getTriggerPointsAlreadyExist() {
		return triggerPointsAlreadyExist;
	}

	public void setTriggerPointsAlreadyExist(Boolean triggerPointsAlreadyExist) {
		this.triggerPointsAlreadyExist = triggerPointsAlreadyExist;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<ViewFVRDTO> getDeletedList() {
		return deletedList;
	}

	public void setDeletedList(List<ViewFVRDTO> deletedList) {
		this.deletedList = deletedList;
	}

	public String getExistingTriggerPoint() {
		return existingTriggerPoint;
	}

	public void setExistingTriggerPoint(String existingTriggerPoint) {
		this.existingTriggerPoint = existingTriggerPoint;
	}

	public Long getStageKey() {
		return stageKey;
	}

	public void setStageKey(Long stageKey) {
		this.stageKey = stageKey;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}
}
