package com.shaic.claim.withdrawPostProcessWizard;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess.SearchWithdrawCashLessPostProcessTableDTO;
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class WithdrawPreauthPostProcessMapper {

	private static MapperFacade tableMapper;

	static WithdrawPreauthPostProcessMapper  myObj;


	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, SearchWithdrawCashLessPostProcessTableDTO> classMap = mapperFactory
				.classMap(Preauth.class,
						SearchWithdrawCashLessPostProcessTableDTO.class);

		ClassMapBuilder<Hospitals, SearchWithdrawCashLessPostProcessTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchWithdrawCashLessPostProcessTableDTO.class);

		classMap.field("key", "key");
		classMap.field("intimation.intimationId", "intimationNo");
		classMap.field("claim.claimId", "claimNo");
		classMap.field("claim.key", "claimKey");
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

	public static List<SearchWithdrawCashLessPostProcessTableDTO> getWithdrawTableDTO(
			List<Preauth> searchClaimTableList) {
		List<SearchWithdrawCashLessPostProcessTableDTO> mapAsList = tableMapper
				.mapAsList(searchClaimTableList,
						SearchWithdrawCashLessPostProcessTableDTO.class);
		return mapAsList;
	}

	public static  List<SearchWithdrawCashLessPostProcessTableDTO> getHospitalInfoListForWithdraw(List<Hospitals> hospitalInfoList)
	{
		//List<SearchWithdrawCashLessProcessTableDTO> tableDTO = new ArrayList<SearchWithdrawCashLessProcessTableDTO>();
		List<SearchWithdrawCashLessPostProcessTableDTO> mapAsList = tableMapper.mapAsList(hospitalInfoList, SearchWithdrawCashLessPostProcessTableDTO.class);
		return mapAsList;
	}

	public static WithdrawPreauthPostProcessMapper getInstance() {
		if(myObj == null){
			myObj = new WithdrawPreauthPostProcessMapper();
			getAllMapValues();
		}
		return myObj;
	}
}


