package com.shaic.claim.reports.claimsdailyreportnew;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ClaimsDailyReportTable extends GBaseTable<ClaimsDailyReportDto> {
	
	
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
			table.setContainerDataSource(new BeanItemContainer<ClaimsDailyReportDto>(ClaimsDailyReportDto.class));
			table.setVisibleColumns(COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setColumnWidth("initialProvisionAmt", 180);
			table.setHeight("480px");
	}

	@Override
	public void tableSelectHandler(ClaimsDailyReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "claimsDailyReport-";
	}

}
