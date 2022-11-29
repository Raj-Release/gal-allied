package com.shaic.paclaim.health.reimbursement.billing.wizard.pages.bililnghosp;

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

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.leagalbilling.LegalBillingUI;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.reimbursement.billing.dto.ConsolidatedAmountDetailsDTO;
import com.shaic.claim.reimbursement.billing.wizard.AddOnBenefitsListenerTable;
import com.shaic.claim.reimbursement.billing.wizard.AddOnBenefitsPatientCareListenerTable;
import com.shaic.claim.reimbursement.billing.wizard.ConsolidatedAmountUI;
import com.shaic.claim.reimbursement.billing.wizard.HospitalizationCalcualtionUI;
import com.shaic.claim.reimbursement.billing.wizard.OtherInsurerHospSettlementAmountUI;
import com.shaic.claim.reimbursement.billing.wizard.OtherInsurerPostHospSettlementAmountUI;
import com.shaic.claim.reimbursement.billing.wizard.OtherInsurerPreHospSettlementAmountUI;
import com.shaic.claim.reimbursement.billing.wizard.PostHospitalizationCalcualtionUI;
import com.shaic.claim.reimbursement.billing.wizard.PreHospitalizationCalcualtionUI;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTable;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.Insured;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.health.reimbursement.billing.wizard.PAHealthBillingProcessButtonsUI;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billingprocess.PAHealthBillingProcessPagePopup;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PAHealthBillingHospitalizationPageUI extends ViewComponent {

	private static final long serialVersionUID = -6992464970175605792L;
	

	@Inject
	private PreauthDTO bean;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;
	
	private GWizard wizard;
	
	private TextField hospitalCashPayableAmt;
	
	private TextField patientCarePayableAmt;
	
	
	@Inject
	private Instance<PAHealthBillingProcessButtonsUI> billingProcessButtonInstance;
	
	@Inject
	private Instance<HospitalizationCalcualtionUI> hospitalizaionInstance;
	
	private BeanFieldGroup<PreauthDataExtaractionDTO> paymentbinder;
	
	
	@Inject
	private Instance<PreHospitalizationCalcualtionUI> preHospitalizationInstance;
	
	@Inject
	private Instance<PostHospitalizationCalcualtionUI> postHospitalizationInstance;
	
	private HospitalizationCalcualtionUI hospitalizaionObj;
	
	private PreHospitalizationCalcualtionUI preHospitalizationObj;
	
	private PostHospitalizationCalcualtionUI postHospitalizationObj;
	
	private PAHealthBillingProcessButtonsUI billingProcessButtonObj;

	@Inject
	private Instance<AddOnBenefitsListenerTable> addOnBenefitsListenerTable;
	
	private AddOnBenefitsListenerTable addOnBenefitsListenerTableObj;
	
	@Inject
	private Instance<AddOnBenefitsPatientCareListenerTable> addOnBenefitsPatientCareListenerTable;
	
	private AddOnBenefitsPatientCareListenerTable addOnBenefitsPatientCareLiseterObj;
	
	@Inject
	private Instance<PAHealthBillingProcessPagePopup> finacialProcessPagePopup;
	
	private PAHealthBillingProcessPagePopup finacialProcessPagePopupObj;
	
	private VerticalLayout wholeLayout;
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	@Inject
	private Instance<OtherInsurerHospSettlementAmountUI> otherInsHospInstance;
	
	@Inject
	private Instance<OtherInsurerPreHospSettlementAmountUI> otherInsPreHospInstance;
	
	@Inject
	private Instance<OtherInsurerPostHospSettlementAmountUI> otherInsPostHospInstance;
	
	private OtherInsurerHospSettlementAmountUI otherInsurerHospObj;
	
	private OtherInsurerPreHospSettlementAmountUI otherInsurerPreHospObj;

	private OtherInsurerPostHospSettlementAmountUI otherInsurerPostHospObj;
	
	private VerticalLayout otherInsSettlementTab ;

	@Inject
	private Instance<ConsolidatedAmountUI> consolidatedAmountInstance;
	
	private ConsolidatedAmountUI consolidatedAmountObj;

	private HorizontalLayout otherInsTabLayout;

	private TextField otherInsurerPreHospAmt;
	
	private OptionGroup optPaymentMode;
	
	private Button btnIFCSSearch;
	
	private TextField txtPayableAt;
	
	private TextField txtAccntNo;
	
	private VerticalLayout paymentDetailsLayout;
	
    public ComboBox cmbPayeeName;
	
	private TextField txtEmailId;
	
	private TextArea txtAreaBillingInternalRemarks;
	
	private TextField txtReasonForChange;
	
	private TextField txtPanNo;
	
	private TextField txtAccountPref;
	
	private TextField txtAccType;
	
	private Button btnAccPrefSearch;
	
	private HorizontalLayout accPrefLayout;
	
	private TextField txtRelationship;
	
	private TextField txtNameAsPerBank;
	
	private TextField txtLegalHeirFirstName;
	private TextField txtLegalHeirMiddleName;
	private TextField txtLegalHeirLastName;
	
	private TextField txtIfscCode;
	
	private TextField txtBranch;
	
	private TextField txtBankName;
	
	private TextField txtCity;
	
	private Map<String, Object> referenceData;
	
	private OptionGroup otherInsApplicable;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;


	private TextField otherInsurerPostHospAmt;
	
	private SelectValue existingPayeeName;


	private TextField otherInsurerHospAmt;
	
	@Inject
	 private PreviousAccountDetailsTable previousAccountDetailsTable ;
	 
	 private Button btnPopulatePreviousAccntDetails;
	 
	 private Window populatePreviousWindowPopup;
	 
	 private Button btnOk;
	 
	private Button btnCancel;
		 
	private VerticalLayout previousPaymentVerticalLayout;
	
	private HorizontalLayout previousAccntDetailsLayout;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	@Inject
	private Instance<BankDetailsTable> bankDetailsTableInstance;
	
	private BankDetailsTable bankDetailsTableObj;
	
	@Inject
	private Instance<LegalBillingUI> legalBillingUIInstance;
	
	private LegalBillingUI legalBillingUIObj;
	
	private VerticalLayout legalBillingLayout;
	
	@Override
	public String getCaption() {
		return "Bill Hospitalization";
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}
	
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		
		if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null){
			existingPayeeName = this.bean.getPreauthDataExtractionDetails().getPayeeName();
		}
	}
	

	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(
				PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(this.bean.getPreauthMedicalDecisionDetails());
		
		this.paymentbinder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.paymentbinder.setItemDataSource(this.bean.getPreauthDataExtractionDetails());
	}

	public Component getContent() {
		initBinder();
		addOnBenefitsListenerTableObj =  addOnBenefitsListenerTable.get();
		addOnBenefitsListenerTableObj.init();
		addOnBenefitsPatientCareLiseterObj= addOnBenefitsPatientCareListenerTable.get();
		addOnBenefitsPatientCareLiseterObj.init();
		
		paymentbinder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		
		otherInsApplicable = new OptionGroup("Other Insurer Settlement Applicable");
		otherInsApplicable.addItems(getReadioButtonOptions());
		otherInsApplicable.setItemCaption(true, "Yes");
		otherInsApplicable.setItemCaption(false, "No");
		otherInsApplicable.setStyleName("horizontal");
		
		FormLayout otherinsureform = new FormLayout(otherInsApplicable);
	
		
		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			otherInsApplicable.setEnabled(false);
		} 
		
		otherInsApplicable.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 8226297582678969878L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
					bean.getPreauthMedicalDecisionDetails().setOtherInsurerApplicable(true);
					
					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
						if(otherInsSettlementTab != null) {
							otherInsSettlementTab.addComponent(getOtherInsSettlementTab());
						}
					} else {
						if(otherInsTabLayout != null) {
							otherInsTabLayout.removeAllComponents();
						}
						if(otherInsSettlementTab != null) {
							otherInsSettlementTab.removeAllComponents();
						}
					}
					
				} else {
					bean.getPreauthMedicalDecisionDetails().setOtherInsurerApplicable(false);
					SHAUtils.resetOtherInsurerValues(bean);
					
					if(otherInsTabLayout != null) {
						otherInsTabLayout.removeAllComponents();
					}
					if(otherInsSettlementTab != null) {
						otherInsSettlementTab.removeAllComponents();
					}
					otherInsSettlementChange();
					
					if(bean.getIsReverseAllocation() && !bean.getIsReverseAllocationHappened() && (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
						if(bean.getOtherInsHospSettlementCalcDTO().getPayableAmt() != null && bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt() != null && !bean.getOtherInsHospSettlementCalcDTO().getPayableAmt().equals(bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt())) {
							bean.setIsReverseAllocationHappened(true);
						}
						
					}
					
					if(bean.getIsReverseAllocation() && bean.getIsReverseAllocationHappened()) {
						doReverseAllocationForTPA(SHAUtils.getDoubleValueFromString(bean.getAmountConsidered()));
					}
					
				}
				SHAUtils.setHospitalizationDetailsToDTOForBilling(bean);
				if(hospitalizaionObj != null) {
					hospitalizaionObj.setPresenterString(SHAConstants.PA_FINANCIAL);
					hospitalizaionObj.initView(bean);
				}
				SHAUtils.setRevisedPostHospitalizationDetailsToDTO(bean, "0");
				if(postHospitalizationObj != null) {
					postHospitalizationObj.initView(bean);
				}
				
				consolidatedTabChage();
				
			}
		});
		
		Integer hospitalCashPayableAmount = 0;
		Integer patientCarePayableAmount = 0;
		if(null != this.addOnBenefitsListenerTable)
		{
			if(null != this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList() && !this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList().isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList()) {
					
					try{
						if(null != addOnBenefitsDTO.getDateOfAdmission())
						{
						Date tempDateOfAdmission = SHAUtils.formatTimeFromString(addOnBenefitsDTO.getDateOfAdmission().toString());
						addOnBenefitsDTO.setDateOfAdmission(SHAUtils.formatDate(tempDateOfAdmission));
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					
					try{
						if(null != addOnBenefitsDTO.getDateOfDischarge())
						{
						Date tempDateOfDischarge = SHAUtils.formatTimeFromString(addOnBenefitsDTO.getDateOfDischarge().toString());
						addOnBenefitsDTO.setDateOfDischarge(SHAUtils.formatDate(tempDateOfDischarge));
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					
					if((ReferenceTable.HOSPITAL_CASH).equalsIgnoreCase(addOnBenefitsDTO.getParticulars()))
					{
						addOnBenefitsListenerTableObj.addBeanToList(addOnBenefitsDTO);
					}
					
					if(addOnBenefitsDTO.getParticulars().equalsIgnoreCase("Hospital Cash")){
						hospitalCashPayableAmount += addOnBenefitsDTO.getPayableAmount();
					} else {
						patientCarePayableAmount += addOnBenefitsDTO.getPayableAmount();
					}
				}
			}
		}
		
		if(null != this.addOnBenefitsPatientCareLiseterObj)
		{
			if(null != this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList() && !this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList().isEmpty())
			{
				for (AddOnBenefitsDTO addOnBenefitsDTO : this.bean.getPreauthDataExtractionDetails().getAddOnBenefitsDTOList()) {
					if((ReferenceTable.PATIENT_CARE).equalsIgnoreCase(addOnBenefitsDTO.getParticulars()))
							{
								addOnBenefitsPatientCareLiseterObj.addBeanToList(addOnBenefitsDTO);
							}
				}
			}
		}
		hospitalCashPayableAmt = new TextField("Hospital Cash Payable Amount");
		hospitalCashPayableAmt.setValue(hospitalCashPayableAmount.toString());
		hospitalCashPayableAmt.setReadOnly(true);
		bean.setHospitalCashAmt(hospitalCashPayableAmount.doubleValue());
		
		
		patientCarePayableAmt = new TextField("Patient Care Payable Amount");
		patientCarePayableAmt.setValue(patientCarePayableAmount.toString());
		patientCarePayableAmt.setReadOnly(true);
		bean.setPatientCareAmt(patientCarePayableAmount.doubleValue());
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(hospitalCashPayableAmt), new FormLayout(patientCarePayableAmt));
		horizontalLayout.setSpacing(true);
		
		paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Payment Details");
		paymentDetailsLayout.setSpacing(true);
		paymentDetailsLayout.setMargin(true);
		
		
		
		btnPopulatePreviousAccntDetails = new Button("Use account details from previous claim");
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		btnPopulatePreviousAccntDetails.addStyleName(ValoTheme.BUTTON_LINK);
		if(null != bean.getDocumentReceivedFromId() && (ReferenceTable.RECEIVED_FROM_HOSPITAL).equals(bean.getDocumentReceivedFromId()))
		{
			btnPopulatePreviousAccntDetails.setEnabled(false);
		}
		
		getPaymentDetailsLayout();
		
		btnOk = new Button("OK");
		//Vaadin8-setImmediate() btnOk.setImmediate(true);
		btnOk.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnOk.setWidth("-1px");
		btnOk.setHeight("-10px");
		//btnOk.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnOk.setImmediate(true);
		
		 btnCancel = new Button("CANCEL");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
	//	btnCancel.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		
		previousAccntDetailsLayout = new HorizontalLayout(btnOk,btnCancel);
		
		previousAccountDetailsTable.init("Previous Account Details", false, false);
		previousAccountDetailsTable.setPresenterString(SHAConstants.BILLING);
		previousPaymentVerticalLayout = new VerticalLayout();
		previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
		previousPaymentVerticalLayout.addComponent(previousAccntDetailsLayout);
		previousPaymentVerticalLayout.setComponentAlignment(previousAccntDetailsLayout, Alignment.TOP_CENTER);

		billingProcessButtonObj =  billingProcessButtonInstance.get();
		billingProcessButtonObj.initView(this.bean, this.wizard);
		billingProcessButtonObj.setBillingProcessPageObject(this);
		
		if(bean.getClaimDTO().getLegalClaim() !=null && bean.getClaimDTO().getLegalClaim().equals("Y")){
			legalBillingUIObj = legalBillingUIInstance.get();
			LegalBillingDTO legalBillingDTO = null;
			if(bean.getLegalBillingDTO() != null){
				legalBillingDTO = bean.getLegalBillingDTO();
			}else{
				legalBillingDTO = policyService.getLegalBillingDetails(bean);
			}
			legalBillingUIObj.setLegalBillingDTO(legalBillingDTO);
			legalBillingUIObj.initView(bean,SHAConstants.PA_BILLING_HOSP);

			legalBillingLayout = new VerticalLayout();
			legalBillingLayout.setCaption("Legal Billing");
			legalBillingLayout.setCaptionAsHtml(true);
			legalBillingLayout.addComponent(legalBillingUIObj);
			legalBillingLayout.setSpacing(true);
			legalBillingLayout.setMargin(true);
		}else
		{
			legalBillingLayout = new VerticalLayout();
			legalBillingLayout.setVisible(false);
		}


		wholeLayout = new VerticalLayout(builBillSummaryTabs(), addOnBenefitsListenerTableObj,  addOnBenefitsPatientCareLiseterObj, legalBillingLayout, horizontalLayout, paymentDetailsLayout,billingProcessButtonObj);

		if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null) {
			otherInsApplicable.setValue(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable());
			otherInsApplicable.select(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable());
			if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable()) {
				if(otherInsTabLayout != null) {
					otherInsTabLayout.removeAllComponents();
				}
				if(otherInsSettlementTab != null) {
					otherInsSettlementTab.removeAllComponents();
				}
				if(otherInsSettlementTab != null) {
					otherInsSettlementTab.addComponent(getOtherInsSettlementTab());
				}
			}
		}
		 addListenerForBenefits();
		 addPreviousPaymentPopupListener();
		 setTableValues();
		 showOrHideValidation(false);
		 if(consolidatedAmountObj !=null
				 && legalBillingUIObj !=null ){
			 legalBillingUIObj.setawardAmount(consolidatedAmountObj.getTotalConsolidatedAmt());
		 }
		 return wholeLayout;
	}
	
	@SuppressWarnings("deprecation")
	private TabSheet builBillSummaryTabs() {
		TabSheet billSummaryTab = new TabSheet();
		//Vaadin8-setImmediate() billSummaryTab.setImmediate(true);
		// previousClaimTab.setWidth("100.0%");
		// previousClaimTab.setHeight("100.0%");
		billSummaryTab.setSizeFull();
		billSummaryTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		TabSheet hospitalizationTab = getHospitalizationTab();
		billSummaryTab.setHeight("100.0%");
		billSummaryTab.addTab(hospitalizationTab, "Hospitalization", null);

		// tabSheet_2
		TabSheet preHospitalizationTab = getPreHospitalizationTab();
		billSummaryTab.addTab(preHospitalizationTab, "Pre-Hospitalization", null);

		TabSheet postHospitalizationTab = getPostHospitalizatonTab();
		billSummaryTab.addTab(postHospitalizationTab, "Post-Hospitalization", null);
//		if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED) && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)) {
		if(false) {
			otherInsSettlementTab = new VerticalLayout();
			otherInsSettlementTab.setCaption("Other Insurer Settlement");
//				otherInsSettlementTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
//					private static final long serialVersionUID = 460697828791510201L;
	//
//					@Override
//					public void selectedTabChange(SelectedTabChangeEvent event) {
//						otherInsSettlementChange();
//					}
//				});
			billSummaryTab.addTab(otherInsSettlementTab, "Other Insurer Settlement", null);
		}
			
			VerticalLayout consolidatedAmountTab = getConsolidatedAmountTab();
			billSummaryTab.addTab(consolidatedAmountTab, "Consolidated Amount", null);
		
			billSummaryTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
				
				@Override
				public void selectedTabChange(SelectedTabChangeEvent event) {
					if(event.getTabSheet().getSelectedTab().getCaption() != null) {
						if(event.getTabSheet().getSelectedTab().getCaption().equalsIgnoreCase("Other Insurer Settlement")) {
							otherInsSettlementChange();
						} else if(event.getTabSheet().getSelectedTab().getCaption().equalsIgnoreCase("Consolidated Amount")) {
							consolidatedTabChage();
						}
					}
					
				}
			});
		

		
		
		return billSummaryTab;
	}
	
	private TabSheet getHospitalizationTab(){
		TabSheet hospitalizationTab = new TabSheet();
		hospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);
		hospitalizationTab.setWidth("100%");
		hospitalizationTab.setHeight("100%");
		hospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() hospitalizationTab.setImmediate(true);
		
		hospitalizaionObj = hospitalizaionInstance.get();
		hospitalizaionObj.setPresenterString(SHAConstants.PA_FINANCIAL);
		hospitalizaionObj.initView(this.bean);
		
		TextField field = new TextField("Amount Claimed (Hospitalisation)");
		field.setEnabled(false);
		field.setValue("");
		if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalizationClaimedAmount() != null) {
			field.setValue(String.valueOf(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getHospitalizationClaimedAmount().longValue()) );
		}
