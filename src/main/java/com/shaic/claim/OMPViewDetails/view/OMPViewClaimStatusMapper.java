package com.shaic.claim.OMPViewDetails.view;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.ompviewroddetails.OMPViewClaimStatusDto;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class OMPViewClaimStatusMapper
{
	private static MapperFacade mapper;
	
	static OMPViewClaimStatusMapper myObj;
	
	public static void getAllValues() 
	{
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
		ClassMapBuilder<ViewClaimStatusDTO, OMPViewClaimStatusDto> classMap = mapperFactory.classMap(ViewClaimStatusDTO.class, OMPViewClaimStatusDto.class);
		
		
		classMap.mapNulls(true);
		classMap.mapNullsInReverse(true);
		classMap.field("intimationKey","intimationKey");
		classMap.field("intimationId","intimationId");
		classMap.field("intimationMode", "modeOfIntimation");
		classMap.field("intimatedBy", "intimatedBy");
		classMap.field("policyNumber", "policyNo");
//		classMap.field("intimaterName", "intimaterName");
		classMap.field("insuredPatientName", "insuredName");
		classMap.field("status", "status");
		classMap.field("dateOfIntimation", "intimationDate");
		classMap.field("claimKey", "claimKey");
		classMap.field("hospitalName", "hospitalName");
//		classMap.field("createdBy","createdBy");
		 classMap.field("admissionDate", "admissionDate");
		 classMap.field("policyIssuing", "policyIssuingOffice");
		 classMap.field("productName", "productName");
		 classMap.field("patientName", "patientName");
		 classMap.field("state", "state");
		 classMap.field("city", "city");
		 classMap.field("smCode", "smCode");
		 classMap.field("smName", "smName");
		 classMap.field("agentBrokerCode", "agentOrBrokerCode");
		 classMap.field("agentBrokerName", "agentOrBrokerName");
		 
		 
		  /*  classMap1.mapNulls(true);
			classMap1.mapNullsInReverse(true);
			classMap1.field("intimationId","intimationId");
			classMap1.field("intimationMode.key", "modeOfIntimation.id");
			classMap1.field("intimationMode.value", "modeOfIntimation.value");
			classMap1.field("intimatedBy.key", "intimatedBy.id");
			classMap1.field("intimatedBy.value", "intimatedBy.value");
			classMap1.field("callerMobileNumber", "callerContactNum");
			classMap1.field("callerLandlineNumber","callerLandlineNum");
			classMap1.field("attendersMobileNumber", "attenderContactNum");
			classMap1.field("attendersLandlineNumber","attenderLandlineNum");
			classMap1.field("admissionReason", "reasonForAdmission");
			classMap1.field("insured.key", "insuredPatient.key");
			classMap1.field("intimaterName", "intimaterName");
			classMap1.field("insuredPatientName", "insuredPatientName");
			classMap1.field("status", "status");
			classMap1.field("relapseofIllness", "relapseofIllnessValue");
			classMap1.field("createdDate", "createdDate");
			classMap1.field("key", "key");
			classMap1.field("patientNotCovered", "newBornFlag");
			classMap1.field("createdBy","createdBy");
			classMap1.field("registrationStatus","registrationStatus");
			
			classMap1.field("claimType.key","claimType.id");
			classMap1.field("claimType.value","claimType.value");		
			
			 classMap1.field("hospitalType.value", "hospitalType.value");
			 classMap1.field("hospitalType.key","hospitalType.id");         
			 classMap1.field("hospitalType.key", "hospitalDto.hospitalType.id");
			 classMap1.field("hospitalType.value", "hospitalDto.hospitalType.value");
			 classMap1.field("hospital", "hospitalDto.key");
			 
			 
//			 classMap.field("cpuCode.cpuCode", "cpuCode").mapNulls(true).mapNullsInReverse(true);
//			 classMap.field("cpuCode.key", "cpuId").mapNulls(true).mapNullsInReverse(true);
//			 classMap.field("cpuCode.key", "hospitalDto.cpuId").mapNulls(true).mapNullsInReverse(true);
			 
			
			 classMap1.field("admissionDate", "admissionDate");
			 classMap1.field("admissionType.key", "admissionType.id");
			 classMap1.field("admissionType.value", "admissionType.value");
			 classMap1.field("roomCategory.key", "roomCategory.id");
			 classMap1.field("roomCategory.value", "roomCategory.value");
			 classMap1.field("inpatientNumber", "inpatientNumber");
			 classMap1.field("hospitalComments", "comments");
			 classMap1.field("lateIntimationReason", "lateIntimationReason");
			 
			 classMap1.field("managementType.key", "managementType.id");
			 classMap1.field("managementType.value", "managementType.value");
			 
			 classMap1.field("doctorName", "doctorName");*/
		 
		 
		 
		 classMap.register();
		 
		 
		 mapper = mapperFactory.getMapperFacade();
	}
	
	public Intimation getNewIntimation(NewIntimationDto newIntimationDto) {
		Intimation dest = mapper.map(newIntimationDto, Intimation.class);
		TmpCPUCode cpuCode = new TmpCPUCode();
		if(newIntimationDto.getHospitalDto() != null) {
			cpuCode.setKey(newIntimationDto.getHospitalDto().getCpuId());
		}
		dest.setCpuCode(cpuCode);
		
//		dest.setPolicy(newIntimationDto.getPolicy());
		
		return dest;
	}
	
	public OMPViewClaimStatusDto getOMPViewClaimStatusDto(ViewClaimStatusDTO claimstatus) {
		OMPViewClaimStatusDto dest = mapper.map(claimstatus, OMPViewClaimStatusDto.class);
		
		dest.setCashlessTableDetails(claimstatus.getCashlessTableDetails());
		dest.setClaimStatusRegistrionDetails(claimstatus.getClaimStatusRegistrionDetails());
		dest.setReceiptOfDocumentValues(claimstatus.getReceiptOfDocumentValues());
		dest.setPreviousPreAuthTableDTO(claimstatus.getPreviousPreAuthTableDTO());
		
		return dest;
	}
	
	public NewIntimationDto getNewIntimationDto(ViewTmpIntimation intimation) {
		NewIntimationDto dest = mapper.map(intimation, NewIntimationDto.class);
		if (intimation.getCpuCode() != null)
		{
			dest.setCpuId(intimation.getCpuCode().getKey());	
		}
		return dest;
	}
	
	public static OMPViewClaimStatusMapper getInstance(){
        if(myObj == null){
            myObj = new OMPViewClaimStatusMapper();
        }
        getAllValues();
        return myObj;
	 }
}
