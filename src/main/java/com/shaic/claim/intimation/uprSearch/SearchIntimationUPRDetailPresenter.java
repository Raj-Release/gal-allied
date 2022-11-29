package com.shaic.claim.intimation.uprSearch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.Page;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuService;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.settlementpullback.searchsettlementpullback.SearchSettlementPullBackService;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentTrailTableDTO;
import com.shaic.domain.BancsPaymentCancel;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.ClaimPaymentCancel;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PaymentModeTrail;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.bean.StopPaymentTrails;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestService;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.StopPaymentTrackingTableDTO;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
@ViewInterface(SearchIntimationUPRDetailView.class)
public class SearchIntimationUPRDetailPresenter extends AbstractMVPPresenter<SearchIntimationUPRDetailView>{
	
	public static final String RESET_UPR_SEARCH_VIEW = "Reset UPR Search Fields for view details"; 
	public static final String SUBMIT_UPR_SEARCH = "UPR Search Submit for view details";
	public static final String VIEW_ACTION_CLICK = "UPR Search Result Action Click";
	public static final String GET_UPR_SEARCH_RESULT = "Get UPR Search Result for view details";
	public static final String GET_PAYMENT_MODE_TRIALS = "Get Paymet Trials for view details";
	public static final String GET_PAYMENT_CANCEL_DETAILS = "Get Paymet Cancel for view details";
	public static final String GET_SETTLEMENT_DETAILS = "Get_Settlement_Details";
	protected static final String GET_STOP_PAYMENT_TRIALS = "Get_Stop_payments_Tracking";
		
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService clmService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private SearchSettlementPullBackService paymentService;
	
	@EJB
	private StopPaymentRequestService requestService; 
	
	@Override
	public void viewEntered() {
		System.out.println("view Entered called");
	}
	
	protected void showSearchViewDetailIntimationTable(@Observes @CDIEvent(SUBMIT_UPR_SEARCH) final ParameterDTO parameters) {
		view.showSearchViewDetailIntimationTable((SearchIntimationFormDto) parameters.getPrimaryParameter());
	}
	
	protected void showSearchIntimation(@Observes @CDIEvent(RESET_UPR_SEARCH_VIEW) final ParameterDTO parameters) {
		view.resetSearchIntimationView();
		System.out.println("view Entered called");
	}
	
