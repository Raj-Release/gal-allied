package com.shaic.claim.policy.search.ui.opsearch;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.outpatient.processOPpages.OPSpecialityDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OPViewSpecialityTable extends GBaseTable<OPSpecialityDTO>{
	
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {table.removeAllItems();
	table.setContainerDataSource(new BeanItemContainer<OPSpecialityDTO>(
			OPSpecialityDTO.class));
	Object[] VISIBLE_COLUMNS = new Object[] {
		"sNo","specialityType","pedfromPolicy","ped","remarks" };
	/*table.setColumnHeader("serialNo","S.No");
	table.setColumnHeader("specialityType", "Speciality");
	table.setColumnHeader("pedfromPolicy", "PED (identified) from Policy");
	table.setColumnHeader("ped", "PED");
	table.setColumnHeader("remarks", "Remarks");*/
	table.setVisibleColumns(VISIBLE_COLUMNS);
	table.setHeight("140px");
	table.setWidth("100%");
	table.setPageLength(table.getItemIds().size());}

	@Override
	public void tableSelectHandler(OPSpecialityDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "op-speciality-view-";
	}

}
