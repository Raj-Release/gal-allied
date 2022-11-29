package com.shaic.claim.reports.investigationassignedreport;

import java.util.Date;
import java.util.List;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.SearchTableDTO;
/**
 * AS Part of CR  R0768
 * @author Lakshminarayana
 *
 */
public class InvAssignStatusReportDto extends SearchTableDTO {
	
	private Date frmDate;
	private Date toDate;
	private SelectValue statusSelect;
	private SelectValue cpuSelect;
	private SelectValue claimType;
	private String cpuCodeList;
	private String statusList;
	
	private int sno;
	private String intimationNumber;
	private String refRodNo;
	private String patientName;
	private String policyNumber;
	private String clmType;
	private String cpu;
	private String hospitalName;
	private String hospitalType;
	private String hosLocation;
	private String aliment;
	private Double claimedAmount;
	private String claimStatus;
	private String saettledAmount;
	private String rvo;
	private String invApprovedDate;
	private String draftInvLetterDate;
	private String clmReqDate;
	private String invRplyDate;
	private int invTat;
	private String invrptUploadBy;
	
	private List<InvAssignStatusReportDto> searchResultList;

	public Date getFrmDate() {
		return frmDate;
	}

	public void setFrmDate(Date frmDate) {
		this.frmDate = frmDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public SelectValue getStatusSelect() {
		return statusSelect;
	}

	public void setStatusSelect(SelectValue statusSelect) {
		this.statusSelect = statusSelect;
	}

	public SelectValue getCpuSelect() {
		return cpuSelect;
	}

	public void setCpuSelect(SelectValue cpuSelect) {
		this.cpuSelect = cpuSelect;
	}

	public SelectValue getClaimType() {
		return claimType;
	}

	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}

	public String getCpuCodeList() {
		return cpuCodeList;
	}

	public void setCpuCodeList(String cpuCodeList) {
		this.cpuCodeList = cpuCodeList;
	}

	public String getStatusList() {
		return statusList;
	}

	public void setStatusList(String statusList) {
		this.statusList = statusList;
	}

	public int getSno() {
		return sno;
	}

	public void setSno(int sno) {
		this.sno = sno;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getRefRodNo() {
		return refRodNo;
	}

	public void setRefRodNo(String refRodNo) {
		this.refRodNo = refRodNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getClmType() {
		return clmType;
	}

	public void setClmType(String clmType) {
		this.clmType = clmType;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getHosLocation() {
		return hosLocation;
	}

	public void setHosLocation(String hosLocation) {
		this.hosLocation = hosLocation;
	}

	public String getAliment() {
		return aliment;
	}

	public void setAliment(String aliment) {
		this.aliment = aliment;
	}

	public Double getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Double claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getSaettledAmount() {
		return saettledAmount;
	}

	public void setSaettledAmount(String saettledAmount) {
		this.saettledAmount = saettledAmount;
	}

	public String getRvo() {
		return rvo;
	}

	public void setRvo(String rvo) {
		this.rvo = rvo;
	}

	public String getInvApprovedDate() {
		return invApprovedDate;
	}

	public void setInvApprovedDate(String invApprovedDate) {
		this.invApprovedDate = invApprovedDate;
	}

	public String getDraftInvLetterDate() {
		return draftInvLetterDate;
	}

	public void setDraftInvLetterDate(String draftInvLetterDate) {
		this.draftInvLetterDate = draftInvLetterDate;
	}
	
	public String getClmReqDate() {
		return clmReqDate;
	}

	public void setClmReqDate(String clmReqDate) {
		this.clmReqDate = clmReqDate;
	}
	
	public String getInvRplyDate() {
		return invRplyDate;
	}

	public void setInvRplyDate(String invRplyDate) {
		this.invRplyDate = invRplyDate;
	}

	public int getInvTat() {
		return invTat;
	}

	public void setInvTat(int invTat) {
		this.invTat = invTat;
	}

	public String getInvrptUploadBy() {
		return invrptUploadBy;
	}

	public void setInvrptUploadBy(String invrptUploadBy) {
		this.invrptUploadBy = invrptUploadBy;
	}

	public List<InvAssignStatusReportDto> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<InvAssignStatusReportDto> searchResultList) {
		this.searchResultList = searchResultList;
	}	

}
