package com.shaic.claim.claimhistory.view;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.arch.table.AuditDetails;
import com.shaic.arch.table.AuditTrails;
import com.shaic.claim.pcc.hrmp.HRMStageInformation;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewClaimHistoryMapper {

	private static MapperFacade tableMapper;
	
	static ViewClaimHistoryMapper  myObj;
    
	private static MapperFacade tableHRMMapper;
	static ViewClaimHistoryMapper  myHRMObj;

	public static void getAllMapValues()  {
 
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<StageInformation, ViewClaimHistoryDTO> classMap = mapperFactory
				.classMap(StageInformation.class, ViewClaimHistoryDTO.class);
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
		classMap.field("status.portalStatus", "portalStatusVal");
		classMap.field("status.websiteStatus", "websiteStatusVal");

		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<ViewClaimHistoryDTO> getViewClaimHistoryDTO(
			List<StageInformation> claimHistoryList) {
		List<ViewClaimHistoryDTO> mapAsList = tableMapper.mapAsList(
				claimHistoryList, ViewClaimHistoryDTO.class);
		return mapAsList;
	}
	
	
	public static ViewClaimHistoryMapper getInstance(){
        if(myObj == null){
            myObj = new ViewClaimHistoryMapper();
            getAllMapValues();
        }
        return myObj;
	 }

	public static List<ViewClaimHistoryDTO> getViewAuditClaimHistoryDTO(
			List<AuditTrails> claimHistoryList) {
		List<ViewClaimHistoryDTO> mapAsList = tableMapper.mapAsList(
				claimHistoryList, ViewClaimHistoryDTO.class);
		return mapAsList;
	}
	
	public static void getAllHRMMapValues()  {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<HRMStageInformation, ViewClaimHistoryDTO> classMap = mapperFactory
				.classMap(HRMStageInformation.class, ViewClaimHistoryDTO.class);
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
		classMap.field("status.portalStatus", "portalStatusVal");
		classMap.field("status.websiteStatus", "websiteStatusVal");

		classMap.register();
		tableHRMMapper = mapperFactory.getMapperFacade();
	}
	public static ViewClaimHistoryMapper getHRMInstance(){
		if(myHRMObj == null){
			myHRMObj = new ViewClaimHistoryMapper();
			getAllHRMMapValues();
		}
		return myHRMObj;
	}
	public static List<ViewClaimHistoryDTO> getViewHRMClaimHistoryDTO(
			List<HRMStageInformation> claimHistoryList) {
		List<ViewClaimHistoryDTO> mapAsList = tableHRMMapper.mapAsList(
				claimHistoryList, ViewClaimHistoryDTO.class);
		return mapAsList;
	}
}
