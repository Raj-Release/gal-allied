package com.shaic.claim.outpatient.createbatchop;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.PagerListener;
import com.shaic.arch.table.PagerUI;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.paymentprocess.createbatch.search.PaymentCpuCodeSearchTable;
import com.shaic.reimbursement.paymentprocess.createbatch.search.SearchCreateBatchPresenter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class CreateBatchOpListenerTable extends ViewComponent {
	
	private Table table;
	private String presenterString;
	//private Map<String, Object> referenceData;
	private Map<CreateBatchOpTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<CreateBatchOpTableDTO, HashMap<String, AbstractField<?>>>();
	private PagerUI pageUI;
	private HorizontalLayout fLayout;
	private Searchable searchable;
	private Label totalRocordsTxt;
	Integer intNoOfDays = 0;
	Double intPenalInterestAmnt = 0d;
	private BeanItemContainer<SelectValue> paymentMode;
	private BeanItemContainer<SelectValue> cpuCode;
	
	@Inject
	private CitySearchCriteriaViewImpl citySearchCriteriaWindow;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	//private CreateBatchOpTableDTO createandSearchLotDto;
	
	//private int i = 0;
	
	@Inject
	private PaymentCpuCodeSearchTable paymentCpucodeListenerTable;
	
	@Inject
	private MasterService masterService;
	
	//private FormLayout fLayout;
	
	private Boolean isSearchBtnClicked = false;
	
	/***
	 * Bean object fetch from db
	 */
	BeanItemContainer<CreateBatchOpTableDTO> data = new BeanItemContainer<CreateBatchOpTableDTO>(CreateBatchOpTableDTO.class);
	public HashMap<Long,Component> compMap = null;
	public List<CreateBatchOpTableDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	private VerticalLayout tableLayout = null;
	
	public void init(String presenterString,Boolean showPagerFlag) {
		
		this.presenterString = presenterString;
		
		initTable();
		pageUI = new PagerUI();
		
		addListener();		
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
		tableLayout = new VerticalLayout();
		fLayout = new HorizontalLayout();
		fLayout.setWidth("100%");
		fLayout.setHeight("25px");
		totalRocordsTxt = new Label();
		totalRocordsTxt.setWidth(null);
		
		// VerticalLayout layout = new VerticalLayout();
		 if(showPagerFlag){
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
		 
		setCompositionRoot(tableLayout);
	}
	
	private void addListener() {
		
	}
	
	public void initPresenterString(String presenterString)
	{
		this.presenterString  = presenterString;
	}
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		//this.referenceData = referenceData;
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table();
		table.setContainerDataSource(data);
		//table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		
		if(null != presenterString && SHAConstants.QUICK_SEARCH.equalsIgnoreCase(presenterString)){
			
			table.setHeight("320px");
		}
		else
		{
			table.setHeight("580px");
		}
		
		
		generatecolumns();
		
	/*	if((SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString) ||
				(SHAConstants.QUICK_SEARCH).equalsIgnoreCase(presenterString))
		{*/
			table.removeAllItems();
			table.setVisibleColumns(new Object[] { "serialNo","claimNo","policyNo","product","paymentStatus","paymentType",
					"ifscCode","beneficiaryAcntNo","bankName","branchName","typeOfClaim" });
			table.setColumnHeader("serialNo", "S.No");
				//table.setColumnHeader("docVerifiedValue", "Doc Verified");
			table.setColumnHeader("claimNo", "Claim No");
			table.setColumnHeader("policyNo", "Policy No");
			table.setColumnHeader("product", "Product");
			table.setColumnHeader("paymentStatus", "Payment</br>Status");
			table.setColumnHeader("paymentType", "Payment Type");
			table.setColumnHeader("ifscCode", "IFSC Code");
			table.setColumnHeader("beneficiaryAcntNo", "beneficiary Account No");
			table.setColumnHeader("branchName", "Branch Name");
			table.setColumnHeader("typeOfClaim", "Type Of Claim");
		/*	table.setVisibleColumns(new Object[] { "serialNo","chkSelect", "viewLinkedPolicy" ,"viewdetails","intimationNo","claimNo","policyNo","rodNo","paymentStatus","product","cpuCode","paymentCpucodeTextValue","paymentCpuCodeSearch","emailID","paymentType",
					"payModeChangeReason","ifscCode","Search","beneficiaryAcntNo","bankName","branchName","typeOfClaim","approvedAmt","payeeNameStr","payeeNameSearch","legalFirstName","reasonForChange","gmcProposerName","gmcEmployeeName","payableAt","payableAtSearch","panNo","providerCode","dbSideRemark" });
			table.setColumnHeader("serialNo", "S.No");
			//table.setColumnHeader("docVerifiedValue", "Doc Verified");
			table.setColumnHeader("chkSelect", "");
			table.setColumnHeader("viewLinkedPolicy", "");
			table.setColumnHeader("viewdetails", "");
			table.setColumnHeader("Search", "Search</br>IFSC");
			table.setColumnHeader("intimationNo", "Intimation No");
			table.setColumnHeader("claimNo", "Claim No");
			table.setColumnHeader("policyNo", "Policy No");
			table.setColumnHeader("rodNo", "ROD No");
			table.setColumnHeader("paymentStatus", "Payment</br>Status");
			table.setColumnHeader("product", "Product");
			table.setColumnHeader("cpuCode", "Cpu Code");
			table.setColumnHeader("paymentCpucodeTextValue", "Payment</br>Cpu Code");
			table.setColumnHeader("paymentCpuCodeSearch", "Search</br>Payment</br>Cpu");
			table.setColumnHeader("reasonForChange", "Reason of Change");
			table.setColumnHeader("ifscCode", "IFSC Code");
			table.setColumnHeader("beneficiaryAcntNo", "beneficiary Account No");
			table.setColumnHeader("branchName", "Branch Name");
			table.setColumnHeader("typeOfClaim", "Type Of Claim");
			table.setColumnHeader("lotNo", "Lot No");
			table.setColumnHeader("approvedAmt", "Approved</br>Amnt");
			table.setColumnHeader("payeeNameStr", "Payee Name");
			table.setColumnHeader("payeeNameSearch", "Search</br>Payee</br>Name");
			table.setColumnHeader("gmcProposerName", "Proposer Name");
			table.setColumnHeader("gmcEmployeeName", "Employee Name");
			table.setColumnHeader("payableAt", "Payable City");
			table.setColumnHeader("payableAtSearch", "Search</br>Payable</br>City");
			table.setColumnHeader("panNo", "PAN No");
			table.setColumnHeader("paymentType", "Payment Type");
			table.setColumnHeader("payModeChangeReason", "Reason for Change");
			table.setColumnHeader("providerCode", "Provider Code");
			table.setColumnHeader("emailID","Email ID");
			table.setColumnHeader("legalFirstName", "Other Payee Name");
			//table.setColumnHeader("zonalMailId", "Zonal Email ID");
			table.setColumnHeader("reconsiderationFlag", "Reconsideration</br>Flag(Y/N)");
			table.setColumnHeader("faApprovedAmnt", "Approved Amnt");
			table.setColumnHeader("lastAckDateValue", "Last<br>Acknowledegement</br>Received Date");
			table.setColumnHeader("faApprovedDateValue", "Date Of<br>Financial</br>Approval");
			table.setColumnHeader("numberofdays", "No Of Days <br>for<br>processing");
			table.setColumnHeader("irdaTAT", "IRDA TAT");
			table.setColumnHeader("noofdaysexceeding", "No Of Days<br>Int Payable</br>");
			table.setColumnHeader("intrestAmount", "Penal Interest<br> Amount");
			table.setColumnHeader("penalTotalAmnt", "Total amount");
	 		table.setColumnHeader("remarks", "Interest Remarks");
	 		table.setColumnHeader("dbSideRemark", "Remarks");*/
	 		 
//		}
		/*else if ((SHAConstants.SEARCH_LOT).equalsIgnoreCase(presenterString))
		{
			table.removeAllItems();
			table.setVisibleColumns(new Object[]{"serialNo","chkSelect", "viewLinkedPolicy" ,"viewdetails","intimationNo","claimNo","policyNo","rodNo","paymentStatus","product","cpuCode","paymentCpucodeTextValue","emailID","paymentType",
					"payModeChangeReason","ifscCode","Search","beneficiaryAcntNo","bankName","branchName","typeOfClaim","approvedAmt","payeeNameStr","legalFirstName","reasonForChange","gmcProposerName","gmcEmployeeName","payableAt","panNo","providerCode",
					"accountBatchNo","lotNo","serviceTax","totalAmnt","tdsAmt","netAmnt","tdsPercentage","refNo","paymentReqDt","paymentReqUID"});
			
			table.setColumnHeader("serialNo", "S.No");
			//table.setColumnHeader("docVerifiedValue", "Doc Verified");
			table.setColumnHeader("chkSelect", "");
			table.setColumnHeader("viewLinkedPolicy", "");
			table.setColumnHeader("viewdetails", "");
			table.setColumnHeader("Search", "Search</br>IFSC");
			table.setColumnHeader("intimationNo", "Intimation No");
			table.setColumnHeader("claimNo", "Claim No");
			table.setColumnHeader("policyNo", "Policy No");
			table.setColumnHeader("rodNo", "ROD No");
			table.setColumnHeader("paymentStatus", "Payment</br>Status");
			table.setColumnHeader("product", "Product");
			table.setColumnHeader("cpuCode", "Cpu Code");
			table.setColumnHeader("paymentCpucodeTextValue", "Payment Cpu</br>Code");
			table.setColumnHeader("reasonForChange", "Reason for Change");
			table.setColumnHeader("ifscCode", "IFSC Code");
			table.setColumnHeader("beneficiaryAcntNo", "beneficiary Acnt No");
			table.setColumnHeader("branchName", "Branch Name");
			table.setColumnHeader("typeOfClaim", "Type Of Claim");
			table.setColumnHeader("approvedAmt", "Approved</br>Amnt");
			table.setColumnHeader("payeeNameStr", "Payee Name");
			table.setColumnHeader("gmcProposerName", "Proposer Name");
			table.setColumnHeader("gmcEmployeeName", "Employee Name");
			table.setColumnHeader("payableAt", "Payable City");
			table.setColumnHeader("panNo", "PAN No");
			table.setColumnHeader("providerCode", "Provider Code");
			table.setColumnHeader("emailID","Email ID");
			table.setColumnHeader("paymentType", "Payment Type");
			table.setColumnHeader("payModeChangeReason", "Reason for Change");
			//table.setColumnHeader("zonalMailId", "Zonal Email ID");
			table.setColumnHeader("accountBatchNo", "Account Batch No");
			table.setColumnHeader("legalFirstName", "Other Payee Name");
			
			table.setColumnHeader("lotNo", "Lot Number");
			table.setColumnHeader("serviceTax", "Service Tax");
			table.setColumnHeader("totalAmnt", "Total Amount");
			table.setColumnHeader("tdsAmt", "Tds Amount");
			table.setColumnHeader("netAmnt", "Net Amount");
			table.setColumnHeader("tdsPercentage", "Tds Percentage");
			table.setColumnHeader("refNo", "Reference No");
			table.setColumnHeader("paymentReqDt", "Payment Request Date");
	 		table.setColumnHeader("paymentReqUID", "Payment Req UID");
		}*/
		table.setEditable(true);
		//generateColumn();
		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		//generateSlNo();
 		table.setFooterVisible(false);
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	}

	private void generateColumn() {
		table.removeGeneratedColumn("Search");
		table.addGeneratedColumn("Search", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				//final Button deleteButton = new Button("Delete");
				final Button searchButton = new Button();
				//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				searchButton.setStyleName(ValoTheme.BUTTON_LINK);
				searchButton.setIcon(new ThemeResource("images/search.png"));
				//searchButton.setWidth("-1px");
				//searchButton.setHeight("-10px");
				searchButton.setData(itemId);
				searchButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO) currentItemId;
						showIfscPopup(updatePaymentDetailTableDTO);
					}
				});

				return searchButton;
			}
		});
	}
	
	public void manageListeners() {

		
	}
		
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			CreateBatchOpTableDTO initiateDTO = (CreateBatchOpTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(initiateDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(initiateDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(initiateDTO);
			}
			
		/*	if("intimationNo".equals(propertyId)) {
				
				TextField field = new TextField();
				field.setWidth("175px");
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("intimationNo", field);

				return field;
			} */
			/*else if ("chkSelect".equals(propertyId)) {
				CheckBox chkBox = new CheckBox("");
				//field.setWidth("50px");
				//field.setNullRepresentation("");
				chkBox.setEnabled(true);
				//field.setMaxLength(6);
				chkBox.setData(initiateDTO);				
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				//validator.extend(field);				
				tableRow.put("chkSelect", chkBox);
				addListener(chkBox);
				compMap.put(initiateDTO.getClaimPaymentKey(), chkBox);

				return chkBox;
			}*/
			 if ("claimNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("claimNo", field);

				return field;
			}
			else if ("policyNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("160px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("policyNo", field);
			
				return field;
			}
			/*else if ("rodNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("190px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("rodNo", field);
				
				return field;
			}*/
			else if("paymentStatus".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("75px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("paymentStatus", field);
				return field;
			}
			else if("product".equals(propertyId)) {
				TextField field = new TextField();
			//	field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("product", field);
				return field;
			}
		/*	else if("cpuCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("cpuCode", field);
				return field;
			}*/
			/*else if("cpuName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("cpuName", field);
				return field;
			}*/
	/*		else if("paymentCpucodeTextValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("110px");
				
				field.setEnabled(Boolean.FALSE);
				//field.setMaxLength(6);
				//field.setContainerDataSource(cpuCode);
				field.setData(initiateDTO);
				field.setValue(initiateDTO.getPaymentCpucodeTextValue());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.addValueChangeListener(paymentCpuListener());
				tableRow.put("paymentCpucodeTextValue", field);
				return field;
			}*/
			/*else if("paymentCpuName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setMaxLength(6);
				field.setEnabled(Boolean.FALSE);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("paymentCpuName", field);
				return field;
			}*//*else if("reasonForChange".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setWidth("120px");
				field.setHeight("40px");
				field.setNullRepresentation("");
				if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}
				//field.setMaxLength(6);
				field.setEnabled(Boolean.TRUE);
				field.setData(initiateDTO);
				changeOfReasonListener(field, null);
				field.setMaxLength(4000);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("reasonForChange", field);
				return field;
			}*/
			else if("paymentType".equals(propertyId)) {
				ComboBox field = new ComboBox();
				field.setWidth("120px");
				//field.setMaxLength(6);
				field.setContainerDataSource(paymentMode);
				if(initiateDTO.getPaymentTypeValue()!=null){
				if(initiateDTO.getPaymentTypeValue().equalsIgnoreCase("NEFT")){
					SelectValue idByIndex = paymentMode.getIdByIndex(0);
					field.setValue(idByIndex);
					initiateDTO.setPaymentType(idByIndex);
					initiateDTO.setTempPaymentType(idByIndex);
				}else{
					SelectValue idByIndex = paymentMode.getIdByIndex(1);
					field.setValue(idByIndex);
					initiateDTO.setPaymentType(idByIndex);
					initiateDTO.setTempPaymentType(idByIndex);
				}}
				/*if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}*/
				field.setEnabled(Boolean.TRUE);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.addValueChangeListener(ifscListener());
				field.addValueChangeListener(paymentTypeListener(field, initiateDTO));
				tableRow.put("paymentType", field);
				return field;
			}
			else if("ifscCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("ifscCode", field);
				return field;
			}
			else if("beneficiaryAcntNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				/*if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}*/
				if (presenterString!=null && (initiateDTO.getPaymentType().getId()).equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
					field.setEnabled(false);
				}
				
				tableRow.put("beneficiaryAcntNo", field);
				return field;
			}
			else if("bankName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				/*if (presenterString!=null && (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}*/
				field.setEnabled(Boolean.FALSE);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("bankName", field);
				return field;
			}
			else if("branchName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("branchName", field);
				return field;
			}
			else if("typeOfClaim".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("typeOfClaim", field);
				return field;
			}
			/*else if("lotNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("lotNo", field);
				return field;
			}
			else if("approvedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("approvedAmt", field);
				return field;
			}
			else if("payeeNameStr".equals(propertyId)) {
				TextField field = new TextField();
				if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}
				
				if(initiateDTO.getDocumentReceivedFrom()!=null && initiateDTO.getDocumentReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
					if(initiateDTO.getHospitalPayableAt() != null){
						
						field.setData(initiateDTO);
						initiateDTO.setPayeeNameStr(initiateDTO.getHospitalPayableAt());
						field.setValue(initiateDTO.getPayeeNameStr());
					}else if(initiateDTO.getHospitalPayableName() != null){
						
						field.setData(initiateDTO);
						initiateDTO.setPayeeNameStr(initiateDTO.getHospitalPayableName());
						field.setValue(initiateDTO.getHospitalPayableName());
					}else{
						field.setData(initiateDTO);
						field.setValue(initiateDTO.getPayeeNameStr());
					}
				}else{
					field.setData(initiateDTO);
					field.setValue(initiateDTO.getPayeeNameStr());
				}
				//field.setMaxLength(6);
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.addValueChangeListener(payeeNameListener());
				field.setWidth("140px");
				tableRow.put("payeeNameStr", field);
				return field;
			}
			else if("legalFirstName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}
				//field.setMaxLength(6);
				field.setEnabled(Boolean.TRUE);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.addValueChangeListener(legalListener());
				tableRow.put("legalFirstName", field);
				return field;
			}
			else if("payModeChangeReason".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setWidth("120px");
				field.setHeight("40px");
				field.setNullRepresentation("");
				if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}
				field.setEnabled(Boolean.TRUE);
				field.setMaxLength(4000);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				changeOfPaymentModeReasonListener(field, null);
				tableRow.put("payModeChangeReason", field);
				return field;
			}
			else if("gmcProposerName".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("gmcProposerName", field);
				return field;
			}
			else if("gmcEmployeeName".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("gmcEmployeeName", field);
				return field;
			}
			else if("payableAt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				if (presenterString!=null && (initiateDTO.getPaymentType().getId()).equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
					field.setEnabled(true);
				if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}}else{
					field.setEnabled(Boolean.FALSE);
				}
				
				tableRow.put("payableAt", field);
				field.setEnabled(false);
				return field;
			}
			else if("panNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}
				
				if (presenterString!=null && (initiateDTO.getPaymentType().getId()).equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
					field.setEnabled(Boolean.TRUE);
				}
				else
				{
					field.setEnabled(Boolean.FALSE);
				}
		
				tableRow.put("panNo", field);
				return field;
			}
			else if("providerCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("providerCode", field);
				return field;
			}
			
			else if("emailID".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setHeight("40px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					
				}else{
					field.setEnabled(Boolean.FALSE);
				}
				tableRow.put("emailID", field);
				return field;
			}
			else if("accountBatchNo".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("1px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("accountBatchNo", field);
				return field;
			}
			else if("serviceTax".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("totalAmnt", field);
				return field;
			}
			else if("totalAmnt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("totalAmnt", field);
				return field;
			}else if("tdsAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("tdsAmt", field);
				return field;
			}else if("netAmnt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("netAmnt", field);
				return field;
			}else if("tdsPercentage".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("tdsPercentage", field);
				return field;
			}else if("refNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("refNo", field);
				return field;
			}
			else if("paymentReqDt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("paymentReqDt", field);
				return field;
			}
			else if("paymentReqUID".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("paymentReqUID", field);
				return field;
			}
			else if("reconsiderationFlag".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("reconsiderationFlag", field);
				return field;
			}
			else if("faApprovedAmnt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("faApprovedAmnt", field);
				return field;
			}else if("lastAckDateValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("lastAckDateValue", field);
				return field;
			}else if("faApprovedDateValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("faApprovedDateValue", field);
				return field;
			}else if("numberofdays".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("numberofdays", field);
				return field;
			}else if("intrestAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				String legalFlag =  null != initiateDTO.getClaimDto().getLegalFlag() ? initiateDTO.getClaimDto().getLegalFlag() : null;
				
				if(initiateDTO.getReconsiderationFlag()!= null && initiateDTO.getReconsiderationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) 
				{
						if((initiateDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID) || (initiateDTO.getDocReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL))
								|| (null != legalFlag &&(legalFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) || 
										legalFlag.equalsIgnoreCase(SHAConstants.N_FLAG))))
						{
							field.setEnabled(false);
						} 
						else
						{
							field.setEnabled(true);
						}
				}
				else
				{
					field.setEnabled(false);
				}
				field.addBlurListener(getInterestAmnt(field));
				field.addBlurListener(totalAmountListener(field));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("intrestAmount", field);
				return field;
			}
			else if("irdaTAT".equals(propertyId)) {
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
				
				if(null != initiateDTO.getIsFvrOrInvesInitiated() && initiateDTO.getIsFvrOrInvesInitiated()){
					
					field.setValue(obj.getIRDAForFvrAndInvs());
				}
				else
				{
					field.setValue(BPMClientContext.getIRDATATDays());	
				}							
				
				tableRow.put("irdaTAT", field);				
				return field;
			}	
			
			else if("noofdaysexceeding".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);

				if(null != initiateDTO.getNumberofdays())
				{
					if(initiateDTO.getNoofdaysexceeding() >= -3)
					{	
						if(ReferenceTable.CORRECTION_PAYMENT_STATUS_ID.equals(initiateDTO.getPaymentStatusKey()) || (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equals(initiateDTO.getDocReceivedFrom()))
					{
						field.setEnabled(false);
					} 
							else
							{
								field.setEnabled(true);
							}
					}
					else
					{					
						field.setEnabled(false);							
						
					}
				}
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				//noOfDaysExceedingListener(field);
				field.addBlurListener(noOfDaysExceedingListener(field));
				tableRow.put("noofdaysexceeding", field);
				return field;
			}
			
			else if("penalTotalAmnt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("penalTotalAmnt", field);
				return field;
			}
			else if("remarks".equals(propertyId)) {
			
				TextField field = new TextField();					
				String legalFlag =  null != initiateDTO.getClaimDto().getLegalFlag() ? initiateDTO.getClaimDto().getLegalFlag() : null;
				
				if(null != legalFlag &&(legalFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) || 
								legalFlag.equalsIgnoreCase(SHAConstants.N_FLAG)))
				{
					field.setEnabled(false);
				} 
				else
				{
					field.setEnabled(true);
				}
			//	field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setDescription(field.getValue());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("remarks", field);
				return field;
			}
			else if("claimCount".equals(propertyId)) {
				TextField field = new TextField();
			//	field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("claimCount", field);
				//final TextField txt = (TextField) tableRow.get("itemNo");
				return field;
			}
*/			else if("serialNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("serialNo", field);
				return field;
			}/*else if("docVerifiedValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				
				tableRow.put("docVerifiedValue", field);
				
				return field;
			}*/
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				
				if("dbSideRemark".equals(propertyId)){
					CreateBatchOpTableDTO dto = (CreateBatchOpTableDTO)itemId;
					if (field instanceof TextField){
						TextField txtField = ((TextField) field);
						txtField.setValue(dto.getDbSideRemark());
						txtField.setDescription(dto.getDbSideRemark());
						//dto.setRemarks(dto.getDbSideRemark());
					}
				}

				if (field instanceof TextField){
					field.setWidth("100%");
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setReadOnly(true);
					
					((TextField) field).setNullRepresentation("");
					
				}
				
				return field;
			}
		}
	}
	
	/*public void generateSlNo()
	{
		
		Collection<CreateBatchOpTableDTO> itemIds = (Collection<CreateBatchOpTableDTO>) table.getItemIds();
		
		 int i = 0;
		 for (CreateBatchOpTableDTO billEntryDetailsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("serialNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}*/
	
	
	private void generatecolumns(){
		
		//	compList = new ArrayList<Component>();
			compMap = new HashMap<Long, Component>();
			
			table.removeGeneratedColumn("chkSelect");
			table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
					  CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO) itemId;
						CheckBox chkBox = new CheckBox("");
						
						 if(null != tableDataList && !tableDataList.isEmpty())
						  {
							  for (CreateBatchOpTableDTO dto : tableDataList) {
								  if(dto.getClaimPaymentKey().equals(tableDTO.getClaimPaymentKey()))
								  {
									   if(null != dto.getChkSelect())
									  {
										  chkBox.setValue(dto.getChkSelect());
									  }
									   else if(("true").equalsIgnoreCase(dto.getCheckBoxStatus()))
									   {
										   chkBox.setValue(true);
									   }
									   else if(("false").equalsIgnoreCase(dto.getCheckBoxStatus()))
									   {
										   chkBox.setValue(false);
									   }
									   //compMap.put(dto.getClaimPaymentKey(), chkBox);
								  }
								
							}
						  }
						
						chkBox.setData(tableDTO);
						addListener(chkBox);
						if(compMap==null){
							compMap = new HashMap<Long, Component>();
						}
						compMap.put(tableDTO.getClaimPaymentKey(), chkBox);
					//	compList.add(chkBox);
						//addListener(chkBox);
					return chkBox;
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
						
					final CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO)itemId;
					
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
			table.addGeneratedColumn("viewLinkedPolicy", new Table.ColumnGenerator() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
					final CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO)itemId;
					
					Button button = new Button("View Linked Policy");
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
			    	button.setEnabled(false);
			    	if (tableDTO.getPaymentPolicyType() != null && (tableDTO.getPaymentPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_PARENT)  ||
			    			tableDTO.getPaymentPolicyType().equalsIgnoreCase(SHAConstants.GMC_POLICY_TYPE_STANDALONE_PARENT))){
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
			

			/*table.removeGeneratedColumn("editpaymentdetails");
			table.addGeneratedColumn("editpaymentdetails", new Table.ColumnGenerator() {
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
						
					
					Button button = new Button("Edit Payment Details");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
						//To implement claims dms functionality.
							showEditPaymentDetailsView((CreateBatchOpTableDTO)itemId);
							
						}
					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
				}
			});*/


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
						
												
							fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_SHOW_DETAILS,(CreateBatchOpTableDTO)itemId);		    	
					    				
						}
					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
				}
			});
			
	/*		table.removeGeneratedColumn("serialNo");
			
			table.addGeneratedColumn("serialNo", new Table.ColumnGenerator() {
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					
					List<CreateBatchOpTableDTO> serialNoList = (List<CreateBatchOpTableDTO>)source.getContainerDataSource().getItemIds();
					
					CreateBatchOpTableDTO dto = (CreateBatchOpTableDTO)itemId;
					Integer sNo = serialNoList.indexOf(dto)+1;
							return sNo;		
					
					
					
					
				}
			});*/
			
			

		
			
		
			
			
			table.removeGeneratedColumn("remarks");
			table.addGeneratedColumn("remarks", new Table.ColumnGenerator() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
					final CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO)itemId;	
					
					
					com.vaadin.v7.ui.TextField txtRemarks = new com.vaadin.v7.ui.TextField();
					
					txtRemarks.addValueChangeListener(new ValueChangeListener() {
						
						
						
						@Override
						public void valueChange(Property.ValueChangeEvent event) {	
						
							String value = (String)event.getProperty().getValue();
							
							
							if(null != value && !(value.equals("")))
							{
								
								tableDTO.setRemarks(value);
							}
							else
							{
								tableDTO.setRemarks("");
							}
							
						}
					});
				
					//txtRemarks.addStyleName(ValoTheme.BUTTON_BORDERLESS);
					txtRemarks.setWidth("150px");
					txtRemarks.addStyleName(ValoTheme.BUTTON_LINK);
					if(null != tableDTO.getRemarks())
						txtRemarks.setValue(tableDTO.getRemarks());
					return txtRemarks;
				}
			});
			

			table.removeGeneratedColumn("Search");
			table.addGeneratedColumn("Search", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					//final Button deleteButton = new Button("Delete");
					final Button searchButton = new Button();
				/*	if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
						//searchButton.setEnabled(initiateDTO.getIsInsured());
					}else{
						searchButton.setEnabled(Boolean.FALSE);
					}*/
					//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
					searchButton.setStyleName(ValoTheme.BUTTON_LINK);
					searchButton.setIcon(new ThemeResource("images/search.png"));
					//searchButton.setWidth("-1px");
					//searchButton.setHeight("-10px");
					CreateBatchOpTableDTO initiateDTO = (CreateBatchOpTableDTO) itemId;
					if(initiateDTO.getIsInsured() && initiateDTO.getPaymentType().getValue().equals("NEFT")){
						searchButton.setEnabled(true);
					}else if(initiateDTO.getIsInsured() && initiateDTO.getPaymentType().getValue().equals("CHEQUE / DD")){
						searchButton.setEnabled(false);
					}else{
						searchButton.setEnabled(false);
					}
					searchButton.setData(itemId);
					searchButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;

						public void buttonClick(ClickEvent event) {
							Object currentItemId = event.getButton().getData();
							CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO) currentItemId;
							showIfscPopup(updatePaymentDetailTableDTO);
						}
					});
					initiateDTO.setSearchButton(searchButton);
					return searchButton;
				}
			});
		
			
			table.removeGeneratedColumn("paymentCpuCodeSearch");
			table.addGeneratedColumn("paymentCpuCodeSearch", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					final Button searchButton = new Button();
					searchButton.setStyleName(ValoTheme.BUTTON_LINK);
					searchButton.setIcon(new ThemeResource("images/search.png"));
					searchButton.setWidth("-6px");
					searchButton.setHeight("-8px");
					CreateBatchOpTableDTO initiateDTO = (CreateBatchOpTableDTO) itemId;				
					searchButton.setData(itemId);
					searchButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;

						public void buttonClick(ClickEvent event) {
							Object currentItemId = event.getButton().getData();
							CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO) currentItemId;
							showPaymentCpuCodePopup(updatePaymentDetailTableDTO);
						}
					});
					return searchButton;
				}
			});
			
			table.removeGeneratedColumn("payableAtSearch");
			table.addGeneratedColumn("payableAtSearch", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					final Button searchButton = new Button();
					searchButton.setStyleName(ValoTheme.BUTTON_LINK);
					searchButton.setIcon(new ThemeResource("images/search.png"));
					searchButton.setWidth("-6px");
					searchButton.setHeight("-8px");
					final CreateBatchOpTableDTO initiateDTO = (CreateBatchOpTableDTO) itemId;				
					searchButton.setData(itemId);
					initiateDTO.setPayableAtButton(searchButton);
					if (initiateDTO.getPaymentType() != null && (initiateDTO.getPaymentType().getId()).equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
						searchButton.setEnabled(true);
					}else{
						searchButton.setEnabled(false);
					}
					searchButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;

						public void buttonClick(ClickEvent event) {

							Window popup = new com.vaadin.ui.Window();
							citySearchCriteriaWindow.setPresenterString(SHAConstants.OP_PROCESS_SCREEN);
//							citySearchCriteriaWindow.initView(popup,initiateDTO);
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
							searchButton.setEnabled(true);
						
						}
					});
					return searchButton;
				}
			});
			
			table.removeGeneratedColumn("payeeNameSearch");
			table.addGeneratedColumn("payeeNameSearch", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					final Button searchButton = new Button();
					searchButton.setStyleName(ValoTheme.BUTTON_LINK);
					searchButton.setIcon(new ThemeResource("images/search.png"));
					searchButton.setWidth("-1px");
					searchButton.setHeight("-10px");
					CreateBatchOpTableDTO initiateDTO = (CreateBatchOpTableDTO) itemId;				
					searchButton.setData(itemId);
					searchButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;

						public void buttonClick(ClickEvent event) {
							Object currentItemId = event.getButton().getData();
							CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO) currentItemId;
							showPayeeNamePopup(updatePaymentDetailTableDTO);
						}
					});
					return searchButton;
				}
			});
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
					//String[] values = null;
					Number number = null ;
					if(null != component)
					{
						value = component.getValue();
						/**
						 * The below parse done for avoid Number format Exception
						 */
						
						try {
							 number = NumberFormat.getNumberInstance(java.util.Locale.US).parse(value);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					}
					
					if(null != number)
					{
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
					
					
					//String value = (String)event.getComponent().get
					TextField component = (TextField) event.getComponent();
					/*String value = null;
					if(null != component)
					{
						value = component.getValue();
						
					}*/
					
				
					CreateBatchOpTableDTO tableDTO = new CreateBatchOpTableDTO();
					 TextField txtField = (TextField)component;
					 tableDTO = (CreateBatchOpTableDTO) txtField.getData();				
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					//TextField faApproveAmnt = (TextField) hashMap.get("faApprovedAmnt");
					//TextField penalInterestAmt = (TextField) hashMap.get("intrestAmount");
					TextField penalTotalAmnt = (TextField) hashMap.get("penalTotalAmnt");
					Double penalTotalamnt = 0d;
					
					Double faApproveAmount = null != tableDTO.getFaApprovedAmnt() ? tableDTO.getFaApprovedAmnt() : 0d;
					Double interestAmount = null != tableDTO.getIntrestAmount() ? tableDTO.getIntrestAmount() : 0d;
					
						penalTotalamnt = faApproveAmount+interestAmount;
						
						double decimalOfApproveAmnt =  penalTotalamnt*10 % 10;
						int converttoIntOfApproveAmnt = (int) decimalOfApproveAmnt;	
						
						if(converttoIntOfApproveAmnt >= 5)
						{
							tableDTO.setPenalTotalAmnt(Math.ceil(penalTotalamnt));
						}
						
						else
						{
							tableDTO.setPenalTotalAmnt(Math.floor(penalTotalamnt));
						}
						
						if(null != penalTotalAmnt && null != tableDTO.getPenalTotalAmnt())
						{
							penalTotalAmnt.setValue(String.valueOf(tableDTO.getPenalTotalAmnt()));
						}															
					
				}
			};
			return listener;
			
		}
	
	
	 public BlurListener noOfDaysExceedingListener(final TextField txtNoOfDaysExceeding) {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {	
					
					
					//String value = (String)event.getComponent().get
					TextField component = (TextField) event.getComponent();
					String value = null;
					if(null != component)
					{
						value = component.getValue();
					}
					
				//	CreateBatchOpTableDTO	tableDTO =  (CreateBatchOpTableDTO) ((TextField)event.getProperty()).getData();
					CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO) txtNoOfDaysExceeding.getData();
					//CreateBatchOpTableDTO tableDTO = new CreateBatchOpTableDTO();
					 TextField txtField = (TextField)component;
					 tableDTO = (CreateBatchOpTableDTO) txtField.getData();				
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					TextField penalInterestAmt = (TextField) hashMap.get("intrestAmount");
					TextField penalTotalAmnt = (TextField) hashMap.get("penalTotalAmnt");
					
					Integer diffNoOfDays = 0;
					//Double intNoOfDays = 0d;
					
					if(null != value && !(value.equals("")))
					{								
						 								
						intNoOfDays = Integer.valueOf(value);
						if(intNoOfDays >=-3)
						{
						
						 diffNoOfDays = intNoOfDays - tableDTO.getNoOfDaysExceedingforCalculation();
						
						if(diffNoOfDays<=3)
						{								
							tableDTO.setNoOfDiffDays(diffNoOfDays);
							tableDTO.setNoofdaysexceeding(intNoOfDays);
							
						}
						else
						{
							tableDTO.setNoofdaysexceeding(intNoOfDays);
							tableDTO.setNoOfDiffDays(diffNoOfDays);
							fireViewEvent(SearchCreateBatchPresenter.VALIDATION,null);				
												
						}
						Double  faApprovedAmnt = null != tableDTO.getFaApprovedAmnt() ? tableDTO.getFaApprovedAmnt() : 0d;
						Double interestRate = 0d;
						
						if((tableDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID) ||
								tableDTO.getDocReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL))
						{
							interestRate = 0d;
						}
						else
						{
						interestRate = null != tableDTO.getIntrestRate() ? tableDTO.getIntrestRate() : 0d;
						}
						Double noOfExceedingDays1 = null != tableDTO.getNoofdaysexceeding() ?  tableDTO.getNoofdaysexceeding() : 0d;				
					
						
						Double noOfExceedingDays = Math.abs(noOfExceedingDays1/365);					
					
						
						Double penalInterestAmount = faApprovedAmnt*(interestRate/100)*(noOfExceedingDays);
						
						Double penalTotalamnt = 0d;
						Double approvedAmnt = 0d;
						if(null != penalInterestAmount)
						{
							
							double decimalNo =  penalInterestAmount*10 % 10;
							int converttoInt = (int) decimalNo;
							
							if(converttoInt >= 5)
							{
								approvedAmnt =  Math.ceil(penalInterestAmount);
							}
							else
							{
								approvedAmnt =Math.floor(penalInterestAmount);	
							}
							
							if(null != penalInterestAmt && null != approvedAmnt)
							{
								penalInterestAmt.setValue(String.valueOf(Math.abs(approvedAmnt)));
							}
							tableDTO.setIntrestAmount(Math.abs(approvedAmnt));
							tableDTO.setInterestAmntForCalculation(Math.abs(approvedAmnt));
						//	tableDTO.setPenalInterestAmntForCalculation(Math.abs(approvedAmnt));
							
							
						}
						
						 penalTotalamnt = faApprovedAmnt+tableDTO.getIntrestAmount();
						
						double decimalOfApproveAmnt =  penalTotalamnt*10 % 10;
						int converttoIntOfApproveAmnt = (int) decimalOfApproveAmnt;	
						
						if(converttoIntOfApproveAmnt >= 5)
						{
							tableDTO.setPenalTotalAmnt(Math.ceil(penalTotalamnt));
						}
						
						else
						{
							tableDTO.setPenalTotalAmnt(Math.floor(penalTotalamnt));
						}
						
						if(null != penalTotalAmnt && null != tableDTO.getPenalTotalAmnt())
						{
							penalTotalAmnt.setValue(String.valueOf(tableDTO.getPenalTotalAmnt()));
						}
						
							
						}
						
						else
						{
							fireViewEvent(SearchCreateBatchPresenter.NO_OF_EXCEEDING_DAYS_VALIDATION,null);
							if(null != tableDTO.getNoofdaysexceeding())
								txtNoOfDaysExceeding.setValue(String.valueOf(tableDTO.getNoofdaysexceeding()));
						}
						
					}
				}
			};
			return listener;
			
		}
	
	
	private void addListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					
					  
					boolean value = (Boolean) event.getProperty().getValue();
					CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO)chkBox.getData();
					
					if(value)
					{
						tableDTO.setCheckBoxStatus("true");
					}else
					{
						tableDTO.setCheckBoxStatus("false");
						//tableDTO.setChkSelect(false);
					}
					/**
					 * Added for issue#192.
					 * */
					if(null != tableDataList && !tableDataList.isEmpty())
					{
						for (CreateBatchOpTableDTO CreateBatchOpTableDTO : tableDataList) {
							
							if(CreateBatchOpTableDTO.getClaimPaymentKey().equals(tableDTO.getClaimPaymentKey()))
							{
								if(value)
								{
									CreateBatchOpTableDTO.setCheckBoxStatus("true");
									//tableDTO.setChkSelect(true);
								}
								else
								{
									CreateBatchOpTableDTO.setCheckBoxStatus("false");
									//tableDTO.setChkSelect(false);
								}
							}
						}
					}
					
					
				
					
					
					/*boolean value = (Boolean) event.getProperty().getValue();
					CreateBatchOpTableDTO tableDTO = (CreateBatchOpTableDTO)chkBox.getData();
					if(value)
					{
						tableDTO.setCheckBoxStatus("true");
					}
					else
					{
						tableDTO.setCheckBoxStatus("false");
					}*/
					
				}
			}
		});
	}

	/*public void setValueForCheckBox(Boolean value)
	{
		List<CreateBatchOpTableDTO> searchTableDTOList = (List<CreateBatchOpTableDTO>) table.getItemIds();
		
		for (CreateBatchOpTableDTO searchTableDTO : searchTableDTOList) {
			//for (CreateBatchOpTableDTO searchTabledto : tableDataList){
				//if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTableDTO.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							//break;
						}
						else
						{
							searchTableDTO.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							//break;
						}
						
					}
			//}
		}
		}*/
	
	public void setValueForCheckBox(Boolean value)
	{
		List<CreateBatchOpTableDTO> searchTableDTOList = (List<CreateBatchOpTableDTO>) table.getItemIds();
		
		for (CreateBatchOpTableDTO searchTableDTO : searchTableDTOList) {
			for (CreateBatchOpTableDTO searchTabledto : tableDataList){
				if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTableDTO.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						else
						{
							searchTableDTO.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						
					}
			}
		}
		}
	
	public void setValueForSelectAllCheckBox(Boolean value)
	{
		List<CreateBatchOpTableDTO> searchTableDTOList = (List<CreateBatchOpTableDTO>) table.getItemIds();
		
		
			for (CreateBatchOpTableDTO searchTabledto : tableDataList){
				for (CreateBatchOpTableDTO searchTableDTO : searchTableDTOList) {
				if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTabledto.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getClaimPaymentKey());
								if(null != chkBox)
									chkBox.setValue(value);
							}
							break;
						}
						else
						{
							searchTabledto.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getClaimPaymentKey());
								if(null != chkBox)
									chkBox.setValue(value);
							}
							break;
						}
						
					}
			}
		}
		}
	
			
		
	public void setFinalTableList(List<CreateBatchOpTableDTO> tableRows) {
		Boolean isListEmpty = false;
//		tableDataList = new ArrayList<CreateBatchOpTableDTO>();
	//	List<CreateBatchOpTableDTO> dtoList = new ArrayList<CreateBatchOpTableDTO>();
		if(null != tableRows && !tableRows.isEmpty())
		{
			for (CreateBatchOpTableDTO CreateBatchOpTableDTO : tableRows) {
				
				/**
				 * When user tries to navigate from forward to previous page.
				 * already added records shouldn't be added to the tableDataList.
				 * Hence another list where keys are stored is used, where if a key is
				 * already existing in that list, then it won't get added in 
				 * the main list.This is done to avoid duplication.
				 * 
				 * */
				
				if(null != tableDataList && !tableDataList.isEmpty() && null != tableDataKeyList && !tableDataKeyList.isEmpty())
				{
					if(!tableDataKeyList.contains(CreateBatchOpTableDTO.getClaimPaymentKey()))
							{
								tableDataList.add(CreateBatchOpTableDTO);
								tableDataKeyList.add(CreateBatchOpTableDTO.getClaimPaymentKey());
							}
				}
				else
				{
					isListEmpty = true;
					break;
				}
			}
			/**
			 * 
			 * When first page is painted, table data list would be empty
			 * and hence adding all the records and its keys to data list and
			 * key list
			 * 
			 * */
			if(isListEmpty)
			{
				for (CreateBatchOpTableDTO CreateBatchOpTableDTO : tableRows) {
					tableDataList.add(CreateBatchOpTableDTO);
					tableDataKeyList.add(CreateBatchOpTableDTO.getClaimPaymentKey());
				}
				
			}
			/*if(null != dtoList && !dtoList.isEmpty()){
				for (CreateBatchOpTableDTO CreateBatchOpTableDTO : dtoList) {
					tableDataList.add(CreateBatchOpTableDTO);
				}
			}*/
		}
		// TODO Auto-generated method stub
		
	}
	
	public List<CreateBatchOpTableDTO> getTableItems()
	{
		return tableDataList;
	}
	
	public void resetTableDataList()
	{
		if(null != tableDataList)
		{
			tableDataList.clear();
		}
		if(null != tableDataKeyList)
		{
			tableDataKeyList.clear();
		}
	}
	
	
	public String isValid()
	{
		
		StringBuffer err = new StringBuffer(); 
		/*if(null != tableDataList && !tableDataList.isEmpty())
		{
			
			
			
			for (CreateBatchOpTableDTO dto : tableDataList) {	
				
				
			if(null != dto.getNoOfDiffDays() &&  dto.getNoOfDiffDays() >= -3 && (null ==dto.getRemarks() || ("").equalsIgnoreCase(dto.getRemarks())))
			{
				 err += "\nPlease Enter remarks for the Intimation Number:" + dto.getIntimationNo() +"<br>";
			}
			
		}
			
	}*/
		
		List<CreateBatchOpTableDTO> requestTableList = (List<CreateBatchOpTableDTO>) table.getItemIds();
		List<CreateBatchOpTableDTO> finalListForProcessing = null;
		if(null != requestTableList && !requestTableList.isEmpty())
		{
			finalListForProcessing = new ArrayList<CreateBatchOpTableDTO>();
			for (CreateBatchOpTableDTO CreateBatchOpTableDTO : requestTableList) {
					
				if(null != CreateBatchOpTableDTO.getChkSelect())
				{
				if(true==CreateBatchOpTableDTO.getChkSelect())
				{
					finalListForProcessing.add(CreateBatchOpTableDTO);
				}				
				}
				}
		}
		if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
		{
			for (CreateBatchOpTableDTO CreateBatchOpTableDTO : finalListForProcessing) {
				
		//if(null != CreateBatchOpTableDTO.getNoOfDiffDays() &&  CreateBatchOpTableDTO.getNoOfDiffDays() >3 && (null ==CreateBatchOpTableDTO.getRemarks() || ("").equalsIgnoreCase(CreateBatchOpTableDTO.getRemarks())))
				if((!( CreateBatchOpTableDTO.getNoOfDaysExceedingforCalculation().equals(CreateBatchOpTableDTO.getNoofdaysexceeding())) || !(CreateBatchOpTableDTO.getInterestAmntForCalculation().equals(CreateBatchOpTableDTO.getIntrestAmount())))
						&& (null ==CreateBatchOpTableDTO.getRemarks() || ("").equalsIgnoreCase(CreateBatchOpTableDTO.getRemarks())))
			{
					 err.append("\nPlease Enter remarks for the Intimation Number:").append(CreateBatchOpTableDTO.getIntimationNo()).append("<br>");
				}
			}
			
		}
		return err.toString();
	}
	

	 public void addBeanToList(CreateBatchOpTableDTO createLotDTO) {
	    	data.addBean(createLotDTO);
	 }
	 
	 public void addList(List<CreateBatchOpTableDTO> createLotDTO) {
		 for (CreateBatchOpTableDTO createandSearchLotDto : createLotDTO) {
			 data.addBean(createandSearchLotDto);
		 }
	 }
	 
	 @SuppressWarnings("unchecked")
	public List<CreateBatchOpTableDTO> getValues() {
		 @SuppressWarnings("unchecked")
			List<CreateBatchOpTableDTO> itemIds = (List<CreateBatchOpTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
    	
	}

	public void setTableList(List<CreateBatchOpTableDTO> tableItems,
			String createBatch) {
		table.removeAllItems();
		//List<CreateBatchOpTableDTO> tableItems = tableRows.getPageItems();
		if(null != tableItems && !tableItems.isEmpty())
		{
			for (CreateBatchOpTableDTO CreateBatchOpTableDTO : tableItems) {
				
				table.addItem(CreateBatchOpTableDTO);
			}
			
			
			
		}
		
		
	}
	
	public void setTotalNoOfRecords(int iSize)
	{
		fLayout.removeAllComponents();
		totalRocordsTxt.setCaption("<span style='font-size:medium;font-weight:medium;'>Total Number Of Records : "+String.valueOf(iSize)+"</span");
		totalRocordsTxt.setCaptionAsHtml(true);
		fLayout.addComponent(totalRocordsTxt);
		fLayout.setComponentAlignment(totalRocordsTxt, Alignment.TOP_CENTER);
	}
	
	public void sortTableList()
	{
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	}
	public void showClaimsDMSView(String intimationNo)
	{
		fireViewEvent(CreateAndSearchLotPresenter.SHOW_VIEW_DOCUMENTS,intimationNo);
		//fireViewEvent(CreateAndSearchLotPresenter.SHOW_VIEW_DOCUMENTS,intimationNo);
	}
	public void showLinkedPolicyDetails(CreateBatchOpTableDTO createLotDTO)
	{
		fireViewEvent(CreateAndSearchLotPresenter.VIEW_LINKED_POLICY,createLotDTO);
	}
	public void showEditPaymentDetailsView(CreateBatchOpTableDTO tableDTO)
	{
		fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_SHOW_EDIT_PAYMENT_DETAILS_VIEW,tableDTO);
	}
	
	public Pageable getPageable()
	{
		return this.pageUI.getPageable();
	}
	
	public void setHasNextPage(boolean flag)
	{
		this.pageUI.hasMoreRecords(flag);
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

	
	
	public void resetTable()
	{
		if(null != table){
			List<CreateBatchOpTableDTO> tableList = (List<CreateBatchOpTableDTO>)table.getItemIds();
			if(null != tableList && !tableList.isEmpty())
			{
				table.removeAllItems();
				if(null != tableLayout) {
					//tableLayout.removeAllComponents();
				}
			}
		}
	//	this.pageUI.resetPage();
	}

	public void addSearchListener(Searchable searchable)
	{
		this.searchable = searchable;
	}
	
	
	/*public void setRowColor(){
	
		ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds()); 
		final Object selectedRowId = getSelectedRowId(itemIds);
		createandSearchLotDto = (CreateBatchOpTableDTO)selectedRowId;
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				createandSearchLotDto = (CreateBatchOpTableDTO)selectedRowId;
				if(createandSearchLotDto != null){
				Double penalInterest = createandSearchLotDto.getIntrestAmount();
				
				if(penalInterest>0){
					
					return "select";
				}
				else{
					return "none";
				}
			
			}
				else{
					return "none";
				}
			}
	
		});
		
	}*/
	
	
public void setRowColor(final CreateBatchOpTableDTO dto){
		
		
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				
				CreateBatchOpTableDTO dto1 = (CreateBatchOpTableDTO) itemId;
				if(dto1 != null){
				String colourFlag = null != dto1.getRecStatusFlag() ? dto1.getRecStatusFlag():"";
				//System.out.println("-------No Of times loop executes-----");
				if(colourFlag.equals("E")){
					
					return "yellow";
				}
				else if(colourFlag.equals("F")){
					
					return "red";
				}
				else if(colourFlag.equals("R"))
				{
					return "amber";
				}
				else
				{
					return "none";
				}
			
			}
				else{
					return "none";
				}
			}
	
		});
		
	}
	
	/*public Object getSelectedRowId(ArrayList<Object> itemIds ){		
	
		for(Object id:itemIds){
			createandSearchLotDto = (CreateBatchOpTableDTO)id;		
			return id;
			 
		}		
		return null;
	}*/
	
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,EditPaymentDetailsView editPaymentView) {
		populatePreviousPaymentDetails(tableDTO);
		
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> paymentStatus) {
		this.paymentMode=paymentStatus;
		this.cpuCode=cpuCode;
	}
	
	public ValueChangeListener ifscListener(){
		ValueChangeListener listener = new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {				
				ComboBox paymentTypeCmb = (ComboBox) event.getProperty();
				if(paymentTypeCmb!=null && paymentTypeCmb.getValue()!=null){
					SelectValue value = (SelectValue) paymentTypeCmb.getValue();
					CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO)paymentTypeCmb.getData();
					if(value.getValue()!=null){
						if(value.getValue().equalsIgnoreCase("NEFT")){
							updatePaymentDetailTableDTO.getSearchButton().setEnabled(true);
							updatePaymentDetailTableDTO.getPayableAtButton().setEnabled(false);
							showIfscPopup(updatePaymentDetailTableDTO);
							HashMap<String, AbstractField<?>> hashMap = tableItem.get(updatePaymentDetailTableDTO);
							if(hashMap!=null){

								TextField txtbeneficiaryAcntNo = (TextField)hashMap.get("beneficiaryAcntNo");
								if(txtbeneficiaryAcntNo!=null){
									txtbeneficiaryAcntNo.setEnabled(Boolean.TRUE);
								}
								/*TextField txtpayableAt = (TextField)hashMap.get("payableAt");
								if(txtpayableAt!=null){
									txtpayableAt.setEnabled(Boolean.FALSE);
								}*/

							}
						}else{
							updatePaymentDetailTableDTO.getSearchButton().setEnabled(false);
							ViewSearchCriteriaTableDTO dto = new ViewSearchCriteriaTableDTO();
							setUpIFSCDetails(dto, updatePaymentDetailTableDTO);
							HashMap<String, AbstractField<?>> hashMap = tableItem.get(updatePaymentDetailTableDTO);
							if(hashMap!=null){
							
							TextField txtbeneficiaryAcntNo = (TextField)hashMap.get("beneficiaryAcntNo");
							if(txtbeneficiaryAcntNo!=null){
								txtbeneficiaryAcntNo.setEnabled(Boolean.TRUE);
								txtbeneficiaryAcntNo.setValue(null);
								txtbeneficiaryAcntNo.setEnabled(Boolean.FALSE);
								
							}
							/*TextField txtpayableAt = (TextField)hashMap.get("payableAt");
							if(txtpayableAt!=null){
								txtpayableAt.setEnabled(Boolean.TRUE);
							}*/
							
							updatePaymentDetailTableDTO.getPayableAtButton().setEnabled(true);
						}
						}
					}}
				}
		};
		return listener;
		
	}
	
