package com.shaic.claim.reports.marketingEscalationReport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reports.opinionvalidationreport.OpinionValidationReportTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class MarketingEscalationReportTable extends GBaseTable<MarketingEscalationReportTableDTO> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3410048715107649795L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber", "intimationNo", "cpuName", "zone","productNameCode", "claimType", "hospitalName", "escalatedDate", 
		"escalatedRole", "escalatedBy", "reasonForEscalation", "actionTaken","lackOfMrkPersonnel", "doctorRemarks","recordedBy", "recordedDate"};
	

	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<MarketingEscalationReportTableDTO>(MarketingEscalationReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("280px");
		
	}

	@Override
	public void tableSelectHandler(MarketingEscalationReportTableDTO t) {
		
		
	}

	@Override
	public String textBundlePrefixString() {
		return "marketing-escalation-report-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
		}
	}

}
