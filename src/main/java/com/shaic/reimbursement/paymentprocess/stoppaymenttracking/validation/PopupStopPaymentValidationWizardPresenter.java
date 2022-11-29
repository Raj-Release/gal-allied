package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.domain.ClaimService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.TmpEmployee;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.bean.StopPaymentRequest;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.bean.StopPaymentTrails;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestService;
import com.vaadin.ui.Button;


@SuppressWarnings("serial")
@ViewInterface(PopupStopPaymentValidateWizard.class)
public class PopupStopPaymentValidationWizardPresenter extends AbstractMVPPresenter<PopupStopPaymentValidateWizard> {

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	@EJB
	private MasterService masterService;
	
	@EJB
	private ClaimService clmService;
	
	private static final long serialVersionUID = 1L;
	public static final String POPUP_CANCEL_VALIDATION_REQUEST = "popup_cancel_payment_validation";
	public static final String POPUP_SUBMIT_VALIDATION_REQUEST = "popup_submit_payment_validation";
	
	public static final String GET_STOP_PAYMENT_TRACKING_TRIALS = "get_stop_payment_tracking_trails";
	
	@EJB
	private StopPaymentRequestService requestService; 
	
	public void cancelLumenRequest(@Observes @CDIEvent(POPUP_CANCEL_VALIDATION_REQUEST) final ParameterDTO parameters) {
		view.cancelStopPaymentValidate();
	}
	
	public void submitLumenRequest(@Observes @CDIEvent(POPUP_SUBMIT_VALIDATION_REQUEST) final ParameterDTO parameters) {
		StopPaymentRequestDto tableDto = (StopPaymentRequestDto) parameters.getPrimaryParameter();
		requestService.submitStopValidate(tableDto);
		view.submitStopPaymentValidate();
	}
	
	public void stopPaymentTracking(@Observes @CDIEvent(GET_STOP_PAYMENT_TRACKING_TRIALS) final ParameterDTO parameters){
		StopPaymentRequestDto tableDto = (StopPaymentRequestDto) parameters.getPrimaryParameter();
		
		StopPaymentRequest reimbObj = requestService.getPaymentKey(tableDto.getStopPaymentKey());
		if(null != reimbObj){
				
			List<StopPaymentTrails> payModeTrail = requestService.getStopPaymentTrailByRodNo(reimbObj.getRodNo());	
			
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

}
