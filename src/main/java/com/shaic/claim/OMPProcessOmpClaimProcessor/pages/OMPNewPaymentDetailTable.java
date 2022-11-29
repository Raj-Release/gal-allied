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

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class OMPNewPaymentDetailTable  extends ViewComponent{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private Map<OMPPaymentDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OMPPaymentDetailsTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<OMPPaymentDetailsTableDTO> container = new BeanItemContainer<OMPPaymentDetailsTableDTO>(OMPPaymentDetailsTableDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Set<String> errorMessages;
	
	private static Validator validator;
	
	private Map<String, Object> referenceData;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	
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
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				OMPPaymentDetailsTableDTO ompOMPPaymentDetailsTableDTO = new OMPPaymentDetailsTableDTO();
				if(container.size()>0){
					btnAdd.setEnabled(Boolean.FALSE);
				}
				else{
					btnAdd.setEnabled(Boolean.TRUE);
				BeanItem<OMPPaymentDetailsTableDTO> addItem = container.addItem(ompOMPPaymentDetailsTableDTO);
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(ompOMPPaymentDetailsTableDTO);
				 if(null != hashMap && !hashMap.isEmpty())
				 { 
					
					 TextField itemNoFld6 = (TextField)hashMap.get("transectionChequeNo");
					 if(null != itemNoFld6)
					 {
						 itemNoFld6.setReadOnly(Boolean.TRUE);
					 }
					 TextField itemNoFld7 = (TextField)hashMap.get("paymentDate");
					 if(null != itemNoFld7)
					 {
						 itemNoFld7.setReadOnly(Boolean.TRUE);
					 }
					 TextField itemNoFld8 = (TextField)hashMap.get("paymentStatus");
					 if(null != itemNoFld8)
					 {
						 itemNoFld8.setReadOnly(Boolean.TRUE);
					 }
					 //CR20181327 additonal fields 
					 TextField itemNoFld9 = (TextField)hashMap.get("chequeDate");
					 if(null != itemNoFld9)
					 {
						 itemNoFld9.setReadOnly(Boolean.TRUE);
					 }
					 TextField itemNoFld10 = (TextField)hashMap.get("convRateInInr");
					 if(null != itemNoFld10)
					 {
						 itemNoFld10.setReadOnly(Boolean.TRUE);
					 }
					 TextField itemNoFld11 = (TextField)hashMap.get("convAmtInInr");
					 if(null != itemNoFld11)
					 {
						 itemNoFld11.setReadOnly(Boolean.TRUE);
					 }
					 TextField itemNoFld12 = (TextField)hashMap.get("bankChargeInInr");
					 if(null != itemNoFld12)
					 {
						 itemNoFld12.setReadOnly(Boolean.TRUE);
					 }
					 TextField itemNoFld13 = (TextField)hashMap.get("settleAmt");
					 if(null != itemNoFld13)
					 {
						 itemNoFld13.setReadOnly(Boolean.TRUE);
					 }
					 TextField itemNoFld14 = (TextField)hashMap.get("postedDate");
					 if(null != itemNoFld14)
					 {
						 itemNoFld14.setReadOnly(Boolean.TRUE);
					 }
					 btnAdd.setEnabled(Boolean.FALSE);
				 }
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
		table.setVisibleColumns(new Object[] { "serialNumber", "paymentTo", "payeeNameStr", "currency","payMode","payableAt","panNo","emailId","transectionChequeNo","paymentDate","paymentStatus",
				"chequeDate","convRateInInr","convAmtInInr","bankChargeInInr","settleAmt","postedDate"});
		table.setColumnHeader("serialNumber", "Sl No");
		table.setColumnHeader("paymentTo", "Payment To");
		table.setColumnHeader("currency", "Payee Currency");
		table.setColumnHeader("payeeNameStr", "Payee Name");
		table.setColumnHeader("payMode", "Payment Mode");
		table.setColumnHeader("payableAt", "Payable At*");
		table.setColumnHeader("panNo", "PAN No");
		table.setColumnHeader("emailId", "Email ID");
		table.setColumnHeader("transectionChequeNo", "Transaction Reference Number/Cheque No. ");
		table.setColumnHeader("paymentDate", "Payment Date");
		table.setColumnHeader("paymentStatus", "Payment Status");
		//CR20181327 Additional columns
		table.setColumnHeader("chequeDate", "Cheque Date");
		table.setColumnHeader("convRateInInr", "Conv. Rate in INR");
		table.setColumnHeader("convAmtInInr", "Conv. Amount in INR");
		table.setColumnHeader("bankChargeInInr", "Bank Charge in INR");
		table.setColumnHeader("settleAmt", "Settled Amount");
		table.setColumnHeader("postedDate", "Posted Date");

		table.setEditable(true);
		
		@SuppressWarnings("unchecked")
		List<OMPPaymentDetailsTableDTO> itemIds =  (List<OMPPaymentDetailsTableDTO>) table.getItemIds();
		bean.setOmpPaymentDetailsList(itemIds);
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			OMPPaymentDetailsTableDTO entryDTO = (OMPPaymentDetailsTableDTO) itemId;
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
					field.setData(entryDTO);
//					onlyNumber(field);
					field.setEnabled(false);
					tableRow.put("serialNumber", field);
					return field;
				}	
				
				else if ("paymentTo".equals(propertyId)) {
					GComboBox field = new GComboBox();
				field.setWidth("250px");
				field.setData(entryDTO);
				field.setContainerDataSource(bean.getPaymentToContainer());
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				field.setValue(entryDTO.getPaymentTo());
//				field.setValue(entryDTO.getCategory());
				field.addValueChangeListener(setPayeeName());
				field.setReadOnly(bean.getIsReadOnly());
//				addListenerForClassification(field, entryDTO);
				tableRow.put("paymentTo", field);
				return field;
			}
				else if ("currency".equals(propertyId)) {
					GComboBox field = new GComboBox();
				field.setWidth("250px");
				field.setData(entryDTO);
				if(bean.getRodClaimType()!=null){
					SelectValue rodClaimType = bean.getRodClaimType();
					if(rodClaimType.getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
						field.setContainerDataSource(bean.getInrCurrencyValueContainer());
					}else{
						if(bean.getCurrencyValueContainer()!=null){
							BeanItemContainer<SelectValue> currencyValueContainer = bean.getCurrencyValueContainer();
							if(currencyValueContainer.size()==1){
								currencyValueContainer.addBean(bean.getCurrencyType());
							}
							field.setContainerDataSource(bean.getCurrencyValueContainer());
						}
					}
				}
						
				
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				field.setValue(entryDTO.getCurrency());
//				field.setValue(entryDTO.getCategory());
				/*field.addValueChangeListener(setPayeeName());*/
				field.setReadOnly(bean.getIsReadOnly());
//				addListenerForClassification(field, entryDTO);
				tableRow.put("currency", field);
				return field;
			}
				else if ("payeeNameStr".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(bean.getIsReadOnly());
					tableRow.put("payeeNameStr", field);
					return field;
				}
				else if ("payMode".equals(propertyId)) {
					GComboBox field = new GComboBox();
				field.setWidth("250px");
				field.setData(entryDTO);
				field.setData(entryDTO);
				field.setContainerDataSource(bean.getPaymentModeContainer());
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				field.setValue(entryDTO.getPayMode());
				field.setReadOnly(bean.getIsReadOnly());
				tableRow.put("payMode", field);
				return field;
			}
				else if ("payableAt".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setNullRepresentation("");
					field.setData(entryDTO);
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(bean.getIsReadOnly());
					tableRow.put("payableAt", field);
					return field;
				}
				else if ("panNo".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(bean.getIsReadOnly());
					tableRow.put("panNo", field);
					return field;
				}
				else if ("emailId".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(bean.getIsReadOnly());
					tableRow.put("emailId", field);
					return field;
				}
				else if ("transectionChequeNo".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("transectionChequeNo", field);
					return field;
				}
				else if ("paymentDate".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("paymentDate", field);
					return field;
				}
				else if ("paymentStatus".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					generateSlNo(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("paymentStatus", field);
					return field;
				}
				//Additional code for CR20181327
				else if ("chequeDate".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					generateSlNo(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("chequeDate", field);
					return field;
				}
				else if ("convRateInInr".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					generateSlNo(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("convRateInInr", field);
					return field;
				}
				else if ("convAmtInInr".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					generateSlNo(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("convAmtInInr", field);
					return field;
				}
				else if ("bankChargeInInr".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					generateSlNo(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("bankChargeInInr", field);
					return field;
				}
				else if ("settleAmt".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					generateSlNo(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("settleAmt", field);
					return field;
				}
				else if ("postedDate".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
//					onlyNumber(field);
					generateSlNo(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("postedDate", field);
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
	
	private ValueChangeListener setPayeeName() {
		ValueChangeListener listener = new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				GComboBox copayPercentage = (GComboBox) event.getProperty();
				OMPPaymentDetailsTableDTO calculationViewTableDTO = (OMPPaymentDetailsTableDTO)copayPercentage.getData();
 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
 				GComboBox payment = (GComboBox)hashMap.get("paymentTo");
 				List<OMPPaymentDetailsTableDTO> paymentList = new ArrayList<OMPPaymentDetailsTableDTO>();
 				SelectValue value = (SelectValue) copayPercentage.getValue();
 				if(value!= null &&value.getId()!= null&& value.getValue()!= null && calculationViewTableDTO!= null){
 					TextField payeeName = (TextField)hashMap.get("payeeNameStr");
 					TextField email = (TextField)hashMap.get("emailId");
 					 DBCalculationService dbCalculationService = new DBCalculationService();
 					 if(value.getId()!= null ){
 						 long paymentId = Long.valueOf(value.getId());
 						 paymentList = dbCalculationService.getpayeeNameDetails(paymentId);
 						 if(paymentList!= null&& !paymentList.isEmpty()){
 						 for(OMPPaymentDetailsTableDTO paymentDto: paymentList){
 							 if(paymentDto!= null){
 								if(paymentDto.getPayeeNameStr()!= null && paymentDto.getPayeeNameStr().equalsIgnoreCase("APRIL USA ASSISTANCE INC.,")){
 									if(payeeName!=null &&email!= null){
 								payeeName.setValue(paymentDto.getPayeeNameStr());
 								email.setValue(paymentDto.getEmailId());
 									}
 							 }
 								if(paymentDto.getPayeeNameStr()!= null && paymentDto.getPayeeNameStr().equalsIgnoreCase("Agada Global Ltd")){
 									if(payeeName!=null &&email!= null){
 									payeeName.setValue(paymentDto.getPayeeNameStr());
 	 								email.setValue(paymentDto.getEmailId());
 									}
 								}
 								if(paymentDto.getPayeeNameStr()!= null && paymentDto.getPayeeNameStr().equalsIgnoreCase("AMERICAN ASSIST TRAVEL SERVICES")){
 									if(payeeName!=null &&email!= null){
 									payeeName.setValue(paymentDto.getPayeeNameStr());
 	 								email.setValue(paymentDto.getEmailId());
 									}
 								}
 						 }
 					 }
 						 }else{
 						payeeName.setValue("");
 						email.setValue("");
 						}
 						 
 				 }
 					/*if(APRILUSA(TPA) 	AGADA(Negotiator) American Assist (TPA)){
 						
 					}*/
 				
 				}
			}
		};
		return listener;
	}
	
	public void setTableList(final List<OMPPaymentDetailsTableDTO> list) {
		table.removeAllItems();
		for (final OMPPaymentDetailsTableDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public List<OMPPaymentDetailsTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<OMPPaymentDetailsTableDTO> itemIds = (List<OMPPaymentDetailsTableDTO>) this.table.getItemIds() ;
    	return itemIds;
    }
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<OMPPaymentDetailsTableDTO> itemIds = (Collection<OMPPaymentDetailsTableDTO>) table.getItemIds();
		
		int i = 0;
		 for (OMPPaymentDetailsTableDTO calculationViewTableDTO : itemIds) {
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
	
	/*private ValueChangeListener addListenerForClassification()
	{
			ValueChangeListener listener = new ValueChangeListener() {
				
				private static final long serialVersionUID = 7455756225751111662L;
				
				
			
				@Override
				public void valueChange(ValueChangeEvent event) {
					GComboBox classification = (GComboBox) event.getProperty();
					OMPClaimCalculationViewTableDTO dto = (OMPClaimCalculationViewTableDTO)classification.getData();
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					if(classification!=null && classification.getValue()!=null && combos!=null){
						GComboBox cmbCategory = (GComboBox) combos.get("paymentTo");
						SelectValue value2 = (SelectValue) cmbCategory.getValue();
						if(value2!= null && value2.getValue()!= null && value2.getValue().equalsIgnoreCase("APRIL USA") 
								|| value2.getValue().equalsIgnoreCase("AGADA") ||value2.getValue().equalsIgnoreCase("AMERICAN ASSIST")){
							TextField recoverDate = (TextField)combos.get("payeeNameStr");
						}
//						doClassificationLogics(classification, dto, combos);
						}
					
				}
			};
			return listener;
		}*/
	
	public Set<String> validateCalculation(OMPClaimProcessorDTO dtoBean) {
//		Boolean hasError = false;
//		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		List<OMPPaymentDetailsTableDTO> itemIds = (List<OMPPaymentDetailsTableDTO>) table.getItemIds();
		
		if(null != itemIds && !itemIds.isEmpty())
		{
			for (OMPPaymentDetailsTableDTO bean : itemIds) {
				if(bean.getPaymentTo() == null){
 					errorMessages.add("Please Select Payment To");
 					//break;
				}
				if(bean.getPayableAt()==null ||bean.getPayableAt().equalsIgnoreCase("")){
					errorMessages.add("Please Enter Payable At");
				}
				if(bean.getPayeeNameStr() == null || bean.getPayeeNameStr().equalsIgnoreCase("")){
					errorMessages.add("Please Enter Payee Name");
				}
				if(bean.getPayMode()==null){
					errorMessages.add("Please Enter PayMode");
				}
				/*if(bean.getPanNo()==null || bean.getPanNo().equalsIgnoreCase("")){
					errorMessages.add("Please Enter Pan No");
				}*/
			}
			
			}else{
				errorMessages.add("Please Add payment Details");
			}
	/*	if(dtoBean!= null && dtoBean.getClaimCalculationViewTable()!= null && dtoBean.getClaimCalculationViewTable().get(0).getSendforApprover()!= null
				&&dtoBean.getClaimCalculationViewTable().get(0).getSendforApprover().equalsIgnoreCase("Y")){
			if(dtoBean.getClaimCalculationViewTable().get(0).getOmpPaymentDetailsList()!= null&&dtoBean.getClaimCalculationViewTable().get(0).getOmpPaymentDetailsList().isEmpty()){
				errorMessages.add("Please enter payment details");
			}
			
		}*/
		
			return errorMessages;
		
		}

	public Set<String> getErrors()
		{
			return this.errorMessages;
		}
	
	
	
	
	
}
