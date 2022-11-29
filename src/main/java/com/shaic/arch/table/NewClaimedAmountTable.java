package com.shaic.arch.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import ch.meemin.pmtable.PMTable.Align;
import ch.meemin.pmtable.PMTreeTable;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.AmountRequestedDetailField;
import com.shaic.arch.fields.AmountRequestedField;
import com.shaic.claim.cashlessprocess.downsize.wizard.DownSizePreauthWizardPresenter;
import com.shaic.claim.cashlessprocess.downsizeRequest.page.DownsizePreauthRequestWizardPresenter;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.negotiation.NegotiationPreauthRequestPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.ClaimDetailsTableBean;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.PMTableRow;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPreauthMoreDetailsPresenter;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/** Based On Benefit ID
 * 8 Room rent 
 * 9 ICU
 * 10 OT Charges
 * 11 Professional Fee
 * 12 Investigation
 * 13 Medicines
 * 23 Consumables
 * 14 Implant/Stunt/Valve/Pacemaker
 * 15 Ambulance
 * 16 ANH Package
 * 17 Composite Package
 * 18 Other Package
 * 19 Others
 * 20 Taxes and Other Cess
 * 21 Discount in Hospital Bill
 * 22 ICCU 
 * 
 * Prorata Flag 1 = Yes, 0 = No, 2 = Percentage...
 */

public class NewClaimedAmountTable extends ViewComponent {
	private static final long serialVersionUID = 5411865540250106214L;
	
	//private static final int TOTAL_CALCULATION_ROW = 16;
	
	protected PMTreeTable pmTreeTable;
	
	private PreauthDTO preauthDto;
	
	//private TextField noOfTextField;
	
	private Map<Integer, Object> dbCalculationValues;
	
	public Boolean isANHOrCompositeSelected = false;
	
	Integer[] mandatoryBenefitId = {8, 9, 22, 16, 17};
	
	protected String roomRentAmountConsidered;
	protected String roomRentAmountRequested;
	
	Integer[] childItemId = {8, 9 , 22, 10, 11, 12, 13, 23, 14, 15, 16, 17, 18, 19,20,21};
	Integer[] labelId = {8, 9 ,22, 10, 11, 12, 0, 15, 19,20, 21};
	Integer[] proRataCalculationIds = {10, 11, /*12,*/ 18, 19};
	Integer[] proRataForSpecificProductCalculationIds = {10, 11, /*12,*/ 18};
	Integer[] parentItemId = {1,7, 11};
	Integer[] checboxItemId = {17, 18};
	Integer[] anhItemId = {16, 17, 18};
	Integer[] addButtonIds = {8, 9, 22, 0, 16};
	Integer[] ignoreItemId = {1, 7, 11, 16};
	Integer[] roomRentICUId = {8, 9, 22};
	Integer[] amountConsiderCalculationIds = {8, 9, 22, 12};
	Integer[] percentageIds = {10, 12, 13, 23, 14, 19};
	Integer[] microProductIds = {10, 12, 13, 23, 14, 16, 17, 18, 19,20};
	String[] calculatedColumns = {"Deductibles", "Net Amount", "Amount Considered"};
	Integer[] hospDiscountId = {21};
	private   int i = 0;
	Map<Integer, String> checkboxMap = new WeakHashMap<Integer, String>();
	
	EnhanceTableFactory tableFactory = new EnhanceTableFactory();
	
	List<String> errorMessages = new ArrayList<String>();
	
	List<Long> deletedIds = new ArrayList<Long>();
	
	Float totalNoOfDays = 0f;
	
	Float totalEntitlementDays = 0f;
	
