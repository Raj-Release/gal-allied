package com.shaic.claim.reimbursement.rawanalysis;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class RequestRepliedByRawTable extends GBaseTable<RawInitiatedRequestDTO>{
	
//	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","category","remarksForEscalation","initiatedBy","intiatedDate","stageValue","resolutionRawValue","remarksfromRaw","rawRemarksUpdatedBy","rawRemarksUpadedDate"};
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","category","subCategory","initiatedBy","intiatedDate","stageValue","resolutionRawValue","remarksfromRaw","rawRemarksUpdatedBy","rawRemarksUpadedDate"};
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<RawInitiatedRequestDTO>(RawInitiatedRequestDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setWidth("100%");
		table.setHeight("150px");
		table.setColumnWidth("category", 150);
		table.setColumnWidth("subCategory", 150);	
		table.setColumnWidth("initiatedBy", 100);
//		table.setColumnWidth("remarksForEscalation", 150);
		table.setColumnWidth("remarksfromRaw", 150);
		table.setColumnWidth("rawRemarksUpdatedBy", 120);
	}

	@Override
	public void tableSelectHandler(RawInitiatedRequestDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "process-raw-replied-";
	}

}