//		otherInsurerHospAmt = (TextField) binder.buildAndBind("Amount Claimed From Other Insurer (Hosp)",
//				"otherInsurerHospAmountClaimed", TextField.class);
//		CSValidator hospValidator = new CSValidator();
//		hospValidator.extend(otherInsurerHospAmt);
//		hospValidator.setRegExp("^[0-9]*$");
//		hospValidator.setPreventInvalidTyping(true);
//		otherInsurerHospAmt.setNullRepresentation("");
//		FormLayout formLayout = new FormLayout(otherInsurerHospAmt);
		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(field));
		horizontalLayout.setWidth("100%");
//		horizontalLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_RIGHT);
		VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, hospitalizaionObj);
		verticalLayout.setSpacing(true);
		hospitalizationTab.addComponent(verticalLayout);
		
		return hospitalizationTab;
	}
	
	private TabSheet getPreHospitalizationTab(){
		TabSheet preHospitalizationTab = new TabSheet();
		preHospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() preHospitalizationTab.setImmediate(true);
		preHospitalizationTab.setWidth("100%");
		preHospitalizationTab.setHeight("100%");
		preHospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() preHospitalizationTab.setImmediate(true);
		
		otherInsurerPreHospAmt = (TextField) binder.buildAndBind("Amount Claimed From Other Insurer (Pre-Hosp)",
				"otherInsurerPreHospAmountClaimed", TextField.class);
		otherInsurerPreHospAmt.setValue(String.valueOf(SHAUtils.getDoubleValueFromString(bean.getPreauthMedicalDecisionDetails().getOtherInsurerPreHospAmountClaimed()).intValue()) );
		SHAUtils.setPreHospitalizationDetailsToDTO(bean, otherInsurerPreHospAmt.getValue());
		CSValidator preHospValidator = new CSValidator();
		preHospValidator.extend(otherInsurerPreHospAmt);
		preHospValidator.setRegExp("^[0-9]*$");
		preHospValidator.setPreventInvalidTyping(true);
		otherInsurerPreHospAmt.setNullRepresentation("");
		otherInsurerPreHospAmt.addValueChangeListener(new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -2777617364408974206L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(bean.getPreHospitalizaionFlag()) {
					SHAUtils.setPreHospitalizationDetailsToDTO(bean, otherInsurerPreHospAmt.getValue());
					preHospitalizationObj.initView(bean);
				}
				
				
			}
		});
		
		 preHospitalizationObj = preHospitalizationInstance.get();
		 preHospitalizationObj.initView(this.bean);
		
		 TextField field = new TextField("Amount Claimed (Pre-Hosp)");
			field.setEnabled(false);
			field.setValue("");
			if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPreHospitalizationClaimedAmount() != null) {
				field.setValue(String.valueOf(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPreHospitalizationClaimedAmount().longValue()) );
			}
			
			
			FormLayout formLayout = new FormLayout(otherInsurerPreHospAmt);
			HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(field), formLayout);
			horizontalLayout.setWidth("100%");
			horizontalLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_RIGHT);
			VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, preHospitalizationObj);
			verticalLayout.setSpacing(true);
		 
		preHospitalizationTab.addComponent(verticalLayout);
		
		return preHospitalizationTab;
		
	}
	
	private TabSheet getPostHospitalizatonTab(){
		TabSheet postHospitalizationTab = new TabSheet();
		postHospitalizationTab.hideTabs(true);
		//Vaadin8-setImmediate() postHospitalizationTab.setImmediate(true);
		postHospitalizationTab.setWidth("100%");
		postHospitalizationTab.setHeight("100%");
		postHospitalizationTab.setSizeFull();
		//Vaadin8-setImmediate() postHospitalizationTab.setImmediate(true);
		
		otherInsurerPostHospAmt = (TextField) binder.buildAndBind("Amount Claimed From Other Insurer (Post-Hosp)",
				"otherInsurerPostHospAmountClaimed", TextField.class);
		otherInsurerPostHospAmt.setValue(String.valueOf(SHAUtils.getDoubleValueFromString(bean.getPreauthMedicalDecisionDetails().getOtherInsurerPostHospAmountClaimed()).intValue()) );
		SHAUtils.setPostHospitalizationDetailsToDTO(bean, otherInsurerPostHospAmt.getValue());
		CSValidator postHospValidator = new CSValidator();
		postHospValidator.extend(otherInsurerPostHospAmt);
		postHospValidator.setRegExp("^[0-9]*$");
		postHospValidator.setPreventInvalidTyping(true);
		otherInsurerPostHospAmt.setNullRepresentation("");
		otherInsurerPostHospAmt.addValueChangeListener(new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -2777617364408974206L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(bean.getPostHospitalizaionFlag()) {
					SHAUtils.setPostHospitalizationDetailsToDTO(bean, otherInsurerPostHospAmt.getValue());
					postHospitalizationObj.initView(bean);
				}
			
				
			}
		});
		
		 postHospitalizationObj = postHospitalizationInstance.get();
		 postHospitalizationObj.initView(this.bean);
		 TextField field = new TextField("Amount Claimed (Post-Hosp)");
			field.setEnabled(false);
			field.setValue("");
			if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPostHospitalizationClaimedAmount() != null) {
				field.setValue(String.valueOf(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getPostHospitalizationClaimedAmount().longValue()) );
			}
			
			
			FormLayout formLayout = new FormLayout(otherInsurerPostHospAmt);
			HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(field), formLayout);
			horizontalLayout.setWidth("100%");
			horizontalLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_RIGHT);
			VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout, postHospitalizationObj);
			verticalLayout.setSpacing(true);
			
			postHospitalizationTab.addComponent(verticalLayout);
		
		return postHospitalizationTab;
	}
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	public void generateButtonFields(String eventName, BeanItemContainer<SelectValue> selectValueContainer) {
		this.bean.setStageKey(ReferenceTable.BILLING_STAGE);
		if(SHAConstants.REFER_TO_COORDINATOR.equalsIgnoreCase(eventName)) {
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_COORDINATOR)) {
				this.bean.getPreauthMedicalDecisionDetails().setTypeOfCoordinatorRequest(null);
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_REFER_TO_COORDINATOR);
			billingProcessButtonObj.buildReferCoordinatorLayout(selectValueContainer);
		} else if(SHAConstants.MEDICAL_APPROVER.equalsIgnoreCase(eventName)) {
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER);
			billingProcessButtonObj.buildReferToMedicalApproverLayout();
		} else if(SHAConstants.FINANCIAL.equalsIgnoreCase(eventName)) {
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
			if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (bean.getHospitalizaionFlag())) {
				Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
				bean.setUniquePremiumAmount(uniqueInstallmentAmount.doubleValue());
				Double premiumAmt = bean.getHospitalizationCalculationDTO().getPreauthAppAmt() + uniqueInstallmentAmount.doubleValue();
				if(bean.getHospitalizationCalculationDTO().getNetPayableAmt() < (premiumAmt.intValue())) {
					bean.setShouldDetectPremium(false);
					alertMessageUniquePremium();
				} else {
					bean.setShouldDetectPremium(true);
					billingProcessButtonObj.buildSendToFinancialLayout();
				}
			} else {
				bean.setShouldDetectPremium(true);
				billingProcessButtonObj.buildSendToFinancialLayout();
			}
			
		} else if(SHAConstants.BILLING_CANCEL_ROD.equalsIgnoreCase(eventName)){
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_CANCEL_ROD)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_CANCEL_ROD);
			billingProcessButtonObj.buildCancelRODLayout(selectValueContainer);
		}else if(SHAConstants.REFER_TO_BILL_ENTRY.equalsIgnoreCase(eventName)){
			if(this.bean.getStatusKey() != null && !this.bean.getStatusKey().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY)) {
				this.bean.getPreauthMedicalDecisionDetails().setReasonForRefering("");
				this.bean.getPreauthMedicalDecisionDetails().setReasonForReferring("");
			}
			this.bean.setStatusKey(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY);
			billingProcessButtonObj.buildReferToBillEntryLayout();
		}
		
	}
	
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		
		SelectValue patientStatus = this.bean.getPreauthDataExtractionDetails().getPatientStatus();
		if(patientStatus != null 
				&& !((ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))
						&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey()))) {
		
			if(optPaymentMode.getValue() != null 
					&& !(Boolean)optPaymentMode.getValue()
					&& bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
					&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
					&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())){
				
				txtAccntNo.setValue(dto.getAccNumber());
				txtAccountPref.setValue(dto.getAccPreference());
				txtAccType.setValue(dto.getAccType());
				txtNameAsPerBank.setValue(dto.getPayeeName());
				this.bean.setDto(dto);
				
				txtBankName.setReadOnly(false);
				txtBankName.setValue(dto.getBankName());
				txtBankName.setReadOnly(true);
				
				txtBranch.setReadOnly(false);
				txtBranch.setValue(dto.getBranchName());
				txtBranch.setReadOnly(true);
				
				txtCity.setReadOnly(false);
				txtCity.setValue(dto.getCity());
				txtCity.setReadOnly(true);
				
				txtIfscCode.setReadOnly(false);
				txtIfscCode.setValue(dto.getIfscCode());
				txtIfscCode.setReadOnly(true);
				
				this.bean.getPreauthDataExtractionDetails().setAccountNo(dto.getAccNumber());
				this.bean.getPreauthDataExtractionDetails().setAccountPref(dto.getAccPreference());
				this.bean.getPreauthDataExtractionDetails().setAccType(dto.getAccType());
				this.bean.getPreauthDataExtractionDetails().setNameAsPerBank(dto.getPayeeName());
				this.bean.getPreauthDataExtractionDetails().setBankName(dto.getBankName());
				this.bean.getPreauthDataExtractionDetails().setBranch(dto.getBranchName());
				this.bean.getPreauthDataExtractionDetails().setCity(dto.getCity());
				this.bean.getPreauthDataExtractionDetails().setIfscCode(dto.getIfscCode());
				
			}
			else{
			
				txtIfscCode.setReadOnly(false);
				txtIfscCode.setValue(dto.getIfscCode());
				txtIfscCode.setReadOnly(true);
				
				txtBankName.setReadOnly(false);
				txtBankName.setValue(dto.getBankName());
				txtBankName.setReadOnly(true);
				
				txtBranch.setReadOnly(false);
				txtBranch.setValue(dto.getBranchName());
				txtBranch.setReadOnly(true);
				
				txtCity.setReadOnly(false);
				txtCity.setValue(dto.getCity());
				txtCity.setReadOnly(true);
				
				if(null != this.bean.getPreauthDataExtractionDetails()){
					this.bean.getPreauthDataExtractionDetails().setBankId(dto.getBankId());
					this.bean.setBankId(dto.getBankId());
				}
			}
		}
		else{
			
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(dto.getIfscCode());
			txtIfscCode.setReadOnly(true);
				
			txtBankName.setReadOnly(false);
			txtBankName.setValue(dto.getBankName());
			txtBankName.setReadOnly(true);
				
			txtBranch.setReadOnly(false);
			txtBranch.setValue(dto.getBranchName());
			txtBranch.setReadOnly(true);
				
			txtCity.setReadOnly(false);
			txtCity.setValue(dto.getCity());
			txtCity.setReadOnly(true);
				
			if(null != this.bean.getPreauthDataExtractionDetails()){
				this.bean.getPreauthDataExtractionDetails().setBankId(dto.getBankId());
				this.bean.setBankId(dto.getBankId());
			}
		}	

	}
	
	public boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";		
		if(!this.binder.isValid()) {
			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
				}
				hasError = true;
			}
		}
		
		if (!this.paymentbinder.isValid()) {

			for (Field<?> field : this.paymentbinder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
				}
				hasError = true;
			}
		}
		

		SelectValue patientStatus = this.bean.getPreauthDataExtractionDetails().getPatientStatus();
		if(patientStatus != null 
				&& !((this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
						&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
						&& patientStatus != null 
						&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))))) {
		
			if(null != bean.getDocumentReceivedFromId() && ! (ReferenceTable.RECEIVED_FROM_HOSPITAL).equals(bean.getDocumentReceivedFromId()))
			{
			
				if(cmbPayeeName != null){
					SelectValue selected = (SelectValue)cmbPayeeName.getValue();
					if(existingPayeeName != null && selected != null 
							&& ! existingPayeeName.getValue().equalsIgnoreCase(selected.getValue())){
						if(txtReasonForChange != null && txtReasonForChange.getValue() == null || txtReasonForChange.getValue().isEmpty()){
							hasError = true;
							eMsg += "Please Enter Reason for changing payee name</br>";
						}
					}
				}
				if(null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag() 
						&& ReferenceTable.PAYMENT_MODE_CHEQUE_DD.equals(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag())) {
					if(txtPayableAt != null && txtPayableAt.getValue() == null || txtPayableAt.getValue().isEmpty()){
						hasError = true;
						eMsg += "Please Select Payable At</br>";
					}
				}
			
				if(optPaymentMode.getValue() != null 
						&& !(Boolean)optPaymentMode.getValue()
						&& bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())) {
					if(txtAccountPref != null && txtAccountPref.getValue() == null || txtAccountPref.getValue().isEmpty()){
						hasError = true;
						eMsg += "Please Select Account Preference</br>";
					}
				}
			}		
			
			if(null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag() 
					&& ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag())) {
				if(txtAccntNo != null && txtAccntNo.getValue() == null || txtAccntNo.getValue().isEmpty()){
					hasError = true;
					eMsg += "Please Enter Account Number</br>";
				}
				
				if(txtIfscCode != null && txtIfscCode.getValue() == null || txtIfscCode.getValue().isEmpty()){
					hasError = true;
					eMsg += "Please Enter IFSC Code</br>";
				}
			}
		
		}
		
		if(!this.bean.getIsScheduleClicked()){
			hasError = true;
			eMsg += "Please Verify Policy Schedule Button.</br>";
		}
		
		if(this.billingProcessButtonObj != null && !this.billingProcessButtonObj.isValid()) {
			hasError = true;
			List<String> errors = this.billingProcessButtonObj.getErrors();
			for (String error : errors) {
				eMsg += error + "</br>";
			   }
		}
		if(legalBillingUIObj != null){		
			String errmsg = legalBillingUIObj.isValid();
			if(errmsg !=null){
				hasError = true;
				eMsg += errmsg;
			}//IMSSUPPOR-32607 changes done for this support fix
			else if(legalBillingUIObj.getinterestApplicable() && legalBillingUIObj.getPanDetails()) {
				if(txtPanNo.getValue() == null || txtPanNo.getValue().isEmpty()){
					hasError = true;
					eMsg += "Please Enter Pan Number For Legal Settlement.</br>" ;
				}	
			}			
		}
		
		
		if (hasError) {
			setRequired(true);
			/*Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
				this.paymentbinder.commit();
				bindValues();
				consolidatedTabChage();
				bean.setHospDiscountAmount(bean.getHospitalizationCalculationDTO().getHospitalDiscount() != null ? bean.getHospitalizationCalculationDTO().getHospitalDiscount().doubleValue() : 0d);
				if(this.bean.getPreauthDataExtractionDetails().getAccountNo() != null){
					this.bean.setAccountNumber(this.bean.getPreauthDataExtractionDetails().getAccountNo());
				}
				
				if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
					if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {

						if(bean.getHospitalizationCalculationDTO().getHospitalDiscount() > 0 && (!bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().equals(!bean.getIsReconsiderationRequest() ? (bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? bean.getUniquePremiumAmount() : 0) + bean.getPayableToInsAmt()) : (bean.getHospitalizationCalculationDTO().getBalanceToBePaid() + bean.getPayableToInsAmt()) ))) {
							bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
							bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
							bean.setUniqueDeductedAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getUniqueDeductedAmount())  : 0d);
							
							
							if(bean.getIsReverseAllocationHappened() && (ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
								Double plusAmt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() + (bean.getShouldDetectPremium() ? bean.getUniquePremiumAmount() : 0);
								if((plusAmt).equals(!bean.getIsReconsiderationRequest() ? (bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? bean.getUniquePremiumAmount() : 0) + bean.getPayableToInsAmt()) : (bean.getHospitalizationCalculationDTO().getBalanceToBePaid() + (bean.getShouldDetectPremium() ? bean.getUniquePremiumAmount() : 0) + bean.getPayableToInsAmt()) )) {
									bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
									bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
									return true;
								}
							}
							
//								if(!(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && bean.getHospitalizaionFlag() && bean.getShouldDetectPremium())) {
							if(bean.getIsReconsiderationRequest()) {
								return doReverseAllocation((bean.getHospitalizationCalculationDTO().getBalanceToBePaid() + (bean.getShouldDetectPremium() ? bean.getUniquePremiumAmount().intValue() : 0)) + bean.getPayableToInsAmt());
							} else {
								return doReverseAllocation(((bean.getHospitalizationCalculationDTO().getAfterHospitalDiscount().doubleValue() + (bean.getShouldDetectPremium() ? bean.getUniquePremiumAmount() : 0)) + bean.getPayableToInsAmt()));
							}
						}
					} else if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
						if(otherInsApplicable != null && otherInsApplicable.getValue() != null && otherInsApplicable.getValue().toString() == "true") {
							if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat()) {
								if(!bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().equals(otherInsurerHospObj != null ? SHAUtils.getDoubleFromStringWithComma(otherInsurerHospObj.getPayableAmount()) : 0d)) {
									bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
									bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
									return doReverseAllocation(SHAUtils.getDoubleFromStringWithComma(otherInsurerHospObj.getPayableAmount()));
								}
							} 
						} 
//						else if(bean.getIsReverseAllocation() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat())) {
//							if(!SHAUtils.getIntegerFromStringWithComma(bean.getReverseAmountConsidered()).equals(bean.getIsReconsiderationRequest() ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() :  bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt()) ) {
//								bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
//								bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
//								if((bean.getIsReconsiderationRequest() ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() :  bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt()).equals(SHAUtils.getIntegerFromStringWithComma(bean.getAmountConsidered()))) {
//									bean.setIsReverseAllocation(false);
//								}
//								return doReverseAllocation(SHAUtils.getDoubleFromStringWithComma(bean.getIsReconsiderationRequest() ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() != null ? String.valueOf(bean.getHospitalizationCalculationDTO().getBalanceToBePaid()) : "0" : String.valueOf(bean.getHospitalizationCalculationDTO().getPayableToInsuredAftPremiumAmt()) ));
//							}
//							
//						}
					}
				}
				if(legalBillingUIObj !=null){
					bean.setLegalBillingDTO(legalBillingUIObj.getvalue());
					if(legalBillingUIObj.getPanDetails()){
						if(bean.getLegalBillingDTO() !=null && bean.getLegalBillingDTO().getPanNo() == null){
							bean.getLegalBillingDTO().setPanNo(txtPanNo.getValue());
						}
					}	
				}
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bean.setRevisedProvisionAmount(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getProvisionAmount())  : 0d);
			bean.setTotalConsolidatedAmt(consolidatedAmountObj != null ? SHAUtils.getDoubleFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt())  : 0d);
			return true;
		}
	}

	private boolean doReverseAllocation(final Double reverseAllocatedAmount) {
		Button popuupOkBtn = new Button("Ok");
		
		Label label = new Label("<b style = 'color:red'>Hospitalisation Approved amount is lesser than the Eligible amount. Please do Reverse allocation.</b>", ContentMode.HTML);
		
		VerticalLayout popupLayout = new VerticalLayout(label, popuupOkBtn);
		popupLayout.setComponentAlignment(popuupOkBtn, Alignment.BOTTOM_CENTER);
		popupLayout.setSpacing(true);
		final Window popupDialog = new Window();
		popupDialog.setCaption("Errors");
		popupDialog.setClosable(true);
		popupDialog.setContent(popupLayout);
		popupDialog.setResizable(false);
		popupDialog.setModal(true);
		UI.getCurrent().addWindow(popupDialog);
//						popupDialog.show(getUI().getCurrent(), null, true);
		
		popuupOkBtn.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popupDialog.close();
				bean.setIsReverseAllocation(true);
				bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(reverseAllocatedAmount);
				if(finacialProcessPagePopupObj == null) {
					finacialProcessPagePopupObj = finacialProcessPagePopup.get();
				}
				finacialProcessPagePopupObj.init(bean, wizard);
				
				Button okButton = new Button("Ok");
				okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				Component content = finacialProcessPagePopupObj.getContent();
				finacialProcessPagePopupObj.setupReferences(referenceData);
				VerticalLayout layout = new VerticalLayout(content, okButton);
			    layout.setComponentAlignment(okButton, Alignment.BOTTOM_LEFT);
				layout.setMargin(true);
				layout.setSpacing(true);
				final Window dialog = new Window();
				dialog.setCaption("Medical Decision Table for Reverse allocation.");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setWidth("90%");
				dialog.setModal(true);
				UI.getCurrent().addWindow(dialog);
//								dialog.show(getUI().getCurrent(), null, true);
				
				okButton.addClickListener(new Button.ClickListener() {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						if(finacialProcessPagePopupObj.validatePage()) {
							bean.setIsReverseAllocationHappened(true);
							dialog.close();
						}
					}
				});
			}
		});
		return false;
	}
	
	private boolean doReverseAllocationForTPA(final Double reverseAllocatedAmt) {
		Button popuupOkBtn = new Button("Ok");
		bean.setIsReverseAllocation(false);
		Label label = new Label("<b style = 'color:red'>Other Insurer Amount has been cleared. Please do reverse allocation to Hopitlalization bill amount.</b>", ContentMode.HTML);
		
		VerticalLayout popupLayout = new VerticalLayout(label, popuupOkBtn);
		popupLayout.setComponentAlignment(popuupOkBtn, Alignment.BOTTOM_CENTER);
		popupLayout.setSpacing(true);
		final Window popupDialog = new Window();
		popupDialog.setCaption("Errors");
		popupDialog.setClosable(true);
		popupDialog.setContent(popupLayout);
		popupDialog.setResizable(false);
		popupDialog.setModal(true);
		UI.getCurrent().addWindow(popupDialog);
//						popupDialog.show(getUI().getCurrent(), null, true);
		
		popuupOkBtn.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popupDialog.close();
//				bean.setIsReverseAllocation(true);
//				bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(reverseAllocatedAmt);
				if(finacialProcessPagePopupObj == null) {
					finacialProcessPagePopupObj = finacialProcessPagePopup.get();
				}
				finacialProcessPagePopupObj.init(bean, wizard);
				
				Button okButton = new Button("Ok");
				okButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				
				Component content = finacialProcessPagePopupObj.getContent();
				finacialProcessPagePopupObj.setupReferences(referenceData);
				VerticalLayout layout = new VerticalLayout(content, okButton);
			    layout.setComponentAlignment(okButton, Alignment.BOTTOM_LEFT);
				layout.setMargin(true);
				layout.setSpacing(true);
				final Window dialog = new Window();
				dialog.setCaption("Medical Decision Table for Reverse allocation.");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.center();
				dialog.setWidth("90%");
				dialog.setModal(true);
				UI.getCurrent().addWindow(dialog);
//								dialog.show(getUI().getCurrent(), null, true);
				
				okButton.addClickListener(new Button.ClickListener() {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						if(finacialProcessPagePopupObj.validatePage()) {
							bean.setIsReverseAllocationHappened(false);
							if(hospitalizaionObj != null) {
								SHAUtils.setHospitalizationDetailsToDTOForFinancial(bean);
								hospitalizaionObj.setPresenterString(SHAConstants.PA_FINANCIAL);
								hospitalizaionObj.initView(bean);
								consolidatedTabChage();
							}
							dialog.close();
						}
					}
				});
			}
		});
		return false;
	}
	
	public void alertMessage() {
		Label successLabel = new Label();
		
		if(this.bean.getStatusKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
			
			if(!bean.getIsPreHospApplicable() && !bean.getIsPostHospApplicable()) {
				successLabel = new Label(
						"<b style = 'color: red;'> Pre and Post Hospitalization are not applicable for this Product. Hence this ROD will be rejected in Financial Stage. !!!</b>",
						ContentMode.HTML);
			} else if(!bean.getIsPreHospApplicable()){
				successLabel = new Label(
						"<b style = 'color: red;'> Pre Hospitalization is not applicable for this Product. Hence this ROD will be rejected in Financial Stage. !!!</b>",
						ContentMode.HTML);
			} else if(!bean.getIsPostHospApplicable()){
				successLabel = new Label(
						"<b style = 'color: red;'> Post Hospitalization is not applicable for this Product. Hence this ROD will be rejected in Financial Stage. !!!</b>",
						ContentMode.HTML);
			}
		}
   		

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
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
				
			    bean.setAlertMessageOpened(true);
			    wizard.next();
			    dialog.close();

			}
		});

	}
	
	private void getPaymentDetailsLayout()
	{
		unbindField(optPaymentMode);
		SelectValue patientStatus = this.bean.getPreauthDataExtractionDetails().getPatientStatus();
		
		optPaymentMode = (OptionGroup) paymentbinder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
		
		if(this.bean.getDocumentReceivedFromId() != null 
				&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
			optPaymentMode.setEnabled(true);
		}
		
		
		paymentModeListener();	
	//	//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		
		if(this.bean.getDocumentReceivedFromId() != null 
				&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
				&& patientStatus != null 
				&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))
				&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
			
			optPaymentMode.setRequired(false);
			optPaymentMode.setEnabled(false);
		}
		else{
			optPaymentMode.setRequired(true);
			optPaymentMode.setEnabled(true);
		}
		
		optPaymentMode.addItems(getReadioButtonOptions());
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		//optPaymentMode.select(true);
		//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		
		

		if(null != this.bean.getPreauthDataExtractionDetails() && null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag() &&
				(ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag()))
		{
			optPaymentMode.setValue(true);
		}
		else
		{
			optPaymentMode.setValue(false);
		}
		


		 if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())
				 && this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
		{
			 optPaymentMode.setReadOnly(true);
			 optPaymentMode.setEnabled(false);
			 if(btnIFCSSearch != null){
				 btnIFCSSearch.setEnabled(false);
			 }
		}else{
			optPaymentMode.setReadOnly(false);
			optPaymentMode.setEnabled(true);
			if(btnIFCSSearch != null){
				btnIFCSSearch.setEnabled(true);
			}
			if(txtPayableAt != null){
				txtPayableAt.setReadOnly(false);
				txtPayableAt.setEnabled(true);
			}
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
						&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())){
				
					if(txtAccntNo != null)
						txtAccntNo.setEnabled(false);
				}else {	
					if(txtAccntNo != null)
						txtAccntNo.setEnabled(true);
				}	
			}
			
		}
		
		//buildPaymentsLayout();
	}
	
	@SuppressWarnings({ "serial", "deprecation" })
	private void paymentModeListener()
	{
		optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = -1774887765294036092L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean value = (Boolean) event.getProperty().getValue();
				
				if (null != paymentDetailsLayout && paymentDetailsLayout.getComponentCount() > 0) 
				{
					paymentDetailsLayout.removeAllComponents();
				}
				if(value)
				{
					unbindField(getListOfPaymentFields());
					bean.getPreauthDataExtractionDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout(value));
				//	bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					if(null != txtAccntNo)
					{
						mandatoryFields.remove(txtAccntNo);
						if(null != txtAccntNo.getValue() )
						{
						bean.getPreauthDataExtractionDetails().setAccountNo(txtAccntNo.getValue());
						}
						
					}
					if(null != txtIfscCode)
					{
						mandatoryFields.remove(txtIfscCode);
						if(null != txtIfscCode.getValue() )
						{
						bean.getPreauthDataExtractionDetails().setIfscCode(txtIfscCode.getValue());
						}
					}
					
				}
				else 
				{

					unbindField(getListOfPaymentFields());
					bean.getPreauthDataExtractionDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
					paymentDetailsLayout.addComponent(btnPopulatePreviousAccntDetails);
					paymentDetailsLayout.setComponentAlignment(btnPopulatePreviousAccntDetails, Alignment.TOP_RIGHT);
					paymentDetailsLayout.addComponent(buildChequePaymentLayout(value));
					paymentDetailsLayout.addComponent(buildBankTransferLayout());
					//bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}				
			}
		});
		
		
		
		/*
		optPaymentMode.addListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub

				Boolean value = (Boolean) event.getProperty().getValue();
				
				if (null != paymentDetailsLayout && paymentDetailsLayout.getComponentCount() > 0) 
				{
					paymentDetailsLayout.removeAllComponents();
				}
				if(value)
				{
					unbindField(getListOfPaymentFields());
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					
				}
				else 
				{

					unbindField(getListOfPaymentFields());
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					paymentDetailsLayout.addComponent(buildBankTransferLayout());
					bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}				
				
			}
		});*/
	}
	
	private HorizontalLayout buildBankTransferLayout()
	{
		
		btnIFCSSearch = new Button();
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));
		
		addIFSCCodeListner();
		
		txtAccntNo = (TextField)paymentbinder.buildAndBind("Account No" , "accountNo", TextField.class);
