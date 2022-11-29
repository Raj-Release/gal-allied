package com.shaic.claim.viewEarlierRodDetails.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.processdatacorrection.dto.ImplantCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.reports.negotiationreport.SearchUpdateNegotiationPresenter;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class ViewHospitalizationListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<BillEntryDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<BillEntryDetailsDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<BillEntryDetailsDTO> container = new BeanItemContainer<BillEntryDetailsDTO>(BillEntryDetailsDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	//private Map<String, Object> referenceData;
	
	
	
	//private List<String> errorMessages;
	
	//private static Validator validator;
	

	
	//This value will be used for validation.
	public Double totalBillValue;
	
	public Double totalNetPayableAmt;
	
	//private int iItemValue = 0;
	
	private String presenterString = "";
	
	//private Double pdtPerDayAmt ;
	
	private Long productKey;
	private VerticalLayout vLayout ;
	private Boolean isView;
	
	private Boolean isChecked = false;
	
	private Double propDedPer =100d;
	
	@Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	public void initPresenter(String presenterString,Boolean isView) {
		this.presenterString = presenterString;
		this.isView = isView;
	}
	
	public void init(Long productKey)
	{
		this.productKey = productKey;
		//this.presenterString = presenterString;
		init();
	}
	
	public void setGMCPropPercentage(Double proportionalValue)
	{
		this.propDedPer = proportionalValue;
	}
	 
	
	public void init() {
		
		container.removeAllItems();
		vLayout = new VerticalLayout();
		//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		//validator = factory.getValidator();
		//this.errorMessages = new ArrayList<String>();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		//layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("100%");
		table.setHeight("400px");
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	public void setTableList(final List<BillEntryDetailsDTO> list) {
		table.removeAllItems();
		for (final BillEntryDetailsDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	@SuppressWarnings("deprecation")
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());	
		if(productKey.equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)||productKey.equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY)){
			table.addGeneratedColumn("proportionateDeductionappl", new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source, final Object itemId, Object columnId) {
					final CheckBox chkBox = new CheckBox();
					BillEntryDetailsDTO billDTO = (BillEntryDetailsDTO) itemId;
					chkBox.setValue(billDTO.getIsproportionateDeductionSelected());
					chkBox.setEnabled(billDTO.getProportionateDeductionappl());
					chkBox.setVisible(billDTO.getIsproportionateDeductionvisble());
					chkBox.addValueChangeListener(new Property.ValueChangeListener() {				
						@Override
						public void valueChange(ValueChangeEvent event) {							
							boolean value = (Boolean) event.getProperty().getValue();
							if(!value){
								checkBoxValueChangeListenerUnselected(billDTO,value,propDedPer);
								isChecked = true;
							}else if(value){
								checkBoxValueChangeListenerForSelected(billDTO,value,propDedPer);
								isChecked = true;
							}
							
						}
					});
					return chkBox;
				}
			});
		}
		if(null != this.presenterString  && (SHAConstants.BILLING.equalsIgnoreCase(this.presenterString) || (SHAConstants.FINANCIAL).equalsIgnoreCase(this.presenterString))){
			if(productKey.equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)||productKey.equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY)){
				table.setVisibleColumns(new Object[] { "itemNoForView", "itemName","proportionateDeductionappl","strRoomType","noOfDays", "perDayAmt", "itemValue" , "noOfDaysAllowed" , "perDayAmtProductBased" , "amountAllowableAmount" , "nonPayableProductBased" , "nonPayable" , "proportionateDeduction" ,"reasonableDeduction" , "totalDisallowances" , "netPayableAmount" , "deductibleOrNonPayableReason"});
			}else{			
				table.setVisibleColumns(new Object[] { "itemNoForView", "itemName","strRoomType","noOfDays", "perDayAmt", "itemValue" , "noOfDaysAllowed" , "perDayAmtProductBased" , "amountAllowableAmount" , "nonPayableProductBased" , "nonPayable" , "proportionateDeduction" ,"reasonableDeduction" , "totalDisallowances" , "netPayableAmount" , "deductibleOrNonPayableReason","deductibleNonPayableReasonBilling","deductibleNonPayableReasonFA"});
			}
		}
		else{
			table.setVisibleColumns(new Object[] { "itemNoForView", "itemName","strRoomType","noOfDays", "perDayAmt", "itemValue" , "noOfDaysAllowed" , "perDayAmtProductBased" , "amountAllowableAmount" , "nonPayableProductBased" , "nonPayable" , "proportionateDeduction" ,"reasonableDeduction" , "totalDisallowances" , "netPayableAmount" , "deductibleOrNonPayableReason"});
		}
		
		if (null != isView && isView)
		{
			table.setVisibleColumns(new Object[] { "itemNoForView", "itemName","strRoomType","noOfDays", "perDayAmt", "itemValue" , "noOfDaysAllowed" , "perDayAmtProductBased" , "amountAllowableAmount" , "nonPayableProductBased" , "nonPayable" , "proportionateDeduction" ,"reasonableDeduction" , "totalDisallowances" , "netPayableAmount" , "deductibleOrNonPayableReason","deductibleNonPayableReasonBilling","deductibleNonPayableReasonFA"});
		}
		
		//Setting the names for the columns present in table
		table.setColumnHeader("itemNoForView", "Sl No");
		table.setColumnHeader("itemName", "Details");
		
		if(productKey.equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)||productKey.equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY)){
			table.setColumnHeader("proportionateDeductionappl", "Applicability </br> of </br> Proportionate </br> Deduction");
			table.setColumnAlignment("proportionateDeductionappl",Align.CENTER);
		}
		
		table.setColumnHeader("strRoomType", "Room </br> Type");
		table.setColumnWidth("strRoomType", 160);
		table.setColumnHeader("noOfDays", "No of </br> Days Claimed");
		table.setColumnHeader("perDayAmt", "Per </br> Day </br> Amt");
		table.setColumnHeader("itemValue", "Claimed </br> Amount </br> (C) = A*B");
		table.setColumnWidth("itemValue", 100);
		table.setColumnHeader("noOfDaysAllowed", "No </br> of </br>Days</br>Allowed");
		table.setColumnHeader("perDayAmtProductBased", "Per </br> Day </br> Amt </br> (Product Based)");
		table.setColumnHeader("amountAllowableAmount", "Amount");
		table.setColumnWidth("amountAllowableAmount", 100);
		table.setColumnHeader("nonPayableProductBased", "Non </br> payable </br> (Product Based)");
		table.setColumnHeader("nonPayable","Non </br> Payable");
		table.setColumnHeader("proportionateDeduction","Proportionate </br> Deduction");
		table.setColumnHeader("reasonableDeduction", "Reasonable </br> Deduction");
		table.setColumnHeader("totalDisallowances", "Total </br> Disallowances");
		table.setColumnHeader("netPayableAmount", "Net </br> Payable </br> Amt");
		table.setColumnHeader("deductibleOrNonPayableReason", "Deductible / </br> Non </br>Payables </br> Reason");
		table.setColumnHeader("deductibleNonPayableReasonBilling","Deductible / </br> Non </br>Payables </br> Reason - Billing");
		table.setColumnHeader("deductibleNonPayableReasonFA","Deductible / </br> Non </br>Payables </br> Reason - FA");
	//	table.setColumnHeader("medicalRemarks", "Medical </br> Remarks");
		table.setEditable(true);
		
		
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		if(!(ReferenceTable.MICRO_INSURANCE_GROUP.equals(productKey) || ReferenceTable.MICRO_INSURANCE_INDIVIDUAL.equals(productKey)))
		{
			calculateTotal();
		}
		table.setFooterVisible(true);
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
				//BeanItem<BillEntryDetailsDTO> addItem = container.addItem(new BillEntryDetailsDTO());
				container.addItem(new BillEntryDetailsDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	
	
	public void manageListeners() {

		/*for (BillEntryDetailsDTO billEntryDetailsDTO : tableItem.keySet()) {
			//HashMap<String, AbstractField<?>> combos = tableItem.get(billEntryDetailsDTO);
			
			final ComboBox classificationCombo = (ComboBox) combos.get("classification");
			final ComboBox categoryCombo = (ComboBox)combos.get("category");
			final TextField txtPerDay = (TextField) combos.get("perDayAmt");
			
			//addClassificationListener(classificationCombo,categoryCombo);
			//calculateItemValue(txtPerDay);
			
			
			

		}*/
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			BillEntryDetailsDTO entryDTO = (BillEntryDetailsDTO) itemId;
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
			
			if ("itemName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("290px");
				field.setNullRepresentation("");
				field.setMaxLength(50);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("itemName", field);
				return field;
			}
			else if ("strRoomType".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("290px");
				field.setNullRepresentation("");
				field.setMaxLength(50);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("itemName", field);
				return field;
			}
			
			else if ("itemNoForView".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("30px");
				field.setNullRepresentation("");
				field.setMaxLength(6);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("itemNoForView", field);

				return field;
			}
			else if("noOfDays".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("30px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("noOfDays", field);
				/*if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					{
						field.setEnabled(true);
					}
					else
					{
						field.setEnabled(false);
						
					}
				}*/
				//field.addBlurListener(getNoOfDaysListener(/*"AmountClaimed"*/));
				//final TextField txt = (TextField) tableRow.get("itemNo");
				//generateSlNo(txt);
				return field;
			}
			else if("perDayAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				//field.setMaxLength(10);
				field.setData(entryDTO);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("perDayAmt", field);
			/*	if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					{
						field.setEnabled(true);
					}
					else
					{
						field.setEnabled(false);
						
					}
				}
				
				field.addBlurListener(getPerDayAmtListener("AmountClaimed"));*/
				return field;
			}
			else if("itemValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				
				//Added for process claim billing bill entry screen. 
				field.setData(entryDTO);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			/*	CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);*/
				tableRow.put("itemValue", field);
			//	valueChangeLisenerForText(field);
				//calculateTotal();
				//enableOrDisableFields(field,"itemValue");
				return field;
			}
			else if("noOfDaysAllowed".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("30px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);
				/*if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					{
						field.setEnabled(true);
					}
					else
					{
						field.setEnabled(false);
						
					}
				}
				
				field.addBlurListener(getNoOfDaysAllowableListener());*/
				tableRow.put("noOfDaysAllowed", field);
				return field;
			}
			else if("perDayAmtProductBased".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setMaxLength(10);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
/*				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);*/
				/*field.addBlurListener(getProductBasedPerDayAmtListener());
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					{
						field.setEnabled(true);
						if(null != entryDTO.getProductBasedRoomRent())
						{
							String roomRent = String.valueOf(entryDTO.getProductBasedRoomRent()); 
							field.setValue(roomRent);
						}
					}
					else
					{
						field.setEnabled(false);
						
					}
				}*/
				//Vaadin8-setImmediate() field.setImmediate(true);
				tableRow.put("perDayAmtProductBased", field);
				return field;
			}
			else if("amountAllowableAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("amountAllowableAmount", field);
				
				/*if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					{
						field.setEnabled(true);
					}
					else
					{
						field.setEnabled(false);
						
					}
				}*/
				
				//final TextField txtFld = (TextField) tableRow.get("perDayAmtProductBased");
				
				//valueChangeLisenerForAllowableAmount(field);
				//calculateAllowableAmountTotal();
				//enableOrDisableFields(field,"amountAllowableAmount");
				//populateProductBasedPerDayAmt();
				
			//	field.addBlurListener(getNoOfDaysListener("AmountEntitled"));
				return field;
			}
			else if("nonPayableProductBased".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
//				field.setWidth("50%");
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.addBlurListener(getNonPayablePdtBasedListener());
				tableRow.put("nonPayableProductBased", field);
				return field;
			}

			else if("nonPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setMaxLength(15);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);
			//	field.addBlurListener(getNonPayableListener());
				tableRow.put("nonPayable", field);
				return field;
			}
			else if("proportionateDeduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setMaxLength(15);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);
			//	field.addBlurListener(getNonPayableListener());
				tableRow.put("proportionateDeduction", field);
				return field;
			}
			else if("reasonableDeduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(15);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);
				//field.addBlurListener(getResonableDeductionListener());
				tableRow.put("reasonableDeduction", field);
				return field;
			}
			else if("totalDisallowances".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("80px");
				field.setNullRepresentation("");
				//field.setMaxLength(10);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//valueChangeLisenerForTotalDisallowances(field);
			//	calculateNetPayableAmount(field);
				tableRow.put("totalDisallowances", field);
				return field;
			}
			
			else if ("netPayableAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//valueChangeLisenerForNetPayableAmount(field);
				tableRow.put("netPayableAmount", field);
				return field;
			}
			else if("deductibleOrNonPayableReason".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setMaxLength(1000);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//				CSValidator validator = new CSValidator();
//				validator.extend(field);
//				validator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
//				validator.setPreventInvalidTyping(true);
				tableRow.put("deductibleOrNonPayableReason", field);
				addDescriptionFromRemarksFld(field);
				return field;
			} //deductibleNonPayableReasonBilling
			else if("deductibleNonPayableReasonBilling".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setMaxLength(4000);
				field.setMaxLength(1000);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				if (SHAConstants.BILLING.equalsIgnoreCase(presenterString))
						{
							field.setEnabled(true);
							field.setReadOnly(false);
							//field.setReadOnly(true);
						}
				else if (SHAConstants.FINANCIAL.equalsIgnoreCase(presenterString))
				{
					field.setEnabled(true);
					field.setReadOnly(true);
					//field.setReadOnly(true);
				}
				
				 if(null != isView && isView)
				 {
						field.setEnabled(false);
						field.setReadOnly(true);
						field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
						//field.setReadOnly(true);
					} 
				
				
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//				CSValidator validator = new CSValidator();
//				validator.extend(field);
//				validator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
//				validator.setPreventInvalidTyping(true);
				tableRow.put("deductibleNonPayableReasonBilling", field);
				field.setDescription("Click the Text Box and Press F8 For Detailed Popup");
				handleEnterForBillingReason(field,null);
				return field;
			}
			else if("deductibleNonPayableReasonFA".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setMaxLength(4000);
				field.setMaxLength(1000);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				
				if (SHAConstants.BILLING.equalsIgnoreCase(presenterString))
				{
					field.setEnabled(true);
					field.setReadOnly(true);
					//field.setReadOnly(true);
				}
				else if (SHAConstants.FINANCIAL.equalsIgnoreCase(presenterString))
				{
					field.setEnabled(true);
					field.setReadOnly(false);
					//field.setReadOnly(true);
				}
				
				 if(null != isView && isView)
				 {
						field.setEnabled(false);
						field.setReadOnly(true);
						field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
						//field.setReadOnly(true);
					} 
				
				/*field.setEnabled(true);
				field.setReadOnly(true);*/
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//				CSValidator validator = new CSValidator();
//				validator.extend(field);
//				validator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
//				validator.setPreventInvalidTyping(true);
				tableRow.put("deductibleNonPayableReasonFA", field);
				field.setDescription("Click the Text Box and Press F8 For Detailed Popup");
				handleEnterForFAReason(field,null);
				return field;
			}
			
			else if("medicalRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setData(entryDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//				CSValidator validator = new CSValidator();
//				validator.extend(field);
//				validator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
//				validator.setPreventInvalidTyping(true);
				tableRow.put("medicalRemarks", field);
			
				return field;
			}else if ("strRoomType".equals(propertyId)) {
				CheckBox field = new CheckBox();
				field.setEnabled(true);
				field.setReadOnly(true);				
				tableRow.put("strRoomType", field);
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

	
	private void addDescriptionFromRemarksFld(TextField txtFld)
	{
		
		BillEntryDetailsDTO billEntryDTO = (BillEntryDetailsDTO) txtFld.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDTO);
		 if(null != hashMap && !hashMap.isEmpty())
		 {
			 TextField dedcutibleOrNonPayableReason = (TextField)hashMap.get("deductibleOrNonPayableReason");
			 TextField dedcutibleOrNonPayableReasonFA = (TextField)hashMap.get("deductibleNonPayableReasonFA");
			 if(null != dedcutibleOrNonPayableReason)
			 {
				 
				 dedcutibleOrNonPayableReason.setDescription(billEntryDTO.getDeductibleOrNonPayableReason());
				/* itemNoFld.setReadOnly(false);
				 itemNoFld.setValue(String.valueOf(i));
				 itemNoFld.setReadOnly(true);*/
				 //itemNoFld.setEnabled(false);
			 }
			 if(null != dedcutibleOrNonPayableReasonFA)
			 {
				 
				 dedcutibleOrNonPayableReasonFA.setDescription(billEntryDTO.getDeductibleNonPayableReasonFA());
				/* itemNoFld.setReadOnly(false);
				 itemNoFld.setValue(String.valueOf(i));
				 itemNoFld.setReadOnly(true);*/
				 //itemNoFld.setEnabled(false);
			 }
		 }
	}
	
	public void calculateTotal(){
		
		List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
		//Long netAmount =0l;
		Double claimedAmount = 0d;
		Double allowableAmount = 0d;
		Double nonPayablePdtBased = 0d;
		Double nonPayableAmount = 0d;
		Double proportionateDeduction = 0d;
		Double totalDisallowances = 0d;
		Double reasonableDeduction = 0d;
		Double netAmount = 0d;
		/*Long amount =0l;
		Long nonPayableAmount =0l;
		Long payableAmount =0l;*/
		for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
			
			if(null != billEntryDetailsDTO.getItemValue())
			{
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
						{
							claimedAmount += billEntryDetailsDTO.getItemValue();
							if(("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getItemName()) || ("Network Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getItemName()))
							{
								claimedAmount -= billEntryDetailsDTO.getItemValue();
							}
						}
			}
			
			if(null != billEntryDetailsDTO.getAmountAllowableAmount())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges"))))
						{
							allowableAmount += billEntryDetailsDTO.getAmountAllowableAmount();
						}
			}
			
			if(null != billEntryDetailsDTO.getNonPayableProductBased())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName() != null && billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") 
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							nonPayablePdtBased += billEntryDetailsDTO.getNonPayableProductBased();
						}
			}
			
			if(null != billEntryDetailsDTO.getNonPayable())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							nonPayableAmount += billEntryDetailsDTO.getNonPayable();
						}
			}
			
			if(null != billEntryDetailsDTO.getProportionateDeduction())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							proportionateDeduction += billEntryDetailsDTO.getProportionateDeduction();
						}
			}
			
			if(null != billEntryDetailsDTO.getReasonableDeduction())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))
						{
							reasonableDeduction += billEntryDetailsDTO.getReasonableDeduction();
						}
			}
			
			if(null != billEntryDetailsDTO.getTotalDisallowances())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				/*if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						)))*/
				if(!( billEntryDetailsDTO.getItemName().contains("Total Room Rent")
						|| billEntryDetailsDTO.getItemName().contains("Total ICU Charges")
						))
						{
							totalDisallowances += billEntryDetailsDTO.getTotalDisallowances();
						}
			}
			if(null != billEntryDetailsDTO.getNetPayableAmount())
			{
				//if(!(billEntryDetailsDTO.getItemName().contains("Sub Total")))
				if(!((billEntryDetailsDTO.getItemName().contains("Sub Total"))))// || billEntryDetailsDTO.getItemName().contains("Total Room Rent"))))
						{
							netAmount += billEntryDetailsDTO.getNetPayableAmount();
						}
			}
			
		}
		/*if(null != nonPayablePdtBased)
		{
			 long lValue = (long) nonPayablePdtBased.longValue();
			 Double decimalVal = nonPayablePdtBased - lValue;
			 if(decimalVal> 0.5)
			 {
					 nonPayablePdtBased = Double.valueOf(String.valueOf(lValue+1));
				 
			 }
			 else
			 {
				
				 nonPayablePdtBased = Double.valueOf(String.valueOf(lValue));
			 }
		}*/

		table.setColumnFooter("itemValue", String.valueOf(claimedAmount));
		table.setColumnFooter("amountAllowableAmount" , String.valueOf(allowableAmount));
		table.setColumnFooter("nonPayableProductBased" , String.valueOf(nonPayablePdtBased));
		table.setColumnFooter("nonPayable" , String.valueOf(nonPayableAmount));
		table.setColumnFooter("proportionateDeduction"  , String.valueOf(proportionateDeduction));
		table.setColumnFooter("reasonableDeduction" , String.valueOf(reasonableDeduction));
		table.setColumnFooter("totalDisallowances"  , String.valueOf(totalDisallowances));
		netAmount = netAmount > 0 ? netAmount : 0;
		table.setColumnFooter("netPayableAmount"  , String.valueOf(netAmount));

		/*table.setColumnFooter("amount", String.valueOf(amount));
		table.setColumnFooter("deductingNonPayable", String.valueOf(nonPayableAmount));
		table.setColumnFooter("payableAmount", String.valueOf(payableAmount));*/
		table.setColumnFooter("itemName", "Total");
		
		
		
 }
	
	
	public Map<String, Double> getFooterValues(){
		
		
		try{
		Map<String,Double> map = new HashMap<String, Double>();
		map.put(SHAConstants.NONPAYABLE_PRODUCT_BASED, Double.valueOf(table.getColumnFooter("nonPayableProductBased").toString()));
		map.put(SHAConstants.NONPAYABLE, Double.valueOf(table.getColumnFooter("nonPayable").toString()));
		map.put(SHAConstants.PROPORTION_DEDUCTION_AMOUNT, Double.valueOf(table.getColumnFooter("proportionateDeduction").toString()));
		map.put(SHAConstants.REASONABLE_DEDUCTION, Double.valueOf(table.getColumnFooter("reasonableDeduction").toString()));
		map.put(SHAConstants.TOTALDISALLOWANCE, Double.valueOf(table.getColumnFooter("totalDisallowances").toString()));
		map.put(SHAConstants.NET_AMOUNT, Double.valueOf(table.getColumnFooter("netPayableAmount").toString()));
		map.put(SHAConstants.TOTAL_CLAIMED_AMOUNT, Double.valueOf(table.getColumnFooter("itemValue").toString()));
		map.put(SHAConstants.AMOUNT, Double.valueOf(table.getColumnFooter("amountAllowableAmount").toString()));
		
		return map;
		}catch(Exception e){	
			return null;
		}
		
	}

	
	
	
