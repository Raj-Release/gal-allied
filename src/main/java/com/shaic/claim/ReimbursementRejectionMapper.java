package com.shaic.claim;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.ReimbursementRejection;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * 
 * @author Lakshminarayana
 *
 */
public class ReimbursementRejectionMapper {
private MapperFacade mapper;
	
	public ReimbursementRejectionMapper() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<ReimbursementRejection, ReimbursementRejectionDto> classMap = mapperFactory.classMap(ReimbursementRejection.class, ReimbursementRejectionDto.class);
		
		classMap.field("key","key");
		classMap.field("reimbursement.key", "reimbursementDto.key");
		classMap.field("reimbursement.claim.key", "reimbursementDto.claimDto.key");
		classMap.field("rejectionCategory.key","rejCategSelectValue.id");
		classMap.field("rejectionCategory.value","rejCategSelectValue.value");
		classMap.field("rejectionRemarks","rejectionRemarks");
		classMap.field("rejectionRemarks2","rejectionRemarks2");
		classMap.field("rejectionLetterRemarks","rejectionLetterRemarks");
		classMap.field("rejectionLetterRemarks2","rejectionLetterRemarks2");
		classMap.field("redraftRemarks","redraftRemarks");
		classMap.field("disapprovedRemarks","disapprovedRemarks");
		classMap.field("rejectionDraftDate","rejectionLetterDate");
		classMap.field("redraftDate","redraftDate");
		classMap.field("disapprovedDate","disapprovedDate");
		classMap.field("approvedRejectionDate","approvedRejectionDate");
		classMap.field("createdBy","createdBy");
		classMap.field("createdDate","createdDate");
		classMap.field("modifiedBy","modifiedBy");
		classMap.field("modifiedBy","letterDraftedUser");
		classMap.field("modifiedDate","modifiedDate");
		classMap.field("status.processValue","statusValue");
		classMap.field("status.key","statusKey");
		classMap.field("allowReconsideration", "allowReconsider");
		
		classMap.register();
		 
		 
		 this.mapper = mapperFactory.getMapperFacade();
	}
	
	public ReimbursementRejection getReimbursementRejection(ReimbursementRejectionDto reimbursementRejectionDto) {
		ReimbursementRejection dest = mapper.map(reimbursementRejectionDto, ReimbursementRejection.class);
		
		return dest;
	}
	
	public ReimbursementRejectionDto getReimbursementRejectionDto(ReimbursementRejection reimbursementRejection) {
		ReimbursementRejectionDto dest = mapper.map(reimbursementRejection, ReimbursementRejectionDto.class);
		return dest;
	}

}
