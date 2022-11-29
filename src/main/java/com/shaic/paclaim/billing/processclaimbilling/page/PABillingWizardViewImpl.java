package com.shaic.paclaim.billing.processclaimbilling.page;

import java.util.ArrayList;
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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.balancesuminsured.view.ViewUnnamedRiskDetailsUI;
import com.shaic.claim.reimbursement.billing.pages.billingprocess.BillingProcessPageViewImpl;
import com.shaic.claim.reimbursement.billing.pages.billsummary.BillingSummaryPageViewImpl;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.IrdaBillDetailsPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewMedicalSummaryPage;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.PARevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.shaic.paclaim.billing.processclaimbilling.page.billassesmentsheet.PABillingAssesmentSheetViewImpl;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingReviewPageViewImpl;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAReconsiderRODRequestListenerTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.zybnet.autocomplete.server.AutocompleteField;

public class PABillingWizardViewImpl extends AbstractMVPView implements PABillingWizard {

	private static final long serialVersionUID = -1756934701433733987L;
	
	@Inject
	private Instance<IWizard> iWizard;
	
	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private PreauthDTO bean;
	
	private VerticalSplitPanel mainPanel;
	
	/*@Inject
	private ViewDetails viewDetails;*/
	
	//private static Window popup;
	
	/*@Inject
	private ReconsiderRODRequestTable reconsiderRequestDetails;
	*/
	private VerticalLayout reconsiderationLayout;
	
	@Inject
	private Instance<PARevisedCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	@Inject
	private Instance<PABillingReviewPageViewImpl> billingReviewPageViewImpl;
	
	@Inject
	private Instance<BillingSummaryPageViewImpl> billingSummaryPageViewImpl;
	
	@Inject
	private Instance<BillingProcessPageViewImpl> billingProcessPageViewImpl;
	
	@Inject
	private Instance<PABillingAssesmentSheetViewImpl> billingassesmentSheetViewImpl;
	
	private PABillingReviewPageViewImpl billingReviewPageViewImplObj;
	
	private BillingSummaryPageViewImpl billingSummaryPageViewImplObj;
	
	private BillingProcessPageViewImpl billingProcessPageViewImplObj;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	private PABillingAssesmentSheetViewImpl billingAssesmentViewImplObj;
	
	@Inject
	private ViewMedicalSummaryPage viewMedicalSummaryPage;
	
	@Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	@Inject
	private IrdaBillDetailsPage viewIRDABillSummaryPage;

	//private ComboBox cmbReconsiderationRequest;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private PAReconsiderRODRequestListenerTable reconsiderRequestDetails;
	
	private ComboBox cmbReasonForReconsideration;
	 
	private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	
	private ComboBox cmbReconsiderationRequest;
	
	private BeanItemContainer<SelectValue> reconsiderationRequest;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Instance<ViewUnnamedRiskDetailsUI> unNamedRiskDetailsInstance;
	
	private ViewUnnamedRiskDetailsUI unNamedRiskDetailsObj;
	
	@Inject
	private Toolbar toolBar;
	
//	@Inject
//	private PABillingReviewPageUI dataExtractionPage;
	
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
		initBinder();
		billingReviewPageViewImplObj = billingReviewPageViewImpl.get();
		billingReviewPageViewImplObj.init(this.bean , wizard);
		wizard.addStep(billingReviewPageViewImplObj,"Bill Review");
		
