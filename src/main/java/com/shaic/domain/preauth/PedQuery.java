package com.shaic.domain.preauth;

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
import com.shaic.domain.Status;

/**
 * Entity implementation class for Entity: PedQuery
 *
 */
@Entity
@Table(name="IMS_CLS_PED_QUERY")
@NamedQueries({
	@NamedQuery(name="PedQuery.findAll", query="SELECT m FROM PedQuery m"),
	@NamedQuery(name="PedQuery.findByPedInitiateKey",query="SELECT m FROM PedQuery m where m.newInitiatePedEndorsement.key=:initiatekey"),
	@NamedQuery(name="PedQuery.findByKey",query="SELECT m FROM PedQuery m where m.key=:primaryKey")
})
public class PedQuery extends AbstractEntity{

	
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="IMS_CLS_PED_QUERY_KEY_GENERATOR", sequenceName = "SEQ_PED_QUERY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PED_QUERY_KEY_GENERATOR" ) 
	@Column(name="PED_QUERY_KEY")
	private Long key;
	
	@OneToOne	
	@JoinColumn(name="PED_INITIATE_KEY",nullable=false)
    private OldInitiatePedEndorsement newInitiatePedEndorsement;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@Column(name="OFFICE_CODE")
	private String officeCode;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="STATUS_DATE")
//	private Date statusDate;
//	
//	@Column(name="VERSION")
//	private Long version;
//	
//	@Column(name="SEQUENCE_NUMBER")
//	private Long sequenceNumber;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="INITIATOR_TYPE")
	private String initiatorType;

	@Column(name="QUERY_REMARKS")
	private String queryRemarks;

	@Column(name="REPLY_REMARKS")
	private String replyRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")  
	private Date modifiedDate;
	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="ACTIVE_STATUS_DATE")
//	private Date activeStatusDate;
//	
//	@Column(name="SUB_STATUS_ID")
//	private Long subStringId;
	
	public PedQuery() {
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
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

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
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

	public String getInitiatorType() {
		return initiatorType;
	}

	public void setInitiatorType(String initiatorType) {
		this.initiatorType = initiatorType;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getReplyRemarks() {
		return replyRemarks;
	}

	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public OldInitiatePedEndorsement getNewInitiatePedEndorsement() {
		return newInitiatePedEndorsement;
	}

	public void setNewInitiatePedEndorsement(
			OldInitiatePedEndorsement newInitiatePedEndorsement) {
		this.newInitiatePedEndorsement = newInitiatePedEndorsement;
	}
   
}
