package com.shaic.claim.reports.fvrassignmentreport;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class FVRAssignmentReportMapper {
	
	static FVRAssignmentReportMapper myObj;
	
	private static MapperFacade tableMapper;
	private static MapperFacade tableMapper1;
	private static MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
	private static ClassMapBuilder<FieldVisitRequest, FVRAssignmentReportTableDTO> intimationClassMap = mapperFactor.classMap(FieldVisitRequest.class,FVRAssignmentReportTableDTO.class);
	private static ClassMapBuilder<Preauth,FVRAssignmentReportTableDTO> intimationClassMap1 = mapperFactor.classMap(Preauth.class, FVRAssignmentReportTableDTO.class);
	
	public static void getAllMapValues()   {
	
		intimationClassMap.field("key", "key");
		intimationClassMap.field("intimation.key", "intimationKey");
		intimationClassMap.field("intimation.cpuCode.cpuCode", "cpuCode");
		intimationClassMap.field("intimation.intimationId","intimationNo");
		intimationClassMap.field("intimation.admissionDate","dateofAdmission");
		intimationClassMap.field("intimation.admissionType","admissionType");
		intimationClassMap.field("intimation.insured.insuredName","patientName");
		intimationClassMap.field("intimation.hospital","hospitalNameId");
		intimationClassMap.field("claim.claimType.value","claimType");
		intimationClassMap.field("allocationTo.value","representativeType");
		intimationClassMap.field("representativeCode","representativeId");
		intimationClassMap.field("representativeName","representativeName");
		intimationClassMap.field("createdDate","fvrDate");
		intimationClassMap.field("fvrReceivedDate","fvrReceivedDate");
		intimationClassMap.field("fvrTriggerPoints","pointToFocus");
		intimationClassMap.field("executiveComments","fvrExecutiveComments");
		intimationClassMap.field("transactionKey","transactionKey");
		intimationClassMap.field("executiveComments","fvrNotRequiredComments");
		intimationClassMap.field("asigneeName.empFirstName","fvrAssigneeName");
		intimationClassMap.field("assignedDate","fvrAssignedDate");
		intimationClassMap.field("fvrCpuId","fvrCpuCode");
		intimationClassMap.field("createdBy","initiatorId");
		//intimationClassMap.field("","fvrAssignedTime");
		intimationClassMap1.field("intimation.cpuCode.cpuCode", "cpuCode");
		intimationClassMap1.field("intimation.intimationId","intimationNo");
		intimationClassMap1.field("intimation.admissionDate","dateofAdmission");
		intimationClassMap1.field("intimation.admissionType","admissionType");
		intimationClassMap1.field("intimation.insured.insuredName","patientName");
		intimationClassMap1.field("intimation.hospital","hospitalNameId");
		intimationClassMap1.field("claim.claimType.value","claimType");
		intimationClassMap1.field("createdDate","fvrDate");
		intimationClassMap1.field("key","key");
		
		
		
	
		intimationClassMap.register();
		intimationClassMap1.register();

		tableMapper = mapperFactor.getMapperFacade();
		tableMapper1 = mapperFactor.getMapperFacade();
	}

	public static List<FVRAssignmentReportTableDTO> getFVRAssignmentReportTableObjects(List<FieldVisitRequest> fvrList) {
		List<FVRAssignmentReportTableDTO> fvrAssignmentReportObjectList = tableMapper.mapAsList(fvrList, FVRAssignmentReportTableDTO.class);
		return fvrAssignmentReportObjectList;

	}
	
	public static List<FVRAssignmentReportTableDTO> getFVRAssignmentReportTableObjects1(List<Preauth> preauthList) {
		List<FVRAssignmentReportTableDTO> fvrAssignmentReportObjectList1 = tableMapper1.mapAsList(preauthList, FVRAssignmentReportTableDTO.class);
		return fvrAssignmentReportObjectList1;

	}
	
	public static FVRAssignmentReportMapper getInstance(){
        if(myObj == null){
            myObj = new FVRAssignmentReportMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}
