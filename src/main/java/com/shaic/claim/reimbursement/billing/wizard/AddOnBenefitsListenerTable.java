package com.shaic.claim.reimbursement.billing.wizard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AddOnBenefitsListenerTable extends ViewComponent {

	private static final long serialVersionUID = 4651688307738097848L;

	private Map<AddOnBenefitsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<AddOnBenefitsDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<AddOnBenefitsDTO> data = new BeanItemContainer<AddOnBenefitsDTO>(AddOnBenefitsDTO.class);

	private Table table;

	private Button btnAdd;
	
	/*private Map<String, Object> referenceData;
	
	private Long hospitalKey;
	
	private String packageRateValue;
	
	private String dayCareFlagValue;
	
	private String procedureCodeValue;
	
	private List<String> errorMessages;
	
	private static Validator validator;*/
	
	public List<String> diagnosisList = new ArrayList<String>();

	//private AbstractSelect dummyField;
	public TextField dummyField;
	
	//private List<DiagnosisDetailsTableDTO> diagnosisList;
	
	
	public void init() {
	//public void init(Long hospitalKey, List<DiagnosisDetailsTableDTO> diagnosisList) {

		//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		//validator = factory.getValidator();
		dummyField = new TextField();
		//this.diagnosisList = diagnosisList;
		//this.errorMessages = new ArrayList<String>();
		diagnosisList = new ArrayList<String>();
		//this.hospitalKey = hospitalKey;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		//layout.addComponent(btnLayout);
		layout.setMargin(true);
		
		initTable(layout);
		table.setWidth("100%");
		
		//table.setHeight("30%");
		/**
		 * Height is set for table visiblity.
		 * */
		table.setHeight("160px");
		
		table.setPageLength(table.getItemIds().size());
		
//		addListener();
		
		layout.addComponent(table);
		setSizeFull();

		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Add on Benefits (Hospital Cash)", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "particulars", "dateOfAdmission", "dateOfDischarge", "admittedNoOfDays", "allowedNoOfDays", "totalClaimedAmount", "entitledNoOfDays", "noOfDaysPerHospitalization", "entitlementPerDayAmt","utilizedNoOfDays", "balanceAvailable", "eligibleNoofDays", "eligiblePayableNoOfDays", "eligiblePerDayAmt", "eligibleNetAmount", "coPayPercentage", "copayAmount", "netAmountAfterCopay", "limitAsPerPolicy", "payableAmount"  });

		table.setColumnHeader("particulars", "Particulars");
		table.setColumnHeader("dateOfAdmission", "Date of Admission (A)");
		table.setColumnHeader("dateOfDischarge", "Date of Discharge (B)");
		table.setColumnHeader("admittedNoOfDays", "No. of Days Admitted (C)");
		table.setColumnHeader("allowedNoOfDays", "No of Days Allowed (Hosp) (D)");
		table.setColumnHeader("totalClaimedAmount", "Total Amount Claimed (E)");
		table.setColumnHeader("entitledNoOfDays", "No. of Days (Entitled) (F)");
		table.setColumnHeader("noOfDaysPerHospitalization", "No of Days Limit Per Hosp (G)");
		table.setColumnHeader("entitlementPerDayAmt", "Per Day Amount (H)");
		table.setColumnHeader("utilizedNoOfDays", "No. of Days Utilized (I)");
		table.setColumnHeader("balanceAvailable", "Balance Available (J)");
		table.setColumnHeader("eligibleNoofDays", "No. of Days Eligible (K)");
		table.setColumnHeader("eligiblePayableNoOfDays", "No. of Days Payable (L)");
		table.setColumnHeader("eligiblePerDayAmt", "Per Day Amount (M)");
		table.setColumnHeader("eligibleNetAmount", "Net Amount (N)");
	//	table.setColumnHeader("copayPercentage", "Co-Pay % (O)");
		table.setColumnHeader("coPayPercentage", "Co-Pay % (O)");
		table.setColumnHeader("copayAmount", "Co-Pay Amount (P)");
		table.setColumnHeader("netAmountAfterCopay", "Net Amount After Co-Pay (Q)");
		table.setColumnHeader("limitAsPerPolicy", "Limit As Per Policy (R)");
		table.setColumnHeader("payableAmount", "Amount Payable (S)");
		table.setEditable(true);
		table.setFooterVisible(true);
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {

		private static final long serialVersionUID = -2485611748255495737L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			AddOnBenefitsDTO benefitsDTO = (AddOnBenefitsDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(benefitsDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(benefitsDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(benefitsDTO);
			}
			
			 if("totalClaimedAmount".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setData(benefitsDTO);
					field.setConverter(plainIntegerConverter);
					field.setEnabled(false);
					tableRow.put("totalClaimedAmount", field);
					return field;
				}
			 else if("coPayPercentage".equals(propertyId)) {
					ComboBox field = new ComboBox();
					field.setWidth("100px");
					field.setData(benefitsDTO);
					tableRow.put("coPayPercentage", field);
					addCoPayPercentageValues(field , benefitsDTO);
					field.addValueChangeListener(copayListener());
					calculateTotal();
					return field;
				}
			 
			else if("copayPercentage".equals(propertyId)) {
				ComboBox field = new ComboBox();
				field.setWidth("100px");
				field.setData(benefitsDTO);
				tableRow.put("copayPercentage", field);
				addCoPayPercentageValues(field , benefitsDTO);
				return field;
			} else  if("eligibleNetAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setData(benefitsDTO);
				field.setConverter(plainIntegerConverter);
				field.setEnabled(false);
				tableRow.put("eligibleNetAmount", field);
				
				//calculateNetAmtListener(field);
				return field;
			} else  if("copayAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setData(benefitsDTO);
				field.setConverter(plainIntegerConverter);
				field.setEnabled(false);
				tableRow.put("copayAmount", field);
				return field;
			} else  if("netAmountAfterCopay".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setData(benefitsDTO);
				field.setConverter(plainIntegerConverter);
				field.setEnabled(false);
				tableRow.put("netAmountAfterCopay", field);
				
				return field;
			} else  if("limitAsPerPolicy".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setData(benefitsDTO);
				field.setConverter(plainIntegerConverter);
				field.setEnabled(false);
				tableRow.put("limitAsPerPolicy", field);
				return field;
			} else  if("payableAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setData(benefitsDTO);
				field.setConverter(plainIntegerConverter);
				field.setEnabled(false);
				tableRow.put("payableAmount", field);
				return field;
			}  
		 
		 else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				return field;
			}
		}

		
	}
	
	/*public void noOfDaysPayableListener(final TextField txtField){
		
		if(null != txtField)
		{
			txtField
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {					
					List<AddOnBenefitsDTO> itemIconPropertyId = (List<AddOnBenefitsDTO>) table.getItemIds();
					 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
					 {
						 for (AddOnBenefitsDTO addOnBenefitsDTO : itemIconPropertyId) {
							Integer minDay1 = Math.min(addOnBenefitsDTO.getEntitledNoOfDays() , addOnBenefitsDTO.getNoOfDaysPerHospitalization());
							Integer minDay2 = Math.min(addOnBenefitsDTO.getBalanceAvailable(), minDay1);
							txtField.setValue(String.valueOf(minDay2));
						 }
					 }
				}
			});
		}
	}*/
	
	public void noOfDaysPayableListener(final TextField txtField){
		
		if(null != txtField)
		{	
			List<AddOnBenefitsDTO> itemIconPropertyId = (List<AddOnBenefitsDTO>) table.getItemIds();
			 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
			 {
				 for (AddOnBenefitsDTO addOnBenefitsDTO : itemIconPropertyId) {
					Integer minDay1 = Math.min(addOnBenefitsDTO.getEntitledNoOfDays() , addOnBenefitsDTO.getNoOfDaysPerHospitalization());
					Integer minDay2 = Math.min(addOnBenefitsDTO.getBalanceAvailable(), minDay1);
					txtField.setValue(String.valueOf(minDay2));
				 }
			 }
		}
	}
	
	
	StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter() {
		private static final long serialVersionUID = -2154393632039317675L;

		protected java.text.NumberFormat getFormat(Locale locale) {
	        NumberFormat format = super.getFormat(locale);
	        format.setGroupingUsed(false);
	        return format;
	    };
	};
	
	
	/*
	public void calculateNetAmtListener(final TextField txtField){
		
		if(null != txtField)
		{
			txtField
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {					
					List<AddOnBenefitsDTO> itemIconPropertyId = (List<AddOnBenefitsDTO>) table.getItemIds();
					 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
					 {
						 for (AddOnBenefitsDTO addOnBenefitsDTO : itemIconPropertyId) {
							 HashMap<String, AbstractField<?>> hashMap = tableItem.get(addOnBenefitsDTO);
							 TextField noOfDaysPayable = (TextField)hashMap.get("eligiblePayableNoOfDays");
							 Integer eligiblePerDayAmt = addOnBenefitsDTO.getEligiblePerDayAmt();
							 Integer netAmt = Integer.valueOf(noOfDaysPayable.getValue()) * eligiblePerDayAmt;
							 txtField.setValue(String.valueOf(netAmt));
						 }
					 }
				}
			});
		}
	}*/
	
	
	 public void addCoPayPercentageValues(ComboBox comboBox, AddOnBenefitsDTO dto){
		   BeanItemContainer<SelectValue> coPayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		   
		    List<String> coPayPercentageValues = dto.getCopayPercentage();
		    if(coPayPercentageValues == null || coPayPercentageValues.isEmpty()) {
		    	coPayPercentageValues = new ArrayList<String>();
		    	for (Double string : dto.getProductCoPay()) {
		    		coPayPercentageValues.add(String.valueOf(string.intValue()));
				}
		    }
		    Long i = 0l;
		    for (String string : coPayPercentageValues) {
		    	SelectValue value = new SelectValue();
		    	value.setId(Long.valueOf(string));
		    	value.setValue(string);
		    	if(dto.getCoPayPercentage() == null) {
		    		if(i.equals(0l)) {
			    		dto.setCoPayPercentage(value);
			    	}
		    	}
		    	coPayContainer.addBean(value);
		    	i++;
			}
		    
			comboBox.setContainerDataSource(coPayContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
			
			
	 }
	
	public ValueChangeListener copayListener(){
		ValueChangeListener listener = new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox component = (ComboBox) event.getProperty();
				AddOnBenefitsDTO benefitsDTO = (AddOnBenefitsDTO)component.getData();
 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(benefitsDTO);
				TextField netAmount = (TextField)hashMap.get("eligibleNetAmount");
				//Integer netAmount = benefitsDTO.getEligibleNetAmount();
				TextField copayAmount = (TextField)hashMap.get("copayAmount");
				TextField aftCopayAmount = (TextField)hashMap.get("netAmountAfterCopay");
				//TextField limitAsperPolicyAmt = (TextField)hashMap.get("limitAsPerPolicy");
				TextField payableAmount = (TextField)hashMap.get("payableAmount");
				Float coPay = Float.valueOf(SHAUtils.isValidFloat(component.getValue().toString()) ? component.getValue().toString() : "0");
				Float calculatedAmt = SHAUtils.getIntegerFromString(netAmount.getValue()) * (coPay/100f);
				Integer roundedValue = Math.round(calculatedAmt);
			//	Integer roundedValue = Math.round(calculatedAmt);
				Integer value = SHAUtils.getIntegerFromString(netAmount.getValue()) - roundedValue;
				//Integer value = netAmount - roundedValue;
				if(null != copayAmount)
				{
					copayAmount.setValue(roundedValue.toString());
					benefitsDTO.setCopayAmount(roundedValue);
				}
				if(null != aftCopayAmount)
				{
					aftCopayAmount.setValue(String.valueOf(value));
					benefitsDTO.setNetAmountAfterCopay(value);
				}
				/**
				 * Since not sure about the limit as per policy field value, we are commenting the same
				 * and populating the net amount after copay in this field.
				 * */
				if(null != payableAmount)
				{
					payableAmount.setValue(String.valueOf(aftCopayAmount));	
					benefitsDTO.setPayableAmount(value);
					
				}
				calculateTotal();
				//Integer payableAmt = Math.min(SHAUtils.getIntegerFromString(limitAsperPolicyAmt.getValue()), value);
				//payableAmount.setValue(String.valueOf(payableAmt));
			}
		};
		
		return listener;
	}
	
	public void calculateTotal() {
		
		List<AddOnBenefitsDTO> itemIconPropertyId = (List<AddOnBenefitsDTO>) table.getItemIds();
		Integer coPayTotal = 0;
		Integer netAmtTotal=0;
		Integer aftCopayTotal = 0;
		Integer payableAmtTotal = 0;
		for (AddOnBenefitsDTO dto : itemIconPropertyId) {
		    Integer netAmt = dto.getEligibleNetAmount();
		    netAmtTotal += netAmt != null ? netAmt : 0;
		    
		    Integer copayAmt = dto.getCopayAmount();
		    coPayTotal += copayAmt != null ? copayAmt : 0;
		    
		    Integer netAmtAftCopay = dto.getNetAmountAfterCopay();
		    aftCopayTotal += netAmtAftCopay != null ? netAmtAftCopay : 0;
		    
		    Integer payableAmt = dto.getPayableAmount();
		    payableAmtTotal += payableAmt != null ? payableAmt : 0;
			
		}
		table.setColumnFooter("eligibleNetAmount", String.valueOf(netAmtTotal));
		table.setColumnFooter("copayAmount", String.valueOf(coPayTotal));
		table.setColumnFooter("netAmountAfterCopay", String.valueOf(aftCopayTotal));
		table.setColumnFooter("payableAmount", String.valueOf(payableAmtTotal));
		dummyField.setValue(String.valueOf(payableAmtTotal));
	}
	
	 public void addBeanToList(AddOnBenefitsDTO benefitsdto) {
		 data.addItem(benefitsdto);

	    }
	 
	 @SuppressWarnings("unchecked")
		public List<AddOnBenefitsDTO> getValues() {
			List<AddOnBenefitsDTO> itemIds = (List<AddOnBenefitsDTO>) this.table.getItemIds() ;
	    	return itemIds;
		}
	 
	

}
