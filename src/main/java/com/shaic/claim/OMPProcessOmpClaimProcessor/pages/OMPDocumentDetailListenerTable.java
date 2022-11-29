/**
 * 
 */
package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.pages.DocumentDetailsPresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class OMPDocumentDetailListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<ReconsiderRODRequestTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ReconsiderRODRequestTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<ReconsiderRODRequestTableDTO> container = new BeanItemContainer<ReconsiderRODRequestTableDTO>(ReconsiderRODRequestTableDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private Map<String, Object> referenceData;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	
	private String presenterString = "";
	
	private ViewDetails objViewDetails;
	
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init() {
	//	populateBillDetails(bean);
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
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
		table.setHeight("150px");
		table.setPageLength(table.getItemIds().size());
		//table.setSizeFull();
		
		addListener();
		
		layout.addComponent(table);
	//	layout.addComponent(btnAdd);
		layout.setComponentAlignment(table, Alignment.TOP_RIGHT);
		//layout.setComponentAlignment(btnAdd,Alignment.TOP_LEFT);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(layout);
		horLayout.setComponentAlignment(layout, Alignment.TOP_RIGHT);
		
		Panel tblPanel = new Panel();
		tblPanel.setWidth("90%");
		tblPanel.setHeight("200px");
		tblPanel.setContent(horLayout);
		
		//horLayout.setWidth("100%");
		
	/*	Panel tablePanel = new Panel();
		tablePanel.setContent(horLayout);
		tablePanel.setWidth("91%");*/
		setCompositionRoot(tblPanel);
		//setCompositionRoot(horLayout);
	}
	
	public void setTableList(final List<ReconsiderRODRequestTableDTO> list) {
		table.removeAllItems();
		if(list!=null){
		for (final ReconsiderRODRequestTableDTO bean : list) {
			table.addItem(bean);
		}}
		table.sort();
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				BeanItem<ReconsiderRODRequestTableDTO> addItem = container.addItem(new ReconsiderRODRequestTableDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		table.setVisibleColumns(new Object[] { "rodNo", "classification", "subclassification", "documentReceivedFrom","documentReceivedDate","modeOfReceipt","approvedAmt","rodStatus","select"});
		table.setColumnHeader("rodNo", "ROD No");
		table.setColumnHeader("classification", "Classification");
		table.setColumnHeader("subclassification", "Sub Classification");
		table.setColumnHeader("documentRecievedFrom", "Document Recieved From");
		table.setColumnHeader("documentRecievedDate", "Document Recieved Date");
		table.setColumnHeader("modeOfReceipt", "Mode of Receipt");
		table.setColumnHeader("approvedAmt", "Approved Amount");
		table.setColumnHeader("rodStatus", "ROD </br> status ");
		table.setColumnHeader("select", "Select");
		table.setEditable(true);
		table.removeGeneratedColumn("viewstatus");
		table.addGeneratedColumn("viewstatus", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
					
				ReconsiderRODRequestTableDTO reconsiderDTO = (ReconsiderRODRequestTableDTO) itemId;
				final String intimationNo = reconsiderDTO.getIntimationNo();
				Button button = new Button("View Claim Status");
				button.setEnabled(true);
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {

						/**
						 * The button click event needs to be added post the view details
						 * page is developed. Once that is done, the same can be injected and
						 * re used. Below commented code can be uncommented and with slight
						 * modification , view details button should work. 
						 * This will be changed later. 
						 * **/
						
						displayClaimStatus(intimationNo);
						
						
					}
				});
				return button;
			}
		});
		table.setTableFieldFactory(new ImmediateFieldFactory());
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
	}
	
	private void displayClaimStatus(String intimationNo)
	{
		//objViewDetails = viewDetails.get();
		//queryDetailsObj.init(viewDetails);
		objViewDetails.viewClaimStatusUpdated(intimationNo);
	}
	
	public void setViewDetailsObj(ViewDetails viewDetails)
	{
		objViewDetails = viewDetails;
	//	initTable();
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	
	
	public void manageListeners() {
		}
	
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			ReconsiderRODRequestTableDTO entryDTO = (ReconsiderRODRequestTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				
			if ("rodNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				//field.setWidth("125px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				field.setMaxLength(50);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				
				tableRow.put("rodNo", field);
				return field;
			}
			
			else if ("classification".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("classification", field);

				return field;
			}else if ("subclassification".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("subclassification", field);

				return field;
			}
			else if ("documentReceivedFrom".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("documentReceivedFrom", field);

				return field;
			}else if ("documentReceivedDate".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("documentReceivedDate", field);

				return field;
			}else if ("modeOfReceipt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("modeOfReceipt", field);

				return field;
			}
			else if ("approvedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("approvedAmt", field);
				return field;
			}
			else if("rodStatus".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
			
				field.setReadOnly(true);
				
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("rodStatus", field);
				return field;
			}
			else if("select".equals(propertyId)) {
				CheckBox field = new CheckBox();
				field.setReadOnly(false);
				field.setData(entryDTO);
				valueChangeListenerForSelect(field);
				if(null != entryDTO && null != entryDTO.getSelect() && entryDTO.getSelect())
				{
					field.setValue(entryDTO.getSelect());
				}
				tableRow.put("select", field);
				
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

	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<BillEntryDetailsDTO> itemIds = (Collection<BillEntryDetailsDTO>) table.getItemIds();
		
		int i = 0;
		 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("itemNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}
	
	 public void addBeanToList(ReconsiderRODRequestTableDTO billEntryDetailsDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 container.addItem(billEntryDetailsDTO);

//	    	data.addItem(pedValidationDTO);
	    	//manageListeners();
	    }
	
	 public List<ReconsiderRODRequestTableDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<ReconsiderRODRequestTableDTO> itemIds = (List<ReconsiderRODRequestTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	 
	public void valueChangeListenerForSelect(final CheckBox chkBox){
			
		if(null != chkBox)
		{
			final ReconsiderRODRequestTableDTO reconsiderDTO = (ReconsiderRODRequestTableDTO) chkBox.getData();
			chkBox.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
					{
						List<ReconsiderRODRequestTableDTO> items = (List<ReconsiderRODRequestTableDTO>) table.getItemIds();
						boolean value = (Boolean) event.getProperty().getValue();
						reconsiderDTO.setSelect(value);
						//validateSelectItems(reconsiderDTO);
						for (ReconsiderRODRequestTableDTO dto : items) {
								
									tableSelectHandler(reconsiderDTO);
								/*if((reconsiderDTO.getAcknowledgementNo().equals(dto.getAcknowledgementNo())))
								{
									tableSelectHandler(reconsiderDTO);
								}*/
						}
						disableCheckBox(chkBox);
					}
				}
			});
			
			if(null != reconsiderDTO.getSelect() && reconsiderDTO.getSelect()) 
			{
				chkBox.setValue(reconsiderDTO.getSelect());
			}
		}
	}
			
	
	private void disableCheckBox(CheckBox chkBox)
	{
		Collection<ReconsiderRODRequestTableDTO> itemIds = (Collection<ReconsiderRODRequestTableDTO>) table.getItemIds();
		
		ReconsiderRODRequestTableDTO reconsiderDTO = (ReconsiderRODRequestTableDTO) chkBox.getData();
		if(null != itemIds && !itemIds.isEmpty())
		{
			for (ReconsiderRODRequestTableDTO reconsiderRODRequestTableDTO : itemIds) {
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(reconsiderRODRequestTableDTO);
				 if(null != hashMap && !hashMap.isEmpty())
				 {
					 CheckBox chkSelect = (CheckBox) hashMap.get("select");
					 if(null != chkSelect)
					 {
						Boolean value = chkBox.getValue();
						//if(!(reconsiderDTO.getAcknowledgementNo().equals(reconsiderRODRequestTableDTO.getAcknowledgementNo())))
							//	{
									if(null != value)
									{
										/***
										 * The below value is set to null,
										 * so that the unselected checkbox 
										 * remains unselect by setting value false. 
										 */
										chkSelect.setValue(null);
										chkSelect.setEnabled(!value);
									}
							//	}
						
					 }
				 }
			}
		}
		
		
		
	}
		
	
	public void tableSelectHandler(ReconsiderRODRequestTableDTO t) {
		
		if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(DocumentDetailsPresenter.DISABLE_TABLE_VALUES, t);
		}
		else if (SHAConstants.CREATE_ROD.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(CreateRODDocumentDetailsPresenter.SELECT_RECONSIDER_TABLE_VALUES, t);
		}
		
		else if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(BillEntryDocumentDetailsPresenter.BILL_ENTRY_SELECT_RECONSIDER_TABLE_VALUES, t);
		}

	}
		
	 
	
	
	 
	 
//	 private void daysAndAmtValidationListenerForPreviousRow (final ComboBox component) {
//		 
//		 if(null != component)
//		 {		 
//			 component.addListener(new Listener() {
//				
//				@Override
//				public void componentEvent(Event event) {
//					// TODO Auto-generated method stub
//					daysAndAmtValidationListener(component, event,true);
//				}
//
//	
//			});
//		 }
//	}
	 

}
