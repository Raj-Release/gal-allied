package com.shaic.claim.edithospitalInfo.search;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchEditHospitalDetailsTable extends
		GBaseTable<SearchEditHospitalDetailsTableDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*	public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"intimationNo", "dateOfIntimation", "policyNo" ,"hospitalType", 
			"status" };*/
	@Inject
	SearchEditHospitalDetailsTableDTO editHospitalInfoDTO;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<SearchEditHospitalDetailsTableDTO>(
				SearchEditHospitalDetailsTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"intimationNo", "dateOfIntimation", "policyNo" ,"hospitalType", 
			"status" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(SearchEditHospitalDetailsTableDTO t) {
		fireViewEvent(MenuItemBean.REVISED_EDIT_INTIMATION, t);
		//fireViewEvent(MenuItemBean.PROCESS_PREAUTH_REJECTION, t.getKey());
		//fireViewEvent(MenuPresenter.PROCESS_REJECTION , t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
		return "search-editHospitalDetails-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
