package com.shaic.main.navigator.ui;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.vaadin.addon.cdimvp.MVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.branchmanagerfeedback.BranchManagerFeedbackView;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.aadhar.search.SearchUpdateAadharTableDTO;
import com.shaic.claim.aadhar.search.SearchUpdateAadharView;
import com.shaic.claim.adviseonped.AdviseOnPEDView;
import com.shaic.claim.adviseonped.search.SearchAdviseOnPEDTableDTO;
import com.shaic.claim.bedphoto.SearchBedPhoto;
import com.shaic.claim.bedphoto.SearchBedPhotoTableDTO;
import com.shaic.claim.bedphoto.UploadBedPhotoView;
import com.shaic.claim.bulkconvertreimb.search.SearchBatchConvertedTableDto;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search.SearchFlagReconsiderationReqTableDTO;
import com.shaic.claim.cashlessprocess.processicac.search.SearchProcessICACTableDTO;
import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessTableDTO;
import com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess.SearchWithdrawCashLessPostProcessTableDTO;
import com.shaic.claim.corpbuffer.allocation.wizard.AllocateCorpBufferDetailDTO;
import com.shaic.claim.cvc.SearchCVCTableDTO;
import com.shaic.claim.cvc.SearchCVCView;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryFormDTO;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.claim.fss.filedetail.ProcessDataEntryWizardViewImpl;
import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;
import com.shaic.claim.fss.searchfile.SearchDataEntryView;
import com.shaic.claim.fvrgrading.page.FvrReportGradingPageDto;
import com.shaic.claim.fvrgrading.search.SearchFvrReportGradingTableDto;
import com.shaic.claim.medical.opinion.OpinionValidationView;
import com.shaic.claim.medical.opinion.RecMarketingEscalationView;
import com.shaic.claim.medical.opinion.RecordMarkEscDTO;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpView;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardTableDTO;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardView;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.hrmp.SearchHRMPTableDTO;
import com.shaic.claim.ped.outsideprocess.InitiatePedView;
import com.shaic.claim.pedquery.PEDQueryDTO;
import com.shaic.claim.pedquery.PEDQueryView;
import com.shaic.claim.pedquery.search.SearchPEDQueryTableDTO;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApproveView;
import com.shaic.claim.pedrequest.approve.bancspedQuery.BancsSearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.approve.bancspedrequest.BancsPEDRequestDetailsApproveView;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveFormDTO;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.process.PEDRequestDetailsProcessDTO;
import com.shaic.claim.pedrequest.process.PEDRequestDetailsProcessView;
import com.shaic.claim.pedrequest.process.search.SearchPEDRequestProcessTableDTO;
import com.shaic.claim.pedrequest.teamlead.PEDRequestDetailsTeamLeadView;
import com.shaic.claim.policy.search.ui.SearchPolicyView;
import com.shaic.claim.policy.search.ui.opsearch.SearchSettlementLetterProcessOPClaimRequestView;
//import com.shaic.claim.preauth.negotiation.SearchUncheckNegotiationView;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.claim.reassignfieldvisit.search.SearchReAssignFieldVisitTableDTO;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationTableDTO;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationView;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.claim.registration.convertclaimcashless.SearchConverClaimCashlessTableDTO;
import com.shaic.claim.registration.convertclaimcashlesspage.ConvertClaimCashlessPageView;
import com.shaic.claim.reimbursement.FinancialApprovalAutoAllocation.SearchProcessFinancialApprovalAutoAllocationView;
import com.shaic.claim.reimbursement.commonBillingFA.SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView;
import com.shaic.claim.reimbursement.commonBillingFA.SearchProcessClaimCommonBillingAndFinancialsView;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotView;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.opscreen.OpView;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageView;
import com.shaic.claim.reimbursement.processClaimRequestAutoAllocation.SearchProcessClaimRequestAutoAllocationView;
import com.shaic.claim.reimbursement.processDraftRejectionLetterDetail.ClaimRejectionDto;
import com.shaic.claim.reimbursement.processdraftquery.ClaimQueryDto;
import com.shaic.claim.reimbursement.rawanalysis.ProcessRawRequestWizard;
import com.shaic.claim.reimbursement.rawanalysis.SearchProcessRawRequestTableDto;
import com.shaic.claim.reimbursement.rawanalysis.SearchProcessRawRequestView;
import com.shaic.claim.reimbursement.rrc.detailsPage.ModifyRRCRequestDataExtractionWizard;
import com.shaic.claim.reimbursement.rrc.detailsPage.ProcessRRCRequestDataExtractionWizard;
import com.shaic.claim.reimbursement.rrc.detailsPage.ReviewRRCRequestDataExtractionWizard;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestView;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestWizardView;
import com.shaic.claim.reimbursement.rrc.services.SearchModifyRRCRequestView;
import com.shaic.claim.reimbursement.rrc.services.SearchProcessRRCRequestView;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCRequestView;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCStatusView;
import com.shaic.claim.reimbursement.rrc.services.SearchReviewRRCRequestView;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkView;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkWizardView;
import com.shaic.claim.reports.ExecutiveStatusReport.ExecutiveStatusDetailReportView;
import com.shaic.claim.reports.autoClosureBatch.AutoClosureBatchView;
import com.shaic.claim.reports.autoallocationaancelreport.AutoAllocationCancelReportView;
import com.shaic.claim.reports.billreceivedreport.BillReceivedReportView;
import com.shaic.claim.reports.callcenterDashBoard.CallcenterDashBoardReportView;
import com.shaic.claim.reports.claimsdailyreportnew.ClaimsDailyReportView;
import com.shaic.claim.reports.claimstatusreportnew.ClaimsStatusReportView;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUwisePreauthReportView;
import com.shaic.claim.reports.dispatchDetailsReport.DispatchDetailsReportView;
import com.shaic.claim.reports.executivesummaryreqort.ExecutiveStatusSummaryReportView;
import com.shaic.claim.reports.fraudanalysis.FraudReportView;
import com.shaic.claim.reports.gmcdailyreport.GmcDailyReportView;
import com.shaic.claim.reports.marketingEscalationReport.MarketingEscalationReportView;
import com.shaic.claim.reports.medicalAuditCashlessIssueanceReport.MedicalAuditCashlessIssuanceReportView;
import com.shaic.claim.reports.medicalAuditClaimStatusReport.MedicalAuditClaimStatusReportView;
import com.shaic.claim.reports.negotiationreport.NegotiationReportView;
import com.shaic.claim.reports.opclaimreport.OPClaimReportView;
import com.shaic.claim.reports.opinionvalidationreport.OpinionValidationReportView;
import com.shaic.claim.reports.shadowProvision.SearchShowdowView;
import com.shaic.claim.reports.tatreport.TATReportView;
import com.shaic.claim.rod.wizard.cancelAcknowledgement.CancelAcknowledgementWizardView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.search.specialist.search.SubmitSpecialistFormDTO;
import com.shaic.claim.search.specialist.search.SubmitSpecialistTableDTO;
import com.shaic.claim.settlementpullback.SettlementPullBackView;
import com.shaic.claim.settlementpullback.dto.SearchSettlementPullBackDTO;
import com.shaic.claim.settlementpullback.searchbatchpendingpullback.LotPullBackPageView;
import com.shaic.claim.settlementpullback.searchbatchpendingpullback.SearchLotPullBackTableDTO;
import com.shaic.claim.specialist.SpecialistViewImpl;
import com.shaic.claim.userproduct.document.ProductAndDocumentTypeDTO;
import com.shaic.claim.userproduct.document.ProductAndDocumentTypeView;
import com.shaic.claim.userproduct.document.search.UserACtivationView;
import com.shaic.claim.userproduct.document.search.UserManagementDTO;
import com.shaic.claim.userproduct.document.search.UserManagementView;
import com.shaic.claim.userreallocation.SearchReallocationDoctorDetailsTableDTO;
import com.shaic.claim.userreallocation.SearchReallocationDoctorDetailsView;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Policy;
import com.shaic.domain.gmcautomailer.GmcAutomailerView;
import com.shaic.feedback.managerfeedback.BranchManagerFeedbackhomePageDto;
import com.shaic.feedback.managerfeedback.FeedbackStatsDto;
import com.shaic.feedback.managerfeedback.ManagerFeedBackView;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackView;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageDTO;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageView;
import com.shaic.main.navigator.domain.MenuItem;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.shaic.newcode.wizard.dto.HospitalAcknowledgeDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.cashless.withdraw.search.PASearchWithdrawCashLessProcessTableDTO;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.search.PASearchProcessClaimFinancialsNonHospView;
import com.shaic.paclaim.health.reimbursement.financial.search.PAHealthSearchProcessClaimFinancialsView;
import com.shaic.paclaim.health.reimbursement.medicalapproval.search.PAHealthSearchProcessClaimRequestFormDTO;
import com.shaic.paclaim.healthsettlementpullback.PAHospSettlementPullBackView;
import com.shaic.paclaim.healthsettlementpullback.dto.PAHospSearchSettlementPullBackDTO;
import com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel.PACloseClaimPageDTO;
import com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel.PACloseClaimView;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimTableDTORODLevel;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageClaimLevel.PAHealthReopenClaimPageDTO;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageClaimLevel.PAHealthReopenClaimView;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageRodLevel.PAHealthReOpenRodLevelClaimView;
import com.shaic.paclaim.manageclaim.healthreopenclaim.searchRodLevel.PAHealthSearchReOpenClaimRodLevelTableDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel.PAReopenClaimPageDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel.PAReopenClaimView;
import com.shaic.paclaim.manageclaim.reopenclaim.pageRodLevel.PAReOpenRodLevelClaimView;
import com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel.PASearchReOpenClaimRodLevelTableDTO;
import com.shaic.paclaim.settlementpullback.PASettlementPullBackView;
import com.shaic.paclaim.settlementpullback.dto.PASearchSettlementPullBackDTO;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingAutoAllocationView;
import com.shaic.reimbursement.claims_alert.search.ClaimsAlertMasterView;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigatorDto;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsView;
import com.shaic.reimbursement.fraudidentification.FraudIdentificationView;
import com.shaic.reimbursement.investigation.ackinvestivationcompleted.search.SearchAckInvestigationCompletedTableDTO;
import com.shaic.reimbursement.investigationin_direct_assigment.search.InvestigationDirectAssignmentView;
import com.shaic.reimbursement.investigationmaster.InvestigationMasterDTO;
import com.shaic.reimbursement.manageclaim.ClaimWiseAllowApprovalPages.ClaimWiseAllowApprovalView;
import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.CloseClaimPageDTO;
import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.CloseClaimView;
import com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel.SearchCloseClaimTableDTORODLevel;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageClaimLevel.ReopenClaimPageDTO;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageClaimLevel.ReopenClaimView;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageRodLevel.ReOpenRodLevelClaimView;
import com.shaic.reimbursement.manageclaim.reopenclaim.searchRodLevel.SearchReOpenClaimRodLevelTableDTO;
import com.shaic.reimbursement.manageclaim.searchClaimwiseApproval.SearchClaimWiseAllowApprovalDto;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestFormDTO;
import com.shaic.reimbursement.paymentprocess.createbatch.search.SearchCreateBatchView;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.PopupStopPaymentRequestWizard;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.PopupStopPaymentValidateWizard;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailTableDTO;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailView;
import com.shaic.reimbursement.processi_investigationi_initiated.search.SearchProcessInvestigationInitiatedTableDTO;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterTableDTO;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;
import com.shaic.reimbursement.topup_policy_master.search.TopUpPolicyMasterView;
import com.shaic.reimbursement.uploadTranslatedDocument.UploadTranslatedDocumentView;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface MenuView extends MVPView {

    void setView(Class<? extends MVPView> viewClass,
            boolean selectInNavigationTree);
    
    void enterIntimationView();
    
    void setViewG(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree);
    void setViewGG(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree);
    
    
    void setViewHospital(Class<? extends GMVPView> viewClass,
            BeanItemContainer<SelectValue> selectedContainer,BeanItemContainer<SelectValue> modeOfIntimation,BeanItemContainer<SelectValue> intimatedBy);
    
    void setSubmitSpecialist(Class<? extends MVPView> viewClass,SubmitSpecialistTableDTO bean);
    
    void setSubmitSpecialistAdvise(Class<? extends MVPView> viewClass,SubmitSpecialistTableDTO bean);
    
    void setViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, Map<String,Object> parameter);
    
    public void setViewAllowGHIRegister(Class<? extends GMVPView> viewClass,
    		Map<String,Object> parameter);
    
    void setPAClaimRegisterView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, Map<String,Object> parameter);
    
   
   /* void setViewG(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree, BeanItemContainer<TmpCPUCode> parameter);*/
    /**
     * Commenting the above code, since BeanContainer<SelectValue> is the valid
     * parameter that has to be passed to master service, to display the values
     * in the combo box. Hence below method is added with valid parameter.
     * */
    void setViewG(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree, BeanItemContainer<SelectValue> parameter);
    
    void setBulkConvertReimbView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpuParamContainer, BeanItemContainer<SelectValue> typeVlueParamContainer,
			List<SearchBatchConvertedTableDto> prevConvertedBatch);
    
    /**
     * The below method is added for submit specialist advise, where reffered by parameter
     * is a drop down. The above method is used by convert claim search screen menu.
     * modifying the above code will cause an impact if the same is used by other claim
     * related services. Hence introducing a new one.
     * */
    void setViewG(Class<? extends GMVPView> viewClass,
           BeanItemContainer<SelectValue> parameter, boolean selectInNavigationTree, boolean isReimburement, BeanItemContainer<SelectValue> cpuCodeContainer);
    /**
     * The below method is added for implementing intimation source , network hospital type , treatment type
     *  combo box in pre auth search related screens.
     **/
    void setViewG(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree, BeanItemContainer<SelectValue> intimationSrcParameter,
            BeanItemContainer<SelectValue> networkHospTypeParameter,BeanItemContainer<SelectValue> treatmentTypeParameter,BeanItemContainer<SelectValue> preAuthType,BeanItemContainer<SelectValue> specialityContainer,BeanItemContainer<SelectValue> cpuCodeContainer,String strViewName);
    
    /**
     * The below method is added for populating drop down values in search policy UI screen.
     * 
     * */
    void setViewG(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree,BeanItemContainer<SelectValue> searchByContainer ,BeanItemContainer<SelectValue> productNameContainer, 
			  BeanItemContainer<SelectValue> productTypeContainer,BeanItemContainer<SelectValue> policyCodeOrNameContainer);
    
    void setViewGOPRegisterClaim(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree,BeanItemContainer<SelectValue> searchByContainer ,BeanItemContainer<SelectValue> productNameContainer, 
			  BeanItemContainer<SelectValue> productTypeContainer,BeanItemContainer<SelectValue> policyCodeOrNameContainer);
    
    /*void setViewGOPExpiredPolicyClaim(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree,BeanItemContainer<SelectValue> searchByContainer ,BeanItemContainer<SelectValue> productNameContainer, 
			  BeanItemContainer<SelectValue> productTypeContainer,BeanItemContainer<SelectValue> policyCodeOrNameContainer);*/
    
    void setView(Class<? extends MVPView> viewClass, boolean selectInNavigationTree,ParameterDTO parameter);
    
    public void setupMenu(MenuItem rootMenu);