	public void setDBCalculationValues(Map<Integer, Object> values) {
		if(this.preauthDto != null){
			Product product = this.preauthDto.getNewIntimationDTO().getPolicy().getProduct();
			if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey()) && 
					this.preauthDto.getNewIntimationDTO().getPolicy().getPolicyPlan() != null && this.preauthDto.getNewIntimationDTO().getPolicy().getPolicyPlan().equalsIgnoreCase("G")) {
				values.put(8, 0d);
				values.put(0, 0);
			}
		}
		this.dbCalculationValues = values;
	}
	
	public void initView(PreauthDTO preauthDTO, String presenterString) {
		this.preauthDto = preauthDTO;
		if(SHAConstants.PRE_MEDICAL_PRE_AUTH.equalsIgnoreCase(presenterString)) {
			/*
			 * The below event is fired to load product amt value on load.
			 * */
			fireViewEvent(PreMedicalPreauthWizardPresenter.GET_HOSPITALIZATION_DETAILS, preauthDTO);
		}
		else if(SHAConstants.PRE_AUTH.equalsIgnoreCase(presenterString)) {
			/*
			 * The below event is fired to load product amt value on load.
			 * */
			fireViewEvent(PreauthWizardPresenter.GET_HOSPITALIZATION_DETAILS, preauthDTO);
		}
		
		else if(SHAConstants.PRE_MEDICAL_ENHANCEMENT.equalsIgnoreCase(presenterString)){
			
			fireViewEvent(PremedicalEnhancementWizardPresenter.GET_HOSPITALIZATION_DETAILS, preauthDTO);
		}
		else if(SHAConstants.PROCESS_ENHANCEMENT.equalsIgnoreCase(presenterString)){
			
			fireViewEvent(PreauthEnhancemetWizardPresenter.GET_HOSPITALIZATION_DETAILS, preauthDTO);
			
		}
         else if(SHAConstants.DOWNSIZE_PREAUTH_SCREEN.equalsIgnoreCase(presenterString)){
			
			fireViewEvent(DownSizePreauthWizardPresenter.GET_HOSPITALIZATION_DETAILS, preauthDTO);
			
		}
         else if(SHAConstants.VIEW_PREAUTH.equalsIgnoreCase(presenterString)){
 			
        	 try{
        		 fireViewEvent(ViewPreauthMoreDetailsPresenter.SET_HOSPITALIZATION, preauthDTO);
        	 }catch(Exception e){
        		 e.printStackTrace();
        	 }
 			
 		}
         else if(SHAConstants.DOWNSIZE_PREAUTH_REQUEST_SCREEN.equalsIgnoreCase(presenterString)){
        	 fireViewEvent(DownsizePreauthRequestWizardPresenter.GET_HOSPITALIZATION_DETAILS, preauthDTO);
         }
         else if(SHAConstants.VIEW_PREAUTH_CLAIM_STATUS.equalsIgnoreCase(presenterString)){
        	 this.dbCalculationValues = preauthDTO.getHospitalizationDetailsVal();
         }
         else if(SHAConstants.NEGOTIATION_PREAUTH_REQUEST_SCREEN.equalsIgnoreCase(presenterString)){
        	 fireViewEvent(NegotiationPreauthRequestPresenter.GET_NEGOTIATION_HOSPITALIZATION_DETAILS,preauthDTO);
         }
     
		
		
		checkboxMap.put(17, "Override Package Deduction");
		checkboxMap.put(18, "Restrict to 80%");
		//Adding header for Amount claimed details page.
		pmTreeTable = new PMTreeTable("Amount Claimed Details");
		pmTreeTable.setStyleName("wordwrap-headers");
//		pmTreeTable.setTableFieldFactory(tableFactory);
		pmTreeTable.addContainerProperty("S.No", Label.class, null );
		pmTreeTable.addContainerProperty("Details", AmountRequestedDetailField.class, null );
		pmTreeTable.addContainerProperty("claimedDays", TextField.class, null);
		pmTreeTable.addContainerProperty("claimedDayAmt", TextField.class, null);
		pmTreeTable.addContainerProperty("claimedAmt", TextField.class, null);
		pmTreeTable.addContainerProperty("deductible", TextField.class, null);
		pmTreeTable.addContainerProperty("netAmt", TextField.class, null);
		pmTreeTable.addContainerProperty("productDays", TextField.class, null);
		pmTreeTable.addContainerProperty("productDayAmt", TextField.class, null);
		pmTreeTable.addContainerProperty("ProductAmount", TextField.class, null);
		pmTreeTable.addContainerProperty("consideredPerDayAmt", TextField.class, null);
		pmTreeTable.addContainerProperty("nonPayableAmt", TextField.class, null);
		pmTreeTable.addContainerProperty("payableAmt", TextField.class, null);
		pmTreeTable.addContainerProperty("reason", TextField.class, null);
		pmTreeTable.addContainerProperty("add", Button.class, null);
		pmTreeTable.setStyleName(ValoTheme.TABLE_COMPACT);
		pmTreeTable.setWidth("100%");
		//pmTreeTable.setHeight("375px");
		
		pmTreeTable.setColumnWidth("Detail", 405);
		
		/**
		 * The below commented line does the job of configuring
		 * the column header names.  As of now this is commented.
		 * While implementing this change , shall incorporate the same.
		 * */
		pmTreeTable.setColumnHeader("claimedDays", "No Of </br> Days </br> (A)");
		pmTreeTable.setColumnHeader("claimedDayAmt", "Per Day </br> Amt </br> (B)");
		pmTreeTable.setColumnHeader("claimedAmt", "Claimed </br> Amount <br> (C)");
		pmTreeTable.setColumnHeader("deductible", "Deductible </br> (D)");
		pmTreeTable.setColumnHeader("netAmt", "Net </br> Amount </br> (E)");
		pmTreeTable.setColumnHeader("productDays", "No Of </br> Days </br> (F)");
		pmTreeTable.setColumnHeader("productDayAmt", "Per Day </br> Amt </br>(Product </br> Based) </br> (G)");
		pmTreeTable.setColumnHeader("ProductAmount", "Amount</br> (H)");
		pmTreeTable.setColumnHeader("consideredPerDayAmt", "Considered </br> Per Day </br> Amt");
		pmTreeTable.setColumnHeader("nonPayableAmt", "Non Payables </br>(Incl <br> Deductibles) </br>(I)");
		pmTreeTable.setColumnHeader("payableAmt", "Payable </br> Amt </br> (J)");
		pmTreeTable.setColumnHeader("reason", "Deductible / </br> Non Payables </br> Reason");
		pmTreeTable.setColumnHeader("add", "");
		
		// Detail column is used as Tree..
		pmTreeTable.setHierarchyColumn("Details");
		pmTreeTable.setColumnWidth("add", 60);
		
		pmTreeTable.setColumnAlignment("claimedDays", Align.CENTER);
		pmTreeTable.setColumnAlignment("claimedDayAmt", Align.CENTER);
		pmTreeTable.setColumnAlignment("claimedAmt", Align.CENTER);
		pmTreeTable.setColumnAlignment("deductible", Align.CENTER);
		pmTreeTable.setColumnAlignment("productDays", Align.CENTER);
		pmTreeTable.setColumnAlignment("reason", Align.CENTER);
		
		setupTable(ClaimDetailsTableBean.getPMTableRows());
		
		pmTreeTable.removeItem(1);
		
		pmTreeTable.setFooterVisible(true);
		pmTreeTable.setColumnFooter("Details", "Total");
		//pmTreeTable.setCaption("Amount Claimed Details");
		
		setCompositionRoot(pmTreeTable);
		
	
	}
	
	@PostConstruct
	public void init()
	{
		
	}
	
	private void setupTable(PMTableRow root)
	{
		addTreeMenuItem(root, null);
		
	}
	
	private TextField createTextField(Integer masterId, Object itemId, String width, Boolean isEnabled, Boolean isReadOnly, Integer maxLength, Boolean isRoomDays) {
		TextField createdField = new TextField();
		createdField.setEnabled(isEnabled);
		createdField.setWidth(width);
		if(maxLength != 0) {
			createdField.setMaxLength(maxLength);
		}
		createdField.setData(masterId.toString() + "-" + itemId.toString());
		//createdField.setNullRepresentation("0");
		/**
		 * When amount claimed details page is loaded for first time, 
		 * values in each column would be null. If null representation is
		 * given as 0 , it would be a value change in those columns and due to
		 * this, value change listener will be invoked which would lead to erroneous
		 * results in amount calculation. Hence commenting the above line and adding below
		 * line.
		 * */
		createdField.setNullRepresentation("");
		
		if(isReadOnly) {
			createdField.setReadOnly(true);
			createdField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		}
		
		CSValidator validator = new CSValidator();
		validator.extend(createdField);
		validator.setRegExp("^[0-9]*$"); // Should allow only 0 to 9 and '.'
		if(isRoomDays) {
			validator.setRegExp("^[0-9.]*$");
		}
		validator.setPreventInvalidTyping(true);
		
		return createdField;
	}
	
	/**
	 * This method will be called recursively. This method creates
	 * each column in the table
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addTreeMenuItem(PMTableRow menu, Object parent) {
		Integer masterId = menu.getMasterId();
		//item id will be the number. refer claimDetailsTableBean file.
		Object itemId = pmTreeTable.addItem();
		Item parentItem = pmTreeTable.getItem(itemId);
		//Collection<?> collection = Arrays.asList(pmTreeTable.getItemIds()).get(0);
		
		AmountRequestedDetailField amountRequestedDetailField;
		if(Arrays.asList(this.checboxItemId).contains(masterId)) {
			amountRequestedDetailField = new AmountRequestedDetailField(false, menu.getDetailLabel(), checkboxMap.get(masterId));
			CheckBox checkboxField = amountRequestedDetailField.getCheckboxField();
			/**
			 * Assigning master id with "-" separated along with itemId.
			 * Master id will come from MAS_BENEFIT table. Itemid will come from
			 * ClaimDetailsTableBean file. There in variable declaration we will be
			 * giving master id value and item id value.
			 **/
			checkboxField.setData(masterId.toString() + "-" + itemId.toString());
			checkboxField.addValueChangeListener(getCheckboxListener(menu.getMasterId()));
		} else {
			amountRequestedDetailField = new AmountRequestedDetailField(true, menu.getDetailLabel(), null);
		}
		// This is used to identify the master id.
		amountRequestedDetailField.setData(masterId.toString() + "-" + itemId.toString());
		parentItem.getItemProperty("Details").setValue(amountRequestedDetailField);
		
		if(Arrays.asList(labelId).contains(masterId)) {
			parentItem.getItemProperty("S.No").setValue(new Label((String.valueOf(i++))));
		}
		
		Property claimedNoOfDaysProperty = parentItem.getItemProperty("claimedDays");
		Property claimedPerDayAmtProperty = parentItem.getItemProperty("claimedDayAmt");
		Property claimedAmtProperty = parentItem.getItemProperty("claimedAmt");
		Property deductibleProperty = parentItem.getItemProperty("deductible");
		Property netAmtProperty = parentItem.getItemProperty("netAmt");
		
		// Product Properties
		Property productNoOfDaysProperty = parentItem.getItemProperty("productDays");
		Property productPerDayAmtProperty = parentItem.getItemProperty("productDayAmt");
		Property productAmountProperty = parentItem.getItemProperty("ProductAmount");
		
		 // General Properties
		Property nonPayableProperty = parentItem.getItemProperty("nonPayableAmt");
		Property consideredPerDayAmt = parentItem.getItemProperty("consideredPerDayAmt");
		Property payableProperty = parentItem.getItemProperty("payableAmt");
		Property reasonProperty = parentItem.getItemProperty("reason");
		
		Property buttonPropery = parentItem.getItemProperty("add");
		
		if(masterId != 0 &&  masterId != 100) {
			if(Arrays.asList(roomRentICUId).contains(masterId)) {
				//CreateTextField will create text box in the table.
				claimedNoOfDaysProperty.setValue(createTextField(masterId, itemId, "50px", true, false, 4, true));
				claimedPerDayAmtProperty.setValue(createTextField(masterId, itemId, "60px" , true, false, 15, false));
				productNoOfDaysProperty.setValue(createTextField(masterId, itemId, "50px" , true, false, 4, true));
				productPerDayAmtProperty.setValue(createTextField(masterId, itemId, "60px", true, true, 15, false));
				TextField field =  ((TextField)productPerDayAmtProperty.getValue());
				field.setReadOnly(false);
				if((Double)this.dbCalculationValues.get(masterId) != null){
				field.setValue(String.valueOf(((Double)this.dbCalculationValues.get(masterId)).intValue()));
				}
				field.setReadOnly(true);
				
				((TextField)claimedNoOfDaysProperty.getValue()).addBlurListener(getClaimedNoOfDaysListener());
				((TextField)claimedPerDayAmtProperty.getValue()).addBlurListener(getClaimedNoOfDaysListener());
				((TextField)productNoOfDaysProperty.getValue()).addValueChangeListener(getProductNoOfDaysListener());
			} 
			
			
			if(Arrays.asList(hospDiscountId).contains(masterId)){
				claimedAmtProperty.setValue(createTextField(masterId, itemId,  "70px", false, false, 0, false));
				netAmtProperty.setValue(createTextField(masterId, itemId, "70px", false, false, 0, false));
				payableProperty.setValue(createTextField(masterId, itemId, "70px", false, false, 0, false));
			}else{
				claimedAmtProperty.setValue(createTextField(masterId, itemId,  "70px", !Arrays.asList(roomRentICUId).contains(masterId), false, 0, false));
				netAmtProperty.setValue(createTextField(masterId, itemId, "70px", true, true, 0, false));
				deductibleProperty.setValue(createTextField(masterId, itemId, "70px", true, false, 15, false));
				productAmountProperty.setValue(createTextField(masterId, itemId, "70px", true, true, 0, false));
				nonPayableProperty.setValue(createTextField(masterId, itemId, "70px", true, true, 0, false));
				payableProperty.setValue(createTextField(masterId, itemId, "70px", true, true, 0, false));
			}
			
			TextField createdField = new TextField();
			createdField.setWidth("100px");
			createdField.setMaxLength(100);
			createdField.setData(null);
			//createdField.setNullRepresentation("0");
			/**
			 * When amount claimed details page is loaded for first time, 
			 * values in each column would be null. If null representation is
			 * given as 0 , it would be a value change in those columns and due to
			 * this, value change listener will be invoked which would lead to erroneous
			 * results in amount calculation. Hence commenting the above line and adding below
			 * line.
			 * */
			createdField.setNullRepresentation("");
			
//			CSValidator validator = new CSValidator();
//			validator.extend(createdField);
//			validator.setRegExp("^[a-zA-Z0-9 ]*$"); // Should allow only 0 to 9 and '.'
//			validator.setPreventInvalidTyping(true);
			
			reasonProperty.setValue(createdField);
			
			TextField reasonField = ((TextField)reasonProperty.getValue());
			reasonField.setNullRepresentation("");
			reasonField.setDescription("Click the Text Box and Press F8 For Detailed Popup");
			
			((TextField)reasonProperty.getValue()).addBlurListener(getNonPayableReasonListener(reasonField));
			getNonPayableReasonShortcutListener(((TextField)reasonProperty.getValue()), null);
			
			consideredPerDayAmt.setValue(createTextField(masterId, itemId, "70px", true, true, 0, false));				
			
			((TextField)claimedAmtProperty.getValue()).addValueChangeListener(getAmountListener(true));
			if(Arrays.asList(roomRentICUId).contains(masterId)) {
				((TextField)productAmountProperty.getValue()).addValueChangeListener(getAmountListener(true));
			}
			
			if(!Arrays.asList(hospDiscountId).contains(masterId)) {
				((TextField)deductibleProperty.getValue()).addValueChangeListener(getAmountListener(true));
			}

			if(preauthDto.getNewIntimationDTO().getIsPaayasPolicy()){
				
				((TextField)claimedAmtProperty.getValue()).addValueChangeListener(getClaimedAmntListener(true));
			}
			
			if(masterId == 15) {
				TextField field =  ((TextField)productAmountProperty.getValue());
				field.setReadOnly(false);
				if((Double)this.dbCalculationValues.get(masterId) != null){
				field.setValue(String.valueOf(((Double)this.dbCalculationValues.get(masterId)).intValue()));
				}
				field.setReadOnly(true);
			}
			
			if(Arrays.asList(addButtonIds).contains(masterId)) {
				Button button = new Button("Add");
				button.setData(masterId.toString() + "-" + itemId.toString());
				
				button.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = -4216310752254520407L;

					@Override
					public void buttonClick(ClickEvent event) {
						Button selectedButton = event.getButton();
						addRow(selectedButton);
					}
				});
				
				buttonPropery.setValue(button);
			}
			
		} else if(masterId == 100 && (preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP)) ) {
			amountRequestedDetailField = new AmountRequestedDetailField(true, "Total of OT charges, </br> Investigation and diagnositics, </br> medicines and consumables, </br> packages procedure", null);
			// This is used to identify the master id.
			amountRequestedDetailField.setData(masterId.toString() + "-" + itemId.toString());
			parentItem.getItemProperty("Details").setValue(amountRequestedDetailField);
			claimedAmtProperty.setValue(createTextField(masterId, itemId,  "70px", false, false, 0, false));
			netAmtProperty.setValue(createTextField(masterId, itemId, "70px", false, true, 0, false));
			deductibleProperty.setValue(createTextField(masterId, itemId, "70px", false, true, 15, false));
			productAmountProperty.setValue(createTextField(masterId, itemId, "70px", false, true, 0, false));
			nonPayableProperty.setValue(createTextField(masterId, itemId, "70px", false, true, 0, false));
			payableProperty.setValue(createTextField(masterId, itemId, "70px", false, true, 0, false));
		}
		
		pmTreeTable.setCollapsed(itemId, false);
	
		if (parent != null) {
			pmTreeTable.setParent(itemId, parent);
		}
		
		if (menu.hasChild()) {
			pmTreeTable.setChildrenAllowed(itemId, true);
			for (PMTableRow childItem : menu.getChildRow()) {
				addTreeMenuItem(childItem, itemId);
			}
		}
		else {
			if(Arrays.asList(addButtonIds).contains(masterId)) {
				pmTreeTable.setChildrenAllowed(itemId, true);
			} else {
				pmTreeTable.setChildrenAllowed(itemId, false);
			}
		}
	}
	
	public void getNonPayableReasonShortcutListener(TextField nonPayableReasonTF, final  Listener listener) {
	    ShortcutListener enterShortCut = new ShortcutListener("ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {
	    	private static final long serialVersionUID = 1L;
	    	@Override
	    	public void handleAction(Object sender, Object target) {
	    		((ShortcutListener) listener).handleAction(sender, target);
	    	}
	    };
	    handleShortcutForNonPayableReason(nonPayableReasonTF, getShortCutListenerForMedicalRemarks(nonPayableReasonTF));
	}
	
	public  void handleShortcutForNonPayableReason(final TextField textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {
			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);
			}
		});
		textField.addBlurListener(new BlurListener() {
			@Override
			public void blur(BlurEvent event) {
				textField.removeShortcutListener(shortcutListener);
			}
		});
	}
	
	private ShortcutListener getShortCutListenerForMedicalRemarks(final TextField txtFld) {
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {
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
//				txtArea.setData(bean);
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setRows(21);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				txtArea.setReadOnly(false);
				
				dialog.setHeight("75%");
		    	dialog.setWidth("65%");
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
//						PreauthDTO mainDto = (PreauthDTO)txtFld.getData();
//						mainDto.getPreauthMedicalDecisionDetails().setMedicalRemarks(txtFld.getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);
				
				String strCaption = "Deductible / Non Payables Reason";
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
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		return listener;
	}
	
	private BlurListener getClaimedNoOfDaysListener() {
		
		BlurListener liste = new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				TextField blurTxtField = (TextField)event.getComponent();
				String ids = (String) blurTxtField.getData();
				//String benefitId = ids.split("-")[0];
				String itemId = ids.split("-")[1];
				Item item = pmTreeTable.getItem(Integer.valueOf(itemId));
				TextField daysField =  (TextField) item.getItemProperty("claimedDays").getValue();
				//TextField productDaysField =  (TextField) item.getItemProperty("productDays").getValue();
				TextField perDayAmtField =  (TextField) item.getItemProperty("claimedDayAmt").getValue();
				TextField claimedAmtField =  (TextField) item.getItemProperty("claimedAmt").getValue();
				setNoDaysCalculation(daysField, perDayAmtField, claimedAmtField);
			}
		};
		

		updateSummation();
		return liste;
	}
	
	private ValueChangeListener getProductNoOfDaysListener() {
		
		ValueChangeListener liste = new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				TextField blurTxtField = (TextField)event.getProperty();
				String ids = (String) blurTxtField.getData();
				//String benefitId = ids.split("-")[0];
				String itemId = ids.split("-")[1];
				Item item = pmTreeTable.getItem(Integer.valueOf(itemId));
				TextField claimedDaysField =  (TextField) item.getItemProperty("claimedDays").getValue();
				TextField daysField =  (TextField) item.getItemProperty("productDays").getValue();
				TextField perDayAmtField =  (TextField) item.getItemProperty("productDayAmt").getValue();
				TextField claimedAmtField =  (TextField) item.getItemProperty("ProductAmount").getValue();
				
				if(SHAUtils.convertFloatToString(daysField.getValue()) > SHAUtils.convertFloatToString(claimedDaysField.getValue())) {
					VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Entitlement No of days should be lesser than claimed No of days </b>", ContentMode.HTML));
					showErrorPopup(daysField, layout);
					setNoDaysCalculation(daysField, perDayAmtField, claimedAmtField);
				} else {
					setNoDaysCalculation(daysField, perDayAmtField, claimedAmtField);
//					if(!(SHAUtils.getIntegerFromString(daysField.getValue()) == 0 && SHAUtils.getIntegerFromString(perDayAmtField.getValue()) != 0)) {
//						setNoDaysCalculation(daysField, perDayAmtField, claimedAmtField);
//					}
					
				}
				
			}
		};
		
		/*BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = -6953428339734363090L;

			@Override
			public void blur(BlurEvent event) {
				TextField blurTxtField = (TextField)event.getComponent();
				String ids = (String) blurTxtField.getData();
				//String benefitId = ids.split("-")[0];
				String itemId = ids.split("-")[1];
				Item item = pmTreeTable.getItem(Integer.valueOf(itemId));
				TextField claimedDaysField =  (TextField) item.getItemProperty("claimedDays").getValue();
				TextField daysField =  (TextField) item.getItemProperty("productDays").getValue();
				TextField perDayAmtField =  (TextField) item.getItemProperty("productDayAmt").getValue();
				TextField claimedAmtField =  (TextField) item.getItemProperty("ProductAmount").getValue();
				
				if(SHAUtils.convertFloatToString(daysField.getValue()) > SHAUtils.convertFloatToString(claimedDaysField.getValue())) {
					VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Entitlement No of days should be lesser than claimed No of days </b>", ContentMode.HTML));
					showErrorPopup(daysField, layout);
				} else {
					setNoDaysCalculation(daysField, perDayAmtField, claimedAmtField);
//					if(!(SHAUtils.getIntegerFromString(daysField.getValue()) == 0 && SHAUtils.getIntegerFromString(perDayAmtField.getValue()) != 0)) {
//						setNoDaysCalculation(daysField, perDayAmtField, claimedAmtField);
//					}
					
				}
				
			}
		};*/
		updateSummation();
		return liste;
	}
	
	private void setNoDaysCalculation(TextField daysField, TextField perDayAmtField, TextField claimedAmtField) {
		/*if(true) {
			if(false) {
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Please Enter No of Days Field </b>", ContentMode.HTML));
				if(perDayAmtField.isReadOnly()) {
					layout = new VerticalLayout(new Label("<b style = 'color: red;'> No of Days should be Greater than zero. </b>", ContentMode.HTML));
					showErrorPopup(null, layout);
				} else {
					showErrorPopup(null, layout);
				}
				
			} */
			
			//else {
				Float floatValue = (SHAUtils.convertFloatToString(daysField.getValue()) * SHAUtils.convertFloatToString(perDayAmtField.getValue()));
				Integer value = Math.round(floatValue);
				claimedAmtField.setReadOnly(false);
				claimedAmtField.setValue(value.toString());
				claimedAmtField.setReadOnly(true);
				daysField.setReadOnly(false);
				daysField.setValue(daysField.getValue());
				perDayAmtField.setValue(perDayAmtField.getValue());
			//}
		//}
				/*else {
			*//**
			 * This else block will be invoked, if the value of the text field is not an valid integer.
			 * The below condition to fetch a integer from string is irony , since the integer check itself failed
			 * and control has navigated to else block. Hence commenting below code.
			 * *//*
//			if(!SHAUtils.isValidInteger(daysField.getValue()) && SHAUtils.getIntegerFromString(perDayAmtField.getValue()) != 0) {
//				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Please Enter No of Days Field </b>", ContentMode.HTML));
//				showErrorPopup(null, layout);
////				setNoDaysCalculation(daysField, perDayAmtField, claimedAmtField);
//			}
//			if(SHAUtils.getIntegerFromString(daysField.getValue()) == 0 && SHAUtils.getIntegerFromString(perDayAmtField.getValue()) != 0) {
//				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Please Enter No of Days Field </b>", ContentMode.HTML));
//				showErrorPopup(null, layout);
//				return;
//			}
		}*/
		/*
		 * The below method does the sum of all values entered in the text box.
		 * */
		updateSummation();
	}

	private void showErrorPopup(TextField field, VerticalLayout layout) {
	/*	layout.setMargin(true);
		layout.setSpacing(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		if(field != null) {
			boolean readOnly = field.isReadOnly();
			field.setReadOnly(false);
			field.setNullRepresentation("");
			
//			field.setValue(null);
			field.setCursorPosition(1);
			field.setReadOnly(readOnly);
		}
		dialog.show(getUI().getCurrent(), null, true);*/
		
		if(field != null) {
			boolean readOnly = field.isReadOnly();
			field.setReadOnly(false);
			field.setNullRepresentation("");
			
//			field.setValue(null);
			field.setCursorPosition(1);
			field.setReadOnly(readOnly);
		}
	
		MessageBox.createError()
    	.withCaptionCust("Errors").withTableMessage(layout)
        .withOkButton(ButtonOption.caption("OK")).open();	
	}
	
	public Object getTotalFieldProperty(String itemPropertyId) {
		Item item = this.pmTreeTable.getItem(16);
		Object propertyObject = item.getItemProperty(itemPropertyId).getValue();
		return propertyObject;
	}
	
	public String getTotalAmountByPropertyName(String itemPropertyId) {
		Item item = this.pmTreeTable.getItem(16);
		Object propertyObject = item.getItemProperty(itemPropertyId).getValue();
		if(propertyObject instanceof AmountRequestedField) {
			AmountRequestedField field =  (AmountRequestedField) propertyObject;
			 return field.getAmount().toString();
		}
		TextField field = (TextField) propertyObject;
		return field.getValue();
	}
	
	public List<TextField> getNoOfDaysFields() {
		//List<AmountRequestedField> fields = new ArrayList<AmountRequestedField>();
		List<TextField> fields = new ArrayList<TextField>();
		Item roomRentItem = pmTreeTable.getItem(2);
		Item ICUItem = pmTreeTable.getItem(3);
		/*AmountRequestedField roomRentItemField = (AmountRequestedField) roomRentItem.getItemProperty("Amount Requested").getValue();
		AmountRequestedField ICUItemField = (AmountRequestedField) ICUItem.getItemProperty("Amount Requested").getValue();*/
		
		TextField roomRentItemField = (TextField) roomRentItem.getItemProperty("claimedDays").getValue();
		TextField ICUItemField = (TextField) ICUItem.getItemProperty("claimedDays").getValue();
		
		fields.add(roomRentItemField);
		fields.add(ICUItemField);
		
		return fields;
	}
	
	public Integer getNoOfDaysValues() {
		//List<AmountRequestedField> fields = new ArrayList<AmountRequestedField>();
		Item roomRentItem = pmTreeTable.getItem(2);
		Item ICUItem = pmTreeTable.getItem(3);
		//AmountRequestedField roomRentItemField = (AmountRequestedField) roomRentItem.getItemProperty("Amount Requested").getValue();
		//AmountRequestedField ICUItemField = (AmountRequestedField) ICUItem.getItemProperty("Amount Requested").getValue();
		
		/**
		 * In previous design,all the text feilds were put up into a class called AmoutRequestedField class and datas are read from them.
		 * But in new design , this has been removed and the claimed days column belongs to  text field class itself. Therefore
		 * the above code which follows old design has  been commented and below code was added.
		 *  
		 * */
		
		TextField roomRentItemField = (TextField) roomRentItem.getItemProperty("claimedDays").getValue();
		TextField ICUItemField = (TextField) ICUItem.getItemProperty("claimedDays").getValue();
		int noOfDays = 0;
	//	return ((roomRentItemField.getNoOfDays() + ICUItemField.getNoOfDays()));
		if(null != roomRentItemField && null != roomRentItemField.getValue() && !("").equals(roomRentItemField.getValue()))
		{
			noOfDays = Integer.parseInt(roomRentItemField.getValue());
		}
		if(null != ICUItemField && null != ICUItemField.getValue() && !("").equals(ICUItemField.getValue()))
		{
			noOfDays = noOfDays + Integer.parseInt(ICUItemField.getValue());
		}
		return  noOfDays;
	}
	
	private void updateSummation()
	{
		Integer claimedDayAmt = 0;
		Integer claimedAmt = 0;
		Integer deductible = 0;
		Integer netAmt = 0;
		Integer productDayAmt = 0;
		Integer productAmt = 0;
		Integer consideredPerDayAmt = 0;
		Integer payableAmt = 0;
		Integer nonPayableAmt = 0;
		Collection<?> itemIds = pmTreeTable.getItemIds();
		for(Object itemId : itemIds)
		{
			Item rowItem = this.pmTreeTable.getItem(itemId);
			if (rowItem != null)
			{
				claimedDayAmt += getValueForProperty(rowItem, "claimedDayAmt");
				claimedAmt += getValueForProperty(rowItem, "claimedAmt");
				deductible += getValueForProperty(rowItem, "deductible");
				netAmt += getValueForProperty(rowItem, "netAmt");
				productDayAmt += getValueForProperty(rowItem, "productDayAmt");
				productAmt += getValueForProperty(rowItem, "ProductAmount");
				consideredPerDayAmt += getValueForProperty(rowItem, "consideredPerDayAmt");
				payableAmt += getValueForProperty(rowItem, "payableAmt");
				nonPayableAmt += getValueForProperty(rowItem, "nonPayableAmt");
			}
		}
//		pmTreeTable.setColumnFooter("claimedDayAmt", String.valueOf(claimedDayAmt));
		pmTreeTable.setColumnFooter("claimedAmt", String.valueOf(claimedAmt));
		pmTreeTable.setColumnFooter("deductible", String.valueOf(deductible));
		pmTreeTable.setColumnFooter("netAmt", String.valueOf(netAmt));
		
		pmTreeTable.setColumnFooter("productDayAmt", String.valueOf(productDayAmt));
//		pmTreeTable.setColumnFooter("ProductAmount", String.valueOf(productAmt));
		pmTreeTable.setColumnFooter("consideredPerDayAmt", String.valueOf(consideredPerDayAmt));
		pmTreeTable.setColumnFooter("payableAmt", String.valueOf(payableAmt));
		
		pmTreeTable.setColumnFooter("nonPayableAmt", String.valueOf(nonPayableAmt));
	}
	
	/*private void setValuesToRow(Item rowItem, String propertyId, float value)
	{
		if (rowItem.getItemProperty(propertyId) != null && rowItem.getItemProperty(propertyId).getValue() != null & rowItem.getItemProperty(propertyId).getValue() instanceof TextField)
		{
				TextField tAConsider = (TextField) rowItem.getItemProperty(propertyId).getValue();
				boolean flag = false;
				if (tAConsider.isReadOnly())
				{
					tAConsider.setReadOnly(false);	
					flag = true;
				}
				tAConsider.setValue("" + value);
				if (flag)
				{
					tAConsider.setReadOnly(true);	
				}
				
		}
		else if (rowItem.getItemProperty(propertyId) != null && rowItem.getItemProperty(propertyId).getValue() != null & rowItem.getItemProperty(propertyId).getValue() instanceof Label)
		{
			Label tAConsider = (Label) rowItem.getItemProperty(propertyId).getValue();
			tAConsider.setValue("" + value);
		}
		else if(rowItem.getItemProperty(propertyId) != null && rowItem.getItemProperty(propertyId).getValue() != null & rowItem.getItemProperty(propertyId).getValue() instanceof AmountRequestedField)
		{
			AmountRequestedField taAmount = (AmountRequestedField) rowItem.getItemProperty(propertyId).getValue();
			taAmount.setAmtTxt("" + value);
		}
	}*/
	
	private Integer getValueForProperty(Item rowItem, String propertyId)
	{
		Property itemProperty = rowItem.getItemProperty(propertyId);
		Integer amountConsider = 0;
		
		if (itemProperty != null)
		{
			Object fieldObject = itemProperty.getValue();
			String aConsiderValue = "0.0";
			if (fieldObject instanceof Label)
			{
				aConsiderValue  = ((Label) fieldObject).getValue();
			}
			else if (fieldObject instanceof TextField)
			{
				aConsiderValue = ((TextField) fieldObject).getValue();
			}
			else if (fieldObject instanceof AmountRequestedField)
			{
				return ((AmountRequestedField) fieldObject).getAmount();
			}
			
			
			if (SHAUtils.isValidFloat(aConsiderValue))
			{
				amountConsider += SHAUtils.getFloatFromString(aConsiderValue);
			}	
		}
		return amountConsider;
	}

	/*private void setTotal(String value, String itemPropertyId) {
//		if(value != null && value.length() > 0) {
//			Item item =  this.pmTreeTable.getItem(16);
//			Object propertyObject = item.getItemProperty(itemPropertyId).getValue();
//			String filledValue;
//			Float sumValue;
//			if(propertyObject instanceof Label) {
//				Label label = (Label) propertyObject;
//				filledValue = label.getValue();
//				sumValue = SHAUtils.getFloatFromString(value);
//				if(filledValue != null && filledValue.length() > 0) {
//					sumValue = SHAUtils.getFloatFromString(filledValue) + SHAUtils.getFloatFromString(value);
//				} 
//				label.setValue(sumValue.toString());
//				return;
//			} else if(propertyObject instanceof TextField) {
//				TextField textField = (TextField) propertyObject;
//				Boolean isReadOnly = false;
//				if(textField.isReadOnly()) {
//					textField.setReadOnly(false);
//					isReadOnly = true;
//				}
//				filledValue = textField.getValue();
//				sumValue = SHAUtils.getFloatFromString(value);
//				if(filledValue != null && filledValue.length() > 0) {
//					sumValue = SHAUtils.getFloatFromString(filledValue) + SHAUtils.getFloatFromString(value);
//				} 
//				textField.setValue(sumValue.toString());
//				textField.setReadOnly(isReadOnly);
//				pmTreeTable.setColumnFooter("Amount Considered", String.valueOf(sumValue));
//				pmTreeTable.setColumnFooter("Detail", "Total");
//				
//			} else if (propertyObject instanceof AmountRequestedField) {
//				AmountRequestedField amountRequestedField = (AmountRequestedField) propertyObject;
//				sumValue = SHAUtils.getFloatFromString(value);
//				if(amountRequestedField.getAmount() != null) {
//					sumValue = amountRequestedField.getAmount() + SHAUtils.getFloatFromString(value);
//				} 
//				amountRequestedField.getListenerField().setValue(sumValue.toString());
////				pmTreeTable.setColumnFooter("Amount Requested", String.valueOf(sumValue));
//			}
//		}
		updateSummation();
	}*/
	
	private ValueChangeListener getAmountListener(final Boolean invokeAgain) {
		
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 7342288705079773186L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				TextField property = (TextField) event.getProperty();
				if(!SHAUtils.isValidFloat(property.getValue())) {
					property.setValue("0");
					Notification notify = new Notification("Message");
					notify.setDelayMsec(-1);
				} else {
					setAmtRequestedCalculationValues(property);
				}
			}
		};
		return listener;
	}
	
	private ValueChangeListener getCheckboxListener(final Integer benefitId) {
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 7342288705079773186L;
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				CheckBox property = (CheckBox) event.getProperty();
				String ids = (String)property.getData();
				//String benefitId = ids.split("-")[0];
				String itemId = ids.split("-")[1];
				Item item = pmTreeTable.getItem(Integer.valueOf(itemId));
				TextField field = (TextField) item.getItemProperty("claimedAmt").getValue();
				setAmtRequestedCalculationValues(field);
			}
		};
		return listener;
	}
	
	private void specificProductCalculation(String enteredValue, Integer focusedBenfitId) {
		Collection<?> itemIds = pmTreeTable.getItemIds();
		Integer sumAmount = 0;
		Integer SIValue = 0;
		Double insuredSumInsured = this.preauthDto.getPolicyDto().getInsuredSumInsured();
		if (null == insuredSumInsured || insuredSumInsured == 0) {
			insuredSumInsured = this.preauthDto.getPolicyDto().getTotalSumInsured();
		}
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId);
			TextField netAmtProperty = (TextField) item.getItemProperty("netAmt").getValue();
			if(netAmtProperty != null) {
				String ids = (String)netAmtProperty.getData();
				Integer benefitId = Integer.valueOf(ids.split("-")[0]);
				if (focusedBenfitId.equals(benefitId)) {
					if (null != insuredSumInsured) {
						Double value = insuredSumInsured * (50d / 100);
						SIValue = Math.round(value.floatValue());
					}
				}
				if(Arrays.asList(percentageIds).contains(benefitId)) {
					sumAmount += SHAUtils.getIntegerFromString(netAmtProperty.getValue());
				}
			}
		}
		
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId);
			TextField netAmtProperty = (TextField) item.getItemProperty("netAmt").getValue();
			TextField productAmountProperty = (TextField) item.getItemProperty("ProductAmount").getValue();
			TextField payableProperty =(TextField) item.getItemProperty("payableAmt").getValue();
			TextField nonPayableProperty = (TextField) item.getItemProperty("nonPayableAmt").getValue();
			TextField claimedAmtProperty =  (TextField) item.getItemProperty("claimedAmt").getValue();
			if(netAmtProperty != null) {
				String ids = (String)netAmtProperty.getData();
				Integer benefitId = Integer.valueOf(ids.split("-")[0]);
				if(Arrays.asList(percentageIds).contains(benefitId)) {
					Integer calculatedValue = SHAUtils.getIntegerFromString(netAmtProperty.getValue());
					if(sumAmount > SIValue) {
					Double value1 =  (calculatedValue.doubleValue() * SIValue.doubleValue()); 
						Double calcValue = value1 / sumAmount;
						Long round = Math.round(calcValue);
						calculatedValue = round.intValue();
					}
					
					productAmountProperty.setReadOnly(false);
					productAmountProperty.setValue(calculatedValue.toString());
					productAmountProperty.setReadOnly(true);
					
					 Integer amountForProRata = SHAUtils.getIntegerFromString(productAmountProperty.getValue()) == 0 ?  SHAUtils.getIntegerFromString(netAmtProperty.getValue()) : Math.min(SHAUtils.getIntegerFromString(netAmtProperty.getValue()), SHAUtils.getIntegerFromString(calculatedValue.toString()));
					 payableProperty.setReadOnly(false);
					 payableProperty.setValue(amountForProRata.toString());
					 payableProperty.setReadOnly(true);
					 
					 nonPayableProperty.setReadOnly(false);
					 nonPayableProperty.setValue(ClaimedAmountRules.getNotPayableAmount(payableProperty.getValue(), netAmtProperty.getValue() != null ? netAmtProperty.getValue() : claimedAmtProperty.getValue()).toString());
					 nonPayableProperty.setReadOnly(true);
				}
			}
		}
		
	}
	
	private void microProductCalculation(String roomRentProrata) {
		Collection<?> itemIds = pmTreeTable.getItemIds();
		//Integer sumAmount = 0;
		//Integer SIValue = 0;
		Double insuredSumInsured = this.preauthDto.getPolicyDto().getInsuredSumInsured();
		if (null == insuredSumInsured || insuredSumInsured == 0) {
			insuredSumInsured = this.preauthDto.getPolicyDto().getTotalSumInsured();
		}
		
		Double limitAmt = 1500d;
		Double claimedAmount = 0d;
		Double deductibleAmt = 0d;
		if(insuredSumInsured >10000d && insuredSumInsured <= 20000d) {
			limitAmt = 3000d;
		} else if(insuredSumInsured >20000d && insuredSumInsured <= 30000d) {
			limitAmt = 4500d;
		}
		Integer resultAmt = limitAmt.intValue();
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId);
			TextField netAmtProperty = (TextField) item.getItemProperty("netAmt").getValue();
			TextField claimedAmtProperty =  (TextField) item.getItemProperty("claimedAmt").getValue();
			TextField deductibleAmtProperty =  (TextField) item.getItemProperty("deductible").getValue();
			
			if(netAmtProperty != null) {
				String ids = (String)netAmtProperty.getData();
				Integer benefitId = Integer.valueOf(ids.split("-")[0]);
				if (Arrays.asList(microProductIds).contains(benefitId)) {
					claimedAmount += SHAUtils.getDoubleValueFromString(claimedAmtProperty.getValue());
					if(benefitId.equals(10)) {
						deductibleAmt = SHAUtils.getDoubleValueFromString(deductibleAmtProperty.getValue());
					}
				}
			}
		}

		Double sumAmt = claimedAmount - deductibleAmt;
		Float value = sumAmt.floatValue() * (SHAUtils.isValidFloat(roomRentProrata) ? new Float(roomRentProrata) : 0f);
		Integer roundedValue = Math.round(value);
		if(roundedValue < limitAmt ) {
			resultAmt = roundedValue;
		}
		
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId);
			TextField netAmtProperty = (TextField) item.getItemProperty("netAmt").getValue();
			TextField productAmountProperty = (TextField) item.getItemProperty("ProductAmount").getValue();
			TextField payableProperty =(TextField) item.getItemProperty("payableAmt").getValue();
			TextField nonPayableProperty = (TextField) item.getItemProperty("nonPayableAmt").getValue();
			TextField claimedAmtProperty =  (TextField) item.getItemProperty("claimedAmt").getValue();
			TextField deductibleAmtProperty =  (TextField) item.getItemProperty("deductible").getValue();
			if(netAmtProperty != null) {
				String ids = (String)netAmtProperty.getData();
				Integer benefitId = Integer.valueOf(ids.split("-")[0]);
				if(benefitId == 100) {
					
					claimedAmtProperty.setReadOnly(false);
					claimedAmtProperty.setValue(String.valueOf(claimedAmount.intValue()) );
					claimedAmtProperty.setReadOnly(true);
					
					netAmtProperty.setReadOnly(false);
					netAmtProperty.setValue(ClaimedAmountRules.otherDetailsNetAmount(claimedAmtProperty.getValue(), deductibleAmtProperty.getValue()).toString());
					netAmtProperty.setReadOnly(true);
					
					 productAmountProperty.setReadOnly(false);
					 productAmountProperty.setValue(resultAmt.toString());
					 productAmountProperty.setReadOnly(true);
					
					 payableProperty.setReadOnly(false);
					 payableProperty.setValue(resultAmt.toString());
					 payableProperty.setReadOnly(true);
					 
					 nonPayableProperty.setReadOnly(false);
					 nonPayableProperty.setValue(ClaimedAmountRules.getNotPayableAmount(payableProperty.getValue(), netAmtProperty.getValue() != null ? netAmtProperty.getValue() : claimedAmtProperty.getValue()).toString());
					 nonPayableProperty.setReadOnly(true);
				}
			}
		}
	}

	private void setAmtRequestedCalculationValues(TextField amtTxt) {
		TextField property = amtTxt;
		String ids = (String)property.getData();
		Integer benefitId = Integer.valueOf(ids.split("-")[0]);
		String itemId = ids.split("-")[1];
		Boolean isRoomRentChanged = false;
		Item item = pmTreeTable.getItem(Integer.valueOf(itemId));
		Boolean isProrata = true;
		if(this.dbCalculationValues.get(0) != null){
			if(((Integer)this.dbCalculationValues.get(0)).equals(1)) {
				isProrata = false;
			}
		}
		
		TextField claimedNoOfDaysProperty = (TextField) item.getItemProperty("claimedDays").getValue();
		//TextField claimedPerDayAmtProperty = (TextField) item.getItemProperty("claimedDayAmt").getValue();
		TextField claimedAmtProperty =  (TextField) item.getItemProperty("claimedAmt").getValue();
		TextField deductibleProperty = (TextField) item.getItemProperty("deductible").getValue();
		TextField netAmtProperty = (TextField) item.getItemProperty("netAmt").getValue();
		
		// Product Properties
		TextField productNoOfDaysProperty = (TextField) item.getItemProperty("productDays").getValue();
		TextField productPerDayAmtProperty = (TextField) item.getItemProperty("productDayAmt").getValue();
		TextField productAmountProperty = (TextField) item.getItemProperty("ProductAmount").getValue();
		
		 // General Properties
		TextField nonPayableProperty = (TextField) item.getItemProperty("nonPayableAmt").getValue();
		TextField consideredPerDayAmt = (TextField) item.getItemProperty("consideredPerDayAmt").getValue();
		TextField payableProperty =(TextField) item.getItemProperty("payableAmt").getValue();
		TextField reasonProperty = (TextField)item.getItemProperty("reason").getValue();
		reasonProperty.setMaxLength(1000);
		reasonProperty.setNullRepresentation("");
		
		AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("Details").getValue();
		
		if(!Arrays.asList(hospDiscountId).contains(Integer.valueOf(benefitId))) {
		if(SHAUtils.getIntegerFromString(claimedAmtProperty.getValue()) < SHAUtils.getIntegerFromString(deductibleProperty.getValue())) {
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Deductible Amount should be lesser than Amount Claimed. </b>", ContentMode.HTML));
			layout.setMargin(true);
			layout.setSpacing(true);
			showErrorPopup(deductibleProperty, layout);
			return;
		}
		}
		
		if(Arrays.asList(roomRentICUId).contains(Integer.valueOf(benefitId))) {
			netAmtProperty.setReadOnly(false);
			
			netAmtProperty.setValue(ClaimedAmountRules.getClaimedNetAmount(deductibleProperty.getValue(), claimedAmtProperty.getValue()).toString());
			netAmtProperty.setReadOnly(true);
			
			if((Double)this.dbCalculationValues.get(benefitId) != null && ((Double)this.dbCalculationValues.get(benefitId)).intValue() == 0) {
				productPerDayAmtProperty.setReadOnly(false);
				productPerDayAmtProperty.setValue(ClaimedAmountRules.perDayMinAmount(String.valueOf(((Double)this.dbCalculationValues.get(benefitId)).intValue()), netAmtProperty.getValue(), claimedNoOfDaysProperty.getValue()).toString());
				productPerDayAmtProperty.setReadOnly(true);
				if(productNoOfDaysProperty != null ) {
					Integer value = SHAUtils.getIntegerFromString(productNoOfDaysProperty.getValue()) * SHAUtils.getIntegerFromString(productPerDayAmtProperty.getValue());
					productAmountProperty.setReadOnly(false);
					productAmountProperty.setValue(value.toString());
					productAmountProperty.setReadOnly(true);
				}
				
			}
			
			consideredPerDayAmt.setReadOnly(false);
			consideredPerDayAmt.setValue(ClaimedAmountRules.perDayMinAmount(productPerDayAmtProperty.getValue(), netAmtProperty.getValue(), claimedNoOfDaysProperty.getValue()).toString());
			consideredPerDayAmt.setReadOnly(true);
			
			payableProperty.setReadOnly(false);
			payableProperty.setValue(ClaimedAmountRules.getPayableAmount(productNoOfDaysProperty.getValue(), consideredPerDayAmt.getValue()).toString());
			if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null &&(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_INVIDUAL) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_FLOATER))) {
				if(Integer.valueOf(benefitId).equals(9) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > 10000) {
					payableProperty.setValue(ReferenceTable.STAR_CARE_ICU_ROOM_RENT_LIMIT);
				}
			}
			
			if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null && (preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.HEALTH_ALL_CARE))) {
				if(Integer.valueOf(benefitId).equals(8) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > 10000) {
					payableProperty.setValue(ReferenceTable.HEALTH_CARE_ROOM_RENT_LIMIT);
				}
			}
			
