package com.shaic.claim.claimhistory.view;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class ViewOPClaimHistoryTable extends GBaseTable<ViewClaimHistoryDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"typeofClaim","docrecdfrom","dateAndTime", "userID","userName", "claimStage", "status",
			"userRemark" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		if(table!=null){
			table.clear();
		}
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewClaimHistoryDTO>(
				ViewClaimHistoryDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		
		table.removeGeneratedColumn("dateAndTime");
		table.addGeneratedColumn("dateAndTime", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  ViewClaimHistoryDTO claimHistoryDTO = (ViewClaimHistoryDTO)itemId;
		    	  
//		    	  String formatDate = SHAUtils.formatDateAndTime(claimHistoryDTO.getDateAndTime());
		    	  String formatDate = SHAUtils.formateDateForHistory(claimHistoryDTO.getCreatedDate());
		    	  return formatDate;
		      }
		});
		
		table.setColumnHeader("userRemark", "User Remarks");
		table.setColumnHeader("dateAndTime", "Date & Time");
		

	}

	@Override
	public void tableSelectHandler(ViewClaimHistoryDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "claim-history-op-";
	}

}
