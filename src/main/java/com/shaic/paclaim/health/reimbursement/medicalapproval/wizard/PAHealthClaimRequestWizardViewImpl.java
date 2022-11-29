package com.shaic.paclaim.health.reimbursement.medicalapproval.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.preauth.wizard.view.ParallelInvestigationDetails;
import com.shaic.claim.registration.balancesuminsured.view.ViewUnnamedRiskDetailsUI;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestButtonsForCommonWizard;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestWizardViewImpl;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.ReconsiderRODRequestListenerTable;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Page.RevisedPreauthViewPage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.dataextraction.PAHealthClaimRequestDataExtractionPageViewImpl;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision.PAHealthClaimRequestMedicalDecisionPageViewImpl;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.premedicalprocessing.PAHealthClaimRequestPremedicalProcessingViewImpl;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.previousclaims.PAHealthClaimRequestPreviousClaimsPageViewImpl;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.zybnet.autocomplete.server.AutocompleteField;

public class PAHealthClaimRequestWizardViewImpl extends AbstractMVPView implements
PAHealthClaimRequestWizard {
	private static final long serialVersionUID = -6326484157414527897L;
	
	public static final String CLAIM_REQUEST= "Claim Request reset view";
	
	@Inject
	private Instance<IWizard> iWizard;
	
	private IWizardPartialComplete wizard;
	
	private FieldGroup binder;
	
	private PreauthDTO bean;
	
	private VerticalSplitPanel mainPanel;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private RevisedPreauthViewPage viewPreauth;
	
	private VerticalLayout reconsiderationLayout;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	@Inject
	private Instance<PAHealthClaimRequestDataExtractionPageViewImpl> dataExtractionViewImpl;
	
	@Inject
	private Instance<PAHealthClaimRequestMedicalDecisionPageViewImpl> medicalDecisionViewImpl;
	
	@Inject
	private Instance<PAHealthClaimRequestPreviousClaimsPageViewImpl> previousClaimViewImpl;
	
	@Inject
	private Instance<PAHealthClaimRequestPremedicalProcessingViewImpl> premedicalprocessingViewImpl;
	
	private PAHealthClaimRequestDataExtractionPageViewImpl dataExtractionViewImplObj;
	
	private PAHealthClaimRequestPremedicalProcessingViewImpl premedicalprocessingViewImplObj;
	
	private PAHealthClaimRequestPreviousClaimsPageViewImpl previousClaimViewImplObj;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	private PAHealthClaimRequestMedicalDecisionPageViewImpl medicalDecisionViewImplObj;



	private VerticalLayout dummyLayout;
	
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
	
	@EJB
	private HospitalService hospitalService;
	
	@Inject
	private Instance<ViewUnnamedRiskDetailsUI> unNamedRiskDetailsInstance;
	
	private ViewUnnamedRiskDetailsUI unNamedRiskDetailsObj;
	
	@Inject
	private Toolbar toolBar;
	
	@Inject
	private Instance<PAClaimRequestButtonsForCommonWizard> claimRequestWizardButtonInstance;

	private PAClaimRequestButtonsForCommonWizard claimRequestWizardButtons;

	@Inject
	private ParallelInvestigationDetails viewInvestigationDetails;

	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;


	private FormLayout queryFrmLayout;
	
	private FormLayout userLayout;

	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;
	

	private TextField txtQueryCount;
	
	private ComboBox cmbQueryType;
	
	private TextArea queryRemarksTxta;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackService;
	
	@EJB
	private MasterService masterService;
	
	private HorizontalLayout viewAllDocsLayout = null;
	
	private Button viewClaimsDMSDocument;
	
	private ArrayList<Component> mandatoryFields;
	
	private Button submitButton;

	public Button cancelButton;
	
	@Inject
	private CreateRODService createRodService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	
	private Map<String, String> roleValidationContainer = new HashMap<String, String>();
	private Map<String, String> userValidationContainer = new HashMap<String, String>();
	
	@EJB
	private PreauthService preauthService;
	
	private List<String> errorMessages;
	
	private VerticalLayout wholeVlayout;	
	
	private final Logger log = LoggerFactory.getLogger(PAHealthClaimRequestWizardViewImpl.class);
	
	
	public Map<String, String> getRoleValidationContainer() {
		return roleValidationContainer;
	}

	public void setRoleValidationContainer(
			Map<String, String> roleValidationContainer) {
		this.roleValidationContainer = roleValidationContainer;
	}

	public Map<String, String> getUserValidationContainer() {
		return userValidationContainer;
	}

	public void setUserValidationContainer(
			Map<String, String> userValidationContainer) {
		this.userValidationContainer = userValidationContainer;
	}
	
	public List<String> getErrors() {
		return this.errorMessages;
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
		this.binder.setItemDataSource(item);
		
	}
	
	private VerticalLayout getZonlaReviewDetails() {
		
		TextField status = new TextField("Zonal Medical Suggestion");
		status.setEnabled(false);
		TextArea remarks = new TextArea("Remarks(Zonal Medical)");
		remarks.setEnabled(false);
		status.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		status.setNullRepresentation("");
		
		remarks.setValue(this.bean.getPreauthMedicalProcessingDetails().getApprovalRemarks());
		
		remarks.setNullRepresentation("");
		VerticalLayout layout = new VerticalLayout(new FormLayout(status, remarks) );
		layout.setCaption("Zonal Medical Review Details");
		layout.setSpacing(true);
		return layout;
	}
	
	private VerticalLayout getFinancialMedicalDetails(){
		
		TextField reason = new TextField("Reason for referring to Medical");
		reason.setEnabled(false);
		reason.setValue(this.bean.getPreviousReasonForReferring());
		TextArea remarks = new TextArea("Remarks");
		remarks.setEnabled(false);
		remarks.setValue(this.bean.getPreviousRemarks());
		reason.setNullRepresentation("");
		remarks.setNullRepresentation("");
		VerticalLayout layout = new VerticalLayout(new FormLayout(reason, remarks) );
		layout.setCaption("Financial Approval Details");
		layout.setSpacing(true);
		return layout;
	}
	
   private VerticalLayout getBillingMedicalDetails(){
		
		TextField reason = new TextField("Reason for referring to Medical");
		reason.setEnabled(false);
		reason.setValue(this.bean.getPreviousReasonForReferring());
		TextArea remarks = new TextArea("Remarks");
		remarks.setEnabled(false);
		remarks.setValue(this.bean.getPreviousRemarks());
		reason.setNullRepresentation("");
		remarks.setNullRepresentation("");
		VerticalLayout layout = new VerticalLayout(new FormLayout(reason, remarks) );
		layout.setCaption("Billing Details");
		layout.setSpacing(true);
		return layout;
	   }
	
	
	private VerticalLayout getRejectionDetails(){
		
		TextArea remarks = new TextArea("Rejection Remarks");
		remarks.setEnabled(false);
		remarks.setNullRepresentation("");
		remarks.setValue(this.bean.getPreauthMedicalDecisionDetails().getRejectionRemarks());
		
		VerticalLayout layout = new VerticalLayout(new FormLayout(remarks) );
		layout.setSpacing(true);
		
		return layout;
		
	}
	
    private VerticalLayout getQueryDetails(){
		
		TextArea remarks = new TextArea("Query Rejection Remarks");
		remarks.setEnabled(false);
		
		remarks.setNullRepresentation("");
		
		remarks.setValue(this.bean.getPreauthMedicalDecisionDetails().getQueryRemarks());
		
		VerticalLayout layout = new VerticalLayout(new FormLayout(remarks) );
		layout.setSpacing(true);
		
		return layout;
		
	}
	
	private VerticalLayout getBillingDetails() {
		
		TextField status = new TextField();
		TextArea remarks = new TextArea();
		
		VerticalLayout layout = new VerticalLayout(status, remarks);
		layout.setSpacing(true);
		return layout;
	}
	
	private VerticalLayout getFinancialDetails() {
		
		TextField status = new TextField();
		TextArea remarks = new TextArea();
		
		VerticalLayout layout = new VerticalLayout(status, remarks);
		layout.setSpacing(true);
		return layout;
	}
	
	/*private VerticalLayout commonValues() 
	{	
		cmbReconsiderationRequest = new ComboBox("Reconsideration Request");
	//Vaadin8-setImmediate() cmbReconsiderationRequest.setImmediate(true);
	Long j = 1l;
	List<SelectValue> values = new ArrayList<SelectValue>();
	for(int i=0; i < 2; i++) {
		SelectValue value = new SelectValue();
		value.setId(j);
		if(i == 1) {
			value.setValue("Yes");
		} else {
			value.setValue("No");
		}
		j++;
		values.add(value);
		
	}
	
	BeanItemContainer<SelectValue> beanContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	beanContainer.addAll(values);
	cmbReconsiderationRequest.setContainerDataSource(beanContainer);
	cmbReconsiderationRequest.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	cmbReconsiderationRequest.setItemCaptionPropertyId("value");
	if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest().toLowerCase().equalsIgnoreCase("y")) {
		SelectValue selectValue = values.get(0);
		if(selectValue.getValue().toLowerCase().equalsIgnoreCase("yes")) {
			cmbReconsiderationRequest.setValue(values.get(0));
		} else {
			cmbReconsiderationRequest.setValue(values.get(1));
		}
		
	} else {
		SelectValue selectValue = values.get(1);
		if(selectValue.getValue().toLowerCase().equalsIgnoreCase("no")) {
			cmbReconsiderationRequest.setValue(values.get(1));
		} else {
			cmbReconsiderationRequest.setValue(values.get(0));
		}
	}
	cmbReconsiderationRequest.setEnabled(false);
	cmbReconsiderationRequest.addValueChangeListener(new Property.ValueChangeListener() {
		private static final long serialVersionUID = 1L;
		@Override
		public void valueChange(ValueChangeEvent event) {
			SelectValue value = (SelectValue) event.getProperty().getValue();
			if(null != value)
			{	
				if (reconsiderationLayout != null
						&& reconsiderationLayout.getComponentCount() > 0) {
					dummyLayout.removeAllComponents();
				}
				if(("no").equalsIgnoreCase(value.getValue()))
				{	
					if (reconsiderationLayout != null
							&& reconsiderationLayout.getComponentCount() > 0) {
						dummyLayout.removeAllComponents();
					}
				}
				else
				{
					reconsiderRequestDetails.init("Earlier Request to be Recosidered", false, false);
					dummyLayout.addComponent(reconsiderRequestDetails);
				}
			}
			
		}
	});
	dummyLayout = new VerticalLayout();
	if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest().toLowerCase().equalsIgnoreCase("y")) {
		dummyLayout = new VerticalLayout(reconsiderRequestDetails);
	}
	reconsiderRequestDetails.init("Earlier Request to be Recosidered", false, false);
	reconsiderRequestDetails.setTableList(this.bean.getReconsiderationList());
	reconsiderationLayout = new VerticalLayout(new FormLayout(cmbReconsiderationRequest), dummyLayout);
	return reconsiderationLayout;
	}*/
	
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

		if(("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()))
		{
			reconsiderationLayout = new VerticalLayout(new FormLayout(cmbReconsiderationRequest,cmbReasonForReconsideration), vLayout);
		}
		else
		{
			reconsiderationLayout = new VerticalLayout(new FormLayout(cmbReconsiderationRequest));
		}
		
		return reconsiderationLayout;
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
		mandatoryFields = new ArrayList<Component>();
		errorMessages = new ArrayList<String>();
		wholeVlayout = new VerticalLayout();
		wholeVlayout.setHeight("-1px");
		wholeVlayout.setWidth("-1px");
		wholeVlayout.setSpacing(true);
		queryFrmLayout = new FormLayout();		
		queryFrmLayout.setHeight("100%");
		queryFrmLayout.setWidth("100%");
		queryFrmLayout.setMargin(true);
		queryFrmLayout.setSpacing(true);
		
//		this.wizard = iWizard.get();
		this.wizard = new IWizardPartialComplete();
		initBinder();
		
		String screenName = "PAHealthClaimRequestWizard";
		claimRequestWizardButtons = claimRequestWizardButtonInstance.get();
		claimRequestWizardButtons.initView(this.bean, wizard,screenName);
		
		dataExtractionViewImplObj = dataExtractionViewImpl.get();
		dataExtractionViewImplObj.init(this.bean , wizard);
		wizard.addStep(dataExtractionViewImplObj,"Claim Request Data Extration");
		
		previousClaimViewImplObj = previousClaimViewImpl.get();
		previousClaimViewImplObj.init(this.bean);
		wizard.addStep(previousClaimViewImplObj,"Previous Claim");
		
		premedicalprocessingViewImplObj = premedicalprocessingViewImpl.get();
		premedicalprocessingViewImplObj.init(this.bean);
		wizard.addStep(premedicalprocessingViewImplObj,"premedical process");
		
		medicalDecisionViewImplObj = medicalDecisionViewImpl.get();
		medicalDecisionViewImplObj.init(this.bean,this.wizard);
		wizard.addStep(medicalDecisionViewImplObj,"Medical Decision");
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		TextField typeFld = new TextField("Type");
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setNullRepresentation("");
		typeFld.setReadOnly(true);
		VerticalLayout commonvalues=new VerticalLayout(commonValues());
		FormLayout hLayout = new FormLayout (typeFld);
		//VerticalLayout vLayout= new VerticalLayout(hLayout,commonvalues);
		
		HorizontalLayout vLayout = null;
		
		if(this.bean.getInsuredPedDetails() != null && ! this.bean.getInsuredPedDetails().isEmpty()){
			
			Panel insuredPedDetailsPanel = getInsuredPedDetailsPanel();
			
			vLayout= new HorizontalLayout(hLayout,commonvalues,insuredPedDetailsPanel);
			vLayout.setSpacing(true);
			vLayout.setComponentAlignment(commonvalues, Alignment.BOTTOM_LEFT);
			vLayout.setComponentAlignment(insuredPedDetailsPanel, Alignment.BOTTOM_RIGHT);
			
		}else{
			
			vLayout= new HorizontalLayout(hLayout,commonvalues);
			vLayout.setSpacing(true);
			vLayout.setComponentAlignment(commonvalues, Alignment.BOTTOM_LEFT);
		}
		//FormLayout fLayout = new FormLayout();
		VerticalLayout fLayout = new VerticalLayout();
		//fLayout.setComponentAlignment(commonvalues, Alignment.BOTTOM_LEFT);
		Label dummyLabel = new Label("");
		dummyLabel.setWidth("50px");
		String panelName =  "Process Claim Request - Hospitalisation";
		if(null != bean.getScreenName() && (bean.getScreenName().equalsIgnoreCase(SHAConstants.PA_HEALTH_MEDICAL_WAIT_FOR_INPUT_SCREEN))){
			panelName = "Waiting For Input - Hospitalisation";
		}
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "Process Claim Request");
		intimationDetailsCarousel.init(this.bean, panelName);
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(), bean.getKey(),ViewLevels.PA_PROCESS, false,"Process Claim Request");
		
		HorizontalLayout horizontal = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		horizontal.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		horizontal.setWidth("100%");
		HorizontalLayout dupLayout = new HorizontalLayout();
		VerticalLayout wizardLayout1 = new VerticalLayout(horizontal,dupLayout);
		
		if(this.bean.getClaimKey() != null && this.bean.getClaimKey().equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)) {
			// wizardLayout1.addComponent(getZonlaReviewDetails());
		}
		
		if(this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS)){
			
			fLayout.addComponent(getRejectionDetails());
			fLayout.setSpacing(true);
			this.bean.getPreauthMedicalDecisionDetails().setRejectionRemarks("");
			
		}else if(this.bean.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)){
			
			fLayout.addComponent(getQueryDetails());
			fLayout.setSpacing(true);
			this.bean.getPreauthMedicalDecisionDetails().setQueryRemarks("");
			
			
		}else if(this.bean.getStatusKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER)){
			fLayout.addComponent(getFinancialMedicalDetails());
			fLayout.setSpacing(true);
			
			
		}else if(this.bean.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)){
			fLayout.addComponent(getBillingMedicalDetails());
			fLayout.setSpacing(true);
		}
	    else{
	    	//fLayout.addComponent(getZonlaReviewDetails());
	    	//fLayout.setSpacing(true);
		}
		//HorizontalLayout mainHorizantal = new HorizontalLayout(wizardLayout1,vLayout);

		HorizontalLayout buttonsHLayout = new HorizontalLayout(
				claimRequestWizardButtons);
		wizardLayout1.setHeight("35px");
		VerticalLayout mainHorizantal = new VerticalLayout(wizardLayout1,dupLayout,vLayout,fLayout,buttonsHLayout);
		mainHorizantal.setComponentAlignment(buttonsHLayout, Alignment.TOP_RIGHT);
		mainHorizantal.setSpacing(true);
		Panel panel1 = new Panel();
		panel1.setContent(mainHorizantal);
