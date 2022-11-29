package com.shaic.claim.preauth.wizard.listenerTables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MedicalDecisionListenerTable extends ViewComponent{
	private static final long serialVersionUID = -4907626818911485038L;
	
	private Map<MedicalDecisionTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<MedicalDecisionTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<MedicalDecisionTableDTO> data = new BeanItemContainer<MedicalDecisionTableDTO>(MedicalDecisionTableDTO.class);

	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	public void init() {
		btnAdd = new Button();
		this.errorMessages = new ArrayList<String>();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(btnLayout);
		
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("30%");
		table.setPageLength(table.getItemIds().size());
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Procedure List", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "procedureName", "procedureCode", "packageRate", "dayCareProcedure", "considerForDayCare", "sublimitApplicable", "sublimitName", "sublimitDesc", "sublimitAmount","considerForPayment", "remarks"  });

		table.setColumnHeader("procedureName", "Procedure Name");
		table.setColumnHeader("procedureCode", "Procedure Code");
		table.setColumnHeader("packageRate", "Package Rate");
		table.setColumnHeader("dayCareProcedure", "Day Care Procedure");
		table.setColumnHeader("considerForDayCare", "Consider For Day Care");
		table.setColumnHeader("sublimitApplicable", "Sub Limit Applicable");
		table.setColumnHeader("sublimitName", "Sub Limit Name");
		table.setColumnHeader("sublimitDesc", "Sub Limit Desc");
		table.setColumnHeader("sublimitAmount", "Sub Limit Amount");
		table.setColumnHeader("considerForPayment", "Consider For Payment");
		table.setColumnHeader("remarks", "Remarks");
		table.setEditable(true);
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 6080648912115172307L;
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			MedicalDecisionTableDTO medicalDecisionTableDTO = (MedicalDecisionTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(medicalDecisionTableDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(medicalDecisionTableDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(medicalDecisionTableDTO);
			}
			
			if("referenceNo".equals(propertyId)) {
				TextField box = new TextField();
				box.setReadOnly(true);
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("referenceNo", box);
				return box;
			} else if ("treatmentType".equals(propertyId)) {
				TextField box = new TextField();
				box.setReadOnly(true);
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("treatmentType", box);
				return box;
			} else if ("procedureOrDiagnosis".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				tableRow.put("procedureOrDiagnosis", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("pedOrExclusionDetails".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				tableRow.put("pedOrExclusionDetails", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("currentSubLimitAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				tableRow.put("currentSubLimitAmount", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("sumInsuredRestriction".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				tableRow.put("sumInsuredRestriction", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("subLimitUtilizedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				tableRow.put("subLimitUtilizedAmount", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("availableSublimit".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				tableRow.put("availableSublimit", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("packageAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				tableRow.put("packageAmount", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			}
			else if ("approvedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				tableRow.put("approvedAmount", field);
				return field;
			} else if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				tableRow.put("remarks", field);
				return field;
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<MedicalDecisionTableDTO> itemIds = (Collection<MedicalDecisionTableDTO>) table.getItemIds();
		for (MedicalDecisionTableDTO bean : itemIds) {
			
			if(!((bean.getAddedAmount() == null && SHAUtils.isValidDouble(bean.getAddedAmount())) && (bean.getCumulativeAmt() == null && SHAUtils.isValidDouble(bean.getCumulativeAmt())))) {
				hasError = true;
				errorMessages.add("Please Enter Add Amount or Cumulative Amt");
			}
		}
		return !hasError;
	}
	public List<String> getErrors()
	{
		return this.errorMessages;
	}

}
