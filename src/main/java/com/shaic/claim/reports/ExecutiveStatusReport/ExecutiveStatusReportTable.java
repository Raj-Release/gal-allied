package com.shaic.claim.reports.ExecutiveStatusReport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ExecutiveStatusReportTable extends GBaseTable<ExecutiveStatusDetailReportDto>{
	
	private static final Object[] COLUMN_HEADER = new Object[] {		
			"sno","registeredDate","claimProcessingUnit","intimationNo","intimationDate","policyNumber","policyIssueOfficeCode",
			"relationshipWithInsured","sumInsured","balanceSumInsured","mainMemberName","insuredName","patientName","patientAge",
			"admissionDate","admissionReason","hospitalType","hospitalName","state","city","fieldVisitorType","fieldVisitorName",
			"isFVRReportUploaded","isPreauthGiven","provisionalAmount","dischargeDate","isCashlessOrReimbursement","createdModifiedBy",
			"screenStage"};
	
	private static final Object[] REVISED_COLUMN_HEADER = new Object[] {		
		"sno","claimProcessingUnit","intimationNo","policyNumber","patientName","patientAge",
		"admissionReason","hospitalType","hospitalName","city","isCashlessOrReimbursement","createdModifiedBy","screenStage",
		"processDateNtime","transacOutcome","transacRemarks"};
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<ExecutiveStatusDetailReportDto>(ExecutiveStatusDetailReportDto.class));
//			table.setVisibleColumns(COLUMN_HEADER);
			table.setVisibleColumns(REVISED_COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");
	}

	@Override
	public void tableSelectHandler(ExecutiveStatusDetailReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "search-executive-status-report-";
	}


}
