/**
 * 
 */
package com.shaic.domain;

/**
 * @author ntv.vijayar
 *
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="MAS_PA_CLM_BEN_SUB_COVER")
@NamedQueries({
	@NamedQuery(name = "MasPAClaimBenefitsCover.findBySubCoverKey", query = "SELECT o FROM MasPAClaimBenefitsCover o where o.subCoverKey = :subCoverKey"),
})
public class MasPAClaimBenefitsCover extends AbstractEntity {
	
	@Id
	@Column(name="SUB_COVER_KEY")
	private Long subCoverKey;
	
	@Column(name="COVER_KEY")
	private Long coverKey;
	
	@Column(name="COVER_DESC")
	private String coverDescription;

	@Column(name="COVER_MAX_PERCENTAGE")
	private Long coverMaxPercentage;
	
	@Column(name="MAX_AMOUNT_WEEK")
	private Double maxAmountWeek;
	
	@Column(name="BENEFITS_ID")
	private Long benefitsId;
	
	/*@Column(name="BENEFITS_KEY1")
	private Long benefitsKey1;*/
	

	public Long getSubCoverKey() {
		return subCoverKey;
	}

	public void setSubCoverKey(Long subCoverKey) {
		this.subCoverKey = subCoverKey;
	}

	public Long getCoverKey() {
		return coverKey;
	}

	public void setCoverKey(Long coverKey) {
		this.coverKey = coverKey;
	}

	public String getCoverDescription() {
		return coverDescription;
	}

	public void setCoverDescription(String coverDescription) {
		this.coverDescription = coverDescription;
	}

	public Long getCoverMaxPercentage() {
		return coverMaxPercentage;
	}

	public void setCoverMaxPercentage(Long coverMaxPercentage) {
		this.coverMaxPercentage = coverMaxPercentage;
	}

	public Double getMaxAmountWeek() {
		return maxAmountWeek;
	}

	public void setMaxAmountWeek(Double maxAmountWeek) {
		this.maxAmountWeek = maxAmountWeek;
	}

	public Long getBenefitsId() {
		return benefitsId;
	}

	public void setBenefitsId(Long benefitsId) {
		this.benefitsId = benefitsId;
	}
/*
	public Long getBenefitsKey1() {
		return benefitsKey1;
	}

	public void setBenefitsKey1(Long benefitsKey1) {
		this.benefitsKey1 = benefitsKey1;
	}
*/
	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
