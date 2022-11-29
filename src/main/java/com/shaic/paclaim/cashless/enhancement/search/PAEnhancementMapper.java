package com.shaic.paclaim.cashless.enhancement.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.TmpStarFaxDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.paclaim.cashless.preauth.search.PASearchPreauthTableDTO;

public class PAEnhancementMapper {

	static PAEnhancementMapper myObj;
	
	private static MapperFacade tableMapper;
	
	public static void getAllMapValues() 	{
		
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, PASearchPreauthTableDTO> classMap = mapperFactory.classMap(Preauth.class, PASearchPreauthTableDTO.class);
		ClassMapBuilder<Hospitals, PASearchPreauthTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, PASearchPreauthTableDTO.class);
		ClassMapBuilder<TmpStarFaxDetails, PASearchPreauthTableDTO> classDocMap = mapperFactory.classMap(TmpStarFaxDetails.class, PASearchPreauthTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.key", "intimationKey");
		classMap.field("intimation.intimationSource.value","intimationSource");
		classMap.field("intimation.cpuCode.description","cpuName");
		classMap.field("policy.product.value", "productName");
		classMap.field("policy.product.key", "productKey");
		classMap.field("policy.key","policyKey");
		//classMap.field("policy.insuredFirstName", "insuredPatientName");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.paPatientName", "paPatientName");
		//classMap.field("intimation.hospitalType.key", "hospitalTypeId");
		classMap.field("intimation.hospital", "hospitalTypeId");
		classMap.field("specialistType.value", "speciality");
		classMap.field("sfxMatchedQDate", "docReceivedTimeForMatch");
		
	//	classMap.field("policy.insuredBalanceSi","balanceSI");
		classMap.field("claim.claimedAmount", "preAuthReqAmt");
		classMap.field("claim.key", "claimKey");
		classMap.field("stage.stageName","type");
		classMap.field("policy.policyNumber", "policyNo");
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
		classDocMap.field("documentDate","docReceivedTimeForMatch");
		classDocMap.field("enhancementReqAmt","enhancementReqAmt");
		
		classMap.register();
		classHospMap.register();
		classDocMap.register();
		tableMapper = mapperFactory.getMapperFacade();

	}
	
	
	@SuppressWarnings("unused")
	public static  List<PASearchPreauthTableDTO> getProcessEnhancement(List<Preauth> processEnhancementList)
	{
		/*List<SearchProcessRejectionTableDTO> list = new ArrayList<SearchProcessRejectionTableDTO>();
		for(int i =1; i <=10; i++)
		{
			SearchProcessRejectionTableDTO item = new SearchProcessRejectionTableDTO();
			item.setHospitalType("Hospital Type" + i);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			item.setIntimationDate(date);
			item.setIntimationNo("intimationNo" +i);
			item.setPreauthStatus("preauthStatus");
			item.setStatus("status");
			list.add(item);
		}
		return list;*/
		List<PASearchPreauthTableDTO> tableDTO = new ArrayList<PASearchPreauthTableDTO>();
		List<PASearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(processEnhancementList, PASearchPreauthTableDTO.class);
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
		/*List<SearchProcessRejectionTableDTO> list = new ArrayList<SearchProcessRejectionTableDTO>();
		for(int i =1; i <=10; i++)
		{
			SearchProcessRejectionTableDTO item = new SearchProcessRejectionTableDTO();
			item.setHospitalType("Hospital Type" + i);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			item.setIntimationDate(date);
			item.setIntimationNo("intimationNo" +i);
			item.setPreauthStatus("preauthStatus");
			item.setStatus("status");
			list.add(item);
		}
		return list;*/
		List<PASearchPreauthTableDTO> tableDTO = new ArrayList<PASearchPreauthTableDTO>();
		List<PASearchPreauthTableDTO> mapAsList = tableMapper.mapAsList(docInfoList, PASearchPreauthTableDTO.class);
		return mapAsList;
	}

	
	public static PAEnhancementMapper getInstance(){
        if(myObj == null){
            myObj = new PAEnhancementMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}