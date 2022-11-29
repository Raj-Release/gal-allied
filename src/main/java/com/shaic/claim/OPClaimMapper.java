package com.shaic.claim;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OPClaim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class OPClaimMapper {
	private static MapperFacade mapper;
	
	
	static OPClaimMapper myObj;
	
	public static void getAllMapValues() 	{
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
		 
		 classMap.field("intimation.insured.insuredName","newIntimationDto.insuredPatientName");
		 classMap.field("intimation.insured.insuredName", "newIntimationDto.patientName");
		 classMap.field("intimation.admissionDate","newIntimationDto.admissionDate");
		 classMap.field("intimation.policy.policyYear", "newIntimationDto.policyYear");
		 classMap.field("status.processValue","claimStatus");
		 classMap.field("intimation.policy.policyNumber","newIntimationDto.policyNumber");
//		 classMap.field("intimation.intimationId","newIntimationDto.intimationNumber");
		 classMap.register();
		 
		 mapper = mapperFactory.getMapperFacade();
	}
	
	public OPClaim getClaim(ClaimDto claimDto) {
		OPClaim dest = mapper.map(claimDto, OPClaim.class);
		return dest;
	}
	
	public ClaimDto getClaimDto(OPClaim claim) {
		ClaimDto dest = mapper.map(claim, ClaimDto.class);
//		dest.setIncidenceFlagValue(claim.getIncidenceFlag());
		return dest;
	}
	
	public List<PreviousClaimsTableDTO> getPreviousClaimDTOListOP(List<OPClaim> claimList) {
		List<PreviousClaimsTableDTO> mapAsList = mapper.mapAsList(claimList, PreviousClaimsTableDTO.class);
		return mapAsList;
	}
	
	public static OPClaimMapper getInstance(){
        if(myObj == null){
            myObj = new OPClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}
