package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Investigation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class InvestigationDetailsMapper {

	private MapperFacade mapper;

	public InvestigationDetailsMapper() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Investigation, InvestigationDetailsReimbursementTableDTO> classMap = mapperFactory.classMap(Investigation.class,
						InvestigationDetailsReimbursementTableDTO.class);

		classMap.field("key", "key");
		classMap.field("investigatorName", "investigatorName");
		classMap.field("investigatorCode", "investigatorCode");
		// classMap.field("allocationTo.value", "investigatorContactNo");
		classMap.field("intimation.hospital", "hospitalName");
		classMap.field("remarks", "remarks");
		classMap.field("assignedDate", "investigationAssignedDate");
		classMap.field("completionDate", "investigationCompletedDate");
		classMap.field("status.processValue", "investigatorStatus");

		classMap.register();

		this.mapper = mapperFactory.getMapperFacade();

	}

	public List<Investigation> getInvestigation(
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTO) {
		List<Investigation> dest = mapper.mapAsList(
				investigationDetailsReimbursementTableDTO, Investigation.class);
		return dest;
	}

	public List<InvestigationDetailsReimbursementTableDTO> getInvestigationDto(
			List<Investigation> investigation) {
		List<InvestigationDetailsReimbursementTableDTO> dest = mapper.mapAsList(
				investigation,
				InvestigationDetailsReimbursementTableDTO.class);
		return dest;
	}

}
