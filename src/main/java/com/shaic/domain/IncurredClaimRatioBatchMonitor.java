package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_CLM_CLR_BATCH_MONITOR")
@NamedQueries({
@NamedQuery(name="IncurredClaimRatioBatchMonitor.findBySysDate", query="SELECT r FROM IncurredClaimRatioBatchMonitor r where r.createdDate >= TRUNC(SYSDATE)")
})
public class IncurredClaimRatioBatchMonitor extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_CLM_CLR_BATCH_MONITOR_BATCH_ID_GENERATOR", sequenceName = "SEQ_CLM_CLR_BATCH_MONITOR"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_CLM_CLR_BATCH_MONITOR_BATCH_ID_GENERATOR" ) 
	
	@Column(name="BATCH_ID")
	private Long key;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="BATCH_DATE")
	private Date batchDate;
	
	@Column(name="APP_BATCH_FLAG")
	private String appBatchFlag;
	
	@Column(name="DB_BATCH_FLAG")
	private String dbBatchFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="ACTIVE_FLAG")
	private String activeFlag;

//	public Long getBatchID() {
//		return batchID;
//	}
//
//	public void setBatchID(Long batchID) {
//		this.batchID = batchID;
//	}

	public Date getBatchDate() {
		return batchDate;
	}

	public void setBatchDate(Date batchDate) {
		this.batchDate = batchDate;
	}

	public String getAppBatchFlag() {
		return appBatchFlag;
	}

	public void setAppBatchFlag(String appBatchFlag) {
		this.appBatchFlag = appBatchFlag;
	}

	public String getDbBatchFlag() {
		return dbBatchFlag;
	}

	public void setDbBatchFlag(String dbBatchFlag) {
		this.dbBatchFlag = dbBatchFlag;
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

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}



}
