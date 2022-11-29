package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewDocumentCheckListTable extends GBaseTable<DocumentCheckListDTO> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*public Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber",
	 "value", "mandatoryDocFlag","requiredDocType", "receivedStatus", "noOfDocuments" , "remarks"};*/

// Visible columns have been modified for document checklist enhancement
	public Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber",
		 "value",  "receivedStatus", "noOfDocuments" , "remarks"};
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		BeanItemContainer<DocumentCheckListDTO> beanItemContainer = new BeanItemContainer<DocumentCheckListDTO>(DocumentCheckListDTO.class);
		table.setContainerDataSource(beanItemContainer);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setPageLength(table.size() + 4);
		
		
	}

	@Override
	public void tableSelectHandler(DocumentCheckListDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-document-check-";
	}

}
