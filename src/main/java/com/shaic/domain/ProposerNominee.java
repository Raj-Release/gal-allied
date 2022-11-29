package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_PROPOSER_NOMINEE")
@NamedQueries( {
@NamedQuery(name="ProposerNominee.findAll", query="SELECT n FROM ProposerNominee n WHERE n.activeStatus = 1"),
@NamedQuery(name="ProposerNominee.findByKey", query="SELECT n FROM ProposerNominee n WHERE n.key = :nomineeKey AND n.activeStatus = 1"),
@NamedQuery(name="ProposerNominee.findByPolicy", query="SELECT n FROM ProposerNominee n WHERE n.policy.key = :policyKey AND n.activeStatus = 1"),
@NamedQuery(name="ProposerNominee.findByInsuredKey", query="SELECT o FROM ProposerNominee o where o.insured.key = :insuredKey AND o.activeStatus = 1"),
@NamedQuery(name="ProposerNominee.findByTransacKey", query="SELECT n FROM ProposerNominee n WHERE n.transactionKey = :transacKey AND n.activeStatus = 1"),
})
public class ProposerNominee extends AbstractEntity implements Serializable {

	
	@Id
	@SequenceGenerator(name="IMS_CLS_PRO_NOMINEE_KEY_GENERATOR", sequenceName = "SEQ_PRO_NOMINEE_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PRO_NOMINEE_KEY_GENERATOR" ) 
	@Column(name="PRO_NOMINEE_KEY")	
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY")    
	private Policy policy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INSURED_KEY")
	private Insured insured;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_NOMINEE_KEY")
	private PolicyNominee policyNominee ;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INTIMATION_KEY")
	private Intimation intimation;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY")    
	private Claim claim;
	

	@Column(name="TRANSACTION_KEY")
	private Long transactionKey;
	
	@Column(name="TRANSACTION_TYPE")  
	private String transactionType;
	
	@Column(name="NOMINEE_NAME")
	private String nomineeName;
	
	@Column(name="NOMINEEDOB")          
	private Date nomineeDob;
	
	@Column(name="NOMINEE_AGE")    
	private Integer nomineeAge;
	
	@Column(name="NOMINEE_RELATIONSHIP") 
	private String relationshipWithProposer;
	
	@Column(name="NOMINEE_PERCENTAGE")  
	private Double sharePercent;
	
	@Column(name="APPOINTEE_NAME") 
	private String appointeeName;
	
	@Column(name="APPOINTEE_AGE")    
	private Integer appointeeAge;
	
	@Column(name="APPOINTEE_RELATIONSHIP") 
	private String appointeeRelationship;
	
	@Column(name="SELECTED_FLAG")   
	private String selectedFlag;
	
	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;

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
	
	@Column(name="NOMINEE_CODE")
	private String nomineeCode;
	
	@Column(name="IFSC_CODE")
	private String ifscCode;
	
	@Column(name="NOMINEE_BANK_NAME")
	private String bankName;
	
	@Column(name="NOMINEE_BRANCH_NAME")
	private String bankBranchName;
	
	@Column(name="NAME_AS_PER_BANK_ACC")
	private String nameAsperBankAcc;
	
	@Column(name="PAN_NUMBER")
	private String panNumber;
	
	@Column(name="MICR_CODE")
	private String micrCode;
	
	@Column(name="VIRTUAL_PAYMENT_ADDRESS")
	private String virtualPaymentAddress;
	
	@Column(name="EMAIL_ID")
	private String emailId;	

	@Column(name="ACCOUNT_TYPE")
	private String accType;
	
	@Column(name="ACCOUNT_PREFERENCE")
	private String accPreference;
	
	@Column(name="ACCOUNT_NUMBER")
	private String accNumber;
	
	@Column(name="BANK_ID")
	private Long bankId;
	
	@Column(name="PAYMENT_MODE_ID")
	private Long paymentModeId;
	
	@Column(name="PAYABLE_AT")
	private String payableAt;
		
	@Transient
	private String strNomineeDOB;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Insured getInsured() {
		return insured;
	}

	public void setInsured(Insured insured) {
		this.insured = insured;
	}

	public Long getTransactionKey() {
		return transactionKey;
	}

	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public Date getNomineeDob() {
		return nomineeDob;
	}

	public void setNomineeDob(Date nomineeDob) {
		this.nomineeDob = nomineeDob;
	}

	public Integer getNomineeAge() {
		return nomineeAge;
	}

	public void setNomineeAge(Integer nomineeAge) {
		this.nomineeAge = nomineeAge;
	}

	public String getRelationshipWithProposer() {
		return relationshipWithProposer;
	}

	public void setRelationshipWithProposer(String relationshipWithProposer) {
		this.relationshipWithProposer = relationshipWithProposer;
	}

	public Double getSharePercent() {
		return sharePercent;
	}

	public void setSharePercent(Double sharePercent) {
		this.sharePercent = sharePercent;
	}

	public String getAppointeeName() {
		return appointeeName;
	}

	public void setAppointeeName(String appointeeName) {
		this.appointeeName = appointeeName;
	}

	public Integer getAppointeeAge() {
		return appointeeAge;
	}

	public void setAppointeeAge(Integer appointeeAge) {
		this.appointeeAge = appointeeAge;
	}

	public String getAppointeeRelationship() {
		return appointeeRelationship;
	}

	public void setAppointeeRelationship(String appointeeRelationship) {
		this.appointeeRelationship = appointeeRelationship;
	}

	public String getSelectedFlag() {
		return selectedFlag;
	}

	public void setSelectedFlag(String selectedFlag) {
		this.selectedFlag = selectedFlag;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}		
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getStrNomineeDOB() {
		return strNomineeDOB;
	}

	public void setStrNomineeDOB(String strNomineeDOB) {
		this.strNomineeDOB = strNomineeDOB;
	}

	public PolicyNominee getPolicyNominee() {
		return policyNominee;
	}

	public void setPolicyNominee(PolicyNominee policyNominee) {
		this.policyNominee = policyNominee;
	}	
	
	public String getNomineeCode() {
		return nomineeCode;
	}

	public void setNomineeCode(String nomineeCode) {
		this.nomineeCode = nomineeCode;
	}

	public String getNameAsperBankAcc() {
		return nameAsperBankAcc;
	}

	public void setNameAsperBankAcc(String nameAsperBankAcc) {
		this.nameAsperBankAcc = nameAsperBankAcc;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getMicrCode() {
		return micrCode;
	}

	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}

	public String getVirtualPaymentAddress() {
		return virtualPaymentAddress;
	}

	public void setVirtualPaymentAddress(String virtualPaymentAddress) {
		this.virtualPaymentAddress = virtualPaymentAddress;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}	
	
	public String getAccPreference() {
		return accPreference;
	}

	public void setAccPreference(String accPreference) {
		this.accPreference = accPreference;
	}

	public String getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public String getPayableAt() {
		return payableAt;
	}

	public void setPayableAt(String payableAt) {
		this.payableAt = payableAt;
	}	
	
	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}
	
	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	@Override
	public String toString() {
		System.out.println("----------------------- " + this.policy.getKey() + " : " + this.policy.getPolicyNumber());
		return super.toString();
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((ProposerNominee) obj).getKey());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }
}