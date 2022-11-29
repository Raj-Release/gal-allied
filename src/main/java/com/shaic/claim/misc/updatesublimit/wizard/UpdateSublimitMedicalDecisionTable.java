package com.shaic.claim.misc.updatesublimit.wizard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.RevisedMedicalDecisionTable.ImmediateFieldFactory;
import com.shaic.claim.icdSublimitMapping.IcdSubLimitMappingService;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.reimbursement.billing.pages.billingprocess.BillingProcessPagePresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.billingprocess.FinancialProcessPagePresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.ui.themes.ValoTheme;

public class UpdateSublimitMedicalDecisionTable extends ViewComponent {

	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<DiagnosisProcedureTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DiagnosisProcedureTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<DiagnosisProcedureTableDTO> data = new BeanItemContainer<DiagnosisProcedureTableDTO>(DiagnosisProcedureTableDTO.class);
	
	private Table table;
	
	PreauthDTO bean;
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private String presenterString;
	
	private String intialSublimitName;
	
	private static Validator validator;
	
	private Boolean isSublimitchanged = false;
	
	@EJB
	private IcdSubLimitMappingService icdSublimitMapService;
	
	
	
	public Object[] VISIBLE_COLUMNS_BILLING_AND_FA = new Object[] {
			"diagOrProcedure", "description", "pedOrExclusion","considerForPaymnt","isAmbChargeApplicable","ambulanceCharge","amountConsidered", "packageAmt", "minimumAmountOfAmtconsideredAndPackAmt","coPayType", "coPayPercentage", "coPayAmount", "netAmount", "amtWithAmbulanceCharge","sublimitYesOrNo","sublimitValues","subLimitAmount", "subLimitUtilAmount", "subLimitAvaliableAmt", "restrictionSI","utilizedAmt", "availableAmout", "minimumAmount", "reverseAllocatedAmt"};
	
	
	public TextField dummyField;
	public TextField ambulanceChangeField;
	
	private TextField totalReverseAmtField;
	
	private HorizontalLayout hLayout;

	private VerticalLayout layout;
	
	private Integer cAmount = 0;
	
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
	
