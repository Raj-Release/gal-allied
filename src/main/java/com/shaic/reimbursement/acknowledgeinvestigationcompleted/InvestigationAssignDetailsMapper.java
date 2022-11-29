package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class InvestigationAssignDetailsMapper {

	private MapperFacade mapper;

	public InvestigationAssignDetailsMapper() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<AssignedInvestigatiorDetails, InvestigationDetailsReimbursementTableDTO> classMap = mapperFactory.classMap(AssignedInvestigatiorDetails.class,
						InvestigationDetailsReimbursementTableDTO.class);

		classMap.field("key", "key");
		classMap.field("investigatorName", "investigatorName");
		classMap.field("investigatorCode", "investigatorCode");
		// classMap.field("allocationTo.value", "investigatorContactNo");
		classMap.field("investigation.intimation.hospital", "hospitalName");
		classMap.field("investigation.remarks", "remarks");
		classMap.field("createdDate", "investigationAssignedDate");
		classMap.field("completionDate", "investigationCompletedDate");
		classMap.field("status.processValue", "investigatorStatus");

		classMap.register();

		this.mapper = mapperFactory.getMapperFacade();

	}

	public List<AssignedInvestigatiorDetails> getAssignedInvestigation(
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTO) {
		List<AssignedInvestigatiorDetails> dest = mapper.mapAsList(
				investigationDetailsReimbursementTableDTO, AssignedInvestigatiorDetails.class);
		return dest;
	}

	public List<InvestigationDetailsReimbursementTableDTO> getAssignedInvestigationDto(
			List<AssignedInvestigatiorDetails> assignedInvestigation) {
		List<InvestigationDetailsReimbursementTableDTO> dest = mapper.mapAsList(
				assignedInvestigation,
				InvestigationDetailsReimbursementTableDTO.class);
		return dest;
	}

}
