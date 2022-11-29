package com.shaic.claim.cashlessprocess.downsize.wizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.PedRaisedDetailsTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.ViewClaimAmountDetils;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
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
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class DownsizePreauthDataExtractionPage  extends ViewComponent implements
WizardStep<PreauthDTO> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<PreviousPreAuthDetailsTable> preauthPreviousDetailsPage;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private PreauthDTO preauthDto;
	
//	@Inject
//	private PreviousPreAuthService previousPreAuthService;
	
	private GWizard wizard;
	
	private PreviousPreAuthDetailsTable objPreviousPreAuthDetailsTable;
	
	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;

	private AmountConsideredTable amountConsideredTable;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	@Inject
	private ViewDetails viewDetails;
	
	
	
	@Inject
	private Instance<ViewClaimAmountDetils> viewClaimAmountDetails;
	
	
	@Inject
	private Instance<RevisedMedicalDecisionTable> newMedicalDecisionTableInstance;
	
	private RevisedMedicalDecisionTable newMedicalDecisionTableObj;
	
	private List<MedicalDecisionTableDTO> medicalDecisionTableList;
	
	private List<PreviousPreAuthTableDTO> previousPreauthTableList;
	
	Map<String, Object> sublimitCalculatedValues;
	
	private String diagnosisName;
	
	private TextField downSizedAmt;
	
	private TextArea medicalRemarks;
	
	private TextArea downsizeRemarks;
	
	private TextArea downsizeInsuredRemarks;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private ComboBox escalateTo;
	
	private Upload fileUpload;
	
	private TextArea escalateRemarks;
	
	private ComboBox reasonForDownSize;
	
	private ComboBox cmbSpecialistType;
	
	private BeanItemContainer<SelectValue> selectValueContainer;
	
	private BeanItemContainer<SelectValue> escalateContainer;
	
	private TextArea doctorNote;
	
	private Button intiatePEDEndorsementButton;
	
	private VerticalLayout wholeVLayout;
	
	private Button downsizePreauthBtn;
	
	private FormLayout downSizeFormLayout;
	
	private Button balanceSumInsuredBtn;

	private Button showclaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;
	
	private Button esclateClaimBtn;
	
	private Button tmpViewBtn;
	
	private Boolean isEsclateClaim=false;
	
	Map<String, Object> referenceData;
	
	private TextField approvedAmtField;
	
	private TextField totalApprovedAmt;
	
	private TextField preauthApprovedAmtTxt;
	
	private TextField consideredAmtTxt;
	
	private TextField nonAllopathicTxt;
	
	private VerticalLayout optionCLayout;
	
	private FormLayout escalateForm;
	
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	private Double amount=0.0;

	private BeanItemContainer<ExclusionDetails> exlusionContainer;
	
	@EJB
	private MasterService masterService;
	
	private File file;
	
	@Inject
	private UploadedFileViewUI fileViewUI;

	private TextField uniquePremAmtTxt;

	private TextField amountToHospAftPremium;
	
	@Inject 
	private Instance<PedRaisedDetailsTable> pedRaiseDetailsTable;
	
	private PedRaisedDetailsTable pedRaiseDetailsTableObj;
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PreauthService preauthService;

	private TextField policyInstPremiumAmt;
	
//	public void init(PreauthDTO bean, GWizard wizard) {
//		this.bean = bean;
//		this.wizard = wizard;
//	}
	
	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
		this.selectValueContainer = this.bean.getDownSizePreauthDataExtractionDetails().getDownsizeReason();
		this.escalateContainer = this.bean.getDownSizePreauthDataExtractionDetails().getEscalateTo();
	}
	
	public void init(PreauthDTO bean,GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		this.selectValueContainer = this.bean.getDownSizePreauthDataExtractionDetails().getDownsizeReason();
		this.escalateContainer = this.bean.getDownSizePreauthDataExtractionDetails().getEscalateTo();
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	@Override
	public String getCaption() {
		return "Revision of Authorization"/*"Downsize Pre-auth"*/;
	}
	
	@Override
	public Component getContent() {
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		
//		if(bean.getIsPEDInitiatedForBtn()) {
//		
//			if(bean.isInsuredDeleted()){
//				alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
//			}else{
//				alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
//			}
//		}
//		else if(bean.isInsuredDeleted()){
//			alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
//		}
		
		intiatePEDEndorsementButton = new Button("Initiate PED Endorsement");
		
		if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() 
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType()
				&& null != bean.getNewIntimationDTO().getPolicy().getProductType().getKey()
				&& 2904 == bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue()){
			intiatePEDEndorsementButton.setEnabled(false);
		}
		else{
			intiatePEDEndorsementButton.setEnabled(true);
		}
		
		
		
		
		HorizontalLayout buttonHLayout = new HorizontalLayout(intiatePEDEndorsementButton);
		buttonHLayout.setMargin(true);
		buttonHLayout.setComponentAlignment(intiatePEDEndorsementButton, Alignment.MIDDLE_RIGHT);
		
		consideredAmtTxt = new TextField("Amount Considered");
		consideredAmtTxt.setReadOnly(false);
		consideredAmtTxt.setValue(this.bean.getAmountConsidered());
		consideredAmtTxt.setReadOnly(true);
		amountConsideredViewButton = new Button("View");
		amountConsideredViewButton.setStyleName("link");
		
		FormLayout consideredAmtForm = new FormLayout(consideredAmtTxt);
		
		HorizontalLayout amountConsideredLayout = new HorizontalLayout(
				consideredAmtForm, new FormLayout(amountConsideredViewButton));
		
		//For intiate ped endorsement button.
		
		balanceSumInsuredBtn = new Button("View");
		showclaimAmtDetailsBtnDuplicate = new Button("View");
		balanceSumInsuredBtn.setStyleName("link");
		showclaimAmtDetailsBtnDuplicate.setStyleName("link");
		
		
		objPreviousPreAuthDetailsTable = preauthPreviousDetailsPage.get();
		objPreviousPreAuthDetailsTable.init("Pre-auth Summary", false, false);
		objPreviousPreAuthDetailsTable.setTableList(this.bean.getPreviousPreauthTableDTO());	
		
		this.newMedicalDecisionTableObj=this.newMedicalDecisionTableInstance.get();
		this.newMedicalDecisionTableObj.init(this.bean);
		
		approvedAmtField = new TextField("");
		
		HorizontalLayout approvedFormLayout = new HorizontalLayout(new Label(
				"C) Sub limits, </br> Package &  </br> SI Restriction Amount",
				ContentMode.HTML), approvedAmtField);
		
		optionCLayout = new VerticalLayout(approvedFormLayout);

		Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
		HorizontalLayout viewLayout1 = new HorizontalLayout(
				amtClaimedDetailsLbl, showclaimAmtDetailsBtnDuplicate);
		viewLayout1.setSpacing(true);

		Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");

		HorizontalLayout viewLayout2 = new HorizontalLayout(
				balanceSumInsuredLbl, balanceSumInsuredBtn);
		viewLayout2.setSpacing(true);

		this.amountConsideredTable = this.amountConsideredTableInstance.get();
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2, optionCLayout, false, false);
		
		setValueToMedicalDecisionValues();
		
		MedicalDecisionTableDTO dto = new MedicalDecisionTableDTO();
		dto.setReferenceNo("Residual Treatment / Procedure Amount");
		dto.setApprovedAmount(this.preauthDto.getResidualAmountDTO().getApprovedAmount() != null ? this.preauthDto.getResidualAmountDTO().getApprovedAmount().toString() : "");
		downSizeFormLayout=new FormLayout();
		
		wholeVLayout = new VerticalLayout(amountConsideredLayout,buttonHLayout, this.newMedicalDecisionTableObj,this.amountConsideredTable,buildDownSizePreAuthButtonLayout(),downSizeFormLayout);
		wholeVLayout.setComponentAlignment(buttonHLayout, Alignment.TOP_RIGHT);
		wholeVLayout.setComponentAlignment(amountConsideredLayout, Alignment.TOP_LEFT);
		wholeVLayout.setSpacing(true);
		
		if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
			downSizeFormLayout.removeAllComponents();
			downSizeFormLayout.addComponent(downSizeFormLayout());
		}
