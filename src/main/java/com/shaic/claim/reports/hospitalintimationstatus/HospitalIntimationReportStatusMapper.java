package com.shaic.claim.reports.hospitalintimationstatus;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class HospitalIntimationReportStatusMapper {
	private static MapperFacade tableMapper;
	
	static HospitalIntimationReportStatusMapper myObj;
	
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	//private static ClassMapBuilder<Intimation, HospitalIntimationReportStatusTableDTO> classMapForHospitalIntimationReport = mapperFactory.classMap(Intimation.class, HospitalIntimationReportStatusTableDTO.class);
	private static ClassMapBuilder<Intimation, HospitalIntimationReportStatusTableDTO> classMapForHospitalIntimationReport = null;
	
	public static void getAllMapValues()  {
		
		classMapForHospitalIntimationReport = mapperFactory.classMap(Intimation.class, HospitalIntimationReportStatusTableDTO.class);
		
		classMapForHospitalIntimationReport.field("key","intimationKey");
		classMapForHospitalIntimationReport.field("intimationId","intimationNo");
		classMapForHospitalIntimationReport.field("policyNumber","policyNo");
		classMapForHospitalIntimationReport.field("createdDate","intimationDate");
		classMapForHospitalIntimationReport.field("policy.product.code","productCode");
		classMapForHospitalIntimationReport.field("insured","insuredName");
		//classMapForHospitalIntimationReport.field("status.processValue","stage");
		classMapForHospitalIntimationReport.field("insured","insuredName");
		classMapForHospitalIntimationReport.field("hospital","hospitalNameId");
						
		classMapForHospitalIntimationReport.register();
		tableMapper = mapperFactory.getMapperFacade();
		
	}
	public static List<HospitalIntimationReportStatusTableDTO> getHospitalIntimationTableObjects(List<Intimation> intimationtList)
    {
	List<HospitalIntimationReportStatusTableDTO> hospitalIntimationTableObjectList = tableMapper.mapAsList(intimationtList, HospitalIntimationReportStatusTableDTO.class);
	return hospitalIntimationTableObjectList;
    }
	
	public static HospitalIntimationReportStatusMapper getInstance(){
        if(myObj == null){
            myObj = new HospitalIntimationReportStatusMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}
