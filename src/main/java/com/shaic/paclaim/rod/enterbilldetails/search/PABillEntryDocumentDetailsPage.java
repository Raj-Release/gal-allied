package com.shaic.paclaim.rod.enterbilldetails.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListValidationListenerTable;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.OptionalCoversTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAPamentQueryDetailsTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PARODQueryDetailsTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAReconsiderRODRequestListenerTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PABillEntryDocumentDetailsPage extends ViewComponent{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;

	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	/*@Inject
	private Instance<PARODQueryDetailsTable> rodQueryDetailsObj;*/
	@Inject
	private PARODQueryDetailsTable rodQueryDetails;
	
	@Inject
	public PAPamentQueryDetailsTable paymentQueryDetails;
/*	@Inject
	private BillEntryRODReconsiderRequestTable reconsiderRequestDetails;
*/
	@Inject
	private PAReconsiderRODRequestListenerTable reconsiderRequestDetails;
	
	/*@Inject
	private BillEntryCheckListValidationTable documentCheckListValidation;*/
	
	@Inject
	private Instance<DocumentCheckListValidationListenerTable> documentCheckListValidationObj;
	
	private DocumentCheckListValidationListenerTable documentCheckListValidation;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	/*@Inject
	private BillEntryDocumentCheckListValidationListenerTable documentCheckListValidation;*/
	
	
	@Inject
	private AddOnCoversTable addOnCoversTable;
	
	@Inject
	private OptionalCoversTable optionalCoversTable;	
	
	private VerticalLayout addOnBenifitLayout;
	
	private VerticalLayout optionalCoverLayout;
	
	private OptionGroup accidentOrDeath;
	
	private DateField accidentOrDeathDate;
	
	private ComboBox cmbDocumentType;

	private ComboBox cmbDocumentsReceivedFrom;
	
	private DateField documentsReceivedDate;
	
	private ComboBox cmbModeOfReceipt;
	
	private ComboBox cmbReconsiderationRequest;
	
	private TextField txtAdditionalRemarks;
	
	//private DateField dateOfAdmission;
	
	//private TextField txtHospitalName;
	
	//private ComboBox cmbInsuredPatientName;
	
	private CheckBox chkDeath;
	
	private CheckBox chkPermanentPartialDisability;
	
	private CheckBox chkPermanentTotalDisability;
	
	private CheckBox chkTemporaryTotalDisability;
	
	private CheckBox chkHospitalExpensesCover;
	
	
	private CheckBox chkhospitalization;
	
	private CheckBox chkPreHospitalization;
	
	private CheckBox chkPostHospitalization;
	
	private CheckBox chkPartialHospitalization;
	
	private CheckBox chkHospitalizationRepeat;
	
	private CheckBox chkLumpSumAmount;
	
	private CheckBox chkAddOnBenefitsHospitalCash;
	
	private CheckBox chkAddOnBenefitsPatientCare;
	
	private VerticalLayout documentDetailsPageLayout;
	
	private OptionGroup optPaymentMode;
	
	private ComboBox cmbPayeeName;
	
	private TextField txtEmailId;
	
	private TextField txtReasonForChange;
	
	private TextField txtPanNo;
	
	private TextField txtLegalHeirFirstName;
	private TextField txtLegalHeirMiddleName;
	private TextField txtLegalHeirLastName;
	
	private TextField txtPayableAt;
	
	private TextField txtAccntNo;
	
	private TextField txtIfscCode;
	
	private TextField txtBranch;
	
	private TextField txtBankName;
	
	private TextField txtCity;
	
	private TextField txtReasonForChangeInDOA;
	
	//private FormLayout formLayout1 = null;
	
	
	

	private GWizard wizard;
	
	private VerticalLayout reconsiderationLayout;
	
	private VerticalLayout paymentDetailsLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	private BeanItemContainer<SelectValue> reconsiderationRequest;
	 
	private BeanItemContainer<SelectValue> modeOfReceipt;
	
	private BeanItemContainer<SelectValue> documentType;
	 
	private BeanItemContainer<SelectValue> docReceivedFromRequest ;
	
	private BeanItemContainer<SelectValue> payeeNameList;
	
	private BeanItemContainer<SelectValue> insuredPatientList;
	
	//private HorizontalLayout insuredLayout;
	
	private List<DocumentDetailsDTO> docsDetailsList ;
	
	private TextField txtHospitalizationClaimedAmt;
	private TextField txtPreHospitalizationClaimedAmt;
	private TextField txtPostHospitalizationClaimedAmt;
	private TextField txtBenifitClaimedAmnt;
	
	
	//private  DocumentDetailsDTO docDTO ;
	private  List<DocumentDetailsDTO> docDTO ;

	 @Inject
	 private ViewDetails viewDetails;
	 
	 private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	 
	 public Map<String,Boolean> reconsiderationMap = new HashMap<String, Boolean>();
	 
	 public String hospitalizationClaimedAmt = "";
		
	 public String preHospitalizationAmt = "";
		
	 public String postHospitalizationAmt= "";
	 public ReconsiderRODRequestTableDTO reconsiderDTO = null;
	  
	 private static boolean isQueryReplyReceived = false;
	 
	 private ComboBox cmbReasonForReconsideration;
	 
	 private BeanItemContainer<SelectValue> reasonForReconsiderationRequest;
	 
	 //Added for refer to bill entry enhancement
	 private TextArea txtBillingRemarks;
	 private TextArea txtFARemarks;
	 
	 private Button editBillClassificationBtn;
	 
	 private Boolean isPreHospAlreadySelected = false;
	 
	 private VerticalLayout reconsiderationTblLayout = null;
	 
	 public HorizontalLayout documentDetailsLayout;
		
	 public VerticalLayout docCheckListLayout;
		
	 public HorizontalLayout remarksLayout;
	 
	 public HorizontalLayout billEntryLayout;
	 
	 public Map<String, Object> referenceMap = new HashMap<String, Object>();
	 
	 
	 private VerticalLayout queryDetailsPanel;

	 private Boolean isValid = false;
		
		private String coverName = "";
		
		private OptionGroup optPaymentCancellation;
		
		private OptionGroup workOrNonWorkSpace;	
		
		private DateField dateOfAccident;
		
		private DateField dateOfDeath;
				
		private DateField dateOfDisablement;
			
		/*@Inject
		private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
			
		private NomineeDetailsTable nomineeDetailsTable;
		
		private FormLayout legaHeirLayout;*/
		
	//private Panel billClassificationPanel;
	
	@PostConstruct
	public void init() {

		//reconsiderRequestDetails.init();
	}
	
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(
				DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean
				.getDocumentDetails());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	public void showCMDAlert() {	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'>" + SHAConstants.CMD_ALERT + "</b>",
					ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});
		}
	
	public Component getContent() {
		
		if(this.bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag() != null
				&& SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getKey());
		if(null != memberType && !memberType.isEmpty() && memberType.equalsIgnoreCase(SHAConstants.CMD)){
			showCMDAlert();
		}
		
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
//		if(this.bean.getClaimCount() >1){
//			alertMessageForClaimCount(this.bean.getClaimCount());
//		}

		documentDetailsPageLayout = new VerticalLayout();
		documentDetailsPageLayout.setSpacing(false);
		documentDetailsPageLayout.setMargin(false);
		
		reconsiderationLayout = new VerticalLayout();
		reconsiderationLayout.setSpacing(false);	
		
		queryDetailsPanel = new VerticalLayout();
		if(paymentQueryDetails != null){
			paymentQueryDetails.removeRow();
		}
		if(rodQueryDetails != null){
			rodQueryDetails.removeRow();
		}
		
		billEntryLayout = new HorizontalLayout();
				
		billEntryLayout.addComponents(buildBillingDetailsLayout());
		
	    documentDetailsLayout = buildDocumentDetailsLayout(); 
		documentDetailsLayout.setCaption("Document Details");
//		documentDetailsLayout.setWidth("90%");
		documentDetailsLayout.setMargin(true);
		//documentDetailsLayout.setHeight("50px");
		
		VerticalLayout addOnCoversLayout = new VerticalLayout();	
		addOnCoversLayout.addComponent(buildAddOncoversDetailsLayout());
		addOnCoversLayout.setSpacing(true);
		
		VerticalLayout benifitLayout  = new VerticalLayout();
		benifitLayout.addComponent(buildBenifitsLayout());
		benifitLayout.setSpacing(true);
		
		VerticalLayout optionalCoversLayout = new VerticalLayout();	
		optionalCoversLayout.addComponent(buildOptionalCoversDetailsLayout());
		optionalCoversLayout.setSpacing(true);
		
		
		
		
		
		VerticalLayout reconsiderLayout  = new VerticalLayout();
		reconsiderLayout.addComponent(buildReconsiderationLayout());
		
		reconsiderLayout.setSpacing(true);
		
	/*	documentCheckListValidation.init("Checklist Validation", false);
		documentCheckListValidation.setReference(this.referenceData);*/
		
		documentCheckListValidation = documentCheckListValidationObj.get();
		documentCheckListValidation.initPresenter(SHAConstants.BILL_ENTRY,SHAConstants.PA_LOB);
		documentCheckListValidation.init();		
		
		docCheckListLayout = new VerticalLayout();
		docCheckListLayout.addComponent(documentCheckListValidation);
		docCheckListLayout.setCaption("Document Checklist");
		docCheckListLayout.setHeight("100%");
	
		//documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,reconsiderationLayout,insuredLayout,documentCheckListValidation);
	//	documentDetailsPageLayout = new VerticalLayout(KKKK,documentDetailsLayout,reconsiderationLayout,documentCheckListValidation);
		documentDetailsPageLayout = new VerticalLayout(billEntryLayout,documentDetailsLayout,benifitLayout,addOnCoversLayout,optionalCoversLayout,reconsiderLayout,docCheckListLayout);
		
		/*SelectValue docRecFromValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
		if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId()))		
				   && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
				   && docRecFromValue != null
				   && ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromValue.getId())) {
			
			buildNomineeLayout();
		}*/
		
		
		if(null != bean.getClaimDTO().getClaimTypeValue() && SHAConstants.CASHLESS_CLAIM_TYPE.equalsIgnoreCase(bean.getClaimDTO().getClaimType().getValue())){
			
			accidentOrDeath.setEnabled(false);			
		}

		addListener();
		setTableValues();
		addBenefitLister();
		return documentDetailsPageLayout;
	}
	
	private void getDocumentTableDataList()
	{
		
		fireViewEvent(PABillEntryDocumentDetailsPresenter.BILL_ENTRY_SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES, null);
	}
	
	
	private VerticalLayout buildAddOncoversDetailsLayout()
	{
				
	//	addOnCoversTable.initpresenterString(SHAConstants.CREATE_ROD);
		addOnCoversTable.init("", true);
		addOnCoversTable.setScreenName(SHAConstants.BILL_ENTRY);
		//loadQueryDetailsTableValues();
		
		referenceMap.put("covers", bean.getDocumentDetails().getAdditionalCovers());
		addOnCoversTable.setReference(referenceMap);
		
		addOnBenifitLayout = new VerticalLayout();
		addOnBenifitLayout.setWidth("100%");
		addOnBenifitLayout.setCaption("Covers (Add On Covers)");
		addOnBenifitLayout.setSpacing(true);
		addOnBenifitLayout.setMargin(false);
		addOnBenifitLayout.addComponent(addOnCoversTable);
	//	addOnBenifitLayout.addComponent();
		
		if(this.bean.getDocumentDetails().getAddOnCoversList() != null && ! this.bean.getDocumentDetails().getAddOnCoversList().isEmpty()){
			for (AddOnCoversTableDTO covers : this.bean.getDocumentDetails().getAddOnCoversList()) {
				addOnCoversTable.addBeanToList(covers);
				
			}
		}
		
		
		//return rodQueryDetails;
		return addOnBenifitLayout;
	}
	
	private VerticalLayout buildOptionalCoversDetailsLayout()
	{
				
	//	addOnCoversTable.initpresenterString(SHAConstants.CREATE_ROD);
		optionalCoversTable.init("", true);
		optionalCoversTable.setScreenName(SHAConstants.BILL_ENTRY);
		//loadQueryDetailsTableValues();
		
		referenceMap.put("optionalCover", bean.getDocumentDetails().getOptionalCovers());
		optionalCoversTable.setReference(referenceMap);
		
		optionalCoverLayout = new VerticalLayout();
		optionalCoverLayout.setWidth("100%");
		optionalCoverLayout.setCaption("Optional Covers");
		optionalCoverLayout.setSpacing(true);
		optionalCoverLayout.setMargin(false);
		optionalCoverLayout.addComponent(optionalCoversTable);	
	//	addOnBenifitLayout.addComponent();
		
		if(this.bean.getDocumentDetails().getOptionalCoversList() != null && ! this.bean.getDocumentDetails().getOptionalCoversList().isEmpty()){
			for (AddOnCoversTableDTO covers : this.bean.getDocumentDetails().getOptionalCoversList()) {
				optionalCoversTable.addBeanToList(covers);
				
			}
		}
		//return rodQueryDetails;
		return optionalCoverLayout;
	}
	
	private VerticalLayout buildReconsiderationLayout()
	{
		reconsiderationLayout = new VerticalLayout();
		reconsiderationLayout.addComponent(cmbReconsiderationRequest);
		
		return 	reconsiderationLayout;
	}

	private void alertMessageForClaimCount(Long claimCount){
		
		String msg = SHAConstants.CLAIM_COUNT_MESSAGE+claimCount;
		
		
   		Label successLabel = new Label(
				"<b style = 'color: black;'>"+msg+"</b>",
				ContentMode.HTML);
//   		successLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//   		successLabel.addStyleName(ValoTheme.LABEL_BOLD);
   		successLabel.addStyleName(ValoTheme.LABEL_H3);
   		Button homeButton = new Button("ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
   		FormLayout firstForm = new FormLayout(successLabel,homeButton);
		Panel panel = new Panel(firstForm);
		
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			panel.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			panel.addStyleName("girdBorder2");
		}
		
		panel.setHeight("103px");
//		panel.setSizeFull();
		
		
		final Window popup = new com.vaadin.ui.Window();
		popup.setWidth("30%");
		popup.setHeight("20%");
//		popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
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
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	
	
	public HorizontalLayout buildBillingDetailsLayout()
	{
		txtBillingRemarks = binder.buildAndBind("Reason for Referring","referToBillEntryBillingRemarks",TextArea.class);
		txtBillingRemarks.setReadOnly(true);
		txtFARemarks = binder.buildAndBind("Financial Approver Remarks","referToBillEntryFAApproverRemarks",TextArea.class);
		txtFARemarks.setReadOnly(true);
		
		FormLayout billingLayout = new FormLayout(txtBillingRemarks);
		HorizontalLayout bLayout = new HorizontalLayout(billingLayout);
		//bLayout.setCaption("Billing Details");
		
		if (null != bean.getStageKey()) {
			if (bean.getStageKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
					&& null != bean.getClaimDTO().getProcessClaimType() && bean.getClaimDTO().getProcessClaimType().equalsIgnoreCase(SHAConstants.PA_TYPE)) {
				bLayout.setCaption("Referred from MA (Non-Hospitalization)");
			}else if (bean.getStageKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
					&& null != bean.getClaimDTO().getProcessClaimType() && bean.getClaimDTO().getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE)){
				bLayout.setCaption("Referred from MA (Hospitalization)");
			}else if (bean.getStageKey().equals(ReferenceTable.BILLING_STAGE)
					&& null != bean.getClaimDTO().getProcessClaimType() && bean.getClaimDTO().getProcessClaimType().equalsIgnoreCase(SHAConstants.PA_TYPE)) {
				bLayout.setCaption("Referred from Billing (Non-Hospitalization)");
			}else if (bean.getStageKey().equals(ReferenceTable.BILLING_STAGE)
					&& null != bean.getClaimDTO().getProcessClaimType() && bean.getClaimDTO().getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE)){
				bLayout.setCaption("Referred from Billing (Hospitalization)");
			}else if (bean.getStageKey().equals(ReferenceTable.FINANCIAL_STAGE)
					&& null != bean.getClaimDTO().getProcessClaimType() && bean.getClaimDTO().getProcessClaimType().equalsIgnoreCase(SHAConstants.PA_TYPE)) {
				bLayout.setCaption("Referred from FA (Non-Hospitalization)");
			}else if (bean.getStageKey().equals(ReferenceTable.FINANCIAL_STAGE)
					&& null != bean.getClaimDTO().getProcessClaimType() && bean.getClaimDTO().getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE)){
				bLayout.setCaption("Referred from FA (Hospitalization)");
			}else {
				bLayout.setCaption("Refer To Bill Entry Details");
			}
		}else {
			bLayout.setCaption("Refer To Bill Entry Details");
		}
		
		Panel billingLayoutPanel = new Panel();
		billingLayoutPanel.setContent(bLayout);
		
		FormLayout falayout = new FormLayout(txtFARemarks);
		HorizontalLayout finlayout = new HorizontalLayout(falayout);
		finlayout.setCaption("Financial Appprover Details");
		
		Panel financialLayoutPanel = new Panel();
		financialLayoutPanel.setContent(finlayout);
		
		//HorizontalLayout referToBillEntryLayout = new HorizontalLayout(bLayout, finlayout);
		HorizontalLayout referToBillEntryLayout = new HorizontalLayout(bLayout);
		referToBillEntryLayout.setSpacing(true);
		referToBillEntryLayout.setMargin(true);
		
		
		return referToBillEntryLayout;
	}
	
	private HorizontalLayout buildDocumentDetailsLayout()
	{
		
		accidentOrDeath = (OptionGroup) binder.buildAndBind("Accident / Death" , "accidentOrDeath" , OptionGroup.class);
		//	optPaymentMode = new OptionGroup("Payment Mode");
		accidentOrDeath.setRequired(true);
		accidentOrDeath.addItems(getReadioButtonOptions());
		accidentOrDeath.setItemCaption(true, "Accident");
		accidentOrDeath.setItemCaption(false, "Death");
		accidentOrDeath.setStyleName("horizontal");
		//Vaadin8-setImmediate() accidentOrDeath.setImmediate(true);
		accidentOrDeath.setReadOnly(false);
		
		accidentOrDeathDate = binder.buildAndBind("Date of Accident / Death" , "accidentOrDeathDate" , DateField.class);
		accidentOrDeathDate.setEnabled(true);
		//Vaadin8-setImmediate() accidentOrDeathDate.setImmediate(true);
			
		cmbDocumentType = binder.buildAndBind("Document Type" , "documentType" , ComboBox.class);
		cmbDocumentType.setEnabled(true);
		//Vaadin8-setImmediate() cmbDocumentType.setImmediate(true);
		
		/*SelectValue selectFreshDocument = new SelectValue();
		selectFreshDocument.setId(null);
		selectFreshDocument.setValue("Fresh Document");

		SelectValue selectReconsiderationDocument = new SelectValue();
		selectReconsiderationDocument.setId(null);
		selectReconsiderationDocument.setValue("Reconsideration Document");
		
		SelectValue selectQueryReplydocument = new SelectValue();
		selectQueryReplydocument.setId(null);
		selectQueryReplydocument.setValue("Query Reply Document");
		
		SelectValue selectPaymentQueryReply = new SelectValue();
		selectPaymentQueryReply.setId(null);
		selectPaymentQueryReply.setValue("Payment Query Reply");
		

		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
		selectVallueList.add(selectFreshDocument);
		selectVallueList.add(selectReconsiderationDocument);
		selectVallueList.add(selectQueryReplydocument);
		selectVallueList.add(selectPaymentQueryReply);
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		cmbDocumentType.setContainerDataSource(selectValueContainer);
		documentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		documentType.setItemCaptionPropertyId("value");*/
		
		
		
		cmbDocumentsReceivedFrom = binder.buildAndBind("Documents Recieved From" , "documentsReceivedFrom" , ComboBox.class);
		//Vaadin8-setImmediate() cmbDocumentsReceivedFrom.setImmediate(true);
		
		documentsReceivedDate = binder.buildAndBind("Documents Recieved Date", "documentsReceivedDate", DateField.class);
		documentsReceivedDate.setValue(new Date());
		documentsReceivedDate.setEnabled(false);
		
		cmbModeOfReceipt = binder.buildAndBind("Mode Of Receipt", "modeOfReceipt", ComboBox.class);
		//Vaadin8-setImmediate() cmbModeOfReceipt.setImmediate(true);
		cmbModeOfReceipt.setEnabled(false);
		
		cmbReconsiderationRequest = binder.buildAndBind("Reconsideration Request", "reconsiderationRequest", ComboBox.class);
		//Vaadin8-setImmediate() cmbReconsiderationRequest.setImmediate(true);
		cmbReconsiderationRequest.setEnabled(false);
		
		txtAdditionalRemarks = binder.buildAndBind("Additional Remarks","additionalRemarks",TextField.class);
		txtAdditionalRemarks.setEnabled(false);
		txtAdditionalRemarks.setMaxLength(4000);;
		
		cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
		/*loadReasonForReconsiderationDropDown();*/
		
	
		txtBenifitClaimedAmnt = binder.buildAndBind("Amount Claimed\n (Benefit)", "benifitClaimedAmount",TextField.class);
		
		//Earlier this was disabled. As a part of fix for ticket GALAXYMAIN-5942, this is made editable.
		txtBenifitClaimedAmnt.setEnabled(true);
		txtBenifitClaimedAmnt.setNullRepresentation("");
		txtBenifitClaimedAmnt.setMaxLength(15);	
		
		workOrNonWorkSpace = (OptionGroup) binder.buildAndBind(
				"Accident", "workOrNonWorkPlace", OptionGroup.class);
		workOrNonWorkSpace.addItems(getReadioButtonOptions());
		workOrNonWorkSpace.setItemCaption(true, "WorkPlace");
		workOrNonWorkSpace.setItemCaption(false, "NonWorkPlace");
		workOrNonWorkSpace.setStyleName("horizontal");
				
		dateOfAccident = (DateField) binder.buildAndBind("Date of Accident",
				"dateOfAccident", DateField.class);
		
		dateOfDeath = (DateField) binder.buildAndBind("Date of Death",
				"dateOfDeath", DateField.class);
		
		dateOfDisablement = (DateField) binder.buildAndBind("Date of Disablement",
				"dateOfDisablement", DateField.class);		
		
		CSValidator benefitAmtValidator = new CSValidator();
		benefitAmtValidator.extend(txtBenifitClaimedAmnt);
		benefitAmtValidator.setRegExp("^[0-9.]*$");
		benefitAmtValidator.setPreventInvalidTyping(true);
	
		
		FormLayout detailsLayout1 = new FormLayout(accidentOrDeath,cmbDocumentsReceivedFrom,documentsReceivedDate,
				cmbModeOfReceipt,cmbDocumentType);
				//detailsLayout1.setMargin(true);
			//	detailsLayout1.setSpacing(true);
		FormLayout detailsLayout2 = new FormLayout(workOrNonWorkSpace,dateOfAccident,dateOfDeath,dateOfDisablement,txtBenifitClaimedAmnt);
				detailsLayout2.setMargin(true);
		//HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,new FormLayout(),new FormLayout(),detailsLayout2);
		HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,detailsLayout2);
		//docDetailsLayout.setComponentAlignment(detailsLayout2, Alignment.MIDDLE_LEFT);
		docDetailsLayout.setMargin(true);
		docDetailsLayout.setSpacing(true);
		//docDetailsLayout.setCaption("Document Details");		
		
		setRequiredAndValidation(cmbDocumentType);
		
		mandatoryFields.add(cmbDocumentType);
		
		if(null != cmbDocumentsReceivedFrom)
		{
			SelectValue selValue = (SelectValue)cmbDocumentsReceivedFrom.getValue();
			if(null != selValue && null != selValue.getId() && (ReferenceTable.RECEIVED_FROM_INSURED.equals(selValue.getId()))){
				
				workOrNonWorkSpace.setEnabled(true);				
			}
			else
			{
				workOrNonWorkSpace.setEnabled(false);	
			}
		}
		
		return docDetailsLayout;
	}
	
	
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
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

	private void loadReasonForReconsiderationDropDown()
	{
		 if(null != cmbReasonForReconsideration)
		 {
			 String reasonForReconsideration = this.bean.getDocumentDetails().getReasonForReconsiderationRequestValue();
			 cmbReasonForReconsideration.setContainerDataSource(reasonForReconsiderationRequest);
			 cmbReasonForReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			 cmbReasonForReconsideration.setItemCaptionPropertyId("value");
			 if(null != reasonForReconsiderationRequest && (null != reasonForReconsideration && !("").equalsIgnoreCase(reasonForReconsideration)))
			 {
				 for(int i = 0 ; i<reasonForReconsiderationRequest.size() ; i++)
				 	{
						if ((reasonForReconsideration).equalsIgnoreCase(reasonForReconsiderationRequest.getIdByIndex(i).getValue()))

						{
							this.cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
						}
					}
			 }
		 }
	}
	
	private HorizontalLayout buildBenifitsLayout()
	{		
		chkDeath = binder.buildAndBind("Death", "death", CheckBox.class);
		//Vaadin8-setImmediate() chkDeath.setImmediate(true);
		
	
		chkPermanentPartialDisability = binder.buildAndBind("Permanent Partial Disability", "permanentPartialDisability", CheckBox.class);
		//Vaadin8-setImmediate() chkPermanentPartialDisability.setImmediate(true);
		
		chkPermanentTotalDisability = binder.buildAndBind("Permanent Total Disability", "permanentTotalDisability", CheckBox.class);
		//Vaadin8-setImmediate() chkPermanentTotalDisability.setImmediate(true);
		
		
		
		chkTemporaryTotalDisability = binder.buildAndBind("Temporary Total Disability", "temporaryTotalDisability", CheckBox.class);
		//Vaadin8-setImmediate() chkTemporaryTotalDisability.setImmediate(true);
		
		
		chkHospitalExpensesCover = binder.buildAndBind("Hospital Expenses Cover", "hospitalExpensesCover", CheckBox.class);
		//Vaadin8-setImmediate() chkHospitalExpensesCover.setImmediate(true);
			
		chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalization", CheckBox.class);
		//Vaadin8-setImmediate() chkhospitalization.setImmediate(true);
		//chkhospitalization.setEnabled(false);
		chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalization", CheckBox.class);
		//Vaadin8-setImmediate() chkPartialHospitalization.setImmediate(true);
		
		
		HorizontalLayout benifitClassificationLayout = new HorizontalLayout(chkDeath,chkPermanentPartialDisability,chkPermanentTotalDisability,chkTemporaryTotalDisability,chkhospitalization,chkPartialHospitalization);
		
		if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(false);
			chkPartialHospitalization.setValue(null);
		}
		
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			if(null != this.bean.getDocumentDetails().getDocumentReceivedFromValue() && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
			{
				chkhospitalization.setEnabled(true);
				chkDeath.setEnabled(false);
				chkPartialHospitalization.setEnabled(false);
				chkPermanentPartialDisability.setEnabled(false);
				chkPermanentTotalDisability.setEnabled(false);
				chkTemporaryTotalDisability.setEnabled(false);		
				addOnCoversTable.setEnabled(false);
				optionalCoversTable.setEnabled(false);
			}			
			else
			{
				chkhospitalization.setEnabled(false);
				chkPartialHospitalization.setEnabled(true);
				chkDeath.setEnabled(true);
				chkPermanentPartialDisability.setEnabled(true);
				chkPermanentTotalDisability.setEnabled(true);
				chkTemporaryTotalDisability.setEnabled(true);
				addOnCoversTable.setEnabled(true);
				optionalCoversTable.setEnabled(true);
			}
			
		}
		
		if((null != chkhospitalization && null != chkhospitalization.getValue()) || (null != chkPartialHospitalization && null != chkPartialHospitalization.getValue()))
		{
			addOnCoversTable.setEnabled(false);
			optionalCoversTable.setEnabled(false);
		}
		
		if(null != bean.getDocumentDetails().getStatusId() &&( bean.getDocumentDetails().getStatusId().equals(ReferenceTable.BILLING_REFER_TO_BILL_ENTRY)
				|| (bean.getDocumentDetails().getStatusId().equals(ReferenceTable.FINANCIAL_REFER_TO_BILL_ENTRY))))
		{
		
		if(null != chkhospitalization && null!= chkhospitalization.getValue())
		{
			addOnCoversTable.setEnabled(false);
			 optionalCoversTable.setEnabled(false);
			 chkhospitalization.setEnabled(true);
				chkDeath.setEnabled(false);
				chkPartialHospitalization.setEnabled(false);
				chkPermanentPartialDisability.setEnabled(false);
				chkPermanentTotalDisability.setEnabled(false);
				chkTemporaryTotalDisability.setEnabled(false);	
		}
		}
		
		if(SHAConstants.YES.equalsIgnoreCase(this.bean.getDocumentDetails().getReconsiderationRequestValue()))
		{		   
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue.getId())))
			{
				chkhospitalization.setValue(false);
				chkPartialHospitalization.setValue(false);
				chkDeath.setValue(false);
				chkPermanentPartialDisability.setValue(false);
				chkPermanentTotalDisability.setValue(false);
				chkTemporaryTotalDisability.setValue(false);
				txtBenifitClaimedAmnt.setValue(null);
				this.bean.getDocumentDetails().setAddOnCoversList(null);
				this.bean.getDocumentDetails().setOptionalCoversList(null);
				
			}
		
		
		}
		
		/*Below code only for Product - MED-PRD-074*/
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
				&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT_KEY)){
			chkhospitalization.setEnabled(false);
			chkTemporaryTotalDisability.setEnabled(false);
		}
		
		if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey() != null
				&& bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey().equals(ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_KEY)){
			chkhospitalization.setEnabled(false);
			chkDeath.setEnabled(true);
			chkPartialHospitalization.setEnabled(false);
			chkPermanentPartialDisability.setEnabled(false);
			chkPermanentTotalDisability.setEnabled(true);
			chkTemporaryTotalDisability.setEnabled(false);
			addOnCoversTable.setEnabled(false);
			optionalCoversTable.setEnabled(false);
		}
		
		//billClassificationLayout.setCaption("Document Details");
		benifitClassificationLayout.setCaption("Benefits");
		benifitClassificationLayout.setSpacing(true);
		benifitClassificationLayout.setMargin(true);
