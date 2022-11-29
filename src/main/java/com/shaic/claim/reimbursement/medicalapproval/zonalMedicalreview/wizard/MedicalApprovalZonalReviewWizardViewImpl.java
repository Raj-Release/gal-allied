package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fvrdetails.view.FVRTriggerPtsTable;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.preauth.wizard.view.ParallelInvestigationDetails;
import com.shaic.claim.premedical.listenerTables.UpdateOtherClaimDetailsUI;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPageViewImpl;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingViewImpl;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.previousclaims.MedicalApprovalPreviousClaimsPageViewImpl;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.tables.ReconsiderRODRequestListenerTable;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.EsclateClaimToRawPage;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableBancs;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.RevisedPreauthViewPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPaayasPolicyDetailsPdfPage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ZUATopViewQueryTable;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;

public class MedicalApprovalZonalReviewWizardViewImpl extends AbstractMVPView implements
MedicalApprovalZonalReviewWizard {
	private static final long serialVersionUID = -6326484157414527897L;
	
	
	public static final String MEDICAL_APPROVAL = "Medical approval reset view";
	
	/*@Inject
	private Instance<IWizard> iWizard;*/
	
	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private PreauthDTO bean;
	
	private VerticalSplitPanel mainPanel;
	
	
	
	@Inject
	private RevisedPreauthViewPage viewPreauth;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
//	private HorizontalLayout reconsiderationLayout;
	
	private VerticalLayout reconsiderationLayout;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	@Inject
	private Instance<MedicalApprovalDataExtractionPageViewImpl> dataExtractionViewImpl;
	
	@Inject
	private Instance<MedicalApprovalPreviousClaimsPageViewImpl> previousClaimViewImpl;
	
	@Inject
	private Instance<MedicalApprovalPremedicalProcessingViewImpl> premedicalprocessingViewImpl;
	
	private MedicalApprovalDataExtractionPageViewImpl dataExtractionViewImplObj;
	
	private MedicalApprovalPremedicalProcessingViewImpl premedicalprocessingViewImplObj;
	
	private MedicalApprovalPreviousClaimsPageViewImpl previousClaimViewImplObj;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private IntimationService intimationService;

	@Inject
	private ViewPaayasPolicyDetailsPdfPage pdfPageUI;


	//private VerticalLayout dummyLayout;
	

	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private ReconsiderRODRequestListenerTable reconsiderRequestDetails;
	
	private ComboBox cmbReasonForReconsideration;
	 
	//private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	
	private ComboBox cmbReconsiderationRequest;
	
	//private BeanItemContainer<SelectValue> reconsiderationRequest;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	
	private HorizontalLayout vLayout;
	
	public Button btnViewRTABalanceSI;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private UpdateOtherClaimDetailsUI updateOtherClaimDetailsUI;
	
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;
	 
	@EJB
	private PolicyService policyService;
	 
	@Inject
	private ZUATopViewQueryTable zuaTopViewQueryTable;
	
	@Inject
	private ParallelInvestigationDetails viewInvestigationDetails;
	
	private final Logger log = LoggerFactory.getLogger(MedicalApprovalZonalReviewWizardViewImpl.class);
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;

	private ComboBox allocationTo;
	private ComboBox fvrPriority;
	private TextArea fvrTriggerPoints;
	private Label countFvr;
	private Button viewFVRDetails;
	private TextArea triggerPointsToFocus;
	private TextArea reasonForReferringIV;
	private Button submitButton;
	public Button cancelButton;
	
	@Inject
	private Instance<FVRTriggerPtsTable> triggerPtsTable;
	
	private FVRTriggerPtsTable triggerPtsTableObj;
	
	private ArrayList<Component> mandatoryFields;
	
	private List<String> errorMessages;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private FieldVisitRequestService fvrService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@Inject
	private Instance<EsclateClaimToRawPage> esclateClaimToRawPageInstance;
	
	private EsclateClaimToRawPage esclateClaimToRawPageViewObj;
	
	@Inject
	private Toolbar toolBar;
	
	@Inject
	private ZUAViewQueryHistoryTableBancs zuaTopViewQueryTableBancs;
	
	
	public MedicalApprovalDataExtractionPageViewImpl getDataExtractionViewImplObj() {
		return dataExtractionViewImplObj;
	}

	public void setDataExtractionViewImplObj(
			MedicalApprovalDataExtractionPageViewImpl dataExtractionViewImplObj) {
		this.dataExtractionViewImplObj = dataExtractionViewImplObj;
	}

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
		
		item.addNestedProperty("preauthMedicalDecisionDetails.allocationTo");
		item.addNestedProperty("preauthMedicalDecisionDetails.assignTo");
		item.addNestedProperty("preauthMedicalDecisionDetails.priority");
		this.binder.setItemDataSource(item);	
	}
	
	@PostConstruct
	public void initView() {

		addStyleName("view");
		setSizeFull();			
	}
	
	public void initView(PreauthDTO bean)
	{
		mandatoryFields = new ArrayList<Component>();
		errorMessages = new ArrayList<String>();
		this.bean = bean;
		mainPanel = new VerticalSplitPanel();
		this.wizard = new IWizardPartialComplete();
		initBinder();
		
		dataExtractionViewImplObj = dataExtractionViewImpl.get();
		dataExtractionViewImplObj.init(this.bean , wizard);
		wizard.addStep(dataExtractionViewImplObj, "Data Extraction");
		
		previousClaimViewImplObj = previousClaimViewImpl.get();
		previousClaimViewImplObj.init(this.bean);
		wizard.addStep(previousClaimViewImplObj,"previous Claim Zonal Review");
		
		premedicalprocessingViewImplObj = premedicalprocessingViewImpl.get();
		premedicalprocessingViewImplObj.init(this.bean);
		wizard.addStep(premedicalprocessingViewImplObj,"premedical process zonal Review");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		TextField typeFld = new TextField("Type");
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setReadOnly(true);
		VerticalLayout commonvalues = commonValues();
		FormLayout hLayout = new FormLayout (typeFld);
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		if (null != memberType && !memberType.isEmpty()) {
			hLayout.addComponent(cmdClubMembership);
		}
		
		if((this.bean.getInsuredPedDetails() != null && ! this.bean.getInsuredPedDetails().isEmpty()) || (this.bean.getApprovedPedDetails() != null && !this.bean.getApprovedPedDetails().isEmpty())){
			
			Panel insuredPedDetailsPanel = getInsuredPedDetailsPanel();
			
//			HorizontalLayout crmFlaggedLayout = SHAUtils.crmFlaggedLayout(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());	
			crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
			
			vLayout= new HorizontalLayout(hLayout,commonvalues);
			
			vLayout.setSpacing(true);
			vLayout.setComponentAlignment(commonvalues, Alignment.TOP_LEFT);
			if(insuredPedDetailsPanel != null){
				vLayout.addComponent(insuredPedDetailsPanel);
				vLayout.setComponentAlignment(insuredPedDetailsPanel, Alignment.MIDDLE_RIGHT);
			}
			vLayout.addComponent(crmFlaggedComponents);
			
		}else{
			
//			HorizontalLayout crmFlaggedLayout = SHAUtils.crmFlaggedLayout(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());	
			crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
			
			vLayout= new HorizontalLayout(hLayout,commonvalues);
			vLayout.addComponent(crmFlaggedComponents);
			vLayout.setSpacing(true);
			vLayout.setComponentAlignment(commonvalues, Alignment.TOP_LEFT);
		}
		
//		hLayout.setComponentAlignment(typeFld, Alignment.MIDDLE_LEFT);
//		hLayout.setComponentAlignment(commonvalues, Alignment.BOTTOM_LEFT);
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "Process Claim Request (Zonal Medical Review)");
		intimationDetailsCarousel.init(this.bean,  "Process Claim Request (Zonal Medical Review)");
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(),bean.getKey(), ViewLevels.PREAUTH_MEDICAL,"Process Claim Request (Zonal Medical Review)");
		HorizontalLayout hLayout1 = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		hLayout1.setWidth("100%");
		hLayout1.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		Panel panel1 = new Panel();
		panel1.setContent(hLayout1);
		//panel1.setHeight("100px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
		if(bean.getTaskNumber() != null){/*
			String aciquireByUserId = SHAUtils.getAciquireByUserId(bean.getTaskNumber());
			if(bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)){
				compareWithUserId(aciquireByUserId);
			}
		*/}
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
		if(null  != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId())
		{
			String reasonForReconsideration = this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getValue();
			selValue.setId(1l);
			selValue.setValue(reasonForReconsideration);
		}
		reconsiderValues.add(selValue);
		
		BeanItemContainer<SelectValue> reconsiderBeanContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		reconsiderBeanContainer.addAll(reconsiderValues);
		
