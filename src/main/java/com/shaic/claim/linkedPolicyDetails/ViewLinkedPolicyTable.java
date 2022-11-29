package com.shaic.claim.linkedPolicyDetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewLinkedPolicyTable extends GBaseTable<ViewLinkedPolicyTableDTO>{
	
	public static final Object[] COLUMN_HEADER = new Object[]{"serialNumber","nameOftheCorporate","policyNumber","mainMemberName","mainMemberId"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewLinkedPolicyTableDTO>(ViewLinkedPolicyTableDTO.class));
		table.setVisibleColumns(COLUMN_HEADER);
		table.setWidth("100%");
		table.setHeight("180px");
		table.setPageLength(table.size());
	}

	@Override
	public void tableSelectHandler(ViewLinkedPolicyTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "view-linked-policy-details-";
	}

}
