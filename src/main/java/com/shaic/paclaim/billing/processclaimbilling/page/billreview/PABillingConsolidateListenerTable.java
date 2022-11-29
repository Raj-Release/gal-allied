/**
 * 
 */
package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

/**
 * @author NEWUSER
 *
 */





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
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class PABillingConsolidateListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<PABillingConsolidatedDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PABillingConsolidatedDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<PABillingConsolidatedDTO> container = new BeanItemContainer<PABillingConsolidatedDTO>(PABillingConsolidatedDTO.class);
	
	private Table table;

	//private Button btnAdd;
	
	private Button btnDelete;
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private String presenterString;
	
	private List<AddOnCoversTableDTO> addOnCoversList;
	
	private List<AddOnCoversTableDTO> optionalCoversList;
	
	private List<AddOnCoversTableDTO> deletedList;
	
	//private Map<String, Object> referenceData ;
	
//	private ReceiptOfDocumentsDTO bean;
	

	public void init() {
		//container.removeAllItems();
		
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		deletedList = new ArrayList<AddOnCoversTableDTO>();
		
		this.errorMessages = new ArrayList<String>();

		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		//layout.addComponent(btnLayout);
		initTable();
		table.removeAllItems();
		table.setWidth("800px");
		table.setHeight("100%");
		table.setPageLength(table.getItemIds().size());
		table.setCaption("Consolidated Table");
		
		table.setFooterVisible(Boolean.TRUE);
		
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
	
	
	public void setTableList(final List<PABillingConsolidatedDTO> list) {
		table.removeAllItems();
		for (PABillingConsolidatedDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		//container.removeAllItems();
		table.addStyleName("generateColumnTable");
		/*table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		*/
		//table.setWidth("100%");
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "part",
				"benefitsCover", "payableAmount"});

		table.setFooterVisible(Boolean.TRUE);
		table.setColumnHeader("part","Part");
		table.setColumnHeader("benefitsCover", "Benefit/Covers");
		table.setColumnHeader("payableAmount", "Payable Amount");
		//table.setColumnHeader("amountAlreadypaid", "Amount Already Paid");
		//table.setColumnHeader("netPayableAmount", "Net Payable Amount");
		
		/*table.setColumnHeader("selectforbillentry", "Select For Bill Entry");
		table.setColumnHeader("status", "Status");*/
		table.setEditable(true);
		//table.setColumnWidth("benefitsCover", 150);


//		manageListeners();
		table.setColumnFooter("benefitsCover", "Total Payable Amount");
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
	
		
	/*	btnDelete.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				List<AddOnCoversTableDTO> paCoversDTO = getValues();
				if(null != paCoversDTO && !paCoversDTO.isEmpty())
				{
					int iSize = paCoversDTO.size();
					table.removeItem(paCoversDTO.get(iSize - 1));
				}
			}
		});*/
	}
	

	
	 private void showErrorMsg(String message)
		{
			Label label = new Label(message, ContentMode.HTML);
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
		/* HorizontalLayout layout = new HorizontalLayout(
					new Label(""));
			layout.setMargin(true);
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setWidth("35%");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
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
		PABillingConsolidatedDTO entryDTO = (PABillingConsolidatedDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			}
				tableRow = tableItem.get(entryDTO);
				
			if("part".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("part", field);		
				return field;
			} 
			else if ("benefitsCover".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				field.setReadOnly(Boolean.TRUE);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("benefitsCover", field);		
				return field;
			}
			else if("payableAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(entryDTO);
				field.setReadOnly(Boolean.TRUE);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("payableAmount", field);	
				addNetAmountListener(field);
				generateSlNo(field);
				return field;
			}
			/*else if("amountAlreadypaid".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("amountAlreadypaid", field);		
				return field;
			}*/
			/*else if("netPayableAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("netPayableAmount", field);		
				return field;
			}*/
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
	
	
	
	

	


	
	
	

	 public void addBeanToList(PABillingConsolidatedDTO coversDTO) {
		 container.addItem(coversDTO);
	    }
	
	 
	 
	
	 public List<PABillingConsolidatedDTO> getValues() {
	    	@SuppressWarnings("unchecked")
	    	List<PABillingConsolidatedDTO> itemIds = new ArrayList<PABillingConsolidatedDTO>();
	    	if(this.table != null) {
	    		itemIds = (List<PABillingConsolidatedDTO>) this.table.getItemIds() ;
	    	} 
			
	    	return itemIds;
	    }
	 
	 public List<String> getErrors()
		{
			return this.errorMessages;
		}
	 
	  public void getErrorMessage(String eMsg){
			
			Label label = new Label(eMsg, ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);
		}
	  
	  
	  private void generateSlNo(TextField txtField)
		{
			
			Collection<PABillingConsolidatedDTO> itemIds = (Collection<PABillingConsolidatedDTO>) table.getItemIds();
			
			int i = 0;
			 for (PABillingConsolidatedDTO coversDTO : itemIds) {
				 i++;
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(coversDTO);
				 if(null != hashMap && !hashMap.isEmpty())
				 {
					 TextField itemNoFld = (TextField)hashMap.get("part");
					 if(null != itemNoFld)
					 {
						 itemNoFld.setValue(String.valueOf(i)); 
						 itemNoFld.setEnabled(false);
					 }
				 }
			 }
			
		}

	  public void addNetAmountListener(final TextField total){
			
			if(null != total)
			{
				
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					List<PABillingConsolidatedDTO> tableList = (List<PABillingConsolidatedDTO>) table.getItemIds();
					if(tableList!=null && !tableList.isEmpty()){
						Double payableAmount =0d ;
						for (PABillingConsolidatedDTO paBillingConsolidatedDTO : tableList) {
							if(paBillingConsolidatedDTO.getPayableAmount()!=null){
								payableAmount += paBillingConsolidatedDTO.getPayableAmount();
								
							}
							table.setColumnFooter("payableAmount", String.valueOf(payableAmount));
							
						}
					}
					
					//calculateTotalAmount(total);
					
				}
			});
			}
		}
}

