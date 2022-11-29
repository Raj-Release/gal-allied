package com.shaic.claim.outpatient.processOPpages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.outpatient.registerclaim.wizard.OPRegisterClaimWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.reimbursement.opscreen.OpPresenter;
import com.shaic.claim.reimbursement.processDraftRejectionLetterDetail.DecideOnRejectionPresenter;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.claim.rod.wizard.tables.UploadDocumentGridForm;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsTable;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.themes.BaseTheme;

public class ProcessOPBillDetailsPage extends ViewComponent{

	private static final long serialVersionUID = -5289304019062349766L;

	private OutPatientDTO bean;

	private GWizard wizard;

	private VerticalLayout opRegisterPageLayout;
//	private Panel billDetailsPanel;
	private Panel docUploadPanel;
	private Table table;
	
//	private Panel docDetailsPanel;
//
//	private ComboBox cmbClaimType;
//	private ComboBox cmbInsuredPatientName;
//	private PopupDateField checkupDate;
//	private PopupDateField billReceivedDate;
//	private TextField amountClaimedTxt;
//	private TextField provisionAmtTxt;
//	private ComboBox cmbDOPVisitReason;
//	private TextField OPVisitRemarks;
//
//	private AutocompleteField<State> cmbState;
//	private AutocompleteField<CityTownVillage> cmbCity;
//	private AutocompleteField<HospitalDto> hospitalName;
//
//	private TextArea hospitalAddress;
//	private TextField hospitalPhone;
//	private TextField hospitalFaxNo;
//	private TextField hospitalDocName;
//
//	private State selectedState;
//	private CityTownVillage selectedCity;
//	private HospitalDto selectedHospital;
//	
//	private ComboBox docReceivedFrom;
//	private TextField docSubmittedName;
//	private PopupDateField docReceivedDate;
//	private ComboBox docModeOfReceipt;
//	private TextField docContactNo;
//	private TextField docEmailId;
	
	private TextArea approvalRemarks;
	private TextField approvalAmt;
	private OptionGroup optPaymentMode;
	
	private TextArea rejectRemarks;
	
	private TextField chqmodeChngReason;
	private TextField chqEmailId;
	private TextField chqPanno;
	private TextField chqPayableAt;
	private ComboBox chqPayeeName;
	private TextField chqNameChngReason;
	private TextField chqHeirName;
	private Button btnCitySearch;
	
	private TextField bnkmodeChngReason;
	private TextField bnkEmailId;
	private TextField bnkPanno;
	private TextField bnkAccNo;
	private TextField bnkIfsc;
	private TextField bnkName;
	private TextField bnkCity;
	private ComboBox bnkPayeeName;
	private TextField bnkNameChngReason;
	private TextField bnkHeirName;
	private TextField bnkBranch;
	
	private Button btnIFCSSearch;
	
	private Button btnOk;
	private Button btnCancel;
	
	private Button btnClearAll;
	
	private HorizontalLayout paymentTypelayout;
	private HorizontalLayout previousAccntDetailsButtonLayout;
	private VerticalLayout previousPaymentVerticalLayout;
	
	private VerticalLayout btnsLayout;
	
	private HorizontalLayout buttonsLayout;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	@Inject
	private PreviousAccountDetailsTable previousAccountDetailsTable;
	
	@Inject
	private CitySearchCriteriaViewImpl citySearchCriteriaWindow;
	
	private Window populatePreviousWindowPopup;

	@Inject
	private MasterService masterService;

	@EJB
	private InsuredService insuredService;

	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private DBCalculationService calcService;

	private Map<String, Object> referenceData;

	private StringBuilder errMsg = new StringBuilder();

	public StringBuilder getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(StringBuilder errMsg) {
		this.errMsg = errMsg;
	}

	// Start
	@Inject
	private OPUploadDocumentGridForm uploadDocsTable;
	
	@Inject
	private OPUploadedDocuments uploadedDocsTable;
	
	private List<UploadDocumentDTO> uploadedTblList = new ArrayList<UploadDocumentDTO>();
	
	@Inject
	private Instance<OPBillDetailsTable> opBillDetailsTableInstance;

	private OPBillDetailsTable opBillDetailsTableObj;
	
	private boolean clickAction = false;
	
	private VerticalLayout dynamicLayout;
	
	private VerticalLayout buttonsVlayout;
	
	private Button	approveButton;
	
