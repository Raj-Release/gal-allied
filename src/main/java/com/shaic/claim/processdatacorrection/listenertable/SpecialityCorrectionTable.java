package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.listenerTables.AddProcedurePopup;
import com.shaic.claim.processdatacorrection.dto.ProcedureCorrectionDTO;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.shaic.domain.MasterService;
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
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class SpecialityCorrectionTable extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1910148376564000628L;

	private Map<SpecialityCorrectionDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SpecialityCorrectionDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<SpecialityCorrectionDTO> data = new BeanItemContainer<SpecialityCorrectionDTO>(SpecialityCorrectionDTO.class);

	private Table table;

	private Map<String, Object> referenceData;

	private List<String> errorMessages;
	
	public Boolean isValueChanges =false;
	
	private String presenterString;
	
	private List<Long> editedKeys;
	
	public void init(String presenterString){
		
		this.errorMessages = new ArrayList<String>();
		this.presenterString = presenterString;
		this.editedKeys = new ArrayList<Long>();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		initTable(layout);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Speciality", data);
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
						SpecialityCorrectionDTO dto = (SpecialityCorrectionDTO) currentItemId;
						if(!editedKeys.contains(dto.getKey())){
							editedKeys.add(dto.getKey());
							if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
								fireViewEvent(DataCorrectionPresenter.EDIT_CORRECTION_SPECIALITY_VALUES,dto);	
							}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
								fireViewEvent(DataCorrectionHistoricalPresenter.EDIT_CORRECTION_SPECIALITY_VALUES_HIST,dto);	
							}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
								fireViewEvent(DataCorrectionPriorityPresenter.EDIT_CORRECTION_SPECIALITY_VALUES_PRIORITY,dto);	
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
		
		if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
			table.setVisibleColumns(new Object[] { "specialityType","procedure","remarks","edit"});
			table.setColumnHeader("procedure", "Procedure");	
			table.setColumnAlignment("procedure",Align.CENTER);
		}else{
			table.setVisibleColumns(new Object[] { "specialityType","remarks","edit"});
		}
		
		table.setColumnHeader("specialityType", "Speciality");	
		table.setColumnHeader("remarks", "Remarks");
		table.setColumnHeader("edit", "");
		
		table.setColumnAlignment("specialityType",Align.CENTER);
		
		table.setColumnAlignment("remarks",Align.CENTER);
		table.setColumnAlignment("edit",Align.CENTER);
		
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			SpecialityCorrectionDTO specialityDTO = (SpecialityCorrectionDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(specialityDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(specialityDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(specialityDTO);
			}

			if("specialityType".equals(propertyId)){
				GComboBox box = new GComboBox();
				addDataSource(box,specialityDTO,"SpecialityType");
				box.setWidth("60%");
				if(specialityDTO.getSpecialityType() != null){
					box.setValue(specialityDTO.getSpecialityType());
				}
				box.setEnabled(false);
				tableRow.put("specialityType", box);
				box.setData(specialityDTO);
				return box;
			} else if("procedure".equals(propertyId)){
				GComboBox box = new GComboBox();
				addDataSource(box,specialityDTO,"Procedure");
				box.setWidth("60%");			
				if(specialityDTO.getProcedure() != null){
					box.setValue(specialityDTO.getProcedure());
				}
				box.setEnabled(false);	
				tableRow.put("procedure", box);
				box.setData(specialityDTO);
				return box;
			} else if("remarks".equals(propertyId)){
				TextField box = new TextField();
				box.setNullRepresentation("");
				box.setWidth("60%");	
				tableRow.put("remarks", box);
				box.setData(specialityDTO);
				if(specialityDTO.getProcedure() != null){
					box.setValue(specialityDTO.getRemarks());
				}
				box.setEnabled(false);	

				return box;
			} else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}	

	@SuppressWarnings("deprecation")
	private void addDataSource(ComboBox box,SpecialityCorrectionDTO specialityDTO,String type) {
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(type.equals("SpecialityType")){
			selectValueContainer.addBean(specialityDTO.getSpecialityType());
		}else if(type.equals("Procedure")){
			selectValueContainer.addBean(specialityDTO.getProcedure());
		}
		box.setContainerDataSource(selectValueContainer);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}
	
	public List<SpecialityCorrectionDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<SpecialityCorrectionDTO> itemIds = (List<SpecialityCorrectionDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(SpecialityCorrectionDTO specialityDTO) {
		data.addItem(specialityDTO);
	}
	
	public void addBeansToList(List<SpecialityCorrectionDTO> specialityCorrectionDTOs){
		
		for(SpecialityCorrectionDTO specialityCorrectionDTO: specialityCorrectionDTOs){
			addBeanToList(specialityCorrectionDTO);
		}
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	public void removeSpecialityEdited(Long Key){
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
	
	private void clearDataCorrectionTableItem(Map<SpecialityCorrectionDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
		
	    	Iterator<Entry<SpecialityCorrectionDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
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
