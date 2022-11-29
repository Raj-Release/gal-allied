package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="ZUC_QUERY_DETAILS")
@NamedQueries({
@NamedQuery(name="ZUAQueryHistoryTable.findByPolicyNumber", query="SELECT z FROM ZUAQueryHistoryTable z where z.policyNo = :policyNumber")
})
public class ZUAQueryHistoryTable extends AbstractEntity{
		
	@Id
	@Column(name="ZUQ_POL_SYS_ID")
	private Long key;
	
	@Column(name="ZUQ_POLICY_NO")
	private String policyNo;
	
	@Column(name="ZUQ_FLOW_FROM")
	private String flowFrom;
	
	@Column(name="ZUQ_FLOW_TO")
	private String flowTo;
	
	@Column(name="ZUQ_FLOW_TYPE")
	private String flowType;
	
	/*@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ZUQ_TIME")
	private Date time;*/
	
	@Column(name="ZUQ_STS")
	private String queryStatus;
	
	@Column(name="ZUQ_REMARKS")
	private String remarks;
	
	@Column(name="ZUQ_SR_NO")
	private String SerialNO;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ZUQ_CR_DT")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ZUQ_APPR_DT")
	private Date appprovedDate;
	
	@Column(name="ZUQ_CR_UID")
	private String createdBy;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getFlowFrom() {
		return flowFrom;
	}

	public void setFlowFrom(String flowFrom) {
		this.flowFrom = flowFrom;
	}

	public String getFlowTo() {
		return flowTo;
	}

	public void setFlowTo(String flowTo) {
		this.flowTo = flowTo;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	/*public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}*/

	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSerialNO() {
		return SerialNO;
	}

	public void setSerialNO(String serialNO) {
		SerialNO = serialNO;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getAppprovedDate() {
		return appprovedDate;
	}

	public void setAppprovedDate(Date appprovedDate) {
		this.appprovedDate = appprovedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
}
