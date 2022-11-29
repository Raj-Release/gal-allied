package com.shaic.paclaim.cashless.preauth.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

@SuppressWarnings("serial")
public class PASearchPreauthFormDTO  extends AbstractSearchDTO  implements Serializable {
	
	private String intimationNo;
	
	private SelectValue type;
	private String policyNo;
	private SelectValue intimationSource;
	private SelectValue networkHospType;
	private SelectValue treatmentType;
	private SelectValue cpuCode;
	private String claimedAmtFrom;
	private String claimedAmtTo;
	
	private ImsUser imsUser;
	
	private SelectValue speciality;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	/*public Long getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(Long intimationNo) {
		this.intimationNo = intimationNo;
	}*/
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
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
	public SelectValue getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(SelectValue treatmentType) {
		this.treatmentType = treatmentType;
	}
	public SelectValue getSpeciality() {
		return speciality;
	}
	public void setSpeciality(SelectValue speciality) {
		this.speciality = speciality;
	}
	public ImsUser getImsUser() {
		return imsUser;
	}
	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getClaimedAmtFrom() {
		return claimedAmtFrom;
	}
	public void setClaimedAmtFrom(String claimedAmtFrom) {
		this.claimedAmtFrom = claimedAmtFrom;
	}
	public String getClaimedAmtTo() {
		return claimedAmtTo;
	}
	public void setClaimedAmtTo(String claimedAmtTo) {
		this.claimedAmtTo = claimedAmtTo;
	}
}
