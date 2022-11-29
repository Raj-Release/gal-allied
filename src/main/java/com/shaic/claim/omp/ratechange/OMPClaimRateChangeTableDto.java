package com.shaic.claim.omp.ratechange;

import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPClaimRateChangeTableDto  extends AbstractTableDTO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date dateofModification;
	
	private String modifyby;
	
	private String processingStage;
	
	private Double  inrConversionRate;
	
	private Double conversionValue;
	
	private Long key;

	public Date getDateofModification() {
		return dateofModification;
	}

	public void setDateofModification(Date dateofModification) {
		this.dateofModification = dateofModification;
	}

	public String getModifyby() {
		return modifyby;
	}

	public void setModifyby(String modifyby) {
		this.modifyby = modifyby;
	}

	public String getProcessingStage() {
		return processingStage;
	}

	public void setProcessingStage(String processingStage) {
		this.processingStage = processingStage;
	}

	public Double getInrConversionRate() {
		return inrConversionRate;
	}

	public void setInrConversionRate(Double inrConversionRate) {
		this.inrConversionRate = inrConversionRate;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Double getConversionValue() {
		return conversionValue;
	}

	public void setConversionValue(Double conversionValue) {
		this.conversionValue = conversionValue;
	}

}
