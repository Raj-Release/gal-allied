package com.shaic.claim.processdatacorrection.viewtable;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.processdatacorrection.dto.TreatingCorrectionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewTreatingCorrectionTabel extends GBaseTable<TreatingCorrectionDTO>{

	private static final long serialVersionUID = 1L;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		if(table!=null){
			table.clear();
		}
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<TreatingCorrectionDTO>(TreatingCorrectionDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"serialNo","treatingDoctorName","actualtreatingDoctorName","qualification","actualqualification"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnWidth("serialNo",80);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(TreatingCorrectionDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-treating-correction-table-";
	}
}
