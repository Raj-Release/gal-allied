package com.shaic.reimbursement.topup_policy_master.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;



















import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.claim.cpuautoallocation.SearchCpuAutoAllocationTableDTO;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TopUpPolicyMasterTable extends ViewComponent {
	

	
	

	private Table table;
	
	private Map<TopUpPolicyMasterTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<TopUpPolicyMasterTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<TopUpPolicyMasterTableDTO> data = new BeanItemContainer<TopUpPolicyMasterTableDTO>(TopUpPolicyMasterTableDTO.class);
	
	
	
	public Object[] VISIBLE_COLUMNS = new Object[] {"policyNo"};
	
	
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
		table = new Table("",data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "policyNo", "enable", "disable","remarks" });
		//table.setColumnWidth("policyNo", 500);
		
		table.setColumnHeader("policyNo", "Policy Number");
		table.setColumnHeader("enable", "Enable");
		table.setColumnHeader("disable", "Disable");
		table.setColumnHeader("remarks", "Remarks");
		
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		
	}
	
		
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			TopUpPolicyMasterTableDTO dto = (TopUpPolicyMasterTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(dto) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(dto, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(dto);
			}
			
			if("enable".equals(propertyId)) {
				CheckBox box = new CheckBox();
				box.setData(dto);
				tableRow.put("enable", box);
				addEnableListener(box);
				box.setValue(dto.getEnable() != null ? (dto.getEnable() ? true: false) : false);
				return box;
			} else if("disable".equals(propertyId)) {
				CheckBox box = new CheckBox();
				box.setData(dto);
				tableRow.put("disable", box);
				addDisableListener(box);
				if(dto.getDisable() == null && dto.getEnable() == null){
					box.setEnabled(Boolean.FALSE);
				}/*else{
					box.setEnabled(Boolean.TRUE);
				}*/
				box.setValue(dto.getDisable() != null ? (dto.getDisable() ? true : false ) : false);
				//box.setValue(dto.getDisable() != null ? ((dto.getDisable() && dto.getActiveStatus()==null) ? true : false ) : false);
				return box;
				
			}else if("remarks".equals(propertyId)) {
				TextArea textarea = new TextArea();
				textarea.setNullRepresentation("");
				TopUpPolicyMasterTableDTO topUpPolicyMasterTableDTO =  (TopUpPolicyMasterTableDTO) itemId;
				textarea.setData(dto);
				tableRow.put("remarks", textarea);
				textarea.setWidth("600px");
				
				textarea.setHeight("50px");
				textarea.setMaxLength(4000);
				textarea.setData(topUpPolicyMasterTableDTO);
				addListener(textarea);
				SHAUtils.handleShortcutForTopUpPolicy(textarea,null,getUI());
				textarea.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				return textarea;
			} 
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setEnabled(false);
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	public List<TopUpPolicyMasterTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<TopUpPolicyMasterTableDTO> itemIds = (List<TopUpPolicyMasterTableDTO>) this.table.getItemIds() ;
    	return itemIds;
    }


	public void addToList(TopUpPolicyMasterTableDTO idaTableDTO) {
		// TODO Auto-generated method stub
		data.addItem(idaTableDTO);
		
	}
	
	private void addDisableListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					boolean value = (Boolean) event.getProperty().getValue();
					TopUpPolicyMasterTableDTO tableDTO = (TopUpPolicyMasterTableDTO)chkBox.getData();
					
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					
					if(hashMap != null){
						CheckBox enableCheckBox = (CheckBox) hashMap.get("enable");
						
						if(value){
							if(enableCheckBox != null){
								enableCheckBox.setValue(Boolean.FALSE);
							}
							
						}else{
							if(enableCheckBox != null){
								enableCheckBox.setValue(Boolean.TRUE);
							}
						}
						
					}
					
					
					
				}
			}
		});
	}
	
	private void addEnableListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					boolean value = (Boolean) event.getProperty().getValue();
					TopUpPolicyMasterTableDTO tableDTO = (TopUpPolicyMasterTableDTO)chkBox.getData();
					
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					
					if(hashMap != null){
						CheckBox enableCheckBox = (CheckBox) hashMap.get("disable");
						
						if(value){
							if(enableCheckBox != null){
								if(enableCheckBox.isEnabled())
								enableCheckBox.setValue(Boolean.FALSE);
							}
							
						}else{
							if(enableCheckBox != null){
								if(enableCheckBox.isEnabled())
								enableCheckBox.setValue(Boolean.TRUE);
							}
						}
						
					}
					
					
					
				}
			}
		});
	}

	private void addListener(final TextArea txtField)
	{	
		txtField
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					String value = (String) event.getProperty().getValue();
					TopUpPolicyMasterTableDTO tableDTO = (TopUpPolicyMasterTableDTO)txtField.getData();
					tableDTO.setRemarks(value);
				}
			}
		});
	}
	

}
