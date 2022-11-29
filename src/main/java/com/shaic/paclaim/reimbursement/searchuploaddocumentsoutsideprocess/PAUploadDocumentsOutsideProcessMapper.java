package com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedTableDTO;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PAUploadDocumentsOutsideProcessMapper {



	private static MapperFacade tableMapper;
	private static MapperFacade tableMapper1;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	static PAUploadDocumentsOutsideProcessMapper  myObj;

	
	public static void getAllMapValues()  {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, UploadDocumentsForAckNotReceivedTableDTO> preauthClassMap = mapperFactory.classMap(Preauth.class, UploadDocumentsForAckNotReceivedTableDTO.class);
		ClassMapBuilder<Reimbursement, UploadDocumentsForAckNotReceivedTableDTO> reimbClassMap = mapperFactory.classMap(Reimbursement.class, UploadDocumentsForAckNotReceivedTableDTO.class);
		
		
		preauthClassMap.field("claim.key", "claimKey");
		preauthClassMap.field("claim.claimId", "claimNo");
		preauthClassMap.field("claim.claimType.value", "claimType");
		
		
		preauthClassMap.field("intimation.intimationId", "intimationNo");
		preauthClassMap.field("intimation.policy.policyNumber", "policyNo");
		preauthClassMap.field("intimation.admissionDate", "dateOfAdmission");
		preauthClassMap.field("intimation.admissionReason", "reasonForAdmission");
		preauthClassMap.field("intimation.insured.insuredName", "insuredPatientName");
		preauthClassMap.field("intimation.hospital", "hospitalNameID");
		preauthClassMap.field("intimation.insured.healthCardNumber", "healthCardIdNo");
	
		
		
		reimbClassMap.field("claim.intimation.intimationId","intimationNo");
		reimbClassMap.field("claim.intimation.policy.policyNumber", "policyNo");
		reimbClassMap.field("claim.intimation.admissionDate", "dateOfAdmission");
		reimbClassMap.field("claim.intimation.admissionReason", "reasonForAdmission");
		reimbClassMap.field("claim.intimation.insured.insuredName", "insuredPatientName");
		reimbClassMap.field("claim.intimation.hospital", "hospitalNameID");
		reimbClassMap.field("claim.intimation.insured.healthCardNumber", "healthCardIdNo");
		reimbClassMap.field("claim.claimType.value", "claimType");
		reimbClassMap.field("claim.claimId", "claimNo");
		reimbClassMap.field("claim.key", "claimKey");

		preauthClassMap.register();
		reimbClassMap.register();
		
		tableMapper = mapperFactory.getMapperFacade();
		tableMapper1 = mapperFactory.getMapperFacade();
	}
	
	public static List<UploadDocumentsForAckNotReceivedTableDTO> getUploadDocumentForAckNotReceivedTableObj(List<Preauth> preauthData){
		List<UploadDocumentsForAckNotReceivedTableDTO> mapAsList = tableMapper.mapAsList(preauthData, UploadDocumentsForAckNotReceivedTableDTO.class);
		return mapAsList;
		
	}
	
	public static List<UploadDocumentsForAckNotReceivedTableDTO> getUploadDocumentForAckNotReceivedTableObj1(List<Reimbursement> reimbdata){
		List<UploadDocumentsForAckNotReceivedTableDTO> mapAsList1 = tableMapper1.mapAsList(reimbdata, UploadDocumentsForAckNotReceivedTableDTO.class);
		return mapAsList1;
		
	}
	
	public static PAUploadDocumentsOutsideProcessMapper getInstance(){
        if(myObj == null){
            myObj = new PAUploadDocumentsOutsideProcessMapper();
            getAllMapValues();
        }
        return myObj;
	 }



}
