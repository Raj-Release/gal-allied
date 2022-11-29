package com.shaic.claim.corpbuffer.allocation.search;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.main.navigator.ui.RevisedMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class AllocateCorpBufferTable extends GBaseTable<AllocateCorpBufferTableDTO> {

	private static final long serialVersionUID = 1L;

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<AllocateCorpBufferTableDTO>(AllocateCorpBufferTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {
				"serialNumber",
				"intimationNo",
				"policyNo",
				"claimNo",
				"insuredPatientName",
				"claimType",
				"cpuCode",
				"hospitalName",
				"sumInsured",
				"dateOfAdmission"
		};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("300px");
	}

	@Override
	public void tableSelectHandler(AllocateCorpBufferTableDTO tableDto) {
		fireViewEvent(RevisedMenuPresenter.SHOW_ALLOCATE_CORP_BUFFER_WIZARD, tableDto);
	}
	
	protected void tablesize() {
		table.setPageLength(table.size() + 1);
		int length = table.getPageLength();
		if (length >= 7 ) {
			table.setPageLength(7);
		}
	}

	@Override
	public String textBundlePrefixString() {
		return "allocate-corp-buffer-";
	}
	

}

