/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.medicalapproval.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

/**
 * @author ntv.narenj
 *
 */
public class PAHealthSearchProcessClaimRequestFormDTO extends AbstractSearchDTO implements Serializable{
	
	
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
	
	private SelectValue accidentDeath;
	
	
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
	public SelectValue getAccidentDeath() {
		return accidentDeath;
	}
	public void setAccidentDeath(SelectValue accidentDeath) {
		this.accidentDeath = accidentDeath;
	}
	
	
}
