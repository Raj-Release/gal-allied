package com.shaic.claim;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.OMPClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ClaimMapper {
	private static MapperFacade mapper;
	
	
	static ClaimMapper myObj;
	
	public static void getAllMapValues() 	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, ClaimDto> classMap = mapperFactory.classMap(Claim.class, ClaimDto.class);
		
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
		classMap.field("ghiAllowUser", "ghiAllowUser");
		classMap.field("ghiAllowFlag","ghiAllowFlag");
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
		 
		classMap.field("claimSectionCode", "sectionCode");
		classMap.field("claimCoverCode", "coverCode");
		classMap.field("claimSubCoverCode", "subCoverCode");
		classMap.field("incidenceFlag", "incidenceFlagValue");
		classMap.field("incidenceDate", "incidenceDate");
		classMap.field("lobId", "lobId");
		classMap.field("processClaimType", "processClaimType");
		classMap.field("hospReqFlag", "hospReqFlag");
		classMap.field("injuryRemarks", "injuryRemarks");						
						
		 classMap.field("claimSectionCode", "claimSectionCode");
		 classMap.field("claimCoverCode", "claimCoverCode");
		 classMap.field("claimSubCoverCode", "claimSubCoverCode");
		 classMap.field("accidentDate", "accidentDate");
		 classMap.field("deathDate", "deathDate");
		 classMap.field("disablementDate", "disablementDate");
		 
		 classMap.field("gpaParentName", "gpaParentName");
		 classMap.field("gpaParentDOB", "gpaParentDOB");
		 classMap.field("gpaParentAge", "gpaParentAge");
		 classMap.field("gpaRiskName", "gpaRiskName");
		 classMap.field("gpaRiskDOB", "gpaRiskDOB");
		 classMap.field("gpaRiskAge", "gpaRiskAge");
		 classMap.field("gpaCategory", "gpaCategory");
		 classMap.field("gpaSection", "gpaSection");
		 

		 
		 classMap.field("firstReminderDate","firstReminderDate");
		 classMap.field("secondReminderDate","secondReminderDate");
		 classMap.field("thirdReminderDate","thirdReminderDate");
		 classMap.field("modifiedDate","modifiedDate");
		 classMap.field("coadingFlag","coadingFlag");
		 classMap.field("coadingUser","coadingUser");
		 classMap.field("coadingDate","coadingDate");
		 classMap.field("coadingRemark","coadingRemark");
		 classMap.field("legalClaim", "legalClaim");
		 classMap.field("documentReceivedDate", "documentReceivedDate");
		 classMap.field("claimPriorityLabel","claimPriorityLabel");
		 classMap.register();
		 
		 mapper = mapperFactory.getMapperFacade();
	}
	
	public Claim getClaim(ClaimDto claimDto) {
		Claim dest = mapper.map(claimDto, Claim.class);
		return dest;
	}
	
	public ClaimDto getClaimDto(Claim claim) {
		ClaimDto dest = mapper.map(claim, ClaimDto.class);
		dest.setIncidenceFlagValue(claim.getIncidenceFlag());
		return dest;
	}
	
	public static ClaimMapper getInstance(){
        if(myObj == null){
            myObj = new ClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	public ClaimDto getClaimDto(OMPClaim claim) {
		ClaimDto dest = mapper.map(claim, ClaimDto.class);
		return dest;
	}
}