//			if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null &&(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL))) {
//				if(Integer.valueOf(benefitId).equals(9) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > 10000) {
//					payableProperty.setValue("10000");
//				}
//			}
			
			if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null && (preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL))) {
				Double insuredSumInsured = this.preauthDto.getPolicyDto().getInsuredSumInsured();
				if (null == insuredSumInsured || insuredSumInsured == 0) {
					insuredSumInsured = this.preauthDto.getPolicyDto().getTotalSumInsured();
				}
				Integer limitAmt = 1667;
				if(insuredSumInsured >10000d && insuredSumInsured <= 20000d) {
					limitAmt = 3333;
				} else if(insuredSumInsured >20000d && insuredSumInsured <= 30000d) {
					limitAmt = 5000;
				}
				
				if(Integer.valueOf(benefitId).equals(8) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > limitAmt) {
					payableProperty.setValue(limitAmt.toString());
				}
			}
			
			payableProperty.setReadOnly(true);
			
			nonPayableProperty.setReadOnly(false);
			nonPayableProperty.setValue(ClaimedAmountRules.getNotPayableAmount(payableProperty.getValue(), netAmtProperty.getValue() != null ? netAmtProperty.getValue() : claimedAmtProperty.getValue()).toString());
			nonPayableProperty.setReadOnly(true);
			
			if(Integer.valueOf(benefitId).equals(8) || Integer.valueOf(benefitId).equals(9) || Integer.valueOf(benefitId).equals(22)) {
				Collection<?> itemIds = pmTreeTable.getItemIds();
				Integer payableSumValue = 0;
				Integer netAmtSumValue = 0;
				for (Object eachItemId : itemIds) {
					Item eachItem = pmTreeTable.getItem(eachItemId);
					TextField claimedAmtPropertyField = (TextField) eachItem.getItemProperty("claimedAmt").getValue();
					TextField payableAmtField = (TextField) eachItem.getItemProperty("payableAmt").getValue();
					TextField netAmtField = (TextField) eachItem.getItemProperty("netAmt").getValue();
					if(claimedAmtPropertyField != null) {
						String existingIds = (String)claimedAmtPropertyField.getData();
						Integer existingBenefitId = SHAUtils.isValidInteger(existingIds.split("-")[0])  ? Integer.valueOf(existingIds.split("-")[0]) : 0;
						if(existingBenefitId.equals(8) || existingBenefitId.equals(9)|| existingBenefitId.equals(22)) {
							payableSumValue += SHAUtils.getIntegerFromString(payableAmtField.getValue());
							netAmtSumValue += SHAUtils.getIntegerFromString(netAmtField.getValue());
						}
					}
				}
				
				Float roomRentProRataValue = SHAUtils.getIntegerFromString(payableSumValue.toString()).floatValue() / SHAUtils.getIntegerFromString(netAmtSumValue.toString()).floatValue();
				if(roomRentAmountRequested != null && !roomRentAmountRequested.equals(String.valueOf(roomRentProRataValue)) ) {
					isRoomRentChanged = true;
				}	
				
				roomRentAmountRequested = String.valueOf(roomRentProRataValue);
				
				if(isRoomRentChanged) {
					setValues(getValues(), true);
				}
			}
		} else {
			if(Arrays.asList(microProductIds).contains(benefitId) && (preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL))) {
				microProductCalculation(roomRentAmountRequested);
			} 			
			else {
				if(preauthDto.getNewIntimationDTO().getIsPaayasPolicy()){
					if(Integer.valueOf(benefitId).equals(16) && null != preauthDto.getNewIntimationDTO().getHospitalDto().getDiscountPercentage() &&						
							preauthDto.getNewIntimationDTO().getHospitalDto().getDiscountPercentage()>0 && !preauthDto.getIsDefaultDeductable())
					{
						if(null != claimedAmtProperty.getValue() && !claimedAmtProperty.getValue().equalsIgnoreCase("") && !claimedAmtProperty.getValue().isEmpty()){
							
							preauthDto.setIsDefaultDeductable(Boolean.TRUE);
							Double deductableAmount = Double.valueOf(claimedAmtProperty.getValue())*(preauthDto.getNewIntimationDTO().getHospitalDto().getDiscountPercentage()/100);
							deductibleProperty.setValue(String.valueOf(Integer.valueOf(deductableAmount.intValue())));
							
						}
					}
				}
				Integer amountForProRata = 0;
				if(!Arrays.asList(hospDiscountId).contains(Integer.valueOf(benefitId))){
				netAmtProperty.setReadOnly(false);
				netAmtProperty.setValue(ClaimedAmountRules.otherDetailsNetAmount(claimedAmtProperty.getValue(), deductibleProperty.getValue()).toString());
				netAmtProperty.setReadOnly(true);
				
				productAmountProperty.setReadOnly(false);
				productAmountProperty.setValue(netAmtProperty.getValue());
				productAmountProperty.setReadOnly(true);
				
				amountForProRata = SHAUtils.getIntegerFromString(productAmountProperty.getValue()) == 0 ?  SHAUtils.getIntegerFromString(netAmtProperty.getValue()) : Math.min(SHAUtils.getIntegerFromString(netAmtProperty.getValue()), SHAUtils.getIntegerFromString(productAmountProperty.getValue()));
				}
				if(Arrays.asList(((preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null && ReferenceTable.getProductsForProporotionateDedForOthers().containsKey(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getCode())) ? proRataForSpecificProductCalculationIds :  proRataCalculationIds)).contains(benefitId)) {
					Integer prorataCalculation = 0;
					payableProperty.setReadOnly(false);
					if(benefitId.equals(18)) {
						if(detailField.getCheckboxValue()) {
							prorataCalculation =  ClaimedAmountRules.otherPackageWithRestrict(roomRentAmountRequested, amountForProRata.toString(), isProrata);
						} else {
							prorataCalculation =  ClaimedAmountRules.otherPackageWithOutRestrict(roomRentAmountRequested, amountForProRata.toString(), isProrata);
						}
					} else {
						prorataCalculation = ClaimedAmountRules.ProrataCalculation(roomRentAmountRequested, amountForProRata.toString(), isProrata);
					}
					payableProperty.setValue(prorataCalculation.toString());
					payableProperty.setReadOnly(true);
					
					if(benefitId.equals(19)) {
						payableProperty.setReadOnly(false);
						if(SHAUtils.getDoubleValueFromString(roomRentAmountRequested).equals(0d)) {
							payableProperty.setValue(netAmtProperty.getValue());
						} else {
							payableProperty.setValue(prorataCalculation.toString());
						}
						payableProperty.setReadOnly(true);
					}
					
				} else if(benefitId.equals(15)) {
					payableProperty.setReadOnly(false);
					if((Double)this.dbCalculationValues.get(benefitId) != null){
					payableProperty.setValue(ClaimedAmountRules.ambulancePayableAmount(netAmtProperty.getValue(), String.valueOf(((Double)this.dbCalculationValues.get(benefitId)).intValue())).toString());
					}
					payableProperty.setReadOnly(true);
										productAmountProperty.setReadOnly(false);
					if((Double)this.dbCalculationValues.get(benefitId) != null){
					productAmountProperty.setValue(String.valueOf(((Double)this.dbCalculationValues.get(benefitId)).intValue()));
					}
					productAmountProperty.setReadOnly(true);
					
				} else if(benefitId.equals(17)) {
					payableProperty.setReadOnly(false);
					if(detailField.getCheckboxValue()) {
						payableProperty.setValue(ClaimedAmountRules.compositePackageWithOverrideDeductionConsiderAmt(netAmtProperty.getValue()).toString());
					} else {
						payableProperty.setValue(ClaimedAmountRules.compositePackageWithoutOverrideDeductionConsiderAmt(netAmtProperty.getValue()).toString());
					}
					payableProperty.setReadOnly(true);
				} 
				else {
					if(!Arrays.asList(hospDiscountId).contains(Integer.valueOf(benefitId))){
						payableProperty.setReadOnly(false);
						payableProperty.setValue(amountForProRata.toString());
						payableProperty.setReadOnly(true);	
					}
					
				}
				
				if((Integer)this.dbCalculationValues.get(0) != null && ((Integer)this.dbCalculationValues.get(0)).equals(2)) {
					if((Arrays.asList(percentageIds).contains(benefitId) && null != ((Double)this.dbCalculationValues.get(benefitId)) &&  (((Double)this.dbCalculationValues.get(benefitId)).intValue()) <= 100)) {
						specificProductCalculation(netAmtProperty.getValue(), benefitId);
					} else if(null != ((Double)this.dbCalculationValues.get(benefitId)) && (((Double)this.dbCalculationValues.get(benefitId)).intValue()) == 25) {
							Double insuredSumInsured = this.preauthDto.getPolicyDto().getInsuredSumInsured();
							Integer SIValue=0;
							if (null != insuredSumInsured) {
								if (insuredSumInsured == 0) {
									insuredSumInsured = this.preauthDto
											.getPolicyDto().getTotalSumInsured();
								}
								Double value = insuredSumInsured * (25d / 100);
								SIValue = Math.round(value.floatValue());
							}
							
							productAmountProperty.setReadOnly(false);
							productAmountProperty.setValue(SIValue.toString());
							productAmountProperty.setReadOnly(true);
							
							Integer proRataValue = SHAUtils.getIntegerFromString(productAmountProperty.getValue()) == 0 ?  SHAUtils.getIntegerFromString(netAmtProperty.getValue()) : Math.min(SHAUtils.getIntegerFromString(netAmtProperty.getValue()), SHAUtils.getIntegerFromString(productAmountProperty.getValue()));
							payableProperty.setReadOnly(false);
							payableProperty.setValue(proRataValue.toString());
							payableProperty.setReadOnly(true);
							
							if(SIValue < SHAUtils.getIntegerFromString(productAmountProperty.getValue())) {
								productAmountProperty.setReadOnly(false);
								productAmountProperty.setValue(SIValue.toString());
								productAmountProperty.setReadOnly(true);
								
								Integer amountForProRata1 = SHAUtils.getIntegerFromString(productAmountProperty.getValue()) == 0 ?  SHAUtils.getIntegerFromString(netAmtProperty.getValue()) : Math.min(SHAUtils.getIntegerFromString(netAmtProperty.getValue()), SHAUtils.getIntegerFromString(productAmountProperty.getValue()));
								payableProperty.setReadOnly(false);
								payableProperty.setValue(amountForProRata1.toString());
								payableProperty.setReadOnly(true);
							} 
					}
				}
				
				if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null && (preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL))) {
					Double insuredSumInsured = this.preauthDto.getPolicyDto().getInsuredSumInsured();
					if (null == insuredSumInsured || insuredSumInsured == 0) {
						insuredSumInsured = this.preauthDto.getPolicyDto().getTotalSumInsured();
					}
					Integer limitAmt = 1500;
					if(insuredSumInsured >10000d && insuredSumInsured <= 20000d) {
						limitAmt = 3000;
					} else if(insuredSumInsured >20000d && insuredSumInsured <= 30000d) {
						limitAmt = 4500;
					}
					
					if(Integer.valueOf(benefitId).equals(11) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > limitAmt) {
						payableProperty.setReadOnly(false);
						payableProperty.setValue(limitAmt.toString());
						payableProperty.setReadOnly(true);
					}
				}
				
				if(!Arrays.asList(hospDiscountId).contains(Integer.valueOf(benefitId))){
					nonPayableProperty.setReadOnly(false);
					nonPayableProperty.setValue(ClaimedAmountRules.getNotPayableAmount(payableProperty.getValue(), netAmtProperty.getValue() != null ? netAmtProperty.getValue() : claimedAmtProperty.getValue()).toString());
					nonPayableProperty.setReadOnly(true);	
				}
				
				
				
			}
			
		}
		
		updateSummation();
		
