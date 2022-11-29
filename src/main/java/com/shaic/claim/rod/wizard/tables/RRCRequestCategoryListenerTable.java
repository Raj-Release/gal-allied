/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.adviseonped.AdviseOnPEDPresenter;
import com.shaic.claim.cashlessprocess.downsize.wizard.DownSizePreauthWizardPresenter;
import com.shaic.claim.cashlessprocess.downsizeRequest.page.DownsizePreauthRequestWizardPresenter;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.fieldVisitPage.FieldVisitPagePresenter;
import com.shaic.claim.fileUpload.fileUploadPresenter;
import com.shaic.claim.hospitalCommunication.AcknowledgeHospitalPresenter;
import com.shaic.claim.negotiation.NegotiationPreauthRequestPresenter;
import com.shaic.claim.pedquery.PEDQueryPresenter;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApprovePresenter;
import com.shaic.claim.pedrequest.process.PEDRequestDetailsProcessPresenter;
import com.shaic.claim.pedrequest.teamlead.PEDRequestDetailsTeamLeadPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.processRejectionPage.ProcessRejectionPresenter;
import com.shaic.claim.registration.ClaimRegistrationPresenter;
import com.shaic.claim.registration.convertClaimPage.ConvertClaimPagePresenter;
import com.shaic.claim.registration.convertClaimToReimbursement.convertReimbursementPage.ConvertReimbursementPagePresenter;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.billing.wizard.BillingWizardPresenter;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard.MedicalApprovalZonalReviewWizardPresenter;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.rrc.detailsPage.ModifyRRCRequestPresenter;
import com.shaic.claim.reimbursement.rrc.detailsPage.ProcessRRCRequestPresenter;
import com.shaic.claim.reimbursement.rrc.detailsPage.ReviewRRCRequestPresenter;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestWizardPresenter;
import com.shaic.claim.reimbursement.submitSpecialist.SubmitSpecialistAdvisePresenter;
import com.shaic.claim.rod.wizard.pages.AcknowledgeDocReceivedWizardPresenter;
import com.shaic.claim.rod.wizard.pages.BillEntryWizardPresenter;
import com.shaic.claim.rod.wizard.pages.CreateRODWizardPresenter;
import com.shaic.claim.submitSpecialist.SubmitSpecialistPagePresenter;
import com.shaic.claim.withdrawPostProcessWizard.WithdrawPreauthPostProcessWizardPresenter;
import com.shaic.claim.withdrawWizard.WithdrawPreauthWizardPresenter;
import com.shaic.claims.reibursement.addaditionaldocuments.AddAditionalDocumentsPresenter;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.addAdditinalDocument.search.PAAddAdditionalDocumentsWizardPresenter;
import com.shaic.paclaim.billing.processclaimbilling.page.PABillingWizardPresenter;
import com.shaic.paclaim.cashless.downsize.wizard.PADownSizePreauthWizardPresenter;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancemetWizardPresenter;
import com.shaic.paclaim.cashless.fle.wizard.wizardfiles.PAPremedicalEnhancementWizardPresenter;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizardPresenter;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardPresenter;
import com.shaic.paclaim.cashless.processdownsize.wizard.PADownsizePreauthRequestWizardPresenter;
import com.shaic.paclaim.cashless.withdraw.wizard.PAWithdrawPreauthWizardPresenter;
import com.shaic.paclaim.convertClaimToReimb.ConvertPAClaimPagePresenter;
import com.shaic.paclaim.convertClaimToReimbursement.convertReimbursementPage.PAConvertReimbursementPagePresenter;
import com.shaic.paclaim.financial.claimapproval.nonhosiptalpage.PAClaimAprNonHosWizardPresenter;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.PANonHospFinancialWizardPresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.wizardfiles.PAHealthBillingWizardPresenter;
import com.shaic.paclaim.health.reimbursement.financial.wizard.PAHealthFinancialWizardPresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.wizard.PAHealthClaimRequestWizardPresenter;
import com.shaic.paclaim.processRejectionPage.PAProcessRejectionPresenter;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAAcknowledgeDocumentWizardPresenter;
import com.shaic.paclaim.rod.createrod.search.PACreateRODWizardPresenter;
import com.shaic.paclaim.rod.enterbilldetails.search.PAEnterBillDetailsWizardPresenter;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.AcknowledgeInvestigationCompletedPresenter;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigationPresenter;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigationPresenter;
import com.shaic.reimbursement.processi_investigationi_initiated.ProcessInvestigationInitiatedPresenter;
import com.shaic.reimbursement.reassigninvestigation.ReAssignInvestigationPresenter;
import com.shaic.reimbursement.uploadTranslatedDocument.UploadTranslatedDocumentPresenter;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportPresenter;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;

/**
 * @author ntv.vijayar
 *
 */
public class RRCRequestCategoryListenerTable extends ViewComponent {

	private static final long serialVersionUID = 7802397137014194525L;

	private Map<ExtraEmployeeEffortDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ExtraEmployeeEffortDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ExtraEmployeeEffortDTO> container = new BeanItemContainer<ExtraEmployeeEffortDTO>(
			ExtraEmployeeEffortDTO.class);

	private Table table;

	private Button btnAdd;

	private Map<String, Object> referenceData;

	private List<String> errorMessages;

	private static Validator validator;

	public static TextField dateDiffFld = new TextField();

	private Date engagedFromDate;

	private Map dateMap;

