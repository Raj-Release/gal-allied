package com.shaic.claim.outpatient.registerclaim.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.DiagnosisComboBox;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.service.PreMedicalService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
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

public class DiganosisDetailsListenerForOP extends ViewComponent {

	private static final long serialVersionUID = 7802397137014194525L;

	@EJB
	private MasterService masterService;

	@EJB
	private PreMedicalService premedicalService;

	private Map<DiagnosisDetailsOPTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DiagnosisDetailsOPTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<DiagnosisDetailsOPTableDTO> data = new BeanItemContainer<DiagnosisDetailsOPTableDTO>(DiagnosisDetailsOPTableDTO.class);

	private Table table;

	private Button btnAdd;

	private Map<String, Object> referenceData;

	private BeanItemContainer<SelectValue> icdBlock;

	private BeanItemContainer<SelectValue> icdCode;

	private List<String> errorMessages;

	private Validator validator;

	private String presenterString;

	public TextField dummyField = new TextField();

	private PreauthDTO bean;

	public List<DiagnosisDetailsOPTableDTO> deletedDTO;

	public TextField listenerField = new TextField();
	
//	List<DiagnosisDetailsOPTableDTO> listOfDiagnosisDetails = new ArrayList<DiagnosisDetailsOPTableDTO>();
//
//	public List<DiagnosisDetailsOPTableDTO> getListOfDiagnosisDetails() {
//		return listOfDiagnosisDetails;
//	}
//
//	public void setListOfDiagnosisDetails(List<DiagnosisDetailsOPTableDTO> listOfDiagnosisDetails) {
//		this.listOfDiagnosisDetails = listOfDiagnosisDetails;
//	}

	public void init(String presenterString) {
//		PreauthDTO bean, 
		this.presenterString = presenterString;
//		this.bean = bean; 
		deletedDTO = new ArrayList<DiagnosisDetailsOPTableDTO>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);

		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(btnLayout);
		layout.setMargin(true);

