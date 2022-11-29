package com.shaic.claim.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MedicalDecisionListenerTable extends ViewComponent  {
	private static final long serialVersionUID = 3618294170496450898L;

	private Map<MedicalDecisionTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<MedicalDecisionTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<MedicalDecisionTableDTO> data = new BeanItemContainer<MedicalDecisionTableDTO>(MedicalDecisionTableDTO.class);

	private Table table;
	
	PreauthDTO bean;
	
	//private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	//private static Validator validator;
	
	public Object[] VISIBLE_COLUMNS = new Object[] {
			"referenceNo", "treatmentType", "procedureOrDiagnosis","description", "pedOrExclusionDetails", "currentSubLimitAmount","sumInsuredRestriction", "subLimitUtilizedAmount", "availableSublimit", "packageRate"};
		

		public Object[] APPROVE_VISIBLE_COLUMNS = new Object[] {
				"referenceNo", "treatmentType","procedureOrDiagnosis","description", "pedOrExclusionDetails", "currentSubLimitAmount","sumInsuredRestriction", "subLimitUtilizedAmount", "availableSublimit", "packageRate", "approvedAmount", "remarks"};

		public TextField dummyField;
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.errorMessages = new ArrayList<String>();
		
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			MedicalDecisionTableDTO medicalDecisionDTO = (MedicalDecisionTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(medicalDecisionDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(medicalDecisionDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(medicalDecisionDTO);
			}
			
			if("approvedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(10);
				validator.extend(field);
				field.setData(medicalDecisionDTO);
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("approvedAmount", field);
				Boolean isResidual = true;
				if(!(medicalDecisionDTO.getReferenceNo() != null && medicalDecisionDTO.getReferenceNo().contains("Residual Treatment"))) {
					field.setValue("0");
					isResidual = false;
				} 
				if(!medicalDecisionDTO.getIsEnabled()) {
					medicalDecisionDTO.setApprovedAmount("0");
					field.setValue("0");
				}
				field.setEnabled(medicalDecisionDTO.getIsEnabled());
				addApporvedAmtListener(field, isResidual);
				
				return field;
			} else if ("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9.]*$");
				validator.setPreventInvalidTyping(true);
				field.setWidth("200px");
				field.setEnabled(medicalDecisionDTO.getIsEnabled());
				tableRow.put("remarks", field);
				return field;
			} 
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					((TextField) field).setNullRepresentation("");
					field.setReadOnly(true);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Medical Decision Table", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(VISIBLE_COLUMNS);

		table.setColumnHeader("referenceNo", "Reference No");
		table.setColumnHeader("treatmentType", "Treatment Type");
		table.setColumnHeader("procedureOrDiagnosis", "Proc/Diag");
		table.setColumnHeader("description", "Description");
		table.setColumnHeader("pedOrExclusionDetails", "PED / Exclusion Details");
		table.setColumnHeader("currentSubLimitAmount", "Current Sub Limit Amt");
		table.setColumnHeader("sumInsuredRestriction", "SI Restriction");
		table.setColumnHeader("subLimitUtilizedAmount", "Sub Limit Utilized Amt");
		table.setColumnHeader("availableSublimit", "Available Sub Limit");
		table.setColumnHeader("packageRate", "Package Amt");
		table.setColumnHeader("approvedAmount", "Approved Amt");
		table.setColumnHeader("remarks", "Remarks");
		table.setEditable(true);
		
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		table.setFooterVisible(true);
		dummyField = new TextField();
		table.setColumnFooter("referenceNo", "Total");
		layout.addComponent(table);
	}
	
	@SuppressWarnings("unused")
	private void addApporvedAmtListener(TextField approvedAmtField, final Boolean isResidual) {
		if (approvedAmtField != null) {
			approvedAmtField.addBlurListener(new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {

					TextField component = (TextField) event.getComponent();
					if(SHAUtils.isValidFloat(component.getValue())) {
						Integer enteredAmt = SHAUtils.getFloatFromString(component.getValue());
						
						if(isResidual) {
							calculateTotal();
							totalCalculation(component);
							return;
						}
						
						if(enteredAmt >= 0) {
							MedicalDecisionTableDTO medicalDecisionDTO = (MedicalDecisionTableDTO) component.getData();
							Boolean isSublimitAvailable = false;
							if(medicalDecisionDTO.getProcedureDTO() != null) {
								if(medicalDecisionDTO.getProcedureDTO().getSublimitName() != null && medicalDecisionDTO.getProcedureDTO().getSublimitName().getLimitId() != null) {
									isSublimitAvailable = true;
								}
							}
							if(medicalDecisionDTO.getPedValidationTableDTO() != null) {
								if(medicalDecisionDTO.getPedValidationTableDTO().getSublimitName() != null && medicalDecisionDTO.getPedValidationTableDTO().getSublimitName().getLimitId() != null) {
									isSublimitAvailable = true;
								}
							}
							
							Integer availableSublimit = SHAUtils.getFloatFromString(medicalDecisionDTO.getAvailableSublimit());
							Integer packageAmt = SHAUtils.getFloatFromString(medicalDecisionDTO.getPackageRate());
							Integer minimumAmt = availableSublimit;
							if(packageAmt > 0) {
								minimumAmt = Math.min(availableSublimit, packageAmt);
							}
							if(isSublimitAvailable && enteredAmt > minimumAmt) {
								VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red;'>Approved Amount should be lesser of Available Sublimit or Package Amount. </b>", ContentMode.HTML));
								layout.setMargin(true);
								component.setValue("0");
								showNotificationAlert(layout);
								calculateTotal();
								return;
							}
							
							//HashMap<String, AbstractField<?>> hashMap = tableItem.get(medicalDecisionDTO);
							totalCalculation(component);
							
						}
					} else {
						totalCalculation(component);
					}
					
				
				}
			}); 
		}

	}
	
	private void totalCalculation(TextField enteredAmtTxt) {
		String amountConsidered = bean.getAmountConsidered();
		Integer amtConsidered = SHAUtils.getFloatFromString(amountConsidered);
		Integer floatValue = bean.getBalanceSI().intValue();
		Integer minValue = Math.min(amtConsidered, floatValue);
		Integer calculatedTotal = calculateTotal();
		
//		if(calculatedTotal <= 0)
//		{
//			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red;'>Total Approved Amount should be greater than 0.</b>", ContentMode.HTML));
//			layout.setMargin(true);
//			enteredAmtTxt.setValue(null);
//			showNotificationAlert(layout);
//			return;
//		}
		
		if(calculatedTotal > minValue) {
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red;'>Total Amount should be lesser of Amount considered and Balance SI </b>", ContentMode.HTML));
			layout.setMargin(true);
			enteredAmtTxt.setValue("0");
			showNotificationAlert(layout);
			calculateTotal();
			table.setColumnFooter("approvedAmount", "0");
			return;
		}
	}
	
	private void showNotificationAlert(VerticalLayout layout) {
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setWidth("45%");
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	public Integer calculateTotal() {
		
		List<MedicalDecisionTableDTO> itemIconPropertyId = (List<MedicalDecisionTableDTO>) table.getItemIds();
		Integer total = 0;
		for (MedicalDecisionTableDTO dto : itemIconPropertyId) {
			String approvedAmount = dto.getApprovedAmount();
			total += SHAUtils.getFloatFromString(approvedAmount);
		}
		
		table.setColumnFooter("approvedAmount", String.valueOf(total));
		dummyField.setValue(String.valueOf(total));
		return total;
	}
	
	public void addBeanToList(MedicalDecisionTableDTO medicalDecisionDTO) {
    	data.addItem(medicalDecisionDTO);
    }
	
	 public List<MedicalDecisionTableDTO> getValues() {
		List<MedicalDecisionTableDTO> itemIds = (List<MedicalDecisionTableDTO>) this.table.getItemIds() ;
    	return itemIds;
	  }
	 
	 public void setVisibleApproveFields(Boolean isApproveVisible) {
			if(isApproveVisible) {
				table.setVisibleColumns(APPROVE_VISIBLE_COLUMNS);
			} else {
				table.setVisibleColumns(VISIBLE_COLUMNS);
			}
		}
	 
	 public boolean isValid()
		{
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			Collection<MedicalDecisionTableDTO> itemIds = (Collection<MedicalDecisionTableDTO>) table.getItemIds();
			for (MedicalDecisionTableDTO bean : itemIds) {
				
				if(!(bean.getReferenceNo() != null && bean.getReferenceNo().contains("Residual")) && !(bean.getApprovedAmount() != null && SHAUtils.isValidDouble(bean.getApprovedAmount()))){
					hasError = true;
					errorMessages.add("Please Enter Approved Amount </br>");
				}
			}
			return !hasError;
		}
		public List<String> getErrors()
		{
			return this.errorMessages;
		}
		
		public Boolean isValidApprovedAmt() {
			Integer total = SHAUtils.getFloatFromString(table.getColumnFooter("approvedAmount"));
			
			if(total == 0) {
				return false;
			}
			
			return true;
		}
	
}
