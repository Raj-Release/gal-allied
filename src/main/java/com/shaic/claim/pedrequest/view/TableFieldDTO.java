package com.shaic.claim.pedrequest.view;

import java.io.Serializable;

import com.shaic.arch.table.TableCellSelectionHandler;

@SuppressWarnings("rawtypes")
public class TableFieldDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String propertyId;

	
	private Class fieldClass;

	private Class typeClass;
	
	private Boolean isEditable;
	
	private TableCellSelectionHandler valueChangeListener;
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable,TableCellSelectionHandler valueChangeListener)
	{
		this(propertyId, fieldClass, typClass, isEditable);
		this.valueChangeListener = valueChangeListener;
	}
	
	public TableFieldDTO(String propertyId, Class fieldClass, Class typClass, Boolean isEditable)
	{
		this.propertyId = propertyId;
		this.fieldClass = fieldClass;
		this.typeClass = typClass;
		this.isEditable = isEditable;
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

	public void setTypeClass(Class typeClass) {
		this.typeClass = typeClass;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public TableCellSelectionHandler getValueChangeListener() {
		return valueChangeListener;
	}

	public void setValueChangeListener(TableCellSelectionHandler valueChangeListener) {
		this.valueChangeListener = valueChangeListener;
	}

}
