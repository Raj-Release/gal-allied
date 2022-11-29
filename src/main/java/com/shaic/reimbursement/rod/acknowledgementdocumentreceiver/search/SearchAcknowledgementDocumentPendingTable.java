package com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchAcknowledgementDocumentPendingTable extends
GBaseTable<SearchAcknowledgementDocumentPendingTableDTO> {
	
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"intimationNo", "claimNo", "policyNo" , "insuredPatientName" , "cpuCode" , "hospitalName" , "hospitalAddress" , "hospitalCity" , "dateofAdmission" ,
			"rodNo" , "rodStatus" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchAcknowledgementDocumentPendingTableDTO>(
				SearchAcknowledgementDocumentPendingTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"intimationNo", "claimNo", "policyNo" , "insuredPatientName" , "cpuCode" , "hospitalName" , "hospitalAddress" , "hospitalCity" , "dateofAdmission" ,
			"rodNo" , "rodStatus" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		// table.setHeight("200px");

	}

	@Override
	public void tableSelectHandler(SearchAcknowledgementDocumentPendingTableDTO t) {
		// TODO
		// fireViewEvent(MenuItemBean.SHOW_HOSPITAL_ACKNOWLEDGE_FORM,
		// t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
		return "acknowledgementpendingcases-";
	}

}
