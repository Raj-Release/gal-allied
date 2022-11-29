package com.shaic.claim.registration.updateHospitalDetails;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.UpdateHospital;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class UpdateHospitalDetailsMapper {
	
	static UpdateHospitalDetailsMapper  myObj;

	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	private static BoundMapperFacade<UpdateHospital, UpdateHospitalDetailsDTO> hospitalAckMapper;
	
	 //private static ClassMapBuilder<UpdateHospital, UpdateHospitalDetailsDTO> hospitalAckMap = mapperFactory.classMap(UpdateHospital.class,UpdateHospitalDetailsDTO.class);
	private static ClassMapBuilder<UpdateHospital, UpdateHospitalDetailsDTO> hospitalAckMap = null;
	 
	 public static void getAllMapValues()  {
		 
		 hospitalAckMap = mapperFactory.classMap(UpdateHospital.class,UpdateHospitalDetailsDTO.class);

		 hospitalAckMap.field("hospitalId", "key");
		 hospitalAckMap.field("modeOfIntimationId", "modeOfIntimation.id");
		 hospitalAckMap.field("intimatedById", "intimatedBy.id");
		// hospitalAckMap.field("hospitalId", "hospitalCode");
		// hospitalAckMap.field("", "hospitalCodeIrda");
		// hospitalAckMap.field("activeStatusDate", "activeStatusDate");
		 hospitalAckMap.field("address", "address");
		 hospitalAckMap.field("createdBy", "createdBy");
		 hospitalAckMap.field("localityId", "localityId.id");
		 hospitalAckMap.field("createdDate", "createdDate");
		 hospitalAckMap.field("mobileNumber", "contactNo");
		 hospitalAckMap.field("remarks", "remarks");
		 
		 hospitalAckMap.field("emailId", "emailId");
		 hospitalAckMap.field("faxNumber", "faxNumber");
		 hospitalAckMap.field("hospitalId", "hospitalId");
		 hospitalAckMap.field("hospitalName", "hospitalName");
		 hospitalAckMap.field("hospitalTypeId", "hospitalTypeId");
		 hospitalAckMap.field("officeCode", "officeCode");
		 hospitalAckMap.field("phoneNumber", "phoneNumber");
		 hospitalAckMap.field("pincode", "pincode");
		 hospitalAckMap.field("remarks", "remarks");
		 hospitalAckMap.field("cityId", "city.id");
		 hospitalAckMap.field("stateId", "state.id");
		 
		 hospitalAckMap.register();
		 
		 hospitalAckMapper=mapperFactory.getMapperFacade(UpdateHospital.class, UpdateHospitalDetailsDTO.class);
	 }
	 
	 public UpdateHospital getUpdateHospital(UpdateHospitalDetailsDTO bean){
		 UpdateHospital dest = hospitalAckMapper.mapReverse(bean);
			return dest;
	 }
	 
	 
	 public static UpdateHospitalDetailsMapper getInstance(){
	        if(myObj == null){
	            myObj = new UpdateHospitalDetailsMapper();
	            getAllMapValues();
	        }
	        return myObj;
		 }


}
