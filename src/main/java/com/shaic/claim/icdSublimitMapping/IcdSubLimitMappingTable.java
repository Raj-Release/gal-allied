package com.shaic.claim.icdSublimitMapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.bulkconvertreimb.search.SearchBulkConvertReimbTableDto;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.TextField;

public class IcdSubLimitMappingTable extends GBaseTable<IcdSublimitMappingDto>{
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<IcdSublimitMappingDto>(IcdSublimitMappingDto.class));
			table.setVisibleColumns(new Object[] {
					"sno","icdDescripiton"});
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");
			table.setWidth("60%");
			autoGenerateSelectColumn();			
	}
	
	public Table getTable(){
		return table;
	}
	
	public void autoGenerateSelectColumn() {
		table.removeGeneratedColumn("Select");
		table.addGeneratedColumn("Select",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
					
					final CheckBox selectChk = new CheckBox();
					
					if((IcdSublimitMappingDto)itemId != null){
					
						final IcdSublimitMappingDto mappingDto = (IcdSublimitMappingDto)itemId;
						selectChk.setValue(mappingDto.isSelected());
						selectChk.setData(mappingDto);						
						selectChk
								.addValueChangeListener(new Property.ValueChangeListener() {
									
									@Override
									public void valueChange(ValueChangeEvent event) {
										IcdSublimitMappingDto mappingDto = (IcdSublimitMappingDto) selectChk.getData();
										Boolean selectedValue = (Boolean)event.getProperty().getValue();
										mappingDto.setSelected(selectedValue);
										
										boolean selectAll = true;
										Collection<IcdSublimitMappingDto> itemIds = (Collection<IcdSublimitMappingDto>) table.getItemIds();
										if(itemIds != null){
											for (IcdSublimitMappingDto icdSublimitMapingdto : itemIds) {
												selectAll = selectAll && icdSublimitMapingdto.isSelected(); 
											}	
										fireViewEvent(IcdSubLimitMappingPresenter.UN_CHECK_MAPPING, selectAll);
										}
									}
								});
						//Vaadin8-setImmediate() selectChk.setImmediate(true);
				        return selectChk;
					}
					else{
						return "";
					}
					}
				});
		table.setColumnAlignment("Select", Align.CENTER);
	}

	@Override
	public void tableSelectHandler(IcdSublimitMappingDto t) {
		
	}
	
	public boolean validateSelection(){
		
		Collection<IcdSublimitMappingDto> itemIds = (Collection<IcdSublimitMappingDto>) table.getItemIds();

		for (IcdSublimitMappingDto icdSublimitMapingdto : itemIds) {
			 
			if(icdSublimitMapingdto.isSelected())
			{
				 return true;
			}
		 }
		 return false;
	}
	
	public List<IcdSublimitMappingDto> getTableList(){
		
		return (List<IcdSublimitMappingDto>)table.getContainerDataSource().getItemIds();
	}

	@Override
	public String textBundlePrefixString() {
		return "icd-sublimit-mapping-";
	}
	
}
