package com.shaic.claim.viewEarlierRodDetails.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPreHospitalizationTable extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<PreHospitalizationDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PreHospitalizationDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<PreHospitalizationDTO> container = new BeanItemContainer<PreHospitalizationDTO>(PreHospitalizationDTO.class);

	private Table table;
	
	private Map<String, Object> referenceData;
	
	private Long productKey;
	
	private String presenterString;
	
	private VerticalLayout vLayout ;
	
	private Boolean isView;
	
	public void initPresenter(Long productKey, Boolean isView)
	{
		this.productKey = productKey;
		this.isView = isView;
	}
	
	public void initPresenterString(String presenterString)
	{
		this.presenterString = presenterString;
	}
	
	
	public void init(){
		container.removeAllItems();
		vLayout = new VerticalLayout();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setFooterVisible(true);
		layout.addComponent(table);

		setCompositionRoot(layout);
		
		
		
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "sno","details","claimedAmt", "billingNonPayable", "netAmount", "amount", "deductingNonPayable", "payableAmount", "reason" });
		//if(null != productKey && productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET))
		//if(null != productKey && (productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET) || productKey.equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_PRODUCT)) && null != this.presenterString  && (SHAConstants.BILLING.equalsIgnoreCase(this.presenterString) || (SHAConstants.FINANCIAL).equalsIgnoreCase(this.presenterString)))
		if(null != this.presenterString  && (SHAConstants.BILLING.equalsIgnoreCase(this.presenterString) || (SHAConstants.FINANCIAL).equalsIgnoreCase(this.presenterString)))
		{
			table.setVisibleColumns(new Object[] { "sno","details","claimedAmt", "billingNonPayable", "reasonableDeduction" ,"netAmount", "reason" , "deductibleNonPayableReasonBilling" , "deductibleNonPayableReasonFA"});
		}
		else
		{
			table.setVisibleColumns(new Object[] { "sno","details","claimedAmt", "billingNonPayable", "reasonableDeduction" ,"netAmount", "reason" });
		}
		if(null != isView && isView)
		{
			table.setVisibleColumns(new Object[] { "sno","details","claimedAmt", "billingNonPayable", "reasonableDeduction" ,"netAmount", "reason" , "deductibleNonPayableReasonBilling" , "deductibleNonPayableReasonFA"});
		}
        
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("details", "Details");
		table.setColumnHeader("claimedAmt", "Claimed Amount(A)");
		//table.setColumnHeader("billingNonPayable", "Non Payable(Entered in Billing)(B)");
		table.setColumnHeader("billingNonPayable", "Non Payable(B)");
		table.setColumnHeader("reasonableDeduction", "Reasonable Deduction (C)");
		table.setColumnHeader("netAmount", "Net Amount(D)");
	/*	table.setColumnHeader("amount", "Amount(D)");
		table.setColumnHeader("deductingNonPayable", "Non Payables (Inc Deductibles)(E)");
		table.setColumnHeader("payableAmount", "Payable Amt(F)");*/
		table.setColumnHeader("reason", "Deductible / Non Payables Reason");
		table.setColumnWidth("sno", 45);
		table.setColumnHeader("deductibleNonPayableReasonBilling","Deductible / </br> Non </br>Payables </br> Reason - Billing");
		table.setColumnHeader("deductibleNonPayableReasonFA","Deductible / </br> Non </br>Payables </br> Reason - FA");
		table.setEditable(true);
		
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		calculateTotal();

	}
	
	
	
	public void setTableList(final List<PreHospitalizationDTO> list) {
		table.removeAllItems();
		for (final PreHospitalizationDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {

	
		private static final long serialVersionUID = 1L;
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext){
			PreHospitalizationDTO preHospitalizationDTO = (PreHospitalizationDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(preHospitalizationDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(preHospitalizationDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(preHospitalizationDTO);
			}
			
			if("claimedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("claimedAmt", field);
				return field;
			} else if ("billingNonPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(preHospitalizationDTO);
				tableRow.put("billingNonPayable", field);
				return field;
			}else if ("reasonableDeduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(preHospitalizationDTO);
				tableRow.put("reasonableDeduction", field);
				return field;
			}

			
			
			else if ("netAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("netAmount", field);
				calculateTotal();
				return field;
			}
			else if("reason".equals(propertyId)){
				
				TextField field = new TextField();
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("450px");
				field.setNullRepresentation("");
				field.setDescription(preHospitalizationDTO.getReason());
				return field;
				
			}
			
			else if("deductibleNonPayableReasonBilling".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setMaxLength(4000);
				field.setMaxLength(300);
				field.setData(preHospitalizationDTO);
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
						//field.setReadOnly(true);
					} 
				
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("deductibleNonPayableReasonBilling", field);
				addDescriptionFromRemarksFld(field);
				handleEnterForBillingReason(field,null);
				return field;
			}
			else if("deductibleNonPayableReasonFA".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setMaxLength(4000);
				field.setMaxLength(300);
				field.setData(preHospitalizationDTO);
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
						//field.setReadOnly(true);
					} 
				
				/*field.setEnabled(true);
				field.setReadOnly(true);*/
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("deductibleNonPayableReasonFA", field);
				addDescriptionFromRemarksFld(field);
				handleEnterForFAReason(field,null);
				return field;
			}
			
			
			/*else if("deductibleNonPayableReasonFA".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setMaxLength(4000);
				field.setData(preHospitalizationDTO);
				//field.setEnabled(false);
				//field.setEnabled(false);
				field.setEnabled(true);
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z 0-9 @ .]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("deductibleNonPayableReasonFA", field);
				addDescriptionFromRemarksFld(field);
				return field;
			}*/
			/*else if("amount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("amount", field);
				return field;
			} else if("deductingNonPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("deductingNonPayable", field);
				return field;
			}
			else if("payableAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(preHospitalizationDTO);
				tableRow.put("payableAmount", field);
				calculateTotal();
				return field;
			}*/
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					((TextField) field).setNullRepresentation("");
					field.setReadOnly(true);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setWidth("450px");
				return field;
			}
			
			
		}
		
	}
	
	 public void addBeanToList(PreHospitalizationDTO preHospitalizationDTO) {
	    	container.addBean(preHospitalizationDTO);

//	    	data.addItem(pedValidationDTO);
	    }
	 
	 @SuppressWarnings("unchecked")
	private void calculateTotal(){
				
				List<PreHospitalizationDTO> itemIconPropertyId = (List<PreHospitalizationDTO>) table.getItemIds();
				//Long netAmount =0l;
				Long claimedAmount = 0l;
				Long nonPayableAmount = 0l;
				Long reasonableDeduction = 0l;
				Long netAmount = 0l;
				/*Long amount =0l;
				Long nonPayableAmount =0l;
				Long payableAmount =0l;*/
				for (PreHospitalizationDTO preHospitalizationDTO : itemIconPropertyId) {
					if(preHospitalizationDTO.getNetAmount() != null){
						netAmount += Math.round(preHospitalizationDTO.getNetAmount());
						}
						if(null != preHospitalizationDTO.getClaimedAmt())
						{
							claimedAmount += Math.round(preHospitalizationDTO.getClaimedAmt());
							
						}
						
						if(null != preHospitalizationDTO.getBillingNonPayable())
						{
							nonPayableAmount += Math.round(preHospitalizationDTO.getBillingNonPayable());
							//nonPayableAmount += Long.parseLong(String.valueOf(preHospitalizationDTO.getBillingNonPayable()));
						}
						
						if(null != preHospitalizationDTO.getReasonableDeduction())
						{
							reasonableDeduction += Math.round(preHospitalizationDTO.getReasonableDeduction());
							//reasonableDeduction += Long.parseLong(String.valueOf(preHospitalizationDTO.getReasonableDeduction()));
						}
					
					/*if(preHospitalizationDTO.getAmount() != null){
					amount += preHospitalizationDTO.getAmount();
					}
					if(preHospitalizationDTO.getDeductingNonPayable() != null){
					nonPayableAmount += preHospitalizationDTO.getDeductingNonPayable();
					}
					if(preHospitalizationDTO.getPayableAmount() != null){
					payableAmount += preHospitalizationDTO.getPayableAmount();
					}*/
				}

				table.setColumnFooter("netAmount", String.valueOf(netAmount));
				table.setColumnFooter("claimedAmt", String.valueOf(claimedAmount));
				table.setColumnFooter("billingNonPayable", String.valueOf(nonPayableAmount));
				table.setColumnFooter("reasonableDeduction", String.valueOf(reasonableDeduction));
				/*table.setColumnFooter("amount", String.valueOf(amount));
				table.setColumnFooter("deductingNonPayable", String.valueOf(nonPayableAmount));
				table.setColumnFooter("payableAmount", String.valueOf(payableAmount));*/
				table.setColumnFooter("details", "Total Amount");
				
	 }
	 
	 public Map<String, Long> getPreHospFooterValues(){
			try{
			Map<String,Long> map = new HashMap<String, Long>();
			
			map.put(SHAConstants.NONPAYABLE, Long.valueOf(table.getColumnFooter("billingNonPayable").toString()));
			map.put(SHAConstants.REASONABLE_DEDUCTION, Long.valueOf(table.getColumnFooter("reasonableDeduction").toString()));
			map.put(SHAConstants.NET_AMOUNT, Long.valueOf(table.getColumnFooter("netAmount").toString()));
			map.put(SHAConstants.TOTAL_CLAIMED_AMOUNT, Long.valueOf(table.getColumnFooter("claimedAmt").toString()));
			return map;
			}catch(Exception e){	
				return null;
			}
			
		}
	 
	 public String getPayableAmount(){
		 return this.table.getColumnFooter("netAmount");
	 }

