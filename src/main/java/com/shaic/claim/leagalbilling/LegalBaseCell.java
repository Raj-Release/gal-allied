package com.shaic.claim.leagalbilling;

import java.io.Serializable;

import com.shaic.arch.SHAUtils;

public class LegalBaseCell implements Serializable{

	private static final long serialVersionUID = -7154278916704496657L;

	private Integer sno;

	private String tds;
	
	private Integer legalBillingAmount;
	
	private String legalBillingRemark;
	
	private Long benefitId;
	
	private String description;
	
	private Boolean isRemarkEditable =true;
	
	public Long getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(Long benefitId) {
		
		try{
			if(benefitId != null){
				String description = SHAUtils.legalbaseCellMap.get(benefitId);
				this.description = description;
				Integer sno = SHAUtils.legalbaseCellSno.get(benefitId);
				this.sno = sno;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.benefitId = benefitId;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getTds() {
		return tds;
	}

	public void setTds(String tds) {
		this.tds = tds;
	}

	public Integer getLegalBillingAmount() {
		return legalBillingAmount;
	}

	public void setLegalBillingAmount(Integer legalBillingAmount) {
		this.legalBillingAmount = legalBillingAmount;
	}

	public String getLegalBillingRemark() {
		return legalBillingRemark;
	}

	public void setLegalBillingRemark(String legalBillingRemark) {
		this.legalBillingRemark = legalBillingRemark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsRemarkEditable() {
		return isRemarkEditable;
	}

	public void setIsRemarkEditable(Boolean isRemarkEditable) {
		this.isRemarkEditable = isRemarkEditable;
	}
			
}
