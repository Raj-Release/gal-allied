package com.shaic.claim.preauth.wizard.listenerTables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
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
import com.shaic.arch.test.DiagnosisComboBox;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.AbstractSelect.NewItemHandler;
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

public class DiganosisDetailsListenerForPreauth extends ViewComponent {
	private static final long serialVersionUID = 7802397137014194525L;
	
	/**
	 * Due to vaadin limitation, we are storing the editable abstract field in the haspmap for manipulating the values in the table
	 * <code>String</code> will hold the propery of the column please refer ImmediateTableFactory class
	 */
	private Map<DiagnosisDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DiagnosisDetailsTableDTO, HashMap<String, AbstractField<?>>>();

	/***
	 * Bean object fetch from db
	 */
	BeanItemContainer<DiagnosisDetailsTableDTO> data = new BeanItemContainer<DiagnosisDetailsTableDTO>(DiagnosisDetailsTableDTO.class);
	
	@EJB
	private MasterService masterService;
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private BeanItemContainer<SelectValue> icdBlock;
	
	private BeanItemContainer<SelectValue> icdCode;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	public TextField dummyField = new TextField();
	
	private PreauthDTO bean;
	
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		//dummyField = new TextField();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("100%");
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
				//BeanItem<DiagnosisDetailsTableDTO> addItem = data.addItem(new DiagnosisDetailsTableDTO());
				data.addItem(new DiagnosisDetailsTableDTO());
				manageListeners();
			}
		});
	}
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("Diagnosis Details", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//Added for table height..
		table.setHeight("160px");
		
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
		
		table.setVisibleColumns(new Object[] { "diagnosisName", "icdChapter", "icdBlock", "icdCode", "sublimitApplicable", "sublimitName", "sublimitAmt", "considerForPayment", "sumInsuredRestriction", "Delete" });

		table.setColumnHeader("diagnosisName", "Diagnosis");
		table.setColumnHeader("icdChapter", "ICD Chapter");
		table.setColumnHeader("icdBlock", "ICD Block");
		table.setColumnHeader("icdCode", "ICD Code");
		table.setColumnHeader("sublimitApplicable", "Sub Limit Applicable");
		table.setColumnHeader("sublimitName", "Sub Limit Name");
		table.setColumnHeader("sublimitAmt", "Sub Limit Amount");
		table.setColumnHeader("considerForPayment", "Consider For Payment");
		table.setColumnHeader("sumInsuredRestriction", "SI Restriction");
		table.setEditable(true);
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}
	
	public void manageListeners() {

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(pedValidationTableDTO);

			final ComboBox icdChapterCombo = (ComboBox) combos.get("icdChapter");
			final ComboBox icdBlockCombo = (ComboBox) combos.get("icdBlock");
			//final ComboBox ickCodeCombo = (ComboBox) combos.get("icdBlock");
			addICDChapterListener(icdChapterCombo, icdBlockCombo);
			if(pedValidationTableDTO.getIcdChapter() != null) {
				addICDBlock(pedValidationTableDTO.getIcdChapter().getId(), icdBlockCombo, pedValidationTableDTO.getIcdBlock() );
			}
			
//			addICDBlockListener(icdBlockCombo, ickCodeCombo);
//			if(pedValidationTableDTO.getIcdBlock() != null) {
//				addICDCode(pedValidationTableDTO.getIcdBlock().getId(), icdBlockCombo, pedValidationTableDTO.getIcdCode());
//			}
			

		}
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			DiagnosisDetailsTableDTO pedValidation = (DiagnosisDetailsTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			/*if (tableItem.get(pedValidation) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(pedValidation, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(pedValidation);
			}*/
			
			if(tableItem.get(pedValidation) == null)
			{
				tableItem.put(pedValidation,
						new HashMap<String, AbstractField<?>>());
			}
			tableRow = tableItem.get(pedValidation);
			
			if("diagnosisName".equals(propertyId)) {
//				ComboBox box = new ComboBox();
//			//	box.setEnabled(isEnabled);
//				 box.setFilteringMode(FilteringMode.STARTSWITH);
//				 box.setTextInputAllowed(true);
//				 box.setNullSelectionAllowed(true);
//				 box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//				 box.setItemCaptionPropertyId("value");
//				 box.setNewItemsAllowed(true);
//				 CustomLazyContainer customLazyContainer = new
//				 CustomLazyContainer(3, "value", masterService, "diagnosis");
//				 customLazyContainer.addContainerProperty("value",
//				 SelectValue.class, null);
//				 box.setContainerDataSource(customLazyContainer);
//				 box.setConverter(getConverter(customLazyContainer));
//				//addDiagnosisValues(box);
//			//	tableRow.put("icdChapter", box);
//				tableRow.put("diagnosisName", box);
//				//box.get
//				//SelectValue selValue = (SelectValue)box.getValue();
//				//dummyField.setValue(selValue.getValue());
//				return box;
				final DiagnosisComboBox box = new DiagnosisComboBox();
				final SuggestingContainer diagnosisContainer = new SuggestingContainer(masterService);
				box.setContainerDataSource(diagnosisContainer);	
//				box.setEnabled(isEnabled);
				box.setFilteringMode(FilteringMode.STARTSWITH);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				box.setItemCaptionPropertyId("value");
				box.setNewItemsAllowed(true);
				
				box.addValueChangeListener(new Property.ValueChangeListener() {
			            @Override
			            public void valueChange(ValueChangeEvent event) {
			                
			                // tell the custom container that a value has been selected. This is necessary to ensure that the
			                // selected value is displayed by the ComboBox
			            	SelectValue value = (SelectValue) event.getProperty().getValue();
							if (value != null)
			            	{
								diagnosisContainer.setSelectedBean(value);
			            		box.select(value);
			            	}
			            }
			        });
				
					box.setNewItemHandler(new NewItemHandler() {
						
						private static final long serialVersionUID = -4453822645147859276L;

						@Override
						public void addNewItem(String newItemCaption) {
							SelectValue newDiagonsisValue = masterService.addDiagnosis(newItemCaption);
							diagnosisContainer.addItem(newDiagonsisValue);
							diagnosisContainer.setNewItemAdded(true);
							box.addItem(newDiagonsisValue);
							box.select(newDiagonsisValue);
						}
					});
					
				tableRow.put("diagnosisName", box);
				return box;
				
			} else if ("icdChapter".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("150px");
				addICDChapterValues(box);
				tableRow.put("icdChapter", box);
				// To fill the exising values
				final ComboBox icdBlock = (ComboBox) tableRow.get("icdBlock");
				box.setData(pedValidation);
				addICDChapterListener(box, icdBlock);
				return box;
			} else if ("icdBlock".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("150px");
				box.setData(pedValidation);
				tableRow.put("icdBlock", box);
				ComboBox icdCodeCmb = (ComboBox) tableRow.get("icdCode");
				addICDBlockListener(box, icdCodeCmb);
				if(pedValidation.getIcdChapter() != null) {
					addICDBlock(pedValidation.getIcdChapter().getId(), box, pedValidation.getIcdBlock());
				}
				return box;
			} else if("icdCode".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("150px");
				tableRow.put("icdCode", box);
				if(pedValidation.getIcdBlock() != null) {
					addICDCode(pedValidation.getIcdBlock().getId(), box, pedValidation.getIcdCode());
				}
				return box;
			} else if("sublimitApplicable".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("100px");
				box.setData(pedValidation);
				tableRow.put("sublimitApplicable", box);
				addCommonValues(box, "sublimitApplicable");
				addSublimitApplicableListener(box);
				return box;
			} else if("sublimitName".equals(propertyId)) {
				ComboBox box = new ComboBox();
				tableRow.put("sublimitName", box);
				final TextField field = (TextField) tableRow.get("sublimitAmt");
				box.setData(pedValidation);
				addSublimtValues(box);
				addSublimitListener(box, field);
				return box;
			} else if("sublimitAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("sublimitAmt", field);
				ComboBox sublimitCombo = (ComboBox) tableRow.get("sublimitName");
				if(pedValidation.getSublimitApplicable() != null && pedValidation.getSublimitApplicable().getId() == ReferenceTable.COMMONMASTER_NO && sublimitCombo != null) {
					setValuesToNull(field, sublimitCombo);
					/*field.setValue("");
					sublimitCombo.setValue(null);*/
				}
				return field;
			}
			else if ("considerForPayment".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("100px");
				tableRow.put("considerForPayment", box);
				addCommonValues(box, "considerForPayment");
				return box;
			} else if("sumInsuredRestriction".equals(propertyId)) {
				ComboBox box = new ComboBox();
				box.setWidth("200px");
				box.setData((bean.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured() != 0) ? bean.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured() : bean.getNewIntimationDTO().getPolicy().getTotalSumInsured());
				addSIRestrictionListener(box);
				tableRow.put("sumInsuredRestriction", box);
				addSIValues(box);
				return box;
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
	
	/*private void addDiagnosisValues(ComboBox diagnosisCombo) {
		//@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) referenceData
				.get("diagnosisName");
		diagnosisCombo.setContainerDataSource(diagnosis);
		diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisCombo.setItemCaptionPropertyId("value");
		
	}*/
	
	private void addCommonValues(ComboBox diagnosisCombo, String tableColumnName) {
		@SuppressWarnings("unchecked")
		BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
				.get(tableColumnName);
		diagnosisCombo.setContainerDataSource(commonValues);
		diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisCombo.setItemCaptionPropertyId("value");
		
	}
	
	@SuppressWarnings("unchecked")
	public void addICDChapterValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> icdChapter = (BeanItemContainer<SelectValue>) referenceData
				.get("icdChapter");
		comboBox.setContainerDataSource(icdChapter);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

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
	
	@SuppressWarnings("unchecked")
	public void addSIValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> siValues = (BeanItemContainer<SelectValue>) referenceData
				.get("sumInsuredRestriction");
		comboBox.setContainerDataSource(siValues);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}
	
	@SuppressWarnings("unused")
	private void addICDChapterListener(final ComboBox icdChpterCombo,
			final ComboBox icdBlockCombo) {
		if (icdChpterCombo != null) {
			icdChpterCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(pedValidationTableDTO);
					ComboBox cmbBox = (ComboBox) hashMap.get("diagnosisName");
					ComboBox comboBox = (ComboBox) hashMap.get("icdBlock");
					if (pedValidationTableDTO != null) {
						if(pedValidationTableDTO.getIcdChapter() != null) {
							if(comboBox != null) {
								addICDBlock(pedValidationTableDTO.getIcdChapter().getId(), comboBox, pedValidationTableDTO.getIcdBlock());
							}
							if(null != cmbBox )
							{
								addDiagnosisValue(pedValidationTableDTO,cmbBox);
								
							}
						}
						
					}
				}
			});
		}

	}
	private void addDiagnosisValue (DiagnosisDetailsTableDTO pedValidationDTO,ComboBox cmdDiagnosis)
	{
		SelectValue value = pedValidationDTO.getDiagnosisName();
		/*BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) referenceData
							.get("diagnosisName");*/
		//SelectValue sel = null;
		cmdDiagnosis.setFilteringMode(FilteringMode.STARTSWITH);
		cmdDiagnosis.setTextInputAllowed(true);
		cmdDiagnosis.setNullSelectionAllowed(true);
		cmdDiagnosis.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmdDiagnosis.setItemCaptionPropertyId("value");
		Container containerDataSource = cmdDiagnosis.getContainerDataSource();
		containerDataSource.addItem(value.getId());
		containerDataSource.getContainerProperty(value.getId(), "value").setValue(value);
		cmdDiagnosis.setValue(value.getId());
		dummyField.setValue(value.getValue());
	}
	
	@SuppressWarnings("unused")
	private void addICDBlockListener(final ComboBox icdBlockCombo,
			final ComboBox icdCodeCombo) {
		if (icdBlockCombo != null) {
			icdBlockCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(pedValidationTableDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("icdCode");
					if (pedValidationTableDTO != null) {
						if(pedValidationTableDTO.getIcdBlock() != null) {
							
							if(comboBox != null) {
								addICDCode(pedValidationTableDTO.getIcdBlock().getId(), comboBox, pedValidationTableDTO.getIcdCode());
							}
						}
					}
				}
			});
		}

	}
	
	private void addSIRestrictionListener(ComboBox siRestriction) {
		siRestriction.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -5698056658049911740L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox siRestrictCombo = (ComboBox) event.getProperty();
				Object selectedSI = siRestrictCombo.getValue();
				Object originalSI = siRestrictCombo.getData();
				
				if(null != selectedSI && originalSI != null && SHAUtils.getDoubleValueFromString(originalSI.toString())  <= SHAUtils.getDoubleValueFromString(selectedSI.toString())) {
					VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> SI Restriction Should be below the Original SI. </b>", ContentMode.HTML));
					showErrorPopup(siRestrictCombo, layout);
				}
			}
		});
	}
	
	private void showErrorPopup(ComboBox field, VerticalLayout layout) {
		layout.setMargin(true);
		layout.setSpacing(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		field.setValue(null);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	@SuppressWarnings("unused")
	private void addSublimitListener(final ComboBox sublimitCombo,
			final TextField text) {
		if (sublimitCombo != null) {
			sublimitCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(pedValidationTableDTO);
					TextField field = (TextField) hashMap.get("sublimitAmt");
					if (pedValidationTableDTO != null) {
						SublimitFunObject sublimitName = pedValidationTableDTO.getSublimitName();
						if(sublimitName != null) {
							
							if(null != bean.getNewIntimationDTO().getIsJioPolicy() && bean.getNewIntimationDTO().getIsJioPolicy()){
								getJioSublimitAlert(sublimitName);
							}
							
							if(field != null) {
								field.setReadOnly(false);
								field.setValue(sublimitName.getAmount() != null ? sublimitName.getAmount().toString() : null);
								field.setReadOnly(true);
							}
						}
						
						//Adding for test
						else
						{
							if (field != null) {
								field.setReadOnly(false);
								field.setValue(null);
								field.setReadOnly(true);
							}
						}
					}
				}
			});
		}
	}
	
	public void addICDBlock(Long icdChpterComboKey, ComboBox icdBlockCombo, SelectValue value) {
		fireViewEvent(PreauthWizardPresenter.GET_ICD_BLOCK, icdChpterComboKey);
		icdBlockCombo.setContainerDataSource(icdBlock);
		icdBlockCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdBlockCombo.setItemCaptionPropertyId("value");
		
		if(value != null) {
			icdBlockCombo.setValue(value);
		}
	}
	
	public void addICDCode(Long icdBlockKey, ComboBox icdCodeCombo, SelectValue value) {
		fireViewEvent(PreauthWizardPresenter.GET_ICD_CODE, icdBlockKey);
		icdCodeCombo.setContainerDataSource(icdCode);
		icdCodeCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdCodeCombo.setItemCaptionPropertyId("value");
		
		if(value != null) {
			icdCodeCombo.setValue(value);
		}
	}
	
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockSelectValueContainer){
		this.icdBlock =	icdBlockSelectValueContainer;
	}
	
    public void setIcdCode(BeanItemContainer<SelectValue> icdCodeSelectValueContainer){
    	this.icdCode = icdCodeSelectValueContainer;
    }
    
    public List<DiagnosisDetailsTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<DiagnosisDetailsTableDTO> itemIds = (List<DiagnosisDetailsTableDTO>) this.table.getItemIds() ;
    	return itemIds;
    }
    
    public void addBeanToList(DiagnosisDetailsTableDTO pedValidationDTO) {
    	data.addBean(pedValidationDTO);
//    	data.addItem(pedValidationDTO);
    	manageListeners();
    }
    
    public boolean isValid()
   	{
   		boolean hasError = false;
   		errorMessages.removeAll(getErrors());
   		@SuppressWarnings("unchecked")
   		Collection<DiagnosisDetailsTableDTO> itemIds = (Collection<DiagnosisDetailsTableDTO>) table.getItemIds();
   		Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();
   		for (DiagnosisDetailsTableDTO bean : itemIds) {
   			
   			if(bean.getSublimitApplicable() != null && bean.getSublimitApplicable().getId() == ReferenceTable.COMMONMASTER_YES && bean.getSublimitName() == null) {
   				hasError = true;
   				errorMessages.add("Please Select Sublimit Name.");
   			}
   			
   			if(bean.getSublimitName() != null) {
				if(valuesMap.containsKey(bean.getSublimitName().getLimitId()) && (bean.getConsiderForPayment() == null || (null != bean.getConsiderForPayment() && bean.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)))) {
					validationMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				} else {
					valuesMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				}
			}
   			
   			Set<ConstraintViolation<DiagnosisDetailsTableDTO>> validate = validator.validate(bean);

   			if (validate.size() > 0) {
   				hasError = true;
   				for (ConstraintViolation<DiagnosisDetailsTableDTO> constraintViolation : validate) {
   					errorMessages.add(constraintViolation.getMessage());
   				}
   			}
   		}
   		if(!validationMap.isEmpty()) {
   			hasError = true;
			errorMessages.add("Diagnosis Table - Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .");
		}
   		return !hasError;
   	}
   	public List<String> getErrors()
   	{
   		return this.errorMessages;
   	}

   	@SuppressWarnings("unused")
	private void addSublimitApplicableListener(final ComboBox sublimitApplicableCombo) {
		if (sublimitApplicableCombo != null) {
			sublimitApplicableCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsTableDTO pedValidationDTO = (DiagnosisDetailsTableDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(pedValidationDTO);
					System.out.println("--the hashMap----"+hashMap);
					TextField sublimitAmt = (TextField) hashMap.get("sublimitAmt");
					ComboBox sublimtName = (ComboBox) hashMap.get("sublimitName");
					if(sublimitAmt !=null && sublimtName !=null){
						if(pedValidationDTO.getSublimitApplicable()!=null){
							if (pedValidationDTO.getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES)) {
								/*sublimitAmt.setEnabled(true);
								sublimtName.setEnabled(true);*/
								sublimitAmt.setEnabled(true);
								sublimitAmt.setReadOnly(false);
								sublimtName.setEnabled(true);
								sublimtName.setReadOnly(false);
							} else if (pedValidationDTO.getSublimitApplicable()
									.getId().equals(ReferenceTable.COMMONMASTER_NO)) {
								setValuesToNull(sublimitAmt, sublimtName);
							}
						}
						if(component.getValue() == null){
							sublimitAmt.setReadOnly(false);
							sublimitAmt.setValue(null);
							sublimtName.setValue(null);	
							sublimitAmt.setReadOnly(true);
							sublimtName.setEnabled(true);
						}
					}
					SelectValue value = (SelectValue) component.getValue();
					if(value != null && value.getId() == ReferenceTable.COMMONMASTER_NO  && sublimitAmt != null  && sublimtName != null) {
						setValuesToNull(sublimitAmt, sublimtName);
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
   	
   	
   	/*private Converter<Object, SelectValue> getConverter(final Object object) {
		return new Converter<Object, SelectValue>() {

			@Override
			public SelectValue convertToModel(Object itemId,
					Class<? extends SelectValue> targetType, Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (itemId != null) {
					IndexedContainer c = (IndexedContainer) object;
					Object propertyId = c.getContainerPropertyIds().iterator()
							.next();
					Object name = c.getItem(itemId).getItemProperty(propertyId)
							.getValue();
					return (SelectValue) name;
				}
				return null;
			}

			@Override
			public Object convertToPresentation(SelectValue value,
					Class<? extends Object> targetType, Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (value != null) {
					IndexedContainer c = (IndexedContainer) object;
					Object propertyId = c.getContainerPropertyIds().iterator()
							.next();
					for (Object itemId : c.getItemIds()) {
						Object name = c
								.getContainerProperty(itemId, propertyId)
								.getValue();
						if (value.equals(name)) {
							return itemId;
						}
					}
				}
				return null;
			}

			@Override
			public Class<SelectValue> getModelType() {
				// TODO Auto-generated method stub
				return SelectValue.class;
			}

			@Override
			public Class<Object> getPresentationType() {
				// TODO Auto-generated method stub
				return Object.class;
			}
		};
	}*/
   	
   	@SuppressWarnings("unchecked")
	public void changeSublimitValues() {

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(pedValidationTableDTO);
			
			final GComboBox sublimitCombo = (GComboBox) combos.get("sublimitName");
			GComboBox sublimitApplicableCombo = (GComboBox) combos.get("sublimitApplicable");
			addCommonValues(sublimitApplicableCombo, "sublimitApplicable");
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
   	
   	private void getJioSublimitAlert(SublimitFunObject sublimitName){
   		
   		if(SHAConstants.JIO_JOINT_AND_KNEE_REPLACEMENT.equalsIgnoreCase(sublimitName.getName())){
			
			StarCommonUtils.alertMessage(getUI(), "Waiting period of 12 months applicable for new member.");
		}
		else if(SHAConstants.JIO_MATERNITY_NORMAL.equalsIgnoreCase(sublimitName.getName()) ||
				SHAConstants.JIO_MATERNITY_CEASAREAN.equalsIgnoreCase(sublimitName.getName())){
			
			StarCommonUtils.alertMessage(getUI(), "Waiting of 9 months applicable for new members.");
		}
   	}
}

	
	/*private void setValuesToNull(TextField sublimitAmt,
			ComboBox sublimtName) {
		if(sublimitAmt != null && sublimtName != null) {
			sublimitAmt.setReadOnly(false);
			sublimitAmt.setValue("");
			sublimitAmt.setReadOnly(true);
			sublimtName.setValue(null);
		}
		
	}
}*/
