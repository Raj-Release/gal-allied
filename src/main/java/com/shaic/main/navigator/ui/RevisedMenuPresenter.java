package com.shaic.main.navigator.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementMapper;
import com.shaic.claim.RevisedClaimMapper;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search.SearchFlagReconsiderationReqTableDTO;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search.SearchFlagReconsiderationReqView;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.showwizard.ReconsiderationFlagRequestView;
import com.shaic.claim.cashlessprocess.processicac.search.ProcessICACRequestView;
import com.shaic.claim.cashlessprocess.processicac.search.ProcessICACService;
import com.shaic.claim.cashlessprocess.processicac.search.SearchProcessICACTableDTO;
import com.shaic.claim.cashlessprocess.processicac.search.SearchProcessICACView;
import com.shaic.claim.corpbuffer.allocation.search.AllocateCorpBufferService;
import com.shaic.claim.corpbuffer.allocation.search.AllocateCorpBufferTableDTO;
import com.shaic.claim.corpbuffer.allocation.wizard.AllocateCorpBufferDetailDTO;
import com.shaic.claim.corpbuffer.allocation.wizard.AllocateCorpBufferWizard;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionService;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditqueryapproval.CVCAuditQryApprovalPageView;
import com.shaic.claim.cvc.auditqueryapproval.SearchCVCAuditQryApprovalView;
//import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.CVCAuditClsQryPageView;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryFormDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryService;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;
//import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryView;
import com.shaic.claim.cvc.auditqueryreplyprocessing.fa.CVCAuditFaQryPageView;
import com.shaic.claim.cvc.auditqueryreplyprocessing.fa.SearchCVCAuditFaQryView;
import com.shaic.claim.cvc.auditqueryreplyprocessing.reimb.CVCAuditReimbQryPageView;
import com.shaic.claim.cvc.auditqueryreplyprocessing.reimb.SearchCVCAuditReimbQryView;
import com.shaic.claim.icdSublimitMapping.IcdSubLimitMappingView;
import com.shaic.claim.intimation.unlockintimationaudit.SearchUnlockIntimationAuditDBView;
import com.shaic.claim.intimation.uprSearch.SearchIntimationUPRDetailView;
//import com.shaic.claim.preauth.search.autoallocation.SearchPreauthAutoAllocationView;
//import com.shaic.claim.preauth.search.flpautoallocation.SearchFLPAutoAllocationView;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.beans.PCCQuery;
import com.shaic.claim.pcc.divisionHead.ProcessPCCDivisionHeadRequestWizardView;
import com.shaic.claim.pcc.divisionHead.SearchPccDivisionHeadView;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.claim.pcc.hrmCoordinator.ProcessPCCHrmCoordinatorRequestWizardView;
import com.shaic.claim.pcc.hrmCoordinator.SearchPccHrmCoordinatorView;
import com.shaic.claim.pcc.processor.ProcessPCCProcessorRequestWizardView;
import com.shaic.claim.pcc.processor.SearchPccProcessorView;
import com.shaic.claim.pcc.reassignHRM.ProcessPCCReassginHrmCoordinatorRequestWizardView;
import com.shaic.claim.pcc.reassignHRM.SearchPccReassignHrmCoordinatorView;
import com.shaic.claim.pcc.reviewer.ProcessPCCReviewerRequestWizardView;
import com.shaic.claim.pcc.reviewer.SearchPccReviewerView;
import com.shaic.claim.pcc.wizard.ProcessPCCCoOrdinatorRequestWizard;
import com.shaic.claim.pcc.wizard.SearchPccView;
import com.shaic.claim.pcc.zonalCoordinator.ProcessPCCZonalCoordinatorRequestWizardView;
import com.shaic.claim.pcc.zonalCoordinator.SearchPccZonalCoordinatorView;
import com.shaic.claim.pcc.zonalMedicalHead.ProcessPCCZonalMedicalHeadRequestWizardView;
import com.shaic.claim.pcc.zonalMedicalHead.SearchPccZonalMedicalHeadView;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionView;
import com.shaic.claim.processdatacorrection.search.SearchProcessDataCorrectionService;
import com.shaic.claim.processdatacorrection.search.SearchProcessDataCorrectionView;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalView;
import com.shaic.claim.processdatacorrectionhistorical.search.SearchProcessDataCorrectionHistoricalService;
import com.shaic.claim.processdatacorrectionhistorical.search.SearchProcessDataCorrectionHistoricalView;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityView;
import com.shaic.claim.processdatacorrectionpriority.search.SearchProcessDataCorrectionPriorityService;
import com.shaic.claim.processdatacorrectionpriority.search.SearchProcessDataCorrectionPriorityView;
import com.shaic.claim.reimbursement.FinancialApprovalAutoAllocation.SearchProcessFinancialApprovalAutoAllocationView;
import com.shaic.claim.reimbursement.commonBillingFA.SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView;
import com.shaic.claim.reimbursement.commonBillingFA.SearchProcessClaimCommonBillingAndFinancialsView;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.processClaimRequestAutoAllocation.SearchProcessClaimRequestAutoAllocationView;
import com.shaic.claim.reports.branchManagerFeedBack.BranchManagerFeedBackReportView;
import com.shaic.claim.reports.branchManagerFeedbackReportingPattern.BranchManagerFeedBackReportingPatternView;
import com.shaic.claim.reports.dispatchDetailsReport.DispatchDetailsReportView;
import com.shaic.claim.reports.finapprovednotsettledreport.FinApprovedPaymentPendingReportView;
import com.shaic.claim.reports.metabasereport.MetabaseReportView;
import com.shaic.claim.reports.provisionhistory.ProvisionHistoryView;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.settlementpullback.searchsettlementpullback.SearchSettlementPullBackView;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimAuditQuery;
import com.shaic.domain.ClaimService;
import com.shaic.domain.CoorporateBuffer;
import com.shaic.domain.GmcCoorporateBufferLimit;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.PABenefitsCovers;
import com.shaic.domain.Policy;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.billing.processclaimbilling.search.PASearchProcessClaimBillingService;
import com.shaic.reimburement.gatewayAddAdditinalDocument.search.PhysicalDocumentReceivedMakerView;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingAutoAllocationView;
import com.shaic.reimbursement.claims_alert.search.ClaimsAlertMasterView;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenFormDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenView;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestView;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.StopPaymentValidationView;
import com.shaic.reimbursement.rod.createonlinerod.search.SearchCreateOnlineRODView;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
@ViewInterface(MenuView.class)
public class RevisedMenuPresenter extends AbstractMVPPresenter<MenuView> {

	private final Logger log = LoggerFactory.getLogger(RevisedMenuPresenter.class);

	@EJB
	private UsertoCPUMappingService usertoCPUMapService;
	
	@EJB
	private DBCalculationService dbCalService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private SearchCVCAuditActionService cvcActionService;

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PreauthService preauthService;

	@EJB
	private CreateRODService rodService;
	
	@EJB
	private SearchCVCAuditClsQryService cvcClsQryService;
	
	@EJB
	private PASearchProcessClaimBillingService paSearchProcessClaimBillingService;

	@Inject
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	@EJB
	private SearchProcessDataCorrectionService searchProcessDataCorrectionService;
	
	@EJB
	private SearchProcessDataCorrectionHistoricalService correctionHistoricalService;
	
	@EJB
	private ProcessICACService processICACService;

	@EJB
	private SearchProcessPCCRequestService pccRequestService;
	
	@EJB
	private AllocateCorpBufferService corpBufferService;	
	
	@EJB
	private SearchProcessDataCorrectionPriorityService searchProcessDataCorrectionPriorityService;
	
	public static final String CVC_AUDIT_QRY_APPROVAL_WIZARD = "CVC_AUDIT_QRY_APPROVAL_WIZARD";
	
	public static final String CVC_CLS_AUDIT_QRY_RLY_WIZARD = "CVC_CLS_AUDIT_QRY_RLY_WIZARD";
	public static final String CVC_FA_AUDIT_QRY_RLY_WIZARD = "CVC_FA_AUDIT_QRY_RLY_WIZARD";
	public static final String CVC_RMB_AUDIT_QRY_RLY_WIZARD = "CVC_RMB_AUDIT_QRY_RLY_WIZARD";
	
	public static final String RECONSIDERATION_FLAG_REQUEST_WIZARD_VIEW = "RECONSIDERATION_FLAG_REQUEST_WIZARD_VIEW";

	public static final String ICAC_REQUEST_WIZARD_VIEW = "ICAC_REQUEST_WIZARD_VIEW";
	
	public static final String SHOW_ALLOCATE_CORP_BUFFER_WIZARD = "SHOW_ALLOCATE_CORP_BUFFER_WIZARD";
	
	@Override
	public void viewEntered() {
		
	}
	
	protected void showIcdSublimitMapping(
			@Observes @CDIEvent(MenuItemBean.ICD_SUBLIMIT_MAPPING) final ParameterDTO parameters) {
		view.setViewG(IcdSubLimitMappingView.class, true);
		
	}	
	
	/**
	 * Part of CR R1201
	 * @param parameters
	 */
	protected void showFAapprovedNotSettledReport(
			@Observes @CDIEvent(MenuItemBean.FA_APPROVED_SETTLEMENT_PENDING_REPORT) final ParameterDTO parameters) {
		
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);	
		BeanItemContainer<SelectValue> cpuCodeContainer = usertoCPUMapService.getRevisedUserCpuContainer(userId);
		
