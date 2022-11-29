package com.shaic.paclaim.health.reimbursement.financial.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.rod.billing.pages.BillingWorksheetUploadDocumentsViewImpl;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.tables.ReconsiderRODRequestListenerTable;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillAssessmentSheet;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPAMedicalSummaryPage;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.shaic.paclaim.health.reimbursement.financial.pages.billinghospitalization.PAHealthFinancialHospitalizationPageViewImpl;
import com.shaic.paclaim.health.reimbursement.financial.pages.billingprocess.PAHealthFinancialProcessPageViewImpl;
import com.shaic.paclaim.health.reimbursement.financial.pages.billreview.PAHealthFinancialReviewPageViewImpl;
import com.shaic.paclaim.health.reimbursement.financial.pages.billsummary.PAHealthFinancialSummaryPageViewImpl;
import com.shaic.paclaim.health.reimbursement.financial.pages.communicationpage.PAHealthFinancialDecisionCommunicationViewImpl;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.zybnet.autocomplete.server.AutocompleteField;

public class PAHealthFinancialWizardViewImpl extends AbstractMVPView implements PAHealthFinancialWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private Instance<IWizard> iWizard;
	
	private IWizardPartialComplete wizard;
	
//	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private PreauthDTO bean;
	
	private VerticalSplitPanel mainPanel;
	
	private TextArea remarksTxt;
	
	private VerticalLayout remarskvertical;
	
	private Panel insuredPedDetailsPanel;
	
	
	private VerticalLayout reconsiderationLayout;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private BillingWorksheetUploadDocumentsViewImpl uploadDocumentViewImpl;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	@Inject
	private Instance<PAHealthFinancialReviewPageViewImpl> billingReviewPageViewImpl;
	
	@Inject
	private Instance<PAHealthFinancialSummaryPageViewImpl> billingSummaryPageViewImpl;
	
	@Inject
	private Instance<PAHealthFinancialProcessPageViewImpl> billingProcessPageViewImpl;
	
	@Inject
	private Instance<PAHealthFinancialHospitalizationPageViewImpl> billingHospitalizationViewImpl;
	
	@Inject
	private Instance<PAHealthFinancialDecisionCommunicationViewImpl> financialDecisionCommunicationViewImpl;
	
	private PAHealthFinancialDecisionCommunicationViewImpl financialDecisionCommunicationObj;
	
	private PAHealthFinancialReviewPageViewImpl billingReviewPageViewImplObj;
	
	private PAHealthFinancialSummaryPageViewImpl billingSummaryPageViewImplObj;
	
	private PAHealthFinancialProcessPageViewImpl billingProcessPageViewImplObj;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	@Inject
	private ViewPAMedicalSummaryPage viewPAMedicalSummaryPage;
	
	@Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	@Inject
	private ViewBillAssessmentSheet viewBillAssessmentSheet;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	private ViewPEDRequestWindow viewPEDRequestObj;
	
	private PAHealthFinancialHospitalizationPageViewImpl billingHospitalizationPageViewImplObj;
	
    private TextArea txtBillingRemarks;
    
    private TextArea txtAreaBillingInternalRemarks;
	
	private TextArea txtMedicalRemarks;
	
	private FormLayout forms;

	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	
	@Inject
	private ReconsiderRODRequestListenerTable reconsiderRequestDetails;
	
	private ComboBox cmbReasonForReconsideration;
	 
	private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	
	private ComboBox cmbReconsiderationRequest;
	
	private BeanItemContainer<SelectValue> reconsiderationRequest;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Toolbar toolBar;
	
	private void initBinder() {
		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<PreauthDTO> item = new BeanItem<PreauthDTO>(bean);
		item.addNestedProperty("preauthDataExtractionDetails");
		item.addNestedProperty("coordinatorDetails");
		item.addNestedProperty("preauthPreviousClaimsDetails");
//		item.addNestedProperty("preauthMedicalDecisionDetails");

		item.addNestedProperty("preauthDataExtractionDetails.reasonForAdmission");
		item.addNestedProperty("preauthDataExtractionDetails.admissionDate");
		item.addNestedProperty("preauthDataExtractionDetails.noOfDays");
		item.addNestedProperty("preauthDataExtractionDetails.natureOfTreatment");
		item.addNestedProperty("preauthDataExtractionDetails.firstConsultantDate");
		item.addNestedProperty("preauthDataExtractionDetails.corpBuffer");
		item.addNestedProperty("preauthDataExtractionDetails.criticalIllness");
		item.addNestedProperty("preauthDataExtractionDetails.specifyIllness");
		item.addNestedProperty("preauthDataExtractionDetails.roomCategory");
		item.addNestedProperty("preauthDataExtractionDetails.ventilatorSupport");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentType");
		item.addNestedProperty("preauthDataExtractionDetails.treatmentRemarks");

		item.addNestedProperty("preauthPreviousClaimsDetails.relapseOfIllness");
		item.addNestedProperty("preauthPreviousClaimsDetails.relapseRemarks");
		item.addNestedProperty("preauthPreviousClaimsDetails.attachToPreviousClaim");
//
//		item.addNestedProperty("preauthMedicalDecisionDetails.investigationReportReviewed");
//		item.addNestedProperty("preauthMedicalDecisionDetails.investigationReviewRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.investigatorName");
//		item.addNestedProperty("preauthMedicalDecisionDetails.initiateFieldVisitRequest");
//		item.addNestedProperty("preauthMedicalDecisionDetails.fvrNotRequiredRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistOpinionTaken");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistType");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistConsulted");
//		item.addNestedProperty("preauthMedicalDecisionDetails.remarksBySpecialist");
//		item.addNestedProperty("preauthMedicalDecisionDetails.allocationTo");
//		item.addNestedProperty("preauthMedicalDecisionDetails.fvrTriggerPoints");
//		item.addNestedProperty("preauthMedicalDecisionDetails.specialistOpinionTakenFlag");
//		item.addNestedProperty("preauthMedicalDecisionDetails.initiateFieldVisitRequestFlag");
//
//		item.addNestedProperty("preauthMedicalDecisionDetails.approvedAmount");
//		item.addNestedProperty("preauthMedicalDecisionDetails.selectedCopay");
//		item.addNestedProperty("preauthMedicalDecisionDetails.initialTotalApprovedAmt");
//		item.addNestedProperty("preauthMedicalDecisionDetails.approvalRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.queryRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionCategory");
//		item.addNestedProperty("preauthMedicalDecisionDetails.rejectionRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForDenial");
//		item.addNestedProperty("preauthMedicalDecisionDetails.denialRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.escalateTo");
//		// item.addNestedProperty("preauthMedicalDecisionDetails.upLoadFile");
//		item.addNestedProperty("preauthMedicalDecisionDetails.escalationRemarks");
//		item.addNestedProperty("preauthMedicalDecisionDetails.typeOfCoordinatorRequest");
//		item.addNestedProperty("preauthMedicalDecisionDetails.reasonForRefering");
//
//		item.addNestedProperty("preauthMedicalDecisionDetails.sentToCPU");
//		item.addNestedProperty("preauthMedicalDecisionDetails.remarksForCPU");

		item.addNestedProperty("coordinatorDetails.refertoCoordinator");
		item.addNestedProperty("coordinatorDetails.typeofCoordinatorRequest");
		item.addNestedProperty("coordinatorDetails.reasonForRefering");
		this.binder.setItemDataSource(item);	
	}
	
	@PostConstruct
	public void initView() {

		addStyleName("view");
		setSizeFull();			
	}
	
	public void initView(PreauthDTO bean)
	{
		this.bean = bean;
		mainPanel = new VerticalSplitPanel();
		//this.wizard = iWizard.get();
		this.wizard = new IWizardPartialComplete();
//		this.wizard = new IWizardPartialComplete();
		initBinder();
		billingReviewPageViewImplObj = billingReviewPageViewImpl.get();
		billingReviewPageViewImplObj.init(this.bean , wizard);
		wizard.addStep(billingReviewPageViewImplObj,"Bill Review");
		
		billingSummaryPageViewImplObj = billingSummaryPageViewImpl.get();
		billingSummaryPageViewImplObj.init(this.bean);
		wizard.addStep(billingSummaryPageViewImplObj,"View Bill Summary");
		
		billingProcessPageViewImplObj = billingProcessPageViewImpl.get();
		billingProcessPageViewImplObj.init(this.bean);
		wizard.addStep(billingProcessPageViewImplObj,"Billing Process");
		
		billingHospitalizationPageViewImplObj = billingHospitalizationViewImpl.get();
		billingHospitalizationPageViewImplObj.init(this.bean,this.wizard);
		wizard.addStep(billingHospitalizationPageViewImplObj,"Biling Hospitalization");
		
		financialDecisionCommunicationObj = financialDecisionCommunicationViewImpl.get();
		financialDecisionCommunicationObj.init(this.bean, this.wizard);
		wizard.addStep(financialDecisionCommunicationObj, "Confirmation");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "Process Claim Financials");
		intimationDetailsCarousel.init(this.bean,  "Process Claim Financials - Hospitalisation");
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),bean.getKey(), ViewLevels.PA_PROCESS,"Process Claim Financials");
		HorizontalLayout hLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		hLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		hLayout.setWidth("100%");
		VerticalLayout wizardLayout1 = new VerticalLayout(hLayout);
