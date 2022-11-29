package com.shaic.claim.OMPreceiptofdocumentsbillentry.page;

import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.GWizardListener;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class OMPRODBillEntryWizardViewImpl extends AbstractMVPView implements OMPProcessRODBillEntryPageWizard,GWizardListener {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private Instance<OMPProcessRODBillEntryPageUI> claimProcessorPageUIInstance;	 
	
	private OMPProcessRODBillEntryPageUI ompClaimProcessorPageUI;
	
	private OMPClaimProcessorDTO bean;
	
	private IWizardPartialComplete wizard;
	
	@Inject
	private Instance<RevisedCarousel> carouselInstance;

	private RevisedCarousel revisedCarousel;
	
	@Inject
	private Instance<OMPUploadDocumentsViewImpl> uploadDocumentsViemImpl;
	
	private OMPUploadDocumentsViewImpl uploadDocumentView;

	private VerticalSplitPanel mainPanel;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	@Override
	public void resetView() {
		if (null != uploadDocumentView) {
			uploadDocumentView.resetPage();
		}
		
	}
	
	@Override
	public String getCaption() {
		return "Documents & Bill Entry";
	}


	

	@Override
	public void buildSuccessLayout() {
		// TODO Auto-generated method stub
		
			Label successLabel = new Label("<b style = 'color: green;'>Receipt of Document and Bill Entry completed successfully !!!</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Acknowledge Document Received Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();

//					fireViewEvent(MenuItemBean.OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY, null);
					
				}
			});
		}
		
	

	@Override
	public void init(OMPClaimProcessorDTO bean, BeanItemContainer<SelectValue> classification, BeanItemContainer<SelectValue> subClassification, 
			BeanItemContainer<SelectValue> paymentTo, BeanItemContainer<SelectValue> paymentMode, BeanItemContainer<SelectValue> eventCode, 
			BeanItemContainer<SelectValue> currencyValue, BeanItemContainer<SelectValue> negotiatorName, BeanItemContainer<SelectValue> modeOfReciept, 
			BeanItemContainer<SelectValue> documentRecievedFrom, BeanItemContainer<SelectValue> documentType, BeanItemContainer<SelectValue> country) {
			mainPanel = new VerticalSplitPanel();
			this.wizard = new IWizardPartialComplete();
			wizard.getFinishButton().setCaption("Submit");
			setSizeFull();
			this.bean = bean;
			
		/*	revisedCarousel = carouselInstance.get();
			revisedCarousel.init(bean.getNewIntimationDto());*/
			IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance
					.get();
			intimationDetailsCarousel.initOMPCarousal(this.bean.getNewIntimationDto(), "Receipt of Documents & Bill Entry");
			mainPanel.setFirstComponent(/*revisedCarousel */intimationDetailsCarousel);
			FormLayout viewDetailsForm = new FormLayout();
			//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
			viewDetailsForm.setWidth("-1px");
			viewDetailsForm.setHeight("-1px");
			viewDetailsForm.setMargin(false);
			viewDetailsForm.setSpacing(true);
			
			viewDetails.initView(bean.getIntimationId(), ViewLevels.OMP);
			viewDetailsForm.addComponent(viewDetails);
			
			HorizontalLayout wizardLayout1 = new HorizontalLayout (viewDetailsForm);
			wizardLayout1.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
			wizardLayout1.setSpacing(true);
			wizardLayout1.setSizeFull();
			
			
			ompClaimProcessorPageUI = claimProcessorPageUIInstance.get();
			ompClaimProcessorPageUI.init(bean, classification,  subClassification,paymentTo,paymentMode,eventCode,
					 currencyValue,negotiatorName,modeOfReciept,documentRecievedFrom,documentType,country);
			wizard.addStep(ompClaimProcessorPageUI, "Documents & Bill Entry");
		 	uploadDocumentView = uploadDocumentsViemImpl.get();
		 	ReceiptOfDocumentsDTO receiptOfDocumentsDTO = new ReceiptOfDocumentsDTO();
		 	bean.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
			//uploadDocumentView.init(bean.getReceiptOfDocumentsDTO());
			wizard.addStep(uploadDocumentView, "Upload Documents ");
			wizard.setStyleName(ValoTheme.PANEL_WELL);
			wizard.setSizeFull();
			wizard.addListener(this);
			//mainPanel.setFirstComponent(ompClaimProcessorPageUI);
			Panel panel1 = new Panel(wizardLayout1);
			VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
			mainPanel.setSecondComponent(wizardLayout2);
			mainPanel.setSizeFull();
			mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
			mainPanel.setHeight("700px");
			setCompositionRoot(mainPanel);
	}
	
	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
		if(uploadDocumentView.onAdvance()){/*
			ReceiptOfDocumentsDTO receiptOfDocumentsDTO = uploadDocumentView.bean;
			bean.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
			fireViewEvent(OMPRODBillEntryPagePresenter.OMP_ROD_CLAIM_SUBMIT, this.bean);
		*/}
		
	}
	
	
	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						"Are you sure you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									
//									fireViewEvent(MenuItemBean.OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY, null);
									// Confirmed to continue
								} else {
									// User did not confirm
								}
							}
						});

		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}
	
	private void initBinder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateFieldsOnNegotiate(HorizontalLayout horizontalLayout, BeanItemContainer<SelectValue> negotiatorName) {
		ompClaimProcessorPageUI.generateNegotiate(horizontalLayout, negotiatorName);
		
	}

	@Override
	public void generateFieldsForRejection(HorizontalLayout horizontalLayout) {
		ompClaimProcessorPageUI.generateRejection(horizontalLayout);
		
	}

	@Override
	public void generateFieldsOnApproval(HorizontalLayout horizontalLayout) {
		ompClaimProcessorPageUI.generateApproval(horizontalLayout);
		
	}

	@Override
	public void setReferenceDate(Map<String, Object> referenceDataMap) {
		ompClaimProcessorPageUI.setReferenceDate(referenceDataMap);
		
	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub
//		fireViewEvent(OMPRODBillEntryPagePresenter.OMP_ROD_CLAIM_SUBMIT, this.bean);
	}

}
