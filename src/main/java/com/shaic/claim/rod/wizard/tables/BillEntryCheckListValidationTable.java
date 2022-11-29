/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Alternative;

import com.shaic.arch.table.GListenerTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;

/**
 * @author ntv.vijayar
 *
 */
@Alternative
public class BillEntryCheckListValidationTable extends GListenerTable<DocumentCheckListDTO> implements TableCellSelectionHandler {

	
	private static final long serialVersionUID = 1L;

	//public TextField dummyField;
	
	private static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
	public Object[] VISIBLE_COLUMNS = new Object[] {
		 "value", "mandatoryDocFlag","requiredDocType", "ackReceivedStatus", "noOfDocuments" , "remarks" ,"rodReceivedStatus", "rodRemarks"};
	

	public BillEntryCheckListValidationTable() {
		super(BillEntryCheckListValidationTable.class);
		setUp();
	}

	{
		//fieldMap.put("slNo", new TableFieldDTO("slNo", TextField.class , Integer.class, false));
		fieldMap.put("particulars", new TableFieldDTO("particulars", TextField.class,String.class, false));
		/*fieldMap.put("mandatoryDocFlag", new TableFieldDTO("mandatoryDocFlag", TextField.class, String.class, false));
		fieldMap.put("requiredDocType", new TableFieldDTO("requiredDocType", TextField.class, String.class, false ));*/
		fieldMap.put("ackReceivedStatus", new TableFieldDTO("ackReceivedStatus", TextField.class, String.class, false ));
		fieldMap.put("noOfDocuments", new TableFieldDTO("noOfDocuments", TextField.class, String.class, false ));
		fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class, String.class, false ));
		fieldMap.put("rodReceivedStatus", new TableFieldDTO("rodReceivedStatus", OptionGroup.class, Boolean.class, false ));
		//fieldMap.put("rodReceivedStatus", new TableFieldDTO("rodReceivedStatus", getOptionGroup(), Boolean.class, true ));
		fieldMap.put("rodRemarks", new TableFieldDTO("rodRemarks", TextField.class, String.class, false ));
		
	
	}
	
	public Class getOptionGroup()
	{
		OptionGroup optionGroup = new OptionGroup();
		optionGroup.addItems(getReadioButtonOptions());
		optionGroup.setItemCaption(true, "Yes");
		optionGroup.setItemCaption(false, "No");
		optionGroup.setStyleName("horizontal");
		optionGroup.setRequired(true);
		
		return (optionGroup).getClass();
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		BeanItemContainer<DocumentCheckListDTO> beanItemContainer = new BeanItemContainer<DocumentCheckListDTO>(DocumentCheckListDTO.class);
		table.setContainerDataSource(beanItemContainer);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
	}

	@Override
	public void tableSelectHandler(DocumentCheckListDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "bill-entry-checklist-validation-";
	} 
	
	public void setReference(Map<String, Object> referenceData) {
		super.setReference(referenceData, this);
	}

	
	@Override
	public List<DocumentCheckListDTO> getValues() 
	{
		Collection<DocumentCheckListDTO> coll = (Collection<DocumentCheckListDTO>) table.getItemIds();
		List list;
		if (coll instanceof List){
			list = (List)coll;
		}
		else{
			list = new ArrayList(coll);
		}
		return list;
	}
}
	