//		else if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
//			downSizeFormLayout.removeAllComponents();
//			downSizeFormLayout.addComponent(escalateFormLayout());
//		}
		
		addMedicalDecisionTableFooterListener();
		return wholeVLayout;
		
	}
	
	public void downSizedAmount(Double amount){
		this.amount=this.amount+amount;
//		this.bean.setDownSizedAmount(this.amount);
		downSizedAmt.setValue("" + this.amount);
	}

	
	
	public HorizontalLayout buildDownSizePreAuthButtonLayout()
	{
		HorizontalLayout hLayout = new HorizontalLayout();
		//addListener();
		
//		downsizePreauthBtn = new Button("DownSize Preauth");
		downsizePreauthBtn = new Button("Revision of Authorization");
		//added for installment payment status check at policy level
//		if(this.bean.getPolicyInstalmentFlag() != null && this.bean.getPolicyInstalmentFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
//			downsizePreauthBtn.setEnabled(false);
//		}
		esclateClaimBtn = new Button("Escalate Claim");
		
		hLayout.addComponents(downsizePreauthBtn,esclateClaimBtn);
		hLayout.setSpacing(true);
		addListener();
		return hLayout;
	}
	
	public FormLayout downSizeFormLayout()
	{
		bean.setStatusKey(ReferenceTable.DOWNSIZE_APPROVED_STATUS);
		unbindField(escalateTo);
		unbindField(escalateRemarks);
		unbindField(reasonForDownSize);
		unbindField(approvedAmtField);
		unbindField(medicalRemarks);
		unbindField(totalApprovedAmt);
		unbindField(downsizeRemarks);
		unbindField(downsizeInsuredRemarks);
		unbindField(doctorNote);
		unbindField(cmbSpecialistType);
		unbindField(policyInstPremiumAmt);
		unbindField(amountToHospAftPremium);
		
		
		mandatoryFields.remove(reasonForDownSize);
		mandatoryFields.remove(downSizedAmt);
		mandatoryFields.remove(medicalRemarks);
		mandatoryFields.remove(escalateRemarks);
		mandatoryFields.remove(downsizeRemarks);
		
//		reasonForDownSize = (ComboBox) binder.buildAndBind("Reason for DownSize",
		reasonForDownSize = (ComboBox) binder.buildAndBind("Reason for Downward Revision",
				"downSizeReason", ComboBox.class);
		
		reasonForDownSize.setContainerDataSource(this.selectValueContainer);
		reasonForDownSize.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForDownSize.setItemCaptionPropertyId("value");
		reasonForDownSize.setNullSelectionAllowed(false);
		
		reasonForDownSize.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if (value != null) {
					fireViewEvent(DownSizePreauthWizardPresenter.DOWNSIZE_LAYOUT,value.getId());
				}
				
			}
		});
		
		
		if(this.bean.getPreauthMedicalDecisionDetails().getDownSizeReason() != null){
			reasonForDownSize.setValue(this.bean.getPreauthMedicalDecisionDetails().getDownSizeReason());
		}
		
//		preauthApprovedAmtTxt = (TextField) binder.buildAndBind("Total Pre-auth Approved Amt", "initialApprovedAmt", TextField.class);
//		preauthApprovedAmtTxt.setNullRepresentation("");
//		preauthApprovedAmtTxt.setValue(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? this.bean.getPreauthDataExtractionDetails().getTotalApprAmt().toString() : null);
//		preauthApprovedAmtTxt.setEnabled(false);
		
		Integer min = Math.min(amountConsideredTable.getMinimumValue(),
						SHAUtils.getIntegerFromString(this.newMedicalDecisionTableObj.dummyField
								.getValue()));
		
		
		
//		totalApprovedAmt = (TextField) binder.buildAndBind("Pre-auth Downsized Amount", "initialTotalApprovedAmt", TextField.class);
		totalApprovedAmt = (TextField) binder.buildAndBind("Revised Amount", "initialTotalApprovedAmt", TextField.class);
		totalApprovedAmt.setNullRepresentation("");
		totalApprovedAmt.setEnabled(false);
		totalApprovedAmt.setValue(min.toString());
		
		setApprovedAmtvalue();
		
		//added for CR2019184 policy instalment handling
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			Long input1 =0l;
			String input2 = null;
			Integer minValue = null;
			 minValue = amountConsideredTable.getMinimumValue() != null ? amountConsideredTable.getMinimumValue() : 0;
			java.util.Date admissionDateCnvr = bean.getPreauthDataExtractionDetails().getAdmissionDate();
			java.util.Date instalmentDateCnvr = this.bean.getPolicyInstalmentDueDate();

			java.sql.Date admissionDate = new java.sql.Date(admissionDateCnvr.getTime()); 
			java.sql.Date instalmentDate = new java.sql.Date(instalmentDateCnvr.getTime());
			DBCalculationService dbCalculationService = new DBCalculationService();
			Map<String, String> getPolicyInstallmentDetails = dbCalculationService.getPolicyInstallmentdetails(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),
					bean.getPolicyInstalmentPremiumAmt(),admissionDate,  Double.valueOf(minValue),
					instalmentDate,input1,input2);
			if(getPolicyInstallmentDetails != null && !getPolicyInstallmentDetails.isEmpty()){
				bean.setPolicyInstalmentDetailsFlag(getPolicyInstallmentDetails.get(SHAConstants.FLAG) != null ? getPolicyInstallmentDetails.get(SHAConstants.FLAG) : "N");
				bean.setPolicyInstalmentDetailsMsg(getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) != null ? getPolicyInstallmentDetails.get(SHAConstants.POLICY_INSTALMENT_MESSAGE) : null);
			}
			if(bean.getPolicyInstalmentDetailsFlag() != null && bean.getPolicyInstalmentDetailsFlag().equals(SHAConstants.YES_FLAG)){
				SHAUtils.showAlertMessageBox( bean.getPolicyInstalmentDetailsMsg());
				downsizePreauthBtn.setEnabled(false);
			} else {
				downsizePreauthBtn.setEnabled(true);
			}
		}
		
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			uniquePremAmtTxt = (TextField) binder.buildAndBind("Less: II Installment Premium Amt","uniquePremiumAmt",TextField.class);
			Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			uniquePremAmtTxt.setValue(String.valueOf(uniqueInstallmentAmount));
			uniquePremAmtTxt.setEnabled(false);
			uniquePremAmtTxt.setNullRepresentation("0");
			
//			amountToHospAftPremium = (TextField) binder.buildAndBind("Pre-auth Downsized Amt - Hospital","amountToHospAftPremium",TextField.class);
			amountToHospAftPremium = (TextField) binder.buildAndBind("Revised Amount - Hospital","amountToHospAftPremium",TextField.class);
			
			Double aftPremAmt = SHAUtils.getDoubleFromStringWithComma(totalApprovedAmt.getValue()) - SHAUtils.getDoubleFromStringWithComma(uniquePremAmtTxt.getValue());
			amountToHospAftPremium.setValue(String.valueOf(aftPremAmt.longValue()) );
			amountToHospAftPremium.setEnabled(false);
			amountToHospAftPremium.setNullRepresentation("0");
		}

		//added for CR2019184
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			policyInstPremiumAmt = (TextField) binder.buildAndBind("Policy Installment Premium","policyInstPremiumAmt",TextField.class);
			policyInstPremiumAmt.setNullRepresentation("0");
			policyInstPremiumAmt.setValue(String.valueOf(bean.getPolicyInstalmentPremiumAmt().longValue()));
			policyInstPremiumAmt.setEnabled(false);

			amountToHospAftPremium = (TextField) binder.buildAndBind("Revised Amount - Hospital","amountToHospAftPremium",TextField.class);
			Double aftPremAmt = SHAUtils.getDoubleFromStringWithComma(totalApprovedAmt.getValue()) - SHAUtils.getDoubleFromStringWithComma(policyInstPremiumAmt.getValue());
			amountToHospAftPremium.setValue(String.valueOf(aftPremAmt < 0d ? 0l : aftPremAmt.longValue()) );
			amountToHospAftPremium.setEnabled(false);
			amountToHospAftPremium.setNullRepresentation("0");

		}else{
			if(policyInstPremiumAmt != null){
				policyInstPremiumAmt.setVisible(false);
			}
			if(amountToHospAftPremium != null){
				amountToHospAftPremium.setVisible(false);
			}
		}
//		downsizeRemarks = (TextArea)binder.buildAndBind("Downsize Remarks","downsizeRemarks",TextArea.class);
		downsizeRemarks = (TextArea)binder.buildAndBind("Revision Hospital Remarks","downsizeRemarks",TextArea.class);
		
//		downsizeRemarks.setMaxLength(50);
//		downsizeRemarks.setWidth("400px");
		downsizeRemarks.setMaxLength(4000);
		downsizeRemarks.setId("hospDwnSizeRmrks");
		downsizeRemarks.setWidth("50%");
		downsizeRemarks.setHeight("200px");
		downsizeRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(downsizeRemarks,null);
		
