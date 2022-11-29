//changes at DB level in progress

package com.shaic.domain.preauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: ClaimSubLimit
 *
 */
@Entity
@Table(name="MAS_CLAIM_LIMIT")
public class ClaimSubLimit implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="KEY")
	private Long key;
	
//	/*@OneToOne
//	@JoinColumn(name="FK_CLAIM_LIMIT_KEY")
//	private Claim claim;*/
//	
//	@Column(name="VERSION")
//	private Long version;
//	
//	@Column(name="AGE_FROM")
//	private Long ageFrom;
//	
//	@Column(name="AGE_TO")
//	private Long ageTo;
//	
//	@Column(name="SUB_LIMIT_AMOUNT")
//	private Double sublimitAmount;
//	
//	@Column(name="SUB_LIMIT_PERCENTAGE")
//	private String subLimitPercentage;
//	
//	@Column(name="CITY_CLASS")
//	private String cityClass;
//	
//	@Column(name="SUB_LIMIT_TYPE")
//	private String subLimitType;
//	
//	@Column(name="SUB_LIMIT_NAME")
//	private String subLimitName;
//	
//	@Column(name="SUB_LIMIT_DESCRIPTION")
//	private String subLimitDescription;
//
//	public ClaimSubLimit() {
//	}
//
//	public Long getKey() {
//		return key;
//	}
//
//	public void setKey(Long key) {
//		this.key = key;
//	}
//
//	/*public Claim getClaim() {
//		return claim;
//	}
//
//	public void setClaim(Claim claim) {
//		this.claim = claim;
//	}*/
//
//	public Long getVersion() {
//		return version;
//	}
//
//	public void setVersion(Long version) {
//		this.version = version;
//	}
//
//	public Long getAgeFrom() {
//		return ageFrom;
//	}
//
//	public void setAgeFrom(Long ageFrom) {
//		this.ageFrom = ageFrom;
//	}
//
//	public Long getAgeTo() {
//		return ageTo;
//	}
//
//	public void setAgeTo(Long ageTo) {
//		this.ageTo = ageTo;
//	}
//
//	public Double getSublimitAmount() {
//		return sublimitAmount;
//	}
//
//	public void setSublimitAmount(Double sublimitAmount) {
//		this.sublimitAmount = sublimitAmount;
//	}
//
//	public String getSubLimitPercentage() {
//		return subLimitPercentage;
//	}
//
//	public void setSubLimitPercentage(String subLimitPercentage) {
//		this.subLimitPercentage = subLimitPercentage;
//	}
//
//	public String getCityClass() {
//		return cityClass;
//	}
//
//	public void setCityClass(String cityClass) {
//		this.cityClass = cityClass;
//	}
//
//	public String getSubLimitType() {
//		return subLimitType;
//	}
//
//	public void setSubLimitType(String subLimitType) {
//		this.subLimitType = subLimitType;
//	}
//
//	public String getSubLimitName() {
//		return subLimitName;
//	}
//
//	public void setSubLimitName(String subLimitName) {
//		this.subLimitName = subLimitName;
//	}
//
//	public String getSubLimitDescription() {
//		return subLimitDescription;
//	}
//
//	public void setSubLimitDescription(String subLimitDescription) {
//		this.subLimitDescription = subLimitDescription;
//	}	
   
}
