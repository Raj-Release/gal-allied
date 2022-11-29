package com.shaic.claim.omp.carousel;

	import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.OMPIntimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

	public class OmpRevisedIntimationMapper {
		
		private static MapperFactory mapperFactory =  new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
		
		private static OmpRevisedIntimationMapper mapperObj;
		
		private static MapperFacade mapperFacade;
		
		private static ClassMapBuilder<OMPIntimation, NewIntimationDto> classMap = mapperFactory.classMap(OMPIntimation.class, NewIntimationDto.class);
		
		public static OmpRevisedIntimationMapper getInstance(){
			if(mapperObj == null){
				mapperObj = new OmpRevisedIntimationMapper();
				getAllMapValues();
			}
			return mapperObj;
		}
		
		
		public static void  getAllMapValues(){

			classMap.field("intimationId","intimationId");
			classMap.field("intimationMode.key", "modeOfIntimation.id");
			classMap.field("intimationMode.value", "modeOfIntimation.value");
			classMap.field("intimatedBy.key", "intimatedBy.id");
			classMap.field("intimatedBy.value", "intimatedBy.value");
			classMap.field("callerMobileNumber", "callerContactNum");
			classMap.field("callerLandlineNumber","callerLandlineNum");
			classMap.field("insured.key", "insuredPatient.key");
			classMap.field("intimaterName", "intimaterName");
			classMap.field("status", "status");
			classMap.field("createdDate", "createdDate");
			classMap.field("key", "key");
			classMap.field("createdBy","createdBy");
			classMap.field("registrationStatus","registrationStatus");
			classMap.field("claimType.key","claimType.id");
			classMap.field("claimType.value","claimType.value");		
			 classMap.field("hospitalType.value", "hospitalType.value");
			 classMap.field("hospitalType.key","hospitalType.id");         
			 classMap.field("hospitalType.key", "hospitalDto.hospitalType.id");
			 classMap.field("hospitalType.value", "hospitalDto.hospitalType.value");
			 classMap.field("hospital", "hospitalDto.key");
			 classMap.field("policyYear", "policyYear");
			 classMap.field("admissionDate", "admissionDate");
			 classMap.field("hospitalComments", "comments");
			 
			 
			classMap.register();
			
			mapperFacade = mapperFactory.getMapperFacade();			
		}	

		public NewIntimationDto getNewIntimationDto(OMPIntimation ompIntimation) {
			NewIntimationDto dest = mapperFacade.map(ompIntimation, NewIntimationDto.class);
			dest.setPolicy(ompIntimation.getPolicy());
			dest.setInsuredPatient(ompIntimation.getInsured());
			return dest;
		}	
}
