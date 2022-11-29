package com.shaic.claim.cvc;

import java.io.Serializable;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

public class SearchCVCFormDTO extends AbstractSearchDTO  implements Serializable{

	private String claimTypeId;
	private ImsUser imsUser;
	private String intimationNo;
	private String reason;
	private String year;
	private Object productNameMulti;
	private String productName;
	
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Object getProductNameMulti() {
		return productNameMulti;
	}
	public void setProductNameMulti(Object productNameMulti) {
		this.productNameMulti = productNameMulti;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
}
