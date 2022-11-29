package com.shaic.claim.premedical.rule;

import com.shaic.arch.SHAUtils;



public class PreauthRule {

	public PreauthRule()
	{
		
	}
	
	public Boolean matchNoOfDays(String noOfDaysFromRentAndICU, String noOfDays ) {
		return true;
	}
	
	public String roomRentAmountRequestedChargesCalculation(String noOfDays, String perDatAmt) {
		
		Integer calculatedAmt = SHAUtils.getFloatFromString(noOfDays) *  SHAUtils.getFloatFromString(perDatAmt);
		return calculatedAmt.toString();
	}
	
	public String roomRentNetAmountChargesCalculation(String noOfDays, String minimumAmt) {
		Integer calculatedAmt = SHAUtils.getFloatFromString(noOfDays) *  SHAUtils.getFloatFromString(minimumAmt);
		return calculatedAmt.toString();
	}
	
	public String roomRentNetDeductibleChargesCalculation(String amountRequested, String netAmount) {
		if (SHAUtils.getFloatFromString(netAmount) == 0)
		{
			return "0";
		}
		Integer calculatedAmt = SHAUtils.getFloatFromString(amountRequested)  -  SHAUtils.getFloatFromString(netAmount);
		if(calculatedAmt > 0) {
			return calculatedAmt.toString();
		}
		return "0";
	}
	
	public String roomRentAmountConsideredChargesCalculation(String amountRequested, String netAmount) {
		if (SHAUtils.getFloatFromString(netAmount) == 0)
		{
			return amountRequested;
		}
		String lowerAmt = netAmount;
		if(SHAUtils.getFloatFromString(amountRequested) < SHAUtils.getFloatFromString(netAmount)) {
			lowerAmt = amountRequested;
		}
		return lowerAmt;
	}
	
	public String otherDetailsAmountConsideredCalculation(String netAmount , String roomRentAmtConsidered, String roomRentRequested) {
		String amt = "";
		Integer floatFromString = SHAUtils.getFloatFromString(roomRentRequested);
		Float calculatedAmt = 0.0f;
		if (floatFromString > 0) {
			calculatedAmt = SHAUtils.getFloatFromString(netAmount) * (SHAUtils.getFloatFromString(roomRentAmtConsidered).floatValue()/floatFromString.floatValue());	
		}
		else {
			calculatedAmt = SHAUtils.getFloatFromString(netAmount).floatValue() * (SHAUtils.getFloatFromString(roomRentAmtConsidered).floatValue());
		}
		Double ceil = Math.ceil(Double.valueOf(calculatedAmt));
		amt = String.valueOf(ceil.intValue());
		return amt;
	}
	
	public String compositePackageWithoutOverrideDeductionConsiderAmt(String netAmount) {
		String amt = "";
		if(netAmount != null && netAmount.length() > 0) {
			Float calculatedAmt = SHAUtils.getFloatFromString(netAmount) * (80f/100f);
			Double ceil = Math.ceil(Double.valueOf(calculatedAmt));
			amt = String.valueOf(ceil.intValue());
		}
		
		return amt;
	}
	
	public String compositePackageWithOverrideDeductionConsiderAmt(String netAmount) {
		String amt = "";
		if(netAmount != null && netAmount.length() > 0) {
			Integer calculatedAmt = SHAUtils.getFloatFromString(netAmount);
			Double ceil = Math.ceil(Double.valueOf(calculatedAmt));
			amt = String.valueOf(ceil.intValue());
		}
		return amt;
	}
	
	public String otherPackageWithOutRestrict(String netAmount, String roomRentAmtConsidered, String roomRentRequested) {
		String amt = "";
		if(netAmount != null && netAmount.length() > 0) {
			Integer floatFromString = SHAUtils.getFloatFromString(roomRentRequested);
			Float calculatedAmt = 0.0f ;
			if (floatFromString > 0) {
				calculatedAmt = SHAUtils.getFloatFromString(netAmount) * (SHAUtils.getFloatFromString(roomRentAmtConsidered).floatValue()/ floatFromString.floatValue());	
			}
			else {
				calculatedAmt = SHAUtils.getFloatFromString(netAmount).floatValue() * (SHAUtils.getFloatFromString(roomRentAmtConsidered)).floatValue();
			}
			Double ceil = Math.ceil(Double.valueOf(calculatedAmt));
			amt = String.valueOf(ceil.intValue());
			return amt;
		}
		
		return amt;
	}
	
	public String otherPackageWithRestrict(String netAmount, String roomRentAmtConsidered, String roomRentRequested) {
		String amt = "";
		if(netAmount != null && netAmount.length() > 0) {
			Float reducedAmt = SHAUtils.getFloatFromString(netAmount) * (80f/100f);
			Double ceil = Math.ceil(Double.valueOf(reducedAmt));
			amt = String.valueOf(ceil.intValue());
			
			Integer floatFromString = SHAUtils.getFloatFromString(roomRentRequested);
			Float calculatedAmt = 0.0f;
			if (floatFromString > 0) {
				calculatedAmt = SHAUtils.getFloatFromString(amt) * (SHAUtils.getFloatFromString(roomRentAmtConsidered).floatValue()/floatFromString.floatValue());	
			}
			else {
				calculatedAmt = SHAUtils.getFloatFromString(amt).floatValue() * (SHAUtils.getFloatFromString(roomRentAmtConsidered).floatValue());
			}
			Double ceilAmt = Math.ceil(Double.valueOf(calculatedAmt));
			amt = String.valueOf(ceilAmt.intValue());
		}
		return amt;
	}
	
	public String otherDetailsNetAmountCalculation(String amountRequested , String deductibleAmt) {
		String amt = "";
		if(amountRequested != null && amountRequested.length() > 0) 
		{
			Integer calculatedAmt = 0;
			if (SHAUtils.isValidFloat(deductibleAmt))
			{
				calculatedAmt = (SHAUtils.getFloatFromString(amountRequested) - SHAUtils.getFloatFromString(deductibleAmt));
			}
			else
			{
				calculatedAmt = SHAUtils.getFloatFromString(amountRequested);
			}
			Double ceil = Math.ceil(Double.valueOf(calculatedAmt));
			amt = String.valueOf(ceil.intValue());
		}
		return amt;
	}
	
	public String otherPackageNetAmount(String amountRequested , String deductibleAmt) {
		String amt = "";
		Integer calculatedAmt = (SHAUtils.getFloatFromString(amountRequested) - SHAUtils.getFloatFromString(deductibleAmt));
		Double ceil = Math.ceil(Double.valueOf(calculatedAmt));
		amt = String.valueOf(ceil.intValue());
		return amt;
	}
	
	public String getTotal(String enteredValue, String existingValue) {
		String amt = enteredValue;
		if(existingValue != null && existingValue.toString().length() > 0 && enteredValue != null && enteredValue.toString().length() > 0) {
			Integer calculatedAmt = (SHAUtils.getFloatFromString(existingValue) +  SHAUtils.getFloatFromString(enteredValue));
			Double ceil = Math.ceil(Double.valueOf(calculatedAmt));
			amt = String.valueOf(ceil.intValue());
		}
		return amt;
	}
	
	
}
