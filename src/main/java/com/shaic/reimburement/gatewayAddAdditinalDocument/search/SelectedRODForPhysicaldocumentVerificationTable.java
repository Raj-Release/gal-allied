package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claims.reibursement.addaditionaldocuments.SelectRODtoAddAdditionalDocumentsDTO;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class SelectedRODForPhysicaldocumentVerificationTable extends GBaseTable<SelectRODtoAddAdditionalDocumentsDTO>{
	

	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"rodNo","billClassification","docUplodedDate"	
		};
	
	
	@Override	public void removeRow() {
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SelectRODtoAddAdditionalDocumentsDTO>(SelectRODtoAddAdditionalDocumentsDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		generateColumn();
		table.setHeight("100px");
			}
	@Override
	public void tableSelectHandler(SelectRODtoAddAdditionalDocumentsDTO t) {
		
	}
	
	@Override
	public String textBundlePrefixString() {
		return "received-physical-document-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

	private void generateColumn()
	{		
		table.removeGeneratedColumn("documentverified");
		table.addGeneratedColumn("documentverified", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final CheckBox verfiedcheckBox = new CheckBox();
		    	verfiedcheckBox.setData(itemId);
		    	
		    	//UploadDocumentDTO uploadDTOForEdit = (UploadDocumentDTO) itemId;		   
		    	
		    	verfiedcheckBox.addValueChangeListener(new ValueChangeListener(){
					private static final long serialVersionUID = 6100598273628582002L;

					@Override
					public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
						Boolean value = false;
						
						CheckBox component = (CheckBox)event.getProperty();
						SelectRODtoAddAdditionalDocumentsDTO roddtDto = (SelectRODtoAddAdditionalDocumentsDTO)component.getData();
						if(event.getProperty() != null && event.getProperty().getValue() != null) {
						 value = (Boolean) event.getProperty().getValue();
						}					
						roddtDto.setIsDocumentVerified(value);
			        } 
			    });
		    	verfiedcheckBox.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		        return verfiedcheckBox;
		      }
		    });
		
		table.setColumnHeader("documentverified", "Original Document Verified");
}
	
	public List<SelectRODtoAddAdditionalDocumentsDTO> getValues(){
		List<SelectRODtoAddAdditionalDocumentsDTO> itemIds = (List<SelectRODtoAddAdditionalDocumentsDTO>)table.getItemIds();
		return itemIds;
	}

}