//		Object itemId = property.getData();
//		if((int)itemId == 2) {
//			
//			if(roomRentAmountRequested != null &&  SHAUtils.isValidFloat(roomRentAmountRequested) && SHAUtils.isValidFloat(amountRequestedValue)  && !roomRentAmountRequested.equals(amountRequestedValue) ) {
//				isRoomRentChanged = true;
//			}	
//			roomRentAmountRequested = amountRequestedValue;
//		}
//		Item item = pmTreeTable.getItem(itemId);
//		TextField deductibleField = (TextField) item.getItemProperty("Deductibles").getValue();
//		TextField netAmountField = (TextField) item.getItemProperty("Net Amount").getValue();
//		TextField amountConsideredField = (TextField) item.getItemProperty("Amount Considered").getValue();
//		AmountRequestedField amountRequestedField = (AmountRequestedField) item.getItemProperty("Amount Requested").getValue();
//		AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("Details").getValue();
//		Object benefitId = detailField.getData();
//		PreauthRule rule = new PreauthRule();
//			netAmountField.setReadOnly(false);
//			amountConsideredField.setReadOnly(false);
//			if(amountRequestedValue == null || amountRequestedValue == "0" || amountRequestedValue == "0.0" || amountRequestedValue.length() == 0) {
//				amountRequestedValue = "0";
//			}
//			
//			if(!isANHOrCompositeSelected && (int)benefitId != 8 && (int)benefitId != 9 && SHAUtils.getFloatFromString(amountRequestedValue) < SHAUtils.getFloatFromString(deductibleField.getValue())) {
//				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Deductible Amount should be lesser than Amount Requested. </b>", ContentMode.HTML));
//				layout.setMargin(true);
//				layout.setSpacing(true);
//				final ConfirmDialog dialog = new ConfirmDialog();
//				dialog.setClosable(true);
//				dialog.setResizable(false);
//				dialog.setContent(layout);
//				dialog.setCaption("Error");
//				dialog.setClosable(true);
//				dialog.show(getUI().getCurrent(), null, true);
//				deductibleField.setReadOnly(false);
//				deductibleField.setValue("0");
//				return;
//			}
//			
//			updateSummation();
//			if(Arrays.asList(roomRentICUId).contains(itemId)) {
//				String roomRentNetAmountChargesCalculation = rule.roomRentNetAmountChargesCalculation(amountRequestedField.getNoOfDays().toString(), dbCalculationValues != null ? dbCalculationValues.get(benefitId.toString()).toString() : "0");
//				String amountConsideredValue = "0";
//				amountConsideredValue = rule.roomRentAmountConsideredChargesCalculation(amountRequestedField.getAmount().toString(), roomRentNetAmountChargesCalculation);
//				
//				netAmountField.setValue(amountConsideredValue);
//				
//				String roomRentDeductibleChargesCalculation = rule.roomRentNetDeductibleChargesCalculation(property.getValue(), amountConsideredValue);
//				deductibleField.setReadOnly(false);
//				deductibleField.setValue(roomRentDeductibleChargesCalculation);
//				deductibleField.setReadOnly(true);
//				
//				amountConsideredField.setReadOnly(false);
//				amountConsideredField.setValue(amountConsideredValue);
//				if((int)itemId == 2) {
//					roomRentAmountConsidered = amountConsideredValue;
//				}
//				amountConsideredField.setReadOnly(true);
//				
//				if(isRoomRentChanged) {
//					setValues(getValues(), true);
//				}
//				
//			} else if((int)itemId == 10) {
//				String roomRentNetAmountChargesCalculation = rule.roomRentNetAmountChargesCalculation("1", dbCalculationValues != null ? dbCalculationValues.get(benefitId.toString()).toString() : "0");
//				String amountConsideredValue = "0";
//				amountConsideredValue = rule.roomRentAmountConsideredChargesCalculation(amountRequestedField.getAmount().toString(), roomRentNetAmountChargesCalculation);
//				
//				netAmountField.setValue(amountConsideredValue);
//				
//				String roomRentDeductibleChargesCalculation = rule.roomRentNetDeductibleChargesCalculation(property.getValue(), roomRentNetAmountChargesCalculation);
//				deductibleField.setReadOnly(false);
//				deductibleField.setValue(roomRentDeductibleChargesCalculation);
//			
//				amountConsideredField.setReadOnly(false);
//				amountConsideredField.setValue(amountConsideredValue);
//			}
//			
//			else {
//				if((int)itemId == 12 || (int)itemId == 13) {
//					Boolean isEnabled = true;
//					String value = property.getValue();
//					if(!isANHOrCompositeSelected && value != null &&value.length() > 0 && !(value.equalsIgnoreCase("0.0") || value.equalsIgnoreCase("0")) ) {
//						enableOrdisableItem(false, itemId, true);
//					}  else if(value != null && isANHOrCompositeSelected && (value.length() == 0 || value.equalsIgnoreCase("0.0") || value.equalsIgnoreCase("0"))) {
//						Item previousItem = (int)itemId == 13 ? pmTreeTable.getItem(12) : pmTreeTable.getItem(13);
//						AmountRequestedField previousAmountRequestedField = (AmountRequestedField) previousItem.getItemProperty("Amount Requested").getValue();
//						if(previousAmountRequestedField.getAmount() <= 0) {
//							enableOrdisableItem(true, null, true);
//						}
//					}
//				}
//				
//				String otherDetailsNetAmountCalculation = null;
//				otherDetailsNetAmountCalculation = rule.otherDetailsNetAmountCalculation(property.getValue(),deductibleField.getValue());
//				netAmountField.setReadOnly(false);
//				netAmountField.setValue(otherDetailsNetAmountCalculation);
//				netAmountField.setReadOnly(true);
//				
//				String otherDetailsAmountConsideredCalculation = otherDetailsNetAmountCalculation;
//				
//				if((int)itemId == 13) {
//					if(detailField.getCheckboxValue()) {
//						otherDetailsAmountConsideredCalculation = rule.compositePackageWithOverrideDeductionConsiderAmt(otherDetailsNetAmountCalculation);
//					} else {
//						otherDetailsAmountConsideredCalculation = rule.compositePackageWithoutOverrideDeductionConsiderAmt(otherDetailsNetAmountCalculation);
//					}
//				} else if((int)itemId == 14) {
//					if(detailField.getCheckboxValue()) {
//						otherDetailsAmountConsideredCalculation = rule.otherPackageWithRestrict(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
//					} else {
//						otherDetailsAmountConsideredCalculation = rule.otherPackageWithOutRestrict(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
//					}
//				} else if(!Arrays.asList(amountConsiderCalculationIds).contains(itemId)) {
//					otherDetailsAmountConsideredCalculation = rule.otherDetailsAmountConsideredCalculation(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
//				} 
//				amountConsideredField.setReadOnly(false);
//				amountConsideredField.setValue(otherDetailsAmountConsideredCalculation);	
//			}
//			netAmountField.setReadOnly(true);
//			amountConsideredField.setReadOnly(true);
	} 
	
	
	/*private ValueChangeListener getDeductibleListener() {
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = -6840859302219285675L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				TextField property = (TextField) event.getProperty();
				if(!SHAUtils.isValidFloat(property.getValue())) {
					property.setValue("0");
					Notification notify = new Notification("Message");
					notify.setDelayMsec(-1);
//					Notification.show("Please Enter Valid Deductible Amount.");
				} else {
					setDeductibleCalcualtionVaules(property);
				}
				
			}
		};
			return listener;
	}*/
	
	
	/*private void setDeductibleCalcualtionVaules(TextField deductibleTxt) {
		TextField property = deductibleTxt;
		Object itemId = property.getData();
		Item item = pmTreeTable.getItem(itemId);
		TextField netAmountField = (TextField) item.getItemProperty("Net Amount").getValue();
		TextField amountConsideredField = (TextField) item.getItemProperty("Amount Considered").getValue();
		AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("Details").getValue();
		Object benefitId = detailField.getData();
		AmountRequestedField amountRequestedField = (AmountRequestedField) item.getItemProperty("Amount Requested").getValue();
		Integer amountRequest = amountRequestedField.getAmount();
		PreauthRule rule = new PreauthRule();
		String deductibleAmt = property.getValue();
		
		netAmountField.setReadOnly(false);
		amountConsideredField.setReadOnly(false);
		
		if(deductibleAmt == null || deductibleAmt == "0" || deductibleAmt == "0.0" || deductibleAmt.length() == 0) {
			deductibleAmt = "0";
		}
		
		if((int)benefitId != 8 && (int)benefitId != 9 && amountRequest < SHAUtils.getFloatFromString(deductibleAmt)) {
			VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Deductible Amount should be lesser than Amount Requested. </b>", ContentMode.HTML));
			layout.setMargin(true);
			layout.setSpacing(true);
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(true);
			dialog.setResizable(false);
			dialog.setContent(layout);
			dialog.setCaption("Error");
			dialog.setClosable(true);
			dialog.show(getUI().getCurrent(), null, true);
			property.setReadOnly(false);
			property.setValue("0");
			deductibleAmt = "0";
			return;
		}
		
		updateSummation();
		
		if(!Arrays.asList(roomRentICUId).contains(itemId)) {
				if((int)itemId == 12 || (int)itemId == 13) {
					String value = property.getValue();
					if( value != null &&value.length() > 0 && !(value.equalsIgnoreCase("0.0") || value.equalsIgnoreCase("0")) ) {
						enableOrdisableItem(false, itemId, true);
					}  else if(value != null && value.length() <= 0) {
						Item previousItem = (int)itemId == 13 ? pmTreeTable.getItem(12) : pmTreeTable.getItem(13);
						AmountRequestedField previousAmountRequestedField = (AmountRequestedField) previousItem.getItemProperty("Amount Requested").getValue();
						if(previousAmountRequestedField.getAmount() <= 0) {
							enableOrdisableItem(true, null, true);
						}
					}
				}
				String otherDetailsNetAmountCalculation = null;
				if((int)itemId == 14) {
					otherDetailsNetAmountCalculation = rule.otherPackageNetAmount(amountRequest.toString(),deductibleAmt);
				} else {
					otherDetailsNetAmountCalculation = rule.otherDetailsNetAmountCalculation(amountRequest.toString(),deductibleAmt);
				}
				netAmountField.setReadOnly(false);
				netAmountField.setValue(otherDetailsNetAmountCalculation);
				netAmountField.setReadOnly(true);
				
				String otherDetailsAmountConsideredCalculation = otherDetailsNetAmountCalculation;
				
				if((int)itemId == 13) {
					if(detailField.getCheckboxValue()) {
						otherDetailsAmountConsideredCalculation = rule.compositePackageWithOverrideDeductionConsiderAmt(otherDetailsNetAmountCalculation);
					} else {
						otherDetailsAmountConsideredCalculation = rule.compositePackageWithoutOverrideDeductionConsiderAmt(otherDetailsNetAmountCalculation);
					}
				}else if((int)itemId == 14) {
					if(detailField.getCheckboxValue()) {
						otherDetailsAmountConsideredCalculation = rule.otherPackageWithRestrict(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
					} else {
						otherDetailsAmountConsideredCalculation = rule.otherPackageWithOutRestrict(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
					}
				}
				else if(!Arrays.asList(amountConsiderCalculationIds).contains(itemId)) {
					otherDetailsAmountConsideredCalculation = rule.otherDetailsAmountConsideredCalculation(otherDetailsNetAmountCalculation, roomRentAmountConsidered, roomRentAmountRequested);
				} 
				amountConsideredField.setReadOnly(false);
				amountConsideredField.setValue(otherDetailsAmountConsideredCalculation);
				amountConsideredField.setReadOnly(true);
			}
		netAmountField.setReadOnly(true);
		amountConsideredField.setReadOnly(true);
	}*/
	
	@SuppressWarnings("unused")
	public void enableOrdisableItem(Boolean isEnabled, Object itemId, Boolean clearValueFlag) {
		isANHOrCompositeSelected = !isEnabled;
		Collection<?> itemIds = pmTreeTable.getItemIds();
		for (Object object : itemIds) {
			Item item = pmTreeTable.getItem(object); 
			if(itemId != null && (int)object == (int)itemId) {
				continue;
			}
			Iterator<?> iterator = item.getItemPropertyIds().iterator();
			while(iterator.hasNext()) {
				Object propertyField = item.getItemProperty(iterator.next()).getValue();
				if(propertyField != null) {
					if(propertyField instanceof AmountRequestedField) {
						AmountRequestedField fullNameField = (AmountRequestedField) propertyField;
						fullNameField.enableOrDisableValue(isEnabled, object);
					}  else if(propertyField instanceof AmountRequestedDetailField) {
						AmountRequestedDetailField detailField = (AmountRequestedDetailField) propertyField;
						detailField.setEnabled(isEnabled);
					} else if(propertyField instanceof Button) {
						Button button = (Button) propertyField;
						button.setEnabled(false);
					}
					else {
						Component  field = (Component) propertyField;
						if(!(field instanceof Label)){
							AbstractField<?>  abstractField = (AbstractField<?>) field;
							Boolean isReadOnly = false;
							if(abstractField.isReadOnly()) {
								abstractField.setReadOnly(false);
								isReadOnly = true;
							}
							abstractField.setReadOnly(isReadOnly);
						}
						field.setEnabled(isEnabled);
					}
				}
			}
		}
	}
	
	/**
	 * The below method will get the values from the 
	 * text field and will set it to DTO. DTO is required
	 * for persisting value in DB.
	 * */
	
	public List<NoOfDaysCell> getValues() {
		Collection<?> itemIds = pmTreeTable.getItemIds();
		List<NoOfDaysCell> listOfDTO = new ArrayList<NoOfDaysCell>();
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId); 
			NoOfDaysCell values = new NoOfDaysCell();
			TextField claimedNoOfDaysProperty = (TextField) item.getItemProperty("claimedDays").getValue();
			TextField claimedPerDayAmtProperty = (TextField) item.getItemProperty("claimedDayAmt").getValue();
			TextField claimedAmtProperty =  (TextField) item.getItemProperty("claimedAmt").getValue();
			TextField deductibleProperty = (TextField) item.getItemProperty("deductible").getValue();
			TextField netAmtProperty = (TextField) item.getItemProperty("netAmt").getValue();
			// Product Properties
			TextField productNoOfDaysProperty = (TextField) item.getItemProperty("productDays").getValue();
			TextField productPerDayAmtProperty = (TextField) item.getItemProperty("productDayAmt").getValue();
			TextField productAmountProperty = (TextField) item.getItemProperty("ProductAmount").getValue();
			 // General Properties
			TextField nonPayableProperty = (TextField) item.getItemProperty("nonPayableAmt").getValue();
			TextField consideredPerDayAmt = (TextField) item.getItemProperty("consideredPerDayAmt").getValue();
			TextField payableProperty =(TextField) item.getItemProperty("payableAmt").getValue();
			TextField reasonProperty = (TextField)item.getItemProperty("reason").getValue();
			AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("Details").getValue();
			String ids = (String)detailField.getData();
			Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0])  ? Integer.valueOf(ids.split("-")[0]) : 0;
			values.setBenefitId(benefitId.longValue());
			//String itemID = ids.split("-")[1];
			
			if(Arrays.asList(childItemId).contains(benefitId)) {
				if(benefitId == 17) {
					values.setOverridePackageDeductionFlag(detailField.getCheckboxValue() ? "Y" : "N");
				} else if(benefitId == 18) {
					values.setRestrictToFlag(detailField.getCheckboxValue() ? "Y" : "N");
				}
				if(Arrays.asList(roomRentICUId).contains(benefitId)) {
					values.setTotalBillingDays(null != claimedNoOfDaysProperty ? SHAUtils.convertFloatToString(claimedNoOfDaysProperty.getValue()) : 0);
					values.setBillingPerDayAmount(null != claimedPerDayAmtProperty ?  SHAUtils.getIntegerFromString(claimedPerDayAmtProperty.getValue()) : 0);
					values.setTotalDaysForPolicy(null != productNoOfDaysProperty ?  SHAUtils.convertFloatToString(productNoOfDaysProperty.getValue()) : 0);
					values.setPolicyPerDayPayment(null != productPerDayAmtProperty ?  SHAUtils.getIntegerFromString(productPerDayAmtProperty.getValue()) : 0);
				}
				
				//values.setBillingPerDayAmount(null != claimedAmtProperty ? SHAUtils.getIntegerFromString(claimedAmtProperty.getValue()) : 0);
				values.setClaimedBillAmount(null != claimedAmtProperty ? SHAUtils.getIntegerFromString(claimedAmtProperty.getValue()) : 0);
				values.setDeductibleAmount(null != deductibleProperty  ?  SHAUtils.getIntegerFromString(deductibleProperty.getValue()) : 0);
				values.setNetAmount(null != netAmtProperty ?  SHAUtils.getIntegerFromString(netAmtProperty.getValue()) : 0);
				
				values.setPolicyMaxAmount(null != productAmountProperty ?  SHAUtils.getIntegerFromString(productAmountProperty.getValue()) : 0);
				values.setNonPayableAmount(null != nonPayableProperty ? SHAUtils.getIntegerFromString(nonPayableProperty.getValue()) : 0);
				values.setPaybleAmount(null != payableProperty ?  SHAUtils.getIntegerFromString(payableProperty.getValue()) : 0);
				values.setConsiderPerDayAmt(null != consideredPerDayAmt ?  SHAUtils.getIntegerFromString(consideredPerDayAmt.getValue()) : 0);
				values.setNonPayableReason(reasonProperty.getValue());
				
				if(reasonProperty.getData() != null) {
					values.setKey((Long)reasonProperty.getData());
				}
				
				listOfDTO.add(values);
			}
		}
		return listOfDTO;
	}
	
	private void setValuesToField(TextField field, Object value) {
		Boolean isReadOnly = false;
		if(field != null){
			if(field.isReadOnly()) {
				isReadOnly = true;
			}
			field.setReadOnly(false);
			if(value != null) {
				if(value instanceof Integer) {
					if((Integer)value != 0) {
						field.setValue(value.toString());
					}
				} else if(value instanceof Float) {
					if((Float)value != 0) {
						field.setValue(value.toString());
					}
				}
			}
			setAmtRequestedCalculationValues(field);
			field.setReadOnly(isReadOnly);
		}
		
	}
	
	public void setValues(List<NoOfDaysCell> listofDTO, Boolean isReProData) {
		
		// Before setting the value we should add multiplerows if exists.
		addMultipleRows(listofDTO);
		
		Collection<?> itemIds = pmTreeTable.getItemIds();
		//List<NoOfDaysCell> listOfDTO = new ArrayList<NoOfDaysCell>();
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId); 
			NoOfDaysCell exactDTO = new NoOfDaysCell();
			//NoOfDaysCell values = new NoOfDaysCell();
			TextField claimedNoOfDaysProperty = (TextField) item.getItemProperty("claimedDays").getValue();
			TextField claimedPerDayAmtProperty = (TextField) item.getItemProperty("claimedDayAmt").getValue();
			TextField claimedAmtProperty =  (TextField) item.getItemProperty("claimedAmt").getValue();
			TextField deductibleProperty = (TextField) item.getItemProperty("deductible").getValue();
			TextField netAmtProperty = (TextField) item.getItemProperty("netAmt").getValue();
			// Product Properties
			TextField productNoOfDaysProperty = (TextField) item.getItemProperty("productDays").getValue();
			//TextField productPerDayAmtProperty = (TextField) item.getItemProperty("productDayAmt").getValue();
			TextField productAmountProperty = (TextField) item.getItemProperty("ProductAmount").getValue();
			 // General Properties
			TextField nonPayableProperty = (TextField) item.getItemProperty("nonPayableAmt").getValue();
			TextField consideredPerDayAmt = (TextField) item.getItemProperty("consideredPerDayAmt").getValue();
			TextField payableProperty =(TextField) item.getItemProperty("payableAmt").getValue();
			TextField reasonProperty = (TextField)item.getItemProperty("reason").getValue();
			AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("Details").getValue();
			String ids = (String)detailField.getData();
			Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0]) ? Integer.valueOf(ids.split("-")[0]) : 0;
			//String itemID = ids.split("-")[1];
			if(Arrays.asList(childItemId).contains(benefitId)) {
				exactDTO = getExactDTO(benefitId.longValue(), listofDTO, isReProData);
				reasonProperty.setData(exactDTO.getKey());
				if(isReProData) {
					if(Arrays.asList(proRataCalculationIds).contains(benefitId)) {
						setValuesToField(claimedAmtProperty, exactDTO.getClaimedBillAmount());
//						setValuesToField(deductibleProperty, exactDTO.getDeductibleAmount());
//						setValuesToField(netAmtProperty, exactDTO.getNetAmount());
//						setValuesToField(productAmountProperty, exactDTO.getPolicyMaxAmount());
//						
//						setValuesToField(nonPayableProperty, exactDTO.getNonPayableAmount());
//						setValuesToField(payableProperty, exactDTO.getPaybleAmount());
//						setValuesToField(consideredPerDayAmt, exactDTO.getConsiderPerDayAmt());
//						
//						reasonProperty.setValue(exactDTO.getNonPayableReason());
					}
				} else {
					if(benefitId == 17) {
						detailField.getCheckboxField().setValue((exactDTO.getOverridePackageDeductionFlag() != null && exactDTO.getOverridePackageDeductionFlag().toLowerCase().equalsIgnoreCase("y"))  ? true : false);
					} else if(benefitId == 18) {
						detailField.getCheckboxField().setValue((exactDTO.getRestrictToFlag() != null && exactDTO.getRestrictToFlag().toLowerCase().equalsIgnoreCase("y")) ? true : false);
					}
					if(Arrays.asList(roomRentICUId).contains(benefitId)) {
						setValuesToField(claimedNoOfDaysProperty, exactDTO.getTotalBillingDays());
						setValuesToField(claimedPerDayAmtProperty, exactDTO.getBillingPerDayAmount());
						setValuesToField(productNoOfDaysProperty, exactDTO.getTotalDaysForPolicy());
//						setValuesToField(productPerDayAmtProperty, exactDTO.getPolicyPerDayPayment());
					}
						setValuesToField(claimedAmtProperty, exactDTO.getClaimedBillAmount());
						setValuesToField(deductibleProperty, exactDTO.getDeductibleAmount());
						/*if(!preauthDto.getNewIntimationDTO().getIsPaayasPolicy()){
							setValuesToField(deductibleProperty, exactDTO.getDeductibleAmount());
						}*/
						setValuesToField(netAmtProperty, exactDTO.getNetAmount());
						setValuesToField(productAmountProperty, exactDTO.getPolicyMaxAmount());
						
						setValuesToField(nonPayableProperty, exactDTO.getNonPayableAmount());
						setValuesToField(payableProperty, exactDTO.getPaybleAmount());
						setValuesToField(consideredPerDayAmt, exactDTO.getConsiderPerDayAmt());
						
						reasonProperty.setValue(exactDTO.getNonPayableReason());
						reasonProperty.setDescription("Click the Text Box and Press F8 For Detailed Popup");
				}
			}
		}
		
		pmTreeTable.setFooterVisible(true);
	}

	private void addMultipleRows(List<NoOfDaysCell> listofDTO) {
		List<Integer> asList = Arrays.asList(addButtonIds);
		for (Integer benefitID : asList) {
			List<NoOfDaysCell> multiplesRowDTO = getMultiplesRowDTO(benefitID.longValue(), listofDTO);
			Item item = null;
			Collection<?> ids = pmTreeTable.getItemIds();
			
			for (Object eachItemId : ids) {
				Item eachItem = pmTreeTable.getItem(eachItemId);
				TextField claimedAmtProperty = (TextField) eachItem.getItemProperty("claimedAmt").getValue();
				if(claimedAmtProperty != null) {
					String concatenateIDs = (String)claimedAmtProperty.getData();
					Integer benefitId = SHAUtils.isValidInteger(concatenateIDs.split("-")[0])  ? Integer.valueOf(concatenateIDs.split("-")[0]) : 0;
					String itemId = concatenateIDs.split("-")[1];
					if(benefitId.equals(benefitID)) {
						item = pmTreeTable.getItem(SHAUtils.isValidInteger(itemId)  ? Integer.valueOf(itemId) : 0);
					}
				}
			}
			
			if(item != null) {
				Button addButton = (Button) item.getItemProperty("add").getValue();
				if(!multiplesRowDTO.isEmpty() && multiplesRowDTO.size() > 1) {
					for(int i=0; i < multiplesRowDTO.size()-1; i++) {
						addRow(addButton);
					}
				}
			}
			
		}
	}
	
	/*private Integer populateFromFieldToDTO(String value)
	{
		if(SHAUtils.isValidInteger(value))
		{
			return Integer.parseInt(value);
		}
		else
		{
			return null;
		}
	}*/
	
	public NoOfDaysCell getExactDTO(Long benefitId, List<NoOfDaysCell> listOfDTO, Boolean isReProData) {
		NoOfDaysCell cell = new NoOfDaysCell();
		List<NoOfDaysCell> claimedDetailsList = this.preauthDto.getPreauthDataExtractionDetails().getClaimedDetailsList();
		if(isReProData) {
			claimedDetailsList = listOfDTO;
		}
		for (NoOfDaysCell noOfDaysCell : claimedDetailsList) {
			if(noOfDaysCell.getBenefitId() == benefitId) {
				cell.setKey(noOfDaysCell.getKey());
				cell = noOfDaysCell;
				break;
			}
		}
		listOfDTO.remove(cell);
		return cell;
	}
	
	public NoOfDaysCell getExactDTOForSection(Long benefitId, List<NoOfDaysCell> listOfDTO) {
		NoOfDaysCell cell = new NoOfDaysCell();
		for (NoOfDaysCell noOfDaysCell : listOfDTO) {
			if(noOfDaysCell.getBenefitId() == benefitId) {
				cell = noOfDaysCell;
				break;
			}
		}
		return cell;
	}
	
	public List<NoOfDaysCell> getMultiplesRowDTO(Long benefitId, List<NoOfDaysCell> listOfDTO) {
		//NoOfDaysCell cell = new NoOfDaysCell();
		List<NoOfDaysCell> list = new ArrayList<NoOfDaysCell>();
		for (NoOfDaysCell noOfDaysCell : listOfDTO) {
			if(noOfDaysCell.getBenefitId() == benefitId) {
				list.add(noOfDaysCell);
			}
		}
		return list;
	}
	
	public Boolean isValid(Boolean isFinalEnhancement) {
		errorMessages.removeAll(errorMessages);
		Collection<?> itemIds = pmTreeTable.getItemIds();
		//List<Integer> amount = new ArrayList<Integer>();
		List<String> roomRentCPuEntered = new ArrayList<String>();
		List<String> roomRentCPuNotEnteredEntered = new ArrayList<String>();
		List<String> mandatoryList = new ArrayList<String>();
		List<String> anhSelectedList = new ArrayList<String>();
		Boolean hasError = false;
		totalNoOfDays = 0f;
		totalEntitlementDays = 0f;
		for (Object itemId : itemIds) {
			Item eachItem = pmTreeTable.getItem(itemId);
			TextField claimedAmtProperty = (TextField) eachItem.getItemProperty("claimedAmt").getValue();
			TextField claimedPerDayAmtProperty = (TextField) eachItem.getItemProperty("claimedDayAmt").getValue();
			TextField claimedNoOfDaysProperty = (TextField) eachItem.getItemProperty("claimedDays").getValue();
			TextField productNoOfDays = (TextField) eachItem.getItemProperty("productDays").getValue();
			
			TextField deductibleProperty = (TextField) eachItem.getItemProperty("deductible").getValue();
			
			if(claimedAmtProperty != null) {
				String ids = (String)claimedAmtProperty.getData();
				Integer benefitId = (null != ids && SHAUtils.isValidInteger(ids.split("-")[0])) ? Integer.valueOf(ids.split("-")[0]) : 0;
				if(Arrays.asList(childItemId).contains(benefitId)) {
					
					if(Arrays.asList(roomRentICUId).contains(benefitId)) {
						
						if(SHAUtils.getIntegerFromString(claimedAmtProperty.getValue()) > 0 && SHAUtils.convertFloatToString(productNoOfDays.getValue()).equals(0)) {
							hasError = true;
							errorMessages.add("Please Enter Entitlement No of Days.");
						}
						
						if(SHAUtils.getIntegerFromString(claimedPerDayAmtProperty.getValue()) > 0 && SHAUtils.convertFloatToString(claimedNoOfDaysProperty.getValue()).equals(0)) {
							hasError = true;
							errorMessages.add("Please Enter Claimed No of Days.");
						}
						
						
						if(SHAUtils.convertFloatToString(claimedNoOfDaysProperty.getValue()) < SHAUtils.convertFloatToString(productNoOfDays.getValue())) {
							hasError = true;
							errorMessages.add("Entitlement No Of Days should not be greater than Claimed No of Days.");
						}
						totalNoOfDays += SHAUtils.convertFloatToString(claimedNoOfDaysProperty.getValue());
						totalEntitlementDays += SHAUtils.convertFloatToString(productNoOfDays.getValue());
						
						if((SHAUtils.getIntegerFromString(claimedAmtProperty.getValue()) > 0)) {
							roomRentCPuEntered.add(benefitId.toString());
						} else {
							roomRentCPuNotEnteredEntered.add(benefitId.toString());
						}
					}
					//Item item = pmTreeTable.getItem(itemId);
					if((SHAUtils.getIntegerFromString(claimedAmtProperty.getValue()) > 0)) {
						mandatoryList.add(benefitId.toString());
					}
					
					if(Arrays.asList(anhItemId).contains(benefitId) && SHAUtils.getIntegerFromString(claimedAmtProperty.getValue()) > 0) {
						anhSelectedList.add(benefitId.toString());
					}
					
					//IMSSUPPOR-27169
					if(!Arrays.asList(hospDiscountId).contains(Integer.valueOf(benefitId))) {
						if(SHAUtils.getIntegerFromString(claimedAmtProperty.getValue()) < SHAUtils.getIntegerFromString(deductibleProperty.getValue())) {
							hasError = true;
							errorMessages.add("Deductible Amount should be lesser than Amount Claimed.");
						}
					}
					
				}
			}
		}
		if(mandatoryList.isEmpty()) {
			hasError = true;
			errorMessages.add("One Entry is mandatory in Amount Claimed Table");
		} else if(roomRentCPuEntered.isEmpty() && anhSelectedList.isEmpty()) {
			if(isFinalEnhancement) {
				hasError = true;
				errorMessages.add("Please Enter at least Room Rent or ICU");
			}
			
		} else if(!mandatoryList.isEmpty() && !roomRentCPuNotEnteredEntered.isEmpty()) {
//			hasError = true;
//			errorMessages.add("Please Enter Room Rent or ICU");
		} 
		
		return !hasError;
	}
	
	
	@SuppressWarnings("unchecked")
	private void addRow (Button selectedButton) {
		String ids = (String)selectedButton.getData();
		Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0])  ? Integer.valueOf(ids.split("-")[0]) : 0 ; //Integer.valueOf(ids.split("-")[0]);
		String itemId = ids.split("-")[1];
		
		Item item = pmTreeTable.getItem(SHAUtils.isValidInteger(itemId)  ? Integer.valueOf(itemId) : 0);
