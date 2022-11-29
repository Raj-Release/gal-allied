package com.shaic.claim.rod.wizard.forms;

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

import org.apache.commons.lang.time.DateUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.tables.DocumentCheckListValidationListenerTable;
import com.shaic.claim.rod.wizard.tables.RODQueryDetailsTable;
import com.shaic.claim.rod.wizard.tables.ReconsiderRODRequestListenerTable;
import com.shaic.claim.rod.wizard.tables.SectionDetailsListenerTable;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class BillEntryDocumentDetailsPage  extends ViewComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<DocumentDetailsDTO> binder;

	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	////private static Window popup;
	
	@Inject
	private RODQueryDetailsTable rodQueryDetails;
	
/*	@Inject
	private BillEntryRODReconsiderRequestTable reconsiderRequestDetails;
*/
	@Inject
	private ReconsiderRODRequestListenerTable reconsiderRequestDetails;
	
	/*@Inject
	private BillEntryCheckListValidationTable documentCheckListValidation;*/
	
	@Inject
	private Instance<DocumentCheckListValidationListenerTable> documentCheckListValidationObj;
	
	private DocumentCheckListValidationListenerTable documentCheckListValidation;
	
	@Inject
	private Instance<SectionDetailsListenerTable> sectionDetailsListenerTable;
	
	private SectionDetailsListenerTable sectionDetailsListenerTableObj;
	
	/*@Inject
	private BillEntryDocumentCheckListValidationListenerTable documentCheckListValidation;*/
	
	private ComboBox cmbDocumentsReceivedFrom;
	
	private DateField documentsReceivedDate;
	
	private ComboBox cmbModeOfReceipt;
	
	private ComboBox cmbReconsiderationRequest;
	
	private TextField txtAdditionalRemarks;
	
	//private DateField dateOfAdmission;
	
	//private TextField txtHospitalName;
	
	//private ComboBox cmbInsuredPatientName;
	
	private CheckBox chkhospitalization;
	
	private CheckBox chkPreHospitalization;
	
	private CheckBox chkPostHospitalization;
	
	private CheckBox chkPartialHospitalization;
	
	private CheckBox chkHospitalizationRepeat;
	
	private CheckBox chkLumpSumAmount;
	
	private CheckBox chkAddOnBenefitsHospitalCash;
	
	private CheckBox chkAddOnBenefitsPatientCare;
	
	private CheckBox chkOtherBenefits;
	
	private CheckBox chkEmergencyMedicalEvaluation;
	
	private CheckBox chkCompassionateTravel;
	
	private CheckBox chkRepatriationOfMortalRemains;
	
	private CheckBox chkPreferredNetworkHospital;
	
	private CheckBox chkSharedAccomodation;
	
	private VerticalLayout documentDetailsPageLayout;
	
	private OptionGroup optPaymentMode;
	
	//COMMENTED ON 21-MAY-2019 AS IT IS NOWHERE USED.(KOTAK SLOWNESS)
	//private ComboBox cmbPayeeName;
	
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
	
//	private TextField txtReasonForChangeInDOA;
	
	//private FormLayout formLayout1 = null;
	
	
	

//	private GWizard wizard;
	
	private VerticalLayout reconsiderationLayout;
	
	private VerticalLayout otherBenefitsLayout;
	
//	private VerticalLayout paymentDetailsLayout;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();

	private BeanItemContainer<SelectValue> reconsiderationRequest;
	 
	private BeanItemContainer<SelectValue> modeOfReceipt;
	 
	private BeanItemContainer<SelectValue> docReceivedFromRequest ;
	
	//private BeanItemContainer<SelectValue> payeeNameList;
	
	//private BeanItemContainer<SelectValue> insuredPatientList;
	
	//private HorizontalLayout insuredLayout;
	
	private List<DocumentDetailsDTO> docsDetailsList ;
	
	private TextField txtHospitalizationClaimedAmt;
	private TextField txtPreHospitalizationClaimedAmt;
	private TextField txtPostHospitalizationClaimedAmt;
	private TextField txtOtherBenefitsClaimedAmnt;
	
	//private  DocumentDetailsDTO docDTO ;
	private  List<DocumentDetailsDTO> docDTO ;

	 @Inject
	 private ViewDetails viewDetails;

	 @EJB
	 private MasterService masterService;
	 
	 private List<ReconsiderRODRequestTableDTO> reconsiderRODRequestList;
	 
	 public Map<String,Boolean> reconsiderationMap = new HashMap<String, Boolean>();
	 
	 public String hospitalizationClaimedAmt = "";
		
	 public String preHospitalizationAmt = "";
		
	 public String postHospitalizationAmt= "";
	 
	 public String otherBenefitAmnt = "";
	 
	 //added for new product076
	 public String HospitalCashClaimedAmnt = "";
	 
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
	 
	private Boolean lumpSumValidationFlag = false;
	
	
	
	//added for new product
    private CheckBox chkHospitalCash;
	private TextField txtHospitalCashClaimedAmnt;
	private ComboBox cmbDiagnosisHospitalCash;
	private ComboBox cmbHospitalCashDueTo;
	
	private DateField dateOfAdmission;
	private DateField dateOfDischarge;
	
	private BeanItemContainer<SelectValue> diagnosisHospitalCashContainer;
	
	private BeanItemContainer<SelectValue> hospitalCashDueTo;
/*	@Inject
	private Instance<NomineeDetailsTable> nomineeDetailsTableInstance;
	
	private NomineeDetailsTable nomineeDetailsTable;
	
	private FormLayout legaHeirLayout;
	
		
	private FormLayout legaHeirLayout;*/

	//private Panel billClassificationPanel;
	
	@PostConstruct
	public void init() {

	}
	
	public void init(ReceiptOfDocumentsDTO bean, GWizard wizard) {
		this.bean = bean;
//		this.wizard = wizard;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<DocumentDetailsDTO>(
				DocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean
				.getDocumentDetails());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	public Component getContent() {
		
		if(SHAConstants.YES_FLAG.equals(bean.getClaimDTO().getNewIntimationDto().getInsuredDeceasedFlag())) {
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		//CR2019217
		//changes done for SM agent percentage by noufel on 13-01-2020
				String agentpercentage = masterService.getAgentPercentage(ReferenceTable.ICR_AGENT);
				String smpercentage = masterService.getAgentPercentage(ReferenceTable.SM_AGENT);
				if((bean.getPreauthDTO().getIcrAgentValue() != null && !bean.getPreauthDTO().getIcrAgentValue().isEmpty() 
						&& (Integer.parseInt(bean.getPreauthDTO().getIcrAgentValue()) >= Integer.parseInt(agentpercentage))) 
						|| bean.getPreauthDTO().getSmAgentValue() != null && !bean.getPreauthDTO().getSmAgentValue().isEmpty() 
								&& (Integer.parseInt(bean.getPreauthDTO().getSmAgentValue()) >= Integer.parseInt(smpercentage))){
					SHAUtils.showICRAgentAlert(bean.getPreauthDTO().getIcrAgentValue(), agentpercentage, bean.getPreauthDTO().getSmAgentValue(), smpercentage);
				}
		DBCalculationService dbService = new DBCalculationService();
		String memberType = dbService.getCMDMemberType(this.bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getKey());
		if(null != memberType && !memberType.isEmpty()){
			showCMDAlert(memberType);
		}
		else if(null != this.bean.getPreauthDTO().getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(this.bean.getPreauthDTO().getTopUpPolicyAlertFlag())){
			showTopUpAlertMessage(this.bean.getPreauthDTO().getTopUpPolicyAlertMessage());
		}
		else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
			getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
		}
		else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
			showICRMessage();
		}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
			policyValidationPopupMessage();
		}		
		/*else if(bean.getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
		}*/
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
//		if(this.bean.getClaimCount() >1){
//			alertMessageForClaimCount(this.bean.getClaimCount());
//		}
		otherBenefitsLayout = new VerticalLayout();	
		
		documentDetailsPageLayout = new VerticalLayout();
		documentDetailsPageLayout.setSpacing(false);
		documentDetailsPageLayout.setMargin(false);
		
		reconsiderationLayout = new VerticalLayout();
		reconsiderationLayout.setSpacing(false);
			
		
		VerticalLayout billEntryLayout = buildBillingDetailsLayout();
		
		HorizontalLayout documentDetailsLayout = buildDocumentDetailsLayout(); 
		documentDetailsLayout.setCaption("Document Details");
//		documentDetailsLayout.setWidth("90%");
		documentDetailsLayout.setMargin(true);
		//documentDetailsLayout.setHeight("50px");
		
	/*	documentCheckListValidation.init("Checklist Validation", false);
		documentCheckListValidation.setReference(this.referenceData);*/
		
		documentCheckListValidation = documentCheckListValidationObj.get();
		documentCheckListValidation.initPresenter(SHAConstants.BILL_ENTRY);
		documentCheckListValidation.init();
		
		
		/*documentCheckListValidation.init();
		documentCheckListValidation.setCaption("Checklist Validation");
*/		
		//HorizontalLayout insuredLayout  = new HorizontalLayout(new FormLayout(dateOfAdmission),new FormLayout(), new FormLayout(),new FormLayout(txtHospitalName, cmbInsuredPatientName));
		/*insuredLayout = new HorizontalLayout(new FormLayout(dateOfAdmission),new FormLayout(txtHospitalName, cmbInsuredPatientName));
		insuredLayout.setWidth("100%");
		insuredLayout.setMargin(true);
		insuredLayout.setSpacing(true);*/
	/*	VerticalLayout docCheckListValidationLayout = new VerticalLayout(documentCheckListValidation);
		docCheckListValidationLayout.setCaption("Checklist Validation");
		docCheckListValidationLayout.setMargin(true);*/
		//docCheckListValidationLayout.setMargin(true);
		
		/*paymentDetailsLayout = new VerticalLayout();
		paymentDetailsLayout.setCaption("Payment Details");
		paymentDetailsLayout.setSpacing(true);
		paymentDetailsLayout.setMargin(true);*/
		
		//getPaymentDetailsLayout();
		
		//documentDetailsPageLayout = new VerticalLayout(documentDetailsLayout,reconsiderationLayout,insuredLayout,documentCheckListValidation);
		
		referenceData.put("sectionDetails", this.bean.getSectionList());
		PreauthDTO preauthDto = new PreauthDTO();
		if(cmbReconsiderationRequest != null) {
			if(this.bean.getDocumentDetails().getReconsiderationRequestValue().equalsIgnoreCase("yes") && !cmbReconsiderationRequest.isEnabled()) {
				preauthDto.setIsReconsiderationRequest(true);
			}
		}
		preauthDto.setNewIntimationDTO(this.bean.getNewIntimationDTO());
		preauthDto.setClaimDTO(this.bean.getClaimDTO());
		preauthDto.setShouldDisableSection(this.bean.getShouldDisableSection());
		if(preauthDto.getShouldDisableSection() && bean.getDocumentDetails().getReconsiderationRequestValue() != null && bean.getDocumentDetails().getReconsiderationRequestValue().equalsIgnoreCase("Yes")) {
			preauthDto.setShouldDisableSection(true);
		}
		this.sectionDetailsListenerTableObj = sectionDetailsListenerTable.get();
		this.sectionDetailsListenerTableObj.init(preauthDto, SHAConstants.BILL_ENTRY);
		sectionDetailsListenerTableObj.setReferenceData(referenceData);
		SectionDetailsTableDTO sectionDTO = new SectionDetailsTableDTO();
		SelectValue correctSelectValue = StarCommonUtils.getCorrectSelectValue(this.bean.getSectionList().getItemIds(), this.bean.getClaimDTO().getClaimSectionCode() != null ? this.bean.getClaimDTO().getClaimSectionCode() : ReferenceTable.HOSPITALIZATION_SECTION_CODE);
		sectionDTO.setSection(correctSelectValue);
		bean.getDocumentDetails().setSectionDetailsDTO(sectionDTO);
		this.sectionDetailsListenerTableObj.addBeanToList(bean.getDocumentDetails().getSectionDetailsDTO());
		
		
		documentDetailsPageLayout = new VerticalLayout(billEntryLayout,documentDetailsLayout, this.sectionDetailsListenerTableObj, reconsiderationLayout,documentCheckListValidation);
		
		
		addListener();
		setTableValues();
		if(("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkLumpSumAmount)
		{
			chkLumpSumAmount.setEnabled(false);
		}
		return documentDetailsPageLayout;
	}
	
	/*private void getDocumentTableDataList()
	{
		
		fireViewEvent(BillEntryDocumentDetailsPresenter.BILL_ENTRY_SETUP_DOCUMENT_CHECKLIST_TABLE_VALUES, null);
	}*/
	
	/*private void alertMessageForClaimCount(Long claimCount){
		
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
		
		
		popup = new com.vaadin.ui.Window();
		popup.setWidth("30%");
		popup.setHeight("20%");
//		popup.setContent( viewDocumentDetailsPage);
		popup.setContent(panel);
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
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}*/
	
	
	
	public VerticalLayout buildBillingDetailsLayout()
	{
		
		String referedFrom = SHAUtils.getReferedFrom(this.bean.getStatusKey());
		Label referedLable = null;
		if(referedFrom != null && !referedFrom.isEmpty()){
			referedFrom = "<B>" + referedFrom + "</B>";
			referedLable = new Label(referedFrom, ContentMode.HTML);
		}
		txtBillingRemarks = binder.buildAndBind("Reason for Referring To <br>Bill Entry</br>","referToBillEntryBillingRemarks",TextArea.class);
		txtBillingRemarks.setCaptionAsHtml(true);
		txtBillingRemarks.setReadOnly(true);
		txtBillingRemarks.setWidth("450px");
		txtBillingRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		txtBillingRemarks.setData(this.bean.getDocumentDetails().getReferToBillEntryBillingRemarks() != null ? this.bean.getDocumentDetails().getReferToBillEntryBillingRemarks() : "");
		handleRemarksPopup(txtBillingRemarks, null);
		
		txtFARemarks = binder.buildAndBind("Financial Approver Remarks","referToBillEntryFAApproverRemarks",TextArea.class);
		txtFARemarks.setReadOnly(true);
		
		
		FormLayout billingLayout = new FormLayout(txtBillingRemarks);
		VerticalLayout bLayout = new VerticalLayout();
		if(referedLable != null){
			bLayout.addComponents(referedLable,billingLayout);
		}
		else{
			bLayout.addComponents(billingLayout);
		}
		bLayout.setWidth("100%");
		bLayout.setSpacing(false);
		//bLayout.setCaption("Billing Details");
		bLayout.setCaption("<B>Refer To Bill Entry Details</B>");
		bLayout.setCaptionAsHtml(true);
		
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
		
		
//		return referToBillEntryLayout;
		return bLayout;
	}
	
	private HorizontalLayout buildDocumentDetailsLayout()
	{
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
		
		cmbReasonForReconsideration = binder.buildAndBind("Reason for Reconsideration" , "reasonForReconsideration" , ComboBox.class);
		cmbReasonForReconsideration.setWidth("220px");
		/*loadReasonForReconsiderationDropDown();*/
		cmbDiagnosisHospitalCash = (ComboBox) binder.buildAndBind("Diagnosis Hospital Cash",
				"diagnosisHospitalCash", ComboBox.class);
//		cmbDiagnosisHospitalCash.setRequired(true);
		cmbDiagnosisHospitalCash.setEnabled(false);
		cmbHospitalCashDueTo = (ComboBox) binder.buildAndBind("Hospital Cash Due To",
				"hospitalCashDueTo", ComboBox.class);
		/*cmbHospitalCashDueTo.setRequired(true);*/
		
		
		dateOfAdmission = binder.buildAndBind("Date of\nAdmission","dateOfAdmission",DateField.class);
		
		dateOfDischarge = binder.buildAndBind("Date Of\nDischarge", "dateOfDischarge", DateField.class);
		
		txtHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed\n(Hospitalisation)" , "hospitalizationClaimedAmount",TextField.class);
		txtPreHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed\n(Pre-Hosp)", "preHospitalizationClaimedAmount", TextField.class);
		txtPostHospitalizationClaimedAmt = binder.buildAndBind("Amount Claimed\n(Post-Hosp)", "postHospitalizationClaimedAmount",TextField.class);
		txtOtherBenefitsClaimedAmnt = binder.buildAndBind("Amount Claimed (Other Benefits)", "otherBenefitclaimedAmount",TextField.class);
		txtHospitalCashClaimedAmnt = binder.buildAndBind("Amount Claimed (Hospital Cash)", "HospitalCashClaimedAmnt",TextField.class);
		txtHospitalizationClaimedAmt.setEnabled(false);
		txtHospitalizationClaimedAmt.setNullRepresentation("");
		txtHospitalizationClaimedAmt.setMaxLength(15);
		
		txtPreHospitalizationClaimedAmt.setEnabled(false);
		txtPreHospitalizationClaimedAmt.setNullRepresentation("");
		txtPreHospitalizationClaimedAmt.setMaxLength(15);
		
		txtPostHospitalizationClaimedAmt.setEnabled(false);
		txtPostHospitalizationClaimedAmt.setNullRepresentation("");
		txtPostHospitalizationClaimedAmt.setMaxLength(15);
		
		txtOtherBenefitsClaimedAmnt.setEnabled(false);
		txtOtherBenefitsClaimedAmnt.setNullRepresentation("");
		txtOtherBenefitsClaimedAmnt.setMaxLength(15);
		
		txtHospitalCashClaimedAmnt.setEnabled(false);
		txtHospitalCashClaimedAmnt.setNullRepresentation("");
		txtHospitalCashClaimedAmnt.setMaxLength(15);
		
		
		CSValidator hospClaimedAmtValidator = new CSValidator();
		hospClaimedAmtValidator.extend(txtHospitalizationClaimedAmt);
		hospClaimedAmtValidator.setRegExp("^[0-9.]*$");
		hospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator preHospClaimedAmtValidator = new CSValidator();
		preHospClaimedAmtValidator.extend(txtPreHospitalizationClaimedAmt);
		preHospClaimedAmtValidator.setRegExp("^[0-9.]*$");
		preHospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator postHospClaimedAmtValidator = new CSValidator();
		postHospClaimedAmtValidator.extend(txtPostHospitalizationClaimedAmt);
		postHospClaimedAmtValidator.setRegExp("^[0-9.]*$");
		postHospClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator otherBenefitClaimedAmtValidator = new CSValidator();
		otherBenefitClaimedAmtValidator.extend(txtOtherBenefitsClaimedAmnt);
		otherBenefitClaimedAmtValidator.setRegExp("^[0-9.]*$");
		otherBenefitClaimedAmtValidator.setPreventInvalidTyping(true);
		
		CSValidator hospitalCashClaimedAmntValidator = new CSValidator();
		hospitalCashClaimedAmntValidator.extend(txtHospitalCashClaimedAmnt);
		hospitalCashClaimedAmntValidator.setRegExp("^[0-9]*$");
		hospitalCashClaimedAmntValidator.setPreventInvalidTyping(true);
		
		/*dateOfAdmission = binder.buildAndBind("Date of Admission","dateOfAdmission",DateField.class);
		if(null != this.bean.getClaimDTO().getNewIntimationDto().getAdmissionDate())
		{
			dateOfAdmission.setValue(this.bean.getClaimDTO().getNewIntimationDto().getAdmissionDate());
		}
		dateOfAdmission.setEnabled(false);
		addDateOfAdmissionListener();
		
		txtHospitalName = binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
		
		if(null != this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto())
		{
			txtHospitalName.setValue(this.bean.getClaimDTO().getNewIntimationDto().getHospitalDto().getName());
		}
		txtHospitalName.setEnabled(false);
		
		cmbInsuredPatientName = binder.buildAndBind("Insured Patient Name", "insuredPatientName", ComboBox.class);*/
		
		/*if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			dateOfAdmission.setReadOnly(true);
			dateOfAdmission.setEnabled(false);
			
			txtHospitalName.setReadOnly(true);
			txtHospitalName.setEnabled(false);
		}*/
		

		FormLayout detailsLayout1 = new FormLayout(cmbDocumentsReceivedFrom,documentsReceivedDate,
				cmbReconsiderationRequest,dateOfAdmission,dateOfDischarge);
		
		if((this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			 detailsLayout1 = new FormLayout(cmbDocumentsReceivedFrom,documentsReceivedDate,
					cmbReconsiderationRequest,cmbDiagnosisHospitalCash,cmbHospitalCashDueTo,dateOfAdmission,dateOfDischarge);
		}
//		setRequiredAndValidation(cmbDiagnosisHospitalCash);
		cmbDiagnosisHospitalCash.setVisible(false);
		cmbHospitalCashDueTo.setVisible(false);
		dateOfAdmission.setVisible(false);
		dateOfDischarge.setVisible(false);
		detailsLayout1.setMargin(false);
		FormLayout detailsLayout2 = new FormLayout(cmbModeOfReceipt,txtAdditionalRemarks,txtHospitalizationClaimedAmt,txtPreHospitalizationClaimedAmt,txtPostHospitalizationClaimedAmt,txtOtherBenefitsClaimedAmnt,txtHospitalCashClaimedAmnt);
		detailsLayout2.setMargin(false);
		//HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,new FormLayout(),new FormLayout(),detailsLayout2);
		HorizontalLayout docDetailsLayout = new HorizontalLayout(detailsLayout1,detailsLayout2);
		//docDetailsLayout.setComponentAlignment(detailsLayout2, Alignment.MIDDLE_LEFT);
		docDetailsLayout.setMargin(true);
		docDetailsLayout.setSpacing(true);
		
		//added for new product
		if(/*this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| */(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			cmbDiagnosisHospitalCash.setVisible(true);
			cmbHospitalCashDueTo.setVisible(true);
			dateOfAdmission.setVisible(true);
			dateOfDischarge.setVisible(true);
			cmbHospitalCashDueTo.setRequired(true);
			cmbDiagnosisHospitalCash.setRequired(true);
		}
				
		addDateOfAdmissionListener();
		//docDetailsLayout.setCaption("Document Details");
		
		return docDetailsLayout;
	}
	
	private void loadReasonForReconsiderationDropDown()
	{
		 if(null != cmbReasonForReconsideration)
		 {
			 String reasonForReconsideration = this.bean.getDocumentDetails().getReasonForReconsiderationRequestValue();
			 cmbReasonForReconsideration.setContainerDataSource(reasonForReconsiderationRequest);
			 cmbReasonForReconsideration.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			 cmbReasonForReconsideration.setItemCaptionPropertyId("value");
			 this.cmbReasonForReconsideration.setReadOnly(false);
			 if(null != reasonForReconsiderationRequest && (null != reasonForReconsideration && !("").equalsIgnoreCase(reasonForReconsideration)))
			 {
				 for(int i = 0 ; i<reasonForReconsiderationRequest.size() ; i++)
				 	{
						if ((reasonForReconsideration).equalsIgnoreCase(reasonForReconsiderationRequest.getIdByIndex(i).getValue()))

						{
							this.cmbReasonForReconsideration.setValue(reasonForReconsiderationRequest.getIdByIndex(i));
							break;
						}
					}
			 }
		 }
		 this.cmbReasonForReconsideration.setReadOnly(true);
	}
	
	private HorizontalLayout buildBillClassificationLayout()
	{
		
		chkhospitalization = binder.buildAndBind("Hospitalisation", "hospitalization", CheckBox.class);
		//Vaadin8-setImmediate() chkhospitalization.setImmediate(true);
		//chkhospitalization.setEnabled(false);
		chkPartialHospitalization = binder.buildAndBind("Partial-Hospitalisation", "partialHospitalization", CheckBox.class);
		//Vaadin8-setImmediate() chkPartialHospitalization.setImmediate(true);
		//chkPartialHospitalization.setEnabled(false);
		
		
		//chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalization", CheckBox.class);
		
		//chkPreHospitalization.setEnabled(false);
		
		//chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
		
		chkPreHospitalization = binder.buildAndBind("Pre-Hospitalisation", "preHospitalization", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("preHospitalizationFlag")))
		{
			chkPreHospitalization.setEnabled(false);
		}
		
		
		
		chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("postHospitalizationFlag")))
		{
			chkPostHospitalization.setEnabled(false);
		}
		
		
		chkHospitalizationRepeat = binder.buildAndBind("Hospitalisation (Repeat)", "hospitalizationRepeat", CheckBox.class);
		//chkPostHospitalization.setEnabled(false);
		
		chkLumpSumAmount = binder.buildAndBind("Lumpsum Amount", "lumpSumAmount", CheckBox.class);
		//chkLumpSumAmount.setEnabled(false);
		//chkAddOnBenefitsHospitalCash = binder.buildAndBind("Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash", CheckBox.class);
		//chkAddOnBenefitsHospitalCash.setEnabled(false);
		//chkAddOnBenefitsPatientCare = binder.buildAndBind("Add on Benefits (Patient Care)", "addOnBenefitsPatientCare", CheckBox.class);
		//chkAddOnBenefitsPatientCare.setEnabled(false);
		/*chkhospitalization.setEnabled(false);
		chkPreHospitalization.setEnabled(false);
		chkPostHospitalization.setEnabled(false);
		chkPartialHospitalization.setEnabled(false);
		*///System.out.println("---the claimType value---"+this.bean.getClaimDTO().getClaimTypeValue());
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("LumpSumFlag")))
		{
			chkLumpSumAmount.setEnabled(false);
		}
		
		chkAddOnBenefitsHospitalCash = binder.buildAndBind("Add on Benefits (Hospital cash)", "addOnBenefitsHospitalCash", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
		{
			chkAddOnBenefitsHospitalCash.setEnabled(false);
		}
		
		chkAddOnBenefitsPatientCare = binder.buildAndBind("Add on Benefits (Patient Care)", "addOnBenefitsPatientCare", CheckBox.class);
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
		{
			chkAddOnBenefitsPatientCare.setEnabled(false);
		}
		
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			chkhospitalization.setEnabled(true);
			chkPartialHospitalization.setEnabled(false);
			chkPreHospitalization.setEnabled(false);
			chkPostHospitalization.setEnabled(false);
		}
		else if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
		{
			chkhospitalization.setEnabled(false);
			chkPartialHospitalization.setEnabled(true);
			if(null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("preHospitalizationFlag")))
			{
				chkPreHospitalization.setEnabled(true);
				//chkPreHospitalization.setEnabled(false);
				//chkPreHospitalization.setValue(true);
			}
			
			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
			{
				if(null != chkOtherBenefits){
				chkOtherBenefits.setEnabled(false);
				}
			}
		}
		
		//GLX2020017
		if(null != bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != bean.getClaimDTO() && ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != bean.getClaimDTO() && ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
		{
			if(chkOtherBenefits != null){
				chkOtherBenefits.setEnabled(false);
			}
			if(chkAddOnBenefitsHospitalCash !=null){
				chkAddOnBenefitsHospitalCash.setEnabled(false);
			}
		}
		
		if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(false);
		}
		/*
		if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			chkPartialHospitalization.setEnabled(true);
		}
		else
		{
			chkhospitalization.setEnabled(true);
		}*/
		chkOtherBenefits = binder.buildAndBind("Other Benefits", "otherBenefits", CheckBox.class);
		
		chkHospitalCash = binder.buildAndBind("Hospital Cash", "hospitalCash", CheckBox.class);
		
		chkOtherBenefits.setValue(false);
		
        chkHospitalCash.setEnabled(false);
		
		chkHospitalCash.setValue(false);
		//Vaadin8-setImmediate() chkOtherBenefits.setImmediate(true);

		
		FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkHospitalizationRepeat);
		classificationLayout1.setMargin(false);
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkLumpSumAmount);
		classificationLayout2.setMargin(false);
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsHospitalCash);
		classificationLayout3.setMargin(false);
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization,chkAddOnBenefitsPatientCare);
		classificationLayout4.setMargin(false);
		
		FormLayout classificationLayout5 = new FormLayout(chkOtherBenefits,chkHospitalCash);
		classificationLayout5.setMargin(false);
		/*FormLayout classificationLayout1 = new FormLayout(chkhospitalization,chkLumpSumAmount);
		FormLayout classificationLayout2 = new FormLayout(chkPreHospitalization,chkAddOnBenefitsHospitalCash);
		FormLayout classificationLayout3 = new FormLayout(chkPostHospitalization,chkAddOnBenefitsPatientCare);
		FormLayout classificationLayout4 = new FormLayout(chkPartialHospitalization);*/
		
		HorizontalLayout billClassificationLayout = new HorizontalLayout(classificationLayout1,classificationLayout2,classificationLayout3,classificationLayout4,classificationLayout5);
		//billClassificationLayout.setCaption("Document Details");
		billClassificationLayout.setCaption("Bill Classification");
		billClassificationLayout.setSpacing(false);
		billClassificationLayout.setMargin(false);
