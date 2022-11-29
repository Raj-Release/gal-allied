package com.shaic.restservices.bancs.policydetails;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.policy.search.ui.PremInsuredDetails;
import com.shaic.claim.policy.search.ui.PremInsuredNomineeDetails;
import com.shaic.claim.policy.search.ui.PremPolicyCoverDetails;
import com.shaic.claim.policy.search.ui.PremPreviousPolicyDetails;
import com.shaic.domain.Insured;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyCoverDetails;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PreviousPolicy;

public class GetPolicyDetailsDBService  {
	@PersistenceContext
	protected  EntityManager entityManager;
	 DateFormat df = new SimpleDateFormat("dd/mm/yyyy");

	
	@SuppressWarnings("unchecked")
	public  PolicyNominee  SetPolicyNomineeDetails (PremInsuredNomineeDetails policyNomineeDetailsObj)  throws ParseException
	{
		PolicyNominee policynomineeDetails = new PolicyNominee();
		if(!isNullOrEmpty(policyNomineeDetailsObj.getAppointeeAge()))
		{
		policynomineeDetails.setAppointeeAge(Integer.parseInt(policyNomineeDetailsObj.getAppointeeAge()));
		}
		else
		{
			policynomineeDetails.setAppointeeAge(0);
		}
		policynomineeDetails.setAppointeeName(policyNomineeDetailsObj.getAppointeeName());
		policynomineeDetails.setAppointeeRelationship(policyNomineeDetailsObj.getAppointeeRelationship());
		if(!isNullOrEmpty(policyNomineeDetailsObj.getNomineeAge()))
		{
		policynomineeDetails.setNomineeAge(Integer.parseInt(policyNomineeDetailsObj.getNomineeAge()));
		}
		else
		{
			policynomineeDetails.setNomineeAge(0);
		}
		policynomineeDetails.setNomineeName(policyNomineeDetailsObj.getNomineeName());
		policynomineeDetails.setNomineeDob(df.parse(policyNomineeDetailsObj.getNomineeDob()));
		if(!isNullOrEmpty(policyNomineeDetailsObj.getNomineePercentage()))
		{
		policynomineeDetails.setSharePercent(Double.parseDouble(policyNomineeDetailsObj.getNomineePercentage()));
		}
		else
		{
			policynomineeDetails.setSharePercent(0.0);
		}
		policynomineeDetails.setRelationshipWithProposer(policyNomineeDetailsObj.getNomineeRelationship());
		policynomineeDetails.setCreatedBy("Test");
		entityManager.persist(policynomineeDetails);
		entityManager.flush();
		return policynomineeDetails;
	}
	@SuppressWarnings("unchecked")
	public PolicyCoverDetails SetPolicyCoverDetailsValue (PremPolicyCoverDetails coverobj) throws ParseException
	{
		PolicyCoverDetails coverData=new PolicyCoverDetails();
		coverData.setCoverCode(coverobj.getCoverCode());
//		coverData.setCoverCodeDescription(coverobj.getCoverDescription());
		if(!isNullOrEmpty(coverobj.getRiskId()))
		{
			coverData.setRiskId(Long.parseLong(coverobj.getRiskId()));
		}
		else
		{
			long l=0;
			coverData.setRiskId(l);
		}
		if(!isNullOrEmpty(coverobj.getSumInsured()))
		{
			coverData.setSumInsured(Double.parseDouble(coverobj.getSumInsured()));
		}
		else
		{
			coverData.setSumInsured(0.0);
		}
		coverData.setSumInsured(Double.parseDouble(coverobj.getSumInsured()));
		entityManager.persist(coverData);
		entityManager.flush();
		return coverData;
		
	}
	@SuppressWarnings("unchecked")
	public Insured SetPolicyInsuredDetailsValue (PremInsuredDetails insureobj)  throws ParseException
	{
		Insured insured = new Insured();
		insured.setHospitalCashBenefits(insureobj.getAddBenefitsHospCash());
		insured.setPatientCareBenefits(insureobj.getAddBenefitsPatientCare());
		insured.setStrInsuredAge(insureobj.getInsuredAge());
		if(!isNullOrEmpty(insureobj.getEntryAge()))
		{
		insured.setEntryAge(Integer.parseInt(insureobj.getEntryAge()));
		}
		else
		{
			insured.setEntryAge(0);
		}
		insured.setCopay(Long.parseLong(insureobj.getCoPay()));
		insured.setInsuredDateOfBirth(SHAUtils.getDateFormatDate(insureobj.getDob()));
		if(!isNullOrEmpty(insureobj.getDeductiableAmt()))
		{
			insured.setDeductibleAmount(Double.parseDouble(insureobj.getDeductiableAmt()));
		}
		else
		{
			insured.setDeductibleAmount(0.0);
		}
		insured.setHealthCardNumber(insureobj.getHealthCardNo());
		if(!isNullOrEmpty(insureobj.getCumulativeBonus()))
		{
			insured.setCummulativeBonus(Double.parseDouble(insureobj.getCumulativeBonus()));
		}
		else
		{
			insured.setCummulativeBonus(0.0);
		}
		insured.setInsuredName(insureobj.getInsuredName());
		//insured.setNomineeDetails();
		//insured.setInsuredPedList();
		insured.setPolicyYear(insureobj.getPolicyYear());
		//insured.setPortablityPolicy();
		//insured.setPortablityPrevPolicy();		
		insured.setRelationshipCode(insureobj.getRelation());
		if(!isNullOrEmpty(insureobj.getRiskSysId()))
		{
			insured.setInsuredId(Long.parseLong(insureobj.getRiskSysId()));
		}
		else
		{
			long l=0;
			insured.setInsuredId(l);
		}
		//insured.setSumInsured1();
		//insured.setSumInsured2();
		//insured.setSumInsured3();
		//insured.setSection2SI();
		if(!isNullOrEmpty(insureobj.getSumInsured()))
		{
			insured.setInsuredId(Long.parseLong(insureobj.getRiskSysId()));
		}
		else
		{
			insured.setInsuredSumInsured(0.0);
		}
		entityManager.persist(insured);
		entityManager.flush();
		return insured;
	}
	