//		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");
		
		if(null != this.bean.getPreauthDataExtractionDetails().getAccountNo())
		{
			txtAccntNo.setValue(this.bean.getPreauthDataExtractionDetails().getAccountNo());
		}
		

		CSValidator accntNoValidator = new CSValidator();
		accntNoValidator.extend(txtAccntNo);
		accntNoValidator.setRegExp("^[a-z A-Z 0-9]*$");
		accntNoValidator.setPreventInvalidTyping(true);
		
		
		txtIfscCode = (TextField) paymentbinder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
//		txtIfscCode.setRequired(true);
		txtIfscCode.setNullRepresentation("");
		txtIfscCode.setEnabled(false);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getIfscCode())
		{
			txtIfscCode.setValue(this.bean.getPreauthDataExtractionDetails().getIfscCode());
		}
		
		txtBranch = (TextField) paymentbinder.buildAndBind("Branch", "branch", TextField.class);
		txtBranch.setNullRepresentation("");
		txtBranch.setEnabled(false);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getBranch())
		{
			txtBranch.setValue(this.bean.getPreauthDataExtractionDetails().getBranch());
		}
		
		txtBankName = (TextField) paymentbinder.buildAndBind("Bank Name", "bankName", TextField.class);
		txtBankName.setNullRepresentation("");
		txtBankName.setEnabled(false);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getBankName())
		{
			txtBankName.setValue(this.bean.getPreauthDataExtractionDetails().getBankName());
		}
		
		txtCity = (TextField) paymentbinder.buildAndBind("City", "city", TextField.class);
		txtCity.setNullRepresentation("");
		txtCity.setEnabled(false);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getCity())
		{
			txtCity.setValue(this.bean.getPreauthDataExtractionDetails().getCity());
		}
		
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			txtAccntNo.setReadOnly(true);
			txtAccntNo.setEnabled(false);
			
			txtIfscCode.setReadOnly(true);
			txtIfscCode.setEnabled(false);
			
			txtBranch.setReadOnly(true);
			txtBranch.setEnabled(false);
			
			txtBankName.setReadOnly(true);
			txtBankName.setEnabled(false);
			
			
			txtCity.setReadOnly(true);
			txtCity.setEnabled(false);
			
		}
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())
				&& this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED))
		{	
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
						&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())){
				
					if(txtAccntNo != null)
						txtAccntNo.setEnabled(false);
				}
				else {
					if(txtAccntNo != null)
						txtAccntNo.setEnabled(true);
				}
			}
			if(txtIfscCode != null){
				txtIfscCode.setReadOnly(false);
				txtIfscCode.setEnabled(true);
			}
			
		}
		
		
		SelectValue patientStatus = this.bean.getPreauthDataExtractionDetails().getPatientStatus();
		if(this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			if(patientStatus != null 
					&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))
					&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
				txtAccntNo.setEnabled(false);
			}
			else {
				if(txtAccntNo != null)
					txtAccntNo.setEnabled(true);
			}
		}
		/*TextField dField1 = new TextField();
		dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField1.setReadOnly(true);
		TextField dField2 = new TextField();
		dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField2.setReadOnly(true);
		TextField dField3 = new TextField();
		dField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField3.setReadOnly(true);
		TextField dField4 = new TextField();
		dField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField4.setReadOnly(true);
		TextField dField5 = new TextField();
		dField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField5.setReadOnly(true);
		dField5.setWidth(2,Unit.CM);
		FormLayout formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtAccntNo,txtIfscCode,txtBankName,txtCity);
		FormLayout formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,dField1,txtBranch);
		HorizontalLayout btnHLayout = new HorizontalLayout(dField5,btnIFCSSearch);
		VerticalLayout btnLayout = new VerticalLayout(btnHLayout,dField2,dField3,dField4);
		HorizontalLayout hLayout = new HorizontalLayout(formLayout1 ,btnLayout,formLayout2);
		hLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);*/
		
		if(this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			if(patientStatus != null 
					&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))
					&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
				btnIFCSSearch.setEnabled(false);
			}
			else {
				btnIFCSSearch.setEnabled(true);
			}
		}
		
		
		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,txtIfscCode,txtBankName,txtCity);
		
		TextField dField = new TextField();
		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField.setReadOnly(true);
		dField.setWidth("30px");
		
		
		TextField dField1 = new TextField();
		dField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField1.setReadOnly(true);
		
		TextField dField2 = new TextField();
		dField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField2.setReadOnly(true);
		
		FormLayout bankTransferLayout2 = new FormLayout(dField,btnIFCSSearch);
		FormLayout bankTransferLayout3 = new FormLayout(dField1,dField2,txtBranch);
		
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , bankTransferLayout2,bankTransferLayout3);
		hLayout.setSpacing(false);//,bankTransferLayout3);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
