package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;

public class BaseMapper {
	
	public final static MapperFactory MAPPER_FACTORY  = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();

}
