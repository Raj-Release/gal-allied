package com.shaic.claim.viewEarlierRodDetails.Table;

import java.util.List;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.InvesAndQueryAndFvrParallelFlowTableDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

public class ViewCancelledFvrInvsQueryTable extends GBaseTable<InvesAndQueryAndFvrParallelFlowTableDTO>{


	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"type","remarks","initiatedDate","status","cancelRemarks"};
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<InvesAndQueryAndFvrParallelFlowTableDTO>(InvesAndQueryAndFvrParallelFlowTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("200px");
		
	}
	@Override
	public void tableSelectHandler(InvesAndQueryAndFvrParallelFlowTableDTO t) {
		//fireViewEvent(MenuPresenter.FVR_ASSINMENT_REPORT, t);
	}
	
	@Override
	public String textBundlePrefixString() {
		return "fvrInvesQueryParallelFlow-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

	
	public List<InvesAndQueryAndFvrParallelFlowTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<InvesAndQueryAndFvrParallelFlowTableDTO> itemIds = (List<InvesAndQueryAndFvrParallelFlowTableDTO>) this.table.getItemIds() ;
    	return itemIds;
    }



}