//		billClassificationLayout.setWidth("100%");
		return benifitClassificationLayout;
	}
	
	
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	
	private HorizontalLayout buildChequePaymentLayout()
	{
		cmbPayeeName = (ComboBox) binder.buildAndBind("Payee Name", "payeeName" , ComboBox.class);
		 
		cmbPayeeName.setEnabled(false);
		 
		//cmbPayeeName.setRequired(true);
		
		txtEmailId = (TextField) binder.buildAndBind("Email ID", "emailId" , TextField.class);
		if(null != this.bean.getDocumentDetails().getEmailId())
		{
			txtEmailId.setValue(this.bean.getDocumentDetails().getEmailId());
		}
		txtEmailId.setEnabled(false);
		//txtEmailId.setRequired(true);
		
		txtReasonForChange = (TextField) binder.buildAndBind("Reason For Change(Payee Name)", "reasonForChange", TextField.class);
		txtReasonForChange.setEnabled(false);
		
		txtPanNo = (TextField) binder.buildAndBind("PAN No","panNo",TextField.class);
		
		if(null != this.bean.getDocumentDetails().getPanNo())
		{
			txtPanNo.setValue(this.bean.getDocumentDetails().getPanNo());
		}
		
		txtPanNo.setEnabled(false);
		
		txtLegalHeirFirstName = (TextField) binder.buildAndBind("","legalFirstName",TextField.class);
		txtLegalHeirFirstName.setEnabled(false);
		txtLegalHeirMiddleName = (TextField) binder.buildAndBind("", "legalMiddleName" , TextField.class);
		txtLegalHeirMiddleName.setEnabled(false);

		txtLegalHeirLastName = (TextField) binder.buildAndBind("", "legalLastName" , TextField.class);
		txtLegalHeirLastName.setEnabled(false);
		txtPayableAt = (TextField) binder.buildAndBind("Payable at", "payableAt", TextField.class);
		//txtPayableAt.setRequired(true);
		if(null != this.bean.getDocumentDetails().getPayableAt())
		{
			txtPayableAt.setValue(this.bean.getDocumentDetails().getPayableAt());
		}
		
		txtPayableAt.setEnabled(false);
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{	
			txtEmailId.setReadOnly(true);
			//txtEmailId.setEnabled(false);
			
			txtReasonForChange.setReadOnly(true);
			//txtReasonForChange.setEnabled(false);
			
			txtPanNo.setReadOnly(true);
			//txtPanNo.setEnabled(false);
			
			txtLegalHeirFirstName.setReadOnly(true);
			//txtLegalHeirFirstName.setEnabled(false);
			
			txtLegalHeirMiddleName.setReadOnly(true);
			//txtLegalHeirMiddleName.setEnabled(false);
			
			txtLegalHeirLastName.setReadOnly(true);
			//txtLegalHeirLastName.setEnabled(false);
			
			txtPayableAt.setReadOnly(true);
			//txtPayableAt.setEnabled(false);
			
		}
		
		HorizontalLayout nameLayout = new HorizontalLayout(txtLegalHeirFirstName,txtLegalHeirMiddleName,txtLegalHeirLastName);
		nameLayout.setComponentAlignment(txtLegalHeirFirstName, Alignment.TOP_LEFT);
		nameLayout.setCaption("Legal Heir Name");
		nameLayout.setWidth("100%");
		nameLayout.setSpacing(true);
		nameLayout.setMargin(false);
		FormLayout formLayout1 = null;
		
		if(null != this.bean.getDocumentDetails().getPaymentModeFlag())
		{
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
		}
		else
		{
			formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo);
		}
		
		FormLayout formLayout2 = new FormLayout(cmbPayeeName,txtReasonForChange,nameLayout);

		HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , formLayout2);
		hLayout.setWidth("80%");
		
		return hLayout;
		
		
	}
	
	private HorizontalLayout buildBankTransferLayout()
	{
		txtAccntNo = (TextField)binder.buildAndBind("Account No" , "accountNo", TextField.class);
		txtAccntNo.setRequired(true);
		txtAccntNo.setNullRepresentation("");
		
		if(null != this.bean.getDocumentDetails().getAccountNo())
		{
			txtAccntNo.setValue(this.bean.getDocumentDetails().getAccountNo());
		}
		
		txtAccntNo.setEnabled(false);
		
		txtIfscCode = (TextField) binder.buildAndBind("IFSC Code", "ifscCode", TextField.class);
		txtIfscCode.setRequired(true);
		txtIfscCode.setNullRepresentation("");
		
		if(null != this.bean.getDocumentDetails().getIfscCode())
		{
			txtIfscCode.setValue(this.bean.getDocumentDetails().getAccountNo());
		}
		txtIfscCode.setEnabled(false);
		
		txtBranch = (TextField) binder.buildAndBind("Branch", "branch", TextField.class);
		txtBranch.setNullRepresentation("");
		
		if(null != this.bean.getDocumentDetails().getBranch())
		{
			txtBranch.setValue(this.bean.getDocumentDetails().getBranch());
		}
		txtBranch.setEnabled(false);
		
		txtBankName = (TextField) binder.buildAndBind("Bank Name", "bankName", TextField.class);
		txtBankName.setNullRepresentation("");
		
		if(null != this.bean.getDocumentDetails().getBankName())
		{
			txtBankName.setValue(this.bean.getDocumentDetails().getBranch());
		}
		txtBankName.setEnabled(false);
		
		txtCity = (TextField) binder.buildAndBind("City", "city", TextField.class);
		txtCity.setNullRepresentation("");
		
		if(null != this.bean.getDocumentDetails().getCity())
		{
			txtCity.setValue(this.bean.getDocumentDetails().getCity());
		}
		txtCity.setEnabled(false);
		
		if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			txtAccntNo.setReadOnly(true);
			//txtAccntNo.setEnabled(false);
			
			txtIfscCode.setReadOnly(true);
			//txtIfscCode.setEnabled(false);
			
			txtBranch.setReadOnly(true);
			//txtBranch.setEnabled(false);
			
			txtBankName.setReadOnly(true);
			//txtBankName.setEnabled(false);
			
			
			txtCity.setReadOnly(true);
			//txtCity.setEnabled(false);
			
		}
		
		FormLayout bankTransferLayout1 = new FormLayout(txtAccntNo,txtIfscCode,txtBankName,txtCity);
		/*formLayout1.addComponent(txtAccntNo);
		formLayout1.addComponent(txtIfscCode);
		formLayout1.addComponent(txtBankName);
		formLayout1.addComponent(txtCity);
		formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
		*/
		TextField dField = new TextField();
		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField.setReadOnly(true);
		FormLayout bankTransferLayout2 = new FormLayout(dField,txtBranch);
		
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , bankTransferLayout2);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
		hLayout.setWidth("80%");
		
		return hLayout;
	}

	
	
	
	//private RODQueryDetailsTable buildQueryDetailsLayout()
	private VerticalLayout buildQueryDetailsLayout()
	{
		
		if (queryDetailsPanel != null
				&& queryDetailsPanel.getComponentCount() == 0) {
			
			List<RODQueryDetailsDTO> rodQueryDetailsList = this.bean.getRodQueryDetailsList();
		//	rodQueryDetails = rodQueryDetailsObj.get();
			rodQueryDetails.initpresenterString(SHAConstants.PA_BILL_ENTRY);
			rodQueryDetails.init("", false, false,reconsiderationRequest);
			loadQueryDetailsTableValues();
		//	rodQueryDetails.setEnabled(true);
			queryDetailsPanel.addComponent(rodQueryDetails);
		}
		
		return queryDetailsPanel;
		
	}
	
	
	private HorizontalLayout buildPaymentQueryDetailsLayout()
	{
		//rodQueryDetails = rodQueryDetailsObj.get();
		paymentQueryDetails.initpresenterString(SHAConstants.PA_BILL_ENTRY);
		paymentQueryDetails.init("", false, false,reconsiderationRequest);
//		rodQueryDetails.setReference(this.referenceData);
		loadPaymentQueryDetailsTableValues();
		//rodQueryDetails.setEditable(true);
			//rodQueryDetails.setTableList(rodQueryDetailsList);
			/*for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
				rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
			}*/
			
		
		/**/
		
		HorizontalLayout queryDetailsLayout = new HorizontalLayout(paymentQueryDetails);
		queryDetailsLayout.setWidth("100%");
		queryDetailsLayout.setCaption("View Query Details");
		queryDetailsLayout.setSpacing(true);
		queryDetailsLayout.setMargin(true);
		
		//return rodQueryDetails;
		return queryDetailsLayout;
		
	}
	
	//private ReconsiderRODRequestTable buildReconsiderRequestLayout()
	//private HorizontalLayout buildReconsiderRequestLayout()
	private VerticalLayout buildReconsiderRequestLayout()
	{
		//List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		this.reconsiderRODRequestList = this.bean.getReconsiderRodRequestList();
		//reconsiderRequestDetails.init("", false, false);
		//reconsiderRequestDetails.initPresenter(SHAConstants.BILL_ENTRY);
		reconsiderRequestDetails.initPresenter(SHAConstants.PA_BILL_ENTRY);
		reconsiderRequestDetails.init();
		reconsiderRequestDetails.setViewDetailsObj(viewDetails);
		if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
		{
			for (ReconsiderRODRequestTableDTO reconsiderList : reconsiderRODRequestList) {
				if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
				{
					Boolean isSelect = reconsiderationMap.get(reconsiderList.getAcknowledgementNo());
					reconsiderList.setSelect(isSelect);
				}
				//reconsiderList.setSelect(null);
				reconsiderRequestDetails.addBeanToList(reconsiderList);
			}
			//reconsiderRequestDetails.setTableList(reconsiderRODRequestList); 
		}
		if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
		{
			reconsiderationMap.clear();
		}
		
		//HorizontalLayout reconsiderRequestLayout = new HorizontalLayout(reconsiderRequestDetails);
		VerticalLayout reconsiderRequestLayout = new VerticalLayout(cmbReasonForReconsideration, reconsiderRequestDetails);
		reconsiderRequestLayout.setCaption("Select Earlier Request to be Reconsidered");
		reconsiderRequestLayout.setSpacing(true);
		reconsiderRequestLayout.setMargin(true);
		//return reconsiderRequestDetails;
		return reconsiderRequestLayout;
		
	}
	
	private void addListener()
	{	
		/*accidentOrDeath.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue docRecFromValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
				Boolean accidentDeath = event.getProperty().getValue() != null ? (Boolean) event.getProperty().getValue() : null;
				if(accidentDeath != null && !accidentDeath) {
					if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
							   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId()))		
							   && docRecFromValue != null
							   && ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromValue.getId())) {
								
						buildNomineeLayout();
					}
					else {
						
						if(nomineeDetailsTable != null) { 
							documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
						}
						if(legaHeirLayout != null) {
							documentDetailsPageLayout.removeComponent(legaHeirLayout);
						}
					}
				}
				else{
					
					if(nomineeDetailsTable != null) { 
						documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
					}
					if(legaHeirLayout != null) {
						documentDetailsPageLayout.removeComponent(legaHeirLayout);
					}
				}
				
			}
		});*/
		
		cmbDocumentType
	.addValueChangeListener(new Property.ValueChangeListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			SelectValue value = (SelectValue) event.getProperty().getValue();
			if(null != value)
			{	
				if (documentDetailsPageLayout != null
						&& documentDetailsPageLayout.getComponentCount() > 0) {
					documentDetailsPageLayout.removeAllComponents();
				}
				if(("Fresh Document").equalsIgnoreCase(value.getValue()))
				{	
					//isReconsiderRequestFlag = false;
					unbindField(getListOfChkBox());			
					/*documentDetailsPageLayout.addComponent(documentDetailsLayout);
					documentDetailsPageLayout.addComponent(buildBillClassificationLayout());
					documentDetailsPageLayout.addComponent(buildAddOncoversDetailsLayout());*/
				//	documentDetailsPageLayout.addComponents(documentDetailsLayout,buildBillClassificationLayout(),buildAddOncoversDetailsLayout(),docCheckListLayout,remarksLayout);
					documentDetailsPageLayout.addComponents(billEntryLayout,documentDetailsLayout,buildBenifitsLayout(),buildAddOncoversDetailsLayout(),buildOptionalCoversDetailsLayout(),docCheckListLayout);
					resetReconsiderationValue();
				}
				
				else if(("Reconsideration Document").equalsIgnoreCase(value.getValue())){
					
					unbindField(getListOfChkBox());		
					//documentDetailsPageLayout.addComponents(documentDetailsLayout,buildReconsiderRequestLayout(),docCheckListLayout,remarksLayout);
					documentDetailsPageLayout.addComponents(billEntryLayout,documentDetailsLayout,buildReconsiderRequestLayout(),docCheckListLayout);
				}
				else if(("Query Reply Document").equalsIgnoreCase(value.getValue())){
					unbindField(getListOfChkBox());
					//documentDetailsPageLayout.addComponents(documentDetailsLayout,buildQueryDetailsLayout(),docCheckListLayout,remarksLayout);
					documentDetailsPageLayout.addComponents(billEntryLayout,documentDetailsLayout,buildQueryDetailsLayout(),docCheckListLayout);
					//documentDetailsPageLayout.addComponent(buildQueryDetailsLayout());
				}
				else
				{
					unbindField(getListOfChkBox());
					//documentDetailsPageLayout.addComponents(documentDetailsLayout,buildQueryDetailsLayout(),docCheckListLayout,remarksLayout);	
					documentDetailsPageLayout.addComponents(billEntryLayout,documentDetailsLayout,buildPaymentQueryDetailsLayout(),docCheckListLayout);
				}
				
				/*SelectValue docRecFromValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
				Boolean accidentDeath = accidentOrDeath.getValue() != null ? (Boolean) accidentOrDeath.getValue() : null;
				if(accidentDeath != null && !accidentDeath) {
					if((bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
							   && ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId()))		
							   && docRecFromValue != null
							   && ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromValue.getId())) {
								
						buildNomineeLayout();
					}
					else {
						
						if(nomineeDetailsTable != null) { 
							documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
						}
						if(legaHeirLayout != null) {
							documentDetailsPageLayout.removeComponent(legaHeirLayout);
						}
					}
				}
				else{
					
					if(nomineeDetailsTable != null) { 
						documentDetailsPageLayout.removeComponent(nomineeDetailsTable);
					}
					if(legaHeirLayout != null) {
						documentDetailsPageLayout.removeComponent(legaHeirLayout);
					}
				}*/
				
				
				addBenefitLister();
			}
			
		}
	});
	
		cmbReconsiderationRequest
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{	
					if (reconsiderationLayout != null
							&& reconsiderationLayout.getComponentCount() > 0) {
						reconsiderationLayout.removeAllComponents();
					}
					if(("NO").equalsIgnoreCase(value.getValue()))
					{	
						unbindField(getListOfChkBox());
						//reconsiderationLayout.addComponent(buildBillClassificationLayout());
					//	reconsiderationLayout.addComponent(buildQueryDetailsLayout());
						
						/*if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))
						{
							chkhospitalization.setEnabled(true);
						}
						else
						{
							chkPartialHospitalization.setEnabled(true);
						}*/
						
						//addBillClassificationLister();
					}
					else
					{
						unbindField(getListOfChkBox());
//						reconsiderationTblLayout = buildReconsiderRequestLayout();		 			

						reconsiderationLayout.addComponent(buildReconsiderRequestLayout());
					}
				}
				
			}
		});
		
		
		/**
		 * 
		 * The document received date listener is commented for issue 819.
		 * As a production feedback, this validation is not required in
		 * bill entry level.
		 * 
		 * **/
				
		/*documentsReceivedDate.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date value = (Date)event.getProperty().getValue();
				if(null != value)
				{
					if(value.after(new Date()))
					{
						documentsReceivedDate.setValue(null);
						HorizontalLayout layout = new HorizontalLayout(
								new Label("Document Received Date cannot be greater than current system date."));
						layout.setMargin(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("");
					//	dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
					}
					else if(getDifferenceBetweenDates(value) > 7)
					{
						documentsReceivedDate.setValue(null);
						HorizontalLayout layout = new HorizontalLayout(
								new Label("Document Received Date can 7 days prior to current system date."));
						layout.setMargin(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
					}
				}
			}
		});*/
		
		
		cmbDocumentsReceivedFrom.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(null != value)
				{
					if(("Insured").equalsIgnoreCase(value.getValue()))
					{	
						/*txtAcknowledgementContactNo.setRequired(true);
						txtAcknowledgementContactNo.setReadOnly(false);
						txtAcknowledgementContactNo.setEnabled(true);*/
						/*txtEmailId.setReadOnly(false);
						txtEmailId.setEnabled(true);*/
						//txtEmailId.setRequired(true);
						
						chkhospitalization.setEnabled(false);
						chkPartialHospitalization.setEnabled(true);
						chkDeath.setEnabled(true);
						chkPermanentPartialDisability.setEnabled(true);
						chkPermanentTotalDisability.setEnabled(true);
						chkTemporaryTotalDisability.setEnabled(true);
						
						if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))// && null != chkPartialHospitalization)
						{
							chkhospitalization.setEnabled(false);
							if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							chkhospitalization.setValue(false);
							
							if(null != chkPartialHospitalization)
							chkPartialHospitalization.setEnabled(true);
							
						/*	chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);*/
							/*if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
							{
								//chkPreHospitalization.setEnabled(false);
								chkPreHospitalization.setEnabled(true);
							}
							//chkPreHospitalization.setEnabled(true);
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
							{
								chkPostHospitalization.setEnabled(true);
								//chkPostHospitalization.setEnabled(false);
							}
							if(null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.isEnabled())
							{
								chkLumpSumAmount.setEnabled(true);
							}
							if(null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.isEnabled())
							{
								chkAddOnBenefitsHospitalCash.setEnabled(true);
							}
							if(null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.isEnabled())
							{
								chkAddOnBenefitsPatientCare.setEnabled(true);
							}
							//chkPartialHospitalization.setValue(false);
*/							
						}
						if(null != chkhospitalization && ("Reimbursement").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkhospitalization)
						{
							if(chkhospitalization.getValue() != null && chkhospitalization.getValue()){
								chkhospitalization.setEnabled(true);
							}else{
								chkhospitalization.setEnabled(false);
							}
							if(null != chkPartialHospitalization)
							{
								chkPartialHospitalization.setEnabled(false);
								if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
									chkPartialHospitalization.setValue(false);
							}
							
							
							//chkPartialHospitalization.setEnabled(true);
							
							/*chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);*/
							
						/*	if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
							{
								//chkPreHospitalization.setEnabled(false);
								chkPreHospitalization.setEnabled(true);
							}
							//chkPreHospitalization.setEnabled(true);
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
							{
								//chkPostHospitalization.setEnabled(false);
								chkPostHospitalization.setEnabled(true);
							}*/
							
							//chkPartialHospitalization.setValue(false);
							
						}
					}
					/*if(("Insured").equalsIgnoreCase(value.getValue()))
					{	
						
						txtEmailId.setReadOnly(false);
						txtEmailId.setEnabled(true);
						txtEmailId.setRequired(true);
						
						if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkPartialHospitalization)
						{
							chkhospitalization.setEnabled(false);
							if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							chkhospitalization.setValue(false);
							
							chkPartialHospitalization.setEnabled(true);
							
							chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);
							//chkPartialHospitalization.setValue(false);
							
						}
						else
						{
							chkhospitalization.setEnabled(true);
						}
						
						
						
						
						setRequiredAndValidation(txtEmailId);
						
						mandatoryFields.add(txtEmailId);
					}*/
					else
					{
						
						
						chkhospitalization.setEnabled(true);
						chkDeath.setEnabled(false);
						chkPartialHospitalization.setEnabled(false);
						chkPermanentPartialDisability.setEnabled(false);
						chkPermanentTotalDisability.setEnabled(false);
						chkTemporaryTotalDisability.setEnabled(false);	
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
						/*txtEmailId.setReadOnly(false);
						//txtEmailId.setEnabled();
						txtEmailId.setValue(null);
						txtEmailId.setReadOnly(true);
						txtEmailId.setEnabled(false);
						txtEmailId.setRequired(false);*/
						/*if(null != chkPartialHospitalization && null != chkhospitalization && null != chkPreHospitalization && null != chkPostHospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))
						{
							chkPartialHospitalization.setEnabled(false);
							if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
							chkPartialHospitalization.setValue(false);
							
							chkhospitalization.setEnabled(true);
							
							chkPreHospitalization.setEnabled(false);
							chkPostHospitalization.setEnabled(false);
						}*/
						if(null != chkhospitalization)
							chkhospitalization.setEnabled(true);
						if(null != chkHospitalizationRepeat)
					//	chkHospitalizationRepeat.setEnabled(true);
							chkHospitalizationRepeat.setEnabled(false);
						if(null != chkPartialHospitalization)
						{

							chkPartialHospitalization.setEnabled(false);
							chkPartialHospitalization.setValue(null);
						}
					/*	if(null != chkPreHospitalization)
						{

							chkPreHospitalization.setEnabled(false);
							chkPreHospitalization.setValue(null);
						}
						if(null != chkPostHospitalization)
						{

							chkPostHospitalization.setEnabled(false);
							chkPostHospitalization.setValue(null);
						}
						//	chkPostHospitalization.setEnabled(false);
						if(null != chkLumpSumAmount)
						{
							chkLumpSumAmount.setEnabled(false);
							chkLumpSumAmount.setValue(null);
						}
						if(null != chkAddOnBenefitsHospitalCash)
						{
							chkAddOnBenefitsHospitalCash.setEnabled(false);
							chkAddOnBenefitsHospitalCash.setValue(null);
							//chkAddOnBenefitsHospitalCash.setEnabled(false);
						}
						if(null != chkAddOnBenefitsPatientCare)
						{
							chkAddOnBenefitsPatientCare.setEnabled(false);
							chkAddOnBenefitsPatientCare.setValue(null);
						}
*/
						
						//mandatoryFields.remove(txtEmailId);
					}
					//loadQueryDetailsTableValues();
				}
				
			}
		});
		

		dateOfAccident.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfAccident.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfAccident.setValue(null);
						showErrorMessage("Please Enter a valid Accident Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date currentDate = new Date();
				Date policyFrmDate = null;
				Date policyToDate = null;
				if (policy != null) {
					policyFrmDate = policy.getPolicyFromDate();
					policyToDate = policy.getPolicyToDate();
				}
				if (enteredDate != null && policyFrmDate != null
						&& policyToDate != null) {
					if (!enteredDate.after(policyFrmDate)
							|| enteredDate.compareTo(policyToDate) > 0) {
						event.getProperty().setValue(null);
					
						showErrorMessage("Accident Date is not in range between Policy From Date and Policy To Date.");
					}
				}
			}
		});		
		
		dateOfDeath.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfDeath.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfDeath.setValue(null);
						showErrorMessage("Please Enter a valid Death Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date currentDate = new Date();
				Date policyFrmDate = null;
				Date policyToDate = null;
				if (policy != null) {
					policyFrmDate = policy.getPolicyFromDate();
					policyToDate = policy.getPolicyToDate();
				}
				/*if (enteredDate != null && policyFrmDate != null
						&& policyToDate != null) {
					if (!enteredDate.after(policyFrmDate)
							|| enteredDate.compareTo(policyToDate) > 0) {
						event.getProperty().setValue(null);
					
						showErrorMessage("Death Date is not in range between Policy From Date and Policy To Date.");
					}
				}*/
				if(null != enteredDate)
				{
					Date accidentDate = new Date();
					if(null != dateOfAccident.getValue()){
						accidentDate = dateOfAccident.getValue();
					}
					if (accidentDate != null && null != enteredDate) {
						
						Long diffDays = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
						
						if(null != diffDays && diffDays>365)
						{
							showErrorMessage("The date of death captured is beyond 12 months from the date of accident");
						}
					}
				}
				
				
				
			}
		});		
		
		dateOfDisablement.addValueChangeListener(new Property.ValueChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				Policy policy = (Policy) ((DateField) event
						.getProperty()).getData();

				Date enteredDate = (Date) ((DateField) event
						.getProperty()).getValue();
				if (enteredDate != null) {

					try {
						dateOfDisablement.validate();
						enteredDate = (Date) event.getProperty()
								.getValue();
					} catch (Exception e) {
						dateOfDisablement.setValue(null);
						showErrorMessage("Please Enter a valid Disablement Date");
						// Notification.show("Please Enter a valid Date");
						return;
					}
				}

				Date accidentDate = new Date();
				if(null != dateOfAccident.getValue()){
					accidentDate = dateOfAccident.getValue();
				}
				if (accidentDate != null && null != enteredDate) {
					
					Long diffMonths = SHAUtils.getDaysBetweenDate(accidentDate,enteredDate);
					
					if(null != diffMonths && diffMonths>365)
					{
						showErrorMessage("The date of disablement captured is beyond 12 months from the date of accident");
					}
				}
				
			}
		});				
	

	}
	

	
	
	
	private int getDifferenceBetweenDates(Date value)
	{
		
		long currentDay = new Date().getTime();
		long enteredDay = value.getTime();
		int diff = (int)((currentDay-enteredDay))/(1000 * 60 * 60 * 24);
		return diff;
	}
	
	
	
	public void loadContainerDataSources(Map<String, Object> referenceDataMap)
	{
		reconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("commonValues");
		 cmbReconsiderationRequest.setContainerDataSource(reconsiderationRequest);
		 cmbReconsiderationRequest.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbReconsiderationRequest.setItemCaptionPropertyId("value");
		 for(int i = 0 ; i<reconsiderationRequest.size() ; i++)
		 {
			 if(null != reconsiderationRequest.getIdByIndex(i) && null != reconsiderationRequest.getIdByIndex(i).getValue())
			 {
				if ((this.bean.getDocumentDetails().getReconsiderationRequestValue()).equalsIgnoreCase(reconsiderationRequest.getIdByIndex(i).getValue().trim()))
				{
					this.cmbReconsiderationRequest.setValue(reconsiderationRequest.getIdByIndex(i));
				}
			 }
		}
		 
		 reasonForReconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("reasonForReconsiderationRequest");
		 loadReasonForReconsiderationDropDown();
		 
		 modeOfReceipt = (BeanItemContainer<SelectValue>) referenceDataMap.get("modeOfReceipt");
		 cmbModeOfReceipt.setContainerDataSource(modeOfReceipt);
		 cmbModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbModeOfReceipt.setItemCaptionPropertyId("value");
		 
		 for(int i = 0 ; i<modeOfReceipt.size() ; i++)
		 	{
				if ((this.bean.getDocumentDetails().getModeOfReceiptValue()).equalsIgnoreCase(modeOfReceipt.getIdByIndex(i).getValue()))
				{
					this.cmbModeOfReceipt.setValue(modeOfReceipt.getIdByIndex(i));
				}
			}
		 
		 docReceivedFromRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("docReceivedFrom");
		 cmbDocumentsReceivedFrom.setContainerDataSource(docReceivedFromRequest);
		 cmbDocumentsReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbDocumentsReceivedFrom.setItemCaptionPropertyId("value");
		 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
		 	{
				if ((this.bean.getDocumentDetails().getDocumentReceivedFromValue()).equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}
		 cmbDocumentsReceivedFrom.setEnabled(false);
		 
		 documentType = (BeanItemContainer<SelectValue>) referenceDataMap.get("documentType");
		 cmbDocumentType.setContainerDataSource(documentType);
		 cmbDocumentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbDocumentType.setItemCaptionPropertyId("value");
		 
		 documentType = (BeanItemContainer<SelectValue>) referenceDataMap.get("documentType");
		 cmbDocumentType.setContainerDataSource(documentType);
		 cmbDocumentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbDocumentType.setItemCaptionPropertyId("value");	
		
		 
		 if(null != cmbPayeeName)
		 {
		 payeeNameList = (BeanItemContainer<SelectValue>) referenceDataMap.get("payeeNameList");
		 cmbPayeeName.setContainerDataSource(payeeNameList);
		 cmbPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbPayeeName.setItemCaptionPropertyId("value");
		 for(int i = 0 ; i<payeeNameList.size() ; i++)
		 	{
				if ((this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProposerFirstName()).equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
				{
					this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
				}
			}
		 if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
			{
				cmbPayeeName.setReadOnly(true);
				cmbPayeeName.setEnabled(false);
			}
		 }
		 
		// documentCheckList.setReceivedStatus((BeanItemContainer<SelectValue>)referenceDataMap.get("docReceivedStatus"));
		// documentCheckListValidation.setReference(referenceDataMap);
		 if(null != documentCheckListValidation)
			{
				documentCheckListValidation.setReferenceData(referenceDataMap);
			}
		 this.docsDetailsList = (List<DocumentDetailsDTO>)referenceDataMap.get("validationDocList");
		// rodQueryDetails.generateDropDown(reconsiderationRequest);
		// this.docDTO = (DocumentDetailsDTO) referenceDataMap.get("billClaissificationDetails");
		 this.docDTO = (List<DocumentDetailsDTO>) referenceDataMap.get("billClaissificationDetails");
		 
			referenceMap.put("covers", bean.getDocumentDetails().getAdditionalCovers());
//			addOnCoversTable.setReference(referenceMap);
		 
		 invokeBenifitListner();
		 
		 displayAmountClaimedDetails();
		 
	//	 loadQueryDetailsTableValues();
		 
		 setValuesFromDTO();
		// validateBillClassificationDetails();

	}
	
	
	private List<Field<?>> getListOfChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkDeath);
		fieldList.add(chkPermanentPartialDisability);
		fieldList.add(chkPermanentTotalDisability);
		fieldList.add(chkTemporaryTotalDisability);
		fieldList.add(chkHospitalExpensesCover);
		fieldList.add(chkhospitalization);
		fieldList.add(chkPartialHospitalization);
		if(null != cmbReasonForReconsideration)
			fieldList.add(cmbReasonForReconsideration);


		if(null != cmbDocumentType)
			fieldList.add(cmbDocumentType);
		return fieldList;
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
		fieldList.add(txtPayableAt);
		return fieldList;
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
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && propertyId != null) {
				this.binder.unbind(field);
			}
		}
	}
	
	private void optPaymentCancellationListener()
	{
		optPaymentCancellation.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean value = (Boolean) event.getProperty().getValue();
				String message = null;
				if(null != value && value)
				{
					message = "Please confirm that payment cancellation needs to be done and you have recieved the cheque/DD for cancellation";
				}
				else
				{
					message = "Are you sure that payment cancellation is not required and the payment has been accepted";
				}
				Label successLabel = new Label("<b style = 'color: green;'>"+message+"</b>", ContentMode.HTML);
				
//				Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
				
				Button homeButton = new Button("Ok");
				homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				Button cancelButton = new Button("Cancel");
				cancelButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
				horizontalLayout.setSpacing(true);
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

						//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED, null);
						
					}
				});
				cancelButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();

						//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVED, null);
						
					}
				});
			}

		});
	}

	
	public boolean validatePage() {
		

		Boolean hasError = false;
	//	showOrHideValidation(true);
		String eMsg = "";
		Boolean isReconsiderationRequest = false;
		
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
					
				}
				hasError = true;
			}
		}
		
	

		if(null != this.cmbReconsiderationRequest )
		{
			SelectValue reconsiderReqValue = (SelectValue)cmbReconsiderationRequest.getValue();
			if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("NO").equalsIgnoreCase(reconsiderReqValue.getValue()))
			{
				if(null != cmbDocumentType && null != cmbDocumentType.getValue())
				{
					SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
					if(null != selValue && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue.getId())))
					{
				
				if((null != this.chkDeath && (null == this.chkDeath.getValue()) || this.chkDeath.getValue().equals(false)) &&
						(null != this.chkPermanentPartialDisability &&  (null == this.chkPermanentPartialDisability.getValue()) || this.chkPermanentPartialDisability.getValue().equals(false)) &&
						
						(null != this.chkPermanentTotalDisability && (null == this.chkPermanentTotalDisability.getValue()) || this.chkPermanentTotalDisability.getValue().equals(false)) &&
						
						(null != this.chkTemporaryTotalDisability && (null == this.chkTemporaryTotalDisability.getValue()) || this.chkTemporaryTotalDisability.getValue().equals(false)) &&
						
					//	(null != this.chkHospitalExpensesCover && null != this.chkHospitalExpensesCover.getValue() && this.chkHospitalExpensesCover.getValue().equals(false)) &&
						
						(null != this.chkhospitalization &&( null == this.chkhospitalization.getValue()) || this.chkhospitalization.getValue().equals(false)) &&
						
						(null != this.chkPartialHospitalization && ( null == this.chkPartialHospitalization.getValue()) || this.chkPartialHospitalization.getValue().equals(false)) &&
						
						(null == addOnCoversTable.getValues() || addOnCoversTable.getValues().isEmpty()) &&
						(null == optionalCoversTable.getValues() || optionalCoversTable.getValues().isEmpty()))
				{
					hasError = true;
					eMsg += "Please select Benefit Or Add on Covers Or Optional Covers</br>";
				}			
			}
						
					List<ReconsiderRODRequestTableDTO> reimbList = bean.getReconsiderRodRequestList();

			/*if(null != this.addOnCoversTable){
				List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();	
				if(null != values){	
					
					//(PABillEntryDocumentDetailsPresenter.VALIDATE_ADD_ON_COVERS_REPEAT, bean.getReconsiderRODdto().getRodKey(),values);
					if(null != reimbList && !reimbList.isEmpty())
					{
					for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reimbList) {
						
						fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_ADD_ON_COVERS_REPEAT, reconsiderRODRequestTableDTO.getRodKey(),values);
					}
					}
					
				for (int cover = 0; cover < values.size()-1; cover++) 
				{
					   for (int cover1 = cover+1; cover1 < values.size(); cover1++) 
					   {
					      if(values.get(cover).getCovers().getId() == values.get(cover1).getCovers().getId())
					      {
					    	  hasError = true;
					    	  eMsg += "you can not select same Additional Covers</br>";
					      }
					   }
				}
				}
			}*/
			/*else if(null != this.optionalCoversTable){
				List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();
				if(null != values){
					
					if(null != reimbList && !reimbList.isEmpty())
					{
					for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : reimbList) {
						
						fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_OPTIONAL_COVERS_REPEAT, reconsiderRODRequestTableDTO.getRodKey(),values);
					}
					}
					//fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_OPTIONAL_COVERS_REPEAT, bean.getReconsiderRODdto().getRodKey(),values);
					
				for (int cover = 0; cover < values.size()-1; cover++) 
					   for (int cover1 = cover+1; cover1 < values.size(); cover1++) 
					      if(values.get(cover).getCovers().getId() == values.get(cover1).getCovers().getId())
					      {
					    	  hasError = true;
					    	  eMsg += "you can not select same Optional Covers</br>";
					      }
				}
			}*/
			}
					/*else if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("YES").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
					{
						isReconsiderationRequest = true;
						if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
						{
							hasError = true;
							eMsg += "No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>";
						}
						else if(null != this.reconsiderDTO)
						{
							//ReconsiderRODRequestTableDTO reconsiderDTO = this.bean.getReconsiderRODdto();
							if(!(null != this.reconsiderDTO.getSelect() && this.reconsiderDTO.getSelect()))
							{
								hasError = true;
								eMsg += "Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to NO to proceed further </br>";
							}
						}
					}*/
			
				}
		}
			
		if(null != this.cmbReconsiderationRequest )
		{
			SelectValue reconsiderReqValue = (SelectValue)cmbReconsiderationRequest.getValue();
			if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("NO").equalsIgnoreCase(reconsiderReqValue.getValue()))
			{
				if(null != cmbDocumentType && null != cmbDocumentType.getValue())
				{
					SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
					if(null != selValue && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue.getId())))
					{
				
			if (null != this.addOnCoversTable){
				boolean isValid = this.addOnCoversTable.isValid();
				if(!isValid){
					hasError = true;
					List<String> errors = this.addOnCoversTable.getErrors();
					for (String error : errors) {
						eMsg += error + "</br>";
					}
				}
				else
				{
					List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();	
					if(null != values){	
				//	fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_ADD_ON_COVERS_REPEAT, bean.getClaimDTO().getKey(),values,bean.getDocumentDetails().getRodKey());
					fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_ADD_ON_COVERS_REPEAT, bean.getClaimDTO().getKey(),values,bean.getDocumentDetails().getDocAcknowledgementKey(),bean.getDocumentDetails().getRodKey());
					if(this.isValid){
						hasError = true;
						eMsg += "The Cover  "+coverName+"  has been selected in another ROD for this claim. Please select another Additional Cover" + "</br>";
					}
					}
				}
			}
			
			if (null != optionalCoversTable && null != this.optionalCoversTable.getValues() && !this.optionalCoversTable.getValues().isEmpty()){
				boolean isValid = this.optionalCoversTable.isValid();
				if(!isValid){
					hasError = true;
					List<String> errors = this.optionalCoversTable.getErrors();
					for (String error : errors) {
						eMsg += error + "</br>";
					}
				}
				else
				{
					List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();
					List<ReconsiderRODRequestTableDTO> reimbList = bean.getReconsiderRodRequestList();

					if(null != values){	
						fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_OPTIONAL_COVERS_REPEAT, bean.getClaimDTO().getKey(),values,bean.getDocumentDetails().getDocAcknowledgementKey(),bean.getDocumentDetails().getRodKey());
						if(this.isValid){
							hasError = true;
							eMsg += "The Cover  "+coverName+"  has been selected in another ROD for this claim. Please select another Optional Cover" + "</br>";
						}
					}
				}
			}
					}
				}
			}
		}
		
		if(null != cmbDocumentType && null != cmbDocumentType.getValue())
		{
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue && ((ReferenceTable.PA_QUERY_REPLY_DOCUMENT).equals(selValue.getId())) || ((ReferenceTable.PA_PAYMENT_QUERY_REPLY).equals(selValue.getId())))
			{
		if(!isReconsiderationRequest)
		{
			if(null != this.rodQueryDetails)
			{
				Boolean isValid = rodQueryDetails.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.rodQueryDetails.getErrors();
					for (String error : errors) {
						eMsg += error + "</br>";
					}
				}
			}
		}
			}
		}
		
		if(null != cmbDocumentType && null != cmbDocumentType.getValue())
		{
			SelectValue selValue = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue && ((ReferenceTable.PA_QUERY_REPLY_DOCUMENT).equals(selValue.getId())) 
					|| ((ReferenceTable.PA_PAYMENT_QUERY_REPLY).equals(selValue.getId())) )
			{
		if(!isReconsiderationRequest)
		{
			if(null != this.rodQueryDetails)
			{
				Boolean isQueryReplyNo = rodQueryDetails.isQueryReplyNo();
				if(isQueryReplyNo){							
							
					//documentType = (BeanItemContainer<SelectValue>) referenceDataMap.get("documentType"); 
					if(null != documentType)
					{
					 for(int i = 0 ; i<documentType.size() ; i++)
					 {					
							this.cmbDocumentType.setValue(documentType.getIdByIndex(i));
							hasError = true;
							eMsg += "Query Reply Status selected as NO so document type has been selected as Fresh Document"+ "</br>";
							break;
							//this.cmbDocumentType.setEnabled(false);
						}
					}
				}
						
				}
				
				
					
			}
		}
			}
		if(null != cmbDocumentType && null != cmbDocumentType.getValue() )//&& (ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(cmbDocumentType.getId()))
		{
			SelectValue selValue1 = (SelectValue) cmbDocumentType.getValue();
			if(null != selValue1 && ((ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(selValue1.getId())))
			{
				if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
				{
					hasError = true;
					eMsg += "No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>";
				}
				 if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
				{
					for (ReconsiderRODRequestTableDTO reconsiderDTO  : reconsiderRODRequestList)
					{					
						if(!(null != reconsiderDTO.getSelect() && reconsiderDTO.getSelect()))
					{
						hasError = true;
						eMsg += "Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to proceed further </br>";
					}
					}
				}
				
							}
		}
		/*if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
				}
				hasError = true;
			}
		}*/
		
		/*if(null != this.cmbDocumentsReceivedFrom) {

				if((!(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))))
				{
					hasError = true;
					eMsg += "Please enter email Id </br>";
				}
			}
			 
		}*/
		
		if(ReferenceTable.getGPAProducts().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
			Boolean validationforOutPatientExpenses = validationforOutPatientExpenses();
			if(!validationforOutPatientExpenses){
				hasError = true;
				eMsg += "Can not select any benefit along with Outpatient Expences</br>";
			}
			
			
			
			Boolean validationforEarningParent = validationforOutEarningParentSI();
			if(!validationforEarningParent){
				hasError = true;
				eMsg += "Can not select any benefit along with Earning Parent SI </br>";
			}
			
			
		}
		
		/*Boolean accedentDeath = accidentOrDeath.getValue() != null ? (Boolean) accidentOrDeath.getValue() : null;
		SelectValue docRecFromValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
		if(accedentDeath != null 
				&& !accedentDeath
				&& docRecFromValue != null
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(docRecFromValue.getId())
				&& bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId())) {  

			if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
				List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
			
				if(tableList != null && !tableList.isEmpty()){
					bean.getClaimDTO().getNewIntimationDto().setNomineeList(tableList);
					StringBuffer nomineeNames = new StringBuffer("");
					int selectCnt = 0;
					for (NomineeDetailsDto nomineeDetailsDto : tableList) {
						nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
						if(nomineeDetailsDto.isSelectedNominee()) {
							nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
						    selectCnt++;	
						}
					}
					bean.getClaimDTO().getNewIntimationDto().setNomineeSelectCount(selectCnt);
					if(selectCnt>0){
						bean.getClaimDTO().getNewIntimationDto().setNomineeName(nomineeNames.toString());
					}
					else{
						bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
						eMsg += "Please Select Nominee<br>";		
						hasError = true;
					}							
				}
			}
			else{
				bean.getClaimDTO().getNewIntimationDto().setNomineeList(null);
				bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
				Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
				if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
						&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
				{
					bean.getClaimDTO().getNewIntimationDto().setNomineeName(legalHeirMap.get("FNAME").toString());
					bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());
					
				}
				else{
					bean.getClaimDTO().getNewIntimationDto().setNomineeName(null);
					bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(null);
				}
				
				
				if( (bean.getClaimDTO().getNewIntimationDto().getNomineeName() == null && bean.getClaimDTO().getNewIntimationDto().getNomineeAddr() == null))
				{
					eMsg += "Please Enter Claimant / Legal Heir Details<br>";
					hasError = true;
				}
				else{
					bean.getClaimDTO().getNewIntimationDto().setNomineeName(legalHeirMap.get("FNAME").toString());
					bean.getClaimDTO().getNewIntimationDto().setNomineeAddr(legalHeirMap.get("ADDR").toString());							
				}	
			}
		}*/
	
		if (hasError) {
			Label label = new Label(eMsg, ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);

			hasError = true;
			return !hasError;
		} 
		else
		{
			try {
				this.binder.commit();
				
				
				if(null != this.addOnCoversTable){
					List<AddOnCoversTableDTO> values = this.addOnCoversTable.getValues();
					this.bean.getDocumentDetails().setAddOnCoversList(values);
				}

				if(null != this.optionalCoversTable){
					List<AddOnCoversTableDTO> values = this.optionalCoversTable.getValues();
					this.bean.getDocumentDetails().setOptionalCoversList(values);
				}
				if(null != cmbReasonForReconsideration && null != cmbReasonForReconsideration.getValue())
				{	
					SelectValue selValue = (SelectValue) cmbReasonForReconsideration.getValue();
					bean.getDocumentDetails().setReasonForReconsideration(selValue);
				}
				if(null != txtHospitalizationClaimedAmt)
					hospitalizationClaimedAmt = txtHospitalizationClaimedAmt.getValue();
				
				if(null != txtPreHospitalizationClaimedAmt)
					preHospitalizationAmt = txtPreHospitalizationClaimedAmt.getValue();
				
				if(null != txtPostHospitalizationClaimedAmt)
					postHospitalizationAmt = txtPostHospitalizationClaimedAmt.getValue();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			//showOrHideValidation(false);
			return true;
		}		
	}
	
	

	
	private void setTableValues()
	{
		if(null != this.documentCheckListValidation)
		{
			List<DocumentCheckListDTO> docCheckList = this.bean.getDocumentDetails().getDocumentCheckList();
			BeanItemContainer<SelectValue> checkListTableContainer = this.bean.getCheckListTableContainerForROD();
			
		/*	for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
				this.documentCheckListValidation.addBeanToList(documentCheckListDTO);
			}*/
			
			for (DocumentCheckListDTO documentCheckListDTO : docCheckList) {
				if(null != documentCheckListDTO && null != checkListTableContainer)
					
				{
					for(int i = 0 ; i<checkListTableContainer.size() ; i++)
				 	{
						if (documentCheckListDTO.getDocTypeId() != null && (documentCheckListDTO.getDocTypeId()).equals(checkListTableContainer.getIdByIndex(i).getId()))
						{
							SelectValue particularsValue = new SelectValue();
						//	particularsValue.setId((documentCheckListDTO.getKey()));
							particularsValue.setId((checkListTableContainer.getIdByIndex(i).getId()));
							particularsValue.setValue(checkListTableContainer.getIdByIndex(i).getValue());
							documentCheckListDTO.setParticulars(checkListTableContainer.getIdByIndex(i));
							break;
						}
					}
					//The below code except add bean to list needs to removed.
					//documentCheckListDTO.setRodReceivedStatus(false);
					if(null != documentCheckListDTO.getAckReceivedStatus() && !("").equalsIgnoreCase(documentCheckListDTO.getAckReceivedStatus())
							&& ("Not Applicable").equalsIgnoreCase(documentCheckListDTO.getAckReceivedStatus())
							)
					{
						//this.documentCheckListValidation.get
					}
					this.documentCheckListValidation.addBeanToList(documentCheckListDTO);
				}
			}
			
		}
		
		if(null != addOnCoversTable)
		{
			List<AddOnCoversTableDTO> addOnCoversTableDTOList = this.bean.getDocumentDetails().getAddOnCoversList();
			BeanItemContainer<SelectValue> coverList = this.bean.getDocumentDetails().getAdditionalCovers();
			if(null != addOnCoversTableDTOList && !addOnCoversTableDTOList.isEmpty())
			{
				for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversTableDTOList) {
					
					if(null != coverList)
					{
						for(int i = 0 ; i<coverList.size() ; i++)
					 	{
							if(null != addOnCoversTableDTO.getCovers() && addOnCoversTableDTO.getCovers().getId().equals(coverList.getIdByIndex(i).getId()))
							{
								SelectValue selectValue = new SelectValue();
								selectValue.setId(coverList.getIdByIndex(i).getId());
								selectValue.setValue(coverList.getIdByIndex(i).getValue());
								addOnCoversTableDTO.setCovers(coverList.getIdByIndex(i));
								break;
							}
						}
					}
					
					addOnCoversTable.addBeanToList(addOnCoversTableDTO);					
				}
			}
		}
		
		List<AddOnCoversTableDTO> optionalCoversTableDTOList = this.bean.getDocumentDetails().getOptionalCoversList();
		BeanItemContainer<SelectValue> coverList = this.bean.getDocumentDetails().getOptionalCovers();
		if(null != optionalCoversTableDTOList && !optionalCoversTableDTOList.isEmpty())
		{
			for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCoversTableDTOList) {
				
				if(null != coverList)
				{
					for(int i = 0 ; i<coverList.size() ; i++)
				 	{
						if(null != addOnCoversTableDTO.getOptionalCover() && addOnCoversTableDTO.getOptionalCover().getId().equals(coverList.getIdByIndex(i).getId()))
						{
							SelectValue selectValue = new SelectValue();
							selectValue.setId(addOnCoversTableDTO.getOptionalCover().getId());
							selectValue.setValue(coverList.getIdByIndex(i).getValue());
							addOnCoversTableDTO.setOptionalCover(selectValue);
							break;
						}
					}
				}

				optionalCoversTable.addBeanToList(addOnCoversTableDTO);					
			}
		}

	}
	
	public  void setTableValuesToDTO()
	{
		/**
		 * Get the list of DTO's first.
		 * Loop it and get individual object. And then assign them to dto and set this
		 * dto to list. This final list will be set in  bean again.
		 * 
		 * */
		
		List<DocumentCheckListDTO> objDocCheckList = this.documentCheckListValidation.getValues();
		this.bean.getDocumentDetails().setDocumentCheckList(objDocCheckList);
//		IMSSUPPOR-28935
		if(this.bean.getRodQueryDetailsList() != null && !this.bean.getRodQueryDetailsList().isEmpty()) {
		List<RODQueryDetailsDTO> rodQueryDetailsDTO = this.rodQueryDetails.getValues();
		if(null != rodQueryDetailsDTO && !rodQueryDetailsDTO.isEmpty())
			{
				for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
					if(null != rodQueryDetailsDTO2.getReplyStatus() && ("Yes").equals (rodQueryDetailsDTO2.getReplyStatus().trim()))
					{
						this.bean.setRodqueryDTO(rodQueryDetailsDTO2);
						break;
					}
				}
			}
		}
