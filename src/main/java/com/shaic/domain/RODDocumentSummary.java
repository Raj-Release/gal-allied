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

/**
 * @author ntv.vijayar
 *
 */

@Entity
@Table(name="IMS_CLS_ROD_DOCUMENT_SUMMARY")
@NamedQueries({
@NamedQuery(name="RODDocumentSummary.findAll", query="SELECT r FROM RODDocumentSummary r"),
@NamedQuery(name ="RODDocumentSummary.findByKey",query="SELECT r FROM RODDocumentSummary r WHERE r.key = :primaryKey"),
@NamedQuery(name="RODDocumentSummary.findByReimbursementKey", query= "SELECT r FROM RODDocumentSummary r WHERE r.reimbursement is not null and r.reimbursement.key = :reimbursementKey and (r.deletedFlag = 'N' OR r.deletedFlag is NULL)"),
@NamedQuery(name="RODDocumentSummary.findByDocToken", query= "SELECT r FROM RODDocumentSummary r WHERE r.documentToken is not null and r.documentToken = :documentToken and (r.deletedFlag = 'N' OR r.deletedFlag is NULL)"),
@NamedQuery(name="RODDocumentSummary.findByReimbKeyFileType", query= "SELECT r FROM RODDocumentSummary r WHERE r.fileType.key = :fileType and r.reimbursement is not null and r.reimbursement.key = :reimbursementKey and (r.deletedFlag = 'N' OR r.deletedFlag is NULL)")

})

public class RODDocumentSummary extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@Id
	@SequenceGenerator(name="IMS_ROD_DOCUMENT_KEY_GENERATOR", sequenceName = "SEQ_ROD_DOCUMENT_SUMMARY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_ROD_DOCUMENT_KEY_GENERATOR" )
	@Column(name="DOCUMENT_SUMMARY_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private Reimbursement reimbursement;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@OneToOne
	@JoinColumn(name = "FILE_TYPE_ID", nullable = false)
	private MastersValue fileType;
	
	@Column(name = "BILL_NUMBER")
	private String billNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "BILL_DATE")
	private Date billDate;
	
	@Column(name = "NUMBER_OF_ITEMS")
	private Long noOfItems;
	
	@Column(name = "BILL_AMOUNT")
	private Double billAmount;
	
	@Column(name = "BILL_NET_AMOUNT")
	private Double billNetAmount;
	
	
	@Column(name = "ZONAL_REMARKS")
	private String zonalRemarks;
	
	@Column(name = "COPORATE_REMARKS")
	private String corporateRemarks;
	
	@Column(name = "BILLING_REMARKS")
	private String billingRemarks;
	
//	@Column(name = "DOCUMENTS")
//	private Blob documents;

	@Column(name = "DOCUMENT_TOKEN")
	private String documentToken;
	
	
/*	@Column(name = "TOTAL_CLAIMED_AMOUNT")
	private Double totalClaimedAmount;
	
	@Column(name = "TOTAL_APPROVED_AMOUNT")
	private Double totalApprovedAmount;*/
	
	@Column(name="CREATED_BY")
	private String createdBy;  
			
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name = "ROD_VERSION")
	private Long rodVersion;

	
	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

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
	 * @return the reimbursement
	 */
	public Reimbursement getReimbursement() {
		return reimbursement;
	}

	/**
	 * @param reimbursement the reimbursement to set
	 */
	public void setReimbursement(Reimbursement reimbursement) {
		this.reimbursement = reimbursement;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileType
	 */
	public MastersValue getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(MastersValue fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the billNumber
	 */
	public String getBillNumber() {
		return billNumber;
	}

	/**
	 * @param billNumber the billNumber to set
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	/**
	 * @return the billDate
	 */
	public Date getBillDate() {
		return billDate;
	}

	/**
	 * @param billDate the billDate to set
	 */
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	/**
	 * @return the noOfItems
	 */
	public Long getNoOfItems() {
		return noOfItems;
	}

	/**
	 * @param noOfItems the noOfItems to set
	 */
	public void setNoOfItems(Long noOfItems) {
		this.noOfItems = noOfItems;
	}

	/**
	 * @return the billAmount
	 */
	public Double getBillAmount() {
		return billAmount;
	}

	/**
	 * @param billAmount the billAmount to set
	 */
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	/**
	 * @return the zonalRemarks
	 */
	public String getZonalRemarks() {
		return zonalRemarks;
	}

	/**
	 * @param zonalRemarks the zonalRemarks to set
	 */
	public void setZonalRemarks(String zonalRemarks) {
		this.zonalRemarks = zonalRemarks;
	}

	/**
	 * @return the corporateRemarks
	 */
	public String getCorporateRemarks() {
		return corporateRemarks;
	}

	/**
	 * @param corporateRemarks the corporateRemarks to set
	 */
	public void setCorporateRemarks(String corporateRemarks) {
		this.corporateRemarks = corporateRemarks;
	}

	/**
	 * @return the billingRemarks
	 */
	public String getBillingRemarks() {
		return billingRemarks;
	}

	/**
	 * @param billingRemarks the billingRemarks to set
	 */
	public void setBillingRemarks(String billingRemarks) {
		this.billingRemarks = billingRemarks;
	}

	/**
	 * @return the documents
	 */
//	public Blob getDocuments() {
//		return documents;
//	}
//
//	/**
//	 * @param documents the documents to set
//	 */
//	public void setDocuments(Blob documents) {
//		this.documents = documents;
//	}
/*
	*//**
	 * @return the totalClaimedAmount
	 *//*
	public Double getTotalClaimedAmount() {
		return totalClaimedAmount;
	}

	*//**
	 * @param totalClaimedAmount the totalClaimedAmount to set
	 *//*
	public void setTotalClaimedAmount(Double totalClaimedAmount) {
		this.totalClaimedAmount = totalClaimedAmount;
	}

	*//**
	 * @return the totalApprovedAmount
	 *//*
	public Double getTotalApprovedAmount() {
		return totalApprovedAmount;
	}

	*//**
	 * @param totalApprovedAmount the totalApprovedAmount to set
	 *//*
	public void setTotalApprovedAmount(Double totalApprovedAmount) {
		this.totalApprovedAmount = totalApprovedAmount;
	}
*/
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(String documentToken) {
		this.documentToken = documentToken;
	}

	public Double getBillNetAmount() {
		return billNetAmount;
	}

	public void setBillNetAmount(Double billNetAmount) {
		this.billNetAmount = billNetAmount;
	}

	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((RODDocumentSummary) obj).getKey());
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

	public Long getRodVersion() {
		return rodVersion;
	}

	public void setRodVersion(Long rodVersion) {
		this.rodVersion = rodVersion;
	}
	
	

}
