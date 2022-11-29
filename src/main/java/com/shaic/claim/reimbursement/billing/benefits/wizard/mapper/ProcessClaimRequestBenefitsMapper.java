/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.mapper;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.ReimbursementBenefitsDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * @author ntv.vijayar
 *
 */
public class ProcessClaimRequestBenefitsMapper {
	
	
	
	private static MapperFacade reimbursementBenefitsMapper;
	//private static MapperFacade reimbbenefitsDetailsMapper;
	private static MapperFacade benefitsDetailsMap;
	
	private static BoundMapperFacade<ReimbursementBenefits,AddOnBenefitsDTO> reimbbenefitsDetailsMapper;
	
	static ProcessClaimRequestBenefitsMapper myObj;
	
	
	
	 public static void getAllMapValues()  { 
		
		//Added for create rod screen.
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<ReimbursementBenefits, UploadDocumentDTO> benefitsDetailsMapper = mapperFactory.classMap(ReimbursementBenefits.class,UploadDocumentDTO.class);
		ClassMapBuilder<ReimbursementBenefitsDetails, PatientCareDTO> reimbbenefitsDetailsMap = mapperFactory.classMap(ReimbursementBenefitsDetails.class,PatientCareDTO.class);
		ClassMapBuilder<ReimbursementBenefits, AddOnBenefitsDTO> addOnbenefitsDetailsMap = mapperFactory.classMap(ReimbursementBenefits.class,AddOnBenefitsDTO.class);
		//Added for create ROD screen.
		
		benefitsDetailsMapper.field("benefitsFlag","hospitalBenefitFlag");
		benefitsDetailsMapper.field( "numberOfDaysBills","hospitalCashNoofDays");
		benefitsDetailsMapper.field("perDayAmountBills","hospitalCashPerDayAmt");
		benefitsDetailsMapper.field("totalClaimAmountBills","hospitalCashTotalClaimedAmt");
		benefitsDetailsMapper.field("treatmentForPhysiotherapy","treatmentPhysiotherapyFlag");
		benefitsDetailsMapper.field("key","hospitalBenefitKey");
		
		
		reimbbenefitsDetailsMap.field("engagedFrom" , "engagedFrom");
		reimbbenefitsDetailsMap.field("engagedTo" , "engagedTo");
		reimbbenefitsDetailsMap.field("key","key");
		
		addOnbenefitsDetailsMap.field("numberOfDaysEligible", "eligibleNoofDays");
		addOnbenefitsDetailsMap.field("numberOfDaysPayable", "eligiblePayableNoOfDays");
		addOnbenefitsDetailsMap.field("totalAmount", "eligibleNetAmount");
		addOnbenefitsDetailsMap.field("copayPercentage", "coPayPercentage.value");
		addOnbenefitsDetailsMap.field("copayAmount", "copayAmount");
		addOnbenefitsDetailsMap.field("netAmount", "netAmountAfterCopay");
		addOnbenefitsDetailsMap.field("payableAmount", "payableAmount");
		
		benefitsDetailsMapper.register();
		reimbbenefitsDetailsMap.register();
		addOnbenefitsDetailsMap.register();
		
		
		reimbursementBenefitsMapper =  mapperFactory.getMapperFacade();
		benefitsDetailsMap = mapperFactory.getMapperFacade();
		reimbbenefitsDetailsMapper = mapperFactory.getMapperFacade(ReimbursementBenefits.class, AddOnBenefitsDTO.class);
	}
	
	public static List<PatientCareDTO> getPatientCareDetails (List<ReimbursementBenefitsDetails> benefitsDetails)
	{
		List<PatientCareDTO> uploadDocs = benefitsDetailsMap.mapAsList(benefitsDetails, PatientCareDTO.class);
		return uploadDocs;
	}
	
	public static UploadDocumentDTO getUploadDocDTO (ReimbursementBenefits benefits)
	{
		UploadDocumentDTO uploadDocDTO = reimbursementBenefitsMapper.map(benefits, UploadDocumentDTO.class);
		return uploadDocDTO;
	}
	
	public static ReimbursementBenefits getAddOnBenefits (AddOnBenefitsDTO benefitsDTO)
	{
		ReimbursementBenefits reimbursementBenefits = reimbbenefitsDetailsMapper.mapReverse(benefitsDTO);
		return reimbursementBenefits;
	}
	
	public static ProcessClaimRequestBenefitsMapper getInstance(){
        if(myObj == null){
            myObj = new ProcessClaimRequestBenefitsMapper();
            getAllMapValues();
        }
        return myObj;
	 }


}

