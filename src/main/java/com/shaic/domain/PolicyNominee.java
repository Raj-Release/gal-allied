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
@Table(name="IMS_CLS_POLICY_NOMINEE")
@NamedQueries( {
@NamedQuery(name="PolicyNominee.findAll", query="SELECT n FROM PolicyNominee n WHERE n.activeStatus = 1"),
@NamedQuery(name="PolicyNominee.findByKey", query="SELECT n FROM PolicyNominee n WHERE n.key = :nomineeKey AND n.activeStatus = 1"),
@NamedQuery(name="PolicyNominee.findByPolicy", query="SELECT n FROM PolicyNominee n WHERE n.policy.key = :policyKey AND n.activeStatus = 1"),
@NamedQuery(name="PolicyNominee.findByInsuredKey", query="SELECT o FROM PolicyNominee o where o.insured.key =:insuredKey AND o.activeStatus = 1")
})

public class PolicyNominee extends AbstractEntity implements Serializable {

	
	@Id
	@SequenceGenerator(name="IMS_CLS_POLICY_NOMINEE_KEY_GENERATOR", sequenceName = "SEQ_POLICY_NOMINEE_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_POLICY_NOMINEE_KEY_GENERATOR" ) 
	@Column(name="POLICY_NOMINEE_KEY")	
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="POLICY_KEY")    
	private Policy policy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="INSURED_KEY")
	private Insured insured;

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
	
	@Column(name= "IFSC_CODE")
	private String IFSCcode;
	
	@Column(name="BANK_ACC_NO")
	private String accountNumber;
	
	@Column(name="BENEFICIARY_NAME")
	private String nameAsPerBank;
	
	
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

	public Insured getInsured() {
		return insured;
	}

	public void setInsured(Insured insured) {
		this.insured = insured;
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

	public String getNomineeCode() {
		return nomineeCode;
	}

	public void setNomineeCode(String nomineeCode) {
		this.nomineeCode = nomineeCode;
	}

	public String getIFSCcode() {
		return IFSCcode;
	}

	public void setIFSCcode(String iFSCcode) {
		IFSCcode = iFSCcode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getNameAsPerBank() {
		return nameAsPerBank;
	}

	public void setNameAsPerBank(String nameAsPerBank) {
		this.nameAsPerBank = nameAsPerBank;
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
            return getKey().equals(((PolicyNominee) obj).getKey());
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
