package com.shaic.paclaim.cashless.listenertables;

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
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPagePresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancemetWizardPresenter;
import com.shaic.paclaim.cashless.fle.wizard.wizardfiles.PAPremedicalEnhancementWizardPresenter;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizardPresenter;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardPresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.dataextraction.PAHealthClaimRequestDataExtractionPagePresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PAProcedureListenerTableForPremedical extends ViewComponent {
	private static final long serialVersionUID = 4809460534159116589L;
	
	private Map<ProcedureDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ProcedureDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ProcedureDTO> data = new BeanItemContainer<ProcedureDTO>(ProcedureDTO.class);

	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private String hospitalCode;
	
	private String packageRateValue;
	
	private String dayCareFlagValue;
	
	private String procedureCodeValue;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private String presenterString;
	
	private PreauthDTO bean;
	
	
	//private List<DiagnosisDetailsTableDTO> diagnosisList;
	public List<String> diagnosisList = new ArrayList<String>();
	public void init(String hosptialCode, String presenterString, PreauthDTO bean) {
	//public void init(Long hospitalKey, String presenterString,List<DiagnosisDetailsTableDTO> diagnosisList) {
		this.presenterString = presenterString;
		this.bean = bean;
		//this.diagnosisList = diagnosisList;
		diagnosisList = new ArrayList<String>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
//		this.hospitalKey = hospitalKey;
		this.hospitalCode = hosptialCode;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(btnLayout);
		layout.setMargin(true);
		
		initTable(layout);
		table.setWidth("100%");
		//table.setHeight("30%");
		/**
		 * Height is set for table visiblity.
		 * */
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}


	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				ProcedureDTO procedureDTO = new ProcedureDTO();
				procedureDTO.setEnableOrDisable(true);
				BeanItem<ProcedureDTO> addItem = data.addItem(procedureDTO);
			}
		});
	}
	
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Procedure List", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	deleteButton.setData(itemId);
		    	ProcedureDTO dto = (ProcedureDTO) itemId;
		    	if(dto.getEnableOrDisable() != null) {
		    		deleteButton.setEnabled(dto.getEnableOrDisable());
				}
		    	
		    	if(dto.getRecTypeFlag() != null && dto.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c")) {
					deleteButton.setEnabled(false);
				}
		    	
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        	ProcedureDTO currentItemId = (ProcedureDTO) event.getButton().getData();
			        	if(table.getItemIds().size() > 0) {
			        		if(currentItemId.getKey() != null) {
			        			if(!bean.getDeletedProcedure().contains(currentItemId)) {
			        				bean.getDeletedProcedure().add(currentItemId);
			        			}
			        			
							}
			        		table.removeItem(currentItemId);
			        	} else {
			        		HorizontalLayout layout = new HorizontalLayout(new Label("One Procedure is Mandatory."));
			        		layout.setMargin(true);
			        		final ConfirmDialog dialog = new ConfirmDialog();
			        		dialog.setCaption("");
			        	//	dialog.setClosable(false);
			        		dialog.setClosable(true);
			        		dialog.setContent(layout);
			        		dialog.setResizable(false);
			        		dialog.setWidth("250px");
			        		dialog.setModal(true);
			        		dialog.show(getUI().getCurrent(), null, true);
			        	}
						
			        } 
			    });
		    	//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
		
		table.setVisibleColumns(new Object[] { "procedureName", "procedureCode", "packageRate", /*"dayCareProcedure", "considerForDayCare", "sublimitApplicable", "sublimitName", "sublimitDesc", "sublimitAmount","considerForPayment",*/ "remarks", "Delete"  });

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
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			ProcedureDTO procedureDTO = (ProcedureDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			//Boolean isEnabled =  procedureDTO.getEnableOrDisable() ? true : false;
			
			Boolean isEnabled = (null != procedureDTO && null != procedureDTO.getRecTypeFlag() && procedureDTO.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c") ) ? false: true;
			if(!"premedicalEnhancement".equalsIgnoreCase(presenterString) && !isEnabled) {
				isEnabled = true;
			}
			if (tableItem.get(procedureDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(procedureDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(procedureDTO);
			}
			
			if("procedureName".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				addProcedureNameValues(box);
				tableRow.put("procedureName", box);
				final ComboBox procedureCode = (ComboBox) tableRow.get("procedureCode");
				box.setData(procedureDTO);
				addProcedureNameListener(box, procedureCode);
				return box;
			} else if ("procedureCode".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				addProcedureCodeValues(box);
				tableRow.put("procedureCode", box);
				final ComboBox procedureName = (ComboBox) tableRow.get("procedureName");
				box.setData(procedureDTO);
				addProcedureCodeListener(box, procedureName);
				return box;
			} else if ("packageRate".equals(propertyId)) {
				TextField field = new TextField();
				field.setMaxLength(10);
				field.setEnabled(isEnabled);
				field.setNullRepresentation("");
				field.setWidth("150px");
				field.setReadOnly(true);
				field.setData(procedureDTO);
				tableRow.put("packageRate", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("dayCareProcedure".equals(propertyId)) {
				TextField field = new TextField();
				field.setEnabled(isEnabled);
				field.setNullRepresentation("");
				field.setWidth("150px");
				field.setReadOnly(true);
				tableRow.put("dayCareProcedure", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("considerForDayCare".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setWidth("100px");
				tableRow.put("considerForDayCare", box);
				addCommonValues(box, "sublimitApplicable");
				SelectValue value = new SelectValue();
				value.setId(ReferenceTable.COMMONMASTER_NO);
				value.setValue("No");
				procedureDTO.setConsiderForDayCare(value);
				box.setEnabled(false);
				return box;
			} else if("sublimitApplicable".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setWidth("100px");
				box.setData(procedureDTO);
				tableRow.put("sublimitApplicable", box);
				if(true){
					SelectValue value = new SelectValue();
					value.setId(ReferenceTable.COMMONMASTER_NO);
					value.setValue("No");
					procedureDTO.setSublimitApplicable(value);
				}
				addSublimitApplicableValues(box, "sublimitApplicable");
				addSublimitApplicableListener(box);
				box.setEnabled(false);
				return box;
			} else if("sublimitName".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				tableRow.put("sublimitName", box);
				final TextField field = (TextField) tableRow.get("sublimitAmt");
				box.setData(procedureDTO);
				box.setEnabled(false);
//				addSublimtValues(box);
//				addSublimitListener(box, field);
				return box;
			} else if("sublimitAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("sublimitAmount", field);
				TextField sublimtDesc = (TextField) tableRow.get("sublimitDesc");
				ComboBox sublimtName = (ComboBox) tableRow.get("sublimitName");
//				if(procedureDTO.getSublimitApplicable() != null && procedureDTO.getSublimitApplicable().getId() == ReferenceTable.COMMONMASTER_NO) {
//					setValuesTONull(field, sublimtDesc, sublimtName);
//				}
				return field;
			} else if("sublimitDesc".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("sublimitDesc", field);
				return field;
			}
			else if ("considerForPayment".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setWidth("100px");
				tableRow.put("considerForPayment", box);
				addCommonValues(box, "considerForPayment");
				return box;
			} else if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setEnabled(isEnabled);
				field.setNullRepresentation("");
				field.setWidth("200px");
				field.setMaxLength(100);
//				CSValidator validator = new CSValidator();				
//				validator.extend(field);
//				validator.setRegExp("^[a-zA-Z 0-9/]*$");
//				validator.setPreventInvalidTyping(true);
				tableRow.put("remarks", field);
				return field;
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setEnabled(isEnabled);
					field.setWidth("100%");
				return field;
			}
		}

		private void addProcedureCodeValues(ComboBox box) {
			@SuppressWarnings("unchecked")
			BeanItemContainer<SelectValue> procedureCode = (BeanItemContainer<SelectValue>) referenceData
					.get("procedureCode");
			box.setContainerDataSource(procedureCode);
			box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			box.setItemCaptionPropertyId("value");
			
		}
		
		private void addCommonValues(ComboBox diagnosisCombo, String tableColumnName) {
			@SuppressWarnings("unchecked")
			BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
					.get(tableColumnName);
			diagnosisCombo.setContainerDataSource(commonValues);
			diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			diagnosisCombo.setItemCaptionPropertyId("value");
			
		}
		
		
		
		
		
	
		
		@SuppressWarnings("unused")
		private void addSublimitListener(final ComboBox sublimitCombo,
				final TextField text) {
			if (sublimitCombo != null) {
				sublimitCombo.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;
					private TextField sittingsField;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						ProcedureDTO procedureDTO = (ProcedureDTO) component.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(procedureDTO);
						TextField field = (TextField) hashMap.get("sublimitAmount");
						TextField sublimtDesc = (TextField) hashMap.get("sublimitDesc");
						GComboBox sublimitApplicable = (GComboBox) hashMap.get("sublimitApplicable");
						
						if(sublimitApplicable != null && sublimitApplicable.getValue() == null){
							component.setValue(null);
						}
						
						
						if (procedureDTO != null) {
							SublimitFunObject sublimitName = procedureDTO.getSublimitName();
							if(sublimitName != null) {
								if(field != null && sublimtDesc != null) {
									field.setReadOnly(false);
									field.setValue(sublimitName.getAmount() != null ? sublimitName.getAmount().toString() : null);
									field.setReadOnly(true);
									sublimtDesc.setReadOnly(false);
									sublimtDesc.setValue(sublimitName.getDescription());
									sublimtDesc.setReadOnly(true);
								}
							}
							else
							{
								if(field != null && sublimtDesc != null) {
									field.setReadOnly(false);
									field.setValue(null);
									field.setReadOnly(true);
									sublimtDesc.setReadOnly(false);
									sublimtDesc.setValue(null);
									sublimtDesc.setReadOnly(true);
								}
							}
						}
					}
				});
			}

		}
		
		@SuppressWarnings("unused")
		private void addSublimitApplicableListener(final ComboBox sublimitApplicableCombo) {
			if (sublimitApplicableCombo != null) {
				sublimitApplicableCombo.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						ProcedureDTO procedureDTO = (ProcedureDTO) component.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(procedureDTO);
						TextField field = (TextField) hashMap.get("sublimitAmount");
						TextField sublimtDesc = (TextField) hashMap.get("sublimitDesc");
						ComboBox sublimtName = (ComboBox) hashMap.get("sublimitName");
						
						
						
						if(field !=null && sublimtDesc !=null && sublimtName!=null){
							if(procedureDTO.getSublimitApplicable()!=null){
								if (procedureDTO.getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
									field.setEnabled(true);
									field.setReadOnly(false);
									sublimtDesc.setEnabled(true);
									sublimtDesc.setReadOnly(false);
									sublimtName.setEnabled(true);
									sublimtName.setReadOnly(false);
								} else if (procedureDTO.getSublimitApplicable()
										.getId().equals(ReferenceTable.COMMONMASTER_NO)) {
									setValuesTONull(field, sublimtDesc, sublimtName);
									/*field.setEnabled(false);
									field.setReadOnly(false);
									field.setValue(null);
									field.setReadOnly(true);
									
									sublimtDesc.setEnabled(false);
									sublimtName.setEnabled(false);*/
									
								}
							}
							if(component.getValue() == null){
								
								field.setReadOnly(false);
								sublimtDesc.setReadOnly(false);
								
								field.setValue(null);
								sublimtDesc.setValue(null);
								sublimtName.setValue(null);
								
								field.setReadOnly(true);							
								sublimtDesc.setReadOnly(true);
								
								sublimtName.setEnabled(true);
							}
						}
						SelectValue value = (SelectValue) component.getValue();
						if(value != null && value.getId().equals(ReferenceTable.COMMONMASTER_NO)  && field != null && sublimtDesc != null && sublimtName != null) {
							setValuesTONull(field, sublimtDesc, sublimtName);
						}
					}

					
				});
			}

		}

		private void setValuesTONull(TextField field,
				TextField sublimtDesc, ComboBox sublimtName) {
			if(field != null && sublimtDesc != null && sublimtName != null) {
				sublimtName.setReadOnly(false);
				sublimtName.setValue(null);
				sublimtName.setReadOnly(true);;
				sublimtName.setEnabled(false);
				
				field.setReadOnly(false);
				field.setValue("");
				field.setReadOnly(true);
				field.setEnabled(false);
			
				sublimtDesc.setReadOnly(false);
				sublimtDesc.setValue(null);
				sublimtDesc.setReadOnly(true);
				sublimtDesc.setEnabled(false);
			}
		}
		
		private void addProcedureNameListener(final ComboBox procedureName,
				ComboBox procedureCode) {
			if (procedureName != null) {
				//String strProValue = (null != procedureName.getValue() ? procedureName.getValue().toString() : "");
				
				/*procedureName.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						SelectValue value = (SelectValue) event.getProperty().getValue();
						if(null != value && null != value.getValue())
						{
							String strProcValue = value.getValue();
							if(null != diagnosisList && !diagnosisList.isEmpty())
							{								
								for(DiagnosisDetailsTableDTO diagnosisObj : diagnosisList)
								{	
									if(strProcValue.equalsIgnoreCase(diagnosisObj.getDiagnosis()))
									{
										HorizontalLayout layout = new HorizontalLayout(new Label("Diagnosis and Procedure are same."));
						        		layout.setMargin(true);
						        		final ConfirmDialog dialog = new ConfirmDialog();
						        		dialog.setCaption("");
						        		dialog.setWidth("50%");
						        		dialog.setClosable(true);
						        		dialog.setContent(layout);
						        		dialog.setResizable(false);
						        		dialog.setModal(true);
						        		dialog.show(getUI().getCurrent(), null, true);
						        		procedureName.setValue(null);
						        		procedureName.setNullSelectionAllowed(false);
						        		return;
									}
								}
							}
							
						}
						
					}
				});*/
				
				procedureName.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						ProcedureDTO procedureDTO = (ProcedureDTO) component.getData();
						
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(procedureDTO);
						ComboBox procedureCode = (ComboBox) hashMap.get("procedureCode");
						TextField packageRate = (TextField) hashMap.get("packageRate");
						TextField dayCareProcedure = (TextField) hashMap.get("dayCareProcedure");
						hashMap.put("procedureName", component);
						SelectValue selectValue = (SelectValue) component.getValue();
						if(null != selectValue && null != selectValue.getValue())
						{
							List<ProcedureDTO> values = getValues();
							int count = 0;
							for (ProcedureDTO eachProcedureDTO : values) {
								if(eachProcedureDTO != null && eachProcedureDTO.getProcedureName() != null && eachProcedureDTO.getProcedureName().getId() != null && procedureDTO.getProcedureName() != null && procedureDTO.getProcedureName().getId() != null && eachProcedureDTO.getProcedureName().getId().equals(procedureDTO.getProcedureName().getId())) {
									count += 1;
								}
							}
							if(count > 1) {
								HorizontalLayout layout = new HorizontalLayout(new Label("Duplicate Procedure is not allowed. Please choose different Procedure"));
				        		layout.setMargin(true);
				        		final ConfirmDialog dialog = new ConfirmDialog();
				        		dialog.setCaption("Warning");
				        		dialog.setClosable(true);
				        		dialog.setContent(layout);
				        		dialog.setResizable(false);
				        		dialog.setModal(true);
				        		component.setValue(null);
				        		resetValues(procedureCode,packageRate,dayCareProcedure);
				        		dialog.show(getUI().getCurrent(), null, true);
				        		return;
							}
							
							String strProcValue = selectValue.getValue();
							if(null != diagnosisList && !diagnosisList.isEmpty())
							{								
								//for(DiagnosisDetailsTableDTO diagnosisObj : diagnosisList)
								for(String strDiagName : diagnosisList)
								{	
									if(strProcValue.equalsIgnoreCase(strDiagName))
									{
										HorizontalLayout layout = new HorizontalLayout(new Label("Diagnosis and Procedure are same."));
						        		layout.setMargin(true);
						        		final ConfirmDialog dialog = new ConfirmDialog();
						        		dialog.setCaption("");
						        		dialog.setWidth("20%");
						        		dialog.setClosable(true);
						        		dialog.setContent(layout);
						        		dialog.setResizable(false);
						        		dialog.setModal(true);
//						        		dialog.show(getUI().getCurrent(), null, true);
						        		
						        		/*packageRate.setReadOnly(false);
						        		//packageRate.setEnabled(true);
						        		packageRate.setValue(null);
						        		packageRate.setNullRepresentation("");
						        		//packageRate.setEnabled(false);
						        		packageRate.setReadOnly(true);
						        		
						        		dayCareProcedure.setReadOnly(false);
						        		//dayCareProcedure.setEnabled(true);
						        		dayCareProcedure.setValue(null);
						        		dayCareProcedure.setNullRepresentation("");
						        		//dayCareProcedure.setEnabled(false);
						        		dayCareProcedure.setReadOnly(true);
						        		
						        		SelectValue procedureCodeValue = (SelectValue)procedureCode.getValue();
						        		procedureCodeValue.setValue("");
						        		procedureCode.setValue(procedureCodeValue);
						        		
						        		procedureName.setValue(null);
						        		procedureName.setNullSelectionAllowed(false);*/
//						        		procedureName.setNullSelectionAllowed(true);
//						        		procedureName.setValue(null);
//						        		resetValues(procedureCode,packageRate,dayCareProcedure);
						        		return;
									}
		
									else if((null != procedureDTO && null == procedureDTO.getProcedureCode()) | (null != procedureDTO && null != procedureDTO.getProcedureCode() &&  (procedureDTO.getProcedureCode()).getId().equals(((SelectValue)component.getValue()).getId()))) {
										SelectValue value = new SelectValue();
										//if(null != procedureDTO && null != procedureDTO.getProcedureName() && null != procedureDTO.getProcedureName().getValue())
										{
											value.setId(procedureDTO.getProcedureName().getId());
											setPackageRateAndDayCareProcedure(procedureDTO.getProcedureName().getId(),procedureDTO.getProcedureName().getValue(), packageRate, dayCareProcedure, procedureCode);
										}
									} 
								}
							}
							else if((null != procedureDTO && null == procedureDTO.getProcedureCode()) | (null != procedureDTO && null != procedureDTO.getProcedureCode() &&  (procedureDTO.getProcedureCode()).getId().equals(((SelectValue)component.getValue()).getId()))) {
								SelectValue value = new SelectValue();
								//if(null != procedureDTO && null != procedureDTO.getProcedureName() && null != procedureDTO.getProcedureName().getValue())
								{
									value.setId(procedureDTO.getProcedureName().getId());
									setPackageRateAndDayCareProcedure(procedureDTO.getProcedureName().getId(),procedureDTO.getProcedureName().getValue(), packageRate, dayCareProcedure, procedureCode);
								}
							}
							
						}
						else 
						{
							/*procedureCode.setNullSelectionAllowed(true);
							procedureCode.setValue(null);
							packageRate.setReadOnly(false);
			        		packageRate.setValue(null);
			        		packageRate.setNullRepresentation("");

			        		packageRate.setReadOnly(true);
			        		dayCareProcedure.setReadOnly(false);
			        		dayCareProcedure.setValue(null);
			        		dayCareProcedure.setNullRepresentation("");
			        		dayCareProcedure.setReadOnly(true);*/
							resetValues(procedureCode,packageRate,dayCareProcedure);
						} 
					}
					
					
				});
				
		
				
			}
		}
		
		private void resetValues(ComboBox cmbProcCode , TextField txtPackageRate ,TextField txtDayCareProcedure )
		{
			if(cmbProcCode != null) {
				cmbProcCode.setNullSelectionAllowed(true);
				cmbProcCode.setValue(null);
			}
			
			if(txtPackageRate != null) {
				txtPackageRate.setReadOnly(false);
				txtPackageRate.setValue(null);
				txtPackageRate.setNullRepresentation("");
				txtPackageRate.setReadOnly(true);
			}
			
			if(txtDayCareProcedure != null) {
				txtDayCareProcedure.setReadOnly(false);
				txtDayCareProcedure.setValue(null);
				txtDayCareProcedure.setNullRepresentation("");
				txtDayCareProcedure.setReadOnly(true);
			}			
		}
		
		@SuppressWarnings("unused")
		private void addProcedureCodeListener(ComboBox procedureCode, ComboBox procedureName) {
			if (procedureCode != null) {
				procedureCode.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						ProcedureDTO procedureDTO = (ProcedureDTO) component.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(procedureDTO);
						ComboBox procedureNameCombo = (ComboBox) hashMap.get("procedureName");
						TextField packageRate = (TextField) hashMap.get("packageRate");
						if(packageRate != null && packageRate.getValue() != null){
							String strPackageRate = packageRate.getValue();
							//Double rate = Double.valueOf(strPackageRate);
							Double rate = SHAUtils.getDoubleFromStringWithComma(strPackageRate);
							procedureDTO.setPackageRate(rate.longValue());
						}
						
						TextField dayCareProcedure = (TextField) hashMap.get("dayCareProcedure");
						if((null != procedureDTO && null == procedureDTO.getProcedureName()) | (null != procedureDTO && null != procedureDTO.getProcedureName() && ! (procedureDTO.getProcedureName()).getId().equals(((SelectValue)component.getValue()).getId()))) {
							hashMap.put("procedureCode", component);
							if(null != procedureDTO && null != procedureDTO.getProcedureCode())
							{
								setPackageRateAndDayCareProcedure(procedureDTO.getProcedureCode().getId(),procedureDTO.getProcedureCode().getValue(), packageRate, dayCareProcedure, procedureNameCombo);
							}
						} 
//						else if((null != procedureDTO && null == procedureDTO.getProcedureName()) | (null != procedureDTO && null != procedureDTO.getProcedureName() && (procedureDTO.getProcedureName()).getId().equals(((SelectValue)component.getValue()).getId()))){
//							hashMap.put("procedureCode", component);
//							if(null != procedureDTO && null != procedureDTO.getProcedureCode())
//							{
//								setPackageRateAndDayCareProcedure(procedureDTO.getProcedureCode().getId(),procedureDTO.getProcedureCode().getValue(), packageRate, dayCareProcedure, procedureNameCombo);
//							}
//						}
					} 
				});
			}
		}

		private void addProcedureNameValues(ComboBox box) {
			@SuppressWarnings("unchecked")
			BeanItemContainer<SelectValue> procedure = (BeanItemContainer<SelectValue>) referenceData.get("procedureName");
			box.setContainerDataSource(procedure);
			box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			box.setItemCaptionPropertyId("value");
		}
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	 public List<ProcedureDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<ProcedureDTO> itemIds = (List<ProcedureDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	
	public void addBeanToList(ProcedureDTO procedureDTO) {
    	data.addItem(procedureDTO);
    }

	public void setPackageRate(Map<String, String> values) {
		this.packageRateValue = values.get("packageRate");
		this.dayCareFlagValue = values.get("dayCareProcedure");
		this.procedureCodeValue = values.get("procedureCode");
	}
	
	private void setPackageRateAndDayCareProcedure(Long procedureKey,String procedureCode,TextField packageRate, TextField dayCareProcedure, ComboBox procedureCodeCombo) {
		if("premedicalPreauth".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPreMedicalPreauthWizardPresenter.PREMEDICAL_GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		} else if("premedicalEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPremedicalEnhancementWizardPresenter.PREMEDICAL_GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		} else if("preauthEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		}else if (SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(MedicalApprovalDataExtractionPagePresenter.GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		} else if (SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.GET_PACKAGE_RATE,procedureKey,hospitalCode,bean);
		} else if(SHAConstants.PRE_AUTH.equalsIgnoreCase(this.presenterString)) {
			if(procedureKey != null) {
				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_GET_PACKAGE_RATE, procedureKey,hospitalCode,bean);
			}
		}
		if(packageRate != null) {
			packageRate.setReadOnly(false);
			packageRate.setValue(packageRateValue);
			packageRate.setReadOnly(true);
		}
		if(dayCareProcedure != null) {
			dayCareProcedure.setReadOnly(false);
			dayCareProcedure.setValue(dayCareFlagValue);
			dayCareProcedure.setReadOnly(true);
		}
		
		
		if(procedureCodeCombo != null) {
			SelectValue value = new SelectValue();
			value.setId(procedureKey);
			value.setValue(procedureCodeValue);
			procedureCodeCombo.setValue(value);
		}
		
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<ProcedureDTO> itemIds = (Collection<ProcedureDTO>) table.getItemIds();
		Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();
		for (ProcedureDTO bean : itemIds) {
			
			if(bean.getSublimitApplicable() != null && bean.getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES) && bean.getSublimitName() == null) {
				hasError = true;
				errorMessages.add("Please Select Sublimit Name.");
			}
			
			if(bean.getSublimitApplicable() == null) {
				/*hasError = true;
				errorMessages.add("Please Select Sublimit Applicable.");*/
			}
			
			if(bean.getConsiderForPayment() == null) {
				/*hasError = true;
				errorMessages.add("Please Select Consider For Payment.");*/
			}
			
			if(bean.getProcedureName() == null || bean.getProcedureCode() == null) {
				hasError = true;
				errorMessages.add("Please choose Procedure Name or Procedure Code.");
			}
			
			if(bean.getConsiderForDayCare()==null){
				SelectValue consider = new SelectValue();
				consider.setId(ReferenceTable.COMMONMASTER_YES);
				consider.setValue("YES");
				bean.setConsiderForDayCare(consider);
			}
			
			Set<ConstraintViolation<ProcedureDTO>> validate = validator.validate(bean);

			if(bean.getSublimitName() != null) {
				if(valuesMap.containsKey(bean.getSublimitName().getLimitId()) && (bean.getConsiderForPayment() == null || (null != bean.getConsiderForPayment() && bean.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)))) {
					validationMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				} else {
					valuesMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				}
			}
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<ProcedureDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		if(!validationMap.isEmpty()) {
			hasError = true;
			errorMessages.add("Procedure Table - Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .");
		}
		return !hasError;
	}
	
	
	public boolean isValidPA()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<ProcedureDTO> itemIds = (Collection<ProcedureDTO>) table.getItemIds();
		Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();
		for (ProcedureDTO bean : itemIds) {
			
			/*if(bean.getSublimitApplicable() != null && bean.getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES) && bean.getSublimitName() == null) {
				hasError = true;
				errorMessages.add("Please Select Sublimit Name.");
			}
			
			if(bean.getSublimitApplicable() == null) {
				hasError = true;
				errorMessages.add("Please Select Sublimit Applicable.");
			}
			
			if(bean.getConsiderForPayment() == null) {
				hasError = true;
				errorMessages.add("Please Select Consider For Payment.");
			}*/
			
			if(bean.getProcedureName() == null || bean.getProcedureCode() == null) {
				hasError = true;
				errorMessages.add("Please choose Procedure Name or Procedure Code.");
			}
			
		//	Set<ConstraintViolation<ProcedureDTO>> validate = validator.validate(bean);

			if(bean.getSublimitName() != null) {
				if(valuesMap.containsKey(bean.getSublimitName().getLimitId()) && (bean.getConsiderForPayment() == null || (null != bean.getConsiderForPayment() && bean.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)))) {
					validationMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				} else {
					valuesMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				}
			}
			/*if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<ProcedureDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}*/
		}
		/*if(!validationMap.isEmpty()) {
			hasError = true;
			errorMessages.add("Procedure Table - Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .");
		}*/
		return !hasError;
	}
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	public void enableOrDisableDeleteButton(final Boolean isEnable) {
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	ProcedureDTO dto = (ProcedureDTO) itemId;
		    		deleteButton.setEnabled(isEnable);
		    		if (!isEnable) {
						if(dto.getRecTypeFlag() != null && !dto.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c")) {
							deleteButton.setEnabled(true);
						}
//						if (dto.getEnableOrDisable() != null) {
//							deleteButton.setEnabled(dto.getEnableOrDisable());
//						}
					}
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        	ProcedureDTO currentItemId = (ProcedureDTO) event.getButton().getData();
			        	if(table.getItemIds().size() > 0) {
			        		if(currentItemId.getKey() != null) {
			        			if(!bean.getDeletedProcedure().contains(currentItemId)) {
			        				bean.getDeletedProcedure().add(currentItemId);
			        			}
							}
			        		table.removeItem(currentItemId);
			        	} else {
			        		HorizontalLayout layout = new HorizontalLayout(new Label("One Diagnosis is Mandatory."));
			        		layout.setMargin(true);
			        		final ConfirmDialog dialog = new ConfirmDialog();
			        		dialog.setCaption("");
			        		dialog.setClosable(true);
			        		dialog.setContent(layout);
			        		dialog.setResizable(false);
			        		dialog.setModal(true);
			        		dialog.show(getUI().getCurrent(), null, true);
			        	}
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
	}
	
	public void removeRow() {
		table.removeAllItems();

	}
	
	@SuppressWarnings("unchecked")
	public void addSublimtValues(ComboBox comboBox) {
		List<SublimitFunObject> list =  (List<SublimitFunObject>) referenceData.get("sublimitDBDetails");
		BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
				.get("sublimitApplicable");
		BeanItemContainer<SublimitFunObject> sublimit = new BeanItemContainer<SublimitFunObject>(SublimitFunObject.class);
		sublimit.addAll(list);
		comboBox.setContainerDataSource(sublimit);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("name");
		if(list.isEmpty()) {
			List<SelectValue> newListValue = new ArrayList<SelectValue>();
			List<SelectValue> itemIds = commonValues.getItemIds();
			SelectValue selected = new SelectValue();
			for (SelectValue selectValue : itemIds) {
				if(selectValue != null && selectValue.getId() != null && selectValue.getId().equals(ReferenceTable.COMMONMASTER_NO)) {
					newListValue.add(selectValue);
					selected = selectValue;
					}
				}
			ProcedureDTO procedureDTO = (ProcedureDTO) comboBox
					.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem
					.get(procedureDTO);
			GComboBox sublimitApplicable = (GComboBox)hashMap.get("sublimitApplicable");
			BeanItemContainer<SelectValue> beanItemContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			beanItemContainer.addAll(newListValue);
//			referenceData.put("sublimitApplicable", beanItemContainer);
			sublimitApplicable.setContainerDataSource(beanItemContainer);
			sublimitApplicable.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			sublimitApplicable.setItemCaptionPropertyId("value");
			if(procedureDTO.getSublimitApplicableFlag() != null &&
					procedureDTO.getSublimitApplicableFlag().equalsIgnoreCase("N")){
				sublimitApplicable.setValue(selected);
			}

		}else{
			ProcedureDTO procedureDTO = (ProcedureDTO) comboBox
					.getData();
			if(procedureDTO.getSublimitName() != null){
				comboBox.setValue(procedureDTO.getSublimitName());
			}
		}

	}
	
	@SuppressWarnings("unchecked")
	private void addSublimitApplicableValues(ComboBox diagnosisCombo, String tableColumnName) {
		
		BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
				.get(tableColumnName);
		
		List<SublimitFunObject> list = (List<SublimitFunObject>) referenceData
				.get("sublimitDBDetails");
		if(list.isEmpty()) {
			List<SelectValue> newListValue = new ArrayList<SelectValue>();
			List<SelectValue> itemIds = commonValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(selectValue != null && selectValue.getId() != null && selectValue.getId().equals(ReferenceTable.COMMONMASTER_NO)) {
					newListValue.add(selectValue);
				}
			}
			commonValues = new BeanItemContainer<SelectValue>(SelectValue.class);
			commonValues.addAll(newListValue);
		}
		diagnosisCombo.setContainerDataSource(commonValues);
		diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisCombo.setItemCaptionPropertyId("value");

	}
	
	@SuppressWarnings("unchecked")
	public void changeSublimitValues() {/*

		for (ProcedureDTO pedValidationTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(pedValidationTableDTO);
			SublimitFunObject obj = new SublimitFunObject();
			if(null !=  pedValidationTableDTO.getSublimitName())
			{
			obj = pedValidationTableDTO.getSublimitName();
			}
			final ComboBox sublimitCombo = (ComboBox) combos.get("sublimitName");
			sublimitCombo.setData(pedValidationTableDTO);
			GComboBox sublimitApplicableCombo = (GComboBox) combos.get("sublimitApplicable");
			addSublimitApplicableValues(sublimitApplicableCombo, "sublimitApplicable");
			addSublimtValues(sublimitCombo);
			
			sublimitCombo.setValue(obj);
//			List<SublimitFunObject> list = (List<SublimitFunObject>) referenceData
//					.get("sublimitDBDetails");
//			if(list!= null && !list.isEmpty()) {
//				if(sublimitApplicableCombo != null) {
//					SelectValue value = new SelectValue();
//					value.setId(ReferenceTable.COMMONMASTER_YES);
//					sublimitApplicableCombo.setValue(value);
//					pedValidationTableDTO.setSublimitApplicable(value);
//				}
//				if(sublimitCombo != null) {
//					pedValidationTableDTO.setSublimitName(list.get(0));
//					sublimitCombo.setValue(list.get(0));
//				}
//				
//			}

		}
	*/}
	
	
}
