/**
 * 
 */
package com.shaic.claim.reimbursement.billing.wizard;

/**
 * @author ntv.vijayar
 *
 */


/**
 * 
 */


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
/*import org.vaadin.dialogs.ConfirmDialog;*/



import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class TreatmentDateListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<PatientCareDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PatientCareDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<PatientCareDTO> container = new BeanItemContainer<PatientCareDTO>(PatientCareDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	
	
	
	
	private List<String> errorMessages;
	
	//private static Validator validator;
	
	public static TextField dateDiffFld = new TextField();
	
	
	//private  Date engagedFromDate ;
	
	private PreauthDTO bean;
	
	
//	private ReceiptOfDocumentsDTO bean;
	

	public void init(Boolean isAddBtnRequired) {
		container.removeAllItems();
		//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		//validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		//this.bean = rodDTO;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("50%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		if(isAddBtnRequired)
		{
			layout.addComponent(btnLayout);
		}
		
		initTable();
		table.setWidth("600px");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	/**
	 * Date of admission changed during bug fixing activity.
	 * Hence to not disturb existing flow, we have added 
	 * the parameterized constructor with extra attribute for
	 * date of admission.
	 * */
	
	public void init(PreauthDTO bean) {
		container.removeAllItems();
		//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		//validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		this.bean = bean;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("50%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("600px");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	public void setTableList(final List<PatientCareDTO> list) {
		table.removeAllItems();
		for (final PatientCareDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
//		table.setWidth("50px");
		table.setPageLength(table.getItemIds().size());
		
		
		
		table.setVisibleColumns(new Object[] { "engagedFrom","engagedTo"});

		table.setColumnHeader("engagedFrom","Treatment From Date");
		table.setColumnHeader("engagedTo", "Treatment To Date");
		table.setColumnWidth("engagedFrom", 200);
		table.setColumnWidth("engagedTo", 200);
		
		/*table.setColumnHeader("selectforbillentry", "Select For Bill Entry");
		table.setColumnHeader("status", "Status");*/
		table.setEditable(true);

		
		table.removeGeneratedColumn("Delete");
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
		
		table.setColumnWidth("Delete", 200);
	
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
	//	table.setEditable(false);
		//table.setFooterVisible(true);

	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				//BeanItem<PatientCareDTO> addItem = container.addItem(new PatientCareDTO());
				container.addItem(new PatientCareDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	
	
	public void manageListeners() {

		/*for (PatientCareDTO patientCare : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(patientCare);

		}*/
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
/**
		 * 
		 */
		private static final long serialVersionUID = -8967055486309269929L;

		/*		private static final long serialVersionUID = -2192723245525925990L;
*/
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			PatientCareDTO entryDTO = (PatientCareDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			
			/*if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(entryDTO);
			}*/
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} //else {
				tableRow = tableItem.get(entryDTO);
			//}
			
			if("engagedFrom".equals(propertyId)) {
				DateField fromDateField = new DateField();
				fromDateField.setDateFormat("dd/MM/yyyy");
				//Vaadin8-setImmediate() fromDateField.setImmediate(true);
				fromDateField.setData(entryDTO);
				tableRow.put("engagedFrom", fromDateField);
				valueChangeLisenerForDate(fromDateField,"EngagedFromDate");
				return fromDateField;
			} 
			else if ("engagedTo".equals(propertyId)) {
				DateField toDateField = new DateField();
				toDateField.setDateFormat("dd/MM/yyyy");
				//Vaadin8-setImmediate() toDateField.setImmediate(true);
				toDateField.setData(entryDTO);
				toDateField.setEnabled(false);
				tableRow.put("engagedTo", toDateField);
//				valueChangeLisenerForDate(toDateField,"EngagedTODate");
				return toDateField;
			}
			else
			{
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	public void valueChangeLisenerForDate(final DateField  total,final String fldName){
		
		if(null != total)
		{
			total.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				private static final long serialVersionUID = 387928077213249928L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					DateField property = (DateField) event.getProperty();
					if(property.getValue() != null) {
						if(calculateDateDiff(total,fldName)) {
							property.setValue(null);
						} else {
							PatientCareDTO dto = (PatientCareDTO) property.getData();
							HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
							DateField toDate = (DateField) hashMap.get("engagedTo");
							if(dto.getEngagedFrom() != null && toDate != null){
								toDate.setValue(dto.getEngagedFrom());
							}
						}
					}
					else{
						PatientCareDTO dto = (PatientCareDTO) property.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
						DateField toDate = (DateField) hashMap.get("engagedTo");
						if(toDate != null && toDate.getValue() != null){
							toDate.setValue(null);
						}
					}
					
					
				}
			});
		}
	}
	
	
	public void setCorrectDate(Date dischargeDate){
		List<PatientCareDTO> itemIds = new ArrayList<PatientCareDTO>();
	    if(this.table != null) {
	    	itemIds = (List<PatientCareDTO>) this.table.getItemIds() ;
	    } 
	    for (PatientCareDTO patientCareDTO : itemIds) {
	    	HashMap<String, AbstractField<?>> hashMap = tableItem.get(patientCareDTO);
	    	DateField fromDate = (DateField) hashMap.get("engagedFrom");
	    	Date fromDateValue = fromDate.getValue();
	    	
	    	if(fromDateValue != null){
		    	if(dischargeDate.after(fromDateValue)){
		    		
		    		fromDate.setValue(null);
		    		
		    	}
	    	}
	    	
		}
		
	}
	
/*	public void calculateDateDifff(DateField totalFld,String fieldName)
	{
		 String fldName = fieldName;
		 Boolean hasError = false;
		 String eMsg = "";
		 PatientCareDTO dto = (PatientCareDTO)totalFld.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
		 DateField engagedFrmDate = (DateField)hashMap.get("engagedFrom");
		 DateField engagedToDate = (DateField)hashMap.get("engagedTo");
		 
		 if(null != engagedFrmDate)
		 {
			 if(null != engagedFromDate && engagedFrmDate.getValue().before(engagedFromDate))
				{
					hasError = true;
					eMsg = "Patient Engaged from date is less than admission date";
					engagedFrmDate.setValue(null);
				}
			 
		 }
	}*/
	
	public void setDischargeDate(Date dischargeDate) {
		this.bean.getPreauthDataExtractionDetails().setDischargeDate(dischargeDate);
	}
	
	 public Boolean calculateDateDiff(DateField totalFld,String fieldName)
	 {
		 @SuppressWarnings("unchecked")
		 List<PatientCareDTO> itemIconPropertyId = (List<PatientCareDTO>) table.getItemIds();
		 Boolean hasError = false;
		 StringBuffer eMsg = new StringBuffer();
		 
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (PatientCareDTO patientCareDTO : itemIconPropertyId) {
				 
				 if(bean.getPreauthDataExtractionDetails().getDischargeDate() == null) {
					 hasError = true;
					 eMsg.append("Please select Discharge Date </br>");
				 }
				 
				 if(bean.getPreauthDataExtractionDetails().getDischargeDate() != null && patientCareDTO.getEngagedFrom() != null) {
					 if(patientCareDTO.getEngagedFrom().before(bean.getPreauthDataExtractionDetails().getDischargeDate()) || patientCareDTO.getEngagedFrom().equals(bean.getPreauthDataExtractionDetails().getDischargeDate())) {
						 hasError = true;
						 eMsg.append("Treatment From Date should be after the date of Discharge Date.");
					 }
				 }
			
				if(hasError) 
				{
					/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
					label.setStyleName("errMessage");
					VerticalLayout layout = new VerticalLayout();
					layout.setMargin(true);
					layout.addComponent(label);

					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(true);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);*/
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
				} 
			 }
		 }
		 return hasError;
	 }
	
	 public Boolean isValid() {
		 List<PatientCareDTO> dtoList = (List<PatientCareDTO>) table.getItemIds();
		 Boolean hasError = false;
		// String eMsg = "";
		 errorMessages = new ArrayList<String>();
		 if(dtoList != null) {
			 if(dtoList.isEmpty()) {
				 hasError = true;
				 errorMessages.add("Please Enter Atleast one Treament Details to Proceed further.");
				 
			 } else {
				 for (PatientCareDTO patientCareDTO : dtoList) {
					 if(patientCareDTO.getEngagedFrom() == null || patientCareDTO.getEngagedTo() == null) {
						 hasError = true;
						 errorMessages.add("Please Enter Treatement date.");
					 } else {
						 if(bean.getPreauthDataExtractionDetails().getDischargeDate() != null && patientCareDTO.getEngagedFrom() != null) {
							 if(patientCareDTO.getEngagedFrom().before(bean.getPreauthDataExtractionDetails().getDischargeDate()) || patientCareDTO.getEngagedFrom().equals(bean.getPreauthDataExtractionDetails().getDischargeDate())) {
								 hasError = true;
								 //eMsg += "Treatment From Date should be after the date of Discharge Date.";
							 }
						 }
					 }
				}
			 }
			 
			 
		 }
		 return !hasError;
	 }
	 
	 public List<String> getErrors() {
		 return errorMessages;
	 }
	
	public void addFileTypeValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> fileTypeContainer = (BeanItemContainer<SelectValue>) referenceData
				.get("fileType");
		comboBox.setContainerDataSource(fileTypeContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");

	}
	
	 public void addBeanToList(PatientCareDTO patientCareDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 container.addItem(patientCareDTO);

//	    	data.addItem(pedValidationDTO);
	    	//manageListeners();
	    }
	
	
	 public List<PatientCareDTO> getValues() {
	    	@SuppressWarnings("unchecked")
	    	List<PatientCareDTO> itemIds = new ArrayList<PatientCareDTO>();
	    	if(this.table != null) {
	    		itemIds = (List<PatientCareDTO>) this.table.getItemIds() ;
	    	} 
			
	    	return itemIds;
	    }
	 
	 

}