//		wizardLayout1.setHeight("250px");
		
	    forms = new FormLayout();
	    
	    
	    txtBillingRemarks = new TextArea("Billing Remarks");
	    txtBillingRemarks.setEnabled(false);
	    
	    txtMedicalRemarks = new  TextArea("Medical Remarks");
	    txtMedicalRemarks.setEnabled(false);
	    
	    FormLayout medicalFormLayout = new FormLayout();
	    FormLayout billingFormLayout = new FormLayout();
//	    
	    if(this.bean.getIsReBilling()){
	    	txtBillingRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getBillingRemarks());
	    	txtBillingRemarks.setNullRepresentation("-");
	    	medicalFormLayout.addComponent(txtBillingRemarks);
	    	medicalFormLayout.setCaption("Billing");
	    	//medicalFormLayout.setHeight("150px");
	    }else {
	    	
	    	txtBillingRemarks.setValue(this.bean.getPreauthMedicalDecisionDetails().getBillingRemarks());
	    	txtBillingRemarks.setNullRepresentation("");
	    	medicalFormLayout.addComponent(txtBillingRemarks);
	    	medicalFormLayout.setCaption("Billing");
	    }
	    	
	    if(this.bean.getIsReMedical()){
	    	txtMedicalRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getMedicalRemarks());
	    	billingFormLayout.addComponent(txtMedicalRemarks);
    	    billingFormLayout.setCaption("Medical");
    	//   billingFormLayout.setHeight("800px");
	    	
	    }
	    
	    // Billing Internal Remarks
 		txtAreaBillingInternalRemarks = (TextArea) binder.buildAndBind("Billing Internal Remarks", "billingInternalRemarks", TextArea.class);
 		txtAreaBillingInternalRemarks.setMaxLength(4000);
 		txtAreaBillingInternalRemarks.setNullRepresentation("");
 		txtAreaBillingInternalRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");
 		
 		if(this.bean.getPreauthDataExtractionDetails().getBillingInternalRemarks() != null) {
 			txtAreaBillingInternalRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getBillingInternalRemarks());
 		}
 		txtAreaBillingInternalRemarks.setReadOnly(true);
 		billingInternalRemarksChangeListener(txtAreaBillingInternalRemarks, null);
 	    
 	    medicalFormLayout.addComponent(txtAreaBillingInternalRemarks);
	    
	    HorizontalLayout remarksHorizontal = new HorizontalLayout(medicalFormLayout,billingFormLayout);
	    remarksHorizontal.setWidth("700px");
	  //  remarksHorizontal.setHeight("900px");
	  // remarksHorizontal.setHeight("700px");
