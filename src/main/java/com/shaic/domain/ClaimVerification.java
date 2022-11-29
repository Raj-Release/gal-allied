package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;

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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="IMS_CLS_CLAIM_VERIFICATION")
@NamedQueries({
     @NamedQuery(name="ClaimVerification.findAll", query="SELECT m FROM ClaimVerification m"),
     @NamedQuery(name="ClaimVerification.findByReimbursementKey",query="SELECT o FROM ClaimVerification o where o.reimbursement.key = :reimbursementKey")
                 
})
public class ClaimVerification  extends AbstractEntity implements Serializable  {
	private static final long serialVersionUID = -1133158643609360523L;
	@Id
	@Column(name="CLAIM_VERIFICATION_KEY")
	@SequenceGenerator(name="IMS_CLS_CLAIM_VERIFICATION_KEY_GENERATOR", sequenceName = "SEQ_CLAIM_VERIFICATION_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_CLAIM_VERIFICATION_KEY_GENERATOR" )
	private Long key;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@OneToOne
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private Reimbursement reimbursement;
	
	@Column(name="VERIFIED_FLAG")
	private String verifiedFlag;
	
	@Column(name="MEDICAL_REMARKS")
	private String medicalRemarks;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="VERIFICATION_TYPE")
	private String verificationType;
	
	@Column(name="VERIFICATION_TYPE_ID")
	private Long verificationTypeId;
	
	@Override
	public Long getKey() {
		return key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
		
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getVerifiedFlag() {
		return verifiedFlag;
	}

	public void setVerifiedFlag(String verifiedFlag) {
		this.verifiedFlag = verifiedFlag;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(String verificationType) {
		this.verificationType = verificationType;
	}

	public Long getVerificationTypeId() {
		return verificationTypeId;
	}

	public void setVerificationTypeId(Long verificationTypeId) {
		this.verificationTypeId = verificationTypeId;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((ClaimVerification) obj).getKey());
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