	    view.setFinApprovedNotSettledReportView(FinApprovedPaymentPendingReportView.class, cpuCodeContainer);
	}
	
	protected void showBMFeedbackReport(
			@Observes @CDIEvent(MenuItemBean.BRANCH_MANAGER_FEEDBACK_REPORT) final ParameterDTO parameters) {
		
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);	
		BeanItemContainer<SelectValue> fbTypeContainer = masterService.getMasterValueByCode(ReferenceTable.FEEDBACK);
		BeanItemContainer<SelectValue> zoneContainer = dbCalService.getZoneCode();
		BeanItemContainer<SelectValue> periodContainer = masterService.getMasterValueByCode(ReferenceTable.BM_FEEDBACK_PERIOD);
		/*BeanItemContainer<SelectValue> branchContainer = masterService.getSelectValueContainerForBranch();*/
		
	    view.setBranchManagerFeedbackReportView(BranchManagerFeedBackReportView.class, fbTypeContainer, zoneContainer, periodContainer/*, branchContainer*/);
	}

	protected void showBMFeedbackReportingPattern(
			@Observes @CDIEvent(MenuItemBean.BRANCH_MANAGER_FEEDBACK_REPORTING_PATTERN) final ParameterDTO parameters) {
		
		BeanItemContainer<SelectValue> fbTypeContainer = masterService.getMasterValueByCode(ReferenceTable.FEEDBACK);
		fbTypeContainer.sort(new Object[] {"value"}, new boolean[] {false});
		
		BeanItemContainer<SelectValue> fbTypeSelectContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue claimRetailSelect = null;
		for (int i = 0; i < fbTypeContainer.size(); i++) {
			
			if(fbTypeContainer.getIdByIndex(i).getValue().equalsIgnoreCase("All") || fbTypeContainer.getIdByIndex(i).getValue().contains("GMC")){
				if(fbTypeContainer.getIdByIndex(i).getValue().contains("GMC")){
					claimRetailSelect = fbTypeContainer.getIdByIndex(i);  
				}
				continue;
			}
			else 
				fbTypeSelectContainer.addItem(fbTypeContainer.getIdByIndex(i));
		}
		fbTypeSelectContainer.addItem(claimRetailSelect);
		
		
		BeanItemContainer<SelectValue> zoneContainer = dbCalService.getZoneCode();
		BeanItemContainer<SelectValue> periodContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue periodSelect = null;
//		for(int i=1; i < 13; i++)        // Changed as per Mr. Satish Sir's decision to show current Financial year months
//		{
//			periodSelect = new SelectValue(Long.valueOf(i), getMonthName(i) + " - " + ((Calendar.getInstance()).getTime().getYear()+1900));
//			periodContainer.addBean(periodSelect);
//		}
		
		 int finYear = 0;
         
         if((Calendar.getInstance()).getTime().getMonth()<3)
         {
                 finYear = (Calendar.getInstance()).getTime().getYear()+1900-1;
         }
         else
         {
                 finYear = (Calendar.getInstance()).getTime().getYear()+1900;
         }
		
		for(int i=4; i < 13; i++)
		{
			periodSelect = new SelectValue(Long.valueOf(i), getMonthName(i) + " - " + finYear);
			periodContainer.addBean(periodSelect);
		}
		for(int i=1; i < 4; i++)
		{
			periodSelect = new SelectValue(Long.valueOf(i), getMonthName(i) + " - " + (finYear+1));
			periodContainer.addBean(periodSelect);
		}
	    view.setBranchManagerFeedbackReportingPatternView(BranchManagerFeedBackReportingPatternView.class, fbTypeSelectContainer, zoneContainer, periodContainer);
	}
	
	public String getMonthName(int i){
		StringBuffer monthName = new StringBuffer("");
		switch(i){
		case 1:
			monthName.append("January");
			break;
		case 2:
			monthName.append("February");
			break;
		case 3:
			monthName.append("March");
			break;
		case 4:
			monthName.append("April");
			break;
		case 5:
			monthName.append("May");
			break;
		case 6:
			monthName.append("June");
			break;
		case 7:
			monthName.append("July");
			break;
		case 8:
			monthName.append("August");
			break;
		case 9:
			monthName.append("September");
			break;
		case 10:
			monthName.append("October");
			break;
		case 11:
			monthName.append("November");
			break;
		case 12:
			monthName.append("December");
			break;
		}
		return monthName.toString();
	}
	
	protected void showCVCAuditQryApprovalWizard(
				@Observes @CDIEvent(CVC_AUDIT_QRY_APPROVAL_WIZARD) final ParameterDTO parameters) {
			SearchCVCAuditActionTableDTO tableDto = (SearchCVCAuditActionTableDTO) parameters
					.getPrimaryParameter();
			
			ImsUser imsUser = null;
			String[] userRoles = null;
			if(VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT) != null) {
				imsUser = (ImsUser) VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT);
				userRoles = imsUser.getUserRoleList();
			}
			
			if(Arrays.asList(userRoles).contains(MenuPresenter.USER_ROLE_CVC_AUDIT)) { //CLM_CVC_AUDIT_NORMAL_USER
				tableDto.setClmAuditHeadUser(false);
			}

			if(Arrays.asList(userRoles).contains(MenuPresenter.USER_ROLE_CVC_AUDIT_CLUSTER_HEAD) ) {  //CLM_CVC_AUDIT_CLUSTER_HEAD_USER
				tableDto.setClmAuditHeadUser(true);
			}
			
			cvcActionService.updateCVCLockKey(tableDto);
			
			List<ClaimAuditQuery> auditQrylist = cvcActionService.getQrylistByAuditKey(tableDto.getAuditKey());
			List<SearchCVCAuditClsQryTableDTO> clsQryList = new ArrayList<SearchCVCAuditClsQryTableDTO>();
			List<SearchCVCAuditClsQryTableDTO> medicalQryList = new ArrayList<SearchCVCAuditClsQryTableDTO>();
			List<SearchCVCAuditClsQryTableDTO> billingFaQryList = new ArrayList<SearchCVCAuditClsQryTableDTO>();
			
			if(null != auditQrylist && !auditQrylist.isEmpty() ){
				SearchCVCAuditClsQryTableDTO auditQryDto = null;
				TmpEmployee employeeName = null;
				
				for (ClaimAuditQuery claimAuditQuery : auditQrylist) {
					if(claimAuditQuery.getReplyby() != null){
					employeeName = masterService.getEmployeeName(claimAuditQuery.getReplyby().toLowerCase());
					}
					if(claimAuditQuery.getTeamName().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_CASHLESS)){
						auditQryDto = new SearchCVCAuditClsQryTableDTO(claimAuditQuery);
						auditQryDto.setsNo(auditQrylist.indexOf(claimAuditQuery)+1);
						auditQryDto.setClmAuditHeadUser(tableDto.isClmAuditHeadUser());
						if(employeeName != null && claimAuditQuery.getReplyby() != null){
							auditQryDto.setClsQryReplyBy(claimAuditQuery.getReplyby() + (employeeName.getEmpFirstName() != null ? ("-"+employeeName.getEmpFirstName()) : ""));							
						}
						clsQryList.add(auditQryDto);
					}
					else if(claimAuditQuery.getTeamName().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_MEDICAL)){
						auditQryDto = new SearchCVCAuditClsQryTableDTO(claimAuditQuery);
						auditQryDto.setClmAuditHeadUser(tableDto.isClmAuditHeadUser());
						if(employeeName != null  && claimAuditQuery.getReplyby() != null){
							auditQryDto.setMedicalAuditQryReplyBy(claimAuditQuery.getReplyby() + (employeeName.getEmpFirstName() != null ? ("-"+employeeName.getEmpFirstName()) : ""));							
						}
						medicalQryList.add(auditQryDto);
					}
					else if(claimAuditQuery.getTeamName().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_BILLING)
							|| claimAuditQuery.getTeamName().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_FINANCIAL)){
						auditQryDto = new SearchCVCAuditClsQryTableDTO(claimAuditQuery);
						auditQryDto.setClmAuditHeadUser(tableDto.isClmAuditHeadUser());
						if(employeeName != null  && claimAuditQuery.getReplyby() != null){
							auditQryDto.setBillinFaAuditQryReplyBy(claimAuditQuery.getReplyby() + (employeeName.getEmpFirstName() != null ? ("-"+employeeName.getEmpFirstName()) : ""));							
						}
						billingFaQryList.add(auditQryDto);
					}
				}
				
			}
			tableDto.setClsQryList(clsQryList);
			tableDto.setMedicalQryList(medicalQryList);
			tableDto.setBillingFaQryList(billingFaQryList);
			
			NewIntimationDto newIntimationDto = new NewIntimationDto();
			
			ClaimDto claimDTO = null;
			if (tableDto != null) {
				
				Claim claimByKey = claimService.getClaimByKey(tableDto.getClaimKey());
				newIntimationDto = intimationService.getIntimationDto(claimByKey.getIntimation());
				claimDTO = ClaimMapper.getInstance().getClaimDto(claimByKey);
				Preauth latestPreauthByClaim = null;
				if (claimDTO.getClaimType() != null
						&& claimDTO.getClaimType().getId()
								.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					latestPreauthByClaim = preauthService
							.getLatestPreauthByClaim(claimDTO.getKey());
					if(null != latestPreauthByClaim){
						claimDTO.setCashlessAppAmt(latestPreauthByClaim
								.getTotalApprovalAmount());
					}
				}
				claimDTO.setNewIntimationDto(newIntimationDto);
					
			if (claimByKey != null) {
				newIntimationDto = intimationService.getIntimationDto(claimByKey
						.getIntimation());
				claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
				
				claimDTO.setNewIntimationDto(newIntimationDto);
				
			}
			
			RRCDTO rrcDTO = new RRCDTO();
			rrcDTO.setClaimDto(claimDTO);
			rrcDTO.setNewIntimationDTO(newIntimationDto);
			rrcDTO.setStrUserName(tableDto.getUsername());
			Double insuredSumInsured = dbCalService.getInsuredSumInsured(
					rrcDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), rrcDTO.getNewIntimationDTO().getPolicy().getKey(),rrcDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			
			tableDto.setRrcDto(rrcDTO);
			
			if(latestPreauthByClaim != null){
				claimDTO.setCashlessAppAmt(latestPreauthByClaim
						.getTotalApprovalAmount());
			}
			
			// R1045
			tableDto.setPreauthDto(new PreauthDTO());
			tableDto.getPreauthDto().setNewIntimationDTO(newIntimationDto);
			tableDto.getPreauthDto().setClaimDTO(claimDTO);
			
			String diagnosisForPreauthByKey = "";
			if(tableDto.getTransactionKey() != null ) {
				diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(tableDto.getTransactionKey());
				
			}
			tableDto.getPreauthDto().getNewIntimationDTO().setDiagnosis(diagnosisForPreauthByKey);
			
			
			//R1152
	    	if(newIntimationDto.getHospitalDto() != null && newIntimationDto.getHospitalDto().getCpuId() != null && newIntimationDto.getPolicy() != null && newIntimationDto.getPolicy().getHomeOfficeCode() != null){
				TmpCPUCode cpu = rodService.getCpuDetails(newIntimationDto.getHospitalDto().getCpuId());
				OrganaizationUnit branch = rodService.getBranchCode(newIntimationDto.getPolicy().getHomeOfficeCode());
				if(cpu != null && branch != null && branch.getCpuCode() != null){
					if(!branch.getCpuCode().equalsIgnoreCase(cpu.getCpuCode().toString())){
					}
				}
			}
	    	
	    	if (null != claimByKey && claimByKey.getLobId() != null && claimByKey.getLobId().equals(ReferenceTable.PA_LOB_KEY)) {
	    		
	    		if (null != tableDto.getClaimType() && tableDto.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS_STRING)) {
		    		Preauth preauthByClaim = preauthService.getPreauthById(tableDto.getTransactionKey());
		    		if (null != preauthByClaim) {
		    			tableDto.setClaimedAmount(preauthByClaim.getClaimedAmt().toString());
		    		}
		    	}
		    	if (null != tableDto.getClaimType() && tableDto.getClaimType().equalsIgnoreCase(SHAConstants.REIMBURSEMENT_STRING)) {
		    		
		    		PABenefitsCovers paBenefitsListByRodKey = paSearchProcessClaimBillingService.getPABenefitsBillAmountByRodKey(tableDto.getTransactionKey());
			    	if (null != paBenefitsListByRodKey) {
			    		tableDto.setClaimedAmount(paBenefitsListByRodKey.getBillAmount().toString());
			    	}
			    	
			    	List<RODBillDetails> billEntryDetails = ackDocReceivedService.getBillEntryDetails(tableDto.getTransactionKey());
					if(billEntryDetails != null && !billEntryDetails.isEmpty()){
						Double billAmount = 0d;
						for (RODBillDetails rodBillDetails : billEntryDetails) {
							billAmount += rodBillDetails.getClaimedAmountBills() != null ?  rodBillDetails.getClaimedAmountBills() : 0d;
						}
						tableDto.setClaimedAmount(billAmount.toString());
					}
		    		
		    	}
	    	} else {
	
		    	if (null != tableDto.getClaimType() && tableDto.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS_STRING)) {
		    		Preauth preauthByClaim = preauthService.getPreauthById(tableDto.getTransactionKey());
		    		if (null != preauthByClaim) {
		    			tableDto.setClaimedAmount(preauthByClaim.getClaimedAmt().toString());
		    		}
		    	}
		    	if (null != tableDto.getClaimType() && tableDto.getClaimType().equalsIgnoreCase(SHAConstants.REIMBURSEMENT_STRING)) {
			    	List<RODBillDetails> billEntryDetails = ackDocReceivedService.getBillEntryDetails(tableDto.getTransactionKey());
					if(billEntryDetails != null && !billEntryDetails.isEmpty()){
						Double billAmount = 0d;
						for (RODBillDetails rodBillDetails : billEntryDetails) {
							billAmount += rodBillDetails.getClaimedAmountBills() != null ?  rodBillDetails.getClaimedAmountBills() : 0d;
						}
						tableDto.setClaimedAmount(billAmount.toString());
					}
		    		
		    	}
	    	}
				
			view.setCVCAuditQryApprovalWizardView(CVCAuditQryApprovalPageView.class,
					true, tableDto);
		}
	}
	
	protected void showViewCVCAuditActionSearch(
			@Observes @CDIEvent(MenuItemBean.CVC_AUDIT_QRY_APPRVOAL) final ParameterDTO parameters){
		view.setViewCVCAuditQryApprovalSearch(SearchCVCAuditQryApprovalView.class, true);
	}	

	protected void showProcessClaimCommonBillingAndFinancials(
			@Observes @CDIEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS) final ParameterDTO parameters) {
		

		BeanItemContainer<SelectValue> claimType = masterService
				.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
		BeanItemContainer<SelectValue> productName = masterService.getSelectValueContainerForProduct();
		BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();
		BeanItemContainer<SelectValue> type = masterService.getStatusByStage(ReferenceTable.BILLING_STAGE);
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
		
//		BeanItemContainer<SelectValue> statusByStage = masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);
		
		Stage stageByKey = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		
		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.BILLING_STAGE);
		
		SelectValue selectValue = new SelectValue();
		selectValue.setId(stageByKey.getKey());
		selectValue.setValue(stageByKey.getStageName());
		
		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue);
		statusByStage.addBean(selectValue1);
		
		
		view.setViewForSearchClaimCommonBillingAndFinancial(SearchProcessClaimCommonBillingAndFinancialsView.class, true,claimType,productName,cpuCode,type,selectValueForPriority,statusByStage);
	}
