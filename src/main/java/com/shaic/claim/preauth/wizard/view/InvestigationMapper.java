package com.shaic.claim.preauth.wizard.view;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Investigation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class InvestigationMapper {

	private MapperFacade mapper;
	
	public InvestigationMapper() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Investigation, InvestigationDetailsTableDTO> classMap = mapperFactory.classMap(Investigation.class, InvestigationDetailsTableDTO.class);
//		classMap.field("allocationTo.value","category");
		classMap.field("investigatorName", "investigatorName");
		classMap.field("investigatorCode", "investigatorCode");
//		classMap.field("callerMobileNumber", "investigatorContactNo");
//		classMap.field("attendersMobileNumber", "hospitalName");
//		classMap.field("hospitalVisitedDate", "hospitalVisitedDate");
//		classMap.field("remarks", "remarks");
		classMap.field("investigationTriggerPoints", "investigationTriggerPoints");
		classMap.field("approvedDate", "investigationApprovedDate");
		classMap.field("assignedDate", "investigationAssignedDate");
		classMap.field("completionDate", "investigationCompletedDate");
		classMap.field("completionDate", "investigationCompletedDateStr");		
		classMap.field("reportReceivedDate", "investigationReportReceivedDate");
//		classMap.field("status", "tat");
		classMap.field("status.processValue", "status");
		classMap.field("intimation.hospital", "hospitalkey");
		classMap.field("intimation.key", "intimationkey");
		classMap.field("policy.key", "policyKey");
		classMap.field("claim.key", "claimKey");
		
		classMap.field("key", "key");
	
		 classMap.register();
		 
		 this.mapper = mapperFactory.getMapperFacade();
	}
	
	public Investigation getInvestigation(InvestigationDetailsTableDTO investigationDetailsTableDTO) {
		Investigation dest = mapper.map(investigationDetailsTableDTO, Investigation.class);
		return dest;
	}
	
	public InvestigationDetailsTableDTO getInvestigationDetailsTableDTO(Investigation investigation) {
		InvestigationDetailsTableDTO dest = mapper.map(investigation, InvestigationDetailsTableDTO.class);
		return dest;
	}
	
}
	