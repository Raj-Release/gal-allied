package com.shaic.claim.userproduct.document.search;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.TmpEmployee;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class UserManagementMapper {


private static MapperFacade tableMapper;
	
	
	private static BoundMapperFacade<TmpEmployee, SearchDoctorDetailsTableDTO> doctorDetailsMapper;
	
	
	
	static {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		
		ClassMapBuilder<TmpEmployee, SearchDoctorDetailsTableDTO> claimClassMap = mapperFactor.classMap(TmpEmployee.class, SearchDoctorDetailsTableDTO.class);
		
		claimClassMap.field("empId", "empId");
		claimClassMap.field("empFirstName", "doctorName");
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		doctorDetailsMapper = mapperFactor.getMapperFacade(TmpEmployee.class, SearchDoctorDetailsTableDTO.class);
		
		
	}
	
	public static List<SearchDoctorDetailsTableDTO> getDoctorDetails(List<TmpEmployee> doctorNAme) {
		List<SearchDoctorDetailsTableDTO> mapAsList = 
				tableMapper.mapAsList(doctorNAme, SearchDoctorDetailsTableDTO.class);
		return mapAsList;
	}
	

}
