package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.event.GWizardEvent;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardInitEvent;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.ViewSeriousDeficiencyUI;
import com.shaic.claim.bpc.ViewBusinessProfileChart;
import com.shaic.claim.cashlessprocess.processicac.search.ProcessICACService;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.preauth.view.ViewPreviousClaimsTable;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.UpdateOtherClaimDetailDTO;
import com.shaic.claim.preauth.wizard.view.InitiateInvestigationDTO;
import com.shaic.claim.preauth.wizard.view.ParallelInvestigationDetails;
import com.shaic.claim.premedical.listenerTables.UpdateOtherClaimDetailsUI;
import com.shaic.claim.registration.BasePolicyPreviousClaimWindowUI;
import com.shaic.claim.registration.ViewBasePolicyClaimsWindowOpen;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPageViewImpl;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestButtonsForWizard;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionButtons;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPageViewImpl;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing.ClaimRequestPremedicalProcessingPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing.ClaimRequestPremedicalProcessingViewImpl;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.previousclaims.ClaimRequestPreviousClaimsPageViewImpl;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.rod.wizard.tables.ReconsiderRODRequestListenerTable;
import com.shaic.claim.scoring.HospitalScoringView;
import com.shaic.claim.scoring.ppcoding.PPCodingDTO;
import com.shaic.claim.scoring.ppcoding.ScoringAndPPTabUI;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.claim.viewEarlierRodDetails.EsclateClaimToRawPage;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableBancs;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.RevisedPreauthViewPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPaayasPolicyDetailsPdfPage;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ZUATopViewQueryTable;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;