//		downsizeInsuredRemarks = (TextArea) binder.buildAndBind("Downsize Insuerd Remarks ", "downsizeInsuredRemarks", TextArea.class);
		downsizeInsuredRemarks = (TextArea) binder.buildAndBind("Revision Insured Remarks", "downsizeInsuredRemarks", TextArea.class);
		downsizeInsuredRemarks.setMaxLength(4000);
		downsizeInsuredRemarks.setId("insDwnSizeRmrks");
		downsizeInsuredRemarks.setWidth("50%");
		downsizeInsuredRemarks.setHeight("200px");
		downsizeInsuredRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		remarksPopupListener(downsizeInsuredRemarks,null);
		
		doctorNote = (TextArea)binder.buildAndBind("Doctor Note(Internal purpose)","doctorNote",TextArea.class);
		doctorNote.setWidth("400px");
		medicalRemarks = (TextArea)binder.buildAndBind("Medical Remarks","medicalRemarks",TextArea.class);
		medicalRemarks.setMaxLength(100);
		medicalRemarks.setWidth("400px");
		
		mandatoryFields.add(reasonForDownSize);
		mandatoryFields.add(totalApprovedAmt);
		mandatoryFields.add(medicalRemarks);
		if(!ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			mandatoryFields.remove(downsizeRemarks);
		}
		else {
			mandatoryFields.add(downsizeRemarks);
		}	
		showOrHideValidation(false);
		
		FormLayout downSizeForm ;
		
		if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			downSizeForm = new FormLayout(totalApprovedAmt, policyInstPremiumAmt, amountToHospAftPremium, reasonForDownSize, new HorizontalLayout(downsizeRemarks,downsizeInsuredRemarks), doctorNote, medicalRemarks);
		}
		else if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			downSizeForm = new FormLayout(totalApprovedAmt, uniquePremAmtTxt, amountToHospAftPremium, reasonForDownSize, new HorizontalLayout(downsizeRemarks,downsizeInsuredRemarks), doctorNote, medicalRemarks);
		} else {
			downSizeForm = new FormLayout(totalApprovedAmt, reasonForDownSize, new HorizontalLayout(downsizeRemarks,downsizeInsuredRemarks), doctorNote, medicalRemarks);
		}
		return downSizeForm;
	}
	
	public FormLayout escalateFormLayout(){
		bean.setStatusKey(ReferenceTable.DOWNSIZE_ESCALATION_STATUS);
		final BeanItemContainer<SelectValue> selectValueContainer2;
		if (null != bean.getPreauthDataExtractionDetails().getNatureOfTreatment()){
			selectValueContainer2 = masterService.getMasterValueByReferenceForNonAllopathic((ReferenceTable.SPECIALIST_TYPE),this.bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue());
		}else{
			selectValueContainer2 = masterService.getSelectValueContainer(ReferenceTable.SPECIALIST_TYPE);
		}
		unbindField(escalateTo);
		unbindField(escalateRemarks);
		unbindField(reasonForDownSize);
		unbindField(downSizedAmt);
		unbindField(medicalRemarks);
		unbindField(downsizeRemarks);
		unbindField(downsizeInsuredRemarks);
		
		mandatoryFields.remove(reasonForDownSize);
		mandatoryFields.remove(downSizedAmt);
		mandatoryFields.remove(medicalRemarks);
		mandatoryFields.remove(escalateRemarks);
		mandatoryFields.remove(downsizeRemarks);
		
		escalateTo=(ComboBox) binder.buildAndBind("Escalate To",
				"escalateTo", ComboBox.class);
		
		List<SelectValue> escalateValues = this.escalateContainer.getItemIds();
		
		BeanItemContainer<SelectValue> escalateContainer2 = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		if(bean.getDownSizePreauthDataExtrationDetails().getCMA4()){
			
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA3()){
			for (SelectValue selectValue : escalateValues) {
				if(selectValue.getId().equals(ReferenceTable.CMA3)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA2()){
			for (SelectValue selectValue : escalateValues) {
				if(selectValue.getId().equals(ReferenceTable.CMA3) || selectValue.getId().equals(ReferenceTable.CMA2)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA1()){
			for (SelectValue selectValue : escalateValues) {
				if(selectValue.getId().equals(ReferenceTable.CMA3) || selectValue.getId().equals(ReferenceTable.CMA2)
						|| selectValue.getId().equals(ReferenceTable.CMA1)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}
		
		for (SelectValue selectValue : escalateValues) {
			if(selectValue.getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
			escalateContainer2.addBean(selectValue);
			}	
		}
		
		escalateTo.setContainerDataSource(escalateContainer2);
		escalateTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		escalateTo.setItemCaptionPropertyId("value");
		escalateTo.setNullSelectionAllowed(false);
		
		final FormLayout escalateForm1=new FormLayout();
		final FormLayout escalateForm2 = new FormLayout();
		final FormLayout escalateForm3 = new FormLayout();
		
		final Label dummyLable1 = new Label();
		final Label dummyLable2 = new Label();
		
		escalateTo.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				
				if(value != null && value.getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
					
					unbindField(cmbSpecialistType);
					cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistValue",ComboBox.class);
					cmbSpecialistType.setContainerDataSource(selectValueContainer2);
					cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbSpecialistType.setItemCaptionPropertyId("value");
					cmbSpecialistType.setVisible(true);
					
					escalateForm1.addComponent(cmbSpecialistType,1);
					escalateForm2.addComponent(dummyLable1,1);
					escalateForm3.addComponent(dummyLable2,1);
					mandatoryFields.add(cmbSpecialistType);
					
					showOrHideValidation(false);
					
				}else{
					unbindField(cmbSpecialistType);
					if(cmbSpecialistType != null){
						
						escalateForm.removeComponent(cmbSpecialistType);
						mandatoryFields.remove(cmbSpecialistType);
						cmbSpecialistType.setVisible(false);
						
					}
					
				}
			}
		});
		
		if(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo() != null){
			escalateTo.setValue(this.bean.getPreauthMedicalDecisionDetails().getEscalateTo());
		}
		
		fileUpload  = new Upload("", new Receiver() {
			
			private static final long serialVersionUID = 4775959511314943621L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
		        FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		        	if(filename != null && ! filename.equalsIgnoreCase("")){
				            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
				            fos = new FileOutputStream(file);
		        	}
		        	else{
		        		  
		        
		        	}
//		        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//		        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//		        	}
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
		                .show(com.vaadin.server.Page.getCurrent());
		            return null;
		        }
		        return fos; // Return the output stream to write to
			}
		});	
		fileUpload.addSucceededListener(new SucceededListener() {
			
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				System.out.println("File uploaded" + event.getFilename());
				
				try{
					
					byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
					
					if(null != fileAsbyteArray )
					{
						
						Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
						boolean hasSpecialChar = p.matcher(event.getFilename()).find();
					//	if(hasSpecialChar)
						//{
						WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
							Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
							//TO read file after load
							if (flagUploadSuccess.booleanValue())
							{
								String token = "" + uploadStatus.get("fileKey");
								String fileName = event.getFilename();
								
							    bean.setTokenName(token);
							    bean.setFileName(fileName);
			                    tmpViewBtn.setEnabled(true);
							    buildSuccessLayout();
							    uploadStatus = null;
//							    thisObj.close();
							}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		fileUpload.setCaption("File Upload");
		fileUpload.setButtonCaption(null);
		
		tmpViewBtn = new Button("View File");
	    tmpViewBtn.setEnabled(false);
	    tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
	    tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
	    tmpViewBtn.setWidth("50%");
	
        tmpViewBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getFileName() != null && bean.getTokenName() != null){
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					fileViewUI.init(popup,bean.getFileName(), bean.getTokenName());
					popup.setContent(fileViewUI);
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
		});
        
     
		Button uploadBtn = new Button("Upload");
		HorizontalLayout uploadHor = new HorizontalLayout(fileUpload,uploadBtn,tmpViewBtn);
		uploadHor.setComponentAlignment(uploadBtn, Alignment.BOTTOM_CENTER);
		uploadHor.setComponentAlignment(tmpViewBtn, Alignment.MIDDLE_LEFT);
		uploadHor.setSpacing(false);
		
		uploadBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fileUpload.submitUpload();
			}
		});
		
		escalateRemarks=(TextArea)binder.buildAndBind("Escalation Remarks","escalateRemarks",TextArea.class);
		escalateRemarks.setMaxLength(100);
		escalateRemarks.setWidth("400px");
		mandatoryFields.add(escalateRemarks);
		mandatoryFields.add(escalateTo);
		
		
		
		
		
		showOrHideValidation(false);
		escalateForm1.addComponent(escalateTo);
		escalateForm1.addComponent(fileUpload);
		escalateForm1.addComponent(escalateRemarks);
		
		TextField dummyField = new TextField();
		dummyField.setEnabled(false);
		dummyField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		TextField dummyField2 = new TextField();
		dummyField2.setEnabled(false);
		dummyField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		escalateForm2.addComponent(dummyField);
		escalateForm2.addComponent(uploadBtn);
		escalateForm2.setWidth("30px");
		//Vaadin8-setImmediate() escalateForm2.setImmediate(true);
		
		TextField dummyField1 = new TextField();
		dummyField1.setEnabled(false);
		dummyField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		TextField dummyField4 = new TextField();
		dummyField4.setEnabled(false);
		dummyField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		 escalateForm3.addComponent(dummyField1);
		 escalateForm3.addComponent(tmpViewBtn);
//		escalateForm3.setWidth("40%");
		//Vaadin8-setImmediate() escalateForm3.setImmediate(true);
		
		HorizontalLayout mainHor = new HorizontalLayout(escalateForm1,escalateForm2,escalateForm3);
		mainHor.setWidth("100%");
		//Vaadin8-setImmediate() mainHor.setImmediate(true);
		mainHor.setSpacing(false);
		escalateForm = new FormLayout(mainHor);
		escalateForm.setWidth("100%");
		return escalateForm;
	}
	
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> File Uploaded Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
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
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		 this.referenceData = referenceData;		 
		 this.newMedicalDecisionTableObj.setReferenceData(referenceData);
		 
		 @SuppressWarnings("unchecked")
		 BeanItemContainer<SelectValue> fvrNotRequiredRemarks = (BeanItemContainer<SelectValue>) referenceData
			.get("fvrNotRequiredRemarks");
		 
		 BeanItemContainer<TmpInvestigation> investigatorNameContainer = (BeanItemContainer<TmpInvestigation>) referenceData.get("investigatorName");
		 
	}
	
	public void setValueToMedicalDecisionValues(){
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				
				if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() != 2904 ||
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)){
					
					dto.setPedImpactOnDiagnosis(pedValidationTableDTO.getPedImpactOnDiagnosis() != null && pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() != null ? pedValidationTableDTO.getPedImpactOnDiagnosis().getValue() : "");
					dto.setNotPayingReason(pedValidationTableDTO.getReasonForNotPaying() != null && pedValidationTableDTO.getReasonForNotPaying().getValue() != null ? pedValidationTableDTO.getReasonForNotPaying().getValue() : "");
					dto.setReasonForNotPaying(pedValidationTableDTO.getReasonForNotPaying() != null ? pedValidationTableDTO.getReasonForNotPaying() : null);
				}
				
				if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = pedValidationTableDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (isPaymentAvailable) {
						List<PedDetailsTableDTO> pedList = pedValidationTableDTO
								.getPedList();
						if (!pedList.isEmpty()) {
							for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

								List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
										.getExclusionAllDetails();
								String paymentFlag = "y";
								for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
									if (null != pedDetailsTableDTO
											.getExclusionDetails()
											&& exclusionDetails
													.getKey()
													.equals(pedDetailsTableDTO
															.getExclusionDetails()
															.getId())) {
										paymentFlag = exclusionDetails
												.getPaymentFlag();
									}
								}

								if (paymentFlag.toLowerCase().equalsIgnoreCase(
										"n")) {
									isPaymentAvailable = false;
									break;
								}
							}
						}
					}

					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				} else {
					dto.setIsPaymentAvailable(false);
				}
				if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
					dto.setRestrictionSI("NA");
				} else {
					dto.setRestrictionSI(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSumInsuredRestriction()
									.getValue()).toString());
				}

				if (pedValidationTableDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSublimitAmt()).toString());
				}
				dto.setPackageAmt("NA");
				//GLX2020047
				dto.setAgreedPackageAmt("NA");
				dto.setDiagnosisDetailsDTO(pedValidationTableDTO);
				if(pedValidationTableDTO.getPedList().size() == 1) {
					dto.setCoPayPercentage(pedValidationTableDTO.getPedList().get(0).getCopay());
				}
				SelectValue value = new SelectValue();
				String[] copayWithPercentage = pedValidationTableDTO.getCopayPercentage().toString().split("\\.");
				String copay = copayWithPercentage[0].trim();
				value.setId(Long.valueOf(copay));
				value.setValue(pedValidationTableDTO.getCopayPercentage().toString());
				dto.setCoPayPercentage(value);
