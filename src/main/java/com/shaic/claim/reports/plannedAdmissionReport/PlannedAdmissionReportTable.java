package com.shaic.claim.reports.plannedAdmissionReport;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PlannedAdmissionReportTable extends GBaseTable<PlannedAdmissionReportDto> {
	
	
	private static final Object[] COLUMN_HEADER = new Object[] {	
		"sno","emailId","intimationNo","insuredName","admittedOn","createdOn","hospitalName","hospitalCity","doctorName"};
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<PlannedAdmissionReportDto>(PlannedAdmissionReportDto.class));
//			table.setRowHeaderMode(Table.ROW_HEADER_MODE_INDEX);
//			
//			table.removeGeneratedColumn("sendemail");
//			table.addGeneratedColumn("sendemail",
//					new Table.ColumnGenerator() {
//						@Override
//						public Object generateCell(Table source,
//								final Object itemId, Object columnId) {
////							CheckBox mailChkBox = new CheckBox("");
////							return mailChkBox;
//							return "";
//						}
//			});			
			table.setVisibleColumns(COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setHeight("480px");
	}

	@Override
	public void tableSelectHandler(PlannedAdmissionReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "planned-admission-report-";
	}

}
