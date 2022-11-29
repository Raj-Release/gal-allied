package com.shaic.claim.reimbursement.rawanalysis;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewRawRequestTable extends GBaseTable<RawInitiatedRequestDTO>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","category","subCategory","initiatedBy","intiatedDate","stageValue","resolutionRawValue","remarksfromRaw","rawRemarksUpdatedBy","rawRemarksUpadedDate","statusValue"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<RawInitiatedRequestDTO>(RawInitiatedRequestDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		setSizeFull();
		//table.setHeight("560px");
//		table.setColumnWidth("remarksForEscalation", 550);
		table.setColumnWidth("remarksfromRaw", 250);
		table.setPageLength(7);
		/*table.setColumnWidth("serialNumber", 40);
		table.setColumnWidth("category", 140);
		table.setColumnWidth("initiatedBy", 85);
		table.setColumnWidth("intiatedDate", 85);
		table.setColumnWidth("rawRemarksUpdatedBy", 85);
		table.setColumnWidth("resolutionRawValue", 100);
		table.setColumnWidth("stageValue", 90);
		table.setColumnWidth("rawRemarksUpadedDate", 85);*/
	}

	@Override
	public void tableSelectHandler(RawInitiatedRequestDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "view-raw-replied-";
	}

}