//		panel1.setHeight("200px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, wizard);
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		
		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
		if(bean.getTaskNumber() != null){
			/*String aciquireByUserId = SHAUtils.getAciquireByUserId(bean.getTaskNumber());
			if((bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				compareWithUserId(aciquireByUserId);
			}*/
		}
		if(bean.getIsSDEnabled() && claimRequestWizardButtons.getQueryBtn().isEnabled()){
			claimRequestWizardButtons.getQueryBtn().setEnabled(false);
		} 
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
	
	
	private VerticalLayout commonButtonsLayout()
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
		
		
		
		TextField reconsiderationFld = new TextField("Reconsideration Claim");
		reconsiderationFld.setValue(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag().toLowerCase().equalsIgnoreCase("y") ? "Yes" : "No" );
		reconsiderationFld.setNullRepresentation("");
		reconsiderationFld.setReadOnly(true);
		FormLayout considerationLayout = new FormLayout (reconsiderationFld);
		considerationLayout.setWidth("344px");
		VerticalLayout considerationVLayout = new VerticalLayout(considerationLayout);
		considerationVLayout.setWidth("100%");
		considerationVLayout.setComponentAlignment(considerationLayout, Alignment.MIDDLE_RIGHT);
		
		
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
		firstForm.setHeight("50px");
		firstForm.setComponentAlignment(txtClaimCount, Alignment.TOP_LEFT);
