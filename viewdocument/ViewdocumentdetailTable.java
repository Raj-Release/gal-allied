package com.shaic.claim.viewdocument;

//import java.util.List;

//import java.util.ArrayList;
//import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
//import com.shaic.claim.history.ViewClaimHistoryDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
//import com.vaadin.v7.data.util.BeanItemContainer;



public class ViewdocumentdetailTable extends GBaseTable<DocumentCheckListDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7771166917702155656L;
	private static final Object[] NATURAL_COL_ORDER= new Object[]{"slNo","docTypeId","mandatoryDocFlag","requiredDocType","receivedStatus","noOfDocuments","remarks"};
	
	

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
        table.setContainerDataSource(new BeanItemContainer<DocumentCheckListDTO>(DocumentCheckListDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);	
		
		table.setSizeFull();
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
	}
	@Override
	public void tableSelectHandler(DocumentCheckListDTO vto) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public String textBundlePrefixString() {
		
		return "document-historyOP-";
	}


}
