package com.shaic.claim.viewEarlierRodDetails;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ViewTmpPreauth;

public class PreauthToTmpPreauthMapper {
	
	static PreauthToTmpPreauthMapper myObj;
	
	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	private static BoundMapperFacade<Preauth, ViewTmpPreauth> preauthMapper;
	
	private static ClassMapBuilder<Preauth, ViewTmpPreauth> preauthMap = mapperFactory.classMap(Preauth.class,ViewTmpPreauth.class);
	
	 public static void getAllMapValues()  {
		 
		preauthMap.field("key", "key");
		preauthMap.field("activeStatus", "activeStatus");
		preauthMap.field("diffAmount", "diffAmount");
		preauthMap.field("coPay", "coPay");
		preauthMap.field("consultationDate", "consultationDate");
		preauthMap.field("coordinatorFlag", "coordinatorFlag");
		preauthMap.field("corporateBufferFlag", "corporateBufferFlag");
		preauthMap.field("createdBy", "createdBy");
		preauthMap.field("createdDate", "createdDate");
		preauthMap.field("criticalIllnessFlag", "criticalIllnessFlag");
		preauthMap.field("criticalIllness", "criticalIllness");
		preauthMap.field("dataOfAdmission", "dataOfAdmission");
		preauthMap.field("dateOfDeath", "dateOfDeath");
		preauthMap.field("dateOfDischarge", "dateOfDischarge");
		preauthMap.field("deathReason", "deathReason");
		preauthMap.field("doctorNote", "doctorNote");
		preauthMap.field("enhancementType", "enhancementType");
		preauthMap.field("claim", "claim");
		preauthMap.field("intimation", "intimation");
		preauthMap.field("policy", "policy");
		preauthMap.field("fvrNotRequiredRemarks", "fvrNotRequiredRemarks");
		preauthMap.field("initiateFvr", "initiateFvr");
		preauthMap.field("medicalCategoryId", "medicalCategoryId");
		preauthMap.field("medicalRemarks", "medicalRemarks");
		preauthMap.field("modifiedBy", "modifiedBy");
		preauthMap.field("modifiedDate", "modifiedDate");
		preauthMap.field("natureOfTreatment", "natureOfTreatment");
		preauthMap.field("numberOfDays", "numberOfDays");
		preauthMap.field("officeCode", "officeCode");
		preauthMap.field("patientStatus", "patientStatus");
		preauthMap.field("preauthId", "preauthId");
		preauthMap.field("processType", "processType");
		preauthMap.field("relapseFlag", "relapseFlag");
		preauthMap.field("relapseRemarks", "relapseRemarks");
		preauthMap.field("remarks", "remarks");
		preauthMap.field("remarks", "roomCategory");
		preauthMap.field("specialistConsulted", "specialistConsulted");
		preauthMap.field("specialistOpinionTaken", "specialistOpinionTaken");
		preauthMap.field("specialistRemarks", "specialistRemarks");
		preauthMap.field("specialistType", "specialistType");
		preauthMap.field("status", "status");
		preauthMap.field("stage", "stage");
		preauthMap.field("autoRestoration", "autoRestoration");
		preauthMap.field("illness", "illness");
		preauthMap.field("homeCpuId", "homeCpuId");
		preauthMap.field("processingCpuId", "processingCpuId");
		preauthMap.field("sendToCpu", "sendToCpu");
		preauthMap.field("reportReviewed", "reportReviewed");
		preauthMap.field("changePreauth", "changePreauth");
		preauthMap.field("investigatorName", "investigatorName");
		preauthMap.field("cpuRemarks", "cpuRemarks");
		preauthMap.field("reviewRemarks", "reviewRemarks");
		preauthMap.field("doaChangeReason", "doaChangeReason");
		preauthMap.field("terminatorCover", "terminatorCover");
		preauthMap.field("totalApprovalAmount", "totalApprovalAmount");
		preauthMap.field("treatmentRemarks", "treatmentRemarks");
		preauthMap.field("treatmentType", "treatmentType");
		preauthMap.field("withdrawReason", "withdrawReason");
		preauthMap.field("downSizeReason", "downSizeReason");
		preauthMap.field("rejectionCategorId", "rejectionCategorId");
		preauthMap.field("insuredKey", "insuredKey");
		preauthMap.field("hopsitaliztionDueto", "hopsitaliztionDueto");
		preauthMap.field("injuryCauseId", "injuryCauseId");
		preauthMap.field("injuryDate", "injuryDate");
		preauthMap.field("medicoLeagalCare", "medicoLeagalCare");
		preauthMap.field("reportedToPolice", "reportedToPolice");
		preauthMap.field("attachedPoliceReport", "attachedPoliceReport");
		preauthMap.field("firNumber", "firNumber");
		preauthMap.field("firstDiseaseDetectedDate", "firstDiseaseDetectedDate");
		preauthMap.field("dateOfDelivery", "dateOfDelivery");
		preauthMap.field("typeOfDelivery", "typeOfDelivery");
		preauthMap.field("sectionCategory", "sectionCategory");
		preauthMap.field("sfxRegisteredQDate", "sfxRegisteredQDate");
		preauthMap.field("sfxMatchedQDate", "sfxMatchedQDate");
		
		preauthMap.register();
		
		preauthMapper = mapperFactory.getMapperFacade(Preauth.class, ViewTmpPreauth.class);

	}
	
	public Preauth getPreauth(ViewTmpPreauth tmpPreauth) {
		Preauth dest = preauthMapper.mapReverse(tmpPreauth);
		return dest;
	}
	
	public static PreauthToTmpPreauthMapper getInstance(){
        if(myObj == null){
            myObj = new PreauthToTmpPreauthMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
