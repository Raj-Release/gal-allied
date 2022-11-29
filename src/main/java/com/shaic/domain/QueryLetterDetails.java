package com.shaic.domain;

import java.io.Serializable;
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

/**
 * Entity implementation class for Entity: QueryLetterDetails
 *
 */
@Entity
@Table(name="IMS_CLS_ROD_QUERY_DTLS")
@NamedQueries({
	@NamedQuery(name="QueryLetterDetails.findAll", query="SELECT q FROM QueryLetterDetails q"),
	@NamedQuery(name="QueryLetterDetails.findDraftDetailsByQueryKey", query="SELECT q FROM QueryLetterDetails q where q.reimbQuery.key = :queryKey and q.processType = 'D' and (q.deletedFlag is null or q.deletedFlag <> 'Y') order by q.key asc"),
	@NamedQuery(name="QueryLetterDetails.findReDraftDetailsByQueryKey", query="SELECT q FROM QueryLetterDetails q where q.reimbQuery.key = :queryKey and q.processType = 'R' and (q.deletedFlag is null or q.deletedFlag <> 'Y') order by q.key asc")
})
public class QueryLetterDetails implements Serializable {

	
	private static final long serialVersionUID = 1L;

	
	@Id
	@SequenceGenerator(name="IMS_CLS_SEQ_ROD_QRY_DTLS_KEY_GENERATOR", sequenceName = "SEQ_ROD_QRY_DTLS_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_ROD_QRY_DTLS_KEY_GENERATOR" )
	@Column(name = "QUERY_DTLS_KEY")
	Long key;
	
	@OneToOne
	@JoinColumn(name = "REIMBURSEMENT_QUERY_KEY")
	private ReimbursementQuery reimbQuery;
	
	@Column(name = "REIMBURSEMENT_KEY")
	private Long reimbursementKey;
	
	@Column(name = "PROCESS_TYPE", nullable = true, columnDefinition = "Varchar(1)", length = 1)
	private String processType;
				
	@Column(name = "SEQUENCE_NUMBER")
	private Long sno;
	
	@Column(name = "REMARKS")
	private String DraftOrReDraftRemarks;
	
	@Column(name = "DELETED_FLAG", nullable = true, columnDefinition = "Varchar(1)", length = 1)
	private String deletedFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")		
	private Date modifiedDate;
			
	public QueryLetterDetails() {
		
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public ReimbursementQuery getReimbQuery() {
		return reimbQuery;
	}

	public void setReimbQuery(ReimbursementQuery reimbQuery) {
		this.reimbQuery = reimbQuery;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public Long getSno() {
		return sno;
	}

	public void setSno(Long sno) {
		this.sno = sno;
	}

	public String getDraftOrReDraftRemarks() {
		return DraftOrReDraftRemarks;
	}

	public void setDraftOrReDraftRemarks(String draftOrReDraftRemarks) {
		DraftOrReDraftRemarks = draftOrReDraftRemarks;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}	
   
}
