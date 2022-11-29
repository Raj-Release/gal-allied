//package com.shaic.reimbursement.queryrejection.draftquery.search;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.shaic.arch.table.GEditableTable;
//import com.shaic.arch.table.TableFieldDTO;
//import com.vaadin.v7.data.util.BeanItemContainer;
//import com.vaadin.ui.Button;
//import com.vaadin.v7.ui.Table;
//import com.vaadin.v7.ui.TextField;
//import com.vaadin.ui.Button.ClickEvent;
//
//public class QueryRedraftLetterDetailsTable extends GEditableTable<DraftQueryLetterDetailTableDto>{
//
//
//
//	private List<DraftQueryLetterDetailTableDto> deltedQueryRedraftRemarksList;
//	
//	public QueryRedraftLetterDetailsTable() {
//		super(DraftQueryLetterDetailTableDto.class);
//		setUp();
//		deltedQueryRedraftRemarksList = new ArrayList<DraftQueryLetterDetailTableDto>();
//	}
//	
//	public static final Object[] VISIBLE_COLUMNS = new Object[] {
//		"sno", "draftOrRedraftRemarks"};
//
//	public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
//
//	{
//		fieldMap.put("sno", new TableFieldDTO("sno", TextField.class, String.class, false));
//		fieldMap.put("draftOrRedraftRemarks", new TableFieldDTO("draftOrRedraftRemarks", TextField.class, String.class, true));
//
//	}
//	
//	@Override
//	protected void newRowAdded() {
//		table.getContainerProperty("DraftQueryLetterDetailTableDto", "sno").setValue(table.getContainerDataSource().size());
//		
//	}
//
//	@Override
//	protected Map<String, TableFieldDTO> getFiledMapping() {
//		return fieldMap;
//	}
//
//	@Override
//	public void deleteRow(Object itemId) {
//		
//		((DraftQueryLetterDetailTableDto)itemId).setDeltedFlag("Y");
//		fireViewEvent(DraftQueryLetterDetailPresenter.DELETE_DRAFT_LETTER_REMARKS,itemId);
//		deltedQueryRedraftRemarksList.add((DraftQueryLetterDetailTableDto)itemId);
//		this.table.getContainerDataSource().removeItem(itemId);
//	}
//
//	@Override
//	public void removeRow() {
//		
//	}
//
//	@Override
//	public void initTable() {
//		
//		table.setContainerDataSource(new BeanItemContainer<DraftQueryLetterDetailTableDto>(DraftQueryLetterDetailTableDto.class));
//		table.setVisibleColumns(VISIBLE_COLUMNS);
//		
//		if(!table.getVisibleItemIds().contains("Delete")){
//			table.removeGeneratedColumn("Delete");
//			table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
//			      @Override
//			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
//			    	Button deleteButton = new Button("Delete");
//			    	deleteButton.addClickListener(new Button.ClickListener() {
//				        public void buttonClick(ClickEvent event) {
//				        	deltedQueryRedraftRemarksList.add((DraftQueryLetterDetailTableDto)itemId);
//				        	 deleteRow(itemId);
//				        } 
//				    });
//			    	return deleteButton;
//			      };
//			});
//		}
//		
//		
//		
//		
//		table.setWidth("100%");
//		table.setPageLength(5);
//	}
//
//	public List<DraftQueryLetterDetailTableDto> getDeltedQueryRedraftList() {
//		return deltedQueryRedraftRemarksList;
//	}
//	
//	
//	@Override
//	public void tableSelectHandler(DraftQueryLetterDetailTableDto t) {
//		
//	}
//
//	@Override
//	public String textBundlePrefixString() {
//		return "query-redraft-letter-details-";
//	}
//
//	
//}