	//End
	
	

	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		opRegisterPageLayout = new VerticalLayout();
		buttonsVlayout = new VerticalLayout();
		buttonsVlayout.setWidth("100%");
		buttonsVlayout.setVisible(false);
		if(bean.isApprove()){
			buttonsVlayout.setVisible(true);
		}
//		billDetailsPanel = new Panel();
		docUploadPanel = new Panel();
	}

	@SuppressWarnings("deprecation")
	public Component getContent(){
		wizard.getNextButton().setEnabled(true);
		
		/*opBillDetailsTableObj = opBillDetailsTableInstance.get();
		opBillDetailsTableObj.init(new com.shaic.claim.outpatient.processOPpages.OPBillDetailsDTO());
		opBillDetailsTableObj.setVisibleColumns();
		if(bean.getUploadedDocsTableList() != null){
			for(com.shaic.claim.outpatient.processOPpages.OPBillDetailsDTO rec : bean.getOpBillDetailsList()){
				opBillDetailsTableObj.addBeanToList(rec);
			}
		}*/
		
		/*billDetailsPanel.setCaption("Bill Details");
		billDetailsPanel.setContent(opBillDetailsTableObj);
		opRegisterPageLayout.addComponent(billDetailsPanel);*/

		uploadDocsTable.init(new UploadDocumentDTO(),bean, SHAConstants.OUTPATIENT_FLAG);
		BeanItemContainer<SelectValue> beanContainer = masterService.getOPBillingCategoryTypes();
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		uploadDocsTable.setFileTypeValues(beanContainer);
		VerticalLayout containerLayout = new VerticalLayout();
		containerLayout.addComponent(uploadDocsTable);
		
//		bean.setUploadedDocsTableList(uploadedDocsTable.getValues());
//		bean.setUploadedDeletedDocsTableList(uploadedDocsTable.getDeletedDocumentList());
//		bean.setOpBillDetailsList(opBillDetailsTableObj.getValues());
		/*if(!(bean.getUploadedDocsTableList() != null && !bean.getUploadedDocsTableList().isEmpty())){
			uploadedTblList.clear();
		}*/
		uploadedTblList.clear();
		uploadedDocsTable.init("Uploaded Documents", false);
		uploadedDocsTable.setPreseneterString(SHAConstants.OUTPATIENT_FLAG);
		uploadedDocsTable.loadTableColumnsAsPerScreen(SHAConstants.OUTPATIENT_FLAG);
		uploadedDocsTable.setReference(referenceDataMap);
		uploadedDocsTable.setParentObj(this);
		
		uploadedDocsTable.setOpDTO(bean);
		if(bean.getUploadedDocsTableList() != null){
			for(UploadDocumentDTO rec : bean.getUploadedDocsTableList()){
//				uploadedDocsTable.removeRow();
				uploadedDocsTable.addBeanToList(rec);
				uploadedTblList.add(rec);
			}
		}
		
		/*VerticalLayout containerLayout = new VerticalLayout();
		containerLayout.addComponent(opBillDetailsTableObj);*/
		containerLayout.addComponent(uploadedDocsTable);
		containerLayout.setSpacing(true);

//		docUploadPanel.setCaption("Upload Documents");
		docUploadPanel.setContent(containerLayout);
//		opRegisterPageLayout.addComponent(docUploadPanel);
		
		btnsLayout = new VerticalLayout(addButtonLayout(),buttonsVlayout);
		
		if(null != this.uploadedDocsTable)	{
			if(null != uploadedTblList && !uploadedTblList.isEmpty()) {
				 buttonsVlayout.setVisible(false);
				 buttonsLayout.setEnabled(true);
				 Long totPayable = 0l;
						for (UploadDocumentDTO billDetailsDTO : uploadedTblList) {
							totPayable += billDetailsDTO.getBillValue() != null ? billDetailsDTO.getBillValue().intValue() : 0;
						}
						/*if(totPayable != null && totPayable <= 0){
							approveButton.setEnabled(false);
						}*/
			}
		}
		if(bean.isApprove()){
			buttonsVlayout.setVisible(true);
		}
//		opRegisterPageLayout.addComponent(addButtonLayout());
		opRegisterPageLayout.addComponents(docUploadPanel,btnsLayout);
		opRegisterPageLayout.setSpacing(true);
		addListeners();
		/*dynamicLayout = new VerticalLayout();
		opRegisterPageLayout.addComponent(dynamicLayout);*/

		return opRegisterPageLayout;
	}

	public void setDropDownValues(){/*
		BeanItemContainer<SelectValue> claimTypes = masterService.getOPClaimTypeSelectValueContainer(ReferenceTable.CLAIM_TYPE);
		cmbClaimType.setContainerDataSource(claimTypes);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");

		cmbClaimType.setValue(bean.getClaimDTO().getClaimType());

		BeanItemContainer<Insured> insuredList = insuredService.getCLSInsuredList(bean.getPolicyDto().getPolicyNumber());
		cmbInsuredPatientName.setContainerDataSource(insuredList);
		cmbInsuredPatientName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbInsuredPatientName.setItemCaptionPropertyId("insuredName");

		cmbInsuredPatientName.setValue(bean.getNewIntimationDTO().getInsuredPatient());

		BeanItemContainer<SelectValue> reasonTypes = masterService.getOPReason();
		cmbDOPVisitReason.setContainerDataSource(reasonTypes);
		cmbDOPVisitReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDOPVisitReason.setItemCaptionPropertyId("value");

		cmbDOPVisitReason.setValue(bean.getDocumentDetails().getReasonForOPVisit());
		
		// Document Received From 
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue docRec = new SelectValue();
		docRec.setId(0L);
		docRec.setValue("Insured");
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		selectValuesList.add(docRec);
		selectValueContainer.addAll(selectValuesList);
		
		System.out.println(selectValueContainer);
		
		docReceivedFrom.setContainerDataSource(selectValueContainer);
		docReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		docReceivedFrom.setItemCaptionPropertyId("value");
		
		SelectValue docReceivedType = (SelectValue) docReceivedFrom.getContainerDataSource().getItemIds().toArray()[0];
		docReceivedFrom.setValue(docReceivedType);
		docReceivedFrom.setReadOnly(true);		
		//---------------------------------------------------
		
//		Mode Of Receipt
//		1781 In Person
//		1782 Courier
		
		BeanItemContainer<SelectValue> modeValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue mode1 = new SelectValue();
		mode1.setId(1781L);
		mode1.setValue("In Person");
		SelectValue mode2 = new SelectValue();
		mode2.setId(1782L);
		docRec.setValue("Courier");
		List<SelectValue> modeValuesList = new ArrayList<SelectValue>();
		modeValuesList.add(mode1);
		modeValuesList.add(mode2);
		modeValueContainer.addAll(modeValuesList);
		
		docModeOfReceipt.setContainerDataSource(modeValueContainer);
		docModeOfReceipt.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		docModeOfReceipt.setItemCaptionPropertyId("value");
	*/}
	
	@SuppressWarnings({ "serial", "deprecation" })
	public AbsoluteLayout addButtonLayout(){
		buttonsLayout = new HorizontalLayout();
		buttonsLayout.setEnabled(false);
		Button	queryButton = new Button("Query");
		queryButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
//				fireViewEvent(OpPresenter.PROCESS_OP_QUERY_LAYOUT, null);
			}
		});
		
		approveButton = new Button("Approve");
	
		//if(bean.getAvailableSiFlag()){
			//approveButton.setEnabled(false);
			//wizard.getNextButton().setEnabled(false);
		//}
		approveButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				updateApproveFlag();
				if(table != null){
				table.setVisible(true);
				}
				buttonsVlayout.removeAllComponents();
				buttonsVlayout.setVisible(true);
				alertForSecHbA1cCover();
				//getAmountCalTable();
				if(bean.getAvailableSiFlag()){
					buttonsVlayout.addComponents(getAmountCalTable());
					approveButton.setEnabled(false);
					wizard.getNextButton().setEnabled(false);
				}else{
					buttonsVlayout.addComponents(getAmountCalTable(),buildApproveLayout());
				}
				
			}
		});

		Button	rejectButton = new Button("Reject");
		rejectButton.addClickListener(new Button.ClickListener() 	{
			@Override
			public void buttonClick(ClickEvent event) {
				updateRejectFlag();
				buttonsVlayout.removeAllComponents();
				buttonsVlayout.setVisible(true);
				buttonsVlayout.addComponent(buildRejectLayout());
			}
		});

		buttonsLayout.addComponents(approveButton, rejectButton);
		buttonsLayout.setSpacing(true);
		buttonsLayout.setMargin(true);
