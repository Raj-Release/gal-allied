package com.shaic.claim.reports.ackwithoutrodreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class AckWithoutRodReportTable extends GBaseTable<AckWithoutRodTableDto>{
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<AckWithoutRodTableDto>(AckWithoutRodTableDto.class));
			table.setVisibleColumns(new Object[] {
					"intimationNumber","billRcvdDate","ackDate","docRecdFrm","cpuCode","cpu","claimType","productCode",
					"productName","userName","userId"});
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");			
	}

	@Override
	public void tableSelectHandler(AckWithoutRodTableDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "ack-without-rod-report-";
	}
	
}
