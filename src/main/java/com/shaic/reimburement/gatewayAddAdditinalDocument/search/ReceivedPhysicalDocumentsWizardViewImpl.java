package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claims.reibursement.addaditionaldocuments.AcknowledgementReceiptViewImpl;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizard;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class ReceivedPhysicalDocumentsWizardViewImpl extends AbstractMVPView implements
ReceivedPhysicalDocumentsWizard{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String DOCUMENT_DETAILS = "Document Details For Bill Entry";

	@Inject
	private Instance<ReceivedPhysicalDocumentDetailsView> documentDetailsViewImpl;

	@SuppressWarnings("unused")
	@Inject
	private Instance<IWizard> iWizard;

	// The second page will be replaced with upload document details screen.
	@Inject
	private Instance<UploadReceivedPhysicalDocumentsView> uploadDocumentsViemImpl;

	@Inject
	private Instance<VerficationConfiremedView> verificationViewImpl;

	@Inject
	private AcknowledgementReceiptViewImpl acknowledgementReceiptViewImpl;

	////private static Window popup;

	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;

	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;

	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;

	@Inject
	private ViewDetails viewDetails;

	/*
	 * @Inject private TextBundle tb;
	 */

	private VerticalSplitPanel mainPanel;

	// private GWizard wizard;
	private IWizard wizard;

	private FieldGroup binder;

	private ReceiptOfDocumentsDTO bean;	

	private UploadReceivedPhysicalDocumentsView uploadDocumentView;
	
	private PreauthDTO preauthDTO;
	
	private RRCDTO rrcDTO;	
	

	private void initBinder() {

		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<ReceiptOfDocumentsDTO> item = new BeanItem<ReceiptOfDocumentsDTO>(
				bean);
		item.addNestedProperty("documentDetails");
		item.addNestedProperty("documentDetails.documentsReceivedFrom");
		item.addNestedProperty("documentDetails.acknowledgmentContactNumber");
		item.addNestedProperty("documentDetails.documentsReceivedDate");
		item.addNestedProperty("documentDetails.emailId");
		item.addNestedProperty("documentDetails.modeOfReceipt");
		item.addNestedProperty("documentDetails.reconsiderationRequest");
		item.addNestedProperty("documentDetails.hospitalization");
		item.addNestedProperty("documentDetails.preHospitalization");
		item.addNestedProperty("documentDetails.postHospitalization");
		item.addNestedProperty("documentDetails.partialHospitalization");
		item.addNestedProperty("documentDetails.lumpSumAmount");
		item.addNestedProperty("documentDetails.addOnBenefitsHospitalCash");
		item.addNestedProperty("documentDetails.addOnBenefitsPatientCare");

		// item.addNestedProperty("uploadDocumentsDTO.fileUpload");
		item.addNestedProperty("uploadDocumentsDTO.fileType");
		item.addNestedProperty("uploadDocumentsDTO.billNo");
		item.addNestedProperty("uploadDocumentsDTO.billDate");
		item.addNestedProperty("uploadDocumentsDTO.noOfItems");
		item.addNestedProperty("uploadDocumentsDTO.billValue");

		item.addNestedProperty("uploadedDocumentsDTO.fileType");
		item.addNestedProperty("uploadedDocumentsDTO.fileName");
		item.addNestedProperty("uploadedDocumentsDTO.rodNo");
		item.addNestedProperty("uploadedDocumentsDTO.billNo");
		item.addNestedProperty("uploadedDocumentsDTO.billDate");
		item.addNestedProperty("uploadedDocumentsDTO.noOfItems");
		item.addNestedProperty("uploadedDocumentsDTO.billValue");

		this.binder.setItemDataSource(item);
	}

	@PostConstruct
	public void initView() {
		
		mainPanel = new VerticalSplitPanel();	
		addStyleName("view");
		setSizeFull();
	}

	public void initView(ReceiptOfDocumentsDTO rodDTO) {
		this.bean = rodDTO;
		// this.wizard = new IWizard();
		// this.wizard = iWizard.get();
		this.wizard = new IWizard();
		this.rrcDTO = rodDTO.getRrcDTO();
		initBinder();

		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		
		ReceivedPhysicalDocumentDetailsView documentDetailsView = documentDetailsViewImpl.get();
		documentDetailsView.init(this.bean, wizard);
		wizard.addStep(documentDetailsView, DOCUMENT_DETAILS);

		uploadDocumentView = uploadDocumentsViemImpl.get();
		uploadDocumentView.init(this.bean);
		wizard.addStep(uploadDocumentView, "Upload Documents Verification");

		VerficationConfiremedView verficationDetailsView = verificationViewImpl.get();
		verficationDetailsView.init(bean);
		wizard.addStep(verficationDetailsView, "Verification Confirmed");

	/*	acknowledgementReceiptViewImpl.init(rodDTO);
		wizard.addStep(acknowledgementReceiptViewImpl,
				"Acknowledgement Receipt");*/

		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);

		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		if(this.bean.getScreenName() != null && this.bean.getScreenName().equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER)){
			intimationDetailsCarousel.init(this.bean.getClaimDTO()
					.getNewIntimationDto(), this.bean.getClaimDTO(),
					"Received Physical Documents (Checker)",rodDTO.getDiagnosis());	
		}
		else{
		intimationDetailsCarousel.init(this.bean.getClaimDTO()
				.getNewIntimationDto(), this.bean.getClaimDTO(),
				"Received Physical Documents (Maker)",rodDTO.getDiagnosis());
		}
		mainPanel.setFirstComponent(intimationDetailsCarousel);	

		Panel panel1 = new Panel();	
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);

		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);

	}	


	@Override
	public void resetView() {

		if (null != uploadDocumentView) {
			uploadDocumentView.resetPage();
		}

	}

	@Override
	public void activeStepChanged(WizardStepActivationEvent event) {


	}

	@Override
	public void stepSetChanged(WizardStepSetChangedEvent event) {

	}

	@Override
	public void wizardCompleted(WizardCompletedEvent event) {
	
		fireViewEvent(ReceivedPhysicalDocumentsWizardPresenter.SUBMIT_RECEIVED_PHYSICAL_DOCUMENT_DETAILS,
				this.bean);
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ?", "Cancel", "Ok",
				new ConfirmDialog.Listener() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void onClose(ConfirmDialog dialog) {
						if (!dialog.isConfirmed()) {
							if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER)){
								fireViewEvent(MenuItemBean.PHYSICAL_DOCUMENT_CHECKER, null);
								}
								else {
							fireViewEvent(MenuItemBean.PHYSICAL_DOCUMENT, null);
								}
						} else {
							// User did not confirm
						}
					}
				});

		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);

	}

	@Override
	public void initData(WizardInitEvent event) {

	}

	@Override
	public void wizardSave(GWizardEvent event) {

	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'>Claim record saved successfully !!!</b>",
				ContentMode.HTML);

		/*Label successLabelEnd = new Label(
				"<b style = 'color: green;'>Pls note: Acknowledgement receipt would be shown for printing</b>",
				ContentMode.HTML);*/

		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);

		VerticalLayout layout = new VerticalLayout(successLabel,
				horizontalLayout/*, successLabelEnd*/);
		layout.setComponentAlignment(successLabel, Alignment.MIDDLE_CENTER);
		//layout.setComponentAlignment(successLabelEnd, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
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
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(bean.getScreenName() != null && bean.getScreenName().equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER)){
					fireViewEvent(MenuItemBean.PHYSICAL_DOCUMENT_CHECKER, null);
					}
					else {
				fireViewEvent(MenuItemBean.PHYSICAL_DOCUMENT, null);
					}
			}
		});

	}



}
