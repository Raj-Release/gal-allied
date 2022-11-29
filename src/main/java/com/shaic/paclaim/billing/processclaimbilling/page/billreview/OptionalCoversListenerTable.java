package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OptionalCoversListenerTable extends ViewComponent{
	
	private Table table;
	
	PreauthDTO bean;
	
	BeanItemContainer<OptionalCoversDTO> data = new BeanItemContainer<OptionalCoversDTO>(OptionalCoversDTO.class);
	
	private Map<OptionalCoversDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OptionalCoversDTO, HashMap<String, AbstractField<?>>>();
	
	private BeanItemContainer<SelectValue> optionalCoverContainer;
	
	private Map<String, Object> referenceData;
	
	public TextField netAmtText = new TextField();

	
	public Object[] VISIBLE_COLUMNS = new Object[] {"optionalCover","eligibleForPolicy","billNo",
			"billDate","noOfDaysClaimed","amountClaimedPerDay","totalClaimed","deduction","noOfDaysAllowed","maxNoOfDaysPerHospital","maxDaysAllowed","noOfDaysUtilised",
			"noOfDaysAvailable","noOfDaysPayable","allowedAmountPerDay","amtPerDayPayable","siLimit","limit","appAmt","remarks"};

	
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		data.removeAllItems();
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		table = new Table("", data);
		//data.removeAllItems();

		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());
		table.setEditable(true);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		
		table.setColumnHeader("optionalCover", "Optional Cover");
		table.setColumnHeader("eligibleForPolicy", "Eligible For Policy");
		table.setColumnHeader("billNo", "Bill no");
		table.setColumnHeader("billDate", "Bill Date");
		table.setColumnHeader("noOfDaysClaimed", "No. of Days Claimed");
		table.setColumnHeader("amountClaimedPerDay", "Amt Claimed per day");
		table.setColumnHeader("totalClaimed", "Total Claimed");
		table.setColumnHeader("deduction", "Deduction (Non Payables)");     // CR2019100 -  Deduction for Medical Extention ROD
		//table.setColumnHeader("amtOfClaimPaid", "Amt of claim Paid");
		//table.setColumnHeader("applicableSI", "Applicable SI");
		table.setColumnHeader("noOfDaysAllowed", "No of Days Allowed");
		table.setColumnHeader("maxNoOfDaysPerHospital", "Max No of days per hosp");
		table.setColumnHeader("maxDaysAllowed", "Max no. days allowed (policy Period");
		table.setColumnHeader("noOfDaysUtilised", "No of Days Utilized");
		table.setColumnHeader("noOfDaysAvailable", "No. of days  Available");
		table.setColumnHeader("noOfDaysPayable", "No of days Payable");
		table.setColumnHeader("allowedAmountPerDay", "Allowed amt per day (as per policy)");
		table.setColumnHeader("amtPerDayPayable", "Amt per day payable");
		table.setColumnHeader("siLimit", "SI limit - (for medical extension)");
		table.setColumnHeader("limit", "Limit (valid claim amt)");
		//table.setColumnHeader("balanceSI", "Balance SI");
		table.setColumnHeader("appAmt", "App. Amt");
		table.setColumnHeader("remarks", "Remarks");
		table.setTableFieldFactory(new ImmediateFieldFactory());	

		
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		/*private static final long serialVersionUID = -2192723245525925990L;*/

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			OptionalCoversDTO optionalCoversDTO = (OptionalCoversDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			
			if (tableItem.get(optionalCoversDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(optionalCoversDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(optionalCoversDTO);
			/*
			Map<String, AbstractField<?>> tableRow = null;
				tableRow = new HashMap<String, AbstractField<?>>();*/
				
			if("eligibleForPolicy".equals(propertyId)){
					TextField field = new TextField();
					field.setNullRepresentation("");
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setWidth("100%");
					field.setData(optionalCoversDTO);
					tableRow.put("eligibleForPolicy", field);
					//field.setReadOnly(true);
					return field;
				}
			
			else if("optionalCover".equals(propertyId)){
				GComboBox field = new GComboBox();
				field.setData(optionalCoversDTO);
				tableRow.put("optionalCover", field);
				field.setNullSelectionAllowed(false);
				addCoversValues(field);
				addCoversListener(field);
				if(null != bean.getPreauthDataExtractionDetails().getDocAckknowledgement() 
					   &&  bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase( bean.getPreauthDataExtractionDetails().getReconsiderationFlag()))
				{
					field.setEnabled(false);
				}
				
				field.setEnabled(false);
				return field;
			}		
			
			else if("billNo".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setWidth("100%");
				field.setData(optionalCoversDTO);
				tableRow.put("billNo", field);
				//field.setReadOnly(true);
				return field;
			}
			else if("billDate".equals(propertyId)){
				DateField field = new DateField();
				field.setDateFormat("dd/MM/yyyy");
				//field.setReadOnly(true);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(optionalCoversDTO);
				tableRow.put("billDate", field);
				return field;
			}else if("amountClaimedPerDay".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				//field.setReadOnly(false);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(optionalCoversDTO);
				//doReadOnly(optionalCoversDTO, field);
				addNetAmountListener(field);
				doReadOnly(optionalCoversDTO, field);
				tableRow.put("amountClaimedPerDay", field);
				return field;
			}
			else if("noOfDaysClaimed".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				//field.setReadOnly(false);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(optionalCoversDTO);
				addNetAmountListener(field);
				doReadOnly(optionalCoversDTO, field);
				tableRow.put("noOfDaysClaimed", field);
				return field;
			}
			
			else if("totalClaimed".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(optionalCoversDTO);
				Boolean isreadOnly = null;
				if(optionalCoversDTO.getOptionalCover()!=null){
					String addOnValue = optionalCoversDTO.getOptionalCover().getValue();
					isreadOnly = isMedicalExt(addOnValue);
					//field.setValue("NA");
					if(isreadOnly != null){
						if(isreadOnly){
							field.setReadOnly(Boolean.FALSE);
						}else{
							field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
							field.setReadOnly(Boolean.TRUE);
						}
					}
				}
				if(!field.isReadOnly()){
					addNetAmountListener(field);
				}
				//IMSSUPPOR-31744
				field.setReadOnly(false);
				field.setValue(optionalCoversDTO.getTotalClaimed() != null ? String.valueOf(optionalCoversDTO.getTotalClaimed()) : "0");
				if(isreadOnly != null){
					if(isreadOnly){
						field.setReadOnly(Boolean.FALSE);
					}else{
						field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
						field.setReadOnly(Boolean.TRUE);
					}
				}
				
				tableRow.put("totalClaimed", field);
				return field;
			}
			
			// CR2019100 -  Deduction for Medical Extention ROD
			else if("deduction".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator amtTxtVal = new CSValidator();
				amtTxtVal.extend(field);
				amtTxtVal.setRegExp("^[0-9]*$");
				amtTxtVal.setPreventInvalidTyping(true);
				
				field.setData(optionalCoversDTO);

				if(optionalCoversDTO.getOptionalCover()!=null){
					String addOnValue = optionalCoversDTO.getOptionalCover().getValue();
					Boolean isreadOnly = isMedicalExt(addOnValue);
					if(isreadOnly){
						field.setReadOnly(Boolean.FALSE);
					}else{
						field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
						field.setReadOnly(Boolean.TRUE);
					}
					
				}
				if(!field.isReadOnly()){
					addNetAmountListener(field);
				}
				
				tableRow.put("deduction", field);
				return field;
                  				// CR2019100 -  Deduction for Medical Extention ROD
			}                 
			
			/*else if("amtOfClaimPaid".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(optionalCoversDTO);
				tableRow.put("amtOfClaimPaid", field);
				return field;
			}*/
			/*else if ("applicableSI".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				tableRow.put("applicableSI", field);
				return field;
			}*/
			else if ("noOfDaysAllowed".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setData(optionalCoversDTO);
				addNetAmountListener(field);
				doReadOnly(optionalCoversDTO, field);
				tableRow.put("noOfDaysAllowed", field);
				field.setData(optionalCoversDTO);
				addDaysListener(field);
				addNetAmountListener(field);
				return field;
			}
			else if ("maxNoOfDaysPerHospital".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setReadOnly(Boolean.TRUE);				
				tableRow.put("maxNoOfDaysPerHospital", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(optionalCoversDTO);
				addDaysListener(field);
				return field;
			}
			else if ("maxDaysAllowed".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setReadOnly(Boolean.TRUE);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("maxDaysAllowed", field);
				return field;
			}
			else if ("noOfDaysUtilised".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setReadOnly(Boolean.TRUE);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("noOfDaysUtilised", field);
				return field;
			}else if ("noOfDaysAvailable".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setReadOnly(Boolean.TRUE);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("noOfDaysAvailable", field);
				field.setData(optionalCoversDTO);
				addDaysListener(field);
				return field;
			}else if ("noOfDaysPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				addNetAmountListener(field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("noOfDaysPayable", field);
				return field;
			}else if ("allowedAmountPerDay".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("allowedAmountPerDay", field);
				return field;
			}else if ("amtPerDayPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				addNetAmountListener(field);
				field.setReadOnly(Boolean.TRUE);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("amtPerDayPayable", field);
				return field;
			}else if ("siLimit".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("siLimit", field);
				return field;
			}else if ("limit".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setReadOnly(Boolean.TRUE);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("limit", field);
				return field;
			}else if ("appAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
//				addNetAmountListener(field);
				field.setReadOnly(Boolean.TRUE);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("200px");
				field.setMaxLength(100);
				tableRow.put("appAmt", field);
				
				TextField ClaimAmtfield = (TextField)tableRow.get("totalClaimed");
				calculateNetAmount(ClaimAmtfield);
				return field;
			}else if ("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				tableRow.put("remarks", field);
				return field;
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					((TextField) field).setNullRepresentation("");
					field.setReadOnly(true);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setWidth("100%");
				return field;
			}
			
		}
	}

	public void setDropDownValues(BeanItemContainer<SelectValue> coverContainer)
	{
		this.optionalCoverContainer = coverContainer;
	}
	
	public void addCoversValues(GComboBox comboBox) {
		//BeanItemContainer<SelectValue> fileTypeContainer = null;
		comboBox.setContainerDataSource(optionalCoverContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
	}
	public void addBeanToList(OptionalCoversDTO benefitsDTO) {
    	data.addItem(benefitsDTO);
    }
	
	
	 public List<OptionalCoversDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<OptionalCoversDTO> itemIds = (List<OptionalCoversDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	 private void calculateNetAmount(TextField field)
		{
			OptionalCoversDTO dto = (OptionalCoversDTO) field.getData();
			if(null != dto)
			{
				 try {
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
					// TextField billAmtFld = (TextField) hashMap.get("appAmt");
					// TextField deductionsFld = (TextField) hashMap.get("deduction");
					 TextField netAmtFld = (TextField) hashMap.get("appAmt");
					 TextField amountperdayText = (TextField) hashMap.get("amountClaimedPerDay");
					 TextField noOfDaysClaimedText = (TextField) hashMap.get("noOfDaysClaimed");
					 TextField totalClaimedtext = (TextField) hashMap.get("totalClaimed");

					 // CR2019100 -  Deduction for Medical Extention ROD
					 TextField deductiontext = (TextField) hashMap.get("deduction");
					 
					 GComboBox optionalCover = (GComboBox) hashMap.get("optionalCover");
					 TextField siLimittext = (TextField) hashMap.get("siLimit");
					 TextField limitText = (TextField) hashMap.get("limit");
					 TextField amtPerDayPayableText = (TextField) hashMap.get("amtPerDayPayable");
					 TextField noOfDaysPayableText = (TextField) hashMap.get("noOfDaysPayable");
					 TextField noOfDaysAllowedText = (TextField) hashMap.get("noOfDaysAllowed");
					 TextField maxNoOfDaysPerHospitalText = (TextField) hashMap.get("maxNoOfDaysPerHospital");
					 TextField noOfDaysAvailableText = (TextField) hashMap.get("noOfDaysAvailable");
					 
					 
					 
					 TextField allowedAmountPerDayFld = (TextField) hashMap.get("allowedAmountPerDay");
					 
					 Double netAmt = 0d ;
					 Double amountperday = 0d;
					 Double noOfDaysClaimed = 0d;
					 Double siLimit = 0d;
					 Double limit = 0d;
					 Double totalClaimed = 0d;
					 Double deduction = 0d;         // CR2019100 -  Deduction for Medical Extention ROD
					 Double amtPerDayPayable = 0d;
					 Double noOfDaysPayable = 0d;
					 Double noOfDaysAllowed = 0d;
					 Double maxNoOfDaysPerHospital = 0d;
					 Double noOfDaysAvailable = 0d;
					 Double allowedAmountPerDay = 0d;
					 Double amountPayablePerDay = 0d;
					 NumberFormat format = NumberFormat.getInstance(Locale.US);

					 if(null != amountperdayText && null != amountperdayText.getValue())
					 {
						 Number number = format.parse(amountperdayText.getValue()); 
						 amountperday = number.doubleValue();
					 }
					 if(null != allowedAmountPerDayFld && null != allowedAmountPerDayFld.getValue())
					 {
						 Number number = format.parse(allowedAmountPerDayFld.getValue());
						 allowedAmountPerDay = number.doubleValue();	 
					 }
					
					 amountPayablePerDay = Math.min(amountperday, allowedAmountPerDay);
					 if(null != amtPerDayPayableText)
					 {
						 amtPerDayPayableText.setReadOnly(false);
						 amtPerDayPayableText.setValue(String.valueOf(amountPayablePerDay));
						 amtPerDayPayableText.setReadOnly(true);
					 }
					
					 if(null != noOfDaysClaimedText && null != noOfDaysClaimedText.getValue())
					 {
						 Number number = format.parse(noOfDaysClaimedText.getValue());
						 noOfDaysClaimed = number.doubleValue();
					 }
					 
					 if(optionalCover!=null && optionalCover.getValue().toString().equals("MEDICAL EXTENSION")){
						 if(null != siLimittext && null != siLimittext.getValue())
						 {
							 Number number = format.parse(siLimittext.getValue());
							 siLimit = number.doubleValue();
						 }
						 if(null != limitText && null != limitText.getValue())
						 {
							 Number number = format.parse(limitText.getValue());
							 limit = number.doubleValue();
						 }
						 if(null != totalClaimedtext && null != totalClaimedtext.getValue())
						 {
							 Number number = format.parse(totalClaimedtext.getValue());
							 totalClaimed = number.doubleValue();
						 }
						 
						// CR2019100 -  Deduction for Medical Extention ROD
						 if(null != deductiontext && null != deductiontext.getValue())
						 {
							 Number number = format.parse(deductiontext.getValue());
							 deduction = number.doubleValue();
						 }
						 
						 
						 netAmt = Math.min(siLimit, Math.min(limit, (totalClaimed - deduction)));   // CR2019100 -  Deduction for Medical Extention ROD
						 
						 dto.setAppAmt(netAmt);  // CR2019100 -  Deduction for Medical Extention ROD
						 
						 if(netAmtText!=null){
								netAmtText.setValue(String.valueOf(new BigDecimal(netAmt)));
							}
					 }
					 //code added for handling PA saral sureksha bima product by noufel
					 else if(optionalCover!=null && (optionalCover.getValue().toString().equals("Temporary Total Disablement")) || 
							 (optionalCover.getValue().toString().equals("EDUCATIONAL GRANT"))){
						 if(null != siLimittext && null != siLimittext.getValue())
						 {
							 Number number = format.parse(siLimittext.getValue());
							 siLimit = number.doubleValue();
						 }
						 if(null != limitText && null != limitText.getValue())
						 {
							 Number number = format.parse(limitText.getValue());
							 limit = number.doubleValue();
						 }
						 if(null != totalClaimedtext && null != totalClaimedtext.getValue())
						 {
							 Number number = format.parse(totalClaimedtext.getValue());
							 totalClaimed = number.doubleValue();
						 }
						 if(noOfDaysAllowedText!=null){
							 if(null != noOfDaysAllowedText.getValue()){
							 Number number = format.parse(noOfDaysAllowedText.getValue());
							 noOfDaysAllowed = number.doubleValue();
							 }
						 }
						 totalClaimed = noOfDaysClaimed *  amountPayablePerDay;
						 if(null != totalClaimedtext)
						 {
							 totalClaimedtext.setReadOnly(false);
							 totalClaimedtext.setValue(String.valueOf(totalClaimed));
							 totalClaimedtext.setReadOnly(true);
						 }
						 
						 if(null != deductiontext && null != deductiontext.getValue())
						 {
							 Number number = format.parse(deductiontext.getValue());
							 deduction = number.doubleValue();
						 }
						 totalClaimed = amountPayablePerDay * noOfDaysAllowed ;
						 
						 netAmt = Math.min(siLimit, Math.min(limit, (totalClaimed - deduction)));   
						 
						 dto.setAppAmt(netAmt);
						 if(netAmtText!=null){
								netAmtText.setValue(String.valueOf(new BigDecimal(netAmt)));
							}
					 }else{
						 if(totalClaimedtext!=null ){
							 Double totalClaimAmt = amountperday * noOfDaysClaimed;
							 if(totalClaimAmt!=0){
								 totalClaimedtext.setReadOnly(false);
								 totalClaimedtext.setValue(totalClaimAmt.toString());
								 totalClaimedtext.setReadOnly(Boolean.TRUE);
							 }
						 }
						
						 
						 if(noOfDaysAllowedText!=null){
							 if(null != noOfDaysAllowedText.getValue()){
							 Number number = format.parse(noOfDaysAllowedText.getValue());
							 noOfDaysAllowed = number.doubleValue();
							 }
						 }
						 if(maxNoOfDaysPerHospitalText!=null){
							 if(null != maxNoOfDaysPerHospitalText.getValue())
							 {
							 Number number = format.parse(maxNoOfDaysPerHospitalText.getValue());
							 maxNoOfDaysPerHospital = number.doubleValue();
							 }
						 }
						 if(noOfDaysAvailableText!=null){
							 if(null != noOfDaysAvailableText.getValue())
							 {
							 Number number = format.parse(noOfDaysAvailableText.getValue());
							 noOfDaysAvailable = number.doubleValue();
							 }
						 }
						 
						 
						 
						/* if(noOfDaysPayableText!=null){
							 Number number = format.parse(noOfDaysPayableText.getValue());
							 noOfDaysPayable = number.doubleValue();
						 }*/
						 noOfDaysPayable = Math.min(noOfDaysAllowed, Math.min(maxNoOfDaysPerHospital, noOfDaysAvailable));
						 
						 
						 /*if(amtPerDayPayableText!=null){
							 amtPerDayPayableText.setReadOnly(Boolean.FALSE);
							 amtPerDayPayableText.setValue(noOfDaysPayable.toString());
							 amtPerDayPayableText.setReadOnly(Boolean.TRUE);
						 }*/
						 netAmt = amountPayablePerDay * noOfDaysPayable;
						 
					 }
					 
					 if(null != netAmtFld){
						 netAmtFld.setReadOnly(Boolean.FALSE);
						 netAmtFld.setValue(String.valueOf(netAmt));
						 netAmtFld.setReadOnly(Boolean.TRUE);
					 }
					 
				} catch (ReadOnlyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			     }
				 //	netAmtText.setValue(String.valueOf(netAmtFld));}
		}
	 
	 
	 public void addNetAmountListener(final TextField total){
			
			if(null != total)
			{
				
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					calculateNetAmount(total);
					calculateTotal();
					//calculateTotalAmount(total);
					
				}
			});
			}
		}
		

		
		private void calculateTotal()
		{
			List<OptionalCoversDTO> tableList = (List<OptionalCoversDTO>) table.getItemIds();
			Double netAmt = 0d;
			if(null != tableList && !tableList.isEmpty())
			{
				for (OptionalCoversDTO tableBenefitsDTO : tableList) {
					netAmt += tableBenefitsDTO.getAppAmt();
				}
				table.setColumnFooter("appAmt", String.valueOf(new BigDecimal(netAmt)));
				if(netAmtText!=null){
					netAmtText.setValue(String.valueOf(new BigDecimal(netAmt)));
				}
			}
		}
		
		private void doReadOnly(OptionalCoversDTO optionalCoversDTO,
				TextField field) {
			if(optionalCoversDTO.getOptionalCover()!=null){
				String addOnValue = optionalCoversDTO.getOptionalCover().getValue();
				Boolean isreadOnly = isMedicalExt(addOnValue);
				field.setValue("NA");
				field.setReadOnly(isreadOnly);
				if(Boolean.TRUE.equals(isreadOnly)){
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
				
			}
		}
		
		
		public void addDaysListener(final TextField total){
			
			if(null != total)
			{
				
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					calculateNoOfDaysPayable(total);
					//calculateTotal();
					//calculateTotalAmount(total);
					
				}
			});
			}
		}
			
		
		
		private void calculateNoOfDaysPayable(TextField txtField)
		{
			if(null != txtField)
			{
				OptionalCoversDTO dto = (OptionalCoversDTO) txtField.getData();
				if(null != dto)
				{
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
					if(null != hashMap)
					{
						TextField noOfDaysAllowedFld = (TextField) hashMap.get("noOfDaysAllowed");
						TextField maxNoOfDaysPerHospitalFld = (TextField) hashMap.get("maxNoOfDaysPerHospital");
						TextField noOfDaysAvailableFld = (TextField) hashMap.get("noOfDaysAvailable");
						TextField noOfDaysPayable = (TextField) hashMap.get("noOfDaysPayable");
						TextField noOfDaysClaimedFld = (TextField) hashMap.get("noOfDaysClaimed");
						Integer noOfDaysAllowed = 0;
						Integer maxNoOfDaysPerHosp = 0;
						Integer noOfDaysAvailable = 0;
						Integer noOfDaysClaimed = 0;
						if(null != noOfDaysClaimedFld && null != noOfDaysClaimedFld.getValue())
						{								
							noOfDaysClaimed = Integer.valueOf(SHAUtils.getIntegerFromStringWithComma(noOfDaysClaimedFld.getValue()));							
						}
						
						if(null != noOfDaysAllowedFld && null != noOfDaysAllowedFld.getValue())
						{
							noOfDaysAllowed = Integer.valueOf(noOfDaysAllowedFld.getValue());
						}
						
						if(noOfDaysAllowed > noOfDaysClaimed)
						{

							 
						 	Label label = new Label("No of allowable days is greater than the number of days claimed", ContentMode.HTML);
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
						/* HorizontalLayout layout = new HorizontalLayout(
									new Label(""));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("Errors");
							dialog.setWidth("35%");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);*/
							noOfDaysAllowedFld.setValue(null);
							/*if(null != txtAmount)
								txtAmount.setEnabled(false);*/
					 
						}
						else
						{
						
							if(null != maxNoOfDaysPerHospitalFld && null != maxNoOfDaysPerHospitalFld.getValue())
							{
								maxNoOfDaysPerHosp = Integer.valueOf(maxNoOfDaysPerHospitalFld.getValue());
							}
							if(null != noOfDaysAvailableFld && null != noOfDaysAvailableFld.getValue())
							{
								noOfDaysAvailable = Integer.valueOf(noOfDaysAvailableFld.getValue());
							}
							if(null != noOfDaysPayable)
							{
								Integer number = Math.min(noOfDaysAllowed, maxNoOfDaysPerHosp );
								Integer number1 = Math.min(number , noOfDaysAvailable);
								if(null != number1)
								{
									noOfDaysPayable.setReadOnly(false);
									noOfDaysPayable.setValue(String.valueOf(number1));
									noOfDaysPayable.setReadOnly(true);
								}
							}
						}
					}
					 
				}
			}
		}
		
	private Boolean isMedicalExt(String addonValue){
		if(addonValue.equalsIgnoreCase("MEDICAL EXTENSION")){
			return Boolean.TRUE;
		}	
		return Boolean.FALSE;
	}
	
	
	 @SuppressWarnings("unused")
		private void addCoversListener(
				final ComboBox categoryCombo) {
			if (categoryCombo != null) {
				categoryCombo.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						Boolean isError = false;
						ComboBox component = (ComboBox) event.getComponent();
						OptionalCoversDTO coversDTO = (OptionalCoversDTO) component.getData();
						if(null != component)
						{
						if(null != coversDTO)
						{							
							if(!isError)
							{
							 HashMap<String, AbstractField<?>> hashMap = tableItem.get(coversDTO);
							 if(null != hashMap)
							 {
								 SelectValue coversCombo = (SelectValue)component.getValue();
								 TextField eligibleForPolicyFld = (TextField) hashMap.get("eligibleForPolicy");
								// TextField amountClaimedFld = (TextField) hashMap.get("claimedAmount");
									/*if(null != coversCombo && null != coversCombo.getId())
									{
										fireViewEvent(PABillingWizardPresenter.PA_VALIDATE_OPTIONAL_COVERS,coversCombo.getId(),eligibleForPolicyFld);
									}*/
								 if(null != coversCombo && null != coversCombo.getId()){
								 List<AddOnCoversTableDTO> optionalCoversList = (List<AddOnCoversTableDTO>) referenceData
											.get("optionalCoverProc");
								 
								 if(null != optionalCoversList && !optionalCoversList.isEmpty())
									{
										for (AddOnCoversTableDTO addOnCoversTableDTO : optionalCoversList) {
											if(null != addOnCoversTableDTO && null != addOnCoversTableDTO.getCoverId() && coversCombo.getId().equals(addOnCoversTableDTO.getCoverId()))
											{
												if(null != eligibleForPolicyFld)
												{
													//if(SHAConstants.YES_FLAG.equalsIgnoreCase(addOnCoversTableDTO.getEligibleForPolicy()))
													eligibleForPolicyFld.setValue(addOnCoversTableDTO.getEligibleForPolicy());
												}
												/**
												 * Added for issue no 5382 in PA.
												 * */
												}
											}
										}
								 	}
					
							 	}
							}
						}	
						}
					}

				});
			}

		}

	 
	 public void populateCoversTableData(Long coverId,TextField eligibleForPolicyFld,List<AddOnCoversTableDTO> coversDTOList)	
	 {
		 if(null != coversDTOList && !coversDTOList.isEmpty())
			{
				for (AddOnCoversTableDTO optionalCoversTableDTO : coversDTOList) {
					if(null != optionalCoversTableDTO && null != optionalCoversTableDTO.getCoverId() && coverId.equals(optionalCoversTableDTO.getCoverId()))
					{
					if(null != eligibleForPolicyFld)
						{						
							eligibleForPolicyFld.setValue(optionalCoversTableDTO.getEligibleForPolicy());
						}
						
					}
				}
			}
	 }
	 
	 public void setupReferences(Map<String, Object> referenceData) {
			this.referenceData = referenceData;
	 }
	 
	 public boolean validateDeductionAmt() {

		 List<OptionalCoversDTO> tableList = getValues();
		 boolean error = false;
		 if(tableList != null && !tableList.isEmpty()) {
			 for (OptionalCoversDTO optionalCoversDTO : tableList) {
				 
				 if(optionalCoversDTO.getOptionalCover() != null
						 && optionalCoversDTO.getOptionalCover().getValue() != null
						 && isMedicalExt(optionalCoversDTO.getOptionalCover().getValue())) {
					 if(optionalCoversDTO.getDeduction() == null || (optionalCoversDTO.getDeduction() != null && (optionalCoversDTO.getDeduction().longValue() > (optionalCoversDTO.getTotalClaimed() != null ? optionalCoversDTO.getTotalClaimed().longValue() : 0l)))) {
						 error = true;
					 }
					 if(error)
						 break;
				 }
				 if(error)
					 break;
			}
		 }
		 return error;
	 }
	 
	 public void removeAllItems()
	 {
		 table.removeAllItems();
	 }
	 
	 public void removeandAddAllItems(List<OptionalCoversDTO> optListBilling)
	 {
		 removeAllItems();
		 for(OptionalCoversDTO coversDTO:optListBilling){
			 addBeanToList(coversDTO);
		 }
	 }
	 
}
