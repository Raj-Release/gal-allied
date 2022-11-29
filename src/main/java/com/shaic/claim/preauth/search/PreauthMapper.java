package com.shaic.claim.preauth.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.premedical.search.ProcessPreMedicalTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.TmpStarFaxDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PreauthMapper {	
	private static MapperFacade tableMapper;
	
	static PreauthMapper myObj;
	
	 public static void getAllMapValues()  {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, SearchPreauthTableDTO> classMap = mapperFactory.classMap(Preauth.class, SearchPreauthTableDTO.class);
		ClassMapBuilder<Hospitals, SearchPreauthTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchPreauthTableDTO.class);
		ClassMapBuilder<TmpStarFaxDetails, SearchPreauthTableDTO> classDocMap = mapperFactory.classMap(TmpStarFaxDetails.class, SearchPreauthTableDTO.class);
		ClassMapBuilder<Claim, SearchPreauthTableDTO> classMap1 = mapperFactory.classMap(Claim.class, SearchPreauthTableDTO.class);
		
		classMap.field("key", "key");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.intimationSource.value","intimationSource");
		classMap.field("intimation.cpuCode.description","cpuName");
		classMap.field("policy.product.value", "productName");
		classMap.field("policy.product.key", "productKey");
		classMap.field("policy.key","policyKey");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.hospital", "hospitalTypeId");
		classMap.field("intimation.key", "intimationKey");
	//	classMap.field("specialistType.value", "speciality");
	//	classMap.field("policy.insuredBalanceSi","balanceSI");
		classMap.field("claim.claimedAmount", "preAuthReqAmt");
		classMap.field("stage.stageName","type");
		classMap.field("policy.policyNumber", "policyNo");
		classMap.field("claim.key", "claimKey");
		classMap.field("sfxMatchedQDate","docReceivedTimeForMatch");
		classMap.field("sfxRegisteredQDate","docReceivedTimeForReg");
		classMap.field("claim.crcFlag", "crmFlagged");
		//the below line is commented since, insuredSumInsured is calculated based on procedure.
		//classMap.field("policy.insuredSumInsured","sumInsured");
		classMap.field("intimation.insured.insuredId", "insuredId");
		classMap.field("intimation.insured.key", "insuredKey");		
		classMap.field("treatmentType.value", "treatmentType");
		
		classMap1.field("key", "key");
		classMap1.field("intimation.intimationId","intimationNo");
		classMap1.field("intimation.intimationSource.value","intimationSource");
		classMap1.field("intimation.cpuCode.description","cpuName");
		classMap1.field("intimation.policy.product.value", "productName");
		classMap1.field("intimation.policy.product.key", "productKey");
		classMap1.field("intimation.policy.key","policyKey");
		classMap1.field("intimation.insuredPatientName", "insuredPatientName");
		classMap1.field("intimation.hospital", "hospitalTypeId");
		classMap1.field("intimation.key", "intimationKey");
	//	classMap.field("specialistType.value", "speciality");
	//	classMap.field("policy.insuredBalanceSi","balanceSI");
		classMap1.field("claimedAmount", "preAuthReqAmt");
		classMap1.field("stage.stageName","type");
		classMap1.field("intimation.policy.policyNumber", "policyNo");
		classMap1.field("key", "claimKey");
	//	classMap1.field("sfxMatchedQDate","docReceivedTimeForMatch");
	//	classMap1.field("sfxRegisteredQDate","docReceivedTimeForReg");
		classMap1.field("crcFlag", "crmFlagged");
		//the below line is commented since, insuredSumInsured is calculated based on procedure.
		//classMap.field("policy.insuredSumInsured","sumInsured");
		classMap1.field("intimation.insured.insuredId", "insuredId");
		classMap1.field("intimation.insured.key", "insuredKey");		
	//	classMap1.field("treatmentType.value", "treatmentType");
		
		classHospMap.field("name", "hospitalName");
		classHospMap.field("networkHospitalType", "networkHospType");
		classHospMap.field("hospitalTypeName", "hospitalTypeName");
		classHospMap.field("key", "key");
		classDocMap.field("transactionType", "transactionType");
		classDocMap.field("documentDate","docReceivedTimeForReg");
		classDocMap.field("enhancementReqAmt","enhancementReqAmt");
		
		
		classMap.register();
		classMap1.register();
		classHospMap.register();
		classDocMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	
	}
	
	@SuppressWarnings("unused")
	public static  List<SearchPreauthTableDTO> getProcessPreAuth(List<Preauth> preAuthSearchList)
	{
		List<SearchPreauthTableDTO> tableDTO = new ArrayList<SearchPreauthTableDTO>();
		List<SearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(preAuthSearchList, SearchPreauthTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<SearchPreauthTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<SearchPreauthTableDTO> tableDTO = new ArrayList<SearchPreauthTableDTO>();
		List<SearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchPreauthTableDTO.class);
		return mapAsList;
	}
	
	
	@SuppressWarnings("unused")
	public static  List<SearchPreauthTableDTO> getDocumentInfoList(List<TmpStarFaxDetails> docInfoList)
	{
		List<SearchPreauthTableDTO> tableDTO = new ArrayList<SearchPreauthTableDTO>();
		List<SearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(docInfoList, SearchPreauthTableDTO.class);
		return mapAsList;
	}
	
	public static PreauthMapper getInstance(){
        if(myObj == null){
            myObj = new PreauthMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	@SuppressWarnings("unused")
	public static  List<SearchPreauthTableDTO> getProcessPreAuthByClaim(List<Claim> preAuthSearchList)
	{
		List<SearchPreauthTableDTO> tableDTO = new ArrayList<SearchPreauthTableDTO>();
		List<SearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(preAuthSearchList, SearchPreauthTableDTO.class);
		return mapAsList;
	}
}
