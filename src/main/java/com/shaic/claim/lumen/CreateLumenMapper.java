package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.domain.Claim;

public class CreateLumenMapper {

	private static MapperFactory mapperFactory =  new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();

	private static CreateLumenMapper mapperObj;

	private static MapperFacade mapperFacade;

	private static ClassMapBuilder<Claim, LumenSearchResultTableDTO> lumenMapper = mapperFactory.classMap(Claim.class, LumenSearchResultTableDTO.class);

	public static  List<LumenSearchResultTableDTO> getDetails(List<Claim> intimationResultList){
		List<LumenSearchResultTableDTO> mapAsList = mapperFacade.mapAsList(intimationResultList, LumenSearchResultTableDTO.class);
		/*LumenSearchResultTableDTO dtoObj = mapAsList.get(0);
		dtoObj.setClaim(intimationResultList.get(0));*/
		//IMSSUPPOR-31163
		if(mapAsList != null && !mapAsList.isEmpty()){
			for(int i = 0;i<mapAsList.size();i++){
				LumenSearchResultTableDTO dtoObj = mapAsList.get(i);
				dtoObj.setClaim(intimationResultList.get(i));
			}
		}

		return mapAsList;
	}

	public static CreateLumenMapper getInstance(){
		if(mapperObj == null){
			mapperObj = new CreateLumenMapper();
			getAllMapValues();
		}
		return mapperObj;
	}

	public static void  getAllMapValues(){
		lumenMapper.field("intimation.intimationId","intimationNumber");
		lumenMapper.field("intimation.key","intimationKey");
		lumenMapper.field("intimation.cpuCode.cpuCode","cpuCode");
		lumenMapper.field("intimation.cpuCode.description","cpuDesc");
		lumenMapper.field("intimation.policy.policyNumber", "policyNumber");
		lumenMapper.field("intimation.policy.productName", "productName");
		lumenMapper.field("intimation.policy.proposerFirstName", "insuredPatientName");
		lumenMapper.field("intimation.hospital", "hospitalNameId");
		lumenMapper.field("intimation.hospitalType.key", "hospitalType.id");
		lumenMapper.field("intimation.hospitalType.value", "hospitalType.value");
		lumenMapper.field("intimation.admissionReason", "reasonForAdmission");
		lumenMapper.field("intimation.policy", "policy");
		lumenMapper.field("intimation.policy.policyType.key", "policyType.id");
		lumenMapper.field("intimation.policy.policyType.value", "policyType.value");
		lumenMapper.field("intimation.policy.productType.key", "productType.id");
		lumenMapper.field("intimation.policy.productType.value", "productType.value");
		lumenMapper.field("intimation.insured", "policyInsured");
		lumenMapper.field("intimation.lobId.key", "lob.id");
		lumenMapper.field("intimation.lobId.value", "lob.value");
		lumenMapper.register();
		mapperFacade = mapperFactory.getMapperFacade();
	}

}
