package com.shaic.claim.reports.helpdeskstatusreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;

public class HelpDeskStatusReportTable extends GBaseTable<HelpDeskStatusReportTableDTO> {
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "intimationDateValue","policyNumber",
		"issueOfficeCode","productType","patientName","hospitalName","hospitalType","callerContactNo","hospitalCode","claimType",
		"billReceivedDateValue","lastQueryReplyDateValue","billingAmount","zonalMedicalStatus","zonalMedicalapprovedDateValue","medicalStatus","medicalApprovedDateValue","billingStage","billinCompletedDateValue",
		"financialStage","financialCompletedDateValue","chequeStatus","cpuCode","cpuName","remainder","remainderDateValue","rodScanStatus","rodBillStatus","receivedFrom"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<HelpDeskStatusReportTableDTO>(HelpDeskStatusReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(
			HelpDeskStatusReportTableDTO t) {
		
		
		/***
		 * need to implement webservice for check the 64vb status
		 */
	fireViewEvent(MenuItemBean.HELP_DESK_STATUS_REPORT, t);
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "helpdeskstatusreport-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


}
