package com.shaic.claim.outpatient.processOPpages;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;

@SuppressWarnings("serial")
public class OpUploadedDocumentDetails extends GBaseTable<UploadDocumentDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"fileName","fileTypeValue","createdBy","createdDate"}; 
	
	private String screenName;
	
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {

		getLayout().setComponentAlignment(getCaptionLayout(), Alignment.TOP_LEFT);		
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(UploadDocumentDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(5);
	}

	@Override
	public void tableSelectHandler(UploadDocumentDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "op-upl-doc-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(5);
		}
	}

}
