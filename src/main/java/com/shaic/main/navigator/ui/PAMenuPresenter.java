package com.shaic.main.navigator.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.MasterGST;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.claim.ReimbursementRejectionDto;
//import com.shaic.claim.clearcashless.pasearchcashless.PASearchCancelCashlessView;
//import com.shaic.claim.clearcashless.searchcashless.SearchCancelCashlessView;
import com.shaic.claim.common.NursingChargesMatchingDTO;
import com.shaic.claim.common.RoomRentMatchingDTO;
import com.shaic.claim.cpuskipzmr.SkipZMRView;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.fileUpload.CoordinatorService;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.intimation.unlock.SearchUnlockIntimationView;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.leagalbilling.LegalTaxDeduction;
import com.shaic.claim.leagalbilling.LegalTaxDeductionMapper;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.PreauthWizard;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.premedical.search.ProcessPreMedicalService;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.claim.reimbursement.billing.benefits.wizard.service.ProcessClaimRequestBenefitsService;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.HopitalizationCalulationDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsHospSettlementDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsPostHospSettlementDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.OtherInsPreHospSettlementDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.billing.dto.PostHopitalizationDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.PreHopitalizationDetailsDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDiagnosisDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.TreatmentQualityVerificationDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.mapper.ZonalMedicalReviewMapper;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestWizard;
import com.shaic.claim.reimbursement.processDraftRejectionLetterDetail.ClaimRejectionDto;
import com.shaic.claim.reimbursement.processdraftquery.ClaimQueryDto;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsTableDTO;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedPageTableDTO;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedSubmitService;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedTableDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claims.reibursement.addaditionaldocuments.SelectRODtoAddAdditionalDocumentsDTO;
import com.shaic.claims.reibursement.addaditionaldocuments.SelectRODtoAddAdditionalDocumentsMapper;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.ClaimService;
import com.shaic.domain.ClaimVerification;
import com.shaic.domain.CoPayService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.FVRGradingMaster;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitalisation;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.MasCopay;
import com.shaic.domain.MasOmbudsman;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PostHospitalisation;
import com.shaic.domain.PreHospitalisation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.PreviousClaimedHistory;
import com.shaic.domain.PreviousClaimedHospitalization;
import com.shaic.domain.Product;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefitsDetails;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.domain.Status;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.UpdateHospital;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.CloseClaim;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthEscalate;
import com.shaic.domain.preauth.PreauthQuery;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.BillItemMapping;
import com.shaic.domain.reimbursement.MedicalApprover;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.domain.reimbursement.Specialist;
import com.shaic.gpaclaim.unnamedriskdetails.SearchUnnamedRiskDetailsTableDTO;
import com.shaic.gpaclaim.unnamedriskdetails.SearchUnnamedRiskDetailsView;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageDTO;
import com.shaic.gpaclaim.unnamedriskdetails.UnnamedRiskDetailsPageView;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.domain.ConvertClaimMapper;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.paclaim.addAdditinalDocument.search.PAAddAdditionalDocumentsView;
import com.shaic.paclaim.addAdditinalDocument.search.PAAddAdditionalDocumentsWizardView;
import com.shaic.paclaim.addAdditionalDocPaymentInfo.search.AddAditionalDocumentsPaymentInfoView;
import com.shaic.paclaim.addAdditionalDocPaymentInfo.search.SearchAddAdditionalDocumentPaymentInfoTableDTO;
import com.shaic.paclaim.addAdditionalDocPaymentInfo.search.SearchAddAdditionalDocumentPaymentInfoView;
import com.shaic.paclaim.billing.hospprocessclaimbilling.page.PAHospCoverBillingWizard;
import com.shaic.paclaim.billing.processclaimbilling.page.PABillingWizard;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoverOnLoadDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.TableBenefitsDTO;
import com.shaic.paclaim.billing.processclaimbilling.search.PASearchProcessClaimBillingService;
import com.shaic.paclaim.billing.processclaimbilling.search.PASearchProcessClaimBillingView;
//import com.shaic.paclaim.cashless.downsize.search.PASearchDownsizeCashLessProcessView;
import com.shaic.paclaim.cashless.downsize.wizard.PADownsizePreauthWizard;
//import com.shaic.paclaim.cashless.enhancement.search.PASearchEnhancementView;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancementWizard;
import com.shaic.paclaim.cashless.fle.search.PASearchPreMedicalProcessingEnhancementTableDTO;
//import com.shaic.paclaim.cashless.fle.search.PASearchPreMedicalProcessingEnhancementView;
import com.shaic.paclaim.cashless.fle.wizard.wizardfiles.PAPremedicalEnhancementWizard;
import com.shaic.paclaim.cashless.flp.search.PAProcessPreMedicalTableDTO;
//import com.shaic.paclaim.cashless.flp.search.PAProcessPreMedicalView;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizard;
import com.shaic.paclaim.cashless.preauth.search.PASearchPreauthTableDTO;
//import com.shaic.paclaim.cashless.preauth.search.PASearchPreauthView;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizard;
//import com.shaic.paclaim.cashless.processdownsize.search.PASearchDownsizeRequestView;
import com.shaic.paclaim.cashless.processdownsize.wizard.PADownsizePreauthRequestWizard;
import com.shaic.paclaim.cashless.withdraw.search.PASearchWithdrawCashLessProcessTableDTO;
//import com.shaic.paclaim.cashless.withdraw.search.PASearchWithdrawCashLessProcessView;
import com.shaic.paclaim.cashless.withdraw.wizard.PAWithdrawPreauthWizard;
import com.shaic.paclaim.convertClaimToReimb.ConvertPAClaimPageViewImpl;
import com.shaic.paclaim.convertClaimToReimb.SearchConvertPAClaimView;
import com.shaic.paclaim.convertClaimToReimbursement.convertReimbursementPage.PAConvertReimbursementPageView;
import com.shaic.paclaim.convertClaimToReimbursement.search.PASearchConvertReimbursementView;
import com.shaic.paclaim.financial.claimapproval.hosiptal.PASearchProcessClaimAprovalHosView;
import com.shaic.paclaim.financial.claimapproval.nonhosiptal.PASearchProcessClaimAprovalNonHosView;
import com.shaic.paclaim.financial.claimapproval.nonhosiptalpage.PAClaimAprNonHosWizard;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.PANonHospFinancialWizard;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.search.PASearchProcessClaimFinancialsNonHospView;
import com.shaic.paclaim.generateCoveringLetter.DecideOnPACoveringLetterWizardViewImpl;
import com.shaic.paclaim.generateCoveringLetter.SearchGenerateCoveringLetterPAView;
import com.shaic.paclaim.generateCoveringLetter.SearchGenerateCoveringLetterPAViewImpl;
import com.shaic.paclaim.health.reimbursement.billing.search.PAHealthSearchProcessClaimBillingTableDTO;
import com.shaic.paclaim.health.reimbursement.billing.search.PAHealthSearchProcessClaimBillingView;
import com.shaic.paclaim.health.reimbursement.billing.wizard.wizardfiles.PAHealthBillingWizard;
import com.shaic.paclaim.health.reimbursement.financial.search.PAHealthSearchProcessClaimFinancialsTableDTO;
import com.shaic.paclaim.health.reimbursement.financial.search.PAHealthSearchProcessClaimFinancialsView;
import com.shaic.paclaim.health.reimbursement.financial.wizard.PAHealthFinancialWizard;
import com.shaic.paclaim.health.reimbursement.medicalapproval.search.PAHealthSearchProcessClaimRequestFormDTO;
import com.shaic.paclaim.health.reimbursement.medicalapproval.search.PAHealthSearchProcessClaimRequestTableDTO;
import com.shaic.paclaim.health.reimbursement.medicalapproval.search.PAHealthSearchProcessClaimRequestView;
import com.shaic.paclaim.health.reimbursement.medicalapproval.wizard.PAHealthClaimRequestWizard;
import com.shaic.paclaim.healthsettlementpullback.PAHospSettlementPullBackView;
import com.shaic.paclaim.healthsettlementpullback.dto.PAHospSearchSettlementPullBackDTO;
import com.shaic.paclaim.healthsettlementpullback.searchsettlementpullback.PAHospSearchSettlementPullBackView;
import com.shaic.paclaim.manageclaim.closeclaim.SearchInProcessRodLevel.PASearchCloseClaimInProcessView;
import com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel.PACloseClaimPageDTO;
import com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel.PACloseClaimTableDTO;
import com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel.PACloseClaimView;
import com.shaic.paclaim.manageclaim.closeclaim.pageRodLevel.PACloseClaimRodLevelView;
import com.shaic.paclaim.manageclaim.closeclaim.searchBasedClaimlevel.PASearchCloseClaimTableDTO;
import com.shaic.paclaim.manageclaim.closeclaim.searchBasedClaimlevel.PASearchCloseClaimView;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimRODLevelView;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimTableDTORODLevel;
import com.shaic.paclaim.manageclaim.closeclaimInProcess.pageRODLevel.PACloseClaimInProcessView;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageClaimLevel.PAHealthReopenClaimPageDTO;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageClaimLevel.PAHealthReopenClaimTableDTO;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageClaimLevel.PAHealthReopenClaimView;
import com.shaic.paclaim.manageclaim.healthreopenclaim.pageRodLevel.PAHealthReOpenRodLevelClaimView;
import com.shaic.paclaim.manageclaim.healthreopenclaim.searchClaimLevel.PAHealthSearchReOpenClaimTableDTO;
import com.shaic.paclaim.manageclaim.healthreopenclaim.searchClaimLevel.PAHealthSearchReOpenClaimView;
import com.shaic.paclaim.manageclaim.healthreopenclaim.searchRodLevel.PAHealthSearchReOpenClaimRodLevelTableDTO;
import com.shaic.paclaim.manageclaim.healthreopenclaim.searchRodLevel.PAHealthSearchReOpenClaimRodLevelView;
import com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel.PAReopenClaimPageDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel.PAReopenClaimTableDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel.PAReopenClaimView;
import com.shaic.paclaim.manageclaim.reopenclaim.pageRodLevel.PAReOpenRodLevelClaimView;
import com.shaic.paclaim.manageclaim.reopenclaim.searchClaimLevel.PASearchReOpenClaimTableDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.searchClaimLevel.PASearchReOpenClaimView;
import com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel.PASearchReOpenClaimRodLevelTableDTO;
import com.shaic.paclaim.manageclaim.reopenclaim.searchRodLevel.PASearchReOpenClaimRodLevelView;
import com.shaic.paclaim.medicalapproval.processclaimrequest.search.PASearchProcessClaimRequestView;
import com.shaic.paclaim.printbulkreminder.SearchPAPrintRemainderBulkViewImpl;
import com.shaic.paclaim.processRejectionPage.PAProcessRejectionWizard;
import com.shaic.paclaim.processrejection.search.PASearchProcessRejectionView;
import com.shaic.paclaim.registration.PAClaimRegistrationView;
import com.shaic.paclaim.registration.SearchPAClaimRegisterView;
import com.shaic.paclaim.registration.SearchPAClaimRegistrationPresenter;
import com.shaic.paclaim.reimbursement.bulkreminder.SearchGeneratePARemainderBulkView;
import com.shaic.paclaim.reimbursement.draftquery.DecideOnDraftPAQueryDetailWizardViewImpl;
import com.shaic.paclaim.reimbursement.draftquery.SearchDraftPAQueryLetterView;
import com.shaic.paclaim.reimbursement.draftrejection.DraftPARejectionLetterDetailView;
import com.shaic.paclaim.reimbursement.draftrejection.SearchDraftPARejectionLetterView;
import com.shaic.paclaim.reimbursement.pasearchuploaddocuments.PASearchUploadDocumentsService;
import com.shaic.paclaim.reimbursement.pasearchuploaddocuments.PASearchUploadDocumentsView;
import com.shaic.paclaim.reimbursement.pasearchuploaddocuments.PASearchUploadDocumentsWizardView;
import com.shaic.paclaim.reimbursement.processdraftquery.DecideOnProcessDraftPAQueryWizardViewImpl;
import com.shaic.paclaim.reimbursement.processdraftquery.SearchProcessDraftPAQueryView;
import com.shaic.paclaim.reimbursement.processdraftrejection.DecideOnPARejectionWizardViewImpl;
import com.shaic.paclaim.reimbursement.processdraftrejection.SearchProcessDraftPARejectionView;
import com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess.PAUploadDocumentsOutSideProcessWizardView;
import com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess.PAUploadDocumentsOutsideProcessView;
import com.shaic.paclaim.reimbursement.service.PAReimbursementService;
import com.shaic.paclaim.reminder.SearchGeneratePARemainderView;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAAcknowledgeDocumentWizardView;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PASearchAckDocumentReceiverView;
import com.shaic.paclaim.rod.createrod.search.PACreateRODWizardView;
import com.shaic.paclaim.rod.createrod.search.PASearchCreateRODView;
import com.shaic.paclaim.rod.enterbilldetails.search.PAEnterBillDetailsWizardView;
import com.shaic.paclaim.rod.enterbilldetails.search.PASearchEnterBillDetailsView;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.shaic.paclaim.settlementpullback.PASettlementPullBackView;
import com.shaic.paclaim.settlementpullback.dto.PASearchSettlementPullBackDTO;
import com.shaic.paclaim.settlementpullback.searchsettlementpullback.PASearchSettlementPullBackView;
import com.shaic.reimburement.addAdditinalDocument.search.SearchAddAdditionalDocumentTableDTO;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingTableDTO;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestFormDTO;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestTableDTO;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterService;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterTableDTO;
import com.shaic.reimbursement.queryrejection.processdraftquery.search.SearchProcessDraftQueryTableDTO;
import com.shaic.reimbursement.queryrejection.processdraftrejection.search.SearchProcessDraftRejectionTableDTO;
import com.shaic.reimbursement.reminderBulkSearch.BulkReminderResultDto;
import com.shaic.reimbursement.reminderBulkSearch.SearchGenerateRemainderBulkService;
import com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search.SearchAcknowledgementDocumentReceiverTableDTO;
import com.shaic.reimbursement.rod.cancelAcknowledgment.search.SearchCancelAcknowledgementView;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;
import com.shaic.reimbursement.rod.enterbilldetails.search.SearchEnterBillDetailTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;

@SuppressWarnings("serial")
@ViewInterface(MenuView.class)
public class PAMenuPresenter extends AbstractMVPPresenter<MenuView> {

	private final Logger log = LoggerFactory.getLogger(PAMenuPresenter.class);

	@EJB
	private MasterService masterService;

	@EJB
	private UsertoCPUMappingService usertoCPUMapService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ProcessPreMedicalService processPremedicalService;

	@Inject
	private DBCalculationService dbCalculationService;

	@EJB
	private PolicyService policyService;

	@EJB
	private ClaimService claimService;

	@Inject
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;

	@EJB
	private AcknowledgementDocumentsReceivedService acknowledgementDocumentsReceivedService;

	@Inject
	private CreateRODService createRodService;

	@EJB
	private IntimationService intimationService;

	@EJB
	private PEDQueryService pedQueryService;

	@EJB
	private CreateRODService rodService;

	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private HospitalService hospitalService;

	@EJB
	private ReimbursementRejectionService reimbursementRejectionService;

	@EJB
	private ReimbursementQueryService reimbursementQuerySerice;
	
	@EJB
	private SearchDraftQueryLetterService searchDraftQueryLetterService;

	@EJB
	private InvestigationService investigationService;

	@EJB
	private CreateRODService billDetailsService;

	@EJB
	private CoPayService copayservice;

	@EJB
	private PreviousPreAuthService previousPreAuthService;

	@EJB
	private CoordinatorService coordinatorService;
	
	 @EJB
	 private SearchGenerateRemainderBulkService bulkReminderSerachSevice;

	@Inject
	private ProcessClaimRequestBenefitsService claimRequestBenefitsService;

	public static final String PA_DOWNSIZE_PREAUTH_PAGE_VIEW = "PA Downsize Preauth Page";

	public static final String PA_DOWNSIZE_PREAUTH_REQUEST_PAGE_VIEW = "PA Downsize preauth request wizard page view";

	public static final String PA_WITHDRAW_PREAUTH_PAGE_VIEW = "PA Withdraw Preauth Page";

	public static final String PA_CREATE_ROD_WIZARD = "PA create rod wizard";

	public static final String PA_SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED = "PA_show_acknowledgement_document_received";

	public static final String PA_PROCESS_REJECTION = "PA Process Rejection Page";

	public static final String PA_SHOW_MEDICAL_APPROVAL_SCREEN = "pa_show_medical_approval_screen";
	// public static final String PA_SHOW_BILL_ENTRY = "PA_show_bill_entry";

	public static final String PA_SHOW_BILL_ENTRY = "PA_show_bill_entry";

	public static final String SHOW_PREATUH_WIZARD = "PA_preauth_wizard_view";

	public static final String SHOW_PREMEDICAL_WIZARD = "PA_premedical_wizard_view";

	public static final String SHOW_PREMEDICAL_ENHANCEMENT_WIZARD = "PA_premedical_enhancement_wizard_view";

	public static final String SHOW_PREAUTH_ENHANCEMENT_WIZARD = "PA_preauth_enhancement_wizard_view";
	
	public static final String SHOW_CONVERT_PA_CLAIM = "Show Convert PA Claim Page";

	public static final String SHOW_PA_HEALTH_MEDICAL_APPROVAL_PROCESS_CLAIM_REQUEST = "pa_health_medical_approval_process_claim_request";
	

	public static final String SHOW_PA_HEALTH_MEDICAL_APPROVAL_CLAIM_BILLING_SCREEN = "show_pa_health_medical_approval_claim_billing_screen";

	public static final String SHOW_PA_HEALTH_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN = "show_pa_health_financial_approval_claim_billing_screen";
	
	public static final String SHOW_PA_HOSP_COVER_MEDICAL_APPROVAL_CLAIM_BILLING_SCREEN = "show_pa_hosp_cover_medical_approval_claim_billing_screen";
	
	public static final String SHOW_PA_MEDICAL_APPROVAL_CLAIM_BILLING_SCREEN = "show_pa_medical_approval_claim_billing_screen";
	
	public static final String SHOW_PA_CLAIM_APPROVAL_SCREEN = "show_pa_claim_approval";
	
	public static final String SHOW_PA_NON_HOSP_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN = "show_pa_non_hosp_financial_approval_claim_billing_screen";
	
	public static final String REOPEN_CLAIM_PAGE_VIEW = "Reopen PA Claim revised page";
	
	public static final String REOPEN_PA_HEALTH_CLAIM_PAGE_VIEW = "Reopen PA Health Claim revised page";
	
	public static final String SHOW_SETTLEMENT_PULL_BACK = "show_pa_settlement_pull_back";
	
	public static final String SHOW_PA_HOSP_SETTLEMENT_PULL_BACK = "show_pa_hosp_settlement_pull_back";
	
	public static final String REOPEN_CLAIM_PAGE = "Re open pa claim page view";
	
	public static final String REOPEN_HEALTH_CLAIM_PAGE = "Re open pa health claim page view";
	
	public static final String CLOSE_CLAIM_SEARCH_BASED_ROD_LEVEL = "Close PA Claim Search based Rod Level";

	public static final String PA_ADD_ADDITIONAL_DOCUMENTS = "PA Add additional Documents";
	

	public static final String PA_SEARCH_OR_UPLOAD_DOCUMENTS_WIZARD = "PA_Search_or_upload_documents_wizard";
	
	public static final String SEARCH_OR_UPLOAD_DOCUMENTS_ACK_NOT_RECEIVED_WIZA = "PA_Search_or_upload_documents_ack_not_received_wizard";

	public static final String CLOSE_CLAIM_PAGE = "Close PA claim Revised page";
	
	public static final String CLOSE_CLAIM_IN_PROCESS = "Close PA Claim In Process";
	
	public static final String SHOW_CONVERT_REIMBURSEMENT = "PA Convert to reimbursement page";
	
	public static final String PA_UPDATE_ROD_WIZARD = "PA Convert to reimbursement page";

	public static final String PA_ADD_ADDITIONAL_PAYMENT_INFORMATION = "PA Add Additional  Payment Information";//R1069
	
	public static final String SHOW_FLP_BYPASS_PREAUTH_WIZARD = "flp_bypass_preauth_wizard_view";
	
	@EJB
	private PASearchProcessClaimBillingService paSearchProcessClaimBillingService;
	
	//@EJB
	//private PASearchProcessClaimAprovalNonHosService paSearchProcessClaimAprovalNonHosService;
	
	//@EJB
	//private PASearchProcessClaimAprovalNonHosService paSearchProcessClaimAprovalNonHosService
	@EJB
	private PAReimbursementService pareimbursementService;
	
	 @EJB
	 private PASearchUploadDocumentsService searchUploadDocService;
	 
	 @EJB
	 private UploadDocumentsForAckNotReceivedSubmitService uploadSubmitServiceforAckNotReceived;
	 
	 @EJB
		private PAReimbursementService paReimbursementService;

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}

	protected void showPAProcessClaimApprovalNonHosp(
			@Observes @CDIEvent(MenuItemBean.PA_CLAIM_APPROVAL_NON_HOS) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
		BeanItemContainer<SelectValue> productNameCode = masterService
				.getProductCodeName();

		BeanItemContainer<SelectValue> selectValueContainerForType = masterService
				.getStageList();

		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
				.getSelectValueForPriority();

		// BeanItemContainer<SelectValue> statusByStage =
		// masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);

		Stage stageByKey = preauthService
				.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);

		Stage stageByKey2 = preauthService
				.getStageByKey(ReferenceTable.BILLING_STAGE);

		SelectValue selectValue = new SelectValue();
		selectValue.setId(stageByKey.getKey());
		selectValue.setValue(stageByKey.getStageName());

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());

		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		statusByStage.addBean(selectValue);
		statusByStage.addBean(selectValue1);

		BeanItemContainer<SelectValue> selectValueContainerForBenefits = masterService.getCoversAndBenefitsContainer(SHAConstants.PA_BENFITS);
		/** 
		 * The below code have to implement later because there is no option to implement currently
		 */
	/*	
		Policy policyDetails = policyService.getPolicyByPolicyNubember(tableDTO.getPolicyNo());	
		if(null != policyDetails)
		{
			List<MasPaClaimCovers> coversList = masterService.getCoversListByProductKey(policyDetails.getProduct().getKey());
			if(null != coversList && !coversList.isEmpty())
			{
				for (MasPaClaimCovers masPaClaimCovers : coversList) {
					SelectValue selectValue = new SelectValue(masPaClaimCovers.getCoverKey(),masPaClaimCovers.getCoverDescription());
					selectValueContainerForBenefits.addBean(selectValue);
				}
			}
		}*/
		
		view.setViewsPAProcessClaimAprNonHos(
				PASearchProcessClaimAprovalNonHosView.class, true,
				selectValueContainerForCPUCode, productNameCode,
				selectValueContainerForType, selectValueForPriority,
				statusByStage,selectValueContainerForBenefits);
	}

	protected void showPAProcessClaimApprovalHosp(
			@Observes @CDIEvent(MenuItemBean.PA_CLAIM_APPROVAL_HOS) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
		BeanItemContainer<SelectValue> productNameCode = masterService
				.getProductCodeName();

		BeanItemContainer<SelectValue> selectValueContainerForType = masterService
				.getStageList();

		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
				.getSelectValueForPriority();

		// BeanItemContainer<SelectValue> statusByStage =
		// masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);

		Stage stageByKey = preauthService
				.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);

		Stage stageByKey2 = preauthService
				.getStageByKey(ReferenceTable.BILLING_STAGE);

		SelectValue selectValue = new SelectValue();
		selectValue.setId(stageByKey.getKey());
		selectValue.setValue(stageByKey.getStageName());

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());

		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		statusByStage.addBean(selectValue);
		statusByStage.addBean(selectValue1);

		view.setViewsPAProcessClaimAprHos(
				PASearchProcessClaimAprovalHosView.class, true,
				selectValueContainerForCPUCode, productNameCode,
				selectValueContainerForType, selectValueForPriority,
				statusByStage);
	}

	protected void showPAClaimRegisterView(
			@Observes @CDIEvent(MenuItemBean.SEARCH_PA_CLAIM_REGISTER) final ParameterDTO parameters) {

		BeanItemContainer<SelectValue> hospitalTypeContainer = masterService
				.getSelectValueContainer(ReferenceTable.HOSPITAL_TYPE);
		Map<String, Object> referenceData = new HashMap<String, Object>();
		referenceData.put("hospitalType", hospitalTypeContainer);

		BeanItemContainer<SelectValue> cpuCodeContainer = masterService
				.getTmpCpuCodes();
		referenceData.put("cpuCodeContainer", cpuCodeContainer);

		BeanItemContainer<SelectValue> incedentTypeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue accedentSelect = new SelectValue(null,
				SHAConstants.ACCIDENT);
		SelectValue deathSelect = new SelectValue(null, SHAConstants.DEATH);
		incedentTypeContainer.addBean(accedentSelect);
		incedentTypeContainer.addBean(deathSelect);
		referenceData.put("incedentTypeContainer", incedentTypeContainer);

		view.setPAClaimRegisterView(SearchPAClaimRegisterView.class, true,
				referenceData);
	}

	protected void showCreatePaRegistration(
			@Observes @CDIEvent(SearchPAClaimRegistrationPresenter.CREATE_PA_CLAIM_REGISTRATION) final ParameterDTO parameters) {
		SearchClaimRegistrationTableDto searchClaimRegistrationTableDto = (SearchClaimRegistrationTableDto) parameters
				.getPrimaryParameter();
		NewIntimationDto newIntimationDto = searchClaimRegistrationTableDto
				.getNewIntimationDto();
		// RRCDTO rrcDTO = new RRCDTO();
		// rrcDTO.setClaimDto(claimDTO);
		// rrcDTO.setNewIntimationDTO(newIntimationDto);
		// rrcDTO.setStrUserName(searchClaimRegistrationTableDto.getUsername());
		// rrcDTO.setHumanTask(searchClaimRegistrationTableDto.getHumanTask());
		// Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
		// rrcDTO.getNewIntimationDTO().getInsuredPatient()
		// .getInsuredId().toString(),
		// rrcDTO.getNewIntimationDTO().getPolicy().getKey());
		// loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured,
		// SHAConstants.PROCESS_CLAIM_REGISTRATION);
		// searchClaimRegistrationTableDto.setRrcDTO(rrcDTO);

		//TODO Need to get PA intimation BSI , new Procedure from DB Team is to be incorporated.
		Double insuredSumInsured = 0d;
		if(!ReferenceTable.getGPAProducts().containsKey(newIntimationDto.getPolicy().getProduct().getKey())){		
		
			insuredSumInsured = dbCalculationService
				.getInsuredSumInsured(newIntimationDto
						.getInsuredPatient().getInsuredId().toString(),
						newIntimationDto.getPolicy()
								.getKey(),newIntimationDto
								.getInsuredPatient().getLopFlag());
		}
		else
		{

			insuredSumInsured = dbCalculationService
				.getGPAInsuredSumInsured(newIntimationDto
						.getInsuredPatient().getInsuredId().toString(),
						newIntimationDto.getPolicy()
								.getKey());
			
			newIntimationDto.setPaSumInsured(insuredSumInsured);
			
		}
		
		Double balanceSI = 0d;
		
		if(!ReferenceTable.getGPAProducts().containsKey(newIntimationDto.getPolicy().getProduct().getKey())){			
				
			balanceSI = dbCalculationService.getBalanceSI(
				newIntimationDto.getPolicy().getKey(),
				newIntimationDto.getInsuredPatient().getKey(), 0l,
				newIntimationDto.getInsuredPatient().getInsuredSumInsured(),
				newIntimationDto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
		}
		else
		{
			 balanceSI = dbCalculationService.getGPABalanceSI(
					newIntimationDto.getPolicy().getKey(),
					newIntimationDto.getInsuredPatient().getKey(), 0l,
					insuredSumInsured,
					newIntimationDto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
		}
		Map<String, String> popupMessages = new HashMap<String, String>();
		// if(balanceSI != null && !(balanceSI > 0)){
		// popupMessages.put(SHAConstants.BALANCE_SI,
		// "<b style = 'black'>Balance SI is Zero </b>");
		// }
		if (newIntimationDto.getAdmissionType() != null
				&& newIntimationDto.getAdmissionType().getId()
						.equals(ReferenceTable.LATE_INTIMATION_KEY)) {
			popupMessages.put(SHAConstants.LATE_INTIMATION,
					"<b style = 'black'>Late intimation </b>");
		}

		if (balanceSI != null && balanceSI > 0) {
			searchClaimRegistrationTableDto.getNewIntimationDto().setBalanceSI(
					balanceSI);
		} else {
			balanceSI = 0d;
			searchClaimRegistrationTableDto.getNewIntimationDto().setBalanceSI(
					0d);
		}

		Map<String, String> suspiciousPopupMessage = new HashMap<String, String>();
		if (newIntimationDto.getHospitalDto().getSuspiciousFlag() != null
				&& newIntimationDto.getHospitalDto().getSuspiciousFlag()
						.equalsIgnoreCase("Y")) {
			if (newIntimationDto.getHospitalDto().getSuspiciousRemarks() != null) {
				suspiciousPopupMessage = SHAUtils
						.getSuspiciousMap(newIntimationDto.getHospitalDto()
								.getSuspiciousRemarks());
			}
		}
		searchClaimRegistrationTableDto
				.setSuspiciousPopUp(suspiciousPopupMessage);

		if (newIntimationDto.getPolicy() != null
				&& newIntimationDto.getPolicy().getPolicyStatus() != null) {
			if (newIntimationDto.getPolicy().getPolicyStatus()
					.equalsIgnoreCase(SHAConstants.ENDORESED_POLICY)) {
				searchClaimRegistrationTableDto
						.setIsProceedFurther(PremiaService.getInstance()
								.getEndorsedPolicyStatus(
										newIntimationDto.getPolicy()
												.getPolicyNumber()));
			} else if (newIntimationDto.getPolicy().getPolicyStatus()
					.equalsIgnoreCase(SHAConstants.CANCELLED_POLICY)) {
				searchClaimRegistrationTableDto.setIsProceedFurther(false);
				searchClaimRegistrationTableDto.setIsCancelledPolicy(true);
			}
		}
		
		Intimation intimationDtls = intimationService.getIntimationByNo(searchClaimRegistrationTableDto.getIntimationNumber());
		if(intimationDtls != null && intimationDtls.getAllowApprovalFlag() != null && intimationDtls.getAllowApprovalFlag().equalsIgnoreCase("Y")){
			searchClaimRegistrationTableDto.setIsProceedFurther(true);
		}

		Policy policyByKey = policyService.getPolicyByKey(newIntimationDto
				.getPolicy().getKey());

		// MASClaimAdvancedProvision claimAdvProvision =
		// policyService.getClaimAdvProvision(Long.valueOf(policyByKey.getHomeOfficeCode()));
		// searchClaimRegistrationTableDto.setClaimedAmount(claimAdvProvision.getAvgAmt());

		if ((SHAConstants.DEATH_FLAG)
				.equalsIgnoreCase(searchClaimRegistrationTableDto
						.getNewIntimationDto().getIncidenceFlag())) {

			searchClaimRegistrationTableDto.setClaimedAmount(balanceSI);
			searchClaimRegistrationTableDto.setPaAccProvAmt(balanceSI);
		} else {
			BPMClientContext obj = new BPMClientContext();
			if(null != obj.getDefaultPAProvisionAmt())
			{
			if (balanceSI > Double.valueOf(obj.getDefaultPAProvisionAmt())) {
				searchClaimRegistrationTableDto.setPaAccProvAmt(Double.valueOf(obj.getDefaultPAProvisionAmt()));
				searchClaimRegistrationTableDto.setClaimedAmount(Double.valueOf(obj.PA_ACC_PROV_AMT));
			} 
			}else {
				searchClaimRegistrationTableDto.setClaimedAmount(balanceSI);
				searchClaimRegistrationTableDto.setPaAccProvAmt(balanceSI);
			}
		}

		popupMessages.putAll(dbCalculationService.getPOPUPMessages(
				newIntimationDto.getPolicy().getKey(), newIntimationDto
						.getInsuredPatient().getKey(),newIntimationDto.getPolicy().getProduct().getKey()));
		searchClaimRegistrationTableDto.setPopupMap(popupMessages);
		
		BeanItemContainer<SelectValue> selectValueForCategory = dbCalculationService.getGPACategory(newIntimationDto.getPolicy().getKey());

		view.setPAClaimRegistrationForm(PAClaimRegistrationView.class,
				searchClaimRegistrationTableDto, selectValueForCategory);
	}

	protected void showSearchPACoveringLetterView(
			@Observes @CDIEvent(MenuItemBean.SEARCH_GENERATE_PA_COVERING_LETTER) final ParameterDTO parameters) {

		view.setViewG(SearchGenerateCoveringLetterPAView.class, true);
	}

	protected void showPAAcknowledgeDocumentReceivedWizard(
			@Observes @CDIEvent(PAMenuPresenter.PA_SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED) final ParameterDTO parameters) {

		SearchAcknowledgementDocumentReceiverTableDTO tableDTO = (SearchAcknowledgementDocumentReceiverTableDTO) parameters
				.getPrimaryParameter();
		Boolean isValidClaimForAck = true;
		ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
		Long claimKey = tableDTO.getClaimKey();
		Claim claimByKey = claimService.getClaimByKey(claimKey);	
		Boolean isValidClaim = true;
		if (null != claimByKey) {
			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaim = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
		}
		
		if(isValidClaim) {
		
		if (null != claimByKey) {
			
			Preauth preauth = preauthService
					.getLatestPreauthByClaimForAck(claimByKey.getKey());
			
			if (null != claimByKey
					&& null != claimByKey.getStatus()
					&& null != claimByKey.getStatus().getKey()
					&& claimByKey.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_CLOSED_STATUS)) {
				isValidClaimForAck = false;
				view.showErrorPopUp("Selected claim has been closed.Cannot proceed further.");
			}
			if (null != claimByKey.getStage()&& ((claimByKey.getStage().getKey().equals(ReferenceTable.CLAIM_REJECTION_STAGE)&& 
					null != claimByKey.getStatus()	&& claimByKey.getStatus().getKey().equals(ReferenceTable.PROCESS_REJECTED)) ||
					(claimByKey.getStage().getKey().equals(ReferenceTable.CLAIM_REGISTRATION_STAGE)&& 
							null != claimByKey.getStatus()	&& claimByKey.getStatus().getKey().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)))){

				List<Preauth> preauthByClaimKey = preauthService
						.getPreauthByClaimKey(claimKey);

				if (preauthByClaimKey != null && !preauthByClaimKey.isEmpty()) {

					// no action.
					//As per requirement the below validation is added for rejected claims from FLP.(ticket-GALAXYMAIN-6699)
					
					isValidClaimForAck = false;
					view.showErrorPopUp("Selected claim has been rejected.Cannot proceed further.");


				} else {
					isValidClaimForAck = false;
					view.showErrorPopUp("Selected claim has been rejected.Cannot proceed further.");
				}

			}	
			/*if(null != preauth)
			{
			if ((null != preauth.getStage()
					&& preauth.getStage().getKey()
							.equals(ReferenceTable.PROCESS_REJECTION_STAGE)
					&& null != preauth.getStatus()
					&& preauth.getStatus().getKey()
							.equals(ReferenceTable.PROCESS_REJECTED)) &&
					(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimByKey
									.getClaimType().getKey())) {
			
				isValidClaimForAck = false;
				view.showErrorPopUp("Selected claim has been rejected.Cannot proceed further.");
				}
			}*/

			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaimForAck = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
			if (isValidClaimForAck) {
				if ((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimByKey
						.getClaimType().getKey())) {					

					String diagnosisForPreauthByKey = "";
					if (preauth != null) {
						diagnosisForPreauthByKey = preauthService
								.getDiagnosisForPreauthByKey(preauth.getKey());

					}
					rodDTO.setDiagnosis(diagnosisForPreauthByKey);
					if (null != preauth) {
						Map<Long, Long> validationMap = ReferenceTable
								.getValidationMapForPAAck();
						Long key = preauth.getStatus().getKey();
						Long value = validationMap.get(key);

						if (null != value) {
							isValidClaimForAck = false;

							if (ReferenceTable.getWithdrawnKeys().containsKey(
									key)) {
								if (null != preauth.getWithdrawReason()) {
									Long withDrawKey = preauth
											.getWithdrawReason().getKey();
									if (withDrawKey
											.equals(ReferenceTable.PATIENT_NOT_ADMITTED_KEY)) {
										/*
										 * The below if condition is added to
										 * check whether the task is pending for
										 * conversion. If an preauth is
										 * withdrawn with patient not admitted
										 * reason, then that record would be
										 * available in convert claim queue
										 * after creating acknowledgement for
										 * first time for this cashless claim.
										 * Again without converting the claim if
										 * the user tries to create another
										 * acknowledgement, then the below
										 * validation will restrict the user
										 * from acknowledgement creation.
										 */

										if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE)))
										{
											isValidClaimForAck = false;
											view.showErrorPopUp("Claim is pending for conversion. Please convert claim first before creating acknowledgement.");
										} else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.FLP_SUGGEST_REJECTION_CURRENT_QUEUE)))
										{
											isValidClaimForAck = false;
											view.showErrorPopUp("Claim is been rejected and pending for approval. Please approve or reject the claim before creating acknowledgement");
										}
										else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.PP_CURRENT_QUEUE))){
											isValidClaimForAck = false;
											view.showErrorPopUp("Claim is pending in Preauth Queue. Please process the claim in that Queue");
										}
										else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.PE_CURRENT_QUEUE))){
											isValidClaimForAck = false;
											view.showErrorPopUp("Claim is pending in Preauth Enhancement Queue . Please process the claim in that Queue");
										}
										else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.FLE_CURRENT_QUEUE))){
											isValidClaimForAck = false;
											view.showErrorPopUp("Claim is pending in First level enhancement Queue. Please process the claim in that Queue");
										}
										else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.QUERY_REPLY_QUEUE))){
											isValidClaimForAck = false;
											view.showErrorPopUp("Claim is pending in Query Reply queue. Please process the claim in that Queue");
										}
										else {
											// isValidClaimForAck = true;
											// rodDTO.setIsConversionAllowed(true);
											isValidClaimForAck = false;
											view.showErrorPopUp("claim can not proceed further, since withdraw reason is that patient not admitted");
										}
									} else {
										view.showErrorPopUp("Claim is pending for conversion. Please convert claim first before creating acknowledgement.");
									}
								}

								
							}
							/*
							 * else
							 * if(ackDocReceivedService.getConvertClaimTask(
							 * claimByKey)) { view.showErrorPopUp(
							 * "Claim is pending for conversion. Please convert claim first before creating acknowledgement."
							 * ); }
							 */						
							else {
								view.showErrorPopUp("Preauth is pending for action. Please act on preauth before creating acknowledgement.");
							}
							
							
						}  else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.FLP_SUGGEST_REJECTION_CURRENT_QUEUE)))
						{
							isValidClaimForAck = false;
							view.showErrorPopUp("Claim is been rejected and pending for approval. Please approve or reject the claim before creating acknowledgement");
						}
						else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.PP_CURRENT_QUEUE))){
							isValidClaimForAck = false;
							view.showErrorPopUp("Claim is pending in Preauth Queue. Please process the claim in that Queue");
						}
						else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.PE_CURRENT_QUEUE))){
							isValidClaimForAck = false;
							view.showErrorPopUp("Claim is pending in Preauth Enhancement Queue . Please process the claim in that Queue");
						}
						else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.FLE_CURRENT_QUEUE))){
							isValidClaimForAck = false;
							view.showErrorPopUp("Claim is pending in First level enhancement Queue. Please process the claim in that Queue");
						}
						else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.QUERY_REPLY_QUEUE))){
							isValidClaimForAck = false;
							view.showErrorPopUp("Claim is pending in Query Reply queue. Please process the claim in that Queue");
						}else {
							rodDTO.setIsConversionAllowed(ackDocReceivedService
									.isConversionAllowed(preauth));

						}
					}
					/**
					 * This block will be invoked only for rejected claim. Since
					 * while rejecting a claim, no status updation will happen
					 * on claim table. Hence fetching based on claim status will
					 * not help us out. Hence, to validate suggest rejection
					 * case, directly bpm rejection task list is fetched , based
					 * on which we conclude that task is present in Process
					 * Rejection Queue.
					 * */
					// else if(null != claimByKey && null !=
					// claimByKey.getStatus() && null !=
					// claimByKey.getStatus().getKey() &&
					// claimByKey.getStatus().getKey().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS))
					else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.FLP_SUGGEST_REJECTION_CURRENT_QUEUE)))
					{
						isValidClaimForAck = false;
						view.showErrorPopUp("Claim is been rejected and pending for approval. Please approve or reject the claim before creating acknowledgement");
					}
					else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.PP_CURRENT_QUEUE))){
						isValidClaimForAck = false;
						view.showErrorPopUp("Claim is pending in Preauth Queue. Please process the claim in that Queue");
					}
					else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.FLE_CURRENT_QUEUE))){
						isValidClaimForAck = false;
						view.showErrorPopUp("Claim is pending in First level enhancement Queue. Please process the claim in that Queue");
					}
					else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.PE_CURRENT_QUEUE))){
						isValidClaimForAck = false;
						view.showErrorPopUp("Claim is pending in Preauth Enhancement Queue . Please process the claim in that Queue");
					}
					else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.QUERY_REPLY_QUEUE))){
						isValidClaimForAck = false;
						view.showErrorPopUp("Claim is pending in Query Reply queue. Please process the claim in that Queue");
					}

					else if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE)))
					{
						isValidClaimForAck = false;
						view.showErrorPopUp("Claim is pending for conversion. Please convert claim first before creating acknowledgement.");
					}
					/*
					 * else if() { isValidClaimForAck = false;
					 * view.showErrorPopUp(
					 * "Claim is pending for conversion. Please convert claim first before creating acknowledgement."
					 * ); }
					 */
					else {
						/**
						 * This block is applicable only if claim is registered
						 * and pre auth is null
						 **/
						/*
						 * Boolean value =
						 * acknowledgementDocumentsReceivedService
						 * .getWaitingForPreauthTask(claimByKey); if(value) {
						 * rodDTO.setIsConversionAllowed(value); }
						 */
						rodDTO.setIsConversionAllowed(ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.WAITING_FOR_PREATH_QUEUE));

						if(null != rodDTO.getIsConversionAllowed() && !rodDTO.getIsConversionAllowed()){
							isValidClaimForAck = false;
							view.showErrorPopUp("Preauth is pending for Action");
						}
						/*
						 * else { Map<Long,Long> valMap =
						 * ReferenceTable.getConversionStatusMap(); Long val =
						 * valMap.get(claimByKey.getStatus().getKey()); if(null
						 * != val) rodDTO.setIsConversionAllowed(true); }
						 */
					}

				}
			}
			
			if((ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE)))
			{
				isValidClaimForAck = false;
				view.showErrorPopUp("Claim is pending for conversion. Please convert claim first before creating acknowledgement.");
			}
			
			if (isValidClaimForAck) {
				Long count = getAckNoCountByClaimKey(claimKey,
						ReferenceTable.ACKNOWLEDGE_DOC_RECEIVED);
				rodDTO.setAcknowledgementNumber(count);
				rodDTO.setAcknowledgementNumber(count);
				ClaimDto claimDTO = null;
				NewIntimationDto newIntimationDto = new NewIntimationDto();
				if (claimByKey != null) {
					newIntimationDto = intimationService
							.getIntimationDto(claimByKey.getIntimation());
					claimDTO = ClaimMapper.getInstance()
							.getClaimDto(claimByKey);
					if (claimDTO.getClaimType() != null
							&& claimDTO
									.getClaimType()
									.getId()
									.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
						Preauth latestPreauthByClaim = preauthService
								.getLatestPreauthByClaim(claimDTO.getKey());
						if (latestPreauthByClaim != null) {
							claimDTO.setCashlessAppAmt(latestPreauthByClaim
									.getTotalApprovalAmount());
						}
						String diagnosisForPreauthByKey = "";
						if (latestPreauthByClaim != null) {
							diagnosisForPreauthByKey = preauthService
									.getDiagnosisForPreauthByKey(latestPreauthByClaim
											.getKey());

						}
						rodDTO.setDiagnosis(diagnosisForPreauthByKey);
					}
					claimDTO.setNewIntimationDto(newIntimationDto);
					rodDTO.setClaimDTO(claimDTO);
				}
				generateAcknowledgeNo(rodDTO);

				Map<String, Integer> productBenefitMap = dbCalculationService
						.getProductBenefitFlag(claimByKey.getKey(), claimByKey
								.getIntimation().getPolicy().getProduct()
								.getKey());
				/**
				 * Earlier , pre and post hospitalization was enabled only based
				 * on product conditions in ack , rod level and bill entry
				 * level. But as per mockup 13.7 , this validation needs to be
				 * removed at ack and rod level and should be available only at
				 * FA level. Hence hardcoding the values of pre and post in map
				 * , by which we always enable pre and post for all products at
				 * ACK and ROD level and bil entry level.
				 * */
				productBenefitMap.put("preHospitalizationFlag", 1);
				productBenefitMap.put("postHospitalizationFlag", 1);
				
				List<DocumentCheckListDTO> setPADocumentCheckListTableValues = setPADocumentCheckListTableValues();
				
				List<DocumentCheckListDTO> documentCheckList = new ArrayList<DocumentCheckListDTO>();
				int i=0;
				for (DocumentCheckListDTO documentCheckListDTO : setPADocumentCheckListTableValues) {
					documentCheckList.add(documentCheckListDTO);
					i++;
					if(i == 5){
						break;
					}
				}
	            
				rodDTO.setProductBenefitMap(productBenefitMap);
				 rodDTO.getDocumentDetails().setDocumentCheckList(
						 documentCheckList);
				 rodDTO.getDocumentDetails().setDefaultDocumentCheckList(documentCheckList);
/*
				List<DocumentCheckListDTO> documentCheckListDTO = setPADocumentCheckListTableValues();

				List<DocumentCheckListDTO> revisedDocumentCheckListDTO = new ArrayList<DocumentCheckListDTO>();

				if (null != documentCheckListDTO
						&& !documentCheckListDTO.isEmpty()) {
					for (int i = 0; i < 6; i++) {

						revisedDocumentCheckListDTO.add(documentCheckListDTO
								.get(i));
					}
				}

				rodDTO.getDocumentDetails().setDocumentCheckList(
						revisedDocumentCheckListDTO);*/
				Long rodKey = null;
				rodDTO.setReconsiderRodRequestList(getReconsiderRODRequest(claimByKey));
				if(rodDTO.getReconsiderRodRequestList() != null && !rodDTO.getReconsiderRodRequestList().isEmpty()) {
					
					rodKey = rodDTO.getReconsiderRodRequestList().get(0).getRodKey();
				}
				rodDTO.setRodQueryDetailsList(getRODQueryDetailsValues(claimByKey));
				if(rodDTO.getRodQueryDetailsList() != null && !rodDTO.getRodQueryDetailsList().isEmpty()) {
					rodKey = rodDTO.getRodQueryDetailsList().get(0).getReimbursementKey();
				}
				
				if(rodKey != null){
					Reimbursement reimb = reimbursementService.getReimbursementByKey(rodKey);
					rodDTO.getClaimDTO().getNewIntimationDto().setNomineeList(intimationService.getNomineeDetailsListByTransactionKey(rodKey));
					rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().setNomineeDeceasedFlag(reimb.getNomineeFlag());
						if(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList() == null || rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()
								|| (reimb.getNomineeFlag() != null && reimb.getNomineeFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))) {
							List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(rodKey);
							if(legalHeirList != null && !legalHeirList.isEmpty()) {
								List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
								LegalHeirDTO legalHeirDto;
								for (LegalHeir legalHeir : legalHeirList) {
									 legalHeirDto = new LegalHeirDTO(legalHeir);
									 legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
									 legalHeirDTOList.add(legalHeirDto);
								}
								rodDTO.getPreauthDTO().setLegalHeirDTOList(legalHeirDTOList);
							}
						}	
				}	
				//rodDTO.setPaymentQueryDetailsList(getPARODPaymentQueryDetailsValues(claimByKey));

				rodDTO.setStrUserName(tableDTO.getUsername());
				rodDTO.setStrPassword(tableDTO.getPassword());

				/*rodDTO.getDocumentDetails().setAccidentOrDeath(
						claimDTO.getIncidenceFlag());

				rodDTO.getDocumentDetails().setAccidentOrDeathDate(
						claimDTO.getIncidenceDate());*/
				
				StarCommonUtils.getAccidentAndDeathDate(claimByKey, rodDTO);			
								

				Long productCode = rodDTO.getClaimDTO().getNewIntimationDto()
						.getPolicy().getProduct().getKey();
				Long insuredKey = rodDTO.getClaimDTO().getNewIntimationDto()
						.getInsuredPatient().getKey();

				
				if (null != productCode && null != insuredKey) {
					rodDTO.getDocumentDetails().setAdditionalCovers(
							dbCalculationService.getBebefitAdditionalCovers(
									SHAConstants.ADDITIONAL_COVER, productCode,
									insuredKey));

					rodDTO.getDocumentDetails().setOptionalCovers(
							dbCalculationService.getBebefitAdditionalCovers(
									SHAConstants.OPTIONAL_COVER, productCode,
									insuredKey));
				}
				
				if(ReferenceTable.getGPAProducts().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
					
					if (null != productCode && null != insuredKey) {
						rodDTO.getDocumentDetails().setAdditionalCovers(
								dbCalculationService.getBebefitAdditionalCovers(
										SHAConstants.GPA_ADDITIONAL_COVER, productCode,
										insuredKey));
					}
					
					if (null != productCode && null != insuredKey) {
						rodDTO.getDocumentDetails().setOptionalCovers(
								dbCalculationService.getBebefitAdditionalCovers(
										SHAConstants.GPA_OPTIONAL_COVER, productCode,
										insuredKey));
					}
				}

				RRCDTO rrcDTO = new RRCDTO();
				rrcDTO.setClaimDto(claimDTO);
				rrcDTO.setNewIntimationDTO(newIntimationDto);
				rrcDTO.setStrUserName(tableDTO.getUsername());
				
				Double insuredSumInsured = 0d;

				if(!ReferenceTable.getGPAProducts().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				
				 insuredSumInsured = dbCalculationService
						.getInsuredSumInsured(rrcDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								rrcDTO.getNewIntimationDTO().getPolicy()
										.getKey(),rrcDTO.getNewIntimationDTO()
										.getInsuredPatient().getLopFlag());
				}
				else
				{
					 insuredSumInsured = dbCalculationService
								.getGPAInsuredSumInsured(rrcDTO.getNewIntimationDTO()
										.getInsuredPatient().getInsuredId().toString(),
										rrcDTO.getNewIntimationDTO().getPolicy()
												.getKey());
				}
				loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured,
						SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
				rodDTO.setRrcDTO(rrcDTO);
				if (null != rrcDTO.getNewIntimationDTO()
						&& null != rrcDTO.getNewIntimationDTO().getPolicy()) {
					rodDTO.setIsPEDInitiated(pedQueryService
							.isPEDInitiated(rrcDTO.getNewIntimationDTO()
									.getPolicy().getKey()));
					rodDTO.setIsPEDInitiatedbtn(rodDTO.getIsPEDInitiated());
				}

				if (rrcDTO.getNewIntimationDTO() != null
						&& rrcDTO.getNewIntimationDTO().getInsuredPatient() != null
						&& rrcDTO.getNewIntimationDTO().getInsuredPatient()
								.getKey() != null) {
					Long claimCount = preauthService.getClaimCount(rrcDTO
							.getNewIntimationDTO().getPolicy().getKey());
					if (claimCount != null) {
						rodDTO.setClaimCount(claimCount);
					}
				}
				
				if(null != preauth && null != preauth.getWorkPlace() && (SHAConstants.YES_FLAG.equalsIgnoreCase(preauth.getWorkPlace()))){
					
					rodDTO.getDocumentDetails().setWorkOrNonWorkPlace(true);
				}
				
				LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
				legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
				rodDTO.getPreauthDTO().setLegalHeirDto(legalHeirDTO);
				
				view.setPAAcknowledgeDocReceivedWizardView(
						PAAcknowledgeDocumentWizardView.class, rodDTO);
			}
		}
	}
	}

	private Long getAckNoCountByClaimKey(Long claimKey, String presenterString) {
		Long count = 0l;
		if ((presenterString)
				.equalsIgnoreCase(ReferenceTable.ACKNOWLEDGE_DOC_RECEIVED)) {
			count = ackDocReceivedService.getCountOfAckByClaimKey(claimKey);
		} else if ((presenterString)
				.equalsIgnoreCase(ReferenceTable.CREATE_ROD)) {
			count = createRodService
					.getACknowledgeNumberCountByClaimKey(claimKey);
		}

		return count;
	}

	private void generateAcknowledgeNo(ReceiptOfDocumentsDTO rodDTO) {
		Long claimKey = rodDTO.getClaimDTO().getKey();
		Long count = ackDocReceivedService.getCountOfAckByClaimKey(claimKey);
		StringBuffer ackNoBuf = new StringBuffer();
		Long lackCount = count + 001;
		ackNoBuf.append("ACK/")
				.append(rodDTO.getClaimDTO().getNewIntimationDto()
						.getIntimationId()).append("/").append(lackCount);
		rodDTO.getDocumentDetails().setAcknowledgementNumber(
				ackNoBuf.toString());
	}

	private List<DocumentCheckListDTO> setPADocumentCheckListTableValues() {

		// return
		// ackDocReceivedService.getDocumentCheckListValues(masterService);
		return ackDocReceivedService
				.getPADocumentCheckListValues(masterService);
	}

	private List<ReconsiderRODRequestTableDTO> getReconsiderRODRequest(
			Claim claim) {
		List<ReconsiderRODRequestTableDTO> reconsiderRODList = ackDocReceivedService
				.getReconsiderRequestTableValues(claim);
		return reconsiderRODList;
	}

	private List<ReconsiderRODRequestTableDTO> getReconsiderRODRequestForRodAndBillEntry(
			Claim claim) {
		List<ReconsiderRODRequestTableDTO> reconsiderRODList = ackDocReceivedService
				.getReconsiderRequestTableValuesForCreateRodAndBillEntry(claim);
		return reconsiderRODList;
	}

	private List<RODQueryDetailsDTO> getRODQueryDetailsValues(Claim claim) {
		List<RODQueryDetailsDTO> rodQueryDetails = ackDocReceivedService
				.getRODQueryDetails(claim);
		return rodQueryDetails;
	}
	private List<RODQueryDetailsDTO> getPARODQueryDetailsValues(Claim claim) {
		List<RODQueryDetailsDTO> rodQueryDetails = ackDocReceivedService
				.getPARODQueryDetails(claim);
		return rodQueryDetails;
	}

	private List<RODQueryDetailsDTO> getPARODPaymentQueryDetailsValues(Claim claim) {
		List<RODQueryDetailsDTO> rodQueryDetails = ackDocReceivedService
				.getPAPaymentRODQueryDetails(claim);
		return rodQueryDetails;
	}
	private void loadRRCRequestValuesForCashless(RRCDTO requestRRCDTO,
			Double sumInsured, String stage) {
		if (null != requestRRCDTO.getNewIntimationDTO()) {
			// load policy data

			String policyNumber = requestRRCDTO.getNewIntimationDTO()
					.getPolicy().getPolicyNumber();
			requestRRCDTO.setPolicyNo(policyNumber);
			requestRRCDTO.setIntimationNo(requestRRCDTO.getNewIntimationDTO()
					.getIntimationId());
			requestRRCDTO.setProductName(requestRRCDTO.getNewIntimationDTO()
					.getProductName());
			// Duration on hold
			Date admissionDate = requestRRCDTO.getNewIntimationDTO()
					.getAdmissionDate();
			// String duration =
			// dbCalculationService.getPolicyAgeing(admissionDate,
			// policyNumber);
			String duration = requestRRCDTO.getNewIntimationDTO()
					.getPolicyYear();
			requestRRCDTO.setDuration(duration);
			requestRRCDTO.setSumInsured(sumInsured);

			// load Hospital data
			requestRRCDTO.setHospitalName(requestRRCDTO.getNewIntimationDTO()
					.getHospitalDto().getName());
			requestRRCDTO.setHospitalCity(requestRRCDTO.getNewIntimationDTO()
					.getHospitalDto().getCity());
			// if(null !=
			// preauthDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals())
			requestRRCDTO.setHospitalZone(requestRRCDTO.getNewIntimationDTO()
					.getHospitalDto().getHospitalZone());
			requestRRCDTO.setDateOfAdmission(admissionDate);

			if (null != requestRRCDTO.getClaimDto()) {
				String claimType = requestRRCDTO.getClaimDto()
						.getClaimTypeValue();
				Long claimKey = requestRRCDTO.getClaimDto().getKey();
				if (SHAConstants.CLAIMREQUEST_CASHLESS
						.equalsIgnoreCase(claimType)) {
					Preauth preauth = preauthService
							.getLatestPreauthByClaim(claimKey);
					if (null != preauth) {
						requestRRCDTO.setDateOfDischarge(preauth
								.getDateOfDischarge());
					}
				} else if (SHAConstants.CLAIMREQUEST_REIMBURSEMENT
						.equalsIgnoreCase(claimType)) {
					Reimbursement reimbursement = rodService
							.getPreviousRODDetails(claimKey);
					if (null != reimbursement) {
						requestRRCDTO.setDateOfDischarge(reimbursement
								.getDateOfDischarge());
					}
				}
			}
			/*
			 * if(null != preauthDTO.getPreauthDataExtractionDetails() && null
			 * !=
			 * preauthDTO.getPreauthDataExtractionDetails().getDischargeDate())
			 * requestRRCDTO
			 * .setDateOfDischarge(preauthDTO.getPreauthDataExtractionDetails
			 * ().getDischargeDate());
			 */

			// load Insured data
			requestRRCDTO.setInsuredName(requestRRCDTO.getNewIntimationDTO()
					.getInsuredPatient().getInsuredName());
			if (null != requestRRCDTO.getNewIntimationDTO().getInsuredPatient()
					.getInsuredAge())
				requestRRCDTO.setInsuredAge(requestRRCDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredAge().longValue());
			if (null != requestRRCDTO.getNewIntimationDTO().getInsuredPatient()
					.getInsuredGender())
				requestRRCDTO.setSex(requestRRCDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredGender().getValue());

			if (null != requestRRCDTO.getClaimDto()) {
				requestRRCDTO.setClaimType(requestRRCDTO.getClaimDto()
						.getClaimTypeValue());
			}
			requestRRCDTO.setProcessingStage(stage);

			TmpEmployee tmpEmployee = reimbursementService
					.getEmployeeDetails(requestRRCDTO.getStrUserName());
			if (null != tmpEmployee) {
				String strName = "";
				if (null != tmpEmployee.getEmpFirstName()) {
					strName = tmpEmployee.getEmpFirstName();
				}
				if (null != tmpEmployee.getEmpMiddleName()) {
					if (("").equalsIgnoreCase(strName)) {
						strName = tmpEmployee.getEmpMiddleName();
					} else {
						strName += tmpEmployee.getEmpMiddleName();
					}
				}
				if (null != tmpEmployee.getEmpLastName()) {
					if (("").equalsIgnoreCase(strName)) {
						strName = tmpEmployee.getEmpLastName();
					} else {
						strName += tmpEmployee.getEmpLastName();
					}
				}
				/*
				 * if(!(null != tmpEmployee.getEmpFirstName()))
				 * tmpEmployee.setEmpFirstName(""); else if(!(null !=
				 * tmpEmployee.getEmpMiddleName()))
				 * tmpEmployee.setEmpMiddleName(""); else if(!(null !=
				 * tmpEmployee.getEmpLastName()))
				 * tmpEmployee.setEmpLastName("");
				 */

				// requestRRCDTO.setEmployeeName(tmpEmployee.getEmpFirstName()+tmpEmployee.getEmpMiddleName()+tmpEmployee.getEmpLastName());
				requestRRCDTO.setEmployeeName(strName);
				requestRRCDTO.setEmployeeId(tmpEmployee.getEmpId());
			}
			// requestRRCDTO.setEmployeeNameList(getEmployeeNamesFromMaster());
			requestRRCDTO.setDataSourcesMap(getEmployeeNamesFromMaster());
			// Map<String,Object> containerMap = new HashMap<String, Object>();

			// containerMap.put(SHAConstants.EMPLOYEE_NAME_ID_MAP ,
			// masterService.loadEmployeeNameData());
			setRequestStageIdForRRC(requestRRCDTO, stage);
			/*
			 * ExtraEmployeeEffortDTO extraEffortEmpDTO = new
			 * ExtraEmployeeEffortDTO(); //EmployeeMasterDTO empMasterDTO = new
			 * EmployeeMasterDTO();
			 * //empMasterDTO.setEmployeeName(requestRRCDTO.getEmployeeName());
			 * extraEffortEmpDTO.setEmployeeId(requestRRCDTO.getEmployeeId());
			 * //
			 * extraEffortEmpDTO.setEmployeeName(requestRRCDTO.getEmployeeName(
			 * )); //extraEffortEmpDTO.setEmployeeNameDTO(empMasterDTO);
			 * 
			 * List<ExtraEmployeeEffortDTO> extraEffortDTOList = new
			 * ArrayList<ExtraEmployeeEffortDTO>();
			 * extraEffortDTOList.add(extraEffortEmpDTO);
			 * //requestRRCDTO.setEmployeeEffortList(extraEffortDTOList);
			 * 
			 * //requestRRCDTO.setEmployeeDept();
			 */// preauthDTO.setRrcDTO(requestRRCDTO);

		}

	}

	private Map<String, Object> getEmployeeNamesFromMaster() {
		BeanItemContainer<SelectValue> employeeNameList = masterService
				.getEmployeeNameFromMaster();
		Map<String, Object> employeeNameMap = new HashMap<String, Object>();
		employeeNameMap.put(SHAConstants.EMPLOYEE_NAME_LIST, employeeNameList);
		employeeNameMap.put(SHAConstants.EMPLOYEE_NAME_ID_MAP,
				masterService.loadEmployeeNameData());
		employeeNameMap.put(SHAConstants.EMPLOYEE_ID, masterService.getEmployeeLoginNameContainer());
		return employeeNameMap;
	}

	private RRCDTO setRequestStageIdForRRC(RRCDTO requestRRCDTO, String stage) {
		if ((SHAConstants.PROCESS_PRE_MEDICAL).equals(stage)) {
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
		}
		if ((SHAConstants.PROCESS_PREAUTH).equals(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.PREAUTH_STAGE);
		} else if ((SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT)
				.equals(stage)) {
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
		} else if ((SHAConstants.PROCESS_ENHANCEMENT).equals(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.ENHANCEMENT_STAGE);
		} else if ((SHAConstants.PROCESS_WITHDRAW_PREAUTH).equals(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.WITHDRAW_STAGE);
		} else if ((SHAConstants.PROCESS_DOWNSIZE_PREAUTH).equals(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.DOWNSIZE_STAGE);
		} else if ((SHAConstants.PROCESS_DOWNSIZE_REQUEST_PREAUTH)
				.equals(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.DOWNSIZE_STAGE);
		}

		else if ((SHAConstants.PROCESS_PED_QUERY).equals(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.PED_QUERY);
		}

		else if ((SHAConstants.PROCESS_REJECTION).equals(stage)) {
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.PROCESS_REJECTION_STAGE);
		} else if ((SHAConstants.ACKNOWLEDGE_HOSPITAL_COMMUNICATION)
				.equals(stage)) {
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.ACKNOWLEDGE_HOSPITAL_STAGE);
		} else if ((SHAConstants.FIELD_VISIT).equals(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.FVR_STAGE_KEY);
		}

		else if ((SHAConstants.PED_REQUEST_PROCESS).equals(stage)) {
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.PED_ENDORSEMENT_STAGE);
		}

		else if ((SHAConstants.PED_REQUEST_APPROVER).equals(stage)) {
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.PED_ENDORSEMENT_STAGE);
		} else if ((SHAConstants.ADVISE_ON_PED).equals(stage)) {
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.PED_ENDORSEMENT_STAGE);
		} else if ((SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_CASHLESS)
				.equals(stage)) {
			/**
			 * To Confirm stage with developer . -- No separate stage available
			 * for submit specialist.
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.PED_ENDORSEMENT_STAGE);
		} else if ((SHAConstants.PROCESS_COORDINATOR_REPLY).equals(stage)) {
			/**
			 * To Confirm stage with developer .
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.COORDINATOR_PROCESS_REPLY_STAGE);
		}

		else if ((SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_REIMBURSEMENT)
				.equals(stage)) {
			/**
			 * To Confirm stage with developer .
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.SPECIALIST_PROCESS_STAGE);
			// requestRRCDTO.setRequestedStageId(ReferenceTable.COORDINATOR_PROCESS_REPLY_STAGE);
		} else if ((SHAConstants.PROCESS_COORDINATOR_REPLY_REIMBURSEMENT)
				.equals(stage)) {
			/**
			 * To Confirm stage with developer . -- no separate stage available
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.CLAIM_REQUEST_STAGE);
		} else if ((SHAConstants.PROCESS_INVESTIGATION_INTIATED).equals(stage)) {
			/**
			 * To Confirm stage with developer .
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		} else if ((SHAConstants.ASSIGN_INVESTIGATION_INTIATED).equals(stage)) {
			/**
			 * To Confirm stage with developer .
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
			// requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REQUEST_STAGE);
		} else if ((SHAConstants.ACKNOWLEDGE_INVESTIGATION).equals(stage)) {
			/**
			 * To Confirm stage with developer .
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
			// requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REQUEST_STAGE);

			// requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		} else if ((SHAConstants.ACKNOWLEDGE_DOC_RECEIVED).equals(stage)) {
			/**
			 * To Confirm stage with developer .
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			// requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		} else if ((SHAConstants.CREATE_ROD).equals(stage)) {
			/**
			 * To Confirm stage with developer .
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.CREATE_ROD_STAGE_KEY);
			// requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		} else if ((SHAConstants.BILL_ENTRY).equals(stage)) {
			/**
			 * To Confirm stage with developer .
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.BILL_ENTRY_STAGE_KEY);
			// requestRRCDTO.setRequestedStageId(ReferenceTable.INVESTIGATION_STAGE);
		}

		else if ((SHAConstants.ADD_ADDITIONAL_DOCUMENTS).equals(stage)) {
			/**
			 * The stage needs to revisited. Need to check sathish sir for this
			 * once.
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		}

		else if ((SHAConstants.PROCESS_UPLOAD_INVESTIGATION).equals(stage)) {
			/**
			 * The stage needs to revisited. Need to check sathish sir for this
			 * once.
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.UPLOAD_INVESTIGATION_STAGE);

			// requestRRCDTO.setRequestedStageId(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		} else if ((SHAConstants.PROCESS_CLAIM_REGISTRATION).equals(stage)) {
			/**
			 * The stage needs to revisited. Need to check sathish sir for this
			 * once.
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		}

		else if ((SHAConstants.PROCESS_CONVERT_CLAIM).equals(stage)) {
			/**
			 * The stage needs to be revisted . Need to check with sathish sir.
			 * 
			 * */
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		}

		else if ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(stage)) {
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.ZONAL_REVIEW_STAGE);
		} else if ((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(stage)) {
			requestRRCDTO
					.setRequestedStageId(ReferenceTable.CLAIM_REQUEST_STAGE);
		}

		else if ((SHAConstants.BILLING).equalsIgnoreCase(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.BILLING_STAGE);
		} else if ((SHAConstants.RRC_FINANCIAL).equalsIgnoreCase(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.FINANCIAL_STAGE);
		}

		else if ((SHAConstants.INITIATE_RRC_REQUEST).equalsIgnoreCase(stage)) {
			requestRRCDTO.setRequestedStageId(ReferenceTable.RRC_STAGE);
		}

		return requestRRCDTO;
	}

	protected void showPACreateROD(
			@Observes @CDIEvent(MenuItemBean.PA_CREATE_ROD) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
				.getSelectValueForPriority();
		// BeanItemContainer<SelectValue> statusByStage =
		// masterService.getStatusByStage(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);

		Status status = preauthService
				.getStatusByKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);

		SelectValue selectValue = new SelectValue();
		selectValue.setId(status.getKey());
		selectValue.setValue(status.getProcessValue());
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		statusByStage.addBean(selectValue);

		BeanItemContainer<SelectValue> selectValueForUploadedDocument = masterService
				.getSelectValueContainer(ReferenceTable.COMMON_VALUES);

		view.setPAViewCreateROD(PASearchCreateRODView.class, true,
				selectValueContainerForCPUCode, selectValueForPriority,
				statusByStage, selectValueForUploadedDocument);
	}

	protected void showPAAcknowledgementDocumentReceiver(
			@Observes @CDIEvent(MenuItemBean.PA_SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED) final ParameterDTO parameters) {

		// getTaskManuallyFromBPMNForProcessPreauth();
		// ReceivePreAuthTask receivePreauthTask =
		// BPMClientContext.getPreAuthReceived(BPMClientContext.BPMN_TASK_USER,
		// BPMClientContext.BPMN_PASSWORD);

		/*
		 * com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthEnhTask
		 * processPreMedicalEnhTask =
		 * BPMClientContext.getProcessPreMedicalEnhancementTask
		 * (BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		 * 
		 * //com.shaic.ims.bpm.claim.servicev2.preauth.search.PreMedicalPreAuthTask
		 * preMedicalClaimTask =
		 * BPMClientContext.getProcessPreMedicalTask(BPMClientContext
		 * .BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD); //ClaimRegROTask
		 * manualRegisterClaimTaskForRO =
		 * BPMClientContext.getManualRegisterClaimTaskForRO
		 * (BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
		 * 
		 * com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks =
		 * processPreMedicalEnhTask.getTasks(BPMClientContext.BPMN_TASK_USER,
		 * null, null);
		 * 
		 * List<HumanTask> humanTasks = tasks.getHumanTasks();
		 * System.out.println("PREENHANCEMENT TASK------------------------>" +
		 * humanTasks.size()); List<String> arr = new ArrayList<String>(); for
		 * (HumanTask humanTask : humanTasks) { PayloadBOType payloadCashless =
		 * humanTask.getPayloadCashless(); if(payloadCashless != null &&
		 * payloadCashless.getIntimation() != null &&
		 * payloadCashless.getIntimation().getIntimationNumber() != null) {
		 * arr.add(payloadCashless.getIntimation().getIntimationNumber());
		 * System
		 * .out.println(payloadCashless.getIntimation().getIntimationNumber());
		 * } }
		 * 
		 * 
		 * if (arr.size() > 0) { StringBuilder nameBuilder = new
		 * StringBuilder();
		 * 
		 * for (String str : arr) { String n = String.valueOf(str); //
		 * nameBuilder.append("'").append(n.replace("'", "\\'")).append("',");
		 * // can also do the following //
		 * nameBuilder.append("'").append(n.replace("'", "''")).append("',"); }
		 * 
		 * nameBuilder.deleteCharAt(nameBuilder.length() - 1);
		 * 
		 * System.out.println(nameBuilder.toString()); }
		 */
		BeanItemContainer<SelectValue> selectValueContainerForPAClaimType = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		// masterService.getPAClaimTypeContainer();
		SelectValue accident = new SelectValue();
		SelectValue death = new SelectValue();
		accident.setValue(SHAConstants.ACCIDENT);
		death.setValue(SHAConstants.DEATH);
		selectValueContainerForPAClaimType.addBean(accident);
		selectValueContainerForPAClaimType.addBean(death);

		BeanItemContainer<SelectValue> selectValueContainerForPADocumentType = masterService
				.getTmpCpuCodeList();

		view.setViewPAackDocReceive(PASearchAckDocumentReceiverView.class,
				true, selectValueContainerForPAClaimType,
				selectValueContainerForPADocumentType);
	}

	protected void showPACreateRODWizard(
			@Observes @CDIEvent(PAMenuPresenter.PA_CREATE_ROD_WIZARD) final ParameterDTO parameters) {

		SearchCreateRODTableDTO tableDTO = (SearchCreateRODTableDTO) parameters
				.getPrimaryParameter();
		String screenName = (String) parameters.getSecondaryParameter(0, String.class);
		ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
		Long docAckKey = tableDTO.getAckNo();

		rodDTO.getDocumentDetails().setDocAcknowledgementKey(docAckKey);
		rodDTO.setScreenName(screenName);
		Long claimKey = tableDTO.getClaimkey();
		Claim claimByKey = claimService.getClaimByKey(claimKey);
		populatePADocumentDetails(rodDTO, docAckKey,
				ReferenceTable.ROD_CREATION,claimByKey);
		Boolean isValidClaimForROD = true;
		
		DocAcknowledgement docAcknowledgementBasedOnKey = createRodService.getDocAcknowledgementBasedOnKey(docAckKey);
		
		Boolean isValidClaim = true;
		if (null != claimByKey) {
			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaim = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
		}
		
		if(isValidClaim) {

		if (null != claimByKey) {
			rodDTO.setPreviousAccountDetailsList(populatePreviousAccountDetails(
					claimByKey.getIntimation().getIntimationId(), rodDTO
							.getDocumentDetails()
							.getDocumentReceivedFromValue()));
		}
		if (null != claimByKey) {
/*
			if (null != claimByKey.getIncidenceFlag()
					&& claimByKey.getIncidenceFlag().equalsIgnoreCase(
							SHAConstants.ACCIDENT_FLAG)) {
				rodDTO.getDocumentDetails().setAccidentOrDeath(true);
			} else {
				rodDTO.getDocumentDetails().setAccidentOrDeath(false);
			}
			rodDTO.getDocumentDetails().setAccidentOrDeathDate(
					claimByKey.getIncidenceDate());*/

			StarCommonUtils.getAccidentAndDeathDate(claimByKey, rodDTO);
			
			if(isValidClaimForROD)
			{
				if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(claimByKey.getClaimType().getKey()))
				{
					if(ackDocReceivedService.getDBTaskForPreauth(claimByKey.getIntimation(), SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE))
					{
						isValidClaimForROD = false;
						view.showErrorPopUp("Claim is pending for conversion. Please convert claim first before creating acknowledgement.");
					}
					NegotiationDetails negDtls = preauthService.getNegotiationPending(claimByKey.getKey());
					if(negDtls != null && negDtls.getNegotiationFlag().equals(SHAConstants.YES_FLAG)
							&& docAcknowledgementBasedOnKey.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
						isValidClaimForROD = false;
						view.showErrorPopUp("Claim is pending for negotiation. Please complete the negotiation before creating ROD");
					}
				}
			}
		}

		if (isValidClaimForROD) {
			// Long count =
			// getAckNoCountByClaimKey(claimKey,ReferenceTable.ACKNOWLEDGE_DOC_RECEIVED);
			Long count = getAckNoCountByClaimKey(claimKey,
					ReferenceTable.CREATE_ROD);
			rodDTO.setAcknowledgementNumber(count);
			NewIntimationDto newIntimationDto = new NewIntimationDto();
			ClaimDto claimDTO = null;
			if (claimByKey != null) {
				// setClaimValuesToDTO(preauthDTO, claimByKey);
				newIntimationDto = intimationService
						.getIntimationDto(claimByKey.getIntimation());
				claimDTO = ClaimMapper.getInstance().getClaimDto(claimByKey);
				if (claimDTO.getClaimType() != null
						&& claimDTO.getClaimType().getId()
								.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					Preauth latestPreauthByClaim = preauthService
							.getLatestPreauthByClaim(claimDTO.getKey());
					claimDTO.setCashlessAppAmt(latestPreauthByClaim
							.getTotalApprovalAmount());
				}
				claimDTO.setNewIntimationDto(newIntimationDto);
				rodDTO.setClaimDTO(claimDTO);
				claimDTO.getNewIntimationDto().setPolicy(claimByKey.getIntimation().getPolicy());
			}
			// Method to populate hospital values from VW_HOSPITALS;
			// if(null != rodDTO.get)
			if (null != claimByKey && null != claimByKey.getClaimType()
					&& null != claimByKey.getClaimType().getKey()) {
				if ((claimByKey.getClaimType().getKey())
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					if (null != newIntimationDto.getHospitalDto()
							.getHospitalType()) {
						populatePaymentDetailsForHosp(newIntimationDto
								.getHospitalDto().getKey(), rodDTO);

					}
				} else {
					/*
					 * rodDTO.getDocumentDetails().setPaymentModeFlag(
					 * ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					 */
					populatePaymentDetailsForReimbursementClaim(claimKey,
							rodDTO);
				}
			}
			Reimbursement previousLatestROD = createRodService
					.getPreviousRODDetails(claimKey);
			if(docAcknowledgementBasedOnKey != null){
				List<Reimbursement> listOfPreviousRODDetails = createRodService.getListOfPreviousRODDetails(claimKey);
				
				for (Reimbursement reimbursement : listOfPreviousRODDetails) {
					previousLatestROD = null;
					if((docAcknowledgementBasedOnKey.getHospitalisationFlag() != null && docAcknowledgementBasedOnKey.getHospitalisationFlag().equalsIgnoreCase("Y"))
							|| (docAcknowledgementBasedOnKey.getPartialHospitalisationFlag() != null && docAcknowledgementBasedOnKey.getPartialHospitalisationFlag().equalsIgnoreCase("Y"))){
						if((reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y"))
								|| (reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y"))){
							previousLatestROD = reimbursement;
							break;
						}
					}else{
						if(! (reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("Y"))
								|| (reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("Y"))){
							previousLatestROD = reimbursement;
							break;
						}
					}
				}
			}
			
			
			
			if (previousLatestROD != null) {
				// reimbursementKey = previousLatestROD.getKey();
				ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper
						.getInstance();
				// ZonalMedicalReviewMapper.getAllMapValues();
				PreauthDTO reimbursementDTO = mapper
						.getReimbursementDTO(previousLatestROD);
				reimbursementDTO.setIsPostHospitalization(true);
				setReimbursmentTOPreauthDTO(mapper, claimByKey,
						previousLatestROD, reimbursementDTO, true,
						SHAConstants.CREATE_ROD);
				Hospitals hospitalById = hospitalService
						.getHospitalById(claimByKey.getIntimation()
								.getHospital());
				ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = reimbursementDTO
						.getPreauthDataExtractionDetails()
						.getUpdateHospitalDetails();
				updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				updateHospitalDetails.setHospitalState(hospitalById.getState());
				updateHospitalDetails.setHospitalCode(hospitalById
						.getHospitalCode());
				reimbursementDTO
						.setReconsiderationList(getReconsiderRODRequest(claimByKey));
				reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
						previousLatestROD.getKey());
				reimbursementDTO.setIsPreviousROD(true);
				rodDTO.getDocumentDetails().setEmailId(
						reimbursementDTO.getPayeeEmailId());
				rodDTO.getDocumentDetails().setPayableAt(
						reimbursementDTO.getPayableAt());
				rodDTO.setPreauthDTO(reimbursementDTO);
				rodDTO.getDocumentDetails().setDateOfAdmission(
						reimbursementDTO.getPreauthDataExtractionDetails()
								.getAdmissionDate());
				rodDTO.getDocumentDetails().setDateOfDischarge(
						reimbursementDTO.getPreauthDataExtractionDetails()
								.getDischargeDate());
				if (null != previousLatestROD.getDoaChangeReason())
					rodDTO.getDocumentDetails().setReasonForChange(
							previousLatestROD.getDoaChangeReason());
				// reimbursementDTO.setPreviousROD(previousLatestROD);
				

				LegalHeirDTO legalHeirDTO = new LegalHeirDTO();				
				legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
				reimbursementDTO.setLegalHeirDto(legalHeirDTO);

				List<NomineeDetailsDto> nomineeDetailsListByTransactionKey = intimationService.getNomineeDetailsListByTransactionKey(previousLatestROD.getKey());
				if(nomineeDetailsListByTransactionKey != null && !nomineeDetailsListByTransactionKey.isEmpty()) {		
						rodDTO.getClaimDTO().getNewIntimationDto().setNomineeList(nomineeDetailsListByTransactionKey);
				}
					if(rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList() == null || rodDTO.getClaimDTO().getNewIntimationDto().getNomineeList().isEmpty()
							|| (reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null 
					&& reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased())) {
						List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(previousLatestROD.getKey());
						if(legalHeirList != null && !legalHeirList.isEmpty()) {
							List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
							LegalHeirDTO legalHeirDto;
							for (LegalHeir legalHeir : legalHeirList) {
								 legalHeirDto = new LegalHeirDTO(legalHeir);
								 legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
								 legalHeirDTOList.add(legalHeirDto);
							}
							reimbursementDTO.setLegalHeirDTOList(legalHeirDTOList);
						}
				}

				rodDTO.getClaimDTO().getNewIntimationDto().setNomineeList(intimationService.getNomineeDetailsListByTransactionKey(previousLatestROD.getKey()));
				if(previousLatestROD.getNomineeName() != null) {
					rodDTO.getClaimDTO().getNewIntimationDto().setNomineeName(previousLatestROD.getNomineeName());
				}	
				if(previousLatestROD.getNomineeAddr() != null) {
					rodDTO.getClaimDTO().getNewIntimationDto().setNomineeAddr(previousLatestROD.getNomineeAddr());
				}
			}

			/*
			 * if(null != rodDTO.getClaimDTO() &&
			 * (ReferenceTable.CLAIM_TYPE_CASHLESS
			 * ).equalsIgnoreCase(rodDTO.getClaimDTO().getClaimTypeValue())) {
			 * populatePaymentDetailsForCashLessClaim
			 * (newIntimationDto.getHospitalDto().getKey(),rodDTO); } else {
			 * populatePaymentDetailsForReimbursementClaim
			 * (rodDTO.getClaimDTO().getKey
			 * (),rodDTO,newIntimationDto.getHospitalDto().getKey());
			 * 
			 * }
			 */
			/*if (tableDTO.getHumanTaskListDTO() == null
					|| tableDTO.getHumanTaskListDTO().getPayload() == null
					|| tableDTO.getHumanTaskListDTO().getPayload().getRod() == null
					|| tableDTO.getHumanTaskListDTO().getPayload().getRod()
							.getKey() == null) {
				generateRODNumber(rodDTO);
				getPreviousRODNumber(rodDTO);
			} */

			Map<String, Object> outPutArray = (Map<String, Object>) tableDTO.getDbOutArray();
			Long payloadRodKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
			if(null == payloadRodKey || payloadRodKey.equals(0l))
			{
				generateRODNumber(rodDTO);
				getPreviousRODNumber(rodDTO);
			}
			else {
				Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(payloadRodKey);
				rodDTO.getDocumentDetails().setRodNumber(
						reimbursementByKey.getRodNumber());
				String diagnosisForPreauthByKey = "";
				if (reimbursementByKey != null) {
					diagnosisForPreauthByKey = preauthService
							.getDiagnosisForPreauthByKey(reimbursementByKey
									.getKey());

					if(reimbursementByKey.getNomineeName() != null) {
						rodDTO.getClaimDTO().getNewIntimationDto().setNomineeName(reimbursementByKey.getNomineeName());
					}	
					if(reimbursementByKey.getNomineeAddr() != null) {
						rodDTO.getClaimDTO().getNewIntimationDto().setNomineeAddr(reimbursementByKey.getNomineeAddr());
					}
				}
				
				rodDTO.setDiagnosis(diagnosisForPreauthByKey);
				rodDTO.setRodKeyFromPayload(payloadRodKey);
				
				List<NomineeDetailsDto> nomineeDetailsListByTransactionKey = intimationService.getNomineeDetailsListByTransactionKey(payloadRodKey);
				
				if(nomineeDetailsListByTransactionKey != null && !nomineeDetailsListByTransactionKey.isEmpty()) {
					rodDTO.getClaimDTO().getNewIntimationDto().setNomineeList(nomineeDetailsListByTransactionKey);
				}	

				rodDTO.getClaimDTO().getNewIntimationDto().setNomineeList(intimationService.getNomineeDetailsListByTransactionKey(payloadRodKey));

			}
			
			if ((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(rodDTO
					.getClaimDTO().getClaimTypeValue())
					&& null == rodDTO.getDocumentDetails().getDateOfAdmission()
					&& null == rodDTO.getDocumentDetails().getDateOfDischarge()) {
				Preauth preauth = createRodService
						.getLatestPreauthForClaim(claimKey);

				if (null != preauth) {
					rodDTO.getDocumentDetails().setDateOfAdmission(
							preauth.getDataOfAdmission());
					rodDTO.getDocumentDetails().setDateOfDischarge(
							preauth.getDateOfDischarge());
				} else {
					rodDTO.getDocumentDetails().setDateOfAdmission(
							rodDTO.getClaimDTO().getNewIntimationDto()
									.getAdmissionDate());

					// rodDTO.getDocumentDetails().setDateOfDischarge(rodDTO.getClaimDTO().getNewIntimationDto().get);
				}

				String diagnosisForPreauthByKey = "";
				if (preauth != null) {
					diagnosisForPreauthByKey = preauthService
							.getDiagnosisForPreauthByKey(preauth.getKey());

				}
				rodDTO.setDiagnosis(diagnosisForPreauthByKey);

			} else {
				if (null == previousLatestROD) {
					rodDTO.getDocumentDetails().setDateOfAdmission(
							rodDTO.getClaimDTO().getNewIntimationDto()
									.getAdmissionDate());
				}
			}
			Map<String, Integer> productBenefitMap = dbCalculationService
					.getProductBenefitFlag(claimByKey.getKey(), claimByKey
							.getIntimation().getPolicy().getProduct().getKey());
			/**
			 * Earlier , pre and post hospitalization was enabled only based on
			 * product conditions in ack , rod level and bill entry level. But
			 * as per mockup 13.7 , this validation needs to be removed at ack
			 * and rod level and should be available only at FA level. Hence
			 * hardcoding the values of pre and post in map , by which we always
			 * enable pre and post for all products at ACK and ROD level and bil
			 * entry level.
			 * */
			productBenefitMap.put("preHospitalizationFlag", 1);
			productBenefitMap.put("postHospitalizationFlag", 1);

			rodDTO.setProductBenefitMap(productBenefitMap);

			/*
			 * rodDTO.setProductBenefitMap(dbCalculationService.
			 * getProductBenefitFlag( claimByKey.getKey(),
			 * claimByKey.getIntimation().getPolicy() .getProduct().getKey()));
			 */

			rodDTO.setStrUserName(tableDTO.getUsername());
			// rodDTO.setStrUserName("weblogic");
			rodDTO.setStrPassword(tableDTO.getPassword());
		//	rodDTO.setHumanTask(tableDTO.getHumanTaskListDTO());
			// Added for document details page enhancement.
			rodDTO.setCheckListTableContainerForROD(masterService
					.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_PA));
			/*
			 * createRodService.getBenefitFlagDetails(rodDTO.getClaimDTO()
			 * .getKey(), rodDTO);
			 */

			createRodService.getBillClassificationFlagDetails(rodDTO
					.getClaimDTO().getKey(), rodDTO);

			/*
			 * rodDTO.setProductBenefitMap(dbCalculationService.
			 * getProductBenefitFlag( claimByKey.getKey(),
			 * claimByKey.getIntimation().getPolicy() .getProduct().getKey()));
			 */
			if(screenName == null || screenName.isEmpty()){
				rodDTO.setReconsiderRodRequestList(getReconsiderRODRequestForRodAndBillEntry(claimByKey));
			} else {
				rodDTO.setReconsiderRodRequestList(getReconsiderRODRequestForRodAndBillEntryForUpdateROD(claimByKey));
			}

			// rodDTO.setUploadDocsList(getUploadedDocumentTableDataForBillEntry(key));

			List<RODQueryDetailsDTO> rodQueryDetailsList = getRODQueryDetailsForCreateRodandBillEntry(
					claimByKey, docAckKey);
			rodDTO.setRodQueryDetailsList(rodQueryDetailsList);
			if (null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty()) {
				for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
					if (null != rodQueryDetailsDTO.getReplyStatus()
							&& ("Y").equalsIgnoreCase(rodQueryDetailsDTO
									.getReplyStatus())) {
						Map<String, Long> reconsiderationMap = new HashMap<String, Long>();
						reconsiderationMap.put("ackKey",
								rodQueryDetailsDTO.getAcknowledgementKey());
						reconsiderationMap.put("rodKey",
								rodQueryDetailsDTO.getReimbursementKey());
						rodDTO.setReconsiderationMap(reconsiderationMap);

						Reimbursement reimbursement = createRodService
								.getReimbursementObjectByKey(rodQueryDetailsDTO
										.getReimbursementKey());
						rodDTO.getDocumentDetails().setPaymentModeFlag(
								reimbursement.getPaymentModeId());
						rodDTO.getDocumentDetails().setEmailId(
								reimbursement.getPayeeEmailId());
						rodDTO.getDocumentDetails().setPanNo(
								reimbursement.getPanNumber());
						rodDTO.getDocumentDetails().setPayableAt(
								reimbursement.getPayableAt());
						SelectValue selValue = new SelectValue();
						selValue.setValue(reimbursement.getPayeeName());
						rodDTO.getDocumentDetails().setReasonForChange(
								reimbursement.getReasonForChange());
						rodDTO.getDocumentDetails().setLegalFirstName(
								reimbursement.getLegalHeirFirstName());
						rodDTO.getDocumentDetails().setAccountNo(
								reimbursement.getAccountNumber());
						if (null != reimbursement.getPaymentModeId()
								&& reimbursement
										.getPaymentModeId()
										.equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)) {
							if (null != reimbursement.getBankId()) {
								BankMaster masBank = masterService
										.getBankDetailsByKey(reimbursement
												.getBankId());
								rodDTO.getDocumentDetails().setBankId(
										masBank.getKey());
								rodDTO.getDocumentDetails().setBankName(
										masBank.getBankName());
								rodDTO.getDocumentDetails().setIfscCode(
										masBank.getIfscCode());
								rodDTO.getDocumentDetails().setBranch(
										masBank.getBranchName());
								rodDTO.getDocumentDetails().setCity(
										masBank.getCity());
							}
						}

						Long reimbursementKey = rodQueryDetailsDTO
								.getReimbursementKey();
						if ((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO
								.getDocumentDetails().getHospitalizationFlag())) {
							if (null == rodDTO.getDocumentDetails()
									.getHospitalizationClaimedAmount()
									|| ("").equalsIgnoreCase(rodDTO
											.getDocumentDetails()
											.getHospitalizationClaimedAmount())) {
								Double totalClaimedAmt = createRodService
										.getTotalClaimedAmt(reimbursementKey,
												ReferenceTable.HOSPITALIZATION);
								if (null != totalClaimedAmt)
									rodDTO.getDocumentDetails()
											.setHospitalizationClaimedAmount(
													String.valueOf(totalClaimedAmt));
							}
						}

						if ((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO
								.getDocumentDetails()
								.getPreHospitalizationFlag())) {
							if (null == rodDTO.getDocumentDetails()
									.getPreHospitalizationClaimedAmount()
									|| ("").equalsIgnoreCase(rodDTO
											.getDocumentDetails()
											.getPreHospitalizationClaimedAmount())) {
								Double totalClaimedAmt = createRodService
										.getTotalClaimedAmt(
												reimbursementKey,
												ReferenceTable.PRE_HOSPITALIZATION);
								if (null != totalClaimedAmt)
									rodDTO.getDocumentDetails()
											.setPreHospitalizationClaimedAmount(
													String.valueOf(totalClaimedAmt));
							}
						}
						if ((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO
								.getDocumentDetails()
								.getPostHospitalizationFlag())) {
							if (null == rodDTO.getDocumentDetails()
									.getPostHospitalizationClaimedAmount()
									|| ("").equalsIgnoreCase(rodDTO
											.getDocumentDetails()
											.getPostHospitalizationClaimedAmount())) {
								Double totalClaimedAmt = createRodService
										.getTotalClaimedAmt(
												reimbursementKey,
												ReferenceTable.POST_HOSPITALIZATION);
								if (null != totalClaimedAmt)
									rodDTO.getDocumentDetails()
											.setPostHospitalizationClaimedAmount(
													String.valueOf(totalClaimedAmt));
							}
						}
						
						LegalHeirDTO legalHeirDTO = new LegalHeirDTO();				
						legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
						rodDTO.getPreauthDTO().setLegalHeirDto(legalHeirDTO);
						rodDTO.getPreauthDTO().getPreauthDataExtractionDetails().setNomineeDeceasedFlag(reimbursement.getNomineeFlag());
						
						List<NomineeDetailsDto> nomineeDetailsListByTransactionKey = intimationService.getNomineeDetailsListByTransactionKey(reimbursement.getKey());
						if(nomineeDetailsListByTransactionKey != null && !nomineeDetailsListByTransactionKey.isEmpty()) {
							rodDTO.getClaimDTO().getNewIntimationDto().setNomineeList(nomineeDetailsListByTransactionKey);
						}	

						if(reimbursement.getNomineeFlag() != null 
								&& reimbursement.getNomineeFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
							List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(reimbursement.getKey());
							if(legalHeirList != null && !legalHeirList.isEmpty()) {
								List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
								LegalHeirDTO legalHeirDto;
								for (LegalHeir legalHeir : legalHeirList) {
									 legalHeirDto = new LegalHeirDTO(legalHeir);
									 legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
									 legalHeirDTOList.add(legalHeirDto);
								}
								rodDTO.getPreauthDTO().setLegalHeirDTOList(legalHeirDTOList);
							}
						
						}
						// To populate bank details.
						/*
						 * rodDTO.get
						 * rodDTO.getDocumentDetails().setIfscCode(reimbursement
						 * .getB);
						 */
					}
				}
			}

			/*List<RODQueryDetailsDTO> rodPaymentQueryDetailsList = getRODPaymentQueryDetailsForCreateRodandBillEntry(
					claimByKey, docAckKey);
			rodDTO.setPaymentQueryDetailsList(rodPaymentQueryDetailsList);			
			
			if (null != rodPaymentQueryDetailsList && !rodPaymentQueryDetailsList.isEmpty()) {
				for (RODQueryDetailsDTO rodQueryDetailsDTO : rodPaymentQueryDetailsList) {
					if (null != rodQueryDetailsDTO.getReplyStatus()
							&& ("Y").equalsIgnoreCase(rodQueryDetailsDTO
									.getReplyStatus())) {
						Map<String, Long> reconsiderationMap = new HashMap<String, Long>();
						reconsiderationMap.put("ackKey",
								rodQueryDetailsDTO.getAcknowledgementKey());
						reconsiderationMap.put("rodKey",
								rodQueryDetailsDTO.getReimbursementKey());
						rodDTO.setReconsiderationMap(reconsiderationMap);

						Reimbursement reimbursement = createRodService
								.getReimbursementObjectByKey(rodQueryDetailsDTO
										.getReimbursementKey());
						rodDTO.getDocumentDetails().setPaymentModeFlag(
								reimbursement.getPaymentModeId());
						rodDTO.getDocumentDetails().setEmailId(
								reimbursement.getPayeeEmailId());
						rodDTO.getDocumentDetails().setPanNo(
								reimbursement.getPanNumber());
						rodDTO.getDocumentDetails().setPayableAt(
								reimbursement.getPayableAt());
						SelectValue selValue = new SelectValue();
						selValue.setValue(reimbursement.getPayeeName());
						rodDTO.getDocumentDetails().setReasonForChange(
								reimbursement.getReasonForChange());
						rodDTO.getDocumentDetails().setLegalFirstName(
								reimbursement.getLegalHeirFirstName());
						rodDTO.getDocumentDetails().setAccountNo(
								reimbursement.getAccountNumber());
						if (null != reimbursement.getPaymentModeId()
								&& reimbursement
										.getPaymentModeId()
										.equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)) {
							if (null != reimbursement.getBankId()) {
								BankMaster masBank = masterService
										.getBankDetailsByKey(reimbursement
												.getBankId());
								rodDTO.getDocumentDetails().setBankId(
										masBank.getKey());
								rodDTO.getDocumentDetails().setBankName(
										masBank.getBankName());
								rodDTO.getDocumentDetails().setIfscCode(
										masBank.getIfscCode());
								rodDTO.getDocumentDetails().setBranch(
										masBank.getBranchName());
								rodDTO.getDocumentDetails().setCity(
										masBank.getCity());
							}
						}

						Long reimbursementKey = rodQueryDetailsDTO
								.getReimbursementKey();
						if ((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO
								.getDocumentDetails().getHospitalizationFlag())) {
							if (null == rodDTO.getDocumentDetails()
									.getHospitalizationClaimedAmount()
									|| ("").equalsIgnoreCase(rodDTO
											.getDocumentDetails()
											.getHospitalizationClaimedAmount())) {
								Double totalClaimedAmt = createRodService
										.getTotalClaimedAmt(reimbursementKey,
												ReferenceTable.HOSPITALIZATION);
								if (null != totalClaimedAmt)
									rodDTO.getDocumentDetails()
											.setHospitalizationClaimedAmount(
													String.valueOf(totalClaimedAmt));
							}
						}

						if ((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO
								.getDocumentDetails()
								.getPreHospitalizationFlag())) {
							if (null == rodDTO.getDocumentDetails()
									.getPreHospitalizationClaimedAmount()
									|| ("").equalsIgnoreCase(rodDTO
											.getDocumentDetails()
											.getPreHospitalizationClaimedAmount())) {
								Double totalClaimedAmt = createRodService
										.getTotalClaimedAmt(
												reimbursementKey,
												ReferenceTable.PRE_HOSPITALIZATION);
								if (null != totalClaimedAmt)
									rodDTO.getDocumentDetails()
											.setPreHospitalizationClaimedAmount(
													String.valueOf(totalClaimedAmt));
							}
						}
						if ((SHAConstants.YES_FLAG).equalsIgnoreCase(rodDTO
								.getDocumentDetails()
								.getPostHospitalizationFlag())) {
							if (null == rodDTO.getDocumentDetails()
									.getPostHospitalizationClaimedAmount()
									|| ("").equalsIgnoreCase(rodDTO
											.getDocumentDetails()
											.getPostHospitalizationClaimedAmount())) {
								Double totalClaimedAmt = createRodService
										.getTotalClaimedAmt(
												reimbursementKey,
												ReferenceTable.POST_HOSPITALIZATION);
								if (null != totalClaimedAmt)
									rodDTO.getDocumentDetails()
											.setPostHospitalizationClaimedAmount(
													String.valueOf(totalClaimedAmt));
							}
						}

						// To populate bank details.
						
						 * rodDTO.get
						 * rodDTO.getDocumentDetails().setIfscCode(reimbursement
						 * .getB);
						 
					}
				}
			}*/
			
			
			
			if (rodDTO.getClaimDTO().getClaimType() != null
					&& rodDTO.getClaimDTO().getClaimType().getId()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				String[] rodSplit = rodDTO.getDocumentDetails().getRodNumber()
						.split("/");
				String string = rodSplit[rodSplit.length - 1];
				if (SHAUtils.getIntegerFromString(string) == 1
						|| previousLatestROD == null) {
					PreMedicalMapper premedicalMapper = PreMedicalMapper
							.getInstance();
					// PreMedicalMapper.getAllMapValues();
					List<Preauth> preauthByClaimKey = preauthService
							.getPreauthByClaimKey(claimByKey.getKey());
					Preauth previousPreauth = null;
					if (!preauthByClaimKey.isEmpty()) {
						previousPreauth = preauthByClaimKey.get(0);
						String[] split = previousPreauth.getPreauthId().split(
								"/");
						String defaultNumber = split[split.length - 1];
						Integer nextReferenceNo = Integer
								.valueOf(defaultNumber);
						for (Preauth preauth : preauthByClaimKey) {
							if (preauth.getPreauthId() != null) {
								String[] splitNumber = preauth.getPreauthId()
										.split("/");
								String number = splitNumber[splitNumber.length - 1];
								if (Integer.valueOf(number) > Integer
										.valueOf(defaultNumber)) {
									previousPreauth = preauth;
									nextReferenceNo = Integer.valueOf(number);
								}
							}
						}
					}

					if (previousPreauth != null) {
						PreauthDTO reimbursementDTO = premedicalMapper
								.getPreauthDTO(previousPreauth);
						reimbursementDTO.getPreauthDataExtractionDetails().setVentilatorSupportFlag(previousPreauth.getVentilatorSupport());
						setpreauthTOPreauthDTO(premedicalMapper, claimByKey,
								previousPreauth, reimbursementDTO, true);
						reimbursementDTO.setIsCashlessType(true);
						reimbursementDTO.getPreauthDataExtractionDetails()
								.setDischargeDate(
										previousPreauth.getDateOfDischarge());

						rodDTO.setPreauthDTO(reimbursementDTO);

						rodDTO.getClaimDTO().setLatestPreauthKey(
								previousPreauth.getKey());

					}
				}
				Preauth preauth = preauthService
						.getLatestPreauthByClaim(claimByKey.getKey());
				String diagnosisForPreauthByKey = "";
				if (preauth != null) {
					diagnosisForPreauthByKey = preauthService
							.getDiagnosisForPreauthByKey(preauth.getKey());

				}
				rodDTO.setDiagnosis(diagnosisForPreauthByKey);

			}

			RRCDTO rrcDTO = new RRCDTO();
			rrcDTO.setClaimDto(claimDTO);
			rrcDTO.setNewIntimationDTO(newIntimationDto);
			rrcDTO.setStrUserName(tableDTO.getUsername());
			
			Double insuredSumInsured = 0d;
			
			if(!ReferenceTable.getGPAProducts().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				
				insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(rrcDTO.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							rrcDTO.getNewIntimationDTO().getPolicy().getKey(),rrcDTO.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService
						.getGPAInsuredSumInsured(rrcDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								rrcDTO.getNewIntimationDTO().getPolicy().getKey());
			}
			loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured,
					SHAConstants.CREATE_ROD);			
			
			rodDTO.getDocumentDetails().setPaSumInsured(insuredSumInsured);
			
			rodDTO.getDocumentDetails().setOrganisationName(claimByKey.getIntimation().getPolicy().getProposerFirstName());
			rodDTO.getDocumentDetails().setDateOfBirth(claimByKey.getGpaParentDOB());			
			rodDTO.getDocumentDetails().setAge(claimByKey.getGpaParentAge());
			rodDTO.getDocumentDetails().setParentName(claimByKey.getGpaParentName());
			rodDTO.getDocumentDetails().setRiskName(claimByKey.getGpaRiskName());
			rodDTO.getDocumentDetails().setGpaRiskDOB(claimByKey.getGpaRiskDOB());
			rodDTO.getDocumentDetails().setGpaRiskAge(claimByKey.getGpaRiskAge());
			rodDTO.getDocumentDetails().setGpaSection(claimByKey.getGpaSection());
			
			SelectValue select = new SelectValue();
			select.setId(1l);
			select.setValue(claimByKey.getGpaCategory());
			rodDTO.getDocumentDetails().setGpaCategory(select);
			
			if(null == insuredSumInsured)
			{
				rodDTO.getDocumentDetails().setPaSumInsured(0d);
			}
			if(null == claimByKey.getGpaParentAge())
			{
				rodDTO.getDocumentDetails().setAge(0d);
			}

			if(null == claimByKey.getGpaRiskAge())
			{
				rodDTO.getDocumentDetails().setGpaRiskAge(0d);
			}
			// loadRRCRequestValuesForProcessing(rrcDTO,insuredSumInsured,SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);

			rodDTO.setRrcDTO(rrcDTO);

			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if (strPremiaFlag != null
					&& ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance()
						.get64VBStatus(
								claimByKey.getIntimation().getPolicy()
										.getPolicyNumber(), claimByKey.getIntimation().getIntimationId());
				if (get64vbStatus != null
						&& SHAConstants.DISHONOURED
								.equalsIgnoreCase(get64vbStatus)) {
					rodDTO.setIsDishonoured(true);
				}
			}

			// Preauth latestPreauthByClaim =
			// createRodService.getLatestPreauthByClaim(5014232l);
			// List<ClaimAmountDetails> claimAmountDetailsByPreauth =
			// createRodService.getClaimAmountDetailsByPreauth(5022781l);
			//
			// //
			// String sendToWhere = ReferenceTable.NORMAL;
			// //
			// // if(SHAUtils.isDirectToBilling(latestPreauthByClaim,
			// claimAmountDetailsByPreauth)) {
			// // sendToWhere = ReferenceTable.DIRECT_TO_BILLING;
			// // } else if(SHAUtils.isDirectToFinancial(latestPreauthByClaim,
			// claimAmountDetailsByPreauth)) {
			// // sendToWhere = ReferenceTable.DIRECT_TO_BILLING;
			// // }
			// ////
			// Reimbursement reimbursementObjectByKey =
			// createRodService.getReimbursementObjectByKey(5024383l);
			// Claim claimByClaimKey =
			// claimService.getClaimByClaimKey(5013027l);
			// //
			// createRodService.submitTaskToBPM(rodDTO,
			// reimbursementObjectByKey, false, sendToWhere, false,
			// claimByClaimKey);

			if (null != rrcDTO.getNewIntimationDTO()
					&& null != rrcDTO.getNewIntimationDTO().getPolicy()) {
				rodDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(rrcDTO
						.getNewIntimationDTO().getPolicy().getKey()));
				rodDTO.setIsPEDInitiatedbtn(rodDTO.getIsPEDInitiated());
			}

			/**
			 * Added for loading already uploaded docs table values.
			 * 
			 * */

			if (null != tableDTO.getIsDocumentUploaded()
					&& (SHAConstants.YES).equalsIgnoreCase(tableDTO
							.getIsDocumentUploaded())
					&& null != tableDTO.getAckDocKey()
					&& null != tableDTO.getDocAcknowledgementKey()) {
				rodDTO.setAlreadyUploadDocsList(createRodService
						.getUploadDocumentForAcknowledgementDocKey(tableDTO
								.getDocAcknowledgementKey()));
			}

			// if(rrcDTO.getNewIntimationDTO() != null &&
			// rrcDTO.getNewIntimationDTO().getInsuredPatient() != null &&
			// rrcDTO.getNewIntimationDTO().getInsuredPatient().getKey() !=
			// null){
			// Long claimCount =
			// preauthService.getClaimCount(rrcDTO.getNewIntimationDTO().getInsuredPatient().getKey());
			// if(claimCount != null){
			// rodDTO.setClaimCount(claimCount);
			// }
			// }

			Long productCode = rodDTO.getClaimDTO().getNewIntimationDto()
					.getPolicy().getProduct().getKey();
			Long insuredKey = rodDTO.getClaimDTO().getNewIntimationDto()
					.getInsuredPatient().getKey();

			if (null != productCode && null != insuredKey) {
				rodDTO.getDocumentDetails().setAdditionalCovers(
						dbCalculationService.getBebefitAdditionalCovers(
								SHAConstants.ADDITIONAL_COVER, productCode,
								insuredKey));

				rodDTO.getDocumentDetails().setOptionalCovers(
						dbCalculationService.getBebefitAdditionalCovers(
								SHAConstants.OPTIONAL_COVER, productCode,
								insuredKey));
			}
			
			if(ReferenceTable.getGPAProducts().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				
				if (null != productCode && null != insuredKey) {
					rodDTO.getDocumentDetails().setAdditionalCovers(
							dbCalculationService.getBebefitAdditionalCovers(
									SHAConstants.GPA_ADDITIONAL_COVER, productCode,
									insuredKey));
					
					rodDTO.getDocumentDetails().setOptionalCovers(
							dbCalculationService.getBebefitAdditionalCovers(
									SHAConstants.GPA_OPTIONAL_COVER, productCode,
									insuredKey));
					
					
				}
				
				
			}

			List<AddOnCoversTableDTO> addOnCoversList = createRodService
					.getAddOnCoversValue(docAckKey);
			if (null != addOnCoversList && !addOnCoversList.isEmpty()) {
				rodDTO.getDocumentDetails().setAddOnCoversList(addOnCoversList);
			}

			List<AddOnCoversTableDTO> optionalCoversList = createRodService
					.getOptionalCoversValue(docAckKey);
			if (null != optionalCoversList && !optionalCoversList.isEmpty()) {
				rodDTO.getDocumentDetails().setOptionalCoversList(
						optionalCoversList);
			}
			rodDTO.setDbOutArray(tableDTO.getDbOutArray());
			
			if(null != docAcknowledgementBasedOnKey){
				
				if(null != docAcknowledgementBasedOnKey.getWorkPlace() && (SHAConstants.YES_FLAG.equalsIgnoreCase(docAcknowledgementBasedOnKey.getWorkPlace()))){
					rodDTO.getDocumentDetails().setWorkOrNonWorkPlace(true);
				}
			}
			
			LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
			legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
			rodDTO.getPreauthDTO().setLegalHeirDto(legalHeirDTO);
			
			view.setPACreateRODWizardView(PACreateRODWizardView.class, rodDTO);

			}
		}
	}

	private void populatePADocumentDetails(ReceiptOfDocumentsDTO rodDTO,
			Long key, String strPresenterString,Claim claim) {
		DocAcknowledgement docAcknowledgment = null;
		if ((ReferenceTable.ROD_CREATION).equalsIgnoreCase(strPresenterString)) {
			docAcknowledgment = createRodService
					.getDocAcknowledgementBasedOnKey(key);
			if (null != docAcknowledgment) {
				rodDTO.getDocumentDetails().setDocumentCheckList(
						getCheckListValidationTableValues(key,
								docAcknowledgment));
				rodDTO.setUploadDocsList(getUploadedDocumentTableDataForBillEntry(docAcknowledgment
						.getRodKey()));

			}

		}
		// Bill entry in place of key , rod key will be passed.
		else if ((ReferenceTable.BILL_ENTRY)
				.equalsIgnoreCase(strPresenterString)) {
			Long ackDocKey = createRodService
					.getLatestDocAcknowledgementKey(key);
			docAcknowledgment = createRodService
					.getDocAcknowledgementBasedOnKey(ackDocKey);
			rodDTO.getDocumentDetails().setDocAcknowledgementKey(ackDocKey);
			rodDTO.getDocumentDetails().setRodKey(key);
			if (null != docAcknowledgment)
				rodDTO.getDocumentDetails().setDocumentCheckList(
						getCheckListValidationTableValues(ackDocKey,
								docAcknowledgment));
			rodDTO.setUploadDocsList(getUploadedDocumentTableDataForBillEntry(key));
			Reimbursement reimb = createRodService
					.getReimbursementByRODKey(key);
			if (null != reimb)
				rodDTO.getDocumentDetails().setReferToBillEntryBillingRemarks(
						reimb.getBillEntryRemarks());

			UploadDocumentDTO uploadDocumentDTO = claimRequestBenefitsService
					.getReimbursementBenefitsValue(key);
			/*
			 * if((ReferenceTable.BENEFITS_HOSPITAL_CASH).equalsIgnoreCase(
			 * uploadDocumentDTO.getHospitalBenefitFlag())) {
			 * rodDTO.getAddOnBenefitsDTO
			 * ().setAdmittedNoOfDays(uploadDocumentDTO
			 * .getHospitalCashNoofDays()) }
			 */
			if (uploadDocumentDTO != null) {
				if ((ReferenceTable.BENEFITS_PATIENT_CARE)
						.equalsIgnoreCase(uploadDocumentDTO
								.getPatientCareBenefitFlag())) {
					List<PatientCareDTO> patientCareList = claimRequestBenefitsService
							.getPatientCareDetails(uploadDocumentDTO
									.getPatientBenefitKey());
					if (null != patientCareList && !patientCareList.isEmpty()) {
						uploadDocumentDTO.setPatientCareDTO(patientCareList);
					}
					// rodDTO.getAddOnBenefitsDTO().setAdmittedNoOfDays(uploadDocumentDTO.getPatientCareNoofDays());
				}
				rodDTO.setUploadDocumentsDTO(uploadDocumentDTO);
			}

		} else if ((ReferenceTable.CLAIM_BILLING_BENEFITS)
				.equalsIgnoreCase(strPresenterString)) {
			Reimbursement reimb = createRodService
					.getReimbursementObjectByKey(key);
			rodDTO.setStatusKey(reimb.getStatus().getKey());
			Long ackDocKey = createRodService
					.getLatestDocAcknowledgementKey(key);
			docAcknowledgment = createRodService
					.getDocAcknowledgementBasedOnKey(ackDocKey);
			rodDTO.getDocumentDetails().setDocAcknowledgementKey(ackDocKey);
			rodDTO.getDocumentDetails().setRodKey(key);
			// List<UploadDocumentDTO> uploadDocumentDTOList =
			// claimRequestBenefitsService.getReimbursementBenefitsValue(key);
			UploadDocumentDTO uploadDocumentDTO = claimRequestBenefitsService
					.getReimbursementBenefitsValue(key);
			/*
			 * if((ReferenceTable.BENEFITS_HOSPITAL_CASH).equalsIgnoreCase(
			 * uploadDocumentDTO.getHospitalBenefitFlag())) {
			 * rodDTO.getAddOnBenefitsDTO
			 * ().setAdmittedNoOfDays(uploadDocumentDTO
			 * .getHospitalCashNoofDays()) }
			 */
			if (uploadDocumentDTO != null) {
				if ((ReferenceTable.BENEFITS_PATIENT_CARE)
						.equalsIgnoreCase(uploadDocumentDTO
								.getPatientCareBenefitFlag())) {
					List<PatientCareDTO> patientCareList = claimRequestBenefitsService
							.getPatientCareDetails(uploadDocumentDTO
									.getPatientBenefitKey());
					if (null != patientCareList && !patientCareList.isEmpty()) {
						uploadDocumentDTO.setPatientCareDTO(patientCareList);
					}
					// rodDTO.getAddOnBenefitsDTO().setAdmittedNoOfDays(uploadDocumentDTO.getPatientCareNoofDays());
				}
				rodDTO.setUploadDocumentsDTO(uploadDocumentDTO);
			} else {
				Notification
						.show("Before processing the claim benefits , please first make sure you had entered the benefits details at bill entry level...");
			}

		}
		if (null != docAcknowledgment) {
			rodDTO.getDocumentDetails().setAdditionalRemarks(
					docAcknowledgment.getAdditionalRemarks());
			rodDTO.getDocumentDetails().setDocumentsReceivedDate(
					docAcknowledgment.getDocumentReceivedDate());
			rodDTO.getDocumentDetails().setDocumentReceivedFromValue(
					docAcknowledgment.getDocumentReceivedFromId().getValue());
			rodDTO.getDocumentDetails().setModeOfReceiptValue(
					docAcknowledgment.getModeOfReceiptId().getValue());
			rodDTO.getDocumentDetails().setAccidentOrDeath(
					rodDTO.getClaimDTO().getIncidenceFlag());
			rodDTO.getDocumentDetails().setAccidentOrDeathDate(
					rodDTO.getClaimDTO().getIncidenceDate());
			rodDTO.getDocumentDetails().setDocumentTypeId(
					docAcknowledgment.getDocumentTypeId());		
			/*String branchOfficeCode = "";
			if(null != claim.getIntimation().getPolicy().getHomeOfficeCode())
			{
			 branchOfficeCode = claim.getIntimation().getPolicy().getHomeOfficeCode();
			
			
			OrganaizationUnit organization = createRodService.getBranchCode(branchOfficeCode);
			if(null != organization)
			{
				rodDTO.getDocumentDetails().setOrganisationName(organization.getOrganizationUnitName());
			}			
			
			}
			rodDTO.getDocumentDetails().setDateOfBirth(claim.getIntimation().getPaParentDOB());
			rodDTO.getDocumentDetails().setRiskName(claim.getIntimation().getInsured().getInsuredName());
			rodDTO.getDocumentDetails().setAge(claim.getIntimation().getPaParentAge());
			rodDTO.getDocumentDetails().setParentName(claim.getIntimation().getPaPatientName());*/
			
			
			if( null == claim.getIntimation().getPaParentAge())
			{
				rodDTO.getDocumentDetails().setAge(0d);
			}			
			

			if (null != rodDTO.getDocumentDetails()
					&& null != rodDTO.getDocumentDetails().getDocumentTypeId()) {
				MastersValue documentDetail = createRodService
						.getDocTypeValue(rodDTO.getDocumentDetails()
								.getDocumentTypeId());
				if (documentDetail != null) {
					rodDTO.getDocumentDetails().setDocumentTypeValue(
							documentDetail.getValue());
					SelectValue selectValue = new SelectValue();
					selectValue.setId(rodDTO.getDocumentDetails()
							.getDocumentTypeId());
					selectValue.setValue(rodDTO.getDocumentDetails()
							.getDocumentTypeValue());
					rodDTO.getDocumentDetails().setDocumentType(selectValue);

				}
			}

			if (null != docAcknowledgment.getBenifitFlag()
					&& docAcknowledgment.getBenifitFlag().equalsIgnoreCase(
							SHAConstants.DEATH_FLAGS)) {
				rodDTO.getDocumentDetails().setDeath(true);
			} else if (null != docAcknowledgment.getBenifitFlag()
					&& docAcknowledgment.getBenifitFlag().equalsIgnoreCase(
							SHAConstants.PPD)) {
				rodDTO.getDocumentDetails().setPermanentPartialDisability(true);
			} else if (null != docAcknowledgment.getBenifitFlag()
					&& docAcknowledgment.getBenifitFlag().equalsIgnoreCase(
							SHAConstants.PTD)) {
				rodDTO.getDocumentDetails().setPermanentTotalDisability(true);
			} else if (null != docAcknowledgment.getBenifitFlag()
					&& docAcknowledgment.getBenifitFlag().equalsIgnoreCase(
							SHAConstants.TTD)) {
				rodDTO.getDocumentDetails().setTemporaryTotalDisability(true);
			} else if (null != docAcknowledgment.getBenifitFlag()
					&& docAcknowledgment.getBenifitFlag().equalsIgnoreCase(
							SHAConstants.HOSP)) {
				rodDTO.getDocumentDetails().setHospitalization(true);
			} else if (null != docAcknowledgment.getBenifitFlag()
					&& docAcknowledgment.getBenifitFlag().equalsIgnoreCase(
							SHAConstants.PART)) {
				rodDTO.getDocumentDetails().setPartialHospitalization(true);
			} else {
				//rodDTO.getDocumentDetails().setHospitalExpensesCover(true);
			}
			
			if(null != docAcknowledgment.getBenifitClaimedAmount())
			{
				rodDTO.getDocumentDetails().setBenifitClaimedAmount(String.valueOf(docAcknowledgment.getBenifitClaimedAmount()));
			}

			if (null != docAcknowledgment.getReconsiderationReasonId()) {
				rodDTO.getDocumentDetails()
						.setReasonForReconsiderationRequestValue(
								docAcknowledgment.getReconsiderationReasonId()
										.getValue());
			}

			/***
			 * Fix for ticket 4287 --- starts
			 */
			rodDTO.getDocumentDetails().setEmailId(
					docAcknowledgment.getInsuredEmailId());
			rodDTO.setEmailIdForPaymentMode(docAcknowledgment
					.getInsuredEmailId());
			/***
			 * Fix for ticket 4287 --- ends
			 */

			if (null != docAcknowledgment.getReconsiderationReasonId()) {
				rodDTO.getDocumentDetails()
						.setReasonForReconsiderationRequestValue(
								docAcknowledgment.getReconsiderationReasonId()
										.getValue());
			}
			if (null != docAcknowledgment.getPaymentCancellationFlag()) {
				if (("Y").equalsIgnoreCase(docAcknowledgment
						.getPaymentCancellationFlag())) {
					rodDTO.getDocumentDetails().setPaymentCancellationNeeded(
							true);
					rodDTO.getDocumentDetails()
							.setPaymentCancellationNeededFlag("Y");
				} else {
					rodDTO.getDocumentDetails().setPaymentCancellationNeeded(
							false);
					rodDTO.getDocumentDetails()
							.setPaymentCancellationNeededFlag("N");
				}
			}

			if (("Y").equals(docAcknowledgment.getReconsiderationRequest())) {
				rodDTO.getDocumentDetails().setReconsiderationRequestValue(
						"Yes");
				Map<String, Long> reconsiderationMap = new HashMap<String, Long>();
				reconsiderationMap.put("ackKey", docAcknowledgment.getKey());
				reconsiderationMap.put("rodKey", docAcknowledgment.getRodKey());
				rodDTO.setReconsiderationMap(reconsiderationMap);
			} else if (("N").equals(docAcknowledgment
					.getReconsiderationRequest()))
				rodDTO.getDocumentDetails()
						.setReconsiderationRequestValue("No");
			if (null != docAcknowledgment.getHospitalizationClaimedAmount()) {
				rodDTO.getDocumentDetails().setHospitalizationClaimedAmount(
						String.valueOf(docAcknowledgment
								.getHospitalizationClaimedAmount().intValue()));
			} else {
				rodDTO.getDocumentDetails().setHospitalizationClaimedAmount("");
			}
			if (null != docAcknowledgment.getPreHospitalizationClaimedAmount()) {
				rodDTO.getDocumentDetails().setPreHospitalizationClaimedAmount(
						String.valueOf(docAcknowledgment
								.getPreHospitalizationClaimedAmount()
								.intValue()));
			} else {
				rodDTO.getDocumentDetails().setPreHospitalizationClaimedAmount(
						"");
			}
			if (null != docAcknowledgment.getPostHospitalizationClaimedAmount()) {
				rodDTO.getDocumentDetails()
						.setPostHospitalizationClaimedAmount(
								String.valueOf(docAcknowledgment
										.getPostHospitalizationClaimedAmount()
										.intValue()));
			} else {
				rodDTO.getDocumentDetails()
						.setPostHospitalizationClaimedAmount("");
			}

			if (docAcknowledgment.getHospitalizationClaimedAmount() != null) {
				rodDTO.getDocumentDetails().setHospitalizationClaimedAmount(
						String.valueOf(docAcknowledgment
								.getHospitalizationClaimedAmount().intValue()));
			}
			if (docAcknowledgment.getPreHospitalizationClaimedAmount() != null) {
				rodDTO.getDocumentDetails().setPreHospitalizationClaimedAmount(
						String.valueOf(docAcknowledgment
								.getPreHospitalizationClaimedAmount()
								.intValue()));
			}
			if (docAcknowledgment.getPostHospitalizationClaimedAmount() != null) {
				rodDTO.getDocumentDetails()
						.setPostHospitalizationClaimedAmount(
								String.valueOf(docAcknowledgment
										.getPostHospitalizationClaimedAmount()
										.intValue()));
			}

			if (docAcknowledgment.getBenifitClaimedAmount() != null) {
				rodDTO.getDocumentDetails().setBenifitClaimedAmount(
						String.valueOf(docAcknowledgment
								.getBenifitClaimedAmount().intValue()));
			}

			/*rodDTO.getDocumentDetails().setHospitalizationFlag(
					docAcknowledgment.getHospitalisationFlag());
			*/rodDTO.getDocumentDetails().setPreHospitalizationFlag(
					docAcknowledgment.getPreHospitalisationFlag());
			rodDTO.getDocumentDetails().setPostHospitalizationFlag(
					docAcknowledgment.getPostHospitalisationFlag());
			/*rodDTO.getDocumentDetails().setPartialHospitalizationFlag(
					docAcknowledgment.getPartialHospitalisationFlag());
			*/rodDTO.getDocumentDetails().setLumpSumAmountFlag(
					docAcknowledgment.getLumpsumAmountFlag());
			rodDTO.getDocumentDetails().setAddOnBenefitsHospitalCashFlag(
					docAcknowledgment.getHospitalCashFlag());
			rodDTO.getDocumentDetails().setAddOnBenefitsPatientCareFlag(
					docAcknowledgment.getPatientCareFlag());
			rodDTO.getDocumentDetails().setHospitalizationRepeatFlag(
					docAcknowledgment.getHospitalizationRepeatFlag());
			rodDTO.setCreatedBy(docAcknowledgment.getCreatedBy());
			rodDTO.setModifiedBy(docAcknowledgment.getModifiedBy());

		}
	}

	private List<List<PreviousAccountDetailsDTO>> populatePreviousAccountDetails(
			String intimationNo, String docReceivedFrom) {
		List<List<PreviousAccountDetailsDTO>> listOfPreviousClaims = new ArrayList<List<PreviousAccountDetailsDTO>>();
		ViewTmpIntimation viewTmpIntimation = intimationService
				.searchbyIntimationNoFromViewIntimation(intimationNo);
		if (null != viewTmpIntimation) {
			List<String> claimNoList = getClaimByPolicyWiseForPaymentDetails(viewTmpIntimation);
			if (null != claimNoList) {
				for (String claimNo : claimNoList) {
					List<PreviousAccountDetailsDTO> previousClaimsDTOList = createRodService
							.getPaymentDetailsForPreviousClaim(claimNo,
									docReceivedFrom);
					if (null != previousClaimsDTOList
							&& !previousClaimsDTOList.isEmpty())
						listOfPreviousClaims.add(previousClaimsDTOList);
				}

			}
		}
		return listOfPreviousClaims;
	}

	private List<String> getClaimByPolicyWiseForPaymentDetails(
			final ViewTmpIntimation intimation) {

		List<String> claimNoList = new ArrayList<String>();
		List<ViewTmpClaim> currentClaim = claimService
				.getTmpClaimByIntimation(intimation.getKey());

		String policyNumber = intimation.getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);

		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);

		previousclaimsList = getPreviousClaimForPreviousPolicy(
				byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);

		if (null != previousclaimsList && !previousclaimsList.isEmpty()) {
			for (ViewTmpClaim viewTmpClaim : previousclaimsList) {
				claimNoList.add(viewTmpClaim.getClaimId());
			}

		}

		return claimNoList;

	}

	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(
			String policyNumber, List<ViewTmpClaim> generatedList) {
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			if (renewalPolNo != null) {
				List<ViewTmpIntimation> intimationKeys = intimationService
						.getIntimationByPolicyKey(renewalPolNo.getKey());
				List<ViewTmpClaim> claimsByPolicyNumber = claimService
						.getViewTmpClaimsByIntimationKeys(intimationKeys);
				// List<ViewTmpClaim> previousPolicyPreviousClaims =
				// claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if (claimsByPolicyNumber != null
						&& !claimsByPolicyNumber.isEmpty()) {
					for (ViewTmpClaim viewTmpClaim : claimsByPolicyNumber) {
						if (!generatedList.contains(viewTmpClaim)) {
							generatedList.add(viewTmpClaim);
						}
					}
				}
				if (renewalPolNo != null
						&& renewalPolNo.getRenewalPolicyNumber() != null) {
					getPreviousClaimForPreviousPolicy(
							renewalPolNo.getRenewalPolicyNumber(),
							generatedList);
				} else {
					return generatedList;
				}
			}
		} catch (Exception e) {

		}
		return generatedList;
	}

	private PreauthDTO setReimbursmentTOPreauthDTO(
			ZonalMedicalReviewMapper reimbursementMapper, Claim claimByKey,
			Reimbursement reimbursement, PreauthDTO preauthDTO,
			Boolean isEnabled, String screenName) {

		if (claimByKey != null) {
			setClaimValuesToDTO(preauthDTO, claimByKey);
			NewIntimationDto newIntimationDto = intimationService
					.getIntimationDto(claimByKey.getIntimation());
			
			newIntimationDto.setInsuredDeceasedFlag(dbCalculationService.getInsuredPatientStatus(newIntimationDto.getPolicy().getKey(), newIntimationDto.getInsuredPatient().getKey()));
			
			ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(
					claimByKey);
			if (claimDTO.getClaimType() != null
					&& claimDTO.getClaimType().getId()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				Preauth latestPreauthByClaim = preauthService
						.getLatestPreauthByClaim(claimDTO.getKey());
				claimDTO.setCashlessAppAmt(latestPreauthByClaim
						.getTotalApprovalAmount());
			}

			Date admissionDate = newIntimationDto.getAdmissionDate();
			try {
				// String duration =
				// dbCalculationService.getPolicyAgeing(admissionDate,
				// newIntimationDto.getPolicy().getPolicyNumber());
				String duration = newIntimationDto.getPolicyYear();
				newIntimationDto.setPolicyAgeing(duration);
			} catch (Exception e) {

			}
			
			newIntimationDto.setNomineeName(reimbursement.getNomineeName());
			newIntimationDto.setNomineeAddr(reimbursement.getNomineeAddr());
			
			preauthDTO.setNewIntimationDTO(newIntimationDto);
			preauthDTO.setClaimDTO(claimDTO);
			preauthDTO.getClaimDTO().setNewIntimationDto(newIntimationDto);

		}
		preauthDTO.setKey(reimbursement.getKey());
		String policyNumber = preauthDTO.getPolicyDto().getPolicyNumber();
		if (screenName != null
				&& (!SHAConstants.BILLING.equalsIgnoreCase(screenName) && !SHAConstants.FINANCIAL
						.equalsIgnoreCase(screenName))) {
			/*Policy byPolicyNumber = policyService
					.getByPolicyNumber(policyNumber);
			List<ViewTmpIntimation> intimationKeys = intimationService
					.getIntimationByPolicyKey(byPolicyNumber.getKey());

			List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
			// List<ViewTmpClaim> claimsByPolicyNumber = claimService
			// .getViewTmpClaimsByPolicyNumber(policyNumber);
			List<ViewTmpClaim> claimsByPolicyNumber = claimService
					.getViewTmpClaimsByIntimationKeys(intimationKeys);

			previousclaimsList.addAll(claimsByPolicyNumber);

			previousclaimsList = getPreviousClaimForPreviousPolicy(
					byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);*/

			// List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
			// .getPreviousClaims(claimsByPolicyNumber,
			// claimByKey.getClaimId(), pedValidationService,
			// masterService);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(claimByKey.getKey(), claimByKey.getIntimation().getPolicy().getKey(), 
					claimByKey.getIntimation().getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

			/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
					.getPreviousClaims(previousclaimsList,
							claimByKey.getClaimId());*/

			// List<PreviousClaimsTableDTO> previousClaimDTOList = new
			// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

			preauthDTO.setPreviousClaimsList(previousClaimDTOList);
		}

		if (null != reimbursement.getCoordinatorFlag()
				&& reimbursement.getCoordinatorFlag().equalsIgnoreCase("y")) {

			CoordinatorDTO coordinatorDTO = reimbursementMapper
					.getCoordinatorDTO(preauthService
							.findCoordinatorByClaimKey(reimbursement.getClaim()
									.getKey()));
			coordinatorDTO.setRefertoCoordinator(true);
			preauthDTO.setCoordinatorDetails(coordinatorDTO);
		}

		UpdateHospital updateHospitalByReimbursementKey = reimbursementService
				.updateHospitalByReimbursementKey(reimbursement.getKey());
		if (updateHospitalByReimbursementKey != null) {
			ZonalReviewUpdateHospitalDetailsDTO updateHospitalDTO = reimbursementMapper
					.getUpdateHospitalDTO(updateHospitalByReimbursementKey);
			preauthDTO.getPreauthDataExtractionDetails()
					.setUpdateHospitalDetails(updateHospitalDTO);
		}

		PreviousClaimedHistory claimedHistoryByTransactionKey = reimbursementService
				.getClaimedHistoryByTransactionKey(reimbursement.getKey());
		if (claimedHistoryByTransactionKey != null) {
			preauthDTO.getPreauthDataExtractionDetails()
					.setCoveredPreviousClaim(true);
			preauthDTO
					.getPreauthDataExtractionDetails()
					.setOtherClaimDetails(
							reimbursementMapper
									.getClaimedHistoryDTO(claimedHistoryByTransactionKey));

			List<PreviousClaimedHospitalization> claimedHospitalizationByClaimedHistoryKey = reimbursementService
					.getClaimedHospitalizationByClaimedHistoryKey(claimedHistoryByTransactionKey
							.getKey());
			List<OtherClaimDiagnosisDTO> otherClaimDiagnosisDTOList = reimbursementMapper
					.getOtherClaimDiagnosisDTOList(claimedHospitalizationByClaimedHistoryKey);

			preauthDTO.getPreauthDataExtractionDetails()
					.setOtherClaimDetailsList(otherClaimDiagnosisDTOList);
		}

		List<SpecialityDTO> specialityDTOList = reimbursementMapper
				.getSpecialityDTOList(preauthService
						.findSpecialityByClaimKey(reimbursement.getClaim()
								.getKey()));
		for (SpecialityDTO specialityDTO : specialityDTOList) {
			specialityDTO.setEnableOrDisable(isEnabled);
		}
		preauthDTO.getPreauthDataExtractionDetails().setSpecialityList(
				specialityDTOList);

		List<PedValidation> findPedValidationByPreauthKey = preauthService
				.findPedValidationByPreauthKey(reimbursement.getKey());

		HashMap<String, String> mapAsList = preauthService
				.diagnosisNameAndIcdCode(findPedValidationByPreauthKey);
		preauthDTO.setFinalDiagnosis(mapAsList.get("diagnosis"));
		preauthDTO.setIcdCodeDesc(mapAsList.get("icdCode"));

		List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = reimbursementMapper
				.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

		// Fix for issue 732 starts.
		DBCalculationService dbCalculationService = new DBCalculationService();
		Double insuredSumInsured  = 0d;
		
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){

			insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					preauthDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), preauthDTO.getPolicyDto()
							.getKey());
		}
		preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);

		Double balanceSI = 0d;
		
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			
			balanceSI = dbCalculationService.getBalanceSI(
				preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
				preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				preauthDTO.getClaimKey(), insuredSumInsured,
				preauthDTO.getNewIntimationDTO().getKey()).get(
				SHAConstants.TOTAL_BALANCE_SI);
		}
		else
		{
			balanceSI = dbCalculationService.getGPABalanceSI(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey(), insuredSumInsured,
					preauthDTO.getNewIntimationDTO().getKey()).get(
					SHAConstants.TOTAL_BALANCE_SI);
		}
		
		
		
		List<Double> copayValue = dbCalculationService.getProductCoPay(
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey(), preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getKey(), preauthDTO
						.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId(),preauthDTO.getNewIntimationDTO());

		if (preauthDTO.getNewIntimationDTO() != null) {
			Product product = preauthDTO.getNewIntimationDTO().getPolicy()
					.getProduct();
			if (product.getCode() != null
					&& ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE
							.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE
							.equalsIgnoreCase(product.getCode())))) {
				// BalanceSumInsuredDTO claimsOutstandingAmt =
				// dbCalculationService.getClaimsOutstandingAmt(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				// preauthDTO.getNewIntimationDTO().getIntimationId(),
				// insuredSumInsured);
				preauthDTO.setSettledAmount(0d);
				preauthDTO
						.setDeductibleAmount((preauthDTO.getNewIntimationDTO()
								.getInsuredPatient() != null && preauthDTO
								.getNewIntimationDTO().getInsuredPatient()
								.getDeductibleAmount() != null) ? preauthDTO
								.getNewIntimationDTO().getInsuredPatient()
								.getDeductibleAmount() : 0d);
			}
		}

		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);

		if (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()
				.equals(ReferenceTable.SUPER_SURPLUS_INDIVIDUAL)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey().equals(ReferenceTable.SUPER_SURPLUS_FLOATER)) {
			// preauthDTO.setSpecificProductDeductibleDTO(getPreviousClaimsSuperSurplusTable(claimsByPolicyNumber
			// ,claimByKey.getClaimId(), preauthDTO));
		}

		Map<Long, SublimitFunObject> sublimitFunObjMap = getSublimitFunObjMap(
				preauthDTO.getPolicyDto().getProduct().getKey(),
				insuredSumInsured, preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredAge(), preauthDTO
						.getPreauthDataExtractionDetails().getSection(),
				preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		// Fix for issue 732 ends

		List<ProcedureDTO> procedureMainDTOList = reimbursementMapper
				.getProcedureMainDTOList(preauthService
						.findProcedureByPreauthKey(reimbursement.getKey()));
		for (ProcedureDTO procedureDTO : procedureMainDTOList) {
			procedureDTO.setEnableOrDisable(isEnabled);
			/*
			 * This is for Reverse Allocation . if any modifictation between
			 * Approved amount or Net Approved Amount then we have to set
			 * reverse allocation into PreauthDTO.
			 */

			if (procedureDTO.getApprovedAmount() != null
					&& procedureDTO.getNetApprovedAmount() != null
					&& !procedureDTO.getApprovedAmount().equals(
							procedureDTO.getNetApprovedAmount())) {
				preauthDTO.setIsReverseAllocation(true);
			}
			if (procedureDTO.getSublimitName() != null) {
				SublimitFunObject objSublimitFun = sublimitFunObjMap
						.get(procedureDTO.getSublimitName().getLimitId());
				if (objSublimitFun != null) {
					procedureDTO.setSublimitName(objSublimitFun);
					procedureDTO.setSublimitDesc(objSublimitFun
							.getDescription());
					procedureDTO.setSublimitAmount(String
							.valueOf(objSublimitFun.getAmount().intValue()));
				}
			}
			if (preauthDTO.getIsPostHospitalization()
					&& !preauthDTO.getHospitalizaionFlag()) {
				procedureDTO.setAmountConsideredAmount(0d);
				procedureDTO.setCopayAmount(0d);
				procedureDTO.setNetAmount(0d);
				procedureDTO.setApprovedAmount(0d);
				procedureDTO.setIsAmbChargeApplicable(false);
				procedureDTO.setAmbulanceCharge(0);
				procedureDTO.setAmtWithAmbulanceCharge(0);
			}

		}

		preauthDTO.getPreauthMedicalProcessingDetails()
				.setProcedureExclusionCheckTableList(procedureMainDTOList);

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : newPedValidationTableListDto) {
			pedValidationTableDTO.setEnableOrDisable(isEnabled);
			/*
			 * This is for Reverse Allocation . if any modifictation between
			 * Approved amount or Net Approved Amount then we have to set
			 * reverse allocation into PreauthDTO.
			 */

			if (pedValidationTableDTO.getApprovedAmount() != null
					&& pedValidationTableDTO.getNetApprovedAmount() != null
					&& !pedValidationTableDTO.getApprovedAmount().equals(
							pedValidationTableDTO.getNetApprovedAmount())) {
				preauthDTO.setIsReverseAllocation(true);
			}
			if (pedValidationTableDTO.getDiagnosisName() != null) {
				String diagnosis = masterService
						.getDiagnosis(pedValidationTableDTO.getDiagnosisName()
								.getId());
				pedValidationTableDTO.setDiagnosis(diagnosis);
				pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
				if (preauthDTO.getIsPostHospitalization()
						&& !preauthDTO.getHospitalizaionFlag()) {
					pedValidationTableDTO.setAmountConsideredAmount(0d);
					pedValidationTableDTO.setCopayAmount(0d);
					pedValidationTableDTO.setNetAmount(0d);
					pedValidationTableDTO.setApprovedAmount(0d);
					pedValidationTableDTO.setIsAmbChargeApplicable(false);
					pedValidationTableDTO.setAmbulanceCharge(0);
					pedValidationTableDTO.setAmtWithAmbulanceCharge(0);
				}
			}

			if (pedValidationTableDTO.getSublimitName() != null) {
				// Fix for issue 732 starts.
				SublimitFunObject objSublimitFun = sublimitFunObjMap
						.get(pedValidationTableDTO.getSublimitName()
								.getLimitId());
				// ClaimLimit limit =
				// claimService.getClaimLimitByKey(pedValidationTableDTO.getSublimitName().getLimitId());
				if (objSublimitFun != null) {
					pedValidationTableDTO.setSublimitName(objSublimitFun);
					pedValidationTableDTO.setSublimitAmt(String
							.valueOf(objSublimitFun.getAmount()));
				}
				// Fix for issue 732 ends
			}
			
			if(pedValidationTableDTO.getIcdCode() != null){
				SelectValue icdValues = masterService.getIcdDescriptionKey(pedValidationTableDTO.getIcdCode().getId());
				pedValidationTableDTO.getIcdCode().setValue(icdValues.getValue());
				pedValidationTableDTO.getIcdCode().setCommonValue(icdValues.getCommonValue());
				
			}

			if (pedValidationTableDTO.getSumInsuredRestriction() != null) {
				MastersValue master = masterService
						.getMaster(pedValidationTableDTO
								.getSumInsuredRestriction().getId());
				pedValidationTableDTO.getSumInsuredRestriction().setValue(
						master.getValue());
			}
			List<DiagnosisPED> pedDiagnosisByPEDValidationKey = preauthService
					.getPEDDiagnosisByPEDValidationKey(pedValidationTableDTO
							.getKey());
			List<PedDetailsTableDTO> dtoList = new ArrayList<PedDetailsTableDTO>();
			for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidationKey) {
				PedDetailsTableDTO dto = new PedDetailsTableDTO();
				// Added for disabling the procedure that is coming from
				// preauth.
				dto.setEnableOrDisable(isEnabled);
				dto.setDiagnosisName(pedValidationTableDTO.getDiagnosis());
				dto.setPolicyAgeing(pedValidationTableDTO.getPolicyAgeing());
				dto.setKey(diagnosisPED.getKey());
				dto.setPedCode(diagnosisPED.getPedCode());
				dto.setPedName(diagnosisPED.getPedName());

				if (diagnosisPED.getDiagonsisImpact() != null) {
					SelectValue value = new SelectValue();
					value.setId(diagnosisPED.getDiagonsisImpact().getKey());
					value.setValue(diagnosisPED.getDiagonsisImpact().getValue());
					dto.setPedExclusionImpactOnDiagnosis(value);
					BeanItemContainer<ExclusionDetails> contaniner = masterService
							.getExclusionDetailsByImpactKey(diagnosisPED
									.getDiagonsisImpact().getKey());
					dto.setExclusionAllDetails(contaniner.getItemIds());
				}

				if (diagnosisPED.getExclusionDetails() != null) {
					SelectValue exclusionValue = new SelectValue();
					exclusionValue.setId(diagnosisPED.getExclusionDetails()
							.getKey());
					exclusionValue.setValue(diagnosisPED.getExclusionDetails()
							.getExclusion());
					dto.setExclusionDetails(exclusionValue);
				}

				dto.setRemarks(diagnosisPED.getDiagnosisRemarks());
				dtoList.add(dto);
			}
			pedValidationTableDTO.setPedList(dtoList);

			if ((preauthDTO.getIsPostHospitalization() || preauthDTO
					.getIsPreHospApplicable())
					&& !preauthDTO.getHospitalizaionFlag()
					&& !preauthDTO.getPartialHospitalizaionFlag()) {
				pedValidationTableDTO.setAmountConsideredAmount(0d);
			}
		}

		// TODO: Need to change this behaviour..
		preauthDTO.getPreauthDataExtractionDetails().setDiagnosisTableList(
				newPedValidationTableListDto);

		// preauthDTO.getPreauthMedicalProcessingDetails().setPedValidationTableList(newPedValidationTableListDto);

		Map<String, Object> autoRestroation = dbCalculationService.getAutoRestroation(preauthDTO.getNewIntimationDTO().getIntimationId());
		if(autoRestroation != null){
			
			if(autoRestroation.containsKey(SHAConstants.AUTO_RESTORATION_CHK) && autoRestroation.get(SHAConstants.AUTO_RESTORATION_CHK) != null){
				preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(String.valueOf(autoRestroation.get(SHAConstants.AUTO_RESTORATION_CHK)));
				if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
					preauthDTO.setIsAutoRestorationDone(true);
				}
			}
			
			if(autoRestroation.containsKey(SHAConstants.AUTO_RESTORATION_COUNT) && autoRestroation.get(SHAConstants.AUTO_RESTORATION_COUNT) != null){
				preauthDTO.getPreauthDataExtractionDetails().setRestorationCount((Integer.valueOf(autoRestroation.get(SHAConstants.AUTO_RESTORATION_COUNT).toString())));
			}
		}
		
		if(null != preauthDTO.getNewIntimationDTO().getLobId().getId() && (((ReferenceTable.PA_LOB_KEY).equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))
				|| ((ReferenceTable.PACKAGE_MASTER_VALUE).equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))))
		{
			preauthDTO.setIsAutoRestorationDone(false);
			preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE);
		}
		/*for bancs*/
		if(reimbursement.getCatastrophicLoss() != null) {

			SelectValue value = masterService.getCatastropheData(reimbursement.getCatastrophicLoss());
			preauthDTO.getPreauthDataExtractionDetails().setCatastrophicLoss(value);
		}
		if(reimbursement.getNatureOfLoss() != null) {

			SelectValue value = masterService.getMasterValueForNatureCause(reimbursement.getNatureOfLoss());
			preauthDTO.getPreauthDataExtractionDetails().setNatureOfLoss(value);
		}
		if(reimbursement.getCauseOfLoss() != null) {
			SelectValue value = masterService.getMasterValueForNatureCause(reimbursement.getCauseOfLoss());
			preauthDTO.getPreauthDataExtractionDetails().setCauseOfLoss(value);
		}
	
		return preauthDTO;
	}

	private void populatePaymentDetailsForReimbursementClaim(Long claimKey,
			ReceiptOfDocumentsDTO rodDTO/* , Long HospitalKey */) {
		DocumentDetailsDTO docDetailsDTO = createRodService
				.getPreviousRODDetailsForClaim(claimKey,rodDTO.getDocumentDetails());
		if (null != docDetailsDTO) {

			rodDTO.getDocumentDetails().setPayableAt(
					docDetailsDTO.getPayableAt());
			rodDTO.getDocumentDetails().setEmailId(docDetailsDTO.getEmailId());
			rodDTO.getDocumentDetails().setPanNo(docDetailsDTO.getPanNo());
			rodDTO.getDocumentDetails().setPayeeName(
					docDetailsDTO.getPayeeName());
			rodDTO.getDocumentDetails().setReasonForChange(
					docDetailsDTO.getReasonForChange());
			rodDTO.getDocumentDetails().setLegalFirstName(
					docDetailsDTO.getLegalFirstName());

			if (null != docDetailsDTO.getPaymentModeFlag()
					&& docDetailsDTO.getPaymentModeFlag().equals(
							ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)) {

				rodDTO.getDocumentDetails().setAccountNo(
						docDetailsDTO.getAccountNo());
				rodDTO.getDocumentDetails().setBankName(
						docDetailsDTO.getBankName());
				rodDTO.getDocumentDetails()
						.setBankId(docDetailsDTO.getBankId());
				rodDTO.getDocumentDetails().setCity(docDetailsDTO.getCity());
				rodDTO.getDocumentDetails().setIfscCode(
						docDetailsDTO.getIfscCode());
				rodDTO.getDocumentDetails().setPaymentMode(false);
			} else {
				rodDTO.getDocumentDetails().setPaymentMode(true);
			}

		} else {
			rodDTO.getDocumentDetails().setPaymentModeFlag(
					ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
			// populatePaymentDetailsForCashLessClaim(HospitalKey, rodDTO);
		}

	}

	/*
	 * private void populatePaymentDetailsForHosp(Long key,
	 * ReceiptOfDocumentsDTO rodDTO) { Hospitals hospitals =
	 * createRodService.getHospitalsDetails(key, masterService); if (null !=
	 * hospitals) {
	 * 
	 * String strHospitalPaymentType = hospitals.getPaymentType(); String
	 * strIFscCode = hospitals.getIfscCode(); String strAccntNo =
	 * hospitals.getAccountNo(); if( (null != strIFscCode &&
	 * !("").equals(strIFscCode) && null != strAccntNo &&
	 * !("").equalsIgnoreCase(strAccntNo) ) ) {
	 * rodDTO.getDocumentDetails().setAccountNo(hospitals.getAccountNo());
	 * rodDTO.getDocumentDetails().setIfscCode(hospitals.getIfscCode());
	 * BankMaster masBank =
	 * masterService.getBankDetails(hospitals.getIfscCode()); if(null !=
	 * masBank) {
	 * rodDTO.getDocumentDetails().setBankName(masBank.getBankName());
	 * rodDTO.getDocumentDetails().setCity(masBank.getCity());
	 * rodDTO.getDocumentDetails().setBranch(masBank.getBranchName()); }
	 * rodDTO.getDocumentDetails().setPanNo(hospitals.getPanNumber());
	 * rodDTO.getDocumentDetails().setPaymentModeFlag(
	 * ReferenceTable.PAYMENT_MODE_BANK_TRANSFER); } else {
	 * rodDTO.getDocumentDetails().setPaymentModeFlag(
	 * ReferenceTable.PAYMENT_MODE_CHEQUE_DD); }
	 * 
	 * /*if (null != strHospitalPaymentType &&
	 * !("").equalsIgnoreCase(strHospitalPaymentType))
	 */
	/*
	 * { if ((ReferenceTable.CHEQUE_DD)
	 * .equalsIgnoreCase(strHospitalPaymentType)) { // In details page , if its
	 * true, the cheque/DD will be // selected. Else bank transfer will
	 * selected. // rodDTO.getDocumentDetails().setPaymentMode(true);
	 * rodDTO.getDocumentDetails().setPaymentModeFlag(
	 * ReferenceTable.PAYMENT_MODE_CHEQUE_DD); } else if
	 * ((ReferenceTable.BANK_TRANSFER)
	 * .equalsIgnoreCase(strHospitalPaymentType)) { //
	 * rodDTO.getDocumentDetails().setPaymentMode(false);
	 * rodDTO.getDocumentDetails().setAccountNo(hospitals.getAccountNo());
	 * rodDTO.getDocumentDetails().setIfscCode(hospitals.getIfscCode());
	 * BankMaster masBank =
	 * masterService.getBankDetails(hospitals.getIfscCode()); if(null !=
	 * masBank) {
	 * rodDTO.getDocumentDetails().setBankName(masBank.getBankName());
	 * rodDTO.getDocumentDetails().setCity(masBank.getCity());
	 * rodDTO.getDocumentDetails().setBranch(masBank.getBranchName()); }
	 * rodDTO.getDocumentDetails().setPanNo(hospitals.getPanNumber());
	 * rodDTO.getDocumentDetails().setPaymentModeFlag(
	 * ReferenceTable.PAYMENT_MODE_BANK_TRANSFER); } } } }
	 */

	private void populatePaymentDetailsForHosp(Long key,
			ReceiptOfDocumentsDTO rodDTO) {
		Hospitals hospitals = createRodService.getHospitalsDetails(key,
				masterService);
		if (null != hospitals) {

			String strHospitalPaymentType = hospitals.getPaymentType();
			String strIFscCode = hospitals.getIfscCode();
			String strAccntNo = hospitals.getAccountNo();
			if ((null != strIFscCode && !("").equals(strIFscCode)
					&& null != strAccntNo && !("").equalsIgnoreCase(strAccntNo))) {
				rodDTO.getDocumentDetails().setAccountNo(
						hospitals.getAccountNo());
				rodDTO.getDocumentDetails()
						.setIfscCode(hospitals.getIfscCode());
				BankMaster masBank = masterService.getBankDetails(hospitals
						.getIfscCode());
				if (null != masBank) {
					rodDTO.getDocumentDetails().setBankName(
							masBank.getBankName());
					rodDTO.getDocumentDetails().setCity(masBank.getCity());
					rodDTO.getDocumentDetails().setBranch(
							masBank.getBranchName());
					rodDTO.getDocumentDetails().setBankId(masBank.getKey());
				}
				rodDTO.getDocumentDetails().setPanNo(hospitals.getPanNumber());
				rodDTO.getDocumentDetails().setPaymentModeFlag(
						ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
			} else {
				rodDTO.getDocumentDetails().setPaymentModeFlag(
						ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
				rodDTO.getDocumentDetails().setPayableAt(
						hospitals.getPayableAt());
			}

			/*
			 * if (null != strHospitalPaymentType &&
			 * !("").equalsIgnoreCase(strHospitalPaymentType))
			 */
			/*
			 * { if ((ReferenceTable.CHEQUE_DD)
			 * .equalsIgnoreCase(strHospitalPaymentType)) { // In details page ,
			 * if its true, the cheque/DD will be // selected. Else bank
			 * transfer will selected. //
			 * rodDTO.getDocumentDetails().setPaymentMode(true);
			 * rodDTO.getDocumentDetails().setPaymentModeFlag(
			 * ReferenceTable.PAYMENT_MODE_CHEQUE_DD); } else if
			 * ((ReferenceTable.BANK_TRANSFER)
			 * .equalsIgnoreCase(strHospitalPaymentType)) { //
			 * rodDTO.getDocumentDetails().setPaymentMode(false);
			 * rodDTO.getDocumentDetails
			 * ().setAccountNo(hospitals.getAccountNo());
			 * rodDTO.getDocumentDetails().setIfscCode(hospitals.getIfscCode());
			 * BankMaster masBank =
			 * masterService.getBankDetails(hospitals.getIfscCode()); if(null !=
			 * masBank) {
			 * rodDTO.getDocumentDetails().setBankName(masBank.getBankName());
			 * rodDTO.getDocumentDetails().setCity(masBank.getCity());
			 * rodDTO.getDocumentDetails().setBranch(masBank.getBranchName()); }
			 * rodDTO.getDocumentDetails().setPanNo(hospitals.getPanNumber());
			 * rodDTO.getDocumentDetails().setPaymentModeFlag(
			 * ReferenceTable.PAYMENT_MODE_BANK_TRANSFER); } }
			 */
		}
	}

	private PreauthDTO getResiudalAmountToDTO(PreauthDTO mainDTO,
			Long transactionKey) {
		ResidualAmount residualAmount = reimbursementService
				.getResidulaAmount(transactionKey);
		ResidualAmountDTO dto = new ResidualAmountDTO();
		if (residualAmount != null) {
			dto.setKey(residualAmount.getKey());
			dto.setAmountConsideredAmount(residualAmount
					.getAmountConsideredAmount());

			dto.setApprovedAmount(residualAmount.getApprovedAmount());
			dto.setApprovedAmount(residualAmount.getApprovedAmount());
			dto.setNetApprovedAmount(residualAmount.getNetApprovedAmount());
			dto.setCopayAmount(residualAmount.getCopayAmount());
			dto.setCopayPercentage(residualAmount.getCopayPercentage());
			dto.setMinimumAmount(residualAmount.getMinimumAmount());
			dto.setNetAmount(residualAmount.getNetAmount());
			dto.setRemarks(residualAmount.getRemarks());
			dto.setPreauthKey(transactionKey);
			dto.setNetApprovedAmount(residualAmount.getNetApprovedAmount() != null ? residualAmount
					.getNetApprovedAmount() : 0d);
			if (mainDTO.getIsPostHospitalization()
					&& !mainDTO.getHospitalizaionFlag()) {
				dto.setAmountConsideredAmount(0d);
				dto.setNetAmount(0d);
				dto.setApprovedAmount(0d);
				dto.setNetApprovedAmount(0d);
			}

			/*
			 * This is for Reverse Allocation . if any modifictation between
			 * Approved amount or Net Approved Amount then we have to set
			 * reverse allocation into PreauthDTO.
			 */

			if (!dto.getApprovedAmount().equals(dto.getNetApprovedAmount())) {
				mainDTO.setIsReverseAllocation(true);
			}
			
			if(null != mainDTO.getNewIntimationDTO().getIsJioPolicy() && mainDTO.getNewIntimationDTO().getIsJioPolicy()){
				if(null != mainDTO.getResidualAmountDTO().getCoPayTypeId()){	
					
					SelectValue copayValue = new SelectValue();
					copayValue.setValue(residualAmount.getCoPayTypeId().getValue());
					copayValue.setId(residualAmount.getCoPayTypeId().getKey());
					dto.setCoPayTypeId(copayValue);
				}
			}
		}
		mainDTO.setResidualAmountDTO(dto);
		return mainDTO;
	}

	private void generateRODNumber(ReceiptOfDocumentsDTO rodDTO) {
		Long claimKey = rodDTO.getClaimDTO().getKey();
		/*Long count = createRodService
				.getACknowledgeNumberCountByClaimKey(claimKey);*/
		Long lackCount = 001l;
		 Reimbursement latestReimbursementByRodNumberwise = createRodService
			.getLatestReimbursementByRodNumberwise(claimKey);
		 if(latestReimbursementByRodNumberwise != null){
			 String[] split = latestReimbursementByRodNumberwise.getRodNumber().split("/");
				String defaultNumber = split[split.length - 1];
				Long nextReferenceNo = Long.valueOf(defaultNumber);
				lackCount = nextReferenceNo + 001l;
		 }else{
			 lackCount = 001l;
		 }
		 
		StringBuffer ackNoBuf = new StringBuffer();
		ackNoBuf.append("ROD/")
				.append(rodDTO.getClaimDTO().getNewIntimationDto()
						.getIntimationId()).append("/").append(lackCount);
		rodDTO.getDocumentDetails().setRodNumber(ackNoBuf.toString());
	}

	private void getPreviousRODNumber(ReceiptOfDocumentsDTO rodDTO) {
		Reimbursement reimbursement = ackDocReceivedService
				.getLatestReimbursementDetails(rodDTO.getClaimDTO().getKey());
		if (null != reimbursement) {
			rodDTO.setRodNumberForUploadTbl(reimbursement.getRodNumber());
		}
	}

	private List<RODQueryDetailsDTO> getRODQueryDetailsForCreateRodandBillEntry(
			Claim claim, Long ackKey) {
		List<RODQueryDetailsDTO> rodQueryDetails = ackDocReceivedService
				.getRODQueryDetailsForCreateRodAndBillEntry(claim, ackKey);
		return rodQueryDetails;
	}

	private List<RODQueryDetailsDTO> getPARODQueryDetailsForCreateRodandBillEntry(
			Claim claim, Long ackKey) {
		List<RODQueryDetailsDTO> rodQueryDetails = ackDocReceivedService
				.getPARODQueryDetailsForCreateRodAndBillEntry(claim, ackKey);
		return rodQueryDetails;
	}
	
	private List<RODQueryDetailsDTO> getRODPaymentQueryDetailsForCreateRodandBillEntry(
			Claim claim, Long ackKey) {
		List<RODQueryDetailsDTO> rodQueryDetails = ackDocReceivedService
				.getRODPaymentQueryDetailsForCreateRodAndBillEntry(claim, ackKey);
		return rodQueryDetails;
	}
	public void setpreauthTOPreauthDTO(PreMedicalMapper premedicalMapper,
			Claim claimByKey, Preauth previousPreauth, PreauthDTO preauthDTO,
			Boolean isEnabled) {
		if (claimByKey != null) {
			setClaimValuesToDTO(preauthDTO, claimByKey);
			NewIntimationDto newIntimationDto = intimationService
					.getIntimationDto(claimByKey.getIntimation());
			
			newIntimationDto.setInsuredDeceasedFlag(dbCalculationService.getInsuredPatientStatus(newIntimationDto.getPolicy().getKey(), newIntimationDto.getInsuredPatient().getKey()));
			
			ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(
					claimByKey);
			preauthDTO.setNewIntimationDTO(newIntimationDto);
			preauthDTO.setClaimDTO(claimDTO);
			preauthDTO.setClaimDTO(claimDTO);
			
			 if (claimByKey.getIntimation().getPolicy().getHomeOfficeCode() != null) {
				 List<MasOmbudsman> ombudsmanOfficeList = getOmbudsmanOffiAddrByPIOCode(claimByKey.getIntimation().getPolicy().getHomeOfficeCode());
				 if(ombudsmanOfficeList !=null && !ombudsmanOfficeList.isEmpty())
					 preauthDTO.getClaimDTO().setOmbudsManAddressList(ombudsmanOfficeList);
			 }
		}
		/*String policyNumber = preauthDTO.getPolicyDto().getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		// List<ViewTmpClaim> claimsByPolicyNumber = claimService
		// .getViewTmpClaimsByPolicyNumber(policyNumber);
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		List<ViewTmpIntimation> intimationKeys = intimationService
				.getIntimationByPolicyKey(byPolicyNumber.getKey());
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByIntimationKeys(intimationKeys);
		previousclaimsList.addAll(claimsByPolicyNumber);

		previousclaimsList = getPreviousClaimForPreviousPolicy(
				byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);*/

		// List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
		// .getPreviousClaims(claimsByPolicyNumber,
		// claimByKey.getClaimId(), pedValidationService,
		// masterService);

		/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList, claimByKey.getClaimId());*/
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(claimByKey.getKey(), claimByKey.getIntimation().getPolicy().getKey(), 
				claimByKey.getIntimation().getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

		// List<PreviousClaimsTableDTO> previousClaimDTOList = new
		// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

		preauthDTO.setPreviousClaimsList(previousClaimDTOList);

		if(previousPreauth.getCatastrophicLoss() != null) {
			
			SelectValue value = masterService.getCatastropheData(previousPreauth.getCatastrophicLoss());
			preauthDTO.getPreauthDataExtractionDetails().setCatastrophicLoss(value);
		}

		if(previousPreauth.getNatureOfLoss() != null) {
			
			SelectValue value = masterService.getMasterValueForNatureCause(previousPreauth.getNatureOfLoss());
			preauthDTO.getPreauthDataExtractionDetails().setNatureOfLoss(value);
		}
		if(previousPreauth.getCauseOfLoss() != null) {
			SelectValue value = masterService.getMasterValueForNatureCause(previousPreauth.getCauseOfLoss());
			preauthDTO.getPreauthDataExtractionDetails().setCauseOfLoss(value);
		}
		if (previousPreauth.getCoordinatorFlag().equalsIgnoreCase("y")) {

			CoordinatorDTO coordinatorDTO = premedicalMapper
					.getCoordinatorDTO(preauthService
							.findCoordinatorByClaimKey(previousPreauth
									.getClaim().getKey()));
			coordinatorDTO.setRefertoCoordinator(true);
			preauthDTO.setCoordinatorDetails(coordinatorDTO);
		}

		List<SpecialityDTO> specialityDTOList = premedicalMapper
				.getSpecialityDTOList(preauthService
						.findSpecialityByClaimKey(previousPreauth.getClaim()
								.getKey()));
		for (SpecialityDTO specialityDTO : specialityDTOList) {
			specialityDTO.setEnableOrDisable(isEnabled);
		}
		preauthDTO.getPreauthDataExtractionDetails().setSpecialityList(
				specialityDTOList);

		List<PedValidation> findPedValidationByPreauthKey = preauthService
				.findPedValidationByPreauthKey(previousPreauth.getKey());
		List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = premedicalMapper
				.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

		// Fix for issue 732 starts.
		//DBCalculationService dbCalculationService = new DBCalculationService();
		Double insuredSumInsured = 0d;
		
		if(null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			
			insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					preauthDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), preauthDTO.getPolicyDto()
							.getKey());
		}

		preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);

		/*Double balanceSI = dbCalculationService.getBalanceSI(
				preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
				preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				preauthDTO.getClaimKey(), insuredSumInsured,
				preauthDTO.getNewIntimationDTO().getKey()).get(
				SHAConstants.TOTAL_BALANCE_SI);
		*/
		Double balanceSI= dbCalculationService.getBalanceSIForPAHealth(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),previousPreauth.getClaim()
				.getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(),ReferenceTable.HOSPITALIZATION_BENEFITS).get(SHAConstants.TOTAL_BALANCE_SI);
		List<Double> copayValue = dbCalculationService.getProductCoPay(
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey(), preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getKey(), preauthDTO
						.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId(),preauthDTO.getNewIntimationDTO());

		if (preauthDTO.getNewIntimationDTO() != null) {
			Product product = preauthDTO.getNewIntimationDTO().getPolicy()
					.getProduct();
			if (product.getCode() != null
					&& ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE
							.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE
							.equalsIgnoreCase(product.getCode())))) {
				// BalanceSumInsuredDTO claimsOutstandingAmt =
				// dbCalculationService.getClaimsOutstandingAmt(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				// preauthDTO.getNewIntimationDTO().getIntimationId(),
				// insuredSumInsured);
				preauthDTO.setSettledAmount(0d);
				preauthDTO
						.setDeductibleAmount((preauthDTO.getNewIntimationDTO()
								.getInsuredPatient() != null && preauthDTO
								.getNewIntimationDTO().getInsuredPatient()
								.getDeductibleAmount() != null) ? preauthDTO
								.getNewIntimationDTO().getInsuredPatient()
								.getDeductibleAmount() : 0d);
			}
		}

		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);

		if (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()
				.equals(ReferenceTable.SUPER_SURPLUS_INDIVIDUAL)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey().equals(ReferenceTable.SUPER_SURPLUS_FLOATER)) {
			// preauthDTO.setSpecificProductDeductibleDTO(getPreviousClaimsSuperSurplusTable(claimsByPolicyNumber
			// ,claimByKey.getClaimId(), preauthDTO));
		}

		Map<Long, SublimitFunObject> sublimitFunObjMap = getSublimitFunObjMap(
				preauthDTO.getPolicyDto().getProduct().getKey(),
				insuredSumInsured, preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredAge(), preauthDTO
						.getPreauthDataExtractionDetails().getSection(),
				preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		// Fix for issue 732 ends

		List<ProcedureDTO> procedureMainDTOList = premedicalMapper
				.getProcedureMainDTOList(preauthService
						.findProcedureByPreauthKey(previousPreauth.getKey()));
		for (ProcedureDTO procedureDTO : procedureMainDTOList) {
			procedureDTO.setEnableOrDisable(isEnabled);
			if (procedureDTO.getSublimitName() != null) {
				SublimitFunObject objSublimitFun = sublimitFunObjMap
						.get(procedureDTO.getSublimitName().getLimitId());
				if (objSublimitFun != null) {
					procedureDTO.setSublimitName(objSublimitFun);
					procedureDTO.setSublimitDesc(objSublimitFun
							.getDescription());
					procedureDTO.setSublimitAmount(String
							.valueOf(objSublimitFun.getAmount().intValue()));
				}

			}
		}

		preauthDTO.getPreauthMedicalProcessingDetails()
				.setProcedureExclusionCheckTableList(procedureMainDTOList);

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : newPedValidationTableListDto) {
			pedValidationTableDTO.setEnableOrDisable(isEnabled);
			if (pedValidationTableDTO.getDiagnosisName() != null) {
				String diagnosis = masterService
						.getDiagnosis(pedValidationTableDTO.getDiagnosisName()
								.getId());
				pedValidationTableDTO.setDiagnosis(diagnosis);
				pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
			}
			
			/*Below Code added for R20181279*/
			if(pedValidationTableDTO.getIcdCode() != null){
				SelectValue icdValues = masterService.getIcdDescriptionKey(pedValidationTableDTO.getIcdCode().getId());
				pedValidationTableDTO.getIcdCode().setValue(icdValues.getValue());
				pedValidationTableDTO.getIcdCode().setCommonValue(icdValues.getCommonValue());
				
			}
			
			if (pedValidationTableDTO.getSublimitName() != null) {
				// Fix for issue 732 starts.
				SublimitFunObject objSublimitFun = sublimitFunObjMap
						.get(pedValidationTableDTO.getSublimitName()
								.getLimitId());
				// ClaimLimit limit =
				// claimService.getClaimLimitByKey(pedValidationTableDTO.getSublimitName().getLimitId());
				if (objSublimitFun != null) {
					pedValidationTableDTO.setSublimitName(objSublimitFun);
					pedValidationTableDTO.setSublimitAmt(String
							.valueOf(objSublimitFun.getAmount()));
				}
				// Fix for issue 732 ends
			}

			if (pedValidationTableDTO.getSumInsuredRestriction() != null) {
				MastersValue master = masterService
						.getMaster(pedValidationTableDTO
								.getSumInsuredRestriction().getId());
				pedValidationTableDTO.getSumInsuredRestriction().setValue(
						master.getValue());

			}
			List<DiagnosisPED> pedDiagnosisByPEDValidationKey = preauthService
					.getPEDDiagnosisByPEDValidationKey(pedValidationTableDTO
							.getKey());
			List<PedDetailsTableDTO> dtoList = new ArrayList<PedDetailsTableDTO>();
			for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidationKey) {
				PedDetailsTableDTO dto = new PedDetailsTableDTO();
				// Added for disabling the procedure that is coming from
				// preauth.
				dto.setEnableOrDisable(isEnabled);
				dto.setDiagnosisName(pedValidationTableDTO.getDiagnosis());
				dto.setPolicyAgeing(pedValidationTableDTO.getPolicyAgeing());
				dto.setKey(diagnosisPED.getKey());
				dto.setPedCode(diagnosisPED.getPedCode());
				dto.setPedName(diagnosisPED.getPedName());

				if (diagnosisPED.getDiagonsisImpact() != null) {
					SelectValue value = new SelectValue();
					value.setId(diagnosisPED.getDiagonsisImpact().getKey());
					value.setValue(diagnosisPED.getDiagonsisImpact().getValue());
					dto.setPedExclusionImpactOnDiagnosis(value);
				}

				if (diagnosisPED.getExclusionDetails() != null) {
					SelectValue exclusionValue = new SelectValue();
					exclusionValue.setId(diagnosisPED.getExclusionDetails()
							.getKey());
					exclusionValue.setValue(diagnosisPED.getExclusionDetails()
							.getExclusion());
					dto.setExclusionDetails(exclusionValue);
				}

				dto.setRemarks(diagnosisPED.getDiagnosisRemarks());
				dtoList.add(dto);
			}
			pedValidationTableDTO.setPedList(dtoList);
		}

		// TODO: Need to change this behaviour..
		preauthDTO.getPreauthDataExtractionDetails().setDiagnosisTableList(
				newPedValidationTableListDto);

		// preauthDTO.getPreauthMedicalProcessingDetails().setPedValidationTableList(newPedValidationTableListDto);

		List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey = preauthService
				.findClaimAmountDetailsByPreauthKey(previousPreauth.getKey());
		preauthDTO
				.getPreauthDataExtractionDetails()
				.setClaimedDetailsList(
						premedicalMapper
								.getClaimedAmountDetailsDTOList(findClaimAmountDetailsByPreauthKey));

		Map<String, Object> autoRestroation = dbCalculationService.getAutoRestroation(preauthDTO.getNewIntimationDTO().getIntimationId());
		if(autoRestroation != null){
			
			if(autoRestroation.containsKey(SHAConstants.AUTO_RESTORATION_CHK) && autoRestroation.get(SHAConstants.AUTO_RESTORATION_CHK) != null){
				preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(String.valueOf(autoRestroation.get(SHAConstants.AUTO_RESTORATION_CHK)));
				if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
					preauthDTO.setIsAutoRestorationDone(true);
				}
			}
			
			if(autoRestroation.containsKey(SHAConstants.AUTO_RESTORATION_COUNT) && autoRestroation.get(SHAConstants.AUTO_RESTORATION_COUNT) != null){
				preauthDTO.getPreauthDataExtractionDetails().setRestorationCount((Integer.valueOf(autoRestroation.get(SHAConstants.AUTO_RESTORATION_COUNT).toString())));
			}
		}
		
		
		if(null != preauthDTO.getNewIntimationDTO().getLobId().getId() && (((ReferenceTable.PA_LOB_KEY).equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))
				|| ((ReferenceTable.PACKAGE_MASTER_VALUE).equals(preauthDTO.getNewIntimationDTO().getLobId().getId()))))
		{
			preauthDTO.setIsAutoRestorationDone(false);
			preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE);
		}
	}

	private List<MasOmbudsman> getOmbudsmanOffiAddrByPIOCode(String pioCode) {
		
		List<MasOmbudsman> ombudsmanDetailsByCpuCode = new ArrayList<MasOmbudsman>();
		if(pioCode != null){
			OrganaizationUnit branchOffice = claimService
					.getInsuredOfficeNameByDivisionCode(pioCode);
			if (branchOffice != null) {
				String ombudsManCode = branchOffice.getOmbudsmanCode();
				if (ombudsManCode != null) {
					ombudsmanDetailsByCpuCode = masterService
							.getOmbudsmanDetailsByCpuCode(ombudsManCode);
				}
			}
		}
		return ombudsmanDetailsByCpuCode;
	}
	
	private List<DocumentCheckListDTO> getCheckListValidationTableValues(
			Long docAckKey, DocAcknowledgement docAcknowledgement) {
		// Fetch values from MAS_DOCUMENT_TYPE table.
		List<DocumentCheckListDTO> masterCheckListValues = setPADocumentCheckListTableValues();

		// Fetch values from IMS_CLS_ROD_DOCUMENT_LIST table for given document
		// acknowledge object.
		List<DocumentCheckListDTO> rodCheckListValues = createRodService
				.getRODDocumentList(masterService, docAcknowledgement);

		// Final list to combine MAS_DOCUMENT_TYPE and IMS_CLS_ROD_DOCUMENT_LIST
		// table values.
		List<DocumentCheckListDTO> finalCheckListValues = null;

		/**
		 * Null check is performed only for ROD checklist table. This is because
		 * mastercheckList values will not be null as it is a master data.
		 * */
		if (null != rodCheckListValues && !rodCheckListValues.isEmpty()) {
			finalCheckListValues = new ArrayList<DocumentCheckListDTO>();

			for (DocumentCheckListDTO documentCheckListDTO2 : rodCheckListValues) {
				// if (key.equals(documentCheckListDTO2.getDocTypeId())) {
				if (null != documentCheckListDTO2.getRodReceivedStatusFlag()) {
					if (("Y").equalsIgnoreCase(documentCheckListDTO2
							.getRodReceivedStatusFlag())) {
						documentCheckListDTO2.setRodReceivedStatus(true);
					} else {
						documentCheckListDTO2.setRodReceivedStatus(false);
					}
					if (null != documentCheckListDTO2.getRodRemarks()) {
						documentCheckListDTO2
								.setRodRemarks(documentCheckListDTO2
										.getRodRemarks());
					}
					// documentCheckListDTO.setRodReceivedStatusFlag(documentCheckListDTO2.getRodReceivedStatusFlag());
				}
				// break;
				// }
			}
			// finalCheckListValues.add(documentCheckListDTO2);

			/*
			 * for (DocumentCheckListDTO documentCheckListDTO :
			 * masterCheckListValues) {
			 * 
			 * Long key = documentCheckListDTO.getKey(); for
			 * (DocumentCheckListDTO documentCheckListDTO2 : rodCheckListValues)
			 * { if (key.equals(documentCheckListDTO2.getDocTypeId())) {
			 * documentCheckListDTO .setAckReceivedStatus(documentCheckListDTO2
			 * .getAckReceivedStatus()); documentCheckListDTO
			 * .setNoOfDocuments(documentCheckListDTO2 .getNoOfDocuments());
			 * documentCheckListDTO.setRemarks(documentCheckListDTO2
			 * .getRemarks()); documentCheckListDTO
			 * .setReceivedStatus(documentCheckListDTO2 .getReceivedStatus());
			 * documentCheckListDTO.setKey(documentCheckListDTO2 .getKey());
			 * documentCheckListDTO.setDocTypeId(documentCheckListDTO2
			 * .getDocTypeId()); if (null != documentCheckListDTO2
			 * .getRodReceivedStatusFlag()) { if
			 * (("Y").equalsIgnoreCase(documentCheckListDTO2
			 * .getRodReceivedStatusFlag())) {
			 * documentCheckListDTO.setRodReceivedStatus(true); } else {
			 * documentCheckListDTO .setRodReceivedStatus(false); } if (null !=
			 * documentCheckListDTO2.getRodRemarks()) { documentCheckListDTO
			 * .setRodRemarks(documentCheckListDTO2 .getRodRemarks()); } //
			 * documentCheckListDTO
			 * .setRodReceivedStatusFlag(documentCheckListDTO2
			 * .getRodReceivedStatusFlag()); } break; } }
			 * finalCheckListValues.add(documentCheckListDTO); }
			 */
			// deferencing the values since this list is no more required. These
			// unused list will be immediately collected by garbage collector
			// and memory will be free.
			/*
			 * masterCheckListValues = null; rodCheckListValues = null;
			 */
			// return finalCheckListValues;
			return rodCheckListValues;
		} else {
			return masterCheckListValues;
		}
	}

	private Map<Long, SublimitFunObject> getSublimitFunObjMap(Long productKey,
			Double insuredSumInsured, Double insuredAge, SelectValue section,
			String plan,Long insuredKey) {
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<Long, SublimitFunObject> sublimitFunMap = new HashMap<Long, SublimitFunObject>();
		/**
		 * There was a conflict in the below method due to which build was
		 * delayed. Hence to resolve it for timebeing the empty string value was
		 * added in getClaimedAmountDetailsSection. Saravanan needs to either
		 * validate or act on it.
		 * */
		List<SublimitFunObject> sublimitList = dbCalculationService
				.getClaimedAmountDetailsForSection(productKey,
						insuredSumInsured, 0l, insuredAge,
						section != null ? section.getId() : 0l, plan, "",insuredKey);
		if (null != sublimitList && !sublimitList.isEmpty()) {
			for (SublimitFunObject sublimitFunObj : sublimitList) {
				sublimitFunMap.put(sublimitFunObj.getLimitId(), sublimitFunObj);
			}
		}
		return sublimitFunMap;

	}

	protected void showPAMedicalApprovalProcessClaimRequest(
			@Observes @CDIEvent(PAMenuPresenter.PA_SHOW_MEDICAL_APPROVAL_SCREEN) final ParameterDTO parameters) {
		Date startDate = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> SHOW MEDICAL APPROVAL CLAIM REQUEST METHOD STARTED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ startDate);

		SearchProcessClaimRequestTableDTO tableDTO = (SearchProcessClaimRequestTableDTO) parameters
				.getPrimaryParameter();

		Reimbursement reimbursementObjectByKey = rodService
				.getReimbursementObjectByKey(tableDTO.getRodKey());
		

		Date date1 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> BEFORE INVOKING ROD SUMMARY DETAILS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date1);

		List<UploadDocumentDTO> rodSummaryDetails = rodService
				.getRODSummaryDetails(reimbursementObjectByKey.getKey());
		Date date2 = new Date();

		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> AFTER INVOKING ROD SUMMARY DETAILS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date2);

		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL DURATION FOR RODSUMMARY DETAILS METHOD %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date1, date2));

		Boolean isValidClaimForMA = true;
		Boolean isValidClaimForZonal = true;
		Claim claimByKeyForLegal = reimbursementObjectByKey.getClaim();
		
		if(null != claimByKeyForLegal)
		{
			if((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKeyForLegal.getLegalFlag()))
			{
				isValidClaimForZonal = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
		}
		
		
		if(isValidClaimForZonal)
		{

		/*
		 * List<Long> documentSummaryKeys = new ArrayList<Long>(); for
		 * (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
		 * documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
		 * uploadDocumentDTO.setStatus(true); List<RODBillDetails>
		 * billEntryDetails = rodService
		 * .getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
		 * List<BillEntryDetailsDTO> dtoList = new
		 * ArrayList<BillEntryDetailsDTO>(); if (billEntryDetails != null &&
		 * !billEntryDetails.isEmpty()) { for (RODBillDetails billEntryDetailsDO
		 * : billEntryDetails) {
		 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
		 * uploadDocumentDTO)); } }
		 * uploadDocumentDTO.setBillEntryDetailList(dtoList); }
		 */
		/*
		 * Double totalBilledAmount = reimbursementService
		 * .getTotalBilledAmount(documentSummaryKeys);
		 */
		Date date3 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> BEFORE INVOKING ZONAL  MEDICAL REVIEW MAPPER  GET REIMBURSEMENT DTO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date3);
		ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper
				.getInstance();
		// ZonalMedicalReviewMapper.getAllMapValues();
		PreauthDTO reimbursementDTO = mapper
				.getReimbursementDTO(reimbursementObjectByKey);
		if(null != reimbursementObjectByKey && ("Y").equalsIgnoreCase(reimbursementObjectByKey.getPedFlag()))
		{
			reimbursementDTO.getPreauthDataExtractionDetails().setPreExistingDisabilities(true);
		}
		else
		{
			reimbursementDTO.getPreauthDataExtractionDetails().setPreExistingDisabilities(false);
		}
		reimbursementDTO.getPreauthDataExtractionDetails().setDisabilitesRemarks(reimbursementObjectByKey.getPedDisablitiyDetails());
		if(null != reimbursementObjectByKey.getAccidentCauseId())
		{
			SelectValue selValue = new SelectValue();
			selValue.setId(reimbursementObjectByKey.getAccidentCauseId().getKey());
			selValue.setValue(reimbursementObjectByKey.getAccidentCauseId().getValue());
			reimbursementDTO.getPreauthDataExtractionDetails().setCauseOfAccident(selValue);
		}
		UpdateHospital hospDetails = pareimbursementService.getHospitalDetails(reimbursementObjectByKey.getKey());
		if(null != hospDetails && null != hospDetails.getInpatientBeds())
		{
				reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails().setInpatientBeds(String.valueOf(hospDetails.getInpatientBeds()));
		}
		Date date4 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> AFTER INVOKING ZONAL  MEDICAL REVIEW MAPPER  GET REIMBURSEMENT DTO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date4);
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL DURATION FOR GET REIMBURSEMENT DTO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date3, date4));

		if (null != reimbursementObjectByKey) {
			/*
			 * Claim claimByKey = (reimbursementObjectByKey.getClaim()); if(null
			 * != claimByKey) {
			 * if((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
			 * .getLegalFlag())) { isValidClaimForMA = false;
			 * view.showErrorPopUp
			 * ("Intimation is locked by legal process. Cannot proceed further"
			 * ); } }
			 */
		}
		/*
		 * if(isValidClaimForMA) {
		 */

		if (("Y").equalsIgnoreCase(reimbursementDTO
				.getPreauthDataExtractionDetails()
				.getReconsiderationFlag())) {
			reimbursementDTO
					.setReconsiderRodRequestList(getReconsiderRODRequestForBillEntry(tableDTO
							.getRodKey()));
		}

		/*
		 * reimbursementDTO.setAmountConsidered(totalBilledAmount != null ?
		 * String .valueOf(totalBilledAmount.intValue()) : "0");
		 * reimbursementDTO .setInitialAmountConsidered(totalBilledAmount !=
		 * null ? String .valueOf(totalBilledAmount.intValue()) : "0");
		 * reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		 * reimbursementDTO.setHumanTask(tableDTO.getHumanTask());
		 */

		Date date5 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> BEFORE INVOKING FVR GRADING SERVICE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date5);

		List<FVRGradingMaster> fvrGrading = reimbursementService
				.getFVRGrading();
		Date date6 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> AFTER INVOKING FVR GRADING SERVICE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date6);

		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> BEFORE INVOKING FVR GRADING SERVICE BY FVR CLAIM KEY %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date6);

		List<FieldVisitRequest> fvrByClaimKey = reimbursementService
				.getFVRByClaimKey(reimbursementObjectByKey.getClaim().getKey());

		Date date7 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> AFTER INVOKING FVR GRADING SERVICE BY FVR CLAIM KEY %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date7);

		List<FvrGradingDetailsDTO> dto = new ArrayList<FvrGradingDetailsDTO>();
		Map<Integer, FieldVisitRequest> valueMap = new HashMap<Integer, FieldVisitRequest>();
		for (int i = 0; i < fvrByClaimKey.size(); i++) {
			FieldVisitRequest fieldVisitRequest = fvrByClaimKey.get(i);
			if (fieldVisitRequest.getStatus() != null
					&& fieldVisitRequest.getStatus().getKey()
							.equals(ReferenceTable.FVR_REPLY_RECEIVED)) {
				FvrGradingDetailsDTO fvrDto = new FvrGradingDetailsDTO();
				fvrDto.setKey(fieldVisitRequest.getKey());
				fvrDto.setRepresentativeName(fieldVisitRequest
						.getRepresentativeName());
				fvrDto.setRepresentiveCode(fieldVisitRequest
						.getRepresentativeCode());
				List<FVRGradingDTO> FVRTableDTO = new ArrayList<FVRGradingDTO>();
				for (FVRGradingMaster masterFVR : fvrGrading) {
					FVRGradingDTO eachFVRDTO = new FVRGradingDTO();
					eachFVRDTO.setKey(masterFVR.getKey());
					eachFVRDTO.setCategory(masterFVR.getGradingType());
					eachFVRDTO.setApplicability(masterFVR.getApplicability());
					switch (Integer.valueOf(String.valueOf(masterFVR.getKey()))) {
					case 8:
						if (fieldVisitRequest.getPatientVerified() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPatientVerified());
						}
						break;
					case 9:
						if (fieldVisitRequest.getDiagnosisVerfied() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getDiagnosisVerfied());
						}
						break;
					case 10:
						if (fieldVisitRequest.getRoomCategoryVerfied() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getRoomCategoryVerfied());
						}
						break;
					case 11:
						if (fieldVisitRequest.getTriggerPointsFocused() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getTriggerPointsFocused());
						}
						break;
					case 12:
						if (fieldVisitRequest.getPedVerified() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPedVerified());
						}
						break;
					case 13:
						if (fieldVisitRequest.getPatientDischarged() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPatientDischarged());
						}
						break;
					case 14:
						if (fieldVisitRequest.getPatientNotAdmitted() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPatientNotAdmitted());
						}
						break;
					case 15:
						if (fieldVisitRequest.getOutstandingFvr() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getOutstandingFvr());
						}
						break;
					default:
						break;
					}
					FVRTableDTO.add(eachFVRDTO);
				}
				fvrDto.setFvrGradingDTO(FVRTableDTO);
				dto.add(fvrDto);
			}
		}
		// added for parralel processing
		reimbursementDTO.getPreauthMedicalDecisionDetails().setIsFvrIntiated(preauthService.getFVRStatusIdByClaimKey(reimbursementDTO.getClaimKey()));
		reimbursementDTO.getPreauthMedicalDecisionDetails().setIsFvrIntiatedMA(preauthService.getFVRStatusIdMAByClaimKey(reimbursementDTO.getClaimKey()));
		reimbursementDTO.getPreauthMedicalDecisionDetails().setIsFvrReplyReceived(preauthService.isFVRReplyReceived(reimbursementDTO.getClaimKey()));
		
		reimbursementDTO.getPreauthMedicalDecisionDetails().setFvrGradingDTO(
				dto);

	//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());
		reimbursementDTO.setStrUserName(tableDTO.getUsername());
		reimbursementDTO.setStrPassword(tableDTO.getPassword());
		reimbursementDTO.setTaskNumber(tableDTO.getTaskNumber());

		reimbursementDTO.setUserRole(tableDTO.getUserRole());
		reimbursementDTO.setRodNumber(reimbursementObjectByKey.getRodNumber());
		Claim claimByKey = reimbursementObjectByKey.getClaim();
		Date date8 = new Date();
		reimbursementDTO
				.setReconsiderationList(getReconsiderRODRequest(claimByKey));
		Date date9 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY GET RECONSIDER ROD REQUEST %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date8, date9));

		BeanItemContainer<SelectValue> medicalContainer = masterService
				.getSelectValueContainer(ReferenceTable.MEDICAL_VERIFICATION);
		BeanItemContainer<SelectValue> treatmentContainer = masterService
				.getSelectValueContainer(ReferenceTable.TREATMENT_QUALITY_VERIFICATION);

		// setHospitalDetailsToDTO(hospitalById, reimbursementDTO);
		Date date10 = new Date();
		setReimbursmentTOPreauthDTO(mapper, claimByKey,
				reimbursementObjectByKey, reimbursementDTO, true,
				SHAConstants.CLAIM_REQUEST);
		Date date11 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY SET REIMBURSEMENT TO PREAUTH DTO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date10, date11));

		String[] hospSplit = reimbursementObjectByKey.getRodNumber().split("/");
		String hsopSplitNo = hospSplit[hospSplit.length - 1];
		Integer hospNo = Integer.valueOf(hsopSplitNo);
		if (reimbursementObjectByKey.getStatus() != null
				&& !reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)
				&& !reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)
				&& hospNo.equals(1)
				&& reimbursementObjectByKey.getClaim().getClaimType() != null
				&& reimbursementObjectByKey.getClaim().getClaimType().getKey()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			Preauth latestPreauthByClaim = preauthService
					.getLatestPreauthByClaim(reimbursementObjectByKey
							.getClaim().getKey());
			reimbursementDTO.setPreviousPreauthKey(latestPreauthByClaim
					.getKey());
			List<PedValidation> findPedValidationByPreauthKey = preauthService
					.findPedValidationByPreauthKey(latestPreauthByClaim
							.getKey());
			List<Procedure> findProcedureByPreauthKey = preauthService
					.findProcedureByPreauthKey(latestPreauthByClaim.getKey());
			List<DiagnosisDetailsTableDTO> diagnosisTableList = reimbursementDTO
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			List<ProcedureDTO> procedureExclusionCheckTableList = reimbursementDTO
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				for (Procedure procedure : findProcedureByPreauthKey) {
					if (procedure.getProcedureID().equals(
							procedureDTO.getProcedureName().getId())) {
						procedureDTO.setOldApprovedAmount(procedure
								.getNetApprovedAmount());
					}
				}
			}

			for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisTableList) {
				for (PedValidation pedValidation : findPedValidationByPreauthKey) {
					if (pedValidation.getDiagnosisId().equals(
							diagnosisDTO.getDiagnosisId())) {
						diagnosisDTO.setOldApprovedAmount(pedValidation
								.getNetApprovedAmount());
					}
				}
			}
		}

		DBCalculationService dbCalculationService = new DBCalculationService();
		Date date12 = new Date();
		Double insuredSumInsured = 0d;
		
		if(null != claimByKey &&  null != claimByKey.getIntimation() && 
				null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
				null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
				&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
		{
		
			insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), reimbursementDTO
						.getPolicyDto().getKey(),reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{

			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
				reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), reimbursementDTO
						.getPolicyDto().getKey());
		}
		Date date13 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY INSURED SUM INSURED PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date12, date13));

		Map<Integer, Object> detailsMap = new HashMap<Integer, Object>();

		// if(null != reimbursementDTO && null !=
		// reimbursementDTO.getNewIntimationDTO() && null !=
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto()
		// && null !=
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals()
		// && null !=
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType()
		// &&
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType().getKey().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID))
		// {
		// String cityClass =
		// createRodService.getHospitalCityClass(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto()
		// .getRegistedHospitals().setCityClass(cityClass);
		//
		// }

		if (reimbursementObjectByKey.getSectionCategory() != null) {

			Date date14 = new Date();
			detailsMap = dbCalculationService.getHospitalizationDetails(
					reimbursementDTO.getPolicyDto().getProduct().getKey(),
					insuredSumInsured, reimbursementDTO.getNewIntimationDTO()
							.getHospitalDto().getRegistedHospitals()
							.getCityClass(), reimbursementDTO
							.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId(), reimbursementDTO
							.getNewIntimationDTO().getKey(),
					reimbursementObjectByKey.getSectionCategory(), "A");
			Date date15 = new Date();
			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY HOSPITALIZATION DETAILS PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date14, date15));

		} else {

			Long sectionCategory = 0l;
			if (reimbursementDTO.getPolicyDto().getProduct().getKey()
					.equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY)
					|| reimbursementDTO.getPolicyDto().getProduct().getKey()
							.equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY)
					|| reimbursementDTO.getPolicyDto().getProduct().getKey()
							.equals(ReferenceTable.DIABETES_FLOATER_POLICY)) {
				sectionCategory = 1l;
			}

			Date date14 = new Date();
			detailsMap = dbCalculationService.getHospitalizationDetails(
					reimbursementDTO.getPolicyDto().getProduct().getKey(),
					insuredSumInsured, reimbursementDTO.getNewIntimationDTO()
							.getHospitalDto().getRegistedHospitals()
							.getCityClass(), reimbursementDTO
							.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId(), reimbursementDTO
							.getNewIntimationDTO().getKey(), sectionCategory,
					"0");
			Date date15 = new Date();

			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY HOSPITALIZATION DETAILS PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date14, date15));

		}

		if (detailsMap != null && detailsMap.get(15) != null) {
			// reimbursementDTO.setAmbulanceLimitAmount((Double)detailsMap.get(15));
		}

		reimbursementDTO = getProrataFlagFromProduct(reimbursementDTO);

		if (reimbursementObjectByKey.getProrataDeductionFlag() != null) {
			reimbursementDTO.setProrataDeductionFlag(reimbursementObjectByKey
					.getProrataDeductionFlag());
		}

		/**
		 * product based variable is added to enable or disable the component in
		 * page level. This would be static. -- starts
		 * */
		// reimbursementDTO.setProductBasedProRata(reimbursementObjectByKey.getProrataDeductionFlag());
		// reimbursementDTO.setProductBasedPackage(reimbursementObjectByKey.getPackageAvailableFlag());
		// ends.
		reimbursementDTO.setPackageAvailableFlag(reimbursementObjectByKey
				.getPackageAvailableFlag());

		List<Long> documentSummaryKeys = new ArrayList<Long>();

		/**
		 * Fix for implemeting claims dms in bill entry screen.. -- starts
		 * **/
		if (null != rodSummaryDetails && !rodSummaryDetails.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				if (null != claimByKey) {
					uploadDocumentDTO.setClaimNo(claimByKey.getClaimId());
					uploadDocumentDTO
							.setDmsDocumentDTOList(getClaimsDMSList(claimByKey
									.getIntimation().getIntimationId()));
				}
			}
		}

		/**
		 * Added for enabling view documents in preauth screen while raising
		 * query -- fix starts.
		 * */
		if (null != claimByKey) {
			Date date17 = new Date();
			reimbursementDTO.setDmsDocumentDTOList(getClaimsDMSList(claimByKey
					.getIntimation().getIntimationId()));
			Date date18 = new Date();
			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY CLAIMS DMS LIST FETCH %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date17, date18));

		}
		/**
		 * Added for enabling view documents in preauth screen while raising
		 * query -- fix ends
		 * */

		for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
			/*
			 * if(null != reimbursementObjectByKey) {
			 * uploadDocumentDTO.setIntimationNo
			 * (reimbursementObjectByKey.getClaim
			 * ().getIntimation().getIntimationId());
			 * uploadDocumentDTO.setDateOfAdmission
			 * (SHAUtils.formatDate(reimbursementObjectByKey
			 * .getDateOfAdmission()));
			 * uploadDocumentDTO.setDateOfDischarge(SHAUtils
			 * .formatDate(reimbursementObjectByKey.getDateOfDischarge()));
			 * uploadDocumentDTO
			 * .setInsuredPatientName(reimbursementObjectByKey.getClaim
			 * ().getIntimation().getInsuredPatientName()); }
			 */
			documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
			uploadDocumentDTO.setStatus(true);
			Date date19 = new Date();
			List<RODBillDetails> billEntryDetails = rodService
					.getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
			Date date20 = new Date();
			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY BILL ENTRY DETAILS LIST %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date19, date20));

			List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
			if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
				for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
					/*
					 * <<<<<<< HEAD
					 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
					 * uploadDocumentDTO)); =======
					 */
					dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,
							uploadDocumentDTO));
					// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
				}
			}
			/*
			 * uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
			 * .get(8));
			 */
			uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
					.get(8));
			uploadDocumentDTO
					.setProductBasedICURent((Double) detailsMap.get(9));
			/*
			 * uploadDocumentDTO.setProductBasedAmbulanceAmt((Double) detailsMap
			 * .get(15));
			 */
			uploadDocumentDTO
					.setProductBasedAmbulanceAmt(getProductBasedAmbulanceAmt(tableDTO
							.getRodKey()));
			uploadDocumentDTO.setStatus(false);
			uploadDocumentDTO.setBillEntryDetailList(dtoList);
			uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO()
					.getClaimType());
			uploadDocumentDTO.setRodKey(reimbursementObjectByKey.getKey());
			uploadDocumentDTO.setStrUserName(tableDTO.getUsername());

		}

		List<UploadDocumentDTO> rodBillSummaryDetails = rodService
				.getRODBillSummaryDetails(reimbursementObjectByKey.getKey(),
						mapper);
		if (rodBillSummaryDetails != null) {
			for (UploadDocumentDTO uploadDocumentDTO : rodBillSummaryDetails) {
				uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO()
						.getClaimType());
			}
		}

		reimbursementDTO.getUploadDocDTO()
				.setBillingWorkSheetUploadDocumentList(rodBillSummaryDetails);
		Double totalBilledAmount = reimbursementService
				.getTotalBilledAmount(documentSummaryKeys);

		reimbursementDTO.setAmountConsidered(totalBilledAmount != null ? String
				.valueOf(totalBilledAmount.intValue()) : "0");
		reimbursementDTO
				.setInitialAmountConsidered(totalBilledAmount != null ? String
						.valueOf(totalBilledAmount.intValue()) : "0");
		reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		// reimbursementDTO.setRodHumanTask(tableDTO.getHumanTask());
	//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());

		Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
				.getIntimation().getHospital());
		ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = reimbursementDTO
				.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
		if (hospitalById != null) {

			updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			updateHospitalDetails.setHospitalState(hospitalById.getState());
			updateHospitalDetails.setHospitalCode(hospitalById
					.getHospitalCode());
			updateHospitalDetails.setPanNumber(hospitalById.getPanNumber());
			updateHospitalDetails.setHospitalTypeId(hospitalById
					.getHospitalType().getKey());
			updateHospitalDetails.setInpatientBeds(hospitalById
					.getInpatientBeds() != null ? String.valueOf(hospitalById
					.getInpatientBeds()) : "0");
			updateHospitalDetails.setHospitalName(hospitalById.getName());
			updateHospitalDetails.setHospitalPhoneNo(hospitalById
					.getPhoneNumber());
			updateHospitalDetails.setHopitalRegNumber(hospitalById
					.getRegistrationNumber());
			updateHospitalDetails.setHospitalPincode(hospitalById.getPincode());
			updateHospitalDetails.setOtFacilityFlag(hospitalById
					.getOtFacilityFlag());
			updateHospitalDetails.setIcuFacilityFlag(hospitalById
					.getIcuFacilityFlag());
			updateHospitalDetails
					.setHospitalAddress1(hospitalById.getAddress());

		}

		List<MedicalVerificationDTO> medicalDTO = new ArrayList<MedicalVerificationDTO>();
		List<TreatmentQualityVerificationDTO> treatmentDTO = new ArrayList<TreatmentQualityVerificationDTO>();
		List<ClaimVerification> claimVerificationByReimbKey = reimbursementService
				.getClaimVerificationByReimbKey(reimbursementObjectByKey
						.getKey());
		if (claimVerificationByReimbKey != null) {

			for (ClaimVerification claimVerification : claimVerificationByReimbKey) {
				if (claimVerification.getVerificationType() != null
						&& claimVerification.getVerificationType()
								.toLowerCase().equalsIgnoreCase("medical")) {

					MedicalVerificationDTO medicalVerifyDTO = new MedicalVerificationDTO();
					List<SelectValue> itemIds = medicalContainer.getItemIds();
					if (itemIds != null && !itemIds.isEmpty()) {
						for (SelectValue selectValue : itemIds) {
							if (claimVerification.getVerificationTypeId()
									.equals(selectValue.getId())) {
								medicalVerifyDTO.setDescriptionId(selectValue
										.getId());
								medicalVerifyDTO.setDescription(selectValue
										.getValue());
							}
						}
					}
					medicalVerifyDTO.setKey(claimVerification.getKey());
					medicalVerifyDTO.setRemarks(claimVerification
							.getMedicalRemarks());
					medicalVerifyDTO.setVerifiedFlag(claimVerification
							.getVerifiedFlag());

					medicalDTO.add(medicalVerifyDTO);
				} else if (claimVerification.getVerificationType() != null
						&& claimVerification.getVerificationType()
								.toLowerCase().equalsIgnoreCase("treatment")) {

					TreatmentQualityVerificationDTO treatmentVerifyDTO = new TreatmentQualityVerificationDTO();
					List<SelectValue> itemIds = treatmentContainer.getItemIds();
					if (itemIds != null && !itemIds.isEmpty()) {
						for (SelectValue selectValue : itemIds) {
							if (claimVerification.getVerificationTypeId()
									.equals(selectValue.getId())) {
								treatmentVerifyDTO.setDescriptionId(selectValue
										.getId());
								treatmentVerifyDTO.setDescription(selectValue
										.getValue());
							}
						}
					}
					treatmentVerifyDTO.setKey(claimVerification.getKey());
					treatmentVerifyDTO.setRemarks(claimVerification
							.getMedicalRemarks());
					treatmentVerifyDTO.setVerifiedFlag(claimVerification
							.getVerifiedFlag());

					treatmentDTO.add(treatmentVerifyDTO);
				}
			}
		}
		if (claimByKey.getClaimType() != null
				&& claimByKey.getClaimType().getKey() != null
				&& claimByKey.getClaimType().getKey()
						.equals(ReferenceTable.CLAIM_TYPE_CASHLESS_ID)) {

			reimbursementDTO.setIsCashlessType(true);

			Preauth previousPreauth = getPreviousPreauth(claimByKey.getKey());
			reimbursementDTO
					.setTreatmentRemarksList(getTreatmentRemarksHistory(claimByKey
							.getKey()));

			if (previousPreauth != null) {
				reimbursementDTO.setPreauthKey(previousPreauth.getKey());
			}
		}

		reimbursementDTO.getPreauthMedicalDecisionDetails()
				.setMedicalVerificationTableDTO(medicalDTO);
		reimbursementDTO.getPreauthMedicalDecisionDetails()
				.setTreatmentVerificationDTO(treatmentDTO);
		reimbursementDTO.getPreauthDataExtractionDetails()
				.setReasonForAdmission(
						claimByKey.getIntimation().getAdmissionReason());
		reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
				reimbursementObjectByKey.getKey());

		
		if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_APPROVAL_REFER_TO_MEDICAL_APPROVER)) {
			
			MedicalApprover medicalApprover = pareimbursementService.getMedicalApprover(reimbursementObjectByKey.getKey());
			if(null != medicalApprover)
			{
				reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalReason(medicalApprover.getReasonForReferring());
				reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalRemarks(medicalApprover.getReferringRemarks());
				reimbursementDTO.setPreviousRemarks(medicalApprover.getReferringRemarks());
				reimbursementDTO.setPreviousReasonForReferring(medicalApprover.getReasonForReferring());
			}
			
		}
		
		
		if (reimbursementObjectByKey.getStatus().getKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)|| 
				reimbursementObjectByKey.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER) ||
						reimbursementObjectByKey.getStatus().getKey().equals(ReferenceTable.CLAIM_APPROVAL_REFER_TO_MEDICAL_APPROVER)) {
			Long medicalApproverKey = reimbursementService
					.getLatestMedicalApproverKey(reimbursementObjectByKey
							.getKey());
			MedicalApprover medicalApprover = reimbursementService
					.getMedicalApproverByKey(medicalApproverKey);
			if (medicalApprover != null) {
				reimbursementDTO.setPreviousRemarks(medicalApprover
						.getReferringRemarks());
				reimbursementDTO.setPreviousReasonForReferring(medicalApprover
						.getReasonForReferring());

				if (medicalApprover.getReferringRemarks() == null) {
					reimbursementDTO.setPreviousRemarks(medicalApprover
							.getApproverReply());
				}
				reimbursementDTO.setMedicalApproverKey(medicalApproverKey);
			}

			if (reimbursementObjectByKey.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
				reimbursementDTO.setIsReplyToFA(true);
			}

			reimbursementDTO.setIsReferToMedicalApprover(true);
		}
		
		if (null != reimbursementObjectByKey.getStatus()
				&& (reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)))
						{
								reimbursementDTO.setIsReBilling(true);
						}
		
		if (null != reimbursementObjectByKey.getStatus()
				&& (reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)))
						{
								reimbursementDTO.setIsReBilling(true);
						}

		

		if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)) {
			PreauthEscalate preauthEscalate = reimbursementService
					.getEscalateByClaimKey(reimbursementObjectByKey.getClaim()
							.getKey());
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(preauthEscalate.getEscalateRemarks());
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(preauthEscalate.getEscalateRemarks());
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setEscalateRemarks(preauthEscalate.getEscalateRemarks());
			reimbursementDTO.setIsEscalateReplyEnabled(true);
		} else if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)) {
			PreauthEscalate preauthEscalate = reimbursementService
					.getEscalateByClaimKey(reimbursementObjectByKey.getClaim()
							.getKey());
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(
							preauthEscalate.getEsclateReplyRemarks());
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(
							preauthEscalate.getEsclateReplyRemarks());
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setEscalateReply(preauthEscalate.getEsclateReplyRemarks());
			reimbursementDTO.setIsEscalateReplyEnabled(false);
		} else if (reimbursementObjectByKey
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS)) {
			ReimbursementRejectionDto reimbursementRejectionByKey = reimbursementRejectionService
					.getReimbursementRejectionByKey(reimbursementObjectByKey
							.getKey());
			if (reimbursementRejectionByKey != null) {
				reimbursementDTO.getPreauthMedicalDecisionDetails()
						.setRejectionRemarks(
								reimbursementRejectionByKey
										.getDisapprovedRemarks());
			}
		} else if (reimbursementObjectByKey
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)) {
			ReimbursementQuery reimbursementyQueryByRodKey = reimbursementQuerySerice
					.getReimbursementyQueryByRodKey(reimbursementObjectByKey
							.getKey());

			if (reimbursementyQueryByRodKey != null) {
				reimbursementDTO.getPreauthMedicalDecisionDetails()
						.setQueryRemarks(
								reimbursementyQueryByRodKey
										.getRejectionRemarks());
			}
		} else if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED)
				|| reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED)) {
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setApprovalRemarks("-");
			Investigation investigation = investigationService
					.getInvestigationByTransactionKey(reimbursementObjectByKey
							.getKey());
			if (investigation != null) {
				reimbursementDTO.getPreauthMedicalProcessingDetails()
						.setApprovalRemarks(investigation.getRemarks());
			}
		}
		setCategoryFlag(reimbursementObjectByKey, reimbursementDTO);

		if (!reimbursementDTO.getHospitalizaionFlag()) {
			reimbursementDTO = setHospitalizationAmount(reimbursementDTO);
		}

		setPreAndPostHopitalizationAmount(reimbursementDTO);

		if (reimbursementObjectByKey.getStatus().getKey() != null
				&& reimbursementObjectByKey.getStatus().getKey()
						.equals(ReferenceTable.ZONAL_REVIEW_REJECTION_STATUS)) {
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(
							reimbursementObjectByKey.getRejectionRemarks());
		} else if (reimbursementObjectByKey.getStatus().getKey() != null
				&& reimbursementObjectByKey.getStatus().getKey()
						.equals(ReferenceTable.ZONAL_REVIEW_QUERY_STATUS)) {
			ReimbursementQuery reimbursementQueryByReimbursmentKey = reimbursementService
					.getReimbursementQueryByReimbursmentKey(reimbursementObjectByKey
							.getKey());
			reimbursementDTO.setIsZonalReviewQuery(true);
			// reimbursementDTO.getPreauthMedicalProcessingDetails().setApprovalRemarks(reimbursementQueryByReimbursmentKey
			// != null ? reimbursementQueryByReimbursmentKey.getQueryRemarks() :
			// "");
		} else if (reimbursementObjectByKey.getStatus().getKey() != null
				&& reimbursementObjectByKey.getStatus().getKey()
						.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)) {
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(
							reimbursementObjectByKey.getApprovalRemarks());
		}
		if (claimByKey.getStatus() != null) {
			if (claimByKey.getStatus().getKey()
					.equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
					|| claimByKey.getStatus().getKey()
							.equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)) {
				reimbursementDTO.setIsWithDrawn(true);
			}
		}
		Boolean rejectionExistOrNot = acknowledgementDocumentsReceivedService
				.isRejectionExistOrNot(reimbursementObjectByKey.getKey());
		reimbursementDTO.setIsRejectionROD(rejectionExistOrNot);
		reimbursementDTO.setFvrCount(intimationService
				.getFVRCount(reimbursementDTO.getNewIntimationDTO().getKey()));

		Date date21 = new Date();
		 loadRRCRequestValues(reimbursementDTO,insuredSumInsured,SHAConstants.CLAIM_REQUEST);
		Date date22 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY LOAD RRC REQUEST VALUES %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date21, date22));

		if (reimbursementDTO.getPreauthMedicalDecisionDetails()
				.getInvestigatorCode() != null) {
			TmpInvestigation tmpInvestigationByInvestigatorCode = investigationService
					.getTmpInvestigationByInvestigatorCode(reimbursementDTO
							.getPreauthMedicalDecisionDetails()
							.getInvestigatorCode());
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setInvestigatorName(tmpInvestigationByInvestigatorCode);
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setInvestigationReviewRemarks(
							reimbursementObjectByKey.getInvestigatorRemarks());
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setInvestigationReportReviewed(true);
		}

		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if (strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(
					reimbursementDTO.getNewIntimationDTO().getPolicy()
							.getPolicyNumber(), reimbursementDTO.getNewIntimationDTO().getIntimationId());
			if (get64vbStatus != null
					&& SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				reimbursementDTO.setIsDishonoured(true);
			} else if (get64vbStatus != null
					&& (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
				reimbursementDTO.setIsPending(true);
			}
		}
		Date date23 = new Date();
		reimbursementDTO.setSittingsAmount(dbCalculationService
				.getDialysisAmount(
						reimbursementDTO.getNewIntimationDTO().getPolicy()
								.getProduct().getKey(),
						insuredSumInsured.longValue()).intValue());
		Date date24 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY GET DIALYSIS AMOUT PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date23, date24));

		if (reimbursementObjectByKey.getStatus() != null
				&& !(reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER) || reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER))) {
			if (reimbursementDTO.getIsReverseAllocation()) {
				reimbursementDTO.setIsReverseAllocation(false);
			}
		}

		/*
		 * maternity flag from db
		 */
		Date date27 = new Date();
		String maternityFlag = dbCalculationService
				.getMaternityFlagForProduct(reimbursementDTO
						.getNewIntimationDTO().getPolicy().getProduct()
						.getKey());
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY MATERNITY FLAG  PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date27, new Date()));

		// if(maternityFlag.equalsIgnoreCase("Y")){
		// reimbursementDTO.setMaternityFlag(true);
		// }

		Date date28 = new Date();
		Boolean queryReceivedStatusRod = reimbursementQuerySerice
				.isQueryReceivedStatusRod(reimbursementObjectByKey.getKey());

		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY QUERY RECEIVED ROD %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date28, new Date()));

		if (queryReceivedStatusRod) {
			reimbursementDTO.setIsQueryReceived(true);
		}

		reimbursementDTO.setIsReverseAllocation(false);
		Date date29 = new Date();
		Map<String, String> popupMessages = dbCalculationService
				.getPOPUPMessages(reimbursementDTO.getPolicyKey(),
						reimbursementDTO.getNewIntimationDTO()
								.getInsuredPatient().getKey(),reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY POPUP MESSAGE PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date28, new Date()));
		reimbursementDTO.setPopupMap(popupMessages);

		if (reimbursementDTO.getNewIntimationDTO().getHospitalDto() != null
				&& reimbursementDTO.getNewIntimationDTO().getHospitalDto()
						.getSuspiciousFlag() != null
				&& reimbursementDTO.getNewIntimationDTO().getHospitalDto()
						.getSuspiciousFlag().equalsIgnoreCase("Y")) {
			if (reimbursementDTO.getNewIntimationDTO().getHospitalDto()
					.getSuspiciousRemarks() != null) {
				Map<String, String> suspiciousMap = SHAUtils
						.getSuspiciousMap(reimbursementDTO
								.getNewIntimationDTO().getHospitalDto()
								.getSuspiciousRemarks());
				reimbursementDTO.setSuspiciousPopupMap(suspiciousMap);
			}
		}

		try {
			ImsUser imsUser = tableDTO.getImsUser();

			if (imsUser != null) {
				String[] userRoleList = imsUser.getUserRoleList();

				WeakHashMap<String, Object> escalateValidation = SHAUtils
						.getEscalateValidation(userRoleList);

				if ((Boolean) escalateValidation.get(SHAConstants.RMA6)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA6(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA5)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA5(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA4)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA4(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA3)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA3(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA2)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA2(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA1)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA1(true);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		reimbursementDTO.setAdmissionDatePopup(dbCalculationService
				.getPolicyAgeingForPopup(reimbursementDTO.getNewIntimationDTO()
						.getAdmissionDate(), reimbursementDTO
						.getNewIntimationDTO().getPolicy().getPolicyNumber()));
		Date date15 = new Date();
		reimbursementDTO.setIsPEDInitiated(pedQueryService
				.isPEDInitiated(reimbursementDTO.getNewIntimationDTO()
						.getPolicy().getKey()));
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY PEQ QUERY SERVICE PED INTIATED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date15, new Date()));

		reimbursementDTO.setIsPEDInitiatedForBtn(reimbursementDTO
				.getIsPEDInitiated());

		if (reimbursementObjectByKey.getDocAcknowLedgement() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getDocumentReceivedFromId() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getDocumentReceivedFromId().getKey()
						.equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			Date date17 = new Date();
			Boolean isPedWatchList = preauthService.getDBTaskForPreauth(reimbursementObjectByKey.getClaim().getIntimation().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY PEQ WATCH LIST BPMN %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date17, new Date()));
			reimbursementDTO.setIsPedWatchList(isPedWatchList);
		}

	/*	if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getReconsiderationRequest() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getReconsiderationRequest().equalsIgnoreCase("Y")) {
			reimbursementDTO.setIsReconsiderationRequest(true);
		}*/
		try {
			if((reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest() != null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest().equalsIgnoreCase("Y"))
					|| (reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))) {
				if(reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag() ==  null || reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag().equalsIgnoreCase("N")) {
					reimbursementDTO.setIsReconsiderationRequest(true);
					if(reimbursementService.isClaimPaymentAvailable(reimbursementObjectByKey.getRodNumber())) {
						Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
						reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
					} else {
						/**
						 * For reject reconsider, cancel rod button was enabled. But for this ticket GALAXYMAIN-6222	
						 * which was raised in PA, now cancel rod button is diabled for rejection case to.
						 * Hence below code was commented.
						 * */
						//reimbursementDTO.setIsReconsiderationRequest(false);
					}
//					Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
//					reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		List<InsuredPedDetails> pedByInsured = policyService
				.getPEDByInsured(reimbursementDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredId());

		reimbursementDTO.setInsuredPedDetails(pedByInsured);
		reimbursementDTO.setSearchFormDTO(tableDTO.getSearchDTO());
		//SHAUtils.setDefaultCopayValue(reimbursementDTO);

		if (reimbursementObjectByKey != null
				&& reimbursementObjectByKey.getDocAcknowLedgement() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag().equalsIgnoreCase("Y")) {
			reimbursementDTO
					.setRodTotalClaimedAmount(reimbursementObjectByKey
							.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() != null ? reimbursementObjectByKey
							.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() : 0d);
		}

		if (reimbursementObjectByKey != null
				&& reimbursementObjectByKey.getDocAcknowLedgement() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag().equalsIgnoreCase("Y")) {
			reimbursementDTO
					.setRodTotalClaimedAmount(reimbursementObjectByKey
							.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() != null ? reimbursementObjectByKey
							.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() : 0d);
		}

		if (reimbursementObjectByKey.getClaim().getClaimType().getKey()
				.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
				&& reimbursementObjectByKey.getDocAcknowLedgement() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getDocumentReceivedFromId() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getDocumentReceivedFromId().getKey()
						.equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {

			if (hospitalById != null) {
				Boolean hospitalDiscount = SHAUtils
						.isHospitalDiscount(hospitalById);
				reimbursementDTO
						.setIsHospitalDiscountApplicable(hospitalDiscount);
			}
		}

		if (reimbursementDTO.getNewIntimationDTO() != null
				&& reimbursementDTO.getNewIntimationDTO().getInsuredPatient() != null
				&& reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
						.getKey() != null) {
			Long claimCount = preauthService.getClaimCount(reimbursementDTO
					.getNewIntimationDTO().getPolicy().getKey());
			if (claimCount != null) {
				reimbursementDTO.setClaimCount(claimCount);
			}
		}
		
			Date policyFromDate = reimbursementDTO.getNewIntimationDTO()
					.getPolicy().getPolicyFromDate();

			Date admissionDate = reimbursementDTO.getNewIntimationDTO()
					.getAdmissionDate();

			Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
			if (diffDays != null && diffDays < 90) {
				reimbursementDTO.setIs64VBChequeStatusAlert(true);
			}

		if (null != reimbursementObjectByKey) {
			List<AddOnCoversTableDTO> addOnCoversList = createRodService
					.getAddOnCoversValueBasedOnROD(reimbursementObjectByKey
							.getKey());
			if (null != addOnCoversList && !addOnCoversList.isEmpty()) {
				reimbursementDTO.getPreauthDataExtractionDetails()
						.setAddOnCoversTableList(addOnCoversList);
			}
			List<AddOnCoversTableDTO> optionalCoversList = createRodService
					.getOpitionalCoversValueBasedOnROD(reimbursementObjectByKey
							.getKey());
			if (null != optionalCoversList && !optionalCoversList.isEmpty()) {
				reimbursementDTO.getPreauthDataExtractionDetails()
						.setOptionalCoversTableList(optionalCoversList);
			}
			List<PABenefitsDTO> benefitsDTOList = createRodService.getPABenefitsBasedOnRodKey(reimbursementObjectByKey.getKey());
			
			if(null != benefitsDTOList && !benefitsDTOList.isEmpty())
			{
				reimbursementDTO.getPreauthDataExtractionDetails().setPaBenefitsList(benefitsDTOList);
				if(null != reimbursementObjectByKey.getBenefitsId())
				{
					reimbursementDTO.getPreauthDataExtractionDetails().setOnloadBenefitId(reimbursementObjectByKey.getBenefitsId().getKey());
					
					List<PABenefitsDTO> paBenefitsProcedureDTOList = null;
					
					if(null != reimbursementDTO && null != reimbursementDTO.getNewIntimationDTO() && null != reimbursementDTO.getNewIntimationDTO().getPolicy() &&
							null != reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct() && null != reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
							!(ReferenceTable.getGPAProducts().containsKey(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
					
						paBenefitsProcedureDTOList = dbCalculationService.getBenefitCoverValues(reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey()  , reimbursementObjectByKey.getBenefitsId().getKey());
					}
					else
					{
						paBenefitsProcedureDTOList = dbCalculationService.getGPABenefitCoverValues(reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey()  , reimbursementObjectByKey.getBenefitsId().getKey());
					}
					if(null != paBenefitsProcedureDTOList && !paBenefitsProcedureDTOList.isEmpty())
					{
						for (PABenefitsDTO paBenefitsDTO : benefitsDTOList) {
							
							for (PABenefitsDTO paBenefitsDTOObj : paBenefitsProcedureDTOList) {
								if(null != paBenefitsDTO.getBenefitCoverValue()  && paBenefitsDTO.getBenefitCoverValue().getValue().equalsIgnoreCase(paBenefitsDTOObj.getBenefitCover()))
								{
									paBenefitsDTO.setSumInsured(paBenefitsDTOObj.getSumInsured());
									paBenefitsDTO.setPercentage(paBenefitsDTOObj.getPercentage());
									//if(null != paBenefitsDTOObj.getNoOfWeeks())
										//paBenefitsDTO.setNoOfWeeks(paBenefitsDTOObj.getNoOfWeeks());
										//paBenefitsDTO.setNoOfWeeks(paBenefitsDTOObj.get);
									if(null != paBenefitsDTOObj.getEligibleAmountPerWeek())
										paBenefitsDTO.setEligibleAmountPerWeek(paBenefitsDTOObj.getEligibleAmountPerWeek());
									paBenefitsDTO.setEligibleAmount(paBenefitsDTOObj.getEligibleAmount());
									break;
								}
							}
						}
						
					}
				}
			}

		}

		if (null != reimbursementObjectByKey.getBenefitsId())
			reimbursementDTO
					.getPreauthDataExtractionDetails()
					.setBenefitsValue(
							reimbursementObjectByKey.getBenefitsId().getValue());
		/*if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)) {*/
			
			MedicalApprover medicalApprover = pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(),SHAConstants.PA_CLAIM_REQUEST_REBILLING);
			if(null != medicalApprover)
			{
				reimbursementDTO.getPreauthDataExtractionDetails().setBillingMedicalReason(medicalApprover.getReasonForReferring());
				reimbursementDTO.getPreauthDataExtractionDetails().setBillingMedicalRemarks(medicalApprover.getReferringRemarks());
				reimbursementDTO.setPreviousRemarks(medicalApprover.getReferringRemarks());
				reimbursementDTO.setPreviousReasonForReferring(medicalApprover.getReasonForReferring());
			}
			
			MedicalApprover claimMedicalApprover = pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(),SHAConstants.PA_CLAIM_REQUEST_RECLAIM);
			if(null != claimMedicalApprover)
			{
				if(claimMedicalApprover.getReasonForReferring()!=null){
					reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalReason(claimMedicalApprover.getReasonForReferring());
					reimbursementDTO.setPreviousReasonForReferring(claimMedicalApprover.getReasonForReferring());
					
				}
				if(claimMedicalApprover.getReferringRemarks()!=null){
					reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalRemarks(claimMedicalApprover.getReferringRemarks());
					reimbursementDTO.setPreviousRemarks(claimMedicalApprover.getReferringRemarks());
				}
			}
			
			MedicalApprover finacialMedicalApprover = pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(),SHAConstants.PA_CLAIM_REQUEST_REFINANCE);
			if(null != finacialMedicalApprover)
			{
				if(finacialMedicalApprover.getReasonForReferring()!=null){
					reimbursementDTO.getPreauthDataExtractionDetails().setFinancialApprovalMedicalReason(finacialMedicalApprover.getReasonForReferring());
					reimbursementDTO.setPreviousReasonForReferring(finacialMedicalApprover.getReasonForReferring());
					reimbursementDTO.setPreviousRemarks(finacialMedicalApprover.getReferringRemarks());
					reimbursementDTO.getPreauthDataExtractionDetails().setFinancialApprovalMedicalRemarks(finacialMedicalApprover.getReferringRemarks());
					
				}
				if(null != claimMedicalApprover && null != claimMedicalApprover.getReferringRemarks()){
					reimbursementDTO.getPreauthDataExtractionDetails().setFinancialApprovalMedicalRemarks(finacialMedicalApprover.getReferringRemarks());
					reimbursementDTO.setPreviousRemarks(claimMedicalApprover.getReferringRemarks());
					
				}
			}
			
	//	}
		if(null != reimbursementObjectByKey.getBenefitsId())
		{
			reimbursementDTO.getPreauthDataExtractionDetails().setBenefitsValue( reimbursementObjectByKey.getBenefitsId().getValue());
		}
		reimbursementDTO.getPreauthDataExtractionDetails().setDischargeDateForPa(reimbursementDTO.getPreauthDataExtractionDetails().getDischargeDate());
		
		if(ReferenceTable.CLAIM_APPROVAL_REFER_TO_MEDICAL_APPROVER.equals(reimbursementObjectByKey.getStatus().getKey())){
			reimbursementDTO.setIsReferToMedicalApprover(Boolean.TRUE);
		}
		
		SelectValue reasonValue = new SelectValue();
		if(null != reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationReasonId())
		{
			reasonValue.setId(reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationReasonId().getKey());
			reasonValue.setValue(reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationReasonId().getValue());
			reimbursementDTO.getPreauthDataExtractionDetails().setReasonForReconsideration(reasonValue);

		}
		
		reimbursementDTO.getPreauthDataExtractionDetails().setDischargeDateForPa(reimbursementObjectByKey.getDateOfDischarge());

		if(null != reimbursementDTO.getRodNumber()){
			ClaimPayment claimPayment = acknowledgementDocumentsReceivedService.getClaimPaymentDetails(reimbursementDTO.getRodNumber());
			if(null != claimPayment && null != claimPayment.getStatusId() && claimPayment.getStatusId().getKey().equals(ReferenceTable.PAYMENT_SETTLED)){
				reimbursementDTO.setIsPaymentSettled(true);
			}
		}
		
		
		UpdateHospital hospitalDetails = pareimbursementService.getHospitalDetails(reimbursementObjectByKey.getKey());
		if(null != hospitalDetails && null != hospitalDetails.getInpatientBeds())
		{
				reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails().setInpatientBeds(String.valueOf(hospitalDetails.getInpatientBeds()));
		}		

		reimbursementDTO.setDbOutArray(tableDTO.getDbOutArray());
		
		List<DocAcknowledgement> docAckListList = pareimbursementService.getDocumentDetailsByReimbKey(reimbursementObjectByKey.getKey());
		if(null != docAckListList && !docAckListList.isEmpty())
		{
			reimbursementDTO.setDocumentAckList(docAckListList);
		}
		
		LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
		legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
		reimbursementDTO.setLegalHeirDto(legalHeirDTO);

		List<NomineeDetailsDto> nomineeDtoList = intimationService.getNomineeDetailsListByTransactionKey(tableDTO.getRodKey());
		if( nomineeDtoList != null && !nomineeDtoList.isEmpty()){
			reimbursementDTO.getNewIntimationDTO().setNomineeList(nomineeDtoList);
		}
		if(reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null 
				&& reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()) {
			List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(tableDTO.getRodKey());
			if(legalHeirList != null && !legalHeirList.isEmpty()) {
				List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
				LegalHeirDTO legalHeirDto;
				for (LegalHeir legalHeir : legalHeirList) {
					 legalHeirDto = new LegalHeirDTO(legalHeir);
					 legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
					 legalHeirDTOList.add(legalHeirDto);
				}
				reimbursementDTO.setLegalHeirDTOList(legalHeirDTOList);
			}	
		}
		reimbursementDTO.setScreenName(tableDTO.getScreenName());
		
		Map<String,Object> icrAgent  = dbCalculationService.getAgentAndBranchName(reimbursementDTO.getNewIntimationDTO().getIntimationId()); 
		 if(icrAgent != null && !icrAgent.isEmpty()) {
				String agentScorePoint = icrAgent.get(SHAConstants.ICR_AGENT_POINT).toString();
				reimbursementDTO.setIcrAgentValue(agentScorePoint);
				String smScorepoint = icrAgent.get(SHAConstants.SM_AGENT_POINT).toString();
				reimbursementDTO.setSmAgentValue(smScorepoint);
		 }

		//CR2019234 reimbrushement
		if(reimbursementDTO != null && reimbursementDTO.getNewIntimationDTO().getIntimatedBy() != null && 
				reimbursementDTO.getNewIntimationDTO().getIntimatedBy().getId() != null && 
						reimbursementDTO.getNewIntimationDTO().getIntimatedBy().getId().toString().equalsIgnoreCase(SHAConstants.AGENT_CODE) 
				|| reimbursementDTO.getNewIntimationDTO().getIntimatedBy().getId().toString().equalsIgnoreCase(SHAConstants.SALES_MANAGER_CODE)){
			view.showInfoPopUp("This claim has been intimated by" + reimbursementDTO.getNewIntimationDTO().getIntimatedBy().getValue());
		}

		Policy policy = reimbursementDTO.getNewIntimationDTO().getPolicy();
		// added for PA installment payment process in policy level and product
		// level check
		Map<String, String> getPolicyInstallment = dbCalculationService
				.getInstallmentPaymentFlag(policy.getPolicyNumber(), policy.getProduct()
						.getKey());
		if (getPolicyInstallment != null && !getPolicyInstallment.isEmpty()) {
			reimbursementDTO.setPolicyInstalmentFlag(getPolicyInstallment
					.get(SHAConstants.FLAG) != null ? getPolicyInstallment
							.get(SHAConstants.FLAG) : "N");
			reimbursementDTO
			.setPolicyInstalmentMsg(getPolicyInstallment
					.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallment
							.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
		}
		if (reimbursementDTO.getPolicyInstalmentFlag() != null
				&& reimbursementDTO.getPolicyInstalmentFlag().equals(
						SHAConstants.YES_FLAG)) {
			Integer policyInstallmentAmount = PremiaService.getInstance()
					.getPolicyInstallmentAmount(policy
							.getPolicyNumber());
			reimbursementDTO.setPolicyInstalmentPremiumAmt(policyInstallmentAmount
					.doubleValue());

			String policyDueDate = PremiaService.getInstance()
					.getPolicyInstallmentDetails(policy
							.getPolicyNumber());
			//code added if we recevied instalment due amount as zero after pending amount adjusted to premia/bancs  need to set due date as admission date by noufel
			if((reimbursementDTO.getPolicyInstalmentPremiumAmt() != null && reimbursementDTO.getPolicyInstalmentPremiumAmt() == 0d) && 
					(policyDueDate == null || policyDueDate.isEmpty())) {

				reimbursementDTO.setPolicyInstalmentDueDate(reimbursementDTO.getPreauthDataExtractionDetails().getAdmissionDate());	
			}
			else{
				if (reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.formatTimeFromString(policyDueDate.toString()));	
				}
				else{
					reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.dateformatFromString(policyDueDate.toString()));
				}
			}
		}
		
		view.setPAProcessClaimRequest(PAClaimRequestWizard.class,
				reimbursementDTO);
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> SHOW MEDICAL APPROVAL CLAIM REQUEST METHOD ENDED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(startDate, new Date()));

		// }
	}
	}
	private List<ReconsiderRODRequestTableDTO> getReconsiderRODRequestForBillEntry(
			Long rodKey) {
		List<ReconsiderRODRequestTableDTO> reconsiderRODList = ackDocReceivedService
				.getReconsiderRequestTableValuesForBillEntry(rodKey);
		return reconsiderRODList;
	}

	private PreauthDTO getProrataFlagFromProduct(PreauthDTO preauthDTO) {
		Product product = masterService.getProrataForProduct(preauthDTO
				.getNewIntimationDTO().getPolicy().getProduct().getKey());
		if (null != product) {
			preauthDTO
					.setProrataDeductionFlag(null != product.getProrataFlag() ? product
							.getProrataFlag() : null);
			/**
			 * product based variable is added to enable or disable the
			 * component in page level. This would be static. -- starts
			 * */
			preauthDTO
					.setProductBasedProRata(null != product.getProrataFlag() ? product
							.getProrataFlag() : null);
			preauthDTO.setProductBasedPackage(null != product
					.getPackageAvailableFlag() ? product
					.getPackageAvailableFlag() : null);
			// ends.
			preauthDTO.setPackageAvailableFlag(null != product
					.getPackageAvailableFlag() ? product
					.getPackageAvailableFlag() : null);
		}
		return preauthDTO;
	}

	public List<DMSDocumentDetailsDTO> getClaimsDMSList(String intimationNo) {

		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getDocumentDetailsData(intimationNo, 0);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			return dmsDocumentDetailsDTO;
		}
		return null;

	}

	private BillEntryDetailsDTO getBillDetailsDTOForBilling(
			RODBillDetails billDetails, UploadDocumentDTO uploadDocumentDTO) {/*
																			 * ,
																			 * Map
																			 * <
																			 * Integer
																			 * ,
																			 * Object
																			 * >
																			 * detailsMap
																			 * )
																			 * {
																			 */
		// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
		BillEntryDetailsDTO dto = new BillEntryDetailsDTO();

		if (null != billDetails.getRodDocumentSummaryKey())
			dto.setDocumentSummaryKey(billDetails.getRodDocumentSummaryKey()
					.getKey());

		dto.setItemName(billDetails.getItemName());
		dto.setKey(billDetails.getKey());
		SelectValue classificationValue = new SelectValue();
		classificationValue.setId(billDetails.getBillClassification().getKey());
		classificationValue.setValue(billDetails.getBillClassification()
				.getValue());
		dto.setClassification(classificationValue);
		dto.setItemNo(billDetails.getItemNumber());
		if (billDetails.getItemNumber() != null) {
			dto.setBillNo(billDetails.getItemNumber().toString());
		}
		dto.setZonalRemarks(uploadDocumentDTO.getZonalRemarks());
		dto.setCorporateRemarks(uploadDocumentDTO.getCorporateRemarks());
		dto.setBillingRemarks(uploadDocumentDTO.getBillingRemarks());
		/*
		 * dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
		 * .getNoOfDaysBills().doubleValue() : 0d);
		 */
		dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
				.getNoOfDaysBills().doubleValue() : null);
		dto.setPerDayAmt(billDetails.getPerDayAmountBills());
		dto.setBillValue(billDetails.getClaimedAmountBills());
		dto.setItemValue(billDetails.getClaimedAmountBills());
		SelectValue billCategoryvalue = new SelectValue();
		billCategoryvalue.setId(billDetails.getBillCategory().getKey());
		billCategoryvalue.setValue(billDetails.getBillCategory().getValue());
		dto.setCategory(billCategoryvalue);

		/*
		 * Below values are added as a part of amount claimed table enhancement
		 */
		dto.setNoOfDaysAllowed(billDetails.getNoOfDaysPolicy());
		dto.setPerDayAmtProductBased(billDetails.getPerDayAmountPolicy());
		dto.setAmountAllowableAmount(billDetails.getTotalAmount());
		dto.setNonPayableProductBased(billDetails.getNonPayableAmountProduct());
		dto.setNonPayable(billDetails.getNonPayableAmount());

		if (null != billCategoryvalue
				&& null != billCategoryvalue.getValue()
				&& (("Deductibles").equalsIgnoreCase(billCategoryvalue
						.getValue().trim()) || ("Deductibles(80%)")
						.equalsIgnoreCase(billCategoryvalue.getValue().trim()))) {
			if (null != billDetails.getClaimedAmountBills()) {
				dto.setReasonableDeduction(billDetails.getClaimedAmountBills());
			} else {
				dto.setReasonableDeduction(billDetails.getDeductibleAmount());
			}
		} else {
			dto.setReasonableDeduction(billDetails.getDeductibleAmount());
		}
		dto.setTotalDisallowances(billDetails.getPayableAmount());
		dto.setNetPayableAmount(billDetails.getNetAmount());
		dto.setDeductibleOrNonPayableReason(billDetails.getNonPayableReason());
		dto.setMedicalRemarks(billDetails.getMedicalRemarks());
		dto.setKey(billDetails.getKey());

		dto.setKey(billDetails.getKey());

		/*
		 * uploadDocumentDTO.setDateOfDischarge(SHAUtils.formatDate(objReimbursement
		 * .getDateOfDischarge()));
		 * uploadDocumentDTO.setInsuredPatientName(objReimbursement
		 * .getClaim().getIntimation().getInsuredPatientName());
		 */
		/*
		 * Reimbursement objReimbursement =
		 * billDetails.getRodDocumentSummaryKey().getReimbursement(); if(null !=
		 * objReimbursement) {
		 * dto.setIntimationNo(objReimbursement.getClaim().getIntimation
		 * ().getIntimationId());
		 * dto.setDateOfAdmission(SHAUtils.formatDate(objReimbursement
		 * .getDateOfAdmission()));
		 * dto.setDateOfDischarge(SHAUtils.formatDate(objReimbursement
		 * .getDateOfDischarge()));
		 * dto.setInsuredPatientName(objReimbursement.getClaim
		 * ().getIntimation().getInsuredPatientName()); }
		 */

		if (billDetails != null && billDetails.getIrdaLevel1Id() != null) {
			SelectValue irdaLevel1ValueByKey = masterService
					.getIRDALevel1ValueByKey(billDetails.getIrdaLevel1Id());
			dto.setIrdaLevel1(irdaLevel1ValueByKey);
		}
		if (billDetails != null && billDetails.getIrdaLevel2Id() != null) {
			SelectValue irdaLevel2ValueByKey = masterService
					.getIRDALevel2ValueByKey(billDetails.getIrdaLevel2Id());
			dto.setIrdaLevel2(irdaLevel2ValueByKey);
		}
		if (billDetails != null && billDetails.getIrdaLevel3Id() != null) {
			SelectValue irdaLevel3ValueByKey = masterService
					.getIRDALevel3ValueByKey(billDetails.getIrdaLevel3Id());
			dto.setIrdaLevel3(irdaLevel3ValueByKey);
		}

		// dto.setProductBasedRoomRent((Double)detailsMap.get(8));

		// IRDA level is not yet implemented. Once done, will do the necessary
		// changes below.
		/*
		 * SelectValue irdaLevelValue = new SelectValue(); if(null !=
		 * billDetails.getIrdaLevel1Id()) {
		 * irdaLevelValue.setId(billDetails.getIrdaLevel1Id()); }
		 */

		return dto;
	}

	private Double getProductBasedAmbulanceAmt(Long rodKey) {
		Double ambAmt = dbCalculationService
				.getProductBasedAmbulanceAmt(rodKey);
		return ambAmt;
	}

	private Preauth getPreviousPreauth(Long claimKey) {
		List<Preauth> preauthByClaimKey = preauthService
				.getPreauthByClaimKey(claimKey);
		Preauth previousPreauth = null;
		if (!preauthByClaimKey.isEmpty()) {
			previousPreauth = preauthByClaimKey.get(0);
			String[] split = previousPreauth.getPreauthId().split("/");
			String defaultNumber = split[split.length - 1];
			Integer nextReferenceNo = Integer.valueOf(defaultNumber);
			for (Preauth preauth : preauthByClaimKey) {
				if (preauth.getPreauthId() != null) {
					String[] splitNumber = preauth.getPreauthId().split("/");
					String number = splitNumber[splitNumber.length - 1];
					if (Integer.valueOf(number) > Integer
							.valueOf(defaultNumber)) {
						previousPreauth = preauth;
						nextReferenceNo = Integer.valueOf(number);
					}
				}
			}
		}
		return previousPreauth;
	}

	private List<String> getTreatmentRemarksHistory(Long claimKey) {
		List<String> treatmentRemarksList = new ArrayList<String>();
		List<Preauth> preauthByClaimKey = preauthService
				.getPreauthByClaimKey(claimKey);

		for (Preauth preauth : preauthByClaimKey) {
			treatmentRemarksList.add(preauth.getTreatmentRemarks());
		}
		return treatmentRemarksList;
	}

	private void setCategoryFlag(Reimbursement reimbursementObjectByKey,
			PreauthDTO reimbursementDTO) {
		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getHospitalisationFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setHospitalizaionFlag(true);
		}
		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getPreHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPreHospitalisationFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setPreHospitalizaionFlag(true);
		}
		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getPostHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setPostHospitalizaionFlag(true);
		}

		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getHospitalizationRepeatFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getHospitalizationRepeatFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setIsHospitalizationRepeat(true);
		}

		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getPartialHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPartialHospitalisationFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setPartialHospitalizaionFlag(true);
		}

		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getHospitalCashFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getHospitalCashFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setAddOnBenefitsHospitalCash(true);
		}

		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getPatientCareFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPatientCareFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setAddOnBenefitsPatientCare(true);
		}

		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getLumpsumAmountFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getLumpsumAmountFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setLumpSumAmountFlag(true);
		}
	}

	private PreauthDTO setPreAndPostHopitalizationAmount(PreauthDTO preauthDTO) {
		dbCalculationService.getBillDetailsSummary(preauthDTO.getKey());
		List<PreHospitalisation> preHospitalisationValues = billDetailsService
				.getPreHospitalisationList(preauthDTO.getKey());
		List<PostHospitalisation> postHospitalisationValues = billDetailsService
				.getPostHospitalisationList(preauthDTO.getKey());
		Integer postHospitalizationAmt = 0;
		Integer preHospitalizationAmt = 0;
		for (PostHospitalisation postHospitalisation : postHospitalisationValues) {
			postHospitalizationAmt += postHospitalisation
					.getClaimedAmountBills() != null ? postHospitalisation
					.getClaimedAmountBills().intValue() : 0;
		}

		for (PreHospitalisation preHospitalisation : preHospitalisationValues) {
			preHospitalizationAmt += preHospitalisation.getClaimedAmountBills() != null ? preHospitalisation
					.getClaimedAmountBills().intValue() : 0;
		}
		preauthDTO.getPreauthMedicalDecisionDetails().setBillingRemarks("");
		preauthDTO.setPreHospitalisationValue(String
				.valueOf(preHospitalizationAmt));
		preauthDTO.setPostHospitalisationValue(String
				.valueOf(postHospitalizationAmt));

		return preauthDTO;
	}

	// protected void showPASearchProcessRejectionView(
	// @Observes @CDIEvent(MenuItemBean.PA_PROCESS_PREAUTH_REJECTION) final
	// ParameterDTO parameters) {
	// view.setView(PAProcessRejectionView.class, true);
	// }

//	Commented the below Cashless Screen
//	protected void showPAProcessRejection(
//			@Observes @CDIEvent(MenuItemBean.PA_PROCESS_PREAUTH_REJECTION) final ParameterDTO parameters) {
//		view.setViewG(PASearchProcessRejectionView.class, true);
//	}

	protected void showRejection(
			@Observes @CDIEvent(PAMenuPresenter.PA_PROCESS_REJECTION) final ParameterDTO parameters) {
		SearchProcessRejectionTableDTO searchDto = (SearchProcessRejectionTableDTO) parameters
				.getPrimaryParameter();

		Preauth preauth = preauthService
				.getPreauthListByIntimationKey(searchDto.getKey());

		NewIntimationDto newIntimationDto = null;
		Claim claimByKey = null;
		if (preauth != null) {
			claimByKey = preauth.getClaim();
			newIntimationDto = intimationService.getIntimationDto(claimByKey
					.getIntimation());
		} else {
			claimByKey = claimService.getClaimforIntimation(searchDto.getKey());
			newIntimationDto = intimationService.getIntimationDto(claimByKey
					.getIntimation());
		}
		searchDto.setIntimationDTO(newIntimationDto);	
	//	searchDto.setAccidentDeath(claimByKey.getIncidenceFlag());
	//	searchDto.setPreauthStatus(preauth.getStatus().getProcessValue());
		ClaimDto claimDTO = null;
		Boolean isValidClaim = true;

		if (claimByKey != null) {

			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaim = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			} else {

				claimDTO = ClaimMapper.getInstance().getClaimDto(claimByKey);
				claimDTO.setNewIntimationDto(newIntimationDto);
			}

		}
		if (isValidClaim) {
			RRCDTO rrcDTO = new RRCDTO();
			rrcDTO.setClaimDto(claimDTO);
			rrcDTO.setNewIntimationDTO(newIntimationDto);
			rrcDTO.setStrUserName(searchDto.getUsername());
			
			Double insuredSumInsured = 0d;
			if(null != claimByKey &&  null != claimByKey.getIntimation() && 
			null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
			null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
			&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
			{
				insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(rrcDTO.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							rrcDTO.getNewIntimationDTO().getPolicy().getKey(),rrcDTO.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService
						.getGPAInsuredSumInsured(rrcDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								rrcDTO.getNewIntimationDTO().getPolicy().getKey());
			}
			loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured,
					SHAConstants.PROCESS_REJECTION);

			searchDto.setRrcDTO(rrcDTO);

			if (preauth != null) {
				searchDto.setIsPremedical(true);
			}
			view.setPARejectionView(PAProcessRejectionWizard.class, searchDto);
		}
	}

	private PreauthDTO setHospitalizationAmount(PreauthDTO preauthDTO) {
		List<Reimbursement> reimbursementByClaimKey = createRodService
				.getReimbursementByClaimKey(preauthDTO.getClaimKey());
		Integer hospitalizationAmount = 0;
		for (Reimbursement reimbursement : reimbursementByClaimKey) {
			if (reimbursement.getDocAcknowLedgement().getHospitalisationFlag() != null
					&& reimbursement.getDocAcknowLedgement()
							.getHospitalisationFlag().equalsIgnoreCase("Y")) {
				List<Hospitalisation> hospitalisationList = createRodService
						.getHospitalisationList(reimbursement.getKey());
				for (Hospitalisation hospitalisation : hospitalisationList) {
					hospitalizationAmount += hospitalisation.getClaimedAmount() != null ? hospitalisation
							.getClaimedAmount().intValue() : 0;
				}
			}
		}
		preauthDTO.setHospitalizationAmount(hospitalizationAmount);
		return preauthDTO;
	}

	protected void showPACoveringLetterDetail(
			@Observes @CDIEvent(MenuItemBean.PA_COVERING_LETTER_DETAIL) final ParameterDTO parameters) {

		GenerateCoveringLetterSearchTableDto searchPaCoveringLetterTableDto = (GenerateCoveringLetterSearchTableDto) parameters
				.getPrimaryParameter();
		
		//IMSSUPPOR-30553
		List<NomineeDetailsDto> nomineeList = intimationService.getNomineeForPolicyKey(searchPaCoveringLetterTableDto.getClaimDto().getNewIntimationDto().getPolicy().getKey());
        if(nomineeList != null && !nomineeList.isEmpty() ) {
                searchPaCoveringLetterTableDto.getClaimDto().getNewIntimationDto().setNomineeList(nomineeList);
                StringBuffer nomineeNames = new StringBuffer("");
                for (NomineeDetailsDto nomineeDetailsDto : nomineeList) {
                        nomineeNames = nomineeNames.toString().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getNomineeName()) : nomineeNames.append(","+nomineeDetailsDto.getNomineeName());
                }
                searchPaCoveringLetterTableDto.getClaimDto().getNewIntimationDto().setNomineeName(nomineeNames.toString());
        }

		view.setDecideOnPACoveringLewtterWizardView(
				DecideOnPACoveringLetterWizardViewImpl.class,
				searchPaCoveringLetterTableDto);
	}

	protected void showPABillEntryWizard(
			@Observes @CDIEvent(PAMenuPresenter.PA_SHOW_BILL_ENTRY) final ParameterDTO parameters) {

		SearchEnterBillDetailTableDTO tableDTO = (SearchEnterBillDetailTableDTO) parameters
				.getPrimaryParameter();
		ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
		Boolean isValidClaimForBillEntry = true;
		// Hard code key value will be later changed. This is for testing
		// purpose

		Long rodKey = tableDTO.getRodKey();// = 7006l;
		Long ackKey = tableDTO.getAckKey();
		Long claimKey = tableDTO.getClaimkey();
		Claim claimByKey = claimService.getClaimByKey(claimKey);
		populatePADocumentDetails(rodDTO, rodKey, ReferenceTable.BILL_ENTRY,claimByKey);
		StarCommonUtils.getAccidentAndDeathDate(claimByKey, rodDTO);
		
		DocAcknowledgement docAcknowledgementBasedOnKey = pareimbursementService.getDocAcknowledgementBasedOnKey(ackKey);
		rodDTO.setHospitalizationFlag(docAcknowledgementBasedOnKey.getHospitalisationFlag());
		
		Boolean isValidClaim = true;
		if (null != claimByKey) {
			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaim = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
		}
		
		if(isValidClaim) {
		
		/*if (null != claimByKey) {

			if (null != claimByKey.getIncidenceFlag()
					&& claimByKey.getIncidenceFlag().equalsIgnoreCase(
							SHAConstants.ACCIDENT_FLAG)) {
				rodDTO.getDocumentDetails().setAccidentOrDeath(true);
			} else {
				rodDTO.getDocumentDetails().setAccidentOrDeath(false);
			}
			rodDTO.getDocumentDetails().setAccidentOrDeathDate(
					claimByKey.getIncidenceDate());
		}*/

		/*
		 * if(null != claimByKey) {
		 * if((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
		 * .getLegalFlag())) { isValidClaimForBillEntry = false;
		 * view.showErrorPopUp
		 * ("Intimation is locked by legal process. Cannot proceed further"); }
		 * }
		 * 
		 * if(isValidClaimForBillEntry) {
		 */
		// Long count =
		// getAckNoCountByClaimKey(claimKey,ReferenceTable.CREATE_ROD);
		Long count = getAckNoCountByClaimKey(claimKey,
				ReferenceTable.CREATE_ROD);
		// based on ack no, fetch the data from document acknowledge table. Ack
		// no will as a part of primary parameter.
		rodDTO.setAcknowledgementNumber(count);
		ClaimDto claimDTO = null;
		// rodDTO.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
		NewIntimationDto newIntimationDto = new NewIntimationDto();
		if (claimByKey != null) {
			// setClaimValuesToDTO(preauthDTO, claimByKey);
			newIntimationDto = intimationService.getIntimationDto(claimByKey
					.getIntimation());
			claimDTO = ClaimMapper.getInstance().getClaimDto(claimByKey);
			if (claimDTO.getClaimType() != null
					&& claimDTO.getClaimType().getId()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				Preauth latestPreauthByClaim = preauthService
						.getLatestPreauthByClaim(claimDTO.getKey());
				claimDTO.setCashlessAppAmt(latestPreauthByClaim
						.getTotalApprovalAmount());
			}
			claimDTO.setRodKeyForDischargeDate(rodKey);
			claimDTO.setNewIntimationDto(newIntimationDto);
			rodDTO.setClaimDTO(claimDTO);
			claimDTO.getNewIntimationDto().setPolicy(claimByKey.getIntimation().getPolicy());
		}
		// Method to populate hospital values from VW_HOSPITALS;
		/*
		 * if(null != rodDTO.getClaimDTO() &&
		 * (ReferenceTable.CLAIM_TYPE_CASHLESS
		 * ).equalsIgnoreCase(rodDTO.getClaimDTO().getClaimTypeValue())) {
		 * populatePaymentDetailsForCashLessClaim
		 * (newIntimationDto.getHospitalDto().getKey(),rodDTO); } else {
		 * populatePaymentDetailsForReimbursementClaim
		 * (rodDTO.getClaimDTO().getKey
		 * (),rodDTO,newIntimationDto.getHospitalDto().getKey()); }
		 */

		getPreviousRODNumber(rodDTO);

		Map<String, Integer> productBenefitMap = dbCalculationService
				.getProductBenefitFlag(claimByKey.getKey(), claimByKey
						.getIntimation().getPolicy().getProduct().getKey());
		/**
		 * Earlier , pre and post hospitalization was enabled only based on
		 * product conditions in ack , rod level and bill entry level. But as
		 * per mockup 13.7 , this validation needs to be removed at ack and rod
		 * level and should be available only at FA level. Hence hardcoding the
		 * values of pre and post in map , by which we always enable pre and
		 * post for all products at ACK and ROD level and bil entry level.
		 * */
		productBenefitMap.put("preHospitalizationFlag", 1);
		productBenefitMap.put("postHospitalizationFlag", 1);

		rodDTO.setProductBenefitMap(productBenefitMap);
		
		createRodService.getBillClassificationFlagDetails(rodDTO.getClaimDTO()
				.getKey(), rodDTO);
		/*
		 * rodDTO.setProductBenefitMap(dbCalculationService.getProductBenefitFlag
		 * ( claimByKey.getKey(), claimByKey.getIntimation().getPolicy()
		 * .getProduct().getKey()));
		 */

		rodDTO.setStrUserName(tableDTO.getUsername());
		rodDTO.setStrPassword(tableDTO.getPassword());

		Boolean isQuery = false;
		//if (("Yes").equalsIgnoreCase(rodDTO.getDocumentDetails()
			//	.getReconsiderationRequestValue()))
		
		if(null != rodDTO.getDocumentDetails().getDocumentTypeId() && (ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(rodDTO.getDocumentDetails().getDocumentTypeId()))
		{
		{
			List<ReconsiderRODRequestTableDTO> reconsiderList = getReconsiderRODRequestForBillEntry(rodKey);
			if (null != reconsiderList && !reconsiderList.isEmpty()) {
				rodDTO.setReconsiderRodRequestList(reconsiderList);
			}

		}
		}
		rodDTO.setRodQueryDetailsList(getRODQueryDetailsForCreateRodandBillEntry(
				claimByKey, ackKey));
		List<RODQueryDetailsDTO> rodQueryDetailsList = rodDTO
				.getRodQueryDetailsList();
		if (rodDTO.getRodQueryDetailsList() != null) {
			for (RODQueryDetailsDTO rodQuery : rodQueryDetailsList) {
				if (rodQuery.getReplyStatus() != null
						&& ("Y").equalsIgnoreCase(rodQuery.getReplyStatus())) {
					isQuery = true;
				}
			}
		}

		rodDTO.setPaymentQueryDetailsList(getRODPaymentQueryDetailsForCreateRodandBillEntry(claimByKey, ackKey));
		List<RODQueryDetailsDTO> rodPaymentQueryDetailsList = rodDTO.getPaymentQueryDetailsList();
		if (rodDTO.getPaymentQueryDetailsList() != null) {
			for (RODQueryDetailsDTO rodQuery : rodPaymentQueryDetailsList) {
				if (rodQuery.getReplyStatus() != null
						&& ("Y").equalsIgnoreCase(rodQuery.getReplyStatus())) {
					isQuery = true;
				}
			}
		}
		List<UploadDocumentDTO> rodSummaryDetails = rodService
				.getRODSummaryDetails(rodKey);
		/**
		 * Fix for implemeting claims dms in bill entry screen.. -- starts
		 * **/
		if (null != rodSummaryDetails && !rodSummaryDetails.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				if (null != claimByKey) {
					uploadDocumentDTO.setClaimNo(claimByKey.getClaimId());
					uploadDocumentDTO
							.setDmsDocumentDTOList(getClaimsDMSList(claimByKey
									.getIntimation().getIntimationId()));
				}
			}
		}
		/**
		 * Fix for implemeting claims dms in bill entry screen.. -- ends
		 * **/
		Reimbursement objReimbursement = createRodService
				.getReimbursementObjectByKey(rodKey);
		if (null != objReimbursement) {
			rodDTO.getDocumentDetails().setDateOfDischarge(
					objReimbursement.getDateOfDischarge());
			if (null != rodSummaryDetails && !rodSummaryDetails.isEmpty()) {
				// if(null != objReimbursement)
				{
					rodDTO.setIntimationNo(objReimbursement.getClaim()
							.getIntimation().getIntimationId());
					rodDTO.setDateOfAdmission(SHAUtils
							.formatDate(objReimbursement.getDateOfAdmission()));
					rodDTO.setDateOfDischarge(SHAUtils
							.formatDate(objReimbursement.getDateOfDischarge()));
					rodDTO.setInsuredPatientName(objReimbursement.getClaim()
							.getIntimation().getInsuredPatientName());
				}
				/*
				 * for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails)
				 * {
				 * 
				 * if(null != objReimbursement) {
				 * uploadDocumentDTO.setIntimationNo
				 * (objReimbursement.getClaim().
				 * getIntimation().getIntimationId());
				 * uploadDocumentDTO.setDateOfAdmission
				 * (SHAUtils.formatDate(objReimbursement.getDateOfAdmission()));
				 * uploadDocumentDTO
				 * .setDateOfDischarge(SHAUtils.formatDate(objReimbursement
				 * .getDateOfDischarge()));
				 * uploadDocumentDTO.setInsuredPatientName
				 * (objReimbursement.getClaim
				 * ().getIntimation().getInsuredPatientName());
				 * 
				 * 
				 * }
				 * 
				 * }
				 */
			}
		}
		
		if (objReimbursement != null) {
			rodDTO.setStageKey(objReimbursement.getStage().getKey());
			rodDTO.setStatusKey(objReimbursement.getStatus().getKey());
		}

		String diagnosisForPreauthByKey = "";
		if (objReimbursement != null) {
			diagnosisForPreauthByKey = preauthService
					.getDiagnosisForPreauthByKey(objReimbursement.getKey());

		}
		rodDTO.setDiagnosis(diagnosisForPreauthByKey);

		if (isQuery) {
			// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
			List<Long> documentSummaryKeys = new ArrayList<Long>();
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
				uploadDocumentDTO.setStatus(true);
				List<RODBillDetails> billEntryDetails = rodService
						.getBillEntryDetails(uploadDocumentDTO
								.getDocSummaryKey());
				List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
				if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
					for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
						/*
						 * <<<<<<< HEAD
						 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
						 * uploadDocumentDTO)); =======
						 */

						dtoList.add(getBillDetailsDTOForBilling(
								billEntryDetailsDO, uploadDocumentDTO));
						// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
					}
				}

				uploadDocumentDTO.setStatus(false);
				uploadDocumentDTO.setBillEntryDetailList(dtoList);

				if (uploadDocumentDTO != null) {
					if ((ReferenceTable.BENEFITS_PATIENT_CARE)
							.equalsIgnoreCase(uploadDocumentDTO
									.getPatientCareBenefitFlag())) {
						List<PatientCareDTO> patientCareList = claimRequestBenefitsService
								.getPatientCareDetails(uploadDocumentDTO
										.getPatientBenefitKey());
						if (null != patientCareList
								&& !patientCareList.isEmpty()) {
							uploadDocumentDTO
									.setPatientCareDTO(patientCareList);
						}
						// rodDTO.getAddOnBenefitsDTO().setAdmittedNoOfDays(uploadDocumentDTO.getPatientCareNoofDays());
					}
				}
			}

			rodDTO.setUploadDocsList(rodSummaryDetails);

		} else {

			// if(null != rodDTO.getUploadDocsList() &&
			// !rodDTO.getUploadDocsList().isEmpty())
			if (null != rodSummaryDetails && !rodSummaryDetails.isEmpty()) {
				/*
				 * List<BillEntryDetailsDTO> dtoList = new
				 * ArrayList<BillEntryDetailsDTO>();
				 */
				// List<UploadDocumentDTO> uploadDocsList =
				// rodDTO.getUploadDocsList();
				for (UploadDocumentDTO uploadDocDTO : rodSummaryDetails) {
					// sss
					uploadDocDTO.setIsBillSaved(true);
					List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
					List<RODBillDetails> billEntryDetails = rodService
							.getBillEntryDetails(uploadDocDTO
									.getDocSummaryKey());
					if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
						for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
							/*
							 * <<<<<<< HEAD
							 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
							 * uploadDocumentDTO)); =======
							 */
							dtoList.add(getBillDetailsDTOForBillEntry(billEntryDetailsDO));

							// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
						}
						uploadDocDTO.setBillEntryDetailList(dtoList);
					}
				}
				// rodDTO.getUploadDocumentsDTO().setBillEntryDetailList(dtoList);
				rodDTO.setUploadDocsList(rodSummaryDetails);

			}
		}

		rodDTO.setCheckListTableContainerForROD(masterService
				.getDocumentCheckListValuesContainer(SHAConstants.MASTER_TYPE_PA));

		RRCDTO rrcDTO = new RRCDTO();
		rrcDTO.setClaimDto(claimDTO);
		rrcDTO.setNewIntimationDTO(newIntimationDto);
		rrcDTO.setStrUserName(tableDTO.getUsername());
		
		Double insuredSumInsured = 0d;

		if(!ReferenceTable.getGPAProducts().equals(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
		 
			insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				rrcDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()
						.toString(), rrcDTO.getNewIntimationDTO().getPolicy()
						.getKey(),rrcDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					rrcDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()
							.toString(), rrcDTO.getNewIntimationDTO().getPolicy()
							.getKey());
		}
		loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured,
				SHAConstants.BILL_ENTRY);

		// loadRRCRequestValuesForProcessing(rrcDTO,insuredSumInsured,SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);

		rodDTO.setRrcDTO(rrcDTO);

		rodDTO.getUploadDocumentsDTO().setClaimType(
				rodDTO.getClaimDTO().getClaimType());
		if (null != rrcDTO.getNewIntimationDTO()
				&& null != rrcDTO.getNewIntimationDTO().getPolicy()) {
			rodDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(rrcDTO
					.getNewIntimationDTO().getPolicy().getKey()));
			rodDTO.setIsPEDInitiatedbtn(rodDTO.getIsPEDInitiated());
		}
		UploadDocumentDTO uploadDocDTO = new UploadDocumentDTO();
		uploadDocDTO.setUsername(tableDTO.getUsername());

		DocAcknowledgement previousAcknowledgmentDetails = reimbursementService
				.getPreviousAcknowledgmentDetails(claimKey);
		if (previousAcknowledgmentDetails != null) {
			rodDTO.setIsAlreadyHospitalizationExist(true);
		}

		// if(rrcDTO.getNewIntimationDTO() != null &&
		// rrcDTO.getNewIntimationDTO().getInsuredPatient() != null &&
		// rrcDTO.getNewIntimationDTO().getInsuredPatient().getKey() != null){
		// Long claimCount =
		// preauthService.getClaimCount(rrcDTO.getNewIntimationDTO().getPolicy().getKey());
		// if(claimCount != null){
		// rodDTO.setClaimCount(claimCount);
		// }
		// }
		Long productCode = rodDTO.getClaimDTO().getNewIntimationDto()
				.getPolicy().getProduct().getKey();
		Long insuredKey = rodDTO.getClaimDTO().getNewIntimationDto()
				.getInsuredPatient().getKey();

		if (null != productCode && null != insuredKey) {
			rodDTO.getDocumentDetails().setAdditionalCovers(
					dbCalculationService.getBebefitAdditionalCovers(
							SHAConstants.ADDITIONAL_COVER, productCode,
							insuredKey));

			rodDTO.getDocumentDetails().setOptionalCovers(
					dbCalculationService.getBebefitAdditionalCovers(
							SHAConstants.OPTIONAL_COVER, productCode,
							insuredKey));
		}

		
		if(ReferenceTable.getGPAProducts().containsKey(rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
			
			if (null != productCode && null != insuredKey) {
				rodDTO.getDocumentDetails().setAdditionalCovers(
						dbCalculationService.getBebefitAdditionalCovers(
								SHAConstants.GPA_ADDITIONAL_COVER, productCode,
								insuredKey));
				
				rodDTO.getDocumentDetails().setOptionalCovers(
						dbCalculationService.getBebefitAdditionalCovers(
								SHAConstants.GPA_OPTIONAL_COVER, productCode,
								insuredKey));
			}
		}
		
		List<AddOnCoversTableDTO> addOnCoversList = createRodService
				.getAddOnCoversValueBasedOnROD(rodKey);
		if (null != addOnCoversList && !addOnCoversList.isEmpty()) {
			rodDTO.getDocumentDetails().setAddOnCoversList(addOnCoversList);
		}

		List<AddOnCoversTableDTO> optionalCoversList = createRodService
				.getOpitionalCoversValueBasedOnROD(rodKey);
		if (null != optionalCoversList && !optionalCoversList.isEmpty()) {
			rodDTO.getDocumentDetails().setOptionalCoversList(
					optionalCoversList);
		}
		
		rodDTO.getDocumentDetails().setStatusId(objReimbursement.getStatus().getKey());
	//	rodDTO.getDocumentDetails().setStatusId(objReimbursement.getStage().getKey());
		if(null != objReimbursement && null != objReimbursement.getWorkPlace() &&
				(SHAConstants.YES_FLAG.equalsIgnoreCase(objReimbursement.getWorkPlace()))){
		rodDTO.getDocumentDetails().setWorkOrNonWorkPlace(true);
		}
		
		rodDTO.setDbOutArray(tableDTO.getDbOutArray());
		view.setPABillEntryWizard(PAEnterBillDetailsWizardView.class, rodDTO);
		// }

	}
	}

	protected void showProcessClaimRequest(
			@Observes @CDIEvent(MenuItemBean.PA_PROCESS_CLAIM_REQUEST) final ParameterDTO parameters) {
		SearchProcessClaimRequestFormDTO dto = null;
		if (parameters.getPrimaryParameter() != null
				&& (parameters.getPrimaryParameter() instanceof SearchProcessClaimRequestFormDTO)) {
			dto = (SearchProcessClaimRequestFormDTO) parameters
					.getPrimaryParameter();
		}
		BeanItemContainer<SelectValue> insuranceSource = masterService
				.getMasterValueByReference(ReferenceTable.INTIMATION_SOURCE);
		BeanItemContainer<SelectValue> networkHospitalType = masterService
				.getMasterValueByReference(ReferenceTable.NETWORK_HOSPITAL_TYPE);
		BeanItemContainer<SelectValue> hospitalType = masterService
				.getMasterValueByReference(ReferenceTable.HOSPITAL_TYPE);
		BeanItemContainer<SelectValue> treatmentType = masterService
				.getMasterValueByReference(ReferenceTable.TREATMENT_MANAGEMENT);

		BeanItemContainer<SelectValue> productName = masterService
				.getSelectValueContainerForProductNameAndCode(SHAConstants.PA_LOB);
		BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();

		BeanItemContainer<SelectValue> selectValueContainerForType = masterService
				.getStageList();

		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
				.getSelectValueForPriority();

		// BeanItemContainer<SelectValue> statusByStage =
		// masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);

		Stage stageByKey = preauthService
				.getStageByKey(ReferenceTable.ZONAL_REVIEW_STAGE);
		Status statusByKey2 = preauthService
				.getStatusByKey(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER);
		Status statuseByKey3 = preauthService
				.getStatusByKey(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);
		Status statusByKey4 = preauthService
				.getStatusByKey(ReferenceTable.CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS);
		Status statusByKey5 = preauthService
				.getStatusByKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS);
		Status statusByKey6 = preauthService
				.getStatusByKey(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);
		Status statusByKey7 = preauthService
				.getStatusByKey(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
		Status statusByKey8 = preauthService
				.getStatusByKey(ReferenceTable.CLAIM_REQUEST_QUERY_RECEIVED_STATUS);
		// Status statusByKey9 =
		// preauthService.getStatusByKey(ReferenceTable.FA_QUERY_REPLY_STATUS);

		SelectValue selectValue = new SelectValue();
		selectValue.setId(stageByKey.getKey());
		selectValue.setValue(stageByKey.getStageName());

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(statusByKey2.getKey());
		selectValue1.setValue("Billing- " + statusByKey2.getProcessValue());

		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(statuseByKey3.getKey());
		selectValue2.setValue("FA- " + statuseByKey3.getProcessValue());

		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(statusByKey4.getKey());
		selectValue3.setValue(statusByKey4.getProcessValue());

		SelectValue selectValue4 = new SelectValue();
		selectValue4.setId(statusByKey5.getKey());
		selectValue4.setValue(statusByKey5.getProcessValue());

		SelectValue selectValue5 = new SelectValue();
		selectValue5.setId(statusByKey6.getKey());
		selectValue5.setValue(statusByKey6.getProcessValue());

		SelectValue selectValue6 = new SelectValue();
		selectValue6.setId(statusByKey7.getKey());
		selectValue6.setValue(statusByKey7.getProcessValue());

		SelectValue selectValue7 = new SelectValue();
		selectValue7.setId(statusByKey8.getKey());
		selectValue7.setValue(SHAConstants.QUERY_REPLY);

		SelectValue selectValue8 = new SelectValue();
		selectValue8.setId(0l);
		selectValue8.setValue(SHAConstants.RECONSIDERATION);

		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		statusByStage.addBean(selectValue);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
		statusByStage.addBean(selectValue3);
		statusByStage.addBean(selectValue4);
		statusByStage.addBean(selectValue5);
		statusByStage.addBean(selectValue6);
		statusByStage.addBean(selectValue7);
		statusByStage.addBean(selectValue8);

		view.setViewsPAProcessClaimRequest(
				PASearchProcessClaimRequestView.class, true, insuranceSource,
				hospitalType, networkHospitalType, treatmentType,
				selectValueContainerForType, productName, cpuCode,
				selectValueForPriority, statusByStage, dto,SHAConstants.PA_MEDICAL_SCREEN);
	}

//	Commented the below Cashless Screen
//	protected void showSearchPreAuthView(
//			@Observes @CDIEvent(MenuItemBean.PA_PROCESS_PREAUTH) final ParameterDTO parameters) {
//		BeanItemContainer<SelectValue> selectValueContainerForIntimationSource = masterService
//				.getSelectValueContainer(ReferenceTable.INTIMATION_SOURCE);
//		BeanItemContainer<SelectValue> selectValueContainerForNetworkHospType = masterService
//				.getMasterValueByReference(ReferenceTable.NETWORK_HOSPITAL_TYPE);
//		BeanItemContainer<SelectValue> selectValueContainerForTreatmentType = masterService
//				.getSelectValueContainer(ReferenceTable.TREATMENT_MANAGEMENT);
//		BeanItemContainer<SelectValue> specialityContainer = preauthService
//				.getSpecialistTypeList();
//
//		BeanItemContainer<SelectValue> selectValueContainerForType = masterService
//				.getStageList();
//
//		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
//				.getTmpCpuCodes();
//
//		view.setViewGForPA(PASearchPreauthView.class, true,
//				selectValueContainerForIntimationSource,
//				selectValueContainerForNetworkHospType,
//				selectValueContainerForTreatmentType,
//				selectValueContainerForType, specialityContainer,
//				selectValueContainerForCPUCode, SHAConstants.PROCESS_PREAUTH);
//	}
	
//	Commented the below Cashless Screen
//	protected void showPreMedicalView(
//			@Observes @CDIEvent(MenuItemBean.PA_FIRST_LEVEL_PROCESSING_PREMEDICAL) final ParameterDTO parameters) {
//
//		BeanItemContainer<SelectValue> selectValueContainerForIntimationSource = masterService
//				.getSelectValueContainer(ReferenceTable.INTIMATION_SOURCE);
//
//		BeanItemContainer<SelectValue> selectValueContainerForNetworkHospType = masterService
//				.getMasterValueByReference(ReferenceTable.NETWORK_HOSPITAL_TYPE);
//
//		BeanItemContainer<SelectValue> selectValueContainerForType = masterService
//				.getStageList();
//
//		BeanItemContainer<SelectValue> specialityContainer = preauthService
//				.getSpecialistTypeList();
//
//		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
//				.getTmpCpuCodes();
//		// statusByStage.addBean(selectValue2);
//
//		view.setViewGForPA(PAProcessPreMedicalView.class, true,
//				selectValueContainerForIntimationSource,
//				selectValueContainerForNetworkHospType, null,
//				selectValueContainerForType, specialityContainer,
//				selectValueContainerForCPUCode,
//				SHAConstants.PROCESS_PRE_MEDICAL);
//	}

//	Commented the below Cashless Screen
//	protected void showPreMedicalProcessingEnhancement(
//			@Observes @CDIEvent(MenuItemBean.PA_FIRST_LEVEL_PROCESSING_PREMEDICAL_ENHANCEMENT) final ParameterDTO parameters) {
//		BeanItemContainer<SelectValue> selectValueContainerForIntimationSource = masterService
//				.getSelectValueContainer(ReferenceTable.INTIMATION_SOURCE);
//
//		BeanItemContainer<SelectValue> selectValueContainerForNetworkHospType = masterService
//				.getMasterValueByReference(ReferenceTable.NETWORK_HOSPITAL_TYPE);
//
//		BeanItemContainer<SelectValue> selectValueContainerForType = masterService
//				.getStageList();
//
//		BeanItemContainer<SelectValue> specialityContainer = preauthService
//				.getSpecialistTypeList();
//
//		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
//				.getTmpCpuCodes();
//
//		view.setViewGForPA(PASearchPreMedicalProcessingEnhancementView.class,
//				true, selectValueContainerForIntimationSource,
//				selectValueContainerForNetworkHospType, null,
//				selectValueContainerForType, specialityContainer,
//				selectValueContainerForCPUCode,
//				SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT);
//	}

//	Commented the below Cashless Screen
//	protected void showEnhancementView(
//			@Observes @CDIEvent(MenuItemBean.PA_PROCESS_ENHANCEMENT) final ParameterDTO parameters) {
//
//		BeanItemContainer<SelectValue> selectValueContainerForIntimationSource = masterService
//				.getSelectValueContainer(ReferenceTable.INTIMATION_SOURCE);
//
//		BeanItemContainer<SelectValue> selectValueContainerForNetworkHospType = masterService
//				.getMasterValueByReference(ReferenceTable.NETWORK_HOSPITAL_TYPE);
//
//		BeanItemContainer<SelectValue> selectValueContainerForTreatmentType = masterService
//				.getSelectValueContainer(ReferenceTable.TREATMENT_MANAGEMENT);
//
//		BeanItemContainer<SelectValue> selectValueContainerForType = masterService
//				.getStageList();
//
//		BeanItemContainer<SelectValue> specialityContainer = preauthService
//				.getSpecialistTypeList();
//
//		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
//				.getTmpCpuCodes();
//
//		view.setViewGForPA(PASearchEnhancementView.class, true,
//				selectValueContainerForIntimationSource,
//				selectValueContainerForNetworkHospType,
//				selectValueContainerForTreatmentType,
//				selectValueContainerForType, specialityContainer,
//				selectValueContainerForCPUCode,
//				SHAConstants.PROCESS_ENHANCEMENT);
//	}

//	Commented the below Cashless Screen
//	protected void showSearchWithDrawPreauthView(
//			@Observes @CDIEvent(MenuItemBean.PA_STANDALONE_WITHDRAW) final ParameterDTO parameters) {
//		view.setViewG(PASearchWithdrawCashLessProcessView.class, true);
//	}

//	Commented the below Cashless Screen
//	protected void showSearchDownSizePreauthView(
//			@Observes @CDIEvent(MenuItemBean.PA_STANDALONE_DOWNSIZE) final ParameterDTO parameters) {
//		view.setViewG(PASearchDownsizeCashLessProcessView.class, true);
//	}

//	Commented the below Cashless Screen
//	protected void showSearchProcessDownSizePreauthView(
//			@Observes @CDIEvent(MenuItemBean.PA_PROCESS_DOWNSIZE) final ParameterDTO parameters) {
//		view.setViewG(PASearchDownsizeRequestView.class, true);
//	}

	protected void showdownSizePreauthView(
			@Observes @CDIEvent(PAMenuPresenter.PA_DOWNSIZE_PREAUTH_PAGE_VIEW) final ParameterDTO parameters) {

		PASearchWithdrawCashLessProcessTableDTO tableDTO = (PASearchWithdrawCashLessProcessTableDTO) parameters
				.getPrimaryParameter();

		PreMedicalMapper premedicalMapper = PreMedicalMapper.getInstance();
		// PreMedicalMapper.getAllMapValues();

		Preauth preauthList = preauthService.getPreauthById(tableDTO.getKey());

		Preauth preauth = preauthService.getPreauthById(tableDTO.getKey());

		String[] split = preauth.getPreauthId().split("/");
		Integer seqNumber = Integer.valueOf(split[split.length - 1]);

		Long preauthKey = preauth.getKey();

		PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
		setpreauthTOPreauthDTO(premedicalMapper, preauth.getClaim(), preauth,
				preauthDTO, false);
	//	preauthDTO.setRodHumanTask(tableDTO.getHumanTask());

		if (preauthDTO.getPreauthDataExtractionDetails()
				.getDiagnosisTableList() != null) {
			List<DiagnosisDetailsTableDTO> diagnosisTableList = preauthDTO
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisTableList) {
				List<ExclusionDetails> exclusionDetailsList = masterService
						.getExclusionDetailsList();
				if (diagnosisDetailsTableDTO.getPedList() != null) {
					for (PedDetailsTableDTO pedDetail : diagnosisDetailsTableDTO
							.getPedList()) {
						pedDetail.setExclusionAllDetails(exclusionDetailsList);
					}
				}
			}
		}
		
		Double insuredSumInsured = 0d;

		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){			
		
		 insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO
						.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			 insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					preauthDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), preauthDTO
							.getNewIntimationDTO().getPolicy().getKey());
		}

		// Double balanceSI =
		// dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey()
		// ,intimationDTO.getInsuredPatient().getKey(),
		// intimationDTO.getPolicy().getTotalSumInsured());
		// Double balanceSI =
		// dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey()
		// ,intimationDTO.getInsuredPatient().getKey(),
		// Double.valueOf(insuredSumInsured.toString()));
		//IMSSUPPOR-30346 - BalanceSI already called with different procedure
		/*Double balanceSI = 0d;
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
		
			balanceSI = dbCalculationService.getBalanceSI(
				preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
				preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				preauthDTO.getClaimKey(), insuredSumInsured,
				preauthDTO.getNewIntimationDTO().getKey()).get(
				SHAConstants.TOTAL_BALANCE_SI);
		}
		else
		{
			balanceSI = dbCalculationService.getGPABalanceSI(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey(), insuredSumInsured,
					preauthDTO.getNewIntimationDTO().getKey()).get(
					SHAConstants.TOTAL_BALANCE_SI);
		}
		preauthDTO.setBalanceSI(balanceSI);*/
		List<Double> copayValue = dbCalculationService.getProductCoPay(
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey(), preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getKey(), preauthDTO
						.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId(),preauthDTO.getNewIntimationDTO());
		preauthDTO.setProductCopay(copayValue);

		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getConversionReasonByValue(ReferenceTable.DOWNSIZE_REASON);

		BeanItemContainer<SelectValue> escalateContainer = masterService
				.getConversionReasonByValue(ReferenceTable.ESCALATE_TO);

		Claim claim = preauthList.getClaim();

		List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
				.search(claim.getKey(), false);

		// List<Preauth> preauthByClaimKey = preauthService
		// .getPreauthByClaimKey(claim.getKey());
		//
		// Preauth previousPreauthDetails = new Preauth();
		// if (!preauthByClaimKey.isEmpty()) {
		// for (Preauth previousPreauth : preauthByClaimKey) {
		// String[] preauthSplit = previousPreauth.getPreauthId().split(
		// "/");
		// Integer previousSeqNumber = Integer
		// .valueOf(preauthSplit[preauthSplit.length - 1]);
		// if (previousSeqNumber.equals(seqNumber - 1)) {
		// previousPreauthDetails = previousPreauth;
		// preauthDTO.setPreviousPreauthKey(previousPreauth.getKey());
		// List<ClaimAmountDetails> claimAmountDetailsByPreauth = preauthService
		// .getClaimAmountDetailsByPreauth(previousPreauth
		// .getKey());
		// Float amount = 0f;
		// for (ClaimAmountDetails claimAmountDetails :
		// claimAmountDetailsByPreauth) {
		// if (claimAmountDetails.getPaybleAmount() != null) {
		// amount += claimAmountDetails.getPaybleAmount();
		// }
		// }
		//
		// preauthDTO.setPreviousPreauthPayableAmount(previousPreauth
		// .getTotalApprovalAmount() != null ? previousPreauth
		// .getTotalApprovalAmount().intValue() : 0);
		// break;
		// }
		// }
		// }

		preauthDTO.setPreviousPreauthKey(preauthKey);

		preauthDTO.setStrUserName(tableDTO.getUsername());

		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
			// if ((tableDTO.getKey() != null && tableDTO.getKey().equals(
			// previousPreAuthTableDTO.getKey()))) {
			previousPreAuthTableDTO.setRequestedAmt(preauthService
					.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
							previousPreAuthTableDTO.getClaimKey()));
			newList.add(previousPreAuthTableDTO);
			// }
		}

		preauthDTO.setPreviousPreauthTableDTO(newList);

		List<Preauth> preauthByClaimKey = preauthService
				.getPreauthByClaimKey(preauth.getClaim().getKey());

		for (Preauth preauth2 : preauthByClaimKey) {
			preauthDTO.setReferenceType(preauth2.getPreauthId());
		}

		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA4(
				tableDTO.getIsCheifMedicalOfficer());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA3(
				tableDTO.getIsZonalMedicalHead());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA2(
				tableDTO.getIsZonalSeniorMedicalApprover());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA1(
				tableDTO.getIsZonalMedicalApprover());

		setClaimValuesToDTO(preauthDTO, claim);

		preauthDTO.setPreviousPreauthKey(preauthKey);

		if (preauth.getTotalApprovalAmount() != null) {
			preauthDTO.setPreviousPreauthPayableAmount(preauth
					.getTotalApprovalAmount().intValue());
		}

		preauthDTO.setStrUserName(tableDTO.getUsername());
		loadRRCRequestValues(preauthDTO, insuredSumInsured,
				SHAConstants.PROCESS_DOWNSIZE_PREAUTH);
		preauthDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(preauthDTO
				.getNewIntimationDTO().getPolicy().getKey()));
		preauthDTO.setIsPEDInitiatedForBtn(preauthDTO.getIsPEDInitiated());
		
		if(null != preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode()){
			
			List<MasOmbudsman> ombudsmanDetailsList = getOmbudsmanOffiAddrByPIOCode(preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode());
			
			if(ombudsmanDetailsList != null &&  !ombudsmanDetailsList.isEmpty())
				preauthDTO.getClaimDTO().setOmbudsManAddressList(ombudsmanDetailsList);
			MasterGST masGstObj = masterService.getGSTByStateId(preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode());

			if(null != masGstObj){
	
				preauthDTO.setGstNumber(masGstObj.getGstNumber());
				preauthDTO.setArnNumber(masGstObj.getArnNumber());
				preauthDTO.setGstState(masGstObj.getStateName());
			}
		}
		
		view.setPADownsizePreauth(PADownsizePreauthWizard.class, preauthDTO,
				selectValueContainer, escalateContainer);

	}

	private void loadRRCRequestValues(PreauthDTO preauthDTO, Double sumInsured,
			String stage) {
		RRCDTO requestRRCDTO = new RRCDTO();

		if (null != preauthDTO.getClaimDTO().getNewIntimationDto()) {
			// load policy data

			String policyNumber = preauthDTO.getNewIntimationDTO().getPolicy()
					.getPolicyNumber();
			requestRRCDTO.setPolicyNo(policyNumber);
			requestRRCDTO.setIntimationNo(preauthDTO.getNewIntimationDTO()
					.getIntimationId());
			requestRRCDTO.setProductName(preauthDTO.getNewIntimationDTO()
					.getProductName());
			// Duration on hold
			Date admissionDate = preauthDTO.getNewIntimationDTO()
					.getAdmissionDate();
			// String duration =
			// dbCalculationService.getPolicyAgeing(admissionDate,
			// policyNumber);
			String duration = preauthDTO.getNewIntimationDTO().getPolicyYear();

			requestRRCDTO.setDuration(duration);
			requestRRCDTO.setSumInsured(sumInsured);

			// load Hospital data
			requestRRCDTO.setHospitalName(preauthDTO.getNewIntimationDTO()
					.getHospitalDto().getName());
			requestRRCDTO.setHospitalCity(preauthDTO.getNewIntimationDTO()
					.getHospitalDto().getCity());
			// if(null !=
			// preauthDTO.getClaimDTO().getNewIntimationDto().getHospitalDto().getRegistedHospitals())
			requestRRCDTO.setHospitalZone(preauthDTO.getNewIntimationDTO()
					.getHospitalDto().getHospitalZone());
			requestRRCDTO.setDateOfAdmission(admissionDate);
			if (null != preauthDTO.getPreauthDataExtractionDetails()
					&& null != preauthDTO.getPreauthDataExtractionDetails()
							.getDischargeDate())
				requestRRCDTO.setDateOfDischarge(preauthDTO
						.getPreauthDataExtractionDetails().getDischargeDate());

			// load Insured data
			requestRRCDTO.setInsuredName(preauthDTO.getNewIntimationDTO()
					.getInsuredPatient().getInsuredName());
			if (null != preauthDTO.getNewIntimationDTO().getInsuredPatient()
					.getInsuredAge())
				requestRRCDTO.setInsuredAge(preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredAge().longValue());
			if (null != preauthDTO.getNewIntimationDTO().getInsuredPatient()
					.getInsuredGender())
				requestRRCDTO.setSex(preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredGender().getValue());

			requestRRCDTO.setClaimType(preauthDTO.getClaimDTO()
					.getClaimTypeValue());
			requestRRCDTO.setProcessingStage(stage);

			TmpEmployee tmpEmployee = reimbursementService
					.getEmployeeDetails(preauthDTO.getStrUserName());
			if (null != tmpEmployee) {
				/*
				 * if(!(null != tmpEmployee.getEmpFirstName()))
				 * tmpEmployee.setEmpFirstName(""); else if(!(null !=
				 * tmpEmployee.getEmpMiddleName()))
				 * tmpEmployee.setEmpMiddleName(""); else if(!(null !=
				 * tmpEmployee.getEmpLastName()))
				 * tmpEmployee.setEmpLastName("");
				 */
				String strName = "";
				if (null != tmpEmployee.getEmpFirstName()) {
					strName = tmpEmployee.getEmpFirstName();
				}
				if (null != tmpEmployee.getEmpMiddleName()) {
					if (("").equalsIgnoreCase(strName)) {
						strName = tmpEmployee.getEmpMiddleName();
					} else {
						strName += tmpEmployee.getEmpMiddleName();
					}
				}
				if (null != tmpEmployee.getEmpLastName()) {
					if (("").equalsIgnoreCase(strName)) {
						strName = tmpEmployee.getEmpLastName();
					} else {
						strName += tmpEmployee.getEmpLastName();
					}
				}

				// requestRRCDTO.setEmployeeName(tmpEmployee.getEmpFirstName()+tmpEmployee.getEmpMiddleName()+tmpEmployee.getEmpLastName());
				requestRRCDTO.setEmployeeName(strName);
				requestRRCDTO.setEmployeeId(tmpEmployee.getEmpId());
			}
			// requestRRCDTO.setEmployeeNameList(getEmployeeNamesFromMaster());
			requestRRCDTO.setDataSourcesMap(getEmployeeNamesFromMaster());

			setRequestStageIdForRRC(requestRRCDTO, stage);
			/*
			 * ExtraEmployeeEffortDTO extraEffortEmpDTO = new
			 * ExtraEmployeeEffortDTO(); //EmployeeMasterDTO empMasterDTO = new
			 * EmployeeMasterDTO();
			 * //empMasterDTO.setEmployeeName(requestRRCDTO.getEmployeeName());
			 * extraEffortEmpDTO.setEmployeeId(requestRRCDTO.getEmployeeId());
			 * //
			 * extraEffortEmpDTO.setEmployeeName(requestRRCDTO.getEmployeeName(
			 * )); //extraEffortEmpDTO.setEmployeeNameDTO(empMasterDTO);
			 * 
			 * List<ExtraEmployeeEffortDTO> extraEffortDTOList = new
			 * ArrayList<ExtraEmployeeEffortDTO>();
			 * extraEffortDTOList.add(extraEffortEmpDTO);
			 * //requestRRCDTO.setEmployeeEffortList(extraEffortDTOList);
			 * 
			 * //requestRRCDTO.setEmployeeDept();
			 */preauthDTO.setRrcDTO(requestRRCDTO);

		}

	}

	protected void showWithdrawPreauthView(
			@Observes @CDIEvent(PAMenuPresenter.PA_WITHDRAW_PREAUTH_PAGE_VIEW) final ParameterDTO parameters) {

		PASearchWithdrawCashLessProcessTableDTO tableDTO = (PASearchWithdrawCashLessProcessTableDTO) parameters
				.getPrimaryParameter();

		Preauth preauthList = preauthService.getPreauthById(tableDTO.getKey());

		tableDTO.setPreauth(preauthList);

		PreMedicalMapper premedicalMapper = PreMedicalMapper.getInstance();
		// PreMedicalMapper.getAllMapValues();

		Long preauthKey = preauthList.getKey();

		PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauthList);
		setpreauthTOPreauthDTO(premedicalMapper, preauthList.getClaim(),
				preauthList, preauthDTO, false);

		if (preauthDTO.getPreauthDataExtractionDetails()
				.getDiagnosisTableList() != null) {
			List<DiagnosisDetailsTableDTO> diagnosisTableList = preauthDTO
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisTableList) {
				List<ExclusionDetails> exclusionDetailsList = masterService
						.getExclusionDetailsList();
				if (diagnosisDetailsTableDTO.getPedList() != null) {
					for (PedDetailsTableDTO pedDetail : diagnosisDetailsTableDTO
							.getPedList()) {
						pedDetail.setExclusionAllDetails(exclusionDetailsList);
					}
				}
			}
		}

		Intimation intimationByKey = intimationService
				.getIntimationByKey(preauthList.getIntimation().getKey());

		preauthDTO
				.setCpuProvisionAmt(intimationByKey.getCpuCode() != null ? intimationByKey
						.getCpuCode().getProvisionAmount() : null);

		preauthDTO.setStrUserName(tableDTO.getUsername());
		preauthDTO.setStrPassword(tableDTO.getPassword());

		tableDTO.setPreauthDto(preauthDTO);

		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getConversionReasonByValue(ReferenceTable.WITHDRAWAL_REASON);

		Claim claim = preauthList.getClaim();

		List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
				.search(claim.getKey(), false);

		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

			previousPreAuthTableDTO.setRequestedAmt(preauthService
					.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
							previousPreAuthTableDTO.getClaimKey()));
			newList.add(previousPreAuthTableDTO);

		}

		tableDTO.setPreviousPreAuthTableDTO(newList);

		tableDTO.setPolicyKey(preauthList.getPolicy().getKey());

		tableDTO.setClaimKey(preauthList.getClaim().getKey());

		ClaimDto claimDto = preauthDTO.getClaimDTO();
		claimDto.setClaimId(claim.getClaimId());
		MastersValue currency = claim.getCurrencyId();
		SelectValue currencyId = new SelectValue();
		currencyId.setId(currency.getKey());
		currencyId.setValue(currency.getValue());

		claimDto.setCurrencyId(currencyId);

		NewIntimationDto intimationDto = intimationService
				.getIntimationDto(preauthList.getIntimation());

		Double insuredSumInsured = 0d;
		if(null != intimationByKey &&  
		null != intimationByKey.getPolicy() && null != intimationByKey.getPolicy().getProduct() &&
		null != intimationByKey.getPolicy().getProduct().getKey() 
		&& !(ReferenceTable.getGPAProducts().containsKey(intimationByKey.getPolicy().getProduct().getKey())))
		{
		
		 insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			 insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						preauthDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), preauthDTO.getPolicyDto()
								.getKey());
		}

		List<Preauth> preauthByClaimKey = preauthService
				.getPreauthByClaimKey(preauthList.getClaim().getKey());

		for (Preauth preauth2 : preauthByClaimKey) {
			preauthDTO.setReferenceType(preauth2.getPreauthId());
		}

		preauthDTO.setPreviousPreauthKey(tableDTO.getKey());

		loadRRCRequestValues(preauthDTO, insuredSumInsured,
				SHAConstants.PROCESS_WITHDRAW_PREAUTH);
		preauthDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(preauthDTO
				.getNewIntimationDTO().getPolicy().getKey()));
		preauthDTO.setIsPEDInitiatedForBtn(preauthDTO.getIsPEDInitiated());

		view.setPAWithdrawPreauthView(PAWithdrawPreauthWizard.class, tableDTO,
				intimationDto, claimDto, selectValueContainer);

	}

	

	protected void showdownSizePreauthRequestView(
			@Observes @CDIEvent(PAMenuPresenter.PA_DOWNSIZE_PREAUTH_REQUEST_PAGE_VIEW) final ParameterDTO parameters) {

		PASearchWithdrawCashLessProcessTableDTO tableDTO = (PASearchWithdrawCashLessProcessTableDTO) parameters
				.getPrimaryParameter();

		PreMedicalMapper premedicalMapper = PreMedicalMapper.getInstance();
		// PreMedicalMapper.getAllMapValues();

		Preauth preauthList = preauthService.getPreauthById(tableDTO.getKey());

		Preauth preauth = preauthService.getPreauthById(tableDTO.getKey());

		Long preauthKey = preauth.getKey();

		PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
		setpreauthTOPreauthDTO(premedicalMapper, preauth.getClaim(), preauth,
				preauthDTO, false);
		//preauthDTO.setRodHumanTask(tableDTO.getHumanTask());

		if (preauthDTO.getPreauthDataExtractionDetails()
				.getDiagnosisTableList() != null) {
			List<DiagnosisDetailsTableDTO> diagnosisTableList = preauthDTO
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisTableList) {
				List<ExclusionDetails> exclusionDetailsList = masterService
						.getExclusionDetailsList();
				if (diagnosisDetailsTableDTO.getPedList() != null) {
					for (PedDetailsTableDTO pedDetail : diagnosisDetailsTableDTO
							.getPedList()) {
						pedDetail.setExclusionAllDetails(exclusionDetailsList);
					}
				}
			}
		}
		Double insuredSumInsured = 0d;
		
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			
		 insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO
						.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					preauthDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), preauthDTO
							.getNewIntimationDTO().getPolicy().getKey());
		}

		// Double balanceSI =
		// dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey()
		// ,intimationDTO.getInsuredPatient().getKey(),
		// intimationDTO.getPolicy().getTotalSumInsured());
		// Double balanceSI =
		// dbCalculationService.getBalanceSI(intimationDTO.getPolicy().getKey()
		// ,intimationDTO.getInsuredPatient().getKey(),
		// Double.valueOf(insuredSumInsured.toString()));

		Double balanceSI = 0d;
		if(null != preauthDTO.getNewIntimationDTO() && null != preauthDTO.getNewIntimationDTO().getPolicy() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct() && 
				null != preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
		
			balanceSI = dbCalculationService.getBalanceSI(
				preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
				preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				preauthDTO.getClaimKey(), insuredSumInsured,
				preauthDTO.getNewIntimationDTO().getKey()).get(
				SHAConstants.TOTAL_BALANCE_SI);
		}
		else
		{
			balanceSI = dbCalculationService.getGPABalanceSI(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey(), insuredSumInsured,
					preauthDTO.getNewIntimationDTO().getKey()).get(
					SHAConstants.TOTAL_BALANCE_SI);
		}
		
		
		List<Double> copayValue = dbCalculationService.getProductCoPay(
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey(), preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getKey(), preauthDTO
						.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId(),preauthDTO.getNewIntimationDTO());
		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);

		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getConversionReasonByValue(ReferenceTable.DOWNSIZE_REASON);

		BeanItemContainer<SelectValue> escalateContainer = masterService
				.getConversionReasonByValue(ReferenceTable.ESCALATE_TO);

		Claim claim = preauthList.getClaim();

		List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
				.search(claim.getKey(), false);

		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
			// if ((tableDTO.getKey() != null && tableDTO.getKey().equals(
			// previousPreAuthTableDTO.getKey()))) {
			previousPreAuthTableDTO.setRequestedAmt(preauthService
					.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
							previousPreAuthTableDTO.getClaimKey()));
			newList.add(previousPreAuthTableDTO);
			// }
		}

		/***
		 * find previous preauth
		 */

		List<Preauth> preauthByClaimKey = preauthService
				.getPreauthByClaimKey(preauth.getClaim().getKey());

		Integer nextReferenceNo = 2;
		Preauth previousPreauth = null;
		if (!preauthByClaimKey.isEmpty()) {
			previousPreauth = preauthByClaimKey.get(0);

			String[] split = previousPreauth.getPreauthId().split("/");
			String defaultNumber = split[split.length - 1];
			nextReferenceNo = Integer.valueOf(defaultNumber);
			for (Preauth preauth1 : preauthByClaimKey) {
				if (preauth1.getPreauthId() != null
						&& !preauth.getKey().equals(preauth1.getKey())
						&& !preauth1.getStatus().getKey()
								.equals(ReferenceTable.PREAUTH_REJECT_STATUS)
						&& !preauth1
								.getStatus()
								.getKey()
								.equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)) {
					previousPreauth = preauth1;
				}
			}

		}

		preauthDTO.setPreviousPreauthKey(preauthKey);
		preauthDTO.getPreauthDataExtractionDetails().setTotalApprAmt(
				previousPreauth.getTotalApprovalAmount());

		preauthDTO.setPreviousPreauthTableDTO(newList);

		preauthDTO.setReferenceType(preauth.getPreauthId());

	//	preauthDTO.setRodHumanTask(tableDTO.getHumanTask());

		preauthDTO.setStrUserName(tableDTO.getUsername());
		preauthDTO.setStrPassword(tableDTO.getPassword());

		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA4(
				tableDTO.getIsCheifMedicalOfficer());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA3(
				tableDTO.getIsZonalMedicalHead());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA2(
				tableDTO.getIsZonalSeniorMedicalApprover());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA1(
				tableDTO.getIsZonalMedicalApprover());

		if (!tableDTO.getIsSpecialistReply()) {
			PreauthEscalate escalateByClaimKey = reimbursementService
					.getEscalateByClaimKey(preauth.getClaim().getKey());
			if (escalateByClaimKey != null) {
				preauthDTO.getDownSizePreauthDataExtrationDetails()
						.setEscalationRemarks(
								escalateByClaimKey.getEscalateRemarks());
				preauthDTO.getDownSizePreauthDataExtrationDetails()
						.setEscalatedRole(escalateByClaimKey.getCreatedBy());
			}
		} else {
			preauthDTO.getDownSizePreauthDataExtrationDetails()
					.setIsEscalateFromSpecialist(true);
			Specialist specialistByClaimKey = reimbursementService
					.getSpecialistByClaimKey(preauth.getClaim().getKey());
			if (specialistByClaimKey != null) {
				preauthDTO
						.getDownSizePreauthDataExtrationDetails()
						.setSpecialistType(
								specialistByClaimKey.getSpcialistType() != null ? specialistByClaimKey
										.getSpcialistType().getValue() : "");
				preauthDTO.getDownSizePreauthDataExtrationDetails()
						.setSpecialistRemarks(
								specialistByClaimKey.getSpecialistRemarks());
			}
		}

		if (preauth.getTotalApprovalAmount() != null) {
			preauthDTO.setPreviousPreauthPayableAmount(preauth
					.getTotalApprovalAmount().intValue());
		}

		preauthDTO.setDbOutArray(tableDTO.getDbOutArray());
		
		loadRRCRequestValues(preauthDTO, insuredSumInsured,
				SHAConstants.PROCESS_DOWNSIZE_REQUEST_PREAUTH);
		
		if(null != preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode()){
			
			MasterGST masGstObj = masterService.getGSTByStateId(preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode());

			if(null != masGstObj){
	
				preauthDTO.setGstNumber(masGstObj.getGstNumber());
				preauthDTO.setArnNumber(masGstObj.getArnNumber());
				preauthDTO.setGstState(masGstObj.getStateName());
			}
		}

		view.setPADownsizePreauthRequest(PADownsizePreauthRequestWizard.class,
				preauthDTO, selectValueContainer, escalateContainer);

	}

	
	private Double getCopay(String insuredAge, Long productKey) {
		MasCopay masCopay = copayservice.getByProduct(productKey);

		Long lInsuredAge = Long.parseLong(insuredAge != null ? !insuredAge
				.equalsIgnoreCase("") ? insuredAge : "0" : "0");
		if (masCopay != null) {
			if (null != masCopay.getEntryAgeFrom()
					&& (lInsuredAge >= masCopay.getEntryAgeFrom())) {
				return masCopay.getMaxPercentage();
			} else if (null == masCopay.getEntryAgeFrom()) {
				return masCopay.getMaxPercentage();
			} else {
				return new Double("0");
			}
		} else {
			return new Double("0");
		}
	}

	private void getResidualAmount(Preauth previousPreauth,
			PreauthDTO preauthDTO) {
		ResidualAmount residualAmtByPreauthKey = preauthService
				.getResidualAmtByPreauthKey(previousPreauth.getKey());
		if (null != residualAmtByPreauthKey) {
			ResidualAmountDTO residualDTO = new ResidualAmountDTO();
			residualDTO.setKey(residualAmtByPreauthKey.getKey());
			residualDTO.setPreauthKey(previousPreauth.getKey());
			residualDTO.setApprovedAmount(residualAmtByPreauthKey
					.getApprovedAmount());
			residualDTO.setNetApprovedAmount(residualAmtByPreauthKey
					.getNetApprovedAmount() != null ? residualAmtByPreauthKey
					.getNetApprovedAmount() : 0d);
			residualDTO.setRemarks(residualAmtByPreauthKey.getRemarks());
			if(preauthDTO != null && preauthDTO.getNewIntimationDTO() != null && null != preauthDTO.getNewIntimationDTO().getIsJioPolicy() && preauthDTO.getNewIntimationDTO().getIsJioPolicy()){
				if(null != residualAmtByPreauthKey.getCoPayTypeId()){		
					SelectValue copayTypeValue = new SelectValue();
					copayTypeValue.setValue(residualAmtByPreauthKey.getCoPayTypeId().getValue());
					copayTypeValue.setId(residualAmtByPreauthKey.getCoPayTypeId().getKey());
					residualDTO.setCoPayTypeId(copayTypeValue);
				}
			}
			preauthDTO.setResidualAmountDTO(residualDTO);
		}

	}

	

	private List<UploadDocumentDTO> getUploadedDocumentTableDataForBillEntry(
			Long reimbursementKey) {
		List<UploadDocumentDTO> uploadDocsList = createRodService
				.getRODSummaryDetails(reimbursementKey);
		return uploadDocsList;
	}

	private void setClaimValuesToDTO(PreauthDTO preauthDTO, Claim claimByKey) {
		PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey
				.getIntimation().getPolicy());
		preauthDTO.setHospitalKey(claimByKey.getIntimation().getHospital());
		Long hospital = claimByKey.getIntimation().getHospital();
		Hospitals hospitalById = hospitalService.getHospitalById(hospital);
		preauthDTO.setHospitalCode(hospitalById.getHospitalCode());
		preauthDTO.setClaimNumber(claimByKey.getClaimId());
		preauthDTO.setPolicyDto(policyDto);
		preauthDTO.setDateOfAdmission(claimByKey.getIntimation()
				.getAdmissionDate());
		preauthDTO.setReasonForAdmission(claimByKey.getIntimation()
				.getAdmissionReason());
		preauthDTO.setIntimationKey(claimByKey.getIntimation().getKey());
		preauthDTO
				.setPolicyKey(claimByKey.getIntimation().getPolicy().getKey());
		preauthDTO.setClaimKey(claimByKey.getKey());
	}

	private BillEntryDetailsDTO getBillDetailsDTOForBillEntry(
			RODBillDetails billDetails) {
		// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
		BillEntryDetailsDTO dto = new BillEntryDetailsDTO();

		dto.setItemName(billDetails.getItemName());
		dto.setKey(billDetails.getKey());
		SelectValue classificationValue = new SelectValue();
		classificationValue.setId(billDetails.getBillClassification().getKey());
		classificationValue.setValue(billDetails.getBillClassification()
				.getValue());
		dto.setClassification(classificationValue);
		dto.setItemNo(billDetails.getItemNumber());
		/*
		 * dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
		 * .getNoOfDaysBills().doubleValue() : 0d);
		 */
		dto.setNoOfDays(billDetails.getNoOfDaysBills() != null ? billDetails
				.getNoOfDaysBills().doubleValue() : null);
		dto.setPerDayAmt(billDetails.getPerDayAmountBills());
		dto.setBillValue(billDetails.getClaimedAmountBills());
		dto.setItemValue(billDetails.getClaimedAmountBills());
		SelectValue billCategoryvalue = new SelectValue();
		billCategoryvalue.setId(billDetails.getBillCategory().getKey());
		billCategoryvalue.setValue(billDetails.getBillCategory().getValue());
		dto.setCategory(billCategoryvalue);
		dto.setDocumentSummaryKey(billDetails.getRodDocumentSummaryKey()
				.getKey());
		dto.setKey(billDetails.getKey());

		Reimbursement objReimbursement = billDetails.getRodDocumentSummaryKey()
				.getReimbursement();
		if (null != objReimbursement) {
			dto.setIntimationNo(objReimbursement.getClaim().getIntimation()
					.getIntimationId());
			dto.setDateOfAdmission(SHAUtils.formatDate(objReimbursement
					.getDateOfAdmission()));
			dto.setDateOfDischarge(SHAUtils.formatDate(objReimbursement
					.getDateOfDischarge()));
			dto.setInsuredPatientName(objReimbursement.getClaim()
					.getIntimation().getInsuredPatientName());
		}

		return dto;
	}

	protected void showPAEnterBillDetails(
			@Observes @CDIEvent(MenuItemBean.PA_ENTER_BILL_DETAILS) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();

		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
				.getSelectValueForPriority();

		// BeanItemContainer<SelectValue> statusByStage =
		// masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);

		Stage stageByKey = preauthService
				.getStageByKey(ReferenceTable.CREATE_ROD_STAGE_KEY);
		Stage billingStage = preauthService
				.getStageByKey(ReferenceTable.BILLING_STAGE);
		Stage financialStage = preauthService
				.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

		SelectValue selectValue = new SelectValue();
		selectValue.setId(stageByKey.getKey());
		selectValue.setValue(stageByKey.getStageName());

		SelectValue selBillValue = new SelectValue();
		if (null != billingStage) {
			selBillValue.setId(billingStage.getKey());
			selBillValue.setValue(billingStage.getStageName());
		}

		SelectValue selFinValue = new SelectValue();
		if (null != financialStage) {
			selFinValue.setId(financialStage.getKey());
			selFinValue.setValue(financialStage.getStageName());
		}

		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		statusByStage.addBean(selectValue);
		statusByStage.addBean(selBillValue);
		statusByStage.addBean(selFinValue);

		view.setPAViewEnterBillDetails(PASearchEnterBillDetailsView.class,
				true, selectValueContainerForCPUCode, selectValueForPriority,
				statusByStage);
	}

	protected void showPAHealthProcessClaimRequest(
			@Observes @CDIEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST) final ParameterDTO parameters) {
		PAHealthSearchProcessClaimRequestFormDTO dto = null;
		if (parameters.getPrimaryParameter() != null
				&& (parameters.getPrimaryParameter() instanceof SearchProcessClaimRequestFormDTO)) {
			dto = (PAHealthSearchProcessClaimRequestFormDTO) parameters
					.getPrimaryParameter();
		}
		BeanItemContainer<SelectValue> insuranceSource = masterService
				.getMasterValueByReference(ReferenceTable.INTIMATION_SOURCE);
		BeanItemContainer<SelectValue> networkHospitalType = masterService
				.getMasterValueByReference(ReferenceTable.NETWORK_HOSPITAL_TYPE);
		BeanItemContainer<SelectValue> hospitalType = masterService
				.getMasterValueByReference(ReferenceTable.HOSPITAL_TYPE);
		BeanItemContainer<SelectValue> treatmentType = masterService
				.getMasterValueByReference(ReferenceTable.TREATMENT_MANAGEMENT);

		BeanItemContainer<SelectValue> productName = masterService
				.getSelectValueContainerForProduct();
		BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();

		BeanItemContainer<SelectValue> selectValueContainerForType = masterService
				.getStageList();

		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
				.getSelectValueForPriority();

		// BeanItemContainer<SelectValue> statusByStage =
		// masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);

		Stage stageByKey = preauthService
				.getStageByKey(ReferenceTable.ZONAL_REVIEW_STAGE);
		Status statusByKey2 = preauthService
				.getStatusByKey(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER);
		Status statuseByKey3 = preauthService
				.getStatusByKey(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);
		Status statusByKey4 = preauthService
				.getStatusByKey(ReferenceTable.CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS);
		Status statusByKey5 = preauthService
				.getStatusByKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS);
		Status statusByKey6 = preauthService
				.getStatusByKey(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);
		Status statusByKey7 = preauthService
				.getStatusByKey(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
		Status statusByKey8 = preauthService
				.getStatusByKey(ReferenceTable.CLAIM_REQUEST_QUERY_RECEIVED_STATUS);
		// Status statusByKey9 =
		// preauthService.getStatusByKey(ReferenceTable.FA_QUERY_REPLY_STATUS);

		SelectValue selectValue = new SelectValue();
		selectValue.setId(stageByKey.getKey());
		selectValue.setValue(stageByKey.getStageName());

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(statusByKey2.getKey());
		selectValue1.setValue("Billing- " + statusByKey2.getProcessValue());

		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(statuseByKey3.getKey());
		selectValue2.setValue("FA- " + statuseByKey3.getProcessValue());

		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(statusByKey4.getKey());
		selectValue3.setValue(statusByKey4.getProcessValue());

		SelectValue selectValue4 = new SelectValue();
		selectValue4.setId(statusByKey5.getKey());
		selectValue4.setValue(statusByKey5.getProcessValue());

		SelectValue selectValue5 = new SelectValue();
		selectValue5.setId(statusByKey6.getKey());
		selectValue5.setValue(statusByKey6.getProcessValue());

		SelectValue selectValue6 = new SelectValue();
		selectValue6.setId(statusByKey7.getKey());
		selectValue6.setValue(statusByKey7.getProcessValue());

		SelectValue selectValue7 = new SelectValue();
		selectValue7.setId(statusByKey8.getKey());
		selectValue7.setValue(SHAConstants.QUERY_REPLY);

		SelectValue selectValue8 = new SelectValue();
		selectValue8.setId(0l);
		selectValue8.setValue(SHAConstants.RECONSIDERATION);

		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		statusByStage.addBean(selectValue);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
		statusByStage.addBean(selectValue3);
		statusByStage.addBean(selectValue4);
		statusByStage.addBean(selectValue5);
		statusByStage.addBean(selectValue6);
		statusByStage.addBean(selectValue7);
		statusByStage.addBean(selectValue8);

		view.setViewsPAHealthProcessClaimRequest(
				PAHealthSearchProcessClaimRequestView.class, true,
				insuranceSource, hospitalType, networkHospitalType,
				treatmentType, selectValueContainerForType, productName,
				cpuCode, selectValueForPriority, statusByStage, dto,SHAConstants.PA_HEALTH_MEDICAL_SCREEN);
	}


	protected void showMedicalApprovalProcessClaimRequest(
			@Observes @CDIEvent(PAMenuPresenter.SHOW_PA_HEALTH_MEDICAL_APPROVAL_PROCESS_CLAIM_REQUEST) final ParameterDTO parameters) {
		Date startDate = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> SHOW MEDICAL APPROVAL CLAIM REQUEST METHOD STARTED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ startDate);

		PAHealthSearchProcessClaimRequestTableDTO tableDTO = (PAHealthSearchProcessClaimRequestTableDTO) parameters
				.getPrimaryParameter();

		Reimbursement reimbursementObjectByKey = rodService
				.getReimbursementObjectByKey(tableDTO.getRodKey());

		Date date1 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> BEFORE INVOKING ROD SUMMARY DETAILS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date1);

		List<UploadDocumentDTO> rodSummaryDetails = rodService
				.getRODSummaryDetails(reimbursementObjectByKey.getKey());
		Date date2 = new Date();
		
		Claim claimByKeyForLegal = reimbursementObjectByKey.getClaim();
				
		Boolean isValidClaim = true;
		if (null != claimByKeyForLegal) {
			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKeyForLegal
					.getLegalFlag())) {
				isValidClaim = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
		}
		
		if(isValidClaim) {

		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> AFTER INVOKING ROD SUMMARY DETAILS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date2);

		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL DURATION FOR RODSUMMARY DETAILS METHOD %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date1, date2));

		Boolean isValidClaimForMA = true;

		/*
		 * List<Long> documentSummaryKeys = new ArrayList<Long>(); for
		 * (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
		 * documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
		 * uploadDocumentDTO.setStatus(true); List<RODBillDetails>
		 * billEntryDetails = rodService
		 * .getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
		 * List<BillEntryDetailsDTO> dtoList = new
		 * ArrayList<BillEntryDetailsDTO>(); if (billEntryDetails != null &&
		 * !billEntryDetails.isEmpty()) { for (RODBillDetails billEntryDetailsDO
		 * : billEntryDetails) {
		 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
		 * uploadDocumentDTO)); } }
		 * uploadDocumentDTO.setBillEntryDetailList(dtoList); }
		 */
		/*
		 * Double totalBilledAmount = reimbursementService
		 * .getTotalBilledAmount(documentSummaryKeys);
		 */
		Date date3 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> BEFORE INVOKING ZONAL  MEDICAL REVIEW MAPPER  GET REIMBURSEMENT DTO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date3);
		ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper
				.getInstance();
		// ZonalMedicalReviewMapper.getAllMapValues();
		PreauthDTO reimbursementDTO = mapper
				.getReimbursementDTO(reimbursementObjectByKey);
		Date date4 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> AFTER INVOKING ZONAL  MEDICAL REVIEW MAPPER  GET REIMBURSEMENT DTO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date4);
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL DURATION FOR GET REIMBURSEMENT DTO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date3, date4));

		
		if(null != reimbursementObjectByKey && ("Y").equalsIgnoreCase(reimbursementObjectByKey.getPedFlag()))
		{
			reimbursementDTO.getPreauthDataExtractionDetails().setPreExistingDisabilities(true);
			reimbursementDTO.getPreauthDataExtractionDetails().setDisabilitesRemarks(reimbursementObjectByKey.getPedDisablitiyDetails());
		}
		else
		{
			reimbursementDTO.getPreauthDataExtractionDetails().setPreExistingDisabilities(false);
		}
		
		reimbursementDTO.getPreauthDataExtractionDetails().setDisabilitesRemarks(reimbursementObjectByKey.getPedDisablitiyDetails());

		if (("Y").equalsIgnoreCase(reimbursementDTO
				.getPreauthDataExtractionDetails()
				.getReconsiderationFlag())) {
			reimbursementDTO
					.setReconsiderRodRequestList(getReconsiderRODRequestForBillEntry(tableDTO
							.getRodKey()));
		}
		NewIntimationDto newIntimationDto = intimationService
				.getIntimationDto(claimByKeyForLegal.getIntimation());
		reimbursementDTO.setNewIntimationDTO(newIntimationDto);
		
		Date policyFromDate = reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyFromDate();
		
		Date admissionDate = reimbursementDTO.getNewIntimationDTO().getAdmissionDate();
		
		Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
		if(diffDays != null && diffDays<90){
			reimbursementDTO.setIs64VBChequeStatusAlert(true);
		}	 
		
		/*
		 * reimbursementDTO.setAmountConsidered(totalBilledAmount != null ?
		 * String .valueOf(totalBilledAmount.intValue()) : "0");
		 * reimbursementDTO .setInitialAmountConsidered(totalBilledAmount !=
		 * null ? String .valueOf(totalBilledAmount.intValue()) : "0");
		 * reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		 * reimbursementDTO.setHumanTask(tableDTO.getHumanTask());
		 */

		Date date5 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> BEFORE INVOKING FVR GRADING SERVICE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date5);

		List<FVRGradingMaster> fvrGrading = reimbursementService
				.getFVRGrading();
		Date date6 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> AFTER INVOKING FVR GRADING SERVICE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date6);

		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> BEFORE INVOKING FVR GRADING SERVICE BY FVR CLAIM KEY %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date6);

		List<FieldVisitRequest> fvrByClaimKey = reimbursementService
				.getFVRByClaimKey(reimbursementObjectByKey.getClaim().getKey());

		Date date7 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> AFTER INVOKING FVR GRADING SERVICE BY FVR CLAIM KEY %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ date7);

		List<FvrGradingDetailsDTO> dto = new ArrayList<FvrGradingDetailsDTO>();
		Map<Integer, FieldVisitRequest> valueMap = new HashMap<Integer, FieldVisitRequest>();
		for (int i = 0; i < fvrByClaimKey.size(); i++) {
			FieldVisitRequest fieldVisitRequest = fvrByClaimKey.get(i);
			if (fieldVisitRequest.getStatus() != null
					&& fieldVisitRequest.getStatus().getKey()
							.equals(ReferenceTable.FVR_REPLY_RECEIVED)) {
				FvrGradingDetailsDTO fvrDto = new FvrGradingDetailsDTO();
				fvrDto.setKey(fieldVisitRequest.getKey());
				fvrDto.setRepresentativeName(fieldVisitRequest
						.getRepresentativeName());
				fvrDto.setRepresentiveCode(fieldVisitRequest
						.getRepresentativeCode());
				List<FVRGradingDTO> FVRTableDTO = new ArrayList<FVRGradingDTO>();
				for (FVRGradingMaster masterFVR : fvrGrading) {
					FVRGradingDTO eachFVRDTO = new FVRGradingDTO();
					eachFVRDTO.setKey(masterFVR.getKey());
					eachFVRDTO.setCategory(masterFVR.getGradingType());
					eachFVRDTO.setApplicability(masterFVR.getApplicability());
					switch (Integer.valueOf(String.valueOf(masterFVR.getKey()))) {
					case 8:
						if (fieldVisitRequest.getPatientVerified() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPatientVerified());
						}
						break;
					case 9:
						if (fieldVisitRequest.getDiagnosisVerfied() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getDiagnosisVerfied());
						}
						break;
					case 10:
						if (fieldVisitRequest.getRoomCategoryVerfied() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getRoomCategoryVerfied());
						}
						break;
					case 11:
						if (fieldVisitRequest.getTriggerPointsFocused() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getTriggerPointsFocused());
						}
						break;
					case 12:
						if (fieldVisitRequest.getPedVerified() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPedVerified());
						}
						break;
					case 13:
						if (fieldVisitRequest.getPatientDischarged() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPatientDischarged());
						}
						break;
					case 14:
						if (fieldVisitRequest.getPatientNotAdmitted() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPatientNotAdmitted());
						}
						break;
					case 15:
						if (fieldVisitRequest.getOutstandingFvr() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getOutstandingFvr());
						}
						break;
					default:
						break;
					}
					FVRTableDTO.add(eachFVRDTO);
				}
				fvrDto.setFvrGradingDTO(FVRTableDTO);
				dto.add(fvrDto);
			}
		}

		reimbursementDTO.getPreauthMedicalDecisionDetails().setFvrGradingDTO(
				dto);
		// added for parralel processing
		reimbursementDTO.getPreauthMedicalDecisionDetails().setIsFvrIntiated(preauthService.getFVRStatusIdByClaimKey(reimbursementDTO.getClaimKey()));
		reimbursementDTO.getPreauthMedicalDecisionDetails().setIsFvrIntiatedMA(preauthService.getFVRStatusIdMAByClaimKey(reimbursementDTO.getClaimKey()));
		reimbursementDTO.getPreauthMedicalDecisionDetails().setIsFvrReplyReceived(preauthService.isFVRReplyReceived(reimbursementDTO.getClaimKey()));

	//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());
		reimbursementDTO.setStrUserName(tableDTO.getUsername());
		reimbursementDTO.setStrPassword(tableDTO.getPassword());
		reimbursementDTO.setTaskNumber(tableDTO.getTaskNumber());

		reimbursementDTO.setUserRole(tableDTO.getUserRole());
		reimbursementDTO.setRodNumber(reimbursementObjectByKey.getRodNumber());
		Claim claimByKey = reimbursementObjectByKey.getClaim();
		Date date8 = new Date();
		reimbursementDTO
				.setReconsiderationList(getReconsiderRODRequest(claimByKey));
		Date date9 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY GET RECONSIDER ROD REQUEST %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date8, date9));

		BeanItemContainer<SelectValue> medicalContainer = masterService
				.getSelectValueContainer(ReferenceTable.MEDICAL_VERIFICATION);
		BeanItemContainer<SelectValue> treatmentContainer = masterService
				.getSelectValueContainer(ReferenceTable.TREATMENT_QUALITY_VERIFICATION);

		// setHospitalDetailsToDTO(hospitalById, reimbursementDTO);
		Date date10 = new Date();
		setReimbursmentTOPreauthDTO(mapper, claimByKey,
				reimbursementObjectByKey, reimbursementDTO, true,
				SHAConstants.CLAIM_REQUEST);
		Date date11 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY SET REIMBURSEMENT TO PREAUTH DTO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date10, date11));

		String[] hospSplit = reimbursementObjectByKey.getRodNumber().split("/");
		String hsopSplitNo = hospSplit[hospSplit.length - 1];
		Integer hospNo = Integer.valueOf(hsopSplitNo);
		if (reimbursementObjectByKey.getStatus() != null
				&& !reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)
				&& !reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)
				&& hospNo.equals(1)
				&& reimbursementObjectByKey.getClaim().getClaimType() != null
				&& reimbursementObjectByKey.getClaim().getClaimType().getKey()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			Preauth latestPreauthByClaim = preauthService
					.getLatestPreauthByClaim(reimbursementObjectByKey
							.getClaim().getKey());
			reimbursementDTO.setPreviousPreauthKey(latestPreauthByClaim
					.getKey());
			List<PedValidation> findPedValidationByPreauthKey = preauthService
					.findPedValidationByPreauthKey(latestPreauthByClaim
							.getKey());
			List<Procedure> findProcedureByPreauthKey = preauthService
					.findProcedureByPreauthKey(latestPreauthByClaim.getKey());
			List<DiagnosisDetailsTableDTO> diagnosisTableList = reimbursementDTO
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			List<ProcedureDTO> procedureExclusionCheckTableList = reimbursementDTO
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				for (Procedure procedure : findProcedureByPreauthKey) {
					if (procedure.getProcedureID().equals(
							procedureDTO.getProcedureName().getId())) {
						procedureDTO.setOldApprovedAmount(procedure
								.getNetApprovedAmount());
					}
				}
			}

			for (DiagnosisDetailsTableDTO diagnosisDTO : diagnosisTableList) {
				for (PedValidation pedValidation : findPedValidationByPreauthKey) {
					if (pedValidation.getDiagnosisId().equals(
							diagnosisDTO.getDiagnosisId())) {
						diagnosisDTO.setOldApprovedAmount(pedValidation
								.getNetApprovedAmount());
					}
				}
			}
		}

		DBCalculationService dbCalculationService = new DBCalculationService();
		Date date12 = new Date();
		
		Double insuredSumInsured = 0d;
		
		if(null != claimByKey &&  null != claimByKey.getIntimation() && 
				null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
				null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
				&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
		{
		  insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), reimbursementDTO
						.getPolicyDto().getKey(),reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			 insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), reimbursementDTO
								.getPolicyDto().getKey());
		}
		Date date13 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY INSURED SUM INSURED PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date12, date13));

		Map<Integer, Object> detailsMap = new HashMap<Integer, Object>();

		// if(null != reimbursementDTO && null !=
		// reimbursementDTO.getNewIntimationDTO() && null !=
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto()
		// && null !=
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals()
		// && null !=
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType()
		// &&
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType().getKey().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID))
		// {
		// String cityClass =
		// createRodService.getHospitalCityClass(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
		// reimbursementDTO.getNewIntimationDTO().getHospitalDto()
		// .getRegistedHospitals().setCityClass(cityClass);
		//
		// }

		if (reimbursementObjectByKey.getSectionCategory() != null) {

			Date date14 = new Date();
			detailsMap = dbCalculationService.getHospitalizationDetails(
					reimbursementDTO.getPolicyDto().getProduct().getKey(),
					insuredSumInsured, reimbursementDTO.getNewIntimationDTO()
							.getHospitalDto().getRegistedHospitals()
							.getCityClass(), reimbursementDTO
							.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId(), reimbursementDTO
							.getNewIntimationDTO().getKey(),
					reimbursementObjectByKey.getSectionCategory(), "A");
			Date date15 = new Date();
			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY HOSPITALIZATION DETAILS PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date14, date15));

		} else {

			Long sectionCategory = 0l;
			if (reimbursementDTO.getPolicyDto().getProduct().getKey()
					.equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY)
					|| reimbursementDTO.getPolicyDto().getProduct().getKey()
							.equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY)
					|| reimbursementDTO.getPolicyDto().getProduct().getKey()
							.equals(ReferenceTable.DIABETES_FLOATER_POLICY)) {
				sectionCategory = 1l;
			}

			Date date14 = new Date();
			detailsMap = dbCalculationService.getHospitalizationDetails(
					reimbursementDTO.getPolicyDto().getProduct().getKey(),
					insuredSumInsured, reimbursementDTO.getNewIntimationDTO()
							.getHospitalDto().getRegistedHospitals()
							.getCityClass(), reimbursementDTO
							.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId(), reimbursementDTO
							.getNewIntimationDTO().getKey(), sectionCategory,
					"0");
			Date date15 = new Date();

			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY HOSPITALIZATION DETAILS PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date14, date15));

		}

		if (detailsMap != null && detailsMap.get(15) != null) {
			// reimbursementDTO.setAmbulanceLimitAmount((Double)detailsMap.get(15));
		}

		reimbursementDTO = getProrataFlagFromProduct(reimbursementDTO);

		if (reimbursementObjectByKey.getProrataDeductionFlag() != null) {
			reimbursementDTO.setProrataDeductionFlag(reimbursementObjectByKey
					.getProrataDeductionFlag());
		}

		/**
		 * product based variable is added to enable or disable the component in
		 * page level. This would be static. -- starts
		 * */
		// reimbursementDTO.setProductBasedProRata(reimbursementObjectByKey.getProrataDeductionFlag());
		// reimbursementDTO.setProductBasedPackage(reimbursementObjectByKey.getPackageAvailableFlag());
		// ends.
		reimbursementDTO.setPackageAvailableFlag(reimbursementObjectByKey
				.getPackageAvailableFlag());

		List<Long> documentSummaryKeys = new ArrayList<Long>();

		/**
		 * Fix for implemeting claims dms in bill entry screen.. -- starts
		 * **/
		if (null != rodSummaryDetails && !rodSummaryDetails.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				if (null != claimByKey) {
					uploadDocumentDTO.setClaimNo(claimByKey.getClaimId());
					uploadDocumentDTO
							.setDmsDocumentDTOList(getClaimsDMSList(claimByKey
									.getIntimation().getIntimationId()));
				}
			}
		}

		/**
		 * Added for enabling view documents in preauth screen while raising
		 * query -- fix starts.
		 * */
		if (null != claimByKey) {
			Date date17 = new Date();
			reimbursementDTO.setDmsDocumentDTOList(getClaimsDMSList(claimByKey
					.getIntimation().getIntimationId()));
			Date date18 = new Date();
			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY CLAIMS DMS LIST FETCH %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date17, date18));

		}
		/**
		 * Added for enabling view documents in preauth screen while raising
		 * query -- fix ends
		 * */

		for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
			/*
			 * if(null != reimbursementObjectByKey) {
			 * uploadDocumentDTO.setIntimationNo
			 * (reimbursementObjectByKey.getClaim
			 * ().getIntimation().getIntimationId());
			 * uploadDocumentDTO.setDateOfAdmission
			 * (SHAUtils.formatDate(reimbursementObjectByKey
			 * .getDateOfAdmission()));
			 * uploadDocumentDTO.setDateOfDischarge(SHAUtils
			 * .formatDate(reimbursementObjectByKey.getDateOfDischarge()));
			 * uploadDocumentDTO
			 * .setInsuredPatientName(reimbursementObjectByKey.getClaim
			 * ().getIntimation().getInsuredPatientName()); }
			 */
			documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
			uploadDocumentDTO.setStatus(true);
			Date date19 = new Date();
			List<RODBillDetails> billEntryDetails = rodService
					.getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
			Date date20 = new Date();
			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY BILL ENTRY DETAILS LIST %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date19, date20));

			List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
			if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
				for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
					/*
					 * <<<<<<< HEAD
					 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
					 * uploadDocumentDTO)); =======
					 */
					dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,
							uploadDocumentDTO));
					// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
				}
			}
			/*
			 * uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
			 * .get(8));
			 */
			uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
					.get(8));
			uploadDocumentDTO
					.setProductBasedICURent((Double) detailsMap.get(9));
			/*
			 * uploadDocumentDTO.setProductBasedAmbulanceAmt((Double) detailsMap
			 * .get(15));
			 */
			uploadDocumentDTO
					.setProductBasedAmbulanceAmt(getProductBasedAmbulanceAmt(tableDTO
							.getRodKey()));
			uploadDocumentDTO.setStatus(false);
			uploadDocumentDTO.setBillEntryDetailList(dtoList);
			uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO()
					.getClaimType());
			uploadDocumentDTO.setRodKey(reimbursementObjectByKey.getKey());
			uploadDocumentDTO.setStrUserName(tableDTO.getUsername());

		}

		List<UploadDocumentDTO> rodBillSummaryDetails = rodService
				.getRODBillSummaryDetails(reimbursementObjectByKey.getKey(),
						mapper);
		if (rodBillSummaryDetails != null) {
			for (UploadDocumentDTO uploadDocumentDTO : rodBillSummaryDetails) {
				uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO()
						.getClaimType());
			}
		}

		reimbursementDTO.getUploadDocDTO()
				.setBillingWorkSheetUploadDocumentList(rodBillSummaryDetails);
		Double totalBilledAmount = reimbursementService
				.getTotalBilledAmount(documentSummaryKeys);

		reimbursementDTO.setAmountConsidered(totalBilledAmount != null ? String
				.valueOf(totalBilledAmount.intValue()) : "0");
		reimbursementDTO
				.setInitialAmountConsidered(totalBilledAmount != null ? String
						.valueOf(totalBilledAmount.intValue()) : "0");
		reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		// reimbursementDTO.setRodHumanTask(tableDTO.getHumanTask());
	//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());

		Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
				.getIntimation().getHospital());
		ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = reimbursementDTO
				.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
		if (hospitalById != null) {

			updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			updateHospitalDetails.setHospitalState(hospitalById.getState());
			updateHospitalDetails.setHospitalCode(hospitalById
					.getHospitalCode());
			updateHospitalDetails.setPanNumber(hospitalById.getPanNumber());
			updateHospitalDetails.setHospitalTypeId(hospitalById
					.getHospitalType().getKey());
			updateHospitalDetails.setInpatientBeds(hospitalById
					.getInpatientBeds() != null ? String.valueOf(hospitalById
					.getInpatientBeds()) : "0");
			updateHospitalDetails.setHospitalName(hospitalById.getName());
			updateHospitalDetails.setHospitalPhoneNo(hospitalById
					.getPhoneNumber());
			updateHospitalDetails.setHopitalRegNumber(hospitalById
					.getRegistrationNumber());
			updateHospitalDetails.setHospitalPincode(hospitalById.getPincode());
			updateHospitalDetails.setOtFacilityFlag(hospitalById
					.getOtFacilityFlag());
			updateHospitalDetails.setIcuFacilityFlag(hospitalById
					.getIcuFacilityFlag());
			updateHospitalDetails
					.setHospitalAddress1(hospitalById.getAddress());

		}

		List<MedicalVerificationDTO> medicalDTO = new ArrayList<MedicalVerificationDTO>();
		List<TreatmentQualityVerificationDTO> treatmentDTO = new ArrayList<TreatmentQualityVerificationDTO>();
		List<ClaimVerification> claimVerificationByReimbKey = reimbursementService
				.getClaimVerificationByReimbKey(reimbursementObjectByKey
						.getKey());
		if (claimVerificationByReimbKey != null) {

			for (ClaimVerification claimVerification : claimVerificationByReimbKey) {
				if (claimVerification.getVerificationType() != null
						&& claimVerification.getVerificationType()
								.toLowerCase().equalsIgnoreCase("medical")) {

					MedicalVerificationDTO medicalVerifyDTO = new MedicalVerificationDTO();
					List<SelectValue> itemIds = medicalContainer.getItemIds();
					if (itemIds != null && !itemIds.isEmpty()) {
						for (SelectValue selectValue : itemIds) {
							if (claimVerification.getVerificationTypeId()
									.equals(selectValue.getId())) {
								medicalVerifyDTO.setDescriptionId(selectValue
										.getId());
								medicalVerifyDTO.setDescription(selectValue
										.getValue());
							}
						}
					}
					medicalVerifyDTO.setKey(claimVerification.getKey());
					medicalVerifyDTO.setRemarks(claimVerification
							.getMedicalRemarks());
					medicalVerifyDTO.setVerifiedFlag(claimVerification
							.getVerifiedFlag());

					medicalDTO.add(medicalVerifyDTO);
				} else if (claimVerification.getVerificationType() != null
						&& claimVerification.getVerificationType()
								.toLowerCase().equalsIgnoreCase("treatment")) {

					TreatmentQualityVerificationDTO treatmentVerifyDTO = new TreatmentQualityVerificationDTO();
					List<SelectValue> itemIds = treatmentContainer.getItemIds();
					if (itemIds != null && !itemIds.isEmpty()) {
						for (SelectValue selectValue : itemIds) {
							if (claimVerification.getVerificationTypeId()
									.equals(selectValue.getId())) {
								treatmentVerifyDTO.setDescriptionId(selectValue
										.getId());
								treatmentVerifyDTO.setDescription(selectValue
										.getValue());
							}
						}
					}
					treatmentVerifyDTO.setKey(claimVerification.getKey());
					treatmentVerifyDTO.setRemarks(claimVerification
							.getMedicalRemarks());
					treatmentVerifyDTO.setVerifiedFlag(claimVerification
							.getVerifiedFlag());

					treatmentDTO.add(treatmentVerifyDTO);
				}
			}
		}
		if (claimByKey.getClaimType() != null
				&& claimByKey.getClaimType().getKey() != null
				&& claimByKey.getClaimType().getKey()
						.equals(ReferenceTable.CLAIM_TYPE_CASHLESS_ID)) {

			reimbursementDTO.setIsCashlessType(true);

			Preauth previousPreauth = getPreviousPreauth(claimByKey.getKey());
			reimbursementDTO
					.setTreatmentRemarksList(getTreatmentRemarksHistory(claimByKey
							.getKey()));

			if (previousPreauth != null) {
				reimbursementDTO.setPreauthKey(previousPreauth.getKey());
			}
		}

		reimbursementDTO.getPreauthMedicalDecisionDetails()
				.setMedicalVerificationTableDTO(medicalDTO);
		reimbursementDTO.getPreauthMedicalDecisionDetails()
				.setTreatmentVerificationDTO(treatmentDTO);
		reimbursementDTO.getPreauthDataExtractionDetails()
				.setReasonForAdmission(
						claimByKey.getIntimation().getAdmissionReason());
		reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
				reimbursementObjectByKey.getKey());

		if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)
				|| reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
			Long medicalApproverKey = reimbursementService
					.getLatestMedicalApproverKey(reimbursementObjectByKey
							.getKey());
			MedicalApprover medicalApprover = reimbursementService
					.getMedicalApproverByKey(medicalApproverKey);
			if (medicalApprover != null) {
				reimbursementDTO.setPreviousRemarks(medicalApprover
						.getReferringRemarks());
				reimbursementDTO.setPreviousReasonForReferring(medicalApprover
						.getReasonForReferring());

				if (medicalApprover.getReferringRemarks() == null) {
					reimbursementDTO.setPreviousRemarks(medicalApprover
							.getApproverReply());
				}
				reimbursementDTO.setMedicalApproverKey(medicalApproverKey);
			}

			if (reimbursementObjectByKey.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)) {
				reimbursementDTO.setIsReplyToFA(true);
			}

			reimbursementDTO.setIsReferToMedicalApprover(true);
		}

		if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_STATUS)) {
			PreauthEscalate preauthEscalate = reimbursementService
					.getEscalateByClaimKey(reimbursementObjectByKey.getClaim()
							.getKey());
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(preauthEscalate.getEscalateRemarks());
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(preauthEscalate.getEscalateRemarks());
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setEscalateRemarks(preauthEscalate.getEscalateRemarks());
			reimbursementDTO.setIsEscalateReplyEnabled(true);
		} else if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_ESCALATION_REPLY_STATUS)) {
			PreauthEscalate preauthEscalate = reimbursementService
					.getEscalateByClaimKey(reimbursementObjectByKey.getClaim()
							.getKey());
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(
							preauthEscalate.getEsclateReplyRemarks());
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(
							preauthEscalate.getEsclateReplyRemarks());
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setEscalateReply(preauthEscalate.getEsclateReplyRemarks());
			reimbursementDTO.setIsEscalateReplyEnabled(false);
		} else if (reimbursementObjectByKey
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS)) {
			ReimbursementRejectionDto reimbursementRejectionByKey = reimbursementRejectionService
					.getReimbursementRejectionByKey(reimbursementObjectByKey
							.getKey());
			if (reimbursementRejectionByKey != null) {
				reimbursementDTO.getPreauthMedicalDecisionDetails()
						.setRejectionRemarks(
								reimbursementRejectionByKey
										.getDisapprovedRemarks());
			}
		} else if (reimbursementObjectByKey
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)) {
			ReimbursementQuery reimbursementyQueryByRodKey = reimbursementQuerySerice
					.getReimbursementyQueryByRodKey(reimbursementObjectByKey
							.getKey());

			if (reimbursementyQueryByRodKey != null) {
				reimbursementDTO.getPreauthMedicalDecisionDetails()
						.setQueryRemarks(
								reimbursementyQueryByRodKey
										.getRejectionRemarks());
			}
		} else if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED)
				|| reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED)) {
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setApprovalRemarks("-");
			Investigation investigation = investigationService
					.getInvestigationByTransactionKey(reimbursementObjectByKey
							.getKey());
			if (investigation != null) {
				reimbursementDTO.getPreauthMedicalProcessingDetails()
						.setApprovalRemarks(investigation.getRemarks());
			}
		}

		setCategoryFlag(reimbursementObjectByKey, reimbursementDTO);

		if (!reimbursementDTO.getHospitalizaionFlag()) {
			reimbursementDTO = setHospitalizationAmount(reimbursementDTO);
		}

		setPreAndPostHopitalizationAmount(reimbursementDTO);

		if (reimbursementObjectByKey.getStatus().getKey() != null
				&& reimbursementObjectByKey.getStatus().getKey()
						.equals(ReferenceTable.ZONAL_REVIEW_REJECTION_STATUS)) {
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(
							reimbursementObjectByKey.getRejectionRemarks());
		} else if (reimbursementObjectByKey.getStatus().getKey() != null
				&& reimbursementObjectByKey.getStatus().getKey()
						.equals(ReferenceTable.ZONAL_REVIEW_QUERY_STATUS)) {
			ReimbursementQuery reimbursementQueryByReimbursmentKey = reimbursementService
					.getReimbursementQueryByReimbursmentKey(reimbursementObjectByKey
							.getKey());
			reimbursementDTO.setIsZonalReviewQuery(true);
			// reimbursementDTO.getPreauthMedicalProcessingDetails().setApprovalRemarks(reimbursementQueryByReimbursmentKey
			// != null ? reimbursementQueryByReimbursmentKey.getQueryRemarks() :
			// "");
		} else if (reimbursementObjectByKey.getStatus().getKey() != null
				&& reimbursementObjectByKey.getStatus().getKey()
						.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)) {
			reimbursementDTO.getPreauthMedicalProcessingDetails()
					.setApprovalRemarks(
							reimbursementObjectByKey.getApprovalRemarks());
		}
		if (claimByKey.getStatus() != null) {
			if (claimByKey.getStatus().getKey()
					.equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
					|| claimByKey.getStatus().getKey()
							.equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)) {
				reimbursementDTO.setIsWithDrawn(true);
			}
		}
		Boolean rejectionExistOrNot = acknowledgementDocumentsReceivedService
				.isRejectionExistOrNot(reimbursementObjectByKey.getKey());
		reimbursementDTO.setIsRejectionROD(rejectionExistOrNot);
		reimbursementDTO.setFvrCount(intimationService
				.getFVRCount(reimbursementDTO.getNewIntimationDTO().getKey()));

		Date date21 = new Date();
		loadRRCRequestValues(reimbursementDTO, insuredSumInsured,
				SHAConstants.CLAIM_REQUEST);
		Date date22 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY LOAD RRC REQUEST VALUES %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date21, date22));

		if (reimbursementDTO.getPreauthMedicalDecisionDetails()
				.getInvestigatorCode() != null) {
			TmpInvestigation tmpInvestigationByInvestigatorCode = investigationService
					.getTmpInvestigationByInvestigatorCode(reimbursementDTO
							.getPreauthMedicalDecisionDetails()
							.getInvestigatorCode());
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setInvestigatorName(tmpInvestigationByInvestigatorCode);
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setInvestigationReviewRemarks(
							reimbursementObjectByKey.getInvestigatorRemarks());
			reimbursementDTO.getPreauthMedicalDecisionDetails()
					.setInvestigationReportReviewed(true);
		}

		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if (strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(
					reimbursementDTO.getNewIntimationDTO().getPolicy()
							.getPolicyNumber(), reimbursementDTO.getNewIntimationDTO().getIntimationId());
			if (get64vbStatus != null
					&& SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				reimbursementDTO.setIsDishonoured(true);
			} else if (get64vbStatus != null
					&& (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
				reimbursementDTO.setIsPending(true);
			}
		}
		Date date23 = new Date();
		reimbursementDTO.setSittingsAmount(dbCalculationService
				.getDialysisAmount(
						reimbursementDTO.getNewIntimationDTO().getPolicy()
								.getProduct().getKey(),
						insuredSumInsured.longValue()).intValue());
		Date date24 = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY GET DIALYSIS AMOUT PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date23, date24));

		if (reimbursementObjectByKey.getStatus() != null
				&& !(reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER) || reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER))) {
			if (reimbursementDTO.getIsReverseAllocation()) {
				reimbursementDTO.setIsReverseAllocation(false);
			}
		}
		
		
		if (null != reimbursementObjectByKey.getStatus()
				&& (reimbursementObjectByKey
						.getStatus()
						.getKey()
						.equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)))
						{
								reimbursementDTO.setIsReBilling(true);
						}

		/*
		 * maternity flag from db
		 */
		Date date27 = new Date();
		String maternityFlag = dbCalculationService
				.getMaternityFlagForProduct(reimbursementDTO
						.getNewIntimationDTO().getPolicy().getProduct()
						.getKey());
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY MATERNITY FLAG  PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date27, new Date()));

		// if(maternityFlag.equalsIgnoreCase("Y")){
		// reimbursementDTO.setMaternityFlag(true);
		// }

		Date date28 = new Date();
		Boolean queryReceivedStatusRod = reimbursementQuerySerice
				.isQueryReceivedStatusRod(reimbursementObjectByKey.getKey());

		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY QUERY RECEIVED ROD %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date28, new Date()));

		if (queryReceivedStatusRod) {
			reimbursementDTO.setIsQueryReceived(true);
		}

		reimbursementDTO.setIsReverseAllocation(false);
		Date date29 = new Date();
		Map<String, String> popupMessages = dbCalculationService
				.getPOPUPMessages(reimbursementDTO.getPolicyKey(),
						reimbursementDTO.getNewIntimationDTO()
								.getInsuredPatient().getKey(),reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY POPUP MESSAGE PROCEDURE %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date28, new Date()));
		reimbursementDTO.setPopupMap(popupMessages);

		if (reimbursementDTO.getNewIntimationDTO().getHospitalDto() != null
				&& reimbursementDTO.getNewIntimationDTO().getHospitalDto()
						.getSuspiciousFlag() != null
				&& reimbursementDTO.getNewIntimationDTO().getHospitalDto()
						.getSuspiciousFlag().equalsIgnoreCase("Y")) {
			if (reimbursementDTO.getNewIntimationDTO().getHospitalDto()
					.getSuspiciousRemarks() != null) {
				Map<String, String> suspiciousMap = SHAUtils
						.getSuspiciousMap(reimbursementDTO
								.getNewIntimationDTO().getHospitalDto()
								.getSuspiciousRemarks());
				reimbursementDTO.setSuspiciousPopupMap(suspiciousMap);
			}
		}

		try {
			ImsUser imsUser = tableDTO.getImsUser();

			if (imsUser != null) {
				String[] userRoleList = imsUser.getUserRoleList();

				WeakHashMap<String, Object> escalateValidation = SHAUtils
						.getEscalateValidation(userRoleList);

				if ((Boolean) escalateValidation.get(SHAConstants.RMA6)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA6(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA5)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA5(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA4)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA4(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA3)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA3(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA2)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA2(true);
				} else if ((Boolean) escalateValidation.get(SHAConstants.RMA1)) {
					reimbursementDTO.getPreauthMedicalDecisionDetails()
							.setRMA1(true);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// <<<<<<< HEAD
		// reimbursementDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(reimbursementDTO.getNewIntimationDTO().getAdmissionDate(),
		// reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));
		Date date15 = new Date();
		// =======

		// reimbursementDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(reimbursementDTO.getNewIntimationDTO().getAdmissionDate(),
		// reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));
		String policyYear = reimbursementDTO.getNewIntimationDTO()
				.getPolicyYear();
		if (policyYear != null) {
			String intValue = policyYear.replaceAll("[^0-9]", "");
			Integer policyAgeing = SHAUtils
					.getIntegerFromStringWithComma(intValue);
			reimbursementDTO
					.setAdmissionDatePopup(policyAgeing != null ? policyAgeing
							.equals(0) ? true : false : false);
		}

		reimbursementDTO.setIsPEDInitiated(pedQueryService
				.isPEDInitiated(reimbursementDTO.getNewIntimationDTO()
						.getPolicy().getKey()));
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY PEQ QUERY SERVICE PED INTIATED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date15, new Date()));

		reimbursementDTO.setIsPEDInitiatedForBtn(reimbursementDTO
				.getIsPEDInitiated());

		if (reimbursementObjectByKey.getDocAcknowLedgement() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getDocumentReceivedFromId() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getDocumentReceivedFromId().getKey()
						.equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			Date date17 = new Date();
			/*Boolean isPedWatchList = pedQueryService
					.isTaskAvailableInWatchListForIntimation(reimbursementDTO
							.getNewIntimationDTO().getIntimationId());*/
			Boolean isPedWatchList = preauthService.getDBTaskForPreauth(reimbursementObjectByKey.getClaim().getIntimation().getIntimationId(), SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
			log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> TOTAL TIME CONSUMED BY PEQ WATCH LIST BPMN %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date17, new Date()));
			reimbursementDTO.setIsPedWatchList(isPedWatchList);
		}

		if ((reimbursementObjectByKey.getDocAcknowLedgement()
				.getReconsiderationRequest() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getReconsiderationRequest().equalsIgnoreCase("Y")) ||
						(reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))) {
			reimbursementDTO.setIsReconsiderationRequest(true);
		}

		List<InsuredPedDetails> pedByInsured = policyService
				.getPEDByInsured(reimbursementDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredId());

		reimbursementDTO.setInsuredPedDetails(pedByInsured);
		reimbursementDTO.setSearchFormDTO(tableDTO.getSearchDTO());
		//SHAUtils.setDefaultCopayValue(reimbursementDTO);

		if (reimbursementObjectByKey != null
				&& reimbursementObjectByKey.getDocAcknowLedgement() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag().equalsIgnoreCase("Y")) {
			reimbursementDTO
					.setRodTotalClaimedAmount(reimbursementObjectByKey
							.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() != null ? reimbursementObjectByKey
							.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() : 0d);
		}

		if (reimbursementObjectByKey != null
				&& reimbursementObjectByKey.getDocAcknowLedgement() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getPostHospitalisationFlag().equalsIgnoreCase("Y")) {
			reimbursementDTO
					.setRodTotalClaimedAmount(reimbursementObjectByKey
							.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() != null ? reimbursementObjectByKey
							.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() : 0d);
		}

		if (reimbursementObjectByKey.getClaim().getClaimType().getKey()
				.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
				&& reimbursementObjectByKey.getDocAcknowLedgement() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getDocumentReceivedFromId() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getDocumentReceivedFromId().getKey()
						.equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {

			if (hospitalById != null) {
				Boolean hospitalDiscount = SHAUtils
						.isHospitalDiscount(hospitalById);
				reimbursementDTO
						.setIsHospitalDiscountApplicable(hospitalDiscount);
			}
		}

		if (reimbursementDTO.getNewIntimationDTO() != null
				&& reimbursementDTO.getNewIntimationDTO().getInsuredPatient() != null
				&& reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
						.getKey() != null) {
			Long claimCount = preauthService.getClaimCount(reimbursementDTO
					.getNewIntimationDTO().getPolicy().getKey());
			if (claimCount != null) {
				reimbursementDTO.setClaimCount(claimCount);
			}
		}
		reimbursementDTO.setDbOutArray(tableDTO.getDbOutArray());
		
		if(null != reimbursementObjectByKey && null != reimbursementObjectByKey.getWorkPlace() &&
				(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementObjectByKey.getWorkPlace()))){
			reimbursementDTO.getPreauthDataExtractionDetails().setWorkOrNonWorkPlace(true);
		}
		
		LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
		legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
		reimbursementDTO.setLegalHeirDto(legalHeirDTO);

		List<NomineeDetailsDto> nomineeDtoList = intimationService.getNomineeDetailsListByTransactionKey(tableDTO.getRodKey());
		if( nomineeDtoList != null && !nomineeDtoList.isEmpty()){
			reimbursementDTO.getNewIntimationDTO().setNomineeList(nomineeDtoList);
		}
		if(reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null 
				&& reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()) {
			
			List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(tableDTO.getRodKey());
			if(legalHeirList != null && !legalHeirList.isEmpty()) {
				List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
				LegalHeirDTO legalHeirDto;
				for (LegalHeir legalHeir : legalHeirList) {
					 legalHeirDto = new LegalHeirDTO(legalHeir);
					 legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
					 legalHeirDTOList.add(legalHeirDto);
				}
				reimbursementDTO.setLegalHeirDTOList(legalHeirDTOList);
			}	
		}
		Map<String,Object> icrAgent  = dbCalculationService.getAgentAndBranchName(reimbursementDTO.getNewIntimationDTO().getIntimationId()); 
		 if(icrAgent != null && !icrAgent.isEmpty()) {
				String agentScorePoint = icrAgent.get(SHAConstants.ICR_AGENT_POINT).toString();
				reimbursementDTO.setIcrAgentValue(agentScorePoint);
				String smScorepoint = icrAgent.get(SHAConstants.SM_AGENT_POINT).toString();
				reimbursementDTO.setSmAgentValue(smScorepoint);
		 }
		reimbursementDTO.setScreenName(tableDTO.getScreenName());
		
		//CR2019234 reimbrushement
		if(reimbursementDTO != null && reimbursementDTO.getNewIntimationDTO().getIntimatedBy() != null && 
				reimbursementDTO.getNewIntimationDTO().getIntimatedBy().getId() != null && 
						reimbursementDTO.getNewIntimationDTO().getIntimatedBy().getId().toString().equalsIgnoreCase(SHAConstants.AGENT_CODE) 
				|| reimbursementDTO.getNewIntimationDTO().getIntimatedBy().getId().toString().equalsIgnoreCase(SHAConstants.SALES_MANAGER_CODE)){
			view.showInfoPopUp("This claim has been intimated by" + reimbursementDTO.getNewIntimationDTO().getIntimatedBy().getValue());
		}
		
		Policy policy = reimbursementDTO.getNewIntimationDTO().getPolicy();
		// added for PA installment payment process in policy level and product
		// level check
		Map<String, String> getPolicyInstallment = dbCalculationService
				.getInstallmentPaymentFlag(policy.getPolicyNumber(), policy.getProduct()
						.getKey());
		if (getPolicyInstallment != null && !getPolicyInstallment.isEmpty()) {
			reimbursementDTO.setPolicyInstalmentFlag(getPolicyInstallment
					.get(SHAConstants.FLAG) != null ? getPolicyInstallment
							.get(SHAConstants.FLAG) : "N");
			reimbursementDTO
			.setPolicyInstalmentMsg(getPolicyInstallment
					.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallment
							.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
		}
		if (reimbursementDTO.getPolicyInstalmentFlag() != null
				&& reimbursementDTO.getPolicyInstalmentFlag().equals(
						SHAConstants.YES_FLAG)) {
			Integer policyInstallmentAmount = PremiaService.getInstance()
					.getPolicyInstallmentAmount(policy
							.getPolicyNumber());
			reimbursementDTO.setPolicyInstalmentPremiumAmt(policyInstallmentAmount
					.doubleValue());

			String policyDueDate = PremiaService.getInstance()
					.getPolicyInstallmentDetails(policy
							.getPolicyNumber());
			//code added if we recevied instalment due amount as zero after pending amount adjusted to premia/bancs  need to set due date as admission date by noufel
			if((reimbursementDTO.getPolicyInstalmentPremiumAmt() != null && reimbursementDTO.getPolicyInstalmentPremiumAmt() == 0d) && 
					(policyDueDate == null || policyDueDate.isEmpty())) {

				reimbursementDTO.setPolicyInstalmentDueDate(reimbursementDTO.getPreauthDataExtractionDetails().getAdmissionDate());	
			}
			else{
				if (reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.formatTimeFromString(policyDueDate.toString()));	
				}
				else{
					reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.dateformatFromString(policyDueDate.toString()));
				}
			}
		}
		if(policy != null && policy.getProduct().getKey() != null && policy.getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
			reimbursementDTO.setIsHospitalExpenseCoverSelected(intimationService.getInsuredCoverByInsuredKey(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey(), "ACC-CVR-032"));
		}
		
		view.setPAHealthProcessClaimRequest(PAHealthClaimRequestWizard.class,
				reimbursementDTO);
		log.info("%%%%%%%%%%%%%%%%%%% MENU PRESENTER ----> SHOW MEDICAL APPROVAL CLAIM REQUEST METHOD ENDED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(startDate, new Date()));

		// }
	}
	}
	
//	Commented the below Cashless Screen
//	protected void showCovertClaim(
//			@Observes @CDIEvent(MenuItemBean.SEARCH_CONVERT_PA_CLAIM) final ParameterDTO parameters) {
//		BeanItemContainer<SelectValue> selectValueContainer = masterService
//				.getTmpCpuCodes();
//		
//		Map<String,Object> referenceData = new HashMap<String,Object>();
//		
//		referenceData.put("cpuCodeContainer",selectValueContainer);
//		
//		BeanItemContainer<SelectValue> hospitalTypeContainer = masterService
//				.getSelectValueContainer(ReferenceTable.HOSPITAL_TYPE);
//		referenceData.put("hospitalTypeContainer",hospitalTypeContainer);
//		
//		Stage stageByKey1 = preauthService.getStageByKey(ReferenceTable.PREAUTH_STAGE);
//		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.ENHANCEMENT_STAGE);
//		Stage statgeByKey = preauthService.getStageByKey(ReferenceTable.STANDALONE_WITHDRAW_STAGE); 
//		
//		SelectValue selectValue = new SelectValue();
//		selectValue.setId(stageByKey1.getKey());
//		selectValue.setValue(stageByKey1.getStageName());
//
//		SelectValue selectValue1 = new SelectValue();
//		selectValue1.setId(stageByKey2.getKey());
//		selectValue1.setValue(stageByKey2.getStageName());
//		
//		SelectValue selectValue2 = new SelectValue();
//		selectValue2.setId(statgeByKey.getKey());
//		selectValue2.setValue(statgeByKey.getStageName());
//		
//		BeanItemContainer<SelectValue> sourceContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
//		sourceContainer.addBean(selectValue1);
//		sourceContainer.addBean(selectValue);
//		sourceContainer.addBean(selectValue2);
//		
//		referenceData.put("sourceContainer",sourceContainer);
//		
//		SelectValue deathSelectValue = new SelectValue(null,SHAConstants.DEATH);
//		SelectValue accidentSelectValue = new SelectValue(null,SHAConstants.ACCIDENT);
//		BeanItemContainer accDeathContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
//		accDeathContainer.addBean(accidentSelectValue);
//		accDeathContainer.addBean(deathSelectValue);
//		
//		referenceData.put("accDeathContainer",accDeathContainer);
//		
//		view.setSearchConvertPAClaimViewG(SearchConvertPAClaimView.class, true, referenceData);
//	}
	
	protected void showConvertClaimPage(
			@Observes @CDIEvent(SHOW_CONVERT_PA_CLAIM) final ParameterDTO parameters) {
		SearchConvertClaimTableDto searchFormDto = (SearchConvertClaimTableDto) parameters
				.getPrimaryParameter();
		ConvertClaimDTO convertClaimDto = new ConvertClaimDTO();
		ConvertClaimDTO preauthDatas = new ConvertClaimDTO();
		ConvertClaimDTO status = new ConvertClaimDTO();
		Claim claimDetails = claimService.getClaimByKey(searchFormDto.getKey());
		OrganaizationUnit orgUnit = claimService.getInsuredOfficeNameByDivisionCode(claimDetails.getIntimation().getPolicy().getHomeOfficeCode());
		
		
		Double insuredSumInsured = 0d;
		
		if(null != claimDetails.getIntimation() && null != claimDetails.getIntimation().getPolicy() && 
				null != claimDetails.getIntimation().getPolicy().getProduct() && 
				null != claimDetails.getIntimation().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(claimDetails.getIntimation().getPolicy().getProduct().getKey()))){	
			
		
			insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				claimDetails.getIntimation().getInsured()
						.getInsuredId().toString(), claimDetails.getIntimation()
						.getPolicy().getKey(),claimDetails.getIntimation().getInsured().getLopFlag());
	}
		else
		{
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					claimDetails.getIntimation().getInsured()
							.getInsuredId().toString(), claimDetails.getIntimation()
							.getPolicy().getKey());
		}
		Double balanceSI = 0d;
		
		if(null != claimDetails.getIntimation() && null != claimDetails.getIntimation().getPolicy() && 
				null != claimDetails.getIntimation().getPolicy().getProduct() && 
				null != claimDetails.getIntimation().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(claimDetails.getIntimation().getPolicy().getProduct().getKey()))){			
		
		
			balanceSI = dbCalculationService.getBalanceSI(
				claimDetails.getIntimation().getPolicy().getKey(),
				claimDetails.getIntimation().getInsured().getKey(),
				claimDetails.getKey(), insuredSumInsured,
				claimDetails.getIntimation().getKey()).get(
				SHAConstants.TOTAL_BALANCE_SI);
		}
		else
		{

			balanceSI = dbCalculationService.getGPABalanceSI(
				claimDetails.getIntimation().getPolicy().getKey(),
				claimDetails.getIntimation().getInsured().getKey(),
				claimDetails.getKey(), insuredSumInsured,
				claimDetails.getIntimation().getKey()).get(
				SHAConstants.TOTAL_BALANCE_SI);
			
		}
		
		
		DocAcknowledgement docAcknowledgementBasedOnKey = null;		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) searchFormDto.getDbOutArray();
		//HumanTask humanTask = searchFormDto.getHumanTask();
		Long acknowledgementKey = (Long)wrkFlowMap.get(SHAConstants.PAYLOAD_ACK_KEY);
		
		if(acknowledgementKey != null && ! acknowledgementKey.equals(0l)){
		
			docAcknowledgementBasedOnKey = createRodService.getDocAcknowledgementBasedOnKey(acknowledgementKey);
		}
				
		
		NewIntimationDto intimationDto = intimationService
				.getIntimationDto(claimDetails.getIntimation());

		Intimation intimation = intimationService
				.getIntimationByKey(intimationDto.getKey());
		
		ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claimDetails);
		claimDTO.setNewIntimationDto(intimationDto);
						
		RRCDTO rrcDTO = new RRCDTO();
		rrcDTO.setNewIntimationDTO(intimationDto);
		rrcDTO.setClaimDto(claimDTO);
		rrcDTO.setStrUserName(searchFormDto.getUsername());
		loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured, SHAConstants.PROCESS_CONVERT_CLAIM);
		searchFormDto.setRrcDTO(rrcDTO);


		Hospitals hospitalDetails = hospitalService.getHospitalById(intimation
				.getHospital());
		ConvertClaimMapper convertClaimMapper = ConvertClaimMapper.getInstance();
		if (claimDetails != null) {
			convertClaimDto = convertClaimMapper.getClaimDTO(claimDetails);
			convertClaimDto.setClaimDTO(claimDTO);
		}
		
		if(orgUnit != null){
			convertClaimDto.setAfterConvCPUCode(orgUnit.getCpuCode());
		}
		
		if (null != claimDetails.getStatus()) {
			
			convertClaimDto.setAccDeathflag(claimDTO.getIncidenceFlag());
			convertClaimDto.setClaimStatus(claimDetails.getStatus()
					.getProcessValue());

			if (claimDetails.getStatus().getKey().equals(ReferenceTable.CLAIM_REGISTERED_STATUS)) {
				convertClaimDto.setDenialRemarks(claimDetails
						.getRegistrationRemarks());

			} else if (claimDetails.getStatus().getKey() == ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS
					|| claimDetails.getStatus().getKey() == ReferenceTable.PREAUTH_QUERY_STATUS) {
				Preauth preauthDetails = preauthService
						.getPreauthClaimKey(claimDetails.getKey());

				PreauthQuery preauthQuery = preauthService
						.getPreauthQueryList(preauthDetails.getKey());

				convertClaimDto
						.setDenialRemarks(preauthQuery.getQueryRemarks());
				convertClaimDto.setRemarks(preauthQuery.getQueryRemarks());
			} else if(claimDetails.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS) || claimDetails.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
				
				Preauth preauthDetails = preauthService
						.getLatestPreauthDetails(claimDetails.getKey());
					convertClaimDto.setDenialRemarks((preauthDetails.getRemarks() != null ? preauthDetails.getRemarks() : "") + (preauthDetails.getMedicalRemarks() != null ? preauthDetails.getMedicalRemarks() : ""));
					convertClaimDto.setRemarks(preauthDetails.getRemarks()); 				
			}
		}

		List<Long> keysList = new ArrayList<Long>();

		if (claimDetails != null) {
			List<Preauth> preauthList = preauthService
					.getPreauthByClaimKey(claimDetails.getKey());
			for (Preauth preauth : preauthList) {
				keysList.add(preauth.getKey());
			}

		}
		if (!keysList.isEmpty()) {
			Long preauthKey = Collections.max(keysList);
			Preauth preauth = preauthService.getPreauthById(preauthKey);
			if (null != preauth) {
				if (null != preauth.getStatus()) {
					if (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS) ) {
						convertClaimDto.setClaimStatus(preauth.getStatus()
								.getProcessValue());
						convertClaimDto.setDenialRemarks(preauth.getRemarks());
						convertClaimDto.setRemarks(preauth.getRemarks());
					}
				}
			}
		}
		
		DocUploadToPremia premiaData = processPremedicalService.getUploadedDataDocument(claimDetails.getIntimation().getIntimationId());   /*"CLI/2015/141125/0003907"*/
		
//		if(premiaData != null && premiaData.getPfdUpFFAXAmt() != null){
//			
//			if((SHAConstants.DEATH_FLAG).equalsIgnoreCase(claimDetails.getIncidenceFlag())){
//				convertClaimDto.setClaimedAmount(balanceSI);
//				convertClaimDto.setProvisionAmount(balanceSI);
//			}
//			else{
//				convertClaimDto.setClaimedAmount(Double.valueOf(premiaData.getPfdUpFFAXAmt()));
//				convertClaimDto.setProvisionAmount(Double.valueOf(premiaData.getPfdUpFFAXAmt()));
		if(premiaData != null && premiaData.getPfdUpFFAXAmt() != null){
				convertClaimDto.setClaimedAmount(Double.valueOf(premiaData.getPfdUpFFAXAmt()));
				//convertClaimDto.setProvisionAmount(claimDetails.getProvisionAmount());
		}
//			}	
//		}
		
		if(claimDetails.getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
			convertClaimDto.setProvisionAmount(claimDetails.getCurrentProvisionAmount());
		}

		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getConversionReasonByValue(ReferenceTable.REASON_FOR_CONVERSION);
		
		if(docAcknowledgementBasedOnKey != null){
			
			Double claimAmount =0d;
			
			if(docAcknowledgementBasedOnKey.getBenifitClaimedAmount() != null){
				claimAmount = docAcknowledgementBasedOnKey.getBenifitClaimedAmount();
			}

			convertClaimDto.setClaimedAmount(claimAmount);
			
		}
		convertClaimDto.setDbOutArray(searchFormDto.getDbOutArray());
		view.setConvertPAClaimView(ConvertPAClaimPageViewImpl.class, convertClaimDto,
				selectValueContainer, searchFormDto);
	}
	

	protected void showPAHospCoverClaimBillingWizard(
			@Observes @CDIEvent(PAMenuPresenter.SHOW_PA_HOSP_COVER_MEDICAL_APPROVAL_CLAIM_BILLING_SCREEN) final ParameterDTO parameters) {

		SearchProcessClaimBillingTableDTO tableDTO = (SearchProcessClaimBillingTableDTO) parameters
				.getPrimaryParameter();

		Long ackDocKey = createRodService
				.getLatestDocAcknowledgementKey(tableDTO.getRodKey());
	
				showPAHospCoverProcessClaimBillingWizard(tableDTO);
	}
	
	protected void showPAHospCoverProcessClaimBillingWizard(
			SearchProcessClaimBillingTableDTO tableDTO) {

		Reimbursement reimbursementObjectByKey = rodService
				.getReimbursementObjectByKey(tableDTO.getRodKey());
		ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
		PreauthDTO reimbursementDTO = mapper
				.getReimbursementDTO(reimbursementObjectByKey);
		
		if(("Y").equalsIgnoreCase(reimbursementDTO.getPreauthDataExtractionDetails().getReconsiderationFlag()))
		{
			List<ReconsiderRODRequestTableDTO> reconsiderDTOList = getReconsiderRODRequestForBillEntry(tableDTO.getRodKey());
			if(null != reconsiderDTOList && !reconsiderDTOList.isEmpty())
			{
				for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reconsiderDTOList) {
					if(null != reconsiderRODRequestTableDTO.getIsRejectReconsidered() && reconsiderRODRequestTableDTO.getIsRejectReconsidered())
					{
						reimbursementDTO.setIsRejectReconsidered(true);
					}
					else
					{
						reimbursementDTO.setIsRejectReconsidered(false);
					}
				}
			}
			reimbursementDTO.setReconsiderRodRequestList(getReconsiderRODRequestForBillEntry(tableDTO.getRodKey()));
		}

		Claim claimByKey = claimService.getClaimByClaimKey(reimbursementObjectByKey.getClaim().getKey());
		if(null != claimByKey)
		{
			if(null != reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId())
			{
				String  docReceivedFrom = reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue();
				reimbursementDTO.setPreviousAccountDetailsList(populatePreviousAccountDetails(claimByKey.getIntimation().getIntimationId(),docReceivedFrom));
			}
		}
		Long reimbursementKey = tableDTO.getRodKey();
		// Reimbursement previousLatestROD =
		// getPreviousLatestROD(claimByKey.getKey(), reimbursementObjectByKey);
		if (false) {
			// reimbursementKey = previousLatestROD.getKey();
			// reimbursementDTO = mapper.getReimbursementDTO(previousLatestROD);
			// reimbursementDTO.setIsPostHospitalization(true);
			// setReimbursmentTOPreauthDTO(mapper, claimByKey,
			// previousLatestROD, reimbursementDTO, true );
			// Hospitals hospitalById =
			// hospitalService.getHospitalById(claimByKey.getIntimation().getHospital());
			// ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails =
			// reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
			// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			// updateHospitalDetails.setHospitalState(hospitalById.getState());
			// updateHospitalDetails.setHospitalCode(hospitalById.getHospitalCode());
			// reimbursementDTO.setReconsiderationList(getReconsiderRODRequest(claimByKey));
			// reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
			// previousLatestROD.getKey());
			//
			// reimbursementDTO.setIsPreviousROD(true);
			// // reimbursementDTO.setPreviousROD(previousLatestROD);

		} else {
			reimbursementDTO
					.setReconsiderationList(getReconsiderRODRequest(claimByKey));

			reimbursementDTO = setReimbursmentTOPreauthDTO(mapper, claimByKey,
					reimbursementObjectByKey, reimbursementDTO, true, SHAConstants.BILLING);
			reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
					reimbursementObjectByKey.getKey());
		}

		UploadDocumentDTO reimbursementBenefitsValue = claimRequestBenefitsService
				.getReimbursementBenefitsValue(reimbursementObjectByKey
						.getKey());
		if (reimbursementBenefitsValue != null) {
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setUploadDocumentDTO(reimbursementBenefitsValue);
		}
		if (null != reimbursementBenefitsValue
				&& reimbursementBenefitsValue.getPatientCareBenefitFlag() != null
				&& reimbursementBenefitsValue.getPatientCareBenefitFlag()
						.equalsIgnoreCase("PC")) {
			List<ReimbursementBenefitsDetails> patientCareTableByBenefitKey = reimbursementService
					.getPatientCareTableByBenefitKey(reimbursementBenefitsValue
							.getPatientBenefitKey());
			List<PatientCareDTO> patientCareDTOList = new ArrayList<PatientCareDTO>();
			if (patientCareTableByBenefitKey != null
					&& !patientCareTableByBenefitKey.isEmpty()) {
				for (ReimbursementBenefitsDetails patientCareDetails : patientCareTableByBenefitKey) {
					PatientCareDTO dto = new PatientCareDTO();
					dto.setEngagedFrom(patientCareDetails.getEngagedFrom());
					dto.setEngagedTo(patientCareDetails.getEngagedTo());
					dto.setKey(patientCareDetails.getKey());
					patientCareDTOList.add(dto);
				}
			}
			reimbursementDTO.getPreauthDataExtractionDetails()
					.getUploadDocumentDTO()
					.setPatientCareDTO(patientCareDTOList);
		}

		/**
		 * Added for amount claimed table enhancement---- starts
		 * */
		Double insuredSumInsured = 0d;
		
		if(null != claimByKey &&  null != claimByKey.getIntimation() && 
				null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
				null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
				&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
		{
			insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), reimbursementDTO
						.getPolicyDto().getKey(),reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), reimbursementDTO
							.getPolicyDto().getKey());
		}

		Map<Integer, Object> detailsMap = new HashMap<Integer, Object>();
		

		if(reimbursementObjectByKey.getSectionCategory() != null){
			
			
			detailsMap = dbCalculationService
					.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
							.getProduct().getKey(), insuredSumInsured,
							reimbursementDTO.getNewIntimationDTO().getHospitalDto()
									.getRegistedHospitals().getCityClass(),
							reimbursementDTO.getNewIntimationDTO()
									.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),reimbursementObjectByKey.getSectionCategory(),"A");
		}else{
			
		Long sectionCategory = 0l;
		if(reimbursementDTO.getPolicyDto()
							.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY) || reimbursementDTO.getPolicyDto()
							.getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY) || reimbursementDTO.getPolicyDto()
							.getProduct().getKey().equals(ReferenceTable.DIABETES_FLOATER_POLICY))
		{
			sectionCategory = 1l;
		}
			
			detailsMap = dbCalculationService
					.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
							.getProduct().getKey(), insuredSumInsured,
							reimbursementDTO.getNewIntimationDTO().getHospitalDto()
									.getRegistedHospitals().getCityClass(),
							reimbursementDTO.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),sectionCategory,"0");
							/**
							 * The below code is commented as per sathish sir suggestion for ticket 2355 (production tracker).
							 * By default, the section category would be 1 for three products 
							 * MED-PRD-033 , MED-PRD-032 , MED-PRD-030
							 * 
							 * */
							
			
									//.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),0l,"0");
		}
		
		List<Double> copayValue = dbCalculationService.getProductCoPay(reimbursementDTO
				.getClaimDTO().getNewIntimationDto().getPolicy().getProduct()
				.getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
				.getInsuredPatient().getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
				.getInsuredPatient().getInsuredId(),reimbursementDTO.getNewIntimationDTO());
		reimbursementDTO.setProductCopay(copayValue);

		/**
		 * Added for amount claimed table enhancement---- ends
		 * */

		List<UploadDocumentDTO> rodSummaryDetails = rodService
				.getRODSummaryDetails(reimbursementObjectByKey.getKey());
		
		/**
		 * Fix for implemeting claims dms in bill entry screen.. -- starts
		 * **/
		if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
		{
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				if(null != claimByKey)
				{
					uploadDocumentDTO.setClaimNo(claimByKey.getClaimId());
					uploadDocumentDTO.setDmsDocumentDTOList(getClaimsDMSList(claimByKey.getIntimation().getIntimationId()));
				}
			}
		}
		
		reimbursementDTO = getProrataFlagFromProduct(reimbursementDTO);
		
		reimbursementDTO.setProrataDeductionFlag(reimbursementObjectByKey.getProrataDeductionFlag());
		/**
		 * product based variable is added to enable or disable the component in page level.
		 * This would be static. -- starts
		 * */
		reimbursementDTO.setPackageAvailableFlag(reimbursementObjectByKey.getPackageAvailableFlag());


		List<Long> documentSummaryKeys = new ArrayList<Long>();
		for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
			
			documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
			uploadDocumentDTO.setStatus(true);
			List<RODBillDetails> billEntryDetails = rodService
					.getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
			List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
			if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
				for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
					/*
					 * <<<<<<< HEAD
					 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
					 * uploadDocumentDTO)); =======
					 */
					dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,
							uploadDocumentDTO));
					// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
				}
			}
			uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
					.get(8));
			uploadDocumentDTO
					.setProductBasedICURent((Double) detailsMap.get(9));
			/*uploadDocumentDTO.setProductBasedAmbulanceAmt((Double) detailsMap
					.get(15));*/
			uploadDocumentDTO.setProductBasedAmbulanceAmt(getProductBasedAmbulanceAmt(tableDTO.getRodKey()));
			uploadDocumentDTO.setStatus(false);
			uploadDocumentDTO.setBillEntryDetailList(dtoList);
			uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
			uploadDocumentDTO.setRodKey(reimbursementObjectByKey.getKey());

			uploadDocumentDTO.setStrUserName(tableDTO.getUsername());

			if (uploadDocumentDTO != null) {
				if ((ReferenceTable.BENEFITS_PATIENT_CARE)
						.equalsIgnoreCase(uploadDocumentDTO
								.getPatientCareBenefitFlag())) {
					List<PatientCareDTO> patientCareList = claimRequestBenefitsService
							.getPatientCareDetails(uploadDocumentDTO
									.getPatientBenefitKey());
					if (null != patientCareList && !patientCareList.isEmpty()) {
						uploadDocumentDTO.setPatientCareDTO(patientCareList);
					}
					// rodDTO.getAddOnBenefitsDTO().setAdmittedNoOfDays(uploadDocumentDTO.getPatientCareNoofDays());
				}
			}
		}
		List<UploadDocumentDTO> rodBillSummaryDetails = rodService
				.getRODBillSummaryDetails(reimbursementObjectByKey.getKey(), mapper);
		
		if(rodBillSummaryDetails != null){
			for (UploadDocumentDTO uploadDocumentDTO2 : rodBillSummaryDetails) {
				uploadDocumentDTO2.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
			}
		}
		
		reimbursementDTO.getUploadDocDTO().setBillingWorkSheetUploadDocumentList(rodBillSummaryDetails);

		Double totalBilledAmount = reimbursementService
				.getTotalBilledAmount(documentSummaryKeys);

		reimbursementDTO.setAmountConsidered(totalBilledAmount != null ? String
				.valueOf(totalBilledAmount.intValue()) : "0");
		reimbursementDTO
				.setInitialAmountConsidered(totalBilledAmount != null ? String
						.valueOf(totalBilledAmount.intValue()) : "0");
		reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
	//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTask());
		reimbursementDTO.setKey(reimbursementObjectByKey.getKey());
		reimbursementDTO.setRodNumber(reimbursementObjectByKey.getRodNumber());

		setCategoryFlag(reimbursementObjectByKey, reimbursementDTO);
		
		if (!reimbursementDTO.getHospitalizaionFlag()) {
			reimbursementDTO = setHospitalizationAmount(reimbursementDTO);
		}

		List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
				.search(reimbursementObjectByKey.getClaim().getKey(),false);

		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

			previousPreAuthTableDTO.setRequestedAmt(preauthService
					.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
							previousPreAuthTableDTO.getClaimKey()));
			newList.add(previousPreAuthTableDTO);

		}
		
		

		reimbursementDTO.setPreviousPreauthTableDTO(newList);

		//reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());
		reimbursementDTO.setStrUserName(tableDTO.getUsername());
		reimbursementDTO.setStrPassword(tableDTO.getPassword());
        reimbursementDTO.setTaskNumber(tableDTO.getTaskNumber());
		
		
		reimbursementDTO.getPreauthMedicalDecisionDetails().setBillingRemarks(
				"");
		
		
		List<AddOnBenefitsDTO> populateAddOnBenefitsTableValues = claimRequestBenefitsService
				.populateAddOnBenefitsTableValues(
						reimbursementDTO.getNewIntimationDTO(),
						reimbursementObjectByKey.getClaim().getKey(),
						reimbursementObjectByKey.getKey(),
						reimbursementDTO.getProductCopay(),null);
		reimbursementDTO.getPreauthDataExtractionDetails()
				.setAddOnBenefitsDTOList(populateAddOnBenefitsTableValues);
		reimbursementDTO = roomRentNursingMapping(reimbursementDTO, 8l, 9l, false);
		reimbursementDTO = roomRentNursingMapping(reimbursementDTO, 10l, 11l, true);
		reimbursementDTO.getPreauthDataExtractionDetails()
				.setDocAckknowledgement(
						reimbursementObjectByKey.getDocAcknowLedgement());

		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getHospitalizationRepeatFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getHospitalizationRepeatFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setIsHospitalizationRepeat(true);
		}
		
		if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
			MedicalApprover medicalApprover = reimbursementService
					.getMedicalApprover(reimbursementObjectByKey.getKey());
			if (medicalApprover != null) {
				reimbursementDTO.setPreviousRemarks(medicalApprover
						.getApproverReply());
				reimbursementDTO.setPreviousReasonForReferring(medicalApprover
						.getReasonForReferring());
			}
			reimbursementDTO.setIsReferToBilling(true);
		}
		
		if (claimByKey.getClaimType() != null
				&& claimByKey.getClaimType().getKey()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {

//			PreMedicalMapper premedicalMapper = new PreMedicalMapper();
			Preauth previousPreauth = getPreviousPreauth(claimByKey.getKey());
			
			if(previousPreauth != null) {
				reimbursementDTO.setPreauthKey(previousPreauth.getKey());
			}
		}
		
		if(claimByKey.getStatus() != null) {
			if(claimByKey.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) ||  claimByKey.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)) {
				reimbursementDTO.setIsWithDrawn(true);
			}
		}
		
		loadRRCRequestValues(reimbursementDTO,insuredSumInsured,SHAConstants.BILLING);
		
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber(),reimbursementDTO.getNewIntimationDTO().getIntimationId());
			if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				reimbursementDTO.setIsDishonoured(true);
			}  else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
				reimbursementDTO.setIsPending(true);
			}
		}
		reimbursementDTO.setSittingsAmount(dbCalculationService.getDialysisAmount(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(), insuredSumInsured.longValue()).intValue());
		Map<String, String> popupMessages = dbCalculationService.getPOPUPMessages(reimbursementDTO.getPolicyKey(), reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
				reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
		reimbursementDTO.setPopupMap(popupMessages);
		
		
		if(reimbursementDTO.getNewIntimationDTO().getHospitalDto() != null && reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag() != null
				&& reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag().equalsIgnoreCase("Y")){
			if(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks() != null){
				Map<String, String> suspiciousMap = SHAUtils.getSuspiciousMap(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks());
				reimbursementDTO.setSuspiciousPopupMap(suspiciousMap);
			}
		}
		// If hospitalization is not processed then remaining ROD should be rejected.
		Reimbursement hospROD = reimbursementService.getHospitalizationROD(reimbursementObjectByKey.getClaim().getKey());
		if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
			if(hospROD == null) {
				reimbursementDTO.setIsHospitalizationRejected(true);
			}
		}
		
		Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
				.getIntimation().getHospital());
		
		paymentDetailsForfinancial(reimbursementObjectByKey, reimbursementDTO,
				hospitalById);
		
		setReimbursementValues(reimbursementObjectByKey, reimbursementDTO);
		
       Reimbursement hospitalizationOrPartialROD = reimbursementService.getHospitalizationOrPartialROD(reimbursementObjectByKey.getClaim().getKey());
		
		if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			
			
			Integer seqNumber = 0;
			
			if(reimbursementObjectByKey.getRodNumber() != null){
				String[] split = reimbursementObjectByKey.getRodNumber().split("/");
				seqNumber = Integer.valueOf(split[split.length - 1]);
			}
			
			if(hospitalizationOrPartialROD == null && ! seqNumber.equals(1)) {
//				reimbursementDTO.setIsDishonoured(true);
				reimbursementDTO.setIsHospitalizationRejected(true);
			}
		}
		
		Map<String, Integer> productBenefitFlag = dbCalculationService.getProductBenefitFlag(reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey());
		
		if(productBenefitFlag.get(SHAConstants.PRE_HOSP_FLAG).equals(0)) {
			reimbursementDTO.setIsPreHospApplicable(false);
		}
		
		if(productBenefitFlag.get(SHAConstants.POST_HOSP_FLAG).equals(0)) {
			reimbursementDTO.setIsPostHospApplicable(false);
		}
		
		if(productBenefitFlag.get(SHAConstants.LUMP_SUM_FLAG).equals(1)) {
			reimbursementDTO.setIsLumpsumApplicable(true);
		}
		
		if(productBenefitFlag.get(SHAConstants.HOSPITALCASH_FLAG).equals(1)) {
			reimbursementDTO.setIsHospitalCashApplicable(true);
		}
		
		if(productBenefitFlag.get(SHAConstants.PATIENTCARE_FLAG).equals(1)) {
			reimbursementDTO.setIsPatientCareApplicable(true);
		}
		
		
		MastersValue networkHospitalType = masterService.getMaster(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getNetworkHospitalTypeId());
		reimbursementDTO.setNetworkHospitalType(networkHospitalType.toString());
		
		reimbursementDTO = checkFinalEnhancement(reimbursementDTO);

		
		try {
			if((reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest() != null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest().equalsIgnoreCase("Y"))
					|| (reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))) {
				if(reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag() ==  null || reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag().equalsIgnoreCase("N")) {
					reimbursementDTO.setIsReconsiderationRequest(true);
					if(reimbursementService.isClaimPaymentAvailable(reimbursementObjectByKey.getRodNumber())) {
						Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
						reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
					} else {
						/**
						 * For reject reconsider, cancel rod button was enabled. But for this ticket GALAXYMAIN-6222	
						 * which was raised in PA, now cancel rod button is diabled for rejection case to.
						 * Hence below code was commented.
						 * */
					//	reimbursementDTO.setIsReconsiderationRequest(false);
					}
//					Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
//					reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			reimbursementDTO.setIsCashlessType(true);
		}
		if(reimbursementDTO.getIsCashlessType() && reimbursementDTO.getHospitalizaionFlag() && (reimbursementDTO.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY))){
			reimbursementDTO.setIsDirectToBilling(true);
			Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursementDTO.getClaimDTO().getKey());
			reimbursementDTO.getClaimDTO().setLatestPreauthKey(latestPreauth.getKey());
		}
		reimbursementDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(reimbursementDTO.getNewIntimationDTO().getAdmissionDate(), reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));
		reimbursementDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey()));
		reimbursementDTO.setIsPEDInitiatedForBtn(reimbursementDTO.getIsPEDInitiated());
		
		List<InsuredPedDetails> pedByInsured = policyService.getPEDByInsured(reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		reimbursementDTO.setInsuredPedDetails(pedByInsured);
		
		if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			if(reimbursementDTO.getHospitalizaionFlag()) {
				Reimbursement partialHospitalizationRODWithSettled = getPartialHospitalizationRODWithSettled(claimByKey.getKey());
				ReimbursementCalCulationDetails hosptialization = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursementDTO.getKey(), ReferenceTable.HOSPITALIZATION);
				if(hosptialization != null) {
					reimbursementDTO.setBillingApprovedAmount(hosptialization.getPayableToHospAftTDS() != null ? hosptialization.getPayableToHospAftTDS().doubleValue() : 0d);
					reimbursementDTO.setPayableToHospAmt(hosptialization.getPayableToHospital() != null ? hosptialization.getPayableToHospital().doubleValue() : 0d);
					reimbursementDTO.setHospDiscountAmount(hosptialization.getHospitalDiscount() != null ? hosptialization.getHospitalDiscount().doubleValue() : 0d);
				}
				if(partialHospitalizationRODWithSettled != null) {
					ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(partialHospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
					reimbursementDTO.setPayableToInsAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium().doubleValue() : 0d);
					reimbursementDTO.setFAApprovedAmount(partialHospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? partialHospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
				}
			} else if(reimbursementDTO.getPartialHospitalizaionFlag()) {
				Reimbursement hospitalizationRODWithSettled = getHospitalizationRODWithSettled(claimByKey.getKey());
				if(hospitalizationRODWithSettled != null) {
					ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(hospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
					reimbursementDTO.setPayableToHospAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital().doubleValue() : 0d);
					reimbursementDTO.setHospDiscountAmount(reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d);
					Double amount = (reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d) + (hospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? hospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
					reimbursementDTO.setFAApprovedAmount(amount);
				}
			}
		}
		
		Boolean queryReceivedStatusRod = reimbursementQuerySerice.isQueryReceivedStatusRod(reimbursementObjectByKey.getKey());
		 
		 if(queryReceivedStatusRod){
			 reimbursementDTO.setIsQueryReceived(true);
		 }
		 
		 
			List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = acknowledgementDocumentsReceivedService.getReimbursementCalculationDetails(reimbursementObjectByKey.getKey());
			if(reimbursementCalculationDetails != null && !reimbursementCalculationDetails.isEmpty()) {
				for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
					if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
							reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.HOSPITALIZATION)){
						
						HopitalizationCalulationDetailsDTO hospitalizationCalcDTO = getHospitalizationCalculationDTO(reimbursementCalCulationDetails2,reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
						if(reimbursementDTO.getDocumentReceivedFromId() != null && reimbursementDTO.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
					           hospitalizationCalcDTO.setBalanceToBePaid(0);
						}
						reimbursementDTO.setHospitalizationCalculationDTO(hospitalizationCalcDTO);
						
					}
					else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
							reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)){
						PreHopitalizationDetailsDTO preHospitalizationCalcDTO = getPreHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
						reimbursementDTO.setPreHospitalizationCalculationDTO(preHospitalizationCalcDTO);
						
					}else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
							reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
						PostHopitalizationDetailsDTO postHospitalizationCalcDTO = getPostHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
						reimbursementDTO.setPostHospitalizationCalculationDTO(postHospitalizationCalcDTO);
					}
				}
			}
		
			reimbursementDTO.setAmountConsidered(reimbursementDTO.getAmountConsidedAfterCoPay() != null ? String
					.valueOf(reimbursementDTO.getAmountConsidedAfterCoPay().intValue()) : "0");
		 
//		 reimbursementService.setBPMForClaimBilling(reimbursementDTO, false, "MEDICAL", reimbursementObjectByKey);
			Double totalClaimedAmount = reimbursementService.getTotalClaimedAmount(reimbursementObjectByKey);
			 reimbursementDTO.setRodTotalClaimedAmount(totalClaimedAmount);
			 
			 if(reimbursementDTO.getAmountConsidedAfterCoPay() != null && totalBilledAmount != null && !reimbursementDTO.getAmountConsidedAfterCoPay().equals(totalBilledAmount)) {
				 reimbursementDTO.setAmountConsidered(String.valueOf(totalBilledAmount.longValue()));
			 }

			 //SHAUtils.setDefaultCopayValue(reimbursementDTO);

			 if(reimbursementDTO.getIsReconsiderationRequest() != null && reimbursementDTO.getIsReconsiderationRequest()){
				 reimbursementDTO.setIsReverseAllocation(false);
			 }

			 if(reimbursementObjectByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
					 && reimbursementObjectByKey.getDocAcknowLedgement() != null && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
					 && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
			 
					 if(hospitalById != null){
							Boolean hospitalDiscount = SHAUtils.isHospitalDiscount(hospitalById);
							reimbursementDTO.setIsHospitalDiscountApplicable(hospitalDiscount);
					 }
			 }
			 
			 /*for bancs*/
			if(reimbursementObjectByKey.getCatastrophicLoss() != null) {
				
				SelectValue value = masterService.getCatastropheData(reimbursementObjectByKey.getCatastrophicLoss());
				reimbursementDTO.getPreauthDataExtractionDetails().setCatastrophicLoss(value);
			}
			if(reimbursementObjectByKey.getNatureOfLoss() != null) {
				
				SelectValue value = masterService.getMasterValueForNatureCause(reimbursementObjectByKey.getNatureOfLoss());
				reimbursementDTO.getPreauthDataExtractionDetails().setNatureOfLoss(value);
			}
			if(reimbursementObjectByKey.getCauseOfLoss() != null) {
				SelectValue value = masterService.getMasterValueForNatureCause(reimbursementObjectByKey.getCauseOfLoss());
				reimbursementDTO.getPreauthDataExtractionDetails().setCauseOfLoss(value);
			}
		 
			
//			 if(reimbursementDTO.getNewIntimationDTO() != null && reimbursementDTO.getNewIntimationDTO().getInsuredPatient() != null &&
//					 reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey() != null){
//					Long claimCount = preauthService.getClaimCount(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
//					if(claimCount != null){
//						reimbursementDTO.setClaimCount(claimCount);
//					}
//				}


		view.setPAHospCoverProcessClaimBilling(PAHospCoverBillingWizard.class, reimbursementDTO);
		
	}

	protected void showProcessClaimFinancials(
			@Observes @CDIEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_FINANCIALS) final ParameterDTO parameters) {

		BeanItemContainer<SelectValue> claimType = masterService
				.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
		BeanItemContainer<SelectValue> productName = masterService
				.getSelectValueContainerForProduct();
		BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();
		BeanItemContainer<SelectValue> type = masterService
				.getStatusByStage(ReferenceTable.BILLING_STAGE);

		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
				.getSelectValueForPriority();

		// BeanItemContainer<SelectValue> statusByStage =
		// masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);

		Stage stageByKey = preauthService
				.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);

		Stage stageByKey2 = preauthService
				.getStageByKey(ReferenceTable.BILLING_STAGE);

		SelectValue selectValue = new SelectValue();
		selectValue.setId(stageByKey.getKey());
		selectValue.setValue(stageByKey.getStageName());

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());

		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		statusByStage.addBean(selectValue);
		statusByStage.addBean(selectValue1);

		view.setViewForPAHealthSearchClaimFinancial(
				PAHealthSearchProcessClaimFinancialsView.class, true,
				claimType, productName, cpuCode, type, selectValueForPriority,
				statusByStage);
	}

	protected void showProcessClaimFinancialsNonHospital(
			@Observes @CDIEvent(MenuItemBean.PA_NON_HOSP_PROCESS_CLAIM_FINANCIAL) final ParameterDTO parameters) {

		BeanItemContainer<SelectValue> claimType = masterService
				.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
		BeanItemContainer<SelectValue> productName = masterService
				.getSelectValueContainerForProduct();
		BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();
		BeanItemContainer<SelectValue> type = masterService
				.getStatusByStage(ReferenceTable.BILLING_STAGE);

		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
				.getSelectValueForPriority();

		// BeanItemContainer<SelectValue> statusByStage =
		// masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);

		Stage stageByKey = preauthService
				.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);

		Stage stageByKey2 = preauthService
				.getStageByKey(ReferenceTable.BILLING_STAGE);

		SelectValue selectValue = new SelectValue();
		selectValue.setId(stageByKey.getKey());
		selectValue.setValue(stageByKey.getStageName());

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());

		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		statusByStage.addBean(selectValue);
		statusByStage.addBean(selectValue1);

		view.setViewForPAHealthSearchClaimFinancialNonHosp(
				PASearchProcessClaimFinancialsNonHospView.class, true,
				claimType, productName, cpuCode, type, selectValueForPriority,
				statusByStage);
	}
	

	private void setReimbursementValues(Reimbursement reimbursement,
			PreauthDTO reimbursmentDTO) {
		reimbursmentDTO.setKey(reimbursement.getKey());
		reimbursmentDTO.setStatusKey(reimbursement.getStatus().getKey());
		reimbursmentDTO.setStageKey(reimbursement.getStage().getKey());

		reimbursmentDTO.getNewIntimationDTO().setInsuredDeceasedFlag(dbCalculationService.getInsuredPatientStatus(reimbursmentDTO.getNewIntimationDTO().getPolicy().getKey(), reimbursmentDTO.getNewIntimationDTO().getInsuredPatient().getKey()));
		
		/**
		 * Bank id as been changed from Long to MAS_BANK type. hence below line
		 * as been changed accordingly
		 * */
		reimbursmentDTO.setBankId(reimbursement.getBankId());
		reimbursmentDTO.setAccountNumber(reimbursement.getAccountNumber());
		reimbursmentDTO.setPayableAt(reimbursement.getPayableAt());
		reimbursmentDTO.setPayeeEmailId(reimbursement.getPayeeEmailId());
		reimbursmentDTO.setPanNumber(reimbursement.getPanNumber());
		reimbursmentDTO.setPayeeName(reimbursement.getPayeeName());
		reimbursmentDTO.setPaymentModeId(reimbursement.getPaymentModeId());

		reimbursmentDTO.setRodNumber(reimbursement.getRodNumber());
		PreauthDataExtaractionDTO dataExtraction = reimbursmentDTO
				.getPreauthDataExtractionDetails();
		dataExtraction.setBankId(reimbursement.getBankId());
		dataExtraction.setAccountNo(reimbursement.getAccountNumber());
		dataExtraction.setPayableAt(reimbursement.getPayableAt());
		dataExtraction.setEmailId(reimbursement.getPayeeEmailId());
		dataExtraction.setPanNo(reimbursement.getPanNumber());

		dataExtraction.setDocAckknowledgement(reimbursement
				.getDocAcknowLedgement());
		dataExtraction.setDocAcknowledgementKey(reimbursement
				.getDocAcknowLedgement() != null ? reimbursement
				.getDocAcknowLedgement().getKey() : null);

		reimbursmentDTO
				.setDocumentReceivedFromId(reimbursement
						.getDocAcknowLedgement().getDocumentReceivedFromId() != null ? reimbursement
						.getDocAcknowLedgement().getDocumentReceivedFromId()
						.getKey()
						: 0l);

		dataExtraction.setPaymentModeFlag(reimbursement.getPaymentModeId());
		dataExtraction.setPanNo(reimbursement.getPanNumber());
		dataExtraction.setChangeInReasonDOA(reimbursement.getReasonForChange());
		dataExtraction.setLegalFirstName(reimbursement.getLegalHeirFirstName());
		dataExtraction.setLegalLastName(reimbursement.getLegalHeirLastName());
		dataExtraction.setLegalMiddleName(reimbursement
				.getLegalHeirMiddleName());
		dataExtraction.setAccountNo(reimbursement.getAccountNumber());
		// dataExtraction.setPayeeName();
		dataExtraction.setPayableAt(reimbursement.getPayableAt());
		/*dataExtraction.setBillingInternalRemarks(reimbursement.getBillingInternalRemarks());
		dataExtraction.setFaInternalRemarks(reimbursement.getFaInternalRemarks());*/
	}

	private void paymentDetailsForfinancial(Reimbursement reimbursement,
			PreauthDTO reimbursementDto, Hospitals hospitals) {
		try {
			PreauthDataExtaractionDTO dataExtractionDto = reimbursementDto
					.getPreauthDataExtractionDetails();
			dataExtractionDto.setEmailId(reimbursement.getPayeeEmailId());

			dataExtractionDto.setIfscCode(hospitals.getIfscCode());
			dataExtractionDto.setPayableAt(reimbursement.getPayableAt());

			if (reimbursement.getPayeeName() != null) {
				SelectValue selected = new SelectValue();
				selected.setId(1l);
				selected.setValue(reimbursement.getPayeeName());
				dataExtractionDto.setPayeeName(selected);
			}

			if (reimbursement.getPaymentModeId() != null
					&& reimbursement.getPaymentModeId().equals(
							ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)) {

				BankMaster masBank = createRodService.getBankMasterByKey(
						reimbursement.getBankId(), masterService);
				if (masBank != null) {
					dataExtractionDto.setBankName(masBank.getBankName());
					dataExtractionDto.setBranch(masBank.getBranchName());
					dataExtractionDto.setCity(masBank.getCity());
					dataExtractionDto.setIfscCode(masBank.getIfscCode());
				}
				dataExtractionDto
						.setAccountNo(reimbursement.getAccountNumber());

			} else {

				if (dataExtractionDto.getIfscCode() != null) {
					BankMaster masBank = createRodService.getBankMaster(
							dataExtractionDto.getIfscCode(), masterService);
					if (masBank != null) {
						dataExtractionDto.setBankName(masBank.getBankName());
						dataExtractionDto.setBranch(masBank.getBranchName());
						dataExtractionDto.setCity(masBank.getCity());
					}
					dataExtractionDto.setAccountNo(reimbursement
							.getAccountNumber());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected Reimbursement getHospitalizationROD(Long claimKey,
			Reimbursement currentReimbursement) {
		Reimbursement previousROD = null;
		Integer previousNumber = 1;
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()
				&& previousRODByClaimKey.size() > 1) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				String[] eachSplit = reimbursement.getRodNumber().split("/");
				String eachSplitNo = eachSplit[eachSplit.length - 1];
				Integer eachRODNo = Integer.valueOf(eachSplitNo);
				if (reimbursement.getDocAcknowLedgement()
						.getHospitalisationFlag() != null
						&& reimbursement.getDocAcknowLedgement()
								.getHospitalisationFlag().toLowerCase()
								.equalsIgnoreCase("y")) {
					previousROD = reimbursement;
					break;
				}
			}
		}

		return previousROD;
	}

	protected Reimbursement getPartialHospitalizationROD(Long claimKey,
			Reimbursement currentReimbursement) {
		Reimbursement previousROD = null;
		Integer previousNumber = 1;
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()
				&& previousRODByClaimKey.size() > 1) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				String[] eachSplit = reimbursement.getRodNumber().split("/");
				String eachSplitNo = eachSplit[eachSplit.length - 1];
				Integer eachRODNo = Integer.valueOf(eachSplitNo);
				if (reimbursement.getDocAcknowLedgement()
						.getPartialHospitalisationFlag() != null
						&& reimbursement.getDocAcknowLedgement()
								.getPartialHospitalisationFlag().toLowerCase()
								.equalsIgnoreCase("y")) {
					previousROD = reimbursement;
					break;
				}
			}
		}

		return previousROD;
	}

	private PreauthDTO checkFinalEnhancement(PreauthDTO reimbursementDTO) {
		if (reimbursementDTO.getClaimDTO().getClaimType() != null
				&& reimbursementDTO.getClaimDTO().getClaimType().getId()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			Preauth latestPreauthByClaim = preauthService
					.getLatestPreauthByClaim(reimbursementDTO.getClaimKey());
			Preauth preauthByClaimKey = preauthService
					.getPreauthClaimKey(reimbursementDTO.getClaimKey());
			if (latestPreauthByClaim.getEnhancementType() != null
					&& latestPreauthByClaim.getEnhancementType()
							.equalsIgnoreCase("F")) {
				reimbursementDTO.setIsFinalEnhancement(true);
			}
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setPreauthTotalApprAmt(
							latestPreauthByClaim.getTotalApprovalAmount());
		}

		return reimbursementDTO;
	}

	private PreHopitalizationDetailsDTO getPreHospitalizationDTO(
			ReimbursementCalCulationDetails reimbursementCalcDetails,
			PreauthDTO reimbursementDTO, Boolean isBilling) {

		PreHopitalizationDetailsDTO dto = new PreHopitalizationDetailsDTO();

		dto.setPayableToInsuredAftPremiumAmt(reimbursementCalcDetails
				.getPayableInsuredAfterPremium());
		dto.setBalancePremiumAmt(reimbursementCalcDetails
				.getDeductedBalancePremium());
		dto.setPayableToInsAmt(reimbursementCalcDetails.getPayableToInsured());
		dto.setClaimRestrictionAmt(reimbursementCalcDetails
				.getClaimRestrictionAmount());
		dto.setNetPayable(reimbursementCalcDetails
				.getNetEligiblePayableAmount());
		dto.setPayableAmt(reimbursementCalcDetails.getNetPayableAmount());
		dto.setCopayAmt(reimbursementCalcDetails.getCopayAmount());
		dto.setAmountConsidered(reimbursementCalcDetails.getEligibleAmount());
		dto.setAmountAlreadyPaid(reimbursementCalcDetails
				.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
				.getAmountAlreadyPaidAmt() : 0);
		dto.setBalanceToBePaid(reimbursementCalcDetails.getBalanceToBePaidAmt() != null ? reimbursementCalcDetails
				.getBalanceToBePaidAmt() : 0);

		OtherInsPreHospSettlementDetailsDTO otherInsDTO = new OtherInsPreHospSettlementDetailsDTO();
		otherInsDTO
				.setBalanceAmt(reimbursementCalcDetails.getTpaBalanceAmt() != null ? reimbursementCalcDetails
						.getTpaBalanceAmt() : 0);
		otherInsDTO.setNonMedicalAmt(reimbursementCalcDetails
				.getTpaNonMedicalAmt() != null ? reimbursementCalcDetails
				.getTpaNonMedicalAmt() : 0);
		otherInsDTO.setTotalClaimedAmt(reimbursementCalcDetails
				.getTpaClaimedAmt() != null ? reimbursementCalcDetails
				.getTpaClaimedAmt() : 0);
		otherInsDTO.setTpaSettledAmt(reimbursementCalcDetails
				.getTpaSettledAmt() != null ? reimbursementCalcDetails
				.getTpaSettledAmt() : 0);
		otherInsDTO.setPayableToIns(reimbursementCalcDetails
				.getTpaPayableToInsured() != null ? reimbursementCalcDetails
				.getTpaPayableToInsured() : 0);
		otherInsDTO
				.setPayableAmt(reimbursementCalcDetails.getTpaPayableAmt() != null ? reimbursementCalcDetails
						.getTpaPayableAmt() : 0);
		otherInsDTO
				.setHospPayableAmt(reimbursementCalcDetails
						.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
						.getPayableInsuredAfterPremium() : 0);
		Integer amt = otherInsDTO.getTotalClaimedAmt()
				- otherInsDTO.getNonMedicalAmt();
		otherInsDTO.setNetClaimedAmt(amt > 0 ? amt : 0);
		reimbursementDTO.setOtherInsPreHospSettlementCalcDTO(otherInsDTO);
		Integer alreadyPaidAmt = 0;
		if (reimbursementDTO.getIsReconsiderationRequest()) {
			if (reimbursementDTO.getPreauthMedicalDecisionDetails()
					.getOtherInsurerApplicableFlag() != null
					&& reimbursementDTO.getPreauthMedicalDecisionDetails()
							.getOtherInsurerApplicableFlag()
							.equalsIgnoreCase("Y")) {
				alreadyPaidAmt = reimbursementCalcDetails.getTpaPayableAmt() != null ? reimbursementCalcDetails
						.getTpaPayableAmt()
						: 0 - (reimbursementCalcDetails
								.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
								.getAmountAlreadyPaidAmt() : 0);
			} else {
				alreadyPaidAmt = (reimbursementCalcDetails
						.getAmountAlreadyPaidAmt() != null) ? (reimbursementCalcDetails
						.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
						.getAmountAlreadyPaidAmt() : 0)
						: (reimbursementCalcDetails
								.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
								.getPayableInsuredAfterPremium() : 0);
				if (isBilling) {
					alreadyPaidAmt = reimbursementCalcDetails
							.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
							.getPayableInsuredAfterPremium() : 0;
				}
			}
			reimbursementDTO.setPreHospAmtAlreadyPaid(alreadyPaidAmt);
		}

		return dto;
	}

	private PostHopitalizationDetailsDTO getPostHospitalizationDTO(
			ReimbursementCalCulationDetails reimbursementCalcDetails,
			PreauthDTO reimbursementDTO, Boolean isBilling) {

		PostHopitalizationDetailsDTO dto = new PostHopitalizationDetailsDTO();

		dto.setNetAmount(reimbursementCalcDetails.getEligibleAmount());
		dto.setEligibleAmt(reimbursementCalcDetails.getEligibleAmount());
		dto.setCopayAmt(reimbursementCalcDetails.getCopayAmount());
		dto.setPayableAmt(reimbursementCalcDetails.getNetPayableAmount());
		dto.setNetPayable(reimbursementCalcDetails
				.getNetEligiblePayableAmount());
		dto.setPayableToInsAmt(reimbursementCalcDetails.getPayableToInsured());
		dto.setClaimRestrictionAmt(reimbursementCalcDetails
				.getClaimRestrictionAmount());
		dto.setBalancePremiumAmt(reimbursementCalcDetails
				.getDeductedBalancePremium());
		dto.setPayableToInsuredAftPremiumAmt(reimbursementCalcDetails
				.getPayableInsuredAfterPremium());
		dto.setMaxPayable(reimbursementCalcDetails.getMaximumPayableAmount());
		dto.setAvaliableSumInsuredAftHosp(reimbursementCalcDetails
				.getPayableInsuredAfterPremium());
		dto.setAmountAlreadyPaid(reimbursementCalcDetails
				.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
				.getAmountAlreadyPaidAmt() : 0);
		dto.setBalanceToBePaid(reimbursementCalcDetails.getBalanceToBePaidAmt() != null ? reimbursementCalcDetails
				.getBalanceToBePaidAmt() : 0);

		OtherInsPostHospSettlementDetailsDTO otherInsDTO = new OtherInsPostHospSettlementDetailsDTO();
		otherInsDTO
				.setBalanceAmt(reimbursementCalcDetails.getTpaBalanceAmt() != null ? reimbursementCalcDetails
						.getTpaBalanceAmt() : 0);
		otherInsDTO.setNonMedicalAmt(reimbursementCalcDetails
				.getTpaNonMedicalAmt() != null ? reimbursementCalcDetails
				.getTpaNonMedicalAmt() : 0);

		otherInsDTO.setTotalClaimedAmt(reimbursementCalcDetails
				.getTpaClaimedAmt() != null ? reimbursementCalcDetails
				.getTpaClaimedAmt() : 0);
		otherInsDTO.setTpaSettledAmt(reimbursementCalcDetails
				.getTpaSettledAmt() != null ? reimbursementCalcDetails
				.getTpaSettledAmt() : 0);
		otherInsDTO.setPayableToIns(reimbursementCalcDetails
				.getTpaPayableToInsured() != null ? reimbursementCalcDetails
				.getTpaPayableToInsured() : 0);
		otherInsDTO
				.setPayableAmt(reimbursementCalcDetails.getTpaPayableAmt() != null ? reimbursementCalcDetails
						.getTpaPayableAmt() : 0);
		otherInsDTO
				.setHospPayableAmt(reimbursementCalcDetails
						.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
						.getPayableInsuredAfterPremium() : 0);
		Integer amt = otherInsDTO.getTotalClaimedAmt()
				- otherInsDTO.getNonMedicalAmt();
		otherInsDTO.setNetClaimedAmt(amt > 0 ? amt : 0);

		Integer alreadyPaidAmt = 0;
		if (reimbursementDTO.getIsReconsiderationRequest()) {
			if (reimbursementDTO.getPreauthMedicalDecisionDetails()
					.getOtherInsurerApplicableFlag() != null
					&& reimbursementDTO.getPreauthMedicalDecisionDetails()
							.getOtherInsurerApplicableFlag()
							.equalsIgnoreCase("Y")) {
				alreadyPaidAmt = reimbursementCalcDetails.getTpaPayableAmt() != null ? reimbursementCalcDetails
						.getTpaPayableAmt()
						: 0 - (reimbursementCalcDetails
								.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
								.getAmountAlreadyPaidAmt() : 0);
			} else {
				alreadyPaidAmt = (reimbursementCalcDetails
						.getAmountAlreadyPaidAmt() != null) ? (reimbursementCalcDetails
						.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
						.getAmountAlreadyPaidAmt() : 0)
						: (reimbursementCalcDetails
								.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
								.getPayableInsuredAfterPremium() : 0);
				if (isBilling) {
					alreadyPaidAmt = reimbursementCalcDetails
							.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
							.getPayableInsuredAfterPremium() : 0;
				}
			}
			reimbursementDTO.setPostHospAmtAlreadyPaid(alreadyPaidAmt);
		}
		reimbursementDTO.setOtherInsPostHospSettlementCalcDTO(otherInsDTO);

		return dto;
	}

	private HopitalizationCalulationDetailsDTO getHospitalizationCalculationDTO(
			ReimbursementCalCulationDetails reimbursementCalcDetails,
			PreauthDTO reimbursementDTO, Boolean isBilling) {

		HopitalizationCalulationDetailsDTO dto = new HopitalizationCalulationDetailsDTO();

		dto.setNetPayableAmt(reimbursementCalcDetails.getNetPayableAmount());
		dto.setClaimRestrictionAmt(reimbursementCalcDetails
				.getClaimRestrictionAmount());
		dto.setPreauthAppAmt(reimbursementCalcDetails
				.getCashlessApprovedAmount());
		dto.setPayableToHospitalAmt(reimbursementCalcDetails
				.getPayableToHospital());
		dto.setPayableToInsAmt(reimbursementCalcDetails.getPayableToInsured());
		dto.setHospitalDiscount(reimbursementCalcDetails.getHospitalDiscount());
		dto.setAfterHospitalDiscount(reimbursementCalcDetails
				.getHospitalDiscountAmtAft());
		dto.setTdsAmt(reimbursementCalcDetails.getTdsAmount());
		dto.setPayableToHospitalAftTDSAmt(reimbursementCalcDetails
				.getPayableToHospAftTDS());
		dto.setBalancePremiumAmt(reimbursementCalcDetails
				.getDeductedBalancePremium());
		dto.setPayableToInsuredAftPremiumAmt(reimbursementCalcDetails
				.getPayableInsuredAfterPremium());
		dto.setAmountAlreadyPaid(reimbursementCalcDetails
				.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
				.getAmountAlreadyPaidAmt() : 0);
		if (reimbursementDTO.getHospitalizaionFlag()) {
			dto.setBalanceToBePaid(((reimbursementCalcDetails
					.getPayableToHospAftTDS() != null ? reimbursementCalcDetails
					.getPayableToHospAftTDS() : 0)));
		} else if (reimbursementDTO.getPartialHospitalizaionFlag()
				|| reimbursementDTO.getIsHospitalizationRepeat()) {
			dto.setBalanceToBePaid(((reimbursementCalcDetails
					.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
					.getPayableInsuredAfterPremium() : 0)));
		}

		OtherInsHospSettlementDetailsDTO otherInsDTO = new OtherInsHospSettlementDetailsDTO();
		otherInsDTO
				.setBalanceAmt(reimbursementCalcDetails.getTpaBalanceAmt() != null ? reimbursementCalcDetails
						.getTpaBalanceAmt() : 0);
		if (reimbursementDTO.getHospitalizaionFlag()) {
			otherInsDTO
					.setHospPayableAmt(reimbursementCalcDetails
							.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
							.getPayableInsuredAfterPremium() : 0);
		} else if (reimbursementDTO.getPartialHospitalizaionFlag()
				|| reimbursementDTO.getIsHospitalizationRepeat()) {
			otherInsDTO
					.setHospPayableAmt(reimbursementCalcDetails
							.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
							.getPayableInsuredAfterPremium() : 0);
		}
		otherInsDTO.setNonMedicalAmt(reimbursementCalcDetails
				.getTpaNonMedicalAmt() != null ? reimbursementCalcDetails
				.getTpaNonMedicalAmt() : 0);
		otherInsDTO.setTotalClaimedAmt(reimbursementCalcDetails
				.getTpaClaimedAmt() != null ? reimbursementCalcDetails
				.getTpaClaimedAmt() : 0);
		otherInsDTO.setTpaSettledAmt(reimbursementCalcDetails
				.getTpaSettledAmt() != null ? reimbursementCalcDetails
				.getTpaSettledAmt() : 0);
		otherInsDTO.setPayableToIns(reimbursementCalcDetails
				.getTpaPayableToInsured() != null ? reimbursementCalcDetails
				.getTpaPayableToInsured() : 0);
		otherInsDTO
				.setPayableAmt(reimbursementCalcDetails.getTpaPayableAmt() != null ? reimbursementCalcDetails
						.getTpaPayableAmt() : 0);
		Integer amt = otherInsDTO.getTotalClaimedAmt()
				- otherInsDTO.getNonMedicalAmt();
		otherInsDTO.setNetClaimedAmt(amt > 0 ? amt : 0);
		reimbursementDTO.setOtherInsHospSettlementCalcDTO(otherInsDTO);
		Integer alreadyPaidAmt = 0;
		if (reimbursementDTO.getIsReconsiderationRequest()) {
			if (reimbursementDTO.getPreauthMedicalDecisionDetails()
					.getOtherInsurerApplicableFlag() != null
					&& reimbursementDTO.getPreauthMedicalDecisionDetails()
							.getOtherInsurerApplicableFlag()
							.equalsIgnoreCase("Y")) {
				alreadyPaidAmt = reimbursementCalcDetails.getTpaPayableAmt() != null ? reimbursementCalcDetails
						.getTpaPayableAmt()
						: 0 - (reimbursementCalcDetails
								.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
								.getAmountAlreadyPaidAmt() : 0);
			} else {
				if (reimbursementDTO.getPreauthDataExtractionDetails()
						.getDocAckknowledgement() != null
						&& reimbursementDTO.getPreauthDataExtractionDetails()
								.getDocAckknowledgement()
								.getDocumentReceivedFromId() != null
						&& reimbursementDTO.getPreauthDataExtractionDetails()
								.getDocAckknowledgement()
								.getDocumentReceivedFromId().getKey()
								.equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
					alreadyPaidAmt = (reimbursementCalcDetails
							.getAmountAlreadyPaidAmt() != null) ? (reimbursementCalcDetails
							.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
							.getAmountAlreadyPaidAmt() : 0)
							: (reimbursementCalcDetails
									.getPayableToHospAftTDS() != null ? reimbursementCalcDetails
									.getPayableToHospAftTDS() : 0);
						if(isBilling && !(reimbursementCalcDetails.getReimbursement().getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING))) {
						alreadyPaidAmt = reimbursementCalcDetails
								.getPayableToHospAftTDS();
					}
				} else {
					alreadyPaidAmt = (reimbursementCalcDetails
							.getAmountAlreadyPaidAmt() != null) ? (reimbursementCalcDetails
							.getAmountAlreadyPaidAmt() != null ? reimbursementCalcDetails
							.getAmountAlreadyPaidAmt() : 0)
							: (reimbursementCalcDetails
									.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
									.getPayableInsuredAfterPremium() : 0);
							//IMSSUPPOR- 26185  --> below code was commented
//					if (isBilling && !(reimbursementCalcDetails.getReimbursement().getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING))) {
//						alreadyPaidAmt = reimbursementCalcDetails
//								.getPayableInsuredAfterPremium() != null ? reimbursementCalcDetails
//								.getPayableInsuredAfterPremium() : 0;
//					}
				}
			}
			reimbursementDTO.setHospAmountAlreadyPaid(alreadyPaidAmt);
		}

		return dto;

	}

	protected Reimbursement getHospitalizationRODWithSettled(Long claimKey) {
		Reimbursement previousROD = null;
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()
				&& previousRODByClaimKey.size() > 1) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if (reimbursement.getDocAcknowLedgement()
						.getHospitalisationFlag() != null
						&& reimbursement.getDocAcknowLedgement()
								.getHospitalisationFlag().toLowerCase()
								.equalsIgnoreCase("y")
						&& ReferenceTable
								.getFinancialApprovalStatus()
								.containsKey(reimbursement.getStatus().getKey())) {
					previousROD = reimbursement;
					break;
				}
			}
		}

		return previousROD;
	}

	protected Reimbursement getPartialHospitalizationRODWithSettled(
			Long claimKey) {
		Reimbursement previousROD = null;
		List<Reimbursement> previousRODByClaimKey = reimbursementService
				.getPreviousRODByClaimKey(claimKey);
		if (previousRODByClaimKey != null && !previousRODByClaimKey.isEmpty()
				&& previousRODByClaimKey.size() > 1) {
			for (Reimbursement reimbursement : previousRODByClaimKey) {
				if (reimbursement.getDocAcknowLedgement()
						.getPartialHospitalisationFlag() != null
						&& reimbursement.getDocAcknowLedgement()
								.getPartialHospitalisationFlag().toLowerCase()
								.equalsIgnoreCase("y")
						&& ReferenceTable
								.getFinancialApprovalStatus()
								.containsKey(reimbursement.getStatus().getKey())) {
					previousROD = reimbursement;
					break;
				}
			}
		}

		return previousROD;
	}
	
	
	protected void showPreAuthWizard(
			@Observes @CDIEvent(PAMenuPresenter.SHOW_PREATUH_WIZARD) final ParameterDTO parameters) {
		Date startDate = new Date();
		log.info("%%%%%%%%%%%%%%%%%%% MENUPRESENTER-----> SHOW PREAUTH WIZARD METHOD STARTED %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(startDate, new Date()));

		Date date1 = new Date();
		PASearchPreauthTableDTO dto = (PASearchPreauthTableDTO) parameters
				.getPrimaryParameter();
		PreMedicalMapper premedicalMapper = PreMedicalMapper.getInstance();
		// PreMedicalMapper.getAllMapValues();
		Preauth preauth = preauthService.getPreauthById(dto.getKey());
		Long preauthKey = preauth.getKey();
		PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
		log.info("%%%%%%%%%%%%%%%%%%% MENUPRESENTER-----> TOTAL TIME CONSUMED BY PREAUTH DTO AND MAPPER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date1, new Date()));

		// preauthDTO.setAmountRequested(dto.getPreAuthReqAmt());

	//	preauthDTO.setRodHumanTask(dto.getHumanTask());
		preauthDTO.setTaskNumber(dto.getTaskNumber());

		preauthDTO.setStrUserName(dto.getUsername());
		preauthDTO.setStrPassword(dto.getPassword());

		preauthDTO.setSfxMatchedQDate(dto.getDocReceivedTimeForMatch());
		preauthDTO.setSfxRegisteredQDate(dto.getDocReceivedTimeForReg());

		preauthDTO.setStatusKey(preauth.getStatus().getKey());
		/*
		 * <<<<<<< HEAD
		 * 
		 * if (preauth .getStatus() .getKey()
		 * .equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS
		 * )) {
		 * preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
		 * preauth.getRemarks()); //
		 * preauthDTO.getPreauthMedicalProcessingDetails
		 * ().setRejectionRemarks(preauth.getRemarks()); } // else if
		 * (preauthDTO.getStatusKey() == //
		 * ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS) {
		 * else if (preauth .getStatus() .getKey()
		 * .equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS
		 * )) {
		 * preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
		 * preauth.getRemarks()); } else if (preauth.getStatus().getKey()
		 * .equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) ||
		 * preauth.getStatus().getKey()
		 * .equals(ReferenceTable.PREAUTH_QUERY_STATUS) ||
		 * preauth.getStatus().getKey()
		 * .equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS)) {
		 * preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
		 * preauth.getRemarks()); } else if (preauth.getStatus().getKey()
		 * .equals(ReferenceTable.PREAUTH_ESCALATION_STATUS)) {
		 * preauthDTO.getPreauthMedicalDecisionDetails().setEscalationRemarks(
		 * preauth.getRemarks()); =======
		 */
		
		Claim claimByKey = claimService.getClaimByKey(preauth.getClaim()
				.getKey());

		
		
		
		
		Boolean isValidClaim = true;
		if (null != claimByKey) {
			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaim = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
		}
		
		if(isValidClaim) {

			if(preauth.getCatastrophicLoss() != null) {
				
				SelectValue value = masterService.getCatastropheData(preauth.getCatastrophicLoss());
				preauthDTO.getPreauthDataExtractionDetails().setCatastrophicLoss(value);
			}

			if(preauth.getNatureOfLoss() != null) {
				
				SelectValue value = masterService.getMasterValueForNatureCause(preauth.getNatureOfLoss());
				preauthDTO.getPreauthDataExtractionDetails().setNatureOfLoss(value);
			}
			if(preauth.getCauseOfLoss() != null) {
				SelectValue value = masterService.getMasterValueForNatureCause(preauth.getCauseOfLoss());
				preauthDTO.getPreauthDataExtractionDetails().setCauseOfLoss(value);
			}
		if (preauth
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)) {
			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					preauth.getRemarks());
			// preauthDTO.getPreauthMedicalProcessingDetails().setRejectionRemarks(preauth.getRemarks());
		} // else if (preauthDTO.getStatusKey() ==
			// ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS) {
		else if (preauth
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS)) {
			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					preauth.getRemarks());
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)
				|| preauth.getStatus().getKey()
						.equals(ReferenceTable.PREAUTH_QUERY_STATUS)
				|| preauth.getStatus().getKey()
						.equals(ReferenceTable.PREAUTH_QUERY_RECEIVED_STATUS)) {
			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					preauth.getRemarks());
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.PREAUTH_ESCALATION_STATUS)) {
			preauthDTO.getPreauthMedicalDecisionDetails().setEscalationRemarks(
					preauth.getRemarks());

			// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
		} else if (preauth
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS)
				|| preauth
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY)
				|| preauth
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY)
				|| preauth
						.getStatus()
						.getKey()
						.equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS)
				|| preauth
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PREAUTH_REFER_TO_COORDINATOR_STATUS)) {
			Coordinator coordinatorByPreauthKey = coordinatorService
					.getCoordinatorByPreauthKey(preauth.getClaim().getKey());
			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					coordinatorByPreauthKey.getCoordinatorRemarks());
			preauthDTO.getCoordinatorDetails().setCoordinatorRemarks(
					coordinatorByPreauthKey.getCoordinatorRemarks());
		}

		
		
		Policy policy = claimByKey.getIntimation().getPolicy();

		List<Claim> claimList = claimService.getClaimsByPolicyNumber(policy
				.getPolicyNumber());
		for (Claim claim : claimList) {
			Boolean isPedPending = false;
			List<OldInitiatePedEndorsement> pedInitiateDetail = pedQueryService
					.getPedInitiateDetail(claim.getKey());
			for (OldInitiatePedEndorsement oldInitiatePedEndorsement : pedInitiateDetail) {
				if (oldInitiatePedEndorsement.getStatus().getKey()
						.equals(ReferenceTable.PED_PENDING_ENDORSEMENT_STATUS)) {
					preauthDTO.setIsPedPending(true);
					isPedPending = true;
				}
			}

			if (isPedPending) {
				break;
			}
		}

		Date date3 = new Date();
		setpreauthTOPreauthDTO(premedicalMapper, claimByKey, preauth,
				preauthDTO, true);
		log.info("%%%%%%%%%%%%%%%%%%% MENUPRESENTER-----> TOTAL TIME CONSUMED BY SET PREAUTH TO PREAUTH DTO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date3, new Date()));

		preauthDTO.setCopay(getCopay(preauthDTO.getNewIntimationDTO()
				.getInsuredAge(), preauth.getPolicy().getProduct().getKey()));

		String reasonForAdmission = preauthDTO.getNewIntimationDTO()
				.getReasonForAdmission();

		if (reasonForAdmission != null) {
			// reasonForAdmission = SHAUtils.removeLastChar(reasonForAdmission);
			// preauthDTO.getNewIntimationDTO().setReasonForAdmission(
			// reasonForAdmission);
			// preauthDTO.getPreauthDataExtractionDetails().setReasonForAdmission(
			// reasonForAdmission);
			// preauthDTO.setReasonForAdmission(reasonForAdmission);
		}
		
		Double insuredSumInsured = 0d;
		if(null != claimByKey &&  null != claimByKey.getIntimation() && 
		null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
		null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
		&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
		{

		 insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			 insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						preauthDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), preauthDTO.getPolicyDto()
								.getKey());
		}
		Date date4 = new Date();
		
		String branchOfficeCode = claimByKey.getIntimation().getPolicy().getHomeOfficeCode();
	/*	OrganaizationUnit organization = createRodService.getBranchCode(branchOfficeCode);
		if(null != organization)
		{
			preauthDTO.getPreauthDataExtractionDetails().setOrganisationName(organization.getOrganizationUnitName());
		}*/
		if(null != insuredSumInsured)
		{
		preauthDTO.getPreauthDataExtractionDetails().setPaSumInsured(insuredSumInsured);
		}
		preauthDTO.getPreauthDataExtractionDetails().setOrganisationName(claimByKey.getIntimation().getPolicy().getProposerFirstName());
		preauthDTO.getPreauthDataExtractionDetails().setDateOfBirth(claimByKey.getGpaParentDOB());			
		preauthDTO.getPreauthDataExtractionDetails().setAge(claimByKey.getGpaParentAge());
		preauthDTO.getPreauthDataExtractionDetails().setParentName(claimByKey.getGpaParentName());
		preauthDTO.getPreauthDataExtractionDetails().setRiskName(claimByKey.getGpaRiskName());
		preauthDTO.getPreauthDataExtractionDetails().setGpaRiskDOB(claimByKey.getGpaRiskDOB());
		preauthDTO.getPreauthDataExtractionDetails().setGpaRiskAge(claimByKey.getGpaRiskAge());
		preauthDTO.getPreauthDataExtractionDetails().setGpaSection(claimByKey.getGpaSection());
		//preauthDTO.getPreauthDataExtractionDetails().setGpaCategory(claimByKey.getGpaCategory());
		
		if(null == insuredSumInsured)
		{
		preauthDTO.getPreauthDataExtractionDetails().setPaSumInsured(0d);
		}
		if(null == claimByKey.getGpaParentAge())
		{
		preauthDTO.getPreauthDataExtractionDetails().setAge(0d);
		}

		if(null == claimByKey.getGpaRiskAge())
		{
		preauthDTO.getPreauthDataExtractionDetails().setGpaRiskAge(0d);
		}
		
		loadRRCRequestValues(preauthDTO, insuredSumInsured,
				SHAConstants.PROCESS_PREAUTH);

		log.info("%%%%%%%%%%%%%%%%%%% MENUPRESENTER-----> TOTAL TIME CONSUMED BY LOAD RRC REQUEST VALUES %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(date4, new Date()));

		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if (strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			Date date5 = new Date();
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(
					preauthDTO.getNewIntimationDTO().getPolicy()
							.getPolicyNumber(), preauthDTO.getNewIntimationDTO().getIntimationId());
			log.info("%%%%%%%%%%%%%%%%%%% MENUPRESENTER-----> TOTAL TIME CONSUMED BY GET 64VSTATUS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
					+ SHAUtils.getDurationFromTwoDate(date5, new Date()));
			if (get64vbStatus != null
					&& SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				preauthDTO.setIsDishonoured(true);
			} else if (get64vbStatus != null
					&& (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
				preauthDTO.setIsPending(true);
			}
		}

		/*
		 * maternity flag from db
		 */
		String maternityFlag = dbCalculationService
				.getMaternityFlagForProduct(preauth.getIntimation().getPolicy()
						.getProduct().getKey());
		if (maternityFlag != null && maternityFlag.equalsIgnoreCase("Y")) {
			preauthDTO.setMaternityFlag(true);
		}

		preauthDTO.setSittingsAmount(dbCalculationService.getDialysisAmount(
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey(), insuredSumInsured.longValue()).intValue());

		Map<String, String> popupMessages = dbCalculationService
				.getPOPUPMessages(preauthDTO.getPolicyKey(), preauthDTO
						.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
		preauthDTO.setPopupMap(popupMessages);

		if (preauthDTO.getNewIntimationDTO().getHospitalDto() != null
				&& preauthDTO.getNewIntimationDTO().getHospitalDto()
						.getSuspiciousFlag() != null
				&& preauthDTO.getNewIntimationDTO().getHospitalDto()
						.getSuspiciousFlag().equalsIgnoreCase("Y")) {
			if (preauthDTO.getNewIntimationDTO().getHospitalDto()
					.getSuspiciousRemarks() != null) {
				Map<String, String> suspiciousMap = SHAUtils
						.getSuspiciousMap(preauthDTO.getNewIntimationDTO()
								.getHospitalDto().getSuspiciousRemarks());
				preauthDTO.setSuspiciousPopupMap(suspiciousMap);
			}
		}

		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA6(
				dto.getCMA6());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA5(
				dto.getCMA5());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA4(
				dto.getCMA4());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA3(
				dto.getCMA3());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA2(
				dto.getCMA2());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA1(
				dto.getCMA1());

		// preauthDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(preauthDTO.getNewIntimationDTO().getAdmissionDate(),
		// preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));

		String policyYear = preauthDTO.getNewIntimationDTO().getPolicyYear();
		if (policyYear != null) {
			String intValue = policyYear.replaceAll("[^0-9]", "");
			Integer policyAgeing = SHAUtils
					.getIntegerFromStringWithComma(intValue);
			preauthDTO
					.setAdmissionDatePopup(policyAgeing != null ? policyAgeing
							.equals(0) ? true : false : false);
		}

		preauthDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(preauthDTO
				.getNewIntimationDTO().getPolicy().getKey()));
		preauthDTO.setIsPEDInitiatedForBtn(preauthDTO.getIsPEDInitiated());

		if (preauthDTO.getNewIntimationDTO() != null
				&& preauthDTO.getNewIntimationDTO().getPolicy() != null
				&& preauthDTO.getNewIntimationDTO().getPolicy().getKey() != null) {
			Long claimCount = preauthService.getClaimCount(preauthDTO
					.getNewIntimationDTO().getPolicy().getKey());
			if (claimCount != null) {
				preauthDTO.setClaimCount(claimCount);
			}
		}

		//SHAUtils.setDefaultCopayValue(preauthDTO);
		log.info("%%%%%%%%%%%%%%%%%%% MENUPRESENTER-----> TOTAL TIME CONSUMED BY SHOW PREAUTH WIZARD %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------> "
				+ SHAUtils.getDurationFromTwoDate(startDate, new Date()));
		
		StarCommonUtils.getIncidentFlagAndDate(claimByKey, preauthDTO);
		preauthDTO.setDbOutArray(dto.getDbOutArray());
		
		if(null != preauth && null != preauth.getWorkPlace() && (SHAConstants.YES_FLAG.equalsIgnoreCase(preauth.getWorkPlace()))){
			preauthDTO.getPreauthDataExtractionDetails().setWorkOrNonWorkPlace(true);
		}
		
		if(null != preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode()){
			
			MasterGST masGstObj = masterService.getGSTByStateId(preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode());

			if(null != masGstObj){
	
				preauthDTO.setGstNumber(masGstObj.getGstNumber());
				preauthDTO.setArnNumber(masGstObj.getArnNumber());
				preauthDTO.setGstState(masGstObj.getStateName());
			}
		}
		
		//R1257
		preauthDTO.getPreauthDataExtractionDetails().setNotAdheringToANHReportFlag(preauth.getNotAdheringToANHReport());
		
		 //CR2019234 
		if(preauth.getIntimation().getIntimatedBy().getKey() != null && 
				preauth.getIntimation().getIntimatedBy().getKey().toString().equalsIgnoreCase(SHAConstants.AGENT_CODE) 
				|| preauth.getIntimation().getIntimatedBy().getKey().toString().equalsIgnoreCase(SHAConstants.SALES_MANAGER_CODE)){
			view.showInfoPopUp("This claim has been intimated by" + preauth.getIntimation().getIntimatedBy().getValue());
		}
		
//		Portal Flag updated in cashless table
		if(dto.getNhpUpdDocumentKey() != null){
			preauthDTO.setNhpUpdKey(dto.getNhpUpdDocumentKey());
		}
		
		view.setPreauthWizardViewForPA(PAPreauthWizard.class, preauthDTO,
				(Boolean) parameters.getSecondaryParameter(0, Boolean.class));
	}	
	}
	
	protected void showPreMedicalWizard(
			@Observes @CDIEvent(PAMenuPresenter.SHOW_PREMEDICAL_WIZARD) final ParameterDTO parameters) {

		PAProcessPreMedicalTableDTO tableDTO = (PAProcessPreMedicalTableDTO) parameters
				.getPrimaryParameter();

		Date startDate = new Date();
		log.info("%%%%%%%%%%%%%%%%%%%%%% STARTING TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
				+ startDate);

		NewIntimationDto newIntimationDto = new NewIntimationDto();
		PreauthDTO preauthDTO = new PreauthDTO();
		//preauthDTO.setRodHumanTask(tableDTO.getHumanTask());
		preauthDTO.setTaskNumber(tableDTO.getTaskNumber());

		preauthDTO.setStrUserName(tableDTO.getUsername());
		preauthDTO.setStrPassword(tableDTO.getPassword());
		if (null != tableDTO.getDocReceivedTimeForMatchDate())
			preauthDTO.setSfxMatchedQDate(tableDTO
					.getDocReceivedTimeForMatchDate());
		/*
		 * if(null !=tableDTO.getDocReceivedTimeForReg())
		 * preauthDTO.setSfxRegisteredQDate
		 * (SHAUtils.formatDateForStarfax(tableDTO.getDocReceivedTimeForReg()));
		 */
		if (null != tableDTO.getDocReceivedTimeForRegDate())
			preauthDTO.setSfxRegisteredQDate(tableDTO
					.getDocReceivedTimeForRegDate());
		/*
		 * if(null != tableDTO.getDocReceivedTimeForMatch() )
		 * preauthDTO.setSfxMatchedQDate
		 * (SHAUtils.formatDateForStarfax(tableDTO.getDocReceivedTimeForMatch
		 * ()));
		 */

		Claim claimByKey = claimService.getClaimByKey(tableDTO.getKey());
		Boolean isValidClaim = true;
		if (null != claimByKey) {
			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaim = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
		}

		if (isValidClaim) {

			List<Preauth> preauthByClaimKey = preauthService
					.getPreauthByClaimKey(claimByKey.getKey());
			Boolean isQueryOrTranslate = false;
			if (!preauthByClaimKey.isEmpty()) {
				for (Preauth preauth : preauthByClaimKey) {

					String[] split = preauth.getPreauthId().split("/");
					Integer seqNumber = Integer
							.valueOf(split[split.length - 1]);
					if (preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)) {
						PreMedicalMapper premedicalMapper = PreMedicalMapper
								.getInstance();
						// PreMedicalMapper.getAllMapValues();
						preauthDTO = premedicalMapper.getPreauthDTO(preauth);
					//	preauthDTO.setRodHumanTask(tableDTO.getHumanTask());
						setpreauthTOPreauthDTO(premedicalMapper, claimByKey,
								preauth, preauthDTO, true);
						CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
						coordinatorDTO.setRefertoCoordinator(false);
						preauthDTO.setCoordinatorDetails(coordinatorDTO);
						isQueryOrTranslate = true;
						break;
					}
				}
			}

			if (!isQueryOrTranslate) {
				List<Preauth> preauthByClaimKeyWithClearCashless = preauthService
						.getPreauthByClaimKeyWithClearCashless(claimByKey
								.getKey());
				String referenceNo = claimByKey.getClaimId() + "/001";
				if (!preauthByClaimKeyWithClearCashless.isEmpty()) {
					referenceNo = claimByKey.getClaimId()
							+ "/00"
							+ String.valueOf(preauthByClaimKeyWithClearCashless
									.size() + 1);
				}

				preauthDTO.getPreauthDataExtractionDetails().setReferenceNo(
						referenceNo);
				
				
				if (claimByKey != null) {
					setClaimValuesToDTO(preauthDTO, claimByKey);
					newIntimationDto = intimationService
							.getIntimationDto(claimByKey.getIntimation());
					// newIntimationDto.getPolicy().getProduct().getAutoRestoration()
					ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(
							claimByKey);
					preauthDTO.setNewIntimationDTO(newIntimationDto);
					preauthDTO.setClaimDTO(claimDTO);
				}
				String policyNumber = preauthDTO.getPolicyDto()
						.getPolicyNumber();
			/*	List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
				// List<ViewTmpClaim> claimsByPolicyNumber = claimService
				// .getViewTmpClaimsByPolicyNumber(policyNumber);
				Policy byPolicyNumber = policyService
						.getByPolicyNumber(policyNumber);
				List<ViewTmpIntimation> intimationKeys = intimationService
						.getIntimationByPolicyKey(byPolicyNumber.getKey());
				List<ViewTmpClaim> claimsByPolicyNumber = claimService
						.getViewTmpClaimsByIntimationKeys(intimationKeys);
				previousclaimsList.addAll(claimsByPolicyNumber);

				previousclaimsList = getPreviousClaimForPreviousPolicy(
						byPolicyNumber.getRenewalPolicyNumber(),
						previousclaimsList);*/

				// List<PreviousClaimsTableDTO> previousClaimDTOList = SHAUtils
				// .getPreviousClaims(claimsByPolicyNumber,
				// claimByKey.getClaimId(), pedValidationService,
				// masterService);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				List<PreviousClaimsTableDTO> previousClaimDTOList = dbCalculationService.getPreviousClaims(claimByKey.getKey(), claimByKey.getIntimation().getPolicy().getKey(), 
						claimByKey.getIntimation().getInsured().getKey(), SHAConstants.POLICY_WISE_SEARCH_TYPE);

			/*	List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
						.getPreviousClaims(previousclaimsList,
								claimByKey.getClaimId());*/

				// List<PreviousClaimsTableDTO> previousClaimDTOList = new
				// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

				// List<PreviousClaimsTableDTO> previousClaimDTOList = new
				// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

				preauthDTO.setPreviousClaimsList(previousClaimDTOList);

				//DBCalculationService dbCalculationService = new DBCalculationService();
				
				
				Double insuredSumInsured = 0d;
				if(null != claimByKey &&  null != claimByKey.getIntimation() && 
				null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
				null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
				&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
				{
					insuredSumInsured = dbCalculationService
						.getInsuredSumInsured(preauthDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO()
								.getInsuredPatient().getLopFlag());
				}
				else
				{
					insuredSumInsured = dbCalculationService
							.getGPAInsuredSumInsured(preauthDTO.getNewIntimationDTO()
									.getInsuredPatient().getInsuredId().toString(),
									preauthDTO.getPolicyDto().getKey());
				}
				Integer sumInsured = preauthService.getSumInsured(preauthDTO
						.getPolicyDto().getProduct().getKey(),
						(insuredSumInsured == 0) ? preauthDTO.getPolicyDto()
								.getTotalSumInsured() : insuredSumInsured);
				preauthDTO.getPolicyDto().setInsuredSumInsured(
						insuredSumInsured);
				/*String autoRestroation = dbCalculationService
						.getAutoRestroation(preauthDTO.getNewIntimationDTO()
								.getIntimationId());
				preauthDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(autoRestroation);
				if (autoRestroation
						.equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
					preauthDTO.setIsAutoRestorationDone(true);
				}*/
			}

			preauthDTO.setIsRepremedical(isQueryOrTranslate);
			
			Double insuredSumInsured = 0d;
			
			if(null != claimByKey &&  null != claimByKey.getIntimation() && 
					null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
					null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
					&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
					{
				insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
					}
			else
			{
				insuredSumInsured = dbCalculationService
						.getGPAInsuredSumInsured(preauthDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								preauthDTO.getPolicyDto().getKey());
			}

			preauthDTO.setStrUserName(tableDTO.getUsername());
			preauthDTO.setStrPassword(tableDTO.getPassword());
			
		String branchOfficeCode = claimByKey.getIntimation().getPolicy().getHomeOfficeCode();
			/*OrganaizationUnit organization = createRodService.getBranchCode(branchOfficeCode);
			if(null != organization)
			{
				preauthDTO.getPreauthDataExtractionDetails().setOrganisationName(organization.getOrganizationUnitName());
			}*/
			if(null != insuredSumInsured)
			{
			preauthDTO.getPreauthDataExtractionDetails().setPaSumInsured(insuredSumInsured);
			}
			
			preauthDTO.getPreauthDataExtractionDetails().setOrganisationName(claimByKey.getIntimation().getPolicy().getProposerFirstName());
			preauthDTO.getPreauthDataExtractionDetails().setDateOfBirth(claimByKey.getGpaParentDOB());			
			preauthDTO.getPreauthDataExtractionDetails().setAge(claimByKey.getGpaParentAge());
			preauthDTO.getPreauthDataExtractionDetails().setParentName(claimByKey.getGpaParentName());
			preauthDTO.getPreauthDataExtractionDetails().setRiskName(claimByKey.getGpaRiskName());
			preauthDTO.getPreauthDataExtractionDetails().setGpaRiskDOB(claimByKey.getGpaRiskDOB());
			preauthDTO.getPreauthDataExtractionDetails().setGpaRiskAge(claimByKey.getGpaRiskAge());
			preauthDTO.getPreauthDataExtractionDetails().setGpaSection(claimByKey.getGpaSection());
		//	preauthDTO.getPreauthDataExtractionDetails().setGpaCategory(claimByKey.getGpaCategory());
			
			if(null == insuredSumInsured)
			{
			preauthDTO.getPreauthDataExtractionDetails().setPaSumInsured(0d);
			}
			if(null == claimByKey.getGpaParentAge())
			{
			preauthDTO.getPreauthDataExtractionDetails().setAge(0d);
			}

			if(null == claimByKey.getGpaRiskAge())
			{
			preauthDTO.getPreauthDataExtractionDetails().setGpaRiskAge(0d);
			}
			loadRRCRequestValues(preauthDTO, insuredSumInsured,
					SHAConstants.PROCESS_PRE_MEDICAL);

			Date premiaStartDate = new Date();
			log.info("%%%%%%%%%%%%%%%%%%%%%% STARTING TIME FOR 64VB PREMIA CALL %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
					+ premiaStartDate);

			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if (strPremiaFlag != null
					&& ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance()
						.get64VBStatus(
								preauthDTO.getNewIntimationDTO().getPolicy()
										.getPolicyNumber(), preauthDTO.getNewIntimationDTO().getIntimationId());
				if (get64vbStatus != null
						&& SHAConstants.DISHONOURED
								.equalsIgnoreCase(get64vbStatus)) {
					preauthDTO.setIsDishonoured(true);
				} else if (get64vbStatus != null
						&& (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
					preauthDTO.setIsPending(true);
				}
			}

			Date premiaEndDate = new Date();
			log.info("%%%%%%%%%%%%%%%%%%%%%% ENDING TIME FOR 64VB PREMIA CALL %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
					+ premiaEndDate);

			log.info("%%%%%%%%%%%%%%%%%%%%%% TOTAL TIME FOR 64VB PREMIA CALL %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
					+ SHAUtils.getDurationFromTwoDate(premiaStartDate,
							premiaEndDate));

			Date dbCalStartDate = new Date();
			log.info("%%%%%%%%%%%%%%%%%%%%%%STARTING TIME FOR  DB CALCULATION SERVICE  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
					+ dbCalStartDate);

			preauthDTO.setSittingsAmount(dbCalculationService
					.getDialysisAmount(
							preauthDTO.getNewIntimationDTO().getPolicy()
									.getProduct().getKey(),
							insuredSumInsured.longValue()).intValue());
			// preauthDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(preauthDTO.getNewIntimationDTO().getAdmissionDate(),
			// preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));

			String policyYear = preauthDTO.getNewIntimationDTO()
					.getPolicyYear();
			if (policyYear != null) {
				String intValue = policyYear.replaceAll("[^0-9]", "");
				Integer policyAgeing = SHAUtils
						.getIntegerFromStringWithComma(intValue);
				preauthDTO
						.setAdmissionDatePopup(policyAgeing != null ? policyAgeing
								.equals(0) ? true : false : false);
			}

			preauthDTO.setIsPEDInitiated(pedQueryService
					.isPEDInitiated(preauthDTO.getNewIntimationDTO()
							.getPolicy().getKey()));
			preauthDTO.setIsPEDInitiatedForBtn(preauthDTO.getIsPEDInitiated());

			Map<String, String> popupMessages = dbCalculationService
					.getPOPUPMessages(preauthDTO.getPolicyKey(), preauthDTO
							.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
			preauthDTO.setPopupMap(popupMessages);

			// if(preauthDTO.getNewIntimationDTO() != null &&
			// preauthDTO.getNewIntimationDTO().getInsuredPatient() != null &&
			// preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey() !=
			// null){
			// Long claimCount =
			// preauthService.getClaimCount(preauthDTO.getNewIntimationDTO().getPolicy().getKey());
			// if(claimCount != null){
			// preauthDTO.setClaimCount(claimCount);
			// }
			// }

			Date dbCalEndate = new Date();
			log.info("%%%%%%%%%%%%%%%%%%%%%%STARTING TIME FOR  DB CALCULATION SERVICE  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
					+ dbCalEndate);

			log.info("%%%%%%%%%%%%%%%%%%%%%% TOTAL TIME FOR 64VB PREMIA CALL %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
					+ SHAUtils.getDurationFromTwoDate(dbCalStartDate,
							dbCalEndate));

			//SHAUtils.setDefaultCopayValue(preauthDTO);
			
			StarCommonUtils.getIncidentFlagAndDate(claimByKey, preauthDTO);
			preauthDTO.setDbOutArray(tableDTO.getDbOutArray());
			
//			Portal Flag updated in cashless table
			if(tableDTO.getNhpUpdDocumentKey() != null){
				preauthDTO.setNhpUpdKey(tableDTO.getNhpUpdDocumentKey());
			}
			
			view.setPreMedicalWizardViewForPA(PAPreMedicalPreauthWizard.class,
					preauthDTO, (Boolean) parameters.getSecondaryParameter(0,
							Boolean.class));

			Date endDate = new Date();
			log.info("%%%%%%%%%%%%%%%%%%%%%% ENDING TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
					+ endDate);

			log.info("%%%%%%%%%%%%%%%%%%%%%% TOTAL TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
					+ SHAUtils.getDurationFromTwoDate(startDate, endDate));

		}
	}
	
	protected void showPreMedicalEnhancementWizard(
			@Observes @CDIEvent(PAMenuPresenter.SHOW_PREMEDICAL_ENHANCEMENT_WIZARD) final ParameterDTO parameters) {

		Date startDate = new Date();
		log.info("%%%%%%%%%%%%%%%%%%%%%% PREMEDICAL_ENHANCEMENT STARTING TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
				+ startDate);

		PASearchPreMedicalProcessingEnhancementTableDTO dto = (PASearchPreMedicalProcessingEnhancementTableDTO) parameters
				.getPrimaryParameter();
		PreMedicalMapper premedicalMapper = PreMedicalMapper.getInstance();
		// PreMedicalMapper.getAllMapValues();	
		Claim claimByKey = claimService.getClaimByKey(dto.getKey());

		Boolean isValidClaim = true;
		if (null != claimByKey) {
			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaim = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
		}
		if (isValidClaim) {

			PreauthDTO preauthDTO = new PreauthDTO();
			List<Preauth> preauthByClaimKey = preauthService
					.getPreauthByClaimKey(claimByKey.getKey());

			if (null != dto.getDocsReceivedDate())
				preauthDTO.setSfxMatchedQDate(dto.getDocsReceivedDate());
			// preauthDTO.setSfxRegisteredQDate(dto.getDocReceivedTimeForReg());

			Boolean isQueryOrTranslate = false;
			if (!preauthByClaimKey.isEmpty()) {
				for (Preauth preauth : preauthByClaimKey) {
					String[] split = preauth.getPreauthId().split("/");
					Integer seqNumber = Integer
							.valueOf(split[split.length - 1]);
					// if (seqNumber == 2) {
					if (preauth
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.ENHANCEMENT_REFER_TO_FLP_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)
							|| preauth
                                     .getStatus()
                                     .getKey()
                                     .equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_RECEIVED_STATUS)
							|| preauth
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS)) {
						preauthDTO = premedicalMapper.getPreauthDTO(preauth);
						// preauthDTO.setHumanTask(dto.getHumanTask());
					//	preauthDTO.setRodHumanTask(dto.getHumanTask());
						setpreauthTOPreauthDTO(premedicalMapper, claimByKey,
								preauth, preauthDTO, false);
						preauthDTO.getPreauthDataExtractionDetails()
								.setAdmissionDate(preauth.getDataOfAdmission());
						CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
						coordinatorDTO.setRefertoCoordinator(false);
						preauthDTO.setCoordinatorDetails(coordinatorDTO);
						preauthDTO.getPreauthDataExtractionDetails()
						.setPreauthTotalApprAmt(preauth.getTotalApprovalAmount());
						isQueryOrTranslate = true;
						break;
					}
					// }
					
					
				}
			}

			if (!isQueryOrTranslate) {
				Integer nextReferenceNo = 2;
				Preauth previousPreauth = null;
				if (!preauthByClaimKey.isEmpty()) {
					previousPreauth = preauthByClaimKey.get(0);

					String[] split = previousPreauth.getPreauthId().split("/");
					String defaultNumber = split[split.length - 1];
					nextReferenceNo = Integer.valueOf(defaultNumber);
					for (Preauth preauth : preauthByClaimKey) {
						if (preauth.getPreauthId() != null) {
							String[] splitNumber = preauth.getPreauthId()
									.split("/");
							String number = splitNumber[splitNumber.length - 1];

							if (Integer.valueOf(number) > Integer
									.valueOf(defaultNumber)) {
								previousPreauth = preauth;
								nextReferenceNo = Integer.valueOf(number);
								preauthDTO.setIsPreMedicalForCoordinator(true);
							}
						}

					}
				}

				if (previousPreauth != null) {
					preauthDTO = premedicalMapper
							.getPreauthDTO(previousPreauth);
					getResidualAmount(previousPreauth, preauthDTO);
					List<Preauth> preauthByClaimKeyWithClearCashless = preauthService
							.getPreauthByClaimKeyWithClearCashless(claimByKey
									.getKey());
					String referenceNo = claimByKey.getClaimId() + "/00"
							+ String.valueOf(nextReferenceNo + 1);
					if (!preauthByClaimKeyWithClearCashless.isEmpty()) {
						referenceNo = claimByKey.getClaimId()
								+ "/00"
								+ String.valueOf(preauthByClaimKeyWithClearCashless
										.size() + 1);
					}
					preauthDTO.getPreauthDataExtractionDetails()
							.setReferenceNo(referenceNo);

					// preauthDTO.setHumanTask(dto.getHumanTask());
				//	preauthDTO.setRodHumanTask(dto.getHumanTask());

					// if(previousPreauth.getStatus().getKey() ==
					// ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)
					// {
					// preauthDTO.getPreauthMedicalProcessingDetails().setRejectionRemarks(previousPreauth.getRemarks());
					// } else if (preauthDTO.getStatusKey() ==
					// ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS)
					// {
					// preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(previousPreauth.getRemarks());
					// }

					setpreauthTOPreauthDTO(premedicalMapper, claimByKey,
							previousPreauth, preauthDTO, false);
					
					if(null != previousPreauth && null != previousPreauth.getWorkPlace() && 
							(SHAConstants.YES_FLAG.equalsIgnoreCase(previousPreauth.getWorkPlace()))){
						preauthDTO.getPreauthDataExtractionDetails().setWorkOrNonWorkPlace(true);
					}
					
					preauthDTO.getPreauthDataExtractionDetails()
							.setAdmissionDate(
									previousPreauth.getDataOfAdmission());
					preauthDTO.getPreauthDataExtractionDetails()
								.setPreauthTotalApprAmt(previousPreauth.getTotalApprovalAmount());
				}
			}
			preauthDTO.setIsRepremedical(isQueryOrTranslate);
			preauthDTO
					.setTreatmentRemarksList(getTreatmentRemarksHistory(preauthDTO
							.getClaimKey()));

			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					"");
			
			Double insuredSumInsured = 0d;
			if(null != claimByKey &&  null != claimByKey.getIntimation() && 
			null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
			null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
			&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
			{
				insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService
						.getGPAInsuredSumInsured(preauthDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								preauthDTO.getPolicyDto().getKey());
			}

			preauthDTO.setStrUserName(dto.getUsername());
			
			String branchOfficeCode = claimByKey.getIntimation().getPolicy().getHomeOfficeCode();
			/*OrganaizationUnit organization = createRodService.getBranchCode(branchOfficeCode);
			if(null != organization)
			{
				preauthDTO.getPreauthDataExtractionDetails().setOrganisationName(organization.getOrganizationUnitName());
			}*/
			if(null != insuredSumInsured)
			{
			preauthDTO.getPreauthDataExtractionDetails().setPaSumInsured(insuredSumInsured);
			}
			
			preauthDTO.getPreauthDataExtractionDetails().setOrganisationName(claimByKey.getIntimation().getPolicy().getProposerFirstName());
			preauthDTO.getPreauthDataExtractionDetails().setDateOfBirth(claimByKey.getGpaParentDOB());			
			preauthDTO.getPreauthDataExtractionDetails().setAge(claimByKey.getGpaParentAge());
			preauthDTO.getPreauthDataExtractionDetails().setParentName(claimByKey.getGpaParentName());
			preauthDTO.getPreauthDataExtractionDetails().setRiskName(claimByKey.getGpaRiskName());
			preauthDTO.getPreauthDataExtractionDetails().setGpaRiskDOB(claimByKey.getGpaRiskDOB());
			preauthDTO.getPreauthDataExtractionDetails().setGpaRiskAge(claimByKey.getGpaRiskAge());
			preauthDTO.getPreauthDataExtractionDetails().setGpaSection(claimByKey.getGpaSection());
		//	preauthDTO.getPreauthDataExtractionDetails().setGpaCategory(claimByKey.getGpaCategory());
			
			if(null == insuredSumInsured)
			{
			preauthDTO.getPreauthDataExtractionDetails().setPaSumInsured(0d);
			}
			if(null == claimByKey.getGpaParentAge())
			{
			preauthDTO.getPreauthDataExtractionDetails().setAge(0d);
			}

			if(null == claimByKey.getGpaRiskAge())
			{
			preauthDTO.getPreauthDataExtractionDetails().setGpaRiskAge(0d);
			}
		
			loadRRCRequestValues(preauthDTO, insuredSumInsured,
					SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT);

			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if (strPremiaFlag != null
					&& ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance()
						.get64VBStatus(
								preauthDTO.getNewIntimationDTO().getPolicy()
										.getPolicyNumber(), preauthDTO.getNewIntimationDTO().getIntimationId());
				if (get64vbStatus != null
						&& SHAConstants.DISHONOURED
								.equalsIgnoreCase(get64vbStatus)) {
					preauthDTO.setIsDishonoured(true);
				} else if (get64vbStatus != null
						&& (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
					preauthDTO.setIsPending(true);
				}

				// if(rrcDTO.getNewIntimationDTO() != null &&
				// rrcDTO.getNewIntimationDTO().getInsuredPatient() != null &&
				// rrcDTO.getNewIntimationDTO().getInsuredPatient().getKey() !=
				// null){
				// Long claimCount =
				// preauthService.getClaimCount(rrcDTO.getNewIntimationDTO().getPolicy().getKey());
				// if(claimCount != null){
				// rodDTO.setClaimCount(claimCount);
				// }
				// }
				/*
				 * Long productCode =
				 * rodDTO.getClaimDTO().getNewIntimationDto().
				 * getPolicy().getProduct().getKey(); Long insuredKey =
				 * rodDTO.getClaimDTO
				 * ().getNewIntimationDto().getInsuredPatient().getKey();
				 * 
				 * if(null != productCode && null != insuredKey) {
				 * rodDTO.getDocumentDetails
				 * ().setAdditionalCovers(dbCalculationService
				 * .getBebefitAdditionalCovers(SHAConstants.ADDITIONAL_COVER,
				 * productCode,insuredKey));
				 * 
				 * rodDTO.getDocumentDetails().setOptionalCovers(
				 * dbCalculationService
				 * .getBebefitAdditionalCovers(SHAConstants.OPTIONAL_COVER,
				 * productCode,insuredKey)); ======= } Preauth preauthRec =
				 * null;
				 * 
				 * for (Preauth eachPreauth : preauthByClaimKey) { String[]
				 * eachSplit = eachPreauth.getPreauthId().split("/"); Integer
				 * eachSeqNumber = Integer .valueOf(eachSplit[eachSplit.length -
				 * 1]); preauthRec = eachPreauth; break; //
				 * if(eachSeqNumber.equals(1)) { // preauthRec = eachPreauth; //
				 * break; // } }
				 * 
				 * if (preauthRec != null) {
				 * preauthDTO.getPreauthDataExtractionDetails()
				 * .setPreauthTotalApprAmt(
				 * preauthRec.getTotalApprovalAmount()); }
				 * preauthDTO.setStrUserName(dto.getUsername());
				 * preauthDTO.setStrPassword(dto.getPassword());
				 * preauthDTO.setSittingsAmount(dbCalculationService
				 * .getDialysisAmount(
				 * preauthDTO.getNewIntimationDTO().getPolicy()
				 * .getProduct().getKey(),
				 * insuredSumInsured.longValue()).intValue()); Map<String,
				 * String> popupMessages = dbCalculationService
				 * .getPOPUPMessages(preauthDTO.getPolicyKey(), preauthDTO
				 * .getNewIntimationDTO().getInsuredPatient().getKey());
				 * preauthDTO.setPopupMap(popupMessages);
				 * 
				 * if (preauthDTO.getNewIntimationDTO().getHospitalDto() != null
				 * && preauthDTO.getNewIntimationDTO().getHospitalDto()
				 * .getSuspiciousFlag() != null &&
				 * preauthDTO.getNewIntimationDTO().getHospitalDto()
				 * .getSuspiciousFlag().equalsIgnoreCase("Y")) { if
				 * (preauthDTO.getNewIntimationDTO().getHospitalDto()
				 * .getSuspiciousRemarks() != null) { Map<String, String>
				 * suspiciousMap = SHAUtils
				 * .getSuspiciousMap(preauthDTO.getNewIntimationDTO()
				 * .getHospitalDto().getSuspiciousRemarks());
				 * preauthDTO.setSuspiciousPopupMap(suspiciousMap); >>>>>>>
				 * paclaimswithpremia } } /* if(null !=
				 * dto.getDocsRecievedTime()) preauthDTO.setSfxMatchedQDate
				 * (SHAUtils.formatDateForStarfax(dto.getDocsRecievedTime()));
				 */
				if (null != dto.getDocsReceivedDate())
					preauthDTO.setSfxMatchedQDate(dto.getDocsReceivedDate());

				// preauthDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(preauthDTO.getNewIntimationDTO().getAdmissionDate(),
				// preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));

				String policyYear = preauthDTO.getNewIntimationDTO()
						.getPolicyYear();
				if (policyYear != null) {
					String intValue = policyYear.replaceAll("[^0-9]", "");
					Integer policyAgeing = SHAUtils
							.getIntegerFromStringWithComma(intValue);
					preauthDTO
							.setAdmissionDatePopup(policyAgeing != null ? policyAgeing
									.equals(0) ? true : false : false);
				}

				preauthDTO.setTaskNumber(dto.getTaskNumber());

				// if(preauthDTO.getNewIntimationDTO() != null &&
				// preauthDTO.getNewIntimationDTO().getInsuredPatient() != null
				// &&
				// preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey()
				// !=
				// null){
				// Long claimCount =
				// preauthService.getClaimCount(preauthDTO.getNewIntimationDTO().getPolicy().getKey());
				// if(claimCount != null){
				// preauthDTO.setClaimCount(claimCount);
				// }
				// }

				preauthDTO.setIsPEDInitiated(pedQueryService
						.isPEDInitiated(preauthDTO.getNewIntimationDTO()
								.getPolicy().getKey()));
				preauthDTO.setIsPEDInitiatedForBtn(preauthDTO
						.getIsPEDInitiated());
				//SHAUtils.setDefaultCopayValue(preauthDTO);

				StarCommonUtils.getIncidentFlagAndDate(claimByKey, preauthDTO);
				preauthDTO.setDbOutArray(dto.getDbOutArray());
				
//				Portal Flag updated in cashless table
				if(dto.getNhpUpdDocumentKey() != null){
					preauthDTO.setNhpUpdKey(dto.getNhpUpdDocumentKey());
				}
				
				
				view.setPreMedicalEnhancementViewForPA(
						PAPremedicalEnhancementWizard.class, preauthDTO);

				Date endDate = new Date();

				log.info("%%%%%%%%%%%%%%%%%%%%%% PREMEDICAL_ENHANCEMENT TOTAL TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"
						+ SHAUtils.getDurationFromTwoDate(startDate, endDate));

			}
		}
	}
	
	protected void showPreAuthEnhancementWizard(
			@Observes @CDIEvent(PAMenuPresenter.SHOW_PREAUTH_ENHANCEMENT_WIZARD) final ParameterDTO parameters) {
		PASearchPreauthTableDTO dto = (PASearchPreauthTableDTO) parameters
				.getPrimaryParameter();
		PreMedicalMapper premedicalMapper = PreMedicalMapper.getInstance();
		// PreMedicalMapper.getAllMapValues();
		
		
		
		Preauth preauth = preauthService.getPreauthById(dto.getKey());
		Long preauthKey = preauth.getKey();
		

		PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
		preauthDTO.getPreauthDataExtractionDetails().setDischargeDate(
				preauth.getDateOfDischarge());

		getResidualAmount(preauth, preauthDTO);
	//	preauthDTO.setRodHumanTask(dto.getHumanTask());
		preauthDTO.setTaskNumber(dto.getTaskNumber());

		preauthDTO.setStrUserName(dto.getUsername());
		preauthDTO.setStrPassword(dto.getPassword());

		preauthDTO.setAmountRequested(dto.getPreAuthReqAmt());

		if(preauth.getCatastrophicLoss() != null) {
			
			SelectValue value = masterService.getCatastropheData(preauth.getCatastrophicLoss());
			preauthDTO.getPreauthDataExtractionDetails().setCatastrophicLoss(value);
		}

		if(preauth.getNatureOfLoss() != null) {
			
			SelectValue value = masterService.getMasterValueForNatureCause(preauth.getNatureOfLoss());
			preauthDTO.getPreauthDataExtractionDetails().setNatureOfLoss(value);
		}
		if(preauth.getCauseOfLoss() != null) {
			SelectValue value = masterService.getMasterValueForNatureCause(preauth.getCauseOfLoss());
			preauthDTO.getPreauthDataExtractionDetails().setCauseOfLoss(value);
		}
		
		Intimation intimationByKey = intimationService
				.getIntimationByKey(preauth.getIntimation().getKey());

		preauthDTO
				.setCpuProvisionAmt(intimationByKey.getCpuCode() != null ? intimationByKey
						.getCpuCode().getProvisionAmount() : null);
		
		
		Claim claimByKey = claimService.getClaimByKey(preauth.getClaim()
				.getKey());
		
		Boolean isValidClaim = true;
		if (null != claimByKey) {
			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaim = false;
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			}
		}
		if (isValidClaim) {
			
		
		
		

		if (preauth
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)
				|| preauth
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS)) {
			// preauthDTO.getPreauthMedicalProcessingDetails().setRejectionRemarks(preauth.getRemarks());
			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					preauth.getRemarks());
		} else if (preauthDTO.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS)
				|| preauthDTO
						.getStatusKey()
						.equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS)) {
			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					preauth.getRemarks());
		} else if (preauthDTO.getStatusKey().equals(
				ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS)
				|| preauthDTO.getStatusKey().equals(
						ReferenceTable.ENHANCEMENT_QUERY_STATUS)
				|| preauthDTO.getStatusKey().equals(
						ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)
				|| preauthDTO.getStatusKey().equals(
						ReferenceTable.ENHANCEMENT_QUERY_RECEIVED_STATUS)) {
			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					preauth.getRemarks());
		} else if (preauth.getStatus().getKey()
				.equals(ReferenceTable.ENHANCEMENT_ESCALATION_STATUS)) {
			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					preauth.getRemarks());
		} else if (preauth
				.getStatus()
				.getKey()
				.equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS)
				|| preauth
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY)
				|| preauth
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY)
				|| preauth
						.getStatus()
						.getKey()
						.equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS)
				|| preauth
						.getStatus()
						.getKey()
						.equals(ReferenceTable.ENHANCEMENT_REFER_TO_COORDINATOR_STATUS)) {
			Coordinator coordinatorByPreauthKey = coordinatorService
					.getCoordinatorByPreauthKey(preauth.getClaim().getKey());
			preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
					coordinatorByPreauthKey.getCoordinatorRemarks());
			preauthDTO.getCoordinatorDetails().setCoordinatorRemarks(
					coordinatorByPreauthKey.getCoordinatorRemarks());
		}

		

		Policy policy = claimByKey.getIntimation().getPolicy();

		List<Claim> claimList = claimService.getClaimsByPolicyNumber(policy
				.getPolicyNumber());
		for (Claim claim : claimList) {
			Boolean isPedPending = false;
			List<OldInitiatePedEndorsement> pedInitiateDetail = pedQueryService
					.getPedInitiateDetail(claim.getKey());
			for (OldInitiatePedEndorsement oldInitiatePedEndorsement : pedInitiateDetail) {
				if (oldInitiatePedEndorsement.getStatus().getKey()
						.equals(ReferenceTable.PED_PENDING_ENDORSEMENT_STATUS)) {
					preauthDTO.setIsPedPending(true);
					isPedPending = true;
				}

				
			//}
		
		}
	

			if (isPedPending) {
				break;
			}
		}

		String[] split = preauth.getPreauthId().split("/");
		Integer seqNumber = Integer.valueOf(split[split.length - 1]);

		List<Preauth> preauthByClaimKey = preauthService
				.getPreauthByClaimKey(claimByKey.getKey());

		Preauth preauthRec = null;

		for (Preauth eachPreauth : preauthByClaimKey) {
			String[] eachSplit = eachPreauth.getPreauthId().split("/");
			Integer eachSeqNumber = Integer
					.valueOf(eachSplit[eachSplit.length - 1]);
			// if(eachSeqNumber.equals(1)) {
			preauthRec = eachPreauth;
			break;
			// }
		}

		Preauth previousPreauthDetails = new Preauth();
		if (!preauthByClaimKey.isEmpty()) {
			for (Preauth previousPreauth : preauthByClaimKey) {
				String[] preauthSplit = previousPreauth.getPreauthId().split(
						"/");
				Integer previousSeqNumber = Integer
						.valueOf(preauthSplit[preauthSplit.length - 1]);

				if (!preauth.equals(previousPreauth)) {
					previousPreauthDetails = previousPreauth;

				} else {
					break;
				}

			}
		}
		// preauthDTO.setCopay(getCopay(preauth.getPolicy().getProduct().getKey()));

		if (previousPreauthDetails != null) {
			preauthDTO.setPreviousPreauthKey(previousPreauthDetails.getKey());
			if (previousPreauthDetails.getStatus() != null
					&& (previousPreauthDetails
							.getStatus()
							.getKey()
							.equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) || previousPreauthDetails
							.getStatus()
							.getKey()
							.equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS))) {
				preauthDTO.setPreviousPreauthKey(0l);
			}
			List<ClaimAmountDetails> claimAmountDetailsByPreauth = preauthService
					.getClaimAmountDetailsByPreauth(previousPreauthDetails
							.getKey());
			Float amount = 0f;
			for (ClaimAmountDetails claimAmountDetails : claimAmountDetailsByPreauth) {
				if (claimAmountDetails.getPaybleAmount() != null) {
					amount += claimAmountDetails.getPaybleAmount();
				}
			}

			preauthDTO.setPreviousPreauthPayableAmount(previousPreauthDetails
					.getTotalApprovalAmount() != null ? previousPreauthDetails
					.getTotalApprovalAmount().intValue() : 0);
			
			if(previousPreauthDetails.getStatus() != null && (previousPreauthDetails.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) 
					||  previousPreauthDetails.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT) 
					|| previousPreauthDetails.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
					|| previousPreauthDetails.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS))) {
				preauthDTO.setIsPreviousPreauthWithdraw(true);
				
			}
		}

		setpreauthTOPreauthDTO(premedicalMapper, claimByKey, preauth,
				preauthDTO, true);

		if (previousPreauthDetails != null
				&& previousPreauthDetails.getTotalApprovalAmount() != null) {
			preauthDTO.getPreauthDataExtractionDetails().setApprovedAmount(
					String.valueOf(previousPreauthDetails
							.getTotalApprovalAmount().intValue()));
			preauthDTO.getPreauthDataExtractionDetails().setTotalApprAmt(
					previousPreauthDetails.getTotalApprovalAmount());
		}

		preauthDTO.setCopay(getCopay(preauthDTO.getNewIntimationDTO()
				.getInsuredAge(), preauth.getPolicy().getProduct().getKey()));
		preauthDTO
				.setTreatmentRemarksList(getTreatmentRemarksHistory(preauthDTO
						.getClaimKey()));

		List<FVRGradingMaster> fvrGrading = reimbursementService
				.getFVRGrading();
		List<FieldVisitRequest> fvrByClaimKey = reimbursementService
				.getFVRByClaimKey(preauth.getClaim().getKey());
		List<FvrGradingDetailsDTO> fvrDtoList = new ArrayList<FvrGradingDetailsDTO>();
		Map<Integer, FieldVisitRequest> valueMap = new HashMap<Integer, FieldVisitRequest>();
		for (int i = 0; i < fvrByClaimKey.size(); i++) {
			FieldVisitRequest fieldVisitRequest = fvrByClaimKey.get(i);
			if (fieldVisitRequest != null
					&& fieldVisitRequest.getStatus().getKey()
							.equals(ReferenceTable.FVR_REPLY_RECEIVED)) {
				FvrGradingDetailsDTO fvrDto = new FvrGradingDetailsDTO();
				fvrDto.setKey(fieldVisitRequest.getKey());
				fvrDto.setRepresentativeName(fieldVisitRequest
						.getRepresentativeName());
				fvrDto.setRepresentiveCode(fieldVisitRequest
						.getRepresentativeCode());
				List<FVRGradingDTO> FVRTableDTO = new ArrayList<FVRGradingDTO>();
				for (FVRGradingMaster masterFVR : fvrGrading) {
					FVRGradingDTO eachFVRDTO = new FVRGradingDTO();
					eachFVRDTO.setKey(masterFVR.getKey());
					eachFVRDTO.setCategory(masterFVR.getGradingType());
					eachFVRDTO.setApplicability(masterFVR.getApplicability());
					switch (Integer.valueOf(String.valueOf(masterFVR.getKey()))) {
					case 8:
						if (fieldVisitRequest.getPatientVerified() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPatientVerified());
						}
						break;
					case 9:
						if (fieldVisitRequest.getDiagnosisVerfied() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getDiagnosisVerfied());
						}
						break;
					case 10:
						if (fieldVisitRequest.getRoomCategoryVerfied() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getRoomCategoryVerfied());
						}
						break;
					case 11:
						if (fieldVisitRequest.getTriggerPointsFocused() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getTriggerPointsFocused());
						}
						break;
					case 12:
						if (fieldVisitRequest.getPedVerified() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPedVerified());
						}
						break;
					case 13:
						if (fieldVisitRequest.getPatientDischarged() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPatientDischarged());
						}
						break;
					case 14:
						if (fieldVisitRequest.getPatientNotAdmitted() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getPatientNotAdmitted());
						}
						break;
					case 15:
						if (fieldVisitRequest.getOutstandingFvr() != null) {
							eachFVRDTO.setStatusFlag(fieldVisitRequest
									.getOutstandingFvr());
						}
						break;
					default:
						break;
					}
					BeanItemContainer<SelectValue> selectValueContainer = masterService
							.getSelectValueContainer(ReferenceTable.COMMON_VALUES);
					eachFVRDTO.setCommonValues(selectValueContainer);
					FVRTableDTO.add(eachFVRDTO);
				}
				fvrDto.setFvrGradingDTO(FVRTableDTO);
				fvrDtoList.add(fvrDto);
			}
		}

		preauthDTO.getPreauthMedicalDecisionDetails().setFvrGradingDTO(
				fvrDtoList);
		
		Double insuredSumInsured = 0d;
		if(null != claimByKey &&  null != claimByKey.getIntimation() && 
		null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
		null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
		&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
		{

			insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{
			insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					preauthDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), preauthDTO.getPolicyDto()
							.getKey());
		}
		
		String branchOfficeCode = claimByKey.getIntimation().getPolicy().getHomeOfficeCode();
		OrganaizationUnit organization = createRodService.getBranchCode(branchOfficeCode);
		/*if(null != organization)
		{
			preauthDTO.getPreauthDataExtractionDetails().setOrganisationName(organization.getOrganizationUnitName());
		}*/
		if(null != insuredSumInsured)
		{
		preauthDTO.getPreauthDataExtractionDetails().setPaSumInsured(insuredSumInsured);
		}
		
		preauthDTO.getPreauthDataExtractionDetails().setOrganisationName(claimByKey.getIntimation().getPolicy().getProposerFirstName());
		preauthDTO.getPreauthDataExtractionDetails().setDateOfBirth(claimByKey.getGpaParentDOB());			
		preauthDTO.getPreauthDataExtractionDetails().setAge(claimByKey.getGpaParentAge());
		preauthDTO.getPreauthDataExtractionDetails().setParentName(claimByKey.getGpaParentName());
		preauthDTO.getPreauthDataExtractionDetails().setRiskName(claimByKey.getGpaRiskName());
		preauthDTO.getPreauthDataExtractionDetails().setGpaRiskDOB(claimByKey.getGpaRiskDOB());
		preauthDTO.getPreauthDataExtractionDetails().setGpaRiskAge(claimByKey.getGpaRiskAge());
		preauthDTO.getPreauthDataExtractionDetails().setGpaSection(claimByKey.getGpaSection());
	//	preauthDTO.getPreauthDataExtractionDetails().setGpaCategory(claimByKey.getGpaCategory());
		
		if(null == insuredSumInsured)
		{
		preauthDTO.getPreauthDataExtractionDetails().setPaSumInsured(0d);
		}
		if(null == claimByKey.getGpaParentAge())
		{
		preauthDTO.getPreauthDataExtractionDetails().setAge(0d);
		}

		if(null == claimByKey.getGpaRiskAge())
		{
		preauthDTO.getPreauthDataExtractionDetails().setGpaRiskAge(0d);
		}
		
		loadRRCRequestValues(preauthDTO, insuredSumInsured,
				SHAConstants.PROCESS_ENHANCEMENT);

		if (preauthDTO.getPreauthMedicalDecisionDetails().getInvestigatorCode() != null) {
			TmpInvestigation tmpInvestigationByInvestigatorCode = investigationService
					.getTmpInvestigationByInvestigatorCode(preauthDTO
							.getPreauthMedicalDecisionDetails()
							.getInvestigatorCode());
			preauthDTO.getPreauthMedicalDecisionDetails().setInvestigatorName(
					tmpInvestigationByInvestigatorCode);
		}

		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if (strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(
					preauthDTO.getNewIntimationDTO().getPolicy()
							.getPolicyNumber(), preauthDTO.getNewIntimationDTO().getIntimationId());
			if (get64vbStatus != null
					&& SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				preauthDTO.setIsDishonoured(true);
			} else if (get64vbStatus != null
					&& (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
				preauthDTO.setIsPending(true);
			}
		}

		/*
		 * maternity flag from db
		 */
		String maternityFlag = dbCalculationService
				.getMaternityFlagForProduct(preauth.getIntimation().getPolicy()
						.getProduct().getKey());
		if (maternityFlag != null && maternityFlag.equalsIgnoreCase("Y")) {
			preauthDTO.setMaternityFlag(true);
		}

		preauthDTO.setSittingsAmount(dbCalculationService.getDialysisAmount(
				preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey(), insuredSumInsured.longValue()).intValue());
		Map<String, String> popupMessages = dbCalculationService
				.getPOPUPMessages(preauthDTO.getPolicyKey(), preauthDTO
						.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
		preauthDTO.setPopupMap(popupMessages);

		if (preauthDTO.getNewIntimationDTO().getHospitalDto() != null
				&& preauthDTO.getNewIntimationDTO().getHospitalDto()
						.getSuspiciousFlag() != null
				&& preauthDTO.getNewIntimationDTO().getHospitalDto()
						.getSuspiciousFlag().equalsIgnoreCase("Y")) {
			if (preauthDTO.getNewIntimationDTO().getHospitalDto()
					.getSuspiciousRemarks() != null) {
				Map<String, String> suspiciousMap = SHAUtils
						.getSuspiciousMap(preauthDTO.getNewIntimationDTO()
								.getHospitalDto().getSuspiciousRemarks());
				preauthDTO.setSuspiciousPopupMap(suspiciousMap);
			}
		}

		if (preauthRec != null) {
			preauthDTO
					.getPreauthDataExtractionDetails()
					.setPreauthTotalApprAmt(preauthRec.getTotalApprovalAmount());
		}

		if (null != dto.getDocReceivedTimeForMatch())
			preauthDTO.setSfxMatchedQDate(dto.getDocReceivedTimeForMatch());

		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA6(
				dto.getCMA6());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA5(
				dto.getCMA5());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA4(
				dto.getCMA4());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA3(
				dto.getCMA3());
		preauthDTO.getDownSizePreauthDataExtrationDetails().setCMA2(
				dto.getCMA2());
		preauthDTO.getDownSizePreauthDataExtractionDetails().setCMA1(
				dto.getCMA1());

		if (preauthDTO.getNewIntimationDTO() != null
				&& preauthDTO.getNewIntimationDTO().getPolicy() != null
				&& preauthDTO.getNewIntimationDTO().getPolicy().getKey() != null) {
			Long claimCount = preauthService.getClaimCount(preauthDTO
					.getNewIntimationDTO().getPolicy().getKey());
			if (claimCount != null) {
				preauthDTO.setClaimCount(claimCount);
			}
		}

		preauthDTO.setAdmissionDatePopup(dbCalculationService
				.getPolicyAgeingForPopup(preauthDTO.getNewIntimationDTO()
						.getAdmissionDate(), preauthDTO.getNewIntimationDTO()
						.getPolicy().getPolicyNumber()));
		preauthDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(preauthDTO
				.getNewIntimationDTO().getPolicy().getKey()));
		preauthDTO.setIsPEDInitiatedForBtn(preauthDTO.getIsPEDInitiated());
		//SHAUtils.setDefaultCopayValue(preauthDTO);
		
		StarCommonUtils.getIncidentFlagAndDate(claimByKey, preauthDTO);
		preauthDTO.setDbOutArray(dto.getDbOutArray());
		
		if(null != preauth && null != preauth.getWorkPlace() && (SHAConstants.YES_FLAG.equalsIgnoreCase(preauth.getWorkPlace()))){
			preauthDTO.getPreauthDataExtractionDetails().setWorkOrNonWorkPlace(true);
		}
		
		if(null != preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode()){
			
			MasterGST masGstObj = masterService.getGSTByStateId(preauthDTO.getNewIntimationDTO().getPolicy().getHomeOfficeCode());

			if(null != masGstObj){
	
				preauthDTO.setGstNumber(masGstObj.getGstNumber());
				preauthDTO.setArnNumber(masGstObj.getArnNumber());
				preauthDTO.setGstState(masGstObj.getStateName());
			}
		}
		 //CR2019234 cashless 
		if(preauth.getIntimation().getIntimatedBy().getKey() != null && 
				preauth.getIntimation().getIntimatedBy().getKey().toString().equalsIgnoreCase(SHAConstants.AGENT_CODE) 
				|| preauth.getIntimation().getIntimatedBy().getKey().toString().equalsIgnoreCase(SHAConstants.SALES_MANAGER_CODE)){
			view.showInfoPopUp("This claim has been intimated by" + preauth.getIntimation().getIntimatedBy().getValue());
		}
		
//		Portal Flag updated in cashless table
		if(dto.getNhpUpdDocumentKey() != null){
			preauthDTO.setNhpUpdKey(dto.getNhpUpdDocumentKey());
		}
		
		view.setPreauthEnhancementWizardViewForPA(
				PAPreauthEnhancementWizard.class, preauthDTO);
	}
	}
	
	protected void showPAHealthProcessClaimBilling(
			@Observes @CDIEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_BILLING) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
		BeanItemContainer<SelectValue> productNameCode = masterService
				.getProductCodeName();
		
		BeanItemContainer<SelectValue> selectValueContainerForType = masterService.getStageList();
		
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
		
		view.setViewsPAHealthProcessClaimBilling(PAHealthSearchProcessClaimBillingView.class,
				true, selectValueContainerForCPUCode, productNameCode,selectValueContainerForType,selectValueForPriority,statusByStage);
	}
	
	protected void showClaimBillingWizard(
			@Observes @CDIEvent(PAMenuPresenter.SHOW_PA_HEALTH_MEDICAL_APPROVAL_CLAIM_BILLING_SCREEN) final ParameterDTO parameters) {

		PAHealthSearchProcessClaimBillingTableDTO tableDTO = (PAHealthSearchProcessClaimBillingTableDTO) parameters
				.getPrimaryParameter();

		Long ackDocKey = createRodService
				.getLatestDocAcknowledgementKey(tableDTO.getRodKey());
		DocAcknowledgement docAcknowledgment = createRodService
				.getDocAcknowledgementBasedOnKey(ackDocKey);

		Reimbursement reimbursementObjectByKey = rodService
				.getReimbursementObjectByKey(tableDTO.getRodKey());
		
		Boolean isValidClaimForMA = true;
		if(null != reimbursementObjectByKey)
		{
			Claim claimByKey = (reimbursementObjectByKey.getClaim());
			if(null != claimByKey)
			{
				if((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey.getLegalFlag()))
				{
					isValidClaimForMA = false;
					view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
				}
			}
		}
		if(isValidClaimForMA)
		{
		
		
		ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
		PreauthDTO reimbursementDTO = mapper
				.getReimbursementDTO(reimbursementObjectByKey);
		
		if(("Y").equalsIgnoreCase(reimbursementDTO.getPreauthDataExtractionDetails().getReconsiderationFlag()))
		{
			List<ReconsiderRODRequestTableDTO> reconsiderDTOList = getReconsiderRODRequestForBillEntry(tableDTO.getRodKey());
			if(null != reconsiderDTOList && !reconsiderDTOList.isEmpty())
			{
				for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reconsiderDTOList) {
					if(null != reconsiderRODRequestTableDTO.getIsRejectReconsidered() && reconsiderRODRequestTableDTO.getIsRejectReconsidered())
					{
						reimbursementDTO.setIsRejectReconsidered(true);
					}
					else
					{
						reimbursementDTO.setIsRejectReconsidered(false);
					}
				}
			}
			reimbursementDTO.setReconsiderRodRequestList(reconsiderDTOList);
			//reimbursementDTO.setReconsiderRodRequestList(getReconsiderRODRequestForBillEntry(tableDTO.getRodKey()));
		}

		Claim claimByKey = claimService.getClaimByClaimKey(reimbursementObjectByKey.getClaim().getKey());
		if(null != claimByKey)
		{
			if(null != reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId())
			{
				String  docReceivedFrom = reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue();
				reimbursementDTO.setPreviousAccountDetailsList(populatePreviousAccountDetails(claimByKey.getIntimation().getIntimationId(),docReceivedFrom));
			}
		}
		Long reimbursementKey = tableDTO.getRodKey();
		// Reimbursement previousLatestROD =
		// getPreviousLatestROD(claimByKey.getKey(), reimbursementObjectByKey);
		if (false) {
			// reimbursementKey = previousLatestROD.getKey();
			// reimbursementDTO = mapper.getReimbursementDTO(previousLatestROD);
			// reimbursementDTO.setIsPostHospitalization(true);
			// setReimbursmentTOPreauthDTO(mapper, claimByKey,
			// previousLatestROD, reimbursementDTO, true );
			// Hospitals hospitalById =
			// hospitalService.getHospitalById(claimByKey.getIntimation().getHospital());
			// ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails =
			// reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
			// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			// updateHospitalDetails.setHospitalState(hospitalById.getState());
			// updateHospitalDetails.setHospitalCode(hospitalById.getHospitalCode());
			// reimbursementDTO.setReconsiderationList(getReconsiderRODRequest(claimByKey));
			// reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
			// previousLatestROD.getKey());
			//
			// reimbursementDTO.setIsPreviousROD(true);
			// // reimbursementDTO.setPreviousROD(previousLatestROD);

		} else {
			reimbursementDTO
					.setReconsiderationList(getReconsiderRODRequest(claimByKey));

			reimbursementDTO = setReimbursmentTOPreauthDTO(mapper, claimByKey,
					reimbursementObjectByKey, reimbursementDTO, true, SHAConstants.BILLING);
			reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
					reimbursementObjectByKey.getKey());
		}

		UploadDocumentDTO reimbursementBenefitsValue = claimRequestBenefitsService
				.getReimbursementBenefitsValue(reimbursementObjectByKey
						.getKey());
		if (reimbursementBenefitsValue != null) {
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setUploadDocumentDTO(reimbursementBenefitsValue);
		}
		if (null != reimbursementBenefitsValue
				&& reimbursementBenefitsValue.getPatientCareBenefitFlag() != null
				&& reimbursementBenefitsValue.getPatientCareBenefitFlag()
						.equalsIgnoreCase("PC")) {
			List<ReimbursementBenefitsDetails> patientCareTableByBenefitKey = reimbursementService
					.getPatientCareTableByBenefitKey(reimbursementBenefitsValue
							.getPatientBenefitKey());
			List<PatientCareDTO> patientCareDTOList = new ArrayList<PatientCareDTO>();
			if (patientCareTableByBenefitKey != null
					&& !patientCareTableByBenefitKey.isEmpty()) {
				for (ReimbursementBenefitsDetails patientCareDetails : patientCareTableByBenefitKey) {
					PatientCareDTO dto = new PatientCareDTO();
					dto.setEngagedFrom(patientCareDetails.getEngagedFrom());
					dto.setEngagedTo(patientCareDetails.getEngagedTo());
					dto.setKey(patientCareDetails.getKey());
					patientCareDTOList.add(dto);
				}
			}
			reimbursementDTO.getPreauthDataExtractionDetails()
					.getUploadDocumentDTO()
					.setPatientCareDTO(patientCareDTOList);
		}
		
		Date policyFromDate = reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyFromDate();
		
		Date admissionDate = reimbursementDTO.getNewIntimationDTO().getAdmissionDate();
		
		Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
		if(diffDays != null && diffDays<90){
			reimbursementDTO.setIs64VBChequeStatusAlert(true);
		}
		
		/*
		 * <<<<<<< HEAD
		 * 
		 * List<UploadDocumentDTO> rodSummaryDetails = rodService
		 * .getRODSummaryDetails(reimbursementObjectByKey.getKey());
		 * 
		 * =======
		 */

		/**
		 * Added for amount claimed table enhancement---- starts
		 * */
		Double insuredSumInsured = 0d;
		
		if(null != claimByKey.getIntimation() && null !=  claimByKey.getIntimation().getPolicy() && 
				null !=  claimByKey.getIntimation().getPolicy().getProduct() && 
				null !=  claimByKey.getIntimation().getPolicy().getProduct().getKey() &&
				!(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey()))){
	
			insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), reimbursementDTO
						.getPolicyDto().getKey(),reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		}
		else
		{				
			if(null != reimbursementObjectByKey && null != reimbursementObjectByKey.getBenefitsId() && 
					null != reimbursementObjectByKey.getBenefitsId().getKey() &&
					(ReferenceTable.HOSP_BENEFIT_MASTER_VALUE.equals(reimbursementObjectByKey.getBenefitsId().getKey())) ||
					(ReferenceTable.PART_BENEFIT_MASTER_VALUE.equals(reimbursementObjectByKey.getBenefitsId().getKey()))){		
				
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsuredForHospitalization(
						reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), reimbursementDTO
								.getPolicyDto().getKey(),reimbursementObjectByKey.getBenefitsId().getKey());
			}
			else
			{
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), reimbursementDTO
								.getPolicyDto().getKey());
			}
		}

	
		reimbursementDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
		
		Map<Integer, Object> detailsMap = new HashMap<Integer, Object>();
		
//		if(null != reimbursementDTO && null != reimbursementDTO.getNewIntimationDTO() && null != reimbursementDTO.getNewIntimationDTO().getHospitalDto() 
//				&& null != reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() && null != reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType()
//				&& reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType().getKey().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID))
//		{
//			String cityClass = createRodService.getHospitalCityClass(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
//			reimbursementDTO.getNewIntimationDTO().getHospitalDto()
//			.getRegistedHospitals().setCityClass(cityClass);
//			
//		}

		if(reimbursementObjectByKey.getSectionCategory() != null){
			
			
			detailsMap = dbCalculationService
					.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
							.getProduct().getKey(), insuredSumInsured,
							reimbursementDTO.getNewIntimationDTO().getHospitalDto()
									.getRegistedHospitals().getCityClass(),
							reimbursementDTO.getNewIntimationDTO()
									.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),reimbursementObjectByKey.getSectionCategory(),"A");
		}else{
			
		Long sectionCategory = 0l;
		if(reimbursementDTO.getPolicyDto()
							.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY) || reimbursementDTO.getPolicyDto()
							.getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY) || reimbursementDTO.getPolicyDto()
							.getProduct().getKey().equals(ReferenceTable.DIABETES_FLOATER_POLICY))
		{
			sectionCategory = 1l;
		}
			
			detailsMap = dbCalculationService
					.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
							.getProduct().getKey(), insuredSumInsured,
							reimbursementDTO.getNewIntimationDTO().getHospitalDto()
									.getRegistedHospitals().getCityClass(),
							reimbursementDTO.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),sectionCategory,"0");
							/**
							 * The below code is commented as per sathish sir suggestion for ticket 2355 (production tracker).
							 * By default, the section category would be 1 for three products 
							 * MED-PRD-033 , MED-PRD-032 , MED-PRD-030
							 * 
							 * */
							
			
									//.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),0l,"0");
		}
		
//		if(detailsMap != null && detailsMap.get(15) != null){
//			reimbursementDTO.setAmbulanceLimitAmount((Double)detailsMap.get(15));
//		}
		
		List<Double> copayValue = dbCalculationService.getProductCoPay(reimbursementDTO
				.getClaimDTO().getNewIntimationDto().getPolicy().getProduct()
				.getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
				.getInsuredPatient().getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
				.getInsuredPatient().getInsuredId(),reimbursementDTO.getNewIntimationDTO());
		reimbursementDTO.setProductCopay(copayValue);

		/**
		 * Added for amount claimed table enhancement---- ends
		 * */

		List<UploadDocumentDTO> rodSummaryDetails = rodService
				.getRODSummaryDetails(reimbursementObjectByKey.getKey());
		
		/**
		 * Fix for implemeting claims dms in bill entry screen.. -- starts
		 * **/
		if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
		{
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				if(null != claimByKey)
				{
					uploadDocumentDTO.setClaimNo(claimByKey.getClaimId());
					uploadDocumentDTO.setDmsDocumentDTOList(getClaimsDMSList(claimByKey.getIntimation().getIntimationId()));
				}
			}
		}
		
		reimbursementDTO = getProrataFlagFromProduct(reimbursementDTO);
		
		reimbursementDTO.setProrataDeductionFlag(reimbursementObjectByKey.getProrataDeductionFlag());
		/**
		 * product based variable is added to enable or disable the component in page level.
		 * This would be static. -- starts
		 * */
		//reimbursementDTO.setProductBasedProRata(reimbursementObjectByKey.getProrataDeductionFlag());
		//reimbursementDTO.setProductBasedPackage(reimbursementObjectByKey.getPackageAvailableFlag());
		//ends.
		reimbursementDTO.setPackageAvailableFlag(reimbursementObjectByKey.getPackageAvailableFlag());


		// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
		List<Long> documentSummaryKeys = new ArrayList<Long>();
		for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
			
			/*if(null != reimbursementObjectByKey)
			{
				uploadDocumentDTO.setIntimationNo(reimbursementObjectByKey.getClaim().getIntimation().getIntimationId());
				uploadDocumentDTO.setDateOfAdmission(SHAUtils.formatDate(reimbursementObjectByKey.getDateOfAdmission()));
				uploadDocumentDTO.setDateOfDischarge(SHAUtils.formatDate(reimbursementObjectByKey.getDateOfDischarge()));
				uploadDocumentDTO.setInsuredPatientName(reimbursementObjectByKey.getClaim().getIntimation().getInsuredPatientName());
			}*/
			
			documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
			uploadDocumentDTO.setStatus(true);
			List<RODBillDetails> billEntryDetails = rodService
					.getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
			List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
			if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
				for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
					/*
					 * <<<<<<< HEAD
					 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
					 * uploadDocumentDTO)); =======
					 */
					dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,
							uploadDocumentDTO));
					// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
				}
			}
			uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
					.get(8));
			uploadDocumentDTO
					.setProductBasedICURent((Double) detailsMap.get(9));
			/*uploadDocumentDTO.setProductBasedAmbulanceAmt((Double) detailsMap
					.get(15));*/
			uploadDocumentDTO.setProductBasedAmbulanceAmt(getProductBasedAmbulanceAmt(tableDTO.getRodKey()));
			uploadDocumentDTO.setStatus(false);
			uploadDocumentDTO.setBillEntryDetailList(dtoList);
			uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
			uploadDocumentDTO.setRodKey(reimbursementObjectByKey.getKey());

			uploadDocumentDTO.setStrUserName(tableDTO.getUsername());

			if (uploadDocumentDTO != null) {
				if ((ReferenceTable.BENEFITS_PATIENT_CARE)
						.equalsIgnoreCase(uploadDocumentDTO
								.getPatientCareBenefitFlag())) {
					List<PatientCareDTO> patientCareList = claimRequestBenefitsService
							.getPatientCareDetails(uploadDocumentDTO
									.getPatientBenefitKey());
					if (null != patientCareList && !patientCareList.isEmpty()) {
						uploadDocumentDTO.setPatientCareDTO(patientCareList);
					}
					// rodDTO.getAddOnBenefitsDTO().setAdmittedNoOfDays(uploadDocumentDTO.getPatientCareNoofDays());
				}
			}
		}
		List<UploadDocumentDTO> rodBillSummaryDetails = rodService
				.getRODBillSummaryDetails(reimbursementObjectByKey.getKey(), mapper);
		
		if(rodBillSummaryDetails != null){
			for (UploadDocumentDTO uploadDocumentDTO2 : rodBillSummaryDetails) {
				uploadDocumentDTO2.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
			}
		}
		
		reimbursementDTO.getUploadDocDTO().setBillingWorkSheetUploadDocumentList(rodBillSummaryDetails);

		Double totalBilledAmount = reimbursementService
				.getTotalBilledAmount(documentSummaryKeys);

		reimbursementDTO.setAmountConsidered(totalBilledAmount != null ? String
				.valueOf(totalBilledAmount.intValue()) : "0");
		reimbursementDTO
				.setInitialAmountConsidered(totalBilledAmount != null ? String
						.valueOf(totalBilledAmount.intValue()) : "0");
		reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		//reimbursementDTO.setRodHumanTask(tableDTO.getHumanTask());
		reimbursementDTO.setKey(reimbursementObjectByKey.getKey());
		reimbursementDTO.setRodNumber(reimbursementObjectByKey.getRodNumber());

		setCategoryFlag(reimbursementObjectByKey, reimbursementDTO);
		
		if (!reimbursementDTO.getHospitalizaionFlag()) {
			reimbursementDTO = setHospitalizationAmount(reimbursementDTO);
		}
		
		Boolean isBasedOnPremium = false;
		if(ReferenceTable.getPremiumDeductionProductKeys().containsKey(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))) {
			isBasedOnPremium = true;
		}
		if(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyInstalmentFlag() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) 
				&& (reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag() != null 
				&& reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))){
			isBasedOnPremium = true;
		}
		
//		IMSSUPPOR-32761
		if(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyInstalmentFlag() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) 
                && (reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag() != null 
                && reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))){
			isBasedOnPremium = true;
		}
		List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
				.search(reimbursementObjectByKey.getClaim().getKey(), isBasedOnPremium);

		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

			previousPreAuthTableDTO.setRequestedAmt(preauthService
					.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
							previousPreAuthTableDTO.getClaimKey()));
			newList.add(previousPreAuthTableDTO);

		}
		
		

		reimbursementDTO.setPreviousPreauthTableDTO(newList);

	//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());
		reimbursementDTO.setStrUserName(tableDTO.getUsername());
		reimbursementDTO.setStrPassword(tableDTO.getPassword());
        reimbursementDTO.setTaskNumber(tableDTO.getTaskNumber());
		
		
		reimbursementDTO.getPreauthMedicalDecisionDetails().setBillingRemarks(
				"");
		
		
		List<AddOnBenefitsDTO> populateAddOnBenefitsTableValues = claimRequestBenefitsService
				.populateAddOnBenefitsTableValues(
						reimbursementDTO.getNewIntimationDTO(),
						reimbursementObjectByKey.getClaim().getKey(),
						reimbursementObjectByKey.getKey(),
						reimbursementDTO.getProductCopay(),null);
		reimbursementDTO.getPreauthDataExtractionDetails()
				.setAddOnBenefitsDTOList(populateAddOnBenefitsTableValues);
		reimbursementDTO = roomRentNursingMapping(reimbursementDTO, 8l, 9l, false);
		reimbursementDTO = roomRentNursingMapping(reimbursementDTO, 10l, 11l, true);
		reimbursementDTO.getPreauthDataExtractionDetails()
				.setDocAckknowledgement(
						reimbursementObjectByKey.getDocAcknowLedgement());

		if (reimbursementObjectByKey.getDocAcknowLedgement()
				.getHospitalizationRepeatFlag() != null
				&& reimbursementObjectByKey.getDocAcknowLedgement()
						.getHospitalizationRepeatFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			reimbursementDTO.setIsHospitalizationRepeat(true);
		}
		
		if (reimbursementObjectByKey.getStatus().getKey()
				.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
			MedicalApprover medicalApprover = reimbursementService
					.getMedicalApprover(reimbursementObjectByKey.getKey());
			if (medicalApprover != null) {
				reimbursementDTO.setPreviousRemarks(medicalApprover
						.getApproverReply());
				reimbursementDTO.setPreviousReasonForReferring(medicalApprover
						.getReasonForReferring());
			}
			reimbursementDTO.setIsReferToBilling(true);
		}
		
		if (claimByKey.getClaimType() != null
				&& claimByKey.getClaimType().getKey()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {

//			PreMedicalMapper premedicalMapper = new PreMedicalMapper();
			Preauth previousPreauth = getPreviousPreauth(claimByKey.getKey());
			
			if(previousPreauth != null) {
				reimbursementDTO.setPreauthKey(previousPreauth.getKey());
			}
		}
		
		if(claimByKey.getStatus() != null) {
			if(claimByKey.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) ||  claimByKey.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)) {
				reimbursementDTO.setIsWithDrawn(true);
			}
		}
		
		loadRRCRequestValues(reimbursementDTO,insuredSumInsured,SHAConstants.BILLING);
		
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber(), reimbursementDTO.getNewIntimationDTO().getIntimationId());
			if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				reimbursementDTO.setIsDishonoured(true);
			}  else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
				reimbursementDTO.setIsPending(true);
			}
		}
		reimbursementDTO.setSittingsAmount(dbCalculationService.getDialysisAmount(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(), insuredSumInsured.longValue()).intValue());
		Map<String, String> popupMessages = dbCalculationService.getPOPUPMessages(reimbursementDTO.getPolicyKey(), reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey(),reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
		reimbursementDTO.setPopupMap(popupMessages);
		
		
		if(reimbursementDTO.getNewIntimationDTO().getHospitalDto() != null && reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag() != null
				&& reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag().equalsIgnoreCase("Y")){
			if(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks() != null){
				Map<String, String> suspiciousMap = SHAUtils.getSuspiciousMap(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks());
				reimbursementDTO.setSuspiciousPopupMap(suspiciousMap);
			}
		}
		// If hospitalization is not processed then remaining ROD should be rejected.
		Reimbursement hospROD = reimbursementService.getHospitalizationROD(reimbursementObjectByKey.getClaim().getKey());
		if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
			if(hospROD == null) {
				reimbursementDTO.setIsHospitalizationRejected(true);
			}
		}
		
		Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
				.getIntimation().getHospital());
		
		paymentDetailsForfinancial(reimbursementObjectByKey, reimbursementDTO,
				hospitalById);
		
		setReimbursementValues(reimbursementObjectByKey, reimbursementDTO);
		
       Reimbursement hospitalizationOrPartialROD = reimbursementService.getHospitalizationOrPartialROD(reimbursementObjectByKey.getClaim().getKey());
		
		if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			
			
			Integer seqNumber = 0;
			
			if(reimbursementObjectByKey.getRodNumber() != null){
				String[] split = reimbursementObjectByKey.getRodNumber().split("/");
				seqNumber = Integer.valueOf(split[split.length - 1]);
			}
			
			if(hospitalizationOrPartialROD == null && ! seqNumber.equals(1)) {
//				reimbursementDTO.setIsDishonoured(true);
				reimbursementDTO.setIsHospitalizationRejected(true);
			}
		}
		
		Map<String, Integer> productBenefitFlag = dbCalculationService.getProductBenefitFlag(reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey());
		
		if(productBenefitFlag.get(SHAConstants.PRE_HOSP_FLAG).equals(0)) {
			reimbursementDTO.setIsPreHospApplicable(false);
		}
		
		if(productBenefitFlag.get(SHAConstants.POST_HOSP_FLAG).equals(0)) {
			reimbursementDTO.setIsPostHospApplicable(false);
		}
		
		if(productBenefitFlag.get(SHAConstants.LUMP_SUM_FLAG).equals(1)) {
			reimbursementDTO.setIsLumpsumApplicable(true);
		}
		
		if(productBenefitFlag.get(SHAConstants.HOSPITALCASH_FLAG).equals(1)) {
			reimbursementDTO.setIsHospitalCashApplicable(true);
		}
		
		if(productBenefitFlag.get(SHAConstants.PATIENTCARE_FLAG).equals(1)) {
			reimbursementDTO.setIsPatientCareApplicable(true);
		}
		
		
		MastersValue networkHospitalType = masterService.getMaster(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getNetworkHospitalTypeId());
		reimbursementDTO.setNetworkHospitalType(networkHospitalType.toString());
		
		reimbursementDTO = checkFinalEnhancement(reimbursementDTO);

		
		try {
			if((reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest() != null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest().equalsIgnoreCase("Y"))
					|| (reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))) {
				if(reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag() ==  null || reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag().equalsIgnoreCase("N")) {
					reimbursementDTO.setIsReconsiderationRequest(true);
					if(reimbursementService.isClaimPaymentAvailable(reimbursementObjectByKey.getRodNumber())) {
						Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
						reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
					} else {
						/**
						 * For reject reconsider, cancel rod button was enabled. But for this ticket GALAXYMAIN-6222	
						 * which was raised in PA, now cancel rod button is diabled for rejection case to.
						 * Hence below code was commented.
						 * */
						
						//reimbursementDTO.setIsReconsiderationRequest(false);
					}
//					Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
//					reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			reimbursementDTO.setIsCashlessType(true);
		}
		if(reimbursementDTO.getIsCashlessType() && reimbursementDTO.getHospitalizaionFlag() && (reimbursementDTO.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY))){
			reimbursementDTO.setIsDirectToBilling(true);
			Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursementDTO.getClaimDTO().getKey());
			reimbursementDTO.getClaimDTO().setLatestPreauthKey(latestPreauth.getKey());
		}
//		reimbursementDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(reimbursementDTO.getNewIntimationDTO().getAdmissionDate(), reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));
		
		String policyYear = reimbursementDTO.getNewIntimationDTO().getPolicyYear();
	    if(policyYear != null){
	    	String intValue = policyYear.replaceAll("[^0-9]", "");
	    	Integer policyAgeing = SHAUtils.getIntegerFromStringWithComma(intValue);
	    	reimbursementDTO.setAdmissionDatePopup(policyAgeing != null ? policyAgeing.equals(0) ? true : false :false);
	    }
		
		reimbursementDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey()));
		reimbursementDTO.setIsPEDInitiatedForBtn(reimbursementDTO.getIsPEDInitiated());
		
		List<InsuredPedDetails> pedByInsured = policyService.getPEDByInsured(reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		
		reimbursementDTO.setInsuredPedDetails(pedByInsured);
		
		if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			if(reimbursementDTO.getHospitalizaionFlag()) {
				Reimbursement partialHospitalizationRODWithSettled = getPartialHospitalizationRODWithSettled(claimByKey.getKey());
				ReimbursementCalCulationDetails hosptialization = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursementDTO.getKey(), ReferenceTable.HOSPITALIZATION);
				if(hosptialization != null) {
					reimbursementDTO.setBillingApprovedAmount(hosptialization.getPayableToHospAftTDS() != null ? hosptialization.getPayableToHospAftTDS().doubleValue() : 0d);
					reimbursementDTO.setPayableToHospAmt(hosptialization.getPayableToHospital() != null ? hosptialization.getPayableToHospital().doubleValue() : 0d);
					reimbursementDTO.setHospDiscountAmount(hosptialization.getHospitalDiscount() != null ? hosptialization.getHospitalDiscount().doubleValue() : 0d);
				}
				if(partialHospitalizationRODWithSettled != null) {
					ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(partialHospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
					reimbursementDTO.setPayableToInsAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium().doubleValue() : 0d);
					reimbursementDTO.setFAApprovedAmount(partialHospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? partialHospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
				}
			} else if(reimbursementDTO.getPartialHospitalizaionFlag()) {
				Reimbursement hospitalizationRODWithSettled = getHospitalizationRODWithSettled(claimByKey.getKey());
				if(hospitalizationRODWithSettled != null) {
					ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(hospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
					reimbursementDTO.setPayableToHospAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital().doubleValue() : 0d);
					reimbursementDTO.setHospDiscountAmount(reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d);
					Double amount = (reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d) + (hospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? hospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
					reimbursementDTO.setFAApprovedAmount(amount);
				}
			}
		}
		
		Boolean queryReceivedStatusRod = reimbursementQuerySerice.isQueryReceivedStatusRod(reimbursementObjectByKey.getKey());
		 
		 if(queryReceivedStatusRod){
			 reimbursementDTO.setIsQueryReceived(true);
		 }
		 
		 
			List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = acknowledgementDocumentsReceivedService.getReimbursementCalculationDetails(reimbursementObjectByKey.getKey());
			if(reimbursementCalculationDetails != null && !reimbursementCalculationDetails.isEmpty()) {
				for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
					if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
							reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.HOSPITALIZATION)){
						
						HopitalizationCalulationDetailsDTO hospitalizationCalcDTO = getHospitalizationCalculationDTO(reimbursementCalCulationDetails2,reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
						if(reimbursementDTO.getDocumentReceivedFromId() != null && reimbursementDTO.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
					           hospitalizationCalcDTO.setBalanceToBePaid(0);
						}
						reimbursementDTO.setHospitalizationCalculationDTO(hospitalizationCalcDTO);
						
					}
					else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
							reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)){
						PreHopitalizationDetailsDTO preHospitalizationCalcDTO = getPreHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
						reimbursementDTO.setPreHospitalizationCalculationDTO(preHospitalizationCalcDTO);
						
					}else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
							reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
						PostHopitalizationDetailsDTO postHospitalizationCalcDTO = getPostHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
						reimbursementDTO.setPostHospitalizationCalculationDTO(postHospitalizationCalcDTO);
					}
				}
			}
		
			reimbursementDTO.setAmountConsidered(reimbursementDTO.getAmountConsidedAfterCoPay() != null ? String
					.valueOf(reimbursementDTO.getAmountConsidedAfterCoPay().intValue()) : "0");
		 
//		 reimbursementService.setBPMForClaimBilling(reimbursementDTO, false, "MEDICAL", reimbursementObjectByKey);
			Double totalClaimedAmount = reimbursementService.getTotalClaimedAmount(reimbursementObjectByKey);
			 reimbursementDTO.setRodTotalClaimedAmount(totalClaimedAmount);
			 
			 if(reimbursementDTO.getAmountConsidedAfterCoPay() != null && totalBilledAmount != null && !reimbursementDTO.getAmountConsidedAfterCoPay().equals(totalBilledAmount)) {
				 reimbursementDTO.setAmountConsidered(String.valueOf(totalBilledAmount.longValue()));
			 }

			 //SHAUtils.setDefaultCopayValue(reimbursementDTO);

			 if(reimbursementDTO.getIsReconsiderationRequest() != null && reimbursementDTO.getIsReconsiderationRequest()){
				 reimbursementDTO.setIsReverseAllocation(false);
			 }

			 if(reimbursementObjectByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
					 && reimbursementObjectByKey.getDocAcknowLedgement() != null && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
					 && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
			 
					 if(hospitalById != null){
							Boolean hospitalDiscount = SHAUtils.isHospitalDiscount(hospitalById);
							reimbursementDTO.setIsHospitalDiscountApplicable(hospitalDiscount);
					 }
			 }

			 //			 if(reimbursementDTO.getNewIntimationDTO() != null && reimbursementDTO.getNewIntimationDTO().getInsuredPatient() != null &&
			 //					 reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey() != null){
			 //					Long claimCount = preauthService.getClaimCount(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
			 //					if(claimCount != null){
			 //						reimbursementDTO.setClaimCount(claimCount);
			 //					}
			 //				}

			 reimbursementDTO.setDbOutArray(tableDTO.getDbOutArray());

			 if(null != reimbursementObjectByKey && null != reimbursementObjectByKey.getWorkPlace() &&
					 (SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementObjectByKey.getWorkPlace()))){
				 reimbursementDTO.getPreauthDataExtractionDetails().setWorkOrNonWorkPlace(true);
			 }

			 if(ReferenceTable.getGMCProductList().containsKey(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey()) ||
					 ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey())){
				 Insured insuredByKey = intimationService.getInsuredByKey(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey());
				 Insured MainMemberInsured = null;

				 if(insuredByKey.getDependentRiskId() == null){
					 MainMemberInsured = insuredByKey;
				 }else{
					 Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredNameForDefault(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId());
					 MainMemberInsured = insuredByPolicyAndInsuredId;
				 }

				 if(MainMemberInsured != null){

					 reimbursementDTO.getNewIntimationDTO().setGmcMainMemberName(MainMemberInsured.getInsuredName());
					 reimbursementDTO.getNewIntimationDTO().setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());

				 }
			 }

			 LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
			 legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
			 reimbursementDTO.setLegalHeirDto(legalHeirDTO);

			 List<NomineeDetailsDto> nomineeDtoList = intimationService.getNomineeDetailsListByTransactionKey(tableDTO.getRodKey());
			 if( nomineeDtoList != null && !nomineeDtoList.isEmpty()){
				 reimbursementDTO.getNewIntimationDTO().setNomineeList(nomineeDtoList);
			 }

			 if(reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null 
						&& reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()) {
				 List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(tableDTO.getRodKey());
				 if(legalHeirList != null && !legalHeirList.isEmpty()) {
					 List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
					 LegalHeirDTO legalHeirDto;
					 for (LegalHeir legalHeir : legalHeirList) {
						 legalHeirDto = new LegalHeirDTO(legalHeir);
						 legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
						 legalHeirDTOList.add(legalHeirDto);
					 }
					 reimbursementDTO.setLegalHeirDTOList(legalHeirDTOList);
				 }	
			 }
			 if(reimbursementObjectByKey.getClaim().getLegalClaim() !=null
					 && reimbursementObjectByKey.getClaim().getLegalClaim().equals("Y")){
				 LegalTaxDeduction taxDeduction = reimbursementService.getTaxDeductionFromRODKey(reimbursementObjectByKey.getKey());
				 if(taxDeduction !=null){
					 LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
					 LegalBillingDTO billingDTO = taxDeductionMapper.getDTOFromTaxDeduction(taxDeduction);
					 billingDTO = reimbursementService.calculateLegalSettlment(billingDTO);
					 reimbursementDTO.setLegalBillingDTO(billingDTO);
				 }
			 }
			 
			 Map<String,Object> icrAgent  = dbCalculationService.getAgentAndBranchName(reimbursementDTO.getNewIntimationDTO().getIntimationId()); 
			 if(icrAgent != null && !icrAgent.isEmpty()) {
					String agentScorePoint = icrAgent.get(SHAConstants.ICR_AGENT_POINT).toString();
					reimbursementDTO.setIcrAgentValue(agentScorePoint);
					String smScorepoint = icrAgent.get(SHAConstants.SM_AGENT_POINT).toString();
					reimbursementDTO.setSmAgentValue(smScorepoint);
			 }
			 
			 Policy policy = reimbursementDTO.getNewIntimationDTO().getPolicy();
			 // added for PA installment payment process in policy level and product
			 // level check
			 Map<String, String> getPolicyInstallment = dbCalculationService
					 .getInstallmentPaymentFlag(policy.getPolicyNumber(), policy.getProduct()
							 .getKey());
			 if (getPolicyInstallment != null && !getPolicyInstallment.isEmpty()) {
				 reimbursementDTO.setPolicyInstalmentFlag(getPolicyInstallment
						 .get(SHAConstants.FLAG) != null ? getPolicyInstallment
								 .get(SHAConstants.FLAG) : "N");
				 reimbursementDTO
				 .setPolicyInstalmentMsg(getPolicyInstallment
						 .get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallment
								 .get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
			 }
			 if (reimbursementDTO.getPolicyInstalmentFlag() != null
					 && reimbursementDTO.getPolicyInstalmentFlag().equals(
							 SHAConstants.YES_FLAG)) {
				 Integer policyInstallmentAmount = PremiaService.getInstance()
						 .getPolicyInstallmentAmount(policy
								 .getPolicyNumber());
				 reimbursementDTO.setPolicyInstalmentPremiumAmt(policyInstallmentAmount
						 .doubleValue());

				 String policyDueDate = PremiaService.getInstance()
						 .getPolicyInstallmentDetails(policy
								 .getPolicyNumber());
				 //code added if we recevied instalment due amount as zero after pending amount adjusted to premia/bancs  need to set due date as admission date by noufel
				 if((reimbursementDTO.getPolicyInstalmentPremiumAmt() != null && reimbursementDTO.getPolicyInstalmentPremiumAmt() == 0d) && 
						 (policyDueDate == null || policyDueDate.isEmpty())) {

					 reimbursementDTO.setPolicyInstalmentDueDate(reimbursementDTO.getPreauthDataExtractionDetails().getAdmissionDate());	
				 }
				 else{
					 if (reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
						 reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.formatTimeFromString(policyDueDate.toString()));	
					 }
					 else{
						 reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.dateformatFromString(policyDueDate.toString()));
					 }
				 }
			 }
				
		view.setPAHealthProcessClaimBilling(PAHealthBillingWizard.class, reimbursementDTO);		
		
	}
	}
	
	private PreauthDTO roomRentNursingMapping(PreauthDTO bean, Long roomRentCategoryId, Long nursingChargesCategoryId, Boolean isIcuMapping) {
		List<RoomRentMatchingDTO> dtoList = new ArrayList<RoomRentMatchingDTO>();
		List<NursingChargesMatchingDTO> nursingDTOList = new ArrayList<NursingChargesMatchingDTO>();
		List<UploadDocumentDTO> uploadDocumentDTOList = bean
				.getUploadDocumentDTO();
		List<SelectValue> listValues = new ArrayList<SelectValue>();
		Long i = 1l;
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocumentDTOList) {
			List<BillEntryDetailsDTO> billEntryDetailList = uploadDocumentDTO
					.getBillEntryDetailList();
			for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {

				if (billEntryDetailsDTO.getCategory() != null
						&& billEntryDetailsDTO.getCategory().getId() != null
						&& billEntryDetailsDTO.getCategory().getId().equals(roomRentCategoryId)) {
					RoomRentMatchingDTO dto = new RoomRentMatchingDTO();
					dto.setIdentityId(i++);
					dto.setItemName(billEntryDetailsDTO.getCategory().getId().equals(10l) ? "ICU Room Rent" : "Room Rent");
					dto.setId(billEntryDetailsDTO.getKey());
					dto.setBillNumber(billEntryDetailsDTO.getBillNo() != null ? billEntryDetailsDTO
							.getBillNo() : "");
					dto.setClaimedNoOfDays(billEntryDetailsDTO.getNoOfDays() != null ? billEntryDetailsDTO
							.getNoOfDays() : 0);
					dto.setAllowedNoOfDays(billEntryDetailsDTO
							.getNoOfDaysAllowed() != null ? billEntryDetailsDTO
							.getNoOfDaysAllowed() : 0);
					dto.setPerDayAmount(billEntryDetailsDTO.getPerDayAmt() != null ? billEntryDetailsDTO
							.getPerDayAmt() : 0);
					SelectValue value = new SelectValue();
					value.setId(dto.getId());
					value.setValue(dto.getIdentityId().toString());
					listValues.add(value);
					dtoList.add(dto);
				} else if (billEntryDetailsDTO.getCategory() != null
						&& billEntryDetailsDTO.getCategory().getValue() != null
						&& billEntryDetailsDTO.getCategory().getId().equals(nursingChargesCategoryId)) {
					NursingChargesMatchingDTO dto = new NursingChargesMatchingDTO();
					dto.setItemName(billEntryDetailsDTO.getCategory().getId().equals(11l) ? " ICU Nursing Charges" : "Nursing Charges");
					// dto.setKey(i++);
					dto.setId(billEntryDetailsDTO.getKey());
					dto.setBillNumber(billEntryDetailsDTO.getBillNo() != null ? billEntryDetailsDTO
							.getBillNo() : "");
					dto.setClaimedNoOfDays(billEntryDetailsDTO.getNoOfDays() != null ? billEntryDetailsDTO
							.getNoOfDays() : 0);
					dto.setAllocatedClaimedNoOfDays(0d);
					nursingDTOList.add(dto);
				}
			}
		}

		if (!dtoList.isEmpty() && !nursingDTOList.isEmpty()) {
			if (dtoList.size() == 1 && nursingDTOList.size() == 1) {
				nursingDTOList.get(0).setMapToRoomDays(
						dtoList.get(0).getAllowedNoOfDays());
				dtoList.get(0).setNursingChargesDTOList(nursingDTOList);
				if(!isIcuMapping) {
					bean.setRoomRentMappingDTOList(dtoList);
					bean.setIsOneMapping(false);
				} else {
					bean.setIcuRoomRentMappingDTOList(dtoList);
					bean.setIsICUoneMapping(false);
				}
				bean.setIsOneMapping(true);
			} else if (dtoList.size() > 1 || nursingDTOList.size() > 1) {
				for (RoomRentMatchingDTO roomRentDTO : dtoList) {

					List<NursingChargesMatchingDTO> nursingDTOLists = new ArrayList<NursingChargesMatchingDTO>();
					for (NursingChargesMatchingDTO nursingDTO : nursingDTOList) {
						nursingDTO.setListValues(listValues);
					}

					roomRentDTO.setNursingChargesDTOList(nursingDTOList);
				}
				if(!isIcuMapping) {
					bean.setRoomRentMappingDTOList(dtoList);
					bean.setIsOneMapping(false);
				} else {
					bean.setIcuRoomRentMappingDTOList(dtoList);
					bean.setIsICUoneMapping(false);
				}
				
			}
		}
		return bean;

	}
	
	protected void showGenerateReminderLetters(
			@Observes @CDIEvent(MenuItemBean.GENERATE_PA_REMINDER_LETTER_CLAIM_WISE) final ParameterDTO parameters) {
	
		Map<String,Object> referenceValue = new HashMap<String, Object>();
		BeanItemContainer<SelectValue> cpuCodeContainer = masterService
				.getTmpCpuCodeList();
		BeanItemContainer<SelectValue> claimTypeContainer = masterService
				.getClaimtypeContainer();
		
		referenceValue.put("cpuCodeContainer",cpuCodeContainer);
		referenceValue.put("claimTypeContainer", claimTypeContainer);	
		
		BeanItemContainer<SelectValue> categoryContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

		SelectValue allCategorySelectValue = new SelectValue(null,SHAConstants.ALL);
		SelectValue billsNotRecvdSelectValue = new SelectValue(null,SHAConstants.BILLS_NOT_RECEIVED);
		SelectValue preAuthbillsNotRecvdSelectValue = new SelectValue(null,SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
		SelectValue querySelectValue = new SelectValue(null,SHAConstants.QUERY);
		categoryContainer.addBean(allCategorySelectValue);
		categoryContainer.addBean(billsNotRecvdSelectValue);
		categoryContainer.addBean(preAuthbillsNotRecvdSelectValue);
		categoryContainer.addBean(querySelectValue);
		
		referenceValue.put("categoryContainer",categoryContainer);
		
		BeanItemContainer<SelectValue> reminderTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

		SelectValue allReminder = new SelectValue(null,SHAConstants.ALL);
		SelectValue firstReminder = new SelectValue(null,SHAConstants.FIRST_REMINDER);
		SelectValue secondReminder = new SelectValue(null,SHAConstants.SECOND_REMINDER);
		SelectValue closeReminder = new SelectValue(null,SHAConstants.CLOSE);
		reminderTypeContainer.addBean(allReminder);
		reminderTypeContainer.addBean(firstReminder);
		reminderTypeContainer.addBean(secondReminder);
		reminderTypeContainer.addBean(closeReminder);
		
		referenceValue.put("reminderTypeContainer",reminderTypeContainer);		
		
		view.setViewGeneratePAReminderLetter(SearchGeneratePARemainderView.class,
				true, referenceValue);
	}
	
	// this for Bulk Reminder Search
	
		protected void showGenerateBulkReminderLetters(
				@Observes @CDIEvent(MenuItemBean.GENERATE_PA_REMINDER_LETTER_BULK) final ParameterDTO parameters) {
		
			Map<String,Object> referenceValue = new HashMap<String, Object>();
			BeanItemContainer<SelectValue> cpuCodeContainer = masterService
					.getTmpCpuCodeList();
			BeanItemContainer<SelectValue> claimTypeContainer = masterService
					.getClaimtypeContainer();
			
			referenceValue.put("cpuCodeContainer",cpuCodeContainer);
			referenceValue.put("claimTypeContainer", claimTypeContainer);	
			
			BeanItemContainer<SelectValue> categoryContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

			SelectValue allCategorySelectValue = new SelectValue(null,SHAConstants.ALL);
			SelectValue billsNotRecvdSelectValue = new SelectValue(null,SHAConstants.BILLS_NOT_RECEIVED);
			SelectValue preAuthbillsNotRecvdSelectValue = new SelectValue(null,SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
			SelectValue querySelectValue = new SelectValue(null,SHAConstants.QUERY);
			categoryContainer.addBean(allCategorySelectValue);
			categoryContainer.addBean(billsNotRecvdSelectValue);
			categoryContainer.addBean(preAuthbillsNotRecvdSelectValue);
			categoryContainer.addBean(querySelectValue);
			
			referenceValue.put("categoryContainer",categoryContainer);
			
			BeanItemContainer<SelectValue> reminderTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

			SelectValue allReminder = new SelectValue(null,SHAConstants.ALL);
			SelectValue firstReminder = new SelectValue(null,SHAConstants.FIRST_REMINDER);
			SelectValue secondReminder = new SelectValue(null,SHAConstants.SECOND_REMINDER);
			SelectValue closeReminder = new SelectValue(null,SHAConstants.CLOSE);
			reminderTypeContainer.addBean(allReminder);
			reminderTypeContainer.addBean(firstReminder);
			reminderTypeContainer.addBean(secondReminder);
			reminderTypeContainer.addBean(closeReminder);
			
			referenceValue.put("reminderTypeContainer",reminderTypeContainer);		
			
			List<BulkReminderResultDto> prevRemindBatchList = bulkReminderSerachSevice.searchPrevBatch();
			
			referenceValue.put("prevBatchList",prevRemindBatchList);
			
			view.setViewGenerateBulkPAReminderLetter(SearchGeneratePARemainderBulkView.class,
					true, referenceValue);
		}
	
	protected void showDraftQueryLetterSearch(
			@Observes @CDIEvent(MenuItemBean.PA_DRAFT_QUERY_LETTER_SEARCH) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
		
		
		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(stageByKey3.getKey());
		selectValue2.setValue(stageByKey3.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
		
		view.setViewDefaultPAQuery(SearchDraftPAQueryLetterView.class, true,
				selectValueContainerForCPUCode,selectValueForPriority,statusByStage);
	}
	
	protected void showDraftQueryLetterDetailView(
			@Observes @CDIEvent(MenuItemBean.PA_DRAFT_QUERY_LETTER_DETAIL) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();

		SearchDraftQueryLetterTableDTO searchDraftQueryLetterTableDTO = (SearchDraftQueryLetterTableDTO) parameters
				.getPrimaryParameter();
		Long rodKey = searchDraftQueryLetterTableDTO.getRodKey();
		
		Reimbursement reimburment=reimbursementService.getReimbursementByKey(rodKey);
		
		Long reimbursementQueryKey = searchDraftQueryLetterTableDTO
				.getQueryKey();
		ReimbursementQueryDto reimbursementQueryDto = reimbursementQuerySerice
				.getReimbursementQuery(reimbursementQueryKey);
			searchDraftQueryLetterTableDTO
					.setReimbursementQueryDto(reimbursementQueryDto);
			
		String diagnosisForPreauthByKey = "";
		Double totalClaimApprovedAmt = 0d;
		if(reimburment != null ) {
			diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimburment.getKey());
			
			if(("Y").equalsIgnoreCase(reimbursementQueryDto.getQueryType())){
				reimbursementQueryDto.setGenerateDisVoucher(true);
				totalClaimApprovedAmt =  ( reimburment.getOptionalApprovedAmount() != null ? reimburment.getOptionalApprovedAmount() :  0d)
				+ ( reimburment.getBenApprovedAmt() != null ? reimburment.getBenApprovedAmt() : 0d ) 
				+ ( reimburment.getAddOnCoversApprovedAmount() != null ? reimburment.getAddOnCoversApprovedAmount() : 0d);
			String amtInWords = SHAUtils.getParsedAmount(totalClaimApprovedAmt);
			reimbursementQueryDto.setPaApprovedAmtInwords(amtInWords);
			}
		}
		searchDraftQueryLetterTableDTO.setDiagnosis(diagnosisForPreauthByKey);
		
		if(reimburment.getBenefitsId() != null) {
			SelectValue benefitSelect = new SelectValue(reimburment.getBenefitsId().getKey(),reimburment.getBenefitsId().getValue());
			searchDraftQueryLetterTableDTO.getReimbursementQueryDto().getReimbursementDto().setBenefitSelected(benefitSelect);
		}
		
		if(!reimbursementQueryDto.getReimbursementDto().getClaimDto().getIncidenceFlag()){
			
			reimbursementQueryDto.getReimbursementDto().getClaimDto().setDocToken(masterService.getDocumentDetailsByDocType(SHAConstants.PA_CLAIM_COVERING_LETTER,reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getIntimationId()));
		}
		
		searchDraftQueryLetterTableDTO.setGenerateDisVoucher(reimbursementQueryDto.getGenerateDisVoucher());
			List<ViewQueryDTO> queryList = searchDraftQueryLetterService
					.getQueryValues(reimbursementQueryDto.getReimbursementDto()
							.getKey(), reimbursementQueryDto.getReimbursementDto()
							.getClaimDto().getKey());
			
			if(queryList != null && !queryList.isEmpty()){
				for (ViewQueryDTO viewQueryDTO : queryList) {
					viewQueryDTO.setSno(queryList.indexOf(viewQueryDTO)+1);
					if(reimbursementQueryDto.getKey() == viewQueryDTO.getKey()){
						OptionGroup radio = new OptionGroup();
						radio.addItems("");			
						radio.select("");
						viewQueryDTO.setSelect(radio);					
					}
				}			
			}

	
			searchDraftQueryLetterTableDTO.setQueryDetailsList(queryList);

			LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
			legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
			searchDraftQueryLetterTableDTO.getPreAuthDto().setLegalHeirDto(legalHeirDTO);
	
			if(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() == null
					|| (reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null
					 && reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty())
					 || (reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased() != null 
					 && reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased())) {
			
				
				List<LegalHeir> displayDto = masterService.getlegalHeirListByTransactionKey(searchDraftQueryLetterTableDTO.getRodKey());	
				if(displayDto != null && !displayDto.isEmpty()) {
					List<LegalHeirDTO> list = new ArrayList<LegalHeirDTO>();
					LegalHeirDTO legalHeirDTODisplay;
					
					for (LegalHeir legalHeir : displayDto) {
						legalHeirDTODisplay = new LegalHeirDTO(legalHeir);
						list.add(legalHeirDTODisplay);
					}	
					
					searchDraftQueryLetterTableDTO.getReimbursementQueryDto().getReimbursementDto().setLegalHeirDTOList(list);
				}
			}
			
			reimbursementQueryDto.setDbOutArray(searchDraftQueryLetterTableDTO.getDbOutArray());
			
			
			Double insuredSumInsured = 0d;
			
			if(null != reimburment && null != reimburment.getClaim() && null != reimburment.getClaim().getIntimation() && 
					null != reimburment.getClaim().getIntimation().getPolicy() && null != reimburment.getClaim().getIntimation().getPolicy().getProduct() &&
					null != reimburment.getClaim().getIntimation().getPolicy().getProduct().getKey() 
					&& !(ReferenceTable.getGPAProducts().containsKey(reimburment.getClaim().getIntimation().getPolicy().getProduct().getKey())))
			{
			  insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient()
							.getInsuredId().toString(), reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getKey()
							,reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient()
								.getInsuredId().toString(), reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getKey());
			}
			
			reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().setTotalSumInsured(insuredSumInsured);
			
			reimbursementQueryDto.getReimbursementDto().setCauseOfAccident(reimburment.getAccidentCauseId() != null ? reimburment.getAccidentCauseId().getValue() : "");
			
			view.setDecideOnDraftPAQueryDetailWizardView(DecideOnDraftPAQueryDetailWizardViewImpl.class,
					searchDraftQueryLetterTableDTO);
	}

	protected void showProcessDraftQueryLetter(
			@Observes @CDIEvent(MenuItemBean.PA_PROCESS_DRAFT_QUERY_LETTER) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
			
        BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
			
		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(stageByKey3.getKey());
		selectValue2.setValue(stageByKey3.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
				
		view.setViewProcessDefaultPAQuery(SearchProcessDraftPAQueryView.class, true,
				selectValueContainerForCPUCode,selectValueForPriority,statusByStage);
	}
	
	protected void showProcessDraftPAQueryLetterDetail(
			@Observes @CDIEvent(MenuItemBean.PA_PROCESS_DRAFT_QUERY_LETTER_DETAIL) final ParameterDTO parameters) {

		SearchProcessDraftQueryTableDTO searchQueryLetterTableDto = (SearchProcessDraftQueryTableDTO) parameters
				.getPrimaryParameter();

		Long reimbursementQueryKey = searchQueryLetterTableDto.getQueryKey();
		
		Long rodKey = searchQueryLetterTableDto.getRodKey();
		
		Reimbursement reimburment=reimbursementService.getReimbursementByKey(rodKey);
		
		ClaimQueryDto claimQueryDto = new ClaimQueryDto();

		ReimbursementQueryDto reimbursementQueryDto = reimbursementQuerySerice
				.getReimbursementQuery(reimbursementQueryKey);
		
		String diagnosisForPreauthByKey = "";
		Double totalClaimApprovedAmt =  0d;
		if(reimburment != null ) {
			diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimburment.getKey());
			
			if(("Y").equalsIgnoreCase(reimbursementQueryDto.getQueryType())){
				reimbursementQueryDto.setGenerateDisVoucher(true);
				totalClaimApprovedAmt =  ( reimburment.getOptionalApprovedAmount() != null ? reimburment.getOptionalApprovedAmount() :  0d)
				+ ( reimburment.getBenApprovedAmt() != null ? reimburment.getBenApprovedAmt() : 0d ) 
				+ ( reimburment.getAddOnCoversApprovedAmount() != null ? reimburment.getAddOnCoversApprovedAmount() : 0d);
			String amtInWords = SHAUtils.getParsedAmount(totalClaimApprovedAmt);
			reimbursementQueryDto.setPaApprovedAmtInwords(amtInWords);
			}
		}
		searchQueryLetterTableDto.setDiagnosis(diagnosisForPreauthByKey);		
		
		claimQueryDto.setDiagnosis(diagnosisForPreauthByKey);
		
		if(reimburment.getBenefitsId() != null) {
			SelectValue benefitSelect = new SelectValue(reimburment.getBenefitsId().getKey(),reimburment.getBenefitsId().getValue());
			reimbursementQueryDto.getReimbursementDto().setBenefitSelected(benefitSelect);
		}
		
		reimbursementQueryDto.getReimbursementDto().getClaimDto().setDocToken(masterService.getDocumentDetailsByDocType(SHAConstants.PA_CLAIM_COVERING_LETTER,reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getIntimationId()));
	
		/*	reimbursementQueryDto.setHumanTask(searchQueryLetterTableDto
					.getHumanTaskDTO());*/
			
		/*reimbursementQueryDto.getReimbursementDto().getClaimDto().setNomineeName(reimbursementQueryDto.getReimbursementDto().getNomineeName());
		reimbursementQueryDto.getReimbursementDto().getClaimDto().setNomineeAddr(reimbursementQueryDto.getReimbursementDto().getNomineeAddr());
			
		if(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeName() == null || reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeName().isEmpty()){

			reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(reimbursementQueryDto.getReimbursementDto().getNomineeName());
		}	
		reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeAddr(reimbursementQueryDto.getReimbursementDto().getNomineeAddr());*/
		
		LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
		legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
		claimQueryDto.getPreAuthDto().setLegalHeirDto(legalHeirDTO);

		if(reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() == null
				|| (reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null
				 && reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty())
	|| (reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased() != null 
			 && reimbursementQueryDto.getReimbursementDto().getIsNomineeDeceased())) {
		
			
			List<LegalHeir> displayDto = masterService.getlegalHeirListByTransactionKey(searchQueryLetterTableDto.getRodKey());	
			if(displayDto != null && !displayDto.isEmpty()) {
				List<LegalHeirDTO> list = new ArrayList<LegalHeirDTO>();
				LegalHeirDTO legalHeirDTODisplay;
					
				for (LegalHeir legalHeir : displayDto) {
					legalHeirDTODisplay = new LegalHeirDTO(legalHeir);			
					list.add(legalHeirDTODisplay);
				}		
				
				searchQueryLetterTableDto.getReimbursementQueryDto().getReimbursementDto().setLegalHeirDTOList(list);
				reimbursementQueryDto.getReimbursementDto().setLegalHeirDTOList(list);
			}
		}	
		
		claimQueryDto.setReimbursementQueryDto(reimbursementQueryDto);
			List<ViewQueryDTO> queryList = searchDraftQueryLetterService
					.getQueryValues(reimbursementQueryDto.getReimbursementDto()
							.getKey(), reimbursementQueryDto.getReimbursementDto()
							.getClaimDto().getKey());
			
			if(queryList != null && !queryList.isEmpty()){
				for (ViewQueryDTO viewQueryDTO : queryList) {
					viewQueryDTO.setSno(queryList.indexOf(viewQueryDTO)+1);
					if(reimbursementQueryDto.getKey() == viewQueryDTO.getKey()){
						OptionGroup radio = new OptionGroup();
						radio.addItems("");			
						radio.select("");
						viewQueryDTO.setSelect(radio);					
					}
				}			
			}		
	
			claimQueryDto.setQueryDetails(queryList);
			reimbursementQueryDto.setDbOutArray(searchQueryLetterTableDto.getDbOutArray());
			
			
			Double insuredSumInsured = 0d;
			if(null != reimburment &&  null != reimburment.getClaim().getIntimation() && 
			null != reimburment.getClaim().getIntimation().getPolicy() && null != reimburment.getClaim().getIntimation().getPolicy().getProduct() &&
			null != reimburment.getClaim().getIntimation().getPolicy().getProduct().getKey() 
			&& !(ReferenceTable.getGPAProducts().containsKey(reimburment.getClaim().getIntimation().getPolicy().getProduct().getKey())))
			{
			
			 insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient()
							.getInsuredId().toString(), reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getKey()
							,reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient()
								.getInsuredId().toString(), reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getKey());
			}
			
			reimbursementQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().setTotalSumInsured(insuredSumInsured);
			
			reimbursementQueryDto.getReimbursementDto().setCauseOfAccident(reimburment.getAccidentCauseId() != null ? reimburment.getAccidentCauseId().getValue() : "");
			
			view.setDecideOnProcessDraftPAQueryWizardView(DecideOnProcessDraftPAQueryWizardViewImpl.class,
					claimQueryDto);
		
	}
	
	protected void showDraftRejectionLetter(
			@Observes @CDIEvent(MenuItemBean.PA_DRAFT_REJECTION_LETTER) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
		
		  BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
			
			
			Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
			Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

			SelectValue selectValue1 = new SelectValue();
			selectValue1.setId(stageByKey2.getKey());
			selectValue1.setValue(stageByKey2.getStageName());
			
			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(stageByKey3.getKey());
			selectValue2.setValue(stageByKey3.getStageName());
			
			BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
			statusByStage.addBean(selectValue1);
			statusByStage.addBean(selectValue2);
			
			
		view.setViewDefaultPARejectionLetter(SearchDraftPARejectionLetterView.class,
				true, selectValueContainerForCPUCode,selectValueForPriority,statusByStage);
	}
	
	protected void showDraftRejecionLetterDetailView(
			@Observes @CDIEvent(MenuItemBean.PA_DRAFT_REJECTION_LETTER_DETAIL) final ParameterDTO parameters) {
		view.setViewG(DraftPARejectionLetterDetailView.class, true);

		SearchDraftRejectionLetterTableDTO searchDraftRejectionLetterTableDTO = (SearchDraftRejectionLetterTableDTO) parameters
				.getPrimaryParameter();

		ReimbursementRejectionDto reimbursementRejectionDto = reimbursementRejectionService
				.getReimbursementRejectionByKey(searchDraftRejectionLetterTableDTO
						.getRodKey());

		if (reimbursementRejectionDto != null) {

			Reimbursement reimbursmentObj = reimbursementService
					.getReimbursementByKey(searchDraftRejectionLetterTableDTO
							.getRodKey());

			String diagnosisForPreauthByKey = "";
			if (reimbursmentObj != null) {
				diagnosisForPreauthByKey = preauthService
						.getDiagnosisForPreauthByKey(reimbursmentObj.getKey());

			}
			searchDraftRejectionLetterTableDTO
					.setDiagnosis(diagnosisForPreauthByKey);

			ReimbursementDto reimbursementDto = reimbursementRejectionDto
					.getReimbursementDto();
			
			Policy policy = reimbursementDto.getClaimDto()
					.getNewIntimationDto().getPolicy();

			if (policy.getHomeOfficeCode() != null) {
				
				List<MasOmbudsman> ombudsmanDetailsByCpuCode = getOmbudsmanOffiAddrByPIOCode(policy.getHomeOfficeCode());
				
				if(ombudsmanDetailsByCpuCode != null &&  !ombudsmanDetailsByCpuCode.isEmpty())
					reimbursementDto.getClaimDto().setOmbudsManAddressList(ombudsmanDetailsByCpuCode);
			}
			

			List<ReimbursementRejectionDetailsDto> rejectionDetailsList = getRejectionList(
					reimbursementRejectionDto.getReimbursementDto().getKey(),
					reimbursementRejectionDto.getKey());

			if (rejectionDetailsList != null && !rejectionDetailsList.isEmpty()) {
				searchDraftRejectionLetterTableDTO
						.setRejectionList(rejectionDetailsList);
			}
			
			LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
			legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
			searchDraftRejectionLetterTableDTO.getPreAuthDto().setLegalHeirDto(legalHeirDTO);
			
			reimbursementRejectionDto.setReimbursementDto(reimbursementDto);
			searchDraftRejectionLetterTableDTO
			.setReimbursementRejectionDto(reimbursementRejectionDto);
			
			
			List<NomineeDetailsDto> nomineeDtoList = intimationService.getNomineeDetailsListByTransactionKey(reimbursmentObj.getKey());
			if( nomineeDtoList != null && !nomineeDtoList.isEmpty()){
				searchDraftRejectionLetterTableDTO.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(nomineeDtoList);
			}
			if(searchDraftRejectionLetterTableDTO.getReimbursementRejectionDto().getReimbursementDto().getIsNomineeDeceased() != null
					&& searchDraftRejectionLetterTableDTO.getReimbursementRejectionDto().getReimbursementDto().getIsNomineeDeceased()){

				List<LegalHeir> displayDto = masterService.getlegalHeirListByTransactionKey(searchDraftRejectionLetterTableDTO.getRodKey());	
				if(displayDto != null && !displayDto.isEmpty()) {
					List<LegalHeirDTO> list = new ArrayList<LegalHeirDTO>();
					LegalHeirDTO legalHeirDTODisplay;
					for (LegalHeir legalHeir : displayDto) {
						legalHeirDTODisplay = new LegalHeirDTO(legalHeir);			
						list.add(legalHeirDTODisplay);
					}		
					
					searchDraftRejectionLetterTableDTO.getReimbursementRejectionDto().getReimbursementDto().setLegalHeirDTOList(list);
				}				
			}

			
			reimbursementRejectionDto.setDbOutArray(searchDraftRejectionLetterTableDTO
							.getDbOutArray());
			
			view.setDraftPARejectionLetterDetailView(
					DraftPARejectionLetterDetailView.class,
					searchDraftRejectionLetterTableDTO);
		}
	}

	private List<ReimbursementRejectionDetailsDto> getRejectionList(
			Long reimbKey,Long rejectionKey) {
		
		List<ReimbursementRejectionDetailsDto> rejectionDetailsList = reimbursementRejectionService
				.getRejectionTableDto(reimbKey);
		
		if(rejectionDetailsList != null && !rejectionDetailsList.isEmpty()){
			for (ReimbursementRejectionDetailsDto viewRejectionDetailsDTO : rejectionDetailsList) {
				if(viewRejectionDetailsDTO.getRejectionKey() != null){
				ReimbursementRejection rejection = acknowledgementDocumentsReceivedService.getReimbursementRejection(viewRejectionDetailsDTO.getRejectionKey());
				
				if(rejection != null && rejection.getReimbursement() != null){
					DocAcknowledgement docAck = rejection.getReimbursement().getDocAcknowLedgement();
					if(docAck != null){
						if(docAck.getBenifitFlag() != null){
							viewRejectionDetailsDTO.setBenefitsAndCovers(SHAUtils.getBenefitsValue(docAck.getBenifitFlag())+", "+acknowledgementDocumentsReceivedService.getCoverValueForViewBasedOnAckKey(docAck.getKey()));
						}
						else{
							viewRejectionDetailsDTO.setBenefitsAndCovers(acknowledgementDocumentsReceivedService.getCoverValueForViewBasedOnAckKey(docAck.getKey()));
						}
					}
					}
				}
				if(rejectionKey != null && viewRejectionDetailsDTO.getRejectionKey() != null && rejectionKey.equals(viewRejectionDetailsDTO.getRejectionKey())){
					OptionGroup radio = new OptionGroup();
					radio.addItems("");			
					radio.select("");
					viewRejectionDetailsDTO.setSelect(radio);					
				}
			}
		}
		return rejectionDetailsList;
	}

	protected void showProcessDraftRejectionLetter(
			@Observes @CDIEvent(MenuItemBean.PA_PROCESS_DRAFT_REJECTION_LETTER) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
		
		 	BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
			
			
			Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
			Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

			SelectValue selectValue1 = new SelectValue();
			selectValue1.setId(stageByKey2.getKey());
			selectValue1.setValue(stageByKey2.getStageName());
			
			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(stageByKey3.getKey());
			selectValue2.setValue(stageByKey3.getStageName());
			
			BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
			statusByStage.addBean(selectValue1);
			statusByStage.addBean(selectValue2);
		
		view.setViewProcessDefaultPARejection(
				SearchProcessDraftPARejectionView.class, true,
				selectValueContainerForCPUCode,selectValueForPriority,statusByStage);
	}
	
	protected void showProcessDraftRejectionLetterWizard(
			@Observes @CDIEvent(MenuItemBean.PA_DRAFT_REJECTION_LETTER_WIZARD) final ParameterDTO parameters) {

		view.setViewG(SearchProcessDraftPARejectionView.class, true);

		SearchProcessDraftRejectionTableDTO searchProcssRejectionLetterTableDto = (SearchProcessDraftRejectionTableDTO) parameters
				.getPrimaryParameter();
		Long reimbursementKey = searchProcssRejectionLetterTableDto.getRodKey();
		ClaimRejectionDto claimRejectionDto = new ClaimRejectionDto();

		ReimbursementRejectionDto reimbursementRejectionDto = reimbursementRejectionService
				.getReimbursementRejectionByKey(reimbursementKey);

		if (reimbursementRejectionDto != null) {

			String diagnosisForPreauthByKey = "";
			if (reimbursementKey != null) {
				diagnosisForPreauthByKey = preauthService
						.getDiagnosisForPreauthByKey(reimbursementKey);

			}
			claimRejectionDto.setDiagnosis(diagnosisForPreauthByKey);

			ReimbursementDto reimbursementDto = reimbursementRejectionDto
					.getReimbursementDto();

			Policy policy = reimbursementDto.getClaimDto().getNewIntimationDto().getPolicy();
			
			if (policy.getHomeOfficeCode() != null) {
				List<MasOmbudsman> ombudsmanDetailsByCpuCode = getOmbudsmanOffiAddrByPIOCode(policy.getHomeOfficeCode());	
				if (ombudsmanDetailsByCpuCode != null && !ombudsmanDetailsByCpuCode.isEmpty()) {
					reimbursementDto.getClaimDto().setOmbudsManAddressList(ombudsmanDetailsByCpuCode);
				}
			}
			
			
			reimbursementRejectionDto.setReimbursementDto(reimbursementDto);

			/*
			 * reimbursementRejectionDto
			 * .setHumanTask(searchProcssRejectionLetterTableDto
			 * .getHumanTaskDTO());
			 */

			claimRejectionDto
					.setReimbursementRejectionDto(reimbursementRejectionDto);

			List<ReimbursementRejectionDetailsDto> rejectionDetailsList = getRejectionList(
					reimbursementRejectionDto.getReimbursementDto().getKey(),
					reimbursementRejectionDto.getKey());

			if (rejectionDetailsList != null && !rejectionDetailsList.isEmpty()) {

				claimRejectionDto.setRejectionDetailsList(rejectionDetailsList);
			}
			
			LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
			legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
			claimRejectionDto.getPreAuthDto().setLegalHeirDto(legalHeirDTO);

			List<NomineeDetailsDto> nomineeDtoList = intimationService.getNomineeDetailsListByTransactionKey(searchProcssRejectionLetterTableDto.getRodKey());
			if( nomineeDtoList != null && !nomineeDtoList.isEmpty()){
				claimRejectionDto.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeList(nomineeDtoList);
			}
			if(claimRejectionDto.getReimbursementRejectionDto().getReimbursementDto().getIsNomineeDeceased() != null
					&& claimRejectionDto.getReimbursementRejectionDto().getReimbursementDto().getIsNomineeDeceased()){
				List<LegalHeir> displayDto = masterService.getlegalHeirListByTransactionKey(searchProcssRejectionLetterTableDto.getRodKey());	
				if(displayDto != null && !displayDto.isEmpty()) {
					List<LegalHeirDTO> list = new ArrayList<LegalHeirDTO>();
					LegalHeirDTO legalHeirDTODisplay;
					for (LegalHeir legalHeir : displayDto) {
						legalHeirDTODisplay = new LegalHeirDTO(legalHeir);			
						list.add(legalHeirDTODisplay);
						
					}		
					
					claimRejectionDto.getReimbursementRejectionDto().getReimbursementDto().setLegalHeirDTOList(list);
				}
			}	
			
			String rejectionRole = null;
			if (searchProcssRejectionLetterTableDto.getRejectionRole() != null) {
				if (searchProcssRejectionLetterTableDto.getRejectionRole()
						.equalsIgnoreCase(SHAConstants.SENIOR_MEDICAL_APPROVER)) {
					rejectionRole = "Senior Medical Approver";
				} else if (searchProcssRejectionLetterTableDto
						.getRejectionRole().equalsIgnoreCase(
								SHAConstants.MEDICAL_APPROVER_ROLE)) {
					rejectionRole = "Medical Approver";
				} else if (searchProcssRejectionLetterTableDto
						.getRejectionRole().equalsIgnoreCase(SHAConstants.RCMO)) {
					rejectionRole = "Reimbursement Chief Medical officer";
				} else {
					rejectionRole = "Financial Approver";
				}
			}
			claimRejectionDto.setRejectionRole(rejectionRole);
			reimbursementRejectionDto
					.setDbOutArray(searchProcssRejectionLetterTableDto
							.getDbOutArray());
			view.setDecideOnPARejectionWizardView(
					DecideOnPARejectionWizardViewImpl.class, claimRejectionDto);
		}
	}		
		protected void showPAProcessClaimBilling(
				@Observes @CDIEvent(MenuItemBean.PA_PROCESS_CLAIM_BILLING) final ParameterDTO parameters) {
			BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
					.getTmpCpuCodes();
			BeanItemContainer<SelectValue> productNameCode = masterService
					.getProductCodeName();
			
			BeanItemContainer<SelectValue> selectValueContainerForType = masterService.getStageList();
			
			BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
			
//			BeanItemContainer<SelectValue> statusByStage = masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);
			
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
			
			BeanItemContainer<SelectValue> selectValueContainerForBenefits = masterService.getSelectValueContainer(SHAConstants.PA_BENFITS);
			view.setViewsPAProcessClaimBilling(PASearchProcessClaimBillingView.class,
					true, selectValueContainerForCPUCode, productNameCode,selectValueContainerForType,selectValueForPriority,statusByStage,selectValueContainerForBenefits);
		}
		
		
		protected void showPAClaimApprovalWizard(
				@Observes @CDIEvent(PAMenuPresenter.SHOW_PA_CLAIM_APPROVAL_SCREEN) final ParameterDTO parameters) {

			SearchProcessClaimBillingTableDTO tableDTO = (SearchProcessClaimBillingTableDTO) parameters
					.getPrimaryParameter();	
		
			
			Long ackDocKey = createRodService
					.getLatestDocAcknowledgementKey(tableDTO.getRodKey());				
			
			
		
			
					showPAProcessClaimApprovalWizard(tableDTO);
			

		}

		protected void showPAClaimBillingWizard(
				@Observes @CDIEvent(PAMenuPresenter.SHOW_PA_MEDICAL_APPROVAL_CLAIM_BILLING_SCREEN) final ParameterDTO parameters) {

			SearchProcessClaimBillingTableDTO tableDTO = (SearchProcessClaimBillingTableDTO) parameters
					.getPrimaryParameter();

			Long ackDocKey = createRodService
					.getLatestDocAcknowledgementKey(tableDTO.getRodKey());
				
					showPAProcessClaimBillingWizard(tableDTO);

		}
		
		
		protected void showPAProcessClaimBillingWizard(
				SearchProcessClaimBillingTableDTO tableDTO) {

			Reimbursement reimbursementObjectByKey = rodService
					.getReimbursementObjectByKey(tableDTO.getRodKey());
			
			Claim objClaim = reimbursementObjectByKey.getClaim();
			
			Boolean isValidClaim = true;
			if (null != objClaim) {
				if ((SHAConstants.YES_FLAG).equalsIgnoreCase(objClaim
						.getLegalFlag())) {
					isValidClaim = false;
					view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
				}
			}
			
			if(isValidClaim) {
			ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
			PreauthDTO reimbursementDTO = mapper
					.getReimbursementDTO(reimbursementObjectByKey);
			
			if(null != reimbursementDTO)
			{
			if(null != reimbursementObjectByKey && null != reimbursementObjectByKey.getDocAcknowLedgement() && 
					null != reimbursementObjectByKey.getDocAcknowLedgement().getDocumentTypeId() &&
					(ReferenceTable.PA_PAYMENT_QUERY_REPLY).equals(reimbursementObjectByKey.getDocAcknowLedgement().getDocumentTypeId()))
			{
				reimbursementDTO.setIsPaymentQuery("Y");
			}
			else
			{
				reimbursementDTO.setIsPaymentQuery("N");
			}
			
			if(null != reimbursementObjectByKey && null != reimbursementObjectByKey.getBenefitsId() && null != reimbursementObjectByKey.getBenefitsId().getKey())
			{
				reimbursementDTO.setBenefitId(reimbursementObjectByKey.getBenefitsId().getKey());
			}
			if(("Y").equalsIgnoreCase(reimbursementDTO.getPreauthDataExtractionDetails().getReconsiderationFlag()))
			{
				List<ReconsiderRODRequestTableDTO> reconsiderDTOList = getReconsiderRODRequestForBillEntry(tableDTO.getRodKey());
				if(null != reconsiderDTOList && !reconsiderDTOList.isEmpty())
				{
					for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reconsiderDTOList) {
						if(null != reconsiderRODRequestTableDTO.getIsRejectReconsidered() && reconsiderRODRequestTableDTO.getIsRejectReconsidered())
						{
							reimbursementDTO.setIsRejectReconsidered(true);
						}
						else
						{
							reimbursementDTO.setIsRejectReconsidered(false);
						}
					}
				}
				reimbursementDTO.setReconsiderRodRequestList(getReconsiderRODRequestForBillEntry(tableDTO.getRodKey()));
			}
			}
			Claim claimByKey = claimService.getClaimByClaimKey(reimbursementObjectByKey.getClaim().getKey());
			if(null != claimByKey)
			{
				if(null != reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId())
				{
					String  docReceivedFrom = reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue();
					reimbursementDTO.setPreviousAccountDetailsList(populatePreviousAccountDetails(claimByKey.getIntimation().getIntimationId(),docReceivedFrom));
				}
			}
			Long reimbursementKey = tableDTO.getRodKey();
			// Reimbursement previousLatestROD =
			// getPreviousLatestROD(claimByKey.getKey(), reimbursementObjectByKey);
			if (false) {
				// reimbursementKey = previousLatestROD.getKey();
				// reimbursementDTO = mapper.getReimbursementDTO(previousLatestROD);
				// reimbursementDTO.setIsPostHospitalization(true);
				// setReimbursmentTOPreauthDTO(mapper, claimByKey,
				// previousLatestROD, reimbursementDTO, true );
				// Hospitals hospitalById =
				// hospitalService.getHospitalById(claimByKey.getIntimation().getHospital());
				// ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails =
				// reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
				// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				// updateHospitalDetails.setHospitalState(hospitalById.getState());
				// updateHospitalDetails.setHospitalCode(hospitalById.getHospitalCode());
				// reimbursementDTO.setReconsiderationList(getReconsiderRODRequest(claimByKey));
				// reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
				// previousLatestROD.getKey());
				//
				// reimbursementDTO.setIsPreviousROD(true);
				// // reimbursementDTO.setPreviousROD(previousLatestROD);

			} else {
				reimbursementDTO
						.setReconsiderationList(getReconsiderRODRequest(claimByKey));

				reimbursementDTO = setReimbursmentTOPreauthDTO(mapper, claimByKey,
						reimbursementObjectByKey, reimbursementDTO, true, SHAConstants.BILLING);
				reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
						reimbursementObjectByKey.getKey());
			}

			UploadDocumentDTO reimbursementBenefitsValue = claimRequestBenefitsService
					.getReimbursementBenefitsValue(reimbursementObjectByKey
							.getKey());
			if (reimbursementBenefitsValue != null) {
				reimbursementDTO.getPreauthDataExtractionDetails()
						.setUploadDocumentDTO(reimbursementBenefitsValue);
			}
			
			Date policyFromDate = reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyFromDate();
			
			Date admissionDate = reimbursementDTO.getNewIntimationDTO().getAdmissionDate();
			
			Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
			if(diffDays != null && diffDays<90){
				reimbursementDTO.setIs64VBChequeStatusAlert(true);
			}
			if (null != reimbursementBenefitsValue
					&& reimbursementBenefitsValue.getPatientCareBenefitFlag() != null
					&& reimbursementBenefitsValue.getPatientCareBenefitFlag()
							.equalsIgnoreCase("PC")) {
				List<ReimbursementBenefitsDetails> patientCareTableByBenefitKey = reimbursementService
						.getPatientCareTableByBenefitKey(reimbursementBenefitsValue
								.getPatientBenefitKey());
				List<PatientCareDTO> patientCareDTOList = new ArrayList<PatientCareDTO>();
				if (patientCareTableByBenefitKey != null
						&& !patientCareTableByBenefitKey.isEmpty()) {
					for (ReimbursementBenefitsDetails patientCareDetails : patientCareTableByBenefitKey) {
						PatientCareDTO dto = new PatientCareDTO();
						dto.setEngagedFrom(patientCareDetails.getEngagedFrom());
						dto.setEngagedTo(patientCareDetails.getEngagedTo());
						dto.setKey(patientCareDetails.getKey());
						patientCareDTOList.add(dto);
					}
				}
				reimbursementDTO.getPreauthDataExtractionDetails()
						.getUploadDocumentDTO()
						.setPatientCareDTO(patientCareDTOList);
			}

			/**
			 * Added for amount claimed table enhancement---- starts
			 * */
			Double insuredSumInsured = 0d;
			if(null != claimByKey &&  null != claimByKey.getIntimation() && 
			null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
			null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
			&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
			{
				insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), reimbursementDTO
							.getPolicyDto().getKey(),reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), reimbursementDTO
								.getPolicyDto().getKey());
			}
			
			reimbursementDTO.getNewIntimationDTO().getPolicy().setTotalSumInsured(insuredSumInsured);

			Map<Integer, Object> detailsMap = new HashMap<Integer, Object>();
			

			if(reimbursementObjectByKey.getSectionCategory() != null){
				
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
								.getProduct().getKey(), insuredSumInsured,
								reimbursementDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
								reimbursementDTO.getNewIntimationDTO()
										.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),reimbursementObjectByKey.getSectionCategory(),"A");
			}else{
				
			Long sectionCategory = 0l;
			if(reimbursementDTO.getPolicyDto()
								.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY) || reimbursementDTO.getPolicyDto()
								.getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY) || reimbursementDTO.getPolicyDto()
								.getProduct().getKey().equals(ReferenceTable.DIABETES_FLOATER_POLICY))
			{
				sectionCategory = 1l;
			}
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
								.getProduct().getKey(), insuredSumInsured,
								reimbursementDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
								reimbursementDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),sectionCategory,"0");
								/**
								 * The below code is commented as per sathish sir suggestion for ticket 2355 (production tracker).
								 * By default, the section category would be 1 for three products 
								 * MED-PRD-033 , MED-PRD-032 , MED-PRD-030
								 * 
								 * */
								
				
										//.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),0l,"0");
			}
			
			List<Double> copayValue = dbCalculationService.getProductCoPay(reimbursementDTO
					.getClaimDTO().getNewIntimationDto().getPolicy().getProduct()
					.getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
					.getInsuredPatient().getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
					.getInsuredPatient().getInsuredId(),reimbursementDTO.getNewIntimationDTO());
			reimbursementDTO.setProductCopay(copayValue);

			/**
			 * Added for amount claimed table enhancement---- ends
			 * */

			List<UploadDocumentDTO> rodSummaryDetails = rodService
					.getRODSummaryDetails(reimbursementObjectByKey.getKey());
			
			/**
			 * Fix for implemeting claims dms in bill entry screen.. -- starts
			 * **/
			if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
			{
				for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
					if(null != claimByKey)
					{
						uploadDocumentDTO.setClaimNo(claimByKey.getClaimId());
						uploadDocumentDTO.setDmsDocumentDTOList(getClaimsDMSList(claimByKey.getIntimation().getIntimationId()));
					}
				}
			}
			
			reimbursementDTO = getProrataFlagFromProduct(reimbursementDTO);
			
			reimbursementDTO.setProrataDeductionFlag(reimbursementObjectByKey.getProrataDeductionFlag());
			/**
			 * product based variable is added to enable or disable the component in page level.
			 * This would be static. -- starts
			 * */
			reimbursementDTO.setPackageAvailableFlag(reimbursementObjectByKey.getPackageAvailableFlag());


			List<Long> documentSummaryKeys = new ArrayList<Long>();
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				
				documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
				uploadDocumentDTO.setStatus(true);
				List<RODBillDetails> billEntryDetails = rodService
						.getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
				List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
				if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
					for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
						/*
						 * <<<<<<< HEAD
						 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
						 * uploadDocumentDTO)); =======
						 */
						dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,
								uploadDocumentDTO));
						// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
					}
				}
				uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
						.get(8));
				uploadDocumentDTO
						.setProductBasedICURent((Double) detailsMap.get(9));
				/*uploadDocumentDTO.setProductBasedAmbulanceAmt((Double) detailsMap
						.get(15));*/
				uploadDocumentDTO.setProductBasedAmbulanceAmt(getProductBasedAmbulanceAmt(tableDTO.getRodKey()));
				uploadDocumentDTO.setStatus(false);
				uploadDocumentDTO.setBillEntryDetailList(dtoList);
				uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
				uploadDocumentDTO.setRodKey(reimbursementObjectByKey.getKey());

				uploadDocumentDTO.setStrUserName(tableDTO.getUsername());

				if (uploadDocumentDTO != null) {
					if ((ReferenceTable.BENEFITS_PATIENT_CARE)
							.equalsIgnoreCase(uploadDocumentDTO
									.getPatientCareBenefitFlag())) {
						List<PatientCareDTO> patientCareList = claimRequestBenefitsService
								.getPatientCareDetails(uploadDocumentDTO
										.getPatientBenefitKey());
						if (null != patientCareList && !patientCareList.isEmpty()) {
							uploadDocumentDTO.setPatientCareDTO(patientCareList);
						}
						// rodDTO.getAddOnBenefitsDTO().setAdmittedNoOfDays(uploadDocumentDTO.getPatientCareNoofDays());
					}
				}
			}
			List<UploadDocumentDTO> rodBillSummaryDetails = rodService
					.getRODBillSummaryDetails(reimbursementObjectByKey.getKey(), mapper);
			
			if(rodBillSummaryDetails != null){
				for (UploadDocumentDTO uploadDocumentDTO2 : rodBillSummaryDetails) {
					uploadDocumentDTO2.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
				}
			}
			
			reimbursementDTO.getUploadDocDTO().setBillingWorkSheetUploadDocumentList(rodBillSummaryDetails);

			Double totalBilledAmount = reimbursementService
					.getTotalBilledAmount(documentSummaryKeys);

			reimbursementDTO.setAmountConsidered(totalBilledAmount != null ? String
					.valueOf(totalBilledAmount.intValue()) : "0");
			reimbursementDTO
					.setInitialAmountConsidered(totalBilledAmount != null ? String
							.valueOf(totalBilledAmount.intValue()) : "0");
			reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTask());
			reimbursementDTO.setKey(reimbursementObjectByKey.getKey());
			reimbursementDTO.setRodNumber(reimbursementObjectByKey.getRodNumber());

			setCategoryFlag(reimbursementObjectByKey, reimbursementDTO);
			
			if (!reimbursementDTO.getHospitalizaionFlag()) {
				reimbursementDTO = setHospitalizationAmount(reimbursementDTO);
			}

			List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
					.search(reimbursementObjectByKey.getClaim().getKey(),false);

			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

				previousPreAuthTableDTO.setRequestedAmt(preauthService
						.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
								previousPreAuthTableDTO.getClaimKey()));
				newList.add(previousPreAuthTableDTO);

			}
			
			

			reimbursementDTO.setPreviousPreauthTableDTO(newList);

		//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());
			reimbursementDTO.setStrUserName(tableDTO.getUsername());
			reimbursementDTO.setStrPassword(tableDTO.getPassword());
	        reimbursementDTO.setTaskNumber(tableDTO.getTaskNumber());
			
			
			reimbursementDTO.getPreauthMedicalDecisionDetails().setBillingRemarks(
					"");
			
			
			List<AddOnBenefitsDTO> populateAddOnBenefitsTableValues = claimRequestBenefitsService
					.populateAddOnBenefitsTableValues(
							reimbursementDTO.getNewIntimationDTO(),
							reimbursementObjectByKey.getClaim().getKey(),
							reimbursementObjectByKey.getKey(),
							reimbursementDTO.getProductCopay(),null);
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setAddOnBenefitsDTOList(populateAddOnBenefitsTableValues);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 8l, 9l, false,false);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 10l, 11l, true,false);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 85l, 84l, false,true);
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setDocAckknowledgement(
							reimbursementObjectByKey.getDocAcknowLedgement());

			if (reimbursementObjectByKey.getDocAcknowLedgement()
					.getHospitalizationRepeatFlag() != null
					&& reimbursementObjectByKey.getDocAcknowLedgement()
							.getHospitalizationRepeatFlag().toLowerCase()
							.equalsIgnoreCase("y")) {
				reimbursementDTO.setIsHospitalizationRepeat(true);
			}
			
			/*if (reimbursementObjectByKey.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {*/
				
				/*Long finacialMedicalApproverKey = pareimbursementService.getLatestMedicalApproverForRebilling(reimbursementObjectByKey.getKey());
				if(finacialMedicalApproverKey!=null){*/
				MedicalApprover financialMedicalApprover = pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(), SHAConstants.PA_BILLING_REFINANCE);
				if (financialMedicalApprover != null) {
					reimbursementDTO.setPreviousRemarks(financialMedicalApprover
							.getApproverReply());
					reimbursementDTO.setPreviousReasonForReferring(financialMedicalApprover
							.getReasonForReferring());
					reimbursementDTO.getPreauthDataExtractionDetails().setFaApproverRemarks(financialMedicalApprover
							.getReferringRemarks());
					reimbursementDTO.getPreauthDataExtractionDetails().setFaReasonForRefferingToBilling(financialMedicalApprover
							.getReasonForReferring());					
					//reimbursementDTO.setIsReferToBilling(true);
				//}
				
				
			}
				//}
			
		/*	if (reimbursementObjectByKey.getStatus().getKey()
					.equals(ReferenceTable.CLAIM_APPROVAL_REFER_TO_BILLING)) {
				*/
				//Long billingMedicalApprovalKey = pareimbursementService.getLatestMedicalApproverForReClaim(reimbursementObjectByKey.getKey());
				//if(billingMedicalApprovalKey!=null){
				MedicalApprover  medicalApprover = pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(), SHAConstants.PA_CLAIM_APPROVAL_REBILLING);
				if (medicalApprover != null) {
					reimbursementDTO.getPreauthDataExtractionDetails().setCaReasonForRefferingToBilling(medicalApprover.getReasonForReferring());
					reimbursementDTO.getPreauthDataExtractionDetails().setCaApproverRemarks(medicalApprover.getReferringRemarks());
					reimbursementDTO.setPreviousReasonForReferring(medicalApprover
							.getReasonForReferring());
					reimbursementDTO.setIsReferToBilling(true);
				}			
				
				
				MedicalApprover  billingMedicalApprover = pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(), SHAConstants.PA_CLAIM_REQUEST_REBILLING);
				if (billingMedicalApprover != null) {
					reimbursementDTO.getPreauthDataExtractionDetails().setMaReasonForRefferingToBilling(billingMedicalApprover.getReasonForReferring());
					reimbursementDTO.getPreauthDataExtractionDetails().setMaApproverRemarks(billingMedicalApprover.getApproverReply());
					reimbursementDTO.setPreviousReasonForReferring(billingMedicalApprover
							.getApproverReply());
					reimbursementDTO.setIsReferToBilling(true);
				}
				//}
				//reimbursementDTO.setIsReferToBilling(true);
			//}
			
			if (claimByKey.getClaimType() != null
					&& claimByKey.getClaimType().getKey()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {

//				PreMedicalMapper premedicalMapper = new PreMedicalMapper();
				Preauth previousPreauth = getPreviousPreauth(claimByKey.getKey());
				
				if(previousPreauth != null) {
					reimbursementDTO.setPreauthKey(previousPreauth.getKey());
				}
			}
			
			if(claimByKey.getStatus() != null) {
				if(claimByKey.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) ||  claimByKey.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)) {
					reimbursementDTO.setIsWithDrawn(true);
				}
			}
			
			loadRRCRequestValues(reimbursementDTO,insuredSumInsured,SHAConstants.BILLING);
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance().get64VBStatus(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber(), reimbursementDTO.getNewIntimationDTO().getIntimationId());
				if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
					reimbursementDTO.setIsDishonoured(true);
				}  else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
					reimbursementDTO.setIsPending(true);
				}
			}
			reimbursementDTO.setSittingsAmount(dbCalculationService.getDialysisAmount(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(), insuredSumInsured.longValue()).intValue());
			Map<String, String> popupMessages = dbCalculationService.getPOPUPMessages(reimbursementDTO.getPolicyKey(), reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey(),reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
			reimbursementDTO.setPopupMap(popupMessages);
			
			
			if(reimbursementDTO.getNewIntimationDTO().getHospitalDto() != null && reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag() != null
					&& reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag().equalsIgnoreCase("Y")){
				if(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks() != null){
					Map<String, String> suspiciousMap = SHAUtils.getSuspiciousMap(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks());
					reimbursementDTO.setSuspiciousPopupMap(suspiciousMap);
				}
			}
			// If hospitalization is not processed then remaining ROD should be rejected.
			Reimbursement hospROD = reimbursementService.getHospitalizationROD(reimbursementObjectByKey.getClaim().getKey());
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
				if(hospROD == null) {
					reimbursementDTO.setIsHospitalizationRejected(true);
				}
			}
			
			Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
					.getIntimation().getHospital());
			
			paymentDetailsForfinancial(reimbursementObjectByKey, reimbursementDTO,
					hospitalById);
			
			setReimbursementValues(reimbursementObjectByKey, reimbursementDTO);
			
	       Reimbursement hospitalizationOrPartialROD = reimbursementService.getHospitalizationOrPartialROD(reimbursementObjectByKey.getClaim().getKey());
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				
				
				Integer seqNumber = 0;
				
				if(reimbursementObjectByKey.getRodNumber() != null){
					String[] split = reimbursementObjectByKey.getRodNumber().split("/");
					seqNumber = Integer.valueOf(split[split.length - 1]);
				}
				
				if(hospitalizationOrPartialROD == null && ! seqNumber.equals(1)) {
//					reimbursementDTO.setIsDishonoured(true);
					reimbursementDTO.setIsHospitalizationRejected(true);
				}
			}
			
			Map<String, Integer> productBenefitFlag = dbCalculationService.getProductBenefitFlag(reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey());
			
			if(productBenefitFlag.get(SHAConstants.PRE_HOSP_FLAG).equals(0)) {
				reimbursementDTO.setIsPreHospApplicable(false);
			}
			
			if(productBenefitFlag.get(SHAConstants.POST_HOSP_FLAG).equals(0)) {
				reimbursementDTO.setIsPostHospApplicable(false);
			}
			
			if(productBenefitFlag.get(SHAConstants.LUMP_SUM_FLAG).equals(1)) {
				reimbursementDTO.setIsLumpsumApplicable(true);
			}
			
			if(productBenefitFlag.get(SHAConstants.HOSPITALCASH_FLAG).equals(1)) {
				reimbursementDTO.setIsHospitalCashApplicable(true);
			}
			
			if(productBenefitFlag.get(SHAConstants.PATIENTCARE_FLAG).equals(1)) {
				reimbursementDTO.setIsPatientCareApplicable(true);
			}
			
			
			MastersValue networkHospitalType = masterService.getMaster(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getNetworkHospitalTypeId());
			reimbursementDTO.setNetworkHospitalType(networkHospitalType.toString());
			
			reimbursementDTO = checkFinalEnhancement(reimbursementDTO);

			
			try {
				if((reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest() != null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest().equalsIgnoreCase("Y"))
						|| (reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))) {
					if(reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag() ==  null || reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag().equalsIgnoreCase("N")) {
						reimbursementDTO.setIsReconsiderationRequest(true);
						if(reimbursementService.isClaimPaymentAvailable(reimbursementObjectByKey.getRodNumber())) {
							Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
							reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
						} else {
							/**
							 * For reject reconsider, cancel rod button was enabled. But for this ticket GALAXYMAIN-6222	
							 * which was raised in PA, now cancel rod button is diabled for rejection case to.
							 * Hence below code was commented.
							 * */
							//reimbursementDTO.setIsReconsiderationRequest(false);
						}
//						Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
//						reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
					}
					
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				reimbursementDTO.setIsCashlessType(true);
			}
			if(reimbursementDTO.getIsCashlessType() && reimbursementDTO.getHospitalizaionFlag() && (reimbursementDTO.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY))){
				reimbursementDTO.setIsDirectToBilling(true);
				Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursementDTO.getClaimDTO().getKey());
				reimbursementDTO.getClaimDTO().setLatestPreauthKey(latestPreauth.getKey());
			}
			reimbursementDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(reimbursementDTO.getNewIntimationDTO().getAdmissionDate(), reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));
			reimbursementDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey()));
			reimbursementDTO.setIsPEDInitiatedForBtn(reimbursementDTO.getIsPEDInitiated());
			
			List<InsuredPedDetails> pedByInsured = policyService.getPEDByInsured(reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			reimbursementDTO.setInsuredPedDetails(pedByInsured);
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				if(reimbursementDTO.getHospitalizaionFlag()) {
					Reimbursement partialHospitalizationRODWithSettled = getPartialHospitalizationRODWithSettled(claimByKey.getKey());
					ReimbursementCalCulationDetails hosptialization = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursementDTO.getKey(), ReferenceTable.HOSPITALIZATION);
					if(hosptialization != null) {
						reimbursementDTO.setBillingApprovedAmount(hosptialization.getPayableToHospAftTDS() != null ? hosptialization.getPayableToHospAftTDS().doubleValue() : 0d);
						reimbursementDTO.setPayableToHospAmt(hosptialization.getPayableToHospital() != null ? hosptialization.getPayableToHospital().doubleValue() : 0d);
						reimbursementDTO.setHospDiscountAmount(hosptialization.getHospitalDiscount() != null ? hosptialization.getHospitalDiscount().doubleValue() : 0d);
					}
					if(partialHospitalizationRODWithSettled != null) {
						ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(partialHospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
						reimbursementDTO.setPayableToInsAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium().doubleValue() : 0d);
						reimbursementDTO.setFAApprovedAmount(partialHospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? partialHospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
					}
				} else if(reimbursementDTO.getPartialHospitalizaionFlag()) {
					Reimbursement hospitalizationRODWithSettled = getHospitalizationRODWithSettled(claimByKey.getKey());
					if(hospitalizationRODWithSettled != null) {
						ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(hospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
						reimbursementDTO.setPayableToHospAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital().doubleValue() : 0d);
						reimbursementDTO.setHospDiscountAmount(reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d);
						Double amount = (reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d) + (hospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? hospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
						reimbursementDTO.setFAApprovedAmount(amount);
					}
				}
			}
			
			Boolean queryReceivedStatusRod = reimbursementQuerySerice.isQueryReceivedStatusRod(reimbursementObjectByKey.getKey());
			 
			 if(queryReceivedStatusRod){
				 reimbursementDTO.setIsQueryReceived(true);
			 }
			 
			 
				List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = acknowledgementDocumentsReceivedService.getReimbursementCalculationDetails(reimbursementObjectByKey.getKey());
				if(reimbursementCalculationDetails != null && !reimbursementCalculationDetails.isEmpty()) {
					for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
						if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
								reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.HOSPITALIZATION)){
							
							HopitalizationCalulationDetailsDTO hospitalizationCalcDTO = getHospitalizationCalculationDTO(reimbursementCalCulationDetails2,reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
							if(reimbursementDTO.getDocumentReceivedFromId() != null && reimbursementDTO.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
						           hospitalizationCalcDTO.setBalanceToBePaid(0);
							}
							reimbursementDTO.setHospitalizationCalculationDTO(hospitalizationCalcDTO);
							
						}
						else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
								reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)){
							PreHopitalizationDetailsDTO preHospitalizationCalcDTO = getPreHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
							reimbursementDTO.setPreHospitalizationCalculationDTO(preHospitalizationCalcDTO);
							
						}else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
								reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
							PostHopitalizationDetailsDTO postHospitalizationCalcDTO = getPostHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
							reimbursementDTO.setPostHospitalizationCalculationDTO(postHospitalizationCalcDTO);
						}
					}
				}
			
				reimbursementDTO.setAmountConsidered(reimbursementDTO.getAmountConsidedAfterCoPay() != null ? String
						.valueOf(reimbursementDTO.getAmountConsidedAfterCoPay().intValue()) : "0");
			 
//			 reimbursementService.setBPMForClaimBilling(reimbursementDTO, false, "MEDICAL", reimbursementObjectByKey);
				Double totalClaimedAmount = reimbursementService.getTotalClaimedAmount(reimbursementObjectByKey);
				 reimbursementDTO.setRodTotalClaimedAmount(totalClaimedAmount);
				 
				 if(reimbursementDTO.getAmountConsidedAfterCoPay() != null && totalBilledAmount != null && !reimbursementDTO.getAmountConsidedAfterCoPay().equals(totalBilledAmount)) {
					 reimbursementDTO.setAmountConsidered(String.valueOf(totalBilledAmount.longValue()));
				 }

				// SHAUtils.setDefaultCopayValue(reimbursementDTO);

				 if(reimbursementDTO.getIsReconsiderationRequest() != null && reimbursementDTO.getIsReconsiderationRequest()){
					 reimbursementDTO.setIsReverseAllocation(false);
				 }

				 if(reimbursementObjectByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
						 && reimbursementObjectByKey.getDocAcknowLedgement() != null && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
						 && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
				 
						 if(hospitalById != null){
								Boolean hospitalDiscount = SHAUtils.isHospitalDiscount(hospitalById);
								reimbursementDTO.setIsHospitalDiscountApplicable(hospitalDiscount);
						 }
				 }
				 
//				 if(reimbursementDTO.getNewIntimationDTO() != null && reimbursementDTO.getNewIntimationDTO().getInsuredPatient() != null &&
//						 reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey() != null){
//						Long claimCount = preauthService.getClaimCount(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
//						if(claimCount != null){
//							reimbursementDTO.setClaimCount(claimCount);
//						}
//					}
				 List<TableBenefitsDTO> paBenefitsListByRodKey = paSearchProcessClaimBillingService.getPABenefitsListByRodKey(reimbursementObjectByKey.getKey());
				 
				 reimbursementDTO.getPreauthDataExtractionDetails().setBenefitDTOList(paBenefitsListByRodKey);
				 
				MastersValue benefitsId = reimbursementObjectByKey.getBenefitsId();
				Insured insuredKey = reimbursementObjectByKey.getInsuredKey();
				 if(benefitsId!=null && insuredKey!=null){
					 
					 BeanItemContainer<SelectValue> coverContainer = null;
					 
					 if(null != insuredKey && null != insuredKey.getPolicy() && null != insuredKey.getPolicy().getProduct() &&
							 null != insuredKey.getPolicy().getProduct().getKey() && 
							 !(ReferenceTable.getGPAProducts().containsKey(insuredKey.getPolicy().getProduct().getKey()))){	
						 
						 coverContainer = dbCalculationService.getBenefitCoverValueContainer(insuredKey.getKey(), benefitsId.getKey());
					 }
					 else
					 {
						 coverContainer = dbCalculationService.getGPABenefitCoverValueContainer(insuredKey.getKey(), benefitsId.getKey());
					 }
					 PreauthDataExtaractionDTO preauthDataExtractionDetails = reimbursementDTO.getPreauthDataExtractionDetails();
					 preauthDataExtractionDetails.setCoverListContainer(coverContainer.getItemIds());
					 
					 reimbursementDTO.getPreauthDataExtractionDetails().setBenefitsValue(benefitsId.getValue());
					
				 }
				 
				 Double alreadyPaidAmount = paSearchProcessClaimBillingService.getAlreadyPaidAmount(reimbursementObjectByKey.getRodNumber());
				 reimbursementDTO.getPreauthDataExtractionDetails().setAlreadyPaidAmt(alreadyPaidAmount);
				 
				 List<OptionalCoversDTO> paOptionalCoverListByRodKey = paSearchProcessClaimBillingService.getPAOptionalCoverListByRodKey(reimbursementObjectByKey);

				 reimbursementDTO.getPreauthDataExtractionDetails().setOptionalCoversTableListBilling(paOptionalCoverListByRodKey);
				 
				 Product product = reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct();
				 
				 BeanItemContainer<SelectValue> bebefitAdditionalCovers = null;
				 if(null != product && null != product.getKey() && !(ReferenceTable.getGPAProducts().containsKey(product.getKey())))
					{
					 bebefitAdditionalCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.OPTIONAL_COVER, product.getKey(),insuredKey.getKey());
					}
				 else
				 {
					 bebefitAdditionalCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.GPA_OPTIONAL_COVER, product.getKey(),insuredKey.getKey());
				 }
				 
				 PreauthDataExtaractionDTO preauthDataExtractionDetails = reimbursementDTO.getPreauthDataExtractionDetails();
				 preauthDataExtractionDetails.setOptionalCoverListContainer(bebefitAdditionalCovers.getItemIds());			
			
				 List<AddOnCoverOnLoadDTO> coverNameList= new ArrayList<AddOnCoverOnLoadDTO>();
				 List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> paAddOnCoverListByRodKey = paSearchProcessClaimBillingService.getAddOnCoverListByRodKey(reimbursementObjectByKey.getKey(),product.getKey(),coverNameList);
				
				 reimbursementDTO.getPreauthDataExtractionDetails().setAddOnCoversTableListBilling(paAddOnCoverListByRodKey);
				 
				if(!coverNameList.isEmpty()){
					reimbursementDTO.getPreauthDataExtractionDetails().setAddOnCoverNameList(coverNameList);
				}
				 
				 //reimbursementDTO.getPreauthDataExtractionDetails().setAddOnAllCoversTableListBilling(paAddOnCoverListByRodKey);
				 BeanItemContainer<SelectValue> addOnCovers = null;
				if(null != product && null != product.getKey() && !(ReferenceTable.getGPAProducts().containsKey(product.getKey())))
				{
					addOnCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.ADDITIONAL_COVER, product.getKey(),insuredKey.getKey());
				}
				else
				{
					addOnCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.GPA_ADDITIONAL_COVER, product.getKey(),insuredKey.getKey());
				}
				 preauthDataExtractionDetails.setAddOnCoverListContainer(addOnCovers.getItemIds());
				 
				 //List<TableBenefitsDTO> benefitDTOList = reimbursementDTO.getPreauthDataExtractionDetails().getBenefitDTOList();
				 //List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> addOnCoversTableListBilling = reimbursementDTO.getPreauthDataExtractionDetails().getAddOnCoversTableListBilling();
				 //List<OptionalCoversDTO> optionalCoversTableListBilling = reimbursementDTO.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling();
				 
				 //List<PABillingConsolidatedDTO> paBillingConsolidatedDTOs = paSearchProcessClaimBillingService.getConsolidatedValue(benefitDTOList,addOnCoversTableListBilling,optionalCoversTableListBilling);
				 //reimbursementDTO.getPreauthDataExtractionDetails().setBillingConsolidatedDTOList(paBillingConsolidatedDTOs);
				 
				 BeanItemContainer<SelectValue> containerBenefitsValue = masterService.getSelectValueContainerForBenefits(ReferenceTable.MASTER_TYPE_CODE_BENEFITS,reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey());
				
				 reimbursementDTO.getPreauthDataExtractionDetails().setBenefitsValueContainer(containerBenefitsValue.getItemIds());
				 
				 
				 Double balanceSI = 0d;

					if(null != product && null != product.getKey() && !(ReferenceTable.getGPAProducts().containsKey(product.getKey())))
					{
					
						balanceSI = dbCalculationService.getPABalanceSI(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey(), reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getKey(),null != reimbursementObjectByKey.getBenefitsId() ? reimbursementObjectByKey.getBenefitsId().getKey() : 0l);
					}
					else
					{
						balanceSI = dbCalculationService.getGPAAvailableSI(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey(), reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getKey(),null != reimbursementObjectByKey.getBenefitsId() ? reimbursementObjectByKey.getBenefitsId().getKey() : 0l);
					}
				 /*if(reimbursementObjectByKey.getDocAcknowLedgement()!=null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest()!=null 
						 && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest())){
					 	balanceSI = balanceSI + reimbursementObjectByKey.getFinancialApprovedAmount();
					 	
					 	
				 }*/

					 if((reimbursementObjectByKey.getDocAcknowLedgement()!=null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest()!=null 
							 && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest()))
							 || (reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))){
						 
						 ClaimPayment claimPayment = paReimbursementService.getClaimPayment(reimbursementObjectByKey.getRodNumber());
						 if(null != claimPayment && claimPayment.getPaBenefitApproveAmount() != null){
						 	balanceSI = balanceSI + claimPayment.getPaBenefitApproveAmount();
						 }
					 }
					 reimbursementDTO.getPreauthDataExtractionDetails().setAvailableSI(null != balanceSI ? String.valueOf(balanceSI) : null);
				 
				 reimbursementDTO.getPreauthDataExtractionDetails().setDeathDate(reimbursementObjectByKey.getClaim().getIncidenceDate());
				 reimbursementDTO.getPreauthDataExtractionDetails().setAdmissionDate(reimbursementObjectByKey.getClaim().getDataOfAdmission());
				 reimbursementDTO.getPreauthDataExtractionDetails().setDischargeDateForPa(reimbursementObjectByKey.getClaim().getDataOfDischarge());
				 reimbursementDTO.getPreauthDataExtractionDetails().setDateOfAccident(reimbursementObjectByKey.getClaim().getAccidentDate());
				 reimbursementDTO.getPreauthDataExtractionDetails().setDateOfDeath(reimbursementObjectByKey.getClaim().getDeathDate());
				 reimbursementDTO.getPreauthDataExtractionDetails().setDateOfDisablement(reimbursementObjectByKey.getClaim().getDisablementDate());
				 Boolean incidenceValue = null;
				 if(("A").equalsIgnoreCase(reimbursementObjectByKey.getClaim().getIncidenceFlag()))
					 incidenceValue = true;
				 else
					 incidenceValue = false;
				 reimbursementDTO.getPreauthDataExtractionDetails().setAccidentOrDeath(incidenceValue);
				 
				 if(null != reimbursementDTO.getRodNumber()){
						ClaimPayment claimPayment = acknowledgementDocumentsReceivedService.getClaimPaymentDetails(reimbursementDTO.getRodNumber());
						if(null != claimPayment && null != claimPayment.getStatusId() && claimPayment.getStatusId().getKey().equals(ReferenceTable.PAYMENT_SETTLED)){
							reimbursementDTO.setIsPaymentSettled(true);
						}
					}
				 
					
					reimbursementDTO.setDbOutArray(tableDTO.getDbOutArray());

					LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
					legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
					reimbursementDTO.setLegalHeirDto(legalHeirDTO);
					
					List<NomineeDetailsDto> nomineeDtoList = intimationService.getNomineeDetailsListByTransactionKey(tableDTO.getRodKey());
					if( nomineeDtoList != null && !nomineeDtoList.isEmpty()){
						reimbursementDTO.getNewIntimationDTO().setNomineeList(nomineeDtoList);
					}
					if(reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null 
							&& reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()) {
						List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(tableDTO.getRodKey());
						if(legalHeirList != null && !legalHeirList.isEmpty()) {
							List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
							LegalHeirDTO legalHeirDto;
							for (LegalHeir legalHeir : legalHeirList) {
								 legalHeirDto = new LegalHeirDTO(legalHeir);
								 legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
								 legalHeirDTOList.add(legalHeirDto);
							}
							reimbursementDTO.setLegalHeirDTOList(legalHeirDTOList);
						}
					}	
					/*for bancs*/
				if(reimbursementObjectByKey.getCatastrophicLoss() != null) {
					
					SelectValue value = masterService.getCatastropheData(reimbursementObjectByKey.getCatastrophicLoss());
					reimbursementDTO.getPreauthDataExtractionDetails().setCatastrophicLoss(value);
				}
				if(reimbursementObjectByKey.getNatureOfLoss() != null) {
					
					SelectValue value = masterService.getMasterValueForNatureCause(reimbursementObjectByKey.getNatureOfLoss());
					reimbursementDTO.getPreauthDataExtractionDetails().setNatureOfLoss(value);
				}
				if(reimbursementObjectByKey.getCauseOfLoss() != null) {
					SelectValue value = masterService.getMasterValueForNatureCause(reimbursementObjectByKey.getCauseOfLoss());
					reimbursementDTO.getPreauthDataExtractionDetails().setCauseOfLoss(value);
				}
				if(reimbursementObjectByKey.getClaim().getLegalClaim() !=null
						 && reimbursementObjectByKey.getClaim().getLegalClaim().equals("Y")){
					 LegalTaxDeduction taxDeduction = reimbursementService.getTaxDeductionFromRODKey(reimbursementObjectByKey.getKey());
					 if(taxDeduction !=null){
						 LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
						 LegalBillingDTO billingDTO = taxDeductionMapper.getDTOFromTaxDeduction(taxDeduction);
						 billingDTO = reimbursementService.calculateLegalSettlment(billingDTO);
						 reimbursementDTO.setLegalBillingDTO(billingDTO);
					 }
				 }
					
				reimbursementDTO.setScreenName(SHAConstants.PA_BILLING_NON_HOSP);
				
				Map<String,Object> icrAgent  = dbCalculationService.getAgentAndBranchName(reimbursementDTO.getNewIntimationDTO().getIntimationId()); 
				 if(icrAgent != null && !icrAgent.isEmpty()) {
						String agentScorePoint = icrAgent.get(SHAConstants.ICR_AGENT_POINT).toString();
						reimbursementDTO.setIcrAgentValue(agentScorePoint);
						String smScorepoint = icrAgent.get(SHAConstants.SM_AGENT_POINT).toString();
						reimbursementDTO.setSmAgentValue(smScorepoint);
				 }
				 
				 Policy policy = reimbursementDTO.getNewIntimationDTO().getPolicy();
				 // added for PA installment payment process in policy level and product
				 // level check
				 Map<String, String> getPolicyInstallment = dbCalculationService
						 .getInstallmentPaymentFlag(policy.getPolicyNumber(), policy.getProduct()
								 .getKey());
				 if (getPolicyInstallment != null && !getPolicyInstallment.isEmpty()) {
					 reimbursementDTO.setPolicyInstalmentFlag(getPolicyInstallment
							 .get(SHAConstants.FLAG) != null ? getPolicyInstallment
									 .get(SHAConstants.FLAG) : "N");
					 reimbursementDTO
					 .setPolicyInstalmentMsg(getPolicyInstallment
							 .get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallment
									 .get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
				 }
				 if (reimbursementDTO.getPolicyInstalmentFlag() != null
						 && reimbursementDTO.getPolicyInstalmentFlag().equals(
								 SHAConstants.YES_FLAG)) {
					 Integer policyInstallmentAmount = PremiaService.getInstance()
							 .getPolicyInstallmentAmount(policy
									 .getPolicyNumber());
					 reimbursementDTO.setPolicyInstalmentPremiumAmt(policyInstallmentAmount
							 .doubleValue());

					 String policyDueDate = PremiaService.getInstance()
							 .getPolicyInstallmentDetails(policy
									 .getPolicyNumber());
					 //code added if we recevied instalment due amount as zero after pending amount adjusted to premia/bancs  need to set due date as admission date by noufel
					 if((reimbursementDTO.getPolicyInstalmentPremiumAmt() != null && reimbursementDTO.getPolicyInstalmentPremiumAmt() == 0d) && 
							 (policyDueDate == null || policyDueDate.isEmpty())) {

						 reimbursementDTO.setPolicyInstalmentDueDate(reimbursementDTO.getPreauthDataExtractionDetails().getAdmissionDate());	
					 }
					 else{
						 if (reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
							 reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.formatTimeFromString(policyDueDate.toString()));	
						 }
						 else{
							 reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.dateformatFromString(policyDueDate.toString()));
						 }
					 }
				 }
				 
				 view.setPAProcessClaimBilling(PABillingWizard.class, reimbursementDTO);
		}
		}
		
		protected void showPAProcessFinancialNonHospWizard(
				SearchProcessClaimFinancialsTableDTO tableDTO) {

			Reimbursement reimbursementObjectByKey = rodService
					.getReimbursementObjectByKey(tableDTO.getRodKey());
			
			Claim objClaim =reimbursementObjectByKey.getClaim();

			
			
			
			
			Boolean isValidClaim = true;
			if (null != objClaim) {
				if ((SHAConstants.YES_FLAG).equalsIgnoreCase(objClaim
						.getLegalFlag())) {
					isValidClaim = false;
					view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
				}
			}
			
			if(isValidClaim) {
			
			ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
			PreauthDTO reimbursementDTO = mapper
					.getReimbursementDTO(reimbursementObjectByKey);
			
			if(("Y").equalsIgnoreCase(reimbursementDTO.getPreauthDataExtractionDetails().getReconsiderationFlag()))
			{
				List<ReconsiderRODRequestTableDTO> reconsiderDTOList = getReconsiderRODRequestForBillEntry(tableDTO.getRodKey());
				if(null != reconsiderDTOList && !reconsiderDTOList.isEmpty())
				{
					for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reconsiderDTOList) {
						if(null != reconsiderRODRequestTableDTO.getIsRejectReconsidered() && reconsiderRODRequestTableDTO.getIsRejectReconsidered())
						{
							reimbursementDTO.setIsRejectReconsidered(true);
						}
						else
						{
							reimbursementDTO.setIsRejectReconsidered(false);
						}
					}
				}
				reimbursementDTO.setReconsiderRodRequestList(getReconsiderRODRequestForBillEntry(tableDTO.getRodKey()));
			}
			
			reimbursementDTO.getPreauthDataExtractionDetails().setDischargeDateForPa(reimbursementObjectByKey.getDateOfDischarge());
			
			reimbursementDTO.getPreauthDataExtractionDetails().setBillingInternalRemarks(reimbursementObjectByKey.getBillingInternalRemarks());
			reimbursementDTO.getPreauthDataExtractionDetails().setFaInternalRemarks(reimbursementObjectByKey.getFaInternalRemarks());

			Claim claimByKey = claimService.getClaimByClaimKey(reimbursementObjectByKey.getClaim().getKey());
			if(null != claimByKey)
			{
				if(null != reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId())
				{
					String  docReceivedFrom = reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue();
					reimbursementDTO.setPreviousAccountDetailsList(populatePreviousAccountDetails(claimByKey.getIntimation().getIntimationId(),docReceivedFrom));
				}
			}
			Long reimbursementKey = tableDTO.getRodKey();
			// Reimbursement previousLatestROD =
			// getPreviousLatestROD(claimByKey.getKey(), reimbursementObjectByKey);
			if (false) {
				// reimbursementKey = previousLatestROD.getKey();
				// reimbursementDTO = mapper.getReimbursementDTO(previousLatestROD);
				// reimbursementDTO.setIsPostHospitalization(true);
				// setReimbursmentTOPreauthDTO(mapper, claimByKey,
				// previousLatestROD, reimbursementDTO, true );
				// Hospitals hospitalById =
				// hospitalService.getHospitalById(claimByKey.getIntimation().getHospital());
				// ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails =
				// reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
				// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				// updateHospitalDetails.setHospitalState(hospitalById.getState());
				// updateHospitalDetails.setHospitalCode(hospitalById.getHospitalCode());
				// reimbursementDTO.setReconsiderationList(getReconsiderRODRequest(claimByKey));
				// reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
				// previousLatestROD.getKey());
				//
				// reimbursementDTO.setIsPreviousROD(true);
				// // reimbursementDTO.setPreviousROD(previousLatestROD);

			} else {
				reimbursementDTO
						.setReconsiderationList(getReconsiderRODRequest(claimByKey));

				reimbursementDTO = setReimbursmentTOPreauthDTO(mapper, claimByKey,
						reimbursementObjectByKey, reimbursementDTO, true, SHAConstants.BILLING);
				reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
						reimbursementObjectByKey.getKey());
			}

			UploadDocumentDTO reimbursementBenefitsValue = claimRequestBenefitsService
					.getReimbursementBenefitsValue(reimbursementObjectByKey
							.getKey());
			if (reimbursementBenefitsValue != null) {
				reimbursementDTO.getPreauthDataExtractionDetails()
						.setUploadDocumentDTO(reimbursementBenefitsValue);
			}
			if (null != reimbursementBenefitsValue
					&& reimbursementBenefitsValue.getPatientCareBenefitFlag() != null
					&& reimbursementBenefitsValue.getPatientCareBenefitFlag()
							.equalsIgnoreCase("PC")) {
				List<ReimbursementBenefitsDetails> patientCareTableByBenefitKey = reimbursementService
						.getPatientCareTableByBenefitKey(reimbursementBenefitsValue
								.getPatientBenefitKey());
				List<PatientCareDTO> patientCareDTOList = new ArrayList<PatientCareDTO>();
				if (patientCareTableByBenefitKey != null
						&& !patientCareTableByBenefitKey.isEmpty()) {
					for (ReimbursementBenefitsDetails patientCareDetails : patientCareTableByBenefitKey) {
						PatientCareDTO dto = new PatientCareDTO();
						dto.setEngagedFrom(patientCareDetails.getEngagedFrom());
						dto.setEngagedTo(patientCareDetails.getEngagedTo());
						dto.setKey(patientCareDetails.getKey());
						patientCareDTOList.add(dto);
					}
				}
				reimbursementDTO.getPreauthDataExtractionDetails()
						.getUploadDocumentDTO()
						.setPatientCareDTO(patientCareDTOList);
			}
			
			Date policyFromDate = reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyFromDate();
			
			Date admissionDate = reimbursementDTO.getNewIntimationDTO().getAdmissionDate();
			
			Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
			if(diffDays != null && diffDays<90){
				reimbursementDTO.setIs64VBChequeStatusAlert(true);
			}
			

			/**
			 * Added for amount claimed table enhancement---- starts
			 * */
			Double insuredSumInsured = 0d;
			if(null != claimByKey &&  null != claimByKey.getIntimation() && 
			null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
			null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
			&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
			{
			
			 insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), reimbursementDTO
							.getPolicyDto().getKey(),reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), reimbursementDTO
								.getPolicyDto().getKey());
			}
			
			reimbursementDTO.getNewIntimationDTO().getPolicy().setTotalSumInsured(insuredSumInsured);
							
			/*String branchOfficeCode = claimByKey.getIntimation().getPolicy().getHomeOfficeCode();
			OrganaizationUnit organization = createRodService.getBranchCode(branchOfficeCode);
			if(null != organization)
			{
				reimbursementDTO.getPreauthDataExtractionDetails().setOrganisationName(organization.getOrganizationUnitName());
			}*/
			if(null != insuredSumInsured)
			{
				reimbursementDTO.getPreauthDataExtractionDetails().setPaSumInsured(insuredSumInsured);
			}			
			
			reimbursementDTO.getPreauthDataExtractionDetails().setOrganisationName(claimByKey.getIntimation().getPolicy().getProposerFirstName());
			reimbursementDTO.getPreauthDataExtractionDetails().setDateOfBirth(claimByKey.getGpaParentDOB());			
			reimbursementDTO.getPreauthDataExtractionDetails().setAge(claimByKey.getGpaParentAge());
			reimbursementDTO.getPreauthDataExtractionDetails().setParentName(claimByKey.getGpaParentName());
			reimbursementDTO.getPreauthDataExtractionDetails().setRiskName(claimByKey.getGpaRiskName());
			reimbursementDTO.getPreauthDataExtractionDetails().setGpaRiskDOB(claimByKey.getGpaRiskDOB());
			reimbursementDTO.getPreauthDataExtractionDetails().setGpaRiskAge(claimByKey.getGpaRiskAge());
			reimbursementDTO.getPreauthDataExtractionDetails().setGpaSection(claimByKey.getGpaSection());
		//	reimbursementDTO.getPreauthDataExtractionDetails().setGpaCategory(claimByKey.getGpaCategory());
			
			if(null == insuredSumInsured)
			{
				reimbursementDTO.getPreauthDataExtractionDetails().setPaSumInsured(0d);
			}
			if(null == claimByKey.getGpaParentAge())
			{
				reimbursementDTO.getPreauthDataExtractionDetails().setAge(0d);
			}

			if(null == claimByKey.getGpaRiskAge())
			{
				reimbursementDTO.getPreauthDataExtractionDetails().setGpaRiskAge(0d);
			}			
		
			
			Map<Integer, Object> detailsMap = new HashMap<Integer, Object>();
			

			if(reimbursementObjectByKey.getSectionCategory() != null){
				
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
								.getProduct().getKey(), insuredSumInsured,
								reimbursementDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
								reimbursementDTO.getNewIntimationDTO()
										.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),reimbursementObjectByKey.getSectionCategory(),"A");
			}else{
				
			Long sectionCategory = 0l;
			if(reimbursementDTO.getPolicyDto()
								.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY) || reimbursementDTO.getPolicyDto()
								.getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY) || reimbursementDTO.getPolicyDto()
								.getProduct().getKey().equals(ReferenceTable.DIABETES_FLOATER_POLICY))
			{
				sectionCategory = 1l;
			}
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
								.getProduct().getKey(), insuredSumInsured,
								reimbursementDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
								reimbursementDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),sectionCategory,"0");
								/**
								 * The below code is commented as per sathish sir suggestion for ticket 2355 (production tracker).
								 * By default, the section category would be 1 for three products 
								 * MED-PRD-033 , MED-PRD-032 , MED-PRD-030
								 * 
								 * */
								
				
										//.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),0l,"0");
			}
			
			List<Double> copayValue = dbCalculationService.getProductCoPay(reimbursementDTO
					.getClaimDTO().getNewIntimationDto().getPolicy().getProduct()
					.getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
					.getInsuredPatient().getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
					.getInsuredPatient().getInsuredId(),reimbursementDTO.getNewIntimationDTO());
			reimbursementDTO.setProductCopay(copayValue);

			/**
			 * Added for amount claimed table enhancement---- ends
			 * */

			List<UploadDocumentDTO> rodSummaryDetails = rodService
					.getRODSummaryDetails(reimbursementObjectByKey.getKey());
			
			/**
			 * Fix for implemeting claims dms in bill entry screen.. -- starts
			 * **/
			if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
			{
				for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
					if(null != claimByKey)
					{
						uploadDocumentDTO.setClaimNo(claimByKey.getClaimId());
						uploadDocumentDTO.setDmsDocumentDTOList(getClaimsDMSList(claimByKey.getIntimation().getIntimationId()));
					}
				}
			}
			
			reimbursementDTO = getProrataFlagFromProduct(reimbursementDTO);
			
			reimbursementDTO.setProrataDeductionFlag(reimbursementObjectByKey.getProrataDeductionFlag());
			/**
			 * product based variable is added to enable or disable the component in page level.
			 * This would be static. -- starts
			 * */
			reimbursementDTO.setPackageAvailableFlag(reimbursementObjectByKey.getPackageAvailableFlag());


			List<Long> documentSummaryKeys = new ArrayList<Long>();
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				
				documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
				uploadDocumentDTO.setStatus(true);
				List<RODBillDetails> billEntryDetails = rodService
						.getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
				List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
				if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
					for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
						/*
						 * <<<<<<< HEAD
						 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
						 * uploadDocumentDTO)); =======
						 */
						dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,
								uploadDocumentDTO));
						// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
					}
				}
				uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
						.get(8));
				uploadDocumentDTO
						.setProductBasedICURent((Double) detailsMap.get(9));
				/*uploadDocumentDTO.setProductBasedAmbulanceAmt((Double) detailsMap
						.get(15));*/
				uploadDocumentDTO.setProductBasedAmbulanceAmt(getProductBasedAmbulanceAmt(tableDTO.getRodKey()));
				uploadDocumentDTO.setStatus(false);
				uploadDocumentDTO.setBillEntryDetailList(dtoList);
				uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
				uploadDocumentDTO.setRodKey(reimbursementObjectByKey.getKey());

				uploadDocumentDTO.setStrUserName(tableDTO.getUsername());

				if (uploadDocumentDTO != null) {
					if ((ReferenceTable.BENEFITS_PATIENT_CARE)
							.equalsIgnoreCase(uploadDocumentDTO
									.getPatientCareBenefitFlag())) {
						List<PatientCareDTO> patientCareList = claimRequestBenefitsService
								.getPatientCareDetails(uploadDocumentDTO
										.getPatientBenefitKey());
						if (null != patientCareList && !patientCareList.isEmpty()) {
							uploadDocumentDTO.setPatientCareDTO(patientCareList);
						}
						// rodDTO.getAddOnBenefitsDTO().setAdmittedNoOfDays(uploadDocumentDTO.getPatientCareNoofDays());
					}
				}
			}
			List<UploadDocumentDTO> rodBillSummaryDetails = rodService
					.getRODBillSummaryDetails(reimbursementObjectByKey.getKey(), mapper);
			
			if(rodBillSummaryDetails != null){
				for (UploadDocumentDTO uploadDocumentDTO2 : rodBillSummaryDetails) {
					uploadDocumentDTO2.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
				}
			}
			
			reimbursementDTO.getUploadDocDTO().setBillingWorkSheetUploadDocumentList(rodBillSummaryDetails);

			Double totalBilledAmount = reimbursementService
					.getTotalBilledAmount(documentSummaryKeys);

			reimbursementDTO.setAmountConsidered(totalBilledAmount != null ? String
					.valueOf(totalBilledAmount.intValue()) : "0");
			reimbursementDTO
					.setInitialAmountConsidered(totalBilledAmount != null ? String
							.valueOf(totalBilledAmount.intValue()) : "0");
			reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTask());
			reimbursementDTO.setKey(reimbursementObjectByKey.getKey());
			reimbursementDTO.setRodNumber(reimbursementObjectByKey.getRodNumber());

			setCategoryFlag(reimbursementObjectByKey, reimbursementDTO);
			
			if (!reimbursementDTO.getHospitalizaionFlag()) {
				reimbursementDTO = setHospitalizationAmount(reimbursementDTO);
			}

			List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
					.search(reimbursementObjectByKey.getClaim().getKey(),false);

			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

				previousPreAuthTableDTO.setRequestedAmt(preauthService
						.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
								previousPreAuthTableDTO.getClaimKey()));
				newList.add(previousPreAuthTableDTO);

			}
			
			

			reimbursementDTO.setPreviousPreauthTableDTO(newList);

		//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());
			reimbursementDTO.setStrUserName(tableDTO.getUsername());
			reimbursementDTO.setStrPassword(tableDTO.getPassword());
	        reimbursementDTO.setTaskNumber(tableDTO.getTaskNumber());
			
			
			reimbursementDTO.getPreauthMedicalDecisionDetails().setBillingRemarks(
					"");
			
			
			List<AddOnBenefitsDTO> populateAddOnBenefitsTableValues = claimRequestBenefitsService
					.populateAddOnBenefitsTableValues(
							reimbursementDTO.getNewIntimationDTO(),
							reimbursementObjectByKey.getClaim().getKey(),
							reimbursementObjectByKey.getKey(),
							reimbursementDTO.getProductCopay(),null);
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setAddOnBenefitsDTOList(populateAddOnBenefitsTableValues);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 8l, 9l, false,false);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 10l, 11l, true,false);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 85l, 84l, false,true);
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setDocAckknowledgement(
							reimbursementObjectByKey.getDocAcknowLedgement());

			if (reimbursementObjectByKey.getDocAcknowLedgement()
					.getHospitalizationRepeatFlag() != null
					&& reimbursementObjectByKey.getDocAcknowLedgement()
							.getHospitalizationRepeatFlag().toLowerCase()
							.equalsIgnoreCase("y")) {
				reimbursementDTO.setIsHospitalizationRepeat(true);
			}
			
			if (reimbursementObjectByKey.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
				MedicalApprover medicalApprover = reimbursementService
						.getMedicalApprover(reimbursementObjectByKey.getKey());
				if (medicalApprover != null) {
					reimbursementDTO.setPreviousRemarks(medicalApprover
							.getApproverReply());
					reimbursementDTO.setPreviousReasonForReferring(medicalApprover
							.getReasonForReferring());
				}
				
				reimbursementDTO.setIsReferToBilling(true);
			}
			
			reimbursementDTO.getPreauthDataExtractionDetails().setBillingRemarks(reimbursementObjectByKey.getBillingRemarks());
			if (claimByKey.getClaimType() != null
					&& claimByKey.getClaimType().getKey()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {

//				PreMedicalMapper premedicalMapper = new PreMedicalMapper();
				Preauth previousPreauth = getPreviousPreauth(claimByKey.getKey());
				
				if(previousPreauth != null) {
					reimbursementDTO.setPreauthKey(previousPreauth.getKey());
				}
			}
			
			if(claimByKey.getStatus() != null) {
				if(claimByKey.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) ||  claimByKey.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)) {
					reimbursementDTO.setIsWithDrawn(true);
				}
			}
			
			loadRRCRequestValues(reimbursementDTO,insuredSumInsured,SHAConstants.BILLING);
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance().get64VBStatus(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber(), reimbursementDTO.getNewIntimationDTO().getIntimationId());
				if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
					reimbursementDTO.setIsDishonoured(true);
				}  else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
					reimbursementDTO.setIsPending(true);
				}
			}
			reimbursementDTO.setSittingsAmount(dbCalculationService.getDialysisAmount(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(), insuredSumInsured.longValue()).intValue());
			Map<String, String> popupMessages = dbCalculationService.getPOPUPMessages(reimbursementDTO.getPolicyKey(), reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey(),reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
			reimbursementDTO.setPopupMap(popupMessages);
			
			
			if(reimbursementDTO.getNewIntimationDTO().getHospitalDto() != null && reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag() != null
					&& reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag().equalsIgnoreCase("Y")){
				if(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks() != null){
					Map<String, String> suspiciousMap = SHAUtils.getSuspiciousMap(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks());
					reimbursementDTO.setSuspiciousPopupMap(suspiciousMap);
				}
			}
			// If hospitalization is not processed then remaining ROD should be rejected.
			Reimbursement hospROD = reimbursementService.getHospitalizationROD(reimbursementObjectByKey.getClaim().getKey());
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
				if(hospROD == null) {
					reimbursementDTO.setIsHospitalizationRejected(true);
				}
			}
			
			Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
					.getIntimation().getHospital());
			
			paymentDetailsForfinancial(reimbursementObjectByKey, reimbursementDTO,
					hospitalById);
			
			setReimbursementValues(reimbursementObjectByKey, reimbursementDTO);
			
	       Reimbursement hospitalizationOrPartialROD = reimbursementService.getHospitalizationOrPartialROD(reimbursementObjectByKey.getClaim().getKey());
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				
				
				Integer seqNumber = 0;
				
				if(reimbursementObjectByKey.getRodNumber() != null){
					String[] split = reimbursementObjectByKey.getRodNumber().split("/");
					seqNumber = Integer.valueOf(split[split.length - 1]);
				}
				
				if(hospitalizationOrPartialROD == null && ! seqNumber.equals(1)) {
//					reimbursementDTO.setIsDishonoured(true);
					reimbursementDTO.setIsHospitalizationRejected(true);
				}
			}
			
			Map<String, Integer> productBenefitFlag = dbCalculationService.getProductBenefitFlag(reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey());
			
			if(productBenefitFlag.get(SHAConstants.PRE_HOSP_FLAG).equals(0)) {
				reimbursementDTO.setIsPreHospApplicable(false);
			}
			
			if(productBenefitFlag.get(SHAConstants.POST_HOSP_FLAG).equals(0)) {
				reimbursementDTO.setIsPostHospApplicable(false);
			}
			
			if(productBenefitFlag.get(SHAConstants.LUMP_SUM_FLAG).equals(1)) {
				reimbursementDTO.setIsLumpsumApplicable(true);
			}
			
			if(productBenefitFlag.get(SHAConstants.HOSPITALCASH_FLAG).equals(1)) {
				reimbursementDTO.setIsHospitalCashApplicable(true);
			}
			
			if(productBenefitFlag.get(SHAConstants.PATIENTCARE_FLAG).equals(1)) {
				reimbursementDTO.setIsPatientCareApplicable(true);
			}
			
			
			MastersValue networkHospitalType = masterService.getMaster(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getNetworkHospitalTypeId());
			reimbursementDTO.setNetworkHospitalType(networkHospitalType.toString());
			
			reimbursementDTO = checkFinalEnhancement(reimbursementDTO);

			
			try {
				if((reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest() != null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest().equalsIgnoreCase("Y"))
						|| (reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))) {
					if(reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag() ==  null || reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag().equalsIgnoreCase("N")) {
						reimbursementDTO.setIsReconsiderationRequest(true);
						if(reimbursementService.isClaimPaymentAvailable(reimbursementObjectByKey.getRodNumber())) {
							Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
							reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
						} else {
							/**
							 * For reject reconsider, cancel rod button was enabled. But for this ticket GALAXYMAIN-6222	
							 * which was raised in PA, now cancel rod button is diabled for rejection case to.
							 * Hence below code was commented.
							 * */
							//reimbursementDTO.setIsReconsiderationRequest(false);
						}
//						Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
//						reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
					}
					
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				reimbursementDTO.setIsCashlessType(true);
			}
			if(reimbursementDTO.getIsCashlessType() && reimbursementDTO.getHospitalizaionFlag() && (reimbursementDTO.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY))){
				reimbursementDTO.setIsDirectToBilling(true);
				Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursementDTO.getClaimDTO().getKey());
				reimbursementDTO.getClaimDTO().setLatestPreauthKey(latestPreauth.getKey());
			}
			reimbursementDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(reimbursementDTO.getNewIntimationDTO().getAdmissionDate(), reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));
			reimbursementDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey()));
			reimbursementDTO.setIsPEDInitiatedForBtn(reimbursementDTO.getIsPEDInitiated());
			
			List<InsuredPedDetails> pedByInsured = policyService.getPEDByInsured(reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			reimbursementDTO.setInsuredPedDetails(pedByInsured);
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				if(reimbursementDTO.getHospitalizaionFlag()) {
					Reimbursement partialHospitalizationRODWithSettled = getPartialHospitalizationRODWithSettled(claimByKey.getKey());
					ReimbursementCalCulationDetails hosptialization = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursementDTO.getKey(), ReferenceTable.HOSPITALIZATION);
					if(hosptialization != null) {
						reimbursementDTO.setBillingApprovedAmount(hosptialization.getPayableToHospAftTDS() != null ? hosptialization.getPayableToHospAftTDS().doubleValue() : 0d);
						reimbursementDTO.setPayableToHospAmt(hosptialization.getPayableToHospital() != null ? hosptialization.getPayableToHospital().doubleValue() : 0d);
						reimbursementDTO.setHospDiscountAmount(hosptialization.getHospitalDiscount() != null ? hosptialization.getHospitalDiscount().doubleValue() : 0d);
					}
					if(partialHospitalizationRODWithSettled != null) {
						ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(partialHospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
						reimbursementDTO.setPayableToInsAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium().doubleValue() : 0d);
						reimbursementDTO.setFAApprovedAmount(partialHospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? partialHospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
					}
				} else if(reimbursementDTO.getPartialHospitalizaionFlag()) {
					Reimbursement hospitalizationRODWithSettled = getHospitalizationRODWithSettled(claimByKey.getKey());
					if(hospitalizationRODWithSettled != null) {
						ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(hospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
						reimbursementDTO.setPayableToHospAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital().doubleValue() : 0d);
						reimbursementDTO.setHospDiscountAmount(reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d);
						Double amount = (reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d) + (hospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? hospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
						reimbursementDTO.setFAApprovedAmount(amount);
					}
				}
			}
			
			Boolean queryReceivedStatusRod = reimbursementQuerySerice.isQueryReceivedStatusRod(reimbursementObjectByKey.getKey());
			 
			 if(queryReceivedStatusRod){
				 reimbursementDTO.setIsQueryReceived(true);
			 }
			 
			 //GALAXYMAIN-5839
			 /*	String remedicalRemarks = null;
				String rebillingRemarks = null;
				Long latestMedicalApproverKey = reimbursementService.getLatestMedicalApproverKey(reimbursementObjectByKey.getKey());
				if(latestMedicalApproverKey != null){
					MedicalApprover medicalApproverByKey = reimbursementService.getMedicalApproverByKey(latestMedicalApproverKey);
					if(medicalApproverByKey != null){
						reimbursementDTO.setIsReMedical(true);
						remedicalRemarks = medicalApproverByKey.getApproverReply();
					}
				}
				
				Long latestApproverKey = reimbursementService.getLatestMedicalApproverForRebilling(reimbursementObjectByKey.getKey());
				if(latestApproverKey != null){
					MedicalApprover medicalApproverByKey = reimbursementService.getMedicalApproverByKey(latestApproverKey);
					if(medicalApproverByKey != null){
						reimbursementDTO.setIsReBilling(true);
						rebillingRemarks = medicalApproverByKey.getApproverReply();
					}
				}
				
				reimbursementDTO.getPreauthDataExtractionDetails().setBillingRemarks(rebillingRemarks);
				reimbursementDTO.getPreauthDataExtractionDetails().setMedicalRemarks(remedicalRemarks);*/
				
			 
			 
			 
				List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = acknowledgementDocumentsReceivedService.getReimbursementCalculationDetails(reimbursementObjectByKey.getKey());
				if(reimbursementCalculationDetails != null && !reimbursementCalculationDetails.isEmpty()) {
					for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
						if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
								reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.HOSPITALIZATION)){
							
							HopitalizationCalulationDetailsDTO hospitalizationCalcDTO = getHospitalizationCalculationDTO(reimbursementCalCulationDetails2,reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
							if(reimbursementDTO.getDocumentReceivedFromId() != null && reimbursementDTO.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
						           hospitalizationCalcDTO.setBalanceToBePaid(0);
							}
							reimbursementDTO.setHospitalizationCalculationDTO(hospitalizationCalcDTO);
							
						}
						else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
								reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)){
							PreHopitalizationDetailsDTO preHospitalizationCalcDTO = getPreHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
							reimbursementDTO.setPreHospitalizationCalculationDTO(preHospitalizationCalcDTO);
							
						}else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
								reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
							PostHopitalizationDetailsDTO postHospitalizationCalcDTO = getPostHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
							reimbursementDTO.setPostHospitalizationCalculationDTO(postHospitalizationCalcDTO);
						}
					}
				}
			
				reimbursementDTO.setAmountConsidered(reimbursementDTO.getAmountConsidedAfterCoPay() != null ? String
						.valueOf(reimbursementDTO.getAmountConsidedAfterCoPay().intValue()) : "0");
			 
//			 reimbursementService.setBPMForClaimBilling(reimbursementDTO, false, "MEDICAL", reimbursementObjectByKey);
				Double totalClaimedAmount = reimbursementService.getTotalClaimedAmount(reimbursementObjectByKey);
				 reimbursementDTO.setRodTotalClaimedAmount(totalClaimedAmount);
				 
				 if(reimbursementDTO.getAmountConsidedAfterCoPay() != null && totalBilledAmount != null && !reimbursementDTO.getAmountConsidedAfterCoPay().equals(totalBilledAmount)) {
					 reimbursementDTO.setAmountConsidered(String.valueOf(totalBilledAmount.longValue()));
				 }

				 //SHAUtils.setDefaultCopayValue(reimbursementDTO);

				 if(reimbursementDTO.getIsReconsiderationRequest() != null && reimbursementDTO.getIsReconsiderationRequest()){
					 reimbursementDTO.setIsReverseAllocation(false);
				 }

				 if(reimbursementObjectByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
						 && reimbursementObjectByKey.getDocAcknowLedgement() != null && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
						 && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
				 
						 if(hospitalById != null){
								Boolean hospitalDiscount = SHAUtils.isHospitalDiscount(hospitalById);
								reimbursementDTO.setIsHospitalDiscountApplicable(hospitalDiscount);
						 }
				 }
				 if(reimbursementObjectByKey.getBillingRemarks()!=null){
					 
					 reimbursementDTO.getPreauthDataExtractionDetails().setBillingMedicalRemarks(reimbursementObjectByKey.getBillingRemarks());
				 }
				 
				 if(reimbursementObjectByKey.getApprovalRemarks()!=null){
					 
					 reimbursementDTO.getPreauthDataExtractionDetails().setMedicalRemarks(reimbursementObjectByKey.getApprovalRemarks());
				 }
				 
				 if(reimbursementObjectByKey.getClaimApprovalRemarks()!=null){
					 
					 reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalRemarks(reimbursementObjectByKey.getClaimApprovalRemarks());
				 }
				 
				 //reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalRemarks(reimbursementObjectByKey.getMedicalRemarks());
				 
				/* if (reimbursementObjectByKey.getStatus().getKey()
							.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
						
						MedicalApprover medicalApprover = pareimbursementService.getMedicalApprover(reimbursementObjectByKey.getKey());
						if(null != medicalApprover)
						{
							reimbursementDTO.getPreauthDataExtractionDetails().setBillingMedicalReason(medicalApprover.getReasonForReferring());
							reimbursementDTO.getPreauthDataExtractionDetails().setBillingMedicalRemarks(medicalApprover.getReferringRemarks());
							reimbursementDTO.setPreviousRemarks(medicalApprover.getReferringRemarks());
							reimbursementDTO.setPreviousReasonForReferring(medicalApprover.getReasonForReferring());
						}
						
					}
				 */
				 /*if (reimbursementObjectByKey.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_APPROVAL_SEND_REPLY_STATUS)) {*/
						
				       // Long latestMedicalApproverForReFinancial = pareimbursementService.getLatestMedicalApproverForReFinancial(reimbursementObjectByKey.getKey());
				      //  if(latestMedicalApproverForReFinancial!=null){
				        	/* MedicalApprover claimMedicalApprover = pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(), SHAConstants.PA_CLAIM_APPROVAL_REFINANCE);
								//MedicalApprover medicalApprover = pareimbursementService.getMedicalApprover(reimbursementObjectByKey.getKey());
								if(null != claimMedicalApprover)
								{
									reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalReason(claimMedicalApprover.getReasonForReferring());
									reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalRemarks(claimMedicalApprover.getApproverReply());
									reimbursementDTO.setPreviousRemarks(claimMedicalApprover.getReferringRemarks());
									reimbursementDTO.setPreviousReasonForReferring(claimMedicalApprover.getReasonForReferring());
								}*/
				       // }
				       
						
				//	}
				 
				/* if (reimbursementObjectByKey.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)) {*/
				       /* Long latestMedicalApproverForReClaim = pareimbursementService.getLatestMedicalApproverForReClaim(reimbursementObjectByKey.getKey());
				        if(latestMedicalApproverForReClaim!=null){*/
				        	/*MedicalApprover medicalApprover = pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(), SHAConstants.PA_CLAIM_REQUEST_REFINANCE);
							if(null != medicalApprover)
							{
								//reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalReason(medicalApprover.getReasonForReferring());
								reimbursementDTO.getPreauthDataExtractionDetails().setMedicalRemarks(medicalApprover.getApproverReply());
								reimbursementDTO.setPreviousRemarks(medicalApprover.getReferringRemarks());
								reimbursementDTO.setPreviousReasonForReferring(medicalApprover.getReasonForReferring());
							}*/
				    //    }
						
						
				//	}
				 
//				 if(reimbursementDTO.getNewIntimationDTO() != null && reimbursementDTO.getNewIntimationDTO().getInsuredPatient() != null &&
//						 reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey() != null){
//						Long claimCount = preauthService.getClaimCount(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
//						if(claimCount != null){
//							reimbursementDTO.setClaimCount(claimCount);
//						}
//					}
				 List<TableBenefitsDTO> paBenefitsListByRodKey = paSearchProcessClaimBillingService.getPABenefitsListByRodKey(reimbursementObjectByKey.getKey());
				 
				 reimbursementDTO.getPreauthDataExtractionDetails().setBenefitDTOList(paBenefitsListByRodKey);
				 
				MastersValue benefitsId = reimbursementObjectByKey.getBenefitsId();
				Insured insuredKey = reimbursementObjectByKey.getInsuredKey();
				 if(benefitsId!=null && insuredKey!=null){
					 
					 BeanItemContainer<SelectValue> coverContainer = null;
					 
					 if(null != insuredKey && null != insuredKey.getPolicy() && null != insuredKey.getPolicy().getProduct() &&
							 null != insuredKey.getPolicy().getProduct().getKey() && 
							 !(ReferenceTable.getGPAProducts().containsKey(insuredKey.getPolicy().getProduct().getKey()))){
						 
						 coverContainer = dbCalculationService.getBenefitCoverValueContainer(insuredKey.getKey(), benefitsId.getKey());
					 }
					 else
					 {
						 coverContainer = dbCalculationService.getGPABenefitCoverValueContainer(insuredKey.getKey(), benefitsId.getKey());
					 }
					 PreauthDataExtaractionDTO preauthDataExtractionDetails = reimbursementDTO.getPreauthDataExtractionDetails();
					 preauthDataExtractionDetails.setCoverListContainer(coverContainer.getItemIds());
					 
					 reimbursementDTO.getPreauthDataExtractionDetails().setBenefitsValue(benefitsId.getValue());
					
				 }
				 Double alreadyPaidAmount = paSearchProcessClaimBillingService.getAlreadyPaidAmount(reimbursementObjectByKey.getRodNumber());
				 reimbursementDTO.getPreauthDataExtractionDetails().setAlreadyPaidAmt(alreadyPaidAmount);
				 
				 List<OptionalCoversDTO> paOptionalCoverListByRodKey = paSearchProcessClaimBillingService.getPAOptionalCoverListByRodKey(reimbursementObjectByKey);

				 reimbursementDTO.getPreauthDataExtractionDetails().setOptionalCoversTableListBilling(paOptionalCoverListByRodKey);
				 
				 Product product = reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct();
				 BeanItemContainer<SelectValue> bebefitAdditionalCovers = null;
				 
				 if(null != product && null != product.getKey() && !(ReferenceTable.getGPAProducts().containsKey(product.getKey())))
					{
					 bebefitAdditionalCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.OPTIONAL_COVER, product.getKey(),insuredKey.getKey());
					}
				 else
				 {
					 bebefitAdditionalCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.GPA_OPTIONAL_COVER, product.getKey(),insuredKey.getKey());
				 }
				 PreauthDataExtaractionDTO preauthDataExtractionDetails = reimbursementDTO.getPreauthDataExtractionDetails();
				 preauthDataExtractionDetails.setOptionalCoverListContainer(bebefitAdditionalCovers.getItemIds());			
			
				 
				 List<AddOnCoverOnLoadDTO> coverNameList= new ArrayList<AddOnCoverOnLoadDTO>();
				 List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> paAddOnCoverListByRodKey = paSearchProcessClaimBillingService.getAddOnCoverListByRodKey(reimbursementObjectByKey.getKey(),product.getKey(),coverNameList);
				
				 reimbursementDTO.getPreauthDataExtractionDetails().setAddOnCoversTableListBilling(paAddOnCoverListByRodKey);
				 
				if(!coverNameList.isEmpty()){
					reimbursementDTO.getPreauthDataExtractionDetails().setAddOnCoverNameList(coverNameList);
				}
				 
				BeanItemContainer<SelectValue> addOnCovers = null;
				if(null != product && null != product.getKey() && !(ReferenceTable.getGPAProducts().containsKey(product.getKey())))
				{
					addOnCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.ADDITIONAL_COVER, product.getKey(),insuredKey.getKey());
				}
				else
				{
					addOnCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.GPA_ADDITIONAL_COVER, product.getKey(),insuredKey.getKey());
				}
				
				 preauthDataExtractionDetails.setAddOnCoverListContainer(addOnCovers.getItemIds());
				 Double balanceSI = 0d;				 
				 if(null != product && null != product.getKey() && !(ReferenceTable.getGPAProducts().containsKey(product.getKey())))
					{
						balanceSI = dbCalculationService.getPABalanceSI(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey(), reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getKey(),null != reimbursementObjectByKey.getBenefitsId() ? reimbursementObjectByKey.getBenefitsId().getKey() : 0l);
					}
				 else
				 {
					 balanceSI = dbCalculationService.getGPAAvailableSI(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey(), reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getKey(),null != reimbursementObjectByKey.getBenefitsId() ? reimbursementObjectByKey.getBenefitsId().getKey() : 0l);
				 }
				 /*if(reimbursementObjectByKey.getDocAcknowLedgement()!=null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest()!=null 
						 && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest())){
					 	balanceSI = balanceSI + reimbursementObjectByKey.getFinancialApprovedAmount();
				 }*/
				 
				 if((reimbursementObjectByKey.getDocAcknowLedgement()!=null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest()!=null 
						 && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest()))
						 || (reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))){
					 
					 ClaimPayment claimPayment = paReimbursementService.getClaimPayment(reimbursementObjectByKey.getRodNumber());
					 if(null != claimPayment && claimPayment.getPaBenefitApproveAmount() != null){
					 	balanceSI = balanceSI + claimPayment.getPaBenefitApproveAmount();
					 }
				 }
				 reimbursementDTO.getPreauthDataExtractionDetails().setAvailableSI(null != balanceSI ? String.valueOf(balanceSI) : null);
				 
				 //List<TableBenefitsDTO> benefitDTOList = reimbursementDTO.getPreauthDataExtractionDetails().getBenefitDTOList();
				 //List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> addOnCoversTableListBilling = reimbursementDTO.getPreauthDataExtractionDetails().getAddOnCoversTableListBilling();
				 //List<OptionalCoversDTO> optionalCoversTableListBilling = reimbursementDTO.getPreauthDataExtractionDetails().getOptionalCoversTableListBilling();
				 
				 //List<PABillingConsolidatedDTO> paBillingConsolidatedDTOs = paSearchProcessClaimBillingService.getConsolidatedValue(benefitDTOList,addOnCoversTableListBilling,optionalCoversTableListBilling);
				 //reimbursementDTO.getPreauthDataExtractionDetails().setBillingConsolidatedDTOList(paBillingConsolidatedDTOs);
				 BeanItemContainer<SelectValue>  bBenefitsValueContainer = masterService.getSelectValueContainerForBenefits(ReferenceTable.MASTER_TYPE_CODE_BENEFITS,reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey());
				 reimbursementDTO.getPreauthDataExtractionDetails().setBenefitsValueContainer(bBenefitsValueContainer.getItemIds());
						 
				 if(null != reimbursementDTO.getRodNumber()){
						ClaimPayment claimPayment = acknowledgementDocumentsReceivedService.getClaimPaymentDetails(reimbursementDTO.getRodNumber());
						if(null != claimPayment && null != claimPayment.getStatusId() && claimPayment.getStatusId().getKey().equals(ReferenceTable.PAYMENT_SETTLED)){
							reimbursementDTO.setIsPaymentSettled(true);
						}
					}
				 
					if(ReferenceTable.getGMCProductList().containsKey(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey()) ||
							ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey())){
					      Insured insuredByKey = intimationService.getInsuredByKey(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey());
					      Insured MainMemberInsured = null;
					      
					      if(insuredByKey.getDependentRiskId() == null){
					    	  MainMemberInsured = insuredByKey;
					      }else{
					    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredNameForDefault(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId());
					    	  MainMemberInsured = insuredByPolicyAndInsuredId;
					      }
					      
					      if(MainMemberInsured != null){
					    	  
					    	  reimbursementDTO.getNewIntimationDTO().setGmcMainMemberName(MainMemberInsured.getInsuredName());
					    	  reimbursementDTO.getNewIntimationDTO().setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());
					    	  
					      }
						}
				 
				 //view.setPAProcessClaimApproval(PAClaimAprNonHosWizard.class,reimbursementDTO);
					reimbursementDTO.setDbOutArray(tableDTO.getDbOutArray());

					LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
					legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
					reimbursementDTO.setLegalHeirDto(legalHeirDTO);
					
					List<NomineeDetailsDto> nomineeDtoList = intimationService.getNomineeDetailsListByTransactionKey(tableDTO.getRodKey());
					if( nomineeDtoList != null && !nomineeDtoList.isEmpty()){
						reimbursementDTO.getNewIntimationDTO().setNomineeList(nomineeDtoList);
					}
					if(reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null 
							&& reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()) {
						List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(tableDTO.getRodKey());
						if(legalHeirList != null && !legalHeirList.isEmpty()) {
							List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
							LegalHeirDTO legalHeirDto;
							for (LegalHeir legalHeir : legalHeirList) {
								legalHeirDto = new LegalHeirDTO(legalHeir);
								legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
								legalHeirDTOList.add(legalHeirDto);
							}
							reimbursementDTO.setLegalHeirDTOList(legalHeirDTOList);
						}
					}
					//R1022
					reimbursementDTO.setScreenName(SHAConstants.PA_FINANCIAL_NON_HOSP);
					//IMSSUPPOR-31736
					reimbursementDTO.setBillingApprovedDate(reimbursementObjectByKey.getBillingCompletedDate());					
					if(reimbursementObjectByKey.getClaim().getLegalClaim() !=null
							&& reimbursementObjectByKey.getClaim().getLegalClaim().equals("Y")){
						LegalTaxDeduction taxDeduction = reimbursementService.getTaxDeductionFromRODKey(reimbursementObjectByKey.getKey());
						if(taxDeduction !=null){
							LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
							LegalBillingDTO billingDTO = taxDeductionMapper.getDTOFromTaxDeduction(taxDeduction);
							billingDTO = reimbursementService.calculateLegalSettlment(billingDTO);
							reimbursementDTO.setLegalBillingDTO(billingDTO);
						}
					}
					
					Map<String,Object> icrAgent  = dbCalculationService.getAgentAndBranchName(reimbursementDTO.getNewIntimationDTO().getIntimationId()); 
					 if(icrAgent != null && !icrAgent.isEmpty()) {
							String agentScorePoint = icrAgent.get(SHAConstants.ICR_AGENT_POINT).toString();
							reimbursementDTO.setIcrAgentValue(agentScorePoint);
							String smScorepoint = icrAgent.get(SHAConstants.SM_AGENT_POINT).toString();
							reimbursementDTO.setSmAgentValue(smScorepoint);
					 }
					 
					 Policy policy = reimbursementDTO.getNewIntimationDTO().getPolicy();
						// added for PA installment payment process in policy level and product
						// level check
						Map<String, String> getPolicyInstallment = dbCalculationService
								.getInstallmentPaymentFlag(policy.getPolicyNumber(), policy.getProduct()
										.getKey());
						if (getPolicyInstallment != null && !getPolicyInstallment.isEmpty()) {
							reimbursementDTO.setPolicyInstalmentFlag(getPolicyInstallment
									.get(SHAConstants.FLAG) != null ? getPolicyInstallment
									.get(SHAConstants.FLAG) : "N");
							reimbursementDTO
									.setPolicyInstalmentMsg(getPolicyInstallment
											.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallment
											.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
						}
						if (reimbursementDTO.getPolicyInstalmentFlag() != null
								&& reimbursementDTO.getPolicyInstalmentFlag().equals(
										SHAConstants.YES_FLAG)) {
							Integer policyInstallmentAmount = PremiaService.getInstance()
									.getPolicyInstallmentAmount(policy
											.getPolicyNumber());
							reimbursementDTO.setPolicyInstalmentPremiumAmt(policyInstallmentAmount
									.doubleValue());
							
							String policyDueDate = PremiaService.getInstance()
									.getPolicyInstallmentDetails(policy
											.getPolicyNumber());
							//code added if we recevied instalment due amount as zero after pending amount adjusted to premia/bancs  need to set due date as admission date by noufel
				 			if((reimbursementDTO.getPolicyInstalmentPremiumAmt() != null && reimbursementDTO.getPolicyInstalmentPremiumAmt() == 0d) && 
				 					(policyDueDate == null || policyDueDate.isEmpty())) {
				 				
				 				reimbursementDTO.setPolicyInstalmentDueDate(reimbursementDTO.getPreauthDataExtractionDetails().getAdmissionDate());	
				 			}
				 			else{
				 			if (reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
				 				reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.formatTimeFromString(policyDueDate.toString()));	
				    			}
				    			else{
				    				reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.dateformatFromString(policyDueDate.toString()));
				    			}
				 			}
						}
					 
				 view.setPAProcessFinancialNonHosp(PANonHospFinancialWizard.class,reimbursementDTO);
		
		}
		}
		
		
		protected void showPAProcessClaimApprovalWizard(
				SearchProcessClaimBillingTableDTO tableDTO) {

			Reimbursement reimbursementObjectByKey = rodService
					.getReimbursementObjectByKey(tableDTO.getRodKey());
			
			Claim objClaim = reimbursementObjectByKey.getClaim();
			Boolean isValidClaim = true;
			if (null != objClaim) {
				if ((SHAConstants.YES_FLAG).equalsIgnoreCase(objClaim
						.getLegalFlag())) {
					isValidClaim = false;
					view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
				}
			}
			
			if(isValidClaim) {
			ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
			PreauthDTO reimbursementDTO = mapper
					.getReimbursementDTO(reimbursementObjectByKey);
			
			if(("Y").equalsIgnoreCase(reimbursementDTO.getPreauthDataExtractionDetails().getReconsiderationFlag()))
			{
				List<ReconsiderRODRequestTableDTO> reconsiderDTOList = getReconsiderRODRequestForBillEntry(tableDTO.getRodKey());
				if(null != reconsiderDTOList && !reconsiderDTOList.isEmpty())
				{
					for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reconsiderDTOList) {
						if(null != reconsiderRODRequestTableDTO.getIsRejectReconsidered() && reconsiderRODRequestTableDTO.getIsRejectReconsidered())
						{
							reimbursementDTO.setIsRejectReconsidered(true);
						}
						else
						{
							reimbursementDTO.setIsRejectReconsidered(false);
						}
					}
				}
				reimbursementDTO.setReconsiderRodRequestList(getReconsiderRODRequestForBillEntry(tableDTO.getRodKey()));
			}

			reimbursementDTO.getPreauthDataExtractionDetails().setDischargeDateForPa(reimbursementObjectByKey.getDateOfDischarge());
			
			Claim claimByKey = claimService.getClaimByClaimKey(reimbursementObjectByKey.getClaim().getKey());
			if(null != claimByKey)
			{
				if(null != reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId())
				{
					String  docReceivedFrom = reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue();
					reimbursementDTO.setPreviousAccountDetailsList(populatePreviousAccountDetails(claimByKey.getIntimation().getIntimationId(),docReceivedFrom));
				}
			}
			Long reimbursementKey = tableDTO.getRodKey();
			// Reimbursement previousLatestROD =
			// getPreviousLatestROD(claimByKey.getKey(), reimbursementObjectByKey);
			if (false) {
				// reimbursementKey = previousLatestROD.getKey();
				// reimbursementDTO = mapper.getReimbursementDTO(previousLatestROD);
				// reimbursementDTO.setIsPostHospitalization(true);
				// setReimbursmentTOPreauthDTO(mapper, claimByKey,
				// previousLatestROD, reimbursementDTO, true );
				// Hospitals hospitalById =
				// hospitalService.getHospitalById(claimByKey.getIntimation().getHospital());
				// ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails =
				// reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
				// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				// updateHospitalDetails.setHospitalState(hospitalById.getState());
				// updateHospitalDetails.setHospitalCode(hospitalById.getHospitalCode());
				// reimbursementDTO.setReconsiderationList(getReconsiderRODRequest(claimByKey));
				// reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
				// previousLatestROD.getKey());
				//
				// reimbursementDTO.setIsPreviousROD(true);
				// // reimbursementDTO.setPreviousROD(previousLatestROD);

			} else {
				reimbursementDTO
						.setReconsiderationList(getReconsiderRODRequest(claimByKey));

				reimbursementDTO = setReimbursmentTOPreauthDTO(mapper, claimByKey,
						reimbursementObjectByKey, reimbursementDTO, true, SHAConstants.BILLING);
				reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
						reimbursementObjectByKey.getKey());
			}

			UploadDocumentDTO reimbursementBenefitsValue = claimRequestBenefitsService
					.getReimbursementBenefitsValue(reimbursementObjectByKey
							.getKey());
			if (reimbursementBenefitsValue != null) {
				reimbursementDTO.getPreauthDataExtractionDetails()
						.setUploadDocumentDTO(reimbursementBenefitsValue);
			}
			if (null != reimbursementBenefitsValue
					&& reimbursementBenefitsValue.getPatientCareBenefitFlag() != null
					&& reimbursementBenefitsValue.getPatientCareBenefitFlag()
							.equalsIgnoreCase("PC")) {
				List<ReimbursementBenefitsDetails> patientCareTableByBenefitKey = reimbursementService
						.getPatientCareTableByBenefitKey(reimbursementBenefitsValue
								.getPatientBenefitKey());
				List<PatientCareDTO> patientCareDTOList = new ArrayList<PatientCareDTO>();
				if (patientCareTableByBenefitKey != null
						&& !patientCareTableByBenefitKey.isEmpty()) {
					for (ReimbursementBenefitsDetails patientCareDetails : patientCareTableByBenefitKey) {
						PatientCareDTO dto = new PatientCareDTO();
						dto.setEngagedFrom(patientCareDetails.getEngagedFrom());
						dto.setEngagedTo(patientCareDetails.getEngagedTo());
						dto.setKey(patientCareDetails.getKey());
						patientCareDTOList.add(dto);
					}
				}
				reimbursementDTO.getPreauthDataExtractionDetails()
						.getUploadDocumentDTO()
						.setPatientCareDTO(patientCareDTOList);
			}

			/**
			 * Added for amount claimed table enhancement---- starts
			 * */
			
			Double insuredSumInsured = 0d;
			
			if(null != claimByKey &&  null != claimByKey.getIntimation() && 
					null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
					null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
					&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
			{
				insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), reimbursementDTO
							.getPolicyDto().getKey(),reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), reimbursementDTO
								.getPolicyDto().getKey());
			}
			
			reimbursementDTO.getNewIntimationDTO().getPolicy().setTotalSumInsured(insuredSumInsured);

			Map<Integer, Object> detailsMap = new HashMap<Integer, Object>();
			

			if(reimbursementObjectByKey.getSectionCategory() != null){
				
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
								.getProduct().getKey(), insuredSumInsured,
								reimbursementDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
								reimbursementDTO.getNewIntimationDTO()
										.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),reimbursementObjectByKey.getSectionCategory(),"A");
			}else{
				
			Long sectionCategory = 0l;
			if(reimbursementDTO.getPolicyDto()
								.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY) || reimbursementDTO.getPolicyDto()
								.getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY) || reimbursementDTO.getPolicyDto()
								.getProduct().getKey().equals(ReferenceTable.DIABETES_FLOATER_POLICY))
			{
				sectionCategory = 1l;
			}
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
								.getProduct().getKey(), insuredSumInsured,
								reimbursementDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
								reimbursementDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),sectionCategory,"0");
								/**
								 * The below code is commented as per sathish sir suggestion for ticket 2355 (production tracker).
								 * By default, the section category would be 1 for three products 
								 * MED-PRD-033 , MED-PRD-032 , MED-PRD-030
								 * 
								 * */
								
				
										//.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),0l,"0");
			}
			
			List<Double> copayValue = dbCalculationService.getProductCoPay(reimbursementDTO
					.getClaimDTO().getNewIntimationDto().getPolicy().getProduct()
					.getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
					.getInsuredPatient().getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
					.getInsuredPatient().getInsuredId(),reimbursementDTO.getNewIntimationDTO());
			reimbursementDTO.setProductCopay(copayValue);

			/**
			 * Added for amount claimed table enhancement---- ends
			 * */

			List<UploadDocumentDTO> rodSummaryDetails = rodService
					.getRODSummaryDetails(reimbursementObjectByKey.getKey());
			
			/**
			 * Fix for implemeting claims dms in bill entry screen.. -- starts
			 * **/
			if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
			{
				for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
					if(null != claimByKey)
					{
						uploadDocumentDTO.setClaimNo(claimByKey.getClaimId());
						uploadDocumentDTO.setDmsDocumentDTOList(getClaimsDMSList(claimByKey.getIntimation().getIntimationId()));
					}
				}
			}
			
			reimbursementDTO = getProrataFlagFromProduct(reimbursementDTO);
			
			reimbursementDTO.setProrataDeductionFlag(reimbursementObjectByKey.getProrataDeductionFlag());
			/**
			 * product based variable is added to enable or disable the component in page level.
			 * This would be static. -- starts
			 * */
			reimbursementDTO.setPackageAvailableFlag(reimbursementObjectByKey.getPackageAvailableFlag());


			List<Long> documentSummaryKeys = new ArrayList<Long>();
			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				
				documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
				uploadDocumentDTO.setStatus(true);
				List<RODBillDetails> billEntryDetails = rodService
						.getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
				List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
				if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
					for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
						/*
						 * <<<<<<< HEAD
						 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
						 * uploadDocumentDTO)); =======
						 */
						dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,
								uploadDocumentDTO));
						// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
					}
				}
				uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
						.get(8));
				uploadDocumentDTO
						.setProductBasedICURent((Double) detailsMap.get(9));
				/*uploadDocumentDTO.setProductBasedAmbulanceAmt((Double) detailsMap
						.get(15));*/
				uploadDocumentDTO.setProductBasedAmbulanceAmt(getProductBasedAmbulanceAmt(tableDTO.getRodKey()));
				uploadDocumentDTO.setStatus(false);
				uploadDocumentDTO.setBillEntryDetailList(dtoList);
				uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
				uploadDocumentDTO.setRodKey(reimbursementObjectByKey.getKey());

				uploadDocumentDTO.setStrUserName(tableDTO.getUsername());

				if (uploadDocumentDTO != null) {
					if ((ReferenceTable.BENEFITS_PATIENT_CARE)
							.equalsIgnoreCase(uploadDocumentDTO
									.getPatientCareBenefitFlag())) {
						List<PatientCareDTO> patientCareList = claimRequestBenefitsService
								.getPatientCareDetails(uploadDocumentDTO
										.getPatientBenefitKey());
						if (null != patientCareList && !patientCareList.isEmpty()) {
							uploadDocumentDTO.setPatientCareDTO(patientCareList);
						}
						// rodDTO.getAddOnBenefitsDTO().setAdmittedNoOfDays(uploadDocumentDTO.getPatientCareNoofDays());
					}
				}
			}
			List<UploadDocumentDTO> rodBillSummaryDetails = rodService
					.getRODBillSummaryDetails(reimbursementObjectByKey.getKey(), mapper);
			
			if(rodBillSummaryDetails != null){
				for (UploadDocumentDTO uploadDocumentDTO2 : rodBillSummaryDetails) {
					uploadDocumentDTO2.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
				}
			}
			
			reimbursementDTO.getUploadDocDTO().setBillingWorkSheetUploadDocumentList(rodBillSummaryDetails);

			Double totalBilledAmount = reimbursementService
					.getTotalBilledAmount(documentSummaryKeys);

			reimbursementDTO.setAmountConsidered(totalBilledAmount != null ? String
					.valueOf(totalBilledAmount.intValue()) : "0");
			reimbursementDTO
					.setInitialAmountConsidered(totalBilledAmount != null ? String
							.valueOf(totalBilledAmount.intValue()) : "0");
			reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTask());
			reimbursementDTO.setKey(reimbursementObjectByKey.getKey());
			reimbursementDTO.setRodNumber(reimbursementObjectByKey.getRodNumber());

			setCategoryFlag(reimbursementObjectByKey, reimbursementDTO);
			
			if (!reimbursementDTO.getHospitalizaionFlag()) {
				reimbursementDTO = setHospitalizationAmount(reimbursementDTO);
			}

			List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
					.search(reimbursementObjectByKey.getClaim().getKey(),false);

			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

				previousPreAuthTableDTO.setRequestedAmt(preauthService
						.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
								previousPreAuthTableDTO.getClaimKey()));
				newList.add(previousPreAuthTableDTO);

			}
			
			

			reimbursementDTO.setPreviousPreauthTableDTO(newList);

		//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());
			reimbursementDTO.setStrUserName(tableDTO.getUsername());
			reimbursementDTO.setStrPassword(tableDTO.getPassword());
	        reimbursementDTO.setTaskNumber(tableDTO.getTaskNumber());
			
			
			reimbursementDTO.getPreauthMedicalDecisionDetails().setBillingRemarks(
					"");
			
			
			List<AddOnBenefitsDTO> populateAddOnBenefitsTableValues = claimRequestBenefitsService
					.populateAddOnBenefitsTableValues(
							reimbursementDTO.getNewIntimationDTO(),
							reimbursementObjectByKey.getClaim().getKey(),
							reimbursementObjectByKey.getKey(),
							reimbursementDTO.getProductCopay(),null);
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setAddOnBenefitsDTOList(populateAddOnBenefitsTableValues);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 8l, 9l, false,false);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 10l, 11l, true,false);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 85l, 84l, false,true);
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setDocAckknowledgement(
							reimbursementObjectByKey.getDocAcknowLedgement());

			if (reimbursementObjectByKey.getDocAcknowLedgement()
					.getHospitalizationRepeatFlag() != null
					&& reimbursementObjectByKey.getDocAcknowLedgement()
							.getHospitalizationRepeatFlag().toLowerCase()
							.equalsIgnoreCase("y")) {
				reimbursementDTO.setIsHospitalizationRepeat(true);
			}
			
			if (reimbursementObjectByKey.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)) {
				MedicalApprover medicalApprover = reimbursementService
						.getMedicalApprover(reimbursementObjectByKey.getKey());
				if (medicalApprover != null) {
					reimbursementDTO.setPreviousRemarks(medicalApprover
							.getApproverReply());
					reimbursementDTO.setPreviousReasonForReferring(medicalApprover
							.getReasonForReferring());
				}
				reimbursementDTO.setIsReferToBilling(true);
			}
			
			if (reimbursementObjectByKey.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_REFER_TO_CLAIM_APPROVAL)) {
				MedicalApprover medicalApprover = reimbursementService
						.getMedicalApprover(reimbursementObjectByKey.getKey());
				if (medicalApprover != null) {
					reimbursementDTO.setPreviousRemarks(medicalApprover
							.getApproverReply());
					reimbursementDTO.setPreviousReasonForReferring(medicalApprover
							.getReasonForReferring());
				}
				reimbursementDTO.setIsReferToClaimApproval(Boolean.TRUE);
			}
			
			if (claimByKey.getClaimType() != null
					&& claimByKey.getClaimType().getKey()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {

//				PreMedicalMapper premedicalMapper = new PreMedicalMapper();
				Preauth previousPreauth = getPreviousPreauth(claimByKey.getKey());
				
				if(previousPreauth != null) {
					reimbursementDTO.setPreauthKey(previousPreauth.getKey());
				}
			}
			
			if(claimByKey.getStatus() != null) {
				if(claimByKey.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) ||  claimByKey.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)) {
					reimbursementDTO.setIsWithDrawn(true);
				}
			}
			
			loadRRCRequestValues(reimbursementDTO,insuredSumInsured,SHAConstants.BILLING);
			
			Date policyFromDate = reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyFromDate();
			
			Date admissionDate = reimbursementDTO.getNewIntimationDTO().getAdmissionDate();
			
			Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
			if(diffDays != null && diffDays<90){
				reimbursementDTO.setIs64VBChequeStatusAlert(true);
			}
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance().get64VBStatus(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber(), reimbursementDTO.getNewIntimationDTO().getIntimationId());
				if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
					reimbursementDTO.setIsDishonoured(true);
				}  else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
					reimbursementDTO.setIsPending(true);
				}
			}
			reimbursementDTO.setSittingsAmount(dbCalculationService.getDialysisAmount(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(), insuredSumInsured.longValue()).intValue());
			Map<String, String> popupMessages = dbCalculationService.getPOPUPMessages(reimbursementDTO.getPolicyKey(), reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey(),reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
			reimbursementDTO.setPopupMap(popupMessages);
			
			
			if(reimbursementDTO.getNewIntimationDTO().getHospitalDto() != null && reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag() != null
					&& reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag().equalsIgnoreCase("Y")){
				if(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks() != null){
					Map<String, String> suspiciousMap = SHAUtils.getSuspiciousMap(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks());
					reimbursementDTO.setSuspiciousPopupMap(suspiciousMap);
				}
			}
			// If hospitalization is not processed then remaining ROD should be rejected.
			Reimbursement hospROD = reimbursementService.getHospitalizationROD(reimbursementObjectByKey.getClaim().getKey());
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
				if(hospROD == null) {
					reimbursementDTO.setIsHospitalizationRejected(true);
				}
			}
			
			Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
					.getIntimation().getHospital());
			
			paymentDetailsForfinancial(reimbursementObjectByKey, reimbursementDTO,
					hospitalById);
			
			setReimbursementValues(reimbursementObjectByKey, reimbursementDTO);
			
	       Reimbursement hospitalizationOrPartialROD = reimbursementService.getHospitalizationOrPartialROD(reimbursementObjectByKey.getClaim().getKey());
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				
				
				Integer seqNumber = 0;
				
				if(reimbursementObjectByKey.getRodNumber() != null){
					String[] split = reimbursementObjectByKey.getRodNumber().split("/");
					seqNumber = Integer.valueOf(split[split.length - 1]);
				}
				
				if(hospitalizationOrPartialROD == null && ! seqNumber.equals(1)) {
//					reimbursementDTO.setIsDishonoured(true);
					reimbursementDTO.setIsHospitalizationRejected(true);
				}
			}
			
			Map<String, Integer> productBenefitFlag = dbCalculationService.getProductBenefitFlag(reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey());
			
			if(productBenefitFlag.get(SHAConstants.PRE_HOSP_FLAG).equals(0)) {
				reimbursementDTO.setIsPreHospApplicable(false);
			}
			
			if(productBenefitFlag.get(SHAConstants.POST_HOSP_FLAG).equals(0)) {
				reimbursementDTO.setIsPostHospApplicable(false);
			}
			
			if(productBenefitFlag.get(SHAConstants.LUMP_SUM_FLAG).equals(1)) {
				reimbursementDTO.setIsLumpsumApplicable(true);
			}
			
			if(productBenefitFlag.get(SHAConstants.HOSPITALCASH_FLAG).equals(1)) {
				reimbursementDTO.setIsHospitalCashApplicable(true);
			}
			
			if(productBenefitFlag.get(SHAConstants.PATIENTCARE_FLAG).equals(1)) {
				reimbursementDTO.setIsPatientCareApplicable(true);
			}
			
			
			MastersValue networkHospitalType = masterService.getMaster(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getNetworkHospitalTypeId());
			reimbursementDTO.setNetworkHospitalType(networkHospitalType.toString());
			
			reimbursementDTO = checkFinalEnhancement(reimbursementDTO);

			
			try {
				if((reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest() != null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest().equalsIgnoreCase("Y"))
						|| (reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))) {
					if(reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag() ==  null || reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag().equalsIgnoreCase("N")) {
						reimbursementDTO.setIsReconsiderationRequest(true);
						if(reimbursementService.isClaimPaymentAvailable(reimbursementObjectByKey.getRodNumber())) {
							Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
							reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
						} else {
							/**
							 * For reject reconsider, cancel rod button was enabled. But for this ticket GALAXYMAIN-6222	
							 * which was raised in PA, now cancel rod button is diabled for rejection case to.
							 * Hence below code was commented.
							 * */
							//reimbursementDTO.setIsReconsiderationRequest(false);
						}
//						Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
//						reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
					}
					
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				reimbursementDTO.setIsCashlessType(true);
			}
			if(reimbursementDTO.getIsCashlessType() && reimbursementDTO.getHospitalizaionFlag() && (reimbursementDTO.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY))){
				reimbursementDTO.setIsDirectToBilling(true);
				Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursementDTO.getClaimDTO().getKey());
				reimbursementDTO.getClaimDTO().setLatestPreauthKey(latestPreauth.getKey());
			}
			reimbursementDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(reimbursementDTO.getNewIntimationDTO().getAdmissionDate(), reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));
			reimbursementDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey()));
			reimbursementDTO.setIsPEDInitiatedForBtn(reimbursementDTO.getIsPEDInitiated());
			
			List<InsuredPedDetails> pedByInsured = policyService.getPEDByInsured(reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			reimbursementDTO.setInsuredPedDetails(pedByInsured);
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				if(reimbursementDTO.getHospitalizaionFlag()) {
					Reimbursement partialHospitalizationRODWithSettled = getPartialHospitalizationRODWithSettled(claimByKey.getKey());
					ReimbursementCalCulationDetails hosptialization = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursementDTO.getKey(), ReferenceTable.HOSPITALIZATION);
					if(hosptialization != null) {
						reimbursementDTO.setBillingApprovedAmount(hosptialization.getPayableToHospAftTDS() != null ? hosptialization.getPayableToHospAftTDS().doubleValue() : 0d);
						reimbursementDTO.setPayableToHospAmt(hosptialization.getPayableToHospital() != null ? hosptialization.getPayableToHospital().doubleValue() : 0d);
						reimbursementDTO.setHospDiscountAmount(hosptialization.getHospitalDiscount() != null ? hosptialization.getHospitalDiscount().doubleValue() : 0d);
					}
					if(partialHospitalizationRODWithSettled != null) {
						ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(partialHospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
						reimbursementDTO.setPayableToInsAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium().doubleValue() : 0d);
						reimbursementDTO.setFAApprovedAmount(partialHospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? partialHospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
					}
				} else if(reimbursementDTO.getPartialHospitalizaionFlag()) {
					Reimbursement hospitalizationRODWithSettled = getHospitalizationRODWithSettled(claimByKey.getKey());
					if(hospitalizationRODWithSettled != null) {
						ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(hospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
						reimbursementDTO.setPayableToHospAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital().doubleValue() : 0d);
						reimbursementDTO.setHospDiscountAmount(reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d);
						Double amount = (reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d) + (hospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? hospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
						reimbursementDTO.setFAApprovedAmount(amount);
					}
				}
			}
			
			Boolean queryReceivedStatusRod = reimbursementQuerySerice.isQueryReceivedStatusRod(reimbursementObjectByKey.getKey());
			 
			 if(queryReceivedStatusRod){
				 reimbursementDTO.setIsQueryReceived(true);
			 }
			 
			 
				List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = acknowledgementDocumentsReceivedService.getReimbursementCalculationDetails(reimbursementObjectByKey.getKey());
				if(reimbursementCalculationDetails != null && !reimbursementCalculationDetails.isEmpty()) {
					for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
						if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
								reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.HOSPITALIZATION)){
							
							HopitalizationCalulationDetailsDTO hospitalizationCalcDTO = getHospitalizationCalculationDTO(reimbursementCalCulationDetails2,reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
							if(reimbursementDTO.getDocumentReceivedFromId() != null && reimbursementDTO.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
						           hospitalizationCalcDTO.setBalanceToBePaid(0);
							}
							reimbursementDTO.setHospitalizationCalculationDTO(hospitalizationCalcDTO);
							
						}
						else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
								reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)){
							PreHopitalizationDetailsDTO preHospitalizationCalcDTO = getPreHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
							reimbursementDTO.setPreHospitalizationCalculationDTO(preHospitalizationCalcDTO);
							
						}else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
								reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
							PostHopitalizationDetailsDTO postHospitalizationCalcDTO = getPostHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, reimbursementObjectByKey.getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING) ? false : true);
							reimbursementDTO.setPostHospitalizationCalculationDTO(postHospitalizationCalcDTO);
						}
					}
				}
			
				reimbursementDTO.setAmountConsidered(reimbursementDTO.getAmountConsidedAfterCoPay() != null ? String
						.valueOf(reimbursementDTO.getAmountConsidedAfterCoPay().intValue()) : "0");
			 
//			 reimbursementService.setBPMForClaimBilling(reimbursementDTO, false, "MEDICAL", reimbursementObjectByKey);
				Double totalClaimedAmount = reimbursementService.getTotalClaimedAmount(reimbursementObjectByKey);
				 reimbursementDTO.setRodTotalClaimedAmount(totalClaimedAmount);
				 
				 if(reimbursementDTO.getAmountConsidedAfterCoPay() != null && totalBilledAmount != null && !reimbursementDTO.getAmountConsidedAfterCoPay().equals(totalBilledAmount)) {
					 reimbursementDTO.setAmountConsidered(String.valueOf(totalBilledAmount.longValue()));
				 }

				 //SHAUtils.setDefaultCopayValue(reimbursementDTO);

				 if(reimbursementDTO.getIsReconsiderationRequest() != null && reimbursementDTO.getIsReconsiderationRequest()){
					 reimbursementDTO.setIsReverseAllocation(false);
				 }

				 if(reimbursementObjectByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
						 && reimbursementObjectByKey.getDocAcknowLedgement() != null && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
						 && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
				 
						 if(hospitalById != null){
								Boolean hospitalDiscount = SHAUtils.isHospitalDiscount(hospitalById);
								reimbursementDTO.setIsHospitalDiscountApplicable(hospitalDiscount);
						 }
				 }
				 
				 	String faApproverRemarks = reimbursementObjectByKey.getFinancialApprovalRemarks();
				 	if(faApproverRemarks!=null){
				 		reimbursementDTO.getPreauthDataExtractionDetails().setFinancialApprovalMedicalRemarks(faApproverRemarks);
				 	}
					//12345
					String medicalRemarks = reimbursementObjectByKey.getApprovalRemarks();
					if(medicalRemarks!=null){
						reimbursementDTO.getPreauthDataExtractionDetails().setMedicalRemarks(medicalRemarks);
						
					}
					//reimbursementDTO.getPreauthDataExtractionDetails().setMedicalRemarks(medicalApprover.getApproverReply());
				 
			/*	 if (reimbursementObjectByKey.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)) {
						
						MedicalApprover medicalApprover = pareimbursementService.getMedicalApprover(reimbursementObjectByKey.getKey());
						if(null != medicalApprover)
						{
							reimbursementDTO.getPreauthDataExtractionDetails().setBillingMedicalReason(medicalApprover.getReasonForReferring());
							reimbursementDTO.getPreauthDataExtractionDetails().setMedicalRemarks(medicalApprover.getApproverReply());
							reimbursementDTO.setPreviousRemarks(medicalApprover.getReferringRemarks());
							reimbursementDTO.setPreviousReasonForReferring(medicalApprover.getReasonForReferring());
						}
						
					}
				 
				  // Long latestMedicalApproverForReFinancial = pareimbursementService.getLatestMedicalApproverForReFinancial(reimbursementObjectByKey.getKey());
			       // if(latestMedicalApproverForReFinancial!=null){
			        	 MedicalApprover claimMedicalApprover =  pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(), SHAConstants.PA_CLAIM_REQUEST_REFINANCE);
							//MedicalApprover medicalApprover = pareimbursementService.getMedicalApprover(reimbursementObjectByKey.getKey());
							if(null != claimMedicalApprover)
							{
								//reimbursementDTO.getPreauthDataExtractionDetails().setFinancialApprovalMedicalReason(reimbursementObjectByKey.getReasonForReferring());
				 				String faApproverRemarks = reimbursementDTO.getPreauthDataExtractionDetails().getFaApproverRemarks();
								reimbursementDTO.getPreauthDataExtractionDetails().setFinancialApprovalMedicalRemarks(faApproverRemarks);
								//reimbursementDTO.setPreviousRemarks(claimMedicalApprover.getReferringRemarks());
								//reimbursementDTO.setPreviousReasonForReferring(claimMedicalApprover.getReasonForReferring());
							//}
			        //}
			       
					
			//	}
			 
			 if (reimbursementObjectByKey.getStatus().getKey()
						.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)) {
			        Long latestMedicalApproverForReClaim = pareimbursementService.getLatestMedicalApproverForReClaim(reimbursementObjectByKey.getKey());
			        if(latestMedicalApproverForReClaim!=null){
			        	MedicalApprover medicalApprover = pareimbursementService.getLatestMedicalApproverKeywithRecordType(reimbursementObjectByKey.getKey(), SHAConstants.PA_CLAIM_REQUEST_RECLAIM);
						if(null != medicalApprover)
						{
							//reimbursementDTO.getPreauthDataExtractionDetails().setClaimApprovalMedicalReason(medicalApprover.getReasonForReferring());
							reimbursementDTO.getPreauthDataExtractionDetails().setMedicalRemarks(medicalApprover.getApproverReply());
							reimbursementDTO.setPreviousRemarks(medicalApprover.getReferringRemarks());
							reimbursementDTO.setPreviousReasonForReferring(medicalApprover.getReasonForReferring());
						}
			     //   }
				 
//				 if(reimbursementDTO.getNewIntimationDTO() != null && reimbursementDTO.getNewIntimationDTO().getInsuredPatient() != null &&
//						 reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey() != null){
//						Long claimCount = preauthService.getClaimCount(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
//						if(claimCount != null){
//							reimbursementDTO.setClaimCount(claimCount);
//						}
//					}
*/						
						
				 List<TableBenefitsDTO> paBenefitsListByRodKey = paSearchProcessClaimBillingService.getPABenefitsListByRodKey(reimbursementObjectByKey.getKey());
				 
				 reimbursementDTO.getPreauthDataExtractionDetails().setBenefitDTOList(paBenefitsListByRodKey);
				 
				MastersValue benefitsId = reimbursementObjectByKey.getBenefitsId();
				Insured insuredKey = reimbursementObjectByKey.getInsuredKey();
				 if(benefitsId!=null && insuredKey!=null){
					 
					 BeanItemContainer<SelectValue> coverContainer = null;
					 
					 if(null != insuredKey && null != insuredKey.getPolicy() && null != insuredKey.getPolicy().getProduct() &&
							 null != insuredKey.getPolicy().getProduct().getKey() && !(ReferenceTable.getGPAProducts().containsKey(insuredKey.getPolicy().getProduct().getKey()))){						 
				
						 coverContainer = dbCalculationService.getBenefitCoverValueContainer(insuredKey.getKey(), benefitsId.getKey());
					 }
					 else
					 {
						  coverContainer = dbCalculationService.getGPABenefitCoverValueContainer(insuredKey.getKey(), benefitsId.getKey());
					 }
					 PreauthDataExtaractionDTO preauthDataExtractionDetails = reimbursementDTO.getPreauthDataExtractionDetails();
					 preauthDataExtractionDetails.setCoverListContainer(coverContainer.getItemIds());
					 
					 reimbursementDTO.getPreauthDataExtractionDetails().setBenefitsValue(benefitsId.getValue());
					
				 }
				 
				 Double alreadyPaidAmount = paSearchProcessClaimBillingService.getAlreadyPaidAmount(reimbursementObjectByKey.getRodNumber());
				 reimbursementDTO.getPreauthDataExtractionDetails().setAlreadyPaidAmt(alreadyPaidAmount);
				 
				 List<OptionalCoversDTO> paOptionalCoverListByRodKey = paSearchProcessClaimBillingService.getPAOptionalCoverListByRodKey(reimbursementObjectByKey);

				 reimbursementDTO.getPreauthDataExtractionDetails().setOptionalCoversTableListBilling(paOptionalCoverListByRodKey);
				 
				 Product product = reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct();
				 BeanItemContainer<SelectValue> bebefitAdditionalCovers = null;
				 
				 if(null != product && null != product.getKey() && !(ReferenceTable.getGPAProducts().containsKey(product.getKey())))
					{
					 bebefitAdditionalCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.OPTIONAL_COVER, product.getKey(),insuredKey.getKey());
					}
				 else
				 {
					 bebefitAdditionalCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.GPA_OPTIONAL_COVER , product.getKey(),insuredKey.getKey());
				 }
				 PreauthDataExtaractionDTO preauthDataExtractionDetails = reimbursementDTO.getPreauthDataExtractionDetails();
				 preauthDataExtractionDetails.setOptionalCoverListContainer(bebefitAdditionalCovers.getItemIds());			
			
				 List<AddOnCoverOnLoadDTO> coverNameList= new ArrayList<AddOnCoverOnLoadDTO>();
				 List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> paAddOnCoverListByRodKey = paSearchProcessClaimBillingService.getAddOnCoverListByRodKey(reimbursementObjectByKey.getKey(),product.getKey(),coverNameList);
				
				 reimbursementDTO.getPreauthDataExtractionDetails().setAddOnCoversTableListBilling(paAddOnCoverListByRodKey);
				 
				if(!coverNameList.isEmpty()){
					reimbursementDTO.getPreauthDataExtractionDetails().setAddOnCoverNameList(coverNameList);
				}
				 
				 BeanItemContainer<SelectValue> addOnCovers = null;
				if(null != product && null != product.getKey() && !(ReferenceTable.getGPAProducts().containsKey(product.getKey())))
				{
					addOnCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.ADDITIONAL_COVER, product.getKey(),insuredKey.getKey());
				}
				else
				{
					 addOnCovers = dbCalculationService.getBebefitAdditionalCovers(SHAConstants.GPA_ADDITIONAL_COVER, product.getKey(),insuredKey.getKey());
				}
				 preauthDataExtractionDetails.setAddOnCoverListContainer(addOnCovers.getItemIds());
				 
				 Double balanceSI = 0d;
				 
					if(null != product && null != product.getKey() && !(ReferenceTable.getGPAProducts().containsKey(product.getKey())))
					{
					
						balanceSI = dbCalculationService.getPABalanceSI(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey(), reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getKey(),null != reimbursementObjectByKey.getBenefitsId() ? reimbursementObjectByKey.getBenefitsId().getKey() : 0l);
					}
					else
					{
						balanceSI = dbCalculationService.getGPAAvailableSI(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey(), reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getKey(),null != reimbursementObjectByKey.getBenefitsId() ? reimbursementObjectByKey.getBenefitsId().getKey() : 0l);
					}
				 
				 /* if(reimbursementObjectByKey.getDocAcknowLedgement()!=null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest()!=null 
						 && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest())){
					 	balanceSI = balanceSI + reimbursementObjectByKey.getFinancialApprovedAmount();
				 }*/
					
					if((reimbursementObjectByKey.getDocAcknowLedgement()!=null && reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest()!=null 
							 && SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementObjectByKey.getDocAcknowLedgement().getReconsiderationRequest()))
							 || (reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y"))){
						 
						 ClaimPayment claimPayment = paReimbursementService.getClaimPayment(reimbursementObjectByKey.getRodNumber());
						 if(null != claimPayment && claimPayment.getPaBenefitApproveAmount() != null){
						 	balanceSI = balanceSI + claimPayment.getPaBenefitApproveAmount();
						 }
					 }
					reimbursementDTO.getPreauthDataExtractionDetails().setAvailableSI(null != balanceSI ? String.valueOf(balanceSI) : null);
				 
				 BeanItemContainer<SelectValue> benefitsValueContainer =  masterService.getSelectValueContainerForBenefits(ReferenceTable.MASTER_TYPE_CODE_BENEFITS,reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey());
				 
				 reimbursementDTO.getPreauthDataExtractionDetails().setBenefitsValueContainer(benefitsValueContainer.getItemIds());
				 
				 if(null != reimbursementDTO.getRodNumber()){
						ClaimPayment claimPayment = acknowledgementDocumentsReceivedService.getClaimPaymentDetails(reimbursementDTO.getRodNumber());
						if(null != claimPayment && null != claimPayment.getStatusId() && claimPayment.getStatusId().getKey().equals(ReferenceTable.PAYMENT_SETTLED)){
							reimbursementDTO.setIsPaymentSettled(true);
						}
					}
				 
					reimbursementDTO.setDbOutArray(tableDTO.getDbOutArray());
					
					LegalHeirDTO legalHeirDTO = new LegalHeirDTO();
					legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
					reimbursementDTO.setLegalHeirDto(legalHeirDTO);

					List<NomineeDetailsDto> nomineeDtoList = intimationService.getNomineeDetailsListByTransactionKey(tableDTO.getRodKey());
					if( nomineeDtoList != null && !nomineeDtoList.isEmpty()){
						reimbursementDTO.getNewIntimationDTO().setNomineeList(nomineeDtoList);
					}
					if(reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null 
							&& reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()) {				
						List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(tableDTO.getRodKey());
						if(legalHeirList != null && !legalHeirList.isEmpty()) {
							List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
							LegalHeirDTO legalHeirDto;
							for (LegalHeir legalHeir : legalHeirList) {
								 legalHeirDto = new LegalHeirDTO(legalHeir);
								 legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
								 legalHeirDTOList.add(legalHeirDto);
							}
							reimbursementDTO.setLegalHeirDTOList(legalHeirDTOList);
						}
					}	
					
					/*for bancs*/
				if(reimbursementObjectByKey.getCatastrophicLoss() != null) {
					
					SelectValue value = masterService.getCatastropheData(reimbursementObjectByKey.getCatastrophicLoss());
					reimbursementDTO.getPreauthDataExtractionDetails().setCatastrophicLoss(value);
				}
				if(reimbursementObjectByKey.getNatureOfLoss() != null) {
					
					SelectValue value = masterService.getMasterValueForNatureCause(reimbursementObjectByKey.getNatureOfLoss());
					reimbursementDTO.getPreauthDataExtractionDetails().setNatureOfLoss(value);
				}
				if(reimbursementObjectByKey.getCauseOfLoss() != null) {
					SelectValue value = masterService.getMasterValueForNatureCause(reimbursementObjectByKey.getCauseOfLoss());
					reimbursementDTO.getPreauthDataExtractionDetails().setCauseOfLoss(value);
				}
				if(reimbursementObjectByKey.getClaim().getLegalClaim() !=null
						 && reimbursementObjectByKey.getClaim().getLegalClaim().equals("Y")){
					 LegalTaxDeduction taxDeduction = reimbursementService.getTaxDeductionFromRODKey(reimbursementObjectByKey.getKey());
					 if(taxDeduction !=null){
						 LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
						 LegalBillingDTO billingDTO = taxDeductionMapper.getDTOFromTaxDeduction(taxDeduction);
						 billingDTO = reimbursementService.calculateLegalSettlment(billingDTO);
						 reimbursementDTO.setLegalBillingDTO(billingDTO);
					 }
				 }
				 
				Policy policy = reimbursementDTO.getNewIntimationDTO().getPolicy();
				// added for installment payment process in policy level and product
				// level check
				Map<String, String> getPolicyInstallment = dbCalculationService
						.getInstallmentPaymentFlag(policy.getPolicyNumber(), policy.getProduct()
								.getKey());
				if (getPolicyInstallment != null && !getPolicyInstallment.isEmpty()) {
					reimbursementDTO.setPolicyInstalmentFlag(getPolicyInstallment
							.get(SHAConstants.FLAG) != null ? getPolicyInstallment
							.get(SHAConstants.FLAG) : "N");
					reimbursementDTO
							.setPolicyInstalmentMsg(getPolicyInstallment
									.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallment
									.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
				}
				if (reimbursementDTO.getPolicyInstalmentFlag() != null
						&& reimbursementDTO.getPolicyInstalmentFlag().equals(
								SHAConstants.YES_FLAG)) {
					Integer policyInstallmentAmount = PremiaService.getInstance()
							.getPolicyInstallmentAmount(policy
									.getPolicyNumber());
					reimbursementDTO.setPolicyInstalmentPremiumAmt(policyInstallmentAmount
							.doubleValue());
					
					String policyDueDate = PremiaService.getInstance()
							.getPolicyInstallmentDetails(policy
									.getPolicyNumber());
					//code added if we recevied instalment due amount as zero after pending amount adjusted to premia/bancs  need to set due date as admission date by noufel
		 			if((reimbursementDTO.getPolicyInstalmentPremiumAmt() != null && reimbursementDTO.getPolicyInstalmentPremiumAmt() == 0d) && 
		 					(policyDueDate == null || policyDueDate.isEmpty())) {
		 				
		 				reimbursementDTO.setPolicyInstalmentDueDate(reimbursementDTO.getPreauthDataExtractionDetails().getAdmissionDate());	
		 			}
		 			else{
		 			if (reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
		 				reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.formatTimeFromString(policyDueDate.toString()));	
		    			}
		    			else{
		    				reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.dateformatFromString(policyDueDate.toString()));
		    			}
		 			}
				}
				reimbursementDTO.setScreenName(SHAConstants.PA_CLAIM_APPROVAL_NON_HOSP);
				 view.setPAProcessClaimApproval(PAClaimAprNonHosWizard.class,reimbursementDTO);
		}
		}
		
		
		protected void showPANonHospFinancialClaimBillingWizard(
				@Observes @CDIEvent(PAMenuPresenter.SHOW_PA_NON_HOSP_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN) final ParameterDTO parameters) {
			
			SearchProcessClaimFinancialsTableDTO tableDTO = (SearchProcessClaimFinancialsTableDTO) parameters
					.getPrimaryParameter();

			Long ackDocKey = createRodService
					.getLatestDocAcknowledgementKey(tableDTO.getRodKey());
				
					showPAProcessFinancialNonHospWizard(tableDTO);
			
		}
		
		protected void showFinancialClaimBillingWizard(
				@Observes @CDIEvent(PAMenuPresenter.SHOW_PA_HEALTH_FINANCIAL_APPROVAL_CLAIM_BILLING_SCREEN) final ParameterDTO parameters) {

			PAHealthSearchProcessClaimFinancialsTableDTO tableDTO = (PAHealthSearchProcessClaimFinancialsTableDTO) parameters
					.getPrimaryParameter();

			Reimbursement reimbursementObjectByKey = rodService
					.getReimbursementObjectByKey(tableDTO.getRodKey());
			List<UploadDocumentDTO> rodSummaryDetails = rodService
					.getRODSummaryDetails(reimbursementObjectByKey.getKey());
			
			
			Boolean isValidClaimForFA = true;
			if(null != reimbursementObjectByKey)
			{
				Claim objClaim = reimbursementObjectByKey.getClaim();
				if(null != objClaim)
				{
					if((SHAConstants.YES_FLAG).equalsIgnoreCase(objClaim.getLegalFlag()))
					{
						isValidClaimForFA = false;
						view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
					}
				}
			}
			if(isValidClaimForFA)
			{
			

			/*
			 * List<Long> documentSummaryKeys = new ArrayList<Long>(); for
			 * (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
			 * documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
			 * uploadDocumentDTO.setStatus(true); List<RODBillDetails>
			 * billEntryDetails = rodService
			 * .getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
			 * List<BillEntryDetailsDTO> dtoList = new
			 * ArrayList<BillEntryDetailsDTO>(); if (billEntryDetails != null &&
			 * !billEntryDetails.isEmpty()) { for (RODBillDetails billEntryDetailsDO
			 * : billEntryDetails) {
			 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
			 * uploadDocumentDTO)); } }
			 * uploadDocumentDTO.setBillEntryDetailList(dtoList);
			 * 
			 * }
			 * 
			 * Double totalBilledAmount = reimbursementService
			 * .getTotalBilledAmount(documentSummaryKeys);
			 */
			ZonalMedicalReviewMapper mapper = ZonalMedicalReviewMapper.getInstance();
//			ZonalMedicalReviewMapper.getAllMapValues();
			PreauthDTO reimbursementDTO = mapper
					.getReimbursementDTO(reimbursementObjectByKey);
//			MastersValue master = masterService.getMaster(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getNetworkHospitalTypeId());
//			reimbursementDTO.getNewIntimationDTO().getHospitalDto().setNetworkHospitalType(master.);
			/*if(("Y").equalsIgnoreCase(reimbursementDTO.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()))
			{
				reimbursementDTO.setReconsiderRodRequestList(getReconsiderRODRequestForBillEntry(tableDTO.getRodKey()));
			}*/
			
			reimbursementDTO.getPreauthDataExtractionDetails().setBillingInternalRemarks(reimbursementObjectByKey.getBillingInternalRemarks());
			reimbursementDTO.getPreauthDataExtractionDetails().setFaInternalRemarks(reimbursementObjectByKey.getFaInternalRemarks());
			if(("Y").equalsIgnoreCase(reimbursementDTO.getPreauthDataExtractionDetails().getReconsiderationFlag()))
			{
				List<ReconsiderRODRequestTableDTO> reconsiderDTOList = getReconsiderRODRequestForBillEntry(tableDTO.getRodKey());
				if(null != reconsiderDTOList && !reconsiderDTOList.isEmpty())
				{
					for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reconsiderDTOList) {
						if(null != reconsiderRODRequestTableDTO.getIsRejectReconsidered() && reconsiderRODRequestTableDTO.getIsRejectReconsidered())
						{
							reimbursementDTO.setIsRejectReconsidered(true);
						}
						else
						{
							reimbursementDTO.setIsRejectReconsidered(false);
						}
					}
				}
				reimbursementDTO.setReconsiderRodRequestList(getReconsiderRODRequestForBillEntry(tableDTO.getRodKey()));
			}
			
			/*
			 * reimbursementDTO.setAmountConsidered(totalBilledAmount != null ?
			 * String .valueOf(totalBilledAmount.intValue()) : "0");
			 * reimbursementDTO .setInitialAmountConsidered(totalBilledAmount !=
			 * null ? String .valueOf(totalBilledAmount.intValue()) : "0");
			 * reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
			 * reimbursementDTO.setHumanTask(tableDTO.getHumanTask());
			 * reimbursementDTO.setHumanTask(tableDTO.getHumanTask());
			 * reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());
			 * reimbursementDTO.setStrUserName(tableDTO.getUsername());
			 * reimbursementDTO.setStrPassword(tableDTO.getPassword());
			 */

			Claim claimByKey = reimbursementObjectByKey.getClaim();
			reimbursementDTO
					.setReconsiderationList(getReconsiderRODRequest(claimByKey));
			
			if(null != claimByKey)
			{
				if(null != reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId())
				{
					String  docReceivedFrom = reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue();
					reimbursementDTO.setPreviousAccountDetailsList(populatePreviousAccountDetails(claimByKey.getIntimation().getIntimationId(),docReceivedFrom));
				}
			}

			setCategoryFlag(reimbursementObjectByKey, reimbursementDTO);
			

			if (!reimbursementDTO.getHospitalizaionFlag()) {
				reimbursementDTO = setHospitalizationAmount(reimbursementDTO);
			}

			reimbursementDTO = setReimbursmentTOPreauthDTO(mapper, claimByKey,
					reimbursementObjectByKey, reimbursementDTO, true, SHAConstants.FINANCIAL);
			reimbursementDTO = getResiudalAmountToDTO(reimbursementDTO,
					reimbursementObjectByKey.getKey());
			
			reimbursementDTO.setBillingApprovedDate(reimbursementObjectByKey.getBillingCompletedDate());

			// Added for amount claimed table enhancement --- stops
			DBCalculationService dbCalculationService = new DBCalculationService();
			
			Double insuredSumInsured = 0d;
			
			if(null != claimByKey &&  null != claimByKey.getIntimation() && 
					null != claimByKey.getIntimation().getPolicy() && null != claimByKey.getIntimation().getPolicy().getProduct() &&
					null != claimByKey.getIntimation().getPolicy().getProduct().getKey() 
					&& !(ReferenceTable.getGPAProducts().containsKey(claimByKey.getIntimation().getPolicy().getProduct().getKey())))
			{
			
			 insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), reimbursementDTO
							.getPolicyDto().getKey(),reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			}
			else
			{
				if(null != reimbursementObjectByKey && null != reimbursementObjectByKey.getBenefitsId() && 
						null != reimbursementObjectByKey.getBenefitsId().getKey() &&
						((ReferenceTable.HOSP_BENEFIT_MASTER_VALUE.equals(reimbursementObjectByKey.getBenefitsId().getKey())) ||
							((ReferenceTable.PART_BENEFIT_MASTER_VALUE.equals(reimbursementObjectByKey.getBenefitsId().getKey()))))){		
					
					insuredSumInsured = dbCalculationService.getGPAInsuredSumInsuredForHospitalization(
							reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
									.getInsuredId().toString(), reimbursementDTO
									.getPolicyDto().getKey(),reimbursementObjectByKey.getBenefitsId().getKey());
				}
				else
				{
					insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
							reimbursementDTO.getNewIntimationDTO().getInsuredPatient()
									.getInsuredId().toString(), reimbursementDTO
									.getPolicyDto().getKey());
				}
			}

			reimbursementDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
			
			Map<Integer, Object> detailsMap = new HashMap<Integer, Object>();
			
//			if(null != reimbursementDTO && null != reimbursementDTO.getNewIntimationDTO() && null != reimbursementDTO.getNewIntimationDTO().getHospitalDto() 
//					&& null != reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() && null != reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType()
//					&& reimbursementDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalType().getKey().equals(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID))
//			{
//				String cityClass = createRodService.getHospitalCityClass(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
//				reimbursementDTO.getNewIntimationDTO().getHospitalDto()
//				.getRegistedHospitals().setCityClass(cityClass);
//				
//			}

			if(reimbursementObjectByKey.getSectionCategory() != null){
				
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
								.getProduct().getKey(), insuredSumInsured,
								reimbursementDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
								reimbursementDTO.getNewIntimationDTO()
										.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),reimbursementObjectByKey.getSectionCategory(),"A");
			}else{
				
				Long sectionCategory = 0l;
				if(reimbursementDTO.getPolicyDto()
									.getProduct().getKey().equals(ReferenceTable.STAR_CARDIAC_CARE_POLICY) || reimbursementDTO.getPolicyDto()
									.getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_POLICY) || reimbursementDTO.getPolicyDto()
									.getProduct().getKey().equals(ReferenceTable.DIABETES_FLOATER_POLICY))
				{
					sectionCategory = 1l;
				}
				
				detailsMap = dbCalculationService
						.getHospitalizationDetails(reimbursementDTO.getPolicyDto()
								.getProduct().getKey(), insuredSumInsured,
								reimbursementDTO.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals().getCityClass(),
								reimbursementDTO.getNewIntimationDTO()
										.getInsuredPatient().getInsuredId(), reimbursementDTO.getNewIntimationDTO().getKey(),sectionCategory,"0");
			}
			
			
//			if(detailsMap != null && detailsMap.get(15) != null){
//				reimbursementDTO.setAmbulanceLimitAmount((Double)detailsMap.get(15));
//			}
			
			List<Double> copayValue = dbCalculationService.getProductCoPay(reimbursementDTO
					.getClaimDTO().getNewIntimationDto().getPolicy().getProduct()
					.getKey(), reimbursementDTO.getClaimDTO().getNewIntimationDto()
					.getInsuredPatient().getKey(),reimbursementDTO.getClaimDTO().getNewIntimationDto()
					.getInsuredPatient().getInsuredId(),reimbursementDTO.getNewIntimationDTO());
			reimbursementDTO.setProductCopay(copayValue);

			List<Long> documentSummaryKeys = new ArrayList<Long>();
			
			/**
			 * Fix for implemeting claims dms in bill entry screen.. -- starts
			 * **/
			if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
			{
				for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
					if(null != claimByKey)
					{
						uploadDocumentDTO.setClaimNo(claimByKey.getClaimId());
						uploadDocumentDTO.setDmsDocumentDTOList(getClaimsDMSList(claimByKey.getIntimation().getIntimationId()));
					}
				}
			}
			
			/**
			 * Added for enabling view documents in preauth screen while raising query -- fix starts.
			 * */
			if(null != claimByKey)
			{
				reimbursementDTO.setDmsDocumentDTOList(getClaimsDMSList(claimByKey.getIntimation().getIntimationId()));
			}
			/**
			 * Added for enabling view documents in preauth screen while raising query -- fix ends
			 * */

			for (UploadDocumentDTO uploadDocumentDTO : rodSummaryDetails) {
				
				if(null != reimbursementObjectByKey)
				{
					uploadDocumentDTO.setIntimationNo(reimbursementObjectByKey.getClaim().getIntimation().getIntimationId());
					uploadDocumentDTO.setDateOfAdmission(SHAUtils.formatDate(reimbursementObjectByKey.getDateOfAdmission()));
					uploadDocumentDTO.setDateOfDischarge(SHAUtils.formatDate(reimbursementObjectByKey.getDateOfDischarge()));
					uploadDocumentDTO.setInsuredPatientName(reimbursementObjectByKey.getClaim().getIntimation().getInsuredPatientName());
				}
				
				documentSummaryKeys.add(uploadDocumentDTO.getDocSummaryKey());
				uploadDocumentDTO.setStatus(true);
				List<RODBillDetails> billEntryDetails = rodService
						.getBillEntryDetails(uploadDocumentDTO.getDocSummaryKey());
				List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
				if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
					for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
						/*
						 * <<<<<<< HEAD
						 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
						 * uploadDocumentDTO)); =======
						 */
						dtoList.add(getBillDetailsDTOForBilling(billEntryDetailsDO,
								uploadDocumentDTO));
						// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
					}
				}
				/*
				 * uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
				 * .get(8));
				 */
				uploadDocumentDTO.setProductBasedRoomRent((Double) detailsMap
						.get(8));
				uploadDocumentDTO
						.setProductBasedICURent((Double) detailsMap.get(9));
				/*uploadDocumentDTO.setProductBasedAmbulanceAmt((Double) detailsMap
						.get(15));*/
				uploadDocumentDTO.setProductBasedAmbulanceAmt(getProductBasedAmbulanceAmt(tableDTO.getRodKey()));
				uploadDocumentDTO.setStatus(false);
				uploadDocumentDTO.setBillEntryDetailList(dtoList);
				uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
				uploadDocumentDTO.setRodKey(reimbursementObjectByKey.getKey());

				uploadDocumentDTO.setStrUserName(tableDTO.getUsername());
			}
			List<UploadDocumentDTO> rodBillSummaryDetails = rodService
					.getRODBillSummaryDetails(reimbursementObjectByKey.getKey(), mapper);
			if(rodBillSummaryDetails != null){
			for (UploadDocumentDTO uploadDocumentDTO : rodBillSummaryDetails) {
				uploadDocumentDTO.setClaimType(reimbursementDTO.getClaimDTO().getClaimType());
			}
			}
			reimbursementDTO.getUploadDocDTO().setBillingWorkSheetUploadDocumentList(rodBillSummaryDetails);

			Double totalBilledAmount = reimbursementService
					.getTotalBilledAmount(documentSummaryKeys);
			reimbursementDTO.setAmountConsidered(totalBilledAmount != null ? String
					.valueOf(totalBilledAmount.intValue()) : "0");
			reimbursementDTO
					.setInitialAmountConsidered(totalBilledAmount != null ? String
							.valueOf(totalBilledAmount.intValue()) : "0");
			reimbursementDTO.setUploadDocumentDTO(rodSummaryDetails);
		//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTask());
		//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTask());
		//	reimbursementDTO.setRodHumanTask(tableDTO.getHumanTaskDTO());
			reimbursementDTO.setStrUserName(tableDTO.getUsername());
			reimbursementDTO.setStrPassword(tableDTO.getPassword());
			reimbursementDTO.setTaskNumber(tableDTO.getTaskNumber());
			// Added for amount claimed table enhancement --- stops

			Hospitals hospitalById = hospitalService.getHospitalById(claimByKey
					.getIntimation().getHospital());
			ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = reimbursementDTO
					.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
			if(hospitalById != null){
				
				updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				updateHospitalDetails.setHospitalCity(hospitalById.getCity());
				updateHospitalDetails.setHospitalState(hospitalById.getState());
				updateHospitalDetails.setHospitalCode(hospitalById.getHospitalCode());
				updateHospitalDetails.setHospitalName(hospitalById.getName());
				updateHospitalDetails.setPanNumber(hospitalById.getPanNumber());
				updateHospitalDetails.setHospitalAddress1(hospitalById.getAddress());
				updateHospitalDetails.setHopitalRegNumber(hospitalById.getRegistrationNumber());
				updateHospitalDetails.setHospitalPhoneNo(hospitalById.getPhoneNumber());
				updateHospitalDetails.setHospitalTypeId(hospitalById.getHospitalType().getKey());
				updateHospitalDetails.setInpatientBeds(hospitalById.getInpatientBeds() != null ? hospitalById
						.getInpatientBeds().toString() : "");
				
			}
			
			UploadDocumentDTO reimbursementBenefitsValue = claimRequestBenefitsService
					.getReimbursementBenefitsValue(reimbursementObjectByKey
							.getKey());

			setReimbursementValues(reimbursementObjectByKey, reimbursementDTO);
			
			
			
			
			/*if(null != claimByKey && null != claimByKey.getClaimType() && null != claimByKey.getClaimType().getKey())
			{
				if((claimByKey.getClaimType().getKey()).equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
				{
						if (null != reimbursementDTO.getNewIntimationDTO().getHospitalDto().getHospitalType()) {
						populatePaymentDetailsForHosp(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getKey(), rodDTO);
						
					}
				}
				else 
				{
					rodDTO.getDocumentDetails().setPaymentModeFlag(
							ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					populatePaymentDetailsForReimbursementClaim(claimKey, rodDTO);
				}
			}
			*/
			paymentDetailsForfinancial(reimbursementObjectByKey, reimbursementDTO,
					hospitalById);
	        
			if (reimbursementBenefitsValue != null) {
				reimbursementDTO.getPreauthDataExtractionDetails()
						.setUploadDocumentDTO(reimbursementBenefitsValue);
			}
			if (null != reimbursementBenefitsValue
					&& reimbursementBenefitsValue.getPatientCareBenefitFlag() != null
					&& reimbursementBenefitsValue.getPatientCareBenefitFlag()
							.equalsIgnoreCase("PC")) {
				List<ReimbursementBenefitsDetails> patientCareTableByBenefitKey = reimbursementService
						.getPatientCareTableByBenefitKey(reimbursementBenefitsValue
								.getPatientBenefitKey());
				List<PatientCareDTO> patientCareDTOList = new ArrayList<PatientCareDTO>();
				if (patientCareTableByBenefitKey != null
						&& !patientCareTableByBenefitKey.isEmpty()) {
					for (ReimbursementBenefitsDetails patientCareDetails : patientCareTableByBenefitKey) {
						PatientCareDTO dto = new PatientCareDTO();
						dto.setEngagedFrom(patientCareDetails.getEngagedFrom());
						dto.setEngagedTo(patientCareDetails.getEngagedTo());
						dto.setKey(patientCareDetails.getKey());
						patientCareDTOList.add(dto);
					}
				}
				reimbursementDTO.getPreauthDataExtractionDetails()
						.getUploadDocumentDTO()
						.setPatientCareDTO(patientCareDTOList);
			}

			List<AddOnBenefitsDTO> populateAddOnBenefitsTableValues = claimRequestBenefitsService
					.populateAddOnBenefitsTableValues(
							reimbursementDTO.getNewIntimationDTO(),
							reimbursementObjectByKey.getClaim().getKey(),
							reimbursementObjectByKey.getKey(),
							reimbursementDTO.getProductCopay(),null);

			reimbursementDTO.getPreauthDataExtractionDetails()
					.setAddOnBenefitsDTOList(populateAddOnBenefitsTableValues);
			// Reimbursement previousLatestROD =
			// getPreviousLatestROD(claimByKey.getKey(), reimbursementObjectByKey);
			// if(previousLatestROD != null) {
			// setReimbursmentTOPreauthDTO(mapper, claimByKey, previousLatestROD,
			// reimbursementDTO, true );
			// Hospitals hospitalById =
			// hospitalService.getHospitalById(claimByKey.getIntimation().getHospital());
			// ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails =
			// reimbursementDTO.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
			// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			// updateHospitalDetails.setHospitalCity(hospitalById.getCity());
			// updateHospitalDetails.setHospitalState(hospitalById.getState());
			// updateHospitalDetails.setHospitalCode(hospitalById.getHospitalCode());
			// reimbursementDTO.setReconsiderationList(getReconsiderRODRequest(claimByKey));
			// }

			Boolean isBasedOnPremium = false;
			if(ReferenceTable.getPremiumDeductionProductKeys().containsKey(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))) {
				isBasedOnPremium = true;
			}
			if(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyInstalmentFlag() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) 
					&& (reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag() != null 
					&& reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))){
				isBasedOnPremium = true;
			}
			
//			IMSSUPPOR-32761
			if(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyInstalmentFlag() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) 
	                && (reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag() != null 
	                && reimbursementObjectByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG))){
				isBasedOnPremium = true;
			}
			List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
					.search(reimbursementObjectByKey.getClaim().getKey(), isBasedOnPremium);

			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

				previousPreAuthTableDTO.setRequestedAmt(preauthService
						.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
								previousPreAuthTableDTO.getClaimKey()));
				newList.add(previousPreAuthTableDTO);
			}

			reimbursementDTO.setPreviousPreauthTableDTO(newList);

			reimbursementDTO.setBillingApprovedAmount(reimbursementObjectByKey
					.getBillingApprovedAmount());

			Reimbursement hospitalizationROD = getHospitalizationROD(
					reimbursementObjectByKey.getClaim().getKey(),
					reimbursementObjectByKey);
			Reimbursement partialHospitalizationROD = getPartialHospitalizationROD(
					reimbursementObjectByKey.getClaim().getKey(),
					reimbursementObjectByKey);

			DocAcknowledgement docAcknowledgement = reimbursementObjectByKey
					.getDocAcknowLedgement();
			String hospitalFlag = null;
			String partialHospitalFlag = null;
			if (docAcknowledgement != null
					&& docAcknowledgement.getHospitalisationFlag() != null) {
				hospitalFlag = docAcknowledgement.getHospitalisationFlag();
				partialHospitalFlag = docAcknowledgement.getPartialHospitalisationFlag();
			}
			if (hospitalFlag != null && !hospitalFlag.equalsIgnoreCase("Y") && partialHospitalFlag != null && !partialHospitalFlag.equalsIgnoreCase("Y")) {
				if ((hospitalizationROD != null
						&& hospitalizationROD.getStatus() != null
						&& !ReferenceTable.FINANCIAL_APPROVE_STATUS
								.equals(hospitalizationROD.getStatus().getKey()) && !ReferenceTable.getPaymentStatus().containsKey(hospitalizationROD.getStatus().getKey())) ) {
					reimbursementDTO.setIsHospitalizationRODApproved(false);
				}
				
				if(partialHospitalizationROD != null
						&& partialHospitalizationROD.getStatus() != null
						&& ReferenceTable.FINANCIAL_APPROVE_STATUS
								.equals(partialHospitalizationROD.getStatus().getKey()) && ReferenceTable.getPaymentStatus().containsKey(partialHospitalizationROD.getStatus().getKey())) {
					reimbursementDTO.setIsHospitalizationRODApproved(true);
				}
			}
			

			if (reimbursementObjectByKey.getDocAcknowLedgement()
					.getHospitalizationRepeatFlag() != null
					&& reimbursementObjectByKey.getDocAcknowLedgement()
							.getHospitalizationRepeatFlag().toLowerCase()
							.equalsIgnoreCase("y")) {
				reimbursementDTO.setIsHospitalizationRepeat(true);
			}
			
			
			reimbursementDTO.getPreauthDataExtractionDetails()
					.setDocAckknowledgement(
							reimbursementObjectByKey.getDocAcknowLedgement());
			reimbursementDTO.getPreauthMedicalDecisionDetails().setApprovalRemarks(
					"");

			String specialityName = claimService
					.getSpecialityName(reimbursementObjectByKey.getClaim().getKey());

			reimbursementDTO.setSpecialityName(specialityName);
	        
			String remedicalRemarks = null;
			String rebillingRemarks = null;
			Long latestMedicalApproverKey = reimbursementService.getLatestMedicalApproverKey(reimbursementObjectByKey.getKey());
			if(latestMedicalApproverKey != null){
				MedicalApprover medicalApproverByKey = reimbursementService.getMedicalApproverByKey(latestMedicalApproverKey);
				if(medicalApproverByKey != null){
					reimbursementDTO.setIsReMedical(true);
					remedicalRemarks = medicalApproverByKey.getApproverReply();
				}
			}
			Long latestApproverKey = reimbursementService.getLatestMedicalApproverForRebilling(reimbursementObjectByKey.getKey());
			if(latestApproverKey != null){
				MedicalApprover medicalApproverByKey = reimbursementService.getMedicalApproverByKey(latestApproverKey);
				if(medicalApproverByKey != null){
					reimbursementDTO.setIsReBilling(true);
					rebillingRemarks = medicalApproverByKey.getApproverReply();
				}
			}
			
			reimbursementDTO.getPreauthDataExtractionDetails().setBillingRemarks(rebillingRemarks);
			reimbursementDTO.getPreauthDataExtractionDetails().setMedicalRemarks(remedicalRemarks);
			reimbursementDTO.setFvrCount(intimationService.getFVRCount(reimbursementDTO.getNewIntimationDTO().getKey())); 
			
			if (claimByKey.getClaimType() != null
					&& claimByKey.getClaimType().getKey()
							.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {

//				PreMedicalMapper premedicalMapper = new PreMedicalMapper();
				
				Preauth previousPreauth = getPreviousPreauth(claimByKey.getKey());
				
				reimbursementDTO.setIsCashlessType(true);
				
				if(previousPreauth != null) {
					reimbursementDTO.setPreauthKey(previousPreauth.getKey());
				}
			}
			
			/*if (null != reimbursementObjectByKey && null != reimbursementObjectByKey.getProrataDeductionFlag() && !("").equalsIgnoreCase(reimbursementObjectByKey.getProrataDeductionFlag()))
			{
			reimbursementDTO.setProrataDeductionFlag(reimbursementObjectByKey.getProrataDeductionFlag());
			}
			*/
			reimbursementDTO = getProrataFlagFromProduct(reimbursementDTO);
			reimbursementDTO.setProrataDeductionFlag(reimbursementObjectByKey.getProrataDeductionFlag());
			/**
			 * product based variable is added to enable or disable the component in page level.
			 * This would be static. -- starts
			 * */
			//reimbursementDTO.setProductBasedProRata(reimbursementObjectByKey.getProrataDeductionFlag());
			//reimbursementDTO.setProductBasedPackage(reimbursementObjectByKey.getPackageAvailableFlag());
			//ends.
			reimbursementDTO.setPackageAvailableFlag(reimbursementObjectByKey.getPackageAvailableFlag());

//			if(claimByKey.getClaimType() != null && claimByKey.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
//				Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(claimByKey.getKey());
//				if(claimByKey.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS) ||  claimByKey.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)) {
//					reimbursementDTO.setIsWithDrawn(true);
//				}
//			}
			
			loadRRCRequestValues(reimbursementDTO,insuredSumInsured,SHAConstants.RRC_FINANCIAL);
			
			

			if (reimbursementDTO.getClaimKey() != null) {
				List<Investigation> investigationList = investigationService
						.getByInvestigationByClaimKey(reimbursementDTO.getClaimKey());
				if (investigationList != null && investigationList.size() >= 0) {
					reimbursementDTO.setInvestigationSize(investigationList.size());
				} else {
					reimbursementDTO.setInvestigationSize(0);
				}
			}

			reimbursementDTO.setSittingsAmount(dbCalculationService.getDialysisAmount(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(), insuredSumInsured.longValue()).intValue());
			Map<String, Integer> productBenefitFlag = dbCalculationService.getProductBenefitFlag(reimbursementObjectByKey.getClaim().getKey(), reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey());
//				
			if(productBenefitFlag.get(SHAConstants.PRE_HOSP_FLAG).equals(0)) {
				reimbursementDTO.setIsPreHospApplicable(false);
			}
			
			if(productBenefitFlag.get(SHAConstants.POST_HOSP_FLAG).equals(0)) {
				reimbursementDTO.setIsPostHospApplicable(false);
			}
			
			if(productBenefitFlag.get(SHAConstants.LUMP_SUM_FLAG).equals(1)) {
				reimbursementDTO.setIsLumpsumApplicable(true);
			}
			
			if(productBenefitFlag.get(SHAConstants.HOSPITALCASH_FLAG).equals(1)) {
				reimbursementDTO.setIsHospitalCashApplicable(true);
			}
			
			if(productBenefitFlag.get(SHAConstants.PATIENTCARE_FLAG).equals(1)) {
				reimbursementDTO.setIsPatientCareApplicable(true);
			}
			
			
				if((!reimbursementDTO.getHospitalizaionFlag() && !reimbursementDTO.getPartialHospitalizaionFlag()) && ! reimbursementDTO.getPostHospitalizaionFlag() && reimbursementDTO.getPreHospitalizaionFlag()
						&& productBenefitFlag.get(SHAConstants.PRE_HOSP_FLAG).equals(0)) {
					reimbursementDTO.setIsDishonoured(true);
				} else if((!reimbursementDTO.getHospitalizaionFlag() && !reimbursementDTO.getPartialHospitalizaionFlag()) && ! reimbursementDTO.getPreHospitalizaionFlag() && reimbursementDTO.getPostHospitalizaionFlag()
						&& productBenefitFlag.get(SHAConstants.POST_HOSP_FLAG).equals(0)) {
					reimbursementDTO.setIsDishonoured(true);
				}

			
			if((!reimbursementDTO.getHospitalizaionFlag() && !reimbursementDTO.getPartialHospitalizaionFlag() && !reimbursementDTO.getLumpSumAmountFlag()) &&
					! reimbursementDTO.getIsPreHospApplicable() && ! reimbursementDTO.getIsPostHospApplicable()){
				reimbursementDTO.setIsDishonoured(true);
			}
			
			// If hospitalization is not processed then remaining ROD should be rejected.
			Reimbursement hospROD = reimbursementService.getHospitalizationROD(reimbursementObjectByKey.getClaim().getKey());
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
				if(hospROD == null) {
//					reimbursementDTO.setIsDishonoured(true);
					reimbursementDTO.setIsHospitalizationRejected(true);
				}
			}
			
			Reimbursement hospitalizationOrPartialROD = reimbursementService.getHospitalizationOrPartialROD(reimbursementObjectByKey.getClaim().getKey());
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				
				Integer seqNumber = 0;
				
				if(reimbursementObjectByKey.getRodNumber() != null){
					String[] split = reimbursementObjectByKey.getRodNumber().split("/");
					seqNumber = Integer.valueOf(split[split.length - 1]);
				}
				
				if(hospitalizationOrPartialROD == null && ! seqNumber.equals(1)) {
					reimbursementDTO.setIsDishonoured(true);
					reimbursementDTO.setIsHospitalizationRejected(true);
				}
				
				if(reimbursementObjectByKey.getStatus().getKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)){
					reimbursementDTO.setIsByPass(true);
				}
				
			}
			
			Map<String, String> popupMessages = dbCalculationService.getPOPUPMessages(reimbursementDTO.getPolicyKey(), reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey(),reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
			reimbursementDTO.setPopupMap(popupMessages);
			
			if(reimbursementDTO.getNewIntimationDTO().getHospitalDto() != null && reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag() != null
					&& reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag().equalsIgnoreCase("Y")){
				if(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks() != null){
					Map<String, String> suspiciousMap = SHAUtils.getSuspiciousMap(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks());
					reimbursementDTO.setSuspiciousPopupMap(suspiciousMap);
				}
			}
			
			if(reimbursementObjectByKey.getNatureOfTreatment() != null 
					&& reimbursementObjectByKey.getNatureOfTreatment().getKey().equals(ReferenceTable.NON_ALLOPATHIC_ID) &&
					ReferenceTable.isNonAllopathicApplicableProduct(reimbursementDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				reimbursementDTO.setIsDishonoured(true);
			}
			
			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance().get64VBStatus(reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicyNumber(), reimbursementDTO.getNewIntimationDTO().getIntimationId());
				if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
					reimbursementDTO.setIsDishonoured(true);
				}  else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
					reimbursementDTO.setIsPending(true);
				}
			}
			reimbursementDTO = checkFinalEnhancement(reimbursementDTO);
			
			MastersValue networkHospitalType = masterService.getMaster(reimbursementDTO.getNewIntimationDTO().getHospitalDto().getNetworkHospitalTypeId());
			reimbursementDTO.setNetworkHospitalType(networkHospitalType.toString());
			
			// Below stuff for Mulitple Room rent or ICU...........
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 8l, 9l, false,false);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 10l, 11l, true,false);
			reimbursementDTO = SHAUtils.roomRentNursingMapping(reimbursementDTO, 85l, 84l, false,true);
			
			List<BillItemMapping> mappingData = reimbursementService.getMappingData(reimbursementObjectByKey.getKey());
			SHAUtils.fillMappingData(reimbursementDTO, mappingData, false);
			
			try {
				if(reimbursementObjectByKey.getReconsiderationRequest() != null && reimbursementObjectByKey.getReconsiderationRequest().equalsIgnoreCase("Y")) {
					if(reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag() !=  null && reimbursementObjectByKey.getDocAcknowLedgement().getPaymentCancellationFlag().equalsIgnoreCase("N")) {
						reimbursementDTO.setIsReconsiderationRequest(true);
						if(reimbursementService.isClaimPaymentAvailable(reimbursementObjectByKey.getRodNumber())) {
							Double claimPaymentAmountByRODNumber = reimbursementService.getClaimPaymentAmountByRODNumber(reimbursementObjectByKey.getRodNumber());
							reimbursementDTO.setClaimPaymentAmount(claimPaymentAmountByRODNumber);
						} else {
							
							/**
							 * For reject reconsider, cancel rod button was enabled. But for this ticket GALAXYMAIN-6222	
							 * which was raised in PA, now cancel rod button is diabled for rejection case to.
							 * Hence below code was commented.
							 * */
							
							//reimbursementDTO.setIsReconsiderationRequest(false);
						}
						
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				reimbursementDTO.setIsCashlessType(true);
			}
			if(reimbursementDTO.getIsCashlessType() && reimbursementDTO.getHospitalizaionFlag() && (reimbursementDTO.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)|| reimbursementDTO.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS))){
				reimbursementDTO.setIsDirectToFinancial(true);
				Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursementDTO.getClaimDTO().getKey());
				reimbursementDTO.getClaimDTO().setLatestPreauthKey(latestPreauth.getKey());
			}
			reimbursementDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey()));
			reimbursementDTO.setIsPEDInitiatedForBtn(reimbursementDTO.getIsPEDInitiated());
			
//			if(reimbursementObjectByKey.getDocAcknowLedgement() != null && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
//					&& reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
//				Boolean isPedWatchList = pedQueryService.isTaskAvailableInWatchListForIntimation(reimbursementDTO.getNewIntimationDTO().getIntimationId());
//				reimbursementDTO.setIsPedWatchList(isPedWatchList);
//			}
			
			
			if(reimbursementDTO.getIsReconsiderationRequest() != null && !reimbursementDTO.getIsReconsiderationRequest() && reimbursementObjectByKey != null) {
				Boolean paymentAvailable = reimbursementService.isPaymentAvailable(reimbursementObjectByKey.getRodNumber());
				reimbursementDTO.setIsPaymentAvailable(paymentAvailable);
				reimbursementDTO.setIsPaymentAvailableShown(paymentAvailable);
				DocAcknowledgement acknowledgementByKey = reimbursementService.getAcknowledgementByKey(reimbursementObjectByKey.getDocAcknowLedgement().getKey());
				if(acknowledgementByKey != null && acknowledgementByKey.getPaymentCancellationFlag() != null && acknowledgementByKey.getPaymentCancellationFlag().equalsIgnoreCase("Y")) {
					reimbursementDTO.setIsPaymentAvailable(false);
					reimbursementDTO.setIsPaymentAvailableShown(false);
				}
				
			}
			
			
			List<InsuredPedDetails> pedByInsured = policyService.getPEDByInsured(reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			
			reimbursementDTO.setInsuredPedDetails(pedByInsured);
			

			try{
		        Double totalBilledAmountForHosp = reimbursementService.getTotalBilledAmountForHosp(documentSummaryKeys);
		        reimbursementDTO.setAmountConsidered(totalBilledAmountForHosp != null ? String
						.valueOf(totalBilledAmountForHosp.intValue()) : "0");
		        
//		        if(reimbursementDTO.getAmountConsCopayPercentage() != null && reimbursementDTO.getAmountConsCopayPercentage().equals(0l)){
		        	reimbursementDTO.setAmountConsidered(reimbursementDTO.getAmountConsidedAfterCoPay() != null ? String
						.valueOf(reimbursementDTO.getAmountConsidedAfterCoPay().intValue()) : "0");
//		        }
		        
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(reimbursementDTO.getClaimDTO().getClaimType() != null && reimbursementDTO.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
				if(reimbursementDTO.getHospitalizaionFlag()) {
					Reimbursement partialHospitalizationRODWithSettled = getPartialHospitalizationRODWithSettled(claimByKey.getKey());
					ReimbursementCalCulationDetails hosptialization = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(reimbursementDTO.getKey(), ReferenceTable.HOSPITALIZATION);
					if(hosptialization != null) {
						reimbursementDTO.setBillingApprovedAmount(hosptialization.getPayableToHospAftTDS() != null ? hosptialization.getPayableToHospAftTDS().doubleValue() : 0d);
						reimbursementDTO.setPayableToHospAmt(hosptialization.getPayableToHospital() != null ? hosptialization.getPayableToHospital().doubleValue() : 0d);
						reimbursementDTO.setHospDiscountAmount(hosptialization.getHospitalDiscount() != null ? hosptialization.getHospitalDiscount().doubleValue() : 0d);
					}
					if(partialHospitalizationRODWithSettled != null) {
						ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(partialHospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
						reimbursementDTO.setPayableToInsAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableInsuredAfterPremium().doubleValue() : 0d);
						reimbursementDTO.setFAApprovedAmount(partialHospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? partialHospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
					}
				} else if(reimbursementDTO.getPartialHospitalizaionFlag()) {
					Reimbursement hospitalizationRODWithSettled = getHospitalizationRODWithSettled(claimByKey.getKey());
					if(hospitalizationRODWithSettled != null) {
						ReimbursementCalCulationDetails reimbursementCalcObjByRodAndClassificationKey = reimbursementService.getReimbursementCalcObjByRodAndClassificationKey(hospitalizationRODWithSettled.getKey(), ReferenceTable.HOSPITALIZATION);
						reimbursementDTO.setPayableToHospAmt(reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital() != null ? reimbursementCalcObjByRodAndClassificationKey.getPayableToHospital().doubleValue() : 0d);
						reimbursementDTO.setHospDiscountAmount(reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d);
						Double amount = (reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount() != null ? reimbursementCalcObjByRodAndClassificationKey.getHospitalDiscount().doubleValue() : 0d) + (hospitalizationRODWithSettled.getFinancialApprovedAmount() != null ? hospitalizationRODWithSettled.getFinancialApprovedAmount() : 0d);
						reimbursementDTO.setFAApprovedAmount(amount);
					}
				}
			}


			Boolean queryReceivedStatusRod = reimbursementQuerySerice.isQueryReceivedStatusRod(reimbursementObjectByKey.getKey());
			 
			 if(queryReceivedStatusRod){
				 reimbursementDTO.setIsQueryReceived(true);   
				 
			 }
			 
			 
			 List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = acknowledgementDocumentsReceivedService.getReimbursementCalculationDetails(reimbursementObjectByKey.getKey());
				

				
				for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {
					if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
							reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.HOSPITALIZATION)){
						
						HopitalizationCalulationDetailsDTO hospitalizationCalcDTO = getHospitalizationCalculationDTO(reimbursementCalCulationDetails2,reimbursementDTO, false);
						if(reimbursementDTO.getDocumentReceivedFromId() != null && reimbursementDTO.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
					           hospitalizationCalcDTO.setBalanceToBePaid(0);
						}
						reimbursementDTO.setHospitalizationCalculationDTO(hospitalizationCalcDTO);
						
					}
					else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
							reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.PRE_HOSPITALIZATION)){
						PreHopitalizationDetailsDTO preHospitalizationCalcDTO = getPreHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, false);
						reimbursementDTO.setPreHospitalizationCalculationDTO(preHospitalizationCalcDTO);
						
					}else if(reimbursementCalCulationDetails2.getBillClassificationId() != null && 
							reimbursementCalCulationDetails2.getBillClassificationId().equals(ReferenceTable.POST_HOSPITALIZATION)){
						PostHopitalizationDetailsDTO postHospitalizationCalcDTO = getPostHospitalizationDTO(reimbursementCalCulationDetails2, reimbursementDTO, false);
						reimbursementDTO.setPostHospitalizationCalculationDTO(postHospitalizationCalcDTO);
					}
				}
				
				List<Double> copayValues = new ArrayList<Double>();
				
				if(reimbursementObjectByKey.getAmtConsCopayPercentage() != null){
					reimbursementDTO.setCoPayValue(reimbursementObjectByKey.getAmtConsCopayPercentage().doubleValue());
					copayValues.add(reimbursementObjectByKey.getAmtConsCopayPercentage());
				}
			 reimbursementDTO.setDoctorNote(reimbursementObjectByKey.getDoctorNote() != null? reimbursementObjectByKey.getDoctorNote() : "" );
			 
				List<DiagnosisDetailsTableDTO> diagnosisTableList = reimbursementDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
				if(diagnosisTableList != null && ! diagnosisTableList.isEmpty()){
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisTableList) {
						if(diagnosisDetailsTableDTO.getCopayPercentage() != null){
							Double copayPercentage = diagnosisDetailsTableDTO.getCopayPercentage();
							copayValues.add(copayPercentage);
						}
					}
				}
				
				List<ProcedureDTO> procedureDTO = reimbursementDTO.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
				if(procedureDTO != null && ! procedureDTO.isEmpty()){
					for (ProcedureDTO procedureDTO2 : procedureDTO) {
						if(procedureDTO2.getCopayPercentage() != null){
							copayValues.add(procedureDTO2.getCopayPercentage());
						}
					}
				}

				if(! copayValues.isEmpty()){
					Double maximumCopay = Collections.max(copayValues);
				    if(maximumCopay != null){
				    	reimbursementDTO.setCoPayValue(maximumCopay.doubleValue());
				    }
				}
				
			 SHAUtils.setConsolidatedAmtDTO(reimbursementDTO, true);
			 Double totalClaimedAmount = reimbursementService.getTotalClaimedAmount(reimbursementObjectByKey);
			 reimbursementDTO.setRodTotalClaimedAmount(totalClaimedAmount);
			 
			 if(reimbursementDTO.getAmountConsidedAfterCoPay() != null && totalBilledAmount != null && !reimbursementDTO.getAmountConsidedAfterCoPay().equals(totalBilledAmount)) {
				 reimbursementDTO.setAmountConsidered(String.valueOf(totalBilledAmount.longValue()));
			 }
			 
			// SHAUtils.setDefaultCopayValue(reimbursementDTO);
			 
			 String previoustAmountForDiagnosisProcedure = SHAUtils.getPrevioustAmountForDiagnosisProcedure(reimbursementDTO.getPreauthDataExtractionDetails().getDiagnosisTableList(), reimbursementDTO
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList(), reimbursementDTO.getResidualAmountDTO());
			 
			 Double approvedAmount = SHAUtils.getDoubleValueFromString(previoustAmountForDiagnosisProcedure);
			 reimbursementDTO.setSublimitAndSIAmt(approvedAmount);

			 if(reimbursementObjectByKey.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
					 && reimbursementObjectByKey.getDocAcknowLedgement() != null && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
					 && reimbursementObjectByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)){
			 
					 if(hospitalById != null){
							Boolean hospitalDiscount = SHAUtils.isHospitalDiscount(hospitalById);
							reimbursementDTO.setIsHospitalDiscountApplicable(hospitalDiscount);
					 }
			 }
			 
//			 if(reimbursementDTO.getNewIntimationDTO() != null && reimbursementDTO.getNewIntimationDTO().getInsuredPatient() != null &&
//					 reimbursementDTO.getNewIntimationDTO().getInsuredPatient().getKey() != null){
//					Long claimCount = preauthService.getClaimCount(reimbursementDTO.getNewIntimationDTO().getPolicy().getKey());
//					if(claimCount != null){
//						reimbursementDTO.setClaimCount(claimCount);
//					}
//				}
			 
//			 reimbursementObjectByKey.getStatus().setKey(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);
//			 reimbursementService.setBPMForClaimBilling(reimbursementDTO, false, "MEDICAL", reimbursementObjectByKey);
			 	
				reimbursementDTO.setDbOutArray(tableDTO.getDbOutArray());
				
				if(null != reimbursementObjectByKey && null != reimbursementObjectByKey.getWorkPlace() &&
						(SHAConstants.YES_FLAG.equalsIgnoreCase(reimbursementObjectByKey.getWorkPlace()))){
					
					reimbursementDTO.getPreauthDataExtractionDetails().setWorkOrNonWorkPlace(true);
				}
				
				if(ReferenceTable.getGMCProductList().containsKey(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey()) ||
						ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getProduct().getKey())){
				      Insured insuredByKey = intimationService.getInsuredByKey(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey());
				      Insured MainMemberInsured = null;
				      
				      if(insuredByKey.getDependentRiskId() == null){
				    	  MainMemberInsured = insuredByKey;
				      }else{
				    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredNameForDefault(reimbursementObjectByKey.getClaim().getIntimation().getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId());
				    	  MainMemberInsured = insuredByPolicyAndInsuredId;
				      }
				      
				      if(MainMemberInsured != null){
				    	  
				    	  reimbursementDTO.getNewIntimationDTO().setGmcMainMemberName(MainMemberInsured.getInsuredName());
				    	  reimbursementDTO.getNewIntimationDTO().setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());
				    	  
				      }
					}

				LegalHeirDTO legalHeirDTO = new LegalHeirDTO();				
				legalHeirDTO.setRelationshipContainer(masterService.getRelationshipContainerValues());
				reimbursementDTO.setLegalHeirDto(legalHeirDTO);
				//reimbursementDTO.setVentilatorSupportFlag(reimbursementByKey.getVentilatorSupport());
				List<NomineeDetailsDto> nomineeDtoList = intimationService.getNomineeDetailsListByTransactionKey(tableDTO.getRodKey());
				if( nomineeDtoList != null && !nomineeDtoList.isEmpty()){
					reimbursementDTO.getNewIntimationDTO().setNomineeList(nomineeDtoList);
				}
				if(reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased() != null 
						&& reimbursementDTO.getPreauthDataExtractionDetails().getIsNomineeDeceased()) {
					List<LegalHeir> legalHeirList = masterService.getlegalHeirListByTransactionKey(tableDTO.getRodKey());
					if(legalHeirList != null && !legalHeirList.isEmpty()) {
						List<LegalHeirDTO> legalHeirDTOList = new ArrayList<LegalHeirDTO>();
						LegalHeirDTO legalHeirDto;
						for (LegalHeir legalHeir : legalHeirList) {
							legalHeirDto = new LegalHeirDTO(legalHeir);
							legalHeirDto.setRelationshipContainer(masterService.getRelationshipContainerValues());
							legalHeirDTOList.add(legalHeirDto);
						}
						reimbursementDTO.setLegalHeirDTOList(legalHeirDTOList);
					}
				}	
				
			//CR2019214
				 Map<String, String> getApproveFlagDetails = dbCalculationService.getApproveFlagDisable(reimbursementDTO.getNewIntimationDTO().getIntimationId(), hospitalById.getHospitalCode());
				 if(getApproveFlagDetails != null && !getApproveFlagDetails.isEmpty()){
					 if(getApproveFlagDetails.get(SHAConstants.FLAG) != null && getApproveFlagDetails.get(SHAConstants.FLAG).equalsIgnoreCase("y")){
						 reimbursementDTO.setApproveBtnFlag(true);
						 reimbursementDTO.setApproveBtnMsg(getApproveFlagDetails.get(SHAConstants.ALERT_MESSAGE) != null ? getApproveFlagDetails.get(SHAConstants.ALERT_MESSAGE) : null);
					 }else if(getApproveFlagDetails.get(SHAConstants.FLAG) != null && getApproveFlagDetails.get(SHAConstants.FLAG).equalsIgnoreCase("N")){
						 reimbursementDTO.setApproveBtnFlag(false);
					 }
					 
				 }	

				
			/*for bancs*/
			if(reimbursementObjectByKey.getCatastrophicLoss() != null) {
				
				SelectValue value = masterService.getCatastropheData(reimbursementObjectByKey.getCatastrophicLoss());
				reimbursementDTO.getPreauthDataExtractionDetails().setCatastrophicLoss(value);
			}
			if(reimbursementObjectByKey.getNatureOfLoss() != null) {
				
				SelectValue value = masterService.getMasterValueForNatureCause(reimbursementObjectByKey.getNatureOfLoss());
				reimbursementDTO.getPreauthDataExtractionDetails().setNatureOfLoss(value);
			}
			if(reimbursementObjectByKey.getCauseOfLoss() != null) {
				SelectValue value = masterService.getMasterValueForNatureCause(reimbursementObjectByKey.getCauseOfLoss());
				reimbursementDTO.getPreauthDataExtractionDetails().setCauseOfLoss(value);
			}
			if(reimbursementObjectByKey.getClaim().getLegalClaim() !=null
					 && reimbursementObjectByKey.getClaim().getLegalClaim().equals("Y")){
				 LegalTaxDeduction taxDeduction = reimbursementService.getTaxDeductionFromRODKey(reimbursementObjectByKey.getKey());
				 if(taxDeduction !=null){
					 LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
					 LegalBillingDTO billingDTO = taxDeductionMapper.getDTOFromTaxDeduction(taxDeduction);
					 billingDTO = reimbursementService.calculateLegalSettlment(billingDTO);
					 billingDTO = reimbursementService.calculateLegalSettlment(billingDTO);
					 reimbursementDTO.setLegalBillingDTO(billingDTO);
				 }
			 }
			
			Map<String,Object> icrAgent  = dbCalculationService.getAgentAndBranchName(reimbursementDTO.getNewIntimationDTO().getIntimationId()); 
			 if(icrAgent != null && !icrAgent.isEmpty()) {
					String agentScorePoint = icrAgent.get(SHAConstants.ICR_AGENT_POINT).toString();
					reimbursementDTO.setIcrAgentValue(agentScorePoint);
					String smScorepoint = icrAgent.get(SHAConstants.SM_AGENT_POINT).toString();
					reimbursementDTO.setSmAgentValue(smScorepoint);
			 }
			 
			 Policy policy = reimbursementDTO.getNewIntimationDTO().getPolicy();
			 // added for PA installment payment process in policy level and product
			 // level check
			 Map<String, String> getPolicyInstallment = dbCalculationService
					 .getInstallmentPaymentFlag(policy.getPolicyNumber(), policy.getProduct()
							 .getKey());
			 if (getPolicyInstallment != null && !getPolicyInstallment.isEmpty()) {
				 reimbursementDTO.setPolicyInstalmentFlag(getPolicyInstallment
						 .get(SHAConstants.FLAG) != null ? getPolicyInstallment
								 .get(SHAConstants.FLAG) : "N");
				 reimbursementDTO
				 .setPolicyInstalmentMsg(getPolicyInstallment
						 .get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallment
								 .get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
			 }
			 if (reimbursementDTO.getPolicyInstalmentFlag() != null
					 && reimbursementDTO.getPolicyInstalmentFlag().equals(
							 SHAConstants.YES_FLAG)) {
				 Integer policyInstallmentAmount = PremiaService.getInstance()
						 .getPolicyInstallmentAmount(policy
								 .getPolicyNumber());
				 reimbursementDTO.setPolicyInstalmentPremiumAmt(policyInstallmentAmount
						 .doubleValue());

				 String policyDueDate = PremiaService.getInstance()
						 .getPolicyInstallmentDetails(policy
								 .getPolicyNumber());
				 //code added if we recevied instalment due amount as zero after pending amount adjusted to premia/bancs  need to set due date as admission date by noufel
				 if((reimbursementDTO.getPolicyInstalmentPremiumAmt() != null && reimbursementDTO.getPolicyInstalmentPremiumAmt() == 0d) && 
						 (policyDueDate == null || policyDueDate.isEmpty())) {

					 reimbursementDTO.setPolicyInstalmentDueDate(reimbursementDTO.getPreauthDataExtractionDetails().getAdmissionDate());	
				 }
				 else{
					 if (reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource() != null && reimbursementDTO.getNewIntimationDTO().getPolicy().getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
						 reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.formatTimeFromString(policyDueDate.toString()));	
					 }
					 else{
						 reimbursementDTO.setPolicyInstalmentDueDate(SHAUtils.dateformatFromString(policyDueDate.toString()));
					 }
				 }
			 }
			 
			 if(policy != null && policy.getProduct().getKey() != null && policy.getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
				 reimbursementDTO.setIsHospitalExpenseCoverSelected(intimationService.getInsuredCoverByInsuredKey(reimbursementObjectByKey.getClaim().getIntimation().getInsured().getKey(), "ACC-CVR-032"));
			 }
			 
			view.setPAHealthProcessClaimFinancial(PAHealthFinancialWizard.class, reimbursementDTO);
		}
		}
		
		
		protected void showPASearchOrUploadDocuments(
				@Observes @CDIEvent(MenuItemBean.PA_SEARCH_OR_UPLOAD_DOCUMENTS) final ParameterDTO parameters) {
			
			BeanItemContainer<SelectValue> selectValueContainerForType =  masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES);	
			
			view.setViewPASearchOrUploadDocuments(PASearchUploadDocumentsView.class,true,selectValueContainerForType);
		}
		
		protected void showPASearchOrUploadDocumentsForAckNotReceived(
				@Observes @CDIEvent(MenuItemBean.PA_SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED) final ParameterDTO parameters) {
			
			BeanItemContainer<SelectValue> selectValueContainerForType =  masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES);	
			
			view.setViewPASearchOrUploaddocumentsForAckNotReceived(PAUploadDocumentsOutsideProcessView.class,true,selectValueContainerForType);
		}
		
//		Commented the below Cashless Screen
//		protected void showPAClearCashlessView(
//				@Observes @CDIEvent(MenuItemBean.PA_CLEAR_CASHLESS) final ParameterDTO parameters) {
//			view.setViewG(PASearchCancelCashlessView.class, true);
//		}
		
		protected void showPARe_OpenClaim(
				@Observes @CDIEvent(MenuItemBean.PA_RE_OPEN_CLAIM_ROD_LEVEL) final ParameterDTO parameters) {
			view.setViewG(PASearchReOpenClaimRodLevelView.class, true);
		}
		
		protected void showPAHealthRe_OpenClaim(
				@Observes @CDIEvent(MenuItemBean.PA_HOSP_RE_OPEN_CLAIM_ROD_LEVEL) final ParameterDTO parameters) {
			view.setViewG(PAHealthSearchReOpenClaimRodLevelView.class, true);
		}
		
		protected void showPAInProcessCloseClaim(@Observes @CDIEvent(MenuItemBean.PA_CLOSE_CLAIM_IN_PROCESS_ROD_LEVEL) final ParameterDTO parameters){
			view.setViewG(PASearchCloseClaimInProcessView.class, true);
		}
		
		protected void showPACloseClaimRODLevelView(
				@Observes @CDIEvent(MenuItemBean.PA_CLOSE_CLAIM_ROD_LEVEL) final ParameterDTO parameters) {
			view.setViewG(PASearchCloseClaimRODLevelView.class, true);
		}
		
		protected void showPACloseClaimClaimLevelSearch(
				@Observes @CDIEvent(MenuItemBean.PA_CLOSE_CLAIM_CLAIM_LEVEL) final ParameterDTO parameters) {
			view.setViewG(PASearchCloseClaimView.class, true);
		}
		
		protected void showPARe_OpenClaimClaimLevel(
				@Observes @CDIEvent(MenuItemBean.PA_RE_OPEN_CLAIM_CLAIM_LEVEL) final ParameterDTO parameters) {
			view.setViewG(PASearchReOpenClaimView.class, true);
		}
		
		protected void showPAHealthRe_OpenClaimClaimLevel(
				@Observes @CDIEvent(MenuItemBean.PA_HOSP_RE_OPEN_CLAIM_CLAIM_LEVEL) final ParameterDTO parameters) {
			view.setViewG(PAHealthSearchReOpenClaimView.class, true);
		}
		
		protected void showPACancelROD(
				@Observes @CDIEvent(MenuItemBean.PA_CANCEL_ACKNOWLEDGEMENT) final ParameterDTO parameters) {
			view.setViewG(SearchCancelAcknowledgementView.class, true);
		}
		
		protected void showPASettlementPullBackView(
				@Observes @CDIEvent(MenuItemBean.PA_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
			view.setViewG(PASearchSettlementPullBackView.class, true);
		}
		
		protected void showPAHospSettlementPullBackView(
				@Observes @CDIEvent(MenuItemBean.PA_HOSP_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
			view.setViewG(PAHospSearchSettlementPullBackView.class, true);
		}
		
		protected void showPAUnlockIntimation(
				@Observes @CDIEvent(MenuItemBean.PA_UNLOCK_INTIMATION) final ParameterDTO parameters) {
			view.setViewG(SearchUnlockIntimationView.class, true);
			
		}
		
		protected void showPACPUSkipZMRView(
				@Observes @CDIEvent(MenuItemBean.PA_CPU_SKIP_ZMR) final ParameterDTO parameters) {
			
			BeanItemContainer<SelectValue> cpuCodeContainer = masterService
					.getTmpCpuCodes();
			view.setSkipZMR(SkipZMRView.class, cpuCodeContainer);
		}
		

		protected void showPAReopenClaimPageView(
				@Observes @CDIEvent(PAMenuPresenter.REOPEN_CLAIM_PAGE_VIEW) final ParameterDTO parameters) {

			
			PASearchReOpenClaimTableDTO tableDTO = (PASearchReOpenClaimTableDTO) parameters.getPrimaryParameter();
			
			NewIntimationDto intimationDto = new NewIntimationDto();
			
			PAReopenClaimPageDTO reopenClaimDto = new PAReopenClaimPageDTO();
			
			if(tableDTO.getIntimationkey() != null){
				
				Claim claim = claimService.getClaimByKey(tableDTO.getClaimKey());
				
				
				reopenClaimDto.setClaimKey(tableDTO.getClaimKey());

				
				Intimation intimation = claim.getIntimation();
				intimationDto = intimationService
						.getIntimationDto(intimation);
	          
				

				ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(claim);
				reopenClaimDto.setClaimDto(claimDTO);
				
				Double insuredSumInsured = 0d;
				if(null != intimation &&  
				null != intimation.getPolicy() && null != intimation.getPolicy().getProduct() &&
				null != intimation.getPolicy().getProduct().getKey() 
				&& !(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey())))
				{
				
				 insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDto.getInsuredPatient().getInsuredId().toString(), intimationDto.getPolicy().getKey()
						 ,intimationDto.getInsuredPatient().getLopFlag());
				}
				else
				{
					 insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(intimationDto.getInsuredPatient().getInsuredId().toString(), intimationDto.getPolicy().getKey());
				}
				
				Reimbursement reimbursement = reimbursementService.getLatestActiveROD(claim.getKey());
				
				Double balanceSI = 0d;
				
				if(reimbursement != null){
					
					
					balanceSI = dbCalculationService.getPABalanceSI(intimationDto.getInsuredPatient().getKey(), 
							claim.getKey(),reimbursement.getKey(),(reimbursement.getBenefitsId()) != null ? reimbursement.getBenefitsId().getKey() : 0l );
				}
				
				reopenClaimDto.setBalanceSI(balanceSI);
				
				Preauth preauth= preauthService.getLatestPreauthDetails(claim.getKey());
				
				if(reimbursement==null){
					
					
					String diagnosisForPreauthByKey = "";
					if(preauth != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(preauth.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}else{
					String diagnosisForPreauthByKey = "";
					if(reimbursement != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursement.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
					reopenClaimDto.setDiagnosis(diagnosisForPreauthByKey);
				}
				
				List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
						.search(claim.getKey(), false);
				
				List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
				for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

					previousPreAuthTableDTO.setRequestedAmt(preauthService
							.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
									previousPreAuthTableDTO.getClaimKey()));
					newList.add(previousPreAuthTableDTO);

				}
				
				reopenClaimDto.setPreviousPreauthDetailsList(newList);

				List<PAReopenClaimTableDTO> reopenClaimList = new ArrayList<PAReopenClaimTableDTO>();
			
				PAReopenClaimTableDTO reopenClaim = new PAReopenClaimTableDTO();
				reopenClaim.setInsuredPatientName(intimation.getInsuredPatientName());
				reopenClaim.setStrDateOfAdmission(SHAUtils.formatDate(intimation.getAdmissionDate()));
				Long aCknowledgeNumberCountByClaimKey = createRodService.getACknowledgeNumberCountByClaimKey(claim.getKey());
				
				if(aCknowledgeNumberCountByClaimKey != null){
					reopenClaim.setNumberOfRod(aCknowledgeNumberCountByClaimKey.intValue());
				}
				
				List<Reimbursement> reimbursementByClaimKey = createRodService.getReimbursementByClaimKey(claim.getKey());
				
				reopenClaim.setClaimStatus(claim.getStatus().getProcessValue());

				Double provisionAmount = 0d;
				
				for (Reimbursement reimbursement2 : reimbursementByClaimKey) {
					
					if(reimbursement2.getCurrentProvisionAmt() != null){
					Double provisionAmount2 = reimbursement2.getCurrentProvisionAmt();
					if(provisionAmount2 != null){
						provisionAmount += provisionAmount2;
					}}
				}

				reopenClaim.setProvisionAmount(provisionAmount);
				
				CloseClaim closeClaim1 = createRodService.getCloseClaim(claim.getKey());
				
				if(closeClaim1 != null){
					if(closeClaim1.getClosedDate() != null){
						reopenClaim.setStrDateOfClosedClaim(SHAUtils.formatDate(closeClaim1.getClosedDate()));	
					}
					if(closeClaim1.getClosingReasonId() != null){
						reopenClaim.setReasonForClosure(closeClaim1.getClosingReasonId().getValue());	
					}
					if(closeClaim1.getClosingRemarks() != null){
						reopenClaim.setCloseClaimRemarks(closeClaim1.getClosingRemarks());
					}
					
				}
				
				reopenClaimList.add(reopenClaim);
				
				reopenClaimDto.setProvisionAmount(provisionAmount);
				
				if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					List<Preauth> preauthList = preauthService.getPreauthListByClaimKey(claim.getKey());

					if(preauthList != null && preauthList.size() == 1){
						
						Preauth lastPreauth = preauthList.get(0);
						
						if(lastPreauth.getStatus() != null && (! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) || ! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS))){
							
							reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
							reopenClaim.setProvisionAmount(claim.getProvisionAmount());
							
						}else{
							
							if(lastPreauth.getWithdrawReason() != null && lastPreauth.getWithdrawReason().getKey() != null
									&& lastPreauth.getWithdrawReason().getKey().equals(ReferenceTable.PATIENT_NOT_ADMITTED)){
								reopenClaimDto.setProvisionAmount(0d);
							}else{
								reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
								reopenClaim.setProvisionAmount(claim.getProvisionAmount());
							}
							
						}
						
					}else{
						
						if(preauth != null && preauth.getStatus() != null && (! preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) || ! preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS))){

								reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
								reopenClaim.setProvisionAmount(claim.getProvisionAmount());

						}else{
							
							if(preauth != null && preauth.getWithdrawReason() != null && preauth.getWithdrawReason().getKey() != null
									&& preauth.getWithdrawReason().getKey().equals(ReferenceTable.PATIENT_NOT_ADMITTED)){
								reopenClaimDto.setProvisionAmount(0d);
							}else{
								reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
								reopenClaim.setProvisionAmount(claim.getProvisionAmount());
							}
						}

					}
				}else if(claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)
						&& reimbursementByClaimKey.isEmpty()){
					
					List<DocAcknowledgement> listOfAcknowledgement = createRodService.getListOfAcknowledgement(claim.getKey());

					if(listOfAcknowledgement != null && ! listOfAcknowledgement.isEmpty()){
						
						for (DocAcknowledgement docAcknowledgement : listOfAcknowledgement) {
							
							provisionAmount = 0d;
							
							if(null != docAcknowledgement){
								provisionAmount = provisionAmount + ackDocReceivedService.getClaimedAmountValueForView(docAcknowledgement);
							}
						}
						reopenClaimDto.setProvisionAmount(provisionAmount);
						
					}else{
						reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
					}

				}
				
				CloseClaim closeClaim = createRodService.getCloseClaim(claim.getKey());
				
				if(closeClaim != null && closeClaim.getClosedProvisionAmt() != null){
					reopenClaim.setProvisionAmount(closeClaim.getClosedProvisionAmt());
					reopenClaimDto.setProvisionAmount(closeClaim.getClosedProvisionAmt());
				}
				
				if(reopenClaimDto.getProvisionAmount() != null && balanceSI != null){
					
					Double minAmount = Math.min(reopenClaimDto.getProvisionAmount(), balanceSI);
					reopenClaimDto.setProvisionAmount(minAmount);
					
				}
				
				reopenClaimDto.setReopenClaimList(reopenClaimList);
				List<ViewDocumentDetailsDTO> listDocumentDetails = ackDocReceivedService.listOfEarlierAckByClaimKey(claim.getKey(),0l);
				
				for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : listDocumentDetails) {
				
					if(viewDocumentDetailsDTO.getStatusKey() != null && ReferenceTable.CANCEL_ROD_KEYS.containsKey(viewDocumentDetailsDTO.getStatusKey())){
						viewDocumentDetailsDTO.setIsReadOnly(true);
					}

					if(viewDocumentDetailsDTO.getReimbursementKey() != null){
						CloseClaim rodAlreadyClosed = createRodService.getAlreadyRodClosed(viewDocumentDetailsDTO.getReimbursementKey());
						if(rodAlreadyClosed != null){
							viewDocumentDetailsDTO.setApprovedAmount(0d);
							viewDocumentDetailsDTO.setIsReadOnly(true);
						}
					}
					
					if(viewDocumentDetailsDTO.getIsReadOnly()){
						viewDocumentDetailsDTO.setApprovedAmount(0d);
					}
					viewDocumentDetailsDTO.setApprovedAmount(0d);
				}
				
				for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : listDocumentDetails) {
					if(viewDocumentDetailsDTO.getModeOfReceipt() != null){
						viewDocumentDetailsDTO.setModeOfReceiptValue(viewDocumentDetailsDTO.getModeOfReceipt().getValue());
					}
					
					if(viewDocumentDetailsDTO.getDocumentReceivedDate() != null){
						viewDocumentDetailsDTO.setStrDocumentReceivedDate(SHAUtils.formatDate(viewDocumentDetailsDTO.getDocumentReceivedDate()));
					}
				}
				
				BeanItemContainer<SelectValue> reasonForClosing = masterService.getMasterValueByReference(ReferenceTable.REASON_FOR_REOPEN);
				
				reopenClaimDto.setReopenContainer(reasonForClosing);
				
				reopenClaimDto.setRodDocumentDetailsList(listDocumentDetails);
				
				reopenClaimDto.setIntimationNumber(claim.getIntimation().getIntimationId());
				
				reopenClaimDto.setClaimNumber(claim.getClaimId());
			}

			reopenClaimDto.setNewIntimationDto(intimationDto);

			view.setPAReopenClaimPageView(PAReopenClaimView.class, true, reopenClaimDto);
			
		
		}
		
		
		protected void showPAHealthReopenClaimPageView(
				@Observes @CDIEvent(PAMenuPresenter.REOPEN_PA_HEALTH_CLAIM_PAGE_VIEW) final ParameterDTO parameters) {

			
			PAHealthSearchReOpenClaimTableDTO tableDTO = (PAHealthSearchReOpenClaimTableDTO) parameters.getPrimaryParameter();
			
			NewIntimationDto intimationDto = new NewIntimationDto();
			
			PAHealthReopenClaimPageDTO reopenClaimDto = new PAHealthReopenClaimPageDTO();
			
			if(tableDTO.getIntimationkey() != null){
				
				Claim claim = claimService.getClaimByKey(tableDTO.getClaimKey());
				
				
				reopenClaimDto.setClaimKey(tableDTO.getClaimKey());

				
				Intimation intimation = claim.getIntimation();
				intimationDto = intimationService
						.getIntimationDto(intimation);
	          
				

				ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(claim);
				reopenClaimDto.setClaimDto(claimDTO);
				
				Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(intimationDto.getInsuredPatient().getInsuredId().toString(), intimationDto.getPolicy().getKey()
						,intimationDto.getInsuredPatient().getLopFlag());
				
				Reimbursement reimbursement = reimbursementService.getLatestActiveROD(claim.getKey());
				
				Double balanceSI = 0d;
				
				if(reimbursement != null){
					balanceSI = dbCalculationService.getPABalanceSI(intimationDto.getInsuredPatient().getKey(), 
							claim.getKey(),reimbursement.getKey(),(reimbursement.getBenefitsId()) != null ? reimbursement.getBenefitsId().getKey() : 0l );
				}
				
				reopenClaimDto.setBalanceSI(balanceSI);
				
				Preauth preauth= preauthService.getLatestPreauthDetails(claim.getKey());
				
				if(reimbursement==null){
					
					
					String diagnosisForPreauthByKey = "";
					if(preauth != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(preauth.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}else{
					String diagnosisForPreauthByKey = "";
					if(reimbursement != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursement.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
					reopenClaimDto.setDiagnosis(diagnosisForPreauthByKey);
				}
				
				List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
						.search(claim.getKey(), false);
				
				List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
				for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

					previousPreAuthTableDTO.setRequestedAmt(preauthService
							.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
									previousPreAuthTableDTO.getClaimKey()));
					newList.add(previousPreAuthTableDTO);

				}
				
				reopenClaimDto.setPreviousPreauthDetailsList(newList);

				List<PAHealthReopenClaimTableDTO> reopenClaimList = new ArrayList<PAHealthReopenClaimTableDTO>();
			
				PAHealthReopenClaimTableDTO reopenClaim = new PAHealthReopenClaimTableDTO();
				reopenClaim.setInsuredPatientName(intimation.getInsuredPatientName());
				reopenClaim.setStrDateOfAdmission(SHAUtils.formatDate(intimation.getAdmissionDate()));
				Long aCknowledgeNumberCountByClaimKey = createRodService.getACknowledgeNumberCountByClaimKey(claim.getKey());
				
				if(aCknowledgeNumberCountByClaimKey != null){
					reopenClaim.setNumberOfRod(aCknowledgeNumberCountByClaimKey.intValue());
				}
				
				List<Reimbursement> reimbursementByClaimKey = createRodService.getReimbursementByClaimKey(claim.getKey());
				
				reopenClaim.setClaimStatus(claim.getStatus().getProcessValue());

				Double provisionAmount = 0d;
				
				for (Reimbursement reimbursement2 : reimbursementByClaimKey) {
					
					if(reimbursement2.getCurrentProvisionAmt() != null){
					Double provisionAmount2 = reimbursement2.getCurrentProvisionAmt();
					if(provisionAmount2 != null){
						provisionAmount += provisionAmount2;
					}}
				}

				reopenClaim.setProvisionAmount(provisionAmount);
				
				CloseClaim closeClaim1 = createRodService.getCloseClaim(claim.getKey());
				
				if(closeClaim1 != null){
					if(closeClaim1.getClosedDate() != null){
						reopenClaim.setStrDateOfClosedClaim(SHAUtils.formatDate(closeClaim1.getClosedDate()));	
					}
					if(closeClaim1.getClosingReasonId() != null){
						reopenClaim.setReasonForClosure(closeClaim1.getClosingReasonId().getValue());	
					}
					if(closeClaim1.getClosingRemarks() != null){
						reopenClaim.setCloseClaimRemarks(closeClaim1.getClosingRemarks());
					}
					
				}
				
				reopenClaimList.add(reopenClaim);
				
				reopenClaimDto.setProvisionAmount(provisionAmount);
				
				if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					List<Preauth> preauthList = preauthService.getPreauthListByClaimKey(claim.getKey());

					if(preauthList != null && preauthList.size() == 1){
						
						Preauth lastPreauth = preauthList.get(0);
						
						if(lastPreauth.getStatus() != null && (! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) || ! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS))){
							
							reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
							reopenClaim.setProvisionAmount(claim.getProvisionAmount());
							
						}else{
							
							if(lastPreauth.getWithdrawReason() != null && lastPreauth.getWithdrawReason().getKey() != null
									&& lastPreauth.getWithdrawReason().getKey().equals(ReferenceTable.PATIENT_NOT_ADMITTED)){
								reopenClaimDto.setProvisionAmount(0d);
							}else{
								reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
								reopenClaim.setProvisionAmount(claim.getProvisionAmount());
							}
							
						}
						
					}else{
						
						if(preauth != null && preauth.getStatus() != null && (! preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS) || ! preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS))){

								reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
								reopenClaim.setProvisionAmount(claim.getProvisionAmount());

						}else{
							
							if(preauth != null && preauth.getWithdrawReason() != null && preauth.getWithdrawReason().getKey() != null
									&& preauth.getWithdrawReason().getKey().equals(ReferenceTable.PATIENT_NOT_ADMITTED)){
								reopenClaimDto.setProvisionAmount(0d);
							}else{
								reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
								reopenClaim.setProvisionAmount(claim.getProvisionAmount());
							}
						}

					}
				}else if(claim.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)
						&& reimbursementByClaimKey.isEmpty()){
					
					List<DocAcknowledgement> listOfAcknowledgement = createRodService.getListOfAcknowledgement(claim.getKey());

					if(listOfAcknowledgement != null && ! listOfAcknowledgement.isEmpty()){
						
						for (DocAcknowledgement docAcknowledgement : listOfAcknowledgement) {
							
							provisionAmount = 0d;
							
							if(null != docAcknowledgement){
								provisionAmount = provisionAmount + ackDocReceivedService.getClaimedAmountValueForView(docAcknowledgement);
							}
						}
						reopenClaimDto.setProvisionAmount(provisionAmount);
						
					}else{
						reopenClaimDto.setProvisionAmount(claim.getProvisionAmount());
					}

				}
				
				CloseClaim closeClaim = createRodService.getCloseClaim(claim.getKey());
				
				if(closeClaim != null && closeClaim.getClosedProvisionAmt() != null){
					reopenClaim.setProvisionAmount(closeClaim.getClosedProvisionAmt());
					reopenClaimDto.setProvisionAmount(closeClaim.getClosedProvisionAmt());
				}
				
				if(reopenClaimDto.getProvisionAmount() != null && balanceSI != null){
					
					Double minAmount = Math.min(reopenClaimDto.getProvisionAmount(), balanceSI);
					reopenClaimDto.setProvisionAmount(minAmount);
					
				}
				
				reopenClaimDto.setReopenClaimList(reopenClaimList);
				List<ViewDocumentDetailsDTO> listDocumentDetails = ackDocReceivedService.listOfEarlierAckByClaimKey(claim.getKey(),0l);
				
				for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : listDocumentDetails) {
				
					if(viewDocumentDetailsDTO.getStatusKey() != null && ReferenceTable.CANCEL_ROD_KEYS.containsKey(viewDocumentDetailsDTO.getStatusKey())){
						viewDocumentDetailsDTO.setIsReadOnly(true);
					}

					if(viewDocumentDetailsDTO.getReimbursementKey() != null){
						CloseClaim rodAlreadyClosed = createRodService.getAlreadyRodClosed(viewDocumentDetailsDTO.getReimbursementKey());
						if(rodAlreadyClosed != null){
							viewDocumentDetailsDTO.setApprovedAmount(0d);
							viewDocumentDetailsDTO.setIsReadOnly(true);
						}
					}
					
					if(viewDocumentDetailsDTO.getIsReadOnly()){
						viewDocumentDetailsDTO.setApprovedAmount(0d);
					}
					viewDocumentDetailsDTO.setApprovedAmount(0d);
				}
				
				for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : listDocumentDetails) {
					if(viewDocumentDetailsDTO.getModeOfReceipt() != null){
						viewDocumentDetailsDTO.setModeOfReceiptValue(viewDocumentDetailsDTO.getModeOfReceipt().getValue());
					}
					
					if(viewDocumentDetailsDTO.getDocumentReceivedDate() != null){
						viewDocumentDetailsDTO.setStrDocumentReceivedDate(SHAUtils.formatDate(viewDocumentDetailsDTO.getDocumentReceivedDate()));
					}
				}
				
				BeanItemContainer<SelectValue> reasonForClosing = masterService.getMasterValueByReference(ReferenceTable.REASON_FOR_REOPEN);
				
				reopenClaimDto.setReopenContainer(reasonForClosing);
				
				reopenClaimDto.setRodDocumentDetailsList(listDocumentDetails);
				
				reopenClaimDto.setIntimationNumber(claim.getIntimation().getIntimationId());
				
				reopenClaimDto.setClaimNumber(claim.getClaimId());
			}

			reopenClaimDto.setNewIntimationDto(intimationDto);

			view.setPAHealthReopenClaimPageView(PAHealthReopenClaimView.class, true, reopenClaimDto);
			
		
		}
		
		protected void showPASettlementPullBakcScreen(
				@Observes @CDIEvent(PAMenuPresenter.SHOW_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
			PASearchSettlementPullBackDTO tableDTO = (PASearchSettlementPullBackDTO) parameters.getPrimaryParameter();
				view.setPASettlementPullBackScreen(PASettlementPullBackView.class, tableDTO);
		}
		
		protected void showPAHospSettlementPullBakcScreen(
				@Observes @CDIEvent(PAMenuPresenter.SHOW_PA_HOSP_SETTLEMENT_PULL_BACK) final ParameterDTO parameters) {
			PAHospSearchSettlementPullBackDTO tableDTO = (PAHospSearchSettlementPullBackDTO) parameters.getPrimaryParameter();
				view.setPAHospSettlementPullBackScreen(PAHospSettlementPullBackView.class, tableDTO);
		}
		
		
		protected void showPAReopenClaimView(
				@Observes @CDIEvent(PAMenuPresenter.REOPEN_CLAIM_PAGE) final ParameterDTO parameters){
			
			PASearchReOpenClaimRodLevelTableDTO tableDTO = (PASearchReOpenClaimRodLevelTableDTO) parameters.getPrimaryParameter();
			
			NewIntimationDto intimationDto = new NewIntimationDto();
			
			Boolean isValid = true;
			
			if(tableDTO.getIntimationkey() != null){
				Intimation intimation = intimationService.getIntimationByKey(tableDTO.getIntimationkey());
				intimationDto = intimationService
						.getIntimationDto(intimation);
				
	            Claim claim = claimService.getClaimsByIntimationNumber(intimation.getIntimationId());
	            
	            if(claim.getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
	            	
	            	isValid = false;
	            	view.showErrorPopUp("Reopen of ROD is not possible. Since Claim is already closed");
	            	
	            }
	            
				
				ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claim);
				tableDTO.setClaimDto(claimDTO);
				
				Reimbursement reimbursement = reimbursementService.getLatestActiveROD(claim.getKey());
				if(reimbursement==null){
					Preauth preauth= preauthService.getLatestPreauthByClaim(claim.getKey());
					
					String diagnosisForPreauthByKey = "";
					if(preauth != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(preauth.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}else{
					String diagnosisForPreauthByKey = "";
					if(reimbursement != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursement.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}
				
			}
			
			
			tableDTO.setIntimationDto(intimationDto);
			tableDTO.setDbOutArray(tableDTO);
			
			if(isValid){
			view.setPAReopenClaimSearchBasedView(PAReOpenRodLevelClaimView.class, true, tableDTO);
			}
			
		}
		
		protected void showPAHealthReopenClaimView(
				@Observes @CDIEvent(PAMenuPresenter.REOPEN_HEALTH_CLAIM_PAGE) final ParameterDTO parameters){
			
			PAHealthSearchReOpenClaimRodLevelTableDTO tableDTO = (PAHealthSearchReOpenClaimRodLevelTableDTO) parameters.getPrimaryParameter();
			
			NewIntimationDto intimationDto = new NewIntimationDto();
			
			Boolean isValid = true;
			
			if(tableDTO.getIntimationkey() != null){
				Intimation intimation = intimationService.getIntimationByKey(tableDTO.getIntimationkey());
				intimationDto = intimationService
						.getIntimationDto(intimation);
				
	            Claim claim = claimService.getClaimsByIntimationNumber(intimation.getIntimationId());
	            
	            if(claim.getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
	            	
	            	isValid = false;
	            	view.showErrorPopUp("Reopen of ROD is not possible. Since Claim is already closed");
	            	
	            }
	            
				
				ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claim);
				tableDTO.setClaimDto(claimDTO);
				
				Reimbursement reimbursement = reimbursementService.getLatestActiveROD(claim.getKey());
				if(reimbursement==null){
					Preauth preauth= preauthService.getLatestPreauthByClaim(claim.getKey());
					
					String diagnosisForPreauthByKey = "";
					if(preauth != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(preauth.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}else{
					String diagnosisForPreauthByKey = "";
					if(reimbursement != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursement.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}
				
			}
			
			
			tableDTO.setIntimationDto(intimationDto);
			
			if(isValid){
			view.setPAHealthReopenClaimSearchBasedView(PAHealthReOpenRodLevelClaimView.class, true, tableDTO);
			}
			
		}

		protected void showPAAddAditionalDocumentsWizard(
				@Observes @CDIEvent(PAMenuPresenter.PA_ADD_ADDITIONAL_DOCUMENTS) final ParameterDTO parameters){		


			SearchAddAdditionalDocumentTableDTO tableDTO = (SearchAddAdditionalDocumentTableDTO) parameters
					.getPrimaryParameter();
			ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
			SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO = new SelectRODtoAddAdditionalDocumentsDTO();

			Long claimKey = tableDTO.getClaimKey();
			Claim claim = claimService.getClaimByKey(claimKey);

			Boolean isValidClaim = true;
			if (null != claim) {
				if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claim
						.getLegalFlag())) {
					isValidClaim = false;
					view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
				}
			}
			
			if(isValidClaim) {
			
			List<Reimbursement> reimbursementList = reimbursementService
					.getReimbursementByClaimKey(claimKey);
			
			//List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
			Map<Long, Long> rejectMap = ReferenceTable.REJECT_ROD_KEYS;
			/*if(null != reimbursementListObj && !reimbursementListObj.isEmpty())
			{
				for (Reimbursement reimbursement : reimbursementListObj) {
					if(null != rejectMap && !rejectMap.isEmpty() && null == rejectMap.get(reimbursement.getStatus().getKey()))
					{
						reimbursementList.add(reimbursement);
					}
				}
			}		
			
	*/
			
			if (null != claim) {

				if (null != claim.getIncidenceFlag()
						&& claim.getIncidenceFlag().equalsIgnoreCase(
								SHAConstants.ACCIDENT_FLAG)) {
					rodDTO.getDocumentDetails().setAccidentOrDeath(true);
				} else {
					rodDTO.getDocumentDetails().setAccidentOrDeath(false);
				}
			}
				
			if (tableDTO.getKey() != null) {
				Reimbursement reimbursement = reimbursementService
						.getReimbursementbyRod(tableDTO.getKey());
			
				if(null != reimbursement)
				{
					rodDTO.getDocumentDetails().setDocAcknowledgementKey(
							reimbursement.getDocAcknowLedgement().getKey());
					rodDTO.getDocumentDetails().setDateOfDischarge(reimbursement.getDateOfDischarge());
					rodDTO.getDocumentDetails().setRodKey(reimbursement.getKey());
				}
				
				
				String diagnosisForPreauthByKey = "";
				if(reimbursement != null ) {
					diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursement.getKey());
					
				}
				rodDTO.setDiagnosis(diagnosisForPreauthByKey);
				
			}

			DocAcknowledgement docAcknowlegement = acknowledgementDocumentsReceivedService
					.getDocAcknowledgment(rodDTO.getDocumentDetails()
							.getDocAcknowledgementKey());

			rodDTO.getDocumentDetails().setAcknowledgementNumber(
					docAcknowlegement.getAcknowledgeNumber());
			rodDTO.getDocumentDetails().setRodKey(tableDTO.getKey());
			
			if(null != docAcknowlegement.getModeOfReceiptId())
			{
				rodDTO.getDocumentDetails().setModeOfReceiptValue(docAcknowlegement.getModeOfReceiptId().getValue());
			}
			
			rodDTO.getDocumentDetails().setAdditionalRemarks(docAcknowlegement.getAdditionalRemarks());

			rodDTO.getDocumentDetails().setAcknowledgmentContactNumber(docAcknowlegement.getInsuredContactNumber());
			rodDTO.getDocumentDetails().setEmailId(docAcknowlegement.getInsuredEmailId());
			rodDTO.getDocumentDetails().setDocumentsReceivedDate(docAcknowlegement.getDocumentReceivedDate());
			/*if(null != docAcknowlegement.getSourceOfDocument())
			{
				if(SHAConstants.SOURCE_DOC_INSURED.equalsIgnoreCase(docAcknowlegement.getSourceOfDocument())){
			rodDTO.getDocumentDetails().setSourceOfDocument(Boolean.TRUE);
		}
		else if(SHAConstants.SOURCE_DOC_INTERNAL.equalsIgnoreCase(docAcknowlegement.getSourceOfDocument())){
			rodDTO.getDocumentDetails().setSourceOfDocument(Boolean.FALSE);
		}
			}*/
			
			rodDTO.getDocumentDetails().setHospitalizationFlag(
					docAcknowlegement.getHospitalisationFlag());
			rodDTO.getDocumentDetails().setPreHospitalizationFlag(
					docAcknowlegement.getPreHospitalisationFlag());
			rodDTO.getDocumentDetails().setPostHospitalizationFlag(
					docAcknowlegement.getPostHospitalisationFlag());
			rodDTO.getDocumentDetails().setPartialHospitalizationFlag(
					docAcknowlegement.getPartialHospitalisationFlag());
			rodDTO.getDocumentDetails().setLumpSumAmountFlag(
					docAcknowlegement.getLumpsumAmountFlag());
			if (docAcknowlegement.getHospitalizationClaimedAmount() != null) {
				rodDTO.getDocumentDetails().setHospitalizationClaimedAmount(
						docAcknowlegement.getHospitalizationClaimedAmount()
								.toString());
			}

			SelectRODtoAddAdditionalDocumentsMapper selectRODtoAddAdditionalDocumentsMapper = new SelectRODtoAddAdditionalDocumentsMapper();
			List<SelectRODtoAddAdditionalDocumentsDTO> selectRODtoAddAdditionalDocumentsDTOList = selectRODtoAddAdditionalDocumentsMapper
					.getReimbursementDto(reimbursementList);
			
			String benefitcover = "";
			for (SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO2 : selectRODtoAddAdditionalDocumentsDTOList) {
				if(selectRODtoAddAdditionalDocumentsDTO2 != null && selectRODtoAddAdditionalDocumentsDTO2.getStatusKey() != null){
				if(selectRODtoAddAdditionalDocumentsDTO2.getStatusKey().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY) || selectRODtoAddAdditionalDocumentsDTO2.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
				|| selectRODtoAddAdditionalDocumentsDTO2.getStatusKey().equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)){
					
					selectRODtoAddAdditionalDocumentsDTO2.setApprovedAmt(null);
					
				}
				}
								
			}

			Long count = getAckNoCountByClaimKey(claimKey,
					ReferenceTable.ACKNOWLEDGE_DOC_RECEIVED);
			rodDTO.setAcknowledgementNumber(count);
			ClaimDto claimDTO  = null;
			NewIntimationDto newIntimationDto = new NewIntimationDto();
			if (claim != null) {
				newIntimationDto = intimationService.getIntimationDto(claim
						.getIntimation());
				claimDTO =  ClaimMapper.getInstance().getClaimDto(claim);
				claimDTO.setNewIntimationDto(newIntimationDto);
				rodDTO.setClaimDTO(claimDTO);

			}
			generateAcknowledgeNo(rodDTO);

			/*
			 * rodDTO.getDocumentDetails().setDocumentCheckList(
			 * getDocumentCheckList(claimByKey));
			 */

			rodDTO.setProductBenefitMap(dbCalculationService.getProductBenefitFlag(
					claim.getKey(), claim.getIntimation().getPolicy().getProduct()
							.getKey()));
			
			List<DocumentCheckListDTO> setPADocumentCheckListTableValues = setPADocumentCheckListTableValues();
			
			List<DocumentCheckListDTO> documentCheckList = new ArrayList<DocumentCheckListDTO>();
			int i=0;
			for (DocumentCheckListDTO documentCheckListDTO : setPADocumentCheckListTableValues) {
				documentCheckList.add(documentCheckListDTO);
				i++;
				if(i == 5){
					break;
				}
			}				
			
			rodDTO.getDocumentDetails().setDocumentCheckList(documentCheckList);

			rodDTO.setReconsiderRodRequestList(getReconsiderRODRequest(claim));
			rodDTO.setRodQueryDetailsList(getRODQueryDetailsValues(claim));

//			List<Reimbursement> reimbursementByStageId = reimbursementService
//					.getReimbursementByClaimKeyAndStageId(claimKey);
			/*List<Reimbursement> reimbursementByStageId = reimbursementService
				.getReimbursementByClaimKey(claimKey);*/
			List<Reimbursement> reimbursementByStageId = new ArrayList<Reimbursement>();
			List<Reimbursement> reimbursementListObj = reimbursementService
			.getReimbursementByClaimKey(claimKey);
			
			if(null != reimbursementListObj && !reimbursementListObj.isEmpty())
			{
				for (Reimbursement reimbursement : reimbursementListObj) {
					if(null != rejectMap && !rejectMap.isEmpty() && null == rejectMap.get(reimbursement.getStatus().getKey()))
					{
						reimbursementByStageId.add(reimbursement);
					}
				}
			}

			selectRODtoAddAdditionalDocumentsDTOList = selectRODtoAddAdditionalDocumentsMapper
					.getReimbursementDto(reimbursementByStageId);

			for (int index = 0; index < selectRODtoAddAdditionalDocumentsDTOList
					.size(); index++) {
				if (reimbursementByStageId.get(index).getStatus().getKey()
						.equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
					selectRODtoAddAdditionalDocumentsDTOList
							.get(index)
							.setApprovedAmt(
									reimbursementByStageId.get(index)
											.getBillingApprovedAmount() != null ? reimbursementByStageId
											.get(index).getBillingApprovedAmount()
											.toString()
											: "");
				} else if (reimbursementByStageId.get(index).getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					selectRODtoAddAdditionalDocumentsDTOList
							.get(index)
							.setApprovedAmt(
									reimbursementByStageId.get(index)
											.getFinancialApprovedAmount() != null ? reimbursementByStageId
											.get(index)
											.getFinancialApprovedAmount()
											.toString()
											: "");
				}
				else if (reimbursementByStageId.get(index).getStatus().getKey()
						.equals(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS)) {
					selectRODtoAddAdditionalDocumentsDTOList
							.get(index)
							.setApprovedAmt(
									reimbursementByStageId.get(index)
											.getClaimApprovalAmount() != null ? reimbursementByStageId
											.get(index)
											.getClaimApprovalAmount()
											.toString()
											: "");
				}
				else {
					selectRODtoAddAdditionalDocumentsDTOList
							.get(index)
							.setApprovedAmt(
									reimbursementByStageId.get(index)
											.getCurrentProvisionAmt() != null ? reimbursementByStageId
											.get(index).getCurrentProvisionAmt()
											.toString()
											: "");
				}
			}

			for (int index = 0; index < reimbursementByStageId.size(); index++) {
				String billClassificaiton = "";
				Double claimedAmt = 0d;
				if (reimbursementByStageId.get(index) != null
						&& reimbursementByStageId.get(index)
								.getDocAcknowLedgement() != null) {
					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getHospitalisationFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement().getHospitalisationFlag()
								.equalsIgnoreCase("y")) {
							billClassificaiton = billClassificaiton
									+ "Hospitalisation, ";
						}

					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getPreHospitalisationFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement()
								.getPreHospitalisationFlag().toString()
								.equalsIgnoreCase("y")) {
							billClassificaiton = billClassificaiton
									+ "Pre - Hospitalisation, ";
						}
					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getPostHospitalisationFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement()
								.getPostHospitalisationFlag().toString()
								.equalsIgnoreCase("y")) {
							billClassificaiton = billClassificaiton
									+ "Post - Hospitalisation,  ";
						}

					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getPartialHospitalisationFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement()
								.getPartialHospitalisationFlag().toString()
								.equalsIgnoreCase("y")) {
							billClassificaiton = billClassificaiton
									+ "Partial - Hospitalisation,  ";
						}

					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getLumpsumAmountFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement().getLumpsumAmountFlag()
								.toString().equalsIgnoreCase("y")) {
							billClassificaiton = billClassificaiton
									+ "Lumpsum - Amount,  ";
						}
					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getHospitalCashFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement().getHospitalCashFlag()
								.toString().equalsIgnoreCase("y")) {
							billClassificaiton = billClassificaiton
									+ "Hospital - Cash,  ";
						}
					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getHospitalizationRepeatFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement().getHospitalizationRepeatFlag()
								.toString().equalsIgnoreCase("y")) {
							billClassificaiton = billClassificaiton
									+ "Hospitalisation - Repeat,  ";
						}
					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getPatientCareFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement()
								.getPatientCareFlag().toString()
								.equalsIgnoreCase("y")) {
							billClassificaiton = billClassificaiton
									+ "Patient - Care,  ";
						}
					}

				}
				if (billClassificaiton.length() > 0) {
					billClassificaiton = billClassificaiton.substring(0,
							billClassificaiton.length() - 2);
					selectRODtoAddAdditionalDocumentsDTOList.get(index)
							.setBillClassification(billClassificaiton);

				}
				claimedAmt = createRodService.getPAClaimedAmnt(selectRODtoAddAdditionalDocumentsDTOList.get(index).getKey());
				if(claimedAmt != null){
				selectRODtoAddAdditionalDocumentsDTOList.get(index).setClaimedAmt(
						claimedAmt.toString());
				}
			}

			Integer index = 1;
			String benefitAndCover = "";
			List<SelectRODtoAddAdditionalDocumentsDTO> finalSelectRODtoAddAdditionalDocumentsDTOList = new ArrayList<SelectRODtoAddAdditionalDocumentsDTO>();
			for (SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO1 : selectRODtoAddAdditionalDocumentsDTOList) {
				selectRODtoAddAdditionalDocumentsDTO1.setsNo(index.toString());
				index++;
				
				benefitcover = acknowledgementDocumentsReceivedService.getCoverValueForViewBasedOnrodKey(selectRODtoAddAdditionalDocumentsDTO1.getKey());
				Reimbursement reimb = acknowledgementDocumentsReceivedService.getReimbursement(selectRODtoAddAdditionalDocumentsDTO1.getKey());
				if(null != reimb && null != reimb.getBenefitsId() && null != reimb.getBenefitsId().getValue())
				{
					benefitAndCover = reimb.getBenefitsId().getValue() + ", " + benefitcover;
				}
				selectRODtoAddAdditionalDocumentsDTO1.setCoverCode(benefitAndCover);
				
				finalSelectRODtoAddAdditionalDocumentsDTOList.add(selectRODtoAddAdditionalDocumentsDTO1);
			}
			/*List<UploadDocumentDTO> rodSummaryDetails = rodService
					.getRODSummaryDetails(tableDTO.getKey());
			
			if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
			{
				List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
			//	List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();
				for (UploadDocumentDTO uploadDocDTO : rodSummaryDetails) {
					//sss
					uploadDocDTO.setIsBillSaved(true);
					List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
					List<RODBillDetails> billEntryDetails = rodService
							.getBillEntryDetails(uploadDocDTO.getDocSummaryKey());
					if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
						for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
							
							 * <<<<<<< HEAD
							 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
							 * uploadDocumentDTO)); =======
							 
							dtoList.add(getBillDetailsDTOForBillEntry(billEntryDetailsDO));
							
							// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
						}
						uploadDocDTO.setBillEntryDetailList(dtoList);
					}
				}
			//	rodDTO.getUploadDocumentsDTO().setBillEntryDetailList(dtoList);
				rodDTO.setUploadDocsList(rodSummaryDetails);
				
			}*/

			rodDTO.setSelectRODtoAddAdditionalDocumentsDTO(finalSelectRODtoAddAdditionalDocumentsDTOList);

			rodDTO.setStrUserName(tableDTO.getUsername());
			
			RRCDTO rrcDTO = new RRCDTO();
			rrcDTO.setClaimDto(claimDTO);
			rrcDTO.setNewIntimationDTO(newIntimationDto);
			rrcDTO.setStrUserName(tableDTO.getUsername());
			
			Double insuredSumInsured = 0d;
			
			if(null != claim &&  null != claim.getIntimation() && 
					null != claim.getIntimation().getPolicy() && null != claim.getIntimation().getPolicy().getProduct() &&
					null != claim.getIntimation().getPolicy().getProduct().getKey() 
					&& !(ReferenceTable.getGPAProducts().containsKey(claim.getIntimation().getPolicy().getProduct().getKey())))
			{

				insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					rrcDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), rrcDTO.getNewIntimationDTO().getPolicy().getKey()
							,rrcDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						rrcDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), rrcDTO.getNewIntimationDTO().getPolicy().getKey());
			}
			
			
			loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured, SHAConstants.ADD_ADDITIONAL_DOCUMENTS);
			
			//loadRRCRequestValuesForProcessing(rrcDTO,insuredSumInsured,SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
			
			rodDTO.setRrcDTO(rrcDTO);

			
			
			
			/*
			 * rodDTO.setStrUserName("oprofficeclaimscoordinator");
			 * rodDTO.setStrPassword("welcome1");
			 */

			view.setPAAddAditionalDocumentsWizard(PAAddAdditionalDocumentsWizardView.class,
					rodDTO);
			}
		
		}
		
		private List<DocumentCheckListDTO> setDocumentCheckListTableValues() {

			return ackDocReceivedService.getDocumentCheckListValues(masterService);
		}
		
		protected void showPAAddAdditionalDoc(
				@Observes @CDIEvent(MenuItemBean.PA_ADD_ADDITIONAL_DOCUMENTS) final ParameterDTO parameters) {
			view.setViewG(PAAddAdditionalDocumentsView.class, true);

		}
		
		protected void showPACloseClaimSearchBasedView(
				@Observes @CDIEvent(PAMenuPresenter.CLOSE_CLAIM_SEARCH_BASED_ROD_LEVEL) final ParameterDTO parameters){
			
			PASearchCloseClaimTableDTORODLevel tableDTO = (PASearchCloseClaimTableDTORODLevel) parameters.getPrimaryParameter();
			
			NewIntimationDto intimationDto = new NewIntimationDto();
		
			Boolean isCloseClaimValid = true;
			if(tableDTO.getIntimationkey() != null){
				Intimation intimation = intimationService.getIntimationByKey(tableDTO.getIntimationkey());
				intimationDto = intimationService
						.getIntimationDto(intimation);
				Claim claim = claimService.getClaimsByIntimationNumber(intimation.getIntimationId());
				
				ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claim);
				tableDTO.setClaimDto(claimDTO);
				
				List<Reimbursement> reimbursementByClaimKey = reimbursementService.getRembursementDetails(claim.getKey());
				//IMSSUPPOR-37576 rejection rod need stop close
				if(reimbursementByClaimKey != null && ! reimbursementByClaimKey.isEmpty() && reimbursementByClaimKey.size()==1){
						if(ReferenceTable.getRejectedRODKeys().containsKey(reimbursementByClaimKey.get(0).getStatus().getKey())){
							
							if(isCloseClaimValid){
								isCloseClaimValid = false;
								view.showErrorPopUp("Closing of claim is not possible. Since ROD is Rejected");
							}
						}	
				}
				
				Reimbursement reimbursement = reimbursementService.getLatestActiveROD(claim.getKey());
				if(reimbursement==null){
					Preauth preauth= preauthService.getLatestPreauthByClaim(claim.getKey());
					
					String diagnosisForPreauthByKey = "";
					if(preauth != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(preauth.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}else{
					String diagnosisForPreauthByKey = "";
					if(reimbursement != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursement.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}
				
			}
			
			
			
			tableDTO.setIntimationDto(intimationDto);
			if(isCloseClaimValid){
				view.setPACloseClaimSearchBasedRODLevelView(PACloseClaimRodLevelView.class, true, tableDTO);
			}
			
		}
		
		protected void showPACloseClaimPageView(
				@Observes @CDIEvent(PAMenuPresenter.CLOSE_CLAIM_PAGE) final ParameterDTO parameters){
			
			Boolean isCloseClaimValid = true;
			
			PASearchCloseClaimTableDTO tableDTO = (PASearchCloseClaimTableDTO) parameters.getPrimaryParameter();
			
			NewIntimationDto intimationDto = new NewIntimationDto();
			
			PACloseClaimPageDTO closeClaimDto = new PACloseClaimPageDTO();

			if(tableDTO.getIntimationkey() != null){
				
				Claim claim = claimService.getClaimByKey(tableDTO.getClaimKey());

				closeClaimDto.setClaimKey(tableDTO.getClaimKey());
				
				closeClaimDto.setClosedProvisionAmt(claim.getCurrentProvisionAmount());
				
				Intimation intimation = claim.getIntimation();
				intimationDto = intimationService
						.getIntimationDto(intimation);
	          

				ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(claim);
				
				closeClaimDto.setClaimDto(claimDTO);
				
				Reimbursement reimbursement = reimbursementService.getLatestActiveROD(claim.getKey());
				if(reimbursement==null){
					Preauth preauth= preauthService.getLatestPreauthByClaim(claim.getKey());
					
					String diagnosisForPreauthByKey = "";
					if(preauth != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(preauth.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
					closeClaimDto.setDiagnosis(diagnosisForPreauthByKey);
				}else{
					String diagnosisForPreauthByKey = "";
					if(reimbursement != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursement.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
					closeClaimDto.setDiagnosis(diagnosisForPreauthByKey);
				}
				
				List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
						.search(claim.getKey(), false);
				
				List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
				for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

					previousPreAuthTableDTO.setRequestedAmt(preauthService
							.getPreauthReqAmt(previousPreAuthTableDTO.getKey(),
									previousPreAuthTableDTO.getClaimKey()));
					DocAcknowledgement docAck = ackDocReceivedService.findAcknowledgmentByClaimKey(previousPreAuthTableDTO.getClaimKey());
					if(docAck != null){
						if(docAck.getDocumentReceivedDate() != null){
							previousPreAuthTableDTO.setStrDocReceivedDate(SHAUtils.formatDate(docAck.getDocumentReceivedDate()));	
						}
						if(docAck.getDocumentReceivedFromId() != null && docAck.getDocumentReceivedFromId().getValue() != null){
							previousPreAuthTableDTO.setDocReceivedFrom(docAck.getDocumentReceivedFromId().getValue());
						}
					}
					
					newList.add(previousPreAuthTableDTO);

				}
				
				closeClaimDto.setPreviousPreauthDetailsList(newList);
				
				
				List<PACloseClaimTableDTO> closeClaimList = new ArrayList<PACloseClaimTableDTO>();
			
				PACloseClaimTableDTO closeClaim = new PACloseClaimTableDTO();
				closeClaim.setInsuredPatientName(intimation.getInsuredPatientName());
				closeClaim.setStrDateOfAdmission(SHAUtils.formatDate(intimation.getAdmissionDate()));
				Long aCknowledgeNumberCountByClaimKey = createRodService.getACknowledgeNumberCountByClaimKey(claim.getKey());
				
				if(aCknowledgeNumberCountByClaimKey != null){
					closeClaim.setNumberOfRod(aCknowledgeNumberCountByClaimKey.intValue());
				}
				closeClaim.setProvisionAmount(claim.getCurrentProvisionAmount());
				closeClaim.setClaimStatus(claim.getStatus().getProcessValue());
				closeClaimList.add(closeClaim);
				
				closeClaimDto.setCloseClaimList(closeClaimList);
				
				List<ViewDocumentDetailsDTO> listDocumentDetails = ackDocReceivedService.listOfEarlierAckByClaimKey(claim.getKey(),0l);
				
				closeClaimDto.setRodDocumentDetailsList(listDocumentDetails);
				
				BeanItemContainer<SelectValue> referenceContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				
				List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claim.getKey());
				
				Preauth latestPreauth = preauthService.getLatestPreauthDetails(claim.getKey());
				
				if(latestPreauth != null){
					SelectValue select = new SelectValue();
					select.setId(latestPreauth.getKey());
					if(latestPreauth.getEnhancementType() != null){
						select.setValue(latestPreauth.getPreauthId() +" - Enhancement");
					}else{
						select.setValue(latestPreauth.getPreauthId() +" - Preauth");
					}
					
					referenceContainer.addBean(select);
				}
				
				List<Reimbursement> reimbursementByClaimKey = createRodService.getReimbursementByClaimKey(claim.getKey());
				
				if(claim.getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
					
					isCloseClaimValid = false;
					view.showErrorPopUp("Closing of claim is not possible. Since Claim is already closed");
				}
				
				if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					if(latestPreauth !=  null && latestPreauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)){
						if(isCloseClaimValid){
							isCloseClaimValid = false;
							view.showErrorPopUp("Closing of claim is not possible. Since Preauth is Rejected");
						}
					}
				}
				
//				List<DocAcknowledgement> listOfAcknowledgement = createRodService.getListOfAcknowledgement(claim.getKey());
				
				List<DocAcknowledgement> listOfAcknowledgement = createRodService.getNonCancelledAcknowledgement(claim.getKey());
				
				if(listOfAcknowledgement != null){
					for (DocAcknowledgement docAcknowledgement : listOfAcknowledgement) {
						if(docAcknowledgement.getRodKey() == null){
							if(isCloseClaimValid){
								isCloseClaimValid = false;
								view.showErrorPopUp("Closing of claim is not possible. Since Rod is not created for acknowledgement");
							}
						}
					}
				}
				
				if(reimbursementByClaimKey != null && ! reimbursementByClaimKey.isEmpty()){
					for (Reimbursement reimbursement2 : reimbursementByClaimKey) {
						if(ReferenceTable.getFinancialApprovalStatus().containsKey(reimbursement2.getStatus().getKey())){
							
							if(isCloseClaimValid){
								isCloseClaimValid = false;
								view.showErrorPopUp("Closing of claim is not possible. Since ROD is already Approved");
								break;
							}
						}
					}
				}
				
				
				
				Boolean isRodPaymentRejected = reimbursementService.isAllRODPaymentRejected(claim.getKey());
				
				if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && !isRodPaymentRejected){
					
					List<Preauth> preauthList = preauthService.getPreauthListByClaimKey(claim.getKey());

					if(preauthList != null && preauthList.size() >1){
						Preauth lastPreauth = preauthList.get(0);
						if(! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
								&& ! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
								&& ! lastPreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
								&& ! lastPreauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)){

							if(isCloseClaimValid){
								isCloseClaimValid = false;
								view.showErrorPopUp("Closing of claim is not possible. Please Withdraw the preauth");
							}
						}
					}else if(preauthList != null && preauthList.size() == 1){
						Preauth lastPreauth = preauthList.get(0);
						if(lastPreauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)){
							
							if(isCloseClaimValid){
								isCloseClaimValid = false;
								view.showErrorPopUp("Closing of claim is not possible. Please Withdraw the preauth");
							}
						}
					}
				}
				
//				for (DocAcknowledgement docAcknowledgement : docAckListByClaim) {
//					if(docAcknowledgement.getRodKey() == null){
//						
//						isCloseClaimValid = false;
//						view.showErrorPopUp("Closing of claim is not possible. ");
//					}
//				}
				
				
				if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthByClaimKey != null && ! preauthByClaimKey.isEmpty() 
						&& reimbursementByClaimKey != null && ! reimbursementByClaimKey.isEmpty() && !isRodPaymentRejected){
					
					for (Reimbursement reimbursement2 : reimbursementByClaimKey) {
						if(!(ReferenceTable.getCancelRODKeys().containsKey(reimbursement2.getStatus().getKey())) && ! reimbursement2.getDocAcknowLedgement().getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS)){
							
							if(isCloseClaimValid){
								isCloseClaimValid = false;
								view.showErrorPopUp("Closing of claim is not possible. Please Process/Cancel and then trying Closing");
							}
						}
					}
					
					if(isCloseClaimValid){
						Boolean preauthApprovedStatus = preauthService.isPreauthApprovedStatus(intimation, claim);
						Boolean enhancementApprovedOrDownsize = preauthService.isEnhancementApprovedOrDownsize(intimation, claim);
						if(preauthApprovedStatus || enhancementApprovedOrDownsize){
							if(isCloseClaimValid){
								isCloseClaimValid = false;
								view.showErrorPopUp("Closing of claim is not possible. Please Withdraw the preauth");
							}
						}
					}
				}else if(claim.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()
						&& !isRodPaymentRejected){
					
					Boolean preauthApprovedStatus = preauthService.isPreauthApprovedStatus(intimation, claim);
					Boolean enhancementApprovedOrDownsize = preauthService.isEnhancementApprovedOrDownsize(intimation, claim);
					if(preauthApprovedStatus || enhancementApprovedOrDownsize){
						if(isCloseClaimValid){
							isCloseClaimValid = false;
							view.showErrorPopUp("Closing of claim is not possible. Please Withdraw the preauth");
						}
					}
				}
				
				if(reimbursementByClaimKey != null && ! reimbursementByClaimKey.isEmpty()){
					for (Reimbursement reimbursementDetails : reimbursementByClaimKey) {
						if(ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursementDetails.getStatus().getKey())){
							if(reimbursementDetails.getDocAcknowLedgement() != null 
									&& ! reimbursementDetails.getDocAcknowLedgement().getStatus().getKey().equals(ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS)){
								if(isCloseClaimValid){
									isCloseClaimValid = false;
									view.showErrorPopUp("Closing of claim is not possible. Please Cancel the Acknowledgement");
								}
							}
						}
					}
				}
				
				
				
				for (Reimbursement reimbursement2 : reimbursementByClaimKey) {
					
					if(! ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement2.getStatus().getKey())){
						SelectValue select = new SelectValue();
						select.setId(reimbursement2.getKey());
						select.setValue(reimbursement2.getRodNumber() + "- ROD");
						referenceContainer.addBean(select);
					}
				}
				
				closeClaimDto.setReferenceNoContainer(referenceContainer);
				
				BeanItemContainer<SelectValue> fileTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				
				BeanItemContainer<SelectValue> beanContainer = masterService.getSelectValueContainer(ReferenceTable.ROD_UPLOAD_DOC_TABLE_FILE_TYPE);
				
				List<SelectValue> itemIds = beanContainer.getItemIds();
				for (SelectValue item : itemIds) {
					if(! item.getValue().contains("Bill") && ! item.getValue().contains("Bil")  && ! item.getValue().contains("Bils")
							&& ! item.getValue().contains("FIR_MLC_AR") && ! item.getValue().contains("bil") && ! item.getValue().contains("Receipts")){
						fileTypeContainer.addBean(item);
					}
				}
				
				closeClaimDto.setFileTypeContainer(fileTypeContainer);
				
				closeClaimDto.setIntimationNumber(claim.getIntimation().getIntimationId());
				closeClaimDto.setClaimNumber(claim.getClaimId());
				
				closeClaimDto.setUserName(tableDTO.getUsername());
				
				BeanItemContainer<SelectValue> reasonForClosing = masterService.getMasterValueByReference(ReferenceTable.REASON_FOR_CLOSING);
				
				closeClaimDto.setReasonForCloseClaimContainer(reasonForClosing);

			}
			
			closeClaimDto.setNewIntimationDto(intimationDto);

			if(isCloseClaimValid){
				view.setPACloseClaimPageView(PACloseClaimView.class, true, closeClaimDto);
			}
			
		}
		
		protected void showPACloseClaimInProcessView(
				@Observes @CDIEvent(PAMenuPresenter.CLOSE_CLAIM_IN_PROCESS) final ParameterDTO parameters){
			
			PASearchCloseClaimTableDTORODLevel tableDTO = (PASearchCloseClaimTableDTORODLevel) parameters.getPrimaryParameter();
			
			NewIntimationDto intimationDto = new NewIntimationDto();
			
			if(tableDTO.getIntimationkey() != null){
				Intimation intimation = intimationService.getIntimationByKey(tableDTO.getIntimationkey());
				intimationDto = intimationService
						.getIntimationDto(intimation);
	            Claim claim = claimService.getClaimsByIntimationNumber(intimation.getIntimationId());
				ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claim);
				tableDTO.setClaimDto(claimDTO);
				
				Reimbursement reimbursement = reimbursementService.getLatestActiveROD(claim.getKey());
				if(reimbursement==null){
					Preauth preauth= preauthService.getLatestPreauthByClaim(claim.getKey());
					
					String diagnosisForPreauthByKey = "";
					if(preauth != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(preauth.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}else{
					String diagnosisForPreauthByKey = "";
					if(reimbursement != null ) {
						diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursement.getKey());
						
					}
					tableDTO.setDiagnosis(diagnosisForPreauthByKey);
				}
				
			}
			tableDTO.setIntimationDto(intimationDto);
			
			view.setPACloseClaimInProcessView(PACloseClaimInProcessView.class, true, tableDTO);
			
		}
		
		protected void showPASearchOrUploadDocumentsWizard(
				@Observes @CDIEvent(PAMenuPresenter.PA_SEARCH_OR_UPLOAD_DOCUMENTS_WIZARD) final ParameterDTO parameters) {
			
			SearchUploadDocumentsTableDTO searchUploadDto = (SearchUploadDocumentsTableDTO)parameters.getPrimaryParameter();
			
			ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
			Long claimKey = searchUploadDto.getClaimKey();
			Claim claimByKey = claimService.getClaimByKey(claimKey);	
			ClaimDto claimDTO = null;
			NewIntimationDto newIntimationDto = new NewIntimationDto();
			if (claimByKey != null) {
				newIntimationDto = intimationService.getIntimationDto(claimByKey
						.getIntimation());

				 claimDTO = ClaimMapper.getInstance().getClaimDto(claimByKey);
				 if(claimDTO.getClaimType() != null && claimDTO.getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					 Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(claimDTO.getKey());
					 if(latestPreauthByClaim != null){
						 claimDTO.setCashlessAppAmt(latestPreauthByClaim.getTotalApprovalAmount());
					 }
					 String diagnosisForPreauthByKey = "";
						if(latestPreauthByClaim != null ) {
							diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(latestPreauthByClaim.getKey());
							
						}
						rodDTO.setDiagnosis(diagnosisForPreauthByKey);
				 }
				claimDTO.setNewIntimationDto(newIntimationDto);
				rodDTO.setClaimDTO(claimDTO);
			}
			
			/*if(null != claimByKey && null != claimByKey.getClaimType() && null != claimByKey.getClaimType().getKey()
					&& claimByKey.getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
			{
				rodDTO.setSearchUploadpreauthUploadTblList(getPreauthDetailsForUpload(claimByKey.getKey()));
			}*/
		/*	rodDTO.setSearchUploadpreauthUploadTblList(getPreauthDetailsForUpload(claimByKey.getKey()));
			rodDTO.setSearchUploadrodUploadTblList(getRodDetailsForUpload(claimByKey.getKey()));
			*/
			
			
			UploadDocumentDTO  uploadDto =  new UploadDocumentDTO();
			if(null != searchUploadDto.getAcknowledgementNo()){
			
			uploadDto.setAcknowledgementNo(searchUploadDto.getAcknowledgementNo());
			}
			uploadDto.setUsername(searchUploadDto.getUsername());
			
			uploadDto.setUploadDocsList(searchUploadDocService.getAckDocByDocAckKey(searchUploadDto.getAckDocKey()));
			
			//AcknowledgeDocument ackDoc = uploadSubmitService.getAcknowledgeDocument(searchUploadDto.getAckDocKey());
			//if(null != ackDoc)
			//{
			//	DocAcknowledgement docAcknowledgment = ackDoc.getDocAcknowledgement();
			//	if(null != docAcknowledgment)
				//{
				rodDTO.getDocumentDetails().setHospitalizationFlag(			
						searchUploadDto.getHospitalizationFlag());
				rodDTO.getDocumentDetails().setPreHospitalizationFlag(
						searchUploadDto.getPreHospitalizationFlag());
				rodDTO.getDocumentDetails().setPostHospitalizationFlag(
						searchUploadDto.getPostHospitalizationFlag());
				rodDTO.getDocumentDetails().setPartialHospitalizationFlag(
						searchUploadDto.getPartialHospitalizationFlag());
				rodDTO.getDocumentDetails().setLumpSumAmountFlag(
						searchUploadDto.getLumpSumAmountFlag());
				rodDTO.getDocumentDetails().setAddOnBenefitsHospitalCashFlag(
						searchUploadDto.getAddOnBenefitsHospitalCashFlag());
				rodDTO.getDocumentDetails().setAddOnBenefitsPatientCareFlag(
						searchUploadDto.getAddOnBenefitsPatientCareFlag());
				rodDTO.getDocumentDetails().setHospitalizationRepeatFlag(
						searchUploadDto.getHospitalizationRepeatFlag());
				
				rodDTO.setAcknowledgementNumber(searchUploadDto.getAckDocKey());
				
					
				//}
			//}
			
			
			rodDTO.setUploadDocumentsDTO(uploadDto);
			
			view.setPASearchOrUploadDocumentsWizard(PASearchUploadDocumentsWizardView.class, rodDTO);
		}
		
		/*protected void showSearchOrUploadDocumentsForAckNotReceived(
				@Observes @CDIEvent(MenuItemBean.PA_SEARCH_OR_UPLOAD_DOCUMENTS_FOR_ACK_NOT_RECEIVED) final ParameterDTO parameters) {
			
			BeanItemContainer<SelectValue> selectValueContainerForType =  masterService.getSelectValueContainer(ReferenceTable.COMMON_VALUES);	
			
			view.setViewPASearchOrUploaddocumentsForAckNotReceived(PAUploadDocumentsOutsideProcessView.class,true,selectValueContainerForType);
		}*/
		
		protected void showPASearchOrUploadDocumentsForAckNotReceivedWizard(
				@Observes @CDIEvent(PAMenuPresenter.SEARCH_OR_UPLOAD_DOCUMENTS_ACK_NOT_RECEIVED_WIZA) final ParameterDTO parameters) {
			
			
			UploadDocumentsForAckNotReceivedTableDTO searchUploadDto = (UploadDocumentsForAckNotReceivedTableDTO)parameters.getPrimaryParameter();
			
			ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
			Long claimKey = searchUploadDto.getClaimKey();
			Claim claimByKey = claimService.getClaimByKey(claimKey);	
			ClaimDto claimDTO = null;
			NewIntimationDto newIntimationDto = new NewIntimationDto();
			if (claimByKey != null) {
				newIntimationDto = intimationService.getIntimationDto(claimByKey
						.getIntimation());
				 claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
				 if(claimDTO.getClaimType() != null && claimDTO.getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
					 Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(claimDTO.getKey());
					 if(latestPreauthByClaim != null){
						 claimDTO.setCashlessAppAmt(latestPreauthByClaim.getTotalApprovalAmount());
					 }
					 String diagnosisForPreauthByKey = "";
						if(latestPreauthByClaim != null ) {
							diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(latestPreauthByClaim.getKey());
							
						}
						rodDTO.setDiagnosis(diagnosisForPreauthByKey);
				 }
				claimDTO.setNewIntimationDto(newIntimationDto);
				rodDTO.setClaimDTO(claimDTO);
			}
			
			UploadDocumentDTO  uploadDto =  new UploadDocumentDTO();
			if(null != searchUploadDto.getIntimationNo()){
				
				uploadDto.setIntimationNo(searchUploadDto.getIntimationNo());
			}
			
			
			if(null != searchUploadDto.getClaimNo()){
				
				uploadDto.setClaimNo(searchUploadDto.getClaimNo());
			}
			
			if(null != searchUploadDto.getClaimType())
			{
				uploadDto.setClaimTypeValue(searchUploadDto.getClaimType());
			}
			
			//uploadDto.setUploadDocsList(searchUploadDocServiceforAckNotReceived.getUploadedDocumentDetails(searchUploadDto.getIntimationNo(),SHAConstants.POST_PROCESS));
			
			uploadDto.setUsername(searchUploadDto.getUsername());
			
			
			rodDTO.setSearchUploadpreauthUploadTblList(getPreauthDetailsForUpload(claimByKey.getKey()));
			rodDTO.setSearchUploadrodUploadTblList(getRodDetailsForUpload(claimByKey.getKey()));
			
			rodDTO.setAcknowledgementNumber(searchUploadDto.getAckDocKey());
			rodDTO.setUploadDocumentsDTO(uploadDto);
			view.setPASearchOrUploadDocumentsForAckNotReceivedWizard(PAUploadDocumentsOutSideProcessWizardView.class, rodDTO);
		}
		
		private List<UploadDocumentsForAckNotReceivedPageTableDTO> getPreauthDetailsForUpload(Long claimKey)
		{
			return uploadSubmitServiceforAckNotReceived.getPreauthDetailsForUpload(claimKey);
		}

		private List<UploadDocumentsForAckNotReceivedPageTableDTO> getRodDetailsForUpload(Long claimKey)
		{
			return uploadSubmitServiceforAckNotReceived.getRODDetailsForUpload(claimKey);
		}
//		Commented the below Cashless Screen	
//		protected void showPAConvertReimbursement(
//				@Observes @CDIEvent(MenuItemBean.CONVERT_PA_CLAIM_OUTSIDE_PROCESS) final ParameterDTO parameters) {
//			BeanItemContainer<SelectValue> selectValueContainer = masterService
//					.getTmpCpuCodes();
//			view.setPASearchConvertReimbursement(PASearchConvertReimbursementView.class, true, selectValueContainer);
//		}
		
		protected void showPAConvertReimbursementPage(
				@Observes @CDIEvent(PAMenuPresenter.SHOW_CONVERT_REIMBURSEMENT) final ParameterDTO parameters) {
			SearchConvertClaimTableDto searchFormDto = (SearchConvertClaimTableDto) parameters
					.getPrimaryParameter();
			ConvertClaimDTO convertClaimDto = new ConvertClaimDTO();
			ConvertClaimDTO preauthDatas = new ConvertClaimDTO();
			ConvertClaimDTO status = new ConvertClaimDTO();
			Claim claimDetails = claimService.getClaimByKey(searchFormDto.getKey());

			//Boolean isValidClaimForCoveringLetter = true;
			
			/*if(null != claimDetails)
			{
				if((SHAConstants.YES_FLAG).equalsIgnoreCase(claimDetails.getLegalFlag()))
				{
					isValidClaimForCoveringLetter = false;
					view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
				}
			}*/
			
			//if(isValidClaimForCoveringLetter)
			//{
			NewIntimationDto intimationDto = intimationService
					.getIntimationDto(claimDetails.getIntimation());

			Intimation intimation = intimationService
					.getIntimationByKey(intimationDto.getKey());
			
			ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claimDetails);
			claimDTO.setNewIntimationDto(intimationDto);
			
			RRCDTO rrcDTO = new RRCDTO();
			rrcDTO.setNewIntimationDTO(intimationDto);
			rrcDTO.setClaimDto(claimDTO);
			rrcDTO.setStrUserName(searchFormDto.getUsername());
			
			Double insuredSumInsured = 0d;
			
			if(null != intimation && null != intimation.getPolicy() && null != intimation.getPolicy().getProduct() &&
					null != intimation.getPolicy().getProduct().getKey() && 
					!(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey())) )
			{
				insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					rrcDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), rrcDTO.getNewIntimationDTO().getPolicy().getKey()
							,rrcDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						rrcDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), rrcDTO.getNewIntimationDTO().getPolicy().getKey());
			}
			loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured, SHAConstants.PA_PROCESS_CONVERT_CLAIM);
			searchFormDto.setRrcDTO(rrcDTO);

			// intimationDto.setIntimatdBy(intimation.getIntimatedBy().getValue());
			//
			// MastersValue intimationMod=intimation.getIntimationMode();
			//
			// SelectValue intimationMode=new SelectValue();
			// intimationMode.setId(intimationMod.getKey());
			// intimationMode.setValue(intimationMod.getValue());
			//
			// intimationDto.setModeOfIntimation(intimationMode);
			//
			// intimationDto.setClaimNo(claimDetails.getClaimId());
			//
			// intimationDto.setPolicyNumber(intimation.getPolicyNumber());
			//

			//
			// MastersValue policyType=intimation.getPolicy().getPolicyType();
			// SelectValue policy=new SelectValue();
			// policy.setId(policyType.getKey());
			// policy.setValue(policyType.getValue());
			//
			// intimationDto.setPolicyType(policy);
			//
			Hospitals hospitalDetails = hospitalService.getHospitalById(intimation
					.getHospital());
			
			ConvertClaimMapper convertClaimMapper = ConvertClaimMapper.getInstance();
			//
			// intimationDto.setHospitalName(hospitalDetails.getName());
			// intimationDto.setHospitalName(intimation.getho);

			// Preauth preauthDetails = preauthService
			// .getPreauthListByClaimNo(claimDetails.getKey());
			if (claimDetails != null) {
				convertClaimDto = convertClaimMapper.getClaimDTO(claimDetails);
			}
			if (null != claimDetails.getStatus()) {
				convertClaimDto.setClaimStatus(claimDetails.getStatus()
						.getProcessValue());

				if (claimDetails.getStatus().getKey().equals(ReferenceTable.CLAIM_REGISTERED_STATUS)) {
					convertClaimDto.setDenialRemarks(claimDetails
							.getRegistrationRemarks());

				} else if (claimDetails.getStatus().getKey() == 19l
						|| claimDetails.getStatus().getKey() == 24l) {
					Preauth preauthDetails = preauthService
							.getPreauthClaimKey(claimDetails.getKey());

					PreauthQuery preauthQuery = preauthService
							.getPreauthQueryList(preauthDetails.getKey());

					convertClaimDto
							.setDenialRemarks(preauthQuery.getQueryRemarks());
				} else if(claimDetails.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS) || claimDetails.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
					
					Preauth preauthDetails = preauthService
							.getLatestPreauthDetails(claimDetails.getKey());
					if(preauthDetails.getMedicalRemarks() != null){
						convertClaimDto.setDenialRemarks(preauthDetails.getMedicalRemarks());
					}else{
						convertClaimDto.setDenialRemarks(preauthDetails.getRemarks());
					}
					
					
				}
			}

			List<Long> keysList = new ArrayList<Long>();

			if (claimDetails != null) {
				List<Preauth> preauthList = preauthService
						.getPreauthByClaimKey(claimDetails.getKey());
				for (Preauth preauth : preauthList) {
					keysList.add(preauth.getKey());
				}

			}
			if (!keysList.isEmpty()) {
				Long preauthKey = Collections.max(keysList);
				Preauth preauth = preauthService.getPreauthById(preauthKey);
				if (null != preauth) {
					if (null != preauth.getStatus()) {
						if (preauth.getStatus().getKey() == 26l) {
							convertClaimDto.setClaimStatus(preauth.getStatus()
									.getProcessValue());
							convertClaimDto.setDenialRemarks(preauth.getRemarks());
						}
					}
				}
			}
			
			
	         DocUploadToPremia premiaData = processPremedicalService.getUploadedDataDocumentDetails(claimDetails.getIntimation().getIntimationId()/*"CLI/2015/141125/0003907"*/);
			
			if(premiaData  != null && premiaData.getPfdUpFFAXAmt() != null){
				convertClaimDto.setClaimedAmount(Double.valueOf(premiaData.getPfdUpFFAXAmt()));
			}
			

			// if (preauthDetails != null) {
			// preauthDatas = convertClaimMapper.getPreauthDTO(preauthDetails);
			// convertClaimDto.setDenialRemarks(preauthDatas.getDenialRemarks());
			// convertClaimDto.setClaimStatus(preauthDatas.getClaimStatus());
			// }
			BeanItemContainer<SelectValue> selectValueContainer = masterService
					.getConversionReasonByValue(ReferenceTable.REASON_FOR_CONVERSION);

			view.setPAConvertReimbursementView(PAConvertReimbursementPageView.class, convertClaimDto,
					selectValueContainer, intimationDto, searchFormDto);
			//}
		}
		
		protected void showPACovertClaim(
				@Observes @CDIEvent(MenuItemBean.PA_CONVERT_CLAIM) final ParameterDTO parameters) {
			BeanItemContainer<SelectValue> selectValueContainer = masterService
					.getTmpCpuCodes();
			view.setPAViewG(PASearchConvertReimbursementView.class, true, selectValueContainer);
		}		
		
		
		protected void showGPAUnamedRiskDetails(
				@Observes @CDIEvent(MenuItemBean.GPA_UNNAMED_RISK_DETAILS) final ParameterDTO parameters) {
			
			view.setViewGPAUnamedDetailsView(SearchUnnamedRiskDetailsView.class, true);
		}
		
		protected void showGPAUnnamedRiskDetailsPage(
				@Observes @CDIEvent(MenuItemBean.GPA_UNNAMED_RISK_DETAILS_PAGE) final ParameterDTO parameters) {	
			SearchUnnamedRiskDetailsTableDTO  unnamedRiskDetailsTableDto= (SearchUnnamedRiskDetailsTableDTO) parameters
					.getPrimaryParameter();
			
			UnnamedRiskDetailsPageDTO unnamedRiskPageDto = new UnnamedRiskDetailsPageDTO();
			
			Claim claimObj = claimService.getClaimsByIntimationNumber(unnamedRiskDetailsTableDto.getIntimationNo());	
			Intimation intimation = preauthService.getIntimationByNo(unnamedRiskDetailsTableDto.getIntimationNo());
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			Double sumInsured = 0d;
			
			
			Double insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
					intimation.getInsured()
							.getInsuredId().toString(), intimation.getPolicy()
							.getKey());
			if(null != sumInsured && null != insuredSumInsured){
				
				unnamedRiskPageDto.setSumInsured(insuredSumInsured);
			}
			
			
			if(null != intimation){
				
				unnamedRiskPageDto.setIntimationNo(unnamedRiskDetailsTableDto.getIntimationNo());
				unnamedRiskPageDto.setPolicyNo(unnamedRiskDetailsTableDto.getPolicyNo());
				unnamedRiskPageDto.setOrganisationName(intimation.getPolicy().getProposerFirstName());						
				unnamedRiskPageDto.setGpaRiskName(intimation.getPaPatientName());				
				unnamedRiskPageDto.setGpaSection(intimation.getPolicy().getSectionCode());
				
				if(null != intimation.getPaParentName()){
					
					unnamedRiskPageDto.setGpaParentName(intimation.getPaParentName());	
				}
				
				if(null != intimation.getPaParentDOB()){
					
					unnamedRiskPageDto.setGpaParentDOB(intimation.getPaParentDOB());	
				}
				
			}
			/*else if(claimObj != null)
			{					
			
				ClaimDto claimDto =  ClaimMapper.getInstance().getClaimDto(claimObj);		
						
				unnamedRiskPageDto.setIntimationNo(unnamedRiskDetailsTableDto.getIntimationNo());
				unnamedRiskPageDto.setPolicyNo(unnamedRiskDetailsTableDto.getPolicyNo());
				unnamedRiskPageDto.setOrganisationName(claimObj.getIntimation().getPolicy().getProposerFirstName());						
				unnamedRiskPageDto.setGpaRiskName(claimDto.getGpaRiskName());				
				unnamedRiskPageDto.setGpaSection(claimDto.getGpaSection());
				
				if(null != claimDto.getGpaParentName()){
					
					unnamedRiskPageDto.setGpaParentName(claimDto.getGpaParentName());	
				}
				
				if(null != claimDto.getGpaParentDOB()){
					
					unnamedRiskPageDto.setGpaParentDOB(claimDto.getGpaParentDOB());	
				}
				
			
			}*/
			

			BeanItemContainer<SelectValue> selectValueForCategory = dbCalculationService.getGPACategory(intimation.getPolicy().getKey());
					
			
			view.setViewUnnamedRiskDetailsPage(UnnamedRiskDetailsPageView.class,true,unnamedRiskPageDto,selectValueForCategory);
					
	 	}
		
		protected void showPAPrintBulkReminderLetters(
				@Observes @CDIEvent(MenuItemBean.PRINT_PA_REMINDER_LETTER_BULK) final ParameterDTO parameters) {
		
			Map<String,Object> referenceValue = new HashMap<String, Object>();
			
			String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);	
			BeanItemContainer<SelectValue> cpuCodeContainer = usertoCPUMapService.getRevisedUserCpuContainer(userId);
			BeanItemContainer<SelectValue> claimTypeContainer = masterService
					.getClaimtypeContainer();
			
			referenceValue.put("cpuCodeContainer",cpuCodeContainer);
			referenceValue.put("claimTypeContainer", claimTypeContainer);	
			
			BeanItemContainer<SelectValue> categoryContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

			SelectValue billsNotRecvdSelectValue = new SelectValue(null,SHAConstants.PA_BILLS_NOT_RECEIVED_OTHERS);
			SelectValue billsNotRecvdDeathSelectValue = new SelectValue(null,SHAConstants.PA_BILLS_NOT_RECEIVED_DEATH);
			SelectValue preAuthbillsNotRecvdSelectValue = new SelectValue(null,SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
			SelectValue querySelectValue = new SelectValue(null,SHAConstants.PA_QUERY);
			SelectValue paymentQuerySelectValue = new SelectValue(null,SHAConstants.PA_PAYMENT_QUERY);
			
			categoryContainer.addBean(billsNotRecvdSelectValue);
			categoryContainer.addBean(billsNotRecvdDeathSelectValue);
			categoryContainer.addBean(preAuthbillsNotRecvdSelectValue);
			categoryContainer.addBean(querySelectValue);
			categoryContainer.addBean(paymentQuerySelectValue);
			
			referenceValue.put("categoryContainer",categoryContainer);
			
			BeanItemContainer<SelectValue> reminderTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

			SelectValue firstReminder = new SelectValue(null,SHAConstants.FIRST_REMINDER);
			SelectValue secondReminder = new SelectValue(null,SHAConstants.SECOND_REMINDER);
			SelectValue closeReminder = new SelectValue(null,SHAConstants.CLOSE);
			reminderTypeContainer.addBean(firstReminder);
			reminderTypeContainer.addBean(secondReminder);
			reminderTypeContainer.addBean(closeReminder);
			
			referenceValue.put("reminderTypeContainer",reminderTypeContainer);		
			
			view.setViewPAPrintBulkReminderLetter(SearchPAPrintRemainderBulkViewImpl.class,
					true, referenceValue);
		}	
		
		private List<ReconsiderRODRequestTableDTO> getReconsiderRODRequestForUpdateROD(
				Claim claim) {
			List<ReconsiderRODRequestTableDTO> reconsiderRODList = ackDocReceivedService
					.getReconsiderRequestTableValuesForUpdateRod(claim);
			return reconsiderRODList;
		}
		
		@SuppressWarnings("unused")
		private List<ReconsiderRODRequestTableDTO> getReconsiderRODRequestForRodAndBillEntryForUpdateROD(
				Claim claim) {
			List<ReconsiderRODRequestTableDTO> reconsiderRODList = ackDocReceivedService
					.getReconsiderRequestTableValuesForCreateRodAndBillEntryforUpdateRod(claim);
			return reconsiderRODList;
		}
		
		//R1069
		protected void showAddAdditionalDocumentPaymentInfo(
				@Observes @CDIEvent(MenuItemBean.PA_ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION) final ParameterDTO parameters) {
			view.setViewG(SearchAddAdditionalDocumentPaymentInfoView.class, true);
		}

		//R1069
		protected void showAddAditionalPaymentInfosWizard(
				@Observes @CDIEvent(PAMenuPresenter.PA_ADD_ADDITIONAL_PAYMENT_INFORMATION) final ParameterDTO parameters) {

			SearchAddAdditionalDocumentPaymentInfoTableDTO tableDTO = (SearchAddAdditionalDocumentPaymentInfoTableDTO) parameters
					.getPrimaryParameter();
			ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
			
			Long claimKey = tableDTO.getClaimKey();
			Claim claim = claimService.getClaimByKey(claimKey);
			
			Boolean isValidClaim = true;
			if (null != claim) {
				if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claim
						.getLegalFlag())) {
					isValidClaim = false;
					view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
				}
			}
			
			if(isValidClaim) {

			List<Reimbursement> reimbursementList = reimbursementService
					.getRembursementDetails(claimKey);
			
			//List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
			Map<Long, Long> rejectMap = ReferenceTable.REJECT_ROD_KEYS;
			/*if(null != reimbursementListObj && !reimbursementListObj.isEmpty())
			{
				for (Reimbursement reimbursement : reimbursementListObj) {
					if(null != rejectMap && !rejectMap.isEmpty() && null == rejectMap.get(reimbursement.getStatus().getKey()))
					{
						reimbursementList.add(reimbursement);
					}
				}
			}
			
	*/
			/*Boolean isValidClaimForAck = true;
			if((ackDocReceivedService.getDBTaskForPreauth(claim.getIntimation(), SHAConstants.BILLING_CURRENT_QUEUE)))
			{
				isValidClaimForAck = false;
				view.showErrorPopUp("This claims has crossed medical stage and you are not allowed to upload the document");
			}
			if((ackDocReceivedService.getDBTaskForPreauth(claim.getIntimation(), SHAConstants.FA_CURRENT_QUEUE)))
			{
				isValidClaimForAck = false;
				view.showErrorPopUp("This claims has crossed medical stage and you are not allowed to upload the document");
			}*/
			
			if (null != claim) {

				if (null != claim.getIncidenceFlag()
						&& claim.getIncidenceFlag().equalsIgnoreCase(
								SHAConstants.ACCIDENT_FLAG)) {
					rodDTO.getDocumentDetails().setAccidentOrDeath(true);
				} else {
					rodDTO.getDocumentDetails().setAccidentOrDeath(false);
				}
			}
			
			if (tableDTO.getKey() != null) {
				Reimbursement reimbursement = reimbursementService
						.getReimbursementbyRod(tableDTO.getKey());
				
				/*Boolean isValidClaimForAck = true;
				if((ackDocReceivedService.getDBTaskForPreauth(claim.getIntimation(), SHAConstants.BILLING_CURRENT_QUEUE)))
				{
					
					isValidClaimForAck = false;
					view.showErrorPopUp("This claims has crossed medical stage and you are not allowed to upload the document");
				}
				if((ackDocReceivedService.getDBTaskForPreauth(claim.getIntimation(), SHAConstants.FA_CURRENT_QUEUE)))
				{
					isValidClaimForAck = false;
					view.showErrorPopUp("This claims has crossed medical stage and you are not allowed to upload the document");
				}*/
			
				if(null != reimbursement)
				{
					rodDTO.getDocumentDetails().setDocAcknowledgementKey(
							reimbursement.getDocAcknowLedgement().getKey());
					rodDTO.getDocumentDetails().setDateOfDischarge(reimbursement.getDateOfDischarge());
					rodDTO.getDocumentDetails().setRodKey(reimbursement.getKey());
				}
				
				
				String diagnosisForPreauthByKey = "";
				if(reimbursement != null ) {
					diagnosisForPreauthByKey = preauthService.getDiagnosisForPreauthByKey(reimbursement.getKey());
					
				}
				rodDTO.setDiagnosis(diagnosisForPreauthByKey);
				
			}

			DocAcknowledgement docAcknowlegement = acknowledgementDocumentsReceivedService
					.getDocAcknowledgment(rodDTO.getDocumentDetails()
							.getDocAcknowledgementKey());

			rodDTO.getDocumentDetails().setAcknowledgementNumber(
					docAcknowlegement.getAcknowledgeNumber());
			rodDTO.getDocumentDetails().setRodKey(tableDTO.getKey());
			
			if(null != docAcknowlegement.getModeOfReceiptId())
			{
				rodDTO.getDocumentDetails().setModeOfReceiptValue(docAcknowlegement.getModeOfReceiptId().getValue());
			}
			
			rodDTO.getDocumentDetails().setAdditionalRemarks(docAcknowlegement.getAdditionalRemarks());

			rodDTO.getDocumentDetails().setAcknowledgmentContactNumber(docAcknowlegement.getInsuredContactNumber());
			rodDTO.getDocumentDetails().setEmailId(docAcknowlegement.getInsuredEmailId());
			rodDTO.getDocumentDetails().setDocumentsReceivedDate(docAcknowlegement.getDocumentReceivedDate());
			/*if(null != docAcknowlegement.getSourceOfDocument())
				{
					if(SHAConstants.SOURCE_DOC_INSURED.equalsIgnoreCase(docAcknowlegement.getSourceOfDocument())){
				rodDTO.getDocumentDetails().setSourceOfDocument(Boolean.TRUE);
			}
			else if(SHAConstants.SOURCE_DOC_INTERNAL.equalsIgnoreCase(docAcknowlegement.getSourceOfDocument())){
				rodDTO.getDocumentDetails().setSourceOfDocument(Boolean.FALSE);
			}
				}*/
			rodDTO.getDocumentDetails().setHospitalizationFlag(
					docAcknowlegement.getHospitalisationFlag());
			rodDTO.getDocumentDetails().setPreHospitalizationFlag(
					docAcknowlegement.getPreHospitalisationFlag());
			rodDTO.getDocumentDetails().setPostHospitalizationFlag(
					docAcknowlegement.getPostHospitalisationFlag());
			rodDTO.getDocumentDetails().setPartialHospitalizationFlag(
					docAcknowlegement.getPartialHospitalisationFlag());
			
			if(docAcknowlegement.getHospitalizationRepeatFlag() != null){
				rodDTO.getDocumentDetails().setHospitalizationRepeatFlag(docAcknowlegement.getHospitalizationRepeatFlag());	
			}
			
			rodDTO.getDocumentDetails().setLumpSumAmountFlag(
					docAcknowlegement.getLumpsumAmountFlag());
			if (docAcknowlegement.getHospitalizationClaimedAmount() != null) {
				rodDTO.getDocumentDetails().setHospitalizationClaimedAmount(
						docAcknowlegement.getHospitalizationClaimedAmount()
								.toString());
			}

			SelectRODtoAddAdditionalDocumentsMapper selectRODtoAddAdditionalDocumentsMapper = new SelectRODtoAddAdditionalDocumentsMapper();
			List<SelectRODtoAddAdditionalDocumentsDTO> selectRODtoAddAdditionalDocumentsDTOList = selectRODtoAddAdditionalDocumentsMapper
					.getReimbursementDto(reimbursementList);
			
			for (SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO2 : selectRODtoAddAdditionalDocumentsDTOList) {
				if(selectRODtoAddAdditionalDocumentsDTO2 != null && selectRODtoAddAdditionalDocumentsDTO2.getStatusKey() != null){
				if(selectRODtoAddAdditionalDocumentsDTO2.getStatusKey().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY) || selectRODtoAddAdditionalDocumentsDTO2.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
				|| selectRODtoAddAdditionalDocumentsDTO2.getStatusKey().equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)){
					
					selectRODtoAddAdditionalDocumentsDTO2.setApprovedAmt(null);
					
					}
				}
				
			}

			Long count = getAckNoCountByClaimKey(claimKey,
					ReferenceTable.ACKNOWLEDGE_DOC_RECEIVED);
			rodDTO.setAcknowledgementNumber(count);
			ClaimDto claimDTO  = null;
			NewIntimationDto newIntimationDto = new NewIntimationDto();
			if (claim != null) {
				newIntimationDto = intimationService.getIntimationDto(claim
						.getIntimation());
				claimDTO =  ClaimMapper.getInstance().getClaimDto(claim);
				claimDTO.setNewIntimationDto(newIntimationDto);
				rodDTO.setClaimDTO(claimDTO);

			}
			generateAcknowledgeNo(rodDTO);

			/*
			 * rodDTO.getDocumentDetails().setDocumentCheckList(
			 * getDocumentCheckList(claimByKey));
			 */

			rodDTO.setProductBenefitMap(dbCalculationService.getProductBenefitFlag(
					claim.getKey(), claim.getIntimation().getPolicy().getProduct()
							.getKey()));
			rodDTO.getDocumentDetails().setDocumentCheckList(
					setDocumentCheckListTableValues());

			rodDTO.setReconsiderRodRequestList(getReconsiderRODRequest(claim));
			rodDTO.setRodQueryDetailsList(getRODQueryDetailsValues(claim));

//			List<Reimbursement> reimbursementByStageId = reimbursementService
//					.getReimbursementByClaimKeyAndStageId(claimKey);
			/*List<Reimbursement> reimbursementByStageId = reimbursementService
				.getReimbursementByClaimKey(claimKey);*/
			List<Reimbursement> reimbursementByStageId = new ArrayList<Reimbursement>();
			/*List<Reimbursement> reimbursementListObj = reimbursementService
			.getReimbursementByClaimKey(claimKey);*/
			
			if(null != reimbursementList && !reimbursementList.isEmpty())
			{
				for (Reimbursement reimbursement : reimbursementList) {
					/*if(null != rejectMap && !rejectMap.isEmpty() && null == rejectMap.get(reimbursement.getStatus().getKey()))
					{
						reimbursementByStageId.add(reimbursement);
					}*/
					if(isMAapproved(reimbursement) && reimbursementService.isBatchNotCreated(reimbursement.getKey())){
						reimbursementByStageId.add(reimbursement);
					}
				}
			}

			selectRODtoAddAdditionalDocumentsDTOList = selectRODtoAddAdditionalDocumentsMapper
					.getReimbursementDto(reimbursementByStageId);

			for (int index = 0; index < selectRODtoAddAdditionalDocumentsDTOList
					.size(); index++) {
				if (reimbursementByStageId.get(index).getStatus().getKey()
						.equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
					selectRODtoAddAdditionalDocumentsDTOList
							.get(index)
							.setApprovedAmt(
									reimbursementByStageId.get(index)
											.getBillingApprovedAmount() != null ? reimbursementByStageId
											.get(index).getBillingApprovedAmount()
											.toString()
											: "");
				} else if (reimbursementByStageId.get(index).getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					selectRODtoAddAdditionalDocumentsDTOList
							.get(index)
							.setApprovedAmt(
									reimbursementByStageId.get(index)
											.getFinancialApprovedAmount() != null ? reimbursementByStageId
											.get(index)
											.getFinancialApprovedAmount()
											.toString()
											: "");
				} else if (reimbursementByStageId.get(index).getStatus().getKey()
						.equals(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS)) {
					selectRODtoAddAdditionalDocumentsDTOList
							.get(index)
							.setApprovedAmt(
									reimbursementByStageId.get(index)
											.getClaimApprovalAmount() != null ? reimbursementByStageId
											.get(index)
											.getClaimApprovalAmount()
											.toString()
											: "");
				}
				else {
					selectRODtoAddAdditionalDocumentsDTOList
							.get(index)
							.setApprovedAmt(
									reimbursementByStageId.get(index)
											.getCurrentProvisionAmt() != null ? reimbursementByStageId
											.get(index).getCurrentProvisionAmt()
											.toString()
											: "");
				}
			}

			for (int index = 0; index < reimbursementByStageId.size(); index++) {
				StringBuffer billClassificaiton = new StringBuffer();
				Double claimedAmt = 0d;
				if (reimbursementByStageId.get(index) != null
						&& reimbursementByStageId.get(index)
								.getDocAcknowLedgement() != null) {
					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getHospitalisationFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement().getHospitalisationFlag()
								.equalsIgnoreCase("y")) {
							billClassificaiton.append("Hospitalisation, ");
						}

					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getPreHospitalisationFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement()
								.getPreHospitalisationFlag().toString()
								.equalsIgnoreCase("y")) {
							billClassificaiton .append("Pre - Hospitalisation, ");
						}
					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getPostHospitalisationFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement()
								.getPostHospitalisationFlag().toString()
								.equalsIgnoreCase("y")) {
							billClassificaiton.append("Post - Hospitalisation,  ");
						}

					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getPartialHospitalisationFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement()
								.getPartialHospitalisationFlag().toString()
								.equalsIgnoreCase("y")) {
							billClassificaiton.append("Partial - Hospitalisation,  ");
						}

					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getLumpsumAmountFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement().getLumpsumAmountFlag()
								.toString().equalsIgnoreCase("y")) {
							billClassificaiton.append("Lumpsum - Amount,  ");
						}
					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getHospitalCashFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement().getHospitalCashFlag()
								.toString().equalsIgnoreCase("y")) {
							billClassificaiton.append("Hospital - Cash,  ");
						}
					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getHospitalizationRepeatFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement().getHospitalizationRepeatFlag()
								.toString().equalsIgnoreCase("y")) {
							billClassificaiton.append("Hospitalisation - Repeat,  ");
						}
					}

					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
							.getPatientCareFlag() != null) {
						if (reimbursementByStageId.get(index)
								.getDocAcknowLedgement()
								.getPatientCareFlag().toString()
								.equalsIgnoreCase("y")) {
							billClassificaiton.append("Patient - Care,  ");
						}
					}
//					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
//							.getHospitalizationClaimedAmount() != null) {
//						claimedAmt = claimedAmt
//								+ reimbursementByStageId.get(index)
//										.getDocAcknowLedgement()
//										.getHospitalizationClaimedAmount();
//					}
	//
//					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
//							.getPreHospitalizationClaimedAmount() != null) {
//						claimedAmt = claimedAmt
//								+ reimbursementByStageId.get(index)
//										.getDocAcknowLedgement()
//										.getPreHospitalizationClaimedAmount();
//					}
	//
//					if (reimbursementByStageId.get(index).getDocAcknowLedgement()
//							.getPostHospitalizationClaimedAmount() != null) {
//						claimedAmt = claimedAmt
//								+ reimbursementByStageId.get(index)
//										.getDocAcknowLedgement()
//										.getPostHospitalizationClaimedAmount();
//					}
				}
				if (billClassificaiton.length() > 0) {
					/*billClassificaiton = billClassificaiton.toString().substring(0,
							billClassificaiton.length() - 2);*/
					String billClassificationStr = billClassificaiton.toString().substring(0, billClassificaiton.length() - 2);
					selectRODtoAddAdditionalDocumentsDTOList.get(index)
							.setBillClassification(billClassificationStr);

				}
				
				claimedAmt = createRodService.getPAClaimedAmnt(selectRODtoAddAdditionalDocumentsDTOList.get(index).getKey());
				if(claimedAmt != null){
				selectRODtoAddAdditionalDocumentsDTOList.get(index).setClaimedAmt(
						claimedAmt.toString());
				}
				
				
			}
			
			
			
			Integer index = 1;
			String benefitAndCover = "";
			String benefitcover = "";
			List<SelectRODtoAddAdditionalDocumentsDTO> finalSelectRODtoAddAdditionalDocumentsDTOList = new ArrayList<SelectRODtoAddAdditionalDocumentsDTO>();
			for (SelectRODtoAddAdditionalDocumentsDTO selectRODtoAddAdditionalDocumentsDTO1 : selectRODtoAddAdditionalDocumentsDTOList) {
				selectRODtoAddAdditionalDocumentsDTO1.setsNo(index.toString());
				index++;
				
				benefitcover = acknowledgementDocumentsReceivedService.getCoverValueForViewBasedOnrodKey(selectRODtoAddAdditionalDocumentsDTO1.getKey());
				Reimbursement reimb = acknowledgementDocumentsReceivedService.getReimbursement(selectRODtoAddAdditionalDocumentsDTO1.getKey());
				if(null != reimb && null != reimb.getBenefitsId() && null != reimb.getBenefitsId().getValue())
				{
					benefitAndCover = reimb.getBenefitsId().getValue() + ", " + benefitcover;
				}
				selectRODtoAddAdditionalDocumentsDTO1.setCoverCode(benefitAndCover);
				
				finalSelectRODtoAddAdditionalDocumentsDTOList.add(selectRODtoAddAdditionalDocumentsDTO1);
			}

						/*List<UploadDocumentDTO> rodSummaryDetails = rodService
					.getRODSummaryDetails(tableDTO.getKey());
			
			if(null != rodSummaryDetails && !rodSummaryDetails.isEmpty())
			{
				List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
			//	List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();
				for (UploadDocumentDTO uploadDocDTO : rodSummaryDetails) {
					//sss
					uploadDocDTO.setIsBillSaved(true);
					List<BillEntryDetailsDTO> dtoList = new ArrayList<BillEntryDetailsDTO>();
					List<RODBillDetails> billEntryDetails = rodService
							.getBillEntryDetails(uploadDocDTO.getDocSummaryKey());
					if (billEntryDetails != null && !billEntryDetails.isEmpty()) {
						for (RODBillDetails billEntryDetailsDO : billEntryDetails) {
							
							 * <<<<<<< HEAD
							 * dtoList.add(getBillDetailsDTO(billEntryDetailsDO,
							 * uploadDocumentDTO)); =======
							 
							dtoList.add(getBillDetailsDTOForBillEntry(billEntryDetailsDO));
							
							// >>>>>>> 7656df822ab29676fae4ca1d8cd4ec48ac917fe0
						}
						uploadDocDTO.setBillEntryDetailList(dtoList);
					}
				}
			//	rodDTO.getUploadDocumentsDTO().setBillEntryDetailList(dtoList);
				rodDTO.setUploadDocsList(rodSummaryDetails);
				
			}*/

			rodDTO.setSelectRODtoAddAdditionalDocumentsDTO(finalSelectRODtoAddAdditionalDocumentsDTOList);

			rodDTO.setStrUserName(tableDTO.getUsername());
			
			RRCDTO rrcDTO = new RRCDTO();
			rrcDTO.setClaimDto(claimDTO);
			rrcDTO.setNewIntimationDTO(newIntimationDto);
			rrcDTO.setStrUserName(tableDTO.getUsername());
			
			Double insuredSumInsured = 0d;
			
			if(null != claim &&  null != claim.getIntimation() && 
					null != claim.getIntimation().getPolicy() && null != claim.getIntimation().getPolicy().getProduct() &&
					null != claim.getIntimation().getPolicy().getProduct().getKey() 
					&& !(ReferenceTable.getGPAProducts().containsKey(claim.getIntimation().getPolicy().getProduct().getKey())))
			{

				insuredSumInsured = dbCalculationService.getInsuredSumInsured(
					rrcDTO.getNewIntimationDTO().getInsuredPatient()
							.getInsuredId().toString(), rrcDTO.getNewIntimationDTO().getPolicy().getKey()
							,rrcDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(
						rrcDTO.getNewIntimationDTO().getInsuredPatient()
								.getInsuredId().toString(), rrcDTO.getNewIntimationDTO().getPolicy().getKey());
			}

			
			loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured, SHAConstants.ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFO);
			
			//loadRRCRequestValuesForProcessing(rrcDTO,insuredSumInsured,SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
			
			rodDTO.setRrcDTO(rrcDTO);

			view.setPAAddAditionalpaymentWizard(AddAditionalDocumentsPaymentInfoView.class,
    				rodDTO);
		}
		}
		
		private Boolean isMAapproved(Reimbursement reimbursement){
			if(reimbursement.getMedicalCompletedDate() != null){
				return Boolean.TRUE;
			}else{
				if(reimbursement.getDocAcknowLedgement() != null && reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL) && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			}
			
		}

		protected void showPABypassPreauthWizard(
				@Observes @CDIEvent(PAMenuPresenter.SHOW_FLP_BYPASS_PREAUTH_WIZARD) final ParameterDTO parameters) {

			PASearchPreauthTableDTO tableDTO = (PASearchPreauthTableDTO) parameters
					.getPrimaryParameter();
			
			Date startDate = new Date();
			log.info("%%%%%%%%%%%%%%%%%%%%%% STARTING TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+startDate);
			
			NewIntimationDto newIntimationDto = new NewIntimationDto();
			PreauthDTO preauthDTO = new PreauthDTO();
			//preauthDTO.setRodHumanTask(tableDTO.getHumanTask());
			preauthDTO.setTaskNumber(tableDTO.getTaskNumber());	
			
			preauthDTO.setStrUserName(tableDTO.getUsername());
			preauthDTO.setStrPassword(tableDTO.getPassword());
			if(null != tableDTO.getDocReceivedTimeForMatchDate())
				preauthDTO.setSfxMatchedQDate(tableDTO.getDocReceivedTimeForMatchDate());
			/*if(null !=tableDTO.getDocReceivedTimeForReg())
				preauthDTO.setSfxRegisteredQDate(SHAUtils.formatDateForStarfax(tableDTO.getDocReceivedTimeForReg()));*/
			if(null != tableDTO.getDocReceivedTimeForRegDate())
				preauthDTO.setSfxRegisteredQDate(tableDTO.getDocReceivedTimeForRegDate());
			/*if(null != tableDTO.getDocReceivedTimeForMatch() )
			preauthDTO.setSfxMatchedQDate(SHAUtils.formatDateForStarfax(tableDTO.getDocReceivedTimeForMatch()));*/
             //added for FLP Bypass case from autoallocation menu
//			preauthDTO.setIsPreauthAutoAllocationQ(tableDTO.getIsPreauthAutoAllocationQ());
			Claim claimByKey = claimService.getClaimByKey(tableDTO.getClaimKey());
			Boolean isValidClaim = true;
			if(null != claimByKey)
			{
				if((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey.getLegalFlag()))
				{
					isValidClaim = false;
					view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
				}
			}
			
			
			if(isValidClaim)
			{
			
				List<Preauth> preauthByClaimKey = preauthService
						.getPreauthByClaimKey(claimByKey.getKey());
				Boolean isQueryOrTranslate = false;
				if (!preauthByClaimKey.isEmpty()) {
					for (Preauth preauth : preauthByClaimKey) {
		
						//String[] split = preauth.getPreauthId().split("/");
						//Integer seqNumber = Integer.valueOf(split[split.length - 1]);
							if (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_COORDINATOR_REPLY_RECEIVED_STATUS) ||
									preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_PREAUTH_COORDINATOR_REPLY) ||
									preauth.getStatus().getKey().equals(ReferenceTable.PREMEDICAL_ENHANCEMENT_COORDINATOR_REPLY) ||
									preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_COORDINATOR_REPLY_RECEIVED_STATUS) ||
									preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS)
									|| preauth
											.getStatus()
											.getKey()
											.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_RECEIVED_STATUS) || preauth
											.getStatus()
											.getKey()
											.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS) || preauth
											.getStatus()
											.getKey()
											.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS) || preauth
											.getStatus()
											.getKey()
											.equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS)) {
								Date mapperStartDate = new Date();
								log.info("%%%%%%%%%%%%%%%%%%%%%% STARTING TIME FOR PREMEDICAL MAPPER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+mapperStartDate);
								
								PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
//								PreMedicalMapper.getAllMapValues();
								preauthDTO = premedicalMapper.getPreauthDTO(preauth);
								
								Date mapperEndDate = new Date();
								log.info("%%%%%%%%%%%%%%%%%%%%%% ENDING TIME FOR PREMEDICAL MAPPER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+mapperEndDate);
								
								log.info("%%%%%%%%%%%%%%%%%%%%%% TOTAL TIME FOR PREMEDICAL MAPPER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+SHAUtils.getDurationFromTwoDate(mapperStartDate, mapperEndDate));

								
								//preauthDTO.setRodHumanTask(tableDTO.getHumanTask());
								setpreauthTOPreauthDTO(premedicalMapper, claimByKey,
										preauth, preauthDTO, true);
								CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
								coordinatorDTO.setRefertoCoordinator(false);
								preauthDTO.setCoordinatorDetails(coordinatorDTO);
								preauthDTO.getPreauthDataExtractionDetails().setCorporateBufferFlag(preauthDTO.getClaimDTO().getIsgmcCorpBuffer() != null ? preauthDTO.getClaimDTO().getIsgmcCorpBuffer() : 0L);
								isQueryOrTranslate = true;
								break;
							}
					}
				}
		
				preauthDTO.setClsProsAllowed(masterService.getClsProsAllowedByPolicyNo(tableDTO.getPolicyNo()));
				
				if (!isQueryOrTranslate) {
					List<Preauth> preauthByClaimKeyWithClearCashless = preauthService
							.getPreauthByClaimKeyWithClearCashless(claimByKey.getKey());
					String referenceNo = claimByKey.getClaimId() + "/001";
					if(!preauthByClaimKeyWithClearCashless.isEmpty()) {
						referenceNo = claimByKey.getClaimId() + "/00" + String.valueOf(preauthByClaimKeyWithClearCashless.size() + 1);
					}
					
					preauthDTO.getPreauthDataExtractionDetails().setReferenceNo(
							referenceNo);
					if (claimByKey != null) {
						
						newIntimationDto = intimationService
								.getIntimationDto(claimByKey.getIntimation());
											
						setClaimValuesToDTO(preauthDTO, claimByKey);
						// newIntimationDto.getPolicy().getProduct().getAutoRestoration()
						
						Date mapperStartDate = new Date();
						log.info("%%%%%%%%%%%%%%%%%%%%%% STARTING TIME FOR CLAIM MAPPER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+mapperStartDate);
						
						ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
						preauthDTO.setNewIntimationDTO(newIntimationDto);
						preauthDTO.setClaimDTO(claimDTO);
						
						Date mapperEndDate = new Date();
						log.info("%%%%%%%%%%%%%%%%%%%%%% ENDING TIME FOR CLAIM MAPPER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+mapperEndDate);
						
						log.info("%%%%%%%%%%%%%%%%%%%%%% TOTAL TIME FOR CLAIM MAPPER %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+SHAUtils.getDurationFromTwoDate(mapperStartDate, mapperEndDate));

					}

					DBCalculationService dbCalculationService= new DBCalculationService();
					Double insuredSumInsured = dbCalculationService
							.getInsuredSumInsured(preauthDTO.getNewIntimationDTO()
									.getInsuredPatient().getInsuredId().toString(),
									preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO()
									.getInsuredPatient().getLopFlag());
					preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
					Map<String, Object> autoRestroation = dbCalculationService.getAutoRestroation(preauthDTO.getNewIntimationDTO().getIntimationId());
					if(autoRestroation.containsKey(SHAConstants.AUTO_RESTORATION_CHK) && autoRestroation.get(SHAConstants.AUTO_RESTORATION_CHK) != null){
						preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(String.valueOf(autoRestroation.get(SHAConstants.AUTO_RESTORATION_CHK)));
						if(preauthDTO.getPreauthDataExtractionDetails().getAutoRestoration().equalsIgnoreCase(SHAConstants.AUTO_RESTORATION_DONE)) {
							preauthDTO.setIsAutoRestorationDone(true);
						}
					}
					if(autoRestroation.containsKey(SHAConstants.AUTO_RESTORATION_COUNT) && autoRestroation.get(SHAConstants.AUTO_RESTORATION_COUNT) != null){
						preauthDTO.getPreauthDataExtractionDetails().setRestorationCount((Integer.valueOf(autoRestroation.get(SHAConstants.AUTO_RESTORATION_COUNT).toString())));
					}
					
				}
		
				preauthDTO.setIsRepremedical(isQueryOrTranslate);
				
				Double insuredSumInsured = dbCalculationService
						.getInsuredSumInsured(preauthDTO.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								preauthDTO.getPolicyDto().getKey(),preauthDTO.getNewIntimationDTO()
								.getInsuredPatient().getLopFlag());
				
				preauthDTO.setStrUserName(tableDTO.getUsername());
				preauthDTO.setStrPassword(tableDTO.getPassword());
				
			//	loadRRCRequestValues(preauthDTO,insuredSumInsured,SHAConstants.PROCESS_PRE_MEDICAL);
				
			Date premiaStartDate = new Date();
			log.info("%%%%%%%%%%%%%%%%%%%%%% STARTING TIME FOR 64VB PREMIA CALL %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+premiaStartDate);
				
				String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
				if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
					String get64vbStatus = PremiaService.getInstance().get64VBStatus(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber(), preauthDTO.getNewIntimationDTO().getIntimationId());
					if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
						preauthDTO.setIsDishonoured(true);
					} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
						preauthDTO.setIsPending(true);
					}
				}
				
				Date premiaEndDate = new Date();
				log.info("%%%%%%%%%%%%%%%%%%%%%% ENDING TIME FOR 64VB PREMIA CALL %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+premiaEndDate);
				
				log.info("%%%%%%%%%%%%%%%%%%%%%% TOTAL TIME FOR 64VB PREMIA CALL %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+SHAUtils.getDurationFromTwoDate(premiaStartDate, premiaEndDate));
				
				
			  Date	dbCalStartDate = new Date();
				log.info("%%%%%%%%%%%%%%%%%%%%%%STARTING TIME FOR  DB CALCULATION SERVICE  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+dbCalStartDate);
				
				preauthDTO.setSittingsAmount(dbCalculationService.getDialysisAmount(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey(), insuredSumInsured.longValue()).intValue());
//				preauthDTO.setAdmissionDatePopup(dbCalculationService.getPolicyAgeingForPopup(preauthDTO.getNewIntimationDTO().getAdmissionDate(), preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber()));
				
				String policyYear = preauthDTO.getNewIntimationDTO().getPolicyYear();
			    if(policyYear != null){
			    	String intValue = policyYear.replaceAll("[^0-9]", "");
			    	Integer policyAgeing = SHAUtils.getIntegerFromStringWithComma(intValue);
			    	preauthDTO.setAdmissionDatePopup(policyAgeing != null ? policyAgeing.equals(0) ? true : false :false);
			    }
				
				preauthDTO.setIsPEDInitiated(pedQueryService.isPEDInitiated(preauthDTO.getNewIntimationDTO().getPolicy().getKey()));
							
				//CR  R1086
				if(preauthDTO.getIsPEDInitiated()){
					boolean isInsuredDeleted = pedQueryService.getStatusOfInsuredForNonDisclosePed(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
					preauthDTO.setInsuredDeleted(isInsuredDeleted);
					
					//CR R1156
					boolean multiplePEDAvailable = pedQueryService.getMultiplePEDAvailableNotDeleted(preauthDTO.getNewIntimationDTO().getPolicy().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
					preauthDTO.setMultiplePEDAvailableNotDeleted(multiplePEDAvailable);
				}
				
				preauthDTO.setIsPEDInitiatedForBtn(preauthDTO.getIsPEDInitiated());
				
				Map<String, String> popupMessages = dbCalculationService.getPOPUPMessages(preauthDTO.getPolicyKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
				preauthDTO.setPopupMap(popupMessages);
				
				if(preauthDTO.getNewIntimationDTO().getHospitalDto() != null && preauthDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag() != null
						&& preauthDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousFlag().equalsIgnoreCase("Y")){
					//if(preauthDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks() != null){
						Map<String, String> suspiciousMap = SHAUtils.getSuspiciousMap(preauthDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks() != null ? preauthDTO.getNewIntimationDTO().getHospitalDto().getSuspiciousRemarks() : SHAConstants.SUSPICIOUS_HOSP);
						preauthDTO.setSuspiciousPopupMap(suspiciousMap);
					//}
				}
				if(preauthDTO.getNewIntimationDTO().getHospitalDto() != null && preauthDTO.getNewIntimationDTO().getHospitalDto().getNonPreferredFlag() != null
						&& preauthDTO.getNewIntimationDTO().getHospitalDto().getNonPreferredFlag().equalsIgnoreCase("Y")){
					//if(preauthDTO.getNewIntimationDTO().getHospitalDto().getNonPreferredRemarks() != null){
						Map<String, String> nonPreferredMap = SHAUtils.getNonPreferredMap(preauthDTO.getNewIntimationDTO().getHospitalDto().getNonPreferredRemarks() != null ? preauthDTO.getNewIntimationDTO().getHospitalDto().getNonPreferredRemarks() : SHAConstants.NON_PREFERRED_HOSP);
						preauthDTO.setNonPreferredPopupMap(nonPreferredMap);
					//}
				}
				
				  Date	dbCalEndate = new Date();
					log.info("%%%%%%%%%%%%%%%%%%%%%%STARTING TIME FOR  DB CALCULATION SERVICE  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+dbCalEndate);
					
					log.info("%%%%%%%%%%%%%%%%%%%%%% TOTAL TIME FOR 64VB PREMIA CALL %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+SHAUtils.getDurationFromTwoDate(dbCalStartDate, dbCalEndate));
					
				
				Date policyFromDate = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyFromDate();
				
				Date admissionDate = preauthDTO.getNewIntimationDTO().getAdmissionDate();
				Long diffDays = SHAUtils.getDiffDays(policyFromDate, admissionDate);
				if(diffDays != null && diffDays<90){
					preauthDTO.setIs64VBChequeStatusAlert(true);
				}
				StarCommonUtils.getIncidentFlagAndDate(claimByKey, preauthDTO);
				preauthDTO.setDbOutArray(tableDTO.getDbOutArray());
				
				if(claimByKey.getIntimation().getUnNamedKey() != null){
					preauthDTO.setUnNamedKey(claimByKey.getIntimation().getUnNamedKey());
				}
				//gmc
				
				Intimation intimation = claimByKey.getIntimation();
				Long mainNo = 0L;
				if(intimation.getInsured().getDependentRiskId() !=null){
					mainNo = intimation.getInsured().getDependentRiskId();
				}else{
					mainNo = intimation.getInsured().getInsuredId();
				}
				Map<String, Double> values = dbCalculationService.getGmcCorpBufferASIForRegister(SHAConstants.PRC_BUFFERTYPE_CB,intimation.getPolicy().getPolicyNumber(),intimation.getInsured().getKey(),mainNo,claimByKey.getKey());
				
				if(values != null && !values.isEmpty()){
					if(values.get(SHAConstants.LN_POLICY_BUFFER_SI) != null){
						preauthDTO.getPreauthDataExtractionDetails().setCorpBufferSI(values.get(SHAConstants.LN_POLICY_BUFFER_SI));	
					}
					if(values.get(SHAConstants.LN_BUFFER_UTILISED_AMT) != null){
						preauthDTO.getPreauthDataExtractionDetails().setCorpBufferUtilisedAmt(values.get(SHAConstants.LN_BUFFER_UTILISED_AMT));	
					}
					if(values.get(SHAConstants.LN_INSURED_ALLOCATE_AMT) != null){
						preauthDTO.getPreauthDataExtractionDetails().setCorpBufferLimit(values.get(SHAConstants.LN_INSURED_ALLOCATE_AMT));	
					}
					if(values.get(SHAConstants.LN_INSURED_UTIL_AMT) != null &&
							values.get(SHAConstants.LN_INSURED_ALLOCATE_AMT) !=null){
						Double avlbBalance = values.get(SHAConstants.LN_INSURED_ALLOCATE_AMT) - values.get(SHAConstants.LN_INSURED_UTIL_AMT);
						preauthDTO.getPreauthDataExtractionDetails().setCorpBufferAvalBal(avlbBalance);
					}		
				}
					
				Map<String, Integer> productBenefitMap = dbCalculationService.getProductBenefitFlag(
						claimByKey.getKey(), claimByKey.getIntimation().getPolicy()
						.getProduct().getKey());
				
				if(productBenefitMap != null && productBenefitMap.containsKey(SHAConstants.OTHER_BENEFITS_FLAG) && productBenefitMap.get(SHAConstants.OTHER_BENEFITS_FLAG) != null){
					
					int benefitFlag = (int)productBenefitMap.get(SHAConstants.OTHER_BENEFITS_FLAG);
					
					preauthDTO.getPreauthDataExtractionDetails().setOtherBeneitApplicableFlag(benefitFlag);
				}
				else{
					preauthDTO.getPreauthDataExtractionDetails().setOtherBeneitApplicableFlag(0);
				}
				
				if(ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					preauthDTO.getPreauthDataExtractionDetails().setOtherBeneitApplicableFlag(0);
				}
				
				if(claimByKey!=null){
					
					Long hospital = claimByKey.getIntimation().getHospital();
					Hospitals hospitalById = hospitalService.getHospitalById(hospital);
					if(hospitalById!=null && hospitalById.getSuspiciousType()!=null){
						preauthDTO.setIsSuspicious(hospitalById.getSuspiciousType());
						if(hospitalById!=null && hospitalById.getSuspiciousType()!=null && !hospitalById.getSuspiciousType().equalsIgnoreCase("Suspicious")){
							preauthDTO.setClmPrcsInstruction(hospitalById.getClmPrcsInstruction());
						}
					}
				}
				
				Boolean policyValidation = policyService.getPolicyValidationObject(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());
				preauthDTO.setIsPolicyValidate(policyValidation);			
				
				Product product = preauthDTO.getNewIntimationDTO().getPolicy()
						.getProduct();
				if (product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey())){
					List<UpdateOtherClaimDetailDTO> updateOtherClaimDetails = preauthService.getUpdateOtherClaimDetails(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO);
					preauthDTO.setUpdateOtherClaimDetailDTO(updateOtherClaimDetails);
				}
				
				Boolean popupMessagesForProduct = dbCalculationService.getPOPUPMessagesForProduct(preauthDTO.getNewIntimationDTO().getPolicy().getKey(), claimByKey.getIntimation().getInsured().getKey(), claimByKey.getIntimation().getPolicy().getProduct().getKey());
				preauthDTO.setIsChangeInsumInsuredAlert(popupMessagesForProduct);
				List<InsuredPedDetails> pedByInsured = policyService.getPEDByInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
				List<PreExistingDisease> approvedPedByInsured = policyService.getPedList(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
				
				preauthDTO.setInsuredPedDetails(pedByInsured);
				preauthDTO.setApprovedPedDetails(approvedPedByInsured);
				
				preauthDTO.setCrcFlaggedReason(claimByKey.getCrcFlaggedReason());
				preauthDTO.setCrcFlaggedRemark(claimByKey.getCrcFlaggedRemark());
				preauthDTO.setCrmFlagged(claimByKey.getCrcFlag());
				
				preauthDTO.setVipCustomer(claimByKey.getIsVipCustomer());
				preauthDTO.setClaimPriorityLabel(claimByKey.getClaimPriorityLabel());
				
				Policy policy = preauthDTO.getNewIntimationDTO().getPolicy();
				
				if(ReferenceTable.getGMCProductList().containsKey(policy.getProduct().getKey())){
					List<SelectValue> duplicateInsuredDetails =dbCalculationService.getDuplicateInsuredDetails(policy.getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
					preauthDTO.setDuplicateInsuredList(duplicateInsuredDetails);
				}
				String topAlertFlag = claimService.getTopUpPolicyDetails(policy.getPolicyNumber(),preauthDTO);
				preauthDTO.setTopUpPolicyAlertFlag(topAlertFlag);
				
				Boolean isZUAQueryAvailable = masterService.getZUAQueryDetails(preauthDTO.getNewIntimationDTO().getPolicy().getPolicyNumber());		
				if(null != isZUAQueryAvailable){
					
					preauthDTO.setIsZUAQueryAvailable(isZUAQueryAvailable);
				}
				preauthDTO.getPreauthMedicalDecisionDetails().setIsFvrIntiated(preauthService.getFVRStatusIdByClaimKey(preauthDTO.getClaimKey()));
//				if(null != preauthDTO.getNewIntimationDTO().getIsTataPolicy() && preauthDTO.getNewIntimationDTO().getIsTataPolicy()){
					preauthDTO.getPreauthDataExtractionDetails().setAdmissionDate(preauthDTO.getNewIntimationDTO().getAdmissionDate());
//				}
				
					view.setPreauthWizardViewForPA(PAPreauthWizard.class, preauthDTO,
							(Boolean) parameters.getSecondaryParameter(0, Boolean.class));
				
				 Date endDate = new Date();
				 log.info("%%%%%%%%%%%%%%%%%%%%%% ENDING TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+endDate);
				 
				 log.info("%%%%%%%%%%%%%%%%%%%%%% TOTAL TIME FOR DB FETCHING %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+SHAUtils.getDurationFromTwoDate(startDate, endDate));
				
			}
		}
		
		protected void showProcessClaimRequestNonHosWaitForInput(
				@Observes @CDIEvent(MenuItemBean.PA_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT) final ParameterDTO parameters) {
			SearchProcessClaimRequestFormDTO dto = null;
			if (parameters.getPrimaryParameter() != null
					&& (parameters.getPrimaryParameter() instanceof SearchProcessClaimRequestFormDTO)) {
				dto = (SearchProcessClaimRequestFormDTO) parameters
						.getPrimaryParameter();
			}
			BeanItemContainer<SelectValue> insuranceSource = masterService
					.getMasterValueByReference(ReferenceTable.INTIMATION_SOURCE);
			BeanItemContainer<SelectValue> networkHospitalType = masterService
					.getMasterValueByReference(ReferenceTable.NETWORK_HOSPITAL_TYPE);
			BeanItemContainer<SelectValue> hospitalType = masterService
					.getMasterValueByReference(ReferenceTable.HOSPITAL_TYPE);
			BeanItemContainer<SelectValue> treatmentType = masterService
					.getMasterValueByReference(ReferenceTable.TREATMENT_MANAGEMENT);

			BeanItemContainer<SelectValue> productName = masterService
					.getSelectValueContainerForProductNameAndCode(SHAConstants.PA_LOB);
			BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();

			BeanItemContainer<SelectValue> selectValueContainerForType = masterService
					.getStageList();

			BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
					.getSelectValueForPriority();

			// BeanItemContainer<SelectValue> statusByStage =
			// masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);

			Stage stageByKey = preauthService
					.getStageByKey(ReferenceTable.ZONAL_REVIEW_STAGE);
			Status statusByKey2 = preauthService
					.getStatusByKey(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER);
			Status statuseByKey3 = preauthService
					.getStatusByKey(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);
			Status statusByKey4 = preauthService
					.getStatusByKey(ReferenceTable.CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS);
			Status statusByKey5 = preauthService
					.getStatusByKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS);
			Status statusByKey6 = preauthService
					.getStatusByKey(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);
			Status statusByKey7 = preauthService
					.getStatusByKey(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
			Status statusByKey8 = preauthService
					.getStatusByKey(ReferenceTable.CLAIM_REQUEST_QUERY_RECEIVED_STATUS);
			// Status statusByKey9 =
			// preauthService.getStatusByKey(ReferenceTable.FA_QUERY_REPLY_STATUS);

			SelectValue selectValue = new SelectValue();
			selectValue.setId(stageByKey.getKey());
			selectValue.setValue(stageByKey.getStageName());

			SelectValue selectValue1 = new SelectValue();
			selectValue1.setId(statusByKey2.getKey());
			selectValue1.setValue("Billing- " + statusByKey2.getProcessValue());

			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(statuseByKey3.getKey());
			selectValue2.setValue("FA- " + statuseByKey3.getProcessValue());

			SelectValue selectValue3 = new SelectValue();
			selectValue3.setId(statusByKey4.getKey());
			selectValue3.setValue(statusByKey4.getProcessValue());

			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(statusByKey5.getKey());
			selectValue4.setValue(statusByKey5.getProcessValue());

			SelectValue selectValue5 = new SelectValue();
			selectValue5.setId(statusByKey6.getKey());
			selectValue5.setValue(statusByKey6.getProcessValue());

			SelectValue selectValue6 = new SelectValue();
			selectValue6.setId(statusByKey7.getKey());
			selectValue6.setValue(statusByKey7.getProcessValue());

			SelectValue selectValue7 = new SelectValue();
			selectValue7.setId(statusByKey8.getKey());
			selectValue7.setValue(SHAConstants.QUERY_REPLY);

			SelectValue selectValue8 = new SelectValue();
			selectValue8.setId(0l);
			selectValue8.setValue(SHAConstants.RECONSIDERATION);

			BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			statusByStage.addBean(selectValue);
			statusByStage.addBean(selectValue1);
			statusByStage.addBean(selectValue2);
			statusByStage.addBean(selectValue3);
			statusByStage.addBean(selectValue4);
			statusByStage.addBean(selectValue5);
			statusByStage.addBean(selectValue6);
			statusByStage.addBean(selectValue7);
			statusByStage.addBean(selectValue8);

			view.setViewsPAProcessClaimRequest(
					PASearchProcessClaimRequestView.class, true, insuranceSource,
					hospitalType, networkHospitalType, treatmentType,
					selectValueContainerForType, productName, cpuCode,
					selectValueForPriority, statusByStage, dto,SHAConstants.PA_MEDICAL_WAIT_FOR_INPUT_SCREEN);
		}
		
		protected void showPAHealthProcessClaimRequestHosWaitForInput(
				@Observes @CDIEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT) final ParameterDTO parameters) {
			PAHealthSearchProcessClaimRequestFormDTO dto = null;
			if (parameters.getPrimaryParameter() != null
					&& (parameters.getPrimaryParameter() instanceof SearchProcessClaimRequestFormDTO)) {
				dto = (PAHealthSearchProcessClaimRequestFormDTO) parameters
						.getPrimaryParameter();
			}
			BeanItemContainer<SelectValue> insuranceSource = masterService
					.getMasterValueByReference(ReferenceTable.INTIMATION_SOURCE);
			BeanItemContainer<SelectValue> networkHospitalType = masterService
					.getMasterValueByReference(ReferenceTable.NETWORK_HOSPITAL_TYPE);
			BeanItemContainer<SelectValue> hospitalType = masterService
					.getMasterValueByReference(ReferenceTable.HOSPITAL_TYPE);
			BeanItemContainer<SelectValue> treatmentType = masterService
					.getMasterValueByReference(ReferenceTable.TREATMENT_MANAGEMENT);

			BeanItemContainer<SelectValue> productName = masterService
					.getSelectValueContainerForProduct();
			BeanItemContainer<SelectValue> cpuCode = masterService.getTmpCpuCodes();

			BeanItemContainer<SelectValue> selectValueContainerForType = masterService
					.getStageList();

			BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils
					.getSelectValueForPriority();

			// BeanItemContainer<SelectValue> statusByStage =
			// masterService.getStatusByStage(ReferenceTable.CREATE_ROD_STAGE_KEY);

			Stage stageByKey = preauthService
					.getStageByKey(ReferenceTable.ZONAL_REVIEW_STAGE);
			Status statusByKey2 = preauthService
					.getStatusByKey(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER);
			Status statuseByKey3 = preauthService
					.getStatusByKey(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER);
			Status statusByKey4 = preauthService
					.getStatusByKey(ReferenceTable.CLAIM_REQUEST_COORDINATOR_REPLY_RECEIVED_STATUS);
			Status statusByKey5 = preauthService
					.getStatusByKey(ReferenceTable.CLAIM_REQUEST_SPECIALIST_REPLY_RECEIVED_STATUS);
			Status statusByKey6 = preauthService
					.getStatusByKey(ReferenceTable.UPLOAD_INVESIGATION_COMPLETED);
			Status statusByKey7 = preauthService
					.getStatusByKey(ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
			Status statusByKey8 = preauthService
					.getStatusByKey(ReferenceTable.CLAIM_REQUEST_QUERY_RECEIVED_STATUS);
			// Status statusByKey9 =
			// preauthService.getStatusByKey(ReferenceTable.FA_QUERY_REPLY_STATUS);

			SelectValue selectValue = new SelectValue();
			selectValue.setId(stageByKey.getKey());
			selectValue.setValue(stageByKey.getStageName());

			SelectValue selectValue1 = new SelectValue();
			selectValue1.setId(statusByKey2.getKey());
			selectValue1.setValue("Billing- " + statusByKey2.getProcessValue());

			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(statuseByKey3.getKey());
			selectValue2.setValue("FA- " + statuseByKey3.getProcessValue());

			SelectValue selectValue3 = new SelectValue();
			selectValue3.setId(statusByKey4.getKey());
			selectValue3.setValue(statusByKey4.getProcessValue());

			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(statusByKey5.getKey());
			selectValue4.setValue(statusByKey5.getProcessValue());

			SelectValue selectValue5 = new SelectValue();
			selectValue5.setId(statusByKey6.getKey());
			selectValue5.setValue(statusByKey6.getProcessValue());

			SelectValue selectValue6 = new SelectValue();
			selectValue6.setId(statusByKey7.getKey());
			selectValue6.setValue(statusByKey7.getProcessValue());

			SelectValue selectValue7 = new SelectValue();
			selectValue7.setId(statusByKey8.getKey());
			selectValue7.setValue(SHAConstants.QUERY_REPLY);

			SelectValue selectValue8 = new SelectValue();
			selectValue8.setId(0l);
			selectValue8.setValue(SHAConstants.RECONSIDERATION);

			BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			statusByStage.addBean(selectValue);
			statusByStage.addBean(selectValue1);
			statusByStage.addBean(selectValue2);
			statusByStage.addBean(selectValue3);
			statusByStage.addBean(selectValue4);
			statusByStage.addBean(selectValue5);
			statusByStage.addBean(selectValue6);
			statusByStage.addBean(selectValue7);
			statusByStage.addBean(selectValue8);

			view.setViewsPAHealthProcessClaimRequest(
					PAHealthSearchProcessClaimRequestView.class, true,
					insuranceSource, hospitalType, networkHospitalType,
					treatmentType, selectValueContainerForType, productName,
					cpuCode, selectValueForPriority, statusByStage, dto,SHAConstants.PA_HEALTH_MEDICAL_WAIT_FOR_INPUT_SCREEN);
		}
		
}