//		hLayout.setWidth("80%");

		
		/*if(null != txtAccntNo)
		{
			mandatoryFields.add(txtAccntNo);
			setRequiredAndValidation(txtAccntNo);
		}
		
		if(null != txtIfscCode)
		{
			mandatoryFields.add(txtIfscCode);
			setRequiredAndValidation(txtIfscCode);
		}*/
		
		if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
				&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
				&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())){
		
			if(txtAccntNo != null)
				txtAccntNo.setEnabled(false);
			if(txtPanNo != null)
				txtPanNo.setEnabled(false);
			if(btnIFCSSearch != null) 
				btnIFCSSearch.setEnabled(false);
		}
			
		
		return hLayout;
	}
	
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
			//field.setValidationVisible(false);
		}
	}
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	private List<Field<?>> getListOfPaymentFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(cmbPayeeName);
		fieldList.add(txtEmailId);
		fieldList.add(txtReasonForChange);
		fieldList.add(txtPanNo);
		fieldList.add(txtLegalHeirFirstName);
		fieldList.add(txtLegalHeirMiddleName);
		fieldList.add(txtLegalHeirLastName);
		fieldList.add(txtAccntNo);
		fieldList.add(txtIfscCode);
		fieldList.add(txtBranch);
		fieldList.add(txtBankName);
		fieldList.add(txtCity);
