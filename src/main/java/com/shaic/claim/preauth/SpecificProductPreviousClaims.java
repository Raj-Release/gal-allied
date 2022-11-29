package com.shaic.claim.preauth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.preauth.dto.SpecificProductDeductibleTableDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SpecificProductPreviousClaims extends ViewComponent {
	
	private static final long serialVersionUID = -9150223671091034333L;

	private Map<SpecificProductDeductibleTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<SpecificProductDeductibleTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<SpecificProductDeductibleTableDTO> data = new BeanItemContainer<SpecificProductDeductibleTableDTO>(SpecificProductDeductibleTableDTO.class);

	private Table table;

	private Button btnAdd;
	
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private String presenterString;
	
	public void init(Long hospitalKey, String presenterString) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		
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
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Procedure List", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		
		table.setVisibleColumns(new Object[] { "claims", "amountConsidered", "originalSI", "amountToBeConsidered", "deductible", "eligibleAmountPayable", "balanceSI", "amountPayable"});

		table.setColumnHeader("claims", "Claims");
		table.setColumnHeader("amountConsidered", "Amount Considered");
		table.setColumnHeader("originalSI", "Original Policy SI");
		table.setColumnHeader("amountToBeConsidered", "Amount to be Considered");
		table.setColumnHeader("deductible", "Deductible");
		table.setColumnHeader("eligibleAmountPayable", "Eligible Amount Payable");
		table.setColumnHeader("balanceSI", "Balance SI");
		table.setColumnHeader("amountPayable", "Amount Payable");
		table.setEditable(false);
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
			SpecificProductDeductibleTableDTO dto = (SpecificProductDeductibleTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			//Boolean isEnabled =  procedureDTO.getEnableOrDisable() ? true : false;
			
			if (tableItem.get(dto) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(dto, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(dto);
			}
			Field<?> field = super.createField(container, itemId,
					propertyId, uiContext);

			if (field instanceof TextField)
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("80px");
			return field;
		}
	}
}
