package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="IMS_CLS_PROD_RPT_LOG")
public class ReportLog extends AbstractEntity implements Serializable {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_RPT_KEY_GENERATOR", sequenceName = "SEQ_RPT_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_RPT_KEY_GENERATOR" ) 
	@Column(name="RPT_KEY")
	private Long key;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CURRENT_DATE")
	private Date currentDate;
	
	@Column(name="REPORT_NAME")
	private String reportName; 
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="STATUS")
	private String statusFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REPORTTIME")
	private Date reportTime;
	
	@Column(name="ACTIVE_FLAG")
	private String aciveStatusFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getAciveStatusFlag() {
		return aciveStatusFlag;
	}

	public void setAciveStatusFlag(String aciveStatusFlag) {
		this.aciveStatusFlag = aciveStatusFlag;
	}
	
	
}
