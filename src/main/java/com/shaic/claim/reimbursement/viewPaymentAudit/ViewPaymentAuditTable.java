package com.shaic.claim.reimbursement.viewPaymentAudit;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewPaymentAuditTable extends GBaseTable<ViewPaymentAuditDTO>{

/**
	 * 
	 */
	private static final long serialVersionUID = 1105233597711677357L;
private final static Object[] NATURAL_COL_ORDER = new Object[]{"stage","auditDate"};
	
	@Override
	public void removeRow() {
		table.removeAllItems();	
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewPaymentAuditDTO>(ViewPaymentAuditDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		table.setWidth("500px");
		
	}

	@Override
	public void tableSelectHandler(ViewPaymentAuditDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "viewpaymentaudit-";
	}
}
