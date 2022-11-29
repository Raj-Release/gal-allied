package com.shaic.claim.reports.opinionvalidationreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author GokulPrasath.A
 *
 */
public class OpinionValidationReportTable extends GBaseTable<OpinionValidationReportTableDTO> {
	private static final long serialVersionUID = -228090196171778814L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber", "intimationNo", "updatedBy", "updatedDateTime", "consultedRole", 
		"consultedName", "consultedRemarks", "validatedBy", "validatedDateTime", "validatedStatus", "validatedRemarks"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<OpinionValidationReportTableDTO>(OpinionValidationReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("480px");
	}
	@Override
	public void tableSelectHandler(OpinionValidationReportTableDTO t) {
		fireViewEvent(MenuPresenter.MEDICAL_MAIL_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "opinion-validation-report-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
		}
	}
}
