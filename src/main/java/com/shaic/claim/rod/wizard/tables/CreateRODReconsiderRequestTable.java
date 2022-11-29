/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;

/**
 * @author ntv.vijayar
 * 
 * The data fetched from acknowledgement
 * document table will be populated in 
 * this table. This table is displayed in 
 * Acknowledge Document Received page.
 *
 */
public class CreateRODReconsiderRequestTable extends GBaseTable<ReconsiderRODRequestTableDTO>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	////private static Window popup;
	
	private ViewDetails objViewDetails;

/*	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;*/

	/*private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"acknowledgementNo", "rodNo", "documentReceivedFrom", "documentReceivedDate",
			"modeOfReceipt", "billClassification", "approvedAmt", "rodStatus" };*/
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		 "rodNo",  "billClassification", "claimedAmt" ,"approvedAmt", "rodStatus" };

	@Override
	public void removeRow() {
		table.removeAllItems();

	}
	
	public void setViewDetailsObj(ViewDetails viewDetails)
	{
		objViewDetails = viewDetails;
	//	initTable();
	}
	

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ReconsiderRODRequestTableDTO>(
				ReconsiderRODRequestTableDTO.class));
		
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.removeGeneratedColumn("viewstatus");
		table.removeGeneratedColumn("select");
		table.setPageLength(3);
		table.setMultiSelect(false);
		table.setNullSelectionAllowed(false);
		table.addGeneratedColumn("viewstatus", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				ReconsiderRODRequestTableDTO reconsiderDTO = (ReconsiderRODRequestTableDTO) itemId;
				final String intimationNo = reconsiderDTO.getIntimationNo();
				
				
				Button button = new Button("View Claim Status");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {

						/**
						 * The button click event needs to be added post the view details
						 * page is developed. Once that is done, the same can be injected and
						 * re used. Below commented code can be uncommented and with slight
						 * modification , view details button should work. 
						 * This will changed later. 
						 * **/
						
						displayClaimStatus(intimationNo);
					}
				});
				return button;
			}
		});
		
		/**
		 * Siva said, this column can be removed and instead of it, we will
		 * have a complete row selected like search screen. But during development
		 * have implemented as per screen. 
		 * */
		
		
		table.addGeneratedColumn("select", new Table.ColumnGenerator() {
			
			//CheckBox chkbox = new CheckBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CheckBox chkBox = new CheckBox();
				final ReconsiderRODRequestTableDTO reconsiderDTO = (ReconsiderRODRequestTableDTO) itemId;
				
				chkBox.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						List<ReconsiderRODRequestTableDTO> items = (List<ReconsiderRODRequestTableDTO>) table.getItemIds();
						
						boolean value = (Boolean) event.getProperty().getValue();
						reconsiderDTO.setSelect(value);
						for (ReconsiderRODRequestTableDTO dto : items) {
								if((reconsiderDTO.getAcknowledgementNo().equals(dto.getAcknowledgementNo())))
								{
									
									tableSelectHandler(reconsiderDTO);
								}
						}
						
					}
				});
				if(null != reconsiderDTO.getSelect() && reconsiderDTO.getSelect()) 
				{
					chkBox.setValue(reconsiderDTO.getSelect());
				}
				return chkBox;
		}
		});
		
		/*table.setCellStyleGenerator(new Table.CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				
				Item item = table.getItem(itemId);
				
			}
		});*/
	}

	private void displayClaimStatus(String intimationNo)
	{
		//objViewDetails = viewDetails.get();
		//queryDetailsObj.init(viewDetails);
		objViewDetails.viewClaimStatusUpdated(intimationNo);
	}
	
	@Override
	public void tableSelectHandler(ReconsiderRODRequestTableDTO t) {
		
		fireViewEvent(CreateRODDocumentDetailsPresenter.SELECT_RECONSIDER_TABLE_VALUES, t);
	}
	
	public void disableTableItems(ReconsiderRODRequestTableDTO t)
	{
		List<ReconsiderRODRequestTableDTO> items = (List<ReconsiderRODRequestTableDTO>) table.getItemIds();
		for (ReconsiderRODRequestTableDTO dto : items) {
			if(!(t.getAcknowledgementNo().equals(dto.getAcknowledgementNo())))
			{
				Item item = table.getItem(dto);
				item.getItemProperty(dto.getSelect()).setReadOnly(true);
			}
		}
	}

	@Override
	public String textBundlePrefixString() {
		return "reconsider-rod-request-details-";
	}	

}

