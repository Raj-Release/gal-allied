package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPNegotiationDetailsDTO;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class OMPNewRRecoverableDetailTable  extends ViewComponent{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private Map<OMPNewRecoverableTableDto, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OMPNewRecoverableTableDto, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<OMPNewRecoverableTableDto> container = new BeanItemContainer<OMPNewRecoverableTableDto>(OMPNewRecoverableTableDto.class);
	
	private Table table;

	private Button btnAdd;
	
	private Set<String> errorMessages;
	
	private  Validator validator;
	
	private Map<String, Object> referenceData;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	
	private String presenterString = "";
	
	private ViewDetails objViewDetails;

	private OMPClaimCalculationViewTableDTO bean;
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init(OMPClaimCalculationViewTableDTO bean) {
		this.bean=bean;
	//	populateBillDetails(bean);
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new HashSet<String>();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		
		//VerticalLayout layout = new VerticalLayout();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("90%");
		table.setHeight("150px");
		table.setPageLength(table.getItemIds().size());
		//table.setSizeFull();
		table.setSortDisabled(true);
		addListener();
		
		layout.addComponent(table);
	//	layout.addComponent(btnAdd);
		layout.setComponentAlignment(table, Alignment.TOP_RIGHT);
		//layout.setComponentAlignment(btnAdd,Alignment.TOP_LEFT);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(layout);
		horLayout.setComponentAlignment(layout, Alignment.TOP_RIGHT);
		
		Panel tblPanel = new Panel();
		tblPanel.setWidth("90%");
		tblPanel.setHeight("200px");
		tblPanel.setContent(horLayout);
		
		//horLayout.setWidth("100%");
		
	/*	Panel tablePanel = new Panel();
		tablePanel.setContent(horLayout);
		tablePanel.setWidth("91%");*/
		setCompositionRoot(tblPanel);
		//setCompositionRoot(horLayout);
	}
	
	public void setTableList(List<OMPNewRecoverableTableDto> list) {
		table.removeAllItems();
		if(list!=null){
		for (OMPNewRecoverableTableDto bean : list) {
			table.addItem(bean);
		}}
		table.sort();
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				OMPNewRecoverableTableDto ompOMPNewRecoverableTableDto = new OMPNewRecoverableTableDto();
				List<OMPNewRecoverableTableDto> dtoList = (List<OMPNewRecoverableTableDto>)table.getItemIds();
				if(dtoList!= null && dtoList.size()>0){
					btnAdd.setEnabled(Boolean.FALSE);
				}else{
					btnAdd.setEnabled(Boolean.TRUE);
					BeanItem<OMPNewRecoverableTableDto> addItem = container.addItem(ompOMPNewRecoverableTableDto);
					btnAdd.setEnabled(Boolean.FALSE);
				}
					manageListeners();
			}
		});
	}
	
	public void manageListeners() {
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		table.setVisibleColumns(new Object[] { "serialNumber", "dateofRecovery", /*"nameOfNegotiatior",*/"amountRecoveredInr","amountRecoveredUsd","remarks","sendToAccounts"});
		table.setColumnHeader("serialNumber", "Sl No");
		table.setColumnHeader("dateofRecovery", "Date of Recovery");
//		table.setColumnHeader("negotiationCompletDate", "Negotiation Completed Date");
//		table.setColumnHeader("nameOfNegotiatior", "Name of Negotiator");
		table.setColumnHeader("amountRecoveredInr", "Amount Recovered(INR)");
		table.setColumnHeader("amountRecoveredUsd", "Amount Recovered($)");
		table.setColumnHeader("remarks", "Remarks");
//		table.setColumnHeader("rodStatus", "ROD </br> status ");
//		table.setColumnHeader("select", "Select");
		table.setEditable(true);
		
		List<OMPNewRecoverableTableDto> itemIds =  (List<OMPNewRecoverableTableDto>) table.getItemIds();
		bean.setOmpRecoverableTableList(itemIds);
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		table.removeGeneratedColumn("sendToAccounts");
		table.addGeneratedColumn("sendToAccounts", new Table.ColumnGenerator() {
			
			//CheckBox chkbox = new CheckBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				OMPNewRecoverableTableDto rodDTO = (OMPNewRecoverableTableDto) itemId;
				if(rodDTO != null){
				
					final CheckBox chkBox = new CheckBox();
					chkBox.setData(rodDTO);
					//tableItemchk.put(rodDTO, chkBox);
					if(rodDTO.getSendToAccounts()!=null && rodDTO.getSendToAccounts().equalsIgnoreCase("Y")){
						chkBox.setValue(Boolean.TRUE);
					}
					 if(bean.getIsReadOnlyRecoverable()){
						 chkBox.setEnabled(Boolean.FALSE);
					 }
					if(bean.getScreenName()!=null && bean.getScreenName().equalsIgnoreCase(SHAConstants.OMP_PROCESSOR)){
						chkBox.setEnabled(Boolean.FALSE);
					}
						/*if(rodDTO.getSendforApprover()!= null && rodDTO.getSendforApprover().equalsIgnoreCase("Y") ||rodDTO.getReject()!= null && rodDTO.getReject().equalsIgnoreCase("Y")){
							chkBox.setEnabled(Boolean.FALSE);
						}*/
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								CheckBox property = (CheckBox) event.getProperty();
								Boolean value = (Boolean) event.getProperty().getValue();
								if(value != null){
									OMPNewRecoverableTableDto calculationViewTableDTO = (OMPNewRecoverableTableDto) itemId;
								   if(!bean.getIsReadOnlyRecoverable()){
									   if(value){
										   calculationViewTableDTO.setSendToAccounts("Y");
									   }else{
										   calculationViewTableDTO.setSendToAccounts("N");
									   }
								   }
								}
								
							}
						});
						return chkBox;
				}else {
					return null;
				}
		}
		});
		table.setColumnHeader("sendToAccounts", "Send To Accounts");
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			OMPNewRecoverableTableDto entryDTO = (OMPNewRecoverableTableDto) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				if ("serialNumber".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("50px");
					field.setMaxLength(5);
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("serialNumber", field);
					return field;
				}	
				
				else if ("dateofRecovery".equals(propertyId)) {
				DateField field = new DateField();
				field.setWidth("250px");
				field.setDateFormat("dd/MM/yyyy");
				//field.setWidth("125px");
				//field.setEnabled(false);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(entryDTO);
//				field.addValueChangeListener(saveDataListener());
				field.setReadOnly(bean.getIsReadOnlyRecoverable());
				//field.setReadOnly(true);
				tableRow.put("dateofRecovery", field);
				return field;
			}
			
			else if ("amountRecoveredInr".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
//				field.setReadOnly(true);
				field.setMaxLength(10);
				field.setData(entryDTO);
				field.setReadOnly(bean.getIsReadOnlyRecoverable());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("amountRecoveredInr", field);

				return field;
			}else if ("amountRecoveredUsd".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setMaxLength(10);
				field.setData(entryDTO);
				field.setReadOnly(bean.getIsReadOnlyRecoverable());
				//field.setReadOnly(true);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("amountRecoveredUsd", field);

				return field;
			}else if ("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setMaxLength(4000);
				//field.addValueChangeListener(saveDataListener());
				field.setData(entryDTO);
				field.setReadOnly(bean.getIsReadOnlyRecoverable());
				/*if(entryDTO.getClaimKey()!=null){
					field.setReadOnly(true);
				}else{
					field.setReadOnly(false);
				}*/
				//field.setReadOnly(true);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				generateSlNo(field);
				tableRow.put("remarks", field);
//				generateSlNo(field);
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

		private ValueChangeListener saveDataListener() {
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField copayPercentage = (TextField) event.getProperty();
					OMPNewRecoverableTableDto calculationViewTableDTO = (OMPNewRecoverableTableDto)copayPercentage.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
	 				TextField recoverDate = (TextField)hashMap.get("remarks");
	 				if(recoverDate!= null && calculationViewTableDTO!= null){
	 					List<OMPNewRecoverableTableDto> dtolist = new ArrayList<OMPNewRecoverableTableDto>();
	 					dtolist.add(calculationViewTableDTO);
	 					bean.setOmpRecoverableTableList(dtolist);
	 						fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROSESS_RECOVERABLE_SAVE, bean);
	 					}
				}
			};
			return listener;
		}
	}
	
	 private void generateSlNo(TextField txtField)
		{
			
			Collection<OMPNewRecoverableTableDto> itemIds = (Collection<OMPNewRecoverableTableDto>) table.getItemIds();
			
			int i = 0;
			 for (OMPNewRecoverableTableDto calculationViewTableDTO : itemIds) {
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

	 public List<OMPNewRecoverableTableDto> getValues() {
	    	@SuppressWarnings("unchecked")
			List<OMPNewRecoverableTableDto> itemIds = (List<OMPNewRecoverableTableDto>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	 public Set<String> validateCalculation() {
//			Boolean hasError = false;
//			showOrHideValidation(true);
			errorMessages.removeAll(getErrors());
			List<OMPNewRecoverableTableDto> itemIds = (List<OMPNewRecoverableTableDto>) table.getItemIds();
			
			if(null != itemIds && !itemIds.isEmpty())
			{
				for (OMPNewRecoverableTableDto bean : itemIds) {
					if(bean.getAmountRecoveredUsd() == null){
	 					errorMessages.add("Please Enter Amount Recovered($)");
	 					//break;
					}
					if(bean.getAmountRecoveredInr()==null){
						errorMessages.add("Please Enter Amount Recovered(INR)");
					}
					if(bean.getDateofRecovery() == null){
						errorMessages.add("Please select Date of Recovery");
					}
					
					if(bean.getRemarks() == null){
						errorMessages.add("Please Enter Remarks");
					}else if(bean.getRemarks().length()<=0){
						errorMessages.add("Please Enter Remarks");
					}
				}
				
				}else{
//					errorMessages.add("Please Add Bill Details");
				}
				return errorMessages;
			
			}

		public Set<String> getErrors()
			{
				return this.errorMessages;
			}
	 
	 
	 
}
