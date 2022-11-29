package com.shaic.claim.misc.updatesublimit.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.misc.updatesublimit.SearchUpdateSublimitTableDTO;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.reimbursement.financialapproval.pages.billingprocess.FinancialProcessPagePresenter;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class UpdateSublimitWizardPage extends ViewComponent {
	
	@Inject
	private Instance<UpdateSublimitMedicalDecisionTable> sublimitmedicalDecisionTableInstance;
	
	private UpdateSublimitMedicalDecisionTable sublimitmedicalDecisionTable;
	
	@Inject
	private Instance<AmountConsideredTable> amountConsideredTableInstance;
	
	private AmountConsideredTable amountConsideredTable;
	
	@Inject
	private ViewBillSummaryPage viewBillSummary;
	
	@Inject
	private ViewDetails viewDetails;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	private TextArea txtsublimitUpdateRemarks;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private TextField approvedAmtField;
	
	private PreauthDTO  bean;
	
	private BeanFieldGroup<PreauthDTO> binder;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Map<String, Object> referenceData;
	
	private VerticalLayout optionCLayout;
	
	private Button amountConsideredViewButton;
	
	private Button showBalanceSumInsured;
	
	private Button showClaimAmtDetailsBtnDuplicate;
	
	private Window popup;
	
	@PostConstruct
	protected void initView(){
		
	}
	
	public void init(PreauthDTO  searchSubmlimitDto){
		
		this.bean = searchSubmlimitDto;
		this.binder = new BeanFieldGroup<PreauthDTO>(PreauthDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		sublimitmedicalDecisionTable = sublimitmedicalDecisionTableInstance.get();
		sublimitmedicalDecisionTable.init(this.bean,SHAConstants.FINANCIAL);
		
		showBalanceSumInsured = new Button("View");
		showClaimAmtDetailsBtnDuplicate = new Button("View");
		amountConsideredViewButton = new Button("View");
		showBalanceSumInsured.setStyleName("link");
		showClaimAmtDetailsBtnDuplicate.setStyleName("link");

		amountConsideredViewButton.setStyleName("link");
		
		approvedAmtField = new TextField();
		approvedAmtField.setEnabled(false);
		HorizontalLayout approvedFormLayout = new HorizontalLayout(new Label(
				"C) Sub limits, </br> Package &  </br> SI Restriction </br> Amount",
				ContentMode.HTML), approvedAmtField);
		optionCLayout = new VerticalLayout(approvedFormLayout);
		
		Label amtClaimedDetailsLbl = new Label("A) Amount Considered");
		HorizontalLayout viewLayout1 = new HorizontalLayout(
				amtClaimedDetailsLbl, showClaimAmtDetailsBtnDuplicate);
		viewLayout1.setSpacing(true);

		Label balanceSumInsuredLbl = new Label("B) Balance Sum Insured");

		HorizontalLayout viewLayout2 = new HorizontalLayout(
				balanceSumInsuredLbl, showBalanceSumInsured);
		viewLayout2.setSpacing(true);

		this.amountConsideredTable = this.amountConsideredTableInstance.get();
		this.amountConsideredTable.setPresenterString(SHAConstants.FINANCIAL);
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2, optionCLayout, false, true);
	
	
		
		amountConsideredViewButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;
			

			@Override
			public void buttonClick(ClickEvent event) {
				popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				viewBillSummary.init(bean,bean.getKey(), true);
				Panel mainPanel = new Panel(viewBillSummary);
		        mainPanel.setWidth("2000px");
				popup.setContent(mainPanel);
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
		
		showClaimAmtDetailsBtnDuplicate.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				viewBillSummary.init(bean,bean.getKey(), true);
				popup.setContent(viewBillSummary);
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
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
				
			}
		});
		
		showBalanceSumInsured.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				viewDetails.getViewBalanceSumInsured(bean.getNewIntimationDTO().getIntimationId());
				
			}
		});
		
		txtsublimitUpdateRemarks = (TextArea)binder.buildAndBind("Remarks to Update Sublimit", "sublimitUpdateRemarks", TextArea.class);
		txtsublimitUpdateRemarks.setRequired(true);
		txtsublimitUpdateRemarks.setMaxLength(4000);
		txtsublimitUpdateRemarks.setWidth("400px");
		
		updateSublimitCommentsChangeListener(txtsublimitUpdateRemarks,null);
		
		FormLayout txtremarks = new FormLayout();
		txtremarks.addComponent(txtsublimitUpdateRemarks);
		
		
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		FormLayout dummyForm = new FormLayout();
		dummyForm.setWidth("50%");
		dummyForm.setHeight("45px");
		
		
		VerticalLayout vLayout = new VerticalLayout();
		//Vaadin8-setImmediate() vLayout.setImmediate(false);
		
		HorizontalLayout formHor = new HorizontalLayout(dummyForm,vLayout);
		
		HorizontalLayout buttonHor = new HorizontalLayout(submitBtn,cancelBtn);
		buttonHor.setSpacing(true);
		buttonHor.setMargin(false);
		
		VerticalLayout mainVerticalOne = new VerticalLayout(formHor,sublimitmedicalDecisionTable,dummyForm,this.amountConsideredTable,dummyForm,txtremarks);
		mainVerticalOne.setSpacing(true);

		
		VerticalLayout mainLayoutTwo = new VerticalLayout(formHor,dummyForm,buttonHor);
		mainLayoutTwo.setComponentAlignment(buttonHor,Alignment.BOTTOM_CENTER);
		
		VerticalLayout mainVertical = new VerticalLayout(mainVerticalOne,mainLayoutTwo);
		
		showOrHideValidation(false);
		
		addListener();
		
		setCompositionRoot(mainVertical); 
		
		approvedAmtField.setValue(String.valueOf(bean.getAmountConsidedAfterCoPay().intValue()));
		
	}
	
	public void setupReferences(Map<String, Object> referenceData) {
		List<DiagnosisProcedureTableDTO> values = new ArrayList<DiagnosisProcedureTableDTO>();
		this.bean.setDiagnosisProcedureDtoList(values);
		this.sublimitmedicalDecisionTable.setReferenceData(referenceData);
		
		this.referenceData = referenceData;
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean    
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();
		
		String previousApprovedAmt = SHAUtils.getPreviousApprovedAmt(this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList(), this.bean
				.getPreauthMedicalProcessingDetails()
				.getProcedureExclusionCheckTableList(), this.bean.getResidualAmountDTO());
		Boolean isSetZero = false;
		if(SHAUtils.getDoubleFromString(this.bean.getAmountConsidered()) < SHAUtils.getDoubleFromString(previousApprovedAmt)) {
			isSetZero = true;
		}
		
		Boolean isAmbulanceReset = false;
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			String ambulancePreviousAmt = SHAUtils.getPreviousApprovedAmtForAmbulance(this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList(), this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList());
			if(this.bean.getAmbulanceLimitAmount() != null && ambulancePreviousAmt != null){
				if(this.bean.getAmbulanceLimitAmount() < SHAUtils.getDoubleFromString(ambulancePreviousAmt)) {
					isAmbulanceReset = true;
				}
			}
		}

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			DiagnosisProcedureTableDTO dto = null;
			SelectValue value = null;
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				dto = new DiagnosisProcedureTableDTO();
				if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = pedValidationTableDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					//if (isPaymentAvailable) {
						List<PedDetailsTableDTO> pedList = pedValidationTableDTO
								.getPedList();
						if (!pedList.isEmpty()) {
							for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

								List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
										.getExclusionAllDetails();
								String paymentFlag = "y";
								if(exclusionAllDetails != null) {
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
								}

								if (paymentFlag.toLowerCase().equalsIgnoreCase(
										"n")) {
									isPaymentAvailable = false;
									dto.setIsPedExclusionFlag(false);
									break;
								}
							}
						}
					//}

					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setReverseAllocatedAmt(0);
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
				List<PedDetailsTableDTO> pedList = pedValidationTableDTO.getPedList();
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
					if(pedDetailsTableDTO.getCopay() != null) {
						dto.setCoPayPercentage(pedDetailsTableDTO.getCopay());
					}
				}

				if (pedValidationTableDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSublimitAmt()).toString());
				}
				dto.setPackageAmt("NA");
				dto.setDiagnosisDetailsDTO(pedValidationTableDTO);
				dto.setAmountConsidered(pedValidationTableDTO.getAmountConsideredAmount() != null ? pedValidationTableDTO.getAmountConsideredAmount().intValue() : 0);
				
				dto.setCoPayAmount(pedValidationTableDTO.getCopayAmount() != null ? pedValidationTableDTO.getCopayAmount().intValue() : 0);
				dto.setMinimumAmountOfAmtconsideredAndPackAmt(pedValidationTableDTO.getMinimumAmount() != null ? pedValidationTableDTO.getMinimumAmount().intValue() : 0);
				dto.setNetAmount(pedValidationTableDTO.getNetAmount() != null ? pedValidationTableDTO.getNetAmount().intValue() : 0);
				dto.setMinimumAmount(pedValidationTableDTO.getApprovedAmount() != null ? pedValidationTableDTO.getApprovedAmount().intValue() : 0);
				dto.setRemarks(pedValidationTableDTO.getApproveRemarks() != null ? pedValidationTableDTO.getApproveRemarks() : "");
				dto.setReverseAllocatedAmt(pedValidationTableDTO.getNetApprovedAmount() != null ? pedValidationTableDTO.getNetApprovedAmount().intValue() : 0);
				if(pedValidationTableDTO.getCopayPercentage() != null) {
					value = new SelectValue();
					value.setId(pedValidationTableDTO.getCopayPercentage().longValue());
					value.setValue( String.valueOf(pedValidationTableDTO.getCopayPercentage().longValue()));
					dto.setCoPayPercentage(value);
				}
				if(null != pedValidationTableDTO.getCoPayTypeId()){
					dto.setCoPayType(pedValidationTableDTO.getCoPayTypeId());
				}
				dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
				dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
				dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
				if(this.bean.getIsAmbulanceApplicable()){
					dto.setIsAmbulanceEnable(true);
				}
				
				if(isSetZero) {
					dto.setAmountConsidered(0);
					dto.setCoPayAmount(0);
					dto.setNetAmount(0);
					dto.setMinimumAmount(0);
					dto.setNetApprovedAmt(0);
					dto.setReverseAllocatedAmt(0);
					dto.setIsAmbChargeApplicable(false);
					dto.setAmbulanceCharge(0);
					dto.setAmtWithAmbulanceCharge(0);
				}
				if(dto.getAmountConsidered() == 0 ) {
					dto.setMinimumAmount(0);
					dto.setReverseAllocatedAmt(0);
				}
				
				if(isAmbulanceReset){
					
					dto.setIsAmbChargeApplicable(false);
					dto.setAmbulanceCharge(0);
					dto.setAmtWithAmbulanceCharge(0);
				}
				
				
				dto.setDiagOrProcedureFlag(SHAConstants.DIAGNOSIS);
				
				medicalDecisionDTOList.add(dto);
			}
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				dto = new DiagnosisProcedureTableDTO();
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
					dto.setIsPaymentAvailable(isPaymentAvailable);
				}
					//if(isPaymentAvailable) {
						if(procedureDTO.getExclusionDetails() != null && procedureDTO.getExclusionDetails().getValue() != null && !procedureDTO.getExclusionDetails().getValue().toLowerCase().equalsIgnoreCase("not applicable")) {
							isPaymentAvailable = false;
							dto.setIsPedExclusionFlag(false);
						}
					//}
					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setReverseAllocatedAmt(0);						
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				dto.setRestrictionSI("NA");
				
				dto.setPackageAmt("NA");
				if(procedureDTO.getPackageRate() != null && procedureDTO.getPackageRate() >= 0) {
					dto.setPackageAmt(procedureDTO.getPackageRate().toString());
				}
				
				if(procedureDTO.getCopay() != null) {
					dto.setCoPayPercentage(procedureDTO.getCopay());
				}
				if(null != procedureDTO.getCoPayTypeId()){
					dto.setCoPayType(procedureDTO.getCoPayTypeId());
				}

				if (procedureDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							procedureDTO.getSublimitAmount()).toString());
				}
				
				dto.setAmountConsidered(procedureDTO.getAmountConsideredAmount() != null ? procedureDTO.getAmountConsideredAmount().intValue() : 0);
				
				dto.setCoPayAmount(procedureDTO.getCopayAmount() != null ? procedureDTO.getCopayAmount().intValue() : 0);
				dto.setMinimumAmountOfAmtconsideredAndPackAmt(procedureDTO.getMinimumAmount() != null ? procedureDTO.getMinimumAmount().intValue() : 0);
				dto.setNetAmount(procedureDTO.getNetAmount() != null ? procedureDTO.getNetAmount().intValue() : 0);
				dto.setMinimumAmount(procedureDTO.getApprovedAmount() != null ? procedureDTO.getApprovedAmount().intValue() : 0);
				dto.setRemarks(procedureDTO.getApprovedRemarks() != null ? procedureDTO.getApprovedRemarks() : "");
				dto.setReverseAllocatedAmt(procedureDTO.getNetApprovedAmount() != null ? procedureDTO.getNetApprovedAmount().intValue() : 0);
				
				dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
				dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
				dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
				
				if(procedureDTO.getCopayPercentage() != null) {
					value = new SelectValue();
					value.setId(procedureDTO.getCopayPercentage().longValue());
					value.setValue( String.valueOf(procedureDTO.getCopayPercentage().longValue()));
					dto.setCoPayPercentage(value);
				}
				if(isSetZero) {
					dto.setAmountConsidered(0);
					dto.setCoPayAmount(0);
					dto.setNetAmount(0);
					dto.setMinimumAmount(0);
					dto.setNetApprovedAmt(0);
					dto.setReverseAllocatedAmt(0);
					dto.setIsAmbChargeApplicable(false);
					dto.setAmbulanceCharge(0);
					dto.setAmtWithAmbulanceCharge(0);
				}
				if(dto.getAmountConsidered() == 0 ) {
					dto.setMinimumAmount(0);
					dto.setReverseAllocatedAmt(0);
				}
				
				if(isAmbulanceReset){
					
					dto.setIsAmbChargeApplicable(false);
					dto.setAmbulanceCharge(0);
					dto.setAmtWithAmbulanceCharge(0);
				}
				
				
				if(this.bean.getIsAmbulanceApplicable()){
					dto.setIsAmbulanceEnable(true);
				}
				dto.setDiagOrProcedureFlag(SHAConstants.PROCEDURE);
				medicalDecisionDTOList.add(dto);
			}
			
			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());

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
						caluculationInputValues.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId", medicalDecisionDto.getProcedureDTO().getProcedureName() == null ? 0l : (medicalDecisionDto.getProcedureDTO().getProcedureName().getId() == null ? 0l : medicalDecisionDto.getProcedureDTO().getProcedureName().getId()));
						caluculationInputValues.put("referenceFlag", "P");
					}
					caluculationInputValues.put("preauthKey",  this.bean.getKey());

					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
					caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
					
					bean.setIsNonAllopathic((Boolean)caluculationInputValues.get(SHAConstants.IS_NON_ALLOPATHIC));
					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					
					if(medicalDecisionSize == 1){
						
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							if(this.bean.getAmbulanceLimitAmount() > 0){
								medicalDecisionDto.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								medicalDecisionDto.setIsAmbulanceEnable(true);
								medicalDecisionDto.setIsAmbChargeApplicable(true);
							}else{
								medicalDecisionDto.setIsAmbulanceEnable(false);
								medicalDecisionDto.setIsAmbChargeApplicable(false);
							}
						}
						
					}
					
					fireViewEvent(
							UpdateSublimitPresenter.SUM_INSURED_CALCULATION_FOR_SUBLIMIT,
							caluculationInputValues, medicalDecisionDto,this.bean);
				}
				
				DiagnosisProcedureTableDTO diagnosisDTO = new DiagnosisProcedureTableDTO();
				diagnosisDTO.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				diagnosisDTO.setPackageAmt("NA");
				diagnosisDTO.setSubLimitAmount("NA");
				diagnosisDTO.setRestrictionSI("NA");
				diagnosisDTO.setIsAmbulanceEnable(false);
				if(this.bean.getResidualAmountDTO() != null && this.bean.getResidualAmountDTO().getKey() != null) {
					ResidualAmountDTO residualAmountDTO = this.bean.getResidualAmountDTO();
					diagnosisDTO.setAmountConsidered(residualAmountDTO.getAmountConsideredAmount() != null ? residualAmountDTO.getAmountConsideredAmount().intValue() : 0);
					diagnosisDTO.setMinimumAmount(residualAmountDTO.getMinimumAmount() != null ? residualAmountDTO.getMinimumAmount().intValue(): 0);
					diagnosisDTO.setCoPayAmount(residualAmountDTO.getCopayAmount() != null ? residualAmountDTO.getCopayAmount().intValue() : 0);
					diagnosisDTO.setRemarks(residualAmountDTO.getRemarks() != null ? residualAmountDTO.getRemarks() : "");
					value = new SelectValue();
					value.setId(residualAmountDTO.getCopayPercentage() != null ? residualAmountDTO.getCopayPercentage().longValue() : 0l);
					value.setValue(residualAmountDTO.getCopayPercentage() != null ? String.valueOf(residualAmountDTO.getCopayPercentage().intValue())  : "0");
					
					diagnosisDTO.setCoPayPercentage(value);
					diagnosisDTO.setCoPayType(residualAmountDTO.getCoPayTypeId());
					diagnosisDTO.setNetAmount(residualAmountDTO.getNetAmount() != null ? residualAmountDTO.getNetAmount().intValue() : 0);
					diagnosisDTO.setNetApprovedAmt(residualAmountDTO.getApprovedAmount() != null ? residualAmountDTO.getApprovedAmount().intValue() : 0);
					diagnosisDTO.setReverseAllocatedAmt(residualAmountDTO.getNetApprovedAmount() != null ? residualAmountDTO.getNetApprovedAmount().intValue() : 0);
					if(isSetZero) {
						diagnosisDTO.setAmountConsidered(0);
						diagnosisDTO.setCoPayAmount(0);
						diagnosisDTO.setNetAmount(0);
						diagnosisDTO.setMinimumAmount(0);
						diagnosisDTO.setNetApprovedAmt(0);
						diagnosisDTO.setReverseAllocatedAmt(0);
					}
					if(diagnosisDTO.getAmountConsidered() == 0 ) {
						diagnosisDTO.setMinimumAmount(0);
						diagnosisDTO.setReverseAllocatedAmt(0);
					}
				}
				
				

				this.sublimitmedicalDecisionTable.addBeanToList(diagnosisDTO);
				this.bean.getDiagnosisProcedureDtoList().add(diagnosisDTO);
			}
