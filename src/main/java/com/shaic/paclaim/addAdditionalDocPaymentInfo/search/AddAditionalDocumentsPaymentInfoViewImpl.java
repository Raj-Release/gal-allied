package com.shaic.paclaim.addAdditionalDocPaymentInfo.search;

import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPNewClaimProcessorPageUI;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claims.reibursement.addaditionaldocuments.UploadDocumentsView;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class AddAditionalDocumentsPaymentInfoViewImpl extends AbstractMVPView
		implements AddAditionalDocumentsPaymentInfoView {
	
	
	
	@Inject
	private Instance<AddAditionalDocumentsPaymentInfoPageUI> documentsPaymentInfoPageUIInstance;	 
	
	private AddAditionalDocumentsPaymentInfoPageUI documentsPaymentInfoPageUIPageUI;
	
	
	


	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}
	
	
	
	@Override
	public void init() {
		
	
	}



	public void initView(ReceiptOfDocumentsDTO rodDTO) {
		
		 setSizeFull();
//		 this.bean = bean;
		 documentsPaymentInfoPageUIPageUI = documentsPaymentInfoPageUIInstance.get();
		 documentsPaymentInfoPageUIPageUI.init(rodDTO);
		 setCompositionRoot(documentsPaymentInfoPageUIPageUI);
	}



	@Override
	public void setUpDropDownValues(Map<String, Object> referenceDataMap) {
		documentsPaymentInfoPageUIPageUI.setFileTypeValues(referenceDataMap);
	}



	@Override
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocList) {
		documentsPaymentInfoPageUIPageUI.loadUploadedDocsTableValues(uploadDocList);		
	}



	@Override
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		documentsPaymentInfoPageUIPageUI.deleteUploadDocumentDetails(dto);		
	}



	@Override
	public void editUploadDocumentDetails(UploadDocumentDTO dto) {
		documentsPaymentInfoPageUIPageUI.editUploadedDocumentDetails(dto);		
	}



	@Override
	public void resetPage() {
		documentsPaymentInfoPageUIPageUI.reset();		
	}



	@Override
	public void updateBean(ReceiptOfDocumentsDTO bean, Map<String, Object> referenceData) {
		documentsPaymentInfoPageUIPageUI.updateBean(bean, referenceData);
	}



	@Override
	public void buildSuccessLayout() {

		Label successLabel = new Label(
				"<b style = 'color: green;'>Claim record saved successfully !!!</b>",
				ContentMode.HTML);
		
		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
	//	horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		VerticalLayout layout = new VerticalLayout();
		layout.addComponents(successLabel,horizontalLayout);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		getUI();
		dialog.show(UI.getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.PA_ADD_ADDITIONAL_DOCUMENT_PAYMENT_INFORMATION, null);
			}
		});

	
	}



	@Override
	public void setUpPayableDetails(String payableAt) {
		documentsPaymentInfoPageUIPageUI.setUpPayableDetails(payableAt);
		
	}



	@Override
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		documentsPaymentInfoPageUIPageUI.setUpIFSCDetails(dto);
		
	}



	@Override
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO) {
		documentsPaymentInfoPageUIPageUI.populatePreviousPaymentDetails(tableDTO);
	}
	
	

}
