package com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;

public class OMPCreIntProposerDetails extends GBaseTable<OMPProposerDetailsDTO>{
	
	private static final long serialVersionUID = -8171257033661627083L;
	
	
	
	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"sno","ackNo","rodNo","ackClassification","ackCategory","rodClaimType","totalAmount","finalAmount","approvedDate","clmStatus"};

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {		
		getLayout().setComponentAlignment(getCaptionLayout(), Alignment.TOP_LEFT);
		table.setContainerDataSource(new BeanItemContainer<OMPProposerDetailsDTO>(OMPProposerDetailsDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(2);
	}

	@Override
	public void tableSelectHandler(OMPProposerDetailsDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "omp-ci-proposer-details-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=2){
			table.setPageLength(2);
		}
	}

}