//		reconsiderBeanContainer = masterService.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION);
		
		cmbReasonForReconsideration.setContainerDataSource(reconsiderBeanContainer);
		cmbReasonForReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonForReconsideration.setItemCaptionPropertyId("value");
		cmbReasonForReconsideration.setReadOnly(false);

		String reasonForReconsideration = "";
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
//		vLayout.setCaption("Earlier Request Reconsidered");		
		
		reconsiderationLayout = new VerticalLayout();
		if(("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()))
		{
			HorizontalLayout reconLayout = new HorizontalLayout(new FormLayout(cmbReconsiderationRequest),new FormLayout(cmbReasonForReconsideration));
			reconsiderationLayout.addComponents(reconLayout,vLayout);
		}
		else
		{
			reconsiderationLayout.addComponents(new FormLayout(cmbReconsiderationRequest));
		}
		
		return reconsiderationLayout;
	}
	
	private Panel getInsuredPedDetailsPanel(){
		
		
		Table table = new Table();
		table.setWidth("80%");
		table.setHeight("116px");
		table.addContainerProperty("pedCode", String.class, null);
		table.addContainerProperty("pedDescription",  String.class, null);
		table.addContainerProperty("pedEffectiveFromDate",  Timestamp.class, null);
		table.setCaption("PED Details");
		 
		int i=0;
//		table.setStyleName(ValoTheme.TABLE_NO_HEADER);
		if(this.bean.getInsuredPedDetails() != null && !this.bean.getInsuredPedDetails().isEmpty()) {
			for (InsuredPedDetails pedDetails : this.bean.getInsuredPedDetails()) {
				if(pedDetails.getPedDescription() != null /*&& !("NIL").equalsIgnoreCase(pedDetails.getPedDescription())*/){
					table.addItem(new Object[]{pedDetails.getPedCode(), pedDetails.getPedDescription(),pedDetails.getPedEffectiveFromDate()}, i+1);
					i++;
				}
			}
		}
		
		if(this.bean.getApprovedPedDetails() != null && !this.bean.getApprovedPedDetails().isEmpty()){
			for (PreExistingDisease component : this.bean.getApprovedPedDetails()) {
				table.addItem(new Object[]{component.getCode(), component.getValue()}, i+1);
				i++;
			}
		}
				
		table.setPageLength(2);
		table.setColumnHeader("pedCode", "PED Code");
		table.setColumnHeader("pedDescription", "Description");
		table.setColumnHeader("pedEffectiveFromDate", "PED Effective from date");
		table.setColumnWidth("pedCode", 70);
		table.setColumnWidth("pedDescription", 250);
		table.setColumnWidth("pedEffectiveFromDate", 200);
		
//		if(i>0){
			Panel tablePanel = new Panel(table);
			return tablePanel;
//		}
//		return null;
	}
	
	private HorizontalLayout commonButtonsLayout()
	{
		TextField acknowledgementNumber = new TextField("Acknowledgement Number");
//		acknowledgementNumber.setValue(String.valueOf(this.bean.getAcknowledgementNumber()));
		//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
		acknowledgementNumber.setWidth("250px");
		acknowledgementNumber.setHeight("20px");
		acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		acknowledgementNumber.setReadOnly(true);
		acknowledgementNumber.setEnabled(false);
		acknowledgementNumber.setNullRepresentation("");
//		FormLayout hLayout = new FormLayout (acknowledgementNumber);
//		hLayout.setComponentAlignment(acknowledgementNumber, Alignment.MIDDLE_LEFT);
		
		Button msgButton = new Button("Message Button");
		msgButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
					
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Zonal Review Home");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createInformationBox(" Claim record saved successfully !!!!", buttonsNamewithType);
					Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
							.toString());
					homeButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							//dialog.close();
							fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_ZONAL_REVIEW, null);
						}
					});
				
			}
		});
		
		TextField txtClaimCount = new TextField("Claim Count");
		txtClaimCount.setValue(this.bean.getClaimCount().toString());
		txtClaimCount.setReadOnly(true);
		TextField dummyField = new TextField();
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		FormLayout firstForm = new FormLayout(txtClaimCount,dummyField);
		dummyField.setReadOnly(true);
//		firstForm.setWidth(txtClaimCount.getWidth(), txtClaimCount.getWidthUnits());
		Panel claimCount = new Panel(firstForm);
		claimCount.setWidth("130px");
		txtClaimCount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.setHeight("30px");
		firstForm.setMargin(false);
		firstForm.setComponentAlignment(txtClaimCount, Alignment.TOP_LEFT);
