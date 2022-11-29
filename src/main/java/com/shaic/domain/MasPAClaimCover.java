package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_PA_CLAIM_COVERS")
@NamedQueries( {
@NamedQuery(name="MasPAClaimCover.findAll", query="SELECT m FROM MasPAClaimCover m"),
@NamedQuery(name="MasPAClaimCover.findByUniqueCoverDesc", query="SELECT distinct(m.coverDesc) FROM MasPAClaimCover m"),
@NamedQuery(name = "MasPAClaimCover.findByKey",query = "select o from MasPAClaimCover o where o.key = :coverKey"),
})
public class MasPAClaimCover {
	
	@Id
	@Column(name="COVER_KEY")
	private Long key;
	
	@Column(name="PRODUCT_KEY")
	private Long prodKey;
	
	@Column(name="PRODUCT_CODE")
	private String prodCode;
	
	@Column(name="COVER_TYPE")
	private String coverType;
	
	@Column(name="COVER_CODE")
	private String coverCode;
	
	@Column(name="COVER_DESCRIPTION")
	private String coverDesc;
	
	@Column(name="MIN_PERCENTAGE")
	private Long minPercent;
	
	@Column(name="MAX_PERCENTAGE")
	private Long maxPercent;
	
	@Column(name="MAX_PER_CLAIM_AMT")
	private Double maxClaimAmt;
	
	@Column(name="MAX_PER_POL_AMT")
	private Double maxPolAmt;
	
	@Column(name="MAX_AGE_LMT")
	private Long maxAgeLmt;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getProdKey() {
		return prodKey;
	}

	public void setProdKey(Long prodKey) {
		this.prodKey = prodKey;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
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

	public String getCoverDesc() {
		return coverDesc;
	}

	public void setCoverDesc(String coverDesc) {
		this.coverDesc = coverDesc;
	}

	public Long getMinPercent() {
		return minPercent;
	}

	public void setMinPercent(Long minPercent) {
		this.minPercent = minPercent;
	}

	public Long getMaxPercent() {
		return maxPercent;
	}

	public void setMaxPercent(Long maxPercent) {
		this.maxPercent = maxPercent;
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

	public Long getMaxAgeLmt() {
		return maxAgeLmt;
	}

	public void setMaxAgeLmt(Long maxAgeLmt) {
		this.maxAgeLmt = maxAgeLmt;
	}

	public Long getLmtCount() {
		return lmtCount;
	}

	public void setLmtCount(Long lmtCount) {
		this.lmtCount = lmtCount;
	}

	public Double getPerDayAmt() {
		return perDayAmt;
	}

	public void setPerDayAmt(Double perDayAmt) {
		this.perDayAmt = perDayAmt;
	}

	public Long getMaxClaimDays() {
		return maxClaimDays;
	}

	public void setMaxClaimDays(Long maxClaimDays) {
		this.maxClaimDays = maxClaimDays;
	}

	public Long getMaxPolDays() {
		return maxPolDays;
	}

	public void setMaxPolDays(Long maxPolDays) {
		this.maxPolDays = maxPolDays;
	}

	public String getPolAplFlag() {
		return polAplFlag;
	}

	public void setPolAplFlag(String polAplFlag) {
		this.polAplFlag = polAplFlag;
	}

	@Column(name="LMT_CNT")
	private Long lmtCount;
	
	@Column(name="PER_DAY_AMT")
	private Double perDayAmt;
	
	@Column(name="MAX_CLM_DAYS")
	private Long maxClaimDays;
	
	@Column(name="MAX_POL_DAYS")
	private Long maxPolDays;
	
	@Column(name="POL_APL_FLAG")
	private String polAplFlag;
}
