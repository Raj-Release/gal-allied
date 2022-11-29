package com.shaic.claim.OMPSearchAndCreateintimation.wizard.pages;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;

public class OMPCreIntPaymentDetails extends GBaseTable<OMPPaymentDetailsDTO>{
	
	private static final long serialVersionUID = -8171257033661627083L;
	
	
	
	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"sno","rodNo","ackClassification","ackCategory","rodClaimType","settledAmount","userName","paymentMode","paymentDate","paymentStatus"};

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {		
		getLayout().setComponentAlignment(getCaptionLayout(), Alignment.TOP_LEFT);
		table.setContainerDataSource(new BeanItemContainer<OMPPaymentDetailsDTO>(OMPPaymentDetailsDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(2);
	}

	@Override
	public void tableSelectHandler(OMPPaymentDetailsDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "omp-ci-payment-details-";
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
