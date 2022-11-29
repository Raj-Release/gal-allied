package com.shaic.domain.gmcautomailer;

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


@Entity
@Table(name="MAS_BRANCH_EMAIL")
@NamedQueries({
	@NamedQuery(name="MasBranchEmail.getRoleByUser", query="SELECT m FROM MasBranchEmail m WHERE m.divCode = :divCode")
})
public class MasBranchEmail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SEQ_BRANCH_EMAIL_KEY_GENERATOR", sequenceName = "SEQ_BRANCH_EMAIL_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_BRANCH_EMAIL_KEY_GENERATOR" ) 
	@Column(name="\"BRANCH_EMAIL_KEY\"")
	private Long key;
	
	@Column(name="DIVN_CODE")
	private Long divCode;
	
	@Column(name="AREA_CODE")
	private Long areaCode;
	
	@Column(name="ZONAL_CODE")
	private Long zonalCode;
	
	@Column(name="BRANCH_MAIL_ID")
	private String branchEmailId;

	@Column(name="INTM_MAIL_ID")
	private String intimationEmailId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_ON")
	private Date modifiedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON")
	private Date createdDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	public Long getDivCode() {
		return divCode;
	}

	public void setDivCode(Long divCode) {
		this.divCode = divCode;
	}

	public Long getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(Long areaCode) {
		this.areaCode = areaCode;
	}

	public Long getZonalCode() {
		return zonalCode;
	}

	public void setZonalCode(Long zonalCode) {
		this.zonalCode = zonalCode;
	}

	public String getBranchEmailId() {
		return branchEmailId;
	}

	public void setBranchEmailId(String branchEmailId) {
		this.branchEmailId = branchEmailId;
	}

	public String getIntimationEmailId() {
		return intimationEmailId;
	}

	public void setIntimationEmailId(String intimationEmailId) {
		this.intimationEmailId = intimationEmailId;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	
}
