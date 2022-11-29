package com.shaic.paclaim.reimbursement.processdraftquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.reimbursement.queryrejection.draftquery.search.DraftQueryLetterDetailTableDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

public class PAQueryDraftLetterDetailsTable extends GEditableTable<DraftQueryLetterDetailTableDto> {

	private List<DraftQueryLetterDetailTableDto> deltedQueryDraftRemarksList;
	
	public PAQueryDraftLetterDetailsTable() {
		super(DraftQueryLetterDetailTableDto.class);
		setUp();
		deltedQueryDraftRemarksList = new ArrayList<DraftQueryLetterDetailTableDto>();
	}
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"serialNumber", "draftOrRedraftRemarks"};*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	{
//		fieldMap.put("sno", new TableFieldDTO("serialNumber", TextField.class, String.class, false));

		fieldMap.put("draftOrRedraftRemarks", new TableFieldDTO("draftOrRedraftRemarks", TextField.class, String.class, true,true,480,500));
	}*/
	
	@Override
	protected void newRowAdded() {
//		table.getContainerProperty("DraftQueryLetterDetailTableDto", "sno").setValue(table.getContainerDataSource().size());
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {

		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

			fieldMap.put("draftOrRedraftRemarks", new TableFieldDTO("draftOrRedraftRemarks", TextField.class, String.class, true,true,480,500));
	
			return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		
		((DraftQueryLetterDetailTableDto)itemId).setDeltedFlag("Y");
		((DraftQueryLetterDetailTableDto)itemId).setProcessType("D");
		deltedQueryDraftRemarksList.add((DraftQueryLetterDetailTableDto)itemId);
		this.table.getContainerDataSource().removeItem(itemId);
//		fireViewEvent(DecideOnQueryPresenter.DELET_DRAFT_LETTER_REMARKS, (DraftQueryLetterDetailTableDto)itemId);
	}

	@Override
	public void removeRow() {
		
	}

	@Override
	public void initTable() {
		
		this.vLayout.setMargin(false);
		table.setContainerDataSource(new BeanItemContainer<DraftQueryLetterDetailTableDto>(DraftQueryLetterDetailTableDto.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"serialNumber", "draftOrRedraftRemarks"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnCollapsingAllowed(false);
		
		table.setColumnExpandRatio("draftOrRedraftRemarks",80.0f);
		
		table.setWidth("80%");
		this.btnLayout.setWidth("80%");
		
		if(!table.getVisibleItemIds().contains("serialNumber")){
			table.removeGeneratedColumn("serialNumber");
			table.addGeneratedColumn("serialNumber", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	 
			    	  BeanItemContainer<DraftQueryLetterDetailTableDto> container = (BeanItemContainer<DraftQueryLetterDetailTableDto>)source.getContainerDataSource(); 
			    	  return container.getItemIds().indexOf((DraftQueryLetterDetailTableDto)itemId)+1;
			    			  
			      };
			});
		}
		
		if(!table.getVisibleItemIds().contains("Delete")){
			table.removeGeneratedColumn("Delete");
			table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	Button deleteButton = new Button("Delete");
			    	deleteButton.addClickListener(new Button.ClickListener() {
				        public void buttonClick(ClickEvent event) {
				        	deltedQueryDraftRemarksList.add((DraftQueryLetterDetailTableDto)itemId);
				        	 deleteRow(itemId);
				        } 
				    });
			    	return deleteButton;
			      };
			});
		}
				
		table.setPageLength(5);
	}

	public List<DraftQueryLetterDetailTableDto> getDeltedQueryDraftRemarksList() {
		return deltedQueryDraftRemarksList;
	}
	
	
	@Override
	public void tableSelectHandler(DraftQueryLetterDetailTableDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "query-draft-letter-details-";
	}
	
	public boolean isValid(){
		boolean notvalid = false;
		BeanItemContainer<DraftQueryLetterDetailTableDto> container =  (BeanItemContainer<DraftQueryLetterDetailTableDto>)this.table.getContainerDataSource();
		
		if(table.getItemIds().size() > 0){
		
			List<DraftQueryLetterDetailTableDto> tableItems = (List<DraftQueryLetterDetailTableDto>)this.getTableItemIds();
			if(tableItems != null && !tableItems.isEmpty()){
				List<DraftQueryLetterDetailTableDto> dummyList = new ArrayList<DraftQueryLetterDetailTableDto>();
				for (DraftQueryLetterDetailTableDto draftQueryLetterDetailTableDto : tableItems) {
					notvalid =	draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks() == null || draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks().isEmpty() ? false : true ; 
		        	if(!notvalid && this.table.getContainerDataSource().size() > 0){
		        		dummyList.add(draftQueryLetterDetailTableDto);
		        	} 
				}
				if(dummyList != null && !dummyList.isEmpty()){
					for (DraftQueryLetterDetailTableDto draftQueryLetterDetailTableDto : dummyList) {
						this.table.getContainerDataSource().removeItem(draftQueryLetterDetailTableDto);	
					}					
				}
			}	
			
		}
		if(table.getItemIds().size() > 0){
			List<DraftQueryLetterDetailTableDto> tableItems = (List<DraftQueryLetterDetailTableDto>)this.getTableItemIds();
			if(tableItems != null && !tableItems.isEmpty()){
				for (DraftQueryLetterDetailTableDto draftQueryLetterDetailTableDto : tableItems) {
					draftQueryLetterDetailTableDto.setSno(tableItems.indexOf(draftQueryLetterDetailTableDto) + 1);
					draftQueryLetterDetailTableDto.setProcessType("D");
					notvalid =	draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks() == null || draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks().isEmpty() || draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks().length() > 500 ? false : true ; 
				}
			}
			else{
				notvalid = false;
			}
	}
		else{
			notvalid = false;
		}
    return notvalid;
	}
}
