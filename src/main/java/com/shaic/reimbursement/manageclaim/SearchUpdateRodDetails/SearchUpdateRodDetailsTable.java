package com.shaic.reimbursement.manageclaim.SearchUpdateRodDetails;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchUpdateRodDetailsTable extends GBaseTable<SearchCreateRODTableDTO>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","crmFlagged","intimationNo", "claimNo", "policyNo","acknowledgementNumber", 
		"insuredPatientName", "strCpuCode", "claimType", "hospitalName",  "hospitalCity", "dateOfAdmission", "reasonForAdmission","isDocumentUploaded"}; 

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchCreateRODTableDTO>(SearchCreateRODTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("250px");
	}

	@Override
	public void tableSelectHandler(SearchCreateRODTableDTO t) {
		if(t.getAccidentOrDeath() != null && (t.getAccidentOrDeath().equalsIgnoreCase(SHAConstants.ACCIDENT_FLAG) 
				|| t.getAccidentOrDeath().equalsIgnoreCase(SHAConstants.DEATH_FLAG))){
			fireViewEvent(PAMenuPresenter.PA_CREATE_ROD_WIZARD, t,SHAConstants.UPDATE_ROD_DETAILS_SCREEN);
		} else {
			fireViewEvent(MenuPresenter.SHOW_UPDATE_ROD_DETAILS, t,SHAConstants.UPDATE_ROD_DETAILS_SCREEN);
		}
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-create-rod-";
	}
	
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
