package com.shaic.arch.fields;

import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.FullNameDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class AmountRequestedField extends CustomField<FullNameDTO> {


	private static final long serialVersionUID = -7848574563339946803L;

	private FieldGroup fieldGroup;

	private HorizontalLayout layout;
	private TextField noOfDaysTxt;
	private TextField perDayAmtTxt;
	private TextField amtTxt;

	Boolean isNoOfDaysField = true;

	public AmountRequestedField(Boolean isNoOfDaysField) {
		this.isNoOfDaysField = isNoOfDaysField;
		amtTxt = new TextField();
		amtTxt.setWidth("70px");
		amtTxt.setNullRepresentation("0");
		
		/*Validation added for amtTxt validator feild*/
		CSValidator amtTxtVal = new CSValidator();
		amtTxtVal.extend(amtTxt);
		amtTxtVal.setRegExp("^[0-9]*$");
		amtTxtVal.setPreventInvalidTyping(true);
		
		noOfDaysTxt = new TextField();
//		noOfDaysTxt.setMaxLength(3);
		/*Validation added for no of days validator feild*/
		CSValidator noOfDaysValidator = new CSValidator();
		
		noOfDaysValidator.extend(noOfDaysTxt);
		noOfDaysValidator.setRegExp("^[0-9]*$");
		noOfDaysValidator.setPreventInvalidTyping(true);
		noOfDaysTxt.setWidth("40px");
		noOfDaysTxt.setNullRepresentation("0");
		
		CSValidator perDayAmtValidator = new CSValidator();
		perDayAmtTxt = new TextField();
		noOfDaysTxt.setMaxLength(3);
		perDayAmtValidator.extend(perDayAmtTxt);
		perDayAmtValidator.setRegExp("^[0-9]*$");
		perDayAmtValidator.setPreventInvalidTyping(true);
		perDayAmtTxt.setWidth("60px");
		perDayAmtTxt.setNullRepresentation("0");
	}

	@Override
	protected Component initContent() {
		ValueChangeListener listener = getListener();
		amtTxt.setInputPrompt("Amount");
		if(isNoOfDaysField) {
			noOfDaysTxt.setInputPrompt("No Of Days");
			perDayAmtTxt.setInputPrompt("Per Day Amt");
			noOfDaysTxt.addValueChangeListener(listener);
			perDayAmtTxt.addValueChangeListener(listener);
			Label dummyLab = new Label();
			dummyLab.setWidth("5px");
			layout = new HorizontalLayout(dummyLab, noOfDaysTxt, perDayAmtTxt, amtTxt);
//			layout.setWidth("170px");
		} else {
			
			Label lab1 = new Label();
			lab1.setWidth("40px");
			Label lab2 = new Label();
			lab2.setWidth("60px");
			layout = new HorizontalLayout(lab1, lab2, amtTxt);
//			layout.setComponentAlignment(noOfDaysTxt, perDayAmtTxt, Alignment.MIDDLE_RIGHT);
		}
		
		
		return layout;
	}

	private ValueChangeListener getListener() {
		
		
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = -6953428339734363090L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				amtTxt.setReadOnly(false);
				if(noOfDaysTxt.getValue() != null && noOfDaysTxt.getValue().toString().length() > 0 && perDayAmtTxt.getValue() != null && perDayAmtTxt.getValue().toString().length() > 0) {
					if(SHAUtils.getFloatFromString(noOfDaysTxt.getValue()) > 0 && SHAUtils.getFloatFromString(perDayAmtTxt.getValue()) > 0) {
						Integer value = (SHAUtils.getFloatFromString(noOfDaysTxt.getValue()) * SHAUtils.getFloatFromString(perDayAmtTxt.getValue()));
						amtTxt.setValue(value.toString());
						noOfDaysTxt.setValue(noOfDaysTxt.getValue());
						perDayAmtTxt.setValue(perDayAmtTxt.getValue());
						
					} else {
						amtTxt.setValue(null);
						if(SHAUtils.getFloatFromString(noOfDaysTxt.getValue()) == 0 && SHAUtils.getFloatFromString(perDayAmtTxt.getValue()) != 0) {
							VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Please Enter No of Days Field </b>", ContentMode.HTML));
							layout.setMargin(true);
							layout.setSpacing(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setClosable(true);
							dialog.setResizable(false);
							dialog.setContent(layout);
							dialog.setCaption("Error");
							dialog.setClosable(true);
							perDayAmtTxt.setValue(null);
							dialog.show(getUI().getCurrent(), null, true);
							return;
						}
					}
				} else {
					amtTxt.setValue(null);
					if(SHAUtils.getFloatFromString(noOfDaysTxt.getValue()) == 0 && SHAUtils.getFloatFromString(perDayAmtTxt.getValue()) != 0) {
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Please Enter No of Days Field </b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setClosable(true);
						dialog.setResizable(false);
						dialog.setContent(layout);
						dialog.setCaption("Error");
						dialog.setClosable(true);
						perDayAmtTxt.setValue(null);
						dialog.show(getUI().getCurrent(), null, true);
						return;
					}
				}
			}
		};
		return listener;
	}

