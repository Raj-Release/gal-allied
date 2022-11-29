package com.shaic.claim.withdrawWizard;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessTableDTO;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class WithdrawPreauthMapper {
	
	private static MapperFacade tableMapper;
	
	static WithdrawPreauthMapper  myObj;

	
	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, SearchWithdrawCashLessProcessTableDTO> classMap = mapperFactory
				.classMap(Preauth.class,
						SearchWithdrawCashLessProcessTableDTO.class);
		
		ClassMapBuilder<Hospitals, SearchWithdrawCashLessProcessTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchWithdrawCashLessProcessTableDTO.class);
		
		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("claim.claimId", "claimNo");
		classMap.field("policy.product.key", "productKey");
		//classMap.field("policy", "lob");
		//classMap.field("intimation.insuredPatientName", "");
		classMap.field("status.processValue","claimStatus");
		classMap.field("intimation.insured.insuredName", "insuredPatientName");
		classMap.field("intimation.hospital", "hospitalId");
		
		
		classMap.field("intimation.hospitalType.key", "hospitalTypeId");
		
		//Hospital info map -- starts
		classHospMap.field("name","hospitalName");
		classHospMap.field("city","hospitalCity");
		classHospMap.field("key", "key");
		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
		
	}
	
	public static List<SearchWithdrawCashLessProcessTableDTO> getWithdrawTableDTO(
			List<Preauth> searchClaimTableList) {
		List<SearchWithdrawCashLessProcessTableDTO> mapAsList = tableMapper
				.mapAsList(searchClaimTableList,
						SearchWithdrawCashLessProcessTableDTO.class);
		return mapAsList;
	}
	
	public static  List<SearchWithdrawCashLessProcessTableDTO> getHospitalInfoListForWithdraw(List<Hospitals> hospitalInfoList)
	{
		//List<SearchWithdrawCashLessProcessTableDTO> tableDTO = new ArrayList<SearchWithdrawCashLessProcessTableDTO>();
		List<SearchWithdrawCashLessProcessTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchWithdrawCashLessProcessTableDTO.class);
		return mapAsList;
	}
	
	
	public static WithdrawPreauthMapper getInstance(){
        if(myObj == null){
            myObj = new WithdrawPreauthMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}