//		txtClaimCount.addStyleName("fail");
//		claimCount.setWidth(txtClaimCount.getWidth(),txtClaimCount.getWidthUnits());
//		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			claimCount.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			claimCount.addStyleName("girdBorder2");
		}
		
		HorizontalLayout formLayout = SHAUtils.newImageCRM(bean);
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean);
		HorizontalLayout crmLayout = new HorizontalLayout(claimCount,formLayout,hopitalFlag);
		crmLayout.setSpacing(true);
		crmLayout.setWidth("100%");
		HorizontalLayout icrAgentBranch = SHAUtils.icrAgentBranch(bean);
		HorizontalLayout buyBackPedHLayout = new HorizontalLayout();
		if(bean.getNewIntimationDTO() !=null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_78)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean);
			icrAgentBranch.addComponent(buyBackPedHLayout);
		}
		if(bean.getNewIntimationDTO() !=null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null
				&& bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_88)){
			buyBackPedHLayout = SHAUtils.buyBackPed(bean);
			icrAgentBranch.addComponent(buyBackPedHLayout);
		}
		VerticalLayout icrAGBR = new VerticalLayout(crmLayout,icrAgentBranch);
		//Added for testing RRC.
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		Button btnLumen = new Button("Initiate Lumen");
		btnLumen.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				invokePreMedicalLumenRequest();
			}
		});
		
		
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
				earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
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
		
		Button btnViewPackage = new Button("View Package");
		btnViewPackage.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(null != bean.getNewIntimationDTO().getIsPaayasPolicy() && bean.getNewIntimationDTO().getIsPaayasPolicy()){
					pdfPageUI.init(bean.getNewIntimationDTO());
					if(pdfPageUI.isAttached()){
						pdfPageUI.detach();
					}
					UI.getCurrent().addWindow(pdfPageUI);
				}
				else if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto() != null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null){
//				HospitalPackageRatesDto packageRatesDto = hospitalService.getHospitalPackageRates(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode());
				
				
				if(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode() != null){
					BPMClientContext bpmClientContext = new BPMClientContext();
					String url = bpmClientContext.getHospitalPackageDetails() + bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHospitalCode();
					//getUI().getPage().open(url, "_blank");
					getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
				}

//					if(packageRatesDto != null){
//						
//					ReportDto reportDto = new ReportDto();
//					reportDto.setClaimId(bean.getNewIntimationDTO().getIntimationId());
//					List<HospitalPackageRatesDto> beanList = new ArrayList<HospitalPackageRatesDto>();
//					beanList.add(packageRatesDto);
//					reportDto.setBeanList(beanList);
//					DocumentGenerator docGen = new DocumentGenerator();
//					String fileUrl = docGen.generatePdfDocument("HospitalPackageRates", reportDto);
//					openPdfFileInWindow(fileUrl);
//					}
//					else{
//						getErrorMessage("Package Not Available for the selected Hospital");
//					}
				}
				else{
					getErrorMessage("Package Not Available for the selected Hospital");
				}
			}
			
		});
		
		Button viewPreauthButton = new Button("View Pre-auth");
		viewPreauthButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(bean.getClaimDTO() != null && bean.getClaimDTO().getClaimType() != null
						&& bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
//				viewPreauth.init(bean.getNewIntimationDTO().getIntimationId());
				viewPreauth.init(bean.getNewIntimationDTO().getIntimationId());
				popup.setContent(viewPreauth);
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
				}else{
					getErrorMessage("Preauth is not available");
				}
				
				
			}
			
		});
		
		Button btnViewDoctorRemarks = new Button("Doctor Remarks");
		btnViewDoctorRemarks.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getDoctorRemarks(bean.getNewIntimationDTO().getIntimationId());
			}
			
		});
		
		Button btnViewPolicySchedule = new Button("View Policy Schedule");
		btnViewPolicySchedule.setStyleName(ValoTheme.BUTTON_DANGER);
		
		if(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
			btnViewPolicySchedule.setEnabled(false);
		}
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
		
		btnViewRTABalanceSI = new Button("View RTA Sum Insured");
		//btnViewRTABalanceSI.setStyleName(ValoTheme.BUTTON_DANGER);
		//btnViewRTABalanceSI.setEnabled(false);
		btnViewRTABalanceSI.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Long policyKey = bean.getNewIntimationDTO().getPolicy().getKey();
				Long insuredKey = bean.getNewIntimationDTO().getInsuredPatient().getKey();
				Long claimKey = bean.getClaimDTO().getKey();
				viewDetails.getViewRTAsumInsured(policyKey, insuredKey, claimKey);;
			}
		});
		
		Button btnviewCashlessDocument = new Button("View Cashless Document");
		btnviewCashlessDocument.setStyleName(ValoTheme.BUTTON_DANGER);
		btnviewCashlessDocument.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			
			/**
			 * Release Number : R3
			 * Requirement Number:R0725
			 * Modified By : Durga Rao
			 * Modified On : 15th May 2017
			 */
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				viewDetails.getViewCashlessDocument(bean.getNewIntimationDTO().getIntimationId());
				bean.setIsViewCashlessDocClicked(true);
				Button button = (Button)event.getSource();
				button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			}
		});
		Button updatePreviousButton = new Button("Update Previous/Other Claim Details(Defined Limit)");
		
		updatePreviousButton.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.UPDATE_PREVIOUS_CLAIM_DETAILS, bean);
			}
		});
		
		Button btnZUAAlert = new Button("View ZUA History");
		btnZUAAlert.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				List<ZUAViewQueryHistoryTableDTO> setViewTopZUAQueryHistoryTableValues = SHAUtils.setViewTopZUAQueryHistoryTableValues(
						bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),policyService);
				

				List<ZUAViewQueryHistoryTableDTO> setViewZUAQueryHistoryTableValues = SHAUtils.setViewZUAQueryHistoryTableValues(
						bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),policyService);
				
				
				if(null != setViewZUAQueryHistoryTableValues && !setViewZUAQueryHistoryTableValues.isEmpty())
				{
					
				Policy policyObj = null;
				VerticalLayout verticalLayout = null;
				if (bean.getNewIntimationDTO().getPolicy().getPolicyNumber() != null) {
					policyObj = policyService.getByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
					if (policyObj != null) {
						if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
							zuaTopViewQueryTableBancs.init(" ",false,false);
							zuaTopViewQueryTableBancs.setTableList(setViewZUAQueryHistoryTableValues);//pending
							VerticalLayout verticalZUALayout = new VerticalLayout();
							
							verticalZUALayout.addComponent(zuaTopViewQueryTableBancs);
							
							verticalLayout = new VerticalLayout();
					    	verticalLayout.addComponents(verticalZUALayout);
						}
						else{
							zuaTopViewQueryTable.init(" ",false,false);
							zuaTopViewQueryTable.initTable();						
							
							zuaViewQueryHistoryTable.init(" ", false, false); 
							
							
							zuaViewQueryHistoryTable.setTableList(setViewZUAQueryHistoryTableValues);
							zuaTopViewQueryTable.setTableList(setViewTopZUAQueryHistoryTableValues);
							VerticalLayout verticalTopZUALayout = new VerticalLayout();
							VerticalLayout verticalZUALayout = new VerticalLayout();
							
							verticalTopZUALayout.addComponent(zuaTopViewQueryTable);
							verticalZUALayout.addComponent(zuaViewQueryHistoryTable);
							
							verticalLayout = new VerticalLayout();
					    	verticalLayout.addComponents(verticalTopZUALayout,verticalZUALayout);
					    	verticalLayout.setComponentAlignment(verticalTopZUALayout,Alignment.TOP_CENTER );  
						}
					}
				}
			
				
				Window popup = new com.vaadin.ui.Window();
				popup.setWidth("70%");
				popup.setHeight("70%");
				popup.setContent(verticalLayout);
				popup.setCaption("ZUA History");
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.setDraggable(true);
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
				else
				{
					getErrorMessage("ZUA History is not available");
				}
			}
		});
		
		Button investigationBtn = new Button("Initiate Investigation");
		investigationBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.ZONAL_REVIEW_INITIATE_INV_EVENT, bean);
			}
			
		});
		
		Button initiateFVRBtn = new Button("Initiate FVR");
		if(null != bean.getNewIntimationDTO().getIsTataPolicy() && bean.getNewIntimationDTO().getIsTataPolicy()){
			initiateFVRBtn.setEnabled(false);
		}
		initiateFVRBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				bean.getPreauthMedicalDecisionDetails().setIsAllowInitiateFVR(
						preauthService.getFVRStatusByRodKey(bean.getKey()));

				if (bean.getPreauthMedicalDecisionDetails() != null
						&& bean.getPreauthMedicalDecisionDetails()
								.getIsAllowInitiateFVR() != null
						&& !bean.getPreauthMedicalDecisionDetails()
								.getIsAllowInitiateFVR()
						&& bean.getPreauthMedicalDecisionDetails() != null
						&& bean.getPreauthMedicalDecisionDetails()
								.getIsFvrIntiated() != null
						&& bean.getPreauthMedicalDecisionDetails()
								.getIsFvrIntiated().equals(Boolean.FALSE)) {

					FieldVisitRequest fvrobjByRodKey = fvrService
							.getPendingFieldVisitByClaimKey(bean.getClaimDTO()
									.getKey());

					if ((fvrobjByRodKey == null || (fvrobjByRodKey != null
							&& fvrobjByRodKey.getStatus() != null && ReferenceTable.INITITATE_FVR
							.equals(fvrobjByRodKey.getStatus().getKey())))) {

						if (fvrobjByRodKey != null) {
							dbCalculationService
									.invokeProcedureAutoSkipFVR(fvrobjByRodKey
											.getFvrId());
							fvrService.autoSkipFirstFVRParallel(fvrobjByRodKey,
									bean.getNewIntimationDTO()
											.getIntimationId(), bean
											.getStrUserName());

						}

					}
					getFieldVisitInitiateLayout();
				} else {
					showErrorPopUp("FVR request is in process cannot initiate another request");
				}

			}
			
		});
		
