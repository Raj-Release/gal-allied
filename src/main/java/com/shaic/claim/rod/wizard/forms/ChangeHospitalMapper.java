package com.shaic.claim.rod.wizard.forms;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.domain.Hospitals;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ChangeHospitalMapper {
	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	static ChangeHospitalMapper myObj;
	
	private static BoundMapperFacade<Hospitals, UpdateHospitalDetailsDTO> hospitalMapper;
	
//	private static BoundMapperFacade<UpdateHospital, UpdateHospitalDetailsDTO> updateHospitalMapper;
	
	
	//private static ClassMapBuilder<Hospitals, UpdateHospitalDetailsDTO> hospitalMap = mapperFactory.classMap(Hospitals.class,UpdateHospitalDetailsDTO.class);
	private static ClassMapBuilder<Hospitals, UpdateHospitalDetailsDTO> hospitalMap = null;
	
//	private static ClassMapBuilder<UpdateHospital, UpdateHospitalDetailsDTO> updateHosp = mapperFactory.classMap(UpdateHospital.class,UpdateHospitalDetailsDTO.class);
	
	
	
	 public static void getAllMapValues() {
		 
		hospitalMap = mapperFactory.classMap(Hospitals.class,UpdateHospitalDetailsDTO.class);
		 
		hospitalMap.field("key", "key");
		hospitalMap.field("stateId", "state.id");
		hospitalMap.field("state", "state.value");
		hospitalMap.field("cityId", "city.id");
		hospitalMap.field("city", "city.value");
	//	hospitalMap.field("localityId", "area.id");
		//hospitalMap.field("locality", "area.value");
		hospitalMap.field("phoneNumber", "contactNo");
		hospitalMap.field("name", "hospitalName");
		hospitalMap.field("fax", "faxNumber");
		hospitalMap.field("hospitalType.key", "hospitalType.id");
		hospitalMap.field("hospitalType.value", "hospitalType.value");
		hospitalMap.field("mobileNumber", "mobileNumber");
		hospitalMap.field("address", "address");
		hospitalMap.field("emailId", "emailId");
		hospitalMap.field("hospitalCode", "hospitalCode");
		hospitalMap.field("hospitalIrdaCode", "hospitalCodeIrda");
		hospitalMap.field("pincode", "pincode");
		
//		updateHosp.field("stateId", "state.id");
//		updateHosp.field("cityId", "city.id");
//		updateHosp.field("localityId", "area.id");
//		updateHosp.field("phoneNumber", "contactNo");
//		updateHosp.field("hospitalName", "hospitalName");
//		updateHosp.field("faxNumber", "faxNumber");
//		updateHosp.field("hospitalTypeId", "hospitalType.id");
//		updateHosp.field("hospitalType.value", "hospitalType.value");
//		updateHosp.field("mobileNumber", "mobileNumber");
//		updateHosp.field("address", "address");
//		updateHosp.field("emailId", "emailId");
//		updateHosp.field("hospitalCode", "hospitalCode");
//		updateHosp.field("hospitalIrdaCode", "hospitalCodeIrda");
//		updateHosp.field("pincode", "pincode");
		
		hospitalMapper = mapperFactory.getMapperFacade(Hospitals.class, UpdateHospitalDetailsDTO.class);
		
//		updateHospitalMapper = mapperFactory.getMapperFacade(UpdateHospital.class, UpdateHospitalDetailsDTO.class);
		
	}
	
	public static UpdateHospitalDetailsDTO getHosptialsDTO(Hospitals hospitals){
		hospitalMap.register();
		UpdateHospitalDetailsDTO dest = hospitalMapper.map(hospitals);
		return dest;
	}
	
	
	public static ChangeHospitalMapper getInstance(){
        if(myObj == null){
            myObj = new ChangeHospitalMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
//	public static UpdateHospitalDetailsDTO getUpdateHospitalsDTO(UpdateHospital hospitals){
//		updateHosp.register();
//		UpdateHospitalDetailsDTO dest = updateHospitalMapper.map(hospitals);
//		return dest;
//	}
	
	
	

}
