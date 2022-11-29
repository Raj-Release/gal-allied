package com.shaic.claim.omp.reports.outstandingreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class OmpOutstandingReportTable extends GBaseTable<OmpStatusReportDto>{
	
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
			table.setContainerDataSource(new BeanItemContainer<OmpStatusReportDto>(OmpStatusReportDto.class));
			table.setVisibleColumns(new Object[] {
					"sno","intimationNo", "tpaIntimationNumber", "policyNo", "policyFromDateValue", "policyToDateValue", "branchOfficeCode", 
					"policyIssuingState", "channelType", "productType", "plan", "policySumInsured",	"eventCodeSumInsured", "insuredName", "age",
					"eventCode", "typeofClaim", "natureOfClaim", "ailmentLoss",	"intimationDateValue", "admissionLossDateValue", "country",
					"initialProvisionAmount", "inrConversionRate", "inrValue", "converstionRate",	"claimStatus", "pendingStage"
			});
			
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");			
	}

	@Override
	public void tableSelectHandler(OmpStatusReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "omp-outstand-report-";
	}
	
}