	protected void showActionClick(@Observes @CDIEvent(VIEW_ACTION_CLICK) final ParameterDTO parameters) {
		PaymentProcessCpuPageDTO paymentDto = (PaymentProcessCpuPageDTO)parameters.getPrimaryParameter();

		String intimationNo = paymentDto.getClaimDto().getNewIntimationDto().getIntimationId();
		Long rodKey = paymentDto.getReimbursementObj().getKey();
		ViewTmpClaim viewClaim = null;

		try {
			final ViewTmpIntimation viewIntimation = intimationService
					.searchbyIntimationNoFromViewIntimation(intimationNo);

			viewClaim = clmService.getTmpClaimforIntimation(viewIntimation.getKey());
			Intimation intimationObj = intimationService
					.getIntimationByKey(viewIntimation
							.getKey());
			
			if (viewClaim != null) {
				EarlierRodMapper instance = EarlierRodMapper.getInstance();
				ClaimStatusRegistrationDTO registrationDetails = instance.getRegistrationDetails(viewClaim);
				 Intimation intimation = paymentDto.getReimbursementObj().getClaim().getIntimation();
//				 intimationService.getIntimationByKey(claim.getIntimation().getKey());
				ViewClaimStatusDTO intimationDetails = EarlierRodMapper
						.getViewClaimStatusDto(intimation);
				Hospitals hospitals = intimationService
						.getHospitalByKey(intimation.getHospital());
				getHospitalDetails(intimationDetails, hospitals);
				intimationDetails
						.setClaimStatusRegistrionDetails(registrationDetails);
				EarlierRodMapper.invalidate(instance);
				intimationDetails.setClaimKey(viewClaim.getKey());
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
				List<ViewDocumentDetailsDTO> listDocumentDetails = ackDocService
						.listOfEarlierAckByClaimKey(viewClaim.getKey(), rodKey);
				intimationDetails
						.setReceiptOfDocumentValues(listDocumentDetails);

				setPaymentDetails(intimationDetails, viewClaim);
				
				if(null != intimation && null != intimation.getPolicy() && 
						null != intimation.getPolicy().getProduct() && 
						null != intimation.getPolicy().getProduct().getKey() &&
							(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
					
					//intimationDetails.setInsuredPatientName(intimationObj.getPaPatientName());
					if(intimationObj.getPaPatientName() !=null && !intimationObj.getPaPatientName().isEmpty())
					{
						intimationDetails.setInsuredPatientName(intimationObj.getPaPatientName());
					}else
					{
						intimationDetails.setInsuredPatientName((intimationObj.getInsured() !=null && intimationObj.getInsured().getInsuredName() !=null) ? intimationObj.getInsured().getInsuredName() : "");
					}
				}
				if(null != intimation && null != intimation.getPolicy() && 
						null != intimation.getPolicy().getProduct() && 
						null != intimation.getPolicy().getProduct().getKey() &&
						(ReferenceTable.getGMCProductList().containsKey(intimationObj.getPolicy().getProduct().getKey()))){
					 boolean isjiopolicy = false;	
						isjiopolicy = intimationService.getJioPolicyDetails(intimationObj.getPolicy().getPolicyNumber());
						  
						intimationDetails.setJioPolicy(isjiopolicy);
					      Insured insuredByKey = intimationService.getInsuredByKey(intimationObj.getInsured().getKey());
					      Insured MainMemberInsured = null;
					      
					      if(insuredByKey.getDependentRiskId() == null){
					    	  MainMemberInsured = insuredByKey;
					      }else{
					    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredId(intimationObj.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
					    	  MainMemberInsured = insuredByPolicyAndInsuredId;
					      }
					      
					      if(MainMemberInsured != null){
					    	  intimationDetails.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());					    	 
					      }							
				}
				intimationDetails.setRodKey(paymentDto.getReimbursementObj().getKey());
				view.showActionClickView(intimationDetails);
				System.out.println("view Entered called");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Claim Number is not generated",
					Notification.TYPE_ERROR_MESSAGE);
		}
		
		if (viewClaim == null) {
			getErrorMessage("Claim Number is not generated");
		}		
		
	}
	
	private void getHospitalDetails(ViewClaimStatusDTO intimationDetails,
			Hospitals hospitals) {
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ViewClaimStatusDTO hospitalDetails = instance.gethospitalDetails(hospitals);
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
		intimationDetails.setHospitalFlag(hospitals.getSuspiciousType());
		EarlierRodMapper.invalidate(instance);
	}
	
	public ViewTmpClaimPayment setPaymentDetails(ViewClaimStatusDTO bean,
			ViewTmpClaim claim) {

		try {
			
			ViewTmpClaimPayment reimbursementForPayment = null;
			
			ViewTmpReimbursement settledReimbursement = ackDocService.getLatestViewTmpReimbursementSettlementDetails(claim.getKey());
			if(settledReimbursement != null){
				
			     reimbursementForPayment = ackDocService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
				
			}else{
				reimbursementForPayment = ackDocService
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

	protected void showSearchResultView(@Observes @CDIEvent(GET_UPR_SEARCH_RESULT) final ParameterDTO parameters) {
		SearchIntimationFormDto searchDto = (SearchIntimationFormDto) parameters.getPrimaryParameter();
		Page<PaymentProcessCpuPageDTO> newIntimationDtoPagedContainer = intimationService.searchIntimationForUPRView(searchDto);
		view.showSearchResultViewDetail(newIntimationDtoPagedContainer);		
	}

	public void getPaymentModeTrialsView(@Observes @CDIEvent(GET_PAYMENT_MODE_TRIALS) final ParameterDTO parameters) {
		
		PaymentProcessCpuTableDTO updatePaymentDTO = (PaymentProcessCpuTableDTO)parameters.getPrimaryParameter(); 		
			
		Reimbursement reimbObj = clmService.getReimbursementByRodNo(updatePaymentDTO.getRodNo());
			
		if(null != reimbObj){
				
			List<PaymentModeTrail> payModeTrail = clmService.getPaymentModeTrailByRodKey(reimbObj.getKey());	
			
			List<ViewPaymentTrailTableDTO> viewPaymentTrailTableList = new ArrayList<ViewPaymentTrailTableDTO>();
			ViewPaymentTrailTableDTO viewPaymentDetailsDto = null;
			if(null != payModeTrail && !payModeTrail.isEmpty()){
				TmpEmployee userObj = null;
				for (PaymentModeTrail paymentModeTrail : payModeTrail) {
					
					viewPaymentDetailsDto = new ViewPaymentTrailTableDTO();
					
					viewPaymentDetailsDto.setClaimType(updatePaymentDTO.getClaimType());
					viewPaymentDetailsDto.setDateAndTime(paymentModeTrail.getCreatedDate());
					viewPaymentDetailsDto.setClaimStage(paymentModeTrail.getStageId().getStageName());
					viewPaymentDetailsDto.setClaimStatus(paymentModeTrail.getStatusId().getProcessValue());
					viewPaymentDetailsDto.setReasonForChange(paymentModeTrail.getPayModeRemarks());
					userObj = masterService.getEmployeeName(paymentModeTrail.getCreatedBy());
					if(userObj != null) {
						viewPaymentDetailsDto.setUserId(userObj.getLoginId() + " - " + userObj.getEmpFirstName());
					}	
					viewPaymentDetailsDto.setPaymentMode(paymentModeTrail.getPaymentMode());
					viewPaymentTrailTableList.add(viewPaymentDetailsDto);
				}
			    view.setPaymentTrials(viewPaymentTrailTableList);				
			}
				
		}
	}
	
	protected void showPaymentCancelViewDetail(@Observes @CDIEvent(GET_PAYMENT_CANCEL_DETAILS) final ParameterDTO parameters) {
	
		PaymentProcessCpuPageDTO paymentDto = (PaymentProcessCpuPageDTO) parameters.getPrimaryParameter(); 
		
		List<ClaimPaymentCancel> premiaPaymentCancelList = intimationService.getPremiaPaymentCancelDetailList(paymentDto,SHAConstants.CHEQUE.toLowerCase());
		List<ClmPaymentCancelDto> chqPremiaCancelListDto = getPremiaCancelListDto(premiaPaymentCancelList);

		List<BancsPaymentCancel> bancsChqPaymentCancelList = intimationService.getBancsPaymentCancelDetailList(paymentDto,SHAConstants.CHEQUE.toLowerCase());
		List<ClmPaymentCancelDto> chqBancsCancelListDto = getBancsCancelListDto(bancsChqPaymentCancelList);
		
		List<ClmPaymentCancelDto> chqCancelListDto = new ArrayList<ClmPaymentCancelDto>();
		chqCancelListDto.addAll(chqPremiaCancelListDto);
		chqCancelListDto.addAll(chqBancsCancelListDto);
		
		List<ClaimPaymentCancel> premiaNeftPaymentCancelList = intimationService.getPremiaPaymentCancelDetailList(paymentDto,SHAConstants.NEFT_TYPE.toLowerCase());
		List<ClmPaymentCancelDto> neftPremiaCancelListDto = getPremiaCancelListDto(premiaNeftPaymentCancelList);
		
		List<BancsPaymentCancel> bancsNeftPaymentCancelList = intimationService.getBancsPaymentCancelDetailList(paymentDto,SHAConstants.NEFT_TYPE.toLowerCase());
		List<ClmPaymentCancelDto> neftBancsCancelListDto = getBancsCancelListDto(bancsNeftPaymentCancelList);
		
		List<ClmPaymentCancelDto> neftCancelListDto = new ArrayList<ClmPaymentCancelDto>();
		neftCancelListDto.addAll(neftPremiaCancelListDto);
		neftCancelListDto.addAll(neftBancsCancelListDto);
		
		view.setPaymentCancelDetails(chqCancelListDto, neftCancelListDto);
	}
	
	private List<ClmPaymentCancelDto> getPremiaCancelListDto(
			List<ClaimPaymentCancel> paymentCancelList) {
		List<ClmPaymentCancelDto> cancelListDto = new ArrayList<ClmPaymentCancelDto>();
		ClmPaymentCancelDto paymentCancelDto = null;
		if(paymentCancelList != null && !paymentCancelList.isEmpty()) {
			for (ClaimPaymentCancel claimPaymentCancel : paymentCancelList) {
				paymentCancelDto = new ClmPaymentCancelDto();
				paymentCancelDto.setPaymentType(claimPaymentCancel.getPaymentType());
				paymentCancelDto.setPaymentKey(claimPaymentCancel.getPaymentKey());
				paymentCancelDto.setRodNumber(claimPaymentCancel.getRodNumber());
				paymentCancelDto.setCancelDate(claimPaymentCancel.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(claimPaymentCancel.getCreatedDate()) : "");
//				paymentCancelDto.setRemarks(claimPaymentCancel.getCancelRemarks());     //the value was not captured in the column so commented.
				paymentCancelDto.setRemarks(claimPaymentCancel.getCancelType());
				cancelListDto.add(paymentCancelDto);
			}
		}
		return cancelListDto;
	}
	
	private List<ClmPaymentCancelDto> getBancsCancelListDto(
			List<BancsPaymentCancel> paymentCancelList) {
		List<ClmPaymentCancelDto> cancelListDto = new ArrayList<ClmPaymentCancelDto>();
		ClmPaymentCancelDto paymentCancelDto = null;
		if(paymentCancelList != null && !paymentCancelList.isEmpty()) {
			for (BancsPaymentCancel claimPaymentCancel : paymentCancelList) {
				paymentCancelDto = new ClmPaymentCancelDto();
				paymentCancelDto.setPaymentKey(claimPaymentCancel.getPaymentKey());
				paymentCancelDto.setRodNumber(claimPaymentCancel.getRodNumber());
				paymentCancelDto.setCancelDate(claimPaymentCancel.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(claimPaymentCancel.getCreatedDate()) : "");
				paymentCancelDto.setRemarks(claimPaymentCancel.getPaymenetCancelRemarks());
				cancelListDto.add(paymentCancelDto);
			}
		}
		return cancelListDto;
	}
	
	protected void showSettlementDetail(@Observes @CDIEvent(GET_SETTLEMENT_DETAILS) final ParameterDTO parameters) {
		
		ClmPaymentCancelDto paymentCancelDto = (ClmPaymentCancelDto) parameters.getPrimaryParameter();
		ClaimPayment settlementObj = intimationService.getSettlementDetailsByPaymode(paymentCancelDto.getPaymentKey(), paymentCancelDto.getPaymentType());
		if(settlementObj != null) {
			PaymentProcessCpuPageDTO paymentDto = new PaymentProcessCpuPageDTO();
			paymentDto.setSettledAmount(settlementObj.getTotalAmount());
			paymentDto.setPaymentType(settlementObj.getPaymentType());
			
			if((SHAConstants.NEFT_TYPE).equalsIgnoreCase(settlementObj.getPaymentType())) {
				paymentDto.setAccNumber(settlementObj.getAccountNumber());
				paymentDto.setIfscCode(settlementObj.getIfscCode());
				paymentDto.setBankName(settlementObj.getBankName());
				paymentDto.setBankBranchName(settlementObj.getBranchName());
				paymentDto.setPayeeName(settlementObj.getPayeeName());
			}
			else {
				paymentDto.setDdNo(settlementObj.getChequeDDNumber());
				paymentDto.setDdDateValue(settlementObj.getChequeDDDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(settlementObj.getChequeDDDate()) : "");
				paymentDto.setPayableAt(settlementObj.getPayableAt());
			}
			paymentDto.setKey(settlementObj.getKey());
		view.setSettlementDetails(paymentDto);
		}
			
	
	}
	
	protected void showStopPaymentTrails(@Observes @CDIEvent(GET_STOP_PAYMENT_TRIALS) final ParameterDTO parameters){
		PaymentProcessCpuTableDTO getDto=  (PaymentProcessCpuTableDTO) parameters.getPrimaryParameter();
		
		List<StopPaymentTrails> payModeTrail = requestService.getStopPaymentTrailByRodNo(getDto.getRodNo());	
		
		List<StopPaymentTrackingTableDTO> viewStopPaymentTrackingTableList = new ArrayList<StopPaymentTrackingTableDTO>();
		StopPaymentTrackingTableDTO viewStopPaymentTrackingTableDTO = null;
		if(null != payModeTrail && !payModeTrail.isEmpty()){
			TmpEmployee userObj = null;
			for (StopPaymentTrails paymentModeTrail : payModeTrail) {
				
				viewStopPaymentTrackingTableDTO = new StopPaymentTrackingTableDTO();
				
				viewStopPaymentTrackingTableDTO.setIntimationNo(paymentModeTrail.getIntimationNo());
				viewStopPaymentTrackingTableDTO.setRodNumber(paymentModeTrail.getRodNo());
				viewStopPaymentTrackingTableDTO.setUtrNumber(paymentModeTrail.getUtrNumber());
				if(paymentModeTrail.getStatusId()!=null && paymentModeTrail.getStatusId().equals(SHAConstants.STOP_PAYMENT_REQUEST)){
				 viewStopPaymentTrackingTableDTO.setRequestedDate(paymentModeTrail.getCreatedDate());
				}
				if(paymentModeTrail.getStatusId()!=null && paymentModeTrail.getStatusId().equals(SHAConstants.STOP_PAYMENT_VALIDATEED)){
				 viewStopPaymentTrackingTableDTO.setValidateDate(paymentModeTrail.getCreatedDate());
				}
				if(paymentModeTrail.getStatusId()!=null && paymentModeTrail.getStatusId().equals(SHAConstants.STOP_PAYMENT_REQUEST)){
					viewStopPaymentTrackingTableDTO.setStopPaymentReqRemarks(paymentModeTrail.getStatusRemarks());
				}
				
				if(paymentModeTrail.getStatusId()!=null && paymentModeTrail.getStatusId().equals(SHAConstants.STOP_PAYMENT_VALIDATEED)){
					viewStopPaymentTrackingTableDTO.setValidationRemarks(paymentModeTrail.getStatusRemarks());
				}
				
				
				userObj = masterService.getEmployeeName(paymentModeTrail.getCreatedBy());
				if(userObj != null && paymentModeTrail.getStatusId()!=null && paymentModeTrail.getStatusId().equals(SHAConstants.STOP_PAYMENT_REQUEST)) {
					viewStopPaymentTrackingTableDTO.setRequestBy(userObj.getLoginId() + " - " + userObj.getEmpFirstName());
				}	
				
				userObj = masterService.getEmployeeName(paymentModeTrail.getCreatedBy());
				if(userObj != null && paymentModeTrail.getStatusId()!=null && paymentModeTrail.getStatusId().equals(SHAConstants.STOP_PAYMENT_VALIDATEED)) {
					viewStopPaymentTrackingTableDTO.setValidateBy(userObj.getLoginId() + " - " + userObj.getEmpFirstName());
				}
				
				viewStopPaymentTrackingTableDTO.setResonForStopPayment(paymentModeTrail.getRequestReason().getValue());
				if(paymentModeTrail.getValidaAction() != null && paymentModeTrail.getValidaAction().equalsIgnoreCase("A")){
					viewStopPaymentTrackingTableDTO.setAction(SHAConstants.STOP_PAYMENT_APP);
				}else if (paymentModeTrail.getValidaAction() != null && paymentModeTrail.getValidaAction().equalsIgnoreCase("R")){
					viewStopPaymentTrackingTableDTO.setAction(SHAConstants.STOP_PAYMENT_REJ);
				}else{
					viewStopPaymentTrackingTableDTO.setAction(paymentModeTrail.getValidaAction());
				}
				viewStopPaymentTrackingTableList.add(viewStopPaymentTrackingTableDTO);
			}
		    view.showTrackingTrails(viewStopPaymentTrackingTableList);				
		}else{
			
        HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("No Records Found", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		}
			
	
	}
	
}