public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto, CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
		
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(updatePaymentDetailTableDTO);
		if(hashMap!=null){
		
		TextField txtIfscCode = (TextField)hashMap.get("ifscCode");
		if(txtIfscCode!=null){
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(dto.getIfscCode());
			//txtIfscCode.setReadOnly(true);
		}
		
	
		TextField txtBankName = (TextField)hashMap.get("bankName");
		if(txtBankName!=null){
			txtBankName.setReadOnly(false);
			txtBankName.setValue(dto.getBankName());
			txtBankName.setReadOnly(true);
		}
		
	
		TextField txtBranch = (TextField)hashMap.get("branchName");
		if(txtBranch!=null){
			txtBranch.setReadOnly(false);
			txtBranch.setValue(dto.getBranchName());
			txtBranch.setReadOnly(true);
		}
		
		
		TextField txtCity = (TextField)hashMap.get("branchCity");
		if(txtCity!=null){
			txtCity.setReadOnly(false);
			txtCity.setValue(dto.getCity());
			txtCity.setReadOnly(true);
		}
		
		TextField txtbeneficiaryAcntNo = (TextField)hashMap.get("beneficiaryAcntNo");
		if(txtbeneficiaryAcntNo!=null){
			txtbeneficiaryAcntNo.setReadOnly(false);
		}
		}
	}

	/*public ValueChangeListener paymentCpuCodeListener(){
		ValueChangeListener listener = new ValueChangeListener() {
			
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;
	
			@Override
			public void valueChange(ValueChangeEvent event) {				
				TextField paymentTypeCmb = (TextField) event.getProperty();
				if(paymentTypeCmb!=null && paymentTypeCmb.getValue()!=null){
					CreateBatchOpTableDTO updatePaymentDetailTableDTO = (CreateBatchOpTableDTO)paymentTypeCmb.getData();
					fireViewEvent(CreateAndSearchLotPresenter.GET_PAYMENT_CPU_NAME,paymentTypeCmb.getValue(),updatePaymentDetailTableDTO);
				}
				}
		};
		return listener;
		
	}*/
	
	/*public void setPaymentCpuName(CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(updatePaymentDetailTableDTO);
		if(hashMap!=null){
			TextField paymentCpuName = (TextField)hashMap.get("paymentCpuName");
			if(paymentCpuName!=null){
				paymentCpuName.setReadOnly(false);
				paymentCpuName.setValue(updatePaymentDetailTableDTO.getPaymentCpuName());
				paymentCpuName.setReadOnly(true);
			}
		}
		
	}*/
	
	
