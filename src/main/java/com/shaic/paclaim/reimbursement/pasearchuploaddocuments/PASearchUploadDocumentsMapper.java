package com.shaic.paclaim.reimbursement.pasearchuploaddocuments;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsTableDTO;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.newcode.wizard.domain.MappingUtil;
	
public class PASearchUploadDocumentsMapper {
	

	private static MapperFacade tableMapper;
	
	static PASearchUploadDocumentsMapper  myObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<DocAcknowledgement, SearchUploadDocumentsTableDTO> claimClassMap = mapperFactor.classMap(DocAcknowledgement.class, SearchUploadDocumentsTableDTO.class);
		
		claimClassMap.field("key", "ackDocKey");
		claimClassMap.field("claim.key", "claimKey");
		claimClassMap.field("claim.claimId", "claimNo");
		claimClassMap.field("claim.claimType.value", "claimType");
		
		claimClassMap.field("acknowledgeNumber","acknowledgementNo");
		claimClassMap.field("documentReceivedFromId.value","docReceivedType");
		claimClassMap.field("claim.intimation.intimationId", "intimationNo");
		claimClassMap.field("claim.intimation.policy.policyNumber", "policyNo");
		claimClassMap.field("claim.intimation.admissionDate", "dateOfAdmission");
		claimClassMap.field("claim.intimation.admissionReason", "reasonForAdmission");
		claimClassMap.field("claim.intimation.insured.insuredName", "insuredPatientName");
		claimClassMap.field("claim.intimation.hospital", "hospitalNameID");
		
		claimClassMap.field("hospitalisationFlag","hospitalizationFlag");
		claimClassMap.field("preHospitalisationFlag", "preHospitalizationFlag");
		claimClassMap.field("postHospitalisationFlag", "postHospitalizationFlag");
		claimClassMap.field("partialHospitalisationFlag", "partialHospitalizationFlag");
		claimClassMap.field("hospitalizationRepeatFlag", "hospitalizationRepeatFlag");
		claimClassMap.field("lumpsumAmountFlag", "lumpSumAmountFlag");
		claimClassMap.field("hospitalCashFlag", "addOnBenefitsHospitalCashFlag");
		claimClassMap.field("patientCareFlag", "addOnBenefitsPatientCareFlag");

		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
	}
	
	public static List<SearchUploadDocumentsTableDTO> getSeachOrUploadDocumentsTableObj(List<DocAcknowledgement> intimationData){
		List<SearchUploadDocumentsTableDTO> mapAsList = tableMapper.mapAsList(intimationData, SearchUploadDocumentsTableDTO.class);
		return mapAsList;
		
	}
	
	public static PASearchUploadDocumentsMapper getInstance(){
        if(myObj == null){
            myObj = new PASearchUploadDocumentsMapper();
            getAllMapValues();
        }
        return myObj;
	 }




}