/*		HorizontalLayout horizontalLayout = new HorizontalLayout(claimCount,btnRRC,btnViewPackage,viewPreauthButton);
		horizontalLayout.setSpacing(true);*/
		
		Button elearnBtn = viewDetails.getElearnButton();
		
		TextField icrEarnedPremium = new TextField("ICR(Earned Premium %)");
		icrEarnedPremium.setReadOnly(Boolean.FALSE);
		if(null != bean.getNewIntimationDTO().getIcrEarnedPremium()){
			icrEarnedPremium.setValue(bean.getNewIntimationDTO().getIcrEarnedPremium());
		}
		icrEarnedPremium.setReadOnly(Boolean.TRUE);
		FormLayout icrLayout = new FormLayout(icrEarnedPremium);
		icrLayout.setMargin(false);
		icrLayout.setSpacing(true);
		
		Product product =bean.getNewIntimationDTO().getPolicy().getProduct();
		
		Button btnEsclateToRAW = new Button("Escalate To Raw");
		btnEsclateToRAW.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				SHAUtils.buildEsclateToRawView(esclateClaimToRawPageInstance,esclateClaimToRawPageViewObj,bean,SHAConstants.ZONAL_REVIEW);
			}
		});
		Button viewLinkedPolicy = viewDetails.getLinkedPolicyDetails(bean);
		
		HorizontalLayout secondlayout1 = null;
		VerticalLayout fvrInvesLayout = new VerticalLayout();
		fvrInvesLayout.addComponents(investigationBtn,initiateFVRBtn);
		fvrInvesLayout.setSpacing(true);
		fvrInvesLayout.setComponentAlignment(investigationBtn,Alignment.TOP_RIGHT);
		fvrInvesLayout.setComponentAlignment(initiateFVRBtn,Alignment.TOP_RIGHT);
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && 
				(ReferenceTable.FHO_PRODUCT_REVISED.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| (ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
					|| ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){			
			dataExtractionViewImplObj.setViewRTAButton(btnViewRTABalanceSI);
			secondlayout1 = new HorizontalLayout(btnRRC,elearnBtn,btnLumen,btnViewPolicySchedule,btnViewDoctorRemarks,viewEarlierRODDetails,btnViewRTABalanceSI,btnZUAAlert,btnViewPackage,viewPreauthButton);
		}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				this.bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && this.bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			secondlayout1 = new HorizontalLayout(btnRRC,elearnBtn,btnLumen,btnViewPolicySchedule,btnViewDoctorRemarks,viewEarlierRODDetails,updatePreviousButton,btnZUAAlert,btnViewPackage,viewPreauthButton);
		}else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			secondlayout1 = new HorizontalLayout(btnRRC,elearnBtn,btnLumen,btnViewPolicySchedule,btnViewDoctorRemarks,viewEarlierRODDetails,updatePreviousButton,btnZUAAlert,btnViewPackage,viewPreauthButton);
		}else if(ReferenceTable.GMC_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			
			if(bean.getPolicyDto().getGmcPolicyType() != null &&
					(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
				secondlayout1 = new HorizontalLayout(btnRRC,elearnBtn,btnLumen,btnViewPolicySchedule,btnViewDoctorRemarks,viewEarlierRODDetails,btnZUAAlert,btnViewPackage,viewPreauthButton,viewLinkedPolicy,icrLayout);
			} else {
				secondlayout1 = new HorizontalLayout(btnRRC,elearnBtn,btnLumen,btnViewPolicySchedule,btnViewDoctorRemarks,viewEarlierRODDetails,btnZUAAlert,btnViewPackage,viewPreauthButton,icrLayout);
			}
		}
		else
		{
			secondlayout1 = new HorizontalLayout(btnRRC,elearnBtn,btnLumen,btnViewPolicySchedule,btnViewDoctorRemarks,viewEarlierRODDetails,btnZUAAlert,btnViewPackage,viewPreauthButton);
		}
		
		if(bean.getClaimKey() != null){
			List<Preauth> preauthByClaimnKey = preauthService.getPreauthByClaimnKey(bean.getClaimKey());
			if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()){
				for(Preauth preauthDto : preauthByClaimnKey){
					if(preauthDto != null){
						if(preauthDto.getStatus().getKey() != null && ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS.equals(preauthDto.getStatus().getKey())){
							secondlayout1.addComponent(btnviewCashlessDocument, 2);
							break;
						}
					}
				}
			}
			
		}
		
		secondlayout1.setSpacing(true);
	
