/**
 * 
 */
package com.shaic.domain.outpatient;

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
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;

/**
 * @author ntv.vijayar
 *
 */

@Entity
@Table(name="IMS_CLS_OP_DOCUMENT_SUMMARY")
@NamedQueries({
@NamedQuery(name="OPDocumentSummary.findAll", query="SELECT r FROM OPDocumentSummary r"),
@NamedQuery(name ="OPDocumentSummary.findByKey",query="SELECT r FROM OPDocumentSummary r WHERE r.key = :primaryKey"),
@NamedQuery(name ="OPDocumentSummary.findByOPHelathCheckupKey",query="SELECT r FROM OPDocumentSummary r WHERE r.opHealthCheckup is not null and r.opHealthCheckup.key = :healthCheckupKey"),
@NamedQuery(name ="OPDocumentSummary.findByClaimKey",query="SELECT r FROM OPDocumentSummary r WHERE r.claim.key = :claimKey")
})

public class OPDocumentSummary extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_DOCUMENT_SUMMARY_KEY_GENERATOR", sequenceName = "SEQ_ROD_DOCUMENT_SUMMARY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OP_DOCUMENT_SUMMARY_KEY_GENERATOR" )
	@Column(name="DOCUMENT_SUMMARY_KEY")//OP_DOC_SUMMARY_KEY
	private Long key;
	
	@OneToOne
	@JoinColumn(name="OP_HEALTH_CHECKUP_KEY", nullable=false)
	private OPHealthCheckup opHealthCheckup;
	
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
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "DOCUMENT_TOKEN")
	private String dmsDocToken;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@OneToOne
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private OPClaim claim;
	
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

	public OPHealthCheckup getOpHealthCheckup() {
		return opHealthCheckup;
	}

	public void setOpHealthCheckup(OPHealthCheckup opHealthCheckup) {
		this.opHealthCheckup = opHealthCheckup;
	}

	public MastersValue getFileType() {
		return fileType;
	}

	public void setFileType(MastersValue fileType) {
		this.fileType = fileType;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDmsDocToken() {
		return dmsDocToken;
	}

	public void setDmsDocToken(String dmsDocToken) {
		this.dmsDocToken = dmsDocToken;
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

	public OPClaim getClaim() {
		return claim;
	}

	public void setClaim(OPClaim claim) {
		this.claim = claim;
	}

}