//  public void setNewIntimationView(Class<? extends GMVPView> viewClass, NewIntimationDto newIntimationDto);
    
   public void setNewIntimationView(Class<? extends GMVPView> viewClass, NewIntimationDto newIntimationDto,ParameterDTO parameters);
    
    
//    public void setIntimationWizardView(Class<? extends GMVPView> viewClass, IntimationDTO intimationDTO);
    
//    public void setIntimationWizardView(Class<? extends GMVPView> viewClass, TmpPolicy policy, LinkedHashMap<String, String> policyValues);
//    
//    public void setIntimationWizardView(Class<? extends GMVPView> viewClass, TmpPolicy policy, IntimationDTO intimationDTO);
    
    public void showLoginSuccess(ImsUser imsUser);
    
    public void showCreateIntimationView();

    public void setPreauthWizardView(Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO,Boolean processId);
    
    public void setPreauthEnhancementWizardView(Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO);
    
    public void setPreMedicalWizardView(Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO,Boolean processId);
    
    public void setPreMedicalEnhancementView(Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO);
    
    public void setHospitalView(Class<? extends GMVPView> viewClass, HospitalAcknowledgeDTO hospitalDTO,SearchAcknowledgeHospitalCommunicationTableDTO hospitalFormDto,NewIntimationDto intimationDto, ClaimDto claimDto);
    
    public void setRejectionView(Class<? extends GMVPView> viewClass, SearchProcessRejectionTableDTO searchProcessRejectionTableDTO);
    
    public void setFileUploadTableView(Class<? extends GMVPView> viewClass, SearchProcessTranslationTableDTO fileUploadDTO);
    
    
    public void setConvertClaimView(Class<? extends GMVPView>viewClass,ConvertClaimDTO convertClaimDto,BeanItemContainer<SelectValue> selectValueContainer, NewIntimationDto intimationDto, SearchConvertClaimTableDto searchFormDto);


	void setPEDRequestProcess(Class<PEDRequestDetailsProcessView> viewClass,
			SearchPEDRequestProcessTableDTO primaryParameter);

	void setPEDRequestApproved(Class<PEDRequestDetailsApproveView> class1,
			SearchPEDRequestApproveTableDTO primaryParameter);
	
	void setPEDQueryRequestApproved(Class<BancsPEDRequestDetailsApproveView> class1,
			BancsSearchPEDRequestApproveTableDTO primaryParameter);
	
	void setPEDRequestTeamLeadView(Class<PEDRequestDetailsTeamLeadView> viewClass,
			SearchPEDRequestApproveTableDTO primaryParameter);

	void setAdviceOnPEDPage(Class<AdviseOnPEDView> class1,
			SearchAdviseOnPEDTableDTO primaryParameter, OldPedEndorsementDTO editDTO);
	
	void setSubmitSpecialist(Class<SpecialistViewImpl> viewClass);
    
    public void setFieldVisitView(Class<? extends GMVPView> viewClass, SearchFieldVisitTableDTO fieldVisitDTO);
    
    public void setUpdateInternalNotesView(Class<? extends GMVPView> viewClass,
    		NewIntimationDto newIntimationDto, ClaimDto claimDTO);
    
    public void setReAssignFieldVisitView(Class<? extends GMVPView> viewClass, SearchReAssignFieldVisitTableDTO fieldVisitDTO);

	void setPEDRequestProcess(Class<? extends GMVPView> viewClass,
			PEDRequestDetailsProcessDTO pedRequestDetailsProcessDTO);

	void setPEDQueryPage(Class<PEDQueryView> class1,
			SearchPEDQueryTableDTO primaryParameter,OldPedEndorsementDTO secondaryParameter);
	
	void redirectAndLoad(Class<? extends MVPView> viewClass, String url);
	
	void setClaimRegistrationForm(Class<? extends MVPView> viewClass, SearchClaimRegistrationTableDto searchClaimRegistrationTableDto);
	
	void setGhiAllowClaimRegistrationForm(Class<? extends MVPView> viewClass, SearchClaimRegistrationTableDto searchClaimRegistrationTableDto);
	
	void setPAClaimRegistrationForm(Class<? extends MVPView> viewClass, SearchClaimRegistrationTableDto searchClaimRegistrationTableDto, BeanItemContainer<SelectValue> selectValueForCategory);
	
	void setWithdrawPreauthView(Class<? extends GMVPView> viewClass,SearchWithdrawCashLessProcessTableDTO tableDTO, NewIntimationDto intimationDto, ClaimDto claimDto, BeanItemContainer<SelectValue> selectValueContainer);
	
	void setWithdrawPreauthPostProcessView(Class<? extends GMVPView> viewClass,SearchWithdrawCashLessPostProcessTableDTO tableDTO, NewIntimationDto intimationDto, ClaimDto claimDto, BeanItemContainer<SelectValue> selectValueContainer,ConvertClaimDTO convertClaimDto,BeanItemContainer<SelectValue> conversionContainer);
	
	void setPAWithdrawPreauthView(Class<? extends GMVPView> viewClass,PASearchWithdrawCashLessProcessTableDTO tableDTO, NewIntimationDto intimationDto, ClaimDto claimDto, BeanItemContainer<SelectValue> selectValueContainer);
	
	void setDownsizePreauth(Class<? extends GMVPView> viewClass, PreauthDTO preauthDto, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> escalateContainer);
	
	void setPADownsizePreauth(Class<? extends GMVPView> viewClass, PreauthDTO preauthDto, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> escalateContainer);
	
	void setDownsizePreauthRequest(Class<? extends GMVPView> viewClass, PreauthDTO preauthDto, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> escalateContainer);
	
	void setPADownsizePreauthRequest(Class<? extends GMVPView> viewClass, PreauthDTO preauthDto, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> escalateContainer);

	void setAcknowledgeDocReceivedWizardView(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);
	void setPAAcknowledgeDocReceivedWizardView(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);
	void setPaymentWizardView(
			Class<? extends GMVPView> viewClass, PaymentProcessCpuPageDTO rodDTO);
	void setAddAditionalDocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);
	
	void setPAAddAditionalDocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);
	
	/*void setSearchOrUploadDocumentsWizard(
			Class<? extends GMVPView> viewClass, UploadDocumentDTO uploadDto);*/
	
	void setSearchOrUploadDocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);
	
	void setSearchOrUploadDocumentsForAckNotReceivedWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);
	
	public void setDecideOnProcessDraftQueryWizardView(
			Class<? extends GMVPView> viewClass, ClaimQueryDto claimQueryDto);
	
