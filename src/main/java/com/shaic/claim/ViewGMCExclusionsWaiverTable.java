package com.shaic.claim;

import java.util.Collection;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewGMCExclusionsWaiverTable extends GBaseTable<ViewGMCExclusionsWaiverTableDto> {

	
	public static final Object[] COLUMN_HEADER = new Object[] {
		"serialNumber",
		"coverCode",
		"coverDesc",
		"sumInsured",
		"rate",
		"ratePer",
		"premimum"
	};
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewGMCExclusionsWaiverTableDto>(
				ViewGMCExclusionsWaiverTableDto.class));
	table.setWidth("100%");
	table.setHeight("200px");
//	table.setPageLength(6);
	table.setVisibleColumns(COLUMN_HEADER);
	}

	@Override
	public void tableSelectHandler(ViewGMCExclusionsWaiverTableDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "gmc-exclusion-waiver-details-";
	}
	
	public void setTableList(final Collection<ViewGMCExclusionsWaiverTableDto> list) {
		table.removeAllItems();
		Long total=new Long(0);
		Long reqTotal =  new Long(0);
		int i =1;
		for (final ViewGMCExclusionsWaiverTableDto bean : list) {
			bean.setSerialNumber(i++);
			table.addItem(bean);
			if(null != bean.getSumInsured()){
				total=total+new Long(bean.getSumInsured());
			}
			if(null != bean.getPremimum()){
				reqTotal = reqTotal + new Long(bean.getPremimum());
			}
		}
		
		table.setColumnFooter("sumInsured", total.toString());
		table.setColumnFooter("premimum", reqTotal.toString());
		table.setFooterVisible(true);
		table.sort();
	}

}