//		fieldList.add(txtPayableAt);
		return fieldList;
	}
	
	private HorizontalLayout buildChequePaymentLayout(Boolean paymentMode)
	{
		if(cmbPayeeName != null){
			unbindField(cmbPayeeName);
		}
		cmbPayeeName = (ComboBox) paymentbinder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
		
		SelectValue patientStatus = this.bean.getPreauthDataExtractionDetails().getPatientStatus();
		
		BeanItemContainer<SelectValue> payee = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		payee = getValuesForNameDropDown();
		
		cmbPayeeName.setContainerDataSource(payee);
		cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPayeeName.setItemCaptionPropertyId("value");
		
		cmbPayeeName.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = event.getProperty().getValue() != null ? (SelectValue) (event.getProperty().getValue()) : null;
				if(null != value)
				{	
					if(txtRelationship != null)
						txtRelationship.setValue(value.getRelationshipWithProposer());
					if(txtNameAsPerBank != null && value.getNameAsPerBankAccount() != null && !value.getNameAsPerBankAccount().isEmpty())
						txtNameAsPerBank.setValue(value.getNameAsPerBankAccount());
				}
			}	
			
		});
		
		if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null) {
			List<SelectValue> itemIds = payee.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(selectValue.getValue() != null && this.bean.getPreauthDataExtractionDetails().getPayeeName().getValue() != null && selectValue.getValue().toString().toLowerCase().equalsIgnoreCase(this.bean.getPreauthDataExtractionDetails().getPayeeName().getValue().toString().toLowerCase())) {
					this.bean.getPreauthDataExtractionDetails().getPayeeName().setId(selectValue.getId());
				}
			}
			cmbPayeeName.setValue(this.bean.getPreauthDataExtractionDetails().getPayeeName());
			cmbPayeeName.setEnabled(false);
		}
		
	    /*if(this.bean.getPreauthDataExtractionDetails().getPayeeName() != null){
//		payee.addBean(this.bean.getPreauthDataExtractionDetails().getPayeeName());
		
	    }*/
		 
		//cmbPayeeName.setRequired(true);
		
		txtEmailId = (TextField) paymentbinder.buildAndBind("Email ID", "emailId" , TextField.class);
		CSValidator emailValidator = new CSValidator();
		emailValidator.extend(txtEmailId);
		emailValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		emailValidator.setPreventInvalidTyping(true);
		txtEmailId.setMaxLength(100);
		if(null != this.bean.getPayeeEmailId())
		{
			txtEmailId.setValue(this.bean.getPayeeEmailId());
		}
		//txtEmailId.setRequired(true);
		
		txtReasonForChange = (TextField) paymentbinder.buildAndBind("Reason For Change(Payee Name)", "reasonForChange", TextField.class);
		
		CSValidator reasonForChangeValidator = new CSValidator();
		reasonForChangeValidator.extend(txtReasonForChange);
		reasonForChangeValidator.setRegExp("^[a-zA-Z]*$");
		reasonForChangeValidator.setPreventInvalidTyping(true);
		txtReasonForChange.setMaxLength(100);
		
		
		txtPanNo = (TextField) paymentbinder.buildAndBind("PAN No","panNo",TextField.class);
		
		CSValidator panValidator = new CSValidator();
		panValidator.extend(txtPanNo);
		panValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		panValidator.setPreventInvalidTyping(true);
		txtPanNo.setMaxLength(10);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getPanNo())
		{
			txtPanNo.setValue(this.bean.getPreauthDataExtractionDetails().getPanNo());
		}
		
		txtLegalHeirFirstName = (TextField) paymentbinder.buildAndBind("","legalFirstName",TextField.class);
		txtLegalHeirMiddleName = (TextField) paymentbinder.buildAndBind("", "legalMiddleName" , TextField.class);
		txtLegalHeirLastName = (TextField) paymentbinder.buildAndBind("", "legalLastName" , TextField.class);
		
		txtLegalHeirFirstName.setNullRepresentation("");
		txtLegalHeirMiddleName.setNullRepresentation("");
		txtLegalHeirLastName.setNullRepresentation("");
		
		if(txtPayableAt != null){
			txtPayableAt.setReadOnly(false);
		}
		unbindField(txtPayableAt);
		txtPayableAt = (TextField) paymentbinder.buildAndBind("Payable at", "payableAt", TextField.class);
		txtPayableAt.setMaxLength(50);
		CSValidator payableAtValidator = new CSValidator();
		payableAtValidator.extend(txtPayableAt);
		payableAtValidator.setRegExp("^[a-zA-Z]*$");
		payableAtValidator.setPreventInvalidTyping(true);;
		//txtPayableAt.setRequired(true);
		if(null != this.bean.getPreauthDataExtractionDetails().getPayableAt())
		{
			txtPayableAt.setValue(this.bean.getPreauthDataExtractionDetails().getPayableAt());
			txtPayableAt.setEnabled(false);
		}
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())
				&& this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))
		{	
			txtEmailId.setReadOnly(true);
			txtEmailId.setEnabled(false);
			
			txtReasonForChange.setReadOnly(true);
			txtReasonForChange.setEnabled(false);
			
			txtPanNo.setReadOnly(true);
			txtPanNo.setEnabled(false);
			
			txtLegalHeirFirstName.setReadOnly(true);
			txtLegalHeirFirstName.setEnabled(false);
			
			txtLegalHeirMiddleName.setReadOnly(true);
			txtLegalHeirMiddleName.setEnabled(false);
			
			txtLegalHeirLastName.setReadOnly(true);
			txtLegalHeirLastName.setEnabled(false);
			
			txtPayableAt.setReadOnly(true);
			txtPayableAt.setEnabled(false);
			
		}else{
//			cmbPayeeName.setEnabled(true);
			
			if(this.bean.getDocumentReceivedFromId() != null 
					&& this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)
					&& patientStatus != null 
					&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))
					&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
				cmbPayeeName.setEnabled(false);
			}
			else {
				cmbPayeeName.setEnabled(true);
			}
			
			if(txtPayableAt != null){
				txtPayableAt.setReadOnly(false);
				txtPayableAt.setEnabled(true);
			}
			if(txtAccntNo != null){
				txtAccntNo.setReadOnly(false);
				if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
						&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
						&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
						&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())){
				
					if(txtAccntNo != null)
						txtAccntNo.setEnabled(false);
				}else {	
					if(txtAccntNo != null)
						txtAccntNo.setEnabled(true);
				}	
			}
			
			accPrefLayout = new HorizontalLayout();
			accPrefLayout.setCaption("Account Preference");
			accPrefLayout.setCaptionAsHtml(true);
			if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())){
				 if(txtAccountPref != null){	
					 unbindField(txtAccountPref);
				 }
				 
			unbindField(txtAccountPref);	 
			txtAccountPref = (TextField) paymentbinder.buildAndBind("", "accountPref", TextField.class);
			txtAccountPref.setCaption(null);
			txtAccountPref.setEnabled(false);
			txtAccountPref.setNullRepresentation("");
			txtAccountPref.setValue(bean.getAccountPreference());
			btnAccPrefSearch = new Button(); 
			btnAccPrefSearch.setStyleName(ValoTheme.BUTTON_LINK);
			btnAccPrefSearch.setIcon(new ThemeResource("images/search.png"));
			btnAccPrefSearch.addClickListener(getAccountTypeSearchListener());
			
			unbindField(txtAccType);
			txtAccType = (TextField) paymentbinder.buildAndBind("Account Type", "accType", TextField.class);
			txtAccType.setNullRepresentation("");
			txtAccType.setValue(bean.getAccountType());
			txtAccType.setEnabled(false);
			
			accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch);
			accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
		 }
		}
		
