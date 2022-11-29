package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;


public class ReconsiderationTableForView extends GBaseTable<ReconsiderRODRequestTableDTO>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	////private static Window popup;
	
	//@Inject
	//private Instance<ViewDetails> viewDetails;
	//@Inject

	private  CheckBox chkBox = null;
	
	List<ReconsiderRODRequestTableDTO> rodList = new ArrayList<ReconsiderRODRequestTableDTO>();

/*	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;*/

	/*private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"acknowledgementNo", "rodNo", "documentReceivedFrom", "documentReceivedDate",
			"modeOfReceipt", "billClassification", "approvedAmt", "rodStatus" };*/
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		 "rodNo",  "billClassification", "claimedAmt" ,"approvedAmt", "rodStatus" };
	
	private static final Object[] NATURAL_COL_ORDER_PA = new Object[] {
		 "rodNo",  "benifitOrCover", "claimedAmt" ,"approvedAmt", "rodStatus" };

	@Override
	public void removeRow() {
		table.removeAllItems();

	}
	
	public void setPAColumns()
	{
		table.setVisibleColumns(NATURAL_COL_ORDER_PA);
		
		table.setColumnHeader("benifitOrCover", "Benefit/Cover");
		table.removeGeneratedColumn("viewstatus");
		
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
					
					
				final ReconsiderRODRequestTableDTO reconsiderDTO = (ReconsiderRODRequestTableDTO) itemId;
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
						 * This will be changed later. 
						 * **/

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
//		table.removeGeneratedColumn("select");
//		table.addGeneratedColumn("select", new Table.ColumnGenerator() {
//			
//			//CheckBox chkbox = new CheckBox();
//			
//			@Override
//			public Object generateCell(Table source, final Object itemId, Object columnId) {
//				// TODO Auto-generated method stub
//				final CheckBox chkBox = new CheckBox();
//				
//				final ReconsiderRODRequestTableDTO reconsiderDTO = (ReconsiderRODRequestTableDTO) itemId;
//			//	if(null != reconsiderDTO.getSelect() )
//				chkBox.addValueChangeListener(new Property.ValueChangeListener() {
//					
//					@Override
//					public void valueChange(ValueChangeEvent event) {
//						List<ReconsiderRODRequestTableDTO> items = (List<ReconsiderRODRequestTableDTO>) table.getItemIds();
//						boolean value = (Boolean) event.getProperty().getValue();
//						reconsiderDTO.setSelect(value);
//						//validateSelectItems(reconsiderDTO);
//						for (ReconsiderRODRequestTableDTO dto : items) {
//								if((reconsiderDTO.getAcknowledgementNo().equals(dto.getAcknowledgementNo())))
//								{
//									
//									tableSelectHandler(reconsiderDTO);
//								}
//						}
//						
//					}
//				});
//				if(null != reconsiderDTO.getSelect() && reconsiderDTO.getSelect()) 
//				{
//					chkBox.setValue(reconsiderDTO.getSelect());
//				}
//				
//				return chkBox;
//		}
//		});
	
		
		/*table.setCellStyleGenerator(new Table.CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				
				Item item = table.getItem(itemId);
				
			}
		});*/
	
	}

	
	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ReconsiderRODRequestTableDTO>(
				ReconsiderRODRequestTableDTO.class));
		
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.removeGeneratedColumn("viewstatus");
		
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
					
					
				final ReconsiderRODRequestTableDTO reconsiderDTO = (ReconsiderRODRequestTableDTO) itemId;
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
						 * This will be changed later. 
						 * **/

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
//		table.removeGeneratedColumn("select");
//		table.addGeneratedColumn("select", new Table.ColumnGenerator() {
//			
//			//CheckBox chkbox = new CheckBox();
//			
//			@Override
//			public Object generateCell(Table source, final Object itemId, Object columnId) {
//				// TODO Auto-generated method stub
//				final CheckBox chkBox = new CheckBox();
//				
//				final ReconsiderRODRequestTableDTO reconsiderDTO = (ReconsiderRODRequestTableDTO) itemId;
//			//	if(null != reconsiderDTO.getSelect() )
//				chkBox.addValueChangeListener(new Property.ValueChangeListener() {
//					
//					@Override
//					public void valueChange(ValueChangeEvent event) {
//						List<ReconsiderRODRequestTableDTO> items = (List<ReconsiderRODRequestTableDTO>) table.getItemIds();
//						boolean value = (Boolean) event.getProperty().getValue();
//						reconsiderDTO.setSelect(value);
//						//validateSelectItems(reconsiderDTO);
//						for (ReconsiderRODRequestTableDTO dto : items) {
//								if((reconsiderDTO.getAcknowledgementNo().equals(dto.getAcknowledgementNo())))
//								{
//									
//									tableSelectHandler(reconsiderDTO);
//								}
//						}
//						
//					}
//				});
//				if(null != reconsiderDTO.getSelect() && reconsiderDTO.getSelect()) 
//				{
//					chkBox.setValue(reconsiderDTO.getSelect());
//				}
//				
//				return chkBox;
//		}
//		});
	
		
		/*table.setCellStyleGenerator(new Table.CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				
				Item item = table.getItem(itemId);
				
			}
		});*/
	}
	
