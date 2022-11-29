package com.shaic.claim.viewEarlierRodDetails.Table;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class RevisedDiagnosisPreauthViewTable extends ViewComponent {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private Map<DiagnosisProcedureTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DiagnosisProcedureTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<DiagnosisProcedureTableDTO> data = new BeanItemContainer<DiagnosisProcedureTableDTO>(DiagnosisProcedureTableDTO.class);
	
	private Table table;
	
	PreauthDTO bean;
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private TextField approvedAmountField;
	
	public Object[] VISIBLE_COLUMNS = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion","isAmbChargeApplicable","ambulanceCharge","amountConsidered", "packageAmt", "minimumAmountOfAmtconsideredAndPackAmt", /*"coPayPercentage"*/"copayPercentageAmount", "coPayAmount", "netAmount","amtWithAmbulanceCharge", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "restrictionSI","utilizedAmt", "availableAmout", "minimumAmount","reverseAllocatedAmt"};
	
	public Object[] VISIBLE_COLUMNS_NON_GMC = new Object[] {
			"diagOrProcedure", "description", "pedImpactOnDiagnosis","notPayingReason", "isAmbChargeApplicable","ambulanceCharge","amountConsidered", "packageAmt", "minimumAmountOfAmtconsideredAndPackAmt", /*"coPayPercentage"*/"copayPercentageAmount", "coPayAmount", "netAmount","amtWithAmbulanceCharge", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "restrictionSI","utilizedAmt", "availableAmout", "minimumAmount","reverseAllocatedAmt"};
	
	public Object[] SPECIFIC_PRODUCT_VISIBLE_COLUMNS = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion","isAmbChargeApplicable","ambulanceCharge","amountConsidered", "packageAmt", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "minimumAmountOfAmtconsideredAndPackAmt", /*"coPayPercentage"*/"copayPercentageAmount", "coPayAmount","netAmount","amtWithAmbulanceCharge", "restrictionSI","utilizedAmt", "availableAmout", "minimumAmount","reverseAllocatedAmt"};
	
	
	public Object[] SPECIFIC_PRODUCT_VISIBLE_COLUMNS_NON_GMC = new Object[] {
			"diagOrProcedure", "description", "pedImpactOnDiagnosis","notPayingReason", "isAmbChargeApplicable","ambulanceCharge","amountConsidered", "packageAmt", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "minimumAmountOfAmtconsideredAndPackAmt", /*"coPayPercentage"*/"copayPercentageAmount", "coPayAmount","netAmount","amtWithAmbulanceCharge", "restrictionSI","utilizedAmt", "availableAmout", "minimumAmount","reverseAllocatedAmt"};
	
	public Object[] CLAIM_REQUEST_VISIBLE_COLUMNS = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion", "packageAmt", "restrictionSI","utilizedAmt", "availableAmout", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt"};
	
	public Object[] CLAIM_REQUEST_VISIBLE_COLUMNS_NON_GMC = new Object[] {
			"diagOrProcedure", "description", "pedImpactOnDiagnosis","notPayingReason", "packageAmt", "restrictionSI","utilizedAmt", "availableAmout", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt"};
	
	public TextField dummyField;
	
	public void init(PreauthDTO bean,TextField approvedAmountField) {
		this.bean = bean;
		this.approvedAmountField = approvedAmountField;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.errorMessages = new ArrayList<String>();
		
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Sub limits, Package & SI Restriction Table", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("a")) {
			if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() == 2904){
				table.setVisibleColumns(VISIBLE_COLUMNS);
			}
			else{
				table.setVisibleColumns(VISIBLE_COLUMNS_NON_GMC);
			}
			
			
		} else if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("b")) {
			table.setVisibleColumns(SPECIFIC_PRODUCT_VISIBLE_COLUMNS);
		}

		table.setColumnHeader("diagOrProcedure", "Diagnosis / Procedure");
		table.setColumnHeader("description", "Description");
		
		if(table.getColumnHeader("pedOrExclusion") != null){
			table.setColumnHeader("pedOrExclusion", "PED / Exclusion Details");
		}	
		
		if(table.getColumnHeader("pedImpactOnDiagnosis") != null) {
			table.setColumnHeader("pedImpactOnDiagnosis", "PED Impact on Diagnosis");
		}
		
		if(table.getColumnHeader("notPayingReason") != null) {
			
			table.setColumnHeader("notPayingReason", "Reason for not paying");
		}
		
		table.setColumnHeader("pedOrExclusion", "PED / Exclusion Details");
		table.setColumnHeader("amountConsidered", "Amount Considered (A)");
		table.setColumnHeader("packageAmt", "Package Amt (B)");
		table.setColumnHeader("restrictionSI", "SI Restriction (J)");
		table.setColumnHeader("utilizedAmt", "SI Restriction Utilized Amt (K)");
		table.setColumnHeader("availableAmout", "SI Restriction Available Amt (L) (J-K)");
		table.setColumnHeader("minimumAmount", "Min of Column ( F, I, L) (M)");
		table.setColumnHeader("coPayPercentage", "Co-Pay % (D)");
		table.setColumnHeader("coPayAmount", "Co-Pay Amount (E) ");
		table.setColumnHeader("netAmount", "Net Amount (After Co-pay) (F)");
		table.setColumnHeader("subLimitAmount", "Sub Limit Amount (G) ");
		table.setColumnHeader("subLimitUtilAmount", "Sub Limit Utilized Amt (H) ");
		table.setColumnHeader("subLimitAvaliableAmt", "Sub Limit Available Amt (I)");
		table.setColumnHeader("netApprovedAmt", "Net Approved Amt (Min of I, L) (M)");
		table.setColumnHeader("remarks", "Remarks");
		table.setColumnHeader("minimumAmountOfAmtconsideredAndPackAmt", "Min of A, B (C)");
		table.setColumnHeader("reverseAllocatedAmt", "Apportion Final App Amt");
		table.setColumnHeader("copayPercentageAmount", "Co-Pay % (D)");
		
		table.setColumnHeader("isAmbChargeApplicable", "Ambulance Charge Applicable");
		table.setColumnHeader("ambulanceCharge", "Ambulance Charges");
		table.setColumnHeader("amtWithAmbulanceCharge", "Amount With Ambulance Charges");
		
		 if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("b")) {
			    table.setColumnHeader("diagOrProcedure", "Diagnosis / Procedure");
				table.setColumnHeader("description", "Description");
				if(table.getColumnHeader("pedOrExclusion") != null){
					table.setColumnHeader("pedOrExclusion", "PED / Exclusion Details");
				}
				table.setColumnHeader("amountConsidered", "Amount Considered (A)");
				table.setColumnHeader("packageAmt", "Package Amt (B)");
				table.setColumnHeader("restrictionSI", "SI Restriction (J)");
				table.setColumnHeader("utilizedAmt", "SI Restriction Utilized Amt (K)");
				table.setColumnHeader("availableAmout", "SI Restriction Available Amt (L) (J-K)");
				table.setColumnHeader("minimumAmount", " Net Approved amount (Min of Column I, L) (M)");
				table.setColumnHeader("coPayPercentage", "Co-Pay % (G)");
				table.setColumnHeader("coPayAmount", "Co-Pay Amount (H) ");
				table.setColumnHeader("netAmount", "Net Amount (After Co-pay) (I)");
				table.setColumnHeader("subLimitAmount", "Sub Limit Amount (C) ");
				table.setColumnHeader("subLimitUtilAmount", "Sub Limit Utilized Amt (D) ");
				table.setColumnHeader("subLimitAvaliableAmt", "Sub Limit Available Amt (E)");
				table.setColumnHeader("netApprovedAmt", "Net Approved Amt (Min of I, L) (M)");
				table.setColumnHeader("remarks", "Remarks ");
				table.setColumnHeader("minimumAmountOfAmtconsideredAndPackAmt", "Min of A, B, E (F)");
				table.setColumnHeader("copayPercentageAmount", "Co-Pay % (D)");
				
				table.setColumnHeader("isAmbChargeApplicable", "Ambulance Charge Applicable");
				table.setColumnHeader("ambulanceCharge", "Ambulance Charges");
				table.setColumnHeader("amtWithAmbulanceCharge", "Amount With Ambulance Charges");
				
		 }
		
		table.setEditable(true);
		
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		table.setFooterVisible(true);
		dummyField = new TextField();
		if(table.getColumnFooter("pedOrExclusion") != null){
			table.setColumnFooter("pedOrExclusion", "Total");
		}
		
		if(table.getColumnHeader("pedImpactOnDiagnosis") != null) {
			table.setColumnFooter("pedImpactOnDiagnosis", "Total");
		}
		layout.addComponent(table);
	}
	
	StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter() {
		private static final long serialVersionUID = -2154393632039317675L;

		protected java.text.NumberFormat getFormat(Locale locale) {
	        NumberFormat format = super.getFormat(locale);
	        format.setGroupingUsed(false);
	        return format;
	    };
	};
	
	public void setVisibleColumns() {
		if(this.bean.getNewIntimationDTO().getPolicy().getProductType().getKey().intValue() == 2904){
			this.table.setVisibleColumns(CLAIM_REQUEST_VISIBLE_COLUMNS);
		}
		else {
			this.table.setVisibleColumns(CLAIM_REQUEST_VISIBLE_COLUMNS_NON_GMC);
		}
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		/*private static final long serialVersionUID = -2192723245525925990L;*/

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			DiagnosisProcedureTableDTO diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(diagnosisProcedureTableDto) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(diagnosisProcedureTableDto, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(diagnosisProcedureTableDto);
			}
			if("isAmbChargeApplicable".equals(propertyId)) {
				CheckBox field = new CheckBox();
				field.setData(diagnosisProcedureTableDto);	
				tableRow.put("isAmbChargeApplicable", field);
				field.setEnabled(false);
				return field;
			}
			else if("ambulanceCharge".equals(propertyId)) {
				
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setConverter(plainIntegerConverter);
				field.setData(diagnosisProcedureTableDto);
				field.setEnabled(false);
				field.setWidth("100px");
				tableRow.put("ambulanceCharge", field);
				field.setEnabled(false);
//				consideredAmountListener(field,null);
//				field.addBlurListener(getAmbulanceAmountListener());
				
				field.setMaxLength(10);
				return field;
			}
			else if("amountConsidered".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(10);
				validator.extend(field);
				field.setConverter(plainIntegerConverter);
				field.setData(diagnosisProcedureTableDto);
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("amountConsidered", field);
//				consideredAmountListener(field,null);
				field.setMaxLength(10);
				//field.addBlurListener(getAmountConsiderListener());
				if(!diagnosisProcedureTableDto.getIsEnabled()) {
					field.setReadOnly(true);
				}
				
				return field;
			}else if("reverseAllocatedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(10);
				validator.extend(field);
				field.setConverter(plainIntegerConverter);
				field.setData(diagnosisProcedureTableDto);
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("reverseAllocatedAmt", field);
//				consideredAmountListener(field,null);
				field.setMaxLength(10);
				field.setEnabled(false);
				if(diagnosisProcedureTableDto.getNetApprovedAmt() == null){
					field.setValue("NA");
				}
				if(bean.getIsReverseAllocation()) {
//					field.setEnabled(true);
				}
//				if(!diagnosisProcedureTableDto.getIsEnabled()) {
//					field.setReadOnly(true);
//				}
//				
				return field;
			}else if(("minimumAmount").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("minimumAmount", field);
				calculateTotal();
				return field;
			} else if(("minimumAmountOfAmtconsideredAndPackAmt").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("minimumAmountOfAmtconsideredAndPackAmt", field);
				return field;
			}
			else if("coPayPercentage".equals(propertyId)){
				ComboBox field = new ComboBox();
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("coPayPercentage", field);
				addCoPayPercentage(field, diagnosisProcedureTableDto);
//				consideredAmountListener(null,field);
				field.setNullSelectionAllowed(false);
				field.setReadOnly(true);
				field.addValueChangeListener(copayListener());
				return field;
			}
			else if("coPayAmount".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("coPayAmount", field);
				return field;
			}
			else if("packageAmt".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("packageAmt", field);
				field.setReadOnly(true);
				return field;
			}
			else if("restrictionSI".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("restrictionSI", field);
				field.setReadOnly(true);
				return field;
			}
			else if("availableAmout".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("availableAmout", field);
				if(diagnosisProcedureTableDto.getRestrictionSI() != null && diagnosisProcedureTableDto.getRestrictionSI().equals(-1)) {
					field.setValue("NA");
				}
				field.setReadOnly(true);
				return field;
			}
			else if("netAmount".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("netAmount", field);
				return field;
			}
			else if("amtWithAmbulanceCharge".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("amtWithAmbulanceCharge", field);
				return field;
			}
			else if("subLimitAmount".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("subLimitAmount", field);
				return field;
			}
			
			else if("subLimitAvaliableAmt".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("subLimitAvaliableAmt", field);
				return field;
			}
			
			else if("netApprovedAmt".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("netApprovedAmt", field);
				return field;
			}
			else if ("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
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
	
	private void calculationMethodForMedicalDecision(Component component, Boolean isCopay) {
		DiagnosisProcedureTableDTO diagnosisProcedureTableDto = new DiagnosisProcedureTableDTO();
		if(isCopay) {
			ComboBox combo  = (ComboBox) component;
			diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)combo.getData();
		} else {
			TextField txtField  = (TextField) component;
			diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)txtField.getData();
		}
		
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(diagnosisProcedureTableDto);
		TextField packageAmtField = (TextField) hashMap.get("packageAmt");
		TextField selectedSIAmount = (TextField) hashMap.get("restrictionSI");
		TextField restrictionSIField = (TextField) hashMap.get("availableAmout");
		TextField minimumAmountField=(TextField)hashMap.get("minimumAmount");
		TextField amountConsidered = (TextField)hashMap.get("amountConsidered");
		TextField sublimitAvaliableField = (TextField)hashMap.get("subLimitAvaliableAmt");
		TextField sublimitAmtField = (TextField)hashMap.get("subLimitAmount");
		TextField netAmountField = (TextField)hashMap.get("netAmount");
		TextField minAmountOfAmtConsideredAndPackAmt = (TextField)hashMap.get("minimumAmountOfAmtconsideredAndPackAmt");
		Integer min = 0;
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("a")) {
			min = SHAUtils.getIntegerFromString(amountConsidered.getValue());
			if(SHAUtils.isValidInteger(packageAmtField.getValue())) {
				min = Math.min(SHAUtils.getIntegerFromString(amountConsidered.getValue()), SHAUtils.getIntegerFromString(packageAmtField.getValue()));
			}
		} else if (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("b") ){
			 min = SHAUtils.getIntegerFromString(amountConsidered.getValue());
			 min = findMinimumValuesForSpecificProduct(amountConsidered, packageAmtField, sublimitAmtField, sublimitAvaliableField);
		}
		if(minAmountOfAmtConsideredAndPackAmt != null) {
			minAmountOfAmtConsideredAndPackAmt.setReadOnly(false);
			minAmountOfAmtConsideredAndPackAmt.setValue(min.toString());
			minAmountOfAmtConsideredAndPackAmt.setReadOnly(true);
		}
		
		ComboBox coPayPercentage=(ComboBox)hashMap.get("coPayPercentage");
		SelectValue selected=(SelectValue)coPayPercentage.getValue();
		TextField coPayField=(TextField)hashMap.get("coPayAmount");
		Integer coPayAmount=0;
		
		if(!diagnosisProcedureTableDto.getIsPaymentAvailable()) {
			min = 0;
		}
		coPayAmount = calculateCopayPercentage(min, selected != null ? selected.getValue() : "0");
		if(coPayField != null) {
			coPayField.setReadOnly(false);
			coPayField.setValue(coPayAmount.toString());
			coPayField.setReadOnly(true);
		}
		
		Integer netAmount = min - coPayAmount;
		if(netAmountField != null) {
			netAmountField.setReadOnly(false);
			netAmountField.setValue(String.valueOf(netAmount));
			netAmountField.setReadOnly(true);
		}
		
			Integer minValue = 0;
			if(netAmountField != null) {
				if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("a")) {
					minValue = findMinimumValuesForCommon(netAmountField, restrictionSIField, selectedSIAmount, sublimitAmtField, sublimitAvaliableField);
				} else if (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("b") ){
					minValue = findSecondMinimumValuesForSpecificProduct(netAmountField, restrictionSIField, selectedSIAmount);
				}
			}
			
			
			if(minimumAmountField != null) {
				minimumAmountField.setReadOnly(false);
				minimumAmountField.setValue(minValue.toString());
				minimumAmountField.setReadOnly(true);
			}
			
			
			if(!diagnosisProcedureTableDto.getIsPaymentAvailable()) {
				if(minimumAmountField != null) {
					minimumAmountField.setReadOnly(false);
					minimumAmountField.setValue("0");
					minimumAmountField.setReadOnly(true);
				}
				
			}
			
			calculateTotal();
		
