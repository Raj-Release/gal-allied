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

/**
 * Entity implementation class for Entity: MasUser
 *
 */
@Entity
@Table(name="MAS_SEC_USER")
@NamedQueries({
	@NamedQuery(name="MasUser.findAll", query="SELECT m FROM MasUser m where m.activeStatus is not null and m.activeStatus <> 'N' order by m.userName asc "),
	@NamedQuery(name="MasUser.findByEmpName", query="SELECT m FROM MasUser m WHERE Lower(m.userName) = :userName and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUser.getEmpByName", query="SELECT m FROM MasUser m WHERE m.userName LIKE :userName and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUser.getEmpById", query="SELECT m FROM MasUser m WHERE m.userId = :userId and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUser.getById", query="SELECT m FROM MasUser m WHERE Lower(m.userId) = :userId and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUser.getByKey", query="SELECT m FROM MasUser m WHERE m.key = :userId and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUser.getByPedReviewer", query="SELECT m FROM MasUser m WHERE Lower(m.userId) = :userId and m.pedReviewer = 'Y' and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUser.getByType", query="SELECT m FROM MasUser m WHERE m.userId = :userId "),
	@NamedQuery(name="MasUser.getEmpDetailsById",query = "SELECT m FROM MasUser m WHERE Lower(m.empId) =:empId and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUser.getByIdWithUserName", query="SELECT m FROM MasUser m WHERE Lower(m.userId) = :userId and m.userName is null and m.activeStatus is not null and m.activeStatus <> 'N'"),
	@NamedQuery(name="MasUser.findByMasuserIdWithInactive", query="SELECT m FROM MasUser m WHERE Lower(m.userId) = :userId ")
})

public class MasUser implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public MasUser() {
		super();
	}
	
	@Id
	@SequenceGenerator(name="MAS_SEC_USER_KEY_GENERATOR", sequenceName = "SEQ_USER_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_SEC_USER_KEY_GENERATOR" )
	@Column(name="\"USER_KEY\"")
	private Long key;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="EMP_ID")
	private String empId;
	
	@Column(name="USER_NAME")
	private String userName;
	
	@Column(name="USER_TYPE")
	private String userType;
	
	@Column(name="MIN_AMOUNT")
	private Long minAmt;
	
	@Column(name="MAX_AMOUNT")
	private Long maxAmt;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	@Column(name="PEDREVIEWER")
	private String pedReviewer;
	
	@Column(name="CORPORAT_USER_FLAG")
	private String corporateUserFlag;

	@Column(name="HIGH_VALUE_FLAG")
	private String highValueFlag;
	
	@Column(name="SHIFT_START_TIME")
	private String shiftStartTime;
	
	@Column(name="SHIFT_END_TIME")
	private String shiftEndTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	// CR2019213
	@Column(name="MANUAL_FLAG")
	private String manualFlag;
	
	// AutoAllocationCategory
	@Column(name="GET_NEXT_CPU_STAGE_OVER_RIDE")
	private String documenType;
	
	@Column(name="CLAIM_APPLICABLE")
	private String claimApplicable;
	
	@Column(name="MANUAL_CODING_FLAG")
	private String manualCodingFlag;
	
	@Column(name="ALLOW_LOW_CLM_VALUE")
	private String allowRestrictToBand;
	
	// CR2019213
	@Column(name="MANUAL_FLAG_MA")
	private String manualPickFlagMA;
	
	// CR2019213
	@Column(name="AA_HYBRID_FLAG")
	private String manualPickHybridFlag;
	
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getKey() {
		return key;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(Long minAmt) {
		this.minAmt = minAmt;
	}

	public Long getMaxAmt() {
		return maxAmt;
	}

	public void setMaxAmt(Long maxAmt) {
		this.maxAmt = maxAmt;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getPedReviewer() {
		return pedReviewer;
	}

	public void setPedReviewer(String pedReviewer) {
		this.pedReviewer = pedReviewer;
	}

	public String getCorporateUserFlag() {
		return corporateUserFlag;
	}

	public void setCorporateUserFlag(String corporateUserFlag) {
		this.corporateUserFlag = corporateUserFlag;
	}

	public String getHighValueFlag() {
		return highValueFlag;
	}

	public void setHighValueFlag(String highValueFlag) {
		this.highValueFlag = highValueFlag;
	}

	public String getShiftStartTime() {
		return shiftStartTime;
	}

	public void setShiftStartTime(String shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}

	public String getShiftEndTime() {
		return shiftEndTime;
	}

	public void setShiftEndTime(String shiftEndTime) {
		this.shiftEndTime = shiftEndTime;
	}

	public String getManualFlag() {
		return manualFlag;
	}

	public void setManualFlag(String manualFlag) {
		this.manualFlag = manualFlag;
	}
	public String getDocumenType() {
		return documenType;
	}
	
	public void setDocumenType(String documenType) {
		this.documenType = documenType;
	}

	public String getClaimApplicable() {
		return claimApplicable;
	}

	public void setClaimApplicable(String claimApplicable) {
		this.claimApplicable = claimApplicable;
	}

	public String getManualCodingFlag() {
		return manualCodingFlag;
	}

	public void setManualCodingFlag(String manualCodingFlag) {
		this.manualCodingFlag = manualCodingFlag;
	}

	public String getAllowRestrictToBand() {
		return allowRestrictToBand;
	}

	public void setAllowRestrictToBand(String allowRestrictToBand) {
		this.allowRestrictToBand = allowRestrictToBand;
	}

	public String getManualPickFlagMA() {
		return manualPickFlagMA;
	}

	public void setManualPickFlagMA(String manualPickFlagMA) {
		this.manualPickFlagMA = manualPickFlagMA;
	}

	public String getManualPickHybridFlag() {
		return manualPickHybridFlag;
	}

	public void setManualPickHybridFlag(String manualPickHybridFlag) {
		this.manualPickHybridFlag = manualPickHybridFlag;
	}
	
}
