package com.shaic.claim.reports.automationdashboard;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface AutomationDashboardView  extends Searchable{
	
	public void list(Page<AutomationDashboardTableDTO> tableRows);
	//public void setupDroDownValues();

}
