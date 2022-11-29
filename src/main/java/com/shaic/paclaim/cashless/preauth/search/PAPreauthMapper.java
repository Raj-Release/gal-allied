package com.shaic.paclaim.cashless.preauth.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.TmpStarFaxDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PAPreauthMapper {	
	private static MapperFacade tableMapper;
	
	static PAPreauthMapper myObj;
	
	 public static void getAllMapValues()  {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, PASearchPreauthTableDTO> classMap = mapperFactory.classMap(Preauth.class, PASearchPreauthTableDTO.class);
		ClassMapBuilder<Hospitals, PASearchPreauthTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, PASearchPreauthTableDTO.class);
		ClassMapBuilder<TmpStarFaxDetails, PASearchPreauthTableDTO> classDocMap = mapperFactory.classMap(TmpStarFaxDetails.class, PASearchPreauthTableDTO.class);
		ClassMapBuilder<Claim, PASearchPreauthTableDTO> classMap1 = mapperFactory.classMap(Claim.class, PASearchPreauthTableDTO.class);
		
		classMap.field("key", "key");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.intimationSource.value","intimationSource");
		classMap.field("intimation.cpuCode.description","cpuName");
		classMap.field("policy.product.value", "productName");
		classMap.field("policy.product.key", "productKey");
		classMap.field("policy.key","policyKey");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.paPatientName", "paPatientName");
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

		//the below line is commented since, insuredSumInsured is calculated based on procedure.
		//classMap.field("policy.insuredSumInsured","sumInsured");
		classMap.field("intimation.insured.insuredId", "insuredId");
		classMap.field("intimation.insured.key", "insuredKey");
		
		classMap.field("treatmentType.value", "treatmentType");
		classHospMap.field("name", "hospitalName");
		classHospMap.field("networkHospitalType", "networkHospType");
		classHospMap.field("hospitalTypeName", "hospitalTypeName");
		classHospMap.field("key", "key");
		classDocMap.field("transactionType", "transactionType");
		classDocMap.field("documentDate","docReceivedTimeForReg");
		classDocMap.field("enhancementReqAmt","enhancementReqAmt");
		
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
		classMap1.field("claimedAmount", "preAuthReqAmt");
		classMap1.field("stage.stageName","type");
		classMap1.field("intimation.policy.policyNumber", "policyNo");
		classMap1.field("key", "claimKey");
		classMap1.field("crcFlag", "crmFlagged");
		classMap1.field("intimation.insured.insuredId", "insuredId");
		classMap1.field("intimation.insured.key", "insuredKey");		
		
		classMap.register();
		classHospMap.register();
		classDocMap.register();
		classMap1.register();
		tableMapper = mapperFactory.getMapperFacade();
	
	}
	
	@SuppressWarnings("unused")
	public static  List<PASearchPreauthTableDTO> getProcessPreAuth(List<Preauth> preAuthSearchList)
	{
		List<PASearchPreauthTableDTO> tableDTO = new ArrayList<PASearchPreauthTableDTO>();
		List<PASearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(preAuthSearchList, PASearchPreauthTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<PASearchPreauthTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<PASearchPreauthTableDTO> tableDTO = new ArrayList<PASearchPreauthTableDTO>();
		List<PASearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, PASearchPreauthTableDTO.class);
		return mapAsList;
	}
	
	
	@SuppressWarnings("unused")
	public static  List<PASearchPreauthTableDTO> getDocumentInfoList(List<TmpStarFaxDetails> docInfoList)
	{
		List<PASearchPreauthTableDTO> tableDTO = new ArrayList<PASearchPreauthTableDTO>();
		List<PASearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(docInfoList, PASearchPreauthTableDTO.class);
		return mapAsList;
	}
	
	public static PAPreauthMapper getInstance(){
        if(myObj == null){
            myObj = new PAPreauthMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	@SuppressWarnings("unused")
	public static  List<PASearchPreauthTableDTO> getPAProcessPreAuthByClaim(List<Claim> preAuthSearchList)
	{
		List<PASearchPreauthTableDTO> tableDTO = new ArrayList<PASearchPreauthTableDTO>();
		List<PASearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(preAuthSearchList, PASearchPreauthTableDTO.class);
		return mapAsList;
	}
	
}