//		Label selectedField = (Label) item.getItemProperty("S.No").getValue();
		
		Object duplicateItemId = pmTreeTable.addItemAfter(SHAUtils.isValidInteger(itemId)  ? Integer.valueOf(itemId) : 0);
		pmTreeTable.setParent(duplicateItemId, SHAUtils.isValidInteger(itemId)  ? Integer.valueOf(itemId) : 0);
		pmTreeTable.setChildrenAllowed(duplicateItemId, false);
		
		
		Item addedItem = pmTreeTable.getItem(duplicateItemId);
		Iterator<?> iterator = item.getItemPropertyIds().iterator();
		while (iterator.hasNext()) {
			String propertyId = (String) iterator.next();
			Object propertyField = item.getItemProperty(propertyId).getValue();
			if(propertyField instanceof Label) {
				Label oldLabel = (Label) propertyField;
				Label label = new Label();
				label.setCaption(oldLabel.getCaption());
				label.setValue(String.valueOf(new Float(oldLabel.getValue())));
//				addedItem.getItemProperty(propertyId).setValue(label);
			} else if(propertyField instanceof TextField) {
				TextField oldText = (TextField) propertyField;
				TextField newText = new TextField();
				newText.setData(benefitId.toString() + "-" + duplicateItemId.toString());
				if(propertyId.equalsIgnoreCase("reason")) {
					newText.setData(null);
					newText.setMaxLength(100);
				}
				
				newText.setCaption(oldText.getCaption());
				
				newText.setEnabled(oldText.isEnabled());
				
				if(propertyId.equalsIgnoreCase("productDayAmt")) {
					newText.setValue(oldText.getValue());
					CSValidator validator = new CSValidator();
					validator.extend(newText);
					validator.setRegExp("^[0-9]*$"); // Should allow only 0 to 9 and '.'
					validator.setPreventInvalidTyping(true);
				}
				newText.setReadOnly(oldText.isReadOnly());
				newText.setStyleName(oldText.getStyleName());
				if(propertyId.equalsIgnoreCase("claimedDays") || propertyId.equalsIgnoreCase("claimedDayAmt")) {
					newText.addBlurListener(getClaimedNoOfDaysListener());
					CSValidator validator = new CSValidator();
					validator.extend(newText);
					validator.setRegExp("^[0-9]*$");
					if(propertyId.equalsIgnoreCase("claimedDays")) {
						validator.setRegExp("^[0-9.]*$");
					}
					 // Should allow only 0 to 9 and '.'
					validator.setPreventInvalidTyping(true);
				} else if(propertyId.equalsIgnoreCase("productDays")) {
					newText.addValueChangeListener(getProductNoOfDaysListener());
					CSValidator validator = new CSValidator();
					validator.extend(newText);
					validator.setRegExp("^[0-9.]*$"); // Should allow only 0 to 9 and '.'
					validator.setPreventInvalidTyping(true);
				} else if(propertyId.equalsIgnoreCase("claimedAmt") || propertyId.equalsIgnoreCase("deductible")) {
					newText.addValueChangeListener(getAmountListener(true));
					CSValidator validator = new CSValidator();
					validator.extend(newText);
					validator.setRegExp("^[0-9]*$"); // Should allow only 0 to 9 and '.'
					validator.setPreventInvalidTyping(true);
				} else if(propertyId.equalsIgnoreCase("ProductAmount") && Arrays.asList(roomRentICUId).contains(benefitId)) {
					newText.addValueChangeListener(getAmountListener(true));
					CSValidator validator = new CSValidator();
					validator.extend(newText);
					validator.setRegExp("^[0-9]*$"); // Should allow only 0 to 9 and '.'
					validator.setPreventInvalidTyping(true);
				} else if(propertyId.equalsIgnoreCase("reason")) {
					getNonPayableReasonShortcutListener(((TextField)newText), null);
				}
				
				float width2 = oldText.getWidth();
				newText.setWidth(width2, Unit.PIXELS);
				addedItem.getItemProperty(propertyId).setValue(newText);
			} else if(propertyField instanceof Button) {
				Button button = new Button("Delete");
				addedItem.getItemProperty(propertyId).setValue(button);
			} else if(propertyField instanceof AmountRequestedDetailField) {
				AmountRequestedDetailField oldField = (AmountRequestedDetailField) propertyField;
				AmountRequestedDetailField field = new AmountRequestedDetailField(true, oldField.getLabelValue(), null);
				field.setData(benefitId.toString() + "-" + duplicateItemId.toString());
				addedItem.getItemProperty(propertyId).setValue(field);
			}
		}
