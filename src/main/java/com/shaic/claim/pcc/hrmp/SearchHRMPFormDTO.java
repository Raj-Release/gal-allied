package com.shaic.claim.pcc.hrmp;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

/**
 * @author GokulPrasath.A
 *
 */
public class SearchHRMPFormDTO extends AbstractSearchDTO  implements Serializable{

	private String intimationNumber;
	private SelectValue cpuCode;
	private SelectValue hospitalCode;
	private String userId;
	private String tabStatus;
	
	public String getTabStatus() {
		return tabStatus;
	}
	public void setTabStatus(String tabStatus) {
		this.tabStatus = tabStatus;
	}
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(SelectValue hospitalCode) {
		this.hospitalCode = hospitalCode;
	}	
	
}