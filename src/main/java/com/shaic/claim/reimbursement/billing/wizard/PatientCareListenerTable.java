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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
/*import org.vaadin.dialogs.ConfirmDialog;*/



import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItem;
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
public class PatientCareListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<PatientCareDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PatientCareDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<PatientCareDTO> container = new BeanItemContainer<PatientCareDTO>(PatientCareDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
//	public static TextField dateDiffFld = new TextField();
	public  TextField dateDiffFld = new TextField();
	
	
	private  Date engagedFromDate ;
	
	private Date dischargedDate ;
	
	private Map dateMap ;
	
	private Map duplicateMap;
	
	private Date dischargeDate;
	
	
	private Date dateOfDischarge;
	
//	private ReceiptOfDocumentsDTO bean;
	

	public void init(Boolean isAddBtnRequired) {
		//container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		dateMap = new HashMap<Date,String>();
		duplicateMap = new HashMap<Date,String>();
		this.errorMessages = new ArrayList<String>();
		//this.bean = rodDTO;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		if(isAddBtnRequired)
		{
			layout.addComponent(btnLayout);
		}
		
		initTable();
		table.removeAllItems();
		table.setWidth("100%");
		table.setHeight("100%");
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
	
	public void init(Boolean isAddBtnRequired, Date admissionDate, Date dischargeDate) {
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		dateMap = new HashMap<Date,String>();
		duplicateMap = new HashMap<Date,String>();

		//this.bean = rodDTO;
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		engagedFromDate = admissionDate;
		this.dateOfDischarge = dischargeDate;

		
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		if(isAddBtnRequired)
		{
			layout.addComponent(btnLayout);
		}
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	public void setTableList(final List<UploadDocumentDTO> list) {
		table.removeAllItems();
		for (final UploadDocumentDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		//container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		
		
		table.setVisibleColumns(new Object[] { "engagedFrom","engagedTo"});

		table.setColumnHeader("engagedFrom","Patient Care engaged from");
		table.setColumnHeader("engagedTo", "Patient Care engaged to");
		
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
			        	dateDiffFld.setValue(null);
						table.removeItem(currentItemId);
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });		
	
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		//manageListeners();
	//	table.setEditable(false);
		//table.setFooterVisible(true);

	}
	
	public void removeAllItems()
	{
		table.removeAllItems();
		//table.removeItem(\);
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
				PatientCareDTO patientCareDTO = new PatientCareDTO();
				try{
//				patientCareDTO.setKey(new Long(container.size()+1));
				}catch(Exception e){
					e.printStackTrace();
				}
				
				BeanItem<PatientCareDTO> addItem = container.addItem(patientCareDTO);
				table.setPageLength(2);
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
			//	manageListeners();
			}
		});
	}
	
	public void setDischargeDate(Date dischargeDate){
		this.dischargeDate = dischargeDate;
	}
	
	
	
/*	public void manageListeners() {
		//List<PatientCareDTO> patientCareList = (List<PatientCareDTO>)table.getItemIds();
		for (PatientCareDTO patientCare : tableItem.keySet()) {
			
			HashMap<String, AbstractField<?>> combos = tableItem.get(patientCare);
			if(null != combos)
			{
				DateField fromDate = (DateField)combos.get("engagedFrom");
				DateField toDate = (DateField)combos.get("engagedTo");
				calculateDateDiff(fromDate, "EngagedFromDate");
				if(null != fromDate.getValue())
				findDuplicateDate(fromDate);
				if(null != toDate.getValue())
				findDuplicateDate(toDate);

			}

		}
	}*/
	
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
				tableRow.put("engagedTo", toDateField);
				valueChangeLisenerForDate(toDateField,"EngagedTODate");
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
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					
					
					if(dischargeDate != null){
						Date enteredDate = (Date) event.getProperty().getValue();
						if(enteredDate != null && enteredDate.before(dischargeDate)){
							
							getErrorMessage("Engaged from date should be greater than discharge date");
							total.setValue(null);
						}else if(enteredDate != null){
							calculateDateDiff(total, fldName);
						}
					}else{
						calculateDateDiff(total,fldName);
					}
					//findDuplicateDate(total);
					
				}
			});
		}
	}
	
