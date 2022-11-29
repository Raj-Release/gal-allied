package com.shaic.arch.fields;

import com.shaic.arch.fields.dto.FullNameDTO;
import com.vaadin.v7.data.Property;

@SuppressWarnings("serial")
public class FullNameProperty implements Property<FullNameDTO>,Property.ValueChangeNotifier {

	private FullNameDTO data;
	
	private boolean readOnlyFlag = false;
	@Override
	public FullNameDTO getValue() {
		return this.data;
	}

	@Override
	public void setValue(FullNameDTO newValue)
			throws com.vaadin.v7.data.Property.ReadOnlyException {
		this.data = newValue;
	}

	@Override
	public Class<? extends FullNameDTO> getType() {
		return FullNameDTO.class;
	}

	@Override
	public boolean isReadOnly() {
		return this.readOnlyFlag;
	}

	@Override
	public void setReadOnly(boolean newStatus) {
		this.readOnlyFlag = newStatus;
	}

	@Override
	public void addValueChangeListener(
			com.vaadin.v7.data.Property.ValueChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(
			com.vaadin.v7.data.Property.ValueChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeValueChangeListener(
			com.vaadin.v7.data.Property.ValueChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(
			com.vaadin.v7.data.Property.ValueChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

}
