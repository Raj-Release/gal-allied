package com.shaic.claim.preauth;

import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewEarlierHRMTable extends GBaseTable<HRMHospitalDetailsTableDTO>{

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"hardCodedString","hardCodedStringValue","hardCodedString1","hardCodedStringValue1"
		};
	
	List<HRMHospitalDetailsTableDTO> dtolist = null;
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		BeanItemContainer<HRMHospitalDetailsTableDTO> container = new BeanItemContainer<HRMHospitalDetailsTableDTO>(HRMHospitalDetailsTableDTO.class);
				
		if(null != dtolist && !dtolist.isEmpty()){
			container.addAll(dtolist);
		}
		table.setContainerDataSource(container);
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");
	}
	@Override
	public void tableSelectHandler(HRMHospitalDetailsTableDTO t) {
		
	}
	

	public void viewInt(List<HRMHospitalDetailsTableDTO> dtolist1 )
	{
		this.dtolist = dtolist1;
	}
	
	@Override
	public String textBundlePrefixString() {
		return "srmtable-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
}
