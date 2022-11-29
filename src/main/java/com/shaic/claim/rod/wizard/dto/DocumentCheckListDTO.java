/**
 * 
 */
package com.shaic.claim.rod.wizard.dto;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.vijayar
 *
 */
public class DocumentCheckListDTO extends AbstractTableDTO  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String slNo;
	
	private Long key;

	private Long sequenceNumber;
	
	private String value;
	
	private String mandatoryDocFlag;
	
	private String requiredDocType;
	
	private SelectValue activeStatus;
	
	private String createdBy;
	
	private String modifiedBy;
	
	private Date createdDate;
	
	private Date modifiedDate;
	
	private SelectValue receivedStatus;
	
	private String strReceivedStatus;
	
	private Long noOfDocuments;
	
	private String remarks;
	
	//This needs to be changed to checkbox.
	private Boolean rodReceivedStatus;
	
	private String rodReceivedStatusFlag;
	
	private String rodRemarks;
	
	private Long docAckTableKey;
	
	private Long docTypeId;
	
	private Long docChkLstKey;
	
	
	private String docType;
	
	private String ackReceivedStatus;
	
	private String particularsValue;
		
	private SelectValue particulars;
	
	private String benefitId;

	public String getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = ""+slNo;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getMandatoryDocFlag() {
		return mandatoryDocFlag;
	}

	public void setMandatoryDocFlag(String mandatoryDocFlag) {
		this.mandatoryDocFlag = mandatoryDocFlag;
	}

	public String getRequiredDocType() {
		return requiredDocType;
	}

	public void setRequiredDocType(String requiredDocType) {
		this.requiredDocType = requiredDocType;
	}

	public SelectValue getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(SelectValue activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public SelectValue getReceivedStatus() {
		return receivedStatus;
	}

	public void setReceivedStatus(SelectValue receivedStatus) {
		this.receivedStatus = receivedStatus;
	}

	public Long getNoOfDocuments() {
		return noOfDocuments;
	}

	public void setNoOfDocuments(Long noOfDocuments) {
		this.noOfDocuments = noOfDocuments;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the rodReceivedStatus
	 */
	public Boolean getRodReceivedStatus() {
		return rodReceivedStatus;
	}

	/**
	 * @param rodReceivedStatus the rodReceivedStatus to set
	 */
	public void setRodReceivedStatus(Boolean rodReceivedStatus) {
		this.rodReceivedStatus = rodReceivedStatus;
		//this.addOnBenefitsHospitalCash = addOnBenefitsHospitalCash;
		this.rodReceivedStatusFlag = this.rodReceivedStatus != null && rodReceivedStatus ? "Y" : "N" ;
	}

	/**
	 * @return the rodReceivedStatusFlag
	 */
	public String getRodReceivedStatusFlag() {
		return rodReceivedStatusFlag;
	}

	/**
	 * @param rodReceivedStatusFlag the rodReceivedStatusFlag to set
	 */
	public void setRodReceivedStatusFlag(String rodReceivedStatusFlag) {
		this.rodReceivedStatusFlag = rodReceivedStatusFlag;
	//	this.addOnBenefitsHospitalCashFlag = addOnBenefitsHospitalCashFlag;
		if(this.rodReceivedStatusFlag != null && this.rodReceivedStatusFlag.equalsIgnoreCase("Y")) {
			this.rodReceivedStatus = true;
		}
	}

	/**
	 * @return the rodRemarks
	 */
	public String getRodRemarks() {
		return rodRemarks;
	}

	/**
	 * @param rodRemarks the rodRemarks to set
	 */
	public void setRodRemarks(String rodRemarks) {
		this.rodRemarks = rodRemarks;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the docAckTableKey
	 */
	public Long getDocAckTableKey() {
		return docAckTableKey;
	}

	/**
	 * @param docAckTableKey the docAckTableKey to set
	 */
	public void setDocAckTableKey(Long docAckTableKey) {
		this.docAckTableKey = docAckTableKey;
	}

	/**
	 * @return the ackReceivedStatus
	 */
	public String getAckReceivedStatus() {
		return ackReceivedStatus;
	}

	/**
	 * @param ackReceivedStatus the ackReceivedStatus to set
	 */
	public void setAckReceivedStatus(String ackReceivedStatus) {
		this.ackReceivedStatus = ackReceivedStatus;
	}

	/**
	 * @return the docTypeId
	 */
	public Long getDocTypeId() {
		return docTypeId;
	}

	/**
	 * @param string the docTypeId to set
	 */
	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	/**
	 * @return the docChkLstKey
	 */
	public Long getDocChkLstKey() {
		return docChkLstKey;
	}

	/**
	 * @param docChkLstKey the docChkLstKey to set
	 */
	public void setDocChkLstKey(Long docChkLstKey) {
		this.docChkLstKey = docChkLstKey;
	}

	public String getStrReceivedStatus() {
		return strReceivedStatus;
	}

	public void setStrReceivedStatus(String strReceivedStatus) {
		this.strReceivedStatus = strReceivedStatus;
	}

	
	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}



	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SelectValue getParticulars() {
		return particulars;
	}

	public void setParticulars(SelectValue particulars) {
		this.particulars = particulars;
	}

	public String getParticularsValue() {
		return particularsValue;
	}

	public void setParticularsValue(String particularsValue) {
		this.particularsValue = particularsValue;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

}