//		HorizontalLayout horizontalLayout1 = new HorizontalLayout(viewEarlierRODDetails);
		HorizontalLayout crmLayout1 = new HorizontalLayout(icrAGBR,btnEsclateToRAW);
		btnEsclateToRAW.setEnabled(Boolean.FALSE);
		crmLayout1.setSpacing(true);
		VerticalLayout verticalLayout1 = new VerticalLayout(crmLayout1,secondlayout1);	
		verticalLayout1.setSpacing(true);
		VerticalLayout verticalLayout = new VerticalLayout(verticalLayout1,vLayout,fvrInvesLayout);
		HorizontalLayout componentsHLayout = new HorizontalLayout(verticalLayout);
		return componentsHLayout;
	}
	

	@Override
	public void resetView() {
		
		if(null != this.wizard && !this.wizard.getSteps().isEmpty())
		{
			this.wizard.clearWizardMap(MEDICAL_APPROVAL);
			this.wizard.clearWizardMap("Data Extraction");
			this.wizard.clearWizardMap("previous Claim Zonal Review");
			this.wizard.clearWizardMap("premedical process zonal Review");	
			this.wizard.clearCurrentStep();
		}
//		initView();
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
		try
		{
			
			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
			
			
			if((ReferenceTable.ZMR_REFER_TO_BILL_ENTRY).equals(this.bean.getStatusKey()) &&  this.bean.getPreauthMedicalProcessingDetails().getRefBillEntyRsn() != null){
				if(this.bean.getPreauthMedicalDecisionDetails() == null){
					this.bean.setPreauthMedicalDecisionDetails(new PreauthMedicalDecisionDTO());
				}	
				this.bean.getPreauthMedicalDecisionDetails().setReferToBillEntryBillingRemarks(this.bean.getPreauthMedicalProcessingDetails().getRefBillEntyRsn());
			}	
			
			if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && ("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag()))
			{
				if(this.cmbReasonForReconsideration.getValue() != null){
					MastersValue reconsiderReasonMaster = new MastersValue();
					SelectValue reasonSelectValue = (SelectValue)this.cmbReasonForReconsideration.getValue();
					reconsiderReasonMaster.setKey(reasonSelectValue.getId());
					reconsiderReasonMaster.setValue(reasonSelectValue.getValue());			
					this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().setReconsiderationReasonId(reconsiderReasonMaster);
				}
			}
			
			if(this.bean.getStatusKey().equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS) && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
				if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0.0d)){
					warningMessageForCopay();
				}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0.0d)){
					alertMessageForCopay();
				}else{
//					fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW, this.bean);
					proceedForFinalSubmit();
				}
			}else{
				proceedForFinalSubmit();
//					fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW, this.bean);
			}
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
		}

	}
	
		/**			  
		 * Part of CR R1136
		 */
	private void proceedForFinalSubmit(){
		showSublimitAlert();
	}
	
