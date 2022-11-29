package com.shaic.claim.reimbursement.paymentprocesscpuview;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GTextField;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementMapper;
import com.shaic.claim.ReportDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.StreamResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
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
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class PaymentProcessCpuPage extends ViewComponent {

	@Inject
	protected ViewDetails viewDetails;

	@Inject
	protected PaymentProcessCpuPageDTO pageDto;

	@Inject
	protected PaymentProcessCpuTableDTO tableDto;

	@EJB
	private ReimbursementService reimbursementService;

	private PaymentProcessCpuPageDTO bean;
	private BeanFieldGroup<PaymentProcessCpuPageDTO> binder;

	private TextField txtIntimationNo;
	private TextField txtClaimNo;
	private TextField txtMainMemberName;
	private TextField txtNameOfTheInsured;
	private TextField txtsettledAmount;
	private TextField txtadmissionDate;
	private TextField txtddNo;
	private TextField txtbankName;
	private TextField txtbillReceivedDate;
	private TextField txtpolicyNo;
	private TextField txtinsuredPatientName;
	private TextField txthospitalName;
	private TextArea txtaddress;
	private TextField txtdischargeDate;
	private TextField txtddDate;
	private TextField txtbillNumber;
	private TextField txtbillDate;
	private TextField txtCcZonalOfc;
	private TextField txtCcAreaOfc;
	private TextField txtBranchOfc;
	private TextField txtMailId;
	private Button btnDischargeVoucher;
	// private Button btnDvCoveringLetter;
	private Button btnHospitalPaymentLetter;
	private Button btnPaymentAndDischarge;
	private Button btnBillSummary;
	private Button btnConfirm;
	private Button btnClose;
	private Button btnAdd;
	private OptionGroup letterPrintingMode;
	private HashMap fileMap;

	public void init(PaymentProcessCpuPageDTO bean) {
		this.bean = bean;
		Panel mainPanel = new Panel("Claim Details");
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setContent(getContent());
		setCompositionRoot(mainPanel);
		this.fileMap = new HashMap();
		viewDetails.setRodNo(this.bean.getRodNumber());
	}

	public void initBinder() {
		this.binder = new BeanFieldGroup<PaymentProcessCpuPageDTO>(
				PaymentProcessCpuPageDTO.class);
		this.binder.setItemDataSource(bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public VerticalLayout getContent() {
		initBinder();
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		txtClaimNo = binder.buildAndBind("Claim No", "claimNumber",
				TextField.class);
		txtMainMemberName = binder.buildAndBind("Main Member Name",
				"mainMemberName", TextField.class);
		txtNameOfTheInsured = binder.buildAndBind("Name of the Insured",
				"nameOfTheInsured", TextField.class);
		txtsettledAmount = binder.buildAndBind("Settled Amount",
				"settledAmount", TextField.class);
		txtadmissionDate = binder.buildAndBind("Admission Date",
				"admissionDateValue", TextField.class);
		txtddNo = binder.buildAndBind("DD No.", "ddNo", TextField.class);
		txtbankName = binder.buildAndBind("Bank Name", "bankName",
				TextField.class);
		txtbillReceivedDate = binder.buildAndBind("Bill Received Date",
				"billReceivedDateValue", TextField.class);
		txtpolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		txtinsuredPatientName = binder.buildAndBind("Insured Patient Name",
				"insuredPatientName", TextField.class);
		txthospitalName = binder.buildAndBind("Hospital Name", "hospitalName",
				TextField.class);
		txtaddress = binder.buildAndBind("Address", "address", TextArea.class);
		txtdischargeDate = binder.buildAndBind("Discharge Date",
				"dischargeDateValue", TextField.class);
		txtddDate = binder.buildAndBind("DD Date", "ddDateValue",
				TextField.class);
		txtbillNumber = binder.buildAndBind("Bill Number", "billNumber",
				TextField.class);
		txtbillDate = binder.buildAndBind("Bill Date", "billDateValue",
				TextField.class);

		Panel ccDetailsPanel = new Panel("CC To Office");
		txtCcZonalOfc = binder.buildAndBind(" CC: Zonal Office", "ccZonalOfc",
				TextField.class);
		txtCcZonalOfc.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtCcAreaOfc = binder.buildAndBind(" CC: Area Office", "ccAreaOfc",
				TextField.class);
		txtCcAreaOfc.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtBranchOfc = binder.buildAndBind(" CC: Branch Office ",
				"ccBranchOfc", TextField.class);
		txtBranchOfc.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		FormLayout ccDetailLayout = new FormLayout(txtCcZonalOfc, txtCcAreaOfc,
				txtBranchOfc);
		ccDetailsPanel.setContent(ccDetailLayout);

		Panel exportAndPrintPanel = new Panel("Export & Print");

		btnDischargeVoucher = new Button("Discharge Voucher");
		btnHospitalPaymentLetter = new Button("Hospital Payment");
		btnPaymentAndDischarge = new Button("Payment And Discharge");
		btnBillSummary = new Button("Bill Summary");
		// btnBillSummary.setEnabled(false);

		if ((this.bean.getClaimType()
				.equalsIgnoreCase(ReferenceTable.CASHLESS_CLAIM))
				&& ((this.bean.getDocReceivedFrom())
						.equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL))
				&& (this.bean.getBillClassification()
						.equalsIgnoreCase(SHAConstants.HOSPITALIZATION_FLAG))) {
			btnPaymentAndDischarge.setEnabled(false);
		} else {
			btnHospitalPaymentLetter.setEnabled(false);
		}

		letterPrintingMode = (OptionGroup) binder.buildAndBind(
				"Letter printing completed", "letterPrintingMode",
				OptionGroup.class);
		// letterPrintingMode = new OptionGroup();
		letterPrintingMode.setRequired(true);
		letterPrintingMode.addItems(getReadioButtonOptions());
		letterPrintingMode.setItemCaption(true, "Yes");
		letterPrintingMode.setItemCaption(false, "No");
		//Vaadin8-setImmediate() letterPrintingMode.setImmediate(true);

		HorizontalLayout exportDetailLayout = new HorizontalLayout(
				btnDischargeVoucher, btnHospitalPaymentLetter,
				btnPaymentAndDischarge, btnBillSummary);
		HorizontalLayout exportDetailLayout1 = new HorizontalLayout(
				letterPrintingMode);
		VerticalLayout buttonLayout = new VerticalLayout(exportDetailLayout,
				exportDetailLayout1);
		buttonLayout.setMargin(true);
		buttonLayout.setSpacing(true);
		// btnBillSummary.setEnabled(false);
		// buttonLayout.setWidth("20%");
		buttonLayout.setComponentAlignment(exportDetailLayout,
				Alignment.MIDDLE_CENTER);
		buttonLayout.setComponentAlignment(exportDetailLayout1,
				Alignment.MIDDLE_CENTER);
		exportAndPrintPanel.setContent(buttonLayout);

		Panel mailIdPanel = new Panel("Mail id's");
		txtMailId = binder.buildAndBind("Mail ID", "mailId", TextField.class);

		btnAdd = new Button("Send Mail");
		FormLayout mailIdLayout = new FormLayout(txtMailId);
		HorizontalLayout mailidLayout = new HorizontalLayout(mailIdLayout,
				btnAdd);
		Label lableMail = new Label(
				"To mail to multiple ID's, pls add them seperated by , or ;");
		HorizontalLayout mailLable = new HorizontalLayout(lableMail);
		VerticalLayout mailLayout = new VerticalLayout(mailidLayout, mailLable);
		// mailidLayout.setMargin(true);
		// mailidLayout.setSpacing(true);
		mailIdPanel.setContent(mailLayout);

		btnConfirm = new Button("Submit");
		btnClose = new Button("Cancel");
		HorizontalLayout buttonLayout1 = new HorizontalLayout(btnConfirm,
				btnClose);
		buttonLayout1.setMargin(true);

		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo, txtClaimNo,
				txtMainMemberName, txtNameOfTheInsured, txtsettledAmount,
				txtadmissionDate, txtddNo, txtbankName, txtbillReceivedDate);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(txtpolicyNo,
				txtinsuredPatientName, txthospitalName, txtaddress,
				txtdischargeDate, txtddDate, txtbillNumber, txtbillDate);
		formLayoutRight.setSpacing(true);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,
				formLayoutRight);
		setReadOnly(formLayoutLeft, true);
		setReadOnly(formLayoutRight, true);
		fieldLayout.setMargin(true);
		VerticalLayout mainLayout = new VerticalLayout(fieldLayout,
				ccDetailsPanel, exportAndPrintPanel, mailIdPanel, buttonLayout1);
		mainLayout
				.setComponentAlignment(buttonLayout1, Alignment.MIDDLE_CENTER);

		addListenerForButtons();
		// this.bean.setFilePathAndTypeMap(fileMap);

		return mainLayout;
	}

	public void addListenerForButtons() {

		btnConfirm.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				String err = validate();
				if (err == null) {

					fireViewEvent(
							PaymentProcessCpuPagePresenter.SUBMIT_BUTTON_CLICK,
							bean);
					buildSuccessLayout();
				} else {
					showErrorMessage(err);
				}

			}
		});

		btnClose.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				wizardCancelled();

			}
		});

		btnDischargeVoucher.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				String dischargeVoucherUrl = null;
				String templateName = "";
				if (null != bean.getClaimDto().getNewIntimationDto().getLobId()
						.getId()
						&& (!ReferenceTable.PA_LOB_KEY.equals(bean
								.getClaimDto().getNewIntimationDto().getLobId()
								.getId()))) {
					if ((bean.getClaimType()
							.equalsIgnoreCase(ReferenceTable.CASHLESS_CLAIM))
							&& ((bean.getDocReceivedFrom())
									.equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL))
							&& (bean.getBillClassification()
									.equalsIgnoreCase(SHAConstants.HOSPITALIZATION_FLAG))) {
						bean.setAmountInwords(SHAUtils.getParsedAmount(bean
								.getSettledAmount()));
						 templateName = "CLDischargeVoucher";
						dischargeVoucherUrl = generateLetter(templateName,bean);
					} else {

						fireViewEvent(
								PaymentProcessCpuPagePresenter.DISCHARGEVOUCHER_BUTTON_CLICK,
								bean);
					}
				} else {
					Intimation intimationObj = reimbursementService.getIntimationByNo(bean.getIntimationNo());
					if(SHAConstants.PROCESS_CLAIM_TYPE.equals(intimationObj.getProcessClaimType())){
						MastersValue masterValue = bean.getReimbursementObj().getBenefitsId();
						bean.setTodayDate(new Date());
						if(bean.getIntrestAmount() == null || bean.getIntrestAmount().equals(SHAConstants.DOUBLE_DEFAULT_VALUE)){
							
							if( masterValue.getKey().equals(ReferenceTable.HOSP_BENEFIT_MASTER_VALUE) ){
								MastersValue masterValueDocAcknowLedgement = bean.getReimbursementObj().getDocAcknowLedgement().getDocumentReceivedFromId();
								
								if(masterValueDocAcknowLedgement.getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)){
									templateName = reimbursementService.getPaymentTemplateNameWithVersion(SHAConstants.PA_DISCHARGE_VOUCHER_WITH_OUT_INT_INSURED, bean.getModifiedDate());
									
								}else{
									templateName = reimbursementService.getPaymentTemplateNameWithVersion(SHAConstants.PA_DISCHARGE_VOUCHER_WITH_OUT_INT_HOSPITAL, bean.getModifiedDate());									
								}
							}
							else if(masterValue.getValue().equals(SHAConstants.PERMANENT_PARTIAL_DISABILITY) || masterValue.getValue().equals(SHAConstants.PERMANENT_TOTAL_DISABILITY) 
									||masterValue.getValue().equals(SHAConstants.TEMPORARY_TOTAL_DISABILITY)){
								templateName = reimbursementService.getPaymentTemplateNameWithVersion(SHAConstants.PA_DISCHARGE_VOUCHER_WITH_OUT_INT_DIS, bean.getModifiedDate());
							}
							else if(masterValue.getValue().equals(SHAConstants.DEATH)){
								templateName = reimbursementService.getPaymentTemplateNameWithVersion(SHAConstants.PA_DISCHARGE_VOUCHER_WITH_OUT_INT_DEATH, bean.getModifiedDate());
							}
							
						}else{
							if( masterValue.getKey().equals(ReferenceTable.HOSP_BENEFIT_MASTER_VALUE ) ){
								templateName = reimbursementService.getPaymentTemplateNameWithVersion(SHAConstants.PA_DISCHARGE_VOUCHER_WITH_INT_HOS, bean.getModifiedDate());
							}
							else if(masterValue.getValue().equals(SHAConstants.PERMANENT_PARTIAL_DISABILITY) || masterValue.getValue().equals(SHAConstants.PERMANENT_TOTAL_DISABILITY) 
									||masterValue.getValue().equals(SHAConstants.TEMPORARY_TOTAL_DISABILITY)){
								templateName = reimbursementService.getPaymentTemplateNameWithVersion(SHAConstants.PA_DISCHARGE_VOUCHER_WITH_INT_DIS, bean.getModifiedDate());
							}
							else if(masterValue.getValue().equals(SHAConstants.DEATH)){
								templateName = reimbursementService.getPaymentTemplateNameWithVersion(SHAConstants.PA_DISCHARGE_VOUCHER_WITH_INT_DEATH, bean.getModifiedDate());
							}
						}
					}else{
						templateName = "PADischargeVoucher";
					}
				}
				if(bean != null && templateName != null && !templateName.isEmpty()){
					dischargeVoucherUrl = generateLetter(templateName,bean);
					bean.setDischargeVoucherUrl(dischargeVoucherUrl);
				}
			}
		});

		btnPaymentAndDischarge.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(
						PaymentProcessCpuPagePresenter.PAYMENT_AND_DISCHARGE_BUTTON_CLICK,
						bean);
			}
		});

		btnHospitalPaymentLetter.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				
				fireViewEvent(PaymentProcessCpuPagePresenter.HOSP_PAYMENT_LETTER_CLICK, bean);
				
				// String templateName = "DVCoveringLetter";
				// String dvCoveringUrl =generateLetter(templateName);
			}

		});

		btnBillSummary.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if (null != bean.getRodNumber()
						&& null != bean.getIntimationNo()) {
					viewDetails.viewUploadedBillDocumentDetails(
							bean.getIntimationNo(), bean.getRodNumber(),
							SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS,
							SHAConstants.BILLASSESSMENTSHEETSCRC,
							SHAConstants.FINANCIAL_APPROVER);
				}

			}

		});

		letterPrintingMode
				.addValueChangeListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {

						if (null != event && null != event.getProperty()
								&& null != event.getProperty().getValue()) {
							Boolean isChecked = false;
							if (event.getProperty() != null
									&& event.getProperty().getValue() != null
									&& event.getProperty().getValue()
											.toString() == "true") {
								isChecked = true;
							}
							bean.setLetterPrintingMode(isChecked);
							bean.setLetterPrintingModeFlag(isChecked.toString());

						/*	String err = validateGenerateLetter();
							try {
								if (!err.equals(null)) {
									showErrorMessage(err);
								}

							} catch (Exception e) {

							}*/
						}
					}
				});

	}
	
	public String generateLetter(String templateName, PaymentProcessCpuPageDTO updatedDto) {
		List<AbstractTableDTO> claimDtoList = new ArrayList<AbstractTableDTO>();

		if(updatedDto != null && ("HospitalPaymentLetter_V3").equalsIgnoreCase(templateName) && updatedDto.getNetAmount() != null){
			updatedDto.setAmountInwords(SHAUtils.getParsedAmountWithRupeesOnly(updatedDto.getNetAmount()));
		}
		
		if(updatedDto != null && updatedDto.getTdsAmount() != null){
			updatedDto.setTdsAmountInWords(SHAUtils.getParsedAmountWithRupeesOnly(updatedDto.getTdsAmount()));
		}
		if(updatedDto != null && ("PAPaymentAndDischargeLetter_V7").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PAPaymentAndDischargeLetter_V8").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PAPaymentAndDischargeLetter_V9").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PAPaymentAndDischargeLetterOutInsured_V10").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PAPaymentAndDischargeLetter_V11").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PAPaymentAndDischargeLetter_V12").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PAPaymentAndDischargeLetterHospital_V13").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PADischargeVoucherIntDeath_V3").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PADischargeVoucherIntHos_V1").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PADischargeVoucherIntDis_V2").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PADischargeVoucherOutInsured_V4").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PADischargeVoucherOutDis_V5").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PADischargeVoucherOutDeath_V6").equalsIgnoreCase(templateName) ||
				updatedDto != null && ("PADischargeVoucherOutHospital_V7").equalsIgnoreCase(templateName)){
			if(updatedDto.getIntrestAmount() != null){
				updatedDto.setIntrestAmountInWords(SHAUtils.getParsedAmountWithRupeesOnly(updatedDto.getIntrestAmount()));
			}
			Double interestAmnt = null != updatedDto.getIntrestAmount() ? updatedDto
					.getIntrestAmount() : 0d;
			Double totalAmnt = updatedDto.getSettledAmount() + interestAmnt;
			updatedDto.setTotalAmntInWords(SHAUtils.getParsedAmountWithRupeesOnly(totalAmnt));
			
			if(updatedDto != null && updatedDto.getReimbursementObj() != null && updatedDto.getReimbursementObj().getKey() != null && updatedDto.getReimbursementObj().getNomineeFlag() != null && !updatedDto.getReimbursementObj().getNomineeFlag().isEmpty() && updatedDto.getReimbursementObj().getNomineeFlag().equals(SHAConstants.NOMINEE_FLAG)) {
				LegalHeir legalHeir = reimbursementService.getlegalHeirListByTransactionKey(updatedDto.getReimbursementObj().getKey());
				if(legalHeir != null){
					if(legalHeir.getAddress() != null){
						legalHeir.setAddress(legalHeir.getAddress().trim());
					}
					if(legalHeir.getAddress_2() != null){
						legalHeir.setAddress_2(legalHeir.getAddress_2().trim());
					}
					updatedDto.setLegalHeir(legalHeir);
				}
			}
		}
		
		if (("PADischargeVoucher").equalsIgnoreCase(templateName) && updatedDto.getReimbursementObj() != null) {
			ReimbursementDto reimbDto = (new ReimbursementMapper())
					.getReimbursementDto(updatedDto.getReimbursementObj());
			reimbDto.setClaimDto(updatedDto.getClaimDto());
			
			reimbDto.setBenApprovedAmt(updatedDto.getReimbursementObj().getBenApprovedAmt());
			reimbDto.setAddOnCoversApprovedAmount(updatedDto.getReimbursementObj().getAddOnCoversApprovedAmount());
			reimbDto.setOptionalApprovedAmount(updatedDto.getReimbursementObj().getOptionalApprovedAmount());
			reimbDto.setDeathReason(updatedDto.getReimbursementObj().getDeathReason());
			Double dvApprovedAmt = (updatedDto.getReimbursementObj()
					.getBenApprovedAmt() != null ? updatedDto.getReimbursementObj()
					.getBenApprovedAmt() : 0d)
					+ (updatedDto.getReimbursementObj()
							.getAddOnCoversApprovedAmount() != null ? updatedDto
							.getReimbursementObj()
							.getAddOnCoversApprovedAmount() : 0d)
					+ (updatedDto.getReimbursementObj().getOptionalApprovedAmount() != null ? updatedDto
							.getReimbursementObj().getOptionalApprovedAmount()
							: 0d);
			reimbDto.setBenApprovedAmt(dvApprovedAmt);
			ReimbursementQueryDto reimbQueryDto = new ReimbursementQueryDto();
			reimbQueryDto.setReimbursementDto(reimbDto);
			reimbQueryDto.setPaApprovedAmtInwords(SHAUtils
					.getParsedAmount(dvApprovedAmt));
			
			/*if(reimbQueryDto.getReimbursementDto().getClaimDto().getIncidenceFlagValue() != null
					&& SHAConstants.DEATH_FLAG.equalsIgnoreCase(reimbQueryDto.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
					&& (reimbQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() == null
						|| (reimbQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() != null
								&& reimbQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()))) {
				
				if(reimbQueryDto.getReimbursementDto().getNomineeName() != null) {
					reimbQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(reimbQueryDto.getReimbursementDto().getNomineeName());
				}
		
				if(reimbQueryDto.getReimbursementDto().getNomineeAddr() != null) {
					reimbQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeAddr(reimbQueryDto.getReimbursementDto().getNomineeAddr());
				}
			}*/
			
			if(reimbQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
					&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reimbQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
					&& ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(reimbQueryDto.getReimbursementDto().getClaimDto().getIncidenceFlagValue())
							|| updatedDto.getReimbursementObj().getBenefitsId() != null && ReferenceTable.DEATH_BENEFIT_MASTER_VALUE.equals(updatedDto.getReimbursementObj().getBenefitsId()))
					&& updatedDto.getReimbursementObj().getDocAcknowLedgement().getDocumentReceivedFromId() != null
					&& ReferenceTable.RECEIVED_FROM_INSURED.equals(updatedDto.getReimbursementObj().getDocAcknowLedgement().getDocumentReceivedFromId().getKey())) {
				
				if(reimbQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() == null ||
						reimbQueryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()){
					
					List<LegalHeirDTO> legalHeirList = this.bean.getLegalHeirDTOList();
					if(legalHeirList != null && !legalHeirList.isEmpty()) {
						Path tempDir = SHAFileUtils.createTempDirectory("dummyHolder");
						ArrayList<File> filelistForMerge = new ArrayList<File>();
						
						for (LegalHeirDTO legalHeirDTO : legalHeirList) {
							reimbQueryDto.getReimbursementDto().setNomineeName(legalHeirDTO.getHeirName());
							reimbQueryDto.getReimbursementDto().setNomineeAddr(legalHeirDTO.getAddress()+(legalHeirDTO.getPincode() != null ? ("\nPinCode : "+legalHeirDTO.getPincode()): ""));
							claimDtoList.add(reimbQueryDto);
							
							//TODO
							/*dischargefilePath = docGenarator.generatePdfDocument(dischargeTemplateName, reportDto);
							try{
								File fl = new File(filePath);
								filelistForMerge.add(fl);
							}
							catch(Exception e) {
//								e.printStackTrace();
							}*/   //TODO
						}
						
						//TODO
						/*if(filelistForMerge != null && !filelistForMerge.isEmpty()) {
							File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bean.getReimbursementDto().getClaimDto().getClaimId().replaceAll(File.separator, "_"));
							dischargefilePath =  mergedDoc.getAbsolutePath();
						}*/  //TODO
						
					}
					
				}
					else {
					claimDtoList.add(reimbQueryDto);
				}		
			}
	
		}
		else if (  ("PADischargeVoucherIntDeath_V3").equalsIgnoreCase(templateName) && updatedDto.getReimbursementObj() != null 
				|| ("PADischargeVoucherIntHos_V1").equalsIgnoreCase(templateName) && updatedDto.getReimbursementObj() != null
				|| ("PADischargeVoucherIntDis_V2").equalsIgnoreCase(templateName) && updatedDto.getReimbursementObj() != null
				|| ("PADischargeVoucherOutInsured_V4").equalsIgnoreCase(templateName) && updatedDto.getReimbursementObj() != null 
				|| ("PADischargeVoucherOutDis_V5").equalsIgnoreCase(templateName) && updatedDto.getReimbursementObj() != null
				|| ("PADischargeVoucherOutDeath_V6").equalsIgnoreCase(templateName) && updatedDto.getReimbursementObj() != null
				|| ("PADischargeVoucherOutHospital_V7").equalsIgnoreCase(templateName) && updatedDto.getReimbursementObj() != null) {
			    
			    ReimbursementDto reimbDto = (new ReimbursementMapper())
						.getReimbursementDto(updatedDto.getReimbursementObj());
				reimbDto.setClaimDto(updatedDto.getClaimDto());
				
				reimbDto.setBenApprovedAmt(updatedDto.getReimbursementObj().getBenApprovedAmt());
				reimbDto.setAddOnCoversApprovedAmount(updatedDto.getReimbursementObj().getAddOnCoversApprovedAmount());
				reimbDto.setOptionalApprovedAmount(updatedDto.getReimbursementObj().getOptionalApprovedAmount());
				reimbDto.setDeathReason(updatedDto.getReimbursementObj().getDeathReason());
				Double dvApprovedAmt = (updatedDto.getReimbursementObj()
						.getBenApprovedAmt() != null ? updatedDto.getReimbursementObj()
						.getBenApprovedAmt() : 0d)
						+ (updatedDto.getReimbursementObj()
								.getAddOnCoversApprovedAmount() != null ? updatedDto
								.getReimbursementObj()
								.getAddOnCoversApprovedAmount() : 0d)
						+ (updatedDto.getReimbursementObj().getOptionalApprovedAmount() != null ? updatedDto
								.getReimbursementObj().getOptionalApprovedAmount()
								: 0d);
				reimbDto.setBenApprovedAmt(dvApprovedAmt);
				ReimbursementQueryDto reimbQueryDto = new ReimbursementQueryDto();
				reimbQueryDto.setReimbursementDto(reimbDto);
				reimbQueryDto.setPaApprovedAmtInwords(SHAUtils
						.getParsedAmount(dvApprovedAmt));
				
				claimDtoList.add(updatedDto);
			}
		else{
			claimDtoList.add(updatedDto);
		}
		
		DocumentGenerator docGenarator = new DocumentGenerator();
		String fileUrl = null;
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(updatedDto.getClaimNumber());
		reportDto.setBeanList(claimDtoList);
		
		fileUrl = docGenarator.generatePdfDocument(templateName, reportDto);

		final String finalFilePath = fileUrl;
		/*
		 * if((SHAConstants.DV_COVERING_LETTER).equalsIgnoreCase(templateName))
		 * {
		 * 
		 * fileMap.put("DVCoveringLetterFilePath", finalFilePath);
		 * fileMap.put("DVCoveringLetterDocType"
		 * ,SHAConstants.DV_COVERING_LETTER); }
		 */
		if ((SHAConstants.HOSPITAL_PAYMENT_LETTER)
				.equalsIgnoreCase(templateName)) {

			fileMap.put("HospitalPaymentLetterFilePath", finalFilePath);
			fileMap.put("HospitalPaymentLetterDocType",
					SHAConstants.HOSPITAL_PAYMENT_LETTER);
		}
		
		/**
		 *  Commented as to stop DMS Uploading of Letters as per Management Change  on 19-05-2018 
		 */

		/*else if ((SHAConstants.DISCHARGE_VOUCHER_LETTER)
				.equalsIgnoreCase(templateName)) {

			fileMap.put("DischargeVoucherFilePath", finalFilePath);
			fileMap.put("DischargeVoucherDocType",
					SHAConstants.DISCHARGE_VOUCHER_LETTER);
		}

		else if ((SHAConstants.PAYMENT_AND_DISCHARGE_VOUCHER)
				.equalsIgnoreCase(templateName)) {
			fileMap.put("PaymentVoucherFilePath", finalFilePath);
			fileMap.put("PaymentVoucherDocType",
					SHAConstants.PAYMENT_AND_DISCHARGE_VOUCHER);
		}
		
		this.bean.setFilePathAndTypeMap(fileMap);
		
		*/
		
		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(finalFilePath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(finalFilePath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};*/

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
		VerticalLayout templateLayout = new VerticalLayout(e);
		templateLayout.setHeight("450px");

		if (!ValidatorUtils.isNull(fileUrl)) {

			openPdfFileInWindow(fileUrl, templateName);
		} else {
			// Exception while PDF Letter Generation

		}

		return fileUrl;
	}

	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("300px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof GTextField) {
				GTextField field = (GTextField) c;
				field.setWidth("300px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

	@SuppressWarnings("static-access")
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Record Confirmed Successful !!!</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

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
				fireViewEvent(MenuItemBean.PAYMENT_PROCESS_CPU, null);

			}
		});
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
		letterPrintingMode.setValue(null);
	}

	public void wizardCancelled() {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ?", "No", "Yes",
				new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (!dialog.isConfirmed()) {
							// Confirmed to continue

							fireViewEvent(MenuItemBean.PAYMENT_PROCESS_CPU,
									null);
						} else {
							// User did not confirm
						}
					}
				});

		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);

	}

	public void openPdfFileInWindow(final String filepath, String letterType) {

		Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption(letterType + " PDF");
		window.setWidth("800");
		window.setHeight("600");
		window.setModal(true);
		window.center();

		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(filepath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

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
*/
		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
		window.setContent(e);
		UI.getCurrent().addWindow(window);
	}

	protected Collection<Boolean> getReadioButtonOptions() {

		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	 
	 public String validate()
		{
			//String err = "";
			
			if(bean.getLetterPrintingMode()== null)
			{
				return "Please Select Letter Printing Completed Option";
			}		
			
			return null;
			
		}
	 
	 public String validateGenerateLetter()
	 
	 	{
		 
		
			//String err = "";
			
			if((bean.getClaimType().equalsIgnoreCase(ReferenceTable.CASHLESS_CLAIM)) && ((bean.getDocReceivedFrom()).equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL))
					&& (bean.getBillClassification().equalsIgnoreCase(SHAConstants.HOSPITALIZATION_FLAG)))	  
			{
			
			if((bean.getDischargeVoucherUrl() == null || bean.getHospitalDischargeVoucher() == null))
			{				
				
				if((bean.getDischargeVoucherUrl() == null && bean.getHospitalDischargeVoucher() == null) && (bean.getLetterPrintingMode().equals(true)))
				{
					return "\nPlease generate Discharge Voucher and Hospital Payment Letter";
				}
				
				else
				{
					if(bean.getDischargeVoucherUrl() == null && (bean.getLetterPrintingMode().equals(true)))
					{
						//String dishargeVoucherUrl = bean.getDischargeVoucherUrl();
						return "\nPlease generate Discharge Voucher Letter";
					}
					if(bean.getHospitalDischargeVoucher() == null && (bean.getLetterPrintingMode().equals(true)))
					{
						//String hospitalPaymentUrl = bean.getHospitalDischargeVoucher();
						return "\nPlease generate Hospital Payment Letter";
					}
				}
				
				
			}
			if(( null != bean.getDischargeVoucherUrl() || null != bean.getHospitalDischargeVoucher()) && bean.getLetterPrintingMode().equals(false))
			{
				return "\nLetter is already generated Please select Letter printing completed option Yes";
			}
			}
			else
			{
				if((bean.getDischargeVoucherUrl() == null || bean.getPaymentDischargeUrl() == null))
				{				
					
					if((bean.getDischargeVoucherUrl() == null && bean.getPaymentDischargeUrl() == null) && (bean.getLetterPrintingMode().equals(true)))
					{
						return "\nPlease generate Discharge Voucher and Payment And Dischage Letter";
					}
					else
					{
						if(bean.getDischargeVoucherUrl() == null && (bean.getLetterPrintingMode().equals(true)))
						{
							//String dishargeVoucherUrl = bean.getDischargeVoucherUrl();
							return "\nPlease generate Discharge Voucher Letter";
						}
						if(bean.getPaymentDischargeUrl() == null && (bean.getLetterPrintingMode().equals(true)))
						{
							//String paymentDischargeUrl = bean.getPaymentDischargeUrl();
							return "\nPlease generate Payment And Dischage Letter";
						}
					}
					
					
				}
				
				if(( null != bean.getDischargeVoucherUrl() || null != bean.getPaymentDischargeUrl()) && bean.getLetterPrintingMode().equals(false))
				{
					return "\nLetter is already generated Please select Letter printing completed option Yes";
				}
			}
			
			return null;
			
		}
	 
	 public void generatePaymentAndDischargeLetter(String templateName, PaymentProcessCpuPageDTO updatedDto){
			Double interestAmnt = null != updatedDto.getIntrestAmount() ? updatedDto
					.getIntrestAmount() : 0d;
			Double totalAmnt = updatedDto.getSettledAmount() + interestAmnt;
			updatedDto.setTotalAmntInWords(SHAUtils.getParsedAmount(totalAmnt));
			updatedDto.setPaymentDischargeUrl(generateLetter(templateName,updatedDto));
			bean = updatedDto;
	 }
	 
	 public void generateDischargeVoucherLetter(String templateName, PaymentProcessCpuPageDTO updatedDto){
		    updatedDto.setDischargeVoucherUrl(generateLetter(templateName,updatedDto));
			bean = updatedDto;
	 }
	 
	 public void generateHospitalPaymentLetter(String templateName, PaymentProcessCpuPageDTO updatedDto){
		 
		 updatedDto.setAmountInwords(SHAUtils.getParsedAmount(updatedDto.getNetAmount() != null ? updatedDto
					.getNetAmount() : 0d));

			updatedDto.setHospitalDischargeVoucher(generateLetter(templateName,updatedDto));
			bean = updatedDto;
	 }
}