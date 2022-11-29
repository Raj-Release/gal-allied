package com.shaic.claim.OMPProcessNegotiation.pages;

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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class OMPProcessNegotiationPageDetailTable extends ViewComponent{

private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<OMPNegotiationDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OMPNegotiationDetailsDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<OMPNegotiationDetailsDTO> container = new BeanItemContainer<OMPNegotiationDetailsDTO>(OMPNegotiationDetailsDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private Map<String, Object> referenceData;
	
	//This value will be used for validation.
	public Double approvedAmt =0d;
	
	
	private String presenterString = "";
	
	private ViewDetails objViewDetails;

	private OMPClaimCalculationViewTableDTO bean;
	
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init(OMPClaimCalculationViewTableDTO calculationViewTableDTO) {
		this.bean=calculationViewTableDTO;
	//	populateBillDetails(bean);
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
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
		if(calculationViewTableDTO.getNegotiationDetailsDTOs()!=null && calculationViewTableDTO.getNegotiationDetailsDTOs().size()>0){
			btnAdd.setEnabled(Boolean.FALSE);
		}
		setCompositionRoot(tblPanel);
		//setCompositionRoot(horLayout);
	}
	
	public void setTableList(List<OMPNegotiationDetailsDTO> list) {
		table.removeAllItems();
		for (OMPNegotiationDetailsDTO detailsDTO : list) {
			table.addItem(detailsDTO);
		}
		table.sort();
	}

	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				if(container.size()!=0){
					btnAdd.setEnabled(Boolean.FALSE);
				}else{
				OMPNegotiationDetailsDTO ompNegotiationDetailsDTO = new OMPNegotiationDetailsDTO();
				BeanItem<OMPNegotiationDetailsDTO> addItem = container.addItem(ompNegotiationDetailsDTO);
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(ompNegotiationDetailsDTO);
				 if(null != hashMap && !hashMap.isEmpty())
				 { 
					 DateField itemNoFld = (DateField)hashMap.get("negotiationReqstDate");
					 if(null != itemNoFld)
					 {
						 itemNoFld.setReadOnly(Boolean.FALSE);
					 }
					 DateField itemNoFld1 = (DateField)hashMap.get("negotiationCompletDate");
					 if(null != itemNoFld1)
					 {
						 itemNoFld1.setReadOnly(Boolean.FALSE);
					 }
					 TextField itemNoFld2 = (TextField)hashMap.get("agreedAmount");
					 if(null != itemNoFld2)
					 {
						 itemNoFld2.setReadOnly(Boolean.FALSE);
					 }
					 TextField itemNoFld3 = (TextField)hashMap.get("negotiationRemarks");
					 if(null != itemNoFld3)
					 {
						 itemNoFld3.setReadOnly(Boolean.FALSE);
					 }
					 TextField itemNoFld4 = (TextField)hashMap.get("nameOfNegotiatior");
					 if(null != itemNoFld4)
					 {
						 itemNoFld4.setReadOnly(Boolean.FALSE);
						 itemNoFld4.setValue("Agada");
						 itemNoFld4.setReadOnly(Boolean.TRUE);
					 }
					 TextField itemNoFld5 = (TextField)hashMap.get("approvedAmt");
					 itemNoFld5.setReadOnly(Boolean.FALSE);
					 itemNoFld5.setValue(approvedAmt.toString());
					 itemNoFld5.setReadOnly(Boolean.TRUE);
					 /*if(null != itemNoFld5 && bean!=null && bean.getCalculationSheetDTO()!=null)
					 {   
						 OMPClaimProcessorCalculationSheetDTO calculationSheetDTO = bean.getCalculationSheetDTO();
						 if(calculationSheetDTO.getCoPayApprovedAmt()!=null){
							 itemNoFld5.setReadOnly(Boolean.FALSE);
							 itemNoFld5.setValue(approvedAmt.toString());
							 itemNoFld5.setReadOnly(Boolean.TRUE);
						 }
						
					 }*/btnAdd.setEnabled(Boolean.FALSE);
				 }}
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		table.setVisibleColumns(new Object[] { "serialNumber", "negotiationReqstDate", "negotiationCompletDate", "nameOfNegotiatior","approvedAmt","agreedAmount","diffAmt","expenseAmt","handlingCharges","negotiationRemarks"});
		table.setColumnHeader("serialNumber", "Sl No");
		table.setColumnHeader("negotiationReqstDate", "Negotiation Requested Date");
		table.setColumnHeader("negotiationCompletDate", "Negotiation Completed Date");
		table.setColumnHeader("nameOfNegotiatior", "Name of Negotiator");
		//R1134
		table.setColumnHeader("approvedAmt", "Negotiated Amount ($)");
		table.setColumnHeader("agreedAmount", "Bill Amount ($)");	
		table.setColumnHeader("diffAmt", "Diff Amount ($)");
		table.setColumnHeader("expenseAmt", "Expense Amount ($)");
		table.setColumnHeader("handlingCharges", "Handling Charges  ($)");
		table.setColumnHeader("negotiationRemarks", "Negotiation Remarks");
//		table.setColumnHeader("rodStatus", "ROD </br> status ");
//		table.setColumnHeader("select", "Select");
		table.setEditable(true);
		
		List<OMPNegotiationDetailsDTO> itemIds =  (List<OMPNegotiationDetailsDTO>) table.getItemIds();
		bean.setNegotiationDetailsDTOs(itemIds);
		table.setTableFieldFactory(new ImmediateFieldFactory());
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
	
	}
	
	private void displayClaimStatus(String intimationNo)
	{
		//objViewDetails = viewDetails.get();
		//queryDetailsObj.init(viewDetails);
		objViewDetails.viewClaimStatusUpdated(intimationNo);
	}
	
	public void setViewDetailsObj(ViewDetails viewDetails)
	{
		objViewDetails = viewDetails;
	//	initTable();
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	
	
	public void manageListeners() {
		}
	
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			OMPNegotiationDetailsDTO entryDTO = (OMPNegotiationDetailsDTO) itemId;
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
					field.setEnabled(false);
					tableRow.put("serialNumber", field);
					return field;
				}	
				
				else if ("negotiationReqstDate".equals(propertyId)) {
				DateField field = new DateField();
				field.setWidth("250px");
				field.setDateFormat("dd/MM/yyyy");
				//field.setWidth("125px");
				//field.setEnabled(false);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(entryDTO);
				field.setReadOnly(bean.getIsReadOnly());
				//field.setReadOnly(true);
				tableRow.put("negotiationReqstDate", field);
				return field;
			}
			
			else if ("negotiationCompletDate".equals(propertyId)) {
				DateField field = new DateField();
				field.setWidth("250px");
				field.setDateFormat("dd/MM/yyyy");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.setReadOnly(bean.getIsReadOnly());
				//field.setReadOnly(true);
				
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("negotiationCompletDate", field);

				return field;
			}else if ("nameOfNegotiatior".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.setReadOnly(true);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("nameOfNegotiatior", field);

				return field;
			}
			else if ("approvedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setValue(String.valueOf(approvedAmt));
				//field.setMaxLength(6);
				entryDTO.setApprovedAmt(approvedAmt);
				field.setData(entryDTO);
				field.setReadOnly(true);
				field.addValueChangeListener(approvedListener());
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("approvedAmt", field);

				return field;
			}else if ("agreedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.setReadOnly(bean.getIsReadOnly());
				field.addValueChangeListener(agreedListener());
				//field.setReadOnly(true);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("agreedAmount", field);

				return field;
			}else if ("diffAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.addValueChangeListener(differenceListener());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("diffAmt", field);

				return field;
			}else if ("expenseAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("expenseAmt", field);

				return field;
			}else if ("handlingCharges".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				if(entryDTO.getHandlingCharges()==null){
					if(bean.getCategory()!=null && bean.getCategory().getValue().equals(SHAConstants.OMP_CATEGORY_MAIN_BILL)){
						field.setValue("100");
						entryDTO.setHandlingCharges(100d);
					}else{
						field.setValue("0");
						entryDTO.setHandlingCharges(0d);
					}
				}
				field.setReadOnly(bean.getIsReadOnly());
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("handlingCharges", field);

				return field;
			}else if ("negotiationRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.setReadOnly(bean.getIsReadOnly());
				//field.setReadOnly(true);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("negotiationRemarks", field);
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
	}

	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<OMPNegotiationDetailsDTO> itemIds = (Collection<OMPNegotiationDetailsDTO>) table.getItemIds();
		
		int i = 0;
		 for (OMPNegotiationDetailsDTO negotiationDetailsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(negotiationDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("serialNumber");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setEnabled(false);
				 }
				 
				 TextField approvedAmt = (TextField)hashMap.get("approvedAmt");
				 if(null != approvedAmt && approvedAmt.getValue()!=null)
				 {
					 String value = approvedAmt.getValue();
					 approvedAmt.setReadOnly(false);
					 approvedAmt.setValue(null); 
					 approvedAmt.setValue(value);
					 approvedAmt.setReadOnly(true);
				 }
			 }
		 }
		
	}
	
	 public void addBeanToList(OMPNegotiationDetailsDTO OMPNegotiationDetailsDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 container.addItem(OMPNegotiationDetailsDTO);

//	    	data.addItem(pedValidationDTO);
	    	//manageListeners();
	    }
	
	 public List<OMPNegotiationDetailsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<OMPNegotiationDetailsDTO> itemIds = (List<OMPNegotiationDetailsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	
	public void tableSelectHandler(OMPNegotiationDetailsDTO t) {/*
		
		if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(DocumentDetailsPresenter.DISABLE_TABLE_VALUES, t);
		}
		else if (SHAConstants.CREATE_ROD.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(CreateRODDocumentDetailsPresenter.SELECT_RECONSIDER_TABLE_VALUES, t);
		}
		
		else if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(BillEntryDocumentDetailsPresenter.BILL_ENTRY_SELECT_RECONSIDER_TABLE_VALUES, t);
		}

	*/}
	private void onlyNumber(TextField field) {
		CSValidator validator = new CSValidator();
		validator.extend(field);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
	}
	
	
	public List<String> validateCalculation() {
//		Boolean hasError = false;
//		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		List<OMPNegotiationDetailsDTO> itemIds = (List<OMPNegotiationDetailsDTO>) table.getItemIds();
		
		if(null != itemIds && !itemIds.isEmpty())
		{
			for (OMPNegotiationDetailsDTO bean : itemIds) {
				if(bean.getNegotiationReqstDate() == null){
				
 					errorMessages.add("Please Enter Negotiation Requisted Date");
 					//break;
					
				}
				if(bean.getNegotiationCompletDate() == null){
					errorMessages.add("Please Enter Negotiation Completed Date");
 					//break;
				}
				if(bean.getAgreedAmount() == null){
					errorMessages.add("Please Enter Agreed Amt");
 					//break;
				}
				if(bean.getAgreedAmount() > bean.getApprovedAmt()){
					errorMessages.add("Agreed amount should not be greater than Approved amount");
 					//break;
				}
			}
		}else{
			errorMessages.add("Please Add Atleast One Row in Negotiation Table");
		}
		return errorMessages;
	}

	public List<String> getErrors()
	{
		return this.errorMessages;
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
				OMPNegotiationDetailsDTO calculationViewTableDTO = (OMPNegotiationDetailsDTO)agreedAmt.getData();
 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
				TextField approvedTxt1 = (TextField)hashMap.get("approvedAmt");
				TextField differenceAmtTxt = (TextField)hashMap.get("diffAmt");
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
				OMPNegotiationDetailsDTO calculationViewTableDTO = (OMPNegotiationDetailsDTO)differenceAmtTxt.getData();
 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
				TextField expenses = (TextField)hashMap.get("expenseAmt");
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
	
	public ValueChangeListener approvedListener(){
		ValueChangeListener listener = new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField approvedAmt = (TextField) event.getProperty();
				OMPNegotiationDetailsDTO calculationViewTableDTO = (OMPNegotiationDetailsDTO)approvedAmt.getData();
 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
				TextField agreedTxt1 = (TextField)hashMap.get("agreedAmount");
				TextField differenceAmtTxt = (TextField)hashMap.get("diffAmt");
				Double approvedDouble = 0d;
				Double agreedTxt1Double =0d;
				Double totalAmtDouble =0d;
				if(agreedTxt1!=null && agreedTxt1.getValue()!=null){
					approvedDouble = SHAUtils.getDoubleFromStringWithComma(approvedAmt.getValue());
					agreedTxt1Double = SHAUtils.getDoubleFromStringWithComma(agreedTxt1.getValue());
					totalAmtDouble =  approvedDouble - agreedTxt1Double;
					if(approvedAmt!=null && differenceAmtTxt!=null){
						differenceAmtTxt.setReadOnly(Boolean.FALSE);
						differenceAmtTxt.setValue(totalAmtDouble.toString());
						differenceAmtTxt.setReadOnly(Boolean.TRUE);
					}
				}
			}
		};
		
		return listener;
	}
}
