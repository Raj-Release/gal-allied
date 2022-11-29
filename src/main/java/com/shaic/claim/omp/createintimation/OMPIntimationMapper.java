package com.shaic.claim.omp.createintimation;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPIntimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class OMPIntimationMapper {
	
	private static MapperFactory mapperFactory =  new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	private static OMPIntimationMapper mapperObj;
	
	private static MapperFacade mapperFacade;
	
	private static ClassMapBuilder<OMPIntimation, OMPCreateIntimationTableDTO> ompMap = mapperFactory.classMap(OMPIntimation.class, OMPCreateIntimationTableDTO.class);
	//private static ClassMapBuilder<OMPIntimation, NewIntimationDto> classMap = mapperFactory.classMap(OMPIntimation.class, NewIntimationDto.class);

	public static  List<OMPCreateIntimationTableDTO> getOMPIntimationList(List<OMPIntimation> OMPIntimationList){
		List<OMPCreateIntimationTableDTO> mapAsList = mapperFacade.mapAsList(OMPIntimationList, OMPCreateIntimationTableDTO.class);
		return mapAsList;
	}
	
	public static OMPIntimationMapper getInstance(){
		if(mapperObj == null){
			mapperObj = new OMPIntimationMapper();
			getAllMapValues();
		}
		return mapperObj;
	}
	
	
	public static void  getAllMapValues(){
		ompMap.field("key","intimationKey");
		ompMap.field("intimationId","intimationno");
		ompMap.field("policy.policyNumber", "policyNo");
		ompMap.field("insured.insuredName", "insuredName");
		ompMap.field("intimationDate", "intimationdate");
		ompMap.field("intimaterName", "intimatername");
		ompMap.field("intimatedBy.key", "intimatedby.id");
		ompMap.field("intimatedBy.value", "intimatedby.value");
		ompMap.field("intimationMode.key", "intimaticmode.id");
		ompMap.field("intimationMode.value", "intimaticmode.value");
		ompMap.field("hospitalName", "hospitalname");
		ompMap.field("status.processValue", "status");
		
		ompMap.field("policy.proposerFirstName", "proposername");
		ompMap.field("policy.productName", "productCodeOrName");
		
		ompMap.field("policy.totalSumInsured", "sumInsured");
		ompMap.field("policy.policyPlan", "plan");
		ompMap.field("passportNumber", "passportNo");
		
		ompMap.field("policy.policyFromDate", "policyCoverPeriodFromDate");
		ompMap.field("policy.policyToDate", "policyCoverPeriodToDate");
		
		
		//ompMap.field("createdDate", "admissionDate");
		ompMap.field("lossDateTime", "lossDateTime");
		ompMap.field("tpaIntimationNumber", "tpaIntimationNumber");
		ompMap.field("event.key", "eventCode.id");
		ompMap.field("event.eventDescription", "eventCode.value");		
		ompMap.field("placeVisit", "placeOfVisit");
		ompMap.field("placeLossDelay", "placeOfLossDelay");
		ompMap.field("sponsorName", "sponsorName");
		ompMap.field("ailmentLoss", "ailmentLoss");
		ompMap.field("admissionDate", "admissionDate");
		ompMap.field("dischargeDate", "dateOfDischarge");
		ompMap.field("callerMobileNumber", "contactNumber"); //txtCallerContactNo
		ompMap.field("dollarInitProvisionAmt", "initProvisionAmount");
		ompMap.field("inrConversionRate", "inrConversionRate");
		ompMap.field("inrTotalAmount", "inrTotalAmount");
		
		ompMap.field("policy.key", "policy.key");
		ompMap.field("insured.key", "insured.key");
		
		ompMap.field("hospitalizationFlag", "hospitalFlag");
		ompMap.field("nonHospitalizationFlag", "nonHospitalFlag");
		
		ompMap.field("claimType.key", "claimType.id");				
		ompMap.field("lobId", "lobId");
		
	/*	classMap.mapNulls(true);
		classMap.mapNullsInReverse(true);
		classMap.field("intimationId","intimationId");
		classMap.field("intimationMode.key", "modeOfIntimation.id");
		classMap.field("intimationMode.value", "modeOfIntimation.value");
		classMap.field("intimatedBy.key", "intimatedBy.id");
		classMap.field("intimatedBy.value", "intimatedBy.value");
		classMap.field("callerMobileNumber", "callerContactNum");
		classMap.field("callerLandlineNumber","callerLandlineNum");
//		classMap.field("admissionReason", "reasonForAdmission");
		classMap.field("insured.key", "insuredPatient.key");
		classMap.field("intimaterName", "intimaterName");
//		classMap.field("insuredPatientName", "insuredPatientName");
		classMap.field("status", "status");
		classMap.field("createdDate", "createdDate");
		classMap.field("key", "key");
		classMap.field("createdBy","createdBy");
		classMap.field("registrationStatus","registrationStatus");
		
		classMap.field("claimType.key","claimType.id");
		classMap.field("claimType.value","claimType.value");		
		
		 classMap.field("hospitalType.value", "hospitalType.value");
		 classMap.field("hospitalType.key","hospitalType.id");         
		 classMap.field("hospitalType.key", "hospitalDto.hospitalType.id");
		 classMap.field("hospitalType.value", "hospitalDto.hospitalType.value");
		 classMap.field("hospital", "hospitalDto.key");
		 classMap.field("policyYear", "policyYear");
		 
		 
//		 classMap.field("cpuCode.cpuCode", "cpuCode").mapNulls(true).mapNullsInReverse(true);
//		 classMap.field("cpuCode.key", "cpuId").mapNulls(true).mapNullsInReverse(true);
//		 classMap.field("cpuCode.key", "hospitalDto.cpuId").mapNulls(true).mapNullsInReverse(true);
		 
		
		 classMap.field("admissionDate", "admissionDate");
//		 classMap.field("admissionType.key", "admissionType.id");
//		 classMap.field("admissionType.value", "admissionType.value");
//		 classMap.field("roomCategory.key", "roomCategory.id");
//		 classMap.field("roomCategory.value", "roomCategory.value");
//		 classMap.field("inpatientNumber", "inpatientNumber");
		 classMap.field("hospitalComments", "comments");
//		 classMap.field("lateIntimationReason", "lateIntimationReason");
//		 
//		 classMap.field("managementType.key", "managementType.id");
//		 classMap.field("managementType.value", "managementType.value");
//		 classMap.field("doctorName", "doctorName");
*/		 
		 
		ompMap.register();
		//classMap.register();
		
		mapperFacade = mapperFactory.getMapperFacade();			
	}	

	public NewIntimationDto getNewIntimationDto(OMPIntimation ompIntimation) {
		NewIntimationDto dest = mapperFacade.map(ompIntimation, NewIntimationDto.class);
		dest.setPolicy(ompIntimation.getPolicy());
		dest.setInsuredPatient(ompIntimation.getInsured());
		return dest;
	}	
	
	public OMPIntimation getNewIntimation(OMPCreateIntimationTableDTO ompIntimationDTO) {
		OMPIntimation dest = mapperFacade.map(ompIntimationDTO, OMPIntimation.class);
		return dest;			
	}
	
}
