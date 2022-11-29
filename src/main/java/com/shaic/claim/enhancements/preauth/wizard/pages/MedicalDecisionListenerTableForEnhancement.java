package com.shaic.claim.enhancements.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.cashlessprocess.downsize.wizard.DownSizePreauthWizardPresenter;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
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

public class MedicalDecisionListenerTableForEnhancement extends ViewComponent {
	private static final long serialVersionUID = 3618294170496450898L;

	private Map<MedicalDecisionTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<MedicalDecisionTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<MedicalDecisionTableDTO> data = new BeanItemContainer<MedicalDecisionTableDTO>(MedicalDecisionTableDTO.class);

	private Table table;
	
	PreauthDTO bean;
	
//	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
//	private static Validator validator;
	
	public Double totalApprovedAmount=0.0;
	
	public Boolean isIndedpendentDownsize = false;
	
	// True for add amount false for Cumulative amt.
	private Boolean mutuallyExclusive = true;
	
	public Object[] VISIBLE_COLUMNS = new Object[] {
			"referenceNo", "treatmentType", "procedureOrDiagnosis","description", "pedOrExclusionDetails", "currentSubLimitAmount","sumInsuredRestriction", "subLimitUtilizedAmount", "availableSublimit", "packageRate", "approvedAmount"};
		

		public Object[] APPROVE_VISIBLE_COLUMNS = new Object[] {
				"referenceNo", "treatmentType","procedureOrDiagnosis","description", "pedOrExclusionDetails", "currentSubLimitAmount","sumInsuredRestriction", "subLimitUtilizedAmount", "availableSublimit", "packageRate", "approvedAmount", "addedAmount", "cumulativeAmt", "totalApprovedAmt"};

		public Object[] DOWNSIZE_VISIBLE_COLUMNS = new Object[] {
				"referenceNo", "treatmentType","procedureOrDiagnosis","description", "pedOrExclusionDetails", "currentSubLimitAmount","sumInsuredRestriction", "subLimitUtilizedAmount", "availableSublimit", "packageRate", "approvedAmount", "reduceAmount", "downSizeCumulativeAmt", "totalApprovedAmt"};
		
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
			