//			if(bean.getIsNonAllopathic()) {
//				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
//			}
		} else {

			
			int size = filledDTO.size();
			
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : filledDTO) {
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					Boolean procedureStatus = SHAUtils.getProcedureStatusforConsiderForPayment(diagnosisProcedureTableDTO);
					if(!procedureStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(procedureStatus);	
					if(size == 2){
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							if(this.bean.getAmbulanceLimitAmount() > 0){
								diagnosisProcedureTableDTO.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								diagnosisProcedureTableDTO.setIsAmbulanceEnable(true);
								diagnosisProcedureTableDTO.setIsAmbChargeApplicable(true);
							}else{
								diagnosisProcedureTableDTO.setIsAmbulanceEnable(false);
								diagnosisProcedureTableDTO.setIsAmbChargeApplicable(false);
							}
						}
						
					}
					
					
				} else if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
					Boolean diagnosisStatus = SHAUtils.getDiagnosisStatusforConsiderForPayment(diagnosisProcedureTableDTO);
					if(!diagnosisStatus) {
						diagnosisProcedureTableDTO.setMinimumAmount(0);
					}
					diagnosisProcedureTableDTO.setIsPaymentAvailable(diagnosisStatus);
					if(size == 2){
						if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
							if(this.bean.getAmbulanceLimitAmount() > 0){
								diagnosisProcedureTableDTO.setAmbulanceCharge(this.bean.getAmbulanceLimitAmount().intValue());
								diagnosisProcedureTableDTO.setIsAmbulanceEnable(true);
								diagnosisProcedureTableDTO.setIsAmbChargeApplicable(true);
							}else{
								diagnosisProcedureTableDTO.setIsAmbulanceEnable(false);
								diagnosisProcedureTableDTO.setIsAmbChargeApplicable(false);
							}
						}
						
					}
				}
				

			}
			this.sublimitmedicalDecisionTable.addList(filledDTO);
			this.bean.setDiagnosisProcedureDtoList(filledDTO);
			if(bean.getIsNonAllopathic()) {
//				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
		}
		
		List<PreviousPreAuthTableDTO> previousPreauthList = this.bean.getPreviousPreauthTableDTO();
		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		if(previousPreauthList != null) {
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				if (!(this.bean.getKey() != null && this.bean.getKey().equals(
						previousPreAuthTableDTO.getKey()))) {

					newList.add(previousPreAuthTableDTO);
				}
			}
		}
		
