package com.shaic.claim.reimbursement.billing.wizard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ReimbursementOtherBenifitsTable extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<OtherBenefitsTableDto, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OtherBenefitsTableDto, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<OtherBenefitsTableDto> data;
	
	private Table table;
	
	PreauthDTO bean;
	
	private List<String> errorMessages;
	
	public Object[] VISIBLE_COLUMNS = new Object[] {"benefitName", "noOfDays", "amtClaimed", "nonPayable", "netPayable","eligibleAmt", "approvedAmt","amtAlreadyPaidToHospital","amtPaybableToInsured","amtAlreadyPaid","balancePayable", "remarks" };
	
	public TextField dummyField;
	
	private HorizontalLayout hLayout;

	private VerticalLayout layout;
	
	//BeanItemContainer<SelectValue> applicableContainer;
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.errorMessages = new ArrayList<String>();
		
		layout = new VerticalLayout();
		
		data = new BeanItemContainer<OtherBenefitsTableDto>(OtherBenefitsTableDto.class);
		
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("320px");
		table.setPageLength(7);
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	private Object getSelectedRowId( ArrayList<Object> ids, Long key,  Boolean isDiagnosis){
		
		for(Object id:ids){
			DiagnosisProcedureTableDTO diagnosisDetailsDTO = (DiagnosisProcedureTableDTO)id;
			Long key1 = 0l;
			if(diagnosisDetailsDTO.getDiagnosisDetailsDTO() != null) {
				key1 = diagnosisDetailsDTO.getDiagnosisDetailsDTO().getKey();
			} else if(diagnosisDetailsDTO.getProcedureDTO() != null) {
				key1 = diagnosisDetailsDTO.getProcedureDTO().getKey();
			}
			if(key1 != null && key != null && key1.equals(key)) {
				return id;
			}
		}
		
		return null;
		
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(7);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		
		table.setColumnHeader("benefitName", "Benefits");
		table.setColumnHeader("noOfDays", "No.Of Days stay");
		table.setColumnHeader("amtClaimed", "Amount Claimed");
		table.setColumnHeader("nonPayable", "Non Payable");
		table.setColumnHeader("netPayable", "Net Payable");
		table.setColumnHeader("eligibleAmt", "Eligible Amt");
		table.setColumnHeader("approvedAmt", "Approved Amount");
		table.setColumnHeader("amtAlreadyPaidToHospital", "Already </br> Paid to </br> Hospital");
		table.setColumnHeader("amtPaybableToInsured", "Amount </br> Payable to </br>Insured");
		table.setColumnHeader("amtAlreadyPaid", "Amount </br> Already Paid to </br> Insured");
		table.setColumnHeader("balancePayable", "Balance Payable");
		table.setColumnHeader("remarks", "Remarks");
		
		/*table.setColumnWidth("benefitName", 200);
		table.setColumnWidth("remarks", 500);*/
		/*table.setColumnWidth("sno", 50);
		table.setColumnWidth("benefitName", 150);
		table.setColumnWidth("noOfDays", 120);
		table.setColumnWidth("amtClaimed", 120);
		table.setColumnWidth("nonPayable", 120);
		table.setColumnWidth("netPayable", 120);
		table.setColumnWidth("approvedAmt", 120);
		table.setColumnWidth("amtPaybableToInsured", 120);
		table.setColumnWidth("amtAlreadyPaid", 120);
		table.setColumnWidth("balancePayable", 120);*/
		
		
		table.setEditable(true);	
		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		table.setFooterVisible(true);
		if(dummyField == null){
			dummyField = new TextField();
		}
		
		table.setColumnFooter("benefitName", "Total");
		table.setHeight("100%");
		
		layout.addComponent(table);
	}
	
	StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter() {
		private static final long serialVersionUID = -2154393632039317675L;

		protected java.text.NumberFormat getFormat(Locale locale) {
	        NumberFormat format = super.getFormat(locale);
	        format.setGroupingUsed(false);
	        return format;
	    };
	};
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			OtherBenefitsTableDto beniftsTableDto = (OtherBenefitsTableDto) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(beniftsTableDto) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(beniftsTableDto, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(beniftsTableDto);
			}			
			if(("noOfDays").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setWidth("50px");
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(beniftsTableDto.isEnabled());
				field.setData(beniftsTableDto);
				tableRow.put("noOfDays", field);
				if(beniftsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.sharedAccomotation)){
					field.setReadOnly(false);
					field.setEnabled(true);
					field.addBlurListener(getNoOfdayListener());
				}
				return field;
			} 
			else if("amtClaimed".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setWidth("100px");
				CSValidator validator = new CSValidator();
				validator.extend(field);
				//field.setConverter(plainIntegerConverter);
				field.setData(beniftsTableDto);
				validator.setRegExp("^[0-9 ,]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("amtClaimed", field);
				field.setEnabled(beniftsTableDto.isEnabled());
				field.addBlurListener(getNonPayableAmountListener());
				if(beniftsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.sharedAccomotation)){
					field.setEnabled(false);
				}
				/*if(!beniftsTableDto.isEnabled()) {
					field.setReadOnly(true);
				}*/				
				return field;
			} 
			else if("nonPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setWidth("100px");
				CSValidator validator = new CSValidator();
				validator.extend(field);
				//field.setConverter(plainIntegerConverter);
				field.setData(beniftsTableDto);
				validator.setRegExp("^[0-9 ,]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("nonPayable", field);
				field.setEnabled(beniftsTableDto.isEnabled());
				if(beniftsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.sharedAccomotation)){
					field.setEnabled(false);
				}

				field.addBlurListener(getNonPayableAmountListener());
				/*if(beniftsTableDto.getNonPayable() != null && beniftsTableDto.getNonPayable() > 0) {
					nonPayableColumnValidation(field);
				}*/

				return field;
			}
			else if(("netPayable").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				//field.setReadOnly(true);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100px");
				field.setData(beniftsTableDto);
				tableRow.put("netPayable", field);
				field.setEnabled(beniftsTableDto.isEnabled());
				field.setEnabled(false);
				return field;
			} else if(("eligibleAmt").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
				field.setData(beniftsTableDto);
				tableRow.put("eligibleAmt", field);
				return field;
			}
			else if("approvedAmt".equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setWidth("100px");
				field.setData(beniftsTableDto);
				tableRow.put("approvedAmt", field);
				field.setEnabled(beniftsTableDto.isEnabled());
				field.setEnabled(false);
				return field;
			}else if(("amtAlreadyPaidToHospital").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setWidth("100px");
				field.setEnabled(false);
				field.setData(beniftsTableDto);
				tableRow.put("amtAlreadyPaidToHospital", field);
				field.setEnabled(beniftsTableDto.isEnabled());
				field.setEnabled(false);
				return field;
			}
			else if(("amtPaybableToInsured").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setWidth("100px");
				field.setData(beniftsTableDto);
				tableRow.put("amtPaybableToInsured", field);
				field.setEnabled(beniftsTableDto.isEnabled());
				field.setEnabled(false);
				calculatePayableToInsure(field);
				return field;
			}
			else if(("amtAlreadyPaid").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setData(beniftsTableDto);
				field.setWidth("100px");
				tableRow.put("amtAlreadyPaid", field);
				field.setEnabled(beniftsTableDto.isEnabled());
				field.setEnabled(false);
				return field;
			}
			else if(("balancePayable").equals(propertyId)){
				TextField field=new TextField();
				field.setNullRepresentation("");
				field.setWidth("100px");
				field.setData(beniftsTableDto);
				tableRow.put("balancePayable", field);
				field.setEnabled(beniftsTableDto.isEnabled());
				//field.addBlurListener(calculateTotalListener());
				field.setEnabled(false);
				calculatePayableToInsure(field);
				calculateTotal();
				return field;
			}
			else if ("remarks".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setNullRepresentation("");
				//field.setWidth("590px");
				field.setMaxLength(500);
				field.setData(beniftsTableDto.getRemarks());
				field.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);				
				handleRemarksPopup(field,null);
				tableRow.put("remarks", field);
				if(!beniftsTableDto.isEnabled()) {
					field.setReadOnly(true);
				}
				return field;
			} 
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					((TextField) field).setNullRepresentation("");
					field.setReadOnly(true);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return field;
			}
			
		}
	}
	
	private void nonPayableColumnValidation(TextField component) {
		if(component.getData() != null) {
			OtherBenefitsTableDto benifitsTableDto = (OtherBenefitsTableDto)component.getData();
			if(benifitsTableDto.getAmtClaimed() < SHAUtils.getIntegerFromStringWithComma(component.getValue())) {
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> NonPayable should not exceed the Amount Claimed </b>", ContentMode.HTML));
				layout.setMargin(true);
				layout.setSpacing(true);
				showErrorPopup(layout);
				component.setValue("0");
			}
		}
		
		calculateTotal();
	}
	public BlurListener getNonPayableAmountListener() {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				
				calculatePayableToInsure(component);
	
				calculateTotal();
			}

		};
		return listener;		
	}
	
	private void calculatePayableToInsure(TextField component) {
		OtherBenefitsTableDto benifitsTableDto = (OtherBenefitsTableDto)component.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(benifitsTableDto);
		TextField nonPayableTxt = (TextField)hashMap.get("nonPayable");
		TextField claimedAmtTxt = (TextField)hashMap.get("amtClaimed");
		TextField netPayableAmtTxt = (TextField)hashMap.get("netPayable");
		TextField eligibleamtTxt = (TextField)hashMap.get("eligibleAmt");
		TextField approvedAmtTxt = (TextField)hashMap.get("approvedAmt");
		TextField amtAlreadyPaidToHospital = (TextField) hashMap.get("amtAlreadyPaidToHospital");
		TextField payableToInsured = (TextField) hashMap.get("amtPaybableToInsured");
		TextField balancePayableTxt = (TextField) hashMap.get("balancePayable");
		TextArea remarksTxt = (TextArea)hashMap.get("remarks");
		
		Integer clmAmt = 0;
		Integer nonPaybable = 0;
		Integer eligibleAmt = 0;
		Integer alreadyPaidToHosp = 0;
		if(claimedAmtTxt != null && claimedAmtTxt.getValue() != null){
			clmAmt = SHAUtils.getIntegerFromStringWithComma(claimedAmtTxt.getValue());
		}
		if(nonPayableTxt != null && nonPayableTxt.getValue() != null){
			nonPaybable = SHAUtils.getIntegerFromStringWithComma(nonPayableTxt.getValue());
		}
		
		Integer netAmt = clmAmt-nonPaybable > 0 ? clmAmt-nonPaybable :0;
		netPayableAmtTxt.setValue(netAmt.toString());
		
		if(eligibleamtTxt != null && eligibleamtTxt.getValue() != null){
			eligibleAmt = SHAUtils.getIntegerFromStringWithComma(eligibleamtTxt.getValue());
		}
		
		Integer minAmt = netAmt < eligibleAmt ? netAmt : eligibleAmt;
		approvedAmtTxt.setValue(minAmt.toString());
		
		if(amtAlreadyPaidToHospital != null && amtAlreadyPaidToHospital.getValue() != null){
			alreadyPaidToHosp = SHAUtils.getIntegerFromStringWithComma(amtAlreadyPaidToHospital.getValue());
		}
		Integer payableToInsuredAmt = minAmt - alreadyPaidToHosp > 0 ? minAmt - alreadyPaidToHosp : 0;
		
		if(payableToInsured != null){
			payableToInsured.setValue(payableToInsuredAmt.toString());
		}
		
		if(balancePayableTxt != null){
			balancePayableTxt.setValue(payableToInsuredAmt.toString());
		}
		
		if(claimedAmtTxt != null && benifitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.treatementForPreferred)){
			if(! benifitsTableDto.getIsPreferred() && clmAmt != 0){
				claimedAmtTxt.setValue("0");
				String alrtLabel = null;
				String alrtRemrk = null;
				if(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					alrtLabel ="<b style = 'color: red;'> HOSPITAL IS NOT VALUABLE SERVICE PROVIDER</b>";
					alrtRemrk="Hospital Is Not Valuable Service Provider";
				}else{
					alrtLabel ="<b style = 'color: red;'> HOSPITAL IS NOT PREFERRED NET  WORK HOSPITAL</b>";
					alrtRemrk="Hospital is not preferred  network hospital";
				}
				VerticalLayout layout = new VerticalLayout(new Label(alrtLabel, ContentMode.HTML));
				layout.setMargin(true);
				layout.setSpacing(true);
				showErrorPopup(layout);
				if(remarksTxt != null && alrtRemrk != null){
					remarksTxt.setValue(alrtRemrk);
				}
			}
		}
	}

	
	public BlurListener getNoOfdayListener() {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				
				OtherBenefitsTableDto benifitsTableDto = (OtherBenefitsTableDto)component.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(benifitsTableDto);
				TextField eligibleamtTxt = (TextField)hashMap.get("eligibleAmt");
				TextField approvedAmtTxt = (TextField)hashMap.get("approvedAmt");
				TextField balancePayableTxt = (TextField) hashMap.get("balancePayable");
				TextField netPayableAmtTxt = (TextField)hashMap.get("netPayable");
				
				Integer noOfdaysLimit = benifitsTableDto.getNoOfdaysLimit() > 0 ? benifitsTableDto.getNoOfdaysLimit() : bean.getPreviousRODNoOfDays();
				
				Integer noOfDays = 0;
				Integer eligibleAmt = 0;
				
				if(component != null){
					if(component != null && component.getValue() != null){
						noOfDays = SHAUtils.getIntegerFromStringWithComma(component.getValue());
					}
				}
				
				Boolean isAlertForSpecialCare = false;
				
				if(ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					if(noOfDays != null && noOfDays > 4){
						isAlertForSpecialCare = true;
					}
				}
				if(noOfdaysLimit != null && noOfdaysLimit < noOfDays){
					VerticalLayout layout = null;
					layout = new VerticalLayout(new Label("<b style = 'color: red;'> No of days exceeded the allowed days of room rent and nursing charges</b>", ContentMode.HTML));
					layout.setMargin(true);
					layout.setSpacing(true);
					showErrorPopup(layout);
					component.setValue("0");
				}else if(isAlertForSpecialCare){
					VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Maximum number of days exceeded (4 days)</b>", ContentMode.HTML));
					layout.setMargin(true);
					layout.setSpacing(true);
					showErrorPopup(layout);
					component.setValue("0");
				}else{
					if(eligibleamtTxt != null && eligibleamtTxt.getValue() != null){
						eligibleAmt = SHAUtils.getIntegerFromStringWithComma(eligibleamtTxt.getValue());
					}
					
					Integer minAmt = noOfDays * eligibleAmt;
					if(approvedAmtTxt != null){
						approvedAmtTxt.setValue(minAmt.toString());
					}
					if(balancePayableTxt != null){
						balancePayableTxt.setValue(minAmt.toString());
					}
					
					if(benifitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.sharedAccomotation)){
						netPayableAmtTxt.setValue(minAmt.toString());
					}
		
					calculateTotal();
				}

			}
			
		};
		return listener;		
	}
	
	
