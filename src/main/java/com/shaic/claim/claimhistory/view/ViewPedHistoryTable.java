
package com.shaic.claim.claimhistory.view;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class ViewPedHistoryTable extends GBaseTable<PedHistoryDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"status", "dateAndTime", "userName", "remarks"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<PedHistoryDTO>(
				PedHistoryDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		
		table.removeGeneratedColumn("dateAndTime");
		table.addGeneratedColumn("dateAndTime", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  PedHistoryDTO claimHistoryDTO = (PedHistoryDTO)itemId;
		    	  
//		    	  String formatDate = SHAUtils.formatDateAndTime(claimHistoryDTO.getDateAndTime());
		    	  String formatDate = SHAUtils.formateDateForHistory(claimHistoryDTO.getDateAndTime());
		    	  return formatDate;
//		    	  return null;
		      }
		});
		
//		table.setColumnHeader("userRemark", "User Remarks");
//		table.setColumnHeader("dateAndTime", "Date & Time");
		

	}

	@Override
	public void tableSelectHandler(PedHistoryDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "claim-history-";
	}

}
