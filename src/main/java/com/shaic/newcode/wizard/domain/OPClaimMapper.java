package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.ClaimDto;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OPClaim;

public class OPClaimMapper {
	
	private static MapperFacade mapper;
	
	static OPClaimMapper myObj;
	
	public static void getAllMapValues() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OPClaim, ClaimDto> classMap = mapperFactory.classMap(OPClaim.class, ClaimDto.class);
		
		classMap.field("key", "key");
		classMap.field("intimation.key","intimationKey");
		classMap.field("intimation.key","newIntimationDto.key");
		classMap.field("claimId", "claimId");
		classMap.field("claimLink", "claimLink");
		classMap.field("claimType.key", "claimType.id");
		classMap.field("claimType.value", "claimType.value");
		classMap.field("claimedAmount", "claimedAmount");
		classMap.field("claimedHomeAmount","claimedHomeAmount");
		classMap.field("claimedamountCurrencyId", "claimedamountCurrencyId");
		classMap.field("claimedhomeamountCurrencyId","claimedhomeamountCurrencyId");
		classMap.field("conversionFlag", "conversionFlag");
		classMap.field("conversionLetter", "conversionLetter");
		classMap.field("conversionReason.key", "conversionReason.id");
		classMap.field("conversionReason.value", "conversionReason.value");
		classMap.field("createdBy", "createdBy");
		classMap.field("createdDate","createdDate");
		classMap.field("status.key", "statusId");
		classMap.field("status.processValue","statusName");
		classMap.field("stage.key", "stageId");
		classMap.field("stage.stageName", "stageName");
//		classMap.field("stage.sequenceNumber","stageSequenceNumber");
		classMap.field("isVipCustomer","isVipCustomer");
		classMap.field("officeCode", "officeCode");
		classMap.field("latestPreauthKey", "latestPreauthKey");
		classMap.field("dataOfAdmission", "newIntimationDto.admissionDate");
		
		classMap.field("dataOfAdmission", "admissionDate");		
		classMap.field("dataOfDischarge", "dischargeDate");
		
		classMap.field("proamountCurrencyId", "proamountCurrencyId");
		classMap.field("prohomeamountCurrencyId", "prohomeamountCurrencyId");
		classMap.field("provisionAmount", "provisionAmount");
		classMap.field("currentProvisionAmount", "currentProvisionAmount");
		classMap.field("provisionHomeAmount", "provisionHomeAmount");
		
		classMap.field("rejectionLetterflag", "rejectionLetterflag");
		
		
		// classMap.field("substatusId", "substatusId");
		// classMap.field("statusDate", "statusDate");
		 classMap.field("currencyId.key", "currencyId.id");
		 classMap.field("currencyId.value", "currencyId.value");
		 classMap.field("rejectionCategoryId.key", "rejectionCategoryId.id");
		 classMap.field("rejectionCategoryId.value", "rejectionCategoryId.value");
		 classMap.field("registrationRemarks", "registrationRemarks");
		 classMap.field("suggestedRejectionRemarks", "suggestedRejectionRemarks");
		 classMap.field("rejectionRemarks", "rejectionRemarks");
		 classMap.field("waiverRemarks", "waiverRemarks");
		 classMap.field("medicalRemarks", "medicalRemarks");
		 classMap.field("doctorNote", "doctorNote");
		 classMap.field("legalFlag", "legalFlag");
		 classMap.field("claimSectionCode", "claimSectionCode");
		 classMap.field("claimCoverCode", "claimCoverCode");
		 classMap.field("claimSubCoverCode", "claimSubCoverCode");
		 
		 classMap.field("productCode", "productCode");
		 classMap.field("productName", "productName");
		 classMap.field("lossDateTime", "lossDateTime");
		 classMap.field("dollarInitProvisionAmount", "dollarInitProvisionAmount");
		 classMap.field("hospitalisationFlag", "hospitalisationFlag");
		 classMap.field("nonHospitalisationFlag", "nonHospitalisationFlag");
		 classMap.field("inrConversionRate", "inrConversionRate");
		 classMap.field("inrTotalAmount", "inrTotalAmount");
		 classMap.field("hospitalisationFlag", "hospitalisationFlag");
		 classMap.field("hospitalName", "hospitalName");
		 classMap.field("cityName", "cityName");
		 classMap.field("countryId", "countryId");
		 classMap.field("event.eventCode","eventCode");
		 classMap.field("event.eventDescription","eventDescription");
		 classMap.field("ailmentLoss", "ailmentLoss");
		 classMap.field("confirmRejectionRemarks", "confirmRejectionRemarks");
		 classMap.field("event.key", "eventCodeValue.id");
		 classMap.register();
		 
		 mapper = mapperFactory.getMapperFacade();
	}
	
	public OPClaim getClaim(ClaimDto claimDto) {
		OPClaim dest = mapper.map(claimDto, OPClaim.class);
		return dest;
	}
	
	public ClaimDto getClaimDto(OPClaim claim) {
		ClaimDto dest = mapper.map(claim, ClaimDto.class);
		return dest;
	}
	
	public static OPClaimMapper getInstance(){
        if(myObj == null){
            myObj = new OPClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }
		
}