//				dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
//				dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
//				dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
				
				if(this.bean.getIsAmbulanceApplicable()){
					dto.setIsAmbulanceEnable(true);
				}else{
					dto.setIsAmbulanceEnable(false);
				}
				
				medicalDecisionDTOList.add(dto);
			}
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setProcedureDTO(procedureDTO);
				Boolean isPaymentAvailable = true;
				if (procedureDTO.getConsiderForPaymentFlag() != null) {
					 isPaymentAvailable = procedureDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
				} else {
					isPaymentAvailable = false;
					if(procedureDTO.getNewProcedureFlag() != null && procedureDTO.getNewProcedureFlag().equals(1l)) {
						isPaymentAvailable = true;
					}
					dto.setIsPaymentAvailable(false);
				}
					if(isPaymentAvailable) {
						if(procedureDTO.getExclusionDetails() != null && procedureDTO.getExclusionDetails().getValue() != null && !procedureDTO.getExclusionDetails().getValue().toLowerCase().equalsIgnoreCase("not applicable")) {
							isPaymentAvailable = false;
						}
					}
					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				dto.setRestrictionSI("NA");
				
				//GLX2020047 - UAT changes
				//dto.setPackageAmt("NA");
				dto.setPackageAmt("0");
				//GLX2020047
				dto.setAgreedPackageAmt("NA");
				if(procedureDTO.getPackageRate() != null && procedureDTO.getPackageRate() >= 0) {
					dto.setPackageAmt(procedureDTO.getPackageRate().toString());
					//GLX2020047
					dto.setAgreedPackageAmt(procedureDTO.getPackageRate().toString());
				}
				

				if (procedureDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							procedureDTO.getSublimitAmount()).toString());
				}
				SelectValue value = new SelectValue();
				String[] copayWithPercentage = procedureDTO.getCopayPercentage().toString().split("\\.");
				String copay = copayWithPercentage[0].trim();
				value.setId(Long.valueOf(copay));
				value.setValue(procedureDTO.getCopayPercentage().toString());
				dto.setCoPayPercentage(value);
				//dto.setCoPayPercentage(procedureDTO.getCopay());
				//dto.setCoPayPercentage(procedureDTO.getCopayPercentage());
				
//				dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
//				dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
//				dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
				
				if(this.bean.getIsAmbulanceApplicable()){
					dto.setIsAmbulanceEnable(true);
				}else{
					dto.setIsAmbulanceEnable(false);
				}
				
				
				medicalDecisionDTOList.add(dto);
			}

			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));
			
			int medicalDecisionSize = medicalDecisionDTOList.size();
			

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					caluculationInputValues.put("preauthKey",
							this.bean.getPreviousPreauthKey());
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
						caluculationInputValues
								.put("restrictedSI",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSumInsuredRestriction() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSumInsuredRestriction().getId()
												: null);
						caluculationInputValues
						.put("restrictedSIAmount",
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getValue()
										: null);
						
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName() == null ? 0l : medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName().getId().toString());
						caluculationInputValues.put("referenceFlag", "D");

						
					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId", medicalDecisionDto.getProcedureDTO().getProcedureName() == null ? 0l : (medicalDecisionDto.getProcedureDTO().getProcedureName().getId() == null ? 0l : medicalDecisionDto.getProcedureDTO().getProcedureName().getId()));
						caluculationInputValues.put("referenceFlag", "P");
						
						caluculationInputValues
						.put("preauthKey",
								(medicalDecisionDto.getProcedureDTO()
										.getNewProcedureFlag() != null && medicalDecisionDto
										.getProcedureDTO()
										.getNewProcedureFlag() == 0) ? this.bean
										.getPreviousPreauthKey() : 0l);
					
					}
