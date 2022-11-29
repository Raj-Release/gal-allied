package com.shaic.restservices.crm;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.StageIntimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class StageIntimationMapper {

	private static MapperFacade tableMapper;
	private static StageIntimationMapper myObj;

	public static void getAllMapValues() {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<AddIntimationRequest, StageIntimation> claimClassMap = mapperFactor.classMap(AddIntimationRequest.class, StageIntimation.class);

		claimClassMap.field("insuredName", "insuredName");
		claimClassMap.field("intimationMode", "intimationMode");
		claimClassMap.field("intimatedBy", "intimatedBy");
		claimClassMap.field("intimatorName", "intimatorName");		
		claimClassMap.field("intimatorContactNo", "intimatorContactNo");
		claimClassMap.field("polNo", "polNo");
		claimClassMap.field("intimationNo", "intimationNo");
		claimClassMap.field("hospCode", "hospCode");
		claimClassMap.field("hospitalTypeYn", "hospitalTypeYn");		
		claimClassMap.field("hospComments", "hospComments");
		claimClassMap.field("admissionType", "admissionType");
		claimClassMap.field("managementType", "managementType");
		claimClassMap.field("reasonForAdmission", "reasonForAdmission");
		claimClassMap.field("admitted", "admitted");		
		claimClassMap.field("roomCategory", "roomCategory");
		claimClassMap.field("savedType", "savedType");
		claimClassMap.field("clmProcDivn", "clmProcDivn");
		claimClassMap.field("patientNameYn", "patientNameYn");
		claimClassMap.field("inpatientNo", "inpatientNo");		
		claimClassMap.field("createdBy", "createdBy");
		claimClassMap.field("createdOn", "createdOn");
		claimClassMap.field("attMobNo", "attMobNo");
		claimClassMap.field("policyYr", "policyYr");
		claimClassMap.field("accDeathYn", "accDeathYn");		
		claimClassMap.field("hospRequYn", "hospRequYn");
		claimClassMap.field("paCategory", "paCategory");
		claimClassMap.field("paPatientName", "paPatientName");
		claimClassMap.field("paParentName", "paParentName");
		claimClassMap.field("paParentDob", "paParentDob");		
		claimClassMap.field("paParentAge", "paParentAge");
		claimClassMap.field("prodCode", "prodCode");
		claimClassMap.field("paPatientDob", "paPatientDob");
		claimClassMap.field("paPatientAge", "paPatientAge");
		claimClassMap.field("suspiciousMultiple", "suspiciousMultiple");
		claimClassMap.field("admittedTime", "admittedTime");
		claimClassMap.field("dischargeDate", "dischargeDate");
		claimClassMap.field("dischargeTime", "dischargeTime");

		claimClassMap.register();
		tableMapper = mapperFactor.getMapperFacade();
	}

	public static StageIntimation getStageIntimation(AddIntimationRequest addIntimationRequest) {
		StageIntimation stageIntimation = tableMapper.map(addIntimationRequest, StageIntimation.class);
		return stageIntimation;
	}

	public static StageIntimationMapper getInstance() {
		if(myObj == null) {
			myObj = new StageIntimationMapper();
			getAllMapValues();
		}
		return myObj;
	}
}
