package com.shaic.claim.reports.hospitalwisereport;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class HospitalWiseReportMapper {
	private static MapperFacade tableMapper;
	
	static HospitalWiseReportMapper myObj;
	
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	//private static ClassMapBuilder<Reimbursement, HospitalWiseReportTableDTO> classMapForHospitalWiseReport = mapperFactory.classMap(Reimbursement.class, HospitalWiseReportTableDTO.class);
	private static ClassMapBuilder<Reimbursement, HospitalWiseReportTableDTO> classMapForHospitalWiseReport = null;
	
	 public static void getAllMapValues()  {
		 		classMapForHospitalWiseReport = mapperFactory.classMap(Reimbursement.class, HospitalWiseReportTableDTO.class);
		  		classMapForHospitalWiseReport.field("claim.intimation.key","key");
		  		classMapForHospitalWiseReport.field("key","preauthKey");
		  		classMapForHospitalWiseReport.field("claim.key","claimKey");
				classMapForHospitalWiseReport.field("claim.intimation.createdDate","intimationDate");
				classMapForHospitalWiseReport.field("claim.intimation.policy.proposerAddress","insuredCity");
				classMapForHospitalWiseReport.field("claim.intimation.hospital","hospitalTypeId");
				classMapForHospitalWiseReport.field("claim.intimation.intimationId","intimationNo");
				classMapForHospitalWiseReport.field("claim.intimation.policy.policyNumber","policyNo");
				classMapForHospitalWiseReport.field("claim.intimation.policy.homeOfficeCode","officeName");
				classMapForHospitalWiseReport.field("claim.intimation.policy.agentCode","agentCode");
				classMapForHospitalWiseReport.field("claim.intimation.policy.agentName","agentName");
				classMapForHospitalWiseReport.field("claim.intimation.policy.smCode","smCode");
				classMapForHospitalWiseReport.field("claim.intimation.policy.smName","smName");
				classMapForHospitalWiseReport.field("claim.intimation.policy.productName","productName");
				classMapForHospitalWiseReport.field("claim.intimation.insured.insuredName","patientName");
				classMapForHospitalWiseReport.field("claim.intimation.insured.insuredAge","age");
				classMapForHospitalWiseReport.field("claim.intimation.admissionDate","dateOfAdmission");
			    classMapForHospitalWiseReport.field("numberOfDays","durationOfStay");
				//classMapForHospitalWiseReport.field("claimedAmount","claimedAmt");				
				classMapForHospitalWiseReport.field("claim.intimation.insured.insuredGender.value","sex");
				classMapForHospitalWiseReport.field("claim.intimation.insured.relationshipwithInsuredId.value","relationWithInsured");
				classMapForHospitalWiseReport.field("dateOfDischarge","dateOfDischarge");
				classMapForHospitalWiseReport.field("claim.claimType.value","claimType");				
				classMapForHospitalWiseReport.field("financialApprovedAmount","outstandingAmount");	
				classMapForHospitalWiseReport.field("createdDate","rodDate");	
				//IMSSUPPOR-28651
				classMapForHospitalWiseReport.field("docAcknowLedgement.hospitalizationClaimedAmount","claimedAmt");
				//classMapForHospitalWiseReport.field("financialApprovedAmount","claimedAmt");
				
				classMapForHospitalWiseReport.field("rodNumber","rodNO");	
				
				classMapForHospitalWiseReport.register();
				tableMapper = mapperFactory.getMapperFacade();
		

	}
	
	
	    public static List<HospitalWiseReportTableDTO> getHospitalWiseTableObjects(List<Reimbursement> reimbursementList)
	    {
		List<HospitalWiseReportTableDTO> hospitalWiseTableObjectList = tableMapper.mapAsList(reimbursementList, HospitalWiseReportTableDTO.class);
		return hospitalWiseTableObjectList;
	    }
	    
	    
	    public static HospitalWiseReportMapper getInstance(){
	        if(myObj == null){
	            myObj = new HospitalWiseReportMapper();
	            getAllMapValues();
	        }
	        return myObj;
		 }
}
