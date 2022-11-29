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

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MedicalDecisionFVRGradingListenerTable extends ViewComponent {
	private static final long serialVersionUID = 4809460534159116589L;
	
	private Map<FVRGradingDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<FVRGradingDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<FVRGradingDTO> data = new BeanItemContainer<FVRGradingDTO>(FVRGradingDTO.class);

	private Table table;

	private Button btnAdd;
	
	/*private Map<String, Object> referenceData;
	
	private Long hospitalKey;
	
	private String packageRateValue;
	
	private String dayCareFlagValue;
	
	private String procedureCodeValue;*/
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	/*private String presenterString;
	
	private PreauthDTO bean;*/
	
	public void init(PreauthDTO bean) {
		//this.bean = bean;
		//this.diagnosisList = diagnosisList;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
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
		table = new Table("FVR Grading", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] {"serialNumber",
				"category", "applicability", "status" });

		table.setColumnHeader("serialNumber", "S.No");
		table.setColumnHeader("category", "Category");
		table.setColumnHeader("applicability", "Applicability");
		table.setColumnHeader("status", "Status");
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
			FVRGradingDTO fvrGradingDTO = (FVRGradingDTO) itemId;
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
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return box;
			} else if ("applicability".equals(propertyId)) {
				TextField box = new TextField();
				box.setNullRepresentation("");
				tableRow.put("applicability", box);
				box.setReadOnly(true);
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return box;
			}  else if("status".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100px");
				tableRow.put("status", box);
				addCommonValues(box, "status", fvrGradingDTO.getCommonValues());
				if(fvrGradingDTO.getStatus() != null) {
					box.setValue(fvrGradingDTO.getStatus());
				}
				return box;
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
	
	protected void manageListeners() {

		for (FVRGradingDTO fvrGradingDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(fvrGradingDTO);
			
			final ComboBox statusCombo = (ComboBox) combos.get("status");
//			addCommonValues(statusCombo, "status");
			if(fvrGradingDTO.getStatusFlag() != null ) {
				statusCombo.setValue(fvrGradingDTO.getStatus());
			}
		}
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<FVRGradingDTO> itemIds = (Collection<FVRGradingDTO>) table.getItemIds();
		/*Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();*/
		for (FVRGradingDTO bean : itemIds) {
			
			Set<ConstraintViolation<FVRGradingDTO>> validate = validator.validate(bean);
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<FVRGradingDTO> constraintViolation : validate) {
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
	
	public void addBeanToList(FVRGradingDTO fvrGradingDTO) {
		data.addItem(fvrGradingDTO);
	}
	
	public void setTableList(List<FVRGradingDTO> fvrGradingDTOList) {
		int i = 1;
		for (FVRGradingDTO fvrGradingDTO2 : fvrGradingDTOList) {
			fvrGradingDTO2.setSerialNumber(i);
			data.addItem(fvrGradingDTO2);
			i++;
		}
//		manageListeners();
	}
}