//					caluculationInputValues.put("preauthKey", 0l);
					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
					caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY,bean.getClaimDTO().getKey());

					bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
					
					fireViewEvent(
							DownSizePreauthWizardPresenter.SUM_INSURED_CALCULATION,
							caluculationInputValues,bean);
					
					Map<String, Object> values = this.sublimitCalculatedValues;
					
					if(bean.getIsNonAllopathic()) {
						bean.setNonAllopathicOriginalAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
						bean.setNonAllopathicUtilizedAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
					}
					
					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					medicalDecisionDto.setAvailableAmout(((Double) values
							.get("restrictedAvailAmt")).intValue());
					medicalDecisionDto.setUtilizedAmt(((Double) values
							.get("restrictedUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAmount(((Double) values
							.get("currentSL")).intValue() > 0 ? (String
							.valueOf(((Double) values.get("currentSL"))
									.intValue())) : "NA");
					medicalDecisionDto.setSubLimitUtilAmount(((Double) values
							.get("SLUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
							.get("SLAvailAmt")).intValue());
					medicalDecisionDto
							.setCoPayPercentageValues((List<String>) values
									.get("copay"));					
				/*medicalDecisionDto.setCoPayPercentage(coPayPercentage);*/
					

					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.getDiagnosisDetailsDTO()
								.setDiagnosis(this.diagnosisName);
					}

					// need to implement in new medical listener table
					if(bean.getNewIntimationDTO() != null && bean.getNewIntimationDTO().getPolicy() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_DELITE)) {
						Integer subLimitAvaliableAmt = 0;
						Boolean isResidual = false;
						if(medicalDecisionDto.getDiagnosisDetailsDTO() != null && medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName() != null && (medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
							subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
						} else if (medicalDecisionDto.getProcedureDTO() != null && medicalDecisionDto.getProcedureDTO().getSublimitName() != null && (medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_1) || medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId().equals(ReferenceTable.SUBLIMIT_INFECTIOUS_2)) ) {
							subLimitAvaliableAmt = medicalDecisionDto.getSubLimitAvaliableAmt();
						} else {
							isResidual = true;
						}
						
						if(!isResidual && bean.getEntitlmentNoOfDays() != null) {
							Float floatAvailAmt = bean.getEntitlmentNoOfDays() * subLimitAvaliableAmt;
							Integer availAmt = Math.round(floatAvailAmt);
							int min = Math.min(SHAUtils.getIntegerFromString(medicalDecisionDto.getSubLimitAmount()) , availAmt);
							medicalDecisionDto.setSubLimitAvaliableAmt(min);
							medicalDecisionDto.setSubLimitUtilAmount(0);
						}
					}
					
					
					if(medicalDecisionSize == 1){
						
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							
							if(this.bean.getAmbulanceLimitAmount() > 0){
								medicalDecisionDto.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								medicalDecisionDto.setIsAmbulanceEnable(true);
								medicalDecisionDto.setIsAmbChargeApplicable(true);
							}
						}
						
					}
					
					this.newMedicalDecisionTableObj
							.addBeanToList(medicalDecisionDto);
				}
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				//GLX2020047
				dto.setAgreedPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				dto.setIsAmbulanceEnable(false);
				this.newMedicalDecisionTableObj.addBeanToList(dto);
				
				if(bean.getIsNonAllopathic()) {
					createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
				}
			}
			
		} else {
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : filledDTO) {
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					Boolean procedureStatus = SHAUtils.getProcedureStatus(diagnosisProcedureTableDTO.getProcedureDTO());
					if(!procedureStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(procedureStatus);
				} else if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					Boolean diagnosisStatus = SHAUtils.getDiagnosisStatus(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO());
					if(!diagnosisStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(diagnosisStatus);
				}
			}
			this.newMedicalDecisionTableObj.addList(filledDTO);
			if(bean.getIsNonAllopathic()) {
				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
		}
	}
	
	private void createNonAllopathicFields(Double originalAmt, Double utilizedAmt) {
		nonAllopathicTxt = new TextField("Non-Allopathic Avail Amt");
		nonAllopathicTxt.setWidth("80px");
		Double availAmt = originalAmt - utilizedAmt;
		nonAllopathicTxt.setValue(String.valueOf(availAmt.intValue()) );
		nonAllopathicTxt.setEnabled(false);
		bean.setNonAllopathicAvailAmt(availAmt.intValue());
		bean.setNonAllopathicOriginalAmt(originalAmt);
		bean.setNonAllopathicUtilizedAmt(utilizedAmt);
		
		Map<String, Object> values = new WeakHashMap<String, Object>();
		values.put(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT, originalAmt);
		values.put(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT, utilizedAmt);
		Button viewBtn = new Button("View");
		viewBtn.setData(values);
		viewBtn.setStyleName("link");
		viewBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 9127517383717464157L;

			@Override
			public void buttonClick(ClickEvent event) {
				Map<String, Object> values = (Map<String, Object>) event.getButton().getData();
				
				TextField originalAmt = new TextField("Total Original Amt");
				originalAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT)).intValue()));
				originalAmt.setReadOnly(true);
				originalAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				TextField utilizedAmt = new TextField("Utilized Amt");
				utilizedAmt.setValue(String.valueOf(((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT)).intValue()));
				utilizedAmt.setReadOnly(true);
				utilizedAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				TextField availableAmt = new TextField("Available Amt");
				Double availAmt = (Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT) - (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT);
				availableAmt.setValue(String.valueOf(availAmt.intValue()) );
				availableAmt.setReadOnly(true);
				availableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Non-Allopathic Details");
				dialog.setClosable(true);
				dialog.setWidth("400px");
				dialog.setResizable(false);
				dialog.setContent(new FormLayout(originalAmt, utilizedAmt, availableAmt));
				dialog.show(getUI().getCurrent(), null, true);
			}
		});
		HorizontalLayout horizontalLayout = new HorizontalLayout(new FormLayout(nonAllopathicTxt), viewBtn);
		horizontalLayout.setSpacing(true);
		optionCLayout.addComponent(horizontalLayout);
		optionCLayout.setSpacing(true);
	}
	
	private void setResidualAmtToDTO() {
		List<DiagnosisProcedureTableDTO> values = this.newMedicalDecisionTableObj.getValues();
		for (DiagnosisProcedureTableDTO medicalDecisionTableDTO : values) {
			if(medicalDecisionTableDTO.getDiagOrProcedure() != null && medicalDecisionTableDTO.getDiagOrProcedure().contains("Residual")) {
				ResidualAmountDTO residualAmountDTO = this.bean.getResidualAmountDTO();
				residualAmountDTO.setNetAmount(medicalDecisionTableDTO.getNetAmount().doubleValue());
				residualAmountDTO.setMinimumAmount(medicalDecisionTableDTO.getMinimumAmount().doubleValue());
				residualAmountDTO.setCopayPercentage(Double.valueOf(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
								.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
										.getValue()) : 0d));
				residualAmountDTO.setCopayAmount(medicalDecisionTableDTO.getCoPayAmount().doubleValue());
				residualAmountDTO.setApprovedAmount(medicalDecisionTableDTO.getNetApprovedAmt().doubleValue());
				residualAmountDTO.setRemarks(medicalDecisionTableDTO.getRemarks());
				residualAmountDTO.setCoPayTypeId(medicalDecisionTableDTO.getCoPayType());
			}
		}
	}
	
	
	
	public void addListener(){
		downsizePreauthBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
									
				if(bean.isInsuredDeleted()){
					alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
				}
				
				Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());
				   NegotiationDetails negotiationPending = preauthService.getNegotiationPending(bean.getClaimDTO().getKey());
				    if(negotiationPending != null){
				    	manageNegotation(intimationDtls,negotiationPending,SHAConstants.DOWNSIZE_PRE_AUTH);
				    }else{
				    	downSizeFormLayout.removeAllComponents();
						downSizeFormLayout.addComponent(downSizeFormLayout());
//						bean.setIsDownsizeOrEscalate(true);
						wizard.getFinishButton().setEnabled(false);
						wizard.getNextButton().setEnabled(true);
				    }
					
				
			}
		});
		
		esclateClaimBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				isEsclateClaim=true;
				downSizeFormLayout.removeAllComponents();
				downSizeFormLayout.addComponent(escalateFormLayout());
				
				wizard.getFinishButton().setEnabled(true);
				wizard.getNextButton().setEnabled(false);
				
			}
		});

		intiatePEDEndorsementButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -8159939563947706329L;

				@Override
				public void buttonClick(ClickEvent event) {
					Long preauthKey = bean.getKey();          //preauth key from table dto
					Long intimationKey = bean.getIntimationKey();
					Long policyKey = bean.getPolicyKey();
					Long claimKey = bean.getClaimKey();
					if(bean.getIsPEDInitiatedForBtn()) {
						if(bean.isInsuredDeleted()){
							alertMessageForPEDInitiate(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
						}
						else{
							alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
						}	
						createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
					}					
				}
			});
		
		
		amountConsideredViewButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2259148886587320228L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						DownSizePreauthWizardPresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});
		
		balanceSumInsuredBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4478247898237407113L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO() != null
						&& bean.getNewIntimationDTO().getIntimationId() != null
						&& !bean.getNewIntimationDTO().getIntimationId()
								.equals("")) {
					fireViewEvent(
							DownSizePreauthWizardPresenter.VIEW_BALANCE_SUM_INSURED_DETAILS,
							bean.getNewIntimationDTO().getIntimationId());
				}
			}
		});
		
		showclaimAmtDetailsBtnDuplicate.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2259148886587320228L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						DownSizePreauthWizardPresenter.VIEW_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});
	}

	private void createPEDScreen(Long preauthKey, Long intimationKey,
			Long policyKey, Long claimKey) {
		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
		viewPEDRequest.initView(bean,preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.DOWNSIZE_STAGE,false);	
		viewPEDRequest.setPresenterString(SHAConstants.CASHLESS_STRING);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View PED Request Details");
		popup.setWidth("85%");
		popup.setHeight("100%");
		popup.setContent(viewPEDRequest);
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
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	
	public Boolean alertMessageForPEDInitiate(final String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		
		VerticalLayout layout = new VerticalLayout(successLabel);
		
		if(SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){
			
			pedRaiseDetailsTableObj = pedRaiseDetailsTable.get();
			pedRaiseDetailsTableObj.init("", false, false);
			pedRaiseDetailsTableObj.initView(bean.getNewIntimationDTO().getPolicy().getKey(), bean.getNewIntimationDTO().getInsuredPatient().getKey());
			
			layout.addComponent(pedRaiseDetailsTableObj.getTable());
		}
		layout.addComponent(homeButton);
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
				
				if(bean.isMultiplePEDAvailableNotDeleted() && !SHAConstants.PED_RAISE_MESSAGE.equalsIgnoreCase(message)){
					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
					bean.setMultiplePEDAvailableNotDeleted(false);
				}
//				Long preauthKey=bean.getKey();                                    // as per CR R1086 Auto deletion of Risk - Non Disclosed PED 
//				Long intimationKey=bean.getIntimationKey();
//				Long policyKey=bean.getPolicyKey();
//				Long claimKey=bean.getClaimKey();
//				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
		return true;
	}
	
	@Override
	public boolean onAdvance() {
		
		setResidualAmtToDTO();
		
		return validatePage();
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		return false;
	}
	
	public void setReferenceData(List<MedicalDecisionTableDTO> medicalDecisionTableList) {
		
		this.medicalDecisionTableList=medicalDecisionTableList;
		
	}
	
	private void unbindField(Field<?> field) {
		if (field != null) {
			if(binder.getPropertyId(field)!=null){
				this.binder.unbind(field);
			}
			
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
		if((this.reasonForDownSize == null && this.escalateTo == null)){
			hasError = true;
//			eMsg.append("Please Select Downsize prauth or Escalate Claim</br>");
			eMsg.append("Please Select Revision of Authorization or Escalate Claim</br>");
		}else if((this.reasonForDownSize != null && this.reasonForDownSize.getValue() == null) && (this.escalateTo.getValue() != null && this.escalateTo.getValue() == null)){
			hasError = true;
//			eMsg.append("Please Select Downsize prauth or Escalate Claim</br>");
			eMsg.append("Please Select Revision of Authorization or Escalate Claim</br>");
		}
		
		if(ReferenceTable.getGMCProductListWithoutOtherBanks().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			
			if(this.reasonForDownSize.getValue() != null) {
				if(downsizeRemarks.getValue() == null || downsizeRemarks.getValue().isEmpty()) {
					hasError = true;
//					eMsg.append("Please Enter Downsize Remarks.</br>");
					eMsg.append("Please Enter Revision Hospital / Insured Remarks.</br>");
				}	
			}
		}
		
		if(((ReferenceTable.DOWNSIZE_APPROVED_STATUS).equals(bean.getStatusKey()) 
				&& this.totalApprovedAmt != null
				&& ("0").equalsIgnoreCase(this.totalApprovedAmt.getValue()))){
			hasError = true;
//			eMsg.append("Please Select Downsize prauth or Escalate Claim</br>");
			eMsg.append("Please Select Revision of Authorization or Escalate Claim</br>");
		}
		
		 if(!this.newMedicalDecisionTableObj.getTotalAmountConsidered().equals(SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
			 hasError = true;
			 eMsg.append("Total Amount Considered Should be equal to Claimed Amount Page Payable Amount. </br>");
		 }
		 
		 if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
				if(this.bean.getIsAmbulanceApplicable() && this.newMedicalDecisionTableObj.getAmbulanceLimitAmount() != null
						&& ! this.bean.getAmbulanceLimitAmount().equals(this.newMedicalDecisionTableObj.getAmbulanceLimitAmount())){
					
					hasError = true;
					eMsg.append("Amount Entered against Ambulance charges should be equal");
					
				}
		}
		 

		// GLX2020047
		String errMsg = newMedicalDecisionTableObj.isValidForPkgChange();

		if (errMsg != null && !errMsg.isEmpty()) {
			hasError = true;
			eMsg.append(errMsg + "<br>");
		}
		 
		 if(! hasError){
			 try {
				this.binder.commit();
				if(this.bean.getStatusKey() != null && this.bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
					Integer min = Math.min(amountConsideredTable.getMinimumValue(),
							SHAUtils.getIntegerFromString(this.newMedicalDecisionTableObj.dummyField
									.getValue()));
					this.bean.getPreauthMedicalDecisionDetails().setInitialApprovedAmt(min != null ? Double.valueOf(min) : 0d);
					this.bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min != null ? Double.valueOf(min) : 0d);
				}
				
				/*if(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet().isEmpty()){
					List<NoOfDaysCell> copyClaimeDetailsList = SHAUtils.copyClaimeDetailsList(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList());
					this.bean.getPreauthDataExtractionDetails().setClaimedDetailsListForBenefitSheet(copyClaimeDetailsList);
				}*/
				
			} catch (CommitException e) {
				e.printStackTrace();
			}
		 }
		 
		  if(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null 
	        		&& this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null
	        		&& this.bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
	        	Double TotalApprovedAmount = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
	        	Double downsizeAmt = this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
	        	
	        	if(TotalApprovedAmount <= downsizeAmt){
	        		hasError = true;
	        		eMsg.append("Downsize amount should be less than Preauth approved Amount. </br>");
	        	}
	        }
		  if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			  Double TotalApprovedAmount = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
			  Double aftPremAmt = (this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()
					  != null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() : 0d) - SHAUtils.getDoubleFromStringWithComma(policyInstPremiumAmt.getValue());
			  this.bean.getPreauthMedicalDecisionDetails().setAmountToHospAftPremium(aftPremAmt);
			  if(aftPremAmt <= 0){
	        		hasError = true;
	        		eMsg.append("Downsize amount should be greater than Premium Instalment Amount . </br>");
	        	}
		  }
		 
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
		} else {
			try {
//				this.binder.commit();  duplicate already commit called above.
				
				SHAUtils.doFinalEnhancementCalculationForLetter(this.newMedicalDecisionTableObj.getValues(), SHAUtils.getDoubleFromStringWithComma(this.amountConsideredTable.getCoPayValue()) , bean);
				
			    this.bean.getPreauthMedicalDecisionDetails().setMedicalDecisionTableDTO(this.newMedicalDecisionTableObj.getValues());
			    this.bean.getPreauthMedicalDecisionDetails().setDownsizedAmt(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()
			    		!= null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() : 0d);
			    
			    this.bean.setFinalTotalApprovedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());
			    
			    if(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null){
					Double diffAmount =  this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() - this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
					this.bean.setDiffAmount(diffAmount);
				}
			    
			    if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			    	
			    	Double aftPremAmt = (this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()
				    		!= null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() : 0d) - SHAUtils.getDoubleFromStringWithComma(uniquePremAmtTxt.getValue());
			    	this.bean.getPreauthMedicalDecisionDetails().setAmountToHospAftPremium(aftPremAmt);
			    	
			    	
			    }
			    if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG)){
			    	Double aftPremAmt = (this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()
			    			!= null ? this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() : 0d) - SHAUtils.getDoubleFromStringWithComma(policyInstPremiumAmt.getValue());
			    	this.bean.getPreauthMedicalDecisionDetails().setAmountToHospAftPremium(aftPremAmt);
			    }
			    
			    if(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsListForBenefitSheet().isEmpty()){
					List<NoOfDaysCell> copyClaimeDetailsList = SHAUtils.copyClaimeDetailsList(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList());
					this.bean.getPreauthDataExtractionDetails().setClaimedDetailsListForBenefitSheet(copyClaimeDetailsList);
				}
			    
			    if(bean.getPreauthDataExtractionDetails().getOtherBenfitFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDataExtractionDetails().getOtherBenfitFlag())){
					
					bean.getPreauthMedicalDecisionDetails().setEnhBenefitApprovedAmount(bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt());

			    }
			    //added for preauth approved date in withdraw letter issue(IMSSUPPOR-28726).
			    fireViewEvent(DownSizePreauthWizardPresenter.PREAUTH_APPROVED_DATE, bean);
			    
			 // New requirement for saving Copay values to Transaction Table......... 
				SHAUtils.setCopayAmounts(bean, this.amountConsideredTable);

				if(approvedAmtField != null && approvedAmtField.getValue() != null){
					Double approvedAmount = SHAUtils.getDoubleValueFromString(approvedAmtField.getValue());
					this.bean.setSublimitAndSIAmt(approvedAmount);
				}

			    return true;
		   
			} catch (Exception e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return false;
		}
	}
	
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	public void setBalanceSI(Double balanceSI, List<Double> productCopay) {
		if(balanceSI == null) {
			balanceSI = new Double("0");
		}
		this.bean.setBalanceSI(balanceSI);
		this.bean.setProductCopay(productCopay);
	}
	
	public void setSumInsuredCaculationsForSublimit(
			Map<String, Object> diagnosisSumInsuredLimit,String diagnosisName) {
		this.sublimitCalculatedValues = diagnosisSumInsuredLimit;
		this.diagnosisName = diagnosisName;
	}
	
	public void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> exclusionContainer) {
	
		this.exlusionContainer=exclusionContainer;
	}
		
	private void addMedicalDecisionTableFooterListener() {

		this.newMedicalDecisionTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 4843316375590220412L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmtvalue = SHAUtils
								.getIntegerFromString((String) event
										.getProperty().getValue());
						approvedAmtField.setValue(String
								.valueOf(totalApprovedAmtvalue));
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmtvalue);
						if(bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						
						if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
							min = Math.min(
									amountConsideredTable.getMinimumValueForGMC(),
									totalApprovedAmtvalue);
							
							if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
								Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
								Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
								if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
									bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
								}
								
							}
							
						}
						
						if ((bean.getStatusKey()
								.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
							Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
							if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
									bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
								if(!bean.getIsReverseAllocation() && totalApprovedAmt != null) {
									totalApprovedAmt.setValue(min.toString());
								}
							}else{
								if(totalApprovedAmt != null) {
									totalApprovedAmt.setValue(min.toString());
								}
							}
							
							if(bean.getNewIntimationDTO() != null) {
								
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtvalue);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmtvalue));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmtvalue));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
										if(totalApprovedAmt != null){
											totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
										}
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										if(totalApprovedAmt != null){
											totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
										}
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}
									
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									/**
									 * As per Tickert No.: IMSSUPPOR-20138, the below line was uncommented.
									 */
									approvedAmtField.setValue(String.valueOf(totalApprovedAmtvalue));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtvalue);
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmtvalue));
										if(totalApprovedAmt != null){
											totalApprovedAmt.setValue(amountConsideredTable.doRevisedSuperSurplusCalculation().toString());
										}
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
										if(totalApprovedAmt != null){
											totalApprovedAmt.setValue(amountConsideredTable.doRevisedSuperSurplusCalculation().toString());
										}
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										//processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doRevisedSuperSurplusCalculation());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
										if(totalApprovedAmt != null){
											totalApprovedAmt.setValue(amountConsideredTable.doRevisedSuperSurplusCalculation().toString());
										}
									}
									
								}
							}
						} else {
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtvalue);
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmtvalue));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
										if(totalApprovedAmt != null){
											totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
										}
