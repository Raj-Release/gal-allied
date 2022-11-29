package com.shaic.claim.reports.agentbrokerreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;

public class AgentBrokerReportTable extends GBaseTable<AgentBrokerReportTableDTO>{
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","policyNo","intimationNo","smCode","smName","agentBrokerCode","agentBrokerName","policyIssueOffice","hospitalName",
		"cashlessSettledAmount","reimbursementSettledAmount","outstandingAmount","totalAmount"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<AgentBrokerReportTableDTO>(AgentBrokerReportTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setColumnCollapsingAllowed(false);
		table.setHeight("490px");
		
   
		
	}
	@Override
	public void tableSelectHandler(AgentBrokerReportTableDTO t) {
		fireViewEvent(MenuPresenter.AGENT_BROKER_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "agentbrokerreport-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=10){
			table.setPageLength(10);
			
		}
	}
	

}
