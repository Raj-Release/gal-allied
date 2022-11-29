package com.shaic.reimbursement.manageclaim.closeclaim.searchBasedClaimlevel;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

/**
 * 
 *
 */
public class SearchCloseClaimTable extends GBaseTable<SearchCloseClaimTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "policyNo", "claimType",
		"insuredPatientName", "cpuCode", "hospitalName", "hospitalCity", "noofRods", "dateOfAdmission", "claimStatus"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchCloseClaimTableDTO>(SearchCloseClaimTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.removeGeneratedColumn("dateOfAdmission");
		table.addGeneratedColumn("dateOfAdmission", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  SearchCloseClaimTableDTO closeClaimDTO = (SearchCloseClaimTableDTO)itemId;
		    	  
		    	  String formatDate = SHAUtils.formatIntimationDateForEdit(closeClaimDTO.getDateOfAdmission());
		    	  return formatDate;   
		      }
		});
		
		
		table.setColumnHeader("dateOfAdmission", "Date of Admission");
		
		table.setColumnWidth("hospitalCity", 250);
		table.setHeight("331px");
		table.setSizeFull();
	}

	@Override
	public void tableSelectHandler(
			SearchCloseClaimTableDTO t) {

			fireViewEvent(MenuPresenter.CLOSE_CLAIM_PAGE, t);

	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-close-claim-";
	}
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
