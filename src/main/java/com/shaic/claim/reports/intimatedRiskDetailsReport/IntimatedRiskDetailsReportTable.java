package com.shaic.claim.reports.intimatedRiskDetailsReport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class IntimatedRiskDetailsReportTable extends GBaseTable<IntimatedRiskDetailsReportDto> {
	
	
	private static final Object[] COLUMN_HEADER = new Object[] {
		
		"intimationNo","policyNo","claimantName","intimatorName","hospitalName","mode","madeBy","status","date","contactNo"
		};
		
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<IntimatedRiskDetailsReportDto>(IntimatedRiskDetailsReportDto.class));
			table.setVisibleColumns(COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setHeight("480px");
	}

	@Override
	public void tableSelectHandler(IntimatedRiskDetailsReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "intimated-risk_details-report-";
	}

}
