package com.shaic.paclaim.cashless.listenertables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;
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
import com.shaic.arch.test.InsuranceDiagnosisComboBox;
import com.shaic.arch.test.InsuranceDiagnosisContainer;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.listenerTables.AddDiagnosisPopup;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPagePresenter;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancemetWizardPresenter;
import com.shaic.paclaim.cashless.fle.wizard.wizardfiles.PAPremedicalEnhancementWizardPresenter;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizardPresenter;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardPresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.dataextraction.PAHealthClaimRequestDataExtractionPagePresenter;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAClaimRequestDataExtractionPagePresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.label.ContentMode;
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
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PADiganosisDetailsListenerForPremedical extends ViewComponent {
	private static final long serialVersionUID = 7802397137014194525L;

	@EJB
	private MasterService masterService;
	
	@EJB
	private PreMedicalService premedicalService;

	private Map<DiagnosisDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DiagnosisDetailsTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<DiagnosisDetailsTableDTO> data = new BeanItemContainer<DiagnosisDetailsTableDTO>(
			DiagnosisDetailsTableDTO.class);
	


	private Table table;
	
	private Button btnAdd;

	private Map<String, Object> referenceData;

	private BeanItemContainer<SelectValue> icdBlock;

	private BeanItemContainer<SelectValue> icdCode;

	private List<String> errorMessages;

	private static Validator validator;

	private String presenterString;
	
	public TextField dummyField = new TextField();
	
	private PreauthDTO bean;
	
	public List<DiagnosisDetailsTableDTO> deletedDTO;
	
	public TextField listenerField = new TextField();
	
	@Inject
	private AddDiagnosisPopup diagnosisPopup;


	//public void init(String presenterString, List<ProcedureDTO> procedureList) {
	public void init(PreauthDTO bean, String presenterString) {
		this.presenterString = presenterString;
		this.bean = bean;
		deletedDTO = new ArrayList<DiagnosisDetailsTableDTO>();
		//this.procedureList = procedureList;
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
				DiagnosisDetailsTableDTO pedValidationTableDTO = new DiagnosisDetailsTableDTO(presenterString);
				pedValidationTableDTO.setEnableOrDisable(true);
				BeanItem<DiagnosisDetailsTableDTO> addItem = data
						.addItem(pedValidationTableDTO);
				manageListeners();
			}
		});
	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	@SuppressWarnings("deprecation")
	void initTable() {
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
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) itemId;
				Boolean isEnabled = (null != dto && null != dto.getRecTypeFlag() && dto.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c") ) ? false: true;
				/*Delete button always enable bcoz change in diagnosis removed as per satish sir instruction*/
				deleteButton.setEnabled(true);
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						final DiagnosisDetailsTableDTO currentItemId = (DiagnosisDetailsTableDTO) event.getButton().getData();
						if (table.getItemIds().size() > 1) {
							
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														DiagnosisDetailsTableDTO dto =  (DiagnosisDetailsTableDTO)currentItemId;
														if(dto.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
															deletedDTO.add((DiagnosisDetailsTableDTO)currentItemId);
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
						
//						if (table.getItemIds().size() > 1) {
//							
//						} else {
//							HorizontalLayout layout = new HorizontalLayout(
//									new Label("One Diagnosis is Mandatory."));
//							layout.setMargin(false);
//							layout.setWidth("100%");
//
//							final ConfirmDialog dialog = new ConfirmDialog();
//							dialog.setCaption("");
//							// dialog.setClosable(false);
//							dialog.setClosable(true);
//							dialog.setContent(layout);
//							dialog.setWidth("250px");
//							// dialog.setResizable(false);
//							dialog.setResizable(true);
//							dialog.setModal(true);
//							dialog.show(getUI().getCurrent(), null, true);
//						}
					}
				});
				// deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});
		
		table.removeGeneratedColumn("addDiagnosis");
		table.addGeneratedColumn("addDiagnosis", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button addDiagnasis = new Button("");
				final DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) itemId;
				if (dto.getEnableOrDisable() != null) {
					addDiagnasis.setEnabled(true);
				}
				addDiagnasis.setEnabled(true);
				addDiagnasis.setIcon(FontAwesome.FILE);
				addDiagnasis.setData(itemId);
				addDiagnasis.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						
