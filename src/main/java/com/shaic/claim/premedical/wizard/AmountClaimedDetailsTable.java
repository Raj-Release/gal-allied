package com.shaic.claim.premedical.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import ch.meemin.pmtable.PMTreeTable;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.AmountRequestedDetailField;
import com.shaic.arch.fields.AmountRequestedField;
import com.shaic.arch.table.EnhanceTableFactory;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.ClaimDetailsTableBean;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.PMTableRow;
import com.shaic.claim.premedical.rule.PreauthRule;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class AmountClaimedDetailsTable extends ViewComponent {

	/**
	 * 15 Room rent 
	 * 9 ICU
	 * 8 Ambulance 
	 */
	private static final long serialVersionUID = 5411865540250106214L;
	
	private static final int TOTAL_CALCULATION_ROW = 16;
	
	protected PMTreeTable pmTreeTable;
	
	private PreauthDTO preauthDto;
	
	private TextField noOfTextField;
	
	private Map<String, Double> dbCalculationValues;
	
	public Boolean isANHOrCompositeSelected = false;
	
	Integer[] mandatoryBenefitId = {8, 9, 16, 17};
	
	protected String roomRentAmountConsidered;
	protected String roomRentAmountRequested;
	
	Integer[] childItemId = {1,8,9,12,13,14,16};
	Integer[] parentItemId = {1,7, 11};
	Integer[] checboxItemId = {13, 14};
	Integer[] ignoreItemId = {1, 7, 11, 16};
	Integer[] roomRentICUId = {2, 3};
	Integer[] amountConsiderCalculationIds = {8, 9, 12};
	String[] calculatedColumns = {"Deductibles", "Net Amount", "Amount Considered"};
	int i = 1;
	Map<Integer, String> checkboxMap = new HashMap<Integer, String>();
	
	EnhanceTableFactory tableFactory = new EnhanceTableFactory();
	
	public void setDBCalculationValues(Map<String, Double> values) {
		this.dbCalculationValues = values;
	}
	
	public void initView(PreauthDTO preauthDTO) {
		this.preauthDto = preauthDTO;
		checkboxMap.put(13, "Override Package Deduction");
		checkboxMap.put(14, "Restrict to 80%");
		//Adding header for Amount claimed details page.
		pmTreeTable = new PMTreeTable("Amount Claimed Details");
//		pmTreeTable.setTableFieldFactory(tableFactory);
		pmTreeTable.addContainerProperty("S.No", Label.class, null );
		pmTreeTable.addContainerProperty("Details", AmountRequestedDetailField.class, null );
		pmTreeTable.addContainerProperty("Amount Requested", AmountRequestedField.class, null);
		pmTreeTable.addContainerProperty("Deductibles", TextField.class, null);
		pmTreeTable.addContainerProperty("Net Amount", TextField.class, null);
		pmTreeTable.addContainerProperty("Amount Considered", TextField.class, null);
		pmTreeTable.setStyleName(ValoTheme.TABLE_COMPACT);
		pmTreeTable.setWidth("100%");
		//pmTreeTable.setHeight("375px");
		
		pmTreeTable.setColumnWidth("Detail", 405);
		pmTreeTable.setColumnWidth("Amount Requested", 170);
		pmTreeTable.setColumnWidth("Deductibles", 100);
		
		// Detail column is used as Tree..
		pmTreeTable.setHierarchyColumn("Details");
		
		setupTable(ClaimDetailsTableBean.getPMTableRows());
		
		pmTreeTable.removeItem(1);
		
		//pmTreeTable.setCaption("Amount Claimed Details");
		
		setCompositionRoot(pmTreeTable);
		
	
	}
	
	@PostConstruct
	public void init()
	{
		
	}
	
	private void setupTable(PMTableRow root)
	{
		addTreeMenuItem(root, null);
		
	}
	
	@SuppressWarnings("unchecked")
	private void addTreeMenuItem(PMTableRow menu, Object parent) {
		
		Object itemId = pmTreeTable.addItem();
		Item parentItem = pmTreeTable.getItem(itemId);
		AmountRequestedDetailField amountRequestedDetailField;
		if(Arrays.asList(this.checboxItemId).contains(itemId)) {
			amountRequestedDetailField = new AmountRequestedDetailField(false, menu.getDetailLabel(), checkboxMap.get(itemId));
			CheckBox checkboxField = amountRequestedDetailField.getCheckboxField();
			checkboxField.addValueChangeListener(getCheckboxListener(menu.getMasterId()));
		} else {
			amountRequestedDetailField = new AmountRequestedDetailField(true, menu.getDetailLabel(), null);
		}
		// This is used to identify the master id.
		amountRequestedDetailField.setData(menu.getMasterId());
		parentItem.getItemProperty("Details").setValue(amountRequestedDetailField);
		
		if(!Arrays.asList(childItemId).contains(itemId)) {
			parentItem.getItemProperty("S.No").setValue(new Label((String.valueOf(i++))));
		}
		
		if(!Arrays.asList(parentItemId).contains(itemId)) {
			Property itemProperty1 = parentItem.getItemProperty("Amount Requested");
			AmountRequestedField amountRequestedField;
			if(Arrays.asList(roomRentICUId).contains(itemId)) {
				amountRequestedField = new AmountRequestedField(true);
				amountRequestedField.disableAmountRequested();
			} else {
				amountRequestedField = new AmountRequestedField(false);
			}
			amountRequestedField.setData(itemId);
			TextField listenerField = amountRequestedField.getListenerField();
			listenerField.setData(itemId);
			itemProperty1.setValue(amountRequestedField);
			
			Property deductiblesProperty = parentItem.getItemProperty("Deductibles");
			TextField deductibleTxt = new TextField();
			/**
			 * Adding validation to amount claimed field.
			 * */
			CSValidator deductibleValidator = new CSValidator();
			
			deductibleValidator.extend(deductibleTxt);
			deductibleValidator.setRegExp("^[0-9]*$"); // Should allow only 0 to 9 and '.'
			deductibleValidator.setPreventInvalidTyping(true);
			
			deductibleTxt.setWidth("80px");
			deductibleTxt.setData(itemId);
			deductibleTxt.setNullRepresentation("0");
			deductiblesProperty.setValue(deductibleTxt);
			
			TextField netAmtTxt = new TextField();
			netAmtTxt.setWidth("80px");
			netAmtTxt.setReadOnly(true);
			netAmtTxt.setNullRepresentation("0");
			netAmtTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			parentItem.getItemProperty("Net Amount").setValue(netAmtTxt);
			
			TextField amountConsiderTxt = new TextField();
			amountConsiderTxt.setWidth("80px");
			amountConsiderTxt.setReadOnly(true);
			amountConsiderTxt.setNullRepresentation("0");
			amountConsiderTxt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			parentItem.getItemProperty("Amount Considered").setValue(amountConsiderTxt);
			
			if((int)itemId != 16) {
				listenerField.addValueChangeListener(getAmountListener(true));
				netAmtTxt.addValueChangeListener(getNetAmountListener("Net Amount"));
				amountConsiderTxt.addValueChangeListener(getConsiderAmtListener("Amount Considered"));
				deductibleTxt.addValueChangeListener(getDeductibleListener());
			} else {
				amountRequestedField.setEnabled(false);
				listenerField.setEnabled(false);
				netAmtTxt.setEnabled(false);
				amountConsiderTxt.setEnabled(false);
				deductibleTxt.setEnabled(false);
			}
	
		}
		pmTreeTable.setCollapsed(itemId, false);
	
		if (parent != null) {
			pmTreeTable.setParent(itemId, parent);
		}
		
		if (menu.hasChild()) {
			pmTreeTable.setChildrenAllowed(itemId, true);
			for (PMTableRow childItem : menu.getChildRow()) {
				addTreeMenuItem(childItem, itemId);
			}
		}
		else {
			pmTreeTable.setChildrenAllowed(itemId, false);
		}
	}
	
	public Object getTotalFieldProperty(String itemPropertyId) {
		Item item = this.pmTreeTable.getItem(16);
		Object propertyObject = item.getItemProperty(itemPropertyId).getValue();
		return propertyObject;
	}
	
	public String getTotalAmountByPropertyName(String itemPropertyId) {
		Item item = this.pmTreeTable.getItem(16);
		Object propertyObject = item.getItemProperty(itemPropertyId).getValue();
		if(propertyObject instanceof AmountRequestedField) {
			AmountRequestedField field =  (AmountRequestedField) propertyObject;
			 return field.getAmount().toString();
		}
		TextField field = (TextField) propertyObject;
		return field.getValue();
	}
	
	public List<AmountRequestedField> getNoOfDaysFields() {
		List<AmountRequestedField> fields = new ArrayList<AmountRequestedField>();
		Item roomRentItem = pmTreeTable.getItem(2);
		Item ICUItem = pmTreeTable.getItem(3);
		AmountRequestedField roomRentItemField = (AmountRequestedField) roomRentItem.getItemProperty("Amount Requested").getValue();
		AmountRequestedField ICUItemField = (AmountRequestedField) ICUItem.getItemProperty("Amount Requested").getValue();
		fields.add(roomRentItemField);
		fields.add(ICUItemField);
		
		return fields;
	}
	
	public Integer getNoOfDaysValues() {
		List<AmountRequestedField> fields = new ArrayList<AmountRequestedField>();
		Item roomRentItem = pmTreeTable.getItem(2);
		Item ICUItem = pmTreeTable.getItem(3);
		AmountRequestedField roomRentItemField = (AmountRequestedField) roomRentItem.getItemProperty("Amount Requested").getValue();
		AmountRequestedField ICUItemField = (AmountRequestedField) ICUItem.getItemProperty("Amount Requested").getValue();
		
		return ((roomRentItemField.getNoOfDays() + ICUItemField.getNoOfDays()));
	}
	
	private void updateSummation()
	{
		Item summationItem = this.pmTreeTable.getItem(TOTAL_CALCULATION_ROW);
		
		Integer amountConsider = 0;
		Integer netAmount = 0;
		Integer deductableAmount = 0;
		Integer amountRequested = 0;
		
		
		
		for(int i = 1; i < 16; i++)
		{
			Item rowItem = this.pmTreeTable.getItem(i);
			if (rowItem != null)
			{
				amountConsider += getValueForProperty(rowItem, "Amount Considered");
				netAmount += getValueForProperty(rowItem, "Net Amount");
				deductableAmount += getValueForProperty(rowItem, "Deductibles");
				amountRequested += getValueForProperty(rowItem, "Amount Requested");
			}
		}
		pmTreeTable.setColumnFooter("Amount Considered", String.valueOf(amountConsider));
		pmTreeTable.setColumnFooter("Deductibles", String.valueOf(deductableAmount));
		pmTreeTable.setColumnFooter("Amount Requested", String.valueOf(amountRequested));
		pmTreeTable.setColumnFooter("Net Amount", String.valueOf(netAmount));
		
		setValuesToRow(summationItem, "Amount Considered", amountConsider);
		setValuesToRow(summationItem, "Net Amount", netAmount);
		setValuesToRow(summationItem, "Deductibles", deductableAmount);
		setValuesToRow(summationItem, "Amount Requested", amountRequested);
		
	}
	
	private void setValuesToRow(Item rowItem, String propertyId, float value)
	{
		if (rowItem.getItemProperty(propertyId) != null && rowItem.getItemProperty(propertyId).getValue() != null & rowItem.getItemProperty(propertyId).getValue() instanceof TextField)
		{
				TextField tAConsider = (TextField) rowItem.getItemProperty(propertyId).getValue();
				boolean flag = false;
				if (tAConsider.isReadOnly())
				{
					tAConsider.setReadOnly(false);	
					flag = true;
				}
				tAConsider.setValue("" + value);
				if (flag)
				{
					tAConsider.setReadOnly(true);	
				}
				
		}
		else if (rowItem.getItemProperty(propertyId) != null && rowItem.getItemProperty(propertyId).getValue() != null & rowItem.getItemProperty(propertyId).getValue() instanceof Label)
		{
			Label tAConsider = (Label) rowItem.getItemProperty(propertyId).getValue();
			tAConsider.setValue("" + value);
		}
		else if(rowItem.getItemProperty(propertyId) != null && rowItem.getItemProperty(propertyId).getValue() != null & rowItem.getItemProperty(propertyId).getValue() instanceof AmountRequestedField)
		{
			AmountRequestedField taAmount = (AmountRequestedField) rowItem.getItemProperty(propertyId).getValue();
			taAmount.setAmtTxt("" + value);
		}
	}
	
	private Integer getValueForProperty(Item rowItem, String propertyId)
	{
		Property itemProperty = rowItem.getItemProperty(propertyId);
		Integer amountConsider = 0;
		
		if (itemProperty != null)
		{
			Object fieldObject = itemProperty.getValue();
			String aConsiderValue = "0.0";
			if (fieldObject instanceof Label)
			{
				aConsiderValue  = ((Label) fieldObject).getValue();
			}
			else if (fieldObject instanceof TextField)
			{
				aConsiderValue = ((TextField) fieldObject).getValue();
			}
			else if (fieldObject instanceof AmountRequestedField)
			{
				return ((AmountRequestedField) fieldObject).getAmount();
			}
			
			
			if (SHAUtils.isValidFloat(aConsiderValue))
			{
				amountConsider += SHAUtils.getFloatFromString(aConsiderValue);
			}	
		}
		return amountConsider;
	}

	private void setTotal(String value, String itemPropertyId) {
//		if(value != null && value.length() > 0) {
//			Item item =  this.pmTreeTable.getItem(16);
//			Object propertyObject = item.getItemProperty(itemPropertyId).getValue();
//			String filledValue;
//			Float sumValue;
//			if(propertyObject instanceof Label) {
//				Label label = (Label) propertyObject;
//				filledValue = label.getValue();
//				sumValue = SHAUtils.getFloatFromString(value);
//				if(filledValue != null && filledValue.length() > 0) {
//					sumValue = SHAUtils.getFloatFromString(filledValue) + SHAUtils.getFloatFromString(value);
//				} 
//				label.setValue(sumValue.toString());
//				return;
//			} else if(propertyObject instanceof TextField) {
//				TextField textField = (TextField) propertyObject;
//				Boolean isReadOnly = false;
//				if(textField.isReadOnly()) {
//					textField.setReadOnly(false);
//					isReadOnly = true;
//				}
//				filledValue = textField.getValue();
//				sumValue = SHAUtils.getFloatFromString(value);
//				if(filledValue != null && filledValue.length() > 0) {
//					sumValue = SHAUtils.getFloatFromString(filledValue) + SHAUtils.getFloatFromString(value);
//				} 
//				textField.setValue(sumValue.toString());
//				textField.setReadOnly(isReadOnly);
//				pmTreeTable.setColumnFooter("Amount Considered", String.valueOf(sumValue));
//				pmTreeTable.setColumnFooter("Detail", "Total");
//				
//			} else if (propertyObject instanceof AmountRequestedField) {
//				AmountRequestedField amountRequestedField = (AmountRequestedField) propertyObject;
//				sumValue = SHAUtils.getFloatFromString(value);
//				if(amountRequestedField.getAmount() != null) {
//					sumValue = amountRequestedField.getAmount() + SHAUtils.getFloatFromString(value);
//				} 
//				amountRequestedField.getListenerField().setValue(sumValue.toString());
////				pmTreeTable.setColumnFooter("Amount Requested", String.valueOf(sumValue));
//			}
//		}
		updateSummation();
	}
	
	private ValueChangeListener getAmountListener(final Boolean invokeAgain) {
		
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 7342288705079773186L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				TextField property = (TextField) event.getProperty();
				if(!SHAUtils.isValidFloat(property.getValue())) {
					property.setValue("0");
					Notification notify = new Notification("Message");
					notify.setDelayMsec(-1);
				} else {
					setAmtRequestedCalculationValues(property);
				}
				}
		};
		return listener;
	}
	
	private ValueChangeListener getCheckboxListener(final Integer benefitId) {
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 7342288705079773186L;
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				CheckBox property = (CheckBox) event.getProperty();
				Item item = null;
				if(benefitId == 17) {
					item = pmTreeTable.getItem(13);
				} else if(benefitId == 18) {
					item = pmTreeTable.getItem(14);
				}
				AmountRequestedField amountRequestedField = (AmountRequestedField) item.getItemProperty("Amount Requested").getValue();
				TextField listenerField = amountRequestedField.getListenerField();
				setAmtRequestedCalculationValues(listenerField);
			}
		};
		return listener;
	}
	
	
	private void setAmtRequestedCalculationValues(TextField amtTxt) {
		TextField property = amtTxt;
		String amountRequestedValue = property.getValue();
		Boolean isRoomRentChanged = false;
		Object itemId = property.getData();
		if((int)itemId == 2) {
			
			if(roomRentAmountRequested != null &&  SHAUtils.isValidFloat(roomRentAmountRequested) && SHAUtils.isValidFloat(amountRequestedValue)  && !roomRentAmountRequested.equals(amountRequestedValue) ) {
				isRoomRentChanged = true;
			}	
			roomRentAmountRequested = amountRequestedValue;
		}
		Item item = pmTreeTable.getItem(itemId);
		TextField deductibleField = (TextField) item.getItemProperty("Deductibles").getValue();
		TextField netAmountField = (TextField) item.getItemProperty("Net Amount").getValue();
		TextField amountConsideredField = (TextField) item.getItemProperty("Amount Considered").getValue();
		AmountRequestedField amountRequestedField = (AmountRequestedField) item.getItemProperty("Amount Requested").getValue();
		AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("Details").getValue();
		Object benefitId = detailField.getData();
		PreauthRule rule = new PreauthRule();
			netAmountField.setReadOnly(false);
			amountConsideredField.setReadOnly(false);
			if(amountRequestedValue == null || amountRequestedValue == "0" || amountRequestedValue == "0.0" || amountRequestedValue.length() == 0) {
				amountRequestedValue = "0";
			}
			
			if(!isANHOrCompositeSelected && (int)benefitId != 8 && (int)benefitId != 9 && SHAUtils.getFloatFromString(amountRequestedValue) < SHAUtils.getFloatFromString(deductibleField.getValue())) {
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Deductible Amount should be lesser than Amount Requested. </b>", ContentMode.HTML));
				layout.setMargin(true);
				layout.setSpacing(true);
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setClosable(true);
				dialog.setResizable(false);
				dialog.setContent(layout);
				dialog.setCaption("Error");
				dialog.setClosable(true);
				dialog.show(getUI().getCurrent(), null, true);
				deductibleField.setReadOnly(false);
				deductibleField.setValue("0");
				return;
			}
			
			updateSummation();
			if(Arrays.asList(roomRentICUId).contains(itemId)) {
				String roomRentNetAmountChargesCalculation = rule.roomRentNetAmountChargesCalculation(amountRequestedField.getNoOfDays().toString(), dbCalculationValues != null ? dbCalculationValues.get(benefitId.toString()).toString() : "0");
				String amountConsideredValue = "0";
				amountConsideredValue = rule.roomRentAmountConsideredChargesCalculation(amountRequestedField.getAmount().toString(), roomRentNetAmountChargesCalculation);
				
				netAmountField.setValue(amountConsideredValue);
				
				String roomRentDeductibleChargesCalculation = rule.roomRentNetDeductibleChargesCalculation(property.getValue(), amountConsideredValue);
				deductibleField.setReadOnly(false);
				deductibleField.setValue(roomRentDeductibleChargesCalculation);
				deductibleField.setReadOnly(true);
				
				amountConsideredField.setReadOnly(false);
				amountConsideredField.setValue(amountConsideredValue);
				if((int)itemId == 2) {
					roomRentAmountConsidered = amountConsideredValue;
				}
				amountConsideredField.setReadOnly(true);
				
				if(isRoomRentChanged) {
					setValues(getValues(), true);
				}
				
			} else if((int)itemId == 10) {
				String roomRentNetAmountChargesCalculation = rule.roomRentNetAmountChargesCalculation("1", dbCalculationValues != null ? dbCalculationValues.get(benefitId.toString()).toString() : "0");
				String amountConsideredValue = "0";
				amountConsideredValue = rule.roomRentAmountConsideredChargesCalculation(amountRequestedField.getAmount().toString(), roomRentNetAmountChargesCalculation);
				
				netAmountField.setValue(amountConsideredValue);
				
				String roomRentDeductibleChargesCalculation = rule.roomRentNetDeductibleChargesCalculation(property.getValue(), roomRentNetAmountChargesCalculation);
				deductibleField.setReadOnly(false);
				deductibleField.setValue(roomRentDeductibleChargesCalculation);
			
				amountConsideredField.setReadOnly(false);
				amountConsideredField.setValue(amountConsideredValue);
			}
			
			else {
				if((int)itemId == 12 || (int)itemId == 13) {
					Boolean isEnabled = true;
					String value = property.getValue();
					if(!isANHOrCompositeSelected && value != null &&value.length() > 0 && !(value.equalsIgnoreCase("0.0") || value.equalsIgnoreCase("0")) ) {
						enableOrdisableItem(false, itemId, true);
					}  else if(value != null && isANHOrCompositeSelected && (value.length() == 0 || value.equalsIgnoreCase("0.0") || value.equalsIgnoreCase("0"))) {
						Item previousItem = (int)itemId == 13 ? pmTreeTable.getItem(12) : pmTreeTable.getItem(13);
						AmountRequestedField previousAmountRequestedField = (AmountRequestedField) previousItem.getItemProperty("Amount Requested").getValue();
						if(previousAmountRequestedField.getAmount() <= 0) {
							enableOrdisableItem(true, null, true);
						}
					}
				}
				
				String otherDetailsNetAmountCalculation = null;
				otherDetailsNetAmountCalculation = rule.otherDetailsNetAmountCalculation(property.getValue(),deductibleField.getValue());
				netAmountField.setReadOnly(false);
				netAmountField.setValue(otherDetailsNetAmountCalculation);
				netAmountField.setReadOnly(true);
				
				String otherDetailsAmountConsideredCalculation = otherDetailsNetAmountCalculation;
				
				if((int)itemId == 13) {
					if(detailField.getCheckboxValue()) {
						otherDetailsAmountConsideredCalculation = rule.compositePackageWithOverrideDeductionConsiderAmt(otherDetailsNetAmountCalculation);
					} else {
						otherDetailsAmountConsideredCalculation = rule.compositePackageWithoutOverrideDeductionConsiderAmt(otherDetailsNetAmountCalculation);
					}
				} else if((int)itemId == 14) {
					if(detailField.getCheckboxValue()) {
						otherDetailsAmountConsideredCalculation = rule.otherPackageWithRestrict(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
					} else {
						otherDetailsAmountConsideredCalculation = rule.otherPackageWithOutRestrict(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
					}
				} else if(!Arrays.asList(amountConsiderCalculationIds).contains(itemId)) {
					otherDetailsAmountConsideredCalculation = rule.otherDetailsAmountConsideredCalculation(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
				} 
				amountConsideredField.setReadOnly(false);
				amountConsideredField.setValue(otherDetailsAmountConsideredCalculation);	
			}
			netAmountField.setReadOnly(true);
			amountConsideredField.setReadOnly(true);
	} 
	
	
	private ValueChangeListener getDeductibleListener() {
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = -6840859302219285675L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				TextField property = (TextField) event.getProperty();
				if(!SHAUtils.isValidFloat(property.getValue())) {
					property.setValue("0");
					Notification notify = new Notification("Message");
					notify.setDelayMsec(-1);
//					Notification.show("Please Enter Valid Deductible Amount.");
				} else {
					setDeductibleCalcualtionVaules(property);
				}
				
			}
		};
			return listener;
	}
	
	
	private void setDeductibleCalcualtionVaules(TextField deductibleTxt) {
		TextField property = deductibleTxt;
		Object itemId = property.getData();
		Item item = pmTreeTable.getItem(itemId);
		TextField netAmountField = (TextField) item.getItemProperty("Net Amount").getValue();
		TextField amountConsideredField = (TextField) item.getItemProperty("Amount Considered").getValue();
		AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("Details").getValue();
		Object benefitId = detailField.getData();
		AmountRequestedField amountRequestedField = (AmountRequestedField) item.getItemProperty("Amount Requested").getValue();
		Integer amountRequest = amountRequestedField.getAmount();
		PreauthRule rule = new PreauthRule();
		String deductibleAmt = property.getValue();
		
		netAmountField.setReadOnly(false);
		amountConsideredField.setReadOnly(false);
		
		if(deductibleAmt == null || deductibleAmt == "0" || deductibleAmt == "0.0" || deductibleAmt.length() == 0) {
			deductibleAmt = "0";
		}
		
		if((int)benefitId != 8 && (int)benefitId != 9 && amountRequest < SHAUtils.getFloatFromString(deductibleAmt)) {
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Deductible Amount should be lesser than Amount Requested. </b>", ContentMode.HTML));
			layout.setMargin(true);
			layout.setSpacing(true);
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(true);
			dialog.setResizable(false);
			dialog.setContent(layout);
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.show(getUI().getCurrent(), null, true);
			property.setReadOnly(false);
			property.setValue("0");
			deductibleAmt = "0";
			return;
		}
		
		updateSummation();
		
		if(!Arrays.asList(roomRentICUId).contains(itemId)) {
				if((int)itemId == 12 || (int)itemId == 13) {
					String value = property.getValue();
					if( value != null &&value.length() > 0 && !(value.equalsIgnoreCase("0.0") || value.equalsIgnoreCase("0")) ) {
						enableOrdisableItem(false, itemId, true);
					}  else if(value != null && value.length() <= 0) {
						Item previousItem = (int)itemId == 13 ? pmTreeTable.getItem(12) : pmTreeTable.getItem(13);
						AmountRequestedField previousAmountRequestedField = (AmountRequestedField) previousItem.getItemProperty("Amount Requested").getValue();
						if(previousAmountRequestedField.getAmount() <= 0) {
							enableOrdisableItem(true, null, true);
						}
					}
				}
				String otherDetailsNetAmountCalculation = null;
				if((int)itemId == 14) {
					otherDetailsNetAmountCalculation = rule.otherPackageNetAmount(amountRequest.toString(),deductibleAmt);
				} else {
					otherDetailsNetAmountCalculation = rule.otherDetailsNetAmountCalculation(amountRequest.toString(),deductibleAmt);
				}
				netAmountField.setReadOnly(false);
				netAmountField.setValue(otherDetailsNetAmountCalculation);
				netAmountField.setReadOnly(true);
				
				String otherDetailsAmountConsideredCalculation = otherDetailsNetAmountCalculation;
				
				if((int)itemId == 13) {
					if(detailField.getCheckboxValue()) {
						otherDetailsAmountConsideredCalculation = rule.compositePackageWithOverrideDeductionConsiderAmt(otherDetailsNetAmountCalculation);
					} else {
						otherDetailsAmountConsideredCalculation = rule.compositePackageWithoutOverrideDeductionConsiderAmt(otherDetailsNetAmountCalculation);
					}
				}else if((int)itemId == 14) {
					if(detailField.getCheckboxValue()) {
						otherDetailsAmountConsideredCalculation = rule.otherPackageWithRestrict(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
					} else {
						otherDetailsAmountConsideredCalculation = rule.otherPackageWithOutRestrict(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
					}
				}
				else if(!Arrays.asList(amountConsiderCalculationIds).contains(itemId)) {
					otherDetailsAmountConsideredCalculation = rule.otherDetailsAmountConsideredCalculation(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
				} 
				amountConsideredField.setReadOnly(false);
				amountConsideredField.setValue(otherDetailsAmountConsideredCalculation);
				amountConsideredField.setReadOnly(true);
			}
		netAmountField.setReadOnly(true);
		amountConsideredField.setReadOnly(true);
	}
	
	@SuppressWarnings("unused")
	public void enableOrdisableItem(Boolean isEnabled, Object itemId, Boolean clearValueFlag) {
		isANHOrCompositeSelected = !isEnabled;
		Collection<?> itemIds = pmTreeTable.getItemIds();
		for (Object object : itemIds) {
			Item item = pmTreeTable.getItem(object); 
			if(itemId != null && (int)object == (int)itemId) {
				continue;
			}
			Iterator<?> iterator = item.getItemPropertyIds().iterator();
			while(iterator.hasNext()) {
				Object propertyField = item.getItemProperty(iterator.next()).getValue();
				if(propertyField != null) {
					if(propertyField instanceof AmountRequestedField) {
						AmountRequestedField fullNameField = (AmountRequestedField) propertyField;
						if(clearValueFlag) {
							fullNameField.clearValues();
						}
						fullNameField.enableOrDisableValue(isEnabled, object);
					}  else if(propertyField instanceof AmountRequestedDetailField) {
						AmountRequestedDetailField detailField = (AmountRequestedDetailField) propertyField;
						if(clearValueFlag) {
							detailField.clearValues();
						}
						detailField.setEnabled(isEnabled);
					}
					else {
						Component  field = (Component) propertyField;
						if(!(field instanceof Label)){
							AbstractField<?>  abstractField = (AbstractField<?>) field;
							Boolean isReadOnly = false;
							if(abstractField.isReadOnly()) {
								abstractField.setReadOnly(false);
								isReadOnly = true;
							}
							if(clearValueFlag) {
								abstractField.setValue(null);
							}
							abstractField.setReadOnly(isReadOnly);
						}
						field.setEnabled(isEnabled);
					}
				}
			}
		}
	}
	
	public List<NoOfDaysCell> getValues() {
		return null;
//		Collection<?> itemIds = pmTreeTable.getItemIds();
//		List<NoOfDaysCell> listOfDTO = new ArrayList<NoOfDaysCell>();
//		for (Object itemId : itemIds) {
//			if(!Arrays.asList(ignoreItemId).contains(itemId)) {
//				Item item = pmTreeTable.getItem(itemId); 
//				NoOfDaysCell values = new NoOfDaysCell();
//				Iterator<?> iterator = item.getItemPropertyIds().iterator();
//				while(iterator.hasNext()) {
//					String propertyId = (String) iterator.next();
//					Object propertyField = item.getItemProperty(propertyId).getValue();
//					Component  field = (Component) propertyField;
//					if(propertyField != null && field.isEnabled()) {
//						if(propertyField instanceof AmountRequestedDetailField) {
//							AmountRequestedDetailField detailField = (AmountRequestedDetailField) propertyField;
//							Long benefitId = ((Integer) detailField.getData()).longValue();
//							if(benefitId == 17) {
//								values.setOverridePackageDeductionFlag(detailField.getCheckboxValue() ? "Y" : "N");
//							} else if(benefitId == 18) {
//								values.setRestrictToFlag(detailField.getCheckboxValue() ? "Y" : "N");
//							}
//							values.setBenefitId(benefitId);
//						} else if(propertyField instanceof AmountRequestedField) {
//							AmountRequestedField fullNameField = (AmountRequestedField) propertyField;
//							values.setNoOfDays(fullNameField.getNoOfDays());
//							values.setPerDayAmount(fullNameField.getPerDayAmt());
//							values.setAmountRequest(fullNameField.getAmount());
//						}
//						else {
//							String value;
//							if(Arrays.asList(calculatedColumns).contains(propertyId)) {
//								if(field instanceof Label) {
//									Label label = (Label) propertyField;
//									value = label.getValue();
//								} else {
//									AbstractField<?>  abstractField = (AbstractField<?>) propertyField;
//									value = (String) abstractField.getValue();
//								}
//								if(propertyId.startsWith("Deductibles")) {
//									values.setDeductible((value != null && value.length() > 0) ?  SHAUtils.getFloatFromString(value) : null);
//								} else if (propertyId.startsWith("Net Amount")) {
//									values.setNetAmount((value != null && value.length() > 0) ?  SHAUtils.getFloatFromString(value) : null);
//								} else if(propertyId.startsWith("Amount Considered")) {
//									values.setAmountConsidered((value != null && value.length() > 0) ?  SHAUtils.getFloatFromString(value) : null);
//								}
//							}
//							
//						}
//					}
//				}
//				listOfDTO.add(values);
//			}
//		
//		}
//		return listOfDTO;
//	}
//	
//	public NoOfDaysCell getExactDTO(Long benefitId, List<NoOfDaysCell> listOfDTO, Boolean isReProData) {
//		NoOfDaysCell cell = new NoOfDaysCell();
//		List<NoOfDaysCell> claimedDetailsList = this.preauthDto.getPreauthDataExtractionDetails().getClaimedDetailsList();
//		if(isReProData) {
//			claimedDetailsList = listOfDTO;
//		}
//		for (NoOfDaysCell noOfDaysCell : claimedDetailsList) {
//			if(noOfDaysCell.getBenefitId() == benefitId) {
//				cell = noOfDaysCell;
//				break;
//			}
//		}
//		return cell;
	}
	
	public void setValues(List<NoOfDaysCell> listofDTO, Boolean isReProData) {
//		Collection<?> itemIds = pmTreeTable.getItemIds();
//		for (Object itemId : itemIds) {
//			if(!Arrays.asList(ignoreItemId).contains(itemId)) {
//				Item item = pmTreeTable.getItem(itemId);
//				NoOfDaysCell values = new NoOfDaysCell();
//				Iterator<?> iterator = item.getItemPropertyIds().iterator();
//				NoOfDaysCell exactDTO = new NoOfDaysCell();
//				while(iterator.hasNext()) {
//					String propertyId = (String) iterator.next();
//					Object propertyField = item.getItemProperty(propertyId).getValue();
//					Component  field = (Component) propertyField;
//					if(propertyField != null && field.isEnabled()) {
//						if(propertyField instanceof AmountRequestedDetailField) {
//							AmountRequestedDetailField detailField = (AmountRequestedDetailField) propertyField;
//							Long benefitId = ((Integer) detailField.getData()).longValue();
//							exactDTO = getExactDTO(benefitId, listofDTO, isReProData);
//							if(benefitId == 17) {
//								detailField.getCheckboxField().setValue(exactDTO.getOverridePackageDeductionFlag() == "Y" ? true :false);
//							} else if(benefitId == 18) {
//								detailField.getCheckboxField().setValue(exactDTO.getRestrictToFlag() == "Y" ? true :false);
//							}
//						} else if(propertyField instanceof AmountRequestedField) {
//							AmountRequestedField amountRequestedField = (AmountRequestedField) propertyField;
//							amountRequestedField.setNoOfDaysTxt(exactDTO.getNoOfDays() != null ? exactDTO.getNoOfDays().toString() : null);
//							amountRequestedField.setPerDayAmt(exactDTO.getPerDayAmount() != null ? exactDTO.getPerDayAmount().toString() :null);
//							amountRequestedField.setAmtTxt(exactDTO.getAmountRequest() != null ? exactDTO.getAmountRequest().toString() : null);
//							if(!(isReProData && (int)itemId == 3)) {
//								setAmtRequestedCalculationValues(amountRequestedField.getListenerField());
//							}
//							
//						} else {
//							if(Arrays.asList(calculatedColumns).contains(propertyId)) {
//								AbstractField<?>  abstractField = (AbstractField<?>) propertyField;
//								if(propertyId.startsWith("Deductibles")) {
//									TextField deductibleTxt = (TextField) propertyField;
//									Boolean isReadOnly = false;
//									if(deductibleTxt.isReadOnly()) {
//										deductibleTxt.setReadOnly(false);
//										isReadOnly = true;
//									}
//									deductibleTxt.setValue(exactDTO.getDeductible() != null ? exactDTO.getDeductible().toString() : null);
//									if(isReProData) {
//										setDeductibleCalcualtionVaules(deductibleTxt);
//									}
//									
//									deductibleTxt.setReadOnly(isReadOnly);
//								} 
//							}
//							
//						}
//					}
//				}
//			}
//		}
//		pmTreeTable.setFooterVisible(true);
	}
	

	private ValueChangeListener getConsiderAmtListener(final String itemPropertyId) {
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField value = (TextField) event.getProperty();
				setTotal(value.getValue(), itemPropertyId);
			}
		};
		return listener;
	}
	
	
	private ValueChangeListener getNetAmountListener(final String itemPropertyId) {
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField value = (TextField) event.getProperty();
				setTotal(value.getValue(), itemPropertyId);
			}
		};
		return listener;
	}
	
	/*public void getTotal() {
		
	}*/
	
	public Boolean isValid() {
		//List<String> errorMessages = new ArrayList<String>();
		Collection<?> itemIds = pmTreeTable.getItemIds();
		List<Integer> amount = new ArrayList<Integer>();
		//Boolean hasError = false;
		for (Object itemId : itemIds) {
			if(!Arrays.asList(ignoreItemId).contains(itemId)) {
				Item item = pmTreeTable.getItem(itemId);
				
				Object field = item.getItemProperty("Details").getValue();
				
				AmountRequestedDetailField detailField = (AmountRequestedDetailField) field;
				
				Object amountRequestedField = item.getItemProperty("Amount Requested").getValue();
				AmountRequestedField requestedField = (AmountRequestedField) amountRequestedField;
				
				if(detailField != null && requestedField != null) {
					Integer benefitId = (Integer) detailField.getData();
					if(Arrays.asList(mandatoryBenefitId).contains(benefitId)) {
						if(requestedField.getAmount() > 0) {
							amount.add(requestedField.getAmount());
						}
					}
				}
			}
		}
		
		return !amount.isEmpty();
	}
	
	
}