	@SuppressWarnings("unchecked")
	public PreviousPolicy SetPreviousPolicyDetails (PremPreviousPolicyDetails PreviousPolicyDetailsObj)  throws ParseException
	{
		PreviousPolicy previousPolicy = new PreviousPolicy(); 
		
		previousPolicy.setIssuingOfficeCode(PreviousPolicyDetailsObj.getOfficeCode());
		if(!isNullOrEmpty(PreviousPolicyDetailsObj.getPolicyFromDate().toString()))
		{
		previousPolicy.setPolicyFrmDate(SHAUtils.getDateFormatDate(PreviousPolicyDetailsObj.getPolicyFromDate()));
		}
		else
		{
			previousPolicy.setPolicyFrmDate(null);
		}
		previousPolicy.setPolicyNumber(PreviousPolicyDetailsObj.getPolicyNo());
		if(!isNullOrEmpty(PreviousPolicyDetailsObj.getPolicyToDate()))
		{
			previousPolicy.setPolicyToDate(SHAUtils.getDateFormatDate(PreviousPolicyDetailsObj.getPolicyToDate()));

		}
		else
		{
			previousPolicy.setPolicyToDate(null);
		}
		if(!isNullOrEmpty(PreviousPolicyDetailsObj.getPolicyUWYear()))
		{
			previousPolicy.setUnderWritingYear(Long.parseLong(PreviousPolicyDetailsObj.getPolicyUWYear()));
		}
		else
		{
			long l=0;
			previousPolicy.setUnderWritingYear(l);
		}
		if(!isNullOrEmpty(PreviousPolicyDetailsObj.getPremium()))
		{
			previousPolicy.setPremium(Double.parseDouble(PreviousPolicyDetailsObj.getPremium()));
		}
		else
		{
			previousPolicy.setPremium(0.0);
		}
		previousPolicy.setPreviousInsurerName(PreviousPolicyDetailsObj.getPreviousInsurerName());
		previousPolicy.setProductCode(PreviousPolicyDetailsObj.getProductCode());
		previousPolicy.setProductName(PreviousPolicyDetailsObj.getProductName());
		previousPolicy.setProposerAddress(PreviousPolicyDetailsObj.getProposerAddress());
		previousPolicy.setProposerCode(PreviousPolicyDetailsObj.getProposerCode());
		previousPolicy.setProposerEmailId(PreviousPolicyDetailsObj.getProposerEmail());
		previousPolicy.setProposerFaxNo(PreviousPolicyDetailsObj.getProposerFaxNo());
		previousPolicy.setProposerName(PreviousPolicyDetailsObj.getProposerName());
		previousPolicy.setProposerTelephoneNo(PreviousPolicyDetailsObj.getProposerTelNo());
		if(!isNullOrEmpty(PreviousPolicyDetailsObj.getSumInsured()))
		{
		previousPolicy.setSumInsured(Double.parseDouble(PreviousPolicyDetailsObj.getSumInsured()));
		}
		else
		{
			previousPolicy.setSumInsured(0.0);
		}
		entityManager.persist(previousPolicy);
		entityManager.flush();
		return previousPolicy;
	}
	
	@SuppressWarnings("unchecked")
	public Policy SetPolicyDetails (Policy newPolicy)  throws ParseException
	{
		entityManager.persist(newPolicy);
		entityManager.flush();
		return newPolicy;
	}

	public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
        {
            return false;
        }
        else
        {
        return true;
        }
}
}