			if("addedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(25);
				validator.extend(field);
				field.setData(medicalDecisionDTO);
				validator.setRegExp("^[0-9]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("addedAmount", field);
				addListenerForAddedAmtorCumulativeAmt(field, false);
				if(!medicalDecisionDTO.getIsEnabled()) {
					medicalDecisionDTO.setAddedAmount("0");
					field.setEnabled(false);
					field.setValue("0");
				}
				return field;
			} 
			else if("reduceAmount".equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				CSValidator validator=new CSValidator();
				field.setMaxLength(25);
				validator.extend(field);
				field.setData(medicalDecisionDTO);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("reduceAmount", field);
				addListenerForReducedAmtorCumulativeAmt(field,false);
				if(!medicalDecisionDTO.getIsEnabled()) {
					medicalDecisionDTO.setReduceAmount("0");
					field.setEnabled(false);
					field.setValue("0");
				}
				return field;
				}
			else if("cumulativeAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(25);
				validator.extend(field);
				field.setData(medicalDecisionDTO);
				validator.setRegExp("^[0-9]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("cumulativeAmt", field);
				addListenerForAddedAmtorCumulativeAmt(field, true);
				if(!medicalDecisionDTO.getIsEnabled()) {
					medicalDecisionDTO.setCumulativeAmt("0");
					field.setEnabled(false);
					field.setValue("0");
				}
				return field;
			}
			else if("downSizeCumulativeAmt".equals(propertyId)){
				
				TextField field=new TextField();
				field.setNullRepresentation("");
				CSValidator validator=new CSValidator();
				field.setMaxLength(25);
				validator.extend(field);
				field.setData(medicalDecisionDTO);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("downSizeCumulativeAmt", field);
				addListenerForReducedAmtorCumulativeAmt(field,true);
				if(!medicalDecisionDTO.getIsEnabled()) {
					medicalDecisionDTO.setDownSizeCumulativeAmt("0");
					field.setEnabled(false);
					field.setValue("0");
				}
				return field;
				
			}
			else if("approvedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(25);
				validator.extend(field);
				field.setData(medicalDecisionDTO);
				validator.setRegExp("^[0-9]*$");	
				validator.setPreventInvalidTyping(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("approvedAmount", field);
				calculateTotal();
//				if(!(medicalDecisionDTO.getReferenceNo() != null && medicalDecisionDTO.getReferenceNo().contains("Residual Treatment"))) {
//					addApporvedAmtListener(field);
//				}
				
				return field;
			}
			else if("totalApprovedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(25);
				validator.extend(field);
				field.setData(medicalDecisionDTO);
				validator.setRegExp("^[0-9]*$");	
				validator.setPreventInvalidTyping(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				tableRow.put("totalApprovedAmt", field);
//				if(!(medicalDecisionDTO.getReferenceNo() != null && medicalDecisionDTO.getReferenceNo().contains("Residual Treatment"))) {
//					addApporvedAmtListener(field);
//				}
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
		table.setColumnHeader("approvedAmount", "Approved Amt (Till Date)");
		table.setColumnHeader("addedAmount", "(+) Add");
		table.setColumnHeader("reduceAmount", "(-)Reduce");
		table.setColumnHeader("cumulativeAmt", "Cumulative Amt");
		table.setColumnHeader("downSizeCumulativeAmt", "Cumulative Amt");
		table.setColumnHeader("totalApprovedAmt", "Total Amt Approved");
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
	
	/*@SuppressWarnings("unused")
	private void addApporvedAmtListener(TextField approvedAmtField) {
		if (approvedAmtField != null) {
			approvedAmtField.addBlurListener(new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {

					TextField component = (TextField) event.getComponent();
					Integer enteredAmt = SHAUtils.getFloatFromString(component.getValue());
					if(enteredAmt > 0) {
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
							calculateAddAndCumulativeTotal();
							return;
						}
						
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(medicalDecisionDTO);
						
						String amountConsidered = bean.getAmountConsidered();
						Integer amtConsidered = SHAUtils.getFloatFromString(amountConsidered);
						Integer floatValue = bean.getBalanceSI().intValue();
						Integer minValue = Math.min(amtConsidered, floatValue);
						Integer calculatedTotal = calculateTotal();
						
						if(calculatedTotal > minValue) {
							VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red;'>Total Amount should be lesser of Amount considered and Balance SI </b>", ContentMode.HTML));
							layout.setMargin(true);
							showNotificationAlert(layout);
							return;
						}
						
					}
				
				}
			}); 
		}

	}*/
	
	@SuppressWarnings("unused")
	private void addListenerForAddedAmtorCumulativeAmt(TextField addOrCumulativeField, final Boolean isCumulative) {
		if (addOrCumulativeField != null) {
			addOrCumulativeField.addBlurListener(new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					
					
					TextField component = (TextField) event.getComponent();
					MedicalDecisionTableDTO medicalDecisionDTO = (MedicalDecisionTableDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(medicalDecisionDTO);
					TextField field = (TextField) hashMap.get("totalApprovedAmt");
					TextField addAmtField = (TextField) hashMap.get("addedAmount");
					TextField cumulativeField = (TextField) hashMap.get("cumulativeAmt");
					Integer enteredAmt = SHAUtils.getFloatFromString(component.getValue());
					
					if(SHAUtils.isValidFloat(component.getValue())) {
						disableMutuallyExculsibleField(isCumulative, false, false);
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
						
						
						
						if(SHAUtils.isValidFloat(component.getValue())) {
							Integer approvedAmt = SHAUtils.getFloatFromString(medicalDecisionDTO.getApprovedAmount());
							
							if(medicalDecisionDTO.getReferenceNo().contains("Residual")) {
								Boolean totalCalculation = totalCalculation(isCumulative, component, field);
								if(!isCumulative) {
//									Float sum = approvedAmt + enteredAmt;
									cumulativeField.setValue("");
									cumulativeField.setEnabled(false);
//									field.setReadOnly(false);
//									field.setValue(sum.toString());
//									field.setReadOnly(true);
									calculateAddAndCumulativeTotal();
								} else {
									addAmtField.setValue("");
									addAmtField.setEnabled(false);
////									field.setReadOnly(false);
//									field.setValue(enteredAmt.toString());
//									field.setReadOnly(true);
									calculateAddAndCumulativeTotal();
								}
								enhancementAmtCalculation(approvedAmt, enteredAmt, isCumulative, medicalDecisionDTO, field, totalCalculation);
								return;
							}
							Boolean totalValidation = true;
							if(enteredAmt >= 0) {
								Integer availableSublimit = SHAUtils.getFloatFromString(medicalDecisionDTO.getAvailableSublimit()) +  SHAUtils.getFloatFromString(medicalDecisionDTO.getSubLimitUtilizedAmount());
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
									calculateAddAndCumulativeTotal();
									return;
								}
								totalValidation = totalCalculation(isCumulative, component, field);
							}
							
							if(field != null) {
								if(!isCumulative) {
									cumulativeField.setValue("");
									cumulativeField.setEnabled(false);
//									field.setReadOnly(false);
//									Float sum = approvedAmt + enteredAmt;
//									field.setValue(sum.toString());
//									field.setReadOnly(true);
//									setProcessFlagAndActions(medicalDecisionDTO, "A");
									calculateAddAndCumulativeTotal();							
								} else {
									addAmtField.setValue("");
									addAmtField.setEnabled(false);
//									field.setReadOnly(false);
//									field.setValue(enteredAmt.toString());
//									field.setReadOnly(true);
//									setProcessFlagAndActions(medicalDecisionDTO, "C");
									calculateAddAndCumulativeTotal();
								}
								enhancementAmtCalculation(approvedAmt, enteredAmt, isCumulative, medicalDecisionDTO, field, totalValidation);
							}
						} else {
							field.setReadOnly(false);
							field.setValue("");
							field.setReadOnly(true);
							addAmtField.setValue("");
							cumulativeField.setValue("");
							calculateAddAndCumulativeTotal();
						}
					} else {
						if(isAllBlank("addedAmount", "cumulativeAmt", isCumulative)) {
							
							disableMutuallyExculsibleField(isCumulative, false, true);
						}
						calculateAddAndCumulativeTotal();
						totalCalculation(isCumulative, component, field);
					}
			
				}
			});
		}
	}
	
	public void enhancementAmtCalculation(Integer approvedAmt, Integer enteredAmt, Boolean isCumulative, MedicalDecisionTableDTO dto, TextField totalAmtField, Boolean totalValidation) {
		/* If cumulative is entered then Calculation is Approved Amt - A, Add Amount - B, Cumulative Amount - C, Total Approved Amount - D
		 * Add Amount - A-C
		 * Cumulative Amount - A+B
		 * Total Approved Amount - A+B
		 *  */
		Integer cumulativeAmt = 0;
		if(totalValidation) {
			if(isCumulative) {
				Integer addAmount = enteredAmt - approvedAmt;
				cumulativeAmt = approvedAmt + addAmount;
			} else {
				cumulativeAmt = approvedAmt + enteredAmt;
				Integer addAmount = approvedAmt - cumulativeAmt;
			}
			totalAmtField.setReadOnly(false);
			totalAmtField.setValue(cumulativeAmt.toString());
			totalAmtField.setReadOnly(true);
			calculateAddAndCumulativeTotal();
		}
		//C - A
		Double diffAmt = (Double.valueOf(cumulativeAmt) - Double.valueOf(approvedAmt));
		if(dto.getProcedureDTO() != null) {
			ProcedureDTO procedureDTO = dto.getProcedureDTO();
			procedureDTO.setOldApprovedAmount(procedureDTO.getApprovedAmount());
			procedureDTO.setApprovedAmount(Double.valueOf(cumulativeAmt));
			procedureDTO.setProcessFlag("E");
			procedureDTO.setActions(isCumulative ? "C" : "A");
			procedureDTO.setOldDiffAmount(procedureDTO.getDiffAmount());
			procedureDTO.setDiffAmount(diffAmt);
		} else if(dto.getPedValidationTableDTO() != null) {
			DiagnosisDetailsTableDTO diagnosisDetailsDTO = dto.getDiagnosisDetailsDTO();
			diagnosisDetailsDTO.setOldApprovedAmount(diagnosisDetailsDTO.getApprovedAmount());
			diagnosisDetailsDTO.setApprovedAmount(Double.valueOf(cumulativeAmt));
			diagnosisDetailsDTO.setProcessFlag("E");
			diagnosisDetailsDTO.setActions(isCumulative ? "C" : "A");
			diagnosisDetailsDTO.setOldDiffAmount(diagnosisDetailsDTO.getDiffAmount());
			diagnosisDetailsDTO.setDiffAmount(diffAmt);
		}
		
	}
	
	public void downSizeAmtCalculation(Integer approvedAmt, Integer enteredAmt, Boolean isCumulative, MedicalDecisionTableDTO dto, TextField totalAmtField, Boolean totalValidation) {
		/* If cumulative is entered then Calculation is Approved Amt - A, Add Amount - B, Cumulative Amount - C, Total Approved Amount - D
		 * Add Amount - A-C
		 * Cumulative Amount - A+B
		 * Total Approved Amount - A+B
		 *  */
		Integer cumulativeAmt = 0;
		if(totalValidation) {
			if(isCumulative) {
				Integer addAmount = approvedAmt - enteredAmt;
				cumulativeAmt = approvedAmt - addAmount;
			} else {
				cumulativeAmt = approvedAmt - enteredAmt;
				Integer addAmount = approvedAmt - cumulativeAmt;
			}
			totalAmtField.setReadOnly(false);
			totalAmtField.setValue(cumulativeAmt.toString());
			totalAmtField.setReadOnly(true);
			calculateAddAndCumulativeTotal();
			String columnFooterValue = "0";
			if(isCumulative) {
				columnFooterValue = table.getColumnFooter("cumulativeAmt");
			} else {
				columnFooterValue = table.getColumnFooter("reduceAmount");
			}
			 String totalApprovedAmt = table.getColumnFooter("totalApprovedAmt");
			bean.setDownsizeTotalAppAmt(new Double(SHAUtils.getDoubleFromString(totalApprovedAmt)));
		}
		
		// A - C (Diff Amt)
		Double diffAmt = (Double.valueOf(approvedAmt) - Double.valueOf(cumulativeAmt));
		if(dto.getProcedureDTO() != null) {
			ProcedureDTO procedureDTO = dto.getProcedureDTO();
			if(procedureDTO.getOldApprovedAmount() == null) {
				procedureDTO.setOldApprovedAmount(procedureDTO.getApprovedAmount());
			}
			procedureDTO.setApprovedAmount(Double.valueOf(cumulativeAmt));
			procedureDTO.setProcessFlag("E");
			procedureDTO.setActions(isCumulative ? "C" : "R");
			procedureDTO.setOldDiffAmount(procedureDTO.getDiffAmount());
			procedureDTO.setDiffAmount(diffAmt);
		} else if(dto.getPedValidationTableDTO() != null) {
			DiagnosisDetailsTableDTO diagnosisDetailsDTO = dto.getDiagnosisDetailsDTO();
			if(diagnosisDetailsDTO.getOldApprovedAmount() == null) {
				diagnosisDetailsDTO.setOldApprovedAmount(diagnosisDetailsDTO.getApprovedAmount());
			}
			diagnosisDetailsDTO.setApprovedAmount(Double.valueOf(cumulativeAmt));
			diagnosisDetailsDTO.setProcessFlag("E");
			diagnosisDetailsDTO.setActions(isCumulative ? "C" : "R");
			diagnosisDetailsDTO.setOldDiffAmount(diagnosisDetailsDTO.getDiffAmount());
			diagnosisDetailsDTO.setDiffAmount(diffAmt);
		}
	}
	
	@SuppressWarnings("unused")
	private void addListenerForReducedAmtorCumulativeAmt(TextField addOrCumulativeField, final Boolean isCumulative) {
		
		if (addOrCumulativeField != null) {
			
			addOrCumulativeField.addBlurListener(new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					MedicalDecisionTableDTO medicalDecisionDTO = (MedicalDecisionTableDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(medicalDecisionDTO);
					TextField field = (TextField) hashMap.get("totalApprovedAmt");
					TextField addAmtField = (TextField) hashMap.get("reduceAmount");
					TextField cumulativeField = (TextField) hashMap.get("downSizeCumulativeAmt");
					Integer enteredAmt = SHAUtils.getFloatFromString(component.getValue());
					
					if(SHAUtils.isValidFloat(component.getValue())) {
						disableMutuallyExculsibleFieldForDownsize(isCumulative, true, false);
						
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
						
						if(SHAUtils.isValidFloat(component.getValue())){
						Integer approvedAmt = SHAUtils.getFloatFromString(medicalDecisionDTO.getApprovedAmount());
						if(medicalDecisionDTO.getReferenceNo().contains("Residual")) {
							Boolean totalCalculation = totalDownSizeCalculation(isCumulative, component, field);
							if(!isCumulative) {
//								Float sum = approvedAmt + enteredAmt;
								cumulativeField.setValue("");
								cumulativeField.setEnabled(false);
//								field.setReadOnly(false);
//								field.setValue(sum.toString());
//								field.setReadOnly(true);
								calculateReduceAndCumulativeTotal();
							} else {
								addAmtField.setValue("");
								addAmtField.setEnabled(false);
////								field.setReadOnly(false);
//								field.setValue(enteredAmt.toString());
//								field.setReadOnly(true);
								calculateReduceAndCumulativeTotal();
							}
							downSizeAmtCalculation(approvedAmt, enteredAmt, isCumulative, medicalDecisionDTO, field, totalCalculation);
							return;
						}
						
						Boolean totalValidation = true;
						if(enteredAmt > 0 ) {
							Integer availableSublimit = SHAUtils.getFloatFromString(medicalDecisionDTO.getAvailableSublimit()) +  SHAUtils.getFloatFromString(medicalDecisionDTO.getSubLimitUtilizedAmount());
							Integer packageAmt = SHAUtils.getFloatFromString(medicalDecisionDTO.getPackageRate());
							Integer minimumAmt = availableSublimit;
							if(packageAmt > 0) {
								minimumAmt = Math.min(availableSublimit, packageAmt);
							}
							if(enteredAmt > minimumAmt && isSublimitAvailable && isCumulative) {
								VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red;'>Approved Amount should be lesser of Available Sublimit or Package Amount. </b>", ContentMode.HTML));
								layout.setMargin(true);
								component.setValue("0");
								showNotificationAlert(layout);
								calculateReduceAndCumulativeTotal();
								return;
							}
							
							if(!isCumulative && enteredAmt > SHAUtils.getFloatFromString(medicalDecisionDTO.getOldApprovedAmount() == null ? medicalDecisionDTO.getApprovedAmount() :medicalDecisionDTO.getOldApprovedAmount() )) {
								VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red;'>Reduced Amount should be lesser of Current Approved Amount. </b>", ContentMode.HTML));
								layout.setMargin(true);
								component.setValue("0");
								showNotificationAlert(layout);
								calculateReduceAndCumulativeTotal();
								return;
							}
							totalValidation = totalDownSizeCalculation(isCumulative, component, field);
						}
						
						//Float approvedAmt = SHAUtils.getFloatFromString(medicalDecisionDTO.getApprovedAmount());
						
						if(field != null) {
							if(!isCumulative) {
								cumulativeField.setValue("");
								cumulativeField.setEnabled(false);
//								field.setReadOnly(false);
//								Float sum = approvedAmt - enteredAmt;
//								table.setColumnFooter("totalApprovedAmt", String.valueOf(sum));
//								field.setValue(sum.toString());
//								field.setReadOnly(true);
								calculateReduceAndCumulativeTotal();
								
							} 
						else {
								addAmtField.setValue("");
								addAmtField.setEnabled(false);
//								field.setReadOnly(false);
//								Float sum = approvedAmt + enteredAmt;
//								table.setColumnFooter("totalApprovedAmt", String.valueOf(sum));
//								field.setValue(sum.toString());
//								field.setReadOnly(true);
								calculateReduceAndCumulativeTotal();
							}
							downSizeAmtCalculation(approvedAmt, enteredAmt, isCumulative, medicalDecisionDTO, field, totalValidation);
							
						}
						
						}
						else
						{
							field.setReadOnly(false);
							field.setValue("");
							field.setReadOnly(true);
							addAmtField.setValue("");
							cumulativeField.setValue("");
							calculateReduceAndCumulativeTotal();
						}
						if(isIndedpendentDownsize) {
							fireViewEvent(DownSizePreauthWizardPresenter.SET_DOWNSIZE_AMOUNT, Double.valueOf("" + enteredAmt));
						}				
		
					} else {
						if(isAllBlank("reduceAmount", "downSizeCumulativeAmt", isCumulative)) {
							disableMutuallyExculsibleFieldForDownsize(isCumulative, true, true);
						}
						calculateReduceAndCumulativeTotal();
						totalDownSizeCalculation(isCumulative, component, field);
					}
					
				}
			});
		}
	}
	
	private Boolean totalCalculation(Boolean isCumulative, TextField enteredField, TextField totalApprovedAmt) {
		String amountConsidered = bean.getAmountConsidered();
		Integer amtConsidered = SHAUtils.getFloatFromString(amountConsidered);
		float floatValue = bean.getBalanceSI().floatValue();
		float minValue = Math.min(amtConsidered, floatValue);
	
		calculateAddAndCumulativeTotal();
		float calculatedTotal = new Float("0");
		if(!isCumulative) {
			calculatedTotal = SHAUtils.getFloatFromString(table.getColumnFooter("addedAmount"));
		} else {
			calculatedTotal = SHAUtils.getFloatFromString(table.getColumnFooter("cumulativeAmt"));
		}
		
		if(!SHAUtils.isValidFloat(enteredField.getValue())) {
			totalApprovedAmt.setReadOnly(false);
			totalApprovedAmt.setValue(null);
			totalApprovedAmt.setReadOnly(true);
			calculateAddAndCumulativeTotal();
		}
		
//		if(calculatedTotal <= 0 && (!isAllBlank("addedAmount", "cumulativeAmt", true) || !isAllBlank("addedAmount", "cumulativeAmt", false)) )
//		{
//			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red;'>Total Approved Amount should be greater than 0.</b>", ContentMode.HTML));
//			layout.setMargin(true);
//			totalApprovedAmt.setReadOnly(false);
//			totalApprovedAmt.setValue(null);
//			totalApprovedAmt.setReadOnly(true);
//			if(SHAUtils.isValidFloat(enteredField.getValue())) {
//				showNotificationAlert(layout);
//			}
//			
//			return false;
//		}
		
		if(calculatedTotal > minValue) {
			/**
			 * Changing color from yellow to red, since the message was not visible in yellow color
			 * */
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red;'>Total Amount should be lesser of Amount considered and Balance SI </b>", ContentMode.HTML));
			layout.setMargin(true);
			showNotificationAlert(layout);
			enteredField.setValue("0");
			totalApprovedAmt.setReadOnly(false);
			totalApprovedAmt.setValue("0");
			totalApprovedAmt.setReadOnly(true);
			calculateAddAndCumulativeTotal();
			return false;
		}
		
		return true;
	}
	
	private Boolean totalDownSizeCalculation(Boolean isCumulative, TextField enteredField, TextField totalApprovedAmt) {
//		String amountConsidered = bean.getAmountConsidered();
//		Float amtConsidered = SHAUtils.getFloatFromString(amountConsidered);
//		float floatValue = bean.getBalanceSI().floatValue();
//		float minValue = Math.min(amtConsidered, floatValue);
		float minValue=0l;
		
		
		calculateReduceAndCumulativeTotal();
		float calculatedTotal = new Float("0");
		if(!isCumulative) {
			calculatedTotal = SHAUtils.getFloatFromString(table.getColumnFooter("reduceAmount"));
		} else {
			calculatedTotal = SHAUtils.getFloatFromString(table.getColumnFooter("downSizeCumulativeAmt"));
		}
		if(calculatedTotal < minValue) {
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color:red;'>Total Amount should be lesser of Amount considered and Balance SI </b>", ContentMode.HTML));
			layout.setMargin(true);
			showNotificationAlert(layout);
			enteredField.setValue("0");
			totalApprovedAmt.setReadOnly(false);
			totalApprovedAmt.setValue("0");
			totalApprovedAmt.setReadOnly(true);
			calculateReduceAndCumulativeTotal();
			return false;
		}
	   return true;
	}
	
	
	private void setProcessAndActionFlag(Boolean isCumulative) {
		
	}
	
	
	private void setProcessFlagAndActions(
			MedicalDecisionTableDTO medicalDecisionDTO, String action) {
		if(medicalDecisionDTO.getProcedureDTO() !=  null){
			ProcedureDTO procedureDTO = medicalDecisionDTO.getProcedureDTO();
			procedureDTO.setProcessFlag("E");
			procedureDTO.setActions(action);
		} else if(medicalDecisionDTO.getPedValidationTableDTO() != null) {
			DiagnosisDetailsTableDTO pedValidationDTO =  medicalDecisionDTO.getPedValidationTableDTO();
			pedValidationDTO.setProcessFlag("E");
			pedValidationDTO.setAction(action);
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
	//	dummyField.setValue(String.valueOf(total));
		return total;
	}
	
	public Float calculateAddAndCumulativeTotal() {
		
		List<MedicalDecisionTableDTO> itemIconPropertyId = (List<MedicalDecisionTableDTO>) table.getItemIds();
		Float total = 0.0f;
		Float addedAmt = 0.0f;
		Float cumulativeAmt = 0.0f;
		for (MedicalDecisionTableDTO dto : itemIconPropertyId) {
			String totalApprovedAmount = dto.getTotalApprovedAmt();
			String addedAmount = dto.getAddedAmount();
			String cumulativeAmount = dto.getCumulativeAmt();
			total += SHAUtils.getFloatFromString(totalApprovedAmount);
			addedAmt += SHAUtils.getFloatFromString(addedAmount);
			cumulativeAmt += SHAUtils.getFloatFromString(cumulativeAmount);
		}
		
		table.setColumnFooter("totalApprovedAmt", String.valueOf(total));
		dummyField.setValue(String.valueOf(total));
		table.setColumnFooter("addedAmount", String.valueOf(addedAmt));
		table.setColumnFooter("cumulativeAmt", String.valueOf(cumulativeAmt));
		return total;
	}
	
	public Float calculateReduceAndCumulativeTotal() {
		
		List<MedicalDecisionTableDTO> itemIconPropertyId = (List<MedicalDecisionTableDTO>) table.getItemIds();
		Float total = 0.0f;
		Float reduceAmt = 0.0f;
		Float cumulativeAmt = 0.0f;
		for (MedicalDecisionTableDTO dto : itemIconPropertyId) {
			String totalApprovedAmount = dto.getTotalApprovedAmt();
			String reduceAmount = dto.getReduceAmount();
			String cumulativeAmount = dto.getDownSizeCumulativeAmt();
			total += SHAUtils.getFloatFromString(totalApprovedAmount);
			reduceAmt += SHAUtils.getFloatFromString(reduceAmount);
			cumulativeAmt += SHAUtils.getFloatFromString(cumulativeAmount);
		}
		
		table.setColumnFooter("totalApprovedAmt", String.valueOf(total));
		dummyField.setValue(String.valueOf(total));
		table.setColumnFooter("reduceAmount", String.valueOf(reduceAmt));
		table.setColumnFooter("downSizeCumulativeAmt", String.valueOf(cumulativeAmt));
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
	 public void setVisibleDownSizeFields(Boolean isDownSize){
		 if(isDownSize) {
			 table.setVisibleColumns(DOWNSIZE_VISIBLE_COLUMNS);
		 }
		 else
		 {
			 table.setVisibleColumns(VISIBLE_COLUMNS);
		 }
	 }
	 
	 public boolean isValid() {
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			Collection<MedicalDecisionTableDTO> itemIds = (Collection<MedicalDecisionTableDTO>) table.getItemIds();
			for (MedicalDecisionTableDTO bean : itemIds) {
				
				if(!(bean.getReferenceNo() != null && bean.getReferenceNo().contains("Residual")) && ((bean.getAddedAmount() == null || bean.getAddedAmount().length() == 0)) && (bean.getCumulativeAmt() == null || bean.getCumulativeAmt().length() == 0)) {
					hasError = true;
					errorMessages.add("Please Enter Add Amount or Cumulative Amount </br>");
				}
			}
			return !hasError;
	}
		public List<String> getErrors()
		{
			return this.errorMessages;
		}
		
		public void disableMutuallyExculsibleField(Boolean isCumulative, Boolean isDownSize, Boolean isEnabled) {

			for (MedicalDecisionTableDTO medicalDecisionDTO : tableItem.keySet()) {
				HashMap<String, AbstractField<?>> combos = tableItem.get(medicalDecisionDTO);
				TextField addOrReduceAmtField = null;
				addOrReduceAmtField = (TextField) combos.get("addedAmount");
				final TextField cumulativeAmtField = (TextField) combos.get("cumulativeAmt");
				
				if(isEnabled) {
					addOrReduceAmtField.setEnabled(isEnabled);
					cumulativeAmtField.setEnabled(isEnabled);
				} else {
					if(isCumulative) {
						if(addOrReduceAmtField != null && addOrReduceAmtField.isEnabled()) {
							addOrReduceAmtField.setEnabled(isEnabled);
						}
					} else {
						
						if(cumulativeAmtField != null && cumulativeAmtField.isEnabled()) {
							cumulativeAmtField.setEnabled(isEnabled);
						}
					}
				}
				
				
			}
		}
		
		public void disableMutuallyExculsibleFieldForDownsize(Boolean isCumulative, Boolean isDownSize, Boolean isEnabled) {

			for (MedicalDecisionTableDTO medicalDecisionDTO : tableItem.keySet()) {
				HashMap<String, AbstractField<?>> combos = tableItem.get(medicalDecisionDTO);
				 TextField addOrReduceAmtField = (TextField) combos.get("reduceAmount");
				final TextField cumulativeAmtField = (TextField) combos.get("downSizeCumulativeAmt");
				
				if(isEnabled) {
					addOrReduceAmtField.setEnabled(isEnabled);
					cumulativeAmtField.setEnabled(isEnabled);
				} else{
					if(isCumulative) {
						if(addOrReduceAmtField != null && addOrReduceAmtField.isEnabled()) {
							addOrReduceAmtField.setEnabled(isEnabled);
						}
					} else {
						if(cumulativeAmtField != null && cumulativeAmtField.isEnabled()) {
							cumulativeAmtField.setEnabled(isEnabled);
						}
					}
				}
				
			}
		}
		
		public Double getTotalDownSizeApprAmount(){
			return this.bean.getDownsizeTotalAppAmt();
		}
		
		public Boolean isAllBlank(String addedAmtNameStr, String cumulativeAmtStr, Boolean isCumulative) {
			Boolean isBlank = true;
			for (MedicalDecisionTableDTO medicalDecisionDTO : tableItem.keySet()) {
				HashMap<String, AbstractField<?>> combos = tableItem.get(medicalDecisionDTO);
				TextField addAmtField = (TextField) combos.get(addedAmtNameStr);
				TextField cumulativeAmtField = (TextField) combos.get(cumulativeAmtStr);
				if(isCumulative) {
					if(SHAUtils.isValidFloat(cumulativeAmtField.getValue())) {
						isBlank = false;
						break;
					}
				} else {
					if(SHAUtils.isValidFloat(addAmtField.getValue())) {
						isBlank = false;
						break;
					}
				}
			}
			
			return isBlank;
		}
		
		public Boolean isValidAddOrCumulative() {
			Integer addTotal = SHAUtils.getFloatFromString(table.getColumnFooter("addedAmount"));
			Integer cumulativeTotal = SHAUtils.getFloatFromString(table.getColumnFooter("cumulativeAmt"));
			
			if(addTotal == 0 && cumulativeTotal == 0) {
				return false;
			}
			
			return true;
		}
		
		public Boolean isValidReducedOrCumulative() {
			Integer addTotal = SHAUtils.getFloatFromString(table.getColumnFooter("reduceAmount"));
			Integer cumulativeTotal = SHAUtils.getFloatFromString(table.getColumnFooter("downSizeCumulativeAmt"));
			
			if(addTotal == 0 && cumulativeTotal == 0) {
				return false;
			}
			
			return true;
		}
	
}