//	public Boolean validateSelectItems()
//	{
//		
//		List<ReconsiderRODRequestTableDTO> items = (List<ReconsiderRODRequestTableDTO>) table.getItemIds();
//		for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : items) {
//			if(null != reconsiderRODRequestTableDTO.getSelect() && reconsiderRODRequestTableDTO.getSelect())
//			{
//				rodList.add(reconsiderRODRequestTableDTO);
//			}
//		}
//		if(null != rodList && !rodList.isEmpty() && rodList.size() > 1)
//		{
//			Label label = new Label("Only one ROD can be selected for reconsideration", ContentMode.HTML);
//			label.setStyleName("errMessage");
//			VerticalLayout layout = new VerticalLayout();
//			layout.setMargin(true);
//			layout.addComponent(label);
//			ConfirmDialog dialog = new ConfirmDialog();
//			dialog.setCaption("Errors");
//			dialog.setClosable(true);
//			dialog.setContent(layout);
//			dialog.setResizable(true);
//			dialog.setModal(true);
//			dialog.show(getUI().getCurrent(), null, true);
//			return false;
//		}
//		return true;
//	}

//	public void disableCheckBox(ReconsiderRODRequestTableDTO reconsiderDTO)
//	{
//		removeRow();
//		List<ReconsiderRODRequestTableDTO> items = (List<ReconsiderRODRequestTableDTO>) table.getItemIds();
//		for (ReconsiderRODRequestTableDTO dto : items) {
//				if((reconsiderDTO.getAcknowledgementNo().equals(dto.getAcknowledgementNo())))
//				{
//					chkBox.setEnabled(true);
//				}
//				else
//				{
//					chkBox.setEnabled(false);
//				}
//				table.addItems(dto);
//		}
//	}

	
	
	@Override
	public void tableSelectHandler(ReconsiderRODRequestTableDTO t) {
		
//		fireViewEvent(DocumentDetailsPresenter.DISABLE_TABLE_VALUES, t);
	}
	
//	public void disableTableItems(ReconsiderRODRequestTableDTO t)
//	{
//		List<ReconsiderRODRequestTableDTO> items = (List<ReconsiderRODRequestTableDTO>) table.getItemIds();
//		for (ReconsiderRODRequestTableDTO dto : items) {
//			if(!(t.getAcknowledgementNo().equals(dto.getAcknowledgementNo())))
//			{
//				Item item = table.getItem(dto);
//				item.getItemProperty(dto.getSelect()).setReadOnly(true);
//			}
//		}
//	}

	@Override
	public String textBundlePrefixString() {
		return "reconsider-rod-request-details-";
	}	
	
	public void setRemoveAllItems(){
		rowCount = 0;
		table.removeAllItems();
	}
	

	


}

