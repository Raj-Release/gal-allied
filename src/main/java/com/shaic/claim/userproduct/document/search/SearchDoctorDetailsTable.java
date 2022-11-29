package com.shaic.claim.userproduct.document.search;


import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;


public class SearchDoctorDetailsTable extends GBaseTable<SearchDoctorDetailsTableDTO> {
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","empId","doctorName","loginId"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchDoctorDetailsTableDTO>(SearchDoctorDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.setHeight("331px");
		table.setSizeFull();
	}

	@Override
	public void tableSelectHandler(
			SearchDoctorDetailsTableDTO t) {
			
			fireViewEvent(MenuPresenter.DOCTOR_USER_ACCESSIBILITY, t);

	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-doctor-details-";
	}



}
