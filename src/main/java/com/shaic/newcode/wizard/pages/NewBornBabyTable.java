package com.shaic.newcode.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.newcode.wizard.dto.NewBabyIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

public class NewBornBabyTable extends GEditableTable<NewBabyIntimationDto>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2239330323400228835L;

	public NewBornBabyTable() {
		super(NewBornBabyTable.class);
		setUp();
		deltedBabyList = new ArrayList<NewBabyIntimationDto>();
	}
	
	private List<NewBabyIntimationDto> deltedBabyList;
	
//	public NewBornBabyTable(NewBabyIntimationDto bean) {
//		super(NewBornBabyTable.class);
//		setUp();
//	}
	
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"babyName", "babyRelationship" };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		fieldMap.put("babyName", new TableFieldDTO("babyName",TextField.class,String.class, true));
		fieldMap.put("babyRelationship", new TableFieldDTO("babyRelationship", ComboBox.class, SelectValue.class, true));
	}*/
	
	@Override
	protected void newRowAdded() {

	}

	@Override
	public void removeRow() {
		
	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<NewBabyIntimationDto>(NewBabyIntimationDto.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"babyName", "babyRelationship" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		if(!table.getVisibleItemIds().contains("Delete")){
			table.removeGeneratedColumn("Delete");
			table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	Button deleteButton = new Button("Delete");
			    	deleteButton.addClickListener(new Button.ClickListener() {
				        public void buttonClick(ClickEvent event) {
				        	deltedBabyList.add((NewBabyIntimationDto)itemId);
				        	 deleteRow(itemId);
				        } 
				    });
			    	return deleteButton;
			      };
			});
		}
		
		table.setWidth("100%");
		table.setPageLength(5);
	}

	
	@Override
	public void tableSelectHandler(NewBabyIntimationDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "newBornBabyTable-";
	}
	
	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
	 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

			fieldMap.put("babyName", new TableFieldDTO("babyName",TextField.class,String.class, true));
			fieldMap.put("babyRelationship", new TableFieldDTO("babyRelationship", ComboBox.class, SelectValue.class, true));
		return fieldMap;
	}

	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
			System.out.println(object);
		}
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}

	public List<NewBabyIntimationDto> getDeltedBabyList() {
		return deltedBabyList;
	}

	public void setDeltedBabyList(List<NewBabyIntimationDto> deltedBabyList) {
		this.deltedBabyList = deltedBabyList;
	}

}
