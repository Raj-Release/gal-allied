package com.shaic.claim.claimhistory.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.OPStageInformation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewOPClaimHistoryMapper {

	private static MapperFacade tableMapper;
	
	static ViewOPClaimHistoryMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<OPStageInformation, ViewClaimHistoryDTO> classMap = mapperFactory
				.classMap(OPStageInformation.class, ViewClaimHistoryDTO.class);
		classMap.field("claimType.value", "typeofClaim");
		classMap.field("createdDate", "createdDate");
		classMap.field("createdBy", "userID");
		classMap.field("stage.stageName", "claimStage");
		classMap.field("status.processValue", "status");
		classMap.field("status.key", "statusID");
		classMap.field("statusRemarks", "userRemark");
		classMap.field("reimbursement.key", "reimbursementKey");
		classMap.field("preauth.key","cashlessKey");
		classMap.field("key", "historyKey");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewClaimHistoryDTO> getViewClaimHistoryDTO(
			List<OPStageInformation> claimHistoryList) {
		List<ViewClaimHistoryDTO> mapAsList = tableMapper.mapAsList(
				claimHistoryList, ViewClaimHistoryDTO.class);
		return mapAsList;
	}
	
	
	public static ViewOPClaimHistoryMapper getInstance(){
        if(myObj == null){
            myObj = new ViewOPClaimHistoryMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
