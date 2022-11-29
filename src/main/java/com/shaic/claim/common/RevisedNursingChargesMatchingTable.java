package com.shaic.claim.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class RevisedNursingChargesMatchingTable extends ViewComponent {

	private static final long serialVersionUID = 2524261416799886411L;
	
	private Map<NursingChargesMatchingDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<NursingChargesMatchingDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<NursingChargesMatchingDTO> data = new BeanItemContainer<NursingChargesMatchingDTO>(NursingChargesMatchingDTO.class);

	private Table table;

	private Button btnAdd;
	
	//private Map<String, Object> referenceData;

	//private List<String> errorMessages;
	
	//private static Validator validator;
	
	private String presenterString;
	
	private RoomRentMatchingDTO roomRentDTO;
	
	public Button saveAndCompleteButton;
	
	public Map<Long, Integer> allocatedValues = new HashMap<Long, Integer>();
	
	public void init(RoomRentMatchingDTO dto, ConfirmDialog dialog ) {
			this.roomRentDTO = dto;
		//public void init(Long hospitalKey, String presenterString,List<DiagnosisDetailsTableDTO> diagnosisList) {
			/*ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();*/
			//this.errorMessages = new ArrayList<String>();
			
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			
			initTable(layout);
			table.setWidth("100%");
			//table.setHeight("30%");
			/**
			 * Height is set for table visiblity.
			 * */
			table.setHeight("160px");
			table.setPageLength(table.getItemIds().size());
			table.setFooterVisible(true);
//			addListener();
			
			layout.addComponent(table);
			saveAndCompleteButton = new Button("Save & Completed Allocation");
			saveAndCompleteButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
			addListener(dialog);
			layout.addComponent(saveAndCompleteButton);
			layout.setSpacing(true);

			setCompositionRoot(layout);
		}
	
	private void addListener(final ConfirmDialog dialog ) {
		saveAndCompleteButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 7397593762586516039L;

			@Override
			public void buttonClick(ClickEvent event) {
				Integer calculateTotal = calculateTotal();
				dialog.close();
				List<NursingChargesMatchingDTO> itemIds = (List<NursingChargesMatchingDTO>) table.getItemIds();
				
				roomRentDTO.setNursingChargesDTOList(itemIds);
//				roomRentDTO.setAllocatedValues(allocatedValues);
				roomRentDTO.setStatus(true);
			}
		});
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Nursing Charges List", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		
		table.setVisibleColumns(new Object[] { "itemName", "billNumber", "claimedNoOfDays", "perDayAmount", "mappingRoomRentKeys" });

		table.setColumnHeader("itemName", "Item Name");
		table.setColumnHeader("billNumber", "Bill No");
		table.setColumnHeader("claimedNoOfDays", "No of days Claimed");
		table.setColumnHeader("perDayAmount", "Nursing Per Day Amt");
		table.setColumnHeader("allocatedClaimedNoOfDays", "Nursing Days Allocated");
		table.setColumnHeader("unAllocatedDays", "Nursing Days Unallocated");
		table.setColumnHeader("mappingRoomRentKeys", "Map to Room Days");
		
		table.setEditable(true);
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			NursingChargesMatchingDTO nursingChargeDTO = (NursingChargesMatchingDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			
			if (tableItem.get(nursingChargeDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(nursingChargeDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(nursingChargeDTO);
			}
			if("mappingRoomRentKeys".equals(propertyId)) {
				GComboBox field = new GComboBox();
				tableRow.put("mappingRoomRentKeys", field);
				field.setWidth("100px");
//				field.setNullSelectionAllowed(false);
				field.setData(nursingChargeDTO);
				addDropdownValues(nursingChargeDTO, field);
				addMapToRoomDaysListener(field);
				return field;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if(field instanceof TextField) {
					field.setReadOnly(true);
					field.setWidth("60px");
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
				return field;
			}
				
				
		}
	}
	
	
	private void addDropdownValues(
			NursingChargesMatchingDTO nursingChargeDTO, ComboBox field) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> values = new BeanItemContainer<SelectValue>(SelectValue.class);
		values.addAll(nursingChargeDTO.getListValues());
		field.setContainerDataSource(values);
		field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		field.setItemCaptionPropertyId("value");
	}
	
	@SuppressWarnings("unchecked")
	 public List<NursingChargesMatchingDTO> getValues() {
		List<NursingChargesMatchingDTO> itemIds = (List<NursingChargesMatchingDTO>) this.table.getItemIds() ;
    	return itemIds;
	 }
	
	public void addBeanToList(NursingChargesMatchingDTO nursingChargeDTO) {
		data.addItem(nursingChargeDTO);
	}
	
	@SuppressWarnings("unused")
	private void addMapToRoomDaysListener(ComboBox field) {
		if (field != null) {
			field.addValueChangeListener(new ValueChangeListener() {
				
				private static final long serialVersionUID = -1985337341257539699L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					ComboBox comboBox = (ComboBox) event.getProperty();
					 NursingChargesMatchingDTO dto = (NursingChargesMatchingDTO) comboBox.getData();
					 List<NursingChargesMatchingDTO> values = getValues();
					 if(!checkSelectedNursingCharges(values, dto)) {
						 comboBox.setValue(null);
					 }
				}

			
			});
		}
	}
	
	
	private Boolean checkSelectedNursingCharges(List<NursingChargesMatchingDTO> values, NursingChargesMatchingDTO currentDTO) {
		int i = 0;
		if(currentDTO.getMappingRoomRentKeys() != null) {
			for(NursingChargesMatchingDTO nursingChargesMatchingDTO : values) {
				 if(!nursingChargesMatchingDTO.getId().equals(currentDTO.getId()) &&   nursingChargesMatchingDTO.getMappingRoomRentKeys() != null && nursingChargesMatchingDTO.getMappingRoomRentKeys().getId().equals(currentDTO.getMappingRoomRentKeys().getId())) {
					 showErrorPopup(new Label("Not allowed to select more than one nursing charges for corresponding Room rent."));
					 return false;
				 }
			}
		}
		 return true;
	}
	private void showErrorPopup(Label label) {
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(label);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	public Integer calculateTotal() {
		
		List<NursingChargesMatchingDTO> itemIconPropertyId = (List<NursingChargesMatchingDTO>) table.getItemIds();
		Integer allocatedDaysTotal = 0;
		Integer unAllocatedDaysTotal = 0;
		Integer mapToRoomDaysTotal = 0;
		
		for (NursingChargesMatchingDTO dto : itemIconPropertyId) {
			Integer allocatedDays = 0;
			if(null != dto.getAllocatedClaimedNoOfDays())
				allocatedDays = dto.getAllocatedClaimedNoOfDays().intValue();
			
		    allocatedDaysTotal += allocatedDays != null ? allocatedDays : 0;
			
		    Integer unallocatedDays = 0;
		    if(null != dto.getUnAllocatedDays())
		     unallocatedDays = dto.getUnAllocatedDays().intValue();
			
			unAllocatedDaysTotal +=  null != unallocatedDays ? unallocatedDays : 0;
			Integer mapToRoomDays = 0;
			if(null != dto.getMapToRoomDays())
				mapToRoomDays = dto.getMapToRoomDays().intValue();
			mapToRoomDaysTotal += null != mapToRoomDays ? mapToRoomDays : 0;
		}
		
		/*for (NursingChargesMatchingDTO dto : itemIconPropertyId) {
		    Integer allocatedDays = dto.getAllocatedClaimedNoOfDays();
		    allocatedDaysTotal += allocatedDays != null ? allocatedDays : 0;
			Integer unallocatedDays = dto.getUnAllocatedDays();
			unAllocatedDaysTotal +=  null != unallocatedDays ? unallocatedDays : 0;
			Integer mapToRoomDays = dto.getMapToRoomDays();
			mapToRoomDaysTotal += null != mapToRoomDays ? mapToRoomDays : 0;
		}*/
		
		table.setColumnFooter("allocatedClaimedNoOfDays", String.valueOf(allocatedDaysTotal));
		table.setColumnFooter("unAllocatedDays", String.valueOf(unAllocatedDaysTotal));
		table.setColumnFooter("mapToRoomDays", String.valueOf(mapToRoomDaysTotal));
		return mapToRoomDaysTotal;
	}
	

}
