package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="IMS_CLSB_LEGAL_HEIR_DTLS")
@NamedQueries({
	@NamedQuery(name = "LegalHeir.findByTransactionKey",  query = "SELECT i FROM LegalHeir i WHERE i.rodKey = :transacKey and activeStatus = 'Y'")
	})
public class LegalHeir implements Serializable{
	
	@Id
	@SequenceGenerator(name="SEQB_LEGAL_HEIR_KEY_GENERATOR", sequenceName="SEQB_LEGAL_HEIR_KEY",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "SEQB_LEGAL_HEIR_KEY_GENERATOR")
	@Column(name="LEGAL_HEIR_KEY")
	private Long key;
	
	@Column(name="POLICY_KEY")
	private Long policyKey;
	
	@Column(name="INSURED_KEY")
	private Long insuredKey;
	
	@Column(name="TRANSACTION_KEY")
	private Long rodKey;
	
	@Column(name="LEGAL_HEIR_NAME")
	private String legalHeirName;
	
	@Column(name="RELATION_CODE")
	private Long relationCode;
	
	@Column(name="RELATION_DESC")
	private String relationDesc;
	
	@Column(name="SHARE_PERCENTAGE")
	private Double sharePercentage;
	
	@Column(name="ADDRESS_1")
	private String address;
	
	@Column(name="ACCOUNT_TYPE")
	private String accountType;
	
	@Column(name="ACCOUNT_PREFERENCE")
	private String accountPreference;
	
	@Column(name="BENEFIARY_NAME")
	private String beneficiaryName;
	
	@Column(name="BANK_NAME")
	private String bankName;
	
	@Column(name="BRANCH_NAME")
	private String bankBranchName;
	
	@Column(name="PAYABLE_AT")
	private String payableAt;
	
	@Column(name="ACCOUNT_NO")
	private Long accountNo;
	
	@Column(name="IFSC_CODE")
	private String ifscCode;
	
	@Column(name="DOC_KEY")
	private Long docKey;
	
	@Column(name="UPLOAD_FLAG")
	private String uploadFlag;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")  
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")  
	private Date modifiedDate;
	
	@Column(name="BI_DATE")
	private Date biDate;
	
	@Column(name="PINCODE")
	private String pincode;
	
	@Column(name="PAYMENT_MODE_ID")         
	private Long paymentModeId;
	
	@Column(name="PAYMODE_CHANGE_REASON")
	private Long paymentModeChangeReason;
	
	@Column (name="ADDRESS_2")
    private String address_2;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public String getLegalHeirName() {
		return legalHeirName;
	}

	public void setLegalHeirName(String legalHeirName) {
		this.legalHeirName = legalHeirName;
	}

	public Long getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(Long relationCode) {
		this.relationCode = relationCode;
	}

	public String getRelationDesc() {
		return relationDesc;
	}

	public void setRelationDesc(String relationDesc) {
		this.relationDesc = relationDesc;
	}

	public Double getSharePercentage() {
		return sharePercentage;
	}

	public void setSharePercentage(Double sharePercentage) {
		this.sharePercentage = sharePercentage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(Long accountNo) {
		this.accountNo = accountNo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public Long getDocKey() {
		return docKey;
	}

	public void setDocKey(Long docKey) {
		this.docKey = docKey;
	}

	public String getUploadFlag() {
		return uploadFlag;
	}

	public void setUploadFlag(String uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
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

	public Date getBiDate() {
		return biDate;
	}

	public void setBiDate(Date biDate) {
		this.biDate = biDate;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public Long getPaymentModeChangeReason() {
		return paymentModeChangeReason;
	}

	public void setPaymentModeChangeReason(Long paymentModeChangeReason) {
		this.paymentModeChangeReason = paymentModeChangeReason;
	}

	public String getAccountPreference() {
		return accountPreference;
	}

	public void setAccountPreference(String accountPreference) {
		this.accountPreference = accountPreference;
	}

	public String getAddress_2() {
		return address_2;
	}

	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
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
	
}