//	public void setDecideOnProcessDraftQueryDetailWizardView(Class<? extends GMVPView> viewClass,
//			SearchDraftQueryLetterTableDTO  searchDraftQueryLetterTableDTO);
	
	void setInvestigationInitiate(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchProcessInvestigationInitiatedTableDTO tableDto);
	
	void setUploadInvestigationReports(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchUploadInvesticationTableDTO investigationKey);
	
	void setAcknowledgeInvestigationCompleted(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchAckInvestigationCompletedTableDTO investigaitonKey);
	
	void setInvestigationMasterWizard(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, InvestigationMasterDTO investigationMasterDTO);
	
	void setAssignInvestigation(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, AssignInvestigatorDto assignInvestigatorDto);

	void setReAssignInvestigation(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, AssignInvestigatorDto assignInvestigatorDto);
	
	void setCreateRODWizardView(Class<? extends GMVPView> viewClass,
			ReceiptOfDocumentsDTO rodDTO);
	
	void setPACreateRODWizardView(Class<? extends GMVPView> viewClass,
			ReceiptOfDocumentsDTO rodDTO);


	void setMedicalApprovalZonalReview(
			Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO, Boolean secondaryParameter);
	
	
	public void setDraftQueryLetterDetailView(Class<? extends GMVPView> viewClass, SearchDraftQueryLetterTableDTO searchDraftQueryLetterTableDTO);
    
	public void setGenerateReminderLetterDetailView(Class<? extends GMVPView> viewClass, SearchDraftQueryLetterTableDTO searchDraftQueryLetterTableDTO);
	
	public void setViewGenerateBulkReminderLetter(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,Map<String,Object> parameter);
	
	public void setViewPrintBulkReminderLetter(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,Map<String,Object> parameter);

	public void setViewPrintBulkPaymentLetter(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree);
	
	public void setViewClaimsAuditReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree);
	
	public void setViewPAPrintBulkReminderLetter(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,Map<String,Object> parameter);
	
	public void setViewGenerateBulkPAReminderLetter(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,Map<String,Object> parameter);
	
	public void setDraftRejectionLetterDetailView(
			Class<? extends GMVPView> viewClass, SearchDraftRejectionLetterTableDTO searchDraftRejectionLetterTableDTO);
	
	public void setDraftPARejectionLetterDetailView(
			Class<? extends GMVPView> viewClass, SearchDraftRejectionLetterTableDTO searchDraftRejectionLetterTableDTO);
	void setBillEntryWizard(Class<? extends GMVPView> viewClass,ReceiptOfDocumentsDTO rodDTO);
	
	void setPABillEntryWizard(Class<? extends GMVPView> viewClass,ReceiptOfDocumentsDTO rodDTO);
	
	public void setDecideOnRejectionWizardView(
			Class<? extends GMVPView> viewClass, ClaimRejectionDto claimRejectionDto);
	
	public void setDecideOnPARejectionWizardView(
			Class<? extends GMVPView> viewClass, ClaimRejectionDto claimRejectionDto);
	
	void setProcessClaimRequest(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO);
	
	
	//----------------------------------------------R3----------------------------------------------
	public void setViewFieldVisit(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter);
	
	public void setPAViewFieldVisit(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter);
	
	public void setCloseClaimSearchBasedRODLevelView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,SearchCloseClaimTableDTORODLevel tableDTO);
	
	public void setViewInvestigationMaster(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, BeanItemContainer<SelectValue> selectValueForInvestigatorType, BeanItemContainer<SelectValue> selectValueForInvestigatorName,BeanItemContainer<SelectValue> selectValueForPrivateInvestigatorName);
	
	public void setViewAssignInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> claimTypeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> statusByInvestigationState);
	
	public void setViewReAssignInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> claimTypeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewProcessInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> claimTypeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewAcknowledgementInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> claimTypeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewCreateROD(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument);
	
	public void setPAViewCreateROD(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument);
	
	public void setViewEnterBillDetails(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> masBillClassificationValues);
	
	public void setPAViewEnterBillDetails(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewDefautQuery(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewDefaultPAQuery(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewProcessDefautQuery(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewProcessDefaultPAQuery(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setDecideOnProcessDraftPAQueryWizardView(
			Class<? extends GMVPView> viewClass, ClaimQueryDto claimQueryDto);
	
	public void setDecideOnDraftPAQueryDetailWizardView(Class<? extends GMVPView> viewClass,
			SearchDraftQueryLetterTableDTO  searchDraftQueryLetterTableDTO);
	
	public void setViewGenerateReminderLetter(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,Map<String,Object> parameter);
	
	public void setViewGeneratePAReminderLetter(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,Map<String,Object> parameter);
	
	public void setViewDefautRejectionLetter(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewProcessDefautRejection(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewDefaultPARejectionLetter(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewProcessDefaultPARejection(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewSubmitSpecialistAdvice(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter);
	
	public void setViewsProcessClaimRequestZonalReview(Class<? extends GMVPView> viewClass,
	boolean selectInNavigationTree,BeanItemContainer<SelectValue> intimationSource, 
	BeanItemContainer<SelectValue> hospitalType, BeanItemContainer<SelectValue> networkHospitalType, BeanItemContainer<SelectValue> selectValueContainerForType, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewsProcessClaimRequest(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> intimationSource, 
			BeanItemContainer<SelectValue> hospitalType, BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType, BeanItemContainer<SelectValue> selectValueContainerForType, 
			BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, 
			BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,
			BeanItemContainer<SelectValue> claimType, SearchProcessClaimRequestFormDTO dto,String screenName);
	
	public void setViewsPAProcessClaimRequest(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> intimationSource, 
			BeanItemContainer<SelectValue> hospitalType, BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType, BeanItemContainer<SelectValue> selectValueContainerForType, 
			BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, SearchProcessClaimRequestFormDTO dto,String screenName);
	
	public void setViewsProcessClaimBilling(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> cpucode, 
			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> selectValueContainerForType, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> claimType,BeanItemContainer<SelectValue> statusByStage);
	
	
	public void setViewsPAProcessClaimBilling(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> cpucode, 
			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> selectValueContainerForType, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> coverContainer);
	
	public void setViewsPAHospCoverProcessClaimBilling(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> cpucode, 
			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> selectValueContainerForType, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueContainerForBenefits);
	
//	public void setViewsPANonHospCoverProcessClaimBilling(Class<? extends GMVPView> viewClass,
//			boolean selectInNavigationTree,BeanItemContainer<SelectValue> cpucode, 
//			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> selectValueContainerForType, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	public void setViewsProcessClaimRequestBenefits(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> intimationSource, 
			BeanItemContainer<SelectValue> hospitalType, BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType);
	
	public void setViewsProcessClaimRequestBenefitsFinace(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> intimationSource, 
			BeanItemContainer<SelectValue> hospitalType, BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType);

	void setProcessClaimBilling(Class<? extends GMVPView> class1,
			PreauthDTO reimbursementDTO);
	
	void setPAProcessClaimBilling(Class<? extends GMVPView> class1,
			PreauthDTO reimbursementDTO);
	
	void setPAProcessClaimApproval(Class<? extends GMVPView> class1,
			PreauthDTO reimbursementDTO);
	
	void setPAHospCoverProcessClaimBilling(Class<? extends GMVPView> class1,
			PreauthDTO reimbursementDTO);
	

	void setProcessClaimRequestBenefitsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);

	void setProcessClaimFinancial(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO);
	
	void setPaNonHospProcessClaimFinancial(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO);
	

	void setUploadTranslatedDocuments(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree , boolean remburementFlag );

	void setFileUploadTranslatedView(Class<UploadTranslatedDocumentView> class1,
			SearchProcessTranslationTableDTO searchProcessTranslationTableDTOR3);

	void setSearchApproveClaim(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter);

	void setSearchProcessClaim(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter);
	
	void setOPRegisterClaim(Class<? extends GMVPView> viewClass,
			OutPatientDTO mainDTO);

	void setOPRegisterClaimView(Class<? extends GMVPView> viewClass,
			Policy policy);
	
	/*void setOPClaimForPolicyExpired(Class<? extends GMVPView> viewClass,
			OutPatientDTO mainDTO);
	
	void setOPExpiredPolicyClaimView(Class<? extends GMVPView> viewClass,
			Policy policy);*/

	void setProcessRRCRequestSearch(Class<SearchProcessRRCRequestView> viewClass,
            boolean selectInNavigationTree, BeanItemContainer<SelectValue>  cpu,BeanItemContainer<SelectValue> rrcRequestType);



	void setProcessRRCRequestWizard(
			Class<ProcessRRCRequestDataExtractionWizard> viewClass,
			RRCDTO rrcDTO);

	void setProcessClaimOP(Class<? extends GMVPView> viewClass,
			OutPatientDTO mainDTO);

	void setViewsProcessClaimClaimOP(
			Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> selectValueContainerForPIOCode);
	
	void setReviewRequestRRCSearch(Class<SearchReviewRRCRequestView> class1, boolean b,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueForRRCRequestType,
			BeanItemContainer<SelectValue> selectValueForEligibility);
	
	void setReviewRRCRequestWizard(
			Class<ReviewRRCRequestDataExtractionWizard> viewClass,
			RRCDTO rrcDTO);
	
	void setModifyRequestRRCSearch(Class<SearchModifyRRCRequestView> class1, boolean b,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueForRRCRequestType,
			BeanItemContainer<SelectValue> selectValueForEligibility);
	
	void setModifyRRCRequestWizard(
			Class<ModifyRRCRequestDataExtractionWizard> viewClass,
			RRCDTO rrcDTO);
            

	void setRRCRequestSearch(Class<SearchRRCRequestView> class1, boolean b,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueForRRCRequestType,
			BeanItemContainer<SelectValue> selectValueForEligibility);
	
	void setRRCStatusScreen(Class<SearchRRCStatusView> class1, boolean b,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueForRRCRequestType,
			BeanItemContainer<SelectValue> selectValueForEligibility);
	
	void setCPUwisePreauthSearch(
			Class<CPUwisePreauthReportView> viewClass,
			boolean b, BeanItemContainer<SelectValue> cPUCodeContainer);
	
	void setEmployeeTypeExecutiveStatusSearch(Class<ExecutiveStatusDetailReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> empTypeContainer, BeanItemContainer<SelectValue> cPUCodeContainer,BeanItemContainer<SelectValue> empContainer);
	
	void setExecutiveStatusSummaryView(Class<ExecutiveStatusSummaryReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> empCPUContainer, BeanItemContainer<SelectValue> empTypeContainer, BeanItemContainer<SelectValue> empContainer);

	void setClaimsDailyReportView(
			Class<ClaimsDailyReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> cPUCodeContainer,
			BeanItemContainer<SelectValue> clmTypeContainer);
	
	void setClaimsStatusReportView(Class<ClaimsStatusReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> cPUCodeContainer,BeanItemContainer<SelectValue> clmStatusContainer);

	void setAdminDashBoardView(Class<CallcenterDashBoardReportView> viewClass, boolean b,boolean auditView);
	
	void setMedicalAuditClaimStatusSearch(
			Class<MedicalAuditClaimStatusReportView> viewClass,
			boolean b, BeanItemContainer<SelectValue> statusContainer);
	
	void setMedicalAuditCashlessIssuanceReportSearch(
			Class<MedicalAuditCashlessIssuanceReportView> viewClass,
			boolean b, BeanItemContainer<SelectValue> statusContainer);
	
	void setViewFvrReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> reportType,
			BeanItemContainer<SelectValue> claimType,BeanItemContainer<SelectValue> fvrCpuCode);
	
	void setViewSearchOrUploadDocuments(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue>  selectValueContainerForType);
	
	
	void setViewSearchOrUploaddocumentsForAckNotReceived(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, BeanItemContainer<SelectValue> type);
	
	
	public void setViewDailyReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> reportType);
	
	public void setViewHospitalWiseReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> dateType) ;
	
	public void setViewMedicalMailReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> dateType) ;
	
	public void setViewHelpDeskStatusReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> claimType) ;
	
	public void setViewHospitalIntimationStatusReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) ;
	
	public void setViewCpuWisePerformanceReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) ;
	
	public void setViewAgentBrokerReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) ;	
	
	public void setViewPaymentProcessTransaction(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) ;
	
	void setViewPaymentProcessCpu(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> year,
			BeanItemContainer<SelectValue> cpuLotNo, BeanItemContainer<SelectValue> status,BeanItemContainer<SelectValue> branch);
	
	

	void setViewPaymentProcessCpuPage(Class<PaymentProcessCpuPageView> class1,
			boolean b, PaymentProcessCpuPageDTO pageDto);

	
	void setViewPaymentProcess(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> year,
			BeanItemContainer<SelectValue> cpuLotNo, BeanItemContainer<SelectValue> status,BeanItemContainer<SelectValue> branch);

	void setViewForAcknowledgementSearch(
			Class<SearchAcknowledgeHospitalCommunicationView> class1,
			BeanItemContainer<SelectValue> selectValueContainer, boolean b,
			boolean c);

	void setCloseClaimInProcessView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchCloseClaimTableDTORODLevel tableDTO);


	void setReopenClaimSearchBasedView(Class<ReOpenRodLevelClaimView> class1,
			boolean b, SearchReOpenClaimRodLevelTableDTO tableDTO);


	void setViewOpClaimReport(Class<OPClaimReportView> viewClass,
			boolean selectInNavigationTree);

	
	void setViewBillReceivedReport(Class<BillReceivedReportView> viewClass,
			boolean selectInNavigationTree);
	
	

	void setCancelAcknowledgementView(Class<CancelAcknowledgementWizardView> class1,
			ReceiptOfDocumentsDTO rodDTO);


	
	
	void setViewForSearchClaimFinancial(
			Class<SearchProcessClaimFinancialsView> class1, boolean b, BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> networkHospTypeParameter);
	
	void setViewForSearchClaimCommonBillingAndFinancial(
			Class<SearchProcessClaimCommonBillingAndFinancialsView> class1, boolean b, BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	void setViewForPASearchClaimFinancialNonHosp(
			Class<PASearchProcessClaimFinancialsNonHospView> class1, boolean b, BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	void setViewCreateAndSearchLot(
			Class<CreateAndSearchLotView> class1,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueContainerForType,
			BeanItemContainer<SelectValue> selectValueContainerForClaimant,
			BeanItemContainer<SelectValue> selectValueContainerForClaimType,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus,
			BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,
			BeanItemContainer<SelectValue> docVerified,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentMode,BeanItemContainer<SelectValue> selectValueContainerForVerificationType,boolean b);
	
	void setViewCreateBatchOp(
			Class<CreateBatchOpView> class1,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueContainerForType,
			BeanItemContainer<SelectValue> selectValueContainerForClaimant,
			BeanItemContainer<SelectValue> selectValueContainerForClaimType,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus,
			BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,
			BeanItemContainer<SelectValue> docVerified,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentMode,
			BeanItemContainer<SelectValue> selectValueContainerForPIOCode, boolean b);
	
	void setViewOPScreen(
			Class<OpView> class1,
			BeanItemContainer<SelectValue> selectValueContainerForZoneType,boolean b);

	void setViewCreateBatch(
			Class<SearchCreateBatchView> class1,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueContainerForType,
			BeanItemContainer<SelectValue> selectValueContainerForClaimant,
			BeanItemContainer<SelectValue> selectValueContainerForClaimType,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus,
			BeanItemContainer<SelectValue> selectValueContainerForNonKeralaCpu,
			BeanItemContainer<SelectValue> selectValueContainerForBatchType,
			BeanItemContainer<SelectValue> selectValueContainerForZoneType,	
			BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,
			BeanItemContainer<SelectValue> selectValueContainerForZoneType2,
			CreateAndSearchLotTableDTO createSearchLotTableDto,	
			BeanItemContainer<SelectValue> selectValueContainerForPenalInstDays,
			BeanItemContainer<SelectValue> selectValueContainerForVerificationType,
			boolean b,String presenterString);
//	Commented below Cashless Screen
//	void setSearchConvertReimbursement(
//			Class<SearchConvertReimbursementView> class1, boolean b,
//			BeanItemContainer<SelectValue> selectValueContainer);

	
	
	
	void showErrorPopUp(String eMsg);

	void setConvertReimbursementView(Class<? extends GMVPView> viewClass,
			ConvertClaimDTO convertClaimDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto,
			SearchConvertClaimTableDto searchFormDto);

//	void setClearCashlessScreen(Class<? extends GMVPView> viewClass,
//			SearchClearCashlessDTO tableDTO);

	void setCloseClaimPageView(Class<CloseClaimView> class1, boolean b,
			CloseClaimPageDTO closeClaimDto);

	void setReopenClaimPageView(Class<ReopenClaimView> viewClass, boolean b,
			ReopenClaimPageDTO tableDTO);
	
	void setClaimWiseAllowApprovalPageView(Class<ClaimWiseAllowApprovalView> viewClass,boolean b,
			SearchClaimWiseAllowApprovalDto tableDto);
	
	void setPedInitiateOutSideView(Class<InitiatePedView> class1,
			PEDQueryDTO pedQueryDto);


	void setConvertClaimCashlessView(Class<ConvertClaimCashlessPageView> class1,
			ConvertClaimDTO convertClaimDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto,
			SearchConverClaimCashlessTableDTO searchFormDto);

	
	void setInitiateRequestRRCSearch(
			Class<InitiateRRCRequestView> viewClass);
	
	void setInitiateRequestTALKTALKTALKSearch(
			Class<InitiateTalkTalkTalkView> viewClass);
	
	void setinitiateRRCRequestWizard(Class<InitiateRRCRequestWizardView> viewClass,ReceiptOfDocumentsDTO rodDTO);
	void setinitiateTalkTalkTalkWizard(Class<InitiateTalkTalkTalkWizardView> viewClass,ReceiptOfDocumentsDTO rodDTO);

	void setSettlementPullBackScreen(Class<SettlementPullBackView> class1,
			SearchSettlementPullBackDTO tableDTO);
	

	public void setViewPaymentBatchReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> dateType) ;


	void setViewTATReport(
			Class<TATReportView> viewClass,BeanItemContainer<SelectValue> selectValueContainerForCPUCode,BeanItemContainer<SelectValue> selectValueContainerForOfficeCode,boolean b);
	
	void setProductAndDocumentTypeView(
			Class<ProductAndDocumentTypeView> viewClass, boolean b,
			ProductAndDocumentTypeDTO doctortableDTO, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> userTypeContainer);
	
	void setUserMgmtView(Class<UserManagementView> viewClass, boolean b,
			UserManagementDTO userManagementDTO,BeanItemContainer<SelectValue> userTypeContainer);
	
	void setUserActivationView(Class<UserACtivationView> viewClass, boolean b,
			UserManagementDTO userManagementDTO,BeanItemContainer<SelectValue> userTypeContainer,BeanItemContainer<SelectValue>  documentTypeContainer,BeanItemContainer<SelectValue> claimFlagTypeContainer);
	

	void setSkipZMR(Class<? extends GMVPView> viewClass, BeanItemContainer<SelectValue> cpuCodeContainer);

	void setViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, String strViewName);
	void setViewProvisionReport(Class<SearchShowdowView> class1, boolean b);
	
	void setAutoClosureView(Class<AutoClosureBatchView> class1, boolean b);
	
	
	void setViewsProcessConsumerForum(Class<? extends GMVPView> viewClass, BeanItemContainer<SelectValue> repudiationMasterValueByCode, BeanItemContainer<SelectValue> tmpZoneList, BeanItemContainer<SelectValue> tmpStateList, BeanItemContainer<SelectValue> orderMasterValueByCode, BeanItemContainer<SelectValue> order1MasterValueByCode, BeanItemContainer<SelectValue> awardReasonMasterValueByCode, BeanItemContainer<SelectValue> depAmtMasterValueByCode, BeanItemContainer<SelectValue> caseUpdateMasterValueByCode, BeanItemContainer<SelectValue> recievedFrom, BeanItemContainer<SelectValue> movedTO, BeanItemContainer<SelectValue> statusCase);
	
	void setViewsProcessAdvocateNotice(Class<? extends GMVPView> viewClass, BeanItemContainer<SelectValue> moveToMasterValueByCode, BeanItemContainer<SelectValue> pendingMasterValueByCode, BeanItemContainer<SelectValue> pendingMasterValueByCode2, BeanItemContainer<SelectValue> recievedFrom);
	
	void setViewsProcessAdvocateFee(Class<? extends GMVPView> viewClass, BeanItemContainer<SelectValue> repudiationMasterValueByCode, BeanItemContainer<SelectValue> tmpZoneList);
	
	void setViewsProcessOmbudsman(Class<? extends GMVPView> viewClass, 
			BeanItemContainer<SelectValue> repudiationMasterValueByCode,BeanItemContainer<SelectValue> statusoftheCase,
			BeanItemContainer<SelectValue> ombudsmanDetailsByDesc,
			BeanItemContainer<SelectValue> addDays,
			BeanItemContainer<SelectValue> pendingLevel,
			BeanItemContainer<SelectValue> hearingStatus,
			BeanItemContainer<SelectValue> awardStatus,
			BeanItemContainer<SelectValue> compromiseStatus,
			BeanItemContainer<SelectValue> decision, BeanItemContainer<SelectValue> recievedFrom, BeanItemContainer<SelectValue> movedTO,BeanItemContainer<SelectValue> grievanceOutcome);
	
	void setViewsLegalHome(Class<? extends GMVPView> viewClass);


	public void setViewInvestigationGrading(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> claimTypeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	void setReallocationUserView(
			Class<SearchReallocationDoctorDetailsView> viewClass, boolean b, SearchReallocationDoctorDetailsTableDTO doctorDetailsDTO);
	
	void setViewInvestigationGrading(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchProcessInvestigationInitiatedTableDTO tableDto);
	void setViewInvestigationGradingDetail(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, AssignInvestigatorDto assignInvestigatorDto);

	public void setViewDraftInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> claimTypeContainer, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	//void setProcess64Wizard(Class<? extends GMVPView> viewClass, SearchPreauthTableDTO tableDTO);

	void setProcess64Wizard(Class<? extends GMVPView> viewClass,
			SearchPreauthTableDTO preauthDTO);



	void setViewPAackDocReceive(Class<? extends GMVPView> viewClass,
	            boolean selectInNavigationTree,BeanItemContainer<SelectValue>  selectValueContainerForPAClaimType,BeanItemContainer<SelectValue> selectValueContainerForPADocumentType);

	void setPAProcessClaimRequest(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO);

	public void setViewsPAProcessClaimAprNonHos(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> cpucode, 
			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> selectValueContainerForType, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> coverContainer);

	public void setViewsPAProcessClaimAprHos(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> cpucode, 
			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> selectValueContainerForType, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);

	public void setPARejectionView(Class<? extends GMVPView> viewClass, SearchProcessRejectionTableDTO searchProcessRejectionTableDTO);
	
	public void setDecideOnPACoveringLewtterWizardView(Class<? extends GMVPView> viewClass,
			GenerateCoveringLetterSearchTableDto searchPaCoveringLetterTableDto);

	void setViewGForPA(
			Class<? extends GMVPView> viewClass,
			boolean b,
			BeanItemContainer<SelectValue> selectValueContainerForIntimationSource,
			BeanItemContainer<SelectValue> selectValueContainerForNetworkHospType,
			BeanItemContainer<SelectValue> selectValueContainerForTreatmentType,
			BeanItemContainer<SelectValue> selectValueContainerForType,
			BeanItemContainer<SelectValue> specialityContainer,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			String processPreauth);

	void setPreMedicalWizardViewForPA(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDTO, Boolean secondaryParameter);

	void setPreauthWizardViewForPA(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDTO, Boolean secondaryParameter);

	void setPreMedicalEnhancementViewForPA(
			Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO);

	void setPreauthEnhancementWizardViewForPA(
			Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO);

	public void setConvertPAClaimView(Class<? extends GMVPView>viewClass,ConvertClaimDTO convertClaimDto,BeanItemContainer<SelectValue> selectValueContainer, SearchConvertClaimTableDto searchFormDto);

	public void setSearchConvertPAClaimViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, Map<String, Object> parameter);
	
	public void setViewsPAHealthProcessClaimRequest(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> intimationSource, 
			BeanItemContainer<SelectValue> hospitalType, BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType, BeanItemContainer<SelectValue> selectValueContainerForType,
			BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, PAHealthSearchProcessClaimRequestFormDTO dto,String screenName);
	
	void setPAHealthProcessClaimRequest(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO);
	
	void setViewForPAHealthSearchClaimFinancial(
			Class<PAHealthSearchProcessClaimFinancialsView> class1, boolean b, BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	void setPAHealthProcessClaimFinancial(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO);
	
	public void setViewsPAHealthProcessClaimBilling(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> cpucode, 
			BeanItemContainer<SelectValue> productNameCode, BeanItemContainer<SelectValue> selectValueContainerForType, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	void setPAHealthProcessClaimBilling(Class<? extends GMVPView> class1,
			PreauthDTO reimbursementDTO);
	
	void setViewForPAHealthSearchClaimFinancialNonHosp(
			Class<PASearchProcessClaimFinancialsNonHospView> class1, boolean b, BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);

	void setPAProcessFinancialNonHosp(Class<? extends GMVPView> class1,
			PreauthDTO reimbursementDTO);
	

	void setViewPASearchOrUploadDocuments(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue>  selectValueContainerForType);
	
	void setPASearchOrUploadDocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);

	void setPAReopenClaimPageView(Class<PAReopenClaimView> viewClass, boolean b,
			PAReopenClaimPageDTO tableDTO);
	
	void setPAHealthReopenClaimPageView(Class<PAHealthReopenClaimView> viewClass, boolean b,
			PAHealthReopenClaimPageDTO tableDTO);
	
	void setPAReopenClaimSearchBasedView(Class<PAReOpenRodLevelClaimView> class1,
			boolean b, PASearchReOpenClaimRodLevelTableDTO tableDTO);
	
	void setPAHealthReopenClaimSearchBasedView(Class<PAHealthReOpenRodLevelClaimView> class1,
			boolean b, PAHealthSearchReOpenClaimRodLevelTableDTO tableDTO);
	
	void setPASettlementPullBackScreen(Class<PASettlementPullBackView> class1,
			PASearchSettlementPullBackDTO tableDTO);
	
	void setPAHospSettlementPullBackScreen(Class<PAHospSettlementPullBackView> class1,
			PAHospSearchSettlementPullBackDTO tableDTO);
	
	public void setPACloseClaimSearchBasedRODLevelView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,PASearchCloseClaimTableDTORODLevel tableDTO);
	
	void setPACloseClaimPageView(Class<PACloseClaimView> class1, boolean b,
			PACloseClaimPageDTO closeClaimDto);
	
	void setPACloseClaimInProcessView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, PASearchCloseClaimTableDTORODLevel tableDTO);

	void setViewPASearchOrUploaddocumentsForAckNotReceived(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, BeanItemContainer<SelectValue> type);
	
	void setPASearchOrUploadDocumentsForAckNotReceivedWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);