	public void init(PreauthDTO bean,String presenterString) {
		this.bean = bean;
		this.presenterString = presenterString;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
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
		
		if(ReferenceTable.getGMCProductList().containsKey(this.bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			table = new Table("Sub limits and Package Table", data);
		}else{
			table = new Table("Sub limits, Package & SI Restriction Table", data);
		}
		
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
	
			if(null != presenterString && ((SHAConstants.BILLING.equalsIgnoreCase(presenterString)) ||
					(SHAConstants.FINANCIAL.equalsIgnoreCase(presenterString)))){
				table.setVisibleColumns(VISIBLE_COLUMNS_BILLING_AND_FA);
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
		table.setColumnHeader("considerForPaymnt", "Consider For Payment");
		table.setColumnHeader("sublimitYesOrNo", "Sublimit</br>Applicable");
		table.setColumnHeader("sublimitValues", "Sublimit Name");
		
		table.setColumnHeader("isAmbChargeApplicable", "Ambulance Charge Applicable");
		table.setColumnHeader("ambulanceCharge", "Ambulance Charges");
		table.setColumnHeader("amtWithAmbulanceCharge", "Amount With Ambulance Charges");
		
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
				table.setColumnHeader("considerForPaymnt", "Consider For Payment");
				table.setColumnHeader("sublimitYesOrNo", "Sublimit</br>Applicable");
				table.setColumnHeader("sublimitValues", "Sublimit Name");
				
				table.setColumnHeader("isAmbChargeApplicable", "Ambulance</br>Charge</br>Applicable");
				table.setColumnHeader("ambulanceCharge", "Ambulance</br>Charges");
				table.setColumnHeader("amtWithAmbulanceCharge", "Amount With</br>Ambulance</br>Charges");
				
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
		this.table.setVisibleColumns(VISIBLE_COLUMNS_BILLING_AND_FA);
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
				field.setEnabled(false);
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
//				if(bean.getIsReverseAllocation()) {
//					field.setEnabled(true);
//				}
				
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
			}
			else if("coPayType".equals(propertyId)){
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
				
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("packageAmt", field);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
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
			else if("subLimitUtilAmount".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("subLimitUtilAmount", field);
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
			else if ("considerForPaymnt".equals(propertyId)) {
				GComboBox field = new GComboBox();
				
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("considerForPaymnt", field);
				addCommonValues(field, "considerForPaymnt");
//				consideredAmountListener(null,field);
				field.setNullSelectionAllowed(false);
				field.setEnabled(false);
				if(diagnosisProcedureTableDto.getDiagOrProcedure() != null && 
						diagnosisProcedureTableDto.getDiagOrProcedure().equalsIgnoreCase("Residual Treatment / Procedure Amount")){
					SelectValue selected = new SelectValue(1021l,"Yes");
					diagnosisProcedureTableDto.setConsiderForPaymnt(selected);
					field.setValue(selected);	
					field.setValue(selected);
					field.setVisible(false);
					
				}
				setConsiderForPaymentValues(field,diagnosisProcedureTableDto);
				field.addValueChangeListener(considerForPaymentListener());
				
				return field;
				
			} 
			else if ("sublimitYesOrNo".equals(propertyId)) {
				GComboBox field = new GComboBox();
				
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("sublimitYesOrNo", field);
				addCommonValues(field, "sublimitYesOrNo");
//				consideredAmountListener(null,field);
				SelectValue selected = new SelectValue(1022l,"No");
				field.setNullSelectionAllowed(false);
				
				if(diagnosisProcedureTableDto.getDiagOrProcedure() != null && 
						diagnosisProcedureTableDto.getDiagOrProcedure().equalsIgnoreCase("Residual Treatment / Procedure Amount")){
					
					diagnosisProcedureTableDto.setSublimitYesOrNo(selected);
					field.setValue(selected);	
					field.setValue(selected);
					field.setVisible(false);
					
				}else{
					setDefaultValuesForSublimitApplicable(
							diagnosisProcedureTableDto, field, selected);
				}
				SublimitFunObject objSublimitFun = null;
				if(bean.getSublimitFunMap() != null){
					if(diagnosisProcedureTableDto.getDiagnosisDetailsDTO() != null && diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitName() != null){
						objSublimitFun = bean.getSublimitFunMap()
								.get(diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitName()
										.getLimitId());
					}
				}
				
				/**
				 *  CR R1136 Start
				 */				
				TextField diagName = (TextField) tableRow.get("description");
				boolean isMapAvailable =  false;
				if(diagName != null){
					isMapAvailable =  isSublimitMapAvailableForDiagnosis(diagName.getValue());
				}
				
				if(diagnosisProcedureTableDto.getDiagnosisDetailsDTO() != null){ 
					isMapAvailable =  diagnosisProcedureTableDto.getDiagnosisDetailsDTO().isSublimitMapAvailable();
				}
				/**
				 *  CR R1136 End
				 */
				
				if(presenterString != null && 
						(presenterString.equalsIgnoreCase(SHAConstants.BILLING) || 
								(presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL) && 
										diagnosisProcedureTableDto.isIcdSublimitMapAvailable() && 
										isMapAvailable))){
					field.setEnabled(false);                  //CR R1136
				}else{
					addSublimitApplicableListener(field);
				}

			/*	setConsiderForPaymentValues(field,diagnosisProcedureTableDto);
				field.addValueChangeListener(considerForPaymentListener());*/
				
				return field;
				
			}else if ("sublimitValues".equals(propertyId)) {
				GComboBox field = new GComboBox();
				
				field.setData(diagnosisProcedureTableDto);
				tableRow.put("sublimitValues", field);
//				consideredAmountListener(null,field);
				addSublimtValues(field);
				field.setNullSelectionAllowed(false);
				
				if(diagnosisProcedureTableDto.getDiagOrProcedure() != null && 
						diagnosisProcedureTableDto.getDiagOrProcedure().equalsIgnoreCase("Residual Treatment / Procedure Amount")){
					field.setVisible(false);
					
				}else{
					if(bean.getSublimitFunMap() != null){
						if(diagnosisProcedureTableDto.getDiagnosisDetailsDTO() != null && diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitName() != null){
							SublimitFunObject objSublimitFun = bean.getSublimitFunMap()
									.get(diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitName()
											.getLimitId());
							diagnosisProcedureTableDto.setSublimitValues(objSublimitFun);
							field.setValue(objSublimitFun);
						}else if(diagnosisProcedureTableDto.getProcedureDTO() != null && diagnosisProcedureTableDto.getProcedureDTO().getSublimitName() != null){
							SublimitFunObject objSublimitFun = bean.getSublimitFunMap()
									.get(diagnosisProcedureTableDto.getProcedureDTO().getSublimitName()
											.getLimitId());
							diagnosisProcedureTableDto.setSublimitValues(objSublimitFun);
							field.setValue(objSublimitFun);
						}
					}
					
					if((diagnosisProcedureTableDto.getSublimitYesOrNo() != null && diagnosisProcedureTableDto.getSublimitYesOrNo().getId().equals(ReferenceTable.COMMONMASTER_NO)) 
							|| (diagnosisProcedureTableDto.isIcdSublimitMapAvailable() && (field.getValue() != null && (((SublimitFunObject)field.getValue()).getName()).equalsIgnoreCase(diagnosisProcedureTableDto.getIcdSublimitMapName())))) {
						field.setEnabled(false);
					}
				}
				
				if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.BILLING)){
					field.setEnabled(false);
				}else{
					addSublimitValueChangeLisener(field);
				}
				
				

			/*	setConsiderForPaymentValues(field,diagnosisProcedureTableDto);
				field.addValueChangeListener(considerForPaymentListener());*/
				
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
	
	private void setDefaultValuesForSublimitApplicable(
			DiagnosisProcedureTableDTO diagnosisProcedureTableDto,
			GComboBox field, SelectValue selected) {
		if(diagnosisProcedureTableDto.getDiagnosisDetailsDTO() != null){
			if(diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitApplicableFlag() != null && 
					diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitApplicableFlag().equalsIgnoreCase("Y")){
				selected.setId(ReferenceTable.COMMONMASTER_YES);
				selected.setValue("Yes");
				diagnosisProcedureTableDto.setSublimitYesOrNo(selected);
				field.setValue(selected);
			}else{
				diagnosisProcedureTableDto.setSublimitYesOrNo(selected);
				field.setValue(selected);
				if(diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitName() != null){
					diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getSublimitName().setLimitId(null);
				}
			}
		}else if(diagnosisProcedureTableDto.getProcedureDTO() != null){
			if(diagnosisProcedureTableDto.getProcedureDTO().getSublimitApplicableFlag() != null && 
					diagnosisProcedureTableDto.getProcedureDTO().getSublimitApplicableFlag().equalsIgnoreCase("Y")){
				selected.setId(ReferenceTable.COMMONMASTER_YES);
				selected.setValue("Yes");
				diagnosisProcedureTableDto.setSublimitYesOrNo(selected);
				field.setValue(selected);
			}else{
				diagnosisProcedureTableDto.setSublimitYesOrNo(selected);
				field.setValue(selected);
				if(diagnosisProcedureTableDto.getProcedureDTO().getSublimitName() != null){
					diagnosisProcedureTableDto.getProcedureDTO().getSublimitName().setLimitId(null);
				}
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
		if(null != component){
		if(isCopay) {
			ComboBox combo  = (ComboBox) component;
			diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)combo.getData();
		} else if(component instanceof CheckBox){
			CheckBox checkbox = (CheckBox) component;
			diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)checkbox.getData();
		}else if(component instanceof GComboBox){
			GComboBox combo = (GComboBox) component;
			diagnosisProcedureTableDto = (DiagnosisProcedureTableDTO)combo.getData();
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
		ComboBox considerForPayment = (ComboBox)hashMap.get("considerForPaymnt");
		
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
				if(SHAUtils.isValidInteger(packageAmtField.getValue())) {
					min = Math.min(SHAUtils.getIntegerFromString(amountConsidered.getValue()), SHAUtils.getIntegerFromString(packageAmtField.getValue()));
				}
			}
		} else if (this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("b") ){
			if(amountConsidered != null){
				 min = SHAUtils.getIntegerFromString(amountConsidered.getValue());
				 min = findMinimumValuesForSpecificProduct(amountConsidered, packageAmtField, sublimitAmtField, sublimitAvaliableField);
			}
		} else if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("c")) {
			if(amountConsidered != null){
				min = SHAUtils.getIntegerFromString(amountConsidered.getValue());
				if(SHAUtils.isValidInteger(packageAmtField.getValue())) {
					min = Math.min(SHAUtils.getIntegerFromString(amountConsidered.getValue()), SHAUtils.getIntegerFromString(packageAmtField.getValue()));
				}
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
		
		if(considerForPayment != null && considerForPayment.getValue() != null){
			SelectValue value = (SelectValue)considerForPayment.getValue();
			if(value.getId() != null && value.getId().equals(ReferenceTable.COMMONMASTER_NO)){
				min = 0;
				
			}			
			else
			{	
				if(diagnosisProcedureTableDto.getDiagOrProcedure() != null && 
						diagnosisProcedureTableDto.getDiagOrProcedure().equalsIgnoreCase("Residual Treatment / Procedure Amount")){
					diagnosisProcedureTableDto.setIsPaymentAvailable(true);
				}else{
					diagnosisProcedureTableDto.setIsPaymentAvailable(diagnosisProcedureTableDto.getIsPedExclusionFlag());
				}
				
				//diagnosisProcedureTableDto.setIsPaymentAvailable(true);
				/*if(pedExclusionValidation(diagnosisProcedureTableDto))
				{
					diagnosisProcedureTableDto.setIsPaymentAvailable(true);
				}
				else
				{
					diagnosisProcedureTableDto.setIsPaymentAvailable(false);
				}*/
			}
		}
		
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
				}else if(this.bean.getNewIntimationDTO().getPolicy().getProduct().getCalculationTemplateFlag().toLowerCase().equalsIgnoreCase("c")) {
					
					if(isAmbulanceCharge){
						minValue = findMinimumValuesForCommon(amtWithAmbulance, restrictionSIField, selectedSIAmount, sublimitAmtField, sublimitAvaliableField);
					}else{
						minValue = findMinimumValuesForCommon(netAmountField, restrictionSIField, selectedSIAmount, sublimitAmtField, sublimitAvaliableField);
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
			
		
			if(considerForPayment != null && considerForPayment.getValue() != null){
				SelectValue value = (SelectValue)considerForPayment.getValue();
				if(value.getId() != null && value.getId().equals(ReferenceTable.COMMONMASTER_NO)){
					minValue = 0;
					
				}
				else
				{
					
					if(diagnosisProcedureTableDto.getDiagOrProcedure() != null && 
							diagnosisProcedureTableDto.getDiagOrProcedure().equalsIgnoreCase("Residual Treatment / Procedure Amount")){
						diagnosisProcedureTableDto.setIsPaymentAvailable(true);
					}else{
						diagnosisProcedureTableDto.setIsPaymentAvailable(diagnosisProcedureTableDto.getIsPedExclusionFlag());
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
		dummyField.setValue(String.valueOf(reverseAllocationTotal));
		ambulanceChangeField.setValue(String.valueOf(ambulanceTotalAmt));
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
		Integer restrictionSI = 0;
		if(restrictionSIField != null){
			restrictionSI = SHAUtils.getIntegerFromStringWithComma(restrictionSIField.getValue());
		}
		Integer availableSublimitAmt = 0;
		if(availableSublimit != null){
			availableSublimitAmt = SHAUtils.getIntegerFromStringWithComma(availableSublimit.getValue());
		}
		int min = 0;
		List<Integer> amounts = new ArrayList<Integer>();
		
		if(selectedSIAmount != null){
			if(SHAUtils.isValidInteger(selectedSIAmount.getValue())) {
				amounts.add(restrictionSI);
			}
		}
		if( sublimitAmt != null && SHAUtils.isValidInteger(sublimitAmt.getValue())) {
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
		    	
		    	if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
		    			(ReferenceTable.setMaxCopayProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
		    		if((bean.getPreauthDataExtractionDetails().getSection() != null &&
		    				bean.getPreauthDataExtractionDetails().getSection().getId()!= null && 
		    				(ReferenceTable.POL_SECTION_2.equals(bean.getPreauthDataExtractionDetails().getSection().getId()))) ||
		    				(ReferenceTable.LUMPSUM_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()) &&
		    						ReferenceTable.LUMPSUM_COVER_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue()))){
		    			coPayPercentageValues.add("0");
		    			
		    			SelectValue value = new SelectValue();
				    	value.setId(0l);
				    	value.setValue("0");
		    			dto.setCoPayPercentage(value);
		    			
		    		}else
		    		{
		    			for (Double string : this.bean.getProductCopay()) {
				    		coPayPercentageValues.add(String.valueOf(string.intValue()));
						}
		    		}
		    	}
		    	else
		    	{		    	
		    		for (Double string : this.bean.getProductCopay()) {
		    		coPayPercentageValues.add(String.valueOf(string.intValue()));
				}
		    	}
		    }
		    else
		    {
		    	if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
		    			(ReferenceTable.setMaxCopayProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
		    		if((bean.getPreauthDataExtractionDetails().getSection() != null &&
		    				bean.getPreauthDataExtractionDetails().getSection().getId()!= null && 
		    				(ReferenceTable.POL_SECTION_2.equals(bean.getPreauthDataExtractionDetails().getSection().getId()))) ||
		    				(ReferenceTable.LUMPSUM_SECTION_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSection().getCommonValue()) &&
		    						ReferenceTable.LUMPSUM_COVER_CODE.equals(bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getCover().getCommonValue()))){		    			
		    		
		    			coPayPercentageValues = new ArrayList<String>();
		    			coPayPercentageValues.add(0, "0");
		    			
		    			SelectValue value = new SelectValue();
				    	value.setId(0l);
				    	value.setValue("0");
		    			dto.setCoPayPercentage(value);
		    		}
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
			    			
			    			   if(null != bean.getNewIntimationDTO().getIsJioPolicy() && !bean.getNewIntimationDTO().getIsJioPolicy()){
			    				   dto.setCoPayPercentage(value);
			    			   }
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
		    if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() && 
					!(ReferenceTable.getDefaultCopayNotApplicableProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){
				
		    	coPayContainer.sort(new Object[] {"value"}, new boolean[] {false});
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
		private void addCommonValues(ComboBox diagnosisCombo, String tableColumnName) {
			
			BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
					.get("commonValues");
			
			diagnosisCombo.setContainerDataSource(commonValues);
			diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			diagnosisCombo.setItemCaptionPropertyId("value");

		}
		
		public void setReferenceData(Map<String, Object> referenceData) {
			this.referenceData = referenceData;
		}
		
		public ValueChangeListener considerForPaymentListener(){
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
					TextField minAmount = (TextField)hashMap.get("minimumAmount");
					TextField reverseAllocation = (TextField)hashMap.get("reverseAllocatedAmt");	
					TextField amountConsidered = (TextField)hashMap.get("amountConsidered");
					
					
					if ( null != component && null != component.getValue()) {
						Boolean componentValue = SHAConstants.YES
								.equalsIgnoreCase(String.valueOf(component.getValue()).trim()) ? true : false;
							if(componentValue){
								if(null != diagnosisProcedureTableDto.getDiagOrProcedureFlag() && (SHAConstants.DIAGNOSIS.equalsIgnoreCase(diagnosisProcedureTableDto.getDiagOrProcedureFlag()))){
									diagnosisProcedureTableDto.getDiagnosisDetailsDTO().setConsiderForPaymentFlag(SHAConstants.YES_FLAG);
								}									
									
								if(null != diagnosisProcedureTableDto.getDiagOrProcedureFlag() && (SHAConstants.PROCEDURE.equalsIgnoreCase(diagnosisProcedureTableDto.getDiagOrProcedureFlag()))){
									diagnosisProcedureTableDto.getProcedureDTO().setConsiderForPaymentFlag(SHAConstants.YES_FLAG);
								}
							}
							else{
								if(null != diagnosisProcedureTableDto.getDiagOrProcedureFlag() && (SHAConstants.DIAGNOSIS.equalsIgnoreCase(diagnosisProcedureTableDto.getDiagOrProcedureFlag()))){
									diagnosisProcedureTableDto.getDiagnosisDetailsDTO().setConsiderForPaymentFlag(SHAConstants.N_FLAG);
								}									
									
								if(null != diagnosisProcedureTableDto.getDiagOrProcedureFlag() && (SHAConstants.PROCEDURE.equalsIgnoreCase(diagnosisProcedureTableDto.getDiagOrProcedureFlag()))){
									diagnosisProcedureTableDto.getProcedureDTO().setConsiderForPaymentFlag(SHAConstants.N_FLAG);
								}
						 }
						}
								
				
						List<DiagnosisDetailsTableDTO> pedValidationTableList = bean
								.getPreauthDataExtractionDetails().getDiagnosisTableList();
						//DiagnosisProcedureTableDTO dto   = null;
						SelectValue value = null;
						for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
							//dto = new DiagnosisProcedureTableDTO();
							/*if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
								Boolean isPaymentAvailable = pedValidationTableDTO
										.getConsiderForPaymentFlag().toLowerCase()
										.equalsIgnoreCase("y") ? true : false;*/
							if ( null != component && null != component.getValue()) {
								
								Boolean isPaymentAvailable = SHAConstants.YES
										.equalsIgnoreCase(String.valueOf(component.getValue()).trim()) ? true : false;
									
								
								if (isPaymentAvailable) {
									
									List<PedDetailsTableDTO> pedList = pedValidationTableDTO
											.getPedList();
									/*if (!pedList.isEmpty()) {
										for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

											List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
													.getExclusionAllDetails();
											String paymentFlag = "y";
											if(exclusionAllDetails != null) {
												for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
													if (null != pedDetailsTableDTO
															.getExclusionDetails()
															&& exclusionDetails
																	.getKey()
																	.equals(pedDetailsTableDTO
																			.getExclusionDetails()
																			.getId())) {
														paymentFlag = exclusionDetails
																.getPaymentFlag();
													}
												}
											}
											

											if (paymentFlag.toLowerCase().equalsIgnoreCase(
													"n")) {
												isPaymentAvailable = false;
												break;
											}
										}
									}*/
									if(isPaymentAvailable){
										isPaymentAvailable = diagnosisProcedureTableDto.getIsPedExclusionFlag();
									}
									
								}

								if (!isPaymentAvailable && null != minAmount && null != reverseAllocation) {
									diagnosisProcedureTableDto.setMinimumAmount(0);
									diagnosisProcedureTableDto.setReverseAllocatedAmt(0);
									
									minAmount.setReadOnly(false);
									reverseAllocation.setReadOnly(false);
									
									minAmount.setValue("0");									
									reverseAllocation.setValue("0");
									
									minAmount.setReadOnly(false);
									reverseAllocation.setReadOnly(false);
									
									
									
									
								}
								diagnosisProcedureTableDto.setIsPaymentAvailable(isPaymentAvailable);
							} else {
								diagnosisProcedureTableDto.setIsPaymentAvailable(false);
							}	
							
						}
						List<ProcedureDTO> procedureExclusionCheckTableList = bean
								.getPreauthMedicalProcessingDetails()
								.getProcedureExclusionCheckTableList();
						for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
													
							//diagnosisProcedureTableDto = new DiagnosisProcedureTableDTO();
							
							
							if(null != diagnosisProcedureTableDto.getProcedureDTO()  && null !=diagnosisProcedureTableDto.getProcedureDTO().getKey()
									&& null != procedureDTO.getKey() && procedureDTO.getKey().equals( diagnosisProcedureTableDto.getProcedureDTO().getKey())){								
								if(diagnosisProcedureTableDto.getConsiderForPaymnt() != null && diagnosisProcedureTableDto.getConsiderForPaymnt().getId()!= null){
									if(diagnosisProcedureTableDto.getConsiderForPaymnt().getId().equals(ReferenceTable.COMMONMASTER_YES)){
										procedureDTO.setConsiderForPaymentFlag(SHAConstants.YES_FLAG);
									}else{
										procedureDTO.setConsiderForPaymentFlag(SHAConstants.N_FLAG);
										
									}									
								}
								diagnosisProcedureTableDto.setProcedureDTO(procedureDTO);
							}
							Boolean isPaymentAvailable = true;
							/*if (procedureDTO.getConsiderForPaymentFlag() != null) {
								isPaymentAvailable = procedureDTO
										.getConsiderForPaymentFlag().toLowerCase()
										.equalsIgnoreCase("y") ? true : false;*/
							if ( null != component && null != component.getValue()) {
								
								 isPaymentAvailable = SHAConstants.YES
										.equalsIgnoreCase(String.valueOf(component.getValue()).trim()) ? true : false;
							} else {
								isPaymentAvailable = false;
								if(procedureDTO.getNewProcedureFlag() != null && procedureDTO.getNewProcedureFlag().equals(1l)) {
									isPaymentAvailable = true;
								}
								diagnosisProcedureTableDto.setIsPaymentAvailable(isPaymentAvailable);
								
							}
								if(isPaymentAvailable) {
									
									if(procedureDTO.getExclusionDetails() != null && procedureDTO.getExclusionDetails().getValue() != null && !procedureDTO.getExclusionDetails().getValue().toLowerCase().equalsIgnoreCase("not applicable")) {
										isPaymentAvailable = false;
									}
								}
								if (!isPaymentAvailable && null != minAmount && null != reverseAllocation) {
									diagnosisProcedureTableDto.setMinimumAmount(0);
									diagnosisProcedureTableDto.setReverseAllocatedAmt(0);									
									
									minAmount.setReadOnly(false);
									reverseAllocation.setReadOnly(false);
									
									minAmount.setValue("0");									
									reverseAllocation.setValue("0");
									
									minAmount.setReadOnly(false);
									reverseAllocation.setReadOnly(false);									
								
								}
								diagnosisProcedureTableDto.setIsPaymentAvailable(isPaymentAvailable);	
								
						
					}	
						
						calculationMethodForMedicalDecision(amountConsidered, false);
						
						calculateTotal(false);
				}
			};
			
			return listener;
		}
		
		
		public boolean isValid() {
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			Collection<DiagnosisProcedureTableDTO> itemIds = (Collection<DiagnosisProcedureTableDTO>) table
					.getItemIds();			
			for (DiagnosisProcedureTableDTO bean : itemIds) {
			
				
				Set<ConstraintViolation<DiagnosisProcedureTableDTO>> validate = validator
						.validate(bean);

				if (validate.size() > 0) {
					hasError = true;
					for (ConstraintViolation<DiagnosisProcedureTableDTO> constraintViolation : validate) {
						if(constraintViolation.getRootBean() != null && (presenterString.equalsIgnoreCase(SHAConstants.BILLING)||
								(presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)))){
							DiagnosisProcedureTableDTO rootBean = constraintViolation.getRootBean();
						    errorMessages.add(constraintViolation.getMessage());
						}else{
							errorMessages.add(constraintViolation.getMessage());
						}
					}
				}
			}		
				
			
			return !hasError;
		}

		public void setConsiderForPaymentValues(ComboBox field, DiagnosisProcedureTableDTO diagnosisProcedureTableDto){
			
			if(null != diagnosisProcedureTableDto && null != diagnosisProcedureTableDto.getDiagnosisDetailsDTO() && 
					null != diagnosisProcedureTableDto.getDiagnosisDetailsDTO().getConsiderForPaymentFlag()){
			Boolean isPaymentAvailable = diagnosisProcedureTableDto.getDiagnosisDetailsDTO()
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
			if(isPaymentAvailable){
				SelectValue selected = new SelectValue(1021l,"Yes");
				diagnosisProcedureTableDto.setConsiderForPaymnt(selected);
				field.setValue(selected);					
			}
			else
			{
				SelectValue selected = new SelectValue(1022l,"No");
				diagnosisProcedureTableDto.setConsiderForPaymnt(selected);
				field.setValue(selected);	
			}
			}
			
			
			if(null != diagnosisProcedureTableDto && null != diagnosisProcedureTableDto.getProcedureDTO() && 
					null != diagnosisProcedureTableDto.getProcedureDTO().getConsiderForPaymentFlag()){
			Boolean isPaymentAvailable = diagnosisProcedureTableDto.getProcedureDTO()
					.getConsiderForPaymentFlag().toLowerCase()
					.equalsIgnoreCase("y") ? true : false;
			
			if(isPaymentAvailable){
				SelectValue selected = new SelectValue(1021l,"Yes");
				diagnosisProcedureTableDto.setConsiderForPaymnt(selected);
				field.setValue(selected);					
			}
			else
			{
				SelectValue selected = new SelectValue(1022l,"No");
				diagnosisProcedureTableDto.setConsiderForPaymnt(selected);
				field.setValue(selected);	
			}
		}
		}
		
		public Boolean pedExclusionValidation(DiagnosisProcedureTableDTO diagnosisProcedureTableDto){
			
			List<DiagnosisDetailsTableDTO> pedValidationTableList = bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			DiagnosisProcedureTableDTO dto   = null;
			SelectValue value = null;
			Boolean isPAymentAvailable = true;
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
			List<PedDetailsTableDTO> pedList = pedValidationTableDTO
					.getPedList();
			if (!pedList.isEmpty()) {
				for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

					List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
							.getExclusionAllDetails();
					String paymentFlag = "y";
					if(exclusionAllDetails != null) {
						for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
							if (null != pedDetailsTableDTO.getExclusionDetails()
									&& exclusionDetails.getKey().equals(pedDetailsTableDTO.getExclusionDetails().getId())) {
								
								paymentFlag = exclusionDetails.getPaymentFlag();
							}
						}
					}
					

					if (paymentFlag.toLowerCase().equalsIgnoreCase(
							"n")) {
						isPAymentAvailable = false;
						break;
					}
					
				}	
				}	
			}
			return isPAymentAvailable;
		}
		
