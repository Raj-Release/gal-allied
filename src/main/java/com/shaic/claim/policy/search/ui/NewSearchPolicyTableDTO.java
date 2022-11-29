/**
 * 
 */
package com.shaic.claim.policy.search.ui;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.vijayar
 *
 */
public class NewSearchPolicyTableDTO  extends AbstractTableDTO  implements Serializable {
	
	private static final long serialVersionUID = 12L;
	
	private String polNo;
	private String healthCardNo;
	private String insuredName;
	private String address;
	private String registeredMobNo;
	private String insuredOffice;
	private String polProductName;
	private String sumInsured;
	private String telephoneNo;
	private Date dateOfIntimation;
	private Date endDate;
	
	private String tmpInsuredPolNo;
	
	private String tmpPolIssueCode;
	
	/**
	 * Added for processing add intimation hyper link
	 * in resultant table. This will not be displayed in 
	 * search result table.
	 * */
	private String productTypeId;
	private String policyProposerCode;
	private String policyType;
	
	private String lineOfBusiness;
	private String productCode;
	private String policyStatus;
	
	
	/**
	 * 
	 * view policy condition , view previous intimation , view add intimation.
	 * Under these we display hyper links. Hence DB info is not required here.
	 * */
	public String getPolNo() {
		return polNo;
	}
	public void setPolNo(String polNo) {
		this.polNo = polNo;
	}
	public String getHealthCardNo() {
		return healthCardNo;
	}
	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRegisteredMobNo() {
		return registeredMobNo;
	}
	public void setRegisteredMobNo(String registeredMobNo) {
		this.registeredMobNo = registeredMobNo;
	}
	public String getInsuredOffice() {
		return insuredOffice;
	}
	public void setInsuredOffice(String insuredOffice) {
		this.insuredOffice = insuredOffice;
	}
	public String getPolProductName() {
		return polProductName;
	}
	public void setPolProductName(String polProductName) {
		this.polProductName = polProductName;
	}
	public String getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}
	public String getTelephoneNo() {
		return telephoneNo;
	}
	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}
	public Date getDateOfIntimation() {
		return dateOfIntimation;
	}
	public void setDateOfIntimation(Date dateOfIntimation) {
		this.dateOfIntimation = dateOfIntimation;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getTmpInsuredPolNo() {
		return tmpInsuredPolNo;
	}
	public void setTmpInsuredPolNo(String tmpInsuredPolNo) {
		this.tmpInsuredPolNo = tmpInsuredPolNo;
	}
	public String getTmpPolIssueCode() {
		return tmpPolIssueCode;
	}
	public void setTmpPolIssueCode(String tmpPolIssueCode) {
		this.tmpPolIssueCode = tmpPolIssueCode;
	}
	public String getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}
	public String getPolAssrCode() {
		return policyProposerCode;
	}
	public void setPolAssrCode(String polAssrCode) {
		this.policyProposerCode = polAssrCode;
	}
	public String getPolicyProposerCode() {
		return policyProposerCode;
	}
	public void setPolicyProposerCode(String policyProposerCode) {
		this.policyProposerCode = policyProposerCode;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public String getLineOfBusiness() {
		return lineOfBusiness;
	}
	public void setLineOfBusiness(String lineOfBusiness) {
		this.lineOfBusiness = lineOfBusiness;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPolicyStatus() {
		return policyStatus;
	}
	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}


}
