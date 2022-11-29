package com.shaic.claim.legal.history.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.StageLegalInformation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewLegalHistoryMapper {

	private static MapperFacade tableMapper;
	
	static ViewLegalHistoryMapper  myObj;


	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<StageLegalInformation, ViewLegalHistoryDTO> classMap = mapperFactory
				.classMap(StageLegalInformation.class, ViewLegalHistoryDTO.class);
		classMap.field("claimType.value", "typeofClaim");
		classMap.field("createdDate", "createdDate");
		classMap.field("createdBy", "userID");
		classMap.field("stage.stageName", "claimStage");
		classMap.field("status.processValue", "status");
		classMap.field("status.key", "statusID");
		classMap.field("statusRemarks", "userRemark");
		classMap.field("reimbursementKey", "reimbursementKey");
		classMap.field("cashlessKey","cashlessKey");
		classMap.field("key", "historyKey");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewLegalHistoryDTO> getViewClaimHistoryDTO(
			List<StageLegalInformation> claimHistoryList) {
		List<ViewLegalHistoryDTO> mapAsList = tableMapper.mapAsList(
				claimHistoryList, ViewLegalHistoryDTO.class);
		return mapAsList;
	}
	
	
	public static ViewLegalHistoryMapper getInstance(){
        if(myObj == null){
            myObj = new ViewLegalHistoryMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