//	    
//	    remarksHorizontal.setSpacing(true);
//	    
	    forms.addComponent(remarksHorizontal);
	    
//		TextField field = new TextField("Billing Remarks");
//		field.setValue(this.bean.getPreauthMedicalDecisionDetails().getBillingRemarks() != null ? this.bean.getPreauthMedicalDecisionDetails().getBillingRemarks() : "");
		wizardLayout1.addComponent(forms);
		
		wizardLayout1.setSpacing(true);
		//wizardLayout1.setHeight("10px");
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
//		panel1.setHeight("500px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
		if(this.bean.getTaskNumber() != null){
			/*String aciquireByUserId = SHAUtils.getAciquireByUserId(bean.getTaskNumber());
			if((bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
//				compareWithUserId(aciquireByUserId);
			}*/
		}
	}
	
	private void showPopup(Layout layout) {
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(layout);
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
	
	private VerticalLayout commonButtonsLayout()
	{
		TextField typeFld = new TextField("Type");
//		typeFld.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setNullRepresentation("");
	
		if(this.bean.getIsCashlessType() && this.bean.getHospitalizaionFlag()&& (this.bean.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)||this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS))){
			typeFld.setValue(SHAConstants.DIRECT_TO_FINANCIAL);
		}
		typeFld.setReadOnly(true);
		
		TextField hospitalNtwrktype = new TextField("Hospital Network Type");
		hospitalNtwrktype.setNullRepresentation("-");
		hospitalNtwrktype.setValue(this.bean.getNetworkHospitalType());
		hospitalNtwrktype.setReadOnly(true);
