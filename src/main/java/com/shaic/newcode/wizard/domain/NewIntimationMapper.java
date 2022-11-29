package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.Intimation;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class NewIntimationMapper
{
	private static MapperFacade mapper;
	
	static NewIntimationMapper myObj;
	
	public static void getAllValues() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Intimation, NewIntimationDto> classMap = mapperFactory.classMap(Intimation.class, NewIntimationDto.class);
		ClassMapBuilder<GalaxyIntimation, NewIntimationDto> classMap2 = mapperFactory.classMap(GalaxyIntimation.class, NewIntimationDto.class);
		ClassMapBuilder<ViewTmpIntimation, NewIntimationDto> classMap1 = mapperFactory.classMap(ViewTmpIntimation.class, NewIntimationDto.class);
		ClassMapBuilder<OPIntimation, NewIntimationDto> classMap3 = mapperFactory.classMap(OPIntimation.class, NewIntimationDto.class);
		
		classMap.mapNulls(true);
		classMap.mapNullsInReverse(true);
		classMap.field("intimationId","intimationId");
		classMap.field("createdDate", "dateOfIntimation");
		classMap.field("intimationMode.key", "modeOfIntimation.id");
		classMap.field("intimationMode.value", "modeOfIntimation.value");
		classMap.field("intimatedBy.key", "intimatedBy.id");
		classMap.field("intimatedBy.value", "intimatedBy.value");
		classMap.field("callerMobileNumber", "callerContactNum");
		classMap.field("callerLandlineNumber","callerLandlineNum");
		classMap.field("attendersMobileNumber", "attenderContactNum");
		classMap.field("attendersLandlineNumber","attenderLandlineNum");
		classMap.field("admissionReason", "reasonForAdmission");
		classMap.field("insured.key", "insuredPatient.key");
		classMap.field("intimaterName", "intimaterName");
		classMap.field("insuredPatientName", "insuredPatientName");
		classMap.field("status", "status");
		classMap.field("relapseofIllness", "relapseofIllnessValue");
		classMap.field("createdDate", "createdDate");
		classMap.field("key", "key");
		classMap.field("patientNotCovered", "newBornFlag");
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
		 classMap.field("insured.insuredEmployeeId", "employeeCode");
		 
//		 classMap.field("cpuCode.cpuCode", "cpuCode").mapNulls(true).mapNullsInReverse(true);
//		 classMap.field("cpuCode.key", "cpuId").mapNulls(true).mapNullsInReverse(true);
//		 classMap.field("cpuCode.key", "hospitalDto.cpuId").mapNulls(true).mapNullsInReverse(true);
		
		 classMap.field("admissionDate", "admissionDate");
		 classMap.field("admissionType.key", "admissionType.id");
		 classMap.field("admissionType.value", "admissionType.value");
		 classMap.field("roomCategory.key", "roomCategory.id");
		 classMap.field("roomCategory.value", "roomCategory.value");
		 classMap.field("inpatientNumber", "inpatientNumber");
		 //classMap.field("comments", "comments");
		 classMap.field("lateIntimationReason", "lateIntimationReason");
		 classMap.field("managementType.key", "managementType.id");
		 classMap.field("managementType.value", "managementType.value");
		 classMap.field("doctorName", "doctorName");
		 
		 classMap.field("lobId.key", "lobId.id");
		 classMap.field("processClaimType", "processClaimType"); 
		 classMap.field("incidenceFlag", "incidenceFlag");
		 classMap.field("hospitalReqFlag", "hospitalReqFlag");
		 classMap.field("accidentDeathDate", "accidentDeathDate"); 
		 classMap.field("paPatientName", "paPatientName");
		 classMap.field("paParentName", "paParentName");
		 classMap.field("paParentDOB", "parentDOB");
		 classMap.field("paParentAge", "parentAge");
		 classMap.field("dummy", "dummy");
		 classMap.field("callerAddress", "callerAddress");
		 classMap.field("callerEmail", "callerEmail");
		 classMap.field("callerEmail", "callerEmail");
		 classMap.field("dateOfDischarge", "dateOfDischarge");		 
		 classMap.field("insuredType", "insuredType.value");
		 classMap.field("hospitalComments", "hospitalComments");
		 classMap.field("paPatientDOB", "paPatientDOB");
		 classMap.field("paPatientAge", "paPatientAge");
		 
	     classMap1.mapNulls(true);
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
			 
			 
//		 classMap.field("cpuCode.cpuCode", "cpuCode").mapNulls(true).mapNullsInReverse(true);
//		 classMap.field("cpuCode.key", "cpuId").mapNulls(true).mapNullsInReverse(true);
//		 classMap.field("cpuCode.key", "hospitalDto.cpuId").mapNulls(true).mapNullsInReverse(true);
			 
			
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
		 
		 classMap1.field("doctorName", "doctorName");
								 
//		 // TMPPOLICY TO POLICY.
//		 classMap.field("policy.key", "policyKey");
//		 classMap.field("policy.insuredDob", "insuredPatientId.insuredDateOfBirth");
//		 classMap.field("policy.healthCardNumber", "insuredPatientId.healthCardNumber");
//		 classMap.field("policy.status", "tmpPolicy.polStatus");
//		 classMap.field("policy.policyStatus", "tmpPolicy.polStatus");
//		 classMap.field("policy.policySysId", "tmpPolicy.polSysId");
//		 classMap.field("policy.policyNumber", "tmpPolicy.polNo");
//		 classMap.field("policy.officeCode", "tmpPolicy.polhDivnCode");
//		 classMap.field("policy.homeOfficeName", "policyIssueOffice");
//		 classMap.field("policy.homeOfficeCode", "tmpPolicy.polhDivnCode");
//		 classMap.field("policy.proposerFirstName", "tmpPolicy.polAssrName");
//		 classMap.field("policy.policyFromDate", "tmpPolicy.polFmDt");
//		 classMap.field("policy.policyToDate", "tmpPolicy.polToDt");
//		 classMap.field("policy.status", "tmpPolicy.polStatus");
//		 classMap.field("policy.proposerCode","tmpPolicy.polAssrCode");
//		 classMap.field("policy.endorsementNumber","tmpPolicy.polEndNo");
//		 classMap.field("policy.polAddr1","tmpPolicy.polAddr01");
//		 classMap.field("policy.polAddr2","tmpPolicy.polAddr02");
//		 classMap.field("policy.polAddr3","tmpPolicy.polAddr03");
//		 classMap.field("policy.polTelephoneNumber","tmpPolicy.polTelNo");
//		 classMap.field("policy.polFaxnumber","tmpPolicy.polFaxNo");
//		 classMap.field("policy.polEmailId","tmpPolicy.polEmailId");
//		 classMap.field("policy.polOfficeAddr1","tmpPolicy.polOffAddress1");
//		 classMap.field("policy.polOfficeAddr2","tmpPolicy.polOffAddress2");
//		 classMap.field("policy.polOfficeAddr3","tmpPolicy.polOffAddress3");
//		 classMap.field("policy.officeTelephone","tmpPolicy.polOffPhone");
//		 classMap.field("policy.officeFax","tmpPolicy.polOffTax");
//		 classMap.field("policy.officeEmailId","tmpPolicy.polOffEmail");
//		 classMap.field("policy.grossPremium","tmpPolicy.polGrossPrem");
//		 classMap.field("policy.totalPremium","tmpPolicy.polTotalAmt");
//		 classMap.field("policy.totalSumInsured","tmpPolicy.polSumInsured");
//		 classMap.field("policy.stampDuty","tmpPolicy.polStampDuty");
//		 classMap.field("policy.premiumTax","tmpPolicy.polPremTax");
//		 classMap.field("policy.receiptNumber","tmpPolicy.polReceiptNo");
//		 classMap.field("policy.receiptDate","tmpPolicy.polReceiptDate");
//		 classMap.field("policy.copay","tmpPolicy.copay");
//		 classMap.field("policy.corporateBufferFlag","tmpPolicy.corporateBufferFlag");
//		 classMap.field("policy.totalBufferAmount","tmpPolicy.totaBufferAmount");
//		 classMap.field("policy.smCode","tmpPolicy.polSmCode");
//		 classMap.field("policy.smName","tmpPolicy.polSmName");
		 
			classMap2.mapNulls(true);
			classMap2.mapNullsInReverse(true);
			classMap2.field("intimationId","intimationId");
			classMap2.field("intimationMode.key", "modeOfIntimation.id");
			classMap2.field("intimationMode.value", "modeOfIntimation.value");
			classMap2.field("intimatedBy.key", "intimatedBy.id");
			classMap2.field("intimatedBy.value", "intimatedBy.value");
			classMap2.field("callerMobileNumber", "callerContactNum");
			classMap2.field("callerLandlineNumber","callerLandlineNum");
			classMap2.field("attendersMobileNumber", "attenderContactNum");
			classMap2.field("attendersLandlineNumber","attenderLandlineNum");
			classMap2.field("admissionReason", "reasonForAdmission");
			classMap2.field("insured.key", "insuredPatient.key");
			classMap2.field("intimaterName", "intimaterName");
			classMap2.field("insuredPatientName", "insuredPatientName");
			classMap2.field("status", "status");
			classMap2.field("relapseofIllness", "relapseofIllnessValue");
			classMap2.field("createdDate", "createdDate");
			classMap2.field("key", "key");
			classMap2.field("patientNotCovered", "newBornFlag");
			classMap2.field("createdBy","createdBy");
			classMap2.field("registrationStatus","registrationStatus");
			
			classMap2.field("claimType.key","claimType.id");
			classMap2.field("claimType.value","claimType.value");		
			
			 classMap2.field("hospitalType.value", "hospitalType.value");
			 classMap2.field("hospitalType.key","hospitalType.id");         
			 classMap2.field("hospitalType.key", "hospitalDto.hospitalType.id");
			 classMap2.field("hospitalType.value", "hospitalDto.hospitalType.value");
			 classMap2.field("hospital", "hospitalDto.key");
			 classMap2.field("policyYear", "policyYear");
			 classMap2.field("insured.insuredEmployeeId", "employeeCode");
			 
//			 classMap.field("cpuCode.cpuCode", "cpuCode").mapNulls(true).mapNullsInReverse(true);
//			 classMap.field("cpuCode.key", "cpuId").mapNulls(true).mapNullsInReverse(true);
//			 classMap.field("cpuCode.key", "hospitalDto.cpuId").mapNulls(true).mapNullsInReverse(true);
			
			 classMap2.field("admissionDate", "admissionDate");
			 classMap2.field("admissionType.key", "admissionType.id");
			 classMap2.field("admissionType.value", "admissionType.value");
			 classMap2.field("roomCategory.key", "roomCategory.id");
			 classMap2.field("roomCategory.value", "roomCategory.value");
			 classMap2.field("inpatientNumber", "inpatientNumber");
			 classMap2.field("comments", "comments");
			 classMap2.field("lateIntimationReason", "lateIntimationReason");
			 classMap2.field("managementType.key", "managementType.id");
			 classMap2.field("managementType.value", "managementType.value");
			 classMap2.field("doctorName", "doctorName");
			 
			 classMap2.field("lobId.key", "lobId.id");
			 classMap2.field("processClaimType", "processClaimType"); 
			 classMap2.field("incidenceFlag", "incidenceFlag");
			 classMap2.field("hospitalReqFlag", "hospitalReqFlag");
			 classMap2.field("accidentDeathDate", "accidentDeathDate"); 
			 classMap2.field("paPatientName", "paPatientName");
			 classMap2.field("paParentName", "paParentName");
			 classMap2.field("paParentDOB", "parentDOB");
			 classMap2.field("paParentAge", "parentAge");
			 classMap2.field("dummy", "dummy");
			 classMap2.field("callerAddress", "callerAddress");
			 classMap2.field("callerEmail", "callerEmail");
			 classMap2.field("callerEmail", "callerEmail");
			 classMap2.field("dateOfDischarge", "dateOfDischarge");		 
			 classMap2.field("insuredType", "insuredType.value");
			 classMap2.field("hospitalComments", "hospitalComments");
			 classMap2.field("paStudentName", "studentPatientName");
			 classMap2.field("paStudentDOB", "studentDOB");
			 classMap2.field("paStudentAge", "studentAge");



			 classMap3.mapNulls(true);
			 classMap3.mapNullsInReverse(true);
			 classMap3.field("intimationId","intimationId");
			 classMap3.field("intimationMode.key", "modeOfIntimation.id");
			 classMap3.field("intimationMode.value", "modeOfIntimation.value");
			 classMap3.field("intimatedBy.key", "intimatedBy.id");
			 classMap3.field("intimatedBy.value", "intimatedBy.value");
			 classMap3.field("callerMobileNumber", "callerContactNum");
			 classMap3.field("callerLandlineNumber","callerLandlineNum");
			 classMap3.field("attendersMobileNumber", "attenderContactNum");
			 classMap3.field("attendersLandlineNumber","attenderLandlineNum");
			 classMap3.field("admissionReason", "reasonForAdmission");
			 classMap3.field("insured.key", "insuredPatient.key");
			 classMap3.field("intimaterName", "intimaterName");
			 classMap3.field("insuredPatientName", "insuredPatientName");
			 classMap3.field("status", "status");
			 classMap3.field("relapseofIllness", "relapseofIllnessValue");
			 classMap3.field("createdDate", "createdDate");
			 classMap3.field("key", "key");
			 classMap3.field("patientNotCovered", "newBornFlag");
			 classMap3.field("createdBy","createdBy");
			 classMap3.field("registrationStatus","registrationStatus");

			 classMap3.field("claimType.key","claimType.id");
			 classMap3.field("claimType.value","claimType.value");		

			 classMap3.field("hospitalType.value", "hospitalType.value");
			 classMap3.field("hospitalType.key","hospitalType.id");         
			 classMap3.field("hospitalType.key", "hospitalDto.hospitalType.id");
			 classMap3.field("hospitalType.value", "hospitalDto.hospitalType.value");
			 classMap3.field("hospital", "hospitalDto.key");
			 classMap3.field("policyYear", "policyYear");
			 classMap3.field("insured.insuredEmployeeId", "employeeCode");

			 classMap3.field("admissionDate", "admissionDate");
			 classMap3.field("admissionType.key", "admissionType.id");
			 classMap3.field("admissionType.value", "admissionType.value");
			 classMap3.field("roomCategory.key", "roomCategory.id");
			 classMap3.field("roomCategory.value", "roomCategory.value");
			 classMap3.field("inpatientNumber", "inpatientNumber");

			 classMap3.field("lateIntimationReason", "lateIntimationReason");
			 classMap3.field("managementType.key", "managementType.id");
			 classMap3.field("managementType.value", "managementType.value");
			 classMap3.field("doctorName", "doctorName");

			 classMap3.field("lobId.key", "lobId.id");
			 classMap3.field("processClaimType", "processClaimType"); 
			 classMap3.field("incidenceFlag", "incidenceFlag");
			 classMap3.field("hospitalReqFlag", "hospitalReqFlag");
			 classMap3.field("accidentDeathDate", "accidentDeathDate"); 
			 classMap3.field("paPatientName", "paPatientName");
			 classMap3.field("paParentName", "paParentName");
			 classMap3.field("paParentDOB", "parentDOB");
			 classMap3.field("paParentAge", "parentAge");
			 classMap3.field("dummy", "dummy");
			 classMap3.field("callerAddress", "callerAddress");
			 classMap3.field("callerEmail", "callerEmail");
			 classMap3.field("callerEmail", "callerEmail");
			 classMap3.field("dateOfDischarge", "dateOfDischarge");		 
			 classMap3.field("insuredType", "insuredType.value");
			 classMap3.field("hospitalComments", "hospitalComments");
		 
			 classMap.register();
			 classMap1.register();
			 classMap2.register();
			 classMap3.register();
		 
		 
		 mapper = mapperFactory.getMapperFacade();
	}
	
	public GalaxyIntimation getNewIntimation(NewIntimationDto newIntimationDto) {
		GalaxyIntimation dest = mapper.map(newIntimationDto, GalaxyIntimation.class);
		TmpCPUCode cpuCode = new TmpCPUCode();
		if(newIntimationDto.getHospitalDto() != null) {
			cpuCode.setKey(newIntimationDto.getHospitalDto().getCpuId());
		}
		dest.setCpuCode(cpuCode);
		
//		dest.setPolicy(newIntimationDto.getPolicy());
		
		return dest;
	}
	
	public NewIntimationDto getNewIntimationDto(OPIntimation intimation) {
		NewIntimationDto dest = mapper.map(intimation, NewIntimationDto.class);
		if (intimation.getCpuCode() != null) {
			dest.setCpuId(intimation.getCpuCode().getKey());
			dest.setCpuCode(intimation.getCpuCode().getCpuCode());
			dest.setCpuAddress(intimation.getCpuCode().getAddress());
		}
		if(intimation.getInsured() != null && intimation.getInsured().getRelationshipwithInsuredId() != null){
			dest.setRelationShipWithEmployee(intimation.getInsured().getRelationshipwithInsuredId().getValue());
		}
		dest.setPolicy(intimation.getPolicy());
		dest.setInsuredPatient(intimation.getInsured());
		return dest;
	}
	
	public NewIntimationDto getNewIntimationDto(Intimation intimation) {
		NewIntimationDto dest = mapper.map(intimation, NewIntimationDto.class);
		if (intimation.getCpuCode() != null)
		{
			dest.setCpuId(intimation.getCpuCode().getKey());
			dest.setCpuCode(intimation.getCpuCode().getCpuCode());
			dest.setCpuAddress(intimation.getCpuCode().getAddress());
		}
		if(intimation.getInsured() != null && intimation.getInsured().getRelationshipwithInsuredId() != null){
			dest.setRelationShipWithEmployee(intimation.getInsured().getRelationshipwithInsuredId().getValue());
		}
		dest.setPolicy(intimation.getPolicy());
		dest.setInsuredPatient(intimation.getInsured());
		return dest;
	}
	
	public NewIntimationDto getStageNewIntimationDto(GalaxyIntimation intimation) {
		NewIntimationDto dest = mapper.map(intimation, NewIntimationDto.class);
		if (intimation.getCpuCode() != null)
		{
			dest.setCpuId(intimation.getCpuCode().getKey());
			dest.setCpuCode(intimation.getCpuCode().getCpuCode());
			dest.setCpuAddress(intimation.getCpuCode().getAddress());
		}
		if(intimation.getInsured() != null && intimation.getInsured().getRelationshipwithInsuredId() != null){
			dest.setRelationShipWithEmployee(intimation.getInsured().getRelationshipwithInsuredId().getValue());
		}
		dest.setPolicy(intimation.getPolicy());
		dest.setInsuredPatient(intimation.getInsured());
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
	
	public static NewIntimationMapper getInstance(){
        if(myObj == null){
            myObj = new NewIntimationMapper();
            getAllValues();
        }
        return myObj;
	 }
}
