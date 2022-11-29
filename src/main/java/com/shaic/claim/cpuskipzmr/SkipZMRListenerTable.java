package com.shaic.claim.cpuskipzmr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class SkipZMRListenerTable  extends ViewComponent  {
	
	private static final long serialVersionUID = 2499717380886927304L;

	private Table table;
	
	private Map<SkipZMRListenerTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SkipZMRListenerTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<SkipZMRListenerTableDTO> data = new BeanItemContainer<SkipZMRListenerTableDTO>(SkipZMRListenerTableDTO.class);
	
	
	public Object[] VISIBLE_COLUMNS = new Object[] {"cpuCode","cpuName"};
	
	
	public void init() {
		//public void init(Long hospitalKey, String presenterString,List<DiagnosisDetailsTableDTO> diagnosisList) {
			VerticalLayout layout = new VerticalLayout();
			
			initTable(layout);
			table.setWidth("100%");
			table.setHeight("160px");
			table.setPageLength(table.getItemIds().size());
			
			layout.addComponent(table);

			setCompositionRoot(layout);
		}
	
	
	void initTable(VerticalLayout layout) {
		data.removeAllItems();
		// Create a data source and bind it to a table
		table = new Table("CPU LIST", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "cpuCode","cpuName", "skipZMRForCashless", "skipZMRForReimbursement" });

		table.setColumnHeader("cpuCode", "CPU Code");
		table.setColumnHeader("cpuName", "CPU Name");
		table.setColumnHeader("skipZMRForCashless", "Skip ZMR for Cashless");
		table.setColumnHeader("skipZMRForReimbursement", "Skip ZMR for Reimbursement");
		table.setEditable(true);
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public void addBeanToList(SkipZMRListenerTableDTO skipZMRListenerTableDTO) {
    	data.addItem(skipZMRListenerTableDTO);
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			SkipZMRListenerTableDTO dto = (SkipZMRListenerTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(dto) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(dto, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(dto);
			}
			
			if("skipZMRForCashless".equals(propertyId)) {
				CheckBox box = new CheckBox();
				tableRow.put("skipZMRForCashless", box);
				box.setValue(dto.getSkipZMRForCashless() != null ? dto.getSkipZMRForCashless() : false);
				return box;
			} else if("skipZMRForReimbursement".equals(propertyId)) {
				CheckBox box = new CheckBox();
				tableRow.put("skipZMRForReimbursement", box);
				box.setValue(dto.getSkipZMRForReimbursement() != null ? dto.getSkipZMRForReimbursement() : false);
				return box;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setEnabled(false);
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	public List<SkipZMRListenerTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<SkipZMRListenerTableDTO> itemIds = (List<SkipZMRListenerTableDTO>) this.table.getItemIds() ;
    	return itemIds;
    }
}
	
	
	
	
	
	
	
