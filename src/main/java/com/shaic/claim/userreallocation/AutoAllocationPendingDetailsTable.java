package com.shaic.claim.userreallocation;

import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.arch.test.DiagnosisComboBox;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;

public class AutoAllocationPendingDetailsTable extends GBaseTable<AutoAllocationDetailsTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	DoctorReallocationSearchCriteriaService searchService;
	
	@EJB
	private MasterService masterService;
	
	private static final Object[] NATURAL_COL_ORDER_PENDING = new Object[] {
		"sNumber","intimationNo","doctorId", "doctorName", "claimedAmt","cpu","assignedDate","chkSelect","reAllocateTo"};
	
	public WeakHashMap<String,Component> checkBoxMap = null;
	
	public WeakHashMap<String,Component> allocateToMap = null;
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		table.setContainerDataSource(new BeanItemContainer<AutoAllocationDetailsTableDTO>(AutoAllocationDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER_PENDING);
		generatecolumns();
	}

	@Override
	public void tableSelectHandler(AutoAllocationDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "intimation-list-re-allocation-pending-";
	}
	
	public void generatecolumns(){
		
		checkBoxMap = new WeakHashMap<String, Component>();
		allocateToMap = new WeakHashMap<String, Component>();
		
		table.removeGeneratedColumn("chkSelect");
		table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				AutoAllocationDetailsTableDTO tableDTO = (AutoAllocationDetailsTableDTO) itemId;
				CheckBox chkBox = new CheckBox("");
				chkBox.setData(tableDTO);
				checkBoxMap.put(tableDTO.getIntimationNo(), chkBox);
				addListener(chkBox);
	
				return chkBox;
			}
		});
		
		
		table.removeGeneratedColumn("reAllocateTo");
		table.addGeneratedColumn("reAllocateTo", new Table.ColumnGenerator() {
			/**
			 * 
			 */

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				AutoAllocationDetailsTableDTO tableDTO = (AutoAllocationDetailsTableDTO) itemId;
				
				final DiagnosisComboBox box = new DiagnosisComboBox(tableDTO.getDoctorId());
				final SuggestingContainer reAllocateContainer = new SuggestingContainer(masterService);
				box.setContainerDataSource(reAllocateContainer);	
//				box.setEnabled(isEnabled);
				box.setFilteringMode(FilteringMode.STARTSWITH);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				box.setItemCaptionPropertyId("value");
				box.setNewItemsAllowed(false);
				box.setData(tableDTO);
				allocateToMap.put(tableDTO.getIntimationNo(), box);
				
				box.addValueChangeListener(new Property.ValueChangeListener() {
			            @Override
			            public void valueChange(ValueChangeEvent event) {
			                
			                // tell the custom container that a value has been selected. This is necessary to ensure that the
			                // selected value is displayed by the ComboBox
			            	SelectValue value = (SelectValue) event.getProperty().getValue();
			            	AutoAllocationDetailsTableDTO tableDTO = (AutoAllocationDetailsTableDTO)box.getData();
							if (value != null)
			            	{
								reAllocateContainer.setSelectedBean(value);
			            		box.select(value);
			            		tableDTO.setReAllocateTo(value.getId());
			            		//tableDTO.setReAllocateTo(value);
			            		if(checkBoxMap != null && !checkBoxMap.isEmpty()){
			            			CheckBox checkBox = (CheckBox)checkBoxMap.get(tableDTO.getIntimationNo());
			            			if(checkBox != null){
			            				checkBox.setEnabled(false);
			            				checkBox.setValue(null);
			            				tableDTO.setChkSelect(false);
			            			}
			            		}
			            	}else{
			            		if(checkBoxMap != null && !checkBoxMap.isEmpty()){
			            			CheckBox checkBox = (CheckBox)checkBoxMap.get(tableDTO.getIntimationNo());
			            			if(checkBox != null){
			            				checkBox.setEnabled(true);
			            			}
			            		}
			            	}
			            }
			        });
				
					/*box.setNewItemHandler(new NewItemHandler() {
						
						private static final long serialVersionUID = -4453822645147859276L;

						@Override
						public void addNewItem(String newItemCaption) {
							SelectValue newDiagonsisValue = masterService.addDiagnosis(newItemCaption);
							reAllocateContainer.addItem(newDiagonsisValue);
							reAllocateContainer.setNewItemAdded(true);
							box.addItem(newDiagonsisValue);
							box.select(newDiagonsisValue);
						}
					});*/
				
				
				return box;
				
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
					AutoAllocationDetailsTableDTO tableDTO = (AutoAllocationDetailsTableDTO)chkBox.getData();
					
					if(value){
						tableDTO.setChkSelect(true);
						if(allocateToMap != null && !allocateToMap.isEmpty()){
							DiagnosisComboBox autoComplete = (DiagnosisComboBox) allocateToMap.get(tableDTO.getIntimationNo());
							if(autoComplete != null){
								autoComplete.setEnabled(false);
								autoComplete.setValue(null);
								tableDTO.setReAllocateTo(null);
							}
						}
					}else{
						tableDTO.setChkSelect(false);
						if(allocateToMap != null && !allocateToMap.isEmpty()){
							DiagnosisComboBox autoComplete = (DiagnosisComboBox) allocateToMap.get(tableDTO.getIntimationNo());
							if(autoComplete != null){
								autoComplete.setEnabled(true);
							}
						}
					}
					
				
				}
			}
		});
	}
	
	public List<AutoAllocationDetailsTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<AutoAllocationDetailsTableDTO> dto = (List<AutoAllocationDetailsTableDTO>) this.table.getItemIds() ;
    	return dto;
    }
	
}
