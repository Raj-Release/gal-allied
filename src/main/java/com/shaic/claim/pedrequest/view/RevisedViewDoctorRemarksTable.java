package com.shaic.claim.pedrequest.view;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;

public class RevisedViewDoctorRemarksTable extends GBaseTable<ViewDoctorRemarksDTO>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {/* "select",*/
			"strNoteDate", "userId","transaction","transactionType" ,"remarks"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewDoctorRemarksDTO>(
				ViewDoctorRemarksDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.setColumnWidth("strNoteDate", 100);
		table.setColumnWidth("userId", 150);
		table.setColumnWidth("transaction", 200);
		table.setColumnWidth("transactionType", 200);
		//table.setColumnExpandRatio("remarks", 20.0f);
		
		table.setColumnAlignment("remarks", Table.ALIGN_LEFT);
		
		table.setWidth("100%");
		//table.setWidth("70%");

	}

	@Override
	public void tableSelectHandler(ViewDoctorRemarksDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "view-doctorremarks-";
	}


}