//		hospitalNtwrktype.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		
		FormLayout hLayout = new FormLayout (typeFld,hospitalNtwrktype);
		hLayout.setSpacing(false);
		hLayout.setMargin(false);
		hLayout.setComponentAlignment(typeFld, Alignment.MIDDLE_LEFT);
		
		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
				showPopup(new VerticalLayout(earlierRodDetailsViewObj));
			}
			
		});
		
		Button viewMedicalSummaryButton = new Button("View Medical Summary");
		viewMedicalSummaryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				viewPAMedicalSummaryPage.init(bean.getKey());
				
				showPopup(new VerticalLayout(viewPAMedicalSummaryPage));
			}
			
		});
		
		Button billingWorksheetBtn = new Button("Billing Worksheet");
		
		billingWorksheetBtn = new Button("Billing Worksheet");
		billingWorksheetBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7439398898591124006L;

			@Override
			public void buttonClick(ClickEvent event) {
				

				Window popup = new com.vaadin.ui.Window();
				
				uploadDocumentViewImpl.initPresenter(SHAConstants.BILLING_WORKSHEET);
				uploadDocumentViewImpl.init(bean,popup);
				popup.setCaption("Billing Worksheet");
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setClosable(true);
				popup.setContent(uploadDocumentViewImpl);
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
				
			/**
			 * Commenting it as per sathish sir advice. In Billing worksheet pdf shouldn't 
			 * be showed, rather the file upload component should be shown.
			 * */
				
			//	fireViewEvent(FinancialReviewPagePresenter.VIEW_BILLING_WORKSHEET, bean);
				
			}
		});
		
		/**
		 * view bill summary button is commanded as per issue ticket no : 2820
		 */
		
//		Button viewBillSummaryButton = new Button("View Bill Summary");
//		viewBillSummaryButton.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
//				
//				
//				viewBillSummaryPage.init(bean,bean.getKey(),true);
//				Panel mainPanel = new Panel(viewBillSummaryPage);
//		        mainPanel.setWidth("2000px");
//		        popup = new com.vaadin.ui.Window();
//				popup.setCaption("");
//			//	popup.setWidth("75%");
//				//popup.setHeight("85%");
//				popup.setSizeFull();
//				popup.setContent(mainPanel);
//				popup.setClosable(true);
//				popup.center();
//				popup.setResizable(false);
//				popup.addCloseListener(new Window.CloseListener() {
//					/**
//					 * 
//					 */
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void windowClose(CloseEvent e) {
//						System.out.println("Close listener called");
//					}
//				});
//
//				popup.setModal(true);
//				UI.getCurrent().addWindow(popup);
//			}
//			
//		});
		
//		Button viewBillAssessmentBtn = new Button("Billing Assessment Sheet");
	
//		if(bean.getBillingDate() != null){
//			
//			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.PREAUTH_DTO,bean);					
//			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE,viewBillAssessmentSheet);
//			
//			BrowserWindowOpener opener = new BrowserWindowOpener(BillAssessmentUI.class);
//			opener.setFeatures("height=900,width=1300,resizable");
//		    opener.extend(viewBillAssessmentBtn);
//		    
//		}	
		
//		viewBillAssessmentBtn.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
//				if(bean.getBillingDate() != null){
//					
//					VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.PREAUTH_DTO,bean);					
//					VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE,viewBillAssessmentSheet);
//					
//				} else {
//					getErrorMessage("Billing Assessment Sheet is not applicable");
//				}
//			}
//			
//		});
		
		Button viewIRDABillSummaryButton = new Button("View IRDA Bill Summary");
		viewIRDABillSummaryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
//				viewBillSummaryPage.init(bean.getKey());
//				showPopup(new VerticalLayout(viewBillSummaryPage));
			}
			
		});
		
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				validateUserForRRCRequestIntiation();
				
				/*popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("85%");
				popup.setHeight("100%");
				rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
				//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				//documentDetails.setClaimDto(bean.getClaimDTO());
				rewardRecognitionRequestViewObj.initPresenter(SHAConstants.FINANCIAL);
				rewardRecognitionRequestViewObj.init(bean, popup);
				
				//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
				popup.setCaption("Reward Recognition Request");
				popup.setContent(rewardRecognitionRequestViewObj);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					*//**
					 * 
					 *//*
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);*/
				
				
			}
			
		});
		
		
		/**
		 * View Policy Schedule button added according to CR20181321
		 */
		
        Button btnViewPolicySchedule = new Button("View Policy Schedule");
        btnViewPolicySchedule.setStyleName(ValoTheme.BUTTON_DANGER);
		btnViewPolicySchedule.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getViewPolicySchedule(bean.getNewIntimationDTO().getIntimationId());
				bean.setIsScheduleClicked(true);
				Button button = (Button)event.getSource();
				button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			}
		});
		
		Button pedButton = new Button("Initiate PED Endorsement");
		pedButton.setEnabled(false);
		pedButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(bean.getIsPEDInitiatedForBtn()) {
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
				} else {
					viewPEDRequestObj = viewPedRequest.get();
					viewPEDRequestObj.initView(bean, bean.getKey(), bean.getIntimationKey(), bean.getPolicyKey(), bean.getClaimKey(),ReferenceTable.FINANCIAL_STAGE,false);
					viewPEDRequestObj.setPresenterString(SHAConstants.REIMBURSEMENT_SCREEN);
					showPopup(new VerticalLayout(viewPEDRequestObj));
				}
				
