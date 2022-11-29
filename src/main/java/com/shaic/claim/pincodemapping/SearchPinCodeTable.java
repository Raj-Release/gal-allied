package com.shaic.claim.pincodemapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.PagerListener;
import com.shaic.arch.table.PagerUI;
import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SearchPinCodeTable extends ViewComponent {
	
	private Table table;
	private String presenterString;
	private Map<SearchPinCodeTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SearchPinCodeTableDTO, HashMap<String, AbstractField<?>>>();
	private PagerUI pageUI;
	private FormLayout fLayout;
	private Searchable searchable;
	
	public 
	
	/***
	 * Bean object fetch from db
	 */
	BeanItemContainer<SearchPinCodeTableDTO> data = new BeanItemContainer<SearchPinCodeTableDTO>(SearchPinCodeTableDTO.class);
	public List<SearchPinCodeTableDTO> tableDataList = new ArrayList();
	public List<String> tableDataKeyList = new ArrayList();
	private VerticalLayout tableLayout = null;
	
	public void init(String presenterString,Boolean showPagerFlag) {
		
		this.presenterString = presenterString;
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		pageUI = new PagerUI();
		
		table.sort(new  Object[] { "pinCode" }, new boolean[] { false });
		tableLayout = new VerticalLayout();
		fLayout = new FormLayout();
		
		// VerticalLayout layout = new VerticalLayout();
		 if(showPagerFlag){
			 tableLayout.addComponent(pageUI);
		     pageUI.addListener(new PagerListener() {
				@Override
				public void changePage() {
					
					searchable.doSearch();
				}
			});
		 }
		
		 tableLayout.addComponent(fLayout);
		 tableLayout.addComponent(table);
		 
		setCompositionRoot(tableLayout);
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.removeAllItems();
			table.setVisibleColumns(new Object[] { "serialNo","chkSelect","pinCode","state","district", "city", "category", "tier","zone","cityClass" });
			table.setColumnHeader("serialNo", "S.No");
			table.setColumnHeader("chkSelect", "");
			table.setColumnHeader("pinCode", "Pin code");
			table.setColumnHeader("state", "State");
			table.setColumnHeader("district", "District");
			table.setColumnHeader("city", "City");
			table.setColumnHeader("category", "Category");
			table.setColumnHeader("tier", "Tier");
			table.setColumnHeader("zone", "Zone");
			table.setColumnHeader("cityClass", "Class");
		
		table.setEditable(true);	
		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		
		table.removeGeneratedColumn("viewHistory");
		table.addGeneratedColumn("viewHistory", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
					
				
				Button button = new Button("View History");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
					//To implement claims dms functionality.
						showHistoryView((SearchPinCodeTableDTO)itemId);
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		table.setColumnHeader("viewHistory", "View History");
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
 		table.setFooterVisible(false);
		table.sort(new  Object[] { "pinCode" }, new boolean[] { false });
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			SearchPinCodeTableDTO initiateDTO = (SearchPinCodeTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(initiateDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(initiateDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(initiateDTO);
			}
						
			if("chkSelect".equals(propertyId)) {
				
				CheckBox field = new CheckBox();
				field.setEnabled(true);
				field.setData(initiateDTO);
				tableRow.put("chkSelect", field);
				setCheckBoxValue(field, initiateDTO);
				addListener(field);
				return field;
			} 
			
			else if ("pinCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("pinCode", field);
				field.setReadOnly(true);
				return field;
			}
			else if ("state".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("state", field);
				field.setReadOnly(true);
				
				return field;
			}
			else if ("district".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("district", field);
				field.setReadOnly(true);
				
				return field;
			}
			else if ("city".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("city", field);
				field.setReadOnly(true);
				
				return field;
			}
			
			else if ("category".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("category", field);
				field.setReadOnly(true);
				
				return field;
			}
			
			else if ("tier".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("tier", field);
				field.setReadOnly(true);
				
				return field;
			}
			
			else if ("zone".equals(propertyId)) {
				GComboBox box = new GComboBox();
				tableRow.put("zone", box);
				setZoneValues(box, initiateDTO);
				addZoneListener(box);
				box.setData(initiateDTO);
				box.setReadOnly(true);
				return box;
			}
			else if ("cityClass".equals(propertyId)) {
				GComboBox box = new GComboBox();
				tableRow.put("cityClass", box);
				setClassValues(box, initiateDTO);
				addClassListener(box);
				box.setData(initiateDTO);
				box.setReadOnly(true);
				return box;
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField){
					field.setWidth("100%");
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setReadOnly(true);
					((TextField) field).setNullRepresentation("");
					
				}
				return field;
			}
		}
	}
	
	public void showHistoryView(SearchPinCodeTableDTO tableDTO)
	{
		fireViewEvent(SearchPinCodePresenter.SHOW_PINCODE_HISTORY_VIEW,tableDTO);
	}
	
	private void addListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					boolean value = (Boolean) event.getProperty().getValue();
					SearchPinCodeTableDTO tableDTO = (SearchPinCodeTableDTO)chkBox.getData();
					
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					
					if(hashMap != null){
						GComboBox zoneCombo = (GComboBox) hashMap.get("zone");
						GComboBox classCombo = (GComboBox) hashMap.get("cityClass");
						
						if(value){
							tableDTO.setCheckBoxStatus("true");
							if(zoneCombo != null){
								zoneCombo.setReadOnly(false);
							}
							if(classCombo != null){
								classCombo.setReadOnly(false);
							}
							
						}else{
							
							tableDTO.setCheckBoxStatus("false");
							if(zoneCombo != null){
								zoneCombo.setReadOnly(true);
							}
							if(classCombo != null){
								classCombo.setReadOnly(true);
							}
						}
						
					}
					
					
					
				}
			}
		});
	}

		
	public void setFinalTableList(List<SearchPinCodeTableDTO> tableRows) {
		Boolean isListEmpty = false;
//		tableDataList = new ArrayList<CreateAndSearchLotTableDTO>();
	//	List<CreateAndSearchLotTableDTO> dtoList = new ArrayList<CreateAndSearchLotTableDTO>();
		if(null != tableRows && !tableRows.isEmpty())
		{
			for (SearchPinCodeTableDTO pinCodeTableDTO : tableRows) {
				
				/**
				 * When user tries to navigate from forward to previous page.
				 * already added records shouldn't be added to the tableDataList.
				 * Hence another list where keys are stored is used, where if a key is
				 * already existing in that list, then it won't get added in 
				 * the main list.This is done to avoid duplication.
				 * 
				 * */
				
				if(null != tableDataList && !tableDataList.isEmpty() && null != tableDataKeyList && !tableDataKeyList.isEmpty())
				{
					if(!tableDataKeyList.contains(pinCodeTableDTO.getPinCode()))
							{
								tableDataList.add(pinCodeTableDTO);
								tableDataKeyList.add(pinCodeTableDTO.getPinCode());
							}
				}
				else
				{
					isListEmpty = true;
					break;
				}
			}
			/**
			 * 
			 * When first page is painted, table data list would be empty
			 * and hence adding all the records and its keys to data list and
			 * key list
			 * 
			 * */
			if(isListEmpty)
			{
				for (SearchPinCodeTableDTO pinCodeTableDTO : tableRows) {
					tableDataList.add(pinCodeTableDTO);
					tableDataKeyList.add(pinCodeTableDTO.getPinCode());
				}
				
			}
			/*if(null != dtoList && !dtoList.isEmpty()){
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : dtoList) {
					tableDataList.add(createAndSearchLotTableDTO);
				}
			}*/
		}
		// TODO Auto-generated method stub
		
	}
	
	public List<SearchPinCodeTableDTO> getTableItems()
	{
		return tableDataList;
	}
	
	public void resetTableDataList()
	{
		if(null != tableDataList)
		{
			tableDataList.clear();
		}
		if(null != tableDataKeyList)
		{
			tableDataKeyList.clear();
		}
	}
	
	
	public void addBeanToList(SearchPinCodeTableDTO pinCodeDTO) {
	    	data.addBean(pinCodeDTO);
	 }
	 
	 public void addList(List<SearchPinCodeTableDTO> pinCodeDTOList) {
		 for (SearchPinCodeTableDTO pinCodeDto : pinCodeDTOList) {
			 data.addBean(pinCodeDto);
		 }
	 }
	 
	 @SuppressWarnings("unchecked")
	public List<SearchPinCodeTableDTO> getValues() {
		 @SuppressWarnings("unchecked")
			List<SearchPinCodeTableDTO> itemIds = (List<SearchPinCodeTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
    	
	}

	public void setTableList(List<SearchPinCodeTableDTO> tableItems) {
		table.removeAllItems();
		// List<CreateAndSearchLotTableDTO> tableItems =
		// tableRows.getPageItems();
		if (null != tableItems && !tableItems.isEmpty()) {
			for (SearchPinCodeTableDTO pinCodeTableDTO : tableItems) {

				table.addItem(pinCodeTableDTO);
			}
		}
	}
	
	public Pageable getPageable()
	{
		return this.pageUI.getPageable();
	}
	
	public void setHasNextPage(boolean flag)
	{
		this.pageUI.hasMoreRecords(flag);
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

	
	
	public void resetTable()
	{
		if(null != table){
			List<SearchPinCodeTableDTO> tableList = (List<SearchPinCodeTableDTO>)table.getItemIds();
			if(null != tableList && !tableList.isEmpty())
			{
				table.removeAllItems();
				if(null != tableLayout) {
					//tableLayout.removeAllComponents();
				}
			}
		}
	//	this.pageUI.resetPage();
	}

	public void addSearchListener(Searchable searchable)
	{
		this.searchable = searchable;
	}
	
	private void setCheckBoxValue(CheckBox chkBox,
			SearchPinCodeTableDTO tableDTO) {
		if (null != tableDataList && !tableDataList.isEmpty()) {
			for (SearchPinCodeTableDTO dto : tableDataList) {
				if (dto.getPinCode().equalsIgnoreCase(tableDTO.getPinCode())) {
					if (null != dto.getChkSelect()) {
						chkBox.setValue(dto.getChkSelect());
					} else if (("true").equalsIgnoreCase(dto
							.getCheckBoxStatus())) {
						chkBox.setValue(true);
					} else if (("false").equalsIgnoreCase(dto
							.getCheckBoxStatus())) {
						chkBox.setValue(false);
					}
				}

			}
		}
	}
	
	private void setZoneValues(GComboBox comboBox,
			SearchPinCodeTableDTO tableDTO) {

		if (tableDTO != null && tableDTO.getZoneList() != null) {

			BeanItemContainer<SelectValue> selectValueContainer = tableDTO
					.getZoneList();
			
			comboBox.setContainerDataSource(selectValueContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
			
			if(selectValueContainer != null){
				List<SelectValue> selectValueList = selectValueContainer.getItemIds();
				if(selectValueList != null && tableDTO.getStrZone() != null){
					for (SelectValue selectValue : selectValueList) {
						if(tableDTO.getStrZone().equalsIgnoreCase(selectValue.getValue())){
							tableDTO.setZone(selectValue);
							comboBox.setValue(selectValue);
						}
					}
				}
			}
			
			

			/*if (null != tableDataList && !tableDataList.isEmpty()) {
				for (SearchPinCodeTableDTO dto : tableDataList) {
					if (dto.getPinCode().equalsIgnoreCase(tableDTO.getPinCode())) {
						if (null != dto.getStrZone()) {
							comboBox.setValue(dto.getZone());
						} 
						else if(dto.getZoneValue() != null && !dto.getZoneValue().isEmpty()){
							comboBox.setValue(dto.getZoneValue());
						}
						
					}

				}
			}*/

		}
	}
	
	private void addZoneListener(final GComboBox comboBox) {
		comboBox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty()
						&& null != event.getProperty().getValue()) {
					ComboBox component = (ComboBox) event.getProperty();

					SearchPinCodeTableDTO tableDTO = (SearchPinCodeTableDTO) comboBox
							.getData();

					if (null != tableDataList && !tableDataList.isEmpty()) {
						String value = null;
						for (SearchPinCodeTableDTO pinCodeDetailsDTO : tableDataList) {

							if (pinCodeDetailsDTO.getPinCode().equalsIgnoreCase(
									tableDTO.getPinCode())) {
								if (component != null && component.getValue() != null) {

									value = String.valueOf(component.getValue());
									
									if(value != null && !value.isEmpty()){
										pinCodeDetailsDTO.setZoneValue(value);
									}
								}
							}
						}
					}
				}
			}
		});
	}
	
	
	private void setClassValues(GComboBox comboBox,
			SearchPinCodeTableDTO tableDTO) {

		if (tableDTO != null && tableDTO.getCityClassList() != null) {

			BeanItemContainer<SelectValue> selectValueContainer = tableDTO
					.getCityClassList();
			
			comboBox.setContainerDataSource(selectValueContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
			
			if(selectValueContainer != null){
				List<SelectValue> selectValueList = selectValueContainer.getItemIds();
				if(selectValueList != null && tableDTO.getStrClass() != null){
					for (SelectValue selectValue : selectValueList) {
						if(tableDTO.getStrClass().equalsIgnoreCase(selectValue.getValue())){
							tableDTO.setCityClass(selectValue);
							comboBox.setValue(selectValue.getValue());
						}
					}
				}
			}
			
			/*if (null != tableDataList && !tableDataList.isEmpty()) {
				for (SearchPinCodeTableDTO dto : tableDataList) {
					if (dto.getPinCode().equalsIgnoreCase(tableDTO.getPinCode())) {
						if (null != dto.getCityClass()) {
							comboBox.setValue(dto.getCityClass());
						}else if(dto.getCityClassValue() != null && !dto.getCityClassValue().isEmpty()){
							comboBox.setValue(dto.getCityClassValue());
						}
					}

				}
			}*/

		}
	}
	
	private void addClassListener(final GComboBox comboBox) {
		comboBox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty()
						&& null != event.getProperty().getValue()) {
					ComboBox component = (ComboBox) event.getProperty();

					SearchPinCodeTableDTO tableDTO = (SearchPinCodeTableDTO) comboBox
							.getData();

					if (null != tableDataList && !tableDataList.isEmpty()) {
						String value = null;
						for (SearchPinCodeTableDTO cpuDetailsDTO : tableDataList) {

							if (cpuDetailsDTO.getPinCode().equalsIgnoreCase(
									tableDTO.getPinCode())) {
								if (component != null && component.getValue() != null) {
									value = String.valueOf(component.getValue());
									
									if(value != null && !value.isEmpty()){
										cpuDetailsDTO.setCityClassValue(value);
									}
								}
							}
						}
					}
				}
			}
		});
	}
	
}
