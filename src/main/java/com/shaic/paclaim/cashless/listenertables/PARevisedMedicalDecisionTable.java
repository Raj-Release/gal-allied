package com.shaic.paclaim.cashless.listenertables;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PARevisedMedicalDecisionTable extends ViewComponent {

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
	
	private Validator validator;
	
	public Object[] VISIBLE_COLUMNS = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion","isAmbChargeApplicable","ambulanceCharge","amountConsidered", "packageAmt", "minimumAmountOfAmtconsideredAndPackAmt","coPayType","coPayPercentage", "coPayAmount", "netAmount","amtWithAmbulanceCharge", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "restrictionSI","utilizedAmt", "availableAmout", "minimumAmount"};
	
	public Object[] SPECIFIC_PRODUCT_VISIBLE_COLUMNS = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion","isAmbChargeApplicable","ambulanceCharge","amountConsidered", "packageAmt", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "minimumAmountOfAmtconsideredAndPackAmt","coPayType","coPayPercentage", "coPayAmount","netAmount", "amtWithAmbulanceCharge", "restrictionSI","utilizedAmt", "availableAmout", "minimumAmount"};
	
	
	public Object[] CLAIM_REQUEST_VISIBLE_COLUMNS = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion", "packageAmt", "restrictionSI","utilizedAmt", "availableAmout", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt"};
	
	public Object[] VISIBLE_COLUMNS_WITH_REVERSE = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion","isAmbChargeApplicable","ambulanceCharge","amountConsidered", "agreedPackageAmt", "packageAmt","reasonForPkgChange", "minimumAmountOfAmtconsideredAndPackAmt", "coPayType","coPayPercentage", "coPayAmount", "netAmount", "amtWithAmbulanceCharge","subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "restrictionSI","utilizedAmt", "availableAmout", "minimumAmount", "reverseAllocatedAmt"};
	
	public Object[] SPECIFIC_PRODUCT_VISIBLE_COLUMNS_WITH_REVERSE = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion","isAmbChargeApplicable","ambulanceCharge","amountConsidered", "agreedPackageAmt", "packageAmt","reasonForPkgChange", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "minimumAmountOfAmtconsideredAndPackAmt", "coPayType","coPayPercentage", "coPayAmount","netAmount", "amtWithAmbulanceCharge", "restrictionSI","utilizedAmt", "availableAmout", "minimumAmount", "reverseAllocatedAmt"};
	
	
	public Object[] CLAIM_REQUEST_VISIBLE_COLUMNS_WITH_REVERSE = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion", "packageAmt", "restrictionSI","utilizedAmt", "availableAmout", "subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "reverseAllocatedAmt"};
	
	public TextField dummyField;
	public TextField ambulanceChangeField;
	
	private TextField totalReverseAmtField;
	
	private HorizontalLayout hLayout;

	private VerticalLayout layout;
	
	private Integer cAmount = 0;
	
	private String presenterString;
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.errorMessages = new ArrayList<String>();
		
		layout = new VerticalLayout();
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	public void setRowColor(final List<Long> keys, final Boolean isDiagnosis){
		for (Long key : keys) {
			if(key != null) {
				ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds()); 
				final Object selectedRowId = getSelectedRowId(itemIds , key, isDiagnosis);
				table.setCellStyleGenerator(new CellStyleGenerator() {
					
					@Override
					public String getStyle(Table source, Object itemId, Object propertyId) {
						DiagnosisProcedureTableDTO diagnosisDetailsDTO = (DiagnosisProcedureTableDTO)selectedRowId;
						Long key1 = 0l;
						if(diagnosisDetailsDTO.getDiagnosisDetailsDTO() != null) {
							key1 = diagnosisDetailsDTO.getDiagnosisDetailsDTO().getKey();
						} else if(diagnosisDetailsDTO.getProcedureDTO() != null) {
							key1 = diagnosisDetailsDTO.getProcedureDTO().getKey();
						}
						diagnosisDetailsDTO = (DiagnosisProcedureTableDTO)itemId;
						long key2 = 0l;
						if(diagnosisDetailsDTO.getDiagnosisDetailsDTO() != null) {
							key2 = diagnosisDetailsDTO.getDiagnosisDetailsDTO().getKey() != null ? diagnosisDetailsDTO.getDiagnosisDetailsDTO().getKey() : 0l;
						} else if (diagnosisDetailsDTO.getProcedureDTO() != null) {
							key2 = diagnosisDetailsDTO.getProcedureDTO().getKey() != null ? diagnosisDetailsDTO.getProcedureDTO().getKey() : 0l;
							 diagnosisDetailsDTO.getProcedureDTO().getKey();
						}
						
						if(key1 == key2){
							return "select";
						} else {
							if(keys.contains(key2)){
								return "select";
							}else{
							return "none";
							}
						}
						
					}
				});
			}
			
		}
		
		
	}

	private Object getSelectedRowId( ArrayList<Object> ids, Long key,  Boolean isDiagnosis){
		
		for(Object id:ids){
			DiagnosisProcedureTableDTO diagnosisDetailsDTO = (DiagnosisProcedureTableDTO)id;
			Long key1 = 0l;
			if(diagnosisDetailsDTO.getDiagnosisDetailsDTO() != null) {
				key1 = diagnosisDetailsDTO.getDiagnosisDetailsDTO().getKey();
			} else if(diagnosisDetailsDTO.getProcedureDTO() != null) {
				key1 = diagnosisDetailsDTO.getProcedureDTO().getKey();
			}
			if(key1 != null && key != null && key1.equals(key)) {
				return id;
			}
		}
		
		return null;
		
	}
	
	public void setReverseAllocationColumn(String amountConsidered) {
//		this.table.setVisibleColumns(VISIBLE_COLUMNS_WITH_REVERSE);
		table.setColumnHeader("reverseAllocatedAmt", "Apportion Final App Amt");
		List<DiagnosisProcedureTableDTO> itemIconPropertyId = (List<DiagnosisProcedureTableDTO>) table.getItemIds();
		for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : itemIconPropertyId) {
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(diagnosisProcedureTableDTO);
			if(hashMap != null && hashMap.get("reverseAllocatedAmt") != null) {
				TextField reverseAllocatedField = (TextField) hashMap.get("reverseAllocatedAmt");
				reverseAllocatedField.setEnabled(true);
			}
			
		}
		createReverseAmtConsideredLabel(amountConsidered);
	}
	
	public void deleteReverseAllocation() {
//		this.table.setVisibleColumns(VISIBLE_COLUMNS_WITH_REVERSE);
		table.setColumnHeader("reverseAllocatedAmt", "Apportion Final App Amt");
		List<DiagnosisProcedureTableDTO> itemIconPropertyId = (List<DiagnosisProcedureTableDTO>) table.getItemIds();
		for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : itemIconPropertyId) {
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(diagnosisProcedureTableDTO);
			if(hashMap != null && hashMap.get("reverseAllocatedAmt") != null) {
				TextField reverseAllocatedField = (TextField) hashMap.get("reverseAllocatedAmt");
				reverseAllocatedField.setEnabled(false);
			}
			
		}
		if(layout != null ) {
			if(hLayout != null) {
				layout.removeComponent(hLayout);
			}
		}
	}
	
	public void createReverseAmtConsideredLabel(String amountConsidered) {
		if(layout != null ) {
			if(hLayout != null) {
				layout.removeComponent(hLayout);
			}
			
			totalReverseAmtField = new TextField("Final Approval Amt (B - Balance SI after Co-pay)");
			totalReverseAmtField.setValue(amountConsidered);
			totalReverseAmtField.setReadOnly(true);
			totalReverseAmtField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			FormLayout formLayout = new FormLayout(totalReverseAmtField);
			hLayout = new HorizontalLayout(formLayout);
			hLayout.setWidth("100%");
			hLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_RIGHT);
			layout.addComponent(hLayout, 0);
		}
	}
	
	public String getReverseAllocationAmountConsidered() {
		if(totalReverseAmtField != null) {
			return totalReverseAmtField.getValue();
		}
		return null;
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Sub limits, Package & SI Restriction Table", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("a")) {
			table.setVisibleColumns(VISIBLE_COLUMNS_WITH_REVERSE);
		} else if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("b")) {
			table.setVisibleColumns(SPECIFIC_PRODUCT_VISIBLE_COLUMNS_WITH_REVERSE);
		}

		table.setColumnHeader("diagOrProcedure", "Diagnosis / Procedure");
		table.setColumnHeader("description", "Description");
		table.setColumnHeader("pedOrExclusion", "PED / Exclusion Details");
		table.setColumnHeader("amountConsidered", "Amount Considered (A)");
		table.setColumnHeader("packageAmt", "Package Amt (B)");
		table.setColumnHeader("restrictionSI", "SI Restriction (J)");
		table.setColumnHeader("utilizedAmt", "SI Restriction Utilized Amt (K)");
		table.setColumnHeader("availableAmout", "SI Restriction Available Amt (L) (J-K)");
		table.setColumnHeader("minimumAmount", "Min of Column ( F, I, L) (M)");
		table.setColumnHeader("coPayType", "Co-Pay Type");
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
		
		table.setColumnHeader("isAmbChargeApplicable", "Ambulance Charge Applicable");
		table.setColumnHeader("ambulanceCharge", "Ambulance Charges");
		table.setColumnHeader("amtWithAmbulanceCharge", "Amount With Ambulance Charges");
		//GLX2020047
		table.setColumnHeader("agreedPackageAmt", "Agreed Package Rate");
		table.setColumnHeader("reasonForPkgChange", "Reason for amount change");
		
		 if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("b")) {
			    table.setColumnHeader("diagOrProcedure", "Diagnosis / Procedure");
				table.setColumnHeader("description", "Description");
				table.setColumnHeader("pedOrExclusion", "PED / Exclusion Details");
				table.setColumnHeader("amountConsidered", "Amount Considered (A)");
				table.setColumnHeader("packageAmt", "Package Amt (B)");
				table.setColumnHeader("restrictionSI", "SI Restriction (J)");
				table.setColumnHeader("utilizedAmt", "SI Restriction Utilized Amt (K)");
				table.setColumnHeader("availableAmout", "SI Restriction Available Amt (L) (J-K)");
				table.setColumnHeader("minimumAmount", " Net Approved amount (Min of Column I, L) (M)");
				table.setColumnHeader("coPayType", "Co-Pay Type");
				table.setColumnHeader("coPayPercentage", "Co-Pay % (G)");
				table.setColumnHeader("coPayAmount", "Co-Pay Amount (H) ");
				table.setColumnHeader("netAmount", "Net Amount (After Co-pay) (I)");
				table.setColumnHeader("subLimitAmount", "Sub Limit Amount (C) ");
				table.setColumnHeader("subLimitUtilAmount", "Sub Limit Utilized Amt (D) ");
				table.setColumnHeader("subLimitAvaliableAmt", "Sub Limit Available Amt (E)");
				table.setColumnHeader("netApprovedAmt", "Net Approved Amt (Min of I, L) (M)");
				table.setColumnHeader("remarks", "Remarks ");
				table.setColumnHeader("minimumAmountOfAmtconsideredAndPackAmt", "Min of A, B, E (F)");
				table.setColumnHeader("reverseAllocatedAmt", "Apportion Final App Amt");
				
				table.setColumnHeader("isAmbChargeApplicable", "Ambulance</br>Charge</br>Applicable");
				table.setColumnHeader("ambulanceCharge", "Ambulance</br>Charges");
				table.setColumnHeader("amtWithAmbulanceCharge", "Amount With</br>Ambulance</br>Charges");
				
		 }
		 
		 //GLX2020047
		 if(presenterString != null && (SHAConstants.PA_BILLING_HOSP.equalsIgnoreCase(presenterString) ||
				 SHAConstants.PA_FINANCIAL_HOSP.equalsIgnoreCase(presenterString) ||
				 SHAConstants.PA_CLAIM_REQUEST_HOSP.equalsIgnoreCase(presenterString))){
			 table.removeContainerProperty("agreedPackageAmt");
			 table.removeContainerProperty("reasonForPkgChange");
		 }
		
		table.setEditable(true);
		
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		table.setFooterVisible(true);
		if(dummyField == null){
			dummyField = new TextField();
		}
		if(ambulanceChangeField == null){
			ambulanceChangeField = new TextField();
		}
		table.setColumnFooter("pedOrExclusion", "Total");
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
		this.table.setVisibleColumns(CLAIM_REQUEST_VISIBLE_COLUMNS_WITH_REVERSE);
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
				field.addValueChangeListener(ambulanceApplicableListener());
				if(diagnosisProcedureTableDto.getIsAmbulanceEnable()){
					field.setEnabled(true);
				}else{
					field.setEnabled(false);
					field.setValue(false);
				}
				
				if(diagnosisProcedureTableDto.getIsDeletedOne()) {
					field.setEnabled(false);
					field.setValue(false);
				}
				
				return field;
			}
			else if("ambulanceCharge".equals(propertyId)) {
				
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(10);
				validator.extend(field);
				field.setConverter(plainIntegerConverter);
				field.setData(diagnosisProcedureTableDto);
				field.setEnabled(false);
				field.setWidth("100px");
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("ambulanceCharge", field);
//				consideredAmountListener(field,null);
//				field.addBlurListener(getAmbulanceAmountListener());
//				if(diagnosisProcedureTableDto.getAmbulanceCharge() != null && diagnosisProcedureTableDto.getAmbulanceCharge() >= 0) {
//					if(diagnosisProcedureTableDto.getIsAmbChargeApplicable()){
//						calculationMethodForMedicalDecision(field, false);
//					}
//					
//				}
				field.addBlurListener(getAmountConsiderListener());
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
				field.addBlurListener(getAmountConsiderListener());
				if(!diagnosisProcedureTableDto.getIsEnabled()) {
					field.setReadOnly(true);
				}
				if(diagnosisProcedureTableDto.getIsDeletedOne()) {
					field.setEnabled(false);
					field.setReadOnly(false);
					field.setValue("0");
					field.setReadOnly(true);
				}
				return field;
			} else if("reverseAllocatedAmt".equals(propertyId)) {
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
				if(bean.getIsReverseAllocation()) {
					field.setEnabled(true);
				}
				
				field.addBlurListener(getReverseAllocationListener());
				if(diagnosisProcedureTableDto.getReverseAllocatedAmt() > 0) {
					reverseAmtColumnValidation(field);
				}
//				if(!diagnosisProcedureTableDto.getIsEnabled()) {
//					field.setReadOnly(true);
//				}
//				
				if(diagnosisProcedureTableDto.getIsDeletedOne()) {
					field.setEnabled(false);
				}
				return field;
			}
			else if(("minimumAmount").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("minimumAmount", field);
				if(diagnosisProcedureTableDto.getAmountConsidered() != null && diagnosisProcedureTableDto.getAmountConsidered() >= 0) {
					TextField component = (TextField) tableRow.get("amountConsidered");
					if(component != null) {
						calculationMethodForMedicalDecision(component, false);
					}
					
				}
				return field;
			} else if(("minimumAmountOfAmtconsideredAndPackAmt").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("minimumAmountOfAmtconsideredAndPackAmt", field);
				return field;
			}else if("coPayType".equals(propertyId)){
				ComboBox field = new ComboBox();
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("coPayType", field);
				field.setNullSelectionAllowed(false);				
				if(null != bean.getNewIntimationDTO().getIsJioPolicy() && bean.getNewIntimationDTO().getIsJioPolicy()){
					
					if(diagnosisProcedureTableDto.getDiagnosisDetailsDTO() != null && diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getCoPayTypeId() != null){
						diagnosisProcedureTableDto.setCoPayType(diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getCoPayTypeId() );
						field.setValue(diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getCoPayTypeId() );
					}else if(diagnosisProcedureTableDto.getProcedureDTO() != null && diagnosisProcedureTableDto.getProcedureDTO().getCoPayTypeId() != null){
						diagnosisProcedureTableDto.setCoPayType(diagnosisProcedureTableDto.getProcedureDTO().getCoPayTypeId());
						field.setValue(diagnosisProcedureTableDto.getProcedureDTO().getCoPayTypeId() );
						
					}
					addCopayTypeValues(field);
					
					addCopayPercentageBasedOnCopayType(field,diagnosisProcedureTableDto.getCoPayType());

					field.addValueChangeListener(copayTypeValueChangeListener());
					field.setVisible(true);
				}else{
					field.setVisible(false);
				}
				
				return field;
			}
			else if("coPayPercentage".equals(propertyId)){
				ComboBox field = new ComboBox();
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("coPayPercentage", field);
				addCoPayPercentage(field, diagnosisProcedureTableDto);
//				consideredAmountListener(null,field);
				field.setNullSelectionAllowed(false);
				field.addValueChangeListener(copayListener());
				field.setEnabled(false);
				return field;
			}
			else if("coPayAmount".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("coPayAmount", field);
				return field;
			}
			else if("packageAmt".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("packageAmt", field);
				field.setReadOnly(true);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
				//GLX2020047
				if(diagnosisProcedureTableDto.getAgreedPackageAmt() != null && diagnosisProcedureTableDto.getProcedureDTO() != null){
					field.setReadOnly(false);
					field.setEnabled(true);
				}
				field.addBlurListener(getPkgAmtListener());
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
			}else if("amtWithAmbulanceCharge".equals(propertyId)){
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
			} else if("agreedPackageAmt".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("agreedPackageAmt", field);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
				return field;
			}else if("reasonForPkgChange".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				//field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(500);
				field.setEnabled(false);
				field.setData(diagnosisProcedureTableDto);
				if(diagnosisProcedureTableDto.getProcedureDTO() != null){
					field.setEnabled(true);
				}
				tableRow.put("reasonForPkgChange", field);
				handleEnter(field, null);
				field.addBlurListener(getPkgAmtChangeReasonListener(field));
				field.setDescription("Click the Text Box and Press F8 For Detailed Popup");
				
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
	
	private void reverseAmtColumnValidation(TextField component) {
		if(component.getData() != null) {
			DiagnosisProcedureTableDTO diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)component.getData();
			if(diagnosisProcedureTableDto.getMinimumAmount() < SHAUtils.getIntegerFromStringWithComma(component.getValue())) {
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Allocation amount should not exceed the Min of Column (F,I,L) (M) </b>", ContentMode.HTML));
				layout.setMargin(true);
				layout.setSpacing(true);
				showErrorPopup(layout);
				component.setValue("0");
			}
		}
		
		calculateTotal(true);
	}
	
	private void calculationMethodForMedicalDecision(Component component, Boolean isCopay) {
		DiagnosisProcedureTableDTO diagnosisProcedureTableDto = new DiagnosisProcedureTableDTO();
		if(isCopay) {
			ComboBox combo  = (ComboBox) component;
			diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)combo.getData();
		} else if(component instanceof CheckBox){
			CheckBox checkbox = (CheckBox) component;
			diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)checkbox.getData();
		}else {
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
		TextField reverseAllocatedAmtField = (TextField)hashMap.get("reverseAllocatedAmt");
		CheckBox ambulanceApplicable = (CheckBox) hashMap.get("isAmbChargeApplicable");
		TextField amtWithAmbulance = (TextField)hashMap.get("amtWithAmbulanceCharge");
		TextField ambulanceCharge = (TextField)hashMap.get("ambulanceCharge");
		Integer min = 0;
		
		Boolean isAmbulanceCharge = ambulanceApplicable != null ? ambulanceApplicable.getValue() != null ? ambulanceApplicable.getValue() : false : false;
		
		if(ambulanceCharge != null && ambulanceCharge.getValue() != null && isAmbulanceCharge){
			
			ambulanceCharge.setEnabled(true);
			
		}else if(ambulanceCharge != null){
			ambulanceCharge.setEnabled(false);
			ambulanceCharge.setValue("0");
			
		}
		
		if(isAmbulanceCharge){
			Boolean ambulanceAmtValidation = ambulanceAmtValidation(ambulanceCharge);
			if(! ambulanceAmtValidation){
				ambulanceCharge.setValue("0");
			}
		}
		
		if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("a")) {
			if(amountConsidered != null){
				min = SHAUtils.getIntegerFromString(amountConsidered.getValue());
				if(SHAUtils.isValidInteger(packageAmtField.getValue()) && !packageAmtField.getValue().equals("0")) {
					min = Math.min(SHAUtils.getIntegerFromString(amountConsidered.getValue()), SHAUtils.getIntegerFromString(packageAmtField.getValue()));
				}
			}
		} else if (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("b") ){
			if(amountConsidered != null){
				 min = SHAUtils.getIntegerFromString(amountConsidered.getValue());
				 min = findMinimumValuesForSpecificProduct(amountConsidered, packageAmtField, sublimitAmtField, sublimitAvaliableField);
			}
		}
		if(minAmountOfAmtConsideredAndPackAmt != null) {
			minAmountOfAmtConsideredAndPackAmt.setReadOnly(false);
			minAmountOfAmtConsideredAndPackAmt.setValue(min.toString());
			minAmountOfAmtConsideredAndPackAmt.setReadOnly(true);
		}
		
		ComboBox coPayPercentage=(ComboBox)hashMap.get("coPayPercentage");
		SelectValue selected = null;
		if(coPayPercentage != null){
			selected=(SelectValue)coPayPercentage.getValue();
		}
		
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

		if(ambulanceCharge != null && ambulanceCharge.getValue() != null && amtWithAmbulance != null){
			Integer ambEnteredAmount = SHAUtils.getIntegerFromStringWithComma(ambulanceCharge.getValue());
			Integer ambulanceAfterNetAmt = netAmount + ambEnteredAmount;
			
			if(! isAmbulanceCharge){
				ambulanceAfterNetAmt = 0;
			}

			amtWithAmbulance.setReadOnly(false);
			amtWithAmbulance.setValue(ambulanceAfterNetAmt.toString());
			amtWithAmbulance.setReadOnly(true);

		}
		
			diagnosisProcedureTableDto.setPostHospitalizationAmt(min);
			if(sublimitAmtField != null && SHAUtils.isValidInteger(sublimitAmtField.getValue())) {
				diagnosisProcedureTableDto.setPostHospitalizationAmt(Math.min(min, SHAUtils.getIntegerFromStringWithComma(sublimitAvaliableField.getValue())));
			}
			Integer minValue = 0;
			if(netAmountField != null) {
				if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("a")) {
					
					if(isAmbulanceCharge){
						minValue = findMinimumValuesForCommon(amtWithAmbulance, restrictionSIField, selectedSIAmount, sublimitAmtField, sublimitAvaliableField);
					}else{
						minValue = findMinimumValuesForCommon(netAmountField, restrictionSIField, selectedSIAmount, sublimitAmtField, sublimitAvaliableField);
					}
					
					
				} else if (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("b") ){
					if(isAmbulanceCharge){
						minValue = findSecondMinimumValuesForSpecificProduct(amtWithAmbulance, restrictionSIField, selectedSIAmount);
					}else{
						minValue = findSecondMinimumValuesForSpecificProduct(netAmountField, restrictionSIField, selectedSIAmount);
					}
				}
			}
			
			
			
			if(diagnosisProcedureTableDto.getDiagnosisDetailsDTO() != null) {
				if(diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitName() != null && diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitName().getName() != null && diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitName().getName().toLowerCase().equalsIgnoreCase(SHAConstants.DIALYSIS)) {
					if(!bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_ANOTHER_POLICY)) {
						minValue = Math.min(minValue, (SHAUtils.getIntegerFromString(diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSittingsInput()) * bean.getSittingsAmount())) ;
					}
					 
				}
			} else if(diagnosisProcedureTableDto.getProcedureDTO() != null) {
				if(diagnosisProcedureTableDto.getProcedureDTO().getSublimitName() != null && diagnosisProcedureTableDto.getProcedureDTO().getSublimitName().getName() != null &&  diagnosisProcedureTableDto.getProcedureDTO().getSublimitName().getName().equalsIgnoreCase(SHAConstants.DIALYSIS)) {
					if(!bean.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.DIABETES_INDIVIDUAL_ANOTHER_POLICY)) {
						minValue = Math.min(minValue, (SHAUtils.getIntegerFromString(diagnosisProcedureTableDto.getProcedureDTO().getSittingsInput()) * bean.getSittingsAmount())) ;
					}
					 
				}
			}
			
			if(!diagnosisProcedureTableDto.getIsPaymentAvailable()) {
				minValue = 0;
			}
			
			if(minimumAmountField != null) {
				minimumAmountField.setReadOnly(false);
				minimumAmountField.setValue(minValue.toString());
				
				diagnosisProcedureTableDto.setMinimumAmount(minValue);
				minimumAmountField.setReadOnly(true);
				
			}
			if(reverseAllocatedAmtField != null) {
				if(diagnosisProcedureTableDto.getReverseAllocatedAmt() != null && (diagnosisProcedureTableDto.getReverseAllocatedAmt().equals(minValue) || !bean.getIsReverseAllocation())) {
					reverseAllocatedAmtField.setValue(minValue.toString());
					diagnosisProcedureTableDto.setReverseAllocatedAmt(minValue);
				}
				
			} else if(!bean.getIsReverseAllocation()){
				diagnosisProcedureTableDto.setReverseAllocatedAmt(minValue);
			}
			
			if(!diagnosisProcedureTableDto.getIsPaymentAvailable()) {
				if(minimumAmountField != null) {
					minimumAmountField.setReadOnly(false);
					minimumAmountField.setValue("0");
					
					diagnosisProcedureTableDto.setMinimumAmount(0);
					minimumAmountField.setReadOnly(true);
				}
				if(reverseAllocatedAmtField != null) {
					reverseAllocatedAmtField.setValue("0");
					diagnosisProcedureTableDto.setReverseAllocatedAmt(0);
				}
			}
			
			calculateTotal(false);
		
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
	
	public ValueChangeListener ambulanceApplicableListener(){
		ValueChangeListener listener = new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
//				CheckBox component = (CheckBox) event.getProperty();
//				DiagnosisProcedureTableDTO diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)component.getData();
//				HashMap<String, AbstractField<?>> hashMap = tableItem.get(diagnosisProcedureTableDto);
//				TextField ambulanceCharge = (TextField)hashMap.get("ambulanceCharge");
//				
//				if(ambulanceCharge != null && component.getValue() != null && component.getValue()){
//					
//					ambulanceCharge.setEnabled(true);
//					
//				}else if(ambulanceCharge != null){
//					ambulanceCharge.setEnabled(false);
//					ambulanceCharge.setValue("0");
//				}
//
//			}
				CheckBox component = (CheckBox) event.getProperty();
				calculationMethodForMedicalDecision(component, false);
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
	
	
	public BlurListener getAmbulanceAmountListener() {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				
				DiagnosisProcedureTableDTO diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)component.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(diagnosisProcedureTableDto);
				TextField ambulanceCharge = (TextField)hashMap.get("ambulanceCharge");
				TextField netAmount = (TextField)hashMap.get("netAmount");
				TextField amtWithAmbulance = (TextField)hashMap.get("amtWithAmbulanceCharge");
				
				
				if(ambulanceCharge != null && ambulanceCharge.getValue() != null && netAmount != null && netAmount.getValue() != null){
					
					String ambulanceEnterAmt = ambulanceCharge.getValue();
					Integer ambulanceAmt = SHAUtils.getIntegerFromStringWithComma(ambulanceEnterAmt);
					
					String netAmountAfterCopay = netAmount.getValue();
					Integer netApprovedAmt = SHAUtils.getIntegerFromStringWithComma(netAmountAfterCopay);
					
					Integer totalAmt = netApprovedAmt + ambulanceAmt;
					amtWithAmbulance.setReadOnly(false);
					amtWithAmbulance.setValue(totalAmt.toString());
					amtWithAmbulance.setReadOnly(true);
					
				}
				
				
			}
		};
		return listener;
		
	}
	
	
	
	public BlurListener getReverseAllocationListener() {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				reverseAmtColumnValidation(component);
			}
		};
		return listener;
		
	}
	
	private void showErrorPopup(VerticalLayout layout) {
		layout.setMargin(true);
		layout.setSpacing(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
//		field.setValue(null);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
public Integer calculateTotal(Boolean isReverseAllocation) {
		
		List<DiagnosisProcedureTableDTO> itemIconPropertyId = (List<DiagnosisProcedureTableDTO>) table.getItemIds();
		Integer total = 0;
		Integer minTotal=0;
		Integer coPayTotal=0;
		Integer netAmtTotal=0;
		Integer reverseAllocationTotal = 0;
		 Integer ambulanceTotalAmt = 0;
		for (DiagnosisProcedureTableDTO dto : itemIconPropertyId) {
		    Integer approvedAmount = dto.getAmountConsidered() != null ? dto.getAmountConsidered() : 0;
			total += approvedAmount != null ? approvedAmount : 0;
			Integer minimumAmt=dto.getMinimumAmount();
			minTotal +=  null != minimumAmt ? minimumAmt : 0;
			Integer coPayAmt = dto.getCoPayAmount();
			coPayTotal += null != coPayAmt ? coPayAmt : 0;
			Integer netAmt=dto.getNetAmount();
			netAmtTotal +=  null != netAmt ? netAmt : 0;
			Integer reverseAmt = dto.getReverseAllocatedAmt();
			reverseAllocationTotal += null != reverseAmt ? reverseAmt : 0;
			ambulanceTotalAmt += dto.getAmbulanceCharge() != null ? dto.getAmbulanceCharge() : 0;
			
			
		}
		table.setColumnFooter("amountConsidered", String.valueOf(total));
		table.setColumnFooter("minimumAmount", String.valueOf(minTotal));
		table.setColumnFooter("coPayAmount", String.valueOf(coPayTotal));
		table.setColumnFooter("netAmount", String.valueOf(netAmtTotal));
		table.setColumnFooter("reverseAllocatedAmt", String.valueOf(reverseAllocationTotal));
		table.setColumnFooter("ambulanceCharge", String.valueOf(ambulanceTotalAmt));
//		if(!minTotal.equals(reverseAllocationTotal)) {
//			
//		}
//		dummyField.setValue(String.valueOf(minTotal));
		ambulanceChangeField.setValue(String.valueOf(ambulanceTotalAmt));
		dummyField.setValue(String.valueOf(reverseAllocationTotal));
		return total;
	}


   public Boolean ambulanceAmtValidation(TextField amtField){
	   
	   List<DiagnosisProcedureTableDTO> itemIconPropertyId = (List<DiagnosisProcedureTableDTO>) table.getItemIds();
	   Integer ambulanceTotalAmt = 0;
	   for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : itemIconPropertyId) {
		   if(! diagnosisProcedureTableDTO.getIsDeletedOne()){
			   ambulanceTotalAmt += diagnosisProcedureTableDTO.getAmbulanceCharge();
		   }
	   }
	   
	   if(this.bean.getAmbulanceLimitAmount() != null && this.bean.getAmbulanceLimitAmount().intValue() >= ambulanceTotalAmt){
		   table.setColumnFooter("ambulanceCharge", String.valueOf(ambulanceTotalAmt));
	   }else{
		   
//		   getErrorMessage("Amount entered against Ambulance charge exceeded");
//		   
//		   if(amtField != null){
//			   amtField.setValue("0");
//		   }
	   }

	   return true;
	   
   }

	
	public Integer getTotalAmountConsidered() {
		
		if(! ReferenceTable.getSeniorCitizenKeys().containsKey(this.bean.getPolicyDto().getProduct().getKey())){
			return SHAUtils.getIntegerFromString(this.table.getColumnFooter("amountConsidered")) ;
		}else{
			Integer amountConsidered = SHAUtils.getIntegerFromString(this.table.getColumnFooter("amountConsidered")) ;
			Integer ambulanceAmount = SHAUtils.getIntegerFromString(this.table.getColumnFooter("ambulanceCharge")) ;
			return amountConsidered + ambulanceAmount;
		}
		
		
	}
	
	public Integer getTotalReverseAllocatedAmt() {
		return SHAUtils.getIntegerFromString(this.table.getColumnFooter("reverseAllocatedAmt")) ;
	}
	
	public Integer getTotalCAmount() {
		return SHAUtils.getIntegerFromString(this.table.getColumnFooter("minimumAmount")) ;
	}
	
	public Integer getTotalCAmountWithReverseAllocation() {
		return cAmount ;
	}
	
	
	
	public Integer getTotalReverseConsideredAmt() {
		if(totalReverseAmtField != null) {
			return SHAUtils.getIntegerFromString(totalReverseAmtField.getValue());
		}
		return 0;
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
		if(SHAUtils.isValidInteger(packageAmtField.getValue()) && !packageAmtField.getValue().equals("0")) {
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
		 	cAmount += diagnosisProcedureTableDTO.getMinimumAmount() != null ? diagnosisProcedureTableDTO.getMinimumAmount() : 0;
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
		    		coPayPercentageValues.add(String.valueOf(string.intValue()));
				}
		    }
		    Boolean isZeroAvail = false;
		    for (String string : coPayPercentageValues) {
				if(SHAUtils.getIntegerFromStringWithComma(string).equals(0)) {
					isZeroAvail = true;
					break;
				}
			}
		    if(!isZeroAvail) {
		    	coPayPercentageValues.add(0, "0");
		    }
		    Long i = 0l;
		    for (String string : coPayPercentageValues) {
		    	SelectValue value = new SelectValue();
		    	value.setId(Long.valueOf(string));
		    	value.setValue(string);
		    	if(dto.getCoPayPercentage() == null) {
		    		if(i.equals(0l)) {
			    		dto.setCoPayPercentage(value);
			    	}

		    		if (bean.getIsDefaultCopay()) {
		    			String copayStr = bean.getDefaultCopayStr();

		    			if(copayStr != null && copayStr.equalsIgnoreCase(string)) {
		    				dto.setCoPayPercentage(value);
		    			}
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
			return SHAUtils.getIntegerFromString(this.table.getColumnFooter("netApprovedAmt")) ;
	 }
	 
	 public Double getAmbulanceLimitAmount(){
		 return SHAUtils.getDoubleValueFromString(this.table.getColumnFooter("ambulanceCharge"));
	 }
	 
	 @SuppressWarnings("unchecked")
	public List<DiagnosisProcedureTableDTO> getValues() {
		List<DiagnosisProcedureTableDTO> itemIds = (List<DiagnosisProcedureTableDTO>) this.table.getItemIds() ;
    	return itemIds;
	}
	 
	public void showFinalApprovedAmount() {
		Object[] visibleColumns = this.table.getVisibleColumns();
		List<Object> asList = Arrays.asList(visibleColumns);
		asList.add("");
	}
	 
	 public Integer getPostHospAmt() {
			List<DiagnosisProcedureTableDTO> itemIds = (List<DiagnosisProcedureTableDTO>) this.table.getItemIds() ;
			Integer postHospAmt = 0;
			if(bean.getPostHospitalizaionFlag() && (bean.getHospitalizaionFlag() || bean.getPartialHospitalizaionFlag())) {
				for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : itemIds) {
					if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
						Boolean isSublimitAvail = false;
						Boolean isPackageAmt = false;
						Integer amt = 0;
						if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO().getSublimitName() != null) {
							isSublimitAvail = true;
						}
						isPackageAmt = SHAUtils.getIntegerFromString(diagnosisProcedureTableDTO.getPackageAmt()) > 0 ? true : false;
						if(isSublimitAvail && isPackageAmt) {
							amt = Math.min(SHAUtils.getIntegerFromString(diagnosisProcedureTableDTO.getPackageAmt()), diagnosisProcedureTableDTO.getSubLimitAvaliableAmt());
						} else if(isSublimitAvail){
							amt = diagnosisProcedureTableDTO.getSubLimitAvaliableAmt();
						} else if(isPackageAmt) {
							amt = SHAUtils.getIntegerFromString(diagnosisProcedureTableDTO.getPackageAmt());
						}
						postHospAmt += Math.min(((diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() : 0) + (diagnosisProcedureTableDTO.getAmbulanceCharge() != null ? diagnosisProcedureTableDTO.getAmbulanceCharge() : 0)), (isSublimitAvail || isPackageAmt) ? amt : ((diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() : 0) + (diagnosisProcedureTableDTO.getAmbulanceCharge() != null ? diagnosisProcedureTableDTO.getAmbulanceCharge() : 0)));
					} else if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
						Boolean isSublimitAvail = false;
						Boolean isPackageAmt = false;
						Integer amt = 0;
						if(diagnosisProcedureTableDTO.getProcedureDTO().getSublimitName() != null) {
							isSublimitAvail = true;
						}
						isPackageAmt = SHAUtils.getIntegerFromString(diagnosisProcedureTableDTO.getPackageAmt()) > 0 ? true : false;
						if(isSublimitAvail && isPackageAmt) {
							amt = Math.min(SHAUtils.getIntegerFromString(diagnosisProcedureTableDTO.getPackageAmt()), diagnosisProcedureTableDTO.getSubLimitAvaliableAmt());
						} else if(isSublimitAvail){
							amt = diagnosisProcedureTableDTO.getSubLimitAvaliableAmt();
						} else if(isPackageAmt) {
							amt = SHAUtils.getIntegerFromString(diagnosisProcedureTableDTO.getPackageAmt());
						}
//						postHospAmt += Math.min((diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() : 0), (isSublimitAvail || isPackageAmt) ? amt : (diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() : 0));
						postHospAmt += Math.min(((diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() : 0) + (diagnosisProcedureTableDTO.getAmbulanceCharge() != null ? diagnosisProcedureTableDTO.getAmbulanceCharge() : 0)), (isSublimitAvail || isPackageAmt) ? amt : ((diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() : 0) + (diagnosisProcedureTableDTO.getAmbulanceCharge() != null ? diagnosisProcedureTableDTO.getAmbulanceCharge() : 0)));
					} else {
						postHospAmt += diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() : 0;
					}
				}
				return postHospAmt;
			}
	
			return 0;
		}

		public List<String> getErrors()
		{
			return this.errorMessages;
		}
	 
		public void getErrorMessage(String eMsg){
			
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
		@SuppressWarnings("unchecked")
		public void addCopayTypeValues(ComboBox comboBox) {
	   		BeanItemContainer<SelectValue> coPayTypeValue = (BeanItemContainer<SelectValue>) referenceData.get("coPayType");
			comboBox.setContainerDataSource(coPayTypeValue);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

		}
		
		public ValueChangeListener copayTypeValueChangeListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					ComboBox component = (ComboBox) event.getProperty();
					SelectValue coPayTypeValue = (SelectValue) event.getProperty().getValue();
					DiagnosisProcedureTableDTO diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(diagnosisProcedureTableDto);
					ComboBox coPayPercentage=(ComboBox)hashMap.get("coPayPercentage");
					
					BeanItemContainer<SelectValue> coapyValues = (BeanItemContainer<SelectValue>) referenceData.get("copay");					
					
					if(null != coPayTypeValue && (ReferenceTable.JIO_NON_PED_COPAY_ID.equals(coPayTypeValue.getId()) ||
							ReferenceTable.JIO_SUBLIMIT_COPAY_ID.equals(coPayTypeValue.getId()))){
						
						coapyValues.sort(new Object[] {"value"}, new boolean[] {true});
						setCopayValue(coapyValues.getItemIds(),coPayPercentage);
					   
					    if(!ReferenceTable.JIO_OTHERS_COPAY_ID.equals(coPayTypeValue.getId())){
					    	coPayPercentage.setEnabled(Boolean.FALSE);
					    }
					    else
					    {
					    	coPayPercentage.setEnabled(Boolean.TRUE);
					    }
					}
					else
					{
						if(null != coPayTypeValue && ReferenceTable.JIO_PED_COPAY_ID.equals(coPayTypeValue.getId())){
							coapyValues.sort(new Object[] {"value"}, new boolean[] {false});
							setCopayValue(coapyValues.getItemIds(),coPayPercentage);
						}
						coPayPercentage.setEnabled(Boolean.TRUE);
					}
				}
			};
			
			return listener;
		}
	   	
	   	public void setCopayValue(List<SelectValue> itemIds,ComboBox coPayPercentage){
	   		
	   	 if(itemIds != null && !itemIds.isEmpty()) {
		    	SelectValue selectValue = itemIds.get(0);
		    	coPayPercentage.setValue(selectValue);
		    	coPayPercentage.setNullSelectionAllowed(false);
			}
	   	}
		
		public void setReferenceData(Map<String, Object> referenceData) {
			this.referenceData = referenceData;
		}
		
		private void addCopayPercentageBasedOnCopayType(
				ComboBox component, SelectValue coPayTypeValue) {
			DiagnosisProcedureTableDTO diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(diagnosisProcedureTableDto);
			ComboBox coPayPercentage=(ComboBox)hashMap.get("coPayPercentage");
			
			BeanItemContainer<SelectValue> coapyValues = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			if(coPayPercentage != null){
				coapyValues = (BeanItemContainer<SelectValue>)coPayPercentage.getContainerDataSource();	
			}
			
			if(null != coPayTypeValue && null != coapyValues && (ReferenceTable.JIO_NON_PED_COPAY_ID.equals(coPayTypeValue.getId()) ||
					ReferenceTable.JIO_SUBLIMIT_COPAY_ID.equals(coPayTypeValue.getId()))){
				
				coapyValues.sort(new Object[] {"value"}, new boolean[] {true});
				setCopayValue(coapyValues.getItemIds(),coPayPercentage,0L,coPayTypeValue);
			   
			    if(!ReferenceTable.JIO_OTHERS_COPAY_ID.equals(coPayTypeValue.getId()) && coPayPercentage != null){
			    	coPayPercentage.setEnabled(Boolean.FALSE);
			    }
			    else if(coPayPercentage != null)
			    {
			    	coPayPercentage.setEnabled(Boolean.TRUE);
			    }
			}
			else
			{
				if(null != coPayTypeValue && null != coapyValues && ReferenceTable.JIO_PED_COPAY_ID.equals(coPayTypeValue.getId())){
					DBCalculationService dbCalculationService = new DBCalculationService();
					Long coPayPercntage = dbCalculationService.getCopayPercentageforJio(bean.getNewIntimationDTO().getPolicy().getKey(), coPayTypeValue.getValue());
					setCopayValue(coapyValues.getItemIds(),coPayPercentage,coPayPercntage,coPayTypeValue);
					
				}
				else
				{
					if(null != coapyValues){
						setCopayValue(coapyValues.getItemIds(),coPayPercentage,0L,coPayTypeValue);
					}
				}
				if(null != coPayPercentage){
					coPayPercentage.setEnabled(Boolean.TRUE);
				}
			}
			
			if (diagnosisProcedureTableDto.getDiagnosisDetailsDTO() != null) {
				diagnosisProcedureTableDto.getDiagnosisDetailsDTO().setCoPayTypeId(coPayTypeValue);
			}else if(diagnosisProcedureTableDto.getProcedureDTO() != null){
				diagnosisProcedureTableDto.getProcedureDTO().setCoPayTypeId(coPayTypeValue);
			}
		}
	   	
	   	public void setCopayValue(List<SelectValue> itemIds,ComboBox coPayPercentage,Long coPayPercntage,SelectValue coPayTypeValue){
	   		
	   		if(null != coPayTypeValue && !ReferenceTable.JIO_PED_COPAY_ID.equals(coPayTypeValue.getId())){
			   	 if(itemIds != null && !itemIds.isEmpty() && coPayPercentage != null) {
				    	SelectValue selectValue = itemIds.get(0);
				    	coPayPercentage.setValue(selectValue);
				    	coPayPercentage.setNullSelectionAllowed(false);
					}
	   		}
	   		else{
			   	 for (SelectValue selectValue : itemIds) {
					if(selectValue.getId().equals(coPayPercntage)){
						coPayPercentage.setValue(selectValue);
						coPayPercentage.setNullSelectionAllowed(false);
					}
				}
	   		}
	   	}
	   	
	   	public String isValidForPkgChange() {
			
			String errMsg = "";
			Collection<DiagnosisProcedureTableDTO> itemIds = (Collection<DiagnosisProcedureTableDTO>) table
					.getItemIds();		
			
			if(itemIds != null && !itemIds.isEmpty()) {
				
				for (DiagnosisProcedureTableDTO bean : itemIds) {
				
						if(bean.getProcedureDTO() != null && (bean.getReasonForPkgChange() == null || bean.getReasonForPkgChange().isEmpty()) &&
								bean.getPackageAmt() != null && bean.getAgreedPackageAmt() != null ){
							if(SHAUtils.isValidInteger(bean.getPackageAmt()) && SHAUtils.isValidInteger(bean.getAgreedPackageAmt()) ){
								if(!bean.getPackageAmt().equalsIgnoreCase(bean.getAgreedPackageAmt())){
									errMsg = "Please Enter Reason for Package amount change - "+bean.getDiagOrProcedure();
								}
							}else if(SHAUtils.isValidInteger(bean.getPackageAmt()) && !bean.getPackageAmt().equals("0")){
								errMsg = "Please Enter Reason for Package amount change - "+bean.getDiagOrProcedure();
							}
						}
				}
			}	
			return errMsg;
		}
	   	
	   	public void initPresenter(String presenterString) {
			this.presenterString = presenterString;
		}
	   	
	   	public BlurListener getPkgAmtListener() {
			
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
	   	
	   	public  void handleEnter(TextField searchField, final  Listener listener) {
		    ShortcutListener enterShortCut = new ShortcutListener(
		        "EnterShortcut", ShortcutAction.KeyCode.F8, null) {
		      @Override
		      public void handleAction( Object sender, Object target) {
		        ((ShortcutListener) listener).handleAction(sender, target);
		      }
		    };
		    
		   // handleShortcut(searchField, enterShortCut);
		    handleShortcut(searchField, getShortCutListener(searchField));
		  }
		
		public  void handleShortcut(final TextField textField, final ShortcutListener shortcutListener) {
			//textField.addFocusListener(F);
			textField.addFocusListener(new FocusListener() {
				
				@Override
				public void focus(FocusEvent event) {
					//shortcutListener = getShortCutListener(textField);
					//textField.addShortcutListener(getShortCutListener(textField));
					textField.addShortcutListener(shortcutListener);
					
				}
			});
			
		   textField.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				
				/*Collection<?> listeners = textField.getListeners(ShortcutListener.class);
				for (Object object : listeners) {
					textField.removeListener(ShortcutListener.class, object);
				}*/
				
				textField.removeShortcutListener(shortcutListener);
				/*Collection<?> listeners = textField.getListeners(ShortcutListener.class);
				for (Object object : listeners) {
					textField.removeListener(ShortcutListener.class, object);
				}*/
				
			}
		});
		  }
		
		private ShortcutListener getShortCutListener(final TextField txtFld) {
			ShortcutListener listener =  new ShortcutListener("EnterShortcut",KeyCodes.KEY_F8,null) {
				private static final long serialVersionUID = 1L;
				@Override
				public void handleAction(Object sender, Object target) {
					final Window dialog = new Window();
					
					VerticalLayout vLayout =  new VerticalLayout();
					vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
					vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
					vLayout.setMargin(true);
					vLayout.setSpacing(true);
					
					final TextArea txtArea = new TextArea();
					txtArea.setMaxLength(500);
					txtArea.setValue(txtFld.getValue());
					txtArea.setNullRepresentation("");
					txtArea.setRows(21);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					txtArea.setReadOnly(false);
					
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					
					txtArea.addValueChangeListener(new Property.ValueChangeListener() {
						@Override
						public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());
						}
					});
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);
					
					String strCaption = "Reason for amount change";
					dialog.setCaption(strCaption);
					dialog.setClosable(true);
					dialog.setContent(vLayout);
					dialog.setResizable(true);
					dialog.setModal(true);
					dialog.setDraggable(true);
					dialog.setData(txtFld);
					
					dialog.addCloseListener(new Window.CloseListener() {
						@Override
						public void windowClose(CloseEvent e) {
							dialog.close();
						}
					});
					
					if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
						dialog.setPositionX(250);
						dialog.setPositionY(100);
					}
					getUI().getCurrent().addWindow(dialog);
					
					okBtn.addClickListener(new Button.ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();
						}
					});	
				}
			};
			return listener;
		}
		
		public BlurListener getPkgAmtChangeReasonListener(final TextField reasonField) {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					if(null != component && null != component.getValue()) {
						reasonField.setDescription("Click the Text Box and Press F8 For Detailed Popup");
					} 
					
				}
			};
			return listener;
			
		}
}
