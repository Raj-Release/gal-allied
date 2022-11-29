package com.shaic.claim.cashlessprocess.downsize.search;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

public class SearchDownsizeCashLessProcessFormDTO extends AbstractSearchDTO
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String intimationNo;
	private SelectValue type;
	private String policyNo;
	private SelectValue intimationSource;
	private SelectValue networkHospType;
	
	private ImsUser imsUser;
	
	private Boolean priorityAll;
	
	private Boolean crm;
	
	private Boolean vip;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

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

	public ImsUser getImsUser() {
		return imsUser;
	}

	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
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

	
}
