package com.shaic.claim.negotiation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.cashlessprocess.downsize.wizard.DownSizePreauthWizardPresenter;
import com.shaic.claim.common.RevisedMedicalDecisionTable;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
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
import com.shaic.domain.MasterService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class NegotiationPreauthDataExtractionPage extends ViewComponent implements WizardStep<PreauthDTO> {

	@Inject
	private Instance<PreviousPreAuthDetailsTable> preauthPreviousDetailsPage;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private PreauthDTO preauthDto;
	
	@Inject
	private PreviousPreAuthService previousPreAuthService;
	
	private GWizard wizard;
	
	private PreviousPreAuthDetailsTable objPreviousPreAuthDetailsTable;
	
	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;

	private AmountConsideredTable amountConsideredTable;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	@Inject
	private Instance<RevisedMedicalDecisionTable> newMedicalDecisionTableInstance;
	
	@Inject
	private Instance<ViewClaimAmountDetils> viewClaimAmountDetails;
	
	@Inject
	private ViewDetails viewDetails;
	
	private RevisedMedicalDecisionTable newMedicalDecisionTableObj;
	
	private List<MedicalDecisionTableDTO> medicalDecisionTableList;
	
	private List<PreviousPreAuthTableDTO> previousPreauthTableList;
	
	Map<String, Object> sublimitCalculatedValues;
	
	private String diagnosisName;
	
	private TextField downSizedAmt;
	
	private TextField medicalRemarks;
	
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
	
	private TextField doctorNote;
	
	private Button intiatePEDEndorsementButton;
	
	private VerticalLayout wholeVLayout;
	
	private Button downsizePreauthBtn;
	
	private FormLayout downSizeFormLayout;
	
	private Button showclaimAmtDetailsBtn;

	private Button showclaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;
	
	private Button esclateClaimBtn;
	
	private Boolean isEsclateClaim=false;
	
	Map<String, Object> referenceData;
	
	private TextField approvedAmtField;
	
	private TextField totalApprovedAmt;
	
	private TextField preauthApprovedAmtTxt;
	
	private TextField consideredAmtTxt;
	
	private TextField nonAllopathicTxt;
	
	private TextField negotiationDeduction;
	
	private TextField amtAfterNegotiation;
	
	private TextField preauthDownsizeAmt;
	
	private VerticalLayout optionCLayout;
	
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	private Double amount=0.0;

	private BeanItemContainer<ExclusionDetails> exlusionContainer;
	
	private Button tmpViewBtn;
	
	private FormLayout escalateForm;
	
	@EJB
	private MasterService masterService;
	
	private File file;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	private Boolean isDownSized = false;
	
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
		fireViewEvent(NegotiationPreauthRequestPresenter.SETUP_REFERENCE_DATA,
				bean);
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	@Override
	public String getCaption() {
		return "Downsize Pre-auth";
	}

	@Override
	public Component getContent() {
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		intiatePEDEndorsementButton = new Button("Initiate PED Endorsement");
		HorizontalLayout buttonHLayout = new HorizontalLayout(intiatePEDEndorsementButton);
		buttonHLayout.setMargin(true);
		buttonHLayout.setComponentAlignment(intiatePEDEndorsementButton, Alignment.MIDDLE_RIGHT);
		
		consideredAmtTxt = new TextField("Amount Considered");
		consideredAmtTxt.setReadOnly(false);
		consideredAmtTxt.setValue(this.bean.getAmountConsidered());
		consideredAmtTxt.setReadOnly(true);
		
		FormLayout consideredAmtForm = new FormLayout(consideredAmtTxt);
		consideredAmtForm.setMargin(false);
		
	
		
		
		
		//For intiate ped endorsement button.
		
		showclaimAmtDetailsBtn = new Button("View");
		showclaimAmtDetailsBtnDuplicate = new Button("View");
		showclaimAmtDetailsBtn.setStyleName("link");
		showclaimAmtDetailsBtnDuplicate.setStyleName("link");
		amountConsideredViewButton = new Button("View");
		amountConsideredViewButton.setStyleName("link");
		
		FormLayout consideredViewForm  = new FormLayout(amountConsideredViewButton);
		consideredViewForm.setMargin(false);
		
		HorizontalLayout consideredFormLayout = new HorizontalLayout(consideredAmtForm,consideredViewForm);
		
		objPreviousPreAuthDetailsTable = preauthPreviousDetailsPage.get();
		objPreviousPreAuthDetailsTable.init("Pre-auth Summary", false, false);
		objPreviousPreAuthDetailsTable.setTableList(this.bean.getPreviousPreauthTableDTO());	
		
		this.newMedicalDecisionTableObj=this.newMedicalDecisionTableInstance.get();
		this.newMedicalDecisionTableObj.init(this.bean);
		
		if(this.referenceData != null) {
			
			 this.newMedicalDecisionTableObj.setReferenceData(this.referenceData);
		}
		
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
				balanceSumInsuredLbl, showclaimAmtDetailsBtn);
		viewLayout2.setSpacing(true);

		this.amountConsideredTable = this.amountConsideredTableInstance.get();
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2, optionCLayout, false, false);
		
		setValueToMedicalDecisionValues();
		
		MedicalDecisionTableDTO dto = new MedicalDecisionTableDTO();
		dto.setReferenceNo("Residual Treatment / Procedure Amount");
		dto.setApprovedAmount(this.preauthDto.getResidualAmountDTO().getApprovedAmount() != null ? this.preauthDto.getResidualAmountDTO().getApprovedAmount().toString() : "");
		downSizeFormLayout=new FormLayout();
		
		wholeVLayout = new VerticalLayout(consideredFormLayout,buttonHLayout, this.newMedicalDecisionTableObj,this.amountConsideredTable,buildDownSizePreAuthButtonLayout(),downSizeFormLayout);
		wholeVLayout.setComponentAlignment(buttonHLayout, Alignment.TOP_RIGHT);
		wholeVLayout.setComponentAlignment(consideredFormLayout, Alignment.TOP_LEFT);
		wholeVLayout.setSpacing(true);
		
		if(bean.getStatusKey() != null && bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
			downSizeFormLayout.removeAllComponents();
			downSizeFormLayout.addComponent(downSizeFormLayout());
		}
		
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
		
		hLayout.addComponents(downsizePreauthBtn);
		hLayout.setSpacing(true);
		addListener();
		return hLayout;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {

		 this.referenceData = referenceData;
//		 this.newMedicalDecisionTableObj.setReferenceData(referenceData);
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
				
				dto.setCoPayType(pedValidationTableDTO.getCoPayTypeId());
//				dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
//				dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
//				dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
//				
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
				dto.setCoPayPercentage(procedureDTO.getCopay());
				
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
					}