//						HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
//						final ComboBox cmbBox = (ComboBox) hashMap.get("diagnosisName");
						
						
						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("Add New Diagnosis");
						popup.setWidth("30%");
						popup.setHeight("30%");
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
						
						final ComboBox cmbBox = (ComboBox) hashMap.get("diagnosisName");
						
						diagnosisPopup.init(dto,cmbBox,popup);
						popup.setContent(diagnosisPopup);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;
							@Override
							public void windowClose(CloseEvent e) {
								// TODO Auto-generated method stub
								
							}
						});

						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
						
						
					}
				});
				// deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return addDiagnasis;
			}
		});

		if(SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(presenterString))
		{
			table.setVisibleColumns(new Object[] { "diagnosisName","addDiagnosis", /*"icdChapter",
					"icdBlock",*/ "icdCode", "Delete" });
		}
		else{
			table.setVisibleColumns(new Object[] { "diagnosisName","addDiagnosis", /*"icdChapter",
					"icdBlock",*/ "icdCode", "sublimitApplicable", "sublimitName",
					"sublimitAmt", "considerForPayment", "sumInsuredRestriction",
					"Delete" });
		}
		/*CR20181279 - Diagnosis Renamed to Hospital Diagnosis*/
		table.setColumnHeader("diagnosisName", "Hospital Diagnosis");
		table.setColumnHeader("addDiagnosis", "");
		table.setColumnHeader("icdChapter", "ICD Chapter");
		table.setColumnHeader("icdBlock", "ICD Block");
		/*CR20181279 - Diagnosis Renamed to Insurance Diagnosis*/
		table.setColumnHeader("icdCode", "Insurance Diagnosis");
		
		table.setColumnHeader("sublimitApplicable", "Sub Limit Applicable");
		table.setColumnHeader("sublimitName", "Sub Limit Name");
		table.setColumnHeader("sublimitAmt", "Sub Limit Amount");
		table.setColumnHeader("considerForPayment", "Consider For Payment");
		table.setColumnHeader("sumInsuredRestriction", "SI Restriction");
		table.setEditable(true);

		// manageListeners();

		// Use a custom field factory to set the edit fields as immediate
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

		manageListeners();

	}
	
	
	protected void manageListeners() {

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(pedValidationTableDTO);
			
			final ComboBox icdChapterCombo = (ComboBox) combos.get("icdChapter");
			final ComboBox icdBlockCombo = (ComboBox) combos.get("icdBlock");
			final ComboBox ickCodeCombo = (ComboBox) combos.get("icdBlock");
			/*R20181279 - Commented Below Code
			addICDChapterListener(icdChapterCombo, icdBlockCombo);
			if (pedValidationTableDTO.getIcdChapter() != null) {
				addICDBlock(pedValidationTableDTO.getIcdChapter().getId(),
						icdBlockCombo, pedValidationTableDTO.getIcdBlock());
			}*/

		}
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			final DiagnosisDetailsTableDTO pedValidation = (DiagnosisDetailsTableDTO) itemId;
			Boolean isEnabled = (null != pedValidation && null != pedValidation.getRecTypeFlag() && pedValidation.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c") ) ? false: true;
			if(/*!"premedicalEnhancement".equalsIgnoreCase(presenterString) &&*/ !isEnabled) {
				isEnabled = true;
			}
			Map<String, AbstractField<?>> tableRow = null;

			if(tableItem.get(pedValidation) == null)
			{
				tableItem.put(pedValidation,
						new HashMap<String, AbstractField<?>>());
				
			}
			tableRow = tableItem.get(pedValidation);
			/*if (tableItem.get(pedValidation) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(pedValidation,
						new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(pedValidation);
			}*/

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
//				}
				 
				
			}
