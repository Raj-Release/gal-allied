package com.shaic.claim.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Alternative;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GListenerTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

@SuppressWarnings("serial")
@Alternative
public class SpecialityTable extends GListenerTable<SpecialityDTO> implements TableCellSelectionHandler {
	
	public SpecialityTable() {
		super(SpecialityTable.class);
		setUp();
	}

	private String presenterString;
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"specialityType", "remarks" };*/

/*	public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	{
		// fieldMap.put("key", new TableFieldDTO("key",TextField.class,
		// Long.class));
		// fieldMap.put("preAuthkey", new
		// TableFieldDTO("preAuthkey",TextField.class, Long.class));
		fieldMap.put("serialNo", new TableFieldDTO("serialNo",
				Integer.class, TextField.class, false, 10));
		fieldMap.put("specialityType", new TableFieldDTO("specialityType",
				ComboBox.class, SelectValue.class, true,this));
		fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
				String.class, true ,100));
	}*/

//	public void setTableList(List<SpecialityDTO> tableRows) {
//		List<SpecialityDTO> itemIds = (List<SpecialityDTO>) table.getItemIds();
//	}

	@Override
	public void removeRow() {
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SpecialityDTO>(
				SpecialityDTO.class));
		this.presenterString = null;
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"specialityType", "remarks" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button deleteButton = new Button("Delete");
		    	deleteButton
				.addClickListener(new Button.ClickListener() {
					public void buttonClick(
							ClickEvent event) {
			        	 deleteRow(itemId);
			        	 /*if(presenterString != null && !presenterString.isEmpty()){
			        		 if(presenterString.equalsIgnoreCase(SHAConstants.PROCESS_PREAUTH)){
			 					fireViewEvent(PreauthWizardPresenter.SHOW_PTCA_CABG,true);
			 				}else if(presenterString.equalsIgnoreCase(SHAConstants.PROCESS_ENHANCEMENT)){
			 					fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_SHOW_PTCA_CABG, true);
			 				}
			        	 }*/
			        } 
			    });
		    	return deleteButton;
		      };
		});
		table.setEditable(true);
	}

	@Override
	public void tableSelectHandler(SpecialityDTO t) {
	}

	@Override
	public String textBundlePrefixString() {
		return "preauth-sepciality-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

		{			
			fieldMap.put("specialityType", new TableFieldDTO("specialityType",
					ComboBox.class, SelectValue.class, true,this));
			fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
					String.class, true ,100));
		}
		return fieldMap;
	}

	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}

	}

	@Override
	public void deleteRow(Object itemId) {
		rowCount--;
		this.table.getContainerDataSource().removeItem(itemId);
		if(presenterString != null && !presenterString.isEmpty()){
	   		 if(presenterString.equalsIgnoreCase(SHAConstants.PROCESS_PREAUTH)){
					fireViewEvent(PreauthWizardPresenter.SHOW_PTCA_CABG,true);
				}else if(presenterString.equalsIgnoreCase(SHAConstants.PROCESS_ENHANCEMENT)){
					fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_SHOW_PTCA_CABG, true);
				}
	   	 }
	}

	/*@Override
	public SpecialityDTO createInstance() {
		SpecialityDTO specialityDTO = new SpecialityDTO();
		specialityDTO.setStatusFlag(true);
		return specialityDTO;
	}*/

	/*@Override
	public boolean isValid() {
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		Collection<SpecialityDTO> itemIds = (Collection<SpecialityDTO>) table.getItemIds();
		for (SpecialityDTO bean : itemIds) {
			Set<ConstraintViolation<SpecialityDTO>> validate = validator.validate(bean);

			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<SpecialityDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		return !hasError;
	}*/

	@Override
	public List<SpecialityDTO> getValues() 
	{
		Collection<SpecialityDTO> coll = (Collection<SpecialityDTO>) table.getItemIds();
		List list;
		if (coll instanceof List){
			list = (List)coll;
		}
		else{
			list = new ArrayList(coll);
		}
		return list;
	}

	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
		if(this.presenterString != null && !this.presenterString.isEmpty()){
			/*if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
			{
				if (event.getProperty().getValue() instanceof SelectValue){
					SelectValue splType = (SelectValue)event.getProperty().getValue();
					
					if(splType != null){
						if(splType.getId() != null && ReferenceTable.getSpecialityTypeSurgical().contains(splType.getId())){
							if(this.presenterString.equalsIgnoreCase(SHAConstants.PROCESS_PREAUTH)){
								fireViewEvent(PreauthWizardPresenter.SHOW_PTCA_CABG,true);
							}else if(this.presenterString.equalsIgnoreCase(SHAConstants.PROCESS_ENHANCEMENT)){
								fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_SHOW_PTCA_CABG, true);
							}
						}else{
							if(this.presenterString.equalsIgnoreCase(SHAConstants.PROCESS_PREAUTH)){
								fireViewEvent(PreauthWizardPresenter.SHOW_PTCA_CABG,false);	
							}else if(this.presenterString.equalsIgnoreCase(SHAConstants.PROCESS_ENHANCEMENT)){
								fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_SHOW_PTCA_CABG, false);	
							}
						}
					}
					
					
				}
			}*/
			if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){
				if (event.getProperty().getValue() instanceof SelectValue){
					if(this.presenterString.equalsIgnoreCase(SHAConstants.PROCESS_PREAUTH)){
						fireViewEvent(PreauthWizardPresenter.SHOW_PTCA_CABG,true);
					}else if(this.presenterString.equalsIgnoreCase(SHAConstants.PROCESS_ENHANCEMENT)){
						fireViewEvent(PreauthEnhancemetWizardPresenter.ENHANCEMENT_SHOW_PTCA_CABG, true);
					}
				}
			}
			
			
		}
		
		//System.out.println("Listener called");
	}

	 public void setReference(Map<String, Object> referenceData) {
			super.setReference(referenceData, this);
	}
	 
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
}
