/**
 * 
 */
package com.shaic.reimbursement.financialapprover.processclaimrequestBenefits.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessClaimRequestBenefitsFinancFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String policyNo;
	private SelectValue hospitalType;
	private SelectValue type;
	private SelectValue intimationSource;
	private SelectValue networkHospType;
	private SelectValue treatementType;
	private SelectValue speciality;
	
	
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
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
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
	
	
	
}