		initTable(false);

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
				DiagnosisDetailsOPTableDTO pedValidationTableDTO = new DiagnosisDetailsOPTableDTO(presenterString);
				pedValidationTableDTO.setRemarks("");
				pedValidationTableDTO.setEnableOrDisable(true);
				BeanItem<DiagnosisDetailsOPTableDTO> addItem = data.addItem(pedValidationTableDTO);
				manageListeners();
			}
		});
	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	void initTable(Boolean siRestricationApplicable) {
		// Create a data source and bind it to a table
		table = new Table("Diagnosis Details", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		// Added for table height..
		table.setHeight("160px");

		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button deleteButton = new Button("Delete");
				DiagnosisDetailsOPTableDTO dto = (DiagnosisDetailsOPTableDTO) itemId;
				Boolean isEnabled = (null != dto && null != dto.getRecTypeFlag() && dto.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c") ) ? false: true;
				deleteButton.setEnabled(isEnabled);
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						final DiagnosisDetailsOPTableDTO currentItemId = (DiagnosisDetailsOPTableDTO) event.getButton().getData();
						if (table.getItemIds().size() > 1) {

							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

										public void onClose(ConfirmDialog dialog) {
											if (!dialog.isConfirmed()) {
												// Confirmed to continue
												DiagnosisDetailsOPTableDTO dto =  (DiagnosisDetailsOPTableDTO)currentItemId;
												if(dto.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
													deletedDTO.add((DiagnosisDetailsOPTableDTO)currentItemId);
												}
												table.removeItem(currentItemId);
											} else {
												// User did not confirm
											}
										}
									});
							dialog.setClosable(false);

						} else {
							HorizontalLayout layout = new HorizontalLayout(
									new Label("One Diagnosis is Mandatory."));
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
				return deleteButton;
			}
		});

		table.setVisibleColumns(new Object[] { "diagnosisName","icdChapter", "icdBlock", "icdCode", "remarks", "Delete" });
		
		/*if(SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(presenterString)) {
		}else{
			if(siRestricationApplicable){
				table.setVisibleColumns(new Object[] { "diagnosisName", "icdChapter",
						"icdBlock", "icdCode", "sublimitApplicable", "sublimitName",
						"sublimitAmt", "considerForPayment", "sumInsuredRestriction",
				"Delete" });
			}else{
				table.setVisibleColumns(new Object[] { "diagnosisName", "icdChapter",
						"icdBlock", "icdCode", "sublimitApplicable", "sublimitName",
						"sublimitAmt", "considerForPayment",
				"Delete" });
			}
		}*/
		table.setColumnHeader("diagnosisName", "Diagnosis");
		table.setColumnHeader("icdChapter", "ICD Chapter");
		table.setColumnHeader("icdBlock", "ICD Block");
		table.setColumnHeader("icdCode", "ICD Codes");
		table.setColumnHeader("remarks", "Remarks");

		/*table.setColumnHeader("sublimitApplicable", "Sub Limit Applicable");
		table.setColumnHeader("sublimitName", "Sub Limit Name");
		table.setColumnHeader("sublimitAmt", "Sub Limit Amount");
		table.setColumnHeader("considerForPayment", "Consider For Payment");
		table.setColumnHeader("sumInsuredRestriction", "SI Restriction");*/
		table.setEditable(true);

		// manageListeners();

		// Use a custom field factory to set the edit fields as immediate
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

		manageListeners();

	}


	protected void manageListeners() {
		for (DiagnosisDetailsOPTableDTO pedValidationTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(pedValidationTableDTO);

			final ComboBox icdChapterCombo = (ComboBox) combos.get("icdChapter");
			final ComboBox icdBlockCombo = (ComboBox) combos.get("icdBlock");
			//final ComboBox ickCodeCombo = (ComboBox) combos.get("icdBlock");
			addICDChapterListener(icdChapterCombo, icdBlockCombo);
			if (pedValidationTableDTO.getIcdChapter() != null) {
				addICDBlock(pedValidationTableDTO.getIcdChapter().getId(),
						icdBlockCombo, pedValidationTableDTO.getIcdBlock());
			}
		}
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			final DiagnosisDetailsOPTableDTO pedValidation = (DiagnosisDetailsOPTableDTO) itemId;
			/*Boolean isEnabled = (null != pedValidation && null != pedValidation.getRecTypeFlag() && pedValidation.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c") ) ? false: true;
			if(!isEnabled) {
				isEnabled = true;
			}*/
			Boolean isEnabled = true;
			Map<String, AbstractField<?>> tableRow = null;

			if(tableItem.get(pedValidation) == null) {
				tableItem.put(pedValidation, new HashMap<String, AbstractField<?>>());
			}
			tableRow = tableItem.get(pedValidation);

			if ("diagnosisName".equals(propertyId)) {
				DiagnosisComboBox box = new DiagnosisComboBox();
				SuggestingContainer diagnosisContainer = new SuggestingContainer(masterService);
				box.setContainerDataSource(diagnosisContainer);
				box.setEnabled(isEnabled);
				box.setFilteringMode(FilteringMode.STARTSWITH);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				box.setItemCaptionPropertyId("value");
				box.setNewItemsAllowed(true);
				box.setData(pedValidation);

				tableRow.put("diagnosisName", box);
				addDiagnosisNameListener(box);
				return box;
			}
			else if ("icdChapter".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setWidth("190px");
				if(presenterString.equalsIgnoreCase("diagnosisDetailsOP")){
					box.setRequired(true);
				}
				addICDChapterValues(box);
				tableRow.put("icdChapter", box);
				// To fill the exising values
				final ComboBox icdBlock = (ComboBox) tableRow.get("icdBlock");
				box.setData(pedValidation);
				addICDChapterListener(box, icdBlock);
				return box;
			} else if ("icdBlock".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("190px");
				box.setData(pedValidation);
				if(presenterString.equalsIgnoreCase("diagnosisDetailsOP")){
					box.setRequired(true);
				}
				tableRow.put("icdBlock", box);
				ComboBox icdCodeCmb = (ComboBox) tableRow.get("icdCode");
				addICDBlockListener(box, icdCodeCmb);
				if (pedValidation.getIcdChapter() != null) {
					addICDBlock(pedValidation.getIcdChapter().getId(), box, pedValidation.getIcdBlock());
				}
				box.setEnabled(isEnabled);
				return box;
			} else if ("icdCode".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setWidth("190px");
				if(presenterString.equalsIgnoreCase("diagnosisDetailsOP")){
					box.setRequired(true);
				}
				tableRow.put("icdCode", box);
				if (pedValidation.getIcdBlock() != null) {
					addICDCode(pedValidation.getIcdBlock().getId(), box, pedValidation.getIcdCode());
				}
				return box;
			} /*else if ("sublimitApplicable".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setWidth("100px");
				box.setData(pedValidation);			
				tableRow.put("sublimitApplicable", box);
				if(!(pedValidation.getSublimitApplicable() != null)){
					SelectValue value = new SelectValue();
					value.setId(ReferenceTable.COMMONMASTER_NO);
					value.setValue("No");
					pedValidation.setSublimitApplicable(value);
				}
				addSublimitApplicableValues(box, "sublimitApplicable");
				addSublimitApplicableListener(box);
				setEnableOrDisableSection(box,pedValidation);				
				return box;
			} else if ("sublimitName".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				tableRow.put("sublimitName", box);
				final TextField field = (TextField) tableRow.get("sublimitAmt");
				box.setData(pedValidation);


				addSublimtValues(box);
				addSublimitListener(box, field);
				return box;
			} else if ("sublimitAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setEnabled(isEnabled);
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("sublimitAmt", field);
				ComboBox sublimitCombo = (ComboBox) tableRow
						.get("sublimitName");
				if (pedValidation.getSublimitApplicable() != null
						&& pedValidation.getSublimitApplicable().getId() == ReferenceTable.COMMONMASTER_NO
						&& sublimitCombo != null) {
					setValuesToNull(field, sublimitCombo);
				}
				return field;
			} else if ("considerForPayment".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setWidth("100px");
				tableRow.put("considerForPayment", box);
				addCommonValues(box, "considerForPayment");
				return box;
			} else if ("sumInsuredRestriction".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setData((bean.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured() != null && bean.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured() != 0) ? bean.getNewIntimationDTO().getInsuredPatient().getInsuredSumInsured() : bean.getNewIntimationDTO().getPolicy().getTotalSumInsured());
				addSIRestrictionListener(box);
				box.setWidth("200px");
				tableRow.put("sumInsuredRestriction", box);
				addSIValues(box);
				return box;
			}*/else if ("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("230px");
				field.setData(pedValidation);
				field.setStyleName(ValoTheme.TEXTFIELD_SMALL);
				field.setValue("");
				tableRow.put("remarks", field);
				return field;
			}
			else {
				Field<?> field = super.createField(container, itemId, propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				field.setEnabled(isEnabled);
				return field;
			}
		}
	}






