package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.processdatacorrection.dto.ProcedureCorrectionDTO;
import com.shaic.claim.processdatacorrection.listenertable.SpecialityCorrectionTable.ImmediateFieldFactory;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Table.Align;

public class ProcedureCorrectionTable  extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8224234108641261062L;

	private Map<ProcedureCorrectionDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ProcedureCorrectionDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ProcedureCorrectionDTO> data = new BeanItemContainer<ProcedureCorrectionDTO>(ProcedureCorrectionDTO.class);

	private Table table;

	private Map<String, Object> referenceData;

	private List<String> errorMessages;
	
	private List<Long> editedKeys;
	
	private String presenterString;

	public void init(String presenterString){
		
		this.presenterString = presenterString;
		this.errorMessages = new ArrayList<String>();
		this.editedKeys = new ArrayList<Long>();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		initTable(layout);

		table.setWidth("70%");
		table.setPageLength(table.getItemIds().size());
		
		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Procedure List", data);
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
						ProcedureCorrectionDTO dto = (ProcedureCorrectionDTO) currentItemId;
						if(!editedKeys.contains(dto.getKey())){
							editedKeys.add(dto.getKey());
							if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
								fireViewEvent(DataCorrectionPresenter.EDIT_PROCEDURE_CORRECTION_VALUES,dto);	
							}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
								fireViewEvent(DataCorrectionHistoricalPresenter.EDIT_PROCEDURE_CORRECTION_VALUES_HIST,dto);	
							}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
								fireViewEvent(DataCorrectionPriorityPresenter.EDIT_PROCEDURE_CORRECTION_VALUES_PRIORITY,dto);	
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
		
		table.setVisibleColumns(new Object[] {"procedureName","newProcedureName","procedureCode","speciality","edit"});
		table.setColumnHeader("procedureName", "Procedure Name");
		table.setColumnHeader("newProcedureName", "New Procedure Name");
		table.setColumnHeader("procedureCode", "Procedure Code");
		table.setColumnHeader("speciality", "Speciality Name");
		table.setColumnHeader("edit", "");
		table.setEditable(true);
		
		table.setColumnAlignment("procedureName",Align.CENTER);
		table.setColumnAlignment("procedureCode",Align.CENTER);
		table.setColumnAlignment("newProcedureName",Align.CENTER);
		table.setColumnAlignment("speciality",Align.CENTER);
		table.setColumnAlignment("edit",Align.CENTER);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			ProcedureCorrectionDTO procedureCorrectionDTO = (ProcedureCorrectionDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(procedureCorrectionDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(procedureCorrectionDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(procedureCorrectionDTO);
			}

			if("procedureName".equals(propertyId)){
				GComboBox box = new GComboBox();
				box.setEnabled(false);
				addProcedureNameValues(box);
				tableRow.put("procedureName", box);
				box.setData(procedureCorrectionDTO);
				if(procedureCorrectionDTO.getProcedureName() != null){
					box.setValue(procedureCorrectionDTO.getProcedureName());
				}
				return box;
			} else if("procedureCode".equals(propertyId)){
				GComboBox box = new GComboBox();
				box.setEnabled(false);
				addProcedureCodeValues(box);
				tableRow.put("procedureCode", box);
				box.setData(procedureCorrectionDTO);
				if(procedureCorrectionDTO.getProcedureName() != null){
					box.setValue(procedureCorrectionDTO.getProcedureCode());
				}
				return box;
			}  else if ("newProcedureName".equals(propertyId)) {
				TextField field = new TextField();
				field.setMaxLength(100);
				field.setEnabled(false);
				field.setNullRepresentation("");
				field.setWidth("150px");
				field.setReadOnly(false);
				field.setData(procedureCorrectionDTO);
				tableRow.put("newProcedureName", field);
				return field;
			}else if ("speciality".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(false);
				addProcedureSpecialityValues(box);
				box.setData(procedureCorrectionDTO);
				tableRow.put("speciality", box);
				if(procedureCorrectionDTO.getSpeciality() != null){
					box.setValue(procedureCorrectionDTO.getSpeciality());
				}
//				addProcedureCodeListener(box, procedureName);
				return box;
			}
			else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addProcedureNameValues(ComboBox box) {
		
		BeanItemContainer<SelectValue> procedure = (BeanItemContainer<SelectValue>) referenceData.get("procedureName");
		box.setContainerDataSource(procedure);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}
	
	private void addProcedureCodeValues(ComboBox box) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> procedureCode = (BeanItemContainer<SelectValue>) referenceData.get("procedureCode");
		box.setContainerDataSource(procedureCode);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
		
	}
	
	public List<ProcedureCorrectionDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ProcedureCorrectionDTO> itemIds = (List<ProcedureCorrectionDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(ProcedureCorrectionDTO procedureCorrectionDTO) {
		data.addItem(procedureCorrectionDTO);
	}
	
	public void addBeansToList(List<ProcedureCorrectionDTO> procedureCorrectionDTOs){

		for(ProcedureCorrectionDTO procedureCorrectionDTO: procedureCorrectionDTOs){
			addBeanToList(procedureCorrectionDTO);
		}
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	public void removeProcedureEdited(Long Key){
		if(Key !=null && editedKeys !=null){	
			editedKeys.remove(Key);
		}
	}
	
	public void clearObject() {
		clearDataCorrectionTableItem(tableItem);
		SHAUtils.setClearReferenceData(referenceData);
		data = null;
		errorMessages = null;
		presenterString = null;
		editedKeys = null;
	}
	
	private void clearDataCorrectionTableItem(Map<ProcedureCorrectionDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
		
	    	Iterator<Entry<ProcedureCorrectionDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
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
	private void addProcedureSpecialityValues(ComboBox box) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> specialityCode = (BeanItemContainer<SelectValue>) referenceData
				.get("surgicalSpeciality");
		box.setContainerDataSource(specialityCode);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
		
	}
}
