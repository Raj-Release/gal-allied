package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="MAS_PA_CLAIM_COVERS")
@NamedQueries({
	@NamedQuery(name = "MasPaClaimCovers.findByCoverKey", query = "SELECT o FROM MasPaClaimCovers o where o.coverKey = :coverKey"),
	@NamedQuery(name = "MasPaClaimCovers.findByProductKey", query = "SELECT o FROM MasPaClaimCovers o where o.productKey = :productKey")
})
public class MasPaClaimCovers {
	
	@Id
	@Column(name="COVER_KEY")
	private Long coverKey;
	
	@Column(name="PRODUCT_KEY")
	private Long productKey;
	
	@Column(name="PRODUCT_CODE")
	private String productCode;

	@Column(name="COVER_TYPE")
	private String coverType;
	
	@Column(name="COVER_CODE")
	private String coverCode;
	
	@Column(name="COVER_DESCRIPTION")
	private String coverDescription;
	
	@Column(name="MIN_PERCENTAGE")
	private Double minPercentage;
	
	@Column(name="MAX_PERCENTAGE")
	private Double maxPercentage;
	
	@Column(name="MAX_PER_CLAIM_AMT")
	private Double maxClaimAmt;
	
	@Column(name="MAX_PER_POL_AMT")
	private Double maxPolAmt;
	
	@Column(name="MAX_AGE_LMT")
	private Integer maxAgeLimit;
	
	@Column(name="LMT_CNT")
	private Double lmtCnt;

	@Column(name="PER_DAY_AMT")
	private Double perDayAmt;
	
	@Column(name="MAX_CLM_DAYS")
	private Double maxClmDays;
	
	@Column(name="MAX_POL_DAYS")
	private Double maxPolDays;
	
	@Column(name="POL_APL_FLAG")
	private String polAplFlag;

	public Long getCoverKey() {
		return coverKey;
	}

	public void setCoverKey(Long coverKey) {
		this.coverKey = coverKey;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}

	public String getCoverDescription() {
		return coverDescription;
	}

	public void setCoverDescription(String coverDescription) {
		this.coverDescription = coverDescription;
	}

	public Double getMinPercentage() {
		return minPercentage;
	}

	public void setMinPercentage(Double minPercentage) {
		this.minPercentage = minPercentage;
	}

	public Double getMaxPercentage() {
		return maxPercentage;
	}

	public void setMaxPercentage(Double maxPercentage) {
		this.maxPercentage = maxPercentage;
	}

	public Double getMaxClaimAmt() {
		return maxClaimAmt;
	}

	public void setMaxClaimAmt(Double maxClaimAmt) {
		this.maxClaimAmt = maxClaimAmt;
	}

	public Double getMaxPolAmt() {
		return maxPolAmt;
	}

	public void setMaxPolAmt(Double maxPolAmt) {
		this.maxPolAmt = maxPolAmt;
	}

	public Integer getMaxAgeLimit() {
		return maxAgeLimit;
	}

	public void setMaxAgeLimit(Integer maxAgeLimit) {
		this.maxAgeLimit = maxAgeLimit;
	}

	public Double getLmtCnt() {
		return lmtCnt;
	}

	public void setLmtCnt(Double lmtCnt) {
		this.lmtCnt = lmtCnt;
	}

	public Double getPerDayAmt() {
		return perDayAmt;
	}

	public void setPerDayAmt(Double perDayAmt) {
		this.perDayAmt = perDayAmt;
	}

	public Double getMaxClmDays() {
		return maxClmDays;
	}

	public void setMaxClmDays(Double maxClmDays) {
		this.maxClmDays = maxClmDays;
	}

	public Double getMaxPolDays() {
		return maxPolDays;
	}

	public void setMaxPolDays(Double maxPolDays) {
		this.maxPolDays = maxPolDays;
	}

	public String getPolAplFlag() {
		return polAplFlag;
	}

	public void setPolAplFlag(String polAplFlag) {
		this.polAplFlag = polAplFlag;
	}
	
}
