package com.shaic.claim.rod.wizard.forms;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.reimbursement.billing.pages.billHospitalization.BillingHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.billing.pages.billreview.BillingReviewPagePresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.billreview.FinancialReviewPagePresenter;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.paclaim.financial.claimapproval.nonhosiptalpagereview.PAClaimAprNonHosReviewPagePresenter;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.billreview.PANonHospFinancialReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.bililnghosp.PAHealthBillingHospitalizationPagePresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billreview.PAHealthBillingReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.financial.pages.billinghospitalization.PAHealthFinancialHospitalizationPagePresenter;
import com.shaic.paclaim.health.reimbursement.financial.pages.billreview.PAHealthFinancialReviewPagePresenter;
import com.shaic.paclaim.rod.createrod.search.PACreateRODDocumentDetailsPresenter;
import com.shaic.reimbursement.paymentprocess.createbatch.search.SearchCreateBatchPresenter;
import com.shaic.restservices.bancs.claimprovision.BankDetailsResponse;
import com.shaic.restservices.bancs.claimprovision.ClaimProvisionService;
import com.shaic.restservices.bancs.claimprovision.PartyBankDetails;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class BankDetailsTable extends ViewComponent{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<BankDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<BankDetailsTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<BankDetailsTableDTO> container = new BeanItemContainer<BankDetailsTableDTO>(BankDetailsTableDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Set<String> errorMessages;
	
	private static Validator validator;
	
	private Map<String, Object> referenceData;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	private ReceiptOfDocumentsDTO bean;
	
	private String presenterString = "";
	
	private ViewDetails objViewDetails;
	
	@Inject
	private Instance<AddBankDetailsTable> addBanksDetailsTableInstance;
	
	private AddBankDetailsTable addBanksDetailsTableObj;
	
	private HashMap<String, String> enteredValues = new HashMap<String, String>();


	private NomineeDetailsDto nomineeDto;
	
	private CreateAndSearchLotTableDTO bankDetailsDTO;
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init(ReceiptOfDocumentsDTO bean) {
		
	
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new HashSet<String>();
		
		btnAdd = new Button("Add Bank Details");
		
		addBanksDetailsTableObj =  addBanksDetailsTableInstance.get();
		addBanksDetailsTableObj.setBankDetailObj(this);
		initTable(bean);
		table.setWidth("90%");
		table.setHeight("150px");
		table.setPageLength(table.getItemIds().size());
		table.setSortDisabled(true);
		addListener(bean);
	
		FormLayout formLayoutRight = new FormLayout(btnAdd,table);
		formLayoutRight.setSpacing(true);
		formLayoutRight.setMargin(true);
		
		HorizontalLayout horLayout = new HorizontalLayout(formLayoutRight);
		horLayout.setSpacing(false);
		
		Panel tblPanel = new Panel();
		tblPanel.setWidth("100%");
		tblPanel.setHeight("250px");
		tblPanel.setContent(horLayout);

	
		setCompositionRoot(tblPanel);
	}
	
	private void addListener(ReceiptOfDocumentsDTO dto) {	

		btnAdd.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				
				final Window popup = new com.vaadin.ui.Window();
				AddBanksDetailsTableDTO addBanksDetailsTableDTO = new AddBanksDetailsTableDTO();
				//addBanksDetailsTableObj =  addBanksDetailsTableInstance.get();
				addBanksDetailsTableObj.init(addBanksDetailsTableDTO,popup,dto,SHAConstants.ADD_BANK,null);
				
				List<BankDetailsTableDTO> beanDto = getValues();
				
//				btnAdd.setData(beanDto);
				addBanksDetailsTableObj.setExistingData(beanDto);
				
				String popupStr = "";
				if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString)) {
					popupStr = CreateRODDocumentDetailsPresenter.ADD_BANK_IFSC_DETAILS_CROD;
				}
				else if(SHAConstants.BILLING_SCREEN.equalsIgnoreCase(presenterString)) {
					popupStr = BillingHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_BILLING;
				}
				else if(SHAConstants.FINANCIAL_APPROVER.equalsIgnoreCase(presenterString)) {
					popupStr = FinancialHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_FA;
				}
				if(SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString)) {
					popupStr = PACreateRODDocumentDetailsPresenter.ADD_BANK_IFSC_DETAILS_PA_CROD;
				}
				else if(SHAConstants.PA_HEALTH_BILLING_SCREEN.equalsIgnoreCase(presenterString)) {
					popupStr = PAHealthBillingHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_PA_BILLING;
				}
				else if(SHAConstants.PA_HEALTH_FINANCIAL_APPROVER.equalsIgnoreCase(presenterString)) {
					popupStr = PAHealthFinancialHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_PA_FA;
				}
				
				else if(SHAConstants.PA_CLAIM_APPROVAL.equalsIgnoreCase(presenterString)) {
					popupStr = PAClaimAprNonHosReviewPagePresenter.ADD_BANK_IFSC_DETAILS_PA_NON_HOSP_CA;
				}
				else if(SHAConstants.PA_FINANCIAL_APPROVER.equalsIgnoreCase(presenterString)) {
					popupStr = PANonHospFinancialReviewPagePresenter.ADD_BANK_IFSC_DETAILS_PA_NON_HOSP_FA;
				}
				else if(SHAConstants.PAYMENT_VERIFICATION_LVL2.equalsIgnoreCase(presenterString) ||
						SHAConstants.PAYMENT_VERIFICATION_LVL1.equalsIgnoreCase(presenterString)) {
					popupStr = presenterString;
				}
				addBanksDetailsTableObj.initPresenter(popupStr);
			//	addBanksDetailsTableObj.addBean(addBanksDetailsTableDTO);
				addBanksDetailsTableObj.setCaption("Add Bank Details");
				
				popup.setWidth("75%");
				popup.setHeight("60%");
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
				
		
				VerticalLayout vlayout = new VerticalLayout(addBanksDetailsTableObj);
				
				popup.setContent(vlayout);
				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
        		
		 }
		});
	}
	
	void initTable(ReceiptOfDocumentsDTO bean) {
		// Create a data source and bind it to a table
		
		this.bean = bean;
		table = new Table("", container);
		generatecolumns(bean);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		getBankDetails();
		
		table.setVisibleColumns(new Object[] {"serialNumber","select","viewLink","update","preference","accountType","ifscCode","accountNumber","namePerBankAccnt","nameOfBank","branchName","micrCode",/*"panNo","virtualPaymentAddr",*/"effectiveFromDate","effectiveToDate"});
		table.setColumnHeader("select", "");
		table.setColumnHeader("viewLink", "");
		table.setColumnHeader("update", "");
		table.setColumnHeader("serialNumber", "Sl No");
		table.setColumnHeader("preference", "Preference");
		table.setColumnHeader("accountType", "Account Type");
		table.setColumnHeader("ifscCode", "IFSC Code");
		table.setColumnHeader("accountNumber", "Account Number");
		table.setColumnHeader("namePerBankAccnt", "Name as per Bank AC");
		table.setColumnHeader("nameOfBank", "Name of the Bank");
		table.setColumnHeader("branchName", "Branch Name");
		table.setColumnHeader("micrCode", "MICR Code");
		/*table.setColumnHeader("panNo", "Pan No");
		table.setColumnHeader("virtualPaymentAddr", "Virtual payment address");*/
		table.setColumnHeader("effectiveFromDate", "Effective from date");
		table.setColumnHeader("effectiveToDate", "Effective to date");
		table.setEditable(true);
		
		table.setTableFieldFactory(new ImmediateFieldFactory());

		
	}

	public void getBankDetails() {
		BeanItemContainer<BankDetailsTableDTO> bankContainer = new BeanItemContainer<BankDetailsTableDTO>(BankDetailsTableDTO.class);
		BankDetailsResponse bankDetails = null;
		if(bean.getPolicyNo() != null) {
			bankDetails = ClaimProvisionService.getInstance().callBankDetailsService(bean.getPolicyNo(),bean.getSourceRiskID());
		}else{
		bankDetails = ClaimProvisionService.getInstance().callBankDetailsService(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),bean.getSourceRiskID());
		}
		if(bankDetails !=null){
			List<PartyBankDetails> partyBankDetails = bankDetails.getPartyBankDetails();
			if(partyBankDetails !=null){
				boolean primaryAvailable = false;
				boolean secAvailable = false;
				for (PartyBankDetails bank : partyBankDetails) {
					List<BankDetailsTableDTO> beanDto = bank.getBankDetails();
					bankContainer.addAll(beanDto);
					if(beanDto != null && !beanDto.isEmpty()) {
						for (BankDetailsTableDTO bankDetailsTableDTO : beanDto) {
							if(bankDetailsTableDTO.getAccountType().equalsIgnoreCase("PRIMARY"))
								primaryAvailable = true;
							if(bankDetailsTableDTO.getAccountType().equalsIgnoreCase("SECONDARY"))
								secAvailable = true;
						}
					}
					btnAdd.setData(beanDto);
				}
				btnAdd.setEnabled(!(primaryAvailable && secAvailable));
			}
		}
		
		table.setContainerDataSource(bankContainer);
		table.setVisibleColumns(new Object[] {"serialNumber","select","viewLink","update","preference","accountType","ifscCode","accountNumber","namePerBankAccnt","nameOfBank","branchName","micrCode",/*"panNo","virtualPaymentAddr",*/"effectiveFromDate","effectiveToDate"});
	}
	
	@SuppressWarnings("deprecation")
	private void generatecolumns(ReceiptOfDocumentsDTO dto) {
		
		table.removeGeneratedColumn("viewLink");
		table.addGeneratedColumn("viewLink", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
			
			Button button = new Button("View Details");
			button.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					showClaimsDMSView(bean.getNewIntimationDTO().getIntimationId());
				}
			});
			button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	    	button.setWidth("80px");
	    	button.addStyleName(ValoTheme.BUTTON_LINK);
			return button;
		}
		});
		
		table.removeGeneratedColumn("select");
		table.addGeneratedColumn("select", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
			
			Button button = new Button("Select");
			button.setData(itemId);

			button.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					BankDetailsTableDTO itemId = (BankDetailsTableDTO) button.getData();
					
					ViewSearchCriteriaTableDTO viewDto = new ViewSearchCriteriaTableDTO(itemId);
					
					if(SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString)) {
					
						if(bean.getDocumentDetails().getPatientStatus() != null && ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getDocumentDetails().getPatientStatus().getId())) {
							
							fireViewEvent(PACreateRODDocumentDetailsPresenter.SETUP_NOMINEE_IFSC_DETAILS_PA_CROD, viewDto);
						}
						else {
							fireViewEvent(PACreateRODDocumentDetailsPresenter.SETUP_IFSC_DETAILS, viewDto);
						}	
					}
					if(SHAConstants.PA_CLAIM_APPROVAL.equalsIgnoreCase(presenterString)) {
					
						if(bean.getDocumentDetails().getPatientStatus() != null && ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getDocumentDetails().getPatientStatus().getId())) {
							
							fireViewEvent(PAClaimAprNonHosReviewPagePresenter.SETUP_NOMINEE_IFSC_PA_NON_HOSP_CLAIM_APPROVAL, viewDto);
						}
						else {
							fireViewEvent(PAClaimAprNonHosReviewPagePresenter.FA_SETUP_IFSC_DETAILS, viewDto);
						}	
					}
					
					else if(SHAConstants.PA_HEALTH_BILLING_SCREEN.equalsIgnoreCase(presenterString)) {
						if(bean.getDocumentDetails().getPatientStatus() != null && ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getDocumentDetails().getPatientStatus().getId())) {
							fireViewEvent(PAHealthBillingReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_PA_HOSP_BILLING, viewDto);
						}
						else {
							fireViewEvent(PAHealthBillingHospitalizationPagePresenter.FA_SETUP_IFSC_DETAILS, viewDto);
						}	
					}
					else if(SHAConstants.PA_HEALTH_FINANCIAL_APPROVER.equalsIgnoreCase(presenterString)) {
						
						if(bean.getDocumentDetails().getPatientStatus() != null && ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getDocumentDetails().getPatientStatus().getId())) {
							fireViewEvent(PAHealthFinancialReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_PA_FA, viewDto);
						}
						else {
							fireViewEvent(PAHealthFinancialHospitalizationPagePresenter.FA_SETUP_IFSC_DETAILS, viewDto);
						}	
					}
					else if(SHAConstants.PA_FINANCIAL_APPROVER.equalsIgnoreCase(presenterString)) {
						
						if(bean.getDocumentDetails().getPatientStatus() != null && ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getDocumentDetails().getPatientStatus().getId())) {
							fireViewEvent(PANonHospFinancialReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_PA_FA, viewDto);
						}
						else {
							fireViewEvent(PANonHospFinancialReviewPagePresenter.FA_SETUP_IFSC_DETAILS, viewDto);
						}	
					}
					
					else if(SHAConstants.FINANCIAL_APPROVER.equalsIgnoreCase(presenterString)) {
					
						if(bean.getDocumentDetails().getPatientStatus() != null && ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getDocumentDetails().getPatientStatus().getId())) {
							fireViewEvent(FinancialReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_FA, viewDto);
						}
						else {
							fireViewEvent(FinancialHospitalizationPagePresenter.FA_SETUP_IFSC_DETAILS, viewDto);
						}	
					}					
					else if(SHAConstants.BILLING_SCREEN.equalsIgnoreCase(presenterString)) {
						if(bean.getDocumentDetails().getPatientStatus() != null && ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getDocumentDetails().getPatientStatus().getId())) {
							fireViewEvent(BillingReviewPagePresenter.SETUP_NOMINEE_IFSC_DETAILS_BILLING, viewDto);
						}
						else {
							fireViewEvent(BillingHospitalizationPagePresenter.FA_SETUP_IFSC_DETAILS, viewDto);
						}	
					}

					else if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString)) {
						if(bean.getDocumentDetails().getPatientStatus() != null && ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getDocumentDetails().getPatientStatus().getId())) {
					
							fireViewEvent(CreateRODDocumentDetailsPresenter.SETUP_NOMINEE_IFSC_DETAILS, viewDto);
						}
						else {
							fireViewEvent(CreateRODDocumentDetailsPresenter.SETUP_IFSC_DETAILS, viewDto);
						}
					}	
					else if(SHAConstants.PAYMENT_VERIFICATION_LVL2.equalsIgnoreCase(presenterString) ||
							SHAConstants.PAYMENT_VERIFICATION_LVL1.equalsIgnoreCase(presenterString)) {
						fireViewEvent(SearchCreateBatchPresenter.PAYEE_BANK_DETAILS, itemId);
					}
					
					((Window)table.getParent().getParent().getParent().getParent().getParent().getParent()).close();
				}
			});
			button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	    	button.setWidth("60px");
	    	button.addStyleName(ValoTheme.BUTTON_LINK);
			return button;
		}
		});
		
		table.removeGeneratedColumn("update");
		table.addGeneratedColumn("update", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
			
			Button button = new Button("Update");
			button.setData(itemId);
			BankDetailsTableDTO currentRow = (BankDetailsTableDTO) itemId;
			
			if(currentRow.getPreference() != null
					&& currentRow.getPreference().equalsIgnoreCase("PRIMARY")) {
				button.setEnabled(false);	
			}
			else {
				button.setEnabled(true);
			}
			
			button.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					Object currentItemId = event.getButton().getData();
					BankDetailsTableDTO banksDetailsTableDTO = (BankDetailsTableDTO) currentItemId;
					final Window popup = new com.vaadin.ui.Window();
					AddBanksDetailsTableDTO addBanksDetailsTableDTO = new AddBanksDetailsTableDTO();
					addBanksDetailsTableDTO.setAccountNumber(banksDetailsTableDTO.getAccountNumber());
					addBanksDetailsTableDTO.setAccountType(banksDetailsTableDTO.getAccountType());
					addBanksDetailsTableDTO.setIfscCode(banksDetailsTableDTO.getIfscCode());
					addBanksDetailsTableDTO.setNameAsPerBankAC(banksDetailsTableDTO.getNamePerBankAccnt());
					addBanksDetailsTableDTO.setNameOfBank(banksDetailsTableDTO.getNameOfBank());
					addBanksDetailsTableDTO.setMicrCode(banksDetailsTableDTO.getMicrCode());
				//	addBanksDetailsTableDTO.setEffectiveFromDate(banksDetailsTableDTO.getEffectiveFromDate());
				//	addBanksDetailsTableDTO.setEffectiveToDate(banksDetailsTableDTO.getEffectiveToDate());
					addBanksDetailsTableDTO.setBranchName(banksDetailsTableDTO.getBranchName());
					SelectValue prefernce = new SelectValue();
					prefernce.setValue(banksDetailsTableDTO.getPreference());
					addBanksDetailsTableDTO.setPreference(prefernce);
					
					//addBanksDetailsTableObj =  addBanksDetailsTableInstance.get();
					addBanksDetailsTableObj.init(addBanksDetailsTableDTO,popup,dto,SHAConstants.UPDATE_BANK,banksDetailsTableDTO.getSerialNumber());
					String popupStr = "";
					if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString)) {
						popupStr = CreateRODDocumentDetailsPresenter.ADD_BANK_IFSC_DETAILS_CROD;
					}
					else if(SHAConstants.BILLING_SCREEN.equalsIgnoreCase(presenterString)) {
						popupStr = BillingHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_BILLING;
					}
					else if(SHAConstants.FINANCIAL_APPROVER.equalsIgnoreCase(presenterString)) {
						popupStr = FinancialHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_FA;
					}
					if(SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString)) {
						popupStr = PACreateRODDocumentDetailsPresenter.ADD_BANK_IFSC_DETAILS_PA_CROD;
					}
					else if(SHAConstants.PA_HEALTH_BILLING_SCREEN.equalsIgnoreCase(presenterString)) {
						popupStr = PAHealthBillingHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_PA_BILLING;
					}
					else if(SHAConstants.PA_HEALTH_FINANCIAL_APPROVER.equalsIgnoreCase(presenterString)) {
						popupStr = PAHealthFinancialHospitalizationPagePresenter.ADD_BANK_IFSC_DETAILS_PA_FA;
					}
					
					else if(SHAConstants.PA_CLAIM_APPROVAL.equalsIgnoreCase(presenterString)) {
						popupStr = PAClaimAprNonHosReviewPagePresenter.ADD_BANK_IFSC_DETAILS_PA_NON_HOSP_CA;
					}
					addBanksDetailsTableObj.initPresenter(popupStr);
				//	addBanksDetailsTableObj.addBean(addBanksDetailsTableDTO);
					addBanksDetailsTableObj.setCaption("Update Bank Details");
					addBanksDetailsTableObj.setExistingData(getValues());
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

					VerticalLayout vlayout = new VerticalLayout(addBanksDetailsTableObj);
					popup.setContent(vlayout);
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
	        		
			 }
			});
			button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	    	button.setWidth("60px");
	    	button.addStyleName(ValoTheme.BUTTON_LINK);
			return button;
		}
		});
		
	}
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			BankDetailsTableDTO entryDTO = (BankDetailsTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				if ("serialNumber".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("40px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("serialNumber", field);
					return field;
				}	
				
				else if  ("preference".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("95px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getPreference());
					field.setReadOnly(true);
					tableRow.put("preference", field);
					return field;
				}
			
				else if  ("accountType".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("105px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getAccountType());
					field.setReadOnly(true);
					tableRow.put("accountType", field);
					return field;
				}
				else if ("ifscCode".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getIfscCode());
					field.setReadOnly(true);
					tableRow.put("ifscCode", field);
					return field;
				}
				else if ("accountNumber".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("120px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getAccountNumber());
					field.setReadOnly(true);
					tableRow.put("accountNumber", field);
					return field;
				}
				else if  ("namePerBankAccnt".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("140px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getNamePerBankAccnt());
					field.setReadOnly(true);
					tableRow.put("namePerBankAccnt", field);
					return field;
				}
				else if  ("nameOfBank".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("130px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getNameOfBank());
					field.setReadOnly(true);
					tableRow.put("nameOfBank", field);
					return field;
				}
				else if ("branchName".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setNullRepresentation("");
					field.setData(entryDTO.getBranchName());
					field.setReadOnly(true);
					tableRow.put("branchName", field);
					return field;
				}
				else if ("micrCode".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("80");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getMicrCode());
					field.setReadOnly(true);
					tableRow.put("micrCode", field);
					return field;
				}
				/*else if ("panNo".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("80");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getMicrCode());
					field.setReadOnly(true);
					tableRow.put("panNo", field);
					return field;
				}
				else if ("virtualPaymentAddr".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("140px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getVirtualPaymentAddr());
					field.setReadOnly(true);
					tableRow.put("virtualPaymentAddr", field);
					return field;
				}*/
				else if ("effectiveFromDate".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getEffectiveFromDate());
					field.setReadOnly(true);
					tableRow.put("effectiveFromDate", field);
					return field;
				}
				else if ("effectiveToDate".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getEffectiveToDate());
					field.setReadOnly(true);
					tableRow.put("effectiveToDate", field);
					return field;
				}
			else {
					Field<?> field = super.createField(container, itemId,
							propertyId, uiContext);
					if (field instanceof TextField)
						field.setWidth("100%");
					return field;
				}
				
				
		}
	}
	
	
	public void setTableList(final List<BankDetailsTableDTO> list) {
		table.removeAllItems();
		for (final BankDetailsTableDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public List<BankDetailsTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<BankDetailsTableDTO> itemIds = this.table.getItemIds() != null ? (List<BankDetailsTableDTO>) this.table.getItemIds() : new ArrayList<BankDetailsTableDTO>();
    	return itemIds;
    }
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<BankDetailsTableDTO> itemIds = (Collection<BankDetailsTableDTO>) table.getItemIds();
		
		int i = 0;
		 for (BankDetailsTableDTO calculationViewTableDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("serialNumber");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setReadOnly(false);
					 itemNoFld.setValue(String.valueOf(i));
					 itemNoFld.setReadOnly(true);
					 //itemNoFld.setEnabled(false);
				 }
			 }
		 }
	}
	
	
	public Set<String> validateCalculation() {/*
		Boolean hasError = false;
		List<BankDetailsTableDTO> itemIds = (List<BankDetailsTableDTO>) table.getItemIds();
		
		if(null != itemIds && !itemIds.isEmpty())
		{
			for (BankDetailsTableDTO bean : itemIds) {
				if(bean.getVerifiedCheck() == false){
 					errorMessages.add("Please Verified Checkbox");
 					return errorMessages;
				}
				
			}
		} */
			return null;
		
		}

	private void onlyNumber(TextField field) {
		CSValidator validator = new CSValidator();
		validator.extend(field);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
	}
	
	public void showClaimsDMSView(String intimationNo)
	{
		
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below code commented due to security reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
		getUI().getPage().open(url, "_blank",1550,800,BorderStyle.NONE);
		
	}
	
	public Set<String> getErrors()
		{
			return this.errorMessages;
		}

	public void setUpAddBankIFSCDetails(ViewSearchCriteriaTableDTO dto) {
		// TODO Auto-generated method stub
		addBanksDetailsTableObj.setUpAddBankIFSCDetails(dto);
	}

	public NomineeDetailsDto getNomineeDto() {
		return nomineeDto;
	}

	public void setNomineeDto(NomineeDetailsDto nomineeDto) {
		this.nomineeDto = nomineeDto;
	}

	public CreateAndSearchLotTableDTO getBankDetailsDTO() {
		return bankDetailsDTO;
	}

	public void setBankDetailsDTO(CreateAndSearchLotTableDTO bankDetailsDTO) {
		this.bankDetailsDTO = bankDetailsDTO;
	}
		
	public String createJWTTokenForClaimStatusPages(Map<String, String> userMap) throws NoSuchAlgorithmException, ParseException{	
		String token = "";	
		try {	
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");	
			keyGenerator.initialize(1024);	
		
			KeyPair kp = keyGenerator.genKeyPair();	
			RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();	
			RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();	
		
		
			JWSSigner signer = new RSASSASigner(privateKey);	
		
			JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();	
			claimsSet.issuer("galaxy");	
			if(userMap.get("intimationNo") != null){
				claimsSet.claim("intimationNo", userMap.get("intimationNo"));	
			}
			if(userMap.get("reimbursementkey") != null){
				claimsSet.claim("reimbursementkey", userMap.get("reimbursementkey"));
			}
			if(userMap.get("acknowledgementNo") !=null){
				claimsSet.claim("acknowledgementNo", userMap.get("acknowledgementNo"));
			}
			claimsSet.expirationTime(new Date(new Date().getTime() + 1000*60*10));	
		
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet.build());	
		
			signedJWT.sign(signer);	
			token = signedJWT.serialize();	
			signedJWT = SignedJWT.parse(token);	
		
			JWSVerifier verifier = new RSASSAVerifier(publicKey);	
				
			return token;	
		} catch (JOSEException ex) {	
				
		}	
		return null;	
		
	 }
}