//		this.previousPreAuthDetailsTableObj.setTableList(newList);
		if(this.bean.getIsReverseAllocation()) {
//			createReverseRelatedFields();
		}
	}
	
	public void setAppropriateValuesToDTOFromProcedure(DiagnosisProcedureTableDTO medicalDecisionDto, Map<String, Object> values) {

		if(bean.getIsNonAllopathic()) {
			bean.setNonAllopathicOriginalAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT));
			bean.setNonAllopathicUtilizedAmt((Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
//			createNonAllopathicFields((Double)values.get(SHAConstants.NON_ALLOPATHIC_ORIGINAL_AMOUNT), (Double)values.get(SHAConstants.NON_ALLOPATHIC_UTILIZED_AMOUNT));
		}
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
			
			if(!isResidual) {
				Integer entitlementNoOfDays = SHAUtils.getEntitlementNoOfDays(bean.getUploadDocumentDTO());
				Integer availAmt = entitlementNoOfDays * subLimitAvaliableAmt;
				int min = Math.min(SHAUtils.getIntegerFromString(medicalDecisionDto.getSubLimitAmount()) , availAmt);
				medicalDecisionDto.setSubLimitAvaliableAmt(min);
				medicalDecisionDto.setSubLimitUtilAmount(0);
			}
		}
		
		this.sublimitmedicalDecisionTable
				.addBeanToList(medicalDecisionDto);
		this.bean.getDiagnosisProcedureDtoList().add(medicalDecisionDto);
	
	}
	
 	public void editSublimitValuesForMedicalDescionTable(DiagnosisProcedureTableDTO medicalDecisionDto, Map<String, Object> values) {
		this.sublimitmedicalDecisionTable.setAppropriateValuesToDTOFromProcedure(medicalDecisionDto, values);
	}
	
	public void addListener(){
		
		submitBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(validateAmt()){
					if(validateSublimitApplicable()) {
						String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
						setTableValues();
						fireViewEvent(UpdateSublimitPresenter.SUBMIT_EVENT, bean, userName);
					}
				} else {
					
				}
			}
		});
		cancelBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
				        "No", "yes", new ConfirmDialog.Listener() {
					
				            public void onClose(ConfirmDialog dialog) {
				            	if (dialog.isCanceled() && !dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.UPDATE_SUBLIMIT, true);
				                } else {
				                    
				                	dialog.close();
				                }
				            }
				        });
				
				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
				
					
			}
		});
		
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public  void updateSublimitCommentsChangeListener(TextArea textArea, final  Listener listener) {
	    @SuppressWarnings("unused")
		ShortcutListener enterShortCut = new ShortcutListener("ShortcutForApprovalComments", ShortcutAction.KeyCode.F8, null) {
	    	private static final long serialVersionUID = -2267576464623389044L;
	    	@Override
	    	public void handleAction(Object sender, Object target) {
	    		((ShortcutListener) listener).handleAction(sender, target);
	    	}
	    };	  
	    handleShortcut(textArea, getUpdateSublimitCommentsShortCutListener(textArea));
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
	
	private ShortcutListener getUpdateSublimitCommentsShortCutListener(final TextArea textAreaField) {
		ShortcutListener listener =  new ShortcutListener("ShortcutForApprovalComments", KeyCodes.KEY_F8,null) {
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
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				dialog.setCaption("Remarks to Update Sublimit");
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

	public Boolean validateAmt() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		
		List<DiagnosisProcedureTableDTO> tableValues = this.sublimitmedicalDecisionTable.getValues();
		List<Long> selectSublimts = checkSublimitSelected(this.bean);
		List<Long> selectSublimtsInMA =  checkSublimitSelectedInMA(this.bean);
		if(tableValues != null){
				for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : tableValues) {
					if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null && diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitApplicableFlag() != null
							&& diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitApplicableFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) {
						if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitApplicableFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)  && 
									diagnosisProcedureTableDTO.getSubLimitAvaliableAmt() >= diagnosisProcedureTableDTO.getNetAmount()){
//							if(!selectSublimts.contains(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName().getLimitId())){
							Long limitId = diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName().getLimitId();
							/*As Per Rajasree req, condition 3 is not required in CR R0999 so commenting the below code
							if(selectSublimts.contains(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName().getLimitId())
									&& checkSimilarSublimits(selectSublimts,limitId)){
								hasError = true;
								break;
							} else {
									if(!selectSublimtsInMA.contains(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName().getLimitId())){
										hasError = false;
									} else{
										hasError = true;
										break;
									}
								}*/
							hasError = false;
						}else {
								hasError = true;
								break;
						}
					} else if(diagnosisProcedureTableDTO.getProcedureDTO() != null && diagnosisProcedureTableDTO.getProcedureDTO().getSublimitApplicableFlag() != null){
						if(diagnosisProcedureTableDTO.getProcedureDTO().getSublimitApplicableFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)  && 
								diagnosisProcedureTableDTO.getSubLimitAvaliableAmt() >= diagnosisProcedureTableDTO.getNetAmount()){
							hasError = false;
						} else {
							hasError = true;
							break;
						}
					}
			}
		}

		if (hasError) {
			Label successLabel = new Label("<b style = 'color: red;'>Selected Sublimit doesn’t have sufficient amount to update.</b>", ContentMode.HTML);			
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
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
					
				}
			});
			return !hasError;
		} else{
			try{
				
			}catch(Exception e){
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	
	}
	
	public Boolean validateSublimitApplicable(){
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		List<DiagnosisProcedureTableDTO> tableValues = this.sublimitmedicalDecisionTable.getValues();
		Boolean sublimitFlag = true;
		
		if(tableValues != null){
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : tableValues) {
				if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null && diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitApplicableFlag() != null
						&& diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitApplicableFlag().equalsIgnoreCase(SHAConstants.N_FLAG)) {
					sublimitFlag = false;
					hasError = true;
					eMsg.append("<b style = 'color: red;'>Please Select Sublimit as Yes</b></br>");
					break;
				}
			}
		}
		
		
		
		Boolean isSublimitChanged = this.sublimitmedicalDecisionTable.isSublimitChanged();
		
		if(isSublimitChanged != null && !isSublimitChanged){
			if(sublimitFlag){
				hasError = true;
				eMsg.append("<b style = 'color: red;'>Same Sublimit not allowed</b>");
			}
		}
		
		/*Below condition removed as per latest documentation
		if(bean.getIsCancelPolicy() != null && bean.getIsCancelPolicy()){
			hasError = true;
			eMsg.append("<b style = 'color: red;'>Policy is cancelled so we can't able to update sublimit</b>");
		}*/
		
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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

			hasError = true;
			return !hasError;
		} else{
			try{
				
			}catch(Exception e){
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
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
	
	public void setTableValues(){
		List<DiagnosisProcedureTableDTO> tableValues = this.sublimitmedicalDecisionTable.getValues();
		if(tableValues != null){
			this.bean.setDiagnosisProcedureDtoList(tableValues);
		}
		if(txtsublimitUpdateRemarks.getValue() != null && !txtsublimitUpdateRemarks.getValue().isEmpty()){
			this.bean.setSublimitUpdateRemarks(txtsublimitUpdateRemarks.getValue());
		}
	}
	
	public List<Long> checkSublimitSelected(PreauthDTO preauthDTO){
		
		List<Long> selectedSublimits = new ArrayList<Long>();
		List<PedValidation> diag = reimbursementService.getDiagnosisbyPolicyKey(preauthDTO.getPolicyKey());
		for (PedValidation pedValidation : diag) {
			if(pedValidation.getSublimitId() != null){
				if(pedValidation.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS) ||
						pedValidation.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
					selectedSublimits.add(pedValidation.getSublimitId());
				}
			}
		}
		return selectedSublimits;
	}
	
	public List<Long> checkSublimitSelectedInMA(PreauthDTO preauthDTO){
		
		List<Long> selectedSublimits = new ArrayList<Long>();
		List<PedValidation> diag = reimbursementService.getDiagnosisbyPolicyKey(preauthDTO.getPolicyKey());
		for (PedValidation pedValidation : diag) {
			if(pedValidation.getSublimitId() != null){
				if(pedValidation.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) ||
						pedValidation.getStatus().getKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER) ||
						pedValidation.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) ||
						pedValidation.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER) ||
						pedValidation.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REFER_TO_BILLING)){
					selectedSublimits.add(pedValidation.getSublimitId());
				}
			}
		}
		return selectedSublimits;
	}
	
	public Boolean checkSimilarSublimits(List<Long> limitIds,Long currentSublimitId){
		Boolean isDuplicate = false;
		for(int i=0; i<limitIds.size();i++){
			if(limitIds.get(i).equals(currentSublimitId)){
				isDuplicate =true;
				break;
			}
		}
		return isDuplicate;
	}
	
	
	
}
