/**
 * 
 */
package com.shaic.claims.reibursement.addaditionaldocuments;

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

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.PreauthIntimationDetailsCarousel;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.IWizard;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.srikanthp
 *
 */
public class WizardViewImpl extends AbstractMVPView implements WizardView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String DOCUMENT_DETAILS = "Document Details For Bill Entry";

	@Inject
	private Instance<DocumentDetailsView> documentDetailsViewImpl;

	/*@SuppressWarnings("unused")
	@Inject
	private Instance<IWizard> iWizard;*/

	// The second page will be replaced with upload document details screen.
	@Inject
	private Instance<UploadDocumentsView> uploadDocumentsViemImpl;

	@Inject
	private Instance<DetailsView> billEntryViewImpl;

	////private static Window popup;

	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;

	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;

	@Inject
	private Instance<PreauthIntimationDetailsCarousel> commonCarouselInstance;

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
	// private String[] arrScreenName ;
	// private Map<String,String> captionMap ;

	private UploadDocumentsView uploadDocumentView;

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
		/*
		 * arrScreenName = new String[] {
		 * "documentDetails","acknowledgementReceipt"};
		 */
		mainPanel = new VerticalSplitPanel();
		// captionMap = new HashMap<String, String>();
		addStyleName("view");
		setSizeFull();
	}

	public void initView(ReceiptOfDocumentsDTO rodDTO) {
		this.bean = rodDTO;
		// this.wizard = new IWizard();
		// this.wizard = iWizard.get();
		this.wizard = new IWizard();

		initBinder();

		DocumentDetailsView documentDetailsView = documentDetailsViewImpl.get();
		documentDetailsView.init(this.bean, wizard);
		// wizard.addStep(documentDetailsView,"Document Details");
		wizard.addStep(documentDetailsView, DOCUMENT_DETAILS);

		// BillEntryUploadDocumentsView uploadDocumentView =
		// uploadDocumentsViemImpl.get();
		uploadDocumentView = uploadDocumentsViemImpl.get();
		uploadDocumentView.init(this.bean);
		wizard.addStep(uploadDocumentView, "Upload Documents For Bill Entry");

		DetailsView billEntryDetailsView = billEntryViewImpl.get();
		billEntryDetailsView.init(bean);
		wizard.addStep(billEntryDetailsView, "Bill Entry");

		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);

		PreauthIntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.init(this.bean.getClaimDTO()
				.getNewIntimationDto(), this.bean.getClaimDTO(),
				"Enter Bill Details (Bill Entry)");
		mainPanel.setFirstComponent(intimationDetailsCarousel);

		VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());

		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		panel1.setHeight("50px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);

		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);

	}

	/*
	 * protected void localize(
	 * 
	 * @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final
	 * ParameterDTO parameterDto) { for (final String propertyId :
	 * arrScreenName) { final String header =
	 * tb.getText(textBundlePrefixString() +
	 * propertyId.toLowerCase());//String.valueOf(propertyId).toLowerCase());
	 * captionMap.put(propertyId, header); } }
	 * 
	 * 
	 * private String textBundlePrefixString() { return
	 * "acknowledge-document-received-"; }
	 */

	private HorizontalLayout commonButtonsLayout() {

		/*
		 * TextField acknowledgementNumber = new
		 * TextField("Acknowledgement Number");
		 * acknowledgementNumber.setValue(String
		 * .valueOf(this.bean.getAcknowledgementNumber()));
		 * //Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
		 * acknowledgementNumber.setWidth("250px");
		 * acknowledgementNumber.setHeight("20px");
		 * acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		 * acknowledgementNumber.setReadOnly(true);
		 * acknowledgementNumber.setEnabled(false);
		 * acknowledgementNumber.setNullRepresentation(""); FormLayout hLayout =
		 * new FormLayout (acknowledgementNumber);
		 * hLayout.setComponentAlignment(acknowledgementNumber,
		 * Alignment.MIDDLE_LEFT);
		 */

		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// viewDetails.getTranslationMiscRequest(bean.getNewIntimationDTO().getIntimationId());
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance
						.get();
				ViewDocumentDetailsDTO documentDetails = new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(), bean
						.getDocumentDetails().getRodKey());
				popup.setContent(earlierRodDetailsViewObj);
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
			}
		});

		FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);

		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		// ComboBox viewDetailsSelect = new ComboBox()
		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto()
				.getIntimationId(), bean.getDocumentDetails().getRodKey(),
				ViewLevels.PREAUTH_MEDICAL,"");
		viewDetailsForm.addComponent(viewDetails);

		// viewDetailsForm.addComponent(viewDetailsSelect);
		// Button goButton = new Button("GO");
		/*
		 * HorizontalLayout horizontalLayout1 = new HorizontalLayout(
		 * viewDetailsForm, goButton);
		 */
		/*
		 * HorizontalLayout horizontalLayout1 = new HorizontalLayout(
		 * viewDetailsForm); horizontalLayout1.setSizeUndefined();
		 * //Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
		 * horizontalLayout1.setSpacing(true);
		 */

		// HorizontalLayout componentsHLayout = new HorizontalLayout(hLayout,
		// viewEarlierRODLayout, viewDetailsForm);
		HorizontalLayout componentsHLayout = new HorizontalLayout(
				viewEarlierRODLayout, viewDetailsForm);
		// HorizontalLayout alignmentHLayout = new
		// HorizontalLayout(componentsHLayout);
		componentsHLayout.setWidth("100%");
		componentsHLayout.setComponentAlignment(viewEarlierRODLayout,
				Alignment.BOTTOM_RIGHT);
		return componentsHLayout; 
		
		
		
	}
	
	
	

	@Override
	public void resetView() {

		if (null != uploadDocumentView) {
			uploadDocumentView.resetPage();
		}

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
	public void wizardCompleted(WizardCompletedEvent event) {
		// TODO Auto-generated method stub
		fireViewEvent(WizardPresenter.SUBMIT_ADD_ADDITIONAL_DOCUMENTS_DETAILS,
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
							fireViewEvent(MenuItemBean.ENTER_BILL_DETAILS, null);
							// fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD,
							// null);
							// Confirmed to continue
							// fireViewEvent(MenuItemBean.PROCESS_PRE_MEDICAL,
							// null);
							// fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER,
							// null);
						} else {
							// User did not confirm
						}
					}
				});

		dialog.setStyleName(Reindeer.WINDOW_BLACK);

	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// fireViewEvent(WizardPresenter.SUBMIT_BILL_ENTRY_DETAILS, this.bean);

	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'>Claim record saved successfully !!!</b>",
				ContentMode.HTML);
		Label successEndLabel = new Label(
				"<b style = 'color: green;'>Pls note: Acknowledgement receipt would be shown for printing</b>",
				ContentMode.HTML);
		Button homeButton = new Button("Home Page");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);

		VerticalLayout layout = new VerticalLayout(successLabel,
				horizontalLayout, successEndLabel);
		layout.setComponentAlignment(successLabel, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
		layout.setComponentAlignment(successEndLabel, Alignment.MIDDLE_CENTER);
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
				fireViewEvent(MenuItemBean.ADD_ADDITIONAL_DOC, null);
			}
		});

	}

}
