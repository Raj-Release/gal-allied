package com.shaic.paclaim.generateCoveringLetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Table;

public class PACoveringLetterDocTable extends GEditableTable<DocumentCheckListDTO> {
	
		public PACoveringLetterDocTable() {
			super(DocumentCheckListDTO.class);
			setUp();
		}
		
	/*	public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"serialNumber", "particulars"};*/

		/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

		{
//			fieldMap.put("sno", new TableFieldDTO("serialNumber", TextField.class, String.class, false));

			fieldMap.put("particulars", new TableFieldDTO("particulars", ComboBox.class, SelectValue.class, true,true));
		}*/
		
		@Override
		protected void newRowAdded() {
//			table.getContainerProperty("DraftQueryLetterDetailTableDto", "sno").setValue(table.getContainerDataSource().size());
			
		}

		@Override
		protected Map<String, TableFieldDTO> getFiledMapping() {
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();			
				fieldMap.put("particulars", new TableFieldDTO("particulars", ComboBox.class, SelectValue.class, true,true));		
				
				return fieldMap;
		}

		@Override
		public void deleteRow(Object itemId) {
			table.removeItem(itemId);
		}

		@Override
		public void removeRow() {
			
		}

		@Override
		public void initTable() {
			this.vLayout.setMargin(false);
			table.setContainerDataSource(new BeanItemContainer<DocumentCheckListDTO>(DocumentCheckListDTO.class));
			Object[] VISIBLE_COLUMNS = new Object[] {
				"serialNumber", "particulars"};
			table.setVisibleColumns(VISIBLE_COLUMNS);
			table.setColumnCollapsingAllowed(false);
			
//			table.setColumnExpandRatio("particulars",80.0f);
			
			table.setWidth("80%");
			this.btnLayout.setWidth("80%");
			
			if(!table.getVisibleItemIds().contains("serialNumber")){
				table.removeGeneratedColumn("serialNumber");
				table.addGeneratedColumn("serialNumber", new Table.ColumnGenerator() {
				      @Override
				      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				    	 
				    	  BeanItemContainer<DocumentCheckListDTO> container = (BeanItemContainer<DocumentCheckListDTO>)source.getContainerDataSource(); 
				    	  return container.getItemIds().indexOf((DocumentCheckListDTO)itemId)+1;
				    			  
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
					        	 deleteRow(itemId);
					        } 
					    });
				    	return deleteButton;
				      };
				});
			}
					
			table.setPageLength(5);
		}		
				
		@Override
		public void tableSelectHandler(DocumentCheckListDTO t) {
			
		}

		@Override
		public String textBundlePrefixString() {
			return "pa-covering-letter-details-";
		}
		
		public boolean isValid(){
			boolean notvalid = false;
			BeanItemContainer<DocumentCheckListDTO> container =  (BeanItemContainer<DocumentCheckListDTO>)this.table.getContainerDataSource();
			
			if(table.getItemIds().size() > 0){
			
				List<DocumentCheckListDTO> tableItems = (List<DocumentCheckListDTO>)this.getTableItemIds();
				if(tableItems != null && !tableItems.isEmpty()){
					List<DocumentCheckListDTO> dummyList = new ArrayList<DocumentCheckListDTO>();
					for (DocumentCheckListDTO draftQueryLetterDetailTableDto : tableItems) {
						notvalid =	draftQueryLetterDetailTableDto.getParticulars() == null || draftQueryLetterDetailTableDto.getParticulars().getValue() == null || draftQueryLetterDetailTableDto.getParticulars().getValue().isEmpty() ? false : true ;			        	 
					}					
				}	
				
			}
			else{
				notvalid = false;
			}
	    return notvalid;
		}
		
		public List<DocumentCheckListDTO> getTableList(){
			List<DocumentCheckListDTO> tableItems = (List<DocumentCheckListDTO>)this.getTableItemIds();
			
			if( tableItems != null && !tableItems.isEmpty() ){
				for (DocumentCheckListDTO documentCheckListDTO : tableItems) {
					documentCheckListDTO.setParticularsValue(documentCheckListDTO.getParticulars().getValue());
					documentCheckListDTO.setValue(documentCheckListDTO.getParticulars().getValue());
					documentCheckListDTO.setDocTypeId(documentCheckListDTO.getParticulars().getId());
				}
			}
			
			return tableItems;
		}
}