//	Commented the below Cashless Screen	
//	void setPASearchConvertReimbursement(
//			Class<PASearchConvertReimbursementView> class1, boolean b,
//			BeanItemContainer<SelectValue> selectValueContainer);
	
	void setPAConvertReimbursementView(Class<? extends GMVPView> viewClass,
			ConvertClaimDTO convertClaimDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto,
			SearchConvertClaimTableDto searchFormDto);
	
	void setPAViewG(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree, BeanItemContainer<SelectValue> parameter);
	
	void setDraftInvestigation(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, DraftInvestigatorDto draftInvestigatorDto);

	void setViewUploadInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> claimTypeContainer);
	

	void setFileStorageSearch(Class<SearchDataEntryView> class1, boolean b);
	
	void setProcessDataEntryWizard(
			Class<ProcessDataEntryWizardViewImpl> viewClass,
			SearchDataEntryTableDTO tableDTO);

	void setViewFileDetailsReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree);
	public void setViewProductivityReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree);
	
	public void setViewAutomationDashboard(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree);
	
	public void setViewPaymentPendingDashboard(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree);

	void setViewPanCard(Class<SearchUploadPanCardView> class1, boolean selectInNavigationTree);
	
	void setUpdatePancardDetails(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchUploadPanCardTableDTO investigationKey);
	
	void setReceivedPhysicaldocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);

	void setGmcDailyReportView(Class<GmcDailyReportView> class1, boolean b,
			BeanItemContainer<SelectValue> cpuContainer,
			BeanItemContainer<SelectValue> clmTypeContainer);
	
	public void setViewGPAUnamedDetailsView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) ;
	
	void setViewUnnamedRiskDetailsPage(Class<UnnamedRiskDetailsPageView> class1,
			boolean b, UnnamedRiskDetailsPageDTO pageDto,BeanItemContainer<SelectValue> category);

	void setViewUpdatePaymentDetail(
			Class<UpdatePaymentDetailView> class1,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueContainerForType,
			BeanItemContainer<SelectValue> selectValueContainerForClaimant,
			BeanItemContainer<SelectValue> selectValueContainerForClaimType,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus,
			BeanItemContainer<SelectValue> selectValueContainerForNonKeralaCPU,
			BeanItemContainer<SelectValue> selectValueContainerForBatchType,
			BeanItemContainer<SelectValue> selectValueContainerForZoneType,
			BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,
			UpdatePaymentDetailTableDTO createSearchLotDto,
			boolean selectInNavigationTree);
	
	void setFVRGradingViewG(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree, BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> parameter1);
	
	public void setFvrReportGradingPageView(Class<? extends GMVPView>viewClass,FvrReportGradingPageDto convertClaimDto, NewIntimationDto intimationDto, SearchFvrReportGradingTableDto searchFormDto);
	
	void setLotPullBackScreen(Class<LotPullBackPageView> class1,
			SearchLotPullBackTableDTO tableDTO);
	
	void setLumenStatusReportView(Class<? extends GMVPView> viewClass, BeanItemContainer<SelectValue> cpuCodeContainer, BeanItemContainer<SelectValue> lumenStatusContainer, BeanItemContainer<SelectValue> clmTypeContainer);
	void setAckWithoutRodReportView(Class<? extends GMVPView> viewClass, BeanItemContainer<SelectValue> cpuCodeContainer, BeanItemContainer<SelectValue> docFromContainer);

	public void setInvAssignStatusReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> cpuCodeContainer, BeanItemContainer<SelectValue> invStatusContainer, BeanItemContainer<SelectValue> clmTypeContainer);
	
	
	public void setOmpOutStandingReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> classificationContainer, WeakHashMap<Long,BeanItemContainer<SelectValue>> subClassificationMap);
	
	public void setOmpStatusWiseReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> classificationContainer, WeakHashMap<Long,BeanItemContainer<SelectValue>> subClassificationMap,
    		BeanItemContainer<SelectValue> statusContainer, BeanItemContainer<SelectValue> lossContainer,
    		BeanItemContainer<SelectValue> yearContainer);

	void setCreateIntimationWithParameter(Class<SearchPolicyView> class1,
			String policyNumber, String healthCardNumber);
	
	public void setMedicalOpinionValidationView(Class<OpinionValidationView> class1, BeanItemContainer<SelectValue> selectValueContainerForCPUCode);
	
	public void setMedicalOpinionValidationReportView(Class<OpinionValidationReportView> class1, 
			BeanItemContainer<SelectValue> roleContainer, 
			BeanItemContainer<SelectValue> empNameContainer);

	public void setAllocateCorpBufferView(Class<? extends GMVPView> viewClass);

	public void setAllocateCorporateBufferWizardView(Class<? extends GMVPView> viewClass, AllocateCorpBufferDetailDTO allocateCorpBufferDetailDTO);

	void setProcessRAWRequestSearch(Class<SearchProcessRawRequestView> viewClass,
            boolean selectInNavigationTree, BeanItemContainer<SelectValue>  cpu,BeanItemContainer<SelectValue> requestType,BeanItemContainer<SelectValue> statusByStage);
	void setProcessRAWRequesttWizard(
			Class<ProcessRawRequestWizard> viewClass,
			SearchProcessRawRequestTableDto tableDTO);
	void setViewsHoldMoniterScreen(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode,String screenName);
	
	public void setUpdateRodDetailsView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument);
	void setUpdateAadhar(Class<SearchUpdateAadharView> class1, boolean selectInNavigationTree);
	
	public void setUpdateSublimitView(Class<? extends GMVPView> viewClass,PreauthDTO reimbursementDTO);
	
	
	void setUpdateAadharDetailsWizard(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchUpdateAadharTableDTO tableDTO);
	
	public void setFinApprovedNotSettledReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> cpuCodeContainer);
	
	public void setbranchManagerFeedback(Class<BranchManagerFeedbackView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> feedBack,BeanItemContainer<SelectValue> zoneValue, BeanItemContainer<SelectValue> branchValue,
			BeanItemContainer<SelectValue> feedbackStatusValue, BeanItemContainer<SelectValue> feedbackTypeValue);
	void setViewTopUpPolicy(Class<TopUpPolicyMasterView> class1);
	
	void setViewFraudIdentification(Class<FraudIdentificationView> class1);


	//cancel status -AUTO ALLOCATION:
	void setViewAutoAllocCancelStatusReport(Class<AutoAllocationCancelReportView> viewClass, boolean b);
	void setViewInvestigationDirectAssigement(
			Class<InvestigationDirectAssignmentView> class1);
	
	
	//For Upload NEFT Details
	void setUploadNEFTDetailsWizard(Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO);
	
	//R1069
	void setAddAditionalpaymentWizard(Class<? extends GMVPView> viewClass,
			ReceiptOfDocumentsDTO rodDTO);
	
	//R1069
	void setPAAddAditionalpaymentWizard(Class<? extends GMVPView> viewClass,
				ReceiptOfDocumentsDTO rodDTO);
	

