package com.shaic.claim.selecthospital.ui;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.domain.HospitalService;
import com.shaic.newcode.wizard.pages.IntimationDetailsPage;
import com.vaadin.v7.data.util.BeanItemContainer;

public class HospitalSearchTable extends GBaseTable<HospitalDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private IntimationDetailsPage intimationDetailsPage;
	
	@Inject
	private HospitalService hospitalService;
	
	@Inject
	private HospitalMapper hospitalMapper;
	

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"hospitalCode", "name", "city", "state" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<HospitalDto>(
				HospitalDto.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"hospitalCode", "name", "city", "state" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(HospitalDto t) {
//		intimationDetailsPage.searchHospitalNameKey(t);
		intimationDetailsPage.setHospitalDetails(t);
	}

	@Override
	public String textBundlePrefixString() {
		return "search-hospital-";
	}
	
	public void resetTable(){
		table.removeAllItems();
	}

}