//		
//		Collection<?> itemIds = pmTreeTable.getItemIds();
//		Integer size = itemIds.size();
//		Float addedValue = size.floatValue() /10;
//		Float i = addedValue;
//		for (Object eachItemId : itemIds) {
//			Item eachItem = pmTreeTable.getItem(eachItemId);
//			TextField claimedAmtProperty = (TextField) eachItem.getItemProperty("claimedAmt").getValue();
//			if(claimedAmtProperty != null) {
//				Label oldField = (Label) item.getItemProperty("S.No").getValue();
//				String existingIds = (String)claimedAmtProperty.getData();
//				Integer existingBenefitId = Integer.valueOf(existingIds.split("-")[0]);
//				
//				if(existingBenefitId.equals(Integer.valueOf(benefitId))) {
//					if(!duplicateItemId.equals(eachItemId) && !Integer.valueOf(itemId).equals(Integer.valueOf(eachItemId.toString()))) {
//						i = i + 0.1f;
//						oldField.setValue((i).toString());
//					}
//				}
//			}
//		}
//		
		Button value = (Button) addedItem.getItemProperty("add").getValue();
		
		value.setCaption("Delete");
		value.setData(benefitId.toString() + "-" + duplicateItemId.toString());
		value.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -8565348148768739423L;
			@Override
			public void buttonClick(ClickEvent event) {
				String ids = (String)event.getButton().getData();
				Integer benefitId = Integer.valueOf(ids.split("-")[0]);
				String itemId = ids.split("-")[1];
				Item item2 = pmTreeTable.getItem(Integer.valueOf(itemId));
				TextField reasonField = (TextField) item2.getItemProperty("reason").getValue();
				
				TextField claimedAmtField = (TextField) item2.getItemProperty("claimedAmt").getValue();
				String value = claimedAmtField.getValue();
				if(reasonField != null && reasonField.getData() != null) {
					reasonField.setNullRepresentation("");
					deletedIds.add((Long)reasonField.getData());
				}
				
				pmTreeTable.removeItem(Integer.valueOf(itemId));
				
				if(SHAUtils.getIntegerFromString(value) != 0 && benefitId.equals(8)) {
					Collection<?> itemIds = pmTreeTable.getItemIds();
					for (Object eachItemId : itemIds) {
						Item eachItem = pmTreeTable.getItem(eachItemId);
						TextField claimedAmtProperty = (TextField) eachItem.getItemProperty("claimedAmt").getValue();
						setAmtRequestedCalculationValues(claimedAmtProperty);
						break;
					}
				}
			}
		});
	}
	
	public List<String> getErrors(){
		return errorMessages;
	}
	
	public List<Long> getDeletedItems(){
		return deletedIds;
	}
	
	public void setDeletedItems(List<Long> deletedIds){
		this.deletedIds = deletedIds;
	}
	
	public Float getTotalNoOfDays(){
		return totalNoOfDays;
	}
	
	public Float getTotalEntitlementNoOfDays(){
		return totalEntitlementDays;
	}
	
	public Integer getTotalPayableAmt() {
		return SHAUtils.getIntegerFromString(pmTreeTable.getColumnFooter("payableAmt"));
	}
	
	public Integer getTotalClaimedAmt() {
		return SHAUtils.getIntegerFromString(pmTreeTable.getColumnFooter("claimedAmt"));
	}
	
	public Integer getTotalDeductableAmount() {
		return SHAUtils.getIntegerFromString(pmTreeTable.getColumnFooter("nonPayableAmt"));
	}
	
	public Integer getTotalDeductibleAmount() {
		return SHAUtils.getIntegerFromString(pmTreeTable.getColumnFooter("deductible"));
	}
	
	public Integer getTotalProductAmount() {
		return SHAUtils.getIntegerFromString(pmTreeTable.getColumnFooter("productDayAmt"));
	}
	
	public Integer getTotalNetAmount() {
		return SHAUtils.getIntegerFromString(pmTreeTable.getColumnFooter("netAmt"));
	}
	