public void calculateTotalForSeniorCitizen(Double claimedAmount ,Double productBasedPerDayAmt , Double allowableAmount , Double nonPayablePdtBased, Double nonPayableAmount ,
		Double proportionateDeduction , Double reasonableDeduction , Double totalDisallowances, Double netAmount){
		
		
		table.setColumnFooter("itemValue", String.valueOf(claimedAmount));
		if(null != productBasedPerDayAmt)
		table.setColumnFooter("perDayAmtProductBased",String.valueOf (productBasedPerDayAmt));
		table.setColumnFooter("amountAllowableAmount" , String.valueOf(allowableAmount));
		table.setColumnFooter("nonPayableProductBased" , String.valueOf(nonPayablePdtBased));
		table.setColumnFooter("nonPayable" , String.valueOf(nonPayableAmount));
		table.setColumnFooter("proportionateDeduction"  , String.valueOf(proportionateDeduction));
		table.setColumnFooter("reasonableDeduction" , String.valueOf(reasonableDeduction));
		table.setColumnFooter("totalDisallowances"  , String.valueOf(totalDisallowances));
		netAmount = netAmount > 0 ? netAmount : 0;
		table.setColumnFooter("netPayableAmount"  , String.valueOf(netAmount));

		/*table.setColumnFooter("amount", String.valueOf(amount));
		table.setColumnFooter("deductingNonPayable", String.valueOf(nonPayableAmount));
		table.setColumnFooter("payableAmount", String.valueOf(payableAmount));*/
		table.setColumnFooter("itemName", "Total");
		
}
	
	
	/*private void generateSlNo(TextField txtField)
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
					 itemNoFld.setReadOnly(false);
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setReadOnly(true);
					 itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}*/
	

	/*private void populateProductBasedPerDayAmt()
	{
		
		Collection<BillEntryDetailsDTO> itemIds = (Collection<BillEntryDetailsDTO>) table.getItemIds();
		 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField pdtPerDayAmt = (TextField)hashMap.get("perDayAmtProductBased");
				 if(null != pdtPerDayAmt)
				 {
					 if(null != billEntryDetailsDTO.getCategory() && (("ICU Rooms").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()))
							 && null != this.pdtPerDayAmt)
						{
						 	pdtPerDayAmt.setValue(String.valueOf(this.pdtPerDayAmt));
						}
				 }
			 }
		 }
		
	}*/
	
	
	
	 public List<BillEntryDetailsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<BillEntryDetailsDTO> itemIds = (List<BillEntryDetailsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	
		public void valueChangeLisenerForAllowableAmount(final TextField total){
			
			if(null != total)
			{
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					calculateAllowableAmountTotal();
					//calculateTotalAmount(total);
					
				}
			});
			}
		
			
		}
	 	 public BlurListener getResonableDeductionListener() {
				
				BlurListener listener = new BlurListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						//TextField component = (TextField) event.getComponent();
						
					}
				};
				return listener;
				
			}
		 
	 
	 	 

	public void valueChangeLisenerForNetPayableAmount(final TextField total){
		
		if(null != total)
		{
		total
		.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				summationOfNetPayableAmount();
				/*calculateNetPayableAmount(total);*/
				//calculateTotalAmount(total);
				
			}
		});
		}
	}
		 
	 	
	
	 	public void summationOfNetPayableAmount()
		 {
			 List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
			 Double total  = 0d;
			 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
			 {
				 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
					 if(null != billEntryDetailsDTO.getNetPayableAmount())
					 {
						 total += billEntryDetailsDTO.getNetPayableAmount();
					 }
			}
				 total = total > 0 ? total : 0;
			 table.setColumnFooter("netPayableAmount", String.valueOf(total));
			 totalNetPayableAmt = total;
			// totalBillValue = total;
			 }
		 }
	 	 
	 	public String getPayableAmount(){
			 return this.table.getColumnFooter("netPayableAmount");
		 }
	 	
	 	public String getClaimedAmount(){
			 return this.table.getColumnFooter("itemValue");
		 }
	 	
	/* public void calculateTotal()
	 {
		 List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
		 Double total  = 0d;
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
				 if(null != billEntryDetailsDTO.getItemValue())
				 {
					 total += billEntryDetailsDTO.getItemValue();
				 }
		}
		 table.setColumnFooter("itemValue", String.valueOf(total));
		 totalBillValue = total;
		 }
	 }*/
	 
	 public void calculateAllowableAmountTotal()
	 {
		 List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
		 Double total  = 0d;
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
				 if(null != billEntryDetailsDTO.getAmountAllowableAmount())
				 {
					 total += billEntryDetailsDTO.getAmountAllowableAmount();
				 }
		}
		 table.setColumnFooter("amountAllowableAmount", String.valueOf(total));
		 //totalBillValue = total;
		 }
	 }

	 
	 
	 /*private void summationOfTotalDisallowances()
	 {
		 List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
		 Double total  = 0d;
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
				 if(null != billEntryDetailsDTO.getTotalDisallowances())
				 {
					 total += billEntryDetailsDTO.getTotalDisallowances();
				 }
		}
		 table.setColumnFooter("totalDisallowances", String.valueOf(total));
		 //totalBillValue = total;
		 }
	 }*/
	 
	

	 public void addBeanToList(BillEntryDetailsDTO billEntryDetailsDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 container.addItem(billEntryDetailsDTO);
	    }
	 
	 public void removeAllItems()
	 {
		 table.removeAllItems();
	 }
	 
	 
		public  void handleShortcutForBilling(final TextField textField, final ShortcutListener shortcutListener) {
			//textField.addFocusListener(F);
			textField.addFocusListener(new FocusListener() {
				
				@Override
				public void focus(FocusEvent event) {
					//shortcutListener = getShortCutListener(textField);
					//textField.addShortcutListener(getShortCutListener(textField));
					textField.addShortcutListener(shortcutListener);
					
				}
			});
			
		   textField.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				
				/*Collection<?> listeners = textField.getListeners(ShortcutListener.class);
				for (Object object : listeners) {
					textField.removeListener(ShortcutListener.class, object);
				}*/
				
				textField.removeShortcutListener(shortcutListener);
				/*Collection<?> listeners = textField.getListeners(ShortcutListener.class);
				for (Object object : listeners) {
					textField.removeListener(ShortcutListener.class, object);
				}*/
				
			}
		});
		  }
		
		
		
		
		public  void handleEnterForBillingReason(TextField searchField, final  Listener listener) {
			
		    ShortcutListener enterShortCut = new ShortcutListener(
		        "EnterShortcut", ShortcutAction.KeyCode.F8, null) {
		      private static final long serialVersionUID = -2267576464623389044L;
		      @Override
		      public void handleAction( Object sender, Object target) {
		        ((ShortcutListener) listener).handleAction(sender, target);
		      }
		    };
		    
		   // handleShortcut(searchField, enterShortCut);
		    handleShortcutForBilling(searchField, getShortCutListener(searchField));
		  }
		
		private ShortcutListener getShortCutListener(final TextField txtFld) {
			ShortcutListener listener =  new ShortcutListener("EnterShortcut",KeyCodes.KEY_F8,null) {
				private static final long serialVersionUID = 1L;
				@Override
				public void handleAction(Object sender, Object target) {
					final Window dialog = new Window();
					
					VerticalLayout vLayout =  new VerticalLayout();
					vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
					vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
					vLayout.setMargin(true);
					vLayout.setSpacing(true);
					
					final TextArea txtArea = new TextArea();
					txtArea.setMaxLength(1000);
					txtArea.setValue(txtFld.getValue());
					txtArea.setNullRepresentation("");
					txtArea.setRows(21);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					txtArea.setReadOnly(true);
					
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					
					txtArea.addValueChangeListener(new Property.ValueChangeListener() {
						@Override
						public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());
						}
					});
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);
					
					String strCaption = "Deductible / Non Payables Reason - Billing";
					dialog.setCaption(strCaption);
					dialog.setClosable(true);
					dialog.setContent(vLayout);
					dialog.setResizable(true);
					dialog.setModal(true);
					dialog.setDraggable(true);
					dialog.setData(txtFld);
					
					dialog.addCloseListener(new Window.CloseListener() {
						@Override
						public void windowClose(CloseEvent e) {
							dialog.close();
						}
					});
					
					if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
						dialog.setPositionX(250);
						dialog.setPositionY(100);
					}
					getUI().getCurrent().addWindow(dialog);
					
					okBtn.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;
						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();
						}
					});	
				}
			};
			return listener;
		}
		
		public  void handleEnterForFAReason(TextField searchField, final  Listener listener) {
		    ShortcutListener enterShortCut = new ShortcutListener(
		        "EnterShortcutForMedical", ShortcutAction.KeyCode.F7, null) {
		      private static final long serialVersionUID = -2267576464623389045L;
		      @Override
		      public void handleAction(Object sender, Object target) {
		        ((ShortcutListener) listener).handleAction(sender, target);
		      }
		    };
		   // handleShortcutForMedical(searchField, enterShortCut);
		    handleShortcutForFAReason(searchField, getShortCutListenerForFAReason(searchField));
		    
		  }
		
		public  void handleShortcutForFAReason(final TextField textField, final ShortcutListener shortcutListener) {
			//textField.addFocusListener(F);
			textField.addFocusListener(new FocusListener() {
				
				@Override
				public void focus(FocusEvent event) {
					//textField.addShortcutListener(getShortCutListenerForMedicalReason(textField));
					textField.addShortcutListener(shortcutListener);
					
				}
			});
			
		   textField.addBlurListener(new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {/*
				Collection<?> listeners = textField.getListeners(ShortcutListener.class);
				for (Object object : listeners) {
					textField.removeListener(ShortcutListener.class, object);
				}
				
			*/
			textField.removeShortcutListener(shortcutListener);	
			}
		});
		  }
		
		private ShortcutListener getShortCutListenerForFAReason(final TextField txtFld) {
			ShortcutListener listener =  new ShortcutListener("EnterShortcutForMedical",KeyCodes.KEY_F8,null) {
				private static final long serialVersionUID = 1L;
				@Override
				public void handleAction(Object sender, Object target) {
					final Window dialog = new Window();
					
					VerticalLayout vLayout =  new VerticalLayout();
					vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
					vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
					vLayout.setMargin(true);
					vLayout.setSpacing(true);
					
					final TextArea txtArea = new TextArea();
					txtArea.setMaxLength(1000);
					txtArea.setValue(txtFld.getValue());
					txtArea.setNullRepresentation("");
					txtArea.setRows(21);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					txtArea.setReadOnly(true);
					
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					
					txtArea.addValueChangeListener(new Property.ValueChangeListener() {
						@Override
						public void valueChange(ValueChangeEvent event) {
							txtFld.setValue(((TextArea)event.getProperty()).getValue());
						}
					});
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);
					
					String strCaption = "Deductible / Non Payables Reason - FA";
					dialog.setCaption(strCaption);
					dialog.setClosable(true);
					dialog.setContent(vLayout);
					dialog.setResizable(true);
					dialog.setModal(true);
					dialog.setDraggable(true);
					dialog.setData(txtFld);
					
					dialog.addCloseListener(new Window.CloseListener() {
						@Override
						public void windowClose(CloseEvent e) {
							dialog.close();
						}
					});
					
					if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
						dialog.setPositionX(250);
						dialog.setPositionY(100);
					}
					getUI().getCurrent().addWindow(dialog);
					
					okBtn.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;
						@Override
						public void buttonClick(ClickEvent event) {
							dialog.close();
						}
					});	
				}
			};
			return listener;
		}
		
		public Integer getTotalNoOfDaysValue(){
			
			Integer noOfDays = 0;
			
			List<BillEntryDetailsDTO> itemIds = (List<BillEntryDetailsDTO>) this.table.getItemIds() ;
			for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {
				if(billEntryDetailsDTO.getNoOfDaysAllowed() != null){
					if(!((billEntryDetailsDTO.getItemName().contains("Sub Total") || billEntryDetailsDTO.getItemName().contains("Total Room Rent") || billEntryDetailsDTO.getItemName().contains("Total ICU Charges")))){
						noOfDays += billEntryDetailsDTO.getNoOfDaysAllowed().intValue();
					}
				}
			}
			return noOfDays;
		}
		
		
		public BlurListener getNonPayableReasonListener(final TextField reasonField) {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					if(null != component && null != component.getValue()) {
						reasonField.setDescription(component.getValue());
					} 
					
				}
			};
			return listener;
			
		}
		public void removeObject(BillEntryDetailsDTO dto){
			 table.removeItem(dto);
		 }
		
		private void checkBoxValueChangeListenerUnselected(BillEntryDetailsDTO billDTO,Boolean isCheckBoxSelected,Double propDedPer) {
			List<BillEntryDetailsDTO> itemIds = (List<BillEntryDetailsDTO>) this.table.getItemIds() ;
			List<BillEntryDetailsDTO> propDedList = new ArrayList<BillEntryDetailsDTO>();
			System.out.println("Inside the checkbox Loop");
			for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {if(billEntryDetailsDTO.getBillTypeNumber() != null && billDTO.getBillTypeNumber() != null &&
					(billEntryDetailsDTO.getBillTypeNumber().equals(billDTO.getBillTypeNumber()))){
				billEntryDetailsDTO.setIsproportionateDeductionSelected(false);
				Double nonPayable =  billEntryDetailsDTO.getNonPayable();
				billEntryDetailsDTO.setProportionateDeduction(0d);
				billEntryDetailsDTO.setNetPayableAmount(billEntryDetailsDTO.getItemValue() - nonPayable);
				billEntryDetailsDTO.setTotalDisallowances(nonPayable);
				billEntryDetailsDTO.setAmountAllowableAmount(billEntryDetailsDTO.getItemValue() - nonPayable);
			}
			propDedList.add(billEntryDetailsDTO);
			}
			setTableList(propDedList);
			calculateTotal();
		}
		
		private void checkBoxValueChangeListenerForSelected(BillEntryDetailsDTO billDTO,Boolean isCheckBoxSelected,Double propDedPer) {
			List<BillEntryDetailsDTO> itemIds = (List<BillEntryDetailsDTO>) this.table.getItemIds() ;
			List<BillEntryDetailsDTO> propDedList = new ArrayList<BillEntryDetailsDTO>();
			Double propDeductionAmount =0d;
			Double nonPayable =0d;
			System.out.println("Inside the checkbox Loop For True Select");
			for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {if(billEntryDetailsDTO.getBillTypeNumber() != null && billDTO.getBillTypeNumber() != null &&
					(billEntryDetailsDTO.getBillTypeNumber().equals(billDTO.getBillTypeNumber()))){
				if(billDTO.getItemValue() !=null && billDTO.getItemValue() >0d){
					Double billAmount = billDTO.getItemValue();
					 nonPayable =  billEntryDetailsDTO.getNonPayable();
					billEntryDetailsDTO.setIsproportionateDeductionSelected(true);
					propDeductionAmount = (billAmount * propDedPer) / 100;
					propDeductionAmount = (double) Math.round(propDeductionAmount);
				}
				if(propDeductionAmount != null && propDeductionAmount >0d){
				billEntryDetailsDTO.setProportionateDeduction(propDeductionAmount);
				billEntryDetailsDTO.setNetPayableAmount(billEntryDetailsDTO.getItemValue() - propDeductionAmount - nonPayable);
				billEntryDetailsDTO.setTotalDisallowances(propDeductionAmount - nonPayable);
				billEntryDetailsDTO.setAmountAllowableAmount(billEntryDetailsDTO.getItemValue() - propDeductionAmount - nonPayable);
				}
			}
			propDedList.add(billEntryDetailsDTO);
			}
			setTableList(propDedList);
			calculateTotal();
		}
		
		public Boolean isCheckedOrNot(){
			
			return isChecked;
			
		}
		
		public void clearObjects(){
			 if(vLayout!=null){
				 vLayout.removeAllComponents();
			 }
				this.container = null;
				this.tableItem = null;
				this.productKey = null;
				if(table!=null){
					table.removeAllItems();
				}
		 }
	
	}