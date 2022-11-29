package com.shaic.claim.premedical.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.TmpStarFaxDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ProcessPreMedicalMapper {
	
	
private static MapperFacade tableMapper;

static ProcessPreMedicalMapper myObj;
	
public static void getAllMapValues()  {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, ProcessPreMedicalTableDTO> classMap = mapperFactory.classMap(Claim.class, ProcessPreMedicalTableDTO.class);
		ClassMapBuilder<Hospitals, ProcessPreMedicalTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, ProcessPreMedicalTableDTO.class);
		ClassMapBuilder<TmpStarFaxDetails, ProcessPreMedicalTableDTO> classDocMap = mapperFactory.classMap(TmpStarFaxDetails.class, ProcessPreMedicalTableDTO.class);
		
				
		classMap.field("key", "key");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.intimationSource.value","intimationSource");
		classMap.field("intimation.key", "intimationKey");
		classMap.field("intimation.cpuCode.description","cpuName");
		classMap.field("intimation.policy.product.value", "productName");
		classMap.field("intimation.policy.product.key", "productKey");
		classMap.field("intimation.policy.key","policyKey");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.hospital", "hospitalTypeId");
	//	classMap.field("intimation.policy.insuredBalanceSi","balanceSI");
		classMap.field("claimedAmount", "preAuthReqAmt");
		classMap.field("stage.stageName","type");
		classMap.field("intimation.policy.policyNumber", "policyNo");
		//classMap.field("intimation.insured.insuredSumInsured","sumInsured");
		
		classMap.field("intimation.insured.insuredId", "insuredId");
		classMap.field("intimation.insured.key", "insuredKey");
		classMap.field("crcFlag", "crmFlagged");
		classHospMap.field("name", "hospitalName");
		classHospMap.field("networkHospitalType", "networkHospType");
		classHospMap.field("hospitalTypeName", "hospitalTypeName");
		classHospMap.field("key", "key");
		classDocMap.field("transactionType", "transactionType");
		classDocMap.field("documentDate","docReceivedTimeForReg");
		classMap.register();
		classHospMap.register();
		classDocMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	
	@SuppressWarnings("unused")
	public static  List<ProcessPreMedicalTableDTO> getProcessPreMedicalPreAuth(List<Claim> processPreMedicalPreAuthList)
	{
		List<ProcessPreMedicalTableDTO> tableDTO = new ArrayList<ProcessPreMedicalTableDTO>();
		List<ProcessPreMedicalTableDTO> mapAsList = tableMapper.mapAsList(processPreMedicalPreAuthList, ProcessPreMedicalTableDTO.class);
		return mapAsList;
	}
	@SuppressWarnings("unused")
	public static  List<ProcessPreMedicalTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<ProcessPreMedicalTableDTO> tableDTO = new ArrayList<ProcessPreMedicalTableDTO>();
		List<ProcessPreMedicalTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, ProcessPreMedicalTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<ProcessPreMedicalTableDTO> getDocumentInfoList(List<TmpStarFaxDetails> docInfoList)
	{
		List<ProcessPreMedicalTableDTO> tableDTO = new ArrayList<ProcessPreMedicalTableDTO>();
		List<ProcessPreMedicalTableDTO> mapAsList = tableMapper.mapAsList(docInfoList, ProcessPreMedicalTableDTO.class);
		return mapAsList;
	}
	
	public static ProcessPreMedicalMapper getInstance(){
        if(myObj == null){
            myObj = new ProcessPreMedicalMapper();
            getAllMapValues();
        }
        return myObj;
	 }


}
