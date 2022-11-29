package com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billingprocess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.enhacement.table.PreviousPreAuthDetailsTable;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.reimbursement.billing.dto.HopitalizationCalulationDetailsDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billingprocess.FinancialProcessPagePresenter;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.listenertables.PAAmountConsideredTable;
import com.shaic.paclaim.cashless.listenertables.PARevisedMedicalDecisionTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAHealthBillingProcessPageUI extends ViewComponent {

	private static final long serialVersionUID = 8487256143179584904L;

	@Inject
	private PreauthDTO bean;
	
	private GWizard wizard;
	
	@Inject
	private Instance<PARevisedMedicalDecisionTable> medicalDecisionTable;
	
	@EJB
	private PreauthService preauthService;
	
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private ViewBillSummaryPage viewBillSummary;
	
	@Inject
	private Instance<PAAmountConsideredTable> amountConsideredTableInstance;
	
	@Inject
	private Instance<PreviousPreAuthDetailsTable> previousPreauthDetailsTableInstance;
	
	private PreviousPreAuthDetailsTable previousPreAuthDetailsTableObj;
	
	private PAAmountConsideredTable amountConsideredTable;
	
	private PARevisedMedicalDecisionTable medicalDecisionTableObj;
	
	public BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Window popup;
	
	public List<String> errorMessages;

	private Label amountConsidered;

	private TextArea approvalRemarks;

	private Button showBalanceSumInsured;

	private Button showClaimAmtDetailsBtnDuplicate;

	private Button amountConsideredViewButton;

	private TextField approvedAmtField;

	private VerticalLayout wholeVLayout;
	
	private String preAuthRequestedAmt;

	private Map<String, Object> referenceData;

	private VerticalLayout optionCLayout;

	private TextField nonAllopathicTxt;
	
	private TextField txtCopayRemarks;
	
	
	@Override
	public String getCaption() {
		return "View Bill Summary";
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

	}
	
	
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(this.bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public String getHospitalizationAmount() {
//		 if(billEntryTableObj != null) {
//			 return billEntryTableObj.getHospitalizationValues();
//		 }
		 Integer hospitalizationAmount = 0;
			 List<UploadDocumentDTO> billEntryDetails = (List<UploadDocumentDTO>) bean.getUploadDocumentDTO();
			 if(billEntryDetails != null && !billEntryDetails.isEmpty()) {
					for (UploadDocumentDTO uploadDocumentDTO : billEntryDetails) {
						List<BillEntryDetailsDTO> billEntryDetailList = uploadDocumentDTO.getBillEntryDetailList();
						for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailList) {
							if(billEntryDetailsDTO.getClassification() != null && billEntryDetailsDTO.getClassification().getId().equals(8l)) {
								//hospitalizationAmount += billEntryDetailsDTO.getItemValue() != null ? billEntryDetailsDTO.getItemValue().intValue() : 0;
								hospitalizationAmount += billEntryDetailsDTO.getNetPayableAmount() != null ? billEntryDetailsDTO.getNetPayableAmount().intValue() : 0;
							}
						}
					}
			 }
		 return  String.valueOf(hospitalizationAmount) ;
	 }

	
	public Component getContent() {
		initBinder();
		
		this.previousPreAuthDetailsTableObj = previousPreauthDetailsTableInstance
				.get();
		this.previousPreAuthDetailsTableObj.init("Cashless Approval Details", false, false);
		
		if(this.bean.getAmountConsidered() != null && bean.getIsReverseAllocation() && !this.bean.getAmountConsidered().equals(String.valueOf(SHAUtils.getDoubleFromString( bean.getHospitalisationValue()).intValue()))) {
			bean.setIsReverseAllocation(false);
		}
		
		this.bean.setAmountConsidered(String.valueOf(SHAUtils.getDoubleFromString( bean.getHospitalisationValue()).intValue()));
		
//		if(SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) > 0) {
//			Integer amount = SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) - (SHAUtils.getIntegerFromString(this.bean.getPreHospitalisationValue()) + SHAUtils.getIntegerFromString(this.bean.getPostHospitalisationValue()));
//			this.bean.setAmountConsidered(String.valueOf(amount));
//			
//			if(!this.bean.getHospitalizaionFlag()) {
//				this.bean.setAmountConsidered("0");
//			}
//			
//		}
		
		this.bean.setAmbulanceAmountConsidered(this.bean.getAmountConsidered());
		
		// For lumpsum, amount consider will be setted as bill value from bill entry...
		StarCommonUtils.setAmountconsideredForLumpsum(bean);
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			
			if(this.bean.getAmbulanceLimitAmount() != null){
			 Double totalAmountConsidered = SHAUtils.getDoubleFromStringWithComma(this.bean.getAmountConsidered());
		      totalAmountConsidered -= this.bean.getAmbulanceLimitAmount();
//		      this.bean.setAmbulanceAmountConsidered(totalAmountConsidered.toString());
		      this.bean.setAmbulanceAmountConsidered(String.valueOf(totalAmountConsidered.intValue()));
			}
			
		}
		
		amountConsidered = new Label("Amount considered");
		amountConsidered.setCaption("Amount considered");
		amountConsidered.setValue(this.bean.getAmountConsidered());
		
		approvalRemarks = (TextArea) binder.buildAndBind("Approval Remarks (Zonal Medical)", "approvalRemarks", TextArea.class);
		approvalRemarks.setMaxLength(100);
		approvalRemarks.setWidth("400px");
		mandatoryFields.removeAll(mandatoryFields);
		mandatoryFields.add(approvalRemarks);
		showOrHideValidation(false);
		
		showBalanceSumInsured = new Button("View");
		showClaimAmtDetailsBtnDuplicate = new Button("View");
		amountConsideredViewButton = new Button("View");
		showBalanceSumInsured.setStyleName("link");
		showClaimAmtDetailsBtnDuplicate.setStyleName("link");

		amountConsideredViewButton.setStyleName("link");
		
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
		
		this.medicalDecisionTableObj = medicalDecisionTable.get();
		this.medicalDecisionTableObj.init(this.bean);
		this.medicalDecisionTableObj.initPresenter(SHAConstants.PA_BILLING_HOSP);

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
		this.amountConsideredTable.initTable(this.bean, viewLayout1,
				viewLayout2, optionCLayout, false, true);
		
		txtCopayRemarks = new TextField("Remarks for Co-pay");
		txtCopayRemarks.setNullRepresentation("");
		txtCopayRemarks.setEnabled(false);
		
		if(bean.getCopayRemarks() != null){
			txtCopayRemarks.setValue(txtCopayRemarks.getValue());
		}
		
		HorizontalLayout amountConsideredLayout = new HorizontalLayout(
				new FormLayout(amountConsidered),new FormLayout(amountConsideredViewButton));
		addMedicalDecisionTableFooterListener();
		wholeVLayout = new VerticalLayout(this.previousPreAuthDetailsTableObj,amountConsideredLayout, this.medicalDecisionTableObj,
				this.amountConsideredTable,new FormLayout(txtCopayRemarks));
		return wholeVLayout;
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		this.medicalDecisionTableObj.setReferenceData(referenceData);
		
		List<PreviousPreAuthTableDTO> previousPreauthList = this.bean.getPreviousPreauthTableDTO();
		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		if(previousPreauthList != null && !previousPreauthList.isEmpty()) {
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				if (!(this.bean.getKey() != null && this.bean.getKey().equals(
						previousPreAuthTableDTO.getKey()))) {
					fireViewEvent(FinancialProcessPagePresenter.GET_PREAUTH_REQUESTED_AMOUT,
							previousPreAuthTableDTO);
//					previousPreAuthTableDTO.setRequestedAmt(preAuthRequestedAmt);
					previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmt(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
					newList.add(previousPreAuthTableDTO);
				}
			}
		}
		
		this.previousPreAuthDetailsTableObj.setTableList(newList);
		
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
		
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
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
									break;
								}
							}
						}
					}

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
					dto.setSubLimitAmount(String.valueOf( SHAUtils.getDoubleFromString(
							pedValidationTableDTO.getSublimitAmt()).intValue())  );
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
					SelectValue value = new SelectValue();
					value.setId(pedValidationTableDTO.getCopayPercentage().longValue());
					value.setValue( String.valueOf(pedValidationTableDTO.getCopayPercentage().longValue()));
					dto.setCoPayPercentage(value);
				}
				
				dto.setIsAmbChargeFlag(pedValidationTableDTO.getIsAmbChargeFlag());
				dto.setAmbulanceCharge(pedValidationTableDTO.getAmbulanceCharge());
				dto.setAmtWithAmbulanceCharge(pedValidationTableDTO.getAmtWithAmbulanceCharge());
				if(this.bean.getIsAmbulanceApplicable()){
					dto.setIsAmbulanceEnable(true);
				}
				
				Integer amount = SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) - (SHAUtils.getIntegerFromString(this.bean.getPreHospitalisationValue()) + SHAUtils.getIntegerFromString(this.bean.getPostHospitalisationValue()));
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
				
				if(isAmbulanceReset){
					
					dto.setIsAmbChargeApplicable(false);
					dto.setAmbulanceCharge(0);
					dto.setAmtWithAmbulanceCharge(0);
				}
				
				
				
				if(dto.getAmountConsidered() == 0 ) {
					dto.setMinimumAmount(0);
					dto.setReverseAllocatedAmt(0);
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
					dto.setIsPaymentAvailable(isPaymentAvailable);
					
				}
					if(isPaymentAvailable) {
						if(procedureDTO.getExclusionDetails() != null && procedureDTO.getExclusionDetails().getValue() != null && !procedureDTO.getExclusionDetails().getValue().toLowerCase().equalsIgnoreCase("not applicable")) {
							isPaymentAvailable = false;
						}
					}
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
				dto.setReverseAllocatedAmt(procedureDTO.getNetApprovedAmount() != null ? procedureDTO.getNetApprovedAmount().intValue() : 0);
				dto.setRemarks(procedureDTO.getApprovedRemarks() != null ? procedureDTO.getApprovedRemarks() : "");
				
				
				dto.setIsAmbChargeFlag(procedureDTO.getIsAmbChargeFlag());
				dto.setAmbulanceCharge(procedureDTO.getAmbulanceCharge());
				dto.setAmtWithAmbulanceCharge(procedureDTO.getAmtWithAmbulanceCharge());
				if(this.bean.getIsAmbulanceApplicable()){
					dto.setIsAmbulanceEnable(true);
				}
				
				if(procedureDTO.getCopayPercentage() != null) {
					SelectValue value = new SelectValue();
					value.setId(procedureDTO.getCopayPercentage().longValue());
					value.setValue( String.valueOf(procedureDTO.getCopayPercentage().longValue()));
					dto.setCoPayPercentage(value);
				}
				Integer amount = SHAUtils.getIntegerFromString(this.bean.getInitialAmountConsidered()) - (SHAUtils.getIntegerFromString(this.bean.getPreHospitalisationValue()) + SHAUtils.getIntegerFromString(this.bean.getPostHospitalisationValue()));
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
				
				if(isAmbulanceReset){
					
					dto.setIsAmbChargeApplicable(false);
					dto.setAmbulanceCharge(0);
					dto.setAmtWithAmbulanceCharge(0);
				}
				
				if(dto.getAmountConsidered() == 0 ) {
					dto.setMinimumAmount(0);
					dto.setReverseAllocatedAmt(0);
				}
				medicalDecisionDTOList.add(dto);
			}
			
			Map<String, Object> caluculationInputValues = new HashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			
			Double insuredSumInsured = 0d;
			if(null != this.bean.getNewIntimationDTO() && null != this.bean.getNewIntimationDTO().getPolicy() && 
					null != this.bean.getNewIntimationDTO().getPolicy().getProduct() && 
					null != this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
					!(ReferenceTable.getGPAProducts().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
			
				insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			}
			else
			{
				insuredSumInsured = dbCalculationService
						.getGPAInsuredSumInsured(this.bean.getNewIntimationDTO()
								.getInsuredPatient().getInsuredId().toString(),
								this.bean.getPolicyDto().getKey());
			}
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
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues
								.put("diagOrProcId", medicalDecisionDto.getProcedureDTO().getProcedureName() == null ? 0l : (medicalDecisionDto.getProcedureDTO().getProcedureName().getId() == null ? 0l : medicalDecisionDto.getProcedureDTO().getProcedureName().getId()));
						caluculationInputValues.put("referenceFlag", "P");
					}
					caluculationInputValues.put("preauthKey", this.bean.getKey());
					
					if(bean.getIsHospitalizationRepeat()) {
						caluculationInputValues.put("preauthKey",0l);
					}
					
					caluculationInputValues.put(SHAConstants.IS_NON_ALLOPATHIC, bean.getPreauthDataExtractionDetails().getNatureOfTreatment() != null ? (bean.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue().toLowerCase().contains("non") ? true : false) : false);
					caluculationInputValues.put(SHAConstants.INSURED_KEY, bean.getNewIntimationDTO().getInsuredPatient().getKey());
					caluculationInputValues.put(SHAConstants.POLICY_KEY, bean.getNewIntimationDTO().getPolicy().getKey());
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
					
					System.out.println("***************************************************"+ bean.getClaimKey());
					
					
					
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
							PAHealthBillingProcessPagePresenter.SUM_INSURED_CALCULATION,
							caluculationInputValues, medicalDecisionDto,this.bean);
				}
				
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				dto.setIsAmbChargeApplicable(false);
				
				if(this.bean.getResidualAmountDTO() != null && this.bean.getResidualAmountDTO().getKey() != null) {
					ResidualAmountDTO residualAmountDTO = this.bean.getResidualAmountDTO();
					dto.setAmountConsidered(residualAmountDTO.getAmountConsideredAmount() != null ? residualAmountDTO.getAmountConsideredAmount().intValue() : 0);
					
					dto.setMinimumAmount(residualAmountDTO.getMinimumAmount() != null ? residualAmountDTO.getMinimumAmount().intValue(): 0);
					dto.setCoPayAmount(residualAmountDTO.getCopayAmount() != null ? residualAmountDTO.getCopayAmount().intValue() : 0);
					dto.setRemarks(residualAmountDTO.getRemarks() != null ? residualAmountDTO.getRemarks() : "");
					
					SelectValue value = new SelectValue();
					value.setId(residualAmountDTO.getCopayPercentage() != null ? residualAmountDTO.getCopayPercentage().longValue() : 0l);
					value.setValue(residualAmountDTO.getCopayPercentage() != null ? String.valueOf(residualAmountDTO.getCopayPercentage().intValue())  : "0");
					
					dto.setCoPayPercentage(value);
					dto.setNetAmount(residualAmountDTO.getNetAmount() != null ? residualAmountDTO.getNetAmount().intValue() : 0);
					dto.setNetApprovedAmt(residualAmountDTO.getApprovedAmount() != null ? residualAmountDTO.getApprovedAmount().intValue() : 0);
					dto.setReverseAllocatedAmt(residualAmountDTO.getNetApprovedAmount() != null ? residualAmountDTO.getNetApprovedAmount().intValue() : 0);
					if(isSetZero) {
						dto.setAmountConsidered(0);
						dto.setCoPayAmount(0);
						dto.setNetAmount(0);
						dto.setMinimumAmount(0);
						dto.setNetApprovedAmt(0);
						dto.setReverseAllocatedAmt(0);
					}
					if(dto.getAmountConsidered() == 0 ) {
						dto.setMinimumAmount(0);
						dto.setReverseAllocatedAmt(0);
					}
				}
				
				this.medicalDecisionTableObj.addBeanToList(dto);
			}
			if(!bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
			if(bean.getIsNonAllopathic()) {
				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
			}
		} else {
			
			int size = filledDTO.size();
			
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : filledDTO) {
				if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
					Boolean procedureStatus = SHAUtils.getProcedureStatus(diagnosisProcedureTableDTO.getProcedureDTO());
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
					Boolean diagnosisStatus = SHAUtils.getDiagnosisStatus(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO());
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
			this.medicalDecisionTableObj.addList(filledDTO);
			if(!bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
			if(bean.getIsNonAllopathic()) {
				createNonAllopathicFields(bean.getNonAllopathicOriginalAmt(), bean.getNonAllopathicUtilizedAmt());
			}
			}
			
		}
		
		if(this.bean.getIsReverseAllocation()) {
			createReverseRelatedFields();
		}
	}
	
	private void createReverseRelatedFields() {
		Integer approvedAmt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0;
		Integer cValue = SHAUtils.getIntegerFromStringWithComma(this.medicalDecisionTableObj.dummyField.getValue()); 
		if(cValue.equals(0)) {
			cValue = bean.getSublimitAndSIAmt() != null ? bean.getSublimitAndSIAmt().intValue() : 0;
		}
		if(!cValue.equals(approvedAmt)) {
			this.bean.setIsReverseAllocation(true);
			if(SHAUtils.getIntegerFromStringWithComma(bean.getReverseAmountConsidered()) <=  approvedAmt ) {
				this.medicalDecisionTableObj.setReverseAllocationColumn(String.valueOf(approvedAmt));
				bean.setReverseAmountConsidered(String.valueOf(approvedAmt));
			} else {
				bean.setIsReverseAllocation(false);
			}
			
		}  else {
			this.bean.setIsReverseAllocation(false);
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
		
		this.medicalDecisionTableObj
				.addBeanToList(medicalDecisionDto);
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
		
		Map<String, Object> values = new HashMap<String, Object>();
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
	
	private void addMedicalDecisionTableFooterListener() {

		this.medicalDecisionTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 4843316375590220412L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmt = SHAUtils
								.getIntegerFromString((String) event
										.getProperty().getValue());
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmt);
						if(!bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.SARAL_SURAKSHA_CARE_INDIVIDUAL)){
						if(bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						}
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
						if ((bean.getStatusKey()
								.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS))) {
							
							approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
							bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										amt -= ((bean.getPostHospitalisationValue() != null && SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) > 0) ? SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) : 0d);
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}
									
								}
							}
						} else {
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										amt -= ((bean.getPostHospitalisationValue() != null && SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) > 0) ? SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) : 0d);
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}
									
								}
							}
						}
					}
				});

		this.amountConsideredTable.dummyField
				.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 9193355451830325446L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Integer totalApprovedAmt = SHAUtils
								.getIntegerFromString(medicalDecisionTableObj.dummyField
										.getValue());
						Integer min = Math.min(
								amountConsideredTable.getMinimumValue(),
								totalApprovedAmt);
						if(bean.getIsNonAllopathic()) {
							min = Math.min(min, bean.getNonAllopathicAvailAmt());
						}
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
						approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
						if(bean.getIsReverseAllocation()) {
							Integer approvedAmt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0;
							Integer cValue = SHAUtils.getIntegerFromStringWithComma(medicalDecisionTableObj.dummyField.getValue()); 
							if(cValue.equals(approvedAmt) && !bean.getReverseAmountConsidered().equals(String.valueOf(approvedAmt))) {
								bean.setIsReverseAllocation(false);
								medicalDecisionTableObj.deleteReverseAllocation();
							}
						}
						
						if ((bean.getStatusKey()
								.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS))) {
							
							approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
							bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										amt -= ((bean.getPostHospitalisationValue() != null && SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) > 0) ? SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) : 0d);
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

									
								}
							}
						} else {
							if(bean.getNewIntimationDTO() != null) {
								Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
								if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
									min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
									approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
									bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
									if(!bean.getIsReverseAllocation()) {
										amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
										approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}  else {
										if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
											bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
										}
										Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
										amt -= ((bean.getPostHospitalisationValue() != null && SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) > 0) ? SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) : 0d);
										amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
										bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
									}

									
								}
							}
						}
					}
				});
		
		this.amountConsideredTable.otherinsurerDummyField.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				setApprovedAmount();
				
			}
		});
		
		this.medicalDecisionTableObj.ambulanceChangeField.addValueChangeListener(new ValueChangeListener() {
			
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

	private void setHospitalizationDetailsToDTO() {
		HopitalizationCalulationDetailsDTO hospitalizationCalculationDTO = this.bean.getHospitalizationCalculationDTO();
//		Integer amount = (bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0) + ((bean.getHospDiscountAmount() != null && bean.getHospitalizaionFlag()) ? bean.getHospDiscountAmount().intValue() : 0 );
		Integer amount = (bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0);
		hospitalizationCalculationDTO.setNetPayableAmt(amount);
		hospitalizationCalculationDTO.setPreauthAppAmt(0);
		String preauthApprAmt = "0";
		
		if(hospitalizationCalculationDTO.getNetPayableAmt() < 0) {
			hospitalizationCalculationDTO.setNetPayableAmt(0);
		}
		
//		if(!bean.getPreviousPreauthTableDTO().isEmpty()) {
//			for (PreviousPreAuthTableDTO preauth : bean.getPreviousPreauthTableDTO()) {
//				preauthApprAmt = preauth.getApprovedAmt();
//			}
//		}
//		if(SHAUtils.isValidDouble(preauthApprAmt)) {
//			Double valueOf = Double.valueOf(preauthApprAmt);
//			hospitalizationCalculationDTO.setPreauthAppAmt(valueOf.intValue());
//			hospitalizationCalculationDTO.setPreauthAppAmt(this.previousPreAuthDetailsTableObj.getTotalApprovedAmt() != null ? this.previousPreAuthDetailsTableObj.getTotalApprovedAmt().intValue() : 0);
//		}
		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			hospitalizationCalculationDTO.setPreauthAppAmt((this.previousPreAuthDetailsTableObj != null && this.previousPreAuthDetailsTableObj.getTotalApprovedAmt() != null) ? this.previousPreAuthDetailsTableObj.getTotalApprovedAmt().intValue() : 0);
		}
		
		if(bean.getIsHospitalizationRepeat()) {
			hospitalizationCalculationDTO.setPreauthAppAmt(0);
		}
		
		
		
		if(bean.getClaimDTO().getClaimType() != null && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {
			if((bean.getPartialHospitalizaionFlag()) &&  bean.getFAApprovedAmount() != null && bean.getFAApprovedAmount() != 0d && hospitalizationCalculationDTO.getPreauthAppAmt() > bean.getFAApprovedAmount()) {
				hospitalizationCalculationDTO.setPreauthAppAmt(bean.getFAApprovedAmount().intValue());
			}
		}
		hospitalizationCalculationDTO.setHospitalDiscount(bean.getHospDiscountAmount() != null  ?bean.getHospDiscountAmount().intValue() : 0 );
		hospitalizationCalculationDTO.setPayableToInsAmt(0);
		hospitalizationCalculationDTO.setClaimRestrictionAmt((bean.getClaimRestrictionAmount() != null && bean.getClaimRestrictionAmount() > 0) ?  bean.getClaimRestrictionAmount().intValue() : 0);
		
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL)) {
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			
			Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(bean.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), bean.getNewIntimationDTO().getPolicy().getKey(),bean.getNewIntimationDTO().getInsuredPatient().getLopFlag());
			
			if(insuredSumInsured != null && insuredSumInsured > 0) {
				Float value =  ((50f/ 100f) * insuredSumInsured.floatValue());
				int roundedValue = Math.round(value);
				if(hospitalizationCalculationDTO.getNetPayableAmt() > roundedValue) {
					hospitalizationCalculationDTO.setClaimRestrictionAmt(roundedValue);
				} else {
					hospitalizationCalculationDTO.setClaimRestrictionAmt(0);
				}
			}
		}
		if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
			hospitalizationCalculationDTO.setPayableToInsAmt(hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt() - hospitalizationCalculationDTO.getPreauthAppAmt());
			if(hospitalizationCalculationDTO.getPayableToInsAmt() < 0) {
				hospitalizationCalculationDTO.setPayableToInsAmt(0);
			}	
			
			hospitalizationCalculationDTO.setPayableToHospitalAmt(bean.getPayableToHospAmt() != null ? bean.getPayableToHospAmt().intValue() : 0);
			Integer afterDiscountAmt = hospitalizationCalculationDTO.getPayableToHospitalAmt() - hospitalizationCalculationDTO.getHospitalDiscount();
			hospitalizationCalculationDTO.setAfterHospitalDiscount(afterDiscountAmt < 0 ? 0 : afterDiscountAmt);
			hospitalizationCalculationDTO.setTdsAmt(0);
			hospitalizationCalculationDTO.setPayableToHospitalAftTDSAmt(afterDiscountAmt < 0 ? 0 : afterDiscountAmt);
			
			hospitalizationCalculationDTO.setBalancePremiumAmt(0);
			hospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(hospitalizationCalculationDTO.getPayableToInsAmt() - hospitalizationCalculationDTO.getBalancePremiumAmt());
			
			/*if(null != hospitalizationCalculationDTO.getHospitalDiscount() && 0 != hospitalizationCalculationDTO.getHospitalDiscount())
			{
				hospitalizationCalculationDTO.setPayableToInsAmt(hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt() - hospitalizationCalculationDTO.getAfterHospitalDiscount());
				
				hospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(hospitalizationCalculationDTO.getPayableToInsAmt() - hospitalizationCalculationDTO.getBalancePremiumAmt());
				
			}*/
			
			if(this.bean.getIsReconsiderationRequest() != null && this.bean.getIsReconsiderationRequest()) {
				hospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getHospAmountAlreadyPaid());
				Integer balanceAmt = 0;
				if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
					balanceAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
				} else {
					balanceAmt = hospitalizationCalculationDTO.getPayableToInsuredAftPremiumAmt();
				}
				
				hospitalizationCalculationDTO.setBalanceToBePaid(balanceAmt < 0  ? 0 : balanceAmt);
			}
			
			
		} else if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
			
			hospitalizationCalculationDTO.setPayableToHospitalAmt((hospitalizationCalculationDTO.getPreauthAppAmt() != 0 ? Math.min(hospitalizationCalculationDTO.getPreauthAppAmt(), (hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt())) : (hospitalizationCalculationDTO.getNetPayableAmt() - hospitalizationCalculationDTO.getClaimRestrictionAmt())));
			if(hospitalizationCalculationDTO.getPayableToHospitalAmt() < 0) {
				hospitalizationCalculationDTO.setPayableToHospitalAmt(0);
			}
			hospitalizationCalculationDTO.setBalancePremiumAmt(0);
			hospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
			
			hospitalizationCalculationDTO.setTdsAmt(0);
			
			Integer discountAmt = hospitalizationCalculationDTO.getHospitalDiscount() != null ? hospitalizationCalculationDTO.getHospitalDiscount() : 0;
			Integer aftDiscountAmt  = hospitalizationCalculationDTO.getPayableToHospitalAmt() - discountAmt;
			hospitalizationCalculationDTO.setPayableToHospitalAftTDSAmt(aftDiscountAmt);
			
			if(this.bean.getIsReconsiderationRequest() != null && this.bean.getIsReconsiderationRequest()) {
				hospitalizationCalculationDTO.setAmountAlreadyPaid(bean.getHospAmountAlreadyPaid());
				Integer balanceAmt = 0;
				if(bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() == null || (bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable() != null && !bean.getPreauthMedicalDecisionDetails().getOtherInsurerApplicable())) {
					balanceAmt = hospitalizationCalculationDTO.getPayableToHospitalAftTDSAmt() - (hospitalizationCalculationDTO.getAmountAlreadyPaid());
				} else {
					balanceAmt = hospitalizationCalculationDTO.getPayableToHospitalAftTDSAmt();
				}
				
				hospitalizationCalculationDTO.setBalanceToBePaid(balanceAmt < 0  ? 0 : balanceAmt);
			}
			
		} else {
			System.out.println("Something went wrong .... Doc Received from not updated.. Please check "+ bean.getKey());
		}
		
		if(bean.getHospitalizaionFlag() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			hospitalizationCalculationDTO.setPreauthAppAmtBeforePremium(hospitalizationCalculationDTO.getPreauthAppAmt() + uniqueInstallmentAmount);
			hospitalizationCalculationDTO.setInstallmentAmount(uniqueInstallmentAmount);
		}
		
	}
	
