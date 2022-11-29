/**
 * 
 */
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
import com.shaic.domain.preauth.Stage;

/**
 * @author ntv.vijayar
 * The persistent class for the IMS_CLS_DOC_ACKNOWLEDGEMENT database table.
 * 
 */
@Entity
@Table(name = "IMS_CLS_ROD_DOCUMENT_LIST")
@NamedQueries({
	@NamedQuery(name = "RODDocumentCheckList.findAll", query = "SELECT i FROM RODDocumentCheckList i"),
	@NamedQuery(name = "RODDocumentCheckList.findByKey", query = "SELECT o FROM RODDocumentCheckList o where o.key = :docListKey and o.docTypeId is not null"),
	@NamedQuery(name = "RODDocumentCheckList.findByDocAck", query = "SELECT o FROM RODDocumentCheckList o where o.docAcknowledgement.key = :docAck and o.docTypeId is not null"),
	@NamedQuery(name = "RODDocumentCheckList.findByDocKey", query = "SELECT o FROM RODDocumentCheckList o where o.docAcknowledgement.key = :docKey and o.docTypeId is not null")
})
public class RODDocumentCheckList  extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name="IMS_CLS_ROD_DOCUMENT_LIST_KEY_GENERATOR", sequenceName = "SEQ_ROD_DOCUMENT_LIST_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_ROD_DOCUMENT_LIST_KEY_GENERATOR" ) 
	@Column(name="DOCUMENT_LIST_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="DOC_ACKNOWLEDGEMENT_KEY")
	private DocAcknowledgement docAcknowledgement;
	
	@Column(name = "DOCUMENT_TYPE_ID")
	private Long docTypeId;
	
	@OneToOne
	@JoinColumn(name="RECEIVED_STATUS_ID")
	private MastersValue receivedStatusId;
	
	@Column(name = "NUMBER_OF_DOCUMENTS")
	private Long noOfDocuments;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "ROD_RECEIVED_STATUS")
	private String rodReceivedStatus;
	
	@Column(name = "ROD_REMARKS")
	private String rodRemarks;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "OFFICE_CODE")
	private String officeCode;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	/**
	 * @return the key
	 */
	public Long getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Long key) {
		this.key = key;
	}

	/**
	 * @return the docAcknowledgement
	 */
	public DocAcknowledgement getDocAcknowledgement() {
		return docAcknowledgement;
	}

	/**
	 * @param docAcknowledgement the docAcknowledgement to set
	 */
	public void setDocAcknowledgement(DocAcknowledgement docAcknowledgement) {
		this.docAcknowledgement = docAcknowledgement;
	}

	


	/**
	 * @return the noOfDocuments
	 */
	public Long getNoOfDocuments() {
		return noOfDocuments;
	}

	/**
	 * @param noOfDocuments the noOfDocuments to set
	 */
	public void setNoOfDocuments(Long noOfDocuments) {
		this.noOfDocuments = noOfDocuments;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the rodReceivedStatus
	 */
	public String getRodReceivedStatus() {
		return rodReceivedStatus;
	}

	/**
	 * @param rodReceivedStatus the rodReceivedStatus to set
	 */
	public void setRodReceivedStatus(String rodReceivedStatus) {
		this.rodReceivedStatus = rodReceivedStatus;
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
	 * @return the activeStatus
	 */
	public Long getActiveStatus() {
		return activeStatus;
	}

	/**
	 * @param activeStatus the activeStatus to set
	 */
	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	/**
	 * @return the officeCode
	 */
	public String getOfficeCode() {
		return officeCode;
	}

	/**
	 * @param officeCode the officeCode to set
	 */
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the documentTypeID
	 */
	public Long getDocumentTypeId() {
		return docTypeId;
	}

	/**
	 * @param documentTypeID the documentTypeID to set
	 */
	public void setDocumentTypeId(Long documentTypeId) {
		this.docTypeId = documentTypeId;
	}

	/**
	 * @return the receivedStatusId
	 */
	public MastersValue getReceivedStatusId() {
		return receivedStatusId;
	}

	/**
	 * @param receivedStatusId the receivedStatusId to set
	 */
	public void setReceivedStatusId(MastersValue receivedStatusId) {
		this.receivedStatusId = receivedStatusId;
	}

}