//		txtClaimCount.addStyleName("fail");
//		claimCount.setWidth(txtClaimCount.getWidth(),txtClaimCount.getWidthUnits());
//		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			claimCount.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			claimCount.addStyleName("girdBorder2");
		}

		
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
				}else {
					
					getErrorMessage("Preauth is not available");
					
				}
				
				
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
				rewardRecognitionRequestViewObj.initPresenter(SHAConstants.CLAIM_REQUEST);
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
				UI.getCurrent().addWindow(popup);
				*/
				
			}
			
		});
		
		Button btnViewPackage = new Button("View Package");
		btnViewPackage.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getHospitalDto() != null && bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null){
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
		
		FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()

		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(), bean.getKey(),ViewLevels.PREAUTH_MEDICAL, false,"Process Claim Request");
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
		
		Button btnViewDoctorRemarks = new Button("Doctor Remarks");
		btnViewDoctorRemarks.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getDoctorRemarks(bean.getNewIntimationDTO().getIntimationId());
			}
			
		});
	
		
		
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
		HorizontalLayout horizontalLayout = new HorizontalLayout(claimCount,btnRRC,btnViewPackage,viewPreauthButton,btnViewPolicySchedule);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setHeight("27px");
		TextField dumText = new TextField();
		dumText.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		HorizontalLayout secondLayout = new HorizontalLayout(dumText,icrAgentBranch,btnViewDoctorRemarks,viewEarlierRODDetails);
		
		if(null != bean.getClaimDTO() && null != bean.getClaimDTO().getNewIntimationDto() && null != bean.getClaimDTO().getNewIntimationDto().getPolicy()&&
				null !=  bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct() && null !=  bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() &&
				(ReferenceTable.getGPAProducts().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())) &&
				(SHAConstants.GPA_UN_NAMED_POLICY_TYPE.equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getGpaPolicyType()))){	
			
		 secondLayout = new HorizontalLayout(dumText,btnViewDoctorRemarks,viewEarlierRODDetails,unNamedRiskDetailsLayout);
		}
		
		secondLayout.setSpacing(true);
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPolicyKey());
		
		TextField cmdClubMembership = new TextField("Club Membership");
		cmdClubMembership.setValue(memberType);
		cmdClubMembership.setReadOnly(true);
		cmdClubMembership.setStyleName("style = background-color: yellow");
		
		FormLayout clubMembershipLayout = new FormLayout(cmdClubMembership);
		clubMembershipLayout.setMargin(false);
		
		if (null != memberType && !memberType.isEmpty()) {
			secondLayout.addComponent(clubMembershipLayout);
		}
		
		//horizontalLayout.setWidth("100%");
