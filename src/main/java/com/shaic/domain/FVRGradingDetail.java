package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "IMS_CLS_FVR_GRADING_DTLS")
@NamedQueries({
	@NamedQuery(name = "FVRGradingDetail.findByFvrKey", query = "SELECT o FROM FVRGradingDetail o where o.fvrKey = :fvrKey"),
	@NamedQuery(name = "FVRGradingDetail.findByKey", query = "SELECT o FROM FVRGradingDetail o where o.key = :key")
	
})
public class FVRGradingDetail implements Serializable{
	
	
	@Id
	@SequenceGenerator(name="IMS_CLS_FVR_GRADING_DTLS_KEY_GENERATOR", sequenceName = "SEQ_FVR_GRADING_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_FVR_GRADING_DTLS_KEY_GENERATOR" )
	@Column(name = "FVR_GRADING_KEY")
	private Long key;
	
	@Column(name = "FVRDTLS_KEY")
	private Long fvrKey;
	
	@Column(name = "GRADING")
	private String grading;

	@Column(name = "SEGEMENT")
	private String segment;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="GRADING_DATE")
	private Date gradingDate;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "SEQUENCE_NUMBER")
	private Long seqNo;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getFvrKey() {
		return fvrKey;
	}

	public void setFvrKey(Long fvrKey) {
		this.fvrKey = fvrKey;
	}

	public String getGrading() {
		return grading;
	}

	public void setGrading(String grading) {
		this.grading = grading;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
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

	public Date getGradingDate() {
		return gradingDate;
	}

	public void setGradingDate(Date gradingDate) {
		this.gradingDate = gradingDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	
	
}
