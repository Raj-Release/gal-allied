package com.shaic.claim.lumen.create;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Insured;
import com.shaic.domain.Policy;

@SuppressWarnings("serial")
public class LumenSearchResultTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String intimationNumber;
	private Long intimationKey;
	private String cpuCode;
	private String cpuDesc;
	private String policyNumber;
	private String productName;
	private String insuredPatientName;
	private String hospitalName;
	private Long hospitalNameId;
	private SelectValue hospitalType;
	private String reasonForAdmission;
	
	private Policy policy;
	private SelectValue policyType;
	private SelectValue productType;
	private Insured policyInsured;
	private String policyIssuingOffice;
	private SelectValue lob;
	private Claim claim;
	
	private String loginId;
	private String empName;
	
	private SelectValue lumenType;
	private SelectValue lumenErrorType;
	private String lumenRemarks;
	
	private DocumentDetails UserUploadedDocument;
	private List<DocumentDetails> listOfUserUploadedDocuments = new ArrayList<DocumentDetails>();
	
	private Long specialistStage;
	
	private String insuredDeceasedFlag; 
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public SelectValue getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public Long getHospitalNameId() {
		return hospitalNameId;
	}
	public void setHospitalNameId(Long hospitalNameId) {
		this.hospitalNameId = hospitalNameId;
	}
	public String getCpuDesc() {
		if(StringUtils.isNotBlank(cpuCode)){
			return cpuCode+"-"+cpuDesc;
		}else{
			if(StringUtils.isNotBlank(cpuDesc)){
				return cpuDesc;
			}else{
				return "";
			}
		}
	}
	public void setCpuDesc(String cpuDesc) {
		this.cpuDesc = cpuDesc;
	}
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	public SelectValue getPolicyType() {
		return policyType;
	}
	public void setPolicyType(SelectValue policyType) {
		this.policyType = policyType;
	}
	public SelectValue getProductType() {
		return productType;
	}
	public void setProductType(SelectValue productType) {
		this.productType = productType;
	}
	public Insured getPolicyInsured() {
		return policyInsured;
	}
	public void setPolicyInsured(Insured policyInsured) {
		this.policyInsured = policyInsured;
	}
	public String getPolicyIssuingOffice() {
		return policyIssuingOffice;
	}
	public void setPolicyIssuingOffice(String policyIssuingOffice) {
		this.policyIssuingOffice = policyIssuingOffice;
	}
	public SelectValue getLob() {
		return lob;
	}
	public void setLob(SelectValue lob) {
		this.lob = lob;
	}
	public Claim getClaim() {
		return claim;
	}
	public void setClaim(Claim claim) {
		this.claim = claim;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
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
	public List<DocumentDetails> getListOfUserUploadedDocuments() {
		return listOfUserUploadedDocuments;
	}
	public void setListOfUserUploadedDocuments(
			List<DocumentDetails> listOfUserUploadedDocuments) {
		this.listOfUserUploadedDocuments = listOfUserUploadedDocuments;
	}
	public DocumentDetails getUserUploadedDocument() {
		return UserUploadedDocument;
	}
	public void setUserUploadedDocument(DocumentDetails userUploadedDocument) {
		UserUploadedDocument = userUploadedDocument;
	}
	public Long getSpecialistStage() {
		return specialistStage;
	}
	public void setSpecialistStage(Long specialistStage) {
		this.specialistStage = specialistStage;
	}
	public String getInsuredDeceasedFlag() {
		return insuredDeceasedFlag;
	}
	public void setInsuredDeceasedFlag(String insuredDeceasedFlag) {
		this.insuredDeceasedFlag = insuredDeceasedFlag;
	}
	
}
