package com.shaic.claim.cashlessprocess.processicac.search;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewICACTeamResponseTabel extends GBaseTable<ProcessingICACTeamResponseDTO>{

	private static final long serialVersionUID = 1L;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<ProcessingICACTeamResponseDTO>(ProcessingICACTeamResponseDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"repliedDate","icacResponseRemarks","responseGivenBY"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-icac-response-table-";
	}

	@Override
	public void tableSelectHandler(ProcessingICACTeamResponseDTO t) {
		// TODO Auto-generated method stub
		
	}
}
