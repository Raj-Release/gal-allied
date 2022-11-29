package com.shaic.paclaim.manageclaim.reopenclaim.pageClaimLevel;

import java.util.List;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class PAReopenClaimReimbursementTable extends GBaseTable<ViewDocumentDetailsDTO> {
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","acknowledgeNumber","rodNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
		,"billClassification","approvedAmount","status"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ViewDocumentDetailsDTO>(
				ViewDocumentDetailsDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","acknowledgeNumber","rodNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
			,"billClassification","approvedAmount","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
		table.removeGeneratedColumn("documentReceivedDate");
		table.addGeneratedColumn("documentReceivedDate", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  ViewDocumentDetailsDTO documentDTO = (ViewDocumentDetailsDTO)itemId;
		    	  
		    	  String formatDate = SHAUtils.formatDate(documentDTO.getDocumentReceivedDate());
		    	  return formatDate;
		      }
		});
		
		table.setColumnHeader("reopenClaim", "Reopen Claim");
		
	}
	

	@Override
	public void tableSelectHandler(ViewDocumentDetailsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "document-view-details-";
	}
	
	 public List<ViewDocumentDetailsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<ViewDocumentDetailsDTO> itemIds = (List<ViewDocumentDetailsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

}