//										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
//										processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
										if(totalApprovedAmt != null){
											totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
										}
									}
								
//									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
								} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
										bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
									
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtvalue);
										amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmtvalue));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
										bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
										if(totalApprovedAmt != null){
											totalApprovedAmt.setValue(amountConsideredTable.doRevisedSuperSurplusCalculation().toString());
										}
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										
										//amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
										if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
											amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
										}
										
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
											Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
											amt =amt - doubleValueFromString;
										}
										//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
										//processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doRevisedSuperSurplusCalculation());
										//bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
									}
								
//									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
								}
							}
						}
						
						
					}
				});
		
		this.amountConsideredTable.dummyField
		.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4843316375590220412L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Integer totalApprovedAmtValue = SHAUtils
						.getIntegerFromString(newMedicalDecisionTableObj.dummyField
								.getValue());
				approvedAmtField.setValue(String
						.valueOf(totalApprovedAmtValue));
				if ((bean.getStatusKey()
						.equals(ReferenceTable.PREAUTH_APPROVE_STATUS))) {
					Integer min = Math.min(
							amountConsideredTable.getMinimumValue(),
							totalApprovedAmtValue);
					if(bean.getIsNonAllopathic()) {
						min = Math.min(min, bean.getNonAllopathicAvailAmt());
					}
					if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
						min = Math.min(
								amountConsideredTable.getMinimumValueForGMC(),
								totalApprovedAmtValue);
						
						if(bean.getPreauthDataExtractionDetails().getCorpBuffer() != null && bean.getPreauthDataExtractionDetails().getCorpBuffer()){
							Integer corpBufferUtilizedAmt = amountConsideredTable.getCorpBufferUtilizedAmt();
							Integer corpBufferAllocatedClaim = bean.getPreauthDataExtractionDetails().getCorpBufferAllocatedClaim();
							if(corpBufferUtilizedAmt <= corpBufferAllocatedClaim){
								bean.getPreauthMedicalDecisionDetails().setCorporateBufferUtilizedAmt(corpBufferUtilizedAmt);
							}
							
						}
					}
					
					if(totalApprovedAmt != null){
						totalApprovedAmt.setValue(min.toString());
					}
					
					if(bean.getIsReverseAllocation()) {
						Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(totalApprovedAmt.getValue());
						Integer cValue = SHAUtils.getIntegerFromStringWithComma(newMedicalDecisionTableObj.dummyField.getValue()); 
						if(cValue.equals(approvedAmt) && !bean.getReverseAmountConsidered().equals(String.valueOf(approvedAmt))) {
							bean.setIsReverseAllocation(false);
							newMedicalDecisionTableObj.deleteReverseAllocation();
						}
					}
					if(bean.getNewIntimationDTO() != null) {
						Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
						if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
								bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
							min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
							bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
							if(!bean.getIsReverseAllocation()) {
								amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
								approvedAmtField.setValue(String.valueOf(totalApprovedAmtValue));
								bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
								if(totalApprovedAmt != null){
									totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
								}
							}  else {
								if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
								}
								Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
								amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
								if(totalApprovedAmt != null){
									totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
								}
								if(totalApprovedAmt != null){
									totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
								}
								bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
							}
							
						} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
								bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
							
							bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
							if(!bean.getIsReverseAllocation()) {
								min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
								amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
								approvedAmtField.setValue(String.valueOf(totalApprovedAmtValue));
								bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
								bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
								if(totalApprovedAmt != null){
									totalApprovedAmt.setValue(amountConsideredTable.doRevisedSuperSurplusCalculation().toString());
								}
							}  else {
								if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
								}
								//amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
								if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
									amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
								}
								Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
								if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
									Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
									amt =amt - doubleValueFromString;
								}
								//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
								//processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doRevisedSuperSurplusCalculation());
								//bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());									
							}
							
							bean.setAdmissableAmntOfCurrentClaim(null != amountConsideredTable.txtAdmissibleAmount? Double.valueOf(amountConsideredTable.txtAdmissibleAmount.getValue()): 0d);
						}
					}
				} else {
					if(bean.getNewIntimationDTO() != null) {
						Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
						if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
								bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
						     Integer min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
						     bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
								if(!bean.getIsReverseAllocation()) {
									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
									approvedAmtField.setValue(String.valueOf(totalApprovedAmtValue));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
//									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
								}  else {
									if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
									}
									Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d); 
									amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
//									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
								}
							
