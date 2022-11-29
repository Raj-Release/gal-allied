/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

/**
 * @author ntv.vijayar
 *
 */

/**
 * 
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections.BidiMap;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.EmployeeComboBox;
import com.shaic.arch.test.EmployeeContainer;
import com.shaic.claim.reimbursement.billing.wizard.BillingWizardPresenter;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard.MedicalApprovalZonalReviewWizardPresenter;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;
import com.zybnet.autocomplete.server.AutocompleteQueryListener;

/**
 * @author ntv.vijayar
 *
 */
public class ExtraEffortEmployeeListenerTable extends ViewComponent {

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
	
	private BeanItemContainer<SelectValue> employeeNameList;
	
	private BeanItemContainer<SelectValue> employeeNamesContainer;
	
	private static final String employeeNameCaption="Employee";
	
	@EJB
	private ReimbursementService reimbursementService;

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
		
		if(extraEmployeeList == null){
			extraEmployeeList = new ArrayList<ExtraEmployeeEffortDTO>();
		}
		
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
		 table.setHeight("200px");
		// table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setCaption("Extra Effort by Employee");
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

	@SuppressWarnings("deprecation")
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		// container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		table.setColumnHeader("slNo", "S.No");
		table.setColumnHeader("selEmployeeId",employeeNameCaption);
		/*table.setColumnHeader("selEmployeeId", "Employee");
		table.setColumnHeader("selEmployeeName", "Employee</br> Name");*/
		if(SHAConstants.PROCESS_RRC_REQUEST.equalsIgnoreCase(this.presenterString) || (SHAConstants.REVIEW_RRC_REQUEST).equalsIgnoreCase(this.presenterString) || (SHAConstants.SEARCH_RRC_REQUEST).equalsIgnoreCase(this.presenterString)
				|| (SHAConstants.VIEW_RRC_REQUEST).equalsIgnoreCase(this.presenterString)
				|| (SHAConstants.MODIFY_RRC_REQUEST).equalsIgnoreCase(this.presenterString))
		{
			//table.setColumnHeader("employeeName", "Employee</br> Name");
			table.setColumnHeader("typeOfContributor", "Type Of <br> Contributor");
			table.setColumnHeader("creditType", "Credit</br> Type");
			table.setColumnHeader("score", "Score");
			table.setColumnHeader("remarks", "Remarks");
			
			table.setVisibleColumns(new Object[] { "slNo", "selEmployeeId",
			"typeOfContributor" ,"creditType","score","remarks"});
		}
		/*else if(SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(this.presenterString)
				|| (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(this.presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(this.presenterString)
				|| (SHAConstants.FINANCIAL).equalsIgnoreCase(this.presenterString))*/
		else
		{
			//table.setColumnHeader("employeeNameDTO", "Employee</br> Name");	
			table.setVisibleColumns(new Object[] { "slNo", "selEmployeeId"});
		}
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
				ExtraEmployeeEffortDTO extraEmployeeEffortDTO = new ExtraEmployeeEffortDTO();
				Integer size = container.size();
				extraEmployeeEffortDTO.setSlNo(size.longValue()+1);
				BeanItem<ExtraEmployeeEffortDTO> addItem = container
						.addItem(extraEmployeeEffortDTO);
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
				field.setWidth("75px");
				// field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setMaxLength(50);
				tableRow.put("slNo", field);
				return field;
			}else if ("selEmployeeId".equals(propertyId)) {
				EmployeeComboBox box = new EmployeeComboBox();
				EmployeeContainer employeeContainer = new EmployeeContainer(reimbursementService);
				box.setWidth("80%");
				box.setContainerDataSource(employeeContainer);
				box.setEnabled(true);
				box.setFilteringMode(FilteringMode.STARTSWITH);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				box.setItemCaptionPropertyId("value");
				box.setNewItemsAllowed(true);
				box.setData(entryDTO);
				tableRow.put("selEmployeeId", box);
				return box;
				
			}/* else if ("selEmployeeName".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("140px");
				box.setData(entryDTO);
				tableRow.put("selEmployeeName", box);
				addEmployeeNameValues(box);
				addEmployeeNameListener(box);
				if(null != entryDTO &&  null != entryDTO.getSelEmployeeName() )
				{
					box.setEnabled(false);
					}
					else
					{
						box.setValue(null);
						box.setEnabled(true);
						
					}
				AutocompleteField<EmployeeMasterDTO> field = new AutocompleteField<EmployeeMasterDTO>();
				//ExtraEmployeeEffortDTO empDTO = (ExtraEmployeeEffortDTO) itemId;
				//field.setText(empDTO.getEmployeeNameDTO());
				field.setWidth("150px");
				field.setEnabled(true);
				field.setData(entryDTO);
				//field.set
				//if(null != entryDTO.getEmployeeNameDTO())
				tableRow.put("employeeNameDTO", field);
			//	populateEmployeeName(field);
				//setEmployeeName(field);
				return box;
			}*/ /*else if ("employeeName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				field.setMaxLength(50);
				tableRow.put("employeeName", field);
				//setEmployeeName(field);
				return field;
			} */ 
			else if ("typeOfContributor".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("140px");
				tableRow.put("typeOfContributor", box);
				addTypeOfContributorValue(box);
				box.setData(entryDTO);	   			   				
				return box;
			}else if ("creditType".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("140px");
				tableRow.put("category", box);
				box.setData(entryDTO);
				addCreditTypeValues(box);
				addCreditTypeListener(box);	
				return box;
			} else if ("score".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				tableRow.put("score", field);
				return field;
			}else if ("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setMaxLength(200);
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				tableRow.put("remarks", field);
				return field;
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

	
	
	public void addEmployeeNameValues(ComboBox comboBox) {
		 employeeNamesContainer = (BeanItemContainer<SelectValue>) referenceData
				.get(SHAConstants.EMPLOYEE_NAME_LIST);
		
		//BeanItemContainer<SelectValue> finalContainer = billClassificationContainer;
		/*for(int i = 0 ; i<billClassificationContainer.size() ; i++)
		 {
			if (("Hospital").equalsIgnoreCase(billClassificationContainer.getIdByIndex(i).getValue()))
			{
				this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
			}
		}*/
		
		comboBox.setContainerDataSource(employeeNamesContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
	}
	
	public void addEmployeeIdList(ComboBox comboBox) {
		BeanItemContainer<SelectValue> employeeNamesContainer = (BeanItemContainer<SelectValue>) referenceData
				.get(SHAConstants.EMPLOYEE_ID);
		
		//BeanItemContainer<SelectValue> finalContainer = billClassificationContainer;
		/*for(int i = 0 ; i<billClassificationContainer.size() ; i++)
		 {
			if (("Hospital").equalsIgnoreCase(billClassificationContainer.getIdByIndex(i).getValue()))
			{
				this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
			}
		}*/
		
		comboBox.setContainerDataSource(employeeNamesContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
	}
	

	 @SuppressWarnings("unused")
		private void addEmployeeNameListener(final ComboBox empNameBox
				) {
			if (empNameBox != null) {
				empNameBox.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						populateEmpId(component);
					}

				});
			}

		}	
	 
	 @SuppressWarnings("unused")
		private void addEmployeeIdListener(final ComboBox empNameBox
				) {
			if (empNameBox != null) {
				empNameBox.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						SelectValue value = (SelectValue)component.getValue();
						if(value != null){
							populateEmployeeId(component,value.getId());
						}
						
					}

				});
			}

		}	 


	 /*@SuppressWarnings("unused")
		private void addEmployeeIdListener(final TextField empIdFld
				) {
			if (null != empIdFld) {
				empIdFld.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						TextField component = (TextField) event.getComponent();
						populateEmpName(component);
					}

				});
			}

		}	
*/	 
	 public BlurListener addEmployeeIdListener( final TextField empIdFld ) {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					if(null != empIdFld)
					{
						TextField component = (TextField) event.getComponent();
						populateEmpName(component);
					}
				}
			};
			return listener;
			
		}
	
	 
	 @SuppressWarnings("unused")
		private void addCreditTypeListener(final ComboBox creditType
				) {
			if (creditType != null) {
				creditType.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						populateScoreValues(component);
					}

				});
			}

		}	
	 
	 
	 private void populateEmpName(TextField component)
	 {
		 BidiMap empNameMap = (BidiMap) referenceData
					.get(SHAConstants.EMPLOYEE_NAME_ID_MAP);
		 	ExtraEmployeeEffortDTO employeeDTO = (ExtraEmployeeEffortDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(employeeDTO);
			ComboBox box = (ComboBox) hashMap.get("selEmployeeName");
			String value = (String)component.getValue();
			if(null != box)
			{
				if(null != value && !("").equalsIgnoreCase(value))
				{
					if(null != empNameMap && !empNameMap.isEmpty())
					{
						String empIdVal = (String)empNameMap.getKey(value);
						//box.setValue(empIdVal);
						setValueForEmployeeNameComboBox(empIdVal,box);
						
					}
				}
			}
	 }
	 
	 private void setValueForEmployeeNameComboBox(String value,ComboBox box)
	 {
		 ExtraEmployeeEffortDTO employeeDTO = (ExtraEmployeeEffortDTO) box.getData();
		 SelectValue selValue = null;
		 for(int i = 0 ; i<employeeNamesContainer.size() ; i++)
		 	{
				if ((employeeNamesContainer.getIdByIndex(i).getValue()).equalsIgnoreCase(value))
				{
					box.setValue(employeeNamesContainer.getIdByIndex(i));
					selValue = new SelectValue();
					selValue.setId(employeeNamesContainer.getIdByIndex(i).getId());
					selValue.setValue(employeeNamesContainer.getIdByIndex(i).getValue());
					employeeDTO.setSelEmployeeName(selValue);
					break;
				}
				else
				{
					box.setValue(null);
				}
			}
	 }
	 
	 private void populateEmpId(ComboBox component)
	 {
		 BidiMap empNameMap = (BidiMap) referenceData
					.get(SHAConstants.EMPLOYEE_NAME_ID_MAP);
		 
		BeanItemContainer<SelectValue> employeeNamesContainer = (BeanItemContainer<SelectValue>) referenceData
					.get(SHAConstants.EMPLOYEE_ID);
		
		List<SelectValue> itemIds = employeeNamesContainer.getItemIds();
		
		 
		 	ExtraEmployeeEffortDTO employeeDTO = (ExtraEmployeeEffortDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(employeeDTO);
			ComboBox empId = (ComboBox) hashMap.get("selEmployeeId");
			
			SelectValue selValue = (SelectValue)component.getValue();
			
			if(selValue != null && selValue.getId() != null){
				 for (SelectValue selectValue : itemIds) {
						if(selValue.getId().equals(selectValue.getId())){
							empId.setValue(selectValue);
							break;
						}
					}
			}else{
				empId.setValue(null);
			}
			
//			if(null != empId)
//			{
//				if(null != selValue && null != selValue.getValue())
//				{
//					if(null != empNameMap && !empNameMap.isEmpty())
//					{
//						String empIdVal = (String)empNameMap.get(selValue.getValue());
//						if(null == empIdVal || ("").equalsIgnoreCase(empIdVal))
//						{
//							empId.setValue(null);
//							
//						}
//						else
//						{
//							empId.setValue(selValue);
//						}
//					}
//				}
//				else
//				{
//					empId.setValue(null);
//				}
//			}
			
	 }
	 
	 private void populateEmployeeId(ComboBox component,Long key)
	 {
		 ExtraEmployeeEffortDTO employeeDTO = (ExtraEmployeeEffortDTO) component.getData();
		 
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(employeeDTO);
		 
		 ComboBox empName = (ComboBox) hashMap.get("selEmployeeName");
		
		 if(empName != null){
		
			List<SelectValue> itemIds = employeeNamesContainer.getItemIds();
			for (SelectValue selectValue : itemIds) {
				if(selectValue.getId().equals(key)){
					empName.setValue(selectValue);
					break;
				}
			}
		 }
		
	 }
	 
	 private void populateScoreValues(ComboBox component)
	 {
		 ExtraEmployeeEffortDTO employeeDTO = (ExtraEmployeeEffortDTO) component.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(employeeDTO);
		TextField score = (TextField) hashMap.get("score");
		SelectValue selValue = (SelectValue)component.getValue();
		if(null != score)
		{
			if(null != selValue && null != selValue.getValue())
			{
				if(("Lapse").equalsIgnoreCase(selValue.getValue()))
						{
							score.setValue("-1");
						}
				//else if(("Not ELigible").equalsIgnoreCase(selValue.getValue()))
				else if(("No Credit").equalsIgnoreCase(selValue.getValue()))
				{
					score.setValue("0");
				}
				else if(("Credit").equalsIgnoreCase(selValue.getValue()))
				{
					score.setValue("1");
				}
			}
		}
		
			
	 }
	
	@SuppressWarnings("unchecked")
	public void addCreditTypeValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> billClassificationContainer = (BeanItemContainer<SelectValue>) referenceData
				.get(SHAConstants.RRC_CREDIT_TYPE_CONTAINER);
		comboBox.setContainerDataSource(billClassificationContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}
	
	@SuppressWarnings("unchecked")
	public void addTypeOfContributorValue(ComboBox comboBox) {
		BeanItemContainer<SelectValue> billClassificationContainer = (BeanItemContainer<SelectValue>) referenceData
				.get(SHAConstants.EMPLOYEE_TYPE_OF_CONTRIBUTOR);
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

	private void populateEmployeeName(
			AutocompleteField<EmployeeMasterDTO> search) {
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
					else if (SHAConstants.CLAIM_REQUEST
							.equalsIgnoreCase(presenterString)) {
						fireViewEvent(
								ClaimRequestWizardPresenter.CLAIM_LOAD_EMPLOYEE_DETAILS,field, query);
					}
					else if (SHAConstants.BILLING
							.equalsIgnoreCase(presenterString)) {
						fireViewEvent(
								BillingWizardPresenter.BILLING_LOAD_EMPLOYEE_DETAILS,field, query);
					}
					else if (SHAConstants.FINANCIAL
							.equalsIgnoreCase(presenterString)) {
						fireViewEvent(
								FinancialWizardPresenter.FINANCIAL_LOAD_EMPLOYEE_DETAILS,field, query);
					}
					// handleHospitalSearchQuery(field, query);
				} else {
					Notification
							.show("State and City both are mandatory for Hospital Selection, Please Select State and City");
				}
			}
		});
	}
	
	
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

	public void addBeanToList(ExtraEmployeeEffortDTO extraEmpDTO) {
		// container.addBean(uploadDocumentsDTO);
		container.addItem(extraEmpDTO);

		// data.addItem(pedValidationDTO);
		// manageListeners();
	}


	public List<ExtraEmployeeEffortDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<ExtraEmployeeEffortDTO> itemIds = new ArrayList<ExtraEmployeeEffortDTO>();
		if (this.table != null) {
			itemIds = (List<ExtraEmployeeEffortDTO>) this.table.getItemIds();
			for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : itemIds) {
				
				if(extraEmployeeEffortDTO.getSelEmployeeId() != null && extraEmployeeEffortDTO.getSelEmployeeId().getValue() != null){
					extraEmployeeEffortDTO.setEmployeeId(extraEmployeeEffortDTO.getSelEmployeeId().getValue());
					
				}
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(extraEmployeeEffortDTO);
				 if(null != hashMap)
				 {
					 ComboBox txtEmpIdFld = (ComboBox) hashMap.get("selEmployeeId");
					 AutocompleteField<ExtraEmployeeEffortDTO> empName = (AutocompleteField<ExtraEmployeeEffortDTO>) hashMap.get("employeeNameDTO");
					 if(null != txtEmpIdFld && txtEmpIdFld.getValue() != null)
					 {
						 SelectValue selected = (SelectValue)txtEmpIdFld.getValue();
//						 extraEmployeeEffortDTO.setEmployeeId(selected.getValue());
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
					
					
					if(SHAConstants.PROCESS_RRC_REQUEST.equalsIgnoreCase(this.presenterString) || (SHAConstants.REVIEW_RRC_REQUEST).equalsIgnoreCase(this.presenterString) || (SHAConstants.SEARCH_RRC_REQUEST).equalsIgnoreCase(this.presenterString)
							|| (SHAConstants.VIEW_RRC_REQUEST).equalsIgnoreCase(this.presenterString))
					{
						if(!(null != extraEmployeeEffortDTO.getCreditType() && null != extraEmployeeEffortDTO.getCreditType().getId()))
						{
							hasError = true;
							 errorMessages.add("Please enter credit type");
						}
						
						
						
						if(null!= extraEmployeeEffortDTO.getScore())
						{
							Long score = extraEmployeeEffortDTO.getScore();
							if( 0 != score && 1 != score && -1 != score)
							{
								if(null == extraEmployeeEffortDTO.getRemarks())
								{
									hasError = true;
									errorMessages.add("Please enter remarks");
								}
							}
						}
					}
					if(!(null != extraEmployeeEffortDTO.getSelEmployeeId() && extraEmployeeEffortDTO.getSelEmployeeId().getValue() != null))
					{
						hasError = true;
						errorMessages.add("Please enter employee Id ");
					}
				}
				/**
				 * If any validation is imposed later, the same will be implemented in this block.
				 * */
				/*for (QuantumReductionDetailsDTO bean : itemIds) {

				}*/
			}
			else
			{
				 hasError = true;
				 errorMessages.add("Please enter atleast one employee details");
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
			employeeNamesContainer = null;
			reimbursementService = null;
	}
}
