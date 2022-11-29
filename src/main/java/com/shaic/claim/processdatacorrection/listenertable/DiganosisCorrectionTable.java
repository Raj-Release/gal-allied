package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.premedical.listenerTables.AddDiagnosisPopup;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionhistorical.search.DataCorrectionHistoricalPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.shaic.domain.MasterService;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class DiganosisCorrectionTable extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 197854198269811200L;

	@EJB
	private MasterService masterService;

	private Map<DiganosisCorrectionDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DiganosisCorrectionDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<DiganosisCorrectionDTO> data = new BeanItemContainer<DiganosisCorrectionDTO>(DiganosisCorrectionDTO.class);

	private Table table;

	private Map<String, Object> referenceData;

	private List<String> errorMessages;

	public Boolean isValueChanges=false;

	public String presenterString;
	
	private List<Long> editedKeys;

	public void init(String presenterString){

		this.presenterString = presenterString;
		this.errorMessages = new ArrayList<String>();
		this.editedKeys = new ArrayList<Long>();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		initTable(layout);

		table.setWidth("80%");
		table.setPageLength(table.getItemIds().size());

		layout.addComponent(table);
		setCompositionRoot(layout);

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	@SuppressWarnings("deprecation")
	void initTable(VerticalLayout layout){

		table = new Table("Diagnosis Details", data);
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
						DiganosisCorrectionDTO dto = (DiganosisCorrectionDTO) currentItemId;
						if(!editedKeys.contains(dto.getKey())){
							editedKeys.add(dto.getKey());
							if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
								fireViewEvent(DataCorrectionPresenter.EDIT_DIGANOSIS_CORRECTION_VALUES,dto);	
							}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_HISTORICAL)){
								fireViewEvent(DataCorrectionHistoricalPresenter.EDIT_DIGANOSIS_CORRECTION_VALUES_HIST,dto);	
							}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
								fireViewEvent(DataCorrectionPriorityPresenter.EDIT_DIGANOSIS_CORRECTION_VALUES_PRIORITY,dto);	
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
		table.setVisibleColumns(new Object[] {"primaryDiagnosis","diagnosisName","icdCode","edit"});
		table.setColumnHeader("diagnosisName", "Hospital Diagnosis");
		table.setColumnHeader("icdCode", "Insurance Diagnosis");
		table.setColumnHeader("primaryDiagnosis", "Primary");
		table.setColumnHeader("edit", "");
		
		table.setColumnAlignment("diagnosisName",Align.CENTER);
		table.setColumnAlignment("icdCode",Align.CENTER);
		table.setColumnAlignment("primaryDiagnosis",Align.CENTER);
		table.setColumnAlignment("edit",Align.CENTER);
		table.setEditable(true);

		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {

		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {

			DiganosisCorrectionDTO diganosisCorrectionDTO = (DiganosisCorrectionDTO)itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(diganosisCorrectionDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(diganosisCorrectionDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(diganosisCorrectionDTO);
			}
			if("diagnosisName".equals(propertyId)){
				GComboBox box = new GComboBox();
				adddiagnosisNames(box,diganosisCorrectionDTO);
				tableRow.put("diagnosisName", box);
				box.setData(diganosisCorrectionDTO);
				box.setWidth("70%");
				if(diganosisCorrectionDTO.getDiagnosisName() != null){
					box.setValue(diganosisCorrectionDTO.getDiagnosisName());
				}
				box.setEnabled(false);		
				return box;
			} else if("icdCode".equals(propertyId)){
				GComboBox box = new GComboBox();
				addicdCodeNames(box,diganosisCorrectionDTO);
				tableRow.put("icdCode", box);
				box.setData(diganosisCorrectionDTO);
				box.setWidth("70%");
				if(diganosisCorrectionDTO.getIcdCode() != null){
					box.setValue(diganosisCorrectionDTO.getIcdCode());
				}
				box.setEnabled(false);
				return box;
			}
			else if("primaryDiagnosis".equals(propertyId))
			 {
				 final OptionGroup optionType = new OptionGroup();
				 optionType.addItems(getReadioButtonOptions());
				 optionType.setItemCaption(Boolean.valueOf("true"), "");
				 optionType.setEnabled(false);
				 optionType.setData(diganosisCorrectionDTO);
				 optionType.setStyleName("inlineStyle");
				 optionType.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
				 optionType.setValue(diganosisCorrectionDTO.getPrimaryDiagnosis());
				 if(diganosisCorrectionDTO.getPrimaryDiagnosis() != null){
					 if(diganosisCorrectionDTO.getPrimaryDiagnosis().booleanValue()){
						 optionType.select(Boolean.valueOf("true"));
					 }
				 }else{
					 optionType.select(null);
				 }
				 return optionType;
			 }else {
				Field<?> field = super.createField(container, itemId,propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	public List<DiganosisCorrectionDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<DiganosisCorrectionDTO> itemIds = (List<DiganosisCorrectionDTO>) this.table.getItemIds();
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(DiganosisCorrectionDTO diganosisCorrectionDTO) {
		data.addItem(diganosisCorrectionDTO);
	}

	public void addBeansToList(List<DiganosisCorrectionDTO> diganosisCorrectionDTOs){

		for(DiganosisCorrectionDTO diganosisCorrectionDTO: diganosisCorrectionDTOs){
			addBeanToList(diganosisCorrectionDTO);
		}
	}

	@SuppressWarnings("deprecation")
	private void addicdCodeNames(ComboBox diagnosisName,DiganosisCorrectionDTO diganosisCorrectionDTO) {
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addBean(diganosisCorrectionDTO.getIcdCode());
		diagnosisName.setContainerDataSource(selectValueContainer);
		diagnosisName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisName.setItemCaptionPropertyId("value");
	}

	@SuppressWarnings("deprecation")
	private void adddiagnosisNames(ComboBox diagnosisName,DiganosisCorrectionDTO diganosisCorrectionDTO) {
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addBean(diganosisCorrectionDTO.getDiagnosisName());
		diagnosisName.setContainerDataSource(selectValueContainer);
		diagnosisName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisName.setItemCaptionPropertyId("value");
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}
	
	public void removeDiganosisEdited(Long Key){
		if(Key !=null && editedKeys !=null){	
			editedKeys.remove(Key);
		}
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(1);
		coordinatorValues.add(true);

		return coordinatorValues;
	}
	
	public void refreshTable(){
		table.refreshRowCache(); 
	}
	
	public void clearObject() {
		clearDataCorrectionTableItem(tableItem);
		SHAUtils.setClearReferenceData(referenceData);
		masterService = null;
		data = null;
		errorMessages = null;
		presenterString = null;
		editedKeys = null;
	}
	
	private void clearDataCorrectionTableItem(Map<DiganosisCorrectionDTO, HashMap<String, AbstractField<?>>> referenceData){
		
		if(referenceData != null){
		
	    	Iterator<Entry<DiganosisCorrectionDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
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
