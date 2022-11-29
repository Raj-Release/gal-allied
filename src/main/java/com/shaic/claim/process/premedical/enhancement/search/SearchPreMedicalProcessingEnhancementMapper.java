package com.shaic.claim.process.premedical.enhancement.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.TmpStarFaxDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchPreMedicalProcessingEnhancementMapper {
	
	private static MapperFacade tableMapper;
	
	static SearchPreMedicalProcessingEnhancementMapper myObj;
	
	public static void getAllMapValues()  {
	
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchPreMedicalProcessingEnhancementTableDTO> classMap = mapperFactory.classMap(Claim.class, SearchPreMedicalProcessingEnhancementTableDTO.class);
		ClassMapBuilder<Hospitals, SearchPreMedicalProcessingEnhancementTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchPreMedicalProcessingEnhancementTableDTO.class);
		ClassMapBuilder<TmpStarFaxDetails, SearchPreMedicalProcessingEnhancementTableDTO> classDocMap = mapperFactory.classMap(TmpStarFaxDetails.class, SearchPreMedicalProcessingEnhancementTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.key", "intimationKey");
		classMap.field("intimation.intimationSource.value","intimationSource");
		classMap.field("intimation.cpuCode.description","cpuNAME");
		classMap.field("intimation.policy.product.value", "productName");
		classMap.field("intimation.policy.product.key", "productKey");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.hospital", "hospitalTypeId");
		classMap.field("intimation.policy.key", "policyKey");
		classMap.field("currentProvisionAmount","enhancementReqAmt");
	//	classMap.field("policy.insuredBalanceSi","balanceSI");
		classMap.field("stage.stageName","type");
		classMap.field("intimation.policy.policyNumber", "policyNo");
		//the below line is commented since, insuredSumInsured is calculated based on procedure.
				//classMap.field("policy.insuredSumInsured","sumInsured");
		classMap.field("crcFlag", "crmFlagged");
		classMap.field("intimation.insured.insuredId", "insuredId");
		classMap.field("intimation.insured.key", "insuredKey");		
		classHospMap.field("name", "hospitalName");
		classHospMap.field("networkHospitalType", "networkHospitalType");
		classHospMap.field("hospitalTypeName", "hospitalTypeName");
		classHospMap.field("key", "key");
		classDocMap.field("transactionType", "transactionType");
		classDocMap.field("documentDate","docsRecievedTime");
		
		classMap.register();
		classHospMap.register();
		classDocMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	/*public static  List<SearchPreMedicalProcessingEnhancementTableDTO> getSearchPreMedicalProcessingEnhancementTableDTO()
	{
		List<SearchPreMedicalProcessingEnhancementTableDTO> list = new ArrayList<SearchPreMedicalProcessingEnhancementTableDTO>();
		for(int i =1; i <=10; i++)
		{
			SearchPreMedicalProcessingEnhancementTableDTO item = new SearchPreMedicalProcessingEnhancementTableDTO();
			item.setBalanceSI(25545d);
			item.setCpuNAME("cpuNAME");
			item.setDocsRecievedTime("docsRecievedTime");
			item.setEnhancementReqAmt(1254d);
			item.setHospitalName("hospitalName");
			item.setInsuredPatientName("insuredPatientName");
			item.setIntimationSource("intimationSource");
			item.setIntimationNo("intimationNo" +i);
			item.setNetworkHospitalType("networkHospitalType");
			item.setProductName("productName");
			item.setSno(i);
			item.setType("type");
			
			list.add(item);
		}
		return list;
	}
	*/
	
	@SuppressWarnings("unused")
	public static List<SearchPreMedicalProcessingEnhancementTableDTO> getSearchPreMedicalProcessEnhancement(
			List<Claim> SearchPreMedicalProcessingEnhancementList) {
		List<SearchPreMedicalProcessingEnhancementTableDTO> mapAsList = tableMapper
				.mapAsList(SearchPreMedicalProcessingEnhancementList,
						SearchPreMedicalProcessingEnhancementTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<SearchPreMedicalProcessingEnhancementTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
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
		List<SearchPreMedicalProcessingEnhancementTableDTO> tableDTO = new ArrayList<SearchPreMedicalProcessingEnhancementTableDTO>();
		List<SearchPreMedicalProcessingEnhancementTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchPreMedicalProcessingEnhancementTableDTO.class);
		return mapAsList;
	}
	
	
	@SuppressWarnings("unused")
	public static  List<SearchPreMedicalProcessingEnhancementTableDTO> getDocumentInfoList(List<TmpStarFaxDetails> docInfoList)
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
		List<SearchPreMedicalProcessingEnhancementTableDTO> tableDTO = new ArrayList<SearchPreMedicalProcessingEnhancementTableDTO>();
		List<SearchPreMedicalProcessingEnhancementTableDTO> mapAsList = tableMapper.mapAsList(docInfoList, SearchPreMedicalProcessingEnhancementTableDTO.class);
		return mapAsList;
	}
	
	public static SearchPreMedicalProcessingEnhancementMapper getInstance(){
        if(myObj == null){
            myObj = new SearchPreMedicalProcessingEnhancementMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}
