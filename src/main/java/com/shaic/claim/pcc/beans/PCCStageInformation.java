package com.shaic.claim.pcc.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;

@Entity
@Table(name = "IMS_CLS_PCC_STAGE_INFORMATION")
@NamedQueries({
	@NamedQuery(name = "PCCStageInformation.findAll", query = "SELECT i FROM PCCStageInformation i"),
	@NamedQuery(name="PCCStageInformation.findEmpIdsByIntimationNo", query="SELECT s FROM PCCStageInformation s where s.intimation.intimationId = :intimationId ORDER BY s.createdDate ASC"),
	@NamedQuery(name ="PCCStageInformation.findByStatusIds",query="SELECT r FROM PCCStageInformation r WHERE r.intimation.intimationId = :intimationId and r.status.key in (:statusList) ORDER BY r.createdDate ASC"),

})
public class PCCStageInformation extends AbstractEntity{
	
	@Id
	@Column(name="STAGE_INFORMATION_KEY")
	private Long key;

	@OneToOne
	@JoinColumn(name = "INTIMATION_KEY", nullable = false)
	private Intimation intimation;

	@ManyToOne(fetch=FetchType.EAGER )
	@JoinColumn(name="CLAIM_TYPE_ID",  nullable=true)
	private MastersValue claimType;

	@Column(name = "ACTIVE_STATUS")
	private String activeStatus;

	@OneToOne
	@JoinColumn(name = "STAGE_ID", nullable = false)
	private Stage stage;

	@OneToOne
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status status;
	
	@Column(name = "STATUS_REMARKS")
	private String statusRemarks;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PCC_CATEGORY_KEY", nullable = false)
	private PCCCategory pccCategory;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Intimation getIntimation() {
		return intimation;
	}

	public void setIntimation(Intimation intimation) {
		this.intimation = intimation;
	}

	public MastersValue getClaimType() {
		return claimType;
	}

	public void setClaimType(MastersValue claimType) {
		this.claimType = claimType;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getStatusRemarks() {
		return statusRemarks;
	}

	public void setStatusRemarks(String statusRemarks) {
		this.statusRemarks = statusRemarks;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public PCCCategory getPccCategory() {
		return pccCategory;
	}

	public void setPccCategory(PCCCategory pccCategory) {
		this.pccCategory = pccCategory;
	}
	

}
