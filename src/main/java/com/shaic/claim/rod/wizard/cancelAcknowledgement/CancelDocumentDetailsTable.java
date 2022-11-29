package com.shaic.claim.rod.wizard.cancelAcknowledgement;

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.Reindeer;

public class CancelDocumentDetailsTable extends GBaseTable<ViewDocumentDetailsDTO> {
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","acknowledgeNumber","receivedFromValue","documentReceivedDate","modeOfReceiptValue"
		,"billClassification","approvedAmount","status"};*/
	
	
	/*public static final Object[] NATURAL_COL_ORDER_PA = new Object[] {"serialNumber","acknowledgeNumber","receivedFromValue","documentReceivedDate","modeOfReceiptValue"
		,"benefits","approvedAmount","status"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	public void setPAColumns()
	{
		Object[] NATURAL_COL_ORDER_PA = new Object[] {"serialNumber","acknowledgeNumber","receivedFromValue","documentReceivedDate","modeOfReceiptValue"
			,"benefits","approvedAmount","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER_PA);
		table.setColumnHeader("benefits", "Benefit/Cover");
		
		table.removeGeneratedColumn("cancel");
		table.addGeneratedColumn("cancel", new Table.ColumnGenerator() {
			
			//CheckBox chkbox = new CheckBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				final ViewDocumentDetailsDTO rodDTO = (ViewDocumentDetailsDTO) itemId;
				
						final CheckBox chkBox = new CheckBox();
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								
								Boolean value = (Boolean) event.getProperty().getValue();
								if(value != null){
								   if(value){
									   reConfirmMessage(rodDTO,chkBox);
								   }else{
									   chkBox.setValue(false);
									   rodDTO.setCloseClaimStatus(false);
								   }
								}
								
							}
						});
						return chkBox;
				
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
		
		table.setColumnHeader("cancel", "Cancel");
	}
	
	@Override
	public void initTable() {
		
		
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ViewDocumentDetailsDTO>(
				ViewDocumentDetailsDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber","acknowledgeNumber","receivedFromValue","documentReceivedDate","modeOfReceiptValue"
			,"billClassification","approvedAmount","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 2);
		table.setHeight("200px");
		
		table.removeGeneratedColumn("cancel");
		table.addGeneratedColumn("cancel", new Table.ColumnGenerator() {
			
			//CheckBox chkbox = new CheckBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				final ViewDocumentDetailsDTO rodDTO = (ViewDocumentDetailsDTO) itemId;
				
						final CheckBox chkBox = new CheckBox();
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								
								Boolean value = (Boolean) event.getProperty().getValue();
								if(value != null){
								   if(value){
									   reConfirmMessage(rodDTO,chkBox);
								   }else{
									   chkBox.setValue(false);
									   rodDTO.setCloseClaimStatus(false);
								   }
								}
								
							}
						});
						return chkBox;
				
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
		
		table.setColumnHeader("cancel", "Cancel");
		
	}
	
	
	public void reConfirmMessage(final ViewDocumentDetailsDTO rodDTO,final CheckBox chkBox){
		
		ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Cancel the Acknowledgement?",
		        "No", "Yes", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	rodDTO.setCloseClaimStatus(true);
		                } else {
		                    dialog.close();
		                    rodDTO.setCloseClaimStatus(false);
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
		
		return "cancel-acknowledgement-details-";
	}
	
	 public List<ViewDocumentDetailsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<ViewDocumentDetailsDTO> itemIds = (List<ViewDocumentDetailsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

}
