package com.shaic.paclaim.cashless.withdraw.search;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PAWithdrawPreauthMapper {
	
	private static MapperFacade tableMapper;
	
	static PAWithdrawPreauthMapper  myObj;

	
	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, PASearchWithdrawCashLessProcessTableDTO> classMap = mapperFactory
				.classMap(Preauth.class,
						PASearchWithdrawCashLessProcessTableDTO.class);
		
		ClassMapBuilder<Hospitals, PASearchWithdrawCashLessProcessTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, PASearchWithdrawCashLessProcessTableDTO.class);
		
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("claim.claimId", "claimNo");
		classMap.field("policy.product.key", "productKey");
		//classMap.field("policy", "lob");
		//classMap.field("intimation.insuredPatientName", "");
		classMap.field("status.processValue","claimStatus");
		classMap.field("intimation.insured.insuredName", "insuredPatientName");
		classMap.field("intimation.paPatientName", "paPatientName");
		classMap.field("intimation.hospital", "hospitalId");
		
		
		classMap.field("intimation.hospitalType.key", "hospitalTypeId");
		
		//Hospital info map -- starts
		classHospMap.field("name","hospitalName");
		classHospMap.field("city","hospitalCity");
		classHospMap.field("key", "key");
		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
		
	}
	
	public static List<PASearchWithdrawCashLessProcessTableDTO> getWithdrawTableDTO(
			List<Preauth> searchClaimTableList) {
		List<PASearchWithdrawCashLessProcessTableDTO> mapAsList = tableMapper
				.mapAsList(searchClaimTableList,
						PASearchWithdrawCashLessProcessTableDTO.class);
		return mapAsList;
	}
	
	public static  List<PASearchWithdrawCashLessProcessTableDTO> getHospitalInfoListForWithdraw(List<Hospitals> hospitalInfoList)
	{
		//List<SearchWithdrawCashLessProcessTableDTO> tableDTO = new ArrayList<SearchWithdrawCashLessProcessTableDTO>();
		List<PASearchWithdrawCashLessProcessTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, PASearchWithdrawCashLessProcessTableDTO.class);
		return mapAsList;
	}
	
	
	public static PAWithdrawPreauthMapper getInstance(){
        if(myObj == null){
            myObj = new PAWithdrawPreauthMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}
