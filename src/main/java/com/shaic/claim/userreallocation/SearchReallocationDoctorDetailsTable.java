package com.shaic.claim.userreallocation;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class SearchReallocationDoctorDetailsTable extends GBaseTable<SearchReallocationDoctorDetailsTableDTO> {

	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","chkSelect","empId","doctorName","loginDate","status","assignedCount","completedCount","pendingCount"};
	
	public List<SearchReallocationDoctorDetailsTableDTO> tableDataList = new ArrayList();
	public List<String> tableDataIdList = new ArrayList();
	
	public WeakHashMap<String,Component> compMap = null;
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		table.setContainerDataSource(new BeanItemContainer<SearchReallocationDoctorDetailsTableDTO>(SearchReallocationDoctorDetailsTableDTO.class));
		generatecolumns();
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public void tableSelectHandler(SearchReallocationDoctorDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-doctor-re-allocation-";
	}

	private void generatecolumns(){
		
		table.removeGeneratedColumn("chkSelect");
		table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				SearchReallocationDoctorDetailsTableDTO tableDTO = (SearchReallocationDoctorDetailsTableDTO) itemId;
				  CheckBox chkBox = new CheckBox("");
				  
				  if(null != tableDataList && !tableDataList.isEmpty())
				  {
					  for (SearchReallocationDoctorDetailsTableDTO dto : tableDataList) {
						  if(dto.getEmpId().equalsIgnoreCase(tableDTO.getEmpId()))
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
				  
				  chkBox.setData(tableDTO);
				  addListener(chkBox);
					
				return chkBox;
			}
		});
		
		compMap = new WeakHashMap<String, Component>();
		
		table.removeGeneratedColumn("status");
		table.addGeneratedColumn("status", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				SearchReallocationDoctorDetailsTableDTO tableDTO = (SearchReallocationDoctorDetailsTableDTO) itemId;
				  ComboBox comboBox = new ComboBox("");
				  
				  if(tableDTO != null && tableDTO.getStatusList() != null){
					  
					  	BeanItemContainer<SelectValue> selectValueContainer = tableDTO.getStatusList();
						List<SelectValue> selValueList = new ArrayList<SelectValue>();
						SelectValue sel1 = new SelectValue();
						sel1.setId(ReferenceTable.REALLOCATION_ACTIVE_STATUS);
						sel1.setValue(SHAConstants.REALLOCATION_ACTIVE);
						selValueList.add(sel1);

						SelectValue sel2 = new SelectValue();
						sel2.setId(ReferenceTable.REALLOCATION_HOLD_STATUS);
						sel2.setValue(SHAConstants.REALLOCATION_HOLD);
						selValueList.add(sel2);
						
						selectValueContainer.addAll(selValueList);
						
						
						comboBox.setContainerDataSource(selectValueContainer);
						comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						comboBox.setItemCaptionPropertyId("value");
					  
				  }
				  
				  if(null != tableDataList && !tableDataList.isEmpty())
				  {
					  for (SearchReallocationDoctorDetailsTableDTO dto : tableDataList) {
						  if(dto.getEmpId().equalsIgnoreCase(tableDTO.getEmpId()))
						  {
							  if(null != dto.getStatus()){
								   comboBox.setValue(dto.getStatus().getValue());
							  }
							   else if(dto.getStatusValue().equals(1))
							   {
								   comboBox.setValue(SHAConstants.REALLOCATION_ACTIVE);
							   }
							   else if(dto.getStatusValue().equals(0))
							   {
								   comboBox.setValue(SHAConstants.REALLOCATION_HOLD);
							   }
						  }
						
					}
				  }
				  
				  comboBox.setValue(tableDTO.getStatus());
				  comboBox.setData(tableDTO);
				  comboBox.setReadOnly(true);
				  compMap.put(tableDTO.getEmpId(), comboBox);
				  addStatusListener(comboBox);
				  
				  return comboBox;
			}
		});
		
		table.removeGeneratedColumn("assignedCount");
		table.addGeneratedColumn("assignedCount", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				SearchReallocationDoctorDetailsTableDTO tableDTO = (SearchReallocationDoctorDetailsTableDTO) itemId;
				
				Button button = new Button("");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
					//To implement claims dms functionality.
						showStatusView((SearchReallocationDoctorDetailsTableDTO)itemId, SHAConstants.REALLOCATION_TOTAL_ASSIGNED);
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	if(tableDTO != null)
		    	button.setCaption(tableDTO.getAssignedCount() != null ? String.valueOf(tableDTO.getAssignedCount()) : "");
				return button;
			}
		});
		
		table.removeGeneratedColumn("completedCount");
		table.addGeneratedColumn("completedCount", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				SearchReallocationDoctorDetailsTableDTO tableDTO = (SearchReallocationDoctorDetailsTableDTO) itemId;
				
				Button button = new Button("");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
					//To implement claims dms functionality.
						showStatusView((SearchReallocationDoctorDetailsTableDTO)itemId, SHAConstants.REALLOCATION_COMPLETED);
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	if(tableDTO != null)
			    	button.setCaption(tableDTO.getCompletedCount() != null ? String.valueOf(tableDTO.getCompletedCount()) : "");
				return button;
			}
		});
		
		table.removeGeneratedColumn("pendingCount");
		table.addGeneratedColumn("pendingCount", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				SearchReallocationDoctorDetailsTableDTO tableDTO = (SearchReallocationDoctorDetailsTableDTO) itemId;
				
				Button button = new Button("");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
					//To implement claims dms functionality.
						showStatusView((SearchReallocationDoctorDetailsTableDTO)itemId, SHAConstants.REALLOCATION_PENDING);
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	if(tableDTO != null)
			    	button.setCaption(tableDTO.getPendingCount() != null ? String.valueOf(tableDTO.getPendingCount()) : "");
				return button;
			}
		});
		
	}
	
	private void addListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					boolean value = (Boolean) event.getProperty().getValue();
					SearchReallocationDoctorDetailsTableDTO tableDTO = (SearchReallocationDoctorDetailsTableDTO)chkBox.getData();
					
					if(null != tableDataList && !tableDataList.isEmpty())
					{
						for (SearchReallocationDoctorDetailsTableDTO doctorDetailsDTO : tableDataList) {
							
							if(doctorDetailsDTO.getEmpId().equalsIgnoreCase(tableDTO.getEmpId()))
							{
								if(value)
								{
									doctorDetailsDTO.setCheckBoxStatus("true");

									if(null != compMap && !compMap.isEmpty())
									{
										ComboBox comBox = (ComboBox) compMap.get(doctorDetailsDTO.getEmpId());
										if(comBox != null){
											comBox.setReadOnly(false);	
										}
									}
									
									//tableDTO.setChkSelect(true);
								}
								else
								{
									doctorDetailsDTO.setCheckBoxStatus("false");
									
									if(null != compMap && !compMap.isEmpty())
									{
										ComboBox comBox = (ComboBox) compMap.get(doctorDetailsDTO.getEmpId());
										if(comBox != null){
											comBox.setReadOnly(true);	
										}
									}
									//tableDTO.setChkSelect(false);
								}
							}
						}
					}
				}
			}
		});
	}
	
	
	private void addStatusListener(final ComboBox comboBox)
	{	
		comboBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					ComboBox component = (ComboBox) event.getProperty();
					
					SearchReallocationDoctorDetailsTableDTO tableDTO = (SearchReallocationDoctorDetailsTableDTO)comboBox.getData();
					
					if(null != tableDataList && !tableDataList.isEmpty())
					{
						for (SearchReallocationDoctorDetailsTableDTO doctorDetailsDTO : tableDataList) {
							
							if(doctorDetailsDTO.getEmpId().equalsIgnoreCase(tableDTO.getEmpId()))
							{
								if(component != null){
								if(String.valueOf(component.getValue()).equalsIgnoreCase(SHAConstants.REALLOCATION_ACTIVE))
								{
									doctorDetailsDTO.setStatusValue(1);
								}
								else if(String.valueOf(component.getValue()).equalsIgnoreCase(SHAConstants.REALLOCATION_HOLD))
								{
									doctorDetailsDTO.setStatusValue(0);
									//tableDTO.setChkSelect(false);
								}
								}
							}
						}
					}
				}
			}
		});
	}
	
	public List<SearchReallocationDoctorDetailsTableDTO> getTableAllItems()
	{
		return (List<SearchReallocationDoctorDetailsTableDTO>)table.getItemIds();
	}
	
	public void setFinalTableList(List<SearchReallocationDoctorDetailsTableDTO> tableRows) {
		Boolean isListEmpty = false;

			if(null != tableRows && !tableRows.isEmpty())
			{
				for (SearchReallocationDoctorDetailsTableDTO tableDTO : tableRows) {
					if(null != tableDataList && !tableDataList.isEmpty() && null != tableDataIdList && !tableDataIdList.isEmpty())
					{
						if(!tableDataIdList.contains(tableDTO.getEmpId()))
								{
									tableDataList.add(tableDTO);
									tableDataIdList.add(tableDTO.getEmpId());
								}
					}
					else
					{
						isListEmpty = true;
						break;
					}
				}
				if(isListEmpty)
				{
					for (SearchReallocationDoctorDetailsTableDTO tableDTO : tableRows) {
						tableDataList.add(tableDTO);
						tableDataIdList.add(tableDTO.getEmpId());
					}
					
				}
			}
	}
	
	public void showStatusView(SearchReallocationDoctorDetailsTableDTO tableDTO, String count)
	{
		fireViewEvent(SearchReallocationDoctorDetailsPresenter.SHOW_INTIMATION_STATUS,tableDTO, count);
		//fireViewEvent(EditReallocationCountDetailsPresenter.SET_INTIMATION_DETAILS_RE_ALLOCATION,tableDTO, count);
	}
	
	
	public List<SearchReallocationDoctorDetailsTableDTO> getFinalDataList()
	{
		return tableDataList;
	}
	
	public List<SearchReallocationDoctorDetailsTableDTO> getTableItems()
	{
		return tableDataList;
	}
	
	public void resetTableDataList()
	{
		if(null != tableDataList)
		{
			tableDataList.clear();
		}
		if(null != tableDataIdList)
		{
			tableDataIdList.clear();
		}
	}
	
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}
}
