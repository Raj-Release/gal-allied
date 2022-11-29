package com.shaic.claim.leagalbilling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import ch.meemin.pmtable.PMTreeTable;
import ch.meemin.pmtable.PMTable.Align;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.AmountRequestedDetailField;
import com.shaic.arch.fields.AmountRequestedField;
import com.shaic.arch.utils.Props;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.ClaimDetailsTableBean;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.PMTableRow;
import com.shaic.domain.ReferenceTable;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.textfield.TextFieldState;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/** Based On Benefit ID
 * 8 Award Amount 
 * 9 Cost
 * 10 Compensation
 * 12 Interest Current Claim
 * 13 Interest Other Claim
 * 14 Interest Payable
 * 15 TDS
 * 16 Interest payable - TDS
 * 
 * 
 */
@Theme(Props.THEME_NAME)
public class LegalSettlementAmountTable extends ViewComponent{

	private static final long serialVersionUID = -1652164044224142612L;

	protected PMTreeTable pmTreeTable;

	Integer[] labelId = {8, 9 , 10, 12, 13, 14, 15, 16};

	Integer[] SummationId = {8, 9 , 10, 12};

	Integer[] intrestApplicableId = {12, 13, 14, 15, 16};

	private int i = 1;

	private Boolean isIntrestApplicable;
	
	private Boolean isView;

	public void initView(boolean isIntrestApplicable,boolean isView) {

		this.isIntrestApplicable = isIntrestApplicable;
		this.isView = isView;
		pmTreeTable = new PMTreeTable("Amount Claimed Details");
		pmTreeTable.setStyleName("wordwrap-headers");

		pmTreeTable.addContainerProperty("S.No", Label.class, null );
		pmTreeTable.addContainerProperty("itemName", AmountRequestedDetailField.class, null );
		if(isIntrestApplicable){
			pmTreeTable.addContainerProperty("tds", TextField.class, null);
		}
		pmTreeTable.addContainerProperty("amount", TextField.class, null);
		if(!isView){
			pmTreeTable.addContainerProperty("remarks", TextField.class, null);
		}

		pmTreeTable.setStyleName(ValoTheme.TABLE_COMPACT);
		pmTreeTable.setWidth("80%");

		if(isView){
			pmTreeTable.setColumnWidth("itemName", 200);
		}else{
			pmTreeTable.setColumnWidth("itemName", 305);
		}
		pmTreeTable.setColumnWidth("S.No", 35);

		/**
		 * The below commented line does the job of configuring
		 * the column header names.  As of now this is commented.
		 * While implementing this change , shall incorporate the same.
		 * */
		pmTreeTable.setColumnHeader("itemName", "Component Name");				
		pmTreeTable.setColumnHeader("amount", "Amount");
		if(!isView){
			pmTreeTable.setColumnHeader("remarks", "Remarks");
		}		
		if(isIntrestApplicable){
			pmTreeTable.setColumnHeader("tds", "TDS</br> %");
		}

		// Detail column is used as Tree..
		pmTreeTable.setHierarchyColumn("itemName");

		pmTreeTable.setColumnAlignment("S.No", Align.CENTER);
		pmTreeTable.setColumnAlignment("itemName", Align.LEFT);
		pmTreeTable.setColumnAlignment("amount", Align.CENTER);
		if(!isView){
			pmTreeTable.setColumnAlignment("remarks", Align.CENTER);
		}
		if(isIntrestApplicable){
			pmTreeTable.setColumnAlignment("tds", Align.CENTER);
			setupTable(LegalSettlementTabelBean.getPMTableintrestApplicableRows());
		}else{
			setupTable(LegalSettlementTabelBean.getPMTableRows());
		}

		pmTreeTable.removeItem(1);

		pmTreeTable.setFooterVisible(true);
		pmTreeTable.setColumnFooter("itemName", "Total Legal Settlement Amount");
		if(!isView){
			setCompositionRoot(pmTreeTable);	
		}
						
	}

	@PostConstruct
	public void init()
	{

	}

	private void setupTable(PMTableRow root)
	{
		addTreeMenuItem(root, null);

	}