	   	@SuppressWarnings("unused")
		private void addSublimitApplicableListener(final ComboBox sublimitApplicableCombo) {
			if (sublimitApplicableCombo != null) {
				sublimitApplicableCombo.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						DiagnosisProcedureTableDTO pedValidationDTO = (DiagnosisProcedureTableDTO) component.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(pedValidationDTO);
						System.out.println("--the hashMap----"+hashMap);
						ComboBox sublimtName = (ComboBox) hashMap.get("sublimitValues");
						TextField sublimitAvaliableField = (TextField)hashMap.get("subLimitAvaliableAmt");
						TextField sublimitAmtField = (TextField)hashMap.get("subLimitAmount");
						TextField sublimtUtilizedField = (TextField)hashMap.get("subLimitUtilAmount");
						
						if(! bean.getIsAlertSublimtChanges() && bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
							alertForSublimitChanges("<b style = 'color: red;'>the claim type is cashless- Do you want to change the sub limit</b>");
							bean.setIsAlertSublimtChanges(true);
						}

						if(sublimtName !=null){
							if(pedValidationDTO.getSublimitYesOrNo()!=null){
								if (pedValidationDTO.getSublimitYesOrNo().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
									/*sublimitAmt.setEnabled(true);
									sublimtName.setEnabled(true);*/
									sublimtName.setEnabled(true);
									sublimtName.setReadOnly(false);
									
									if( pedValidationDTO.getDiagnosisDetailsDTO() != null && pedValidationDTO.getDiagnosisDetailsDTO().getSublimitName() != null){
										SublimitFunObject objSublimitFun = bean.getSublimitFunMap()
												.get(pedValidationDTO.getDiagnosisDetailsDTO().getSublimitName()
														.getLimitId());
										pedValidationDTO.setSublimitValues(objSublimitFun);
										sublimtName.setValue(objSublimitFun);
									}else if( pedValidationDTO.getProcedureDTO() != null && pedValidationDTO.getProcedureDTO().getSublimitName() != null){
										SublimitFunObject objSublimitFun = bean.getSublimitFunMap()
												.get(pedValidationDTO.getProcedureDTO().getSublimitName()
														.getLimitId());
										pedValidationDTO.setSublimitValues(objSublimitFun);
										sublimtName.setValue(objSublimitFun);
									}
									
							     }
								
								if (pedValidationDTO.getSublimitYesOrNo().getId().equals(ReferenceTable.COMMONMASTER_NO)) {
									sublimtName.setValue(null);
									sublimtName.setEnabled(false);
									pedValidationDTO.setSubLimitAmount("NA");
									pedValidationDTO.setSubLimitUtilAmount(0);
									pedValidationDTO.setSubLimitAvaliableAmt(0);
									
								    	sublimitAmtField.setReadOnly(false);
								    	sublimitAmtField.setValue("NA");
								    	sublimitAmtField.setReadOnly(true);
								    	
								    	sublimtUtilizedField.setReadOnly(false);
								    	sublimtUtilizedField.setValue("0");
								    	sublimtUtilizedField.setReadOnly(true);
								    	
								    	sublimitAvaliableField.setReadOnly(false);
								    	sublimitAvaliableField.setValue("0");
								    	sublimitAvaliableField.setReadOnly(true);
								    	
								    calculationMethodForMedicalDecision(component, false);
								}
								
							if(component.getValue() == null){
								sublimtName.setValue(null);	
								sublimtName.setEnabled(true);
							}
						}
					  }
					}

					
				});
			}

		}
	   	
	   	private void setValuesToNull(TextField sublimitAmt, ComboBox sublimtName) {		
			sublimtName.setReadOnly(false);
			sublimtName.setValue(null);
			sublimtName.setReadOnly(true);;
			sublimtName.setEnabled(false);
			
			sublimitAmt.setReadOnly(false);
			sublimitAmt.setValue("");
			sublimitAmt.setReadOnly(true);
			sublimitAmt.setEnabled(false);
		}
	   	
	   	@SuppressWarnings("unchecked")
		public void addSublimtValues(ComboBox comboBox) {
			List<SublimitFunObject> list =  (List<SublimitFunObject>) referenceData.get("sublimitDBDetails");
			BeanItemContainer<SublimitFunObject> sublimit = new BeanItemContainer<SublimitFunObject>(SublimitFunObject.class);
			sublimit.addAll(list);
			comboBox.setContainerDataSource(sublimit);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("name");

		}
	   	
		@SuppressWarnings("unused")
		private void addSublimitValueChangeLisener(final GComboBox sublimitApplicableCombo) {
			if (sublimitApplicableCombo != null) {
				sublimitApplicableCombo.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						GComboBox component = (GComboBox) event.getComponent();
						DiagnosisProcedureTableDTO pedValidationDTO = (DiagnosisProcedureTableDTO) component.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(pedValidationDTO);
						ComboBox sublimtName = (ComboBox) hashMap.get("sublimitValues");
						System.out.println("--the hashMap----"+hashMap);
						
						TextField diagName = (TextField) hashMap.get("diagOrProcedure");
						
						boolean isMapAvailable =  false; 
						
						/**
						 *  CR R1136 - start
						 */
						if(pedValidationDTO.getDiagnosisDetailsDTO() != null && pedValidationDTO.getDiagnosisDetailsDTO().getIcdCode() != null && pedValidationDTO.getDiagnosisDetailsDTO().getIcdCode().getId() != null){
							SublimitFunObject sublimitObj = icdSublimitMapService.getSublimitDetailsBasedOnIcdCode(pedValidationDTO.getDiagnosisDetailsDTO().getIcdCode().getId());
							if(sublimitObj != null){
								isMapAvailable = true;
								pedValidationDTO.getDiagnosisDetailsDTO().setSublimitMapAvailable(true);
							}
						}
						
						
						if(!isMapAvailable){
						
						if(sublimtName != null && sublimtName.getValue() != null){
						
						Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
						caluculationInputValues.put("policyNumber", bean
								.getPolicyDto().getPolicyNumber());
						caluculationInputValues.put("insuredId", bean
								.getNewIntimationDTO().getInsuredPatient().getInsuredId());
						
						if (pedValidationDTO.getDiagnosisDetailsDTO() != null) {
							caluculationInputValues.put("restrictedSI",
									pedValidationDTO
									.getDiagnosisDetailsDTO()
									.getSumInsuredRestriction() != null ? pedValidationDTO
									.getDiagnosisDetailsDTO()
									.getSumInsuredRestriction().getId()
									: null);
							caluculationInputValues.put("diagOrProcId",
									pedValidationDTO.getDiagnosisDetailsDTO().getDiagnosisName() == null ? 0l : pedValidationDTO.getDiagnosisDetailsDTO().getDiagnosisName().getId());
							caluculationInputValues.put("diagnosisId",
									pedValidationDTO.getDiagnosisDetailsDTO()
											.getDiagnosisName().getId().toString());
							caluculationInputValues.put("referenceFlag", "D");
							
							pedValidationDTO.getDiagnosisDetailsDTO().setSublimitApplicableFlag("Y");
							
							SublimitFunObject dataValue = null;
							dataValue = component.getValue() !=  null ? (SublimitFunObject)component.getValue() : null;

							caluculationInputValues
							.put("sublimitId",
									dataValue != null ? dataValue.getLimitId() : null);
							
							if(dataValue != null){
								pedValidationDTO.getDiagnosisDetailsDTO().setSublimitName(dataValue);
							}
							
							
							
						}else if(pedValidationDTO.getProcedureDTO() != null){
							
							caluculationInputValues.put("restrictedSI", null);
							caluculationInputValues.put("restrictedSIAmount", null);
							
							caluculationInputValues
									.put("diagOrProcId", pedValidationDTO.getProcedureDTO().getProcedureName() == null ? 0l : (pedValidationDTO.getProcedureDTO().getProcedureName().getId() == null ? 0l : pedValidationDTO.getProcedureDTO().getProcedureName().getId()));
							caluculationInputValues.put("referenceFlag", "P");
							
							pedValidationDTO.getProcedureDTO().setSublimitApplicableFlag("Y");
							
							SublimitFunObject dataValue = null;
							dataValue = component.getValue() !=  null ? (SublimitFunObject)component.getValue() : null;

							caluculationInputValues
							.put("sublimitId",
									dataValue != null ? dataValue.getLimitId() : null);
							if(dataValue != null){
								pedValidationDTO.getProcedureDTO().setSublimitName(dataValue);
							}
							
						}

						caluculationInputValues.put(SHAConstants.CLAIM_KEY, bean.getClaimKey());
						caluculationInputValues.put("preauthKey", bean.getKey());
						
						if(bean.getIsHospitalizationRepeat()) {
							caluculationInputValues.put("preauthKey",0l);
						}

						if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.BILLING)){
							fireViewEvent(BillingProcessPagePresenter.EDIT_SUBLIMIT_VALUES, caluculationInputValues, pedValidationDTO,bean);
						}else if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)){
							pedValidationDTO.setIsDiagnosisSublimitChanged(true);
							fireViewEvent(UpdateSublimitPresenter.EDIT_SUBLIMIT_VALUES_FOR_UPDATE_SUBLIMIT, caluculationInputValues, pedValidationDTO,bean);
							if(pedValidationDTO.getDiagnosisDetailsDTO() !=null && pedValidationDTO.getDiagnosisDetailsDTO().getSublimitName().getDescription() != null){
								isSublimitchanged = true;
								pedValidationDTO.setIsDiagnosisSublimitChanged(true);
							} else if(pedValidationDTO.getProcedureDTO() != null && pedValidationDTO.getProcedureDTO().getSublimitName().getDescription() != null){
								isSublimitchanged = true;
								pedValidationDTO.setIsProcedureSublimitChanged(true);
							}
						}