//					caluculationInputValues.put("preauthKey", 0l);
					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
					caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY,bean.getClaimDTO().getKey());
					
					bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
					
					fireViewEvent(
							NegotiationPreauthRequestPresenter.NEGOTIATION_SUM_INSURED_CALCULATION,
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
	
	private void addMedicalDecisionTableFooterListener() {

		this.newMedicalDecisionTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 4843316375590220412L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmtValue = SHAUtils
								.getIntegerFromString((String) event
										.getProperty().getValue());
						approvedAmtField.setValue(String
								.valueOf(totalApprovedAmtValue));
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmtValue);
						if(bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						if ((bean.getStatusKey() != null && bean.getStatusKey()
								.equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS))) {
							if(totalApprovedAmt != null){
								Double lastapprovedAmt = bean.getPreauthDataExtractionDetails().getTotalApprAmt();
								if(min <= lastapprovedAmt) {
									totalApprovedAmt.setValue(min.toString());
								} else {
									showErrorMessageForClaimedAmt();
								}
								
							}
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
									if(totalApprovedAmt != null){
										totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
									}
								}
							}
						} else {
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
									amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
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
				if (bean.getStatusKey() != null && (bean.getStatusKey()
						.equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS))) {
					Integer min = Math.min(
							amountConsideredTable.getMinimumValue(),
							totalApprovedAmtValue);
					if(bean.getIsNonAllopathic()) {
						min = Math.min(min, bean.getNonAllopathicAvailAmt());
					}
					if(totalApprovedAmt != null){
						totalApprovedAmt.setValue(min.toString());
					}
					if(bean.getNewIntimationDTO() != null) {
						Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
						if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
							min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
							amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
							if(totalApprovedAmt != null){
								totalApprovedAmt.setValue(amountConsideredTable.doSuperSurplusCalculation().toString());
							}
						}
					}
				} else {
					if(bean.getNewIntimationDTO() != null) {
						Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
						if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
						     Integer min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmtValue);
							amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
//							processPreauthButtonObj.setApprovedAmtValue(amountConsideredTable.doSuperSurplusCalculation());
						}
					}
				}

				
			}
		});

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
	@Override
	public boolean onAdvance() {
		setResidualAmtToDTO();
		
		return validatePage();
	}

	@Override
	public boolean onBack() {
		downSizeFormLayout.removeAllComponents();
		bean.setStatusKey(null);
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
		
		if(!isDownSized){
			hasError = true;
			eMsg.append("Please Select Downsize Pre auth to proceed further</br>");
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
				
			} catch (CommitException e) {
				e.printStackTrace();
			}
		 }
		 
		  if(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null 
	        		&& this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null
	        		&& this.bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
	        	Double TotalApprovedAmount = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
	        	if(negotiationDeduction.getValue() != null){
	        		this.bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleFromStringWithComma(preauthDownsizeAmt.getValue()));
	        	}
	        	Double downsizeAmt = this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt();
	        	if(preauthDownsizeAmt.getValue() != null){
	        		bean.getPreauthMedicalDecisionDetails().setDownsizedAmt(preauthDownsizeAmt.getValue() != null ? SHAUtils.getDoubleValueFromString(preauthDownsizeAmt.getValue()) :0d);
	        	}
	        	
	        	if(TotalApprovedAmount <= downsizeAmt){
	        		hasError = true;
	        		eMsg.append("Downsize amount should be less than Preauth approved Amount. </br>");
	        	}
	        	
	        	Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(preauthDownsizeAmt.getValue());
				Integer cValue = SHAUtils.getIntegerFromStringWithComma(this.newMedicalDecisionTableObj.dummyField.getValue());
				if(approvedAmt < cValue){
					hasError = true;
					this.bean.setIsReverseAllocation(true);
					this.newMedicalDecisionTableObj.setReverseAllocationColumn(String.valueOf(approvedAmt));
					eMsg.append("Please enter Reverse Allocation Amount.</br>");
				}
				
				if(approvedAmt <= 0 || approvedAmt == null){
					hasError = true;
					eMsg.append("Preauth Downsize amount should not be zero. </br>");
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
				this.binder.commit();
			    this.bean.getPreauthMedicalDecisionDetails().setMedicalDecisionTableDTO(this.newMedicalDecisionTableObj.getValues());
			    this.bean.getPreauthMedicalDecisionDetails().setDownsizedAmt(preauthDownsizeAmt.getValue() != null ? SHAUtils.getDoubleValueFromString(preauthDownsizeAmt.getValue()) :0d);
			    
			    this.bean.setFinalTotalApprovedAmount(this.bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt());
			    
			    if(this.bean.getStatusKey() != null && this.bean.getStatusKey().equals(ReferenceTable.DOWNSIZE_ESCALATION_STATUS)){
					Integer min = Math.min(amountConsideredTable.getMinimumValue(),
							SHAUtils.getIntegerFromString(this.newMedicalDecisionTableObj.dummyField
									.getValue()));
					this.bean.getPreauthMedicalDecisionDetails().setInitialApprovedAmt(min != null ? Double.valueOf(min) : 0d);
					this.bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min != null ? Double.valueOf(min) : 0d);
				}
			    
			    if(approvedAmtField != null && approvedAmtField.getValue() != null){
					Double approvedAmount = SHAUtils.getDoubleValueFromString(approvedAmtField.getValue());
					this.bean.setSublimitAndSIAmt(approvedAmount);
				}
			    
			    //added for jira IMSSUPPOR-28726
			    fireViewEvent(
						NegotiationPreauthRequestPresenter.PREAUTH_APPROVED_DATE,
						bean);

			    return true;
		   
			} catch (CommitException e) {
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
	
	public void showErrorMessage(){
		
		Label label = new Label("Negotiation amount should be less than Preauth approved Amount", ContentMode.HTML);
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
	
	public void addListener(){
		downsizePreauthBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				downSizeFormLayout.removeAllComponents();
				Integer min = Math.min(amountConsideredTable.getMinimumValue(),
						SHAUtils.getIntegerFromString(newMedicalDecisionTableObj.dummyField
								.getValue()));
		
				Double lastapprovedAmt = bean.getPreauthDataExtractionDetails().getTotalApprAmt();
				Double minimumAmt  = min.doubleValue();
				if(minimumAmt <= lastapprovedAmt) {
					downSizeFormLayout.addComponent(downSizeFormLayout());
					isDownSized = true;
				} else {
					showErrorMessageForClaimedAmt();
					downSizeFormLayout.removeAllComponents();
					wizard.getNextButton().setEnabled(false);
				}
//				bean.setIsDownsizeOrEscalate(true);
				wizard.getFinishButton().setEnabled(false);
//				wizard.getNextButton().setEnabled(true);
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
						alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
					} else {
						createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
					}
				}
			});
		
		amountConsideredViewButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2259148886587320228L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						NegotiationPreauthRequestPresenter.VIEW_NEG_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});
		
		showclaimAmtDetailsBtnDuplicate.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2259148886587320228L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(
						NegotiationPreauthRequestPresenter.VIEW_NEG_CLAIMED_AMOUNT_DETAILS,
						true);
			}
		});
		
		showclaimAmtDetailsBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4478247898237407113L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (bean.getNewIntimationDTO() != null
						&& bean.getNewIntimationDTO().getIntimationId() != null
						&& !bean.getNewIntimationDTO().getIntimationId()
								.equals("")) {
					fireViewEvent(
							NegotiationPreauthRequestPresenter.VIEW_NEG_BALANCE_SUM_INSURED_DETAILS,
							bean.getNewIntimationDTO().getIntimationId());
				}
			}
		});
	}
	
	public FormLayout downSizeFormLayout()
	{
		bean.setStatusKey(ReferenceTable.DOWNSIZE_APPROVED_STATUS);

		unbindField(negotiationDeduction);
		unbindField(amtAfterNegotiation);
		unbindField(preauthDownsizeAmt);
		unbindField(downsizeRemarks);
		unbindField(downsizeInsuredRemarks);
		unbindField(approvedAmtField);
		unbindField(totalApprovedAmt);
		unbindField(downsizeRemarks);
		unbindField(doctorNote);
		unbindField(cmbSpecialistType);
		
		mandatoryFields.remove(downsizeRemarks);

		
		Integer min = Math.min(amountConsideredTable.getMinimumValue(),
						SHAUtils.getIntegerFromString(this.newMedicalDecisionTableObj.dummyField
								.getValue()));
		
		Double lastapprovedAmt = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt();
		Double minimumAmt  = min.doubleValue();
		
		totalApprovedAmt = (TextField) binder.buildAndBind("Approved Amount", "initialTotalApprovedAmt", TextField.class);
		totalApprovedAmt.setNullRepresentation("");
		totalApprovedAmt.setEnabled(false);
		if(minimumAmt <= lastapprovedAmt) {
			if(!bean.getIsReverseAllocation()) {
			totalApprovedAmt.setValue(min.toString());
			}
		} else {
			showErrorMessageForClaimedAmt();
		}
		
		negotiationDeduction = (TextField) binder.buildAndBind("Negotiated Deduction","negotiationDeductionAmt", TextField.class);
		negotiationDeduction.setNullRepresentation("");
		
		CSValidator negDeductionValidator = new CSValidator();
		negDeductionValidator.extend(negotiationDeduction);
		negDeductionValidator.setRegExp("^[0-9]*$");
		negDeductionValidator.setPreventInvalidTyping(true);
		
		negotiationDeduction.addBlurListener(getAmountAfterNegotiation());
		
		amtAfterNegotiation = (TextField) binder.buildAndBind("Amount after Negotiation","amtAfterNegotiationAmt", TextField.class);
		amtAfterNegotiation.setNullRepresentation("");
		amtAfterNegotiation.setEnabled(false);
//		amtAfterNegotiation.setValue(String.valueOf(amtAftNeg.longValue()));
		
		CSValidator amtAftNegValidator = new CSValidator();
		amtAftNegValidator.extend(amtAfterNegotiation);
		amtAftNegValidator.setRegExp("^[0-9]*$");
		amtAftNegValidator.setPreventInvalidTyping(true);
		
		preauthDownsizeAmt = (TextField) binder.buildAndBind("Preauth Downsized Amount","preauthDownsizeAmt", TextField.class);
		preauthDownsizeAmt.setNullRepresentation("");
		preauthDownsizeAmt.setEnabled(false);
//		preauthDownsizeAmt.setValue(String.valueOf(amtAftNeg.longValue()));
		
		CSValidator preauthDownsizeAmtValidator = new CSValidator();
		preauthDownsizeAmtValidator.extend(preauthDownsizeAmt);
		preauthDownsizeAmtValidator.setRegExp("^[0-9]*$");
		preauthDownsizeAmtValidator.setPreventInvalidTyping(true);
		
		
		downsizeRemarks = (TextArea)binder.buildAndBind("Downsize Remarks","downsizeRemarks",TextArea.class);
		// As per Jatin Mail remarks field size extend to 1000
		downsizeRemarks.setMaxLength(1000);
		downsizeRemarks.setWidth("400px");
		SHAUtils.handleTextAreaPopupDetails(downsizeRemarks,null,getUI(),SHAConstants.NEGOTIATION_DOWNSIZE_REMARKS);
		
		
		downsizeInsuredRemarks = (TextArea)binder.buildAndBind("Downsize Insured Remarks","downsizeInsuredRemarks",TextArea.class);
		// As per Jatin Mail remarks field size extend to 1000
		downsizeInsuredRemarks.setMaxLength(1000);
		downsizeInsuredRemarks.setWidth("400px");
		SHAUtils.handleTextAreaPopupDetails(downsizeInsuredRemarks,null,getUI(),SHAConstants.NEGOTIATION_DOWNSIZE_INSURED_REMARKS);
		
		mandatoryFields.add(totalApprovedAmt);
		mandatoryFields.add(downsizeRemarks);
		mandatoryFields.add(amtAfterNegotiation);
		mandatoryFields.add(preauthDownsizeAmt);
		mandatoryFields.add(negotiationDeduction);
		showOrHideValidation(false);
		
		FormLayout downSizeForm=new FormLayout(totalApprovedAmt, negotiationDeduction, amtAfterNegotiation,preauthDownsizeAmt,downsizeRemarks,downsizeInsuredRemarks);
		
		return downSizeForm;
	}
	
	private BlurListener getAmountAfterNegotiation(){
		
			BlurListener listener = new BlurListener() {
				private static final long serialVersionUID = 1L;
		
				@Override
				public void blur(BlurEvent event){
					
					TextField value = (TextField) event.getComponent();
					if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
						Integer negotiationAmt = Integer.valueOf(value.getValue());
						Integer approvedAmt = SHAUtils.getIntegerFromStringWithComma(totalApprovedAmt.getValue());
						Integer amtAfterNeg = approvedAmt - negotiationAmt;
						if(amtAfterNeg != null){
							if(amtAfterNeg <= 0) {
								showErrorMessage();
							} else {
								amtAfterNegotiation.setValue(amtAfterNeg.toString());
								preauthDownsizeAmt.setValue(amtAfterNeg.toString());
							}
						}
					}
					
				}
			
		};
		return listener;
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
	
	public void showErrorMessageForClaimedAmt(){
		
		Label label = new Label("Claimed amount should be less than Preauth approved Amount", ContentMode.HTML);
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
				createPEDScreen(preauthKey, intimationKey, policyKey, claimKey);
			}
		});
		return true;
	}
	
	private void createPEDScreen(Long preauthKey, Long intimationKey,
			Long policyKey, Long claimKey) {
		ViewPEDRequestWindow viewPEDRequest = viewPedRequest.get();
		viewPEDRequest.initView(bean,preauthKey,intimationKey,policyKey,claimKey,ReferenceTable.DOWNSIZE_STAGE,false);						
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
	
}