//	private void initBinder() {
//		fieldGroup.bind(noOfDaysTxt, "firstName");
//		fieldGroup.bind(perDayAmtTxt, "lastName");
//		fieldGroup.bind(amtTxt, "middleName");
//	}

	public void setNoOfDaysTxt(String value) {
		this.noOfDaysTxt.setValue(value);
	}
	
	public void setPerDayAmt(String value) {
		this.perDayAmtTxt.setValue(value);
	}
	
	public void setAmtTxt(String value) {
		this.amtTxt.setValue(value);
	}


	@Override
	public void setValue(FullNameDTO newFieldValue)
			throws Property.ReadOnlyException,
			com.vaadin.v7.data.util.converter.Converter.ConversionException {
		this.noOfDaysTxt.setValue(newFieldValue.getFirstName());
		this.amtTxt.setValue(newFieldValue.getMiddleName());
		this.perDayAmtTxt.setValue(newFieldValue.getLastName());
	};
	
	@Override
	public Class<? extends FullNameDTO> getType() {
		return FullNameDTO.class;
	}
	
	public TextField getListenerField() {
		return this.amtTxt;
	}

	@Override
	public void focus() {
		noOfDaysTxt.focus();
	}

	public void clearValues() {
		this.noOfDaysTxt.setValue("0");
		this.amtTxt.setValue("0");
		this.perDayAmtTxt.setValue("0");
	}
	
	public void enableOrDisableValue(Boolean isEnabled, Object itemId) {
		this.noOfDaysTxt.setEnabled(isEnabled);
		this.amtTxt.setEnabled(isEnabled);
		if((int)itemId == 2 || (int)itemId == 3) {
			this.amtTxt.setEnabled(false);
		} 
		this.perDayAmtTxt.setEnabled(isEnabled);
	}
	
	@Override
    protected void setInternalValue(FullNameDTO fullName) {
        super.setInternalValue(fullName);
        fieldGroup.setItemDataSource(new BeanItem<FullNameDTO>(fullName));
    }

	public void setValues() throws CommitException
	{
		this.fieldGroup.commit();
	}
	
	public String validateInput(Boolean isRequired) 
	{ 
		StringBuffer eMsg = new StringBuffer();
		
		if (isRequired && !this.fieldGroup.isValid()) {
			this.noOfDaysTxt.setValidationVisible(isRequired);
		    for (Field<?> field : this.fieldGroup.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		eMsg.append(errMsg.getFormattedHtmlMessage());
		    	}
			}
		}
		return eMsg.toString();
	}
	
	private Integer getValue(String value) {
		if(value != null && value.length() > 0) {
			return SHAUtils.getFloatFromString(value);
		}
		return SHAUtils.getFloatFromString("0");
	}
	
	public Integer getNoOfDays() {
		return SHAUtils.getFloatFromString(this.noOfDaysTxt.getValue());
	}
	
	public Integer getPerDayAmt() {
		return SHAUtils.getFloatFromString(this.perDayAmtTxt.getValue());
	}
	
	public Integer getAmount() {
		return SHAUtils.getFloatFromString(this.amtTxt.getValue());
	}
	
	public TextField getNoOfDaysField() {
		return this.noOfDaysTxt;
	}
	
	public TextField getPerDayAmtField() {
		return this.perDayAmtTxt;
	}
	
	public Float updateTotal()
	{
		float total = 0.0f;
		String noOfDaysValueStr = this.noOfDaysTxt.getValue();
		String perDayAmountStr = this.perDayAmtTxt.getValue();
		float perDayValue = 0.0f;
		float noofDays = 0.0f;
		
		boolean hasDays = false;
		if (SHAUtils.isValidFloat(noOfDaysValueStr))
		{
			noofDays += SHAUtils.getFloatFromString(noOfDaysValueStr);
			hasDays = true;
		}
		if (SHAUtils.isValidFloat(perDayAmountStr))
		{
			perDayValue += noofDays += SHAUtils.getFloatFromString(perDayAmountStr);
			hasDays = true;
		}
		if (hasDays)
		{
			total = perDayValue * noofDays;
		}
		this.amtTxt.setValue("" + total);
		return total;
	}

	public void disableAmountRequested() {
		this.amtTxt.setEnabled(false);
	}
}
