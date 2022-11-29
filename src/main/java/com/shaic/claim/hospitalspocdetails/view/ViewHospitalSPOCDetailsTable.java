package com.shaic.claim.hospitalspocdetails.view;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.ViewPccRemarksDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class ViewHospitalSPOCDetailsTable extends GBaseTable<ViewHospSPOCDetailsDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {"hospitalSPOC","name","mobileNumber","landLineNumber","emailId"};


	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewHospSPOCDetailsDTO>(
				ViewHospSPOCDetailsDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setWidth("100%");
		table.setHeight("100px");
	}

	@Override
	public String textBundlePrefixString() {
		return "view-hospspocdetails-";
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableSelectHandler(ViewHospSPOCDetailsDTO t) {
		// TODO Auto-generated method stub
		
	}
	
}