//	public void setSearchProcessNegotiationView(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,
//			BeanItemContainer<SelectValue> cpuCode);
	
	void setNegotiationPreauthRequest(Class<? extends GMVPView> viewClass, PreauthDTO preauthDto, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> escalateContainer);

	void setManagerFeedBack(Class<ManagerFeedBackView> class1, boolean b,BeanItemContainer<SelectValue> branchName);

	//feedbackPhaseII_1238
	public void setBranchManagerFeedBackHomePage(Class<ManagerFeedBackView> class1, boolean b, BeanItemContainer<SelectValue> branchName, BranchManagerFeedbackhomePageDto homePageDto);
	
	//CR R1238 BRMFB Report
	public void setBranchManagerFeedbackReportView(Class<? extends GMVPView> viewClass,
	    		BeanItemContainer<SelectValue> feedbackContainer, BeanItemContainer<SelectValue> zoneContainer, BeanItemContainer<SelectValue> periodContainer/*, BeanItemContainer<SelectValue> branchContainer*/);
		
	public void setBranchManagerFeedbackReportingPatternView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> feedbackContainer, BeanItemContainer<SelectValue> zoneContainer, BeanItemContainer<SelectValue> periodContainer);

	void setViewPreviousManagerFeedBack(
			Class<BranchManagerPreviousFeedbackView> class1);
	
	void setViewPreviousManagerLink(Class<BranchManagerPreviousFeedbackView> class1,FeedbackStatsDto fbStatusDTO,Long fbStatus);
	
	void setViewNegotiationReport(Class<NegotiationReportView> class1,Boolean b,BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> user);
	
	void setViewFraudAnalysisReport(Class<FraudReportView> class1);
	
	void setMagazineDetails(List<MasMagazineDocument> magazineList);