//				showPopup(new VerticalLayout(viewPEDRequestObj));
			}
			
		});
		
		
		
		HorizontalLayout viewEarlierRODLayout = new HorizontalLayout(btnRRC , viewMedicalSummaryButton/*, viewBillSummaryButton*/, billingWorksheetBtn, btnViewPolicySchedule);
		viewEarlierRODLayout.setSpacing(true);
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		FormLayout clubMembershipLayout = new FormLayout(cmdClubMembership);
		clubMembershipLayout.setMargin(false);
		
		if (null != memberType && !memberType.isEmpty()) {
			viewEarlierRODLayout.addComponent(clubMembershipLayout);
		}
		
		HorizontalLayout icrAgentBranch = SHAUtils.icrAgentBranchForPA(bean);
		
//		HorizontalLayout viewEarlierRODLayout1 = new HorizontalLayout(viewIRDABillSummaryButton,viewBillAssessmentBtn,pedButton, viewEarlierRODDetails);
		HorizontalLayout viewEarlierRODLayout1 = new HorizontalLayout(icrAgentBranch,viewIRDABillSummaryButton,pedButton, viewEarlierRODDetails);
		viewEarlierRODLayout1.setSpacing(true);
		//VerticalLayout verticalLayout = new VerticalLayout(viewEarlierRODLayout,viewEarlierRODLayout1);
		//viewEarlierRODLayout.setComponentAlignment(btnRRC, Alignment.TOP_RIGHT);
		//viewEarlierRODLayout.setSpacing(true);
	//	HorizontalLayout layout = new HorizontalLayout(viewEarlierRODLayout);
		//layout.setComponentAlignment(viewEarlierRODLayout, Alignment.MIDDLE_RIGHT);
		/*FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
*/	
		
		HorizontalLayout componentsHLayout = new HorizontalLayout(/*hLayout*/viewEarlierRODLayout);
	//	componentsHLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		//		HorizontalLayout horizontalLayout = new HorizontalLayout(viewEarlierRODLayout);
//		horizontalLayout.setComponentAlignment(viewEarlierRODLayout, Alignment.BOTTOM_RIGHT);

//		VerticalLayout newVLayout = new VerticalLayout(commonValues());
//		newVLayout.setSpacing(false);
//		newVLayout.setMargin(false);
		
		
		remarksTxt = new TextArea("Doctor Remarks");
		remarksTxt.setHeight("70%");
		remarksTxt.setValue(bean.getDoctorNote() != null? bean.getDoctorNote(): "" );
		remarksTxt.setDescription(bean.getDoctorNote() != null? bean.getDoctorNote(): "" );
		remarksTxt.setReadOnly(true);
		
		HorizontalLayout horizontalLayout = null;
		if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) {
			insuredPedDetailsPanel = getInsuredPedDetailsPanel();
			remarskvertical = new VerticalLayout(insuredPedDetailsPanel,remarksTxt);
			if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && ! this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag().equalsIgnoreCase("Y")){
				horizontalLayout = new HorizontalLayout(hLayout,new FormLayout(commonValues()),remarskvertical);
			}else{
				horizontalLayout = new HorizontalLayout(hLayout,new FormLayout(commonValues()));
			}
			
		}else{
			if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && ! this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag().equalsIgnoreCase("Y")){
				horizontalLayout = new HorizontalLayout(hLayout,new FormLayout(commonValues()),remarksTxt);
			}else{
				horizontalLayout = new HorizontalLayout(hLayout,new FormLayout(commonValues()));
			}
		}
		
		horizontalLayout.setSpacing(true);
		VerticalLayout verticalLayout = new VerticalLayout(componentsHLayout,viewEarlierRODLayout1,horizontalLayout);
		verticalLayout.setSpacing(true);
		//verticalLayout.setComponentAlignment(layout, Alignment.BOTTOM_RIGHT);

		/*VerticalLayout verticalLayout = new VerticalLayout( componentsHLayout , commonValues() , layout);
		verticalLayout.setSpacing(true);
		//Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		verticalLayout.setComponentAlignment(layout, Alignment.BOTTOM_RIGHT);*/

		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
	//	componentsHLayout.setWidth("100%");