//		billClassificationLayout.setWidth("100%");
		
		
		// Added for referToBillEntryIssue
		editBillClassificationBtn = new Button("Edit");
		editBillClassificationBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		
		addBillClassificationLister();		
		
		//HorizontalLayout hLayout = new HorizontalLayout(billClassificationLayout, editBillClassificationBtn);
		if(null != this.bean.getDocumentDetails().getOtherBenefitsFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(this.bean.getDocumentDetails().getOtherBenefitsFlag()))
		{
			if(null != chkOtherBenefits){
				chkOtherBenefits.setValue(true);
			}
		}
		
		if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
		{
			if(null != chkOtherBenefits){
				chkOtherBenefits.setEnabled(false);
			}
		}
		
		//GLX2020017
		if(null != bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_INDIVIDUAL_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != bean.getClaimDTO() && ReferenceTable.STAR_AROGYA_SANJEEVANI_PRODUCT_FLOATER_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != bean.getClaimDTO() && ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())
			||	null != bean.getClaimDTO() && ReferenceTable.GROUP_TOPUP_PROD_KEY.equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()))
		{
			if(chkOtherBenefits != null){
				chkOtherBenefits.setEnabled(false);
			}
			if(chkAddOnBenefitsHospitalCash !=null){
				chkAddOnBenefitsHospitalCash.setEnabled(false);
			}
		}
		
		if(null != this.bean.getDocumentDetails().getHospitalCashFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashFlag()))
		{
			if(null != chkHospitalCash){
				chkHospitalCash.setValue(true);
			}
		}
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			chkhospitalization.setEnabled(false);
			chkHospitalizationRepeat.setEnabled(false);
			chkPreHospitalization.setEnabled(false);
			chkLumpSumAmount.setEnabled(false);
			chkPostHospitalization.setEnabled(false);
			chkPartialHospitalization.setEnabled(false);
			chkAddOnBenefitsHospitalCash.setEnabled(false);
			chkAddOnBenefitsPatientCare.setEnabled(false);
			chkOtherBenefits.setEnabled(false);
			chkHospitalCash.setEnabled(true);
			cmbReconsiderationRequest.setEnabled(false);
			
		}
		
		 if(chkAddOnBenefitsPatientCare != null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				 bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)) {
			 chkAddOnBenefitsPatientCare.setEnabled(false);
		 }
		
		HorizontalLayout hLayout = new HorizontalLayout(billClassificationLayout);
		return hLayout;
		
		//return billClassificationLayout;
	}
	
	
	private void buildOtherBenefitsLayout(Boolean value)
	{
		if(value)
		{
			otherBenefitsLayout.removeAllComponents();
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			chkEmergencyMedicalEvaluation = binder.buildAndBind("Emergency Medical Evacuation", "emergencyMedicalEvaluation", CheckBox.class);
			
			chkCompassionateTravel = binder.buildAndBind("Compassionate Travel", "compassionateTravel", CheckBox.class);
			
			chkRepatriationOfMortalRemains = binder.buildAndBind("Repatriation Of Mortal Remains", "repatriationOfMortalRemains", CheckBox.class);
			
			chkPreferredNetworkHospital = binder.buildAndBind("Preferred Network Hospital", "preferredNetworkHospital", CheckBox.class);
			
			if(null != this.bean.getClaimDTO() && ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				chkPreferredNetworkHospital.setCaption("Valuable Service Provider (Hospital)");
			}
			
			chkSharedAccomodation = binder.buildAndBind("Shared Accomodation", "sharedAccomodation", CheckBox.class);
			
			FormLayout otherBenefitsLayout1 = new FormLayout(chkEmergencyMedicalEvaluation,chkPreferredNetworkHospital);
			FormLayout otherBenefitsLayout2 = new FormLayout(chkCompassionateTravel,chkSharedAccomodation);
			FormLayout otherBenefitsLayout3 = new FormLayout(chkRepatriationOfMortalRemains);
			
			if(null != this.bean.getClaimDTO() && ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
				chkEmergencyMedicalEvaluation.setVisible(false);
				chkSharedAccomodation.setVisible(false);
				chkRepatriationOfMortalRemains.setVisible(false);
			}
			HorizontalLayout otherBenefitLayout = new HorizontalLayout();
			otherBenefitLayout.addComponents(otherBenefitsLayout1,otherBenefitsLayout2,otherBenefitsLayout3);
			
			if(null != this.bean.getClaimDTO().getClaimType() && (SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
			{
				if( null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docReceivedFrom = (SelectValue) cmbDocumentsReceivedFrom.getValue();
						if(null != docReceivedFrom && null != docReceivedFrom.getId() && (ReferenceTable.RECEIVED_FROM_HOSPITAL).equals(docReceivedFrom.getId()))
					{
							otherBenefitLayout.removeAllComponents();
							otherBenefitLayout.addComponents(chkEmergencyMedicalEvaluation,chkRepatriationOfMortalRemains);
					}	
				}else if(this.bean.getDocumentDetails().getDocumentReceivedFromValue() != null && this.bean.getDocumentDetails().getDocumentReceivedFromValue().equalsIgnoreCase("Hospital")){
					otherBenefitLayout.removeAllComponents();
					otherBenefitLayout.addComponents(chkEmergencyMedicalEvaluation,chkRepatriationOfMortalRemains);
				}
			}
			otherBenefitLayout.setSpacing(true);
		//	otherBenefitLayout.setMargin(true);
			
			if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(this.bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())*/
						(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
							&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
									|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
									|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan())))
					|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
				
				chkEmergencyMedicalEvaluation.setVisible(false);
				chkCompassionateTravel.setVisible(false);
				chkRepatriationOfMortalRemains.setVisible(false);
				chkPreferredNetworkHospital.setVisible(false);
			}
			else if(ReferenceTable.POS_FAMILY_HEALTH_OPTIMA.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				chkCompassionateTravel.setVisible(false);
			}
			otherBenefitsLayout.addComponent(otherBenefitLayout);
		}else {
			List<Field<?>> listOfOtherBenefitsChkBox = getListOfOtherBenefitsChkBox();
			unbindField(listOfOtherBenefitsChkBox);
			otherBenefitsLayout.removeAllComponents();
		}
		
		//return otherBenefitsLayput;
		
	}
	
	private List<Field<?>> getListOfOtherBenefitsChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkEmergencyMedicalEvaluation);
		fieldList.add(chkCompassionateTravel);
		fieldList.add(chkRepatriationOfMortalRemains);
		fieldList.add(chkPreferredNetworkHospital);
		fieldList.add(chkSharedAccomodation);
		return fieldList;
	}
	/*private void getPaymentDetailsLayout()
	{
		optPaymentMode = (OptionGroup) binder.buildAndBind("Payment Mode" , "paymentMode" , OptionGroup.class);
		paymentModeListener();
		optPaymentMode.setRequired(true);
		optPaymentMode.addItems(getReadioButtonOptions());
		Item item1 = optPaymentMode.addItem(true);
		Item item2 = optPaymentMode.addItem(false);	
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		optPaymentMode.select(true);
		if(null != this.bean.getDocumentDetails() && null != this.bean.getDocumentDetails().getPaymentModeFlag() &&
				(ReferenceTable.PAYMENT_MODE_CHEQUE_DD).equals(this.bean.getDocumentDetails().getPaymentModeFlag()))
		{

			optPaymentMode.setValue(true);
		}
		else
		{
			optPaymentMode.setValue(false);
		}
		if(null != this.bean.getDocumentDetails() && this.bean.getDocumentDetails().getPaymentMode())
		{
			optPaymentMode.setValue(true);
		}
		else
		{
			optPaymentMode.setValue(false);
		}
		 if(null != this.bean.getClaimDTO() && (ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))
		{
			 optPaymentMode.setReadOnly(true);
			 optPaymentMode.setEnabled(false);
		}
		
		//Vaadin8-setImmediate() optPaymentMode.setImmediate(true);
		optPaymentMode.setEnabled(false);
		//buildPaymentsLayout();
	}*/
	
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	
	/*private HorizontalLayout buildChequePaymentLayout()
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
		
		
	}*/
	
	/*private HorizontalLayout buildBankTransferLayout()
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
		formLayout1.addComponent(txtAccntNo);
		formLayout1.addComponent(txtIfscCode);
		formLayout1.addComponent(txtBankName);
		formLayout1.addComponent(txtCity);
		formLayout1 = new FormLayout(optPaymentMode,txtEmailId,txtPanNo,txtPayableAt);
		
		TextField dField = new TextField();
		dField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dField.setReadOnly(true);
		FormLayout bankTransferLayout2 = new FormLayout(dField,txtBranch);
		
		HorizontalLayout hLayout = new HorizontalLayout(bankTransferLayout1 , bankTransferLayout2);
		//HorizontalLayout hLayout = new HorizontalLayout(formLayout1 , bankTransferLayout2);
		hLayout.setWidth("80%");
		
		return hLayout;
	}*/

	
	
	
	//private RODQueryDetailsTable buildQueryDetailsLayout()
	private Panel buildQueryDetailsLayout()
	{
		//List<RODQueryDetailsDTO> rodQueryDetailsList = this.bean.getRodQueryDetailsList();
	/*	rodQueryDetails.init("", false, false);
		if(null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty())
		{
			rodQueryDetails.setTableList(rodQueryDetailsList);
		}
		
		if(null != reconsiderationRequest)
		{
			rodQueryDetails.generateDropDown(reconsiderationRequest);
		}*/
		rodQueryDetails.initpresenterString(SHAConstants.BILL_ENTRY);
		rodQueryDetails.init("", false, false,reconsiderationRequest);
		loadQueryDetailsTableValues();
		/*if(null != rodQueryDetailsList && !rodQueryDetailsList.isEmpty())
		{
			//rodQueryDetails.setTableList(rodQueryDetailsList);

			for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
				rodQueryDetails.setTableList(rodQueryDetailsList);

				//rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
			}
			
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
			
		}*/
		rodQueryDetails.setEnabled(false);
		
		//HorizontalLayout queryDetailsLayout = new HorizontalLayout(rodQueryDetails);
		Panel queryDetailsLayout = new Panel(rodQueryDetails);
		queryDetailsLayout.setCaption("View Query Details");
	//	queryDetailsLayout.setSpacing(true);
		//queryDetailsLayout.setMargin(true);
		queryDetailsLayout.setSizeFull();
		
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
		reconsiderRequestDetails.initPresenter(SHAConstants.BILL_ENTRY);
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
				
				if(! reconsiderList.getIsRejectReconsidered() && reconsiderList.getSelect()){
					if(chkhospitalization != null){
						chkhospitalization.setEnabled(false);
					}
					if(chkPartialHospitalization != null){
						chkPartialHospitalization.setEnabled(false);
					}if(chkPostHospitalization != null){
						chkPostHospitalization.setEnabled(false);
					}
					if(chkPreHospitalization != null){
						chkPreHospitalization.setEnabled(false);
					}
					if(chkHospitalizationRepeat != null){
						chkHospitalizationRepeat.setEnabled(false);
					}
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
						reconsiderationLayout.addComponent(buildBillClassificationLayout());
						reconsiderationLayout.addComponent(otherBenefitsLayout);
						reconsiderationLayout.addComponent(buildQueryDetailsLayout());
						
						/*if((ReferenceTable.CLAIM_TYPE_CASHLESS).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))
						{
							chkhospitalization.setEnabled(true);
						}
						else
						{
							chkPartialHospitalization.setEnabled(true);
						}*/
						
						//addBillClassificationLister();
						sectionDetailsListenerTableObj.enableDisable(true);
					}
					else
					{
						unbindField(getListOfChkBox());
//						reconsiderationTblLayout = buildReconsiderRequestLayout();
						reconsiderationLayout.addComponent(buildBillClassificationLayout());
						reconsiderationLayout.addComponent(otherBenefitsLayout);
						reconsiderationLayout.addComponent(buildReconsiderRequestLayout());
						sectionDetailsListenerTableObj.enableDisable(false);
						
						/*SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();
						if(reconsiderDTO != null 
								&& (SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
										|| (reconsiderDTO.getPatientStatus() != null
												&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reconsiderDTO.getPatientStatus().getId())
														|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reconsiderDTO.getPatientStatus().getId()))))
								&& selectValue.getValue().equalsIgnoreCase("insured")
								&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
								&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())){
							buildNomineeLayout();
						}*/
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
						
						if(null != chkhospitalization && ("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()))// && null != chkPartialHospitalization)
						{
							chkhospitalization.setEnabled(false);
							if(null != chkhospitalization.getValue() && chkhospitalization.getValue())
							chkhospitalization.setValue(false);
							
							if(null != chkPartialHospitalization)
							chkPartialHospitalization.setEnabled(true);
							
						/*	chkPreHospitalization.setEnabled(true);
							chkPostHospitalization.setEnabled(true);*/
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
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
							
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
							{
								//chkPreHospitalization.setEnabled(false);
								chkPreHospitalization.setEnabled(true);
							}
							//chkPreHospitalization.setEnabled(true);
							if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
							{
								//chkPostHospitalization.setEnabled(false);
								chkPostHospitalization.setEnabled(true);
							}
							
							//chkPartialHospitalization.setValue(false);
							
						}
						if(("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && null != chkLumpSumAmount)
						{
							chkLumpSumAmount.setEnabled(false);
						}
						if(null != sectionDetailsListenerTableObj)
						{
							subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
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
						if(null != chkPreHospitalization)
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

						
						//mandatoryFields.remove(txtEmailId);
					}
					//loadQueryDetailsTableValues();
				}
				
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
					chkhospitalization.setEnabled(false);
					chkHospitalizationRepeat.setEnabled(false);
					chkPreHospitalization.setEnabled(false);
					chkLumpSumAmount.setEnabled(false);
					chkPostHospitalization.setEnabled(false);
					chkPartialHospitalization.setEnabled(false);
					chkAddOnBenefitsHospitalCash.setEnabled(false);
					chkAddOnBenefitsPatientCare.setEnabled(false);
					chkOtherBenefits.setEnabled(false);
					chkHospitalCash.setEnabled(true);
					cmbReconsiderationRequest.setEnabled(false);
					
				}
				
			}
		});
		
		this.sectionDetailsListenerTableObj.dummySubCoverField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -7831804284490287934L;

			@Override
			public void valueChange(ValueChangeEvent event) {TextField property = (TextField) event.getProperty();
			String value = property.getValue();
			subCoverBasedBillClassificationManipulation(value,bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
			}
		});	
		
	}
	
	
	/*private void addDateOfAdmissionListener()
	{
		dateOfAdmission.addValueChangeListener(new Property.ValueChangeListener() {			
			private static final long serialVersionUID = -1774887765294036092L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Date dateOfAdmissionValue = (Date)event.getProperty().getValue();
				
				if(!((bean.getClaimDTO().getNewIntimationDto().getAdmissionDate()).equals(dateOfAdmission)))
				{
					if (insuredLayout != null
							&& insuredLayout.getComponentCount() > 0) {
						insuredLayout.removeAllComponents();
					}
					unbindField(txtReasonForChangeInDOA);
					txtReasonForChangeInDOA = (TextField)binder.buildAndBind("Reason For Change in DOA","changeInReasonDOA",TextField.class);
					txtReasonForChangeInDOA.setRequired(true);
					txtReasonForChangeInDOA.setNullRepresentation("");
					FormLayout formLayout1 = new FormLayout(dateOfAdmission,txtReasonForChangeInDOA);
					FormLayout formLayout2 = new FormLayout(txtHospitalName, cmbInsuredPatientName);
					HorizontalLayout hLayout = new HorizontalLayout(formLayout1,formLayout2);
					//hLayout.setComponentAlignment(formLayout2, Alignment.MIDDLE_LEFT);
					hLayout.setSpacing(true);
					hLayout.setMargin(true);
					hLayout.setWidth("100%");
					insuredLayout.addComponent(hLayout);
				}
			}
		});
	}*/
	
	/*private void paymentModeListener()
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
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					//bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_CHEQUE_DD);
					
				}
				else 
				{

					unbindField(getListOfPaymentFields());
					paymentDetailsLayout.addComponent(buildChequePaymentLayout());
					paymentDetailsLayout.addComponent(buildBankTransferLayout());
					//bean.getDocumentDetails().setPaymentModeFlag(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER);
				}				
			}
		});
	}*/
	
	
	
	/*private void addBillClassificationLister()

	{
		chkhospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 chkPostHospitalization.setEnabled(true);
					 chkPreHospitalization.setEnabled(true);
				 }
				 else
				 {
					 chkPostHospitalization.setEnabled(false);
					 chkPreHospitalization.setEnabled(false);
				 }
//				/ validateBillClassificationDetails();
			}
		});
		
		chkPartialHospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 chkPostHospitalization.setEnabled(true);
					 chkPreHospitalization.setEnabled(true);
				 }
				 else
				 {
					 chkPostHospitalization.setEnabled(false);
					 chkPreHospitalization.setEnabled(false);
				 }
				// validateBillClassificationDetails();
			}
		});
	}*/
	
	
	private void addBillClassificationLister()

	{
		chkhospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
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
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.HOSPITALIZATION, chkhospitalization);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateDuplicationHospPartialHospClassification())
						 {
							 /*Label label = new Label("Already hospitalization is existing for this claim.", ContentMode.HTML);
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
								dialog.show(getUI().getCurrent(), null, true);*/
								
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Already hospitalization is existing for this claim.", buttonsNamewithType);
								//chkhospitalization.setValue(false);
								chkhospitalization.setValue(null);
								if(null != txtHospitalizationClaimedAmt)
								{
									txtHospitalizationClaimedAmt.setValue("");
								
								}
						 }
						 
						 else
						 {
							// fireViewEvent(DocumentDetailsPresenter.VALIDATE_HOSPITALIZATION_REPEAT, bean.getClaimDTO().getKey());

							 txtHospitalizationClaimedAmt.setEnabled(true);
						 }
					 
					 }
				 }
				 else
				 {
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 
					/* if(validateBillClassification())
					 {
						 //Label label = new Label("Pre or Post hospitalization cannot exist without hospitalization", ContentMode.HTML);
						 Label label = new Label("None of the bill classification can exist without hospitalization", ContentMode.HTML);
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
							if(null != chkPreHospitalization)
							{
								chkPreHospitalization.setValue(false);
							}
							if(null != chkPostHospitalization)
							{
								chkPostHospitalization.setValue(false);
							}
							if(null != chkLumpSumAmount)
							{
								chkLumpSumAmount.setValue(false);
							}
							if(null != chkAddOnBenefitsHospitalCash)
							{
								chkAddOnBenefitsHospitalCash.setValue(false);
							}
							if(null != chkAddOnBenefitsPatientCare)
							{
								chkAddOnBenefitsPatientCare.setValue(false);
							}
							if(null != chkHospitalizationRepeat)
							{
								chkHospitalizationRepeat.setValue(false);
							}
							//chkhospitalization.setValue(false);
					 }
					 
					 txtHospitalizationClaimedAmt.setEnabled(false);*/
					 
					 if(validateBillClassification())
					 {
						 /*Label label = new Label("hospitalization is required for bill entry process for reimbursement claim", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
							label);
						 layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("50%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("hospitalization is required for bill entry process for reimbursement claim", buttonsNamewithType);
							chkhospitalization.setValue(true);
					 }
					 /*
					  * If hospitalization required validation is thrown the below code is not
					  * required. Mostly, this scenario wont occur. If itself will cater the needs.
					  * */
					 else
					 {
						 fireViewEvent(BillEntryDocumentDetailsPresenter.VALIDATE_BILL_REVIEW_WITH_BILL_CLASSIFICATION, bean.getDocumentDetails().getRodKey(), ReferenceTable.HOSPITALIZATION,SHAConstants.HOSPITALIZATION);
					 }

					 
					// txtHospitalizationClaimedAmt.setValue(null);

				 }
				 }
				 else if(null != value && value)
				 {
					// txtHospitalizationClaimedAmt.setValue(null);
					 txtHospitalizationClaimedAmt.setEnabled(true);
				 }
			}
			}
		});
		
		
		
		
		
		chkPreHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{ 
					
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
				 if(null != value && !(flag.equalsIgnoreCase(bean.getDocumentDetails().getPreHospitalizationFlag())))
						 {
							 if(value)
							 {
								 if(!bean.getIsQueryReplyReceived()){
								 validateLumpSumClassification(SHAConstants.PREHOSPITALIZATION, chkPreHospitalization);
								 }
								 if(!lumpSumValidationFlag)
								 {
									 if(validateBillClassification())
									 {
										 /*Label label = new Label("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
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
											dialog.show(getUI().getCurrent(), null, true);*/
											HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
											buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
											GalaxyAlertBox.createErrorBox("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
											chkPreHospitalization.setValue(false);
									 }
									 else
									 {
										 txtPreHospitalizationClaimedAmt.setEnabled(true);
									 } 
								 }
							 }
							 else
							 {
									 fireViewEvent(BillEntryDocumentDetailsPresenter.VALIDATE_BILL_REVIEW_WITH_BILL_CLASSIFICATION, bean.getDocumentDetails().getRodKey(), ReferenceTable.PRE_HOSPITALIZATION,SHAConstants.PRE_HOSPITALIZATION);
							 }
						}
				 else if(null != value && value)
				 {
					 validateLumpSumClassification(SHAConstants.PREHOSPITALIZATION, chkPreHospitalization);
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							/* Label label = new Label("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
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
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkPreHospitalization.setValue(false);
						 }
						 else
						 {
							// txtPreHospitalizationClaimedAmt.setValue(null);
							 txtPreHospitalizationClaimedAmt.setEnabled(true);
						 }
					 }
				 }
				}
			}
		});
		
		chkPostHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
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
				 if(null != value && !(flag.equalsIgnoreCase(bean.getDocumentDetails().getPostHospitalizationFlag())))
				 {
				 if(value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.POSTHOSPITALIZATION, chkPostHospitalization);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							/* Label label = new Label("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
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
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkPostHospitalization.setValue(false);
						 }
						 else
						 {
							 txtPostHospitalizationClaimedAmt.setEnabled(true);
					 }
					}
					 
				 }
				 else
				 {
					 fireViewEvent(BillEntryDocumentDetailsPresenter.VALIDATE_BILL_REVIEW_WITH_BILL_CLASSIFICATION, bean.getDocumentDetails().getRodKey(), ReferenceTable.POST_HOSPITALIZATION,SHAConstants.POST_HOSPITALIZATION);

					 /*txtPostHospitalizationClaimedAmt.setEnabled(false);
					 txtPostHospitalizationClaimedAmt.setValue(null);*/

				 }
				 }
				 else if(null != value && value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.POSTHOSPITALIZATION, chkPostHospitalization);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							 /*Label label = new Label("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
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
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkPostHospitalization.setValue(false);
						 }
						 else
						 {
							// txtPostHospitalizationClaimedAmt.setValue(null);
							 txtPostHospitalizationClaimedAmt.setEnabled(true);
						 }
					 }
				 }
			}
			}
		});
		
		
		
		chkLumpSumAmount.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 Boolean value = (Boolean) event.getProperty().getValue();
				 if(null != value && value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					validateLumpSumClassification(SHAConstants.LUMPSUMAMOUNT,chkLumpSumAmount);
					 }
					if(!lumpSumValidationFlag)
					{
						/*
						 * Lumpsum can be first rod for medi premier product. Hence if product is
						 * medipremier, then below validation will not happen.
						 * **/
						if(!((ReferenceTable.MEDIPREMIER_PRODUCT_CODE).equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()) ||
								(ReferenceTable.STAR_CRITICARE_PRODUCT).equalsIgnoreCase(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()) ||
								(ReferenceTable.STAR_CANCER_PRODUCT_KEY).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) ||
								(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) ||
								(ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) ))
							{
								 if(validateBillClassification())
								 {
									 /*Label label = new Label("Lumpsum Amount cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
										label.setStyleName("errMessage");
									 HorizontalLayout layout = new HorizontalLayout(
												label);
										layout.setMargin(true);
										final ConfirmDialog dialog = new ConfirmDialog();
										dialog.setCaption("Errors");
										//dialog.setWidth("55%");
										dialog.setClosable(true);
										dialog.setContent(layout);
										dialog.setResizable(false);
										dialog.setModal(true);
										dialog.show(getUI().getCurrent(), null, true);*/
										HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
										buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
										GalaxyAlertBox.createErrorBox("Lumpsum Amount cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
										chkLumpSumAmount.setValue(false);
										enableOrDisableBillClassification(true);
								 } 
								 else
								 {
									// warnMessageForLumpSum(); 
									 enableOrDisableBillClassification(false);
								 }
						}
						else
						{
							if(!((ReferenceTable.STAR_CANCER_PRODUCT_KEY).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) ||
									(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey()) ||
									(ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND).equals(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())))
							{
							if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getPolicyType() 
									&& ReferenceTable.FRESH_POLICY.equals(bean.getNewIntimationDTO().getPolicy().getPolicyType().getKey()))
							{
								warnMessageForLumpSum();
							}
							 enableOrDisableBillClassification(false);
							}
						}
					}
				 }
				 else
				 {
					 fireViewEvent(BillEntryDocumentDetailsPresenter.VALIDATE_BILL_REVIEW_WITH_BILL_CLASSIFICATION, bean.getDocumentDetails().getRodKey(), ReferenceTable.LUMPSUM,SHAConstants.LUMPSUM);
					 if(null != sectionDetailsListenerTableObj)
					 {
						 String subCovervalue = sectionDetailsListenerTableObj.getSubCoverFieldValue();
						  	if(!(ReferenceTable.LUMPSUM_SUB_COVER_CODE).equalsIgnoreCase(subCovervalue))
							{
						  		enableOrDisableBillClassification(true);
								//enableOrDisableClassificationBasedOnsubCover(5,docRecFromVal);
							}
					 }
				 }
			}
			}
		});
		
		chkAddOnBenefitsHospitalCash.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.HOSPITALCASH, chkAddOnBenefitsHospitalCash);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							/* Label label = new Label("Hospital cash cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
										label);
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
							//	dialog.setWidth("50%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Hospital cash cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkAddOnBenefitsHospitalCash.setValue(false);
						 } 
					 }
				 }
			}
			}
		});
		
		chkAddOnBenefitsPatientCare.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.PATIENTCARE, chkAddOnBenefitsPatientCare);
					 }
					 if(!lumpSumValidationFlag)
					 {
						 if(validateBillClassification())
						 {
							 /*Label label = new Label("Patient care cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
										label);
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("50%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Patient care cannot be selected without selecting hospitalization or partial hosptilization", buttonsNamewithType);
								chkAddOnBenefitsPatientCare.setValue(false);
						 } 
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
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				//boolean value = (Boolean) event.getProperty().getValue();
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
				 if(null != value && !(flag.equalsIgnoreCase(bean.getDocumentDetails().getPartialHospitalizationFlag())))
				 {
				 if(value)
				 {
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.PARTIALHOSPITALIZATION, chkPartialHospitalization);
					 }
					if(!lumpSumValidationFlag)
					{
						 if(validateDuplicationHospPartialHospClassification())
						 {
							 /*Label label = new Label("Already partial hospitalization is existing for this claim.", ContentMode.HTML);
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
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Already partial hospitalization is existing for this claim.", buttonsNamewithType);
							//	chkPartialHospitalization.setValue(false);
								chkPartialHospitalization.setValue(null);
						 }
						 else
						 {
						 //chkPostHospitalization.setEnabled(true);
						 //chkPreHospitalization.setEnabled(true);
							 txtHospitalizationClaimedAmt.setEnabled(true);
						 }
					}

				 }
				 else
				 {
					 
					 if(validateBillClassification())
					 {
						/* Label label = new Label("Pre or Post hospitalization cannot exist without Partial hospitalization", ContentMode.HTML);
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
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("Pre or Post hospitalization cannot exist without Partial hospitalization", buttonsNamewithType);
							if(null != chkPreHospitalization)
							{
								//chkPreHospitalization.setValue(false);
							}
							if(null != chkPostHospitalization)
							{
								//chkPostHospitalization.setValue(false);
							}
							chkPartialHospitalization.setValue(true);
							//chkhospitalization.setValue(false);
					 }
					 else
					 {
						 fireViewEvent(BillEntryDocumentDetailsPresenter.VALIDATE_BILL_REVIEW_WITH_BILL_CLASSIFICATION, bean.getDocumentDetails().getRodKey(), ReferenceTable.HOSPITALIZATION,SHAConstants.PARTIAL_HOSPITALIZATION);
					 }
					 
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 
					 /**
					  * The below code was added for ticket 4601.
					  * */
					 
					/* txtHospitalizationClaimedAmt.setValue(null);
					 txtHospitalizationClaimedAmt.setEnabled(false);*/


				 }
				 }
				 else if(null != value && value)
				 {
					 //txtHospitalizationClaimedAmt.setValue(null);
					 txtHospitalizationClaimedAmt.setEnabled(true);
				 }
			}
			}
		});
		
		chkHospitalizationRepeat
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 //boolean value = (Boolean) event.getProperty().getValue();
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
				 if(null != value && !(flag.equalsIgnoreCase(bean.getDocumentDetails().getHospitalizationRepeatFlag())))
				 {
				 if(value)
				 {
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 
					 /*if(!((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue()) ||
									 (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) ||
									 (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) ||
									 (null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue()) ||
									 (null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue()) ||
									 (null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue()))
					   )*/
					 if(!bean.getIsQueryReplyReceived()){
					 validateLumpSumClassification(SHAConstants.HOSPITALIZATION_REPEAT, chkHospitalizationRepeat);
					 }
					 if(!lumpSumValidationFlag)
					 {
					 if(!((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue()) ||
							 (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) ||
							 (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) ||
							 (null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue()) ||
							 (null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue()) ||
							 (null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue()) ||
							 (null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
					 ))
					 {
						 if(validateBillClassification())
						 {
							/* Label label = new Label("Hospitalization Repeat cannot exist without hospitalization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
									 label);
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("40%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);*/
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createErrorBox("Hospitalization Repeat cannot exist without hospitalization", buttonsNamewithType);
								//chkHospitalizationRepeat.setValue(false);
								chkHospitalizationRepeat.setValue(null);
						 }
						 else
						 {
							 /**
							  * Added for ticket #5202.
							  * */
							fireViewEvent(BillEntryDocumentDetailsPresenter.VALIDATE_HOSPITALIZATION_REPEAT, bean.getClaimDTO().getKey());
							/* txtHospitalizationClaimedAmt.setEnabled(true);
							 chkhospitalization.setEnabled(false);
							 //if(null != chkPreHospitalization && chkPreHospitalization.isEnabled())
							 chkPreHospitalization.setEnabled(false);
							 //if(null != chkPostHospitalization && chkPostHospitalization.isEnabled())
							 chkPostHospitalization.setEnabled(false);
							 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
							 chkPartialHospitalization.setEnabled(false);
							 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
							 chkLumpSumAmount.setEnabled(false);
							 //if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
							 chkAddOnBenefitsHospitalCash.setEnabled(false);
							 //if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
							 chkAddOnBenefitsPatientCare.setEnabled(false);*/
						 }
					 }
					 else
					 {
						 /*Label label = new Label("None of the classification details can be selected along with hospitalization repeat", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
									label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
						//	dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox("None of the classification details can be selected along with hospitalization repeat", buttonsNamewithType);
							//chkHospitalizationRepeat.setValue(false);
							/*
							 * If value is set to false, then again listener is
							 * invoked. To avoid this, value is set to null.
							 * */
							chkHospitalizationRepeat.setValue(null);
							if (null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue())
							 {
								 txtHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) 
							 {
								 txtPreHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) 
							 {
								 txtPostHospitalizationClaimedAmt.setEnabled(true);
							 }
							 if(null != chkAddOnBenefitsHospitalCash && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
								{
									chkAddOnBenefitsHospitalCash.setValue(null);
								}
								else if(null != chkAddOnBenefitsHospitalCash)
								{
									chkAddOnBenefitsHospitalCash.setEnabled(false);
								}
								if(null != chkAddOnBenefitsPatientCare && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
								{
									chkAddOnBenefitsPatientCare.setValue(null);
								}
								else if(null != chkAddOnBenefitsPatientCare)
								{
									chkAddOnBenefitsPatientCare.setEnabled(false);
								}
							 
							/* if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
							 {
								 SelectValue docRecFromVal = (SelectValue)cmbDocumentsReceivedFrom.getValue();
								 if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal.getValue()) && null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue())
								 {
									 chkPreHospitalization.setEnabled(true);
								 }
								 if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal.getValue()) && null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue())
								 {
									 chkPostHospitalization.setEnabled(true);
								 }
								 
							 }*/
							 
							/*chkhospitalization.setValue(false);
							chkPreHospitalization.setValue(false);
							chkPostHospitalization.setValue(false);
							chkPartialHospitalization.setValue(false);
							chkLumpSumAmount.setValue(false);
							chkAddOnBenefitsHospitalCash.setValue(false);
							chkAddOnBenefitsPatientCare.setValue(false);*/
					 }	
				 }
				 }
				 else
				 {
					 /**
					  * The below code is commented here, since the 
					  * same is done after validating with bill entry popup
					  * values.
					  * */
					// txtHospitalizationClaimedAmt.setEnabled(false);
					 
					// if(null != chkhospitalization && chkhospitalization.getValue() != null && !chkhospitalization.getValue())
					 //{
						// txtHospitalizationClaimedAmt.setValue(null);
					 //}
					 
					 //txtHospitalizationClaimedAmt.setValue(null);
					 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
					 {
						 SelectValue docRecFromVal = (SelectValue)cmbDocumentsReceivedFrom.getValue();
						 if(null != chkhospitalization && ( 
								 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(docRecFromVal.getValue())) //||("Reimbursement").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
								 )) 
						 if(null != chkhospitalization && !chkhospitalization.isEnabled())
						 chkhospitalization.setEnabled(true);
						// if(null != chkPreHospitalization && !chkPreHospitalization.isEnabled())
						 if(null != chkPreHospitalization && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
						 {
							/* if(("Insured").equalsIgnoreCase(docRecFromVal.getValue()))
							 {
								 chkPreHospitalization.setEnabled(false);
								 chkPreHospitalization.setValue(true);
							 }
							 else*/
							 {
								 chkPreHospitalization.setEnabled(true);
							 }
							 
						 }
						 //if(null != chkPostHospitalization && !chkPostHospitalization.isEnabled())
						 if(null != chkPostHospitalization && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
						 {
							 /*if(("Insured").equalsIgnoreCase(docRecFromVal.getValue()))
							 {
								 chkPostHospitalization.setEnabled(false);
								 chkPostHospitalization.setValue(true);
							 }*/
							// else
							 {
								 chkPostHospitalization.setEnabled(true);
							 }
						 }
						 if(null != chkPartialHospitalization && ( 
								 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(docRecFromVal.getValue())) //||("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
								 )) 
						 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
						 chkPartialHospitalization.setEnabled(true);
						if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("LumpSumFlag"))) 
						 chkLumpSumAmount.setEnabled(true);
						 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
						 chkAddOnBenefitsHospitalCash.setEnabled(true);
						 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
						 chkAddOnBenefitsPatientCare.setEnabled(true);
					 }
					 
					 fireViewEvent(BillEntryDocumentDetailsPresenter.VALIDATE_BILL_REVIEW_WITH_BILL_CLASSIFICATION, bean.getDocumentDetails().getRodKey(), ReferenceTable.HOSPITALIZATION,SHAConstants.HOSPITALIZATION_REPEAT);

					 
					 
					/* else
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Please select documents received from"));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkHospitalizationRepeat.setValue(false);
					 }*/
				 }
				 }
				 else if(null != value && value)
				 {
					 chkHospitalizationRepeat.setValue(true);
					 txtHospitalizationClaimedAmt.setEnabled(true);
						if(null != chkPartialHospitalization)
						{
							chkPartialHospitalization.setValue(null);		
							chkPartialHospitalization.setEnabled(false);
							//chkPartialHospitalization.setReadOnly(true);
						}
						if(null != chkPreHospitalization)
						{
							chkPreHospitalization.setValue(null);
							chkPreHospitalization.setEnabled(false);
							//chkPreHospitalization.setReadOnly(true);
							txtPreHospitalizationClaimedAmt.setValue(null);
							txtPreHospitalizationClaimedAmt.setEnabled(false);
						}
						if(null != chkPostHospitalization)
						{
							chkPostHospitalization.setValue(null);
							chkPostHospitalization.setEnabled(false);
						//	chkPostHospitalization.setReadOnly(true);
							txtPostHospitalizationClaimedAmt.setValue(null);
							txtPostHospitalizationClaimedAmt.setEnabled(false);
						}
						if(null != chkhospitalization)
						{
							chkhospitalization.setValue(null);
							chkhospitalization.setEnabled(false);
							//chkhospitalization.setReadOnly(true);
						}
						if(null != chkAddOnBenefitsHospitalCash)
						{
							chkAddOnBenefitsHospitalCash.setValue(null);
							chkAddOnBenefitsHospitalCash.setEnabled(false);
						}
						if(null != chkAddOnBenefitsPatientCare)
						{
							chkAddOnBenefitsPatientCare.setValue(null);
							chkAddOnBenefitsPatientCare.setEnabled(false);
						}
				 }
			}
			}
		});
		
		editBillClassificationBtn.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = -7023902310675004614L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(null != chkhospitalization)
				{
					chkhospitalization.setReadOnly(false);
					chkhospitalization.setEnabled(true);
				}
				if(null != chkPreHospitalization)
				{
					chkPreHospitalization.setReadOnly(false);
					chkPreHospitalization.setEnabled(true);
				}
				if(null != chkPostHospitalization)
				{
					chkPostHospitalization.setReadOnly(false);
					chkPostHospitalization.setEnabled(true);
				}
				if(null != chkPartialHospitalization)
				{
					chkPartialHospitalization.setReadOnly(false);
					chkPartialHospitalization.setEnabled(true);
				}
				
				if(null != chkAddOnBenefitsHospitalCash)
				{
					chkAddOnBenefitsHospitalCash.setReadOnly(false);
					chkAddOnBenefitsHospitalCash.setEnabled(true);
				}
				if(null != chkAddOnBenefitsPatientCare)
				{
					chkAddOnBenefitsPatientCare.setReadOnly(false);
					chkAddOnBenefitsPatientCare.setEnabled(true);
				}
				
				if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| (bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
								bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
					chkhospitalization.setEnabled(false);
					chkHospitalizationRepeat.setEnabled(false);
					chkPreHospitalization.setEnabled(false);
					chkLumpSumAmount.setEnabled(false);
					chkPostHospitalization.setEnabled(false);
					chkPartialHospitalization.setEnabled(false);
					chkAddOnBenefitsHospitalCash.setEnabled(false);
					chkAddOnBenefitsPatientCare.setEnabled(false);
					chkOtherBenefits.setEnabled(false);
					chkHospitalCash.setEnabled(true);
					cmbReconsiderationRequest.setEnabled(false);
					
				}
			}
		});
		
		chkOtherBenefits .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();					
						 
				 buildOtherBenefitsLayout(value);
				 
				 if(value)
				 {
					 txtOtherBenefitsClaimedAmnt.setEnabled(true);
				 }
				 else{
					 txtOtherBenefitsClaimedAmnt.setEnabled(false);
					 txtOtherBenefitsClaimedAmnt.setValue(null);
				 }
						 						 
						
				 }							
			}
			
		});
		
		chkHospitalCash .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();				
			
				 if(value)
				 {
					 txtHospitalCashClaimedAmnt.setEnabled(true);
				 }
				 else{
					 txtHospitalCashClaimedAmnt.setEnabled(false);
					 txtHospitalCashClaimedAmnt.setValue(null);
				 }
						
				 }							
			}
			
		});
	}
	
	
	/*private void addBillClassificationLister()

	{
		chkhospitalization
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 
					 if(validateBillClassification())
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Already hospitalization is existing for this claim."));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkhospitalization.setValue(false);
							if(null != txtHospitalizationClaimedAmt)
							{
								txtHospitalizationClaimedAmt.setValue("");
							
							}

					 }
					 else
					 {
						 
						 txtHospitalizationClaimedAmt.setEnabled(true);
					 }
					 
				 }
				 else
				 {
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 
					 if(validateBillClassification())
					 {
						 Label label = new Label("hospitalization is required for bill entry process for reimbursement claim", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
							label);
						 layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("50%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							
							chkhospitalization.setValue(true);
					 }
					// else
					 //{
						 //txtHospitalizationClaimedAmt.setEnabled(false);
					//	 txtHospitalizationClaimedAmt.setValue(null);
					 //}

				 }
			}
			}
		});
		
		
		chkHospitalizationRepeat
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					// chkPostHospitalization.setEnabled(true);
					// chkPreHospitalization.setEnabled(true);
					 
					 if(!((null != chkhospitalization && null != chkhospitalization.getValue() && chkhospitalization.getValue()) ||
									 (null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue()) ||
									 (null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue()) ||
									 (null != chkLumpSumAmount && null != chkLumpSumAmount.getValue() && chkLumpSumAmount.getValue()) ||
									 (null != chkAddOnBenefitsHospitalCash && null != chkAddOnBenefitsHospitalCash.getValue() && chkAddOnBenefitsHospitalCash.getValue()) ||
									 (null != chkAddOnBenefitsPatientCare && null != chkAddOnBenefitsPatientCare.getValue() && chkAddOnBenefitsPatientCare.getValue()) ||
									 (null != chkPartialHospitalization && null != chkPartialHospitalization.getValue() && chkPartialHospitalization.getValue())
							 )
					   )
					 {
						 if(validateBillClassification())
						 {
							 Label label = new Label("Hospitalization Repeat cannot exist without hospitalization", ContentMode.HTML);
								label.setStyleName("errMessage");
							 HorizontalLayout layout = new HorizontalLayout(
								label);
								layout.setMargin(true);
								final ConfirmDialog dialog = new ConfirmDialog();
								dialog.setCaption("Errors");
								//dialog.setWidth("40%");
								dialog.setClosable(true);
								dialog.setContent(layout);
								dialog.setResizable(false);
								dialog.setModal(true);
								dialog.show(getUI().getCurrent(), null, true);
								//chkHospitalizationRepeat.setValue(false);
								chkHospitalizationRepeat.setValue(null);
						 }
						 else
						 {
							 txtHospitalizationClaimedAmt.setEnabled(true);
							// if(null != chkhospitalization && chkhospitalization.getValue() != null && !chkhospitalization.getValue())
							// {
								 txtHospitalizationClaimedAmt.setValue(null);
							// }
							 chkhospitalization.setEnabled(false);
							 //if(null != chkPreHospitalization && chkPreHospitalization.isEnabled())
							 chkPreHospitalization.setEnabled(false);
							 //if(null != chkPostHospitalization && chkPostHospitalization.isEnabled())
							 chkPostHospitalization.setEnabled(false);
							 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
							 chkPartialHospitalization.setEnabled(false);
							 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
							 chkLumpSumAmount.setEnabled(false);
							 //if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
							 chkAddOnBenefitsHospitalCash.setEnabled(false);
							 //if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
							 chkAddOnBenefitsPatientCare.setEnabled(false);
						 }
					 }
					 else
					 { 
						 Label label = new Label("None of the classification details can be selected along with hospitalization repeat", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
							label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							//dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkHospitalizationRepeat.setValue(false);
							if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
							 {
								 SelectValue docRecFromVal = (SelectValue)cmbDocumentsReceivedFrom.getValue();
								 if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal.getValue()) && null != chkPreHospitalization && null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue())
								 {
									 chkPreHospitalization.setEnabled(true);
								 }
								 if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal.getValue()) && null != chkPostHospitalization && null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue())
								 {
									 chkPostHospitalization.setEnabled(true);
								 }
								 
							 }
							
							  Added for enabling and disabling 
							/**
							 * Added for enabling and disabling 
							 * bill classification fields , if 
							 * hospitalization repeat is
							 * selected or  unselected.
							 * 
							 * 
							chkhospitalization.setValue(false);
							chkPreHospitalization.setValue(false);
							chkPostHospitalization.setValue(false);
							chkPartialHospitalization.setValue(false);
							chkLumpSumAmount.setValue(false);
							chkAddOnBenefitsHospitalCash.setValue(false);
							chkAddOnBenefitsPatientCare.setValue(false);
							chkhospitalization.setValue(null);
							chkPreHospitalization.setValue(null);
							chkPostHospitalization.setValue(null);
							chkPartialHospitalization.setValue(null);
							chkLumpSumAmount.setValue(null);
							chkAddOnBenefitsHospitalCash.setValue(null);
							chkAddOnBenefitsPatientCare.setValue(null);
							
							txtHospitalizationClaimedAmt.setValue(null);
							txtPreHospitalizationClaimedAmt.setValue(null);
							txtPostHospitalizationClaimedAmt.setValue(null);
					 }	
				 }
				 else
				 {
					 txtHospitalizationClaimedAmt.setEnabled(false);
					 txtHospitalizationClaimedAmt.setValue(null);
					 if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
					 {
						 SelectValue docRecFromVal = (SelectValue)cmbDocumentsReceivedFrom.getValue();
						 if(null != chkhospitalization && ( 
								 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(docRecFromVal.getValue())) ||("Reimbursement").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
								 )) 
						 if(null != chkhospitalization && !chkhospitalization.isEnabled())
						 chkhospitalization.setEnabled(true);
						 if(null != chkPreHospitalization && !chkPreHospitalization.isEnabled())
						 chkPreHospitalization.setEnabled(true);
						 if(null != chkPostHospitalization && !chkPostHospitalization.isEnabled())
						 chkPostHospitalization.setEnabled(true);
						 if(null != chkPreHospitalization && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
						 {	 
							 if(("Insured").equalsIgnoreCase(docRecFromVal.getValue()))
							 {
								 chkPreHospitalization.setEnabled(false);
								 chkPreHospitalization.setValue(true);
							 }
							 else
							 {
								 chkPreHospitalization.setEnabled(true);
							 }
					 }
							 
							 //chkPreHospitalization.setEnabled(true);
							 //if(null != chkPostHospitalization && !chkPostHospitalization.isEnabled())
							 if(null != chkPostHospitalization && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
							 {

								 if(("Insured").equalsIgnoreCase(docRecFromVal.getValue()))
								 {
									 chkPostHospitalization.setEnabled(false);
									 chkPostHospitalization.setValue(true);
								 }
								 else
								 {
									 chkPostHospitalization.setEnabled(true);
								 } 
							 }
							 //chkPostHospitalization.setEnabled(true);
						 if(null != chkPartialHospitalization && ( 
								 (("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(docRecFromVal.getValue())) //||("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue())
								 )) 
						 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
						 chkPartialHospitalization.setEnabled(true);
						if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("LumpSumFlag"))) 
						 chkLumpSumAmount.setEnabled(true);
						 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("hospitalCashFlag")))
						 chkAddOnBenefitsHospitalCash.setEnabled(true);
						 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("PatientCareFlag")))
						 chkAddOnBenefitsPatientCare.setEnabled(true);
					 }
					 else
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Please select documents received from"));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkHospitalizationRepeat.setValue(false);
					 }
				 }
			}
			}
		});
		
		chkPreHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					
					 if(validateBillClassification())
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Pre hosptilization cannot be selected without selecting hospitalization or partial hosptilization"));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPreHospitalization.setValue(false);
					 }
					 txtPreHospitalizationClaimedAmt.setEnabled(true);
					 
				 }
				 else
				 {
					
					 txtPreHospitalizationClaimedAmt.setEnabled(false);
					 txtPreHospitalizationClaimedAmt.setValue(null);

				 }
			}
			}
		});
		
		chkPostHospitalization .addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 if(validateBillClassification())
					 {
						 HorizontalLayout layout = new HorizontalLayout(
									new Label("Post hosptilization cannot be selected without selecting hospitalization or partial hosptilization"));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPostHospitalization.setValue(false);
					 }
					 txtPostHospitalizationClaimedAmt.setEnabled(true);
				 }
				 else
				 {
					 txtPostHospitalizationClaimedAmt.setEnabled(false);
					 txtPostHospitalizationClaimedAmt.setValue(null);

				 }
			}
			}
		});
		
		chkLumpSumAmount.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					
					 if(validateBillClassification())
					 {
						 Label label = new Label("Lumpsum Amount cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
							label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
						//	dialog.setWidth("50%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkLumpSumAmount.setValue(false);
					 } 
				 }
			}
			}
		});
		
		chkAddOnBenefitsHospitalCash.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					
					 if(validateBillClassification())
					 {
						 Label label = new Label("Hospital cash cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
							label);
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
						//	dialog.setWidth("50%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkAddOnBenefitsHospitalCash.setValue(false);
					 } 
				 }
			}
			}
		});
		
		chkAddOnBenefitsPatientCare.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				 boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					
					 if(validateBillClassification())
					 {
						 
						 Label label = new Label("Patient care cannot be selected without selecting hospitalization or partial hosptilization", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
							label); 
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							//dialog.setWidth("50%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkAddOnBenefitsPatientCare.setValue(false);
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
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
				boolean value = (Boolean) event.getProperty().getValue();
				 if(value)
				 {
					 
					 if(validateBillClassification())
					 {

						 Label label = new Label("Already partial hospitalization is existing for this claim.", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
									label);

							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							//dialog.setWidth("55%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							chkPartialHospitalization.setValue(false);
					 }
					 //chkPostHospitalization.setEnabled(true);
					 //chkPreHospitalization.setEnabled(true);
					 txtHospitalizationClaimedAmt.setEnabled(true);

				 }
				 else
				 {
					 
					 if(validateBillClassification())
					 {
						 Label label = new Label("partial hospitalization is required for bill entry process for cashless claim", ContentMode.HTML);
							label.setStyleName("errMessage");
						 HorizontalLayout layout = new HorizontalLayout(
							label); 
						 layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
						//	dialog.setWidth("50%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
							
							chkPartialHospitalization.setValue(true);
					 }
					 //chkPostHospitalization.setEnabled(false);
					 //chkPreHospitalization.setEnabled(false);
					 //txtHospitalizationClaimedAmt.setEnabled();

				 }
			}
			}
		});
		
editBillClassificationBtn.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = -7023902310675004614L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(null != chkhospitalization)
				{
					chkhospitalization.setReadOnly(false);
					chkhospitalization.setEnabled(true);
				}
				if(null != chkPreHospitalization)
				{
					chkPreHospitalization.setReadOnly(false);
					chkPreHospitalization.setEnabled(true);
				}
				if(null != chkPostHospitalization)
				{
					chkPostHospitalization.setReadOnly(false);
					chkPostHospitalization.setEnabled(true);
				}
				if(null != chkPartialHospitalization)
				{
					chkPartialHospitalization.setReadOnly(false);
					chkPartialHospitalization.setEnabled(true);
				}
				
				if(null != chkAddOnBenefitsHospitalCash)
				{
					chkAddOnBenefitsHospitalCash.setReadOnly(false);
					chkAddOnBenefitsHospitalCash.setEnabled(true);
				}
				if(null != chkAddOnBenefitsPatientCare)
				{
					chkAddOnBenefitsPatientCare.setReadOnly(false);
					chkAddOnBenefitsPatientCare.setEnabled(true);
				}
			}
		});

		
		
	}*/
	
	/*private int getDifferenceBetweenDates(Date value)
	{
		
		long currentDay = new Date().getTime();
		long enteredDay = value.getTime();
		int diff = (int)((currentDay-enteredDay))/(1000 * 60 * 60 * 24);
		return diff;
	}*/
	
	
	
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
		 
		 
		
		/* for(int i = 0 ; i<payeeNameList.size() ; i++)
		 	{
				if ((this.bean.getClaimDTO()).equalsIgnoreCase(docReceivedFromRequest.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}
		 */
	/*	 insuredPatientList = (BeanItemContainer<SelectValue>) referenceDataMap.get("insuredPatientList");
		 cmbInsuredPatientName.setContainerDataSource(insuredPatientList);
		 cmbInsuredPatientName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbInsuredPatientName.setItemCaptionPropertyId("value");
		 for(int i = 0 ; i<insuredPatientList.size() ; i++)
	 	{
			if ((this.bean.getClaimDTO().getNewIntimationDto().getInsuredPatientName()).equalsIgnoreCase(insuredPatientList.getIdByIndex(i).getValue()))
			{
				this.cmbInsuredPatientName.setValue(insuredPatientList.getIdByIndex(i));
			}
		}
		 cmbInsuredPatientName.setEnabled(false);*/
		 
		 //COMMENTED ON 21-MAY-2019 AS IT IS NOWHERE USED.
		 /*if(null != cmbPayeeName)
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
		 }*/
		 
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
		 
		//added for new product
		 diagnosisHospitalCashContainer = (BeanItemContainer<SelectValue>) referenceDataMap.get("diagnosisHospitalCashContainer");
		 
         cmbDiagnosisHospitalCash.setContainerDataSource(diagnosisHospitalCashContainer);
         cmbDiagnosisHospitalCash.setItemCaptionMode(ItemCaptionMode.PROPERTY);
         cmbDiagnosisHospitalCash.setItemCaptionPropertyId("value");
         
         hospitalCashDueTo = (BeanItemContainer<SelectValue>) referenceDataMap.get("hospitalCashDueTo");
         cmbHospitalCashDueTo.setContainerDataSource(hospitalCashDueTo);
         cmbHospitalCashDueTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
         cmbHospitalCashDueTo.setItemCaptionPropertyId("value");
		 
		 invokeBillClassificationListner();
		 
		 displayAmountClaimedDetails();
		 
		 loadQueryDetailsTableValues();
		 
		 setValuesFromDTO();
		// validateBillClassificationDetails();

	}
	
	
	private List<Field<?>> getListOfChkBox()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(chkhospitalization);
		fieldList.add(chkPreHospitalization);
		fieldList.add(chkPostHospitalization);
		fieldList.add(chkPartialHospitalization);
		fieldList.add(chkLumpSumAmount);
		fieldList.add(chkAddOnBenefitsHospitalCash);
		fieldList.add(chkAddOnBenefitsPatientCare);
		fieldList.add(chkHospitalizationRepeat);
		if(null != cmbReasonForReconsideration)
			fieldList.add(cmbReasonForReconsideration);
		return fieldList;
	}
	
	/*private List<Field<?>> getListOfPaymentFields()
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
	}*/
	
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
	
	public boolean validatePage() {
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		Boolean isReconsiderationRequest = false;
		//{
				if(null != this.cmbReconsiderationRequest )
				{
					SelectValue reconsiderReqValue = (SelectValue)cmbReconsiderationRequest.getValue();
					if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("NO").equalsIgnoreCase(reconsiderReqValue.getValue()))
					{
						if(!((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || 
								(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) ||
								
								(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue()) ||
								
								(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue()) ||
								
								(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) ||
								
								(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue()) ||
								
								(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue()) ||
								
								(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue()) ||
								
								(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue()) ||
								
								(null != this.chkHospitalCash && null != this.chkHospitalCash.getValue() && this.chkHospitalCash.getValue())))
								
						{
							hasError = true;
							eMsg.append("Please select any one bill classification value");
						}
						
						if(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
						{
							if(!((null != this.chkEmergencyMedicalEvaluation && null != this.chkEmergencyMedicalEvaluation.getValue() && this.chkEmergencyMedicalEvaluation.getValue()) ||
								(null != this.chkCompassionateTravel && null != this.chkCompassionateTravel.getValue() && this.chkCompassionateTravel.getValue()) ||
								(null != this.chkRepatriationOfMortalRemains && null != this.chkRepatriationOfMortalRemains.getValue() && this.chkRepatriationOfMortalRemains.getValue()) ||
								(null != this.chkPreferredNetworkHospital && null != this.chkPreferredNetworkHospital.getValue() && this.chkPreferredNetworkHospital.getValue()) ||
								(null != this.chkSharedAccomodation && null != this.chkSharedAccomodation.getValue() && this.chkSharedAccomodation.getValue())))
							{
								
								hasError = true;
								eMsg.append("Please select any one of the benefits </br>");
								
							}
						}
						
						if((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || (null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) ||
								(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue())
								)
						{
							if(!(null != this.txtHospitalizationClaimedAmt && null != this.txtHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtHospitalizationClaimedAmt.getValue())))
							{
								hasError = true;
								eMsg.append("Please enter Hospitalization claimed amount </br>");
							}
						}
						if(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
						{
							if(!(null != this.txtPreHospitalizationClaimedAmt && null != this.txtPreHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtPreHospitalizationClaimedAmt.getValue())))
							{
								hasError = true;
								eMsg.append("Please enter Pre Hospitalization claimed amount </br>");
							}
						}
						if(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
						{
							if(!(null != this.txtPostHospitalizationClaimedAmt && null != this.txtPostHospitalizationClaimedAmt.getValue() && !("").equalsIgnoreCase(this.txtPostHospitalizationClaimedAmt.getValue())))
							{
								hasError = true;
								eMsg.append("Please enter Post Hospitalization claimed amount </br>");
							}
						}
						
						if(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
						{
							if(!(null != this.txtOtherBenefitsClaimedAmnt && null != this.txtOtherBenefitsClaimedAmnt.getValue() && !("").equalsIgnoreCase(this.txtOtherBenefitsClaimedAmnt.getValue())))
							{
								hasError = true;
								eMsg.append("Please enter Other Benefits claimed amount </br>");
							}
						}
						
						//added for new product076
						if(null != this.chkHospitalCash && null != this.chkHospitalCash.getValue() && this.chkHospitalCash.getValue())
						{
							if(!(null != this.txtHospitalCashClaimedAmnt && null != this.txtHospitalCashClaimedAmnt.getValue() && !("").equalsIgnoreCase(this.txtHospitalCashClaimedAmnt.getValue())))
							{
								hasError = true;
								eMsg.append("Please enter hospital Cash claimed amount </br>");
							}
						}
						
						
						if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							
							if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null){
								
							    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();				
								
								if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
										&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
									
									if(null != this.bean.getDocumentDetails().getDocumentsReceivedFrom() && null != this.bean.getDocumentDetails().getDocumentsReceivedFrom().getId() && 
											selectValue != null && selectValue.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)&&
											(this.bean.getDocumentDetails().getHospitalizationFlag() != null && ! this.bean.getDocumentDetails().getHospitalizationFlag().equalsIgnoreCase("Y")
													)){
										hasError = true;
										eMsg.append("Document Received from can not be Hospital for classification type other than Hospital");
										
									}
								}
							}
							}
						
					}
					else if(null != reconsiderReqValue && null != reconsiderReqValue.getValue() && ("YES").equalsIgnoreCase(reconsiderReqValue.getValue().trim()))
					{
						isReconsiderationRequest = true;
						if(!((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) || 
								(null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()) ||
								
								(null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue()) ||
								
								(null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue()) ||
								
								(null != this.chkHospitalizationRepeat && null != this.chkHospitalizationRepeat.getValue() && this.chkHospitalizationRepeat.getValue()) ||
								
								(null != this.chkLumpSumAmount && null != this.chkLumpSumAmount.getValue() && this.chkLumpSumAmount.getValue()) ||
								
								(null != this.chkAddOnBenefitsHospitalCash && null != this.chkAddOnBenefitsHospitalCash.getValue() && this.chkAddOnBenefitsHospitalCash.getValue()) ||
								
								(null != this.chkAddOnBenefitsPatientCare && null != this.chkAddOnBenefitsPatientCare.getValue() && this.chkAddOnBenefitsPatientCare.getValue()) ||
								(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())))							
								
						{
							hasError = true;
							eMsg.append("Please select any one bill classification value");
						}
						
						if(null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
						{
							if(!((null != this.chkEmergencyMedicalEvaluation && null != this.chkEmergencyMedicalEvaluation.getValue() && this.chkEmergencyMedicalEvaluation.getValue()) ||
								(null != this.chkCompassionateTravel && null != this.chkCompassionateTravel.getValue() && this.chkCompassionateTravel.getValue()) ||
								(null != this.chkRepatriationOfMortalRemains && null != this.chkRepatriationOfMortalRemains.getValue() && this.chkRepatriationOfMortalRemains.getValue()) ||
								(null != this.chkPreferredNetworkHospital && null != this.chkPreferredNetworkHospital.getValue() && this.chkPreferredNetworkHospital.getValue()) ||
								(null != this.chkSharedAccomodation && null != this.chkSharedAccomodation.getValue() && this.chkSharedAccomodation.getValue())))
							{
								
								hasError = true;
								eMsg.append("Please select any one of the benefits </br>");
								
							}
						}
						if(null ==  reconsiderRODRequestList || reconsiderRODRequestList.isEmpty())
						{
							hasError = true;
							eMsg.append("No Claim is available for reconsideration. Please reset Reconsideration request to NO to proceed further </br>");
						}
						else if(null != this.reconsiderDTO)
						{
							//ReconsiderRODRequestTableDTO reconsiderDTO = this.bean.getReconsiderRODdto();
							if(!(null != this.reconsiderDTO.getSelect() && this.reconsiderDTO.getSelect()))
							{
								hasError = true;
								eMsg.append("Please select any one earlier rod for reconsideration. If not, then reset reconsideration request to NO to proceed further </br>");
							}
						}
					}
				}
				
				if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
				{
					SelectValue docRecFrom = (SelectValue)cmbDocumentsReceivedFrom.getValue();
					if(docRecFrom != null && docRecFrom.getId() != null &&   docRecFrom.getId().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
						
						Integer doubleFromString = 0;
						
						if(null != txtHospitalizationClaimedAmt)
						
						doubleFromString += SHAUtils.getDoubleFromString(txtHospitalizationClaimedAmt.getValue());
						
						if(null != txtPreHospitalizationClaimedAmt)
//							preHospitalizationAmt = txtPreHospitalizationClaimedAmt.getValue();
						
						doubleFromString += SHAUtils.getDoubleFromString(txtPreHospitalizationClaimedAmt.getValue());
						
						if(null != txtPostHospitalizationClaimedAmt)
//							postHospitalizationAmt = txtPostHospitalizationClaimedAmt.getValue();
							doubleFromString += SHAUtils.getDoubleFromString(txtPreHospitalizationClaimedAmt.getValue());
						
						if(null != txtOtherBenefitsClaimedAmnt)
							doubleFromString += SHAUtils.getDoubleFromString(txtOtherBenefitsClaimedAmnt.getValue());
						
						if(null != txtHospitalCashClaimedAmnt)
							doubleFromString += SHAUtils.getDoubleFromString(txtHospitalCashClaimedAmnt.getValue());
						
						if(txtPanNo != null){
								String panNumber = txtPanNo.getValue();
								
//								if(doubleFromString != null && ((panNumber == null) || (panNumber != null && panNumber.equals("")))){
//									Double totalAmt = Double.valueOf(doubleFromString);
//									if(SHAConstants.FINANCIAL_APPROVED_AMT_FOR_PAN_NUMBER <= totalAmt){
//										hasError = true;
//										eMsg += "Please Enter Pan Number</br>";
//									}
//									
//									
//								}
								
								
						}

						/*if(isReconsiderationRequest
								&& reconsiderDTO != null 
								&& (SHAConstants.DEATH_FLAG.equalsIgnoreCase(bean.getClaimDTO().getIncidenceFlagValue())
												|| (reconsiderDTO.getPatientStatus() != null
														&& (ReferenceTable.PATIENT_STATUS_DECEASED.equals(reconsiderDTO.getPatientStatus().getId())
																|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(reconsiderDTO.getPatientStatus().getId()))))
								&& bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId() != null
								&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getNewIntimationDTO().getInsuredPatient().getRelationshipwithInsuredId().getKey())) {
							
							if(nomineeDetailsTable != null && nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty()){
								List<NomineeDetailsDto> tableList = nomineeDetailsTable.getTableList();
							
								if(tableList != null && !tableList.isEmpty()){
									bean.getNewIntimationDTO().setNomineeList(tableList);
									StringBuffer nomineeNames = new StringBuffer("");
									int selectCnt = 0;
									for (NomineeDetailsDto nomineeDetailsDto : tableList) {
										nomineeDetailsDto.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
										if(nomineeDetailsDto.isSelectedNominee()) {
											nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
										    selectCnt++;	
										}
									}
									bean.getNewIntimationDTO().setNomineeSelectCount(selectCnt);
									if(selectCnt>0){
										bean.getNewIntimationDTO().setNomineeName(nomineeNames.toString());
										bean.getNewIntimationDTO().setNomineeAddr(null);
										hasError = false;
									}
									else{
										bean.getNewIntimationDTO().setNomineeName(null);
										
										eMsg.append("Please Select Nominee<br>");
										hasError = true;						
									}							
								}
							}
							else{
								bean.getNewIntimationDTO().setNomineeList(null);
								bean.getNewIntimationDTO().setNomineeName(null);
								Map<String, String> legalHeirMap = nomineeDetailsTable.getLegalHeirDetails();
								if((legalHeirMap.get("FNAME") != null && !legalHeirMap.get("FNAME").toString().isEmpty())
										&& (legalHeirMap.get("ADDR") != null && !legalHeirMap.get("ADDR").toString().isEmpty()))
								{
									bean.getNewIntimationDTO().setNomineeName(legalHeirMap.get("FNAME").toString());
									bean.getNewIntimationDTO().setNomineeAddr(legalHeirMap.get("ADDR").toString());
									hasError = false;
								}
								else{
									bean.getNewIntimationDTO().setNomineeName(null);
									bean.getNewIntimationDTO().setNomineeAddr(null);
									hasError = true;
									eMsg.append("Please Enter Claimant / Legal Heir Details");
								}
									
							}							
							
						}*/
						
//						if(txtPanNo != null && txtPanNo.getValue() != null  && ! txtPanNo.getValue().equalsIgnoreCase("")){
//							String value = txtPanNo.getValue();
//							if(value.length() != 10){
//								hasError = true;
//								eMsg += "PAN number should be 10 digit value</br>";
//							}
//						}
						
						
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
			SelectValue selValue = (SelectValue)this.cmbDocumentsReceivedFrom.getValue();
			if((null!= selValue) &&("Insured").equalsIgnoreCase(selValue.getValue()))
			{
				if(!(null != this.txtAcknowledgementContactNo && null != this.txtAcknowledgementContactNo.getValue() && !("").equalsIgnoreCase(this.txtAcknowledgementContactNo.getValue())))
				{
					hasError = true;
					eMsg += "Please enter Acknowledgement Contact No</br>";
				}
				if((!(null != this.txtEmailId && null != this.txtEmailId.getValue() && !("").equalsIgnoreCase(this.txtEmailId.getValue()))))
				{
					hasError = true;
					eMsg += "Please enter email Id </br>";
				}
			}
			 
		}*/
				
		// Section details included for Comprehensive product. Remaining products, the Hospitalization will be the default value.........
		if(this.sectionDetailsListenerTableObj != null && !sectionDetailsListenerTableObj.isValid()) {
			hasError = true;
			List<String> errors = this.sectionDetailsListenerTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			}
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey() != null && ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			
			if(cmbDocumentsReceivedFrom != null && cmbDocumentsReceivedFrom.getValue() != null){
				
			    SelectValue selectValue =(SelectValue)cmbDocumentsReceivedFrom.getValue();				
				
				if(this.bean.getClaimDTO().getClaimType() != null && this.bean.getClaimDTO().getClaimType().getId() != null 
						&& this.bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					
					if(selectValue != null && selectValue.getId().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)&&
							(chkOtherBenefits != null && chkOtherBenefits.getValue() != null && chkOtherBenefits.getValue())){
						if(! bean.getDocumentDetails().getIsOtherBenefitApplicableInPreauth()){
						hasError = true;
						eMsg.append("Other Benefits is not applicable. Since It is not applied in Cashless");
						}else{
							if(chkEmergencyMedicalEvaluation != null && chkEmergencyMedicalEvaluation.getValue() && ! this.bean.getDocumentDetails().getIsEmergencyMedicalEvacuation()){
								hasError = true;
								eMsg.append("Emergency Medical Evaluation is not applicable. Since It is not applied in Cashless");
							}
							if(chkRepatriationOfMortalRemains != null && chkRepatriationOfMortalRemains.getValue() && ! this.bean.getDocumentDetails().getIsRepatriationOfMortal()){
								hasError = true;
								eMsg.append("Repatriation Of Mortal Remains is not applicable. Since It is not applied in Cashless");
							}
						}
					} 
				}
			  }
			}
		/*if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			
			if(cmbHospitalCashDueTo == null || cmbHospitalCashDueTo.isEmpty()){
				hasError = true;
				eMsg.append("Please select Hospital Cash Due To.</br>");
			}
			
			if(null == dateOfAdmission.getValue())
			{
				hasError = true;
				eMsg.append("Please enter admission date. </br>");
			}
			
			if(null == dateOfDischarge.getValue())
			{
				hasError = true;
				eMsg.append("Please enter discharge date. </br>");
			}
		}*/
		
		if (hasError) {
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			hasError = true;
			return !hasError;
		} 
		else
		{
			try {
				this.binder.commit();
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
				
				if(null != txtOtherBenefitsClaimedAmnt)
					otherBenefitAmnt = txtOtherBenefitsClaimedAmnt.getValue();
				
				//added for new product
				if(null != txtHospitalCashClaimedAmnt)
					HospitalCashClaimedAmnt = txtHospitalCashClaimedAmnt.getValue();
				
				
				if(!this.sectionDetailsListenerTableObj.getValues().isEmpty()) {
					bean.setSectionDetailsDTO(this.sectionDetailsListenerTableObj.getValues().get(0));
				}
				if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey())){
					if(bean.getUploadDocumentsDTO() != null){
						if(bean.getDocumentDetails().getCompassionateTravelFlag() != null && bean.getDocumentDetails().getCompassionateTravelFlag().equalsIgnoreCase("Y")){
							bean.getUploadDocumentsDTO().setIsCompassionateTravel(true);
						}
						if(bean.getDocumentDetails().getPreferredNetworkHospitalFlag() != null && bean.getDocumentDetails().getPreferredNetworkHospitalFlag().equalsIgnoreCase("Y")){
							bean.getUploadDocumentsDTO().setIsTreatementForPreferred(true);
						}
						if(bean.getDocumentDetails().getSharedAccomodationFlag() != null && bean.getDocumentDetails().getSharedAccomodationFlag().equalsIgnoreCase("Y")){
							bean.getUploadDocumentsDTO().setIsSharedAccomotation(true);
						}
						if(bean.getDocumentDetails().getEmergencyMedicalEvaluationFlag() != null && bean.getDocumentDetails().getEmergencyMedicalEvaluationFlag().equalsIgnoreCase("Y")){
							bean.getUploadDocumentsDTO().setIsEmergencyDomestic(true);
						}
						if(bean.getDocumentDetails().getRepatriationOfMortalRemainsFlag() != null && bean.getDocumentDetails().getRepatriationOfMortalRemainsFlag().equalsIgnoreCase("Y")){
							bean.getUploadDocumentsDTO().setIsRepatriationOfMortal(true);
						}

					}
				}
				
				
				if(null != chkOtherBenefits && chkOtherBenefits.getValue() != null && chkOtherBenefits.getValue() == false)
				{
					bean.getDocumentDetails().setOtherBenefitsFlag(SHAConstants.N_FLAG);
				}
				
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
					SelectValue particularsValue = null;
					for(int i = 0 ; i<checkListTableContainer.size() ; i++)
				 	{
						if (documentCheckListDTO.getDocTypeId() != null && (documentCheckListDTO.getDocTypeId()).equals(checkListTableContainer.getIdByIndex(i).getId()))
						{
							particularsValue = new SelectValue();
							particularsValue.setId((documentCheckListDTO.getKey()));
							particularsValue.setValue(checkListTableContainer.getIdByIndex(i).getValue());
							documentCheckListDTO.setParticulars(particularsValue);
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
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
		if(null != documentDetails.getDiagnosisHospitalCash())
		{
			 for(int i = 0 ; i<diagnosisHospitalCashContainer.size() ; i++)
			 	{
					if ( documentDetails.getDiagnosisHospitalCash().getValue().equalsIgnoreCase(diagnosisHospitalCashContainer.getIdByIndex(i).getValue()))
					{
						this.cmbDiagnosisHospitalCash.setValue(diagnosisHospitalCashContainer.getIdByIndex(i));
					}
				}
		}
	    if(null != documentDetails.getHospitalCashDueTo())
		{
			 for(int i = 0 ; i<hospitalCashDueTo.size() ; i++)
			 	{
					if ( documentDetails.getHospitalCashDueTo().getValue().equalsIgnoreCase(hospitalCashDueTo.getIdByIndex(i).getValue()))
					{
						this.cmbHospitalCashDueTo.setValue(hospitalCashDueTo.getIdByIndex(i));
					}
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
		if(null != this.sectionDetailsListenerTableObj)
			subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
		

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
						/*else
						{
							
						}*/
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
	
	private void invokeBillClassificationListner()
	{
		if(null != this.bean.getDocumentDetails().getHospitalization())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getHospitalization();
			//unbindField(optPaymentMode);
			if(null != chkhospitalization)
			chkhospitalization.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPreHospitalization())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPreHospitalization();
			//unbindField(optPaymentMode);
			if(null != chkPreHospitalization)
			chkPreHospitalization.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPostHospitalization())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPostHospitalization();
			//unbindField(optPaymentMode);
			if(null != chkPostHospitalization)
			chkPostHospitalization.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getPartialHospitalization())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getPartialHospitalization();

			//unbindField(optPaymentMode);
			if(null != chkPartialHospitalization)
			chkPartialHospitalization.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getOtherBenefits())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getOtherBenefits();
			//unbindField(optPaymentMode);
			if(null != chkOtherBenefits)
				chkOtherBenefits.setValue(val);
		}
		
		if(null != this.bean.getDocumentDetails().getHospitalizationRepeat())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getHospitalizationRepeat();
			if(null != chkHospitalizationRepeat)
			{
			if(val)
			{
				//chkHospitalizationRepeat.setValue(false);
				chkHospitalizationRepeat.setValue(null);
				if(null != chkPreHospitalization)
				{
					chkPreHospitalization.setValue(false);
					chkPreHospitalization.setEnabled(false);
				}
				if(null != chkPostHospitalization)
				{
					chkPostHospitalization.setValue(false);
					chkPostHospitalization.setEnabled(false);
				}
				if(null != chkPartialHospitalization)
				{
					chkPartialHospitalization.setValue(null);
					chkPartialHospitalization.setEnabled(false);;
				}
			}
			chkHospitalizationRepeat.setValue(val);
			//unbindField(optPaymentMode);
			//chkHospitalizationRepeat.setValue(val);
			}
		}
		if(null != this.bean.getDocumentDetails().getLumpSumAmount())// && this.bean.getDocumentDetails().getPaymentMode())
		{
			
			Boolean val = this.bean.getDocumentDetails().getLumpSumAmount();
			//unbindField(optPaymentMode);
			if(null != chkLumpSumAmount)
			{
				chkLumpSumAmount.setValue(null);
				chkLumpSumAmount.setValue(val);
			}
		}
		
	}
	
	private void displayAmountClaimedDetails()
	{
		if(null != this.bean.getDocumentDetails())
		{
			if(null != this.bean.getDocumentDetails().getHospitalizationClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalizationClaimedAmount()))
			{
				txtHospitalizationClaimedAmt.setEnabled(true);
				txtHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getHospitalizationClaimedAmount());
			}

			else if ((null != this.chkhospitalization && null != this.chkhospitalization.getValue() && this.chkhospitalization.getValue()) ||
					 (null != this.chkPartialHospitalization && null != this.chkPartialHospitalization.getValue() && this.chkPartialHospitalization.getValue()))
			{
				txtHospitalizationClaimedAmt.setEnabled(true);
			}
			if(null != this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount()))
					
			{
				txtPreHospitalizationClaimedAmt.setEnabled(true);
				txtPreHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount());
			}
			else if (null != this.chkPreHospitalization && null != this.chkPreHospitalization.getValue() && this.chkPreHospitalization.getValue())
			{
				txtPreHospitalizationClaimedAmt.setEnabled(true);
			}
			if(null != this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount()))
					
			{
				txtPostHospitalizationClaimedAmt.setEnabled(true);
				txtPostHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount());
			}
			else if (null != this.chkPostHospitalization && null != this.chkPostHospitalization.getValue() && this.chkPostHospitalization.getValue())
			{
				txtPostHospitalizationClaimedAmt.setEnabled(true);
				//txtPostHospitalizationClaimedAmt.setEnabled(false);
			}
			if(null != this.bean.getDocumentDetails().getOtherBenefitclaimedAmount() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getOtherBenefitclaimedAmount()))
				
			{
				txtOtherBenefitsClaimedAmnt.setEnabled(true);
				txtOtherBenefitsClaimedAmnt.setValue(this.bean.getDocumentDetails().getOtherBenefitclaimedAmount());
			}
			else if (null != this.chkOtherBenefits && null != this.chkOtherBenefits.getValue() && this.chkOtherBenefits.getValue())
			{
				txtOtherBenefitsClaimedAmnt.setEnabled(true);
				//txtPostHospitalizationClaimedAmt.setEnabled(false);
			}
			
            if(null != this.bean.getDocumentDetails().getHospitalCashClaimedAmnt() && !("").equalsIgnoreCase(this.bean.getDocumentDetails().getHospitalCashClaimedAmnt()))
				
			{
            	txtHospitalCashClaimedAmnt.setEnabled(true);
            	txtHospitalCashClaimedAmnt.setValue(this.bean.getDocumentDetails().getHospitalCashClaimedAmnt());
			}
			else if (null != this.chkHospitalCash && null != this.chkHospitalCash.getValue() && this.chkHospitalCash.getValue())
			{
				txtHospitalCashClaimedAmnt.setEnabled(true);
				//txtPostHospitalizationClaimedAmt.setEnabled(false);
			}
		}
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
					int sno = 1;
					for (RODQueryDetailsDTO rodQueryDetailsDTO : rodQueryDetailsList) {
						
						if(rodQueryDetailsDTO.getDocReceivedFrom().equalsIgnoreCase(docRecFromVal))
						{
							rodQueryDetailsDTO.setSno(sno);
							sno++;
							rodQueryDetails.addBeanToList(rodQueryDetailsDTO);
							
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
			/*if(null != dto && null != dto.getIsRejectReconsidered() && dto.getIsRejectReconsidered())
			{
				if(("Cashless").equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue()))
						{
							if(null != chkhospitalization)
							{
								chkhospitalization.setEnabled(true);
							}
							
							enableOrDisableBillClassification(false);
							
						}
				else if (("Insured").equalsIgnoreCase(bean.getDocumentDetails().getDocumentReceivedFromValue()))
				{
					if(null != chkhospitalization)
					{
						chkhospitalization.setEnabled(true);
					}
					if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
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
					//enableOrDisableBillClassification(true);
				}
					
			}*/
			/*else
			{
				if(null != chkhospitalization)
					chkhospitalization.setEnabled(false);
				enableOrDisableBillClassification(false);
			}*/
			
				
			reconsiderationMap.put(dto.getAcknowledgementNo(), dto.getSelect());
			if((null != dto.getHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationFlag())) || 
					(null != dto.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPartialHospitalizationFlag())) ||
					(null != dto.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationRepeatFlag()))
					)
			{
				if(null!= txtHospitalizationClaimedAmt)
				{
					txtHospitalizationClaimedAmt.setEnabled(true);
					if(null != hospitalizationClaimedAmt && !("").equalsIgnoreCase(hospitalizationClaimedAmt))
					{
						txtHospitalizationClaimedAmt.setValue(hospitalizationClaimedAmt);
						
					}
					else
					{
						txtHospitalizationClaimedAmt.setValue(null != dto.getHospitalizationClaimedAmt() ? String.valueOf(dto.getHospitalizationClaimedAmt()) : "");
					}
					
					
					if(null != dto.getHospitalizationFlag() && ("N").equalsIgnoreCase(dto.getHospitalizationFlag()) && null != dto.getIsRejectReconsidered() && !dto.getIsRejectReconsidered())
					{
						chkhospitalization.setEnabled(false);
					}
					if(null != dto.getPartialHospitalizationFlag() && ("N").equalsIgnoreCase(dto.getPartialHospitalizationFlag()) && null != dto.getIsRejectReconsidered() && !dto.getIsRejectReconsidered())
					{
						chkPartialHospitalization.setEnabled(false);
					}
					if(null != dto.getHospitalizationRepeatFlag() && ("N").equalsIgnoreCase(dto.getHospitalizationRepeatFlag()) && null != dto.getIsRejectReconsidered() && !dto.getIsRejectReconsidered())
					{
						chkHospitalizationRepeat.setEnabled(false);
					}
					
					//txtHospitalizationClaimedAmt.setValue(null != dto.getHospitalizationClaimedAmt() ? String.valueOf(dto.getHospitalizationClaimedAmt()) : "");
				}
			}
			else if(null != dto && null != dto.getIsRejectReconsidered() && dto.getIsRejectReconsidered())
			{
				if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Insured").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
				{
					chkhospitalization.setEnabled(false);
					chkPartialHospitalization.setEnabled(true);
					chkHospitalizationRepeat.setEnabled(true);
				}
				else if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
				{
					chkhospitalization.setEnabled(true);
					chkPartialHospitalization.setEnabled(false);
					chkHospitalizationRepeat.setEnabled(false);
				}
				else if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()))// && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
				{
					chkhospitalization.setEnabled(true);
					chkPartialHospitalization.setEnabled(false);
					chkHospitalizationRepeat.setEnabled(true);
				}

			}
			else
			{
				chkhospitalization.setEnabled(false);
				chkPartialHospitalization.setEnabled(false);
				chkHospitalizationRepeat.setEnabled(false);
			}
			if(null != dto.getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPreHospitalizationFlag()))
					{
						if(null!= txtPreHospitalizationClaimedAmt)
						{
							txtPreHospitalizationClaimedAmt.setEnabled(true);
							if(null != preHospitalizationAmt && !("").equalsIgnoreCase(preHospitalizationAmt))
							{
								txtPreHospitalizationClaimedAmt.setValue(preHospitalizationAmt);
								
							}
							else
							{
								txtPreHospitalizationClaimedAmt.setValue(null != dto.getPreHospClaimedAmt() ? String.valueOf(dto.getPreHospClaimedAmt()) : "");
							}
						//	txtPreHospitalizationClaimedAmt.setValue(null != dto.getPreHospClaimedAmt() ? String.valueOf(dto.getPreHospClaimedAmt()) : "");
						}
					}
			
			if(null != dto.getOtherBenefitFlag() && ("Y").equalsIgnoreCase(dto.getOtherBenefitFlag()))
			{
				if(null!= txtOtherBenefitsClaimedAmnt)
				{
					txtOtherBenefitsClaimedAmnt.setEnabled(true);
					if(null != otherBenefitAmnt && !("").equalsIgnoreCase(otherBenefitAmnt))
					{
						txtOtherBenefitsClaimedAmnt.setValue(otherBenefitAmnt);
						
					}
					else
					{
						txtOtherBenefitsClaimedAmnt.setValue(null != dto.getOtherBenefitClaimedAmnt() ? String.valueOf(dto.getOtherBenefitClaimedAmnt()) : "");
					}
				//	txtPreHospitalizationClaimedAmt.setValue(null != dto.getPreHospClaimedAmt() ? String.valueOf(dto.getPreHospClaimedAmt()) : "");
				}
			}
			
			else if(null != dto && null != dto.getIsRejectReconsidered() && dto.getIsRejectReconsidered())
			{
				
				if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
				{
					chkPreHospitalization.setEnabled(false);
				}
				else if (null != dto.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationRepeatFlag()))
				{
					chkPreHospitalization.setEnabled(false);
				}
				
				else if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
				{
					//chkPreHospitalization.setEnabled(false);
					chkPreHospitalization.setEnabled(true);
				}
				else
				{
					chkPreHospitalization.setEnabled(false);
				}
				
				if(null != chkOtherBenefits && null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get(SHAConstants.OTHER_BENEFITS_FLAG)))
				{
					chkOtherBenefits.setEnabled(true);
				}
				else
				{
					chkOtherBenefits.setEnabled(false);
				}
				
			}
			else
			{
				chkPreHospitalization.setEnabled(false);
			}
			if(null != dto.getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPostHospitalizationFlag()))
			{
				
				if(null!= txtPostHospitalizationClaimedAmt)
				{
					txtPostHospitalizationClaimedAmt.setEnabled(true);
					if(null != postHospitalizationAmt && !("").equalsIgnoreCase(postHospitalizationAmt))
					{
						txtPostHospitalizationClaimedAmt.setValue(postHospitalizationAmt);
						
					}
					else
					{
						txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
					}
					//txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
				}
				
				
			}
			else if(null != dto && null != dto.getIsRejectReconsidered() && dto.getIsRejectReconsidered())
			{
				
				if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
				{
					chkPostHospitalization.setEnabled(false);
				}
				else if (null != dto.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationRepeatFlag()))
				{
					chkPostHospitalization.setEnabled(false);
				}
				else if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("postHospitalizationFlag")))
				{
					//chkPreHospitalization.setEnabled(false);
					chkPostHospitalization.setEnabled(true);
				}
				else
				{
					chkPostHospitalization.setEnabled(false);
				}
			}
			else
			{
				chkPostHospitalization.setEnabled(false);
			}
			
			if(null != dto.getAddOnBenefitsHospitalCashFlag() && ("Y").equalsIgnoreCase(dto.getAddOnBenefitsHospitalCashFlag()))
			{
				if(null != dto.getHospitalCashNoOfDaysBills())
					this.bean.getUploadDocumentsDTO().setHospitalCashNoofDays(String.valueOf(dto.getHospitalCashNoOfDaysBills()));
				if(null != dto.getHospitalCashPerDayAmtBills())
					this.bean.getUploadDocumentsDTO().setHospitalCashPerDayAmt(String.valueOf(dto.getHospitalCashPerDayAmtBills()));
				if(null != dto.getHospitalCashTotalClaimedAmount())
					this.bean.getUploadDocumentsDTO().setHospitalCashTotalClaimedAmt(String.valueOf(dto.getHospitalCashTotalClaimedAmount()));
				if(null != dto.getHospitalCashReimbursementBenefitsKey())
					this.bean.getUploadDocumentsDTO().setHospitalCashReimbursementBenefitsKey(dto.getHospitalCashReimbursementBenefitsKey());
			}
			else if(null != dto && null != dto.getIsRejectReconsidered() && dto.getIsRejectReconsidered())
			{
				
				if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
				{
					chkAddOnBenefitsHospitalCash.setEnabled(false);
				}
				else if (null != dto.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationRepeatFlag()))
				{
					chkAddOnBenefitsHospitalCash.setEnabled(false);
				}
				
				
				else if(null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("hospitalCashFlag")))
				{
					chkAddOnBenefitsHospitalCash.setEnabled(true);
				}
				else
				{
					chkAddOnBenefitsHospitalCash.setEnabled(false);
				}
			}
			else
			{
				chkAddOnBenefitsHospitalCash.setEnabled(false);

			}
			
			
			if(null != dto.getAddOnBenefitsPatientCareFlag() && ("Y").equalsIgnoreCase(dto.getAddOnBenefitsPatientCareFlag()))
			{
				if(null != dto.getPatientCareNoOfDaysBills())
					this.bean.getUploadDocumentsDTO().setPatientCareNoofDays(String.valueOf(dto.getPatientCareNoOfDaysBills()));
				if(null != dto.getPatientCarePerDayAmtBills())
					this.bean.getUploadDocumentsDTO().setPatientCarePerDayAmt(String.valueOf(dto.getPatientCarePerDayAmtBills()));
				if(null != dto.getPatientCareTotalClaimedAmount())
					this.bean.getUploadDocumentsDTO().setPatientCareTotalClaimedAmt(String.valueOf(dto.getPatientCareTotalClaimedAmount()));
				if(null != dto.getPatientCareReimbursementBenefitsKey())
					this.bean.getUploadDocumentsDTO().setPatientCareReimbursementBenefitsKey(dto.getPatientCareReimbursementBenefitsKey());
				if(null != dto.getPatientCareDTOList() && !dto.getPatientCareDTOList().isEmpty())
					this.bean.getUploadDocumentsDTO().setReconsiderationPatientCareDTOList(dto.getPatientCareDTOList());
			}
			else if(null != dto && null != dto.getIsRejectReconsidered() && dto.getIsRejectReconsidered())
			{
				
				if(("Cashless").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue()) && ("Hospital").equalsIgnoreCase(this.bean.getDocumentDetails().getDocumentReceivedFromValue()))
				{
					chkAddOnBenefitsHospitalCash.setEnabled(false);
				}
				
				else if (null != dto.getHospitalizationRepeatFlag() && ("Y").equalsIgnoreCase(dto.getHospitalizationRepeatFlag()))
				{
					chkAddOnBenefitsPatientCare.setEnabled(false);
				}
				
				else if(null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("PatientCareFlag")))
				{
					chkAddOnBenefitsPatientCare.setEnabled(true);
				}
				else
				{
					chkAddOnBenefitsPatientCare.setEnabled(false);
				}
			}
			else
			{
				chkAddOnBenefitsPatientCare.setEnabled(false);

			}
			
			/*if(null != dto.getPartialHospitalizationFlag() && ("Y").equalsIgnoreCase(dto.getPartialHospitalizationFlag()))
			{
				if(null!= txtHospitalizationClaimedAmt)
				{
					txtPostHospitalizationClaimedAmt.setEnabled(true);
					txtPostHospitalizationClaimedAmt.setValue(null != dto.getPostHospClaimedAmt() ? String.valueOf(dto.getPostHospClaimedAmt()) : "");
				}
			}*/
			
			
		}
		else
		{
			
			if(null != dto && null != dto.getIsRejectReconsidered() && dto.getIsRejectReconsidered())
			{
				if(null != chkhospitalization)
				{
					chkhospitalization.setEnabled(false);
				}
				enableOrDisableBillClassification(false);
			}
			
			if(null!= txtHospitalizationClaimedAmt)
			{
				txtHospitalizationClaimedAmt.setEnabled(false);
				txtHospitalizationClaimedAmt.setValue(null);
			}
			if(null!= txtPreHospitalizationClaimedAmt)
			{
				txtPreHospitalizationClaimedAmt.setEnabled(false);
				txtPreHospitalizationClaimedAmt.setValue(null);
			}
			if(null!= txtPostHospitalizationClaimedAmt)
			{
				txtPostHospitalizationClaimedAmt.setEnabled(false);
				txtPostHospitalizationClaimedAmt.setValue(null);
			}
			
			if(null!= txtOtherBenefitsClaimedAmnt)
			{
				txtOtherBenefitsClaimedAmnt.setEnabled(false);
				txtOtherBenefitsClaimedAmnt.setValue(null);
			}
			
			hospitalizationClaimedAmt = "";
			preHospitalizationAmt = "";
			postHospitalizationAmt = "";
			otherBenefitAmnt = "";
			this.bean.setReconsiderRODdto(null);
			
			

		}
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			chkhospitalization.setEnabled(false);
			chkHospitalizationRepeat.setEnabled(false);
			chkPreHospitalization.setEnabled(false);
			chkLumpSumAmount.setEnabled(false);
			chkPostHospitalization.setEnabled(false);
			chkPartialHospitalization.setEnabled(false);
			chkAddOnBenefitsHospitalCash.setEnabled(false);
			chkAddOnBenefitsPatientCare.setEnabled(false);
			chkOtherBenefits.setEnabled(false);
			chkHospitalCash.setEnabled(true);
			cmbReconsiderationRequest.setEnabled(false);
			
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
					bean.setIsQueryReplyReceived(true);
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
						chkHospitalizationRepeat.setEnabled(true);
						enablePreAndPostHosp();
						
						 
						
					}
					else if(("Reimbursement").equalsIgnoreCase(this.bean.getClaimDTO().getClaimTypeValue())) 
					{
						chkhospitalization.setEnabled(true);
						chkPartialHospitalization.setEnabled(false);
						chkHospitalizationRepeat.setEnabled(true);
						if(null != chkHospitalizationRepeat && null != chkHospitalizationRepeat.getValue() && chkHospitalizationRepeat.getValue() )
						{
							if(null != chkPreHospitalization)
							{
								chkPreHospitalization.setEnabled(false);
							}
							if(null != chkPostHospitalization)
							{
								chkPostHospitalization.setEnabled(false);
							}
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
					
					if(!(null != rodQueryDetails.getHospitalizationRepeatFlag() &&  ("Y").equalsIgnoreCase(rodQueryDetails.getHospitalizationRepeatFlag())))
					{
						/**
						 * Disabling bill classification at bill entry level for ticket #5148.
						 * */
						//disableBillClassification();
					}/*else
			{
				
			}*/
					
					txtHospitalizationClaimedAmt.setEnabled(false);
					txtPreHospitalizationClaimedAmt.setEnabled(false);
					txtPostHospitalizationClaimedAmt.setEnabled(false);
					
				// if yes , then disable section table.- send parameter as false
					if(null != this.sectionDetailsListenerTableObj)
					{
						/**
						 * Below code was commented. But for issue # 5272, we have uncommented the same.
						 * */
						subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());

						this.sectionDetailsListenerTableObj.setEnabled(false);
					}
				}
			
			else if(("No").equalsIgnoreCase(rodQueryDetails.getReplyStatus()))
			{
				isQueryReplyReceived = false;
				bean.setIsQueryReplyReceived(false);
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
					if(null != chkPreHospitalization)
					{
						chkPreHospitalization.setEnabled(true);
						//if(null != chkPreHospitalization.getValue() && chkPreHospitalization.getValue())
						/*if(null != this.bean.getDocumentDetails().getPreHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPreHospitalizationFlag()))
						{
							chkPreHospitalization.setValue(true);
							txtPreHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPreHospitalizationClaimedAmount());
						}
						else
						{
							chkPreHospitalization.setValue(false);
						}*/
					}
				}
				
				
				
				//chkPostHospitalization = binder.buildAndBind("Post-Hospitalisation", "postHospitalization", CheckBox.class);
				
				if(null != docRecFromVal && ("Insured").equalsIgnoreCase(docRecFromVal) && null != this.bean.getProductBenefitMap() && (1 == this.bean.getProductBenefitMap().get("postHospitalizationFlag")))
				{
					if(null != chkPostHospitalization)
					{
						chkPostHospitalization.setEnabled(true);
						//if(null != chkPostHospitalization.getValue() && chkPostHospitalization.getValue())
						/*if(null != this.bean.getDocumentDetails().getPostHospitalizationFlag() && ("Y").equalsIgnoreCase(this.bean.getDocumentDetails().getPostHospitalizationFlag()))
						{
							chkPostHospitalization.setValue(true);
							txtPostHospitalizationClaimedAmt.setValue(this.bean.getDocumentDetails().getPostHospitalizationClaimedAmount());
						}
						else
						{
							chkPostHospitalization.setValue(false);
						}*/
					}
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
							
							/**
							 * If false is set, then in the listener level, for false, bill
							 * classifications are enabled or disabled. Hence resetting to null
							 * which will not have any effect in listener level.
							 * */
							this.chkLumpSumAmount.setValue(null);
							
							//chkLumpSumAmount.setValue(false);
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
				if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equalsIgnoreCase(docRecFromVal) && null != chkHospitalizationRepeat)
				{
					this.chkHospitalizationRepeat.setEnabled(false);
					chkHospitalizationRepeat.setValue(null);
				}

				else if(null != chkHospitalizationRepeat)
				{
					this.chkHospitalizationRepeat.setEnabled(true);
					chkHospitalizationRepeat.setValue(null);
				}
				if(null != sectionDetailsListenerTableObj)
				{
					// if no , then enable section table. - send it as true
					
					subCoverBasedBillClassificationManipulation(sectionDetailsListenerTableObj.getSubCoverFieldValue(),bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey());
					this.sectionDetailsListenerTableObj.setEnabled(false);
					impactOfSettledReconsideration();
				}			
			}
		}
		//added for new product change.
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				|| (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
			chkhospitalization.setEnabled(false);
			chkHospitalizationRepeat.setEnabled(false);
			chkPreHospitalization.setEnabled(false);
			chkLumpSumAmount.setEnabled(false);
			chkPostHospitalization.setEnabled(false);
			chkPartialHospitalization.setEnabled(false);
			chkAddOnBenefitsHospitalCash.setEnabled(false);
			chkAddOnBenefitsPatientCare.setEnabled(false);
			chkOtherBenefits.setEnabled(false);
			chkHospitalCash.setEnabled(true);

			cmbReconsiderationRequest.setEnabled(false);
			
		}
		
		if(chkAddOnBenefitsPatientCare != null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
				 bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)) {
			 chkAddOnBenefitsPatientCare.setEnabled(false);
		 }
	}
	
	
	private void enablePreAndPostHosp()
	{
		 if(null != bean.getProductBenefitMap() && (1 == bean.getProductBenefitMap().get("preHospitalizationFlag")))
			{
				//chkPreHospitalization.setEnabled(false);
				chkPreHospitalization.setEnabled(true);
			}
		 else
		 {
			 chkPreHospitalization.setEnabled(false);
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
				
			case 11 :
				if(categoryKey.equals(ReferenceTable.LUMPSUM))
				{
					showPopup("Lumpsum is selected as classification in Bill Entry popup. Please Delete or Deselect Lumpsum before making change.",11l,chkBox);
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
		/*Label label = new Label(
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
				true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(message, buttonsNamewithType);
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
		if(null != this.chkLumpSumAmount && categoryKey.equals(ReferenceTable.LUMPSUM))
		{
			chkLumpSumAmount.setValue(true);
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
								if(("Y").equalsIgnoreCase(documentDetailsDTO.getHospitalizationFlag()) && !(ReferenceTable.CANCEL_ROD_KEYS).containsKey(documentDetailsDTO.getStatusId()))
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

	public void setCoverList(BeanItemContainer<SelectValue> coverContainer) {
		this.sectionDetailsListenerTableObj.setCoverList(coverContainer);
		
	}

	public void setSubCoverList(BeanItemContainer<SelectValue> subCoverContainer) {
		this.sectionDetailsListenerTableObj.setSubCoverList(subCoverContainer);
		
	}
	
	private void validateLumpSumClassification(String classificationType,CheckBox chkBox)
	{
		Long claimKey = bean.getClaimDTO().getKey();
		Long rodKey = bean.getDocumentDetails().getRodKey();
		fireViewEvent(BillEntryDocumentDetailsPresenter.BILL_ENTRY_VALIDATE_LUMPSUM_AMOUNT_CLASSIFICATION,claimKey, classificationType, chkBox,rodKey);
	}
	
	public void validateLumpSumAmount(Boolean isValid,String classificationType,CheckBox checkBox) {
		// TODO Auto-generated method stub
		if(isValid && (SHAConstants.LUMPSUMAMOUNT).equalsIgnoreCase(classificationType))
		{
			/* Label label = new Label("Section 2 - Lumpsum cannot be processed under this Intimation", ContentMode.HTML);
				label.setStyleName("errMessage");
			 HorizontalLayout layout = new HorizontalLayout(
						label);
				layout.setMargin(true);
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				//dialog.setWidth("55%");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Section 2 - Lumpsum cannot be processed under this Intimation", buttonsNamewithType);
				chkLumpSumAmount.setValue(null);
				lumpSumValidationFlag = true;
		}
		else if(isValid)
		{
			/*Label label = new Label("Section I cannot be processed under this Intimation", ContentMode.HTML);
			label.setStyleName("errMessage");
		 HorizontalLayout layout = new HorizontalLayout(
					label);
			layout.setMargin(true);
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			//dialog.setWidth("55%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox("Section I cannot be processed under this Intimation", buttonsNamewithType);
			//chkLumpSumAmount.setValue(false);
			if(null != checkBox)
				checkBox.setValue(null);
			lumpSumValidationFlag = true;
		}
		else
		{
			lumpSumValidationFlag = false;
		}
	}
	
	private void enableOrDisableBillClassificationValues(Boolean value)
	{
		if(null != chkhospitalization)
		{
			chkhospitalization.setEnabled(value);
			chkhospitalization.setValue(null);
		}
		if(null != chkPreHospitalization)
		{
			chkPreHospitalization.setEnabled(value);
			chkPreHospitalization.setValue(null);
		}
		if(null != chkPostHospitalization)
		{
			chkPostHospitalization.setEnabled(value);
			chkPostHospitalization.setValue(null);
		}
		if(null != chkPartialHospitalization)
		{
			chkPartialHospitalization.setEnabled(value);
			chkPartialHospitalization.setValue(null);
		}
		if(null != chkAddOnBenefitsHospitalCash)
		{
			chkAddOnBenefitsHospitalCash.setEnabled(value);
			chkAddOnBenefitsHospitalCash.setValue(null);
		}
		if(null != chkAddOnBenefitsPatientCare)
		{
			chkAddOnBenefitsPatientCare.setEnabled(value);
			chkAddOnBenefitsPatientCare.setValue(null);
		}
		if(null != chkHospitalizationRepeat)
		{
			chkHospitalizationRepeat.setEnabled(value);
			chkHospitalizationRepeat.setValue(null);
		}
	}
	
	public void resetLumpsumValidationFlag()
	{
		this.lumpSumValidationFlag = false;
	}
	
	public Boolean warnMessageForLumpSum() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.LUMPSUM_WARNING_MESSAGE + "</b>",
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
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.LUMPSUM_WARNING_MESSAGE + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				alertMessageForLumpSum();
			}
		});
		return true;
	}
	
	public Boolean alertMessageForLumpSum() {
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.LUMPSUM_ALERT_MESSAGE + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
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
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(SHAConstants.LUMPSUM_ALERT_MESSAGE + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
			}
		});
		return true;
	}
	
	 private void enableOrDisableClassificationBasedOnsubCover(int i,String docRecValue)
	    {
	    	
	    	switch(i)
	    	{
	    		case 1:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    				chkPreHospitalization.setEnabled(true);
	    			if(null != chkPostHospitalization)
	    				chkPostHospitalization.setEnabled(true);
	    			if(null != chkHospitalizationRepeat)
	    				chkHospitalizationRepeat.setEnabled(true);
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("hospitalCashFlag") && null != chkAddOnBenefitsHospitalCash))
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    			}
	    			else if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(true);
	    			}
	    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("PatientCareFlag") && null != chkAddOnBenefitsPatientCare))
	    			{
		    			chkAddOnBenefitsPatientCare.setEnabled(false);
//		    			chkAddOnBenefitsPatientCare.setValue(false);
	    			}else if(null != chkAddOnBenefitsPatientCare){
	    				
	    				chkAddOnBenefitsPatientCare.setEnabled(true);
	    				
	    			}
	    			impactOfHospitalizationRepeatClassification();
	    			impactOfSettledReconsideration();
	
	    		break;
	    		
	    		case 2:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPostHospitalization)
	    			{
	    				chkPostHospitalization.setEnabled(false);
	    				chkPostHospitalization.setValue(null);
	    			}
	    			
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setEnabled(false);
	    				chkPreHospitalization.setValue(null);
	    			}
	    			
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setEnabled(false);
	    				chkHospitalizationRepeat.setValue(null);
	    			}
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
	    				chkAddOnBenefitsPatientCare.setEnabled(false);
	    				chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    		break;
	    		
	    		case 3:
	    			
	    			if(null != chkhospitalization)
	    			{
	    				chkhospitalization.setEnabled(false);
	    				chkhospitalization.setValue(null);
	    			}
	    			if(null != chkPartialHospitalization)
	    			{
	    				chkPartialHospitalization.setEnabled(false);
	    				chkPartialHospitalization.setValue(null);
	    			}
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setEnabled(false);
	    				chkPreHospitalization.setValue(null);
	    			}
	    			if(null != chkPostHospitalization)
	    			{
	    				chkPostHospitalization.setEnabled(false);
	    				chkPostHospitalization.setValue(null);
	    			}
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setEnabled(false);
	    				chkHospitalizationRepeat.setValue(null);
	    			}
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
	    				chkAddOnBenefitsPatientCare.setEnabled(false);
	    				chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    		break;
	    		
	    		case 4:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setEnabled(true);
	    				chkPostHospitalization.setEnabled(true);
	    			}
	    			
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setEnabled(false);
	    				chkHospitalizationRepeat.setValue(null);
	    			}
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
	    				chkAddOnBenefitsPatientCare.setEnabled(false);
	    				chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    			
	    		break;
	    		
	    		case 5:
	    			if(null != chkhospitalization)
	    			{
	    				chkhospitalization.setEnabled(false);
	    				chkPartialHospitalization.setEnabled(false);
	    			}
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setEnabled(false);
	    			}
	    			if(null != chkPostHospitalization)
	    			{
	    				chkPostHospitalization.setEnabled(false);
	    			}
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setEnabled(false);
	    			}
	    			if(null != this.bean.getProductBenefitMap() && (0 == this.bean.getProductBenefitMap().get("LumpSumFlag") && null != chkLumpSumAmount))
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    			}
	    			else if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(true);
	    			}
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
	    				chkAddOnBenefitsPatientCare.setEnabled(false);
	    			}
	    			if(null != chkhospitalization)
	    			{
	    				chkhospitalization.setValue(null);
	    			}
	    			if(null != chkPreHospitalization)
	    			{
	    				chkPreHospitalization.setValue(null);
	    			}
	    			if(null != chkPostHospitalization)
	    			{
	    				chkPostHospitalization.setValue(null);
	    			}
	    			if(null != chkPartialHospitalization)
	    			{
	    				chkPartialHospitalization.setValue(null);
	    			}
	    			if(null != chkHospitalizationRepeat)
	    			{
	    				chkHospitalizationRepeat.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    				chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    			
	    			if(null != txtHospitalizationClaimedAmt)
	    			{
	    				txtHospitalizationClaimedAmt.setValue(null);
	    				txtHospitalizationClaimedAmt.setEnabled(false);
	    			}
	    			
	    			if(null != txtPreHospitalizationClaimedAmt)
	    			{
	    				txtPreHospitalizationClaimedAmt.setValue(null);
	    				txtPreHospitalizationClaimedAmt.setEnabled(false);
	    			}
	    			
	    			if(null != txtPostHospitalizationClaimedAmt)
	    			{
	    				txtPostHospitalizationClaimedAmt.setValue(null);
	    				txtPostHospitalizationClaimedAmt.setEnabled(false);
	    			}
	    			
	    		break;	
	    		
	    		case 6:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    				chkPreHospitalization.setEnabled(true);
	    			if(null != chkPostHospitalization)
	    				chkPostHospitalization.setEnabled(true);
	    			if(null != chkHospitalizationRepeat)
	    				chkHospitalizationRepeat.setEnabled(true);
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			 if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
		    			chkAddOnBenefitsPatientCare.setEnabled(false);
		    			chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    			impactOfHospitalizationRepeatClassification();
	    		break;
	    		
	    		case 7:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    				chkPreHospitalization.setEnabled(true);
	    			if(null != chkPostHospitalization)
	    				chkPostHospitalization.setEnabled(true);
	    			if(null != chkHospitalizationRepeat)
	    				chkHospitalizationRepeat.setEnabled(false);
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			 if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
		    			chkAddOnBenefitsPatientCare.setEnabled(false);
		    			chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    		break;
	    		
	    		case 8:
	    			enableDisableHospAndPartialHosp(docRecValue);
	    			if(null != chkPreHospitalization)
	    				chkPreHospitalization.setEnabled(false);
	    			if(null != chkPostHospitalization)
	    				chkPostHospitalization.setEnabled(false);
	    			if(null != chkHospitalizationRepeat)
	    				chkHospitalizationRepeat.setEnabled(false);
	    			if(null != chkLumpSumAmount)
	    			{
	    				chkLumpSumAmount.setEnabled(false);
	    				chkLumpSumAmount.setValue(null);
	    			}
	    			 if(null != chkAddOnBenefitsHospitalCash)
	    			{
	    				chkAddOnBenefitsHospitalCash.setEnabled(false);
	    				chkAddOnBenefitsHospitalCash.setValue(null);
	    			}
	    			if(null != chkAddOnBenefitsPatientCare)
	    			{
		    			chkAddOnBenefitsPatientCare.setEnabled(false);
		    			chkAddOnBenefitsPatientCare.setValue(null);
	    			}
	    		break;
	    		
	    	}
	    	//Added for new product change
			if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
				chkhospitalization.setEnabled(false);
				chkHospitalizationRepeat.setEnabled(false);
				chkPreHospitalization.setEnabled(false);
				chkLumpSumAmount.setEnabled(false);
				chkPostHospitalization.setEnabled(false);
				chkPartialHospitalization.setEnabled(false);
				chkAddOnBenefitsHospitalCash.setEnabled(false);
				chkAddOnBenefitsPatientCare.setEnabled(false);
				chkOtherBenefits.setEnabled(false);
				chkHospitalCash.setEnabled(true);

				cmbReconsiderationRequest.setEnabled(false);
				
			}
			
			if(chkAddOnBenefitsPatientCare != null && bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					 bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)) {
				 chkAddOnBenefitsPatientCare.setEnabled(false);
			 }
	    }
	    
	 private void enableDisableHospAndPartialHosp(String docRecValue)
	    {
	    	if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_INSURED).equalsIgnoreCase(docRecValue))
			{
	    		if(null != chkPartialHospitalization)
	    			chkPartialHospitalization.setEnabled(true);
	    		if(null != chkhospitalization)
	    		{
	    			chkhospitalization.setEnabled(false);
					chkhospitalization.setValue(null);
	    		}
			}
			else if ((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_INSURED).equalsIgnoreCase(docRecValue))
			{
				if(null != chkPartialHospitalization)
				{
					chkPartialHospitalization.setEnabled(false);
					chkPartialHospitalization.setValue(null);
				}
				if(null != chkhospitalization)
					chkhospitalization.setEnabled(true);
				
			}
	    	
	    	if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
					this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode() != null && 
							this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY))){
				chkhospitalization.setEnabled(false);
				chkHospitalizationRepeat.setEnabled(false);
				chkPreHospitalization.setEnabled(false);
				chkLumpSumAmount.setEnabled(false);
				chkPostHospitalization.setEnabled(false);
				chkPartialHospitalization.setEnabled(false);
				chkAddOnBenefitsHospitalCash.setEnabled(false);
				chkAddOnBenefitsPatientCare.setEnabled(false);
				chkOtherBenefits.setEnabled(false);
				chkHospitalCash.setEnabled(true);

				cmbReconsiderationRequest.setEnabled(false);
				
			}
	    }
	    
	    private void subCoverBasedBillClassificationManipulation(String value,Long productKey)
	    {

			String docRecFromVal = null;
			if(null != cmbDocumentsReceivedFrom && null != cmbDocumentsReceivedFrom.getValue())
			{
				SelectValue selValue = (SelectValue)cmbDocumentsReceivedFrom.getValue();
				docRecFromVal = selValue.getValue();
			}
				
			if(!((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(bean.getClaimDTO().getClaimTypeValue()) && (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equalsIgnoreCase(docRecFromVal))) 
			{
				if((ReferenceTable.HOSP_SUB_COVER_CODE).equalsIgnoreCase(value) && ReferenceTable.STAR_WEDDING_GIFT_INSURANCE.equals(productKey))					
				{
					enableOrDisableClassificationBasedOnsubCover(6,docRecFromVal);
				}
				else if((ReferenceTable.HOSP_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(1,docRecFromVal);
				}
				else if(ReferenceTable.MATERNITY_NORMAL_SUB_COVER_CODE.equalsIgnoreCase(value) || ReferenceTable.MATERNITY_CEASEAREAN_SUB_COVER_CODE.equalsIgnoreCase(value) ||
						ReferenceTable.NEW_BORN_SUB_COVER_CODE.equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(2,docRecFromVal);
				}
				else if((ReferenceTable.NEW_BORN_CHILD_VACCINATION_SUB_COVER_CODE).equalsIgnoreCase(value) || (ReferenceTable.DENTAL_OPTHALMIC_SUB_COVER_CODE).equalsIgnoreCase(value) ||
						(ReferenceTable.HOSPITAL_CASH_SUB_COVER_CODE).equalsIgnoreCase(value) || (ReferenceTable.HEALTH_CHECKUP_SUB_COVER_CODE).equalsIgnoreCase(value) || (ReferenceTable.ACCIDENTAL_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(3,docRecFromVal);
				}
				else if((ReferenceTable.BARIATRIC_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(4,docRecFromVal);
				}
				else if((ReferenceTable.LUMPSUM_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(5,docRecFromVal);
				}
				else if((ReferenceTable.NEW_BORN_HOSPITALISATION).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(7,docRecFromVal);
				}
				else if((ReferenceTable.NEW_BORN_LUMPSUM_SUB_COVER_CODE).equalsIgnoreCase(value))
				{
					enableOrDisableClassificationBasedOnsubCover(8,docRecFromVal);
				}
			}
		
	    }
	    
		public void validateHospitalizationRepeat(Boolean isValid) {
			
			if(!isValid)
			{
			/* Label label = new Label("Hospitalization Repeat can be allowed only if Hospitalization is approved for this claim", ContentMode.HTML);
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
				dialog.show(getUI().getCurrent(), null, true);*/
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Hospitalization Repeat can be allowed only if Hospitalization is approved for this claim", buttonsNamewithType);
				chkHospitalizationRepeat.setValue(null);
			}
			else
			{
				 txtHospitalizationClaimedAmt.setEnabled(true);
				 if(null != chkhospitalization && chkhospitalization.getValue() != null && !chkhospitalization.getValue())
				 {
					 txtHospitalizationClaimedAmt.setValue(null);
				 }
				 
				 chkhospitalization.setEnabled(false);
				 //if(null != chkPreHospitalization && chkPreHospitalization.isEnabled())
				 chkPreHospitalization.setEnabled(false);
				 //if(null != chkPostHospitalization && chkPostHospitalization.isEnabled())
				 chkPostHospitalization.setEnabled(false);
				 //if(null != chkPartialHospitalization && chkPartialHospitalization.isEnabled())
				 chkPartialHospitalization.setEnabled(false);
				 //chkPartialHospitalization.setValue(null);
				 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
				 chkLumpSumAmount.setEnabled(false);
				 //if(null != chkAddOnBenefitsHospitalCash && chkAddOnBenefitsHospitalCash.isEnabled())
				 chkAddOnBenefitsHospitalCash.setEnabled(false);
				 //if(null != chkAddOnBenefitsPatientCare && chkAddOnBenefitsPatientCare.isEnabled())
				 chkAddOnBenefitsPatientCare.setEnabled(false);
			}
		}
			
		private void impactOfHospitalizationRepeatClassification()
		{
			if(null != chkHospitalizationRepeat && null != chkHospitalizationRepeat.getValue() && chkHospitalizationRepeat.getValue())
			{
				if(null != chkhospitalization)
					chkhospitalization.setEnabled(false);
				if(null != chkPreHospitalization)// && chkPreHospitalization.isEnabled())
					chkPreHospitalization.setEnabled(false);
				if(null != chkPostHospitalization)// && chkPostHospitalization.isEnabled())
				 chkPostHospitalization.setEnabled(false);
				if(null != chkPartialHospitalization)// && chkPartialHospitalization.isEnabled())
				 chkPartialHospitalization.setEnabled(false);
				 //chkPartialHospitalization.setValue(null);
				 //if(null != chkLumpSumAmount && chkLumpSumAmount.isEnabled())
				if(null != chkLumpSumAmount)
					chkLumpSumAmount.setEnabled(false);
				 if(null != chkAddOnBenefitsHospitalCash)// && chkAddOnBenefitsHospitalCash.isEnabled())
				 chkAddOnBenefitsHospitalCash.setEnabled(false);
				if(null != chkAddOnBenefitsPatientCare)// && chkAddOnBenefitsPatientCare.isEnabled())
				 chkAddOnBenefitsPatientCare.setEnabled(false);
			}
			
		}
		
		private void impactOfSettledReconsideration(){
			
			if(null != reconsiderRODRequestList && !reconsiderRODRequestList.isEmpty())
			{
				for (ReconsiderRODRequestTableDTO reconsiderList : reconsiderRODRequestList) {
					if(null != reconsiderationMap && !reconsiderationMap.isEmpty())
					{
						Boolean isSelect = reconsiderationMap.get(reconsiderList.getAcknowledgementNo());
						reconsiderList.setSelect(isSelect);
					}
					
					if(! reconsiderList.getIsRejectReconsidered() && reconsiderList.getSelect()){
						if(chkhospitalization != null){
							chkhospitalization.setEnabled(false);
						}
						if(chkPartialHospitalization != null){
							chkPartialHospitalization.setEnabled(false);
						}if(chkPostHospitalization != null){
							chkPostHospitalization.setEnabled(false);
						}
						if(chkPreHospitalization != null){
							chkPreHospitalization.setEnabled(false);
						}
						if(chkHospitalizationRepeat != null){
							chkHospitalizationRepeat.setEnabled(false);
						}
						if(null != chkAddOnBenefitsHospitalCash)
			    		{
			    			chkAddOnBenefitsHospitalCash.setEnabled(false);
			    		}
					}
					//reconsiderList.setSelect(null);
				}
				//reconsiderRequestDetails.setTableList(reconsiderRODRequestList);
			}
		}
		
		public void setClearReferenceData(){
			SHAUtils.setClearReferenceData(referenceData);
			if(documentDetailsPageLayout != null){
				documentDetailsPageLayout.removeAllComponents();
			}
		}
		
		
		protected void showPopup(final String message) {

			 String message1 = message.replaceAll("(.{200})", "$1<br />");
			 message1 = message1.replaceAll("(\r\n|\n)", "<br />");

			/*Label successLabel = new Label(
					"<b style = 'color: red;'>" +   message1 + "</br>",
					ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			//layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			layout.setHeightUndefined();*/
			/*if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
				if(message.length()>4000){
				layout.setHeight("100%");
				layout.setWidth("100%");
				}
				
			}		*/	
			/*final ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Alert");
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);*/
		
			
			/*if(bean.getClmPrcsInstruction()!=null && bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
				if(message.length()>4000){
					dialog.setWidth("55%");
				}
			}*/
			/*dialog.show(getUI().getCurrent(), null, true);*/

			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(message1 + "</br>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			homeButton.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						 	//dialog.close();
						 	if(bean.getClmPrcsInstruction()!=null && !bean.getClmPrcsInstruction().equalsIgnoreCase(message)){
						 		showPopup(bean.getClmPrcsInstruction());
						 	}
					}
				});
	    
		
		}
		
		
		public void policyValidationPopupMessage() {	 
			 
			/* Label successLabel = new Label(
						"<b style = 'color: red;'>" + SHAConstants.POLICY_VALIDATION_ALERT + "</b>",
						ContentMode.HTML);
		   		//final Boolean isClicked = false;
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
//				dialog.setCaption("Alert");
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createAlertBox(SHAConstants.POLICY_VALIDATION_ALERT + "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						 /*if(bean.getIsSuspicious()!=null){
								StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
							}*/
					}
				});
			}
		
		private void showICRMessage(){
			String msg = SHAConstants.ICR_MESSAGE;
			/*Label successLabel = new Label("<div style = 'text-align:center;'><b style = 'color: red;'>"+msg+"</b></div>", ContentMode.HTML);

			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout firstForm = new VerticalLayout(successLabel,homeButton);
			firstForm.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			firstForm.setSpacing(true);
			firstForm.setMargin(true);
			firstForm.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(firstForm);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setWidth("20%");
			dialog.show(getUI().getCurrent(), null, true);*/

			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(msg+"</b></div>", buttonsNamewithType);
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					//dialog.close();						
					if(bean.getPreauthDTO().getIsPolicyValidate()){
						policyValidationPopupMessage();
					}/*else if(bean.getIsSuspicious()!=null){
						StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
					}*/						
				}
			});
		}

		public  void handleRemarksPopup(TextArea searchField, final  Listener listener) {
			
		    ShortcutListener enterShortCut = new ShortcutListener(
		        "ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {
			
		      private static final long serialVersionUID = 1L;
		      @Override
		      public void handleAction(Object sender, Object target) {
		        ((ShortcutListener) listener).handleAction(sender, target);
		      }
		    };
		    handleShortcutForRedraft(searchField, getShortCutListenerForRedraftRemarks(searchField));
		    
		  }

		public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
			textField.addFocusListener(new FocusListener() {
				
				@Override
				public void focus(FocusEvent event) {
					textField.addShortcutListener(shortcutListener);
					
				}
			});
			textField.addBlurListener(new BlurListener() {

				@Override
				public void blur(BlurEvent event) {

					textField.removeShortcutListener(shortcutListener);

				}
			});
		}	
		private ShortcutListener getShortCutListenerForRedraftRemarks(final TextArea txtFld)
		{
			ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void handleAction(Object sender, Object target) {
					String remarksValue = (String) txtFld.getData();
					VerticalLayout vLayout =  new VerticalLayout();
					
					vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
					vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
					vLayout.setMargin(true);
					vLayout.setSpacing(true);
					final TextArea txtArea = new TextArea();
//					txtArea.setStyleName("Boldstyle"); 
					txtArea.setValue(txtFld.getValue());
					txtArea.setNullRepresentation("");
					txtArea.setSizeFull();
					txtArea.setWidth("100%");
					txtArea.setMaxLength(4000);
//					txtArea.setRows(remarksValue != null ? (remarksValue.length()/80 >= 25 ? 25 : ((remarksValue.length()/80)%25)+1) : 25);
					txtArea.setReadOnly(true);
					
					
					String splitArray[] = remarksValue.split("[\n*|.*]");
					
					if(splitArray != null && splitArray.length > 0 && splitArray.length > 25){
						txtArea.setRows(25);
					}
					else{
						txtArea.setRows(splitArray.length);
					}
					txtArea.setHeight("30%");
					
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
					
					final Window dialog = new Window();
					
					String strCaption = "Reason for Referring To Billentry";
					
					dialog.setCaption(strCaption);
					
					dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
					dialog.setWidth("45%");
//					dialog.setHeight("75%");
//			    	dialog.setWidth("65%");
					dialog.setClosable(true);
					
					dialog.setContent(vLayout);
//					dialog.setResizable(true);
					dialog.setModal(true);
					dialog.setDraggable(true);
					dialog.setData(txtFld);

					dialog.addCloseListener(new Window.CloseListener() {
						
						@Override
						public void windowClose(CloseEvent e) {
//							TextArea txtArea = (TextArea)dialog.getData();
//							txtArea.setValue(bean.getRedraftRemarks());
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
//							TextArea txtArea = (TextArea)dialog.getData();
//							txtArea.setValue(bean.getRedraftRemarks());
							dialog.close();
						}
					});	
				}
			};
			
			return listener;
		}
		
		public void getHospitalCategory(String hospitalCategory) {	 
			 
			 /*Label successLabel = new Label(
						"<b style = 'color: red;'>" + hospitalCategory + " Category Hospital"+ "</b>",
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
				dialog.show(getUI().getCurrent(), null, true);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(hospitalCategory + " Category Hospital"+ "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;
		
					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}		
						/*else if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
						}*/
					}
				});
			}
		
		public void showTopUpAlertMessage(String remarks) {/*	 
					 
					Label successLabel = new Label(
					"<b style = 'color: red;'>" + remarks + "</b>",
					ContentMode.HTML);
			TextArea txtArea = new TextArea();
			txtArea.setMaxLength(4000);
			txtArea.setData(bean);
			//txtArea.setStyleName("Boldstyle");
			txtArea.setValue(remarks);
			txtArea.setNullRepresentation("");
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
			
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			
			
			txtArea.setRows(remarks.length()/80 >= 25 ? 25 : ((remarks.length()/80)%25)+1);
			VerticalLayout layout = new VerticalLayout(txtArea);
			//layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setHeight(layout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setResizable(false);
			dialog.setModal(true);		
			dialog.show(getUI().getCurrent(), null, true);
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createCustomBox("", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
					.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
							getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
						}
						else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}		
						else if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
						}
					}
				});

		*/
			 
			 
		
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
					.createInformationBox(remarks, buttonsNamewithType);
			
			Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
					getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
				}
				else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
					showICRMessage();
				}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
					policyValidationPopupMessage();
				}		
				/*else if(bean.getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
				}*/
			}
		});

	
		}
		
		public void showCMDAlert(String memberType) {	 
			 
			 /*Label successLabel = new Label(
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
				dialog.show(getUI().getCurrent(), null, true);*/
				
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createInformationBox(SHAConstants.CMD_ALERT_LATEST + memberType + " Club" + "</b>", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						//dialog.close();
						if(null != bean.getPreauthDTO().getTopUpPolicyAlertFlag() && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDTO().getTopUpPolicyAlertFlag())){
							showTopUpAlertMessage(bean.getPreauthDTO().getTopUpPolicyAlertMessage());
						}
						else if(null != bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName()){
							getHospitalCategory(bean.getNewIntimationDTO().getHospitalDto().getFinalGradeName());
						}
						else if(masterService.doGMCPolicyCheckForICR(bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyNumber())){
							showICRMessage();
						}else if(bean.getPreauthDTO().getIsPolicyValidate()){		
							policyValidationPopupMessage();
						}		
						/*else if(bean.getIsSuspicious()!=null){
							StarCommonUtils.showPopup(getUI(), bean.getIsSuspicious(), bean.getClmPrcsInstruction());
						}*/	
					}
				});
			}
		

