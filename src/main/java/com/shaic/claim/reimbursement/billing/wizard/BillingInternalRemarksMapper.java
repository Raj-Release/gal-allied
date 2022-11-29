package com.shaic.claim.reimbursement.billing.wizard;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.billing.dto.BillingInternalRemarksTableDTO;
import com.shaic.domain.preauth.InternalInfo;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class BillingInternalRemarksMapper {

	private static MapperFacade tableMapper;
	public static BillingInternalRemarksMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<InternalInfo, BillingInternalRemarksTableDTO> classMap = mapperFactory.classMap(InternalInfo.class, BillingInternalRemarksTableDTO.class);
		classMap.field("claimTypeId", "claimType");
		classMap.field("refNo", "refNoRodNo");
		classMap.field("createdDate", "createdDate");
		classMap.field("createdBy", "userId");
		classMap.field("statusRemarks", "internalRemarks");
		classMap.field("remarks", "remarks");

		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<BillingInternalRemarksTableDTO> getBillingInternalRemarksTrials(List<InternalInfo> resultList) {
		List<BillingInternalRemarksTableDTO> mapAsList = tableMapper.mapAsList(resultList, BillingInternalRemarksTableDTO.class);
		return mapAsList;
	}

	public static BillingInternalRemarksMapper getInstance() {
		if(myObj == null) {
			myObj = new BillingInternalRemarksMapper();
			getAllMapValues();
		}
		return myObj;
	}
}