//	Commented the below Cashless Screen
//	void setViewUncheckNegotiation(
//			Class<SearchUncheckNegotiationView> class1);
	void setUploadBedPhoto(Class<SearchBedPhoto> class1, boolean selectInNavigationTree);

	void setUploadBedPhotoWizard(Class<UploadBedPhotoView> class1, boolean b,
			SearchBedPhotoTableDTO tableDTO);
	
	void setGmcAutomailer(
			Class<GmcAutomailerView> class1);

	void setViewCVCAudit(Class<SearchCVCView> class1, boolean b);
	
	void setCVCWizardView(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchCVCTableDTO investigationKey);
	
	void showInfoPopUp(String eMsg);

//	void setHospitalScoringView(Class<HospitalScoringDetailsView> class1);
	void setCVCAuditActionWizardView(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchCVCAuditActionTableDTO investigationKey);
	
	void setViewCVCAuditActionSearch(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree);
	
	void setHRMPSearch(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree);
	
	void setCVCAuditQryApprovalWizardView(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchCVCAuditActionTableDTO investigationKey);
	
	void setViewCVCAuditQryApprovalSearch(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree);

//	void setViewCVCAuditClsQryRlySearch(Class<? extends GMVPView> viewClass,
//			boolean selectInNavigationTree, SearchCVCAuditClsQryFormDTO srchFrmDto);
	void setViewCVCAuditRmbQryRlySearch(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchCVCAuditClsQryFormDTO srchFrmDto);
	void setViewCVCAuditFaQryRlySearch(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchCVCAuditClsQryFormDTO srchFrmDto);
