package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPViewUploadDocumentDetailsTable extends GBaseTable<OMPViewUploadDocumentDetailsTableDTO> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*public Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber",
	 "value", "mandatoryDocFlag","requiredDocType", "receivedStatus", "noOfDocuments" , "remarks"};*/

// Visible columns have been modified for document checklist enhancement
	private final static Object[] VISIBLE_COL_ORDER = new Object[]{"fileType", "documentType", "receivedStatus", "noOfDocuments", "remarks"}; 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		BeanItemContainer<OMPViewUploadDocumentDetailsTableDTO> beanItemContainer = new BeanItemContainer<OMPViewUploadDocumentDetailsTableDTO>(OMPViewUploadDocumentDetailsTableDTO.class);
		table.setContainerDataSource(beanItemContainer);
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setWidth("100%");
		table.setPageLength(table.size() + 4);
		
		
	}

	@Override
	public void tableSelectHandler(OMPViewUploadDocumentDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "ompdocumentuploaddetails-";
	}

}