/*	@SuppressWarnings("unchecked")
	private void addCommonValues(ComboBox diagnosisCombo, String tableColumnName) {

		BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
				.get(tableColumnName);

		diagnosisCombo.setContainerDataSource(commonValues);
		diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisCombo.setItemCaptionPropertyId("value");

	}*/

/*	@SuppressWarnings("unchecked")
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

	}*/


	@SuppressWarnings("unchecked")
	public void addICDChapterValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> icdChapter = (BeanItemContainer<SelectValue>) referenceData.get("icdChapter");
		comboBox.setContainerDataSource(icdChapter);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}

/*	@SuppressWarnings("unchecked")
	public void addSublimtValues(ComboBox comboBox) {
		List<SublimitFunObject> list = (List<SublimitFunObject>) referenceData
				.get("sublimitDBDetails");
		BeanItemContainer<SublimitFunObject> sublimit = new BeanItemContainer<SublimitFunObject>(
				SublimitFunObject.class);
		sublimit.addAll(list);
		comboBox.setContainerDataSource(sublimit);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("name");
	}

	@SuppressWarnings("unchecked")
	public void addSIValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> siValues = (BeanItemContainer<SelectValue>) referenceData.get("sumInsuredRestriction");
		comboBox.setContainerDataSource(siValues);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		if(ReferenceTable.getHealthGainProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			comboBox.setItemCaptionPropertyId("commonValue");
		}else{
			comboBox.setItemCaptionPropertyId("value");
		}


	}*/

