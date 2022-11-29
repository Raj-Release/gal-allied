package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class CorporateBufferUtilisationTable extends GBaseTable<CorporateBufferUtilisationTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"typeOfClaim", "referenceOrRodNo","status","corpBufferUtilisation"
		};

	@Override
	public void removeRow() {}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<CorporateBufferUtilisationTableDTO>(CorporateBufferUtilisationTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("100px");	
		table.setSelectable(false);
//		table.setSizeFull();
		
	}

	@Override
	public void tableSelectHandler(CorporateBufferUtilisationTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-corporate-buffer-utilisation-table-";
	}
	
}