//			if(SHAUtils.getIntegerFromString(table.getColumnFooter("amountConsidered")) > SHAUtils.getIntegerFromString(this.bean.getAmountConsidered())) {
//				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Total Amount Considered Amt should be equal to Data Extraction Payable Amount. </b>", ContentMode.HTML));
//				layout.setMargin(true);
//				layout.setSpacing(true);
//				boolean readOnly = amountConsidered.isReadOnly();
//				
//				amountConsidered.setReadOnly(false);
//				amountConsidered.setValue("0");
//				amountConsidered.setReadOnly(readOnly);
//				calculationMethodForMedicalDecision(amountConsidered, false);
//				calculateTotal();
////				showErrorPopup(layout);
//			}
		
		
		
	}
	
	
	public void consideredAmountListener(TextField amountField,ComboBox coPayField){
		
		if(amountField != null){
			amountField.addBlurListener(new BlurListener() {
				
				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					if(component.getValue() == null){
						component.setValue("0");
					}
					calculationMethodForMedicalDecision(component, false);
					
				}

	

			});
		}
		
//		if(coPayField != null){
//			coPayField.addBlurListener(new BlurListener() {
//				
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void blur(BlurEvent event) {
//					ComboBox component = (ComboBox) event.getComponent();
//					DiagnosisProcedureTableDTO diagnosisProcedureTableDto=(DiagnosisProcedureTableDTO)component.getData();
//					HashMap<String, AbstractField<?>> hashMap = tableItem.get(diagnosisProcedureTableDto);
//					SelectValue selected=(SelectValue)component.getValue();
//					if(selected == null){
//						selected.setValue("0");
//					}
//					
//					TextField minimumAmountField=(TextField)hashMap.get("minimumAmount");
//					TextField coPayField=(TextField)hashMap.get("coPayAmount");
//					TextField netAmountField=(TextField)hashMap.get("netAmount");			
//					Integer coPayAmount=null;
//					Integer minimumAmount=null;
//					if(minimumAmountField.getValue() != null){
//						minimumAmount=SHAUtils.getIntegerFromStringWithComma(minimumAmountField.getValue());
//						coPayAmount=calculateCopayPercentage(minimumAmount, selected.getValue());
//						coPayField.setReadOnly(false);
//						coPayField.setValue(String.valueOf(coPayAmount));
//						coPayField.setReadOnly(true);
//					}
//					
//					Integer netAmount=minimumAmount-coPayAmount;
//					netAmountField.setReadOnly(false);
//					netAmountField.setValue(String.valueOf(netAmount));
//					netAmountField.setReadOnly(true);
//					
//					TextField sublimitAvaliableField=(TextField)hashMap.get("subLimitAvaliableAmt");
//					TextField approvedAmountField=(TextField)hashMap.get("netApprovedAmt");
//					Integer availableAmount=SHAUtils.getIntegerFromStringWithComma(sublimitAvaliableField.getValue());
//					Integer approvedAmount=findMinimumTwoValues(netAmount, availableAmount);
//					approvedAmountField.setReadOnly(false);
//					approvedAmountField.setValue(String.valueOf(approvedAmount));
//					approvedAmountField.setReadOnly(true);
//					
//					calculateTotal();
//                    
//				}
//			});
//		}
		
	}
	
	
	public ValueChangeListener copayListener(){
		ValueChangeListener listener = new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox component = (ComboBox) event.getProperty();
				DiagnosisProcedureTableDTO diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)component.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(diagnosisProcedureTableDto);
				TextField amountConsidered = (TextField)hashMap.get("amountConsidered");
				if(SHAUtils.isValidInteger(amountConsidered.getValue())) {
					calculationMethodForMedicalDecision(component, true);
				} else {
//					VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Please Enter Amount Considered. </b>", ContentMode.HTML));
//					layout.setMargin(true);
//					layout.setSpacing(true);
//					showErrorPopup(layout);
//					component.setValue(null);
				}
			}
		};
		
		return listener;
	}
	
	public BlurListener getAmountConsiderListener() {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				calculationMethodForMedicalDecision(component, false);
				
			}
		};
		return listener;
		
	}
	
	private void showErrorPopup(VerticalLayout layout) {
		layout.setMargin(true);
		layout.setSpacing(true);
		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
//		field.setValue(null);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createCustomBox("Error", layout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
	}
	
	public Integer calculateTotal() {
		
		List<DiagnosisProcedureTableDTO> itemIconPropertyId = (List<DiagnosisProcedureTableDTO>) table.getItemIds();
		Integer total = 0;
		Integer minTotal=0;
		Integer coPayTotal=0;
		Integer netAmtTotal=0;
		Integer netApprovedAmount = 0;
		for (DiagnosisProcedureTableDTO dto : itemIconPropertyId) {
		    Integer approvedAmount = dto.getAmountConsidered();
			total += approvedAmount != null ? approvedAmount : 0;
			Integer minimumAmt=dto.getMinimumAmount();
			minTotal +=  null != minimumAmt ? minimumAmt : 0;
			Integer coPayAmt = dto.getCoPayAmount();
			coPayTotal += null != coPayAmt ? coPayAmt : 0;
			Integer netAmt=dto.getNetAmount();
			netAmtTotal +=  null != netAmt ? netAmt : 0;
			Integer netAppAmt = dto.getReverseAllocatedAmt();
			netApprovedAmount +=  null != netAppAmt ? netAppAmt : 0;
		}
		table.setColumnFooter("amountConsidered", String.valueOf(total));
		table.setColumnFooter("minimumAmount", String.valueOf(minTotal));
		table.setColumnFooter("coPayAmount", String.valueOf(coPayTotal));
		table.setColumnFooter("netAmount", String.valueOf(netAmtTotal));
		table.setColumnFooter("reverseAllocatedAmt", String.valueOf(netApprovedAmount));
		if(minTotal<=netApprovedAmount){
			dummyField.setValue(String.valueOf(minTotal));
			approvedAmountField.setValue(String.valueOf(minTotal));
		}else{
			dummyField.setValue(String.valueOf(netApprovedAmount));
			approvedAmountField.setValue(String.valueOf(netApprovedAmount));
		}
		return total;
		
	}
	
	public Integer getTotalAmountConsidered() {
		return SHAUtils.getIntegerFromString(this.table.getColumnFooter("amountConsidered")) ;
	}
	
	public Integer findMinimumValues(TextField component,
			TextField packageAmtField, TextField restrictionSIField, TextField selectedSIAmount) {
		Integer minValue = 0;
		Integer enteredAmt = SHAUtils.getIntegerFromStringWithComma(component.getValue());
		Integer packageAmt = SHAUtils.getIntegerFromStringWithComma(packageAmtField.getValue());
		Integer restrictionSI = SHAUtils.getIntegerFromStringWithComma(restrictionSIField.getValue());
		int min = 0;
		if(SHAUtils.isValidInteger(packageAmtField.getValue()) && SHAUtils.isValidInteger(selectedSIAmount.getValue())) {
			min = Math.min(packageAmt, restrictionSI);
		} else if(SHAUtils.isValidInteger(packageAmtField.getValue())) {
			min = packageAmt;
		} else if(SHAUtils.isValidInteger(selectedSIAmount.getValue())) {
			min = restrictionSI;
		} else {
			min = -1;
		}
		
		minValue = min == -1 ? enteredAmt :  Math.min(enteredAmt, min);
		
		return minValue;
	}
	
	public Integer findMinimumValuesForSpecificProduct(TextField component,
			TextField packageAmtField, TextField sublimitAmt, TextField availableSublimit) {
		Integer minValue = 0;
		Integer enteredAmt = SHAUtils.getIntegerFromStringWithComma(component.getValue());
		Integer packageAmt = SHAUtils.getIntegerFromStringWithComma(packageAmtField.getValue());
		Integer availableSublimitAmt = SHAUtils.getIntegerFromStringWithComma(availableSublimit.getValue());
		int min = 0;
		List<Integer> amounts = new ArrayList<Integer>();
		if(SHAUtils.isValidInteger(packageAmtField.getValue())) {
			amounts.add(packageAmt);
		}
		if(SHAUtils.isValidInteger(sublimitAmt.getValue())) {
			amounts.add(availableSublimitAmt);
		}
		minValue = enteredAmt;
		if(!amounts.isEmpty()) {
			min = amounts.get(0);
			for (Integer amount : amounts) {
				if(amount < min) {
					min = amount;
				}
			}
		} else {
			min = -1;
		}
		minValue = min == -1 ? enteredAmt :  Math.min(enteredAmt, min);
		
		return minValue;
	}
	
	
	
	public Integer findSecondMinimumValuesForSpecificProduct(TextField netAmountField,TextField restrictionSIField, TextField selectionSIAmount) {
		Integer minValue = 0;
		Integer enteredAmt = SHAUtils.getIntegerFromStringWithComma(netAmountField.getValue());
		Integer restrictionSI = SHAUtils.getIntegerFromStringWithComma(restrictionSIField.getValue());
		int min = enteredAmt;
		if(SHAUtils.isValidInteger(selectionSIAmount.getValue())) {
			 min = Math.min(restrictionSI, min);
		}
		
		return min;
	}
	
	
	public Integer findMinimumValuesForCommon(TextField netAmountAftCopay, TextField restrictionSIField, TextField selectedSIAmount, TextField sublimitAmt, TextField availableSublimit) {
		Integer minValue = 0;
		
		Integer netAmountAftCopayAmt = SHAUtils.getIntegerFromStringWithComma(netAmountAftCopay.getValue());
		Integer restrictionSI = SHAUtils.getIntegerFromStringWithComma(restrictionSIField.getValue());
		Integer availableSublimitAmt = SHAUtils.getIntegerFromStringWithComma(availableSublimit.getValue());
		int min = 0;
		List<Integer> amounts = new ArrayList<Integer>();
		
		if(SHAUtils.isValidInteger(selectedSIAmount.getValue())) {
			amounts.add(restrictionSI);
		}
		if(SHAUtils.isValidInteger(sublimitAmt.getValue())) {
			amounts.add(availableSublimitAmt);
		}
		minValue = netAmountAftCopayAmt;
		if(!amounts.isEmpty()) {
			min = amounts.get(0);
			for (Integer amount : amounts) {
				if(amount < min) {
					min = amount;
				}
			}
		} else {
			min = -1;
		}
		minValue = min == -1 ? netAmountAftCopayAmt :  Math.min(netAmountAftCopayAmt, min);
		
		return minValue;
	}
	
	
	public Integer findMinimumTwoValues(Integer netAmount,Integer avaliableAmount){
		Integer minimumValue=Math.min(netAmount, avaliableAmount);
		return minimumValue;
	}
	
	public Integer calculateCopayPercentage(Integer considerAmt,String coPayPercentage){
		Float coPay=Float.valueOf(coPayPercentage);
		Float calculatedAmt = considerAmt * (coPay/100f);
		Integer roundedValue = Math.round(calculatedAmt);
		return roundedValue;
	}
	
	 public void addBeanToList(DiagnosisProcedureTableDTO diagnosisProcedureTableDTO) {
	    	data.addBean(diagnosisProcedureTableDTO);
	 }
	 
	 public void addList(List<DiagnosisProcedureTableDTO> diagnosisProcedureTableDTO) {
		 for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO2 : diagnosisProcedureTableDTO) {
			 data.addBean(diagnosisProcedureTableDTO2);
		 }
	 }
	 
	 public void addCoPayPercentage(ComboBox comboBox, DiagnosisProcedureTableDTO dto){
		   BeanItemContainer<SelectValue> coPayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		   
		    List<String> coPayPercentageValues = dto.getCoPayPercentageValues();
		    if(coPayPercentageValues == null || coPayPercentageValues.isEmpty()) {
		    	coPayPercentageValues = new ArrayList<String>();
		    	for (Double string : this.bean.getProductCopay()) {
		    		coPayPercentageValues.add(String.valueOf(string));
				}
		    }
		    Long i = 0l;
		    for (String string : coPayPercentageValues) {
		    	SelectValue value = new SelectValue();
		    	String[] copayWithPercentage = string.split("\\.");
				String copay = copayWithPercentage[0].trim();
		    	value.setId(Long.valueOf(copay));
		    	value.setValue(string);
		    	if(dto.getCoPayPercentage() == null) {
		    		if(i.equals(0l)) {
			    		dto.setCoPayPercentage(value);
			    	}
		    	}
		    	coPayContainer.addBean(value);
		    	i++;
			}
		    
			comboBox.setContainerDataSource(coPayContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
			
			
	 }
	 
	 public Integer getTotalPayableAmount(){
			return SHAUtils.getIntegerFromString(this.table.getColumnFooter("minimumAmount")) ;
		}
	 
	 @SuppressWarnings("unchecked")
	public List<DiagnosisProcedureTableDTO> getValues() {
		List<DiagnosisProcedureTableDTO> itemIds = (List<DiagnosisProcedureTableDTO>) this.table.getItemIds() ;
    	return itemIds;
	}
	 
	 public void removeAllItems(){
		 	if(table != null){
		 		table.removeAllItems();
		 	}
		}

}


