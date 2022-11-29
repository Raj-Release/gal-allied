package com.shaic.paclaim.manageclaim.healthreopenclaim.searchClaimLevel;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

/**
 * 
 *
 */
public class PAHealthSearchReOpenClaimTable extends GBaseTable<PAHealthSearchReOpenClaimTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "policyNo", "claimType","cpuCode",
		"insuredPatientName", "hospitalName", "hospitalCity", "dateOfAdmission", "reasonForClose","claimStatus"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<PAHealthSearchReOpenClaimTableDTO>(PAHealthSearchReOpenClaimTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.removeGeneratedColumn("dateOfAdmission");
		table.addGeneratedColumn("dateOfAdmission", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  PAHealthSearchReOpenClaimTableDTO closeClaimDTO = (PAHealthSearchReOpenClaimTableDTO)itemId;
		    	  
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
			PAHealthSearchReOpenClaimTableDTO t) {
		fireViewEvent(PAMenuPresenter.REOPEN_PA_HEALTH_CLAIM_PAGE_VIEW, t);
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-reopen-claim-claim-lvl-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

}