public class ClaimRequestWizardViewImpl extends AbstractMVPView implements
ClaimRequestWizard {
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
	private Instance<ClaimRequestDataExtractionPageViewImpl> dataExtractionViewImpl;
	
	@Inject
	private Instance<ClaimRequestMedicalDecisionPageViewImpl> medicalDecisionViewImpl;
	
	@Inject
	private Instance<ClaimRequestPreviousClaimsPageViewImpl> previousClaimViewImpl;
	
	@Inject
	private Instance<ClaimRequestPremedicalProcessingViewImpl> premedicalprocessingViewImpl;
	
	private ClaimRequestDataExtractionPageViewImpl dataExtractionViewImplObj;
	
	private ClaimRequestPremedicalProcessingViewImpl premedicalprocessingViewImplObj;
	
	private ClaimRequestPreviousClaimsPageViewImpl previousClaimViewImplObj;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	private ClaimRequestMedicalDecisionPageViewImpl medicalDecisionViewImplObj;

	private VerticalLayout dummyLayout;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	@Inject
	private ReconsiderRODRequestListenerTable reconsiderRequestDetails;
	
	@Inject
	private UpdateOtherClaimDetailsUI updateOtherClaimDetailsUI;
	
	private ComboBox cmbReasonForReconsideration;
	 
	private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	
	private ComboBox cmbReconsiderationRequest;
	
	private BeanItemContainer<SelectValue> reconsiderationRequest;
	
	private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	
	@Inject		
	private ViewSeriousDeficiencyUI revisedViewSeriousDeficiencyTable;
	
	@Inject		
	private ViewBusinessProfileChart viewBusinessProfileChart;	
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	public Button btnViewRTABalanceSI;
	
	@Inject
	private ZUAViewQueryHistoryTable zuaViewQueryHistoryTable;
	 
	@EJB
	private PolicyService policyService;
	 
	@Inject
	private ZUATopViewQueryTable zuaTopViewQueryTable;
	
	private final Logger log = LoggerFactory.getLogger(ClaimRequestWizardViewImpl.class);
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	
	@Inject
	private ParallelInvestigationDetails viewInvestigationDetails;
	
	@Inject
	private Instance<ClaimRequestButtonsForWizard> claimRequestWizardButtonInstance;
	
	private ClaimRequestButtonsForWizard claimRequestWizardButtons;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;
	
	private TextField txtQueryCount;
	private ComboBox cmbQueryType;
	
	private TextArea queryRemarksTxta;
	
	private FormLayout dynamicFrmLayout;
	
	private HorizontalLayout viewAllDocsLayout = null;
	
	private Button viewClaimsDMSDocument;
	
	private ArrayList<Component> mandatoryFields;
	
	private Button submitButton;

	public Button cancelButton;
	
	private List<String> errorMessages;
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackService;
	
	private VerticalLayout wholeVlayout;	
	
	@Inject
	private ViewPaayasPolicyDetailsPdfPage pdfPageUI;
	
	@EJB
	private ReimbursementService reimbService;
	
	@Inject
	private HospitalScoringView hospitalScoringView;
	
	private Button btnHospitalScroing;
	private Button seriousDeficiency;
	private ComboBoxMultiselect cmbUserRoleMulti;
	private ComboBoxMultiselect cmbDoctorNameMulti;
	private TextArea remarksFromDeptHead;	
	@EJB
	private DBCalculationService dbCalculationService;
	
	FormLayout userLayout = new FormLayout();
	
	@Inject
	private Instance<EsclateClaimToRawPage> esclateClaimToRawPageInstance;
	
	private EsclateClaimToRawPage esclateClaimToRawPageViewObj;
	
	@Inject
	private Toolbar toolBar;
	
	@Inject
	private ZUAViewQueryHistoryTableBancs zuaTopViewQueryTableBancs;

	@EJB
	private ProcessICACService processICACService;
	
	private Button viewLinkPolicyDtls;
	
	private NativeSelect viewBasePolicyDetailsSelect;
	private Button btnGo;
	
	private EnhancedBrowserWindowOpener sopener;
	
	private static final String VIEW_BASE_POLICY_CLAIMS = "Policy Claims Details";

	@Inject
	private ViewPreviousClaimsTable preauthPreviousClaimsTable;
	@Inject
	private Instance<ViewBasePolicyClaimsWindowOpen> ViewBasePolicyClaimsWindowOpen;
	// R1207
		private Map<String, String> roleValidationContainer = new HashMap<String, String>();
		private Map<String, String> userValidationContainer = new HashMap<String, String>();

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
		
		@Inject
		private CreateRODService createRodService;

	//CR2019017 - Start
	@Inject
	private Instance<ClaimRequestMedicalDecisionButtons> claimRequestButtonInstance;

	private ClaimRequestMedicalDecisionButtons claimRequestButtonObj;
	//CR2019017 - End
	
	@Inject
	private ScoringAndPPTabUI scoringAndPPTabUI;
	
	private TextArea autoAllocCancelRemarks;
	
	private void initBinder() {
		
		wizard.getFinishButton().setCaption("Submit");
		this.binder = new FieldGroup();
		BeanItem<PreauthDTO> item = new BeanItem<PreauthDTO>(bean);
		item.addNestedProperty("preauthDataExtractionDetails");
		item.addNestedProperty("coordinatorDetails");
		item.addNestedProperty("preauthPreviousClaimsDetails");

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
		item.addNestedProperty("preauthMedicalDecisionDetails.fvrNotRequiredRemarks");

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
		
		
		TextArea remarksBillEntry = new TextArea("Remarks(Bill Entry)");
		remarksBillEntry.setEnabled(false);
		remarksBillEntry.setValue(this.bean.getPreauthDataExtractionDetails().getRemarksBillEntry());
		remarksBillEntry.setNullRepresentation("");
		
		VerticalLayout layout = new VerticalLayout(new FormLayout(status, remarks,remarksBillEntry) );
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
//		List<SelectValue> reconsiderValues = new ArrayList<SelectValue>();
//		SelectValue selValue = new SelectValue();
//		if(null  != this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId())
//		{
//			reasonForReconsideration = this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationReasonId().getValue();
//			selValue.setId(1l);
//			selValue.setValue(reasonForReconsideration);
//		}
//		reconsiderValues.add(selValue);
		
		BeanItemContainer<SelectValue> reconsiderBeanContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
//		reconsiderBeanContainer.addAll(reconsiderValues);
		
		reconsiderBeanContainer = masterService.getSelectValueContainer(ReferenceTable.REASON_FOR_RECONSIDERATION);		
		
		cmbReasonForReconsideration.setContainerDataSource(reconsiderBeanContainer);
		cmbReasonForReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonForReconsideration.setItemCaptionPropertyId("value");
		cmbReasonForReconsideration.setWidth("285px");
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
		
		reconsiderationLayout = new VerticalLayout();
		FormLayout reconReqFrmLayout = new FormLayout(cmbReconsiderationRequest);
		
		if(("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getReconsiderationRequest()))
		{
			reconReqFrmLayout.addComponent(cmbReasonForReconsideration);
			HorizontalLayout reconLayout = new HorizontalLayout(reconReqFrmLayout);
			reconsiderationLayout.addComponents(reconLayout/*,vLayout*/);
		}
		else
		{
			reconsiderationLayout.addComponents(reconReqFrmLayout);
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
//		this.wizard = iWizard.get();
		this.wizard = new IWizardPartialComplete();
		dynamicFrmLayout = new FormLayout();		
		dynamicFrmLayout.setHeight("100%");
		dynamicFrmLayout.setWidth("100%");
		dynamicFrmLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);
		wholeVlayout.addComponent(dynamicFrmLayout);
		initBinder();
		
		claimRequestWizardButtons = claimRequestWizardButtonInstance.get();
		claimRequestWizardButtons.initView(this.bean, wizard);
		
		dataExtractionViewImplObj = dataExtractionViewImpl.get();
		dataExtractionViewImplObj.init(this.bean , wizard);
		wizard.addStep(dataExtractionViewImplObj,"Claim Request Data Extration");
		
		previousClaimViewImplObj = previousClaimViewImpl.get();
		previousClaimViewImplObj.init(this.bean,this.wizard);
		wizard.addStep(previousClaimViewImplObj,"Previous Claim");
		
		if(! ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			premedicalprocessingViewImplObj = premedicalprocessingViewImpl.get();
			premedicalprocessingViewImplObj.init(this.bean,this.wizard);
			wizard.addStep(premedicalprocessingViewImplObj,"premedical process");
		}
		
		medicalDecisionViewImplObj = medicalDecisionViewImpl.get();
		medicalDecisionViewImplObj.init(this.bean,this.wizard);
		wizard.addStep(medicalDecisionViewImplObj,"Medical Decision");
		
		
		HorizontalLayout buttonsHLayout = new HorizontalLayout(
				claimRequestWizardButtons);
		
		wizard.setStyleName(ValoTheme.PANEL_WELL);
		wizard.setSizeFull();
		wizard.addListener(this);
		TextField typeFld = new TextField("Type");
		typeFld.setValue(SHAUtils.getTypeBasedOnStatusId(this.bean
				.getStatusKey()));
		typeFld.setNullRepresentation("");
		typeFld.setReadOnly(true);
		VerticalLayout commonvalues=commonValues();
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
		
		//VerticalLayout vLayout= new VerticalLayout(hLayout,commonvalues);
		
//		VerticalLayout vLayout = null;
		
		HorizontalLayout vLayout = new HorizontalLayout();
		
		if((this.bean.getInsuredPedDetails() != null && ! this.bean.getInsuredPedDetails().isEmpty()) || (this.bean.getApprovedPedDetails() != null && !this.bean.getApprovedPedDetails().isEmpty())){
				
//				Panel insuredPedDetailsPanel = getInsuredPedDetailsPanel();
//				if(insuredPedDetailsPanel != null){
//					insuredPedDetailsPanel.setWidth("30%");
//				}
				
				vLayout.addComponents(hLayout,commonvalues);
				vLayout.setSpacing(true);
				vLayout.setComponentAlignment(commonvalues, Alignment.TOP_LEFT);
//				if(insuredPedDetailsPanel != null){
//					vLayout.addComponent(insuredPedDetailsPanel);
//					vLayout.setComponentAlignment(insuredPedDetailsPanel, Alignment.BOTTOM_RIGHT);
//				}

				vLayout.addComponent(getInsuredPedDetailsPanel());
				
		}else{
				
				vLayout.addComponents(hLayout,commonvalues);
				vLayout.setSpacing(true);
				vLayout.setComponentAlignment(commonvalues, Alignment.TOP_LEFT);
		}
		
//		HorizontalLayout crmFlaggedLayout = SHAUtils.crmFlaggedLayout(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());		
		crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
		//vLayout.addComponent(crmFlaggedLayout);
		
		//FormLayout fLayout = new FormLayout();
		VerticalLayout fLayout = new VerticalLayout();
		//fLayout.setComponentAlignment(commonvalues, Alignment.BOTTOM_LEFT);
		Label dummyLabel = new Label("");
		dummyLabel.setWidth("50px");
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		
		String panelName = "Process Claim Request";
//		intimationDetailsCarousel.init(this.bean.getNewIntimationDTO(), this.bean.getClaimDTO(),  "Process Claim Request");
		if(null != bean.getScreenName() && (bean.getScreenName().equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
			panelName = "Waiting For Input";
		}
		intimationDetailsCarousel.init(this.bean, panelName );
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		viewDetails.initView(bean.getNewIntimationDTO().getIntimationId(), bean.getKey(),ViewLevels.PREAUTH_MEDICAL, false,"Process Claim Request");
		
		HorizontalLayout hTemLayout =new HorizontalLayout(commonButtonsLayout());
		hTemLayout.setSpacing(false);
		HorizontalLayout horizontal = new HorizontalLayout(hTemLayout,viewDetails);
		horizontal.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		horizontal.setWidth("100%");
		horizontal.setExpandRatio(hTemLayout, 7);
		horizontal.setExpandRatio(viewDetails, 3);
		
		HorizontalLayout dupLayout = new HorizontalLayout();
		VerticalLayout wizardLayout1 = new VerticalLayout(horizontal,dupLayout);
		
		 viewAllDocsLayout = new HorizontalLayout();
		
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
	    	fLayout.addComponent(getZonlaReviewDetails());
	    	fLayout.setSpacing(true);
		}
		//HorizontalLayout mainHorizantal = new HorizontalLayout(wizardLayout1,vLayout);
		
		this.viewClaimsDMSDocument = new Button("View All Documents");
		viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);
		
		wizardLayout1.setHeight("80px");
		VerticalLayout mainVertical = new VerticalLayout(wizardLayout1,vLayout,crmFlaggedComponents);
		mainVertical.setComponentAlignment(crmFlaggedComponents, Alignment.BOTTOM_RIGHT);
		
		if(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && ("Y").equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getReconsiderationFlag())){
			
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
			
			
//			HorizontalLayout vLayout = new HorizontalLayout(reconsiderRequestDetails);
//			vLayout.setMargin(true);
//			vLayout.setCaption("Earlier Request Reconsidered");
			mainVertical.addComponent(reconsiderRequestDetails);
			
//			if(this.bean.getInsuredPedDetails() != null && ! this.bean.getInsuredPedDetails().isEmpty()){
//				Panel insuredPedDetailsPanel = getInsuredPedDetailsPanel();
//				if(insuredPedDetailsPanel != null){
//					insuredPedDetailsPanel.setWidth("30%");
//				}
//				
//				if(insuredPedDetailsPanel != null){
//					mainHorizantal.addComponent(insuredPedDetailsPanel);
//					mainHorizantal.setComponentAlignment(insuredPedDetailsPanel, Alignment.MIDDLE_CENTER);
//				}
//				mainVertical.addComponent(getInsuredPedDetailsPanel());
//			}			
			
			
		}
		mainVertical.addComponent(fLayout);
		mainVertical.setSpacing(true);
		wizardLayout1.setSpacing(true);
		vLayout.setSpacing(true);
		//vLayout.setHeight("");
		HorizontalLayout dupLayout1 = new HorizontalLayout();
		dupLayout1.setHeight("54px");
		HorizontalLayout hTempLayout = new HorizontalLayout(fLayout,crmFlaggedComponents);
		hTempLayout.setComponentAlignment(crmFlaggedComponents, Alignment.BOTTOM_RIGHT);
		hTempLayout.setSpacing(true);
		VerticalLayout mainHorizantal = new VerticalLayout(wizardLayout1,dupLayout1,vLayout/*,crmFlaggedLayout,fLayout*/,hTempLayout,buttonsHLayout);
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
		
		if(bean.getTaskNumber() != null){/*
			String aciquireByUserId = SHAUtils.getAciquireByUserId(bean.getTaskNumber());
			if((bean.getStrUserName() != null && aciquireByUserId != null && ! bean.getStrUserName().equalsIgnoreCase(aciquireByUserId)) || aciquireByUserId == null){
				compareWithUserId(aciquireByUserId);
			}
		*/}
		
		//CR2019017 - Start
		claimRequestButtonObj = claimRequestButtonInstance.get();
		claimRequestButtonObj.initView(this.bean, wizard, null);
		if(bean.getIsSDEnabled() && claimRequestButtonObj.getApproveBtn().isEnabled()){
			claimRequestButtonObj.getApproveBtn().setEnabled(false);
		}/*else{
			claimRequestButtonObj.getApproveBtn().setEnabled(true);
		}*/
		//CR2019179
		if(bean.getIsSDEnabled() && claimRequestWizardButtons.getQueryBtn().isEnabled()){
			claimRequestWizardButtons.getQueryBtn().setEnabled(false);
		} 
		bean.setClaimRequestButtonsWizard(claimRequestWizardButtons);
		//CR2019179 End
		bean.setClaimReqButtonObj(claimRequestButtonObj);
		//CR2019017 - End
		
		//CR2019056
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getInvPendingFlag())) {
			claimRequestButtonObj.getApproveBtn().setEnabled(false);
		}
	} 
	
	private Table getInsuredPedDetailsPanel(){
		
		Table table = new Table();
		table.setWidth("100%");
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
		table.setColumnWidth("pedCode", 80);
		table.setColumnWidth("pedDescription", 320);
		table.setColumnWidth("pedEffectiveFromDate", 200);
		table.setWidth("602px");
////		if(i>0){
//			Panel tablePanel = new Panel(table);
//			return tablePanel;
////		}
////		return null;
		
		return table;
	}
	
	
	private VerticalLayout commonButtonsLayout()
	{
		/*TextField acknowledgementNumber = new TextField("Acknowledgement Number");
//		acknowledgementNumber.setValue(String.valueOf(this.bean.getAcknowledgementNumber()));
		//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
		acknowledgementNumber.setWidth("250px");
		acknowledgementNumber.setHeight("20px");
		acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		acknowledgementNumber.setReadOnly(true);
		acknowledgementNumber.setEnabled(false);
		acknowledgementNumber.setNullRepresentation("");*/
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
		Label activityAgeing = new Label();
		Label claimAgeing = new Label();
		if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(SHAConstants.DOC_RECIEVED_FROM_INSURED_ID))
		{
			Long wkKey = 0l;
			if(null != bean.getScreenName() && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getScreenName())))
			{
				wkKey = dbCalculationService.getWorkflowKey(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.WAIT_FOR_INPUT_CURRENT_QUEUE);
			}
			else{
				wkKey = dbCalculationService.getWorkflowKey(bean.getNewIntimationDTO().getIntimationId(),SHAConstants.MA_CURRENT_QUEUE);
			}
			Map<String, Integer> ageingValues = dbCalculationService.getClaimAndActivityAge(wkKey,bean.getClaimDTO().getKey(),(bean.getKey()).toString());
			Integer activityAge = ageingValues.get(SHAConstants.LN_ACTIVITY_AGEING);
			Integer claimAge = ageingValues.get(SHAConstants.LN_CLAIM_AGEING);
			
			activityAgeing.setDescription("Activity Ageing");
			if(activityAge != null)
			{
				if(activityAge >= 4 && activityAge <= 7)
				{
					activityAgeing.setIcon(new ThemeResource("images/activity_productivity_Y.png"));
				}
				else if(activityAge > 7)
				{
					activityAgeing.setIcon(new ThemeResource("images/activity_productivity_R.png"));
				}
				else{
					activityAgeing.setIcon(new ThemeResource("images/activity_productivity.png"));
				}
			}

			claimAgeing.setDescription("Claim Ageing");
			if(claimAge != null)
			{
				if(claimAge >= 4 && claimAge <= 7)
				{
					claimAgeing.setIcon(new ThemeResource("images/Claim_ageing_Y.png"));
				}
				else if(claimAge > 7)
				{
					claimAgeing.setIcon(new ThemeResource("images/Claim_ageing_R.png"));
				}
				else{
					claimAgeing.setIcon(new ThemeResource("images/Claim_ageing.png"));
				}
			}
		}
		
		HorizontalLayout hoapitalScore = SHAUtils.hospitalScore(bean,hospitalService);
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean);
		
		// GXL2021044
     	HorizontalLayout HsTrafficIcon = SHAUtils.HsTraficImages(bean);
		HorizontalLayout crmLayout = new HorizontalLayout(claimCount,formLayout,activityAgeing,claimAgeing,hoapitalScore,hopitalFlag,HsTrafficIcon);
		crmLayout.setSpacing(true);
		crmLayout.setWidth("40%");
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
		
		Button btnInsuredChannelName = new Button("Insured/Channel Name");
		btnInsuredChannelName.setStyleName(ValoTheme.BUTTON_DANGER);
		btnInsuredChannelName.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				HorizontalLayout insuredChannelName= SHAUtils.getInsuredChannedName(bean);
				bean.setIsInsuredChannedNameClicked(true);
				Button button = (Button)event.getSource();
				button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
			
			}
		});		
			
		icrAgentBranch.addComponent(btnInsuredChannelName);
		VerticalLayout icrAGBR = new VerticalLayout(crmLayout,icrAgentBranch);
		
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
		
		Button btnLumen = new Button("Initiate Lumen");
		btnLumen.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				invokePreMedicalLumenRequest();
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
		
		//FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);
		
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
	
		Button btnViewPolicySchedule = new Button("View Policy Schedule");
		if (!ReferenceTable.getGMCProductList().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			btnViewPolicySchedule.setStyleName(ValoTheme.BUTTON_DANGER);
		}
		else
		{
			bean.setIsScheduleClicked(true);
		}
		
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
		
		Button btnBusinessProfileChart = new Button("View Mini Business Profile");
		btnBusinessProfileChart.setStyleName(ValoTheme.BUTTON_DANGER);
		btnBusinessProfileChart.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				viewBusinessProfileChart.init(bean);
				
				bean.setIsBusinessProfileClicked(true);
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
		
		/***
		 * new button added bellow for GLX2020065 
		 */
		Button btnPolicyScheduleWithoutRisk = new Button("Policy Schedule without Risk");
		if(ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			btnPolicyScheduleWithoutRisk.setEnabled(true);
		}else{
			btnPolicyScheduleWithoutRisk.setEnabled(false);
		}
		
		btnPolicyScheduleWithoutRisk.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				viewDetails.getViewPolicyScheduleWithoutRisk(bean.getNewIntimationDTO().getIntimationId());
				Button button = (Button)event.getSource();
			
				
			}
		});
		
		btnHospitalScroing = new Button("Hospital Scoring/PP Coding");
		if(bean.getScoringClicked()){
			btnHospitalScroing.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		}else{
			if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag()){
				btnHospitalScroing.setStyleName(ValoTheme.BUTTON_DANGER);
			}else{
				bean.setScoringClicked(true);
				btnHospitalScroing.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				btnHospitalScroing.setVisible(false);
			}
		}
		
		seriousDeficiency = new Button("Serious Deficiency");
		seriousDeficiency.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				revisedViewSeriousDeficiencyTable.init(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("Serious Deficiency Intimation List");
				popup.setWidth("35%");
				popup.setHeight("75%");
				popup.setContent(revisedViewSeriousDeficiencyTable);
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
		
		btnHospitalScroing.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				showScoringView();				
			}
		});
		
		/*if(bean.getNewIntimationDTO().getHospitalDto().getHospitalType().getId() == ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
			bean.setScoringVisibility(true);
		}else{
			bean.setScoringVisibility(false);
		}*/
		
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
		
		Button elearnBtn = viewDetails.getElearnButton();
		
		Button updatePreviousButton = new Button("Update Previous/Other Claim Details(Defined Limit)");
		
		updatePreviousButton.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(ClaimRequestWizardPresenter.UPDATE_PREVIOUS_CLAIM_DETAILS, bean);
			}
		});
		
		TextField icrEarnedPremium = new TextField("ICR(Earned Premium %)");
		icrEarnedPremium.setReadOnly(Boolean.FALSE);
		if(null != bean.getNewIntimationDTO().getIcrEarnedPremium()){
			icrEarnedPremium.setValue(bean.getNewIntimationDTO().getIcrEarnedPremium());
		}
		icrEarnedPremium.setReadOnly(Boolean.TRUE);
		FormLayout icrLayout = new FormLayout(icrEarnedPremium);
		icrLayout.setMargin(false);
		icrLayout.setSpacing(true);
		
		HorizontalLayout secondLayout1 = null;
		HorizontalLayout buttonsLayout = null;
		Button viewLinkedPolicy = viewDetails.getLinkedPolicyDetails(bean);
		

		Product product =bean.getNewIntimationDTO().getPolicy().getProduct();
		
		Button btnEsclateToRAW = new Button("Escalate To Raw");
		btnEsclateToRAW.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				SHAUtils.buildEsclateToRawView(esclateClaimToRawPageInstance,esclateClaimToRawPageViewObj,bean,SHAConstants.CLAIM_REQUEST);
			}
		});
		
		//Parralel ICAC Process
		Button processICACBtn = new Button("Refer to ICAC");

		processICACBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				List<String> sourceList = new ArrayList<String>();
				sourceList.add(SHAConstants.MA);
				Boolean allowed  = processICACService.getTocheckIcacreqValidOrNot(bean.getNewIntimationDTO().getIntimationId(),sourceList);

						if(allowed){
							submitIcacProcess();
						}
						else{
							SHAUtils.showMessageBoxWithCaption(SHAConstants.ICAC_PROCESS_ALRDY_INIT,"Information");
						}
			}

		});
		
		//GLX2020162 topup policy creation for HC policy
		viewLinkPolicyDtls = new Button("View Linked Policy");
		Policy linkPolicyKey = null;
		if((bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo() != null && 
				!bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo().isEmpty()) ||(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null && 
				!bean.getNewIntimationDTO().getPolicy().getBasePolicyNo().isEmpty())){
			if(bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo() != null && 
					!bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo().isEmpty()){
				linkPolicyKey = policyService.getKeyByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getTopupPolicyNo());
			}else if(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo() != null && 
					!bean.getNewIntimationDTO().getPolicy().getBasePolicyNo().isEmpty()){
				linkPolicyKey = policyService.getKeyByPolicyNumber(bean.getNewIntimationDTO().getPolicy().getBasePolicyNo());
			}
			viewLinkPolicyDtls.setVisible(true);
		}else {
			viewLinkPolicyDtls.setVisible(false);
		}
		if(linkPolicyKey != null && linkPolicyKey.getKey() != null){
			final ShortcutListener sListener = callSListener();
			final EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener(BasePolicyPreviousClaimWindowUI.class);
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.CLAIM_DETAILS,linkPolicyKey.getKey());					
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS,ViewBasePolicyClaimsWindowOpen);
			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.VIEW_PREVIOUS_CLAIMS_TABLE,preauthPreviousClaimsTable);
			opener.popupBlockerWorkaround(true);

			opener.withShortcut(sListener);
			opener.setFeatures("height=700,width=1300,resizable");
			opener.doExtend(viewLinkPolicyDtls);
			//					setSopener(opener);
			viewLinkPolicyDtls.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					opener.open();
				}
			});
      }	
		if((bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
				|| /*(ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY).equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
					(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
								|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
						&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
				|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) || 
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
			dataExtractionViewImplObj.setViewRTAButton(btnViewRTABalanceSI);
			buttonsLayout = new HorizontalLayout(btnRRC,btnLumen,btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewLinkPolicyDtls,btnViewDoctorRemarks,elearnBtn,viewEarlierRODDetails,btnViewRTABalanceSI,btnZUAAlert,btnViewPackage,viewPreauthButton);
			secondLayout1 = new HorizontalLayout(buttonsLayout);

		}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
				this.bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && this.bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			buttonsLayout = new HorizontalLayout(btnRRC,btnLumen,btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewLinkPolicyDtls,btnViewDoctorRemarks,elearnBtn,viewEarlierRODDetails,updatePreviousButton,btnZUAAlert,btnViewPackage,viewPreauthButton);
			secondLayout1 = new HorizontalLayout(buttonsLayout);

		}
		else if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
		{
			HorizontalLayout hTempLayout1 = new HorizontalLayout(btnRRC,btnLumen,btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,btnViewDoctorRemarks,elearnBtn,viewEarlierRODDetails,btnViewRTABalanceSI,btnZUAAlert,btnViewPackage);

			HorizontalLayout hTempLayout2 = new HorizontalLayout(viewPreauthButton,btnEsclateToRAW,icrLayout);

			if(bean.getClaimKey() != null){
				List<Preauth> preauthByClaimnKey = preauthService.getPreauthByClaimnKey(bean.getClaimKey());
				if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()){
					for(Preauth preauthDto : preauthByClaimnKey){
						if(preauthDto != null){
							if(preauthDto.getStatus().getKey() != null && ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS.equals(preauthDto.getStatus().getKey())){
								hTempLayout1.addComponent(btnviewCashlessDocument, 2);
								break;
							}
						}
					}
				}
				
			}
			hTempLayout1.setMargin(false);
			hTempLayout1.setSpacing(true);
			VerticalLayout vTempLayout = null;
			if(bean.getPolicyDto().getGmcPolicyType() != null &&
					(bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT) ||
							bean.getPolicyDto().getGmcPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
				HorizontalLayout linkedLayot = new HorizontalLayout(viewLinkedPolicy);
				linkedLayot.setMargin(false);
				linkedLayot.setSpacing(true);
				 vTempLayout =new VerticalLayout(hTempLayout1,hTempLayout2,linkedLayot);
			} else {
				vTempLayout =new VerticalLayout(hTempLayout1,hTempLayout2);
			}
			vTempLayout.setMargin(false);
			vTempLayout.setSpacing(true);
			secondLayout1 = new HorizontalLayout(vTempLayout);
		}
		else
		{
			buttonsLayout = new HorizontalLayout(btnRRC,btnLumen,btnViewPolicySchedule,btnBusinessProfileChart,btnPolicyScheduleWithoutRisk,btnHospitalScroing,seriousDeficiency,viewLinkPolicyDtls,btnViewDoctorRemarks,elearnBtn,viewEarlierRODDetails,btnViewRTABalanceSI,btnZUAAlert,btnViewPackage,viewPreauthButton);
			VerticalLayout icrAGBRlayout = new VerticalLayout(buttonsLayout);
			secondLayout1 = new HorizontalLayout(icrAGBRlayout);
		}
		
		if(!(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			if(bean.getClaimKey() != null){
				List<Preauth> preauthByClaimnKey = preauthService.getPreauthByClaimnKey(bean.getClaimKey());
				if(preauthByClaimnKey != null && !preauthByClaimnKey.isEmpty()){
					for(Preauth preauthDto : preauthByClaimnKey){
						if(preauthDto != null){
							if(buttonsLayout != null && preauthDto.getStatus().getKey() != null && ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS.equals(preauthDto.getStatus().getKey())){
								//cashless.addComponent(btnviewCashlessDocument);
								buttonsLayout.addComponent(btnviewCashlessDocument, 2);
								break;
							}
						}
					}
				}
				
			}
		}
		HorizontalLayout secondLayout2 = new HorizontalLayout();
		TextField dummytxt = new TextField();
		dummytxt.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		dummytxt.setEnabled(false);
		VerticalLayout verticaldummy = new VerticalLayout(dummytxt,processICACBtn);
		secondLayout2.addComponent(verticaldummy);
		secondLayout2.setSpacing(true);
		
		secondLayout1.setSpacing(true);

		//HorizontalLayout dummyhlayout = new HorizontalLayout();
		HorizontalLayout crmLayout1 = new HorizontalLayout(crmLayout,btnEsclateToRAW);
		btnEsclateToRAW.setEnabled(Boolean.FALSE);
		crmLayout1.setSpacing(true);
		VerticalLayout verticalLayout1 = new VerticalLayout(crmLayout1,icrAgentBranch,secondLayout1,secondLayout2);
		verticalLayout1.setSpacing(true);
		verticalLayout1.setHeight("100px");
		verticalLayout1.setHeight("125px");
		VerticalLayout verticalLayout = new VerticalLayout(verticalLayout1/*, horizontalLayout*/);
		verticalLayout.setSpacing(true);
		verticalLayout.setHeight("5px");
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
		
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createInformationBox(eMsg, buttonsNamewithType);
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
		
		if(isInsured() 
				&&(bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
						|| bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS))){

			MessageBox getContRRC = MessageBox
					.createQuestion()
					.withCaptionCust("Confirmation")
					.withMessage("Contribute RRC ?")
					.withYesButton(ButtonOption.caption("Yes"))
					.withNoButton(ButtonOption.caption("No"))
					.open();
			
			Button yesBtn =getContRRC.getButton(ButtonType.YES);
			yesBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					getContRRC.close();
					finalSubmit();
					validateUserForRRCRequestIntiation();
				}
			});	
			Button noBtn = getContRRC.getButton(ButtonType.NO);
			noBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					getContRRC.close();
					finalSubmit();
				}
			});
		}else{
			finalSubmit();
		}
	}
	
	private void finalSubmit(){
		
		try{
			if(bean.getPreauthHoldStatusKey() != null && ReferenceTable.PREAUTH_HOLD_STATUS_KEY.equals(bean.getPreauthHoldStatusKey())){
				fireViewEvent(ClaimRequestWizardPresenter.PROCESS_CLAIM_REQUEST_HOLD_SUBMIT, this.bean);
			}
			else {
			
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
			
		if(null != this.bean.getStatusKey() && this.bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) && bean != null && bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct() != null && (ReferenceTable.getSeniorCitizenKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			if(bean.getHighestCopay() != null && bean.getHighestCopay().equals(0.0d)){
				warningMessageForCopay();
			}else if(bean.getHighestCopay() != null && ! bean.getHighestCopay().equals(0.0d)){
				alertMessageForCopay();
			}else{
				Boolean alertMessageForProvisionValidation = SHAUtils.alertMessageForProvisionValidation(bean.getStageKey(), bean.getStatusKey(), this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().longValue():0l, 
						bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey(), bean.getKey(), "C",getUI());
				if(! alertMessageForProvisionValidation){
					proceedForFinalSubmit();  // For R1136
				}
			}
		}else{
						
			if(ReferenceTable.getMedicalDecisionButtonStatus().containsKey(bean.getStatusKey())){
				
//				fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, this.bean);  // For R1136
				
	
				Boolean alertMessageForProvisionValidation = SHAUtils.alertMessageForProvisionValidation(bean.getStageKey(), bean.getStatusKey(), this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().longValue():0l, 
						bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey(), bean.getKey(), "C",getUI());
				if(! alertMessageForProvisionValidation){
					proceedForFinalSubmit();  // For R1136
				}

			}
			else
			{
				confirmationForInvestigation();
			}
		}
		}
		}catch(Exception e)
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void wizardCancelled(WizardCancelledEvent event) {
		/*ConfirmDialog dialog = ConfirmDialog
				.show(getUI(),
						"Confirmation",
						"Are you sure you want to cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									// Confirmed to continue
									releaseHumanTask();
									SHAUtils.setClearPreauthDTO(bean);
									if(null != bean.getScreenName() && !(SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getScreenName())))
									{
										fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST,
												bean.getSearchFormDTO());
									}
									else
									{
										fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT,
												bean.getSearchFormDTO());
									}
									clearObject();
									VaadinRequest currentRequest = VaadinService.getCurrentRequest();
									SHAUtils.clearSessionObject(currentRequest);
									
								    
								} else {
									// User did not confirm
								}
								
							}

						});
						dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);*/

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

							// Confirmed to continue
							if(null != bean.getScreenName() && !(SHAConstants.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION.equals(bean.getScreenName())))
							{
							releaseHumanTask();
							SHAUtils.setClearPreauthDTO(bean);
							}
							
							if(null != bean.getScreenName() && (SHAConstants.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION.equals(bean.getScreenName())))
							{
								autoAllocationCancelRemarksLayout();
							}
							else if(null != bean.getScreenName() && !(SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getScreenName())))
							{
								fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST,
										bean.getSearchFormDTO());
							}
							else
							{
								fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT,
										bean.getSearchFormDTO());
							}
							clearObject();
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

	@Override
	public void buildSuccessLayout() {
	/*	Label successLabel = new Label(
				"<b style = 'color: green;'> Claim has been processed successfully !!!</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Home");
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
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Home");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(" Claim has been processed successfully !!!</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
               toolBar.countTool();
               if(null != bean.getScreenName() && (SHAConstants.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION.equals(bean.getScreenName())))
				{
            	   fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION,null);
				}
               else if(null != bean.getScreenName() && (bean.getScreenName().equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, bean.getSearchFormDTO());
				}else{
					fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST, bean.getSearchFormDTO());
				}
				
				SHAUtils.setClearPreauthDTO(bean);
				clearObject();
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
		if(bean.getStatusKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)){
			if(medicalDecisionViewImplObj !=null){
				bean.getRrcDTO().getQuantumReductionDetailsDTO().setSettlementAmount(medicalDecisionViewImplObj.getTotalApprovedAmnt());
			}
		}else{
			bean.getRrcDTO().getQuantumReductionDetailsDTO().setSettlementAmount(0L);
		}
		fireViewEvent(ClaimRequestWizardPresenter.VALIDATE_CLAIM_REQUEST_USER_RRC_REQUEST, bean);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
			if (!isValid) {
				/*Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Alert");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.CLAIM_REQUEST);
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
		List<Long> listOfSettledStatus = new ArrayList<Long>();
		/*listOfSettledStatus.add(ReferenceTable.FINANCIAL_SETTLED);
		listOfSettledStatus.add(ReferenceTable.PAYMENT_SETTLED);
		listOfSettledStatus.add(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS);*/
		if(!listOfSettledStatus.contains(bean.getClaimDTO().getStatusId())){
			fireViewEvent(ClaimRequestWizardPresenter.CLAIM_LUMEN_REQUEST, bean);
		}else{
			showErrorMessage("Claim is settled, lumen cannot be initiated");
			return;
		}
	}
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Alert");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
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
		initiateLumenRequestWizardObj.initView(resultObj, popup, "PCR");
		
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

		 /*final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setResizable(false);
			dialog.setModal(true);
			
			Button okButton = new Button("OK");
			okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			
			
			HorizontalLayout hLayout = new HorizontalLayout(okButton);
			hLayout.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
			hLayout.setMargin(true);
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red'>Intimation is already opened by another user  :</b>"+userId, ContentMode.HTML), hLayout);
			layout.setMargin(true);
			dialog.setContent(layout);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("Intimation is already opened by another user  :", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					if(null != bean.getScreenName() && (SHAConstants.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION.equals(bean.getScreenName())))
					{
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION, null);
					}
					else if(null != bean.getScreenName() && !(bean.getScreenName().equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, bean.getSearchFormDTO());
					}else{
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST, bean.getSearchFormDTO());
					}
					clearObject();
					//dialog.close();
				}
			});
 }
	 
	 	@Override
		public void buildFailureLayout() {
		
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: green;'>Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("Hospitalization ROD is pending for financial approval.</br> Please try submitting this ROD , after hospitalization ROD is approved.", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					if(null != bean.getScreenName() && (SHAConstants.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION.equals(bean.getScreenName())))
					{
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION, null);
					}
					else if(null != bean.getScreenName() && !(bean.getScreenName().equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, bean.getSearchFormDTO());
					}else{
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST, bean.getSearchFormDTO());
					}
					clearObject();
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					SHAUtils.clearSessionObject(currentRequest);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
		}
	 	
	 	public void warningMessageForCopay() {
			
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: red;'>Selected Co-pay Percentage is Zero !!!!.</br> Do you wish to Proceed.</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Yes");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button cancelButton = new Button("No");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox("Selected Co-pay Percentage is Zero !!!!.</br> Do you wish to Proceed.", buttonsNamewithType);
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
//					fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, bean);  // For  R1136
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					wizard.getFinishButton().setEnabled(true);
//					wizard.getFinishButton().setEnabled(true);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
		}
	 	
	 	@Override
		public void validationForLimit() {
		
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: red;'>Approved Amount exceeded for this user.</b>", ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
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
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Home");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox("Approved Amount exceeded for this user.", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					releaseHumanTask();
					if(null != bean.getScreenName() && (SHAConstants.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION.equals(bean.getScreenName())))
					{
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION, null);
					}
					else if(null != bean.getScreenName() && !(bean.getScreenName().equalsIgnoreCase(SHAConstants.WAIT_FOR_INPUT_SCREEN))){
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, bean.getSearchFormDTO());
					}else{
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST, bean.getSearchFormDTO());
					}
					clearObject();
					VaadinRequest currentRequest = VaadinService.getCurrentRequest();
					SHAUtils.clearSessionObject(currentRequest);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
		}
		
		public void alertMessageForCopay() {
			
			String message = "Selected Co-pay Percentage is "+bean.getHighestCopay() + " %" + "</br> Do you wish to Proceed.</b>";
			
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			/*Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Yes");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button cancelButton = new Button("No");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(message, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button cancelButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					proceedForFinalSubmit();  // R1136
//					fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, bean);  // For  CR R1136
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					wizard.getFinishButton().setEnabled(true);
//					wizard.getFinishButton().setEnabled(true);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			
			
		}

		  private void releaseHumanTask(){
				
				Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
		     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
		 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
		 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

		 		if(existingTaskNumber != null){/*
		 			BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
		 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		 		*/}
		 		
		 		if(wrkFlowKey != null){
		 			DBCalculationService dbService = new DBCalculationService();
		 			dbService.callUnlockProcedure(wrkFlowKey);
		 			getSession().setAttribute(SHAConstants.WK_KEY, null);
		 		}
			}

		@Override
		public void alertForAlreadyAcquired(String aquiredUser) {
			/*Label successLabel = new Label(
					"<b style = 'color: green;'> Claim is already opened by "+aquiredUser +"</b>",
					ContentMode.HTML);
			
			// Label noteLabel = new
			// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
			// ContentMode.HTML);

			Button homeButton = new Button("Home");
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
			dialog.show(getUI().getCurrent(), null, true);*/

			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "Home");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("Claim is already opened by "+aquiredUser, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					if(null != bean.getScreenName() && (SHAConstants.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION.equals(bean.getScreenName())))
					{
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION, null);
						SHAUtils.setClearPreauthDTO(bean);
					}
					else if(null != bean.getScreenName() && !(SHAConstants.WAIT_FOR_INPUT_SCREEN.equals(bean.getScreenName())))
					{
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST, bean.getSearchFormDTO());
						SHAUtils.setClearPreauthDTO(bean);
					}
					else
					{
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_WAIT_FOR_INPUT, bean.getSearchFormDTO());
						SHAUtils.setClearPreauthDTO(bean);
					}
					

				}
			});
		}
		
		private void clearObject() {
			if(rewardRecognitionRequestViewObj != null){
				rewardRecognitionRequestViewObj.invalidate();
			}
			dataExtractionViewImplObj.setClearReferenceData();
			if(premedicalprocessingViewImplObj != null){
				premedicalprocessingViewImplObj.setClearReferenceData();
			}
			medicalDecisionViewImplObj.setClearReferenceData();
			fireViewEvent(ClaimRequestDataExtractionPagePresenter.REFERENCE_DATA_CLEAR, null);
			fireViewEvent(ClaimRequestPremedicalProcessingPagePresenter.REFERENCE_DATA_CLEAR, null);
			fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.REFERENCE_DATA_CLEAR, null);
		}
		
		@Override
 		public void setUpdateOtherClaimsDetails(
 				List<UpdateOtherClaimDetailDTO> updateOtherClaimDetailList) {

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
		
		
		@Override
		public void confirmMessageForDefinedLimt(final Object rejectionCategoryDropdownValues) {
			
			Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
			if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
			bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
			
			/*ConfirmDialog dialog = ConfirmDialog
					.show(getUI(),
							"Confirmation",
							"Is the Defined Limit is the reason for Rejection ?",
							"No", "Yes", new ConfirmDialog.Listener() {

								public void onClose(ConfirmDialog dialog) {
									if (!dialog.isConfirmed()) {
										medicalDecisionViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, true);
										
									} else {
										medicalDecisionViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, false);
									}
								}
							});

			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
			buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createConfirmationbox("Is the Defined Limit is the reason for Rejection ?", buttonsNamewithType);
			Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
					.toString());
			Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
					.toString());
			yesButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {

					medicalDecisionViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, true);
					
				
				}
				});
			noButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					medicalDecisionViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, false);
				}
				});
		}
		else{
			medicalDecisionViewImplObj.confirmRejectionLayout(rejectionCategoryDropdownValues, false);
		}
	}
		
		public Boolean alertMessageForPEDWatchList() {
	   		/*Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.PED_WATCHLIST + "</b>",
					ContentMode.HTML);
	   		final Boolean isClicked = false;
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(SHAConstants.PED_WATCHLIST, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
					bean.setIsPEDInitiated(false);
				}
			});
			return true;
		}
		
		public void showErrorPopUp(String emsg) {
			/*Label label = new Label(emsg, ContentMode.HTML);
		    label.setStyleName("errMessage");
		    VerticalLayout layout = new VerticalLayout();
		    layout.setMargin(true);
		    layout.addComponent(label);
		    
		    ConfirmDialog dialog = new ConfirmDialog();
		    dialog.setCaption("Warning");
		    dialog.setClosable(true);
		    dialog.setContent(layout);
		    dialog.setResizable(false);
		    dialog.setModal(true);
		    dialog.show(getUI().getCurrent(), null, true);*/
		    HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createWarningBox(emsg, buttonsNamewithType);
		}
		
/*		@Override
		public void genertateFieldsBasedOnFieldVisit(
				BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority) {*/
		
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
//			this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
			this.bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_INVESTIGATION_STATUS);
			this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);
			ParallelInvestigationDetails viewInvestigationDetails = getRevisedInvestigationDetails(bean, true,ReferenceTable.ZONAL_REVIEW_STAGE,popup);
			popup.setContent(viewInvestigationDetails);
			popup.setModal(true);
			popup.setClosable(true);
			UI.getCurrent().addWindow(popup);
		}

		@Override
		public void alertMessageForInvestigation(final boolean directToAssignInv) {

			
			String message = "Investigation Request has already been initiated. </br> Do you still want to initiate another Investigation request?</b>";
			
			/*Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
			
			Button homeButton = new Button("Yes");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			Button cancelButton = new Button("No");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			
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
			dialog.show(getUI().getCurrent(), null, true);*/
			
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
					
					generateFieldsOnInvtClick(directToAssignInv);
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
//		this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
		this.bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
		buildQueryLayout();
}
	
	@Override
	public void validateCancelRequest(InvesAndQueryAndFvrParallelFlowTableDTO invesFvrQueryDTO) {
		
		/*Label alertMsg = new Label();*/
		String alertMsg="";
		if(null != invesFvrQueryDTO && null != invesFvrQueryDTO.getType()){
			
			/*if((SHAConstants.PARALLEL_FVR_TYPE).equalsIgnoreCase(invesFvrQueryDTO.getType())){
				
				if(null != invesFvrQueryDTO.getStatusKey() && !(ReferenceTable.INITITATE_FVR).equals(invesFvrQueryDTO.getStatusKey()))
				{
					 alertMsg = new Label("<b style = 'color: red;'>  FVR has been assigned to Field Visit Representative and awaiting report, <br>request cannot be cancelled </b>", ContentMode.HTML);
					
				}
			}*/
			
			 if((SHAConstants.PARALLEL_INVESTIGATION_TYPE).equalsIgnoreCase(invesFvrQueryDTO.getType())){
				
				if(null != invesFvrQueryDTO.getStatusKey() && !(ReferenceTable.INITIATE_INVESTIGATION).equals(invesFvrQueryDTO.getStatusKey()))
				{
					 /*alertMsg = new Label("<b style = 'color: red;'>  “Investigation has been assigned to an RVO and awaiting report,</br>request cannot be cancelled” </b>", ContentMode.HTML);*/
					//alertMsg="<b style = 'color: red;'>  “Investigation has been assigned to an RVO and awaiting report,</br>request cannot be cancelled” </b>";
					alertMsg="Investigation has been assigned to an RVO and awaiting report,</br>request cannot be cancelled";
					
				}
			}
		}
		
		
		/*VerticalLayout layout = new VerticalLayout(alertMsg);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(alertMsg, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		
	
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
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
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
		unbindAndRemoveComponents(dynamicFrmLayout);
		unbindAndRemoveComponents(userLayout);

		queryDetailsTableObj.init("Previous Query Details", false,
				false);
		queryDetailsTableObj.setViewQueryVisibleColumn();
		
		setQueryValues(this.bean.getKey(), this.bean.getClaimDTO().getKey());

		//R1295
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

					unbindAndRemoveComponents(dynamicFrmLayout);
					unbindAndRemoveComponents(userLayout);
					generateQueryDetails(queryCount);
				}else{
					bean.setQueryType(null);
					bean.setQueryCount(queryCount);

					unbindAndRemoveComponents(dynamicFrmLayout);
					unbindAndRemoveComponents(userLayout);
					generateQueryDetails(queryCount);
				}
			}
		});
		
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(setQueryValues + "");
		txtQueryCount.setReadOnly(true);
		txtQueryCount.setEnabled(false);
		
		queryRemarksTxta = new TextArea("Query Remarks");		
		queryRemarksTxta.setMaxLength(4000);
		queryRemarksTxta.setWidth("350px");
		
		dynamicFrmLayout.addComponent(cmbQueryType);
		dynamicFrmLayout.addComponent(txtQueryCount);		
		dynamicFrmLayout.addComponent(queryRemarksTxta);
		dynamicFrmLayout.setSpacing(true);
		

		alignFormComponents();
		
		if (viewAllDocsLayout != null
				&& viewAllDocsLayout.getComponentCount() > 0) {
			viewAllDocsLayout.removeAllComponents();
		}
		
		viewAllDocsLayout.addComponent(viewClaimsDMSDocument);
		bean.getPreauthMedicalDecisionDetails().setUserClickAction("Query");
		userLayout = buildUserRoleLayout();
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(dynamicFrmLayout,userLayout);
		
		VerticalLayout vTblLayout = new VerticalLayout(
				queryDetailsTableObj, viewAllDocsLayout ,hLayout);
		
		vTblLayout.setComponentAlignment(viewAllDocsLayout, Alignment.MIDDLE_RIGHT);
		
		wholeVlayout.addComponent(vTblLayout);
		mandatoryFields = new ArrayList<Component>();
		mandatoryFields.add(cmbQueryType);
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
	
	
	@SuppressWarnings("deprecation")
	private void alignFormComponents() {
		if (dynamicFrmLayout != null) {
			for (int i = 0; i < dynamicFrmLayout.getComponentCount(); i++) {
				dynamicFrmLayout.setExpandRatio(
						dynamicFrmLayout.getComponent(i), 0.5f);
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
							  tokenInputs = null;
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
						wizard.getNextButton().setEnabled(false);
						wizard.getFinishButton().setEnabled(true);
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

		
		private void showErrorPopup(String eMsg) {
			/*Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Alert");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
		}
		
		public List<String> getErrors() {
			return this.errorMessages;
		}

		
		public boolean isValid() {
			boolean hasError = false;
			showOrHideValidation(true);
			errorMessages.removeAll(getErrors());
			
			if(this.bean.getParallelStatusKey() != null && this.bean.getParallelStatusKey().equals(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS)){
				if(queryRemarksTxta != null && (queryRemarksTxta.getValue() == null) || (queryRemarksTxta.getValue() != null && queryRemarksTxta.getValue().isEmpty())){
					errorMessages.add("Please Enter Query Remarks </br>");
					hasError = true;
				}
				if(cmbQueryType.getValue() == null){
					errorMessages.add("Please Select Query Type </br>");
					hasError = true;
				}
				
				if(bean.getPreauthMedicalDecisionDetails().getIsMandatory()){
					/*if((cmbUserRoleMulti.getValue() == null || cmbUserRoleMulti.isEmpty()) ||(cmbDoctorNameMulti.getValue() == null || cmbDoctorNameMulti.isEmpty()) ||
						(remarksFromDeptHead.getValue() == null || ("").equalsIgnoreCase(remarksFromDeptHead.getValue()) || remarksFromDeptHead.getValue().isEmpty())){
						hasError = true;
						errorMessages.add(SHAConstants.USER_ROLE_VALIDATION + " </br>");
					}*/
					if(cmbUserRoleMulti!= null && cmbUserRoleMulti.isEmpty()){
						hasError = true;
						errorMessages.add("Please provide Consulted With"+ "</br>");
					}
					if(cmbDoctorNameMulti!= null && cmbDoctorNameMulti.isEmpty()){
						hasError = true;
						errorMessages.add("Please provide Opinion Given By"+ "</br>");
					}
					
					if(remarksFromDeptHead.getValue() == null || ("").equalsIgnoreCase(remarksFromDeptHead.getValue())){
						hasError = true;
						errorMessages.add("Please provide Opinion Given is mandatory"+ "</br>");
					}
					
//					R0001	MEDICAL HEAD
//					R0002	UNIT HEAD
//					R0003	DIVISION HEAD
//					R0004	CLUSTER HEAD
//					R0005	SPECIALIST
//					R0006	ZONAL HEAD
//					R00011	MEDICAL HEAD - GMC
//					R00012	DEPUTY MEDICAL HEAD - GMC
					List<String> roleList = null;
					Map<String, String> selectedRole = getRoleValidationContainer();
					Map<String, String> selectedUser = getUserValidationContainer();
					
					System.out.println("Role Val :"+selectedRole);
					System.out.println("User Val :"+selectedUser);
					if(bean.getPreauthMedicalDecisionDetails().getIsSDMarkedForOpinion()){
						System.out.println("ClaimType : "+bean.getNewIntimationDTO().getClaimType().getValue());
						boolean roleAvailabilityFlag = false;
						if(bean.getNewIntimationDTO().getClaimType() != null && bean.getNewIntimationDTO().getClaimType().getValue().equals("Cashless")){
							roleList =  Arrays.asList("R0002", "R0006");
							hasError = doOpinionSDValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
						}else{
							roleList =  Arrays.asList("R0002");
							hasError = doOpinionSDValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
						}

					}else{
						if(bean.getPreauthMedicalDecisionDetails().getIsPortedPolicy()){
							System.out.println("This is a Ported Policy.......");
							//						if((Long.valueOf(bean.getPreauthMedicalDecisionDetails().getFinalClaimAmout()) >= 300000L) && bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
							if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
								roleList =  Arrays.asList("R0004", "R0003", "R0001", "R00011", "R00012", "R0007");
							}
							System.out.println(bean.getNewIntimationDTO().getIntimationId()+"<------>"+bean.getPreauthMedicalDecisionDetails().getUserClickAction());
							List<String> userPortedActionList = Arrays.asList("Cancel ROD");
							boolean roleAvailabilityFlag = false;
							if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
								hasError = doRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
							}else if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Reject")){		
								roleList =  Arrays.asList("R0002", "R0001", "R0007");
								String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
								boolean isSeniorDocLogin = masterService.checkSeniorDoctor(loginUserId.toUpperCase());
								if(!isSeniorDocLogin){
									hasError = doPortedPolicyRejectionRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
								}else{
									hasError = doPPRejectionValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
								}
								System.out.println("CRMD Ported Policy Rejection ErrorFlg : "+hasError);
							}else if(userPortedActionList.contains(bean.getPreauthMedicalDecisionDetails().getUserClickAction())){					
								roleList =  Arrays.asList("R0004", "R0003", "R0001", "R0007");
								hasError = doPortedPolicyRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
								System.out.println("CRMD Ported Policy ErrorFlg : "+hasError);
							}

						}else{

							if(Long.valueOf(bean.getPreauthMedicalDecisionDetails().getFinalClaimAmout()) >= 300000L){
								roleList =  Arrays.asList("R0001", "R0002", "R00011", "R00012", "R0007");
							}else{
								roleList =  Arrays.asList("R0003", "R0001", "R0002", "R00011", "R00012", "R0007");
							}

							System.out.println(bean.getNewIntimationDTO().getIntimationId()+"<------>"+bean.getPreauthMedicalDecisionDetails().getUserClickAction());

							boolean roleAvailabilityFlag = false;
							List<String> userActionList = Arrays.asList("Approve", "Cancel ROD", "Reject");
							if(userActionList.contains(bean.getPreauthMedicalDecisionDetails().getUserClickAction())){
								hasError = doRoleClarityValidation(selectedRole, selectedUser, hasError, roleList, roleAvailabilityFlag);
								System.out.println("CRMD ErrorFlg : "+hasError);
							}
						}
					}
				}else if(!bean.getPreauthMedicalDecisionDetails().getIsMandatory()){
					String nonMandateerrorMessage = "Please provide Consulted With, Opinion Given by, and Opinion Given or Make Consulted With, Opinion Given by, and Opinion Given as Empty"+"</br>";
					if(cmbUserRoleMulti != null && cmbDoctorNameMulti != null && remarksFromDeptHead != null){
						if((!cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue()))){
							hasError = true;
							errorMessages.add(nonMandateerrorMessage);
						}

						if((cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue()))){
							hasError = true;
							errorMessages.add(nonMandateerrorMessage);
						}

						if((cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue()))){
							hasError = true;
							errorMessages.add(nonMandateerrorMessage);
						}					

						if(!cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && StringUtils.isBlank(remarksFromDeptHead.getValue())){
							hasError = true;
							errorMessages.add(nonMandateerrorMessage);
						}

						if(!cmbUserRoleMulti.isEmpty() && cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue())){
							hasError = true;
							errorMessages.add(nonMandateerrorMessage);
						}

						if(cmbUserRoleMulti.isEmpty() && !cmbDoctorNameMulti.isEmpty() && !StringUtils.isBlank(remarksFromDeptHead.getValue())){
							hasError = true;
							errorMessages.add(nonMandateerrorMessage);
						}
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

				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			showOrHideValidation(false);
			return !hasError;
		}
		
		private Button getQuerySubmitButtonWithListener(final ConfirmDialog dialog) {
			submitButton = new Button("Submit");
			submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			submitButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -5934419771562851393L;

				@Override
				public void buttonClick(ClickEvent event) {
					StringBuffer eMsg = new StringBuffer();
					if (isValid()) {
						//wrongly merged to production build
						//IMSSUPPOR-31602 - uncommented for fix
						bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
						bean.setParallelStatusKey(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
						
						if(queryRemarksTxta != null){
							bean.getPreauthMedicalDecisionDetails().setQueryRemarks(queryRemarksTxta.getValue());
						}
						
						if(remarksFromDeptHead != null){
							bean.getPreauthMedicalDecisionDetails().setRemarksFromDeptHead(remarksFromDeptHead.getValue());
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
						showErrorPopup(eMsg.toString());
					}
				}
			});
			return submitButton;
		}
		
		public void buildQuerySuccessLayout() {
			/*Label successLabel = new Label(
					"<b style = 'color: green;'> Query has been initiated successfully!!!</b>",
					ContentMode.HTML);

			Button homeButton = new Button("Ok");
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
			dialog.show(getUI().getCurrent(), null, true);*/
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

//					toolBar.countTool();As Per BA Revised Req sub flow process won't considered as count
					
				}
			});
			
		}
		
		
		public void buildFVRSuccessLayout() {
			/*Label successLabel = new Label(
					"<b style = 'color: green;'>FVR has been initiated successfully!!</b>",
					ContentMode.HTML);

			Button homeButton = new Button("Ok");
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox("FVR has been initiated successfully!!", buttonsNamewithType);
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

					//dialog.close();

//					toolBar.countTool();  As Per BA Revised Req sub flow process won't considered as count
					
				}
			});
			
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
			 
			/* this.bean.setStatusKey(ReferenceTable.CLAIM_REQUEST_INITIATE_FIELD_REQUEST_STATUS);
			 this.bean.setStageKey(ReferenceTable.CLAIM_REQUEST_STAGE);*/
			// this.claimRequestWizardButtons.buildInitiateParallelFieldVisit(dropDownValues);
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
			dynamicFrmLayout.removeAllComponents();
			wholeVlayout.removeComponent(dynamicFrmLayout);

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

		@Override
		public void validateCancelQueryRequest() {
			// TODO Auto-generated method stub
			
			String message = "Query reply has been received and is in progress</b>";
			
			/*Label successLabel = new Label("<b style = 'color: red;'>"+ message, ContentMode.HTML);
			
			Button homeButton = new Button("Ok");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel,homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
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
				}
			});
		}

		@Override
		public void validateUploadQueryLetter() {
			// TODO Auto-generated method stub

			String message = "Please Upload Letter before Cancel the Query</b>";
			
			/*Label successLabel = new Label("<b style = 'color: red;'>"+ message, ContentMode.HTML);
			
			Button homeButton = new Button("Ok");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel,homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createAlertBox(message, buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();
				}
			});
		
		}	
		
	public void confirmationForInvestigation() {			
			
		String message = "The claim would be moved to wait for input Q. Do you want to proceed further?";
		
		/*Label successLabel = new Label("<b style = 'color: blue;'>"+message, ContentMode.HTML);
		
		Button homeButton = new Button("Yes");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button cancelButton = new Button("No");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		
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
		dialog.show(getUI().getCurrent(), null, true);*/
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
//				fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, bean);    //For  R1136 Commented
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
	
	public void showScoringView(){
		
		scoringAndPPTabUI.setIntimationNumber(bean.getNewIntimationDTO().getIntimationId());
		scoringAndPPTabUI.setScreenName("Claim Request");
		scoringAndPPTabUI.setDtoBean(bean);
		scoringAndPPTabUI.setParentScoringButton(btnHospitalScroing);
		scoringAndPPTabUI.init(true);
		
		/*hospitalScoringView.init(bean.getNewIntimationDTO().getIntimationId(), true);
		hospitalScoringView.setScreenName("Claim Request");
		hospitalScoringView.setDtoBean(bean);
		hospitalScoringView.setParentScoringButton(btnHospitalScroing);*/
		VerticalLayout misLayout = new VerticalLayout(scoringAndPPTabUI);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("50%");
		popup.setHeight("58%");
		popup.setContent(misLayout);
		popup.setClosable(false);
		popup.center();
		popup.setResizable(true);
		scoringAndPPTabUI.setPopupWindow(popup);
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

	@Override
	public void validateForEnableParallel(SelectValue value, SelectValue value1) {
		if(claimRequestWizardButtons != null){
			if((value != null && value.getId() != null && (ReferenceTable.MATERNITY_MASTER_ID).equals(value.getId()) && ((bean.getNewIntimationDTO().getInsuredPatient() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender().getKey().equals(ReferenceTable.FEMALE_GENDER)) 
					|| (ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())))) || (value1 != null && value1.getValue() != null && ReferenceTable.RELATED_TO_EARLIER_CLAIMS.equals(value1.getId())) ){
				claimRequestWizardButtons.enableButton(Boolean.TRUE);
			}else{
				claimRequestWizardButtons.enableButton(Boolean.FALSE);
			}
			
		}
	}
	
	public FormLayout buildUserRoleLayout(){
		String enhancementType = "F";
		String hospitilizationType = "N";
		if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat()){
			hospitilizationType = "Y";
		}
		//R1295
		Integer qryTyp;
		if (bean.getQueryType() == null){
			qryTyp = 0;
		}else{
			qryTyp = bean.getQueryType().getId().intValue();
		}
		Integer qryCnt;
		if (bean.getQueryCount() == null){
			qryCnt = 0;
		}else{
			qryCnt = bean.getQueryCount() + 1;
		}
	
		String reconsiderationFlag = null != bean.getIsRejectReconsidered() && bean.getIsRejectReconsidered() ? "Y" : "N";
		String finalClaimAmount = "";
		if(bean.getPreauthMedicalDecisionDetails().getUserClickAction().equals("Approve")){
			finalClaimAmount = bean.getAmountConsidered();
		}else{
			finalClaimAmount = String.valueOf(createRodService.getTotalClaimedAmt(bean.getKey(), ReferenceTable.HOSPITALIZATION).longValue());
		}
		finalClaimAmount = (finalClaimAmount == null)?"0":finalClaimAmount;
		bean.getPreauthMedicalDecisionDetails().setFinalClaimAmout(Long.valueOf(finalClaimAmount));

		System.out.println("CRMD User Role For Intimation No "+bean.getNewIntimationDTO().getIntimationId());
	
		Map<String,Object> opinionValues = dbCalculationService.getOpinionValidationDetails(Long.valueOf(finalClaimAmount),bean.getStageKey(),bean.getStatusKey(),
				Long.valueOf(bean.getNewIntimationDTO().getCpuCode()),reconsiderationFlag,bean.getNewIntimationDTO().getPolicy().getKey(),bean.getClaimDTO().getKey(),enhancementType,hospitilizationType,qryTyp,qryCnt,
				bean.getClaimDTO().getClaimType().getId(),bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey(),SHAConstants.CLAIM_REQUEST,
				bean.getKey(), bean.getStatusKey(),SHAConstants.N_FLAG); 
		
		BeanItemContainer<SpecialSelectValue> userRole = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
		userRole.addAll((List<SpecialSelectValue>)opinionValues.get("role"));
		BeanItemContainer<SpecialSelectValue> userRoleWithoutSGm = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
		if(bean.getNewIntimationDTO().getClaimType().getValue().equalsIgnoreCase(SHAConstants.CASHLESS)) {
			
			for (SpecialSelectValue component : userRole.getItemIds()) {
				if(!component.getSpecialValue().equalsIgnoreCase("R0007")) {
					userRoleWithoutSGm.addItem(component);
					
				}
			}
		}
		BeanItemContainer<SpecialSelectValue> empNames = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
		empNames.addAll((List<SpecialSelectValue>) opinionValues.get("emp"));
		
			if(null != opinionValues){			
				String mandatoryFlag =  (String) opinionValues.get("mandatoryFlag");
				if(null != mandatoryFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(mandatoryFlag)){
					bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.TRUE);
				}
				else{
					bean.getPreauthMedicalDecisionDetails().setIsMandatory(Boolean.FALSE);
				}
				
				String portedFlag =  (String) opinionValues.get("portedFlag");
				if(null != portedFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(portedFlag)){
					bean.getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.TRUE);
				}else{
					bean.getPreauthMedicalDecisionDetails().setIsPortedPolicy(Boolean.FALSE);
				}
				
				String seriousDefiencyFlagForOpinion = (String) opinionValues.get("seriousDeficiencyFlag");
				System.out.println("seriousDefiencyFlagForOpinion : "+seriousDefiencyFlagForOpinion);
				if(null != portedFlag && SHAConstants.YES_FLAG.equalsIgnoreCase(seriousDefiencyFlagForOpinion)){
					bean.getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(Boolean.TRUE);
				}else if(null != portedFlag && SHAConstants.N_FLAG.equalsIgnoreCase(seriousDefiencyFlagForOpinion)){
					bean.getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(Boolean.FALSE);
				}else{
					bean.getPreauthMedicalDecisionDetails().setIsSDMarkedForOpinion(null);
				}
			}
			cmbUserRoleMulti = new ComboBoxMultiselect("Consulted With");
			cmbUserRoleMulti.setShowSelectedOnTop(true);
			cmbUserRoleMulti.setComparator(SHAUtils.getComparator());
			if(bean.getNewIntimationDTO().getClaimType().getValue().equalsIgnoreCase(SHAConstants.CASHLESS)) {
				cmbUserRoleMulti.setContainerDataSource(userRoleWithoutSGm);
			}else{
			cmbUserRoleMulti.setContainerDataSource(userRole);
			}
			cmbUserRoleMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbUserRoleMulti.setItemCaptionPropertyId("value");	
			cmbUserRoleMulti.setData(userRole);
			
					
			cmbDoctorNameMulti = new ComboBoxMultiselect("Opinion Given by");
			cmbDoctorNameMulti.setShowSelectedOnTop(true);
			cmbDoctorNameMulti.setContainerDataSource(empNames);
			cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
			cmbDoctorNameMulti.setData(empNames);
	
			remarksFromDeptHead = new TextArea("Opinion Given");
			remarksFromDeptHead.setMaxLength(2000);
			remarksFromDeptHead.setWidth("400px");
			SHAUtils.handleTextAreaPopup(remarksFromDeptHead,null,getUI());
			remarksFromDeptHead.setData(bean);
			
			addUserRoleListener();
			FormLayout fLayout = new FormLayout();
			fLayout.addComponents(cmbUserRoleMulti,cmbDoctorNameMulti,remarksFromDeptHead);
			fLayout.setMargin(Boolean.TRUE);
			fLayout.setSpacing(Boolean.TRUE);
			if(bean.getPreauthMedicalDecisionDetails().getIsMandatory()){
				mandatoryFields.add(cmbUserRoleMulti);
				mandatoryFields.add(cmbDoctorNameMulti);
				mandatoryFields.add(remarksFromDeptHead);
				showOrHideValidation(false);
			}
			else{
				mandatoryFields.remove(cmbUserRoleMulti);
				mandatoryFields.remove(cmbDoctorNameMulti);
				mandatoryFields.remove(remarksFromDeptHead);
			}
			return fLayout;	
		}

	public void addUserRoleListener(){
		getRoleValidationContainer().clear();
		getUserValidationContainer().clear();
		cmbUserRoleMulti.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
	
				PreauthMedicalDecisionDTO dtoObject = bean.getPreauthMedicalDecisionDetails();
				dtoObject.setUserRoleMulti(null);
				dtoObject.setUserRoleMulti(event.getProperty().getValue());
	
				if(cmbDoctorNameMulti != null && cmbDoctorNameMulti.getData() != null){
					BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) cmbDoctorNameMulti.getData();
					List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
					BeanItemContainer<SpecialSelectValue> listOfRoleContainer = (BeanItemContainer<SpecialSelectValue>)cmbUserRoleMulti.getData();
					getRoleValidationContainer().clear();
					List<String> roles = new ArrayList<String>();
					List<SpecialSelectValue> listOfRoles = listOfRoleContainer.getItemIds();
					for (SpecialSelectValue specialSelectValue : listOfRoles) {
						if(null != docList && !docList.isEmpty() &&docList.contains(specialSelectValue.getValue())){
							roles.add(specialSelectValue.getSpecialValue());
							if(!getRoleValidationContainer().containsKey(specialSelectValue.getSpecialValue())){
								getRoleValidationContainer().put(specialSelectValue.getSpecialValue(), specialSelectValue.getValue());
							}
						}
	
					}
					List<SpecialSelectValue> filtersValue = new ArrayList<SpecialSelectValue>();
					List<SpecialSelectValue> itemIds = listOfDoctors.getItemIds();
					for (SpecialSelectValue specialSelectValue : itemIds) {
						if( specialSelectValue.getSpecialValue() != null && 
								roles.contains(specialSelectValue.getSpecialValue())){
							filtersValue.add(specialSelectValue);
						}
					}
	
					BeanItemContainer<SpecialSelectValue> filterContainer = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
					filterContainer.addAll(filtersValue);
					cmbDoctorNameMulti.setContainerDataSource(filterContainer);
					cmbDoctorNameMulti.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbDoctorNameMulti.setItemCaptionPropertyId("commonValue");
				}
			}
		});
	
		cmbDoctorNameMulti.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				PreauthMedicalDecisionDTO dtoObject = bean.getPreauthMedicalDecisionDetails();
				dtoObject.setDoctorName(null);
				dtoObject.setDoctorName(event.getProperty().getValue());
				Set selectedObject = new HashSet<>((Collection) event.getProperty().getValue());
				getUserValidationContainer().clear();
				List<SpecialSelectValue> listOfUserSelected = new ArrayList<SpecialSelectValue>(selectedObject);
				if(listOfUserSelected.size() > 0){
					for(SpecialSelectValue specialSelectValue : listOfUserSelected){
						if(!getUserValidationContainer().containsKey(specialSelectValue.getSpecialValue())){
							getUserValidationContainer().put(specialSelectValue.getSpecialValue(), specialSelectValue.getValue());
						}else{
							String temp = getUserValidationContainer().get(specialSelectValue.getSpecialValue());
							if(temp.contains(specialSelectValue.getValue())){
								getUserValidationContainer().put(specialSelectValue.getSpecialValue(),specialSelectValue.getValue());
							}else{
								getUserValidationContainer().put(specialSelectValue.getSpecialValue(), temp+","+specialSelectValue.getValue());
							}
						}
					}
				}else{
					getUserValidationContainer().clear();
				}
				List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
				BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) cmbDoctorNameMulti.getData();
				bean.getPreauthMedicalDecisionDetails().setDoctorContainer(listOfDoctors);
			}
		});
	}
	
	/**			  
	 * Part of CR R1136
	 */
	private void proceedForFinalSubmit(){
		showSublimitAlert();
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
				/*Label successLabel = new Label("<b style = 'color: red;'> Sublimit selected is "+selectedSublimitNames.toString()+"</b>", ContentMode.HTML);
				
				Button homeButton = new Button("OK");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
				horizontalLayout.setMargin(true);
				
				VerticalLayout layout = new VerticalLayout(successLabel, homeButton horizontalLayout);
				layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.setStyleName("borderLayout");
				
				HorizontalLayout hLayout = new HorizontalLayout(layout);
				hLayout.setMargin(true);
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setWidth("250px");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
						wizard.getFinishButton().setEnabled(true);
						dialog.close();
						
					}
				});*/
				
				if(sublimitMapAvailable && !selectedSublimitNames.toString().isEmpty()){
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createInformationBox("Sublimit selected is "+selectedSublimitNames.toString(), buttonsNamewithType);
					Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
							.toString());
					homeButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 1L;
		
						@Override
						public void buttonClick(ClickEvent event) {
							wizard.getFinishButton().setEnabled(true);
							//dialog.close();
							fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, bean);
						}
					});
					//dialog.show(getUI().getCurrent(), null, true);
				}
				else if (!sublimitMapAvailable){
					fireViewEvent(ClaimRequestWizardPresenter.SUBMIT_MEDICAL_APPROVAL_CLAIM_REQUEST, this.bean);	
				}
				
				
			  }	
		}
	
	private boolean doPPRejectionValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
		if(selectedRole != null && selectedRole.size() > 0){
			for(String key : roleList){
				if(selectedRole.containsKey(key)){
					roleAvailabilityFlag = true;
				}
				if(roleAvailabilityFlag){
					break;
				}
			}
			
			if(!roleAvailabilityFlag){
				System.out.println("Required Department Role is not Selected / not Available.");
				hasError = true;
				errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor"+"</br>"+"(Medical Head / Unit Head)"+"</br>");
			}
			
			if(!hasError){
				if(selectedUser != null){
					roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
					for(String key : roleList){
						if(selectedUser.containsKey(key)){
							roleAvailabilityFlag = true;
						}
					}
					
					if(!roleAvailabilityFlag){
						System.out.println("Required Department User is not Selected / not Available.");
						hasError = true;
						errorMessages.add("User Selection is Mandatory, Please select User for the selected Department Heads"+"</br>");
					}
					
				}else{
					hasError = true;
					errorMessages.add("User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
					System.out.println("Selected user Map is empty.......");
				}
			}
			
		}else{
			hasError = true;
			errorMessages.add("Consulted With selection is Mandatory"+"</br>");
			System.out.println("Selected user Map is empty.......");
		}
		
		return hasError;
	}
	
	private boolean doPortedPolicyRejectionRoleClarityValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
 		if(selectedRole.get("R00013") != null){
			for(String key : roleList){
				if(selectedRole.containsKey(key)){
					roleAvailabilityFlag = true;
				}
				if(roleAvailabilityFlag){
					break;
				}
			}

			if(!roleAvailabilityFlag){
				System.out.println("Senior Doctor Alone Selected.");
				hasError = true;
				errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor along with Department Heads like"+"</br>"+"(Medical Head / Unit Head)"+"</br>");
			}

			if(!hasError){					
				if(selectedUser != null){
					if(selectedUser.get("R00013") == null){
						hasError = true;
						errorMessages.add("Senior Doctor User Selection is Mandatory, Please select Senior Doctor User"+"</br>");
					}
					roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
					if(selectedUser.get("R00013") != null){
						System.out.println("Senior Doctor is Selected");
						for(String key : roleList){
							if(selectedUser.containsKey(key)){
								roleAvailabilityFlag = true;
							}
							if(roleAvailabilityFlag){
								break;
							}
						}

						if(!roleAvailabilityFlag){
							System.out.println("Senior Doctor User Alone Selected.");
							hasError = true;
							errorMessages.add("User Selection is Mandatory, Please select User for the selected Senior Doctor and Department Heads"+"</br>");
						}
					}

					for (Map.Entry<String, String> entry : selectedUser.entrySet()){
						if(entry.getKey().equals("R00013") && StringUtils.isBlank(entry.getValue())){
							hasError = true;
							System.out.println("PP 00013: "+entry.getKey()+"<------>"+entry.getValue());
							errorMessages.add("Senior Doctor Opnion is Mandatory, please select Senior Doctor User in Opinion Given By"+"</br>");
							break;
						}
						if(!entry.getKey().equals("R00013") && StringUtils.isBlank(entry.getValue())){
							hasError = true;
							System.out.println("PP !00013: "+entry.getKey()+"<------>"+entry.getValue());
							errorMessages.add("Please select User in Opinion Given By for "+"</br>"+"(Medical Head /  Unit Head )"+"</br>");
							break;
						}
						System.out.println(entry.getKey() + "/" + entry.getValue());
					}
				}else{
					hasError = true;
					errorMessages.add("Senior Doctor and Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
					System.out.println("PP : Selected user Map is empty.......");
				}
			}
		}else{
			hasError = true;
			errorMessages.add("Senior Doctor selection is mandatory, Please select the Senior Doctor in Consulted With"+"</br>");
			System.out.println("PP : Senior Doctor Not selected");
		}
 		return hasError;
 	}	
	
	private boolean doPortedPolicyRoleClarityValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
		if(selectedRole.get("R0005") != null){
			for(String key : roleList){
				if(selectedRole.containsKey(key)){
					roleAvailabilityFlag = true;
				}
				if(roleAvailabilityFlag){
					break;
				}
			}

			if(!roleAvailabilityFlag){
				System.out.println("Specialist Alone Selected.");
				hasError = true;
				errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor along with Specialist"+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM - Medical)"+"</br>");
			}

			if(!hasError){					
				if(selectedUser != null){
					if(selectedUser.get("R0005") == null){
						hasError = true;
						errorMessages.add("Specialist User Selection is Mandatory, Please select Specialist User"+"</br>");
					}
					roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
					if(selectedUser.get("R0005") != null){
						System.out.println("Specialist is Selected");
						for(String key : roleList){
							if(selectedUser.containsKey(key)){
								roleAvailabilityFlag = true;
							}
							if(roleAvailabilityFlag){
								break;
							}
						}

						if(!roleAvailabilityFlag){
							System.out.println("Specialist User Alone Selected.");
							hasError = true;
							errorMessages.add("User Selection is Mandatory, Please select User for the selected Specialist and Department Heads"+"</br>");
						}
					}

					for (Map.Entry<String, String> entry : selectedUser.entrySet()){
						if(entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
							hasError = true;
							System.out.println("PP 0005: "+entry.getKey()+"<------>"+entry.getValue());
							errorMessages.add("Specialist Opnion is Mandatory, please select Specialist User in Opinion Given By"+"</br>");
							break;
						}
						if(!entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
							hasError = true;
							System.out.println("PP !0005: "+entry.getKey()+"<------>"+entry.getValue());
							errorMessages.add("Please select User in Opinion Given By for "+"</br>"+"(Medical Head /  Division Head / Cluster Head )"+"</br>");
							break;
						}
						System.out.println(entry.getKey() + "/" + entry.getValue());
					}
				}else{
					hasError = true;
					errorMessages.add("Specialist and Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
					System.out.println("PP : Selected user Map is empty.......");
				}
			}
		}else{
			//			R0001	MEDICAL HEAD
			//			R0002	UNIT HEAD
			//			R0003	DIVISION HEAD
			//			R0004	CLUSTER HEAD
			//			R0005	SPECIALIST
			//			R0006	ZONAL HEAD
			//			R00011	MEDICAL HEAD - GMC
			//			R00012	DEPUTY MEDICAL HEAD - GMC

			if((selectedRole.get("R0004") == null && selectedRole.get("R0001") == null) && (selectedRole.get("R0003") == null && selectedRole.get("R0001") == null)){
				hasError = true;
				errorMessages.add("Cluster Head/ Division Head selection is mandatory"+"</br>");
				System.out.println("PP : Cluster Head/ Division Head not selected");
			}

			if(!hasError && (selectedRole.get("R0004") != null && selectedRole.get("R0001") == null) || (selectedRole.get("R0004") == null && selectedRole.get("R0001") != null)){
				hasError = true;
				errorMessages.add("Intimation Decision has to be validated by Medical Head along with Cluster Head or Specialist"+"</br>");
				System.out.println("PP : Cluster Head/ Medical Head not selected");
			}

			if(!hasError && (selectedRole.get("R0004") == null && selectedRole.get("R0001") == null) && (selectedRole.get("R0003") != null && selectedRole.get("R0001") == null || (selectedRole.get("R0003") == null && selectedRole.get("R0001") != null))){
				hasError = true;
				errorMessages.add("Intimation Decision has to be validated by Medical Head along with Divisional Head or Specialist"+"</br>");
				System.out.println("PP : Division Head/ Medical Head not selected");
			}

			if(!hasError && (selectedRole.get("R0004") != null && selectedRole.get("R0001") != null) || (selectedRole.get("R0003") != null && selectedRole.get("R0001") != null)){
				if(selectedUser == null || selectedUser.size() == 0){
					hasError = true;
					errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
					System.out.println("PP : Division Head/ Medical Head not selected");
				}else if(selectedUser != null){
					if((selectedUser.get("R0004") != null) && (selectedUser.get("R0001") == null)){
						hasError = true;
						errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("PP : Division Head/ Medical Head not selected");
					}

					if(!hasError &&  (selectedUser.get("R0004") == null) && (selectedUser.get("R0001") != null)){
						hasError = true;
						errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("PP : Division Head/ Medical Head not selected");
					}

					if(!hasError &&  (selectedUser.get("R0004") == null && selectedUser.get("R0001") == null) && (selectedUser.get("R0003") != null) && (selectedUser.get("R0001") == null)){
						hasError = true;
						errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("PP : Division Head/ Medical Head not selected");
					}

					if(!hasError &&  (selectedUser.get("R0004") == null && selectedUser.get("R0001") == null) && (selectedUser.get("R0003") == null) && (selectedUser.get("R0001") != null)){
						hasError = true;
						errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("PP : Division Head/ Medical Head not selected");
					}

				}
			}

		}
		return hasError;
	}
	
	private boolean doRoleClarityValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
		if(selectedRole.get("R0005") != null){
			System.out.println("Specialist is Selected");
			for(String key : roleList){
				if(selectedRole.containsKey(key)){
					roleAvailabilityFlag = true;
				}
				if(roleAvailabilityFlag){
					break;
				}
			}
			
			if(!roleAvailabilityFlag){
				System.out.println("Specialist Alone Selected.");
				hasError = true;
				errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor along with Specialist"+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM - Medical)"+"</br>");
			}
			
			if(!hasError){					
				if(selectedUser != null){
					if(selectedUser.get("R0005") == null){
						hasError = true;
						errorMessages.add("Specialist User Selection is Mandatory, Please select Specialist User"+"</br>");
					}
					roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
					if(selectedUser.get("R0005") != null){
						System.out.println("Specialist is Selected");
						for(String key : roleList){
							if(selectedUser.containsKey(key)){
								roleAvailabilityFlag = true;
							}
							if(roleAvailabilityFlag){
								break;
							}
						}
						
						if(!roleAvailabilityFlag){
							System.out.println("Specialist User Alone Selected.");
							hasError = true;
							errorMessages.add("User Selection is Mandatory, Please select User for the selected Specialist and Department Heads"+"</br>");
						}
					}
					
					
					for (Map.Entry<String, String> entry : selectedUser.entrySet()){
						if(entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
							hasError = true;
							System.out.println("0005: "+entry.getKey()+"<------>"+entry.getValue());
							errorMessages.add("Specialist Opnion is Mandatory, please select Specialist User in Opinion Given By"+"</br>");
							break;
						}
						if(!entry.getKey().equals("R0005") && StringUtils.isBlank(entry.getValue())){
							hasError = true;
							System.out.println("! 0005: "+entry.getKey()+"<------>"+entry.getValue());
							errorMessages.add("Please select User in Opinion Given By for "+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head / SGM - Medical / Medical Head - GMC / Deputy Medical Head - GMC)"+ "</br>");
							break;
						}
						System.out.println(entry.getKey() + "/" + entry.getValue());
					}
				}else{
					hasError = true;
					errorMessages.add("Specialist and Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
					System.out.println("Selected user Map is empty.......");
				}
			}
		}else{
			if(selectedRole != null && selectedRole.size() > 0){
				for(String key : roleList){
					if(selectedRole.containsKey(key)){
						roleAvailabilityFlag = true;
					}
					if(roleAvailabilityFlag){
						break;
					}
				}
				
				if(!roleAvailabilityFlag){
					System.out.println("Required Department Role is not Selected / not Available.");
					hasError = true;
					errorMessages.add("Intimation Decision has to be validated by any of the Senior Doctor"+"</br>"+"(Medical Head / Unit Head / Division Head / Cluster Head)"+"</br>");
				}
				
				if(!hasError){
					if(selectedUser != null){
						roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
						for(String key : roleList){
							if(selectedUser.containsKey(key)){
								roleAvailabilityFlag = true;
							}
						}
						
						if(!roleAvailabilityFlag){
							System.out.println("Required Department User is not Selected / not Available.");
							hasError = true;
							errorMessages.add("User Selection is Mandatory, Please select User for the selected Department Heads"+"</br>");
						}
						
					}else{
						hasError = true;
						errorMessages.add("Department Head User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
						System.out.println("Selected user Map is empty.......");
					}
				}
				
			}else{
				hasError = true;
				errorMessages.add("Consulted With selection is Mandatory"+"</br>");
				System.out.println("Selected user Map is empty.......");
			}
		}
		
		return hasError;	
	}
	
	@Override
	public void validateIllnessEnableParallel(SelectValue value1,SelectValue value) {

		if(claimRequestWizardButtons != null){
			if( (value != null && value.getId() != null && (ReferenceTable.MATERNITY_MASTER_ID).equals(value.getId()) && ((bean.getNewIntimationDTO().getInsuredPatient() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredGender().getKey().equals(ReferenceTable.FEMALE_GENDER)) 
					|| (ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())))) || (value1 != null && value1.getValue() != null && ReferenceTable.RELATED_TO_EARLIER_CLAIMS.equals(value1.getId())) ){
				claimRequestWizardButtons.enableIllnessButton(Boolean.TRUE);
			}else{
				claimRequestWizardButtons.enableIllnessButton(Boolean.FALSE);
			}
			
		}
	}

	@Override
	public void generateButtonLayoutBasedOnScoring(ClaimRequestMedicalDecisionButtons buttons) {
		
		if(buttons != null){
			buttons.initView(this.bean, wizard, null);		
		}
	}
	
	@Override
	public void checkPatientStatusForMAQuery() {
		
		dataExtractionViewImplObj.checkPatientStatusForMAQuery();
	}
	
	private boolean doOpinionSDValidation(Map<String, String> selectedRole, Map<String, String> selectedUser, boolean hasError, List<String> roleList, boolean roleAvailabilityFlag){
		if(selectedRole != null && selectedRole.size() > 0){
			for(String key : roleList){
				if(selectedRole.containsKey(key)){
					roleAvailabilityFlag = true;
				}
				if(roleAvailabilityFlag){
					break;
				}
			}

			if(!roleAvailabilityFlag){
				System.out.println("SDValidation Required Department Role is not Selected / not Available.");
				hasError = true;
				if(bean.getNewIntimationDTO().getClaimType().getValue().equals("Cashless")){
					errorMessages.add("Intimation Decision has to be validated by Zonal Head / Unit Head"+"</br>");
				}else{
					errorMessages.add("Intimation Decision has to be validated by Unit Head"+"</br>");
				}
			}

			if(!hasError){
				if(selectedUser != null){
					roleAvailabilityFlag = false; // resetting the boolean to false to prepare the flag for selecteduser iteration
					for(String key : roleList){
						if(selectedUser.containsKey(key)){
							roleAvailabilityFlag = true;
						}
					}

					if(!roleAvailabilityFlag){
						System.out.println("SDValidation Required Department User is not Selected / not Available.");
						hasError = true;
						errorMessages.add("User Selection is Mandatory, Please select User for the selected department"+"</br>");
					}

				}else{
					hasError = true;
					errorMessages.add("User selection is mandatory, Please select the Opinion Given by for selected Consulted With"+"</br>");
					System.out.println("SDValidation Selected user Map is empty.......");
				}
			}

		}else{
			hasError = true;
			errorMessages.add("Consulted With selection is Mandatory"+"</br>");
			System.out.println("SDValidation Selected user Map is empty outer.......");
		}
		return hasError;
	}
	
	private boolean isInsured(){
		if(bean !=null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
			return true;
		}
		return false;
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
	 public void generateFieldsBasedOnImplantApplicable(Boolean isChecked) {
		 dataExtractionViewImplObj.generateFieldsBasedOnImplantApplicable(isChecked);
	 }
	 private void submitIcacProcess(){

			HorizontalLayout horLayout = new HorizontalLayout();
			TextArea processICACRemarks = new TextArea(
					"Refer to ICAC Remarks");
			bean.getPreauthMedicalDecisionDetails().setUserClickAction("Refer to ICAC");

			processICACRemarks.setMaxLength(3000);
			processICACRemarks.setWidth("400px");
			processICACRemarks.setHeight("200px");
			processICACRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
			SHAUtils.handleTextAreaPopupDetails(processICACRemarks,null,getUI(),SHAConstants.ICAC_REMARKS);

			Window popup = new com.vaadin.ui.Window();
			popup.setWidth("50%");
			popup.setHeight("50%");
			Button submitBtn = new Button("Submit");
			Button submitCancel = new Button("Cancel");

			FormLayout icacForm = new FormLayout(processICACRemarks);
			HorizontalLayout hLayout = new HorizontalLayout(icacForm);
			submitBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			submitCancel.setStyleName(ValoTheme.BUTTON_DANGER);
			submitBtn.setHeight("-1px");
			submitCancel.setHeight("-1px");
			horLayout.addComponents(submitBtn);
			horLayout.addComponents(submitCancel);
			horLayout.setSpacing(true);
			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.addComponents(hLayout, horLayout);
			verticalLayout.setComponentAlignment(horLayout,
					Alignment.MIDDLE_CENTER);
			popup.setContent(verticalLayout);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);

			submitCancel.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					System.out.println("Cancel listener called");
					popup.close();
				}
			});
			popup.addCloseListener(new Window.CloseListener() {

				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			submitBtn.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					String pccRemarkValue = "";
					System.out.println("Submit listener called");

					if (processICACRemarks.getValue() != null
							&& !processICACRemarks.getValue().trim()
							.isEmpty()) {

						System.out.println("----ICAC-----"
								+ processICACRemarks + "-----------");
						//Parraell Processing submit
						bean.setIcacProcessRemark(processICACRemarks.getValue());
						bean.setIcacProcessFlag(SHAConstants.YES_FLAG);
						fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.MA_ICAC_SUBMIT_EVENT,bean);
						popup.close();
					} else {
						showErrorMessage("Please enter the Refer to ICAC remarks fields");
					}

				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);	
	 }
	 
	 @Override
	 public void generatePPCoadingField(Boolean ischecked,List<PPCodingDTO> codingDTOs,Map<String,Boolean> selectedPPCoding) {
		 scoringAndPPTabUI.generatePPCoadingField(ischecked,codingDTOs,selectedPPCoding);
	 }
	 
	 @Override
	 public void generateadmissiontypeFields(Boolean displayadmissiontype) {
		 dataExtractionViewImplObj.generateadmissiontypeFields(displayadmissiontype);// TODO Auto-generated method stub

	 }

	 @Override
	 public void genertateFieldsBasedOnTypeOfAdmisstion() {
		 dataExtractionViewImplObj.genertateFieldsBasedOnTypeOfAdmisstion();

	 }
	 
	 public void autoAllocationCancelRemarksLayout(){

			autoAllocCancelRemarks = new TextArea("Cancel Remarks");
			autoAllocCancelRemarks.setMaxLength(4000);
			autoAllocCancelRemarks.setWidth("350px");
			autoAllocCancelRemarks.setHeight("150px");
			autoAllocCancelRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
			SHAUtils.handleTextAreaPopupDetails(autoAllocCancelRemarks,null,getUI(),null);
			Button cancel = new Button("OK");
			cancel.addStyleName(ValoTheme.BUTTON_DANGER);
			cancel.setWidth("-1px");
			cancel.setHeight("-10px");
			cancel.setEnabled(true);
			VerticalLayout vLayout = new VerticalLayout();
			vLayout.addComponents(autoAllocCancelRemarks,cancel);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			vLayout.setComponentAlignment(autoAllocCancelRemarks, Alignment.MIDDLE_CENTER);
			vLayout.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);
			final Window popup = new com.vaadin.ui.Window();
			popup.setWidth("35%");
			popup.setHeight("30%");
			popup.setContent(vLayout);
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

			cancel.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -5934419771562851393L;
				@Override
				public void buttonClick(ClickEvent event) {
					if(null != autoAllocCancelRemarks && null != autoAllocCancelRemarks.getValue() && !("").equalsIgnoreCase(autoAllocCancelRemarks.getValue())){
						releaseHumanTask();
						bean.setAutoAllocCancelRemarks(autoAllocCancelRemarks.getValue());
						fireViewEvent(ClaimRequestWizardPresenter.PROCESS_CLAIM_REQUEST_SAVE_AUTO_ALLOCATION_CANCEL_REMARKS, bean);
						popup.close();
						SHAUtils.setClearPreauthDTO(bean);
						fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION,null);
						clearObject();
						VaadinRequest currentRequest = VaadinService.getCurrentRequest();
						SHAUtils.clearSessionObject(currentRequest);
					}else
					{
						SHAUtils.showAlertMessageBox("Please enter the Cancel Remarks");
					}

				}
			});
			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
	 }
	 
	 public void addVaadinSessionAttribute(Long policyKey){
		 
	 }
	 
	 @SuppressWarnings("serial")
		public ShortcutListener callPreviousClaimsDetailsViewSListener(){
			ShortcutListener shortcutListener = new ShortcutListener("PreviousClaimDetails", KeyCode.NUM2, new int[]{ModifierKey.CTRL, ModifierKey.ALT}) {
				@Override
				public void handleAction(Object sender, Object target) {
					viewBasePolicyDetailsSelect.setValue(VIEW_BASE_POLICY_CLAIMS);
					if(viewBasePolicyDetailsSelect.getValue() != null){
						viewBasePolicyDetailsSelect.select(viewBasePolicyDetailsSelect.getValue());
						//btnGo.click();
					}
				}
			};
			getActionManager().addAction(shortcutListener);
			return shortcutListener;
		}
	 
	 public EnhancedBrowserWindowOpener getSopener() {
			return sopener;
		}

	 public void setSopener(EnhancedBrowserWindowOpener sopener) {
		 this.sopener = sopener;
	 }
	 
	 @SuppressWarnings("serial")
		public ShortcutListener callSListener(){
		ShortcutListener shortcutListener = new ShortcutListener("PreviousClaimDetails", KeyCode.NUM3, new int[]{ModifierKey.CTRL, ModifierKey.ALT}) {
			    @Override
			    public void handleAction(Object sender, Object target) {
			    		viewLinkPolicyDtls.click();
			    }
			};
			getActionManager().addAction(shortcutListener);
			return shortcutListener;
		}
}
