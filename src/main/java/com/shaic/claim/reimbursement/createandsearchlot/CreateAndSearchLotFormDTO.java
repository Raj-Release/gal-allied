package com.shaic.claim.reimbursement.createandsearchlot;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.domain.ReferenceTable;

public class CreateAndSearchLotFormDTO extends AbstractSearchDTO implements Serializable{
	private Date fromDate = new Date();
	private Date toDate = new Date();
	private SelectValue type;
	private SelectValue claimType;
	private SelectValue cpuCode;
	private Object cpuCodeMulti;	
	private SelectValue claimant;
	private SelectValue paymentStatus = new SelectValue(ReferenceTable.PAYMENT_STATUS_FRESH, "Fresh");
	private String lotNo;
	private String intimationNo;
	private String rodNo;
	private String claimNo;
	private String batchNo;
	private SpecialSelectValue product;		
	private SelectValue docVerified;	
	private String searchTabType;
	private String quickIntimationNo;
	private SelectValue verificationType;
	
	private List<String> selectedPriority;

	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
	}
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getClaimant() {
		return claimant;
	}
	public void setClaimant(SelectValue claimant) {
		this.claimant = claimant;
	}
	public SelectValue getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(SelectValue paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public SpecialSelectValue getProduct() {
		return product;
	}
	public void setProduct(SpecialSelectValue product) {
		this.product = product;
	}
	public SelectValue getDocVerified() {
		return docVerified;
	}
	public void setDocVerified(SelectValue docVerified) {
		this.docVerified = docVerified;
	}
	public String getSearchTabType() {
		return searchTabType;
	}
	public void setSearchTabType(String searchTabType) {
		this.searchTabType = searchTabType;
	}
	public String getQuickIntimationNo() {
		return quickIntimationNo;
	}
	public void setQuickIntimationNo(String quickIntimationNo) {
		this.quickIntimationNo = quickIntimationNo;
	}
	public Object getCpuCodeMulti() {
		return cpuCodeMulti;
	}
	public void setCpuCodeMulti(Object cpuCodeMulti) {
		this.cpuCodeMulti = cpuCodeMulti;
	}
	public SelectValue getVerificationType() {
		return verificationType;
	}
	public void setVerificationType(SelectValue verificationType) {
		this.verificationType = verificationType;
	}
	public List<String> getSelectedPriority() {
		return selectedPriority;
	}
	public void setSelectedPriority(List<String> selectedPriority) {
		this.selectedPriority = selectedPriority;
	}
	
}