//		IMSSUPPOR-28935
		if(this.bean.getPaymentQueryDetailsList() != null && !this.bean.getPaymentQueryDetailsList().isEmpty()) {
		List<RODQueryDetailsDTO> rodPaymentQueryDetailsDTO = this.paymentQueryDetails.getValues();
		if(null != rodPaymentQueryDetailsDTO && !rodPaymentQueryDetailsDTO.isEmpty())
			{
				for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodPaymentQueryDetailsDTO) {
					
					this.bean.setRodqueryDTO(null);
					
					if(null != rodQueryDetailsDTO2.getReplyStatus() && ("Yes").equals (rodQueryDetailsDTO2.getReplyStatus().trim()))
					{
						this.bean.setRodqueryDTO(rodQueryDetailsDTO2);
						break;
					}
				}
			}
		}
		
		List<AddOnCoversTableDTO> objAddOnCoversList = this.addOnCoversTable.getValues();
		if(null != objAddOnCoversList && !objAddOnCoversList.isEmpty())
		{
			this.bean.getDocumentDetails().setAddOnCoversList(objAddOnCoversList);
		}
		this.bean.getDocumentDetails().setAddOnCoversDeletedList(this.addOnCoversTable.getDeltedAddOnCoversList());

		
		List<AddOnCoversTableDTO> benefitCoversList = this.optionalCoversTable.getValues();
		if(null != benefitCoversList && !benefitCoversList.isEmpty())
		{
			this.bean.getDocumentDetails().setOptionalCoversList(benefitCoversList);
		}
		this.bean.getDocumentDetails().setOptionalCoversDeletedList(this.optionalCoversTable.getDeltedAddOnCoversList());
	}
	
	public void setValuesFromDTO()
	{
		DocumentDetailsDTO documentDetails = this.bean.getDocumentDetails();
		/*if(null != documentDetails.getDocumentsReceivedFrom())
		{
			 for(int i = 0 ; i<docReceivedFromRequest.size() ; i++)
			 	{
					if ( documentDetails.getDocumentsReceivedFrom().getValue().equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
					{
						this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
					}
				}
		}*/
		
		if(null != documentDetails.getDocumentsReceivedDate())
		{
			documentsReceivedDate.setValue(documentDetails.getDocumentsReceivedDate());
		}
		
		if(null != documentDetails.getModeOfReceipt())
		{
			 for(int i = 0 ; i<modeOfReceipt.size() ; i++)
			 	{
					if ( documentDetails.getModeOfReceipt().getValue().equalsIgnoreCase(modeOfReceipt.getIdByIndex(i).getValue()))
					{
						this.cmbModeOfReceipt.setValue(modeOfReceipt.getIdByIndex(i));
					}
				}
		}
		
		if(null != documentDetails.getReconsiderationRequest())
		{
			for(int i = 0 ; i<reconsiderationRequest.size() ; i++)
		 	{
				if ( documentDetails.getReconsiderationRequest().getValue().equalsIgnoreCase(reconsiderationRequest.getIdByIndex(i).getValue()))
				{
					this.cmbReconsiderationRequest.setValue(reconsiderationRequest.getIdByIndex(i));
				}
			}
		}
		
		if(null != documentDetails.getDocumentType())
		{
			for(int i = 0 ; i<documentType.size() ; i++)
		 	{
				if ( documentDetails.getDocumentType().getValue().equalsIgnoreCase(documentType.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentType.setValue(documentType.getIdByIndex(i));
				}
			}
		}

		/*if(null != documentDetails.getPayeeName())
		{
			for(int i = 0 ; i<payeeNameList.size() ; i++)
		 	{
				if ( documentDetails.getPayeeName().getValue().equalsIgnoreCase(payeeNameList.getIdByIndex(i).getValue()))
				{
					this.cmbPayeeName.setValue(payeeNameList.getIdByIndex(i));
				}
			}
		}*/
		
		/*if(null != documentDetails.getInsuredPatientName())
		{
			for(int i = 0 ; i<insuredPatientList.size() ; i++)
		 	{
				if ( documentDetails.getInsuredPatientName().getValue().equalsIgnoreCase(insuredPatientList.getIdByIndex(i).getValue()))
				{
					this.cmbInsuredPatientName.setValue(insuredPatientList.getIdByIndex(i));
				}
			}
		}*/
	}
	
	
	public void saveReconsideRequestTableValue(ReconsiderRODRequestTableDTO dto,List<UploadDocumentDTO> uploadDocsDTO)
	{
		this.bean.setReconsiderRODdto(dto);
		this.reconsiderDTO = dto;
		/*if(null != this.bean.getUploadDocsList() && !this.bean.getUploadDocsList().isEmpty())
		{
			this.bean.setUploadDocsList(new ArrayList<UploadDocumentDTO>());
			//this.bean.getUploadDocsList().clear();
		}
*/		this.bean.setUploadDocsList(uploadDocsDTO);
		this.bean.setRodNumberForUploadTbl(dto.getRodNo());
		setClaimedAmountField(dto);
		

	}

	public void setDocumentDetailsListForValidation(List<DocumentDetailsDTO> documentDetailsDTO)
	{
		this.docsDetailsList = documentDetailsDTO;
		//validateBillClassification();
	}
	
	
	/*public void validateBillClassificationDetails()
	{
		if(null != docsDetailsList)
		{
			for (DocumentDetailsDTO documentDetailsDTO : docsDetailsList) {
				
				if((("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()) && 
						("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag())) && (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag()) && 
								("Y").equalsIgnoreCase(documentDetailsDTO.getPreHospitalizationFlag())) && (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()) && 
										("Y").equalsIgnoreCase(documentDetailsDTO.getPostHospitalizationFlag())))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization, pre and post hospitalization. Please  link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				else if ((("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()) && 
						("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag())) && (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag())))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization, pre hospitalization. Please choose either post hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				else if ((("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()) && 
						("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag())) && (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag())))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization,  post hospitalization.  Please choose either pre hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				
				else if (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization. Please choose either pre or  post hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				
				else if((("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag()) && 
						("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag())) && (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag()) && 
								("Y").equalsIgnoreCase(documentDetailsDTO.getPreHospitalizationFlag())) && (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()) && 
										("Y").equalsIgnoreCase(documentDetailsDTO.getPostHospitalizationFlag())))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization, pre and post hospitalization. Please  link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				else if ((("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag()) && 
						("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag())) && (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag())))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization, pre hospitalization. Please choose either post hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				else if ((("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag()) && 
						("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag())) && (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag())))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization,  post hospitalization.  Please choose either pre hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
				
				else if (("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag()))
				{
					HorizontalLayout layout = new HorizontalLayout(
							new Label("Already an ROD exist for hospitalization. Please choose either pre or  post hospitalization or link to existing ROD."));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setWidth("50%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);
				}
			}
			
		}
		
	}*/

	
	public Boolean validateBillClassification() 
	{
		Boolean isError = false;
		/**
		 * This is set to true, since now
		 * bill classicfication needs to be editable
		 * for query reply cases. Added as a part of
		 * refer to bill entry enhancement.
		 * */
		isQueryReplyReceived = false;
		if(!isQueryReplyReceived)
		{
			if(null != docDTO && !docDTO.isEmpty())
			{
				for (DocumentDetailsDTO documentDetailsDTO : docDTO) {
				
					if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
					{
						if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
						//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
						{
							/**
							 * The below if conditions are added to make sure, that other classifications can also be selected along with
							 * hospitalization. -- handled in a separate code block.
							 * */
							/*if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue())
							{
								isError = true;
							}*/
							//isError = true;
							//else
							{
								 if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
								{
									isError = false;
								}
								 if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
								{
									isError = false;
								}
								 if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && !this.chkLumpSumAmount.getValue())
								{
									isError = false;
								}
								 if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
								{
									isError = false;
								}
								 if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
								{
									isError = false;
								}
								 if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue())
								{
									isError = false;
								}
							}
							/*else
							{
							isError = true;
							}*/
						}
					}
					/**
					 * The below else is added to make sure that other classifications
					 * cannot be selected without hosp rod.
					 */
					else if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && !this.chkhospitalization.getValue()) 
					{
						if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
							//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
							{
								/**
								 * The below if conditions are added to make sure, that other classifications can also be selected along with
								 * hospitalization.
								 * */
								//isError = true;
								if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
								{
									isError = true;
								}
								 if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
								{
									isError = true;
								}
								 if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && !this.chkLumpSumAmount.getValue())
								{
									isError = true;
								}
								 if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
								{
									isError = true;
								}
								 if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
								{
									isError = true;
								}
								 if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue())
								{
									isError = true;
								}
							}
						
						
					}
					else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) 
					{
						if(("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag()) &&  !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
						//if(documentDetailsDTO.getPartialHospitalizationFlag().equalsIgnoreCase("Y"))
						{
							/*if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue())
							{
								isError = true;
							}
							//isError = true;
							else*/
							{
								 if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
								{
									isError = false;
								}
								 if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
								{
									isError = false;
								}
								 if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && !this.chkLumpSumAmount.getValue())
								{
									isError = false;
								}
								 if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
								{
									isError = false;
								}
								 if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
								{
									isError = false;
								}
								 if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue())
								{
									isError = false;
								}
							}
						}
					}
					else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && !this.chkPartialHospitalization.getValue())
					{
						if(("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag()) && !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
							//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
							{
								/**
								 * The below if conditions are added to make sure, that other classifications can also be selected along with
								 * hospitalization.
								 * */
								//isError = true;
								if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
								{
									isError = true;
								}
								 if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
								{
									isError = true;
								}
								 if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && !this.chkLumpSumAmount.getValue())
								{
									isError = true;
								}
								 if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
								{
									isError = true;
								}
								 if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
								{
									isError = true;
								}
								 if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue())
								{
									isError = true;
								}
								 
								 if(this.bean.getIsAlreadyHospitalizationExist()){
									 isError = false;
								 }
							}
						else
						{
							
						}
					}
					
					/**
					 * Below validation is added for cancel rod scenario. If an hospitalization rod is cancelled and user tries to deselect hospitalization
					 * and select hospitalization repeat, then below validation will not allow user to create an hospitalization repeat rod, since hospitalization
					 * rod is not yet created. -- Added for #3768 
					 */
					else if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && null != this.chkHospitalizationRepeat.getValue())
						{
							if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationRepeatFlag()) && !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
								//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
								{
									/*if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
									{
										isError = true;
									}
									if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
									{
										isError = true;
									}
									if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && !this.chkLumpSumAmount.getValue())
									{
										isError = true;
									}
									if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
									{
										isError = true;
									}
									if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
									{
										isError = true;
									}
									if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue())
									{
										isError = true;
									}*/
									isError = true;
								}
						}
					
				}
			}
			
			/*if(null != docDTO)
			{
				if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
				{
					if(docDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
					{
						isError = true;
					}
				}
				if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) 
				{
					if(docDTO.getPartialHospitalizationFlag().equalsIgnoreCase("Y"))
					{
						isError = true;
					}
				}
			}*/
			else
			{
				if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
				{
					isError = false;
				}
				else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() &&this.chkPartialHospitalization.getValue())
				{
					
					isError = false;
				}
				else if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && !this.chkhospitalization.getValue())
				{
					if((SHAConstants.CLAIMREQUEST_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) )
					{
						isError = false;
					}
					else
					{
						if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
						{
							isError = true;
						}
						if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue())
						{
							isError = true;
						}
					}
				}
				else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && !this.chkPartialHospitalization.getValue())
				{
					if((SHAConstants.CLAIMREQUEST_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) )
					{
						isError = false;
					}
					else
					{
						if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
						{
							isError = true;
						}
						if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
						{
							isError = true;
						}
						if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
						{
							isError = true;
						}
					}
				}
				else if((SHAConstants.CLAIMREQUEST_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) )
				{
					isError = false;
				}
				else
				{
					isError = true;
				}
			}
		}
		return isError;
		
	}
	
	/*public Boolean validateBillClassification() 
	{
		Boolean isError = false;
		if(null != docDTO)
		{
			if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && !this.chkhospitalization.getValue()) 
			{
				if(docDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
				{
					isError = true;
				}
			}
			if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && !this.chkPartialHospitalization.getValue()) 
			{
				//isError = true;
				if(docDTO.getPartialHospitalizationFlag().equalsIgnoreCase("Y"))
				{
					isError = true;
				}
			}
			
			if(null != docDTO && !docDTO.isEmpty())
			{
				for (DocumentDetailsDTO documentDetailsDTO : docDTO) {
				
					if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
					{
						if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()))
						//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
						{
							isError = true;
						}
					}
					if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && !this.chkPartialHospitalization.getValue()) 
					{
						if(("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag()))
						//if(documentDetailsDTO.getPartialHospitalizationFlag().equalsIgnoreCase("Y"))
						{
							isError = true;
						}
					}
					
					*//**
					 * Below validation is added for cancel rod scenario. If an hospitalization rod is cancelled and user tries to deselect hospitalization
					 * and select hospitalization repeat, then below validation will not allow user to create an hospitalization repeat rod, since hospitalization
					 * rod is not yet created. -- Added for #3768 
					 *//*
						if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && null != this.chkHospitalizationRepeat.getValue())
						{
							if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && (ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
								//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
								{
									isError = true;
								}
						}
					
				}
		}
		}
		*//**
		 * Only if ROD is created , we would do be able to create Bill Entry for that.
		 * Therefore , for bill entry screen , first time ROD creation validation for bill classfication
		 * listener is not required .
		 * *//*
		else if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && !this.chkhospitalization.getValue())
		{
			if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
			{
				isError = true;
			}
			if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
			{
				isError = true;
			}
			if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
			{
				isError = true;
			}
			if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
			{
				isError = true;
			}
			if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
			{
				isError = true;
			}
		}
		else if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && !this.chkPartialHospitalization.getValue())
		{
			if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
			{
				isError = true;
			}
			if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
			{
				isError = true;
			}
			if(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue())
			{
				isError = true;
			}
			if(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue())
			{
				isError = true;
			}
			if(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue())
			{
				isError = true;
			}
		}
			else
			{
				isError = true;
			}
		
		return isError;
		
	}
	*/
	
	private void invokeBenifitListner()
	{
		if(null != this.bean.getDocumentDetails().getDeath())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getDeath();
			//unbindField(optPaymentMode);
			if(null != chkDeath)
				chkDeath.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPermanentPartialDisability())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPermanentPartialDisability();
			//unbindField(optPaymentMode);
			if(null != chkPermanentPartialDisability)
				chkPermanentPartialDisability.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPermanentTotalDisability())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPermanentTotalDisability();
			//unbindField(optPaymentMode);
			if(null != chkPermanentTotalDisability)
			chkPermanentTotalDisability.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getTemporaryTotalDisability() )// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getTemporaryTotalDisability();		
			
		if(null != chkTemporaryTotalDisability)
			chkTemporaryTotalDisability.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getHospitalExpensesCover())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getHospitalExpensesCover();
			if(null != chkHospitalExpensesCover)
			{					
				chkHospitalExpensesCover.setValue(val);
			}
		}
		
	}
	
	private void displayAmountClaimedDetails()
	{
		
	}
	
	private void loadQueryDetailsTableValues()
	{
		if(null != rodQueryDetails)
		{
			List<RODQueryDetailsDTO> rodQueryDetailsList = this.bean.getRodQueryDetailsList();
			if(null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty())
			{
				rodQueryDetails.removeRow();
				if(null != this.cmbDocumentsReceivedFrom && null != this.cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
					String docRecFromVal = docRecFrom.getValue();
					for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
						
						if(rodQueryDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docRecFromVal))
						{
							rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
						}
					}
				}
			}
		}
	}
	
	
	private void loadPaymentQueryDetailsTableValues()
	{
		if(null != paymentQueryDetails )
		{
			List<RODQueryDetailsDTO> rodPaymentQueryDetailsList = this.bean.getPaymentQueryDetailsList();
			if(null != rodPaymentQueryDetailsList && !rodPaymentQueryDetailsList.isEmpty())
			{
			//	rodQueryDetails.removeRow();
				if(null != this.cmbDocumentsReceivedFrom && null != this.cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
					String docRecFromVal = docRecFrom.getValue();
					for (RODQueryDetailsDTO rodPaymentQueryDetailsDTO : rodPaymentQueryDetailsList) {
						
						if(rodPaymentQueryDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docRecFromVal))
						{
							paymentQueryDetails.addBeanToList(rodPaymentQueryDetailsDTO);
						}
						else
						{
							if(null != paymentQueryDetails)
							{
								paymentQueryDetails.removeRow();
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Added for enabling bill classification in case of reconsideration.
	 * */
	
	private void setClaimedAmountField(ReconsiderRODRequestTableDTO dto)
	{
		if(!reconsiderationMap.isEmpty())
		{
			reconsiderationMap.clear();
		}
		if(null != dto.getSelect() && dto.getSelect())
		{
			if(null != txtBenifitClaimedAmnt)
			{
				if(null != dto.getBenefitClaimedAmnt() && 0 != dto.getBenefitClaimedAmnt() && !("").equals(dto.getBenefitClaimedAmnt()))
				{
					txtBenifitClaimedAmnt.setValue(String.valueOf(dto.getBenefitClaimedAmnt()));
				}
			}

		}
		else
		{
			if(null != txtBenifitClaimedAmnt)
			{				
					txtBenifitClaimedAmnt.setValue(null);
				
			}

		}
	}
	
	
	private void enableOrDisableBillClassification(Boolean value)
	{
		if(null != chkHospitalizationRepeat)
		{
			chkHospitalizationRepeat.setEnabled(value);
		}
		if(null != chkPreHospitalization)
		{
			chkPreHospitalization.setEnabled(value);
		}
		if(null != chkPostHospitalization)
		{
			chkPostHospitalization.setEnabled(value);
		}
		if(null != chkPartialHospitalization)
		{
			chkPartialHospitalization.setEnabled(value);
		}
		/*if(null != chkPartialHospitalization)
		{
			chkPartialHospitalization.setEnabled(value);
		}
		if(null != chkAddOnBenefitsHospitalCash)
		{
			chkAddOnBenefitsHospitalCash.setEnabled(value);
		}
		if(null != chkAddOnBenefitsPatientCare)
		{
			chkAddOnBenefitsPatientCare.setEnabled(value);
		}*/
	}
	
	public void setDocStatusIfReplyReceivedForQuery(RODQueryDetailsDTO rodQueryDetails)
	
	{	
		/*this.chkhospitalization.setValue(false);
		this.chkPreHospitalization.setValue(false);
		this.chkPostHospitalization.setValue(false);
		this.chkLumpSumAmount.setValue(false);
		this.chkHospitalizationRepeat.setValue(false);
		this.chkPartialHospitalization.setValue(false);
		this.chkAddOnBenefitsHospitalCash.setValue(false);
		this.chkAddOnBenefitsPatientCare.setValue(false);*/
		if(null != rodQueryDetails)
		{
				if(("Yes").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
				{
					isQueryReplyReceived = true;
				/*	
					chkhospitalization.set
					else
					{
						this.chkhospitalization.setValue(false);
					}
					if(null != rodQueryDetails.getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPreHospitalizationFlag()))
					{
						this.chkPreHospitalization.setValue(true);
						if(null != rodQueryDetails.getPreHospitalizationClaimedAmt())
						{
							this.txtPreHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkPreHospitalization.setEnabled(false);
					}
					else
					{
						this.chkPreHospitalization.setValue(false);
					}
					if(null != rodQueryDetails.getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPostHospitalizationFlag()))
					{
						this.chkPostHospitalization.setValue(true);
						if(null != rodQueryDetails.getPostHospitalizationClaimedAmt())
						{
							this.txtPostHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPostHospitalizationClaimedAmt()));
						}
						this.chkPostHospitalization.setEnabled(false);
					}
					else
					{
						this.chkPostHospitalization.setValue(false);
					}
					if(null != rodQueryDetails.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getPartialHospitalizationFlag()))
					{
						this.chkPartialHospitalization.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getHospitalizationClaimedAmt()));
						}
						this.chkPartialHospitalization.setEnabled(false);
					}
					else
					{
						this.chkPartialHospitalization.setValue(false);
					}
					if(null != rodQueryDetails.getAddOnBenefitsLumpsumFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBenefitsLumpsumFlag()))
					{
						this.chkLumpSumAmount.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkLumpSumAmount.setEnabled(false);
					}
					else
					{
						this.chkLumpSumAmount.setValue(false);
					}
					if(null != rodQueryDetails.getAddOnBeneftisHospitalCashFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBeneftisHospitalCashFlag()))
					{
						this.chkAddOnBenefitsHospitalCash.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkAddOnBenefitsHospitalCash.setEnabled(false);
					}
					else
					{
						this.chkAddOnBenefitsHospitalCash.setValue(false);
					}
					if(null != rodQueryDetails.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(rodQueryDetails.getAddOnBenefitsPatientCareFlag()))
					{
						this.chkAddOnBenefitsPatientCare.setValue(true);
						if(null != rodQueryDetails.getHospitalizationClaimedAmt())
						{
							this.txtHospitalizationClaimedAmt.setValue(String.valueOf(rodQueryDetails.getPreHospitalizationClaimedAmt()));
						}
						this.chkAddOnBenefitsPatientCare.setEnabled(false);
					}
					else
					{
						this.chkAddOnBenefitsPatientCare.setValue(false);
					}*/
					
					if(!(null != rodQueryDetails.getHospitalizationRepeatFlag() &&  ("Y").equalsIgnoreCase(rodQueryDetails.getHospitalizationRepeatFlag())))
							{
							//	disableBillClassification();
							}else
					{
						
					}
					
					if(("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue()))
					{
						if(null != chkhospitalization)
						{
							chkhospitalization.setEnabled(true);
						}
						enableOrDisableBillClassification(false);
					}
					else if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
					{
						chkhospitalization.setEnabled(false);
						chkPartialHospitalization.setEnabled(true);
					//	chkHospitalizationRepeat.setEnabled(true);
						//enablePreAndPostHosp();
						
						 
						
					}
					else if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) 
					{
						chkhospitalization.setEnabled(true);
						chkPartialHospitalization.setEnabled(false);
						/*chkHospitalizationRepeat.setEnabled(true);
						if(null != chkHospitalizationRepeat && null != chkHospitalizationRepeat.getValue() && chkHospitalizationRepeat.getValue() )
						{
							if(null != chkPreHospitalization)
							{
								chkPreHospitalization.setEnabled(false);
							}
							if(null != chkPostHospitalization)
							{
								chkPostHospitalization.setEnabled(false);
							}*/
							if(null != chkPartialHospitalization)
							{
								chkPartialHospitalization.setEnabled(false);
							}
							if(null != chkhospitalization)
							{
								chkhospitalization.setEnabled(false);
							}
						}
						else
						{
							enablePreAndPostHosp();
						}
					}	
				else
				{
					this.chkDeath.setValue(false);
					this.chkPermanentPartialDisability.setValue(false);
					this.chkPermanentTotalDisability.setValue(false);
					this.chkTemporaryTotalDisability.setValue(false);
					this.chkhospitalization.setValue(false);
					this.chkPartialHospitalization.setValue(false);
					//this.bean.getDocumentDetails().setAdditionalCovers(null);
					
				}
   
					
				/*	txtHospitalizationClaimedAmt.setEnabled(false);
					txtPreHospitalizationClaimedAmt.setEnabled(false);
					txtPostHospitalizationClaimedAmt.setEnabled(false);*/
					
				}
			
			else if(("No").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
			{
				isQueryReplyReceived = false;
				
				String docRecFromVal = null;				
				
				if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
					docRecFromVal = docRecFrom.getValue();
				}
				if(null != chkhospitalization )//&& chkhospitalization.isEnabled())
				{
					if(null != docRecFromVal && (("Hospital").equalsIgnoreCase(docRecFromVal) || (("Insured").equalsIgnoreCase(docRecFromVal) && ("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))))
					{
						chkhospitalization.setEnabled(true);
						if(null != this.bean.getDocumentDetails().getHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationFlag()))
						//if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
						{
							chkhospitalization.setValue(true);
							txtHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getHospitalizationClaimedAmount());
							//txtHospitalizationClaimedAmt.setValue();
						}
						else
						{
							chkhospitalization.setValue(false);
							//txtHospitalizationClaimedAmt.setValue(null);
						}
					}
					
				}
				
				
				if((null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("preHospitalizationFlag"))))
				{
					/*if(null != chkPreHospitalization)
					{
						chkPreHospitalization.setEnabled(true);
						//if(null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue())
						if(null != this.bean.getDocumentDetails().getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag()))
						{
							chkPreHospitalization.setValue(true);
							txtPreHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount());
						}
						else
						{
							chkPreHospitalization.setValue(false);
						}
					}*/
				}
				
				
				
				//chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("postHospitalizationFlag")))
				{
					/*if(null != chkPostHospitalization)
					{
						chkPostHospitalization.setEnabled(true);
						//if(null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue())
						if(null != this.bean.getDocumentDetails().getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()))
						{
							chkPostHospitalization.setValue(true);
							txtPostHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount());
						}
						else
						{
							chkPostHospitalization.setValue(false);
						}
					}*/
				}
				
				
				if(null != chkPartialHospitalization)//&& chkPartialHospitalization.isEnabled())
				{
					if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && ("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
					{
						chkPartialHospitalization.setEnabled(true);	
					//	if(null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
						if(null != this.bean.getDocumentDetails().getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPartialHospitalizationFlag()))
							//if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							{
								chkPartialHospitalization.setValue(true);
								txtHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getHospitalizationClaimedAmount());
							}
						else
						{
							chkPartialHospitalization.setValue(false);
						}
					}
					//txtHospitalizationClaimedAmt.setValue(null);
				}
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
				{	if(null != chkLumpSumAmount)
					{
						chkLumpSumAmount.setEnabled(true);
					//	if(null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue())
						if(null != this.bean.getDocumentDetails().getLumpSumAmountFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getLumpSumAmountFlag()))
						{
							chkLumpSumAmount.setValue(true);
						}
						else
						{
							chkLumpSumAmount.setValue(false);
						}
					}
				}
				
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
				{
					if(null != chkAddOnBenefitsHospitalCash)
					{
						chkAddOnBenefitsHospitalCash.setEnabled(true);
						//if(null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue())
						if(null != this.bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getAddOnBenefitsHospitalCashFlag()))
						{
							chkAddOnBenefitsHospitalCash.setValue(true);
						}
						else
						{
							chkAddOnBenefitsHospitalCash.setValue(false);
						}
					}
				}
				
				
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
				{
					//chkAddOnBenefitsPatientCare.setEnabled(true);
					if(null != chkAddOnBenefitsPatientCare)
					{
						chkAddOnBenefitsPatientCare.setEnabled(true);
						//if(null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue())
						if(null != this.bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getAddOnBenefitsPatientCareFlag()))
						{
							chkAddOnBenefitsPatientCare.setValue(true);
						}
						else
						{
							chkAddOnBenefitsPatientCare.setValue(false);
						}
					}
				}
				if(null != chkHospitalizationRepeat)
				{
					this.chkHospitalizationRepeat.setEnabled(true);
				}
			}
		//}
 	}
	
	
	private void enablePreAndPostHosp()
	{
		 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
			{
				//chkPreHospitalization.setEnabled(false);
				//chkPreHospitalization.setEnabled(true);
			}
		 else
		 {
			// chkPreHospitalization.setEnabled(false);
		 }
		 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
			{
				//chkPreHospitalization.setEnabled(false);
			 chkPostHospitalization.setEnabled(true);
			}
		 else
		 {
			 chkPostHospitalization.setEnabled(false);
		 }
			 
		 if(null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
			{
				chkAddOnBenefitsHospitalCash.setEnabled(true);
			}
		 else
		 {
			 chkAddOnBenefitsHospitalCash.setEnabled(false);
		 }
		 
		 if(null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
			{
				chkAddOnBenefitsPatientCare.setEnabled(true);
			}
		 else
		 {
			 chkAddOnBenefitsPatientCare.setEnabled(false);
		 }
		 
		 if(null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
			{
			 	chkLumpSumAmount.setEnabled(true);
			}
		 else
		 {
			 chkLumpSumAmount.setEnabled(false);
		 }
	}
	
	public void resetQueryReplyReceived()
	{
		isQueryReplyReceived = false;	
	}
	
	private void disableBillClassification()
	{
		List<Field<?>> chkBoxList = getListOfChkBox();
		if(null != chkBoxList && !chkBoxList.isEmpty())
		{
			for (Field<?> field : chkBoxList) {
				field.setEnabled(false);
			}
		}
		
	}

	public void validateBillClassificationAgainstBillEntry(
			List<Long> categoryList,Long classificationKey,String chkBox) {
		if(null != categoryList && !categoryList.isEmpty())
		{
			Long categoryKey = categoryList.get(0);
			switch(categoryKey.intValue())
			{
			case 8:
				if(categoryKey.equals(ReferenceTable.HOSPITALIZATION))
				{
					showPopup("Hospitalization is selected as classification in Bill Entry popup. Please Delete or Deselect  Hospitalization before making change.",8l,chkBox);
				}
				else
				{
					txtHospitalizationClaimedAmt.setEnabled(false);
					txtHospitalizationClaimedAmt.setValue(null);
				}
				break;
			case 9:
				if(categoryKey.equals(ReferenceTable.PRE_HOSPITALIZATION))
				{
					showPopup("Pre Hospitalization is selected as classification in Bill Entry popup. Please Delete or Deselect Pre Hospitalization before making change.",9l,chkBox);
				}
				else
				{
					 txtPreHospitalizationClaimedAmt.setEnabled(false);
					 txtPreHospitalizationClaimedAmt.setValue(null);
				}
				break;
				
			case 10 :
				
				if(categoryKey.equals(ReferenceTable.POST_HOSPITALIZATION))
				{
					showPopup("Post Hospitalization is selected as classification in Bill Entry popup. Please Delete or Deselect Post Hospitalization before making change.",10l,chkBox);
				}
				else
				{
					 txtPostHospitalizationClaimedAmt.setEnabled(false);
					 txtPostHospitalizationClaimedAmt.setValue(null);
				}
				break;
				
					
			
			}
			
			/*if(categoryKey.equals(ReferenceTable.PRE_HOSPITALIZATION))
			{
				showPopup("Pre Hospitalization is selected as classification in Bill Entry popup. Please choose Pre Hospitalization classifcation before making change.",9l);
			}
			else
			{
				 txtPreHospitalizationClaimedAmt.setEnabled(false);
				 txtPreHospitalizationClaimedAmt.setValue(null);
			}*/
			
		}
		else if(classificationKey.equals(ReferenceTable.HOSPITALIZATION))
		{
			
			txtHospitalizationClaimedAmt.setEnabled(false);
			txtHospitalizationClaimedAmt.setValue(null);
		}
		else if(classificationKey.equals(ReferenceTable.PRE_HOSPITALIZATION))
		{
			 txtPreHospitalizationClaimedAmt.setEnabled(false);
			 txtPreHospitalizationClaimedAmt.setValue(null);
		}
		else if (classificationKey.equals(ReferenceTable.POST_HOSPITALIZATION))
		{
			txtPostHospitalizationClaimedAmt.setEnabled(false);
			txtPostHospitalizationClaimedAmt.setValue(null);
		}
	}
	
	protected void showPopup(String message,Long categoryKey,String chkBox) {
		Label label = new Label(
				message,
				ContentMode.HTML);
		label.setStyleName("errMessage");
		HorizontalLayout layout = new HorizontalLayout(
				label);
		layout.setMargin(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		// dialog.setWidth("40%");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null,
				true);
		if(null != this.chkhospitalization && categoryKey.equals(ReferenceTable.HOSPITALIZATION))
		{
			if(SHAConstants.HOSPITALIZATION_REPEAT.equalsIgnoreCase(chkBox))
			{
				chkHospitalizationRepeat.setValue(true);
				if(null != chkPartialHospitalization)
				{
					chkPartialHospitalization.setValue(null);
					chkPartialHospitalization.setEnabled(false);
				}
				if(null != chkPreHospitalization)
				{
					chkPreHospitalization.setValue(null);
					chkPreHospitalization.setEnabled(false);
					txtPreHospitalizationClaimedAmt.setValue(null);
					txtPreHospitalizationClaimedAmt.setEnabled(false);
				}
				if(null != chkPostHospitalization)
				{
					chkPostHospitalization.setValue(null);
					chkPostHospitalization.setEnabled(false);
					txtPostHospitalizationClaimedAmt.setValue(null);
					txtPostHospitalizationClaimedAmt.setEnabled(false);
				}
				if(null != chkhospitalization)
				{
					chkhospitalization.setValue(null);
					chkhospitalization.setEnabled(false);
				}
				if(null != chkAddOnBenefitsHospitalCash)
				{
					chkAddOnBenefitsHospitalCash.setValue(null);
					chkAddOnBenefitsHospitalCash.setEnabled(false);
				}
				if(null != chkAddOnBenefitsPatientCare)
				{
					chkAddOnBenefitsPatientCare.setValue(null);
					chkAddOnBenefitsHospitalCash.setEnabled(false);;
				}
			}
			else if (SHAConstants.HOSPITALIZATION.equalsIgnoreCase(chkBox))
			{
				chkhospitalization.setValue(true);
			}
			else if(SHAConstants.PARTIAL_HOSPITALIZATION.equalsIgnoreCase(chkBox))
			{
				chkPartialHospitalization.setValue(true);
			}
			
		}
		if(null != this.chkPreHospitalization && categoryKey.equals(ReferenceTable.PRE_HOSPITALIZATION))
		{
			//isPreHospAlreadySelected = true;
		//	chkPreHospitalization.removeListener(listener);
			chkPreHospitalization.setValue(true);
			
		}
		if(null != this.chkPostHospitalization && categoryKey.equals(ReferenceTable.POST_HOSPITALIZATION))
		{
			chkPostHospitalization.setValue(true);
		}
	}
	
	
	
	private Boolean validateDuplicationHospPartialHospClassification()
	{
			Boolean isError = false;
			isQueryReplyReceived = false;
			if(!isQueryReplyReceived)
				{
					if(null != docDTO && !docDTO.isEmpty())
					{
						for (DocumentDetailsDTO documentDetailsDTO : docDTO) {
						
							if(null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) 
							{
								if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && (!(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId())
										|| !ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS.equals(documentDetailsDTO.getStatusId())))
								//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
								{
									isError = true;
								}
							}
							if(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) 
							{
								if(("Y").equalsIgnoreCase(documentDetailsDTO.getPartialHospitalizationFlag()) &&  !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
								//if(documentDetailsDTO.getPartialHospitalizationFlag().equalsIgnoreCase("Y"))
								{
									isError = true;
								}
							}
							
							/**
							 * Below validation is added for cancel rod scenario. If an hospitalization rod is cancelled and user tries to deselect hospitalization
							 * and select hospitalization repeat, then below validation will not allow user to create an hospitalization repeat rod, since hospitalization
							 * rod is not yet created. -- Added for #3768 
							 */
								if(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && null != this.chkHospitalizationRepeat.getValue())
								{
									if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && (ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
										//if(documentDetailsDTO.getHospitalizationFlag().equalsIgnoreCase("Y"))
										{
											isError = true;
										}
								}
							
						}
					}
				}
			return isError ;
	}
	
	public Boolean validateBenifits() 
	{
		Boolean isError = false;
		
		if(((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true))) && 
				((null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true)) ||
				(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true)) || 
				(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue()  && chkTemporaryTotalDisability.getValue().equals(true)) ||				
				(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
				(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
			{
				isError = true;
			}
			if(((null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true))) && 
					((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
					(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue()&& chkPermanentTotalDisability.getValue().equals(true)) || 
					(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue() && chkTemporaryTotalDisability.getValue().equals(true)) ||			
					(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
					(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
				{
					isError = true;
				}
			if(((null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true))) && 
					((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
					(null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue()  && chkPermanentPartialDisability.getValue().equals(true)) || 
					(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue()  && chkTemporaryTotalDisability.getValue().equals(true)) ||					
					(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
					(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
				{
					isError = true;
				}
			if(((null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue() && chkTemporaryTotalDisability.getValue().equals(true))) && 
					((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
					(null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true)) || 
					(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true)) ||				
					(null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) ||
					(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
				{
					isError = true;
				}
		
			
			if(((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true))) && 
					((null != chkDeath && null != chkDeath.getValue() && chkDeath.getValue().equals(true)) ||
					(null != chkPermanentTotalDisability && null != chkPermanentTotalDisability.getValue() && chkPermanentTotalDisability.getValue().equals(true)) || 
					(null != chkTemporaryTotalDisability && null != chkTemporaryTotalDisability.getValue() && chkTemporaryTotalDisability.getValue().equals(true)) ||
					(null != chkPermanentPartialDisability && null != chkPermanentPartialDisability.getValue() && chkPermanentPartialDisability.getValue().equals(true)) ||					
					(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true))))
				{
					isError = true;
				}
			
			return isError;
	}
	
	private void addBenefitLister()

	{	
		chkDeath
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					// chkTemporaryTotalDisability.setValue(value);
				
				
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkDeath.setValue(false);
					 }
					
					fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT_BILL_ENTRY, bean.getClaimDTO().getKey(),ReferenceTable.DEATH_BENEFIT_MASTER_VALUE,value,
							chkDeath,bean.getDocumentDetails().getRodKey(),SHAConstants.DEATH_FLAGS);
					
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				
				 
				}
						
				}
				
		});
		
		
		chkPermanentPartialDisability
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {			
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				 					 
					// chkTemporaryTotalDisability.setValue(value);
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPermanentPartialDisability.setValue(false);
					 }
					
					fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT_BILL_ENTRY, bean.getClaimDTO().getKey(),ReferenceTable.PPD_BENEFIT_MASTER_VALUE,value,chkPermanentPartialDisability,
							bean.getDocumentDetails().getRodKey(),SHAConstants.PPD);
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
				}
						
				}
				
		});
		
		
		chkHospitalExpensesCover
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {				
			
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					// chkTemporaryTotalDisability.setValue(value);
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkHospitalExpensesCover.setValue(false);
					 }
					
				
				 
				}
						
				}
				
		});
		
		
		chkPermanentTotalDisability
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					 
					// chkTemporaryTotalDisability.setValue(value);
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPermanentTotalDisability.setValue(false);
					 }
					
					fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT_BILL_ENTRY, bean.getClaimDTO().getKey(),ReferenceTable.PTD_BENEFIT_MASTER_VALUE,value,chkPermanentTotalDisability,
							bean.getDocumentDetails().getRodKey(),SHAConstants.PTD);
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
				}
						
				}
				
		});
		
		
		chkTemporaryTotalDisability
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				
					// chkTemporaryTotalDisability.setValue(value);
					 
					if(validateBenifits())
					 {
						 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							 
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkTemporaryTotalDisability.setValue(false);
					 }
					fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT_BILL_ENTRY, bean.getClaimDTO().getKey(),ReferenceTable.TTD_BENEFIT_MASTER_VALUE,value,chkTemporaryTotalDisability,
							bean.getDocumentDetails().getRodKey(),SHAConstants.TTD);
					if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
				
				 
				}
						
				}
				
		});
		
		
		chkhospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 Boolean value = (Boolean) event.getProperty().getValue();
				 String flag = null;
				 if(null != value && value )
				 {
					 flag = "Y";
				 }
				 else
				 {
					 flag = "N";
				 }
				 if(null != value && !(flag.equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationFlag())))
				 {
				 if(value)
				 {
					 addOnCoversTable.setEnabled(false);
					 optionalCoversTable.setEnabled(false);
					 
					  
					 if(validateDuplicationHospPartialHospClassification())
					 {
						 Label label = new Label("Already hospitalization is existing for this claim.", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							//chkhospitalization.setValue(false);
							chkhospitalization.setValue(null);
							if(null != txtHospitalizationClaimedAmt)
							{
								txtHospitalizationClaimedAmt.setValue("");
							
							}
					 }		
					/* fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT_BILL_ENTRY, bean.getClaimDTO().getKey(),ReferenceTable.HOSP_BENEFIT_MASTER_VALUE,value,chkhospitalization,
								bean.getDocumentDetails().getRodKey(),SHAConstants.HOSP);*/
				
				 }
				 else
				 {
					 
					 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())						
						{
						SelectValue selValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
						if(null != selValue && ((ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL).equals(selValue.getId())))
						{
							addOnCoversTable.setEnabled(false);
							optionalCoversTable.setEnabled(false);
						}
						else
							
						{
					 addOnCoversTable.setEnabled(true);
					 optionalCoversTable.setEnabled(true);
						}
						}
					 /*chkPostHospitalization.setEnabled(false);
					 chkPreHospitalization.setEnabled(false);
					 
					 txtHospitalizationClaimedAmt.setEnabled(false);
					 txtHospitalizationClaimedAmt.setValue(null);*/
				 }				
				
				
				 if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())						
							{
							SelectValue selValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
							if(null != selValue && ((ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL).equals(selValue.getId())))
							{
								addOnCoversTable.setEnabled(false);
								optionalCoversTable.setEnabled(false);
							}
							else
							{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
							}
							}
					}
				
				 //validateBillClassificationDetails();
			}
				 
				 if(null != value && value)
				 {
					 if((null != addOnCoversTable.getValues() && !addOnCoversTable.getValues().isEmpty())|| null != optionalCoversTable.getValues() && !optionalCoversTable.getValues().isEmpty())
					 {
						 Label label = new Label("Please Delete the selected Add On and Optional covers before selecting hospitalization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkhospitalization.setValue(false);
					 }
				 }
				 
				 if(validateBenifits())
				 {
					 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
						label.setStyleName("errMessage");
					 HorizontalLayout layout = new HorizontalLayout(
							 label);
						layout.setMargin(true);
						
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("Errors");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
						chkhospitalization.setValue(false);
				 }
				
				 if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
						addOnCoversTable.setEnabled(false);
						optionalCoversTable.setEnabled(false);
					}
					else
					{
						 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())						
							{
							SelectValue selValue = (SelectValue) cmbDocumentsReceivedFrom.getValue();
							if(null != selValue && ((ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL).equals(selValue.getId())))
							{
								addOnCoversTable.setEnabled(false);
								optionalCoversTable.setEnabled(false);
							}
							else
							{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
							}
							}
					}
				
			}
		});
		
		chkPartialHospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 Boolean value = (Boolean) event.getProperty().getValue();
				 String flag = null;
				 
				 if(null != value && value )
				 {
					 
					 addOnCoversTable.setEnabled(false);
					 optionalCoversTable.setEnabled(false);					 
					 
					 
					 if((null != addOnCoversTable.getValues() && !addOnCoversTable.getValues().isEmpty())|| null != optionalCoversTable.getValues() && !optionalCoversTable.getValues().isEmpty())
					 {
						 Label label = new Label("Please Delete the selected Add On and Optional covers before selecting hospitalization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
								 label);
							layout.setMargin(true);
							
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPartialHospitalization.setValue(false);
					 } 
					 
					 else
					 {
				 
					 fireViewEvent(PABillEntryDocumentDetailsPresenter.VALIDATE_BENEFIT_REPEAT_BILL_ENTRY, bean.getClaimDTO().getKey(),ReferenceTable.PART_BENEFIT_MASTER_VALUE,value,chkPartialHospitalization,
								bean.getDocumentDetails().getRodKey(),SHAConstants.PART);
					 }
					/* if(validateDuplicationHospPartialHospClassification())
					 {
						 Label label = new Label("Already partial hospitalization is existing for this claim.", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
									label);

							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
						//	chkPartialHospitalization.setValue(false);
							chkPartialHospitalization.setValue(null);
					 }			 */			
				 
				 if(validateBenifits())
				 {
					 Label label = new Label("Can not select more than one Benefit", ContentMode.HTML);
						label.setStyleName("errMessage");
					 HorizontalLayout layout = new HorizontalLayout(
							 label);
						layout.setMargin(true);
						
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("Errors");
						//dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);
						chkPartialHospitalization.setValue(false);
				 }
				 
				 if((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue().equals(true)) || 
							(null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue().equals(true)))
					{
					 if(null != addOnCoversTable.getValues() && !addOnCoversTable.getValues().isEmpty())
					 {
						addOnCoversTable.setEnabled(true);
					 }
					 else
					 {
						 addOnCoversTable.setEnabled(false);
					 }
					  if(null != optionalCoversTable.getValues()  && !optionalCoversTable.getValues().isEmpty() )
					 {
						optionalCoversTable.setEnabled(true);
					 }
					  else
					  {
						  optionalCoversTable.setEnabled(false);
					 }
					}
					else
					{
						addOnCoversTable.setEnabled(true);
						optionalCoversTable.setEnabled(true);
					}
				 
			
				 //validateBillClassificationDetails();
			}
				 else
				 {
					 addOnCoversTable.setEnabled(true);
					 optionalCoversTable.setEnabled(true);
					/* chkPostHospitalization.setEnabled(false);
					 chkPreHospitalization.setEnabled(false);
					 txtHospitalizationClaimedAmt.setEnabled(false);*/

				 }
			}
		});
		
		
		
	}
	
	public void validateCoversRepeat(Boolean isValid,String coverName) {
		
		this.isValid = isValid;
		this.coverName = coverName;
		if(isValid)
		{
		 /*Label label = new Label("The Cover  "+coverName+"  has been selected in another ROD for this claim. Please select another Additional Cover", ContentMode.HTML);
			label.setStyleName("errMessage");
		 HorizontalLayout layout = new HorizontalLayout(
				 label);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			//dialog.setWidth("35%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			//chkHospitalizationRepeat.setValue(null);
			*/
		}
		
		
	}

	
