/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
/*import org.vaadin.dialogs.ConfirmDialog;*/









import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.rrc.detailsPage.ViewClaimWiseRRCHistoryPage;
import com.shaic.claim.rod.wizard.pages.HopsitalCashBenefitDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
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
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class QuantumReductionDetailsListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<QuantumReductionDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<QuantumReductionDetailsDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<QuantumReductionDetailsDTO> container = new BeanItemContainer<QuantumReductionDetailsDTO>(QuantumReductionDetailsDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private BeanItemContainer<SelectValue> category;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private BeanItemContainer<SelectValue> categoryValues;
		
	private int iItemValue = 0;
	
	private String presenterString = "";
	
	private RRCDTO bean;
	private Window popup;
	
	@Inject
	private ViewClaimWiseRRCHistoryPage viewClaimWiseRRCHistoryPage;
	
	private Boolean isEnabledFlag = true;
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init(RRCDTO rrcDTO)
	{
		this.bean = rrcDTO;
		init();
		//this.popup = popup;
	}
	
	public void init(/*QuantumReductionDetailsDTO bean*/) {
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
		
		//table.setHeight("359px");
		table.setPageLength(table.getItemIds().size());
		/**
		 * The caption is removed as a part of production comments.
		 * */
		
		//table.setCaption("Quantum Reduction Details");
		//table.setSizeFull();
		
		addListener();
		
		/*layout.addComponent(table);
		//layout.addComponent(btnAdd);
		layout.setComponentAlignment(table, Alignment.TOP_RIGHT);*/
		//layout.setComponentAlignment(btnAdd,Alignment.TOP_LEFT);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		/*horLayout.addComponent(layout);
		horLayout.setComponentAlignment(layout, Alignment.TOP_RIGHT);*/
		horLayout.addComponent(table);
		horLayout.setComponentAlignment(table, Alignment.TOP_RIGHT);
			table.setWidth("100%");
			horLayout.setWidth("100%");
		
		
		
		horLayout.setMargin(true);
		setCompositionRoot(horLayout);
		
		/*Panel tblPanel = new Panel();
		tblPanel.setWidth("80%");
		tblPanel.setHeight("400px");
		//tblPanel.setHeight("100%");
		tblPanel.setContent(horLayout);
	
		setCompositionRoot(tblPanel);*/
	
	}
	
	public void setTableList(final List<QuantumReductionDetailsDTO> list) {
		table.removeAllItems();
		for (final QuantumReductionDetailsDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("90%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		if((SHAConstants.VIEW_CLAIM_WISE_RRC_HISTORY).equalsIgnoreCase(this.presenterString))
		{
			table.setVisibleColumns(new Object[] { "slNo", "requestNo", "preAuthAmount", "settlementAmount","savedAmount", "anh", "diagnosis" , "management"});
			/*table.setColumnHeader("eligiblity","Eligiblity");
			table.setColumnHeader("requestedSavedAmount", "Saved Amount");*/
			isEnabledFlag = false;
			
		}
		else if((SHAConstants.RRC_REQUEST_FORM).equalsIgnoreCase(this.presenterString))
		{
			table.setVisibleColumns(new Object[] { "slNo", "preAuthAmount","settlementAmount", "savedAmount","anh", "diagnosis" , "management"});
		}
		else
		{
			table.setVisibleColumns(new Object[] { "slNo", "requestNo", "preAuthAmount","settlementAmount","savedAmount", "anh", "diagnosis" , "management"});
		}
		
		table.setColumnHeader("slNo", "Sl</br> No");
		table.setColumnHeader("requestNo", "Request </br> No");
		table.setColumnHeader("preAuthAmount", "Claimed </br> Amount");
		/*table.setColumnHeader("finalBillAmount", "Final Bill </br> Amount");*/
		table.setColumnHeader("settlementAmount", "Approved </br> Amount");
		table.setColumnHeader("anh", "ANH");
		table.setColumnHeader("savedAmount", "Saved </br> Amount");
		table.setColumnHeader("diagnosis", "Diagnosis");
		table.setColumnHeader("management", "Management");
		
		table.setEditable(true);
		
		if((SHAConstants.VIEW_CLAIM_WISE_RRC_HISTORY).equalsIgnoreCase(this.presenterString))
		{
			table.removeGeneratedColumn("View Details");
			table.addGeneratedColumn("View Details", new Table.ColumnGenerator() {
				private static final long serialVersionUID = 5936665477260011479L;
	
				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
					final QuantumReductionDetailsDTO dto = (QuantumReductionDetailsDTO)itemId;
					//final Button deleteButton = new Button("Delete");
					final Button viewButton = new Button("View Details");
					viewButton.setEnabled(true);
					//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
					//deleteButton.setIcon(FontAwesome.TRASH_O);
					viewButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
					/*viewButton.setWidth("-1px");
					viewButton.setHeight("-10px");*/
					viewButton.setData(itemId);
					viewButton.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;
	
						public void buttonClick(ClickEvent event) {
							/**
							 * On click, the rrc histiory needs to displayed.
							 * */
							popup = new com.vaadin.ui.Window();
							
							QuantumReductionDetailsDTO dto = (QuantumReductionDetailsDTO)itemId;
						
							//viewClaimWiseRRCHistoryPage.init(bean,popup);
							bean.setSignificantClinicalInformationValue(dto.getSignificantClinicalInformationValue());
							bean.setRemarks(dto.getRequestRemarks());
							viewClaimWiseRRCHistoryPage.init(bean, popup,dto.getRequestNo());
							viewClaimWiseRRCHistoryPage.initPresenter(SHAConstants.VIEW_RRC_REQUEST);
							viewClaimWiseRRCHistoryPage.getContent();
							
							popup.setCaption("View Claim Wise RRC History");
							popup.setWidth("75%");
							popup.setHeight("85%");
							popup.setContent(viewClaimWiseRRCHistoryPage);
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
									System.out.println("Close listener called");
								}
							});

							popup.setModal(true);
							UI.getCurrent().addWindow(popup);
						}
						
					});
	
					return viewButton;
				}
			});
		}
		
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		table.setFooterVisible(true);
		table.setColumnFooter("category", String.valueOf("Total"));

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
				BeanItem<QuantumReductionDetailsDTO> addItem = container.addItem(new QuantumReductionDetailsDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	
	public List<QuantumReductionDetailsDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<QuantumReductionDetailsDTO> itemIds = new ArrayList<QuantumReductionDetailsDTO>();
		if (this.table != null) {
			itemIds = (List<QuantumReductionDetailsDTO>) this.table.getItemIds();
		}

		return itemIds;
	}
	
	public void manageListeners() {

		for (QuantumReductionDetailsDTO quantumReductionDetailsDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(quantumReductionDetailsDTO);
			
			final ComboBox classificationCombo = (ComboBox) combos.get("classification");
			final ComboBox categoryCombo = (ComboBox)combos.get("category");
			final TextField txtPerDay = (TextField) combos.get("perDayAmt");
		}
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			QuantumReductionDetailsDTO entryDTO = (QuantumReductionDetailsDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				
			if ("slNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("75px");
				//field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setMaxLength(50);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("slNo", field);
				return field;
			}
			else if ("requestNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("180px");
				field.setNullRepresentation("");
				if(isEnabled())
				{
					field.setEnabled(true);
				}
				else
				{
					field.setEnabled(false);
				}
				field.setData(entryDTO);
				if((SHAConstants.VIEW_RRC_REQUEST).equalsIgnoreCase(presenterString)
						|| (SHAConstants.VIEW_CLAIM_WISE_RRC_HISTORY).equalsIgnoreCase(presenterString)
						|| (SHAConstants.RRC_STATUS_SCREEN).equalsIgnoreCase(presenterString)
						|| (SHAConstants.SEARCH_RRC_REQUEST).equalsIgnoreCase(presenterString)){
					field.setEnabled(false);
				}
			
			/*	CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);*/
				tableRow.put("requestNo", field);

				return field;
			}
			else if ("preAuthAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("90px");
				field.setNullRepresentation("");
				if(isEnabled())
				{
					field.setEnabled(true);
				}
				else
				{
					field.setEnabled(false);
				}
				if(entryDTO.getPreAuthAmount() !=null){
					field.setValue(entryDTO.getPreAuthAmount().toString());
				}
				if((SHAConstants.VIEW_RRC_REQUEST).equalsIgnoreCase(presenterString)
						|| (SHAConstants.VIEW_CLAIM_WISE_RRC_HISTORY).equalsIgnoreCase(presenterString)
						|| (SHAConstants.RRC_STATUS_SCREEN).equalsIgnoreCase(presenterString)
						|| (SHAConstants.SEARCH_RRC_REQUEST).equalsIgnoreCase(presenterString)){
					field.setEnabled(false);
				}
				field.setData(entryDTO);
				field.setMaxLength(15);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("preAuthAmount", field);
				return field;
				
				/*GComboBox box = new GComboBox();
				box.setWidth("140px");
				tableRow.put("preAuthAmount", box);
				box.setData(entryDTO);
				return box;*/
			}
			/*else if ("finalBillAmount".equals(propertyId)) {
		
				TextField field = new TextField();
				field.setWidth("90px");
				field.setNullRepresentation("");
				if(isEnabled())
				{
					field.setEnabled(true);
				}
				else
				{
					field.setEnabled(false);
				}
				field.setData(entryDTO);
				field.setMaxLength(15);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("finalBillAmount", field);

				return field;
				
				GComboBox box = new GComboBox();
				box.setWidth("150px");
				tableRow.put("finalBillAmount", box);
				box.setData(entryDTO);
				return box;
			}*/
			else if("settlementAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("90px");
				field.setNullRepresentation("");
				field.setMaxLength(15);
				field.setData(entryDTO);
				if(isEnabled())
				{
					field.setEnabled(true);
				}
				if((SHAConstants.VIEW_RRC_REQUEST).equalsIgnoreCase(presenterString)
						|| (SHAConstants.VIEW_CLAIM_WISE_RRC_HISTORY).equalsIgnoreCase(presenterString)
						|| (SHAConstants.RRC_STATUS_SCREEN).equalsIgnoreCase(presenterString)
						|| (SHAConstants.SEARCH_RRC_REQUEST).equalsIgnoreCase(presenterString)){
					field.setEnabled(false);
				}
				field.addBlurListener(getCalculateAmtListener());
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9. ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("settlementAmount", field);
				final TextField txt = (TextField) tableRow.get("slNo");
				generateSlNo(txt);
				return field;
			}
			else if("anh".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("90px");
				addAnhValues(box);
				box.setFilteringMode(FilteringMode.CONTAINS);
				box.setTextInputAllowed(true);
				box.setNullSelectionAllowed(true);
				tableRow.put("anh", box);
				box.setData(entryDTO);
				if(entryDTO.getAnh() !=null){
					box.setEnabled(false);
				}
				return box;
			}
			else if("savedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("90px");
				field.setNullRepresentation("");
				field.setMaxLength(15);
				if(isEnabled())
				{
					field.setEnabled(true);
				}
				else
				{
					field.setEnabled(false);
				}
				if((SHAConstants.VIEW_RRC_REQUEST).equalsIgnoreCase(presenterString)
						|| (SHAConstants.VIEW_CLAIM_WISE_RRC_HISTORY).equalsIgnoreCase(presenterString)
						|| (SHAConstants.RRC_STATUS_SCREEN).equalsIgnoreCase(presenterString)
						|| (SHAConstants.SEARCH_RRC_REQUEST).equalsIgnoreCase(presenterString)){
					field.setEnabled(false);
				}
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("savedAmount", field);
				field.addValueChangeListener(savedAmountValidationListener(field));						
				
				return field;
			}
			else if("diagnosis".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("90px");
				field.setNullRepresentation("");
				field.setMaxLength(50);
				if(isEnabled())
				{
					field.setEnabled(true);
				}
				else
				{
					field.setEnabled(false);
				}
				if((SHAConstants.VIEW_RRC_REQUEST).equalsIgnoreCase(presenterString)
						|| (SHAConstants.VIEW_CLAIM_WISE_RRC_HISTORY).equalsIgnoreCase(presenterString)
						|| (SHAConstants.RRC_STATUS_SCREEN).equalsIgnoreCase(presenterString)
						|| (SHAConstants.SEARCH_RRC_REQUEST).equalsIgnoreCase(presenterString)){
					field.setEnabled(false);
				}
				if(entryDTO.getDiagnosis() !=null &&
						!entryDTO.getDiagnosis().isEmpty()){
					field.setDescription(entryDTO.getDiagnosis());
				}
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9 /]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("diagnosis", field);
				return field;
			}
			else if("management".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("90px");
				field.setNullRepresentation("");
				field.setMaxLength(50);
				if(isEnabled())
				{
					field.setEnabled(true);
				}
				else
				{
					field.setEnabled(false);
				}
				
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9 /]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("management", field);
				if(entryDTO.getManagement() !=null){
					field.setEnabled(false);
				}
				return field;
			}
			else if ("eligiblity".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("90px");
				field.setNullRepresentation("");
				if(isEnabled())
				{
					field.setEnabled(true);
				}
				else
				{
					field.setEnabled(false);
				}
				field.setData(entryDTO);				
				tableRow.put("eligiblity", field);
				return field;
				
				/*GComboBox box = new GComboBox();
				box.setWidth("140px");
				tableRow.put("preAuthAmount", box);
				box.setData(entryDTO);
				return box;*/
			}
			else if ("requestedSavedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("90px");
				field.setNullRepresentation("");
				if(isEnabled())
				{
					field.setEnabled(true);
				}
				else
				{
					field.setEnabled(false);
				}
				field.setData(entryDTO);				
				tableRow.put("requestedSavedAmount", field);
				return field;
				
				/*GComboBox box = new GComboBox();
				box.setWidth("140px");
				tableRow.put("preAuthAmount", box);
				box.setData(entryDTO);
				return box;*/
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

	
	 public ValueChangeListener savedAmountValidationListener(final TextField component)
	 {
		 ValueChangeListener listener =  new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				String value = (String) event.getProperty().getValue();
				if(null != value)
				{
					String str = value.replaceAll(",","");
					if(null != str && (Double.valueOf(str) > Double.valueOf(bean.getSavedAmount())))
					{
						String err = "Saved Amount Should not be exceed";
						showErrorMessage(err);
						event.getProperty().setValue(bean.getSavedAmount());
					}
				}
				
			}
		};
		return listener;
	 }
	 
		private void showErrorMessage(String eMsg) {
			/*Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
			
		}
		

	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<QuantumReductionDetailsDTO> itemIds = (Collection<QuantumReductionDetailsDTO>) table.getItemIds();
		
		int i = 0;
		 for (QuantumReductionDetailsDTO quantumReductionDetailsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(quantumReductionDetailsDTO);
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
	
	 public void addBeanToList(QuantumReductionDetailsDTO quantumReductionDetailsDTO) {
		 container.addItem(quantumReductionDetailsDTO);
	    }
	
	 public boolean isValid()
		{
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			Collection<QuantumReductionDetailsDTO> itemIds = (Collection<QuantumReductionDetailsDTO>) table.getItemIds();
			/*Map<Long, String> valuesMap = new HashMap<Long, String>();
			Map<Long, String> validationMap = new HashMap<Long, String>();*/
			if(null != itemIds && !itemIds.isEmpty())
			{
				for (QuantumReductionDetailsDTO quantumReductionDetailsDTO : itemIds) {
					
					if(null == quantumReductionDetailsDTO.getPreAuthAmount())
					{
						hasError = true;
						errorMessages.add("Please enter Pre Auth Amount");
					}
					/*if(null == quantumReductionDetailsDTO.getFinalBillAmount())
					{
						hasError = true;
						errorMessages.add("Please enter Final Bill Amount");
					}*/
					if(null == quantumReductionDetailsDTO.getSettlementAmount())
					{
						hasError = true;
						errorMessages.add("Please enter settlement Amount");
					}
					/*if(null == quantumReductionDetailsDTO.getAnh())
					{
						hasError = true;
						errorMessages.add("Please enter Anh Amount");
					}*/
					if(null == quantumReductionDetailsDTO.getSavedAmount())
					{
						hasError = true;
						errorMessages.add("Please enter Saved Amount");
					}
					if(null == quantumReductionDetailsDTO.getDiagnosis() || ("").equalsIgnoreCase(quantumReductionDetailsDTO.getDiagnosis()))
					{
						hasError = true;
						errorMessages.add("Please enter Diagnosis");
					}
					if(null == quantumReductionDetailsDTO.getManagement() || ("").equalsIgnoreCase(quantumReductionDetailsDTO.getManagement()))
					{
						hasError = true;
						errorMessages.add("Please enter Management");
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
				 errorMessages.add("Please enter atleast one quantum reduction details data");
			}
			
				return !hasError;
		}
	 
	 public List<String> getErrors()
	 {
		 return this.errorMessages;
	 }

	 private void addAnhValues(ComboBox box) {
		 BeanItemContainer<SelectValue> AnhValues = (BeanItemContainer<SelectValue>)SHAUtils.getSelectValueForANH();
		 box.setContainerDataSource(AnhValues);
		 box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 box.setItemCaptionPropertyId("value");
	 }
	 public BlurListener getCalculateAmtListener() {

		 BlurListener listener = new BlurListener() {
			 private static final long serialVersionUID = 1L;

			 @Override
			 public void blur(BlurEvent event) {
				 TextField component = (TextField) event.getComponent();
				 calculationMethodForSavedAmt(component);

			 }
		 };
		 return listener;
	 }

	 @SuppressWarnings("deprecation")
	 private void calculationMethodForSavedAmt(Component component){
		 QuantumReductionDetailsDTO quantumReductionDetailsDTO =new QuantumReductionDetailsDTO();
		 TextField txtField  = (TextField) component;
		 quantumReductionDetailsDTO = (QuantumReductionDetailsDTO)txtField.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(quantumReductionDetailsDTO);
		 TextField claimedAmt = (TextField) hashMap.get("preAuthAmount");
		 TextField approvedAmt = (TextField) hashMap.get("settlementAmount");
		 TextField savedAmt = (TextField) hashMap.get("savedAmount");

		 if(claimedAmt !=  null && approvedAmt != null && claimedAmt.getValue() != null && approvedAmt.getValue() != null 
				 && !claimedAmt.getValue().isEmpty() && !approvedAmt.getValue().isEmpty()){

			 Integer claimed = Integer.valueOf(claimedAmt.getValue().replaceAll(",",""));
			 Integer approved = Integer.valueOf(approvedAmt.getValue().replaceAll(",",""));
			 if(claimed >= approved){
				 Integer saved = claimed - approved;
				 if(saved != null){
					 savedAmt.setReadOnly(false);
					 bean.setSavedAmount(saved.toString());
					 savedAmt.setValue(String.valueOf(saved));
				 }
			 }else{
				 String err = "Approved Amount Should not be exceed Claimed Amount";
				 showErrorMessage(err);
				 bean.setSavedAmount(String.valueOf(claimed));
				 savedAmt.setValue(String.valueOf(claimed));
				 approvedAmt.setValue("0");	
			 }
		 }
	 }
	 
	 public void clearObjects(){
			clearTableItem(tableItem);
			SHAUtils.setClearReferenceData(referenceData);
			errorMessages = null;
			validator = null;
			presenterString = null;
			popup = null;
			bean = null;
			if(viewClaimWiseRRCHistoryPage != null){
				viewClaimWiseRRCHistoryPage.clearObjects();
			}
				
	 }
	 
	 private void clearTableItem(Map<QuantumReductionDetailsDTO, HashMap<String, AbstractField<?>>> referenceData){
			
			if(referenceData != null){
	    	
		    	Iterator<Entry<QuantumReductionDetailsDTO, HashMap<String, AbstractField<?>>>> iterator = referenceData.entrySet().iterator();
		    	//referenceData.clear();
		    	try{
			        while (iterator.hasNext()) {
			            Map.Entry pair = (Map.Entry)iterator.next();
			            Object object = pair.getValue();
			            object = null;
			            pair = null;	
			        }
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
		       referenceData.clear();
		       referenceData = null;
			}
	    	
	    }

}