/*	public void setCustomDiagValueToContainer(SelectValue selValue,ComboBox cmbBox)
	{

		Container containerDataSource = cmbBox.getContainerDataSource();
		Long selValId = selValue.getId()+1;
		selValue.setId(selValId);
		containerDataSource.addItem(selValue.getId());
		containerDataSource.getContainerProperty(selValue.getId(), "value").setValue(selValue);
		cmbBox.setContainerDataSource(containerDataSource);
		containerDataSource.addItem(selValue);
		containerDataSource.getContainerProperty(selValue.getValue(), "value");

		cmbBox.setValue(selValue.getId());

	}*/

/*	private void addSIRestrictionListener(ComboBox siRestriction) {
		siRestriction.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -5698056658049911740L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox siRestrictCombo = (ComboBox) event.getProperty();
				Object selectedSI = siRestrictCombo.getValue();
				Object originalSI = siRestrictCombo.getData();

				if(! ReferenceTable.getHealthGainProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){

					if(null != selectedSI && originalSI != null && SHAUtils.getDoubleValueFromString(originalSI.toString())  <= SHAUtils.getDoubleValueFromString(selectedSI.toString())) {
						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> SI Restriction Should be below the Original SI. </b>", ContentMode.HTML));
						showErrorPopup(siRestrictCombo, layout);

					}
				}
			}
		});
	}*/

	private void addDiagnosisNameListener(ComboBox diagnosisName) {
		diagnosisName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2332276795125344767L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox diagnosis = (ComboBox) event.getProperty();
				if(diagnosis.getValue() != null) {
					DiagnosisDetailsOPTableDTO dto = (DiagnosisDetailsOPTableDTO) diagnosis.getData();

					SuggestingContainer containerDataSource = (SuggestingContainer)diagnosis.getContainerDataSource();

					if(diagnosis != null && diagnosis.getValue() != null){
						SelectValue selected = (SelectValue)diagnosis.getValue();
						containerDataSource.setComboBoxValue(selected.getValue());
					}else{
						containerDataSource.setComboBoxValue("");
					}

					List<DiagnosisDetailsOPTableDTO> values = getValues();
					int count = 0;
					for (DiagnosisDetailsOPTableDTO diagnosisDetailsTableDTO : values) {
						if(dto != null && dto.getDiagnosisName() != null && dto.getDiagnosisName().getId() != null && diagnosisDetailsTableDTO.getDiagnosisName() != null && diagnosisDetailsTableDTO.getDiagnosisName().getId() != null && dto.getDiagnosisName().getId().equals(diagnosisDetailsTableDTO.getDiagnosisName().getId())) {
							count += 1;
						}
					}
					if(count > 1) {
						HorizontalLayout layout = new HorizontalLayout(new Label("Duplicate Diagnosis is not allowed. Please choose different diagnosis"));
						layout.setMargin(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("Warning");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						diagnosis.setValue(null);
						dialog.show(getUI().getCurrent(), null, true);
					}
				}else{
					SuggestingContainer containerDataSource = (SuggestingContainer)diagnosis.getContainerDataSource();
					containerDataSource.setComboBoxValue("");
				}
			}
		});
	}

