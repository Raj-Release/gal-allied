/**
 * 
 */
package com.shaic.main.navigator.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
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
import com.shaic.claim.OMPClaimPayment;
import com.shaic.claim.OMPBulkUploadRejection.OMPBulkUploadRejectionView;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPNegotiationDetailsDTO;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPProcessNegotiationWizardView;
import com.shaic.claim.OMPProcessNegotiation.search.OMPProcessNegotiationTableDTO;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPAcknowledgementDocumentsPageWizard;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPProcessOmpAcknowledgementDocumentsTableDTO;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPProcessOmpAcknowledgementDocumentsView;
import com.shaic.claim.OMPProcessOmpClaimApprover.pages.OMPProcessOmpClaimApproverWizardView;
import com.shaic.claim.OMPProcessOmpClaimApprover.search.OMPProcessOmpClaimApproverTableDTO;
import com.shaic.claim.OMPProcessOmpClaimApprover.search.OMPProcessOmpClaimApproverView;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorCalculationSheetDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPNewRecoverableTableDto;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPProcessOmpClaimProcessorPageWizard;
import com.shaic.claim.OMPProcessOmpClaimProcessor.search.OMPProcessOmpClaimProcessorTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.search.OMPProcessOmpClaimProcessorView;
import com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages.OMPCreateIntimationWizardView;
//import com.shaic.claim.OMPclaimregistration.search.SearchOMPClaimRegistrationPresenter;
//import com.shaic.claim.OMPclaimregistration.search.SearchOMPClaimRegistrationView;
import com.shaic.claim.OMPprocessrejection.detailPage.OMPProcessRejectionView;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryPageWizard;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.search.OMPReceiptofDocumentsAndBillEntryTableDTO;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationTableDTO;
import com.shaic.claim.omp.createintimation.OMPCreateIntimationView;
import com.shaic.claim.omp.newregistration.OMPNewRegistrationPageView;
import com.shaic.claim.omp.newregistration.OMPNewRegistrationSearchDTO;
import com.shaic.claim.omp.newregistration.OMPNewRegistrationView;
import com.shaic.claim.omp.ratechange.OMPClaimRateChangeAndOsUpdationTableDTO;
import com.shaic.claim.omp.ratechange.OMPClaimRateChangeAndOsUpdationView;
import com.shaic.claim.omp.registration.OMPClaimRegistrationWizardView;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationPresenter;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.claim.reports.shadowProvision.SearchShowdowView;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Currency;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasOmbudsman;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPDocumentDetails;
import com.shaic.domain.OMPHospitals;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.omp.OMPBenefitCover;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.omp.OMPNegotiation;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.domain.OMPClaimMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
//import com.shaic.claim.OMPclaimregistration.wizard.pages.OMPClaimRegistrationWizardView;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author temp
 *
 */
@SuppressWarnings("serial")
@ViewInterface(OMPMenuView.class)
public class OMPMenuPresenter extends AbstractMVPPresenter<OMPMenuView>  {
	

	public static final String OMP_SEARCHINTIMATION_CREATE = "omp_searchintimationcreate";
	public static final String OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION = "omp_claimsratechangeandop";
	public static final String OMP_CLAIM_REGISTRATION = "omp_claimregistration";
	public static final String OMP_PROCESS_REJECTION = "omp_Processrejection";
	public static final String OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY = "omp_receiptofdocandbillentry";
	//public static final String OMP_CLAIM_PROCESS_MAKER = "omp_claimprocessmaker";
	//public static final String OMP_CLAIM_PRPCESS_CHECKER = "omp_claimprocesschecker";
	//public static final String OMP_TPA_SERVICE_CHARGES_PAYMENT_MAKER = "omp_tpaservicechargespayment";
	//public static final String OMP_INTIMATION_DETAIL_REPORT = "omp_intimationdetailreport";
	//public static final String OMP_PAID_DETAIL_REPORT = "ömp_paiddetailreport";
	//public static final String OMP_OUTSTANDING_DETAIL_REPORT = "omp_outstandingdetailreport";
	//public static final String OMP_RE_OPEN_CLAIM = "omp_reopenclaim";
	//public static final String OMP_CLOSE_CLAIM = "omp_closeclaim";
	public static final String OMP_PROCESS_OMP_NEGOTIATION = "omp_processompnegotation";
	public static final String OMP_PROCESS_OMP_CLAIM_PROCESSOR = "omp_processompclaimprocessor";
	public static final String OMP_PROCESS_OMP_CLAIM_APPROVER = "omp_processompclaimapprover";
	//public static final String OMP_REOPEN_CLAIM_ROD_LEVEL_SEARCHBASED = "omp_reopenclaimrodlevelsearchbased";
	//public static final String OMP_REOPEN_CLAIM_CLAIM_LEVEL_SEARCHBASED = "omp_reopenclaimclaimlevaelsearchbased";
	//public static final String OMP_CLOSE_CLAIM_ROD_LEVEL_SEARCHBASED = "omp_closeclaimrodlevelsearchbased";
	//public static final String OMP_CLOSE_CLAIM_CLAIM_LEVEL_SEARCHBASED = "omp_closeclaimclaimlevelsearchbased";
	

	public static final String OMP_CLAIM_REGISTRARION_DETAILPAGE = "omp_claimregistration_detail_page";
	public static final String OMP_REOPEN_RODCLAIM_DETAILPAGE = "omp_reOpenROD_claim_detail_page";
	

	public static final String OMP_PROCESS_REJECTION_DETAIL_PAGE = "OMP PROCESS REJECTION DETAIL PAGE";

	public static final String SHOW_OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY = "Show Omp receipt of document bill entry";
	
	public static final String OMP_PROCESS_APPROVER_DETAIL_PAGE = "omp process approver detail page";
	
	public static final String OMP_PROCESS_DETAIL_PAGE = "omp process detail page";
	
	public static final String OMP_PROCESS_NEGOTIATION_DETAIL_PAGE = "omp process negotitaton detail page";
	
	public static final String OMP_INTIMATION_REG = "omp registration page";
	
	public static final String OMP_REG_DETAIL_PAGE = "omp_reg_detail_page";
	
	public static final String OMP_PROCESS_ACK_DETAIL_PAGE = "omp process ack detail page";
	
	private final Logger log = LoggerFactory.getLogger(OMPMenuPresenter.class);
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private OMPIntimationService ompIntimationService;
	
	@EJB
	private OMPClaimService ompClaimService;
	
	@EJB
	private PolicyService policyService;
	

	@EJB
	private DBCalculationService CalculationService;

	@Inject
	private DBCalculationService dbCalculationService;
	
	@Inject
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private IntimationService intimationService;
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private ClaimService claimService;
	@EJB
	private OMPProcessRODBillEntryService ompRodBillEntryService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	/*protected void showUnlockIntimationBPMNToDB(
			@Observes @CDIEvent(MenuItemBean.UNLOCK_OMP_INTIMATION_DB) final ParameterDTO parameters) {
		view.setViewG(SearchUnlockOMPIntimationDBView.class, true);
		
	}*/
	
	protected void showOMPCreateIntimationSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_SEARCHINTIMATION_CREATE) final ParameterDTO parameters){
		
		
		view.setViewG(OMPCreateIntimationView.class, true);
	}
	
	protected void showOMPRegistrationSearch(@Observes @CDIEvent(MenuItemBean.OMP_INTIMATION_REG) final ParameterDTO parameters){
		view.setViewG(OMPNewRegistrationView.class, true);
	}
	
	
	
	
	/*protected void showOMPClaimRegistrationSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_CLAIM_REGISTRATION) final ParameterDTO parameters){
		OMPSearchClaimRegistrationTableDTO ompClaimRegistrationTableDTO = (OMPSearchClaimRegistrationTableDTO) parameters
				.getPrimaryParameter();
//		NewIntimationDto newIntimationDto = ompClaimRegistrationTableDTO
//				.getNewIntimationDto();
		
	view.setOMPSearchRegistration(OMPSearchClaimRegisterView.class, true);
//		
//	//	view.setOmpClaimRegistrationForm(OMPClaimRegistrationWizardView.class,
//	//			ompClaimRegistrationTableDTO);
		
	
	}*/
	
	
	protected void setOmpClaimDetailRegistrationPage(@Observes @CDIEvent(OMPSearchClaimRegistrationPresenter.OMP_CREATE_CLAIM_REGISTRATION) final ParameterDTO parameters){
		OMPSearchClaimRegistrationTableDTO ompClaimDetailRegistrationDTO = (OMPSearchClaimRegistrationTableDTO) parameters.getPrimaryParameter();
		NewIntimationDto newIntimationDto = ompClaimDetailRegistrationDTO.getNewIntimationDto();
			ClaimDto claimDto = new ClaimDto();
			claimDto.setNewIntimationDto(newIntimationDto);
			ompClaimDetailRegistrationDTO.setClaimDto(claimDto);
			
			claimDto.setDollarInitProvisionAmount(ompClaimDetailRegistrationDTO.getInitlProvisionAmt());
			claimDto.setInrConversionRate(ompClaimDetailRegistrationDTO.getInrConversionRate());
			claimDto.setInrTotalAmount(ompClaimDetailRegistrationDTO.getInrValue());
			
			
			claimDto.setEventCodeValue(ompClaimDetailRegistrationDTO.getType());
			
			
			
			claimDto.setHospitalName(ompClaimDetailRegistrationDTO.getNewIntimationDto().getHospitalName());
			claimDto.setCityName(ompClaimDetailRegistrationDTO.getNewIntimationDto().getHospitalCity());
			claimDto.setAilmentLoss(ompClaimDetailRegistrationDTO.getNewIntimationDto().getAilmentLoss());
			claimDto.setClaimType(ompClaimDetailRegistrationDTO.getCmbclaimType());
			if(ompClaimDetailRegistrationDTO.getCmbclaimType()!=null){
				SelectValue cmbclaimType = ompClaimDetailRegistrationDTO.getCmbclaimType();
				if(cmbclaimType.getValue()!=null){
					if(ReferenceTable.CASHLESS_CLAIM.equalsIgnoreCase(cmbclaimType.getValue())){
						claimDto.setClaimTypeBoolean(Boolean.TRUE);
					}else{
						claimDto.setClaimTypeBoolean(Boolean.FALSE);
					}
				}
			}
			claimDto.setHospitalisationFlag(ompClaimDetailRegistrationDTO.getNewIntimationDto().getHospitalizationFlag());
			claimDto.setNonHospitalisationFlag(ompClaimDetailRegistrationDTO.getNewIntimationDto().getNonHospitalizationFlag());
			if(ompClaimDetailRegistrationDTO.getHospitalCountry()!=null){
				claimDto.setCountryId(ompClaimDetailRegistrationDTO.getHospitalCountry().getId());
			}
			if(ompClaimDetailRegistrationDTO.getNewIntimationDto().getHospitalizationFlag()!=null ){
				if(SHAConstants.YES_FLAG.equalsIgnoreCase(ompClaimDetailRegistrationDTO.getNewIntimationDto().getHospitalizationFlag())){
					claimDto.setHospitalTypeBoolean(Boolean.TRUE);
				}else{
					claimDto.setHospitalTypeBoolean(Boolean.FALSE);
				}
			}
			claimDto.setHospitalType(ompClaimDetailRegistrationDTO.getHospType());
			claimDto.setAdmissionDate(ompClaimDetailRegistrationDTO.getAdmissiondate());
			claimDto.setProductName(ompClaimDetailRegistrationDTO.getProductName());
			claimDto.setProductCode(ompClaimDetailRegistrationDTO.getNewIntimationDto().getPolicy().getProduct().getCode());
			if(ompClaimDetailRegistrationDTO.getType() != null && ompClaimDetailRegistrationDTO.getType().getId() != null){
			claimDto.setEventCodeId(ompClaimDetailRegistrationDTO.getType().getId());
			}
//		RRCDTO rrcDTO = new RRCDTO();
//		// rrcDTO.setClaimDto(claimDTO);
//		rrcDTO.setNewIntimationDTO(newIntimationDto);
//		rrcDTO.setStrUserName(ompClaimDetailRegistrationDTO.getUsername());
//		rrcDTO.setHumanTask(ompClaimDetailRegistrationDTO.getHumanTask());
//		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
//				rrcDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId()
//						.toString(), rrcDTO.getNewIntimationDTO().getPolicy()
//						.getKey());
//		loadRRCRequestValuesForCashless(rrcDTO, insuredSumInsured,
//				SHAConstants.PROCESS_CLAIM_REGISTRATION);
//		searchClaimRegistrationTableDto.setRrcDTO(rrcDTO);
			Double balanceSI =0d;
		if(newIntimationDto.getEventCode()!=null && newIntimationDto.getPolicy()!=null){
			//SelectValue eventCodeValue = newIntimationDto.getEventCodeValue();
			//MastersEvents event = newIntimationDto.getEvent();
			String eventCode = newIntimationDto.getEventCode();
			Double sumInsured = 0d;
			sumInsured = dbCalculationService.getOmpInsuredSumInsured(newIntimationDto.getPolicy().getKey(), eventCode);
			balanceSI = dbCalculationService.getOmpBalanceSI(
					newIntimationDto.getPolicy().getKey(),
					newIntimationDto.getInsuredPatient().getKey(), 0l, null,
					sumInsured,  	//uncomment after testing all not rrc
					newIntimationDto.getKey(),eventCode).get(SHAConstants.TOTAL_BALANCE_SI);
		}
		
		
		Map<String, String> popupMessages = new HashMap<String, String>();
		if (balanceSI != null && !(balanceSI > 0)) {
			popupMessages.put(SHAConstants.BALANCE_SI,
					"<b style = 'black'>Balance SI is Zero </b>");
		}
		if (newIntimationDto.getAdmissionType() != null
				&& newIntimationDto.getAdmissionType().getId()
						.equals(ReferenceTable.LATE_INTIMATION_KEY)) {
			popupMessages.put(SHAConstants.LATE_INTIMATION,
					"<b style = 'black'>Late intimation </b>");
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
		ompClaimDetailRegistrationDTO
				.setSuspiciousPopUp(suspiciousPopupMessage);

		if (newIntimationDto.getPolicy() != null
				&& newIntimationDto.getPolicy().getPolicyStatus() != null) {
			if (newIntimationDto.getPolicy().getPolicyStatus()
					.equalsIgnoreCase(SHAConstants.ENDORESED_POLICY)) {
				ompClaimDetailRegistrationDTO
						.setIsProceedFurther(PremiaService.getInstance()
								.getEndorsedPolicyStatus(
										newIntimationDto.getPolicy()
												.getPolicyNumber()));
			} else if (newIntimationDto.getPolicy().getPolicyStatus()
					.equalsIgnoreCase(SHAConstants.CANCELLED_POLICY)) {
				ompClaimDetailRegistrationDTO.setIsProceedFurther(false);
				ompClaimDetailRegistrationDTO.setIsCancelledPolicy(true);
			}
		}

		Policy policyByKey = policyService.getPolicyByKey(newIntimationDto
				.getPolicy().getKey());
		/*if(policyByKey
				.getHomeOfficeCode()!=null){
		MASClaimAdvancedProvision claimAdvProvision = policyService
				.getClaimAdvProvision(Long.valueOf(policyByKey
						.getHomeOfficeCode()));
		ompClaimDetailRegistrationDTO.setClaimedAmount(claimAdvProvision
				.getAvgAmt());
		}*/
		popupMessages.putAll(dbCalculationService.getPOPUPMessages(
				newIntimationDto.getPolicy().getKey(), newIntimationDto
						.getInsuredPatient().getKey(),newIntimationDto.getPolicy().getProduct().getKey()));
		ompClaimDetailRegistrationDTO.setPopupMap(popupMessages);
//		view.setViewG(OMPClaimRegistrationWizardView.class,true); //
		view.setOmpClaimDetailRegistrationPage(
				OMPClaimRegistrationWizardView.class, ompClaimDetailRegistrationDTO,
				(Boolean) parameters.getSecondaryParameter(0, Boolean.class));
	}
	
	
	
	/*protected void showOMPProcessRejectionSearch(
			@Observes @CDIEvent(MenuItemBean.SEARCH_OMP_PROCESS_REJECTION) final ParameterDTO parameters){
		view.setViewG(SearchOMPProcessRejectionView.class, true);
	}

	protected void showOMPReceiptOfDocandBillEntrySearch(
			@Observes @CDIEvent(MenuItemBean.OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY) final ParameterDTO parameters){
		view.setViewG(OMPReceiptofDocumentsAndBillEntryView.class, true);
	}*/
	protected void showOMPClaimsRateChangeAndOutstandingUpdationSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION) final ParameterDTO parameters){
		OMPClaimRateChangeAndOsUpdationTableDTO claimRateDto = (OMPClaimRateChangeAndOsUpdationTableDTO) parameters.getPrimaryParameter();
		view.setSearchClaimRateChangeViewG(OMPClaimRateChangeAndOsUpdationView.class, true);
	}
	
	/*protected void showOMPClaimProcessMakerSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_CLAIM_PROCESS_MAKER) final ParameterDTO parameters){
		view.setViewG(OMPClaimProcessMakerView.class, true);
	}
	
	protected void showOMPClaimProcessCheckerSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_CLAIM_PRPCESS_CHECKER) final ParameterDTO parameters){
		view.setViewG(OMPClaimProcessCheckerView.class, true);
	}
	protected void showOMPTpaServiceChargesPaymentMakerSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_TPA_SERVICE_CHARGES_PAYMENT_MAKER) final ParameterDTO parameters){
		view.setViewG(OMPTpaServiceChargesMakerView.class, true);
	}
	protected void showOMPClaimsRateChangeAndOutstandingUpdationSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION) final ParameterDTO parameters){
		view.setViewG(OMPClaimRateChangeAndOsUpdationView.class, true);
	}
	protected void showOMPIntimationDetailReportSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_INTIMATION_DETAIL_REPORT) final ParameterDTO parameters){
		view.setViewG(OMPIntimationDetailReportView.class, true);
	}
	
	protected void showOMPPaidDetailReportSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_PAID_DETAIL_REPORT) final ParameterDTO parameters){
		view.setViewG(OMPPaidDetailReportView.class, true);
	}
	protected void showOMPOutstandingDetailReportSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_OUTSTANDING_DETAIL_REPORT) final ParameterDTO parameters){
		view.setViewG(OMPOutstandingDetailReportView.class, true);
	}
			
	protected void showOMPReOpenClaimSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_RE_OPEN_CLAIM) final ParameterDTO parameters){
		view.setViewG(OMPReOpenClaimView.class, true);
	}
	
	protected void showOMPCloseClaimSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_CLOSE_CLAIM) final ParameterDTO parameters){
		view.setViewG(OMPCloseClaimView.class, true);
	}*/
/*	protected void showOMPProcessNegotiationSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_PROCESS_OMP_NEGOTIATION) final ParameterDTO parameters){
		OMPProcessNegotiationTableDTO ompClaimRegistrationTableDTO = (OMPProcessNegotiationTableDTO) parameters
				.getPrimaryParameter();
		view.setOmpNegotiationViewG(OMPProcessNegotiationView.class, true);
	}*/
	
	protected void showOMPProcessOmpClaimProcessorSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_PROCESS_OMP_CLAIM_PROCESSOR) final ParameterDTO parameters){
		BeanItemContainer<SelectValue> classification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CLASSIFICATION);
		view.setViewG(OMPProcessOmpClaimProcessorView.class, classification,true);
	}
	
	protected void showOMPProcessOmpClaimApproverSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_PROCESS_OMP_CLAIM_APPROVER) final ParameterDTO parameters){
		BeanItemContainer<SelectValue> classification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CLASSIFICATION);
		view.ompProcessApproverSearch(OMPProcessOmpClaimApproverView.class,classification, true);
	}

	// CR20181332
	protected void showOMPBulkUploadRejection(
			@Observes @CDIEvent(MenuItemBean.OMP_BULK_UPLOAD_REJECTION) final ParameterDTO parameters) {
		// view.setViewTATReport(TATReportView.class,selectValueContainerForCpu,selectValueContainerForOfficeCode,true);
		List<SelectValue> statusList = new ArrayList<SelectValue>();

		SelectValue dispatchStatus = new SelectValue();
		dispatchStatus.setId(SHAConstants.DISCHARGE_VOUCHER_KEY);
		dispatchStatus.setValue(SHAConstants.DISPATCH_VOUCHER_VALUE);
		statusList.add(dispatchStatus);

		SelectValue rejectionStatus = new SelectValue();
		rejectionStatus.setId(SHAConstants.REJECTION_FLOW_KEY);
		rejectionStatus.setValue(SHAConstants.REJECTION_FLOW_VALUE);
		statusList.add(rejectionStatus);

		BeanItemContainer<SelectValue> statusContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		statusContainer.addAll(statusList);
		view.ompBulkUploadRejectionSearch(OMPBulkUploadRejectionView.class,
				statusContainer, true);
	}

	
	/*protected void showOMPReOpenClaimRODLevelSearchBasedSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_REOPEN_CLAIM_ROD_LEVEL_SEARCHBASED) final ParameterDTO parameters){
		view.setViewG(OMPReopenClaimRODLevelSearchBasedView.class, true);
	}
	
	protected void setOmpReOpenRODClaimDetailPage(@Observes @CDIEvent(OMPMenuPresenter.OMP_REOPEN_RODCLAIM_DETAILPAGE) final ParameterDTO parameters){
		OMPSearchClaimRegistrationTableDTO ompClaimDetailRegistrationDTO = (OMPSearchClaimRegistrationTableDTO) parameters
				.getPrimaryParameter();
		
	}
	protected void showOMPReOpenClaimClaimLevelSearchBasedSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_REOPEN_CLAIM_CLAIM_LEVEL_SEARCHBASED) final ParameterDTO parameters){
		view.setViewG(OMPReopenClaimClaimLevelSearchBasedView.class, true);
	}
	protected void showOMPCloseClaimRODLevelSearchBasedSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_CLOSE_CLAIM_ROD_LEVEL_SEARCHBASED) final ParameterDTO parameters){
		view.setViewG(OMPCloseClaimRODLevelSearchBasedView.class, true);
	}
	protected void showOMPCloseClaimClaimLevelSearchBasedSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_CLOSE_CLAIM_CLAIM_LEVEL_SEARCHBASED) final ParameterDTO parameters){
		view.setViewG(OMPCloseClaimClaimLevelSearchBasedView.class, true);
	}*/
	
	protected void showOMPProcessRejectionDetailPage(
			@Observes @CDIEvent(OMP_PROCESS_REJECTION_DETAIL_PAGE) final ParameterDTO parameters){

		SearchProcessRejectionTableDTO searchDto = (SearchProcessRejectionTableDTO) parameters
				.getPrimaryParameter();
		ProcessRejectionDTO processRejectionDto = new ProcessRejectionDTO();
//		Preauth preauth = preauthService
//				.getPreauthListByIntimationKey(searchDto.getKey());
		String intimationNo = searchDto.getIntimationNo();
		OMPClaim claimsByIntimationNumber = ompClaimService.getClaimsByIntimationNumber(intimationNo);
		if(claimsByIntimationNumber != null){
		ClaimDto claimdto = OMPClaimMapper.getInstance().getClaimDto(claimsByIntimationNumber);
		NewIntimationDto newIntimationDto = null;
		OMPClaim claimByKey = null;
//		if (preauth != null) {
//			claimByKey = preauth.getClaim();
//			newIntimationDto = intimationService.getIntimationDto(claimByKey
//					.getIntimation());
//		} else {
		
		
//		claimDto.setDollarInitProvisionAmount(ompClaimDetailRegistrationDTO.getInitlProvisionAmt());
//		claimDto.setInrConversionRate(ompClaimDetailRegistrationDTO.getInrConversionRate());
//		claimDto.setInrTotalAmount(ompClaimDetailRegistrationDTO.getInrValue());
		processRejectionDto.setKey(claimsByIntimationNumber.getKey());
		processRejectionDto.setInrConversionRate(String.valueOf(claimdto.getInrConversionRate()));
		processRejectionDto.setProvisionAmt(claimdto.getDollarInitProvisionAmount());
		
		processRejectionDto.setHospitalName(claimdto.getHospitalName());
		processRejectionDto.setHospitalCity(claimdto.getCityName());
		if(claimsByIntimationNumber.getCountryId()!=null){
			SelectValue countryValueByKey = masterService.getCountryValueByKey(claimsByIntimationNumber.getCountryId());
			processRejectionDto.setHospitalCounty(countryValueByKey != null && countryValueByKey.getValue() != null ? countryValueByKey.getValue() : "" );
		}	
		
		
		processRejectionDto.setAilmentLossDetails(claimdto.getAilmentLoss());
		processRejectionDto.setRejectionRemarks(claimdto.getSuggestedRejectionRemarks());
	
	
		if(claimdto.getClaimType() != null){
			SelectValue claimType = claimdto.getClaimType();
			if(claimType.getValue() != null){
				if(ReferenceTable.CASHLESS_CLAIM.equalsIgnoreCase(claimType.getValue())){
					processRejectionDto.setClaimType(Boolean.TRUE);
				}else{
					processRejectionDto.setClaimType(Boolean.FALSE);
			}
		}
		}
			
		if(claimdto.getHospitalisationFlag()!= null){
			if(SHAConstants.YES_FLAG.equalsIgnoreCase(claimdto.getHospitalisationFlag())){
				processRejectionDto.setHospitalisation(Boolean.TRUE);
			}else{
				processRejectionDto.setHospitalisation(Boolean.FALSE);
			}
				
		}
					
		
		SelectValue event = new SelectValue();
		if(searchDto.getEventCodeDescription() != null){
			event.setId(1l);
			event.setValue(searchDto.getEventCodeDescription());
		}
		processRejectionDto.setEventCode(event);
			claimByKey = ompClaimService.getClaimByKey(searchDto.getKey());
			if(claimByKey != null)
			newIntimationDto = ompIntimationService.getIntimationDto(claimByKey
					.getIntimation());
//		}
		searchDto.setIntimationDTO(newIntimationDto);
		processRejectionDto.setInrValue(String.valueOf(newIntimationDto.getInrTotalAmount()));
		ClaimDto claimDTO = null;
		Boolean isValidClaim = true;

		if (claimByKey != null) {

			if ((SHAConstants.YES_FLAG).equalsIgnoreCase(claimByKey
					.getLegalFlag())) {
				isValidClaim = false;
				VaadinSession session = UI.getCurrent().getSession();
				   SHAUtils.setActiveOrDeactiveOMPClaim(searchDto.getWorkFlowKey(),session);
				view.showErrorPopUp("Intimation is locked by legal process. Cannot proceed further");
			} else {

				claimDTO = OMPClaimMapper.getInstance().getClaimDto(claimByKey);
				claimDTO.setNewIntimationDto(newIntimationDto);
			}

		}
		if (isValidClaim) {
			searchDto.setClaimDto(claimDTO);
			searchDto.setIntimationDTO(newIntimationDto);
			searchDto.setUsername(searchDto.getUsername());
			searchDto.setProcessRejectionDTO(processRejectionDto);
			view.setOMPRejectionView(OMPProcessRejectionView.class, searchDto);
		}
	}
			
	}
	
	@SuppressWarnings("unchecked")
	protected void showOMPCreateIntimation(@Observes @CDIEvent(MenuItemBean.NEW_OMP_INTIMATION) final ParameterDTO parameters) {
		OMPCreateIntimationTableDTO createDTO = (OMPCreateIntimationTableDTO)parameters.getPrimaryParameter();
		Insured  insuredObj =insuredService.getInsuredByPolicyNo(createDTO.getPolicy().getKey().toString());
		createDTO.setInsured(insuredObj);
		NewIntimationDto newIntimationDto = createDTO.getIntimationDto();
		ClaimDto claimDto = new ClaimDto();
		claimDto.setNewIntimationDto(newIntimationDto);
		claimDto.setIntimationKey(createDTO.getIntimationKey());
		claimDto.setAdmissionDate(createDTO.getAdmissionDate());
		createDTO.setClaimDto(claimDto);
		view.showOMPAddIntimationView(OMPCreateIntimationWizardView.class, createDTO);
	}
	
	protected void showOmpRodBillEntry(
			@Observes @CDIEvent(OMPMenuPresenter.SHOW_OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY) final ParameterDTO parameters) {
		//PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		OMPReceiptofDocumentsAndBillEntryTableDTO dto = (OMPReceiptofDocumentsAndBillEntryTableDTO) parameters.getPrimaryParameter();
		String intimationNo = dto.getIntimationNo();
		Long rodkey = dto.getRodKey();
		String userName =dto.getUserName();
		OMPClaim claimsByIntimationNumber = ompClaimService.getClaimsByIntimationNumber(intimationNo);
		
		
		
		String productCode = claimsByIntimationNumber.getProductCode();
		Product productByProductCode = masterService.getProductByProductCode(productCode);
		
		if(claimsByIntimationNumber.getStatus()!=null  && !ReferenceTable.PROCESS_REJECTED.equals(claimsByIntimationNumber.getStatus().getKey())){
		OMPClaimProcessorDTO claimProcessorDTO = new OMPClaimProcessorDTO();
		OMPReimbursement reimbursement = ompIntimationService.getReimbursementByKey(rodkey);
		OMPClaimPayment claimPayment = new OMPClaimPayment();
		List<OMPReimbursement> rembursementDetailsByClaimKey = ompIntimationService.getRembursementDetailsByClaimKey(claimsByIntimationNumber.getKey());
		if(rembursementDetailsByClaimKey!=null && rembursementDetailsByClaimKey.size()>=1){
				claimProcessorDTO.setIsMutipleRod(Boolean.TRUE);
			
			/*List<ReconsiderRODRequestTableDTO> reconsiderRodRequestList = new ArrayList<ReconsiderRODRequestTableDTO>();
			
			for (OMPReimbursement ompReimbursement : rembursementDetailsByClaimKey) {
				ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO = new ReconsiderRODRequestTableDTO();
				reconsiderRODRequestTableDTO.setClassification(ompReimbursement.getClassificationId().getValue());
				reconsiderRODRequestTableDTO.setSubclassification(ompReimbursement.getSubClassificationId().getValue());
				reconsiderRODRequestTableDTO.setRodNo(ompReimbursement.getRodNumber());
				if(ompReimbursement.getClassiDocumentRecivedFmId()!=null){
					reconsiderRODRequestTableDTO.setDocumentReceivedFrom(ompReimbursement.getClassiDocumentRecivedFmId().getValue());
				}
				reconsiderRODRequestTableDTO.setDocumentReceivedDate(ompReimbursement.getDocumentRecivedDate());
				if(ompReimbursement.getClassiModeReceiptId()!=null){
					reconsiderRODRequestTableDTO.setModeOfReceipt(ompReimbursement.getClassiModeReceiptId().getValue());
				}
				reconsiderRODRequestTableDTO.setApprovedAmt(ompReimbursement.getTotalPayableAmtUsd().doubleValue());
				if(ompReimbursement.getStatus()!=null){
					reconsiderRODRequestTableDTO.setRodStatus(ompReimbursement.getStatus().getProcessValue());
				}
				reconsiderRodRequestList.add(reconsiderRODRequestTableDTO);
			}
			claimProcessorDTO.setReconsiderRodRequestList(reconsiderRodRequestList);*/
		}
		
		List<OMPReimbursement> docuList = ompIntimationService.getDocumentRecDetailsByClaimKey(claimsByIntimationNumber.getKey(),SHAConstants.DOC_RECEIVED_FROM_HOSPITAL);
		if(docuList!=null && docuList.size()>=1){
				claimProcessorDTO.setIsDocRecHospital(Boolean.TRUE);
		}		
		List<OMPReimbursement> insuredList = ompIntimationService.getDocumentRecDetailsByClaimKey(claimsByIntimationNumber.getKey(),SHAConstants.DOC_RECEIVED_FROM_INSURED);
		if(insuredList!=null && insuredList.size()>=1){
				claimProcessorDTO.setIsDocRecInsured(Boolean.TRUE);
		}
		
		
		NewIntimationDto newIntimationDto = ompIntimationService.getIntimationDto(claimsByIntimationNumber.getIntimation());
		claimProcessorDTO.setNewIntimationDto(newIntimationDto);
		claimProcessorDTO.setIntimationId(newIntimationDto.getIntimationId());
		ClaimDto claimDTO = OMPClaimMapper.getInstance().getClaimDto(claimsByIntimationNumber);
		claimProcessorDTO.setClaimDto(claimDTO);
		claimProcessorDTO.setUserName(userName);
		String generateRODNumber = generateRODNumber(claimsByIntimationNumber.getKey(), intimationNo);
		if(generateRODNumber!=null){
			claimProcessorDTO.setRodNumber(generateRODNumber);
		}
		claimProcessorDTO.setAilmentLoss(claimDTO.getAilmentLoss());
		claimProcessorDTO.setLossDate(newIntimationDto.getLossDateTime());
		claimProcessorDTO.setHospName(claimDTO.getHospitalName());
		claimProcessorDTO.setHospCity(claimDTO.getCityName());
		if(claimsByIntimationNumber.getCountryId()!=null){
			SelectValue countryValueByKey = masterService.getCountryValueByKey(claimsByIntimationNumber.getCountryId());
			claimProcessorDTO.setHospCountry(countryValueByKey);
		}
		claimProcessorDTO.setPlaceOfVisit(newIntimationDto.getPlaceVisit());
		claimProcessorDTO.setClaimTypeValue(newIntimationDto.getClaimType());
		
			/*if (claimProcessorDTO.getClaimTypeValue() != null) {
				SelectValue cmbclaimType = claimProcessorDTO
						.getClaimTypeValue();
				if (cmbclaimType.getValue() != null) {
					if (ReferenceTable.CASHLESS_CLAIM
							.equalsIgnoreCase(cmbclaimType.getValue())) {
						claimProcessorDTO.setClaimType(Boolean.TRUE);
					} else {
						claimProcessorDTO.setClaimType(Boolean.FALSE);
					}
				}
			}*/
		if(claimsByIntimationNumber.getClaimType()!=null){
			MastersValue claimType = claimsByIntimationNumber.getClaimType();
		if (claimType.getValue() != null) {
			if (ReferenceTable.CASHLESS_CLAIM
					.equalsIgnoreCase(claimType.getValue())) {
				claimProcessorDTO.setClaimType(Boolean.TRUE);
			} else {
				claimProcessorDTO.setClaimType(Boolean.FALSE);
			}
		}
		}
			claimProcessorDTO.setHospitalisationFlag(claimDTO
					.getHospitalisationFlag());
			claimProcessorDTO.setNonHospitalisationFlag(claimDTO
					.getNonHospitalisationFlag());
			if (claimProcessorDTO.getHospitalisationFlag() != null) {
				if (SHAConstants.YES_FLAG.equalsIgnoreCase(claimProcessorDTO
						.getHospitalisationFlag())) {
					claimProcessorDTO.setHospTypeBooleanval(Boolean.TRUE);
				}else{
					claimProcessorDTO.setHospTypeBooleanval(Boolean.FALSE);
				}
			}
		
		MastersEvents event = claimsByIntimationNumber.getEvent();
		SelectValue eventCodeMast = new SelectValue();
		if(event!=null){
			String eventCode = event.getEventCode();
			//Double balanceSI = reimbursementService.getBalanceSI(reimbursement,claimsByIntimationNumber,eventCode);
			//System.out.println(balanceSI);
			//claimProcessorDTO.setBalanceSI(balanceSI);
			eventCodeMast.setId(event.getKey());
			eventCodeMast.setValue(event.getEventDescription());
			claimProcessorDTO.setEventCode(eventCodeMast);
		}
		
//		if(reimbursement.getDelayHR() != null){
//			
//			claimProcessorDTO.setDelayHrs(Long.valueOf(reimbursement.getDelayHR()));
//		}
//			if(reimbursement.getDocumentTypeId() != null){
//				SelectValue docType = new SelectValue();
//				docType.setId(reimbursement.getDocumentTypeId().getKey());
//				docType.setValue(reimbursement.getDocumentTypeId().getValue());
//				claimProcessorDTO.setDocType(docType);
//		}
//			if(reimbursement.getSubClassificationId() != null){
//				SelectValue subclass = new SelectValue();
//				subclass.setId(reimbursement.getSubClassificationId().getKey());
//				subclass.setValue(reimbursement.getSubClassificationId().getValue());
//				claimProcessorDTO.setSubClassification(subclass);
//		}
//			if(reimbursement.getClassificationId() != null){
//				SelectValue classif = new SelectValue();
//				classif.setId(reimbursement.getClassificationId().getKey());
//				classif.setValue(reimbursement.getClassificationId().getValue());
//				claimProcessorDTO.setClassification(classif);	
//		}
//		claimProcessorDTO.setEmailId(reimbursement.getEmailId());
//		MastersValue modeRecept = reimbursement.getClassiModeReceiptId();
//		SelectValue modeReceptMast = new SelectValue();
//			if(modeRecept != null){
//			modeReceptMast.setId(modeRecept.getKey());
//			modeReceptMast.setValue(modeRecept.getValue());						
//		}
//		claimProcessorDTO.setModeOfReceipt(modeReceptMast);
//		claimProcessorDTO.setPayeeNameStr(claimPayment.getPayeeName());
//		claimProcessorDTO.setPayableAt(claimPayment.getPayableAt());
//		claimProcessorDTO.setPanNo(claimPayment.getPanNumber());
//		
//		MastersValue paymode = claimPayment.getPaymentType();
//		SelectValue paymodeMast = new SelectValue();
//		if(paymode != null){
//			paymodeMast.setId(paymode.getKey());
//			paymodeMast.setValue(paymode.getValue());						
//		}
//	
//		claimProcessorDTO.setPayMode(paymodeMast);
//		
//		MastersValue payment = claimPayment.getPaymentStatus();
//		SelectValue paymentToMast = new SelectValue();
//		if(payment != null){
//			paymentToMast.setId(payment.getKey());
//			paymentToMast.setValue(payment.getValue());						
//		}
//		
//		claimProcessorDTO.setPaymentTo(paymentToMast);
//		claimProcessorDTO.setReasonForReconsider(reimbursement.getReasonReconsideration());
//		claimProcessorDTO.setDoctorName(reimbursement.getClassiSubDoctorName());
//		claimProcessorDTO.setInvestigatorName(reimbursement.getClassiInvestigatorName());
//		claimProcessorDTO.setAdvocateName(reimbursement.getClassiAdvocateName());
//		claimProcessorDTO.setAuditorName(reimbursement.getClassiAuditorName());
//	
////		claimProcessorDTO.setNegotiatorName(reimbursement.getNegotiatorName());
//		
//		MastersValue docReceivedFrm = reimbursement.getClassiDocumentRecivedFmId();
//		SelectValue docReceivedFrmMast = new SelectValue();
//		if(docReceivedFrm != null){
//			docReceivedFrmMast.setId(docReceivedFrm.getKey());
//			docReceivedFrmMast.setValue(docReceivedFrm.getValue());						
//	}
//		claimProcessorDTO.setDocumentsReceivedFrom(docReceivedFrmMast); 
//		claimProcessorDTO.setDocumentsReceivedDate(reimbursement.getDocumentRecivedDate());
		
		BeanItemContainer<SelectValue> classification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CLASSIFICATION);
		BeanItemContainer<SelectValue> subClassification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_SUB_CLASSIFICATION);
		BeanItemContainer<SelectValue> paymentTo = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_OMP_PAYTO);
		BeanItemContainer<SelectValue> paymentMode = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_PAY_MODE);
		BeanItemContainer<SelectValue> eventCode = masterService.getListMasterEventByProduct(productByProductCode.getKey());
		BeanItemContainer<SelectValue> currencyValue = masterService.getListMasterCurrencyValue();
		BeanItemContainer<SelectValue> negotiatorName = masterService.getNegotiationNamesAll();
		BeanItemContainer<SelectValue> modeOfReciept = masterService.getListMasterValuebyTypeCode(ReferenceTable.CLAIM_TYPE);
		BeanItemContainer<SelectValue> documentRecievedFrom = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_RECFRM);
		BeanItemContainer<SelectValue> documentType = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_TYPE);
		BeanItemContainer<SelectValue> country = masterService.getCountryValue();
		claimProcessorDTO.getDocumentDetails().setDocumentCheckList(
				setDocumentCheckListTableValues());
		//claimProcessorDTO.setEventCode(newIntimationDto.getEventCodeValue());
		view.setOmpRODBillEntryview(OMPProcessRODBillEntryPageWizard.class,claimProcessorDTO, classification, subClassification,paymentTo,
				paymentMode,eventCode,currencyValue,negotiatorName,modeOfReciept,documentRecievedFrom,documentType,country);
		}
	}
	
	private List<DocumentCheckListDTO> setDocumentCheckListTableValues() {

		return ackDocReceivedService.getDocumentCheckListValues(masterService);
	}
	
	protected void showOmpProcessorApprover(
			@Observes @CDIEvent(OMPMenuPresenter.OMP_PROCESS_APPROVER_DETAIL_PAGE) final ParameterDTO parameters) {
		OMPProcessOmpClaimApproverTableDTO dto = (OMPProcessOmpClaimApproverTableDTO) parameters.getPrimaryParameter();
		Long rodkey = dto.getRodKey();
		String intimationNo = dto.getIntimationNo();
		String userName =dto.getUserName();
		OMPClaimProcessorDTO claimProcessorDTO = new OMPClaimProcessorDTO();
		claimProcessorDTO.setDbOutArray(dto.getDbOutArray());
		OMPClaim claimsByIntimationNumber = ompClaimService.getClaimsByIntimationNumber(intimationNo);
		if(claimsByIntimationNumber.getHospital()!=null){/*
			OMPHospitals hospitalDetailsBykey = ompIntimationService.getHospitalDetailsBykey(claimsByIntimationNumber.getHospital());
			if(hospitalDetailsBykey!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(hospitalDetailsBykey.getKey());
				selectValue.setValue(hospitalDetailsBykey.getName());
				claimProcessorDTO.setHospital(selectValue);
			}
		*/}
		
		NewIntimationDto newIntimationDto = ompIntimationService
				.getIntimationDto(claimsByIntimationNumber.getIntimation());
		
		setClaimDetails(rodkey, userName, claimProcessorDTO,claimsByIntimationNumber, newIntimationDto);
		
		
		List<OMPReimbursement> rembursementByClaimKey = ompIntimationService.getRembursementDetailsByClaimKey(claimsByIntimationNumber.getKey());
			List<OMPClaimCalculationViewTableDTO> claimcalculationDto = new ArrayList<OMPClaimCalculationViewTableDTO>();
			if(rembursementByClaimKey != null && rembursementByClaimKey.size()>0){
			for (OMPReimbursement ompreimbursement : rembursementByClaimKey) {
				OMPClaimCalculationViewTableDTO claimCalulation = null ;
				if(ompreimbursement.getViewForApprover()!= null && ompreimbursement.getViewForApprover().equalsIgnoreCase("Y")
						||ompreimbursement.getRejectFlg()!= null && ompreimbursement.getRejectFlg().equalsIgnoreCase("Y")){
					claimProcessorDTO.setDescription("Approve");
					claimCalulation = new OMPClaimCalculationViewTableDTO();
					setReimbursementDetails(claimProcessorDTO, claimCalulation,ompreimbursement);
					
					if(claimCalulation.getReject() != null){
						if(claimCalulation.getReject().equalsIgnoreCase("Y")){
							claimCalulation.setProcessorRejectedClaim(true);
						}else{
							claimCalulation.setProcessorRejectedClaim(false);
						}
					}
				}
				
				if(rembursementByClaimKey!=null && rembursementByClaimKey.size()>1){
					claimProcessorDTO.setIsMutipleRod(Boolean.TRUE);
				}
				if(rembursementByClaimKey!=null && rembursementByClaimKey.size()==1 
						&& ompreimbursement.getSendForApproverFlg()!=null && ompreimbursement.getSendForApproverFlg().equalsIgnoreCase("Y")){
						claimProcessorDTO.setIsMutipleRod(Boolean.TRUE);
				}
				if(claimCalulation!=null){
					claimcalculationDto.add(claimCalulation);
				}
				
				MastersEvents event = claimsByIntimationNumber.getEvent();
				if(event!=null && ompreimbursement!= null){
					String eventCode = event.getEventCode();
					Double balanceSI = reimbursementService.getBalanceSI(ompreimbursement,claimsByIntimationNumber,eventCode);
//					System.out.println(balanceSI);
					claimProcessorDTO.setBalanceSI(balanceSI);
					OMPIntimation intimation = ompreimbursement.getClaim().getIntimation();
					Policy policy = ompreimbursement.getClaim().getIntimation().getPolicy();
					Product product = policy.getProduct();
					String plan = intimation.getInsured().getPlan();
					Double insuredSumInsured = policy.getTotalSumInsured();
					Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(policy.getKey(), eventCode);
					Map<String, Double> ompDeductible = dbCalculationService.getOmpDeductible(product.getKey(), plan, sumInsured, event.getEventCode(),insuredSumInsured);
					Double deductibles = (Double)ompDeductible.get(SHAConstants.DEDUCTIBLES);
					if(ompreimbursement.getDeductbleUsd()!=null){
						if(claimCalulation!=null){
							claimCalulation.setDeduction(ompreimbursement.getDeductbleUsd().doubleValue());
							claimCalulation.setDeductiblesOriginal(ompreimbursement.getDeductbleUsd().doubleValue());
						}
						claimProcessorDTO.setDeductibles(ompreimbursement.getDeductbleUsd().doubleValue());
					}else{
						if(claimCalulation!=null){
							claimCalulation.setDeduction(deductibles);
							claimCalulation.setDeductiblesOriginal(deductibles);
							claimProcessorDTO.setDeductiblesOriginal(deductibles);
						}
						claimProcessorDTO.setDeductibles(deductibles);
						claimProcessorDTO.setDeductiblesOriginal(deductibles);
					}
					claimProcessorDTO.setProductCode(product.getCode());
					claimProcessorDTO.setSumInsured(balanceSI);
				}
				//IMSSUPPOR-29233 comented
//				claimCalulation.setLateDocReceivedDate(ompreimbursement.getLastDocRecDate());
				if(ompreimbursement.getStatus() != null){
					claimCalulation.setProcessorStatus(ompreimbursement.getStatus().getKey());
				}
				/*OMPClaimPayment clmPayment = ompIntimationService.getRODPaymentStatus(ompreimbursement.getRodNumber());
				if(clmPayment != null){
					claimCalulation.setRodPaymentStatus(clmPayment.getStatusId().getKey());
				}else{
					claimCalulation.setRodPaymentStatus(null);
				}*/
				if(claimCalulation != null){
					MastersValue docReceivedFrm = ompreimbursement.getClassiDocumentRecivedFmId();
					SelectValue docReceivedFrmMast = new SelectValue();
					if(docReceivedFrm != null){
						docReceivedFrmMast.setId(docReceivedFrm.getKey());
						docReceivedFrmMast.setValue(docReceivedFrm.getValue());
					}
					claimCalulation.setDocRecivedFrm(docReceivedFrmMast);
					if(ompreimbursement.getGenerateDischargeVoucherFlag() != null){
						if(ompreimbursement.getGenerateDischargeVoucherFlag().equals("Y")){
							claimCalulation.setGenDisVoucherFlag(true);
						}else{
							claimCalulation.setGenDisVoucherFlag(false);
						}
					}else{
						claimCalulation.setGenDisVoucherFlag(false);
					}
					if(!StringUtils.isBlank(ompreimbursement.getDischargeVoucherRemarks())){
						claimCalulation.setDisVoucherRemarks(ompreimbursement.getDischargeVoucherRemarks());
					}

					if(ompreimbursement.getGenerateCoveringLetterFlag() != null){
						if(ompreimbursement.getGenerateCoveringLetterFlag().equals("Y")){
							claimCalulation.setGenCoveringLetterFlag(true);
						}else{
							claimCalulation.setGenCoveringLetterFlag(false);
						}
					}else{
						claimCalulation.setGenCoveringLetterFlag(false);
					}
					if(claimCalulation.getProcessorStatus() != null){
						if(claimCalulation.getProcessorStatus().intValue() == 3221){
							claimCalulation.setDvReceivedDate(ompreimbursement.getDvReceivedDate());
						}
					}
					claimCalulation.setDisVoucFlag(ompreimbursement.getDischargeFlag());
					if(!StringUtils.isBlank(ompreimbursement.getNomineeName())){
						claimCalulation.setNomineeName(ompreimbursement.getNomineeName());
						claimCalulation.setNomineeAddress(ompreimbursement.getNomineeAddress());
					}
				}
			}
			claimProcessorDTO.setClaimCalculationViewTable(claimcalculationDto);
		}else{
			Long policyKey = claimsByIntimationNumber.getIntimation().getPolicy().getKey();
			Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(policyKey, claimsByIntimationNumber.getEvent().getEventCode());
			Map balanceSIMap = dbCalculationService.getOmpBalanceSI(policyKey , claimsByIntimationNumber.getIntimation().getInsured().getKey() , 
					claimsByIntimationNumber.getKey(),null, sumInsured,claimsByIntimationNumber.getIntimation().getKey(), claimsByIntimationNumber.getEvent().getEventCode());
			Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
			claimProcessorDTO.setSumInsured(balanceSI);
			claimProcessorDTO.setBalanceSI(balanceSI);
		}
		
		List<OMPNegotiation> ompNegotiation1 = ompRodBillEntryService.getOMPNegotiation(rodkey);
		if(ompNegotiation1!=null){
			OMPNegotiation ompNegotiation2 = ompNegotiation1.get(0);
			claimProcessorDTO.setAgreedAmt(ompNegotiation2.getAggredAmount());
		}
		
		String productCode = claimsByIntimationNumber.getProductCode();
		Product productByProductCode = masterService.getProductByProductCode(productCode);
		
		
		BeanItemContainer<SelectValue> classification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CLASSIFICATION);
		BeanItemContainer<SelectValue> subClassification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_SUB_CLASSIFICATION);
		BeanItemContainer<SelectValue> paymentTo = masterService.getMastersValuebyTypeCodeOnStaatus(SHAConstants.OMP_OMP_PAYTO);
		BeanItemContainer<SelectValue> paymentMode = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_PAY_MODE);
		BeanItemContainer<SelectValue> eventCode = masterService.getListMasterEventByProduct(productByProductCode.getKey());
		BeanItemContainer<SelectValue> currencyValue = masterService.getListMasterCurrencyValue();
		BeanItemContainer<SelectValue> negotiatorName = masterService.getNegotiationNamesAll();
		BeanItemContainer<SelectValue> rodClaimTypeContainer = masterService.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
		BeanItemContainer<SelectValue> documentRecievedFrom = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_RECFRM);
		BeanItemContainer<SelectValue> documentType = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_TYPE);
		BeanItemContainer<SelectValue> country = masterService.getCountryValue();
		
		claimProcessorDTO.setClassificationContainer(classification);
		claimProcessorDTO.setSubClassificationContainer(subClassification);
		claimProcessorDTO.setCurrencyValueContainer(currencyValue);
		claimProcessorDTO.setDocumentRecievedFromContainer(documentRecievedFrom);
		claimProcessorDTO.setPaymentToContainer(paymentTo);
		claimProcessorDTO.setPaymentModeContainer(paymentMode);
		claimProcessorDTO.setRodClaimTypeContainer(rodClaimTypeContainer);
		claimProcessorDTO.setPolicy(claimsByIntimationNumber.getIntimation().getPolicy());
		claimProcessorDTO.getNewIntimationDto().setTpaIntimationNumber(claimsByIntimationNumber.getIntimation().getTpaIntimationNumber());
		claimProcessorDTO.setCgOption(claimsByIntimationNumber.getCgOption());
		claimProcessorDTO.setCgDate(claimsByIntimationNumber.getDateOfCashGuarantee());
		claimProcessorDTO.setCgApprovedAmt(claimsByIntimationNumber.getCgAmount());
		claimProcessorDTO.setCgRemarks(claimsByIntimationNumber.getCgRemarks());
		MastersValue masPatient = masterService.getMaster(claimsByIntimationNumber.getPatientStatusId());
		SelectValue patientSelectValue = new SelectValue();
		patientSelectValue.setId(masPatient.getKey());
		patientSelectValue.setValue(masPatient.getValue());
		claimProcessorDTO.setPatientStatus(patientSelectValue);
		claimProcessorDTO.setDeathDate(claimsByIntimationNumber.getDateOfDeath());
		view.setOmpProcessApproverView(OMPProcessOmpClaimApproverWizardView.class,claimProcessorDTO, classification, subClassification,paymentTo,
				paymentMode,eventCode,currencyValue,negotiatorName,rodClaimTypeContainer,documentRecievedFrom,documentType,country);
	}
	
	

	protected void showOmpClaimProcessor(
			@Observes @CDIEvent(OMPMenuPresenter.OMP_PROCESS_DETAIL_PAGE) final ParameterDTO parameters) {
		//PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		
		OMPProcessOmpClaimProcessorTableDTO dto = (OMPProcessOmpClaimProcessorTableDTO) parameters.getPrimaryParameter();
		Long rodkey = dto.getRodkey();
		String intimationNo = dto.getIntimationNo();
		String userName =dto.getUserName();
		OMPClaimProcessorDTO claimProcessorDTO = new OMPClaimProcessorDTO();
		claimProcessorDTO.setDbOutArray(dto.getDbOutArray());
		//OMPReimbursement reimbursement = ompIntimationService.getReimbursementByKey(rodkey);
		OMPClaim claimsByIntimationNumber = ompClaimService.getClaimsByIntimationNumber(intimationNo);
		
		if(claimsByIntimationNumber.getStatus()!= null){
			claimProcessorDTO.setStatusKey(claimsByIntimationNumber.getStatus().getKey());
		}
		NewIntimationDto newIntimationDto = ompIntimationService
				.getIntimationDto(claimsByIntimationNumber.getIntimation());
		
		setClaimDetails(rodkey, userName, claimProcessorDTO,claimsByIntimationNumber, newIntimationDto);
		
		List<OMPClaimCalculationViewTableDTO> claimcalculationDto = new ArrayList<OMPClaimCalculationViewTableDTO>();
		List<OMPReimbursement> rembursementByClaimKey = ompIntimationService.getRembursementDetailsByClaimKey(claimsByIntimationNumber.getKey());
		List<OMPDocAcknowledgement> acknowledgeByClaimKey = ompIntimationService.getAcknowledgetDetailsByClaimKey(claimsByIntimationNumber.getKey());
		if(acknowledgeByClaimKey != null && acknowledgeByClaimKey.size()>0){
			for (OMPDocAcknowledgement ompAcknowledge : acknowledgeByClaimKey) {
				if(ompAcknowledge!=null && ompAcknowledge.getRodKey()==null){
					OMPClaimCalculationViewTableDTO claimCalulation = new OMPClaimCalculationViewTableDTO();
					setAknReimbursementDetails(claimProcessorDTO, claimCalulation,ompAcknowledge,claimsByIntimationNumber);
					//-----------------------------------------------------------------
					

					ReceiptOfDocumentsDTO receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
					List<UploadDocumentDTO> uploadDocsList = new ArrayList<UploadDocumentDTO>();
					List<OMPDocumentDetails> documentDetailsByRodNumber = ompRodBillEntryService.getDocumentsByAckNumber(ompAcknowledge.getAcknowledgeNumber());
					if(documentDetailsByRodNumber!=null){
					for (OMPDocumentDetails ompDocumentDetails : documentDetailsByRodNumber) {
						UploadDocumentDTO documentDTO = new UploadDocumentDTO();
						documentDTO.setRodNo(ompDocumentDetails.getReimbursementNumber());
						//documentDTO.setRodKey(ompreimbursement.getRodKey());
						documentDTO.setFileName(ompDocumentDetails.getFileName());
						documentDTO.setFileTypeValue(ompDocumentDetails.getFileType());
						if(ompDocumentDetails.getDocumentToken()!=null){
							documentDTO.setDmsDocToken(ompDocumentDetails.getDocumentToken().toString());
						}
						documentDTO.setDmsToken(ompDocumentDetails.getDocumentToken());
						documentDTO.setIntimationNo(intimationNo);
//						documentDTO.setRodKey(ompreimbursement.getRodKey());
//						documentDTO.setRodNo(ompreimbursement.getRodNumber());
						documentDTO.setDocumentTypeValue(ompDocumentDetails.getDocumentType());
						documentDTO.setDocReceivedDate(ompDocumentDetails.getDocRecievedDate());
						documentDTO.setReceivStatusValue(ompDocumentDetails.getRecievedStatus());
						documentDTO.setNoOfItems(ompDocumentDetails.getNoOfDoc());
						documentDTO.setRemarks(ompDocumentDetails.getRemarks());
						if(ompDocumentDetails.getCreatedBy() != null){
							String employeeName = "";
							String modifyEmployeeName = "";
							TmpEmployee employee =  masterService.getUserLoginDetail(ompDocumentDetails.getCreatedBy());
							if(employee != null){
								if(employee.getEmpFirstName() != null){
									employeeName = employeeName+employee.getEmpFirstName();
								}
								if(employee.getEmpMiddleName() != null){
									employeeName = employeeName+employee.getEmpMiddleName();
								}
								if(employee.getEmpLastName() != null){
									employeeName = employeeName+employee.getEmpLastName();
								}
							}
							modifyEmployeeName = modifyEmployeeName+ompDocumentDetails.getCreatedBy()+" - "+employeeName;
							documentDTO.setUploadedBy(modifyEmployeeName);
						}
						if(ompDocumentDetails.getCreatedDate()!=null){
							String dateWithoutTime = SHAUtils.formateDateForHistory(ompDocumentDetails.getCreatedDate());
							documentDTO.setUpdatedOn(dateWithoutTime);
						}
						documentDTO.setAckDocumentSource(ompDocumentDetails.getDocumentSource());
						documentDTO.setAcknowledgementNo(ompDocumentDetails.getAcknowledgementNumber());
						uploadDocsList.add(documentDTO);
					}}
					receiptOfDocumentsDTO.setUploadDocsList(uploadDocsList);
					claimCalulation.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
				
					//----------------------------------------------------------------------------
					
					claimcalculationDto.add(claimCalulation);
					claimProcessorDTO.setClaimCalculationViewTable(claimcalculationDto);
//					add for after create acknowledge first time
					Long policyKey = claimsByIntimationNumber.getIntimation().getPolicy().getKey();
					Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(policyKey, claimsByIntimationNumber.getEvent().getEventCode());
					Map balanceSIMap = dbCalculationService.getOmpBalanceSI(policyKey , claimsByIntimationNumber.getIntimation().getInsured().getKey() , 
							claimsByIntimationNumber.getKey(),null, sumInsured,claimsByIntimationNumber.getIntimation().getKey(), claimsByIntimationNumber.getEvent().getEventCode());
					Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
					claimProcessorDTO.setSumInsured(balanceSI);
					claimProcessorDTO.setBalanceSI(balanceSI);
					
					//
					MastersEvents event = claimsByIntimationNumber.getEvent();
						String eventCode = event.getEventCode();
					OMPIntimation intimation = claimsByIntimationNumber.getIntimation();
					Policy policy = claimsByIntimationNumber.getIntimation().getPolicy();
					Product product = policy.getProduct();
					String plan = intimation.getInsured().getPlan();
					Double insuredSumInsured = policy.getTotalSumInsured();
					Map<String, Double> ompDeductible = dbCalculationService.getOmpDeductible(product.getKey(), plan, sumInsured, eventCode,insuredSumInsured);
					Double deductibles = (Double)ompDeductible.get(SHAConstants.DEDUCTIBLES);
					claimProcessorDTO.setDeductibles(deductibles);
					claimCalulation.setDeduction(deductibles);
					claimProcessorDTO.setDeductibles(deductibles);
					claimProcessorDTO.setDeductiblesOriginal(deductibles);
					
					//
				
				}else{
					if(ompAcknowledge!=null &&ompAcknowledge.getRodKey()!=null){
						OMPReimbursement ompreimbursement = ompIntimationService.getReimbursementByKey(ompAcknowledge.getRodKey());
					
			if(ompreimbursement != null ){
//			for (OMPReimbursement ompreimbursement : rembursementByClaimKey) {
				OMPClaimCalculationViewTableDTO claimCalulation = new OMPClaimCalculationViewTableDTO();
				setReimbursementDetails(claimProcessorDTO, claimCalulation,ompreimbursement);
				if(rembursementByClaimKey!=null && rembursementByClaimKey.size()>1){
						claimProcessorDTO.setIsMutipleRod(Boolean.TRUE);
				}
				if(rembursementByClaimKey!=null && rembursementByClaimKey.size()==1 
						&& ompreimbursement.getSendForApproverFlg()!=null && ompreimbursement.getSendForApproverFlg().equalsIgnoreCase("Y")){
						claimProcessorDTO.setIsMutipleRod(Boolean.TRUE);
				}
				MastersEvents event = claimsByIntimationNumber.getEvent();
				if(event!=null && ompreimbursement!= null){
					String eventCode = event.getEventCode();
					Double balanceSI = reimbursementService.getBalanceSI(ompreimbursement,claimsByIntimationNumber,eventCode);
//					System.out.println(balanceSI);
					claimProcessorDTO.setBalanceSI(balanceSI);
					OMPIntimation intimation = ompreimbursement.getClaim().getIntimation();
					Policy policy = ompreimbursement.getClaim().getIntimation().getPolicy();
					Product product = policy.getProduct();
					String plan = intimation.getInsured().getPlan();
					Double insuredSumInsured = policy.getTotalSumInsured();
					Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(policy.getKey(), eventCode);
					Map<String, Double> ompDeductible = dbCalculationService.getOmpDeductible(product.getKey(), plan, sumInsured, event.getEventCode(),insuredSumInsured);
					Double deductibles = (Double)ompDeductible.get(SHAConstants.DEDUCTIBLES);
					if(ompreimbursement.getDeductbleUsd()!=null){
						claimCalulation.setDeduction(ompreimbursement.getDeductbleUsd().doubleValue());
						claimProcessorDTO.setDeductibles(ompreimbursement.getDeductbleUsd().doubleValue());
						claimCalulation.setDeductiblesOriginal(ompreimbursement.getDeductbleUsd().doubleValue());
					}else{
						claimCalulation.setDeduction(deductibles);
						claimProcessorDTO.setDeductibles(deductibles);
						claimProcessorDTO.setDeductiblesOriginal(deductibles);
					}
					claimProcessorDTO.setProductCode(product.getCode());
					claimProcessorDTO.setSumInsured(balanceSI);
				}
				if(ompreimbursement.getStatus() != null){
					claimCalulation.setProcessorStatus(ompreimbursement.getStatus().getKey());
				}
				MastersValue docReceivedFrm = ompreimbursement.getClassiDocumentRecivedFmId();
				SelectValue docReceivedFrmMast = new SelectValue();
				if(docReceivedFrm != null){
					docReceivedFrmMast.setId(docReceivedFrm.getKey());
					docReceivedFrmMast.setValue(docReceivedFrm.getValue());				
				}
				claimCalulation.setDocRecivedFrm(docReceivedFrmMast);
				
				claimcalculationDto.add(claimCalulation);
			}
			claimProcessorDTO.setClaimCalculationViewTable(claimcalculationDto);
		}else{
			Long policyKey = claimsByIntimationNumber.getIntimation().getPolicy().getKey();
			Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(policyKey, claimsByIntimationNumber.getEvent().getEventCode());
			Map balanceSIMap = dbCalculationService.getOmpBalanceSI(policyKey , claimsByIntimationNumber.getIntimation().getInsured().getKey() , 
					claimsByIntimationNumber.getKey(),null, sumInsured,claimsByIntimationNumber.getIntimation().getKey(), claimsByIntimationNumber.getEvent().getEventCode());
			Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
			claimProcessorDTO.setSumInsured(balanceSI);
			claimProcessorDTO.setBalanceSI(balanceSI);
		}
		
		List<OMPNegotiation> ompNegotiation1 = ompRodBillEntryService.getOMPNegotiation(rodkey);
		if(ompNegotiation1!=null){
			OMPNegotiation ompNegotiation2 = ompNegotiation1.get(0);
			claimProcessorDTO.setAgreedAmt(ompNegotiation2.getAggredAmount());
		}
		
				}
			}
		}
		
		String productCode = claimsByIntimationNumber.getProductCode();
		Product productByProductCode = masterService.getProductByProductCode(productCode);
		
		
		BeanItemContainer<SelectValue> classification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CLASSIFICATION);
		BeanItemContainer<SelectValue> subClassification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_SUB_CLASSIFICATION);
		BeanItemContainer<SelectValue> paymentTo = masterService.getMastersValuebyTypeCodeOnStaatus(SHAConstants.OMP_OMP_PAYTO);
		BeanItemContainer<SelectValue> paymentMode = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_PAY_MODE);
		BeanItemContainer<SelectValue> eventCode = masterService.getListMasterEventByProduct(productByProductCode.getKey());
		BeanItemContainer<SelectValue> currencyValue = masterService.getListMasterCurrencyValue();
		BeanItemContainer<SelectValue> negotiatorName = masterService.getNegotiationNamesAll();
		BeanItemContainer<SelectValue> rodClaimTypeContainer = masterService.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
		BeanItemContainer<SelectValue> documentRecievedFrom = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_RECFRM);
		BeanItemContainer<SelectValue> documentType = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_TYPE);
		BeanItemContainer<SelectValue> country = masterService.getCountryValue();
		
		claimProcessorDTO.setClassificationContainer(classification);
		claimProcessorDTO.setSubClassificationContainer(subClassification);
		claimProcessorDTO.setCurrencyValueContainer(currencyValue);
		claimProcessorDTO.setDocumentRecievedFromContainer(documentRecievedFrom);
		claimProcessorDTO.setPaymentToContainer(paymentTo);
		claimProcessorDTO.setPaymentModeContainer(paymentMode);
		claimProcessorDTO.setRodClaimTypeContainer(rodClaimTypeContainer);
		claimProcessorDTO.setPolicy(claimsByIntimationNumber.getIntimation().getPolicy());
		claimProcessorDTO.getNewIntimationDto().setTpaIntimationNumber(claimsByIntimationNumber.getIntimation().getTpaIntimationNumber());
		claimProcessorDTO.setCgOption(claimsByIntimationNumber.getCgOption());
		claimProcessorDTO.setCgDate(claimsByIntimationNumber.getDateOfCashGuarantee());
		claimProcessorDTO.setCgApprovedAmt(claimsByIntimationNumber.getCgAmount());
		claimProcessorDTO.setCgRemarks(claimsByIntimationNumber.getCgRemarks());
		
		view.setOmpClaimProcessorview(OMPProcessOmpClaimProcessorPageWizard.class,claimProcessorDTO, classification, subClassification,paymentTo,
				paymentMode,eventCode,currencyValue,negotiatorName,rodClaimTypeContainer,documentRecievedFrom,documentType,country);
	}

	private void setReimbursementDetails(
			OMPClaimProcessorDTO claimProcessorDTO,
			OMPClaimCalculationViewTableDTO claimCalulation,
			OMPReimbursement ompreimbursement) {
		OMPDocAcknowledgement acknowledgeByRodKey = ompIntimationService.getAcknowledgementByRodKey(ompreimbursement.getKey());
		ReceiptOfDocumentsDTO receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
		List<UploadDocumentDTO> uploadDocsList = new ArrayList<UploadDocumentDTO>();
		if(acknowledgeByRodKey!=null){
			claimCalulation.setAcknumber(acknowledgeByRodKey.getAcknowledgeNumber());
			claimCalulation.setAckKey(acknowledgeByRodKey.getKey());
			claimProcessorDTO.setAckKey(acknowledgeByRodKey.getKey());
			

			// Upload doc ACK
			
			if(acknowledgeByRodKey.getAcknowledgeNumber()!=null){
				List<OMPDocumentDetails> documentDetailsByRodNumber = ompRodBillEntryService.getDocumentsByAckNumber(acknowledgeByRodKey.getAcknowledgeNumber());
				if(documentDetailsByRodNumber!=null){
				for (OMPDocumentDetails ompDocumentDetails : documentDetailsByRodNumber) {
					UploadDocumentDTO documentDTO = new UploadDocumentDTO();
					documentDTO.setRodNo(ompDocumentDetails.getReimbursementNumber());
					documentDTO.setRodKey(acknowledgeByRodKey.getRodKey());
					documentDTO.setFileName(ompDocumentDetails.getFileName());
					documentDTO.setFileTypeValue(ompDocumentDetails.getFileType());
					if(ompDocumentDetails.getDocumentToken()!=null){
						documentDTO.setDmsDocToken(ompDocumentDetails.getDocumentToken().toString());
					}
					documentDTO.setDmsToken(ompDocumentDetails.getDocumentToken());
					documentDTO.setIntimationNo(ompreimbursement.getClaim().getIntimation().getIntimationId());
//					documentDTO.setRodKey(ompreimbursement.getRodKey());
//					documentDTO.setRodNo(ompreimbursement.getRodNumber());
					documentDTO.setDocumentTypeValue(ompDocumentDetails.getDocumentType());
					documentDTO.setDocReceivedDate(ompDocumentDetails.getDocRecievedDate());
					documentDTO.setReceivStatusValue(ompDocumentDetails.getRecievedStatus());
					documentDTO.setNoOfItems(ompDocumentDetails.getNoOfDoc());
					documentDTO.setRemarks(ompDocumentDetails.getRemarks());
					if(ompDocumentDetails.getCreatedBy() != null){
						String employeeName = "";
						String modifyEmployeeName = "";
						TmpEmployee employee =  masterService.getUserLoginDetail(ompDocumentDetails.getCreatedBy());
						if(employee != null){
							if(employee.getEmpFirstName() != null){
								employeeName = employeeName+employee.getEmpFirstName();
							}
							if(employee.getEmpMiddleName() != null){
								employeeName = employeeName+employee.getEmpMiddleName();
							}
							if(employee.getEmpLastName() != null){
								employeeName = employeeName+employee.getEmpLastName();
							}
						}
						modifyEmployeeName = modifyEmployeeName+ompDocumentDetails.getCreatedBy()+" - "+employeeName;
						documentDTO.setUploadedBy(modifyEmployeeName);
					}
					if(ompDocumentDetails.getCreatedDate()!=null){
						String dateWithoutTime = SHAUtils.formateDateForHistory(ompDocumentDetails.getCreatedDate());
						documentDTO.setUpdatedOn(dateWithoutTime);
					}
					documentDTO.setAckDocumentSource(ompDocumentDetails.getDocumentSource());
					documentDTO.setAcknowledgementNo(ompDocumentDetails.getAcknowledgementNumber());
					uploadDocsList.add(documentDTO);
				}}
				receiptOfDocumentsDTO.setUploadDocsList(uploadDocsList);
				claimCalulation.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
			}
		}
		
		// Upload doc ROD
					if(ompreimbursement.getRodNumber()!=null){
						/*ReceiptOfDocumentsDTO receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
						List<UploadDocumentDTO> uploadDocsList = new ArrayList<UploadDocumentDTO>();*/
						List<OMPDocumentDetails> documentDetailsByRodNumber = ompRodBillEntryService.getDocumentDetailsByRodNumber(ompreimbursement.getRodNumber());
						if(documentDetailsByRodNumber!=null){
						for (OMPDocumentDetails ompDocumentDetails : documentDetailsByRodNumber) {
							UploadDocumentDTO documentDTO = new UploadDocumentDTO();
							documentDTO.setRodNo(ompDocumentDetails.getReimbursementNumber());
							documentDTO.setRodKey(ompreimbursement.getKey());
							documentDTO.setFileName(ompDocumentDetails.getFileName());
							documentDTO.setFileTypeValue(ompDocumentDetails.getFileType());
							if(ompDocumentDetails.getDocumentToken()!=null){
								documentDTO.setDmsDocToken(ompDocumentDetails.getDocumentToken().toString());
							}
							documentDTO.setDmsToken(ompDocumentDetails.getDocumentToken());
							documentDTO.setIntimationNo(ompreimbursement.getClaim().getIntimation().getIntimationId());
							documentDTO.setRodKey(ompreimbursement.getKey());
							documentDTO.setRodNo(ompreimbursement.getRodNumber());
							documentDTO.setDocumentTypeValue(ompDocumentDetails.getDocumentType());
							documentDTO.setDocReceivedDate(ompDocumentDetails.getDocRecievedDate());
							documentDTO.setReceivStatusValue(ompDocumentDetails.getRecievedStatus());
							documentDTO.setNoOfItems(ompDocumentDetails.getNoOfDoc());
							documentDTO.setRemarks(ompDocumentDetails.getRemarks());
							
							if(ompDocumentDetails.getCreatedBy() != null){
								String employeeName = "";
								String modifyEmployeeName = "";
								TmpEmployee employee =  masterService.getUserLoginDetail(ompDocumentDetails.getCreatedBy());
								if(employee != null){
									if(employee.getEmpFirstName() != null){
										employeeName = employeeName+employee.getEmpFirstName();
									}
									if(employee.getEmpMiddleName() != null){
										employeeName = employeeName+employee.getEmpMiddleName();
									}
									if(employee.getEmpLastName() != null){
										employeeName = employeeName+employee.getEmpLastName();
									}
								}
								modifyEmployeeName = modifyEmployeeName+ompDocumentDetails.getCreatedBy()+" - "+employeeName;
								documentDTO.setUploadedBy(modifyEmployeeName);
							}
							/*if(ompDocumentDetails.getCreatedDate()!=null){
								String dateWithoutTime = SHAUtils.getDateWithoutTime(ompDocumentDetails.getCreatedDate());
								documentDTO.setUpdatedOn(dateWithoutTime);
							}*/
							if(ompDocumentDetails.getCreatedDate()!=null){
								String dateWithoutTime = SHAUtils.formateDateForHistory(ompDocumentDetails.getCreatedDate());
								documentDTO.setUpdatedOn(dateWithoutTime);
							}
							documentDTO.setAckDocumentSource(ompDocumentDetails.getDocumentSource());
							uploadDocsList.add(documentDTO);
						}}
						receiptOfDocumentsDTO.setUploadDocsList(uploadDocsList);
						claimCalulation.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
					}
					
		claimCalulation.setRodKey(ompreimbursement.getKey());
		if(ompreimbursement.getClassificationId()!= null && ompreimbursement.getClassificationId().getValue()!= null){
			MastersValue classValue = ompreimbursement.getClassificationId();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setClassification(seleCValue);
		}
		if(ompreimbursement.getSubClassificationId()!= null && ompreimbursement.getSubClassificationId().getValue()!= null){
			MastersValue classValue = ompreimbursement.getSubClassificationId();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setSubClassification(seleCValue);
		}
		if(ompreimbursement.getCategoryId()!= null && ompreimbursement.getCategoryId().getValue()!= null){
			MastersValue classValue = ompreimbursement.getCategoryId();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setCategory(seleCValue);
		}
		if(ompreimbursement.getClassiDocumentRecivedFmId()!= null && ompreimbursement.getClassiDocumentRecivedFmId().getValue()!= null){
			MastersValue docValue = ompreimbursement.getClassiDocumentRecivedFmId();
			SelectValue docRecValue = new SelectValue();
			docRecValue.setId(ompreimbursement.getClassiDocumentRecivedFmId().getKey());
			docRecValue.setValue(ompreimbursement.getClassiDocumentRecivedFmId().getValue());
			claimCalulation.setDocRecivedFrm(docRecValue);
		}
		if(ompreimbursement.getRodClaimType()!= null && ompreimbursement.getRodClaimType().getValue()!= null){
			MastersValue classValue = ompreimbursement.getRodClaimType();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setRodClaimType(seleCValue);
		}
		Currency currencyType = ompreimbursement.getCurrencyTypeId();
		if(currencyType!=null){
		Long currencyTypeId = currencyType.getKey();
		SelectValue currencyValue = new SelectValue();
		currencyValue.setId(currencyTypeId);
		currencyValue.setValue(currencyType.getCurrencyCode());
		claimCalulation.setCurrencyType(currencyValue);
		}
		/*Currency currency = rodBillentryService.getOMPCurrency(currencyTypeId);
		if(currency != null){
			reimbursement.setCurrencyTypeId(currency);
		}*/
		
		claimCalulation.setCurrencyrate(ompreimbursement.getInrTotalAmount());
		claimCalulation.setConversionValue(ompreimbursement.getInrConversionRate());
		if(ompreimbursement.getNegotiationDone()!= null){
			SelectValue negodone = new SelectValue();
			if(ompreimbursement.getNegotiationDone().equalsIgnoreCase("Y")){
				negodone.setId(1L);
				negodone.setValue("Yes");
			}else{
				negodone.setId(2L);
				negodone.setValue("No");
			}
			claimCalulation.setNegotiationDone(negodone);
		}
		if(ompreimbursement.getRejectFlg()!=null ){
			if(ompreimbursement.getRejectFlg().equalsIgnoreCase("Y")){
				claimCalulation.setIsreject(Boolean.TRUE);
				claimCalulation.setIsReadOnly(Boolean.TRUE);
				claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
			}else{
				claimCalulation.setIsreject(Boolean.FALSE);
			}
		}
		claimCalulation.setReject(ompreimbursement.getRejectFlg());
		claimCalulation.setRejectionRemarks(ompreimbursement.getRejectionRemarks());
		claimCalulation.setReconsiderFlag(ompreimbursement.getReconsiderFlag());
		List<SelectValue> selectedRejId = ompClaimService.getRejectionIdByRodKey(ompreimbursement.getKey());
		claimCalulation.setRejectionIds(selectedRejId);
		if(claimProcessorDTO.getDescription()!=null && claimProcessorDTO.getDescription().equalsIgnoreCase("Approve")){
			claimCalulation.setSendforApprover(ompreimbursement.getApprovedFlg());
		}else{
			claimCalulation.setSendforApprover(ompreimbursement.getSendForApproverFlg());
		}
		SelectValue appValue = new SelectValue();
		if(ompreimbursement.getViewForApprover()!= null && ompreimbursement.getViewForApprover().equalsIgnoreCase("Y")){
			    appValue.setId(0l);
				appValue.setValue("Yes");
				if(ompreimbursement.getSendForApproverFlg()!=null ){
					if(claimProcessorDTO.getDescription()!=null){
						if(!claimProcessorDTO.getDescription().equalsIgnoreCase("Approve")){
							if(ompreimbursement.getSendForApproverFlg().equalsIgnoreCase("Y")){
								claimCalulation.setIsReadOnly(Boolean.TRUE);
								claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
							}
						}
					}else{
						if(ompreimbursement.getSendForApproverFlg().equalsIgnoreCase("Y")){
							claimCalulation.setIsReadOnly(Boolean.TRUE);
							claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
						}
					}
			}else{
				if(claimProcessorDTO.getDescription()!=null){
					if(claimProcessorDTO.getDescription().equalsIgnoreCase("Approve")){
						claimCalulation.setIsReadOnly(Boolean.TRUE);
						claimCalulation.setIsReadOnlyRecoverable(Boolean.FALSE);
					}
				}
			}if(claimProcessorDTO.getDescription()!= null){
				if(claimProcessorDTO.getDescription().equalsIgnoreCase("Approve")){
					if(ompreimbursement.getFaSubmitFlg()!= null && ompreimbursement.getFaSubmitFlg().equalsIgnoreCase("Y")){
						claimCalulation.setIsReadOnly(Boolean.TRUE);
						claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
					}
					if(ompreimbursement.getApprovedFlg()!= null && ompreimbursement.getApprovedFlg().equalsIgnoreCase("Y")){
						claimCalulation.setIsReadOnly(Boolean.TRUE);
						claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
					}
					if(ompreimbursement.getSendForApproverFlg()!=null ){
						if(!ompreimbursement.getSendForApproverFlg().equalsIgnoreCase("Y")){
							claimCalulation.setIsReadOnly(Boolean.TRUE);
							claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
						}
					}else{
						claimCalulation.setIsReadOnly(Boolean.TRUE);
						claimCalulation.setIsReadOnlyRecoverable(Boolean.FALSE);
					}
				}
			}
			}else{
				appValue.setId(2L);
				appValue.setValue("No");
			}
			claimCalulation.setViewforApprover(appValue);
		/*if(ompreimbursement.getApprovedFlg()!= null && ompreimbursement.getApprovedFlg().equalsIgnoreCase("Y")){
			claimCalulation.setIsReadOnly(Boolean.TRUE);
		}else{
			claimCalulation.setIsReadOnly(Boolean.FALSE);
		}*/claimCalulation.setLateDocReceivedDate(ompreimbursement.getLastDocRecDate());
			
			claimCalulation.setFinalApprovedAmtDollor(ompreimbursement.getFinalApprovedAmtUsd());
			
		claimCalulation.setFinalApprovedAmtInr(ompreimbursement.getFinalApprovedAmtInr() != null ? Math.round(ompreimbursement.getFinalApprovedAmtInr()) : 0d);
		claimCalulation.setAmountInWords(SHAUtils.getParsedAmount(claimCalulation.getFinalApprovedAmtInr()));
		claimCalulation.setNotinClaimCount(ompreimbursement.getNotInClaimCountFlg());
		claimCalulation.setRodnumber(ompreimbursement.getRodNumber());
		claimCalulation.setAfternegotiation(ompreimbursement.getAmountAfterNego());
		claimCalulation.setProcessorRemarks(ompreimbursement.getRemarksProcessor());
		claimCalulation.setReasonForApproval(ompreimbursement.getReasonSuggestApprover());
		if(ompreimbursement.getRejectionCategoryId()!=null){
			SelectValue selectValue = new SelectValue();
			selectValue.setId(ompreimbursement.getRejectionCategoryId().getKey());
			selectValue.setValue(ompreimbursement.getRejectionCategoryId().getValue());
			claimCalulation.setReasonForRejectionRemarks(selectValue);
		}
		claimCalulation.setSubmit(ompreimbursement.getFaSubmitFlg());
		if(ompreimbursement.getNegotiationList()!=null){
			OMPNegotiation ompNegotiationbyKey = ompRodBillEntryService.getOMPNegotiationbyKey(ompreimbursement.getNegotiationList());
			if(ompNegotiationbyKey!=null){
				double total = ompNegotiationbyKey.getExpenseAmountusd() + ompNegotiationbyKey.getHandlingChargsUsd();
			SelectValue select = new SelectValue();
			select.setId(ompreimbursement.getNegotiationList());
			select.setValue(ompNegotiationbyKey.getRodKey().getRodNumber() +"-"+ total);
			claimCalulation.setSelect(select);
			}
		}				
		List<OMPNewRecoverableTableDto> recovList = new ArrayList<OMPNewRecoverableTableDto>();
		OMPNewRecoverableTableDto  recoverableDto = new OMPNewRecoverableTableDto();
		recoverableDto.setDateofRecovery(ompreimbursement.getRecoveredDate());
		if(ompreimbursement.getRecoveredAmountInr()!= null){
			double recoinr = ompreimbursement.getRecoveredAmountInr().doubleValue();
			recoverableDto.setAmountRecoveredInr(recoinr);
		}
		if(ompreimbursement.getRecoveredAmountUsd()!= null){
			double recoUsd = ompreimbursement.getRecoveredAmountUsd().doubleValue();
			recoverableDto.setAmountRecoveredUsd(recoUsd);
		}
		recoverableDto.setRodKey(ompreimbursement.getKey());
		recoverableDto.setRemarks(ompreimbursement.getRemarks());
		recoverableDto.setSendToAccounts(ompreimbursement.getSendToAccounts());
		if(ompreimbursement.getSendToAccounts()!=null && ompreimbursement.getSendToAccounts().equalsIgnoreCase("Y")){
			claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
		}
		recovList.add(recoverableDto);
		
		List<OMPPaymentDetailsTableDTO> paymentListDto = new ArrayList<OMPPaymentDetailsTableDTO>();
		
		OMPPaymentDetailsTableDTO paymentDto = new OMPPaymentDetailsTableDTO();
		BeanItemContainer<SelectValue> paymentTo = masterService.getMastersValuebyTypeCodeOnStaatus(SHAConstants.OMP_OMP_PAYTO);
		BeanItemContainer<SelectValue> paymentMode = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_PAY_MODE);
		BeanItemContainer<SelectValue> currencyValue = new BeanItemContainer<SelectValue>(SelectValue.class);
		BeanItemContainer<SelectValue> inrCurrencyValue = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Currency currencyTypeId =null;
		if(ompreimbursement.getPayeeCurrencyTypeId()!=null){
			currencyTypeId = ompreimbursement.getPayeeCurrencyTypeId();	
		}else{
			currencyTypeId = ompreimbursement.getCurrencyTypeId();	
		}
				Currency ompCurrency = ompRodBillEntryService.getOMPCurrency(107L);
				SelectValue selectValue1 = new SelectValue();
				selectValue1.setId(ompCurrency.getKey());
				selectValue1.setValue(ompCurrency.getCurrencyCode());
				currencyValue.addBean(selectValue1);
				
				
				Currency ompCurrency1 = ompRodBillEntryService.getOMPCurrency(177L);
				SelectValue selectValue3 = new SelectValue();
				selectValue3.setId(ompCurrency1.getKey());
				selectValue3.setValue(ompCurrency1.getCurrencyCode());
				inrCurrencyValue.addBean(selectValue3);
				//paymentDto.setCurrency(selectValue1);
				claimCalulation.setInrCurrencyValueContainer(inrCurrencyValue);
		
		if(currencyTypeId!=null){
			SelectValue selectValue = new SelectValue();
			selectValue.setId(currencyTypeId.getKey());
			selectValue.setValue(currencyTypeId.getCurrencyCode());
			currencyValue.addBean(selectValue);
			paymentDto.setCurrency(selectValue);
		}
		
		claimCalulation.setCurrencyValueContainer(currencyValue);
		claimCalulation.setPaymentToContainer(paymentTo);
		claimCalulation.setPaymentModeContainer(paymentMode);
		paymentDto.setPanNo(ompreimbursement.getPanNumber());
		paymentDto.setPayableAt(ompreimbursement.getPayableAt());
		paymentDto.setPayeeNameStr(ompreimbursement.getPayeeName());
		paymentDto.setEmailId(ompreimbursement.getEmailId());
		SelectValue paymentValue = null; 
		MastersValue paymentToMast = ompreimbursement.getPaymentTo();
		if(paymentToMast != null && paymentToMast.getKey() != null){
			paymentValue = new SelectValue();
			paymentValue.setId(paymentToMast.getKey());
			paymentValue.setValue(paymentToMast.getValue());
			paymentDto.setPaymentTo(paymentValue);
		}
		if(ompreimbursement.getPaymentModeId()!= null){
			
			MastersValue master = ompRodBillEntryService.getMaster(ompreimbursement.getPaymentModeId());
			SelectValue value = null;
			if(master!= null && master.getKey()!= null){
				value = new SelectValue();
				value.setId(master.getKey());
				value.setValue(master.getValue());
				paymentDto.setPayMode(value);
				
			}
		}