//		buttonsLayout.setComponentAlignment(queryButton, Alignment.MIDDLE_RIGHT);
//		buttonsLayout.setComponentAlignment(approveButton, Alignment.MIDDLE_RIGHT);
//		buttonsLayout.setComponentAlignment(rejectButton, Alignment.MIDDLE_RIGHT);

		AbsoluteLayout submit_layout =  new AbsoluteLayout();
		submit_layout.addComponent(buttonsLayout, "left: 75%; top: 20%;");
		submit_layout.setWidth("100%");
		submit_layout.setHeight("50px");

		return submit_layout;
	}
	
	public HorizontalLayout buildApproveLayout(){
		paymentTypelayout = new HorizontalLayout();
		
		approvalAmt = new TextField("Approval Amount");
		approvalAmt.setConverter(Double.class);
		approvalAmt.setValue(bean.getPayble() != null ? String.valueOf(bean.getPayble()): "0");
		approvalAmt.setEnabled(false);
		
		

		//Label appRemLabel = new Label("Approval Remarks <b style= 'color: red'>*</b>");
		approvalRemarks = new TextArea("Approval Remarks <b style= 'color: red'>*</b>");
		approvalRemarks.setCaptionAsHtml(true);
		approvalRemarks.setRows(5);
		approvalRemarks.setColumns(16);
		approvalRemarks.setCaptionAsHtml(true);
		handleTextAreaPopup(approvalRemarks,null);
		if(bean.getApprovalRemarks() != null){
			approvalRemarks.setValue(bean.getApprovalRemarks());
		}

	//	Label payOptLabel = new Label("Payment Mode <b style= 'color: red'>*</b>");

		optPaymentMode = new OptionGroup("Payment Mode <b style= 'color: red'>*</b>");
		optPaymentMode.setCaptionAsHtml(true);
		optPaymentMode.addItems(getRadioButtonOptions());
		optPaymentMode.setItemCaption(true, "Cheque/DD");
		optPaymentMode.setItemCaption(false, "Bank Transfer");
		optPaymentMode.setStyleName("horizontal");
		optPaymentMode.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean value = (Boolean) event.getProperty().getValue();
				if(value){
					
					/*if(bean.isChqPayment() != null && !bean.isChqPayment()){
						updatePaymentFlag(false, true);
					} else {
						updatePaymentFlag(true, false);
					}*/
					updatePaymentFlag(true, false);
					paymentTypelayout.removeAllComponents();
					paymentTypelayout.addComponent(buildChequelayout());
				}else{
					updatePaymentFlag(false, true);
					paymentTypelayout.removeAllComponents();
					paymentTypelayout.addComponent(buildBanklayout());
				}
			}
		});
		//by default selecting cheque
		optPaymentMode.setValue(true);
		if(bean.isChqPayment() != null && !bean.isChqPayment()){
			optPaymentMode.setValue(false);
		}
		
		FormLayout infoForm4 = new FormLayout(approvalAmt);
		FormLayout infoForm5 = new FormLayout(approvalRemarks);

		HorizontalLayout approvallayout = new HorizontalLayout( infoForm4,  infoForm5);
		approvallayout.setSpacing(true);

		HorizontalLayout paymentlayout = new HorizontalLayout( optPaymentMode);
		paymentlayout.setSpacing(true);

		VerticalLayout containerVLayout = new VerticalLayout(approvallayout, paymentlayout, paymentTypelayout);

		HorizontalLayout containerHLayout = new HorizontalLayout(containerVLayout);

		HorizontalLayout appHolderlayout = new HorizontalLayout(containerHLayout);
		appHolderlayout.setWidth("100%");
		appHolderlayout.setComponentAlignment(containerHLayout, Alignment.MIDDLE_CENTER);

		return appHolderlayout;
	}
	
	public HorizontalLayout buildRejectLayout(){
		FormLayout rejectionForm = new FormLayout();
		rejectRemarks = new TextArea("Rejection Remarks <b style= 'color: red'>*</b>");
		rejectRemarks.setCaptionAsHtml(true);
		rejectionForm.addComponent(rejectRemarks);
		HorizontalLayout rejectlayout = new HorizontalLayout(rejectionForm);
		
		HorizontalLayout rejHolderlayout = new HorizontalLayout(rejectlayout);
		rejHolderlayout.setWidth("100%");
		rejHolderlayout.setComponentAlignment(rejectlayout, Alignment.MIDDLE_CENTER);
		
		return rejHolderlayout;
	}
	
	private Table getAmountCalTable(){
		
//		Integer opAvailableAmount = calcService.getOPAvailableAmount(bean.getDocumentDetails().getInsuredPatientName().getKey(), bean.getClaimDTO().getClaimType() != null ? bean.getClaimDTO().getClaimType().getId() : 0l);
		Map<String, Integer> amountList = calcService.getOPAvailableAmount(bean.getDocumentDetails().getInsuredPatientName().getKey(), bean.getClaimDTO().getKey(), bean.getClaimDTO().getClaimType() != null ? bean.getClaimDTO().getClaimType().getId() : 0l,
				bean.getDocumentDetails().getConsultationType() != null && bean.getDocumentDetails().getConsultationType().getCommonValue() != null ? bean.getDocumentDetails().getConsultationType().getCommonValue():"0");
		Integer opAvailableAmount = 0;
		Integer perClaimLimit = 0;
		Integer perPolicyLimit = 0;
		
		if(amountList != null && !amountList.isEmpty()){
			opAvailableAmount = amountList.get(SHAConstants.CURRENT_BALANCE_SI);
			perClaimLimit = amountList.get(SHAConstants.OP_CLAIM_LIMIT);
			perPolicyLimit = amountList.get(SHAConstants.OP_POLICY_LIMIT);
		}
		bean.setReportOPAvailableSI(opAvailableAmount);
		if(opAvailableAmount!= null && opAvailableAmount > 0){
			bean.setAvailableSiFlag(false);
		}
		bean.setPerClaimLimit(Double.valueOf(perClaimLimit.toString()));
		bean.setPerPolicyLimit(Double.valueOf(perPolicyLimit.toString()));
		if(bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER)
				|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)){
			bean.setAvailablevaccinationlimit(Double.valueOf(perPolicyLimit.toString()));			
		}
		if(bean.getPolicyDto().getProduct().getKey() != null &&
				!bean.getPolicyDto().getProduct().getKey().equals(601l)){
			bean.getDocumentDetails().setBalanceSI(Double.valueOf(perPolicyLimit.toString()));
		}
		
		if(opAvailableAmount ==0 && bean != null && bean.getPolicyDto() != null && bean.getPolicyDto().getProduct() != null && bean.getPolicyDto().getProduct().getKey() != null &&
				(bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER)
						|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL))
						&& bean != null && bean.getDocumentDetails() != null && bean.getDocumentDetails().getConsultationType() != null && 
								bean.getDocumentDetails().getConsultationType().getId().equals(ReferenceTable.OUT_PATIENT_VACCINATION_BENEFIT)){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox("Available Vaccination Benefit is also Zero and <br> hence claim can't be approved", buttonsNamewithType);
		}
		else if(opAvailableAmount ==0){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox("Available OP SI is Zero and <br> hence claim can't be approved", buttonsNamewithType);
		}
		
		if(bean.getPolicyDto().getProduct().getKey() != null &&
				!bean.getPolicyDto().getProduct().getKey().equals(601l)
				&& perClaimLimit != null && perClaimLimit == 0
				&& perPolicyLimit != null && perPolicyLimit == 0){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox("Policy and Claim limit not available so<br>case is zero approval and case will be a<br>rejected", buttonsNamewithType);
			//GalaxyAlertBox.createErrorBox("Available OP SI is Zero and <br> hence claim can't be approved", buttonsNamewithType);
		}
		
		/*Integer amt = new Integer(0);
		Integer payableamt = new Integer(0);
		Integer nonPayable = new Integer(0);
		Integer deductions = new Integer(0);
		Integer billAmt = new Integer(0);*/
		
		Double amt = 0d;
		Double payableamt = 0d;
		Double nonPayable = 0d;
		Double deductions = 0d;
		Double billAmt = 0d;
		
		List<UploadDocumentDTO> uploadDocsList = uploadedDocsTable.getValues();
		if(uploadDocsList != null && !uploadDocsList.isEmpty()){
			for (UploadDocumentDTO billDetailsDTO : uploadDocsList) {
				payableamt += billDetailsDTO.getBillValue() != null ? billDetailsDTO.getBillValue() : 0d;
				nonPayable += billDetailsDTO.getNonPaybleAmt() != null ? billDetailsDTO.getNonPaybleAmt() : 0d;
				deductions += billDetailsDTO.getDeductibleAmt() != null ? billDetailsDTO.getDeductibleAmt() : 0d;
				billAmt += billDetailsDTO.getBillAmt() != null ? billDetailsDTO.getBillAmt().intValue() : 0d;
			}
		}
		
		Double min = 0d;
		Double minclaimLimit = 0d;
		Double minSi = 0d;
		if(perClaimLimit != null &&  perClaimLimit > 0){
			minclaimLimit = Math.min(Double.valueOf(perClaimLimit.toString()), opAvailableAmount);
			min = Math.min(payableamt,minclaimLimit);
		} else if(perPolicyLimit != null && perPolicyLimit > 0){
			minclaimLimit = Math.min(Double.valueOf(perPolicyLimit.toString()), opAvailableAmount);
			min = Math.min(payableamt,minclaimLimit);
		} else {
			min= Math.min(payableamt, opAvailableAmount);
		}
		
		Double totalDeductions = deductions + nonPayable;
		
		bean.setAvailableSI(opAvailableAmount.toString());
		bean.setPayble(min);
		bean.setEligible(payableamt.toString());
		bean.getDocumentDetails().setDeductions(totalDeductions);
		bean.getDocumentDetails().setTotalBillAmount(billAmt);
		
		table = new Table();
		table.setWidth("100%");
		table.addContainerProperty("amountEligible", Double.class, null);
		table.addContainerProperty("perClaimLimit",  Double.class, null);
		table.addContainerProperty("perPolicyLimit",  Double.class, null);
		if(bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER)
						|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)){
			table.addContainerProperty("availablevaccinationlimit",  Double.class, null);
		}else{
			table.addContainerProperty("availableopSI",  Double.class, null);
		}
		
		table.addContainerProperty("amountPayable",  Double.class, null);
		
		int i=0;
			if(bean.getAvailableSI() != null){
				if(bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER)
						|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)){
					table.addItem(new Object[]{payableamt,Double.valueOf(perClaimLimit.toString()),Double.valueOf(perPolicyLimit.toString()),Double.valueOf(opAvailableAmount.toString()),min}, i+1);
				}else{
					table.addItem(new Object[]{payableamt,Double.valueOf(perClaimLimit.toString()),Double.valueOf(perPolicyLimit.toString()), Double.valueOf(opAvailableAmount.toString()),min}, i+1);
				}
				
				i++;
			}
		
		table.setPageLength(5);
		table.setColumnHeader("amountEligible", "Amount Eligible");
		table.setColumnHeader("perClaimLimit", "Visit Limit");
		table.setColumnHeader("perPolicyLimit", "Policy Limit");
		if(bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER)
				|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)){
			table.setColumnHeader("availablevaccinationlimit", "Available Vaccination Limit");
		}else{
			table.setColumnHeader("availableopSI", "Available OP SI");			
		}
		table.setColumnHeader("amountPayable", "Amount Payable");
		table.setColumnWidth("amountEligible", 165);
		table.setColumnWidth("perClaimLimit", 180);
		table.setColumnWidth("perPolicyLimit", 180);
		if(bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER)
				|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL)){
			table.setColumnWidth("availablevaccinationlimit", 180);
		}else{
			table.setColumnWidth("availableopSI", 170);
		}
		
		table.setColumnWidth("amountPayable", 170);
		table.setWidth("890px");
		table.setHeight("62px");

		return table;
	}
	
	protected Collection<Boolean> getRadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	public HorizontalLayout buildChequelayout(){
		chqmodeChngReason = new TextField("Reason for change (Pay Mode)");
		chqmodeChngReason.setCaptionAsHtml(true);
		CSValidator paymentModeValidator = new CSValidator();
		paymentModeValidator.extend(chqmodeChngReason);
		paymentModeValidator.setRegExp("^[a-zA-Z . ]*$");
		paymentModeValidator.setPreventInvalidTyping(true);
		chqmodeChngReason.setValue((bean.getChqModeChngReason() == null)?"":bean.getChqModeChngReason());
		
		chqEmailId = new TextField("Email ID <b style= 'color: red'>*</b>");
		chqEmailId.setCaptionAsHtml(true);
		chqEmailId.setValue((bean.getChqEmailId() == null)?"":bean.getChqEmailId());
		CSValidator emailIdTxtValidator = new CSValidator();
		emailIdTxtValidator.extend(chqEmailId);
		emailIdTxtValidator.setRegExp("^[a-z A-Z 0-9 @ . _]*$");
		emailIdTxtValidator.setPreventInvalidTyping(true);
		
		chqPanno = new TextField("PAN No");
		chqPanno.setCaptionAsHtml(true);
		chqPanno.setMaxLength(10);
		CSValidator panValidator = new CSValidator();
		panValidator.extend(chqPanno);
		panValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		panValidator.setPreventInvalidTyping(true);
		chqPanno.setValue((bean.getChqPanno() == null)?"":bean.getChqPanno());
		
		chqPayableAt = new TextField();
		chqPayableAt.setCaptionAsHtml(true);
		chqPayableAt.setValue((bean.getChqPayableAt() == null)?"":bean.getChqPayableAt());
		CSValidator doctorNameValidator = new CSValidator();
		doctorNameValidator.extend(chqPayableAt);
		doctorNameValidator.setRegExp("^[a-zA-Z . ]*$");
		doctorNameValidator.setPreventInvalidTyping(true);
		
		btnCitySearch = new Button();
		
		btnCitySearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnCitySearch.setIcon(new ThemeResource("images/search.png"));
		
		btnCitySearch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				citySearchCriteriaWindow.setPresenterString(SHAConstants.OP_PROCESS_SCREEN);
				citySearchCriteriaWindow.initView(popup);
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setContent(citySearchCriteriaWindow);
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
				btnCitySearch.setEnabled(true);
			}
		});
		
		HorizontalLayout payableCity = new HorizontalLayout(chqPayableAt,btnCitySearch);
		payableCity.setCaptionAsHtml(true);
		payableCity.setSpacing(true);
		payableCity.setCaption("Payable at <b style= 'color: red'></b>");
		
		
		FormLayout left = new FormLayout(chqmodeChngReason, chqEmailId, chqPanno, payableCity);
		
		chqPayeeName = new ComboBox("Payee Name <b style= 'color: red'>*</b>");
		chqPayeeName.setCaptionAsHtml(true);
		BeanItemContainer<Insured> insuredList = insuredService.getCLSInsuredList(bean.getPolicyDto().getPolicyNumber());
		chqPayeeName.setNullSelectionAllowed(false);
		chqPayeeName.setContainerDataSource(insuredList);
		chqPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		chqPayeeName.setItemCaptionPropertyId("insuredName");
		chqPayeeName.setValue(bean.getNewIntimationDTO().getInsuredPatient());
		bean.setChqPayeeName(bean.getNewIntimationDTO().getInsuredPatient());
		
		chqPayeeName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6251822616467768479L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Insured Insval = (Insured) event.getProperty().getValue();
				if(Insval != null){
					bean.setChqPayeeName(Insval);
				}
			}
		});
		
		chqNameChngReason = new TextField();
		chqNameChngReason.setCaption("Reason For Change (Payee Name)");
		chqNameChngReason.setCaptionAsHtml(true);
		CSValidator chequeNameValidator = new CSValidator();
		chequeNameValidator.extend(chqNameChngReason);
		chequeNameValidator.setRegExp("^[a-zA-Z . ]*$");
		chequeNameValidator.setPreventInvalidTyping(true);
		if(chqNameChngReason.getValue() != null && !chqNameChngReason.getValue().toString().isEmpty() 
				&& !chqNameChngReason.getValue().toString().equals(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName())){
			chqNameChngReason.setCaption("Reason For Change (Payee Name) <b style= 'color: red'>*</b>");
			chqNameChngReason.setCaptionAsHtml(true);
		}
		chqNameChngReason.setValue((bean.getChqNameChngReason() == null)?"":bean.getChqNameChngReason());
		