//							processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
						} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
								bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
						     
						     bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
								if(!bean.getIsReverseAllocation()) {
									Integer min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
									amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
									approvedAmtField.setValue(String.valueOf(totalApprovedAmtValue));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
//									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
									bean.getPreauthMedicalDecisionDetails().setReverseAllocatedMininumAmt(min.doubleValue());
								}  else {
									if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
									}
									//amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
									if(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt() != null){
										amountConsideredTable.txtAdmissibleAmount.setValue(bean.getPreauthMedicalDecisionDetails().getReverseAllocatedMininumAmt().toString());
									}
									Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
									if(bean.getAlreadySettlementAmt() != null && ! bean.getAlreadySettlementAmt().isEmpty()){
										Double doubleValueFromString = SHAUtils.getDoubleValueFromString(bean.getAlreadySettlementAmt());
										amt =amt - doubleValueFromString;
									}
									//amountConsideredTable.txtAdmissibleAmount.setValue(amt.toString());
//									processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
									//bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doRevisedSuperSurplusCalculation().doubleValue());
								}
							
//							processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
						}
					}
				}
			}
		});
		
		this.newMedicalDecisionTableObj.ambulanceChangeField.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -4052108705772482724L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField ambulanceTotal = (TextField) event.getProperty();
				if(amountConsideredTable != null) {
					amountConsideredTable.ambulanceChargeField.setValue(String.valueOf(SHAUtils.getIntegerFromStringWithComma(ambulanceTotal.getValue())));
				}
			}
		});

	}
	
	public void showClaimAmountDetails() {
		ViewClaimAmountDetils claimDetails = viewClaimAmountDetails.get();
		claimDetails.showDetails(this.bean, this.referenceData);
		UI.getCurrent().addWindow(claimDetails);
	}
	
	public void showBalanceSumInsured(String intimationId) {
		if (intimationId != null && !intimationId.equals("")) {
			viewDetails.getViewBalanceSumInsured(intimationId);
		}
	}
	
	public void showErrorMessage(){
		
//		Label label = new Label("Downsize amount should be less than Preauth approved Amount", ContentMode.HTML);
		Label label = new Label("Revision of Authorization amount should be less than Preauth approved Amount", ContentMode.HTML);
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
	
	private void setApprovedAmtvalue() {
		Integer totalApprovedAmt = this.newMedicalDecisionTableObj.getTotalCAmount();
		approvedAmtField.setValue(String
				.valueOf(totalApprovedAmt));
		Integer min = Math.min(
				amountConsideredTable.getMinimumValue(),
				totalApprovedAmt);
		if(bean.getIsNonAllopathic()) {
			min = Math.min(min, bean.getNonAllopathicAvailAmt());
		}
		if (bean.getStatusKey() != null && (bean.getStatusKey()
				.equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS))) {
			if(this.totalApprovedAmt != null){
				this.totalApprovedAmt.setValue(min.toString());
			}
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doSuperSurplusCalculation();
					this.totalApprovedAmt.setValue(doSuperSurplusCalculation.toString());
				}else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
					Integer doSuperSurplusCalculation = amountConsideredTable.doRevisedSuperSurplusCalculation();
					this.totalApprovedAmt.setValue(doSuperSurplusCalculation.toString());
				}
			}
		} else {
			if(bean.getNewIntimationDTO() != null) {
				Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
				if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("S")) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
//					processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
				} else if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
						bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && bean.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					amountConsideredTable.txtAdmissibleAmount.setValue(min.toString());
