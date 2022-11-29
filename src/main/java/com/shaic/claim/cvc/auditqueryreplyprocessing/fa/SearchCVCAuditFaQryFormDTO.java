package com.shaic.claim.cvc.auditqueryreplyprocessing.fa;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

/**
 * @author GokulPrasath.A
 *
 */
public class SearchCVCAuditFaQryFormDTO extends AbstractSearchDTO  implements Serializable{

	private String claimTypeId;
	private ImsUser imsUser;	
	private String intimationNumber;
	private SelectValue cmbauditStatus;
	private String userId;
	private SelectValue clmType;
	private String tabStatus;
	
	
	public String getClaimTypeId() {
		return claimTypeId;
	}
	public void setClaimTypeId(String claimTypeId) {
		this.claimTypeId = claimTypeId;
	}
	public ImsUser getImsUser() {
		return imsUser;
	}
	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
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
		public SelectValue getCmbauditStatus() {
		return cmbauditStatus;
	}
	public void setCmbauditStatus(SelectValue cmbauditStatus) {
		this.cmbauditStatus = cmbauditStatus;
	}
	public SelectValue getClmType() {
		return clmType;
	}
	public void setClmType(SelectValue clmType) {
		this.clmType = clmType;
	}
	public String getTabStatus() {
		return tabStatus;
	}
	public void setTabStatus(String tabStatus) {
		this.tabStatus = tabStatus;
	}
	
}
