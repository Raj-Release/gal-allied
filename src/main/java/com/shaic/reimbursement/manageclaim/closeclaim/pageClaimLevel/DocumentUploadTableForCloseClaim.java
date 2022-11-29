package com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel;

import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;


public class DocumentUploadTableForCloseClaim extends GBaseTable<UploadDocumentCloseClaimDTO> {
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","fileName","strDateAndTime","referenceNo"};*/

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		
		
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentCloseClaimDTO>(
				UploadDocumentCloseClaimDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","fileName","strDateAndTime","referenceNo"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		table.setHeight("100px");
		
		
		table.removeGeneratedColumn("delete");
		table.addGeneratedColumn("delete", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				
				
			    	final Button button = new Button("Delete");
			    	button.addClickListener(new Button.ClickListener() {
				        /**
						 * 
						 */
						private static final long serialVersionUID = 1L;
	
						public void buttonClick(ClickEvent event) {
							table.removeItem(itemId);
							
							UploadDocumentCloseClaimDTO uploadDocumentDto = (UploadDocumentCloseClaimDTO) itemId;
							
							
				        } 
				    });
			    	
//			    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	
//			    	button.addStyleName(ValoTheme.BUTTON_LINK);
			    	button.setWidth("150px");
			    	button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			    	return button;

		      }
		});
		
		table.setColumnHeader("delete", "Delete");
		
		
		
	}
	
	

	@Override
	public void tableSelectHandler(UploadDocumentCloseClaimDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "upload-close-claim-details-";
	}
	
	 public List<UploadDocumentCloseClaimDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<UploadDocumentCloseClaimDTO> itemIds = (List<UploadDocumentCloseClaimDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

}

