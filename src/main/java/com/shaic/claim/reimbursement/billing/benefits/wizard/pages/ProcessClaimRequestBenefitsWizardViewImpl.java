package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;
/**
 * @author ntv.vijayar
 *
 */

import java.util.HashMap;

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

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.bpc.ViewBusinessProfileChart;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.FinancialDecisionCommunicationViewImpl;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
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
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class ProcessClaimRequestBenefitsWizardViewImpl extends AbstractMVPView implements
ProcessClaimRequestBenefitsWizard {
	
	private static final String DOCUMENT_DETAILS = "Document Details";

	@Inject
	private Instance<ProcessClaimRequestBenefitsDataExtractionView> documentDetailsViewImpl;
	
	@Inject
	private Instance<IWizard> iWizard;
	
	@Inject
	private Instance<ProcessClaimRequestBenefitsDecisionView> claimRequestBenefitsDecisionView;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	////private static Window popup;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	@Inject		
	private ViewBusinessProfileChart viewBusinessProfileChart;	
	
	/*@Inject
	private TextBundle tb;*/
	
	private VerticalSplitPanel mainPanel;
	
	private IWizard wizard;
	
	private FieldGroup binder;
	
	private ReceiptOfDocumentsDTO bean;
	
	private ProcessClaimRequestBenefitsDataExtractionView documentDetailsView;
	
	private ProcessClaimRequestBenefitsDecisionView claimRequestDecisionViewImpl;
//	private String[] arrScreenName ; 
	//private Map<String,String> captionMap ;
	
	@Inject
	private Instance<ProcessClaimRequestBenefitsDecisionCommunicationViewImpl> benefitsDecisionCommunicationViewImpl;
	
	private ProcessClaimRequestBenefitsDecisionCommunicationViewImpl benefitsDecisionCommunicationObj;
	
	@Inject
	private Toolbar toolBar;	
	
	private void initBinder() {
		
		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<ReceiptOfDocumentsDTO> item = new BeanItem<ReceiptOfDocumentsDTO>(bean);
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
		item.addNestedProperty("documentDetails.hospitalizationClaimedAmount");
		item.addNestedProperty("documentDetails.preHospitalizationClaimedAmount");
		item.addNestedProperty("documentDetails.postHospitalizationClaimedAmount");
/*		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");
		item.addNestedProperty("documentDetails.documentCheckList");*/
		this.binder.setItemDataSource(item);	
	}
	
	@PostConstruct
	public void initView() {
		addStyleName("view");
		
		setSizeFull();			
	}
	
	public void initView(ReceiptOfDocumentsDTO rodDTO)
	{
		this.bean = rodDTO;
		mainPanel = new VerticalSplitPanel();
		//this.wizard = iWizard.get();
		this.wizard = new IWizard();
		initBinder();	
		documentDetailsView = documentDetailsViewImpl.get();
		documentDetailsView.init(rodDTO,wizard);
		//acknowledgeReceiptViewImpl.init(rodDTO);
		wizard.addStep(documentDetailsView,DOCUMENT_DETAILS);
		
		claimRequestDecisionViewImpl = claimRequestBenefitsDecisionView.get();
		claimRequestDecisionViewImpl.init(rodDTO,wizard);
		wizard.addStep(this.claimRequestDecisionViewImpl,"Decision");
		
		if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null &&  rodDTO.getClaimDTO().getNewIntimationDto().getPolicy()
				.getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode() != null && 
						rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			if(null != rodDTO.getIsBillingScreen() && !rodDTO.getIsBillingScreen() && (null != rodDTO.getIsMedicalScreen() && !rodDTO.getIsMedicalScreen())){
		benefitsDecisionCommunicationObj = benefitsDecisionCommunicationViewImpl.get();
		benefitsDecisionCommunicationObj.init(this.bean, this.wizard);
		wizard.addStep(benefitsDecisionCommunicationObj, "Confirmation");
			}
		}
		//wizard.addStep(this.acknowledgeReceiptViewImpl,"Acknowledgement Receiwizapt");
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);	
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		
		if(this.bean.getDiagnosis() != null){
			this.bean.getClaimDTO().setDiagnosis(this.bean.getDiagnosis());
		}
		intimationDetailsCarousel.init(this.bean.getClaimDTO().getNewIntimationDto(), this.bean.getClaimDTO(),  "Process Claim Request (Benefits)");
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		TextField acknowledgementNumber = new TextField("Type");
		acknowledgementNumber.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
		acknowledgementNumber.setWidth("250px");
		acknowledgementNumber.setHeight("20px");
		acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		acknowledgementNumber.setReadOnly(true);
		acknowledgementNumber.setEnabled(false);
		acknowledgementNumber.setNullRepresentation("");
		
		FormLayout hLayout = new FormLayout (acknowledgementNumber);
		hLayout.setMargin(false);
