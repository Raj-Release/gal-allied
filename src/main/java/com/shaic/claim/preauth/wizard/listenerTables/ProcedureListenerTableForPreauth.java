package com.shaic.claim.preauth.wizard.listenerTables;

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

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
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

public class ProcedureListenerTableForPreauth extends ViewComponent {
	private static final long serialVersionUID = 8583255555159403961L;

	private Map<ProcedureDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ProcedureDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ProcedureDTO> data = new BeanItemContainer<ProcedureDTO>(ProcedureDTO.class);

	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private Long hospitalKey;
	
	private String hosptialCode;
	
	private String packageRateValue;
	
	private String dayCareFlagValue;
	
	private String procedureCodeValue;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private PreauthDTO bean;
	
	public List<String> diagnosisList = new ArrayList<String>();
	
	//private List<DiagnosisDetailsTableDTO> diagnosisList;
	
	public void init(String hospitalCode,PreauthDTO bean) {
	//public void init(Long hospitalKey, List<DiagnosisDetailsTableDTO> diagnosisList) {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		//this.diagnosisList = diagnosisList;
		this.errorMessages = new ArrayList<String>();
		diagnosisList = new ArrayList<String>();
//		this.hospitalKey = hospitalKey;
		this.hosptialCode = hospitalCode;
		this.bean = bean;
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
				
				
				
					BeanItem<ProcedureDTO> addItem = data.addItem(new ProcedureDTO());
				
				
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
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        	Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
		
		table.setVisibleColumns(new Object[] { "procedureName", "procedureCode", "packageRate", "dayCareProcedure", "considerForDayCare", "sublimitApplicable", "sublimitName", "sublimitDesc", "sublimitAmount","considerForPayment", "remarks", "Delete"  });

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
			ProcedureDTO procedureDTO = (ProcedureDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			Boolean isEnabled = (null != procedureDTO && null != procedureDTO.getRecTypeFlag() && procedureDTO.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c") ) ? false: true;
//			if(!"premedicalEnhancement".equalsIgnoreCase(pre) && !isEnabled) {
//				isEnabled = true;
//			}
			if (tableItem.get(procedureDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(procedureDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(procedureDTO);
			}
			
			if("procedureName".equals(propertyId)) {
				GComboBox box = new GComboBox();
				addProcedureNameValues(box);
				box.setEnabled(isEnabled);
				tableRow.put("procedureName", box);
				final ComboBox procedureCode = (ComboBox) tableRow.get("procedureCode");
				box.setData(procedureDTO);
				addProcedureNameListener(box, procedureCode);
				return box;
			} else if ("procedureCode".equals(propertyId)) {
				GComboBox box = new GComboBox();
				addProcedureCodeValues(box);
				tableRow.put("procedureCode", box);
				final ComboBox procedureName = (ComboBox) tableRow.get("procedureName");
				box.setData(procedureDTO);
				box.setEnabled(isEnabled);
				addProcedureCodeListener(box, procedureName);
				return box;
			} else if ("packageRate".equals(propertyId)) {
				TextField field = new TextField();
				field.setMaxLength(10);
				field.setNullRepresentation("");
				field.setWidth("150px");
				field.setReadOnly(true);
				tableRow.put("packageRate", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("dayCareProcedure".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setWidth("150px");
				field.setReadOnly(true);
				tableRow.put("dayCareProcedure", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			} else if("considerForDayCare".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100px");
				tableRow.put("considerForDayCare", box);
				addCommonValues(box, "sublimitApplicable");
				box.setEnabled(isEnabled);
				return box;
			} else if("sublimitApplicable".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100px");
				box.setData(procedureDTO);
				tableRow.put("sublimitApplicable", box);
				addSublimitApplicableValues(box, "sublimitApplicable");
				addSublimitApplicableListener(box);
				box.setEnabled(isEnabled);
				return box;
			} else if("sublimitName".equals(propertyId)) {
				GComboBox box = new GComboBox();
				tableRow.put("sublimitName", box);
				final TextField field = (TextField) tableRow.get("sublimitAmount");
				box.setData(procedureDTO);
				addSublimtValues(box);
				addSublimitListener(box, field);
				box.setEnabled(isEnabled);
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
//				if(procedureDTO.getSublimitName() != null) {
//					setSublimitValues(field, sublimitDesc, (SublimitFunObject)sublimitCombo.getValue());
//				}
				if(procedureDTO.getSublimitApplicable() != null && procedureDTO.getSublimitApplicable().getId() == ReferenceTable.COMMONMASTER_NO) {
					setValuesTONull(field, sublimtDesc, sublimtName);
				}
				field.setEnabled(isEnabled);
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
				box.setWidth("100px");
				tableRow.put("considerForPayment", box);
				addCommonValues(box, "considerForPayment");
				box.setEnabled(isEnabled);
				return box;
			} else if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setWidth("200px");
				field.setMaxLength(100);
				tableRow.put("remarks", field);
				field.setEnabled(isEnabled);
				return field;
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				field.setEnabled(isEnabled);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
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
	
	private void addProcedureNameValues(ComboBox box) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> procedure = (BeanItemContainer<SelectValue>) referenceData.get("procedureName");
		box.setContainerDataSource(procedure);
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
	public void addSublimtValues(ComboBox comboBox) {
		List<SublimitFunObject> list =  (List<SublimitFunObject>) referenceData.get("sublimitDBDetails");
		BeanItemContainer<SublimitFunObject> sublimit = new BeanItemContainer<SublimitFunObject>(SublimitFunObject.class);
		sublimit.addAll(list);
		comboBox.setContainerDataSource(sublimit);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("name");

	}
	
	private void addSublimitListener(final ComboBox sublimitCombo,
			final TextField text) {
		if (sublimitCombo != null) {
			sublimitCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

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
								setSublimitValues(field, sublimtDesc, sublimitName);
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

	
	private void setSublimitValues(TextField field,
			TextField sublimtDesc,
			SublimitFunObject sublimitName) {
		field.setReadOnly(false);
		field.setValue(sublimitName.getAmount() != null ? sublimitName.getAmount().toString() : null);
		field.setReadOnly(true);
		sublimtDesc.setReadOnly(false);
		sublimtDesc.setValue(sublimitName.getDescription());
		sublimtDesc.setReadOnly(true);
	}
	
	private void addProcedureNameListener(final ComboBox procedureName,
			ComboBox procedureCode) {
		if (procedureName != null) {
			procedureName.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					//component.getUI().getSession().getLockInstance().lock();
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
//			        		resetValues(procedureCode,packageRate,dayCareProcedure);
			        		dialog.show(getUI().getCurrent(), null, true);
			        		return;
						}
						String strProcValue = selectValue.getValue();
						if(null != diagnosisList && !diagnosisList.isEmpty())
						{								
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
					        		dialog.show(getUI().getCurrent(), null, true);
					        		
					        		packageRate.setReadOnly(false);
					        		packageRate.setEnabled(true);
					        		packageRate.setValue(null);
					        		packageRate.setNullRepresentation("");
					        		//packageRate.setEnabled(false);
					        		packageRate.setReadOnly(true);
					        		
					        		dayCareProcedure.setReadOnly(false);
					        		dayCareProcedure.setEnabled(true);
					        		dayCareProcedure.setValue(null);
					        		dayCareProcedure.setNullRepresentation("");
					        		//dayCareProcedure.setEnabled(false);
					        		dayCareProcedure.setReadOnly(true);
					        		
					        		SelectValue procedureCodeValue = (SelectValue)procedureCode.getValue();
					        		procedureCodeValue.setValue("");
					        		procedureCode.setValue(procedureCodeValue);
					        		
					        		procedureName.setValue(null);
					        		procedureName.setNullSelectionAllowed(false);
					        		return;
								}
								else if((null != procedureDTO && null == procedureDTO.getProcedureCode()) | (null != procedureDTO && null != procedureDTO.getProcedureCode() && ! (procedureDTO.getProcedureCode()).getId().equals(((SelectValue)component.getValue()).getId()))) {
									SelectValue value = new SelectValue();
									//if(null != procedureDTO && null != procedureDTO.getProcedureName() && null != procedureDTO.getProcedureName().getValue())
									{
										value.setId(procedureDTO.getProcedureName().getId());
										setPackageRateAndDayCareProcedure(procedureDTO.getProcedureName().getId(),procedureDTO.getProcedureName().getValue(), packageRate, dayCareProcedure, procedureCode);
									}
								} 
							}
						}
						
					}
					
					if(null == component.getValue()) {
						setPackageRateAndDayCareProcedure(null ,null, packageRate, dayCareProcedure, procedureCode);
					} else if((null != procedureDTO && null != component.getValue() && null == procedureDTO.getProcedureCode()) | (null != procedureDTO && null != procedureDTO.getProcedureCode() && ! (procedureDTO.getProcedureCode()).getId().equals(((SelectValue)component.getValue()).getId()))) {
						SelectValue value = new SelectValue();
						if(procedureDTO.getProcedureName() != null) {
							value.setId(procedureDTO.getProcedureName().getId());
							setPackageRateAndDayCareProcedure(procedureDTO.getProcedureName().getId(),procedureDTO.getProcedureName().getValue(), packageRate, dayCareProcedure, procedureCode);
						}
					} 
				}
			});
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
					TextField dayCareProcedure = (TextField) hashMap.get("dayCareProcedure");
					
					
					if(packageRate != null && packageRate.getValue() != null){
						String strPackageRate = packageRate.getValue();
						Double rate = Double.valueOf(strPackageRate);
						procedureDTO.setPackageRate(rate.longValue());
					}
					
					if(null == component.getValue()) {
						setPackageRateAndDayCareProcedure(null,null, packageRate, dayCareProcedure, procedureNameCombo);
					} else if((null != procedureDTO && null != component.getValue() && null == procedureDTO.getProcedureName()) | (null != procedureDTO && null != procedureDTO.getProcedureName() && ! (procedureDTO.getProcedureName()).getId().equals(((SelectValue)component.getValue()).getId()))) {
						hashMap.put("procedureCode", component);
						if(procedureDTO.getProcedureCode() != null) {
							setPackageRateAndDayCareProcedure(procedureDTO.getProcedureCode().getId(),procedureDTO.getProcedureName().getValue(), packageRate, dayCareProcedure, procedureNameCombo);
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
					if(field !=null && sublimtDesc !=null && sublimtName !=null){
						if(procedureDTO.getSublimitApplicable()!=null){
							if (procedureDTO.getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
								/*field.setEnabled(true);
								sublimtDesc.setEnabled(true);
								sublimtName.setEnabled(true);*/
								field.setEnabled(true);
								field.setReadOnly(false);
								sublimtDesc.setEnabled(true);
								sublimtDesc.setReadOnly(false);
								sublimtName.setEnabled(true);
								sublimtName.setReadOnly(false);
							} else if (procedureDTO.getSublimitApplicable()
									.getId().equals(ReferenceTable.COMMONMASTER_NO)) {
								/*field.setEnabled(false);
								sublimtDesc.setEnabled(false);
								sublimtName.setEnabled(false);*/
								field.setEnabled(false);
								setValuesTONull(field, sublimtDesc, sublimtName);
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
					if(value != null && value.getId() == ReferenceTable.COMMONMASTER_NO  && field != null && sublimtDesc != null && sublimtName != null) {
						setValuesTONull(field, sublimtDesc, sublimtName);
					}
				}

				
			});
		}

	}

	/*private void setValuesTONull(TextField field,
			TextField sublimtDesc, ComboBox sublimtName) {
		if(field != null && sublimtDesc != null && sublimtName != null) {
			field.setReadOnly(false);
			field.setValue("");
			field.setReadOnly(true);
			sublimtDesc.setReadOnly(false);
			sublimtDesc.setValue("");
			sublimtDesc.setReadOnly(true);
			sublimtName.setValue(null);
		}
		
	}*/
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
    	manageListeners();
    }

	public void setPackageRate(Map<String, String> values) {
		this.packageRateValue = values.get("packageRate");
		this.dayCareFlagValue = values.get("dayCareProcedure");
		this.procedureCodeValue = values.get("procedureCode");
	}
	
	private void setPackageRateAndDayCareProcedure(Long procedureKey,String procedureCode, TextField packageRate, TextField dayCareProcedure, ComboBox procedureCodeCombo) {
		if(procedureKey != null) {
			fireViewEvent(PreauthWizardPresenter.PREAUTH_GET_PACKAGE_RATE, procedureKey,hosptialCode,bean);
		}
		
		if(packageRate != null) {
			packageRate.setReadOnly(false);
			packageRate.setValue(packageRateValue);
			if(procedureKey == null) {
				packageRate.setValue(null);
			}
			
			packageRate.setMaxLength(10);
			packageRate.setReadOnly(false);
		}
		if(dayCareProcedure != null) {
			dayCareProcedure.setReadOnly(false);
			dayCareProcedure.setValue(dayCareFlagValue);
			if(procedureKey == null) {
				dayCareProcedure.setValue(null);
			}
			dayCareProcedure.setReadOnly(true);
		}
		
		
		if(procedureCodeCombo != null) {
			SelectValue value = new SelectValue();
			value.setId(procedureKey);
			value.setValue(procedureCodeValue);
			procedureCodeCombo.setValue(value);
			if(procedureKey == null) {
				procedureCodeCombo.setValue(null);
			}
			
		}
		
	}
	
	
	
	private void manageListeners() {

		for (ProcedureDTO procedureDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(procedureDTO);

			final ComboBox sublimitNameCombo = (ComboBox) combos.get("sublimitName");
			final TextField sublimitAmt = (TextField) combos.get("sublimitAmount");
			final TextField sublimitDesc = (TextField) combos.get("sublimitDesc");
			SublimitFunObject value = (SublimitFunObject) sublimitNameCombo.getValue();
			
			if(value != null) {
				procedureDTO.setSublimitName(value);
				addSublimitListener(sublimitNameCombo, sublimitAmt);
				setSublimitValues(sublimitAmt, sublimitDesc, procedureDTO.getSublimitName());
			}
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
			
			if(bean.getSublimitApplicable() != null && bean.getSublimitApplicable().getId() == ReferenceTable.COMMONMASTER_YES && bean.getSublimitName() == null) {
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
			}
			
			if(bean.getProcedureName() == null || bean.getProcedureCode() == null) {
				hasError = true;
				errorMessages.add("Please choose Procedure Name or Procedure Code.");
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
			errorMessages.add("Procedure Table - Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced . ");
		}
		return !hasError;
	}
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	

	public void removeRow() {
		table.removeAllItems();

	}
	
	@SuppressWarnings("unchecked")
	public void changeSublimitValues() {

		for (ProcedureDTO pedValidationTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(pedValidationTableDTO);
			
			final ComboBox sublimitCombo = (ComboBox) combos.get("sublimitName");
			GComboBox sublimitApplicableCombo = (GComboBox) combos.get("sublimitApplicable");
			addSublimitApplicableValues(sublimitApplicableCombo, "sublimitApplicable");
			addSublimtValues(sublimitCombo);
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
	}

}
