package com.shaic.arch.table;

import java.util.Collection;

import com.shaic.arch.fields.dto.SearchScreenValidationTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class SearchScreenValidationTableForCashless extends GBaseTable<SearchScreenValidationTableDTO>{

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		//"rodNo", "lastCompletedStage", "lastRemarks","lockedUserId"};
	"cashlessNo", "lastCompletedStage","status","lockedUserId"};*/

/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

static {
	
	//The file upload mapping will be changed once the upload component works.
	fieldMap.put("cashlessNo", new TableFieldDTO("cashlessNo",TextField.class, String.class, true));
	fieldMap.put("lastCompletedStage", new TableFieldDTO("lastCompletedStage",TextField.class, String.class, true));
	fieldMap.put("status", new TableFieldDTO("status",TextField.class, String.class, true));
	//fieldMap.put("lastRemarks", new TableFieldDTO("lastRemarks", TextField.class,String.class, true,20));
	fieldMap.put("lockedUserId", new TableFieldDTO("lockedUserId", TextField.class,String.class, true,20));

}*/



public void removeRow(SearchScreenValidationTableDTO dto) {
	
	table.removeItem(dto);
}

@Override
public void initTable() {
//	errorMessages = new ArrayList<String>();
	table.setContainerDataSource(new BeanItemContainer<SearchScreenValidationTableDTO>(
			SearchScreenValidationTableDTO.class));
	 Object[] VISIBLE_COLUMNS = new Object[] {
		//"rodNo", "lastCompletedStage", "lastRemarks","lockedUserId"};
	"cashlessNo", "lastCompletedStage","status","lockedUserId"};
	table.setVisibleColumns(VISIBLE_COLUMNS);
	//table.setWidth("80%");
	//Adding the height for procedure table.
	table.setHeight("140px");
	table.setWidth("100%");
	
	
	//table.setPageLength(table.getItemIds().size());
}

@Override
public String textBundlePrefixString() {
	return "search-screen-validation-";
}



public void validateFields() {
	Collection<?> itemIds = table.getItemIds();
	for (Object object : itemIds) {
	}

}



public void clearTableData()
{
	table.removeAllItems();
}



@Override
public void tableSelectHandler(SearchScreenValidationTableDTO t) {
	// TODO Auto-generated method stub
	
}

@Override
public void removeRow() {
	// TODO Auto-generated method stub
	
}
}