//					processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
				}
			}
		}
	}
	
public void manageNegotation(final Intimation intimationDtls,final NegotiationDetails negotiation,final String decision){
		
		ConfirmDialog dialog = ConfirmDialog.show(UI.getCurrent(),"Confirmation", "Negotation is under progress. Do You Want to Cancel or Update?",
		        "Update", "Cancel", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	 dialog.close();
		                	 showNagotiationRemarks(true, intimationDtls,negotiation,decision);
		                } else {
		                    dialog.close();
		                    showNagotiationRemarks(false, intimationDtls,negotiation,decision);
		                }
		            }
		        });
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}
public void showNagotiationRemarks(final Boolean isCancel,final Intimation intimationDtls
		,final NegotiationDetails negotiation,final String decision){

	VerticalLayout vLayout =  new VerticalLayout();
	final Window dialog = new Window();
	vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
	vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
	vLayout.setMargin(true);
	vLayout.setSpacing(true);
	final TextArea txtArea = new TextArea();
	txtArea.setMaxLength(4000);
	txtArea.setData(bean);
	//txtArea.setStyleName("Boldstyle");
	txtArea.setNullRepresentation("");
	if(!isCancel){
		txtArea.setValue(negotiation.getNegotiationRemarks());
	}
	//txtArea.setSizeFull();
	
	
//	if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
		txtArea.setRows(25);
		txtArea.setHeight("30%");
		txtArea.setWidth("100%");
		dialog.setHeight("65%");
    	dialog.setWidth("65%");
		txtArea.setReadOnly(false);
//	}
	
	final TextField amtToNegotiatedFild = new TextField("Amount to be Negotiated");
	amtToNegotiatedFild.setValue(negotiation.getNegotiationAmt() != null ? String.valueOf(negotiation.getNegotiationAmt()) : "0");
	FormLayout negAmt = new FormLayout(amtToNegotiatedFild);
	negAmt.setVisible(false);
	
	
	Button okBtn = new Button("OK");
	okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	vLayout.addComponent(negAmt);
	vLayout.addComponent(txtArea);
	vLayout.addComponent(okBtn);
	vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
	
	
	String strCaption = "";
	
	if(isCancel){
		strCaption = "Cancel Remarks";
		negAmt.setVisible(false);
	}else{
		strCaption = "Update Remarks";
		negAmt.setVisible(true);
	}
	dialog.setCaption(strCaption);
			
	
	dialog.setClosable(true);
	
	dialog.setContent(vLayout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.setDraggable(true);
	
	dialog.addCloseListener(new Window.CloseListener() {
		
		@Override
		public void windowClose(CloseEvent e) {
			if(null != decision && SHAConstants.DOWNSIZE_PRE_AUTH.equalsIgnoreCase(decision)){
			downSizeFormLayout.removeAllComponents();
			downSizeFormLayout.addComponent(downSizeFormLayout());
//				bean.setIsDownsizeOrEscalate(true);
			wizard.getFinishButton().setEnabled(false);
			wizard.getNextButton().setEnabled(true);
		}
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
			if(txtArea.getValue() != null && ! txtArea.getValue().isEmpty() && !StringUtils.isBlank(txtArea.getValue())){
				if(isCancel){
					Status status = new Status();
					status.setKey(ReferenceTable.NEGOTIATION_CANCELLED);
					negotiation.setStatusId(status);
					negotiation.setNegotiationCancelRemarks(txtArea.getValue());
				}else{
					negotiation.setCashlessKey(bean.getKey());
					negotiation.setNegotiationRemarks(txtArea.getValue());
					negotiation.setNegotiationAmt(Double.valueOf(amtToNegotiatedFild.getValue()));
				}
				
				fireViewEvent(DownSizePreauthWizardPresenter.NEGOTIATION_CANCEL_OR_UPDATE, negotiation);
				SHAUtils.buildNegotiationSuccessLayout(getUI(),isCancel);
				
				if(null != decision && SHAConstants.DOWNSIZE_PRE_AUTH.equalsIgnoreCase(decision)){
					downSizeFormLayout.removeAllComponents();
					downSizeFormLayout.addComponent(downSizeFormLayout());
	//				bean.setIsDownsizeOrEscalate(true);
					wizard.getFinishButton().setEnabled(false);
					wizard.getNextButton().setEnabled(true);
				}

				dialog.close();
			}else{
				showErrorMessage("Please Enter Remarks");
			}
		}
	});	
}
public void showErrorMessage(String eMsg) {
	Label label = new Label(eMsg, ContentMode.HTML);
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
	
	dialog.show(getUI().getCurrent(), null, true);
}


private BeanItemContainer<SelectValue> getCopayValues(PreauthDTO dto) {
	 BeanItemContainer<SelectValue> coPayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
	 	List<String> coPayPercentage = new ArrayList<String>();
	    for (Double string : dto.getProductCopay()) {
	    	coPayPercentage.add(String.valueOf(string));
		}
	    
	    //Long i = 0l;
	    SelectValue value = null;
	    for (String string : coPayPercentage) {
	    	value = new SelectValue();
	    	String[] copayWithPercentage = string.split("\\.");
			String copay = copayWithPercentage[0].trim();
		    value.setId(Long.valueOf(copay));
	    	value.setValue(string);
	    	coPayContainer.addBean(value);
	    	//i++;
		}
	    
	    return coPayContainer;
}

	public void setRemarks(MasterRemarks remarks) {
		String remarksValue =null;
		String insuredRemarks = null;
		if (remarks != null) {
			remarksValue = remarks.getRemarks() != null ? remarks.getRemarks() : "";
		}
		if (remarks != null) {
			insuredRemarks = remarks.getInsuredRemarks() != null ? remarks.getInsuredRemarks() : "";
		}
	
		if(downsizeRemarks != null){
			downsizeRemarks.setValue(remarksValue);
		}
		if (downsizeInsuredRemarks != null){
			downsizeInsuredRemarks.setValue(insuredRemarks);
		}
		
	}
	
	public  void remarksPopupListener(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForMedicalRemarks(searchField, getShortCutListenerForRemarks(searchField));
	    
	  }

	public  void handleShortcutForMedicalRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
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
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				txtArea.setData(bean);
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					txtArea.setReadOnly(false);
				
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						
						if(("insDwnSizeRmrks").equalsIgnoreCase(txtFld.getId()) || ("hospDwnSizeRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());					
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				
				String strCaption = "";
				
				if(("insDwnSizeRmrks").equalsIgnoreCase(txtFld.getId())){
//			    	strCaption = "Downsize Remarks For Insured";
					strCaption = "Revision Insured Remarks";
			    }
			    else if(("hospDwnSizeRmrks").equalsIgnoreCase(txtFld.getId())){
//			    	strCaption = "Downsize Remarks For Hospital";
			    	strCaption = "Revision Hospital Remarks";
			    }
				dialog.setCaption(strCaption);
						
				
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
	
	 public Long getApprovalAmtForRRC(){
		 if(newMedicalDecisionTableObj !=null){
			 return Long.valueOf(newMedicalDecisionTableObj.getTotalCAmount());
		 }
		 return 0l;
	 }
}