public  void changeOfReasonListener(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForMedicalRemarks(searchField, getShortCutListenerForChangeOfReason(searchField));
	    
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
	
	
	private ShortcutListener getShortCutListenerForChangeOfReason(final TextArea txtFld)
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
				CreateBatchOpTableDTO mainDto = (CreateBatchOpTableDTO)txtFld.getData();
				txtArea.setData(mainDto);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				//txtArea.setSizeFull();
				txtArea.setRows(25);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				dialog.setHeight("75%");
			    dialog.setWidth("65%");
				txtArea.setReadOnly(false);
				
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
							CreateBatchOpTableDTO mainDto = (CreateBatchOpTableDTO)txtFld.getData();
							mainDto.setReasonForChange(txtFld.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				
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
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
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
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}

	private void showIfscPopup(
			CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
		Window popup = new com.vaadin.ui.Window();
		viewSearchCriteriaWindow.setWindowObject(popup);
		//viewSearchCriteriaWindow.setPresenterString(SHAConstants.CREATE_LOT,updatePaymentDetailTableDTO);
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
	
	
	
	public ValueChangeListener paymentTypeListener(ComboBox searchField,final CreateBatchOpTableDTO createLotDto){
		ValueChangeListener listener = new ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void valueChange(ValueChangeEvent event) {	
				
				ComboBox paymentType = (ComboBox) event.getProperty();
				SelectValue value = (SelectValue) paymentType.getValue();
				
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(createLotDto);
				TextArea emailID = (TextArea)hashMap.get("emailID");
				TextField panNo = (TextField)hashMap.get("panNo");
				TextField payableAt = (TextField)hashMap.get("payableAt");
				ComboBox payeeName = (ComboBox)hashMap.get("payeeName");
				TextArea payeeNameReasonForChange = (TextArea)hashMap.get("reasonForChange");
				TextArea paymentTypeReasonForChange = (TextArea)hashMap.get("payModeChangeReason");
				TextField legalHairName = (TextField)hashMap.get("legalFirstName");
				TextField beneficiayAcntNo = (TextField)hashMap.get("beneficiaryAcntNo");
				TextField ifscCode = (TextField)hashMap.get("ifscCode");
				
				if(value != null && value.getValue()!=null && value.getId().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
						
						panNo.setEnabled(true);
						//payableAt.setEnabled(true);
						beneficiayAcntNo.setEnabled(false);
					}
					else
					{
						panNo.setEnabled(true);
						//payableAt.setEnabled(false);
						beneficiayAcntNo.setEnabled(true);
						ifscCode.setEnabled(false);
					}
				
				if(null != paymentTypeReasonForChange && null != paymentTypeReasonForChange.getValue()){
					paymentTypeReasonForChange.setValue(null);
				}
				}
		};
		return listener;
		
	}
	
	public ValueChangeListener legalListener(){
		ValueChangeListener listener = new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {				
				TextField legalFirstName = (TextField) event.getProperty();
				if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					
				if(legalFirstName!=null && legalFirstName.getValue()!=null && !legalFirstName.getValue().isEmpty() && !legalFirstName.equals(null)){
					CreateBatchOpTableDTO createLotDto = (CreateBatchOpTableDTO)legalFirstName.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(createLotDto);
					TextField payeeName = (TextField)hashMap.get("payeeNameStr");
					
					if(payeeName!=null && payeeName.getValue()!=null){
					/*	showErrorMessage("Please unselect payee name");	
						legalFirstName.setValue(null);*/
						payeeName.setReadOnly(false);
						payeeName.setValue(null);
						payeeName.setReadOnly(true);
						payeeName.setNullRepresentation("");
						//payeeName.setEnabled(Boolean.FALSE);
					}else{
						payeeName.setEnabled(Boolean.FALSE);
					}
		 				
					}else{
						CreateBatchOpTableDTO createLotDto = (CreateBatchOpTableDTO)legalFirstName.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(createLotDto);
						ComboBox payeeName = (ComboBox)hashMap.get("payeeName");
//						payeeName.setEnabled(createLotDto.getIsInsured());
					}}
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
	
	
	public ValueChangeListener paymentCpuListener(){
		ValueChangeListener listener = new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {				
				TextField paymentCpu = (TextField) event.getProperty();
				if(paymentCpu != null && paymentCpu.getValue() != null){
					String value = paymentCpu.getValue();
					String[] cpuCode = value.split("-");
				    String cpuCodeStr = cpuCode[0].trim();
					CreateBatchOpTableDTO createLotDto = (CreateBatchOpTableDTO)paymentCpu.getData();
					fireViewEvent(SearchCreateBatchPresenter.PAYMENT_CPU_EMAIL_ID,cpuCodeStr,createLotDto);		
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(createLotDto);
					TextArea emailID = (TextArea)hashMap.get("emailID");
					if(emailID!=null){
						emailID.setValue(createLotDto.getZonalMailId());
					}
				}}
		};
		return listener;
		
	}
	
	
public boolean validatePage(){
	
	List<CreateBatchOpTableDTO> itemIds = (List<CreateBatchOpTableDTO>) this.table.getItemIds() ;
	
	StringBuffer eMsg = new StringBuffer();
	Boolean hasError = false;
	
	for (CreateBatchOpTableDTO CreateBatchOpTableDTO : itemIds) {
		if(CreateBatchOpTableDTO.getCheckBoxStatus() != null && ("true").equalsIgnoreCase(CreateBatchOpTableDTO.getCheckBoxStatus())){
			
			if(null ==CreateBatchOpTableDTO.getPaymentType()){
				eMsg.append("Please choose payment Type</br>");
				hasError = true;
			}
			
		if(CreateBatchOpTableDTO.getPaymentType() != null && null !=CreateBatchOpTableDTO.getPaymentType().getId() &&
				(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER.equals(CreateBatchOpTableDTO.getPaymentType().getId())))
		{

			if(CreateBatchOpTableDTO.getIfscCode() == null || CreateBatchOpTableDTO.getIfscCode().equals("")) 
			{
				eMsg.append("Please enter the IFSC Code</br>");
				hasError = true;
			}

			if(CreateBatchOpTableDTO.getBeneficiaryAcntNo() == null || CreateBatchOpTableDTO.getBeneficiaryAcntNo().isEmpty())
			{
				eMsg.append("Please enter Account No</br>");
				hasError = true; 
			}

		}
		else 
		{

			if(CreateBatchOpTableDTO.getPayableAt() == null || CreateBatchOpTableDTO.getPayableAt().isEmpty())
			{
				eMsg.append(" </br>Please enter Payable At ");
				hasError = true;
			}


		}
		if(CreateBatchOpTableDTO.getDocumentReceivedFrom()!=null && ! CreateBatchOpTableDTO.getDocumentReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
			
			if(null != CreateBatchOpTableDTO.getPayeeNameStr())
			{
				if(null != CreateBatchOpTableDTO.getTempPayeeName() && CreateBatchOpTableDTO.getTempPayeeName().getValue() != null && !(CreateBatchOpTableDTO.getTempPayeeName().getValue().equals(CreateBatchOpTableDTO.getPayeeNameStr())) &&  
						(CreateBatchOpTableDTO.getReasonForChange() == null || CreateBatchOpTableDTO.getReasonForChange().equalsIgnoreCase("")))
						{
							eMsg.append(" </br>Please enter Reason For changing the Payee Name ");
							hasError = true;
						}
			}
		}

		if((null == CreateBatchOpTableDTO.getPayModeChangeReason()) || (CreateBatchOpTableDTO.getPayModeChangeReason().equals(""))){

			if(CreateBatchOpTableDTO.getPaymentType() != null && null != CreateBatchOpTableDTO.getTempPaymentType() && !(CreateBatchOpTableDTO.getTempPaymentType().getId().equals(CreateBatchOpTableDTO.getPaymentType().getId()))){				

				hasError = true;
				eMsg.append("Please Enter Reason for Changing the Payment Mode</br>");
			}

		}

		if((null == CreateBatchOpTableDTO.getPayeeNameStr() || (CreateBatchOpTableDTO.getPayeeNameStr().equals(""))) 
				&& (CreateBatchOpTableDTO.getLegalFirstName() == null || (CreateBatchOpTableDTO.getLegalFirstName() != null && CreateBatchOpTableDTO.getLegalFirstName().isEmpty())))
		{
			hasError = true;
			eMsg.append("</br>Please select Payee Name ");
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
	} 
	else 
	{
		return true;
	}		

}

public  void changeOfPaymentModeReasonListener(TextArea searchField, final  Listener listener) {
	
    ShortcutListener enterShortCut = new ShortcutListener(
        "ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {
	
      private static final long serialVersionUID = 1L;
      @Override
      public void handleAction(Object sender, Object target) {
        ((ShortcutListener) listener).handleAction(sender, target);
      }
    };
    handleShortcutForMedicalRemarks(searchField, getShortCutListenerForchangeOfPaymentModeReason(searchField));
    
  }

private ShortcutListener getShortCutListenerForchangeOfPaymentModeReason(final TextArea txtFld)
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
			CreateBatchOpTableDTO mainDto = (CreateBatchOpTableDTO)txtFld.getData();
			txtArea.setData(mainDto);
			//txtArea.setStyleName("Boldstyle");
			txtArea.setValue(txtFld.getValue());
			txtArea.setNullRepresentation("");
			//txtArea.setSizeFull();
			txtArea.setRows(25);
			txtArea.setHeight("30%");
			txtArea.setWidth("100%");
			dialog.setHeight("75%");
		    dialog.setWidth("65%");
			txtArea.setReadOnly(false);
			
			
			txtArea.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());						
						CreateBatchOpTableDTO mainDto = (CreateBatchOpTableDTO)txtFld.getData();
						mainDto.setPayModeChangeReason(txtFld.getValue());
				}
			});
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			
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
//					TextArea txtArea = (TextArea)dialog.getData();
//					txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
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
//					TextArea txtArea = (TextArea)dialog.getData();
//					txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
					dialog.close();
				}
			});	
		}
	};
	
	return listener;
}
private void showPaymentCpuCodePopup(
		CreateBatchOpTableDTO CreateBatchOpTableDTO) {
	final Window popup = new com.vaadin.ui.Window();
	
	paymentCpucodeListenerTable.init("", false, false);
//	paymentCpucodeListenerTable.initPresenter(SHAConstants.CREATE_LOT_PAYMENT_CPU_CODE_CHANGE,CreateBatchOpTableDTO,popup);
	paymentCpucodeListenerTable.initTable();
	
	List<ViewSearchCriteriaTableDTO> paymentCpuCodeList= masterService.getTmpCpuCodeWithDescriptionList();
 
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
	vLayout.addComponents(paymentCpucodeListenerTable,closebutLayout);
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

public void setUpPaymentCpuCodeDetails(ViewSearchCriteriaTableDTO dto, CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
	
	HashMap<String, AbstractField<?>> hashMap = tableItem.get(updatePaymentDetailTableDTO);
	if(hashMap!=null){
	
	TextField txtpaymentCpuCode= (TextField)hashMap.get("paymentCpucodeTextValue");
	if(txtpaymentCpuCode!=null){
		txtpaymentCpuCode.setReadOnly(false);
		txtpaymentCpuCode.setValue(dto.getCpuCodeWithDescription());
		txtpaymentCpuCode.setReadOnly(true);
	}
	
	}
}

public void setPage(Page<CreateBatchOpTableDTO> page){
	this.pageUI.setPageDetails(page);
}

public void clearExistingList(){
	if(this.tableDataList != null){
		this.tableDataList.clear();
	}
	
	if(this.tableDataKeyList != null){
		this.tableDataKeyList.clear();
	}
}


public void setUpPayeeNameDetails(ViewSearchCriteriaTableDTO dto, CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
	
	HashMap<String, AbstractField<?>> hashMap = tableItem.get(updatePaymentDetailTableDTO);
	if(hashMap!=null){
	
	TextField txtpayeeName= (TextField)hashMap.get("payeeNameStr");
	if(txtpayeeName!=null){
		txtpayeeName.setReadOnly(false);
		if(null != dto.getChangePayeeName()){
			txtpayeeName.setValue(dto.getChangePayeeName().toUpperCase());
		}
		else if(null != dto.getPayeeName()){
			
			txtpayeeName.setValue(dto.getPayeeName());
		}
		txtpayeeName.setReadOnly(true);
	}
	
	}
}

private void showPayeeNamePopup(
		CreateBatchOpTableDTO CreateBatchOpTableDTO) {
	final Window popup = new com.vaadin.ui.Window();
	
	paymentCpucodeListenerTable.init("", false, false);
//	paymentCpucodeListenerTable.initPresenter(SHAConstants.CREATE_LOT_PAYEE_NAME_CHANGE,CreateBatchOpTableDTO,popup);
	paymentCpucodeListenerTable.initTable();
	
	fireViewEvent(CreateAndSearchLotPresenter.GET_PAYEE_NAME_DETAILS,CreateBatchOpTableDTO);
	
	if(null != CreateBatchOpTableDTO.getPayeeNameList() && !CreateBatchOpTableDTO.getPayeeNameList().isEmpty()){
		
		paymentCpucodeListenerTable.setTableList(CreateBatchOpTableDTO.getPayeeNameList());;
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
	vLayout.addComponents(paymentCpucodeListenerTable,closebutLayout);
	closebutLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_CENTER);
	
	popup.setWidth("30%");
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

public Boolean getIsSearchBtnClicked() {
	return isSearchBtnClicked;
}

public void setIsSearchBtnClicked(Boolean isSearchBtnClicked) {
	this.isSearchBtnClicked = isSearchBtnClicked;
}

public ValueChangeListener payeeNameListener(){
	ValueChangeListener listener = new ValueChangeListener() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {				
			TextField payeeName = (TextField) event.getProperty();
			if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
				
			if(payeeName!=null && payeeName.getValue()!=null && !payeeName.getValue().isEmpty() && !payeeName.equals(null)){
				CreateBatchOpTableDTO createLotDto = (CreateBatchOpTableDTO)payeeName.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(createLotDto);
				TextField otherPayeeName = (TextField)hashMap.get("legalFirstName");
				
				if(otherPayeeName!=null && otherPayeeName.getValue()!=null){
					otherPayeeName.setValue(null);
					otherPayeeName.setNullRepresentation("");
				}
			}
		}
	}
};
	return listener;
}

