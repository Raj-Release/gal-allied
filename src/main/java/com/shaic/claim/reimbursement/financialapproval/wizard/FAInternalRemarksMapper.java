package com.shaic.claim.reimbursement.financialapproval.wizard;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.dto.FAInternalRemarksTableDTO;
import com.shaic.domain.preauth.InternalInfo;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class FAInternalRemarksMapper {

	private static MapperFacade tableMapper;
	public static FAInternalRemarksMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<InternalInfo, FAInternalRemarksTableDTO> classMap = mapperFactory.classMap(InternalInfo.class, FAInternalRemarksTableDTO.class);
		classMap.field("claimTypeId", "claimType");
		classMap.field("refNo", "refNoRodNo");
		classMap.field("createdDate", "createdDate");
		classMap.field("createdBy", "userId");
		classMap.field("statusRemarks", "internalRemarks");
		classMap.field("remarks", "remarks");

		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<FAInternalRemarksTableDTO> getFAInternalRemarksTrials(List<InternalInfo> resultList) {
		List<FAInternalRemarksTableDTO> mapAsList = tableMapper.mapAsList(resultList, FAInternalRemarksTableDTO.class);
		return mapAsList;
	}

	public static FAInternalRemarksMapper getInstance() {
		if(myObj == null) {
			myObj = new FAInternalRemarksMapper();
			getAllMapValues();
		}
		return myObj;
	}
}
