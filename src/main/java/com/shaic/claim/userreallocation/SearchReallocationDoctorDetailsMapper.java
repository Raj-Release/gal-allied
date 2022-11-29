package com.shaic.claim.userreallocation;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.MasUser;
import com.shaic.newcode.wizard.domain.MappingUtil;


public class SearchReallocationDoctorDetailsMapper {

private static MapperFacade tableMapper;
	
	
	private static BoundMapperFacade<MasUser, SearchReallocationDoctorDetailsTableDTO> doctorDetailsMapper;
	
	
	
	static {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		
		ClassMapBuilder<MasUser, SearchReallocationDoctorDetailsTableDTO> claimClassMap = mapperFactor.classMap(MasUser.class, SearchReallocationDoctorDetailsTableDTO.class);
		
		claimClassMap.field("empId", "empId");
		claimClassMap.field("userName", "doctorName");
		claimClassMap.field("userId", "loginId");
		/*claimClassMap.field("minAmt", "minAmt");
		claimClassMap.field("maxAmt", "maxAmt");*/
		
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		doctorDetailsMapper = mapperFactor.getMapperFacade(MasUser.class, SearchReallocationDoctorDetailsTableDTO.class);
		
		
	}
	
	public static List<SearchReallocationDoctorDetailsTableDTO> getClaimDTO(List<MasUser> claimData){
		List<SearchReallocationDoctorDetailsTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchReallocationDoctorDetailsTableDTO.class);
		return mapAsList;
		
	}
	
	public static SearchReallocationDoctorDetailsTableDTO getSearchClaimDTO(MasUser claim){
		
		SearchReallocationDoctorDetailsTableDTO dto = doctorDetailsMapper.map(claim);
		
		return dto;
	}

}