//		GridLayout grid = new GridLayout(5,3);
		
		txtLegalHeirFirstName.setCaption(null);
		txtLegalHeirMiddleName.setCaption(null);
		txtLegalHeirLastName.setCaption(null);
		
		HorizontalLayout nameLayout = new HorizontalLayout(txtLegalHeirFirstName,txtLegalHeirMiddleName,txtLegalHeirLastName);
		nameLayout.setComponentAlignment(txtLegalHeirFirstName, Alignment.TOP_LEFT);
		nameLayout.setCaption("Legal Heir Name");
		nameLayout.setWidth("100%");
		nameLayout.setSpacing(true);
		nameLayout.setMargin(false);
		FormLayout formLayout1 = null;
		
		// Billing Internal Remarks
		unbindField(txtAreaBillingInternalRemarks);
		txtAreaBillingInternalRemarks = (TextArea) paymentbinder.buildAndBind("Billing Hospitalization Internal Remarks", "billingInternalRemarks", TextArea.class);
		txtAreaBillingInternalRemarks.setMaxLength(4000);
		txtAreaBillingInternalRemarks.setNullRepresentation("");
		txtAreaBillingInternalRemarks.setDescription("Click the Text Box and Press F8 For Detailed Popup");
		
		if(this.bean.getPreauthDataExtractionDetails().getBillingInternalRemarks() != null) {
			txtAreaBillingInternalRemarks.setValue(this.bean.getPreauthDataExtractionDetails().getBillingInternalRemarks());
		}
		billingInternalRemarksChangeListener(txtAreaBillingInternalRemarks, null);
		
		if(null != this.bean.getPreauthDataExtractionDetails().getPaymentModeFlag())
		{
//			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
			formLayout1 = new FormLayout(optPaymentMode, cmbPayeeName, txtReasonForChange, txtPanNo,txtEmailId,txtPayableAt,txtAreaBillingInternalRemarks);
		}
		else
		{
//			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo);
			
			unbindField(txtNameAsPerBank);
			txtNameAsPerBank = (TextField) paymentbinder.buildAndBind("Name As per Bank Account", "nameAsPerBank", TextField.class);
			txtNameAsPerBank.setNullRepresentation("");
			txtNameAsPerBank.setValue(this.bean.getNameAsPerBankAccount());
			txtNameAsPerBank.setEnabled(false);
			
			formLayout1 = new FormLayout(optPaymentMode, cmbPayeeName, txtReasonForChange, txtNameAsPerBank, txtPanNo, txtEmailId,txtAreaBillingInternalRemarks);
		}
		
		if(! paymentMode){
			
			if(txtPayableAt != null){
				formLayout1.removeComponent(txtPayableAt);
			}
		}
		
//		FormLayout formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,nameLayout);
		
		unbindField(txtRelationship);
		txtRelationship = (TextField) paymentbinder.buildAndBind("Relationship with Proposer", "payeeRelationship", TextField.class);
		txtRelationship.setNullRepresentation("");
		txtRelationship.setValue(this.bean.getPayeeRelationship());
		txtRelationship.setEnabled(false);
		FormLayout formLayout2 = new FormLayout(new Label());
		if(this.bean.getDocumentReceivedFromId() != null && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			formLayout2.addComponent(txtRelationship);
		}
		
		if(this.bean.getDocumentReceivedFromId() != null 
				 && this.bean.getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED) 
				 && SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())){

			unbindField(txtAccType);
			txtAccType = (TextField) paymentbinder.buildAndBind("Account Type", "accType", TextField.class);
			txtAccType.setNullRepresentation("");
			txtAccType.setValue(bean.getAccountType());
			txtAccType.setEnabled(false);
			accPrefLayout.addComponents(txtAccountPref, btnAccPrefSearch);
			accPrefLayout.setComponentAlignment(btnAccPrefSearch, Alignment.BOTTOM_RIGHT);
			
			
			if(this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& patientStatus != null 
					&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(patientStatus.getId()) || ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(patientStatus.getId()))) {
				btnAccPrefSearch.setEnabled(false);
			}
			else if(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(bean.getPreauthDataExtractionDetails().getPaymentModeFlag())) {
				btnAccPrefSearch.setEnabled(true);
			}
			else {
				btnAccPrefSearch.setEnabled(false);
			}
			
			
			formLayout2 = new FormLayout(new Label(), txtRelationship, txtAccType, accPrefLayout);
		 }

		HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , formLayout2);
//		hLayout.setWidth("90%");
		
