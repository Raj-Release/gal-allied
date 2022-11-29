package com.shaic.arch.fields;

import com.shaic.arch.fields.dto.FullNameDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;

public class FullNameField extends CustomField<FullNameDTO> {

	private static final long serialVersionUID = -4474379751753410212L;
	
	private FieldGroup fieldGroup;

	private HorizontalLayout layout;
	private TextField firstNameTxt;
	private TextField lastNameTxt;
	private TextField middleNameTxt;

	private FullNameDTO fullName;

	public FullNameField() {
		firstNameTxt = new TextField("");
		firstNameTxt.setNullRepresentation("");
		middleNameTxt = new TextField();
		middleNameTxt.setNullRepresentation("");
		lastNameTxt = new TextField("");
		lastNameTxt.setNullRepresentation("");
		fieldGroup = new BeanFieldGroup<FullNameDTO>(FullNameDTO.class);
	}

	@Override
	protected Component initContent() {
//		firstNameTxt.setRequired(true);
//		firstNameTxt.setRequiredError("First Name can't be left blank");
//		firstNameTxt.setValidationVisible(false);
		
		firstNameTxt.setCaption("First Name");
		firstNameTxt.setInputPrompt("First Name");
		
		
		middleNameTxt.setCaption("Middle Name");
		middleNameTxt.setInputPrompt("Middle Name");

		lastNameTxt.setCaption("Last Name");
		lastNameTxt.setInputPrompt("Last Name");

		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 7342288705079773186L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				FullNameDTO newValue = new FullNameDTO(firstNameTxt.getValue(),
						middleNameTxt.getValue(), lastNameTxt.getValue());
				
				setValue(newValue);
			}
		};
		firstNameTxt.addValueChangeListener(listener);
		middleNameTxt.addValueChangeListener(listener);
		lastNameTxt.addValueChangeListener(listener);

		layout = new HorizontalLayout(firstNameTxt, middleNameTxt, lastNameTxt);
		// setUpValue();
		initBinder();
		return layout;
	}

	private void initBinder() {
		fieldGroup.bind(firstNameTxt, "firstName");
		fieldGroup.bind(lastNameTxt, "lastName");
		fieldGroup.bind(middleNameTxt, "middleName");
	}

	public void setUpValue() {
		this.firstNameTxt.setValue(this.fullName.getFirstName());
		this.lastNameTxt.setValue(this.fullName.getLastName());
		this.middleNameTxt.setValue(this.fullName.getMiddleName());
	}

	@Override
	public void setValue(FullNameDTO newFieldValue)
			throws Property.ReadOnlyException,
			com.vaadin.v7.data.util.converter.Converter.ConversionException {
		this.firstNameTxt.setValue(newFieldValue.getFirstName());
		this.middleNameTxt.setValue(newFieldValue.getMiddleName());
		this.lastNameTxt.setValue(newFieldValue.getLastName());
	};
	
	@Override
	public FullNameDTO getValue()
	{
		FullNameDTO fullName = new FullNameDTO();
		fullName.setFirstName(this.firstNameTxt.getValue());
		fullName.setMiddleName(this.middleNameTxt.getValue());
		fullName.setLastName(this.lastNameTxt.getValue());
		return fullName;
	}

	@Override
	public Class<? extends FullNameDTO> getType() {
		return FullNameDTO.class;
	}

	@Override
	public void focus() {
		firstNameTxt.focus();
	}

	public void clearValues() {
		this.firstNameTxt.setValue("");
		this.middleNameTxt.setValue("");
		this.lastNameTxt.setValue("");
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
			this.firstNameTxt.setValidationVisible(isRequired);
		    for (Field<?> field : this.fieldGroup.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		eMsg.append(errMsg.getFormattedHtmlMessage());
		    	}
			}
		}
		return eMsg.toString();
	}
}
