package com.shaic.reimbursement.paymentprocess.paymentreprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.PagerListener;
import com.shaic.arch.table.PagerUI;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.rod.citySearchCriteria.CitySearchCriteriaViewImpl;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
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
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PaymentReprocessSearchResultTable extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2456020660272910853L;
	
	
	private Table table;
	private String presenterString;
	private Map<PaymentReprocessSearchResultDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PaymentReprocessSearchResultDTO, HashMap<String, AbstractField<?>>>();
	private PagerUI pageUI;
	private HorizontalLayout fLayout;
	private VerticalLayout vLayout;
	private Searchable searchable;
	private Label totalRocordsTxt;
	Integer intNoOfDays = 0;
	Double intPenalInterestAmnt = 0d;
	private BeanItemContainer<SelectValue> paymentMode;
	private BeanItemContainer<SelectValue> cpuCode;
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	@Inject
	private CitySearchCriteriaViewImpl citySearchCriteriaWindow;
	@Inject
	private MasterService masterService;
	PaymentReprocessSearchFormDTO paymentFormDto;
	
	public BeanItemContainer<PaymentReprocessSearchResultDTO> data = new BeanItemContainer<PaymentReprocessSearchResultDTO>(PaymentReprocessSearchResultDTO.class);
	public HashMap<Long,Component> compMap = null;
	public List<PaymentReprocessSearchResultDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	private VerticalLayout tableLayout = null;
	private Boolean isSearchBtnClicked = false;
	
	private Window popup;
	private TextArea remarksForCancelAndReprocess;
	Window searchPopup = new com.vaadin.ui.Window();
	
	
	@SuppressWarnings("deprecation")
	public void init(String presenterString, Boolean showPagerFlag, Window wpop) {
		this.presenterString = presenterString;
		this.searchPopup = wpop;
		
		initTable();
		pageUI = new PagerUI();
		
		addListener();		
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
		tableLayout = new VerticalLayout();
		fLayout = new HorizontalLayout();
		fLayout.setWidth("100%");
		fLayout.setHeight("20px");
		totalRocordsTxt = new Label();
		totalRocordsTxt.setWidth(null);
		
		 if(showPagerFlag){
			 tableLayout.addComponent(pageUI);
		     pageUI.addListener(new PagerListener() {
				@Override
				public void changePage() {
					
					searchable.doSearch();
				}
			});
		 }
		
//		 tableLayout.addComponent(fLayout);
		 tableLayout.addComponent(table);
		 tableLayout.setSpacing(true);
		 tableLayout.setMargin(false);
		 
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
	
	
	@SuppressWarnings("deprecation")
	void initTable() {
		// Create a data source and bind it to a table
			table = new Table();
			table.setContainerDataSource(data);
			table.setWidth("100%");
			table.setPageLength(table.getItemIds().size());
			table.setHeight("550px");
			
			generatecolumns();
		
			table.removeAllItems();
			table.setVisibleColumns(new Object[] {"serialNo","sendToPayment","viewdetails","intimationNumber", "policyNumber","rodNumber","recordCount","productCode",
					"cpuCode","paymentCpuCode","emailId","paymentType","claimType","priorityFlag","payeeName","payeeRelationship","nomineeName","nomineeRelationship",
					"legalHeirName","legalHeirRelationship","accType","ifscCode","accountNumber","bankName","branchName","nameAsperBankAC","panNumber","micrCode",
					 "virtualPaymentAddr","proposerName","gmcEmployeeName","payableAt","bancsUprId","utrNumber",/*"xxxxxxxx",*/"chequeDDDate"/*,"payableAtSearch"*/
					 ,"hospitalCode","reconisderFlag","approvedAmount","lastAckDate","faApprovalDate","delayDays","iRDATAT","interestRate","interestAmount","totalAmount"
					 ,"intrestRemarks","remarks"});
			
			table.setColumnHeader("serialNo", "S.No");
			table.setColumnHeader("sendToPayment", "Send </br>To Payment");
			table.setColumnHeader("viewdetails", "");
			table.setColumnHeader("intimationNumber", "Intimation </br> No");
			table.setColumnHeader("policyNumber", "Policy No");
			table.setColumnHeader("rodNumber", "ROD No");
			table.setColumnHeader("recordCount", "Record</br>Count");
			table.setColumnHeader("productCode", "Product");
			table.setColumnHeader("cpuCode", "Cpu</br>Code");
			table.setColumnHeader("paymentCpuCode", "Payment</br> CPU </br>Code");
			table.setColumnHeader("emailId", "Email ID");
			table.setColumnHeader("paymentType", "Payment </br>Type");
			table.setColumnHeader("claimType", "Type of </br> Claim");			
			table.setColumnHeader("priorityFlag", "Priority</br> Flag");			
			table.setColumnHeader("payeeName", "Payee</br> Details ");
			table.setColumnHeader("payeeRelationship", "Payee</br> Relation </br>ship");
			table.setColumnHeader("nomineeName", "Nominee");
			table.setColumnHeader("nomineeRelationship", "Nominee</br> Relation</br> Ship");
			table.setColumnHeader("legalHeirName", "Legal </br>Heir");
			table.setColumnHeader("legalHeirRelationship", "Legal</br> Heir </br>Relationship");
			table.setColumnHeader("accType", "Account</br> Type");
			table.setColumnHeader("ifscCode", "IFSC </br> Code");
			table.setColumnHeader("accountNumber", "Account Number");
			table.setColumnHeader("bankName", "Name of the Bank");
			table.setColumnHeader("branchName", "Branch Name");		
			table.setColumnHeader("nameAsperBankAC", "Name as per Bank AC");			
			table.setColumnHeader("panNumber", "PAN No");
			table.setColumnHeader("micrCode", "MICR Code");
			table.setColumnHeader("virtualPaymentAddr", "Virtual Payment Address");
			table.setColumnHeader("proposerName", "Proposer Name");
			table.setColumnHeader("gmcEmployeeName", "Employee Name");
			table.setColumnHeader("payableAt", "Payable City");
			table.setColumnHeader("bancsUprId","UPR ID");
			table.setColumnHeader("utrNumber","UTR/Instrument No");
			//table.setColumnHeader("","Bank Name");
			table.setColumnHeader("chequeDDDate", "Cheque/DD Date");
			//table.setColumnHeader("payableAtSearch", "Seach</br> Payable</br> City");
			table.setColumnHeader("hospitalCode", "Provider<br>Code");
			table.setColumnHeader("reconisderFlag", "Reconsideration</br> Flag (Y/N)");
			table.setColumnHeader("approvedAmount", "Approved Amount");
			table.setColumnHeader("lastAckDate", "Last<br> Acknowledgement</br> Received Date");
			table.setColumnHeader("faApprovalDate", "Date of<br>Financial<br>Approval");
			table.setColumnHeader("delayDays", "No of Day</br> for</br> Processing");
			table.setColumnHeader("iRDATAT", "IRDA TAT");//
	 		table.setColumnHeader("interestRate", "No of Days</br> Interest Payable");
	 		table.setColumnHeader("interestAmount", "Penal Interest Amount");
	 		table.setColumnHeader("totalAmount", "Total Amount");
	 		table.setColumnHeader("intrestRemarks", "Interest Remarks");
	 		table.setColumnHeader("remarks", "Remarks");
		
			
			table.setEditable(false);	
	 		table.setFooterVisible(false);
			table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	}

	
	@SuppressWarnings("deprecation")
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@SuppressWarnings("deprecation")
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			PaymentReprocessSearchResultDTO initiateDTO = (PaymentReprocessSearchResultDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(initiateDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(initiateDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(initiateDTO);
			}
						
			if("intimationNumber".equals(propertyId)) {
				
				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("intimationNumber", field);

				return field;
			} 
			else if ("policyNumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("160px");
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("policyNumber", field);
			
				return field;
			}
			else if ("rodNumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("190px");
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("rodNumber", field);
				
				return field;
			}

			else if ("recordCount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("190px");
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("recordCount", field);
				
				return field;
			}
			
			else if("product".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("product", field);
				return field;
			}
			else if("cpuCode".equals(propertyId)) {
				ComboBox field = new ComboBox();
				field.setEnabled(false);
				field.setWidth("120px");
				field.setData(initiateDTO);
				tableRow.put("cpuCode", field);
				return field;
			}
			
			else if("paymentCpuCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("110px");
				field.setData(initiateDTO);
				field.setEnabled(Boolean.FALSE);
				tableRow.put("paymentCpuCode", field);
				return field;
			}
			
			else if("emailId".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("emailId", field);
				return field;
			}

			else if("paymentType".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("paymentType", field);
				return field;
				
			}
			
			else if("claimType".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("claimType", field);
				return field;
			}
			else if("priorityFlag".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("priorityFlag", field);
				return field;
			}

			else if("payeeName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("payeeName", field);
				return field;
			}
			else if("payeeRelationship".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("payeeRelationship", field);
				return field;
			}
			else if("nomineeName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("nomineeName", field);
				return field;
			}
			
			else if("nomineeRelationship".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("nomineeRelationship", field);
				return field;
			}
			
			else if("legalHeirName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("legalHeirName", field);
				return field;
			}
			
			else if("legalHeirRelationship".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("legalHeirRelationship", field);
				return field;
			}
			
			else if("accType".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("accType", field);
				return field;
			}

			else if("ifscCode".equals(propertyId)) {
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
			}
			else if("accountNumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("accountNumber", field);
				return field;
			}
			else if("bankName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("bankName", field);
				return field;
			}
			else if("branchName".equals(propertyId)) {
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
			}
			else if("nameAsperBankAC".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(Boolean.TRUE);
				tableRow.put("nameAsperBankAC", field);
				return field;
			}

			else if("panNumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(initiateDTO);
				field.setEnabled(Boolean.TRUE);
				tableRow.put("panNumber", field);
				return field;
			}
			else if("micrCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("micrCode", field);
				return field;
			}
			else if("virtualPaymentAddr".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("virtualPaymentAddr", field);
				return field;
			}
			else if("proposerName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("proposerName", field);
				return field;
			}

			else if("gmcEmployeeName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
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
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("payableAt", field);
				return field;
			}
			else if("bancsUprId".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("bancsUprId", field);
				return field;
			}
			
			else if("utrNumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("utrNumber", field);
				return field;
			}
			
			//bank name
			else if("chequeDDDate".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("chequeDDDate", field);
				return field;
			}
			else if("hospitalCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("hospitalCode", field);
				return field;
			}
			
			else if("reconisderFlag".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("reconisderFlag", field);
				return field;
			}
			else if("approvedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("approvedAmount", field);
				return field;
			}
			else if("lastAckDate".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("lastAckDate", field);
				return field;
			}
			
			else if("faApprovalDate".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("faApprovalDate", field);
				return field;
			}
			else if("delayDays".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("delayDays", field);
				return field;
			}
			
			else if("delayDays".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("delayDays", field);
				return field;
			}
			
			else if("iRDATAT".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("iRDATAT", field);
				return field;
			}
			
			else if("interestAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("interestAmount", field);
				return field;
			}
			else if("totalAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("totalAmount", field);
				return field;
			}
			
			else if("intrestRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("intrestRemarks", field);
				return field;
			}
			
			else if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(initiateDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("remarks", field);
				return field;
			}
			
			else if("serialNo".equals(propertyId)) {
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
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

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
	
	@SuppressWarnings("deprecation")
	private void generatecolumns(){
			
			/*table.removeGeneratedColumn("payableAtSearch");
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
					final PaymentReprocessSearchResultDTO initiateDTO = (PaymentReprocessSearchResultDTO) itemId;				
					searchButton.setData(itemId);
					initiateDTO.setPayableAtButton(searchButton);
					if (initiateDTO.getPaymentType() != null) {
						searchButton.setEnabled(true);
					}else{
						searchButton.setEnabled(false);
					}
					searchButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;

						public void buttonClick(ClickEvent event) {

							Window popup = new com.vaadin.ui.Window();
							citySearchCriteriaWindow.setPresenterString("");//
							//citySearchCriteriaWindow.initView(popup,initiateDTO);
							popup.setWidth("75%");
							popup.setHeight("90%");
							popup.setContent(citySearchCriteriaWindow);
							popup.setClosable(true);
							popup.center();
							popup.setResizable(true);
							
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

							popup.setModal(true);
							
							UI.getCurrent().addWindow(popup);
							searchButton.setEnabled(true);
						
						}
					});
					return searchButton;
				}
			});*/

			table.removeGeneratedColumn("sendToPayment");
			table.addGeneratedColumn("sendToPayment", new Table.ColumnGenerator() {
				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					Button button = new Button();
					final PaymentReprocessSearchResultDTO tableDTO = (PaymentReprocessSearchResultDTO)itemId;
					if(tableDTO.getReprocessType()!=null && !tableDTO.getReprocessType().isEmpty()){
						
						if(tableDTO.getReprocessType().equalsIgnoreCase(SHAConstants.WITHOUT_CHANGE_IN_AMOUNT)){
							button = new Button("Reprocess");
						}else if(tableDTO.getReprocessType().equalsIgnoreCase(SHAConstants.CANCEL_CLAIM_PAYMENET)){
							button = new Button("Cancel /Reject Payment");
						}else if(tableDTO.getReprocessType().equalsIgnoreCase(SHAConstants.WITH_CHANGE_IN_AMOUNT)){
							button = new Button("Reprocess");
						}
					}

					button.addClickListener(new Button.ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							// TODO Auto-generated method stub
							showCancelAndReprocess(tableDTO);
						}
					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("90px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
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
						
					final PaymentReprocessSearchResultDTO tableDTO = (PaymentReprocessSearchResultDTO)itemId;
					
					Button button = new Button("View Details");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
						
							showClaimsDMSView(tableDTO.getIntimationNumber());
						}
					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("90px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
				}
			});



			
			/*table.removeGeneratedColumn("remarks");
			table.addGeneratedColumn("remarks", new Table.ColumnGenerator() {
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
					final PaymentReprocessSearchResultDTO tableDTO = (PaymentReprocessSearchResultDTO)itemId;	
					
					
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
			});*/
			
		}

	
	public List<PaymentReprocessSearchResultDTO> getTableItems()
	{
		@SuppressWarnings("unchecked")
		List<PaymentReprocessSearchResultDTO> itemIds = (List<PaymentReprocessSearchResultDTO>) this.table.getItemIds() ;
		return itemIds;
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
	
	
	public String isValid()//
	{
		
		StringBuffer err = new StringBuffer(); 
		
		List<PaymentReprocessSearchResultDTO> requestTableList = (List<PaymentReprocessSearchResultDTO>) table.getItemIds();
		List<PaymentReprocessSearchResultDTO> finalListForProcessing = null;
        return err.toString();
	}
	
	public void sortTableList()
	{
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	}
	public void showClaimsDMSView(String intimationNo)
	{
		fireViewEvent(PaymentReprocessSearchPresenter.SHOW_VIEW_DOCUMENTS,intimationNo);//
	}
	
	@SuppressWarnings("deprecation")
	public void showCancelAndReprocess(PaymentReprocessSearchResultDTO paymentReprocess){
		
		popup = new com.vaadin.ui.Window();
		
		if(paymentReprocess.getReprocessType().equals(SHAConstants.WITHOUT_CHANGE_IN_AMOUNT)){
			remarksForCancelAndReprocess=new TextArea("Remarks for Reprocess*");
		}else if(paymentReprocess.getReprocessType().equals(SHAConstants.CANCEL_CLAIM_PAYMENET)){
			remarksForCancelAndReprocess=new TextArea("Remarks for\n Cancelling/Rejecting\n the Payment*");
		}else if(paymentReprocess.getReprocessType().equals(SHAConstants.WITH_CHANGE_IN_AMOUNT)){
			remarksForCancelAndReprocess=new TextArea("Remarks for Reprocess*");
		}
		remarksForCancelAndReprocess.setWidth("400px");
		remarksForCancelAndReprocess.setDescription("Click the Text Box and Press F8 for Detail Popup");
		handleTextAreaPopup(remarksForCancelAndReprocess,null);
		
		
		FormLayout linkedPolicyDetails = new FormLayout(remarksForCancelAndReprocess);
		linkedPolicyDetails.setSpacing(true);
		linkedPolicyDetails.setMargin(true);
		linkedPolicyDetails.setStyleName("layoutDesign");
		
		Button btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(remarksForCancelAndReprocess.getValue()!=null && !remarksForCancelAndReprocess.getValue().equalsIgnoreCase("")){
					paymentReprocess.setPaymenetCancelRemarks(remarksForCancelAndReprocess.getValue());
					fireViewEvent(PaymentReprocessSearchPresenter.PAYMENT_CANCEL_REPROCESS_REMARKS,paymentReprocess);
					UI.getCurrent().removeWindow(popup);
					UI.getCurrent().removeWindow(searchPopup);
				}else{
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					GalaxyAlertBox.createErrorBox("Please Enter Remarks before Submit", buttonsNamewithType);
				}
				
			}
		});
		
		Button btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				//fireViewEvent(PaymentReprocessSearchPresenter.POPUP_CANCEL_REQUEST,"");

				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
				buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
				HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
						.createConfirmationbox("Are you sure you want to cancel ?", buttonsNamewithType);
				Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
						.toString());
				homeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						UI.getCurrent().removeWindow(popup);
					}
					});
			}
		});
		
		@SuppressWarnings("deprecation")
		HorizontalLayout buttonLayout = new HorizontalLayout(btnSubmit, btnCancel);
		buttonLayout.setSpacing(true);
		buttonLayout.setStyleName("layoutDesign", true);
		
		@SuppressWarnings("deprecation")
		VerticalLayout vLayout = new VerticalLayout(linkedPolicyDetails,buttonLayout);
		vLayout.setSpacing(true);
		vLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		vLayout.setHeight("90%");
		popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("50%");
		popup.setHeight("35%");
		popup.setContent(vLayout);
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
	

	
	@SuppressWarnings("unused")
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
	@SuppressWarnings("deprecation")
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
		ShortcutListener listener =  new ShortcutListener("Redraft Remarks",KeyCodes.KEY_F8,null) {

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

				String strCaption = "";

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
					//GALAXYMAIN-10545
					dialog.setPositionX(350);
					dialog.setPositionY(50);
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

	public Pageable getPageable()
	{
		return this.pageUI.getPageable();
	}
	
	/*public void setHasNextPage(boolean flag)
	{
		this.pageUI.hasMoreRecords(flag);

	}*/
	
	
	/*public void setPage(Page<PaymentReprocessSearchResultDTO> page){
		this.pageUI.setPageDetails(page);
	}*/
	
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
			List<PaymentReprocessSearchResultDTO> tableList = (List<PaymentReprocessSearchResultDTO>)table.getItemIds();
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
		createandSearchLotDto = (PaymentReprocessSearchResultDTO)selectedRowId;
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				createandSearchLotDto = (PaymentReprocessSearchResultDTO)selectedRowId;
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
	
	
public void setRowColor(final PaymentReprocessSearchResultDTO dto){/*
		
		
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				
				PaymentReprocessSearchResultDTO dto1 = (PaymentReprocessSearchResultDTO) itemId;
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
		
	*/}


public void clearExistingList(){
	if(this.tableDataList != null){
		this.tableDataList.clear();
	}
	
	if(this.tableDataKeyList != null){
		this.tableDataKeyList.clear();
	}
}

public Boolean getIsSearchBtnClicked() {
	return isSearchBtnClicked;
}

public void setIsSearchBtnClicked(Boolean isSearchBtnClicked) {
	this.isSearchBtnClicked = isSearchBtnClicked;
}


public void setTableList(List<PaymentReprocessSearchResultDTO> tableItems) {
	table.removeAllItems();
	if(null != tableItems && !tableItems.isEmpty())
	{
		for (PaymentReprocessSearchResultDTO paymentReprocessSearchResultDTO : tableItems) {
			
			table.addItem(paymentReprocessSearchResultDTO);
		}
		
	}
	
	
}

public void setPage(Page<PaymentReprocessSearchResultDTO> tableRows) {
	// TODO Auto-generated method stub
	this.pageUI.setPageDetails(tableRows);
}

public void setTotalNoOfRecords(int size) {
	// TODO Auto-generated method stub
	
}
}