public void validateBenefitRepeat(Boolean isValid,Boolean ChkBoxValue) {
		
		if(isValid && ChkBoxValue)
		{
		 Label label = new Label("Benefit has been selected in another ROD for this claim. Please select another Benefit", ContentMode.HTML);
			label.setStyleName("errMessage");
		 HorizontalLayout layout = new HorizontalLayout(
				 label);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			//dialog.setWidth("35%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			//chkHospitalizationRepeat.setValue(null);
			chkDeath.setValue(false);
			chkPermanentPartialDisability.setValue(false);
			chkPermanentTotalDisability.setValue(false);
			chkTemporaryTotalDisability.setValue(false);
			chkHospitalExpensesCover.setValue(false);
		}
		else
		{
			 txtHospitalizationClaimedAmt.setEnabled(true);		

		}
}

public void resetReconsiderationValue()
{
	if(null != cmbDocumentType && null != cmbDocumentType.getValue() )//&& (ReferenceTable.PA_RECONSIDERATION_DOCUMENT).equals(cmbDocumentType.getId()))
	{
		if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
		{
		SelectValue selValue1 = (SelectValue) cmbDocumentType.getValue();
		if(null != selValue1 && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(selValue1.getId())))	
		{
			txtBenifitClaimedAmnt.setValue(null);
			/*List<ReconsiderRODRequestTableDTO> reconsiderList = new ArrayList<ReconsiderRODRequestTableDTO>();
			if(null != reconsiderRequestDetails)
			{
				reconsiderList = reconsiderRequestDetails.getValues();
			}
			
			if(null != reconsiderList && !reconsiderList.isEmpty())
			{
				reconsiderRODRequestList = new ArrayList<ReconsiderRODRequestTableDTO>();
				for (ReconsiderRODRequestTableDTO reconsiderDto : reconsiderList) {
					
					if(null != reconsiderDto.getSelect() && reconsiderDto.getSelect())
					{
						reconsiderDto.setSelect(false);
						txtBenifitClaimedAmnt.setValue(null);
					}
					reconsiderRODRequestList.add(reconsiderDto);
					
				}
				this.bean.setReconsiderRodRequestList(reconsiderRODRequestList);
			}*/
		}
		}
	}
	
}


