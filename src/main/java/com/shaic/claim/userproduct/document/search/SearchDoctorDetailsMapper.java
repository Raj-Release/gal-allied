package com.shaic.claim.userproduct.document.search;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.MasUser;
import com.shaic.domain.TmpEmployee;
import com.shaic.newcode.wizard.domain.MappingUtil;


public class SearchDoctorDetailsMapper {

private static MapperFacade tableMapper;
	
	
	private static BoundMapperFacade<MasUser, SearchDoctorDetailsTableDTO> doctorDetailsMapper;
	
	
	
	static {
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		
		ClassMapBuilder<MasUser, SearchDoctorDetailsTableDTO> claimClassMap = mapperFactor.classMap(MasUser.class, SearchDoctorDetailsTableDTO.class);
		
		claimClassMap.field("empId", "empId");
		claimClassMap.field("userName", "doctorName");
		claimClassMap.field("userId", "loginId");
		/*claimClassMap.field("minAmt", "minAmt");
		claimClassMap.field("maxAmt", "maxAmt");*/
		
		
		claimClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		
		doctorDetailsMapper = mapperFactor.getMapperFacade(MasUser.class, SearchDoctorDetailsTableDTO.class);
		
		
	}
	
	public static List<SearchDoctorDetailsTableDTO> getClaimDTO(List<MasUser> claimData){
		List<SearchDoctorDetailsTableDTO> mapAsList = 
										tableMapper.mapAsList(claimData, SearchDoctorDetailsTableDTO.class);
		return mapAsList;
		
	}
	
	
	public static SearchDoctorDetailsTableDTO getSearchClaimDTO(MasUser claim){
		
		SearchDoctorDetailsTableDTO dto = doctorDetailsMapper.map(claim);
		
		return dto;
	}

}
