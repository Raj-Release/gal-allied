/**
 * 
 */




package com.shaic.reimbursement.draftinvesigation;

import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class InvestigatorTriggerPointsTable extends GBaseTable<DraftTriggerPointsToFocusDetailsTableDto> {

	private List<DraftTriggerPointsToFocusDetailsTableDto> deletedInvestigationDescList;
	
	//private String presenterString;
	
	public InvestigatorTriggerPointsTable() {
	/*	super(DraftTriggerPointsToFocusDetailsTableDto.class);
		setUp();*/
		// TODO Auto-generated constructor stub
		deletedInvestigationDescList = new ArrayList<DraftTriggerPointsToFocusDetailsTableDto>();
	}
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"serialNumber", "remarks"};*/
	
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	{
		fieldMap.put("serialNumber", new TableFieldDTO(
				"serialNumber", TextField.class, Integer.class, true,
				true, 60, 100));
		
		fieldMap.put("remarks", new TableFieldDTO(
				"remarks", TextField.class, String.class, true,
				true, 650, 2000));
	}*/

	

	
	
	public void initPresenter(String presenterString)
	{
		//this.presenterString = presenterString;
	}

	

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		// TODO Auto-generated method stub
		
		table.setContainerDataSource(new BeanItemContainer<DraftTriggerPointsToFocusDetailsTableDto>(DraftTriggerPointsToFocusDetailsTableDto.class));
		Object[] VISIBLE_COLUMNS = new Object[] {
			"serialNumber", "remarks"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnCollapsingAllowed(false);
		
		table.setColumnExpandRatio("remarks",80.0f);
		
		table.setWidth("80%");
		
		if(!table.getVisibleItemIds().contains("serialNumber")){
			table.removeGeneratedColumn("serialNumber");
			table.addGeneratedColumn("serialNumber", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	 
			    	  BeanItemContainer<DraftTriggerPointsToFocusDetailsTableDto> container = (BeanItemContainer<DraftTriggerPointsToFocusDetailsTableDto>)source.getContainerDataSource(); 
			    	  return container.getItemIds().indexOf((DraftTriggerPointsToFocusDetailsTableDto)itemId)+1;
			    			  
			      };
			});
		}
		
	/*	if(!table.getVisibleItemIds().contains("Delete")){
			table.removeGeneratedColumn("Delete");
			table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	Button deleteButton = new Button("Delete");
			    	deleteButton.addClickListener(new Button.ClickListener() {
				        public void buttonClick(ClickEvent event) {
				        	deletedInvestigationDescList.add((DraftTriggerPointsToFocusDetailsTableDto)itemId);
				        	 deleteRow(itemId);
				        } 
				    });
			    	return deleteButton;
			      };
			});
		}*/
				
		table.setHeight("330px");
		table.setPageLength(table.getItemIds() != null ? table.getItemIds().size() : 1);
	}

	public List<DraftTriggerPointsToFocusDetailsTableDto> getDeletedDraftInvgDescList() {
		return deletedInvestigationDescList;
	}
	
	@Override
	public void tableSelectHandler(DraftTriggerPointsToFocusDetailsTableDto t) {
		// TODO Auto-generated method stub
		
	}
	
	/*public void deleteEmptyRows(){
		boolean notvalid = false;
		
		if(table.getItemIds().size() > 0){
		
			List<DraftTriggerPointsToFocusDetailsTableDto> tableItems = (List<DraftTriggerPointsToFocusDetailsTableDto>)this.getTableItemIds();
			if(tableItems != null && !tableItems.isEmpty()){
				List<DraftTriggerPointsToFocusDetailsTableDto> dummyList = new ArrayList<DraftTriggerPointsToFocusDetailsTableDto>();
				for (DraftTriggerPointsToFocusDetailsTableDto draftTriggerDetailTableDto : tableItems) {
					notvalid =	draftTriggerDetailTableDto.getRemarks() == null || draftTriggerDetailTableDto.getRemarks().isEmpty() ? false : true ; 
		        	if(!notvalid && this.table.getContainerDataSource().size() > 0){
		        		dummyList.add(draftTriggerDetailTableDto);
		        	} 
				}
				if(dummyList != null && !dummyList.isEmpty()){
					for (DraftTriggerPointsToFocusDetailsTableDto draftTriggerDetailTableDto : dummyList) {
						this.table.getContainerDataSource().removeItem(draftTriggerDetailTableDto);	
					}					
				}
			}	
			
		}
	}*/
	

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "investigation-draft-letter-details-";
	}

}
