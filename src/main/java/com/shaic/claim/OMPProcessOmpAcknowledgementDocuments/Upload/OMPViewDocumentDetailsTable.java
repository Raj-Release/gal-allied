package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPViewDocumentDetailsTable extends GBaseTable<OMPViewDocumentDetailsTableDTO> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*public Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber",
	 "value", "mandatoryDocFlag","requiredDocType", "receivedStatus", "noOfDocuments" , "remarks"};*/

// Visible columns have been modified for document checklist enhancement
	private final static Object[] VISIBLE_COL_ORDER = new Object[]{"classification", "subClassification", "category"}; 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		BeanItemContainer<OMPViewDocumentDetailsTableDTO> beanItemContainer = new BeanItemContainer<OMPViewDocumentDetailsTableDTO>(OMPViewDocumentDetailsTableDTO.class);
		table.setContainerDataSource(beanItemContainer);
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setWidth("100%");
		table.setPageLength(table.size() + 4);
		
		
	}

	@Override
	public void tableSelectHandler(OMPViewDocumentDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "ompdocumentdetails-";
	}

}