public void validateBenefitRepeat(Boolean isValid,Boolean ChkBoxValue,CheckBox chkBox,String benefitValue) {
	
	
	if(cmbDocumentType != null && cmbDocumentType.getValue() != null){
		SelectValue value = (SelectValue)cmbDocumentType.getValue();
		if(null != value && ((ReferenceTable.PA_FRESH_DOCUMENT_ID).equals(value.getId()))){
			if(isValid && ChkBoxValue)
			{
			 Label label = new Label("Benefit has been selected in another ROD for this claim. Please select another Benefit", ContentMode.HTML);
				label.setStyleName("errMessage");
			 HorizontalLayout layout = new HorizontalLayout(
					 label);
				layout.setMargin(true);
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				//dialog.setWidth("35%");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
				chkBox.setValue(null);
				//chkHospitalizationRepeat.setValue(null);
			/*	chkDeath.setValue(false);
				chkPermanentPartialDisability.setValue(false);
				chkPermanentTotalDisability.setValue(false);
				chkTemporaryTotalDisability.setValue(false);
				chkHospitalExpensesCover.setValue(false);
				chkhospitalization.setValue(false);
				chkPartialHospitalization.setValue(false);*/
			}
			else
			{
				 if(ChkBoxValue){
					// fireViewEvent(PABillEntryDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,benefitValue);
				 }
			}
		}
	}
	
}