//		horizontalLayout.setWidth("100%");
		return verticalLayout;
	}
	
	private Panel getInsuredPedDetailsPanel(){
		
		
		Table table = new Table();
		table.setWidth("80%");
		table.addContainerProperty("pedCode", String.class, null);
		table.addContainerProperty("pedDescription",  String.class, null);
		table.setCaption("PED Details");
		
		int i=0;
//		table.setStyleName(ValoTheme.TABLE_NO_HEADER);
		if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) {
			for (InsuredPedDetails pedDetails : this.bean.getInsuredPedDetails()) {
				
				table.addItem(new Object[]{pedDetails.getPedCode(), pedDetails.getPedDescription()}, i+1);
				i++;

			}
		}
		
		
		table.setPageLength(2);
		table.setColumnHeader("pedCode", "PED Code");
		table.setColumnHeader("pedDescription", "Description");
		table.setColumnWidth("pedCode", 80);
		table.setColumnWidth("pedDescription", 250);
		
		Panel tablePanel = new Panel(table);
		return tablePanel;
	}
	
	public Boolean alertMessageForPEDInitiate(String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
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
				Long preauthKey=bean.getKey();
				Long intimationKey=bean.getIntimationKey();
				Long policyKey=bean.getPolicyKey();
				Long claimKey=bean.getClaimKey();
				viewPEDRequestObj = viewPedRequest.get();
				viewPEDRequestObj.initView(bean, bean.getKey(), bean.getIntimationKey(), bean.getPolicyKey(), bean.getClaimKey(),ReferenceTable.FINANCIAL_STAGE,false);
				viewPEDRequestObj.setPresenterString(SHAConstants.REIMBURSEMENT_SCREEN);
				showPopup(new VerticalLayout(viewPEDRequestObj));
			}
		});
		return true;
	}
	
	public void getErrorMessage(String eMsg){
		
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
		dialog.show(getUI().getCurrent(), null, true);
	}
	