//	Commented the below Cashless Screen	
//	protected void showViewCVCAuditClsQryRlySearch(
//			@Observes @CDIEvent(MenuItemBean.CVC_AUDIT_CLS_QRY_RLY) final ParameterDTO parameters){
//		
//		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
//		SearchCVCAuditClsQryFormDTO searchFrmDto = masterService.getAuditReplyUserRoleByUserIdForRplScreen(userId, SHAConstants.AUDIT_CLS_QRY_REPLY_SCREEN);		
//		
//		view.setViewCVCAuditClsQryRlySearch(SearchCVCAuditClsQryView.class, true, searchFrmDto);
//	}	
	protected void showViewCVCAuditRmbQryRlySearch(
			@Observes @CDIEvent(MenuItemBean.CVC_AUDIT_RMB_QRY_RLY) final ParameterDTO parameters){
		
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		SearchCVCAuditClsQryFormDTO searchFrmDto = masterService.getAuditReplyUserRoleByUserIdForRplScreen(userId, SHAConstants.AUDIT_MEDICAL_QRY_REPLY_SCREEN);
		
		view.setViewCVCAuditRmbQryRlySearch(SearchCVCAuditReimbQryView.class, true, searchFrmDto);
	}	
	protected void showViewCVCAuditFaQryRlySearch(
			@Observes @CDIEvent(MenuItemBean.CVC_AUDIT_FA_QRY_RLY) final ParameterDTO parameters){
		
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		SearchCVCAuditClsQryFormDTO searchFrmDto = masterService.getAuditReplyUserRoleByUserIdForRplScreen(userId, SHAConstants.AUDIT_BILLING_FA_QRY_REPLY_SCREEN);
		
		view.setViewCVCAuditFaQryRlySearch(SearchCVCAuditFaQryView.class, true, searchFrmDto);
	}
	
