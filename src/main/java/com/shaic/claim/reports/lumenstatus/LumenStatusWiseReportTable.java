package com.shaic.claim.reports.lumenstatus;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class LumenStatusWiseReportTable extends GBaseTable<SearchLumenStatusWiseDto>{
	
//	private static final Object[] COLUMN_HEADER = new Object[] {
//		"sno","date","clmNo","doi","policyYr","productType","policyNo","policyIssueOffice","agentCode","suminsured",
//		"doa","dod","hospitalName","ailment","claimedAmount","descreption","typeofError","claimHistory","lapse1","lapse2",
//		"lapse3","lapse4","lapse5","cpu","status"};
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<SearchLumenStatusWiseDto>(SearchLumenStatusWiseDto.class));
			table.setVisibleColumns(new Object[] {
					"sno","date","clmNo","doi","policyYr","productType","policyNo","policyIssueOffice","agentCode","suminsured",
					"doa","dod","hospitalName","ailment","claimedAmount","descreption","typeofError","claimHistory","lapse1","cpu","status"});
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");
			table.setColumnWidth("date", 85);
			table.setColumnWidth("clmNo", -1);
			table.setColumnWidth("doi", 85);
			table.setColumnWidth("policyNo", -1);
			table.setColumnWidth("policyIssueOffice", -1);
			table.setColumnWidth("agentCode", -1);
			table.setColumnWidth("doa", 85);
			table.setColumnWidth("dod", 85);
			table.setColumnWidth("hospitalName", -1);
			table.setColumnWidth("ailment", 330);
			table.setColumnWidth("descreption", 300);
			table.setColumnWidth("cpu", 100);
			table.setColumnWidth("status", 180);			
	}

	@Override
	public void tableSelectHandler(SearchLumenStatusWiseDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "lumen-status-report-";
	}
	
}
