package com.shaic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.shaic.domain.MastersValue;

@Entity
@Table(name="IMS_CLS_CLAIM_REMARKS_ALERTS")
@NamedQueries({
@NamedQuery(name="ClaimRemarksAlerts.findByIntimationno",query = "SELECT o FROM ClaimRemarksAlerts o where o.intitmationNo = :intitmationNo and o.deleteFlag = 'N' "),
@NamedQuery(name="ClaimRemarksAlerts.findBykey",query = "SELECT o FROM ClaimRemarksAlerts o where o.key = :key"),
@NamedQuery(name="ClaimRemarksAlerts.findByIntimationnoWithStatus",query = "SELECT o FROM ClaimRemarksAlerts o where o.intitmationNo = :intitmationNo and o.activeStatus = 1 and o.deleteFlag = 'N'"),
@NamedQuery(name="ClaimRemarksAlerts.findByfindByIntimationCatKey", query="SELECT r FROM ClaimRemarksAlerts r WHERE  r.intitmationNo = :intitmationNo and r.alertCategory.key IN (:catList) and r.activeStatus = 1 and r.deleteFlag = 'N'"),

})
public class ClaimRemarksAlerts implements Serializable{
	
	@Id
	@SequenceGenerator(name="CLAIM_REMARKS_ALERTS_KEY_GENERATOR", sequenceName = "SEQ_CLM_REMARK_ALERT_ID", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLAIM_REMARKS_ALERTS_KEY_GENERATOR" )
	@Column(name="CLM_REMARK_ALERT_KEY")
	private Long key;
	
	@Column(name="INTIMATION_NUMBER")
	private String intitmationNo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="REMARK_ID",  nullable=false)
	private MastersValue alertCategory;
	
	@Column(name="CLAIM_REMARKS")
	private String remarks;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifyBy;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "CLM_REMARK_ALERT_KEY")
	@Where(clause = "ACTIVE_STATUS = 1")
	private List<ClaimRemarksDocs> remarksDocs;
	
	@Column(name="DELETE_FLAG")
	private String deleteFlag;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntitmationNo() {
		return intitmationNo;
	}

	public void setIntitmationNo(String intitmationNo) {
		this.intitmationNo = intitmationNo;
	}

	public MastersValue getAlertCategory() {
		return alertCategory;
	}

	public void setAlertCategory(MastersValue alertCategory) {
		this.alertCategory = alertCategory;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<ClaimRemarksDocs> getRemarksDocs() {
		return remarksDocs;
	}

	public void setRemarksDocs(List<ClaimRemarksDocs> remarksDocs) {
		this.remarksDocs = remarksDocs;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}	
}