//		chqHeirName = new TextField("Legal Heir Name <b style= 'color: red'>*</b>");
//		chqHeirName.setCaptionAsHtml(true);
//		CSValidator heairName = new CSValidator();
//		heairName.extend(chqHeirName);
//		heairName.setRegExp("^[a-zA-Z . ]*$");
//		heairName.setPreventInvalidTyping(true);
//		chqHeirName.setValue((bean.getChqHeirName() == null)?"":bean.getChqHeirName());
//		CSValidator legalHeairname = new CSValidator();
//		legalHeairname.extend(chqNameChngReason);
//		legalHeairname.setRegExp("^[a-zA-Z . ]*$");
//		legalHeairname.setPreventInvalidTyping(true);
		
		FormLayout right = new FormLayout(chqPayeeName, chqNameChngReason);
		
		btnClearAll = new Button("Clear All");
		btnClearAll.addStyleName(BaseTheme.BUTTON_LINK);
		
		btnClearAll.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(null != chqmodeChngReason)
				{
					chqmodeChngReason.setReadOnly(false);
					chqmodeChngReason.setEnabled(true);
					chqmodeChngReason.setValue("");
				}
				if(null != chqEmailId)
				{
					chqEmailId.setReadOnly(false);
					chqEmailId.setEnabled(true);
					chqEmailId.setValue("");
				}
				if(null != chqPanno)
				{
					chqPanno.setReadOnly(false);
					chqPanno.setEnabled(true);
					chqPanno.setValue("");
				}
				if(null != chqPayableAt)
				{
					chqPayableAt.setReadOnly(false);
					chqPayableAt.setEnabled(true);
					chqPayableAt.setValue("");
				}
				if(null != chqNameChngReason)
				{
					chqNameChngReason.setReadOnly(false);
					chqNameChngReason.setEnabled(true);
					chqNameChngReason.setValue("");
				}
//				if(chqHeirName != null){
//					chqHeirName.setReadOnly(false);
//					chqHeirName.setEnabled(true);
//					chqHeirName.setValue("");
//				}

			}
		});
		
		HorizontalLayout clearlayout = new HorizontalLayout(btnClearAll);
		clearlayout.setSpacing(true);
		
		HorizontalLayout returnlayout = new HorizontalLayout(left, right);
		returnlayout.setSpacing(true);
		
		VerticalLayout holderLay = new VerticalLayout();
		holderLay.addComponent(clearlayout);
		holderLay.addComponent(returnlayout);
		holderLay.setComponentAlignment(clearlayout, Alignment.MIDDLE_RIGHT);
		
		HorizontalLayout finallayout = new HorizontalLayout(holderLay);
		
		return finallayout;
		
	}
	
	public HorizontalLayout buildBanklayout(){
		VerticalLayout holderLay = new VerticalLayout();

		Button btnPopulatePreviousAccntDetails = new Button("Use account details from policy/previous claim");
		btnPopulatePreviousAccntDetails.addStyleName(BaseTheme.BUTTON_LINK);

		btnPopulatePreviousAccntDetails.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {	
				
				btnOk = new Button("OK");
				btnOk.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				btnOk.setWidth("-1px");
				btnOk.setHeight("-10px");
				//Vaadin8-setImmediate() btnOk.setImmediate(true);
				
				btnCancel = new Button("CANCEL");
				btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
				btnCancel.setWidth("-1px");
				btnCancel.setHeight("-10px");
				//Vaadin8-setImmediate() btnCancel.setImmediate(true);
				
				btnOk.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						String err = previousAccountDetailsTable.isValidate();
						if("" == err){								
							buildDialogBox("Selected Data will be populated in payment details section. Please click OK to proceeed",populatePreviousWindowPopup,SHAConstants.BTN_OK);
						}
					}
				});
			
			btnCancel.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void buttonClick(ClickEvent event) {
					populatePreviousWindowPopup.close();
//					buildDialogBox("Are you sure you want to cancel",populatePreviousWindowPopup,SHAConstants.BTN_CANCEL);
				}
			});
			
				
				previousAccntDetailsButtonLayout = new HorizontalLayout(btnOk,btnCancel);
				
				previousAccountDetailsTable.resetTableDataList();
				populatePreviousWindowPopup = new com.vaadin.ui.Window();
				populatePreviousWindowPopup.setWidth("75%");
				populatePreviousWindowPopup.setHeight("90%");

				previousAccountDetailsTable.init("Previous Account Details", false, false);
				previousAccountDetailsTable.setPresenterString(SHAConstants.OP_IFSC);
				previousPaymentVerticalLayout = new VerticalLayout();
				previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
				populatePreviousWindowPopup.setContent(previousPaymentVerticalLayout);	
				previousPaymentVerticalLayout.addComponent(previousAccntDetailsButtonLayout);
				previousPaymentVerticalLayout.setComponentAlignment(previousAccntDetailsButtonLayout, Alignment.TOP_CENTER);				


				setPreviousAccountDetailsValues();
				populatePreviousWindowPopup.setClosable(true);
				populatePreviousWindowPopup.center();
				populatePreviousWindowPopup.setResizable(true);

				populatePreviousWindowPopup.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				populatePreviousWindowPopup.setModal(true);
				populatePreviousWindowPopup.setClosable(false);

				UI.getCurrent().addWindow(populatePreviousWindowPopup);
			}
		});

		btnClearAll = new Button("Clear All");
		btnClearAll.addStyleName(BaseTheme.BUTTON_LINK);
		btnClearAll.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(null != bnkAccNo)
				{
					bnkAccNo.setReadOnly(false);
					bnkAccNo.setEnabled(true);
					bnkAccNo.setValue("");
				}
				if(null != bnkIfsc)
				{
					bnkIfsc.setReadOnly(false);
					bnkIfsc.setEnabled(true);
					bnkIfsc.setValue("");
				}
				if(null != bnkName)
				{
					bnkName.setReadOnly(false);
					bnkName.setEnabled(true);
					bnkName.setValue("");
				}
				if(null != bnkCity)
				{
					bnkCity.setReadOnly(false);
					bnkCity.setEnabled(true);
					bnkCity.setValue("");
				}
				if(null != bnkBranch)
				{
					bnkBranch.setReadOnly(false);
					bnkBranch.setEnabled(true);
					bnkBranch.setValue("");
				}
