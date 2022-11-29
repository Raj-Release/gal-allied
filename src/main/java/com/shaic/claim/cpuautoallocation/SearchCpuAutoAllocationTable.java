package com.shaic.claim.cpuautoallocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.PagerListener;
import com.shaic.arch.table.PagerUI;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
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

public class SearchCpuAutoAllocationTable extends ViewComponent {
	
	private Table table;
	private String presenterString;
	private Map<SearchCpuAutoAllocationTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SearchCpuAutoAllocationTableDTO, HashMap<String, AbstractField<?>>>();
	private PagerUI pageUI;
	private FormLayout fLayout;
	private Searchable searchable;
	
	public 
	
	/***
	 * Bean object fetch from db
	 */
	BeanItemContainer<SearchCpuAutoAllocationTableDTO> data = new BeanItemContainer<SearchCpuAutoAllocationTableDTO>(SearchCpuAutoAllocationTableDTO.class);
	public HashMap<Long,Component> compMap = null;
	public List<SearchCpuAutoAllocationTableDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	private VerticalLayout tableLayout = null;
	
	public void init(String presenterString,Boolean showPagerFlag) {
		
		this.presenterString = presenterString;
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		pageUI = new PagerUI();
		
		table.sort(new  Object[] { "cpuCode" }, new boolean[] { false });
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
			table.setVisibleColumns(new Object[] { "serialNo","chkSelect","cpuCodestr","cpuName","minAmt", "maxAmt", "withinLimit", "limitCases","aboveLimit","corpOffice" });
			table.setColumnHeader("serialNo", "S.No");
			table.setColumnHeader("chkSelect", "");
			table.setColumnHeader("cpuCodestr", "CPU code");
			table.setColumnHeader("cpuName", "CPU Name");
			table.setColumnHeader("minAmt", "CPU Limit</br>Min Amount");
			table.setColumnHeader("maxAmt", "CPU Limit</br>Max Amount");
			table.setColumnHeader("withinLimit", "Auto Allocation</br>(Within Limit)");
			table.setColumnHeader("limitCases", "Processing of Above Limit Cases");
			table.setColumnHeader("aboveLimit", "Auto Allocation</br>(Above Limit)");
			table.setColumnHeader("corpOffice", "Processed at Corp Office");
		
		table.setEditable(true);	
		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
 		table.setFooterVisible(false);
		table.sort(new  Object[] { "cpuCodestr" }, new boolean[] { false });
	}
	
