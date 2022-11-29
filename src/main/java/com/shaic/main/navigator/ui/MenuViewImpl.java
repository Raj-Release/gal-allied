package com.shaic.main.navigator.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken.Access;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.MVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.annotation.HorizontalSplitPanelProperties;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.GMVPView;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.view.LoaderView;
import com.shaic.branchmanagerfeedback.BranchManagerFeedbackView;
import com.shaic.branchmanagerfeedback.BranchManagerFeedbackViewImpl;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.OMPBulkUploadRejection.OMPBulkUploadRejectionViewImpl;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPProcessNegotiationWizardView;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPProcessNegotiationWizardViewImpl;
import com.shaic.claim.OMPProcessNegotiation.search.OMPProcessNegotiationView;
import com.shaic.claim.OMPProcessNegotiation.search.OMPProcessNegotiationViewImpl;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPAcknowledgementDocumentsPageWizard;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPAcknowledgementDocumentsWizardViewImpl;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPProcessOmpAcknowledgementDocumentsView;
import com.shaic.claim.OMPProcessOmpClaimApprover.pages.OMPProcessOmpClaimApproverWizardView;
import com.shaic.claim.OMPProcessOmpClaimApprover.pages.OMPProcessOmpClaimApproverWizardViewImpl;
import com.shaic.claim.OMPProcessOmpClaimApprover.search.OMPProcessOmpClaimApproverViewImpl;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorWizardViewImpl;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPProcessOmpClaimProcessorPageWizard;
import com.shaic.claim.OMPProcessOmpClaimProcessor.search.OMPProcessOmpClaimProcessorViewImpl;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPCreateIntimationWizardViewImpl;
import com.shaic.claim.OMPprocessrejection.detailPage.OMPProcessRejectionView;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryPageWizard;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPRODBillEntryWizardViewImpl;
import com.shaic.claim.aadhar.pages.UpdateAadharDetailsViewImpl;
import com.shaic.claim.aadhar.search.SearchUpdateAadharTableDTO;
import com.shaic.claim.aadhar.search.SearchUpdateAadharView;
import com.shaic.claim.aadhar.search.SearchUpdateAadharViewImpl;
import com.shaic.claim.adviseonped.AdviseOnPEDView;
import com.shaic.claim.adviseonped.AdviseOnPEDViewImpl;
import com.shaic.claim.adviseonped.search.SearchAdviseOnPEDTableDTO;
import com.shaic.claim.allowghiregistration.GhiAllowClaimRegistrationView;
import com.shaic.claim.allowghiregistration.SearchGhiAllowClaimRegisterViewImpl;
import com.shaic.claim.bedphoto.SearchBedPhoto;
import com.shaic.claim.bedphoto.SearchBedPhotoTableDTO;
import com.shaic.claim.bedphoto.SearchBedPhotoViewImpl;
import com.shaic.claim.bedphoto.UploadBedPhotoView;
import com.shaic.claim.bedphoto.UploadBedPhotoViewImpl;
import com.shaic.claim.bulkconvertreimb.search.SearchBatchConvertedTableDto;
import com.shaic.claim.bulkconvertreimb.search.SearchBulkConvertReimbViewImpl;
import com.shaic.claim.cashlessprocess.downsize.wizard.DownSizePreauthWizardViewImpl;
import com.shaic.claim.cashlessprocess.downsizeRequest.page.DownsizePreauthRequestWizardViewImpl;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search.SearchFlagReconsiderationReqTableDTO;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.showwizard.ReconsiderationFlagRequestViewImpl;
import com.shaic.claim.cashlessprocess.processicac.search.ProcessICACRequestViewImpl;
import com.shaic.claim.cashlessprocess.processicac.search.SearchProcessICACTableDTO;
import com.shaic.claim.cashlessprocess.withdrawpreauth.SearchWithdrawCashLessProcessTableDTO;
import com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess.SearchWithdrawCashLessPostProcessTableDTO;
import com.shaic.claim.corpbuffer.allocation.search.AllocateCorpBufferViewImpl;
import com.shaic.claim.corpbuffer.allocation.wizard.AllocateCorpBufferDetailDTO;
import com.shaic.claim.corpbuffer.allocation.wizard.AllocateCorpBufferWizardViewImpl;
import com.shaic.claim.cpuskipzmr.SkipZMRViewImpl;
import com.shaic.claim.cvc.CVCPageViewImpl;
import com.shaic.claim.cvc.SearchCVCTableDTO;
import com.shaic.claim.cvc.SearchCVCView;
import com.shaic.claim.cvc.SearchCVCViewImpl;
import com.shaic.claim.cvc.auditaction.CVCAuditActionPageViewImpl;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionViewImpl;
import com.shaic.claim.cvc.auditqueryapproval.CVCAuditQryApprovalPageViewImpl;
import com.shaic.claim.cvc.auditqueryapproval.SearchCVCAuditQryApprovalViewImpl;
//import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.CVCAuditClsQryPageViewImpl;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryFormDTO;
//import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryViewImpl;
import com.shaic.claim.cvc.auditqueryreplyprocessing.fa.CVCAuditFaQryPageViewImpl;
import com.shaic.claim.cvc.auditqueryreplyprocessing.fa.SearchCVCAuditFaQryViewImpl;
import com.shaic.claim.cvc.auditqueryreplyprocessing.reimb.CVCAuditReimbQryPageViewImpl;
import com.shaic.claim.cvc.auditqueryreplyprocessing.reimb.SearchCVCAuditReimbQryViewImpl;
import com.shaic.claim.cvc.auditreport.ClaimsAuditReportViewImpl;
import com.shaic.claim.cvc.postprocess.PostProcessClaimCVCAuditViewImpl;
import com.shaic.claim.cvc.postprocess.SearchPostProcessCVCViewImpl;
import com.shaic.claim.doctorinternalnotes.InternalNotesPageViewImpl;
import com.shaic.claim.edithospitalInfo.search.SearchEditHospitalDetailsTableDTO;
//import com.shaic.claim.enhancement.search.SearchEnhancementViewImpl;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancementWizardViewImpl;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardViewImpl;
import com.shaic.claim.fieldVisitPage.FieldVisitPageViewImpl;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.claim.fileUpload.FileUploadViewImpl;
import com.shaic.claim.fss.filedetail.ProcessDataEntryWizardViewImpl;
import com.shaic.claim.fss.filedetailsreport.FileDetailsReportViewImpl;
import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;
import com.shaic.claim.fss.searchfile.SearchDataEntryView;
import com.shaic.claim.fvrgrading.page.FvrReportGradingPageDto;
import com.shaic.claim.fvrgrading.page.FvrReportGradingPageViewImpl;
import com.shaic.claim.fvrgrading.search.SearchFvrReportGradingTableDto;
import com.shaic.claim.fvrgrading.search.SearchFvrReportGradingViewImpl;
import com.shaic.claim.hospitalCommunication.HospitalViewImpl;
import com.shaic.claim.legal.home.SearchLegalHomeViewImpl;
import com.shaic.claim.legal.processconsumerforum.page.advocatefee.SearchProcessAdvocateFeeViewImpl;
import com.shaic.claim.legal.processconsumerforum.page.advocatenotice.SearchProcessAdvocateNoticeViewImpl;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.SearchProcessConsumerForumViewImpl;
import com.shaic.claim.legal.processconsumerforum.page.ombudsman.SearchProcessOmbudsmanViewImpl;
import com.shaic.claim.lumen.create.InitiateLumenPolicyRequestWizardImpl;
import com.shaic.claim.lumen.create.InitiateLumenRequestWizardImpl;
import com.shaic.claim.lumen.create.LumenPolicySearchResultTableDTO;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.initiatorquerycase.ProcessInitiatorWizardImpl;
import com.shaic.claim.lumen.querytomis.ProcessMISWizardImpl;
import com.shaic.claim.lumen.searchcoordinator.ProcessCoordinatorWizardImpl;
import com.shaic.claim.lumen.searchlevelone.ProcessLevelOneWizardImpl;
import com.shaic.claim.lumen.searchleveltwo.ProcessLevelTwoWizardImpl;
import com.shaic.claim.medical.opinion.OpinionValidationView;
import com.shaic.claim.medical.opinion.OpinionValidationViewImpl;
import com.shaic.claim.medical.opinion.RecMarketingEscalationView;
import com.shaic.claim.medical.opinion.RecMarketingEscalationViewImpl;
import com.shaic.claim.medical.opinion.RecordMarkEscDTO;
import com.shaic.claim.misc.updatesublimit.wizard.UpdateSublimitWizardViewImpl;
import com.shaic.claim.negotiation.ProcessNegotiationWizardViewImpl;
//import com.shaic.claim.negotiation.SearchProcessNegotiationViewImpl;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationTableDTO;
import com.shaic.claim.omp.newregistration.OMPNewRegistrationPageViewImpl;
import com.shaic.claim.omp.newregistration.OMPNewRegistrationSearchDTO;
import com.shaic.claim.omp.ratechange.OMPClaimRateChangeAndOsUpdationView;
import com.shaic.claim.omp.ratechange.OMPClaimRateChangeAndOsUpdationViewImpl;
import com.shaic.claim.omp.registration.OMPClaimRegistrationWizardView;
import com.shaic.claim.omp.registration.OMPClaimRegistrationWizardViewImpl;
import com.shaic.claim.omp.reports.outstandingreport.OmpOutstandingReportViewImpl;
import com.shaic.claim.omp.reports.statusreport.OmpStatusWiseReportViewImpl;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegisterViewImpl;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.omppaymentletterbulk.SearchPrintPaymentBulkViewImpl;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpView;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpViewImpl;
import com.shaic.claim.outpatient.processOP.wizard.ProcessOPClaimWizardViewImpl;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.outpatient.registerclaim.wizard.OPRegisterClaimWizardViewImpl;
import com.shaic.claim.pancard.page.UpdatePanCardReportViewImpl;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardTableDTO;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardView;
import com.shaic.claim.pancard.search.pages.SearchUploadPanCardViewImpl;
import com.shaic.claim.pcc.divisionHead.ProcessPCCDivisionHeadRequestWizardViewImpl;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.hrmCoordinator.ProcessPCCHrmCoordinatorRequestWizardViewImpl;
import com.shaic.claim.pcc.hrmp.HRMPWizardViewImpl;
import com.shaic.claim.pcc.hrmp.SearchHRMPTableDTO;
import com.shaic.claim.pcc.hrmp.SearchHRMPViewImpl;
import com.shaic.claim.pcc.processor.ProcessPCCProcessorRequestWizardViewImpl;
import com.shaic.claim.pcc.reassignHRM.ProcessPCCReassignHrmCoordinatorRequestWizardViewImpl;
import com.shaic.claim.pcc.reviewer.ProcessPCCReviewerRequestWizardViewImpl;
import com.shaic.claim.pcc.wizard.ProcessPccCoOrdinateRequestWizardViewImpl;
import com.shaic.claim.pcc.zonalCoordinator.ProcessPCCZonalCoordinatorRequestWizardViewImpl;
import com.shaic.claim.pcc.zonalMedicalHead.ProcessPCCZonalMedicalHeadRequestWizardViewImpl;
import com.shaic.claim.ped.outsideprocess.InitiatePedView;
import com.shaic.claim.ped.outsideprocess.InitiatePedViewImpl;
import com.shaic.claim.pedquery.PEDQueryDTO;
import com.shaic.claim.pedquery.PEDQueryView;
import com.shaic.claim.pedquery.PEDQueryViewImpl;
import com.shaic.claim.pedquery.search.SearchPEDQueryTableDTO;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApproveView;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApproveViewImpl;
import com.shaic.claim.pedrequest.approve.bancspedQuery.BancsSearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.approve.bancspedrequest.BancsPEDRequestDetailsApproveView;
import com.shaic.claim.pedrequest.approve.bancspedrequest.BancsPEDRequestDetailsApproveViewImpl;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveFormDTO;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveViewImpl;
import com.shaic.claim.pedrequest.process.PEDRequestDetailsProcessDTO;
import com.shaic.claim.pedrequest.process.PEDRequestDetailsProcessView;
import com.shaic.claim.pedrequest.process.PEDRequestDetailsProcessViewImpl;
import com.shaic.claim.pedrequest.process.search.SearchPEDRequestProcessTableDTO;
import com.shaic.claim.pedrequest.teamlead.PEDRequestDetailsTeamLeadView;
import com.shaic.claim.pedrequest.teamlead.PEDRequestDetailsTeamLeadViewImpl;
import com.shaic.claim.policy.search.ui.NewSearchPolicyViewImpl;
import com.shaic.claim.policy.search.ui.SearchPolicyView;
import com.shaic.claim.policy.search.ui.opsearch.SearchProcessOPClaimRequestViewImpl;
import com.shaic.claim.policy.search.ui.opsearch.SearchSettlementLetterProcessOPClaimRequestView;
import com.shaic.claim.policy.search.ui.opsearch.SearchSettlementLetterProcessOPClaimRequestViewImpl;
import com.shaic.claim.preauth.PreauthWizardViewImpl;
//import com.shaic.claim.preauth.negotiation.SearchUncheckNegotiationView;
//import com.shaic.claim.preauth.negotiation.SearchUncheckNegotiationViewImpl;
import com.shaic.claim.preauth.search.SearchPreauthViewImpl;
//import com.shaic.claim.preauth.search.autoallocation.SearchPreauthAutoAllocationViewImpl;
//import com.shaic.claim.preauth.search.flpautoallocation.SearchFLPAutoAllocationViewImpl;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
//import com.shaic.claim.premedical.search.ProcessPreMedicalViewImpl;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizarViewImpl;
//import com.shaic.claim.process.premedical.enhancement.search.SearchPreMedicalProcessingEnhancementViewImpl;
//import com.shaic.claim.process64VB.search.SearchProcessVBViewImpl;
import com.shaic.claim.process64VB.wizard.pages.Process64VBWizardViewImpl;
import com.shaic.claim.processRejectionPage.ProcessRejectionPageWizardViewImpl;
import com.shaic.claim.processdatacorrection.dto.ProcessDataCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionViewImpl;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalViewImpl;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityViewImpl;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationViewImpl;
import com.shaic.claim.reassignfieldVisitPage.ReAssignFieldVisitViewImpl;
import com.shaic.claim.reassignfieldvisit.search.SearchReAssignFieldVisitTableDTO;
import com.shaic.claim.registration.ClaimRegistrationView;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.claim.registration.GenerateLetterView;
import com.shaic.claim.registration.SearchClaimRegisterViewImpl;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationTableDTO;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationView;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationViewImpl;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimViewImpl;
import com.shaic.claim.registration.convertClaimPage.ConvertClaimPageViewImpl;
import com.shaic.claim.registration.convertClaimToReimbursement.convertReimbursementPage.ConvertReimbursementPageViewImpl;
import com.shaic.claim.registration.convertclaimcashless.SearchConverClaimCashlessTableDTO;
import com.shaic.claim.registration.convertclaimcashlesspage.ConvertClaimCashlessPageView;
import com.shaic.claim.registration.convertclaimcashlesspage.ConvertClaimcashlessPageViewImpl;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsViewImpl;
import com.shaic.claim.reimbursement.FinancialApprovalAutoAllocation.SearchProcessFinancialApprovalAutoAllocationView;
import com.shaic.claim.reimbursement.billing.benefits.wizard.pages.ProcessClaimRequestBenefitsWizardViewImpl;
import com.shaic.claim.reimbursement.billing.wizard.BillingWizardViewImpl;
import com.shaic.claim.reimbursement.commonBillingFA.SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView;
import com.shaic.claim.reimbursement.commonBillingFA.SearchProcessClaimCommonBillingAndFinancialsView;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotView;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotViewImpl;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardViewImpl;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardViewImpl;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard.MedicalApprovalZonalReviewWizardViewImpl;
import com.shaic.claim.reimbursement.opscreen.OpView;
import com.shaic.claim.reimbursement.opscreen.OpViewImpl;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestWizardViewImpl;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuViewImpl;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageView;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageViewImpl;
import com.shaic.claim.reimbursement.processClaimRequestAutoAllocation.SearchProcessClaimRequestAutoAllocationView;
import com.shaic.claim.reimbursement.processDraftRejectionLetterDetail.ClaimRejectionDto;
import com.shaic.claim.reimbursement.processDraftRejectionLetterDetail.DecideOnRejectionWizardViewImpl;
import com.shaic.claim.reimbursement.processdraftquery.ClaimQueryDto;
import com.shaic.claim.reimbursement.processdraftquery.DecideOnProcessDraftQueryWizardViewImpl;
import com.shaic.claim.reimbursement.rawanalysis.ProcessRawRequestWizard;
import com.shaic.claim.reimbursement.rawanalysis.SearchProcessRawRequestTableDto;
import com.shaic.claim.reimbursement.rawanalysis.SearchProcessRawRequestView;
import com.shaic.claim.reimbursement.rawanalysis.SearchProcessRawRequestViewImpl;
import com.shaic.claim.reimbursement.rrc.detailsPage.ModifyRRCRequestDataExtractionWizard;
import com.shaic.claim.reimbursement.rrc.detailsPage.ModifyRRCRequestWizardViewImpl;
import com.shaic.claim.reimbursement.rrc.detailsPage.ProcessRRCRequestDataExtractionWizard;
import com.shaic.claim.reimbursement.rrc.detailsPage.ProcessRRCRequestWizardViewImpl;
import com.shaic.claim.reimbursement.rrc.detailsPage.ReviewRRCRequestDataExtractionWizard;
import com.shaic.claim.reimbursement.rrc.detailsPage.ReviewRRCRequestWizardViewImpl;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestView;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestViewImpl;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestWizardView;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestWizardViewImpl;
import com.shaic.claim.reimbursement.rrc.services.SearchModifyRRCRequestViemImpl;
import com.shaic.claim.reimbursement.rrc.services.SearchModifyRRCRequestView;
import com.shaic.claim.reimbursement.rrc.services.SearchProcessRRCRequestView;
import com.shaic.claim.reimbursement.rrc.services.SearchProcessRRCRequestViewImpl;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCRequestView;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCRequestViewImpl;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCStatusView;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCStatusViewImpl;
import com.shaic.claim.reimbursement.rrc.services.SearchReviewRRCRequestView;
import com.shaic.claim.reimbursement.rrc.services.SearchReviewRRCRequestViewImpl;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsViewImpl;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsWizardViewImpl;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedViewImpl;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedWizardViewImpl;
import com.shaic.claim.reimbursement.submitSpecialist.SubmitSpecialistAdviseViewImpl;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkView;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkViewImpl;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkWizardView;
import com.shaic.claim.reimbursement.talktalktalk.InitiateTalkTalkTalkWizardViewImpl;
import com.shaic.claim.reports.ExecutiveStatusReport.ExecutiveStatusDetailReportView;
import com.shaic.claim.reports.ExecutiveStatusReport.ExecutiveStatusDetailReportViewImpl;
import com.shaic.claim.reports.ackwithoutrodreport.AckWithoutRodReportViewImpl;
import com.shaic.claim.reports.agentbrokerreport.AgentBrokerReportViewImpl;
import com.shaic.claim.reports.autoClosureBatch.AutoClosureBatchView;
import com.shaic.claim.reports.autoClosureBatch.AutoClosureBatchViewImpl;
import com.shaic.claim.reports.autoallocationaancelreport.AutoAllocationCancelReportView;
import com.shaic.claim.reports.autoallocationaancelreport.AutoAllocationCancelReportViewImpl;
import com.shaic.claim.reports.automationdashboard.AutomationDashboardViewImpl;
import com.shaic.claim.reports.billreceivedreport.BillReceivedReportView;
import com.shaic.claim.reports.billreceivedreport.BillReceivedReportViewImpl;
import com.shaic.claim.reports.branchManagerFeedBack.BranchManagerFeedBackReportViewImpl;
import com.shaic.claim.reports.branchManagerFeedbackReportingPattern.BranchManagerFeedBackReportingPatternViewImpl;
import com.shaic.claim.reports.callcenterDashBoard.CallcenterDashBoardReportView;
import com.shaic.claim.reports.callcenterDashBoard.CallcenterDashBoardReportViewImpl;
import com.shaic.claim.reports.claimsdailyreportnew.ClaimsDailyReportView;
import com.shaic.claim.reports.claimsdailyreportnew.ClaimsDailyReportViewImpl;
import com.shaic.claim.reports.claimstatusreportnew.ClaimsStatusReportView;
import com.shaic.claim.reports.claimstatusreportnew.ClaimsStatusReportViewImpl;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUwisePreauthReportView;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUwisePreauthReportViewImpl;
import com.shaic.claim.reports.cpuwiseperformancedetail.CpuWisePerformanceReportViewImpl;
import com.shaic.claim.reports.dailyreport.DailyReportViewImpl;
import com.shaic.claim.reports.dispatchDetailsReport.DispatchDetailsReportView;
import com.shaic.claim.reports.dispatchDetailsReport.DispatchDetailsReportViewImpl;
import com.shaic.claim.reports.executivesummaryreqort.ExecutiveStatusSummaryReportView;
import com.shaic.claim.reports.executivesummaryreqort.ExecutiveStatusSummaryReportViewImpl;
import com.shaic.claim.reports.finapprovednotsettledreport.FinApprovedPaymentPendingReportViewImpl;
import com.shaic.claim.reports.fraudanalysis.FraudReportView;
import com.shaic.claim.reports.fraudanalysis.FraudReportViewImpl;
import com.shaic.claim.reports.fvrassignmentreport.FVRAssignmentReportViewImpl;
import com.shaic.claim.reports.gmcdailyreport.GmcDailyReportView;
import com.shaic.claim.reports.gmcdailyreport.GmcDailyReportViewImpl;
import com.shaic.claim.reports.helpdeskstatusreport.HelpDeskStatusReportViewImpl;
import com.shaic.claim.reports.hospitalintimationstatus.HospitalIntimationReportStatusViewImpl;
import com.shaic.claim.reports.hospitalwisereport.HospitalWiseReportViewImpl;
import com.shaic.claim.reports.investigationassignedreport.InvAssignReportViewImpl;
import com.shaic.claim.reports.lumenstatus.LumenStatusWiseReportViewImpl;
import com.shaic.claim.reports.marketingEscalationReport.MarketingEscalationReportView;
import com.shaic.claim.reports.marketingEscalationReport.MarketingEscalationReportViewImpl;
import com.shaic.claim.reports.medicalAuditCashlessIssueanceReport.MedicalAuditCashlessIssuanceReportView;
import com.shaic.claim.reports.medicalAuditCashlessIssueanceReport.MedicalAuditCashlessIssuanceReportViewImpl;
import com.shaic.claim.reports.medicalAuditClaimStatusReport.MedicalAuditClaimStatusReportView;
import com.shaic.claim.reports.medicalAuditClaimStatusReport.MedicalAuditClaimStatusReportViewImpl;
import com.shaic.claim.reports.medicalmailreport.MedicalMailReportViewImpl;
import com.shaic.claim.reports.metabasereport.MetabaseReportViewImpl;
import com.shaic.claim.reports.negotiationreport.NegotiationReportView;
import com.shaic.claim.reports.negotiationreport.NegotiationReportViewImpl;
import com.shaic.claim.reports.opclaimreport.OPClaimReportView;
import com.shaic.claim.reports.opclaimreport.OPClaimReportViewImpl;
import com.shaic.claim.reports.opinionvalidationreport.OpinionValidationReportView;
import com.shaic.claim.reports.opinionvalidationreport.OpinionValidationReportViewImpl;
import com.shaic.claim.reports.paymentbatchreport.PaymentBatchReportViewImpl;
import com.shaic.claim.reports.paymentpendingdashboard.PaymentPendingDashboardViewImpl;
import com.shaic.claim.reports.paymentprocess.PaymentProcessViewImpl;
import com.shaic.claim.reports.productivityreport.ProductivityReportViewImpl;
import com.shaic.claim.reports.shadowProvision.SearchShowdowView;
import com.shaic.claim.reports.shadowProvision.ShadowProvisionViewImpl;
import com.shaic.claim.reports.tatreport.TATReportView;
import com.shaic.claim.reports.tatreport.TATReportViewImpl;
import com.shaic.claim.rod.wizard.cancelAcknowledgement.CancelAcknowledgementWizardView;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.pages.AcknowledgeDocReceivedWizardViewImpl;
import com.shaic.claim.rod.wizard.pages.BillEntryWizardViewImpl;
import com.shaic.claim.rod.wizard.pages.CreateOnlineRODWizardViewImpl;
import com.shaic.claim.rod.wizard.pages.CreateRODWizardViewImpl;
import com.shaic.claim.search.specialist.search.SubmitSpecialistFormDTO;
import com.shaic.claim.search.specialist.search.SubmitSpecialistTableDTO;
import com.shaic.claim.search.specialist.search.SubmitSpecialistViewImpl;
import com.shaic.claim.settlementpullback.SettlementPullBackView;
import com.shaic.claim.settlementpullback.SettlementPullBackViewImpl;
import com.shaic.claim.settlementpullback.dto.SearchSettlementPullBackDTO;
import com.shaic.claim.settlementpullback.searchbatchpendingpullback.LotPullBackPageView;
import com.shaic.claim.settlementpullback.searchbatchpendingpullback.LotPullBackPageViewImpl;
import com.shaic.claim.settlementpullback.searchbatchpendingpullback.SearchLotPullBackTableDTO;
import com.shaic.claim.specialist.SpecialistViewImpl;
import com.shaic.claim.submitSpecialist.SubmitSpecialistPageViewImpl;
import com.shaic.claim.userproduct.document.ProductAndDocumentTypeDTO;
import com.shaic.claim.userproduct.document.ProductAndDocumentTypeView;
import com.shaic.claim.userproduct.document.search.UserACtivationView;
import com.shaic.claim.userproduct.document.search.UserManagementDTO;
import com.shaic.claim.userproduct.document.search.UserManagementView;
import com.shaic.claim.userreallocation.SearchReallocationDoctorDetailsTableDTO;
import com.shaic.claim.userreallocation.SearchReallocationDoctorDetailsView;
import com.shaic.claim.withdrawPostProcessWizard.WithdrawPreauthPostProcessPageDTO;
import com.shaic.claim.withdrawPostProcessWizard.WithdrawPreauthPostProcessWizardViewImpl;
import com.shaic.claim.withdrawWizard.WithdrawPreauthPageDTO;
import com.shaic.claim.withdrawWizard.WithdrawPreauthWizardViewImpl;
import com.shaic.claims.reibursement.addaditionaldocuments.AddAditionalDocumentsViewImpl;
import com.shaic.claims.reibursement.rod.UploadNEFTDetails.UploadNEFTDetailsViewImpl;
import com.shaic.claims.reibursement.rod.addaditionaldocumentsPaymentInfo.AddAditionalDocumentsPaymentInfoViewImpl;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.gmcautomailer.GmcAutomailerView;
import com.shaic.domain.gmcautomailer.GmcAutomailerViewImpl;
import com.shaic.feedback.managerfeedback.BranchManagerFeedbackhomePageDto;
import com.shaic.feedback.managerfeedback.FeedbackStatsDto;
import com.shaic.feedback.managerfeedback.ManagerFeedBackView;
import com.shaic.feedback.managerfeedback.ManagerFeedBackViewImpl;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackView;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackViewImpl;
import com.shaic.gpaclaim.unnamedriskdetails.SearchUnnamedRiskDetailsViewImpl;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageDTO;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageView;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageViewImpl;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItem;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.shaic.newcode.wizard.dto.HospitalAcknowledgeDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.pages.IntimationDetailsImpl;
import com.shaic.paclaim.addAdditinalDocument.search.PAAddAdditionalDocumentsWizardViewImpl;
import com.shaic.paclaim.billing.hospprocessclaimbilling.page.PAHospCoverBillingWizardViewImpl;
import com.shaic.paclaim.billing.hospprocessclaimbilling.search.PASearchHospCoverProcessClaimBillingViewImpl;
import com.shaic.paclaim.billing.processclaimbilling.page.PABillingWizardViewImpl;
import com.shaic.paclaim.billing.processclaimbilling.search.PASearchProcessClaimBillingViewImpl;
import com.shaic.paclaim.cashless.downsize.wizard.PADownSizePreauthWizardViewImpl;
//import com.shaic.paclaim.cashless.enhancement.search.PASearchEnhancementViewImpl;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancementWizardViewImpl;
//import com.shaic.paclaim.cashless.fle.search.PASearchPreMedicalProcessingEnhancementViewImpl;
import com.shaic.paclaim.cashless.fle.wizard.wizardfiles.PAPremedicalEnhancementWizardViewImpl;
//import com.shaic.paclaim.cashless.flp.search.PAProcessPreMedicalViewImpl;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizarViewImpl;
//import com.shaic.paclaim.cashless.preauth.search.PASearchPreauthViewImpl;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardViewImpl;
import com.shaic.paclaim.cashless.processdownsize.wizard.PADownsizePreauthRequestWizardViewImpl;
import com.shaic.paclaim.cashless.withdraw.search.PASearchWithdrawCashLessProcessTableDTO;
import com.shaic.paclaim.cashless.withdraw.wizard.PAWithdrawPreauthPageDTO;
import com.shaic.paclaim.cashless.withdraw.wizard.PAWithdrawPreauthWizardViewImpl;
import com.shaic.paclaim.convertClaimToReimb.ConvertPAClaimPageViewImpl;
import com.shaic.paclaim.convertClaimToReimb.SearchConvertPAClaimViewImpl;
import com.shaic.paclaim.convertClaimToReimbursement.convertReimbursementPage.PAConvertReimbursementPageViewImpl;
import com.shaic.paclaim.convertClaimToReimbursement.search.PASearchConvertReimbursementViewImpl;
import com.shaic.paclaim.financial.claimapproval.hosiptal.PASearchProcessClaimAprovalHosViewImpl;
import com.shaic.paclaim.financial.claimapproval.nonhosiptal.PASearchProcessClaimAprovalNonHosViewImpl;
import com.shaic.paclaim.financial.claimapproval.nonhosiptalpage.PAClaimAprNonHosWizardViewImpl;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.PANonHospFinancialWizardViewImpl;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.search.PASearchProcessClaimFinancialsNonHospView;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.search.PASearchProcessClaimFinancialsNonHospViewImpl;
import com.shaic.paclaim.generateCoveringLetter.DecideOnPACoveringLetterWizardViewImpl;
import com.shaic.paclaim.health.reimbursement.billing.search.PAHealthSearchProcessClaimBillingViewImpl;
import com.shaic.paclaim.health.reimbursement.billing.wizard.wizardfiles.PAHealthBillingWizardViewImpl;
import com.shaic.paclaim.health.reimbursement.financial.search.PAHealthSearchProcessClaimFinancialsView;
import com.shaic.paclaim.health.reimbursement.financial.search.PAHealthSearchProcessClaimFinancialsViewImpl;
import com.shaic.paclaim.health.reimbursement.financial.wizard.PAHealthFinancialWizardViewImpl;
import com.shaic.paclaim.health.reimbursement.medicalapproval.search.PAHealthSearchProcessClaimRequestFormDTO;
import com.shaic.paclaim.health.reimbursement.medicalapproval.search.PAHealthSearchProcessClaimRequestViewImpl;
import com.shaic.paclaim.health.reimbursement.medicalapproval.wizard.PAHealthClaimRequestWizardViewImpl;
import com.shaic.paclaim.healthsettlementpullback.PAHospSettlementPullBackView;
import com.shaic.paclaim.healthsettlementpullback.PAHospSettlementPullBackViewImpl;
import com.shaic.paclaim.healthsettlementpullback.dto.PAHospSearchSettlementPullBackDTO;
import com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel.PACloseClaimPageDTO;
import com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel.PACloseClaimView;
import com.shaic.paclaim.manageclaim.closeclaim.pageRodLevel.PACloseClaimRodLevelView;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimTableDTORODLevel;
import com.shaic.paclaim.manageclaim.closeclaimInProcess.pageRODLevel.PACloseClaimInProcessView;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageClaimLevel.PAHealthReopenClaimPageDTO;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageClaimLevel.PAHealthReopenClaimView;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageRodLevel.PAHealthReOpenRodLevelClaimView;
import com.shaic.paclaim.manageclaim.healthreopenclaim.searchRodLevel.PAHealthSearchReOpenClaimRodLevelTableDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel.PAReopenClaimPageDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel.PAReopenClaimView;
import com.shaic.paclaim.manageclaim.reopenclaim.pageRodLevel.PAReOpenRodLevelClaimView;
import com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel.PASearchReOpenClaimRodLevelTableDTO;
import com.shaic.paclaim.medicalapproval.processclaimrequest.search.PASearchProcessClaimRequestViewImpl;
import com.shaic.paclaim.printbulkreminder.SearchPAPrintRemainderBulkViewImpl;
import com.shaic.paclaim.processRejectionPage.PAProcessRejectionPageWizardViewImpl;
import com.shaic.paclaim.processfieldvisit.search.PASearchProcessFieldVisitViewImpl;
import com.shaic.paclaim.registration.PAClaimRegistrationView;
import com.shaic.paclaim.registration.SearchPAClaimRegisterViewImpl;
import com.shaic.paclaim.reimbursement.bulkreminder.SearchGeneratePARemainderBulkViewImpl;
import com.shaic.paclaim.reimbursement.draftquery.DecideOnDraftPAQueryDetailWizardViewImpl;
import com.shaic.paclaim.reimbursement.draftquery.SearchDraftPAQueryLetterViewImpl;
import com.shaic.paclaim.reimbursement.draftrejection.DraftPARejectionLetterDetailViewImpl;
import com.shaic.paclaim.reimbursement.draftrejection.SearchDraftPARejectionLetterViewImpl;
import com.shaic.paclaim.reimbursement.pasearchuploaddocuments.PASearchUploadDocumentsViewImpl;
import com.shaic.paclaim.reimbursement.pasearchuploaddocuments.PASearchUploadDocumentsWizardViewImpl;
import com.shaic.paclaim.reimbursement.processdraftquery.DecideOnProcessDraftPAQueryWizardViewImpl;
import com.shaic.paclaim.reimbursement.processdraftquery.SearchProcessDraftPAQueryViewImpl;
import com.shaic.paclaim.reimbursement.processdraftrejection.DecideOnPARejectionWizardViewImpl;
import com.shaic.paclaim.reimbursement.processdraftrejection.SearchProcessDraftPARejectionViewImpl;
import com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess.PAUploadDocumentsOutsideProcessViewImpl;
import com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess.PAUploadDocumentsOutsideProcessWizardViewImpl;
import com.shaic.paclaim.reminder.SearchGeneratePARemainderViewImpl;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAAcknowledgeDocumentWizardviewImpl;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PASearchAckDocumentReceiverViewImpl;
import com.shaic.paclaim.rod.createrod.search.PACreateRODWizardViewImpl;
import com.shaic.paclaim.rod.createrod.search.PASearchCreateRODViewImpl;
import com.shaic.paclaim.rod.enterbilldetails.search.PAEnterBillDetailsWizardViewImpl;
import com.shaic.paclaim.rod.enterbilldetails.search.PASearchEnterBillDetailsViewImpl;
import com.shaic.paclaim.settlementpullback.PASettlementPullBackView;
import com.shaic.paclaim.settlementpullback.PASettlementPullBackViewImpl;
import com.shaic.paclaim.settlementpullback.dto.PASearchSettlementPullBackDTO;
import com.shaic.reimburement.gatewayAddAdditinalDocument.search.PhysicalDocumentReceivedMakerViewImpl;
import com.shaic.reimburement.gatewayAddAdditinalDocument.search.ReceivedPhysicalDocumentsWizardViewImpl;
import com.shaic.reimburement.specialapprover.approveclaim.search.SearchApproveClaimViewImpl;
import com.shaic.reimburement.specialapprover.processclaim.search.SearchProcessClaimViewImpl;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.AcknowledgeInvestigationCompletedViewImpl;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigationWizardViewImpl;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigatorDto;
import com.shaic.reimbursement.billing.processClaimRequestBenefits.SearchProcessClaimRequestBenefitsViewImpl;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingAutoAllocationView;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingViewImpl;
import com.shaic.reimbursement.claims_alert.search.ClaimsAlertMasterView;
import com.shaic.reimbursement.claims_alert.search.ClaimsAlertMasterViewImpl;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigationWizardViewImpl;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigatorDto;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsView;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsViewImpl;
import com.shaic.reimbursement.financialapprover.processclaimrequestBenefits.search.SearchProcessClaimRequestBenefitsFinancViewImpl;
import com.shaic.reimbursement.fraudidentification.FraudIdentificationView;
import com.shaic.reimbursement.fraudidentification.FraudIdentificationViewImpl;
import com.shaic.reimbursement.investigation.ackinvestivationcompleted.search.SearchAckInvestigationCompletedTableDTO;
import com.shaic.reimbursement.investigation.ackinvestivationcompleted.search.SearchAckInvestigationCompletedViewImpl;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationViewImpl;
import com.shaic.reimbursement.investigation.draftinvestigation.search.SearchDraftInvestigationViewImpl;
import com.shaic.reimbursement.investigation.reassigninvestigation.search.SearchReAssignInvestigationViewImpl;
import com.shaic.reimbursement.investigationgrading.InvestigationGradingWizardViewImpl;
import com.shaic.reimbursement.investigationgrading.SearchInvestgationGradingViewImpl;
import com.shaic.reimbursement.investigationin_direct_assigment.search.InvestigationDirectAssignmentView;
import com.shaic.reimbursement.investigationin_direct_assigment.search.InvestigationDirectAssignmentViewImpl;
import com.shaic.reimbursement.investigationmaster.InvestigationMasterDTO;
import com.shaic.reimbursement.investigationmaster.InvestigationMasterViewImpl;
import com.shaic.reimbursement.investigationmaster.InvestigationMasterWizardViewImpl;
import com.shaic.reimbursement.manageclaim.ClaimWiseAllowApprovalPages.ClaimWiseAllowApprovalView;
import com.shaic.reimbursement.manageclaim.HoldMonitorFLPScreen.SearchHoldMonitorFLPScreenViewImpl;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenViewImpl;
//import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenViewImpl;
import com.shaic.reimbursement.manageclaim.SearchUpdateRodDetails.SearchUpdateRodDetailsViewImpl;
import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.CloseClaimPageDTO;
import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.CloseClaimView;
import com.shaic.reimbursement.manageclaim.closeclaim.pageRodLevel.CloseClaimRodLevelView;
import com.shaic.reimbursement.manageclaim.closeclaim.searchRodLevel.SearchCloseClaimTableDTORODLevel;
import com.shaic.reimbursement.manageclaim.closeclaimInProcess.pageRODLevel.CloseClaimInProcessView;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageClaimLevel.ReopenClaimPageDTO;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageClaimLevel.ReopenClaimView;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageRodLevel.ReOpenRodLevelClaimView;
import com.shaic.reimbursement.manageclaim.reopenclaim.searchRodLevel.SearchReOpenClaimRodLevelTableDTO;
import com.shaic.reimbursement.manageclaim.searchClaimwiseApproval.SearchClaimWiseAllowApprovalDto;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestFormDTO;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestViewImpl;
import com.shaic.reimbursement.medicalapproval.processclaimrequestzonal.search.SearchProcessClaimRequestZonalViewImpl;
import com.shaic.reimbursement.paymentprocess.createbatch.search.SearchCreateBatchView;
import com.shaic.reimbursement.paymentprocess.createbatch.search.SearchCreateBatchViewImpl;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.PopupStopPaymentRequestWizard;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestWizardPage;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.PopupStopPaymentValidateWizard;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.StopPaymentValidationWizardPage;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailTableDTO;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailView;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailViewImpl;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkViewImpl;
import com.shaic.reimbursement.processfieldvisit.search.SearchProcessFieldVisitViewImpl;
import com.shaic.reimbursement.processi_investigationi_initiated.ProcessInvestigationInitiatedViewImpl;
import com.shaic.reimbursement.processi_investigationi_initiated.search.SearchProcessInvestigationInitiatedTableDTO;
import com.shaic.reimbursement.processi_investigationi_initiated.search.SearchProcessInvestigationInitiatedViewImpl;
import com.shaic.reimbursement.queryrejection.draftquery.search.DecideOnDraftQueryDetailWizardViewImpl;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterViewImpl;
import com.shaic.reimbursement.queryrejection.draftrejection.search.DraftRejectionLetterDetailViewImpl;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterTableDTO;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterViewImpl;
import com.shaic.reimbursement.queryrejection.generateremainder.search.GenerateReminderLetterDetailViewImpl;
import com.shaic.reimbursement.queryrejection.generateremainder.search.SearchGenerateRemainderViewImpl;
import com.shaic.reimbursement.queryrejection.processdraftquery.search.SearchProcessDraftQueryViewImpl;
import com.shaic.reimbursement.queryrejection.processdraftrejection.search.SearchProcessDraftRejectionViewImpl;
import com.shaic.reimbursement.reassigninvestigation.ReAssignInvestigationWizardViewImpl;
import com.shaic.reimbursement.reminderBulkSearch.SearchGenerateRemainderBulkViewImpl;
import com.shaic.reimbursement.rod.createonlinerod.search.SearchCreateOnlineRODViewImpl;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODViewImpl;
import com.shaic.reimbursement.rod.enterbilldetails.search.SearchEnterBillDetailViewImpl;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationViewImpl;
import com.shaic.reimbursement.specialistprocessing.submitspecialist.search.SearchSubmitSpecialistAdviseViewImpl;
import com.shaic.reimbursement.topup_policy_master.search.TopUpPolicyMasterView;
import com.shaic.reimbursement.topup_policy_master.search.TopUpPolicyMasterViewImpl;
import com.shaic.reimbursement.uploadTranslatedDocument.UploadTranslatedDocumentView;
import com.shaic.reimbursement.uploadTranslatedDocument.UploadTranslatedDocumentViewImpl;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportViewImpl;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MenuViewImpl extends AbstractMVPView implements MenuView,OMPMenuView,LumenMenuView {
	@Inject
	private NavigationTree tree;
	@Inject
	private Toolbar toolbar;
	@Inject
	@HorizontalSplitPanelProperties
	private HorizontalSplitPanel horizontalSplit;

	private Panel contentPanel;

//	@Inject
//	private LoginViewUI loginViewUI;

	@Inject
	private Instance<MVPView> views;

	@EJB
	private MenuItemBean menuItems;
	
	@EJB
	private MasterService masterService;

	private VerticalLayout mainLayout = new VerticalLayout();

	private Accordion accordion = new Accordion();

	@PostConstruct
	public void initView() {
		setSizeFull();
		setCompositionRoot(mainLayout);
		mainLayout.setSizeFull();
		Object attribute = null;
		
		String username = "admin";
		
//		if (username != null)
//		{
//			SSOAgentSessionBean ssoAgentBean = (SSOAgentSessionBean) getSession().getAttribute("ssoAgentBean");
//			Map<String, String> samlSSOAttributes = ssoAgentBean.getSamlSSOAttributes();
//			String[] roles = new String[samlSSOAttributes.keySet().size()];
//			int i = 0;
//			for (String key : samlSSOAttributes.keySet()) {
//				roles[i] = samlSSOAttributes.get(key);
//			}
//			ImsUser imsUser = new ImsUser(username, roles, true);
//			fireViewEvent(MenuPresenter.SHOW_SUCCESS_VIEW, imsUser);
//			fireViewEvent(MenuItemBean.PROCESS_PREAUTH, null);
//		}
			
		
		
		
//		if (getSession() != null) {
//			attribute = getSession().getAttribute("isLoggedIn");
//		}
//
//		if (attribute == null) {
//			mainLayout.addComponent(toolbar);
//			mainLayout.addComponent(loginViewUI);
//		} else if ((Boolean) getSession().getAttribute("isLoggedIn")) {
//			// fireViewEvent(MenuPresenter.SHOW_SUCCESS_VIEW, null);
//			fireViewEvent(MenuItemBean.PROCESS_PREAUTH, null);
//		}
	}

	@Override
	public void setView(final Class<? extends MVPView> viewClass,
			final boolean selectInNavigationTree) {
		MVPView view = views.select(viewClass).get();
		contentPanel.setContent((Component) view);

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void enter() {
		super.enter();
		VaadinSession session = getSession();
		String username =(String) session.getAttribute(BPMClientContext.USERID);

		if (username != null)
		{
			KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) getSession().getAttribute(BPMClientContext.KEYCLOAK_PRINCIPAL);
			String emailAddress = keycloakPrincipal.getKeycloakSecurityContext().getToken().getEmail();
			String firstName = keycloakPrincipal.getKeycloakSecurityContext().getToken().getGivenName();
			
			String[] roles = null;
			if(BPMClientContext.KEYCLOAK_CLIENT_ID != null) {
				Access access = keycloakPrincipal.getKeycloakSecurityContext().getToken().getResourceAccess(BPMClientContext.KEYCLOAK_CLIENT_ID);
//				System.out.println("User Roles : " + access.getRoles());
				if((access != null) && (access.getRoles() != null)) {
					Set<String> roleSet = access.getRoles();
					roles = roleSet.toArray(new String[roleSet.size()]);
				} else {
					roles = new String[1];
					roles[0] = "NONE";
				}
			} else {
				roles = new String[1];
				roles[0] = "NONE";
			}
			ImsUser imsUser = new ImsUser(username, roles, true);
			imsUser.setEmpName(firstName);
			getSession().setAttribute("isLoggedIn", true);
			getSession().setAttribute(BPMClientContext.USERID, username);
			getSession().setAttribute(BPMClientContext.EMAIL_ADDRESS, emailAddress);
			getSession().setAttribute(BPMClientContext.USER_OBJECT, imsUser);
			getSession().setAttribute(BPMClientContext.PASSWORD, "Star@123");
			
			fireViewEvent(MenuPresenter.SHOW_SUCCESS_VIEW, imsUser, firstName);
		}/*else{
			fireViewEvent(MenuPresenter.SHOW_INTIMATION_SERVLET_VIEW,null);
		}*/
	}
	
	@Override
	public void enterIntimationView() {
		fireViewEvent(MenuPresenter.SHOW_INTIMATION_SERVLET_VIEW,null);
	}

	@Override
	public void setupMenu(MenuItem rootMenu) {
		tree.setupMenu(rootMenu);
		// fireViewEvent(MenuItemBean.NEW_INTIMATION, 1143224l);

		// if(rootMenu.getCode().equalsIgnoreCase(MenuItemBean.GENERATE_REJECTIONLETTER))
		// {
		// fireViewEvent(MenuItemBean.GENERATE_REJECTIONLETTER,"RejectionLetter");
		// }
	}

	// Will handle add Intimation
	// @Override
	// public void setIntimationWizardView(Class<? extends GMVPView> viewClass,
	// TmpPolicy policy, LinkedHashMap<String, String> policyValues) {
	//
	// MVPView view = views.select(viewClass).get();
	// IntimationWizardImpl wizard = (IntimationWizardImpl) view;
	// IntimationDTO bean = new IntimationDTO();
	// wizard.initView(bean);
	// tree.setSelectedView(viewClass);
	// horizontalSplit.setSecondComponent((Component) view);
	//
	// ((AbstractMVPView) view).enter();
	// }

	// @Override
	// public void setIntimationWizardView(Class<? extends GMVPView> viewClass,
	// IntimationDTO intimationDTO) {
	//
	// // MVPView view = views.select(viewClass).get();
	// //// IntimationWizardImpl wizard = (IntimationWizardImpl) view;
	// // wizard.initView(intimationDTO);
	// // tree.setSelectedView(viewClass);
	// // horizontalSplit.setSecondComponent((Component) view);
	// //
	// // ((AbstractMVPView) view).enter();
	// }

	// @Override
	// public void setNewIntimationView(Class<? extends GMVPView> viewClass,
	// NewIntimationDto newIntimationDto) {
	//
	// GMVPView view = views.select(viewClass).get();
	// IntimationDetailsImpl intimationDetailView = (IntimationDetailsImpl)
	// view;
	// intimationDetailView.initView(newIntimationDto);
	// tree.setSelectedView(viewClass);
	// contentPanel.setContent((Component) view);
	//
	// ((AbstractMVPView) view).enter();
	// }

	@Override
	public void setNewIntimationView(Class<? extends GMVPView> viewClass,
			NewIntimationDto newIntimationDto, ParameterDTO parameters) {

		GMVPView view = views.select(viewClass).get();
		IntimationDetailsImpl intimationDetailView = (IntimationDetailsImpl) view;
		intimationDetailView.initView(newIntimationDto);
		if (null != parameters.getPrimaryParameter()) {
			if (parameters.getPrimaryParameter() instanceof SearchEditHospitalDetailsTableDTO) {
				SearchEditHospitalDetailsTableDTO searchEditHospitalDto = (SearchEditHospitalDetailsTableDTO) parameters
						.getPrimaryParameter();
				/*intimationDetailView
						.setEditHospitalHumanTask(searchEditHospitalDto
								.getHumanTask());*/
			}
		}
		tree.setSelectedView(viewClass);
		if(contentPanel == null){
			//setCompositionRoot(intimationDetailView);
		}else{
			contentPanel.setContent((Component) view);
			((AbstractMVPView) view).enter();
			
		}
		
	}

	// //Intimation Edit
	// @Override
	// public void setIntimationWizardView(Class<? extends GMVPView> viewClass,
	// TmpPolicy policy, IntimationDTO intimationDTO) {
	// MVPView view = views.select(viewClass).get();
	// IntimationWizardImpl wizard = (IntimationWizardImpl) view;
	// wizard.markAsDirtyRecursive();
	// horizontalSplit.setSecondComponent((Component) view);
	// tree.setSelectedView(viewClass);
	// wizard.initView(intimationDTO);
	//
	// ((AbstractMVPView) view).enter();
	// }

	@Override
	public void setView(Class<? extends MVPView> viewClass,
			boolean selectInNavigationTree, ParameterDTO parameter) {
		MVPView view = views.select(viewClass).get();
		GenerateLetterView letterView = (GenerateLetterView) view;
		contentPanel.setContent((Component) view);
		tree.setSelectedView(viewClass);

		letterView.showSearchCoveringLetterView(null);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void showLoginSuccess(ImsUser imsUser) {
		VerticalLayout cPanel = new VerticalLayout();
		contentPanel = new Panel();
		final Button toggleBtn = new Button();
		toggleBtn.setIcon(FontAwesome.TOGGLE_LEFT);
		
		toggleBtn.setStyleName("link");
		toggleBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (horizontalSplit.getFirstComponent() != null) {
					boolean flag = !horizontalSplit.getFirstComponent()
							.isVisible();
					if (flag) {
						horizontalSplit.setSplitPosition(230, Unit.PIXELS);
						toggleBtn.setIcon(FontAwesome.TOGGLE_LEFT);
					} else {
						horizontalSplit.setSplitPosition(0, Unit.PIXELS);
						toggleBtn.setIcon(FontAwesome.TOGGLE_RIGHT);
					}
					horizontalSplit.getFirstComponent().setVisible(flag);

				}

			}
		});

		cPanel.addComponent(toggleBtn);
		VerticalLayout mainVertical = new VerticalLayout(contentPanel);
		cPanel.addComponent(mainVertical);
		cPanel.setComponentAlignment(toggleBtn, Alignment.MIDDLE_LEFT);
		HorizontalLayout treeLayout = new HorizontalLayout();
		String bancsPaymentFlag = null;
		MastersValue paymentMenuBasedOnMAsterFlag = masterService.getMasterCodeFlag(SHAConstants.MASTER_CODE);
		
		if(null != paymentMenuBasedOnMAsterFlag && null != paymentMenuBasedOnMAsterFlag.getMappingCode() && 
				paymentMenuBasedOnMAsterFlag.getMappingCode().equalsIgnoreCase(SHAConstants.N_FLAG)) {
			bancsPaymentFlag = SHAConstants.N_FLAG;
		}
		MenuItem rootMenu = menuItems.getMenusBasedOnUserLogin(imsUser, bancsPaymentFlag);
		treeLayout.addComponent(tree);
		setupMenu(rootMenu);
		
		mainLayout.addComponent(toolbar);
		mainLayout.addComponent(horizontalSplit);
		mainLayout.setExpandRatio(horizontalSplit, 1);
		if (horizontalSplit.getFirstComponent() == null) {
			horizontalSplit.setFirstComponent(treeLayout);
			horizontalSplit.getFirstComponent().setWidth("-1px");
		}
		horizontalSplit.setSplitPosition(230, Unit.PIXELS);
		horizontalSplit.setSecondComponent(cPanel);
		toolbar.updateUserStatus();

	}
	
	@Override
	public void showCreateIntimationView() {
		contentPanel = new Panel();
		mainLayout.addComponent(contentPanel);
	}

	@Override
	public void setViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	//R1257
	@Override
	public void setViewGG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	
	@Override
	public void setViewPAackDocReceive(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> paClaimType,BeanItemContainer<SelectValue> paDocumentType) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchAckDocumentReceiverViewImpl paSearchAckDocReceiveViewImpl = (PASearchAckDocumentReceiverViewImpl) view;
		//paSearchAckDocReceiveViewImpl.init(paClaimType);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	

	/**
	 * Commenting the above code, since BeanContainer<SelectValue> is the valid
	 * parameter that has to be passed to master service, to display the values
	 * in the combo box. Hence below method is added with valid parameter.
	 * */

	/*
	 * public void setViewG(Class<? extends GMVPView> viewClass, boolean
	 * selectInNavigationTree, BeanItemContainer<TmpCPUCode> parameter) {
	 */
	public void setViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchConvertClaimViewImpl convertClaimViewImpl = (SearchConvertClaimViewImpl) view;
		convertClaimViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	public void setBulkConvertReimbView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpuParamContainer,BeanItemContainer<SelectValue> typeVlueParamContainer,List<SearchBatchConvertedTableDto> prevConvertedBatch) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchBulkConvertReimbViewImpl bulkConvertReimbViewImpl = (SearchBulkConvertReimbViewImpl) view;
		bulkConvertReimbViewImpl.init(cpuParamContainer,typeVlueParamContainer);
		bulkConvertReimbViewImpl.setPrevBatchConvertList(prevConvertedBatch);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	
	public void setViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, Map<String, Object> parameter) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		SearchClaimRegisterViewImpl claimregisterViewImpl = (SearchClaimRegisterViewImpl) view;

		claimregisterViewImpl.setReferenceData(parameter);
		claimregisterViewImpl.init();

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();

	}
	
	public void setViewAllowGHIRegister(Class<? extends GMVPView> viewClass, Map<String, Object> parameter){
		MVPView view = views.select(viewClass).get();

		SearchGhiAllowClaimRegisterViewImpl ghiAllowclaimregisterViewImpl = (SearchGhiAllowClaimRegisterViewImpl) view;
		ghiAllowclaimregisterViewImpl.init(parameter);

		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	public void setPAClaimRegisterView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, Map<String, Object> parameter) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		SearchPAClaimRegisterViewImpl paClaimregisterViewImpl = (SearchPAClaimRegisterViewImpl) view;

		paClaimregisterViewImpl.setReferenceData(parameter);
		paClaimregisterViewImpl.init();

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setPreauthWizardView(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDTO, Boolean processId) {

		MVPView view = views.select(viewClass).get();
		PreauthWizardViewImpl wizard = (PreauthWizardViewImpl) view;
		wizard.initView(preauthDTO, processId);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setPreMedicalWizardView(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDTO, Boolean processId) {

		GMVPView view = views.select(viewClass).get();
		PreMedicalPreauthWizarViewImpl wizard = (PreMedicalPreauthWizarViewImpl) view;
		wizard.initView(preauthDTO, processId);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setHospitalView(Class<? extends GMVPView> viewClass, HospitalAcknowledgeDTO hospitalDTO,SearchAcknowledgeHospitalCommunicationTableDTO searchFormDto,NewIntimationDto intimationDto,ClaimDto claimDto) {
		MVPView view = views.select(viewClass).get();
		HospitalViewImpl wizard = (HospitalViewImpl) view;
		wizard.initView(hospitalDTO,searchFormDto,intimationDto,claimDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setPEDRequestProcess(
			Class<PEDRequestDetailsProcessView> viewClass,
			SearchPEDRequestProcessTableDTO searchTableDTO) {
		MVPView view = views.select(viewClass).get();
		PEDRequestDetailsProcessViewImpl wizard = (PEDRequestDetailsProcessViewImpl) view;
		wizard.initView(searchTableDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}

	@Override
	public void setRejectionView(Class<? extends GMVPView> viewClass,
			SearchProcessRejectionTableDTO rejectionDTO) {
		MVPView view = views.select(viewClass).get();
//		PAProcessRejectionPageViewImpl wizard = (PAProcessRejectionPageViewImpl) view;
        ProcessRejectionPageWizardViewImpl wizard = (ProcessRejectionPageWizardViewImpl) view;
		wizard.initView(rejectionDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}

	@Override
	public void setPARejectionView(Class<? extends GMVPView> viewClass,
			SearchProcessRejectionTableDTO rejectionDTO) {
		MVPView view = views.select(viewClass).get();
		PAProcessRejectionPageWizardViewImpl wizard = (PAProcessRejectionPageWizardViewImpl) view;
      //  ProcessRejectionPageWizardViewImpl wizard = (ProcessRejectionPageWizardViewImpl) view;
		wizard.initView(rejectionDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setPEDRequestApproved(
			Class<PEDRequestDetailsApproveView> viewClass,
			SearchPEDRequestApproveTableDTO searchTableDTO) {
		MVPView view = views.select(viewClass).get();
		PEDRequestDetailsApproveViewImpl wizard = (PEDRequestDetailsApproveViewImpl) view;
		wizard.initView(searchTableDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setPEDQueryRequestApproved(
			Class<BancsPEDRequestDetailsApproveView> viewClass,
			BancsSearchPEDRequestApproveTableDTO searchTableDTO) {
		MVPView view = views.select(viewClass).get();
		BancsPEDRequestDetailsApproveViewImpl wizard = (BancsPEDRequestDetailsApproveViewImpl) view;
		wizard.initView(searchTableDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setPEDRequestTeamLeadView(
			Class<PEDRequestDetailsTeamLeadView> viewClass,
			SearchPEDRequestApproveTableDTO searchTableDTO) {
		MVPView view = views.select(viewClass).get();
		PEDRequestDetailsTeamLeadViewImpl wizard = (PEDRequestDetailsTeamLeadViewImpl) view;
		wizard.initView(searchTableDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	public void setFieldVisitView(Class<? extends GMVPView> viewClass,
			SearchFieldVisitTableDTO fieldVisitDTO) {
		MVPView view = views.select(viewClass).get();
		FieldVisitPageViewImpl wizard = (FieldVisitPageViewImpl) view;
		// rejectionDTO=ProcessRejectionMapper.getHospitalDetails();
		wizard.initView(fieldVisitDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}
	
	public void setReAssignFieldVisitView(Class<? extends GMVPView> viewClass,
			SearchReAssignFieldVisitTableDTO fieldVisitDTO) {
		MVPView view = views.select(viewClass).get();
		ReAssignFieldVisitViewImpl wizard = (ReAssignFieldVisitViewImpl) view;
		// rejectionDTO=ProcessRejectionMapper.getHospitalDetails();
		wizard.initView(fieldVisitDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}

	@Override
	public void setPEDRequestProcess(Class<? extends GMVPView> viewClass,
			PEDRequestDetailsProcessDTO pedRequestDetailsProcessDTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFileUploadTableView(Class<? extends GMVPView> viewClass,
			SearchProcessTranslationTableDTO fileUploadDTO) {
		MVPView view = views.select(viewClass).get();
		FileUploadViewImpl wizard = (FileUploadViewImpl) view;
		wizard.initView(fileUploadDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}

	@Override
	public void setAdviceOnPEDPage(Class<AdviseOnPEDView> viewClass,
			SearchAdviseOnPEDTableDTO searchTableDTO,
			OldPedEndorsementDTO editDTO) {
		MVPView view = views.select(viewClass).get();
		AdviseOnPEDViewImpl wizard = (AdviseOnPEDViewImpl) view;
		wizard.initView(searchTableDTO, editDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}

	@Override
	public void setPEDQueryPage(Class<PEDQueryView> viewClass,
			SearchPEDQueryTableDTO searchTableDTO,
			OldPedEndorsementDTO editTableDTO) {
		MVPView view = views.select(viewClass).get();
		PEDQueryViewImpl wizard = (PEDQueryViewImpl) view;
		wizard.initView(searchTableDTO, editTableDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setConvertClaimView(Class<? extends GMVPView> viewClass,
			ConvertClaimDTO convertClaimDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto,
			SearchConvertClaimTableDto searchFormDto) {
		MVPView view = views.select(viewClass).get();
		ConvertClaimPageViewImpl page = (ConvertClaimPageViewImpl) view;
		page.init(convertClaimDto, selectValueContainer, intimationDto,
				searchFormDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setConvertReimbursementView(Class<? extends GMVPView> viewClass,
			ConvertClaimDTO convertClaimDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto,
			SearchConvertClaimTableDto searchFormDto) {
		MVPView view = views.select(viewClass).get();
		ConvertReimbursementPageViewImpl page = (ConvertReimbursementPageViewImpl) view;
		page.init(convertClaimDto, selectValueContainer, intimationDto,
				searchFormDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setSubmitSpecialist(Class<SpecialistViewImpl> viewClass) {
		MVPView view = views.select(viewClass).get();
		SpecialistViewImpl wizard = (SpecialistViewImpl) view;
		wizard.initView();
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}

	@Override
	public void setViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> intimationSrcParameter,
			BeanItemContainer<SelectValue> networkHospTypeParameter,
			BeanItemContainer<SelectValue> treatmentTypeParameter,
			BeanItemContainer<SelectValue> preAuthTypeParameter,BeanItemContainer<SelectValue> specialityContainer,BeanItemContainer<SelectValue> cpuCodeContainer,
			String strViewName) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		if ((SHAConstants.PROCESS_ENHANCEMENT).equalsIgnoreCase(strViewName)) {
//			SearchEnhancementViewImpl enhancementViewImpl = (SearchEnhancementViewImpl) view;
//			enhancementViewImpl.init(intimationSrcParameter,
//					networkHospTypeParameter, treatmentTypeParameter,
//					preAuthTypeParameter,specialityContainer, cpuCodeContainer);
		} else if ((SHAConstants.PROCESS_PREAUTH).equalsIgnoreCase(strViewName)) {
			SearchPreauthViewImpl preAuthViewImpl = (SearchPreauthViewImpl) view;
			preAuthViewImpl.init(intimationSrcParameter,
					networkHospTypeParameter, treatmentTypeParameter,
					preAuthTypeParameter,specialityContainer, cpuCodeContainer);
		} else if ((SHAConstants.PROCESS_PRE_MEDICAL)
				.equalsIgnoreCase(strViewName)) {
//			ProcessPreMedicalViewImpl processPreMedicalImpl = (ProcessPreMedicalViewImpl) view;
//			processPreMedicalImpl.init(intimationSrcParameter,
//					networkHospTypeParameter, preAuthTypeParameter, cpuCodeContainer);
		} else if ((SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT)
				.equalsIgnoreCase(strViewName)) {
//			SearchPreMedicalProcessingEnhancementViewImpl processPreMedicalEnhancement = (SearchPreMedicalProcessingEnhancementViewImpl) view;
//			processPreMedicalEnhancement.init(intimationSrcParameter,
//					networkHospTypeParameter, preAuthTypeParameter, cpuCodeContainer);
		} else if(SHAConstants.PROCESS_PREAUTH_AUTO_ALLOCATION.equalsIgnoreCase(strViewName)){
//			SearchPreauthAutoAllocationViewImpl preauthAutoAllocationViewImpl = (SearchPreauthAutoAllocationViewImpl) view;
//			preauthAutoAllocationViewImpl.init(intimationSrcParameter, networkHospTypeParameter, treatmentTypeParameter, preAuthTypeParameter, specialityContainer, cpuCodeContainer);
		}else if ((SHAConstants.PROCESS_64VB).equalsIgnoreCase(strViewName)) {
//			SearchProcessVBViewImpl preAuthViewImpl = (SearchProcessVBViewImpl) view;
//			preAuthViewImpl.init(intimationSrcParameter,
//					networkHospTypeParameter, treatmentTypeParameter,
//					preAuthTypeParameter,specialityContainer, cpuCodeContainer);
		}
		// SearchConvertClaimViewImpl convertClaimViewImpl =
		// (SearchConvertClaimViewImpl) view;
		// convertClaimViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewG(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,
			String strViewName) {
//		GMVPView view = views.select(viewClass).get();
//		view.resetView();
//		contentPanel.setContent((Component) view);
//		SearchPreauthAutoAllocationViewImpl preauthAutoAllocationViewImpl = (SearchPreauthAutoAllocationViewImpl) view;
//		preauthAutoAllocationViewImpl.manuallyDoSearchForCompletedCases();
////		preauthAutoAllocationViewImpl.manuallyDoSearch();
////		preauthAutoAllocationViewImpl.init(intimationSrcParameter, networkHospTypeParameter, treatmentTypeParameter, preAuthTypeParameter, specialityContainer, cpuCodeContainer);
//		tree.setSelectedView(viewClass);
//		if (selectInNavigationTree) {
//			tree.setSelectedView(viewClass);
//		}
//		((AbstractMVPView) view).enter();
	}

	/**
	 * Added during policy table refractoring activity for search policy UI
	 * screen.
	 *
	 * */
	public void setViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> searchByContainer,
			BeanItemContainer<SelectValue> productNameContainer,
			BeanItemContainer<SelectValue> productTypeContainer,
			BeanItemContainer<SelectValue> policyCodeOrNameContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		NewSearchPolicyViewImpl searchPolicyViewImpl = (NewSearchPolicyViewImpl) view;
		searchPolicyViewImpl.init(searchByContainer, productNameContainer,
				productTypeContainer, policyCodeOrNameContainer);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	public void setViewGOPRegisterClaim(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> searchByContainer,
			BeanItemContainer<SelectValue> productNameContainer,
			BeanItemContainer<SelectValue> productTypeContainer,
			BeanItemContainer<SelectValue> policyCodeOrNameContainer) {
		GMVPView view = views.select(viewClass).get();
//		view.resetView();
//		contentPanel.setContent((Component) view);
//		OPRegisterClaimPolicyViewImpl searchPolicyViewImpl = (OPRegisterClaimPolicyViewImpl) view;
//		searchPolicyViewImpl.init(searchByContainer, productNameContainer,
//				productTypeContainer, policyCodeOrNameContainer);
//		tree.setSelectedView(viewClass);
//		if (selectInNavigationTree) {
//			tree.setSelectedView(viewClass);
//		}
		((AbstractMVPView) view).enter();
	}
	
	/*public void setViewGOPExpiredPolicyClaim(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> searchByContainer,
			BeanItemContainer<SelectValue> productNameContainer,
			BeanItemContainer<SelectValue> productTypeContainer,
			BeanItemContainer<SelectValue> policyCodeOrNameContainer) {
		GMVPView view = views.select(viewClass).get();
		((AbstractMVPView) view).enter();
	}*/

	@Override
	public void setViewHospital(Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> selectedContainer,
			BeanItemContainer<SelectValue> modeOfIntimation,
			BeanItemContainer<SelectValue> intimatedBy) {

		GMVPView view = views.select(viewClass).get();
		UpdateHospitalDetailsViewImpl page = (UpdateHospitalDetailsViewImpl) view;
		page.listOfStates(selectedContainer, modeOfIntimation, intimatedBy);
		page.initView();
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}

	public void setViewG(Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> refferedByDocSrc,
			boolean selectInNavigationTree,boolean isReimburement, BeanItemContainer<SelectValue> cpuCodeContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SubmitSpecialistViewImpl submitSpecialistViewImpl = (SubmitSpecialistViewImpl) view;
		submitSpecialistViewImpl.init(refferedByDocSrc,isReimburement, cpuCodeContainer);
		submitSpecialistViewImpl.setDropDownValues(cpuCodeContainer);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setSubmitSpecialist(Class<? extends MVPView> viewClass,
			SubmitSpecialistTableDTO bean) {
		MVPView view = views.select(viewClass).get();
		SubmitSpecialistPageViewImpl page = (SubmitSpecialistPageViewImpl) view;
		page.initView(bean);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}

	@Override
	public void redirectAndLoad(Class<? extends MVPView> viewClass, String url) {
		LoaderView loaderView = (LoaderView) views.select(viewClass).get();
		((AbstractMVPView) loaderView).enter();
		contentPanel.setContent((Component) loaderView);
		loaderView.initView(url);
	}

	@Override
	public void setClaimRegistrationForm(Class<? extends MVPView> viewClass,
			SearchClaimRegistrationTableDto searchClaimRegistrationTableDto) {
		ClaimRegistrationView view = (ClaimRegistrationView) views.select(
				viewClass).get();
		((AbstractMVPView) view).enter();
		view.resetView();
		view.initView(searchClaimRegistrationTableDto);
		contentPanel.setContent((Component) view);
	}
	
	@Override
	public void setGhiAllowClaimRegistrationForm(Class<? extends MVPView> viewClass,
			SearchClaimRegistrationTableDto searchClaimRegistrationTableDto) {
		GhiAllowClaimRegistrationView view = (GhiAllowClaimRegistrationView) views.select(
				viewClass).get();
		((AbstractMVPView) view).enter();
		view.resetView();
		view.initView(searchClaimRegistrationTableDto);
		contentPanel.setContent((Component) view);
	}

	@Override
	public void setPAClaimRegistrationForm(Class<? extends MVPView> viewClass,
			SearchClaimRegistrationTableDto searchClaimRegistrationTableDto,
			BeanItemContainer<SelectValue> selectValueForCategory) {
		PAClaimRegistrationView view = (PAClaimRegistrationView) views.select(
				viewClass).get();
		((AbstractMVPView) view).enter();
		view.resetView();
		view.initView(searchClaimRegistrationTableDto);
		view.init(selectValueForCategory);
		contentPanel.setContent((Component) view);
	}
	
	@Override
	public void setPreMedicalEnhancementView(
			Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO) {
		MVPView view = views.select(viewClass).get();
		PremedicalEnhancementWizardViewImpl wizard = (PremedicalEnhancementWizardViewImpl) view;
//		if(null == preauthDTO.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement()) {
			wizard.initView(preauthDTO);
			tree.setSelectedView(viewClass);
			contentPanel.setContent((Component) view);
			((AbstractMVPView) view).enter();
//		} else if (preauthDTO.getPreauthDataExtractionDetails().getInterimOrFinalEnhancement() || (preauthDTO.getStatusKey() != null && preauthDTO.getStatusKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS))
//				|| (preauthDTO.getStageKey() != null && preauthDTO.getStageKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE))) {
//			wizard.initView(preauthDTO);
//			tree.setSelectedView(viewClass);
//			contentPanel.setContent((Component) view);
//			((AbstractMVPView) view).enter();
//		} else {
//			getErrorMessage("Already Final Enhancement has been submitted");
//		}

	}

	@Override
	public void setPreauthEnhancementWizardView(
			Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO) {
		MVPView view = views.select(viewClass).get();
		PreauthEnhancementWizardViewImpl wizard = (PreauthEnhancementWizardViewImpl) view;
		wizard.initView(preauthDTO);
		tree.setSelectedView(viewClass);
		//Panel contentPanel = new Panel();
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setWithdrawPreauthView(Class<? extends GMVPView> viewClass,
			SearchWithdrawCashLessProcessTableDTO searchWithdrawPreauthDto,
			NewIntimationDto intimationDto, ClaimDto claimDto,
			BeanItemContainer<SelectValue> selectValueContainer) {
		GMVPView view = views.select(viewClass).get();
		WithdrawPreauthWizardViewImpl wizard = (WithdrawPreauthWizardViewImpl) view;
		wizard.initView(new WithdrawPreauthPageDTO(), searchWithdrawPreauthDto,
				intimationDto, claimDto, selectValueContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setWithdrawPreauthPostProcessView(Class<? extends GMVPView> viewClass,
			SearchWithdrawCashLessPostProcessTableDTO searchWithdrawPreauthDto,
			NewIntimationDto intimationDto, ClaimDto claimDto,
			BeanItemContainer<SelectValue> selectValueContainer,ConvertClaimDTO convertClaimDto,
			BeanItemContainer<SelectValue> conversionContainer) {
		GMVPView view = views.select(viewClass).get();
		WithdrawPreauthPostProcessWizardViewImpl wizard = (WithdrawPreauthPostProcessWizardViewImpl) view;
		wizard.initView(new WithdrawPreauthPostProcessPageDTO(), searchWithdrawPreauthDto,
				intimationDto, claimDto, selectValueContainer,convertClaimDto,conversionContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setPAWithdrawPreauthView(Class<? extends GMVPView> viewClass,
			PASearchWithdrawCashLessProcessTableDTO searchWithdrawPreauthDto,
			NewIntimationDto intimationDto, ClaimDto claimDto,
			BeanItemContainer<SelectValue> selectValueContainer) {
		GMVPView view = views.select(viewClass).get();
		PAWithdrawPreauthWizardViewImpl wizard = (PAWithdrawPreauthWizardViewImpl) view;
		wizard.initView(new PAWithdrawPreauthPageDTO(), searchWithdrawPreauthDto,
				intimationDto, claimDto, selectValueContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}

	
	
	@Override
	public void setDownsizePreauth(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> escalateContainer) {

		GMVPView view = views.select(viewClass).get();
		DownSizePreauthWizardViewImpl wizard = (DownSizePreauthWizardViewImpl) view;
		wizard.initView(preauthDto, selectValueContainer, escalateContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setPADownsizePreauth(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> escalateContainer) {

		GMVPView view = views.select(viewClass).get();
		PADownSizePreauthWizardViewImpl wizard = (PADownSizePreauthWizardViewImpl) view;
		wizard.initView(preauthDto, selectValueContainer, escalateContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}

	@Override
	public void setInvestigationInitiate(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchProcessInvestigationInitiatedTableDTO investigation) {
		try{
			GMVPView view = views.select(viewClass).get();
			view.resetView();
			contentPanel.setContent((Component) view);

			ProcessInvestigationInitiatedViewImpl processInvestigationInitiatedViewImpl = (ProcessInvestigationInitiatedViewImpl) view;

			processInvestigationInitiatedViewImpl.init(investigation);

			if (selectInNavigationTree) {
				tree.setSelectedView(viewClass);
			}
			((AbstractMVPView) view).enter();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void setAcknowledgeInvestigationCompleted(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchAckInvestigationCompletedTableDTO investigationKey) {
		try{
			GMVPView view = views.select(viewClass).get();
			view.resetView();
			contentPanel.setContent((Component) view);

			AcknowledgeInvestigationCompletedViewImpl acknowledgeInvestigationCompletedViewImpl = (AcknowledgeInvestigationCompletedViewImpl) view;

			acknowledgeInvestigationCompletedViewImpl.startup(investigationKey);

			if (selectInNavigationTree) {
				tree.setSelectedView(viewClass);
			}
			((AbstractMVPView) view).enter();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void setInvestigationMasterWizard(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,	InvestigationMasterDTO investigationMasterDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		InvestigationMasterWizardViewImpl investigationMasterWizardViewImpl = (InvestigationMasterWizardViewImpl) view;

		investigationMasterWizardViewImpl.initView(investigationMasterDTO);

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setAssignInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			AssignInvestigatorDto assignInvestigatorDto) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		AssignInvestigationWizardViewImpl assignInvestigationViewImpl = (AssignInvestigationWizardViewImpl) view;

		assignInvestigationViewImpl.initView(assignInvestigatorDto);

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setReAssignInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			AssignInvestigatorDto assignInvestigatorDto) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		ReAssignInvestigationWizardViewImpl reAssignInvestigationViewImpl = (ReAssignInvestigationWizardViewImpl) view;

		reAssignInvestigationViewImpl.initView(assignInvestigatorDto);

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setAcknowledgeDocReceivedWizardView(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO) {
		GMVPView view = views.select(viewClass).get();
		
		
		AcknowledgeDocReceivedWizardViewImpl wizard = (AcknowledgeDocReceivedWizardViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setPAAcknowledgeDocReceivedWizardView(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO) {
		GMVPView view = views.select(viewClass).get();
		
		
		PAAcknowledgeDocumentWizardviewImpl wizard = (PAAcknowledgeDocumentWizardviewImpl) view;
		wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	

	@Override
	public void setAddAditionalDocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO) {
		GMVPView view = views.select(viewClass).get();
		
		
		AddAditionalDocumentsViewImpl wizard = (AddAditionalDocumentsViewImpl) view;
	//	wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	
	@Override
	public void setPAAddAditionalDocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO) {
		GMVPView view = views.select(viewClass).get();
		
		
		PAAddAdditionalDocumentsWizardViewImpl wizard = (PAAddAdditionalDocumentsWizardViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}

	/*@Override
	public void setSearchOrUploadDocumentsWizard(
			Class<? extends GMVPView> viewClass, UploadDocumentDTO uploadDto) {*/
	@Override
	public void setSearchOrUploadDocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO uploadDto) {
		GMVPView view = views.select(viewClass).get();
		
		
		SearchUploadDocumentsWizardViewImpl wizard = (SearchUploadDocumentsWizardViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(uploadDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setSearchOrUploadDocumentsForAckNotReceivedWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO uploadDto) {
		GMVPView view = views.select(viewClass).get();
		
		
		UploadDocumentsForAckNotReceivedWizardViewImpl wizard = (UploadDocumentsForAckNotReceivedWizardViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(uploadDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	
	
	@Override
	public void setCreateRODWizardView(Class<? extends GMVPView> viewClass,
			ReceiptOfDocumentsDTO rodDTO) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		CreateRODWizardViewImpl wizard = (CreateRODWizardViewImpl) view;
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setPACreateRODWizardView(Class<? extends GMVPView> viewClass,
			ReceiptOfDocumentsDTO rodDTO) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PACreateRODWizardViewImpl wizard = (PACreateRODWizardViewImpl) view;
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setBillEntryWizard(Class<? extends GMVPView> viewClass,
			ReceiptOfDocumentsDTO rodDTO) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		BillEntryWizardViewImpl wizard = (BillEntryWizardViewImpl) view;
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setPABillEntryWizard(Class<? extends GMVPView> viewClass,
			ReceiptOfDocumentsDTO rodDTO) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PAEnterBillDetailsWizardViewImpl wizard = (PAEnterBillDetailsWizardViewImpl) view;
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setProcessClaimRequestBenefitsWizard(Class<? extends GMVPView> viewClass,
			ReceiptOfDocumentsDTO rodDTO) {

		GMVPView view = views.select(viewClass).get();
		//view.resetView();
		ProcessClaimRequestBenefitsWizardViewImpl wizard = (ProcessClaimRequestBenefitsWizardViewImpl) view;
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}

	@Override
	public void setDecideOnProcessDraftQueryWizardView(Class<? extends GMVPView> viewClass,
			ClaimQueryDto claimQueryDto) {

		GMVPView view = views.select(viewClass).get();
		DecideOnProcessDraftQueryWizardViewImpl wizard = (DecideOnProcessDraftQueryWizardViewImpl) view;
		wizard.initView(claimQueryDto);
		tree.setSelectedView(viewClass);

		//		contentPanel.setCaption("Process Draft Query Letter");

		contentPanel.setContent((Component) view);
		
		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setDecideOnProcessDraftPAQueryWizardView(Class<? extends GMVPView> viewClass,
			ClaimQueryDto claimQueryDto) {

		GMVPView view = views.select(viewClass).get();
		DecideOnProcessDraftPAQueryWizardViewImpl wizard = (DecideOnProcessDraftPAQueryWizardViewImpl) view;
		wizard.initView(claimQueryDto);
		tree.setSelectedView(viewClass);

		//		contentPanel.setCaption("Process Draft Query Letter");

		contentPanel.setContent((Component) view);
		
		((AbstractMVPView) view).enter();

	}
	
//	@Override
//	public void setDecideOnDraftQueryDetailWizardView(Class<? extends GMVPView> viewClass,
//			SearchDraftQueryLetterTableDTO  searchDraftQueryLetterTableDTO){
//		
//		GMVPView view = views.select(viewClass).get();
//		DecideOnDraftQueryDetailWizardViewImpl wizard = (DecideOnDraftQueryDetailWizardViewImpl) view;
//		wizard.initView(searchDraftQueryLetterTableDTO);
//		tree.setSelectedView(viewClass);
//
//		//		contentPanel.setCaption("Process Draft Query Letter");
//
//		contentPanel.setContent((Component) view);
//		
//		((AbstractMVPView) view).enter();
//		
//	}
	
	@Override
	public void setDecideOnDraftPAQueryDetailWizardView(Class<? extends GMVPView> viewClass,
			SearchDraftQueryLetterTableDTO  searchDraftQueryLetterTableDTO){
		
		GMVPView view = views.select(viewClass).get();
		DecideOnDraftPAQueryDetailWizardViewImpl wizard = (DecideOnDraftPAQueryDetailWizardViewImpl) view;
		wizard.initView(searchDraftQueryLetterTableDTO);
		tree.setSelectedView(viewClass);

		contentPanel.setContent((Component) view);
		
		((AbstractMVPView) view).enter();		
	}
	
	@Override
	public void setMedicalApprovalZonalReview(
			Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDTO, Boolean secondaryParameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		MedicalApprovalZonalReviewWizardViewImpl wizard = (MedicalApprovalZonalReviewWizardViewImpl) view;
		wizard.initView(preauthDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	public void setDraftQueryLetterDetailView(
			Class<? extends GMVPView> viewClass, SearchDraftQueryLetterTableDTO searchDraftQueryLetterTableDTO) {
		GMVPView view = views.select(viewClass).get();
		DecideOnDraftQueryDetailWizardViewImpl draftQueryLetterDetailView = (DecideOnDraftQueryDetailWizardViewImpl) view;
		
		draftQueryLetterDetailView.initView(searchDraftQueryLetterTableDTO);
		
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setGenerateReminderLetterDetailView(
			Class<? extends GMVPView> viewClass,
			SearchDraftQueryLetterTableDTO searchDraftQueryLetterTableDTO) {
		GMVPView view = views.select(viewClass).get();
		GenerateReminderLetterDetailViewImpl generateReminderLetterDetailView = (GenerateReminderLetterDetailViewImpl) view;
		
		generateReminderLetterDetailView.initView(searchDraftQueryLetterTableDTO);
		
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setUploadInvestigationReports(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchUploadInvesticationTableDTO investigationKey) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		UploadInvestigationReportViewImpl uploadInvestigationReportViewImpl = (UploadInvestigationReportViewImpl) view;
		uploadInvestigationReportViewImpl.init(investigationKey);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setDraftRejectionLetterDetailView(
			Class<? extends GMVPView> viewClass, SearchDraftRejectionLetterTableDTO searchDraftRejectionLetterTableDTO) {
		GMVPView view = views.select(viewClass).get();
		DraftRejectionLetterDetailViewImpl draftRejectionLetterDetailView = (DraftRejectionLetterDetailViewImpl) view;
		
		draftRejectionLetterDetailView.initView(searchDraftRejectionLetterTableDTO);
		
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setDecideOnRejectionWizardView(
			Class<? extends GMVPView> viewClass,
			ClaimRejectionDto claimRejectionDto) {
		
		GMVPView view = views.select(viewClass).get();
		DecideOnRejectionWizardViewImpl wizard = (DecideOnRejectionWizardViewImpl) view;
		wizard.initView(claimRejectionDto);
		tree.setSelectedView(viewClass);
//		contentPanel.setCaption("Process Draft Rejection Letter");
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();		
		
	}
	
	@Override
	public void setDraftPARejectionLetterDetailView(
			Class<? extends GMVPView> viewClass, SearchDraftRejectionLetterTableDTO searchDraftRejectionLetterTableDTO) {
		GMVPView view = views.select(viewClass).get();
		DraftPARejectionLetterDetailViewImpl draftRejectionLetterDetailView = (DraftPARejectionLetterDetailViewImpl) view;
		
		draftRejectionLetterDetailView.initView(searchDraftRejectionLetterTableDTO);
		
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setDecideOnPARejectionWizardView(
			Class<? extends GMVPView> viewClass,
			ClaimRejectionDto claimRejectionDto) {
		
		GMVPView view = views.select(viewClass).get();
		DecideOnPARejectionWizardViewImpl wizard = (DecideOnPARejectionWizardViewImpl) view;
		wizard.initView(claimRejectionDto);
		tree.setSelectedView(viewClass);
//		contentPanel.setCaption("Process Draft Rejection Letter");
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();		
		
	}
	
	@Override
	public void setSubmitSpecialistAdvise(Class<? extends MVPView> viewClass,
			SubmitSpecialistTableDTO bean) {
		MVPView view = views.select(viewClass).get();
		SubmitSpecialistAdviseViewImpl page = (SubmitSpecialistAdviseViewImpl) view;
		page.initView(bean);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setProcessClaimRequest(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ClaimRequestWizardViewImpl wizard = (ClaimRequestWizardViewImpl) view;
		wizard.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setSkipZMR(Class<? extends GMVPView> viewClass,BeanItemContainer<SelectValue> cpuCodeContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SkipZMRViewImpl wizard = (SkipZMRViewImpl) view;
		wizard.initView(cpuCodeContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
//------------------------------------------------------------------------R3-Search-Narenj------------------------------------------------------
	
	public void setViewFieldVisit(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessFieldVisitViewImpl searchProcessFieldVisitViewImpl = (SearchProcessFieldVisitViewImpl) view;
		searchProcessFieldVisitViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	public void setPAViewFieldVisit(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchProcessFieldVisitViewImpl paSearchProcessFieldVisitViewImpl = (PASearchProcessFieldVisitViewImpl) view;
		paSearchProcessFieldVisitViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewInvestigationMaster(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			 BeanItemContainer<SelectValue> investigatorType,BeanItemContainer<SelectValue> investigatorName,BeanItemContainer<SelectValue> privateInvestigatorName) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		InvestigationMasterViewImpl investigationMasterViewImpl = (InvestigationMasterViewImpl) view;
		investigationMasterViewImpl.init(investigatorType,investigatorName,privateInvestigatorName);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewAssignInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> claimTypeContainer,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> statusByInvestigationState) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchAssignInvestigationViewImpl searchAssignInvestigationViewImpl = (SearchAssignInvestigationViewImpl) view;
		searchAssignInvestigationViewImpl.init(parameter,claimTypeContainer,selectValueForPriority,statusByStage,statusByInvestigationState);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewReAssignInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> claimTypeContainer,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchReAssignInvestigationViewImpl searchReAssignInvestigationViewImpl = (SearchReAssignInvestigationViewImpl) view;
		searchReAssignInvestigationViewImpl.init(parameter,claimTypeContainer,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	
	@Override
	public void setViewProcessInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> claimTypeContainer,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessInvestigationInitiatedViewImpl searchProcessInvestigationInitiatedViewImpl = (SearchProcessInvestigationInitiatedViewImpl) view;
		searchProcessInvestigationInitiatedViewImpl.init(parameter,claimTypeContainer,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setViewAcknowledgementInvestigation(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> claimTypeContainer,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchAckInvestigationCompletedViewImpl searchAckInvestigationCompletedViewImpl = (SearchAckInvestigationCompletedViewImpl) view;
		searchAckInvestigationCompletedViewImpl.init(parameter,claimTypeContainer,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewUploadInvestigation(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
		BeanItemContainer<SelectValue> claimTypeContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchUploadInvesticationViewImpl searchUploadInvestigation = (SearchUploadInvesticationViewImpl) view;
		searchUploadInvestigation.init(claimTypeContainer);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setViewCreateROD(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchCreateRODViewImpl searchCreateRODViewImpl = (SearchCreateRODViewImpl) view;
		searchCreateRODViewImpl.init(parameter,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setPAViewCreateROD(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchCreateRODViewImpl searchCreateRODViewImpl = (PASearchCreateRODViewImpl) view;
		searchCreateRODViewImpl.init(parameter,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewPaymentProcessCpu(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> year,
			BeanItemContainer<SelectValue> cpuLotNo,
			BeanItemContainer<SelectValue> status,
			BeanItemContainer<SelectValue> branch) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PaymentProcessCpuViewImpl paymentProcessCpuViewImpl = (PaymentProcessCpuViewImpl) view;
		paymentProcessCpuViewImpl.init(cpu,year,cpuLotNo,status,branch);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
		
		
	}
	
	@Override
	public void setViewPaymentProcessCpuPage(Class<PaymentProcessCpuPageView> class1,
			boolean b, PaymentProcessCpuPageDTO pageDto) {
		
		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PaymentProcessCpuPageViewImpl paymentProcessCpuViewImpl = (PaymentProcessCpuPageViewImpl) view;
		paymentProcessCpuViewImpl.init(pageDto);
		tree.setSelectedView(class1);
		if (b) {
			tree.setSelectedView(class1);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	
	
	@Override
	public void setViewPaymentProcess(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> year,
			BeanItemContainer<SelectValue> cpuLotNo,
			BeanItemContainer<SelectValue> status,
			BeanItemContainer<SelectValue> branch) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PaymentProcessViewImpl paymentProcessViewImpl = (PaymentProcessViewImpl) view;
		paymentProcessViewImpl.init(cpu,year,cpuLotNo,status,branch);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
		
		
	}
	
	
	@Override
	
	public void setViewHospitalWiseReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> dateType) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		HospitalWiseReportViewImpl hospitalWiseReportViewImpl = (HospitalWiseReportViewImpl) view;
		//hospitalWiseReportViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	
	

	
	
	public void setViewMedicalMailReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> dateType) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		MedicalMailReportViewImpl hospitalWiseReportViewImpl = (MedicalMailReportViewImpl) view;
		//hospitalWiseReportViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	

	public void setViewHelpDeskStatusReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpu ,BeanItemContainer<SelectValue> claimType) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		HelpDeskStatusReportViewImpl hospitalWiseReportViewImpl = (HelpDeskStatusReportViewImpl) view;
		hospitalWiseReportViewImpl.init(cpu,claimType);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	public void setViewHospitalIntimationStatusReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		HospitalIntimationReportStatusViewImpl hospitalIntimationViewImpl = (HospitalIntimationReportStatusViewImpl) view;
		//hospitalWiseReportViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	

	@Override
	public void setViewDailyReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> reportType) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		DailyReportViewImpl FVRAssignmentReportViewImpl = (DailyReportViewImpl) view;
		//FVRAssignmentReportViewImpl.init(cpuCode, reportType);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	
	
	
	
	@Override
	public void setViewFvrReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> reportType,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> fvrCpuCode) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		FVRAssignmentReportViewImpl FVRAssignmentReportViewImpl = (FVRAssignmentReportViewImpl) view;
		FVRAssignmentReportViewImpl.init(cpuCode, reportType,claimType,fvrCpuCode);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewSearchOrUploadDocuments(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> type) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchUploadDocumentsViewImpl searchUploadDocumentsViewImpl = (SearchUploadDocumentsViewImpl) view;
		searchUploadDocumentsViewImpl.init(type);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	
		
	}
	
	@Override
	public void setViewSearchOrUploaddocumentsForAckNotReceived(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> type) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		UploadDocumentsForAckNotReceivedViewImpl searchUploadDocumentsViewImpl = (UploadDocumentsForAckNotReceivedViewImpl) view;
		searchUploadDocumentsViewImpl.init(type);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	
		
	}
	
	
	
	@Override
	public void setViewCpuWisePerformanceReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpuCode) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		CpuWisePerformanceReportViewImpl cpuWiseReportViewImpl = (CpuWisePerformanceReportViewImpl) view;
		//FVRAssignmentReportViewImpl.init(cpuCode);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	
	
	
	@Override
	public void setViewAgentBrokerReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		AgentBrokerReportViewImpl AgentBrokerReportViewImpl = (AgentBrokerReportViewImpl) view;
		//FVRAssignmentReportViewImpl.init(cpuCode);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	
	@Override
	public void setViewOpClaimReport(Class<OPClaimReportView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		OPClaimReportViewImpl AgentBrokerReportViewImpl = (OPClaimReportViewImpl) view;
		//FVRAssignmentReportViewImpl.init(cpuCode);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	
	
	
	@Override
	public void setViewBillReceivedReport(Class<BillReceivedReportView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		BillReceivedReportViewImpl AgentBrokerReportViewImpl = (BillReceivedReportViewImpl) view;
		//FVRAssignmentReportViewImpl.init(cpuCode);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	
	

	@Override
	public void setViewEnterBillDetails(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> billClassification) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchEnterBillDetailViewImpl searchEnterBillDetailViewImpl = (SearchEnterBillDetailViewImpl) view;
		searchEnterBillDetailViewImpl.init(parameter,selectValueForPriority,statusByStage,billClassification);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setPAViewEnterBillDetails(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchEnterBillDetailsViewImpl searchEnterBillDetailViewImpl = (PASearchEnterBillDetailsViewImpl) view;
		searchEnterBillDetailViewImpl.init(parameter,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewDefautQuery(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchDraftQueryLetterViewImpl searchEnterBillDetailViewImpl = (SearchDraftQueryLetterViewImpl) view;
		searchEnterBillDetailViewImpl.init(parameter,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewDefaultPAQuery(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchDraftPAQueryLetterViewImpl searchEnterBillDetailViewImpl = (SearchDraftPAQueryLetterViewImpl) view;
		searchEnterBillDetailViewImpl.init(parameter,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewProcessDefautQuery(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessDraftQueryViewImpl searchProcessDraftQueryViewImpl = (SearchProcessDraftQueryViewImpl) view;
		searchProcessDraftQueryViewImpl.init(parameter,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewProcessDefaultPAQuery(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessDraftPAQueryViewImpl searchProcessDraftQueryViewImpl = (SearchProcessDraftPAQueryViewImpl) view;
		searchProcessDraftQueryViewImpl.init(parameter,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewGenerateReminderLetter(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			Map<String,Object> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchGenerateRemainderViewImpl searchGenerateRemainderViewImpl = (SearchGenerateRemainderViewImpl) view;
		searchGenerateRemainderViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewGeneratePAReminderLetter(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			Map<String,Object> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchGeneratePARemainderViewImpl searchGeneratePARemainderViewImpl = (SearchGeneratePARemainderViewImpl) view;
		searchGeneratePARemainderViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewDefautRejectionLetter(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchDraftRejectionLetterViewImpl searchDraftRejectionLetterViewImpl = (SearchDraftRejectionLetterViewImpl) view;
		searchDraftRejectionLetterViewImpl.init(parameter,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewProcessDefautRejection(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessDraftRejectionViewImpl searchProcessDraftRejectionViewImpl = (SearchProcessDraftRejectionViewImpl) view;
		searchProcessDraftRejectionViewImpl.init(parameter,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewDefaultPARejectionLetter(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchDraftPARejectionLetterViewImpl searchDraftRejectionLetterViewImpl = (SearchDraftPARejectionLetterViewImpl) view;
		searchDraftRejectionLetterViewImpl.init(parameter,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewProcessDefaultPARejection(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessDraftPARejectionViewImpl searchProcessDraftRejectionViewImpl = (SearchProcessDraftPARejectionViewImpl) view;
		searchProcessDraftRejectionViewImpl.init(parameter,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewSubmitSpecialistAdvice(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchSubmitSpecialistAdviseViewImpl searchSubmitSpecialistAdviseViewImpl = (SearchSubmitSpecialistAdviseViewImpl) view;
		searchSubmitSpecialistAdviseViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setViewsProcessClaimRequestZonalReview(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,BeanItemContainer<SelectValue> typeContainer
			,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessClaimRequestZonalViewImpl searchProcessClaimRequestZonalViewImpl = (SearchProcessClaimRequestZonalViewImpl) view;
		searchProcessClaimRequestZonalViewImpl.init(intimationSource, hospitalType, networkHospitalType,typeContainer,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewsProcessClaimRequest(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType,BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode
			,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,
			BeanItemContainer<SelectValue> claimType, SearchProcessClaimRequestFormDTO dto,String screenName){
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessClaimRequestViewImpl searchProcessClaimRequestViewImpl = (SearchProcessClaimRequestViewImpl) view;
		searchProcessClaimRequestViewImpl.initViewForReferesh(dto, (dto != null ? true : false));
		searchProcessClaimRequestViewImpl.init(intimationSource, hospitalType, networkHospitalType, treatementType,typeContainer,productName,cpuCode,selectValueForPriority,statusByStage,claimType,screenName);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	
	@Override
	public void setViewsPAProcessClaimRequest(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType,BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode
			,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, SearchProcessClaimRequestFormDTO dto,String screenName){
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchProcessClaimRequestViewImpl paSearchProcessClaimRequestViewImpl = (PASearchProcessClaimRequestViewImpl) view;
		paSearchProcessClaimRequestViewImpl.initViewForReferesh(dto, (dto != null ? true : false));
		paSearchProcessClaimRequestViewImpl.init(intimationSource, hospitalType, networkHospitalType, treatementType,typeContainer,productName,cpuCode,selectValueForPriority,statusByStage,screenName);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	@Override
	public void setViewsProcessClaimBilling(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode,BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> claimType,BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessClaimBillingViewImpl searchProcessClaimBillingViewImpl = (SearchProcessClaimBillingViewImpl) view;
		searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,claimType,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewsPAProcessClaimBilling(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode,BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> coverContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchProcessClaimBillingViewImpl searchProcessClaimBillingViewImpl = (PASearchProcessClaimBillingViewImpl) view;
		searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage,coverContainer);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewsPAProcessClaimAprHos(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode,BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchProcessClaimAprovalHosViewImpl searchProcessClaimBillingViewImpl = (PASearchProcessClaimAprovalHosViewImpl) view;
		searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewsPAProcessClaimAprNonHos(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode,BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> coverContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchProcessClaimAprovalNonHosViewImpl searchProcessClaimBillingViewImpl = (PASearchProcessClaimAprovalNonHosViewImpl) view;
		searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage,coverContainer);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewsPAHospCoverProcessClaimBilling(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode,BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,BeanItemContainer<SelectValue> coverContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchHospCoverProcessClaimBillingViewImpl searchProcessClaimBillingViewImpl = (PASearchHospCoverProcessClaimBillingViewImpl) view;
		searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage,coverContainer);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	
	
	@Override
	public void setViewsProcessClaimRequestBenefits(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessClaimRequestBenefitsViewImpl searchProcessClaimRequestBenefitsViewImpl = (SearchProcessClaimRequestBenefitsViewImpl) view;
		searchProcessClaimRequestBenefitsViewImpl.init(intimationSource, hospitalType, networkHospitalType, treatementType);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewsProcessClaimRequestBenefitsFinace(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessClaimRequestBenefitsFinancViewImpl searchProcessClaimRequestBenefitsFinancViewImpl = (SearchProcessClaimRequestBenefitsFinancViewImpl) view;
		searchProcessClaimRequestBenefitsFinancViewImpl.init(intimationSource, hospitalType, networkHospitalType, treatementType);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewsProcessClaimClaimOP(
			Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> claimTypeContainer, BeanItemContainer<SelectValue> pioCodeContainer ) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessOPClaimRequestViewImpl searchProcessClaimOPViewImpl = (SearchProcessOPClaimRequestViewImpl) view;
		searchProcessClaimOPViewImpl.init(claimTypeContainer,pioCodeContainer);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setProcessClaimBilling(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		BillingWizardViewImpl billingWizardViewImpl = (BillingWizardViewImpl) view;
		billingWizardViewImpl.initView(reimbursementDTO);
		billingWizardViewImpl.addShortcutListener(billingWizardViewImpl.setPolicyScheduleShortcut());
		billingWizardViewImpl.addShortcutListener(billingWizardViewImpl.setWizardBackShortcut());
		billingWizardViewImpl.addShortcutListener(billingWizardViewImpl.setWizardNextShortcut());
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	@Override
	public void setPAProcessClaimBilling(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PABillingWizardViewImpl billingWizardViewImpl = (PABillingWizardViewImpl) view;
		billingWizardViewImpl.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	
	@Override
	public void setPAHospCoverProcessClaimBilling(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PAHospCoverBillingWizardViewImpl billingWizardViewImpl = (PAHospCoverBillingWizardViewImpl) view;
		billingWizardViewImpl.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setUploadTranslatedDocuments(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, boolean remburementFlag) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessTranslationViewImpl submitSpecialistViewImpl = (SearchProcessTranslationViewImpl) view;
		submitSpecialistViewImpl.init(remburementFlag);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setProcessClaimFinancial(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		FinancialWizardViewImpl financialWizardViewImpl = (FinancialWizardViewImpl) view;
		financialWizardViewImpl.initView(reimbursementDTO);
		financialWizardViewImpl.addShortcutListener(financialWizardViewImpl.setPolicyScheduleShortcut());
		financialWizardViewImpl.addShortcutListener(financialWizardViewImpl.setWizardBackShortcut());
		financialWizardViewImpl.addShortcutListener(financialWizardViewImpl.setWizardNextShortcut());
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	
	
	@Override
	public void setPaNonHospProcessClaimFinancial(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PANonHospFinancialWizardViewImpl financialWizardViewImpl = (PANonHospFinancialWizardViewImpl) view;
		financialWizardViewImpl.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	
	
	@Override
	public void setFileUploadTranslatedView(Class<UploadTranslatedDocumentView> viewClass,
			SearchProcessTranslationTableDTO fileUploadDTO) {
		MVPView view = views.select(viewClass).get();
		UploadTranslatedDocumentViewImpl wizard = (UploadTranslatedDocumentViewImpl) view;
		wizard.initView(fileUploadDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}

	@Override
	public void setSearchApproveClaim(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchApproveClaimViewImpl searchApproveClaimViewImpl = (SearchApproveClaimViewImpl) view;
		searchApproveClaimViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setSearchProcessClaim(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
	SearchProcessClaimViewImpl searchProcessClaimViewImpl = (SearchProcessClaimViewImpl) view;
		searchProcessClaimViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setProcessRRCRequestSearch(Class<SearchProcessRRCRequestView> viewClass,
			boolean selectInNavigationTree, BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> rrcRequestType) 
	{
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchProcessRRCRequestViewImpl searchProcessClaimViewImpl = (SearchProcessRRCRequestViewImpl) view;
		searchProcessClaimViewImpl.init(cpu , rrcRequestType);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setOPRegisterClaim(Class<? extends GMVPView> viewClass,
			OutPatientDTO mainDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		OPRegisterClaimWizardViewImpl registerClaimViewImpl = (OPRegisterClaimWizardViewImpl) view;
		registerClaimViewImpl.initView(mainDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setOPRegisterClaimView(Class<? extends GMVPView> viewClass,
			Policy policy) {
		OutPatientDTO dto = new OutPatientDTO();
		dto.setPolicy(policy);
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		OPRegisterClaimWizardViewImpl registerClaimViewImpl = (OPRegisterClaimWizardViewImpl) view;
		registerClaimViewImpl.initView(dto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	
	/*@Override
	public void setOPClaimForPolicyExpired(Class<? extends GMVPView> viewClass,
			OutPatientDTO mainDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		OPRegisterClaimWizardViewImpl registerClaimViewImpl = (OPRegisterClaimWizardViewImpl) view;
		registerClaimViewImpl.initView(mainDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setOPExpiredPolicyClaimView(Class<? extends GMVPView> viewClass,
			Policy policy) {
		OutPatientDTO dto = new OutPatientDTO();
		dto.setPolicy(policy);
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		OPRegisterClaimWizardViewImpl registerClaimViewImpl = (OPRegisterClaimWizardViewImpl) view;
		registerClaimViewImpl.initView(dto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	*/
	@Override
	public void setProcessRRCRequestWizard(
			Class<ProcessRRCRequestDataExtractionWizard> viewClass,
			RRCDTO rrcDTO) {
			GMVPView view = views.select(viewClass).get();
			
			ProcessRRCRequestWizardViewImpl wizard = (ProcessRRCRequestWizardViewImpl) view;
			//wizard.initDTO(rodDTO);
			view.resetView();
			wizard.initView(rrcDTO);
			tree.setSelectedView(viewClass);
			contentPanel.setContent((Component) view);
			((AbstractMVPView) view).enter();
	}

	@Override
	public void setProcessClaimOP(Class<? extends GMVPView> viewClass,
			OutPatientDTO mainDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessOPClaimWizardViewImpl registerClaimViewImpl = (ProcessOPClaimWizardViewImpl) view;
		registerClaimViewImpl.initView(mainDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setReviewRequestRRCSearch(Class<SearchReviewRRCRequestView> viewClass,
			boolean selectInNavigationTree, BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> rrcRequestType, BeanItemContainer<SelectValue> rrcEligibility) 
	{
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchReviewRRCRequestViewImpl searchReviewRequestViewImpl = (SearchReviewRRCRequestViewImpl) view;
		searchReviewRequestViewImpl.init(cpu , rrcRequestType,rrcEligibility);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setReviewRRCRequestWizard(
			Class<ReviewRRCRequestDataExtractionWizard> viewClass, RRCDTO rrcDTO) {
		GMVPView view = views.select(viewClass).get();
		
		ReviewRRCRequestWizardViewImpl wizard = (ReviewRRCRequestWizardViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(rrcDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setModifyRequestRRCSearch(
			Class<SearchModifyRRCRequestView> viewClass, boolean b,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueForRRCRequestType,
			BeanItemContainer<SelectValue> selectValueForEligibility) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchModifyRRCRequestViemImpl searchModifyRRCRequestViewImpl = (SearchModifyRRCRequestViemImpl) view;
		searchModifyRRCRequestViewImpl.init(selectValueContainerForCPUCode , selectValueForRRCRequestType,selectValueForEligibility);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
            
	@Override
	public void setModifyRRCRequestWizard(
			Class<ModifyRRCRequestDataExtractionWizard> viewClass, RRCDTO rrcDTO) {
		GMVPView view = views.select(viewClass).get();
		
		ModifyRRCRequestWizardViewImpl wizard = (ModifyRRCRequestWizardViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(rrcDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setRRCRequestSearch(
			Class<SearchRRCRequestView> viewClass, boolean b,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueForRRCRequestType,
			BeanItemContainer<SelectValue> selectValueForEligibility) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchRRCRequestViewImpl searchModifyRRCRequestViewImpl = (SearchRRCRequestViewImpl) view;
		searchModifyRRCRequestViewImpl.init(selectValueContainerForCPUCode , selectValueForRRCRequestType,selectValueForEligibility);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setCPUwisePreauthSearch(Class<CPUwisePreauthReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> cPUCodeContainer) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		CPUwisePreauthReportViewImpl searchCPUWisePreauthViewImpl = (CPUwisePreauthReportViewImpl) view;
		searchCPUWisePreauthViewImpl.init(cPUCodeContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setMedicalAuditClaimStatusSearch(Class<MedicalAuditClaimStatusReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> statusContainer) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		MedicalAuditClaimStatusReportViewImpl searchMedicalAuditClaimStatusViewImpl = (MedicalAuditClaimStatusReportViewImpl) view;
		searchMedicalAuditClaimStatusViewImpl.init(statusContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setMedicalAuditCashlessIssuanceReportSearch(Class<MedicalAuditCashlessIssuanceReportView> viewClass, boolean b, 
			BeanItemContainer<SelectValue> statusContainer){
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		MedicalAuditCashlessIssuanceReportViewImpl searchMedicalAuditCashlessIssuanceViewImpl = (MedicalAuditCashlessIssuanceReportViewImpl) view;
		searchMedicalAuditCashlessIssuanceViewImpl.init(statusContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setEmployeeTypeExecutiveStatusSearch(Class<ExecutiveStatusDetailReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> empTypeContainer,BeanItemContainer<SelectValue> cPUCodeContainer,BeanItemContainer<SelectValue> empContainer) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ExecutiveStatusDetailReportViewImpl searchExecutiveDetailStatusImpl = (ExecutiveStatusDetailReportViewImpl) view;		
		tree.setSelectedView(viewClass);
		searchExecutiveDetailStatusImpl.showSearchEmpwiseReport(empTypeContainer,cPUCodeContainer,empContainer);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	public void getErrorMessage(String eMsg){
			
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		}

	@Override
	public void setDownsizePreauthRequest(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> escalateContainer) {

	GMVPView view = views.select(viewClass).get();
	DownsizePreauthRequestWizardViewImpl wizard = (DownsizePreauthRequestWizardViewImpl) view;
	wizard.initView(preauthDto, selectValueContainer, escalateContainer);
	tree.setSelectedView(viewClass);
	contentPanel.setContent((Component) view);

	((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setPADownsizePreauthRequest(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> escalateContainer) {

	GMVPView view = views.select(viewClass).get();
	PADownsizePreauthRequestWizardViewImpl wizard = (PADownsizePreauthRequestWizardViewImpl) view;
	wizard.initView(preauthDto, selectValueContainer, escalateContainer);
	tree.setSelectedView(viewClass);
	contentPanel.setContent((Component) view);

	((AbstractMVPView) view).enter();
		
	}
	
	

	@Override
	public void setViewPaymentProcessTransaction(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) {
		
		
	}		

	public void setExecutiveStatusSummaryView(
			Class<ExecutiveStatusSummaryReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> cPUCodeContainer,
			BeanItemContainer<SelectValue> empTypeContainer,
			BeanItemContainer<SelectValue> empContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ExecutiveStatusSummaryReportViewImpl searchExecutiveStatusSummaryViewImpl = (ExecutiveStatusSummaryReportViewImpl) view;		
		tree.setSelectedView(viewClass);
		searchExecutiveStatusSummaryViewImpl.setupDroDownValues(cPUCodeContainer,empTypeContainer,empContainer);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setAdminDashBoardView(
			Class<CallcenterDashBoardReportView> viewClass, boolean b,
			boolean auditView) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		CallcenterDashBoardReportViewImpl callcenterDashBoardViewImpl = (CallcenterDashBoardReportViewImpl) view;		
		tree.setSelectedView(viewClass);
		callcenterDashBoardViewImpl.setAuditView(auditView);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

	public void setPaymentWizardView(Class<? extends GMVPView> viewClass,
			PaymentProcessCpuPageDTO rodDTO) {
		GMVPView view = views.select(viewClass).get();
		
		
		PaymentProcessCpuPageViewImpl wizard = (PaymentProcessCpuPageViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.init(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

		
	}

	public void setClaimsStatusReportView(
			Class<ClaimsStatusReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> cPUCodeContainer,
			BeanItemContainer<SelectValue> clmStatusContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ClaimsStatusReportViewImpl searchClaimsStatusViewImpl = (ClaimsStatusReportViewImpl) view;		
		tree.setSelectedView(viewClass);
		searchClaimsStatusViewImpl.setupDroDownValues(cPUCodeContainer,clmStatusContainer);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	public void setClaimsDailyReportView(
			Class<ClaimsDailyReportView> viewClass, boolean b,
			BeanItemContainer<SelectValue> cPUCodeContainer,
			BeanItemContainer<SelectValue> clmTypeContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ClaimsDailyReportViewImpl searchClmsDailyReportViewImpl = (ClaimsDailyReportViewImpl) view;		
		tree.setSelectedView(viewClass);
		searchClmsDailyReportViewImpl.setupDroDownValues(cPUCodeContainer,clmTypeContainer);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	
	

@Override
public void setCloseClaimSearchBasedRODLevelView(
		Class<? extends GMVPView> viewClass,
		boolean selectInNavigationTree, SearchCloseClaimTableDTORODLevel tableDTO) {
	
	GMVPView view = views.select(viewClass).get();
	view.resetView();
	CloseClaimRodLevelView closeClaimView = (CloseClaimRodLevelView) view;
	tree.setSelectedView(viewClass);
	closeClaimView.init(tableDTO);
	contentPanel.setContent((Component) view);
	((AbstractMVPView) view).enter();
	
}

@Override
public void setCloseClaimInProcessView(
		Class<? extends GMVPView> viewClass,
		boolean selectInNavigationTree, SearchCloseClaimTableDTORODLevel tableDTO) {
	
	GMVPView view = views.select(viewClass).get();
	view.resetView();
	CloseClaimInProcessView closeClaimView = (CloseClaimInProcessView) view;
	tree.setSelectedView(viewClass);
	closeClaimView.init(tableDTO);
	contentPanel.setContent((Component) view);
	((AbstractMVPView) view).enter();
	
}

@Override
public void setViewForAcknowledgementSearch(
		Class<SearchAcknowledgeHospitalCommunicationView> class1,
		BeanItemContainer<SelectValue> selectValueContainer, boolean selectInnavigationTree,
		boolean c) {
	
	GMVPView view = views.select(class1).get();
	view.resetView();
	contentPanel.setContent((Component) view);
	SearchAcknowledgeHospitalCommunicationViewImpl communicationViewImpl = (SearchAcknowledgeHospitalCommunicationViewImpl) view;
	communicationViewImpl.setCpuCode(selectValueContainer);
	tree.setSelectedView(class1);
	if (selectInnavigationTree) {
		tree.setSelectedView(class1);
	}
	((AbstractMVPView) view).enter();
	
	
}

@Override
public void setReopenClaimSearchBasedView(Class<ReOpenRodLevelClaimView> class1,
		boolean b, SearchReOpenClaimRodLevelTableDTO tableDTO) {
	
	GMVPView view = views.select(class1).get();
	view.resetView();
	ReOpenRodLevelClaimView reOpenClaimView = (ReOpenRodLevelClaimView) view;
	tree.setSelectedView(class1);
	reOpenClaimView.init(tableDTO);
	contentPanel.setContent((Component) view);
	((AbstractMVPView) view).enter();
	
}

@Override
public void setCancelAcknowledgementView(Class<CancelAcknowledgementWizardView> class1,
		ReceiptOfDocumentsDTO rodDTO) {
	
	GMVPView view = views.select(class1).get();
	view.resetView();
	CancelAcknowledgementWizardView wizard = (CancelAcknowledgementWizardView) view;
	view.resetView();
	wizard.initView(rodDTO);
	tree.setSelectedView(class1);
	contentPanel.setContent((Component) view);

	((AbstractMVPView) view).enter();
}

/*@Override
public void setViewCreateAndSearchLot(Class<CreateAndSearchLotView> viewClass,
		boolean selectInNavigationTree) {
	
			GMVPView view = views.select(viewClass).get();
			view.resetView();
			contentPanel.setContent((Component) view);
			CreateAndSearchLotViewImpl createAndSearchLotViewImpl = (CreateAndSearchLotViewImpl) view;
			//createAndSearchLotViewImpl.init(type, cpuCode, claimant, claimType, paymentStatus);
			tree.setSelectedView(viewClass);
			if (selectInNavigationTree) {
				tree.setSelectedView(viewClass);
			}
			((AbstractMVPView) view).enter();
		
	
}*/


/*Override
public void @setViewSearchLot(Class<SearchCreateOrSearchLOTView> class1,
		boolean selectInNavigationTree, BeanItemContainer<SelectValue> selectValueContainerForType,
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
		BeanItemContainer<SelectValue> selectValueContainerForClaimant,
		BeanItemContainer<SelectValue> selectValueContainerForClaimType,
		BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus) {
	
			GMVPView view = views.select(class1).get();
			view.resetView();
			contentPanel.setContent((Component) view);
			SearchCreateOrSearchLOTViewImpl createAndSearchLotViewImpl = (SearchCreateOrSearchLOTViewImpl) view;
			//SearchCreateOrSearchLOTViewImpl.init(type, cpuCode, claimant, claimType, paymentStatus);
			tree.setSelectedView(class1);
			if (selectInNavigationTree) {
				tree.setSelectedView(class1);
			}
			((AbstractMVPView) view).enter();

	
}
*/

@Override
public void setViewForSearchClaimFinancial(
		Class<SearchProcessClaimFinancialsView> class1, boolean selectInNavigationTree,
		BeanItemContainer<SelectValue> claimType,
		BeanItemContainer<SelectValue> productName,
		BeanItemContainer<SelectValue> cpuCode,
		BeanItemContainer<SelectValue> type,
		BeanItemContainer<SelectValue> selectValueForPriority, 
		BeanItemContainer<SelectValue> statusByStage,
		BeanItemContainer<SelectValue> networkHospTypeParameter) {

	GMVPView view = views.select(class1).get();
	view.resetView();
	contentPanel.setContent((Component) view);
	SearchProcessClaimFinancialsViewImpl searchProcessClaimRequestViewImpl = (SearchProcessClaimFinancialsViewImpl) view;
	searchProcessClaimRequestViewImpl.setUpDropDownValues(claimType, productName, cpuCode,type,selectValueForPriority,statusByStage,networkHospTypeParameter);
//	searchProcessClaimRequestViewImpl.init(intimationSource, hospitalType, networkHospitalType, treatementType,typeContainer);
	tree.setSelectedView(class1);
	if (selectInNavigationTree) {
		tree.setSelectedView(class1);
	}
	((AbstractMVPView) view).enter();
}

@Override
public void setViewForPASearchClaimFinancialNonHosp(
		Class<PASearchProcessClaimFinancialsNonHospView> class1, boolean selectInNavigationTree,
		BeanItemContainer<SelectValue> claimType,
		BeanItemContainer<SelectValue> productName,
		BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {

	GMVPView view = views.select(class1).get();
	view.resetView();
	contentPanel.setContent((Component) view);
	PASearchProcessClaimFinancialsNonHospViewImpl searchProcessClaimRequestViewImpl = (PASearchProcessClaimFinancialsNonHospViewImpl) view;
	searchProcessClaimRequestViewImpl.setUpDropDownValues(claimType, productName, cpuCode,type,selectValueForPriority,statusByStage);
//	searchProcessClaimRequestViewImpl.init(intimationSource, hospitalType, networkHospitalType, treatementType,typeContainer);
	tree.setSelectedView(class1);
	if (selectInNavigationTree) {
		tree.setSelectedView(class1);
	}
	((AbstractMVPView) view).enter();
}




@Override
public void setViewCreateAndSearchLot(
		Class<CreateAndSearchLotView> viewClass,
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
		BeanItemContainer<SelectValue> selectValueContainerForType,
		BeanItemContainer<SelectValue> selectValueContainerForClaimant,
		BeanItemContainer<SelectValue> selectValueContainerForClaimType,
		BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus,
		BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,
		BeanItemContainer<SelectValue> docVerified,
		BeanItemContainer<SelectValue> selectValueContainerForPaymentMode,
		BeanItemContainer<SelectValue> selectValueContainerForVerificationType,
		boolean selectInNavigationTree) {
	
	GMVPView view = views.select(viewClass).get();
	//view.resetView();
	
	contentPanel.setContent((Component) view);
	CreateAndSearchLotViewImpl createAndSearchLotViewImpl = (CreateAndSearchLotViewImpl) view;
	createAndSearchLotViewImpl.resetView();
	createAndSearchLotViewImpl.init(selectValueContainerForType, selectValueContainerForCPUCode, selectValueContainerForClaimant, 
			selectValueContainerForClaimType, selectValueContainerForPaymentStatus,selectValueContainerForProduct,selectValueContainerForPaymentMode,docVerified,selectValueContainerForVerificationType);
	tree.setSelectedView(viewClass);
	if (selectInNavigationTree) {
		tree.setSelectedView(viewClass);
	}
	((AbstractMVPView) view).enter();
}

@Override
public void setViewCreateBatchOp(
		Class<CreateBatchOpView> viewClass,
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
		BeanItemContainer<SelectValue> selectValueContainerForType,
		BeanItemContainer<SelectValue> selectValueContainerForClaimant,
		BeanItemContainer<SelectValue> selectValueContainerForClaimType,
		BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus,
		BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,
		BeanItemContainer<SelectValue> docVerified,
		BeanItemContainer<SelectValue> selectValueContainerForPaymentMode,
		BeanItemContainer<SelectValue> selectValueContainerForPIOCode,
		boolean selectInNavigationTree) {
	
	GMVPView view = views.select(viewClass).get();
	//view.resetView();
	
	contentPanel.setContent((Component) view);
	CreateBatchOpViewImpl createBatchViewImpl = (CreateBatchOpViewImpl) view;
	createBatchViewImpl.resetView();
	createBatchViewImpl.init(selectValueContainerForType, selectValueContainerForCPUCode, selectValueContainerForClaimant, 
			selectValueContainerForClaimType, selectValueContainerForPaymentStatus,selectValueContainerForProduct,docVerified,selectValueContainerForPaymentMode,selectValueContainerForPIOCode);
	tree.setSelectedView(viewClass);
	if (selectInNavigationTree) {
		tree.setSelectedView(viewClass);
	}
	((AbstractMVPView) view).enter();
}

@Override
public void setViewOPScreen(Class<OpView> viewClass,
		BeanItemContainer<SelectValue> selectValueContainerForZoneType,
		boolean selectInNavigationTree) {
	GMVPView view = views.select(viewClass).get();
	//view.resetView();
	
	contentPanel.setContent((Component) view);
	OpViewImpl opViewImpl = (OpViewImpl) view;
	opViewImpl.resetView();
	//opViewImpl.init(selectValueContainerForType, selectValueContainerForCPUCode, selectValueContainerForClaimant, selectValueContainerForClaimType, selectValueContainerForPaymentStatus);
	tree.setSelectedView(viewClass);
	if (selectInNavigationTree) {
		tree.setSelectedView(viewClass);
	}
	((AbstractMVPView) view).enter();
	
}


@Override
public void setViewCreateBatch(Class<SearchCreateBatchView> viewClass,
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
		BeanItemContainer<SelectValue> selectValueContainerForType,
		BeanItemContainer<SelectValue> selectValueContainerForClaimant,
		BeanItemContainer<SelectValue> selectValueContainerForClaimType,
		BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus,
		BeanItemContainer<SelectValue> selectValueContainerForNonKeralaCPU,
		BeanItemContainer<SelectValue> selectValueContainerForBatchType,
		BeanItemContainer<SelectValue> selectValueContainerForZoneType,
		BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,
		BeanItemContainer<SelectValue> selectValueContainerForPaymentType,
		CreateAndSearchLotTableDTO createSearchLotDto,
		BeanItemContainer<SelectValue> selectValueContainerForPenalInstDays,
		BeanItemContainer<SelectValue> selectValueContainerForVerificationType,
		boolean selectInNavigationTree,String presenterString) {
	
	GMVPView view = views.select(viewClass).get();
//	view.resetView();
	contentPanel.setContent((Component) view);
	SearchCreateBatchViewImpl createBatchViewImpl = (SearchCreateBatchViewImpl) view;
	createBatchViewImpl.resetView();
	createBatchViewImpl.setMenuString(presenterString);
	createBatchViewImpl.init(selectValueContainerForType, selectValueContainerForCPUCode, selectValueContainerForClaimant,selectValueContainerForClaimType,selectValueContainerForPaymentStatus,selectValueContainerForNonKeralaCPU,
			selectValueContainerForBatchType,selectValueContainerForZoneType,selectValueContainerForProduct,selectValueContainerForPaymentType,selectValueContainerForPenalInstDays,selectValueContainerForVerificationType,presenterString);
	createBatchViewImpl.getPenalInterestRate(createSearchLotDto);
	tree.setSelectedView(viewClass);
	if (selectInNavigationTree) {
		tree.setSelectedView(viewClass);
	}
	((AbstractMVPView) view).enter();
	
}
// Commented below Cashless Screen
//@Override
//public void setSearchConvertReimbursement(
//		Class<SearchConvertReimbursementView> viewClass, boolean selectInNavigationTree,
//		BeanItemContainer<SelectValue> selectValueContainer) {
//	GMVPView view = views.select(viewClass).get();
//	view.resetView();
//	contentPanel.setContent((Component) view);
//	SearchConvertReimbursementViewImpl convertClaimViewImpl = (SearchConvertReimbursementViewImpl) view;
//	convertClaimViewImpl.init(selectValueContainer);
//	tree.setSelectedView(viewClass);
//	if (selectInNavigationTree) {
//		tree.setSelectedView(viewClass);
//	}
//	((AbstractMVPView) view).enter();
//}

public void showErrorPopUp(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(true);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
		
}

//	@Override
//	public void setClearCashlessScreen(Class<? extends GMVPView> viewClass,
//			SearchClearCashlessDTO tableDTO) {
//		GMVPView view = views.select(viewClass).get();
//		view.resetView();
//		contentPanel.setContent((Component) view);
//		ClearCashlessViewImpl convertClaimViewImpl = (ClearCashlessViewImpl) view;
//		convertClaimViewImpl.initView(tableDTO);
//		tree.setSelectedView(viewClass);
//		((AbstractMVPView) view).enter();
//	}

	@Override
	public void setCloseClaimPageView(Class<CloseClaimView> class1, boolean b,
			CloseClaimPageDTO tableDTO) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		CloseClaimView closeClaimView = (CloseClaimView) view;
		tree.setSelectedView(class1);
		closeClaimView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setReopenClaimPageView(Class<ReopenClaimView> viewClass,boolean b,
			ReopenClaimPageDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ReopenClaimView reopenClaimView = (ReopenClaimView) view;
		tree.setSelectedView(viewClass);
		reopenClaimView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setClaimWiseAllowApprovalPageView(Class<ClaimWiseAllowApprovalView> viewApprovalPageClass,boolean b,
			SearchClaimWiseAllowApprovalDto tableDto){
		GMVPView view = views.select(viewApprovalPageClass).get();
		view.resetView();
		ClaimWiseAllowApprovalView claimWiseView = (ClaimWiseAllowApprovalView) view;
		tree.setSelectedView(viewApprovalPageClass);
		claimWiseView.init(tableDto);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setPedInitiateOutSideView(Class<InitiatePedView> class1,
			PEDQueryDTO pedQueryDto) {
		
		MVPView view = views.select(class1).get();
		InitiatePedViewImpl wizard = (InitiatePedViewImpl) view;
		wizard.initView(pedQueryDto);
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setConvertClaimCashlessView(
			Class<ConvertClaimCashlessPageView> viewClass,
			ConvertClaimDTO convertClaimDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto,
			SearchConverClaimCashlessTableDTO searchFormDto) {

		MVPView view = views.select(viewClass).get();
		ConvertClaimcashlessPageViewImpl page = (ConvertClaimcashlessPageViewImpl) view;
		page.init(convertClaimDto, selectValueContainer, intimationDto,
				searchFormDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

			
	}
	@Override
	public void setProductAndDocumentTypeView(Class<ProductAndDocumentTypeView> viewClass,boolean b,
			ProductAndDocumentTypeDTO tableDTO, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> userTypeContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProductAndDocumentTypeView doctorDetailsView = (ProductAndDocumentTypeView) view;
		tree.setSelectedView(viewClass);
		doctorDetailsView.init(tableDTO, selectValueContainer, userTypeContainer);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	

/*@Override
public void setViewSearchLot(Class<SearchCreateOrSearchLOTView> viewClass, 
		boolean selectInNavigationTree) {
	
	GMVPView view = views.select(viewClass).get();
	view.resetView();
	contentPanel.setContent((Component) view);
	SearchCreateOrSearchLOTViewImpl createAndSearchLotViewImpl = (SearchCreateOrSearchLOTViewImpl) view;
	SearchCreateOrSearchLOTViewImpl.init(type, cpuCode, claimant, claimType, paymentStatus);
	tree.setSelectedView(viewClass);
	if (selectInNavigationTree) {
		tree.setSelectedView(viewClass);
	}
	((AbstractMVPView) view).enter();


	
	
}*/

	@Override
	public void setInitiateRequestRRCSearch(
			Class<InitiateRRCRequestView> viewClass) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		InitiateRRCRequestViewImpl searchModifyRRCRequestViewImpl = (InitiateRRCRequestViewImpl) view;
	//	searchModifyRRCRequestViewImpl.init(selectValueContainerForCPUCode , selectValueForRRCRequestType,selectValueForEligibility);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setInitiateRequestTALKTALKTALKSearch(
			Class<InitiateTalkTalkTalkView> viewClass) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		InitiateTalkTalkTalkViewImpl searchModifyRRCRequestViewImpl = (InitiateTalkTalkTalkViewImpl) view;
		searchModifyRRCRequestViewImpl.init();
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setinitiateTalkTalkTalkWizard(
			Class<InitiateTalkTalkTalkWizardView> viewClass,ReceiptOfDocumentsDTO rodDTO) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		InitiateTalkTalkTalkWizardViewImpl wizard = (InitiateTalkTalkTalkWizardViewImpl) view;
	//	searchModifyRRCRequestViewImpl.init(selectValueContainerForCPUCode , selectValueForRRCRequestType,selectValueForEligibility);
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setinitiateRRCRequestWizard(
			Class<InitiateRRCRequestWizardView> viewClass,ReceiptOfDocumentsDTO rodDTO) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		InitiateRRCRequestWizardViewImpl wizard = (InitiateRRCRequestWizardViewImpl) view;
	//	searchModifyRRCRequestViewImpl.init(selectValueContainerForCPUCode , selectValueForRRCRequestType,selectValueForEligibility);
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		
		((AbstractMVPView) view).enter();
	}


	@Override
	public void setViewGenerateBulkReminderLetter(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, Map<String, Object> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchGenerateRemainderBulkViewImpl searchGenerateRemainderBulkViewImpl = (SearchGenerateRemainderBulkViewImpl) view;
		searchGenerateRemainderBulkViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();	
		
	}
	

@Override
public void setViewPrintBulkReminderLetter(
		Class<? extends GMVPView> viewClass,
		boolean selectInNavigationTree, Map<String, Object> parameter) {
	GMVPView view = views.select(viewClass).get();
	view.resetView();
	contentPanel.setContent((Component) view);
	SearchPrintRemainderBulkViewImpl searchGenerateRemainderBulkViewImpl = (SearchPrintRemainderBulkViewImpl) view;
	searchGenerateRemainderBulkViewImpl.init(parameter);
	tree.setSelectedView(viewClass);
	if (selectInNavigationTree) {
		tree.setSelectedView(viewClass);
	}
	((AbstractMVPView) view).enter();	
	
}


@Override
public void setViewPrintBulkPaymentLetter(
		Class<? extends GMVPView> viewClass,
		boolean selectInNavigationTree) {
	GMVPView view = views.select(viewClass).get();
	view.resetView();
	contentPanel.setContent((Component) view);
	SearchPrintPaymentBulkViewImpl searchGeneratePaymentBulkViewImpl = (SearchPrintPaymentBulkViewImpl) view;
	tree.setSelectedView(viewClass);
	if (selectInNavigationTree) {
		tree.setSelectedView(viewClass);
	}
	((AbstractMVPView) view).enter();	
	
}

@Override
public void setViewPAPrintBulkReminderLetter(
		Class<? extends GMVPView> viewClass,
		boolean selectInNavigationTree, Map<String, Object> parameter) {
	GMVPView view = views.select(viewClass).get();
	view.resetView();
	contentPanel.setContent((Component) view);
	SearchPAPrintRemainderBulkViewImpl searchGenerateRemainderBulkViewImpl = (SearchPAPrintRemainderBulkViewImpl) view;
	searchGenerateRemainderBulkViewImpl.init(parameter);
	tree.setSelectedView(viewClass);
	if (selectInNavigationTree) {
		tree.setSelectedView(viewClass);
	}
	((AbstractMVPView) view).enter();	
	
}

@Override
public void setViewGenerateBulkPAReminderLetter(
		Class<? extends GMVPView> viewClass,
		boolean selectInNavigationTree, Map<String, Object> parameter) {
	GMVPView view = views.select(viewClass).get();
	view.resetView();
	contentPanel.setContent((Component) view);
	SearchGeneratePARemainderBulkViewImpl searchGeneratePARemainderBulkViewImpl = (SearchGeneratePARemainderBulkViewImpl) view;
	searchGeneratePARemainderBulkViewImpl.init(parameter);
	tree.setSelectedView(viewClass);
	if (selectInNavigationTree) {
		tree.setSelectedView(viewClass);
	}
	((AbstractMVPView) view).enter();	
	
}

	public void setViewPaymentBatchReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> dateType) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PaymentBatchReportViewImpl paymentBatchReportViewImpl = (PaymentBatchReportViewImpl) view;
		//hospitalWiseReportViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
public void setViewTATReport(Class<TATReportView> viewClass,BeanItemContainer<SelectValue> selectValueContainerForCPUCode,		
		BeanItemContainer<SelectValue> selectValueContainerForOfficeCode,boolean b) {
	
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		TATReportViewImpl tatReportViewImpl = (TATReportViewImpl) view;
		tatReportViewImpl.init(selectValueContainerForCPUCode,selectValueContainerForOfficeCode);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setSettlementPullBackScreen(
			Class<SettlementPullBackView> viewClass,
			SearchSettlementPullBackDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SettlementPullBackViewImpl settlementPullBackViewImpl = (SettlementPullBackViewImpl) view;
		settlementPullBackViewImpl.initView(tableDTO);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setViewProvisionReport(Class<SearchShowdowView> class1,
			boolean b) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		ShadowProvisionViewImpl viewImpl = (ShadowProvisionViewImpl) view;
		viewImpl.init();
		tree.setSelectedView(class1);
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setProcess64Wizard(Class<? extends GMVPView> viewClass,SearchPreauthTableDTO tableDTO) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		Process64VBWizardViewImpl wizard = (Process64VBWizardViewImpl) view;
		view.resetView();
		wizard.initView(tableDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setReallocationUserView(Class<SearchReallocationDoctorDetailsView> viewClass,boolean b, SearchReallocationDoctorDetailsTableDTO doctorDetailsDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchReallocationDoctorDetailsView doctorDetailsView = (SearchReallocationDoctorDetailsView) view;
		tree.setSelectedView(viewClass);
		doctorDetailsView.init(doctorDetailsDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	
	
	@Override
	public void setPAProcessClaimRequest(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PAClaimRequestWizardViewImpl wizard = (PAClaimRequestWizardViewImpl) view;
		wizard.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	public void setViewsProcessConsumerForum(
			Class<? extends GMVPView> viewClass, 
			BeanItemContainer<SelectValue> repudiationMasterValueByCode,
			BeanItemContainer<SelectValue> tmpZoneList,
			BeanItemContainer<SelectValue> tmpStateList,
			BeanItemContainer<SelectValue> orderMasterValueByCode,
			BeanItemContainer<SelectValue> order1MasterValueByCode,
			BeanItemContainer<SelectValue> awardReasonMasterValueByCode,
			BeanItemContainer<SelectValue> depAmtMasterValueByCode,
			BeanItemContainer<SelectValue> caseUpdateMasterValueByCode,
			BeanItemContainer<SelectValue> recievedFrom, BeanItemContainer<SelectValue> movedTO, BeanItemContainer<SelectValue> statusCase
			) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessConsumerForumViewImpl searchProcessConsumerForumViewImpl = (SearchProcessConsumerForumViewImpl) view;
		//searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage,coverContainer);
		searchProcessConsumerForumViewImpl.init(repudiationMasterValueByCode,tmpZoneList,tmpStateList,orderMasterValueByCode,order1MasterValueByCode,awardReasonMasterValueByCode,
				depAmtMasterValueByCode,caseUpdateMasterValueByCode,recievedFrom,movedTO,statusCase);
		tree.setSelectedView(viewClass);
		/*if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}*/
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewsProcessAdvocateNotice(
			Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> moveToMasterValueByCode,
			BeanItemContainer<SelectValue> pendingLevelMasterValueByCode,
			BeanItemContainer<SelectValue> repudiationMasterValueByCode,
			BeanItemContainer<SelectValue> recievedFrom
			) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessAdvocateNoticeViewImpl searchProcessAdvocateNoticeViewImpl = (SearchProcessAdvocateNoticeViewImpl) view;
		//searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage,coverContainer);
		searchProcessAdvocateNoticeViewImpl.init(repudiationMasterValueByCode,moveToMasterValueByCode,pendingLevelMasterValueByCode,recievedFrom);
		tree.setSelectedView(viewClass);
		/*if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}*/
//>>>>>>> legal
		((AbstractMVPView) view).enter();
		
	}
	
	public void setViewsProcessAdvocateFee(
			Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> repudiationMasterValueByCode, 
			BeanItemContainer<SelectValue> tmpZoneList
			) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessAdvocateFeeViewImpl searchProcessAdvocateFeeViewImpl = (SearchProcessAdvocateFeeViewImpl) view;
		//searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage,coverContainer);
		searchProcessAdvocateFeeViewImpl.init(repudiationMasterValueByCode,tmpZoneList);
		tree.setSelectedView(viewClass);
		/*if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}*/
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewsProcessOmbudsman(
			Class<? extends GMVPView> viewClass,BeanItemContainer<SelectValue> repudiationMasterValueByCode,BeanItemContainer<SelectValue> statusoftheCase,
			BeanItemContainer<SelectValue> ombudsmanDetailsByDesc,
			BeanItemContainer<SelectValue> addDays,
			BeanItemContainer<SelectValue> pendingLevel,
			BeanItemContainer<SelectValue> hearingStatus,
			BeanItemContainer<SelectValue> awardStatus,
			BeanItemContainer<SelectValue> compromiseStatus,
			BeanItemContainer<SelectValue> decision,
			BeanItemContainer<SelectValue> recievedFrom, BeanItemContainer<SelectValue> movedTO, BeanItemContainer<SelectValue> grievanceOutcome) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessOmbudsmanViewImpl searchProcessOmbudsmanViewImpl = (SearchProcessOmbudsmanViewImpl) view;
		//searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage,coverContainer);
		searchProcessOmbudsmanViewImpl.init(repudiationMasterValueByCode,statusoftheCase,
				ombudsmanDetailsByDesc,addDays,pendingLevel,hearingStatus,awardStatus,compromiseStatus,decision,recievedFrom,movedTO,grievanceOutcome);
		tree.setSelectedView(viewClass);
		/*if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}*/
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewsLegalHome(
			Class<? extends GMVPView> viewClass
			) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchLegalHomeViewImpl legalHome = (SearchLegalHomeViewImpl) view;
		//searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage,coverContainer);
		legalHome.initView();
		tree.setSelectedView(viewClass);
		/*if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}*/
		((AbstractMVPView) view).enter();
		
	}

	public void setDecideOnPACoveringLewtterWizardView(Class<? extends GMVPView> viewClass,
			GenerateCoveringLetterSearchTableDto searchPaCoveringLetterTableDto){
		
		GMVPView view = views.select(viewClass).get();
		DecideOnPACoveringLetterWizardViewImpl wizard = (DecideOnPACoveringLetterWizardViewImpl) view;
		wizard.initView(searchPaCoveringLetterTableDto);
		tree.setSelectedView(viewClass);

		contentPanel.setContent((Component) view);
		
		((AbstractMVPView) view).enter();

		
	}
	
	@Override
	public void setViewInvestigationGrading (Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> claimTypeContainer,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchInvestgationGradingViewImpl searchInvestigationGradingViewImpl = (SearchInvestgationGradingViewImpl) view;
		searchInvestigationGradingViewImpl.init(parameter,claimTypeContainer,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	/*@Override
	public void setViewInvestigationGrading(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchProcessInvestigationInitiatedTableDTO investigation) {
		try{
			GMVPView view = views.select(viewClass).get();
			view.resetView();
			contentPanel.setContent((Component) view);

			InvestigationGradingViewImpl investigationGradingViewImpl = (InvestigationGradingViewImpl) view;

			investigationGradingViewImpl.init(investigation);

			if (selectInNavigationTree) {
				tree.setSelectedView(viewClass);
			}
			((AbstractMVPView) view).enter();
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	@Override
	public void setViewInvestigationGradingDetail(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			AssignInvestigatorDto assignInvestigatorDto) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		InvestigationGradingWizardViewImpl investigationGradingWizardViewImpl = (InvestigationGradingWizardViewImpl) view;

		investigationGradingWizardViewImpl.initView(assignInvestigatorDto);

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setViewGForPA(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> intimationSrcParameter,
			BeanItemContainer<SelectValue> networkHospTypeParameter,
			BeanItemContainer<SelectValue> treatmentTypeParameter,
			BeanItemContainer<SelectValue> preAuthTypeParameter,BeanItemContainer<SelectValue> specialityContainer,BeanItemContainer<SelectValue> cpuCodeContainer,
			String strViewName) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		if ((SHAConstants.PROCESS_ENHANCEMENT).equalsIgnoreCase(strViewName)) {
//			PASearchEnhancementViewImpl enhancementViewImpl = (PASearchEnhancementViewImpl) view;
//			enhancementViewImpl.init(intimationSrcParameter,
//					networkHospTypeParameter, treatmentTypeParameter,
//					preAuthTypeParameter,specialityContainer, cpuCodeContainer);
		} else if ((SHAConstants.PROCESS_PREAUTH).equalsIgnoreCase(strViewName)) {
//			PASearchPreauthViewImpl preAuthViewImpl = (PASearchPreauthViewImpl) view;
//			preAuthViewImpl.init(intimationSrcParameter,
//					networkHospTypeParameter, treatmentTypeParameter,
//					preAuthTypeParameter,specialityContainer, cpuCodeContainer);
		} else if ((SHAConstants.PROCESS_PRE_MEDICAL)
				.equalsIgnoreCase(strViewName)) {
//			PAProcessPreMedicalViewImpl processPreMedicalImpl = (PAProcessPreMedicalViewImpl) view;
//
//			processPreMedicalImpl.init(intimationSrcParameter,
//					networkHospTypeParameter, preAuthTypeParameter, cpuCodeContainer);
		} else if ((SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT)
				.equalsIgnoreCase(strViewName)) {
//			PASearchPreMedicalProcessingEnhancementViewImpl processPreMedicalEnhancement = (PASearchPreMedicalProcessingEnhancementViewImpl) view;
//
//			processPreMedicalEnhancement.init(intimationSrcParameter,
//					networkHospTypeParameter, preAuthTypeParameter, cpuCodeContainer);
		}
		// SearchConvertClaimViewImpl convertClaimViewImpl =
		// (SearchConvertClaimViewImpl) view;
		// convertClaimViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setPreMedicalWizardViewForPA(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDTO, Boolean processId) {

		GMVPView view = views.select(viewClass).get();
		PAPreMedicalPreauthWizarViewImpl wizard = (PAPreMedicalPreauthWizarViewImpl) view;
		wizard.initView(preauthDTO, processId);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setPreauthWizardViewForPA(Class<? extends GMVPView> viewClass,
			PreauthDTO preauthDTO, Boolean processId) {

		MVPView view = views.select(viewClass).get();
		PAPreauthWizardViewImpl wizard = (PAPreauthWizardViewImpl) view;
		wizard.initView(preauthDTO, processId);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setPreMedicalEnhancementViewForPA(
			Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO) {
		MVPView view = views.select(viewClass).get();
		PAPremedicalEnhancementWizardViewImpl wizard = (PAPremedicalEnhancementWizardViewImpl) view;
			wizard.initView(preauthDTO);
			tree.setSelectedView(viewClass);
			contentPanel.setContent((Component) view);
			((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setPreauthEnhancementWizardViewForPA(
			Class<? extends GMVPView> viewClass, PreauthDTO preauthDTO) {
		MVPView view = views.select(viewClass).get();
		PAPreauthEnhancementWizardViewImpl wizard = (PAPreauthEnhancementWizardViewImpl) view;
		wizard.initView(preauthDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setConvertPAClaimView(Class<? extends GMVPView> viewClass,
			ConvertClaimDTO convertClaimDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			SearchConvertClaimTableDto searchFormDto) {
		MVPView view = views.select(viewClass).get();
		ConvertPAClaimPageViewImpl page = (ConvertPAClaimPageViewImpl) view;
		page.init(convertClaimDto, selectValueContainer, searchFormDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}

	public void setSearchConvertPAClaimViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, Map<String, Object> parameter) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		SearchConvertPAClaimViewImpl convertPAClaimViewImpl = (SearchConvertPAClaimViewImpl) view;
		convertPAClaimViewImpl.init(parameter);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewsPAHealthProcessClaimRequest(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType,BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> productName, BeanItemContainer<SelectValue> cpuCode
			,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, PAHealthSearchProcessClaimRequestFormDTO dto,String screenName){
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PAHealthSearchProcessClaimRequestViewImpl searchProcessClaimRequestViewImpl = (PAHealthSearchProcessClaimRequestViewImpl) view;
		searchProcessClaimRequestViewImpl.initViewForReferesh(dto, (dto != null ? true : false));
		searchProcessClaimRequestViewImpl.init(intimationSource, hospitalType, networkHospitalType, treatementType,typeContainer,productName,cpuCode,selectValueForPriority,statusByStage,screenName);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	
	@Override
	public void setPAHealthProcessClaimRequest(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PAHealthClaimRequestWizardViewImpl wizard = (PAHealthClaimRequestWizardViewImpl) view;
		wizard.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewForPAHealthSearchClaimFinancial(
			Class<PAHealthSearchProcessClaimFinancialsView> class1, boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> productName,
			BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {

		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PAHealthSearchProcessClaimFinancialsViewImpl searchProcessClaimRequestViewImpl = (PAHealthSearchProcessClaimFinancialsViewImpl) view;
		searchProcessClaimRequestViewImpl.setUpDropDownValues(claimType, productName, cpuCode,type,selectValueForPriority,statusByStage);
//		searchProcessClaimRequestViewImpl.init(intimationSource, hospitalType, networkHospitalType, treatementType,typeContainer);
		tree.setSelectedView(class1);
		if (selectInNavigationTree) {
			tree.setSelectedView(class1);
		}
		((AbstractMVPView) view).enter();
	}
	

	@Override
	public void setPAHealthProcessClaimFinancial(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PAHealthFinancialWizardViewImpl financialWizardViewImpl = (PAHealthFinancialWizardViewImpl) view;
		financialWizardViewImpl.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewsPAHealthProcessClaimBilling(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> cpucode,
			BeanItemContainer<SelectValue> productNameCode,BeanItemContainer<SelectValue> typeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PAHealthSearchProcessClaimBillingViewImpl searchProcessClaimBillingViewImpl = (PAHealthSearchProcessClaimBillingViewImpl) view;
		searchProcessClaimBillingViewImpl.init(cpucode, productNameCode,typeContainer,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewInvestigationGrading(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			SearchProcessInvestigationInitiatedTableDTO tableDto) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setPAHealthProcessClaimBilling(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PAHealthBillingWizardViewImpl billingWizardViewImpl = (PAHealthBillingWizardViewImpl) view;
		billingWizardViewImpl.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	public void setPAProcessClaimApproval(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PAClaimAprNonHosWizardViewImpl billingWizardViewImpl = (PAClaimAprNonHosWizardViewImpl) view;
		billingWizardViewImpl.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setViewForPAHealthSearchClaimFinancialNonHosp(
			Class<PASearchProcessClaimFinancialsNonHospView> class1, boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> productName,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> type,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage) {
		
		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchProcessClaimFinancialsNonHospViewImpl searchProcessClaimRequestViewImpl = (PASearchProcessClaimFinancialsNonHospViewImpl) view;
		searchProcessClaimRequestViewImpl.setUpDropDownValues(claimType, productName, cpuCode,type,selectValueForPriority,statusByStage);
//		searchProcessClaimRequestViewImpl.init(intimationSource, hospitalType, networkHospitalType, treatementType,typeContainer);
		tree.setSelectedView(class1);
		if (selectInNavigationTree) {
			tree.setSelectedView(class1);
		}
		((AbstractMVPView) view).enter();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setViewDraftInvestigation(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,
			BeanItemContainer<SelectValue> claimTypeContainer,
			BeanItemContainer<SelectValue> selectValueForPriority,
			BeanItemContainer<SelectValue> statusByStage) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchDraftInvestigationViewImpl searchDraftInvestigationViewImpl = (SearchDraftInvestigationViewImpl) view;
		searchDraftInvestigationViewImpl.init(parameter,claimTypeContainer,selectValueForPriority,statusByStage);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setDraftInvestigation(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			DraftInvestigatorDto draftInvestigatorDto) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		DraftInvestigationWizardViewImpl draftInvestigationViewImpl = (DraftInvestigationWizardViewImpl) view;

		draftInvestigationViewImpl.initView(draftInvestigatorDto);

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	public void setPAProcessFinancialNonHosp(Class<? extends GMVPView> viewClass,
			PreauthDTO reimbursementDTO) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PANonHospFinancialWizardViewImpl billingWizardViewImpl = (PANonHospFinancialWizardViewImpl) view;
		billingWizardViewImpl.initView(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	
		
	}
	
	@Override

	public void setViewPASearchOrUploadDocuments(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> type) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchUploadDocumentsViewImpl searchUploadDocumentsViewImpl = (PASearchUploadDocumentsViewImpl) view;
		searchUploadDocumentsViewImpl.init(type);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	public void setPAReopenClaimPageView(Class<PAReopenClaimView> viewClass,boolean b,
			PAReopenClaimPageDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PAReopenClaimView reopenClaimView = (PAReopenClaimView) view;
		tree.setSelectedView(viewClass);
		reopenClaimView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	public void setPAHealthReopenClaimPageView(Class<PAHealthReopenClaimView> viewClass,boolean b,
			PAHealthReopenClaimPageDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PAHealthReopenClaimView reopenClaimView = (PAHealthReopenClaimView) view;
		tree.setSelectedView(viewClass);
		reopenClaimView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setPAReopenClaimSearchBasedView(Class<PAReOpenRodLevelClaimView> class1,
			boolean b, PASearchReOpenClaimRodLevelTableDTO tableDTO) {
		
		GMVPView view = views.select(class1).get();
		view.resetView();
		PAReOpenRodLevelClaimView reOpenClaimView = (PAReOpenRodLevelClaimView) view;
		tree.setSelectedView(class1);
		reOpenClaimView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

		
	}
	
	@Override
	public void setPAHealthReopenClaimSearchBasedView(Class<PAHealthReOpenRodLevelClaimView> class1,
			boolean b, PAHealthSearchReOpenClaimRodLevelTableDTO tableDTO) {
		
		GMVPView view = views.select(class1).get();
		view.resetView();
		PAHealthReOpenRodLevelClaimView reOpenClaimView = (PAHealthReOpenRodLevelClaimView) view;
		tree.setSelectedView(class1);
		reOpenClaimView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

		
	}
	
	@Override

	public void setPASearchOrUploadDocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO uploadDto) {
		GMVPView view = views.select(viewClass).get();
		
		
		PASearchUploadDocumentsWizardViewImpl wizard = (PASearchUploadDocumentsWizardViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(uploadDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	public void setPASettlementPullBackScreen(
			Class<PASettlementPullBackView> viewClass,
			PASearchSettlementPullBackDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASettlementPullBackViewImpl settlementPullBackViewImpl = (PASettlementPullBackViewImpl) view;
		settlementPullBackViewImpl.initView(tableDTO);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();
	}
	
	public void setPAHospSettlementPullBackScreen(
			Class<PAHospSettlementPullBackView> viewClass,
			PAHospSearchSettlementPullBackDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PAHospSettlementPullBackViewImpl settlementPullBackViewImpl = (PAHospSettlementPullBackViewImpl) view;
		settlementPullBackViewImpl.initView(tableDTO);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setPACloseClaimSearchBasedRODLevelView(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, PASearchCloseClaimTableDTORODLevel tableDTO) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PACloseClaimRodLevelView closeClaimView = (PACloseClaimRodLevelView) view;
		tree.setSelectedView(viewClass);
		closeClaimView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setPACloseClaimPageView(Class<PACloseClaimView> class1, boolean b,
			PACloseClaimPageDTO tableDTO) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		PACloseClaimView closeClaimView = (PACloseClaimView) view;
		tree.setSelectedView(class1);
		closeClaimView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setPACloseClaimInProcessView(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, PASearchCloseClaimTableDTORODLevel tableDTO) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PACloseClaimInProcessView closeClaimView = (PACloseClaimInProcessView) view;
		tree.setSelectedView(viewClass);
		closeClaimView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		

	}
	
	@Override
	public void setViewPASearchOrUploaddocumentsForAckNotReceived(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> type) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PAUploadDocumentsOutsideProcessViewImpl searchUploadDocumentsViewImpl = (PAUploadDocumentsOutsideProcessViewImpl) view;
		searchUploadDocumentsViewImpl.init(type);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	
		
	}
	
	@Override
	public void setPASearchOrUploadDocumentsForAckNotReceivedWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO uploadDto) {
		GMVPView view = views.select(viewClass).get();
		
		
		PAUploadDocumentsOutsideProcessWizardViewImpl wizard = (PAUploadDocumentsOutsideProcessWizardViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(uploadDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
//	Commented the below Cashless Screen
//	public void setPASearchConvertReimbursement(
//			Class<PASearchConvertReimbursementView> viewClass, boolean selectInNavigationTree,
//			BeanItemContainer<SelectValue> selectValueContainer) {
//		GMVPView view = views.select(viewClass).get();
//		view.resetView();
//		contentPanel.setContent((Component) view);
//		PASearchConvertReimbursementViewImpl convertClaimViewImpl = (PASearchConvertReimbursementViewImpl) view;
//		convertClaimViewImpl.init(selectValueContainer);
//		tree.setSelectedView(viewClass);
//		if (selectInNavigationTree) {
//			tree.setSelectedView(viewClass);
//		}
//		((AbstractMVPView) view).enter();
//	}
	
	@Override
	public void setPAConvertReimbursementView(Class<? extends GMVPView> viewClass,
			ConvertClaimDTO convertClaimDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto,
			SearchConvertClaimTableDto searchFormDto) {
		MVPView view = views.select(viewClass).get();
		PAConvertReimbursementPageViewImpl page = (PAConvertReimbursementPageViewImpl) view;
		page.init(convertClaimDto, selectValueContainer, intimationDto,
				searchFormDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	public void setPAViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PASearchConvertReimbursementViewImpl convertClaimViewImpl = (PASearchConvertReimbursementViewImpl) view;
		convertClaimViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	@Override
	public void setFileStorageSearch(
			Class<SearchDataEntryView> viewClass, boolean b) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	public void setViewProductivityReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		ProductivityReportViewImpl productivityReportViewImpl = (ProductivityReportViewImpl) view;
		tree.setSelectedView(viewClass);
		productivityReportViewImpl.setupDroDownValues();
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	public void setViewAutomationDashboard(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		AutomationDashboardViewImpl automationDashboardViewImpl = (AutomationDashboardViewImpl) view;
		tree.setSelectedView(viewClass);
		//automationDashboardViewImpl.setupDroDownValues();
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	public void setViewPaymentPendingDashboard(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		PaymentPendingDashboardViewImpl automationDashboardViewImpl = (PaymentPendingDashboardViewImpl) view;
		tree.setSelectedView(viewClass);
		//automationDashboardViewImpl.setupDroDownValues();
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	@Override
	public void setProcessDataEntryWizard(
			Class<ProcessDataEntryWizardViewImpl> viewClass,
			SearchDataEntryTableDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		
		ProcessDataEntryWizardViewImpl wizard = (ProcessDataEntryWizardViewImpl) view;
		//wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(tableDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
}

	@Override
	public void setViewFileDetailsReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		// TODO Auto-generated method stub

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		FileDetailsReportViewImpl fileDetailsReportViewImpl = (FileDetailsReportViewImpl) view;
		fileDetailsReportViewImpl.init();
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	public void setViewPanCard(Class<SearchUploadPanCardView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchUploadPanCardViewImpl uploadInvestigationReportViewImpl = (SearchUploadPanCardViewImpl) view;
		uploadInvestigationReportViewImpl.init();
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setUpdatePancardDetails(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchUploadPanCardTableDTO investigationKey) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		UpdatePanCardReportViewImpl uploadInvestigationReportViewImpl = (UpdatePanCardReportViewImpl) view;
		uploadInvestigationReportViewImpl.init(investigationKey);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setReceivedPhysicaldocumentsWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO) {
		GMVPView view = views.select(viewClass).get();
		
		
		ReceivedPhysicalDocumentsWizardViewImpl wizard = (ReceivedPhysicalDocumentsWizardViewImpl) view;
	//	wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setGmcDailyReportView(Class<GmcDailyReportView> class1,
			boolean b, BeanItemContainer<SelectValue> cpuContainer,
			BeanItemContainer<SelectValue> clmTypeContainer) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		GmcDailyReportViewImpl searchClmsDailyReportViewImpl = (GmcDailyReportViewImpl) view;		
		tree.setSelectedView(class1);
		searchClmsDailyReportViewImpl.setupDroDownValues(cpuContainer,clmTypeContainer);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	
	public void setViewGPAUnamedDetailsView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchUnnamedRiskDetailsViewImpl unnamedRiskDetailsViewImpl = (SearchUnnamedRiskDetailsViewImpl) view;
		//hospitalWiseReportViewImpl.init(parameter);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewUnnamedRiskDetailsPage(Class<UnnamedRiskDetailsPageView> class1,
			boolean b, UnnamedRiskDetailsPageDTO pageDto,BeanItemContainer<SelectValue> category) {
		
		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		UnnamedRiskDetailsPageViewImpl unnamedRiskDetailsViewImpl = (UnnamedRiskDetailsPageViewImpl) view;
		unnamedRiskDetailsViewImpl.init(pageDto);
		unnamedRiskDetailsViewImpl.init(category);
		tree.setSelectedView(class1);
		if (b) {
			tree.setSelectedView(class1);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setOmpClaimProcessorview(
			Class<OMPProcessOmpClaimProcessorPageWizard> viewClass,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country) {
		
		MVPView view = views.select(viewClass).get();
		OMPClaimProcessorWizardViewImpl wizard = (OMPClaimProcessorWizardViewImpl) view;
		wizard.init(claimProcessorDTO,classification,subClassification,paymentTo,paymentMode,eventCode,currencyValue,negotiatorName,modeOfReciept,
				documentRecievedFrom,documentType,country);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		claimProcessorDTO.setIsOnLoad(Boolean.FALSE);
	}

	@Override
	public void setViewG(Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> classification,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		OMPProcessOmpClaimProcessorViewImpl oMPProcessOmpClaimProcessorViewImpl = (OMPProcessOmpClaimProcessorViewImpl) view;
		oMPProcessOmpClaimProcessorViewImpl.init(classification);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	

	@Override
	public void showOMPAddIntimationView(Class<? extends GMVPView> viewClass,
			OMPCreateIntimationTableDTO ompcreateintimationtabledto) {
		GMVPView view = views.select(viewClass).get();
		OMPCreateIntimationWizardViewImpl ompcreateintimationwizardview = (OMPCreateIntimationWizardViewImpl) view;
		ompcreateintimationwizardview.initView(ompcreateintimationtabledto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setOmpClaimDetailRegistrationPage(
			Class<OMPClaimRegistrationWizardView> viewClass,
			OMPSearchClaimRegistrationTableDTO ompClaimDetailRegistrationDTO,
			Boolean secondaryParameter) {
		

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		OMPClaimRegistrationWizardViewImpl wizard = (OMPClaimRegistrationWizardViewImpl) view;
		wizard.initView(ompClaimDetailRegistrationDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	
	}


	@Override
	public void setOMPRejectionView(Class<? extends GMVPView> viewClass,
			SearchProcessRejectionTableDTO rejectionDTO) {
		MVPView view = views.select(viewClass).get();
//		ProcessRejectionPageViewImpl wizard = (ProcessRejectionPageViewImpl) view;
		OMPProcessRejectionView wizard = (OMPProcessRejectionView) view;
		wizard.setReferenceData(rejectionDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setOMPSearchRegistration(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		OMPSearchClaimRegisterViewImpl claimregisterViewImpl = (OMPSearchClaimRegisterViewImpl) view;

	//	claimregisterViewImpl.setReferenceData();
		claimregisterViewImpl.init();

		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();

	}

	@Override
	public void setOmpRODBillEntryview(
			Class<OMPProcessRODBillEntryPageWizard> viewClass,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country) {
		MVPView view = views.select(viewClass).get();
		OMPRODBillEntryWizardViewImpl wizard = (OMPRODBillEntryWizardViewImpl) view;
		wizard.init(claimProcessorDTO,classification,subClassification,paymentTo,paymentMode,eventCode,currencyValue,negotiatorName,modeOfReciept,
				documentRecievedFrom,documentType,country);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void ompProcessApproverSearch(Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> classification,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		OMPProcessOmpClaimApproverViewImpl oMPProcessOmpClaimApproverViewImpl = (OMPProcessOmpClaimApproverViewImpl) view;
		oMPProcessOmpClaimApproverViewImpl.init(classification);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	//CR20181332
	@Override
	public void ompBulkUploadRejectionSearch(Class<? extends GMVPView> viewClass,BeanItemContainer<SelectValue> statusContainer,
			boolean b){
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		OMPBulkUploadRejectionViewImpl viewImpl = (OMPBulkUploadRejectionViewImpl) view;
		viewImpl.initView(statusContainer);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setOmpProcessApproverView(
			Class<OMPProcessOmpClaimApproverWizardView> viewClass,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country) {
		MVPView view = views.select(viewClass).get();
		OMPProcessOmpClaimApproverWizardViewImpl wizard = (OMPProcessOmpClaimApproverWizardViewImpl) view;
		wizard.init(claimProcessorDTO,classification,subClassification,paymentTo,paymentMode,eventCode,currencyValue,negotiatorName,modeOfReciept,
				documentRecievedFrom,documentType,country);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		claimProcessorDTO.setIsOnLoad(Boolean.FALSE);
	}

	@Override
	public void setOmpNegotiationViewG(Class<OMPProcessNegotiationView> class1,
			boolean b) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		OMPProcessNegotiationViewImpl claimregisterViewImpl = (OMPProcessNegotiationViewImpl) view;

	//	claimregisterViewImpl.setReferenceData();
		claimregisterViewImpl.init();

		if (b) {
			tree.setSelectedView(class1);
		}
		((AbstractMVPView) view).enter();
		
	}
	
//	@Override
//	public void setOmpNegotiationViewG(Class<? extends GMVPView> viewClass,
//			boolean selectInNavigationTree) {
//
//		GMVPView view = views.select(viewClass).get();
//		view.resetView();
//		contentPanel.setContent((Component) view);
//
//		OMPProcessNegotiationViewImpl claimregisterViewImpl = (OMPProcessNegotiationViewImpl) view;
//
//	//	claimregisterViewImpl.setReferenceData();
//		claimregisterViewImpl.init();
//
//		if (selectInNavigationTree) {
//			tree.setSelectedView(viewClass);
//		}
//		((AbstractMVPView) view).enter();
//
//	}
	
	@Override
	public void setOmpProcessNegotiationview(
			Class<OMPProcessNegotiationWizardView> viewClass,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country) {
		MVPView view = views.select(viewClass).get();
		OMPProcessNegotiationWizardViewImpl wizard = (OMPProcessNegotiationWizardViewImpl) view;
		wizard.init(claimProcessorDTO,classification,subClassification,paymentTo,paymentMode,eventCode,currencyValue,negotiatorName,modeOfReciept,
				documentRecievedFrom,documentType,country);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setSearchClaimRateChangeViewG(
			Class<OMPClaimRateChangeAndOsUpdationView> class1, boolean selectInNavigationTree) {

		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);

		OMPClaimRateChangeAndOsUpdationViewImpl claimregisterViewImpl = (OMPClaimRateChangeAndOsUpdationViewImpl) view;

	//	claimregisterViewImpl.setReferenceData();
		claimregisterViewImpl.init();

		if (selectInNavigationTree) {
			tree.setSelectedView(class1);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	//lumen 
	@Override 
	public void renderLumenInitiateRequestPage(Class<? extends GMVPView> viewClass, LumenSearchResultTableDTO resultDTO){
		GMVPView view = views.select(viewClass).get();
		InitiateLumenRequestWizardImpl lumenRequestObj = (InitiateLumenRequestWizardImpl) view;
		lumenRequestObj.initView(resultDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override 
	public void renderPolicyLumenInitiateRequestPage(Class<? extends GMVPView> viewClass, LumenPolicySearchResultTableDTO resultDTO){
		GMVPView view = views.select(viewClass).get();
		InitiateLumenPolicyRequestWizardImpl lumenRequestObj = (InitiateLumenPolicyRequestWizardImpl) view;
		lumenRequestObj.initView(resultDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override 
	public void renderLevelOneWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO resultDTO){
		GMVPView view = views.select(viewClass).get();
		ProcessLevelOneWizardImpl levelOneObj = (ProcessLevelOneWizardImpl) view;
		levelOneObj.initView(resultDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override 
	public void renderInitiatorWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO resultDTO){
		GMVPView view = views.select(viewClass).get();
		ProcessInitiatorWizardImpl initiatorObj = (ProcessInitiatorWizardImpl) view;
		initiatorObj.initView(resultDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override 
	public void renderCoordinatorWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO resultDTO){
		GMVPView view = views.select(viewClass).get();
		ProcessCoordinatorWizardImpl coordinatorObj = (ProcessCoordinatorWizardImpl) view;
		coordinatorObj.initView(resultDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override 
	public void renderLevelTwoWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO resultDTO){
		GMVPView view = views.select(viewClass).get();
		ProcessLevelTwoWizardImpl coordinatorObj = (ProcessLevelTwoWizardImpl) view;
		coordinatorObj.initView(resultDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override 
	public void renderMISQueryWizardPage(Class<? extends GMVPView> viewClass, LumenRequestDTO resultDTO){
		GMVPView view = views.select(viewClass).get();
		ProcessMISWizardImpl misObj = (ProcessMISWizardImpl) view;
		misObj.initView(resultDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	public void setViewUpdatePaymentDetail(Class<UpdatePaymentDetailView> viewClass,
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
			boolean selectInNavigationTree) {
		
		GMVPView view = views.select(viewClass).get();
//		view.resetView();
		contentPanel.setContent((Component) view);
		UpdatePaymentDetailViewImpl createBatchViewImpl = (UpdatePaymentDetailViewImpl) view;
		createBatchViewImpl.resetView();	
		createBatchViewImpl.init(selectValueContainerForType, selectValueContainerForCPUCode, selectValueContainerForClaimant,selectValueContainerForClaimType,selectValueContainerForPaymentStatus,selectValueContainerForNonKeralaCPU,
				selectValueContainerForBatchType,selectValueContainerForZoneType,selectValueContainerForProduct);
		//createBatchViewImpl.getPenalInterestRate(createSearchLotDto);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	public void setFVRGradingViewG(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> parameter1) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchFvrReportGradingViewImpl fvrGradingViewImpl = (SearchFvrReportGradingViewImpl) view;
		fvrGradingViewImpl.init(parameter, parameter1);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setFvrReportGradingPageView(
			Class<? extends GMVPView> viewClass,
			FvrReportGradingPageDto convertClaimDto,
			NewIntimationDto intimationDto,
			SearchFvrReportGradingTableDto searchFormDto) {
		MVPView view = views.select(viewClass).get();
		FvrReportGradingPageViewImpl page = (FvrReportGradingPageViewImpl) view;
		page.init(convertClaimDto, intimationDto,
				searchFormDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
		
	@Override
    public void setUpdateInternalNotesView(Class<? extends GMVPView> viewClass,
    		NewIntimationDto newIntimationDto,ClaimDto claimDTO){
		MVPView view = views.select(viewClass).get();
		InternalNotesPageViewImpl wizard = (InternalNotesPageViewImpl) view;
		wizard.initView(newIntimationDto,claimDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
    }
	

	@Override
    public void setLumenStatusReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> cpuCodeContainer, BeanItemContainer<SelectValue> lumenStatusContainer, BeanItemContainer<SelectValue> clmTypeContainer){
		MVPView view = views.select(viewClass).get();
		LumenStatusWiseReportViewImpl lumenStatusReport = (LumenStatusWiseReportViewImpl) view;
		lumenStatusReport.init(cpuCodeContainer,lumenStatusContainer,clmTypeContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
    }
	
	@Override
	public void setLotPullBackScreen(
			Class<LotPullBackPageView> viewClass,
			SearchLotPullBackTableDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		LotPullBackPageViewImpl settlementPullBackViewImpl = (LotPullBackPageViewImpl) view;
		settlementPullBackViewImpl.initView(tableDTO);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();
	}


	@Override
	public void setAckWithoutRodReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> cpuCodeContainer, BeanItemContainer<SelectValue> docFromContainer){
		MVPView view = views.select(viewClass).get();
		AckWithoutRodReportViewImpl ackWithourtReportview = (AckWithoutRodReportViewImpl) view;
		ackWithourtReportview.init(cpuCodeContainer,docFromContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setAutoClosureView(Class<AutoClosureBatchView> class1,
			boolean b) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		AutoClosureBatchViewImpl viewImpl = (AutoClosureBatchViewImpl) view;
		viewImpl.init();
		tree.setSelectedView(class1);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
    public void setInvAssignStatusReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> cpuCodeContainer, BeanItemContainer<SelectValue> invStatusContainer, BeanItemContainer<SelectValue> clmTypeContainer){
		MVPView view = views.select(viewClass).get();
		InvAssignReportViewImpl lumenStatusReport = (InvAssignReportViewImpl) view;
		lumenStatusReport.init(cpuCodeContainer,invStatusContainer,clmTypeContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
    }
    
    @Override
    public void setOmpOutStandingReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> classificationContainer, WeakHashMap<Long,BeanItemContainer<SelectValue>> subClassificationMap){
		MVPView view = views.select(viewClass).get();
		OmpOutstandingReportViewImpl ompOutstandingReport = (OmpOutstandingReportViewImpl) view;
		ompOutstandingReport.init(classificationContainer, subClassificationMap);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
    }
    
    @Override
    public void setOmpStatusWiseReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> classificationContainer, WeakHashMap<Long,BeanItemContainer<SelectValue>> subClassificationMap,
    		BeanItemContainer<SelectValue> statusContainer, BeanItemContainer<SelectValue> lossContainer, 
    		BeanItemContainer<SelectValue> yearContainer){
		MVPView view = views.select(viewClass).get();
		OmpStatusWiseReportViewImpl ompOutstandingReport = (OmpStatusWiseReportViewImpl) view;
		ompOutstandingReport.init(classificationContainer, subClassificationMap,
									statusContainer, lossContainer, yearContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
		
    }

	@Override
	public void setCreateIntimationWithParameter(
			Class<SearchPolicyView> class1, String policyNumber,
			String healthCardNumber) {
		
		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		tree.setSelectedView(class1);
		SearchPolicyView searchPolicyWithParameter = (SearchPolicyView) view;
		searchPolicyWithParameter.searchPolicyWithParameter(policyNumber, healthCardNumber);
	
		((AbstractMVPView) view).enter();
	}
	public void setMedicalOpinionValidationView(
			Class<OpinionValidationView> viewClass,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode) {
		MVPView view = views.select(viewClass).get();
		OpinionValidationViewImpl opinionValidationViewImpl = (OpinionValidationViewImpl) view;
		opinionValidationViewImpl.init(selectValueContainerForCPUCode);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setMedicalOpinionValidationReportView(
			Class<OpinionValidationReportView> viewClass, 
			BeanItemContainer<SelectValue> roleContainer, 
			BeanItemContainer<SelectValue> empNameContainer) {
		MVPView view = views.select(viewClass).get();
		OpinionValidationReportViewImpl opinionValidationReportViewImpl = (OpinionValidationReportViewImpl) view;
		opinionValidationReportViewImpl.init(roleContainer, empNameContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	public void setAllocateCorpBufferView(Class<? extends GMVPView> viewClass) {
		MVPView view = views.select(viewClass).get();
		AllocateCorpBufferViewImpl allocateCorpBufferViewImpl = (AllocateCorpBufferViewImpl) view;
		allocateCorpBufferViewImpl.init();
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setAllocateCorporateBufferWizardView(
			Class<? extends GMVPView> viewClass,
			AllocateCorpBufferDetailDTO allocateCorpBufferDetailDTO) {
		GMVPView view = views.select(viewClass).get();
		AllocateCorpBufferWizardViewImpl wizard = (AllocateCorpBufferWizardViewImpl) view;
		wizard.initView(allocateCorpBufferDetailDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
    
	public void setViewsHoldMoniterScreen(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode,String screenName){
		GMVPView view = views.select(viewClass).get();
		view.resetView();   
		contentPanel.setContent((Component) view);
		SearchHoldMonitorScreenViewImpl searchHoldMonitorScreen = (SearchHoldMonitorScreenViewImpl) view;
		searchHoldMonitorScreen.init(type,userList,cpuCode,screenName);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
    }
    
	@Override
	public void setUpdateRodDetailsView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchUpdateRodDetailsViewImpl searchUpdateRODViewImpl = (SearchUpdateRodDetailsViewImpl) view;
		searchUpdateRODViewImpl.init(parameter,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setUpdateAadhar(Class<SearchUpdateAadharView> class1,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		SearchUpdateAadharViewImpl udateAadharDtlsViewImpl = (SearchUpdateAadharViewImpl) view;
		udateAadharDtlsViewImpl.init();
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
    
	@Override
	public void setFinApprovedNotSettledReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> cpuCodeContainer){

		MVPView view = views.select(viewClass).get();
		FinApprovedPaymentPendingReportViewImpl finApprovedNotSettledReport = (FinApprovedPaymentPendingReportViewImpl) view;
		finApprovedNotSettledReport.init(cpuCodeContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setUpdateAadharDetailsWizard(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchUpdateAadharTableDTO tableDTO) {
			GMVPView view = views.select(viewClass).get();
			view.resetView();
			UpdateAadharDetailsViewImpl updateAadharDetailsViewImpl = (UpdateAadharDetailsViewImpl) view;
			updateAadharDetailsViewImpl.init(tableDTO);
			tree.setSelectedView(viewClass);
			contentPanel.setContent((Component) view);
			((AbstractMVPView) view).enter();
	}
	
	
	//Upload NEFT Details
		@Override
		public void setUploadNEFTDetailsWizard(
				Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO) {
			GMVPView view = views.select(viewClass).get();
			
			
			UploadNEFTDetailsViewImpl wizard = (UploadNEFTDetailsViewImpl) view;
		//	wizard.initDTO(rodDTO);
			view.resetView();
			wizard.initView(rodDTO);
			tree.setSelectedView(viewClass);
			contentPanel.setContent((Component) view);
			((AbstractMVPView) view).enter();

		}
		
	//R1069
	@Override
	public void setAddAditionalpaymentWizard(
			Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO) {
		GMVPView view = views.select(viewClass).get();
		
		
		AddAditionalDocumentsPaymentInfoViewImpl wizard = (AddAditionalDocumentsPaymentInfoViewImpl) view;
	//	wizard.initDTO(rodDTO);
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();

	}
	
	//R1069
		@Override
		public void setPAAddAditionalpaymentWizard(
				Class<? extends GMVPView> viewClass, ReceiptOfDocumentsDTO rodDTO) {
			GMVPView view = views.select(viewClass).get();
			
			
			com.shaic.paclaim.addAdditionalDocPaymentInfo.search.AddAditionalDocumentsPaymentInfoViewImpl wizard = (com.shaic.paclaim.addAdditionalDocPaymentInfo.search.AddAditionalDocumentsPaymentInfoViewImpl) view;
		//	wizard.initDTO(rodDTO);
			view.resetView();
			wizard.initView(rodDTO);
			tree.setSelectedView(viewClass);
			contentPanel.setContent((Component) view);
			((AbstractMVPView) view).enter();

		}

	@Override
	public void setViewInvestigationDirectAssigement(
			Class<InvestigationDirectAssignmentView> viewclass) {
		
		
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewclass).get();
		view.resetView();
		InvestigationDirectAssignmentViewImpl vimp = (InvestigationDirectAssignmentViewImpl) view;
		vimp.initView();
		tree.setSelectedView(viewclass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	

	
	@Override
	public void setProcessRAWRequestSearch(Class<SearchProcessRawRequestView> viewClass,
			boolean selectInNavigationTree, BeanItemContainer<SelectValue> cpu,
			BeanItemContainer<SelectValue> requestBy,BeanItemContainer<SelectValue> statusByStage) 
	{
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchProcessRawRequestViewImpl searchProcessClaimViewImpl = (SearchProcessRawRequestViewImpl) view;
		searchProcessClaimViewImpl.init(cpu ,requestBy,statusByStage);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setProcessRAWRequesttWizard(
			Class<ProcessRawRequestWizard> viewClass,
			SearchProcessRawRequestTableDto tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessRawRequestWizard claimWiseView = (ProcessRawRequestWizard) view;
		tree.setSelectedView(viewClass);
		claimWiseView.init(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewAutoAllocCancelStatusReport(
			Class<AutoAllocationCancelReportView> viewClass, boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		AutoAllocationCancelReportViewImpl autoAllocationCancelReportViewImpl = (AutoAllocationCancelReportViewImpl) view;
		//autoAllocationCancelReportViewImpl.init();
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}


//	@Override
//	public void setSearchProcessNegotiationView(
//			Class<? extends GMVPView> viewClass,
//			boolean selectInNavigationTree,
//			BeanItemContainer<SelectValue> cpuCode) {
//		GMVPView view = views.select(viewClass).get();
//		view.resetView();
//		contentPanel.setContent((Component) view);
//		SearchProcessNegotiationViewImpl searchProcessNegotiationViewImpl = (SearchProcessNegotiationViewImpl) view;
//		searchProcessNegotiationViewImpl.init(cpuCode);
//		tree.setSelectedView(viewClass);
//		if (selectInNavigationTree) {
//			tree.setSelectedView(viewClass);
//		}
//		((AbstractMVPView) view).enter();
//	}

	@Override
	public void setNegotiationPreauthRequest(
			Class<? extends GMVPView> viewClass, PreauthDTO preauthDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> escalateContainer) {
		GMVPView view = views.select(viewClass).get();
		ProcessNegotiationWizardViewImpl wizard = (ProcessNegotiationWizardViewImpl) view;
				wizard.initView(preauthDto, selectValueContainer, escalateContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}		
	public void setUpdateSublimitView(Class<? extends GMVPView> viewClass,PreauthDTO reimbursementDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		UpdateSublimitWizardViewImpl updateSublimitWizardViewImpl = (UpdateSublimitWizardViewImpl) view;
		updateSublimitWizardViewImpl.init(reimbursementDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

public void setManagerFeedBack(Class<ManagerFeedBackView> class1, boolean b,BeanItemContainer<SelectValue> branchName) {
	GMVPView view = views.select(class1).get();
	view.resetView();
	ManagerFeedBackViewImpl wizard = (ManagerFeedBackViewImpl) view;
	wizard.init();
	wizard.setDropDownValues(branchName);
	tree.setSelectedView(class1);
	contentPanel.setContent((Component) view);
	((AbstractMVPView) view).enter();
}

	//feedbackPhaseII_1238
	@Override
	public void setBranchManagerFeedBackHomePage(Class<ManagerFeedBackView> class1, boolean b,BeanItemContainer<SelectValue> branchNameContainer, BranchManagerFeedbackhomePageDto homePageDto) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		ManagerFeedBackViewImpl wizard = (ManagerFeedBackViewImpl) view;
		wizard.initHomePage(homePageDto);
		wizard.setHomePageDropDownValues(branchNameContainer);
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setbranchManagerFeedback(Class<BranchManagerFeedbackView> viewClass,
			boolean selectInNavigationTree,BeanItemContainer<SelectValue> feedBack,BeanItemContainer<SelectValue> zoneValue, BeanItemContainer<SelectValue> branchValue,
			BeanItemContainer<SelectValue> feedbackStatusValue, BeanItemContainer<SelectValue> feedbackTypeValue) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		BranchManagerFeedbackViewImpl branchManagerFeedbackViewImpl = (BranchManagerFeedbackViewImpl) view;
		branchManagerFeedbackViewImpl.initView();
		branchManagerFeedbackViewImpl.setDropDownValues(feedBack,zoneValue,branchValue,feedbackStatusValue,feedbackTypeValue);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setBranchManagerFeedbackReportView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> feedbackContainer, BeanItemContainer<SelectValue> zoneContainer, BeanItemContainer<SelectValue> periodContainer/*, BeanItemContainer<SelectValue> branchContainer*/){

		MVPView view = views.select(viewClass).get();
		BranchManagerFeedBackReportViewImpl bmFeedBackReport = (BranchManagerFeedBackReportViewImpl) view;
		bmFeedBackReport.init(feedbackContainer, zoneContainer, periodContainer/*, branchContainer*/);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

		
	public void setViewTopUpPolicy(
			Class<TopUpPolicyMasterView> viewclass) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewclass).get();
		view.resetView();
		TopUpPolicyMasterViewImpl vimp = (TopUpPolicyMasterViewImpl) view;
		vimp.initView();
		tree.setSelectedView(viewclass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	public void setViewFraudIdentification(Class<FraudIdentificationView> viewclass) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewclass).get();
		view.resetView();
		FraudIdentificationViewImpl vimp = (FraudIdentificationViewImpl) view;
		vimp.initView();
		tree.setSelectedView(viewclass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
//	Commented the below Cashless Screen	
//	public void setViewUncheckNegotiation(
//			Class<SearchUncheckNegotiationView> viewclass) {
//		// TODO Auto-generated method stub
//		GMVPView view = views.select(viewclass).get();
//		view.resetView();
//		SearchUncheckNegotiationViewImpl vimp = (SearchUncheckNegotiationViewImpl) view;
//	//	vimp.initView();
//		tree.setSelectedView(viewclass);
//		contentPanel.setContent((Component) view);
//		((AbstractMVPView) view).enter();
//		
//	}
	
	public void setBranchManagerFeedbackReportingPatternView(Class<? extends GMVPView> viewClass,
    		BeanItemContainer<SelectValue> feedbackContainer, BeanItemContainer<SelectValue> zoneContainer, BeanItemContainer<SelectValue> periodContainer){
		
		MVPView view = views.select(viewClass).get();
		BranchManagerFeedBackReportingPatternViewImpl bmFeedBackReportingPattern = (BranchManagerFeedBackReportingPatternViewImpl) view;
		bmFeedBackReportingPattern.init(feedbackContainer, zoneContainer, periodContainer);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setViewPreviousManagerFeedBack(
			Class<BranchManagerPreviousFeedbackView> class1) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		BranchManagerPreviousFeedbackViewImpl vimp = (BranchManagerPreviousFeedbackViewImpl) view;
		vimp.initView();
		vimp.loadPreviousDropDownValues();
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewPreviousManagerLink(Class<BranchManagerPreviousFeedbackView> class1,FeedbackStatsDto fbStatusDTO, Long fbStatus) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(class1).get();
		view.resetView();
		BranchManagerPreviousFeedbackViewImpl vimp = (BranchManagerPreviousFeedbackViewImpl) view;
		vimp.initView();
		vimp.loadPreviousDropDownValuesForHomePage(fbStatusDTO,fbStatus);
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	

	public void setViewNegotiationReport(Class<NegotiationReportView> class1,Boolean b,BeanItemContainer<SelectValue> cpu, BeanItemContainer<SelectValue> empContainer) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		NegotiationReportViewImpl negViewImpl = (NegotiationReportViewImpl) view;
		negViewImpl.init(cpu,empContainer);
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	public void setViewFraudAnalysisReport(Class<FraudReportView> class1) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		FraudReportViewImpl fraudViewImpl = (FraudReportViewImpl) view;
		fraudViewImpl.init();
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}


	@Override
	public void setMagazineDetails(List<MasMagazineDocument> magazineList) {
		// TODO Auto-generated method stub
		toolbar.setMagazineTableValues(magazineList);
	}

	@Override
	public void setUploadBedPhoto(Class<SearchBedPhoto> class1,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		SearchBedPhotoViewImpl uploadBedPhtoViewImpl = (SearchBedPhotoViewImpl) view;
		uploadBedPhtoViewImpl.init();
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	public void setUserMgmtView(Class<UserManagementView> viewClass, boolean b,
			UserManagementDTO userManagementDTO,
			BeanItemContainer<SelectValue> userTypeContainer) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		UserManagementView doctorDetailsView = (UserManagementView) view;
		tree.setSelectedView(viewClass);
		//doctorDetailsView.init(userManagementDTO, userTypeContainer);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setUploadBedPhotoWizard(Class<UploadBedPhotoView> class1,
			boolean b, SearchBedPhotoTableDTO tableDTO) {
		GMVPView view = views.select(class1).get();
		view.resetView();
		UploadBedPhotoViewImpl uploadBedViewImpl = (UploadBedPhotoViewImpl) view;
		uploadBedViewImpl.init(tableDTO);
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	public void setUserActivationView(Class<UserACtivationView> viewClass,
			boolean b, UserManagementDTO userManagementDTO,
			BeanItemContainer<SelectValue> userTypeContainer,
			BeanItemContainer<SelectValue>  documentTypeContainer,
			BeanItemContainer<SelectValue> claimFlagTypeContainer) {
			GMVPView view = views.select(viewClass).get();
			view.resetView();
			UserACtivationView doctorDetailsView = (UserACtivationView) view;
			tree.setSelectedView(viewClass);
			doctorDetailsView.init(userManagementDTO, userTypeContainer,documentTypeContainer,claimFlagTypeContainer);
			contentPanel.setContent((Component) view);
			((AbstractMVPView) view).enter();
		
	}
	
	public void setGmcAutomailer(
			Class<GmcAutomailerView> viewclass) {
		GMVPView view = views.select(viewclass).get();
		view.resetView();
		GmcAutomailerViewImpl vimp = (GmcAutomailerViewImpl) view;
	//	vimp.initView();
		tree.setSelectedView(viewclass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewCVCAudit(Class<SearchCVCView> viewclass, boolean b) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewclass).get();
		view.resetView();
		SearchCVCViewImpl vimp = (SearchCVCViewImpl) view;
		vimp.init();
		tree.setSelectedView(viewclass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setCVCWizardView(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchCVCTableDTO bean) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		CVCPageViewImpl uploadInvestigationReportViewImpl = (CVCPageViewImpl) view;
		uploadInvestigationReportViewImpl.init(bean);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override 
	public void renderOMPRegistationPage(Class<? extends GMVPView> viewClass, OMPNewRegistrationSearchDTO resultDTO){
		GMVPView view = views.select(viewClass).get();
		OMPNewRegistrationPageViewImpl ompRegisObj = (OMPNewRegistrationPageViewImpl) view;
		ompRegisObj.initView(resultDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void showInfoPopUp(String eMsg) {
		 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(eMsg, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());		
	}
	
	/*@Override
	public void setHospitalScoringView(Class<HospitalScoringDetailsView> class1) {
		// TODO Auto-generated method stub
		
	}*/
	@Override
	public void setCVCAuditActionWizardView(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchCVCAuditActionTableDTO bean) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		CVCAuditActionPageViewImpl cvcAuditActionPageViewImpl = (CVCAuditActionPageViewImpl) view;
		cvcAuditActionPageViewImpl.init(bean);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewCVCAuditActionSearch(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchCVCAuditActionViewImpl searchCVCAuditActionViewImpl = (SearchCVCAuditActionViewImpl) view;
		searchCVCAuditActionViewImpl.init();
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewClaimsAuditReport(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		ClaimsAuditReportViewImpl claimsAuditReportViewImpl = (ClaimsAuditReportViewImpl) view;
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setCVCAuditQryApprovalWizardView(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchCVCAuditActionTableDTO bean) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		CVCAuditQryApprovalPageViewImpl cvcAuditActionPageViewImpl = (CVCAuditQryApprovalPageViewImpl) view;
		cvcAuditActionPageViewImpl.init(bean);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setViewCVCAuditQryApprovalSearch(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchCVCAuditQryApprovalViewImpl searchCVCAuditActionViewImpl = (SearchCVCAuditQryApprovalViewImpl) view;
		searchCVCAuditActionViewImpl.init();
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	public void setViewForSearchClaimCommonBillingAndFinancial(
			Class<SearchProcessClaimCommonBillingAndFinancialsView> class1, boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> productName,
			BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {

		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessClaimCommonBillingAndFinancialsView searchProcessClaimRequestViewImpl = (SearchProcessClaimCommonBillingAndFinancialsView) view;
//		searchProcessClaimRequestViewImpl.setUpDropDownValues(claimType, productName, cpuCode,type,selectValueForPriority,statusByStage);
		tree.setSelectedView(class1);
		if (selectInNavigationTree) {
			tree.setSelectedView(class1);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setRRCStatusScreen(
			Class<SearchRRCStatusView> viewClass, boolean b,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueForRRCRequestType,
			BeanItemContainer<SelectValue> selectValueForEligibility) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchRRCStatusViewImpl searchModifyRRCRequestViewImpl = (SearchRRCStatusViewImpl) view;
		searchModifyRRCRequestViewImpl.init(selectValueContainerForCPUCode , selectValueForRRCRequestType,selectValueForEligibility);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	
//	@Override
//	public void setViewCVCAuditClsQryRlySearch(Class<? extends GMVPView> viewClass,
//			boolean selectInNavigationTree, SearchCVCAuditClsQryFormDTO srchFrmDto) {
//		GMVPView view = views.select(viewClass).get();
//		view.resetView();
//		contentPanel.setContent((Component) view);
//		SearchCVCAuditClsQryViewImpl searchCVCAuditActionViewImpl = (SearchCVCAuditClsQryViewImpl) view;
//		searchCVCAuditActionViewImpl.init(srchFrmDto);
//		tree.setSelectedView(viewClass);
//		if (selectInNavigationTree) {
//			tree.setSelectedView(viewClass);
//		}
//		((AbstractMVPView) view).enter();
//		
//	}

	@Override
	public void setViewCVCAuditRmbQryRlySearch(
			Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchCVCAuditClsQryFormDTO srchFrmDto) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchCVCAuditReimbQryViewImpl searchCVCAuditActionViewImpl = (SearchCVCAuditReimbQryViewImpl) view;
		searchCVCAuditActionViewImpl.init(srchFrmDto);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setViewCVCAuditFaQryRlySearch(
			Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, SearchCVCAuditClsQryFormDTO srchFrmDto) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchCVCAuditFaQryViewImpl searchCVCAuditActionViewImpl = (SearchCVCAuditFaQryViewImpl) view;
		searchCVCAuditActionViewImpl.init(srchFrmDto);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}


//	@Override
//	public void setViewCVCAuditClsQrySearch(
//			Class<? extends GMVPView> viewClass,
//			boolean selectInNavigationTree,
//			SearchCVCAuditActionTableDTO investigationKey) {
//		GMVPView view = views.select(viewClass).get();
//		view.resetView();
//		contentPanel.setContent((Component) view);
//		CVCAuditClsQryPageViewImpl cVCClsQryRlyViewImpl = (CVCAuditClsQryPageViewImpl) view;
//		cVCClsQryRlyViewImpl.init(investigationKey);
//		tree.setSelectedView(viewClass);
//		if (selectInNavigationTree) {
//			tree.setSelectedView(viewClass);
//		}
//		((AbstractMVPView) view).enter();
//	}

	@Override
	public void setViewCVCAuditFaQrySearch(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			SearchCVCAuditActionTableDTO investigationKey) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		CVCAuditFaQryPageViewImpl cVCClsQryRlyViewImpl = (CVCAuditFaQryPageViewImpl) view;
		cVCClsQryRlyViewImpl.init(investigationKey);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setViewCVCAuditRmbQrySearch(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			SearchCVCAuditActionTableDTO auditActionQryDto) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		CVCAuditReimbQryPageViewImpl cVCClsQryRlyViewImpl = (CVCAuditReimbQryPageViewImpl) view;
		cVCClsQryRlyViewImpl.init(auditActionQryDto);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	public void setViewClaimsAlert(Class<ClaimsAlertMasterView> viewclass) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewclass).get();
		view.resetView();
		ClaimsAlertMasterViewImpl vimp = (ClaimsAlertMasterViewImpl) view;
		vimp.initView();
		tree.setSelectedView(viewclass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	public void setViewSettlementProcessClaimClaimOP(
			Class<SearchSettlementLetterProcessOPClaimRequestView> viewClass,
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode,
			BeanItemContainer<SelectValue> selectValueContainerForType,
			BeanItemContainer<SelectValue> selectValueContainerForClaimant,
			BeanItemContainer<SelectValue> selectValueContainerForClaimType,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentStatus,
			BeanItemContainer<SpecialSelectValue> selectValueContainerForProduct,
			BeanItemContainer<SelectValue> docVerified,
			BeanItemContainer<SelectValue> selectValueContainerForPaymentMode,
			BeanItemContainer<SelectValue> selectValueContainerForPIOCode,
			boolean selectInNavigationTree) {
		
		GMVPView view = views.select(viewClass).get();
		//view.resetView();
		
		contentPanel.setContent((Component) view);
		SearchSettlementLetterProcessOPClaimRequestViewImpl createSettlementLetterBatchViewImpl = (SearchSettlementLetterProcessOPClaimRequestViewImpl) view;
		createSettlementLetterBatchViewImpl.resetView();
		createSettlementLetterBatchViewImpl.init(selectValueContainerForType, selectValueContainerForCPUCode, selectValueContainerForClaimant, 
				selectValueContainerForClaimType, selectValueContainerForPaymentStatus,selectValueContainerForProduct,selectValueContainerForPaymentMode,docVerified,selectValueContainerForPIOCode);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
//	@Override
//	public void setViewFLPAutoAllocation(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree) {
//		GMVPView view = views.select(viewClass).get();
//		view.resetView();
//		contentPanel.setContent((Component) view);
//		SearchFLPAutoAllocationViewImpl flpAutoAllocationViewImpl = (SearchFLPAutoAllocationViewImpl) view;
//		flpAutoAllocationViewImpl.manuallyDoSearchForCompletedCases();
//		tree.setSelectedView(viewClass);
//		if (selectInNavigationTree) {
//			tree.setSelectedView(viewClass);
//		}
//		((AbstractMVPView) view).enter();
//	}
	
	public void setViewHoldMonitorFLPScreen(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree, 
			BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode){
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchHoldMonitorFLPScreenViewImpl searchHoldMonitorScreen = (SearchHoldMonitorFLPScreenViewImpl) view;
		searchHoldMonitorScreen.init(type,userList,cpuCode);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewMonitoringReport(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		MetabaseReportViewImpl metabaseReportViewImpl = (MetabaseReportViewImpl) view;
		metabaseReportViewImpl.init();
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewProcessDataCorrection(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,ProcessDataCorrectionDTO correctionDTO) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		DataCorrectionViewImpl correctionViewImpl = (DataCorrectionViewImpl) view;
		correctionViewImpl.initView(correctionDTO);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setReconsiderationFlagReqWizardView(
			Class<? extends GMVPView> viewClass,
			SearchFlagReconsiderationReqTableDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		ReconsiderationFlagRequestViewImpl reconsiderationFlagRequestViewImpl = (ReconsiderationFlagRequestViewImpl) view;
		reconsiderationFlagRequestViewImpl.initView(tableDTO);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();
	}

	public void setViewProcessDataCorrectionHistorical(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,ProcessDataCorrectionDTO correctionDTO) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		DataCorrectionHistoricalViewImpl correctionViewImpl = (DataCorrectionHistoricalViewImpl) view;
		correctionViewImpl.initView(correctionDTO);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void showCashLessLoginSuccess(){
		String url = BPMClientContext.CASHLESS_URL; 
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
	}
	
	@Override
	public void setViewProcessICACRequest(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,SearchProcessICACTableDTO correctionDTO) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		ProcessICACRequestViewImpl ProcessIcscViewImpl = (ProcessICACRequestViewImpl) view;
		ProcessIcscViewImpl.initView(correctionDTO);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	public void setProcessPCCCoOrdinatorRequestWizard(Class<? extends GMVPView> viewClass,PccDetailsTableDTO pccDetailsTableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessPccCoOrdinateRequestWizardViewImpl claimWiseView = (ProcessPccCoOrdinateRequestWizardViewImpl) view;
		tree.setSelectedView(viewClass);
		claimWiseView.initView(pccDetailsTableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setProcessPCCProcessorRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO pccDetailsTableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessPCCProcessorRequestWizardViewImpl claimWiseView = (ProcessPCCProcessorRequestWizardViewImpl) view;
		tree.setSelectedView(viewClass);
		claimWiseView.initView(pccDetailsTableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setProcessPCCReviewerRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO pccDetailsTableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessPCCReviewerRequestWizardViewImpl claimWiseView = (ProcessPCCReviewerRequestWizardViewImpl) view;
		tree.setSelectedView(viewClass);
		claimWiseView.initView(pccDetailsTableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setProcessPCCDivisionHeadRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO pccDetailsTableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessPCCDivisionHeadRequestWizardViewImpl claimWiseView = (ProcessPCCDivisionHeadRequestWizardViewImpl) view;
		tree.setSelectedView(viewClass);
		claimWiseView.initView(pccDetailsTableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override	
	public void setProcessPCCZonalMedicalHeadRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessPCCZonalMedicalHeadRequestWizardViewImpl claimWiseView = (ProcessPCCZonalMedicalHeadRequestWizardViewImpl) view;
		tree.setSelectedView(viewClass);
		claimWiseView.initView(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setProcessPCCZonalCoordinatorRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessPCCZonalCoordinatorRequestWizardViewImpl claimWiseView = (ProcessPCCZonalCoordinatorRequestWizardViewImpl) view;
		tree.setSelectedView(viewClass);
		claimWiseView.initView(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setProcessPCCHrmCoordinatorRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessPCCHrmCoordinatorRequestWizardViewImpl claimWiseView = (ProcessPCCHrmCoordinatorRequestWizardViewImpl) view;
		tree.setSelectedView(viewClass);
		claimWiseView.initView(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setHRMPSearch(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchHRMPViewImpl searchCVCAuditActionViewImpl = (SearchHRMPViewImpl) view;
		searchCVCAuditActionViewImpl.init();
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setMarketingEscalationReportView(Class<MarketingEscalationReportView> viewClass, boolean b) {

		MVPView view = views.select(viewClass).get();
		MarketingEscalationReportViewImpl marketingEscalationReportViewImpl = (MarketingEscalationReportViewImpl) view;
		marketingEscalationReportViewImpl.init();
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}

	@Override
	public void setHRMPWizardView(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchHRMPTableDTO hrmDto) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		HRMPWizardViewImpl cvcAuditActionPageViewImpl = (HRMPWizardViewImpl) view;
		cvcAuditActionPageViewImpl.init(hrmDto);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
	public void setrecordMarkEscView(Class<RecMarketingEscalationView> class1,
			RecordMarkEscDTO recMarEscDto) {
		
		MVPView view = views.select(class1).get();
		RecMarketingEscalationViewImpl wizard = (RecMarketingEscalationViewImpl) view;
		wizard.initView(recMarEscDto);
		tree.setSelectedView(class1);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();
	}
	public void setStopPyamentValidationScreen(
			Class<PopupStopPaymentValidateWizard> viewClass,
			StopPaymentRequestDto tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		StopPaymentValidationWizardPage stopPaymentValidationWizardPage = (StopPaymentValidationWizardPage) view;
		stopPaymentValidationWizardPage.initView(tableDTO);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setStopPyamentRequestScreen(
			Class<PopupStopPaymentRequestWizard> viewClass,
			StopPaymentRequestDto tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		StopPaymentRequestWizardPage stopPaymentRequestWizardPage = (StopPaymentRequestWizardPage) view;
		stopPaymentRequestWizardPage.initView(tableDTO);
		tree.setSelectedView(viewClass);
		((AbstractMVPView) view).enter();
	}
	public void setProcessPCCReassignHrmCoordinatorRequestWizardView(Class<? extends GMVPView> viewClass,PccDetailsTableDTO tableDTO) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		ProcessPCCReassignHrmCoordinatorRequestWizardViewImpl claimWiseView = (ProcessPCCReassignHrmCoordinatorRequestWizardViewImpl) view;
		tree.setSelectedView(viewClass);
		claimWiseView.initView(tableDTO);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setViewForSearchClaimCommonBillingAndFinancialAutoAllocation(
			Class<SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView> class1, boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> productName,
			BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {

		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView searchProcessClaimRequestViewImpl = (SearchProcessClaimCommonBillingAndFinancialsAutoAllocationView) view;
		searchProcessClaimRequestViewImpl.manuallyDoSearchForCompletedCases();
		tree.setSelectedView(class1);
		if (selectInNavigationTree) {
			tree.setSelectedView(class1);
		}
		((AbstractMVPView) view).enter();
	}
	
	public void setViewGetTask(Class<? extends GMVPView> viewClass,
			BeanItemContainer<SelectValue> refferedByDocSrc,
			boolean selectInNavigationTree,boolean isReimburement, BeanItemContainer<SelectValue> cpuCodeContainer,SubmitSpecialistFormDTO dto) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SubmitSpecialistViewImpl submitSpecialistViewImpl = (SubmitSpecialistViewImpl) view;
		submitSpecialistViewImpl.initResetView(refferedByDocSrc,isReimburement, cpuCodeContainer,dto);
		submitSpecialistViewImpl.setDropDownValues(cpuCodeContainer);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setViewForSearchProcessClaimRequestAutoAllocation(
			Class<SearchProcessClaimRequestAutoAllocationView> class1, boolean selectInNavigationTree) {

		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessClaimRequestAutoAllocationView searchProcessClaimRequestViewImpl = (SearchProcessClaimRequestAutoAllocationView) view;
		searchProcessClaimRequestViewImpl.manuallyDoSearchForCompletedCases();
		tree.setSelectedView(class1);
		if (selectInNavigationTree) {
			tree.setSelectedView(class1);
		}
	}
	
	@Override
	public void setPhysicalVerification(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,String screenName) {
		GMVPView view = views.select(viewClass).get();
		PhysicalDocumentReceivedMakerViewImpl wizard = (PhysicalDocumentReceivedMakerViewImpl) view;
		wizard.initView(screenName);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	public void setViewOMPDocumentUploadProcess(
			Class<OMPProcessOmpAcknowledgementDocumentsView> viewClass,
			BeanItemContainer<SelectValue> classification, boolean selectInNavigationTree) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		//OMPProcessOmpAcknowledgementDocumentsViewImpl oMPProcessOmpClaimProcessorViewImpl = (OMPProcessOmpAcknowledgementDocumentsViewImpl) view;
		//oMPProcessOmpClaimProcessorViewImpl.init(classification);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	public void setViewsHoldMoniterScreenForMA(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode,String screenName){
		GMVPView view = views.select(viewClass).get();
		view.resetView();   
		contentPanel.setContent((Component) view);
		SearchHoldMonitorScreenViewImpl searchHoldMonitorScreen = (SearchHoldMonitorScreenViewImpl) view;
		searchHoldMonitorScreen.init(type,userList,cpuCode,screenName);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	public void setViewGGetTask(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,SearchPEDRequestApproveFormDTO dto)
	{
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchPEDRequestApproveViewImpl searchPEDRequestApproveViewImpl = (SearchPEDRequestApproveViewImpl) view;
		searchPEDRequestApproveViewImpl.initResetView(dto);
		//searchPEDRequestApproveViewImpl.setDropDownValues(cpuCodeContainer);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}

	@Override
	public void setOmpClaimProcessorAknview(
			Class<OMPAcknowledgementDocumentsPageWizard> viewClass,
			OMPClaimProcessorDTO claimProcessorDTO,
			BeanItemContainer<SelectValue> classification,
			BeanItemContainer<SelectValue> subClassification,
			BeanItemContainer<SelectValue> paymentTo,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> eventCode,
			BeanItemContainer<SelectValue> currencyValue,
			BeanItemContainer<SelectValue> negotiatorName,
			BeanItemContainer<SelectValue> modeOfReciept,
			BeanItemContainer<SelectValue> documentRecievedFrom,
			BeanItemContainer<SelectValue> documentType,
			BeanItemContainer<SelectValue> country) {

		MVPView view = views.select(viewClass).get();
		OMPAcknowledgementDocumentsWizardViewImpl wizard = (OMPAcknowledgementDocumentsWizardViewImpl) view;
		wizard.init(claimProcessorDTO,classification,subClassification,paymentTo,paymentMode,eventCode,currencyValue,negotiatorName,modeOfReciept,
				documentRecievedFrom,documentType,country);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		claimProcessorDTO.setIsOnLoad(Boolean.FALSE);

	}
	
	@Override
	public void showReportsLoginSuccess(){
		String url = BPMClientContext.REPORTS_URL; 
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
	}
	
	@Override
	public void setViewCreateOnlineROD(Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> parameter,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchCreateOnlineRODViewImpl searchCreateRODViewImpl = (SearchCreateOnlineRODViewImpl) view;
		searchCreateRODViewImpl.init(parameter,selectValueForPriority,statusByStage,selectValueForUploadedDocument);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
		
	}
	
	@Override
	public void setCreateOnlineRODWizardView(Class<? extends GMVPView> viewClass,ReceiptOfDocumentsDTO rodDTO) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		CreateOnlineRODWizardViewImpl wizard = (CreateOnlineRODWizardViewImpl) view;
		view.resetView();
		wizard.initView(rodDTO);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);

		((AbstractMVPView) view).enter();

	}
	
	@Override
	public void setViewProcessDataCorrectionPriority(Class<? extends GMVPView> viewClass,boolean selectInNavigationTree,ProcessDataCorrectionDTO correctionDTO) {
		
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		DataCorrectionPriorityViewImpl correctionViewImpl = (DataCorrectionPriorityViewImpl) view;
		correctionViewImpl.initView(correctionDTO);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	public void setViewForSearchFinancialApprovalAutoAllocation(
			Class<SearchProcessFinancialApprovalAutoAllocationView> class1, boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> productName,
			BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage) {

		GMVPView view = views.select(class1).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessFinancialApprovalAutoAllocationView searchProcessClaimRequestViewImpl = (SearchProcessFinancialApprovalAutoAllocationView) view;
		searchProcessClaimRequestViewImpl.manuallyDoSearchForCompletedCases();
		tree.setSelectedView(class1);
		if (selectInNavigationTree) {
			tree.setSelectedView(class1);
		}
		((AbstractMVPView) view).enter();
	}
	
	public void setViewsHoldMoniterScreenForFA(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree,
			BeanItemContainer<SelectValue> type,BeanItemContainer<SelectValue> userList,BeanItemContainer<SelectValue> cpuCode,String screenName){
		GMVPView view = views.select(viewClass).get();
		view.resetView();   
		contentPanel.setContent((Component) view);
		SearchHoldMonitorScreenViewImpl searchHoldMonitorScreen = (SearchHoldMonitorScreenViewImpl) view;
		searchHoldMonitorScreen.init(type,userList,cpuCode,screenName);
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	public void setViewForSearchClaimBillingAutoAllocation(Class<SearchProcessClaimBillingAutoAllocationView> viewClass, boolean selectInNavigationTree) {

		GMVPView view = views.select(viewClass).get();
		view.resetView();
		contentPanel.setContent((Component) view);
		SearchProcessClaimBillingAutoAllocationView searchProcessClaimRequestViewImpl = (SearchProcessClaimBillingAutoAllocationView) view;
		searchProcessClaimRequestViewImpl.manuallyDoSearchForCompletedCases();
		tree.setSelectedView(viewClass);
		if (selectInNavigationTree) {
			tree.setSelectedView(viewClass);
		}
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setUpdateDispatchReportView(Class<DispatchDetailsReportView> viewClass, boolean b) {
		MVPView view = views.select(viewClass).get();
		DispatchDetailsReportViewImpl detailsReportViewImpl = (DispatchDetailsReportViewImpl) view;
		detailsReportViewImpl.init();
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();	
	}

	@Override
	public void setPostProcessCVCSearch(Class<? extends GMVPView> viewClass, boolean selectInNavigationTree) {
		// TODO Auto-generated method stub
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		SearchPostProcessCVCViewImpl vimp = (SearchPostProcessCVCViewImpl) view;
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
	}
	
	@Override
	public void setPostProcessCVCWizardView(
			Class<? extends GMVPView> viewClass,
			boolean selectInNavigationTree, SearchCVCTableDTO bean) {
		GMVPView view = views.select(viewClass).get();
		view.resetView();
		PostProcessClaimCVCAuditViewImpl uploadInvestigationReportViewImpl = (PostProcessClaimCVCAuditViewImpl) view;
		uploadInvestigationReportViewImpl.init(bean);
		tree.setSelectedView(viewClass);
		contentPanel.setContent((Component) view);
		((AbstractMVPView) view).enter();
		
	}
}

