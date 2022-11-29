package com.shaic.claim.medical.opinion;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.RecMarkEscalation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class RecMarkEscalationMapper {
	
	static RecMarkEscalationMapper myObj;
	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	private static MapperFacade recMarkEscMapper;
	
	private static BoundMapperFacade<RecMarkEscalation, RecordMarkEscDTO> recMarkEscqueryMapper;

	
	private static ClassMapBuilder<RecMarkEscalation, RecordMarkEscDTO> recMarkEscMap = mapperFactory.classMap(RecMarkEscalation.class, RecordMarkEscDTO.class);
	
	public static void getAllMapValues()  { 
		 
		recMarkEscMap.field("refNumber", "refNumber");
		recMarkEscMap.field("intimationNumber", "intimationNumber");
		recMarkEscMap.field("escalatedRole", "escalatedRole");
		recMarkEscMap.field("escalatedDate", "escalatedDate");
		recMarkEscMap.field("escalatedBy", "escalatedBy");
		recMarkEscMap.field("escalationReason", "escalationReason");
		recMarkEscMap.field("actionTaken", "actionTaken");
		recMarkEscMap.field("doctorRemarks", "doctorRemarks");
		recMarkEscMap.field("escalationRemarks", "escalationRemarks");
		recMarkEscMap.field("statusId", "statusId");
		recMarkEscMap.field("stageId", "stageId");
		recMarkEscMap.field("createdBy", "createdBy");
		recMarkEscMap.field("createdDate", "createdDate");
		recMarkEscMap.field("modifiedBy", "modifiedBy");
		recMarkEscMap.field("modifiedDate", "modifiedDate");
		
		recMarkEscMapper = mapperFactory.getMapperFacade();
		recMarkEscqueryMapper=mapperFactory.getMapperFacade(RecMarkEscalation.class, RecordMarkEscDTO.class);
	}
		
		
	public static List<RecordMarkEscDTO> getRecordMarkEscDTO(List<RecMarkEscalation> resultList) {
		recMarkEscMap.register();
		List<RecordMarkEscDTO> mapAsList = recMarkEscMapper.mapAsList(resultList, RecordMarkEscDTO.class);
			return mapAsList;
		
	}
	
	public static RecMarkEscalation getRecMarkEscalation(RecordMarkEscDTO bean) {
		RecMarkEscalation dest=recMarkEscqueryMapper.mapReverse(bean);
		return dest;
	}
	
	public static RecMarkEscalationMapper getInstance(){
        if(myObj == null){
            myObj = new RecMarkEscalationMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	

	
}
