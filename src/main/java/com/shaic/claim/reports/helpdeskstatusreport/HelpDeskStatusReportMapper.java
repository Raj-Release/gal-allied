package com.shaic.claim.reports.helpdeskstatusreport;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class HelpDeskStatusReportMapper {
	
	static HelpDeskStatusReportMapper myObj;
	
	private static MapperFacade tableMapper;
	private static MapperFacade tableMapper1;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static ClassMapBuilder<Reimbursement, HelpDeskStatusReportTableDTO> classMapForHelpDeskReport = null;
	private static ClassMapBuilder<Claim,HelpDeskStatusReportTableDTO> classMapForHelpDeskReport1 = null;
	
	 public static void getAllMapValues()   {
		 
		classMapForHelpDeskReport = mapperFactory.classMap(Reimbursement.class, HelpDeskStatusReportTableDTO.class);
		classMapForHelpDeskReport1 = mapperFactory.classMap(Claim.class, HelpDeskStatusReportTableDTO.class);
		
		classMapForHelpDeskReport.field("claim.key","claimKey");	
		classMapForHelpDeskReport.field("claim.intimation.intimationId","intimationNo");		
		classMapForHelpDeskReport.field("claim.intimation.createdDate","intimationDate");
		classMapForHelpDeskReport.field("claim.intimation.policyNumber","policyNumber");
		classMapForHelpDeskReport.field("claim.intimation.hospital","hospitalId");
		classMapForHelpDeskReport.field("claim.intimation.policy.homeOfficeCode","issueOfficeCode");
		classMapForHelpDeskReport.field("claim.intimation.policy.productName","productType");
		classMapForHelpDeskReport.field("claim.intimation.insured.insuredName","patientName");
		classMapForHelpDeskReport.field("claim.intimation.callerLandlineNumber","callerContactNo");
		classMapForHelpDeskReport.field("claim.claimType.value","claimType");
		classMapForHelpDeskReport.field("claim.claimType.value","claimType");
	//	classMapForHelpDeskReport.field("stage.stageName","medicalStatus");
	//	classMapForHelpDeskReport.field("status.processValue","medicalStatus1");
		classMapForHelpDeskReport.field("medicalCompletedDate","medicalApprovedDate");
		classMapForHelpDeskReport.field("billingCompletedDate","billinCompletedDate");
		classMapForHelpDeskReport.field("billingApprovedAmount","billingAmount");

	//	classMapForHelpDeskReport.field("status.processValue","billingStage");
	//	classMapForHelpDeskReport.field("status.processValue","financialStage");

		classMapForHelpDeskReport.field("financialCompletedDate","financialCompletedDate");
		//classMapForHelpDeskReport.field("financialApprovalRemarks","financialStage");
		classMapForHelpDeskReport.field("claim.intimation.cpuCode.cpuCode","cpuCode");
		classMapForHelpDeskReport.field("claim.intimation.cpuCode.description","cpuName");
		classMapForHelpDeskReport.field("claim.intimation.cpuCode.description","receivedFrom");
		classMapForHelpDeskReport.field("key","reimbursementKey");	
		classMapForHelpDeskReport.field("createdDate","billReceivedDate");
		classMapForHelpDeskReport.field("stage.key","stageKey");
		classMapForHelpDeskReport.field("status.key","statusKey");
		classMapForHelpDeskReport.field("docAcknowLedgement.hospitalisationFlag","hospitalizationFlag");
		classMapForHelpDeskReport.field("docAcknowLedgement.preHospitalisationFlag","preHospitalizationFlag");
		classMapForHelpDeskReport.field("docAcknowLedgement.postHospitalisationFlag","postHospitalizationFlag");
		classMapForHelpDeskReport.field("docAcknowLedgement.partialHospitalisationFlag","partialHospitalizationFlag");
		classMapForHelpDeskReport.field("docAcknowLedgement.hospitalizationRepeatFlag","hospitalizationRepeatFlag");
		classMapForHelpDeskReport.field("docAcknowLedgement.hospitalCashFlag","addOnBenifits");
		classMapForHelpDeskReport.field("docAcknowLedgement.patientCareFlag","patientCare");
		
		
		classMapForHelpDeskReport1.field("intimation.intimationId", "intimationNo");
		classMapForHelpDeskReport1.field("intimation.createdDate", "intimationDate");
		classMapForHelpDeskReport1.field("intimation.policyNumber", "policyNumber");
		classMapForHelpDeskReport1.field("intimation.hospital", "hospitalId");
		classMapForHelpDeskReport1.field("intimation.insured.insuredName", "patientName");
		classMapForHelpDeskReport1.field("intimation.policy.homeOfficeCode", "issueOfficeCode");
		classMapForHelpDeskReport1.field("intimation.policy.productName", "productType");
		classMapForHelpDeskReport1.field("intimation.callerLandlineNumber", "callerContactNo");
		classMapForHelpDeskReport1.field("claimType.value", "claimType");
		classMapForHelpDeskReport1.field("medicalRemarks", "medicalStatus");
		classMapForHelpDeskReport1.field("intimation.cpuCode.cpuCode", "cpuCode");
		classMapForHelpDeskReport1.field("intimation.cpuCode.description", "cpuName");
		classMapForHelpDeskReport1.field("intimation.cpuCode.description","receivedFrom");
		classMapForHelpDeskReport1.field("createdDate","billReceivedDate");
		classMapForHelpDeskReport1.field("key","key");
		
		classMapForHelpDeskReport.register();
		classMapForHelpDeskReport1.register();
		tableMapper = mapperFactory.getMapperFacade();		
		tableMapper1 = mapperFactory.getMapperFacade();
	}
	
	public static List<HelpDeskStatusReportTableDTO> getHelpDeskTableObjects(List<Reimbursement> reimbursementList)
	{
		List<HelpDeskStatusReportTableDTO> helpDeskStatusTableObjectList = tableMapper.mapAsList(reimbursementList, HelpDeskStatusReportTableDTO.class);
		return helpDeskStatusTableObjectList;
	}
	
	
	public static List<HelpDeskStatusReportTableDTO> getHelpDeskTableObjects1(List<Claim> claimList)
	{
		List<HelpDeskStatusReportTableDTO> helpDeskStatusTableObjectList1 = tableMapper1.mapAsList(claimList, HelpDeskStatusReportTableDTO.class);
		return helpDeskStatusTableObjectList1;
	}
	
	
	public static HelpDeskStatusReportMapper getInstance(){
        if(myObj == null){
            myObj = new HelpDeskStatusReportMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
	
}
