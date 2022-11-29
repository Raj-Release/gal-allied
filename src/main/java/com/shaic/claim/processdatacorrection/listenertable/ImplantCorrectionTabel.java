package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.processdatacorrection.dto.ImplantCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.TreatingCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ImplantCorrectionTabel extends ViewComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3651791515051529314L;

	private Map<ImplantCorrectionDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ImplantCorrectionDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ImplantCorrectionDTO> data = new BeanItemContainer<ImplantCorrectionDTO>(ImplantCorrectionDTO.class);

	private Table table;

	private List<String> errorMessages;

	public Boolean isValueChanges =false;

	public String presenterString;
	
	private List<Long> editedKeys;

	public void init(String presenterString){

		this.presenterString = presenterString;
		this.errorMessages = new ArrayList<String>();
		this.editedKeys = new ArrayList<Long>();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		initTable(layout);

		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Implant Details", data);
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());

		table.addGeneratedColumn("edit", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button edit = new Button("");
				edit.setEnabled(true);
				edit.setIcon(FontAwesome.EDIT);
				edit.setData(itemId);
				edit.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;
					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						ImplantCorrectionDTO dto = (ImplantCorrectionDTO) currentItemId;
						if(!editedKeys.contains(dto.getKey())){
							editedKeys.add(dto.getKey());
							if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
								fireViewEvent(DataCorrectionPresenter.EDIT_IMPLANT_CORRECTION_VALUES,dto);	
							}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
								fireViewEvent(DataCorrectionPriorityPresenter.EDIT_IMPLANT_CORRECTION_VALUES_PRIORITY,dto);	
							}
						}else{
							SHAUtils.showMessageBoxWithCaption("Data already in correction", "Information");
						}
					} 
				});
				edit.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				return edit;
			}
		});
		table.setVisibleColumns(new Object[] { "implantName","implantType","implantCost","edit"});
		table.setColumnHeader("implantName", "Implant Name");
		table.setColumnHeader("implantType", "Implant Type");
		table.setColumnHeader("implantCost", "Implant Cost");

		table.setColumnAlignment("implantName",Align.CENTER);
		table.setColumnAlignment("implantType",Align.CENTER);
		table.setColumnAlignment("implantCost",Align.CENTER);
		table.setColumnAlignment("edit",Align.CENTER);

		table.setEditable(true);

		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			ImplantCorrectionDTO implantCorrectionDTO = (ImplantCorrectionDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(implantCorrectionDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(implantCorrectionDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(implantCorrectionDTO);
			}

			if("implantName".equals(propertyId)){
				TextArea box = new TextArea();
				box.setWidth("70%");
				box.setHeight("21px");
				box.setNullRepresentation("");
				box.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(box,null,getUI(),SHAConstants.IMPLANT_NAME);
				box.setMaxLength(100);
				tableRow.put("implantName", box);
				box.setData(implantCorrectionDTO);
				if(implantCorrectionDTO.getImplantName() != null){
					box.setValue(implantCorrectionDTO.getImplantName());
				}
				box.setReadOnly(true);				
				return box;
			}else if("implantType".equals(propertyId)){
				TextArea box = new TextArea();
				box.setWidth("70%");
				box.setHeight("21px");
				box.setNullRepresentation("");
				box.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
				SHAUtils.handleTextAreaPopupDetails(box,null,getUI(),SHAConstants.IMPLANT_TYPE);
				box.setMaxLength(100);
				tableRow.put("implantType", box);
				box.setData(implantCorrectionDTO);
				if(implantCorrectionDTO.getImplantType() != null){
					box.setValue(implantCorrectionDTO.getImplantType());
				}
				box.setReadOnly(true);				
				return box;
			}else if("implantCost".equals(propertyId)){
				TextField box = new TextField();
				box.setWidth("70%");
				box.setNullRepresentation("");
				tableRow.put("implantCost", box);
				box.setData(implantCorrectionDTO);
				if(implantCorrectionDTO.getImplantCost() != null){
					box.setValue(implantCorrectionDTO.getImplantCost().toString());
				}
				box.setReadOnly(true);				
				return box;
			} else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public List<ImplantCorrectionDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ImplantCorrectionDTO> itemIds = (List<ImplantCorrectionDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(ImplantCorrectionDTO implantCorrectionDTO) {
		data.addItem(implantCorrectionDTO);
	}

	public void addBeansToList(List<ImplantCorrectionDTO> implantCorrectionDTOs){

		for(ImplantCorrectionDTO correctionDTO: implantCorrectionDTOs){
			addBeanToList(correctionDTO);
		}
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}

	public void removeImplantEdited(Long Key){
		if(Key !=null && editedKeys !=null){	
			editedKeys.remove(Key);
		}
	}
	
	public void clearObject() {
		clearDataCorrectionTableItem(tableItem);
		data = null;
		errorMessages = null;
		presenterString = null;
		editedKeys = null;
	}
	
	private void clearDataCorrectionTableItem(Map<ImplantCorrectionDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
		
	    	Iterator<Entry<ImplantCorrectionDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
	    	//referenceData.clear();
	    	try{
		        while (iterator.hasNext()) {
		            Map.Entry pair = (Map.Entry)iterator.next();
		            Object object = pair.getValue();
		            object = null;
		            pair = null;	
		        }
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	       referenceData.clear();
	       referenceData = null;
		}
		
		}
}
