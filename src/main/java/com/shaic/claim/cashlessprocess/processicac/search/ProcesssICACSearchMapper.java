package com.shaic.claim.cashlessprocess.processicac.search;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.processdatacorrectionhistorical.bean.SpecialityHist;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.preauth.Speciality;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.manageclaim.reopenclaim.sarchClaimLevel.SearchReOpenClaimMapper;
import com.shaic.reimbursement.manageclaim.reopenclaim.sarchClaimLevel.SearchReOpenClaimTableDTO;

public class ProcesssICACSearchMapper {
	
private static MapperFacade tableMapper;
	
	static ProcesssICACSearchMapper  myObj;
	
	public static BoundMapperFacade<Claim,SearchProcessICACTableDTO> icacMapper;
	public static BoundMapperFacade<IcacRequest,SearchProcessICACTableDTO> icacReqMapper;
	public static BoundMapperFacade<Hospitals, SearchProcessICACTableDTO> icacReqHospMapper;
	
	public static void getAllMapValues() {
 
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Claim, SearchProcessICACTableDTO> classMap = mapperFactor.classMap(Claim.class, SearchProcessICACTableDTO.class);
		ClassMapBuilder<IcacRequest, SearchProcessICACTableDTO> icacReqMap = mapperFactor.classMap(IcacRequest.class, SearchProcessICACTableDTO.class);
		ClassMapBuilder<Hospitals, SearchProcessICACTableDTO> classHospMap = mapperFactor.classMap(Hospitals.class, SearchProcessICACTableDTO.class);
		ClassMapBuilder<IcacRequest, ProcessingDoctorDetailsDTO> classProReqMap = mapperFactor.classMap(IcacRequest.class, ProcessingDoctorDetailsDTO.class);
		
		classMap.field("key", "claimKey");
		classMap.field("intimation.intimationId","intimationNo");
		classMap.field("intimation.intimationSource.value","intimationSource");
		classMap.field("intimation.cpuCode.description","cpuName");
		classMap.field("intimation.policy.product.value", "productName");
		classMap.field("intimation.policy.product.key", "productKey");
		classMap.field("intimation.policy.policyNumber","policyNumber");
		classMap.field("intimation.insuredPatientName", "insuredPatientName");
		classMap.field("intimation.hospital", "hospitalTypeId");
		classMap.field("intimation.key", "intimationKey");
		classMap.field("claimedAmount", "preAuthReqAmt");
		classMap.field("stage.stageName","type");
		classMap.field("intimation.policy.policyNumber", "policyNumber");
//		classMap.field("sfxMatchedQDate","docReceivedTimeForMatch");
//		classMap.field("sfxRegisteredQDate","docReceivedTimeForReg");
		classMap.field("crcFlag", "crmFlagged");
		classMap.field("intimation.policy.key", "policyKey");
		classMap.field("intimation.insured.insuredId", "insuredId");
		classMap.field("intimation.insured.key", "insuredKey");		
//		classMap.field("treatmentType.value", "treatmentType");
		classMap.field("icacFlag", "claimIcacChkflg");
		
		classHospMap.field("name", "hospitalName");
		classHospMap.field("networkHospitalType", "networkHospType");
		classHospMap.field("hospitalTypeName", "hospitalTypeName");
		classHospMap.field("key", "key");
		
		icacReqMap.field("key","icacKey");
		icacReqMap.field("intimationNum","intimationNo");
		
		classMap.register();
		classHospMap.register();
		icacReqMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		icacMapper = mapperFactor.getMapperFacade(Claim.class, SearchProcessICACTableDTO.class);
		icacReqMapper = mapperFactor.getMapperFacade(IcacRequest.class, SearchProcessICACTableDTO.class);
		icacReqHospMapper = mapperFactor.getMapperFacade(Hospitals.class, SearchProcessICACTableDTO.class);
	}
	
	public static List<SearchProcessICACTableDTO> getResultDTO(List<Claim> claimData){
		List<SearchProcessICACTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchProcessICACTableDTO.class);
		return mapAsList;
		
	}
	
	@SuppressWarnings("unused")
	public static  List<SearchProcessICACTableDTO> getHospitalInfoList(List<Hospitals> hospitalInfoList)
	{
		List<SearchProcessICACTableDTO> tableDTO = new ArrayList<SearchProcessICACTableDTO>();
		List<SearchProcessICACTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchProcessICACTableDTO.class);
		return mapAsList;
	}
	
	@SuppressWarnings("unused")
	public static  List<ProcessingDoctorDetailsDTO> getIcacReqList(List<IcacRequest> icacInfoList)
	{
		List<ProcessingDoctorDetailsDTO> mapAsList = tableMapper.mapAsList(icacInfoList, ProcessingDoctorDetailsDTO.class);
		return mapAsList;
	}
	
	public List<SearchProcessICACTableDTO> getICACReqResultDTO(List<IcacRequest> reqIcac){
		List<SearchProcessICACTableDTO> mapAsList = 
										tableMapper.mapAsList(reqIcac, SearchProcessICACTableDTO.class);
		return mapAsList;
		
	}
	
	public SearchProcessICACTableDTO getHospICACTable(Hospitals hospitals) {
		SearchProcessICACTableDTO dest = icacReqHospMapper.map(hospitals);
		return dest;
	}
	
	public SearchProcessICACTableDTO getICACFromClaim(Claim claim) {
		SearchProcessICACTableDTO dest = icacMapper.map(claim);
		return dest;
	}
	
	public static ProcesssICACSearchMapper getInstance(){
        if(myObj == null){
            myObj = new ProcesssICACSearchMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