//		payeenameListner();
		
		return hLayout;
		
		
	}
	
	private BeanItemContainer<SelectValue>  getValuesForNameDropDown()
	{
		Policy policy = policyService.getPolicy(this.bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		SelectValue selectValue = null;
		SelectValue payeeValue = null;
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			selectValue = new SelectValue();
			payeeValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			payeeValue.setId(Long.valueOf(String.valueOf(i)));
			payeeValue.setValue(insured.getInsuredName());
			payeeValue.setSourceRiskId(insured.getSourceRiskId());
			payeeValue.setRelationshipWithProposer(insured.getRelationshipwithInsuredId() != null ? insured.getRelationshipwithInsuredId().getValue() : "");
			payeeValue.setNameAsPerBankAccount(insured.getNameOfAccountHolder());
			
			selectValueList.add(selectValue);
			payeeValueList.add(payeeValue);
		}
		
		/*for (int i = 0; i < insuredList.size(); i++) {
			Insured insured = insuredList.get(i);
			List<NomineeDetails> nomineeDetails = policyService.getNomineeDetails(insured.getKey());
			for (NomineeDetails nomineeDetails2 : nomineeDetails) {
				selectValue = new SelectValue();
				selectValue.setId(nomineeDetails2.getKey());
				selectValue.setValue(nomineeDetails2.getNomineeName());
				payeeValueList.add(selectValue);
			}
			
		}*/
		
		if( (this.bean.getPreauthDataExtractionDetails().getPatientStatus() != null && this.bean.getPreauthDataExtractionDetails().getPatientStatus().getId().equals(ReferenceTable.PATIENT_STATUS_DECEASED_REIMB)) 
				&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())
				&& this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(this.bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
		
			List<PolicyNominee> pNomineeDetails = intimationService.getPolicyNomineeList(policy.getKey());
		
		for (PolicyNominee pNominee : pNomineeDetails) {
			selectValue = new SelectValue();
			selectValue.setId(pNominee.getKey());
			selectValue.setValue(pNominee.getNomineeName());
			payeeValueList.add(selectValue);
			selectValue = null;
		}
		
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		
		SelectValue payeeSelValue = new SelectValue();
		int iSize = payeeValueList.size() +1;
		payeeSelValue.setId(Long.valueOf(String.valueOf(iSize)));
		payeeSelValue.setValue(proposerName);
		payeeSelValue.setSourceRiskId(policy.getProposerCode());
		payeeSelValue.setRelationshipWithProposer(SHAConstants.RELATIONSHIP_SELF);
		
		payeeValueList.add(payeeSelValue);
		
		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CLAIM_TYPE_CASHLESS_ID)){
			SelectValue hospitalName = new SelectValue();
			hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
			hospitalName.setValue(this.bean.getNewIntimationDTO().getHospitalDto().getName());
			payeeValueList.add(hospitalName);
		}

		BeanItemContainer<SelectValue> payeeNameValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		payeeNameValueContainer.addAll(payeeValueList);
		
		payeeNameValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		return payeeNameValueContainer;
		
		}
		
		return null;
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
		
		if (field != null ) {
			Object propertyId = this.paymentbinder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.paymentbinder.unbind(field);
			}
		}
	}
	
	private void unbindField(List<Field<?>> field) {
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
		
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.paymentbinder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.paymentbinder.unbind(field2);
					}
				}
			}
		}
		
		
	}
	
	public void addIFSCCodeListner()
	{
		btnIFCSSearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.PA_HEALTH_BILLING_SCREEN);
				viewSearchCriteriaWindow.initView();
				
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setContent(viewSearchCriteriaWindow);
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
		
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	public void alertMessageForRejection() {
		Label successLabel = new Label();
		successLabel = new Label(
				"<b style = 'color: red;'> Hospitalization Rod is in Rejected State. Hence this ROD will be rejected in Financial Stage</b>",
				ContentMode.HTML);
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
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
			    bean.setRejectionAlertMessageOpened(true);
			    wizard.next();
			    dialog.close();

			}
		});
	}
	
	
	public void alertMessageUniquePremium() {
		Label successLabel = new Label();
		successLabel = new Label(
				"<b style = 'color: red;'> II Instalment premium cannot be adjusted as amount claimed is lower, Please collect the premium from the insured </b>",
				ContentMode.HTML);
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("ok");
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
				billingProcessButtonObj.buildSendToFinancialLayout();
			}
		});
	}
	
	private VerticalLayout getOtherInsSettlementTab(){
		if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			if(otherInsTabLayout == null) {
				otherInsTabLayout = new HorizontalLayout();
				otherInsTabLayout.setSpacing(false);
				otherInsTabLayout.setWidth("100%");
			} else {
				otherInsTabLayout.removeAllComponents();
			}
			
			if(bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag() || bean.getIsHospitalizationRepeat()) {
				if(otherInsurerHospObj == null) {
					otherInsurerHospObj = otherInsHospInstance.get();
				} 
				SHAUtils.setOtherInsurerSettlementHospValues(bean, hospitalizaionObj != null ? hospitalizaionObj.getHospAmt() : "0", hospitalizaionObj != null ? hospitalizaionObj.getAmountAlreadyPaid() : "0");
				otherInsurerHospObj.initView(bean);
				otherInsTabLayout.addComponent(otherInsurerHospObj);
			} 
			if(bean.getPreHospitalizaionFlag()){
				if(otherInsurerPreHospObj == null) {
					otherInsurerPreHospObj = otherInsPreHospInstance.get();
				} 
				SHAUtils.setOtherInsurerSettlementPreHospValues(bean, preHospitalizationObj != null ? preHospitalizationObj.getPreHospAmt() : "0", preHospitalizationObj != null ? preHospitalizationObj.getAmountAlreadyPaid() : "0");
				otherInsurerPreHospObj.initView(bean);
				otherInsTabLayout.addComponent(otherInsurerPreHospObj);
			}
			if(bean.getPostHospitalizaionFlag()) {
				if(otherInsurerPostHospObj == null) {
					otherInsurerPostHospObj = otherInsPostHospInstance.get();
				} 
				SHAUtils.setOtherInsurerSettlementPostHospValues(bean, postHospitalizationObj != null ? postHospitalizationObj.getPreHospAmt() : "0", postHospitalizationObj != null ? postHospitalizationObj.getAmountAlreadyPaid() : "0");
				otherInsurerPostHospObj.initView(bean);
				otherInsTabLayout.addComponent(otherInsurerPostHospObj);
			}
		} else {
			if(otherInsTabLayout != null) {
				otherInsTabLayout.removeAllComponents();
			}
		}

		addListenerForConsolidated();
		TabSheet otherInsSettlementTab = new TabSheet();
		//Vaadin8-setImmediate() otherInsSettlementTab.setImmediate(true);
		otherInsSettlementTab.setWidth("100%");
		otherInsSettlementTab.setHeight("100%");
		otherInsSettlementTab.setSizeFull();
		//Vaadin8-setImmediate() otherInsSettlementTab.setImmediate(true);
		
		VerticalLayout verticalLayout = new VerticalLayout(otherInsTabLayout);
		verticalLayout.setStyleName("tpabackground");
		verticalLayout.setWidth("100%");
		verticalLayout.setCaption("Other Insurer Settlement");
//		otherInsSettlementTab.addComponent(verticalLayout);
		
		return verticalLayout;
	}
	
	private VerticalLayout getConsolidatedAmountTab(){
		TabSheet consolidateAmtTab = new TabSheet();
		//Vaadin8-setImmediate() consolidateAmtTab.setImmediate(true);
		consolidateAmtTab.setWidth("100%");
		consolidateAmtTab.setHeight("100%");
		consolidateAmtTab.setSizeFull();
		//Vaadin8-setImmediate() consolidateAmtTab.setImmediate(true);
		if(consolidatedAmountObj == null) {
			 consolidatedAmountObj = consolidatedAmountInstance.get();
		}
//		consolidatedAmountObj.initView(this.bean, false);
		consolidatedTabChage();
		
		VerticalLayout verticalLayout = new VerticalLayout(consolidatedAmountObj);
		verticalLayout.setCaption("Consolidated Amount");
		verticalLayout.setSpacing(true);
		 
//		consolidateAmtTab.addComponent(verticalLayout);
		
		return verticalLayout;
		
	}
	

	private void consolidatedTabChage() {
		ConsolidatedAmountDetailsDTO consolidatedAmtDTO = bean.getConsolidatedAmtDTO();
		if(otherInsApplicable != null && otherInsApplicable.getValue() != null && otherInsApplicable.getValue().toString() == "true") {
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				
				if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
					if(otherInsurerHospObj != null) {
						consolidatedAmtDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerHospObj.getPayableAmount()));
					}
				}
				if(bean.getPreHospitalizaionFlag()) {
					if(otherInsurerPreHospObj != null) {
						consolidatedAmtDTO.setPreHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerPreHospObj.getPayableAmount()));
					}
				}
				
				if(bean.getPostHospitalizaionFlag()) {
					if(otherInsurerPostHospObj != null) {
						consolidatedAmtDTO.setPostHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(otherInsurerPostHospObj.getPayableAmount()));
					}
				}
				if(consolidatedAmountObj != null) {
					consolidatedAmountObj.initView(bean, false);
				}
			} else {
				if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
					if(hospitalizaionObj != null) {
						consolidatedAmtDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(hospitalizaionObj.getHospAmt()));
					}
				}
				if(bean.getPreHospitalizaionFlag()) {
					if(preHospitalizationObj != null) {
						consolidatedAmtDTO.setPreHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(preHospitalizationObj.getPreHospAmt()));
					}
				}
				
				if(bean.getPostHospitalizaionFlag()) {
					if(postHospitalizationObj != null) {
						consolidatedAmtDTO.setPostHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(postHospitalizationObj.getPreHospAmt()));
					}
				}
				if(consolidatedAmountObj != null) {
					consolidatedAmountObj.initView(bean, false);
				}
			}
		} else {
			if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
				if(hospitalizaionObj != null) {
					consolidatedAmtDTO.setHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(hospitalizaionObj.getHospAmt()));
				}
			}
			if(bean.getPreHospitalizaionFlag()) {
				if(preHospitalizationObj != null) {
					consolidatedAmtDTO.setPreHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(preHospitalizationObj.getPreHospAmt()));
				}
			}
			
			if(bean.getPostHospitalizaionFlag()) {
				if(postHospitalizationObj != null) {
					consolidatedAmtDTO.setPostHospPayableAmt(SHAUtils.getIntegerFromStringWithComma(postHospitalizationObj.getPreHospAmt()));
				}
			}
			if(consolidatedAmountObj != null) {
				consolidatedAmountObj.initView(bean, false);
			}
		}
		
		if((ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ) {
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
				consolidatedAmtDTO.setPremiumAmt(uniqueInstallmentAmount);
				Integer deductedAmt = (consolidatedAmountObj != null ? SHAUtils.getIntegerFromStringWithComma(consolidatedAmountObj.getTotalConsolidatedAmt()) : 0)  - uniqueInstallmentAmount;
				consolidatedAmtDTO.setAmountPayableToInsAftPremium(deductedAmt > 0 ? deductedAmt : 0);
			}
		}
		
		//Added for add on benefits table value save code.
		List<AddOnBenefitsDTO> hospCashList = null;
		List<AddOnBenefitsDTO> patientCareList = null;
		List<AddOnBenefitsDTO> consolidatedDTOList = new ArrayList<AddOnBenefitsDTO>();
		Double consolidatedNetAppAmt = 0d;
		if(null != addOnBenefitsListenerTableObj)
			hospCashList = addOnBenefitsListenerTableObj.getValues();
		if(null != addOnBenefitsPatientCareLiseterObj)
			patientCareList = addOnBenefitsPatientCareLiseterObj.getValues();
		
		if(null != hospCashList && !hospCashList.isEmpty())
		{
			for (AddOnBenefitsDTO addOnBenefitsDTO : hospCashList) {
				consolidatedNetAppAmt += addOnBenefitsDTO.getPayableAmount();
				consolidatedDTOList.add(addOnBenefitsDTO);
			}
			
		}
		if(null != patientCareList && !patientCareList.isEmpty())
		{
			for (AddOnBenefitsDTO addOnBenefitsDTO : patientCareList) {
				consolidatedNetAppAmt += addOnBenefitsDTO.getPayableAmount();
				consolidatedDTOList.add(addOnBenefitsDTO);
			}
		}
		if(null != consolidatedNetAppAmt)
			consolidatedAmtDTO.setAddonBenefitAmt(consolidatedNetAppAmt.intValue());
		if(null !=consolidatedDTOList && !consolidatedDTOList.isEmpty())
			bean.getPreauthDataExtractionDetails().setAddOnBenefitsDTOList(consolidatedDTOList);
		
		if(consolidatedAmountObj !=null
				&& legalBillingUIObj !=null){
					legalBillingUIObj.setawardAmount(consolidatedAmountObj.getTotalConsolidatedAmt());
		}
		}
	
	
	private void otherInsSettlementChange() {
		if(otherInsApplicable != null && otherInsApplicable.getValue() != null && otherInsApplicable.getValue().toString() == "true") {
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
					if(otherInsurerHospObj != null ) {
						otherInsurerHospObj.isValid();
					}
					SHAUtils.setOtherInsurerSettlementHospValues(bean, hospitalizaionObj != null ? hospitalizaionObj.getHospAmt() : "0", hospitalizaionObj != null ? hospitalizaionObj.getAmountAlreadyPaid() : "0");
					if(otherInsurerHospObj != null) {
						otherInsurerHospObj.initView(bean);
					}
				}
				if(bean.getPreHospitalizaionFlag()) {
					if(otherInsurerPreHospObj != null ) {
						otherInsurerPreHospObj.isValid();
					}
					SHAUtils.setOtherInsurerSettlementPreHospValues(bean, preHospitalizationObj != null ? preHospitalizationObj.getPreHospAmt() : "0", preHospitalizationObj != null ? preHospitalizationObj.getAmountAlreadyPaid() : "0");
					if(otherInsurerPreHospObj != null) {
						otherInsurerPreHospObj.initView(bean);
					}
				}
				
				if(bean.getPostHospitalizaionFlag()) {
					if(otherInsurerPostHospObj != null ) {
						otherInsurerPostHospObj.isValid();
					}
					SHAUtils.setOtherInsurerSettlementPostHospValues(bean, postHospitalizationObj != null ? postHospitalizationObj.getPreHospAmt() : "0", postHospitalizationObj != null ? postHospitalizationObj.getAmountAlreadyPaid() : "0");
					if(otherInsurerPostHospObj != null) {
						otherInsurerPostHospObj.initView(bean);
					}
				}
			}
		} else {
			
		}
	}
	
	public void addListenerForConsolidated() {
		if(otherInsurerHospObj != null) {
			otherInsurerHospObj.payableAmtChangeListenerField.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					consolidatedTabChage();
					
				}
			});
		}
		
		if(otherInsurerPostHospObj != null) {
			otherInsurerPostHospObj.payableAmtChangeListenerField.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					consolidatedTabChage();
					
				}
			});
		}
		
		if(otherInsurerPreHospObj != null) {
			otherInsurerPreHospObj.payableAmtChangeListenerField.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					consolidatedTabChage();
					
				}
			});
		}

	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {
	
		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	private void bindValues() {
		if(this.hospitalizaionObj != null) {
			this.hospitalizaionObj.isValid();
		}
		if(this.otherInsurerHospObj != null) {
			this.otherInsurerHospObj.isValid();
		}
		if(this.otherInsurerPreHospObj != null) {
			this.otherInsurerPreHospObj.isValid();
		}
		if(this.otherInsurerPostHospObj != null) {
			this.otherInsurerPostHospObj.isValid();
		}
	}
	
	private void addListenerForBenefits()
	{
		if(addOnBenefitsListenerTableObj != null) {
			addOnBenefitsListenerTableObj.dummyField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue != null) {
						Double doubleFromStringWithComma = SHAUtils.getDoubleFromStringWithComma(totalValue);
						hospitalCashPayableAmt.setReadOnly(false);
						hospitalCashPayableAmt.setValue(String.valueOf(doubleFromStringWithComma.intValue()) );
						bean.setHospitalCashAmt(doubleFromStringWithComma);
						hospitalCashPayableAmt.setReadOnly(true);
					}
					
				}
			});
		}
		
		if(addOnBenefitsPatientCareLiseterObj != null) {
			addOnBenefitsPatientCareLiseterObj.dummyField.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 7455756225751111662L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					String totalValue = (String) event.getProperty().getValue();
					if(totalValue != null) {
						Double doubleFromStringWithComma = SHAUtils.getDoubleFromStringWithComma(totalValue);
						patientCarePayableAmt.setReadOnly(false);
						patientCarePayableAmt.setValue(String.valueOf(doubleFromStringWithComma.intValue()) );
						bean.setPatientCareAmt(doubleFromStringWithComma);
						patientCarePayableAmt.setReadOnly(true);
					}
					
				}
			});
		}	
	}
	
	private void addPreviousPaymentPopupListener()
	{
	btnPopulatePreviousAccntDetails.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
				
				populatePreviousWindowPopup = new com.vaadin.ui.Window();
				populatePreviousWindowPopup.setWidth("75%");
				populatePreviousWindowPopup.setHeight("90%");
				
				previousAccountDetailsTable.init("Previous Account Details", false, false);
				previousAccountDetailsTable.setPresenterString(SHAConstants.BILLING);
				previousPaymentVerticalLayout = new VerticalLayout();
				previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
				previousPaymentVerticalLayout.addComponent(previousAccntDetailsLayout);
				previousPaymentVerticalLayout.setComponentAlignment(previousAccntDetailsLayout, Alignment.TOP_CENTER);
				setTableValues();
				populatePreviousWindowPopup.setContent(previousPaymentVerticalLayout);
				populatePreviousWindowPopup.setClosable(true);
				populatePreviousWindowPopup.center();
				populatePreviousWindowPopup.setResizable(true);
				
				populatePreviousWindowPopup.addCloseListener(new Window.CloseListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				populatePreviousWindowPopup.setModal(true);
				populatePreviousWindowPopup.setClosable(false);
				
				UI.getCurrent().addWindow(populatePreviousWindowPopup);
				btnPopulatePreviousAccntDetails.setEnabled(true);
			}
		});
		
	btnOk.addClickListener(new ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			String err = previousAccountDetailsTable.isValidate();
				if("" == err)
				{
			
			buildDialogBox("Selected Data will be populated in payment details section. Please click OK to proceeed",populatePreviousWindowPopup,SHAConstants.BTN_OK);
			//populatePreviousWindowPopup.close();
		//	previousAccountDetailsTable.clearCheckBoxValue();
				}
		}
	});

	btnCancel.addClickListener(new ClickListener() {
	
	@Override
	public void buttonClick(ClickEvent event) {
		buildDialogBox("Are you sure you want to cancel",populatePreviousWindowPopup,SHAConstants.BTN_CANCEL);
	//	resetBankPaymentFeidls();
	//	previousAccountDetailsTable.clearCheckBoxValue();
		
	}
	});
	}
	
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
		if(null != txtEmailId)
		{
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(tableDTO.getEmailId());
			txtEmailId.setEnabled(true);
		}
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(tableDTO.getPanNo());
			txtPanNo.setEnabled(true);
		}
		if(null != txtAccntNo)
		{
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(tableDTO.getBankAccountNo());
			if(bean.getNewIntimationDTO().getPolicy().getPolicySource() != null
					&& SHAConstants.BANK_POLICY_SOURCE.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getPolicySource())
					&& this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null
					&& ReferenceTable.RECEIVED_FROM_INSURED.equals(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey())){
			
				if(txtAccntNo != null)
					txtAccntNo.setEnabled(false);
			}
			else {
				if(txtAccntNo != null)
					txtAccntNo.setEnabled(true);
			}
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(tableDTO.getIfsccode());
			txtIfscCode.setEnabled(true);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(tableDTO.getBankName());
			txtBankName.setEnabled(true);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(tableDTO.getBankCity());
			txtCity.setEnabled(true);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(tableDTO.getBankBranch());
			txtBranch.setEnabled(true);
		}
		
		
		
	}

	public void resetBankPaymentFeidls() {
		if(null != txtEmailId)
		{	
			txtEmailId.setReadOnly(false);
			txtEmailId.setValue(null);
		}	
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(null);
		}
		if(null != txtAccntNo)
		{
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(null);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(null);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(null);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(null);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(null);
		}
		
		
		
	}
	
