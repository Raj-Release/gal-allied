package com.shaic.reimbursement.paymentprocess.updatepayment;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.PagerListener;
import com.shaic.arch.table.PagerUI;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaViewImpl;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
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
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UpdatePaymentDetailListenerTable extends ViewComponent {
	
	private Table table;
	private String presenterString;
	//private Map<String, Object> referenceData;
	private Map<UpdatePaymentDetailTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<UpdatePaymentDetailTableDTO, HashMap<String, AbstractField<?>>>();
	private PagerUI pageUI;
	private FormLayout fLayout;
	private Searchable searchable;
	private Label totalRocordsTxt;
	Integer intNoOfDays = 0;
	Double intPenalInterestAmnt = 0d;
	private BeanItemContainer<SelectValue> paymentMode;	
	
	@Inject
	private ViewSearchCriteriaViewImpl viewSearchCriteriaWindow;
	
	private UpdatePaymentDetailTableDTO createandSearchLotDto;
	
	//private int i = 0;
	
	
	//private FormLayout fLayout;
	
	public 
	
	/***
	 * Bean object fetch from db
	 */
	BeanItemContainer<UpdatePaymentDetailTableDTO> data = new BeanItemContainer<UpdatePaymentDetailTableDTO>(UpdatePaymentDetailTableDTO.class);
	public HashMap<Long,Component> compMap = null;
	public List<UpdatePaymentDetailTableDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	private VerticalLayout tableLayout = null;
	private BeanItemContainer<SelectValue> cpucode;
	
	public void init(String presenterString,Boolean showPagerFlag) {
		
		this.presenterString = presenterString;
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		pageUI = new PagerUI();
		
		addListener();		
		//table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
		tableLayout = new VerticalLayout();
		fLayout = new FormLayout();
		totalRocordsTxt = new Label();
		
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
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setHeight("310px");
		
		generatecolumns();
		
			table.removeAllItems();
			table.setVisibleColumns(new Object[] { "serialNumber","chkSelect","viewdetails","intimationNo", "maApprovedDate", "cpucode", 
					"cpuName","paymentCpuCode","paymentCpuName","proposerName","employeeName","payeeName","paymentType","reasonForChange","ifscCode","beneficiaryAcno","bankName",
					"branchName","branchCity","payableCity","emailID","typeOfClaim","approvedAmt",
					"productName" });
			table.setColumnHeader("serialNumber", "S.No");
			table.setColumnHeader("chkSelect", "");
			table.setColumnHeader("viewdetails", "");
			table.setColumnHeader("intimationNo", "Intimation No");
			table.setColumnHeader("maApprovedDate", "MA Approved Date");
			table.setColumnHeader("cpucode", "Cpu Code");
			table.setColumnHeader("cpuName", "Cpu Name");
			table.setColumnHeader("paymentStatus", "Payment Status");
			table.setColumnHeader("paymentCpuCode", "Payment Cpu Code");
			table.setColumnHeader("paymentCpuName", "Payment Cpu Name");
			table.setColumnHeader("proposerName", "Proposer Name");
			table.setColumnHeader("employeeName", "Employee Name");
			table.setColumnHeader("payeeName", "Payee Name");
			table.setColumnHeader("paymentType", "Payment Type");
			table.setColumnHeader("reasonForChange", "Reason for Change");
			table.setColumnHeader("ifscCode", "IFSC Code");
			table.setColumnHeader("beneficiaryAcno", "Beneficiary Acct No");
			table.setColumnHeader("bankName", "Bank Name");
			table.setColumnHeader("branchName", "Branch Name");
			table.setColumnHeader("branchCity", "Branch City");
			table.setColumnHeader("payableCity", "Payable City");
			table.setColumnHeader("emailID", "Email ID");
			table.setColumnHeader("typeOfClaim","Type of Claim");
			//table.setColumnHeader("zonalMailId", "Zonal Email ID");
			table.setColumnHeader("approvedAmt", "Approved Amount");
			table.setColumnHeader("productName", "Product Name");
			table.setEditable(true);	
		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
			table.setTableFieldFactory(new ImmediateFieldFactory());
		
			table.setFooterVisible(false);
		//table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	}
	
	public void manageListeners() {

		
	}
		
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			UpdatePaymentDetailTableDTO initiateDTO = (UpdatePaymentDetailTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(initiateDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(initiateDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(initiateDTO);
			}
						
			if("intimationNo".equals(propertyId)) {
				
				TextField field = new TextField();
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
			} 
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
			else if ("maApprovedDate".equals(propertyId)) {
				TextField field = new TextField();
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
				tableRow.put("maApprovedDate", field);

				return field;
			}
			else if ("cpucode".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				tableRow.put("cpucode", field);
			
				return field;
			}
			else if ("cpuName".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("cpuName", field);
				
				return field;
			}
			else if("paymentCpuCode".equals(propertyId)) {
				ComboBox field = new ComboBox();
				field.setContainerDataSource(cpucode);
				//field.setMaxLength(6);
				if(initiateDTO.getPaymentCpuCodeString()!=null){
					getPaymentCpuCode(initiateDTO);
				}
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.addValueChangeListener(paymentCpuCodeListener());
				tableRow.put("paymentCpuCode", field);
				return field;
			}
			else if("paymentCpuName".equals(propertyId)) {
				TextField field = new TextField();
			//	field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("paymentCpuName", field);
				return field;
			}
			else if("proposerName".equals(propertyId)) {
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
				tableRow.put("proposerName", field);
				return field;
			}
		
			else if("employeeName".equals(propertyId)) {
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
				tableRow.put("employeeName", field);
				return field;
			}
			else if("payeeName".equals(propertyId)) {
				ComboBox field = new ComboBox();
				field.setWidth("100px");
				field.setContainerDataSource(initiateDTO.getPayeeNameList());
				//field.setMaxLength(6);
				BeanItemContainer<SelectValue> payeeNameList = initiateDTO.getPayeeNameList();
				List<SelectValue> itemIds = payeeNameList.getItemIds();
				if(itemIds!=null){
				for (SelectValue selectValue : itemIds) {
					if(selectValue.getValue().equalsIgnoreCase(initiateDTO.getPayeeNameString())){
						initiateDTO.setPayeeName(selectValue);
						field.setValue(selectValue);
						break;
					}
				}}
				if(initiateDTO.getPayeeNameList().containsId(initiateDTO.getPayeeNameString())){
					
				}
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("payeeName", field);
				return field;
			}
			else if("paymentType".equals(propertyId)) {
				ComboBox field = new ComboBox();
				field.setWidth("150px");
				//field.setMaxLength(6);
				field.setContainerDataSource(paymentMode);
				if(initiateDTO.getPaymentTypeValue()!=null){
				if(initiateDTO.getPaymentTypeValue().equalsIgnoreCase("BANK TRANSFER")){
					SelectValue idByIndex = paymentMode.getIdByIndex(0);
					field.setValue(idByIndex);
					initiateDTO.setPaymentType(idByIndex);
				}else{
					SelectValue idByIndex = paymentMode.getIdByIndex(1);
					field.setValue(idByIndex);
					initiateDTO.setPaymentType(idByIndex);
				}}
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.addValueChangeListener(ifscListener());
				tableRow.put("paymentType", field);
				return field;
			}
			else if("reasonForChange".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setWidth("150px");
				field.setNullRepresentation("");
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				changeOfReasonListener(field, null);
				field.setMaxLength(4000);
				tableRow.put("reasonForChange", field);
				return field;
			}
			else if("ifscCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("ifscCode", field);
				return field;
			}
			else if("beneficiaryAcno".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("beneficiaryAcno", field);
				return field;
			}
			else if("bankName".equals(propertyId)) {
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
				tableRow.put("bankName", field);
				return field;
			}
			else if("branchName".equals(propertyId)) {
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
				tableRow.put("branchName", field);
				return field;
			}
			else if("branchCity".equals(propertyId)) {
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
				tableRow.put("branchCity", field);
				return field;
			}
			else if("payableCity".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setNullRepresentation("");
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("payableCity", field);
				return field;
			}
			else if("emailID".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("emailID", field);
				return field;
			}
			else if("typeOfClaim".equals(propertyId)) {
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
				tableRow.put("typeOfClaim", field);
				return field;
			}
			else if("approvedAmt".equals(propertyId)) {
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
				tableRow.put("approvedAmt", field);
				return field;
			}
			else if("productName".equals(propertyId)) {
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
				tableRow.put("productName", field);
				return field;
			}
			
			/*else if("serialNo".equals(propertyId)) {
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
			}*/
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
	
	/*private void generateSlNo(TextField txtField)
	{
		
		Collection<UpdatePaymentDetailTableDTO> itemIds = (Collection<UpdatePaymentDetailTableDTO>) table.getItemIds();
		
		
		 for (UpdatePaymentDetailTableDTO billEntryDetailsDTO : itemIds) {
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
						
					  UpdatePaymentDetailTableDTO tableDTO = (UpdatePaymentDetailTableDTO) itemId;
						CheckBox chkBox = new CheckBox("");
						
						 if(null != tableDataList && !tableDataList.isEmpty())
						  {
							  for (UpdatePaymentDetailTableDTO dto : tableDataList) {
								  if(dto.getRodKey().equals(tableDTO.getRodKey()))
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
						compMap.put(tableDTO.getRodKey(), chkBox);
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
						
					final UpdatePaymentDetailTableDTO tableDTO = (UpdatePaymentDetailTableDTO)itemId;
					
					Button button = new Button("View Documents");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
						
							showClaimsDMSView(tableDTO.getIntimationNo());
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
					
					List<UpdatePaymentDetailTableDTO> serialNoList = (List<UpdatePaymentDetailTableDTO>)source.getContainerDataSource().getItemIds();
					
					UpdatePaymentDetailTableDTO dto = (UpdatePaymentDetailTableDTO)itemId;
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
						
					final UpdatePaymentDetailTableDTO tableDTO = (UpdatePaymentDetailTableDTO)itemId;	
					
					
					com.vaadin.v7.ui.TextField txtRemarks = new com.vaadin.v7.ui.TextField();
					
					txtRemarks.addValueChangeListener(new ValueChangeListener() {
						
						
						
						@Override
						public void valueChange(Property.ValueChangeEvent event) {/*	
						
							String value = (String)event.getProperty().getValue();
							
							
							if(null != value && !(value.equals("")))
							{
								
								tableDTO.setRemarks(value);
							}
							else
							{
								tableDTO.setRemarks("");
							}
							
						*/}
					});
				
					//txtRemarks.addStyleName(ValoTheme.BUTTON_BORDERLESS);
					txtRemarks.setWidth("150px");
					txtRemarks.addStyleName(ValoTheme.BUTTON_LINK);
					/*if(null != tableDTO.getRemarks())
						txtRemarks.setValue(tableDTO.getRemarks());*/
					return txtRemarks;
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
					UpdatePaymentDetailTableDTO tableDTO = (UpdatePaymentDetailTableDTO)chkBox.getData();
					
					if(value)
					{
						tableDTO.setCheckBoxStatus("true");
						tableDTO.setRecStatusFlag("F");
						setRowColor(tableDTO);
						setFieldReadOnly(Boolean.TRUE , tableDTO);
					}else
					{
						tableDTO.setCheckBoxStatus("false");
						tableDTO.setRecStatusFlag("");
						setRowColor(tableDTO);
						setFieldReadOnly(Boolean.FALSE, tableDTO);
					}
					/**
					 * Added for issue#192.
					 * */
					if(null != tableDataList && !tableDataList.isEmpty())
					{
						for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : tableDataList) {
							
							if(UpdatePaymentDetailTableDTO.getRodKey().equals(tableDTO.getRodKey()))
							{
								if(value)
								{
									UpdatePaymentDetailTableDTO.setCheckBoxStatus("true");
									//tableDTO.setChkSelect(true);
								}
								else
								{
									UpdatePaymentDetailTableDTO.setCheckBoxStatus("false");
									//tableDTO.setChkSelect(false);
								}
							}
						}
					}
					
					
				
					
					
					/*boolean value = (Boolean) event.getProperty().getValue();
					UpdatePaymentDetailTableDTO tableDTO = (UpdatePaymentDetailTableDTO)chkBox.getData();
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

	protected void setFieldReadOnly(Boolean true1, UpdatePaymentDetailTableDTO tableDTO) {

		HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
		if(hashMap!=null){
			
			ComboBox txtPaymentCpuCode = (ComboBox)hashMap.get("paymentCpuCode");
			if(txtPaymentCpuCode!=null){
				txtPaymentCpuCode.setReadOnly(true1);
			}
			ComboBox txtPayeeName = (ComboBox)hashMap.get("payeeName");
			if(txtPayeeName!=null){
				txtPayeeName.setReadOnly(true1);
			}
			ComboBox txtPaymentType = (ComboBox)hashMap.get("paymentType");
			if(txtPaymentType!=null){
				txtPaymentType.setReadOnly(true1);
				//txtIfscCode.setReadOnly(true);
			}
			TextArea txtReasonForChange = (TextArea)hashMap.get("reasonForChange");
			if(txtReasonForChange!=null){
				txtReasonForChange.setReadOnly(true1);
				//txtIfscCode.setReadOnly(true);
			}
			TextField txtBeneficiaryAcno = (TextField)hashMap.get("beneficiaryAcno");
			if(txtBeneficiaryAcno!=null){
				txtBeneficiaryAcno.setReadOnly(true1);
				//txtIfscCode.setReadOnly(true);
			}
			TextField txtPayableCity = (TextField)hashMap.get("payableCity");
			if(txtPayableCity!=null){
				txtPayableCity.setReadOnly(true1);
				//txtIfscCode.setReadOnly(true);
			}
			TextField txtemailID = (TextField)hashMap.get("emailID");
			if(txtemailID!=null){
				txtemailID.setReadOnly(true1);
				//txtIfscCode.setReadOnly(true);
			}
			TextField txtIfscCode = (TextField)hashMap.get("ifscCode");
			if(txtIfscCode!=null){
				txtIfscCode.setReadOnly(true1);
				//txtIfscCode.setReadOnly(true);
			}
		}
	}

	/*public void setValueForCheckBox(Boolean value)
	{
		List<UpdatePaymentDetailTableDTO> searchTableDTOList = (List<UpdatePaymentDetailTableDTO>) table.getItemIds();
		
		for (UpdatePaymentDetailTableDTO searchTableDTO : searchTableDTOList) {
			//for (UpdatePaymentDetailTableDTO searchTabledto : tableDataList){
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
		List<UpdatePaymentDetailTableDTO> searchTableDTOList = (List<UpdatePaymentDetailTableDTO>) table.getItemIds();
		
		for (UpdatePaymentDetailTableDTO searchTableDTO : searchTableDTOList) {
			for (UpdatePaymentDetailTableDTO searchTabledto : tableDataList){
				if(searchTabledto.getRodKey().equals(searchTableDTO.getRodKey()))
					{
						if(value)
						{
							searchTableDTO.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getRodKey());
								chkBox.setValue(value);
							}
							break;
						}
						else
						{
							searchTableDTO.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getRodKey());
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
		List<UpdatePaymentDetailTableDTO> searchTableDTOList = (List<UpdatePaymentDetailTableDTO>) table.getItemIds();
		
		
			for (UpdatePaymentDetailTableDTO searchTabledto : tableDataList){
				for (UpdatePaymentDetailTableDTO searchTableDTO : searchTableDTOList) {
				if(searchTabledto.getRodKey().equals(searchTableDTO.getRodKey()))
					{
						if(value)
						{
							searchTabledto.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getRodKey());
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
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getRodKey());
								if(null != chkBox)
									chkBox.setValue(value);
							}
							break;
						}
						
					}
			}
		}
		}
	
			
		
	public void setFinalTableList(List<UpdatePaymentDetailTableDTO> tableRows) {
		Boolean isListEmpty = false;
//		tableDataList = new ArrayList<UpdatePaymentDetailTableDTO>();
	//	List<UpdatePaymentDetailTableDTO> dtoList = new ArrayList<UpdatePaymentDetailTableDTO>();
		if(null != tableRows && !tableRows.isEmpty())
		{
			for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : tableRows) {
				
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
					if(!tableDataKeyList.contains(UpdatePaymentDetailTableDTO.getRodKey()))
							{
								tableDataList.add(UpdatePaymentDetailTableDTO);
								tableDataKeyList.add(UpdatePaymentDetailTableDTO.getRodKey());
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
				for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : tableRows) {
					tableDataList.add(UpdatePaymentDetailTableDTO);
					tableDataKeyList.add(UpdatePaymentDetailTableDTO.getRodKey());
				}
				
			}
			/*if(null != dtoList && !dtoList.isEmpty()){
				for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : dtoList) {
					tableDataList.add(UpdatePaymentDetailTableDTO);
				}
			}*/
		}
		// TODO Auto-generated method stub
		
	}
	
	public List<UpdatePaymentDetailTableDTO> getTableItems()
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
			
			
			
			for (UpdatePaymentDetailTableDTO dto : tableDataList) {	
				
				
			if(null != dto.getNoOfDiffDays() &&  dto.getNoOfDiffDays() >= -3 && (null ==dto.getRemarks() || ("").equalsIgnoreCase(dto.getRemarks())))
			{
				 err += "\nPlease Enter remarks for the Intimation Number:" + dto.getIntimationNo() +"<br>";
			}
			
		}
			
	}*/
		
		List<UpdatePaymentDetailTableDTO> requestTableList = (List<UpdatePaymentDetailTableDTO>) table.getItemIds();
		List<UpdatePaymentDetailTableDTO> finalListForProcessing = null;
		if(null != requestTableList && !requestTableList.isEmpty())
		{
			finalListForProcessing = new ArrayList<UpdatePaymentDetailTableDTO>();
			for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : requestTableList) {
					
				if(null != UpdatePaymentDetailTableDTO.getChkSelect())
				{
				if(true==UpdatePaymentDetailTableDTO.getChkSelect())
				{
					finalListForProcessing.add(UpdatePaymentDetailTableDTO);
				}				
				}
				}
		}
		if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
		{
			for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : finalListForProcessing) {/*
				
		//if(null != UpdatePaymentDetailTableDTO.getNoOfDiffDays() &&  UpdatePaymentDetailTableDTO.getNoOfDiffDays() >3 && (null ==UpdatePaymentDetailTableDTO.getRemarks() || ("").equalsIgnoreCase(UpdatePaymentDetailTableDTO.getRemarks())))
				if((!( UpdatePaymentDetailTableDTO.getNoOfDaysExceedingforCalculation().equals(UpdatePaymentDetailTableDTO.getNoofdaysexceeding())) || !(UpdatePaymentDetailTableDTO.getInterestAmntForCalculation().equals(UpdatePaymentDetailTableDTO.getIntrestAmount())))
						&& (null ==UpdatePaymentDetailTableDTO.getRemarks() || ("").equalsIgnoreCase(UpdatePaymentDetailTableDTO.getRemarks())))
			{
					 err.append("\nPlease Enter remarks for the Intimation Number:").append(UpdatePaymentDetailTableDTO.getIntimationNo()).append("<br>");
				}
			*/}
			
		}
		return err.toString();
	}
	

	 public void addBeanToList(UpdatePaymentDetailTableDTO createLotDTO) {
	    	data.addBean(createLotDTO);
	 }
	 
	 public void addList(List<UpdatePaymentDetailTableDTO> createLotDTO) {
		 for (UpdatePaymentDetailTableDTO createandSearchLotDto : createLotDTO) {
			 data.addBean(createandSearchLotDto);
		 }
	 }
	 
	 @SuppressWarnings("unchecked")
	public List<UpdatePaymentDetailTableDTO> getValues() {
		 @SuppressWarnings("unchecked")
			List<UpdatePaymentDetailTableDTO> itemIds = (List<UpdatePaymentDetailTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
    	
	}

	public void setTableList(List<UpdatePaymentDetailTableDTO> tableItems,
			String createBatch) {
		table.removeAllItems();
		//List<UpdatePaymentDetailTableDTO> tableItems = tableRows.getPageItems();
		if(null != tableItems && !tableItems.isEmpty())
		{
			for (UpdatePaymentDetailTableDTO UpdatePaymentDetailTableDTO : tableItems) {
				
				table.addItem(UpdatePaymentDetailTableDTO);
			}
			
			
			
		}
		
		
	}
	
	public void setTotalNoOfRecords(int iSize)
	{
		fLayout.removeAllComponents();
		totalRocordsTxt = new Label(String.valueOf(iSize));
		totalRocordsTxt.setCaption("Total Number Of Records :");
		fLayout.addComponent(totalRocordsTxt);

	}
	
	public void sortTableList()
	{
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	}
	public void showClaimsDMSView(String intimationNo)
	{
		fireViewEvent(UpdatePaymentDetailPresenter.CREATE_BATCH_SHOW_VIEW_DOCUMENTS,intimationNo);
		//fireViewEvent(CreateAndSearchLotPresenter.SHOW_VIEW_DOCUMENTS,intimationNo);
	}
	public void showEditPaymentDetailsView(UpdatePaymentDetailTableDTO tableDTO)
	{
		fireViewEvent(UpdatePaymentDetailPresenter.CREATE_BATCH_SHOW_EDIT_PAYMENT_DETAILS_VIEW,tableDTO);
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
			List<UpdatePaymentDetailTableDTO> tableList = (List<UpdatePaymentDetailTableDTO>)table.getItemIds();
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
		createandSearchLotDto = (UpdatePaymentDetailTableDTO)selectedRowId;
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				createandSearchLotDto = (UpdatePaymentDetailTableDTO)selectedRowId;
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
	
	
public void setRowColor(final UpdatePaymentDetailTableDTO dto){
		
		
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				
				UpdatePaymentDetailTableDTO dto1 = (UpdatePaymentDetailTableDTO) itemId;
				if(dto1 != null){
				String colourFlag = null != dto1.getRecStatusFlag() ? dto1.getRecStatusFlag():"";
				if(colourFlag.equals("E")){
					
					return "yellow";
				}
				else if(colourFlag.equals("F")){
					
					return "select";
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
			createandSearchLotDto = (UpdatePaymentDetailTableDTO)id;		
			return id;
			 
		}		
		return null;
	}*/
	
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,EditPaymentDetailsView editPaymentView) {
		editPaymentView.populatePreviousPaymentDetails(tableDTO);
		
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
					if(value.getValue()!=null && value.getValue().equalsIgnoreCase("NEFT")){
						
						UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO = (UpdatePaymentDetailTableDTO)paymentTypeCmb.getData();
		 				
						Window popup = new com.vaadin.ui.Window();
						viewSearchCriteriaWindow.setWindowObject(popup);
						viewSearchCriteriaWindow.setPresenterString(SHAConstants.UPDATE_PAYMENT_DETAIL,updatePaymentDetailTableDTO);
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
					}}
				}
		};
		return listener;
		
	}
	
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO dto, UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO) {
		
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
		
		}
	}

	public void setDropDownValues(BeanItemContainer<SelectValue> paymentStatus, BeanItemContainer<SelectValue> cpuCode) {
		this.paymentMode=paymentStatus;
		this.cpucode = cpuCode;
	}
	
	public ValueChangeListener paymentCpuCodeListener(){
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
					UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO = (UpdatePaymentDetailTableDTO)paymentTypeCmb.getData();
					fireViewEvent(UpdatePaymentDetailPresenter.GET_PAYMENT_CPU_NAME,value.getValue(),updatePaymentDetailTableDTO);
				}
				}
		};
		return listener;
		
	}

	public void setPaymentCpuName(
			UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO) {
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(updatePaymentDetailTableDTO);
		if(hashMap!=null){
			TextField paymentCpuName = (TextField)hashMap.get("paymentCpuName");
			if(paymentCpuName!=null){
				paymentCpuName.setReadOnly(false);
				paymentCpuName.setValue(updatePaymentDetailTableDTO.getPaymentCpuName());
				paymentCpuName.setReadOnly(true);
			}
			ComboBox paymentCpu = (ComboBox)hashMap.get("paymentCpuCode");
			
			if(paymentCpu!=null){
				paymentCpu.setReadOnly(false);
				paymentCpu.setValue(updatePaymentDetailTableDTO.getPaymentCpuCode());
			}
		}
		
	}
	
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
				UpdatePaymentDetailTableDTO mainDto = (UpdatePaymentDetailTableDTO)txtFld.getData();
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
							UpdatePaymentDetailTableDTO mainDto = (UpdatePaymentDetailTableDTO)txtFld.getData();
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
	
	private void getPaymentCpuCode(UpdatePaymentDetailTableDTO initiateDTO) {
		fireViewEvent(UpdatePaymentDetailPresenter.GET_PAYMENT_CPU ,initiateDTO);
		
	}
	

}