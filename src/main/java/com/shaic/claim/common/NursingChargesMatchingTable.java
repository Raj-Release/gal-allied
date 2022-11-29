package com.shaic.claim.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class NursingChargesMatchingTable extends ViewComponent {

	private static final long serialVersionUID = 2524261416799886411L;
	
	private Map<NursingChargesMatchingDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<NursingChargesMatchingDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<NursingChargesMatchingDTO> data = new BeanItemContainer<NursingChargesMatchingDTO>(NursingChargesMatchingDTO.class);

	private Table table;
	
	public TextField dummyField;

	//private Button btnAdd;
	
	//private Map<String, Object> referenceData;

	//private List<String> errorMessages;
	
	//private static Validator validator;
	
	//private String presenterString;
	
	private RoomRentMatchingDTO roomRentDTO;
	
	public Button saveAndCompleteButton;
	
	public Button cancelButton;
	
	public Map<Long, Integer> allocatedValues = new HashMap<Long, Integer>();
	
	public void init(RoomRentMatchingDTO dto, ConfirmDialog dialog) {
			this.roomRentDTO = dto;
			dummyField = new TextField();
		//public void init(Long hospitalKey, String presenterString,List<DiagnosisDetailsTableDTO> diagnosisList) {
			//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			//validator = factory.getValidator();
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
			
			cancelButton = new Button("Cancel");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			addListener(dialog);
			
			HorizontalLayout hLayout = new HorizontalLayout(saveAndCompleteButton, cancelButton);
			hLayout.setComponentAlignment(saveAndCompleteButton, Alignment.MIDDLE_CENTER);
			hLayout.setWidth("100%");
			hLayout.setMargin(true);
			layout.addComponent(hLayout);
			layout.setSpacing(true);

			setCompositionRoot(layout);
		}
	
	private void addListener(final ConfirmDialog dialog ) {
		saveAndCompleteButton.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 7397593762586516039L;

			@Override
			public void buttonClick(ClickEvent event) {
				Double calculateTotal = calculateTotal();
				if(!roomRentDTO.getAllowedNoOfDays().equals(calculateTotal)) {
//				if(false) {
					 String errorMessgage = "<b style = 'color:red'>Map to Room Days should be equal to Room Rent Allowed No of Days. </b>";
					 Label label = new Label(errorMessgage, ContentMode.HTML);
					 
					 showErrorPopup(label);
					 dummyField.setValue("true");
				} else {
					dialog.close();
					List<NursingChargesMatchingDTO> itemIds = (List<NursingChargesMatchingDTO>) table.getItemIds();
					for (NursingChargesMatchingDTO nursingDTO : itemIds) {
						allocatedValues.put(nursingDTO.getId(), nursingDTO.getMapToRoomDays() != null ? nursingDTO.getMapToRoomDays().intValue() : 0);
					}
					roomRentDTO.setNursingChargesDTOList(itemIds);
					roomRentDTO.setAllocatedValues(allocatedValues);
					roomRentDTO.setStatus(true);
					dummyField.setValue("true");
				}
			}
		});
		
		cancelButton.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				List<NursingChargesMatchingDTO> itemIds = (List<NursingChargesMatchingDTO>) table.getItemIds();
				for (NursingChargesMatchingDTO nursingDTO : itemIds) {
					nursingDTO.setMapToRoomDays(0d);
				}
				
				dialog.close();
				
			}
		});
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Nursing Charges List", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		
		table.setVisibleColumns(new Object[] { "itemName", "billNumber", "claimedNoOfDays", "perDayAmount", "allocatedClaimedNoOfDays", "unAllocatedDays", "mapToRoomDays" });

		table.setColumnHeader("itemName", "Item Name");
		table.setColumnHeader("billNumber", "Bill No");
		table.setColumnHeader("claimedNoOfDays", "No of days Claimed");
		table.setColumnHeader("perDayAmount", "Nursing Per Day Amt");
		table.setColumnHeader("allocatedClaimedNoOfDays", "Nursing Days Allocated");
		table.setColumnHeader("unAllocatedDays", "Nursing Days Unallocated");
		table.setColumnHeader("mapToRoomDays", "Map to Room Days");
		
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
			if("mapToRoomDays".equals(propertyId)) {
				TextField field = new TextField();
				tableRow.put("mapToRoomDays", field);
				field.setWidth("50px");
				field.setNullRepresentation("0");
				field.setData(nursingChargeDTO);
				addMapToRoomDaysListener(field);
				return field;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if(field instanceof TextField) {
					field.setReadOnly(true);
					field.setWidth("150px");
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
				return field;
			}
				
				
		}
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
	private void addMapToRoomDaysListener(TextField field) {
		if (field != null) {
			field.addValueChangeListener(new ValueChangeListener() {
				
				private static final long serialVersionUID = -1985337341257539699L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					 TextField txtField = (TextField) event.getProperty();
					 NursingChargesMatchingDTO dto = (NursingChargesMatchingDTO) txtField.getData();
					 if(dto.getUnAllocatedDays() != null && dto.getMapToRoomDays() != null && !(dto.getUnAllocatedDays() >= dto.getMapToRoomDays()) ) {
						 String errorMessgage = "<b style='color:red'>Map to Room Days should not exceed the No of Days UnAllocated Days.</b>";
						 Label label = new Label(errorMessgage, ContentMode.HTML);
						 showErrorPopup(label);
				    	 txtField.setValue("0");
					 }
				}
			});
			
			calculateTotal();
		}
	}
	
	private void showErrorPopup(Label label) {
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(label);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	public Double calculateTotal() {
		
		List<NursingChargesMatchingDTO> itemIconPropertyId = (List<NursingChargesMatchingDTO>) table.getItemIds();
		Double allocatedDaysTotal = 0d;
		Double unAllocatedDaysTotal = 0d;
		Double mapToRoomDaysTotal = 0d;
		for (NursingChargesMatchingDTO dto : itemIconPropertyId) {
			Double allocatedDays = 0d;
			if(null != dto.getAllocatedClaimedNoOfDays())
				allocatedDays = dto.getAllocatedClaimedNoOfDays();
			
		    allocatedDaysTotal += allocatedDays != null ? allocatedDays : 0;
			
		    Double unallocatedDays = 0d;
		    if(null != dto.getUnAllocatedDays())
		     unallocatedDays = dto.getUnAllocatedDays();
			
			unAllocatedDaysTotal +=  null != unallocatedDays ? unallocatedDays : 0;
			Double mapToRoomDays = 0d;
			if(null != dto.getMapToRoomDays())
				mapToRoomDays = dto.getMapToRoomDays();
			mapToRoomDaysTotal += null != mapToRoomDays ? mapToRoomDays : 0;
		}
		
		table.setColumnFooter("allocatedClaimedNoOfDays", String.valueOf(allocatedDaysTotal));
		table.setColumnFooter("unAllocatedDays", String.valueOf(unAllocatedDaysTotal));
		table.setColumnFooter("mapToRoomDays", String.valueOf(mapToRoomDaysTotal));
		return mapToRoomDaysTotal;
	}
	

}