//				if(bnkHeirName != null){
//					bnkHeirName.setReadOnly(false);
//					bnkHeirName.setEnabled(true);
//					bnkHeirName.setValue("");
//				}
				
				if(bnkNameChngReason != null){
					bnkNameChngReason.setReadOnly(false);
					bnkNameChngReason.setEnabled(true);
					bnkNameChngReason.setValue("");
				}
				if(bnkmodeChngReason != null){
					bnkmodeChngReason.setReadOnly(false);
					bnkmodeChngReason.setEnabled(true);
					bnkmodeChngReason.setValue("");
				}
				if(null != bnkPanno)
				{
					bnkPanno.setReadOnly(false);
					bnkPanno.setEnabled(true);
					bnkPanno.setValue("");
				}
				
				if(null != bnkEmailId)
				{
					bnkEmailId.setReadOnly(false);
					bnkEmailId.setEnabled(true);
					bnkEmailId.setValue("");
				}

			}
		});

		HorizontalLayout clearlayout = new HorizontalLayout(btnPopulatePreviousAccntDetails, btnClearAll);
		clearlayout.setSpacing(true);

		bnkmodeChngReason = new TextField("Reason for change (Pay Mode)<b style= 'color: red'>*</b>");
		bnkmodeChngReason.setCaptionAsHtml(true);
		bnkmodeChngReason.setValue((bean.getBnkmodeChngReason() == null)?"":bean.getBnkmodeChngReason());
		
		bnkEmailId = new TextField("Email ID <b style= 'color: red'>*</b>");
		bnkEmailId.setCaptionAsHtml(true);
		CSValidator emailIdTxtValidator = new CSValidator();
		emailIdTxtValidator.extend(bnkEmailId);
		emailIdTxtValidator.setRegExp("^[a-z A-Z 0-9 @ . _]*$");
		emailIdTxtValidator.setPreventInvalidTyping(true);
		bnkEmailId.setValue((bean.getBnkEmailId() == null)?"":bean.getBnkEmailId());
		
		bnkPanno = new TextField("PAN No");
		bnkPanno.setCaptionAsHtml(true);
		bnkPanno.setMaxLength(10);
		CSValidator panValidator = new CSValidator();
		panValidator.extend(bnkPanno);
		panValidator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
		panValidator.setPreventInvalidTyping(true);

		bnkPanno.setValue((bean.getBnkPanno() == null)?"":bean.getBnkPanno());
		
		bnkAccNo = new TextField("Account No <b style= 'color: red'>*</b>");
		bnkAccNo.setCaptionAsHtml(true);
		bnkAccNo.setMaxLength(15);
		bnkAccNo.setValue((bean.getBnkAccNo() == null)?"":bean.getBnkAccNo());
				
		bnkIfsc = new TextField();
		bnkIfsc.setCaptionAsHtml(true);
		bnkIfsc.setValue((bean.getBnkIfsc() == null)?"":bean.getBnkIfsc());
		
		bnkName = new TextField("Bank Name <b style= 'color: red'>*</b>"); // No Need to save
		bnkName.setCaptionAsHtml(true);
		if(bean.getBnkName() != null){
			bnkName.setValue(bean.getBnkName());
		}
		bnkCity = new TextField("City <b style= 'color: red'>*</b>"); // No Need to save
		bnkCity.setCaptionAsHtml(true);
		if(bean.getBnkCity() != null){
			bnkCity.setValue(bean.getBnkCity());
		}
//		FormLayout left = new FormLayout(bnkmodeChngReason, bnkEmailId, bnkPanno, bnkAccNo, bnkIfsc, bnkName, bnkCity);

		bnkPayeeName = new ComboBox("Payee Name <b style= 'color: red'>*</b>");
		bnkPayeeName.setCaptionAsHtml(true);
		BeanItemContainer<Insured> insuredList = insuredService.getCLSInsuredList(bean.getPolicyDto().getPolicyNumber());
		bnkPayeeName.setContainerDataSource(insuredList);
		bnkPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		bnkPayeeName.setItemCaptionPropertyId("insuredName");
		bnkPayeeName.setNullSelectionAllowed(false);
		bnkPayeeName.setValue(bean.getNewIntimationDTO().getInsuredPatient());
		bean.setBnkPayeeName(bean.getNewIntimationDTO().getInsuredPatient());
		
		bnkPayeeName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 6251822616467768479L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Insured Insval = (Insured) event.getProperty().getValue();
				if(Insval != null){
					bean.setBnkPayeeName(Insval);
				}else{
					bean.setBnkPayeeName(null);
				}
			}
		});

		bnkNameChngReason = new TextField();
		bnkNameChngReason.setCaption("Reason For Change (Payee Name)");
		bnkNameChngReason.setCaptionAsHtml(true);
		CSValidator bankNameChng = new CSValidator();
		bankNameChng.extend(bnkNameChngReason);
		bankNameChng.setRegExp("^[a-zA-Z . ]*$");
		bankNameChng.setPreventInvalidTyping(true);
		if(bnkPayeeName.getValue() != null && !bnkPayeeName.getValue().toString().equals(bean.getNewIntimationDTO().getInsuredPatient().getInsuredName())){
			bnkNameChngReason.setCaption("Reason For Change (Payee Name) <b style= 'color: red'>*</b>");
			bnkNameChngReason.setCaptionAsHtml(true);
		}
		bnkNameChngReason.setValue((bean.getBnkNameChngReason() == null)?"":bean.getBnkNameChngReason());
		