	private Map duplicateMap;

	private String presenterString;

	private List<ExtraEmployeeEffortDTO> extraEmployeeList ;

	private BeanItemContainer<SelectValue> subCategoryValues;

	private BeanItemContainer<SelectValue> sourceValues;
	
	@EJB
	private MasterService masterService;

	// private ReceiptOfDocumentsDTO bean;

	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}

	public void init(Boolean isAddBtnRequired) {
		// container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		dateMap = new HashMap<Date, String>();
		duplicateMap = new HashMap<Date, String>();

		extraEmployeeList = new ArrayList<ExtraEmployeeEffortDTO>();

		this.errorMessages = new ArrayList<String>();
		// this.bean = rodDTO;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));

		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		initTable();
		table.setWidth("100%");
		table.setHeight("100px");
		// table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setCaption("Category");
		addListener();
		if (isAddBtnRequired) {
			layout.addComponent(btnLayout);
		}
		layout.addComponent(table);

		setCompositionRoot(layout);
	}

	/**
	 * Date of admission changed during bug fixing activity. Hence to not
	 * disturb existing flow, we have added the parameterized constructor with
	 * extra attribute for date of admission.
	 * */

	/*
	 * public void init(Boolean isAddBtnRequired, Date admissionDate) {
	 * container.removeAllItems(); ValidatorFactory factory =
	 * Validation.buildDefaultValidatorFactory(); validator =
	 * factory.getValidator(); this.errorMessages = new ArrayList<String>();
	 * dateMap = new HashMap<Date,String>(); duplicateMap = new
	 * HashMap<Date,String>();
	 * 
	 * //this.bean = rodDTO; btnAdd = new Button(); btnAdd.setStyleName("link");
	 * btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
	 * 
	 * engagedFromDate = admissionDate;
	 * 
	 * HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
	 * btnLayout.setWidth("100%"); btnLayout.setComponentAlignment(btnAdd,
	 * Alignment.MIDDLE_RIGHT);
	 * 
	 * VerticalLayout layout = new VerticalLayout(); layout.setMargin(true);
	 * if(isAddBtnRequired) { layout.addComponent(btnLayout); }
	 * 
	 * initTable(); table.setWidth("100%");
	 * table.setPageLength(table.getItemIds().size());
	 * 
	 * addListener();
	 * 
	 * layout.addComponent(table);
	 * 
	 * setCompositionRoot(layout); }
	 */

	public void setTableList(final List<ExtraEmployeeEffortDTO> list) {
		table.removeAllItems();
		for (final ExtraEmployeeEffortDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}

	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		// container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		table.setVisibleColumns(new Object[] { "slNo", "category","subCategory","sourceOfIdentification","talkSpokento","talkSpokenDate","talkMobto"});

		table.setColumnHeader("slNo", "S.No");
		table.setColumnHeader("category", "Category");
		table.setColumnHeader("subCategory", "Sub Category");
		table.setColumnHeader("sourceOfIdentification", "Source Of Identification");
		table.setColumnHeader("talkSpokento", "Spoken To (Name)");
		table.setColumnHeader("talkSpokenDate", "Date and Time of Call");
		table.setColumnHeader("talkMobto", "Contact Number of the person");
		table.setColumnWidth("slNo", 50);

		/*
		 * table.setColumnHeader("selectforbillentry", "Select For Bill Entry");
		 * table.setColumnHeader("status", "Status");
		 */
		table.setEditable(true);

		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						// dateDiffFld.setValue(null);
						table.removeItem(currentItemId);
					}
				});
				deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return deleteButton;
			}
		});

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}

	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				// if(container.size()==0){
				BeanItem<ExtraEmployeeEffortDTO> addItem = container
						.addItem(new ExtraEmployeeEffortDTO());
				// }
				// else{
				// btnAdd.setVisible(false);
				// }
				//	 manageListeners();
			}
		});
	}

	public void manageListeners() {

		for (ExtraEmployeeEffortDTO items : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(items);

			final AutocompleteField<EmployeeMasterDTO> employeeMaster = (AutocompleteField<EmployeeMasterDTO>) combos.get("employeeNameDTO");
			setEmployeeName(employeeMaster);
			/*final ComboBox categoryCombo = (ComboBox)combos.get("category");
			final TextField txtPerDay = (TextField) combos.get("perDayAmt");*/

			//addClassificationListener(classificationCombo,categoryCombo);
			//calculateItemValue(txtPerDay);




		}
	}

	/*
	 * public void manageListeners() { //List<PatientCareDTO> patientCareList =
	 * (List<PatientCareDTO>)table.getItemIds(); for (PatientCareDTO patientCare
	 * : tableItem.keySet()) {
	 * 
	 * HashMap<String, AbstractField<?>> combos = tableItem.get(patientCare);
	 * if(null != combos) { DateField fromDate =
	 * (DateField)combos.get("engagedFrom"); DateField toDate =
	 * (DateField)combos.get("engagedTo"); calculateDateDiff(fromDate,
	 * "EngagedFromDate"); if(null != fromDate.getValue())
	 * findDuplicateDate(fromDate); if(null != toDate.getValue())
	 * findDuplicateDate(toDate);
	 * 
	 * }
	 * 
	 * } }
	 */

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8967055486309269929L;

		/*
		 * private static final long serialVersionUID = -2192723245525925990L;
		 */
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			ExtraEmployeeEffortDTO entryDTO = (ExtraEmployeeEffortDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem
				.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} // else {
			tableRow = tableItem.get(entryDTO);
			// }

			if ("slNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("45px");
				// field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setMaxLength(50);
				tableRow.put("slNo", field);
				return field;
			} else if ("category".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				tableRow.put("category", box);
				box.setData(entryDTO);
				final GComboBox  subCategory= (GComboBox) tableRow.get("subCategory");
				addCategoryListener(box,subCategory);
				addCategoryValues(box);
				final TextField txt = (TextField) tableRow.get("slNo");
				generateSlNo(txt);
				return box;
			} else if ("subCategory".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				tableRow.put("subCategory", box);
				box.setData(entryDTO);
				if(entryDTO.getCategory() != null) {
					addsubCategoryValues(entryDTO.getCategory().getId(), box,entryDTO.getSubCategory());
				}
				addsubCategoryListener(box,null);
				return box;
			} else if ("sourceOfIdentification".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("100%");
				tableRow.put("sourceOfIdentification", box);
				box.setData(entryDTO);
				if(entryDTO.getSubCategory() != null){
					addsourceValues(entryDTO.getSubCategory().getId(), box,entryDTO.getSourceOfIdentification());
				}
				return box;
			}else if("talkSpokento".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setMaxLength(75);	
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z .]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("talkSpokento", field);
				if(entryDTO.getCategory() != null && entryDTO.getCategory().getId() !=null 
						&& entryDTO.getCategory().getId().equals(SHAConstants.TALK_TALK_MASTER_ID)) {
					field.setEnabled(true);
				}else{
					field.setEnabled(false);
				}
				return field;
			}else if("talkSpokenDate".equals(propertyId)) {
				PopupDateField calBackDate = new PopupDateField();
				calBackDate.setDateFormat("dd/MM/yyyy hh:mm a");
				calBackDate.setInputPrompt("DD/MM/YYYY HH:MM A");
				calBackDate.setLocale(new Locale("en", "EN"));
				calBackDate.setResolution(PopupDateField.RESOLUTION_HOUR);
				calBackDate.setResolution(PopupDateField.RESOLUTION_MIN);
				calBackDate.setData(entryDTO);
				Date oldcurrentDate = new Date();
				calBackDate.setRangeEnd(oldcurrentDate);
				calBackDate.setWidth("100%");
				tableRow.put("talkSpokenDate", calBackDate);
				if(entryDTO.getCategory() != null && entryDTO.getCategory().getId() !=null 
						&& entryDTO.getCategory().getId().equals(SHAConstants.TALK_TALK_MASTER_ID)) {
					calBackDate.setEnabled(true);
				}else{
					calBackDate.setEnabled(false);
				}
				addpopupDateListener(calBackDate);
				return calBackDate;
			}else if("talkMobto".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				field.setConverter(String.class);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("talkMobto", field);
				if(entryDTO.getCategory() != null && entryDTO.getCategory().getId() !=null 
						&& entryDTO.getCategory().getId().equals(SHAConstants.TALK_TALK_MASTER_ID)) {
					field.setEnabled(true);
				}else{
					field.setEnabled(false);
				}
				return field;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void addCategoryValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> billClassificationContainer = (BeanItemContainer<SelectValue>) referenceData.get(SHAConstants.RRC_CATEGORY_CONTAINER);
		if(billClassificationContainer == null){
			billClassificationContainer =masterService.getshortOrderMasterValueByShortNO(ReferenceTable.RRC_CATEGORY);
		}
		comboBox.setContainerDataSource(billClassificationContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}


	private void setEmployeeName(AutocompleteField<EmployeeMasterDTO> field)
	{
		if(null != field)
		{
			ExtraEmployeeEffortDTO extraEmployeeEffort = (ExtraEmployeeEffortDTO) field.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(extraEmployeeEffort);
			if(null != hashMap)
			{
				AutocompleteField<EmployeeMasterDTO> empMaster = (AutocompleteField<EmployeeMasterDTO>)hashMap.get("employeeNameDTO");
				if(null != empMaster)
				{
					field.setValue(empMaster.getText());
					//field.setText(empMaster.getText());
				}
			}
		}
	}

	private void generateSlNo(TextField txtField)
	{

		Collection<ExtraEmployeeEffortDTO> itemIds = (Collection<ExtraEmployeeEffortDTO>) table.getItemIds();

		int i = 0;
		for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : itemIds) {
			i++;
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(extraEmployeeEffortDTO);
			if(null != hashMap && !hashMap.isEmpty())
			{
				TextField itemNoFld = (TextField)hashMap.get("slNo");
				if(null != itemNoFld)
				{
					itemNoFld.setValue(String.valueOf(i)); 
					itemNoFld.setEnabled(false);
				}
			}
		}

	}

	/*private void populateEmployeeName(AutocompleteField<EmployeeMasterDTO> search) {
		search.setQueryListener(new AutocompleteQueryListener<EmployeeMasterDTO>() {
			@Override
			public void handleUserQuery(
					AutocompleteField<EmployeeMasterDTO> field,
					String query) {
				if (null != query) {
					if (SHAConstants.ZONAL_REVIEW
							.equalsIgnoreCase(presenterString)) {
						fireViewEvent(
								MedicalApprovalZonalReviewWizardPresenter.LOAD_EMPLOYEE_DETAILS,field, query);
					}
					// handleHospitalSearchQuery(field, query);
				} else {
					Notification
							.show("State and City both are mandatory for Hospital Selection, Please Select State and City");
				}
			}
		});
	}*/


	public ValueChangeListener valueChangeListenerForAutoCompleteFeild(final AutocompleteField<EmployeeMasterDTO> autoCompleteFld)
	{

		//if(null != box)
		//{
		ValueChangeListener valChange = new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				//daysAndAmtValidationListener(box);
				//populateEmpName(autoCompleteFld);

			}
		};
		return valChange;
		//}

	}

	/*public List<ExtraEmployeeEffortDTO> getValues()
	{
		List<ExtraEmployeeEffortDTO> employeeDTO = (List<ExtraEmployeeEffortDTO>) table.getItemIds();
		return employeeDTO;
	}*/

	/*private void populateEmpName(final AutocompleteField<EmployeeMasterDTO> autoCompleteField)
	{
			 if(null != autoCompleteField)
			 {		 
				 autoCompleteField.addListener(new Listener() {

					@Override
					public void componentEvent(Event event) {

						String empName =  autoCompleteField.getValue();
						ExtraEmployeeEffortDTO extraEffortDTO = (ExtraEmployeeEffortDTO) autoCompleteField.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(extraEffortDTO);
						if(null != hashMap)
						{
							TextField empNameFld = (TextField) hashMap.get("employeeId"); 
						}
						extraEffortDTO.setEm
						//ExtraEmployeeEffortDTO extraEmpEffortDTO = new ExtraEmployeeEffortDTO(); 
						extraEmployeeList.add(e)


				}
			});
		}
	}*/




	public void valueChangeLisenerForDate(final DateField total,
			final String fldName) {

		if (null != total) {
			total.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					//	calculateDateDiff(total, fldName);
					// findDuplicateDate(total);

				}
			});
		}
	}


	/*
	 * public void calculateDateDifff(DateField totalFld,String fieldName) {
	 * String fldName = fieldName; Boolean hasError = false; String eMsg = "";
	 * PatientCareDTO dto = (PatientCareDTO)totalFld.getData(); HashMap<String,
	 * AbstractField<?>> hashMap = tableItem.get(dto); DateField engagedFrmDate
	 * = (DateField)hashMap.get("engagedFrom"); DateField engagedToDate =
	 * (DateField)hashMap.get("engagedTo");
	 * 
	 * if(null != engagedFrmDate) { if(null != engagedFromDate &&
	 * engagedFrmDate.getValue().before(engagedFromDate)) { hasError = true;
	 * eMsg = "Patient Engaged from date is less than admission date";
	 * engagedFrmDate.setValue(null); }
	 * 
	 * } }
	 */


	public void addFileTypeValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> fileTypeContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("fileType");
		comboBox.setContainerDataSource(fileTypeContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}

	public void addBeanToList(ExtraEmployeeEffortDTO employeeEffortDTO) {
		// container.addBean(uploadDocumentsDTO);
		container.addItem(employeeEffortDTO);

		// data.addItem(pedValidationDTO);
		// manageListeners();
	}


	public List<ExtraEmployeeEffortDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ExtraEmployeeEffortDTO> itemIds = new ArrayList<ExtraEmployeeEffortDTO>();
		if (this.table != null) {
			itemIds = (List<ExtraEmployeeEffortDTO>) this.table.getItemIds();
			for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : itemIds) {
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(extraEmployeeEffortDTO);
				if(null != hashMap)
				{
					TextField txtEmpIdFld = (TextField) hashMap.get("employeeId");
					AutocompleteField<ExtraEmployeeEffortDTO> empName = (AutocompleteField<ExtraEmployeeEffortDTO>) hashMap.get("employeeNameDTO");
					if(null != txtEmpIdFld)
					{
						extraEmployeeEffortDTO.setEmployeeId(txtEmpIdFld.getValue());
					}
					if(null != empName)
					{
						extraEmployeeEffortDTO.setEmployeeName(empName.getText());
					}
				}

			}
		}

		return itemIds;
	}

	public List<String> getErrors() {
		return this.errorMessages;
	}

	public void loadEmployeeMasterData(
			AutocompleteField<EmployeeMasterDTO> field,
			List<EmployeeMasterDTO> employeeDetailsList) {
		if (null != employeeDetailsList && !employeeDetailsList.isEmpty()) {
			for (EmployeeMasterDTO extraEmployeeEffortDTO : employeeDetailsList) {
				field.addSuggestion(extraEmployeeEffortDTO,
						extraEmployeeEffortDTO.getEmployeeName());
			}
		}

	}

	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<ExtraEmployeeEffortDTO> itemIds = (Collection<ExtraEmployeeEffortDTO>) table.getItemIds();
		/*Map<Long, String> valuesMap = new HashMap<Long, String>();
			Map<Long, String> validationMap = new HashMap<Long, String>();*/
		if(null != itemIds && !itemIds.isEmpty())
		{
			for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : itemIds) {
				if(!(null != extraEmployeeEffortDTO.getCategory() && null != extraEmployeeEffortDTO.getCategory().getValue()))
				{
					hasError = true;
					errorMessages.add("Please select category type");
				}
				if(null != extraEmployeeEffortDTO.getCategory() && extraEmployeeEffortDTO.getCategory().getId() !=null 
						&& extraEmployeeEffortDTO.getCategory().getId().equals(SHAConstants.TALK_TALK_MASTER_ID)){
					if(extraEmployeeEffortDTO.getTalkSpokento() == null || extraEmployeeEffortDTO.getTalkSpokento().isEmpty()){
						hasError = true;
						errorMessages.add("Please Enter Spoken To");
					}
					if(extraEmployeeEffortDTO.getTalkSpokenDate() == null){
						hasError = true;
						errorMessages.add("Please Select Date and Time");
					}
					if(extraEmployeeEffortDTO.getTalkMobto() == null || extraEmployeeEffortDTO.getTalkMobto().isEmpty()){
						hasError = true;
						errorMessages.add("Please Enter Contact Number");
					}
					if(extraEmployeeEffortDTO.getTalkMobto() != null && extraEmployeeEffortDTO.getTalkMobto().length() != 10){
						hasError = true;
						errorMessages.add("Please Enter Valid Contact Number");
					}
				}
			}
		}
		else
		{
			hasError = true;
			errorMessages.add("Please enter atleast one category details");
		}
		return !hasError;
	}

	/* public boolean isValid()
		{
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			Collection<ExtraEmployeeEffortDTO> itemIds = (Collection<ExtraEmployeeEffortDTO>) table.getItemIds();
			Map<Long, String> valuesMap = new HashMap<Long, String>();
			Map<Long, String> validationMap = new HashMap<Long, String>();
			if(null != itemIds && !itemIds.isEmpty())
			{
	 *//**
	 * If any validation is imposed later, the same will be implemented in this block.
	 * *//*
				for (QuantumReductionDetailsDTO bean : itemIds) {

				}
			}
			else
			{
				 hasError = true;
				 errorMessages.add("Please enter select atleast one employee details");
			}

				return !hasError;*/


	/*private void autoCompletionListenerForEmployee (final AutocompleteField<ExtraEmployeeEffortDTO> component) {

		 component
			.setSuggestionPickedListener(new AutocompleteSuggestionPickedListener<ExtraEmployeeEffortDTO>() {

				@Override
				public void onSuggestionPicked(ExtraEmployeeEffortDTO suggestion) {
					if(suggestion != null){

						bindBeanToUI();
						handleHospitalSelection(suggestion);
						searchHospitalBtn.setCaption(searchHospitalBtn.getCaption() + " " + (anhHospitalCount == null || anhHospitalCount.intValue() <= 0 ? "" : anhHospitalCount.toString()));					
					}
					else{
						Notification.show("Please Select a Hospital Name or Enter Hospital Details");
						setHospitalFieldsEditable(true);
						bindTempHospitalTypeToUI();
					}
				}
			});
	 }*/

	@SuppressWarnings("unused")
	private void addCategoryListener(final GComboBox categoryCombo,final ComboBox subCategoryCombo) {
		if (categoryCombo != null) {
			categoryCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;
				@Override
				public void componentEvent(Event event) {
					GComboBox component = (GComboBox) event.getComponent();
					populatesubCategoryValues(component);
				}
			});
		}

	}

	private void populatesubCategoryValues(GComboBox component) {
		ExtraEmployeeEffortDTO effortDTO = (ExtraEmployeeEffortDTO) component.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(effortDTO);
		GComboBox subCategory = (GComboBox) hashMap.get("subCategory");
		TextField talkSpokento = (TextField) hashMap.get("talkSpokento");
		PopupDateField talkSpokenDate = (PopupDateField) hashMap.get("talkSpokenDate");
		TextField talkMobto = (TextField) hashMap.get("talkMobto");
		if (effortDTO != null) {
			if(null != subCategory) {
				if(null != effortDTO.getCategory() && effortDTO.getCategory().getId() !=null){
					addsubCategoryValues(effortDTO.getCategory().getId(), subCategory, effortDTO.getSubCategory());
				}				
			}
			if(talkSpokento !=null && talkSpokenDate !=null && talkMobto !=null){
				if(null != effortDTO.getCategory() && effortDTO.getCategory().getId() !=null 
						&& effortDTO.getCategory().getId().equals(SHAConstants.TALK_TALK_MASTER_ID)){
					talkSpokento.setEnabled(true);
					talkSpokenDate.setEnabled(true);
					talkMobto.setEnabled(true);
				}else{
					talkSpokento.setEnabled(false);
					talkSpokenDate.setEnabled(false);
					talkMobto.setEnabled(false);
				}
			}
		}
	}

	public void addsubCategoryValues(Long categoryKey, GComboBox subCategory, SelectValue value) {

		if(presenterString.equals(SHAConstants.PROCESS_RRC_REQUEST)){	
			fireViewEvent(ProcessRRCRequestPresenter.PROCESS_RRC_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}else if(presenterString.equals(SHAConstants.REVIEW_RRC_REQUEST)){
			fireViewEvent(ReviewRRCRequestPresenter.REVIEW_RRC_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}else if(presenterString.equals(SHAConstants.MODIFY_RRC_REQUEST)){
			fireViewEvent(ModifyRRCRequestPresenter.MODIFY_RRC_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}else if(SHAConstants.PROCESS_PRE_MEDICAL.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PreMedicalPreauthWizardPresenter.PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PreauthWizardPresenter.PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PremedicalEnhancementWizardPresenter.PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_ENHANCEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_WITHDRAW_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(WithdrawPreauthWizardPresenter.WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_DOWNSIZE_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(DownSizePreauthWizardPresenter.DOWNSZIE_PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.NEGOTIATION_PREAUTH_REQUEST_SCREEN.equalsIgnoreCase(presenterString)){
			fireViewEvent(NegotiationPreauthRequestPresenter.NEGOTIATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}

		else if(SHAConstants.PROCESS_DOWNSIZE_REQUEST_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(DownsizePreauthRequestWizardPresenter.DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_PED_QUERY.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PEDQueryPresenter.PROCESS_PED_QUERY_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_REJECTION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ProcessRejectionPresenter.PROCESS_REJECTION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_CONVERT_CLAIM.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ConvertClaimPagePresenter.CONVERT_CLAIM_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_CONVERT_CLAIM_SEARCH_BASED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ConvertReimbursementPagePresenter.CONVERT_CLAIM_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.ACKNOWLEDGE_HOSPITAL_COMMUNICATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AcknowledgeHospitalPresenter.ACKNOWLEDGE_HOSPITAL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.FIELD_VISIT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(FieldVisitPagePresenter.FIELD_VISIT_REP_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PED_REQUEST_PROCESS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PEDRequestDetailsProcessPresenter.PROCESS_PED_PROCESS_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PED_REQUEST_APPROVER.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PEDRequestDetailsApprovePresenter.PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PED_TEAM_LEAD.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PROCESS_PED_TL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.ADVISE_ON_PED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AdviseOnPEDPresenter.ADVISE_ON_PED_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_CASHLESS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(SubmitSpecialistPagePresenter.SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_COORDINATOR_REPLY.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(fileUploadPresenter.COORDINATOR_REPLY_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_COORDINATOR_REPLY_REIMBURSEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(UploadTranslatedDocumentPresenter.COORDINATOR_REPLY_FOR_REIMB_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_REIMBURSEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(SubmitSpecialistAdvisePresenter.PROCESS_SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ProcessInvestigationInitiatedPresenter.PROCESS_INVESTIGATION_INITIATED_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.ASSIGN_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AssignInvestigationPresenter.PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.ACKNOWLEDGE_INVESTIGATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AcknowledgeInvestigationCompletedPresenter.PROCESS_ACKNOWLEDGE_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AcknowledgeDocReceivedWizardPresenter.ACK_DOC_REC_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(CreateRODWizardPresenter.CREATE_ROD_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(BillEntryWizardPresenter.BILL_ENTRY_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AddAditionalDocumentsPresenter.ADD_ADDITIONAL_DOC_REC_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}

		else if(SHAConstants.PROCESS_UPLOAD_INVESTIGATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(UploadInvestigationReportPresenter.PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}

		else if(SHAConstants.PROCESS_CLAIM_REGISTRATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ClaimRegistrationPresenter.PROCESS_CLAIM_REGISTRATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.CLAIM_REQUEST.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ClaimRequestWizardPresenter.CLAIM_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.BILLING.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(BillingWizardPresenter.BILLING_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.FINANCIAL.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(FinancialWizardPresenter.FINANCIAL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.INITIATE_RRC_REQUEST.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(InitiateRRCRequestWizardPresenter.SAVE_INITIATE_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.DRAFT_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(DraftInvestigationPresenter.PROCESS_DRAFT_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PROCESS_WITHDRAW_PREAUTH_POST_PROCESS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.WITHDRAW_PREAUTH_POST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PROCESS_REJECTION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAProcessRejectionPresenter.PA_PROCESS_REJECTION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}	
		else if(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAAcknowledgeDocumentWizardPresenter.PA_ACK_DOC_REC_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PACreateRODWizardPresenter.PA_CREATE_ROD_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAEnterBillDetailsWizardPresenter.PA_BILL_ENTRY_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_CLAIM_REQUEST.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAClaimRequestWizardPresenter.PA_CLAIM_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_CLAIM_REQUEST_HOSP.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAHealthClaimRequestWizardPresenter.CLAIM_REQUEST_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_BILLING.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PABillingWizardPresenter.PA_BILLING_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_BILLING_HOSP.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAHealthBillingWizardPresenter.BILLING_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PROCESS_PRE_MEDICAL.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAPreMedicalPreauthWizardPresenter.PA_PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PROCESS_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAPreauthWizardPresenter.PA_PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PRE_MEDICAL_PROCESSING_ENHANCEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAPremedicalEnhancementWizardPresenter.PA_PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PROCESS_ENHANCEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAPreauthEnhancemetWizardPresenter.PA_PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PROCESS_DOWNSIZE_REQUEST_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PADownsizePreauthRequestWizardPresenter.PA_DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PROCESS_DOWNSIZE_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PADownSizePreauthWizardPresenter.PA_DOWNSZIE_PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PROCESS_WITHDRAW_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAWithdrawPreauthWizardPresenter.PA_WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PROCESS_CONVERT_CLAIM.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ConvertPAClaimPagePresenter.CONVERT_PA_CLAIM_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_FINANCIAL_HOSP.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAHealthFinancialWizardPresenter.PA_FINANCIAL_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_FINANCIAL.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PANonHospFinancialWizardPresenter.FINANCIAL_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_CLAIM_APPROVAL.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAClaimAprNonHosWizardPresenter.CLAIM_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}
		else if(SHAConstants.PA_PROCESS_CONVERT_CLAIM_SEARCH_BASED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAConvertReimbursementPagePresenter.PA_CONVERT_CLAIM_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}else if(SHAConstants.PA_ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAAddAdditionalDocumentsWizardPresenter.PA_ADD_ADDITIONAL_DOCUMENTS_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}else if(SHAConstants.RE_ASSIGN_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ReAssignInvestigationPresenter.PROCESS_REASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, categoryKey,subCategory,value);
		}

	}

	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){

		this.subCategoryValues = selectValueContainer;
		subCategory.setContainerDataSource(subCategoryValues);
		subCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		subCategory.setItemCaptionPropertyId("value");
		if(null != value)
		{
			subCategory.setValue(value);
		}
	}
	@SuppressWarnings("unused")
	private void addsubCategoryListener(final GComboBox subCategoryCombo,final ComboBox sourceCombo) {
		if (subCategoryCombo != null) {
			subCategoryCombo.addListener(new Listener() {
				private static final long serialVersionUID = -4865225814973226596L;
				@Override
				public void componentEvent(Event event) {
					GComboBox component = (GComboBox) event.getComponent();
					populatesourceValues(component);
				}
			});
		}

	}
	private void populatesourceValues(GComboBox component) {
		ExtraEmployeeEffortDTO effortDTO = (ExtraEmployeeEffortDTO) component.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(effortDTO);
		GComboBox source = (GComboBox) hashMap.get("sourceOfIdentification");
		if (effortDTO != null) {
			if(null != source && effortDTO.getSubCategory() != null && effortDTO.getSubCategory().getId() !=null) {
				addsourceValues(effortDTO.getSubCategory().getId(), source,effortDTO.getSourceOfIdentification());
			}
		}					
	}
	private void addsourceValues(Long subCategoryKey, GComboBox source, SelectValue value) {
		if(presenterString.equals(SHAConstants.PROCESS_RRC_REQUEST)){	
			fireViewEvent(ProcessRRCRequestPresenter.PROCESS_RRC_SOURCE_VALUES, subCategoryKey,source,value);
		}else if(presenterString.equals(SHAConstants.REVIEW_RRC_REQUEST)){
			fireViewEvent(ReviewRRCRequestPresenter.REVIEW_RRC_SOURCE_VALUES,subCategoryKey,source,value);
		}else if(presenterString.equals(SHAConstants.MODIFY_RRC_REQUEST)){
			fireViewEvent(ModifyRRCRequestPresenter.MODIFY_RRC_SOURCE_VALUES, subCategoryKey,source,value);
		}else if(SHAConstants.PROCESS_PRE_MEDICAL.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PreMedicalPreauthWizardPresenter.PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PreauthWizardPresenter.PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PremedicalEnhancementWizardPresenter.PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_ENHANCEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_WITHDRAW_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(WithdrawPreauthWizardPresenter.WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_DOWNSIZE_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(DownSizePreauthWizardPresenter.DOWNSZIE_PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.NEGOTIATION_PREAUTH_REQUEST_SCREEN.equalsIgnoreCase(presenterString)){
			fireViewEvent(NegotiationPreauthRequestPresenter.NEGOTIATION_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}

		else if(SHAConstants.PROCESS_DOWNSIZE_REQUEST_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(DownsizePreauthRequestWizardPresenter.DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_PED_QUERY.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PEDQueryPresenter.PROCESS_PED_QUERY_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_REJECTION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ProcessRejectionPresenter.PROCESS_REJECTION_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_CONVERT_CLAIM.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ConvertClaimPagePresenter.CONVERT_CLAIM_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_CONVERT_CLAIM_SEARCH_BASED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ConvertReimbursementPagePresenter.CONVERT_CLAIM_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.ACKNOWLEDGE_HOSPITAL_COMMUNICATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AcknowledgeHospitalPresenter.ACKNOWLEDGE_HOSPITAL_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.FIELD_VISIT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(FieldVisitPagePresenter.FIELD_VISIT_REP_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PED_REQUEST_PROCESS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PEDRequestDetailsProcessPresenter.PROCESS_PED_PROCESS_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PED_REQUEST_APPROVER.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PEDRequestDetailsApprovePresenter.PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PED_TEAM_LEAD.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PROCESS_PED_TL_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.ADVISE_ON_PED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AdviseOnPEDPresenter.ADVISE_ON_PED_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_CASHLESS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(SubmitSpecialistPagePresenter.SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_COORDINATOR_REPLY.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(fileUploadPresenter.COORDINATOR_REPLY_LOAD_RRC_REQUEST_SUB_CATEGORY_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_COORDINATOR_REPLY_REIMBURSEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(UploadTranslatedDocumentPresenter.COORDINATOR_REPLY_FOR_REIMB_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_REIMBURSEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(SubmitSpecialistAdvisePresenter.PROCESS_SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ProcessInvestigationInitiatedPresenter.PROCESS_INVESTIGATION_INITIATED_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.ASSIGN_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AssignInvestigationPresenter.PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.ACKNOWLEDGE_INVESTIGATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AcknowledgeInvestigationCompletedPresenter.PROCESS_ACKNOWLEDGE_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AcknowledgeDocReceivedWizardPresenter.ACK_DOC_REC_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(CreateRODWizardPresenter.CREATE_ROD_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(BillEntryWizardPresenter.BILL_ENTRY_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AddAditionalDocumentsPresenter.ADD_ADDITIONAL_DOC_REC_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_UPLOAD_INVESTIGATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(UploadInvestigationReportPresenter.PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_CLAIM_REGISTRATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ClaimRegistrationPresenter.PROCESS_CLAIM_REGISTRATION_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.CLAIM_REQUEST.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ClaimRequestWizardPresenter.CLAIM_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.BILLING.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(BillingWizardPresenter.BILLING_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.FINANCIAL.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(FinancialWizardPresenter.FINANCIAL_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.INITIATE_RRC_REQUEST.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(InitiateRRCRequestWizardPresenter.SAVE_INITIATE_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.DRAFT_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(DraftInvestigationPresenter.PROCESS_DRAFT_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PROCESS_WITHDRAW_PREAUTH_POST_PROCESS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.WITHDRAW_PREAUTH_POST_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PROCESS_REJECTION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAProcessRejectionPresenter.PA_PROCESS_REJECTION_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}	
		else if(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAAcknowledgeDocumentWizardPresenter.PA_ACK_DOC_REC_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PACreateRODWizardPresenter.PA_CREATE_ROD_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAEnterBillDetailsWizardPresenter.PA_BILL_ENTRY_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_CLAIM_REQUEST.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAClaimRequestWizardPresenter.PA_CLAIM_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_CLAIM_REQUEST_HOSP.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAHealthClaimRequestWizardPresenter.CLAIM_REQUEST_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_BILLING.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PABillingWizardPresenter.PA_BILLING_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_BILLING_HOSP.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAHealthBillingWizardPresenter.BILLING_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PROCESS_PRE_MEDICAL.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAPreMedicalPreauthWizardPresenter.PA_PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PROCESS_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAPreauthWizardPresenter.PA_PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PRE_MEDICAL_PROCESSING_ENHANCEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAPremedicalEnhancementWizardPresenter.PA_PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PROCESS_ENHANCEMENT.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAPreauthEnhancemetWizardPresenter.PA_PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PROCESS_DOWNSIZE_REQUEST_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PADownsizePreauthRequestWizardPresenter.PA_DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PROCESS_DOWNSIZE_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PADownSizePreauthWizardPresenter.PA_DOWNSZIE_PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PROCESS_WITHDRAW_PREAUTH.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAWithdrawPreauthWizardPresenter.PA_WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PROCESS_CONVERT_CLAIM.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ConvertPAClaimPagePresenter.CONVERT_PA_CLAIM_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_FINANCIAL_HOSP.equalsIgnoreCase(presenterString)){
			fireViewEvent(PAHealthFinancialWizardPresenter.PA_FINANCIAL_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_FINANCIAL.equalsIgnoreCase(presenterString)){
			fireViewEvent(PANonHospFinancialWizardPresenter.FINANCIAL_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_CLAIM_APPROVAL.equalsIgnoreCase(presenterString)){
			fireViewEvent(PAClaimAprNonHosWizardPresenter.CLAIM_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
		else if(SHAConstants.PA_PROCESS_CONVERT_CLAIM_SEARCH_BASED.equalsIgnoreCase(presenterString)){
			fireViewEvent(PAConvertReimbursementPagePresenter.PA_CONVERT_CLAIM_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}else if(SHAConstants.PA_ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(PAAddAdditionalDocumentsWizardPresenter.PA_ADD_ADDITIONAL_DOCUMENTS_SOURCE_VALUES, subCategoryKey,source,value);
		}else if(SHAConstants.RE_ASSIGN_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ReAssignInvestigationPresenter.PROCESS_REASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_SOURCE_VALUES, subCategoryKey,source,value);
		}
	}
	public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){

		this.sourceValues = selectValueContainer;
		source.setContainerDataSource(sourceValues);
		source.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		source.setItemCaptionPropertyId("value");
		if(null != value)
		{
			source.setValue(value);
		}
	}
	
	public void invalidate(){
		SHAUtils.setCleaEEETableItem(tableItem);
		SHAUtils.setClearReferenceData(referenceData);
		errorMessages = null;
		validator = null;
		presenterString = null;
		extraEmployeeList = null;
		engagedFromDate = null;
		dateMap = null;
		duplicateMap = null;
		masterService = null;
		sourceValues = null;
		subCategoryValues = null;
	}
	
	private void addpopupDateListener(final PopupDateField calBackDate){
		calBackDate.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				//calBackDate.setValue(calBackDate.getValue());
				Date selected = (Date) event.getProperty().getValue();
				Date currentDate = new Date();
				try {
					SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
					if(fmt.format(selected).equals(fmt.format(currentDate))){
						Calendar selectedCal = Calendar.getInstance();
						selectedCal.setTime(selected);
						Calendar currentCal = Calendar.getInstance();
						currentCal.setTime(currentDate);
						if (selectedCal.get(Calendar.AM_PM) == currentCal.get(Calendar.AM_PM)) {
							if(selectedCal.get(Calendar.HOUR_OF_DAY) > currentCal.get(Calendar.HOUR_OF_DAY)){
								event.getProperty().setValue(currentDate);
							}else if(selectedCal.get(Calendar.HOUR_OF_DAY) == currentCal.get(Calendar.HOUR_OF_DAY)
									&& selectedCal.get(Calendar.MINUTE) > currentCal.get(Calendar.MINUTE)){
								event.getProperty().setValue(currentDate);
							}
						}else if(currentCal.get(Calendar.AM_PM) == Calendar.AM && selectedCal.get(Calendar.AM_PM) == Calendar.PM){
							event.getProperty().setValue(currentDate);
						}
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

}