		billingAssesmentViewImplObj = billingassesmentSheetViewImpl.get();
		billingAssesmentViewImplObj.init(this.bean, wizard);
		wizard.addStep(billingAssesmentViewImplObj,"Bill Assesment Sheet");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		
		PARevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "Process Claim Billing");
		intimationDetailsCarousel.init(this.bean,  "Process Claim Billing - Non Hospitalisation");
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());
		wizardLayout1.setSpacing(true);
		
		if(bean.getIsReferToBilling()) {
			TextField field1 = new TextField("Reason for Referring to Billing");
			if(bean.getPreauthDataExtractionDetails().getFaReasonForRefferingToBilling()!=null){
				field1.setValue(bean.getPreauthDataExtractionDetails().getFaReasonForRefferingToBilling());
			}
			field1.setReadOnly(true);
			field1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			TextField field2 = new TextField("Financial Approver Remarks");
			if(bean.getPreauthDataExtractionDetails().getFaApproverRemarks()!=null){
				field2.setValue(bean.getPreauthDataExtractionDetails().getFaApproverRemarks());
			}
			field2.setReadOnly(true);
			field2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			if(bean.getPreauthDataExtractionDetails().getFaApproverRemarks()!=null){
				field2.setDescription(bean.getPreauthDataExtractionDetails().getFaApproverRemarks());
			}
			FormLayout fLayout = new FormLayout(field1, field2);
			fLayout.setSpacing(true);
			VerticalLayout layout = new VerticalLayout(fLayout);
			layout.setCaption("Financial Approval Details");
			wizardLayout1.addComponent(layout);
		}
		
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
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
				compareWithUserId(aciquireByUserId);
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
	
	private VerticalLayout commonButtonsLayout() {
		TextField typeFld = new TextField("Type");
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setNullRepresentation("");
		
		if(this.bean.getIsCashlessType() && this.bean.getHospitalizaionFlag()&& this.bean.getStatusKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)){
			typeFld.setValue(SHAConstants.DIRECT_TO_BILLING);
		}
		typeFld.setReadOnly(true);
		
		TextField hospitalNtwrktype = new TextField("Hospital Network Type");
		hospitalNtwrktype.setNullRepresentation("");
		hospitalNtwrktype.setValue(this.bean.getNetworkHospitalType());
		hospitalNtwrktype.setReadOnly(true);
//		hospitalNtwrktype.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		FormLayout hLayout = new FormLayout (typeFld,hospitalNtwrktype);
		hLayout.setComponentAlignment(typeFld, Alignment.MIDDLE_LEFT);
		
		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
				ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
				documentDetails.setClaimDto(bean.getClaimDTO());
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),null);
				showPopup(new VerticalLayout(earlierRodDetailsViewObj));
			}
			
		});
		
		
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				validateUserForRRCRequestIntiation();
				
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
		
		/*Button viewMedicalSummaryButton = new Button("View Medical Summary");
		viewMedicalSummaryButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				viewMedicalSummaryPage.init(bean.getKey());
				showPopup(new VerticalLayout(viewMedicalSummaryPage));
			}
			
		});*/
		
//		Button viewBillSummaryButton = new Button("View Bill Summary");
//		viewBillSummaryButton.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
//				popup = new com.vaadin.ui.Window();
//				popup.setCaption("");
//				/*popup.setWidth("75%");
//				popup.setHeight("85%");*/
//				popup.setSizeFull();
//				viewBillSummaryPage.init(bean,bean.getKey(),true);
//				Panel mainPanel = new Panel(viewBillSummaryPage);
//		        mainPanel.setWidth("2000px");
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
//					}
//				});
//
//				popup.setModal(true);
//				UI.getCurrent().addWindow(popup);
//				
//			}
//			
//		});
//		
//		Button viewIRDASummaryButton = new Button("View IRDA Bill Summary");
//		viewIRDASummaryButton.addClickListener(new ClickListener() {
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
//				viewIRDABillSummaryPage.init(bean.getKey());
//				showPopup(new VerticalLayout(viewIRDABillSummaryPage));
//			}
//			
//		});
		
		//VerticalLayout viewEarlierRODLayout = new VerticalLayout(btnRRC, viewMedicalSummaryButton, viewEarlierRODDetails, viewBillSummaryButton,viewIRDASummaryButton );
		//viewEarlierRODLayout.setComponentAlignment(btnRRC, Alignment.MIDDLE_RIGHT);
	//	viewEarlierRODLayout.setSpacing(true);
		
		Button viewUnnamedDetails = new Button("Unnamed Risk Details");
		viewUnnamedDetails.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				unNamedRiskDetailsObj = unNamedRiskDetailsInstance.get();				
				unNamedRiskDetailsObj.init(bean.getClaimDTO().getNewIntimationDto().getKey());
				popup.setContent(unNamedRiskDetailsObj);
				popup.setCaption("Unnamed Risk Details");
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
		
		HorizontalLayout icrAgentBranch = SHAUtils.icrAgentBranchForPA(bean);
		
		HorizontalLayout unNamedRiskDetailsLayout = new HorizontalLayout(viewUnnamedDetails);
		
		
		
		HorizontalLayout layout = new HorizontalLayout(icrAgentBranch,btnRRC,/*viewMedicalSummaryButton,*/ viewEarlierRODDetails,btnViewPolicySchedule);
		
		
		if(null != bean.getClaimDTO() && null != bean.getClaimDTO().getNewIntimationDto() && null != bean.getClaimDTO().getNewIntimationDto().getPolicy()&&
				null !=  bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() && null !=  bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGPAProducts().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())) &&
				(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getGpaPolicyType()))){
			 //IMSSUPPOR-30899
             layout = new HorizontalLayout(btnRRC,/*viewMedicalSummaryButton,*/ viewEarlierRODDetails,unNamedRiskDetailsLayout,btnViewPolicySchedule);

		}
		//layout.setComponentAlignment(viewEarlierRODLayout, Alignment.MIDDLE_RIGHT);
		layout.setSpacing(true);
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		FormLayout clubMembershipLayout = new FormLayout(cmdClubMembership);
		clubMembershipLayout.setMargin(false);
		
		if (null != memberType && !memberType.isEmpty()) {
			layout.addComponent(clubMembershipLayout);
		}
		
