package com.shaic.claim.omp.createintimation;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPCreateIntimationFormDTO extends AbstractSearchDTO implements Serializable{

	private static final long serialVersionUID = -1773913101219872527L;
	
	private String intimationNo;
	private SelectValue type;
	private String policyNo;
	private String insuredName;
	private String claimNumber;
	private SelectValue intimationStatus;
	private SelectValue productCode;
	private String passportno;


	public String getPassportno(){
		return passportno;
	}
	public void setPassportno(String passportno){
		this.passportno = passportno;
	}
	
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
	
	/*public SelectValue getIntimationStatus() {
		return intimationStatus;
	}
	public void setIntimationStation(SelectValue intimationStatus) {
		this.intimationStatus = intimationStatus;
	}*/
	
	public SelectValue getIntimationStatus() {
		return intimationStatus;
	}
	public void setIntimationStatus(SelectValue intimationStatus) {
		this.intimationStatus = intimationStatus;
	}
	public SelectValue getProductCode() {
		return productCode;
	}
	public void setProductCode(SelectValue productCode) {
		this.productCode = productCode;
	}
	
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

}
