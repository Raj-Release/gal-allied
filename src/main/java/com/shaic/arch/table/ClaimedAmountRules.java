package com.shaic.arch.table;

import java.io.Serializable;

import com.shaic.arch.SHAUtils;

public class ClaimedAmountRules implements Serializable {

	private static final long serialVersionUID = -8305035596264005460L;

	public static Integer getClaimedNetAmount(String deductibleAmount, String claimedAmount) {
		return SHAUtils.getIntegerFromString(claimedAmount) - SHAUtils.getIntegerFromString(deductibleAmount);
	}
	
	public static Integer perDayMinAmount(String productPerDayAmt, String claimedNetAmount, String claimedDays) {
		if(SHAUtils.convertFloatToString(claimedDays) != 0) {
			Float calculatedAmt  = SHAUtils.getIntegerFromString(claimedNetAmount).floatValue() / SHAUtils.convertFloatToString(claimedDays);
			if(SHAUtils.getIntegerFromString(productPerDayAmt) == 0) {
				Integer round = Math.round(calculatedAmt);
				return round;
			}
			Integer round = Math.round(calculatedAmt);
			return Math.min(SHAUtils.getIntegerFromString(productPerDayAmt), round);
		}
		return 0;
	}
	
	public static Integer getPayableAmount(String productDays, String perDayMinAmount) {
		Float amt = SHAUtils.getIntegerFromString(perDayMinAmount) * SHAUtils.convertFloatToString(productDays);
		return Math.round(amt);
	}
	
	public static Integer getNotPayableAmount(String payableAmount, String ClaimedNetAmount) {
		return SHAUtils.getIntegerFromString(ClaimedNetAmount) - SHAUtils.getIntegerFromString(payableAmount);
	}
	
	public static Integer otherDetailsNetAmount(String claimedAmount , String deductibleAmt) {
		String amt = "";
		Integer calculatedAmt = (SHAUtils.getIntegerFromString(claimedAmount) - SHAUtils.getIntegerFromString(deductibleAmt));
		return calculatedAmt;
	}
	
	public static Integer ProrataCalculation(String roomRentAmount, String NetAmount, Boolean prorataFlag) {
		Float calculatedAmount = new Float(roomRentAmount) * SHAUtils.getIntegerFromString(NetAmount);
		int roundedValue = Math.round(calculatedAmount);
		if(prorataFlag) {
			return SHAUtils.getIntegerFromString(NetAmount);
		}
		return roundedValue;
	}
	
	
	public static Integer compositePackageWithoutOverrideDeductionConsiderAmt(String netAmount) {
		Float calculatedAmt = SHAUtils.getIntegerFromString(netAmount) * (80f/100f);
		int roundedValue = Math.round(calculatedAmt);
		
		return roundedValue;
	}
	
	public static Integer ambulancePayableAmount(String netAmount, String balanceAmount) {
		int min = Math.min(SHAUtils.getIntegerFromString(balanceAmount), SHAUtils.getIntegerFromString(netAmount));
		
		return min;
	}
	
	public static Integer ambulancePayableAmountForPA(String netAmount, String balanceAmount) {
		if(SHAUtils.getIntegerFromString(balanceAmount).equals(0)) {
			return SHAUtils.getIntegerFromString(netAmount);
		}
		int min = Math.min(SHAUtils.getIntegerFromString(balanceAmount), SHAUtils.getIntegerFromString(netAmount));
		
		return min;
	}
	
	public static Integer compositePackageWithOverrideDeductionConsiderAmt(String netAmount) {
		Integer calculatedAmt = SHAUtils.getIntegerFromString(netAmount);
		int roundedValue = Math.round(calculatedAmt);
		return roundedValue;
	}
	
	public static Integer otherPackageWithOutRestrict(String roomRentAmt, String netAmount, Boolean prorataFlag) {
		Float calculatedAmount = new Float(roomRentAmt) * SHAUtils.getIntegerFromString(netAmount);
		int roundedValue = Math.round(calculatedAmount);
		if(prorataFlag) {
			return SHAUtils.getIntegerFromString(netAmount);
		}
		return roundedValue;
	}
	
	public static Integer otherPackageWithRestrict(String roomRentAmt, String netAmount, Boolean prorataFlag) {
		Float reducedAmt = SHAUtils.getFloatFromString(netAmount) * (80f/100f);
		int ceil = Math.round(reducedAmt);
		if(prorataFlag) {
			return ceil;
		}
		Double calculatedAmount = new Double(ceil) * (SHAUtils.isValidFloat(roomRentAmt) ? new Float(roomRentAmt) : 0f);
		int roundedValue = Math.round(calculatedAmount.floatValue());
			
		return roundedValue;
	}
}