/*	private void findDuplicateDate(DateField date)
	{
		 List<PatientCareDTO> itemIconPropertyId = (List<PatientCareDTO>) table.getItemIds();
		 Date dateValue = date.getValue();
		 Boolean hasError = false;
		 String eMsg = "";
		 //if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			// for (PatientCareDTO patientCareDTO : itemIconPropertyId) {
				 
				 if(null != duplicateMap)
				 {
					 
					 if(duplicateMap.containsKey(dateValue))
					 {
						 hasError = true;
						 eMsg += "The entered  date is already existing . Please enter a valid date";
						// break;
					 }
					 else
					 {
						 if(null != dateValue)
							 duplicateMap.put(dateValue,dateValue.toString());
					 }
					 if(dateMap.containsKey(patientCareDTO.getEngagedFrom()))
					 {
						 hasError = true;
						 eMsg += "The entered from date is already existing in previous row . Please enter a valid date";
						 break;
					 }
					 else
					 {
						 dateMap.put(patientCareDTO.getEngagedFrom(),patientCareDTO.getEngagedFrom().toString());
					 }
					 
					 if(dateMap.containsKey(patientCareDTO.getEngagedTo()))
					 {
						 hasError = true;
						 eMsg += "The entered to date is already existing in previous row . Please enter a valid date";
					 }
					 else
					 {
						 dateMap.put(patientCareDTO.getEngagedTo(),patientCareDTO.getEngagedTo().toString());
						 break;
					 }
				 }
				 else
				 {
					 if(null != patientCareDTO.getEngagedFrom())
					 dateMap.put(patientCareDTO.getEngagedFrom(),patientCareDTO.getEngagedFrom().toString());
					 if(null != patientCareDTO.getEngagedTo() )
					 dateMap.put(patientCareDTO.getEngagedTo(),patientCareDTO.getEngagedTo().toString());
				 }
			 }*/
		// }
		/* if(hasError) 
			{
				Label label = new Label(eMsg, ContentMode.HTML);
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
				dialog.show(getUI().getCurrent(), null, true);
			}
		 
	}*/
	
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
	
	 public void calculateDateDiff(DateField totalFld,String fieldName)
	 {
		 String fldName = fieldName;
		 PatientCareDTO dto = (PatientCareDTO) totalFld.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
		 DateField engagedTODate = (DateField) hashMap.get("engagedTo");
		 DateField engagedFrmDate = (DateField) hashMap.get("engagedFrom");
		 List<PatientCareDTO> itemIconPropertyId = (List<PatientCareDTO>) table.getItemIds();
		
		 //Double total  = 0d;
		 Boolean hasError = false;
		 StringBuffer eMsg = new StringBuffer();
		 Long totalDiff = 0l;
		 /**
		  * Added for date validation functionality.
		  * */
		 
		 /*PatientCareDTO dto = (PatientCareDTO)totalFld.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
		 DateField engagedFrmDate = (DateField)hashMap.get("engagedFrom");
		 DateField engagedToDate = (DateField)hashMap.get("engagedTo");*/
		 
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (PatientCareDTO patientCareDTO : itemIconPropertyId) {
					/*HashMap<String, AbstractField<?>> hashMap = tableItem.get(itemIconPropertyId);
					DateField engagedFrmDate = (DateField)hashMap.get("engagedFrom");
					DateField engagedToDate = (DateField)hashMap.get("engagedTo");*/
				if(null != patientCareDTO.getEngagedFrom() )
				{
					//if(engagedFrmDate)
					if(null != this.engagedFromDate && patientCareDTO.getEngagedFrom().before(this.engagedFromDate))
					{
						hasError = true;
						eMsg.append("Patient Engaged from date is less than admission date </br>");
						totalFld.setValue(null);
						//engagedFrmDate.setValue(null);
					}
					if(null != this.engagedFromDate && null != this.dateOfDischarge && null != patientCareDTO && null != patientCareDTO.getEngagedFrom() &&  patientCareDTO.getEngagedFrom().before(this.dateOfDischarge))
					{
						hasError = true;
						eMsg.append("Patient Engaged from date should not be prior to discharge date </br>");
						totalFld.setValue(null);
						//engagedFrmDate.setValue(null);
					}
					else
					{
						if(null != patientCareDTO.getEngagedTo())
						{
							Long dateDiff = 0l;
							//Integer dateDiff = patientCareDTO.getEngagedTo().compareTo(patientCareDTO.getEngagedFrom());
							if(patientCareDTO.getEngagedFrom().after(patientCareDTO.getEngagedTo()))
							{
								hasError = true;
								eMsg.append("Patient care engaged to date cannot be lesser than engaged from date </br>");
								totalFld.setValue(null);
								dateDiffFld.setValue(null);
								dateDiff = null;
								
								 //dateDiff = SHAUtils.getDaysBetweenDate(patientCareDTO.getEngagedTo(), patientCareDTO.getEngagedFrom());
							}
							else
							{
								 dateDiff = SHAUtils.getDaysBetweenDate(patientCareDTO.getEngagedFrom(), patientCareDTO.getEngagedTo());
							}
							if(null != dateDiff)
							{
								totalDiff += dateDiff;
								dateDiffFld.setValue(String.valueOf((totalDiff-1)+1));
							}
						}
						else if(null == patientCareDTO.getEngagedTo() && ("EngagedTODate").equalsIgnoreCase(fldName))
						{
							if(null != engagedFrmDate )
							{
								engagedFrmDate.setValue(null);
							}
							totalFld.setValue(null);
						/*	engagedFrmDate.setValue(null);
							engagedToDate.setValue(null);*/
							dateDiffFld.setValue(null);
						}
						else if (("EngagedTODate").equalsIgnoreCase(fldName))
						{
							totalFld.setValue(null);
							//engagedToDate.setValue(null);
							dateDiffFld.setValue(null);
						}
					}
				}
			
				else if(null == patientCareDTO.getEngagedFrom() && null != patientCareDTO.getEngagedTo())
				{
					if(null != engagedTODate )
					{
						engagedTODate.setValue(null);
					}
					totalFld.setValue(null);
				/*	engagedFrmDate.setValue(null);
					engagedToDate.setValue(null);*/
					dateDiffFld.setValue(null);
				}
				
				
				
				/*else if(null == patientCareDTO.getEngagedTo() && null != patientCareDTO.getEngagedFrom())
				{
					if(null == patientCareDTO.getEngagedTo())
					{
						totalFld.setValue(null);
						dateDiffFld.setValue(null);
					}
				}
*/
				//The below else is not required. 
				/*else
				{
					totalFld.setValue(null);
					dateDiffFld.setValue(null);
					hasError = true;
					eMsg = "Please Enter Engaged Date From";
				}*/
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
		// table.setColumnFooter("billValue", String.valueOf(total));
		// totalClaimedValue = total;
		 }
	 }

	private Boolean findDublicateDate(List<PatientCareDTO> itemIconPropertyId) {
		Boolean dublicateDate = false;
         for (PatientCareDTO patientCareDTO : itemIconPropertyId) {
			 
			 if(patientCareDTO.getKey() != null){
				 for(int i=0; i<itemIconPropertyId.size();i++){
					 if(!patientCareDTO.getKey().equals(itemIconPropertyId.get(i).getKey())){
						 Date engagedFrom = itemIconPropertyId.get(i).getEngagedFrom();
						 Date engagedTo = itemIconPropertyId.get(i).getEngagedTo();
						 if(engagedFrom != null && patientCareDTO.getEngagedFrom() != null){
							 if(patientCareDTO.getEngagedFrom().compareTo(engagedFrom) ==0){
								 dublicateDate = true;
								 break;
							 }
						 }
						 
						 if(engagedTo != null && patientCareDTO.getEngagedFrom() != null){
							 if(patientCareDTO.getEngagedFrom().compareTo(engagedTo) ==0){
								 dublicateDate = true;
								 break;
							 }
						 }
						 
						 if(engagedFrom != null && patientCareDTO.getEngagedTo() != null){
							 if(patientCareDTO.getEngagedTo().compareTo(engagedFrom) ==0){
								 dublicateDate = true;
								 break;
							 }
						 }
						 
						 if(engagedTo != null && patientCareDTO.getEngagedTo() != null){
							 if(patientCareDTO.getEngagedTo().compareTo(engagedTo) ==0){
								 dublicateDate = true;
								 break;
							 }
						 }
					 }else{
						 
						 Date engagedFrom = patientCareDTO.getEngagedFrom();
						 Date engagedTo = patientCareDTO.getEngagedTo();
						 if(engagedFrom != null && engagedTo != null){
							 if(engagedFrom.compareTo(engagedTo)==0){
								 dublicateDate = true;
								 break;
							 }
						 }
						 
					 }
				 }
			 }
				
		 }
         
         return dublicateDate;
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
	
	 
	 public boolean isValid()
		{
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			List<PatientCareDTO> itemIds = (List<PatientCareDTO>) table.getItemIds();
			/*Map<Long, String> valuesMap = new HashMap<Long, String>();
			Map<Long, String> validationMap = new HashMap<Long, String>();*/
 			if(null != itemIds && !itemIds.isEmpty())
			{
			for (PatientCareDTO bean : itemIds) {
				
				if(bean.getEngagedFrom() == null){
					
					hasError = true;
 					errorMessages.add("Please Enter Engaged From Date");
 					break;
					
				}
				/*else if(bean.getEngagedFrom().before(engagedFromDate))
				{
					hasError = true;
 					errorMessages.add("Engaged from date cannot be prior to admission date.");
 					break;

				}*/
				
				if(bean.getEngagedTo() == null){
					hasError = true;
 					errorMessages.add("Please Enter Engaged To Date");
 					break;
				}
	
//				
//				if(null != dateMap)
//				 {
//					 if(dateMap.containsKey(bean.getEngagedFrom()))
//					 {
//						 hasError = true;
//						 errorMessages.add("The entered from date is already existing . Please enter a valid date");
//						// break;
//					 }
//					 else
//					 {
//						 if(null != bean.getEngagedFrom())
//						 dateMap.put(bean.getEngagedFrom(),bean.getEngagedFrom().toString());
//					 }
//					 if(dateMap.containsKey(bean.getEngagedTo()))
//					 {
//						 hasError = true;
//						 errorMessages.add("The entered to date is already existing . Please enter a valid date");
//						// break;
//					 }
//					 else
//					 {
//						 if(null != bean.getEngagedTo())
//						 dateMap.put(bean.getEngagedTo(),bean.getEngagedTo().toString());
//					 }
//				 }
		}
 				
 				 Boolean findDublicateDate = findDublicateDate(itemIds);
 				 if(findDublicateDate){
 					hasError = true;
 					errorMessages.add("The entered to date is already existing . Please enter a valid date");
 				 }
			}
			else
			{
				hasError = true;
				errorMessages.add("Please enter engaged from and engaged To date in patient care table to proceed further.");
			}
			return !hasError;

		}
	
	 public List<PatientCareDTO> getValues() {
	    	@SuppressWarnings("unchecked")
	    	List<PatientCareDTO> itemIds = new ArrayList<PatientCareDTO>();
	    	if(this.table != null) {
	    		itemIds = (List<PatientCareDTO>) this.table.getItemIds() ;
	    	} 
			
	    	return itemIds;
	    }
	 
	 public List<String> getErrors()
		{
			return this.errorMessages;
		}
	 
	  public void getErrorMessage(String eMsg){
			
			/*Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
		}

}