//	void setViewCVCAuditClsQrySearch(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchCVCAuditActionTableDTO investigationKey);
	void setViewCVCAuditFaQrySearch(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchCVCAuditActionTableDTO investigationKey);
	void setViewCVCAuditRmbQrySearch(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchCVCAuditActionTableDTO investigationKey);

	void setViewClaimsAlert(Class<ClaimsAlertMasterView> viewclass);

	//added for OP settlement CR
	void setViewSettlementProcessClaimClaimOP(
			Class<SearchSettlementLetterProcessOPClaimRequestView> class1,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueContainerForType,
			BeanItemContainer<SelectValue> selectValueContainerForClaimant,
			BeanItemContainer<SelectValue> selectValueContainerForClaimType,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus,
			BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,
			BeanItemContainer<SelectValue> docVerified,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentMode,
			BeanItemContainer<SelectValue> selectValueContainerForPIOCode,boolean b);
	
//	void setViewFLPAutoAllocation(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree);

	void setViewHoldMonitorFLPScreen(Class<? extends GMVPView> viewClass, boolean b, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> userList,
			BeanItemContainer<SelectValue> cpuCode);
	
	void setViewMonitoringReport(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree);
	
	void setViewProcessDataCorrection(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,ProcessDataCorrectionDTO correctionDTO); 
	
	void setReconsiderationFlagReqWizardView(Class<? extends GMVPView> viewClass, SearchFlagReconsiderationReqTableDTO tableDTO);

	void setViewProcessDataCorrectionHistorical(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,ProcessDataCorrectionDTO correctionDTO); 
	
	void setViewProcessICACRequest(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,SearchProcessICACTableDTO correctionDTO); 

	void showCashLessLoginSuccess();

	void setProcessPCCCoOrdinatorRequestWizard(Class<? extends GMVPView> viewClass,PccDetailsTableDTO pccDetailsTableDTO);
		
	void setProcessPCCProcessorRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO pccDetailsTableDTO);
	
	void setProcessPCCReviewerRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO pccDetailsTableDTO);
		
	void setProcessPCCDivisionHeadRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO pccDetailsTableDTO);
		
	void setProcessPCCZonalMedicalHeadRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO tableDTO);
	
	void setProcessPCCZonalCoordinatorRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO tableDTO);
	
	void setProcessPCCHrmCoordinatorRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO tableDTO);

	void setMarketingEscalationReportView(Class<MarketingEscalationReportView> class1, boolean b);
	
	void setrecordMarkEscView(Class<RecMarketingEscalationView> class1,RecordMarkEscDTO recMarEscDto);
	
	void setHRMPWizardView(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchHRMPTableDTO hrmDto);
	void setStopPyamentValidationScreen(Class<PopupStopPaymentValidateWizard> class1,
			StopPaymentRequestDto tableDTO);

	void setStopPyamentRequestScreen(Class<PopupStopPaymentRequestWizard> viewClass,
			StopPaymentRequestDto tableDTO);
	
	void setProcessPCCReassignHrmCoordinatorRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO tableDTO);
	
	void setViewForSearchClaimCommonBillingAndFinancialAutoAllocation(
			Class<SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView> class1, boolean b, BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	void setViewGetTask(Class<? extends GMVPView> viewClass,
	           BeanItemContainer<SelectValue> parameter, boolean selectInNavigationTree, boolean isReimburement, BeanItemContainer<SelectValue> cpuCodeContainer,SubmitSpecialistFormDTO dto);

	void setViewForSearchProcessClaimRequestAutoAllocation(
			Class<SearchProcessClaimRequestAutoAllocationView> class1, boolean b);
	
	void setViewsHoldMoniterScreenForMA(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode,String screenName);

	void setPhysicalVerification(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,String ScreenName);
	
	void showReportsLoginSuccess();

	void setViewGGetTask(Class<? extends GMVPView> viewClass,
            boolean selectInNavigationTree,SearchPEDRequestApproveFormDTO dto);
	
	public void setViewCreateOnlineROD(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument);

	void setCreateOnlineRODWizardView(Class<? extends GMVPView> viewClass,ReceiptOfDocumentsDTO rodDTO);
	
	void setViewProcessDataCorrectionPriority(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,ProcessDataCorrectionDTO correctionDTO); 
	void setViewForSearchFinancialApprovalAutoAllocation(
			Class<SearchProcessFinancialApprovalAutoAllocationView> class1, boolean b, BeanItemContainer<SelectValue> claimType, BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> type, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage);
	
	void setViewsHoldMoniterScreenForFA(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode,String screenName);

	void setViewForSearchClaimBillingAutoAllocation(Class<SearchProcessClaimBillingAutoAllocationView> viewClass, boolean b);
	
	public void setUpdateDispatchReportView(Class<DispatchDetailsReportView> class1, boolean b);
	
	void setPostProcessCVCSearch(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree);
	void setPostProcessCVCWizardView(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchCVCTableDTO investigationKey);
}
