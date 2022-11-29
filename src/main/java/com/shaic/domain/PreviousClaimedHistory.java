package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_PREV_CLAIMED_HISTORY")
@NamedQueries({
@NamedQuery(name="PreviousClaimedHistory.findAll", query="SELECT r FROM PreviousClaimedHistory r"),
@NamedQuery(name="PreviousClaimedHistory.findByReimbursementKey", query="SELECT r FROM PreviousClaimedHistory r WHERE r.reimbursement is not null and r.reimbursement.key = :reimbursementKey")
})
public class PreviousClaimedHistory extends AbstractEntity {

	private static final long serialVersionUID = -3849419574963377958L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_PREV_CLAIMED_HISTORY_KEY_GENERATOR", sequenceName = "SEQ_PREV_CLAIMED_HISTORY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PREV_CLAIMED_HISTORY_KEY_GENERATOR" )
	@Column(name="PREV_CLAIMED_HISTORY_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private Reimbursement reimbursement;
	
	@Column(name="COMPANY_NAME")
	private String companyName;
	
	@Temporal(TemporalType.DATE)
	@Column(name="COMMENCEMENT_DATE")
	private Date commencementDate; 
	
	@Column(name="POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name="SUM_INSURED")
	private Double sumInsured;
	
	@Column(name="HOSPITALIZED_FLAG")
	private String hospitalizationFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getCommencementDate() {
		return commencementDate;
	}

	public void setCommencementDate(Date commencementDate) {
		this.commencementDate = commencementDate;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}

	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}
}