public void setValuesForSectionChange(List<NoOfDaysCell> listofDTO, Boolean isReProData) {
		
		// Before setting the value we should add multiplerows if exists.
		addMultipleRows(listofDTO);
		
		Collection<?> itemIds = pmTreeTable.getItemIds();
		//List<NoOfDaysCell> listOfDTO = new ArrayList<NoOfDaysCell>();
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId); 
			NoOfDaysCell exactDTO = new NoOfDaysCell();
			//NoOfDaysCell values = new NoOfDaysCell();
			TextField claimedNoOfDaysProperty = (TextField) item.getItemProperty("claimedDays").getValue();
			TextField claimedPerDayAmtProperty = (TextField) item.getItemProperty("claimedDayAmt").getValue();
			TextField claimedAmtProperty =  (TextField) item.getItemProperty("claimedAmt").getValue();
			TextField deductibleProperty = (TextField) item.getItemProperty("deductible").getValue();
			TextField netAmtProperty = (TextField) item.getItemProperty("netAmt").getValue();
			// Product Properties
			TextField productNoOfDaysProperty = (TextField) item.getItemProperty("productDays").getValue();
			TextField productPerDayAmtProperty = (TextField) item.getItemProperty("productDayAmt").getValue();
			TextField productAmountProperty = (TextField) item.getItemProperty("ProductAmount").getValue();
			 // General Properties
			TextField nonPayableProperty = (TextField) item.getItemProperty("nonPayableAmt").getValue();
			TextField consideredPerDayAmt = (TextField) item.getItemProperty("consideredPerDayAmt").getValue();
			TextField payableProperty =(TextField) item.getItemProperty("payableAmt").getValue();
			TextField reasonProperty = (TextField)item.getItemProperty("reason").getValue();
			AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("Details").getValue();
			String ids = (String)detailField.getData();
			Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0]) ? Integer.valueOf(ids.split("-")[0]) : 0;
			//String itemID = ids.split("-")[1];
			if(Arrays.asList(childItemId).contains(benefitId)) {
				exactDTO = getExactDTOForSection(benefitId.longValue(), listofDTO);
				reasonProperty.setData(exactDTO.getKey());
				if(isReProData) {
					if(Arrays.asList(proRataCalculationIds).contains(benefitId)) {
						setValuesToField(claimedAmtProperty, exactDTO.getClaimedBillAmount());
//						setValuesToField(deductibleProperty, exactDTO.getDeductibleAmount());
//						setValuesToField(netAmtProperty, exactDTO.getNetAmount());
//						setValuesToField(productAmountProperty, exactDTO.getPolicyMaxAmount());
//						
//						setValuesToField(nonPayableProperty, exactDTO.getNonPayableAmount());
//						setValuesToField(payableProperty, exactDTO.getPaybleAmount());
//						setValuesToField(consideredPerDayAmt, exactDTO.getConsiderPerDayAmt());
//						
//						reasonProperty.setValue(exactDTO.getNonPayableReason());
					}
				} else {
					if(benefitId == 17) {
						detailField.getCheckboxField().setValue((exactDTO.getOverridePackageDeductionFlag() != null && exactDTO.getOverridePackageDeductionFlag().toLowerCase().equalsIgnoreCase("y"))  ? true : false);
					} else if(benefitId == 18) {
						detailField.getCheckboxField().setValue((exactDTO.getRestrictToFlag() != null && exactDTO.getRestrictToFlag().toLowerCase().equalsIgnoreCase("y")) ? true : false);
					}
					if(Arrays.asList(roomRentICUId).contains(benefitId)) {
						setValuesToField(claimedNoOfDaysProperty, exactDTO.getTotalBillingDays());
						setValuesToField(claimedPerDayAmtProperty, exactDTO.getBillingPerDayAmount());
						setValuesToField(productNoOfDaysProperty, exactDTO.getTotalDaysForPolicy());
						setValuesToField(productPerDayAmtProperty, exactDTO.getPolicyPerDayPayment());
					}
						setValuesToField(claimedAmtProperty, exactDTO.getClaimedBillAmount());
						setValuesToField(deductibleProperty, exactDTO.getDeductibleAmount());
						setValuesToField(netAmtProperty, exactDTO.getNetAmount());
						setValuesToField(productAmountProperty, exactDTO.getPolicyMaxAmount());
						
						setValuesToField(nonPayableProperty, exactDTO.getNonPayableAmount());
						setValuesToField(payableProperty, exactDTO.getPaybleAmount());
						setValuesToField(consideredPerDayAmt, exactDTO.getConsiderPerDayAmt());
						
						reasonProperty.setValue(exactDTO.getNonPayableReason());
				}
			}
			if(claimedAmtProperty != null) {
				Boolean isProrata = true;
				if(((Integer)this.dbCalculationValues.get(0)).equals(1)) {
					isProrata = false;
				}
				
				if(!Arrays.asList(hospDiscountId).contains(Integer.valueOf(benefitId))) {
				if(SHAUtils.getIntegerFromString(claimedAmtProperty.getValue()) < SHAUtils.getIntegerFromString(deductibleProperty.getValue())) {
					VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Deductible Amount should be lesser than Amount Claimed. </b>", ContentMode.HTML));
					layout.setMargin(true);
					layout.setSpacing(true);
					showErrorPopup(deductibleProperty, layout);
					return;
				}}
				
				if(Arrays.asList(roomRentICUId).contains(Integer.valueOf(benefitId))) {
					netAmtProperty.setReadOnly(false);
					netAmtProperty.setValue(ClaimedAmountRules.getClaimedNetAmount(deductibleProperty.getValue(), claimedAmtProperty.getValue()).toString());
					netAmtProperty.setReadOnly(true);
					
					setNoDaysCalculation(productNoOfDaysProperty, productPerDayAmtProperty, productAmountProperty);
					
					consideredPerDayAmt.setReadOnly(false);
					consideredPerDayAmt.setValue(ClaimedAmountRules.perDayMinAmount(productPerDayAmtProperty.getValue(), netAmtProperty.getValue(), claimedNoOfDaysProperty.getValue()).toString());
					consideredPerDayAmt.setReadOnly(true);
					
					payableProperty.setReadOnly(false);
					payableProperty.setValue(ClaimedAmountRules.getPayableAmount(productNoOfDaysProperty.getValue(), consideredPerDayAmt.getValue()).toString());
					if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null &&(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_INVIDUAL) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_CARE_FLOATER))) {
						if(Integer.valueOf(benefitId).equals(9) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > 10000) {
							payableProperty.setValue("10000");
						}
					}
					
					if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null && (preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.HEALTH_ALL_CARE))) {
						if(Integer.valueOf(benefitId).equals(8) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > 10000) {
							payableProperty.setValue("10000");
						}
					}
					
