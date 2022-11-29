package com.shaic.claim.cashlessprocess.processicac.search;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table.Align;

public class ViewProcessingDoctorDetailsTabel extends GBaseTable<ProcessingDoctorDetailsDTO>{

	private static final long serialVersionUID = 1L;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<ProcessingDoctorDetailsDTO>(ProcessingDoctorDetailsDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"doctorIdAndName","referToIcacRemarks","remarksRaisedDateTime"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
//		table.setColumnWidth("serialNo",80);
		table.setColumnAlignment("doctorIdAndName",Align.CENTER);
		table.setColumnAlignment("referToIcacRemarks",Align.CENTER);
		table.setColumnAlignment("remarksRaisedDateTime",Align.CENTER);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(ProcessingDoctorDetailsDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-icac-doctor-table-";
	}
}