//		hLayout.setComponentAlignment(acknowledgementNumber, Alignment.MIDDLE_LEFT);
		
		VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout(),hLayout);
		wizardLayout1.setSpacing(false);
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
//		panel1.setHeight("50px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		
		setCompositionRoot(mainPanel);			
		}

	
	private HorizontalLayout commonButtonsLayout()
	{
		
//		TextField acknowledgementNumber = new TextField("Type");
//		acknowledgementNumber.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
//				.getStatusKey()));
//		//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
//		acknowledgementNumber.setWidth("250px");
//		acknowledgementNumber.setHeight("20px");
//		acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		acknowledgementNumber.setReadOnly(true);
//		acknowledgementNumber.setEnabled(false);
//		acknowledgementNumber.setNullRepresentation("");
//		
	/*	TextField typeFld = new TextField("Type");
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setNullRepresentation("");
		typeFld.setReadOnly(true);
		FormLayout hLayout = new FormLayout (typeFld);
		hLayout.setComponentAlignment(typeFld, Alignment.MIDDLE_LEFT);*/
		
//		FormLayout hLayout = new FormLayout (acknowledgementNumber);
//		hLayout.setComponentAlignment(acknowledgementNumber, Alignment.MIDDLE_LEFT);
		
		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),null);
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
		
		Button btnBusinessProfileChart = new Button("View Mini Business Profile");
		btnBusinessProfileChart.setStyleName(ValoTheme.BUTTON_DANGER);
		btnBusinessProfileChart.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				viewBusinessProfileChart.init(bean.getPreauthDTO());
				
				bean.getPreauthDTO().setIsBusinessProfileClicked(true);
				Button button = (Button)event.getSource();
				button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("VIEW MINI BUSINESS PROFILE");
				popup.setWidth("35%");
				popup.setHeight("75%");
				popup.setContent(viewBusinessProfileChart);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
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
		
		
//		FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
		
		viewDetails.initView(bean.getClaimDTO().getNewIntimationDto().getIntimationId(),bean.getDocumentDetails().getRodKey(), ViewLevels.PREAUTH_MEDICAL,"Process Claim Request (Benefits)");  //need to implements
		viewDetailsForm.addComponent(viewDetails);
		
		
	//	viewDetailsForm.addComponent(viewDetailsSelect);
	//	Button goButton = new Button("GO");
		/*HorizontalLayout horizontalLayout1 = new HorizontalLayout(
				viewDetailsForm, goButton);*/
	/*	HorizontalLayout horizontalLayout1 = new HorizontalLayout(
				viewDetailsForm);
		horizontalLayout1.setSizeUndefined();
		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
		horizontalLayout1.setSpacing(true);*/
		Label dummyLabel=new Label();
		dummyLabel.setWidth("250px");
		
		HorizontalLayout componentsHLayout = new HorizontalLayout(viewEarlierRODDetails,btnBusinessProfileChart,dummyLabel, viewDetailsForm);
		
		componentsHLayout.setComponentAlignment(viewDetailsForm, Alignment.BOTTOM_RIGHT);
		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
		componentsHLayout.setWidth("100%");