//			if ("diagnosisName".equals(propertyId)) {
//				ComboBox box = new ComboBox();
//				box.setEnabled(isEnabled);
//				 box.setFilteringMode(FilteringMode.STARTSWITH);
//				 box.setTextInputAllowed(true);
//				 box.setNullSelectionAllowed(true);
//				 box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//				 box.setItemCaptionPropertyId("value");
//				 box.setNewItemsAllowed(true);
//				 CustomLazyContainer customLazyContainer = new
//				 CustomLazyContainer(3, "value", masterService, premedicalService ,"diagnosis");
//				/* customLazyContainer.addContainerProperty("value",
//				 SelectValue.class, null);*/
//				 customLazyContainer.addContainerProperty("value",
//						 Object.class, null);
//				 box.setContainerDataSource(customLazyContainer);
//				 box.setConverter(getConverter(customLazyContainer));
//				//addDiagnosisValues(box);
//				tableRow.put("diagnosisName", box);
//				// addDiagnosisListener(box);
//
//				//addICDChapterListener(box , null);
//				//addDiagnosisListener(box);
//				return box;
//			}
			
			/*R20181279 - Removed ICD Code & Block*/
			/*else if ("icdChapter".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setWidth("150px");
				if(presenterString.equalsIgnoreCase("premedicalPreauth")){
					box.setRequired(false);
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
				box.setWidth("150px");
				box.setData(pedValidation);
				if(presenterString.equalsIgnoreCase("premedicalPreauth")){
					box.setRequired(false);
				}
				tableRow.put("icdBlock", box);
				ComboBox icdCodeCmb = (ComboBox) tableRow.get("icdCode");
				addICDBlockListener(box, icdCodeCmb);
				if (pedValidation.getIcdChapter() != null) {
					addICDBlock(pedValidation.getIcdChapter().getId(), box,
							pedValidation.getIcdBlock());
				}
				box.setEnabled(isEnabled);
				return box;
			}*/ else if ("icdCode".equals(propertyId)) {
				InsuranceDiagnosisComboBox box = new InsuranceDiagnosisComboBox();
				final InsuranceDiagnosisContainer diagnosisContainer = new InsuranceDiagnosisContainer(masterService);
				box.setContainerDataSource(diagnosisContainer);
				box.setEnabled(isEnabled);
				box.setFilteringMode(FilteringMode.STARTSWITH);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				box.setItemCaptionPropertyId("value");
				box.setNewItemsAllowed(true);
				box.setData(pedValidation);
				
				box.setWidth("150px");
				if(presenterString.equalsIgnoreCase("premedicalPreauth") ||
						presenterString.equalsIgnoreCase("premedicalEnhancement") || presenterString.equalsIgnoreCase("meical_approval_data_extraciton")){
					box.setRequired(false);
					box.setEnabled(false);
				}
			
				box.setData(pedValidation);
				//box.setFilteringMode(FilteringMode.CONTAINS);
				if (pedValidation.getIcdCode() !=null && (presenterString.equalsIgnoreCase("premedicalEnhancement") || bean.getIsBack()
						|| (presenterString.equalsIgnoreCase("meical_approval_claim_request_data_extraciton") && !(bean.getHospitalizaionFlag()))
						|| (presenterString.equalsIgnoreCase("meical_approval_data_extraciton") && !(bean.getHospitalizaionFlag())))) {
					addICDCode(pedValidation.getIcdCode().getId(), box,
							pedValidation.getIcdCode());
				} else {
					addICDCode(0l, box,
							pedValidation.getIcdCode());
					if(!(presenterString.equalsIgnoreCase("premedicalPreauth")
							|| presenterString.equalsIgnoreCase("meical_approval_data_extraciton"))){
						pedValidation.setIcdCode(null);
					} else {
						SelectValue icdCode = new SelectValue();
						pedValidation.setIcdCode(icdCode);
					}
				}
				tableRow.put("icdCode", box);
				insuranceDiagnosisListener(box);
				return box;
			} else if ("sublimitApplicable".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				box.setWidth("100px");
				box.setData(pedValidation);
				
				tableRow.put("sublimitApplicable", box);
				if(true){
					SelectValue value = new SelectValue();
					value.setId(ReferenceTable.COMMONMASTER_NO);
					value.setValue("No");
					pedValidation.setSublimitApplicable(value);
				}
				addSublimitApplicableValues(box, "sublimitApplicable");
				
				addSublimitApplicableListener(box);
				box.setEnabled(false);
				return box;
			} else if ("sublimitName".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setEnabled(isEnabled);
				tableRow.put("sublimitName", box);
				final TextField field = (TextField) tableRow.get("sublimitAmt");
				box.setData(pedValidation);
				box.setEnabled(false);
				
//				addSublimtValues(box);
//				addSublimitListener(box, field);
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
//				if (pedValidation.getSublimitApplicable() != null
//						&& pedValidation.getSublimitApplicable().getId() == ReferenceTable.COMMONMASTER_NO
//						&& sublimitCombo != null) {
//					setValuesToNull(field, sublimitCombo);
//					/*field.setValue("");
//					sublimitCombo.setValue(null);*/
//				}
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
//				addSIRestrictionListener(box);
				box.setWidth("200px");
				tableRow.put("sumInsuredRestriction", box);
				box.setEnabled(false);
//				addSIValues(box);
				return box;
			}
			// else if("deleteButton".equals(propertyId)) {
			// Button deleteButton = new Button("Delete");
			// deleteButton.setData(itemId);
			// deleteButton.addClickListener(new ClickListener() {
			// private static final long serialVersionUID = 1L;
			//
			// @Override
			// public void buttonClick(ClickEvent event) {
			// Object currentItemId = event.getButton().getData();
			// table.removeItem(currentItemId);
			// }
			// });
			// return deleteButton;
			// }
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				field.setEnabled(isEnabled);
				return field;
			}
		}
	}
	



