package com.shaic.claim.reports.gmcdailyreport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class GmcDailyReportTable extends GBaseTable<GmcDailyReportDto> {
	
	
	private static final Object[] COLUMN_HEADER = new Object[] {	
		"sno","intimationNo","intimationDate","policyNumber","productName","insuredProposerName","mainMemberName",
		"CPUcode","patientName","patientAge","admissionReason","callerContactNo","hospitalCode","hospitalType","hospitalDate","hospitalName",
		"hospitalCity","hospitalState","ANHStatus","fieldDoctorNameAllocated","contactNumOfDoctor","dataOfAllocationWithTime",
		"registrationStatus","cashlessOrReimbursement","plannedAdmission","suminsured","provisionAmt","initialProvisionAmt"	};
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<GmcDailyReportDto>(GmcDailyReportDto.class));
			table.setVisibleColumns(COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setColumnWidth("initialProvisionAmt", 180);
			table.setHeight("480px");
	}

	@Override
	public void tableSelectHandler(GmcDailyReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "claimsDailyReport-";
	}

}
