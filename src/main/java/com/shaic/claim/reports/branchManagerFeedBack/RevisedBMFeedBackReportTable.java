package com.shaic.claim.reports.branchManagerFeedBack;

import java.util.WeakHashMap;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;
/**
 * Part of CR R1238
 * @author Lakshminarayana
 *
 */
public class RevisedBMFeedBackReportTable extends GBaseTable<BranchManagerFeedBackReportDto>{
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	public void setReportColHeader(){
		table.setVisibleColumns(new Object[] {
				"branchCode", "branchName", "rating", 
				"merReported", "merPending", "merResponded", 
				"claimRetailReported", "claimRetailPending", "claimRetailResponded",
				"claimGmcReported", "claimGmcPending", "claimGmcResponded" });
	}
	
	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<BranchManagerFeedBackReportDto>(BranchManagerFeedBackReportDto.class));
			setReportColHeader();
			
			table.setColumnWidth("branchCode", 95);
			table.setColumnWidth("branchName",450);
			table.setColumnWidth("rating", 120);
			table.setColumnWidth("policyReported", 80);
			table.setColumnWidth("policyPending", 80);
			table.setColumnWidth("policyResponded", 80);
			table.setColumnWidth("merReported", 80);
			table.setColumnWidth("merPending", 80);
			table.setColumnWidth("merResponded", 80);
			table.setColumnWidth("claimReported", 80);
			table.setColumnWidth("claimPending", 80);
			table.setColumnWidth("claimResponded", 80);
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");
			
	}

	@Override
	public void tableSelectHandler(BranchManagerFeedBackReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "revised-branchmanager-feedback-branch-report-";
	}
	
}