//	protected void showCVCAuditClsQryReplyWizard(
//			@Observes @CDIEvent(CVC_CLS_AUDIT_QRY_RLY_WIZARD) final ParameterDTO parameters) {
//		SearchCVCAuditActionTableDTO tableDto = (SearchCVCAuditActionTableDTO) parameters
//				.getPrimaryParameter();
//		
//		if(tableDto.getClaimType() != null && tableDto.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS)){
//			
//			Preauth preAuth = preauthService.getPreauthById(tableDto.getTransactionKey());
//			PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
//			PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preAuth);
//			ClaimMapper clmMapper = ClaimMapper.getInstance();
//			preauthDTO.setClaimDTO(clmMapper.getClaimDto(preAuth.getClaim()));
//			preauthDTO.setNewIntimationDTO(intimationService.getIntimationDto(preAuth.getClaim().getIntimation()));	
//			tableDto.setPreauthDto(preauthDTO);
//		}
//		
//
//		List<SearchCVCAuditClsQryTableDTO> tableQryDtoList = cvcClsQryService.getCVCAuditQryReplyPendingTeamWise(tableDto.getAuditKey(),SHAConstants.AUDIT_TEAM_CASHLESS);
//		tableDto.setClsQryList(tableQryDtoList);
//			
//		view.setViewCVCAuditClsQrySearch(CVCAuditClsQryPageView.class,
//				true, tableDto);
//	}
	
	protected void showCVCAuditFaQryReplyWizard(
			@Observes @CDIEvent(CVC_FA_AUDIT_QRY_RLY_WIZARD) final ParameterDTO parameters) {
		SearchCVCAuditActionTableDTO tableDto = (SearchCVCAuditActionTableDTO) parameters
				.getPrimaryParameter();
		if(tableDto.getClaimType() != null && tableDto.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS)){
			
			Preauth preAuth = preauthService.getPreauthById(tableDto.getTransactionKey());
			PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
			PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preAuth);
			ClaimMapper clmMapper = ClaimMapper.getInstance();
			preauthDTO.setClaimDTO(clmMapper.getClaimDto(preAuth.getClaim()));
			preauthDTO.setNewIntimationDTO(intimationService.getIntimationDto(preAuth.getClaim().getIntimation()));	
			tableDto.setPreauthDto(preauthDTO);
		}
		else if(tableDto.getClaimType() != null && tableDto.getClaimType().equalsIgnoreCase(SHAConstants.REIMBURSEMENT)){
			
			Reimbursement reimbObj = preauthService.getReimbursementObjectByKey(tableDto.getTransactionKey());
			ReimbursementMapper reimbMapper= new ReimbursementMapper();
			ReimbursementDto reimbDTO = reimbMapper.getReimbursementDto(reimbObj);
			PreauthDTO preauthDTO =  new PreauthDTO();
			ClaimMapper clmMapper = ClaimMapper.getInstance();
			preauthDTO.setClaimDTO(clmMapper.getClaimDto(reimbObj.getClaim()));
			preauthDTO.setNewIntimationDTO(intimationService.getIntimationDto(reimbObj.getClaim().getIntimation()));
			tableDto.setPreauthDto(preauthDTO);
		}
		List<SearchCVCAuditClsQryTableDTO> tableQryDtoList1 = cvcClsQryService.getCVCAuditQryReplyPendingTeamWise(tableDto.getAuditKey(),SHAConstants.AUDIT_TEAM_BILLING);
		tableDto.setBillingFaQryList(tableQryDtoList1);	
		List<SearchCVCAuditClsQryTableDTO> tableQryDtoList2 = cvcClsQryService.getCVCAuditQryReplyPendingTeamWise(tableDto.getAuditKey(),SHAConstants.AUDIT_TEAM_FINANCIAL);
		tableDto.getBillingFaQryList().addAll(tableQryDtoList2);
		
		view.setViewCVCAuditFaQrySearch(CVCAuditFaQryPageView.class,
				true, tableDto);
	}
	protected void showCVCAuditRmbQryReplyWizard(
			@Observes @CDIEvent(CVC_RMB_AUDIT_QRY_RLY_WIZARD) final ParameterDTO parameters) {
		SearchCVCAuditActionTableDTO tableDto = (SearchCVCAuditActionTableDTO) parameters
				.getPrimaryParameter();
		
		if(tableDto.getClaimType() != null && tableDto.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS)){
		
			Preauth preAuth = preauthService.getPreauthById(tableDto.getTransactionKey());
			PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
			PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preAuth);
			ClaimMapper clmMapper = ClaimMapper.getInstance();
			preauthDTO.setClaimDTO(clmMapper.getClaimDto(preAuth.getClaim()));
			preauthDTO.setNewIntimationDTO(intimationService.getIntimationDto(preAuth.getClaim().getIntimation()));	
			tableDto.setPreauthDto(preauthDTO);
		}
		else if(tableDto.getClaimType() != null && tableDto.getClaimType().equalsIgnoreCase(SHAConstants.REIMBURSEMENT)){
			
			Reimbursement reimbObj = preauthService.getReimbursementObjectByKey(tableDto.getTransactionKey());
			ReimbursementMapper reimbMapper= new ReimbursementMapper();
			ReimbursementDto reimbDTO = reimbMapper.getReimbursementDto(reimbObj);
			PreauthDTO preauthDTO =  new PreauthDTO();
			ClaimMapper clmMapper = ClaimMapper.getInstance();
			preauthDTO.setClaimDTO(clmMapper.getClaimDto(reimbObj.getClaim()));
			preauthDTO.setNewIntimationDTO(intimationService.getIntimationDto(reimbObj.getClaim().getIntimation()));
			tableDto.setPreauthDto(preauthDTO);
		}
		
		
		List<SearchCVCAuditClsQryTableDTO> tableQryDtoList = cvcClsQryService.getCVCAuditQryReplyPendingTeamWise(tableDto.getAuditKey(),SHAConstants.AUDIT_TEAM_MEDICAL);
		tableDto.setMedicalQryList(tableQryDtoList);

			
		view.setViewCVCAuditRmbQrySearch(CVCAuditReimbQryPageView.class,
				true, tableDto);
	}

	protected void showClaimsAlertSearch(
			@Observes @CDIEvent(MenuItemBean.CLAIMS_ALERT_MASTER_SCREEN) final ParameterDTO parameters) {
		view.setViewClaimsAlert(ClaimsAlertMasterView.class);
	}
