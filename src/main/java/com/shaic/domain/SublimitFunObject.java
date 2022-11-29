package com.shaic.domain;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SublimitFunObject extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = -5107320632574890710L;

	private String name;
	
	private Double amount;
	
	private Double amntUtilisedIncludingCurrentClaim;
	
	private Double availableBalanceIncludingCurrentClaim;
	
	private Double amntUtilisedExcludingCurrentClaim;
	
	private Double availableBalanceExcludingCurrentClaim;
	
	
	
	public Double getAmntUtilisedIncludingCurrentClaim() {
		return amntUtilisedIncludingCurrentClaim;
	}

	public void setAmntUtilisedIncludingCurrentClaim(
			Double amntUtilisedIncludingCurrentClaim) {
		this.amntUtilisedIncludingCurrentClaim = amntUtilisedIncludingCurrentClaim;
	}

	public Double getAvailableBalanceIncludingCurrentClaim() {
		return availableBalanceIncludingCurrentClaim;
	}

	public void setAvailableBalanceIncludingCurrentClaim(
			Double availableBalanceIncludingCurrentClaim) {
		this.availableBalanceIncludingCurrentClaim = availableBalanceIncludingCurrentClaim;
	}

	public Double getAmntUtilisedExcludingCurrentClaim() {
		return amntUtilisedExcludingCurrentClaim;
	}

	public void setAmntUtilisedExcludingCurrentClaim(
			Double amntUtilisedExcludingCurrentClaim) {
		this.amntUtilisedExcludingCurrentClaim = amntUtilisedExcludingCurrentClaim;
	}

	public Double getAvailableBalanceExcludingCurrentClaim() {
		return availableBalanceExcludingCurrentClaim;
	}

	public void setAvailableBalanceExcludingCurrentClaim(
			Double availableBalanceExcludingCurrentClaim) {
		this.availableBalanceExcludingCurrentClaim = availableBalanceExcludingCurrentClaim;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private String description;
	
	private Long limitId;
	
	
	public SublimitFunObject()
	{
		
	}
	
	public SublimitFunObject(String name, String description, Double amount, Long limitId)
	{
		this.name = name;
		this.description = description;
		this.amount = amount;
		this.limitId = limitId;
	}
	
	public SublimitFunObject(Long limitIdNumber,String name,Double limitAmnt,Double limittotalUtilizationAmnt,Double limitUtilizationAmnt)
	{
		this.limitId = limitIdNumber;
		this.name = name;
		this.amount = limitAmnt;
		this.amntUtilisedExcludingCurrentClaim = limitUtilizationAmnt;
		this.amntUtilisedIncludingCurrentClaim = limittotalUtilizationAmnt;
		Double balanceIncludingCurrentClaim = limitAmnt - limittotalUtilizationAmnt;
		Double balanceExcludingCurrentClaim = limitAmnt - limitUtilizationAmnt;
		this.availableBalanceIncludingCurrentClaim = balanceIncludingCurrentClaim;
		this.availableBalanceExcludingCurrentClaim = balanceExcludingCurrentClaim;
		
	}
	@Override
	public String toString() {
		return this.name + " : " + this.amount + " : " + this.description;
	}

	public Long getLimitId() {
		return limitId;
	}

	public void setLimitId(Long limitId) {
		this.limitId = limitId;
	}
	

	 @Override
	    public boolean equals(Object obj) {
	        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
	            return false;
	        } else if (getLimitId() == null) {
	            return obj == this;
	        } else {
	            return getLimitId().equals(((SublimitFunObject) obj).getLimitId());
	        }
	    }

	    @Override
	    public int hashCode() {
	        if (limitId != null) {
	            return limitId.hashCode();
	        } else {
	            return super.hashCode();
	        }
	    }

	
}