//						calculationMethodForMedicalDecision(component, false);
					 }else{
						 if(pedValidationDTO.getProcedureDTO() != null){
							 pedValidationDTO.getProcedureDTO().setSublimitApplicableFlag("N");
							 //pedValidationDTO.getProcedureDTO().getSublimitName().setLimitId(null);
						 }else if(pedValidationDTO.getDiagnosisDetailsDTO() != null){
							 pedValidationDTO.getDiagnosisDetailsDTO().setSublimitApplicableFlag("N");
							 //pedValidationDTO.getDiagnosisDetailsDTO().getSublimitName().setLimitId(null);
						 }
					 }
					}
						else if(isMapAvailable && ("y").equalsIgnoreCase(pedValidationDTO.getDiagnosisDetailsDTO().getSublimitApplicableFlag())){
							if(component.getValue() != null && pedValidationDTO.getDiagnosisDetailsDTO().getSublimitName() != null && pedValidationDTO.getDiagnosisDetailsDTO().getSublimitName().getDescription() != null 
									&& !(pedValidationDTO.getDiagnosisDetailsDTO().getSublimitName().getDescription()).equalsIgnoreCase(((SublimitFunObject)component.getValue()).getDescription())){
								alertForSublimitChanges("<b style = 'color: red;'>Sublimit cannot be changed, Refer to Medical approver to change the Sublimit.</b>");
								component.setValue(pedValidationDTO.getDiagnosisDetailsDTO().getSublimitName());
							}
						}
				}
					
				});
			}

		}
	   	
	   	public void setAppropriateValuesToDTOFromProcedure(DiagnosisProcedureTableDTO medicalDecisionDto, Map<String, Object> values) {
	   		HashMap<String, AbstractField<?>> hashMap = tableItem.get(medicalDecisionDto);
	   		
	   		medicalDecisionDto.setSubLimitAmount(((Double) values
					.get("currentSL")).intValue() > 0 ? (String
					.valueOf(((Double) values.get("currentSL"))
							.intValue())) : "NA");
			medicalDecisionDto.setSubLimitUtilAmount(((Double) values
					.get("SLUtilAmt")).intValue());
			medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
					.get("SLAvailAmt")).intValue());
			
			TextField sublimitAvaliableField = (TextField)hashMap.get("subLimitAvaliableAmt");
			TextField sublimitAmtField = (TextField)hashMap.get("subLimitAmount");
			TextField sublimtUtilizedField = (TextField)hashMap.get("subLimitUtilAmount");
			
		    if(medicalDecisionDto.getSubLimitAmount() != null && sublimitAmtField != null){
		    	sublimitAmtField.setReadOnly(false);
		    	sublimitAmtField.setValue(medicalDecisionDto.getSubLimitAmount().toString());
		    	sublimitAmtField.setReadOnly(true);
		    }
		    if(medicalDecisionDto.getSubLimitUtilAmount() != null && sublimtUtilizedField != null){
		    	sublimtUtilizedField.setReadOnly(false);
		    	sublimtUtilizedField.setValue(medicalDecisionDto.getSubLimitUtilAmount().toString());
		    	sublimtUtilizedField.setReadOnly(true);
		    }
		    if(medicalDecisionDto.getSubLimitAvaliableAmt() != null && sublimitAvaliableField != null){
		    	sublimitAvaliableField.setReadOnly(false);
		    	sublimitAvaliableField.setValue(medicalDecisionDto.getSubLimitAvaliableAmt().toString());
		    	sublimitAvaliableField.setReadOnly(true);
		    }
			
	   		
	   	}
	   	
	   	public void alertForSublimitChanges(String errMsg) {
			
			//Label successLabel = new Label("<b style = 'color: green;'>"+strMessage+"  ROD is pending for financial approval.</br> Please try submitting this ROD , after " +strMessage+"  is approved.</b>", ContentMode.HTML);
			Label successLabel = new Label(errMsg, ContentMode.HTML);
			
//			Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
			
			Button homeButton = new Button("Ok");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					bean.setIsAlertSublimtChanges(false);
				}
			});
			
		}
	   	
	   	@SuppressWarnings("unchecked")
		public void addCopayTypeValues(ComboBox comboBox) {
	   		if(null != referenceData){
		   		BeanItemContainer<SelectValue> coPayTypeValue = (BeanItemContainer<SelectValue>) referenceData.get("coPayType");
				comboBox.setContainerDataSource(coPayTypeValue);
				comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				comboBox.setItemCaptionPropertyId("value");
	   		}

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
					addCopayPercentageBasedOnCopayType(component,
							coPayTypeValue);
				}

			
			};
			
			return listener;
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
	   	 
	   	private boolean isSublimitMapAvailableForDiagnosis(String diagName)
	   	{
	   		boolean result = false;
	   		List<DiagnosisDetailsTableDTO>  diagList = this.bean
			.getPreauthDataExtractionDetails().getDiagnosisTableList();
	   		
	   		if(diagList != null && !diagList.isEmpty()){
	   			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagList) {
	   				if(diagnosisDetailsTableDTO.isSublimitMapAvailable() && diagnosisDetailsTableDTO.getDiagnosis().equalsIgnoreCase(diagName)){
	   					result = true;
	   					break;
	   				}
				}
	   		}
	   		return result;
	   	}
	   	
		public boolean isSublimitChanged(){
	   		boolean result = false;
	   		if(isSublimitchanged != null && isSublimitchanged){
	   			result = true;
	   		}
	   		isSublimitchanged = false;
	   		return result;
	   	}
		
}
