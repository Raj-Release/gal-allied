package com.shaic.paclaim.cashless.flp.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.TmpStarFaxDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PAProcessPreMedicalMapper {
	
	
private static MapperFacade tableMapper;

static PAProcessPreMedicalMapper myObj;
	
public static void getAllMapValues()  {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, PAProcessPreMedicalTableDTO> classMap = mapperFactory.classMap(Claim.class, PAProcessPreMedicalTableDTO.class);
		ClassMapBuilder<Hospitals, PAProcessPreMedicalTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, PAProcessPreMedicalTableDTO.class);
		ClassMapBuilder<TmpStarFaxDetails, PAProcessPreMedicalTableDTO> classDocMap = mapperFactory.classMap(TmpStarFaxDetails.class, PAProcessPreMedicalTableDTO.class);
		
				
		classMap.field("key", "key");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.intimationSource.value","intimationSource");
		classMap.field("intimation.key", "intimationKey");
		classMap.field("intimation.cpuCode.description","cpuName");
		classMap.field("intimation.policy.product.value", "productName");
		classMap.field("intimation.policy.product.key", "productKey");
		classMap.field("intimation.policy.key","policyKey");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.paPatientName", "paPatientName");
		classMap.field("intimation.hospital", "hospitalTypeId");
	//	classMap.field("intimation.policy.insuredBalanceSi","balanceSI");
		classMap.field("claimedAmount", "preAuthReqAmt");
		classMap.field("stage.stageName","type");
		classMap.field("intimation.policy.policyNumber", "policyNo");
		//classMap.field("intimation.insured.insuredSumInsured","sumInsured");
		
		classMap.field("intimation.insured.insuredId", "insuredId");
		classMap.field("intimation.insured.key", "insuredKey");
		
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
	public static  List<PAProcessPreMedicalTableDTO> getProcessPreMedicalPreAuth(List<Claim> processPreMedicalPreAuthList)
	{
		List<PAProcessPreMedicalTableDTO> tableDTO = new ArrayList<PAProcessPreMedicalTableDTO>();
		List<PAProcessPreMedicalTableDTO> mapAsList = tableMapper.mapAsList(processPreMedicalPreAuthList, PAProcessPreMedicalTableDTO.class);
		return mapAsList;
	}
	@SuppressWarnings("unused")
	public static  List<PAProcessPreMedicalTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<PAProcessPreMedicalTableDTO> tableDTO = new ArrayList<PAProcessPreMedicalTableDTO>();
		List<PAProcessPreMedicalTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, PAProcessPreMedicalTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<PAProcessPreMedicalTableDTO> getDocumentInfoList(List<TmpStarFaxDetails> docInfoList)
	{
		List<PAProcessPreMedicalTableDTO> tableDTO = new ArrayList<PAProcessPreMedicalTableDTO>();
		List<PAProcessPreMedicalTableDTO> mapAsList = tableMapper.mapAsList(docInfoList, PAProcessPreMedicalTableDTO.class);
		return mapAsList;
	}
	
	public static PAProcessPreMedicalMapper getInstance(){
        if(myObj == null){
            myObj = new PAProcessPreMedicalMapper();
            getAllMapValues();
        }
        return myObj;
	 }


}