	private TextField createTextField(Integer masterId, Object itemId, String width, Boolean isEnabled, Boolean isReadOnly, Integer maxLength,Boolean isamountfield) {
		TextField createdField = new TextField();
		createdField.setEnabled(isEnabled);
		createdField.setWidth(width);
		if(maxLength != 0) {
			createdField.setMaxLength(maxLength);
		}
		createdField.setData(masterId.toString() + "-" + itemId.toString());
		createdField.setNullRepresentation("");

		if(isamountfield) {
			createdField.setReadOnly(true);
			createdField.setStyleName("v-textfield-borderless-rightalign");
		}
		if(isReadOnly) {
			createdField.setReadOnly(true);
			createdField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		}

		CSValidator validator = new CSValidator();
		validator.extend(createdField);
		validator.setRegExp("^[0-9]*$"); // Should allow only 0 to 9 and '.'
		validator.setPreventInvalidTyping(true);

		return createdField;
	}

	/**
	 * This method will be called recursively. This method creates
	 * each column in the table
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private void addTreeMenuItem(PMTableRow menu, Object parent) {
		Integer masterId = menu.getMasterId();
		//item id will be the number. refer LegalSettlementTabelBean file.

		Object itemId = pmTreeTable.addItem();
		Item parentItem = pmTreeTable.getItem(itemId);

		AmountRequestedDetailField	amountRequestedDetailField = new AmountRequestedDetailField(true, menu.getDetailLabel(), null);
		amountRequestedDetailField.setData(masterId.toString() + "-" + itemId.toString());
		parentItem.getItemProperty("itemName").setValue(amountRequestedDetailField);
		if(Arrays.asList(labelId).contains(masterId)) {
			parentItem.getItemProperty("S.No").setValue(new Label((String.valueOf(i++))));
		}

		Property billingAmountProperty = parentItem.getItemProperty("amount");
		billingAmountProperty.setValue(createTextField(masterId, itemId, "60px" , false, false, 0,true));
		if(!isView){
			Property billingRemarkProperty = parentItem.getItemProperty("remarks");
			TextField createdField = new TextField();
			createdField.setWidth("143px");
			createdField.setMaxLength(100);
			createdField.setData(null);
			createdField.setNullRepresentation("");		
			billingRemarkProperty.setValue(createdField);

			TextField reasonField = ((TextField)billingRemarkProperty.getValue());
			reasonField.setNullRepresentation("");
			reasonField.setDescription("Click the Text Box and Press F8 For Detailed Popup");
			((TextField)billingRemarkProperty.getValue()).addBlurListener(getRemarkListener(reasonField));
			getRemarkShortcutListener(((TextField)billingRemarkProperty.getValue()), null);
		}		
		if(isIntrestApplicable){
			Property tdsProperty = parentItem.getItemProperty("tds");
			tdsProperty.setValue(createTextField(masterId, itemId, "50px", false, true, 0,false));
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
			pmTreeTable.setChildrenAllowed(itemId, false);			
		}
	}

	public BlurListener getRemarkListener(final TextField reasonField) {

		BlurListener listener = new BlurListener() {

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

	private void getRemarkShortcutListener(TextField remarks, final  Listener listener) {
		ShortcutListener enterShortCut = new ShortcutListener("ShortcutForPreauthMedRemarks", ShortcutAction.KeyCode.F8, null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRemark(remarks, getShortCutListenerForRemarks(remarks));
	}

	private  void handleShortcutForRemark(final TextField textField, final ShortcutListener shortcutListener) {
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

	private ShortcutListener getShortCutListenerForRemarks(final TextField txtFld) {
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
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);

				String strCaption = "Legal Billing Remarks";
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
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};
		return listener;
	}

	private void updateSummation()
	{
		Integer amount = 0;
		Integer tdsamount = 0;
		Collection<?> itemIds = pmTreeTable.getItemIds();
		for(Object itemId : itemIds)
		{
			Item rowItem = this.pmTreeTable.getItem(itemId);
			AmountRequestedDetailField detailField = (AmountRequestedDetailField) rowItem.getItemProperty("itemName").getValue();
			String ids = (String) detailField.getData();
			Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0]) ? Integer.valueOf(ids.split("-")[0]) : 0;
			if(Arrays.asList(SummationId).contains(benefitId)) {
				if (rowItem != null)
				{
					amount += getValueForProperty(rowItem, "amount");
				}
			}else if(benefitId.equals(15)){
				tdsamount = getValueForProperty(rowItem, "amount");
			}

		}
		amount -= tdsamount;
		pmTreeTable.setColumnFooter("amount", String.valueOf(amount));
	}

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

	public void setValuesForFields(LegalBaseCell dto,Integer setbenefitId) {

		Collection<?> itemIds = pmTreeTable.getItemIds();
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId);
			TextField tdsAmtProperty = null;
			if(isIntrestApplicable){
				tdsAmtProperty = (TextField) item.getItemProperty("tds").getValue();
			}
			TextField amtProperty = (TextField) item.getItemProperty("amount").getValue();
			TextField remarksProperty = (TextField) item.getItemProperty("remarks").getValue();

			AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("itemName").getValue();
			String ids = (String) detailField.getData();
			Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0]) ? Integer.valueOf(ids.split("-")[0]) : 0;
			if (benefitId.equals(15)
					&& setbenefitId.equals(benefitId)) {
				setValuesToField(amtProperty,dto.getLegalBillingAmount());
				setValuesToField(tdsAmtProperty, dto.getTds());	
				if(!dto.getIsRemarkEditable()){
					remarksProperty.setEnabled(false);
					remarksProperty.setValue(null);
				}else{
					remarksProperty.setEnabled(true);
				}
				updateSummation();
			}else if(setbenefitId.equals(benefitId)){
				setValuesToField(amtProperty,dto.getLegalBillingAmount());
				if(!dto.getIsRemarkEditable()){
					remarksProperty.setEnabled(false);
					remarksProperty.setValue(null);
				}else{
					remarksProperty.setEnabled(true);
				}
				updateSummation();
			}

		}
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
					field.setValue(value.toString());
				} else if(value instanceof Float) {
					field.setValue(value.toString());
				}else if(value instanceof String) {
					field.setValue(value.toString());
				}else if(value instanceof Long) {
					field.setValue(value.toString());
				}
			}else {
				field.setValue(null);
			}
			field.setReadOnly(isReadOnly);
		}

	}

	public Integer getTotalAppAmt() {
		return SHAUtils.getIntegerFromString(pmTreeTable.getColumnFooter("amount"));
	}

	public LegalBillingDTO getValues(LegalBillingDTO values) {
		Collection<?> itemIds = pmTreeTable.getItemIds();
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId); 
			TextField tdsAmtProperty = null;
			if(isIntrestApplicable){
				tdsAmtProperty = (TextField) item.getItemProperty("tds").getValue();
			}
			TextField amountProperty = (TextField) item.getItemProperty("amount").getValue();
			TextField remarksProperty = (TextField) item.getItemProperty("remarks").getValue();
			AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("itemName").getValue();
			String ids = (String) detailField.getData();
			Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0]) ? Integer.valueOf(ids.split("-")[0]) : 0;
			if(benefitId.equals(8)){
				values.setAwardAmountRemark(remarksProperty.getValue());
			}else if(benefitId.equals(9)){
				values.setCostRemark(remarksProperty.getValue());
			}else if(benefitId.equals(10)){
				values.setCompensationRemark(remarksProperty.getValue());
			}else if(benefitId.equals(12)){
				values.setIntrCurrentClaimRemark(remarksProperty.getValue());
			} 
			if(values.getInterestApplicable() !=null 
					&& values.getInterestApplicable()){	
				if(benefitId.equals(13)){
					values.setIntrOtherClaimRemark(remarksProperty.getValue());
				}else if(benefitId.equals(14)){
					values.setIntrPayRemark(remarksProperty.getValue());
				}else if(benefitId.equals(15)){
					if(tdsAmtProperty.getValue() !=null 
							&& !tdsAmtProperty.getValue().isEmpty()){
						values.setTdsPercentge(Double.parseDouble(tdsAmtProperty.getValue().replace("%","")));
					}	
					if(amountProperty.getValue() !=null 
							&& !amountProperty.getValue().isEmpty()){
						values.setTdsAmount(Long.parseLong(amountProperty.getValue()));
					}
					values.setTdsRemark(remarksProperty.getValue());
				}else if(benefitId.equals(16)){
					values.setIntrPayTDSRemark(remarksProperty.getValue());
				}	
			}				
		}
		values.setTotalApprovedAmount(Long.parseLong(pmTreeTable.getColumnFooter("amount")));
		return values;
	}

	public void setValues(LegalBillingDTO values) {

		Collection<?> itemIds = pmTreeTable.getItemIds();
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId); 
			TextField amountProperty = (TextField) item.getItemProperty("amount").getValue();
			TextField remarksProperty = (TextField) item.getItemProperty("remarks").getValue();
			AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("itemName").getValue();
			String ids = (String) detailField.getData();
			Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0]) ? Integer.valueOf(ids.split("-")[0]) : 0;
			if(benefitId.equals(8)){
				setValuesToField(amountProperty,values.getAwardAmount());
				setValuesToField(remarksProperty, values.getAwardAmountRemark());	
			}else if(benefitId.equals(9)){
				setValuesToField(amountProperty,values.getCost());
				setValuesToField(remarksProperty, values.getCostRemark());
			}else if(benefitId.equals(10)){
				setValuesToField(amountProperty,values.getCompensation());
				setValuesToField(remarksProperty, values.getCompensationRemark());
			}else if(benefitId.equals(12)){
				setValuesToField(amountProperty,values.getInterestCurrentClaim());
				if(values.getInterestApplicable()){
					setValuesToField(remarksProperty, values.getIntrCurrentClaimRemark());
				}else{
					remarksProperty.setEnabled(false);
				}
			}else if(benefitId.equals(13)){
				setValuesToField(amountProperty,values.getInterestOtherClaim());
				if(values.getInterestApplicable()){
					setValuesToField(remarksProperty, values.getIntrOtherClaimRemark());
				}else{
					remarksProperty.setEnabled(false);
				}
			}else if(benefitId.equals(14)){
				if(values.getInterestApplicable()){
					setValuesToField(remarksProperty, values.getIntrPayRemark());
				}else{
					remarksProperty.setEnabled(false);
				}
			}else if(benefitId.equals(15)){
				if(values.getInterestApplicable()){
					setValuesToField(remarksProperty,values.getTdsRemark() );
				}else{
					remarksProperty.setEnabled(false);
				}
			}else if(benefitId.equals(16)){
				if(values.getInterestApplicable()){
					setValuesToField(remarksProperty, values.getIntrPayTDSRemark());
				}else{
					remarksProperty.setEnabled(false);
				}
			}			
		}
		updateSummation();
	}
	
	public void setTabelViewValues(LegalBillingDTO values) {

		Collection<?> itemIds = pmTreeTable.getItemIds();
		for (Object itemId : itemIds) {
			Item item = pmTreeTable.getItem(itemId); 
			TextField tdsAmtProperty = null;
			if(isIntrestApplicable){
				tdsAmtProperty = (TextField) item.getItemProperty("tds").getValue();
			}
			TextField amountProperty = (TextField) item.getItemProperty("amount").getValue();
			AmountRequestedDetailField detailField = (AmountRequestedDetailField) item.getItemProperty("itemName").getValue();
			String ids = (String) detailField.getData();
			Integer benefitId = SHAUtils.isValidInteger(ids.split("-")[0]) ? Integer.valueOf(ids.split("-")[0]) : 0;
			if(benefitId.equals(8)){
				setValuesToField(amountProperty,values.getAwardAmount());
			}else if(benefitId.equals(9)){
				setValuesToField(amountProperty,values.getCost());
			}else if(benefitId.equals(10)){
				setValuesToField(amountProperty,values.getCompensation());
			}else if(benefitId.equals(12)){
				setValuesToField(amountProperty,values.getInterestCurrentClaim());
			}else if(benefitId.equals(13)){
				setValuesToField(amountProperty,values.getInterestOtherClaim());
			}else if(benefitId.equals(14)){
				setValuesToField(amountProperty,values.getTotalInterestAmount());
			}else if(benefitId.equals(15)){
				setValuesToField(tdsAmtProperty, values.getTdsPercentge());
				setValuesToField(amountProperty,values.getTdsAmount());
			}else if(benefitId.equals(16)){
				Long intrTds = values.getTotalInterestAmount() - values.getTdsAmount();
				setValuesToField(amountProperty,intrTds);
			}			
		}
		updateSummation();
	}
	
	public PMTreeTable getTable(){
		pmTreeTable.setWidth("100%");
		pmTreeTable.setSizeFull();
		return this.pmTreeTable;
	}
}
