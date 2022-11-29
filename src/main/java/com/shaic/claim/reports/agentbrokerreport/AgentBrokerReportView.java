package com.shaic.claim.reports.agentbrokerreport;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface AgentBrokerReportView extends Searchable{
	
	public void list(Page< AgentBrokerReportTableDTO> tableRows);
	public void generateReport();
}