// Commented the below Cashless Screen	
//	protected void showPreauthAutoAllocationView(
//			@Observes @CDIEvent(MenuItemBean.PROCESS_FLP_AUTO_ALLOCATION) final ParameterDTO parameters) {
//		
//		view.setViewFLPAutoAllocation(SearchFLPAutoAllocationView.class, true);
//		
//	}
	
	protected void showMonitoringReportView(
			@Observes @CDIEvent(MenuItemBean.MONITORING_REPORT) final ParameterDTO parameters) {	
		view.setViewMonitoringReport(MetabaseReportView.class,true);	
	}
	
	protected void showServiceDataCorrection(
			@Observes @CDIEvent(MenuItemBean.DATA_CODING_DATA_CORRECTION) final ParameterDTO parameters) {
		view.setViewG(SearchProcessDataCorrectionView.class,true);
	}
	
	protected void setViewProcessDataCorrection(
			@Observes @CDIEvent(MenuItemBean.PROCESS_DATA_CORRECTION_VIEW) final ParameterDTO parameters) {
		
		ProcessDataCorrectionDTO tableDto = (ProcessDataCorrectionDTO) parameters.getPrimaryParameter();
		tableDto = searchProcessDataCorrectionService.getCorrectionDatas(tableDto);
		view.setViewProcessDataCorrection(DataCorrectionView.class,true,tableDto);
	}
	
	protected void showSearchReconisderationFlagRequestView(
			@Observes @CDIEvent(MenuItemBean.FLAG_RECONSIDERATION_REQUEST) final ParameterDTO parameters) {

		view.setViewG(SearchFlagReconsiderationReqView.class, true);
	}
	
	protected void showServiceDataCorrectionHistorical(
			@Observes @CDIEvent(MenuItemBean.DATA_CODING_DATA_CORRECTION_HISTORICAL) final ParameterDTO parameters) {
		view.setViewG(SearchProcessDataCorrectionHistoricalView.class,true);
	}
	
	protected void setViewProcessDataCorrectionHistorical(
			@Observes @CDIEvent(MenuItemBean.PROCESS_DATA_CORRECTION_HISTORICAL_VIEW) final ParameterDTO parameters) {
		
		ProcessDataCorrectionDTO tableDto = (ProcessDataCorrectionDTO) parameters.getPrimaryParameter();
		tableDto = correctionHistoricalService.getCorrectionDatas(tableDto);
		view.setViewProcessDataCorrectionHistorical(DataCorrectionHistoricalView.class,true,tableDto);
	}
	
	protected void showReconisderationFlagRequestWizardView(
			@Observes @CDIEvent(RECONSIDERATION_FLAG_REQUEST_WIZARD_VIEW) final ParameterDTO parameters) {
		SearchFlagReconsiderationReqTableDTO tableDTO = (SearchFlagReconsiderationReqTableDTO) parameters.getPrimaryParameter();
		
		if(tableDTO.getIntimationNumber() != null){
			Intimation intimation=intimationService.getIntimationByNo(tableDTO.getIntimationNumber());
			NewIntimationDto newIntimationDto=intimationService.getIntimationDto(intimation);
			tableDTO.setNewIntimationDTO(newIntimationDto);
			
		}
		view.setReconsiderationFlagReqWizardView(ReconsiderationFlagRequestView.class, tableDTO);
	}
	
	protected void showIntimationAndViewDetails(
			@Observes @CDIEvent(MenuItemBean.INTIMATION_VIEW_UPR_DETAILS) final ParameterDTO parameters) {
		view.setViewG(SearchIntimationUPRDetailView.class, true);
	}

	protected void showSearchIcacProcessView(
			@Observes @CDIEvent(MenuItemBean.PROCESS_ICAC_REQUEST) final ParameterDTO parameters) {

		view.setViewG(SearchProcessICACView.class, true);
	}
	
	protected void setViewProcessICACRequestWizard(
			@Observes @CDIEvent(ICAC_REQUEST_WIZARD_VIEW) final ParameterDTO parameters) {
		
		SearchProcessICACTableDTO tableDto = (SearchProcessICACTableDTO) parameters.getPrimaryParameter();
		
		if(tableDto.getClaimKey() !=null){
			RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
			Claim claim = preauthService.getClaimByKey(tableDto.getClaimKey());
			ClaimDto claimDto = claimMapper.getClaimDto(claim);
			if(claim !=null && claim.getIntimation() !=null
					&& claimDto !=null){
				claimDto.setNewIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
			}
			if(claimDto.getNewIntimationDto() != null &&claimDto.getNewIntimationDto().getPolicy() != null &&
					claimDto.getNewIntimationDto().getPolicy().getKey() != null){
				Long claimCount = preauthService.getClaimCount(claimDto.getNewIntimationDto().getPolicy().getKey());
				if(claimCount != null){
					tableDto.setClaimCount(claimCount);
				}
			}
			if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
				Preauth preauth = preauthService.getPreauthClaimKey(tableDto.getClaimKey());
				PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
				if(preauth != null && preauth.getKey() != null){
					PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
					preauthDTO.setNewIntimationDTO(intimationService.getIntimationDto(claim.getIntimation()));
					tableDto.setPreauthDto(preauthDTO);
				}else{
					PreauthDTO preauthDTO =new PreauthDTO();
					preauthDTO.setNewIntimationDTO(intimationService.getIntimationDto(claim.getIntimation()));
					tableDto.setPreauthDto(preauthDTO);
				}
			}else if(claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
				PreauthDTO preauthDTO =new PreauthDTO();
				preauthDTO.setNewIntimationDTO(intimationService.getIntimationDto(claim.getIntimation()));
				tableDto.setPreauthDto(preauthDTO);
			}
			
			tableDto.setClaimDto(claimDto);	
		}
		
		tableDto = processICACService.getSetRequestData(tableDto);
		
		view.setViewProcessICACRequest(ProcessICACRequestView.class, true, tableDto);
	}
	
	protected void showUnlockIntimationAudit(
			@Observes @CDIEvent(MenuItemBean.AUDIT_INTIMATION_UNLOCK) final ParameterDTO parameters) {
		view.setViewG(SearchUnlockIntimationAuditDBView.class, true);
	}
	
	protected void showProcessPCCCoOrdinatorRequestWizard(
			@Observes @CDIEvent(MenuPresenter.SHOW_PROCESS_PCC_CO_ORDINATOR_REQUEST_WIZARD) final ParameterDTO parameters) {

		SearchProcessPCCRequestTableDTO tableDto = (SearchProcessPCCRequestTableDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		userName = userName.toUpperCase();
		PccDetailsTableDTO doctorDeatils = new PccDetailsTableDTO();
		if(tableDto.getKey() !=null){
			doctorDeatils = pccRequestService.getPccDetailsFromPCCRequest(pccRequestService.getPCCRequestByKey(tableDto.getKey()));
		}
		RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
		Claim claim = claimService.getClaimsByIntimationNumber(tableDto.getIntimationNo());
		ClaimDto claimDto = claimMapper.getClaimDto(claim);
		if(claim !=null && claim.getIntimation() !=null
				&& claimDto !=null){
			doctorDeatils.setIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
		}	
		doctorDeatils.setClaimDto(claimDto);
		List<Long> querystatusList = ReferenceTable.getPCCQueryStatusKeysByRole(SHAConstants.PCC_COORDINATOR_ROLE);
		List<PCCQuery> pccQueries = pccRequestService.getPCCQueryByRole(tableDto.getKey(),SHAConstants.PCC_COORDINATOR_ROLE,userName,querystatusList);
		pccRequestService.setPCCQueryAndReplyDetails(doctorDeatils, pccQueries,null);

		view.setProcessPCCCoOrdinatorRequestWizard(ProcessPCCCoOrdinatorRequestWizard.class, doctorDeatils);
	}
	
	protected void showProcessPCCProcessorRequestWizard(
			@Observes @CDIEvent(MenuPresenter.SHOW_PROCESS_PCC_PROCESSOR_REQUEST_WIZARD) final ParameterDTO parameters) {
		
		SearchProcessPCCRequestTableDTO tableDto = (SearchProcessPCCRequestTableDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		userName = userName.toUpperCase();
		PccDetailsTableDTO doctorDeatils = new PccDetailsTableDTO();
		if(tableDto.getKey() !=null){
			doctorDeatils = pccRequestService.getPccDetailsFromPCCRequest(pccRequestService.getPCCRequestByKey(tableDto.getKey()));
		}
		RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
		Claim claim = claimService.getClaimsByIntimationNumber(tableDto.getIntimationNo());
		ClaimDto claimDto = claimMapper.getClaimDto(claim);
		if(claim !=null && claim.getIntimation() !=null
				&& claimDto !=null){
			doctorDeatils.setIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
		}	
		doctorDeatils.setClaimDto(claimDto);
		List<Long> querystatusList = ReferenceTable.getPCCQueryStatusKeysByRole(SHAConstants.PCC_PROCESSOR_ROLE);
		List<PCCQuery> pccQueries = pccRequestService.getPCCQueryByRole(tableDto.getKey(),SHAConstants.PCC_PROCESSOR_ROLE,userName,querystatusList);
		pccRequestService.setPCCQueryAndReplyDetails(doctorDeatils, pccQueries,SHAConstants.PCC_PROCESSOR_QUERY_INITIATED_STATUS);
		
		view.setProcessPCCProcessorRequestWizardView(ProcessPCCProcessorRequestWizardView.class, doctorDeatils);
	
	}
	

	protected void showProcessPCCReviewerRequestWizard(
			@Observes @CDIEvent(MenuPresenter.SHOW_PROCESS_PCC_REVIEWER_REQUEST_WIZARD) final ParameterDTO parameters) {
		
		SearchProcessPCCRequestTableDTO tableDto = (SearchProcessPCCRequestTableDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		userName = userName.toUpperCase();
		PccDetailsTableDTO doctorDeatils = new PccDetailsTableDTO();
		if(tableDto.getKey() !=null){
			doctorDeatils = pccRequestService.getPccDetailsFromPCCRequest(pccRequestService.getPCCRequestByKey(tableDto.getKey()));
		}
		RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
		Claim claim = claimService.getClaimsByIntimationNumber(tableDto.getIntimationNo());
		ClaimDto claimDto = claimMapper.getClaimDto(claim);
		if(claim !=null && claim.getIntimation() !=null
				&& claimDto !=null){
			doctorDeatils.setIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
		}	
		doctorDeatils.setClaimDto(claimDto);
		List<Long> querystatusList = ReferenceTable.getPCCQueryStatusKeysByRole(SHAConstants.PCC_REVIEWER_ROLE);
		List<PCCQuery> pccQueries = pccRequestService.getPCCQueryByRole(tableDto.getKey(),SHAConstants.PCC_REVIEWER_ROLE,userName,querystatusList);
		PCCQuery directQuery = pccRequestService.getDirectPCCQuery(tableDto.getKey(), SHAConstants.PCC_PROCESSOR_QUERY_INITIATED_STATUS, SHAConstants.PCC_COORDINATOR_SOURCE);
		pccRequestService.setPCCQueryAndReplyDetails(doctorDeatils, pccQueries,SHAConstants.PCC_REVIEWER_QUERY_INITIATED_STATUS);
		if(directQuery !=null){
			pccRequestService.setDirectrespoceDetails(directQuery,doctorDeatils);
		}
		view.setProcessPCCReviewerRequestWizardView(ProcessPCCReviewerRequestWizardView.class, doctorDeatils);
	
	}
	
	protected void showProcessPCCDivisionHeadRequestWizard(
			@Observes @CDIEvent(MenuPresenter.SHOW_PROCESS_PCC_DIVISION_HEAD_REQUEST_WIZARD) final ParameterDTO parameters) {
		
		SearchProcessPCCRequestTableDTO tableDto = (SearchProcessPCCRequestTableDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		userName = userName.toUpperCase();
		PccDetailsTableDTO doctorDeatils = new PccDetailsTableDTO();
		if(tableDto.getKey() !=null){
			doctorDeatils = pccRequestService.getPccDetailsFromPCCRequest(pccRequestService.getPCCRequestByKey(tableDto.getKey()));
		}
		RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
		Claim claim = claimService.getClaimsByIntimationNumber(tableDto.getIntimationNo());
		ClaimDto claimDto = claimMapper.getClaimDto(claim);
		if(claim !=null && claim.getIntimation() !=null
				&& claimDto !=null){
			doctorDeatils.setIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
		}	
		doctorDeatils.setClaimDto(claimDto);
		List<Long> querystatusList = ReferenceTable.getPCCQueryStatusKeysByRole(SHAConstants.DIVISION_HEAD_ROLE);
		List<PCCQuery> pccQueries = pccRequestService.getPCCQueryByRole(tableDto.getKey(),SHAConstants.DIVISION_HEAD_ROLE,userName,querystatusList);
		pccRequestService.setPCCQueryAndReplyDetails(doctorDeatils, pccQueries,SHAConstants.PCC_DIVISIONHEAD_QUERY_INITIATED_STATUS);
		
		view.setProcessPCCDivisionHeadRequestWizardView(ProcessPCCDivisionHeadRequestWizardView.class, doctorDeatils);
	
	}
	
	protected void showsetProcessPCCZonalMedicalHeadRequestWizard(
			@Observes @CDIEvent(MenuPresenter.SHOW_PROCESS_PCC_ZONAL_MEDICAL_HEAD_REQUEST_WIZARD) final ParameterDTO parameters) {
		
		SearchProcessPCCRequestTableDTO tableDto = (SearchProcessPCCRequestTableDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		userName = userName.toUpperCase();
		PccDetailsTableDTO doctorDeatils = new PccDetailsTableDTO();
		if(tableDto.getKey() !=null){
			doctorDeatils = pccRequestService.getPccDetailsFromPCCRequest(pccRequestService.getPCCRequestByKey(tableDto.getKey()));
		}
		RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
		Claim claim = claimService.getClaimsByIntimationNumber(tableDto.getIntimationNo());
		ClaimDto claimDto = claimMapper.getClaimDto(claim);
		if(claim !=null && claim.getIntimation() !=null
				&& claimDto !=null){
			doctorDeatils.setIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
		}	
		doctorDeatils.setClaimDto(claimDto);
		List<Long> querystatusList = ReferenceTable.getPCCQueryStatusKeysByRole(SHAConstants.ZONAL_MEDICAL_HEAD_ROLE);
		List<PCCQuery> pccQueries = pccRequestService.getPCCQueryByStatus(tableDto.getKey(),querystatusList);
		pccRequestService.setPCCZMHQueryAndReplyDetails(doctorDeatils, pccQueries);
		
		view.setProcessPCCZonalMedicalHeadRequestWizardView(ProcessPCCZonalMedicalHeadRequestWizardView.class, doctorDeatils);
	
	}
	
	protected void showsetProcessPCCZonalCoordinatorRequestWizard(
			@Observes @CDIEvent(MenuPresenter.SHOW_PROCESS_PCC_ZONAL_COORDINATOR_REQUEST_WIZARD) final ParameterDTO parameters) {
		
		SearchProcessPCCRequestTableDTO tableDto = (SearchProcessPCCRequestTableDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		userName = userName.toUpperCase();
		PccDetailsTableDTO doctorDeatils = new PccDetailsTableDTO();
		if(tableDto.getKey() !=null){
			doctorDeatils = pccRequestService.getPccDetailsFromPCCRequest(pccRequestService.getPCCRequestByKey(tableDto.getKey()));
		}
		RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
		Claim claim = claimService.getClaimsByIntimationNumber(tableDto.getIntimationNo());
		ClaimDto claimDto = claimMapper.getClaimDto(claim);
		if(claim !=null && claim.getIntimation() !=null
				&& claimDto !=null){
			doctorDeatils.setIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
		}	
		doctorDeatils.setClaimDto(claimDto);
		List<Long> querystatusList = ReferenceTable.getPCCQueryStatusKeysByRole(SHAConstants.ZONAL_COORDINATOR_ROLE);
		List<PCCQuery> pccQueries = pccRequestService.getPCCQueryByStatus(tableDto.getKey(),querystatusList);
		pccRequestService.setPCCZonalDetails(doctorDeatils, pccQueries,SHAConstants.PCC_ZC_ASSIGNED_STATUS);
		
		view.setProcessPCCZonalCoordinatorRequestWizardView(ProcessPCCZonalCoordinatorRequestWizardView.class, doctorDeatils);
	}
	
	protected void showsetProcessPCCHrmCoordinatorRequestWizard(
			@Observes @CDIEvent(MenuPresenter.SHOW_PROCESS_PCC_HRM_COORDINATOR_REQUEST_WIZARD) final ParameterDTO parameters) {
		
		SearchProcessPCCRequestTableDTO tableDto = (SearchProcessPCCRequestTableDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		userName = userName.toUpperCase();
		PccDetailsTableDTO doctorDeatils = new PccDetailsTableDTO();
		if(tableDto.getKey() !=null){
			doctorDeatils = pccRequestService.getPccDetailsFromPCCRequest(pccRequestService.getPCCRequestByKey(tableDto.getKey()));
		}
		RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
		Claim claim = claimService.getClaimsByIntimationNumber(tableDto.getIntimationNo());
		ClaimDto claimDto = claimMapper.getClaimDto(claim);
		if(claim !=null && claim.getIntimation() !=null
				&& claimDto !=null){
			doctorDeatils.setIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
		}	
		doctorDeatils.setClaimDto(claimDto);
		List<Long> querystatusList = ReferenceTable.getPCCQueryStatusKeysByRole(SHAConstants.HRM_COORDINATOR_ROLE);
		List<PCCQuery> pccQueries = pccRequestService.getPCCQueryByStatus(tableDto.getKey(),querystatusList);
		pccRequestService.setPCCZonalDetails(doctorDeatils, pccQueries,SHAConstants.PCC_HRMC_ASSIGNED_STATUS);
		
		view.setProcessPCCHrmCoordinatorRequestWizardView(ProcessPCCHrmCoordinatorRequestWizardView.class, doctorDeatils);
	
	}
	
	protected void showsetProcessPCCReassignHrmCoordinatorRequestWizard(
			@Observes @CDIEvent(MenuPresenter.SHOW_PROCESS_PCC_REASSIGN_HRM_COORDINATOR_REQUEST_WIZARD) final ParameterDTO parameters) {
		
		SearchProcessPCCRequestTableDTO tableDto = (SearchProcessPCCRequestTableDTO) parameters.getPrimaryParameter();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		userName = userName.toUpperCase();
		PccDetailsTableDTO doctorDeatils = new PccDetailsTableDTO();
		if(tableDto.getKey() !=null){
			doctorDeatils = pccRequestService.getPccDetailsFromPCCRequest(pccRequestService.getPCCRequestByKey(tableDto.getKey()));
		}
		RevisedClaimMapper claimMapper = RevisedClaimMapper.getInstance();
		Claim claim = claimService.getClaimsByIntimationNumber(tableDto.getIntimationNo());
		ClaimDto claimDto = claimMapper.getClaimDto(claim);
		if(claim !=null && claim.getIntimation() !=null
				&& claimDto !=null){
			doctorDeatils.setIntimationDto(intimationService.getIntimationDto(claim.getIntimation()));
		}	
		doctorDeatils.setClaimDto(claimDto);
		List<Long> querystatusList = ReferenceTable.getPCCQueryStatusKeysByRole(SHAConstants.HRM_COORDINATOR_ROLE);
		List<PCCQuery> pccQueries = pccRequestService.getPCCQueryByStatus(tableDto.getKey(),querystatusList);
		pccRequestService.setPCCZonalDetails(doctorDeatils, pccQueries,SHAConstants.PCC_HRMC_ASSIGNED_STATUS);
		
		view.setProcessPCCReassignHrmCoordinatorRequestWizardView(ProcessPCCReassginHrmCoordinatorRequestWizardView.class, doctorDeatils);
	
	}
	
	protected void showProcessPCCRequest(
			@Observes @CDIEvent(MenuItemBean.PCC_CO_ORDINATOR) final ParameterDTO parameters) {	
		view.setViewG(SearchPccView.class,true);
	}
	
	protected void showProcessPCCProcessorRequest(
			@Observes @CDIEvent(MenuItemBean.PCC_PROCESSOR) final ParameterDTO parameters) {		
		view.setViewG(SearchPccProcessorView.class,true);
	}
			
	protected void showProcessPCCReviewerRequest(
			@Observes @CDIEvent(MenuItemBean.PCC_REVIEWER) final ParameterDTO parameters) {
		view.setViewG(SearchPccReviewerView.class,true);
	}	
	
	protected void showProcessPCCDivisionHeadRequest(
			@Observes @CDIEvent(MenuItemBean.DIVISION_HEAD) final ParameterDTO parameters) {
		view.setViewG(SearchPccDivisionHeadView.class,true);
	}
	
	protected void showProcessPCCZonalMedicalHeadRequest(
			@Observes @CDIEvent(MenuItemBean.ZONAL_MEDICAL_HEAD_PCC_PROCESS) final ParameterDTO parameters) {	
		view.setViewG(SearchPccZonalMedicalHeadView.class,true);
	}
		
	protected void showProcessPCCZonalCoordinatorRequest(
			@Observes @CDIEvent(MenuItemBean.ZONAL_CO_ORDINATOR) final ParameterDTO parameters) {	
		view.setViewG(SearchPccZonalCoordinatorView.class,true);
	}
	
	protected void showProcessPCCHrmCoordinatorRequest(
			@Observes @CDIEvent(MenuItemBean.HRM_CO_ORDINATOR) final ParameterDTO parameters) {	
		view.setViewG(SearchPccHrmCoordinatorView.class,true);
	}
	
	protected void showStopPaymentRequest(
			@Observes @CDIEvent(MenuItemBean.STOP_PAYMENT_REQUREST) final ParameterDTO parameters) {
		view.setViewG(StopPaymentRequestView.class, true);
	}
	
	protected void showStopPaymentValidation(
			@Observes @CDIEvent(MenuItemBean.STOP_PAYMENT_VALIDATION) final ParameterDTO parameters) {
		view.setViewG(StopPaymentValidationView.class, true);
	}

	protected void showProcessPCCReassignHrmCoordinatorRequest(
			@Observes @CDIEvent(MenuItemBean.REASSIGN_HRM) final ParameterDTO parameters){	
		view.setViewG(SearchPccReassignHrmCoordinatorView.class,true);
	}
	
	protected void showAllocateCorporateBufferWizard(
			@Observes @CDIEvent(SHOW_ALLOCATE_CORP_BUFFER_WIZARD) final ParameterDTO parameters) {
		AllocateCorpBufferTableDTO tableDTO = (AllocateCorpBufferTableDTO) parameters.getPrimaryParameter();
		NewIntimationDto intimationDto = new NewIntimationDto();
		AllocateCorpBufferDetailDTO allocateCorpBufferDetailDTO = new AllocateCorpBufferDetailDTO();
		
		if (tableDTO != null) {
			Claim claim = claimService.getClaimByKey(tableDTO.getKey());
			Intimation intimation = intimationService.getIntimationByKey(claim.getIntimation().getKey());
			Policy policy = intimation.getPolicy();
			Insured insured = intimation.getInsured();
			if(insured != null){
			allocateCorpBufferDetailDTO.setInsuredNo(insured.getInsuredId());
			}
			if(insured.getDependentRiskId() !=null){
				allocateCorpBufferDetailDTO.setInsuredmainNo(insured.getDependentRiskId());
				allocateCorpBufferDetailDTO.setIsDependent(true);
			}else{
				allocateCorpBufferDetailDTO.setInsuredmainNo(insured.getInsuredId());
				allocateCorpBufferDetailDTO.setIsEmployee(true);
			}
			if(allocateCorpBufferDetailDTO.getIsDependent() != null && allocateCorpBufferDetailDTO.getIsDependent().equals(true)){
				insured = rodService.getPAInsuredByPolicyAndInsuredID(policy.getPolicyNumber(),insured.getDependentRiskId());
			}
			intimationDto = intimationService.getIntimationDto(intimation);
			ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(claim);
			GmcCoorporateBufferLimit disBuffer = masterService.getCBBypolicyKey(policy.getKey(),insured.getInsuredSumInsured());
						
			allocateCorpBufferDetailDTO.setClaimKey(claim.getKey());
			allocateCorpBufferDetailDTO.setClaimDto(claimDTO);
			allocateCorpBufferDetailDTO.setNewIntimationDto(intimationDto);
			allocateCorpBufferDetailDTO.setPolicyNo(policy.getPolicyNumber());
			allocateCorpBufferDetailDTO.setInsuredKey(insured.getKey());
			allocateCorpBufferDetailDTO.setStageInformation(claim.getStage().getStageName());
			
			if(disBuffer !=null 
					&& disBuffer.getFamilySiType() != null && disBuffer.getFamilySiType().equalsIgnoreCase(SHAConstants.YES)){
				allocateCorpBufferDetailDTO.setDisBufferIndviSI("YES");
			}else{
				allocateCorpBufferDetailDTO.setDisBufferIndviSI("NO");
			}
			
			if(policy.getNacbBufferAmt() != null && policy.getNacbBufferAmt() > 0){
				allocateCorpBufferDetailDTO.setIsNacBufferApplicable(true);
			}
			if(policy.getWintageBufferAmt() != null && policy.getWintageBufferAmt() > 0){
				allocateCorpBufferDetailDTO.setIsWintageBufferApplicable(true);
			}
			if(disBuffer !=null){
				allocateCorpBufferDetailDTO.setIsDisBufferApplicable(true);
			}
			
			
			if(insured !=null){
				allocateCorpBufferDetailDTO.setInsureName(insured.getInsuredName());
				allocateCorpBufferDetailDTO.setInsureAge(insured.getInsuredAge().longValue());
				if(insured.getRelationshipwithInsuredId() !=null){
					allocateCorpBufferDetailDTO.setInsurerelationShip(insured.getRelationshipwithInsuredId().getValue());
				}
				if(insured.getDependentRiskId() !=null){
					allocateCorpBufferDetailDTO.setInsuredmainNo(insured.getDependentRiskId());
					allocateCorpBufferDetailDTO.setIsDependent(true);
				}else{
					allocateCorpBufferDetailDTO.setInsuredmainNo(insured.getInsuredId());
					allocateCorpBufferDetailDTO.setIsEmployee(true);
				}
			}
			if(disBuffer !=null){
				allocateCorpBufferDetailDTO.setDisBufferApplTo(disBuffer.getLimitApplicable());
				if(disBuffer.getTotalNoSi() != null && disBuffer.getTotalNoSi() > 0){
				allocateCorpBufferDetailDTO.setNoOfTimes(disBuffer.getTotalNoSi());
				}
			}else{			
				allocateCorpBufferDetailDTO.setDisBufferApplTo("-");
			}
			
			if(policy.getPolicyNumber() !=null && insured.getKey()!=null 
					&& allocateCorpBufferDetailDTO.getInsuredmainNo() !=null){
				Map<String, Double> disBufferValues = dbCalService.getGmcCorpBufferASIForVwDeatils(SHAConstants.PRC_BUFFERTYPE_CB,
						policy.getPolicyNumber(),insured.getKey(),allocateCorpBufferDetailDTO.getInsuredmainNo(),claim.getKey());
				allocateCorpBufferDetailDTO.setDisBufferSI(disBufferValues.get(SHAConstants.LN_POLICY_BUFFER_SI));
				allocateCorpBufferDetailDTO.setDisBufferUtilizedAmt(disBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
				if(disBufferValues.get(SHAConstants.LN_MAX_INSURED_ALLOCATE_AMT) !=null){
					allocateCorpBufferDetailDTO.setMaxdisBufferInsuredLimit(disBufferValues.get(SHAConstants.LN_MAX_INSURED_ALLOCATE_AMT));
				}
				if(allocateCorpBufferDetailDTO.getDisBufferSI() !=null 
						&& allocateCorpBufferDetailDTO.getDisBufferUtilizedAmt() !=null){
					Double avlbln = allocateCorpBufferDetailDTO.getDisBufferSI() - allocateCorpBufferDetailDTO.getDisBufferUtilizedAmt();
					allocateCorpBufferDetailDTO.setPolicywisedisBufferAvlBlnc(avlbln);
				}
				
					allocateCorpBufferDetailDTO.setDisBufferInsuredLimit(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));
					allocateCorpBufferDetailDTO.setDisAllocatedLimit(disBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));
					allocateCorpBufferDetailDTO.setDiscretionaryUtilizedInsured(disBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) != null ? disBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) : 0d);
					Double disAvlbln = allocateCorpBufferDetailDTO.getDisAllocatedLimit() - allocateCorpBufferDetailDTO.getDiscretionaryUtilizedInsured();
					if(disAvlbln < 0){
						disAvlbln =0d;
					}
					allocateCorpBufferDetailDTO.setDisAvlBalnc(disAvlbln);
					allocateCorpBufferDetailDTO.setDisBufferAvailBalnc(disAvlbln);
				
//				CoorporateBuffer discretionaryBuffer = corpBufferService.getBufferbyinsuerdNo(insured.getInsuredId(),SHAConstants.PRC_BUFFERTYPE_CB);

				Map<String, Double> WintageBufferValues = dbCalService.getGmcCorpBufferASIForVwDeatils(SHAConstants.PRC_BUFFERTYPE_WINTAGE,
						policy.getPolicyNumber(),insured.getKey(),allocateCorpBufferDetailDTO.getInsuredmainNo(),claim.getKey());
				allocateCorpBufferDetailDTO.setMaxwintageBufferLimit(WintageBufferValues.get(SHAConstants.LN_POLICY_BUFFER_SI));
				if(allocateCorpBufferDetailDTO.getMaxwintageBufferLimit() !=null 
						&& WintageBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT) !=null){
					//allocateCorpBufferDetailDTO.setWintageBufferLimit(WintageBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
					allocateCorpBufferDetailDTO.setWintageAllocatedLimit(WintageBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
					Double avlbln = allocateCorpBufferDetailDTO.getMaxwintageBufferLimit() - WintageBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT);
					allocateCorpBufferDetailDTO.setWintageBufferAvlBalnc(avlbln);
				}
				
				CoorporateBuffer wintageBuffer  = corpBufferService.getBufferbyMainMemeberID(insured.getInsuredId(),SHAConstants.PRC_BUFFERTYPE_WINTAGE);
				if(wintageBuffer !=null 
						&& wintageBuffer.getAllocatedAmount()!=null){
//					SelectValue wintageApplicable = new SelectValue();
//					wintageApplicable.setValue(wintageBuffer.getLimitApplicable());
//					allocateCorpBufferDetailDTO.getWinBufferApplTo().setValue(wintageApplicable.getValue());
					allocateCorpBufferDetailDTO.setWintageBufferLimit(WintageBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));
					allocateCorpBufferDetailDTO.setWintageUtilizedInsured(WintageBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) != null ? WintageBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) : 0d);
					if(allocateCorpBufferDetailDTO.getWintageBufferLimit() !=null){
						Double avlbln = allocateCorpBufferDetailDTO.getWintageBufferLimit() - allocateCorpBufferDetailDTO.getWintageUtilizedInsured();
						allocateCorpBufferDetailDTO.setWintageAvlBalnc(avlbln);
					}
					
				}
				Map<String, Double> nacBufferValues = dbCalService.getGmcCorpBufferASIForVwDeatils(SHAConstants.PRC_BUFFERTYPE_NACB,
						policy.getPolicyNumber(),insured.getKey(),allocateCorpBufferDetailDTO.getInsuredmainNo(),claim.getKey());
				allocateCorpBufferDetailDTO.setMaxnacBufferLimit(nacBufferValues.get(SHAConstants.LN_POLICY_BUFFER_SI));
				if(allocateCorpBufferDetailDTO.getMaxnacBufferLimit() !=null 
						&& nacBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT) !=null){
					//allocateCorpBufferDetailDTO.setNacBufferLimit(nacBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
					allocateCorpBufferDetailDTO.setNacAllocatedLimit(nacBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT));
					Double avlbln = allocateCorpBufferDetailDTO.getMaxnacBufferLimit() - nacBufferValues.get(SHAConstants.LN_BUFFER_UTILISED_AMT);
					allocateCorpBufferDetailDTO.setNacBufferAvlBalnc(avlbln);			
				}
				CoorporateBuffer nacBuffer  = corpBufferService.getBufferbyMainMemeberID(insured.getInsuredId(),SHAConstants.PRC_BUFFERTYPE_NACB);
				if(nacBuffer !=null 
						&& nacBuffer.getAllocatedAmount()!=null){
//					SelectValue nacbApplicable = new SelectValue();
//					nacbApplicable.setValue(nacBuffer.getLimitApplicable());
//					allocateCorpBufferDetailDTO.getNacbBufferApplTo().setValue(nacbApplicable.getValue());
					allocateCorpBufferDetailDTO.setNacBufferLimit(nacBufferValues.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));
					allocateCorpBufferDetailDTO.setNacbUtilizedInsured(nacBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) != null ? nacBufferValues.get(SHAConstants.LN_INSURED_UTIL_AMT) : 0d);
					if(allocateCorpBufferDetailDTO.getNacBufferLimit() !=null){
						Double avlbln = allocateCorpBufferDetailDTO.getNacBufferLimit() - allocateCorpBufferDetailDTO.getNacbUtilizedInsured();
						allocateCorpBufferDetailDTO.setNacAvlBalnc(avlbln);
					}
					
				}
			}
		}
		String userName = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		allocateCorpBufferDetailDTO.setUserName(userName);

		view.setAllocateCorporateBufferWizardView(AllocateCorpBufferWizard.class, allocateCorpBufferDetailDTO);
	}
	
	protected void showProcessClaimCommonBillingAndFinancialsAutoAllocation(
			@Observes @CDIEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION) final ParameterDTO parameters) {
		
		BeanItemContainer<SelectValue> claimType = masterService
				.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
		BeanItemContainer<SelectValue> productName = masterService.getSelectValueContainerForProduct();
		BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();
		BeanItemContainer<SelectValue> type = masterService.getStatusByStage(ReferenceTable.BILLING_STAGE);
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
		
		Stage stageByKey = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		
		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.BILLING_STAGE);
		
		SelectValue selectValue = new SelectValue();
		selectValue.setId(stageByKey.getKey());
		selectValue.setValue(stageByKey.getStageName());
		
		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue);
		statusByStage.addBean(selectValue1);
		
		
		view.setViewForSearchClaimCommonBillingAndFinancialAutoAllocation(SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView.class, true,claimType,productName,cpuCode,type,selectValueForPriority,statusByStage);

}
protected void showPhysicalReceivedDocuments(
			@Observes @CDIEvent(MenuItemBean.PHYSICAL_DOCUMENT_CHECKER) final ParameterDTO parameters) {
		view.setPhysicalVerification(PhysicalDocumentReceivedMakerView.class, true,SHAConstants.PHYSICAL_DOCUMENT_CHECKER);
	}
	
	protected void showProcessClaimRequestAutoAllocation(
			@Observes @CDIEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION) final ParameterDTO parameters) {
		
		
		view.setViewForSearchProcessClaimRequestAutoAllocation(SearchProcessClaimRequestAutoAllocationView.class, true);
	}
	
	//Uncommented for reimbursement auto allocation
			protected void showHoldMonitorScreen(
					@Observes @CDIEvent(MenuItemBean.HOLD_MONITOR_SCREEN_FOR_MA) final ParameterDTO parameters) {
				
				SearchHoldMonitorScreenFormDTO searchFormDto = (SearchHoldMonitorScreenFormDTO) parameters.getPrimaryParameter();
				
				
				SelectValue selectValue8 = new SelectValue();
				selectValue8.setId(1l);
				selectValue8.setValue("Process Claim Request - Auto Allocation");
				
//				SelectValue selectValue9 = new SelectValue();
//				selectValue9.setId(2l);
//				selectValue9.setValue("ENHANCEMENT");
				
				BeanItemContainer<SelectValue> type = new BeanItemContainer<SelectValue>(SelectValue.class);
				type.addBean(selectValue8);
//				type.addBean(selectValue9);
				
				BeanItemContainer<SelectValue> userList = masterService
						.getRefferedByDocList();
				BeanItemContainer<SelectValue> cpuCode = masterService
						.getTmpCpuCodes();
				String screenName = SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION;
				view.setViewsHoldMoniterScreenForMA(SearchHoldMonitorScreenView.class,true,type,userList,cpuCode,screenName);
			}
			
			protected void showCreateOnlineROD(
					@Observes @CDIEvent(MenuItemBean.CREATE_ONLINE_ROD) final ParameterDTO parameters) {
				BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService.getTmpCpuCodes();
				BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();			
				Status status = preauthService.getStatusByKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
				
				SelectValue selectValue = new SelectValue();
				selectValue.setId(status.getKey());
				selectValue.setValue(status.getProcessValue());
				BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
				statusByStage.addBean(selectValue);
				BeanItemContainer<SelectValue> selectValueForUploadedDocument = masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES);
				
				view.setViewCreateOnlineROD(SearchCreateOnlineRODView.class, true,
						selectValueContainerForCPUCode,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
			}
			
			protected void showSearchDataCorrectionPriority(
					@Observes @CDIEvent(MenuItemBean.DATA_CODING_DATA_CORRECTION_PRIORITY) final ParameterDTO parameters) {
				view.setViewG(SearchProcessDataCorrectionPriorityView.class,true);
			}
			
			protected void setViewProcessDataCorrectionPriority(
					@Observes @CDIEvent(MenuItemBean.PROCESS_DATA_CORRECTION_PRIORITY_VIEW) final ParameterDTO parameters) {
				
				ProcessDataCorrectionDTO tableDto = (ProcessDataCorrectionDTO) parameters.getPrimaryParameter();
				tableDto = searchProcessDataCorrectionPriorityService.getCorrectionDatas(tableDto);
				view.setViewProcessDataCorrectionPriority(DataCorrectionPriorityView.class,true,tableDto);
			}
				protected void showProcessFinancialApprovalAutoAllocation(
					@Observes @CDIEvent(MenuItemBean.FINANCIALS_APPROVAL_AUTO_ALLOCATION) final ParameterDTO parameters) {
				
				BeanItemContainer<SelectValue> claimType = masterService
						.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
				BeanItemContainer<SelectValue> productName = masterService.getSelectValueContainerForProduct();
				BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();
				BeanItemContainer<SelectValue> type = masterService.getStatusByStage(ReferenceTable.BILLING_STAGE);
				
				BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
				
				Stage stageByKey = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
				
				Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.BILLING_STAGE);
				
				SelectValue selectValue = new SelectValue();
				selectValue.setId(stageByKey.getKey());
				selectValue.setValue(stageByKey.getStageName());
				
				SelectValue selectValue1 = new SelectValue();
				selectValue1.setId(stageByKey2.getKey());
				selectValue1.setValue(stageByKey2.getStageName());
				
				BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
				statusByStage.addBean(selectValue);
				statusByStage.addBean(selectValue1);
				
				
				view.setViewForSearchFinancialApprovalAutoAllocation(SearchProcessFinancialApprovalAutoAllocationView.class, true,claimType,productName,cpuCode,type,selectValueForPriority,statusByStage);

		}
			
			//Uncommented for reimbursement auto allocation
			protected void showHoldMonitorScreenForFA(
					@Observes @CDIEvent(MenuItemBean.HOLD_MONITOR_SCREEN_FOR_FA_AUTO) final ParameterDTO parameters) {
				
				SearchHoldMonitorScreenFormDTO searchFormDto = (SearchHoldMonitorScreenFormDTO) parameters.getPrimaryParameter();
				
				
				SelectValue selectValue8 = new SelectValue();
				selectValue8.setId(1l);
				selectValue8.setValue("Process Claim Financial - Auto Allocation");
				
				BeanItemContainer<SelectValue> type = new BeanItemContainer<SelectValue>(SelectValue.class);
				type.addBean(selectValue8);
				
				BeanItemContainer<SelectValue> userList = masterService
						.getRefferedByDocList();
				BeanItemContainer<SelectValue> cpuCode = masterService
						.getTmpCpuCodes();
				String screenName = SHAConstants.HOLD_MONITORING_PROCESS_FOR_FINANCIAL_AUTO;
				view.setViewsHoldMoniterScreenForFA(SearchHoldMonitorScreenView.class,true,type,userList,cpuCode,screenName);
			}
			
			protected void showProcessClaimBillingAutoAllocation(
			@Observes @CDIEvent(MenuItemBean.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION) final ParameterDTO parameters) {
		view.setViewForSearchClaimBillingAutoAllocation(SearchProcessClaimBillingAutoAllocationView.class, true);
	}
	
	protected void showHoldMonitorScreenBilling(
			@Observes @CDIEvent(MenuItemBean.HOLD_MONITOR_SCREEN_FOR_BILLING) final ParameterDTO parameters) {		
		
		SelectValue selectValue8 = new SelectValue();
		selectValue8.setId(1l);
		selectValue8.setValue("Process Claim Billing - Auto Allocation");
		
		BeanItemContainer<SelectValue> type = new BeanItemContainer<SelectValue>(SelectValue.class);
		type.addBean(selectValue8);
		
		BeanItemContainer<SelectValue> userList = masterService.getRefferedByDocList();
		BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();
		String screenName = SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_BILLING_AUTO_ALLOCATION;
		view.setViewsHoldMoniterScreenForMA(SearchHoldMonitorScreenView.class,true,type,userList,cpuCode,screenName);
	}
	
	protected void showUpdateDispatchReport(
			@Observes @CDIEvent(MenuItemBean.SEARCH_DISPATCH_DETAILS_REPORT) final ParameterDTO parameters) {	
		view.setUpdateDispatchReportView(DispatchDetailsReportView.class, true);
	}
	protected void showProvisionHistoryView(
			@Observes @CDIEvent(MenuItemBean.PROVISION_HISTORY) final ParameterDTO parameters) {
		view.setViewG(ProvisionHistoryView.class, true);
	}
}