//				paymentDto.setPaymentStatus(ompreimbursement.getPatientStatus().getValue());
		paymentListDto.add(paymentDto);
			List<OMPBenefitCover> benefitCoverList = ompRodBillEntryService.getOMPBenefitCoverByKey(ompreimbursement.getKey());
			if(benefitCoverList != null){
				
				if(benefitCoverList.get(0)!=null){
					OMPBenefitCover benefitCover = benefitCoverList.get(0);
					claimCalulation.setKey(benefitCover.getKey());
					if(benefitCover.getRodKey() != null){
						
						claimCalulation.setRodKey(benefitCover.getRodKey().getKey());
					}
					claimCalulation.setBillAmt(benefitCover.getBillAmount());
					claimCalulation.setAgreedAmt(benefitCover.getAgreedAmtDollar());
					claimCalulation.setAmtIn(benefitCover.getNetAmt());
					claimCalulation.setApprovedAmt(benefitCover.getApprovedAmountDollor());
//							ompClaimCalculationViewTableDTO.setCategory(benefitCover.get);
					/*ompClaimCalculationViewTableDTO.setDifferenceAmt(benefitCover.getDiffAmtDollar());
					ompClaimCalculationViewTableDTO.setExpenses(benefitCover.getExpenesesDollar());
					ompClaimCalculationViewTableDTO.setHandlingCharges(benefitCover.getHandlingChargesDollar());
					ompClaimCalculationViewTableDTO.setNegotiationCapping(benefitCover.getNegoFeeCapping());
					ompClaimCalculationViewTableDTO.setNegotiationClaimed(benefitCover.getNegoFeesClaimedDollar());
					ompClaimCalculationViewTableDTO.setNegotiationPayable(benefitCover.getNegoFeesClaimedInr());
					ompClaimCalculationViewTableDTO.setTotalExp(benefitCover.getTotalExpenceDollar());*/
					claimCalulation.setTotalAmtInr(benefitCover.getTotalAmtInr());
					claimCalulation.setTotalAmt(benefitCover.getTotalAmtPayBleDollar());
					claimCalulation.setCopayamount(benefitCover.getCopayAmount());
					if(benefitCover.getCopayPercentage()!= null){
						SelectValue copayvValue = new SelectValue();
							if(benefitCover.getCopayPercentage()==1L){
								copayvValue.setValue("0");
							}else{
								copayvValue.setValue("30");
							}
							copayvValue.setId(benefitCover.getCopayPercentage());
							claimCalulation.setCopay(copayvValue);
					}
					claimCalulation.setApprovedamountaftecopay(benefitCover.getApprAmtAftrCopya());
					
				}
			}
			claimCalulation.setOmpRecoverableTableList(recovList);
			claimCalulation.setOmpPaymentDetailsList(paymentListDto);



			//Negotiation
			if(ompreimbursement.getKey()!=null){
					List<OMPNegotiation> ompNegotiation = ompRodBillEntryService.getOMPNegotiation(ompreimbursement.getKey());
					List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = new ArrayList<OMPNegotiationDetailsDTO>();
					if(ompNegotiation!=null){
						for (OMPNegotiation ompNegotiation2 : ompNegotiation) {
							OMPNegotiationDetailsDTO negotiationDetailsDTO = new OMPNegotiationDetailsDTO();
							negotiationDetailsDTO.setKey(ompNegotiation2.getKey());
							negotiationDetailsDTO.setAgreedAmount(ompNegotiation2.getAggredAmount());
							negotiationDetailsDTO.setApprovedAmt(ompNegotiation2.getApprovedAmount());
							negotiationDetailsDTO.setNameOfNegotiatior(ompNegotiation2.getNegotiatorName());
							negotiationDetailsDTO.setNegotiationCompletDate(ompNegotiation2.getNegotiationCompletedDate());
							negotiationDetailsDTO.setNegotiationRemarks(ompNegotiation2.getNegotiationRemarks());
							negotiationDetailsDTO.setNegotiationReqstDate(ompNegotiation2.getNegotiationRequestedDate());
							negotiationDetailsDTO.setDiffAmt(ompNegotiation2.getDiffAmountusd());
							negotiationDetailsDTO.setExpenseAmt(ompNegotiation2.getExpenseAmountusd());
							negotiationDetailsDTO.setHandlingCharges(ompNegotiation2.getHandlingChargsUsd());
							if(ompNegotiation2.getRodKey()!=null){
								negotiationDetailsDTO.setRodKey(ompNegotiation2.getRodKey().getKey());
							}
							if(ompNegotiation2.getClaim()!=null){
								negotiationDetailsDTO.setClaimKey(ompNegotiation2.getClaim().getKey());
							}
							negotiationDetailsDTOs.add(negotiationDetailsDTO);
						}
					}
					claimCalulation.setNegotiationDetailsDTOs(negotiationDetailsDTOs);
			}
			
			/*// Upload doc
			if(ompreimbursement.getRodNumber()!=null){
				ReceiptOfDocumentsDTO receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
				List<UploadDocumentDTO> uploadDocsList = new ArrayList<UploadDocumentDTO>();
				List<OMPDocumentDetails> documentDetailsByRodNumber = ompRodBillEntryService.getDocumentDetailsByRodNumber(ompreimbursement.getRodNumber());
				if(documentDetailsByRodNumber!=null){
				for (OMPDocumentDetails ompDocumentDetails : documentDetailsByRodNumber) {
					UploadDocumentDTO documentDTO = new UploadDocumentDTO();
					documentDTO.setRodNo(ompDocumentDetails.getReimbursementNumber());
					documentDTO.setRodKey(ompreimbursement.getKey());
					documentDTO.setFileName(ompDocumentDetails.getFileName());
					documentDTO.setFileTypeValue(ompDocumentDetails.getFileType());
					if(ompDocumentDetails.getDocumentToken()!=null){
						documentDTO.setDmsDocToken(ompDocumentDetails.getDocumentToken().toString());
					}
					documentDTO.setDmsToken(ompDocumentDetails.getDocumentToken());
					documentDTO.setIntimationNo(ompreimbursement.getClaim().getIntimation().getIntimationId());
					documentDTO.setRodKey(ompreimbursement.getKey());
					documentDTO.setRodNo(ompreimbursement.getRodNumber());
					documentDTO.setDocumentTypeValue(ompDocumentDetails.getDocumentType());
					documentDTO.setDocReceivedDate(ompDocumentDetails.getDocRecievedDate());
					documentDTO.setReceivStatusValue(ompDocumentDetails.getRecievedStatus());
					documentDTO.setNoOfItems(ompDocumentDetails.getNoOfDoc());
					documentDTO.setRemarks(ompDocumentDetails.getRemarks());
					if(ompDocumentDetails.getCreatedDate()!=null){
						String dateWithoutTime = SHAUtils.getDateWithoutTime(ompDocumentDetails.getCreatedDate());
						documentDTO.setUpdatedOn(dateWithoutTime);
					}
					documentDTO.setAckDocumentSource(ompDocumentDetails.getDocumentSource());
					uploadDocsList.add(documentDTO);
				}}
				receiptOfDocumentsDTO.setUploadDocsList(uploadDocsList);
				claimCalulation.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
			}
			*/
			claimProcessorDTO.setIsOnLoad(Boolean.TRUE);
			claimCalulation.setLateDocReceivedDate(ompreimbursement.getLastDocRecDate());
	}

	private void setClaimDetails(Long rodkey, String userName,OMPClaimProcessorDTO claimProcessorDTO,OMPClaim claimsByIntimationNumber,
			NewIntimationDto newIntimationDto) {
		claimProcessorDTO.setNewIntimationDto(newIntimationDto);
		claimProcessorDTO.setIntimationId(newIntimationDto.getIntimationId());
		ClaimDto claimDTO = OMPClaimMapper.getInstance().getClaimDto(claimsByIntimationNumber);
		if (claimsByIntimationNumber.getIntimation().getPolicy().getHomeOfficeCode() != null) {
			 List<MasOmbudsman> ombudsmanOfficeList = getOmbudsmanOffiAddrByPIOCode(claimsByIntimationNumber.getIntimation().getPolicy().getHomeOfficeCode());
			 if(ombudsmanOfficeList !=null && !ombudsmanOfficeList.isEmpty()){
				 claimDTO.setOmbudsManAddressList(ombudsmanOfficeList);
			 }
		 }
		claimProcessorDTO.setClaimDto(claimDTO);
		claimProcessorDTO.setRodKey(rodkey);
		claimProcessorDTO.setUserName(userName);
		claimProcessorDTO.setInrConversionRate(claimsByIntimationNumber.getInrConversionRate());
		claimProcessorDTO.setProvisionAmt(claimsByIntimationNumber.getDollarInitProvisionAmount());
		claimProcessorDTO.setInrtotal(claimsByIntimationNumber.getInrTotalAmount());
		claimProcessorDTO.setAdmissionDate(claimsByIntimationNumber.getDataOfAdmission());
		claimProcessorDTO.setDischargeDate(claimsByIntimationNumber.getDataOfDischarge());
		claimProcessorDTO.setPlaceEvent(claimsByIntimationNumber.getPlaceOfAccident());
		claimProcessorDTO.setLossTime(claimsByIntimationNumber.getLossTime());
		claimProcessorDTO.setLossOrDelay(claimsByIntimationNumber.getPlaveOfLossOrDelay());
		claimProcessorDTO.setPlaceOfVisit(claimsByIntimationNumber.getPlaveOfVisit());
		claimProcessorDTO.setPolicy(claimsByIntimationNumber.getIntimation().getPolicy());
		if(claimsByIntimationNumber.getLegalOpinionFlag()!= null&& claimsByIntimationNumber.getLegalOpinionFlag().equalsIgnoreCase("Y")){
			claimProcessorDTO.setIsLegalFlag(Boolean.TRUE);
		}else{
			claimProcessorDTO.setIsLegalFlag(Boolean.FALSE);
		}
		if(claimsByIntimationNumber.getHospital()!=null){
			OMPHospitals hospitalDetailsBykey = ompIntimationService.getHospitalDetailsBykey(claimsByIntimationNumber.getHospital());
			if(hospitalDetailsBykey!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(hospitalDetailsBykey.getKey());
				selectValue.setValue(hospitalDetailsBykey.getName());
				claimProcessorDTO.setHospital(selectValue);
			}
		}
		
		claimProcessorDTO.setLossDetails(claimsByIntimationNumber.getLossDetails());
		claimProcessorDTO.setAilmentLoss(claimsByIntimationNumber.getAilmentLoss());
		claimProcessorDTO.setLossOfDate(claimsByIntimationNumber.getLossDateTime());
		claimProcessorDTO.setHospName(claimsByIntimationNumber.getHospitalName());
		claimProcessorDTO.setHospCity(claimsByIntimationNumber.getCityName());
		if(claimsByIntimationNumber.getCountryId()!=null){
			SelectValue countryValueByKey = masterService.getCountryValueByKey(claimsByIntimationNumber.getCountryId());
			claimProcessorDTO.setHospCountry(countryValueByKey);
		}
		if(claimsByIntimationNumber.getClaimType()!= null && claimsByIntimationNumber.getClaimType().getValue()!= null){
			MastersValue claimValue = claimsByIntimationNumber.getClaimType();
			SelectValue claimValue2 = new SelectValue();
			claimValue2.setId(claimValue.getKey());
			claimValue2.setValue(claimValue.getValue());
			claimProcessorDTO.setClaimTypeValue(claimValue2);
		}
		
		
		MastersEvents eventCode1 =claimsByIntimationNumber.getEvent();
		SelectValue eventCodeMast = new SelectValue();
		if(eventCode1 != null){
			eventCodeMast.setId(eventCode1.getKey());
			eventCodeMast.setValue(eventCode1.getEventDescription());
			claimProcessorDTO.setEventCode(eventCodeMast);
			claimProcessorDTO.setEventdescription(eventCode1.getEventDescription());
		}
		
		if(claimsByIntimationNumber.getClaimType()!=null){
			
			MastersValue cmbclaimType = claimsByIntimationNumber.getClaimType();
			SelectValue claimType = new SelectValue();
			claimType.setId(cmbclaimType.getKey());
			claimType.setValue(cmbclaimType.getValue());
			if(claimType!=null){
				if(ReferenceTable.CASHLESS_CLAIM.equalsIgnoreCase(claimType.getValue())){
//					optionClaimtype.setValue("Cashless");
					claimProcessorDTO.setClaimType(Boolean.TRUE);
					claimProcessorDTO.setClaimTypeValue(claimType);
					claimProcessorDTO.setIsCashless(Boolean.TRUE);
				}else{
//					optionClaimtype.setValue("Reimbursement");
					claimProcessorDTO.setClaimTypeValue(claimType);
					claimProcessorDTO.setClaimType(Boolean.FALSE);
					claimProcessorDTO.setIsCashless(Boolean.FALSE);
				}
			}
		}
		
		claimProcessorDTO.setNonHospitalisationFlag(claimsByIntimationNumber.getNonHospitalisationFlag());
		if(claimsByIntimationNumber.getNonHospitalisationFlag()!=null ){
			claimProcessorDTO.setHospTypeBooleanval(Boolean.TRUE);
		}
		if(claimsByIntimationNumber.getIntimation().getInsured()!=null){
			claimProcessorDTO.setPlan(claimsByIntimationNumber.getIntimation().getInsured().getPlan());
			claimProcessorDTO.setAge(claimsByIntimationNumber.getIntimation().getInsured().getInsuredAge());
		}
		if(claimsByIntimationNumber.getIntimation().getPolicy()!=null){
			Policy policy = claimsByIntimationNumber.getIntimation().getPolicy();
			Product product = policy.getProduct();
			claimProcessorDTO.setProductCode(product.getCode());
		}
		claimProcessorDTO.setIsOnLoad(Boolean.TRUE);
	}
	
	protected void showOmpProcessNegotiation(
			@Observes @CDIEvent(OMPMenuPresenter.OMP_PROCESS_NEGOTIATION_DETAIL_PAGE) final ParameterDTO parameters) {
		//PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		OMPProcessNegotiationTableDTO dto = (OMPProcessNegotiationTableDTO) parameters.getPrimaryParameter();
		Long rodkey = dto.getRodKey();
		String intimationNo = dto.getIntimationNo();
		String userName =dto.getUserName();
		OMPClaimProcessorDTO claimProcessorDTO = new OMPClaimProcessorDTO();
		claimProcessorDTO.setDbOutArray(dto.getDbOutArray());
//		OMPClaimPayment claimPayment = new OMPClaimPayment();
		OMPReimbursement reimbursement = ompIntimationService.getReimbursementByKey(rodkey);
		OMPClaim claimsByIntimationNumber = ompClaimService.getClaimsByIntimationNumber(intimationNo);
//		OMPClaim claim = reimbursement.getClaim();
		String rodNumber = reimbursement.getRodNumber();
		OMPClaimPayment claimPayment = ompIntimationService.getClaimPaymentByRODNo(rodNumber);
		NewIntimationDto newIntimationDto = ompIntimationService
				.getIntimationDto(claimsByIntimationNumber.getIntimation());
		claimProcessorDTO.setNewIntimationDto(newIntimationDto);
		claimProcessorDTO.setIntimationId(newIntimationDto.getIntimationId());
		ClaimDto claimDTO = OMPClaimMapper.getInstance().getClaimDto(claimsByIntimationNumber);
		claimProcessorDTO.setClaimDto(claimDTO);
		claimProcessorDTO.setRodKey(rodkey);
		claimProcessorDTO.setUserName(userName);
//		claimProcessorDTO.setEventCode(newIntimationDto.getEventCodeValue());
		List<OMPReimbursement> rembursementDetailsByClaimKey = ompIntimationService.getRembursementDetailsByClaimKey(claimsByIntimationNumber.getKey());
		if(rembursementDetailsByClaimKey!=null && rembursementDetailsByClaimKey.size()>0){
			if(!rembursementDetailsByClaimKey.get(0).getKey().equals(reimbursement.getKey())){
				claimProcessorDTO.setIsMutipleRod(Boolean.TRUE);
			}
		}
		claimProcessorDTO.setRodNumber(reimbursement.getRodNumber());
		MastersEvents eventCode1 =reimbursement.getEventCodeId();
		SelectValue eventCodeMast = new SelectValue();
		if(eventCode1 != null){
			eventCodeMast.setId(eventCode1.getKey());
			eventCodeMast.setValue(eventCode1.getEventDescription());
			claimProcessorDTO.setEventCode(eventCodeMast);
		}
		if(reimbursement.getCountryId()!=null){
			SelectValue countryValueByKey = masterService.getCountryValueByKey(reimbursement.getCountryId());
			claimProcessorDTO.setHospCountry(countryValueByKey);
		}
		
		claimProcessorDTO.setAilmentLoss(reimbursement.getAilmentLoss());
		claimProcessorDTO.setLossDate(reimbursement.getLossDateTime());
		claimProcessorDTO.setHospName(reimbursement.getHospitalName());
		claimProcessorDTO.setHospCity(reimbursement.getCityName());
		Long hospitalId = reimbursement.getHospitalId();
		//claimProcessorDTO.setHospCountry(reimbursement.getHospitalId());
		claimProcessorDTO.setPlaceOfVisit(reimbursement.getPlaceVisit());
		
		claimProcessorDTO.setClaimTypeValue(claimDTO.getClaimType());
		if(reimbursement.getClaimTypeFlag()!=null){
			if(reimbursement.getClaimTypeFlag().equalsIgnoreCase("C")){
				claimProcessorDTO.setClaimType(Boolean.TRUE);
			}else{
				claimProcessorDTO.setClaimType(Boolean.FALSE);
			}
		}
		
		List<OMPReimbursement> docuList = ompIntimationService.getDocumentRecDetailsByClaimKey(claimsByIntimationNumber.getKey(),SHAConstants.DOC_RECEIVED_FROM_HOSPITAL);
		if(docuList!=null && docuList.size()>=1){
			if(!docuList.get(0).getKey().equals(reimbursement.getKey())){
				claimProcessorDTO.setIsDocRecHospital(Boolean.TRUE);
			}
		}		
		List<OMPReimbursement> insuredList = ompIntimationService.getDocumentRecDetailsByClaimKey(claimsByIntimationNumber.getKey(),SHAConstants.DOC_RECEIVED_FROM_INSURED);
		if(insuredList!=null && insuredList.size()>=1){
			if(!insuredList.get(0).getKey().equals(reimbursement.getKey())){
				claimProcessorDTO.setIsDocRecInsured(Boolean.TRUE);
			}	
		}
	claimProcessorDTO.setHospitalisationFlag(reimbursement.getHospitalisationFlag());
	claimProcessorDTO.setNonHospitalisationFlag(reimbursement.getNonHospitalisationFlag());
	if(reimbursement.getHospitalisationFlag()!=null ){
		claimProcessorDTO.setHospTypeBooleanval(Boolean.TRUE);
	}
	if(reimbursement.getNonHospitalisationFlag()!=null ){
		claimProcessorDTO.setHospTypeBooleanval(Boolean.FALSE);
	}
		
		if(reimbursement.getDelayHR() != null){
			String delayHR = reimbursement.getDelayHR();
			claimProcessorDTO.setDelayHrs(delayHR);
			}
		
		if(reimbursement.getDocumentTypeId() != null){
			SelectValue docType = new SelectValue();
			docType.setId(reimbursement.getDocumentTypeId().getKey());
			docType.setValue(reimbursement.getDocumentTypeId().getValue());
			claimProcessorDTO.setDocType(docType);
	}
		if(reimbursement.getSubClassificationId() != null){
			SelectValue subclass = new SelectValue();
			subclass.setId(reimbursement.getSubClassificationId().getKey());
			subclass.setValue(reimbursement.getSubClassificationId().getValue());
			claimProcessorDTO.setSubClassification(subclass);
	}
		if(reimbursement.getClassificationId() != null){
			SelectValue classif = new SelectValue();
			classif.setId(reimbursement.getClassificationId().getKey());
			classif.setValue(reimbursement.getClassificationId().getValue());
			claimProcessorDTO.setClassification(classif);	
	}
		claimProcessorDTO.setReasonForReconsider(reimbursement.getReasonReconsideration());
		claimProcessorDTO.setDoctorName(reimbursement.getClassiSubDoctorName());
		claimProcessorDTO.setInvestigatorName(reimbursement.getClassiInvestigatorName());
		claimProcessorDTO.setAdvocateName(reimbursement.getClassiAdvocateName());
		claimProcessorDTO.setAuditorName(reimbursement.getClassiAuditorName());
				
//		claimProcessorDTO.setNegotiatorName(reimbursement.getNegotiatorName());
		SelectValue negoName = new SelectValue();
		negoName.setId(1l);
		negoName.setValue(reimbursement.getNegotiatorName());
	claimProcessorDTO.setNegotiatorName(negoName);
		
		MastersValue docReceivedFrm = reimbursement.getClassiDocumentRecivedFmId();
		SelectValue docReceivedFrmMast = new SelectValue();
		if(docReceivedFrm != null){
			docReceivedFrmMast.setId(docReceivedFrm.getKey());
			docReceivedFrmMast.setValue(docReceivedFrm.getValue());						
	}
		claimProcessorDTO.setDocumentsReceivedFrom(docReceivedFrmMast);  								
		claimProcessorDTO.setDocumentsReceivedDate(reimbursement.getDocumentRecivedDate());
		
		claimProcessorDTO.setRemarks(reimbursement.getRemarksProcessor());
		claimProcessorDTO.setRemarksApprover(reimbursement.getRemarksApprover());
		
		Currency currency =reimbursement.getCurrencyTypeId();
		SelectValue currencyMast = new SelectValue();
		if(currency != null){
			currencyMast.setId(currency.getKey());
			currencyMast.setValue(currency.getCurrencyCode());
			claimProcessorDTO.setCurrencyType(currencyMast);
		}
		
		claimProcessorDTO.setConversionValue(reimbursement.getInrConversionRate());
		claimProcessorDTO.setCurrencyRate(reimbursement.getInrTotalAmount());
		
		if(claimPayment != null){

		claimProcessorDTO.setPayeeNameStr(claimPayment.getPayeeName());
		claimProcessorDTO.setPayableAt(claimPayment.getPayabelAt());
		claimProcessorDTO.setPanNo(claimPayment.getPanNo());
	
		MastersValue paymode = claimPayment.getPaymentType();
		SelectValue paymodeMast = new SelectValue();
		if(paymode != null){
		paymodeMast.setId(paymode.getKey());
		paymodeMast.setValue(paymode.getValue());						
	}
		claimProcessorDTO.setPayMode(paymodeMast);
		MastersValue payment = claimPayment.getPaymentStatusId();
		SelectValue paymentToMast = new SelectValue();
		if(payment != null){
		paymentToMast.setId(payment.getKey());
		paymentToMast.setValue(payment.getValue());						
	}
		claimProcessorDTO.setPaymentTo(paymentToMast);
		claimProcessorDTO.setReasonForApproval(claimPayment.getReasonSugesstApproval());
		claimProcessorDTO.setReasonForNegotiation(claimPayment.getReasonNegotiation());
		claimProcessorDTO.setReasonForRejection(claimPayment.getReasonSuggestRejectior());
		claimProcessorDTO.setRemarks(claimPayment.getRemarksProcessor());
		claimProcessorDTO.setRemarksApprover(claimPayment.getRemarksApprover());
		}
		
		MastersValue modeRecept = reimbursement.getClassiModeReceiptId();
		SelectValue modeReceptMast = new SelectValue();
		if(modeRecept != null){
		modeReceptMast.setId(modeRecept.getKey());
		modeReceptMast.setValue(modeRecept.getValue());						
	}
	claimProcessorDTO.setModeOfReceipt(modeReceptMast);
		
List<OMPBenefitCover> benefitCoverList = ompRodBillEntryService.getOMPBenefitCoverByKey(reimbursement.getKey());
		
		List<OMPClaimCalculationViewTableDTO> claimcalculation = new ArrayList<OMPClaimCalculationViewTableDTO>();
		if(benefitCoverList != null){
		for (OMPBenefitCover benefitCover : benefitCoverList) {
			OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO = new OMPClaimCalculationViewTableDTO();
			ompClaimCalculationViewTableDTO.setKey(benefitCover.getKey());
			if(benefitCover.getRodKey() != null){
				
				ompClaimCalculationViewTableDTO.setRodKey(benefitCover.getRodKey().getKey());
			}
			ompClaimCalculationViewTableDTO.setBillAmt(benefitCover.getBillAmount());
			ompClaimCalculationViewTableDTO.setAgreedAmt(benefitCover.getAgreedAmtDollar());
			ompClaimCalculationViewTableDTO.setAmtIn(benefitCover.getNetAmt());
			ompClaimCalculationViewTableDTO.setApprovedAmt(benefitCover.getApprovedAmountDollor());
//			ompClaimCalculationViewTableDTO.setCategory(benefitCover.get);
			ompClaimCalculationViewTableDTO.setDeduction(benefitCover.getDeductibleNonPayBles());
			ompClaimCalculationViewTableDTO.setDifferenceAmt(benefitCover.getDiffAmtDollar());
			ompClaimCalculationViewTableDTO.setExpenses(benefitCover.getExpenesesDollar());
			ompClaimCalculationViewTableDTO.setHandlingCharges(benefitCover.getHandlingChargesDollar());
			ompClaimCalculationViewTableDTO.setNegotiationCapping(benefitCover.getNegoFeeCapping());
			ompClaimCalculationViewTableDTO.setNegotiationClaimed(benefitCover.getNegoFeesClaimedDollar());
			ompClaimCalculationViewTableDTO.setNegotiationPayable(benefitCover.getNegoFeesClaimedInr());
			ompClaimCalculationViewTableDTO.setTotalAmt(benefitCover.getTotalAmtPayBleDollar());
			ompClaimCalculationViewTableDTO.setTotalAmtInr(benefitCover.getTotalAmtInr());
			MastersValue category = benefitCover.getCategory();
			SelectValue categoryMast = new SelectValue();
			if(category != null){
				categoryMast.setId(category.getKey());
				categoryMast.setValue(category.getValue());						
		}
			ompClaimCalculationViewTableDTO.setCategory(categoryMast);
			claimcalculation.add(ompClaimCalculationViewTableDTO);
		}
		}
		MastersEvents event =reimbursement.getEventCodeId();
		if(event!=null){
			String eventCode = event.getEventCode();
			Double balanceSI = reimbursementService.getBalanceSI(reimbursement,claimsByIntimationNumber,eventCode);
			System.out.println(balanceSI);
			claimProcessorDTO.setBalanceSI(balanceSI);
			OMPIntimation intimation = reimbursement.getClaim().getIntimation();
			Policy policy = reimbursement.getClaim().getIntimation().getPolicy();
			Product product = policy.getProduct();
			String plan = intimation.getInsured().getPlan();
			Double insuredSumInsured = policy.getTotalSumInsured();
			Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(policy.getKey(), eventCode);
			Map<String, Double> ompDeductible = dbCalculationService.getOmpDeductible(product.getKey(), plan, sumInsured, event.getEventCode(),insuredSumInsured);
			Double deductibles = (Double)ompDeductible.get(SHAConstants.DEDUCTIBLES);
			claimProcessorDTO.setDeductibles(deductibles);
			claimProcessorDTO.setProductCode(product.getCode());
		}
		
		
		OMPClaimProcessorCalculationSheetDTO calculationSheetDto = new OMPClaimProcessorCalculationSheetDTO();
		if(reimbursement != null){
			
			if(reimbursement.getTotalAmount() != null){
				Double totAmt = reimbursement.getTotalAmount().doubleValue();
				calculationSheetDto.setAmttotal(totAmt);
			}
			if(reimbursement.getDeductbleUsd() != null){
				Double deductble = reimbursement.getDeductbleUsd().doubleValue();
				calculationSheetDto.setDeductiblePerPolicy(deductble);
			}
			if(reimbursement.getCoPay() != null){
//				long coPay = reimbursement.getCoPay().longValue();
				SelectValue copay = new SelectValue();
				if(reimbursement.getCoPay()==30){
					copay.setId(2L);
				}else{
					copay.setId(1L);
				}
				copay.setValue(reimbursement.getCoPay().toString());
				calculationSheetDto.setCoPayPercentage(copay);
			}
			if(reimbursement.getApprovedAmtAfterCoPayUsd() != null){
				Double aftrCopayAproved = reimbursement.getApprovedAmtAfterCoPayUsd().doubleValue();
				calculationSheetDto.setCoPayApprovedAmt(aftrCopayAproved);
			}
			if(reimbursement.getAgreedAmtAfterCopayUsd() != null){
				Double agreedAmtAfrtCopay = reimbursement.getAgreedAmtAfterCopayUsd().doubleValue();
				calculationSheetDto.setCoPayAmount(agreedAmtAfrtCopay);
			}
			if(reimbursement.getBalanceSI() != null){
				Double balanceSi = reimbursement.getBalanceSI().doubleValue();
				calculationSheetDto.setBalanceSumInured(balanceSi);
			}
			if(reimbursement.getTotalPayableAmtUsd() != null){
				Double totalamtusd = reimbursement.getTotalPayableAmtUsd().doubleValue();
				calculationSheetDto.setTotalAmtPayable(totalamtusd);
			}
			if(reimbursement.getAlreadyPaidAmtUsd() != null){
				Double alreadyPaid = reimbursement.getAlreadyPaidAmtUsd().doubleValue();
				calculationSheetDto.setAlreadyPaidAmt(alreadyPaid);
			}
			if(reimbursement.getPayableAmtUsd() != null){
				Double paybleAmtusd = reimbursement.getPayableAmtUsd().doubleValue();
				calculationSheetDto.setPayableAmt(paybleAmtusd);
			}
			if(reimbursement.getPayableAmtInr() != null){
				Double payBleamtInr = reimbursement.getPayableAmtInr().doubleValue();
				calculationSheetDto.setInrAmt(payBleamtInr);
			}
			if(reimbursement.getRecoveredDate() != null){
				calculationSheetDto.setDateOfRecovery(reimbursement.getRecoveredDate());
			}
			if(reimbursement.getRecoveredAmountInr() != null){
				Double recovAmt = reimbursement.getRecoveredAmountInr().doubleValue();
				calculationSheetDto.setAmtRecoveredINR(recovAmt);
			}
			if(reimbursement.getRecoveredAmountUsd() != null){
				Double recoUsd = reimbursement.getRecoveredAmountUsd().doubleValue();
				calculationSheetDto.setAmtRecoveredDollar(recoUsd);
			}
			if(reimbursement.getNegogiationAgreedAmt() != null){
				Double recoUsd = reimbursement.getNegogiationAgreedAmt().doubleValue();
				calculationSheetDto.setNegotiateAgreedAmt(recoUsd);
			}
			calculationSheetDto.setRemarks(reimbursement.getRemarks());
		}
		claimProcessorDTO.setCalculationSheetDTO(calculationSheetDto);
		List<OMPNegotiation> ompNegotiation = ompRodBillEntryService.getOMPNegotiation(rodkey);
		List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = new ArrayList<OMPNegotiationDetailsDTO>();
		if(ompNegotiation!=null){
		for (OMPNegotiation ompNegotiation2 : ompNegotiation) {
			OMPNegotiationDetailsDTO negotiationDetailsDTO = new OMPNegotiationDetailsDTO();
			negotiationDetailsDTO.setKey(ompNegotiation2.getKey());
			negotiationDetailsDTO.setAgreedAmount(ompNegotiation2.getAggredAmount());
			negotiationDetailsDTO.setApprovedAmt(ompNegotiation2.getApprovedAmount());
			negotiationDetailsDTO.setNameOfNegotiatior(ompNegotiation2.getNegotiatorName());
			negotiationDetailsDTO.setNegotiationCompletDate(ompNegotiation2.getNegotiationCompletedDate());
			negotiationDetailsDTO.setNegotiationRemarks(ompNegotiation2.getNegotiationRemarks());
			negotiationDetailsDTO.setNegotiationReqstDate(ompNegotiation2.getNegotiationRequestedDate());
			if(ompNegotiation2.getRodKey()!=null){
				negotiationDetailsDTO.setRodKey(ompNegotiation2.getRodKey().getKey());
			}
			if(ompNegotiation2.getClaim()!=null){
				negotiationDetailsDTO.setClaimKey(ompNegotiation2.getClaim().getKey());
			}
			negotiationDetailsDTOs.add(negotiationDetailsDTO);
		}}
		claimProcessorDTO.setNegotiationDetailsDTOs(negotiationDetailsDTOs);
		
		claimProcessorDTO.setClaimCalculationViewTable(claimcalculation);
		String productCode = claimsByIntimationNumber.getProductCode();
		Product productByProductCode = masterService.getProductByProductCode(productCode);
		
		BeanItemContainer<SelectValue> classification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CLASSIFICATION);
		BeanItemContainer<SelectValue> subClassification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_SUB_CLASSIFICATION);
		BeanItemContainer<SelectValue> paymentTo = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_OMP_PAYTO);
		BeanItemContainer<SelectValue> paymentMode = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_PAY_MODE);
		BeanItemContainer<SelectValue> eventCode = masterService.getListMasterEventByProduct(productByProductCode.getKey());
		BeanItemContainer<SelectValue> currencyValue = masterService.getListMasterCurrencyValue();
		BeanItemContainer<SelectValue> negotiatorName = masterService.getNegotiationNamesAll();
		BeanItemContainer<SelectValue> modeOfReciept = masterService.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
		BeanItemContainer<SelectValue> documentRecievedFrom = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_RECFRM);
		BeanItemContainer<SelectValue> documentType = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_TYPE);
		BeanItemContainer<SelectValue> country = masterService.getCountryValue();
		view.setOmpProcessNegotiationview(OMPProcessNegotiationWizardView.class,claimProcessorDTO,classification,subClassification,paymentTo,
				paymentMode,eventCode,currencyValue,negotiatorName,modeOfReciept,documentRecievedFrom,documentType,country);
	}
	
	
	private String generateRODNumber(Long claimKey, String intimationNumber) {
		Long count = ompClaimService.getACknowledgeNumberCountByClaimKey(claimKey);
		StringBuffer ackNoBuf = new StringBuffer();
		Long lackCount = count + 001;
		ackNoBuf.append("ROD/")
				.append(intimationNumber).append("/").append(lackCount);
		return ackNoBuf.toString();
		
	}
	
	private void ompPrcessApproverDetails(OMPClaimProcessorDTO claimProcessorDTO, String intimationNo, String userName, Long rodkey) {
		// claimProcessorDTO.setDbOutArray(dto.getDbOutArray());
		/*String rodNumber = reimbursement.getRodNumber();
		OMPClaimPayment claimPayment = ompIntimationService.getClaimPaymentByRODNo(rodNumber);*/
		OMPClaim claimsByIntimationNumber = ompClaimService.getClaimsByIntimationNumber(intimationNo);
//		OMPClaim claim = reimburment.getClaim();
		
		
		NewIntimationDto newIntimationDto = ompIntimationService
				.getIntimationDto(claimsByIntimationNumber.getIntimation());
		setClaimDetails(rodkey, userName, claimProcessorDTO,claimsByIntimationNumber, newIntimationDto);
		
		
		List<OMPReimbursement> rembursementByClaimKey = ompIntimationService.getRembursementDetailsByClaimKey(claimsByIntimationNumber.getKey());
			List<OMPClaimCalculationViewTableDTO> claimcalculationDto = new ArrayList<OMPClaimCalculationViewTableDTO>();
			if(rembursementByClaimKey != null){
			for (OMPReimbursement ompreimbursement : rembursementByClaimKey) {
				OMPClaimCalculationViewTableDTO claimCalulation = new OMPClaimCalculationViewTableDTO();
				if(ompreimbursement.getClassificationId()!= null && ompreimbursement.getClassificationId().getValue()!= null){
					MastersValue classValue = ompreimbursement.getClassificationId();
					SelectValue seleCValue = new SelectValue();
					seleCValue.setId(classValue.getKey());
					seleCValue.setValue(classValue.getValue());
					claimCalulation.setClassification(seleCValue);
				}
				if(ompreimbursement.getSubClassificationId()!= null && ompreimbursement.getSubClassificationId().getValue()!= null){
					MastersValue classValue = ompreimbursement.getSubClassificationId();
					SelectValue seleCValue = new SelectValue();
					seleCValue.setId(classValue.getKey());
					seleCValue.setValue(classValue.getValue());
					claimCalulation.setSubClassification(seleCValue);
				}
				if(ompreimbursement.getCategoryId()!= null && ompreimbursement.getCategoryId().getValue()!= null){
					MastersValue classValue = ompreimbursement.getCategoryId();
					SelectValue seleCValue = new SelectValue();
					seleCValue.setId(classValue.getKey());
					seleCValue.setValue(classValue.getValue());
					claimCalulation.setCategory(seleCValue);
				}
				if(ompreimbursement.getClassiDocumentRecivedFmId()!= null && ompreimbursement.getClassiDocumentRecivedFmId().getValue()!= null){
					MastersValue docValue = ompreimbursement.getClassiDocumentRecivedFmId();
					SelectValue docRecValue = new SelectValue();
					docRecValue.setId(ompreimbursement.getClassiDocumentRecivedFmId().getKey());
					docRecValue.setValue(ompreimbursement.getClassiDocumentRecivedFmId().getValue());
					claimCalulation.setDocRecivedFrm(docRecValue);
				}
				Currency currencyType = ompreimbursement.getCurrencyTypeId();
				if(currencyType!=null){
				Long currencyTypeId = currencyType.getKey();
				SelectValue currencyValue = new SelectValue();
				currencyValue.setId(currencyTypeId);
				currencyValue.setValue(currencyType.getCurrencyName());
				claimCalulation.setCurrencyType(currencyValue);
				}
				/*Currency currency = rodBillentryService.getOMPCurrency(currencyTypeId);
				if(currency != null){
					reimbursement.setCurrencyTypeId(currency);
				}*/
				claimCalulation.setCurrencyrate(ompreimbursement.getInrTotalAmount());
				claimCalulation.setConversionValue(ompreimbursement.getInrConversionRate());
				if(ompreimbursement.getNegotiationDone()!= null){
					SelectValue negodone = new SelectValue();
					negodone.setId(0l);
					negodone.setValue(ompreimbursement.getNegotiationDone());
					claimCalulation.setNegotiationDone(negodone);
				}
				claimCalulation.setReject(ompreimbursement.getRejectFlg());
				claimCalulation.setSendforApprover(ompreimbursement.getSendForApproverFlg());
				if(ompreimbursement.getViewForApprover()!= null){
					SelectValue appValue = new SelectValue();
					appValue.setId(0l);
					appValue.setValue(ompreimbursement.getViewForApprover());
					claimCalulation.setViewforApprover(appValue);
				}
				claimCalulation.setNotinClaimCount(ompreimbursement.getNotInClaimCountFlg());
				claimCalulation.setRodnumber(ompreimbursement.getRodNumber());
				
				
				List<OMPNewRecoverableTableDto> recovList = new ArrayList<OMPNewRecoverableTableDto>();
				OMPNewRecoverableTableDto  recoverableDto = new OMPNewRecoverableTableDto();
				recoverableDto.setDateofRecovery(ompreimbursement.getRecoveredDate());
				if(ompreimbursement.getRecoveredAmountInr()!= null){
					double recoinr = ompreimbursement.getRecoveredAmountInr().doubleValue();
					recoverableDto.setAgreedAmount(recoinr);
				}
				if(ompreimbursement.getRecoveredAmountUsd()!= null){
					double recoUsd = ompreimbursement.getRecoveredAmountUsd().doubleValue();
					recoverableDto.setAmountRecoveredUsd(recoUsd);
				}
				recoverableDto.setSendToAccounts(ompreimbursement.getSendToAccounts());
				recoverableDto.setRemarks(ompreimbursement.getRemarks());
				recovList.add(recoverableDto);
				
				List<OMPPaymentDetailsTableDTO> paymentListDto = new ArrayList<OMPPaymentDetailsTableDTO>();
				
				OMPPaymentDetailsTableDTO paymentDto = new OMPPaymentDetailsTableDTO();
				paymentDto.setPanNo(ompreimbursement.getPanNumber());
				paymentDto.setPayableAt(ompreimbursement.getPayableAt());
				paymentDto.setPayeeNameStr(ompreimbursement.getPayeeName());
//				paymentDto.setPaymentTo(ompreimbursement.getPaymentTo());
//				paymentDto.setPayMode(ompreimbursement.getPaymentModeId());
//				paymentDto.setPaymentStatus(ompreimbursement.getPatientStatus().getValue());
				paymentListDto.add(paymentDto);
					List<OMPBenefitCover> benefitCoverList = ompRodBillEntryService.getOMPBenefitCoverByKey(ompreimbursement.getKey());
					if(benefitCoverList != null){
						
						if(benefitCoverList.get(0)!=null){
							OMPBenefitCover benefitCover = benefitCoverList.get(0);
							claimCalulation.setKey(benefitCover.getKey());
							if(benefitCover.getRodKey() != null){
								
								claimCalulation.setRodKey(benefitCover.getRodKey().getKey());
							}
							claimCalulation.setBillAmt(benefitCover.getBillAmount());
							claimCalulation.setAgreedAmt(benefitCover.getAgreedAmtDollar());
							claimCalulation.setAmtIn(benefitCover.getNetAmt());
							claimCalulation.setApprovedAmt(benefitCover.getApprovedAmountDollor());
//							ompClaimCalculationViewTableDTO.setCategory(benefitCover.get);
							claimCalulation.setDeduction(benefitCover.getDeductibleNonPayBles());
							/*ompClaimCalculationViewTableDTO.setDifferenceAmt(benefitCover.getDiffAmtDollar());
							ompClaimCalculationViewTableDTO.setExpenses(benefitCover.getExpenesesDollar());
							ompClaimCalculationViewTableDTO.setHandlingCharges(benefitCover.getHandlingChargesDollar());
							ompClaimCalculationViewTableDTO.setNegotiationCapping(benefitCover.getNegoFeeCapping());
							ompClaimCalculationViewTableDTO.setNegotiationClaimed(benefitCover.getNegoFeesClaimedDollar());
							ompClaimCalculationViewTableDTO.setNegotiationPayable(benefitCover.getNegoFeesClaimedInr());
							ompClaimCalculationViewTableDTO.setTotalExp(benefitCover.getTotalExpenceDollar());*/
							claimCalulation.setTotalAmtInr(benefitCover.getTotalAmtInr());
							claimCalulation.setTotalAmt(benefitCover.getTotalAmtPayBleDollar());
							claimCalulation.setCopayamount(benefitCover.getCopayAmount());
							if(benefitCover.getCopayPercentage()!= null){
								SelectValue copayvValue = new SelectValue();
									if(benefitCover.getCopayPercentage()==1L){
										copayvValue.setValue("0");
									}else{
										copayvValue.setValue("30");
									}
									copayvValue.setId(benefitCover.getCopayPercentage());
									claimCalulation.setCopay(copayvValue);
							}
							claimCalulation.setApprovedamountaftecopay(benefitCover.getApprAmtAftrCopya());
						}
					}
					claimcalculationDto.add(claimCalulation);
			claimProcessorDTO.setClaimCalculationViewTable(claimcalculationDto);
			claimProcessorDTO.setOmpRecoverableTableList(recovList);
			claimProcessorDTO.setOmpPaymentDetailsList(paymentListDto);
			}
		}
		/*if(rembursementDetailsByClaimKey!=null && rembursementDetailsByClaimKey.size()>1){
			if(!rembursementDetailsByClaimKey.get(0).getKey().equals(reimbursement.getKey())){
				claimProcessorDTO.setIsMutipleRod(Boolean.TRUE);
			}
		}
		
		List<OMPReimbursement> docuList = ompIntimationService.getDocumentRecDetailsByClaimKey(claimsByIntimationNumber.getKey(),SHAConstants.DOC_RECEIVED_FROM_HOSPITAL);
		if(docuList!=null && docuList.size()>=1){
			if(!docuList.get(0).getKey().equals(reimbursement.getKey())){
				claimProcessorDTO.setIsDocRecHospital(Boolean.TRUE);
			}
		}		
		List<OMPReimbursement> insuredList = ompIntimationService.getDocumentRecDetailsByClaimKey(claimsByIntimationNumber.getKey(),SHAConstants.DOC_RECEIVED_FROM_INSURED);
		if(insuredList!=null && insuredList.size()>=1){
			if(!insuredList.get(0).getKey().equals(reimbursement.getKey())){
				claimProcessorDTO.setIsDocRecInsured(Boolean.TRUE);
			}	
		}*/
		
		
		/*claimProcessorDTO.setAilmentLoss(reimbursement.getAilmentLoss());
		claimProcessorDTO.setLossDate(reimbursement.getLossDateTime());
		claimProcessorDTO.setHospName(reimbursement.getHospitalName());
		claimProcessorDTO.setHospCity(reimbursement.getCityName());
		Long hospitalId = reimbursement.getHospitalId();*/
		
	/*	Country country1 = null;
		if (country1!=null && country1.getKey() != null ){
		SelectValue countrysel = new SelectValue();
		countrysel = masterService.getCountryValueByValue(country1.getKey().toString());
		countrysel.setId(country1.getKey());
		countrysel.setValue(country1.getValue());
		}
		claimProcessorDTO.setHospCountry(reimbursement.getCountryId());
		*/
//		claimProcessorDTO.setPlaceOfVisit(newIntimationDto.getPlaceVisit());
//		claimProcessorDTO.setRodNumber(reimbursement.getRodNumber());
		
			if(claimsByIntimationNumber.getClaimType()!=null){
				
				MastersValue cmbclaimType = claimsByIntimationNumber.getClaimType();
				SelectValue claimType = new SelectValue();
				claimType.setId(cmbclaimType.getKey());
				claimType.setValue(cmbclaimType.getValue());
				if(claimType!=null){
					if(ReferenceTable.CASHLESS_CLAIM.equalsIgnoreCase(claimType.getValue())){
//						optionClaimtype.setValue("Cashless");
						claimProcessorDTO.setClaimType(Boolean.TRUE);
					}else{
//						optionClaimtype.setValue("Reimbursement");
						claimProcessorDTO.setClaimType(Boolean.FALSE);
					}
				}
			}
				
				/*if(reimbursement.getClaimTypeFlag().equalsIgnoreCase("C")){
					claimProcessorDTO.setClaimType(Boolean.TRUE);
				}else{
					claimProcessorDTO.setClaimType(Boolean.FALSE);
				}
			}*/
//		claimProcessorDTO.setHospitalisationFlag(claimsByIntimationNumber.getHospitalisationFlag());
		claimProcessorDTO.setNonHospitalisationFlag(claimsByIntimationNumber.getNonHospitalisationFlag());
//		if(claimsByIntimationNumber.getHospitalisationFlag()!=null ){
//			claimProcessorDTO.setHospTypeBooleanval(Boolean.TRUE);
//		}
		if(claimsByIntimationNumber.getNonHospitalisationFlag()!=null ){
			claimProcessorDTO.setHospTypeBooleanval(Boolean.TRUE);
		}
		/*if(reimbursement.getDelayHR() != null){
			String delayHR = reimbursement.getDelayHR();
			claimProcessorDTO.setDelayHrs(delayHR);
		}
		if(reimbursement.getDocumentTypeId() != null){
			SelectValue docType = new SelectValue();
			docType.setId(reimbursement.getDocumentTypeId().getKey());
			docType.setValue(reimbursement.getDocumentTypeId().getValue());
	claimProcessorDTO.setDocType(docType);
	}
		if(reimbursement.getSubClassificationId() != null){
			SelectValue subclass = new SelectValue();
			subclass.setId(reimbursement.getSubClassificationId().getKey());
			subclass.setValue(reimbursement.getSubClassificationId().getValue());
	claimProcessorDTO.setSubClassification(subclass);
	}
		if(reimbursement.getClassificationId() != null){
			SelectValue classif = new SelectValue();
			classif.setId(reimbursement.getClassificationId().getKey());
			classif.setValue(reimbursement.getClassificationId().getValue());
	claimProcessorDTO.setClassification(classif);	
	}
		claimProcessorDTO.setEmailId(reimbursement.getEmailId());
		
		MastersValue modeRecept = reimbursement.getClassiModeReceiptId();
		SelectValue modeReceptMast = new SelectValue();
		if(modeRecept != null){
		modeReceptMast.setId(modeRecept.getKey());
		modeReceptMast.setValue(modeRecept.getValue());						
	}
	claimProcessorDTO.setModeOfReceipt(modeReceptMast);

	Currency currency =reimbursement.getCurrencyTypeId();
	SelectValue currencyMast = new SelectValue();
	if(currency != null){
		currencyMast.setId(currency.getKey());
		currencyMast.setValue(currency.getCurrencyCode());
		claimProcessorDTO.setCurrencyType(currencyMast);
	}

	claimProcessorDTO.setConversionValue(reimbursement.getInrConversionRate());
	claimProcessorDTO.setCurrencyRate(reimbursement.getInrTotalAmount());
	
	claimProcessorDTO.setPayeeNameStr(reimbursement.getPayeeName());
	claimProcessorDTO.setPayableAt(reimbursement.getPayableAt());
	claimProcessorDTO.setPanNo(reimbursement.getPanNumber());
	SelectValue payMode = new SelectValue();
	payMode.setId(reimbursement.getPaymentModeId());
	payMode.setValue("");
	claimProcessorDTO.setPayMode(payMode);
	
	MastersValue paymentMas = reimbursement.getPaymentTo();
	SelectValue paymentSelet = new SelectValue();
	if(paymentMas != null){
		paymentSelet.setId(paymentMas.getKey());
		paymentSelet.setValue(paymentMas.getValue());
		claimProcessorDTO.setPaymentTo(paymentSelet);     
	}
	claimProcessorDTO.setEmailId(reimbursement.getEmailId());
	
	if(claimPayment != null){
		claimProcessorDTO.setPayeeNameStr(claimPayment.getPayeeName());
		claimProcessorDTO.setPayableAt(claimPayment.getPayabelAt());
	claimProcessorDTO.setPanNo(claimPayment.getPanNo());
	
		MastersValue paymode = claimPayment.getPaymentType();
		SelectValue paymodeMast = new SelectValue();
		if(paymode != null){
		paymodeMast.setId(paymode.getKey());
		paymodeMast.setValue(paymode.getValue());						
	}
	claimProcessorDTO.setPayMode(paymodeMast);
		MastersValue payment = claimPayment.getPaymentStatusId();
		SelectValue paymentToMast = new SelectValue();
		if(payment != null){
		paymentToMast.setId(payment.getKey());
		paymentToMast.setValue(payment.getValue());						
	}
		claimProcessorDTO.setPaymentTo(paymentToMast);
		claimProcessorDTO.setReasonForApproval(claimPayment.getReasonSugesstApproval());
		claimProcessorDTO.setReasonForNegotiation(claimPayment.getReasonNegotiation());
		claimProcessorDTO.setReasonForRejection(claimPayment.getReasonSuggestRejectior());
		claimProcessorDTO.setRemarks(claimPayment.getRemarksProcessor());
	}
		claimProcessorDTO.setReasonForReconsider(reimbursement.getReasonReconsideration());
		claimProcessorDTO.setDoctorName(reimbursement.getClassiSubDoctorName());
		claimProcessorDTO.setInvestigatorName(reimbursement.getClassiInvestigatorName());
		claimProcessorDTO.setAdvocateName(reimbursement.getClassiAdvocateName());
		claimProcessorDTO.setAuditorName(reimbursement.getClassiAuditorName());

		claimProcessorDTO.setRemarks(reimbursement.getRemarksProcessor());
		claimProcessorDTO.setReasonForNegotiation(reimbursement.getReasonNegotiation());
		claimProcessorDTO.setReasonForRejection(reimbursement.getReasonSuggestRejection());
		claimProcessorDTO.setReasonForApproval(reimbursement.getReasonSuggestApprover());
		
		
//		claimProcessorDTO.setNegotiatorName(reimbursement.getNegotiatorName());
		SelectValue negoName = new SelectValue();
		negoName.setId(1l);
		negoName.setValue(reimbursement.getNegotiatorName());
	claimProcessorDTO.setNegotiatorName(negoName);
		
		MastersValue docReceivedFrm = reimbursement.getClassiDocumentRecivedFmId();
		SelectValue docReceivedFrmMast = new SelectValue();
		if(docReceivedFrm != null){
			docReceivedFrmMast.setId(docReceivedFrm.getKey());
			docReceivedFrmMast.setValue(docReceivedFrm.getValue());						
	}
		claimProcessorDTO.setDocumentsReceivedFrom(docReceivedFrmMast); 
		claimProcessorDTO.setDocumentsReceivedDate(reimbursement.getDocumentRecivedDate());*/
		
	
		/*List<OMPClaimCalculationViewTableDTO> claimcalculation1 = new ArrayList<OMPClaimCalculationViewTableDTO>();
		if(rembursementByClaimKey!= null && rembursementByClaimKey.get(0).getKey()!= null){
			List<OMPBenefitCover> benefitCoverList = ompRodBillEntryService.getOMPBenefitCoverByKey(rembursementByClaimKey.get(0).getKey());
		if(benefitCoverList != null){
		for (OMPBenefitCover benefitCover : benefitCoverList) {
		
			OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO = new OMPClaimCalculationViewTableDTO();
			ompClaimCalculationViewTableDTO.setKey(benefitCover.getKey());
			if(benefitCover.getRodKey() != null){
				
				ompClaimCalculationViewTableDTO.setRodKey(benefitCover.getRodKey().getKey());
			}
			ompClaimCalculationViewTableDTO.setBillAmt(benefitCover.getBillAmount());
			ompClaimCalculationViewTableDTO.setAgreedAmt(benefitCover.getAgreedAmtDollar());
			ompClaimCalculationViewTableDTO.setAmtIn(benefitCover.getNetAmt());
			ompClaimCalculationViewTableDTO.setApprovedAmt(benefitCover.getApprovedAmountDollor());
//			ompClaimCalculationViewTableDTO.setCategory(benefitCover.get);
			ompClaimCalculationViewTableDTO.setDeduction(benefitCover.getDeductibleNonPayBles());
			ompClaimCalculationViewTableDTO.setDifferenceAmt(benefitCover.getDiffAmtDollar());
			ompClaimCalculationViewTableDTO.setExpenses(benefitCover.getExpenesesDollar());
			ompClaimCalculationViewTableDTO.setHandlingCharges(benefitCover.getHandlingChargesDollar());
			ompClaimCalculationViewTableDTO.setNegotiationCapping(benefitCover.getNegoFeeCapping());
			ompClaimCalculationViewTableDTO.setNegotiationClaimed(benefitCover.getNegoFeesClaimedDollar());
			ompClaimCalculationViewTableDTO.setNegotiationPayable(benefitCover.getNegoFeesClaimedInr());
			ompClaimCalculationViewTableDTO.setTotalExp(benefitCover.getTotalExpenceDollar());
			ompClaimCalculationViewTableDTO.setTotalAmtInr(benefitCover.getTotalAmtInr());
			ompClaimCalculationViewTableDTO.setTotalAmt(benefitCover.getTotalAmtPayBleDollar());
			ompClaimCalculationViewTableDTO.setCopayamount(benefitCover.getCopayAmount());
			if(benefitCover.getCopayPercentage()!= null){
				SelectValue copayvValue = new SelectValue();
				copayvValue.setId(benefitCover.getCopayPercentage());
				copayvValue.setValue("");
				ompClaimCalculationViewTableDTO.setCopay(copayvValue);
			}
			ompClaimCalculationViewTableDTO.setApprovedamountaftecopay(benefitCover.getApprAmtAftrCopya());
			MastersValue category = benefitCover.getCategory();
			SelectValue categoryMast = new SelectValue();
			if(category != null){
				categoryMast.setId(category.getKey());
				categoryMast.setValue(category.getValue());						
		}
			ompClaimCalculationViewTableDTO.setCategory(categoryMast);
			claimcalculation1.add(ompClaimCalculationViewTableDTO);
			claimProcessorDTO.setClaimCalculationViewTable(claimcalculation1);
			}
		}
		}*/
		
		List<OMPNegotiation> ompNegotiation1 = ompRodBillEntryService.getOMPNegotiation(rodkey);
		if(ompNegotiation1!=null){
			OMPNegotiation ompNegotiation2 = ompNegotiation1.get(0);
			claimProcessorDTO.setAgreedAmt(ompNegotiation2.getAggredAmount());
		}
		
		String productCode = claimsByIntimationNumber.getProductCode();
		Product productByProductCode = masterService.getProductByProductCode(productCode);
//		claimProcessorDTO.setClaimCalculationViewTable(claimcalculation);
		
		
		/*OMPClaimProcessorCalculationSheetDTO calculationSheetDto = new OMPClaimProcessorCalculationSheetDTO();
		if(reimbursement != null){
			
			if(reimbursement.getTotalAmount() != null){
				Double totAmt = reimbursement.getTotalAmount().doubleValue();
						calculationSheetDto.setAmttotal(totAmt);
			}
			if(reimbursement.getDeductbleUsd() != null){
				Double deductble = reimbursement.getDeductbleUsd().doubleValue();
				calculationSheetDto.setDeductiblePerPolicy(deductble);
			}
			if(reimbursement.getCoPay() != null){
//				long coPay = reimbursement.getCoPay().longValue();
				SelectValue copay = new SelectValue();
				if(reimbursement.getCoPay()==30){
					copay.setId(2L);
				}else{
					copay.setId(1L);
				}
				copay.setValue(reimbursement.getCoPay().toString());
				calculationSheetDto.setCoPayPercentage(copay);
			}
			if(reimbursement.getApprovedAmtAfterCoPayUsd() != null){
				Double aftrCopayAproved = reimbursement.getApprovedAmtAfterCoPayUsd().doubleValue();
				calculationSheetDto.setCoPayApprovedAmt(aftrCopayAproved);
			}
			if(reimbursement.getAgreedAmtAfterCopayUsd() != null){
				Double agreedAmtAfrtCopay = reimbursement.getAgreedAmtAfterCopayUsd().doubleValue();
				calculationSheetDto.setCoPayAmount(agreedAmtAfrtCopay);
			}
			if(reimbursement.getBalanceSI() != null){
				Double balanceSi = reimbursement.getBalanceSI().doubleValue();
				calculationSheetDto.setBalanceSumInured(balanceSi);
			}
			if(reimbursement.getTotalPayableAmtUsd() != null){
				Double totalamtusd = reimbursement.getTotalPayableAmtUsd().doubleValue();
				calculationSheetDto.setTotalAmtPayable(totalamtusd);
			}
			if(reimbursement.getAlreadyPaidAmtUsd() != null){
				Double alreadyPaid = reimbursement.getAlreadyPaidAmtUsd().doubleValue();
				calculationSheetDto.setAlreadyPaidAmt(alreadyPaid);
			}
			if(reimbursement.getPayableAmtUsd() != null){
				Double paybleAmtusd = reimbursement.getPayableAmtUsd().doubleValue();
				calculationSheetDto.setPayableAmt(paybleAmtusd);
			}
			if(reimbursement.getPayableAmtInr() != null){
				Double payBleamtInr = reimbursement.getPayableAmtInr().doubleValue();
				calculationSheetDto.setInrAmt(payBleamtInr);
			}
			if(reimbursement.getRecoveredDate() != null){
				calculationSheetDto.setDateOfRecovery(reimbursement.getRecoveredDate());
			}
			if(reimbursement.getRecoveredAmountInr() != null){
				Double recovAmt = reimbursement.getRecoveredAmountInr().doubleValue();
				calculationSheetDto.setAmtRecoveredINR(recovAmt);
			}
			if(reimbursement.getRecoveredAmountUsd() != null){
				Double recoUsd = reimbursement.getRecoveredAmountUsd().doubleValue();
				calculationSheetDto.setAmtRecoveredDollar(recoUsd);
			}
			if(reimbursement.getNegogiationAgreedAmt() != null){
				Double recoUsd = reimbursement.getNegogiationAgreedAmt().doubleValue();
				calculationSheetDto.setNegotiateAgreedAmt(recoUsd);
			}
			
			calculationSheetDto.setRemarks(reimbursement.getRemarks());
		}
		claimProcessorDTO.setCalculationSheetDTO(calculationSheetDto);*/
		
		if(rodkey!=null){
			List<OMPNegotiation> ompNegotiation = ompRodBillEntryService.getOMPNegotiation(rodkey);
			List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = new ArrayList<OMPNegotiationDetailsDTO>();
			if(ompNegotiation!=null){
				for (OMPNegotiation ompNegotiation2 : ompNegotiation) {
					OMPNegotiationDetailsDTO negotiationDetailsDTO = new OMPNegotiationDetailsDTO();
					negotiationDetailsDTO.setKey(ompNegotiation2.getKey());
					negotiationDetailsDTO.setAgreedAmount(ompNegotiation2.getAggredAmount());
					negotiationDetailsDTO.setApprovedAmt(ompNegotiation2.getApprovedAmount());
					negotiationDetailsDTO.setNameOfNegotiatior(ompNegotiation2.getNegotiatorName());
					negotiationDetailsDTO.setNegotiationCompletDate(ompNegotiation2.getNegotiationCompletedDate());
					negotiationDetailsDTO.setNegotiationRemarks(ompNegotiation2.getNegotiationRemarks());
					negotiationDetailsDTO.setNegotiationReqstDate(ompNegotiation2.getNegotiationRequestedDate());
					if(ompNegotiation2.getRodKey()!=null){
						negotiationDetailsDTO.setRodKey(ompNegotiation2.getRodKey().getKey());
					}
					if(ompNegotiation2.getClaim()!=null){
						negotiationDetailsDTO.setClaimKey(ompNegotiation2.getClaim().getKey());
					}
					negotiationDetailsDTOs.add(negotiationDetailsDTO);
				}
			}
			claimProcessorDTO.setNegotiationDetailsDTOs(negotiationDetailsDTOs);
		}
		
	}
	
	//R1276
	protected void showOMPRegistrationPage(@Observes @CDIEvent(OMPMenuPresenter.OMP_REG_DETAIL_PAGE) final ParameterDTO parameters) {
		OMPNewRegistrationSearchDTO createDTO = (OMPNewRegistrationSearchDTO)parameters.getPrimaryParameter();
		Insured  insuredObj =insuredService.getInsuredByPolicyNo(createDTO.getPolicy().getKey().toString());
		createDTO.setInsured(insuredObj);
		NewIntimationDto newIntimationDto = createDTO.getIntimationDto();
		ClaimDto claimDto = new ClaimDto();
		claimDto.setNewIntimationDto(newIntimationDto);
		claimDto.setIntimationKey(createDTO.getIntimationKey());
		claimDto.setAdmissionDate(createDTO.getAdmissionDate());
		createDTO.setClaimDto(claimDto);	
		view.renderOMPRegistationPage(OMPNewRegistrationPageView.class, createDTO);
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
	
	protected void showOMPAcknowledgementDocumentsReceivedSearch(
			@Observes @CDIEvent(MenuItemBean.OMP_ACKNOWLEDGEMENT_DOCUMENTS_RECEIVED) final ParameterDTO parameters){
		BeanItemContainer<SelectValue> classification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CLASSIFICATION);
		view.setViewOMPDocumentUploadProcess(OMPProcessOmpAcknowledgementDocumentsView.class, classification,true);
	}
	
    protected void showOmpAckClaimProcessor(
			@Observes @CDIEvent(OMPMenuPresenter.OMP_PROCESS_ACK_DETAIL_PAGE) final ParameterDTO parameters) {
		//PreauthDTO dto = (PreauthDTO) parameters.getPrimaryParameter();
		
    	OMPProcessOmpAcknowledgementDocumentsTableDTO dto = (OMPProcessOmpAcknowledgementDocumentsTableDTO) parameters.getPrimaryParameter();
		Long rodkey = dto.getRodkey();
		String intimationNo = dto.getIntimationNo();
		String userName =dto.getUserName();
		OMPClaimProcessorDTO claimProcessorDTO = new OMPClaimProcessorDTO();
		claimProcessorDTO.setDbOutArray(dto.getDbOutArray());
		//OMPReimbursement reimbursement = ompIntimationService.getReimbursementByKey(rodkey);
		OMPClaim claimsByIntimationNumber = ompClaimService.getClaimsByIntimationNumber(intimationNo);
		
		if(claimsByIntimationNumber.getStatus()!= null){
			claimProcessorDTO.setStatusKey(claimsByIntimationNumber.getStatus().getKey());
		}
		
		NewIntimationDto newIntimationDto = ompIntimationService
				.getIntimationDto(claimsByIntimationNumber.getIntimation());
		ClaimDto claimDTO = OMPClaimMapper.getInstance().getClaimDto(claimsByIntimationNumber);
		if (claimsByIntimationNumber.getIntimation().getPolicy().getHomeOfficeCode() != null) {
			 List<MasOmbudsman> ombudsmanOfficeList = getOmbudsmanOffiAddrByPIOCode(claimsByIntimationNumber.getIntimation().getPolicy().getHomeOfficeCode());
			 if(ombudsmanOfficeList !=null && !ombudsmanOfficeList.isEmpty()){
				 claimDTO.setOmbudsManAddressList(ombudsmanOfficeList);
			 }
		 }
		claimProcessorDTO.setClaimDto(claimDTO);
		claimProcessorDTO.setNewIntimationDto(newIntimationDto);
		claimProcessorDTO.getReceiptOfDocumentsDTO().setNewIntimationDTO(newIntimationDto);
		setClaimDetails(rodkey, userName, claimProcessorDTO,claimsByIntimationNumber, newIntimationDto);
		
		List<OMPDocAcknowledgement> acknowledgeByClaimKey = ompIntimationService.getAcknowledgetDetailsByClaimKey(claimsByIntimationNumber.getKey());
		if(acknowledgeByClaimKey!=null && acknowledgeByClaimKey.isEmpty()){
			if(claimsByIntimationNumber!=null && claimsByIntimationNumber.getStatus()!=null){
				Status statuObj =claimsByIntimationNumber.getStatus();
				claimProcessorDTO.setStatus(statuObj.getProcessValue());
			}
		}
//		List<OMPReimbursement> rembursementByClaimKey = ompIntimationService.getRembursementDetailsByClaimKey(claimsByIntimationNumber.getKey());
			List<OMPClaimCalculationViewTableDTO> claimcalculationDto = new ArrayList<OMPClaimCalculationViewTableDTO>();
			if(acknowledgeByClaimKey != null && acknowledgeByClaimKey.size()>0){
			for (OMPDocAcknowledgement ompreimbursement : acknowledgeByClaimKey) {
				OMPClaimCalculationViewTableDTO claimCalulation = new OMPClaimCalculationViewTableDTO();
				setAknReimbursementDetails(claimProcessorDTO, claimCalulation,ompreimbursement,claimsByIntimationNumber);
				if(acknowledgeByClaimKey!=null && acknowledgeByClaimKey.size()>0){
					claimCalulation.setIsReadOnly(Boolean.TRUE);
						claimProcessorDTO.setIsMutipleRod(Boolean.TRUE);
				}
				/*if(rembursementByClaimKey!=null && rembursementByClaimKey.size()==1 
						&& ompreimbursement.getSendForApproverFlg()!=null && ompreimbursement.getSendForApproverFlg().equalsIgnoreCase("Y")){
						claimProcessorDTO.setIsMutipleRod(Boolean.TRUE);
				}*/
				MastersEvents event = claimsByIntimationNumber.getEvent();
				if(event!=null && ompreimbursement!= null){
					String eventCode = event.getEventCode();
//					Double balanceSI = reimbursementService.getBalanceSI(ompreimbursement,claimsByIntimationNumber,eventCode);
//					System.out.println(balanceSI);
//					claimProcessorDTO.setBalanceSI(balanceSI);
					OMPIntimation intimation = ompreimbursement.getClaim().getIntimation();
					Policy policy = ompreimbursement.getClaim().getIntimation().getPolicy();
					Product product = policy.getProduct();
					String plan = intimation.getInsured().getPlan();
					Double insuredSumInsured = policy.getTotalSumInsured();
					Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(policy.getKey(), eventCode);
					Map<String, Double> ompDeductible = dbCalculationService.getOmpDeductible(product.getKey(), plan, sumInsured, event.getEventCode(),insuredSumInsured);
					Double deductibles = (Double)ompDeductible.get(SHAConstants.DEDUCTIBLES);
					/*if(ompreimbursement.getDeductbleUsd()!=null){
						claimCalulation.setDeduction(ompreimbursement.getDeductbleUsd().doubleValue());
						claimProcessorDTO.setDeductibles(ompreimbursement.getDeductbleUsd().doubleValue());
						claimCalulation.setDeductiblesOriginal(ompreimbursement.getDeductbleUsd().doubleValue());
					}else{
						claimCalulation.setDeduction(deductibles);
						claimProcessorDTO.setDeductibles(deductibles);
						claimProcessorDTO.setDeductiblesOriginal(deductibles);
					}
					*/claimProcessorDTO.setProductCode(product.getCode());
//					claimProcessorDTO.setSumInsured(balanceSI);
				}
//				if(ompreimbursement.getStatus() != null){
//					claimCalulation.setProcessorStatus(ompreimbursement.getStatus().getKey());
//				}
				MastersValue docReceivedFrm = ompreimbursement.getDocumentReceivedFromId();
				SelectValue docReceivedFrmMast = new SelectValue();
				if(docReceivedFrm != null){
					docReceivedFrmMast.setId(docReceivedFrm.getKey());
					docReceivedFrmMast.setValue(docReceivedFrm.getValue());				
				}
				claimCalulation.setDocRecivedFrm(docReceivedFrmMast);
				
				// Upload doc
				if(ompreimbursement.getAcknowledgeNumber()!=null){
					ReceiptOfDocumentsDTO receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
					List<UploadDocumentDTO> uploadDocsList = new ArrayList<UploadDocumentDTO>();
					List<OMPDocumentDetails> documentDetailsByRodNumber = ompRodBillEntryService.getDocumentsByAckNumber(ompreimbursement.getAcknowledgeNumber());
					if(documentDetailsByRodNumber!=null){
					for (OMPDocumentDetails ompDocumentDetails : documentDetailsByRodNumber) {
						UploadDocumentDTO documentDTO = new UploadDocumentDTO();
						documentDTO.setRodNo(ompDocumentDetails.getReimbursementNumber());
						documentDTO.setRodKey(ompreimbursement.getRodKey());
						documentDTO.setFileName(ompDocumentDetails.getFileName());
						documentDTO.setFileTypeValue(ompDocumentDetails.getFileType());
						if(ompDocumentDetails.getDocumentToken()!=null){
							documentDTO.setDmsDocToken(ompDocumentDetails.getDocumentToken().toString());
						}
						documentDTO.setDmsToken(ompDocumentDetails.getDocumentToken());
						documentDTO.setIntimationNo(ompreimbursement.getClaim().getIntimation().getIntimationId());
//						documentDTO.setRodKey(ompreimbursement.getRodKey());
//						documentDTO.setRodNo(ompreimbursement.getRodNumber());
						documentDTO.setDocumentTypeValue(ompDocumentDetails.getDocumentType());
						documentDTO.setDocReceivedDate(ompDocumentDetails.getDocRecievedDate());
						documentDTO.setReceivStatusValue(ompDocumentDetails.getRecievedStatus());
						documentDTO.setNoOfItems(ompDocumentDetails.getNoOfDoc());
						documentDTO.setRemarks(ompDocumentDetails.getRemarks());
						if(ompDocumentDetails.getCreatedBy() != null){
							String employeeName = "";
							String modifyEmployeeName = "";
							TmpEmployee employee =  masterService.getUserLoginDetail(ompDocumentDetails.getCreatedBy());
							if(employee != null){
								if(employee.getEmpFirstName() != null){
									employeeName = employeeName+employee.getEmpFirstName();
								}
								if(employee.getEmpMiddleName() != null){
									employeeName = employeeName+employee.getEmpMiddleName();
								}
								if(employee.getEmpLastName() != null){
									employeeName = employeeName+employee.getEmpLastName();
								}
							}
							modifyEmployeeName = modifyEmployeeName+ompDocumentDetails.getCreatedBy()+" - "+employeeName;
							documentDTO.setUploadedBy(modifyEmployeeName);
						}
						if(ompDocumentDetails.getCreatedDate()!=null){
							String dateWithoutTime = SHAUtils.formateDateForHistory(ompDocumentDetails.getCreatedDate());
							documentDTO.setUpdatedOn(dateWithoutTime);
						}
						documentDTO.setAckDocumentSource(ompDocumentDetails.getDocumentSource());
						documentDTO.setAcknowledgementNo(ompDocumentDetails.getAcknowledgementNumber());
						uploadDocsList.add(documentDTO);
					}}
					receiptOfDocumentsDTO.setUploadDocsList(uploadDocsList);
					claimCalulation.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
				}
				
				claimcalculationDto.add(claimCalulation);
			}
			claimProcessorDTO.setClaimCalculationViewTable(claimcalculationDto);
		}else{
			Long policyKey = claimsByIntimationNumber.getIntimation().getPolicy().getKey();
			Double sumInsured = dbCalculationService.getOmpInsuredSumInsured(policyKey, claimsByIntimationNumber.getEvent().getEventCode());
			Map balanceSIMap = dbCalculationService.getOmpBalanceSI(policyKey , claimsByIntimationNumber.getIntimation().getInsured().getKey() , 
					claimsByIntimationNumber.getKey(),null, sumInsured,claimsByIntimationNumber.getIntimation().getKey(), claimsByIntimationNumber.getEvent().getEventCode());
			Double balanceSI = (Double)balanceSIMap.get(SHAConstants.TOTAL_BALANCE_SI);
			claimProcessorDTO.setSumInsured(balanceSI);
			claimProcessorDTO.setBalanceSI(balanceSI);
		}
		
			
		String productCode = claimsByIntimationNumber.getProductCode();
		Product productByProductCode = masterService.getProductByProductCode(productCode);
		
		BeanItemContainer<SelectValue> classification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_CLASSIFICATION);
		BeanItemContainer<SelectValue> subClassification = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_SUB_CLASSIFICATION);
		BeanItemContainer<SelectValue> paymentTo = masterService.getMastersValuebyTypeCodeOnStaatus(SHAConstants.OMP_OMP_PAYTO);
		BeanItemContainer<SelectValue> paymentMode = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_PAY_MODE);
		BeanItemContainer<SelectValue> eventCode = masterService.getListMasterEventByProduct(productByProductCode.getKey());
		BeanItemContainer<SelectValue> currencyValue = masterService.getListMasterCurrencyValue();
		BeanItemContainer<SelectValue> negotiatorName = masterService.getNegotiationNamesAll();
		BeanItemContainer<SelectValue> rodClaimTypeContainer = masterService.getMasterValueByReference(ReferenceTable.CLAIM_TYPE);
		BeanItemContainer<SelectValue> documentRecievedFrom = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_RECFRM);
		BeanItemContainer<SelectValue> documentType = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_TYPE);
		BeanItemContainer<SelectValue> country = masterService.getCountryValue();
		BeanItemContainer<SelectValue> documentTypeRecipet = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_DOC_TYPE_MODE);
		
		claimProcessorDTO.setClassificationContainer(classification);
		claimProcessorDTO.setSubClassificationContainer(subClassification);
		claimProcessorDTO.setCurrencyValueContainer(currencyValue);
		claimProcessorDTO.setDocumentRecievedFromContainer(documentRecievedFrom);
		claimProcessorDTO.setPaymentToContainer(paymentTo);
		claimProcessorDTO.setPaymentModeContainer(paymentMode);
		claimProcessorDTO.setRodClaimTypeContainer(rodClaimTypeContainer);
		claimProcessorDTO.setPolicy(claimsByIntimationNumber.getIntimation().getPolicy());
		claimProcessorDTO.getNewIntimationDto().setTpaIntimationNumber(claimsByIntimationNumber.getIntimation().getTpaIntimationNumber());
		claimProcessorDTO.setCgOption(claimsByIntimationNumber.getCgOption());
		claimProcessorDTO.setCgDate(claimsByIntimationNumber.getDateOfCashGuarantee());
		claimProcessorDTO.setCgApprovedAmt(claimsByIntimationNumber.getCgAmount());
		claimProcessorDTO.setCgRemarks(claimsByIntimationNumber.getCgRemarks());
		claimProcessorDTO.setModeOfReceiptContainer(documentTypeRecipet);
		
		view.setOmpClaimProcessorAknview(OMPAcknowledgementDocumentsPageWizard.class,claimProcessorDTO, classification, subClassification,paymentTo,
				paymentMode,eventCode,currencyValue,negotiatorName,rodClaimTypeContainer,documentRecievedFrom,documentType,country);
	}
    
    private void setAcknowledgeDetails(OMPClaimProcessorDTO claimProcessorDTO,
			OMPClaimCalculationViewTableDTO claimCalulation,
			OMPDocAcknowledgement ompreimbursement) {
		// TODO Auto-generated method stub
		
	}

	private void setClaimAknDetails(Long rodkey, String userName,OMPClaimProcessorDTO claimProcessorDTO,OMPClaim claimsByIntimationNumber,
			NewIntimationDto newIntimationDto) {
		claimProcessorDTO.setNewIntimationDto(newIntimationDto);
		claimProcessorDTO.setIntimationId(newIntimationDto.getIntimationId());
		ClaimDto claimDTO = OMPClaimMapper.getInstance().getClaimDto(claimsByIntimationNumber);
		if (claimsByIntimationNumber.getIntimation().getPolicy().getHomeOfficeCode() != null) {
			 List<MasOmbudsman> ombudsmanOfficeList = getOmbudsmanOffiAddrByPIOCode(claimsByIntimationNumber.getIntimation().getPolicy().getHomeOfficeCode());
			 if(ombudsmanOfficeList !=null && !ombudsmanOfficeList.isEmpty()){
				 claimDTO.setOmbudsManAddressList(ombudsmanOfficeList);
			 }
		 }
		claimProcessorDTO.setClaimDto(claimDTO);
		claimProcessorDTO.setRodKey(rodkey);
		claimProcessorDTO.setUserName(userName);
		claimProcessorDTO.setInrConversionRate(claimsByIntimationNumber.getInrConversionRate());
		claimProcessorDTO.setProvisionAmt(claimsByIntimationNumber.getDollarInitProvisionAmount());
		claimProcessorDTO.setInrtotal(claimsByIntimationNumber.getInrTotalAmount());
		claimProcessorDTO.setAdmissionDate(claimsByIntimationNumber.getDataOfAdmission());
		claimProcessorDTO.setDischargeDate(claimsByIntimationNumber.getDataOfDischarge());
		claimProcessorDTO.setPlaceEvent(claimsByIntimationNumber.getPlaceOfAccident());
		claimProcessorDTO.setLossTime(claimsByIntimationNumber.getLossTime());
		claimProcessorDTO.setLossOrDelay(claimsByIntimationNumber.getPlaveOfLossOrDelay());
		claimProcessorDTO.setPlaceOfVisit(claimsByIntimationNumber.getPlaveOfVisit());
		claimProcessorDTO.setPolicy(claimsByIntimationNumber.getIntimation().getPolicy());
		if(claimsByIntimationNumber.getLegalOpinionFlag()!= null&& claimsByIntimationNumber.getLegalOpinionFlag().equalsIgnoreCase("Y")){
			claimProcessorDTO.setIsLegalFlag(Boolean.TRUE);
		}else{
			claimProcessorDTO.setIsLegalFlag(Boolean.FALSE);
		}
		if(claimsByIntimationNumber.getHospital()!=null){
			OMPHospitals hospitalDetailsBykey = ompIntimationService.getHospitalDetailsBykey(claimsByIntimationNumber.getHospital());
			if(hospitalDetailsBykey!=null){
				SelectValue selectValue = new SelectValue();
				selectValue.setId(hospitalDetailsBykey.getKey());
				selectValue.setValue(hospitalDetailsBykey.getName());
				claimProcessorDTO.setHospital(selectValue);
			}
		}
		
		claimProcessorDTO.setLossDetails(claimsByIntimationNumber.getLossDetails());
		claimProcessorDTO.setAilmentLoss(claimsByIntimationNumber.getAilmentLoss());
		claimProcessorDTO.setLossOfDate(claimsByIntimationNumber.getLossDateTime());
		claimProcessorDTO.setHospName(claimsByIntimationNumber.getHospitalName());
		claimProcessorDTO.setHospCity(claimsByIntimationNumber.getCityName());
		if(claimsByIntimationNumber.getCountryId()!=null){
			SelectValue countryValueByKey = masterService.getCountryValueByKey(claimsByIntimationNumber.getCountryId());
			claimProcessorDTO.setHospCountry(countryValueByKey);
		}
		if(claimsByIntimationNumber.getClaimType()!= null && claimsByIntimationNumber.getClaimType().getValue()!= null){
			MastersValue claimValue = claimsByIntimationNumber.getClaimType();
			SelectValue claimValue2 = new SelectValue();
			claimValue2.setId(claimValue.getKey());
			claimValue2.setValue(claimValue.getValue());
			claimProcessorDTO.setClaimTypeValue(claimValue2);
		}
		
		
		MastersEvents eventCode1 =claimsByIntimationNumber.getEvent();
		SelectValue eventCodeMast = new SelectValue();
		if(eventCode1 != null){
			eventCodeMast.setId(eventCode1.getKey());
			eventCodeMast.setValue(eventCode1.getEventDescription());
			claimProcessorDTO.setEventCode(eventCodeMast);
			claimProcessorDTO.setEventdescription(eventCode1.getEventDescription());
		}
		
		if(claimsByIntimationNumber.getClaimType()!=null){
			
			MastersValue cmbclaimType = claimsByIntimationNumber.getClaimType();
			SelectValue claimType = new SelectValue();
			claimType.setId(cmbclaimType.getKey());
			claimType.setValue(cmbclaimType.getValue());
			if(claimType!=null){
				if(ReferenceTable.CASHLESS_CLAIM.equalsIgnoreCase(claimType.getValue())){
//					optionClaimtype.setValue("Cashless");
					claimProcessorDTO.setClaimType(Boolean.TRUE);
					claimProcessorDTO.setClaimTypeValue(claimType);
					claimProcessorDTO.setIsCashless(Boolean.TRUE);
				}else{
//					optionClaimtype.setValue("Reimbursement");
					claimProcessorDTO.setClaimTypeValue(claimType);
					claimProcessorDTO.setClaimType(Boolean.FALSE);
					claimProcessorDTO.setIsCashless(Boolean.FALSE);
				}
			}
		}
		
		claimProcessorDTO.setNonHospitalisationFlag(claimsByIntimationNumber.getNonHospitalisationFlag());
		if(claimsByIntimationNumber.getNonHospitalisationFlag()!=null ){
			claimProcessorDTO.setHospTypeBooleanval(Boolean.TRUE);
		}
		if(claimsByIntimationNumber.getIntimation().getInsured()!=null){
			claimProcessorDTO.setPlan(claimsByIntimationNumber.getIntimation().getInsured().getPlan());
			claimProcessorDTO.setAge(claimsByIntimationNumber.getIntimation().getInsured().getInsuredAge());
		}
		if(claimsByIntimationNumber.getIntimation().getPolicy()!=null){
			Policy policy = claimsByIntimationNumber.getIntimation().getPolicy();
			Product product = policy.getProduct();
			claimProcessorDTO.setProductCode(product.getCode());
		}
		claimProcessorDTO.setIsOnLoad(Boolean.TRUE);
	}
    
    private void setAknReimbursementDetails(
    		OMPClaimProcessorDTO claimProcessorDTO,
    		OMPClaimCalculationViewTableDTO claimCalulation,
    		OMPDocAcknowledgement ompAcknowledgement,OMPClaim claimsObj) {
//    	claimCalulation.setIsReadOnly(Boolean.TRUE);
		claimCalulation.setAckKey(ompAcknowledgement.getKey());
		claimProcessorDTO.setAckKey(ompAcknowledgement.getKey());
		claimCalulation.setAcknumber(ompAcknowledgement.getAcknowledgeNumber());
		claimCalulation.setAckContactNumber(ompAcknowledgement.getInsuredContactNumber());
		claimCalulation.setEmailId(ompAcknowledgement.getInsuredEmailId());
		claimCalulation.setAckDocReceivedDate(ompAcknowledgement.getDocumentReceivedDate());
		if(ompAcknowledgement!=null && ompAcknowledgement.getRodKey()==null){
			Status statuObj =ompAcknowledgement.getStatus();
			claimCalulation.setStatus(statuObj.getProcessValue());
			
		}else{
			if(ompAcknowledgement!=null && ompAcknowledgement.getRodKey()!=null){
				OMPReimbursement ompReimObj = ompIntimationService.getReimbursementByKey(ompAcknowledgement.getRodKey());
				if(ompReimObj!=null){
					claimCalulation.setRodnumber(ompReimObj.getRodNumber());
					claimCalulation.setLateDocReceivedDate(ompReimObj.getLastDocRecDate());
					Status statuObj =ompReimObj.getStatus();
					if(ompReimObj.getStatus() != null){
						claimProcessorDTO.setRodStatus(ompReimObj.getStatus().getKey());
						claimCalulation.setStatus(statuObj.getProcessValue());
					}
				}
				
			}
		}
		
		if(ompAcknowledgement.getDocumentReceivedFromId()!= null && ompAcknowledgement.getDocumentReceivedFromId().getValue()!= null){
			MastersValue classValue = ompAcknowledgement.getDocumentReceivedFromId();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setDocRecivedFrm(seleCValue);
		}
		
		if(ompAcknowledgement.getModeOfReceiptId()!= null && ompAcknowledgement.getModeOfReceiptId().getValue()!= null){
			MastersValue classValue = ompAcknowledgement.getModeOfReceiptId();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setModeOfReceipt(seleCValue);
		}
		if(ompAcknowledgement.getClassificationId()!= null && ompAcknowledgement.getClassificationId().getValue()!= null){
			MastersValue classValue = ompAcknowledgement.getClassificationId();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setClassification(seleCValue);
		}
		if(ompAcknowledgement.getSubClassificationId()!= null && ompAcknowledgement.getSubClassificationId().getValue()!= null){
			MastersValue classValue = ompAcknowledgement.getSubClassificationId();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setSubClassification(seleCValue);
		}
		if(ompAcknowledgement.getCategoryId()!= null && ompAcknowledgement.getCategoryId().getValue()!= null){
			MastersValue classValue = ompAcknowledgement.getCategoryId();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setCategory(seleCValue);
		}
		if(ompAcknowledgement.getDocumentReceivedFromId()!= null && ompAcknowledgement.getDocumentReceivedFromId().getValue()!= null){
			MastersValue docValue = ompAcknowledgement.getDocumentReceivedFromId();
			SelectValue docRecValue = new SelectValue();
			docRecValue.setId(ompAcknowledgement.getDocumentReceivedFromId().getKey());
			docRecValue.setValue(ompAcknowledgement.getDocumentReceivedFromId().getValue());
			claimCalulation.setDocRecivedFrm(docRecValue);
		}
		/*if(ompreimbursement.getRodClaimType()!= null && ompreimbursement.getRodClaimType().getValue()!= null){
			MastersValue classValue = ompreimbursement.getRodClaimType();
			SelectValue seleCValue = new SelectValue();
			seleCValue.setId(classValue.getKey());
			seleCValue.setValue(classValue.getValue());
			claimCalulation.setRodClaimType(seleCValue);
		}
		Currency currencyType = ompreimbursement.getCurrencyTypeId();
		if(currencyType!=null){
		Long currencyTypeId = currencyType.getKey();
		SelectValue currencyValue = new SelectValue();
		currencyValue.setId(currencyTypeId);
		currencyValue.setValue(currencyType.getCurrencyCode());
		claimCalulation.setCurrencyType(currencyValue);
		}
		Currency currency = rodBillentryService.getOMPCurrency(currencyTypeId);
		if(currency != null){
			reimbursement.setCurrencyTypeId(currency);
		}
		
		claimCalulation.setCurrencyrate(ompreimbursement.getInrTotalAmount());
		claimCalulation.setConversionValue(ompreimbursement.getInrConversionRate());
		if(ompreimbursement.getNegotiationDone()!= null){
			SelectValue negodone = new SelectValue();
			if(ompreimbursement.getNegotiationDone().equalsIgnoreCase("Y")){
				negodone.setId(1L);
				negodone.setValue("Yes");
			}else{
				negodone.setId(2L);
				negodone.setValue("No");
			}
			claimCalulation.setNegotiationDone(negodone);
		}
		if(ompreimbursement.getRejectFlg()!=null ){
			if(ompreimbursement.getRejectFlg().equalsIgnoreCase("Y")){
				claimCalulation.setIsreject(Boolean.TRUE);
				claimCalulation.setIsReadOnly(Boolean.TRUE);
				claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
			}else{
				claimCalulation.setIsreject(Boolean.FALSE);
			}
		}
		claimCalulation.setReject(ompreimbursement.getRejectFlg());
		claimCalulation.setRejectionRemarks(ompreimbursement.getRejectionRemarks());
		claimCalulation.setReconsiderFlag(ompreimbursement.getReconsiderFlag());
		List<SelectValue> selectedRejId = ompClaimService.getRejectionIdByRodKey(ompreimbursement.getKey());
		claimCalulation.setRejectionIds(selectedRejId);
		if(claimProcessorDTO.getDescription()!=null && claimProcessorDTO.getDescription().equalsIgnoreCase("Approve")){
			claimCalulation.setSendforApprover(ompreimbursement.getApprovedFlg());
		}else{
			claimCalulation.setSendforApprover(ompreimbursement.getSendForApproverFlg());
		}
		SelectValue appValue = new SelectValue();
		if(ompreimbursement.getViewForApprover()!= null && ompreimbursement.getViewForApprover().equalsIgnoreCase("Y")){
			    appValue.setId(0l);
				appValue.setValue("Yes");
				if(ompreimbursement.getSendForApproverFlg()!=null ){
					if(claimProcessorDTO.getDescription()!=null){
						if(!claimProcessorDTO.getDescription().equalsIgnoreCase("Approve")){
							if(ompreimbursement.getSendForApproverFlg().equalsIgnoreCase("Y")){
								claimCalulation.setIsReadOnly(Boolean.TRUE);
								claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
							}
						}
					}else{
						if(ompreimbursement.getSendForApproverFlg().equalsIgnoreCase("Y")){
							claimCalulation.setIsReadOnly(Boolean.TRUE);
							claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
						}
					}
			}else{
				if(claimProcessorDTO.getDescription()!=null){
					if(claimProcessorDTO.getDescription().equalsIgnoreCase("Approve")){
						claimCalulation.setIsReadOnly(Boolean.TRUE);
						claimCalulation.setIsReadOnlyRecoverable(Boolean.FALSE);
					}
				}
			}if(claimProcessorDTO.getDescription()!= null){
				if(claimProcessorDTO.getDescription().equalsIgnoreCase("Approve")){
					if(ompreimbursement.getFaSubmitFlg()!= null && ompreimbursement.getFaSubmitFlg().equalsIgnoreCase("Y")){
						claimCalulation.setIsReadOnly(Boolean.TRUE);
						claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
					}
					if(ompreimbursement.getApprovedFlg()!= null && ompreimbursement.getApprovedFlg().equalsIgnoreCase("Y")){
						claimCalulation.setIsReadOnly(Boolean.TRUE);
						claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
					}
					if(ompreimbursement.getSendForApproverFlg()!=null ){
						if(!ompreimbursement.getSendForApproverFlg().equalsIgnoreCase("Y")){
							claimCalulation.setIsReadOnly(Boolean.TRUE);
							claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
						}
					}else{
						claimCalulation.setIsReadOnly(Boolean.TRUE);
						claimCalulation.setIsReadOnlyRecoverable(Boolean.FALSE);
					}
				}
			}
			}else{
				appValue.setId(2L);
				appValue.setValue("No");
			}
			claimCalulation.setViewforApprover(appValue);
		*//*if(ompreimbursement.getApprovedFlg()!= null && ompreimbursement.getApprovedFlg().equalsIgnoreCase("Y")){
			claimCalulation.setIsReadOnly(Boolean.TRUE);
		}else{
			claimCalulation.setIsReadOnly(Boolean.FALSE);
		}*/
//		claimCalulation.setLateDocReceivedDate(ompAcknowledgement.getDocumentReceivedDate());
			
//			claimCalulation.setFinalApprovedAmtDollor(ompreimbursement.getFinalApprovedAmtUsd());
			
		/*claimCalulation.setFinalApprovedAmtInr(ompreimbursement.getFinalApprovedAmtInr() != null ? Math.round(ompreimbursement.getFinalApprovedAmtInr()) : 0d);
		claimCalulation.setAmountInWords(SHAUtils.getParsedAmount(claimCalulation.getFinalApprovedAmtInr()));
		claimCalulation.setNotinClaimCount(ompreimbursement.getNotInClaimCountFlg());
		claimCalulation.setAcknumber(ompreimbursement.getRodNumber());//PANNEER
		claimCalulation.setAfternegotiation(ompreimbursement.getAmountAfterNego());
		claimCalulation.setProcessorRemarks(ompreimbursement.getRemarksProcessor());
		claimCalulation.setReasonForApproval(ompreimbursement.getReasonSuggestApprover());
		if(ompreimbursement.getRejectionCategoryId()!=null){
			SelectValue selectValue = new SelectValue();
			selectValue.setId(ompreimbursement.getRejectionCategoryId().getKey());
			selectValue.setValue(ompreimbursement.getRejectionCategoryId().getValue());
			claimCalulation.setReasonForRejectionRemarks(selectValue);
		}
		claimCalulation.setSubmit(ompreimbursement.getFaSubmitFlg());
		if(ompreimbursement.getNegotiationList()!=null){
			OMPNegotiation ompNegotiationbyKey = ompRodBillEntryService.getOMPNegotiationbyKey(ompreimbursement.getNegotiationList());
			if(ompNegotiationbyKey!=null){
				double total = ompNegotiationbyKey.getExpenseAmountusd() + ompNegotiationbyKey.getHandlingChargsUsd();
			SelectValue select = new SelectValue();
			select.setId(ompreimbursement.getNegotiationList());
			select.setValue(ompNegotiationbyKey.getRodKey().getRodNumber() +"-"+ total);
			claimCalulation.setSelect(select);
			}
		}				
		List<OMPNewRecoverableTableDto> recovList = new ArrayList<OMPNewRecoverableTableDto>();
		OMPNewRecoverableTableDto  recoverableDto = new OMPNewRecoverableTableDto();
		recoverableDto.setDateofRecovery(ompreimbursement.getRecoveredDate());
		if(ompreimbursement.getRecoveredAmountInr()!= null){
			double recoinr = ompreimbursement.getRecoveredAmountInr().doubleValue();
			recoverableDto.setAmountRecoveredInr(recoinr);
		}
		if(ompreimbursement.getRecoveredAmountUsd()!= null){
			double recoUsd = ompreimbursement.getRecoveredAmountUsd().doubleValue();
			recoverableDto.setAmountRecoveredUsd(recoUsd);
		}
		recoverableDto.setRodKey(ompreimbursement.getKey());
		recoverableDto.setRemarks(ompreimbursement.getRemarks());
		recoverableDto.setSendToAccounts(ompreimbursement.getSendToAccounts());
		if(ompreimbursement.getSendToAccounts()!=null && ompreimbursement.getSendToAccounts().equalsIgnoreCase("Y")){
			claimCalulation.setIsReadOnlyRecoverable(Boolean.TRUE);
		}
		recovList.add(recoverableDto);
		*/
		List<OMPPaymentDetailsTableDTO> paymentListDto = new ArrayList<OMPPaymentDetailsTableDTO>();
		
		OMPPaymentDetailsTableDTO paymentDto = new OMPPaymentDetailsTableDTO();
		BeanItemContainer<SelectValue> paymentTo = masterService.getMastersValuebyTypeCodeOnStaatus(SHAConstants.OMP_OMP_PAYTO);
		BeanItemContainer<SelectValue> paymentMode = masterService.getListMasterValuebyTypeCode(SHAConstants.OMP_PAY_MODE);
		BeanItemContainer<SelectValue> currencyValue = new BeanItemContainer<SelectValue>(SelectValue.class);
		BeanItemContainer<SelectValue> inrCurrencyValue = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		/*Currency currencyTypeId =null;
		if(ompreimbursement.getPayeeCurrencyTypeId()!=null){
			currencyTypeId = ompreimbursement.getPayeeCurrencyTypeId();	
		}else{
			currencyTypeId = ompreimbursement.getCurrencyTypeId();	
		}*/
				Currency ompCurrency = ompRodBillEntryService.getOMPCurrency(107L);
				SelectValue selectValue1 = new SelectValue();
				selectValue1.setId(ompCurrency.getKey());
				selectValue1.setValue(ompCurrency.getCurrencyCode());
				currencyValue.addBean(selectValue1);
				
				
				Currency ompCurrency1 = ompRodBillEntryService.getOMPCurrency(177L);
				SelectValue selectValue3 = new SelectValue();
				selectValue3.setId(ompCurrency1.getKey());
				selectValue3.setValue(ompCurrency1.getCurrencyCode());
				inrCurrencyValue.addBean(selectValue3);
				//paymentDto.setCurrency(selectValue1);
				claimCalulation.setInrCurrencyValueContainer(inrCurrencyValue);
		
		/*if(currencyTypeId!=null){
			SelectValue selectValue = new SelectValue();
			selectValue.setId(currencyTypeId.getKey());
			selectValue.setValue(currencyTypeId.getCurrencyCode());
			currencyValue.addBean(selectValue);
			paymentDto.setCurrency(selectValue);
		}*/
		
		claimCalulation.setCurrencyValueContainer(currencyValue);
		claimCalulation.setPaymentToContainer(paymentTo);
		claimCalulation.setPaymentModeContainer(paymentMode);
/*
		paymentDto.setPanNo(ompreimbursement.getPanNumber());
		paymentDto.setPayableAt(ompreimbursement.getPayableAt());
		paymentDto.setPayeeNameStr(ompreimbursement.getPayeeName());
		paymentDto.setEmailId(ompreimbursement.getEmailId());
		SelectValue paymentValue = null; 
		MastersValue paymentToMast = ompreimbursement.getPaymentTo();
		if(paymentToMast != null && paymentToMast.getKey() != null){
			paymentValue = new SelectValue();
			paymentValue.setId(paymentToMast.getKey());
			paymentValue.setValue(paymentToMast.getValue());
			paymentDto.setPaymentTo(paymentValue);
		}
		if(ompreimbursement.getPaymentModeId()!= null){
			
			MastersValue master = ompRodBillEntryService.getMaster(ompreimbursement.getPaymentModeId());
			SelectValue value = null;
			if(master!= null && master.getKey()!= null){
				value = new SelectValue();
				value.setId(master.getKey());
				value.setValue(master.getValue());
				paymentDto.setPayMode(value);
				
			}
		}
//				paymentDto.setPaymentStatus(ompreimbursement.getPatientStatus().getValue());
//*/		paymentListDto.add(paymentDto);
			/*List<OMPBenefitCover> benefitCoverList = ompRodBillEntryService.getOMPBenefitCoverByKey(ompAcknowledgement.getKey());
			if(benefitCoverList != null){
				
				if(benefitCoverList.get(0)!=null){
					OMPBenefitCover benefitCover = benefitCoverList.get(0);
					claimCalulation.setKey(benefitCover.getKey());
					if(benefitCover.getRodKey() != null){
						
						claimCalulation.setRodKey(benefitCover.getRodKey().getKey());
					}
					claimCalulation.setBillAmt(benefitCover.getBillAmount());
					claimCalulation.setAgreedAmt(benefitCover.getAgreedAmtDollar());
					claimCalulation.setAmtIn(benefitCover.getNetAmt());
					claimCalulation.setApprovedAmt(benefitCover.getApprovedAmountDollor());
//							ompClaimCalculationViewTableDTO.setCategory(benefitCover.get);
					ompClaimCalculationViewTableDTO.setDifferenceAmt(benefitCover.getDiffAmtDollar());
					ompClaimCalculationViewTableDTO.setExpenses(benefitCover.getExpenesesDollar());
					ompClaimCalculationViewTableDTO.setHandlingCharges(benefitCover.getHandlingChargesDollar());
					ompClaimCalculationViewTableDTO.setNegotiationCapping(benefitCover.getNegoFeeCapping());
					ompClaimCalculationViewTableDTO.setNegotiationClaimed(benefitCover.getNegoFeesClaimedDollar());
					ompClaimCalculationViewTableDTO.setNegotiationPayable(benefitCover.getNegoFeesClaimedInr());
					ompClaimCalculationViewTableDTO.setTotalExp(benefitCover.getTotalExpenceDollar());
					claimCalulation.setTotalAmtInr(benefitCover.getTotalAmtInr());
					claimCalulation.setTotalAmt(benefitCover.getTotalAmtPayBleDollar());
					claimCalulation.setCopayamount(benefitCover.getCopayAmount());
					if(benefitCover.getCopayPercentage()!= null){
						SelectValue copayvValue = new SelectValue();
							if(benefitCover.getCopayPercentage()==1L){
								copayvValue.setValue("0");
							}else{
								copayvValue.setValue("30");
							}
							copayvValue.setId(benefitCover.getCopayPercentage());
							claimCalulation.setCopay(copayvValue);
					}
					claimCalulation.setApprovedamountaftecopay(benefitCover.getApprAmtAftrCopya());
					
				}
			}*/
//			claimCalulation.setOmpRecoverableTableList(recovList);
			claimCalulation.setOmpPaymentDetailsList(paymentListDto);



			//Negotiation
			if(ompAcknowledgement.getKey()!=null){/*
					List<OMPNegotiation> ompNegotiation = ompRodBillEntryService.getOMPNegotiation(ompreimbursement.getKey());
					List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = new ArrayList<OMPNegotiationDetailsDTO>();
					if(ompNegotiation!=null){
						for (OMPNegotiation ompNegotiation2 : ompNegotiation) {
							OMPNegotiationDetailsDTO negotiationDetailsDTO = new OMPNegotiationDetailsDTO();
							negotiationDetailsDTO.setKey(ompNegotiation2.getKey());
							negotiationDetailsDTO.setAgreedAmount(ompNegotiation2.getAggredAmount());
							negotiationDetailsDTO.setApprovedAmt(ompNegotiation2.getApprovedAmount());
							negotiationDetailsDTO.setNameOfNegotiatior(ompNegotiation2.getNegotiatorName());
							negotiationDetailsDTO.setNegotiationCompletDate(ompNegotiation2.getNegotiationCompletedDate());
							negotiationDetailsDTO.setNegotiationRemarks(ompNegotiation2.getNegotiationRemarks());
							negotiationDetailsDTO.setNegotiationReqstDate(ompNegotiation2.getNegotiationRequestedDate());
							negotiationDetailsDTO.setDiffAmt(ompNegotiation2.getDiffAmountusd());
							negotiationDetailsDTO.setExpenseAmt(ompNegotiation2.getExpenseAmountusd());
							negotiationDetailsDTO.setHandlingCharges(ompNegotiation2.getHandlingChargsUsd());
							if(ompNegotiation2.getRodKey()!=null){
								negotiationDetailsDTO.setRodKey(ompNegotiation2.getRodKey().getKey());
							}
							if(ompNegotiation2.getClaim()!=null){
								negotiationDetailsDTO.setClaimKey(ompNegotiation2.getClaim().getKey());
							}
							negotiationDetailsDTOs.add(negotiationDetailsDTO);
						}
					}
					claimCalulation.setNegotiationDetailsDTOs(negotiationDetailsDTOs);
			*/}
			
			/*// Upload doc
			if(ompAcknowledgement.getAcknowledgeNumber()!=null){
				ReceiptOfDocumentsDTO receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
				List<UploadDocumentDTO> uploadDocsList = new ArrayList<UploadDocumentDTO>();
				List<OMPDocumentDetails> documentDetailsByRodNumber = ompRodBillEntryService.getDocumentsByIntimationNumber(claimsObj.getIntimation().getIntimationId());
				if(documentDetailsByRodNumber!=null){
				for (OMPDocumentDetails ompDocumentDetails : documentDetailsByRodNumber) {
					UploadDocumentDTO documentDTO = new UploadDocumentDTO();
					documentDTO.setRodNo(ompDocumentDetails.getReimbursementNumber());
					documentDTO.setRodKey(ompAcknowledgement.getRodKey());
					documentDTO.setFileName(ompDocumentDetails.getFileName());
					documentDTO.setFileTypeValue(ompDocumentDetails.getFileType());
					if(ompDocumentDetails.getDocumentToken()!=null){
						documentDTO.setDmsDocToken(ompDocumentDetails.getDocumentToken().toString());
					}
					documentDTO.setDmsToken(ompDocumentDetails.getDocumentToken());
					documentDTO.setIntimationNo(ompAcknowledgement.getClaim().getIntimation().getIntimationId());
					documentDTO.setRodKey(ompAcknowledgement.getRodKey());
//					documentDTO.setRodNo(ompreimbursement.getRodNumber());
					documentDTO.setDocumentTypeValue(ompDocumentDetails.getDocumentType());
					documentDTO.setDocReceivedDate(ompDocumentDetails.getDocRecievedDate());
					documentDTO.setReceivStatusValue(ompDocumentDetails.getRecievedStatus());
					documentDTO.setNoOfItems(ompDocumentDetails.getNoOfDoc());
					documentDTO.setRemarks(ompDocumentDetails.getRemarks());
					if(ompDocumentDetails.getCreatedBy() != null){
						String employeeName = "";
						String modifyEmployeeName = "";
						TmpEmployee employee =  masterService.getUserLoginDetail(ompDocumentDetails.getCreatedBy());
						if(employee != null){
							if(employee.getEmpFirstName() != null){
								employeeName = employeeName+employee.getEmpFirstName();
							}
							if(employee.getEmpMiddleName() != null){
								employeeName = employeeName+employee.getEmpMiddleName();
							}
							if(employee.getEmpLastName() != null){
								employeeName = employeeName+employee.getEmpLastName();
							}
						}
						modifyEmployeeName = modifyEmployeeName+ompDocumentDetails.getCreatedBy()+" - "+employeeName;
						documentDTO.setUploadedBy(modifyEmployeeName);
					}
					if(ompDocumentDetails.getCreatedDate()!=null){
						String dateWithoutTime = SHAUtils.formateDateForHistory(ompDocumentDetails.getCreatedDate());
						documentDTO.setUpdatedOn(dateWithoutTime);
					}
					uploadDocsList.add(documentDTO);
				}}
				receiptOfDocumentsDTO.setUploadDocsList(uploadDocsList);
				claimCalulation.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
			}*/
			
			claimProcessorDTO.setIsOnLoad(Boolean.TRUE);
	}
	
}
