package com.shaic.arch.table;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ReferenceTable;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.ui.Field;

@SuppressWarnings({ "serial", "rawtypes" })
public class TableFieldDTO implements Serializable {

	private String propertyId;
	
	private int length;

	private Class fieldClass;

	private Class typeClass;
	
	private Boolean isEditable;
	
	private Boolean isEnabled;
	
	private Boolean isCSValidator;
	
	private int size;
	
	private TableCellSelectionHandler valueChangeListener;
	
	private SelectValue selectedValue;
	private BlurListener blurListner;
	
	private Field field;

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public TableCellSelectionHandler getValueChangeListener() {
		return valueChangeListener;
	}

	public void setValueChangeListener(TableCellSelectionHandler valueChangeListener) {
		this.valueChangeListener = valueChangeListener;
	}

	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable,TableCellSelectionHandler valueChangeListener)
	{
		this(propertyId, fieldClass, typClass, isEditable);
		this.valueChangeListener = valueChangeListener;
	}
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable,BlurListener blurListener)
	{
		this(propertyId, fieldClass, typClass, isEditable);
		this.blurListner = blurListener;
	}	
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable)
	{
		field = null;
		this.propertyId = propertyId;
		this.fieldClass = fieldClass;
		this.typeClass = typClass;
		this.isEditable = isEditable;
	}
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable, int length)
	{
		field = null;
		this.propertyId = propertyId;
		this.fieldClass = fieldClass;
		this.typeClass = typClass;
		this.isEditable = isEditable;
		this.length = length;
	}
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable, Boolean isEnabled)
	{
		field = null;
		this.propertyId = propertyId;
		this.fieldClass = fieldClass;
		this.typeClass = typClass;
		this.isEditable = isEditable;
		this.isEnabled = isEnabled;
	}
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable, Boolean isEnabled,int size,TableCellSelectionHandler valueChangeListener)
	{
		field = null;
		this.propertyId = propertyId;
		this.fieldClass = fieldClass;
		this.typeClass = typClass;
		this.isEditable = isEditable;
		this.isEnabled = isEnabled;
		this.size = size;
	}
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable, Boolean isEnabled,int size)
	{
		field = null;
		this.propertyId = propertyId;
		this.fieldClass = fieldClass;
		this.typeClass = typClass;
		this.isEditable = isEditable;
		this.isEnabled = isEnabled;
		this.size = size;
	}
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable, Boolean isEnabled,int size, int length)
	{
		field = null;
		this.propertyId = propertyId;
		this.fieldClass = fieldClass;
		this.typeClass = typClass;
		this.isEditable = isEditable;
		this.isEnabled = isEnabled;
		this.size = size;
		this.length = length;
	}
	
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable, int length, Boolean csValidator)
	{
		field = null;
		this.propertyId = propertyId;
		this.fieldClass = fieldClass;
		this.typeClass = typClass;
		this.isEditable = isEditable;
		this.length = length;
		this.isCSValidator = csValidator;
		
	}
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable, int length,SelectValue selectedValue, Boolean isSelected)
	{
		field = null;
		this.propertyId = propertyId;
		this.fieldClass = fieldClass;
		this.typeClass = typClass;
		this.isEditable = isEditable;
		this.length = length;
//		this.isCSValidator = csValidator;
		SelectValue selectValue = new SelectValue();
		selectValue.setId(ReferenceTable.COMMONMASTER_NO);
		selectValue.setValue("No");
		selectedValue = selectValue;
		this.selectedValue = selectedValue;
		
	}
	
	
	
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public Boolean getIsCSValidator() {
		return isCSValidator;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public void setIsCSValidator(Boolean isCSValidator) {
		this.isCSValidator = isCSValidator;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public Class getFieldClass() {
		return fieldClass;
	}

	public void setFieldClass(Class fieldClass) {
		this.fieldClass = fieldClass;
	}

	public Class getTypeClass() {
		return typeClass;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public void setTypeClass(Class typeClass) {
		this.typeClass = typeClass;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public SelectValue getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(SelectValue selectedValue) {
		this.selectedValue = selectedValue;
	}
	public BlurListener getBlurListner() {
		return blurListner;
	}

	public void setBlurListner(BlurListener blurListner) {
		this.blurListner = blurListner;
	}
}
