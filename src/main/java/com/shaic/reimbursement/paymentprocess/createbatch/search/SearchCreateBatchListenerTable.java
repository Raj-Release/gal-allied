package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.PagerListener;
import com.shaic.arch.table.PagerUI;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.leagalbilling.LegalSettlementAmountTable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.PaidAccountTable;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTable;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.VerificationAccountDeatilsTableDTO;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTable;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class SearchCreateBatchListenerTable extends ViewComponent {

	private Table table;
	private String presenterString;
	private Map<CreateAndSearchLotTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<CreateAndSearchLotTableDTO, HashMap<String, AbstractField<?>>>();
	private PagerUI pageUI;
	private HorizontalLayout fLayout;
	private VerticalLayout vLayout;
	private Searchable searchable;
	private Label totalRocordsTxt;
	Integer intNoOfDays = 0;
	Double intPenalInterestAmnt = 0d;
	private BeanItemContainer<SelectValue> paymentMode;
	private BeanItemContainer<SelectValue> cpuCode;
	private String vType;

	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;

	@Inject
	private UpdateHoldRemarks updateHoldRemarksUI;

	@Inject
	private CitySearchCriteriaViewImpl citySearchCriteriaWindow;

	@Inject
	private MasterService masterService;
	
	@Inject
	private CreateRODService createRodService;
	
	private Window popup1;

	private Window popup;

	@Inject
	private PaymentCpuCodeSearchTable paymentCpucodeListenerTable;

	@EJB
	private SearchCreateBatchService createBatchService;

	@Inject
	private Instance<BankDetailsTable> bankDetailsTableInstance;

	private BankDetailsTable bankDetailsTableObj;

	@Inject
	private Instance<VerificationAccountDeatilsTable> verificationAccountDeatilsTableInstance;

	private VerificationAccountDeatilsTable verificationAccountDeatilsTableObj;
	
	@Inject
	private Instance<PaidAccountTable> paidAccountTableInstance;
	
	private PaidAccountTable paidAccountTableObj;

	private Instance<LegalSettlementAmountTable> legalamountTableinstance;
	
	private LegalSettlementAmountTable legalamountTableObj;

	/***
	 * Bean object fetch from db
	 */
	public BeanItemContainer<CreateAndSearchLotTableDTO> data = new BeanItemContainer<CreateAndSearchLotTableDTO>(
			CreateAndSearchLotTableDTO.class);
	public HashMap<Long, Component> compMap = null;
	public List<CreateAndSearchLotTableDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	private VerticalLayout tableLayout = null;
	private Boolean isSearchBtnClicked = false;
	private String menuString;
	public WeakHashMap<Long, Component> payeeSearchMap = null;

	public void paymentVerification(String menuString) {
		this.menuString = menuString;
	}

	public void init(String presenterString, Boolean showPagerFlag) {
		this.presenterString = presenterString;

		initTable();
		pageUI = new PagerUI();

		addListener();
		table.sort(new Object[] { "numberofdays" }, new boolean[] { false });
		tableLayout = new VerticalLayout();
		fLayout = new HorizontalLayout();
		fLayout.setWidth("100%");
		fLayout.setHeight("20px");
		totalRocordsTxt = new Label();
		totalRocordsTxt.setWidth(null);

		if (showPagerFlag) {
			tableLayout.addComponent(pageUI);
			pageUI.addListener(new PagerListener() {
				@Override
				public void changePage() {

					searchable.doSearch();
				}
			});
		}

		tableLayout.addComponent(fLayout);
		tableLayout.addComponent(table);
		tableLayout.setSpacing(true);
		tableLayout.setMargin(false);

		setCompositionRoot(tableLayout);
	}

	private void addListener() {

	}

	public void initPresenterString(String presenterString) {
		this.presenterString = presenterString;
	}

	public void setReferenceData(Map<String, Object> referenceData) {
		// this.referenceData = referenceData;
	}

	void initTable() {
		// Create a data source and bind it to a table
		table = new Table();
		table.setContainerDataSource(data);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		if (null != presenterString
				&& SHAConstants.QUICK_SEARCH.equalsIgnoreCase(presenterString)) {

			table.setHeight("320px");
		} else {
			table.setHeight("550px");
		}

		generatecolumns();

		if ((SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(presenterString)
				|| (SHAConstants.QUICK_SEARCH)
						.equalsIgnoreCase(presenterString)) {
			table.removeAllItems();
			table.setVisibleColumns(new Object[] { "serialNo", "chkSelect",
					"viewLinkedPolicy", "editpaymentdetails", "viewdetails","legaldetails",
					"intimationNo", "policyNo", "paymentType",
					"payModeChangeReason", "paymentCpucodeTextValue",
					"paymentCpuCodeSearch", "emailID", "payableAt",
					"payableAtSearch", "paymentPartyMode", "payeeNameStr", "payeeNameSearch",
					"beneficiaryAcntNo", "accountVerificationDetails",
					"ifscCode", "Search", "cpuCode", "bankName", "branchName",
					"claimNo", "rodNo", "paymentStatus", "product",
					"typeOfClaim", "lotNo", "approvedAmt", "legalFirstName",
					"reasonForChange", "gmcProposerName", "gmcEmployeeName",
					"panNo", "providerCode", "reconsiderationFlag",
					"faApprovedAmnt", "lastAckDateValue",
					"faApprovedDateValue", "numberofdays", "irdaTAT",
					"noofdaysexceeding", "intrestAmount", "penalTotalAmnt",
					"remarks", "dbSideRemark" });

			table.setColumnHeader("serialNo", "S.No");
			table.setColumnHeader("chkSelect", "");
			table.setColumnHeader("viewLinkedPolicy", "");
			table.setColumnHeader("editpaymentdetails", "");
			table.setColumnHeader("viewdetails", "");
			table.setColumnHeader("Search", "Search</br>IFSC");
			table.setColumnHeader("intimationNo", "Intimation No");
			table.setColumnHeader("claimNo", "Claim No");
			table.setColumnHeader("policyNo", "Policy No");
			table.setColumnHeader("rodNo", "ROD No");
			table.setColumnHeader("paymentStatus", "Payment Status");
			table.setColumnHeader("product", "Product");
			table.setColumnHeader("cpuCode", "Cpu Code");
			table.setColumnHeader("cpuName", "Cpu Name");
			table.setColumnHeader("bankName", "Bank Name");
			table.setColumnHeader("paymentCpucodeTextValue",
					"Payment</br>Cpu Code");
			table.setColumnHeader("paymentCpuCodeSearch",
					"Search</br>Payment</br>Cpu");
			table.setColumnHeader("payModeChangeReason", "Reason for Change");
			table.setColumnHeader("reasonForChange", "Reason for Change");
			table.setColumnHeader("ifscCode", "IFSC Code");
			table.setColumnHeader("beneficiaryAcntNo", "Beneficiary Account No");
			table.setColumnHeader("accountVerificationDetails",
					"Account Verification Details");
			table.setColumnHeader("branchName", "Branch Name");
			table.setColumnHeader("typeOfClaim", "Type Of Claim");
			table.setColumnHeader("lotNo", "Lot No");
			table.setColumnHeader("approvedAmt", "Approved Amnt");
			table.setColumnHeader("paymentPartyMode", "Payment Party Mode");
			table.setColumnHeader("payeeNameStr", "Payee Name");
			table.setColumnHeader("payeeNameSearch",
					"Search</br>Payee</br>Name");
			table.setColumnHeader("gmcProposerName", "Proposer Name");
			table.setColumnHeader("gmcEmployeeName", "Employee Name");
			table.setColumnHeader("payableAt", "Payable City");
			table.setColumnHeader("payableAtSearch",
					"Search</br>Payable</br>City");
			table.setColumnHeader("panNo", "PAN No");
			table.setColumnHeader("providerCode", "Provider Code");
			table.setColumnHeader("emailID", "Email ID");
			table.setColumnHeader("paymentType", "Payment Type");
			table.setColumnHeader("reconsiderationFlag",
					"Reconsideration</br>Flag(Y/N)");
			table.setColumnHeader("faApprovedAmnt", "Approved Amnt");
			table.setColumnHeader("lastAckDateValue",
					"Last<br>Acknowledegement</br>Received Date");
			table.setColumnHeader("faApprovedDateValue",
					"Date Of<br>Financial</br>Approval");
			table.setColumnHeader("numberofdays",
					"No Of Days <br>for<br>processing");
			table.setColumnHeader("irdaTAT", "IRDA TAT");
			table.setColumnHeader("noofdaysexceeding",
					"No Of Days<br>Int Payable</br>");
			table.setColumnHeader("intrestAmount", "Penal Interest<br> Amount");
			table.setColumnHeader("penalTotalAmnt", "Total amount");
			table.setColumnHeader("remarks", "Interest Remarks");
			table.setColumnHeader("dbSideRemark", "Remarks");
			table.setColumnHeader("legalFirstName", "Other Payee Name");
			table.setColumnHeader("payModeChangeReason", "Reason for Change");
			table.setColumnHeader("gmcProposerName", "Proposer Name");
			table.setColumnHeader("gmcEmployeeName", "Employee Name");
			table.setColumnHeader("legaldetails", ""); 	
		} else if ((SHAConstants.SEARCH_BATCH_TYPE)
				.equalsIgnoreCase(presenterString)) {
			table.removeAllItems();
			table.setVisibleColumns(new Object[] { "serialNo", "chkSelect",
					"viewLinkedPolicy", "accountBatchNo",
					"paymentReqDateValue", "userId", "typeOfClaim",
					"claimCount", "showdetails" });

			table.setColumnHeader("serialNo", "S.No");
			table.setColumnHeader("chkSelect", "");
			table.setColumnHeader("viewLinkedPolicy", "");
			table.setColumnHeader("accountBatchNo", "Account Batch No");
			table.setColumnHeader("paymentReqDateValue", "Payment Req Date");
			table.setColumnHeader("userId", "User ID");
			table.setColumnHeader("typeOfClaim", "Claim Type");
			table.setColumnHeader("claimCount", "Claim CTN");
			table.setColumnHeader("showdetails", "Show Details");	

		} else {
			table.removeAllItems();
			table.setVisibleColumns(new Object[] { "chkSelect", "viewdetails",
					"intimationNo", "claimNo", "policyNo", "rodNo",
					"paymentStatus", "product", "cpuCode",
					"paymentCpucodeTextValue", "paymentType",
					"payModeChangeReason", "ifscCode", "beneficiaryAcntNo",
					"branchName", "typeOfClaim", "lotNo", "approvedAmt","paymentPartyMode",
					"payeeNameStr", "legalFirstName", "reasonForChange",
					"gmcProposerName", "gmcEmployeeName", "payableAt", "panNo",
					"providerCode", "emailID", "zonalMailId",
					"reconsiderationFlag", "faApprovedAmnt",
					"lastAckDateValue", "faApprovedDateValue", "numberofdays",
					"noofdaysexceeding", "intrestAmount", "penalTotalAmnt",
					"remarks" });
		}
		if (this.menuString != null
				&& !this.menuString.equalsIgnoreCase(SHAConstants.CREATE_BATCH)
				&& (this.menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || this.menuString
						.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
			table.removeAllItems();
			/*
			 * table.setVisibleColumns(new Object[]
			 * {"serialNo","holdSubmitButtonLayout","submitButton",
			 * "viewLinkedPolicy"
			 * ,"editpaymentdetails","viewdetails","intimationNo","recordCount",
			 * "policyNo","paymentType","payModeChangeReason",
			 * "paymentCpucodeTextValue"
			 * ,"paymentCpuCodeSearch","emailID","payableAt"
			 * ,"payableAtSearch","priorityFlag"
			 * ,"payeeNameStr","payeeNameSearch"
			 * ,"searchPayeeDetail","payeeRelationship","nominee",
			 * "nomineeRelationship"
			 * ,"nomineePayeeDetailSearch","legalHeir","legalHeirRelationship"
			 * ,"accountPreference","accountType","nameAsPerBankAccount",
			 * "micrCode","virtualPaymentProcess","beneficiaryAcntNo",
			 * "accountVerificationDetails","ifscCode",
			 * "Search","cpuCode","bankName"
			 * ,"branchName","claimNo","rodNo","paymentStatus"
			 * ,"product","typeOfClaim","lotNo","approvedAmt",
			 * "legalFirstName","reasonForChange"
			 * ,"gmcProposerName","gmcEmployeeName"
			 * ,"panNo","providerCode","reconsiderationFlag","faApprovedAmnt"
			 * ,"lastAckDateValue"
			 * ,"faApprovedDateValue","numberofdays","irdaTAT"
			 * ,"noofdaysexceeding"
			 * ,"intrestAmount","penalTotalAmnt","remarks","dbSideRemark"});
			 */

			table.setVisibleColumns(new Object[] { "serialNo",
					"holdSubmitButtonLayout", "submitButton",
					"viewLinkedPolicy", "editpaymentdetails", "viewdetails","legaldetails",
					"intimationNo", "policyNo", "rodNo", "paymentStatus",
					"product", "cpuCode", "paymentCpucodeTextValue",
					"paymentCpuCodeSearch", "emailID", "paymentType",
					"payModeChangeReason", "typeOfClaim", "approvedAmt",
					"priorityFlag","paymentPartyMode", "payeeNameStr", "payeeNameSearch",
					"searchPayeeDetail", "payeeRelationship","reasonForChange", "nominee",
					"nomineeRelationship", "legalFirstName",
					"legalHeirRelationship", "nomineePayeeDetailSearch",
					"accountPreference", "accountType", "ifscCode", "Search",
					"beneficiaryAcntNo", "accountVerificationDetails",
					"bankName", "branchName", "nameAsPerBankAccount", "panNo",
					"micrCode", "virtualPaymentProcess", "gmcProposerName",
					"gmcEmployeeName", "payableAt", "payableAtSearch",
					"providerCode", "reconsiderationFlag", "faApprovedAmnt",
					"lastAckDateValue", "faApprovedDateValue", "numberofdays",
					"irdaTAT", "noofdaysexceeding", "intrestAmount",
					"penalTotalAmnt", "remarks", "dbSideRemark" });

			table.setColumnHeader("serialNo", "S.No");
			table.setColumnHeader("holdSubmitButtonLayout", "");
			table.setColumnHeader("submitButton", "");
			table.setColumnHeader("viewLinkedPolicy", "");
			table.setColumnHeader("editpaymentdetails", "");
			table.setColumnHeader("viewdetails", "");
			table.setColumnHeader("intimationNo", "Intimation No");
			table.setColumnHeader("policyNo", "Policy No");
			/* table.setColumnHeader("recordCount", "Record Count"); */
			/* table.setColumnHeader("claimNo", "Claim No"); */
			table.setColumnHeader("rodNo", "ROD No");
			table.setColumnHeader("paymentStatus", "Payment Status");
			table.setColumnHeader("product", "Product");
			table.setColumnHeader("cpuCode", "Cpu Code");
			// table.setColumnHeader("cpuName", "Cpu Name");
			table.setColumnHeader("paymentCpucodeTextValue",
					"Payment</br>Cpu Code");
			table.setColumnHeader("paymentCpuCodeSearch",
					"Search</br>Payment</br>Cpu");
			table.setColumnHeader("emailID", "Email ID");
			table.setColumnHeader("paymentType", "Payment Type");
			table.setColumnHeader("payModeChangeReason", "Reason for Change");
			table.setColumnHeader("typeOfClaim", "Type Of Claim");
			table.setColumnHeader("approvedAmt", "Approved Amnt");
			table.setColumnHeader("priorityFlag", "Priority Flag");
			table.setColumnHeader("paymentPartyMode", "Payment Party Mode");
			table.setColumnHeader("payeeNameStr", "Payee Name");
			table.setColumnHeader("payeeNameSearch",
					"Search</br>Payee</br>Name");
			table.setColumnHeader("searchPayeeDetail",
					"Payee</br>Bank</br>Details");
			table.setColumnHeader("payeeRelationship", "Payee Relationship");
			table.setColumnHeader("nominee", "Nominee");
			table.setColumnHeader("nomineeRelationship", "Nominee Relationship");
			table.setColumnHeader("legalFirstName", "Legal Heir");
			table.setColumnHeader("legalHeirRelationship",
					"Legal</br>Heir</br>Relationship");
			table.setColumnHeader("nomineePayeeDetailSearch",
					"Nominee</br>Bank</br>Details");
			table.setColumnHeader("accountPreference", "Preferences");
			table.setColumnHeader("accountType", "Account Type");
			table.setColumnHeader("ifscCode", "IFSC Code");
			table.setColumnHeader("Search", "Search</br>IFSC");
			table.setColumnHeader("beneficiaryAcntNo", "Beneficiary Account No");
			table.setColumnHeader("accountVerificationDetails",
					"Account Verification Details");
			table.setColumnHeader("bankName", "Bank Name");
			table.setColumnHeader("branchName", "Branch Name");
			table.setColumnHeader("nameAsPerBankAccount",
					"Name</br>As Per</br>Bank");
			table.setColumnHeader("panNo", "PAN No");
			table.setColumnHeader("micrCode", "MICR</br>Code");
			table.setColumnHeader("virtualPaymentProcess",
					"Virtual</br>Payment</br>Address");
			table.setColumnHeader("gmcProposerName", "Proposer Name");
			table.setColumnHeader("gmcEmployeeName", "Employee Name");
			table.setColumnHeader("payableAt", "Payable City");
			table.setColumnHeader("payableAtSearch",
					"Search</br>Payable</br>City");
			table.setColumnHeader("providerCode", "Provider Code");
			table.setColumnHeader("reconsiderationFlag",
					"Reconsideration</br>Flag(Y/N)");
			table.setColumnHeader("faApprovedAmnt", "Approved Amnt");
			table.setColumnHeader("lastAckDateValue",
					"Last<br>Acknowledegement</br>Received Date");
			table.setColumnHeader("faApprovedDateValue",
					"Date Of<br>Financial</br>Approval");
			table.setColumnHeader("numberofdays",
					"No Of Days <br>for<br>processing");
			table.setColumnHeader("irdaTAT", "IRDA TAT");
			table.setColumnHeader("noofdaysexceeding",
					"No Of Days<br>Int Payable</br>");
			table.setColumnHeader("intrestAmount", "Penal Interest<br> Amount");
			table.setColumnHeader("penalTotalAmnt", "Total amount");
			table.setColumnHeader("remarks", "Interest Remarks");
			table.setColumnHeader("dbSideRemark", "Remarks");
			table.setColumnHeader("legaldetails", ""); 

			table.setColumnHeader("reasonForChange", "Reason for Change");
			/* table.setColumnHeader("legalFirstName", "Other Payee Name"); */

		}

		table.setEditable(true);
		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

		table.setFooterVisible(false);
		table.sort(new Object[] { "numberofdays" }, new boolean[] { false });
	}

	public void manageListeners() {

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			CreateAndSearchLotTableDTO initiateDTO = (CreateAndSearchLotTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(initiateDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(initiateDTO,
						new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(initiateDTO);
			}

			if ("intimationNo".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("intimationNo", field);

				return field;
			} else if ("recordCount".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				tableRow.put("recordCount", field);

				return field;
			} else if ("priorityFlag".equals(propertyId)) {
				ComboBox field = new ComboBox();
				field.setWidth("120px");

				List<String> selVal = new ArrayList<String>();
				selVal.add("High");
				selVal.add("Normal");
				SelectValue sVal = null;
				List<SelectValue> result = new ArrayList<SelectValue>();
				for (int i = 0; i < selVal.size(); i++) {
					sVal = new SelectValue();
					sVal.setValue(selVal.get(i));
					result.add(sVal);
				}
				BeanItemContainer<SelectValue> r = new BeanItemContainer<SelectValue>(
						SelectValue.class);
				r.addAll(result);
				field.setContainerDataSource(r);
				/*
				 * if(initiateDTO.getBancsPriorityFlag() != null) { SelectValue
				 * data = new SelectValue();
				 * data.setValue(initiateDTO.getBancsPriorityFlag());
				 * field.setValue(data); }
				 */
				/*
				 * field.setContainerDataSource(paymentMode);
				 * if(initiateDTO.getPaymentTypeValue()!=null){
				 * if(initiateDTO.getPaymentTypeValue
				 * ().equalsIgnoreCase("NEFT")){ SelectValue idByIndex =
				 * paymentMode.getIdByIndex(0); field.setValue(idByIndex);
				 * initiateDTO.setPaymentType(idByIndex);
				 * initiateDTO.setTempPaymentType(idByIndex); }else{ SelectValue
				 * idByIndex = paymentMode.getIdByIndex(1);
				 * field.setValue(idByIndex);
				 * initiateDTO.setPaymentType(idByIndex);
				 * initiateDTO.setTempPaymentType(idByIndex); }}
				 * field.setEnabled(Boolean.TRUE);
				 * 
				 * field.addValueChangeListener(ifscListener());
				 * field.addValueChangeListener(paymentTypeListener(field,
				 * initiateDTO));
				 */
				field.setData(initiateDTO);
				field.setValue(initiateDTO.getPriorityFlag());
				tableRow.put("priorityFlag", field);
				return field;
			} else if ("payeeRelationship".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("payeeRelationship", field);

				return field;
			} else if ("nominee".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("nominee", field);

				return field;
			} else if ("nomineeRelationship".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("nomineeRelationship", field);

				return field;
			} else if ("legalHeir".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("legalHeir", field);

				return field;
			} else if ("legalHeirRelationship".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("legalHeirRelationship", field);

				return field;
			} else if ("accountPreference".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				tableRow.put("accountPreference", field);

				return field;
			} else if ("accountType".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("accountType", field);

				return field;
			} else if ("nameAsPerBankAccount".equals(propertyId)) {

				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("nameAsPerBankAccount", field);

				return field;
			} else if ("micrCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("micrCode", field);
				return field;
			} else if ("virtualPaymentProcess".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				tableRow.put("virtualPaymentProcess", field);
				return field;
			} else if ("claimNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("claimNo", field);

				return field;
			} else if ("policyNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("160px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("policyNo", field);

				return field;
			} else if ("rodNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("190px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("rodNo", field);

				return field;
			} else if ("paymentStatus".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("75px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("paymentStatus", field);
				return field;
			} else if ("product".equals(propertyId)) {
				TextField field = new TextField();
				//
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("product", field);
				return field;
			} else if ("cpuCode".equals(propertyId)) {
				ComboBox field = new ComboBox();
				field.setEnabled(false);
				field.setWidth("120px");
				field.setData(initiateDTO);
				tableRow.put("cpuCode", field);
				return field;
			}

			else if ("paymentCpucodeTextValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("110px");
				field.setData(initiateDTO);
				field.setValue(initiateDTO.getPaymentCpucodeTextValue());
				field.setEnabled(Boolean.FALSE);
				tableRow.put("paymentCpucodeTextValue", field);
				field.addValueChangeListener(paymentCpuListener());
				return field;
			} else if ("reasonForChange".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setWidth("120px");
				field.setHeight("40px");
				field.setEnabled(Boolean.TRUE);
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				changeOfModeReasonListener(field, null);
				field.setMaxLength(4000);
				tableRow.put("reasonForChange", field);
				return field;
			}

			else if ("paymentType".equals(propertyId)) {
				ComboBox field = new ComboBox();
				field.setWidth("120px");
				field.setContainerDataSource(paymentMode);
				if (initiateDTO.getPaymentTypeValue() != null) {
					if (initiateDTO.getPaymentTypeValue().equalsIgnoreCase(
							"NEFT")) {
						SelectValue idByIndex = paymentMode.getIdByIndex(0);
						field.setValue(idByIndex);
						initiateDTO.setPaymentType(idByIndex);
						initiateDTO.setTempPaymentType(idByIndex);
					} else {
						SelectValue idByIndex = paymentMode.getIdByIndex(1);
						field.setValue(idByIndex);
						initiateDTO.setPaymentType(idByIndex);
						initiateDTO.setTempPaymentType(idByIndex);
					}
				}
				field.setEnabled(Boolean.TRUE);
				field.setData(initiateDTO);
				field.addValueChangeListener(ifscListener());
				field.addValueChangeListener(paymentTypeListener(field,
						initiateDTO));
				tableRow.put("paymentType", field);
				return field;
			} else if ("payModeChangeReason".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setWidth("120px");
				field.setHeight("40px");
				field.setNullRepresentation("");
				field.setEnabled(Boolean.TRUE);
				field.setData(initiateDTO);
				field.setMaxLength(4000);
				changeOfPaymentModeReasonListener(field, null);
				tableRow.put("payModeChangeReason", field);
				return field;
			} else if ("ifscCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("ifscCode", field);
				return field;
			} else if ("gmcProposerName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("gmcProposerName", field);
				return field;
			} else if ("gmcEmployeeName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("gmcEmployeeName", field);
				return field;
			} else if ("beneficiaryAcntNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				if (initiateDTO != null && initiateDTO.getPaymentType() != null
						&& initiateDTO.getPaymentType().getId() != null) {
					if (presenterString != null
							&& (initiateDTO.getPaymentType().getId())
									.equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)) {
						field.setEnabled(false);
					}
				} else {
					field.setEnabled(Boolean.TRUE);
				}
				
				if (menuString != null
						&& !menuString.equalsIgnoreCase(SHAConstants.CREATE_BATCH)
						&& (menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString
								.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
				
					if(initiateDTO != null && initiateDTO.getPaymentType() != null && initiateDTO.getPaymentType().getId() != null) {
						if (initiateDTO.getPaymentType() != null && (initiateDTO.getPaymentType().getId()).equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)){
							field.setEnabled(true);
						}else{
							field.setEnabled(false);
						}
					}
				}
				field.setValue(initiateDTO.getBeneficiaryAcntNo());
				field.setData(initiateDTO);
				field.addValueChangeListener(accountNumberListener(initiateDTO,
						field));
				tableRow.put("beneficiaryAcntNo", field);
				return field;
			} else if ("bankName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setEnabled(Boolean.FALSE);
				field.setData(initiateDTO);
				tableRow.put("bankName", field);
				return field;
			} else if ("branchName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("branchName", field);
				return field;
			} else if ("typeOfClaim".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("typeOfClaim", field);
				return field;
			} else if (!(menuString != null && (menuString
					.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString
					.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)))
					&& "lotNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("lotNo", field);
				return field;
			} else if ("approvedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("approvedAmt", field);
				return field;
			} else if ("payeeNameStr".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("140px");
				field.setNullRepresentation("");
				if (initiateDTO.getDocumentReceivedFrom() != null
						&& initiateDTO
								.getDocumentReceivedFrom()
								.equalsIgnoreCase(
										SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)) {
					if (initiateDTO.getHospitalPayableAt() != null) {

						field.setData(initiateDTO);
						initiateDTO.setPayeeNameStr(initiateDTO
								.getHospitalPayableAt());
						field.setValue(initiateDTO.getPayeeNameStr());
					} else if (initiateDTO.getHospitalPayableName() != null) {

						field.setData(initiateDTO);
						initiateDTO.setPayeeNameStr(initiateDTO
								.getHospitalPayableName());
						field.setValue(initiateDTO.getHospitalPayableName());
					} else {
						field.setData(initiateDTO);
						field.setValue(initiateDTO.getPayeeNameStr());
					}
				} else {
					field.setData(initiateDTO);
					field.setValue(initiateDTO.getPayeeNameStr());
				}

				field.setNullRepresentation("");
				field.setEnabled(Boolean.FALSE);
				field.addValueChangeListener(payeeNameListener());
				tableRow.put("payeeNameStr", field);
				return field;
			} else if ("legalFirstName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				field.setEnabled(Boolean.TRUE);
				field.setData(initiateDTO);

				if ((menuString != null && (menuString
						.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString
						.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)))) {
					
					if (initiateDTO.getNominee() != null
							|| (initiateDTO.getNominee() != null && !initiateDTO
							.getNominee().isEmpty())) {
						field.setEnabled(false);
					}
					
					/*if (initiateDTO.getLegalFirstName() != null
							|| (initiateDTO.getLegalFirstName() != null && !initiateDTO
							.getLegalFirstName().isEmpty())) {
						field.setEnabled(false);
					}*/
				}
				field.setValue(initiateDTO.getLegalFirstName());

				field.addValueChangeListener(legalListener());
				tableRow.put("legalFirstName", field);
				return field;
			} else if ("reasonForChange".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setWidth("120px");
				field.setHeight("40px");
				field.setNullRepresentation("");
				field.setMaxLength(4000);
				field.setEnabled(Boolean.TRUE);
				field.setData(initiateDTO);
				changeOfReasonListener(field, null);
				tableRow.put("reasonForChange", field);
				return field;
			} else if ("gmcProposerName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("gmcProposerName", field);
				return field;
			} else if ("gmcEmployeeName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("gmcEmployeeName", field);
				return field;
			} else if ("payableAt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				if (initiateDTO != null && initiateDTO.getPaymentType() != null
						&& initiateDTO.getPaymentType().getId() != null) {
					if (presenterString != null
							&& (initiateDTO.getPaymentType().getId())
									.equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)) {
						field.setEnabled(true);
						field.setEnabled(Boolean.FALSE);
					}
				}
				tableRow.put("payableAt", field);
				field.setEnabled(Boolean.FALSE);
				return field;
			} else if ("panNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(Boolean.TRUE);
				tableRow.put("panNo", field);
				return field;
			} else if ("providerCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("providerCode", field);
				return field;
			} else if ("emailID".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setHeight("40px");
				field.setNullRepresentation("");
				if (presenterString != null
						&& (SHAConstants.CREATE_BATCH_TYPE)
								.equalsIgnoreCase(presenterString)) {
					// field.setEnabled(initiateDTO.getIsInsured());
				} else {
					field.setEnabled(Boolean.FALSE);
				}
				field.setData(initiateDTO);
				if (initiateDTO.getDbEmailId() != null) {
					field.setValue(initiateDTO.getDbEmailId());
					initiateDTO.setEmailID(initiateDTO.getDbEmailId());
					initiateDTO.setZonalMailId(initiateDTO.getDbEmailId());
				}
				tableRow.put("emailID", field);
				return field;
			} else if ("zonalMailId".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setHeight("30px");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("zonalMailId", field);
				return field;
			} else if ("reconsiderationFlag".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("reconsiderationFlag", field);
				return field;
			} else if ("faApprovedAmnt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("faApprovedAmnt", field);
				return field;
			} else if ("lastAckDateValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("lastAckDateValue", field);
				return field;
			} else if ("faApprovedDateValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("faApprovedDateValue", field);
				return field;
			} else if ("numberofdays".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("numberofdays", field);
				return field;
			} else if ("intrestAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				String legalFlag = initiateDTO.getClaimDto() != null ? null != initiateDTO
						.getClaimDto().getLegalFlag() ? initiateDTO
						.getClaimDto().getLegalFlag() : null : null;

				if (initiateDTO.getReconsiderationFlag() != null
						&& initiateDTO.getReconsiderationFlag()
								.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
					if ((initiateDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID)
							|| (initiateDTO.getDocReceivedFrom()
									.equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL))
							|| (null != legalFlag && (legalFlag
									.equalsIgnoreCase(SHAConstants.YES_FLAG) || legalFlag
									.equalsIgnoreCase(SHAConstants.N_FLAG)))) {
						field.setEnabled(false);
					} else {
						field.setEnabled(true);
					}
				} else {
					field.setEnabled(false);
				}
				field.setEnabled(false);
				field.addBlurListener(getInterestAmnt(field));
				field.addBlurListener(totalAmountListener(field));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("intrestAmount", field);
				return field;
			} else if ("irdaTAT".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				BPMClientContext obj = new BPMClientContext();

				if (null != initiateDTO.getIsFvrOrInvesInitiated()
						&& initiateDTO.getIsFvrOrInvesInitiated()) {

					field.setValue(obj.getIRDAForFvrAndInvs());
				} else {
					field.setValue(BPMClientContext.getIRDATATDays());
				}

				tableRow.put("irdaTAT", field);
				return field;
			}

			else if ("noofdaysexceeding".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);

				if (null != initiateDTO.getNumberofdays()) {
					if (initiateDTO.getNoofdaysexceeding() >= -3) {
						// IMSSUPPOR-28441 - Penal interest calculation to be
						// editable for correction cases - As per Sathish Sir -
						// 10-May-2019
						if (/*
							 * ReferenceTable.CORRECTION_PAYMENT_STATUS_ID.equals
							 * (initiateDTO.getPaymentStatusKey()) ||
							 */
						(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)
								.equals(initiateDTO.getDocReceivedFrom())) {
							field.setEnabled(false);
						} else {
							field.setEnabled(true);
						}
					} else {
						field.setEnabled(false);

					}
				}
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				field.addBlurListener(noOfDaysExceedingListener(field));
				tableRow.put("noofdaysexceeding", field);
				return field;
			}

			else if ("penalTotalAmnt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("penalTotalAmnt", field);
				return field;
			} else if ("remarks".equals(propertyId)) {

				TextField field = new TextField();
				String legalFlag = null != initiateDTO.getClaimDto()
						.getLegalFlag() ? initiateDTO.getClaimDto()
						.getLegalFlag() : null;

				if (null != legalFlag
						&& (legalFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) || legalFlag
								.equalsIgnoreCase(SHAConstants.N_FLAG))) {
					field.setEnabled(false);
				} else {
					field.setEnabled(true);
				}
				field.setNullRepresentation("");
				field.setEnabled(true);
				if (presenterString != null
						&& (SHAConstants.CREATE_BATCH_TYPE)
								.equalsIgnoreCase(presenterString)) {
					field.setEnabled(initiateDTO.getIsInsured());
				} else {
					field.setEnabled(Boolean.FALSE);
				}
				field.setData(initiateDTO);
				field.setDescription(field.getValue());
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("remarks", field);
				initiateDTO.setIsPaymentCpuCodeListenerEnable(true);
				return field;
			} else if ("claimCount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("claimCount", field);
				return field;
			} else if ("serialNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("serialNo", field);
				return field;
			}else if ("paymentPartyMode".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("paymentPartyMode", field);
				return field;
			}  else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if ("dbSideRemark".equals(propertyId)) {
					CreateAndSearchLotTableDTO dto = (CreateAndSearchLotTableDTO) itemId;
					initiateDTO.setIsPaymentCpuCodeListenerEnable(true);
					if (field instanceof TextField) {
						TextField txtField = ((TextField) field);
						txtField.setValue(dto.getDbSideRemark());
						txtField.setDescription(dto.getDbSideRemark());
					}
				}

				if (field instanceof TextField) {
					field.setWidth("100%");
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setReadOnly(true);

					((TextField) field).setNullRepresentation("");

				}
				return field;
			}
		}
	}

	private void generatecolumns() {

		compMap = new HashMap<Long, Component>();
		payeeSearchMap = new WeakHashMap<Long, Component>();
		if (!(this.menuString != null
				&& !this.menuString.equalsIgnoreCase(SHAConstants.CREATE_BATCH) && (this.menuString
				.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || this.menuString
				.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)))) {
			table.removeGeneratedColumn("chkSelect");
			table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {

					CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) itemId;
					CheckBox chkBox = new CheckBox("");

					if (null != tableDataList && !tableDataList.isEmpty()) {
						for (CreateAndSearchLotTableDTO dto : tableDataList) {
							if (dto.getClaimPaymentKey().equals(
									tableDTO.getClaimPaymentKey())) {
								if (null != dto.getChkSelect()) {
									chkBox.setValue(dto.getChkSelect());
								} else if (("true").equalsIgnoreCase(dto
										.getCheckBoxStatus())) {
									chkBox.setValue(true);
								} else if (("false").equalsIgnoreCase(dto
										.getCheckBoxStatus())) {
									chkBox.setValue(false);
								}
							}

						}
					}

					chkBox.setData(tableDTO);
					addListener(chkBox);
					compMap.put(tableDTO.getClaimPaymentKey(), chkBox);
					return chkBox;
				}
			});
		} else {
			final SearchCreateBatchListenerTable searchCreateBatchViewImpl = this;
			table.removeGeneratedColumn("holdSubmitButtonLayout");
			table.addGeneratedColumn("holdSubmitButtonLayout",
					new Table.ColumnGenerator() {
						/**
				 * 
				 */
						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(final Table source,
								final Object itemId, Object columnId) {

							CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) itemId;

							Button holdButton = new Button("Hold");
							holdButton.setWidth("65px");
							holdButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

							if (null != tableDataList
									&& !tableDataList.isEmpty()) {
								for (CreateAndSearchLotTableDTO dto : tableDataList) {
									if (dto.getClaimPaymentKey().equals(
											tableDTO.getClaimPaymentKey())) {
										holdButton
												.addClickListener(new ClickListener() {

													@Override
													public void buttonClick(
															ClickEvent event) {
														Button value = event
																.getButton();
														final Window popup = (Window) value
																.getData();

														ConfirmDialog dialog = ConfirmDialog
																.show(getUI(),
																		"Confirmation",
																		"Are you sure, you want to hold the selected record(s)?",
																		"No",
																		"Yes",
																		new ConfirmDialog.Listener() {

																			public void onClose(
																					ConfirmDialog dialog) {
																				if (!dialog
																						.isConfirmed()) {
																					popup1 = new com.vaadin.ui.Window();
																					popup1.setCaption("Update Hold Remarks");
																					popup1.setWidth("50%");
																					popup1.setHeight("40%");
																					String presenter = "Hold";
																					updateHoldRemarksUI
																							.init(searchCreateBatchViewImpl,
																									popup1,
																									dto,
																									presenter);

																					popup1.setContent(updateHoldRemarksUI);
																					popup1.setClosable(true);
																					popup1.center();
																					popup1.setResizable(true);
																					popup1.close();
																					popup1.addCloseListener(new Window.CloseListener() {
																						/**
																			 * 
																			 */
																						private static final long serialVersionUID = 1L;

																						@Override
																						public void windowClose(
																								CloseEvent e) {
																							popup1.close();
																							System.out
																									.println("Close listener called");
																						}
																					});

																					popup1.setModal(true);
																					UI.getCurrent()
																							.addWindow(
																									popup1);
																				} else {
																					// User
																					// did
																					// not
																					// confirm
																					dialog.setClosable(false);
																					dialog.setStyleName(Reindeer.WINDOW_BLACK);
																				}

																			}
																		});

														dialog.setClosable(false);

													}
												});
									}

								}
							}

							// holdButton.setData(tableDTO);
							// addListener(holdButton);
							/*compMap.put(tableDTO.getClaimPaymentKey(),
									holdButton);*/
							return holdButton;
						}
					});
			table.removeGeneratedColumn("submitButton");
			table.addGeneratedColumn("submitButton",
					new Table.ColumnGenerator() {
						/**
				 * 
				 */
						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(final Table source,
								final Object itemId, Object columnId) {
							CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) itemId;

							Button holdButton = new Button("Submit");
							holdButton.setWidth("65px");
							holdButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

							if (null != tableDataList
									&& !tableDataList.isEmpty()) {
								for (CreateAndSearchLotTableDTO dto : tableDataList) {
									if (dto.getClaimPaymentKey().equals(
											tableDTO.getClaimPaymentKey())) {
										holdButton
												.addClickListener(new ClickListener() {

													@Override
													public void buttonClick(
															ClickEvent event) {
														Button value = event
																.getButton();
														final Window popup = (Window) value
																.getData();
														String presenter = "submit";

														if (validatePageForBancs(tableDTO)) {

															fireViewEvent(
																	SearchCreateBatchPresenter.CREATE_BATCH_PAYMENT_LVL1_SUBMIT,
																	tableDTO,
																	popup,
																	presenter,
																	menuString,
																	data.getItemIds().size());
														}

													}
												});
									}

								}
							}

							/*compMap.put(tableDTO.getClaimPaymentKey(),
									holdButton);*/
							return holdButton;
						}
					});

		}

		table.removeGeneratedColumn("payableAtSearch");
		table.addGeneratedColumn("payableAtSearch",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 5936665477260011479L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button searchButton = new Button();
						searchButton.setStyleName(ValoTheme.BUTTON_LINK);
						searchButton.setIcon(new ThemeResource(
								"images/search.png"));
						searchButton.setWidth("-6px");
						searchButton.setHeight("-8px");
						final CreateAndSearchLotTableDTO initiateDTO = (CreateAndSearchLotTableDTO) itemId;
						searchButton.setData(itemId);
						initiateDTO.setPayableAtButton(searchButton);
						if (initiateDTO != null
								&& initiateDTO.getPaymentType() != null
								&& initiateDTO.getPaymentType().getId() != null) {
							if (initiateDTO.getPaymentType() != null
									&& (initiateDTO.getPaymentType().getId())
											.equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)) {
								searchButton.setEnabled(true);
							} else {
								searchButton.setEnabled(false);
							}
						}
						searchButton
								.addClickListener(new Button.ClickListener() {
									private static final long serialVersionUID = 6100598273628582002L;

									public void buttonClick(ClickEvent event) {

										Window popup = new com.vaadin.ui.Window();
										citySearchCriteriaWindow
												.setPresenterString(SHAConstants.CREATE_BATCH);
										citySearchCriteriaWindow.initView(
												popup, initiateDTO);
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
												System.out
														.println("Close listener called");
											}
										});

										popup.setModal(true);

										UI.getCurrent().addWindow(popup);
										searchButton.setEnabled(true);

									}
								});
						return searchButton;
					}
				});

		table.removeGeneratedColumn("viewdetails");
		table.addGeneratedColumn("viewdetails", new Table.ColumnGenerator() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {

				final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) itemId;

				Button button = new Button("View Details");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {

						showClaimsDMSView(tableDTO.getIntimationNo());
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.setWidth("90px");
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});

		table.removeGeneratedColumn("viewLinkedPolicy");
		table.addGeneratedColumn("viewLinkedPolicy",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) itemId;

						Button button = new Button("View Linked Policy");
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						button.setWidth("150px");
						button.addStyleName(ValoTheme.BUTTON_LINK);
						button.setEnabled(false);
						if (tableDTO.getPaymentPolicyType() != null
								&& (tableDTO
										.getPaymentPolicyType()
										.equalsIgnoreCase(
												SHAConstants.GMC_POLICY_TYPE_PARENT) || tableDTO
										.getPaymentPolicyType()
										.equalsIgnoreCase(
												SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))) {
							button.setEnabled(true);
						}
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {

								showLinkedPolicyDetails(tableDTO);
							}
						});
						return button;
					}
				});

		/*
		 * if(!(this.menuString != null &&
		 * !this.menuString.equalsIgnoreCase(SHAConstants.CREATE_BATCH) &&
		 * (this.menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) ||
		 * this.menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)))) {
		 */

		table.removeGeneratedColumn("editpaymentdetails");
		table.addGeneratedColumn("editpaymentdetails",
				new Table.ColumnGenerator() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) itemId;
						Button button = new Button(
								"Payment details from previous claim");
						tableDTO.setPreviousAccntDtlsBtn(button);
						if ((null != tableDTO.getPaymentTypeValue() && tableDTO
								.getPaymentTypeValue().equalsIgnoreCase("NEFT"))
								&& (!(null != tableDTO.getDocReceivedFrom() && (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)
										.equals(tableDTO.getDocReceivedFrom())))) {
							button.setEnabled(true);
						} else {
							button.setEnabled(false);
						}
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								showEditPaymentDetailsView((CreateAndSearchLotTableDTO) itemId);
							}
						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						// button.setWidth("150px");
						button.addStyleName(Reindeer.BUTTON_LINK);
						return button;
					}
				});

		// }

		table.removeGeneratedColumn("showdetails");

		table.addGeneratedColumn("showdetails", new Table.ColumnGenerator() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {

				Button button = new Button("Show Details");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {

						fireViewEvent(
								SearchCreateBatchPresenter.CREATE_BATCH_SHOW_DETAILS,
								(CreateAndSearchLotTableDTO) itemId);

					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.setWidth("150px");
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});

		table.removeGeneratedColumn("Search");
		table.addGeneratedColumn("Search", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				// final Button deleteButton = new Button("Delete");
				final Button searchButton = new Button();
				if (presenterString != null
						&& (SHAConstants.CREATE_LOT)
								.equalsIgnoreCase(presenterString)) {
					// searchButton.setEnabled(initiateDTO.getIsInsured());
				} else {
					searchButton.setEnabled(Boolean.FALSE);
				}
				// deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				searchButton.setStyleName(ValoTheme.BUTTON_LINK);
				searchButton.setIcon(new ThemeResource("images/search.png"));
				// searchButton.setWidth("-1px");
				// searchButton.setHeight("-10px");
				CreateAndSearchLotTableDTO initiateDTO = (CreateAndSearchLotTableDTO) itemId;
				if (null != initiateDTO.getPaymentType()) {
					if (initiateDTO.getIsInsured()
							&& initiateDTO.getPaymentType().getValue()
									.equals("NEFT")) {
						searchButton.setEnabled(true);
					} else if (initiateDTO.getIsInsured()
							&& initiateDTO.getPaymentType().getValue()
									.equals("CHEQUE / DD")) {
						searchButton.setEnabled(false);
					} else {
						searchButton.setEnabled(false);
					}
					if (menuString != null
							&& (menuString
									.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString
									.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))
							&& initiateDTO.getIsInsured() ) {
						
						if (null != initiateDTO.getLegalFirstName()) {
								searchButton.setEnabled(true);
						}					
					}
					searchButton.setData(itemId);
					searchButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;
	
						public void buttonClick(ClickEvent event) {
							Object currentItemId = event.getButton().getData();
							CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) currentItemId;
							showIfscPopup(updatePaymentDetailTableDTO);
						}
					});
					initiateDTO.setSearchButton(searchButton);
				}	
				return searchButton;
			}
		});

		table.removeGeneratedColumn("accountVerificationDetails");
		table.addGeneratedColumn("accountVerificationDetails",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						CreateAndSearchLotTableDTO initiateDTO = (CreateAndSearchLotTableDTO) itemId;

						Button verifyAcntDtlButton = new Button(
								"Account Verification Details");
						verifyAcntDtlButton
								.setStyleName(ValoTheme.BUTTON_DANGER);
						verifyAcntDtlButton.setWidth("180px");
						if (initiateDTO.getPaymentType().getValue()
								.equals("NEFT")
								&& (vType != null && !vType
										.equalsIgnoreCase(ReferenceTable.VERIFICATION_NOT_REQUIRED))) {
							verifyAcntDtlButton.setEnabled(true);
						} else {
							verifyAcntDtlButton
									.setStyleName(ValoTheme.BUTTON_FRIENDLY);
							initiateDTO.setVerificationClicked(true);
							initiateDTO.setVerifiedStatusFlag("Y");
							verifyAcntDtlButton.setEnabled(false);
						}

						if (initiateDTO.getVerifiedStatusFlag() != null
								&& initiateDTO.getVerifiedStatusFlag()
										.equalsIgnoreCase("Y")) {
							verifyAcntDtlButton
									.setStyleName(ValoTheme.BUTTON_FRIENDLY);
							initiateDTO.setVerificationClicked(true);
							verifyAcntDtlButton.setEnabled(false);
						}
						
						initiateDTO.setVerifyAccntDtlsBtn(verifyAcntDtlButton);
						verifyAcntDtlButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(ClickEvent event) {
										if (initiateDTO.getPaymentType()
												.getValue().equals("NEFT") ) {
												if ((initiateDTO
														.getBeneficiaryAcntNo() == null
														|| (initiateDTO
														.getBeneficiaryAcntNo() != null && initiateDTO
														.getBeneficiaryAcntNo().trim().isEmpty()
														 ))) {
													getAlertMessage("Please Enter Account Number");
												}
												else {
													fireViewEvent(
															SearchCreateBatchPresenter.BATCH_VERIFICATION_ACCOUNT_DETAILS,
															initiateDTO);
													if (initiateDTO
															.getVerificationAccountDeatilsTableDTO() != null
															&& !initiateDTO
																	.getVerificationAccountDeatilsTableDTO()
																	.isEmpty()) {
														final Window popup = new com.vaadin.ui.Window();
														popup.setCaption("Account Verification Details");
														//List<VerificationAccountDeatilsTableDTO> paidAccountDeatilsList = initiateDTO.getPaidAccountDeatilsTableDTO();
														Button paidAccDtlsBtn = new Button("Settled Transactions (Same Account No)");
														paidAccDtlsBtn.setHeight("-1px");
														//if(paidAccountDeatilsList != null){
															paidAccDtlsBtn.setStyleName(ValoTheme.BUTTON_LINK);
															//paidAccDtlsBtn.setData(paidAccountDeatilsList);
															paidAccountTableObj = paidAccountTableInstance.get();
															paidAccountTableObj.init("", false, false);
															paidAccDtlsBtn.addClickListener(new Button.ClickListener() {
																@Override
																public void buttonClick(ClickEvent event) {
																	
																	fireViewEvent(
																			SearchCreateBatchPresenter.BATCH_VERIFICATION_ACCOUNT_DETAILS_SETTLED,
																			initiateDTO);
																	
																	Window paidTransPopup = new com.vaadin.ui.Window();
																	paidTransPopup.setCaption("Settled Transactions (Same Account No)");
																	List<VerificationAccountDeatilsTableDTO> paidAccountDeatilsList = initiateDTO.getPaidAccountDeatilsTableDTO();
																	paidAccountTableObj.setTableList(paidAccountDeatilsList);
																	
																	Button closeBtn = new Button(
																			"Close");
																	closeBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
																	closeBtn.addClickListener(new Button.ClickListener() {
					
																		@Override
																		public void buttonClick(
																				ClickEvent event) {
																			paidTransPopup.close();
																		}
																	});
																	
																	VerticalLayout vLayout = new VerticalLayout(paidAccountTableObj,closeBtn);
																	vLayout.setComponentAlignment(closeBtn, Alignment.BOTTOM_CENTER);
																	vLayout.setSpacing(true);
																	
																	paidTransPopup.setContent(vLayout);
																	paidTransPopup.setWidth("75%");
																	paidTransPopup.setHeight("70%");
																	paidTransPopup.setClosable(true);
																	paidTransPopup.center();
																	paidTransPopup.setResizable(true);
																	paidTransPopup.addCloseListener(new Window.CloseListener() {
																		
																		private static final long serialVersionUID = 1L;

																		@Override
																		public void windowClose(CloseEvent e) {
																			System.out.println("Close listener called");
																		}
																	});
																	paidTransPopup.setModal(true);
																	UI.getCurrent().addWindow(paidTransPopup);
																	
																}
															});
														//}
														List<VerificationAccountDeatilsTableDTO> verificationAccountDeatilsList = initiateDTO
																.getVerificationAccountDeatilsTableDTO();
														verificationAccountDeatilsTableObj = verificationAccountDeatilsTableInstance
																.get();
														verificationAccountDeatilsTableObj
																.init(initiateDTO);
//														verificationAccountDeatilsTableObj
//																.setCaption("Account Verification Details");
														if (verificationAccountDeatilsList != null) {
															verificationAccountDeatilsTableObj
																	.setTableList(verificationAccountDeatilsList);
														}
		
														popup.setWidth("95%");
														popup.setHeight("600px");
														popup.setClosable(true);
														popup.center();
														popup.setResizable(true);
														popup.addCloseListener(new Window.CloseListener() {
															/**
															 * 
															 */
															private static final long serialVersionUID = 1L;
		
															@Override
															public void windowClose(
																	CloseEvent e) {
																System.out
																		.println("Close listener called");
															}
														});
														Button okBtn = new Button(
																"Close");
														okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
														okBtn.addClickListener(new Button.ClickListener() {
		
															@Override
															public void buttonClick(
																	ClickEvent event) {
																List<VerificationAccountDeatilsTableDTO> verificationAccountDeatilsTableDTO = verificationAccountDeatilsTableObj
																		.getValues();
																verificationAccountDeatilsTableDTO = new ArrayList<VerificationAccountDeatilsTableDTO>();
																initiateDTO
																		.setVerificationAccountDeatilsTableDTO(verificationAccountDeatilsTableDTO);
																popup.close();
															}
														});
														Button saveBtn = new Button(
																"Save");
														saveBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
														saveBtn.addClickListener(new Button.ClickListener() {
		
															@Override
															public void buttonClick(
																	ClickEvent event) {
																//
																if (!validatePagepayment(Boolean.TRUE)) {
																	List<VerificationAccountDeatilsTableDTO> verificationAccountDeatilsTableDTO = verificationAccountDeatilsTableObj
																			.getValues();
																	fireViewEvent(
																			SearchCreateBatchPresenter.BATCH_VERIFICATION_ACCOUNT_DETAILS_SAVE,
																			initiateDTO);
																	// changeVerifiedButtonValue(initiateDTO.getVerificationClicked());
																	verifyAcntDtlButton
																			.setStyleName(ValoTheme.BUTTON_FRIENDLY);
																	verifyAcntDtlButton
																			.setEnabled(false);
																	initiateDTO
																			.setVerifiedStatusFlag("Y");
																	initiateDTO
																			.setVerificationClicked(true);
																	popup.close();
																}
															}
		
														});
														VerticalLayout vlayout = new VerticalLayout();
														
														vlayout.addComponents(paidAccDtlsBtn,
																verificationAccountDeatilsTableObj);
														vlayout.setSpacing(true);
														HorizontalLayout hLayout = new HorizontalLayout(
																saveBtn, okBtn);
														hLayout.setSpacing(true);
														vlayout.addComponent(hLayout);
														vlayout.setComponentAlignment(
																hLayout,
																Alignment.BOTTOM_CENTER);
//														vlayout.setHeight("100%");
														popup.setContent(vlayout);
														popup.setModal(true);
														UI.getCurrent()
																.addWindow(popup);
													} else {
														showInformation("Matched Account Not Found");
														initiateDTO
																.setVerificationClicked(true);
														verifyAcntDtlButton
																.setStyleName(ValoTheme.BUTTON_FRIENDLY);
														verifyAcntDtlButton
																.setEnabled(false);
														initiateDTO
																.setVerifiedStatusFlag("Y");
														initiateDTO
																.setVerificationClicked(true);
														// changeVerifiedButtonValue(initiateDTO.getVerificationClicked());
													}
												}
										}
									}
								});

						return verifyAcntDtlButton;
					}
				});

		table.removeGeneratedColumn("remarks");
		table.addGeneratedColumn("remarks", new Table.ColumnGenerator() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {

				final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) itemId;

				com.vaadin.v7.ui.TextField txtRemarks = new com.vaadin.v7.ui.TextField();

				txtRemarks.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(Property.ValueChangeEvent event) {

						String value = (String) event.getProperty().getValue();

						if (null != value && !(value.equals(""))) {

							tableDTO.setRemarks(value);
						} else {
							tableDTO.setRemarks("");
						}

					}
				});

				// txtRemarks.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				txtRemarks.setWidth("150px");
				txtRemarks.addStyleName(ValoTheme.BUTTON_LINK);
				if (null != tableDTO.getRemarks())
					txtRemarks.setValue(tableDTO.getRemarks());
				return txtRemarks;
			}
		});

		table.removeGeneratedColumn("paymentCpuCodeSearch");
		table.addGeneratedColumn("paymentCpuCodeSearch",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 5936665477260011479L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button searchButton = new Button();
						searchButton.setStyleName(ValoTheme.BUTTON_LINK);
						searchButton.setIcon(new ThemeResource(
								"images/search.png"));
						searchButton.setWidth("-6px");
						searchButton.setHeight("-8px");
						CreateAndSearchLotTableDTO initiateDTO = (CreateAndSearchLotTableDTO) itemId;
						searchButton.setData(itemId);
						searchButton
								.addClickListener(new Button.ClickListener() {
									private static final long serialVersionUID = 6100598273628582002L;

									public void buttonClick(ClickEvent event) {
										Object currentItemId = event
												.getButton().getData();
										CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) currentItemId;
										showPaymentCpuCodePopup(updatePaymentDetailTableDTO);
									}
								});
						return searchButton;
					}
				});

		table.removeGeneratedColumn("payeeNameSearch");
		table.addGeneratedColumn("payeeNameSearch",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 5936665477260011479L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button searchButton = new Button();
						searchButton.setStyleName(ValoTheme.BUTTON_LINK);
						searchButton.setIcon(new ThemeResource(
								"images/search.png"));
						searchButton.setWidth("-1px");
						searchButton.setHeight("-10px");
						CreateAndSearchLotTableDTO initiateDTO = (CreateAndSearchLotTableDTO) itemId;

						if (initiateDTO.getNominee() != null
								|| (initiateDTO.getNominee() != null && !initiateDTO
										.getNominee().isEmpty())) {
							searchButton.setEnabled(false);
						}

						if (initiateDTO.getLegalFirstName() != null
								|| (initiateDTO.getLegalFirstName() != null && !initiateDTO
										.getLegalFirstName().isEmpty())) {
							searchButton.setEnabled(false);
						}
						
						if(menuString != null
								&& (menuString
										.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString
										.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
							if (initiateDTO.getIsInsured() != null
									&& !initiateDTO.getIsInsured()) {
								searchButton.setEnabled(false);
							}
						}	
						

						searchButton.setData(itemId);
						searchButton
								.addClickListener(new Button.ClickListener() {
									private static final long serialVersionUID = 6100598273628582002L;

									public void buttonClick(ClickEvent event) {
										Object currentItemId = event
												.getButton().getData();
										/*
										 * if(menuString != null &&
										 * !menuString.equalsIgnoreCase
										 * (SHAConstants.CREATE_BATCH) &&
										 * (menuString
										 * .equalsIgnoreCase(SHAConstants
										 * .PAYMENT_LVL1) ||
										 * menuString.equalsIgnoreCase
										 * (SHAConstants.PAYMENT_LVL2))) {
										 */
										CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) currentItemId;
										showPayeeNamePopup(updatePaymentDetailTableDTO);
										// }										
									}
								});
						payeeSearchMap.put(initiateDTO.getClaimPaymentKey(), searchButton);
						return searchButton;
					}
				});
		table.removeGeneratedColumn("searchPayeeDetail");
		table.addGeneratedColumn("searchPayeeDetail",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 5936665477260011479L;
					final Window popup = new com.vaadin.ui.Window();

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button searchButton = new Button();
						searchButton.setStyleName(ValoTheme.BUTTON_LINK);
						searchButton.setIcon(new ThemeResource(
								"images/search.png"));
						searchButton.setWidth("-1px");
						searchButton.setHeight("-10px");
						CreateAndSearchLotTableDTO initiateDTO = (CreateAndSearchLotTableDTO) itemId;

						if (menuString != null
								&& (menuString
										.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString
										.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
							if (initiateDTO.getIsInsured()) {
						
								if(initiateDTO.getNominee() != null
										|| (initiateDTO.getNominee() != null && !initiateDTO
												.getNominee().isEmpty())) {
									searchButton.setEnabled(false);
								}
		
								if(initiateDTO.getLegalFirstName() != null
										|| (initiateDTO.getLegalFirstName() != null && !initiateDTO
												.getLegalFirstName().isEmpty())) {
									searchButton.setEnabled(false);
								}
								if(initiateDTO.getPayeeNameStr() != null)
									searchButton.setEnabled(true);
							}
							else{
								searchButton.setEnabled(false);
							}
						}						
						
						searchButton.setData(itemId);
						searchButton
								.addClickListener(new Button.ClickListener() {
									private static final long serialVersionUID = 6100598273628582002L;

									public void buttonClick(ClickEvent event) {
										Object currentItemId = event
												.getButton().getData();
										CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) currentItemId;
										bankDetailsTableObj = bankDetailsTableInstance
												.get();
										// showPayeeNamePopup(updatePaymentDetailTableDTO);
										// if(updatePaymentDetailTableDTO.getReceiptOfDocumentsDTO().getSourceRiskID()
										// != null) {
										bankDetailsTableObj
												.init(updatePaymentDetailTableDTO
														.getReceiptOfDocumentsDTO());
										bankDetailsTableObj
												.setCaption("Bank Details");
										bankDetailsTableObj
												.initPresenter(SHAConstants.PAYMENT_VERIFICATION_LVL2);
										bankDetailsTableObj
												.setBankDetailsDTO(updatePaymentDetailTableDTO);
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
												System.out
														.println("Close listener called");
											}
										});
										Button okBtn = new Button("Cancel");
										okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
										okBtn.addClickListener(new Button.ClickListener() {

											@Override
											public void buttonClick(
													ClickEvent event) {
												List<BankDetailsTableDTO> bankDetailsTableDTO = bankDetailsTableObj
														.getValues();
												bankDetailsTableDTO = new ArrayList<BankDetailsTableDTO>();
												// bean.setVerificationAccountDeatilsTableDTO(bankDetailsTableDTO);
												popup.close();
											}
										});

										VerticalLayout vlayout = new VerticalLayout(
												bankDetailsTableObj);
										HorizontalLayout hLayout = new HorizontalLayout(
												okBtn);
										hLayout.setSpacing(false);
										vlayout.setMargin(false);
										vlayout.addComponent(hLayout);
										vlayout.setComponentAlignment(hLayout,
												Alignment.BOTTOM_CENTER);
										popup.setContent(vlayout);
										popup.setModal(true);
										UI.getCurrent().addWindow(popup);
									}
									// }
								});
						return searchButton;
					}
				});

		table.removeGeneratedColumn("nomineePayeeDetailSearch");
		table.addGeneratedColumn("nomineePayeeDetailSearch",
				new Table.ColumnGenerator() {
					private static final long serialVersionUID = 5936665477260011479L;
					final Window popup = new com.vaadin.ui.Window();

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button searchButton = new Button();

						searchButton.setEnabled(false); // Disabled as per
														// Satish sir's
														// suggestion

						searchButton.setStyleName(ValoTheme.BUTTON_LINK);
						searchButton.setIcon(new ThemeResource(
								"images/search.png"));
						searchButton.setWidth("-1px");
						searchButton.setHeight("-10px");
						CreateAndSearchLotTableDTO initiateDTO = (CreateAndSearchLotTableDTO) itemId;

						if (initiateDTO.getNominee() != null
								|| (initiateDTO.getNominee() != null && !initiateDTO
										.getNominee().isEmpty())) {
							searchButton.setEnabled(false);
						}

						if (initiateDTO.getLegalFirstName() != null
								|| (initiateDTO.getLegalFirstName() != null && !initiateDTO
										.getLegalFirstName().isEmpty())) {
							searchButton.setEnabled(false);
						}

						searchButton.setData(itemId);
						searchButton
								.addClickListener(new Button.ClickListener() {
									private static final long serialVersionUID = 6100598273628582002L;

									public void buttonClick(ClickEvent event) {
										Object currentItemId = event
												.getButton().getData();
										CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) currentItemId;
										bankDetailsTableObj = bankDetailsTableInstance
												.get();
										// showPayeeNamePopup(updatePaymentDetailTableDTO);
										// if(updatePaymentDetailTableDTO.getReceiptOfDocumentsDTO().getSourceRiskID()
										// != null) {
										bankDetailsTableObj
												.init(updatePaymentDetailTableDTO
														.getReceiptOfDocumentsDTO());
										bankDetailsTableObj
												.setCaption("Bank Details");
										bankDetailsTableObj
												.initPresenter(SHAConstants.PAYMENT_VERIFICATION_LVL2);
										bankDetailsTableObj
												.setBankDetailsDTO(updatePaymentDetailTableDTO);
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
												System.out
														.println("Close listener called");
											}
										});
										Button okBtn = new Button("Cancel");
										okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
										okBtn.addClickListener(new Button.ClickListener() {

											@Override
											public void buttonClick(
													ClickEvent event) {
												List<BankDetailsTableDTO> bankDetailsTableDTO = bankDetailsTableObj
														.getValues();
												bankDetailsTableDTO = new ArrayList<BankDetailsTableDTO>();
												// bean.setVerificationAccountDeatilsTableDTO(bankDetailsTableDTO);
												popup.close();
											}
										});

										VerticalLayout vlayout = new VerticalLayout(
												bankDetailsTableObj);
										HorizontalLayout hLayout = new HorizontalLayout(
												okBtn);
										hLayout.setSpacing(false);
										vlayout.setMargin(false);
										vlayout.addComponent(hLayout);
										vlayout.setComponentAlignment(hLayout,
												Alignment.BOTTOM_CENTER);
										popup.setContent(vlayout);
										popup.setModal(true);
										UI.getCurrent().addWindow(popup);
									}
									// }
								});
						return searchButton;
					}
				});
		table.removeGeneratedColumn("legaldetails");
		table.addGeneratedColumn("legaldetails", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)itemId;
				
				Button button = new Button("Legal Details");
				button.addClickListener(new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						if(tableDTO.isLegalPayment()){
							showLegalDetailsView(tableDTO);
						}			
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("90px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	if(!tableDTO.isLegalPayment()){
		    		button.setVisible(false);
		    	}
				return button;
			}
		});
	}

	public void linkPayeeBankDetials(BankDetailsTableDTO viewSearchCriteriaDTO) {
		CreateAndSearchLotTableDTO currentItemId = bankDetailsTableObj
				.getBankDetailsDTO();
		Map<String, AbstractField<?>> tableRow = tableItem.get(currentItemId);
		TextField nameAsPerBank = (TextField) tableRow
				.get("nameAsPerBankAccount");
		TextField accountType = (TextField) tableRow.get("accountType");
		TextField accountPref = (TextField) tableRow.get("accountPreference");
		TextField ifsc = (TextField) tableRow.get("ifscCode");
		TextField txtBankName = (TextField) tableRow.get("bankName");
		TextField branchName = (TextField) tableRow.get("branchName");
		TextField panNo = (TextField) tableRow.get("panNo");
		TextField beneficiaryAcntNo = (TextField) tableRow
				.get("beneficiaryAcntNo");
		TextField micrText = (TextField) tableRow.get("micrCode");

		if (null != viewSearchCriteriaDTO.getMicrCode()) {
			micrText.setValue(viewSearchCriteriaDTO.getMicrCode());
		}
		if (null != viewSearchCriteriaDTO.getNamePerBankAccnt()) {
			nameAsPerBank.setValue(viewSearchCriteriaDTO.getNamePerBankAccnt());
		}
		if (null != viewSearchCriteriaDTO.getAccountType()) {
			accountType.setValue(viewSearchCriteriaDTO.getAccountType());
		}
		
		if (null != viewSearchCriteriaDTO.getPreference()) {
			accountPref.setValue(viewSearchCriteriaDTO.getPreference());
		}
		if (null != viewSearchCriteriaDTO.getIfscCode()) {
			ifsc.setValue(viewSearchCriteriaDTO.getIfscCode());
		}
		if (txtBankName != null) {
			txtBankName.setValue(viewSearchCriteriaDTO.getNameOfBank());
		}
		if (null != viewSearchCriteriaDTO.getBranchName()) {
			branchName.setValue(viewSearchCriteriaDTO.getBranchName());
		}
		if (null != viewSearchCriteriaDTO.getPanNo()) {
			panNo.setValue(viewSearchCriteriaDTO.getPanNo());
		}
		if (null != viewSearchCriteriaDTO.getAccountNumber()) {
			beneficiaryAcntNo
					.setValue(viewSearchCriteriaDTO.getAccountNumber());
		}

	}

	public BlurListener getInterestAmnt(final TextField interstAmnt) {

		BlurListener listener = new BlurListener() {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {

				TextField component = (TextField) event.getComponent();
				String value = null;
				// String[] values = null;
				Number number = null;
				if (null != component) {
					value = component.getValue();
					/**
					 * The below parse done for avoid Number format Exception
					 */

					try {
						number = NumberFormat.getNumberInstance(
								java.util.Locale.US).parse(value);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (null != number) {
					intPenalInterestAmnt = number.doubleValue();
				}
			}

		};
		return listener;

	}

	public BlurListener totalAmountListener(final TextField interstAmnt) {

		BlurListener listener = new BlurListener() {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {

				TextField component = (TextField) event.getComponent();

				CreateAndSearchLotTableDTO tableDTO = new CreateAndSearchLotTableDTO();
				TextField txtField = (TextField) component;
				tableDTO = (CreateAndSearchLotTableDTO) txtField.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem
						.get(tableDTO);
				TextField penalTotalAmnt = (TextField) hashMap
						.get("penalTotalAmnt");
				Double penalTotalamnt = 0d;

				Double faApproveAmount = null != tableDTO.getFaApprovedAmnt() ? tableDTO
						.getFaApprovedAmnt() : 0d;
				Double interestAmount = null != tableDTO.getIntrestAmount() ? tableDTO
						.getIntrestAmount() : 0d;

				penalTotalamnt = faApproveAmount + interestAmount;

				double decimalOfApproveAmnt = penalTotalamnt * 10 % 10;
				int converttoIntOfApproveAmnt = (int) decimalOfApproveAmnt;

				if (converttoIntOfApproveAmnt >= 5) {
					tableDTO.setPenalTotalAmnt(Math.ceil(penalTotalamnt));
				}

				else {
					tableDTO.setPenalTotalAmnt(Math.floor(penalTotalamnt));
				}

				if (null != penalTotalAmnt
						&& null != tableDTO.getPenalTotalAmnt()) {
					penalTotalAmnt.setValue(String.valueOf(tableDTO
							.getPenalTotalAmnt()));
				}

			}
		};
		return listener;

	}

	public BlurListener noOfDaysExceedingListener(
			final TextField txtNoOfDaysExceeding) {

		BlurListener listener = new BlurListener() {

			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {

				TextField component = (TextField) event.getComponent();
				String value = null;
				if (null != component) {
					value = component.getValue();
				}

				CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) txtNoOfDaysExceeding
						.getData();
				TextField txtField = (TextField) component;
				tableDTO = (CreateAndSearchLotTableDTO) txtField.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem
						.get(tableDTO);
				TextField penalInterestAmt = (TextField) hashMap
						.get("intrestAmount");
				TextField penalTotalAmnt = (TextField) hashMap
						.get("penalTotalAmnt");

				Integer diffNoOfDays = 0;

				if (null != value && !(value.equals(""))) {

					intNoOfDays = Integer.valueOf(value);
					if (intNoOfDays >= -3) {

						diffNoOfDays = intNoOfDays
								- tableDTO.getNoOfDaysExceedingforCalculation();

						if (diffNoOfDays <= 3) {
							tableDTO.setNoOfDiffDays(diffNoOfDays);
							tableDTO.setNoofdaysexceeding(intNoOfDays);

						} else {
							tableDTO.setNoofdaysexceeding(intNoOfDays);
							tableDTO.setNoOfDiffDays(diffNoOfDays);
							fireViewEvent(
									SearchCreateBatchPresenter.VALIDATION, null);

						}
						Double faApprovedAmnt = null != tableDTO
								.getFaApprovedAmnt() ? tableDTO
								.getFaApprovedAmnt() : 0d;
						Double interestRate = 0d;

						if ((tableDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID)
								|| tableDTO
										.getDocReceivedFrom()
										.equalsIgnoreCase(
												SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)) {
							interestRate = 0d;
						} else {
							interestRate = null != tableDTO.getIntrestRate() ? tableDTO
									.getIntrestRate() : 0d;
						}
						Double noOfExceedingDays1 = null != tableDTO
								.getNoofdaysexceeding() ? tableDTO
								.getNoofdaysexceeding() : 0d;

						Double noOfExceedingDays = Math
								.abs(noOfExceedingDays1 / 365);

						Double penalInterestAmount = faApprovedAmnt
								* (interestRate / 100) * (noOfExceedingDays);

						Double penalTotalamnt = 0d;
						Double approvedAmnt = 0d;
						if (null != penalInterestAmount) {

							double decimalNo = penalInterestAmount * 10 % 10;
							int converttoInt = (int) decimalNo;

							if (converttoInt >= 5) {
								approvedAmnt = Math.ceil(penalInterestAmount);
							} else {
								approvedAmnt = Math.floor(penalInterestAmount);
							}

							if (null != penalInterestAmt
									&& null != approvedAmnt) {
								penalInterestAmt.setValue(String.valueOf(Math
										.abs(approvedAmnt)));
							}
							tableDTO.setIntrestAmount(Math.abs(approvedAmnt));
							tableDTO.setInterestAmntForCalculation(Math
									.abs(approvedAmnt));
						}

						penalTotalamnt = faApprovedAmnt
								+ tableDTO.getIntrestAmount();

						double decimalOfApproveAmnt = penalTotalamnt * 10 % 10;
						int converttoIntOfApproveAmnt = (int) decimalOfApproveAmnt;

						if (converttoIntOfApproveAmnt >= 5) {
							tableDTO.setPenalTotalAmnt(Math
									.ceil(penalTotalamnt));
						}

						else {
							tableDTO.setPenalTotalAmnt(Math
									.floor(penalTotalamnt));
						}

						if (null != penalTotalAmnt
								&& null != tableDTO.getPenalTotalAmnt()) {
							penalTotalAmnt.setValue(String.valueOf(tableDTO
									.getPenalTotalAmnt()));
						}

					}

					else {
						fireViewEvent(
								SearchCreateBatchPresenter.NO_OF_EXCEEDING_DAYS_VALIDATION,
								null);
						if (null != tableDTO.getNoofdaysexceeding())
							txtNoOfDaysExceeding.setValue(String
									.valueOf(tableDTO.getNoofdaysexceeding()));
					}

				}
			}
		};
		return listener;

	}

	private void addListener(final CheckBox chkBox) {
		chkBox.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty()
						&& null != event.getProperty().getValue()) {

					boolean value = (Boolean) event.getProperty().getValue();
					CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) chkBox
							.getData();

					if (value) {
						tableDTO.setCheckBoxStatus("true");
					} else {
						tableDTO.setCheckBoxStatus("false");
					}
					/**
					 * Added for issue#192.
					 * */
					if (null != tableDataList && !tableDataList.isEmpty()) {
						for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDataList) {

							if (createAndSearchLotTableDTO.getClaimPaymentKey()
									.equals(tableDTO.getClaimPaymentKey())) {
								if (value) {
									createAndSearchLotTableDTO
											.setCheckBoxStatus("true");
								} else {
									createAndSearchLotTableDTO
											.setCheckBoxStatus("false");
								}
							}
						}
					}

				}
			}
		});
	}

	public void setValueForCheckBox(Boolean value) {
		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table
				.getItemIds();

		for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
			for (CreateAndSearchLotTableDTO searchTabledto : tableDataList) {
				if (searchTabledto.getClaimPaymentKey().equals(
						searchTableDTO.getClaimPaymentKey())) {
					if (value) {
						searchTableDTO.setCheckBoxStatus("true");
						if (null != compMap && !compMap.isEmpty()) {
							CheckBox chkBox = (CheckBox) compMap
									.get(searchTabledto.getClaimPaymentKey());
							chkBox.setValue(value);
						}
						break;
					} else {
						searchTableDTO.setCheckBoxStatus("false");
						if (null != compMap && !compMap.isEmpty()) {
							CheckBox chkBox = (CheckBox) compMap
									.get(searchTableDTO.getClaimPaymentKey());
							chkBox.setValue(value);
						}
						break;
					}

				}
			}
		}
	}

	public void setValueForSelectAllCheckBox(Boolean value) {
		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table
				.getItemIds();

		if (!(this.menuString != null
				&& !this.menuString.equalsIgnoreCase(SHAConstants.CREATE_BATCH) && (this.menuString
				.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || this.menuString
				.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)))) {
			for (CreateAndSearchLotTableDTO searchTabledto : tableDataList) {
				for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
					if (searchTabledto.getClaimPaymentKey().equals(
							searchTableDTO.getClaimPaymentKey())) {
						if (value) {
							searchTabledto.setCheckBoxStatus("true");
							if (null != compMap && !compMap.isEmpty()) {
								CheckBox chkBox = (CheckBox) compMap
										.get(searchTabledto
												.getClaimPaymentKey());
								if (null != chkBox)
									chkBox.setValue(value);
							}
							break;
						} else {
							searchTabledto.setCheckBoxStatus("false");
							if (null != compMap && !compMap.isEmpty()) {
								CheckBox chkBox = (CheckBox) compMap
										.get(searchTabledto
												.getClaimPaymentKey());
								if (null != chkBox)
									chkBox.setValue(value);
							}
							break;
						}

					}
				}
			}
		}
	}

	public void setFinalTableList(List<CreateAndSearchLotTableDTO> tableRows) {
		Boolean isListEmpty = false;
		// tableDataList = new ArrayList<CreateAndSearchLotTableDTO>();
		// List<CreateAndSearchLotTableDTO> dtoList = new
		// ArrayList<CreateAndSearchLotTableDTO>();
		if (null != tableRows && !tableRows.isEmpty()) {
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableRows) {

				/**
				 * When user tries to navigate from forward to previous page.
				 * already added records shouldn't be added to the
				 * tableDataList. Hence another list where keys are stored is
				 * used, where if a key is already existing in that list, then
				 * it won't get added in the main list.This is done to avoid
				 * duplication.
				 * 
				 * */

				if (null != tableDataList && !tableDataList.isEmpty()
						&& null != tableDataKeyList
						&& !tableDataKeyList.isEmpty()) {
					if (!tableDataKeyList.contains(createAndSearchLotTableDTO
							.getClaimPaymentKey())) {
						tableDataList.add(createAndSearchLotTableDTO);
						tableDataKeyList.add(createAndSearchLotTableDTO
								.getClaimPaymentKey());
					}
				} else {
					isListEmpty = true;
					break;
				}
			}
			/**
			 * 
			 * When first page is painted, table data list would be empty and
			 * hence adding all the records and its keys to data list and key
			 * list
			 * 
			 * */
			if (isListEmpty) {
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableRows) {
					tableDataList.add(createAndSearchLotTableDTO);
					tableDataKeyList.add(createAndSearchLotTableDTO
							.getClaimPaymentKey());
				}

			}
			/*
			 * if(null != dtoList && !dtoList.isEmpty()){ for
			 * (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : dtoList)
			 * { tableDataList.add(createAndSearchLotTableDTO); } }
			 */
		}
		// TODO Auto-generated method stub

	}

	public List<CreateAndSearchLotTableDTO> getTableItems() {
		List<CreateAndSearchLotTableDTO> itemIds = (List<CreateAndSearchLotTableDTO>) this.table
				.getItemIds();
		return itemIds;
	}

	public void resetTableDataList() {
		if (null != tableDataList) {
			tableDataList.clear();
		}
		if (null != tableDataKeyList) {
			tableDataKeyList.clear();
		}
	}

	public String isValid() {

		StringBuffer err = new StringBuffer();

		List<CreateAndSearchLotTableDTO> requestTableList = (List<CreateAndSearchLotTableDTO>) table
				.getItemIds();
		List<CreateAndSearchLotTableDTO> finalListForProcessing = null;
		if (null != requestTableList && !requestTableList.isEmpty()) {
			finalListForProcessing = new ArrayList<CreateAndSearchLotTableDTO>();
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {

				if (null != createAndSearchLotTableDTO.getCheckBoxStatus()) {
					if (("true").equalsIgnoreCase(createAndSearchLotTableDTO
							.getCheckBoxStatus())) {
						finalListForProcessing.add(createAndSearchLotTableDTO);
					}
				}
			}
		}
		if (null != finalListForProcessing && !finalListForProcessing.isEmpty()) {
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : finalListForProcessing) {

				if (null != createAndSearchLotTableDTO.getNoOfDiffDays()
						&& createAndSearchLotTableDTO.getNoOfDiffDays() > 3
						&& (null == createAndSearchLotTableDTO.getRemarks() || ("")
								.equalsIgnoreCase(createAndSearchLotTableDTO
										.getRemarks()))
						&& (null == createAndSearchLotTableDTO.getRemarks() || ("")
								.equalsIgnoreCase(createAndSearchLotTableDTO
										.getRemarks()))) {
					err.append(
							"\nPlease Enter Interest remarks for the Intimation Number : ")
							.append(createAndSearchLotTableDTO
									.getIntimationNo()).append("<br>");
				}
			}

		}
		return err.toString();
	}

	public void addBeanToList(CreateAndSearchLotTableDTO createLotDTO) {
		data.addBean(createLotDTO);
	}

	public void addList(List<CreateAndSearchLotTableDTO> createLotDTO) {
		for (CreateAndSearchLotTableDTO createandSearchLotDto : createLotDTO) {
			data.addBean(createandSearchLotDto);
		}
	}

	@SuppressWarnings("unchecked")
	public List<CreateAndSearchLotTableDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<CreateAndSearchLotTableDTO> itemIds = (List<CreateAndSearchLotTableDTO>) this.table
				.getItemIds();
		return itemIds;

	}

	public void setTableList(List<CreateAndSearchLotTableDTO> tableItems,
			String createBatch) {
		table.removeAllItems();
		if (null != tableItems && !tableItems.isEmpty()) {
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableItems) {
				if (createAndSearchLotTableDTO.getBancsPriorityFlag() != null) {
					SelectValue pFlag = new SelectValue();
					pFlag.setValue(createAndSearchLotTableDTO
							.getBancsPriorityFlag());
					createAndSearchLotTableDTO.setPriorityFlag(pFlag);
				}
				table.addItem(createAndSearchLotTableDTO);
			}

		}

	}

	public void setTotalNoOfRecords(int iSize) {
		fLayout.removeAllComponents();
		totalRocordsTxt
				.setCaption("<span style='font-size:medium;font-weight:medium;'>Total Number Of Records : "
						+ String.valueOf(iSize) + "</span");
		totalRocordsTxt.setCaptionAsHtml(true);
		fLayout.addComponent(totalRocordsTxt);
		fLayout.setComponentAlignment(totalRocordsTxt, Alignment.TOP_CENTER);
	}

	public void sortTableList() {
		table.sort(new Object[] { "numberofdays" }, new boolean[] { false });
	}

	public void showClaimsDMSView(String intimationNo) {
		fireViewEvent(
				SearchCreateBatchPresenter.CREATE_BATCH_SHOW_VIEW_DOCUMENTS,
				intimationNo);
	}

	public void showLinkedPolicyDetails(CreateAndSearchLotTableDTO tableDTO) {
		fireViewEvent(SearchCreateBatchPresenter.VIEW_LINKED_POLICY_BATCH,
				tableDTO);
	}

	public void showEditPaymentDetailsView(CreateAndSearchLotTableDTO tableDTO) {
		fireViewEvent(
				SearchCreateBatchPresenter.CREATE_BATCH_SHOW_EDIT_PAYMENT_DETAILS_VIEW,
				tableDTO);
	}

	public Pageable getPageable() {
		return this.pageUI.getPageable();
	}

	public void setHasNextPage(boolean flag) {
		this.pageUI.hasMoreRecords(flag);

	}

	public void setPage(Page<CreateAndSearchLotTableDTO> page) {
		this.pageUI.setPageDetails(page);
	}

	protected void tablesize() {
		table.setPageLength(table.size() + 1);
		int length = table.getPageLength();
		if (length >= 7) {
			table.setPageLength(7);
		}

	}

	public void resetTable() {
		if (null != table) {
			List<CreateAndSearchLotTableDTO> tableList = (List<CreateAndSearchLotTableDTO>) table
					.getItemIds();
			if (null != tableList && !tableList.isEmpty()) {
				table.removeAllItems();
				if (null != tableLayout) {
					// tableLayout.removeAllComponents();
				}
			}
		}
		// this.pageUI.resetPage();
	}

	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	/*
	 * public void setRowColor(){
	 * 
	 * ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds());
	 * final Object selectedRowId = getSelectedRowId(itemIds);
	 * createandSearchLotDto = (CreateAndSearchLotTableDTO)selectedRowId;
	 * table.setCellStyleGenerator(new CellStyleGenerator() {
	 * 
	 * @Override public String getStyle(Table source, Object itemId, Object
	 * propertyId) { createandSearchLotDto =
	 * (CreateAndSearchLotTableDTO)selectedRowId; if(createandSearchLotDto !=
	 * null){ Double penalInterest = createandSearchLotDto.getIntrestAmount();
	 * 
	 * if(penalInterest>0){
	 * 
	 * return "select"; } else{ return "none"; }
	 * 
	 * } else{ return "none"; } }
	 * 
	 * });
	 * 
	 * }
	 */

	public void setRowColor(final CreateAndSearchLotTableDTO dto) {

		table.setCellStyleGenerator(new CellStyleGenerator() {

			@Override
			public String getStyle(Table source, Object itemId,
					Object propertyId) {

				CreateAndSearchLotTableDTO dto1 = (CreateAndSearchLotTableDTO) itemId;
				if (dto1 != null) {
					String colourFlag = null != dto1.getRecStatusFlag() ? dto1
							.getRecStatusFlag() : "";
					// System.out.println("-------No Of times loop executes-----");
					if (colourFlag.equals("E")) {

						return "yellow";
					} else if (colourFlag.equals("F")) {

						return "red";
					} else if (colourFlag.equals("R")) {
						return "amber";
					} else {
						return "none";
					}

				} else {
					return "none";
				}
			}

		});

	}

	/*
	 * public Object getSelectedRowId(ArrayList<Object> itemIds ){
	 * 
	 * for(Object id:itemIds){ createandSearchLotDto =
	 * (CreateAndSearchLotTableDTO)id; return id;
	 * 
	 * } return null; }
	 */

	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO, CreateAndSearchLotTableDTO bean) {
		autoPopulatePreviousPaymentDetails(tableDTO, bean);

	}

	public ValueChangeListener ifscListener() {
		ValueChangeListener listener = new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox paymentTypeCmb = (ComboBox) event.getProperty();
				if (paymentTypeCmb != null && paymentTypeCmb.getValue() != null) {
					SelectValue value = (SelectValue) paymentTypeCmb.getValue();
					CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) paymentTypeCmb
							.getData();
					if (value.getValue() != null) {
						if (value.getValue().equalsIgnoreCase("NEFT")) {
							updatePaymentDetailTableDTO.getSearchButton()
									.setEnabled(true);
							updatePaymentDetailTableDTO.getPayableAtButton()
									.setEnabled(false);
							
							if(menuString == null ||(menuString != null
									&& menuString.equalsIgnoreCase(SHAConstants.CREATE_BATCH))) {
								showIfscPopup(updatePaymentDetailTableDTO);
							}	
							HashMap<String, AbstractField<?>> hashMap = tableItem
									.get(updatePaymentDetailTableDTO);
							if (hashMap != null) {

								TextField txtbeneficiaryAcntNo = (TextField) hashMap
										.get("beneficiaryAcntNo");
								if (txtbeneficiaryAcntNo != null) {
									txtbeneficiaryAcntNo
											.setEnabled(Boolean.TRUE);
								}
								/*
								 * TextField txtpayableAt =
								 * (TextField)hashMap.get("payableAt");
								 * if(txtpayableAt!=null){
								 * txtpayableAt.setEnabled(Boolean.FALSE); }
								 */

							}
						} else {
							updatePaymentDetailTableDTO.getSearchButton()
									.setEnabled(false);
							
							if(menuString == null ||(menuString != null
									&& menuString.equalsIgnoreCase(SHAConstants.CREATE_BATCH))) {
								ViewSearchCriteriaTableDTO dto = new ViewSearchCriteriaTableDTO();
								setUpIFSCDetails(dto, updatePaymentDetailTableDTO);
							}	
							HashMap<String, AbstractField<?>> hashMap = tableItem
									.get(updatePaymentDetailTableDTO);
							if (hashMap != null) {

								TextField txtbeneficiaryAcntNo = (TextField) hashMap
										.get("beneficiaryAcntNo");
								if (txtbeneficiaryAcntNo != null) {
									txtbeneficiaryAcntNo
											.setEnabled(Boolean.TRUE);
									txtbeneficiaryAcntNo.setValue(null);
									txtbeneficiaryAcntNo
											.setEnabled(Boolean.FALSE);

								}
								/*
								 * TextField txtpayableAt =
								 * (TextField)hashMap.get("payableAt");
								 * if(txtpayableAt!=null){
								 * txtpayableAt.setEnabled(Boolean.TRUE); }
								 */
								updatePaymentDetailTableDTO
										.getPayableAtButton().setEnabled(true);
							}
						}
					}
				}
			}
		};
		return listener;

	}

	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {

		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(updatePaymentDetailTableDTO);
		if (hashMap != null) {

			TextField txtIfscCode = (TextField) hashMap.get("ifscCode");
			if (txtIfscCode != null) {
				txtIfscCode.setReadOnly(false);
				txtIfscCode.setValue(dto.getIfscCode());
				// txtIfscCode.setReadOnly(true);
			}

			TextField txtBankName = (TextField) hashMap.get("bankName");
			if (txtBankName != null) {
				txtBankName.setValue(dto.getBankName());
			}

			TextField txtBranch = (TextField) hashMap.get("branchName");
			if (txtBranch != null) {
				txtBranch.setValue(dto.getBranchName());
			}

			TextField txtCity = (TextField) hashMap.get("branchCity");
			if (txtCity != null) {
				txtCity.setValue(dto.getCity());
			}

			TextField txtbeneficiaryAcntNo = (TextField) hashMap
					.get("beneficiaryAcntNo");
			if (txtbeneficiaryAcntNo != null) {
				txtbeneficiaryAcntNo.setReadOnly(false);
			}

		}
	}

	public ValueChangeListener paymentCpuCodeListener() {
		ValueChangeListener listener = new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField paymentTypeCmb = (TextField) event.getProperty();
				if (paymentTypeCmb != null && paymentTypeCmb.getValue() != null) {
					CreateAndSearchLotTableDTO updatePaymentDetailTableDTO = (CreateAndSearchLotTableDTO) paymentTypeCmb
							.getData();
					fireViewEvent(
							SearchCreateBatchPresenter.GET_PAYMENT_CPU_NAME,
							paymentTypeCmb.getValue(),
							updatePaymentDetailTableDTO);
				}
			}
		};
		return listener;

	}

	public void setPaymentCpuName(
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {
		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(updatePaymentDetailTableDTO);
		if (hashMap != null) {
			TextField paymentCpuName = (TextField) hashMap
					.get("paymentCpuName");
			if (paymentCpuName != null) {
				paymentCpuName.setReadOnly(false);
				paymentCpuName.setValue(updatePaymentDetailTableDTO
						.getPaymentCpuName());
				paymentCpuName.setReadOnly(true);
			}
		}

	}

	public void setDropDownValues(BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> paymentStatus) {
		this.paymentMode = paymentStatus;
		this.cpuCode = cpuCode;
	}

	public void changeOfModeReasonListener(TextArea searchField,
			final Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForPreauthMedRemarks1", ShortcutAction.KeyCode.F8,
				null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForMedicalRemarks(searchField,
				getShortCutListenerForChangeOfReason(searchField));

	}

	public void changeOfReasonListener(TextArea searchField,
			final Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForMedicalRemarks(searchField,
				getShortCutListenerForChangeOfReasonPreauth(searchField));

	}

	public void handleShortcutForMedicalRemarks(final TextArea textField,
			final ShortcutListener shortcutListener) {
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

	private ShortcutListener getShortCutListenerForChangeOfReason(
			final TextArea txtFld) {
		ShortcutListener listener = new ShortcutListener("", KeyCodes.KEY_F8,
				null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout = new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f, Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,
						Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				CreateAndSearchLotTableDTO mainDto = (CreateAndSearchLotTableDTO) txtFld
						.getData();
				txtArea.setData(mainDto);
				// txtArea.setStyleName("Boldstyle");
				txtArea.setValue(mainDto.getReasonForChange());
				txtArea.setNullRepresentation("");
				// txtArea.setSizeFull();
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("75%");
				dialog.setWidth("65%");
				txtArea.setReadOnly(false);
				txtArea.setMaxLength(4000);

				txtArea.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea) event.getProperty())
								.getValue());
						CreateAndSearchLotTableDTO mainDto = (CreateAndSearchLotTableDTO) txtFld
								.getData();
						mainDto.setReasonForChange(txtFld.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);

				String strCaption = "";

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
						// TextArea txtArea = (TextArea)dialog.getData();
						// txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});

				if (getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						// TextArea txtArea = (TextArea)dialog.getData();
						// txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
			}
		};

		return listener;
	}

	private void showIfscPopup(
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {
		Window popup = new com.vaadin.ui.Window();
		viewSearchCriteriaWindow.setWindowObject(popup);
		viewSearchCriteriaWindow.setPresenterString(SHAConstants.CREATE_BATCH,
				updatePaymentDetailTableDTO);
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

	private ShortcutListener getShortCutListenerForChangeOfReasonPreauth(
			final TextArea txtFld) {
		ShortcutListener listener = new ShortcutListener("", KeyCodes.KEY_F8,
				null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout = new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f, Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,
						Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				CreateAndSearchLotTableDTO mainDto = (CreateAndSearchLotTableDTO) txtFld
						.getData();
				txtArea.setData(mainDto);
				// txtArea.setStyleName("Boldstyle");
				txtArea.setValue(mainDto.getPayModeChangeReason());
				txtArea.setNullRepresentation("");
				// txtArea.setSizeFull();
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("75%");
				dialog.setWidth("65%");
				txtArea.setReadOnly(false);

				txtArea.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea) event.getProperty())
								.getValue());
						CreateAndSearchLotTableDTO mainDto = (CreateAndSearchLotTableDTO) txtFld
								.getData();
						mainDto.setReasonForChange(txtFld.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);

				String strCaption = "";

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
						// TextArea txtArea = (TextArea)dialog.getData();
						// txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});

				if (getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						// TextArea txtArea = (TextArea)dialog.getData();
						// txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
			}
		};

		return listener;
	}

	public ValueChangeListener legalListener() {
		ValueChangeListener listener = new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField legalFirstName = (TextField) event.getProperty();
				if (presenterString != null
						&& ((SHAConstants.CREATE_BATCH_TYPE)
								.equalsIgnoreCase(presenterString) 
								|| (SHAConstants.QUICK_SEARCH)
								.equalsIgnoreCase(presenterString))) {
					if (legalFirstName != null
							&& legalFirstName.getValue() != null
							&& !legalFirstName.getValue().isEmpty()
							&& !legalFirstName.equals(null)) {
						CreateAndSearchLotTableDTO createLotDto = (CreateAndSearchLotTableDTO) legalFirstName
								.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem
								.get(createLotDto);
						TextField payeeName = (TextField) hashMap
								.get("payeeNameStr");

						if (payeeName != null && payeeName.getValue() != null) {
							// showErrorMessage("Please unselect payee name");
							payeeName.setReadOnly(false);
							payeeName.setValue(null);
							payeeName.setReadOnly(true);
							payeeName.setNullRepresentation("");
							// payeeName.setEnabled(Boolean.FALSE);
						} else {
							payeeName.setEnabled(Boolean.FALSE);
						}

						if(createLotDto.getPaymentType() != null && createLotDto.getPaymentType().getId() != null &&
								createLotDto.getPaymentType().getId().equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)){
							createLotDto.setVerificationClicked(false);
							if (createLotDto.getVerifyAccntDtlsBtn() != null) {
								createLotDto.getVerifyAccntDtlsBtn().setStyleName(
										ValoTheme.BUTTON_DANGER);
								createLotDto.getVerifyAccntDtlsBtn().setEnabled(true);
								createLotDto.setVerifiedStatusFlag("N");
							}
						}
						
					} else {
						CreateAndSearchLotTableDTO createLotDto = (CreateAndSearchLotTableDTO) legalFirstName
								.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem
								.get(createLotDto);
						ComboBox payeeName = (ComboBox) hashMap
								.get("payeeName");
						// payeeName.setEnabled(createLotDto.getIsInsured());
						if (null != payeeSearchMap && !payeeSearchMap.isEmpty()) {
							Button payeeSearchBtn = (Button) payeeSearchMap
									.get(createLotDto.getClaimPaymentKey());
							payeeSearchBtn.setEnabled(true);
							
							if (createLotDto.getNominee() != null
									|| (createLotDto.getNominee() != null && !createLotDto
											.getNominee().isEmpty())) {
								payeeSearchBtn.setEnabled(false);
							}
						}
					}
				}
			}
		};
		return listener;

	}

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

	public ValueChangeListener paymentCpuListener() {
		ValueChangeListener listener = new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField paymentCpu = (TextField) event.getProperty();
				CreateAndSearchLotTableDTO createLotDto = (CreateAndSearchLotTableDTO) paymentCpu
						.getData();
				if (paymentCpu != null && paymentCpu.getValue() != null
						&& createLotDto.getIsPaymentCpuCodeListenerEnable()) {
					String value = paymentCpu.getValue();
					String[] cpuCode = value.split("-");
					String cpuCodeStr = cpuCode[0].trim();
					fireViewEvent(
							SearchCreateBatchPresenter.PAYMENT_CPU_EMAIL_ID,
							cpuCodeStr, createLotDto);
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(createLotDto);
					TextArea emailID = (TextArea) hashMap.get("emailID");
					if (emailID != null) {
						emailID.setValue(createLotDto.getZonalMailId());
					}
				}
			}
		};
		return listener;

	}

	public ValueChangeListener paymentTypeListener(ComboBox searchField,
			final CreateAndSearchLotTableDTO createLotDto) {
		ValueChangeListener listener = new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				ComboBox paymentType = (ComboBox) event.getProperty();
				SelectValue value = (SelectValue) paymentType.getValue();

				HashMap<String, AbstractField<?>> hashMap = tableItem
						.get(createLotDto);
				TextArea emailID = (TextArea) hashMap.get("emailID");
				TextField panNo = (TextField) hashMap.get("panNo");
				TextField payableAt = (TextField) hashMap.get("payableAt");
				ComboBox payeeName = (ComboBox) hashMap.get("payeeName");
				TextArea payeeNameReasonForChange = (TextArea) hashMap
						.get("reasonForChange");
				TextArea paymentTypeReasonForChange = (TextArea) hashMap
						.get("payModeChangeReason");
				TextField legalHairName = (TextField) hashMap
						.get("legalFirstName");
				TextField beneficiayAcntNo = (TextField) hashMap
						.get("beneficiaryAcntNo");
				TextField ifscCode = (TextField) hashMap.get("ifscCode");
				Button prvsAcntDtlsBtn = createLotDto.getPreviousAccntDtlsBtn();

				if (value != null
						&& value.getId() != null
						&& value.getId().equals(
								ReferenceTable.PAYMENT_MODE_CHEQUE_DD)) {

					panNo.setEnabled(true);
					// payableAt.setEnabled(true);
					beneficiayAcntNo.setEnabled(false);
					if (prvsAcntDtlsBtn != null) {
						prvsAcntDtlsBtn.setEnabled(false);
					}

				} else {
					panNo.setEnabled(true);
					// payableAt.setEnabled(false);
					
					if (menuString != null
							&& !menuString.equalsIgnoreCase(SHAConstants.CREATE_BATCH)
							&& (menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString
									.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
						beneficiayAcntNo.setEnabled(true);
					}
					
					ifscCode.setEnabled(false);
					if (prvsAcntDtlsBtn != null) {
						prvsAcntDtlsBtn.setEnabled(true);
					}
				}

				if (null != paymentTypeReasonForChange
						&& null != paymentTypeReasonForChange.getValue()) {
					paymentTypeReasonForChange.setValue(null);
				}
			}
		};
		return listener;

	}

	public boolean validatePage() {

		List<CreateAndSearchLotTableDTO> itemIds = (List<CreateAndSearchLotTableDTO>) this.table
				.getItemIds();

		StringBuffer eMsg = new StringBuffer();
		Boolean hasError = false;

		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : itemIds) {
			if (createAndSearchLotTableDTO.getCheckBoxStatus() != null
					&& ("true").equalsIgnoreCase(createAndSearchLotTableDTO
							.getCheckBoxStatus()) ) {

				if (null == createAndSearchLotTableDTO.getPaymentType()) {
					eMsg.append("Please choose payment Type</br>");
					hasError = true;
				}

				if (null != createAndSearchLotTableDTO.getPaymentType()
						&& null != createAndSearchLotTableDTO.getPaymentType()
								.getId()
						&& (ReferenceTable.PAYMENT_MODE_BANK_TRANSFER
								.equals(createAndSearchLotTableDTO
										.getPaymentType().getId()))) {

					if (createAndSearchLotTableDTO.getIfscCode() == null
							|| createAndSearchLotTableDTO.getIfscCode().equals(
									"")) {
						eMsg.append("Please enter the IFSC Code</br>");
						hasError = true;
					}

					if (createAndSearchLotTableDTO.getBeneficiaryAcntNo() == null
							|| createAndSearchLotTableDTO
									.getBeneficiaryAcntNo().isEmpty()) {
						eMsg.append("Please enter Account No</br>");
						hasError = true;
					}
				} else {
					
					/*if(isAlertMessageNeededForNEFTDetails(createAndSearchLotTableDTO)){
						eMsg.append(" </br> Select mode of payment as Bank Transfer only ");
						hasError = true;
						
					}*/


					if (createAndSearchLotTableDTO.getPayableAt() == null
							|| createAndSearchLotTableDTO.getPayableAt()
									.isEmpty()) {
						eMsg.append(" </br>Please enter Payable At ");
						hasError = true;
					}
				}

				if (createAndSearchLotTableDTO.getPaymentType() != null
						&& null != createAndSearchLotTableDTO.getPaymentType()
								.getId()
						&& (ReferenceTable.PAYMENT_MODE_BANK_TRANSFER
								.equals(createAndSearchLotTableDTO
										.getPaymentType().getId()))) {
					if (createAndSearchLotTableDTO.getCheckBoxStatus() != null
							&& ("true")
									.equalsIgnoreCase(createAndSearchLotTableDTO
											.getCheckBoxStatus())) {

						if (!createAndSearchLotTableDTO
								.getVerificationClicked()) {
							eMsg.append("Please Verify Account Details</br>");
							hasError = true;
						}
					}
				}
				if (createAndSearchLotTableDTO.getDocumentReceivedFrom() != null
						&& !createAndSearchLotTableDTO
								.getDocumentReceivedFrom()
								.equalsIgnoreCase(
										SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)) {

					if (null != createAndSearchLotTableDTO.getPayeeNameStr()) {
						if (null != createAndSearchLotTableDTO
								.getTempPayeeName()
								&& createAndSearchLotTableDTO
										.getTempPayeeName().getValue() != null
								&& !(createAndSearchLotTableDTO
										.getTempPayeeName().getValue()
										.equals(createAndSearchLotTableDTO
												.getPayeeNameStr()))
								&& (createAndSearchLotTableDTO
										.getReasonForChange() == null || createAndSearchLotTableDTO
										.getReasonForChange().equalsIgnoreCase(
												""))) {
							eMsg.append(" </br>Please enter Reason For changing the Payee Name ");
							hasError = true;
						}
					}
				}

				if ((null == createAndSearchLotTableDTO
						.getPayModeChangeReason())
						|| (createAndSearchLotTableDTO.getPayModeChangeReason()
								.equals(""))) {

					if (createAndSearchLotTableDTO.getPaymentType() != null
							&& null != createAndSearchLotTableDTO
									.getTempPaymentType()
							&& !(createAndSearchLotTableDTO
									.getTempPaymentType().getId()
									.equals(createAndSearchLotTableDTO
											.getPaymentType().getId()))) {

						hasError = true;
						eMsg.append("Please Enter Reason for Changing the Payment Mode</br>");
					}

				}

				if ((null == createAndSearchLotTableDTO.getPayeeNameStr() || (createAndSearchLotTableDTO
						.getPayeeNameStr().equals("")))
						&& (createAndSearchLotTableDTO.getNominee() == null || (createAndSearchLotTableDTO
								.getNominee() != null && createAndSearchLotTableDTO
								.getNominee().isEmpty()))
						&& (createAndSearchLotTableDTO.getLegalFirstName() == null || (createAndSearchLotTableDTO
								.getLegalFirstName() != null && createAndSearchLotTableDTO
								.getLegalFirstName().isEmpty()))) {
					hasError = true;
					eMsg.append("</br>Please select Payee Name / Nominee / Legal Heir");
				}
			}
		}

		if (hasError) {

			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			hasError = true;

			return !hasError;
		} else {
			return true;
		}

	}

	public boolean validatePageForBancs(CreateAndSearchLotTableDTO tableDTO) {
	
		List<CreateAndSearchLotTableDTO> itemIds = (List<CreateAndSearchLotTableDTO>) this.table
				.getItemIds();

		StringBuffer eMsg = new StringBuffer();
		Boolean hasError = false;

		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : itemIds) {
			if (createAndSearchLotTableDTO.getClaimPaymentKey().equals(
					tableDTO.getClaimPaymentKey())
					&& (menuString != null 
					&& (menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString
											.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) ) {

				if (null == createAndSearchLotTableDTO.getPaymentType()) {
					eMsg.append("Please choose payment Type</br>");
					hasError = true;
				}

				if (null != createAndSearchLotTableDTO.getPaymentType()
						&& null != createAndSearchLotTableDTO.getPaymentType()
								.getId()
						&& (ReferenceTable.PAYMENT_MODE_BANK_TRANSFER
								.equals(createAndSearchLotTableDTO
										.getPaymentType().getId()))) {

					if (createAndSearchLotTableDTO.getIfscCode() == null
							|| createAndSearchLotTableDTO.getIfscCode().equals(
									"")) {
						eMsg.append("Please enter the IFSC Code</br>");
						hasError = true;
					}

					if (createAndSearchLotTableDTO.getBeneficiaryAcntNo() == null
							|| createAndSearchLotTableDTO
									.getBeneficiaryAcntNo().isEmpty()) {
						eMsg.append("Please enter Account No</br>");
						hasError = true;
					}
				} else {

					if (createAndSearchLotTableDTO.getPayableAt() == null
							|| createAndSearchLotTableDTO.getPayableAt()
									.isEmpty()) {
						eMsg.append(" </br>Please enter Payable At ");
						hasError = true;
					}
				}

				if (createAndSearchLotTableDTO.getPaymentType() != null
						&& null != createAndSearchLotTableDTO.getPaymentType()
								.getId()
						&& (ReferenceTable.PAYMENT_MODE_BANK_TRANSFER
								.equals(createAndSearchLotTableDTO
										.getPaymentType().getId()))) {
					
						if (!createAndSearchLotTableDTO
								.getVerificationClicked()) {
							eMsg.append("Please Verify Account Details</br>");
							hasError = true;
						}
				}
				if (createAndSearchLotTableDTO.getDocumentReceivedFrom() != null
						&& !createAndSearchLotTableDTO
								.getDocumentReceivedFrom()
								.equalsIgnoreCase(
										SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)) {

					if (null != createAndSearchLotTableDTO.getPayeeNameStr()) {
						if (null != createAndSearchLotTableDTO
								.getTempPayeeName()
								&& createAndSearchLotTableDTO
										.getTempPayeeName().getValue() != null
								&& !(createAndSearchLotTableDTO
										.getTempPayeeName().getValue()
										.equals(createAndSearchLotTableDTO
												.getPayeeNameStr()))
								&& (createAndSearchLotTableDTO
										.getReasonForChange() == null || createAndSearchLotTableDTO
										.getReasonForChange().equalsIgnoreCase(
												""))) {
							eMsg.append(" </br>Please enter Reason For changing the Payee Name ");
							hasError = true;
						}
					}
				}

				if ((null == createAndSearchLotTableDTO
						.getPayModeChangeReason())
						|| (createAndSearchLotTableDTO.getPayModeChangeReason()
								.equals(""))) {

					if (createAndSearchLotTableDTO.getPaymentType() != null
							&& null != createAndSearchLotTableDTO
									.getTempPaymentType()
							&& !(createAndSearchLotTableDTO
									.getTempPaymentType().getId()
									.equals(createAndSearchLotTableDTO
											.getPaymentType().getId()))) {

						hasError = true;
						eMsg.append("Please Enter Reason for Changing the Payment Mode</br>");
					}

				}

				if ((null == createAndSearchLotTableDTO.getPayeeNameStr() || (createAndSearchLotTableDTO
						.getPayeeNameStr().equals("")))
						&& (createAndSearchLotTableDTO.getNominee() == null || (createAndSearchLotTableDTO
								.getNominee() != null && createAndSearchLotTableDTO
								.getNominee().isEmpty()))
						&& (createAndSearchLotTableDTO.getLegalFirstName() == null || (createAndSearchLotTableDTO
								.getLegalFirstName() != null && createAndSearchLotTableDTO
								.getLegalFirstName().isEmpty()))) {
					hasError = true;
					eMsg.append("</br>Please select Payee Name / Nominee / Legal Heir");
				}
			}
		}

		if (hasError) {

			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			hasError = true;

			return !hasError;
		} else {
			return true;
		}

	}
	public void setUpPaymentCpuCodeDetails(ViewSearchCriteriaTableDTO dto,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {

		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(updatePaymentDetailTableDTO);
		if (hashMap != null) {

			TextField txtpaymentCpuCode = (TextField) hashMap
					.get("paymentCpucodeTextValue");
			if (txtpaymentCpuCode != null) {
				txtpaymentCpuCode.setReadOnly(false);
				txtpaymentCpuCode.setValue(dto.getCpuCodeWithDescription());
				txtpaymentCpuCode.setReadOnly(true);
			}

		}
	}

	private void showPaymentCpuCodePopup(
			CreateAndSearchLotTableDTO createAndSearchLotTableDto) {
		final Window popup = new com.vaadin.ui.Window();

		paymentCpucodeListenerTable.init("", false, false);
		paymentCpucodeListenerTable.initPresenter(
				SHAConstants.CREATE_BATCH_PAYMENT_CPU_CODE_CHANGE,
				createAndSearchLotTableDto, popup);
		paymentCpucodeListenerTable.initTable();

		List<ViewSearchCriteriaTableDTO> paymentCpuCodeList = masterService
				.getTmpCpuCodeWithDescriptionList();

		paymentCpucodeListenerTable.setTableList(paymentCpuCodeList);
		VerticalLayout vLayout = new VerticalLayout();

		Button btnClose = new Button();
		btnClose.setCaption("Close");
		btnClose.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnClose.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(popup);
			}
		});

		HorizontalLayout closebutLayout = new HorizontalLayout();
		closebutLayout.addComponent(btnClose);
		closebutLayout.setSizeFull();
		vLayout.addComponents(paymentCpucodeListenerTable, closebutLayout);
		closebutLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_CENTER);

		popup.setWidth("30%");
		popup.setHeight("70%");
		popup.setContent(vLayout);
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
				popup.close();
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	private void showPayeeNamePopup(
			CreateAndSearchLotTableDTO createAndSearchLotTableDto) {
		final Window popup = new com.vaadin.ui.Window();

		paymentCpucodeListenerTable.init("", false, false);
		paymentCpucodeListenerTable.initPresenter(
				SHAConstants.CREATE_BATCH_PAYEE_NAME_CHANGE,
				createAndSearchLotTableDto, popup);
		paymentCpucodeListenerTable.initTable();

		fireViewEvent(SearchCreateBatchPresenter.GET_PAYEE_NAME_DETAILS,
				createAndSearchLotTableDto);

		if (null != createAndSearchLotTableDto.getPayeeNameList()
				&& !createAndSearchLotTableDto.getPayeeNameList().isEmpty()) {

			paymentCpucodeListenerTable.setTableList(createAndSearchLotTableDto
					.getPayeeNameList());
			;
		}
		VerticalLayout vLayout = new VerticalLayout();

		Button btnClose = new Button();
		btnClose.setCaption("Close");
		btnClose.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnClose.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(popup);
			}
		});

		HorizontalLayout closebutLayout = new HorizontalLayout();
		closebutLayout.addComponent(btnClose);
		closebutLayout.setSizeFull();
		vLayout.addComponents(paymentCpucodeListenerTable, closebutLayout);
		closebutLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_CENTER);

		popup.setWidth("50%");
		popup.setHeight("50%");
		popup.setContent(vLayout);
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
				popup.close();
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	public void setUpPayeeNameDetails(ViewSearchCriteriaTableDTO dto,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {

		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(updatePaymentDetailTableDTO);
		if (hashMap != null) {

			TextField txtpayeeName = (TextField) hashMap.get("payeeNameStr");
			if (txtpayeeName != null) {
				txtpayeeName.setReadOnly(false);
				if (null != dto.getChangePayeeName()) {
					txtpayeeName.setValue(dto.getChangePayeeName()
							.toUpperCase());
				} else if (null != dto.getPayeeName()) {

					txtpayeeName.setValue(dto.getPayeeName());
				}
				txtpayeeName.setReadOnly(true);
				TextField txtpayeeRelation = (TextField) hashMap.get("payeeRelationship");
				if(txtpayeeRelation != null && dto.getRelationship() != null){
					txtpayeeRelation.setValue(dto.getRelationship());
				}
				
				if(updatePaymentDetailTableDTO != null && updatePaymentDetailTableDTO.getPaymentType() != null && updatePaymentDetailTableDTO.getPaymentType().getId() != null &&
						updatePaymentDetailTableDTO.getPaymentType().getId().equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)){
					updatePaymentDetailTableDTO.setVerificationClicked(false);
					if (updatePaymentDetailTableDTO.getVerifyAccntDtlsBtn() != null) {
						updatePaymentDetailTableDTO.getVerifyAccntDtlsBtn().setStyleName(
								ValoTheme.BUTTON_DANGER);
						updatePaymentDetailTableDTO.getVerifyAccntDtlsBtn().setEnabled(true);
						updatePaymentDetailTableDTO.setVerifiedStatusFlag("N");
					}
				}
			}

		}
	}

	public void changeOfPaymentModeReasonListener(TextArea searchField,
			final Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForMedicalRemarks(searchField,
				getShortCutListenerForchangeOfPaymentModeReason(searchField));

	}

	private ShortcutListener getShortCutListenerForchangeOfPaymentModeReason(
			final TextArea txtFld) {
		ShortcutListener listener = new ShortcutListener("", KeyCodes.KEY_F8,
				null) {

			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout = new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f, Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,
						Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(4000);
				CreateAndSearchLotTableDTO mainDto = (CreateAndSearchLotTableDTO) txtFld
						.getData();
				txtArea.setData(mainDto);
				// txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				// txtArea.setSizeFull();
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("75%");
				dialog.setWidth("65%");
				txtArea.setReadOnly(false);

				txtArea.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea) event.getProperty())
								.getValue());
						CreateAndSearchLotTableDTO mainDto = (CreateAndSearchLotTableDTO) txtFld
								.getData();
						// / mainDto.setPayModeChangeReason(txtFld.getValue());
						mainDto.setPayModeChangeReason(txtFld.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);

				String strCaption = "";

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
						// TextArea txtArea = (TextArea)dialog.getData();
						// txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});

				if (getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						// TextArea txtArea = (TextArea)dialog.getData();
						// txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
			}
		};

		return listener;
	}

	public void clearExistingList() {
		if (this.tableDataList != null) {
			this.tableDataList.clear();
		}

		if (this.tableDataKeyList != null) {
			this.tableDataKeyList.clear();
		}
	}

	public Boolean getIsSearchBtnClicked() {
		return isSearchBtnClicked;
	}

	public void setIsSearchBtnClicked(Boolean isSearchBtnClicked) {
		this.isSearchBtnClicked = isSearchBtnClicked;
	}

	public ValueChangeListener payeeNameListener() {
		ValueChangeListener listener = new ValueChangeListener() {

			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField payeeName = (TextField) event.getProperty();
				if (presenterString != null
						&& ((SHAConstants.CREATE_BATCH_TYPE)
								.equalsIgnoreCase(presenterString) ||
								(SHAConstants.QUICK_SEARCH)
								.equalsIgnoreCase(presenterString))) {

					if (payeeName != null && payeeName.getValue() != null
							&& !payeeName.getValue().isEmpty()
							&& !payeeName.equals(null)) {
						CreateAndSearchLotTableDTO createLotDto = (CreateAndSearchLotTableDTO) payeeName
								.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem
								.get(createLotDto);
						TextField otherPayeeName = (TextField) hashMap
								.get("legalFirstName");

						if (otherPayeeName != null
								&& otherPayeeName.getValue() != null) {
							otherPayeeName.setValue(null);
							otherPayeeName.setNullRepresentation("");
						}
					}
				}
			}
		};
		return listener;
	}

	public void showInformation(String eMsg) {
		MessageBox.create().withCaptionCust("Information")
				.withHtmlMessage(eMsg.toString())
				.withOkButton(ButtonOption.caption("OK")).open();
	}

	public void getAlertMessage(String eMsg) {

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

	public boolean validatePagepayment(Boolean true1) {

		Boolean hasError = false;
		String eMsg = "";
		if (verificationAccountDeatilsTableObj != null) {
			Set<String> errors = verificationAccountDeatilsTableObj
					.validateCalculation();
			if (null != errors && !errors.isEmpty()) {
				for (String error : errors) {
					eMsg += error + "</br>";
					hasError = true;
					// break;
				}

			}
		}

		if (hasError) {
			// setRequired(true);
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
			return hasError;

		} else {
			// errorMessages.add("Please Add Bill Details");
		}
		return false;
	}

	public void setUpPayableAmt(String payableAt,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {

		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(updatePaymentDetailTableDTO);
		if (hashMap != null) {

			TextField txtPayableAt = (TextField) hashMap.get("payableAt");
			if (txtPayableAt != null) {
				txtPayableAt.setReadOnly(false);
				txtPayableAt.setValue(payableAt);
				txtPayableAt.setReadOnly(true);
				updatePaymentDetailTableDTO.setPayableAt(payableAt);
			}

		}
	}

	public ValueChangeListener accountNumberListener(
			CreateAndSearchLotTableDTO dto, TextField field) {
		ValueChangeListener listener = new ValueChangeListener() {

			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (field.getValue() != null) {
					dto.setBeneficiaryAcntNo(field.getValue());
					dto.setVerificationClicked(false);
					if (dto.getVerifyAccntDtlsBtn() != null) {
						// Button verifyAcntDtlButton = new
						// Button("Account Verification Details");
						dto.getVerifyAccntDtlsBtn().setStyleName(
								ValoTheme.BUTTON_DANGER);
						dto.getVerifyAccntDtlsBtn().setEnabled(true);
						dto.setVerifiedStatusFlag("N");
					}
				}
			}
		};
		return listener;

	}

	public boolean validatePageOnSave() {

		List<CreateAndSearchLotTableDTO> itemIds = (List<CreateAndSearchLotTableDTO>) this.table
				.getItemIds();

		StringBuffer eMsg = new StringBuffer();
		Boolean hasError = false;

		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : itemIds) {

			if (createAndSearchLotTableDTO.getPaymentType() != null
					&& null != createAndSearchLotTableDTO.getPaymentType()
							.getId()
					&& (ReferenceTable.PAYMENT_MODE_BANK_TRANSFER
							.equals(createAndSearchLotTableDTO.getPaymentType()
									.getId()))) {
				if (createAndSearchLotTableDTO.getCheckBoxStatus() != null
						&& ("true").equalsIgnoreCase(createAndSearchLotTableDTO
								.getCheckBoxStatus())) {

					if (!createAndSearchLotTableDTO.getVerificationClicked()) {
						// eMsg.append("Please Verified Account Details</br>");
						hasError = true;
					}
				}
			}

		}

		if (hasError) {

			Label label = new Label(
					"Please Verify Account Details Button</br>",
					ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			hasError = true;

			return !hasError;
		} else {
			return true;
		}

	}

	public void autoPopulatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO, CreateAndSearchLotTableDTO bean) {

		HashMap<String, AbstractField<?>> hashMap = tableItem.get(bean);
		if (hashMap != null) {
			TextArea emailID = (TextArea) hashMap.get("emailID");
			TextField txtPanNo = (TextField) hashMap.get("panNo");
			TextField txtAccntNo = (TextField) hashMap.get("beneficiaryAcntNo");
			TextField txtIfscCode = (TextField) hashMap.get("ifscCode");
			TextField txtBankName = (TextField) hashMap.get("bankName");
			TextField txtCity = (TextField) hashMap.get("branchCity");
			TextField txtBranch = (TextField) hashMap.get("branchName");

			if (null != emailID) {
				emailID.setReadOnly(false);
				emailID.setValue(tableDTO.getEmailId());
				emailID.setEnabled(true);
			}
			if (null != txtPanNo) {
				txtPanNo.setReadOnly(false);
				txtPanNo.setValue(tableDTO.getPanNo());
				txtPanNo.setEnabled(true);
			}
			if (null != txtAccntNo) {
				txtAccntNo.setReadOnly(false);
				txtAccntNo.setValue(tableDTO.getBankAccountNo());
				txtAccntNo.setEnabled(true);
			}
			if (null != txtIfscCode) {
				txtIfscCode.setReadOnly(false);
				txtIfscCode.setValue(tableDTO.getIfsccode());
				txtIfscCode.setEnabled(false);
			}
			if (null != txtBankName) {
				txtBankName.setReadOnly(false);
				txtBankName.setValue(tableDTO.getBankName());
				txtBankName.setEnabled(false);
			}
			if (null != txtCity) {
				txtCity.setReadOnly(false);
				txtCity.setValue(tableDTO.getBankCity());
				txtCity.setEnabled(false);
			}
			if (null != txtBranch) {
				txtBranch.setReadOnly(false);
				txtBranch.setValue(tableDTO.getBankBranch());
				txtBranch.setEnabled(false);
			}
		}
	}

	public void submitClosePopUp() {
		if (popup != null) {
			popup.close();
		}
	}

	public String getvType() {
		return vType;
	}

	public void setvType(String vType) {
		this.vType = vType;
	}

	public void clearObject() {
		SHAUtils.setClearTableItemForLot(tableItem);
		SHAUtils.setClearReferenceDataLot(compMap);
	}
	public void showLegalDetailsView(CreateAndSearchLotTableDTO tableDTO)
	{
		if(tableDTO.getLegalBillingDTO() !=null){		
			VerticalLayout vLayout = new VerticalLayout();
			legalamountTableObj = legalamountTableinstance.get();
			legalamountTableObj.initView(tableDTO.getLegalBillingDTO().getInterestApplicable(),true);
			legalamountTableObj.setTabelViewValues(tableDTO.getLegalBillingDTO());
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Information - Legal Details");
			popup.setWidth("35%");
			if(tableDTO.getLegalBillingDTO().getInterestApplicable()){
				popup.setHeight("47%");
			}else{
				popup.setHeight("26%");
			}
			popup.setContent(legalamountTableObj.getTable());
			popup.setClosable(true);
			popup.center();
			popup.setResizable(false);
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
	}
	
	public Boolean isAlertMessageNeededForNEFTDetails(CreateAndSearchLotTableDTO createAndSearchLotTableDTO){

		List<DocumentDetails> documentDetails = createRodService.getDocumentDetailsByIntimationNumber(createAndSearchLotTableDTO.getIntimationNo(),SHAConstants.NEFT_DETAILS);
		if(documentDetails !=null){
			System.out.println("NEFT Details Available in DMS");
			createAndSearchLotTableDTO.setIsNEFTDetailsAvailableinDMS(true);

		}

		if(createAndSearchLotTableDTO.getPaymentType()!=null && createAndSearchLotTableDTO.getPaymentType().getId() !=null && createAndSearchLotTableDTO.getPaymentType().getId().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)&& createAndSearchLotTableDTO.getIsNEFTDetailsAvailableinDMS()){
			return true;

		}
		else{

			return false;
		}
	}


}