//		componentsHLayout.setComponentAlignment(viewEarlierRODLayout, Alignment.BOTTOM_RIGHT);
		return componentsHLayout;
	}
	
	public void initDTO(ReceiptOfDocumentsDTO dto)
	{
		this.bean = dto;
	}
	

	@Override
	public void resetView() {
		
		/*if(null != this.wizard && !this.wizard.getSteps().isEmpty())
		{
			this.wizard.clearWizardMap(DOCUMENT_DETAILS);
			this.wizard.clearWizardMap("Acknowledgement Receipt");
		//	this.wizard.clearWizardMap(DOCUMENT_DETAILS);
			this.wizard.clearCurrentStep();
		}*/
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
		if(bean.getIsMedicalScreen() != null && bean.getIsMedicalScreen()){
		if(ReferenceTable.getMedicalDecisionButtonStatus().containsKey(bean.getPreauthDTO().getStatusKey())){
			fireViewEvent(ProcessClaimRequestBenefitsWizardPresenter.SUBMIT_PROCESS_CLAIM_REQUEST_BENEFITS, this.bean);
		} else {
			confirmationForWaitForInput(this.bean);
		 }
		}
		else {
			fireViewEvent(ProcessClaimRequestBenefitsWizardPresenter.SUBMIT_PROCESS_CLAIM_REQUEST_BENEFITS, this.bean);
		}
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
		        "Cancel", "Ok", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                    // Confirmed to continue
		                	//fireViewEvent(MenuItemBean.PROCESS_PRE_MEDICAL, null);
		                	wizard.releaseHumanTask();
		                	if(bean.getIsBillingScreen() != null && bean.getIsBillingScreen()){
		                		fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING, null);
		                	}else if (bean.getIsMedicalScreen() != null && bean.getIsMedicalScreen()){
		                		if(null != bean.getPreauthDTO().getScreenName() && !(SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getPreauthDTO().getScreenName()))){
		                		  fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST, null);
		                		}
		                		else{
		                			fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, null);
		                		}
		                	}
		                	else if (bean.getIsMedicalScreen() != null && ! bean.getIsMedicalScreen() 
		                			&& (bean.getIsBillingScreen() != null && ! bean.getIsBillingScreen()) ){
		                		fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS, null);
		                	}
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
		fireViewEvent(ProcessClaimRequestBenefitsWizardPresenter.SUBMIT_PROCESS_CLAIM_REQUEST_BENEFITS, this.bean);

		
	}

	@Override
	public void buildSuccessLayout(String strMessage,final Boolean isError ) {
		Label successLabel = new Label("<b style = 'color: green;'>"+ strMessage +"</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Process Claim Request Benefits Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		/*HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);*/
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				//IMSSUPPOR-30808
				if(toolBar != null){
					toolBar.countTool();
				}
				if(bean.getIsMedicalScreen() != null && bean.getIsMedicalScreen())
				{
				    fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST, null);
				}
				else if(bean.getIsBillingScreen() != null && bean.getIsBillingScreen() && !isError){
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING, null);
				}else if(!isError){
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_FINANCIALS, null);
				}
				
			}
		});
		
		
		
		
	
		
	}
	
	public void confirmationForWaitForInput(ReceiptOfDocumentsDTO rodDTO) {			
		
		String message = "The claim would be moved to wait for input Q. Do you want to proceed further?";

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createConfirmationbox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
	
			@Override
			public void buttonClick(ClickEvent event) {
				rodDTO.getDocumentDetails().setStatusId(ReferenceTable.WAIT_FOR_INPUT_KEY);
				fireViewEvent(ProcessClaimRequestBenefitsWizardPresenter.SUBMIT_PROCESS_CLAIM_REQUEST_BENEFITS, rodDTO);
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
	
			@Override
			public void buttonClick(ClickEvent event) {
				wizard.getFinishButton().setEnabled(true);
			}
		});
	}
	

}