//		HorizontalLayout horizontalLayout1 = new HorizontalLayout(viewEarlierRODDetails);
		//horizontalLayout1.setSpacing(true);
	//	VerticalLayout buttons = new VerticalLayout( horizontalLayout);
	//	buttons.setSpacing(true);
		//componentsHLayout.setComponentAlignment(btnRRC, Alignment.TOP_RIGHT);
	//	HorizontalLayout componentsHLayout = new HorizontalLayout(buttons ,dummyLabel,viewDetailsForm);
		
	//	componentsHLayout.setComponentAlignment(buttons, Alignment.TOP_LEFT);
	//	componentsHLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
//		componentsHLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
//		componentsHLayout.setComponentAlignment(buttons, Alignment.MIDDLE_RIGHT);

		//componentsHLayout.setSpacing(true);
	//	componentsHLayout.setHeight("5px");
	//	
		
//		VerticalLayout horizontalLayout = new VerticalLayout( btnRRC, viewEarlierRODDetails,viewPreauthButton);
//		horizontalLayout.setComponentAlignment(btnRRC, Alignment.MIDDLE_RIGHT);
//		horizontalLayout.setComponentAlignment(viewEarlierRODDetails, Alignment.MIDDLE_RIGHT);
//		horizontalLayout.setComponentAlignment(viewPreauthButton, Alignment.BOTTOM_RIGHT);
//		horizontalLayout.setSpacing(true);
//		horizontalLayout.setWidth("100%");
		HorizontalLayout dummyhlayout = new HorizontalLayout();
		VerticalLayout verticalLayout1 = new VerticalLayout(horizontalLayout,dummyhlayout,secondLayout);
		VerticalLayout verticalLayout = new VerticalLayout(verticalLayout1/*, horizontalLayout*/);
