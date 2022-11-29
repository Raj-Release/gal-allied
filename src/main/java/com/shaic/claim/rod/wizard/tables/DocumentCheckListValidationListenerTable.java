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

import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.galaxyalert.utils.GalaxyTypeofMessage;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAAcknowledgementDocumentDetailsPage;
import com.shaic.paclaim.rod.createrod.search.PACreateRODDocumentDetailsPage;
import com.shaic.paclaim.rod.enterbilldetails.search.PABillEntryDocumentDetailsPage;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class DocumentCheckListValidationListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<DocumentCheckListDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DocumentCheckListDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<DocumentCheckListDTO> container = new BeanItemContainer<DocumentCheckListDTO>(DocumentCheckListDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	
	
	private BeanItemContainer<SelectValue> category;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private BeanItemContainer<SelectValue> categoryValues;
	
	private String billNo;
	
	private Date billDate;
	
	private Long noOfItems;
	
	private Double billValue;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	private int iItemValue = 0;
	
	private String presenterString = "";
	
	private String lobPA = "";
	
	private String acknowledgementRemarks;
	
	private VerticalLayout vLayout ;
	
	private VerticalLayout mainLayout;
	
	private Boolean isHospitalization;
	
	private PAAcknowledgementDocumentDetailsPage paAckDocumentDetailsPage;
	
	private PACreateRODDocumentDetailsPage paCreateRodDocumentDetailsPage;
	
	private PABillEntryDocumentDetailsPage paBillEntryDocumentDetailsPage;
	
	@Inject
	private ReceiptOfDocumentsDTO bean;
	
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;	
		isHospitalization = true;
	}
	
	public void initPresenter(String presenterString,String paLob) {
		this.presenterString = presenterString;	
		this.lobPA = paLob;
		isHospitalization = true;
	}
	public void init() {
	//	populateBillDetails(bean);
		initTb1();
		//setCompositionRoot(horLayout);
	}

	public void setPaAckDocumentDetailsPage(
			PAAcknowledgementDocumentDetailsPage paAckDocumentDetailsPage) {
		this.paAckDocumentDetailsPage = paAckDocumentDetailsPage;
		
	}

	public void setPaCreateRodDocumentDetailsPage(
			PACreateRODDocumentDetailsPage paCreateRodDocumentDetailsPage) {
		this.paCreateRodDocumentDetailsPage = paCreateRodDocumentDetailsPage;
	}

	public void setPaBillEntryDocumentDetailsPage(
			PABillEntryDocumentDetailsPage paBillEntryDocumentDetailsPage) {
		this.paBillEntryDocumentDetailsPage = paBillEntryDocumentDetailsPage;
	}

	private void initTb1()
	{
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		vLayout = new VerticalLayout();
		this.errorMessages = new ArrayList<String>();
		
		
		HorizontalLayout btnLayout = buildButtonLayout();
		
		
		//VerticalLayout layout = new VerticalLayout();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		//layout.setMargin(true);
		//layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("100%");
		//table.setHeight("359px");
		//table.setWidth("100%");
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
		
		Panel tablePanel = new Panel();
		tablePanel.setContent(horLayout);
		
		tablePanel.setWidth("100%");
	
		
		tablePanel.setSizeFull();
		
		if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString)|| 
				SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString) || 
				SHAConstants.ADD_ADDITIONAL_DOCS.equalsIgnoreCase(presenterString)|| SHAConstants.OMP_ROD.equalsIgnoreCase(presenterString))
		{
			mainLayout = new VerticalLayout();
			mainLayout.addComponent(btnLayout);
			mainLayout.addComponent(tablePanel);
			setCompositionRoot(mainLayout);
		}
		else
		{
			setCompositionRoot(tablePanel);
		}
	}
	
	
	private void addAddBtnListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				
				DocumentCheckListDTO docDTO = new DocumentCheckListDTO();
				List<DocumentCheckListDTO> dtoList = (List<DocumentCheckListDTO>)table.getItemIds();
				BeanItem<DocumentCheckListDTO> addItem = container.addItem(docDTO);

				//dtoList.add(new DocumentCheckListDTO());
				
				if(null != dtoList && !dtoList.isEmpty())
				{ 
					/*for (DocumentCheckListDTO documentCheckListDTO : dtoList) {
						addBeanToList(documentCheckListDTO);
					}*/
					//addBeanToList(new DocumentCheckListDTO());
					int iSize = dtoList.size();
					DocumentCheckListDTO dto = dtoList.get(iSize-1);
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					//final TextField txtFld = (TextField) combos.get("particulars");
					final ComboBox txtFld = (ComboBox) combos.get("particulars");
					txtFld.focus();
				}
				
			/*	BeanItem<DocumentCheckListDTO> addItem = container.addItem(new DocumentCheckListDTO());
				if(null != dtoList && !dtoList.isEmpty())
				{
					int iSize = dtoList.size();
					DocumentCheckListDTO dto = dtoList.get(iSize-1);
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					//final TextField txtFld = (TextField) combos.get("particulars");
					final ComboBox txtFld = (ComboBox) combos.get("particulars");
					txtFld.focus();
				}*/
				
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	private HorizontalLayout buildButtonLayout()
	{
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		addAddBtnListener();
		
		return btnLayout;
	}
	
	public void setTableList(final List<DocumentCheckListDTO> list) {
		table.removeAllItems();
		for (final DocumentCheckListDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		/*"slNo",
		 "value", "mandatoryDocFlag","requiredDocType", "ackReceivedStatus", "noOfDocuments" , "remarks" ,"rodReceivedStatus", "rodRemarks"*/
		//table.setVisibleColumns(new Object[] { "slNo", "value", "mandatoryDocFlag", "requiredDocType","ackReceivedStatus", "noOfDocuments", "remarks","rodReceivedStatus","rodRemarks"});
		
		if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString) || SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString)
				|| SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString) || SHAConstants.ADD_ADDITIONAL_DOCS.equalsIgnoreCase(presenterString))
			table.setVisibleColumns(new Object[] { "slNo", "particulars",  "receivedStatus", "noOfDocuments", "remarks"});
		else if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString) || SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString) || SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString))
			table.setVisibleColumns(new Object[] { "slNo", "particulars", "ackReceivedStatus", "noOfDocuments", "remarks","rodReceivedStatus","rodRemarks"});
		else if(SHAConstants.OMP_ROD.equalsIgnoreCase(presenterString))
			table.setVisibleColumns(new Object[] { "slNo", "particulars",  "receivedStatus", "noOfDocuments", "remarks"});
		table.setColumnHeader("slNo", "Sl</br> No");
		table.setColumnHeader("particulars", "Particulars");
		if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString) || 
				SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString) ||
				SHAConstants.ADD_ADDITIONAL_DOCS.equalsIgnoreCase(presenterString)
				|| SHAConstants.OMP_ROD.equalsIgnoreCase(presenterString))
		{
			table.setColumnHeader("receivedStatus", "Received </br> Status");
		}
		table.setColumnHeader("noOfDocuments", "No Of </br> Documents");		
		table.setColumnHeader("remarks", "Remarks");	
		
		if(SHAConstants.PA_LOB.equalsIgnoreCase(lobPA))
		{		
		table.setColumnHeader("noOfDocuments", "No Of </br> Pages");
		}
		
		if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString) || SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString)|| SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString)
				||SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString))
		{
			table.setColumnHeader("ackReceivedStatus", "Acknowledge </br>Received </br> Status");
			table.setColumnHeader("rodReceivedStatus", "ROD </br> Received </br> status");
			table.setColumnHeader("rodRemarks", "ROD </br> Remarks");
		}
		//table.setColumnHeader("mandatoryDocFlag", "Mandatory </br> Document </br> (Yes/No)");
				//table.setColumnHeader("requiredDocType", "Required Doc </br>Type </br> Original/photocopy");
		table.setEditable(true);
		if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString) || SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString) ||
				SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString) || SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString) ||
				SHAConstants.ADD_ADDITIONAL_DOCS.equalsIgnoreCase(presenterString)|| SHAConstants.OMP_ROD.equalsIgnoreCase(presenterString))
		{
			table.removeGeneratedColumn("Delete");
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
		}
		
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		//table.setFooterVisible(true);
		//table.setColumnFooter("category", String.valueOf("Total"));
		}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	private void addListener() {/*
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				BeanItem<DocumentCheckListDTO> addItem = container.addItem(new DocumentCheckListDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	*/}
	
	
	
	public void manageListeners() {

		for (DocumentCheckListDTO documentCheckListDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(documentCheckListDTO);
			
			
			
			//addClassificationListener(classificationCombo,categoryCombo);
			//calculateItemValue(txtPerDay);
			
			
			

		}
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
				
			if ("particulars".equals(propertyId)) {
				
				GComboBox box = new GComboBox();
				box.setWidth("400px");
				//box.setWidth("150px");
				tableRow.put("particulars", box);
				box.setData(entryDTO);
				
				//field.setEnabled(false);
			
				
				if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString) || SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString) /*|| SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString)*/)
				{
					box.setReadOnly(true);	
//					addValuesForValueDropDown(box);
//					box.setReadOnly(false);
				} 
				else
				{
					addValuesForValueDropDown(box);
					box.setReadOnly(false);
					box = populateDefaultParticularsValues(box);
				}
				if(!(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString)) && 
						!(SHAConstants.ADD_ADDITIONAL_DOCS.equalsIgnoreCase(presenterString)))
				{
				addParticularsListener(box);
				}
				return box;
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
				
				if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString) || SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
					field.setReadOnly(true);
				
				return field;
			}
			
		/*	else if ("mandatoryDocFlag".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(false);
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
			}*/
			else if ("receivedStatus".equals(propertyId)) {
				
				GComboBox box = new GComboBox();
				box.setWidth("160px");
				//box.setWidth("150px");
				box.setData(entryDTO);
				tableRow.put("receivedStatus", box);
				//field.setEnabled(false);
				addValuesForReceivedStatus(box);
				if(!(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString) || SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString)))
				box.setReadOnly(false);
				
				if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString) || SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString) ||
						SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString) || SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString))
					box.setReadOnly(true);
				
				/*if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString) || SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString) 
						|| SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
				{*/
				if(!(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString)) && 
						!(SHAConstants.ADD_ADDITIONAL_DOCS.equalsIgnoreCase(presenterString)))
				{
				addReceivedStatusListener(box);
				}
				//}
				//field.addValueChangeListener(valueChangeListenerForAckStatus(field));
				return box;
			}
			else if ("ackReceivedStatus".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				tableRow.put("ackReceivedStatus", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(false);
				if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString) || SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
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
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(false);
				if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString) || SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
				{
					field.setReadOnly(true);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
				final TextField txt = (TextField) tableRow.get("slNo");
				generateSlNo(txt);
				return field;
			}
			else if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("300px");
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setData(entryDTO);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(false);
				tableRow.put("remarks", field);
				if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
					field.setReadOnly(true);
				return field;
			}
			else if("rodReceivedStatus".equals(propertyId)) {
				OptionGroup field = new OptionGroup();
				field.addItems(getReadioButtonOptions());
				field.setItemCaption(true, "Yes");
				field.setItemCaption(false, "No");
				field.setStyleName("horizontal");
				field.setData(entryDTO);
				
				//field.setStyleName(ValoTheme.);
				tableRow.put("rodReceivedStatus", field);
				if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
					field.setReadOnly(true);
				return field;
			}
			else if("rodRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("80px");
				//field.setWidth("150px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				field.setMaxLength(100);
				addDescriptionFromRemarksFld(field);
				enableOrDisableRODStatus(field);
				addDeductibleRemarksListener(field);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("rodRemarks", field);
				if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
					field.setReadOnly(true);
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
	
	
	
	
	 @SuppressWarnings("unused")
		private void addReceivedStatusListener(final ComboBox receivedStatusBox) {
			if (receivedStatusBox != null) {
				receivedStatusBox.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						
						/*extracted(component);
						resetFieldsBasedOnClassification(component);*/
						populateRemarksBasedOnReceivedStatus(receivedStatusBox);
					}

				});
			}

		}	 
		
		 @SuppressWarnings("unused")
			private void addParticularsListener(final ComboBox partisularStatusBox) {
				if (partisularStatusBox != null) {
					partisularStatusBox.addListener(new Listener() {
						private static final long serialVersionUID = -4865225814973226596L;

						@Override
						public void componentEvent(Event event) {
							ComboBox component = (ComboBox) event.getComponent();
							
							/*extracted(component);
							resetFieldsBasedOnClassification(component);*/
							populateRemarksBasedOnParticulars(partisularStatusBox);
						}

					});
				}

			}
	 
	/*public ValueChangeListener addReceivedStatusListener(final ComboBox box)
	{
		
		//if(null != box)
		//{
			ValueChangeListener valChange = new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					populateRemarksBasedOnReceivedStatus(box);
					
				}
			};
			return valChange;
		//}
		
	}*/
	 
	 private void populateRemarksBasedOnParticulars(final ComboBox component) {
			SelectValue seltValue = (SelectValue) component.getValue(); 
			DocumentCheckListDTO documentCheckListDTO = (DocumentCheckListDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(documentCheckListDTO);
			GComboBox comboBox = (GComboBox) hashMap.get("receivedStatus");
			GComboBox comboBox2 = (GComboBox) hashMap.get("particulars");
			if (null != documentCheckListDTO) {
				//if(null != seltValue && null != seltValue.getValue())
				//{
				if(null != documentCheckListDTO.getReceivedStatus() && null != documentCheckListDTO.getParticulars()) {
					if(null != comboBox && null != comboBox.getValue()) {
						
						SelectValue selValue = (SelectValue)comboBox.getValue();
						SelectValue selValue2 = (SelectValue)comboBox2.getValue();
						HashMap<String, AbstractField<?>> valHashMap = tableItem.get(documentCheckListDTO);
						
						TextField remarks = (TextField) valHashMap.get("remarks");
						
						if(null != selValue && null != selValue.getId() && (ReferenceTable.RECEIVED_PHOTOCOPY).equals(selValue.getId())
								&& (null != isHospitalization && isHospitalization)
								&& !(ReferenceTable.POLICY_DOCUMENT).equals(selValue2.getId())
									&& !(ReferenceTable.PHOTO_ID_CARD).equals(selValue2.getId()) && !(ReferenceTable.CHEQUE).equals(selValue2.getId())
								/*|| (ReferenceTable.RECEIVED_ORIGINAL).equals(selValue.getId())*/)
						{ 
							if(null != remarks){
								if(documentCheckListDTO.getRemarks() != null && !documentCheckListDTO.getRemarks().equalsIgnoreCase("")){
									remarks.setValue(documentCheckListDTO.getRemarks());
								}else{
									remarks.setValue("Please submit the original which is mandatory for claim processing.");
								}
								
							}
//								
								
						}
						else
						{
							if(null != remarks){
								remarks.clear();
							}
						}
					}
				 }
			}
	 	}
	 
	private void populateRemarksBasedOnReceivedStatus(final ComboBox component) {
		SelectValue seltValue = (SelectValue) component.getValue(); 
		DocumentCheckListDTO documentCheckListDTO = (DocumentCheckListDTO) component.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(documentCheckListDTO);
		GComboBox comboBox = (GComboBox) hashMap.get("receivedStatus");
		GComboBox comboBox2 = (GComboBox) hashMap.get("particulars");
		if (null != documentCheckListDTO) {
			//if(null != seltValue && null != seltValue.getValue())
			//{
			if(null != documentCheckListDTO.getReceivedStatus() && null != documentCheckListDTO.getParticulars()) {
				if(null != comboBox && null != comboBox.getValue()) {
					
					SelectValue selValue = (SelectValue)comboBox.getValue();
					SelectValue selValue2 = (SelectValue)comboBox2.getValue();
					HashMap<String, AbstractField<?>> valHashMap = tableItem.get(documentCheckListDTO);
					
					TextField remarks = (TextField) valHashMap.get("remarks");
					
					/*if(("ICU Rooms").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue())
							
							|| ("Nursing Charges").equalsIgnoreCase(selValue.getValue())
							)
					if(null != selValue && null != selValue.getId() && (ReferenceTable.RECEIVED_PHOTOCOPY).equals(selValue.getId()) || (ReferenceTable.RECEIVED_ORIGINAL).equals(selValue.getId()))
					{
						if(null != remarks)
							remarks.setValue("Please submit the original which is mandatory for claim processing.");
					}
					else
					{
						if(null != remarks)
							remarks.setValue("");
					}*/
					
					
					
					if(null != selValue && null != selValue.getId() && (ReferenceTable.RECEIVED_PHOTOCOPY).equals(selValue.getId())
							&& (null != isHospitalization && isHospitalization)
							&& !(ReferenceTable.POLICY_DOCUMENT).equals(selValue2.getId())
								&& !(ReferenceTable.PHOTO_ID_CARD).equals(selValue2.getId()) && !(ReferenceTable.CHEQUE).equals(selValue2.getId())
							/*|| (ReferenceTable.RECEIVED_ORIGINAL).equals(selValue.getId())*/)
					{ 
						if(null != remarks){
							if(documentCheckListDTO.getRemarks() != null && !documentCheckListDTO.getRemarks().equalsIgnoreCase("")){
								remarks.setValue(documentCheckListDTO.getRemarks());
							}else{
								remarks.setValue("Please submit the original which is mandatory for claim processing.");
							}
							
						}
//							
							
					}
					else
					{
						if(null != remarks){
							remarks.clear();
						}
					}
				}
			}
		}
	}
	
	
	public void setIsHospitalization(Boolean value)
	{
		this.isHospitalization = value;
	}
	 
	 @SuppressWarnings("unchecked")
		public void addValuesForValueDropDown(ComboBox comboBox) {
			BeanItemContainer<SelectValue> billClassificationContainer = (BeanItemContainer<SelectValue>) referenceData
					.get("particulars");
			
			//BeanItemContainer<SelectValue> finalContainer = billClassificationContainer;
			/*for(int i = 0 ; i<billClassificationContainer.size() ; i++)
			 {
				if (("Hospital").equalsIgnoreCase(billClassificationContainer.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}*/
			
			comboBox.setContainerDataSource(billClassificationContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

		}
	 
	 
	 private GComboBox populateDefaultParticularsValues(GComboBox cmbBox)
	 {
		 BeanItemContainer<SelectValue> billClassificationContainer = (BeanItemContainer<SelectValue>) referenceData
					.get("particulars");
		 if(null != cmbBox)
		 {
			 DocumentCheckListDTO documentDTO = (DocumentCheckListDTO)cmbBox.getData();
			 if(null != documentDTO)
			 {
				 SelectValue particularsValue = documentDTO.getParticulars();
				 cmbBox.setValue(particularsValue);
				 /*if(null != particularsValue)
				 {
					 if(null != billClassificationContainer)
					 {
						 for(int i = 0 ; i<billClassificationContainer.size() ; i++)
						 {
							if ((particularsValue.getValue()).equalsIgnoreCase(billClassificationContainer.getIdByIndex(i).getValue()))
							{
								//cmbBox.setId(String.valueOf(billClassificationContainer.getIdByIndex(i).getId()));
								cmbBox.setValue(billClassificationContainer.getIdByIndex(i));
							}
						}
					 }
				 }*/
			 }
		 }
		 return cmbBox;
	 }
	 
	 @SuppressWarnings("unchecked")
		public void addValuesForReceivedStatus(ComboBox comboBox) {
			BeanItemContainer<SelectValue> billClassificationContainer = (BeanItemContainer<SelectValue>) referenceData
					.get("receivedStatus");
			
			//BeanItemContainer<SelectValue> finalContainer = billClassificationContainer;
			/*for(int i = 0 ; i<billClassificationContainer.size() ; i++)
			 {
				if (("Hospital").equalsIgnoreCase(billClassificationContainer.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}*/
			
			comboBox.setContainerDataSource(billClassificationContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

		}
		
	
public void addDeductibleRemarksListener(final TextField txtFld){
		
		if(null != txtFld)
		{
			
//			txtFld.addTextChangeListener(new TextChangeListener() {
//				
//				@Override
//				public void textChange(TextChangeEvent event) {
			
			txtFld.addShortcutListener(new ShortcutListener("",KeyCodes.KEY_F5,null) {
				
				@Override
				 public void handleAction(Object sender, Object target) {
					
					DocumentCheckListDTO documentCheckListDTO = (DocumentCheckListDTO) txtFld.getData();
					 HashMap<String, AbstractField<?>> hashMap = tableItem.get(documentCheckListDTO);
					 TextField txtItemValue = (TextField) hashMap.get("rodRemarks");
					
					if (null != vLayout
							&& vLayout.getComponentCount() > 0) {
						vLayout.removeAllComponents();
					}
					
					TextArea txtArea = new TextArea();
					txtArea.setMaxLength(200);
					txtArea.setNullRepresentation("");
					txtArea.setValue(documentCheckListDTO.getRodRemarks());
					txtArea.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							TextArea txt = (TextArea)event.getProperty();
							txtFld.setValue(txt.getValue());
							txtFld.setDescription(txt.getValue());
							// TODO Auto-generated method stub
							
						}
					});
					
					documentCheckListDTO.setRodRemarks(txtFld.getValue());
					txtFld.setDescription(documentCheckListDTO.getRodRemarks());
					/*Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
					vLayout.addComponent(txtArea);
					//vLayout.addComponent(okBtn);
					//vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
					/*final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(vLayout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);*/
					
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createCustomBox("Remarks", vLayout, buttonsNamewithType, GalaxyTypeofMessage.INFORMATION.toString());
					Button okBtn = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
							.toString());
					okBtn.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							//dialog.close();
						}
					});	
				}
			});
		}
	}


	
	private void addDescriptionFromRemarksFld(TextField txtFld)
	{
		
		DocumentCheckListDTO docCheckList = (DocumentCheckListDTO) txtFld.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(docCheckList);
		 if(null != hashMap && !hashMap.isEmpty())
		 {
			 TextField remarks = (TextField)hashMap.get("remarks");
			 if(null != remarks)
			 {
				 
				 remarks.setDescription(docCheckList.getRemarks());
				/* itemNoFld.setReadOnly(false);
				 itemNoFld.setValue(String.valueOf(i));
				 itemNoFld.setReadOnly(true);*/
				 //itemNoFld.setEnabled(false);
			 }
		 }
	}

protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}

/*private void addListenerForRemarks(TextField txtFld)
{
	if(null != txtFld)
	{
		txtFld.addM
	}
}*/
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<DocumentCheckListDTO> itemIds = (Collection<DocumentCheckListDTO>) table.getItemIds();
		
		int i = 0;
		 for (DocumentCheckListDTO docCheckListDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(docCheckListDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("slNo");
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
	    	
	    	List<DocumentCheckListDTO> itemIds = new ArrayList<DocumentCheckListDTO>();
	    	
			itemIds = (List<DocumentCheckListDTO>) this.table.getItemIds() ;
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
		
			if(null != feild && feild.getValue() != null && ("Not Applicable").equalsIgnoreCase(feild.getValue().trim()))
			{
				if(null != optGroup)
				optGroup.setEnabled(false);
			}
				
		}
		
		
		public boolean validatePageForAckScreen()
		{

			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			Collection<DocumentCheckListDTO> itemIds = (Collection<DocumentCheckListDTO>) table.getItemIds();
			/*Map<Long, String> valuesMap = new HashMap<Long, String>();
			Map<Long, String> validationMap = new HashMap<Long, String>();*/
			if(null != itemIds && !itemIds.isEmpty())
			{
				for (DocumentCheckListDTO bean : itemIds) {
					
					if((null != bean.getReceivedStatus() && ReferenceTable.RECEIVED_ORIGINAL.equals(bean.getReceivedStatus().getId())) ||
							
							(null != bean.getReceivedStatus() && ReferenceTable.RECEIVED_PHOTOCOPY.equals(bean.getReceivedStatus().getId())))
					{
						hasError = false;
						break;
					}
					else
					{
						hasError = true; 
						errorMessages.add("Please choose received status for atleast one document in document checklist table");
					}
				}
			}
			else
			{
				hasError = true; 
				errorMessages.add("Please add atleast one doucment in checklist table before you proceed");
			}
			
				return !hasError;
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
				
				if((null != bean.getAckReceivedStatus() && ("Received").equalsIgnoreCase(bean.getAckReceivedStatus().trim())) && ("N").equalsIgnoreCase(bean.getRodReceivedStatusFlag().trim()))
				{
					if(null == bean.getRodRemarks() || ("").equalsIgnoreCase(bean.getRodRemarks()))
					{
						hasError = true;
						errorMessages.add("Item no :"+bean.getSlNo() +" ,Please Enter ROD Remarks if acknowledgement status is Received and rod status is No");
					}
				}
				
				else if((null != bean.getAckReceivedStatus() && ("Not Received").equalsIgnoreCase(bean.getAckReceivedStatus().trim())) && bean.getRodReceivedStatusFlag()  != null && ("Y").equalsIgnoreCase(bean.getRodReceivedStatusFlag().trim()))
				{
					if(null == bean.getRodRemarks() || ("").equalsIgnoreCase(bean.getRodRemarks()))
					{
						hasError = true;
						errorMessages.add("Item no :"+bean.getSlNo() +" ,Please Enter ROD Remarks if acknowledgement status is Not Received and rod status is Yes");
					}
				}
				
				if((null != bean.getAckReceivedStatus() && !("").equalsIgnoreCase(bean.getAckReceivedStatus().trim()) && !("Not Applicable").equalsIgnoreCase(bean.getAckReceivedStatus().trim())
						))
				{
					if(null == bean.getRodReceivedStatus())
					{
						hasError = true;
						errorMessages.add("Item no :"+bean.getSlNo() +" ,Please select ROD Received Status");
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