/*	private void buildDialogBox(String message,final Window populatePreviousWindowPopup)
	{
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		
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
		if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			//dialog.setDraggable(true);
			
			
		}
		getUI().getCurrent().addWindow(dialog);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != populatePreviousWindowPopup)
					populatePreviousWindowPopup.close();
				//fireViewEvent(MenuItemBean.CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
	}*/
	
	private void buildDialogBox(String message,final Window populatePreviousWindowPopup,String btnName)
	{
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		/*Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);*/
		
		Button cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		
		
		if(SHAConstants.BTN_CANCEL.equalsIgnoreCase(btnName))
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.addComponent(cancelBtn);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			horizontalLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		}
		else
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		}
		 
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
		//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
		//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
		
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
		/*if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			//dialog.setDraggable(true);
			
			
		}*/
		getUI().getCurrent().addWindow(dialog);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != populatePreviousWindowPopup)
					populatePreviousWindowPopup.close();
				//fireViewEvent(MenuItemBean.CREATE_ROD, null);
				//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
				
			}
		});
		if(null != cancelBtn)
		{
			cancelBtn.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					/*if(null != populatePreviousWindowPopup)
						populatePreviousWindowPopup.close();*/
					//fireViewEvent(MenuItemBean.CREATE_ROD, null);
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
		}
	}
	
	private void setTableValues()
	{
		if(null != previousAccountDetailsTable)
		{
			int rowCount = 1;
			List<List<PreviousAccountDetailsDTO>> previousListTable = this.bean.getPreviousAccountDetailsList();
			if(null != previousListTable && !previousListTable.isEmpty())
			{
				for (List<PreviousAccountDetailsDTO> list : previousListTable) {
					for (PreviousAccountDetailsDTO previousAccountDetailsDTO : list) {
						
						previousAccountDetailsDTO.setChkSelect(false);
						previousAccountDetailsDTO.setChkSelect(null);						
						previousAccountDetailsDTO.setSerialNo(rowCount);
						previousAccountDetailsTable.addBeanToList(previousAccountDetailsDTO);
						rowCount ++ ;
					}
				}
				
			}
		}
	}
	
	private ClickListener getAccountTypeSearchListener(){
		
		ClickListener listener = new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {				
				final Window popup = new com.vaadin.ui.Window();
			//	List<BankDetailsTableDTO> verificationAccountDeatilsList = bean.getVerificationAccountDeatilsTableDTO();
				SelectValue value = (SelectValue) cmbPayeeName.getValue();
				ReceiptOfDocumentsDTO beanDto = new ReceiptOfDocumentsDTO();
				DocumentDetailsDTO doctDto = new DocumentDetailsDTO();
				doctDto.setPatientStatus(bean.getPreauthDataExtractionDetails().getPatientStatus());
				beanDto.setDocumentDetails(doctDto);
				beanDto.setSourceRiskID(value.getSourceRiskId());
				bankDetailsTableObj =  bankDetailsTableInstance.get();
				bankDetailsTableObj.init(beanDto);
				bankDetailsTableObj.initPresenter(SHAConstants.PA_HEALTH_BILLING_SCREEN);
				bankDetailsTableObj.setCaption("Bank Details");
				/*if(verificationAccountDeatilsList != null){
					verificationAccountDeatilsTableObj.setTableList(verificationAccountDeatilsList);
				}*/
				
				popup.setWidth("75%");
				popup.setHeight("70%");
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
				Button okBtn = new Button("Cancel");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				okBtn.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						List<BankDetailsTableDTO> bankDetailsTableDTO = bankDetailsTableObj.getValues();
						bankDetailsTableDTO = new ArrayList<BankDetailsTableDTO>();
						//bean.setVerificationAccountDeatilsTableDTO(bankDetailsTableDTO);
						popup.close();
					}
				});
		
				VerticalLayout vlayout = new VerticalLayout(bankDetailsTableObj);
				HorizontalLayout hLayout = new HorizontalLayout(okBtn);
				hLayout.setSpacing(false);
				vlayout.setMargin(false);
				vlayout.addComponent(hLayout);
				vlayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
				popup.setContent(vlayout);
				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
	    		
			}
		};
	return listener;
	}
	
	public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		bankDetailsTableObj.setUpAddBankIFSCDetails(dto);
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
				txtArea.setReadOnly(false);
				
				final Window dialog = new Window();
				dialog.setHeight("75%");
		    	dialog.setWidth("65%");
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void valueChange(ValueChangeEvent event) {
						textAreaField.setValue(((TextArea) event.getProperty()).getValue());						
//						PreauthDTO mainDto = (PreauthDTO) textAreaField.getData();
//						mainDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(textAreaField.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				dialog.setCaption("Billing Hospitalization Internal Remarks");
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
	public void setupPanDetailsMandatory(Boolean panDetails){
		if(panDetails !=null
				&& panDetails){
			if(txtPanNo !=null
					&& (txtPanNo.getValue() == null || txtPanNo.getValue().equals(""))){
				txtPanNo.setEnabled(true);
				mandatoryFields.add(txtPanNo);
				showOrHideValidation(false);
			}

		}else{
			mandatoryFields.remove(txtPanNo);
		}
	}
	
	public void setTableValuesForLegalSettlement(){	
		if(legalBillingUIObj !=null){
			this.bean.setLegalBillingDTO(legalBillingUIObj.getvalue());	
		}
	}
}
