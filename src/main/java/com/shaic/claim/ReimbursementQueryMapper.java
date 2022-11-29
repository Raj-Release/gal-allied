package com.shaic.claim;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * 
 * @author Lakshminarayana
 *
 */
public class ReimbursementQueryMapper {

	private MapperFacade mapper;
	
	public ReimbursementQueryMapper() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<ReimbursementQuery, ReimbursementQueryDto> classMap = mapperFactory.classMap(ReimbursementQuery.class, ReimbursementQueryDto.class);
		
		classMap.field("key","key");
		classMap.field("reimbursement.key", "reimbursementDto.key");
		classMap.field("reimbursement.claim.key", "reimbursementDto.claimDto.key");
		classMap.field("queryRemarks","queryRemarks");
		classMap.field("queryLetterRemarks","queryLetterRemarks");
		classMap.field("redraftRemarks","redraftRemarks");
		classMap.field("rejectionRemarks","rejectionRemarks");
		classMap.field("draftedDate","draftedDate");
		classMap.field("reDraftDate","reDraftDate");
		classMap.field("approvedRejectionDate","approvedRejectionDate");
		classMap.field("createdBy","createdBy");
		classMap.field("createdDate","createdDate");
		classMap.field("modifiedBy","modifiedBy");
		classMap.field("modifiedDate","modifiedDate");
		classMap.field("reminderCount","reminderCount");
		classMap.field("reminderDate1","firstReminderDate");
		classMap.field("reminderDate2","secondReminderDate");
		classMap.field("reminderDate3","thirdReminderDate");
		classMap.field("queryReplyDate","queryReplyReceivedDate");
		classMap.field("status.key","statusKey");
		classMap.field("stage.key","stageKey");
		classMap.field("queryType","queryType");
		
		
		classMap.register();
		 
		 
		 this.mapper = mapperFactory.getMapperFacade();
	}
	
	public ReimbursementQuery getReimbursementQuery(ReimbursementQueryDto reimbursementQueryDto) {
		ReimbursementQuery dest = mapper.map(reimbursementQueryDto, ReimbursementQuery.class);
		
		return dest;
	}
	
	public ReimbursementQueryDto getReimbursementQueryDto(ReimbursementQuery reimbursementQuery) {
		ReimbursementQueryDto dest = mapper.map(reimbursementQuery, ReimbursementQueryDto.class);
		return dest;
	}	
}
