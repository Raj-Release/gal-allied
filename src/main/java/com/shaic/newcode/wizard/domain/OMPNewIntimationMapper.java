package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPIntimation;
import com.shaic.domain.TmpCPUCode;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class OMPNewIntimationMapper {
	
	private static MapperFacade mapper;
	
	static OMPNewIntimationMapper myObj;
	
	public static void getAllValues() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true); 
		ClassMapBuilder<OMPIntimation, NewIntimationDto> classMap = mapperFactory.classMap(OMPIntimation.class, NewIntimationDto.class);
			
		classMap.mapNulls(true);
		classMap.mapNullsInReverse(true);
		classMap.field("intimationId","intimationId");
//		classMap.field("intimationMode.key", "modeOfIntimation.id");
//		classMap.field("intimationMode.value", "modeOfIntimation.value");
//		classMap.field("intimatedBy.key", "intimatedBy.id");
//		classMap.field("intimatedBy.value", "intimatedBy.value");
		
//		classMap.field("callerMobileNumber", "callerContactNum");//
//		classMap.field("callerLandlineNumber","callerLandlineNum");//
//		classMap.field("attendersMobileNumber", "attenderContactNum");//
//		classMap.field("attendersLandlineNumber","attenderLandlineNum");//
//		classMap.field("admissionReason", "reasonForAdmission");//
		
//		classMap.field("insured.key", "insuredPatient.key");
		classMap.field("intimaterName", "intimaterName");
		classMap.field("lobId", "lobId");
		classMap.field("insuredName", "insuredPatientName");
		classMap.field("status", "status");
//		classMap.field("relapseofIllness", "relapseofIllnessValue");//
		classMap.field("createdDate", "createdDate");
		classMap.field("key", "key");
		classMap.field("patientNotCovered", "newBornFlag");
		classMap.field("createdBy","createdBy");
		classMap.field("registrationStatus","registrationStatus");
		
		classMap.field("claimType.key","claimType.id");
		classMap.field("claimType.value","claimType.value");		
//		
//		 classMap.field("hospitalType.value", "hospitalType.value");
//		 classMap.field("hospitalType.key","hospitalType.id");         
//		 classMap.field("hospitalType.key", "hospitalDto.hospitalType.id");
//		 classMap.field("hospitalType.value", "hospitalDto.hospitalType.value");
//		 classMap.field("hospital", "hospitalDto.key");
		 classMap.field("policyYear", "policyYear");
//		 classMap.field("policy.productName", "productName");
		 
		 
//		 classMap.field("cpuCode.cpuCode", "cpuCode").mapNulls(true).mapNullsInReverse(true);//
//		 classMap.field("cpuCode.key", "cpuId").mapNulls(true).mapNullsInReverse(true);//
//		 classMap.field("cpuCode.key", "hospitalDto.cpuId").mapNulls(true).mapNullsInReverse(true);//
		 
		
		 classMap.field("admissionDate", "admissionDate");
		 classMap.field("dischargeDate", "dischargeDate");
//		 classMap.field("admissionType.key", "admissionType.id");//
//		 classMap.field("admissionType.value", "admissionType.value");//
//		 classMap.field("roomCategory.key", "roomCategory.id");//
//		 classMap.field("roomCategory.value", "roomCategory.value");//
//		 classMap.field("inpatientNumber", "inpatientNumber");//
		 classMap.field("hospitalComments", "comments");
//		 classMap.field("lateIntimationReason", "lateIntimationReason");//
//		 classMap.field("managementType.key", "managementType.id");//
//		 classMap.field("managementType.value", "managementType.value");//
//		 classMap.field("policy.policyNumber", "policyNumber");
//		 classMap.field("doctorName", "doctorName");//
		 
		 classMap.field("processClaimType", "processClaimType");
		 classMap.field("incidenceFlag", "incidenceFlag");
		 classMap.field("hospitalReqFlag", "hospitalReqFlag");
		 classMap.field("accountDeactivatedDate", "accountDeactivatedDate");
		 classMap.field("passportNumber", "passportNumber");
		 classMap.field("lossDateTime", "lossDateTime");
		 classMap.field("ailmentLoss", "ailmentLoss");
		 classMap.field("tpaIntimationNumber", "tpaIntimationNumber");
//		 classMap.field("event.eventCodeId", "eventCodeId");
		 classMap.field("hospitalizationFlag", "hospitalizationFlag");
		 classMap.field("nonHospitalizationFlag", "nonHospitalizationFlag");
		 classMap.field("placeVisit", "placeVisit");
		 classMap.field("placeLossDelay", "placeLossDelay");
		 classMap.field("sponsorName", "sponsorName");
		 classMap.field("countryId", "countryId");
		 classMap.field("remarks", "remarks");
		 classMap.field("dollarInitProvisionAmt", "dollarInitProvisionAmt");
		 classMap.field("inrConversionRate", "inrConversionRate");
		 classMap.field("inrTotalAmount", "inrTotalAmount");
		 classMap.field("passportExpiryDate", "passportExpiryDate");
	
		 
		 	 
		 
		 classMap.register();
		
		 
		 
		 mapper = mapperFactory.getMapperFacade();
		
		
	}
	

	public OMPIntimation getNewIntimation(NewIntimationDto newIntimationDto) {
		OMPIntimation dest = mapper.map(newIntimationDto, OMPIntimation.class);
		TmpCPUCode cpuCode = new TmpCPUCode();
		if(newIntimationDto.getHospitalDto() != null) {
			cpuCode.setKey(newIntimationDto.getHospitalDto().getCpuId());
		}
		dest.setCpuCode(cpuCode);
		
//		dest.setPolicy(newIntimationDto.getPolicy());
		
		return dest;
	}
	
	public NewIntimationDto getNewIntimationDto(OMPIntimation intimation) {
		NewIntimationDto dest = mapper.map(intimation, NewIntimationDto.class);
		if (intimation.getCpuCode() != null)
		{
			dest.setCpuId(intimation.getCpuCode().getKey());
			dest.setCpuCode(intimation.getCpuCode().getCpuCode());
			
			
			
		}
		
		dest.setPolicy(intimation.getPolicy());
		dest.setInsuredPatient(intimation.getInsured());
		return dest;
	}
	
	public static OMPNewIntimationMapper getInstance(){
        if(myObj == null){
            myObj = new OMPNewIntimationMapper();
            getAllValues();
        }
        return myObj;
	 }
}
