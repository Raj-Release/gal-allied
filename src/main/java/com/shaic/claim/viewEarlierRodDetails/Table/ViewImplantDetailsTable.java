package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewImplantDetailsTable extends GBaseTable<ImplantDetailsDTO>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<ImplantDetailsDTO>(ImplantDetailsDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber","implantName","implantType","implantCost"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnWidth("serialNo",80);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(ImplantDetailsDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-implant-details-table-";
	}
}