//					if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null &&(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL))) {
//						if(Integer.valueOf(benefitId).equals(9) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > 10000) {
//							payableProperty.setValue("10000");
//						}
//					}
					
					if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null && (preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL))) {
						Double insuredSumInsured = this.preauthDto.getPolicyDto().getInsuredSumInsured();
						if (null == insuredSumInsured || insuredSumInsured == 0) {
							insuredSumInsured = this.preauthDto.getPolicyDto().getTotalSumInsured();
						}
						Integer limitAmt = 1667;
						if(insuredSumInsured >10000d && insuredSumInsured <= 20000d) {
							limitAmt = 3333;
						} else if(insuredSumInsured >20000d && insuredSumInsured <= 30000d) {
							limitAmt = 5000;
						}
						
						if(Integer.valueOf(benefitId).equals(8) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > limitAmt) {
							payableProperty.setValue(limitAmt.toString());
						}
					}
					
					payableProperty.setReadOnly(true);
					
					nonPayableProperty.setReadOnly(false);
					nonPayableProperty.setValue(ClaimedAmountRules.getNotPayableAmount(payableProperty.getValue(), netAmtProperty.getValue() != null ? netAmtProperty.getValue() : claimedAmtProperty.getValue()).toString());
					nonPayableProperty.setReadOnly(true);
					
					if(Integer.valueOf(benefitId).equals(8)) {
						Integer payableSumValue = 0;
						Integer netAmtSumValue = 0;
						for (Object eachItemId : itemIds) {
							Item eachItem = pmTreeTable.getItem(eachItemId);
							TextField claimedAmtPropertyField = (TextField) eachItem.getItemProperty("claimedAmt").getValue();
							TextField payableAmtField = (TextField) eachItem.getItemProperty("payableAmt").getValue();
							TextField netAmtField = (TextField) eachItem.getItemProperty("netAmt").getValue();
							if(claimedAmtPropertyField != null) {
								String existingIds = (String)claimedAmtPropertyField.getData();
								Integer existingBenefitId = SHAUtils.isValidInteger(existingIds.split("-")[0])  ? Integer.valueOf(existingIds.split("-")[0]) : 0;
								if(existingBenefitId.equals(8)) {
									payableSumValue += SHAUtils.getIntegerFromString(payableAmtField.getValue());
									netAmtSumValue += SHAUtils.getIntegerFromString(netAmtField.getValue());
								}
							}
						}
						
						Float roomRentProRataValue = SHAUtils.getIntegerFromString(payableSumValue.toString()).floatValue() / SHAUtils.getIntegerFromString(netAmtSumValue.toString()).floatValue();
						Boolean isRoomRentChanged = false;
						if(roomRentAmountRequested != null && !roomRentAmountRequested.equals(String.valueOf(roomRentProRataValue)) ) {
							isRoomRentChanged = true;
						}	
						
						roomRentAmountRequested = String.valueOf(roomRentProRataValue);
						
						if(isRoomRentChanged) {
							setValues(getValues(), true);
						}
					}
				} else {
					if(Arrays.asList(microProductIds).contains(benefitId) && (preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL))) {
						microProductCalculation(roomRentAmountRequested);
					} else {
						
						Integer amountForProRata = 0;
						if(!Arrays.asList(hospDiscountId).contains(Integer.valueOf(benefitId))){
						
						netAmtProperty.setReadOnly(false);
						netAmtProperty.setValue(ClaimedAmountRules.otherDetailsNetAmount(claimedAmtProperty.getValue(), deductibleProperty.getValue()).toString());
						netAmtProperty.setReadOnly(true);
						
						productAmountProperty.setReadOnly(false);
						productAmountProperty.setValue(netAmtProperty.getValue());
						productAmountProperty.setReadOnly(true);
						
						amountForProRata = SHAUtils.getIntegerFromString(productAmountProperty.getValue()) == 0 ?  SHAUtils.getIntegerFromString(netAmtProperty.getValue()) : Math.min(SHAUtils.getIntegerFromString(netAmtProperty.getValue()), SHAUtils.getIntegerFromString(productAmountProperty.getValue()));
						}
						
						if(Arrays.asList(proRataCalculationIds).contains(benefitId)) {
							Integer prorataCalculation = 0;
							payableProperty.setReadOnly(false);
							if(benefitId.equals(18)) {
								if(detailField.getCheckboxValue()) {
									prorataCalculation =  ClaimedAmountRules.otherPackageWithRestrict(roomRentAmountRequested, amountForProRata.toString(), isProrata);
								} else {
									prorataCalculation =  ClaimedAmountRules.otherPackageWithOutRestrict(roomRentAmountRequested, amountForProRata.toString(), isProrata);
								}
							} else {
								prorataCalculation = ClaimedAmountRules.ProrataCalculation(roomRentAmountRequested, amountForProRata.toString(), isProrata);
							}
							payableProperty.setValue(prorataCalculation.toString());
							payableProperty.setReadOnly(true);
							
						} else if(benefitId.equals(15)) {
							payableProperty.setReadOnly(false);
							payableProperty.setValue(ClaimedAmountRules.ambulancePayableAmount(netAmtProperty.getValue(), String.valueOf(((Double)this.dbCalculationValues.get(benefitId)).intValue())).toString());
							payableProperty.setReadOnly(true);
							
							productAmountProperty.setReadOnly(false);
							productAmountProperty.setValue(String.valueOf(((Double)this.dbCalculationValues.get(benefitId)).intValue()));
							productAmountProperty.setReadOnly(true);
							
						} else if(benefitId.equals(17)) {
							payableProperty.setReadOnly(false);
							if(detailField.getCheckboxValue()) {
								payableProperty.setValue(ClaimedAmountRules.compositePackageWithOverrideDeductionConsiderAmt(netAmtProperty.getValue()).toString());
							} else {
								payableProperty.setValue(ClaimedAmountRules.compositePackageWithoutOverrideDeductionConsiderAmt(netAmtProperty.getValue()).toString());
							}
							payableProperty.setReadOnly(true);
						}
						else {
							if(!Arrays.asList(hospDiscountId).contains(Integer.valueOf(benefitId))){
							payableProperty.setReadOnly(false);
							payableProperty.setValue(amountForProRata.toString());
							payableProperty.setReadOnly(true);
							}
						}
						
						if(((Integer)this.dbCalculationValues.get(0)).equals(2)) {
							if((Arrays.asList(percentageIds).contains(benefitId) && null != ((Double)this.dbCalculationValues.get(benefitId)) &&  (((Double)this.dbCalculationValues.get(benefitId)).intValue()) <= 100)) {
								specificProductCalculation(netAmtProperty.getValue(), benefitId);
							} else if(null != ((Double)this.dbCalculationValues.get(benefitId)) && (((Double)this.dbCalculationValues.get(benefitId)).intValue()) == 25) {
									Double insuredSumInsured = this.preauthDto.getPolicyDto().getInsuredSumInsured();
									Integer SIValue=0;
									if (null != insuredSumInsured) {
										if (insuredSumInsured == 0) {
											insuredSumInsured = this.preauthDto
													.getPolicyDto().getTotalSumInsured();
										}
										Double value = insuredSumInsured * (25d / 100);
										SIValue = Math.round(value.floatValue());
									}
									
									productAmountProperty.setReadOnly(false);
									productAmountProperty.setValue(SIValue.toString());
									productAmountProperty.setReadOnly(true);
									
									Integer proRataValue = SHAUtils.getIntegerFromString(productAmountProperty.getValue()) == 0 ?  SHAUtils.getIntegerFromString(netAmtProperty.getValue()) : Math.min(SHAUtils.getIntegerFromString(netAmtProperty.getValue()), SHAUtils.getIntegerFromString(productAmountProperty.getValue()));
									payableProperty.setReadOnly(false);
									payableProperty.setValue(proRataValue.toString());
									payableProperty.setReadOnly(true);
									
									if(SIValue < SHAUtils.getIntegerFromString(productAmountProperty.getValue())) {
										productAmountProperty.setReadOnly(false);
										productAmountProperty.setValue(SIValue.toString());
										productAmountProperty.setReadOnly(true);
										
										Integer amountForProRata1 = SHAUtils.getIntegerFromString(productAmountProperty.getValue()) == 0 ?  SHAUtils.getIntegerFromString(netAmtProperty.getValue()) : Math.min(SHAUtils.getIntegerFromString(netAmtProperty.getValue()), SHAUtils.getIntegerFromString(productAmountProperty.getValue()));
										payableProperty.setReadOnly(false);
										payableProperty.setValue(amountForProRata1.toString());
										payableProperty.setReadOnly(true);
									} 
							}
						}
						
						if(preauthDto != null && preauthDto.getNewIntimationDTO() != null && preauthDto.getNewIntimationDTO().getPolicy() != null && preauthDto.getNewIntimationDTO().getPolicy().getProduct() != null && (preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_GROUP) || preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.MICRO_INSURANCE_INDIVIDUAL))) {
							Double insuredSumInsured = this.preauthDto.getPolicyDto().getInsuredSumInsured();
							if (null == insuredSumInsured || insuredSumInsured == 0) {
								insuredSumInsured = this.preauthDto.getPolicyDto().getTotalSumInsured();
							}
							Integer limitAmt = 1500;
							if(insuredSumInsured >10000d && insuredSumInsured <= 20000d) {
								limitAmt = 3000;
							} else if(insuredSumInsured >20000d && insuredSumInsured <= 30000d) {
								limitAmt = 4500;
							}
							
							if(Integer.valueOf(benefitId).equals(11) && SHAUtils.getIntegerFromString(payableProperty.getValue()) > limitAmt) {
								payableProperty.setReadOnly(false);
								payableProperty.setValue(limitAmt.toString());
								payableProperty.setReadOnly(true);
							}
						}
						
						if(!Arrays.asList(hospDiscountId).contains(Integer.valueOf(benefitId))){
						nonPayableProperty.setReadOnly(false);
						nonPayableProperty.setValue(ClaimedAmountRules.getNotPayableAmount(payableProperty.getValue(), netAmtProperty.getValue() != null ? netAmtProperty.getValue() : claimedAmtProperty.getValue()).toString());
						nonPayableProperty.setReadOnly(true);
						}
						
					}
					
				}
				
				updateSummation();
			}
		
		}
		
		pmTreeTable.setFooterVisible(true);
	}

	/*private ValueChangeListener getNonPayableReasonListener(final TextField reasonField) {
	ValueChangeListener listener = new ValueChangeListener() {
		private static final long serialVersionUID = -6840859302219285675L;

		@Override
		public void valueChange(Property.ValueChangeEvent event) {
			TextField property = (TextField) event.getProperty();
			if(null != property && null != property.getValue()) {
				reasonField.setDescription(property.getValue());
			} 
			
		}
	};
		return listener;
}*/
	
	
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
					reasonField.setDescription("Click the Text Box and Press F8 For Detailed Popup");
				} 
				
			}
		};
		return listener;
		
	}
	
	
	public void setValuesForHospDiscount(NoOfDaysCell dto) {

		Collection<?> itemIds = pmTreeTable.getItemIds();
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId);
			NoOfDaysCell exactDTO = dto;
			TextField claimedAmtProperty = (TextField) item.getItemProperty(
					"claimedAmt").getValue();
			TextField netAmtProperty = (TextField) item.getItemProperty(
					"netAmt").getValue();
			TextField payableProperty = (TextField) item.getItemProperty(
					"payableAmt").getValue();
			AmountRequestedDetailField detailField = (AmountRequestedDetailField) item
					.getItemProperty("Details").getValue();
			String ids = (String) detailField.getData();
			Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0]) ? Integer
					.valueOf(ids.split("-")[0]) : 0;
			if (Arrays.asList(hospDiscountId).contains(benefitId)) {
				setValuesToHospDiscntField(claimedAmtProperty,
						exactDTO.getClaimedBillAmount());
				setValuesToHospDiscntField(netAmtProperty, exactDTO.getNetAmount());
				setValuesToHospDiscntField(payableProperty, exactDTO.getPaybleAmount());
				
				updateSummation();
			}
			
		}

		pmTreeTable.setFooterVisible(true);
	}
	
	private void setValuesToHospDiscntField(TextField field, Object value) {
		Boolean isReadOnly = false;
		if(field != null){
			if(field.isReadOnly()) {
				isReadOnly = true;
			}
			field.setReadOnly(false);
			if(value != null) {
				if(value instanceof Integer) {
						field.setValue(value.toString());
				} else if(value instanceof Float) {
						field.setValue(value.toString());
				}
			}
			setAmtRequestedCalculationValues(field);
			field.setReadOnly(isReadOnly);
		}
		
	}
	
	private ValueChangeListener getClaimedAmntListener(final Boolean invokeAgain) {
		
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 7342288705079773186L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				
				TextField claimedAmtProperty = (TextField) event.getProperty();	
				
				String ids = (String)claimedAmtProperty.getData();
				Integer benefitId = Integer.valueOf(ids.split("-")[0]);
				
				Item item = pmTreeTable.getItem(12);
				TextField deductibleProperty = (TextField) item.getItemProperty("deductible").getValue();
				
				if(!SHAUtils.isValidFloat(claimedAmtProperty.getValue())) {
					claimedAmtProperty.setValue("0");
					Notification notify = new Notification("Message");
					notify.setDelayMsec(-1);
				} else {
					if(preauthDto.getNewIntimationDTO().getIsPaayasPolicy()){
						if(null != preauthDto.getNewIntimationDTO().getHospitalDto().getDiscountPercentage() &&
								preauthDto.getNewIntimationDTO().getHospitalDto().getDiscountPercentage()>0  && !preauthDto.getIsDefaultDeductable())
						{
							if(Integer.valueOf(benefitId).equals(16) && null != claimedAmtProperty.getValue() && !claimedAmtProperty.getValue().equalsIgnoreCase("") && !claimedAmtProperty.getValue().isEmpty()){
								
								Double deductableAmount = Double.valueOf(claimedAmtProperty.getValue())*(preauthDto.getNewIntimationDTO().getHospitalDto().getDiscountPercentage()/100);
								deductibleProperty.setValue(String.valueOf(Integer.valueOf(deductableAmount.intValue())));
								preauthDto.setIsDefaultDeductable(Boolean.TRUE);
							}
						}
					}
				}
			}
		};
		return listener;
	}
	
	// View PreauthDetails Multilple Data Issue
	public void resetSerialNo(){
		i = 0;
	}
}