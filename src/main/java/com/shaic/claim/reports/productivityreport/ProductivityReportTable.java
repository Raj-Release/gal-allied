package com.shaic.claim.reports.productivityreport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ProductivityReportTable extends GBaseTable<ProductivityReportTableDTO>{
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"intimationNo", "intimationDate", "claimNumber", "policyNumber",
			"cashlessReferenceNo", "claimType", "dateOfInception",
			"breakInPolicy", "noOfPreviousClaims", "portableIfAny",
			"endorsementsIfAny", "currentSumInsured", "changeInSi",
			"productCode", "productType", "insuredName", "patientName",
			"patientAge", "doa", "dod", "diagnosis", "roomRent", "icu",
			"cpuCode", "cpuName", "registrationQueueTime",
			"matchedQueueDateTime", "responseDateAndTime", "documentType",
			"responseDocType", "processingDoctorId", "processingDoctorName",
			"processingDoctorsRemarks", "userId", "userName", "remarks",
			"requestAmount", "requestStatus", "requestProcessedDate",
			"approvedAmount", "hospitalName", "city", "anh",
			"declaredPedIfAny", "copay" };

	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<ProductivityReportTableDTO>(ProductivityReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("480px");
	}
	@Override
	public void tableSelectHandler(ProductivityReportTableDTO t) {
		
	}
	
	@Override
	public String textBundlePrefixString() {
		return "productivity-report-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
		}
	}
}