private VerticalLayout commonValues() {
		
		cmbReconsiderationRequest = new ComboBox("Reconsideration Request");
		//cmbReconsiderationRequest = binder.buildAndBind("Reconsideration Request", "reconsiderationRequest", ComboBox.class);
		//Vaadin8-setImmediate() cmbReconsiderationRequest.setImmediate(true);		
		String reconsiderationFlag = "";
		List<SelectValue> values = new ArrayList<SelectValue>();
		SelectValue value = new SelectValue();
		if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && ("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()))
		{
			reconsiderationFlag = "Yes";
			value.setId(1l);
			value.setValue("Yes");
		}
		else
		{
			reconsiderationFlag = "No";
			value.setId(1l);
			value.setValue("No");
		}
		values.add(value);
		BeanItemContainer<SelectValue> beanContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		beanContainer.addAll(values);
		cmbReconsiderationRequest.setContainerDataSource(beanContainer);
		cmbReconsiderationRequest.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReconsiderationRequest.setItemCaptionPropertyId("value");
		cmbReconsiderationRequest.setReadOnly(false);
		
		for(int i = 0 ; i<beanContainer.size() ; i++)
		{
			 if(null != beanContainer.getIdByIndex(i) && null != beanContainer.getIdByIndex(i).getValue())
			 {
				if ((reconsiderationFlag).equalsIgnoreCase(beanContainer.getIdByIndex(i).getValue().trim()))
				{
					this.cmbReconsiderationRequest.setValue(beanContainer.getIdByIndex(i));
				}
			 }
		}
		cmbReconsiderationRequest.setReadOnly(true);
		
		//cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
		cmbReasonForReconsideration = new ComboBox("Reason for Reconsideration");
		List<SelectValue> reconsiderValues = new ArrayList<SelectValue>();
		SelectValue selValue = new SelectValue();
		String reasonForReconsideration = "";
		if(null  != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId())
		{
			reasonForReconsideration = this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getValue();
			selValue.setId(1l);
			selValue.setValue(reasonForReconsideration);
		}
		reconsiderValues.add(selValue);
		BeanItemContainer<SelectValue> reconsiderBeanContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		reconsiderBeanContainer.addAll(reconsiderValues);
		cmbReasonForReconsideration.setContainerDataSource(reconsiderBeanContainer);
		cmbReasonForReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonForReconsideration.setItemCaptionPropertyId("value");
		cmbReasonForReconsideration.setReadOnly(false);

		 if(null != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId())
		 {
			 reasonForReconsideration = this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getValue();
		 }
		
		 for(int i = 0 ; i<reconsiderBeanContainer.size() ; i++)
		 	{
				if ((reasonForReconsideration).equalsIgnoreCase(reconsiderBeanContainer.getIdByIndex(i).getValue()))
				{
					this.cmbReasonForReconsideration.setValue(reconsiderBeanContainer.getIdByIndex(i));
				}
			}
		 cmbReasonForReconsideration.setReadOnly(true);
		
		this.reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		reconsiderRequestDetails.initPresenter(SHAConstants.ZONAL_REVIEW);
		reconsiderRequestDetails.init();
		reconsiderRequestDetails.setViewDetailsObj(viewDetails);
		if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
		{
			for (ReconsiderRODRequestTableDTO reconsiderList : reconsiderRODRequestList) {
				//if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
				{
					//Boolean isSelect = reconsiderationMap.get(reconsiderList.getAcknowledgementNo());
					reconsiderList.setSelect(true);
				}
				//reconsiderList.setSelect(null);
				reconsiderRequestDetails.addBeanToList(reconsiderList);
			}
			//reconsiderRequestDetails.setTableList(reconsiderRODRequestList);
		}
		reconsiderRequestDetails.setEnabled(false);
		
		
		HorizontalLayout vLayout = new HorizontalLayout(reconsiderRequestDetails);
		vLayout.setMargin(true);
		vLayout.setCaption("Earlier Request Reconsidered");
		
		FormLayout fLayout = new FormLayout(cmbReconsiderationRequest);
		fLayout.setSpacing(false);
		fLayout.setMargin(false);
		FormLayout fLayout1 = new FormLayout(cmbReasonForReconsideration);
		
		
		if(("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()))
		{
			if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty() && remarskvertical != null) {
				reconsiderationLayout = new VerticalLayout(fLayout,fLayout1, vLayout,remarskvertical);
			}else{
				reconsiderationLayout = new VerticalLayout(fLayout,fLayout1, vLayout,remarksTxt);
			}
			
		}
		else
		{
			reconsiderationLayout = new VerticalLayout(fLayout);
		}
		
		return reconsiderationLayout;
	}



	@Override
	public void resetView() {
		/*if(null != this.wizard && !this.wizard.getSteps().isEmpty())
		{
			this.wizard.clearWizardMap("Bill Review");
			this.wizard.clearWizardMap("View Bill Summary");
			this.wizard.clearWizardMap("Biliing Process");
			this.wizard.clearWizardMap("Biling Hospitalization");
			this.wizard.clearCurrentStep();
		}*/
	}

	@Override
	public void wizardSave(GWizardEvent event) {
		// TODO Auto-generated method stub
		
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
		
		if(this.bean.getIsHospitalDiscountApplicable() && 
				this.bean.getHospitalizationCalculationDTO().getHospitalDiscount() != null && this.bean.getHospitalizationCalculationDTO().getHospitalDiscount() == 0){
			confirmMessageForHospitalDiscount();
		}else{
			fireViewEvent(PAHealthFinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, this.bean);
		}
		
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
									// Confirmed to continue
									releaseHumanTask();
									fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_FINANCIALS,
											null);
									VaadinRequest currentRequest = VaadinService.getCurrentRequest();
									SHAUtils.clearSessionObject(currentRequest);
								} else {
									// User did not confirm
								}
							}
						});

		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}
	
	 private void releaseHumanTask(){
			
			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
	     	/*String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
	 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);*/
	 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

	 		if(existingTaskNumber != null){
	 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
	 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	 		}
	 		
	 		if(wrkFlowKey != null){
	 			DBCalculationService dbService = new DBCalculationService();
	 			dbService.callUnlockProcedure(wrkFlowKey);
	 			getSession().setAttribute(SHAConstants.WK_KEY, null);
	 		}
		}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim record saved successfully !!!</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Financial Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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
				toolBar.countTool();
				fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_FINANCIALS, null);
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);

			}
		});
		
	}
	
	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	@Override
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;	
	}

	
	@Override
	public void loadEmployeeMasterData(
			AutocompleteField<EmployeeMasterDTO> field,
			List<EmployeeMasterDTO> employeeDetailsList) {
		
		rewardRecognitionRequestViewObj.loadEmployeeMasterData(field,  employeeDetailsList);
			
	}
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(PAHealthFinancialWizardPresenter.VALIDATE_FINANCIAL_USER_RRC_REQUEST, bean);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
			if (!isValid) {
				Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
			} 
		else
		{
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("85%");
			popup.setHeight("100%");
			rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
			//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
			//documentDetails.setClaimDto(bean.getClaimDTO());
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PA_FINANCIAL_HOSP);
			rewardRecognitionRequestViewObj.init(bean, popup);
			
			//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
			popup.setCaption("Reward Recognition Request");
			popup.setContent(rewardRecognitionRequestViewObj);
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
		}
	
	 public void compareWithUserId(String userId) {
		 
		 if(userId == null){
			 userId = "";
		 }

		 final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_FINANCIALS,null);
					dialog.close();
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>Intimation is already opened by another user</b>"+userId, ContentMode.HTML), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);
	
 }
	 
	 public void confirmMessageForHospitalDiscount(){
			
			ConfirmDialog dialog = ConfirmDialog
					.show(getUI(),
							"Hospital  is eligible for Discount !!!",
							"Do you want to proceed without Discount ? ",
							"No", "Yes", new ConfirmDialog.Listener() {

								public void onClose(ConfirmDialog dialog) {
									
									if (!dialog.isConfirmed()) {
										// Confirmed to continue
										fireViewEvent(PAHealthFinancialWizardPresenter.SUBMIT_CLAIM_FINANCIAL, bean);
									} else {
										wizard.getFinishButton().setEnabled(true);
									}
								}
							});
			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);
			
		}

	 @Override
		public void buildFailureLayout() {
		
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			Label successLabel = new Label("<b style = 'color: green;'>Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Bill Entry Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
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
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.ENTER_BILL_DETAILS, null);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					SHAUtils.clearSessionObject(currentRequest);
				}
			});
			
		}

	@Override
	public void alertForAlreadyAcquired(String aquiredUser) {

		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim is already opened by "+aquiredUser +"</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Financial Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_FINANCIALS, null);

			}
		});
		
	
		
	}

	@Override
	public void buildAlreadyExist() {


		Label successLabel = new Label("<b style = 'color: red;'>Another ROD exists/settled  for same classification.!!!</b>", ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Financial Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_FINANCIALS, null);

			}
		});
		
	
		
	
		
	}

	@Override
	public void buildPaymentFailureLayout() {


		Label successLabel = new Label("<b style = 'color: red;'>Payment initiated/made for this ROD Already. Please contact IMS Support.", ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Financial Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_FINANCIALS, null);

			}
		});
	}
	
	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 @Override
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
	 }
	 
	 public  void billingInternalRemarksChangeListener(TextArea textArea, final  Listener listener) {
		    @SuppressWarnings("unused")
			ShortcutListener enterShortCut = new ShortcutListener("ShortcutForBillingInternalRemarks", ShortcutAction.KeyCode.F8, null) {
		    	private static final long serialVersionUID = -2267576464623389044L;
		    	@Override
		    	public void handleAction(Object sender, Object target) {
		    		((ShortcutListener) listener).handleAction(sender, target);
		    	}
		    };	  
		    handleShortcut(textArea, getBillingInternalRemarksShortCutListener(textArea));
	}
		
	public  void handleShortcut(final TextArea textArea, final ShortcutListener shortcutListener) {	
		textArea.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void focus(FocusEvent event) {				
				textArea.addShortcutListener(shortcutListener);
			}
		});
		textArea.addBlurListener(new BlurListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void blur(BlurEvent event) {			
				textArea.removeShortcutListener(shortcutListener);		
			}
		});
	}
	
	private ShortcutListener getBillingInternalRemarksShortCutListener(final TextArea textAreaField) {
		ShortcutListener listener =  new ShortcutListener("ShortcutForBillingInternalRemarks", KeyCodes.KEY_F8,null) {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				txtArea.setValue(textAreaField.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				txtArea.setReadOnly(true);
				
				final Window dialog = new Window();
				dialog.setHeight("75%");
		    	dialog.setWidth("65%");
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						textAreaField.setValue(((TextArea) event.getProperty()).getValue());						
//							PreauthDTO mainDto = (PreauthDTO) textAreaField.getData();
//							mainDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(textAreaField.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				dialog.setCaption("Billing Internal Remarks");
				dialog.setClosable(true);
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(textAreaField);
				
				dialog.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};
		return listener;
	}
	
	@Override
	public void validationForLimit() {
	
		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		/*Label successLabel = new Label("<b style = 'color: red;'>Amount exceeds your limit and you cannot proceed further.</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
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
		dialog.show(getUI().getCurrent(), null, true);
		*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("Amount exceeds your limit and you cannot proceed further.</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				releaseHumanTask();
				/*if(bean.getScreenName() != null
						&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA_AUTO_ALLOCATION.equalsIgnoreCase(bean.getScreenName())){
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS_AUTO_ALLOCATION,
							null);
				}
				else if(bean.getScreenName() != null
						&& SHAConstants.PROCESS_CLAIM_COMMON_BILLING_AND_FA.equalsIgnoreCase(bean.getScreenName())){
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_COMMON_BILLING_FINANCIALS,
							null);
				}
				else {*/
					fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_FINANCIALS, null);
				//}	
				clearObject();
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
	}
	
	private void clearObject() {
		if(rewardRecognitionRequestViewObj != null){
				rewardRecognitionRequestViewObj.invalidate();
		}
	 }
}