/*	private void showErrorPopup(ComboBox field, VerticalLayout layout) {
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
	}*/

	@SuppressWarnings("unused")
	private void addICDChapterListener(final ComboBox icdChpterCombo,
			final ComboBox icdBlockCombo) {
		if (icdChpterCombo != null) {
			icdChpterCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsOPTableDTO pedValidationTableDTO = (DiagnosisDetailsOPTableDTO) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidationTableDTO);
					System.out.println("---the hashmap---"+hashMap);
					ComboBox cmbBox = (ComboBox) hashMap.get("diagnosisName");
					ComboBox comboBox = (ComboBox) hashMap.get("icdBlock");
					if (pedValidationTableDTO != null) {
						if (pedValidationTableDTO.getIcdChapter() != null) {
							if (comboBox != null) {
								addICDBlock(pedValidationTableDTO
										.getIcdChapter().getId(), comboBox,
										pedValidationTableDTO.getIcdBlock());
							}							
						}
					}
				}
			});
		}

	}



	/*private void addDiagnosisValue(DiagnosisDetailsTableDTO pedValidationDTO , ComboBox cmdDiagnosis)
	{
		SelectValue value = pedValidationDTO.getDiagnosisName();
		Object object = referenceData.get("diagnosisName");
		BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) object;
		diagnosis.addBean(value);
		cmdDiagnosis.setContainerDataSource(diagnosis);
		cmdDiagnosis.setFilteringMode(FilteringMode.STARTSWITH);
		cmdDiagnosis.setTextInputAllowed(true);
		cmdDiagnosis.setNullSelectionAllowed(true);
		cmdDiagnosis.setNewItemsAllowed(true);
		cmdDiagnosis.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmdDiagnosis.setItemCaptionPropertyId("value");

		diagnosis.sort(new Object[] {"value"}, new boolean[] {true});

		if(value != null) {
			cmdDiagnosis.setValue(value);
		}


	}*/


	private void addICDBlockListener(final ComboBox icdBlockCombo,
			final ComboBox icdCodeCombo) {
		if (icdBlockCombo != null) {
			icdBlockCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsOPTableDTO pedValidationTableDTO = (DiagnosisDetailsOPTableDTO) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidationTableDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("icdCode");
					if (pedValidationTableDTO != null) {
						if (pedValidationTableDTO.getIcdBlock() != null) {
							if (comboBox != null) {
								addICDCode(pedValidationTableDTO.getIcdBlock()
										.getId(), comboBox,
										pedValidationTableDTO.getIcdCode());
							}
						}
					}
				}
			});
		}

	}

