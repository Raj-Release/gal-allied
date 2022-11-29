package com.shaic.claim.intimation.rule;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;

public class IntimationRule {
	
	enum HospitalType
	{
		NETWORK {
			@Override
			public String toString() {
				return "NETWORK";
			}
		},
		NONNETWORK{
			@Override
			public String toString() {
				return "NONNETWORK";
			}
		},
		NOTREGISTERED{
			@Override
			public String toString(){
				return "NOTREGISTERED";
			}
		}
	}
	
	enum CliamType
	{
		CASHLESS {
			@Override
			public String toString() {
				return "CASHLESS";
			}
		},
		REIMBURSEMENT{
			@Override
			public String toString() {
				return "REIMBURSEMENT";
			}
		}
	}
	
	enum INTIMATION_STATUS
	{
		Rejected, Closed, Disbursed, Saved 
	}
	
	public IntimationRule()
	{
		
	}
	
	public boolean isClaimExist(Intimation intimation)
	{
		Boolean isClaimExist = true;
		 for (INTIMATION_STATUS  value : INTIMATION_STATUS.values()) {
		       if(StringUtils.equalsIgnoreCase(value.name(), intimation.getStatus().getProcessValue())) {
		    	   isClaimExist = false;
		       }
		    }
		return isClaimExist;
	}
	
	public boolean isPolicyValid(Intimation intimation)
	{
		Date currentDate = new Date();
		Date policyFrmDate = intimation.getPolicy().getPolicyFromDate();
		Date policyToDate = intimation.getPolicy().getPolicyToDate();

		return currentDate.after(policyFrmDate) && policyFrmDate.before(policyToDate);
	}
	
	public boolean isSumInsuredPositive(Intimation intimation)
	{
		//Double sumInsured = intimation.getPolicy().getInsuredSumInsured();
		Double sumInsured = intimation.getInsured().getInsuredSumInsured();
		if(sumInsured != null){
		if(sumInsured > 0){
			return true;
		}
		}
		
		return false;
	}
	
	public String getCliamType(Intimation intimation)
	{
		Long hospitalTypeId = intimation.getHospital() != null && intimation.getHospitalType() != null ? intimation.getHospitalType().getKey() : null;
		String claimType = null;
		
		if(hospitalTypeId == ReferenceTable.NETWORK_HOSPITAL_TYPE_ID)
		{
			claimType = CliamType.CASHLESS.toString();	
		}
		else
		{
			claimType = CliamType.REIMBURSEMENT.toString();	
		}
	
	    return claimType;
		
	}
	public String getHospitalType(Intimation intimation)
	{
		String hospitalType = intimation.getHospital() != null && intimation.getHospitalType().getValue() != null ?intimation.getHospitalType().getValue() :"";
		
		if(StringUtils.equalsIgnoreCase(hospitalType,"Network"))
		{
			hospitalType = HospitalType.NETWORK.toString();	
		}
		else if(StringUtils.equalsIgnoreCase(hospitalType,"Non-Network"))
		{
			hospitalType = HospitalType.NONNETWORK.toString();	
		}
		else
		{
			hospitalType = HospitalType.NOTREGISTERED.toString();
		}
		
		return hospitalType;
	}
	
	public String getHospitalTypeForPremia(Intimation intimation)
	{
		String hospitalType = null;
		Long hospitalTypeId = intimation.getHospitalType().getKey();
		if(hospitalTypeId.equals( ReferenceTable.NETWORK_HOSPITAL_TYPE_ID))
		{
			hospitalType = HospitalType.NETWORK.toString();
		}
		else
		{
			hospitalType = HospitalType.NONNETWORK.toString();	
		}
		return hospitalType;
	}
}