/*		 public void buildNomineeLayout(){
>>>>>>> Stashed changes
				
			  nomineeDetailsTable = nomineeDetailsTableInstance.get();
					
			  nomineeDetailsTable.init("", false, false);
					
			  if(bean.getNewIntimationDTO().getNomineeList() != null) {
				  nomineeDetailsTable.setTableList(bean.getNewIntimationDTO().getNomineeList());
				  nomineeDetailsTable.generateSelectColumn();
			  }
					
			  documentDetailsPageLayout.addComponent(nomineeDetailsTable);
				
			  boolean enableLegalHeir = nomineeDetailsTable.getTableList() != null && !nomineeDetailsTable.getTableList().isEmpty() ? false : true; 
						
			  legaHeirLayout = nomineeDetailsTable.getLegalHeirLayout(enableLegalHeir);
				
			  if(enableLegalHeir) {
				  nomineeDetailsTable.setLegalHeirDetails("","");
			  }	
					
			  documentDetailsPageLayout.addComponent(legaHeirLayout);
		  }*/
		
		private void addDateOfAdmissionListener()
		{
			dateOfAdmission.addValueChangeListener(new Property.ValueChangeListener() {			
				private static final long serialVersionUID = -1774887765294036092L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					Date dateOfAdmissionValue = (Date)event.getProperty().getValue();
					Date policyFrmDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate();
					Date policyToDate = bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate();
					
					if((bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
							|| bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
							|| bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)
							|| ReferenceTable.getGMCProductCodeListWithoutOtherBanks().containsKey(bean.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getCode()))){

						policyFrmDate = (bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getEffectiveFromDate() != null ? bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getEffectiveFromDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate());
						policyToDate = (bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getEffectiveToDate() != null ? bean.getClaimDTO().getNewIntimationDto().getInsuredPatient().getEffectiveToDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate());
						
						if(bean.getClaimDTO().getNewIntimationDto().getPolicy().getSectionCode() != null && bean.getClaimDTO().getNewIntimationDto().getPolicy().getSectionCode().equalsIgnoreCase(SHAConstants.GMC_SECTION_I) && bean.getClaimDTO().getNewIntimationDto().getGmcMainMember() != null){
							policyFrmDate = (bean.getClaimDTO().getNewIntimationDto().getGmcMainMember().getEffectiveFromDate() != null ? bean.getClaimDTO().getNewIntimationDto().getGmcMainMember().getEffectiveFromDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyFromDate());
							policyToDate = (bean.getClaimDTO().getNewIntimationDto().getGmcMainMember().getEffectiveToDate() != null ? bean.getClaimDTO().getNewIntimationDto().getGmcMainMember().getEffectiveToDate() : bean.getClaimDTO().getNewIntimationDto().getPolicy().getPolicyToDate());		
						}
					}
					if (null != dateOfAdmissionValue && null != policyFrmDate 
							&& null != policyToDate) {
						//if ( !(((DateUtils.isSameDay(dateOfAdmissionValue, policyFrmDate)  || dateOfAdmissionValue.after(policyFrmDate))) && dateOfAdmissionValue.before(policyToDate))) {
						if (!SHAUtils.isDateOfAdmissionWithPolicyRange(policyFrmDate,policyToDate,dateOfAdmissionValue)) {
							dateOfAdmission.setValue(null);
							 	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
										.createErrorBox("Admission Date is not in range between Policy From Date and Policy To Date.", buttonsNamewithType);
								
							
						}
					}
				}
			});
			
			dateOfDischarge.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					Date dischargeDate = (Date)event.getProperty().getValue();
					if(null != dateOfAdmission)
					{
						Date admissionDate = dateOfAdmission.getValue();
						if(null != admissionDate)
						{
							Long diffDays = SHAUtils.getDaysBetweenDate( admissionDate,dischargeDate);
							if(diffDays<0)
							{
								dateOfDischarge.setValue(null);
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								GalaxyAlertBox.createAlertBox("Discharge date is before than admission date. Please enter valid discharge date", buttonsNamewithType);
								
							}
						}
					}
				}
			});
		}
		

}