/*	private void addSublimitApplicableListener(	final ComboBox sublimitApplicableCombo) {
		if (sublimitApplicableCombo != null) {
			sublimitApplicableCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsTableDTO pedValidationDTO = (DiagnosisDetailsTableDTO) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidationDTO);
					System.out.println("--the hashMap----" + hashMap);
					TextField sublimitAmt = (TextField) hashMap
							.get("sublimitAmt");
					ComboBox sublimtName = (ComboBox) hashMap
							.get("sublimitName");

					if (sublimitAmt != null && sublimtName != null
							&& sublimitApplicableCombo != null) {
						if (pedValidationDTO.getSublimitApplicable() != null) {
							if (pedValidationDTO.getSublimitApplicable()
									.getId()
									.equals(ReferenceTable.COMMONMASTER_YES)) {
								sublimitAmt.setEnabled(true);
								sublimitAmt.setReadOnly(false);
								sublimtName.setEnabled(true);
								sublimtName.setReadOnly(false);
							} else if (pedValidationDTO.getSublimitApplicable()
									.getId()
									.equals(ReferenceTable.COMMONMASTER_NO)) {
								if (component.getValue() == null || ("").equals(component.getValue())) {
									setValuesToNull(sublimitAmt, sublimtName);
								}
								SelectValue value = (SelectValue) component
										.getValue();
								if (value != null
										&& value.getId().equals(ReferenceTable.COMMONMASTER_NO)
										&& sublimitAmt != null
										&& sublimtName != null) {
									setValuesToNull(sublimitAmt, sublimtName);
								}
							}
						}
						else{
							if(sublimitApplicableCombo != null && sublimitApplicableCombo.getValue() == null)
							{
								sublimtName.setValue(null);
							}
						}
					}else{
						if(sublimitApplicableCombo != null && sublimitApplicableCombo.getValue() == null)
						{
							sublimtName.setValue(null);
						}
					}
				}
			});
		}
	}*/


	private void setValuesToNull(TextField sublimitAmt, ComboBox sublimtName) {
		sublimtName.setReadOnly(false);
		sublimtName.setValue(null);
		sublimtName.setReadOnly(true);
		sublimtName.setEnabled(false);

		sublimitAmt.setReadOnly(false);
		sublimitAmt.setValue("");
		sublimitAmt.setReadOnly(true);
		sublimitAmt.setEnabled(false);
	}

	/*@SuppressWarnings("unused")
	private void addSublimitListener(final ComboBox sublimitCombo,
			final TextField text) {
		if (sublimitCombo != null) {
			sublimitCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;
				private TextField sittingsField;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidationTableDTO);
					TextField field = (TextField) hashMap.get("sublimitAmt");
					GComboBox sublimitApplicable = (GComboBox)hashMap.get("sublimitApplicable");

					if(sublimitApplicable != null && sublimitApplicable.getValue() == null){
						component.setValue(null);
					}

					if (pedValidationTableDTO != null) {
						SublimitFunObject sublimitName = pedValidationTableDTO
								.getSublimitName();
						if (sublimitName != null) {

							if(null != bean.getNewIntimationDTO().getIsJioPolicy() && bean.getNewIntimationDTO().getIsJioPolicy()){
								getJioSublimitAlert(sublimitName);
							}

							if (field != null) {
								field.setReadOnly(false);
								field.setValue(sublimitName.getAmount() != null ? sublimitName
										.getAmount().toString() : null);
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
	}*/

	public void addICDBlock(Long icdChpterComboKey, ComboBox icdBlockCombo, SelectValue value) {

		if ("diagnosisDetailsOP".equalsIgnoreCase(this.presenterString)) {
			BeanItemContainer<SelectValue> icdBlockContainer = masterService.searchIcdBlockByChapterKey(icdChpterComboKey);
			setIcdBlock(icdBlockContainer);
			//fireViewEvent(PreMedicalPreauthWizardPresenter.GET_ICD_BLOCK, icdChpterComboKey);
		} 
		icdBlockCombo.setContainerDataSource(icdBlock);
		icdBlockCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdBlockCombo.setItemCaptionPropertyId("value");

		if (value != null) {
			icdBlockCombo.setValue(value);
		}
	}

	public void addICDCode(Long icdBlockKey, ComboBox icdCodeCombo,
			SelectValue value) {

		if ("diagnosisDetailsOP".equalsIgnoreCase(this.presenterString)) {
			BeanItemContainer<SelectValue> icdCodeContainer = masterService.searchIcdCodeByBlockKey(icdBlockKey);
			setIcdCode(icdCodeContainer);
			//fireViewEvent(PreMedicalPreauthWizardPresenter.GET_ICD_CODE, icdBlockKey);
		} 
		icdCodeCombo.setContainerDataSource(icdCode);
		icdCodeCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdCodeCombo.setItemCaptionPropertyId("value");

		if (value != null) {
			icdCodeCombo.select(value);
			icdCodeCombo.setValue(value);
		}
	}

	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockSelectValueContainer) {
		icdBlock = icdBlockSelectValueContainer;
	}

	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeSelectValueContainer) {
		icdCode = icdCodeSelectValueContainer;
	}

	public List<DiagnosisDetailsOPTableDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<DiagnosisDetailsOPTableDTO> itemIds = (List<DiagnosisDetailsOPTableDTO>) this.table.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<DiagnosisDetailsOPTableDTO>();
		}
		return itemIds;
	}

	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(DiagnosisDetailsOPTableDTO pedValidationDTO) {
		data.addItem(pedValidationDTO);
		manageListeners();
	}

	@SuppressWarnings("unchecked")
	public boolean isValid() {
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		Collection<DiagnosisDetailsOPTableDTO> itemIds = (Collection<DiagnosisDetailsOPTableDTO>) table.getItemIds();
		Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();
		for (DiagnosisDetailsOPTableDTO bean : itemIds) {
			/*if (bean.getSublimitApplicable() != null
					&& bean.getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES)
					&& bean.getSublimitName() == null) {
				hasError = true;
				errorMessages.add("Please Select Sublimit Name in Diagnosis Details");
			}else if(bean.getSublimitApplicable() != null
					&& bean.getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES)){
				HashMap<String, AbstractField<?>> hashMap = tableItem
						.get(bean);
				ComboBox sublimtName = (ComboBox) hashMap
						.get("sublimitName");
				if(sublimtName != null && sublimtName.getValue() != null){
					SublimitFunObject sublimtNameValue = (SublimitFunObject) sublimtName.getValue();
					if(sublimtNameValue != null && (sublimtNameValue.getName() == null || (sublimtNameValue.getName() != null && sublimtNameValue.getName().isEmpty())))
					{
						hasError = true;
						errorMessages.add("Please Select Sublimit Name in Diagnosis Details");
					}
				}
			}*/

			/*if(bean.getSublimitName() != null) {
				if(valuesMap.containsKey(bean.getSublimitName().getLimitId()) && (bean.getConsiderForPayment() == null || (null != bean.getConsiderForPayment() && bean.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)))) {
					validationMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				} else {
					valuesMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				}
			}*/


			Set<ConstraintViolation<DiagnosisDetailsOPTableDTO>> validate = validator.validate(bean);

			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<DiagnosisDetailsOPTableDTO> constraintViolation : validate) {
					if(constraintViolation.getRootBean() != null && presenterString.equalsIgnoreCase("diagnosisDetailsOP")){
						DiagnosisDetailsOPTableDTO rootBean = constraintViolation.getRootBean();
						errorMessages.add(constraintViolation.getMessage());
					}else{
						errorMessages.add(constraintViolation.getMessage());
					}
				}
			}
		}

		/*if(!validationMap.isEmpty()) {
			hasError = true;
			errorMessages.add("Diagnosis Table - Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .");
		}*/

		return !hasError;
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}

