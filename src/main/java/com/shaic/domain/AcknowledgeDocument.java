package com.shaic.domain;

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

@Entity
@Table(name="IMS_CLS_ACKNOWLEDGE_DOCUMENT")
@NamedQueries({
	@NamedQuery(name = "AcknowledgeDocument.findByDocAcknowledgementKey", query = "SELECT o FROM AcknowledgeDocument o where o.docAcknowledgement.key = :docAckKey and o.deleteFlag = 'N'"),
	@NamedQuery(name = "AcknowledgeDocument.findByKey", query = "SELECT o FROM AcknowledgeDocument o where o.key = :key and o.deleteFlag = 'N'"),
	@NamedQuery(name = "AcknowledgeDocument.findByClaimKey", query = "SELECT o FROM AcknowledgeDocument o where o.claimKey = :claimKey and o.deleteFlag = 'N'")
})


public class AcknowledgeDocument extends AbstractEntity{

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="IMS_CLS_ACKNOWLEDGE_DOCUMENT", sequenceName = "SEQ_ACKNOWLEDGE_DOC_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_ACKNOWLEDGE_DOCUMENT" )
	@Column(name="ACK_DOC_KEY")
	private Long key;
	
/*	@Column(name="ACKNOWLEDGEMENT_KEY")
	private Long ackKey;*/
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_ACKNOWLEDGEMENT_KEY", nullable = false,updatable=false)
	private DocAcknowledgement docAcknowledgement;
	
	@Column(name="CLAIM_KEY")
	private Long claimKey;
	
	
	
	@OneToOne
	@JoinColumn(name = "FILE_TYPE_ID", nullable = false)
	private MastersValue fileType;
	
	
	@Column(name="BILL_NUMBER")
	private String billNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BILL_DATE")
	private Date billDate;
	
	@Column(name="NUMBER_OF_ITEMS")
	private Long noOfItems;
	
	@Column(name="BILL_AMOUNT")
	private Double billAmount;
	
	@Column(name="DOCUMENT_TOKEN")
	private String docToken;	
	
	@Column(name="DELETED_FLAG")
	private String deleteFlag;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
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

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	

	public DocAcknowledgement getDocAcknowledgement() {
		return docAcknowledgement;
	}

	public void setDocAcknowledgement(DocAcknowledgement docAcknowledgement) {
		this.docAcknowledgement = docAcknowledgement;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
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

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public String getDocToken() {
		return docToken;
	}

	public void setDocToken(String docToken) {
		this.docToken = docToken;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
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

	public MastersValue getFileType() {
		return fileType;
	}

	public void setFileType(MastersValue fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((AcknowledgeDocument) obj).getKey());
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
