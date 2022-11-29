package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class NewMedicalDecisionFVRGradingBListenerTable extends ViewComponent {
	
	private Map<NewFVRGradingDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<NewFVRGradingDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<NewFVRGradingDTO> data = new BeanItemContainer<NewFVRGradingDTO>(NewFVRGradingDTO.class);

	private Table table;

	//private Button btnAdd;
	
	/*private Map<String, Object> referenceData;
	
	private Long hospitalKey;
	
	private String packageRateValue;
	
	private String dayCareFlagValue;
	
	private String procedureCodeValue;*/
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	/*private String presenterString;
	
	private PreauthDTO bean;*/
	public TextField dummyField;
	
	public void init(PreauthDTO bean) {
		//this.bean = bean;
		//this.diagnosisList = diagnosisList;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		//btnAdd = new Button();
		//btnAdd.setStyleName("link");
		//btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		//HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		//btnLayout.setWidth("100%");
		//btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
//		layout.addComponent(btnLayout);
		layout.setMargin(true);
		
		initTable(layout);
		table.setWidth("100%");
		//table.setHeight("30%");
		/**
		 * Height is set for table visiblity.
		 * */
		table.setPageLength(table.getItemIds().size());
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}


	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("SEGMENT: B", data);
		table.addStyleName("generateColumnTable");
		//table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] {"serialNumber",
				"category", "selectFlag" });

		table.setColumnHeader("serialNumber", "S.No");
		table.setColumnHeader("category", "B. STANDARD VERIFICATION POINTS");
		table.setColumnHeader("selectFlag", "");
		
		table.setEditable(true);
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		dummyField = new TextField();
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			NewFVRGradingDTO fvrGradingDTO = (NewFVRGradingDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			//Boolean isEnabled =  procedureDTO.getEnableOrDisable() ? true : false;
			
			if (tableItem.get(fvrGradingDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(fvrGradingDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(fvrGradingDTO);
			}
			
			if("serialNumber".equals(propertyId)) {
				TextField box = new TextField();
				box.setWidth("20px");
				box.setNullRepresentation("");
				tableRow.put("serialNumber", box);
				box.setReadOnly(true);
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return box;
			} else if ("category".equals(propertyId)) {
				TextField box = new TextField();
				box.setNullRepresentation("");
				tableRow.put("category", box);
				box.setReadOnly(true);
				box.setWidth("450px");
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return box;
			}  else if("selectFlag".equals(propertyId)) {
				OptionGroup selectGroup = new OptionGroup();
				selectGroup.addItems(getReadioButtonOptions());
				selectGroup.setItemCaption(true, "Yes");
				selectGroup.setItemCaption(false, "No");
				selectGroup.setStyleName("horizontal");
				tableRow.put("selectFlag", selectGroup);
				
				if(fvrGradingDTO.getIsEditAB() != null){
					if(fvrGradingDTO.getIsEditAB()){
						selectGroup.setEnabled(true);
					}else{
						selectGroup.setEnabled(false);
					}
				}
				
				/*if(!fvrGradingDTO.getIsEditABC()){
					selectGroup.setEnabled(false);
				}*/
				
				selectGroup.addValueChangeListener(segmentBListener());
				return selectGroup;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		//this.referenceData = referenceData;
//		manageListeners();
	}
	
	@SuppressWarnings("unchecked")
	private void addCommonValues(ComboBox statusCombo, String tableColumnName, BeanItemContainer<SelectValue> commonValues) {
		if(commonValues != null) {
			statusCombo.setContainerDataSource(commonValues);
			statusCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			statusCombo.setItemCaptionPropertyId("value");
		}
		
	}
	
	/*protected void manageListeners() {

		for (NewFVRGradingDTO fvrGradingDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(fvrGradingDTO);
			
			final ComboBox statusCombo = (ComboBox) combos.get("status");
//			addCommonValues(statusCombo, "status");
			if(fvrGradingDTO.getStatusFlag() != null ) {
				statusCombo.setValue(fvrGradingDTO.getStatus());
			}
		}
	}*/
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<NewFVRGradingDTO> itemIds = (Collection<NewFVRGradingDTO>) table.getItemIds();
		/*Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();*/
		for (NewFVRGradingDTO bean : itemIds) {
			
			Set<ConstraintViolation<NewFVRGradingDTO>> validate = validator.validate(bean);
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<NewFVRGradingDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		return !hasError;
	}
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	public void addBeanToList(NewFVRGradingDTO fvrGradingDTO) {
		data.addItem(fvrGradingDTO);
	}
	
	public void setTableList(List<NewFVRGradingDTO> fvrGradingDTOList) {
		int i = 1;
		for (NewFVRGradingDTO fvrGradingDTO2 : fvrGradingDTOList) {
			if(fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B)){
				fvrGradingDTO2.setSerialNumber(i);
				data.addItem(fvrGradingDTO2);
				i++;	
			}
		}
//		manageListeners();
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}
	
	 @SuppressWarnings("unchecked")
		public List<NewFVRGradingDTO> getValues() {
			List<NewFVRGradingDTO> itemIds = (List<NewFVRGradingDTO>) this.table.getItemIds() ;
	    	return itemIds;
		}
	 
	 public void removeRow() {
			table.removeAllItems();
		}
	 
	 public void resetTableList(List<NewFVRGradingDTO> fvrGradingDTOList) {
			int i = 1;
			for (NewFVRGradingDTO fvrGradingDTO2 : fvrGradingDTOList) {
				if(fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B)){
					fvrGradingDTO2.setSerialNumber(i);
					fvrGradingDTO2.setSelectFlag(null);
					fvrGradingDTO2.setIsEditAB(null);
					
					if (tableItem.get(fvrGradingDTO2) != null) {
						HashMap<String, AbstractField<?>> option = tableItem
								.get(fvrGradingDTO2);
						if (option != null) {
							final OptionGroup statusOption = (OptionGroup) option
									.get("selectFlag");

							if (statusOption != null) {
								statusOption.setValue(null);
								statusOption.setEnabled(true);
							}
						}

					}
					
					data.addItem(fvrGradingDTO2);
					i++;
				}
				dummyField.setValue(null);
			}
//			manageListeners();
		}
	 
	public ValueChangeListener segmentBListener() {
		ValueChangeListener listener = new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {

				if (event.getProperty() != null && event.getProperty().getValue() != null) {
					dummyField.setValue(SHAConstants.YES);
				}else{
					dummyField.setValue(SHAConstants.No);
				}
			}
		};

		return listener;
	}
	
	public void nonEditFields(List<NewFVRGradingDTO> fvrGradingDTOList) {

		for (NewFVRGradingDTO fvrGradingDTO2 : fvrGradingDTOList) {

			if (tableItem.get(fvrGradingDTO2) != null) {
				HashMap<String, AbstractField<?>> option = tableItem
						.get(fvrGradingDTO2);
				if (option != null) {
					final OptionGroup statusOption = (OptionGroup) option
							.get("selectFlag");

					if (statusOption != null) {
						statusOption.setEnabled(false);
						fvrGradingDTO2.setIsEditAB(false);
					}
				}

			}/*else{
				if(fvrGradingDTO2.getSegment() != null && fvrGradingDTO2.getSegment().equalsIgnoreCase(SHAConstants.FVR_GRADING_SEGMENT_B)){
					fvrGradingDTO2.setIsEditAB(false);
				}
			}*/

		}

	}
	
	public void init() {
		//this.bean = bean;
		//this.diagnosisList = diagnosisList;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		//btnAdd = new Button();
		//btnAdd.setStyleName("link");
		//btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		//HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		//btnLayout.setWidth("100%");
		//btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
//		layout.addComponent(btnLayout);
		layout.setMargin(true);
		
		initTable(layout);
		table.setWidth("100%");
		//table.setHeight("30%");
		/**
		 * Height is set for table visiblity.
		 * */
		table.setPageLength(table.getItemIds().size());
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	 
}
