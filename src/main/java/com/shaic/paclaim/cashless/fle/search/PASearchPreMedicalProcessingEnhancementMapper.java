package com.shaic.paclaim.cashless.fle.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.TmpStarFaxDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PASearchPreMedicalProcessingEnhancementMapper {
	
	private static MapperFacade tableMapper;
	
	static PASearchPreMedicalProcessingEnhancementMapper myObj;
	
	public static void getAllMapValues()  {
	
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, PASearchPreMedicalProcessingEnhancementTableDTO> classMap = mapperFactory.classMap(Claim.class, PASearchPreMedicalProcessingEnhancementTableDTO.class);
		ClassMapBuilder<Hospitals, PASearchPreMedicalProcessingEnhancementTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, PASearchPreMedicalProcessingEnhancementTableDTO.class);
		ClassMapBuilder<TmpStarFaxDetails, PASearchPreMedicalProcessingEnhancementTableDTO> classDocMap = mapperFactory.classMap(TmpStarFaxDetails.class, PASearchPreMedicalProcessingEnhancementTableDTO.class);
		classMap.field("key", "key");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.key", "intimationKey");
		classMap.field("intimation.intimationSource.value","intimationSource");
		classMap.field("intimation.cpuCode.description","cpuNAME");
		classMap.field("intimation.policy.product.value", "productName");
		classMap.field("intimation.policy.product.key", "productKey");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.paPatientName", "paPatientName");
		classMap.field("intimation.hospital", "hospitalTypeId");
		classMap.field("intimation.policy.key", "policyKey");
		classMap.field("currentProvisionAmount","enhancementReqAmt");
	//	classMap.field("policy.insuredBalanceSi","balanceSI");
		classMap.field("stage.stageName","type");
		classMap.field("intimation.policy.policyNumber", "policyNo");
		//the below line is commented since, insuredSumInsured is calculated based on procedure.
				//classMap.field("policy.insuredSumInsured","sumInsured");
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
	public static List<PASearchPreMedicalProcessingEnhancementTableDTO> getSearchPreMedicalProcessEnhancement(
			List<Claim> SearchPreMedicalProcessingEnhancementList) {
		List<PASearchPreMedicalProcessingEnhancementTableDTO> mapAsList = tableMapper
				.mapAsList(SearchPreMedicalProcessingEnhancementList,
						PASearchPreMedicalProcessingEnhancementTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<PASearchPreMedicalProcessingEnhancementTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
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
		List<PASearchPreMedicalProcessingEnhancementTableDTO> tableDTO = new ArrayList<PASearchPreMedicalProcessingEnhancementTableDTO>();
		List<PASearchPreMedicalProcessingEnhancementTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, PASearchPreMedicalProcessingEnhancementTableDTO.class);
		return mapAsList;
	}
	
	
	@SuppressWarnings("unused")
	public static  List<PASearchPreMedicalProcessingEnhancementTableDTO> getDocumentInfoList(List<TmpStarFaxDetails> docInfoList)
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
		List<PASearchPreMedicalProcessingEnhancementTableDTO> tableDTO = new ArrayList<PASearchPreMedicalProcessingEnhancementTableDTO>();
		List<PASearchPreMedicalProcessingEnhancementTableDTO> mapAsList = tableMapper.mapAsList(docInfoList, PASearchPreMedicalProcessingEnhancementTableDTO.class);
		return mapAsList;
	}
	
	public static PASearchPreMedicalProcessingEnhancementMapper getInstance(){
        if(myObj == null){
            myObj = new PASearchPreMedicalProcessingEnhancementMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	
}
