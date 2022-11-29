package com.shaic.claims.reibursement.addaditionaldocuments;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SelectRODtoAddAdditionalDocumentsMapper {
	
	private MapperFacade mapper;

	public SelectRODtoAddAdditionalDocumentsMapper() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Reimbursement, SelectRODtoAddAdditionalDocumentsDTO> classMap = mapperFactory.classMap(Reimbursement.class,
				SelectRODtoAddAdditionalDocumentsDTO.class);

		classMap.field("key", "key");
		classMap.field("rodNumber", "rodNo");
		classMap.field("currentProvisionAmt", "approvedAmt");
		classMap.field("status.processValue", "rodStatus");
		classMap.field("status.key", "statusKey");

		classMap.register();

		this.mapper = mapperFactory.getMapperFacade();

	}

	public List<Reimbursement> getReimbursement(
			List<SelectRODtoAddAdditionalDocumentsDTO> selectRODtoAddAdditionalDocumentsDTO) {
		List<Reimbursement> dest = mapper.mapAsList(
				selectRODtoAddAdditionalDocumentsDTO, Reimbursement.class);
		return dest;
	}

	public List<SelectRODtoAddAdditionalDocumentsDTO> getReimbursementDto(
			List<Reimbursement> reimbursement) {
		List<SelectRODtoAddAdditionalDocumentsDTO> dest = mapper.mapAsList(
				reimbursement,
				SelectRODtoAddAdditionalDocumentsDTO.class);
		return dest;
	}

}