/*	private Converter<Object, Object> getConverter(final Object object) {
		return new Converter<Object, Object>() {

			@Override
			public Object convertToModel(Object itemId,
					Class<? extends Object> targetType, Locale locale)
							throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (itemId != null) {
					IndexedContainer c = (IndexedContainer) object;
					Object propertyId = c.getContainerPropertyIds().iterator()
							.next();

					Object name = c.getItem(itemId).getItemProperty(propertyId)
							.getValue();
					return (Object) name;

				}

				return null;
			}

			@Override
			public Object convertToPresentation(Object value,
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
			public Class<Object> getModelType() {
				// TODO Auto-generated method stub
				return Object.class;

			}

			@Override
			public Class<Object> getPresentationType() {
				// TODO Auto-generated method stub
				return Object.class;
			}
		};
	}


	private Converter<Object, String> getCustomConverter(final Object object) {
		return new Converter<Object, String>() {

			@Override
			public String convertToModel(Object itemId,
					Class<? extends String> targetType, Locale locale)
							throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				if (itemId != null) {
					IndexedContainer c = (IndexedContainer) object;
					Object propertyId = c.getContainerPropertyIds().iterator()
							.next();

					Object name = c.getItem(itemId).getItemProperty(propertyId)
							.getValue();
					SelectValue selValue = (SelectValue)name;
					return (String) selValue.getValue();

				}
				return null;
			}

			@Override
			public Object convertToPresentation(String value,
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
			public Class<String> getModelType() {
				// TODO Auto-generated method stub
				return String.class;

			}

			@Override
			public Class<Object> getPresentationType() {
				// TODO Auto-generated method stub
				return Object.class;
			}

		};
	}*/

	public void enableOrDisableDeleteButton(final Boolean isEnable) {
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				DiagnosisDetailsOPTableDTO dto = (DiagnosisDetailsOPTableDTO) itemId;
				deleteButton.setEnabled(isEnable);
				if (!isEnable) {
					if(dto.getRecTypeFlag() != null && !dto.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c")) {
						deleteButton.setEnabled(true);
					}
				}
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						final Object currentItemId = event.getButton().getData();
						if (table.getItemIds().size() > 1) {
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

										public void onClose(ConfirmDialog dialog) {
											if (!dialog.isConfirmed()) {
												// Confirmed to continue
												DiagnosisDetailsOPTableDTO dto =  (DiagnosisDetailsOPTableDTO)currentItemId;
												if(dto.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
													deletedDTO.add((DiagnosisDetailsOPTableDTO)currentItemId);
												}
												table.removeItem(currentItemId);
												listenerField.setValue("true");
											} else {
												// User did not confirm
											}
										}
									});

						} else {
							HorizontalLayout layout = new HorizontalLayout(
									new Label("One Diagnosis is Mandatory."));
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


	/*public void changeSublimitValues() {
		for (DiagnosisDetailsTableDTO pedValidationTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(pedValidationTableDTO);

			GComboBox sublimitCombo = (GComboBox) combos.get("sublimitName");
			GComboBox sublimitApplicableCombo = (GComboBox) combos.get("sublimitApplicable");
			addSublimitApplicableValues(sublimitApplicableCombo, "sublimitApplicable");
			addSublimtValues(sublimitCombo);
		}
	}*/

	public void clearObject(){
		SHAUtils.setClearTableItemOP(tableItem);
		SHAUtils.setClearReferenceData(referenceData);
		SHAUtils.setClearPreauthDTO(bean);
		masterService = null;
		premedicalService = null;
		data = null;
		errorMessages = null;
		icdBlock = null;
		icdCode = null;
		validator = null;
		presenterString = null;
		dummyField = null;
		deletedDTO = null;
		listenerField = null;
		//		diagnosisPopup = null;
	}


	/*public void setEnableOrDisableSection(GComboBox box, DiagnosisDetailsTableDTO pedValidation)
	{
		if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				(ReferenceTable.STAR_CARDIAC_CARE.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))){

			if(null != bean.getPreauthDataExtractionDetails().getSection() && null != bean.getPreauthDataExtractionDetails().getSection().getId() &&
					(ReferenceTable.POL_SECTION_2.equals(bean.getPreauthDataExtractionDetails().getSection().getId()))){

				SelectValue value = new SelectValue();
				value.setId(ReferenceTable.COMMONMASTER_NO);
				value.setValue(SHAConstants.No);
				pedValidation.setSublimitApplicable(value);	
				box.setValue(value);				
				box.setEnabled(false);							

			}

		}
	}*/
	/*private void getJioSublimitAlert(SublimitFunObject sublimitName){

		if(SHAConstants.JIO_JOINT_AND_KNEE_REPLACEMENT.equalsIgnoreCase(sublimitName.getName())){

			StarCommonUtils.alertMessage(getUI(), "Waiting period of 12 months applicable for new member.");
		}
		else if(SHAConstants.JIO_MATERNITY_NORMAL.equalsIgnoreCase(sublimitName.getName()) ||
				SHAConstants.JIO_MATERNITY_CEASAREAN.equalsIgnoreCase(sublimitName.getName())){

			StarCommonUtils.alertMessage(getUI(), "Waiting of 9 months applicable for new members.");
		}
	}*/

}
