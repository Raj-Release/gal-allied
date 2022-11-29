package com.shaic.claim.intimation;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class CashLessTableMapper {

	private static MapperFacade tableMapper;
	
	static CashLessTableMapper myObj;

	 public static void getAllMapValues() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Preauth, CashLessTableDTO> classMap = mapperFactory
				.classMap(Preauth.class,
						CashLessTableDTO.class);
		classMap.field("preauthId", "cashLessType");
		classMap.field("modifiedDate", "responseDate");
		classMap.field("status.processValue", "replyStatus");		
		classMap.field("totalApprovalAmount", "authApprovedAmt");
		classMap.field("remarks", "reason");
		classMap.field("doctorNote", "doctorNote");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public List<CashLessTableDTO> getCashLessTableDTO(
			List<Preauth> preAuthList) {
		List<CashLessTableDTO> mapAsList = tableMapper.mapAsList(
				preAuthList, CashLessTableDTO.class);
		return mapAsList;
	}
	
	
	public static CashLessTableMapper getInstance(){
        if(myObj == null){
            myObj = new CashLessTableMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}