public void warningMessageForCopay() {
		
		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("Selected Co-pay Percentage is Zero !!!!.</br> Do you wish to Proceed.</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				proceedForFinalSubmit();
//				fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW, bean);
//				//wizard.finish();
//				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
//				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
	}
	
	public void alertMessageForCopay() {
		
		String message = "Selected Co-pay Percentage is  "+bean.getHighestCopay() + " %" + "</br> Do you wish to Proceed.</b>";
		
		//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
		Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
//		Button homeButton = new Button("Yes");
//		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//		
//		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
//		horizontalLayout.setMargin(true);
//		
//		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
//		layout.setSpacing(true);
//		layout.setMargin(true);
//		HorizontalLayout hLayout = new HorizontalLayout(layout);
//		hLayout.setMargin(true);
//		
//		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("");
//		dialog.setClosable(false);
//		dialog.setContent(hLayout);
//		dialog.setResizable(false);
//		dialog.setModal(true);
//		dialog.show(getUI().getCurrent(), null, true);
//		
//		homeButton.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = 7396240433865727954L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				dialog.close();
//				fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW, bean);
//				
//				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
//				
//			}
//		});
		
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
		buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
				.toString());
		Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
				.toString());
		homeButton.addClickListener(new ClickListener() { 
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				proceedForFinalSubmit();
//				fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW, bean);
//				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				wizard.getFinishButton().setEnabled(true);
//				wizard.getFinishButton().setEnabled(true);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
					
			Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
							.toString());
					
					yesButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							releaseHumanTask();
							SHAUtils.setClearPreauthDTO(bean);
							fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_ZONAL_REVIEW,
									null);
							dataExtractionViewImplObj.setClearReferenceData();
							premedicalprocessingViewImplObj.setClearReferenceData();
							fireViewEvent(MedicalApprovalDataExtractionPagePresenter.REFERENCE_DATA_CLEAR, null);
							fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.REFERENCE_DATA_CLEAR, null);
							VaadinRequest currentRequest = VaadinService.getCurrentRequest();
							SHAUtils.clearSessionObject(currentRequest);
						}
						});
					noButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							
						}
						});
	}

	@Override
	public void initData(WizardInitEvent event) {
		// TODO Auto-generated method stub
		
	}
	
    public void getErrorMessage(String eMsg){
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

	@Override
	public void buildSuccessLayout() {
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Zonal Review Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox("Claim record saved successfully !!!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();

				toolBar.countTool();

				SHAUtils.setClearPreauthDTO(bean);
				fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_ZONAL_REVIEW, null);
				dataExtractionViewImplObj.setClearReferenceData();
				premedicalprocessingViewImplObj.setClearReferenceData();
				fireViewEvent(MedicalApprovalDataExtractionPagePresenter.REFERENCE_DATA_CLEAR, null);
				fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.REFERENCE_DATA_CLEAR, null);
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				
			}
		});
		
	}

	@Override
	public void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer) {
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;	
	}

	
	@Override
	public void loadEmployeeMasterData(AutocompleteField<EmployeeMasterDTO> field, List<EmployeeMasterDTO> employeeDetailsList) {
		rewardRecognitionRequestViewObj.loadEmployeeMasterData(field,  employeeDetailsList);			
	}

	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.VALIDATE_ZONAL_USER_RRC_REQUEST, bean);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
			if (!isValid) {
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Same user cannot raise request more than once from same stage", buttonsNamewithType);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.ZONAL_REVIEW);
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
	
	private void invokePreMedicalLumenRequest(){
		/*List<Long> listOfSettledStatus = new ArrayList<Long>();
		listOfSettledStatus.add(ReferenceTable.FINANCIAL_SETTLED);
		listOfSettledStatus.add(ReferenceTable.PAYMENT_SETTLED);
		listOfSettledStatus.add(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS);
		if(!listOfSettledStatus.contains(bean.getClaimDTO().getStatusId())){
			fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.ZONAL_REVIEW_LUMEN_REQUEST, bean);
		}else{
			showErrorMessage("Claim is settled, lumen cannot be initiated");
			return;
		}*/
		fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.ZONAL_REVIEW_LUMEN_REQUEST, bean);
	}
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	
	@Override
	public void buildInitiateLumenRequest(String intimationNumber){
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Initiate Lumen Request");
		popup.setWidth("75%");
		popup.setHeight("90%");
		LumenSearchResultTableDTO resultObj = lumenService.buildInitiatePage(intimationNumber);
		initiateLumenRequestWizardObj = initiateLumenRequestWizardInstance.get();
		initiateLumenRequestWizardObj.initView(resultObj, popup, "PCR ZMR");
		
		VerticalLayout containerLayout = new VerticalLayout();
		containerLayout.addComponent(initiateLumenRequestWizardObj);
		popup.setContent(containerLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	/*private void openPdfFileInWindow(final String filepath) {
		
		Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Hospital Package Rate");
		window.setWidth("800");
		window.setHeight("600");
		window.setModal(true);
		window.center();

		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(filepath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		window.setContent(e);
		UI.getCurrent().addWindow(window);
	}*/
	
	 public void compareWithUserId(String userId) {

			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("Intimation is already opened by another user  :</b>"+userId, buttonsNamewithType);
			Button okButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			
			okButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_ZONAL_REVIEW,null);
					//dialog.close();
				}
			});
			
	
 }
	 
	 
	 	@Override
		public void buildFailureLayout() {
		
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Home");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					releaseHumanTask();
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_ZONAL_REVIEW, null);
					SHAUtils.setClearPreauthDTO(bean);
					dataExtractionViewImplObj.setClearReferenceData();
					premedicalprocessingViewImplObj.setClearReferenceData();
					fireViewEvent(MedicalApprovalDataExtractionPagePresenter.REFERENCE_DATA_CLEAR, null);
					fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.REFERENCE_DATA_CLEAR, null);
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					SHAUtils.clearSessionObject(currentRequest);
					
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
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

		

	 	  public void getFieldVisitInitiateLayout(){
	 		
	 		 BeanItemContainer<SelectValue> fvrAllocationTo = masterService
		 				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO);
	 		BeanItemContainer<SelectValue> fvrAssignTo = null;
	 		BeanItemContainer<SelectValue> fvrPriority = masterService.getSelectValueContainer(ReferenceTable.FVR_PRIORITY);
	 		
	 		Map<String, BeanItemContainer<SelectValue>> values = new HashMap<String,BeanItemContainer<SelectValue>>();
			values.put("allocationTo", fvrAllocationTo);
			values.put("fvrAssignTo", fvrAssignTo);			
			values.put("fvrPriority", fvrPriority);
	 		  
	 		 if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS)) {
				 this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				 this.bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
				 this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints("");
			 }

			 if(!ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(this.bean.getStatusKey())){
				 ViewFVRDTO trgptsDto = null;
				 List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
				 for(int i = 1; i<=5;i++){
					 trgptsDto = new ViewFVRDTO();
					 trgptsDto.setRemarks("");
					 trgptsList.add(trgptsDto);
				 }
				 this.bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
			 }
			 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
			 this.bean.setStageKey(ReferenceTable.ZONAL_REVIEW_STAGE);
			 buildInitiateFieldVisit(values);
			
	 	  }
	 	
	 	  
	 		@Override
	 		public void setUpdateOtherClaimsDetails(List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailList) {

	 			Window popup = new com.vaadin.ui.Window();
	 			updateOtherClaimDetailsUI.init(updateOtherClaimDetailList, null, bean,popup);
	 			popup.setCaption("Update Previous/Other Claim Details(Defined Limit)");
	 			popup.setWidth("100%");
	 			popup.setHeight("70%");
	 			popup.setContent(updateOtherClaimDetailsUI);
	 			popup.setClosable(true);
	 			popup.center();
	 			popup.setResizable(true);
	 			popup.addCloseListener(new Window.CloseListener() {
	 				
	 				private static final long serialVersionUID = 1L;

	 				@Override
	 				public void windowClose(CloseEvent e) {
	 					System.out.println("Close listener called");
	 				}
	 			});

	 			popup.setModal(true);
	 			UI.getCurrent().addWindow(popup);
	 			
	 		}
	 	  
	 	  
	 	 public void buildInitiateFieldVisit(Object fieldVisitValues) {
	 		initBinder();
	 		
	 		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());

	 	    Map<String,BeanItemContainer<SelectValue>> map = (Map<String,BeanItemContainer<SelectValue>>)fieldVisitValues;
	 	
	 	//	allocationTo = (ComboBox) binder.buildAndBind("Allocation To","allocationTo",ComboBox.class);
	 	    allocationTo = new ComboBox("Allocation To");
	 		allocationTo.setContainerDataSource(map.get("allocationTo"));
	 		allocationTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	 		allocationTo.setItemCaptionPropertyId("value");
	 		if(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo() != null){
	 			allocationTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getAllocationTo());
	 		}
	 		
	 		//fvrPriority = (ComboBox) binder.buildAndBind("Priority","priority",ComboBox.class);
	 		fvrPriority = new ComboBox("Priority");
	 		fvrPriority.setContainerDataSource(map.get("fvrPriority"));
	 		fvrPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	 		fvrPriority.setItemCaptionPropertyId("value");
	 		if(this.bean.getPreauthMedicalDecisionDetails().getPriority()!= null){
	 			fvrPriority.setValue(this.bean.getPreauthMedicalDecisionDetails().getPriority());
	 		}

	 		triggerPtsTableObj = triggerPtsTable.get();
	 		triggerPtsTableObj.init();
	 		if(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList() != null && !bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList().isEmpty()){
	 			triggerPtsTableObj.setTableList(bean.getPreauthMedicalDecisionDetails().getFvrTriggerPtsList());
	 		}
	 		viewFVRDetails = new Button("View FVR Details");
	 		viewFVRDetails.addClickListener(new Button.ClickListener() {

	 			/**
	 			 * 
	 			 */
	 			private static final long serialVersionUID = 1L;

	 			@Override
	 			public void buttonClick(ClickEvent event) {
	 				viewDetails.getFVRDetails(bean.getNewIntimationDTO()
	 						.getIntimationId(), false);
	 			}
	 		});
	 		countFvr = new Label();
	 		if(bean.getFvrCount() != null){
	 			
	 			countFvr.setValue(bean.getFvrCount().toString() + " FVR Reports Already Exists");
	 		
	 		}
	 		
	 		FormLayout horizontalLayout = new FormLayout(countFvr, viewFVRDetails);
	 		
	 		HorizontalLayout horizontalLayout2 = new HorizontalLayout(new FormLayout(allocationTo,/*fvrAssignTo,*/fvrPriority), horizontalLayout);
	 		horizontalLayout2.setSpacing(true);
	 		
	 		//alignFormComponents();
	 		mandatoryFields= new ArrayList<Component>();
	 		mandatoryFields.add(allocationTo);
	 		mandatoryFields.add(fvrPriority);
	 		showOrHideValidation(false);
	 		
	 		final ConfirmDialog dialog = new ConfirmDialog();
	 		
	 		
	 		HorizontalLayout btnLayout = new HorizontalLayout(getSubmitFVRButtonWithListener(dialog), getFVRCancelButton(dialog));
	 		btnLayout.setSpacing(true);
	 		showOrHideValidation(false);
	 		
	 		VerticalLayout VLayout = new VerticalLayout(horizontalLayout2, triggerPtsTableObj,btnLayout);
	 		VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
	 		showInPopup(VLayout, dialog);
	 		
	 		/*HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Submit");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.CANCEL.toString(), "Cancel");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createCustomBox("", VLayout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.CANCEL
					.toString());
			getSubmitFVRButtonWithListener(homeButton);
			getFVRCancelButton(cancelButton);*/
	 	}
	 	 
	 	protected void showOrHideValidation(Boolean isVisible) {
			for (Component component : mandatoryFields) {
				AbstractField<?> field = (AbstractField<?>) component;
				if (field != null) {
					field.setRequired(!isVisible);
					field.setValidationVisible(isVisible);
				}
			}
		}
	 	
	 	private void showInPopup(Layout layout, ConfirmDialog dialog) {
			dialog.setCaption("");
			dialog.setClosable(true);

			Panel panel = new Panel();
			panel.setHeight("500px");
			panel.setWidth("850px");
			panel.setContent(layout);
			dialog.setContent(panel);
			dialog.setResizable(false);
			dialog.setModal(true);

			dialog.show(getUI().getCurrent(), null, true);

		}
	 	
	 	private Button getSubmitButtonWithListener(final ConfirmDialog dialog) {
			submitButton = new Button("Submit");
			submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			submitButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -5934419771562851393L;

				@Override
				public void buttonClick(ClickEvent event) {
					StringBuffer eMsg = new StringBuffer();
					if (isValid()) {
						dialog.close();
					} else {
						List<String> errors = getErrors();
						for (String error : errors) {
							eMsg.append(error);
						}
						showErrorPopup(eMsg.toString());
					}
				}
			});
			return submitButton;
		}
	 	
	 	private Button getCancelButton(final ConfirmDialog dialog) {
			cancelButton = new Button("Cancel");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -5934419771562851393L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					//binder = null;
				}
			});
			return cancelButton;
		}
	 	
	 	
	 	public boolean isValid() {
			boolean hasError = false;
			showOrHideValidation(true);
			errorMessages.removeAll(getErrors());
			
			if (this.binder == null) {
				hasError = true;
				errorMessages
						.add("Please select Approve or Reject or Escalate Claim or Refer to coordinator. </br>");
				return !hasError;
			}

			if(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS.equals(bean.getStatusKey())){
				if(triggerPtsTable != null){
					hasError = !triggerPtsTableObj.isValid();
					if(hasError){
						errorMessages.add("Please Provide atleast one FVR Trigger Point.</br>");
						errorMessages.add("FVR Trigger Points size will be Max. of 600.<br>");
					}
					else{
						bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(triggerPtsTableObj.getValues());
						bean.getPreauthMedicalDecisionDetails().setFvrTriggerPoints(triggerPtsTableObj.getTriggerRemarks());
					}
				}
			}
			if (!this.binder.isValid()) {
				hasError = true;
				for (Field<?> field : this.binder.getFields()) {
					ErrorMessage errMsg = ((AbstractField<?>) field)
							.getErrorMessage();
					if (errMsg != null) {
						errorMessages.add(errMsg.getFormattedHtmlMessage());
					}
				}
			} else {
				try {
					this.binder.commit();
					if (this.bean
							.getStatusKey()
							.equals(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS)) {
//						this.investigationReportReviewedChk.setEnabled(true);
					} else {
//						this.investigationReportReviewedChk.setValue(false);
//						this.investigationReportReviewedChk.setEnabled(false);
					}

				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			showOrHideValidation(false);
			return !hasError;
		}
	 	
	 	public List<String> getErrors() {
			return this.errorMessages;
		}
	 	
		private void showErrorPopup(String eMsg) {
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
		}

		@Override
		public void generateFieldsOnInvtClick(boolean directToAssignInv) {
			Window popup = new com.vaadin.ui.Window();
			popup.setClosable(true);
			popup.setWidth("75%");
			popup.setHeight("90%");
			popup.center();
			popup.setResizable(false);
			this.bean.setDirectToAssignInv(directToAssignInv);
			this.bean.setStatusKey(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS);
			this.bean.setStageKey(ReferenceTable.ZONAL_REVIEW_STAGE);
			ParallelInvestigationDetails viewInvestigationDetails = getRevisedInvestigationDetails(bean, true,ReferenceTable.ZONAL_REVIEW_STAGE,popup);
			popup.setContent(viewInvestigationDetails);
			popup.setModal(true);
			popup.setClosable(true);
			UI.getCurrent().addWindow(popup);
			//UI.getCurrent().addWindow((ParallelInvestigationDetails)viewInvestigationDetails);
			
		}

		@Override
		public void alertMessageForInvestigation() {

			
			String message = "Investigation Request has already been initiated.</b>";
			
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(message, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();	
//					generateFieldsOnInvtClick();
					//generateButtonFields(SHAConstants.INITIATE_INVESTIGATION);
					//fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, bean);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			/*
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					//wizard.getFinishButton().setEnabled(true);
					bean.setIsInvestigation(false);
//					wizard.getFinishButton().setEnabled(true);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});*/
		
			
		}
		
		public void setInitiateInitInvDto(InitiateInvestigationDTO  initInvDto){
			this.bean.setInitInvDto(initInvDto);
			this.bean.setStatusKey(ReferenceTable.ZONAL_REVIEW_INITATE_INV_STATUS);
		}
		
		public void showErrorPopUp(String emsg) {
		    
		    HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createWarningBox(emsg, buttonsNamewithType);
		}
		
		private Button getSubmitFVRButtonWithListener(final ConfirmDialog dialog) {
			submitButton = new Button("Submit");
			submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			submitButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -5934419771562851393L;

				@Override
				public void buttonClick(ClickEvent event) {
					StringBuffer eMsg = new StringBuffer();
					if (isValid()) {
						reimbursementService.initiateFVR(bean,SHAConstants.ZONAL_REVIEW);
						premedicalprocessingViewImplObj.setEnableOrDisableButtons();
						bean.getPreauthDataExtractionDetails().setIsFvrOrInvsInitiated(Boolean.TRUE);	
						bean.setInitInvDto(null);
						dataExtractionViewImplObj.setBillEntryDisable(bean.getPreauthDataExtractionDetails().getIsFvrOrInvsInitiated());
						dialog.close();
						buildFVRSuccessLayout();
						
						ViewFVRDTO trgptsDto = null;
						 List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
						 for(int i = 1; i<=5;i++){
							 trgptsDto = new ViewFVRDTO();
							 trgptsDto.setRemarks("");
							 trgptsList.add(trgptsDto);
						 }
						 bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
						 bean.getPreauthMedicalDecisionDetails().setAllocationTo(null);
						 bean.getPreauthMedicalDecisionDetails().setPriority(null);
						
					} else {
						List<String> errors = getErrors();
						for (String error : errors) {
							eMsg.append(error);
						}
						showErrorPopup(eMsg.toString());
					}
				}
			});
			return submitButton;
		}

		
		
		public ParallelInvestigationDetails getRevisedInvestigationDetails(PreauthDTO preauthDto, Boolean isDiabled,Long stageKey,Window popup) {
			
			String intimationNo = preauthDto.getNewIntimationDTO().getIntimationId();
			bean.setDirectToAssignInv(preauthDto.isDirectToAssignInv());
			Intimation intimation = intimationService
						.searchbyIntimationNo(intimationNo);
				Long claimKey = null;
				if (intimation != null) {
					Claim claim = claimService.getClaimforIntimation(intimation
							.getKey());
					if (null != claim) {
						claimKey = claim.getKey();
					}
				}
				if(null != stageKey && stageKey.equals(ReferenceTable.ZONAL_REVIEW_STAGE))
				{
					isDiabled = true;
				}
				else
				{
					isDiabled = false;
				}
								
				viewInvestigationDetails.init(isDiabled,bean,popup,null);
				
				if(null != dataExtractionViewImplObj && ReferenceTable.ZONAL_REVIEW_STAGE.equals(bean.getStageKey())){
					viewInvestigationDetails.init(isDiabled,bean,popup,dataExtractionViewImplObj);
				}
				//this.parent = parent;
				viewInvestigationDetails.showRevisedValues(claimKey,stageKey,bean);
				addLisenerForInvestigation();
				InitiateInvestigationDTO initateInvDto = viewInvestigationDetails.getInitateInvDto();
				bean.setInitInvDto(initateInvDto);
				return viewInvestigationDetails;
			}
		
		public void addLisenerForInvestigation(){
			viewInvestigationDetails.dummyField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 4843316375590220412L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
				
					premedicalprocessingViewImplObj.setEnableOrDisableButtons();
					
				}
			});
		}
		
		public void buildFVRSuccessLayout() {
			
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("<b style = 'color: green;'>FVR has been initiated successfully!!</b>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
								
					if(null != bean.getStageKey() && bean.getStageKey().equals(ReferenceTable.ZONAL_REVIEW_STAGE))
					{	Collection<Window> windows =  (Collection<Window>)getUI().getCurrent().getWindows();
						Object[] winArray = windows.toArray();
						for(int i = 0; i < winArray.length;i++){
							((Window)winArray[i]).close();
						}
					}
					
					toolBar.countTool();

					
				}
			});
			
		}
		
		private Button getFVRCancelButton(final ConfirmDialog dialog) {
			cancelButton = new Button("Cancel");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -5934419771562851393L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					
					ViewFVRDTO trgptsDto = null;
					 List<ViewFVRDTO> trgptsList = new ArrayList<ViewFVRDTO>();
					 for(int i = 1; i<=5;i++){
						 trgptsDto = new ViewFVRDTO();
						 trgptsDto.setRemarks("");
						 trgptsList.add(trgptsDto);
					 }
					 bean.getPreauthMedicalDecisionDetails().setFvrTriggerPtsList(trgptsList);
					//binder = null;
				}
			});
			return cancelButton;
		}
		
		public void confirmationForInvestigation(boolean isdir) {

			
			String message = "Investigation already raised has been completed. </br>Do you want to proceed further?";
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox.createConfirmationbox(message, buttonsNamewithType);
			
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();	
					generateFieldsOnInvtClick(bean.isDirectToAssignInv());
				}
			});
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					bean.setIsInvestigation(false);
				}
			});
		}
		
			
		/**			  
		 * Part of CR R1136
		 */
		public void showSublimitAlert() {
				boolean sublimitMapAvailable = false;
				List<DiagnosisDetailsTableDTO> diagnosisList = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
				if(diagnosisList != null && !diagnosisList.isEmpty()){
						StringBuffer selectedSublimitNames = new StringBuffer("");
						for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
							if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getSublimitName() != null){
								sublimitMapAvailable = sublimitMapAvailable || diagnosisDetailsTableDTO.isSublimitMapAvailable();
								selectedSublimitNames = selectedSublimitNames.toString().isEmpty() ? selectedSublimitNames.append(diagnosisDetailsTableDTO.getSublimitName().getName()) : selectedSublimitNames.append(", ").append(diagnosisDetailsTableDTO.getSublimitName().getName());
							}
						}
					
					
					if(sublimitMapAvailable && !selectedSublimitNames.toString().isEmpty()){
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
								.createInformationBox("Sublimit selected is "+selectedSublimitNames.toString()+"</b>", buttonsNamewithType);
						Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
								.toString());
						homeButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 1L;
			
							@Override
							public void buttonClick(ClickEvent event) {
								wizard.getFinishButton().setEnabled(true);
								fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW, bean);
							}
						});
						//dialog.show(getUI().getCurrent(), null, true);
					}
					else if (!sublimitMapAvailable){
						fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.SUBMIT_MEDICAL_APPROVAL_ZONAL_REVIEW, bean);	
					}
					
					
			  }	
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