public void setParticularsByBenefitValue(
		SelectValue value) {
	
	if(value != null && value.getId() != null &&( value.getId().equals(ReferenceTable.OUTPATIENT_EXPENSES) || 
			value.getId().equals(ReferenceTable.EARNING_PARENT_SI))){
		Boolean uncheckBenefitsValue = uncheckBenefitsValue();
		
		if(chkDeath != null){
			chkDeath.setValue(false);
		}
		if(chkTemporaryTotalDisability != null){
			chkTemporaryTotalDisability.setValue(false);
		}
		
		if(chkPermanentTotalDisability != null){
			chkPermanentTotalDisability.setValue(false);
		}
		
		if(chkPermanentPartialDisability != null){
			chkPermanentPartialDisability.setValue(false);
		}
		
		if(chkhospitalization != null){
			chkhospitalization.setValue(false);
		}
		
		if(chkPartialHospitalization != null){
			chkPartialHospitalization.setValue(false);
		}
		if(! uncheckBenefitsValue){
		
		showErrorMessage("Can not select any benefit along with Outpatient Expences");
		}
	}

	
	/*List<SelectValue> itemIds = particulars.getItemIds();
	
	
	
	
	if(documentCheckListValidation != null){
		
		List<DocumentCheckListDTO> documentCheckListValues = new ArrayList<DocumentCheckListDTO>();
		
		List<DocumentCheckListDTO> tableValue = documentCheckListValidation.getValues();
		
		documentCheckListValues.addAll(tableValue);

		documentCheckListValidation.setPaBillEntryDocumentDetailsPage(this);
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		String lob = (String) wrkFlowMap.get(SHAConstants.LOB);		 
		documentCheckListValidation.initPresenter(SHAConstants.PA_BILL_ENTRY,SHAConstants.PA_LOB);
		
		this.referenceData.put("particulars",particulars);
		
		documentCheckListValidation.setReferenceData(referenceData);
		documentCheckListValidation.init();
		
		int i = 0;
		
		for (SelectValue selectValue : itemIds) {
			if(documentCheckListValues.size() > i){
				DocumentCheckListDTO documentCheckListDTO = documentCheckListValues.get(i);
				documentCheckListDTO.setParticulars(selectValue);
				documentCheckListValidation.addBeanToList(documentCheckListDTO);
			}else{
				DocumentCheckListDTO documentDTO = new DocumentCheckListDTO();
				documentDTO.setParticulars(selectValue);					
				SelectValue setReceivedStatus = new SelectValue();
				
				setReceivedStatus.setId(ReferenceTable.ACK_DOC_NOT_APPLICABLE);
				setReceivedStatus.setValue("Not Applicable");
				documentDTO.setReceivedStatus(setReceivedStatus);
				documentCheckListValidation.addBeanToList(documentDTO);
			}
			
			i++;
			
		}
		
		
		
	
	}
*/
	
}

