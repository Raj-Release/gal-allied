package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class StopPaymentRequestWizardPage extends AbstractMVPView implements PopupStopPaymentRequestWizard,GMVPView {
	
	
	@Inject
	private Instance<StopPaymentRequestUI> stopPaymentRequestUIInstance;	 
	
	private StopPaymentRequestUI stopPaymentRequestUI;
	
	
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
		 stopPaymentRequestUI = stopPaymentRequestUIInstance.get();
		 stopPaymentRequestUI.init();
		 stopPaymentRequestUI.initView(searchResult);         
	     setCompositionRoot(stopPaymentRequestUI);
	

	}

	@Override
	public void cancelStopPaymentRequest() {

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
				fireViewEvent(MenuItemBean.STOP_PAYMENT_REQUREST,null);
				//UI.getCurrent().removeWindow(popup);
				stopPaymentRequestUI.clearReference();
			}
			});
		
	

	}

	@Override
	public void submitStopPaymentRequest() {

		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Stop Payment Validation Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Request Submitted Successfully", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MenuItemBean.STOP_PAYMENT_REQUREST,null);
				//UI.getCurrent().removeWindow(popup);
				stopPaymentRequestUI.clearReference();

			}
		});
		
	
	

	}

	@Override
	public void editUploadDocumentDetails(UploadDocumentDTO uploadDTO) {
		stopPaymentRequestUI.editUploadedDocumentDetails(uploadDTO);
		
	}

	@Override
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocDTO) {
		stopPaymentRequestUI.loadUploadedDocsTableValues(uploadDocDTO);
		
	}

	@Override
	public void deleteUploadDocumentDetails(UploadDocumentDTO uploadDTO) {
		stopPaymentRequestUI.deleteUploadDocumentDetails(uploadDTO);
		
	}

}