//		HorizontalLayout Layout1 = new HorizontalLayout(viewBillSummaryButton,viewIRDASummaryButton);
//		Layout1.setSpacing(true);
		VerticalLayout vLayout = new VerticalLayout(layout);
				vLayout.setSpacing(true);
		HorizontalLayout viewDetailsForm = new HorizontalLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),bean.getKey(), ViewLevels.PA_PROCESS,"Process Claim Billing");
		viewDetailsForm.addComponent(viewDetails);
	//	Label dummy = new Label();
	//	dummy.setWidth("300px");
		
		HorizontalLayout componentsHLayout = new HorizontalLayout(/*hLayout*/vLayout, viewDetailsForm);
//		HorizontalLayout horizontalLayout = new HorizontalLayout(viewEarlierRODLayout);
		componentsHLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		
		HorizontalLayout formLayout = null;
		if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) {
			Panel insuredPedDetailsPanel = getInsuredPedDetailsPanel();
			formLayout = new HorizontalLayout(hLayout,new FormLayout(commonValues()),insuredPedDetailsPanel);
		}else{
			formLayout = new HorizontalLayout(hLayout,new FormLayout(commonValues()));
		}
		
		formLayout.setSpacing(false);
		formLayout.setMargin(false);
		VerticalLayout verticalLayout = new VerticalLayout(componentsHLayout,formLayout);
		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
		componentsHLayout.setWidth("100%");
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
		table.setColumnWidth("pedCode", 70);
		table.setColumnWidth("pedDescription", 250);
		
		Panel tablePanel = new Panel(table);
		return tablePanel;
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
		//vLayout.setMargin(true);
		vLayout.setCaption("Earlier Request Reconsidered");
		
		FormLayout fLayout = new FormLayout(cmbReconsiderationRequest);
		fLayout.setSpacing(false);
		fLayout.setMargin(false);
		FormLayout fLayout1 = new FormLayout(cmbReasonForReconsideration);
		
		if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && ("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()))
		{
			reconsiderationLayout = new VerticalLayout(fLayout,fLayout1, vLayout);
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
		
		
		getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		fireViewEvent(PABillingWizardPresenter.SUBMIT_CLAIM_BILLING, this.bean);
		
		/*if(this.bean.getIsHospitalDiscountApplicable() && 
				this.bean.getHospitalizationCalculationDTO().getHospitalDiscount() != null && this.bean.getHospitalizationCalculationDTO().getHospitalDiscount() == 0){
			confirmMessageForHospitalDiscount();
		}else{
			
		}*/
		
		
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
									fireViewEvent(MenuItemBean.PA_PROCESS_CLAIM_BILLING,
											null);
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
	
	public void confirmMessageForHospitalDiscount(){
		
		ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Hospital  is eligible for Discount !!!",
						"Do you want to proceed without Discount ? ",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									getSession().setAttribute(SHAConstants.TOKEN_ID, null);
									fireViewEvent(PABillingWizardPresenter.SUBMIT_CLAIM_BILLING, bean);
								} else {
									wizard.getFinishButton().setEnabled(true);
								}
							}
						});
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim record saved successfully !!!.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Billing Home");
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
				fireViewEvent(MenuItemBean.PA_PROCESS_CLAIM_BILLING, null);

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
		fireViewEvent(PABillingWizardPresenter.VALIDATE_BILLING_USER_RRC_REQUEST, bean);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PA_BILLING);
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
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_BILLING,null);
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
					fireViewEvent(MenuItemBean.PA_PROCESS_CLAIM_BILLING, null);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
		}

	@Override
	public void validateOPtionalCovers(Long coverId, TextField eligiblityFld) {
		billingReviewPageViewImplObj.validateOptionalCovers(coverId,eligiblityFld);
		
	}
	
	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 @Override
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
	 }
}