//	 public String getClaimedAmount(){
//		 return this.table.getColumnFooter("claimedAmt");
//	 }

	 private void addDescriptionFromRemarksFld(TextField txtFld)
		{
			
		 PreHospitalizationDTO preHospitalizationDTO = (PreHospitalizationDTO) txtFld.getData();
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(preHospitalizationDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField dedcutibleOrNonPayableReasonFA = (TextField)hashMap.get("deductibleNonPayableReasonFA");
				 if(null != dedcutibleOrNonPayableReasonFA)
				 {
					 
					 dedcutibleOrNonPayableReasonFA.setDescription(preHospitalizationDTO.getDeductibleNonPayableReasonFA());
					/* itemNoFld.setReadOnly(false);
					 itemNoFld.setValue(String.valueOf(i));
					 itemNoFld.setReadOnly(true);*/
					 //itemNoFld.setEnabled(false);
				 }
			 }
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
		
		private ShortcutListener getShortCutListener(final TextField txtFld)
		{
			ShortcutListener listener =  new ShortcutListener("EnterShortcut",KeyCodes.KEY_F8,null) {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void handleAction(Object sender, Object target) {
					BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) txtFld.getData();
					 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
					 TextField txtItemValue = (TextField) hashMap.get("deductibleNonPayableReasonBilling");
					
					 //txtFld.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
					 
					if (null != vLayout
							&& vLayout.getComponentCount() > 0) {
						vLayout.removeAllComponents();
					}
					
					TextArea txtArea = new TextArea();
					txtArea.setNullRepresentation("");
					txtArea.setMaxLength(100);
					
					txtArea.setValue(billEntryDetailsDTO.getDeductibleNonPayableReasonBilling());
					//txtArea.setValue(txtFld.getValue());
					txtArea.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							TextArea txt = (TextArea)event.getProperty();
							txtFld.setValue(txt.getValue());
							txtFld.setDescription(txt.getValue());
							// TODO Auto-generated method stub
							
						}
					});
					
					billEntryDetailsDTO.setDeductibleNonPayableReasonBilling(txtArea.getValue());
				//	txtFld.setDescription(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
					txtFld.setDescription(txtArea.getValue());
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
					
					
					
					final Window dialog = new Window();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(vLayout);
					dialog.setResizable(false);
					dialog.setModal(true);
					//dialog.show(getUI().getCurrent(), null, true);
					
					//if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((null != fileName && fileName.endsWith(".PDF")) || (null != fileName && fileName.endsWith(".pdf")))) {
					if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
						dialog.setPositionX(450);
						dialog.setPositionY(500);
					}
					getUI().getCurrent().addWindow(dialog);
					
					okBtn.addClickListener(new ClickListener() {
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
		
		private ShortcutListener getShortCutListenerForFAReason(final TextField txtFld)
		{
			ShortcutListener listener =  new ShortcutListener("EnterShortcutForMedical",KeyCodes.KEY_F7,null) {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void handleAction(Object sender, Object target) {
					BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) txtFld.getData();
					 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
					 TextField txtItemValue = (TextField) hashMap.get("deductibleNonPayableReasonFA");
					
					 //txtFld.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
					 
					if (null != vLayout
							&& vLayout.getComponentCount() > 0) {
						vLayout.removeAllComponents();
					}
					
					TextArea txtArea = new TextArea();
					txtArea.setNullRepresentation("");
					txtArea.setMaxLength(100);
					
					txtArea.setValue(billEntryDetailsDTO.getDeductibleNonPayableReasonFA());
					//txtArea.setValue(txtFld.getValue());
					txtArea.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							TextArea txt = (TextArea)event.getProperty();
							txtFld.setValue(txt.getValue());
							txtFld.setDescription(txt.getValue());
							// TODO Auto-generated method stub
							
						}
					});
					
					billEntryDetailsDTO.setDeductibleNonPayableReasonFA(txtArea.getValue());
				//	txtFld.setDescription(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
					txtFld.setDescription(txtArea.getValue());
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
					
					
					
					final Window dialog = new Window();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(vLayout);
					dialog.setResizable(false);
					dialog.setModal(true);
					//dialog.show(getUI().getCurrent(), null, true);
					
					if(getUI().getCurrent().getPage().getWebBrowser().isIE() ) {
						dialog.setPositionX(450);
						dialog.setPositionY(500);
					}
					getUI().getCurrent().addWindow(dialog);
					
					okBtn.addClickListener(new ClickListener() {
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
		

		 public List<PreHospitalizationDTO> getValues() {
		    	@SuppressWarnings("unchecked")
				List<PreHospitalizationDTO> itemIds = (List<PreHospitalizationDTO>) this.table.getItemIds() ;
		    	return itemIds;
		    }


	 public String getClaimedAmount(){
		 
		 String claimedAmt = this.table.getColumnFooter("claimedAmt");
		 return claimedAmt;
		 
	 }
	 
	 public String getNonPayableAmount(){
		 return this.table.getColumnFooter("billingNonPayable");
	 }
	
	 public void clearObjects(){
		 if(vLayout!=null){
			 vLayout.removeAllComponents();
		 }
//		 	SHAUtils.setClearTableItem(tableItem);
			SHAUtils.setClearReferenceData(referenceData);
			this.container = null;
			this.tableItem = null;
			this.productKey = null;
			if(table!=null){
				table.removeAllItems();
			}
	 }
}
