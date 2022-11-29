package com.shaic.paclaim.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationDetailsUI;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.CashlessDetailsService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.domain.PreviousClaimMapper;
import com.shaic.newcode.wizard.dto.ClaimStatusDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings("serial")
@ViewInterface(PAClaimRegistrationView.class)
public class PAClaimRegistrationPresenter extends
		AbstractMVPPresenter<PAClaimRegistrationView> {

	public static final String SUBMIT_PA_CLAIM_CLICK = "submit_register_pa_claim";
	public static final String CLICK_PA_REGISTER_BUTTON = "click_pa_register_button";
	public static final String SUGGEST_PA_REJECTION = "Pa_Rejection_Suggested";
	public static final String SUGGEST_PA_REJECTION_VALUE_CHANGE = "Suggest Pa Rejection Value Changed";
	public static final String CANCEL_PA_CLAIM_REGISTRATION = "cancel_pa_claim_registration";
	public static final String CLICK_PA_GENERATE_COVERING_LETTER_BUTTON = "Click_Pa_Generate_Covering_Letter";
//	public static final String CLICK_HOMEPAGE_OR_REG_ANOTHER_CLAIM = "click_homepage_or_reg_another_claim";
	public static final String GET_PA_CLAIM = "get_pa_claim_details";
	public static final String SUBMIT_PA_CLAIM_REGISTRATION = "submit_pa_registration_of_claim";
	public static final String GET_PA_CURRENCY_MASTER = "get_pa_currency_master";
	public static final String GET_PREVIOUS_PA_CLAIMS = "get_pa_previous_claims";
//	public static final String GET_PA_BALANCE_SI = "get_pa_balance_sumInsured";
//	public static final String GET_PA_CPU_OBJECT = "get_pa_cpu_object";
//	public static final String GET_PA_SUB_LIMIT_LIST = "get_pa_sub_limit_list";
	public static final String CLICK_VIEW_PA_CLAIM_STATUS = "click_view_pa_claim_status";

	@EJB
	private PolicyService policyService;

	@EJB
	private ClaimService claimService;
	
	@EJB
	private InvestigationService investigationService;
	
	@EJB
	private PreauthService preauthService;

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dBCalculationService;
	
	@Inject
	private CashLessTableDetails cashLessTableDetails;

	@Inject
	private CashlessTable cashlessTable;

	@Inject
	private NewIntimationService newIntimationService;
	
	@Inject
	private RodIntimationDetailsUI rodIntimationDetailsObj;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private HospitalService hospitalService;
	
//	@Inject
//	private	DBCalculationService dbCalculationService;
	
	@Inject
	private ClaimStatusDto claimStatusDto;
	
	@Inject
	private CashlessDetailsService cashlessDetailsService;
	
	/*@EJB
	private MasterService masterService;*/
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService acknowledgementService;
	
	public static final String VALIDATE_CLAIM_REGISTRATION_USER_RRC_REQUEST = "validate_claim_registration_user_rrc_request";
	
	public static final String PROCESS_CLAIM_REGISTRATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES = "claim_registration_load_rrc_request_drop_down_values";
	
	public static final String PROCESS_CLAIM_REGISTRATION_SAVE_RRC_REQUEST_VALUES = "claim_registration_save_rrc_request_values";
	
	
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
	
	protected void viewPreviousClaimStatus(
			@Observes @CDIEvent(CLICK_VIEW_PA_CLAIM_STATUS) final ParameterDTO parameters) {
		
		PreviousClaimsTableDTO previousClaimDato = (PreviousClaimsTableDTO) parameters.getPrimaryParameter(); 
		
		Intimation intimation = intimationService.getIntimationByNo(previousClaimDato.getIntimationNumber());
		
//		NewIntimationDto newIntimationDto = intimationService.getIntimationDto(intimation);
//		
//		//NewIntimationDto newIntimationDto = policyService.newIntimationToIntimationDTO(intimation);
//		
//		Claim claim = claimService.getClaimsByIntimationNumber(intimation.getIntimationId()); 
//		
//		Hospitals hospital = hospitalService.getHospitalById(intimation.getHospital());
//		
//		CashlessDetailsDto cashlessDetailsDto = cashlessDetailsService
//				.getCashlessDetails(intimation.getKey());
//		
//		/*ClaimDtoOld claimDto = policyService.claimToClaimDTO(claim,
//				hospital);*/
//		ClaimDto claimDto = claimService.claimToClaimDTO(claim);
//		if(claimDto!=null){
//			claimStatusDto.setClaimDto(claimDto);
//			claimStatusDto.setNewIntimationDto(claimDto.getNewIntimationDto());
//			claimStatusDto.setCashlessDetailsDto(cashlessDetailsDto);
//			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(claimStatusDto, intimation.getPolicy()
//							.getActiveStatus() == null, cashLessTableDetails,
//					cashlessTable, cashLessTableMapper, newIntimationService, intimation);
//			
//			UI.getCurrent().addWindow(intimationStatus);
//		}
		
		if( intimation != null){
			
			viewSearchClaimStatus(intimation.getIntimationId());			
//			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(intimation.getIntimationId(),viewDetails);			
//			UI.getCurrent().addWindow(intimationStatus);
			
		}
		
		
	}
	
	private void viewSearchClaimStatus(String intimationNo) {
		
		final ViewTmpIntimation intimation = intimationService
				.searchbyIntimationNoFromViewIntimation(intimationNo);
		// Hospitals hospital =
		// hospitalService.searchbyHospitalKey(intimation
		// .getKey());
		ViewTmpClaim claim = claimService.getTmpClaimforIntimation(intimation
				.getKey());
		
		if(BPMClientContext.CLAIM_STATUS_PREMIA != null && BPMClientContext.CLAIM_STATUS_PREMIA.equalsIgnoreCase(SHAConstants.CLAIMS_VIEW_PLAN_B)){
			
			if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
				
				showClaimStatus(intimation, claim);                            // fetch claim status from galaxy
				
			}else{
				getViewClaimStatusFromPremia(intimation.getIntimationId());    // fetch claim status from premia end.
			}
			
		}else{
			showClaimStatus(intimation, claim);     //fetch claim status only from galaxy
		}

		
	}
	
	private void showClaimStatus(final ViewTmpIntimation viewIntimation,
			ViewTmpClaim claim) {
		
		if(claim != null){
		
		try {
			Long rodKey = null;
			List<Reimbursement> rodList = reimbursementService.getPreviousRODByClaimKey(claim.getKey());
			if(rodList != null && ! rodList.isEmpty()){
				Reimbursement rodObj = rodList.get(0);
				rodKey = rodObj.getKey();
			}
			EarlierRodMapper instance = EarlierRodMapper.getInstance();
			ClaimStatusRegistrationDTO registrationDetails = 
					instance.getRegistrationDetails(claim);
			 Intimation intimation =
			 intimationService.getIntimationByKey(claim.getIntimation().getKey());
			ViewClaimStatusDTO intimationDetails = EarlierRodMapper
					.getViewClaimStatusDto(intimation);
			Hospitals hospitals = hospitalService.getHospitalById(intimation
					.getHospital());
			getHospitalDetails(intimationDetails, hospitals);
			intimationDetails
					.setClaimStatusRegistrionDetails(registrationDetails);
			intimationDetails.setClaimKey(claim.getKey());
			String name = null != intimationService
					.getNewBabyByIntimationKey(intimationDetails
							.getIntimationKey()) ? intimationService
					.getNewBabyByIntimationKey(
							intimationDetails.getIntimationKey()).getName()
					: "";
			intimationDetails.setPatientNotCoveredName(name);
			String relationship = null != intimationService
					.getNewBabyByIntimationKey(intimationDetails
							.getIntimationKey()) ? intimationService
					.getNewBabyByIntimationKey(
							intimationDetails.getIntimationKey())
					.getBabyRelationship().getValue() : "";
			intimationDetails.setRelationshipWithInsuredId(relationship);
			List<ViewDocumentDetailsDTO> listDocumentDetails = acknowledgementService
					.listOfEarlierAckByClaimKey(claim.getKey(), rodKey);
			intimationDetails.setReceiptOfDocumentValues(listDocumentDetails);

			setPaymentDetails(intimationDetails, claim);

			DocAcknowledgement docAcknowledgement = acknowledgementService
					.findAcknowledgment(rodKey);
			if (docAcknowledgement != null
					&& docAcknowledgement.getHospitalisationFlag() != null
					&& !docAcknowledgement.getHospitalisationFlag()
							.equalsIgnoreCase("Y")) {
				rodIntimationDetailsObj.init(intimationDetails, rodKey);
			} else {
				rodIntimationDetailsObj.init(intimationDetails, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("No preauth for this intimation");
		}

		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Claim Status");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(rodIntimationDetailsObj);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		}else{
			getErrorMessage("Claim Number is not generated");
		}
	}
	
	public void getErrorMessage(String eMsg) {

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
		dialog.show(UI.getCurrent(), null, true);
	}
	
	private void getHospitalDetails(ViewClaimStatusDTO intimationDetails,
			Hospitals hospitals) {
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ViewClaimStatusDTO hospitalDetails = 
				instance.gethospitalDetails(hospitals);
		intimationDetails.setState(hospitalDetails.getState());
		intimationDetails.setCity(hospitalDetails.getCity());
		intimationDetails.setArea(hospitalDetails.getArea());
		intimationDetails.setHospitalAddress(hospitals.getAddress());
		intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
		intimationDetails.setHospitalTypeValue(hospitalDetails
				.getHospitalTypeValue());
		intimationDetails.setHospitalIrdaCode(hospitalDetails
				.getHospitalIrdaCode());
		intimationDetails.setHospitalInternalCode(hospitalDetails
				.getHospitalInternalCode());
		intimationDetails.setHospitalCategory(hospitals.getFinalGradeName());
		EarlierRodMapper.invalidate(instance);

	}
	
	
	public void getViewClaimStatusFromPremia(String intimationNo) {
		VerticalLayout vLayout = new VerticalLayout();
	
		String strDmsViewURL = BPMClientContext.PREMIA_CLAIM_VIEW_URL;
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		
		
		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+intimationNo));
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setWidth("100%");
		browserFrame.setHeight("150%");
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setWidth("100%");
		vLayout.setHeight("110%");
		final Window popup = new Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	
	public ViewTmpClaimPayment setPaymentDetails(ViewClaimStatusDTO bean,
			ViewTmpClaim claim) {

		try {
			
			ViewTmpClaimPayment reimbursementForPayment = null;
			
			ViewTmpReimbursement settledReimbursement = acknowledgementService.getLatestViewTmpReimbursementSettlementDetails(claim.getKey());
			if(settledReimbursement != null){
				
			     reimbursementForPayment = acknowledgementService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
				
			}else{
				reimbursementForPayment = acknowledgementService
						.getRimbursementForPayment(claim.getClaimId());
			}

			if(reimbursementForPayment != null){
			bean.setBankName(reimbursementForPayment.getBankName());
			bean.setTypeOfPayment(reimbursementForPayment.getPaymentType());
			bean.setAccountName(reimbursementForPayment.getAccountNumber());
			bean.setBranchName(reimbursementForPayment.getBranchName());
			bean.setChequeNumber(reimbursementForPayment.getChequeDDNumber());
			if(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
				
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setNeftDate(formatDate);
				}
			 bean.setChequeNumber(null);
			}else{
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setChequeDate(formatDate);
				}
			}
			

			return reimbursementForPayment;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
	
	/**
	 * Added for RRC Starts
	 * */
	public void validatUserForRRCRequest(@Observes @CDIEvent(VALIDATE_CLAIM_REGISTRATION_USER_RRC_REQUEST) final ParameterDTO parameters){
//		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
//		Boolean isValid = reimbursementService.validateUserForRRCRequest(preauthDTO);
//		view.buildValidationUserRRCRequestLayout(isValid);
		
	}
	

	public void saveRRCRequestValues(@Observes @CDIEvent(PROCESS_CLAIM_REGISTRATION_SAVE_RRC_REQUEST_VALUES) final ParameterDTO parameters){
//		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
//		String rrcRequestNo = reimbursementService.submitRRCRequestValues(preauthDTO,SHAConstants.CLAIMREQUEST_CASHLESS);
//		view.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	public void loadRRCRequestDropDownValues(
			@Observes @CDIEvent(PROCESS_CLAIM_REGISTRATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES) final ParameterDTO parameters) 
		{
//		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getMasterValueByReference(ReferenceTable.SIGNIFICANT_CLINICAL_INFORMATION);
//		view.loadRRCRequestDropDownValues(mastersValueContainer);
		}
	/**
	 * Added for RRC Ends
	 * */


//	protected void getSublimitOfClaim(
//			@Observes @CDIEvent(GET_PA_SUB_LIMIT_LIST) final ParameterDTO parameters) {
//
//		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters
//				.getPrimaryParameter();
//		List<SublimitFunObject> sublimitList =  new ArrayList<SublimitFunObject>();
//		
//		if(null != newIntimationDto.getPolicy().getProduct().getKey() )
//		{
//			/*sublimitList = dBCalculationService
//				.getClaimedAmountDetails(newIntimationDto.getPolicy().getProduct().getKey(),
//						newIntimationDto.getPolicy().getInsuredSumInsured(),
//						Integer.parseInt(newIntimationDto.getInsuredAge()));*/
//			Double insuredSumInsured = dBCalculationService.getInsuredSumInsured(newIntimationDto.getInsuredPatient().getInsuredId().toString(),newIntimationDto.getPolicy().getKey());
//			sublimitList = dBCalculationService
//					.getClaimedAmountDetailsForSection(newIntimationDto.getPolicy().getProduct().getKey(),
//							insuredSumInsured,
//							Double.parseDouble(newIntimationDto.getInsuredAge() != null && ! newIntimationDto.getInsuredAge().equals("") ? newIntimationDto.getInsuredAge() : "0"),0l,"0");
//			
//		}
//		System.out.println("================list size =======================:"+sublimitList.size());
//		view.setSublimt(sublimitList);
//	}


	protected void getCurrencyMaster(
			@Observes @CDIEvent(GET_PA_CURRENCY_MASTER) final ParameterDTO parameters) {
		BeanItemContainer<SelectValue> currencyMaster = masterService
				.getSelectValueContainer(ReferenceTable.CURRENCY);

		view.setCurrenceyMaster(currencyMaster);
	}

//	protected void getBalanceSumInsured(
//			@Observes @CDIEvent(GET_PA_BALANCE_SI) final ParameterDTO parameters) {
//		
//		NewIntimationDto newIntimationDto = (NewIntimationDto) parameters.getPrimaryParameter();
//		
//		Double insuredSumInsured = dBCalculationService.getInsuredSumInsured(newIntimationDto.getInsuredPatient().getKey().toString(),newIntimationDto.getPolicy().getKey());
//		Double balanceSI = dBCalculationService.getBalanceSI(newIntimationDto.getPolicy().getKey(),newIntimationDto.getInsuredPatient().getKey(), 0l, insuredSumInsured,newIntimationDto.getKey()).get(SHAConstants.TOTAL_BALANCE_SI);
//		
//		System.out.println("balanceSI ======================================"+balanceSI);
//		view.setBalanceSumInsued(balanceSI);
//	}
		
//	protected void getCpuObject(
//			@Observes @CDIEvent(GET_PA_CPU_OBJECT) final ParameterDTO parameters) {
//		Long cpuKey = (Long) parameters.getPrimaryParameter();
//		TmpCPUCode tmpCpuCode = policyService.getTmpCpuCode(cpuKey);
//		view.setCpuObject(tmpCpuCode);
//	}	
	
	protected void getPreviousClaims(
			@Observes @CDIEvent(GET_PREVIOUS_PA_CLAIMS) final ParameterDTO parameters) {
		List<PreviousClaimsTableDTO> previousClaimList = null;
		if(null != parameters.getPrimaryParameter())
		{	
			NewIntimationDto newIntimationDto = (NewIntimationDto)parameters.getPrimaryParameter();
//			previousClaimList = claimService.getPreviousClaimForPolicy(newIntimationDto);
			previousClaimList = preauthService.getPreviousClaimByForRegistration(newIntimationDto);
			view.setPreviousClaims(previousClaimList);
		}
	}

	protected void submitClaimRegister(
			@Observes @CDIEvent(SUBMIT_PA_CLAIM_CLICK) final ParameterDTO parameters) {
		ClaimDto claimDto = (ClaimDto) parameters.getPrimaryParameter();
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		Boolean isProceedFurther = true;
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(claimDto.getNewIntimationDto().getPolicy().getPolicyNumber(), claimDto.getNewIntimationDto().getIntimationId());
			if(get64vbStatus != null && (SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus))) {
				isProceedFurther = false;
			}
		}
		view.submitClaimRegister(claimDto, isProceedFurther);
	}

	protected void submitClaimRegistration(
			@Observes @CDIEvent(SUBMIT_PA_CLAIM_REGISTRATION) final ParameterDTO parameters) {
		
		SearchClaimRegistrationTableDto resultTask = (SearchClaimRegistrationTableDto) parameters.getPrimaryParameter();
		ClaimDto claimDto = (ClaimDto) parameters.getSecondaryParameters()[0];
		
		MastersValue master = masterService.getMaster(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
		
		if ((claimDto.getNewIntimationDto().getHospitalDto() != null && claimDto
						.getNewIntimationDto().getHospitalDto().getRegistedHospitals() != null && 
						(ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID).equals(claimDto
				.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getHospitalType().getKey()))
				|| ("D").equalsIgnoreCase(claimDto.getIncidenceFlagValue())) {
			master = masterService.getMaster(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
			claimDto.setProcessClaimType(SHAConstants.PA_LOB_TYPE);
		}
		if (master != null) {
			claimDto.setConversionFlag(master.getValue().toLowerCase()
					.contains("cashless") ? 1l : 0l);
			SelectValue claimType = new SelectValue();
			claimType.setId(master.getKey());
			claimType.setValue(master.getValue());
			claimDto.setClaimType(claimType);
		}
		claimDto.setStageId(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		if(claimDto.getSuggestRejection()){				
			claimDto.setStatusId(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
		}
		else{
			//claimDto.setStatusId(ReferenceTable.CLAIM_REGISTERED_STATUS);
			//claimDto.setStatusId(ReferenceTable.CLAIM_REGISTERED_STATUS);
			claimDto.setStatusId(ReferenceTable.INTIMATION_REGISTERED_STATUS);
		}
		
		if(claimDto.getNewIntimationDto().getPolicy() != null){
			claimDto.setOfficeCode(claimDto.getNewIntimationDto().getPolicy().getHomeOfficeCode());	
		}
		
		claimDto = claimService.submitPAClaim(resultTask,claimDto);
		Claim claimObj = claimService.getClaimByClaimKey(claimDto.getKey());
/*		if(claimDto != null && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(claimDto.getIncidenceFlagValue())){
			
			InitiateInvestigationDTO investigationDto = new InitiateInvestigationDTO();
			investigationDto.setClaimKey(claimDto.getKey());
			investigationDto.setIntimationkey(claimDto.getNewIntimationDto().getKey());
			investigationDto.setPolicyKey(claimDto.getNewIntimationDto().getPolicy().getKey());
			MastersValue allocatorMaster =  masterService.getMaster(ReferenceTable.IN_HOUSE_INVESTIGATOR);
			SelectValue allocator = new SelectValue(allocatorMaster.getKey(),allocatorMaster.getValue());
			investigationDto.setAllocationTo(allocator);
			Stage satgeObj = masterService.getStageByKey(claimDto.getStageId());
			investigationDto.setStage(satgeObj);
			Status satusObj =  masterService.getStatusByKey(claimDto.getStatusId());
			investigationDto.setStatus(satusObj);
			investigationDto.setReasonForRefering(SHAConstants.DIRECT_FROM_REGISTRATION);
			investigationDto.setTriggerPointsToFocus(SHAConstants.DIRECT_FROM_REGISTRATION);
			
			Investigation result = investigationService.create(investigationDto, null, BPMClientContext.USERID, BPMClientContext.PASSWORD, claimObj);
			if(result != null && result.getKey() != null){
				
				Hospitals hospitalByKey = hospitalService.getHospitalByKey(result.getClaim().getIntimation().getHospital());
				
				claimDto.setInvestigationKey(investigationDto.getTransactionKey());	
				intiateInvestigationForDBmigration(result,hospitalByKey);
			}
			
			
		}	*/
		
		ClaimDto responseClaim = ClaimMapper.getInstance().getClaimDto(claimObj);
		responseClaim.setNewIntimationDto(claimDto.getNewIntimationDto());
		if(null != responseClaim.getKey()){
			
			String outCome = "";
			
			if(claimDto.getStatusId().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)){
				outCome = SHAConstants.OUTCOME_FOR_MANUAL_SUGGEST_REJECTION;
			}else{
				if(claimObj != null && claimObj.getClaimType() != null && claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
					outCome = SHAConstants.AUTO_REGISTRATION_OUTCOME_FOR_REIMBURSEMENT;
				}else{
					outCome = SHAConstants.AUTO_REGISTRATION_OUTCOME;
				}
			}	

			
		if((ReferenceTable.PA_LOB_KEY).equals(claimDto.getNewIntimationDto().getPolicy().getLobId()) || (ReferenceTable.PACKAGE_MASTER_VALUE).equals(claimDto.getNewIntimationDto().getPolicy().getLobId())){
			//claimService.setBPMOutcome(resultTask, claimDto);	
			//claimService.updateUnNamedInsuredDetails(claimDto);
			claimService.submitDBProcedureForRegistration(resultTask, claimObj, claimDto, outCome);
		}	
		
		//if((claimObj != null &&	claimObj.getClaimType() != null && claimObj.getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) && (null == claimObj.getIncidenceFlag() || SHAConstants.ACCIDENT_FLAG.equalsIgnoreCase(claimObj.getIncidenceFlag()))){

			if(claimDto.getStatusId() != null && claimDto.getStatusId().equals(ReferenceTable.INTIMATION_REGISTERED_STATUS)){
				/*****
				 * **
				 * BPM to DB Migration done hence below code was commented. this task will get submitted in the above DBprocedure call for submit
				 */
//				initiateBPMNforReimbursement(claim);
//				autoRegisterFVR(claim.getIntimation(),resultTask.getUserId());
				claimService.autoRegisterFVR(claimObj.getIntimation(),resultTask);	
								
			}
		//}	
		if(claimObj.getClaimType().getKey() != null && ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(claimObj.getClaimType().getKey()))
		{
		Map<String,Object> wrkflowMap = (Map<String,Object>)resultTask.getDbOutArray();
		wrkflowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_REIMINDER_PROCESS);
		wrkflowMap.put(SHAConstants.LOB, SHAConstants.PA_LOB);
		if(claimDto.getIncidenceFlagValue() != null && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(claimDto.getIncidenceFlagValue())){
			wrkflowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY, SHAConstants.PA_BILLS_NOT_RECEIVED_DEATH);
		}
		else{
			wrkflowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY, SHAConstants.PA_BILLS_NOT_RECEIVED_OTHERS);
		}	
		
		claimService.initiateReminderLetterProcess(wrkflowMap);	
		}
		view.setClaimDetails(claimDto);
	}
	}

	protected void registerClicked(
			@Observes @CDIEvent(CLICK_PA_REGISTER_BUTTON) final ParameterDTO parameters) {
		view.registerClicked();
	}

	protected void suggestRejectionClicked(
			@Observes @CDIEvent(SUGGEST_PA_REJECTION) final ParameterDTO parameters) {
		view.suggestRejectionClicked();
	}

	protected void cancelClaimRegistration(
			@Observes @CDIEvent(CANCEL_PA_CLAIM_REGISTRATION) final ParameterDTO parameters) {
		view.cancelClaimRegistration();
	}
	
	protected void setClaimDetails(
			@Observes @CDIEvent(GET_PA_CLAIM) final ParameterDTO parameters) {
		ClaimDto claimDto = (ClaimDto) parameters.getPrimaryParameter();
		Claim claim = claimService.getClaimByKey(claimDto.getKey());
		claimDto =ClaimMapper.getInstance().getClaimDto(claim);
		NewIntimationDto newIntimationDto = new NewIntimationMapper()
				.getNewIntimationDto(claim.getIntimation());

		//Long tempPolicyKey = claim.getIntimation().getPolicy().getPolicySysId();
		Long policyKey = claim.getIntimation().getPolicy().getKey();
		//TmpPolicy tmpPolicy = policyService.getPolicyById(tempPolicyKey);
		Policy policy = policyService.getPolicyByKey(policyKey);

		//claimDto.getNewIntimationDto().setTmpPolicy(tmpPolicy);
		claimDto.getNewIntimationDto().setPolicy(policy);

		claimDto.setNewIntimationDto(newIntimationDto);

		List<Claim> previousClaims = claimService
				.getClaimsByPolicyNumber(newIntimationDto.getPolicy()
						.getPolicyNumber());
		List<PreviousClaimsTableDTO> previousClaimsDtoList = new ArrayList<PreviousClaimsTableDTO>();
		if (!previousClaims.isEmpty()) {
			for (Claim prevClaim : previousClaims) {
				if (prevClaim.getClaimId() == claimDto.getClaimId()) {
					previousClaims.remove(prevClaim);
				} else {
					PreviousClaimsTableDTO previousClaimsDto =  PreviousClaimMapper.getInstance()
							.getPreviousClaimsTableDTO(prevClaim);
				}
			}
		}

		view.setClaimDetails(claimDto);
	}
	
	public void intiateInvestigationForDBmigration(Investigation investigation, Hospitals hospitals){
		
		Map<String, Object> workFlowPayload = SHAUtils
				.getRevisedPayloadMap(investigation.getClaim(), hospitals);
		
		workFlowPayload.put(SHAConstants.STAGE_SOURCE,investigation.getStage().getStageName());
		
		workFlowPayload
				.put(SHAConstants.PAYLOAD_INVESTIGATION_KEY,
						investigation.getKey() != null ? investigation
								.getKey() : 0);
		
		workFlowPayload.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_REGISTRATION_TO_INVESTIGATION);
		
		DBCalculationService dbServcie = new DBCalculationService();
		Object[] inputWorkFlowArray = SHAUtils
				.getRevisedObjArrayForSubmit(workFlowPayload);
		dbServcie
				.revisedInitiateTaskProcedure(inputWorkFlowArray);
		
		
	}

}