public void setUpPayableAmt(String payableAt, CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
	
	HashMap<String, AbstractField<?>> hashMap = tableItem.get(updatePaymentDetailTableDTO);
	if(hashMap!=null){
	
	TextField txtPayableAt = (TextField)hashMap.get("payableAt");
		if(txtPayableAt!=null){
			txtPayableAt.setReadOnly(false);
			txtPayableAt.setValue(payableAt);
			txtPayableAt.setReadOnly(true);
			updatePaymentDetailTableDTO.setPayableAt(payableAt);
		}
	
	}
}
	
public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
	
	HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
		if(hashMap!=null){
		TextArea emailID = (TextArea)hashMap.get("emailID");
		TextField txtPanNo = (TextField)hashMap.get("panNo");
		TextField txtAccntNo = (TextField)hashMap.get("beneficiaryAcntNo");
		TextField txtIfscCode = (TextField)hashMap.get("ifscCode");
		TextField txtBankName = (TextField)hashMap.get("bankName");
		TextField txtCity = (TextField)hashMap.get("branchCity");
		TextField txtBranch = (TextField)hashMap.get("branchName");
		
		if(null != emailID)
		{
			emailID.setReadOnly(false);
			emailID.setValue(tableDTO.getEmailId());
			emailID.setEnabled(true);
		}
		if(null != txtPanNo)
		{
			txtPanNo.setReadOnly(false);
			txtPanNo.setValue(tableDTO.getPanNo());
			txtPanNo.setEnabled(true);
		}
		if(null != txtAccntNo)
		{
			txtAccntNo.setReadOnly(false);
			txtAccntNo.setValue(tableDTO.getBankAccountNo());
			txtAccntNo.setEnabled(true);
		}
		if(null != txtIfscCode)
		{
			txtIfscCode.setReadOnly(false);
			txtIfscCode.setValue(tableDTO.getIfsccode());
			txtIfscCode.setEnabled(true);
		}
		if(null != txtBankName)
		{
			txtBankName.setReadOnly(false);
			txtBankName.setValue(tableDTO.getBankName());
			txtBankName.setEnabled(true);
		}
		if(null != txtCity)
		{
			txtCity.setReadOnly(false);
			txtCity.setValue(tableDTO.getBankCity());
			txtCity.setEnabled(true);
		}
		if(null != txtBranch)
		{
			txtBranch.setReadOnly(false);
			txtBranch.setValue(tableDTO.getBankBranch());
			txtBranch.setEnabled(true);
		}
		}
}
}