	public void manageListeners() {

		
	}
		
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			SearchCpuAutoAllocationTableDTO initiateDTO = (SearchCpuAutoAllocationTableDTO) itemId;
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
				//compMap.put(initiateDTO.getCpuCode(), field);
				return field;
			} 
			
			else if ("cpuCodestr".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("cpuCodestr", field);
				field.setReadOnly(true);
				
				return field;
			}
			else if ("cpuName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("cpuName", field);
				field.setReadOnly(true);

				return field;
			}
			else if ("minAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("minAmt", field);
				field.setReadOnly(true);
				return field;
			}
			else if ("maxAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(initiateDTO);
				tableRow.put("maxAmt", field);
				field.setReadOnly(true);
				return field;
			}
			else if ("withinLimit".equals(propertyId)) {
				GComboBox box = new GComboBox();
				tableRow.put("withinLimit", box);
				setWithinLimitValues(box, initiateDTO);
				addWithinLimitListener(box);
				box.setData(initiateDTO);
				box.setReadOnly(true);
				
				return box;
			}
			else if ("limitCases".equals(propertyId)) {
				GComboBox box = new GComboBox();
				tableRow.put("limitCases", box);
				setLimitCasesValues(box, initiateDTO);
				addLimitCasesListener(box);
				box.setData(initiateDTO);
				box.setReadOnly(true);
				
				return box;
			}
			else if ("aboveLimit".equals(propertyId)) {
				GComboBox box = new GComboBox();
				tableRow.put("aboveLimit", box);
				setAboveLimitValues(box, initiateDTO);
				addAboveLimitListener(box);
				box.setData(initiateDTO);
				box.setReadOnly(true);
				
				return box;
			}
			else if ("corpOffice".equals(propertyId)) {
				GComboBox box = new GComboBox();
				tableRow.put("corpOffice", box);
				setCorpOfficeValues(box, initiateDTO);
				addCorpOfficeListener(box);
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
					SearchCpuAutoAllocationTableDTO tableDTO = (SearchCpuAutoAllocationTableDTO)chkBox.getData();
					
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					
					if(hashMap != null){
						GComboBox withinLimitCombo = (GComboBox) hashMap.get("withinLimit");
						GComboBox limitCasesCombo = (GComboBox) hashMap.get("limitCases");
						GComboBox aboveLimitCombo = (GComboBox) hashMap.get("aboveLimit");
						GComboBox corpOfficeCombo = (GComboBox) hashMap.get("corpOffice");
						
						if(value){
							tableDTO.setCheckBoxStatus("true");
							if(withinLimitCombo != null){
								withinLimitCombo.setReadOnly(false);
							}
							if(limitCasesCombo != null){
								limitCasesCombo.setReadOnly(false);
							}
							if(aboveLimitCombo != null){
								aboveLimitCombo.setReadOnly(false);
							}
							if(corpOfficeCombo != null){
								corpOfficeCombo.setReadOnly(false);
							}
						}else{
							
							tableDTO.setCheckBoxStatus("false");
							if(withinLimitCombo != null){
								withinLimitCombo.setReadOnly(true);
							}
							if(limitCasesCombo != null){
								limitCasesCombo.setReadOnly(true);
							}
							if(aboveLimitCombo != null){
								aboveLimitCombo.setReadOnly(true);
							}
							if(corpOfficeCombo != null){
								corpOfficeCombo.setReadOnly(true);
							}
						}
						
					}
					
					
					
				}
			}
		});
	}

		
	public void setFinalTableList(List<SearchCpuAutoAllocationTableDTO> tableRows) {
		Boolean isListEmpty = false;
//		tableDataList = new ArrayList<CreateAndSearchLotTableDTO>();
	//	List<CreateAndSearchLotTableDTO> dtoList = new ArrayList<CreateAndSearchLotTableDTO>();
		if(null != tableRows && !tableRows.isEmpty())
		{
			for (SearchCpuAutoAllocationTableDTO createAndSearchLotTableDTO : tableRows) {
				
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
					if(!tableDataKeyList.contains(createAndSearchLotTableDTO.getCpuCode()))
							{
								tableDataList.add(createAndSearchLotTableDTO);
								tableDataKeyList.add(createAndSearchLotTableDTO.getCpuCode());
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
				for (SearchCpuAutoAllocationTableDTO createAndSearchLotTableDTO : tableRows) {
					tableDataList.add(createAndSearchLotTableDTO);
					tableDataKeyList.add(createAndSearchLotTableDTO.getCpuCode());
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
	
	public List<SearchCpuAutoAllocationTableDTO> getTableItems()
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
	
	
	public void addBeanToList(SearchCpuAutoAllocationTableDTO createLotDTO) {
	    	data.addBean(createLotDTO);
	 }
	 
	 public void addList(List<SearchCpuAutoAllocationTableDTO> createLotDTO) {
		 for (SearchCpuAutoAllocationTableDTO createandSearchLotDto : createLotDTO) {
			 data.addBean(createandSearchLotDto);
		 }
	 }
	 
	 @SuppressWarnings("unchecked")
	public List<SearchCpuAutoAllocationTableDTO> getValues() {
		 @SuppressWarnings("unchecked")
			List<SearchCpuAutoAllocationTableDTO> itemIds = (List<SearchCpuAutoAllocationTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
    	
	}

	public void setTableList(List<SearchCpuAutoAllocationTableDTO> tableItems) {
		table.removeAllItems();
		//List<CreateAndSearchLotTableDTO> tableItems = tableRows.getPageItems();
		if(null != tableItems && !tableItems.isEmpty())
		{
			for (SearchCpuAutoAllocationTableDTO createAndSearchLotTableDTO : tableItems) {
				
				table.addItem(createAndSearchLotTableDTO);
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
			List<SearchCpuAutoAllocationTableDTO> tableList = (List<SearchCpuAutoAllocationTableDTO>)table.getItemIds();
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
	
	private void setCheckBoxValue(CheckBox chkBox, SearchCpuAutoAllocationTableDTO tableDTO){
		 if(null != tableDataList && !tableDataList.isEmpty())
		  {
			  for (SearchCpuAutoAllocationTableDTO dto : tableDataList) {
				  if(dto.getCpuCode().equals(tableDTO.getCpuCode()))
				  {
					   if(null != dto.getChkSelect())
					  {
						  chkBox.setValue(dto.getChkSelect());
					  }
					   else if(("true").equalsIgnoreCase(dto.getCheckBoxStatus()))
					   {
						   chkBox.setValue(true);
					   }
					   else if(("false").equalsIgnoreCase(dto.getCheckBoxStatus()))
					   {
						   chkBox.setValue(false);
					   }
				  }
				
			}
		  }
	}
	
	private void setWithinLimitValues(GComboBox comboBox,
			SearchCpuAutoAllocationTableDTO tableDTO) {

		if (tableDTO != null && tableDTO.getWithinLimitList() != null) {

			BeanItemContainer<SelectValue> selectValueContainer = tableDTO
					.getWithinLimitList();
			/*List<SelectValue> selValueList = new ArrayList<SelectValue>();
			SelectValue sel1 = new SelectValue();
			sel1.setId(ReferenceTable.COMMONMASTER_YES);
			sel1.setValue(SHAConstants.YES);
			selValueList.add(sel1);

			SelectValue sel2 = new SelectValue();
			sel2.setId(ReferenceTable.COMMONMASTER_NO);
			sel2.setValue(SHAConstants.No);
			selValueList.add(sel2);

			selectValueContainer.addAll(selValueList);*/

			comboBox.setContainerDataSource(selectValueContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

			if (null != tableDataList && !tableDataList.isEmpty()) {
				for (SearchCpuAutoAllocationTableDTO dto : tableDataList) {
					if (dto.getCpuCode().equals(tableDTO.getCpuCode())) {
						if (null != dto.getWithinLimit()) {
							comboBox.setValue(dto.getWithinLimit().getValue());
						} 
						else if(dto.getWithinLimitValue() != null){
							if (dto.getWithinLimitValue().equals(
									ReferenceTable.COMMONMASTER_YES)) {
								comboBox.setValue(SHAConstants.YES);
							} else if (dto.getWithinLimitValue().equals(
									ReferenceTable.COMMONMASTER_NO)) {
								comboBox.setValue(SHAConstants.No);
							}
						}
						
					}

				}
			}

		}
	}
	
	private void addWithinLimitListener(final GComboBox comboBox) {
		comboBox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty()
						&& null != event.getProperty().getValue()) {
					ComboBox component = (ComboBox) event.getProperty();

					SearchCpuAutoAllocationTableDTO tableDTO = (SearchCpuAutoAllocationTableDTO) comboBox
							.getData();

					if (null != tableDataList && !tableDataList.isEmpty()) {
						for (SearchCpuAutoAllocationTableDTO cpuDetailsDTO : tableDataList) {

							if (cpuDetailsDTO.getCpuCode().equals(
									tableDTO.getCpuCode())) {
								if (component != null) {

									if (String.valueOf(component.getValue())
											.equalsIgnoreCase(SHAConstants.YES)) {
										cpuDetailsDTO
												.setWithinLimitValue(ReferenceTable.COMMONMASTER_YES);
									} else if (String.valueOf(
											component.getValue())
											.equalsIgnoreCase(SHAConstants.No)) {
										cpuDetailsDTO
												.setWithinLimitValue(ReferenceTable.COMMONMASTER_NO);
									}

								}
							}
						}
					}
				}
			}
		});
	}
	
	
	private void setAboveLimitValues(GComboBox comboBox,
			SearchCpuAutoAllocationTableDTO tableDTO) {

		if (tableDTO != null && tableDTO.getAboveLimitList() != null) {

			BeanItemContainer<SelectValue> selectValueContainer = tableDTO
					.getAboveLimitList();
			/*List<SelectValue> selValueList = new ArrayList<SelectValue>();
			SelectValue sel1 = new SelectValue();
			sel1.setId(ReferenceTable.COMMONMASTER_YES);
			sel1.setValue(SHAConstants.YES);
			selValueList.add(sel1);

			SelectValue sel2 = new SelectValue();
			sel2.setId(ReferenceTable.COMMONMASTER_NO);
			sel2.setValue(SHAConstants.No);
			selValueList.add(sel2);

			selectValueContainer.addAll(selValueList);*/

			comboBox.setContainerDataSource(selectValueContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

			if (null != tableDataList && !tableDataList.isEmpty()) {
				for (SearchCpuAutoAllocationTableDTO dto : tableDataList) {
					if (dto.getCpuCode().equals(tableDTO.getCpuCode())) {
						if (null != dto.getAboveLimit()) {
							comboBox.setValue(dto.getAboveLimit().getValue());
						}else if(dto.getAboveLimitValue() != null){
							if (dto.getAboveLimitValue().equals(
									ReferenceTable.COMMONMASTER_YES)) {
								comboBox.setValue(SHAConstants.YES);
							} else if (dto.getAboveLimitValue().equals(
									ReferenceTable.COMMONMASTER_NO)) {
								comboBox.setValue(SHAConstants.No);
							}
						}
					}

				}
			}

		}
	}
	
	private void addAboveLimitListener(final GComboBox comboBox) {
		comboBox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty()
						&& null != event.getProperty().getValue()) {
					ComboBox component = (ComboBox) event.getProperty();

					SearchCpuAutoAllocationTableDTO tableDTO = (SearchCpuAutoAllocationTableDTO) comboBox
							.getData();

					if (null != tableDataList && !tableDataList.isEmpty()) {
						for (SearchCpuAutoAllocationTableDTO cpuDetailsDTO : tableDataList) {

							if (cpuDetailsDTO.getCpuCode().equals(
									tableDTO.getCpuCode())) {
								if (component != null) {

									if (String.valueOf(component.getValue())
											.equalsIgnoreCase(SHAConstants.YES)) {
										cpuDetailsDTO
												.setAboveLimitValue(ReferenceTable.COMMONMASTER_YES);
									} else if (String.valueOf(
											component.getValue())
											.equalsIgnoreCase(SHAConstants.No)) {
										cpuDetailsDTO
												.setAboveLimitValue(ReferenceTable.COMMONMASTER_NO);
									}

								}
							}
						}
					}
				}
			}
		});
	}
	
	
	private void setCorpOfficeValues(GComboBox comboBox,
			SearchCpuAutoAllocationTableDTO tableDTO) {

		if (tableDTO != null && tableDTO.getCorpOfficeList() != null) {

			BeanItemContainer<SelectValue> selectValueContainer = tableDTO
					.getCorpOfficeList();
			/*List<SelectValue> selValueList = new ArrayList<SelectValue>();
			SelectValue sel1 = new SelectValue();
			sel1.setId(ReferenceTable.COMMONMASTER_YES);
			sel1.setValue(SHAConstants.YES);
			selValueList.add(sel1);

			SelectValue sel2 = new SelectValue();
			sel2.setId(ReferenceTable.COMMONMASTER_NO);
			sel2.setValue(SHAConstants.No);
			selValueList.add(sel2);

			selectValueContainer.addAll(selValueList);*/

			comboBox.setContainerDataSource(selectValueContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

			if (null != tableDataList && !tableDataList.isEmpty()) {
				for (SearchCpuAutoAllocationTableDTO dto : tableDataList) {
					if (dto.getCpuCode().equals(tableDTO.getCpuCode())) {
						if (null != dto.getCorpOffice()) {
							comboBox.setValue(dto.getCorpOffice().getValue());
						} else if(dto.getCorpOfficeValue() != null){
							if (dto.getCorpOfficeValue().equals(
									ReferenceTable.COMMONMASTER_YES)) {
								comboBox.setValue(SHAConstants.YES);
							} else if (dto.getCorpOfficeValue().equals(
									ReferenceTable.COMMONMASTER_NO)) {
								comboBox.setValue(SHAConstants.No);
							}
						}
					}

				}
			}

		}
	}
	
	private void addCorpOfficeListener(final GComboBox comboBox) {
		comboBox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty()
						&& null != event.getProperty().getValue()) {
					ComboBox component = (ComboBox) event.getProperty();

					SearchCpuAutoAllocationTableDTO tableDTO = (SearchCpuAutoAllocationTableDTO) comboBox
							.getData();

					if (null != tableDataList && !tableDataList.isEmpty()) {
						for (SearchCpuAutoAllocationTableDTO cpuDetailsDTO : tableDataList) {

							if (cpuDetailsDTO.getCpuCode().equals(
									tableDTO.getCpuCode())) {
								if (component != null) {

									if (String.valueOf(component.getValue())
											.equalsIgnoreCase(SHAConstants.YES)) {
										cpuDetailsDTO
												.setCorpOfficeValue(ReferenceTable.COMMONMASTER_YES);
									} else if (String.valueOf(
											component.getValue())
											.equalsIgnoreCase(SHAConstants.No)) {
										cpuDetailsDTO
												.setCorpOfficeValue(ReferenceTable.COMMONMASTER_NO);
									}

								}
							}
						}
					}
				}
			}
		});
	}
	
	
	private void setLimitCasesValues(GComboBox comboBox,
			SearchCpuAutoAllocationTableDTO tableDTO) {

		if (tableDTO != null && tableDTO.getLimitCasesList() != null) {
			BeanItemContainer<SelectValue> selectValueContainer = tableDTO.getLimitCasesList();
			/*List<SelectValue> selValueList = new ArrayList<SelectValue>();
			SelectValue sel1 = new SelectValue();
			sel1.setId(ReferenceTable.CPU_AUTO_CAPA);
			sel1.setValue(SHAConstants.CPU_ALLOCATION_LIMIT_CASE_CAPA);
			selValueList.add(sel1);

			SelectValue sel2 = new SelectValue();
			sel2.setId(ReferenceTable.CPU_AUTO_CP);
			sel2.setValue(SHAConstants.CPU_ALLOCATION_LIMIT_CASE_CP);
			selValueList.add(sel2);
			
			selectValueContainer.addAll(selValueList);*/
			
			
			comboBox.setContainerDataSource(selectValueContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

			if (null != tableDataList && !tableDataList.isEmpty()) {
				for (SearchCpuAutoAllocationTableDTO dto : tableDataList) {
					if (dto.getCpuCode().equals(tableDTO.getCpuCode())) {
						if (null != dto.getLimitCases()) {
							comboBox.setValue(dto.getLimitCases().getValue());
						}else if(dto.getLimitCasesValue() != null && !dto.getLimitCasesValue().isEmpty()){
							comboBox.setValue(dto.getLimitCasesValue());
						}
					}
				}
			}

		}
	}
	
	private void addLimitCasesListener(final GComboBox comboBox) {
		comboBox.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty()
						&& null != event.getProperty().getValue()) {
					ComboBox component = (ComboBox) event.getProperty();

					SearchCpuAutoAllocationTableDTO tableDTO = (SearchCpuAutoAllocationTableDTO) comboBox
							.getData();

					if (null != tableDataList && !tableDataList.isEmpty()) {
						for (SearchCpuAutoAllocationTableDTO cpuDetailsDTO : tableDataList) {

							if (cpuDetailsDTO.getCpuCode().equals(
									tableDTO.getCpuCode())) {
								if (component != null) {

									String value = String.valueOf(component.getValue());
									if(value != null && !value.isEmpty()){
										cpuDetailsDTO.setLimitCasesValue(value);
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