//		verticalLayout.setComponentAlignment(viewDetailsForm, Alignment.TOP_RIGHT);
		verticalLayout.setSpacing(true);
		verticalLayout.setHeight("5px");
//		verticalLayout.setWidth("1000px");
		//HorizontalLayout alignmentHLayout = new HorizontalLayout(componentsHLayout);
//		verticalLayout.setComponentAlignment(considerationVLayout, Alignment.MIDDLE_RIGHT);
//		horizontalLayout.setWidth("100%");
		return verticalLayout;
	}
	

	@Override
	public void resetView() {
		
		if(null != this.wizard && !this.wizard.getSteps().isEmpty())
		{
			this.wizard.clearWizardMap(CLAIM_REQUEST);
			this.wizard.clearWizardMap("Claim Request Data Extration");
			this.wizard.clearWizardMap("Previous Claim");
			this.wizard.clearWizardMap("premedical process");
			this.wizard.clearWizardMap("Medical Decision");
			this.wizard.clearCurrentStep();
		}
//		initView();
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
		finalSubmit();
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
									if(null != bean.getScreenName() && !(bean.getScreenName().equalsIgnoreCase(SHAConstants.PA_HEALTH_MEDICAL_WAIT_FOR_INPUT_SCREEN))){
										fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, bean.getSearchFormDTO());
									}else{
										fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST,
												bean.getSearchFormDTO());									}
									
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
				"<b style = 'color: green;'> Claim has been processed successfully !!!</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Corporate Review Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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
				toolBar.countTool();
				if(null != bean.getScreenName() && (bean.getScreenName().equalsIgnoreCase(SHAConstants.PA_HEALTH_MEDICAL_WAIT_FOR_INPUT_SCREEN))){
					fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, bean.getSearchFormDTO());
				}else{
					fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST, bean.getSearchFormDTO());
				}
			

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
		fireViewEvent(PAHealthClaimRequestWizardPresenter.VALIDATE_CLAIM_REQUEST_USER_RRC_REQUEST, bean);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PA_CLAIM_REQUEST_HOSP);
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
	
	private void openPdfFileInWindow(final String filepath) {
		
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
					
					if(null != bean.getScreenName() && !(bean.getScreenName().equalsIgnoreCase(SHAConstants.PA_HEALTH_MEDICAL_WAIT_FOR_INPUT_SCREEN))){
						fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, null);
					}else{
						fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST, null);
					}
					dialog.close();
				}
			});
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>Intimation is already opened by another user  :</b>"+userId, ContentMode.HTML), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);
	
 }
	 
	 	@Override
		public void buildFailureLayout() {
		
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			Label successLabel = new Label("<b style = 'color: green;'>Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Claim Request Home");
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
					if(null != bean.getScreenName() && !(bean.getScreenName().equalsIgnoreCase(SHAConstants.PA_HEALTH_MEDICAL_WAIT_FOR_INPUT_SCREEN))){
						fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, null);
					}else{
						fireViewEvent(MenuItemBean.PA_HEALTH_PROCESS_CLAIM_REQUEST, null);
					}
					
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
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
		 

		 @Override
		 public void generateFieldsOnInvtClick(boolean isDirectToAssignInv) {
			 Window popup = new com.vaadin.ui.Window();
			 popup.setClosable(true);
			 popup.setWidth("75%");
			 popup.setHeight("90%");
			 popup.center();
			 popup.setResizable(false);
			 this.bean.setDirectToAssignInv(isDirectToAssignInv);
			 // comment for GALAXYMAIN-13428 
			 //				this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
			 this.bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
			 this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
			 ParallelInvestigationDetails viewInvestigationDetails = getRevisedInvestigationDetails(bean, true,ReferenceTable.ZONAL_REVIEW_STAGE,popup);
			 popup.setContent(viewInvestigationDetails);
			 popup.setModal(true);
			 popup.setClosable(true);
			 UI.getCurrent().addWindow(popup);
		 }

		 public ParallelInvestigationDetails getRevisedInvestigationDetails(PreauthDTO preathDto, Boolean isDiabled,Long stageKey,Window popup) {
			 String intimationNo = preathDto.getNewIntimationDTO().getIntimationId();
			 bean.setDirectToAssignInv(preathDto.isDirectToAssignInv());
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
			 viewInvestigationDetails.showRevisedValues(claimKey,stageKey,bean);
			 InitiateInvestigationDTO initateInvDto = viewInvestigationDetails.getInitateInvDto();
			 bean.setInitInvDto(initateInvDto);
			 return viewInvestigationDetails;
		 }


		 @Override
		 public void generateQueryLayout() {
			 // comment for GALAXYMAIN-13428 
			 //				this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
			 this.bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
			 buildQueryLayout();
		 }

		 public void buildQueryLayout() {

			 final Integer setQueryValues = setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());
			 if(setQueryValues > 0) {
				 alertMessage(SHAConstants.QUERY_RAISE_MESSAGE, setQueryValues);
			 } else {
				 generateQueryDetails(setQueryValues);

			 }

		 }
		 public Boolean alertMessage(String message, final Integer count) {
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
					 generateQueryDetails(count);
				 }
			 });
			 return true;
		 }

		 private void generateQueryDetails(Integer setQueryValues) {
			 initBinder();
			 unbindAndRemoveComponents(queryFrmLayout);
			 //				unbindAndRemoveComponents(userLayout);

			 queryDetailsTableObj.init("Previous Query Details",false,false);
			 queryDetailsTableObj.setViewQueryVisibleColumn();

			 setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());

			 /*//R1295
				cmbQueryType = new ComboBox("Query Type");// binder.buildAndBind("Query Type","queryType",ComboBox.class);
				cmbQueryType.setContainerDataSource(masterService.getOpinionQueryType());
				cmbQueryType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbQueryType.setItemCaptionPropertyId("value");
				cmbQueryType.setWidth("200px");
				if(bean.getQueryType() != null){
					cmbQueryType.setValue(bean.getQueryType());
				}
				final int queryCount = setQueryValues;
				cmbQueryType.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -486851813151743902L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						SelectValue value = (SelectValue) event.getProperty().getValue();
						if(value != null){
							bean.setQueryType(value);
							bean.setQueryCount(queryCount);

							unbindAndRemoveComponents(queryFrmLayout);
//							unbindAndRemoveComponents(userLayout);
							generateQueryDetails(queryCount);
						}else{
							bean.setQueryType(null);
							bean.setQueryCount(queryCount);

							unbindAndRemoveComponents(queryFrmLayout);
//							unbindAndRemoveComponents(userLayout);
							generateQueryDetails(queryCount);
						}
					}
				});*/

			 txtQueryCount = new TextField("Query Count");
			 txtQueryCount.setValue(setQueryValues + "");
			 txtQueryCount.setReadOnly(true);
			 txtQueryCount.setEnabled(false);

			 queryRemarksTxta = new TextArea("Query Remarks");		
			 queryRemarksTxta.setMaxLength(4000);
			 queryRemarksTxta.setWidth("350px");

			 queryFrmLayout = new FormLayout();
			 //				queryFrmLayout.addComponent(cmbQueryType);
			 queryFrmLayout.addComponent(txtQueryCount);		
			 queryFrmLayout.addComponent(queryRemarksTxta);
			 queryFrmLayout.setSpacing(true);
			 alignFormComponents();

			 if (viewAllDocsLayout != null
					 && viewAllDocsLayout.getComponentCount() > 0) {
				 viewAllDocsLayout.removeAllComponents();
			 }
			 viewAllDocsLayout = new HorizontalLayout();
			 this.viewClaimsDMSDocument = new Button("View All Documents");
			 viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);
			 viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
			 bean.getPreauthMedicalDecisionDetails().setUserClickAction("Query");
			 //				userLayout = buildUserRoleLayout();
			 HorizontalLayout hLayout = new HorizontalLayout();
			 //				hLayout.addComponents(queryFrmLayout,userLayout);
			 hLayout.addComponents(queryFrmLayout);
			 VerticalLayout vTblLayout = new VerticalLayout(
					 queryDetailsTableObj, viewAllDocsLayout ,hLayout);

			 vTblLayout.setComponentAlignment(viewAllDocsLayout, Alignment.MIDDLE_RIGHT);

			 wholeVlayout.addComponent(vTblLayout);
			 mandatoryFields = new ArrayList<Component>();
			 //				mandatoryFields.add(cmbQueryType);
			 mandatoryFields.add(queryRemarksTxta);
			 showOrHideValidation(false);

			 final ConfirmDialog dialog = new ConfirmDialog();
			 Button submitButtonWithListener = getQuerySubmitButtonWithListener(dialog);

			 HorizontalLayout btnLayout = new HorizontalLayout(
					 submitButtonWithListener, getCancelButton(dialog));
			 btnLayout.setWidth("400px");
			 btnLayout.setComponentAlignment(submitButtonWithListener,
					 Alignment.MIDDLE_CENTER);
			 showOrHideValidation(false);

			 VerticalLayout VLayout = new VerticalLayout(vTblLayout, btnLayout);
			 VLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
			 //VLayout.setWidth("800px");
			 VLayout.setMargin(true);

			 addViewAllDocsListener();
			 showInPopup(VLayout, dialog);
		 }

		 @SuppressWarnings("rawtypes")
		 private void unbindAndRemoveComponents(AbstractComponent component) {
			 for (int i = 0; i < ((FormLayout) component).getComponentCount(); i++) {
				 if (((FormLayout) component).getComponent(i) instanceof Upload) {
					 continue;
				 }
				 unbindField((AbstractField) ((FormLayout) component)
						 .getComponent(i));
			 }
			 queryFrmLayout.removeAllComponents();
			 wholeVlayout.removeComponent(queryFrmLayout);

			 if (null != wholeVlayout && 0 != wholeVlayout.getComponentCount()) {
				 Iterator<Component> componentIterator = wholeVlayout.iterator();
				 while (componentIterator.hasNext()) {
					 Component searchScrnComponent = componentIterator.next();
					 if (searchScrnComponent instanceof VerticalLayout) {
						 ((VerticalLayout) searchScrnComponent)
						 .removeAllComponents();

					 }
				 }
			 }
		 }
		 private void unbindField(Field<?> field) {
			 if (field != null) {
				 Object propertyId = this.binder.getPropertyId(field);
				 if (propertyId != null) {
					 this.binder.unbind(field);
				 }
			 }
		 }


		 public Integer setQueryValues(Long key,Long claimKey){

			 List<ViewQueryDTO> QuerytableValues = ackService.getQueryDetails(key);
			 Hospitals hospitalDetails=null;    

			 Integer count =0;

			 Integer sno = 1;

			 String diagnosisName = ackService.getDiagnosisName(key);

			 Claim claim = claimService.getClaimByClaimKey(claimKey);
			 //need to implement
			 if(claim != null){
				 Long hospitalKey = claim.getIntimation().getHospital();
				 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
			 }	
			 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
				 viewQueryDTO.setDiagnosis(diagnosisName);
				 if(hospitalDetails != null){
					 viewQueryDTO.setHospitalName(hospitalDetails.getName());
					 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
				 }

				 viewQueryDTO.setQueryRaised("");       //need to implement
				 viewQueryDTO.setQueryRaiseRole("");    //need to implement
				 viewQueryDTO.setDesignation("");    //need to implement
				 viewQueryDTO.setClaim(claim);
				 viewQueryDTO.setSno(sno);
				 if(viewQueryDTO.getQueryRaisedDate() != null){
					 viewQueryDTO.setQueryRaisedDateStr(SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate()));
				 }
				 queryDetailsTableObj.addBeanToList(viewQueryDTO);
				 sno++;

				 if(null != viewQueryDTO.getStatusId() && !ReferenceTable.PARALLEL_QUERY_CANCELLED.equals(viewQueryDTO.getStatusId())){

					 count++;
				 }
			 }	

			 return count;
		 }

		 @SuppressWarnings("deprecation")
		 private void alignFormComponents() {
			 if (queryFrmLayout != null) {
				 for (int i = 0; i < queryFrmLayout.getComponentCount(); i++) {
					 queryFrmLayout.setExpandRatio(
							 queryFrmLayout.getComponent(i), 0.5f);
				 }
			 }
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

		 private void addViewAllDocsListener()
		 {
			 if(null != viewClaimsDMSDocument)
			 {
				 if(null != viewClaimsDMSDocument)
				 {
					 viewClaimsDMSDocument.addClickListener(new Button.ClickListener() {
						 private static final long serialVersionUID = 6100598273628582002L;

						 @SuppressWarnings("deprecation")
						 public void buttonClick(ClickEvent event) {

							 BPMClientContext bpmClientContext = new BPMClientContext();
							 Map<String,String> tokenInputs = new HashMap<String, String>();
							 tokenInputs.put("intimationNo", bean.getNewIntimationDTO().getIntimationId());
							 String intimationNoToken = null;
							 try {
								 intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
							 } catch (NoSuchAlgorithmException e) {
								 // TODO Auto-generated catch block
								 e.printStackTrace();
							 } catch (ParseException e) {
								 // TODO Auto-generated catch block
								 e.printStackTrace();
							 }
							 String url = bpmClientContext.getGalaxyDMSUrl() +intimationNoToken;
							 /*Below code commented due to security reason
								String url = bpmClientContext.getGalaxyDMSUrl() +bean.getNewIntimationDTO().getIntimationId();*/
							 getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
						 }
					 });
				 }
			 }
		 }
		 private void showInPopup(Layout layout, ConfirmDialog dialog) {
			 Collection<Window> windows = UI.getCurrent().getWindows();
			 for (Window window : windows) {
				 if(window.getId() != null && window.getId().equalsIgnoreCase("duplicate_popup")){
					 window.close();
				 }
			 }
			 dialog.setCaption("");
			 dialog.setClosable(true);
			 dialog.setId("duplicate_popup");

			 Panel panel = new Panel();
			 panel.setHeight("600px");
			 panel.setWidth("1100px");
			 panel.setContent(layout);
			 dialog.setContent(panel);
			 dialog.setResizable(false);
			 dialog.setModal(true);

			 dialog.show(getUI().getCurrent(), null, true);

		 }

		 private Button getQuerySubmitButtonWithListener(final ConfirmDialog dialog) {
			 submitButton = new Button("Submit");
			 submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			 submitButton.addClickListener(new ClickListener() {
				 private static final long serialVersionUID = -5934419771562851393L;

				 @Override
				 public void buttonClick(ClickEvent event) {
					 StringBuffer eMsg = new StringBuffer();
					 if (isValidQuery()) {
						 //wrongly merged to production build
						 //IMSSUPPOR-31602 - uncommented for fix
						 bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
						 bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);

						 if(queryRemarksTxta != null){
							 bean.getPreauthMedicalDecisionDetails().setQueryRemarks(queryRemarksTxta.getValue());
						 }



						 fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_PARALLEL_QUERY,bean);
						 bean.setIsParallelInvFvrQuery(Boolean.TRUE);
						 buildQuerySuccessLayout();
						 dialog.close();
					 } else {
						 List<String> errors = getErrors();
						 for (String error : errors) {
							 eMsg.append(error);
						 }
						 showErrorPopUp(eMsg.toString());
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

		 public void buildQuerySuccessLayout() {

			 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			 buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			 HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					 .createInformationBox("Query has been initiated successfully!!!", buttonsNamewithType);
			 Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					 .toString());
			 homeButton.addClickListener(new ClickListener() {
				 private static final long serialVersionUID = 7396240433865727954L;

				 @Override
				 public void buttonClick(ClickEvent event) {
					 //dialog.close();

					 //						toolBar.countTool();As Per BA Revised Req sub flow process won't considered as count

				 }
			 });

		 }
		 public void showErrorPopUp(String emsg) {
			 HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			 buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			 GalaxyAlertBox.createWarningBox(emsg, buttonsNamewithType);
		 }

		 public boolean isValidQuery() {
			 boolean hasError = false;
			 showOrHideValidation(true);
			 errorMessages.removeAll(getErrors());

			 if(this.bean.getStatusKey() != null && this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)){
				 if(queryRemarksTxta != null && (queryRemarksTxta.getValue() == null) || (queryRemarksTxta.getValue() != null && queryRemarksTxta.getValue().isEmpty())){
					 errorMessages.add("Please Enter Query Remarks </br>");
					 hasError = true;
				 }
				 /*if(cmbQueryType.getValue() == null){
						errorMessages.add("Please Select Query Type </br>");
						hasError = true;
					}*/
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

				 } catch (CommitException e) {
					 // TODO Auto-generated catch block
					 e.printStackTrace();
				 }
			 }
			 showOrHideValidation(false);
			 return !hasError;
		 }

		 @Override
		 public void genertateFieldsBasedOnFieldVisit(
				 BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority,
				 boolean isFVRAssigned, String repName, String repContactNo) {
			 if(!isFVRAssigned){
				 Map<String, BeanItemContainer<SelectValue>> values = new HashMap<String,BeanItemContainer<SelectValue>>();
				 values.put("allocationTo", selectValueContainer);
				 values.put("fvrAssignTo", fvrAssignTo);

				 values.put("fvrPriority", fvrPriority);

				 generateFvrLayout(values);
			 }
			 else if (isFVRAssigned) {

				 Map<String, Object> values = new HashMap<String,Object>();
				 values.put("allocationTo", selectValueContainer);
				 values.put("fvrAssignTo", fvrAssignTo);

				 values.put("fvrPriority", fvrPriority);

				 values.put("isFVRAssigned", isFVRAssigned);
				 values.put("repName", repName);
				 values.put("repContactNo", repContactNo);

				 generateFvrLayout(values);
			 }

		 }

		 public void generateFvrLayout(Object dropDownValues){

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

			 this.claimRequestWizardButtons.fVRVisit(dropDownValues);

			 if(bean.getIsFvrInitiate()){
				 this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
				 bean.setInitInvDto(null);
			 }else{
				 bean.setIsFvrNotRequiredAndSelected(Boolean.FALSE);
			 }
		 }
		 
		 private void finalSubmit(){

				try{		
					if(ReferenceTable.getMedicalDecisionButtonStatus().containsKey(bean.getStatusKey())){

						proceedForFinalSubmit(); 
					}
					else
					{
						confirmationForInvestigation();
					}

				}catch(Exception e)
				{
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
			
			private void proceedForFinalSubmit(){
				fireViewEvent(PAHealthClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, this.bean);
			}
			public void confirmationForInvestigation() {			
				
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
						//dialog.close();	
						proceedForFinalSubmit();
					}
				});
				
				cancelButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;
			
					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						bean.setIsInvestigation(false);
						wizard.getFinishButton().setEnabled(true);
					}
				});
			}
			
}
