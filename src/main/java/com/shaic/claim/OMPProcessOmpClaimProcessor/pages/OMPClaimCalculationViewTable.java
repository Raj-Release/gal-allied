package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OMPClaimCalculationViewTable extends ViewComponent {
	
	private Map<OMPClaimCalculationViewTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OMPClaimCalculationViewTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<OMPClaimCalculationViewTableDTO> container = new BeanItemContainer<OMPClaimCalculationViewTableDTO>(OMPClaimCalculationViewTableDTO.class);
	
	private static Validator validator;
	
	private List<String> errorMessages;
	
	private Button btnAdd;
	
	private Table table;
	
	private Map<String, Object> referenceData;
	
	public TextField dummyField;
	
	public TextField dummyFieldBillAmt;
	
	public TextField dummyFieldINRTotal;

	private OMPClaimProcessorDTO bean;

	public TextField classification ;
	
	public TextField dropDownDummy ;
	
	public TextField dummyFieldExpTotal;
	
	private List<OMPClaimCalculationViewTableDTO> deletedList;
	//public TextField dummyField;
	
	//public TextField dummyField;
	
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"category", "billAmt", "amtIn", "deduction",
			"totalAmt", "totalAmtInr", "approvedAmt",
			"agreedAmt", "differenceAmt", "expenses","negotiationClaimed",
			"negotiationCapping","handlingCharges","totalExp"};*/

	/*@Override
	public void removeRow() {
		table.removeAllItems();
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<OMPClaimCalculationViewTableDTO>(
				OMPClaimCalculationViewTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("295px");
		

	}*/
	

	public void init(OMPClaimProcessorDTO bean) {
		//	populateBillDetails(bean);
			this.bean = bean;
			container.removeAllItems();
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			this.errorMessages = new ArrayList<String>();
			HorizontalLayout btnLayout = buildButtonLayout();
			btnLayout.setWidth("100%");
			btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
			
			dummyField = new TextField();
			dummyFieldBillAmt = new TextField();
			dummyFieldINRTotal = new TextField();
			classification = new TextField();
			dropDownDummy = new TextField();
			dummyFieldExpTotal = new TextField();
			deletedList = new ArrayList<OMPClaimCalculationViewTableDTO>();
			//VerticalLayout layout = new VerticalLayout();
			HorizontalLayout layout = new HorizontalLayout();
			layout.setSpacing(true);
			//layout.setMargin(true);
			//layout.addComponent(btnLayout);
			
			initTable();
			table.setWidth("90%");
			table.setHeight("150px");
			table.setPageLength(table.getItemIds().size());
			//table.setSizeFull();
			
			
			layout.addComponent(table);
		//	layout.addComponent(btnAdd);
			layout.setComponentAlignment(table, Alignment.TOP_RIGHT);
			//layout.setComponentAlignment(btnAdd,Alignment.TOP_LEFT);
			
			HorizontalLayout horLayout = new HorizontalLayout();
			horLayout.addComponent(btnLayout);
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
			currencyRateListener();
			dropDownListener();
			//setCompositionRoot(horLayout);
			readOnlyListener();
		}
	
	

	/*@Override
	public String textBundlePrefixString() {
		return "ompclaimcalculationviewtable-";
	}*/
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		table.setVisibleColumns(new Object[] {"serialNumber","category", "billAmt", "amtIn", "deduction","totalAmt", "totalAmtInr", "approvedAmt","agreedAmt", "differenceAmt", "expenses","negotiationClaimed",
				"negotiationCapping","negotiationPayable","handlingCharges","totalExp"});
		table.setColumnHeader("serialNumber", "S.No");
		table.setColumnHeader("category", "Category");
		table.setColumnHeader("billAmt", "Bill amt (in FC)");
		table.setColumnHeader("amtIn", "Amt in $");
		table.setColumnHeader("deduction", "Deduction (Non Payables) $");
		table.setColumnHeader("totalAmt", "Total amount in $");
		table.setColumnHeader("totalAmtInr", "Total amount in INR");
		table.setColumnHeader("approvedAmt", "Approved Amount ($)");
		table.setColumnHeader("agreedAmt", "Agreed Amount ($)");
		table.setColumnHeader("differenceAmt", "Difference Amount ($)");
		table.setColumnHeader("expenses", "Expenses ($)");
		table.setColumnHeader("negotiationClaimed", "Negotiation fees claimed($)");
		table.setColumnHeader("negotiationCapping", "Negotiation Fee Capping($)");
		table.setColumnHeader("negotiationPayable", "Negotiation fees payable($)");
		table.setColumnHeader("handlingCharges", "Handling Charges ($)");
		table.setColumnHeader("totalExp", "Total Expence $");
		table.setEditable(true);
		table.setFooterVisible(true);
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				//final Button deleteButton = new Button("Delete");
				final Button deleteButton = new Button();
				//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				deleteButton.setIcon(FontAwesome.TRASH_O);
				deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				deleteButton.setWidth("-1px");
				deleteButton.setHeight("-10px");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO =  (OMPClaimCalculationViewTableDTO) currentItemId;
						deletedList.add(calculationViewTableDTO);
						table.removeItem(currentItemId);
						setFooter();
					}
					
				});

				return deleteButton;
			}
		});
	
		table.setTableFieldFactory(new ImmediateFieldFactory());
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
	}
	private void setFooter() {
		if(table.getItemIds()!=null && table.getItemIds().size()<1){
			btnAdd.setEnabled(true);
			table.setColumnFooter("billAmt", String.valueOf("0.0"));
			table.setColumnFooter("amtIn", String.valueOf("0"));
			table.setColumnFooter("deduction", String.valueOf("0"));
			table.setColumnFooter("totalAmt", String.valueOf("0.0"));
			table.setColumnFooter("totalAmtInr", String.valueOf("0"));
			table.setColumnFooter("totalExp", String.valueOf("0"));
			dummyFieldBillAmt.setValue(String.valueOf("0.0"));
			dummyFieldINRTotal.setValue(String.valueOf("0"));
			dummyFieldExpTotal.setValue(String.valueOf("0"));
		}
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
//		int length =table.getPageLength();
//		if(length>=7){
//			table.setPageLength(7);
//		}
		
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			OMPClaimCalculationViewTableDTO entryDTO = (OMPClaimCalculationViewTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				
			if ("serialNumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				onlyNumber(field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setEnabled(false);
				tableRow.put("serialNumber", field);
				return field;
			}
			else if ("category".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				addValuesForValueDropDown(field);
				field.setValue(entryDTO.getCategory());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("category", field);

				return field;
			}else if ("billAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				onlyNumber(field);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(billAmtListener());
				tableRow.put("billAmt", field);

				return field;
			}else if ("amtIn".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				onlyNumber(field);
				field.addValueChangeListener(amtInDollorListener());
				field.setReadOnly(Boolean.TRUE);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("amtIn", field);

				return field;
			}
			else if ("deduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				onlyNumber(field);
				field.addValueChangeListener(deductionListener());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("deduction", field);

				return field;
			}else if ("totalAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setData(entryDTO);
				onlyNumber(field);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("totalAmt", field);

				return field;
			}else if ("totalAmtInr".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				onlyNumber(field);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.addValueChangeListener(totalAmtInrListener());
				tableRow.put("totalAmtInr", field);

				return field;
			}
			else if ("approvedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				onlyNumber(field);
				field.setData(entryDTO);
				field.addValueChangeListener(approvedListener());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("approvedAmt", field);
				return field;
			}
			else if("agreedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				field.addValueChangeListener(agreedListener());
				tableRow.put("agreedAmt", field);
				return field;
			}
			else if("differenceAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				field.addValueChangeListener(differenceListener());
				tableRow.put("differenceAmt", field);
				return field;
			}
			else if("expenses".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				field.addValueChangeListener(negoListener());
				tableRow.put("expenses", field);
				return field;
			}
			else if("negotiationClaimed".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				field.addValueChangeListener(negoListener());
				tableRow.put("negotiationClaimed", field);
				return field;
			}
			else if("negotiationCapping".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				field.setValue(SHAConstants.OMP_NEGO_CAPPING);
				
				tableRow.put("negotiationCapping", field);
				return field;
			}else if("negotiationPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				field.addValueChangeListener(totalpayableListener());
				tableRow.put("negotiationPayable", field);
				return field;
			}else if("handlingCharges".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				onlyNumber(field);
				field.setNullRepresentation("");
				field.setData(entryDTO);
				field.setValue(SHAConstants.OMP_HANDLING_CHARGES);
				field.addValueChangeListener(handlingpayableListener());
				tableRow.put("handlingCharges", field);
				return field;
			}else if("totalExp".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				tableRow.put("totalExp", field);
				field.addValueChangeListener(totalAmtInrListener());
				generateSlNo(field);
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

		private void onlyNumber(TextField field) {
			CSValidator validator = new CSValidator();
			validator.extend(field);
			validator.setRegExp("^[0-9.]*$");
			validator.setPreventInvalidTyping(true);
		}
	}
	
	public void setTableList(final List<OMPClaimCalculationViewTableDTO> list) {
		table.removeAllItems();
		if(list != null && !list.isEmpty()){
			for (final OMPClaimCalculationViewTableDTO bean : list) {
				table.addItem(bean);
			}
			table.sort();
		}
	}
	
	/*public List<OMPClaimCalculationViewTableDTO> getDeletedBillList()
	{
		return deletedList;
	}*/
	
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	private HorizontalLayout buildButtonLayout()
	{
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		addAddBtnListener();
		
		return btnLayout;
	}

	private void addAddBtnListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				
				OMPClaimCalculationViewTableDTO docDTO = new OMPClaimCalculationViewTableDTO();
				List<OMPClaimCalculationViewTableDTO> dtoList = (List<OMPClaimCalculationViewTableDTO>)table.getItemIds();
				
					if(classification.getValue()!=null ){
						if(!classification.getValue().equalsIgnoreCase("OMP Claim Related")){
							if(dtoList.size()<1){
									String value = classification.getValue();
								 classification.setValue(null);
								 classification.setValue(value);
								BeanItem<OMPClaimCalculationViewTableDTO> addItem = container.addItem(docDTO);
							}
						}else{
							String value = classification.getValue();
							 classification.setValue(null);
							 classification.setValue(value);
							BeanItem<OMPClaimCalculationViewTableDTO> addItem = container.addItem(docDTO);
						}
					}else{
						String value = classification.getValue();
						 classification.setValue(null);
						 classification.setValue(value);
						BeanItem<OMPClaimCalculationViewTableDTO> addItem = container.addItem(docDTO);
					}
					
				//dtoList.add(new DocumentCheckListDTO());
				
				if(null != dtoList && !dtoList.isEmpty())
				{ 
					/*for (DocumentCheckListDTO documentCheckListDTO : dtoList) {
						addBeanToList(documentCheckListDTO);
					}*/
					//addBeanToList(new DocumentCheckListDTO());
					int iSize = dtoList.size();
					OMPClaimCalculationViewTableDTO dto = dtoList.get(iSize-1);
					doReadOnly(classification, dto);
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					//final TextField txtFld = (TextField) combos.get("particulars");
					if(combos!=null){
						final GComboBox txtFld = (GComboBox) combos.get("category");
						txtFld.focus();
					}
				}
				
			/*	BeanItem<DocumentCheckListDTO> addItem = container.addItem(new DocumentCheckListDTO());
				if(null != dtoList && !dtoList.isEmpty())
				{
					int iSize = dtoList.size();
					DocumentCheckListDTO dto = dtoList.get(iSize-1);
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					//final TextField txtFld = (TextField) combos.get("particulars");
					final ComboBox txtFld = (ComboBox) combos.get("particulars");
					txtFld.focus();
				}*/
				
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
			}
		});
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	 @SuppressWarnings("unchecked")
		public void addValuesForValueDropDown(GComboBox comboBox) {
			BeanItemContainer<SelectValue> categoryContainer = (BeanItemContainer<SelectValue>) referenceData
					.get("category");
			
			//BeanItemContainer<SelectValue> finalContainer = billClassificationContainer;
			/*for(int i = 0 ; i<billClassificationContainer.size() ; i++)
			 {
				if (("Hospital").equalsIgnoreCase(billClassificationContainer.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}*/
			
			comboBox.setContainerDataSource(categoryContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

		}
	 
	 private void generateSlNo(TextField txtField)
		{
			
			Collection<OMPClaimCalculationViewTableDTO> itemIds = (Collection<OMPClaimCalculationViewTableDTO>) table.getItemIds();
			
			int i = 0;
			 for (OMPClaimCalculationViewTableDTO calculationViewTableDTO : itemIds) {
				 String value = classification.getValue();
				 classification.setValue(null);
				 classification.setValue(value);
				 i++;
				 doReadOnly(classification, calculationViewTableDTO);
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
	 
	 private void currencyRateListener()
		{
			if(dummyField != null) {
				dummyField.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 7455756225751111662L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextField txtCurrencyRate = (TextField) event.getProperty();
						List<OMPClaimCalculationViewTableDTO> itemIds =  (List<OMPClaimCalculationViewTableDTO>) table.getItemIds();
						if(itemIds!=null){
							for (OMPClaimCalculationViewTableDTO calculationViewTableDTO : itemIds) {
								Double currencyRate =0d;
								Double billAmtDouble =0d;
								Double amtInDollorDouble =0d;
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
								if(hashMap!=null){
								TextField billAmt = (TextField)hashMap.get("billAmt");
								if(billAmt!=null && billAmt.getValue()!=null){
									billAmtDouble = SHAUtils.getDoubleFromStringWithComma(billAmt.getValue());
								}
								if(txtCurrencyRate!=null && txtCurrencyRate.getValue()!=null){
									currencyRate = SHAUtils.getDoubleFromStringWithComma(txtCurrencyRate.getValue());
								}
								TextField amtInDollor = (TextField)hashMap.get("amtIn");
								if(amtInDollor!=null){
									amtInDollorDouble = billAmtDouble * currencyRate;
									amtInDollor.setReadOnly(Boolean.FALSE);
									amtInDollor.setValue(amtInDollorDouble.toString());
									amtInDollor.setReadOnly(Boolean.TRUE);
								}
								}
							}
						}
		 				
						calculateTotal();
					}
				});
			}
			
			
		}
	/* public ValueChangeListener currencyRateListener(TextField txtCurrencyRate){
			ValueChangeListener listener = new ValueChangeListener() {
				
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField txtCurrencyRate = (TextField) event.getProperty();
					List<OMPClaimCalculationViewTableDTO> itemIds =  (List<OMPClaimCalculationViewTableDTO>) table.getItemIds();
					if(itemIds!=null){
						for (OMPClaimCalculationViewTableDTO calculationViewTableDTO : itemIds) {
							Double currencyRate =0d;
							Double billAmtDouble =0d;
							Double amtInDollorDouble =0d;
							HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
							TextField billAmt = (TextField)hashMap.get("billAmt");
							if(billAmt!=null && billAmt.getValue()!=null){
								billAmtDouble = SHAUtils.getDoubleValueFromString(billAmt.getValue());
							}
							if(txtCurrencyRate!=null && txtCurrencyRate.getValue()!=null){
								currencyRate = SHAUtils.getDoubleValueFromString(txtCurrencyRate.getValue());
							}
							TextField amtInDollor = (TextField)hashMap.get("amtIn");
							if(amtInDollor!=null && amtInDollor.getValue()!=null){
								amtInDollorDouble = billAmtDouble * currencyRate;
								amtInDollor.setValue(amtInDollorDouble.toString());
							}
							
						}
					}
	 				
					
				}
			};
			
			return listener;
		}*/
	 
	 public ValueChangeListener billAmtListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField billAmt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)billAmt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
	 				TextField amtIn = (TextField)hashMap.get("amtIn");
	 				Double currencyRate= 0d;
	 				if(dummyField!=null && dummyField.getValue()!=null && !dummyField.getValue().equals("")){
	 					currencyRate = SHAUtils.getDoubleFromStringWithComma(dummyField.getValue());
	 					Double billAmtDouble = SHAUtils.getDoubleFromStringWithComma(billAmt.getValue());
	 					Double amtInDollor = billAmtDouble * currencyRate;
	 					if(amtIn!=null){
	 						amtIn.setReadOnly(Boolean.FALSE);
	 						amtIn.setValue(amtInDollor.toString());
	 						amtIn.setReadOnly(Boolean.TRUE);
	 					}
	 					
	 				}else if(bean.getCurrencyRate()!=null){

	 					currencyRate = bean.getCurrencyRate();
	 					Double billAmtDouble = SHAUtils.getDoubleFromStringWithComma(billAmt.getValue());
	 					Double amtInDollor = billAmtDouble * currencyRate;
	 					if(amtIn!=null){
	 						amtIn.setReadOnly(Boolean.FALSE);
	 						amtIn.setValue(amtInDollor.toString());
	 						amtIn.setReadOnly(Boolean.TRUE);
	 					}
	 					
	 				
	 				}
	 				calculateTotal();
				}
			};
			
			return listener;
		}
	 
	 public ValueChangeListener amtInDollorListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField amtIn = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)amtIn.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField deduction = (TextField)hashMap.get("deduction");
					TextField totalAmt = (TextField)hashMap.get("totalAmt");
					Double deductionDouble = 0d;
					Double amtinDollorDouble =0d;
					Double totalAmtDouble =0d;
					if(deduction!=null && deduction.getValue()!=null){
						deductionDouble = SHAUtils.getDoubleFromStringWithComma(deduction.getValue());
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
						totalAmtDouble = amtinDollorDouble - deductionDouble;
						if(totalAmt!=null){
							totalAmt.setReadOnly(Boolean.FALSE);
							totalAmt.setValue(totalAmtDouble.toString());
							totalAmt.setReadOnly(Boolean.FALSE);
						}
					}else{
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
						if(totalAmt!=null){
							totalAmt.setReadOnly(Boolean.FALSE);
							totalAmt.setValue(amtinDollorDouble.toString());
							totalAmt.setReadOnly(Boolean.TRUE);
						}
					}
					calculateTotal();
				}
			};
			
			return listener;
		}
	 
	 public ValueChangeListener deductionListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField deduction = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)deduction.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField amtIn = (TextField)hashMap.get("amtIn");
					TextField totalAmt = (TextField)hashMap.get("totalAmt");
					Double deductionDouble = 0d;
					Double amtinDollorDouble =0d;
					Double totalAmtDouble =0d;
					if(deduction!=null && deduction.getValue()!=null){
						deductionDouble = SHAUtils.getDoubleFromStringWithComma(deduction.getValue());
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
						totalAmtDouble = amtinDollorDouble - deductionDouble;
						if(totalAmt!=null){
							totalAmt.setReadOnly(Boolean.FALSE);
							totalAmt.setValue(totalAmtDouble.toString());
							totalAmt.setReadOnly(Boolean.TRUE);
						}
					}else{
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
						if(totalAmt!=null){
							totalAmt.setReadOnly(Boolean.FALSE);
							totalAmt.setValue(amtinDollorDouble.toString());
							totalAmt.setReadOnly(Boolean.TRUE);
						}
					}
					calculateTotal();
				}
			};
			
			return listener;
		}
	 
	 	public void calculateTotal() {
			
			List<OMPClaimCalculationViewTableDTO> itemIconPropertyId = (List<OMPClaimCalculationViewTableDTO>) table.getItemIds();
			Double billAmt = 0d;
			Double amtInDollor=0d;
			Double deduction = 0d;
			Double totalAmt = 0d;
			for (OMPClaimCalculationViewTableDTO dto : itemIconPropertyId) {
			    Double billAmtDouble = dto.getBillAmt();
			    billAmt += billAmtDouble != null ? billAmtDouble : 0;
			    
			    Double amtInDollorDouble = dto.getAmtIn();
			    amtInDollor += amtInDollorDouble != null ? amtInDollorDouble : 0;
			    
			    Double deductionDouble = dto.getDeduction();
			    deduction += deductionDouble != null ? deductionDouble : 0;
			    
			    Double totalAmtDouble = dto.getTotalAmt();
			    totalAmt += totalAmtDouble != null ? totalAmtDouble : 0;
			    
			}
			table.setColumnFooter("billAmt", String.valueOf(billAmt));
			table.setColumnFooter("amtIn", String.valueOf(amtInDollor));
			table.setColumnFooter("deduction", String.valueOf(deduction));
			table.setColumnFooter("totalAmt", String.valueOf(totalAmt));
			bean.setRodProvisionAmt(totalAmt);
			dummyFieldBillAmt.setValue("0");
			dummyFieldBillAmt.setValue(String.valueOf(totalAmt));
		}
	 	
	 	public String getBillAmt(){
			 return this.table.getColumnFooter("billAmt");
		 }
	 	
	 	@SuppressWarnings("unchecked")
		public List<OMPClaimCalculationViewTableDTO> getValues() {
			List<OMPClaimCalculationViewTableDTO> itemIds = (List<OMPClaimCalculationViewTableDTO>) this.table.getItemIds() ;
			if(deletedList!=null && !deletedList.isEmpty()){
				for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : deletedList) {
					ompClaimCalculationViewTableDTO.setDeleted("Y");
				}
				deletedList.addAll(itemIds);
				return deletedList;
			}else{
				return itemIds;
			}	
		}
	 	
	 	
	 	private void readOnlyListener()
		{
			if(classification != null) {
				classification.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 7455756225751111662L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextField classification = (TextField) event.getProperty();
						List<OMPClaimCalculationViewTableDTO> itemIds =  (List<OMPClaimCalculationViewTableDTO>) table.getItemIds();
						if(itemIds!=null){
							if(classification.getValue()!=null ){
								if(!classification.getValue().equalsIgnoreCase("OMP Claim Related")){
									if(itemIds.size()<1){
										btnAdd.setEnabled(true);
									}else{
										btnAdd.setEnabled(false);
									}
								}else{
									btnAdd.setEnabled(true);
								}
							}
							
							for (OMPClaimCalculationViewTableDTO calculationViewTableDTO : itemIds) {
								doReadOnly(classification,calculationViewTableDTO);
								}
								
							}
						}

					

					
		 				
					}
				);
			}
			
			
		}
	 	
	 	private void dropDownListener()
		{
			if(dropDownDummy != null) {
				dropDownDummy.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = 7455756225751111662L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextField txtCurrencyRate = (TextField) event.getProperty();
						List<OMPClaimCalculationViewTableDTO> itemIds =  (List<OMPClaimCalculationViewTableDTO>) table.getItemIds();
						if(itemIds!=null){
							for (OMPClaimCalculationViewTableDTO calculationViewTableDTO : itemIds) {
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
								if(hashMap!=null){
									GComboBox category = (GComboBox)hashMap.get("category");
									addValuesForValueDropDown(category);
									category.setValue(calculationViewTableDTO.getCategory());
								}
							}
						}
		 				
					}
				});
			}
			
			
		}
	 	
	 	 public ValueChangeListener totalAmtInrListener(){
				ValueChangeListener listener = new ValueChangeListener() {
					
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextField deduction = (TextField) event.getProperty();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)deduction.getData();
		 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
						TextField amtIn = (TextField)hashMap.get("totalAmtInr");
						TextField totalExptxt = (TextField)hashMap.get("totalExp");
						if(amtIn!=null && amtIn.getValue()!=null){
							Double totalAmt = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
							dummyFieldINRTotal.setValue(String.valueOf(totalAmt));
							table.setColumnFooter("totalAmtInr", String.valueOf(totalAmt));
						}
						if(totalExptxt!=null && totalExptxt.getValue()!=null){
							Double totalAmt = SHAUtils.getDoubleFromStringWithComma(totalExptxt.getValue());
							dummyFieldExpTotal.setValue(String.valueOf(totalAmt));
							table.setColumnFooter("totalExp", String.valueOf(totalAmt));
						}
					}
				};
				
				return listener;
			}
			
			
	 	public ValueChangeListener approvedListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField approvedTxt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)approvedTxt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField agreedTxt = (TextField)hashMap.get("agreedAmt");
					TextField differenceAmtTxt = (TextField)hashMap.get("differenceAmt");
					Double approvedDouble = 0d;
					Double amtinDollorDouble =0d;
					Double totalAmtDouble =0d;
					if(approvedTxt!=null && approvedTxt.getValue()!=null){
						approvedDouble = SHAUtils.getDoubleFromStringWithComma(approvedTxt.getValue());
						if(agreedTxt!=null){
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(agreedTxt.getValue());
						totalAmtDouble = approvedDouble - amtinDollorDouble;
						
							differenceAmtTxt.setReadOnly(Boolean.FALSE);
							differenceAmtTxt.setValue(totalAmtDouble.toString());
							differenceAmtTxt.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 	
	 	public ValueChangeListener agreedListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField agreedAmt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)agreedAmt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField approvedTxt1 = (TextField)hashMap.get("approvedAmt");
					TextField differenceAmtTxt = (TextField)hashMap.get("differenceAmt");
					Double approvedDouble = 0d;
					Double amtinDollorDouble =0d;
					Double totalAmtDouble =0d;
					if(agreedAmt!=null && agreedAmt.getValue()!=null){
						approvedDouble = SHAUtils.getDoubleFromStringWithComma(agreedAmt.getValue());
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(approvedTxt1.getValue());
						totalAmtDouble = amtinDollorDouble - approvedDouble;
						if(approvedTxt1!=null && differenceAmtTxt!=null){
							differenceAmtTxt.setReadOnly(Boolean.FALSE);
							differenceAmtTxt.setValue(totalAmtDouble.toString());
							differenceAmtTxt.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 	
	 	public ValueChangeListener differenceListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField differenceAmtTxt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)differenceAmtTxt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField expenses = (TextField)hashMap.get("expenses");
					Double differenceDouble = 0d;
					Double amtinDollorDouble =0d;
					Double totalAmtDouble =0d;
					if(differenceAmtTxt!=null && differenceAmtTxt.getValue()!=null){
						differenceDouble = SHAUtils.getDoubleFromStringWithComma(differenceAmtTxt.getValue());
						totalAmtDouble = differenceDouble * SHAUtils.getDoubleFromStringWithComma(SHAConstants.OMP_NEGO_EXP);
						totalAmtDouble = totalAmtDouble / 100;
						if(differenceAmtTxt!=null && expenses!=null){
							expenses.setReadOnly(Boolean.FALSE);
							expenses.setValue(totalAmtDouble.toString());
							expenses.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 	
	 	public ValueChangeListener negoListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField negoPayableTxt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)negoPayableTxt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField negotiationPayable = (TextField)hashMap.get("negotiationPayable");
					TextField negotiationClaimed = (TextField)hashMap.get("negotiationClaimed");
					TextField expenses = (TextField)hashMap.get("expenses");
					
					Double differenceDouble = 0d;
					Double totalAmtDouble =0d;
					Double negoClaimedAmt =0d;
					Double expensesAmt =0d;
					if(negoPayableTxt!=null && negoPayableTxt.getValue()!=null && negotiationClaimed!=null && negotiationClaimed.getValue()!=null){
						differenceDouble = SHAUtils.getDoubleFromStringWithComma(negoPayableTxt.getValue());
						negoClaimedAmt = SHAUtils.getDoubleFromStringWithComma(negotiationClaimed.getValue());
						if(expenses!=null && expenses.getValue()!=null){
							expensesAmt = SHAUtils.getDoubleFromStringWithComma(expenses.getValue());
							totalAmtDouble = Math.min(expensesAmt, SHAUtils.getDoubleFromStringWithComma(SHAConstants.OMP_NEGO_CAPPING));
						}
						totalAmtDouble = Math.min(negoClaimedAmt, totalAmtDouble);
						if(negoPayableTxt!=null && negotiationPayable!=null){
							negotiationPayable.setReadOnly(Boolean.FALSE);
							negotiationPayable.setValue(totalAmtDouble.toString());
							negotiationPayable.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 	
	 	public ValueChangeListener totalpayableListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField negoPayableTxt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)negoPayableTxt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField totalExp = (TextField)hashMap.get("totalExp");
					TextField handlingCharges = (TextField)hashMap.get("handlingCharges");
					Double differenceDouble = 0d;
					Double totalAmtDouble =0d;
					Double handlingChargesDouble =0d;
					if(handlingCharges!=null &&negoPayableTxt!=null && negoPayableTxt.getValue()!=null ){
						differenceDouble = SHAUtils.getDoubleFromStringWithComma(negoPayableTxt.getValue());
						handlingChargesDouble = SHAUtils.getDoubleFromStringWithComma(handlingCharges.getValue());
						totalAmtDouble = differenceDouble + handlingChargesDouble ;
						if(negoPayableTxt!=null){
							totalExp.setReadOnly(Boolean.FALSE);
							totalExp.setValue(totalAmtDouble.toString());
							totalExp.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 	
	 	public ValueChangeListener handlingpayableListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField handingText = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)handingText.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField totalExp = (TextField)hashMap.get("totalExp");
					Double differenceDouble = 0d;
					Double totalAmtDouble =0d;
					if(handingText!=null && handingText.getValue()!=null){
						differenceDouble = SHAUtils.getDoubleFromStringWithComma(handingText.getValue());
						if(calculationViewTableDTO.getNegotiationPayable()!=null){
							totalAmtDouble = differenceDouble + calculationViewTableDTO.getNegotiationPayable() ;
						}
						if(totalExp!=null){
							totalExp.setReadOnly(Boolean.FALSE);
							totalExp.setValue(totalAmtDouble.toString());
							totalExp.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 	
	 	private void doReadOnly(
				TextField classification,
				OMPClaimCalculationViewTableDTO calculationViewTableDTO) {
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
			if(hashMap!= null && classification.getValue()!=null && classification.getValue().equalsIgnoreCase("OMP Claim Related")){
						getAllField(hashMap,Boolean.FALSE, Boolean.TRUE);
						TextField amtIn = (TextField)hashMap.get("amtIn");
						amtIn.setReadOnly(Boolean.TRUE);
						TextField totalAmt = (TextField)hashMap.get("totalAmt");
						totalAmt.setReadOnly(Boolean.TRUE);
			}
			if(hashMap!= null && classification.getValue()!=null && classification.getValue().equalsIgnoreCase("Other Exp")){
				//getAllField(hashMap,Boolean.TRUE, Boolean.FALSE);
				getAllField(hashMap,Boolean.TRUE, Boolean.TRUE);
				TextField totalAmtInr = (TextField)hashMap.get("totalAmtInr");
				totalAmtInr.setReadOnly(Boolean.FALSE);
			}
			if(hashMap!= null &&  classification.getValue()!=null && classification.getValue().equalsIgnoreCase("Negotiator Fee")){
				getAllField(hashMap,Boolean.TRUE, Boolean.FALSE);
				TextField totalAmtInr = (TextField)hashMap.get("totalAmtInr");
				totalAmtInr.setReadOnly(Boolean.TRUE);
				TextField negotiationCapping = (TextField)hashMap.get("negotiationCapping");
				negotiationCapping.setReadOnly(Boolean.FALSE);
				negotiationCapping.setValue(SHAConstants.OMP_NEGO_CAPPING);
				negotiationCapping.setReadOnly(Boolean.TRUE);
				
				TextField handlingCharges = (TextField)hashMap.get("handlingCharges");
				handlingCharges.setReadOnly(Boolean.FALSE);
				handlingCharges.setValue(SHAConstants.OMP_HANDLING_CHARGES);
				
				TextField differenceAmt = (TextField)hashMap.get("differenceAmt");
				differenceAmt.setReadOnly(Boolean.TRUE);
				
				TextField expenses = (TextField)hashMap.get("expenses");
				expenses.setReadOnly(Boolean.TRUE);
				
				TextField negotiationPayable = (TextField)hashMap.get("negotiationPayable");
				negotiationPayable.setReadOnly(Boolean.TRUE);
				
				TextField totalExp = (TextField)hashMap.get("totalExp");
				totalExp.setReadOnly(Boolean.TRUE);
			}
		}
	 	
	 	private void getAllField(
				HashMap<String, AbstractField<?>> hashMap, Boolean true1, Boolean false1) {
			TextField billAmt = (TextField)hashMap.get("billAmt");
			billAmt.setReadOnly(true1);
			TextField amtIn = (TextField)hashMap.get("amtIn");
			amtIn.setReadOnly(true1);
			TextField deduction = (TextField)hashMap.get("deduction");
			deduction.setReadOnly(true1);
			TextField totalAmt = (TextField)hashMap.get("totalAmt");
			totalAmt.setReadOnly(true1);
			TextField totalAmtInr = (TextField)hashMap.get("totalAmtInr");
			totalAmtInr.setReadOnly(false1);
			TextField approvedAmt = (TextField)hashMap.get("approvedAmt");
			approvedAmt.setReadOnly(false1);
			TextField agreedAmt = (TextField)hashMap.get("agreedAmt");
			agreedAmt.setReadOnly(false1);
			TextField differenceAmt = (TextField)hashMap.get("differenceAmt");
			differenceAmt.setReadOnly(false1);
			TextField expenses = (TextField)hashMap.get("expenses");
			expenses.setReadOnly(false1);
			TextField negotiationClaimed = (TextField)hashMap.get("negotiationClaimed");
			negotiationClaimed.setReadOnly(false1);
			TextField negotiationCapping = (TextField)hashMap.get("negotiationCapping");
			negotiationCapping.setReadOnly(false1);
			TextField handlingCharges = (TextField)hashMap.get("handlingCharges");
			handlingCharges.setReadOnly(false1);
			TextField negotiationPayable = (TextField)hashMap.get("negotiationPayable");
			negotiationPayable.setReadOnly(false1);
			TextField totalExp = (TextField)hashMap.get("totalExp");
			totalExp.setReadOnly(false1);
		}
	 	
	 	public List<String> validateCalculation() {
//			Boolean hasError = false;
//			showOrHideValidation(true);
			errorMessages.removeAll(getErrors());
			List<OMPClaimCalculationViewTableDTO> itemIds = (List<OMPClaimCalculationViewTableDTO>) table.getItemIds();
			
			if(null != itemIds && !itemIds.isEmpty())
			{
				for (OMPClaimCalculationViewTableDTO bean : itemIds) {
					if(bean.getCategory() == null){
	 					errorMessages.add("Please Select Category Type");
	 					//break;
					}
					if(classification!=null && classification.getValue()!=null){
						doValidation(bean, classification.getValue());
					}else{
						if(this.bean!=null && this.bean.getClassification()!=null && this.bean.getClassification().getValue()!=null){
							String classfication = this.bean.getClassification().getValue();
							doValidation(bean , classfication);
						}
					}
					
				}
				
				/*
			for (OMPClaimCalculationViewTableDTO bean : itemIds) {
				if(bean.getCategory() == null){
				
 					errorMessages.add("Please Select Category Type");
 					break;
					
				}
				if(bean.getBillAmt() == null){
				
					errorMessages.add("Please Enter BillAmt(in FC)");
 					break;
				}
				if(bean.getAmtIn() == null){
				
					errorMessages.add("Please Enter AmtIn");
 					break;
				}
				if(bean.getDeduction() == null){
				
					errorMessages.add("Please Enter Deduction");
 					break;
				}
				if(bean.getTotalAmt() == null){
					
					errorMessages.add("Please Enter TotalAmt in $");
 					break;
				}
				if(bean.getTotalAmtInr() == null){
					
					errorMessages.add("Please Enter TotalAmtInr");
 					break;
				}
				if(bean.getApprovedAmt() == null){
				
					errorMessages.add("Please Enter ApprovedAmt");
 					break;
				}
				if(bean.getAgreedAmt() == null){
					
					errorMessages.add("Please Enter AgreedAmt");
 					break;
				}
				if(bean.getDifferenceAmt() == null){
				
					errorMessages.add("Please Enter DifferenceAmt");
 					break;
				}
				if(bean.getExpenses() == null){
				
					errorMessages.add("Please Enter Expenses");
 					break;
				}
				if(bean.getNegotiationClaimed() == null){
				
					errorMessages.add("Please Enter Negotiatior Claimed");
 					break;
				}
				if(bean.getNegotiationCapping() == null){
				
					errorMessages.add("Please Enter Negotiation Capping");
 					break;
				}
				if(bean.getNegotiationPayable() == null){
				
					errorMessages.add("Please Enter Negotiator Payable ");
 					break;
				}
				if(bean.getHandlingCharges() == null){
				
					errorMessages.add("Please Enter Handling Charges");
 					break;
				}
				if(bean.getTotalExp() == null){
				
					errorMessages.add("Please Enter Total Expence");
 					break;
				}
			}
				
			*/}else{
				errorMessages.add("Please Add Bill Details");
			}
			return errorMessages;
		
		}



		private void doValidation(OMPClaimCalculationViewTableDTO bean , String classification) {
			if(classification.equalsIgnoreCase("OMP Claim Related")){
				if(bean.getBillAmt() == null){
					errorMessages.add("Please Enter BillAmt(in FC)");
					//break;
				}
			}
			if(classification.equalsIgnoreCase("Other Exp")){
				if(bean.getTotalAmtInr() == null){
					errorMessages.add("Please Enter Total Amount in Inr");
					//break;
				}
			}
			if(classification.equalsIgnoreCase("Negotiator Fee")){
				if(bean.getApprovedAmt() == null){
					
					errorMessages.add("Please Enter Approved Amount");
				}
				if(bean.getAgreedAmt() == null){
					
					errorMessages.add("Please Enter Agreed Amount");
				}
				if(bean.getNegotiationClaimed() == null){
					
					errorMessages.add("Please Enter Negotiatior Claimed");
				}
			}
		}
	 	
	 	public List<String> getErrors()
		{
			return this.errorMessages;
		}
	 	
		public void getTableList(final List<OMPClaimCalculationViewTableDTO> list) {
			table.removeAllItems();
			for (final OMPClaimCalculationViewTableDTO bean : list) {
				table.addItem(bean);
				
			}
			table.sort();
		}



		public void reset() {
			List<OMPClaimCalculationViewTableDTO> values = getValues();
			if(values!=null && !values.isEmpty()){
				deletedList.addAll(values);
				table.removeAllItems();
				setFooter();
			}
		}
	 	
		
	 	
	 	
		}
