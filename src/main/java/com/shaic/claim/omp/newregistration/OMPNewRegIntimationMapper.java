package com.shaic.claim.omp.newregistration;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPIntimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class OMPNewRegIntimationMapper {
	
	private static MapperFactory mapperFactory =  new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	private static OMPNewRegIntimationMapper mapperObj;
	
	private static MapperFacade mapperFacade;
	
	private static ClassMapBuilder<OMPIntimation, OMPNewRegistrationSearchDTO> ompMap = mapperFactory.classMap(OMPIntimation.class, OMPNewRegistrationSearchDTO.class);

	public static  List<OMPNewRegistrationSearchDTO> getOMPIntimationList(List<OMPIntimation> OMPIntimationList){
		List<OMPNewRegistrationSearchDTO> mapAsList = mapperFacade.mapAsList(OMPIntimationList, OMPNewRegistrationSearchDTO.class);
		return mapAsList;
	}
	
	public static OMPNewRegIntimationMapper getInstance(){
		if(mapperObj == null){
			mapperObj = new OMPNewRegIntimationMapper();
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
		
		ompMap.register();
		
		mapperFacade = mapperFactory.getMapperFacade();			
	}	

	public NewIntimationDto getNewIntimationDto(OMPIntimation ompIntimation) {
		NewIntimationDto dest = mapperFacade.map(ompIntimation, NewIntimationDto.class);
		dest.setPolicy(ompIntimation.getPolicy());
		dest.setInsuredPatient(ompIntimation.getInsured());
		return dest;
	}	
	
	public OMPIntimation getNewIntimation(OMPNewRegistrationSearchDTO ompIntimationDTO) {
		OMPIntimation dest = mapperFacade.map(ompIntimationDTO, OMPIntimation.class);
		return dest;			
	}
	
}