//	private void setPostHospitalizationDetailsToDTO() {
//		PostHopitalizationDetailsDTO postHospitalizationCalculationDTO = this.bean.getPostHospitalizationCalculationDTO();
//		if(bean.getPostHospitalizaionFlag()) {
//			postHospitalizationCalculationDTO.setNetAmount(SHAUtils.getIntegerFromString(bean.getPostHospitalisationValue()) );
//			if(postHospitalizationCalculationDTO.getNetAmount() > 0) {
//				postHospitalizationCalculationDTO.setOtherInsurerAmt(SHAUtils.getIntegerFromStringWithComma(bean.getPreauthMedicalDecisionDetails().getOtherInsurerPostHospAmountClaimed()) );
//				Integer amountForPayment =  postHospitalizationCalculationDTO.getNetAmount() - postHospitalizationCalculationDTO.getOtherInsurerAmt();
//				postHospitalizationCalculationDTO.setAmountConsideredForPayment(amountForPayment < 0 ? 0 : amountForPayment);
//					if(bean.getPostHospAmt() < 0) {
//						bean.setPostHospAmt(0);
//					}
//					
//					Float value =  (((!bean.getPostHospPercentage().equals(0d) ? bean.getPostHospPercentage().floatValue() : 7f)/ 100f) * bean.getPostHospAmt().floatValue());
//					int roundedValue = Math.round(value);
//					postHospitalizationCalculationDTO.setEligibleAmt(roundedValue);
//					
//					Integer amt = roundedValue;
//					Integer min2 = Math.min(amt, postHospitalizationCalculationDTO.getAmountConsideredForPayment() < 0 ? 0 : postHospitalizationCalculationDTO.getAmountConsideredForPayment());
//					postHospitalizationCalculationDTO.setAmountPayable(min2);
//					
//					List<Double> productCopay = this.bean.getProductCopay();
//					Double copayValue = 0d;
//					if(productCopay != null && !productCopay.isEmpty()) {
//						copayValue = productCopay.get(0);
//					}
//					postHospitalizationCalculationDTO.setCopayValue(copayValue.intValue());
//					Double copayAmount = (copayValue / 100) * postHospitalizationCalculationDTO.getAmountPayable();
//					Long roundedCopay = Math.round(copayAmount);
//					
//					postHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
//					postHospitalizationCalculationDTO.setIsSIRestrictionAvail(bean.getIsSIRestrictionAvail());
//					Integer payableAmt = postHospitalizationCalculationDTO.getAmountPayable() - postHospitalizationCalculationDTO.getCopayAmt();
//					postHospitalizationCalculationDTO.setPayableAmt(payableAmt < 0 ? 0 : payableAmt);
//					postHospitalizationCalculationDTO.setMaxPayable(bean.getPostHospclaimRestrictionAmount() != null ? bean.getPostHospclaimRestrictionAmount().intValue() : 0);
//					postHospitalizationCalculationDTO.setAvaliableSumInsuredAftHosp(bean.getBalanceSIAftHosp());
//					postHospitalizationCalculationDTO.setPreviousRodPostHospamt(bean.getPreviousPostHospAmount());
//					postHospitalizationCalculationDTO.setRestrictedSIAftHosp(bean.getIsSIRestrictionAvail() ? bean.getSiRestrictionAmount() : 0);
//					Integer amtPayable  = postHospitalizationCalculationDTO.getMaxPayable() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
//					
//					int min3 = Math.min(postHospitalizationCalculationDTO.getPayableAmt(), amtPayable < 0 ? 0 :amtPayable);
//					if(amtPayable < 0) {
//						min3 = 0;
//					}
//					
//					Integer min = Math.min(postHospitalizationCalculationDTO.getPayableAmt(), Math.min(bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_ANOTHER_POLICY) ? postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()  : postHospitalizationCalculationDTO.getMaxPayable(), Math.min(postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp(),(postHospitalizationCalculationDTO.getIsSIRestrictionAvail() ? (postHospitalizationCalculationDTO.getRestrictedSIAftHosp() > 0 ? postHospitalizationCalculationDTO.getRestrictedSIAftHosp() : 0 ) : postHospitalizationCalculationDTO.getAvaliableSumInsuredAftHosp()))));
//					postHospitalizationCalculationDTO.setNetPayable(min);
//					
//					postHospitalizationCalculationDTO.setClaimRestrictionAmt(0);  
////					if(this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && this.bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
////						postHospitalizationCalculationDTO.setPayableToInsAmt(postHospitalizationCalculationDTO.getPayableAmt());
////					} 
//					
//					Integer eligibleAmt = postHospitalizationCalculationDTO.getNetPayable() - postHospitalizationCalculationDTO.getClaimRestrictionAmt();
//					postHospitalizationCalculationDTO.setRevisedEligibleAmount(eligibleAmt < 0 ? 0: eligibleAmt);
//					Integer payableToIns = postHospitalizationCalculationDTO.getRevisedEligibleAmount() - postHospitalizationCalculationDTO.getPreviousRodPostHospamt();
//					postHospitalizationCalculationDTO.setPayableToInsAmt(payableToIns < 0 ? 0: payableToIns);
//					postHospitalizationCalculationDTO.setBalancePremiumAmt(0);
//					postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(postHospitalizationCalculationDTO.getPayableToInsAmt() - postHospitalizationCalculationDTO.getBalancePremiumAmt());
//					
//					if(!bean.getIsPostHospApplicable() || bean.getIsHospitalizationRejected()  || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) {
//						postHospitalizationCalculationDTO.setPayableToInsAmt(0);
//						postHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
//					}
//			}
////			
//		}
//		
//	}
	
