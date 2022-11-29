package com.shaic.claim.reports.finapprovednotsettledreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;
/**
 * Part of CR R1201
 * @author Lakshminarayana
 *
 */
public class FinApprovedPaymentPendingReportTable extends GBaseTable<FinApprovedPaymentPendingReportDto>{
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	public void setReportColHeader(){
		table.setVisibleColumns(new Object[] {
				"sno", "claimNumber", "intimationNumber", "policyNumber", "prodName", "cpuCode", "diagnosis", "hospitalName", "hospCode",
				"patientName", "paidDate", "approvedAmount", "claimedAmount", "deductAmount", "clmType", "medicalApprovedBy", "billingApprovedBy",
				"finApprovedBy", "clmCoverage", "sumInsured", "age", "financialYear", "admissionDate", "intimationDate" });
	}
	
	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<FinApprovedPaymentPendingReportDto>(FinApprovedPaymentPendingReportDto.class));
			setReportColHeader();
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");	
			table.setColumnWidth("admissionDate", 100);
	}

	@Override
	public void tableSelectHandler(FinApprovedPaymentPendingReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "fin-approved-not-settled-report-";
	}
	
}