//		bnkHeirName = new TextField("Legal Heir Name <b style= 'color: red'>*</b>");
//		bnkHeirName.setCaptionAsHtml(true);
//		bnkHeirName.setValue((bean.getBnkHeirName() == null)?"":bean.getBnkHeirName());
		
		bnkBranch = new TextField("Branch <b style= 'color: red'>*</b>"); // No Need to save
		bnkBranch.setCaptionAsHtml(true);
		if(bean.getBnkBranch() != null){
			bnkBranch.setValue(bean.getBnkBranch());
		}

		btnIFCSSearch = new Button();
		btnIFCSSearch.setDescription("Pick IFSC Code");
		btnIFCSSearch.setStyleName(ValoTheme.BUTTON_LINK);
		btnIFCSSearch.setIcon(new ThemeResource("images/search.png"));

		btnIFCSSearch.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5797933806585865425L;
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new com.vaadin.ui.Window();
				viewSearchCriteriaWindow.setWindowObject(popup);
				viewSearchCriteriaWindow.setPresenterString(SHAConstants.OP_IFSC);
				viewSearchCriteriaWindow.initView();
				popup.setWidth("75%");
				popup.setHeight("90%");
				popup.setContent(viewSearchCriteriaWindow);
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
		});
		HorizontalLayout ifsc = new HorizontalLayout(bnkIfsc,btnIFCSSearch);
		ifsc.setCaptionAsHtml(true);
		ifsc.setSpacing(true);
		ifsc.setCaption("IFSC Code <b style= 'color: red'>*</b>");
		FormLayout left = new FormLayout(bnkmodeChngReason, bnkEmailId, bnkPanno, bnkAccNo,ifsc, bnkName, bnkCity);
		FormLayout rightUpper = new FormLayout(bnkPayeeName, bnkNameChngReason, bnkBranch);
		rightUpper.setMargin(false);
		FormLayout right = new FormLayout(rightUpper);

		HorizontalLayout supportlayout = new HorizontalLayout(left, right);

		holderLay.addComponent(clearlayout);
		holderLay.addComponent(supportlayout);
		holderLay.setComponentAlignment(clearlayout, Alignment.MIDDLE_RIGHT);


		HorizontalLayout returnlayout = new HorizontalLayout(holderLay);

		return returnlayout;
	}
	
	private void buildDialogBox(String message,final Window populatePreviousWindowPopup,String btnName)
	{
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
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
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);

		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);

		getUI().getCurrent().addWindow(dialog);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != populatePreviousWindowPopup)
					populatePreviousWindowPopup.close();
			}
		});
		if(null != cancelBtn){
			cancelBtn.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});
		}
	}
	
	private void updateApproveFlag(){
		bean.setApprove(true);
		bean.setReject(false);
		//wizard.getNextButton().setEnabled(true);
	}
	
	private void updateRejectFlag(){
		bean.setApprove(false);
		bean.setReject(true);
		wizard.getNextButton().setEnabled(false);
		wizard.getFinishButton().setEnabled(true);
	}
	
	
	private void updatePaymentFlag(boolean isChq, boolean isBank){
		if(isChq){
			bean.setChqPayment(true);
		}else{
			bean.setChqPayment(false);
		}
	}
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));
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
	
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

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
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Comments";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
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
	
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		bnkIfsc.setReadOnly(false);
		bnkIfsc.setValue(dto.getIfscCode());
		bnkIfsc.setReadOnly(true);
		
		bnkName.setReadOnly(false);
		bnkName.setValue(dto.getBankName());
		bnkName.setReadOnly(true);
		
		bnkBranch.setReadOnly(false);
		bnkBranch.setValue(dto.getBranchName());
		bnkBranch.setReadOnly(true);
		
		bnkCity.setReadOnly(false);
		bnkCity.setValue(dto.getCity());
		bnkCity.setReadOnly(true);
	}
	
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
	
		/*if(null != bnkEmailId)
		{
			bnkEmailId.setReadOnly(false);
			bnkEmailId.setEnabled(true);
			bnkEmailId.setValue(tableDTO.getEmailId());
			bnkEmailId.setNullRepresentation("");
			//txtEmailId.setReadOnly(true);
			}*/
		/*if(null != bnkPanno)
		{
			bnkPanno.setReadOnly(false);
			bnkPanno.setEnabled(true);
			bnkPanno.setValue(tableDTO.getPanNo());
			bnkPanno.setNullRepresentation("");
			//txtPanNo.setReadOnly(true);
		}*/
		if(null != bnkAccNo)
		{
			bnkAccNo.setReadOnly(false);
			bnkAccNo.setEnabled(true);

			bnkAccNo.setValue(tableDTO.getBankAccountNo());
			bnkAccNo.setNullRepresentation("");
			bnkAccNo.setReadOnly(true);
		}
		if(null != bnkIfsc)
		{
			bnkIfsc.setReadOnly(false);
			bnkIfsc.setEnabled(true);

			bnkIfsc.setValue(tableDTO.getIfsccode());
			bnkIfsc.setNullRepresentation("");
			bnkIfsc.setReadOnly(true);
		}
		if(null != bnkName)
		{
			bnkName.setReadOnly(false);
			bnkName.setEnabled(true);

			bnkName.setValue(tableDTO.getBankName());
			bnkName.setNullRepresentation("");
			bnkName.setReadOnly(true);
		}
		if(null != bnkCity)
		{
			bnkCity.setReadOnly(false);
			bnkCity.setEnabled(true);
			bnkCity.setValue(tableDTO.getBankCity());
			bnkCity.setNullRepresentation("");
			bnkCity.setReadOnly(true);
		}
		if(null != bnkBranch)
		{
			bnkBranch.setReadOnly(false);
			bnkBranch.setEnabled(true);
			bnkBranch.setValue(tableDTO.getBankBranch());
			bnkBranch.setNullRepresentation("");
			bnkBranch.setReadOnly(true);
		}

	}
	
	public void setUpPayableDetails(String payableAt) {	
		chqPayableAt.setReadOnly(false);
		chqPayableAt.setValue(payableAt);
		chqPayableAt.setReadOnly(true);
	}
	
	public void setPreviousAccountDetailsValues() {
		if(null != previousAccountDetailsTable)	{
			int rowCount = 1;
			List<PreviousAccountDetailsDTO> previousListTable = this.bean.getPreviousAccntDetailsList();
			if(null != previousListTable && !previousListTable.isEmpty()){				
				for (PreviousAccountDetailsDTO previousAccountDetailsDTO : previousListTable) {
					previousAccountDetailsDTO.setChkSelect(false);
					previousAccountDetailsDTO.setChkSelect(null);						
					previousAccountDetailsDTO.setSerialNo(rowCount);
					previousAccountDetailsTable.addBeanToList(previousAccountDetailsDTO);
					rowCount ++ ;
				}
			}
		}
	}

	public void addListeners(){

		/*btnClearAll.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
					bnkEmailId.setNullRepresentation("");
					bnkPanno.setNullRepresentation("");
					bnkAccNo.setNullRepresentation("");
					bnkIfsc.setNullRepresentation("");
					bnkName.setNullRepresentation("");
					bnkCity.setNullRepresentation("");
					bnkBranch.setNullRepresentation("");
					bnkHeirName.setNullRepresentation("");
					bnkNameChngReason.setNullRepresentation("");
					//txtEmailId.setReadOnly(true);
			}
		});*/
	}


	public boolean validatePage() {
		Boolean hasError = false;
		errMsg.setLength(0);
		/*if(uploadedDocsTable.getValues() != null && uploadedDocsTable.getValues().size() == 0){
			hasError = true;
			errMsg.append("Please provide Bill Details </br>");
		}*/
		
		/*if(uploadedDocsTable.getValues() != null && uploadedDocsTable.getValues().size() > 0){
			uploadedDocsTable.validateFields();
			String errorMsg = uploadedDocsTable.getErrMsg().toString();
			if(!StringUtils.isBlank(errorMsg)){
				hasError = true;
				errMsg.append(errorMsg);
			}
		}*/
		
		/*if(opBillDetailsTableObj.getValues() != null && opBillDetailsTableObj.getValues().size() == 0){
			hasError = true;
			errMsg.append("Please provide Bill Details </br>");
		}*/
		
		/*if(opBillDetailsTableObj.getValues() != null && opBillDetailsTableObj.getValues().size() > 0){
			opBillDetailsTableObj.validateFields();
			String errorMsg = opBillDetailsTableObj.getErrMsg().toString();
			if(!StringUtils.isBlank(errorMsg)){
				hasError = true;
				errMsg.append(errorMsg);
			}
		}*/
		
		if(!bean.isApprove() && !bean.isReject()){
			hasError = true;
			errMsg.append("Please approve or reject the claim </br>");
		}
		/*if(bean.getAvailableSiFlag() && !bean.isReject()){
			hasError = true;
			errMsg.append("Available SI is Zero</br>");
		}*/
		if(bean.isApprove() && !bean.getAvailableSiFlag()){
			if(bean.isChqPayment() != null){
				if(bean.isChqPayment()){
					if(bean.isChqPayment() && chqPayeeName != null && (chqPayeeName.getValue() == null || StringUtils.isBlank(chqPayeeName.getValue().toString()))){
						hasError = true;
						errMsg.append("Please select the payee name </br>");
					}

					/*if(bean.isChqPayment() && chqmodeChngReason != null && (chqmodeChngReason.getValue() == null|| StringUtils.isBlank(chqPayeeName.getValue().toString()))){
						hasError = true;
						errMsg.append("Please enter Reason for change (Pay Mode) </br>");
					}*/
					
					if(bean.isChqPayment() && chqEmailId != null && (chqEmailId.getValue() == null|| StringUtils.isBlank(chqEmailId.getValue().toString()))){
						if(!isValidEmail(this.chqEmailId.getValue()))
						{
						hasError = true;
						errMsg.append("Please enter Email ID </br>");
						}
					}
					
					if(bean.isChqPayment() && chqEmailId != null && (chqEmailId.getValue().toString() != null && !StringUtils.isBlank(chqEmailId.getValue().toString()))){
						if(!isValidEmail(this.chqEmailId.getValue()))
						{
						hasError = true;
						errMsg.append("Please enter a valid Email ID </br>");
						}
					}
					
					/*if(bean.isChqPayment() && chqPanno != null && (chqPanno.getValue() == null|| StringUtils.isBlank(chqPanno.getValue().toString()))){
						hasError = true;
						errMsg.append("Please enter PAN No </br>");
					}*/
					
					/*if(bean.isChqPayment() && chqPayableAt != null && (chqPayableAt.getValue() == null|| StringUtils.isBlank(chqPayableAt.getValue().toString()))){
						hasError = true;
						errMsg.append("Please enter Payable at </br>");
					}*/
					
					if(bean.isChqPayment() && chqNameChngReason != null && (chqNameChngReason.getValue() == null|| StringUtils.isBlank(chqNameChngReason.getValue().toString()))){
						if(bean.getNewIntimationDTO().getInsuredPatientName() != null 
								&& !bean.getNewIntimationDTO().getInsuredPatientName().equalsIgnoreCase(chqPayeeName.getValue().toString())){
							hasError = true;
							errMsg.append("Please enter Reason For Change (Payee Name) </br>");
						}
					}
					//commented by noufel as per raja request
//					if(bean.isChqPayment() && chqHeirName != null && (chqHeirName.getValue() == null|| StringUtils.isBlank(chqHeirName.getValue().toString()))){
//						hasError = true;
//						errMsg.append("Please enter Legal Heir Name </br>");
//					}
					
				}else if(!bean.isChqPayment()){
					if(!bean.isChqPayment() && bnkPayeeName != null && (bnkPayeeName.getValue() == null|| StringUtils.isBlank(bnkPayeeName.getValue().toString()))){
						hasError = true;
						errMsg.append("Please select the payee name </br>");
					}

					/*if(!bean.isChqPayment() && bnkmodeChngReason != null && StringUtils.isBlank(bnkmodeChngReason.getValue())){
						hasError = true;
						errMsg.append("Please enter Reason for change (Pay Mode) </br>");
					}*/
					
					if(!bean.isChqPayment() && bnkEmailId != null && StringUtils.isBlank(bnkEmailId.getValue())){
						if(!isValidEmail(this.bnkEmailId.getValue()))
						{
							hasError = true;
							errMsg.append("Please enter Email ID </br>");
						}
					}
					
					if(!bean.isChqPayment() && bnkEmailId != null && !StringUtils.isBlank(bnkEmailId.getValue())){
						if(!isValidEmail(this.bnkEmailId.getValue()))
						{
							hasError = true;
							errMsg.append("Please enter Valid Email ID </br>");
						}
					}
					
					/*if(!bean.isChqPayment() && bnkPanno != null && StringUtils.isBlank(bnkPanno.getValue())){
						hasError = true;
						errMsg.append("Please enter PAN No </br>");
					}*/

					if(!bean.isChqPayment() && bnkAccNo != null && StringUtils.isBlank(bnkAccNo.getValue())){
						hasError = true;
						errMsg.append("Please fill the account number </br>");
					}
					
					if(!bean.isChqPayment() && bnkIfsc != null && StringUtils.isBlank(bnkIfsc.getValue())){
						hasError = true;
						errMsg.append("Please pick the Ifsc code </br>");
					}
					
					if(!bean.isChqPayment() && bnkName != null && StringUtils.isBlank(bnkName.getValue())){
						hasError = true;
						errMsg.append("Please pick the Ifsc code  or enter Bank Name manually. </br>");
					}
					
					if(!bean.isChqPayment() && bnkmodeChngReason != null && StringUtils.isBlank(bnkmodeChngReason.getValue())){
						hasError = true;
						errMsg.append("Please enter Reason For Change (Pay Mode). </br>");
					}
					
					if(!bean.isChqPayment() && bnkCity != null && StringUtils.isBlank(bnkCity.getValue())){
						hasError = true;
						errMsg.append("Please pick the Ifsc code  or enter City manually. </br>");
					}
					
					if(!bean.isChqPayment() && bnkNameChngReason != null && StringUtils.isBlank(bnkNameChngReason.getValue())){
						if(bean.getNewIntimationDTO().getInsuredPatientName() != null 
									&& !bean.getNewIntimationDTO().getInsuredPatientName().equalsIgnoreCase(bnkPayeeName.getValue().toString())){
							hasError = true;
							errMsg.append("Please enter Reason For Change (Payee Name) </br>");
							}
					}
					
					//commented by noufel 
//					if(!bean.isChqPayment() && bnkHeirName != null && StringUtils.isBlank(bnkHeirName.getValue())){
//						hasError = true;
//						errMsg.append("Please enter Legal Heir Name </br>");
//					}
					
					if(!bean.isChqPayment() && bnkBranch != null && StringUtils.isBlank(bnkBranch.getValue())){
						hasError = true;
						errMsg.append("Please pick the Ifsc code  or enter Branch manually.</br>");
					}
				}
			}else{
				hasError = true;
				errMsg.append("Please select Payment Mode </br>");
			}
		}
		
		if(bean.isReject()){
			if(rejectRemarks != null && StringUtils.isBlank(rejectRemarks.getValue())){
				hasError = true;
				errMsg.append("Please fill the rejection remarks </br>");
			}
		}
		
		if(bean.isApprove()&& !bean.getAvailableSiFlag()){
			if(approvalRemarks != null && StringUtils.isBlank(approvalRemarks.getValue())){
				hasError = true;
				errMsg.append("Please fill the approval remarks </br>");
			}
		}

		if(!hasError){
			bean.setUploadedDocsTableList(uploadedDocsTable.getValues());
			bean.setUploadedDeletedDocsTableList(uploadedDocsTable.getDeletedDocumentList());
//			bean.setOpBillDetailsList(opBillDetailsTableObj.getValues());

			if(bean.isApprove() && !bean.getAvailableSiFlag()){
				if(bean.isChqPayment()){
					bean.setChqModeChngReason(chqmodeChngReason.getValue());
					bean.setChqEmailId(chqEmailId.getValue());
					bean.setChqPanno(chqPanno.getValue());
					bean.setChqPayableAt(chqPayableAt.getValue());
					bean.setChqNameChngReason(chqNameChngReason.getValue());
//					bean.setChqHeirName(chqHeirName.getValue());
					SelectValue ins = new SelectValue();
					ins.setId(1l);
					ins.setValue(chqPayeeName.getValue().toString());
					bean.getDocumentDetails().setPayeeName(ins);
				}else{
					bean.setBnkmodeChngReason(bnkmodeChngReason.getValue());
					bean.setBnkEmailId(bnkEmailId.getValue());
					bean.setBnkPanno(bnkPanno.getValue());
					bean.setBnkAccNo(bnkAccNo.getValue());
					bean.setBnkIfsc(bnkIfsc.getValue());
					bean.setBnkName(bnkName.getValue());
					bean.setBnkBranch(bnkBranch.getValue());
					bean.setBnkCity(bnkCity.getValue());
					bean.setBnkNameChngReason(bnkNameChngReason.getValue());
//					bean.setBnkHeirName(bnkHeirName.getValue());
					SelectValue ins = new SelectValue();
					ins.setId(1l);
					ins.setValue(bnkPayeeName.getValue().toString());
					bean.getDocumentDetails().setPayeeName(ins);
					bean.setChqPayment(false);
				}
				
				Double balancsSIaftPayable = (bean.getAvailableSI() != null ? Double.valueOf(bean.getAvailableSI()) : 0d) - (bean.getPayble() != null ? Double.valueOf(bean.getPayble()) :0d);
				if(bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getProductType() != null
						&& bean.getNewIntimationDTO().getPolicy().getProduct().getProductType().equalsIgnoreCase("Individual")){
					bean.getDocumentDetails().setBalanceSI(bean.getNewIntimationDTO().getOrginalSI());
				} else {
					bean.getDocumentDetails().setBalanceSI(bean.getNewIntimationDTO().getPolicy().getTotalSumInsured());
				}
				if(bean.getPolicyDto().getProduct().getKey() != null &&
						!bean.getPolicyDto().getProduct().getKey().equals(601l)){
					bean.getDocumentDetails().setBalanceSI(bean.getPerPolicyLimit());
				}
				bean.getDocumentDetails().setAmountPayable(Double.valueOf(bean.getPayble()));
				bean.getDocumentDetails().setDocumentReceivedDate(bean.getDocumentDetails().getBillReceivedDate());
				bean.getDocumentDetails().setBillReceivedDate(bean.getDocumentDetails().getBillReceivedDate());
				bean.setApprovalRemarks(approvalRemarks.getValue());
			}
			if(bean.isReject()){
				bean.setRejectRemarks(rejectRemarks.getValue());
			}
			
		
		}
		
		return hasError;
	}
	
	private Boolean isValidEmail(String strEmail)
	{
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern validEmailPattern = Pattern.compile(emailPattern);
		Matcher validMatcher = validEmailPattern.matcher(strEmail);
		return validMatcher.matches();
	}

	@SuppressWarnings("static-access")
	public void showErrorMessage(String eMsg) {
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

	public void editUploadedDocumentDetails(UploadDocumentDTO dto){
		BeanItemContainer<SelectValue> beanContainer = masterService.getOPBillingCategoryTypes();
		if(null != this.uploadDocsTable){
			uploadDocsTable.init(dto,bean,SHAConstants.OUTPATIENT_FLAG);
			uploadDocsTable.setFileTypeValues(beanContainer);
			uploadDocsTable.setValueFromTable(beanContainer, dto.getFileTypeValue());
		}
	}
	
	public void deleteUploadDocumentDetails(UploadDocumentDTO dto) {
		if(null != this.uploadDocsTable){
			this.uploadedDocsTable.removeRow(dto);
			this.uploadedTblList.remove(dto);
			uploadedTblList.remove(dto);
			buttonsVlayout.setVisible(false);
			if(uploadedTblList != null && uploadedTblList.isEmpty()){
				buttonsLayout.setEnabled(false);
			}
			Long totPayable = 0l;
			if(uploadedTblList != null && !uploadedTblList.isEmpty()){
				for (UploadDocumentDTO billDetailsDTO : uploadedTblList) {
					totPayable += billDetailsDTO.getBillAmt() != null ? billDetailsDTO.getBillAmt().intValue() : 0;
				}
				if(totPayable != null && totPayable <= 0){
					approveButton.setEnabled(false);
				}
			}
		}
	}
	
	public void loadUploadedDocsTableValues(UploadDocumentDTO uploadDocsDTO){
		if(null != this.uploadedDocsTable){
			uploadDocsDTO.setFileTypeValue(uploadDocsDTO.getFileType().getValue());
			uploadDocsDTO.setFileName(uploadDocsDTO.getFileName());
//			uploadedTblList.clear();
			uploadedTblList.add(uploadDocsDTO);
			setTableValues();
		}
	}
	
	private void setTableValues(){
		BeanItemContainer<SelectValue> beanContainer = masterService.getOPBillingCategoryTypes();
		if(null != this.uploadDocsTable)	{
			this.uploadDocsTable.init(new UploadDocumentDTO(),bean,SHAConstants.OUTPATIENT_FLAG);
			uploadDocsTable.setFileTypeValues(beanContainer);		
		}
		
		if(null != this.uploadedDocsTable)	{
			uploadedDocsTable.removeRow();
			if(null != uploadedTblList && !uploadedTblList.isEmpty()) {
				 this.uploadedDocsTable.setTableList(uploadedTblList);
				 buttonsVlayout.setVisible(false);
				 buttonsLayout.setEnabled(true);
					Long totPayable = 0l;
					if(uploadedTblList != null && !uploadedTblList.isEmpty()){
						for (UploadDocumentDTO billDetailsDTO : uploadedTblList) {
							totPayable += billDetailsDTO.getBillValue() != null ? billDetailsDTO.getBillValue().intValue() : 0;
						}
						if(totPayable != null && totPayable <= 0){
							approveButton.setEnabled(false);
						} else {
							approveButton.setEnabled(true);
						}
					}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void buildQueryLayout() {
		clickAction = true;
		
		if (dynamicLayout != null && dynamicLayout.getComponentCount() > 0) {
			opRegisterPageLayout.removeComponent(opRegisterPageLayout);
			dynamicLayout.removeAllComponents();
		} else {
			dynamicLayout = new VerticalLayout();
		}
		
		TextArea queryRemarks = new TextArea("Query Remarks <b style= 'color: red'>*</b>");
		queryRemarks.setCaptionAsHtml(true);
		queryRemarks.setNullRepresentation("");

		dynamicLayout.addComponent(new FormLayout(queryRemarks));
		opRegisterPageLayout.addComponent(dynamicLayout);

		this.wizard.getNextButton().setEnabled(false);
		this.wizard.getFinishButton().setEnabled(true);

	}
	
	public List<UploadDocumentDTO>  getUploadedDocsTableValues(){
		List<UploadDocumentDTO> listUploadedDocs = new ArrayList<UploadDocumentDTO>(); 
		if(uploadedDocsTable.getValues() != null && !uploadedDocsTable.getValues().isEmpty()){
			listUploadedDocs = uploadedDocsTable.getValues();
		}
		return listUploadedDocs;
	}
	
	public void removeLayout(){
		opRegisterPageLayout.removeComponent(btnsLayout);
	}

	public Table getTable() {
		return table;
	}
	
	/*@SuppressWarnings("deprecation")
	public void buildApproveLayout() {
		clickAction = true;
		
		if (dynamicLayout != null && dynamicLayout.getComponentCount() > 0) {
			opRegisterPageLayout.removeComponent(opRegisterPageLayout);
			dynamicLayout.removeAllComponents();
		} else {
			dynamicLayout = new VerticalLayout();
		}
		
		TextArea approveRemarks = new TextArea("Approve Remarks <b style= 'color: red'>*</b>");
		approveRemarks.setCaptionAsHtml(true);
		approveRemarks.setNullRepresentation("");

		dynamicLayout.addComponent(new FormLayout(approveRemarks));
		opRegisterPageLayout.addComponent(dynamicLayout);

		this.wizard.getNextButton().setEnabled(false);
		this.wizard.getFinishButton().setEnabled(true);

	}*/
	
	/*@SuppressWarnings("deprecation")
	public void buildRejectLayout() {
		clickAction = true;
		
		if (dynamicLayout != null && dynamicLayout.getComponentCount() > 0) {
			opRegisterPageLayout.removeComponent(opRegisterPageLayout);
			dynamicLayout.removeAllComponents();
		} else {
			dynamicLayout = new VerticalLayout();
		}
		
		TextArea rejectionRemarks = new TextArea("Rejection Remarks <b style= 'color: red'>*</b>");
		rejectionRemarks.setCaptionAsHtml(true);
		rejectionRemarks.setNullRepresentation("");

		dynamicLayout.addComponent(new FormLayout(rejectionRemarks));
		opRegisterPageLayout.addComponent(dynamicLayout);

		this.wizard.getNextButton().setEnabled(false);
		this.wizard.getFinishButton().setEnabled(true);

	}*/
	
	public void alertForSecHbA1cCover(){
		if(bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_FLOATER_REVISED)
				|| bean.getPolicyDto().getProduct().getKey().equals(ReferenceTable.STAR_DIABETIC_SAFE_INDIVIDUAL_REVISED)){
			if(bean != null && bean.getDocumentDetails() != null && bean.getDocumentDetails().getConsultationType() != null &&
					bean.getDocumentDetails().getConsultationType().getId().equals(ReferenceTable.HbA1C_TREATMENT)){
				SHAUtils.showMessageBoxWithCaption(SHAConstants.HbA1C_ALERT_MSG, "INFORMATION");
			}
		}
	}
}