private void showErrorMessage(String eMsg) {
	Label label = new Label(eMsg, ContentMode.HTML);
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
	dialog.show(getUI().getCurrent(), null, true);
}


public Boolean validationforOutPatientExpenses(){
	
	Boolean isOutPatient = true;
	
	List<AddOnCoversTableDTO> values = addOnCoversTable.getValues();
	for (AddOnCoversTableDTO addOnCoversTableDTO : values) {
		
		if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getId() != null && 
				addOnCoversTableDTO.getCovers().getId().equals(ReferenceTable.OUTPATIENT_EXPENSES)){
			isOutPatient = uncheckBenefitsValue();
			
			break;
		}
		
	}
	
	return isOutPatient;
}

public Boolean validationforOutEarningParentSI(){
	
	Boolean isEarningParentSI = true;
	
	List<AddOnCoversTableDTO> values = addOnCoversTable.getValues();
	for (AddOnCoversTableDTO addOnCoversTableDTO : values) {
		
		if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getId() != null && 
				addOnCoversTableDTO.getCovers().getId().equals(ReferenceTable.EARNING_PARENT_SI)){
			isEarningParentSI = uncheckBenefitsValue();
			break;
		}
		
	}
	
	return isEarningParentSI;
}

	public Boolean uncheckBenefitsValue(){
	
				
		if(chkDeath != null && chkDeath.getValue() != null && chkDeath.getValue()){
			return false;
		}
		if(chkPermanentPartialDisability != null && chkPermanentPartialDisability.getValue() != null && chkPermanentPartialDisability.getValue()){
			return false;
		}
		
		if(chkPermanentTotalDisability != null && chkPermanentTotalDisability.getValue() != null && chkPermanentTotalDisability.getValue()){
			return false;
		}
		
		if(chkTemporaryTotalDisability != null && chkTemporaryTotalDisability.getValue() != null && chkTemporaryTotalDisability.getValue()){
			return false;
		}
		
		if( chkhospitalization != null && chkhospitalization.getValue() != null && chkhospitalization.getValue()){
			return false;
		}
		
		if(chkPartialHospitalization != null && chkPartialHospitalization.getValue() != null && chkPartialHospitalization.getValue()){
			return false;
		}
		
		return true;
	
	
}
	
	public void resetBenefitsValue(){
		if(chkDeath != null){
			chkDeath.setValue(false);
		}
		if(chkTemporaryTotalDisability != null){
			chkTemporaryTotalDisability.setValue(false);
		}
		
		if(chkPermanentTotalDisability != null){
			chkPermanentTotalDisability.setValue(false);
		}
		
		if(chkPermanentPartialDisability != null){
			chkPermanentPartialDisability.setValue(false);
		}
		
		if(chkhospitalization != null){
			chkhospitalization.setValue(false);
		}
		
		if(chkPartialHospitalization != null){
			chkPartialHospitalization.setValue(false);
		}
	}
	
	/*public void buildNomineeLayout() {
		
		nomineeDetailsTable = nomineeDetailsTableInstance.get();
		
		nomineeDetailsTable.init("", false, false);
		
		if(bean.getClaimDTO().getNewIntimationDto().getNomineeList() != null) { 
			nomineeDetailsTable.setTableList(bean.getClaimDTO().getNewIntimationDto().getNomineeList());
			nomineeDetailsTable.generateSelectColumn();
		}	
		
		documentDetailsPageLayout.addComponent(nomineeDetailsTable);
	
		boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
				
		legaHeirLayout = nomineeDetailsTable.getLegalHeirLayout(enableLegalHeir);
	
		if(enableLegalHeir) {
			nomineeDetailsTable.setLegalHeirDetails(
			bean.getClaimDTO().getNewIntimationDto().getNomineeName(),
			bean.getClaimDTO().getNewIntimationDto().getNomineeAddr());
		}	
		
		documentDetailsPageLayout.addComponent(legaHeirLayout);	
	
	}*/

}
