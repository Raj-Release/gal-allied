/**
 * 
 */
package com.shaic.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author ntv.vijayar
 *
 */
@Entity
@Table(name="MAS_CLM_ADV_PROVISION")
@NamedQueries( {
	@NamedQuery(name="MASClaimAdvancedProvision.findAll", query="SELECT m FROM MASClaimAdvancedProvision m"),
	@NamedQuery(name="MASClaimAdvancedProvision.findByBranchCode", query="SELECT m FROM MASClaimAdvancedProvision m where m.branchCode = :branchCode")
})
public class MASClaimAdvancedProvision {
	
	@Id
	@Column(name="BRANCH_CODE")
	private Long branchCode;
	
	@Column(name = "CLAIMS_COUNT")
	private Long claimsCount;
	
	
	@Column(name = "TOT_AMT")
	private Double totalAmt;
	
	@Column(name = "AVG_AMT")
	private Double avgAmt;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	

//	@Column(name="MASTER_TYPE_KEY")
//	private Integer masterListKey;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	public Long getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(Long branchCode) {
		this.branchCode = branchCode;
	}

	public Long getClaimsCount() {
		return claimsCount;
	}

	public void setClaimsCount(Long claimsCount) {
		this.claimsCount = claimsCount;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Double getAvgAmt() {
		return avgAmt;
	}

	public void setAvgAmt(Double avgAmt) {
		this.avgAmt = avgAmt;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


}
