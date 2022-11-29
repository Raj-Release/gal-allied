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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class BillEntryDocumentCheckListValidationListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<DocumentCheckListDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DocumentCheckListDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<DocumentCheckListDTO> container = new BeanItemContainer<DocumentCheckListDTO>(DocumentCheckListDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	//private Map<String, Object> referenceData;
	
	
	
	//private BeanItemContainer<SelectValue> category;
	
	private List<String> errorMessages;
	
	//private static Validator validator;
	
	//private BeanItemContainer<SelectValue> categoryValues;
	
	//private String billNo;
	
	//private Date billDate;
	
	//private Long noOfItems;
	
	//private Double billValue;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	//private int iItemValue = 0;
	
	//private String presenterString = "";
	
	public void initPresenter(String presenterString) {
		//this.presenterString = presenterString;
	}
	
	public void init() {
	//	populateBillDetails(bean);
		container.removeAllItems();
		/*ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();*/
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		/*HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);*/
		
		
		//VerticalLayout layout = new VerticalLayout();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		//layout.setMargin(true);
		//layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("90%");
		//table.setHeight("359px");
		table.setHeight("100%");
		table.setPageLength(table.getItemIds().size());
		//table.setSizeFull();
		
		addListener();
		
		layout.addComponent(table);
		//layout.addComponent(btnAdd);
		layout.setComponentAlignment(table, Alignment.TOP_RIGHT);
		//layout.setComponentAlignment(btnAdd,Alignment.TOP_LEFT);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(layout);
		horLayout.setMargin(true);
		//horLayout.setComponentAlignment(layout, Alignment.TOP_RIGHT);
		
		/*Panel tblPanel = new Panel();
		tblPanel.setWidth("90%");
		tblPanel.setHeight("800px");
		tblPanel.setContent(horLayout);*/
		
		//horLayout.setWidth("100%");
		
	/*	Panel tablePanel = new Panel();
		tablePanel.setContent(horLayout);
		tablePanel.setWidth("91%");*/
		setSizeFull();
		setCompositionRoot(horLayout);
		//setCompositionRoot(horLayout);
	}
	
	public void setTableList(final List<BillEntryDetailsDTO> list) {
		table.removeAllItems();
		for (final BillEntryDetailsDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		/*"slNo",
		 "value", "mandatoryDocFlag","requiredDocType", "ackReceivedStatus", "noOfDocuments" , "remarks" ,"rodReceivedStatus", "rodRemarks"*/
		table.setVisibleColumns(new Object[] { "slNo", "value", "mandatoryDocFlag", "requiredDocType","ackReceivedStatus", "noOfDocuments", "remarks","rodReceivedStatus","rodRemarks"});
		
		
		table.setColumnHeader("slNo", "Sl</br> No");
		table.setColumnHeader("value", "Particulars");
		table.setColumnHeader("mandatoryDocFlag", "Mandatory </br> Document </br> (Yes/No)");
		table.setColumnHeader("requiredDocType", "Required Doc </br>Type </br> Original/photocopy");
		table.setColumnHeader("ackReceivedStatus", "Acknowledge </br>Received </br> Status");
		table.setColumnHeader("noOfDocuments", "No Of </br> Documents");
		table.setColumnHeader("remarks", "Acknowledgement </br> Remarks");
		table.setColumnHeader("rodReceivedStatus", "ROD </br> Received </br> status");
		table.setColumnHeader("rodRemarks", "ROD </br> Remarks");
		table.setEditable(true);
		/*table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				//final Button deleteButton = new Button("Delete");
				final Button deleteButton = new Button();
				//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				deleteButton.setIcon(FontAwesome.TRASH_O);
				deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				deleteButton.setWidth("-1px");
				deleteButton.setHeight("-10px");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
					}
				});

				return deleteButton;
			}
		});
		*/
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		//table.setFooterVisible(true);
		//table.setColumnFooter("category", String.valueOf("Total"));

	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		//this.referenceData = referenceData;
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				//BeanItem<DocumentCheckListDTO> addItem = container.addItem(new DocumentCheckListDTO());
				container.addItem(new DocumentCheckListDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	
	
	public void manageListeners() {

		/*for (DocumentCheckListDTO documentCheckListDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(documentCheckListDTO);
			
			
			
			//addClassificationListener(classificationCombo,categoryCombo);
			//calculateItemValue(txtPerDay);
			
			
			

		}*/
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			DocumentCheckListDTO entryDTO = (DocumentCheckListDTO) itemId;
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
			} 
				tableRow = tableItem.get(entryDTO);
				
			if ("value".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("150px");
				field.setWidth("350px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setMaxLength(50);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("value", field);
				//field.setEnabled(false);
				field.setReadOnly(true);
				return field;
			}
			
			else if ("slNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				
				field.setData(entryDTO);
				
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setEnabled(false);
			
				tableRow.put("slNo", field);
				
				return field;
			}
			
			else if ("mandatoryDocFlag".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

				tableRow.put("mandatoryDocFlag", field);

				return field;
			}
			else if ("requiredDocType".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
			
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);

				tableRow.put("requiredDocType", field);

				return field;
			}
			else if ("ackReceivedStatus".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				tableRow.put("ackReceivedStatus", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				//field.addValueChangeListener(valueChangeListenerForAckStatus(field));
				return field;
			}
			else if("noOfDocuments".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				tableRow.put("noOfDocuments", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				final TextField txt = (TextField) tableRow.get("slNo");
				generateSlNo(txt);
				return field;
			}
			else if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				tableRow.put("remarks", field);
				return field;
			}
			else if("rodReceivedStatus".equals(propertyId)) {
				OptionGroup field = new OptionGroup();
				field.addItems(getReadioButtonOptions());
				field.setItemCaption(true, "Yes");
				field.setItemCaption(false, "No");
				field.setStyleName("horizontal");
				field.setData(entryDTO);
				field.setReadOnly(true);
				//field.setStyleName(ValoTheme.);
				tableRow.put("rodReceivedStatus", field);
				return field;
			}
			else if("rodRemarks".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("120px");
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				field.setMaxLength(100);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				//enableOrDisableRODStatus(field);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("rodRemarks", field);
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

protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<DocumentCheckListDTO> itemIds = (Collection<DocumentCheckListDTO>) table.getItemIds();
		
		int i = 0;
		 for (DocumentCheckListDTO docCheckListDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(docCheckListDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("itemNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setReadOnly(false);
					 itemNoFld.setValue(String.valueOf(i));
					 itemNoFld.setReadOnly(true);
					 //itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}
	
	
	public void addBeanToList(DocumentCheckListDTO documentCheckListDTO) {
    	//container.addBean(uploadDocumentsDTO);
	 container.addItem(documentCheckListDTO);

//    	data.addItem(pedValidationDTO);
    	//manageListeners();
    }
	
	 public List<DocumentCheckListDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<DocumentCheckListDTO> itemIds = (List<DocumentCheckListDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	 
	
	 
	
		public ValueChangeListener valueChangeListenerForAckStatus(final TextField box)
		{
			
			//if(null != box)
			//{
				ValueChangeListener valChange = new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						enableOrDisableRODStatus(box);
						
					}
				};
				return valChange;
			//}
			
		}
		
		private void enableOrDisableRODStatus(TextField box)
		{
			DocumentCheckListDTO docCheckListDTO = (DocumentCheckListDTO) box.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(docCheckListDTO);
			OptionGroup optGroup = (OptionGroup) hashMap.get("rodReceivedStatus");
			TextField feild = (TextField) hashMap.get("ackReceivedStatus");
		
			if(null != feild && ("Not Applicable").equalsIgnoreCase(feild.getValue()))
			{
				if(null != optGroup)
				optGroup.setEnabled(false);
			}
				
		}
		public boolean isValid()
		{
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			Collection<DocumentCheckListDTO> itemIds = (Collection<DocumentCheckListDTO>) table.getItemIds();
			/*Map<Long, String> valuesMap = new HashMap<Long, String>();
			Map<Long, String> validationMap = new HashMap<Long, String>();*/
			for (DocumentCheckListDTO bean : itemIds) {
				
				if((null != bean.getAckReceivedStatus() && ("Received").equalsIgnoreCase(bean.getAckReceivedStatus())) && ("N").equalsIgnoreCase(bean.getRodReceivedStatusFlag()))
				{
					if(null == bean.getRodRemarks() || ("").equalsIgnoreCase(bean.getRodRemarks()))
					{
						hasError = true;
						errorMessages.add("Please Enter ROD Remarks if acknowledgement status is Received and rod status is No");
					}
				}
				
				else if((null != bean.getAckReceivedStatus() && ("Not Received").equalsIgnoreCase(bean.getAckReceivedStatus())) && ("Y").equalsIgnoreCase(bean.getRodReceivedStatusFlag()))
				{
					if(null == bean.getRodRemarks() || ("").equalsIgnoreCase(bean.getRodRemarks()))
					{
						hasError = true;
						errorMessages.add("Please Enter ROD Remarks if acknowledgement status is Not Received and rod status is Yes");
					}
				}
				
				if((null != bean.getAckReceivedStatus() && !("").equalsIgnoreCase(bean.getAckReceivedStatus()) && !("Not Applicable").equalsIgnoreCase(bean.getAckReceivedStatus())
						))
				{
					if(null == bean.getRodReceivedStatus())
					{
						hasError = true;
						errorMessages.add("Please select ROD Received Status");
					}
				}
				
				
				
				
				/*Set<ConstraintViolation<ProcedureDTO>> validate = validator.validate(bean);

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
	*/		}
				return !hasError;
		}
		
		public List<String> getErrors()
		{
			return this.errorMessages;
		}

}

