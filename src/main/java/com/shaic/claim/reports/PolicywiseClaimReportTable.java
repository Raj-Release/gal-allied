package com.shaic.claim.reports;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PolicywiseClaimReportTable extends GBaseTable<PolicywiseClaimReportDto> {
	
	
	private static final Object[] COLUMN_HEADER = new Object[] {
		"sno","intimationDate","intimationNo","mainMemberName","insuredPatientName","age","gender","iDCard","dataOfDischarge",
		"relationshipWithInsured","provisionalDiagnosis","dateOfAdmission","isNetworkHospital","hospitalName","hospitalCity",
		"cashlessOrReimbursement","managementType","outstandingAmount","paidAmount","claimedAmount","paidDate","chequeNo",
		"chequeDate","modeOfPayment","queryRaisedDate","queryReplyReceivedDate","rejectionReason","queryReason","claimStatus",
		"iCDCode","iCDDescription","employeeID","claimClassification","rODDate","claimRejectedDate" };
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<PolicywiseClaimReportDto>(PolicywiseClaimReportDto.class));
			table.setVisibleColumns(COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setColumnWidth("claimRejectedDate", 180);
			table.setHeight("380px");
	}

	@Override
	public void tableSelectHandler(PolicywiseClaimReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "searchClaimPolicywiseReport-";
	}

}
