/**
 * 
 */
package com.shaic.reimbursement.medicalapproval.processclaimrequest.search;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimRequestFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String policyNo;
	private SelectValue hospitalType;
	private SelectValue intimationSource;
	private SelectValue networkHospType;
	private SelectValue treatementType;
	private SelectValue speciality;
	private SelectValue cpuCode;
	private SpecialSelectValue productName;
	private ImsUser imsUser;
	
	private Double claimedAmountFrom;
	
	private Double claimedAmountTo;
	
	private SelectValue requestedBy;
	
	private SelectValue claimType;
	private SelectValue accidentDeath;
	
	private SelectValue pendingStatusType;
	private SelectValue priorityNew;
	
	private Boolean priorityAll;
	
	private Boolean crm;
	
	private Boolean vip;
	
	private List<String> selectedPriority;
	
	
	public SelectValue getTreatementType() {
		return treatementType;
	}
	public void setTreatementType(SelectValue treatementType) {
		this.treatementType = treatementType;
	}
	public SelectValue getSpeciality() {
		return speciality;
	}
	public void setSpeciality(SelectValue speciality) {
		this.speciality = speciality;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public SelectValue getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}
	
	public SelectValue getIntimationSource() {
		return intimationSource;
	}
	public void setIntimationSource(SelectValue intimationSource) {
		this.intimationSource = intimationSource;
	}
	public SelectValue getNetworkHospType() {
		return networkHospType;
	}
	public void setNetworkHospType(SelectValue networkHospType) {
		this.networkHospType = networkHospType;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SpecialSelectValue getProductName() {
		return productName;
	}
	public void setProductName(SpecialSelectValue productName) {
		this.productName = productName;
	}
	public ImsUser getImsUser() {
		return imsUser;
	}
	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
	}
	public Double getClaimedAmountFrom() {
		return claimedAmountFrom;
	}
	public void setClaimedAmountFrom(Double claimedAmountFrom) {
		this.claimedAmountFrom = claimedAmountFrom;
	}
	public Double getClaimedAmountTo() {
		return claimedAmountTo;
	}
	public void setClaimedAmountTo(Double claimedAmountTo) {
		this.claimedAmountTo = claimedAmountTo;
	}
	public SelectValue getRequestedBy() {
		return requestedBy;
	}
	public void setRequestedBy(SelectValue requestedBy) {
		this.requestedBy = requestedBy;
	}
	public SelectValue getClaimType() {
		return claimType;
	}
	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}
	public SelectValue getAccidentDeath() {
		return accidentDeath;
	}
	public void setAccidentDeath(SelectValue accidentDeath) {
		this.accidentDeath = accidentDeath;
	}
	public SelectValue getPendingStatusType() {
		return pendingStatusType;
	}
	public void setPendingStatusType(SelectValue pendingStatusType) {
		this.pendingStatusType = pendingStatusType;
	}
	public SelectValue getPriorityNew() {
		return priorityNew;
	}
	public void setPriorityNew(SelectValue priorityNew) {
		this.priorityNew = priorityNew;
	}
	public Boolean getPriorityAll() {
		return priorityAll;
	}
	public void setPriorityAll(Boolean priorityAll) {
		this.priorityAll = priorityAll;
	}
	public Boolean getCrm() {
		return crm;
	}
	public void setCrm(Boolean crm) {
		this.crm = crm;
	}
	public Boolean getVip() {
		return vip;
	}
	public void setVip(Boolean vip) {
		this.vip = vip;
	}
	public List<String> getSelectedPriority() {
		return selectedPriority;
	}
	public void setSelectedPriority(List<String> selectedPriority) {
		this.selectedPriority = selectedPriority;
	}
	
}
