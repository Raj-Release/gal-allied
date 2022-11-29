package com.shaic.claim.preauth.dto;

import com.shaic.arch.fields.dto.FullNameDTO;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;

public class AmountConsideredField extends CustomField<FullNameDTO> {



	private static final long serialVersionUID = -7848574563339946803L;

	private FieldGroup fieldGroup;

	private FormLayout layout;
	private ComboBox detailText;
	private Label detailLabel;

	private FullNameDTO fullName;
	
	Boolean isTextField = true;
	
	Boolean isTwoField = false;

	public AmountConsideredField(Boolean isBaseLabel, String detailName, String Label) {
		this.isTextField = isBaseLabel;
		
		detailText = new ComboBox();
		detailText.setWidth("100px");
		
		detailLabel = new Label(detailName);
		
	}
	
	public AmountConsideredField(Boolean isTwoField, String detailName) {
		this.isTwoField = isTwoField;
		
		detailText = new ComboBox();
		detailText.setWidth("70px");
		
		detailLabel = new Label(detailName);
	}

	@Override
	protected Component initContent() {
		if(isTwoField) {
			HorizontalLayout textHorizontal=new HorizontalLayout(detailLabel,detailText);
			detailLabel.setWidth("200px");
			detailText.setWidth("70px");
			
			textHorizontal.setSpacing(true);
			layout = new FormLayout(textHorizontal);
			detailText.setWidth("100px");
			layout.setMargin(false);
		} else {
			layout = new FormLayout(detailLabel);
			layout.setSpacing(true);
			layout.setWidth("100%");
			layout.setMargin(false);
		}
		return layout;
	}
	
	public ComboBox getPercentageTextfield(){
		return detailText;
	}
	
	public String getLabel(){
		return detailLabel != null ? detailLabel.getValue() : null;
	}
	
//	public Boolean getCheckboxValue() {
//		return this.detailLabel.getValue();
//	}
//	
//	public CheckBox getCheckboxField() {
//		return this.detailLabel;
//	}

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
	
//	public void clearValues() {
//		this.detailLabel.setValue(false);
//	}
	


}
