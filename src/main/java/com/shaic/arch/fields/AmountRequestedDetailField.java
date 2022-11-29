package com.shaic.arch.fields;

import com.shaic.arch.fields.dto.FullNameDTO;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;

public class AmountRequestedDetailField extends CustomField<FullNameDTO> {


	private static final long serialVersionUID = -7848574563339946803L;

	private FieldGroup fieldGroup;

	private HorizontalLayout layout;
	private Label detailLabel;
	private CheckBox detailCheckbox;

	private FullNameDTO fullName;
	
	Boolean isBaseLabel = true;

	public AmountRequestedDetailField(Boolean isBaseLabel, String detailName, String checkboxLabel) {
		this.isBaseLabel = isBaseLabel;
		
		detailLabel = new Label(detailName, ContentMode.HTML);
		
		detailCheckbox = new CheckBox(checkboxLabel);
		
	}

	@Override
	protected Component initContent() {
		if(isBaseLabel) {
			layout = new HorizontalLayout(detailLabel);
		} else {
			layout = new HorizontalLayout(detailLabel, detailCheckbox);
			layout.setSpacing(true);
			layout.setWidth("100%");
		}
		return layout;
	}
	
	public Boolean getCheckboxValue() {
		return this.detailCheckbox.getValue();
	}
	
	public String getLabelValue() {
		return this.detailLabel.getValue();
	}
	
	public CheckBox getCheckboxField() {
		return this.detailCheckbox;
	}

//	private void initBinder() {
//		fieldGroup.bind(noOfDaysTxt, "firstName");
//		fieldGroup.bind(perDayAmtTxt, "lastName");
//		fieldGroup.bind(amtTxt, "middleName");
//	}

//	@Override
//	public void setValue(FullNameDTO newFieldValue)
//			throws Property.ReadOnlyException,
//			com.vaadin.v7.data.util.converter.Converter.ConversionException {
//		this.noOfDaysTxt.setValue(newFieldValue.getFirstName());
//		this.amtTxt.setValue(newFieldValue.getMiddleName());
//		this.perDayAmtTxt.setValue(newFieldValue.getLastName());
//	};
	
//	@Override
//	public FullNameDTO getValue()
//	{
//		FullNameDTO fullName = new FullNameDTO();
//		fullName.setFirstName(this.noOfDaysTxt.getValue());
//		fullName.setMiddleName(this.amtTxt.getValue());
//		fullName.setLastName(this.perDayAmtTxt.getValue());
//		return fullName;
//	}

	@Override
	public Class<? extends FullNameDTO> getType() {
		return FullNameDTO.class;
	}
	

//	public void clearValues() {
//		this.noOfDaysTxt.setValue("");
//		this.amtTxt.setValue("");
//		this.perDayAmtTxt.setValue("");
//	}
	
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
//			this.noOfDaysTxt.setValidationVisible(isRequired);
		    for (Field<?> field : this.fieldGroup.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		eMsg.append(errMsg.getFormattedHtmlMessage());
		    	}
			}
		}
		return eMsg.toString();
	}
	
	public void clearValues() {
		this.detailCheckbox.setValue(false);
	}
	
}
