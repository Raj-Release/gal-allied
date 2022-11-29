package com.shaic.claim.premedical.listenerTables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import org.apache.poi.util.SystemOutLogger;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.itextpdf.text.log.SysoCounter;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.DiagnosisComboBox;
import com.shaic.arch.test.InsuranceDiagnosisComboBox;
import com.shaic.arch.test.InsuranceDiagnosisContainer;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.icdSublimitMapping.IcdSubLimitMappingService;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.ClaimRequestDataExtractionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction.MedicalApprovalDataExtractionPagePresenter;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.service.PreMedicalService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAClaimRequestDataExtractionPagePresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
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
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class DiganosisDetailsListenerForPremedical extends ViewComponent {
	private static final long serialVersionUID = 7802397137014194525L;

	@EJB
	private MasterService masterService;

	@EJB
	private PreMedicalService premedicalService;

	@EJB
	private IcdSubLimitMappingService icdSublimitMapService;

	@EJB
	private DBCalculationService dBCalculationService;

	private Map<DiagnosisDetailsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DiagnosisDetailsTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<DiagnosisDetailsTableDTO> data = new BeanItemContainer<DiagnosisDetailsTableDTO>(
			DiagnosisDetailsTableDTO.class);

	private Table table;

	// //private static Window popup;

	private Button btnAdd;

	private Map<String, Object> referenceData;

	private BeanItemContainer<SelectValue> icdBlock;

	private BeanItemContainer<SelectValue> icdCode;

	private List<String> errorMessages;

	// private static Validator validator;
	private Validator validator;

	private String presenterString;

	public TextField dummyField = new TextField();

	private PreauthDTO bean;

	public List<DiagnosisDetailsTableDTO> deletedDTO;

	public TextField listenerField = new TextField();


	@Inject
	private AddDiagnosisPopup diagnosisPopup;

	// public void init(String presenterString, List<ProcedureDTO>
	// procedureList) {
	public void init(PreauthDTO bean, String presenterString) {
		this.presenterString = presenterString;
		this.bean = bean;
		deletedDTO = new ArrayList<DiagnosisDetailsTableDTO>();
		// this.procedureList = procedureList;
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

		if (!ReferenceTable.getGMCProductList().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			initTable(true);
		} else {
			initTable(false);
		}

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
				DiagnosisDetailsTableDTO pedValidationTableDTO = new DiagnosisDetailsTableDTO(
						presenterString);
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
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) itemId;
				Boolean isEnabled = (null != dto
						&& null != dto.getRecTypeFlag() && dto.getRecTypeFlag()
						.toLowerCase().equalsIgnoreCase("c")) ? false : true;
				/*
				 * Delete button always enable bcoz change in diagnosis removed
				 * as per satish sir instruction
				 */
				deleteButton.setEnabled(true);
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						final DiagnosisDetailsTableDTO currentItemId = (DiagnosisDetailsTableDTO) event
								.getButton().getData();

						
					
						if(currentItemId.getPrimaryDiagnosis()!=null && currentItemId.getPrimaryDiagnosis())
						{
							SHAUtils.showErrorMessageBoxWithCaption("Primary Diagnosis cannot be deleted. Change the primary to another row to proceed further", "Error");
						}else{
						if (table.getItemIds().size() > 1) {

							/*
							 * ConfirmDialog dialog = ConfirmDialog
							 * .show(getUI(), "Confirmation",
							 * "Do you want to Delete ?", "No", "Yes", new
							 * ConfirmDialog.Listener() {
							 * 
							 * public void onClose(ConfirmDialog dialog) { if
							 * (!dialog.isConfirmed()) { // Confirmed to
							 * continue DiagnosisDetailsTableDTO dto =
							 * (DiagnosisDetailsTableDTO)currentItemId;
							 * if(dto.getKey() != null && dto.getDiagnosis() !=
							 * null && dto.getDiagnosis().length() > 0) {
							 * deletedDTO
							 * .add((DiagnosisDetailsTableDTO)currentItemId); }
							 * table.removeItem(currentItemId); } else { // User
							 * did not confirm } } });
							 * dialog.setClosable(false);
							 */

							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(
									GalaxyButtonTypesEnum.YES.toString(), "Yes");
							buttonsNamewithType.put(
									GalaxyButtonTypesEnum.NO.toString(), "No");
							HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
									.createConfirmationbox(
											"Do you want to Delete ?",
											buttonsNamewithType);
							Button yesButton = messageBoxButtons
									.get(GalaxyButtonTypesEnum.YES.toString());
							Button noButton = messageBoxButtons
									.get(GalaxyButtonTypesEnum.NO.toString());
							yesButton.addClickListener(new ClickListener() {
								private static final long serialVersionUID = 7396240433865727954L;

								@Override
								public void buttonClick(ClickEvent event) {

										// Confirmed to continue
										DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) currentItemId;
										if (dto.getKey() != null
												&& dto.getDiagnosis() != null
												&& dto.getDiagnosis().length() > 0) {
											deletedDTO.add((DiagnosisDetailsTableDTO) currentItemId);
										}
										table.removeItem(currentItemId);

										if (SHAConstants.PRE_AUTH.equalsIgnoreCase(presenterString) 
												|| SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(presenterString)
												|| SHAConstants.PRE_AUTH_ENHANCEMENT.equalsIgnoreCase(presenterString)
												|| SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(presenterString)) {
											SelectValue selectDiag = new SelectValue();
											selectDiag.setValue(dto.getDiagnosis());
											List<DiagnosisDetailsTableDTO> itemIds = (List<DiagnosisDetailsTableDTO>)table
													.getItemIds();
											if(itemIds != null && !itemIds.isEmpty()){
												for (DiagnosisDetailsTableDTO component : itemIds) {
													if(component.getDiagnosisName() != null && (component.getDiagnosisName().getValue().toUpperCase().contains("FEVER") 
															|| component.getDiagnosisName().getValue().toUpperCase().contains("PYREXIA") 
															|| component.getDiagnosisName().getValue().toUpperCase().contains("MALARIA") 
															|| component.getDiagnosisName().getValue().toUpperCase().contains("DENGUE") 
															|| component.getDiagnosisName().getValue().toUpperCase().contains("TYPHOID") 
															|| component.getDiagnosisName().getValue().toUpperCase().contains("LEPTOSPIROSIS") 
															|| component.getDiagnosisName().getValue().toUpperCase().contains("CHICKUNGUNYA") 
															|| component.getDiagnosisName().getValue().toUpperCase().contains("SWINE"))){
														selectDiag.setValue("FEVER");
														break;
													}else{
														selectDiag.setValue("OTHERS");
													}
												}

												if (SHAConstants.PRE_AUTH.equalsIgnoreCase(presenterString)) {
													fireViewEvent(PreauthWizardPresenter.SET_PRE_AUTH_CATAGORY_VALUE, selectDiag);
												}if (SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(presenterString)) {
													fireViewEvent(PreMedicalPreauthWizardPresenter.SET_PRE_MEDICAL_PRE_AUTH_CATAGORY_VALUE, selectDiag);
												}if (SHAConstants.PRE_AUTH_ENHANCEMENT.equalsIgnoreCase(presenterString)) {
													fireViewEvent(PreauthEnhancemetWizardPresenter.SET_PRE_AUTH_ENHANCEMENT_CATAGORY_VALUE, selectDiag);
												}if (SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(presenterString)) {
													fireViewEvent(PremedicalEnhancementWizardPresenter.SET_PRE_MEDICAL_ENHANCEMENT_CATAGORY_VALUE, selectDiag);
												}

											}
										}
										removeTOAbyvalues();
									}
							});
							noButton.addClickListener(new ClickListener() {
								private static final long serialVersionUID = 7396240433865727954L;

								@Override
								public void buttonClick(ClickEvent event) {

								}
							});
						} else {
							/*
							 * HorizontalLayout layout = new HorizontalLayout(
							 * new Label("One Diagnosis is Mandatory."));
							 * layout.setMargin(true); final ConfirmDialog
							 * dialog = new ConfirmDialog();
							 * dialog.setCaption(""); dialog.setClosable(true);
							 * dialog.setContent(layout);
							 * dialog.setResizable(false);
							 * dialog.setModal(true);
							 * dialog.show(getUI().getCurrent(), null, true);
							 */

							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(
									GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox(
									"One Diagnosis is Mandatory.",
									buttonsNamewithType);
						}}

						// if (table.getItemIds().size() > 1) {
						//
						// } else {
						// HorizontalLayout layout = new HorizontalLayout(
						// new Label("One Diagnosis is Mandatory."));
						// layout.setMargin(false);
						// layout.setWidth("100%");
						//
						// final ConfirmDialog dialog = new ConfirmDialog();
						// dialog.setCaption("");
						// // dialog.setClosable(false);
						// dialog.setClosable(true);
						// dialog.setContent(layout);
						// dialog.setWidth("250px");
						// // dialog.setResizable(false);
						// dialog.setResizable(true);
						// dialog.setModal(true);
						// dialog.show(getUI().getCurrent(), null, true);
						// }
						
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

						// HashMap<String, AbstractField<?>> hashMap =
						// tableItem.get(dto);
						// final ComboBox cmbBox = (ComboBox)
						// hashMap.get("diagnosisName");

						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("Add New Diagnosis");
						popup.setWidth("30%");
						popup.setHeight("30%");
						HashMap<String, AbstractField<?>> hashMap = tableItem
								.get(dto);

						final ComboBox cmbBox = (ComboBox) hashMap
								.get("diagnosisName");

						diagnosisPopup.init(dto, cmbBox, popup);
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

		if (SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION
				.equalsIgnoreCase(presenterString)) {
			table.setVisibleColumns(new Object[] { "diagnosisName",
					"addDiagnosis", /*
					 * "icdChapter", "icdBlock",
					 */"icdCode", "Delete" });
		} else {
			if (siRestricationApplicable) {

				table.setVisibleColumns(new Object[] { "primaryDiagnosis","diagnosisName",
						"addDiagnosis", /*
						 * "icdChapter", "icdBlock",
						 */"icdCode", "pedImpactOnDiagnosis",
						 "sublimitApplicable", "sublimitName", "sublimitAmt",
						 "considerForPayment", "reasonForNotPaying",
						 "sumInsuredRestriction", "Delete" });
			} else {
				table.setVisibleColumns(new Object[] { "primaryDiagnosis","diagnosisName",
						"addDiagnosis", /*
						 * "icdChapter", "icdBlock",
						 */"icdCode", "pedImpactOnDiagnosis",
						 "sublimitApplicable", "sublimitName", "sublimitAmt",
						 "considerForPayment", "reasonForNotPaying", "Delete" });
			}
		}
		/*GLX2020080 - Primary radio button before Hospital Diagnosis */
		table.setColumnHeader("primaryDiagnosis", "Primary");

		/* CR20181279 - Diagnosis Renamed to Hospital Diagnosis */
		table.setColumnHeader("diagnosisName", "Hospital Diagnosis");
		table.setColumnHeader("addDiagnosis", "");
		/* CR20181279 - Removed ICD Chapter & Block */
		table.setColumnHeader("icdChapter", "ICD Chapter");
		table.setColumnHeader("icdBlock", "ICD Block");
		/* CR20181279 - Diagnosis Renamed to Insurance Diagnosis */
		table.setColumnHeader("icdCode", "Insurance Diagnosis");

		table.setColumnHeader("sublimitApplicable", "Sub Limit Applicable");
		table.setColumnHeader("sublimitName", "Sub Limit Name");
		table.setColumnHeader("sublimitAmt", "Sub Limit Amount");
		table.setColumnHeader("considerForPayment", "Consider For Payment");
		table.setColumnHeader("pedImpactOnDiagnosis", "PED impact on diagnosis");
		table.setColumnHeader("reasonForNotPaying", "Reason for not paying");
		table.setColumnHeader("sumInsuredRestriction", "SI Restriction");
		table.setEditable(true);

		table.setColumnWidth("pedImpactOnDiagnosis", 260);
		table.setColumnWidth("reasonForNotPaying", 240);

		// manageListeners();

		// Use a custom field factory to set the edit fields as immediate
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	protected void manageListeners() {

		/*
		 * for (DiagnosisDetailsTableDTO pedValidationTableDTO :
		 * tableItem.keySet()) { HashMap<String, AbstractField<?>> combos =
		 * tableItem.get(pedValidationTableDTO);
		 * 
		 * final ComboBox icdChapterCombo = (ComboBox) combos.get("icdChapter");
		 * final ComboBox icdBlockCombo = (ComboBox) combos.get("icdBlock");
		 * final ComboBox ickCodeCombo = (ComboBox) combos.get("icdBlock");
		 * addICDChapterListener(icdChapterCombo, icdBlockCombo); if
		 * (pedValidationTableDTO.getIcdChapter() != null) {
		 * addICDBlock(pedValidationTableDTO.getIcdChapter().getId(),
		 * icdBlockCombo, pedValidationTableDTO.getIcdBlock()); }
		 * 
		 * }
		 */
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			final DiagnosisDetailsTableDTO pedValidation = (DiagnosisDetailsTableDTO) itemId;
			Boolean isEnabled = (null != pedValidation
					&& null != pedValidation.getRecTypeFlag() && pedValidation
					.getRecTypeFlag().toLowerCase().equalsIgnoreCase("c")) ? false
							: true;
			if (/* !"premedicalEnhancement".equalsIgnoreCase(presenterString) && */!isEnabled) {
				isEnabled = true;
			}
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(pedValidation) == null) {
				tableItem.put(pedValidation,
						new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(pedValidation);
				if (tableRow.get(propertyId) != null)
					return tableRow.get(propertyId);
			}
			tableRow = tableItem.get(pedValidation);
			/*
			 * if (tableItem.get(pedValidation) == null) { tableRow = new
			 * HashMap<String, AbstractField<?>>(); tableItem.put(pedValidation,
			 * new HashMap<String, AbstractField<?>>()); } else { tableRow =
			 * tableItem.get(pedValidation); }
			 */

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

				//				box.addValueChangeListener(new Property.ValueChangeListener() {
				//			            @Override
				//			            public void valueChange(ValueChangeEvent event) {
				//			                
				//			                // tell the custom container that a value has been selected. This is necessary to ensure that the
				//			                // selected value is displayed by the ComboBox
				//			            	SelectValue value = (SelectValue) event.getProperty().getValue();
				//							if (value != null)
				//			            	{
				////								diagnosisContainer.setSelectedBean(value);
				//			            		box.select(value);
				//			            	}
				//			            }
				//			        });

				//					box.setNewItemHandler(new NewItemHandler() {
				//						
				//						private static final long serialVersionUID = -4453822645147859276L;
				//
				//						@Override
				//						public void addNewItem(String newItemCaption) {
				//							SelectValue newDiagonsisValue = masterService.addDiagnosis(newItemCaption);
				//							diagnosisContainer.addItem(newDiagonsisValue);
				//							diagnosisContainer.setNewItemAdded(true);
				//							box.addItem(newDiagonsisValue);
				//							diagnosisContainer.setSelectedBean(newDiagonsisValue);
				//							box.select(newDiagonsisValue);
				//						}
				//					});
				//=======
				//				if(((DiagnosisComboBox) tableRow.get("diagnosisName") != null))
				//				{
				//					final DiagnosisComboBox box = (DiagnosisComboBox) tableRow.get("diagnosisName");
				//					final SuggestingContainer diagnosisContainer = new SuggestingContainer(masterService);
				//					box.setContainerDataSource(diagnosisContainer);	
				//					return box;
				//				}
				//				else
				//				{
				//					final DiagnosisComboBox box =  new DiagnosisComboBox();
				//					final SuggestingContainer diagnosisContainer = new SuggestingContainer(masterService);
				//					box.setContainerDataSource(diagnosisContainer);	
				//					box.setEnabled(isEnabled);
				//					box.setFilteringMode(FilteringMode.STARTSWITH);
				//					box.setTextInputAllowed(true);
				//					box.setNullSelectionAllowed(true);
				//					box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				//					box.setItemCaptionPropertyId("value");
				//					box.setNewItemsAllowed(true);
				//					box.addValueChangeListener(new Property.ValueChangeListener() {
				//				            @Override
				//				            public void valueChange(ValueChangeEvent event) {
				//				                
				//				                // tell the custom container that a value has been selected. This is necessary to ensure that the
				//				                // selected value is displayed by the ComboBox
				//				            	SelectValue value = (SelectValue) event.getProperty().getValue();
				//								if (value != null)
				//				            	{
				//									diagnosisContainer.setSelectedBean(value);
				//									pedValidation.setDiagnosisName(value);
				//									box.setData(pedValidation);
				//				            		box.select(value);
				//				            	}
				//				            }
				//				        });
				//>>>>>>> 4a5aa813daafa6c72c586ed2bc26c0c157fd339f
				//					
				//						box.setNewItemHandler(new NewItemHandler() {
				//							
				//							private static final long serialVersionUID = -4453822645147859276L;
				//
				//							@Override
				//							public void addNewItem(String newItemCaption) {
				//								SelectValue newDiagonsisValue = masterService.addDiagnosis(newItemCaption);
				//								diagnosisContainer.addItem(newDiagonsisValue);
				//								diagnosisContainer.setNewItemAdded(true);
				//								box.addItem(newDiagonsisValue);
				//								pedValidation.setDiagnosisName(newDiagonsisValue);
				//								box.setData(pedValidation);
				//								box.select(newDiagonsisValue);
				//							}
				//						});
				//					
				tableRow.put("diagnosisName", box);
				addDiagnosisNameListener(box);
				//diagnosisContainer = null;
				return box;
				//				}


			}
			else if("primaryDiagnosis".equals(propertyId))
			 {
				 final OptionGroup optionType = new OptionGroup();
				 optionType.addItems(getReadioButtonOptions());
				 optionType.setItemCaption(Boolean.valueOf("true"), "");
				 if(presenterString.equalsIgnoreCase("premedicalPreauth") || presenterString.equalsIgnoreCase("premedicalEnhancement"))
				 {
			     optionType.setEnabled(false);	 
				 }
				 else
				 {
				 optionType.setEnabled(true);
				 }
				 optionType.setData(pedValidation);
				 optionType.setStyleName("inlineStyle");
				 optionType.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
				 optionType.setValue(pedValidation.getPrimaryDiagnosis());
				 if(pedValidation.getPrimaryDiagnosis() != null){
					 if(pedValidation.getPrimaryDiagnosis().booleanValue()){
						 optionType.select(Boolean.valueOf("true"));
					 }
				 }else{
					 optionType.select(null);
				 }
				 addPrimaryListener(optionType);
				 return optionType;
			 }
			// if ("diagnosisName".equals(propertyId)) {
			// ComboBox box = new ComboBox();
			// box.setEnabled(isEnabled);
			// box.setFilteringMode(FilteringMode.STARTSWITH);
			// box.setTextInputAllowed(true);
			// box.setNullSelectionAllowed(true);
			// box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			// box.setItemCaptionPropertyId("value");
			// box.setNewItemsAllowed(true);
			// CustomLazyContainer customLazyContainer = new
			// CustomLazyContainer(3, "value", masterService, premedicalService
			// ,"diagnosis");
			// /* customLazyContainer.addContainerProperty("value",
			// SelectValue.class, null);*/
			// customLazyContainer.addContainerProperty("value",
			// Object.class, null);
			// box.setContainerDataSource(customLazyContainer);
			// box.setConverter(getConverter(customLazyContainer));
			// //addDiagnosisValues(box);
			// tableRow.put("diagnosisName", box);
			// // addDiagnosisListener(box);
			//
			// //addICDChapterListener(box , null);
			// //addDiagnosisListener(box);
			// return box;
			// }
			/*
			 * R20181279 - Removed ICD Code & Block else if
			 * ("icdChapter".equals(propertyId)) { GComboBox box = new
			 * GComboBox(); box.setEnabled(isEnabled); box.setWidth("150px");
			 * if(presenterString.equalsIgnoreCase("premedicalPreauth")){
			 * box.setRequired(false); } addICDChapterValues(box);
			 * tableRow.put("icdChapter", box); // To fill the exising values
			 * final ComboBox icdBlock = (ComboBox) tableRow.get("icdBlock");
			 * box.setData(pedValidation); addICDChapterListener(box, icdBlock);
			 * return box; } else if ("icdBlock".equals(propertyId)) { GComboBox
			 * box = new GComboBox(); box.setWidth("150px");
			 * box.setData(pedValidation);
			 * if(presenterString.equalsIgnoreCase("premedicalPreauth")){
			 * box.setRequired(false); } tableRow.put("icdBlock", box); ComboBox
			 * icdCodeCmb = (ComboBox) tableRow.get("icdCode"); if
			 * (pedValidation.getIcdChapter() != null) {
			 * addICDBlock(pedValidation.getIcdChapter().getId(), box,
			 * pedValidation.getIcdBlock()); } addICDBlockListener(box,
			 * icdCodeCmb); box.setEnabled(isEnabled); return box; }
			 */else if ("icdCode".equals(propertyId)) {

				 InsuranceDiagnosisComboBox box = new InsuranceDiagnosisComboBox();
				 final InsuranceDiagnosisContainer diagnosisContainer = new InsuranceDiagnosisContainer(
						 masterService);
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
				 if (presenterString.equalsIgnoreCase("premedicalPreauth")
						 || presenterString
						 .equalsIgnoreCase("premedicalEnhancement")
						 || presenterString
						 .equalsIgnoreCase("meical_approval_data_extraciton")) {
					 box.setRequired(false);
					 box.setEnabled(false);
				 }

				 box.setData(pedValidation);
				 addSublimitForSelectedIcdCode(box);
				 // box.setFilteringMode(FilteringMode.CONTAINS);
				 if (pedValidation.getIcdCode() != null
						 && (presenterString
								 .equalsIgnoreCase("premedicalEnhancement")
								 || bean.getIsBack()
								 || (presenterString
										 .equalsIgnoreCase("meical_approval_claim_request_data_extraciton") && !(bean
												 .getHospitalizaionFlag())) || (presenterString
														 .equalsIgnoreCase("meical_approval_data_extraciton") && !(bean
																 .getHospitalizaionFlag())))) {
					 addICDCode(pedValidation.getIcdCode().getId(), box,
							 pedValidation.getIcdCode());
				 } else {
					 addICDCode(0l, box, pedValidation.getIcdCode());
					 if (!(presenterString.equalsIgnoreCase("premedicalPreauth") || presenterString
							 .equalsIgnoreCase("meical_approval_data_extraciton"))) {
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
				 box.setWidth("100px");

				 GComboBox icdCodeCmb = (GComboBox) tableRow.get("icdCode");

				 DiagnosisDetailsTableDTO updatedpedDto = (DiagnosisDetailsTableDTO) icdCodeCmb
						 .getData();

				 box.setData(updatedpedDto);

				 addSublimitApplicableValues(box, "sublimitApplicable");
				 tableRow.put("sublimitApplicable", box);

				 List<SelectValue> applicableList = ((BeanItemContainer<SelectValue>) box
						 .getContainerDataSource()).getItemIds();

				 if (updatedpedDto.isSublimitMapAvailable()) {
					 if (applicableList != null
							 && !applicableList.isEmpty()
							 && (ReferenceTable.COMMONMASTER_YES)
							 .equals(applicableList.get(0).getId())) {
						 box.setValue(((BeanItemContainer<SelectValue>) box
								 .getContainerDataSource()).getItemIds().get(0));
					 } else {
						 box.setValue(((BeanItemContainer<SelectValue>) box
								 .getContainerDataSource()).getItemIds().get(1));
					 }
				 }

				 box.setEnabled(!updatedpedDto.isSublimitMapAvailable());

				 if (pedValidation.getSublimitApplicable() == null) {
					 SelectValue value = new SelectValue();
					 value.setId(ReferenceTable.COMMONMASTER_NO);
					 value.setValue("No");
					 pedValidation.setSublimitApplicable(value);
				 }
				 addSublimitApplicableListener(box);
				 setEnableOrDisableSection(box, pedValidation);
				 return box;
			 } else if ("sublimitName".equals(propertyId)) {
				 GComboBox box = new GComboBox();
				 tableRow.put("sublimitName", box);
				 final TextField field = (TextField) tableRow.get("sublimitAmt");

				 GComboBox icdCodeCmb = (GComboBox) tableRow.get("icdCode");

				 DiagnosisDetailsTableDTO updatedpedDto = (DiagnosisDetailsTableDTO) icdCodeCmb
						 .getData();

				 box.setData(updatedpedDto);
				 box.setEnabled(!updatedpedDto.isSublimitMapAvailable()
						 || isEnabled);
				 addSublimtValues(box);
				 addSublimitListener(box, field);
				 addSubLimitLIstnerForAlert(box);
				 if (updatedpedDto.isSublimitMapAvailable()) {
					 box.setEnabled(false);
				 }
				 return box;
			 } else if ("sublimitAmt".equals(propertyId)) {
				 TextField field = new TextField();
				 field.setWidth("200px");
				 field.setNullRepresentation("");
				 field.setReadOnly(true);
				 field.setEnabled(isEnabled);
				 field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				 tableRow.put("sublimitAmt", field);
				 ComboBox sublimitCombo = (ComboBox) tableRow
						 .get("sublimitName");
				 if (pedValidation.getSublimitApplicable() != null
						 && (ReferenceTable.COMMONMASTER_NO)
						 .equals(pedValidation.getSublimitApplicable()
								 .getId()) && sublimitCombo != null) {
					 setValuesToNull(field, sublimitCombo);
					 /*
					  * field.setValue(""); sublimitCombo.setValue(null);
					  */
				 }
				 return field;
			 } else if ("considerForPayment".equals(propertyId)) {
				 GComboBox box = new GComboBox();
				 box.setData(pedValidation);
				 box.setEnabled(isEnabled);
				 box.setWidth("100px");
				 tableRow.put("considerForPayment", box);
				 addCommonValues(box, "considerForPayment");

				 // CR R20181300
				 if (SHAConstants.PRE_AUTH.equalsIgnoreCase(presenterString)
						 || SHAConstants.PRE_AUTH_ENHANCEMENT
						 .equalsIgnoreCase(presenterString)
						 || SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION
						 .equalsIgnoreCase(presenterString)) {
					 box.setEnabled(true);
				 } else {
					 box.setEnabled(false);
				 }

				 addConsiderForPaymentListener(box);

				 return box;
			 } // CR R20181300
			 else if ("pedImpactOnDiagnosis".equals(propertyId)) { // CR
				 // R20181300
				 GComboBox box = new GComboBox();
				 box.setWidth("100%");
				 box.setData(pedValidation);
				 tableRow.put("pedImpactOnDiagnosis", box);
				 if (bean.getNewIntimationDTO().getPolicy().getProduct()
						 .getCode() != null
						 && bean.getNewIntimationDTO()
						 .getPolicy()
						 .getProduct()
						 .getCode()
						 .equalsIgnoreCase(
								 SHAConstants.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS)
								 && ((SHAConstants.PRE_AUTH
										 .equalsIgnoreCase(presenterString))
										 || SHAConstants.PRE_AUTH_ENHANCEMENT
										 .equalsIgnoreCase(presenterString) || SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION
										 .equalsIgnoreCase(presenterString))) {
					 box.setEnabled(true);
				 } else if ((((bean.getNewIntimationDTO().getPolicy()
						 .getProductType() != null
						 && bean.getNewIntimationDTO().getPolicy()
						 .getProductType().getKey() != null && bean
						 .getNewIntimationDTO().getPolicy().getProductType()
						 .getKey().intValue() != 2904) || (bean
								 .getNewIntimationDTO().getPolicy().getProduct()
								 .getCode()
								 .equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS))) && (SHAConstants.PRE_AUTH
										 .equalsIgnoreCase(presenterString)
										 || SHAConstants.PRE_AUTH_ENHANCEMENT
										 .equalsIgnoreCase(presenterString) || SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION
										 .equalsIgnoreCase(presenterString)))
										 || (bean.getNewIntimationDTO()
												 .getPolicy()
												 .getProduct()
												 .getCode()
												 .equalsIgnoreCase(
														 SHAConstants.GMC_CONTINUITY_PRODUCT_CODE)
														 && bean.getNewIntimationDTO()
														 .getPolicy()
														 .getSectionCode()
														 .equalsIgnoreCase(
																 SHAConstants.GMC_WITH_CONTINUITY_SECTION_CODE)
																 && !SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION
																 .equalsIgnoreCase(presenterString)
																 && !SHAConstants.PRE_MEDICAL_PRE_AUTH
																 .equalsIgnoreCase(presenterString) && !SHAConstants.PRE_MEDICAL_ENHANCEMENT
																 .equalsIgnoreCase(presenterString))) {
					 box.setEnabled(true);
				 }

				 else {
					 box.setEnabled(false);
				 }

				 addPedImpactOnDiagnosisValues(box, "pedImpactOnDiagnosis");
				 addPedImpactOnDiagnosisListener(box);
				 return box;
			 } else if ("reasonForNotPaying".equals(propertyId)) { // CR
				 // R20181300
				 GComboBox box = new GComboBox();
				 box.setWidth("100%");
				 box.setData(pedValidation);
				 tableRow.put("reasonForNotPaying", box);

				 HashMap<String, AbstractField<?>> hashMap = tableItem
						 .get(pedValidation);
				 ComboBox considerPaymentCmb = (ComboBox) hashMap
						 .get("considerForPayment");
				 SelectValue considerPaymentSelect = (SelectValue) considerPaymentCmb
						 .getValue();

				 if(((bean.getNewIntimationDTO().getPolicy().getProductType() != null
						 && bean.getNewIntimationDTO().getPolicy()
						 .getProductType().getKey() != null
						 && bean.getNewIntimationDTO().getPolicy()
						 .getProductType().getKey().intValue() != 2904 && considerPaymentSelect != null))			
						 && ReferenceTable.COMMONMASTER_NO.equals(pedValidation
								 .getConsiderForPayment().getId()))
					 /*
					  * && pedValidation != null &&
					  * pedValidation.getConsiderForPayment() != null &&
					  * ReferenceTable
					  * .COMMONMASTER_NO.equals(pedValidation.getConsiderForPayment
					  * ().getId()))
					  */{
					 box.setEnabled(true);
				 } else {
					 box.setEnabled(false);
				 }

				 if (((bean.getNewIntimationDTO().getPolicy().getProductType() != null
						 && bean.getNewIntimationDTO().getPolicy()
						 .getProductType().getKey() != null && bean
						 .getNewIntimationDTO().getPolicy().getProductType()
						 .getKey().intValue() != 2904)
						 && (SHAConstants.PRE_AUTH
								 .equalsIgnoreCase(presenterString) || (bean
										 .getNewIntimationDTO()
										 .getPolicy()
										 .getProduct()
										 .getCode()
										 .equalsIgnoreCase(
												 SHAConstants.GMC_CONTINUITY_PRODUCT_CODE) && bean
												 .getNewIntimationDTO()
												 .getPolicy()
												 .getSectionCode()
												 .equalsIgnoreCase(
														 SHAConstants.GMC_WITH_CONTINUITY_SECTION_CODE)))
														 || SHAConstants.PRE_AUTH_ENHANCEMENT
														 .equalsIgnoreCase(presenterString) || SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION
														 .equalsIgnoreCase(presenterString))) {
					 box.setEnabled(true);
				 } else {
					 box.setEnabled(false);
				 }
				 /*
				  * if(bean.getNewIntimationDTO().getPolicy().getProduct().getCode
				  * (
				  * ).equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)
				  * ) { box.setEnabled(true); }
				  */
				 addReasonForNotPayingValues(box, "reasonForNotPaying");
				 return box;
			 } // CR R20181300
			 else if ("sumInsuredRestriction".equals(propertyId)) {
				 GComboBox box = new GComboBox();
				 box.setEnabled(isEnabled);

				 // Sublimit Based on SI Restriction CR2019050

				 /*box.setData((bean.getNewIntimationDTO().getInsuredPatient()
						.getInsuredSumInsured() != null && bean
						.getNewIntimationDTO().getInsuredPatient()
						.getInsuredSumInsured() != 0) ? bean
								.getNewIntimationDTO().getInsuredPatient()
								.getInsuredSumInsured() : bean.getNewIntimationDTO()
								.getPolicy().getTotalSumInsured());*/

				 // Sublimit Based on SI Restriction CR2019050
				 box.setData(pedValidation);
				 bean.getNewIntimationDTO().setOrginalSI((bean.getNewIntimationDTO().getInsuredPatient()
						 .getInsuredSumInsured() != null && bean
						 .getNewIntimationDTO().getInsuredPatient()
						 .getInsuredSumInsured() != 0) ? bean
								 .getNewIntimationDTO().getInsuredPatient()
								 .getInsuredSumInsured() : bean.getNewIntimationDTO()
								 .getPolicy().getTotalSumInsured());
				 addSIRestrictionListener(box);
				 box.setWidth("200px");
				 tableRow.put("sumInsuredRestriction", box);
				 addSIValues(box);
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

	// private void addDiagnosisValues(ComboBox diagnosisCombo) {
	// @SuppressWarnings("unchecked")
	// BeanItemContainer<SelectValue> diagnosis =
	// (BeanItemContainer<SelectValue>) referenceData
	// .get("diagnosisName");
	// diagnosisCombo.setContainerDataSource(diagnosis);
	// diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
	// diagnosisCombo.setItemCaptionPropertyId("value");
	//
	// }

	@SuppressWarnings("unchecked")
	private void addCommonValues(ComboBox diagnosisCombo, String tableColumnName) {

		BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
				.get(tableColumnName);

		diagnosisCombo.setContainerDataSource(commonValues);
		diagnosisCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		diagnosisCombo.setItemCaptionPropertyId("value");

	}

	/***
	 * alert added for CR2019253
	 */
	public void addSubLimitLIstnerForAlert(GComboBox sublimitCombo) {

		// TODO Auto-generated method stub/***
		if(sublimitCombo != null){
			sublimitCombo.addValueChangeListener(new ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					// TODO Auto-generated method stub
					DiagnosisDetailsTableDTO pedValidation = (DiagnosisDetailsTableDTO) ((GComboBox) event
							.getProperty()).getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidation);
					GComboBox sublimitCombo = (GComboBox) event.getProperty();
					GComboBox sublimitApplicable = (GComboBox) hashMap
							.get("sublimitApplicable");
					SelectValue select = sublimitApplicable != null ? (SelectValue) sublimitApplicable
							.getValue() : null;

							SublimitFunObject sublimitNameSelect= (SublimitFunObject) sublimitCombo.getValue();
							if((ReferenceTable.POS_MED_CLASSIC_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									|| ReferenceTable.PACK_ACCIDENT_CARE_012.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
									|| ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY_98.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
									&& (bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null &&
									bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan().equalsIgnoreCase("G"))
									&& sublimitNameSelect !=null
									&& (sublimitNameSelect.getName().equalsIgnoreCase("NEW BORN BABY EXPENSES")
										|| sublimitNameSelect.getName().equalsIgnoreCase("NEW BORN BABY COVER") )
									&& select !=null && select.getId().equals(ReferenceTable.COMMONMASTER_YES)){
								HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
								buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
								HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
										.createInformationBox(SHAConstants.NEW_BORN_BABAY_MESSAGE_MCI_GOLD, buttonsNamewithType);
								Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
							}

				}
			});
		}

	}

	private void addPedImpactOnDiagnosisValues(GComboBox pedimapctCombo,
			String tableColumnName) {

		BeanItemContainer<SelectValue> containerValues = (BeanItemContainer<SelectValue>) referenceData
				.get(tableColumnName);

		pedimapctCombo.setContainerDataSource(containerValues);
		pedimapctCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		pedimapctCombo.setItemCaptionPropertyId("value");

		List<SelectValue> applicableList = containerValues.getItemIds();
		DiagnosisDetailsTableDTO pedValidation = (DiagnosisDetailsTableDTO) pedimapctCombo
				.getData();

		if (pedValidation.getPedImpactOnDiagnosis() != null) {

			if (applicableList != null && !applicableList.isEmpty()) {
				for (SelectValue selectValue : applicableList) {
					if (pedValidation.getPedImpactOnDiagnosis().getId()
							.equals(selectValue.getId())) {
						pedimapctCombo.setValue(selectValue);
					}
				}
			}
		}

	}

	private void addReasonForNotPayingValues(GComboBox reasonNotPayingCombo,
			String tableColumnName) {

		BeanItemContainer<SelectValue> containerValues = (BeanItemContainer<SelectValue>) referenceData
				.get(tableColumnName);

		reasonNotPayingCombo.setContainerDataSource(containerValues);
		reasonNotPayingCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonNotPayingCombo.setItemCaptionPropertyId("value");

		List<SelectValue> applicableList = containerValues.getItemIds();
		DiagnosisDetailsTableDTO pedValidation = (DiagnosisDetailsTableDTO) reasonNotPayingCombo
				.getData();

		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(pedValidation);

		ComboBox cmbConsiderPaymentBox = (ComboBox) hashMap
				.get("considerForPayment");

		if (cmbConsiderPaymentBox != null
				&& cmbConsiderPaymentBox.getValue() != null) {
			SelectValue considerPaymentSelect = (SelectValue) cmbConsiderPaymentBox
					.getValue();

			if (pedValidation.getReasonForNotPaying() != null
					&& bean.getNewIntimationDTO().getPolicy().getProductType()
					.getKey().intValue() != 2904) {

				if (applicableList != null
						&& !applicableList.isEmpty()
						&& ReferenceTable.COMMONMASTER_NO
						.equals(considerPaymentSelect.getId())) {
					for (SelectValue selectValue : applicableList) {
						if (pedValidation.getReasonForNotPaying().getId()
								.equals(selectValue.getId())
								&& pedValidation.getConsiderForPayment() != null) {
							reasonNotPayingCombo.setValue(selectValue);
						} else {
							reasonNotPayingCombo.setValue(null);
						}
					}
				} else {
					reasonNotPayingCombo.setValue(null);
					// pedValidation.setReasonForNotPaying(null);
				}
			} else if (ReferenceTable.COMMONMASTER_YES
					.equals(considerPaymentSelect.getId())) {
				reasonNotPayingCombo.setValue(null);
				reasonNotPayingCombo.setEnabled(false);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void addSublimitApplicableValues(GComboBox diagnosisCombo,
			String tableColumnName) {

		BeanItemContainer<SelectValue> commonValues = (BeanItemContainer<SelectValue>) referenceData
				.get(tableColumnName);

		List<SublimitFunObject> list = (List<SublimitFunObject>) referenceData
				.get("sublimitDBDetails");
		if (list.isEmpty()) {
			List<SelectValue> newListValue = new ArrayList<SelectValue>();
			List<SelectValue> itemIds = commonValues.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if (selectValue != null
						&& selectValue.getId() != null
						&& selectValue.getId().equals(
								ReferenceTable.COMMONMASTER_NO)) {
					newListValue.add(selectValue);
				}
			}
			/*
			 * if(itemIds!= null && itemIds.size() == 2){
			 * if(itemIds.get(0).getId
			 * ().equals(ReferenceTable.COMMONMASTER_NO)){
			 * newListValue.add(itemIds.get(1)); } else
			 * if(itemIds.get(1).getId().equals(ReferenceTable.COMMONMASTER_NO))
			 * { newListValue.add(itemIds.get(0)); } }
			 */
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
		// if(list.isEmpty()) {
		// List<SelectValue> newListValue = new ArrayList<SelectValue>();
		// List<SelectValue> itemIds = commonValues.getItemIds();
		// for (SelectValue selectValue : itemIds) {
		// if(selectValue != null && selectValue.getId() != null &&
		// selectValue.getId().equals(ReferenceTable.COMMONMASTER_NO)) {
		// newListValue.add(selectValue);
		// }
		// }
		// DiagnosisDetailsTableDTO pedValidationTableDTO =
		// (DiagnosisDetailsTableDTO) comboBox
		// .getData();
		// SelectValue sublimitApplicable2 =
		// pedValidationTableDTO.getSublimitApplicable();
		// HashMap<String, AbstractField<?>> hashMap = tableItem
		// .get(pedValidationTableDTO);
		// GComboBox sublimitApplicable =
		// (GComboBox)hashMap.get("sublimitApplicable");
		// BeanItemContainer<SelectValue> beanItemContainer = new
		// BeanItemContainer<SelectValue>(SelectValue.class);
		// beanItemContainer.addAll(newListValue);
		// // referenceData.put("sublimitApplicable", beanItemContainer);
		// sublimitApplicable.setContainerDataSource(beanItemContainer);
		// sublimitApplicable.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		// sublimitApplicable.setItemCaptionPropertyId("value");
		// pedValidationTableDTO.setSublimitApplicable(sublimitApplicable2);
		// sublimitApplicable.setValue(pedValidationTableDTO.getSublimitApplicable());
		// }
	}

	@SuppressWarnings("unchecked")
	public void addSIValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> siValues = (BeanItemContainer<SelectValue>) referenceData
				.get("sumInsuredRestriction");

		if (siValues.getItemIds().isEmpty()) {
			comboBox.setEnabled(false);
		}
		comboBox.setContainerDataSource(siValues);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		if (ReferenceTable.getHealthGainProducts().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			comboBox.setItemCaptionPropertyId("value");

		} else {
			comboBox.setItemCaptionPropertyId("value");
		}

	}

	public void setCustomDiagValueToContainer(SelectValue selValue,
			ComboBox cmbBox) {

		Container containerDataSource = cmbBox.getContainerDataSource();
		Long selValId = selValue.getId() + 1;
		selValue.setId(selValId);
		containerDataSource.addItem(selValue.getId());
		containerDataSource.getContainerProperty(selValue.getId(), "value")
		.setValue(selValue);
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
				Double originalSI = bean.getNewIntimationDTO().getOrginalSI();

				DiagnosisDetailsTableDTO pedValidation = (DiagnosisDetailsTableDTO)siRestrictCombo.getData();

				if (!ReferenceTable.getHealthGainProducts().containsKey(
						bean.getNewIntimationDTO().getPolicy().getProduct()
						.getKey())) {

					if (null != selectedSI
							&& originalSI != null
							&& originalSI <= SHAUtils
							.getDoubleValueFromString(selectedSI
									.toString())) {
						VerticalLayout layout = new VerticalLayout(
								new Label(
										"<b style = 'color: red;'> SI Restriction Should be below the Original SI. </b>",
										ContentMode.HTML));
						showErrorPopup(siRestrictCombo, layout);

					}

					// Sublimit Based on SI Restriction CR2019050
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidation);

					ComboBox sublimitCombo = (ComboBox) hashMap.get("sublimitName");

					SublimitFunObject sublimitName = (SublimitFunObject)sublimitCombo.getValue();

					ComboBox sublimitApplCombo = (ComboBox) hashMap.get("sublimitApplicable");

					SelectValue applicableSelect = sublimitApplCombo.getValue() != null ? (SelectValue) sublimitApplCombo.getValue() : null;

					if (applicableSelect.getId().equals(ReferenceTable.COMMONMASTER_YES)) {

						Long restrictedSIKey = selectedSI != null ? ((SelectValue)selectedSI).getId() : 0l;

						Double insuredAge = bean.getNewIntimationDTO().getInsuredPatient().getInsuredAge();

						String policyPlan = bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? bean.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";
						


						if(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
								&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
										SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
										|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
										|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
										|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
												SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
												&& bean.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
							policyPlan = bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
						}

						if(bean.getPreauthDataExtractionDetails().getSection() != null){

							if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
									ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								referenceData.put("sublimitDBDetails", dBCalculationService
										.getClaimedAmountDetailsForSectionForGMC(bean.getNewIntimationDTO().getPolicy().getKey(),
												originalSI, insuredAge,bean.getPreauthDataExtractionDetails().getSection().getId(),
												bean.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? bean.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0", (bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : bean.getClaimDTO().getClaimSubCoverCode())));
							}else{
								referenceData.put("sublimitDBDetails", dBCalculationService
										.getClaimedAmountDetailsForSection(bean.getNewIntimationDTO().getPolicy().getKey(),
												originalSI, restrictedSIKey, insuredAge,bean.getPreauthDataExtractionDetails().getSection().getId(),
												policyPlan, (bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : bean.getClaimDTO().getClaimSubCoverCode()),bean.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
							}		

						}else{
							if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
									ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
								referenceData.put("sublimitDBDetails", dBCalculationService
										.getClaimedAmountDetailsForSectionForGMC(bean.getNewIntimationDTO().getPolicy().getKey(),
												originalSI, insuredAge,0l,"0", (bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : bean.getClaimDTO().getClaimSubCoverCode())));
							}else{
								referenceData.put("sublimitDBDetails", dBCalculationService
										.getClaimedAmountDetailsForSection(bean.getNewIntimationDTO().getPolicy().getProduct().getKey(),
												originalSI, restrictedSIKey, insuredAge,0l,policyPlan, (bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : bean.getClaimDTO().getClaimSubCoverCode()), bean.getNewIntimationDTO().getInsuredPatient().getInsuredId()));
							}

						}

						addSublimtValues(sublimitCombo);


						BeanItemContainer<SublimitFunObject> sublimit = (BeanItemContainer<SublimitFunObject>)sublimitCombo.getContainerDataSource();
						if(sublimit.getItemIds() != null && !sublimit.getItemIds().isEmpty()) {
							List<SublimitFunObject> sublimitList = sublimit.getItemIds();
							for (int i = 0;  i<sublimitList.size(); i++) {
								if(sublimitName != null && sublimitName.getName() != null && !sublimitName.getName().isEmpty()){
									if(sublimit.getIdByIndex(i).getName().equalsIgnoreCase(sublimitName.getName())){
										sublimitCombo.setValue(sublimit.getIdByIndex(i));
									}
								}
							}
						}
					}	
				}
			}
		});
	}

	private void addDiagnosisNameListener(ComboBox diagnosisName) {
		diagnosisName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2332276795125344767L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				GComboBox diagnosis = (GComboBox) event.getProperty();
				if(diagnosis.getValue() != null) {
					DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) diagnosis.getData();

					SuggestingContainer containerDataSource = (SuggestingContainer)diagnosis.getContainerDataSource();

					if(diagnosis != null && diagnosis.getValue() != null){
						SelectValue selected = (SelectValue)diagnosis.getValue();
						containerDataSource.setComboBoxValue(selected.getValue());
					}else{
						containerDataSource.setComboBoxValue("");
					}

					if (SHAConstants.PRE_AUTH.equalsIgnoreCase(presenterString) 
							|| SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(presenterString)
							|| SHAConstants.PRE_AUTH_ENHANCEMENT.equalsIgnoreCase(presenterString)
							|| SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(presenterString)) {
						SelectValue selectDiag = new SelectValue();
						List<DiagnosisDetailsTableDTO> itemIds = (List<DiagnosisDetailsTableDTO>)table
								.getItemIds();
						if(itemIds != null && !itemIds.isEmpty()){
							for (DiagnosisDetailsTableDTO component : itemIds) {
								if(component.getDiagnosisName() != null && (component.getDiagnosisName().getValue().toUpperCase().contains("FEVER") 
										|| component.getDiagnosisName().getValue().toUpperCase().contains("PYREXIA") 
										|| component.getDiagnosisName().getValue().toUpperCase().contains("MALARIA") 
										|| component.getDiagnosisName().getValue().toUpperCase().contains("DENGUE") 
										|| component.getDiagnosisName().getValue().toUpperCase().contains("TYPHOID") 
										|| component.getDiagnosisName().getValue().toUpperCase().contains("LEPTOSPIROSIS") 
										|| component.getDiagnosisName().getValue().toUpperCase().contains("CHICKUNGUNYA") 
										|| component.getDiagnosisName().getValue().toUpperCase().contains("SWINE"))){
									selectDiag.setValue("FEVER");
									break;
								}else{
									selectDiag.setValue("OTHERS");
								}
							}
						} else {
							selectDiag = (SelectValue) diagnosis.getValue();
						}

						if (SHAConstants.PRE_AUTH.equalsIgnoreCase(presenterString)) {
							fireViewEvent(PreauthWizardPresenter.SET_PRE_AUTH_CATAGORY_VALUE, selectDiag);
						}if (SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(presenterString)) {
							fireViewEvent(PreMedicalPreauthWizardPresenter.SET_PRE_MEDICAL_PRE_AUTH_CATAGORY_VALUE, selectDiag);
						}if (SHAConstants.PRE_AUTH_ENHANCEMENT.equalsIgnoreCase(presenterString)) {
							fireViewEvent(PreauthEnhancemetWizardPresenter.SET_PRE_AUTH_ENHANCEMENT_CATAGORY_VALUE, selectDiag);
						}if (SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(presenterString)) {
							fireViewEvent(PremedicalEnhancementWizardPresenter.SET_PRE_MEDICAL_ENHANCEMENT_CATAGORY_VALUE, selectDiag);
						}
					}

					List<DiagnosisDetailsTableDTO> values = getValues();
					int count = 0;
					for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : values) {
						if(dto != null && dto.getDiagnosisName() != null && dto.getDiagnosisName().getId() != null && diagnosisDetailsTableDTO.getDiagnosisName() != null && diagnosisDetailsTableDTO.getDiagnosisName().getId() != null && dto.getDiagnosisName().getId().equals(diagnosisDetailsTableDTO.getDiagnosisName().getId())) {
							count += 1;
						}
					}
					if(count > 1) {
						/*HorizontalLayout layout = new HorizontalLayout(new Label("Duplicate Diagnosis is not allowed. Please choose different diagnosis"));
		        		layout.setMargin(true);
		        		final ConfirmDialog dialog = new ConfirmDialog();
		        		dialog.setCaption("Warning");
		        		dialog.setClosable(true);
		        		dialog.setContent(layout);
		        		dialog.setResizable(false);
		        		dialog.setModal(true);
		        		diagnosis.setValue(null);
		        		dialog.show(getUI().getCurrent(), null, true);*/
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
						GalaxyAlertBox.createWarningBox("Duplicate Diagnosis is not allowed. Please choose different diagnosis", buttonsNamewithType);
					}

					if((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
							||!ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))
							&& bean.getClaimDTO().getClaimType() != null
							&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(bean.getClaimDTO().getClaimType().getId())) 					
					{
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);

						SelectValue diagSelect = (SelectValue)diagnosis.getValue();
						GComboBox icdCmb = hashMap.get("icdCode") != null ? (GComboBox)hashMap.get("icdCode") : null;
						SelectValue icdSelect = icdCmb != null ? (SelectValue)icdCmb.getValue() : null;

						if(icdSelect != null && icdSelect.getValue() != null && diagSelect != null && diagSelect.getValue() != null) {
							String icdCode = icdSelect.getValue();
							String split[] = icdCode.split("-");

							if(split.length>0){
								String icdCodeValue = split[split.length-1];  // bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode().getCommonValue();
								bean.setMulticlaimAvailFlag(dBCalculationService.getClaimRestrictionAvailable(bean.getNewIntimationDTO().getPolicy().getKey(),
										bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getNewIntimationDTO().getKey(),
										icdCodeValue,diagSelect.getValue()
										/*bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getDiagnosisName().getValue()*/));
							}
						}
					}
					//Dinesh Murugan	
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
					GComboBox sublimitApplicableCombo = (GComboBox)hashMap.get("sublimitApplicable");
					if(sublimitApplicableCombo!=null)
					{
						SelectValue value = (SelectValue) diagnosis.getValue();
						SelectValue subLimitApplicableValue = new SelectValue();
						Boolean diagBoolean = (value.getValue().toUpperCase()).contains("CATARACT"); 
						//SelectValue subLimitApplicableValue = new SelectValue();
						if(diagBoolean){
							subLimitApplicableValue.setId(ReferenceTable.COMMONMASTER_YES);
							subLimitApplicableValue.setValue(SHAConstants.YES);
							sublimitApplicableCombo.setValue(subLimitApplicableValue);
							sublimitApplicableCombo.setEnabled(true);
						}
						else {
							subLimitApplicableValue.setId(ReferenceTable.COMMONMASTER_NO);
							subLimitApplicableValue.setValue(SHAConstants.No);
							sublimitApplicableCombo.setValue(subLimitApplicableValue);
							sublimitApplicableCombo.setEnabled(true);
						}
					}


				}else{
					SuggestingContainer containerDataSource = (SuggestingContainer)diagnosis.getContainerDataSource();
					containerDataSource.setComboBoxValue("");
				}
			}
		});
	}

	private void insuranceDiagnosisListener(ComboBox diagnosisName) {
		diagnosisName.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2332276795125344767L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				ComboBox diagnosis = (ComboBox) event.getProperty();
				if (diagnosis.getValue() != null) {
					DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) diagnosis
							.getData();

					InsuranceDiagnosisContainer containerDataSource = (InsuranceDiagnosisContainer) diagnosis
							.getContainerDataSource();

					if (diagnosis != null && diagnosis.getValue() != null) {
						SelectValue selected = (SelectValue) diagnosis
								.getValue();
						containerDataSource.setComboBoxValue(selected
								.getCommonValue());
						String excludedIcdCode=selected.getCommonValue();
						Long prodKey = bean.getNewIntimationDTO().getPolicy().getProduct().getKey();
						String prodCode= bean.getNewIntimationDTO().getPolicy().getProduct().getCode();
						String policyNo=bean.getNewIntimationDTO().getPolicy().getPolicyNumber();
						String insuredNo=null;
						String commonIP=null;
						//String commonIP="";
						
						if(excludedIcdCode !=null) {
						Map<String,String> PerExcludedICDCode = dBCalculationService
								.getPermanentExclusionsIcdMapping(excludedIcdCode,prodKey,prodCode,policyNo,insuredNo,commonIP);
						String icdCodeFlag= PerExcludedICDCode.get("flag");
						String icdCodeRemarks= PerExcludedICDCode.get("remarks");
						String exIcdCode=PerExcludedICDCode.get(icdCode);
						System.out.println("icdCodeFlag"+icdCodeFlag);
						System.out.println("icdCodeRemarks"+icdCodeRemarks);
						if(icdCodeFlag!=null && icdCodeFlag.equalsIgnoreCase("Y")){
						SHAUtils.showMessageBoxWithCaption(icdCodeRemarks,"information");
						}
						else if(icdCodeFlag!=null && icdCodeFlag.equalsIgnoreCase("A")){
							SHAUtils.showMessageBoxWithCaption(icdCodeRemarks,"information");
							}
						}
						
						//Covid 19 GLX2020086
						
						String insuranceDiagnosisCode=selected.getValue();
						String productCode=bean.getNewIntimationDTO().getPolicy().getProduct().getCode();
						Date policyFromDate=bean.getNewIntimationDTO().getPolicy().getPolicyFromDate();
						
						// code changes for GLX2020168
						/*Date issuedPeriodStartDate= SHAUtils.formatDateWithoutTime("01-07-2020");
						Date issuedPeriodEndDate = SHAUtils.formatDateWithoutTime("30-09-2020");*/
						Date issuedPeriodStartDate= SHAUtils.formatDateWithoutTime("09-12-2020");
						Date issuedPeriodEndDate = SHAUtils.formatDateWithoutTime("31-03-2021");
						Date currnetDate = new Date();


						if(insuranceDiagnosisCode !=null){
							if(productCode!=null && policyFromDate !=null){
								if((ReferenceTable.getCovidInsuranceDiagnosisCode().contains(insuranceDiagnosisCode))
										/*&& (ReferenceTable.getCovidSpecifiedProducts().contains(productCode))*/
										&& (ReferenceTable.getCovidProducts().contains(productCode))
										&& (SHAUtils.isDateOfIntimationWithPolicyRange(issuedPeriodStartDate, issuedPeriodEndDate, policyFromDate))
										&& (SHAUtils.isDateOfIntimationWithPolicyRange(issuedPeriodStartDate, issuedPeriodEndDate, currnetDate))){
									SHAUtils.showMessageBox(SHAConstants.COVID_WAITING_PERIOD_15DAYS_ALERT_MSG);

								}
							}	
						}

						if ((ReferenceTable.STAR_CRITICARE_OTHER_BANKS_KEY
								.equals(bean.getNewIntimationDTO().getPolicy()
										.getProduct().getKey()) || !ReferenceTable
										.getGMCProductList().containsKey(
												bean.getNewIntimationDTO().getPolicy()
												.getProduct().getKey()))
												&& bean.getClaimDTO().getClaimType() != null
												&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY
												.equals(bean.getClaimDTO()
														.getClaimType().getId())) {

							if (selected.getId() != null) {

								if (bean.getNewIntimationDTO().getHospitalDto()
										.getRegistedHospitals() != null
										&& bean.getNewIntimationDTO()
										.getHospitalDto()
										.getRegistedHospitals()
										.getHospitalCode() != null) {

									String hospIcdMapFlag = dBCalculationService
											.getHospIcdMappingAvailable(bean
													.getNewIntimationDTO()
													.getHospitalDto()
													.getRegistedHospitals()
													.getHospitalCode(),
													selected.getId());
									bean.setHospIcdMappingAlertFlag(hospIcdMapFlag);

									if (!ReferenceTable.getGMCProductList()
											.containsKey(
													bean.getNewIntimationDTO()
													.getPolicy()
													.getProduct()
													.getKey())
													&& bean.getClaimDTO()
													.getClaimType() != null
													&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY
													.equals(bean.getClaimDTO()
															.getClaimType()
															.getId())
															&& SHAConstants.YES_FLAG.equalsIgnoreCase(bean
																	.getHospIcdMappingAlertFlag())) {

										SHAUtils.showAlertMessageBox(SHAConstants.HOSP_ICD_MAP_ALERT_MSG);
									}
									
								}

								HashMap<String, AbstractField<?>> hashMap = tableItem
										.get(dto);

								DiagnosisComboBox diagCmb = hashMap
										.get("diagnosisName") != null ? (DiagnosisComboBox) hashMap
										.get("diagnosisName") : null;
								SelectValue diagSelect = diagCmb != null ? (SelectValue) diagCmb
										.getValue() : null;

								if (diagSelect != null
										&& diagSelect.getValue() != null
										&& selected != null) {
									String icdCode = selected.getValue();
									String split[] = icdCode.split("-");

									if (split.length > 0) {
										String icdCodeValue = split[split.length - 1]; // bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(0).getIcdCode().getCommonValue();
										bean.setMulticlaimAvailFlag(dBCalculationService
												.getClaimRestrictionAvailable(
														bean.getNewIntimationDTO()
																.getPolicy()
																.getKey(),
														bean.getNewIntimationDTO()
																.getInsuredPatient()
																.getKey(),
														bean.getNewIntimationDTO()
																.getKey(),
														icdCodeValue,
														diagSelect.getValue()
												/*
												 * bean.
												 * getPreauthDataExtractionDetails
												 * (
												 * ).getDiagnosisTableList().get
												 * (
												 * 0).getDiagnosisName().getValue
												 * ()
												 */));
									}
									
								}

							}
						}
						
						if (selected.getId() != null 
								&& selected.getId().equals(SHAConstants.COVID_19_ICD_IDENT_KEY)) {
							if(presenterString.equalsIgnoreCase(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION)){
								fireViewEvent(ClaimRequestWizardPresenter.CLAIM_REQUEST_GENERATE_ADMISSION_TYPE,true);
							}
						}else{
							removeTOAbyvalues();
						}
					} else {
						containerDataSource.setComboBoxValue("");
					}
				} else {
					InsuranceDiagnosisContainer containerDataSource = (InsuranceDiagnosisContainer) diagnosis
							.getContainerDataSource();
					containerDataSource.setComboBoxValue("");
				}
			}
		});
	}

	private void showErrorPopup(ComboBox field, VerticalLayout layout) {
		/*
		 * layout.setMargin(true); layout.setSpacing(true); final ConfirmDialog
		 * dialog = new ConfirmDialog(); dialog.setClosable(true);
		 * dialog.setResizable(false); dialog.setContent(layout);
		 * dialog.setCaption("Error"); dialog.setClosable(true);
		 * field.setValue(null); dialog.show(getUI().getCurrent(), null, true);
		 */

		MessageBox.createError().withCaptionCust("Errors")
		.withTableMessage(layout)
		.withOkButton(ButtonOption.caption("OK")).open();
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
					System.out.println("---the hashmap---" + hashMap);
					ComboBox cmbBox = (ComboBox) hashMap.get("diagnosisName");
					ComboBox comboBox = (ComboBox) hashMap.get("icdBlock");
					if (pedValidationTableDTO != null) {
						if (pedValidationTableDTO.getIcdChapter() != null) {
							if (comboBox != null) {
								addICDBlock(pedValidationTableDTO
										.getIcdChapter().getId(), comboBox,
										pedValidationTableDTO.getIcdBlock());
							}
							if (null != cmbBox) {
								// addDiagnosisValue(pedValidationTableDTO,cmbBox);

							}
						}
					}
				}
			});
		}

	}

	private void addDiagnosisValue(DiagnosisDetailsTableDTO pedValidationDTO,
			ComboBox cmdDiagnosis) {
		SelectValue value = pedValidationDTO.getDiagnosisName();
		Object object = referenceData.get("diagnosisName");
		BeanItemContainer<SelectValue> diagnosis = (BeanItemContainer<SelectValue>) object;
		diagnosis.addBean(value);
		// SelectValue sel = null;
		cmdDiagnosis.setContainerDataSource(diagnosis);
		cmdDiagnosis.setFilteringMode(FilteringMode.STARTSWITH);
		cmdDiagnosis.setTextInputAllowed(true);
		cmdDiagnosis.setNullSelectionAllowed(true);
		cmdDiagnosis.setNewItemsAllowed(true);
		cmdDiagnosis.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmdDiagnosis.setItemCaptionPropertyId("value");

		diagnosis.sort(new Object[] { "value" }, new boolean[] { true });

		if (value != null) {
			// containerDataSource.addItem(value.getId());
			// containerDataSource.getContainerProperty(value.getId(),
			// "value").setValue(value);
			cmdDiagnosis.setValue(value);
			// dummyField.setValue(value.getValue());
		}

	}

	// @SuppressWarnings("unused")
	/*
	 * private void addDiagnosisListener(final ComboBox diagnosisCombo) {
	 * 
	 * if (diagnosisCombo != null) { //String strProValue = (null !=
	 * procedureName.getValue() ? procedureName.getValue().toString() : "");
	 * 
	 * diagnosisCombo.addValueChangeListener(new Property.ValueChangeListener()
	 * {
	 * 
	 * @Override public void valueChange(ValueChangeEvent event) { SelectValue
	 * value = (SelectValue) event.getProperty().getValue(); if(null != value &&
	 * null != value.getValue()) { String strDiagnosisValue = value.getValue();
	 * if(null != procedureList && !procedureList.isEmpty()) { for(ProcedureDTO
	 * procedureObj : procedureList) {
	 * if(strDiagnosisValue.equalsIgnoreCase(procedureObj
	 * .getProcedureNameValue())) { HorizontalLayout layout = new
	 * HorizontalLayout(new Label("Diagnosis and Procedure are same."));
	 * layout.setMargin(true); final ConfirmDialog dialog = new ConfirmDialog();
	 * dialog.setCaption(""); dialog.setClosable(true);
	 * dialog.setContent(layout); dialog.setResizable(false);
	 * dialog.setModal(true); dialog.show(getUI().getCurrent(), null, true); } }
	 * }
	 * 
	 * }
	 * 
	 * } }); }
	 * 
	 * 
	 * }
	 */

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
							/*
							 * if(comboBox != null){ comboBox.setValue(null); }
							 */
							if (comboBox != null) {
								addICDCode(pedValidationTableDTO.getIcdBlock()
										.getId(), comboBox,
										pedValidationTableDTO.getIcdCode());
							}

							// Start of CR R1136
							GComboBox sublimitApplicableCombo = (GComboBox) hashMap
									.get("sublimitApplicable");
							if (sublimitApplicableCombo != null) {
								sublimitApplicableCombo.setEnabled(true);
							}

							TextField sublimitAmt = (TextField) hashMap
									.get("sublimitAmt");
							GComboBox sublimtName = (GComboBox) hashMap
									.get("sublimitName");

							if (sublimitAmt != null) {
								sublimitAmt.setEnabled(true);
								sublimitAmt.setReadOnly(false);
							}
							if (sublimtName != null) {
								sublimtName.setEnabled(true);
								sublimtName.setReadOnly(false);
							}
							// if(sublimitAmt != null && sublimtName != null){
							// setValuesToNull(sublimitAmt, sublimtName);
							// }
							// end of CR R1136
						} else {
							/*
							 * if(comboBox != null){ comboBox.setValue(null); }
							 * pedValidationTableDTO.setIcdCode(null); if
							 * (comboBox != null &&
							 * pedValidationTableDTO.getIcdBlock() != null) {
							 * addICDCode(pedValidationTableDTO.getIcdBlock()
							 * .getId(), comboBox, null); }
							 */
						}

						/**
						 * Start of CR R1136
						 * 
						 */

						/*
						 * GComboBox sublimitApplicableCombo = (GComboBox)
						 * (GComboBox) hashMap .get("sublimitApplicable");
						 * 
						 * BeanItemContainer<SelectValue> applicableContainer =
						 * (BeanItemContainer<SelectValue>) referenceData
						 * .get("sublimitApplicable");
						 * 
						 * if(sublimitApplicableCombo != null){
						 * sublimitApplicableCombo.setEnabled(true);
						 * 
						 * applicableContainer =
						 * (BeanItemContainer<SelectValue>)
						 * sublimitApplicableCombo.getContainerDataSource(); }
						 * 
						 * TextField sublimitAmt = (TextField) hashMap
						 * .get("sublimitAmt"); GComboBox sublimtName =
						 * (GComboBox) hashMap .get("sublimitName");
						 * 
						 * 
						 * if(sublimitAmt != null){
						 * sublimitAmt.setEnabled(true);
						 * sublimitAmt.setReadOnly(false); } if(sublimtName !=
						 * null){ sublimtName.setEnabled(true);
						 * sublimtName.setReadOnly(false); } if(sublimitAmt !=
						 * null && sublimtName != null)
						 * setValuesToNull(sublimitAmt, sublimtName);
						 * 
						 * if(sublimitApplicableCombo != null){ if
						 * (applicableContainer != null &&
						 * applicableContainer.getItemIds() != null &&
						 * applicableContainer
						 * .getItemIds().get(0).getId().equals
						 * (ReferenceTable.COMMONMASTER_NO)) {
						 * 
						 * sublimitApplicableCombo.setValue(applicableContainer.
						 * getItemIds().get(0)); } else{
						 * sublimitApplicableCombo.
						 * setValue(applicableContainer.getItemIds().get(1)); }
						 * sublimitApplicableCombo.setEnabled(true); }
						 * //=======================END of CR R1136
						 */
					}
				}
			});
		}

	}

	@SuppressWarnings("unused")
	private void addSublimitApplicableListener(
			final GComboBox sublimitApplicableCombo) {
		if (sublimitApplicableCombo != null) {
			sublimitApplicableCombo
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					GComboBox component = (GComboBox) event
							.getProperty();
					BeanItemContainer<SelectValue> applicableContainer = (BeanItemContainer<SelectValue>) component
							.getContainerDataSource();
					DiagnosisDetailsTableDTO pedValidationDTO = (DiagnosisDetailsTableDTO) component
							.getData();

					if (component.getValue() != null
							&& (ReferenceTable.COMMONMASTER_YES)
							.equals(((SelectValue) component
									.getValue()).getId())) {

						SHAUtils.showMessageBox(SHAConstants.ALERT_RESTRICTED_SI_BASED_SUBLIMIT, "Alert");
					}	

					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidationDTO);
					System.out.println("--the hashMap----" + hashMap);
					TextField sublimitAmt = (TextField) hashMap
							.get("sublimitAmt");
					GComboBox sublimtName = (GComboBox) hashMap
							.get("sublimitName");

					ComboBox comboBox = (ComboBox) hashMap
							.get("icdCode");
					SelectValue value = (SelectValue) comboBox
							.getValue();
					SublimitFunObject icdSublimitMap = null;
					if (value != null
							&& value.getValue() != null
							&& value.getId() != null
							&& !ReferenceTable.getGMCProductList()
							.containsKey(
									bean.getNewIntimationDTO()
									.getPolicy()
									.getProduct()
									.getKey())) {

						// icdSublimitMap =
								// icdSublimitMapService.getSublimitDetailsBasedOnIcdCode(value.getId());
						pedValidationDTO = getSublimitDetailsByIcdCode(
								value.getId(), pedValidationDTO);
					}

					// if(icdSublimitMap != null){
					// pedValidationDTO.setSublimitMapAvailable(Boolean.TRUE);
					// }
					// else{
					// pedValidationDTO.setSublimitMapAvailable(Boolean.FALSE);
					// }

					if (pedValidationDTO.isSublimitMapAvailable()) {
						if (sublimitAmt != null && sublimtName != null
								&& sublimitApplicableCombo != null) {

							setValues(sublimitAmt, sublimtName,
									pedValidationDTO);

							/*
							 * if (applicableContainer != null &&
							 * applicableContainer.getItemIds() != null
							 * &&
							 * applicableContainer.getItemIds().get(0)
							 * .getId
							 * ().equals(ReferenceTable.COMMONMASTER_YES
							 * )){
							 * component.setValue(applicableContainer
							 * .getItemIds().get(0)); }else{
							 * component.setValue
							 * (applicableContainer.getItemIds
							 * ().get(1)); }
							 */
						}
					} else if (!pedValidationDTO
							.isSublimitMapAvailable()) {
						if (component.getValue() == null
								|| (component.getValue() != null && (ReferenceTable.COMMONMASTER_NO)
								.equals(((SelectValue) component
										.getValue()).getId()))) {

							if (sublimitAmt != null) {
								sublimitAmt.setEnabled(true);
								sublimitAmt.setReadOnly(true);
							}
							if (sublimtName != null) {
								sublimtName.setEnabled(true);
								sublimtName.setReadOnly(false);
							}
							if (sublimitAmt != null
									&& sublimtName != null)
								setValuesToNull(sublimitAmt,
										sublimtName);

							/*
							 * if (applicableContainer != null &&
							 * applicableContainer.getItemIds() != null
							 * &&
							 * applicableContainer.getItemIds().get(0)
							 * .getId
							 * ().equals(ReferenceTable.COMMONMASTER_NO
							 * )) {
							 * 
							 * component.setValue(applicableContainer.
							 * getItemIds().get(0)); } else{
							 * component.setValue
							 * (applicableContainer.getItemIds
							 * ().get(1)); }
							 */
						} else if (component.getValue() != null
								&& (ReferenceTable.COMMONMASTER_YES)
								.equals(((SelectValue) component
										.getValue()).getId())) {

							if (sublimtName != null) {
								sublimtName.setEnabled(true);
								sublimtName.setReadOnly(false);
							}
							if (sublimitAmt != null) {
								sublimitAmt.setEnabled(false);
								sublimitAmt.setReadOnly(true);
							}
							if (sublimitAmt != null
									&& sublimtName != null) {
								setValues(sublimitAmt, sublimtName,
										pedValidationDTO);
								// setValuesToNull(sublimitAmt,
								// sublimtName);
								sublimtName.setEnabled(component
										.isEnabled());
								sublimtName.setReadOnly(false);
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
		sublimtName.setReadOnly(true);
		sublimtName.setEnabled(false);

		sublimitAmt.setReadOnly(false);
		sublimitAmt.setValue("");
		sublimitAmt.setReadOnly(true);
		sublimitAmt.setEnabled(false);
	}

	private void setValues(TextField sublimitAmt, ComboBox sublimtName,
			DiagnosisDetailsTableDTO pedValidationDTO) {

		BeanItemContainer<SublimitFunObject> sublimitContainer = (BeanItemContainer<SublimitFunObject>) sublimtName
				.getContainerDataSource();
		boolean sublimitAvailable = false;
		for (int i = 0; i < sublimitContainer.getItemIds().size(); i++) {

			if (pedValidationDTO.isSublimitMapAvailable()
					&& pedValidationDTO.getSublimitNameKey() != null
					&& (pedValidationDTO.getIcdsublimitMapName())
					.equalsIgnoreCase(sublimitContainer.getItemIds()
							.get(i).getName())) {
				pedValidationDTO.setSublimitName(sublimitContainer.getItemIds()
						.get(i));
				sublimtName.setEnabled(true);
				sublimtName.setReadOnly(false);
				sublimtName.setValue(sublimitContainer.getItemIds().get(i));
				sublimtName.setReadOnly(true);
				sublimtName.setEnabled(false);
				sublimitAmt.setEnabled(true);
				sublimitAmt.setReadOnly(false);
				sublimitAmt.setValue(String.valueOf(sublimitContainer
						.getItemIds().get(i).getAmount()));
				sublimitAmt.setReadOnly(true);
				sublimitAmt.setEnabled(false);
				sublimitAvailable = true;
				break;
			}
		}
		if (pedValidationDTO != null
				&& pedValidationDTO.getOldSublimitName() != null) {
			setValuesToDefault(sublimitAmt, sublimtName, pedValidationDTO);
		} else {
			if (sublimitContainer.getItemIds().size() == 0
					|| !sublimitAvailable) {
				setValuesToNull(sublimitAmt, sublimtName);
			}
		}

		/*
		 * if(sublimitContainer.getItemIds().size()==0 || !sublimitAvailable){
		 * setValuesToNull(sublimitAmt, sublimtName); }
		 */
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
					GComboBox component = (GComboBox) event.getComponent();
					DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) sublimitCombo
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidationTableDTO);
					TextField field = (TextField) hashMap.get("sublimitAmt");
					GComboBox sublimitApplicable = (GComboBox) hashMap
							.get("sublimitApplicable");

					// Adding for test
					/*
					 * ComboBox sublimtName = (ComboBox)
					 * hashMap.get("sublimitName"); if(null == sublimtName) {
					 * 
					 * }
					 */

					SelectValue select = sublimitApplicable != null ? (SelectValue) sublimitApplicable
							.getValue() : null;
							if (select == null
									|| (select != null && ("null")
									.equalsIgnoreCase(select.getValue()))) {
								component.setValue(null);
							}

							if (pedValidationTableDTO != null
									&& component.getValue() != null) {

								BeanItemContainer<SublimitFunObject> sublimitContainer = (BeanItemContainer<SublimitFunObject>) sublimitCombo
										.getContainerDataSource();

								for (int i = 0; i < sublimitContainer.getItemIds()
										.size(); i++) {

									if (pedValidationTableDTO.isSublimitMapAvailable()
											&& pedValidationTableDTO
											.getSublimitNameKey() != null
											&& (pedValidationTableDTO
													.getIcdsublimitMapName())
													.equalsIgnoreCase(sublimitContainer
															.getItemIds().get(i)
															.getName())) {
										pedValidationTableDTO
										.setSublimitName(sublimitContainer
												.getItemIds().get(i));
										sublimitApplicable.setEnabled(false);	
										component.setEnabled(false);
									}
								}

								SublimitFunObject sublimitName = pedValidationTableDTO
										.getSublimitName();

								if (null != bean.getNewIntimationDTO().getIsJioPolicy()
										&& bean.getNewIntimationDTO().getIsJioPolicy()) {
									getJioSublimitAlert(sublimitName);
								}
								
								if(pedValidationTableDTO.getSublimitApplicable() != null && pedValidationTableDTO.getSublimitApplicable().getValue() != null
										&& pedValidationTableDTO.getSublimitApplicable().getValue().equalsIgnoreCase("YES") 
										&& sublimitName != null && sublimitName.getName() !=null && SHAConstants.HOSPICE_CARE.equalsIgnoreCase(sublimitName.getName())){
									alertForCancerHospiceCareSublimit();	
								}

								if (field != null) {
									field.setReadOnly(false);
									field.setValue(sublimitName.getAmount() != null ? sublimitName
											.getAmount().toString() : null);
									field.setReadOnly(true);
								}

								/***
								 * alert added for CR2019253
								 *//*
						SublimitFunObject sublimitNameSelect= (SublimitFunObject) component.getValue();
						if(ReferenceTable.POS_MED_CLASSIC_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
								&& (bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null &&
										bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan().equalsIgnoreCase("G"))
								&& sublimitNameSelect !=null
								&& sublimitNameSelect.getName().equalsIgnoreCase("NEW BORN BABY EXPENSES")
								&& select !=null && select.getId().equals(ReferenceTable.COMMONMASTER_YES)){
									HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
									buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
									HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
											.createInformationBox(SHAConstants.NEW_BORN_BABAY_MESSAGE_MCI_GOLD, buttonsNamewithType);
									Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());
						}*/

								/*
								 * SublimitFunObject sublimitNameSelect =
								 * (SublimitFunObject)component.getValue(); if
								 * (ReferenceTable
								 * .MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean
								 * .getNewIntimationDTO
								 * ().getPolicy().getProduct().getKey()) &&
								 * sublimitNameSelect != null &&
								 * sublimitNameSelect.getName
								 * ().equalsIgnoreCase("New Born Baby Cover")) {
								 * 
								 * final MessageBox showInfo = MessageBox .createInfo()
								 * .withCaptionCust("Information")
								 * .withHtmlMessage(SHAConstants
								 * .NEW_BORN_BABAY_MESSAGE_MCI_GOLD)
								 * .withOkButton(ButtonOption
								 * .caption(ButtonType.OK.name())) .open();
								 * 
								 * Button homeButton =
								 * showInfo.getButton(ButtonType.OK);
								 * homeButton.addClickListener(new ClickListener() {
								 * private static final long serialVersionUID =
								 * 7396240433865727954L;
								 * 
								 * @Override public void buttonClick(ClickEvent event) {
								 * showInfo.close(); Collection<Window> windows =
								 * UI.getCurrent().getWindows(); for (Window window :
								 * windows) { window.close(); } } });
								 * 
								 * }
								 */

							}
							// Adding for test
							else {
								if (field != null) {
									field.setReadOnly(false);
									field.setValue(null);
									field.setReadOnly(true);
								}
								sublimitApplicable.setEnabled(true);
								component.setEnabled(true);
							}

				}
			});
		}
	}

	public void addICDBlock(Long icdChpterComboKey, ComboBox icdBlockCombo,
			SelectValue value) {
		if ("premedicalPreauth".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreMedicalPreauthWizardPresenter.GET_ICD_BLOCK,
					icdChpterComboKey);
		} else if ("premedicalEnhancement"
				.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PremedicalEnhancementWizardPresenter.GET_ICD_BLOCK,
					icdChpterComboKey);
		} else if ("preauthEnhancement".equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreauthEnhancemetWizardPresenter.GET_ICD_BLOCK,
					icdChpterComboKey);
		} else if (SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION
				.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(
					MedicalApprovalDataExtractionPagePresenter.GET_ICD_BLOCK,
					icdChpterComboKey);
		} else if (SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION
				.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(
					ClaimRequestDataExtractionPagePresenter.GET_ICD_BLOCK,
					icdChpterComboKey);

		} else if (SHAConstants.PRE_AUTH.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(PreauthWizardPresenter.GET_ICD_BLOCK,
					icdChpterComboKey);
		} else if (SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION
				.equalsIgnoreCase(this.presenterString)) {
			fireViewEvent(
					PAClaimRequestDataExtractionPagePresenter.GET_ICD_BLOCK,
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

		/*
		 * if ("premedicalPreauth".equalsIgnoreCase(this.presenterString)) {
		 * fireViewEvent(PreMedicalPreauthWizardPresenter.GET_ICD_CODE,
		 * icdBlockKey); } else if ("premedicalEnhancement"
		 * .equalsIgnoreCase(this.presenterString)) {
		 * fireViewEvent(PremedicalEnhancementWizardPresenter.GET_ICD_CODE,
		 * icdBlockKey); } else if
		 * ("preauthEnhancement".equalsIgnoreCase(this.presenterString)) {
		 * fireViewEvent(PreauthEnhancemetWizardPresenter.GET_ICD_CODE,
		 * icdBlockKey); }else if
		 * (SHAConstants.MEDICAL_APPROVAL_DATA_EXTRACTION.
		 * equalsIgnoreCase(this.presenterString)) {
		 * fireViewEvent(MedicalApprovalDataExtractionPagePresenter
		 * .GET_ICD_CODE, icdBlockKey); } else if
		 * (SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION
		 * .equalsIgnoreCase(this.presenterString)) {
		 * fireViewEvent(ClaimRequestDataExtractionPagePresenter.GET_ICD_CODE,
		 * icdBlockKey); } else
		 * if(SHAConstants.PRE_AUTH.equalsIgnoreCase(this.presenterString)){
		 * fireViewEvent(PreauthWizardPresenter.GET_ICD_CODE, icdBlockKey); }
		 * else if
		 * (SHAConstants.PA_MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION
		 * .equalsIgnoreCase(this.presenterString)) {
		 * fireViewEvent(PAClaimRequestDataExtractionPagePresenter.GET_ICD_CODE,
		 * icdBlockKey);
		 * 
		 * } icdCodeCombo.setContainerDataSource(icdCode);
		 * icdCodeCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 * icdCodeCombo.setItemCaptionPropertyId("value");
		 */

		DiagnosisDetailsTableDTO pedValidationdto = (DiagnosisDetailsTableDTO) icdCodeCombo
				.getData();

		if (value != null) {
			icdCodeCombo.select(value);
			icdCodeCombo.setValue(value);

			/**
			 * CR R1136 start
			 */
			SublimitFunObject icdSublimitMap = null;
			if (!ReferenceTable.getGMCProductList().containsKey(
					bean.getNewIntimationDTO().getPolicy().getProduct()
					.getKey())) {
				// SublimitFunObject icdSublimitMap =
				// icdSublimitMapService.getSublimitDetailsBasedOnIcdCode(value.getId());
				pedValidationdto = getSublimitDetailsByIcdCode(value.getId(),
						pedValidationdto);
			}

			// if(icdSublimitMap != null){
			// pedValidationdto.setSublimitMapAvailable(Boolean.TRUE);
			// }
			// else{
			// pedValidationdto.setSublimitMapAvailable(Boolean.FALSE);
			// }

			/**
			 * CR R1136 End
			 */
		}
		/**
		 * start of CR R1136
		 */
		else {
			HashMap<String, AbstractField<?>> hashMap = tableItem
					.get(pedValidationdto);

			GComboBox sublimitApplicable = (GComboBox) hashMap
					.get("sublimitApplicable");

			// if(sublimitApplicable != null && sublimitApplicable.getValue() ==
			// null){
			// sublimitApplicable.setValue(null);
			// }

			TextField sublimitAmt = (TextField) hashMap.get("sublimitAmt");
			GComboBox sublimtName = (GComboBox) hashMap.get("sublimitName");

			if (sublimitApplicable != null
					&& ((SelectValue) sublimitApplicable.getValue()).getId()
					.equals(ReferenceTable.COMMONMASTER_YES)
					&& sublimtName != null && sublimtName.getValue() != null
					&& sublimitAmt != null) {

				setValues(sublimitAmt, sublimtName, pedValidationdto);
			} else {
				if (sublimtName != null && sublimitAmt != null)
					setValuesToNull(sublimitAmt, sublimtName);
			}

		}
		/**
		 * CR R1136 End
		 */
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
		if (itemIds.isEmpty()) {
			itemIds = new ArrayList<DiagnosisDetailsTableDTO>();
		}
		return itemIds;
	}

	public void removeAllItems() {
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
		Boolean isprimaryselected =false;
		int count=0;
		for (DiagnosisDetailsTableDTO bean : itemIds) {

			/*
			 * if(ReferenceTable.getHealthGainProducts().containsKey(this.bean.
			 * getNewIntimationDTO().getPolicy().getProduct().getKey())){
			 * if(bean.getSumInsuredRestriction() == null){ hasError = true;
			 * errorMessages.add("Please Select SI Restrication"); } }
			 */
			if (bean.getSublimitApplicable() != null
					&& bean.getSublimitApplicable().getValue() != null
					&& bean.getSublimitApplicable().getId()
					.equals(ReferenceTable.COMMONMASTER_YES)
					&& bean.getSublimitName() == null) {
				hasError = true;
				errorMessages
				.add("Please Select Sublimit Name in Diagnosis Details");
			} else if (bean.getSublimitApplicable() != null
					&& bean.getSublimitApplicable().getValue() != null
					&& bean.getSublimitApplicable().getId()
					.equals(ReferenceTable.COMMONMASTER_YES)) {
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(bean);
				ComboBox sublimtName = (ComboBox) hashMap.get("sublimitName");
				if (sublimtName != null && sublimtName.getValue() != null) {
					SublimitFunObject sublimtNameValue = (SublimitFunObject) sublimtName
							.getValue();
					if (sublimtNameValue != null
							&& (sublimtNameValue.getName() == null || (sublimtNameValue
									.getName() != null && sublimtNameValue
									.getName().isEmpty()))) {
						hasError = true;
						errorMessages
						.add("Please Select Sublimit Name in Diagnosis Details");
					}
				}
			}

			if (bean.getSublimitName() != null) {
				if (valuesMap.containsKey(bean.getSublimitName().getLimitId())
						&& (bean.getConsiderForPayment() == null || (null != bean
						.getConsiderForPayment() && bean
						.getConsiderForPayment().getId()
						.equals(ReferenceTable.COMMONMASTER_YES)))) {
					validationMap.put(bean.getSublimitName().getLimitId(), bean
							.getSublimitName().getLimitId().toString());
				} else {
					valuesMap.put(bean.getSublimitName().getLimitId(), bean
							.getSublimitName().getLimitId().toString());
				}
			}

			Set<ConstraintViolation<DiagnosisDetailsTableDTO>> validate = validator
					.validate(bean);

			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<DiagnosisDetailsTableDTO> constraintViolation : validate) {

					if (constraintViolation.getRootBean() != null
							&& presenterString
							.equalsIgnoreCase("premedicalPreauth")) {
						DiagnosisDetailsTableDTO rootBean = constraintViolation
								.getRootBean();
						errorMessages.add(constraintViolation.getMessage());
					} else {
						errorMessages.add(constraintViolation.getMessage());
					}

					// 26-Dec Due to STP Process.
					if (constraintViolation.getRootBean() != null
							&& presenterString
							.equalsIgnoreCase("premedicalEnhancement")
							&& constraintViolation.getMessage() != null
							&& constraintViolation
							.getMessage()
							.equalsIgnoreCase(
									"Please Select Insurance Diagnosis")) {
						errorMessages.remove(constraintViolation.getMessage());
					}
				}
			}
			if (bean.getPrimaryDiagnosis() != null && bean.getPrimaryDiagnosis()) {
				isprimaryselected = true;
			}
			count++;
		}

		if (!validationMap.isEmpty()) {
			hasError = true;
			errorMessages
			.add("Diagnosis Table - Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .");
		}
		if(count>0&&!isprimaryselected && !(presenterString.equalsIgnoreCase("premedicalPreauth") || presenterString.equalsIgnoreCase("premedicalEnhancement"))){
			hasError = true;
			errorMessages.add("Selection of Primary in diagnosis is mandatory.");
		}

		return !hasError;
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}

	/*
	 * private Converter<Object, SelectValue> getConverter(final Object object)
	 * { return new Converter<Object, SelectValue>() {
	 * 
	 * @Override public SelectValue convertToModel(Object itemId, Class<?
	 * extends SelectValue> targetType, Locale locale) throws
	 * com.vaadin.v7.data.util.converter.Converter.ConversionException { if
	 * (itemId != null) { IndexedContainer c = (IndexedContainer) object; Object
	 * propertyId = c.getContainerPropertyIds().iterator() .next(); Object name
	 * = c.getItem(itemId).getItemProperty(propertyId) .getValue(); return
	 * (SelectValue) name; } return null; }
	 * 
	 * @Override public Object convertToPresentation(SelectValue value, Class<?
	 * extends Object> targetType, Locale locale) throws
	 * com.vaadin.v7.data.util.converter.Converter.ConversionException { if
	 * (value != null) { IndexedContainer c = (IndexedContainer) object; Object
	 * propertyId = c.getContainerPropertyIds().iterator() .next(); for (Object
	 * itemId : c.getItemIds()) { Object name = c .getContainerProperty(itemId,
	 * propertyId) .getValue(); if (value.equals(name)) { return itemId; } } }
	 * return null; }
	 * 
	 * @Override public Class<SelectValue> getModelType() { // TODO
	 * Auto-generated method stub return SelectValue.class; }
	 * 
	 * @Override public Class<Object> getPresentationType() { // TODO
	 * Auto-generated method stub return Object.class; } }; }
	 */

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
					SelectValue selValue = (SelectValue) name;
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

			/*
			 * public String convertToModel(Object value, Class<? extends
			 * String> targetType, Locale locale) throws
			 * com.vaadin.v7.data.util.converter.Converter.ConversionException {
			 * // TODO Auto-generated method stub return null; }
			 */

			/*
			 * @Override public Object convertToPresentation(String value,
			 * Class<? extends Object> targetType, Locale locale) throws
			 * com.vaadin.v7.data.util.converter.Converter.ConversionException {
			 * // TODO Auto-generated method stub return null; }
			 */
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
					if (dto.getRecTypeFlag() != null
							&& !dto.getRecTypeFlag().toLowerCase()
							.equalsIgnoreCase("c")) {
						deleteButton.setEnabled(true);
					}
					// if (dto.getEnableOrDisable() != null) {
					// deleteButton.setEnabled(dto.getEnableOrDisable());
					// }
				}
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						final Object currentItemId = event.getButton()
								.getData();
						if (table.getItemIds().size() > 1) {
							/*
							 * ConfirmDialog dialog = ConfirmDialog
							 * .show(getUI(), "Confirmation",
							 * "Do you want to Delete ?", "No", "Yes", new
							 * ConfirmDialog.Listener() {
							 * 
							 * public void onClose(ConfirmDialog dialog) { if
							 * (!dialog.isConfirmed()) { // Confirmed to
							 * continue DiagnosisDetailsTableDTO dto =
							 * (DiagnosisDetailsTableDTO)currentItemId;
							 * if(dto.getKey() != null && dto.getDiagnosis() !=
							 * null && dto.getDiagnosis().length() > 0) {
							 * deletedDTO
							 * .add((DiagnosisDetailsTableDTO)currentItemId); }
							 * table.removeItem(currentItemId);
							 * listenerField.setValue("true"); } else { // User
							 * did not confirm } } });
							 */

							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(
									GalaxyButtonTypesEnum.YES.toString(), "Yes");
							buttonsNamewithType.put(
									GalaxyButtonTypesEnum.NO.toString(), "No");
							HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
									.createConfirmationbox(
											"Do you want to Delete ?",
											buttonsNamewithType);
							Button yesButton = messageBoxButtons
									.get(GalaxyButtonTypesEnum.YES.toString());
							Button noButton = messageBoxButtons
									.get(GalaxyButtonTypesEnum.NO.toString());
							yesButton.addClickListener(new ClickListener() {
								private static final long serialVersionUID = 7396240433865727954L;

								@Override
								public void buttonClick(ClickEvent event) {

									// Confirmed to continue
									DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) currentItemId;
									if (dto.getKey() != null
											&& dto.getDiagnosis() != null
											&& dto.getDiagnosis().length() > 0) {
										deletedDTO
										.add((DiagnosisDetailsTableDTO) currentItemId);
									}
									table.removeItem(currentItemId);
									listenerField.setValue("true");

								}
							});
							noButton.addClickListener(new ClickListener() {
								private static final long serialVersionUID = 7396240433865727954L;

								@Override
								public void buttonClick(ClickEvent event) {

								}
							});

						} else {
							/*
							 * HorizontalLayout layout = new HorizontalLayout(
							 * new Label("One Diagnosis is Mandatory."));
							 * layout.setMargin(true); final ConfirmDialog
							 * dialog = new ConfirmDialog();
							 * dialog.setCaption(""); dialog.setClosable(true);
							 * dialog.setContent(layout);
							 * dialog.setResizable(false);
							 * dialog.setModal(true);
							 * dialog.show(getUI().getCurrent(), null, true);
							 */
							HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
							buttonsNamewithType.put(
									GalaxyButtonTypesEnum.OK.toString(), "OK");
							GalaxyAlertBox.createErrorBox(
									"One Diagnosis is Mandatory.",
									buttonsNamewithType);
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

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : tableItem
				.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem
					.get(pedValidationTableDTO);

			GComboBox sublimitCombo = (GComboBox) combos.get("sublimitName");
			GComboBox sublimitApplicableCombo = (GComboBox) combos
					.get("sublimitApplicable");
			addSublimitApplicableValues(sublimitApplicableCombo,
					"sublimitApplicable");
			addSublimtValues(sublimitCombo);
			// List<SublimitFunObject> list = (List<SublimitFunObject>)
			// referenceData
			// .get("sublimitDBDetails");
			// if(list!= null && !list.isEmpty()) {
			// if(sublimitApplicableCombo != null) {
			// SelectValue value = new SelectValue();
			// value.setId(ReferenceTable.COMMONMASTER_YES);
			// sublimitApplicableCombo.setValue(value);
			// pedValidationTableDTO.setSublimitApplicable(value);
			// }
			// if(sublimitCombo != null) {
			// pedValidationTableDTO.setSublimitName(list.get(0));
			// sublimitCombo.setValue(list.get(0));
			// }
			//
			// }
		}
	}

	public void populateSublimitValues(DiagnosisDetailsTableDTO pedValidationDTO) {

		HashMap<String, AbstractField<?>> hashMap = tableItem
				.get(pedValidationDTO);

		System.out.println("--the hashMap----" + hashMap);
		GComboBox sublimitApplicableCombo = (GComboBox) (GComboBox) hashMap
				.get("sublimitApplicable");

		BeanItemContainer<SelectValue> applicableContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("sublimitApplicable");

		if (sublimitApplicableCombo != null) {

			applicableContainer = (BeanItemContainer<SelectValue>) sublimitApplicableCombo
					.getContainerDataSource();
		}

		TextField sublimitAmt = (TextField) hashMap.get("sublimitAmt");
		GComboBox sublimtName = (GComboBox) hashMap.get("sublimitName");

		if (pedValidationDTO.isSublimitMapAvailable()
				&& pedValidationDTO.getSublimitNameKey() != null) {
			if (sublimitAmt != null && sublimtName != null
					&& sublimitApplicableCombo != null) {

				setValues(sublimitAmt, sublimtName, pedValidationDTO);
				sublimitApplicableCombo.setEnabled(true);
				if (applicableContainer != null
						&& applicableContainer.getItemIds() != null
						&& applicableContainer.getItemIds().get(0).getId()
						.equals(ReferenceTable.COMMONMASTER_YES)) {
					sublimitApplicableCombo.setValue(applicableContainer
							.getItemIds().get(0));
				} else {
					sublimitApplicableCombo.setValue(applicableContainer
							.getItemIds().get(1));
				}
				sublimitApplicableCombo.setEnabled(false);
			}
		} else if (!pedValidationDTO.isSublimitMapAvailable()
				|| (sublimtName.getContainerDataSource() != null
				&& sublimtName.getContainerDataSource().getItemIds() != null && sublimtName
				.getContainerDataSource().getItemIds().size() == 0)) {

			if (sublimitAmt != null) {
				sublimitAmt.setEnabled(true);
				sublimitAmt.setReadOnly(false);
			}
			if (sublimtName != null) {
				sublimtName.setEnabled(true);
				sublimtName.setReadOnly(false);

			}
			if (sublimitAmt != null && sublimtName != null) {
				if (pedValidationDTO != null
						&& pedValidationDTO.getOldSublimitName() != null) {
					setValuesToDefault(sublimitAmt, sublimtName,
							pedValidationDTO);
				} else {
					setValuesToNull(sublimitAmt, sublimtName);
				}
			}

			if (sublimitApplicableCombo != null) {
				sublimitApplicableCombo.setEnabled(!pedValidationDTO
						.isSublimitMapAvailable());
			}

			if (sublimitApplicableCombo != null
					&& pedValidationDTO.getOldSublimitName() == null) {
				if (applicableContainer != null
						&& applicableContainer.getItemIds() != null
						&& applicableContainer.getItemIds().get(0).getId()
						.equals(ReferenceTable.COMMONMASTER_NO)) {

					sublimitApplicableCombo.setValue(applicableContainer
							.getItemIds().get(0));
				} else {
					sublimitApplicableCombo.setValue(applicableContainer
							.getItemIds().get(1));
				}
				sublimitApplicableCombo.setEnabled(!pedValidationDTO
						.isSublimitMapAvailable());
			}

		}

		// else if(pedValidationDTO.getSublimitNameKey() == null){
		// if((sublimitApplicableCombo == null || (sublimitApplicableCombo !=
		// null && sublimitApplicableCombo.getValue() == null))
		// ||(sublimitApplicableCombo.getValue() != null
		// && (SelectValue)sublimitApplicableCombo.getValue() != null &&
		// ((SelectValue)sublimitApplicableCombo.getValue()).getId().equals(ReferenceTable.COMMONMASTER_NO))){
		//
		// if(sublimitAmt != null){
		// sublimitAmt.setEnabled(true);
		// sublimitAmt.setReadOnly(false);
		// }
		// if(sublimtName != null){
		// sublimtName.setEnabled(true);
		// sublimtName.setReadOnly(false);
		// }
		// if(sublimitAmt != null && sublimtName != null)
		// setValuesToNull(sublimitAmt, sublimtName);
		//
		// if (applicableContainer != null
		// && applicableContainer.getItemIds() != null
		// &&
		// applicableContainer.getItemIds().get(0).getId().equals(ReferenceTable.COMMONMASTER_NO))
		// {
		//
		// sublimitApplicableCombo.setValue(applicableContainer.getItemIds().get(0));
		// }
		// else{
		// sublimitApplicableCombo.setValue(applicableContainer.getItemIds().get(1));
		// }
		// }
		// }

	}

	public void clearObject() {
		SHAUtils.setClearTableItem(tableItem);
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
		diagnosisPopup = null;
	}

	public void setEnableOrDisableSection(GComboBox box,
			DiagnosisDetailsTableDTO pedValidation) {
		if (null != bean.getNewIntimationDTO().getPolicy().getProduct()
				.getKey()
				&& (ReferenceTable.STAR_CARDIAC_CARE.equals(bean
						.getNewIntimationDTO().getPolicy().getProduct()
						.getKey()))
						|| (ReferenceTable.STAR_CARDIAC_CARE_NEW.equals(bean
								.getNewIntimationDTO().getPolicy().getProduct()
								.getKey()))
								|| (ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM.equals(bean
										.getNewIntimationDTO().getPolicy().getProduct()
										.getKey()))) {

			if (null != bean.getPreauthDataExtractionDetails().getSection()
					&& null != bean.getPreauthDataExtractionDetails()
					.getSection().getId()
					&& (ReferenceTable.POL_SECTION_2.equals(bean
							.getPreauthDataExtractionDetails().getSection()
							.getId()))) {

				SelectValue value = new SelectValue();
				value.setId(ReferenceTable.COMMONMASTER_NO);
				value.setValue(SHAConstants.No);
				pedValidation.setSublimitApplicable(value);
				box.setValue(value);
				box.setEnabled(false);

			}

		}

	}

	private void getJioSublimitAlert(SublimitFunObject sublimitName) {

		if (SHAConstants.JIO_JOINT_AND_KNEE_REPLACEMENT
				.equalsIgnoreCase(sublimitName.getName())) {

			StarCommonUtils.alertMessage(getUI(),
					"Waiting period of 12 months applicable for new member.");
		} else if (SHAConstants.JIO_MATERNITY_NORMAL
				.equalsIgnoreCase(sublimitName.getName())
				|| SHAConstants.JIO_MATERNITY_CEASAREAN
				.equalsIgnoreCase(sublimitName.getName())) {

			StarCommonUtils.alertMessage(getUI(),
					"Waiting of 9 months applicable for new members.");
		}
	}

	public void clearTableItems() {
		if (tableItem != null) {
			tableItem.clear();
		}
	}

	private DiagnosisDetailsTableDTO getSublimitDetailsByIcdCode(
			Long icdCodeKey, DiagnosisDetailsTableDTO pedValidation) {

		SublimitFunObject icdSublimitMap = null;
		if (!ReferenceTable.getGMCProductList().containsKey(
				bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {

			icdSublimitMap = icdSublimitMapService
					.getSublimitDetailsBasedOnIcdCode(icdCodeKey);
		}

		if (icdSublimitMap != null) {

			List<SublimitFunObject> sublimitList = (List<SublimitFunObject>) referenceData
					.get("sublimitDBDetails");

			if (sublimitList != null && !sublimitList.isEmpty()) {

				for (int i = 0; i < sublimitList.size(); i++) {

					if ((icdSublimitMap.getName())
							.equalsIgnoreCase(sublimitList.get(i).getName())) {

						pedValidation.setIcdsublimitMapName(icdSublimitMap
								.getName());
						pedValidation.setSublimitNameKey(icdSublimitMap
								.getKey());
						pedValidation.setSublimitMapAvailable(Boolean.TRUE);
					}
				}
			} else {
				pedValidation.setIcdsublimitMapName("");
				pedValidation.setSublimitNameKey(null);
				pedValidation.setSublimitMapAvailable(Boolean.FALSE);
				pedValidation.setSublimitName(null);
			}
		} else {
			HashMap<String, AbstractField<?>> hashMap = tableItem
					.get(pedValidation);

			GComboBox sublimitApplicableCombo = (GComboBox) (GComboBox) hashMap
					.get("sublimitApplicable");
			pedValidation.setIcdsublimitMapName("");
			pedValidation.setSublimitNameKey(null);
			pedValidation.setSublimitMapAvailable(Boolean.FALSE);
			// pedValidation.setSublimitName(null);
		}

		return pedValidation;
	}

	private void addSublimitForSelectedIcdCode(final GComboBox icdCodeCmb) {

		icdCodeCmb.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				DiagnosisDetailsTableDTO pedValidation = (DiagnosisDetailsTableDTO) ((ComboBox) event
						.getProperty()).getData();

				InsuranceDiagnosisComboBox icdCodeSelected = (InsuranceDiagnosisComboBox) event
						.getProperty();
				if (icdCodeSelected != null
						&& icdCodeSelected.getValue() != null
						&& ((SelectValue) icdCodeSelected.getValue()).getId() != null
						&& ((SelectValue) icdCodeSelected.getValue())
						.getValue() != null) {
					pedValidation = getSublimitDetailsByIcdCode(
							((SelectValue) icdCodeSelected.getValue()).getId(),
							pedValidation);
					icdCodeCmb.setData(pedValidation);
					populateSublimitValues(pedValidation);
				}
				/**
				 * start of CR R1136
				 */
				else {

					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidation);
					GComboBox sublimitApplicable = (GComboBox) hashMap
							.get("sublimitApplicable");

					// if(sublimitApplicable != null && !
					// sublimitApplicable.isEnabled()){
					// sublimitApplicable.setValue(null);
					// }

					TextField sublimitAmt = (TextField) hashMap
							.get("sublimitAmt");
					GComboBox sublimtName = (GComboBox) hashMap
							.get("sublimitName");

					if (sublimitApplicable != null
							&& sublimitApplicable.getValue() != null
							&& ((SelectValue) sublimitApplicable.getValue())
							.getId().equals(
									ReferenceTable.COMMONMASTER_YES)
									&& sublimtName != null
									&& sublimtName.getValue() != null
									&& sublimitAmt != null) {

						setValues(sublimitAmt, sublimtName, pedValidation);
					} else {

						if (sublimitAmt != null && sublimtName != null
								&& !sublimitApplicable.isEnabled()) {
							setValuesToNull(sublimitAmt, sublimtName);
						}
					}

				}
				/**
				 * CR R1136 End
				 */
				/*
				 * else{ HashMap<String, AbstractField<?>> hashMap = tableItem
				 * .get(pedValidation);
				 * 
				 * GComboBox sublimitApplicableCombo = (GComboBox) (GComboBox)
				 * hashMap .get("sublimitApplicable");
				 * 
				 * BeanItemContainer<SelectValue> applicableContainer =
				 * (BeanItemContainer<SelectValue>) referenceData
				 * .get("sublimitApplicable");
				 * 
				 * if(sublimitApplicableCombo != null){
				 * 
				 * applicableContainer =
				 * (BeanItemContainer<SelectValue>)sublimitApplicableCombo
				 * .getContainerDataSource(); }
				 * 
				 * TextField sublimitAmt = (TextField) hashMap
				 * .get("sublimitAmt"); GComboBox sublimtName = (GComboBox)
				 * hashMap .get("sublimitName");
				 * 
				 * 
				 * if(sublimitAmt != null){ sublimitAmt.setEnabled(true);
				 * sublimitAmt.setReadOnly(false); } if(sublimtName != null){
				 * sublimtName.setEnabled(true); sublimtName.setReadOnly(false);
				 * } if(sublimitAmt != null && sublimtName != null)
				 * setValuesToNull(sublimitAmt, sublimtName);
				 * 
				 * if(sublimitApplicableCombo != null){ if (applicableContainer
				 * != null && applicableContainer.getItemIds() != null &&
				 * applicableContainer
				 * .getItemIds().get(0).getId().equals(ReferenceTable
				 * .COMMONMASTER_NO)) {
				 * 
				 * sublimitApplicableCombo.setValue(applicableContainer.getItemIds
				 * ().get(0)); } else{
				 * sublimitApplicableCombo.setValue(applicableContainer
				 * .getItemIds().get(1)); }
				 * sublimitApplicableCombo.setEnabled(true); } }
				 */
			}
		});

	}

	public List<String> checkPedExclusionDetails() {
		List<String> errMsgList = new ArrayList<String>();
		Collection<DiagnosisDetailsTableDTO> itemIds = (Collection<DiagnosisDetailsTableDTO>) table
				.getItemIds();
		Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();
		for (DiagnosisDetailsTableDTO bean : itemIds) {

			if (this.bean.getNewIntimationDTO().getPolicy().getProductType() != null
					&& this.bean.getNewIntimationDTO().getPolicy()
					.getProductType().getKey() != null
					&& this.bean.getNewIntimationDTO().getPolicy()
					.getProductType().getKey().intValue() != 2904
					&& bean.getPedImpactOnDiagnosis() == null) {
				errMsgList
				.add("Please Select Value for PED impact on diagnosis");
			}

			if (bean.getConsiderForPayment() == null
					|| (bean.getConsiderForPayment() != null
					&& bean.getConsiderForPayment().getId() != null && bean
					.getConsiderForPayment().getId().intValue() == 0)) {
				errMsgList.add("Please Select Consider For Payment");
			}		

			if ((this.bean.getNewIntimationDTO().getPolicy().getProductType() != null
					&& (this.bean.getNewIntimationDTO().getPolicy()
							.getProductType().getKey() != null && this.bean
							.getNewIntimationDTO().getPolicy().getProductType()
							.getKey().intValue() != 2904) || (null != this.bean
							.getNewIntimationDTO().getPolicy().getProduct().getCode() && this.bean
							.getNewIntimationDTO()
							.getPolicy()
							.getProduct()
							.getCode()
							.equalsIgnoreCase(ReferenceTable.STAR_CRITICARE_OTHER_BANKS)))
							&& (bean.getConsiderForPayment() != null && ReferenceTable.COMMONMASTER_NO
							.equals(bean.getConsiderForPayment().getId()))) {
				if (bean.getReasonForNotPaying() == null) {
					errMsgList.add("Please Select Reason For Not Paying");
				}
			}
		}
		return errMsgList;
	}

	private void addConsiderForPaymentListener(
			final ComboBox considerForPaymentCombo) {
		if (considerForPaymentCombo != null) {
			considerForPaymentCombo
			.addValueChangeListener(new Property.ValueChangeListener() {

				private static final long serialVersionUID = 1L;
				private TextField sittingsField;

				@Override
				public void valueChange(ValueChangeEvent event) {

					GComboBox component = (GComboBox) event
							.getProperty();
					DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) considerForPaymentCombo
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(pedValidationTableDTO);

					SelectValue considerForPaymentSelect = (SelectValue) component
							.getValue();

					GComboBox notPayingReasonCmb = hashMap
							.get("reasonForNotPaying") != null ? (GComboBox) hashMap
									.get("reasonForNotPaying") : null;
									if (bean.getNewIntimationDTO().getPolicy()
											.getProduct().getCode() != null
											&& bean.getNewIntimationDTO()
											.getPolicy()
											.getProduct()
											.getCode()
											.equalsIgnoreCase(
													SHAConstants.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS)
													&& notPayingReasonCmb != null
													&& component.getValue() != null
													&& considerForPaymentSelect != null
													&& ReferenceTable.COMMONMASTER_NO
													.equals(considerForPaymentSelect
															.getId())) {
										notPayingReasonCmb.setEnabled(true);
										notPayingReasonCmb
										.setValue(pedValidationTableDTO
												.getReasonForNotPaying());
									} else if ((bean.getNewIntimationDTO().getPolicy()
											.getProductType() != null && (bean
													.getNewIntimationDTO().getPolicy()
													.getProductType().getKey() != null
													&& bean.getNewIntimationDTO().getPolicy()
													.getProductType().getKey()
													.intValue() != 2904
													|| (null != bean.getNewIntimationDTO()
													.getPolicy().getProduct().getCode() && bean
													.getNewIntimationDTO()
													.getPolicy()
													.getProduct()
													.getCode()
													.equalsIgnoreCase(
															ReferenceTable.STAR_CRITICARE_OTHER_BANKS))
															|| bean.getNewIntimationDTO()
															.getPolicy()
															.getProduct()
															.getCode()
															.equalsIgnoreCase(
																	SHAConstants.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS) || (bean
																			.getNewIntimationDTO()
																			.getPolicy()
																			.getProduct()
																			.getCode()
																			.equalsIgnoreCase(
																					SHAConstants.GMC_CONTINUITY_PRODUCT_CODE) && bean
																					.getNewIntimationDTO()
																					.getPolicy()
																					.getSectionCode()
																					.equalsIgnoreCase(
																							SHAConstants.GMC_WITH_CONTINUITY_SECTION_CODE))))
																							&& notPayingReasonCmb != null
																							&& component.getValue() != null
																							&& considerForPaymentSelect != null
																							&& ReferenceTable.COMMONMASTER_NO
																							.equals(considerForPaymentSelect
																									.getId())) {

										notPayingReasonCmb.setEnabled(true);
										notPayingReasonCmb
										.setValue(pedValidationTableDTO
												.getReasonForNotPaying());
									} else {
										if (notPayingReasonCmb != null) {
											notPayingReasonCmb.setValue(null);
											notPayingReasonCmb.setEnabled(false);
										}
									}
				}
			});

			DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) considerForPaymentCombo
					.getData();
			if (pedValidationTableDTO.getConsiderForPayment() != null) {
				List<SelectValue> selectedList = (List<SelectValue>) considerForPaymentCombo
						.getContainerDataSource().getItemIds();

				if (selectedList
						.get(0)
						.getId()
						.equals(pedValidationTableDTO.getConsiderForPayment()
								.getId())) {
					considerForPaymentCombo.setValue(selectedList.get(0));
				} else {
					considerForPaymentCombo.setValue(selectedList.get(1));
				}
			}
		}
	}

	private void setValuesToDefault(TextField sublimitAmt,
			ComboBox sublimtName, DiagnosisDetailsTableDTO newPedValidationDTO) {

		BeanItemContainer<SublimitFunObject> sublimitContainer = (BeanItemContainer<SublimitFunObject>) sublimtName
				.getContainerDataSource();
		for (int i = 0; i < sublimitContainer.getItemIds().size(); i++) {

			if (!newPedValidationDTO.isSublimitMapAvailable()
					&& newPedValidationDTO.getSublimitNameKey() == null
					&& (newPedValidationDTO.getOldSublimitName())
					.equals(sublimitContainer.getItemIds().get(i))) {
				newPedValidationDTO.setSublimitName(sublimitContainer
						.getItemIds().get(i));
				sublimtName.setEnabled(true);
				sublimtName.setReadOnly(false);
				sublimtName.setValue(sublimitContainer.getItemIds().get(i));
				sublimitAmt.setEnabled(true);
				sublimitAmt.setReadOnly(false);
				sublimitAmt.setValue(String.valueOf(sublimitContainer
						.getItemIds().get(i).getAmount()));

				break;
			}
		}

	}

	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(1);
		coordinatorValues.add(true);

		return coordinatorValues;
	}

	private void addPrimaryListener(final OptionGroup optionType){

		optionType.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(ValueChangeEvent event) {
				OptionGroup component = (OptionGroup) event.getProperty();
				DiagnosisDetailsTableDTO pedValidationTableDTO = (DiagnosisDetailsTableDTO) component.getData();
				List<DiagnosisDetailsTableDTO> itemIds = (List<DiagnosisDetailsTableDTO>) table.getItemIds();
				if(pedValidationTableDTO.getDiagnosisName() !=null){
					if(itemIds !=null && !itemIds.isEmpty()){
						for(DiagnosisDetailsTableDTO detailsTableDTO:itemIds){
							if(pedValidationTableDTO.getDiagnosisName().toString().equals(detailsTableDTO.getDiagnosisName().toString())){
								detailsTableDTO.setPrimaryDiagnosis(true);
							}		
							else{
								detailsTableDTO.setPrimaryDiagnosis(null);
							}
						}
					}
					refreshTable();
				}else{
					component.select(null);
				}
			}
		});
	}
	
	public void refreshTable(){
		table.refreshRowCache(); 
	}
	
	private void removeTOAbyvalues(){
			if(presenterString.equalsIgnoreCase(SHAConstants.MEDICAL_APPROVAL_CLAIM_REQUEST_DATA_EXTRACTION)){
				fireViewEvent(ClaimRequestWizardPresenter.CLAIM_REQUEST_GENERATE_ADMISSION_TYPE,false);								
			}
	}
	
	@SuppressWarnings("unused")
	private void addPedImpactOnDiagnosisListener(
			final GComboBox PedImpactOnDiagnosisCombo) {
		if (PedImpactOnDiagnosisCombo != null) {
			PedImpactOnDiagnosisCombo
			.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = -4865225814973226596L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					GComboBox component = (GComboBox) event
							.getProperty();
					BeanItemContainer<SelectValue> applicableContainer = (BeanItemContainer<SelectValue>) component
							.getContainerDataSource();
					DiagnosisDetailsTableDTO pedValidationDTO = (DiagnosisDetailsTableDTO) component
							.getData();

					String componentValue = ((SelectValue) component.getValue()).toString();
					
					if (component.getValue() != null
							&& (ReferenceTable.PED_IMPACT_DIAG)
							.equals(componentValue)) {

						HashMap<String, AbstractField<?>> hashMap = tableItem
								.get(pedValidationDTO);

						Boolean primaryDiagnosis =  pedValidationDTO.getPrimaryDiagnosis();

						ComboBox comboBox = (ComboBox) hashMap
								.get("icdCode");
						SelectValue value = (SelectValue) comboBox
								.getValue();
//						String icdCodeValuetemp = value.getCommonValue();
						String split[] = (value.toString()).split("-");
						String icdCodeValue ="";

						if (split.length > 0) {
							 icdCodeValue = split[split.length - 1];
						}
//						icdCodeList[i] = icdCodeValuetemp;
						System.out.println(icdCodeValue);

						if(primaryDiagnosis != null && primaryDiagnosis.equals(Boolean.TRUE) && !icdCodeValue.isEmpty())
						{
							Map<String, String> result = dBCalculationService.getICDBlockAlertFlag(null,icdCodeValue.trim());
							String flag = "N";
							String remarks = "";
							if(result != null){
								if(result.containsKey("blockAlertFlag")){
									flag = result.get("blockAlertFlag"); 
								}
								if(result.containsKey("blockAlertRemark")){
									remarks = result.get("blockAlertRemark"); 
								}
							}

							if(flag.equals("Y"))
							{
								pedValidationDTO.setBlockAlertFlag("Y");
								SHAUtils.showMessageBox(remarks, "Alert");
							}

						}	
					}

				}
			});
		}
	}
	
	
	private void alertForCancerHospiceCareSublimit(){
		StarCommonUtils.alertMessage(getUI(),
				"Waiting period of 12 months from the policy inception is applicable and payable once in a lifetime");
	}
}