public BlurListener calculateTotalListener() {
		
		BlurListener listener = new BlurListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				calculateTotal();
			}
			
		};
		return listener;		
	}
		
	private void showErrorPopup(VerticalLayout layout) {
		layout.setMargin(true);
		layout.setSpacing(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	public void calculateTotal() {
		
		List<OtherBenefitsTableDto> itemIconPropertyId = (List<OtherBenefitsTableDto>) table.getItemIds();
		if(itemIconPropertyId != null && !itemIconPropertyId.isEmpty()){
			Integer claimedTotal = 0;
			Integer nonPayableTotal=0;
			Integer netPayableTotal=0;
			Integer eligibleAmtTotal=0;
			Integer approvedTotal = 0;
			Integer balancePayableTotal = 0;
			for (OtherBenefitsTableDto dto : itemIconPropertyId) {
				
				    claimedTotal += (dto.getAmtClaimed() != null ? dto.getAmtClaimed().intValue() : 0);
				    nonPayableTotal +=  (null != dto.getNonPayable() ? dto.getNonPayable().intValue() : 0);
					netPayableTotal += (null != dto.getNetPayable() ? dto.getNetPayable().intValue() : 0);
					eligibleAmtTotal += (null != dto.getEligibleAmt() ? dto.getEligibleAmt().intValue() : 0);
					approvedTotal += (null != dto.getApprovedAmt() ? dto.getApprovedAmt().intValue() : 0);
					balancePayableTotal += (null != dto.getBalancePayable() ? dto.getBalancePayable().intValue() : 0);
			}
			table.setColumnFooter("amtClaimed", String.valueOf(claimedTotal));
			table.setColumnFooter("nonPayable", String.valueOf(nonPayableTotal));
			table.setColumnFooter("netPayable", String.valueOf(netPayableTotal));
			table.setColumnFooter("eligibleAmt", String.valueOf(eligibleAmtTotal));
			table.setColumnFooter("approvedAmt", String.valueOf(approvedTotal));
			table.setColumnFooter("balancePayable", String.valueOf(balancePayableTotal));
			dummyField.setValue(String.valueOf(balancePayableTotal));
			bean.getPreauthDataExtractionDetails().setTotalOtherBenefitsApprovedAmt(Double.valueOf(balancePayableTotal));
			bean.getConsolidatedAmtDTO().setOtherBenefitAmt(balancePayableTotal);
		}
	}
	
	 public void addBeanToList(OtherBenefitsTableDto benifitsTableDto) {
	    	data.addBean(benifitsTableDto);
	 }
	 
	 public void addList(List<OtherBenefitsTableDto> benifitsTableDtoList) {
		for (OtherBenefitsTableDto otherBenefitsTableDto : benifitsTableDtoList) {
			 data.addBean(otherBenefitsTableDto);
		}		 
	 }
	 
	 @SuppressWarnings("unchecked")
	public List<OtherBenefitsTableDto> getValues() {
		List<OtherBenefitsTableDto> itemIds = (List<OtherBenefitsTableDto>) this.table.getItemIds() ;
    	return itemIds;
	}
	 
	public void showFinalApprovedAmount() {
		Object[] visibleColumns = this.table.getVisibleColumns();
		List<Object> asList = Arrays.asList(visibleColumns);
		asList.add("");
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

	public  void handleRemarksPopup(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForRedraft(searchField, getShortCutListenerForRedraftRemarks(searchField));
	    
	  }

	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
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
	private ShortcutListener getShortCutListenerForRedraftRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				String remarksValue = (String) txtFld.getData();
				VerticalLayout vLayout =  new VerticalLayout();
				
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
//				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(500);
				txtArea.setRows(25);
				txtArea.setHeight("30%");

				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(event.getProperty().getValue() != null){
							txtFld.setValue((String)event.getProperty().getValue());
						}
					}
				});				
				
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				
				String strCaption = "Remarks";

				dialog.setCaption(strCaption);
				
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
//				dialog.setHeight("75%");
//		    	dialog.setWidth("65%");
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
//				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getRedraftRemarks());
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
//						txtArea.setValue(bean.getRedraftRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
}
