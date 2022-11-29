package com.shaic.claim;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reports.claimsdailyreportnew.ClaimsDailyReportDto;
import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class RevisedClaimMapper {
	
	static RevisedClaimMapper myObj;
	
	private static MapperFacade mapper;
	private static MapperFacade repMapper;
	
	 public static void getAllMapValues()  { 
	
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		MapperFactory mapperFactory1 = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, ClaimDto> classMap = mapperFactory.classMap(Claim.class, ClaimDto.class);
		ClassMapBuilder<Claim, ClaimsDailyReportDto> classMap1 = mapperFactory1.classMap(Claim.class, ClaimsDailyReportDto.class);
				
		classMap.field("intimation.intimationId","newIntimationDto.intimationId");
		classMap.field("intimation.intimationMode.key", "newIntimationDto.modeOfIntimation.id");
		classMap.field("intimation.intimationMode.value", "newIntimationDto.modeOfIntimation.value");
		classMap.field("intimation.intimatedBy.key", "newIntimationDto.intimatedBy.id");
		classMap.field("intimation.intimatedBy.value", "newIntimationDto.intimatedBy.value");
		classMap.field("intimation.callerMobileNumber", "newIntimationDto.callerContactNum");
		classMap.field("intimation.callerLandlineNumber","newIntimationDto.callerLandlineNum");
		classMap.field("intimation.attendersMobileNumber", "newIntimationDto.attenderContactNum");
		classMap.field("intimation.attendersLandlineNumber","newIntimationDto.attenderLandlineNum");
		classMap.field("intimation.admissionReason", "newIntimationDto.reasonForAdmission");
		classMap.field("intimation.insured.key", "newIntimationDto.insuredPatient.key");
		classMap.field("intimation.intimaterName", "newIntimationDto.intimaterName");
		classMap.field("intimation.insuredPatientName", "newIntimationDto.insuredPatientName");
		classMap.field("intimation.status", "newIntimationDto.status");
		classMap.field("intimation.relapseofIllness", "newIntimationDto.relapseofIllnessValue");
		classMap.field("intimation.createdDate", "newIntimationDto.createdDate");
		classMap.field("intimation.key", "newIntimationDto.key");
		classMap.field("intimation.patientNotCovered", "newIntimationDto.newBornFlag");
		classMap.field("intimation.createdBy","newIntimationDto.createdBy");
		classMap.field("intimation.registrationStatus","newIntimationDto.registrationStatus");
		
		classMap.field("intimation.claimType.key","newIntimationDto.claimType.id");
		classMap.field("intimation.claimType.value","newIntimationDto.claimType.value");		
		
		 classMap.field("intimation.hospitalType.value", "newIntimationDto.hospitalType.value");
		 classMap.field("intimation.hospitalType.key","newIntimationDto.hospitalType.id");         
		 classMap.field("intimation.hospitalType.key", "newIntimationDto.hospitalDto.hospitalType.id");
		 classMap.field("intimation.hospitalType.value", "newIntimationDto.hospitalDto.hospitalType.value");
		 classMap.field("intimation.hospital", "newIntimationDto.hospitalDto.key");
		
		 classMap.field("intimation.admissionDate", "newIntimationDto.admissionDate");
		 classMap.field("intimation.admissionType.key", "newIntimationDto.admissionType.id");
		 classMap.field("intimation.admissionType.value", "newIntimationDto.admissionType.value");
		 classMap.field("intimation.roomCategory.key", "newIntimationDto.roomCategory.id");
		 classMap.field("intimation.roomCategory.value", "newIntimationDto.roomCategory.value");
		 classMap.field("intimation.inpatientNumber", "newIntimationDto.inpatientNumber");
		 classMap.field("intimation.hospitalComments", "newIntimationDto.comments");
		 classMap.field("intimation.lateIntimationReason", "newIntimationDto.lateIntimationReason");
		 
		 classMap.field("intimation.managementType.key", "newIntimationDto.managementType.id");
		 classMap.field("intimation.managementType.value", "newIntimationDto.managementType.value");
		 
		 classMap.field("intimation.doctorName", "newIntimationDto.doctorName");
		
		
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
		 classMap.field("coadingFlag","coadingFlag");
		 classMap.field("coadingUser","coadingUser");
		 classMap.field("coadingDate","coadingDate");
		 classMap.field("coadingRemark","coadingRemark");	
		 classMap.field("intimation.policy.totalSumInsured","newIntimationDto.orginalSI");
		 
		 // For Claims Daily Report
		 
		 classMap1.field("intimation.policy.policyNumber", "policyNumber");
		 classMap1.field("intimation.policy.proposerFirstName", "mainMemberName");
		 classMap1.field("intimation.policy.proposerFirstName", "insuredProposerName");
		 classMap1.field("intimation.policy.product.value",	"productName");
		 classMap1.field("intimation.createdDate", "intimationDate");
		 classMap1.field("intimation.admissionDate", "hospitalDate");
		 classMap1.field("intimation.intimationId", "intimationNo");
		 classMap1.field("intimation.insured.insuredName", "patientName");
		 classMap1.field("intimation.insured.insuredAge", "patientAge");
		 classMap1.field("intimation.hospitalType.value", "hospitalType");		 	
		 classMap1.field("intimation.cpuCode.cpuCode", "CPUcode");
		 classMap1.field("intimation.admissionReason", "admissionReason");
		 classMap1.field("intimation.callerLandlineNumber", "callerContactNo");					 
		 classMap1.field("claimType.value", "cashlessOrReimbursement");						 
		 classMap1.field("intimation.registrationStatus", "registrationStatus"); 	
		 classMap1.field("intimation.admissionType.value", "plannedAdmission");
		 classMap1.field("provisionAmount", "initialProvisionAmt");                              
		 classMap1.field("currentProvisionAmount", "provisionAmt");	
		 
		 
		 classMap.register();
		 		 
		 classMap1.register();
		 mapper = mapperFactory.getMapperFacade();
		 repMapper = mapperFactory1.getMapperFacade();
		 
	}
	
	public Claim getClaim(ClaimDto claimDto) {
		Claim dest = mapper.map(claimDto, Claim.class);
		return dest;
	}
	
	public static ClaimDto getClaimDto(Claim claim) {
		ClaimDto dest = mapper.map(claim, ClaimDto.class);
		dest.getNewIntimationDto().setPolicy(claim.getIntimation().getPolicy());
		dest.getNewIntimationDto().setInsuredPatient(claim.getIntimation().getInsured());
		dest.getNewIntimationDto().setCpuCode(claim.getIntimation().getCpuCode().getCpuCode());
		dest.getNewIntimationDto().setCpuId(claim.getIntimation().getCpuCode().getKey());
		dest.getNewIntimationDto().setCpuAddress(claim.getIntimation().getCpuCode().getAddress());
		return dest;
	}
	
	public static ClaimsDailyReportDto getClaimsDailyReportDto(Claim claim) {
		ClaimsDailyReportDto dest = repMapper.map(claim, ClaimsDailyReportDto.class);
		
		return dest;
	}
	
	
	public static RevisedClaimMapper getInstance(){
        if(myObj == null){
            myObj = new RevisedClaimMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
}
