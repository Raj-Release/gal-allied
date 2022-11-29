package com.shaic.claim.preauth.view;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pedrequest.view.ViewDoctorRemarksDTO;
import com.shaic.claim.preauth.wizard.dto.ViewPccRemarksDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class ViewPostCashlessRemarkTable extends GBaseTable<ViewPccRemarksDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {/* "select",*/
			"strNoteDate","userId","transactionType" ,"remarks"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewPccRemarksDTO>(
				ViewPccRemarksDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.setColumnWidth("strNoteDate", 100);
		table.setColumnWidth("userId", 150);
		table.setColumnWidth("transactionType", 200);
	
		table.setColumnAlignment("remarks", Table.ALIGN_LEFT);
		table.setWidth("100%");

	}

	@Override
	public void tableSelectHandler(ViewPccRemarksDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "view-pccremarks-";
	}

}