//	private void setPreHospitalizationDetailsToDTO() {
//		PreHopitalizationDetailsDTO pretHospitalizationCalculationDTO = this.bean.getPreHospitalizationCalculationDTO();
//		if(bean.getPreHospitalizaionFlag()) {
//			Double min = Math.min(bean.getPreHospitalizaionFlag() ? SHAUtils.getDoubleFromString(bean.getPreHospitalisationValue())  : 0d  , SHAUtils.getDoubleFromString(bean.getPreHospitalisationValue()));
//			if(min > 0) {
//				pretHospitalizationCalculationDTO.setAmountConsidered(min.intValue());
//				pretHospitalizationCalculationDTO.setOtherInsurerAmt(SHAUtils.getIntegerFromStringWithComma(bean.getPreauthMedicalDecisionDetails().getOtherInsurerPreHospAmountClaimed()) );
//				Integer deductibleAmt = pretHospitalizationCalculationDTO.getAmountConsidered() - pretHospitalizationCalculationDTO.getOtherInsurerAmt();
//				pretHospitalizationCalculationDTO.setAmountConsideredForPayment(deductibleAmt > 0 ? deductibleAmt : 0);
//				List<Double> productCopay = this.bean.getProductCopay();
//				Double copayValue = 0d;
//				if(productCopay != null && !productCopay.isEmpty()) {
//					copayValue = productCopay.get(0);
//				}
//				pretHospitalizationCalculationDTO.setCopayValue(copayValue.intValue());
//				Double copayAmount = (copayValue / 100d) * pretHospitalizationCalculationDTO.getAmountConsideredForPayment();
//				Long roundedCopay = Math.round(copayAmount);
//				
//				pretHospitalizationCalculationDTO.setCopayAmt(roundedCopay.intValue());
//				Integer calcAmt = pretHospitalizationCalculationDTO.getAmountConsideredForPayment() - pretHospitalizationCalculationDTO.getCopayAmt();
//				pretHospitalizationCalculationDTO.setPayableAmt(calcAmt < 0 ? 0 : calcAmt);
//				Integer balanceSIAftHospAndPostHosp = bean.getBalanceSIAftHosp() - this.bean.getPostHospitalizationCalculationDTO().getPreviousRodPostHospamt();
//				if(bean.getPostHospitalizaionFlag()) {
//					balanceSIAftHospAndPostHosp = balanceSIAftHospAndPostHosp - this.bean.getPostHospitalizationCalculationDTO().getPayableToInsAmt();
//				}
//				pretHospitalizationCalculationDTO.setAvailableSIAftHosp(balanceSIAftHospAndPostHosp < 0 ? 0 : balanceSIAftHospAndPostHosp);
//				Integer siRestrictionAftHospAndPostHosp = 0;
//				if(bean.getIsSIRestrictionAvail()) {
//					siRestrictionAftHospAndPostHosp = bean.getSiRestrictionAmount() - this.bean.getPostHospitalizationCalculationDTO().getPreviousRodPostHospamt();
//					if(bean.getPostHospitalizaionFlag()) {
//						siRestrictionAftHospAndPostHosp = siRestrictionAftHospAndPostHosp - this.bean.getPostHospitalizationCalculationDTO().getPayableToInsAmt();
//					}
//				}
//				pretHospitalizationCalculationDTO.setIsSIRestrictionAvail(bean.getIsSIRestrictionAvail());
//				pretHospitalizationCalculationDTO.setRestrictedSIAftHosp(siRestrictionAftHospAndPostHosp < 0 ? 0 : siRestrictionAftHospAndPostHosp);
//				pretHospitalizationCalculationDTO.setNetPayable(Math.min(pretHospitalizationCalculationDTO.getPayableAmt(), Math.min(bean.getIsSIRestrictionAvail() ? (pretHospitalizationCalculationDTO.getRestrictedSIAftHosp() > 0 ? pretHospitalizationCalculationDTO.getRestrictedSIAftHosp() : 0) : pretHospitalizationCalculationDTO.getAvailableSIAftHosp(), pretHospitalizationCalculationDTO.getAvailableSIAftHosp())));
//				
//				pretHospitalizationCalculationDTO.setBalancePremiumAmt(0);
//				Integer calculatedAmt = pretHospitalizationCalculationDTO.getNetPayable() - pretHospitalizationCalculationDTO.getClaimRestrictionAmt();
//				pretHospitalizationCalculationDTO.setRevisedEligibleAmount(calculatedAmt < 0 ? 0: calculatedAmt);
//				pretHospitalizationCalculationDTO.setPreviousRodPreviousPrehospAmt(bean.getPreviousPreHospAmount());
//				Integer payableToInsAmt = pretHospitalizationCalculationDTO.getRevisedEligibleAmount() - pretHospitalizationCalculationDTO.getPreviousRodPreviousPrehospAmt();
//				pretHospitalizationCalculationDTO.setPayableToInsAmt(payableToInsAmt < 0 ? 0 : payableToInsAmt);
//				Integer aftPremiumAmt = pretHospitalizationCalculationDTO.getPayableToInsAmt() - pretHospitalizationCalculationDTO.getBalancePremiumAmt();
//				pretHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(aftPremiumAmt < 0 ? 0: aftPremiumAmt);
//				if(!bean.getIsPreHospApplicable() || bean.getIsHospitalizationRejected() || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) {
//					pretHospitalizationCalculationDTO.setPayableToInsAmt(0);
//					pretHospitalizationCalculationDTO.setPayableToInsuredAftPremiumAmt(0);
//				}
//				
//			}
//	
//		}
//		
//	}
	
	public Boolean validatePage() {
		String eMsg = "";
		Boolean hasError = false;
		if(bean.getIsReverseAllocation()) {
			setApprovedAmount();
			Integer approvedAmt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0;
			Integer cValue = this.medicalDecisionTableObj.getTotalCAmount();
			if(approvedAmt.equals(cValue)) {
				bean.setIsReverseAllocation(false);
				medicalDecisionTableObj.deleteReverseAllocation();
			} else {
				bean.setReverseAmountConsidered(String.valueOf(approvedAmt));
				this.medicalDecisionTableObj.setReverseAllocationColumn(String.valueOf(approvedAmt));
//				if(this.medicalDecisionTableObj.getReverseAllocationAmountConsidered() != null && !bean.getReverseAmountConsidered().equals(this.medicalDecisionTableObj.getReverseAllocationAmountConsidered())) {
//					
////					hasError = true;
////					eMsg += "Please make the change on Apportion amount correctly .</br>";
//				}
			}
		}
		
		if (!bean.getIsReverseAllocation() && !this.medicalDecisionTableObj.getTotalAmountConsidered().equals(
				SHAUtils.getIntegerFromString(this.bean.getAmountConsidered()))) {
			hasError = true;
			eMsg += "Total Amount Considered Should be equal to Amount Considered(A) of Sub limits, Package & SI Restriction Table </br>";
		}
		
		if(ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			if(this.bean.getIsAmbulanceApplicable() && this.medicalDecisionTableObj.getAmbulanceLimitAmount() != null
					&& ! this.bean.getAmbulanceLimitAmount().equals(this.medicalDecisionTableObj.getAmbulanceLimitAmount())){
				
				hasError = true;
				eMsg += "Amount Entered against Ambulance charges should be equal";
				
			}
		}
		
		if ((amountConsideredTable.getCoPayValue() != null && SHAUtils.getLongFromString(amountConsideredTable.getCoPayValue()) > 0) 
				|| (SHAUtils.isCopayAvailable(medicalDecisionTableObj.getValues()))) {
			if (txtCopayRemarks.getValue() == null || txtCopayRemarks.getValue().equals("")) {
				hasError = true;
				eMsg += "Please enter Co_pay Remarks";
			}
			
		}
		
		 /*if(! hasError && ! this.bean.getIsRechargePopUpOpened()){
				
				if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag() != null &&
						this.bean.getNewIntimationDTO().getPolicy().getProduct().getRechargeSiFlag().equalsIgnoreCase("Y")){
				
					if(approvedAmtField != null && approvedAmtField.getValue() != null && ! ("").equals(approvedAmtField.getValue())){
						Double approvedAmount = SHAUtils.getDoubleValueFromString(approvedAmtField.getValue());
						this.bean.setSublimitAndSIAmt(approvedAmount);
						Double balanceSI = this.bean.getBalanceSI();
						if(approvedAmount > balanceSI){
							
							fireViewEvent(PAHealthBillingProcessPagePresenter.RECHARGE_SI_FOR_PRODUCT, this.bean);
							Double balanceSI2 = this.bean.getBalanceSI();
							Double rechargedAmount = balanceSI2 - balanceSI;
							getAlertMessage("Balance Sum Insured Amount is recharged. Since Approved amount is greater than Balance SI. </br> Recharged Amount : "+rechargedAmount);
							this.bean.setIsRechargePopUpOpened(true);
							setApprovedAmount();
							return false;
						}
						
					}
				}
			}*/

		
		
		
		if(bean.getIsReverseAllocation() && !this.medicalDecisionTableObj.getTotalReverseAllocatedAmt().equals(SHAUtils.getIntegerFromString(this.bean.getReverseAmountConsidered())  )) {
			hasError = true;
			eMsg += "Total Final Approval Amt (B - Balance SI after Co-pay) Should be equal to Reverse column of Sub limits, Package & SI Restriction Table </br>";
		} else if(!hasError && !bean.getIsReverseAllocation() && (bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag() || (bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()))) {
				setApprovedAmount();
				Integer approvedAmt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null ? bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue() : 0;
				Integer cValue = this.medicalDecisionTableObj.getTotalCAmount();
				
				if(!cValue.equals(approvedAmt)) {
					hasError = true;
					if(!this.bean.getIsReverseAllocation()) {
						eMsg += "Please enter Reverse Allocation Amount.";
					} 
					this.bean.setIsReverseAllocation(true);
					
					this.medicalDecisionTableObj.setReverseAllocationColumn(String.valueOf(approvedAmt));
					bean.setReverseAmountConsidered(String.valueOf(approvedAmt));
				}  else {
//					this.bean.setIsReverseAllocation(false);
				}
		}
		
		if(amountConsideredTable != null && !amountConsideredTable.isValid()) {
			hasError = true;
			eMsg += "Amount Received from other Insurers field in Amount Considered Section should not be blank</br>";
		}
		
		if(!hasError) {
			setResidualAmtToDTO();
			bean.getPreauthMedicalDecisionDetails()
			.setMedicalDecisionTableDTO(
					medicalDecisionTableObj.getValues());
			if(this.bean.getHospitalizaionFlag() || this.bean.getIsHospitalizationRepeat() || this.bean.getPartialHospitalizaionFlag()) {
				setHospitalizationDetailsToDTO();
			} else {
				setHospitalizationDetailsToDTO();
			}
			
			if(this.bean.getPreHospitalizaionFlag()) {
				SHAUtils.setPreHospitalizationDetailsToDTO(bean, bean.getPreauthMedicalDecisionDetails().getOtherInsurerPreHospAmountClaimed());
			}  else {
				SHAUtils.setPreHospitalizationDetailsToDTOForChange(bean, bean.getPreauthMedicalDecisionDetails().getOtherInsurerPreHospAmountClaimed());
			}
			if(this.bean.getPostHospitalizaionFlag()) {
				SHAUtils.setPostHospitalizationDetailsToDTO(bean, bean.getPreauthMedicalDecisionDetails().getOtherInsurerPostHospAmountClaimed());
			} else  {
				SHAUtils.setPostHospitalizationDetailsToDTOForChange(bean, bean.getPreauthMedicalDecisionDetails().getOtherInsurerPostHospAmountClaimed());
			}
			
			bean.setOtherInsurerAmount(amountConsideredTable.getOtherInsurerAmount());
			if(txtCopayRemarks != null){
				bean.setCopayRemarks(txtCopayRemarks.getValue());
			}
			setApprovedAmount();
			if(bean.getHospitalizaionFlag() || bean.getIsHospitalizationRepeat() || bean.getPartialHospitalizaionFlag()) {
				// New requirement for saving Copay values to Transaction Table......... 
				SHAUtils.setCopayAmounts(bean, this.amountConsideredTable);
			}
			
			if(approvedAmtField != null && approvedAmtField.getValue() != null && ! ("").equals(approvedAmtField.getValue())){
				Double approvedAmount = SHAUtils.getDoubleValueFromString(approvedAmtField.getValue());
				this.bean.setSublimitAndSIAmt(approvedAmount);
			}
			List<Double> copayValues = new ArrayList<Double>();
			
			Double copayPercentage = bean.getAmountConsCopayPercentage();
			if(copayPercentage != null){
				copayValues.add(copayPercentage);
			}
			
			List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO = bean.getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO();
			for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : medicalDecisionTableDTO) {
				if(diagnosisProcedureTableDTO.getCoPayPercentage() != null && diagnosisProcedureTableDTO.getCoPayPercentage().getValue() != null){
					SelectValue coPayPercentage = diagnosisProcedureTableDTO.getCoPayPercentage();
					String strCopay = coPayPercentage.getValue();
					Double copay = SHAUtils.getDoubleFromStringWithComma(strCopay);
					copayValues.add(copay);
				}
			}
			
			if(! copayValues.isEmpty()){
				Double maximumCopay = Collections.max(copayValues);
				if(maximumCopay != null)
				bean.setCoPayValue(maximumCopay.doubleValue());
			}
			
			
		} else {
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
		
		
		return !hasError;
	}
	
	public Integer getConsideredAmountValue()
	{
		if(amountConsideredTable != null){
		return amountConsideredTable.getConsideredAmountValue();
		}else{
			return 0;
		}
		
	}
	
	public String getCoPayValue(){
		
		if(amountConsideredTable != null){
			return amountConsideredTable.getCoPayValue();
			}else{
				return "0";
			}
		
	}
	
	public Integer getBalanceSumInsuredAmt(){
		
		if(amountConsideredTable != null){
			return amountConsideredTable.getBalanceSumInsuredAmt();
			}else{
				return 0;
			}
	}
	
	 public void getAlertMessage(String eMsg){

			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("alertMessage");
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
	
	private void setResidualAmtToDTO() {
		List<DiagnosisProcedureTableDTO> values = this.medicalDecisionTableObj
				.getValues();
		for (DiagnosisProcedureTableDTO medicalDecisionTableDTO : values) {
			if (medicalDecisionTableDTO.getDiagOrProcedure() != null
					&& medicalDecisionTableDTO.getDiagOrProcedure().contains(
							"Residual")) {
				ResidualAmountDTO residualAmountDTO = this.bean
						.getResidualAmountDTO();
				residualAmountDTO
						.setAmountConsideredAmount(medicalDecisionTableDTO
								.getAmountConsidered() != null ? medicalDecisionTableDTO
								.getAmountConsidered().intValue() : 0d);
				residualAmountDTO.setNetAmount(medicalDecisionTableDTO
						.getNetAmount() != null ? medicalDecisionTableDTO
						.getNetAmount().doubleValue() : 0d);
				residualAmountDTO.setMinimumAmount(medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt() != null ? medicalDecisionTableDTO
						.getMinimumAmountOfAmtconsideredAndPackAmt().doubleValue() : 0d);
				residualAmountDTO.setCopayPercentage(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
								.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
										.getValue()) : 0d);
				residualAmountDTO.setCopayAmount(medicalDecisionTableDTO
						.getCoPayAmount() != null ? medicalDecisionTableDTO
						.getCoPayAmount().doubleValue() : 0d);
				residualAmountDTO.setApprovedAmount(medicalDecisionTableDTO
						.getMinimumAmount() != null ? medicalDecisionTableDTO
						.getMinimumAmount().doubleValue() : 0);
				residualAmountDTO.setNetApprovedAmount(medicalDecisionTableDTO
						.getReverseAllocatedAmt() != null ? medicalDecisionTableDTO
						.getReverseAllocatedAmt().doubleValue() : 0);
				residualAmountDTO.setRemarks(medicalDecisionTableDTO
						.getRemarks());
				residualAmountDTO.setCopayPercentage(medicalDecisionTableDTO
						.getCoPayPercentage() != null ? Double
						.valueOf(medicalDecisionTableDTO.getCoPayPercentage()
								.getValue()) : 0d);
				residualAmountDTO.setCoPayTypeId(medicalDecisionTableDTO.getCoPayType());
			}
		}
	}
	
	public Integer getPayableAmt() {
		return medicalDecisionTableObj.getPostHospAmt();
	}
	
	public void setBalanceSIforRechargedProcessing(Double balanceSI){
		if(balanceSI != null){
			this.bean.setBalanceSI(balanceSI);
			if(this.amountConsideredTable != null){
				this.amountConsideredTable.setBalanceSumInsuredAfterRecharge(balanceSI);
			}
		}
	}

	public void setPreauthRequestedAmount(String calculatePreRequestedAmt) {
		this.preAuthRequestedAmt = calculatePreRequestedAmt;
	}

	
	public void setCalculationValues() {
		if((bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.HEALTH_ALL_CARE) && (bean.getNewIntimationDTO().getHospitalType() != null && bean.getNewIntimationDTO().getHospitalType().getValue().toString().toLowerCase().contains("non")))) {
			fireViewEvent(PAHealthBillingProcessPagePresenter.GET_CLAIM_RESTRICTION, bean);
		}
		
		
		if(this.bean.getHospitalizaionFlag() || this.bean.getIsHospitalizationRepeat() || this.bean.getPartialHospitalizaionFlag()) {
			setHospitalizationDetailsToDTO();
		} 
		if(this.bean.getPostHospitalizaionFlag()) {
			SHAUtils.setPostHospitalizationDetailsToDTO(bean, bean.getPreauthMedicalDecisionDetails().getOtherInsurerPostHospAmountClaimed());
		}
		
		if(this.bean.getPreHospitalizaionFlag()) {
			SHAUtils.setPreHospitalizationDetailsToDTO(bean, bean.getPreauthMedicalDecisionDetails().getOtherInsurerPreHospAmountClaimed());
		} 
	}
	
	
	private void setApprovedAmount() {
		Integer totalApprovedAmt = this.medicalDecisionTableObj.getTotalCAmount();
		if(bean.getIsReverseAllocation()) {
			totalApprovedAmt = this.medicalDecisionTableObj.getTotalReverseAllocatedAmt();
		}
		Integer min = Math.min(amountConsideredTable.getMinimumValue(),totalApprovedAmt);
		Product product = bean.getNewIntimationDTO().getPolicy().getProduct();
		if(bean.getIsNonAllopathic()) {
			min = Math.min(min, bean.getNonAllopathicAvailAmt());
		}
		bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
		approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
		if ((bean.getStatusKey()
				.equals(ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS))) {
			
			approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
			bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(min.doubleValue());
			if(bean.getNewIntimationDTO() != null) {
				if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
					bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
					if(!bean.getIsReverseAllocation()) {
						amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
						approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
					} else {
						if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
							bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
						}
						Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
						amt -= ((bean.getPostHospitalisationValue() != null && SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) > 0) ? SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) : 0d);
						amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
					}
					
				}
			}
		} else {
			if(bean.getNewIntimationDTO() != null) {
				
				if(product.getCode() != null &&  ((ReferenceTable.SUPER_SURPLUS_INDIVIDUAL_CODE.equalsIgnoreCase(product.getCode())) || (ReferenceTable.SUPER_SURPLUS_FLOATER_CODE.equalsIgnoreCase(product.getCode())))) {
					min = Math.min(amountConsideredTable.getConsideredAmountValue(), totalApprovedAmt);
					approvedAmtField.setValue(String.valueOf(totalApprovedAmt));
					bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt((amountConsideredTable.getApprovedAmount()).doubleValue());
					if(!bean.getIsReverseAllocation()) {
						amountConsideredTable.hospApprovedAmountTxt.setValue(min.toString());
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
					} else {
						if(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() != null && !(bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt() > 0)) {
							bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(SHAUtils.getDoubleValueFromString(bean.getReverseAmountConsidered()));
						}
						Double amt = bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()  +  (bean.getDeductibleAmount().intValue() > 0 ? bean.getDeductibleAmount() : 300000d);
						amt -= ((bean.getPostHospitalisationValue() != null && SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) > 0) ? SHAUtils.getDoubleFromString(bean.getPostHospitalisationValue()) : 0d);
						amountConsideredTable.hospApprovedAmountTxt.setValue(amt.toString());
						bean.getPreauthMedicalDecisionDetails().setInitialTotalApprovedAmt(amountConsideredTable.doSuperSurplusCalculation().doubleValue());
					}
				}
			}
		}
	}
}
