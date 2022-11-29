package com.shaic.claim.OMPBulkUploadRejection;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class BulkUploadRejectionTable extends GBaseTable<OMPBulkUploadRejectionResultDto>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private final static Object[] NATURAL_COL_ORDER = new Object[]{"intimationNumber","cpuCode","existingProvision","newProvision"}; 
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"rodNumber","dateOfDispatch","podNumber","modeOfDispatch","remarks"}; 
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<OMPBulkUploadRejectionResultDto>(OMPBulkUploadRejectionResultDto.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
//		table.setColumnWidth("hospitalAddress", 350);
		table.setHeight("260px");
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "bulk-upload-rejection-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
  public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

@Override
public void tableSelectHandler(OMPBulkUploadRejectionResultDto t) {
	// TODO Auto-generated method stub
	
}

}
