package com.shaic.claim.lumen.create;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Insured;
import com.shaic.domain.MastersValue;

@SuppressWarnings("serial")
public class LumenPolicySearchResultTableDTO extends AbstractTableDTO {
	
	private Long policyKey;
	private String policyNumber;
	private int proposerCode;
	private String proposerName;
	private int noOfInsured;
	private String productName;
	private String policyIssuingOffice;
	private Date policyStartDate;
	private Date policyEndDate;
	private Double premiumAmount;
	private String homeOfficeCode;
	
	private List<Insured> listOfInsured;
	private MastersValue selectPolicyType;
	private MastersValue selectProductType;
	
	private String selectedInsuredName;
	
	private String policyType;
	private String productType;
	
	private String loginId;
	private String empName;
	
	private SelectValue lumenType;
	private SelectValue lumenErrorType;
	private String lumenRemarks;
	
	private DocumentDetails UserUploadedDocument;
	private List<DocumentDetails> listOfUserUploadedDocuments = new ArrayList<DocumentDetails>();
	
	public Long getPolicyKey() {
		return policyKey;
	}
	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public int getProposerCode() {
		return proposerCode;
	}
	public void setProposerCode(int proposerCode) {
		this.proposerCode = proposerCode;
	}
	public int getNoOfInsured() {
		return noOfInsured;
	}
	public void setNoOfInsured(int noOfInsured) {
		this.noOfInsured = noOfInsured;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPolicyIssuingOffice() {
		return policyIssuingOffice;
	}
	public void setPolicyIssuingOffice(String policyIssuingOffice) {
		this.policyIssuingOffice = policyIssuingOffice;
	}
	public Date getPolicyStartDate() {
		return policyStartDate;
	}
	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}
	public Date getPolicyEndDate() {
		return policyEndDate;
	}
	public void setPolicyEndDate(Date policyEndDate) {
		this.policyEndDate = policyEndDate;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public SelectValue getLumenType() {
		return lumenType;
	}
	public void setLumenType(SelectValue lumenType) {
		this.lumenType = lumenType;
	}
	public SelectValue getLumenErrorType() {
		return lumenErrorType;
	}
	public void setLumenErrorType(SelectValue lumenErrorType) {
		this.lumenErrorType = lumenErrorType;
	}
	public String getLumenRemarks() {
		return lumenRemarks;
	}
	public void setLumenRemarks(String lumenRemarks) {
		this.lumenRemarks = lumenRemarks;
	}
	public DocumentDetails getUserUploadedDocument() {
		return UserUploadedDocument;
	}
	public void setUserUploadedDocument(DocumentDetails userUploadedDocument) {
		UserUploadedDocument = userUploadedDocument;
	}
	public List<DocumentDetails> getListOfUserUploadedDocuments() {
		return listOfUserUploadedDocuments;
	}
	public void setListOfUserUploadedDocuments(
			List<DocumentDetails> listOfUserUploadedDocuments) {
		this.listOfUserUploadedDocuments = listOfUserUploadedDocuments;
	}
	public Double getPremiumAmount() {
		return premiumAmount;
	}
	public void setPremiumAmount(Double premiumAmount) {
		this.premiumAmount = premiumAmount;
	}
	public String getHomeOfficeCode() {
		return homeOfficeCode;
	}
	public void setHomeOfficeCode(String homeOfficeCode) {
		this.homeOfficeCode = homeOfficeCode;
	}
	public String getProposerName() {
		return proposerName;
	}
	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}
	public List<Insured> getListOfInsured() {
		return listOfInsured;
	}
	public void setListOfInsured(List<Insured> listOfInsured) {
		this.listOfInsured = listOfInsured;
	}
	public MastersValue getSelectPolicyType() {
		return selectPolicyType;
	}
	public void setSelectPolicyType(MastersValue selectPolicyType) {
		this.selectPolicyType = selectPolicyType;
	}
	public MastersValue getSelectProductType() {
		return selectProductType;
	}
	public void setSelectProductType(MastersValue selectProductType) {
		this.selectProductType = selectProductType;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getSelectedInsuredName() {
		return selectedInsuredName;
	}
	public void setSelectedInsuredName(String selectedInsuredName) {
		this.selectedInsuredName = selectedInsuredName;
	}
	
}
