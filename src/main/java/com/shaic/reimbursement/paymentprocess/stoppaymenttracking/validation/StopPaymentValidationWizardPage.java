package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.GMVPView;
import com.shaic.claim.settlementpullback.SettlementPullBackUI;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;

@SuppressWarnings("serial")
public class StopPaymentValidationWizardPage extends AbstractMVPView implements PopupStopPaymentValidateWizard,GMVPView {
	
	@Inject
	private Instance<StopPaymentValidationUI> StopPaymentValidationUIInstance;	 
	
	private StopPaymentValidationUI stopPaymentValidationUI;
	
	@Inject
	private StopPaymentTrackingTable trailForStopPayment;
	
	@PostConstruct
	public void init() {
		addStyleName("view");
	        
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView(StopPaymentRequestDto searchResult) {

		 setSizeFull();
		 stopPaymentValidationUI = StopPaymentValidationUIInstance.get();
		 stopPaymentValidationUI.init();
		 stopPaymentValidationUI.initView(searchResult/*, searchResult.getNewIntimationDTO()*/);         
	     setCompositionRoot(stopPaymentValidationUI);
	

	}

	@Override
	public void cancelStopPaymentValidate() {

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MenuItemBean.STOP_PAYMENT_VALIDATION,null);
				//UI.getCurrent().removeWindow(popup);
			}
			});
		
	

	}

	@Override
	public void submitStopPaymentValidate() {

		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Stop Payment Validation Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Request Submitted Successfully", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MenuItemBean.STOP_PAYMENT_VALIDATION,null);
				//UI.getCurrent().removeWindow(popup);

			}
		});
		
	
	

	}

	@Override
	public void showTrackingTrails(
			List<StopPaymentTrackingTableDTO> viewStopPaymentTrackingTableList) {
		stopPaymentValidationUI.showTrackingTrails(viewStopPaymentTrackingTableList);
	}

}
