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

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author karthikeyan.r
 * The persistent class for the IMS_CLS_LUMEN_ACK_DOCUMENT database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name="IMS_CLS_LUMEN_ACK_DOCUMENT")
@NamedQueries({
	@NamedQuery(name="LumenQueryDocument.findAll", query="SELECT m FROM LumenQueryDocument m"),
	@NamedQuery(name="LumenQueryDocument.findByLumenKey", query="SELECT m FROM LumenQueryDocument m where m.lumenRequest.key = :lumenReqKey and m.lumenQueryDetails.key in (:qryDetailsList) and m.deletedFlag <> 'Y' ")
})
public class LumenQueryDocument extends AbstractEntity implements Serializable  {

	@Id
	@SequenceGenerator(name="IMS_CLS_LUMEN_ACK_GENERATOR", sequenceName = "SEQ_LMN_ACK_DOC_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LUMEN_ACK_GENERATOR" ) 
	@Column(name="LMN_ACK_DOC_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LUMEN_REQUEST_KEY", nullable=false)
	private LumenRequest lumenRequest;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LUMEN_QUERY_KEY", nullable=false)
	private LumenQuery lumenQuery;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LUMEN_QUERY_DTLS_KEY", nullable=false)
	private LumenQueryDetails lumenQueryDetails;
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name = "FILE_TYPE_ID")
	private Long fileTypeId;	
	
	@Column(name="BILL_NUMBER")
	private String billNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BILL_DATE")
	private Date billDate;
	
	@Column(name="NUMBER_OF_ITEMS")
	private Long noOfItems;
	
	@Column(name="BILL_AMOUNT")
	private Long billAmount;
	
	@Column(name = "DOCUMENT_TOKEN")
	private Long documentToken;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
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
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}

	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileTypeId() {
		return fileTypeId;
	}

	public void setFileTypeId(Long fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Long getNoOfItems() {
		return noOfItems;
	}

	public void setNoOfItems(Long noOfItems) {
		this.noOfItems = noOfItems;
	}

	public Long getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Long billAmount) {
		this.billAmount = billAmount;
	}

	public Long getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
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

	public LumenQuery getLumenQuery() {
		return lumenQuery;
	}

	public void setLumenQuery(LumenQuery lumenQuery) {
		this.lumenQuery = lumenQuery;
	}

	public LumenQueryDetails getLumenQueryDetails() {
		return lumenQueryDetails;
	}

	public void setLumenQueryDetails(LumenQueryDetails lumenQueryDetails) {
		this.lumenQueryDetails = lumenQueryDetails;
	}
}
