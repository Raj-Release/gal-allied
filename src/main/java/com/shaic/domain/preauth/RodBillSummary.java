/**
 * 
 */
package com.shaic.domain.preauth;

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
import com.shaic.domain.Reimbursement;

/**
 * @author ntv.vijayar
 *
 */

@Entity
@Table(name="IMS_CLS_ROD_BILL_SUMMARY")
@NamedQueries({
@NamedQuery(name="RodBillSummary.findAll", query="SELECT r FROM RodBillSummary r"),
@NamedQuery(name ="RodBillSummary.findByKey",query="SELECT r FROM RodBillSummary r WHERE r.key = :primaryKey"),
@NamedQuery(name="RodBillSummary.findByReimbursementKey", query="SELECT r FROM RodBillSummary r WHERE r.reimbursement is not null and r.reimbursement.key = :reimbursementKey and r.deletedFlag is NULL order by r.key asc")
})

public class RodBillSummary extends AbstractEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@Id
	@SequenceGenerator(name="IMS_ROD_BILL_SUMMARY_KEY_GENERATOR", sequenceName = "SEQ_ROD_Bill_Summary_Key", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_ROD_BILL_SUMMARY_KEY_GENERATOR" )
	@Column(name="ROD_BILL_SUMMARY_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REIMBURSEMENT_KEY", nullable=false)
	private Reimbursement reimbursement;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name = "DOCUMENT_TOKEN")
	private String documentToken;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDate;

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	public String getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(String documentToken) {
		this.documentToken = documentToken;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
}


