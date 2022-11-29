/*
* Copyright 2011 Nicolas Frankel
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*/
package com.shaic.arch;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.components.GTextField;
import com.shaic.arch.fields.FullNameField;
import com.shaic.arch.fields.dto.FullNameDTO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.domain.Insured;
import com.shaic.domain.TmpInvestigation;
import com.vaadin.v7.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.v7.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.RichTextArea;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;

@SuppressWarnings("serial")
public class EnhancedFieldGroupFieldFactory implements FieldGroupFieldFactory {

	private FieldGroupFieldFactory fieldFactory = DefaultFieldGroupFieldFactory.get();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType) {
		T createdField = doCreateField(dataType, fieldType);
		if (createdField != null)
		{
			return createdField;
		}
		return fieldFactory.createField(dataType, fieldType);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T createTextField() {
		TextField field = new TextField();
		field.setNullRepresentation("");
		//Vaadin8-setImmediate() field.setImmediate(true);
		
		return (T) field;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T createGTextField() {
		GTextField field = new GTextField();
		field.setNullRepresentation("");
		//Vaadin8-setImmediate() field.setImmediate(true);
		
		return (T) field;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T createCheckBoxField() {
		CheckBox field = new CheckBox();
		//Vaadin8-setImmediate() field.setImmediate(true);
		
		return (T) field;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T createOptionGroup() {
		OptionGroup field = new OptionGroup();
		field.addItems(getRadioButtonOptions());
		field.setItemCaption(true, "Yes");
		field.setItemCaption(false, "No");
		field.setStyleName("horizontal");
		//Vaadin8-setImmediate() field.setImmediate(true);
		
		return (T) field;
	}
	
	protected static Collection<Boolean> getRadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	public static <T extends Field> T createTextArea() {
		TextArea field = new TextArea();
		field.setNullRepresentation("");
		return (T) field;
	}
	
	public static <T extends Field> T createRichTextArea() {
		RichTextArea field = new RichTextArea();
		field.setNullRepresentation("");
		return (T) field;
	}
	
	public static <T extends Field> T createLabel() {
		Label field = new Label();
		return (T) field;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T createComboBox() {

		GComboBox field = new GComboBox();
		field.setNewItemsAllowed(false);
		//Vaadin8-setImmediate() field.setImmediate(true);
//		field.setConverter(ComboBoxConverter.class);
		return (T) field;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T createNativeSelect() {

		NativeSelect field = new NativeSelect();
		field.setNewItemsAllowed(false);
		//Vaadin8-setImmediate() field.setImmediate(true);
		return (T) field;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	
	
	public static <T extends AbstractComponent> T  doCreateComponent(Class<?> dataType, Class<T> fieldType) {
		if(File.class.isAssignableFrom(dataType) && Upload.class.isAssignableFrom(fieldType))
		{
			return(T) createUploadField();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T doCreateField(Class<?> dataType, Class<T> fieldType) {

		if(Date.class.isAssignableFrom(dataType) && PopupDateField.class.isAssignableFrom(fieldType))
		{
			return (T) createPopupDateField();
		}
		//Added for upload option
		//String Value given below will be changed later.
		
		
		
		else if (Date.class.isAssignableFrom(dataType) && DateField.class.isAssignableFrom(fieldType))
		{
			return (T) createDateField();
		}
		else if (FullNameDTO.class.isAssignableFrom(dataType))
		{
			return (T) createFullNameField();
		}
		else if (fieldType.isAssignableFrom(OptionGroup.class))
		{
			return (T) createOptionGroup();
		}
		else if(SelectValue.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(ComboBox.class))
		{
			return (T) createComboBox();
		}
		else if(SelectValue.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(ComboBox.class))
		{
			return (T) createComboBox();
		}
		else if(Insured.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(ComboBox.class))
		{
			return (T) createComboBox();
		}
		else if(TmpInvestigation.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(ComboBox.class))
		{
			return (T) createComboBox();
		}
		else if(HospitalDto.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(ComboBox.class))
		{
			return (T) createComboBox();
		}
		else if(String.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(ComboBox.class))
		{
			return (T) createComboBox();
		}
		else if(String.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(TextField.class))
		{
			return (T) createTextField();
		} 
		else if(String.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(GTextField.class))
		{
			return (T) createGTextField();
		}
		else if(Long.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(GTextField.class))
		{
			return (T) createGTextField();
		}
		else if(String.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(TextArea.class))
		{
			return (T) createTextArea();
		}
		else if(String.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(RichTextArea.class))
		{
			return (T) createRichTextArea();
		}
		else if(String.class.isAssignableFrom(dataType) && fieldType.isAssignableFrom(Label.class))
		{
			return (T) createLabel();
		}
		else if (CheckBox.class.isAssignableFrom(fieldType))
		{
			return (T) createCheckBoxField();
		}
//		else if (GTokenField.class.isAssignableFrom(fieldType))
//		{
//			return (T) createTokenField();
//		}
		return null;
	}
	
	
	public void testMethod()
	{
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T createPopupDateField() {

		PopupDateField field = new PopupDateField();
		field.setDateFormat("dd/MM/yyyy");
		//Vaadin8-setImmediate() field.setImmediate(true);

		return (T) field;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends AbstractComponent> T createUploadField() {
		
		Upload field = new Upload();
		//Vaadin8-setImmediate() field.setImmediate(true);
		return (T) field;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T createDateField() {

		DateField field = new DateField();
		field.setDateFormat("dd/MM/yyyy");
		//Vaadin8-setImmediate() field.setImmediate(true);

		return (T) field;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <T extends Field> T createFullNameField(){
		FullNameField field = new FullNameField();
		//Vaadin8-setImmediate() field.setImmediate(true);
//		field.setConverter(FullnameConverter.class);
		return (T) field;
	}
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Field> T createUploadField() {
		
		Upload upload  = new Upload("");	
		
		DateField field = new DateField();
		field.setDateFormat("dd/MM/yyyy");
		//Vaadin8-setImmediate() field.setImmediate(true);

		return (T) field;
	}*/
}