//	private void addDiagnosisValues(ComboBox diagnosisCombo) {
//		@SuppressWarnings("unchecked")
//		BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) referenceData
//				.get("diagnosisName");
//		diagnosisCombo.setContainerDataSource(diagnosis);
//		diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		diagnosisCombo.setItemCaptionPropertyId("value");
//
//	}

	@SuppressWarnings("unchecked")
	private void addCommonValues(ComboBox diagnosisCombo, String tableColumnName) {
		
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
	public void addICDChapterValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> icdChapter = (BeanItemContainer<SelectValue>) referenceData
				.get("icdChapter");
		comboBox.setContainerDataSource(icdChapter);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}

	@SuppressWarnings("unchecked")
	public void addSublimtValues(ComboBox comboBox) {
		List<SublimitFunObject> list = (List<SublimitFunObject>) referenceData
				.get("sublimitDBDetails");
		BeanItemContainer<SublimitFunObject> sublimit = new BeanItemContainer<SublimitFunObject>(
				SublimitFunObject.class);
		sublimit.addAll(list);
		comboBox.setContainerDataSource(sublimit);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("name");
//		if(list.isEmpty()) {
//			List<SelectValue> newListValue = new ArrayList<SelectValue>();
//			List<SelectValue> itemIds = commonValues.getItemIds();
//			for (SelectValue selectValue : itemIds) {
//				if(selectValue != null && selectValue.getId() != null && selectValue.getId().equals(ReferenceTable.COMMONMASTER_NO)) {
//					newListValue.add(selectValue);
//				}
//			}
//			DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) comboBox
//					.getData();
//			SelectValue sublimitApplicable2 = pedValidationTableDTO.getSublimitApplicable();
//			HashMap<String, AbstractField<?>> hashMap = tableItem
//					.get(pedValidationTableDTO);
//			GComboBox sublimitApplicable = (GComboBox)hashMap.get("sublimitApplicable");
//			BeanItemContainer<SelectValue> beanItemContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
//			beanItemContainer.addAll(newListValue);
////			referenceData.put("sublimitApplicable", beanItemContainer);
//			sublimitApplicable.setContainerDataSource(beanItemContainer);
//			sublimitApplicable.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//			sublimitApplicable.setItemCaptionPropertyId("value");
//			pedValidationTableDTO.setSublimitApplicable(sublimitApplicable2);
//			sublimitApplicable.setValue(pedValidationTableDTO.getSublimitApplicable());
//		}
	}

	@SuppressWarnings("unchecked")
	public void addSIValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> siValues = (BeanItemContainer<SelectValue>) referenceData
				.get("sumInsuredRestriction");
		comboBox.setContainerDataSource(siValues);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}
	
	public void setCustomDiagValueToContainer(SelectValue selValue,ComboBox cmbBox)
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
	
	private void addDiagnosisNameListener(ComboBox diagnosisName) {
		diagnosisName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2332276795125344767L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox diagnosis = (ComboBox) event.getProperty();
				if(diagnosis.getValue() != null) {
					DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) diagnosis.getData();
					
					SuggestingContainer containerDataSource = (SuggestingContainer)diagnosis.getContainerDataSource();
					
					if(diagnosis != null && diagnosis.getValue() != null){
						SelectValue selected = (SelectValue)diagnosis.getValue();
						containerDataSource.setComboBoxValue(selected.getValue());
					}else{
						containerDataSource.setComboBoxValue("");
					}
					
					List<DiagnosisDetailsTableDTO> values = getValues();
					int count = 0;
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : values) {
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
	private void addICDChapterListener(final ComboBox icdChpterCombo,
			final ComboBox icdBlockCombo) {
		if (icdChpterCombo != null) {
			icdChpterCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) component
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
							if(null != cmbBox )
							{
//								addDiagnosisValue(pedValidationTableDTO,cmbBox);
								
							}
						}
					}
				}
			});
		}

	}
	

	
	private void addDiagnosisValue(DiagnosisDetailsTableDTO pedValidationDTO , ComboBox cmdDiagnosis)
	{
		SelectValue value = pedValidationDTO.getDiagnosisName();
		Object object = referenceData.get("diagnosisName");
		BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) object;
		diagnosis.addBean(value);
		//SelectValue sel = null;
		cmdDiagnosis.setContainerDataSource(diagnosis);
		cmdDiagnosis.setFilteringMode(FilteringMode.STARTSWITH);
		cmdDiagnosis.setTextInputAllowed(true);
		cmdDiagnosis.setNullSelectionAllowed(true);
		cmdDiagnosis.setNewItemsAllowed(true);
		cmdDiagnosis.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmdDiagnosis.setItemCaptionPropertyId("value");
		
		diagnosis.sort(new Object[] {"value"}, new boolean[] {true});
		
		if(value != null) {
//			containerDataSource.addItem(value.getId());
//			containerDataSource.getContainerProperty(value.getId(), "value").setValue(value);
			cmdDiagnosis.setValue(value);
//			dummyField.setValue(value.getValue());
		}
		

	}

	//@SuppressWarnings("unused")
	/*private void addDiagnosisListener(final ComboBox diagnosisCombo) {
		
		if (diagnosisCombo != null) {
			//String strProValue = (null != procedureName.getValue() ? procedureName.getValue().toString() : "");
			
			diagnosisCombo.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					if(null != value && null != value.getValue())
					{
						String strDiagnosisValue = value.getValue();
						if(null != procedureList && !procedureList.isEmpty())
						{
							for(ProcedureDTO procedureObj : procedureList)
							{	
								if(strDiagnosisValue.equalsIgnoreCase(procedureObj.getProcedureNameValue()))
								{
									HorizontalLayout layout = new HorizontalLayout(new Label("Diagnosis and Procedure are same."));
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
						}
						
					}
					
				}
			});
		}
		

	}*/

	@SuppressWarnings("unused")
	private void addICDBlockListener(final ComboBox icdBlockCombo,
			final ComboBox icdCodeCombo) {
		if (icdBlockCombo != null) {
			icdBlockCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void componentEvent(Event event) {
					ComboBox component = (ComboBox) event.getComponent();
					DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) component
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

	@SuppressWarnings("unused")
	private void addSublimitApplicableListener(
			final ComboBox sublimitApplicableCombo) {
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
					DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidationTableDTO);
					TextField field = (TextField) hashMap.get("sublimitAmt");
					GComboBox sublimitApplicable = (GComboBox)hashMap.get("sublimitApplicable");
					
					//Adding for test
				/*	ComboBox sublimtName = (ComboBox) hashMap.get("sublimitName");
					if(null == sublimtName)
					{
						
					}*/
					if(sublimitApplicable != null && sublimitApplicable.getValue() == null){
						component.setValue(null);
					}
					
					if (pedValidationTableDTO != null) {
						SublimitFunObject sublimitName = pedValidationTableDTO
								.getSublimitName();
						if (sublimitName != null) {
							
							getJioSublimitAlert(sublimitName);
							
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
	}

	public void addICDBlock(Long icdChpterComboKey, ComboBox icdBlockCombo,
			SelectValue value) {
		if ("premedicalPreauth".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPreMedicalPreauthWizardPresenter.GET_ICD_BLOCK,
					icdChpterComboKey);
		} else if ("premedicalEnhancement"
				.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPremedicalEnhancementWizardPresenter.GET_ICD_BLOCK,
					icdChpterComboKey);
		} else if ("preauthEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPreauthEnhancemetWizardPresenter.GET_ICD_BLOCK,
					icdChpterComboKey);
		}  else if (SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(MedicalApprovalDataExtractionPagePresenter.GET_ICD_BLOCK,
					icdChpterComboKey);
		} else if (SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.GET_ICD_BLOCK,
			icdChpterComboKey);
			
        }else if(SHAConstants.PRE_AUTH.equalsIgnoreCase(this.presenterString)){
        	fireViewEvent(PAPreauthWizardPresenter.GET_ICD_BLOCK, icdChpterComboKey);
        }else if (SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAClaimRequestDataExtractionPagePresenter.GET_ICD_BLOCK,
			icdChpterComboKey);
			
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

		/*if ("premedicalPreauth".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPreMedicalPreauthWizardPresenter.GET_ICD_CODE,
					icdBlockKey);
		} else if ("premedicalEnhancement"
				.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPremedicalEnhancementWizardPresenter.GET_ICD_CODE,
					icdBlockKey);
		} else if ("preauthEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAPreauthEnhancemetWizardPresenter.GET_ICD_CODE,
					icdBlockKey);
		}else if (SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(MedicalApprovalDataExtractionPagePresenter.GET_ICD_CODE,
					icdBlockKey);
		} else if (SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAHealthClaimRequestDataExtractionPagePresenter.GET_ICD_CODE,
					icdBlockKey);
		} else if(SHAConstants.PRE_AUTH.equalsIgnoreCase(this.presenterString)){
			fireViewEvent(PAPreauthWizardPresenter.GET_ICD_CODE, icdBlockKey);
		}
		else if (SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PAClaimRequestDataExtractionPagePresenter.GET_ICD_CODE,
					icdBlockKey);
			
        }
		icdCodeCombo.setContainerDataSource(icdCode);
		icdCodeCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdCodeCombo.setItemCaptionPropertyId("value");*/

		if (value != null) {
			icdCodeCombo.select(value);
			icdCodeCombo.setValue(value);
		}
	}

	public void setIcdBlock(
			BeanItemContainer<SelectValue> icdBlockSelectValueContainer) {
		icdBlock = icdBlockSelectValueContainer;
	}

	public void setIcdCode(
			BeanItemContainer<SelectValue> icdCodeSelectValueContainer) {
		icdCode = icdCodeSelectValueContainer;
	}

	public List<DiagnosisDetailsTableDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<DiagnosisDetailsTableDTO> itemIds = (List<DiagnosisDetailsTableDTO>) this.table
				.getItemIds();
		if(itemIds.isEmpty()) {
			itemIds = new ArrayList<DiagnosisDetailsTableDTO>();
		}
		return itemIds;
	}
	
	public void removeAllItems(){
		table.removeAllItems();
	}

	public void addBeanToList(DiagnosisDetailsTableDTO pedValidationDTO) {
		data.addItem(pedValidationDTO);
		manageListeners();
	}

	public boolean isValid() {
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<DiagnosisDetailsTableDTO> itemIds = (Collection<DiagnosisDetailsTableDTO>) table
				.getItemIds();
		Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();
		for (DiagnosisDetailsTableDTO bean : itemIds) {

			if (bean.getSublimitApplicable() != null
					&& bean.getSublimitApplicable().getId().equals(ReferenceTable.COMMONMASTER_YES)
					&& bean.getSublimitName() == null) {
				hasError = true;
				errorMessages.add("Please Select Sublimit Name in Diagnosis Details");
			}
			
				if(bean.getSublimitName() != null) {
					if(valuesMap.containsKey(bean.getSublimitName().getLimitId()) && (bean.getConsiderForPayment() == null || (null != bean.getConsiderForPayment() && bean.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)))) {
						validationMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
					} else {
						valuesMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
					}
				}
			
			
			Set<ConstraintViolation<DiagnosisDetailsTableDTO>> validate = validator
					.validate(bean);

			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<DiagnosisDetailsTableDTO> constraintViolation : validate) {
					if(constraintViolation.getRootBean() != null && presenterString.equalsIgnoreCase("premedicalPreauth")){
					    DiagnosisDetailsTableDTO rootBean = constraintViolation.getRootBean();
					    errorMessages.add(constraintViolation.getMessage());
					}else{
						errorMessages.add(constraintViolation.getMessage());
					}
				}
			}
		}
		
			if(!validationMap.isEmpty()) {
				hasError = true;
				errorMessages.add("Diagnosis Table - Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .");
			}
		
		return !hasError;
	}

	public List<String> getErrors() {
		return this.errorMessages;
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
	
	private Converter<Object, Object> getConverter(final Object object) {
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

			
			/*public String convertToModel(Object value,
					Class<? extends String> targetType, Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				// TODO Auto-generated method stub
				return null;
			}*/

		/*	@Override
			public Object convertToPresentation(String value,
					Class<? extends Object> targetType, Locale locale)
					throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
				// TODO Auto-generated method stub
				return null;
			}*/
		};
	}

	public void enableOrDisableDeleteButton(final Boolean isEnable) {
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) itemId;
				deleteButton.setEnabled(isEnable);
				if (!isEnable) {
					if(dto.getRecTypeFlag() != null && !dto.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c")) {
						deleteButton.setEnabled(true);
					}
//					if (dto.getEnableOrDisable() != null) {
//						deleteButton.setEnabled(dto.getEnableOrDisable());
//					}
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
														DiagnosisDetailsTableDTO dto =  (DiagnosisDetailsTableDTO)currentItemId;
														if(dto.getKey() != null && dto.getDiagnosis() != null && dto.getDiagnosis().length() > 0) {
															deletedDTO.add((DiagnosisDetailsTableDTO)currentItemId);
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
	
	
	@SuppressWarnings("unchecked")
	public void changeSublimitValues() {

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(pedValidationTableDTO);
			
			GComboBox sublimitCombo = (GComboBox) combos.get("sublimitName");
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
	
	private void getJioSublimitAlert(SublimitFunObject sublimitName){
   		
   		if(SHAConstants.JIO_JOINT_AND_KNEE_REPLACEMENT.equalsIgnoreCase(sublimitName.getName())){
			
			StarCommonUtils.alertMessage(getUI(), "Waiting period of 12 months applicable for new member.");
		}
		else if(SHAConstants.JIO_MATERNITY_NORMAL.equalsIgnoreCase(sublimitName.getName()) ||
				SHAConstants.JIO_MATERNITY_CEASAREAN.equalsIgnoreCase(sublimitName.getName())){
			
			StarCommonUtils.alertMessage(getUI(), "Waiting of 9 months applicable for new members.");
		}
   	}
	
	private void insuranceDiagnosisListener(ComboBox diagnosisName) {
		diagnosisName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2332276795125344767L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox diagnosis = (ComboBox) event.getProperty();
				if(diagnosis.getValue() != null) {
					DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) diagnosis.getData();
					
					InsuranceDiagnosisContainer containerDataSource = (InsuranceDiagnosisContainer)diagnosis.getContainerDataSource();
					
					if(diagnosis != null && diagnosis.getValue() != null){
						SelectValue selected = (SelectValue)diagnosis.getValue();
						containerDataSource.setComboBoxValue(selected.getCommonValue());
					}else{
						containerDataSource.setComboBoxValue("");
					}
				}else{
					InsuranceDiagnosisContainer containerDataSource = (InsuranceDiagnosisContainer)diagnosis.getContainerDataSource();
					containerDataSource.setComboBoxValue("");
				}
			}
		});
	}
}
