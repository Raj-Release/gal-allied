package com.shaic.claim.claimhistory.view.ompView;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.claimhistory.view.ViewClaimHistoryDTO;
import com.shaic.domain.omp.OMPStageInformation;

public class ViewOMPClaimHistoryMapper {

	private static MapperFacade tableMapper;
	
	static ViewOMPClaimHistoryMapper  myObj;


	public static void getAllMapValues()  {
 
//		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
		ClassMapBuilder<OMPStageInformation, ViewClaimHistoryDTO> classMap = mapperFactory
				.classMap(OMPStageInformation.class, ViewClaimHistoryDTO.class);
		classMap.field("claimType.value", "typeofClaim");
		classMap.field("createdDate", "createdDate");
		classMap.field("createdBy", "userID");
		classMap.field("stage.stageName", "claimStage");
		classMap.field("status.processValue", "status");
		classMap.field("status.key", "statusID");
		classMap.field("statusRemarks", "userRemark");
		classMap.field("reimbursement.key", "reimbursementKey");
		classMap.field("rodType", "rodtype");
		classMap.field("classificationId", "classification");
		classMap.field("subClassificationId", "subclassification");
		classMap.field("classiDocumentRecivedFmId", "docrecdfrom");
//		classMap.field("preauth.key","cashlessKey");
		classMap.field("key", "historyKey");
		classMap.field("acknowledgement.key", "ackKey");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewClaimHistoryDTO> getViewClaimHistoryDTO(
			List<OMPStageInformation> claimHistoryList) {
		List<ViewClaimHistoryDTO> mapAsList = tableMapper.mapAsList(
				claimHistoryList, ViewClaimHistoryDTO.class);
		return mapAsList;
	}
	
	
	public static ViewOMPClaimHistoryMapper getInstance(){
        if(myObj == null){
            myObj = new ViewOMPClaimHistoryMapper();
        }
        getAllMapValues();
        return myObj;
	 }

}
