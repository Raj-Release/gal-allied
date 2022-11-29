package com.shaic.claim.lumen.create;

import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.SearchTableDTO;

public class SearchLumenStatusWiseDto extends SearchTableDTO {
	
	private Date frmDate;
	private Date toDate;
	private SelectValue statusSelect;
	private SelectValue cpuSelect;
	private SelectValue claimType;
	private String cpuCodeList;
	private String statusList;
	
	private int sno;
	private String date;
	private String clmNo;
	private String doi;
	private String policyYr;
	private String productType;
	private String policyNo;
	private String policyIssueOffice;
	private String agentCode;
	private Long suminsured;
	private String doa;
	private String dod;
	private String hospitalName;
	private String ailment;
	private Long claimedAmount;
	private String descreption;
	private String typeofError;
	private String claimHistory;
	private String lapse1;
	private String lapse2;
	private String lapse3;
	private String lapse4;
	private String lapse5;
	private String cpu;
	private String status;
	
	private List<SearchLumenStatusWiseDto> searchResultList;
	
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
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getClmNo() {
		return clmNo;
	}
	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	public String getPolicyYr() {
		return policyYr;
	}
	public void setPolicyYr(String policyYr) {
		this.policyYr = policyYr;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getPolicyIssueOffice() {
		return policyIssueOffice;
	}
	public void setPolicyIssueOffice(String policyIssueOffice) {
		this.policyIssueOffice = policyIssueOffice;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public Long getSuminsured() {
		return suminsured;
	}
	public void setSuminsured(Long suminsured) {
		this.suminsured = suminsured;
	}
	public String getDoa() {
		return doa;
	}
	public void setDoa(String doa) {
		this.doa = doa;
	}
	public String getDod() {
		return dod;
	}
	public void setDod(String dod) {
		this.dod = dod;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getAilment() {
		return ailment;
	}
	public void setAilment(String ailment) {
		this.ailment = ailment;
	}
	public Long getClaimedAmount() {
		return claimedAmount;
	}
	public void setClaimedAmount(Long claimedAmount) {
		this.claimedAmount = claimedAmount;
	}
	public String getDescreption() {
		return descreption;
	}
	public void setDescreption(String descreption) {
		this.descreption = descreption;
	}
	public String getTypeofError() {
		return typeofError;
	}
	public void setTypeofError(String typeofError) {
		this.typeofError = typeofError;
	}
	public String getClaimHistory() {
		return claimHistory;
	}
	public void setClaimHistory(String claimHistory) {
		this.claimHistory = claimHistory;
	}
	public String getLapse1() {
		return lapse1;
	}
	public void setLapse1(String lapse1) {
		this.lapse1 = lapse1;
	}
	public String getLapse2() {
		return lapse2;
	}
	public void setLapse2(String lapse2) {
		this.lapse2 = lapse2;
	}
	public String getLapse3() {
		return lapse3;
	}
	public void setLapse3(String lapse3) {
		this.lapse3 = lapse3;
	}
	public String getLapse4() {
		return lapse4;
	}
	public void setLapse4(String lapse4) {
		this.lapse4 = lapse4;
	}
	public String getLapse5() {
		return lapse5;
	}
	public void setLapse5(String lapse5) {
		this.lapse5 = lapse5;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<SearchLumenStatusWiseDto> getSearchResultList() {
		return searchResultList;
	}
	public void setSearchResultList(List<SearchLumenStatusWiseDto> searchResultList) {
		this.searchResultList = searchResultList;
	}
	public String getStatusList() {
		return statusList;
	}
	public void setStatusList(String statusList) {
		this.statusList = statusList;
	}	
}
