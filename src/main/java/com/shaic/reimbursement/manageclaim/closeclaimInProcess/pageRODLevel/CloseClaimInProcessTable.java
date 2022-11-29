package com.shaic.reimbursement.manageclaim.closeclaimInProcess.pageRODLevel;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class CloseClaimInProcessTable extends GBaseTable<ViewDocumentDetailsDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ViewDetails viewDetails;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","acknowledgeNumber","rodNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
		,"billClassification","approvedAmount","status"};
*/
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ViewDocumentDetailsDTO>(
				ViewDocumentDetailsDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","acknowledgeNumber","rodNumber","receivedFromValue","documentReceivedDate","modeOfReceipt"
			,"billClassification","approvedAmount","status"};

		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
		table.removeGeneratedColumn("claimStatus");
		table.addGeneratedColumn("claimStatus", new Table.ColumnGenerator() {
			
			//CheckBox chkbox = new CheckBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				Button button = new Button("View Claim Status");
				button.addClickListener(new Button.ClickListener() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						ViewDocumentDetailsDTO dto = (ViewDocumentDetailsDTO)itemId;
						viewDetails.viewClaimStatusUpdated(dto.getIntimationNumber());
					}

				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;

		}
		});
		
		table.removeGeneratedColumn("documentReceivedDate");
		table.addGeneratedColumn("documentReceivedDate", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 
		    	  ViewDocumentDetailsDTO documentDTO = (ViewDocumentDetailsDTO)itemId;
		    	  
		    	  String formatDate = SHAUtils.formatDate(documentDTO.getDocumentReceivedDate());
		    	  return formatDate;
		      }
		});
		
		table.setColumnHeader("claimStatus", " ");
		
	}
	
	
	public void reConfirmMessage(final ViewDocumentDetailsDTO rodDTO,final CheckBox chkBox){
		
		ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Close the Claim?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	rodDTO.setCloseClaimStatus(true);
		                } else {
		                    dialog.close();
		                    chkBox.setValue(false);
		                }
		            }
		        });
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}

	@Override
	public void tableSelectHandler(ViewDocumentDetailsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "document-view-details-";
	}
	
	 public List<ViewDocumentDetailsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<ViewDocumentDetailsDTO> itemIds = (List<ViewDocumentDetailsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	
	
	

}
