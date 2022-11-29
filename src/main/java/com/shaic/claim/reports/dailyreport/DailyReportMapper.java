package com.shaic.claim.reports.dailyreport;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class DailyReportMapper {

	private static MapperFacade tableMapper;
	
	static DailyReportMapper myObj;
	
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	//private static ClassMapBuilder<Preauth, DailyReportTableDTO> classMapForDailyReport = mapperFactory.classMap(Preauth.class, DailyReportTableDTO.class);
	private static ClassMapBuilder<Preauth, DailyReportTableDTO> classMapForDailyReport = null;
	
	 public static void getAllMapValues()  {
		 classMapForDailyReport = mapperFactory.classMap(Preauth.class, DailyReportTableDTO.class);
		
		classMapForDailyReport.field("intimation.key","key");
		classMapForDailyReport.field("intimation.intimationId","intimationNo");
		classMapForDailyReport.field("intimation.policyNumber","policyNumber");
		classMapForDailyReport.field("intimation.createdDate","intimationDate");
		classMapForDailyReport.field("intimation.policy.productType.value","productType");
		classMapForDailyReport.field("intimation.insured.insuredName","insuredName");
		classMapForDailyReport.field("intimation.policy.proposerFirstName","mainMemberName");
		classMapForDailyReport.field("intimation.callerMobileNumber","callerContactNo");
		classMapForDailyReport.field("intimation.insuredPatientName","patientName");
		classMapForDailyReport.field("intimation.hospital","hospitalNameId");	
		classMapForDailyReport.field("intimation.insured.insuredAge","patientAge");
		classMapForDailyReport.field("intimation.admissionReason","admissionReason");
		classMapForDailyReport.field("intimation.cpuCode.cpuCode","cpuCode");
		classMapForDailyReport.field("totalApprovalAmount","totalAuthAmount");
		classMapForDailyReport.field("modifiedDate","authDate");
		classMapForDailyReport.field("doctorNote","cashlessReason");
		classMapForDailyReport.field("status.processValue","cashlessStatus");
		classMapForDailyReport.field("claim.dataOfAdmission","hospitalDate");
		classMapForDailyReport.field("intimation.registrationStatus","registrationStatus");
		
		
		
		
		classMapForDailyReport.register();
		tableMapper = mapperFactory.getMapperFacade();
		
	}
	public static List<DailyReportTableDTO> getDailyReportTableObjects(List<Preauth> intimationtList)
    {
	List<DailyReportTableDTO> dailyReportTableObjectList = tableMapper.mapAsList(intimationtList, DailyReportTableDTO.class);
	return dailyReportTableObjectList;
    }
	
	
	public static DailyReportMapper getInstance(){
        if(myObj == null){
            myObj = new DailyReportMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}
