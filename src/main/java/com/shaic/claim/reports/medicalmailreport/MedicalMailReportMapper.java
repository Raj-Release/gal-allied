package com.shaic.claim.reports.medicalmailreport;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.preauth.PreauthQuery;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class MedicalMailReportMapper {
	
	
	static MedicalMailReportMapper myObj;
	
	private static MapperFacade tableMapper;
	private static MapperFacade tableMapper1;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	/*private static ClassMapBuilder<PreauthQuery, MedicalMailReportTableDTO> classMapForMedicalMailReport = mapperFactory.classMap(PreauthQuery.class, MedicalMailReportTableDTO.class);
	private static ClassMapBuilder<ReimbursementQuery, MedicalMailReportTableDTO> classMapForMedicalMailReport1 = mapperFactory.classMap(ReimbursementQuery.class, MedicalMailReportTableDTO.class);*/
	
	private static ClassMapBuilder<PreauthQuery, MedicalMailReportTableDTO> classMapForMedicalMailReport = null;
	private static ClassMapBuilder<ReimbursementQuery, MedicalMailReportTableDTO> classMapForMedicalMailReport1 = null;
	
	
	 public static void getAllMapValues()  {
		 
		classMapForMedicalMailReport = mapperFactory.classMap(PreauthQuery.class, MedicalMailReportTableDTO.class);
		classMapForMedicalMailReport1 = mapperFactory.classMap(ReimbursementQuery.class, MedicalMailReportTableDTO.class);
		
		classMapForMedicalMailReport.field("queryRemarks","reasonForQuery");
		classMapForMedicalMailReport.field("createdDate","queryDate");
		classMapForMedicalMailReport.field("status.processValue","status");
		classMapForMedicalMailReport.field("preauth.intimation.intimationId","intimationNo");
		classMapForMedicalMailReport.field("officeCode","officeCode");
		classMapForMedicalMailReport.field("preauth.intimation.cpuCode.cpuCode","cpuCode");	
		
		classMapForMedicalMailReport1.field("queryRemarks","reasonForQuery");
		classMapForMedicalMailReport1.field("createdDate","queryDate");
		classMapForMedicalMailReport1.field("status.processValue","status");
		classMapForMedicalMailReport1.field("reimbursement.claim.intimation.intimationId","intimationNo");
		classMapForMedicalMailReport1.field("reimbursement.claim.intimation.cpuCode.cpuCode","cpuCode");
		classMapForMedicalMailReport1.field("reimbursement.claim.intimation.officeCode","officeCode");
				
						
		classMapForMedicalMailReport.register();
		classMapForMedicalMailReport1.register();
		tableMapper = mapperFactory.getMapperFacade();
		tableMapper1 = mapperFactory.getMapperFacade();
		
	}
	public static List<MedicalMailReportTableDTO> getMedicalMailTableObjects(List<PreauthQuery> preauthQueryList )
    {
	List<MedicalMailReportTableDTO> medicalMailTableObjectList = tableMapper.mapAsList(preauthQueryList, MedicalMailReportTableDTO.class);
	return medicalMailTableObjectList;
    }
	
	public static List<MedicalMailReportTableDTO> getMedicalMailTableObjects1(List<ReimbursementQuery> reimbursementQuerytList) {
		List<MedicalMailReportTableDTO> medicalMailTableObjectList1 = tableMapper1.mapAsList(reimbursementQuerytList, MedicalMailReportTableDTO.class);
		return medicalMailTableObjectList1;

	}
	
	public static MedicalMailReportMapper getInstance(){
        if(myObj == null){
            myObj = new MedicalMailReportMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
