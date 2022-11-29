package com.shaic.claim.premedical.dto;

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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class RevisedOtherBenifitsTable extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<OtherBenefitsTableDto, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OtherBenefitsTableDto, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<OtherBenefitsTableDto> data;
	
	private Table table;
	
	PreauthDTO bean;
	
	private Double totalBenefitApprovedAmt;
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	public Object[] VISIBLE_COLUMNS = new Object[] {"sno",
			"benefitName", "applicable", "amtClaimed", "nonPayable", "netPayable", "eligibleAmt", "approvedAmt", "remarks" };
	
	public TextField dummyField;

	private TextField totalReverseAmtField;
	
	private HorizontalLayout hLayout;

	private VerticalLayout layout;
	
	BeanItemContainer<SelectValue> applicableContainer;
	
	private String presenterString;
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.errorMessages = new ArrayList<String>();
		
		layout = new VerticalLayout();
		
		data = new BeanItemContainer<OtherBenefitsTableDto>(OtherBenefitsTableDto.class);
		
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	public void setPresenterString(String presenterString){
		this.presenterString = presenterString;
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
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setVisibleColumns(VISIBLE_COLUMNS);
		
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("benefitName", "Benefits");
		table.setColumnHeader("applicable", "Applicable");
		table.setColumnHeader("amtClaimed", "Amount Claimed");
		table.setColumnHeader("nonPayable", "Non Payable");
		table.setColumnHeader("netPayable", "Net Payable");
		table.setColumnHeader("eligibleAmt", "Eligible Amount");
		table.setColumnHeader("approvedAmt", "Approved Amount");
		table.setColumnHeader("remarks", "Remarks");
		
		table.setColumnWidth("sno", 50);
		table.setColumnWidth("benefitName", 330);
		table.setColumnWidth("amtClaimed", 120);
		table.setColumnWidth("nonPayable", 85);
		table.setColumnWidth("netPayable", 85);
		table.setColumnWidth("eligibleAmt", 105);
		table.setColumnWidth("approvedAmt", 140);
		
		table.setColumnWidth("remarks", 500);
		
		table.setEditable(true);
		
		applicableContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		applicableContainer.addBean(new SelectValue(1l,"Yes"));
		applicableContainer.addBean(new SelectValue(0l,"No"));
		
		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		table.setFooterVisible(true);
		if(dummyField == null){
			dummyField = new TextField();
		}
		
		table.setColumnFooter("benefitName", "Total");
		
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
			
			if("applicable".equals(propertyId)) {				
				GComboBox field = new GComboBox();
				field.setData(beniftsTableDto);
				
				field.setWidth("70px");
				field.setContainerDataSource(applicableContainer);
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				field.setNullSelectionAllowed(false);
				field.addValueChangeListener(getApplicableChangeListener());
				if(beniftsTableDto.getApplicable() != null){
					field.setValue(beniftsTableDto.getApplicable());
					
				}
				tableRow.put("applicable", field);
				
				return field;
			}
			else if("amtClaimed".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(10);
				validator.extend(field);
				field.setConverter(plainIntegerConverter);
				field.setData(beniftsTableDto);
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("amtClaimed", field);
				field.setMaxLength(10);
				if(!beniftsTableDto.isEnabled()) {
					field.setReadOnly(true);
				}		
				field.addBlurListener(getNonPayableAmountListener());
				return field;
			} 
			else if("nonPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				CSValidator validator = new CSValidator();
				field.setMaxLength(10);
				validator.extend(field);
				field.setConverter(plainIntegerConverter);
				field.setData(beniftsTableDto);
				validator.setRegExp("^[0-9 ]*$");	
				validator.setPreventInvalidTyping(true);
				tableRow.put("nonPayable", field);
				field.setMaxLength(10);
				
				if(!beniftsTableDto.isEnabled()) {
					field.setReadOnly(true);
				}
				
				field.addBlurListener(getNonPayableAmountListener());
//				if(beniftsTableDto.getNonPayable() != null && beniftsTableDto.getNonPayable() > 0) {
//					nonPayableColumnValidation(field);
//				}
				return field;
			}
			else if(("netPayable").equals(propertyId)){
				TextField field=new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
				field.setData(beniftsTableDto);
				tableRow.put("netPayable", field);
				
				return field;
			} else if(("eligibleAmt").equals(propertyId)){
				TextField field=new TextField();
				field.setWidth("100%");
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
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(false);
				field.setData(beniftsTableDto);
				tableRow.put("approvedAmt", field);
				return field;
			}
			else if ("remarks".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setNullRepresentation("");
				field.setWidth("400px");
				field.setRows(3);
				field.setMaxLength(1000);
				tableRow.put("remarks", field);
				if(!beniftsTableDto.isEnabled()) {
					field.setReadOnly(true);
				}
				calculateTotal();
				return field;
			} 
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					((TextField) field).setNullRepresentation("");
					field.setReadOnly(true);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setWidth("100%");
				return field;
			}
			
		}
	}
	
//	private void nonPayableColumnValidation(TextField component) {
//		if(component.getData() != null) {
//			OtherBenefitsTableDto benifitsTableDto = (OtherBenefitsTableDto)component.getData();
//			if(benifitsTableDto != null && benifitsTableDto.getAmtClaimed() != null && component.getValue() != null){
//				if(benifitsTableDto.getAmtClaimed() < SHAUtils.getIntegerFromStringWithComma(component.getValue())) {
//					VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> NonPayable should not exceed the Amount Claimed </b>", ContentMode.HTML));
//					layout.setMargin(true);
//					layout.setSpacing(true);
//					showErrorPopup(layout);
//					benifitsTableDto.setNonPayable(0d);
//					component.setValue("0");
//				}
//			}
////			else {
////				component.setValue("0");
////			}
//		}
//		
//		calculateTotal();
//	}
	
	public void setEnableORDisable(boolean enable){
		table.setEditable(enable);
	}
	
	public ValueChangeListener getApplicableChangeListener(){
		
		ValueChangeListener changeListener = new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				ComboBox component = (ComboBox) event.getProperty();
				
				SelectValue select = (SelectValue)component.getValue();
				OtherBenefitsTableDto benifitsTableDto = (OtherBenefitsTableDto)component.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(benifitsTableDto);
				TextField claimedAmtTxt = (TextField)hashMap.get("amtClaimed");
				TextField nonPayableTxt = (TextField)hashMap.get("nonPayable");
				TextArea remarksTxt = (TextArea)hashMap.get("remarks");
				
				
				TextField netPayableTxt = (TextField)hashMap.get("netPayable");
				TextField approvedAmtTxt = (TextField)hashMap.get("approvedAmt");
				
				if(claimedAmtTxt != null && nonPayableTxt != null && remarksTxt != null){
					if(SHAConstants.No.equalsIgnoreCase(select.getValue())){
						
						claimedAmtTxt.setReadOnly(false);
						claimedAmtTxt.setValue(null);
						benifitsTableDto.setAmtClaimed(0d);
						claimedAmtTxt.setReadOnly(true);
						benifitsTableDto.setNonPayable(0d);
						nonPayableTxt.setReadOnly(false);
						nonPayableTxt.setValue(null);
						nonPayableTxt.setReadOnly(true);
						
						netPayableTxt.setReadOnly(false);
						benifitsTableDto.setNetPayable(0d);
						netPayableTxt.setValue(null);
						netPayableTxt.setReadOnly(true);
						
						approvedAmtTxt.setReadOnly(false);
						benifitsTableDto.setApprovedAmt(0d);
						approvedAmtTxt.setValue(null);
						approvedAmtTxt.setReadOnly(true);
						
						remarksTxt.setReadOnly(false);
						benifitsTableDto.setRemarks("");
						remarksTxt.setValue(null);
						remarksTxt.setReadOnly(true);
						benifitsTableDto.setEnabled(false);
					}
					else{
						benifitsTableDto.setEnabled(true);
						claimedAmtTxt.setReadOnly(false);					
						nonPayableTxt.setReadOnly(false);
						netPayableTxt.setReadOnly(true);
						approvedAmtTxt.setReadOnly(true);
						remarksTxt.setReadOnly(false);					
					}
				}				
			}
		};
		
		return changeListener;
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
				
				OtherBenefitsTableDto benifitsTableDto = (OtherBenefitsTableDto)component.getData();
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(benifitsTableDto);
				ComboBox applicableCmb = (ComboBox) hashMap.get("applicable");
				TextField nonPayableTxt = (TextField)hashMap.get("nonPayable");
				TextField claimedAmtTxt = (TextField)hashMap.get("amtClaimed");
				TextField netPayableAmtTxt = (TextField)hashMap.get("netPayable");
				TextField eligibleamtTxt = (TextField)hashMap.get("eligibleAmt");
				TextField approvedAmtTxt = (TextField)hashMap.get("approvedAmt");
				
				SelectValue applicable = (SelectValue)applicableCmb.getValue();
				
				if(applicable != null && (SHAConstants.YES).equalsIgnoreCase(applicable.getValue())){
					if(claimedAmtTxt != null && claimedAmtTxt.getValue() != null && nonPayableTxt != null && nonPayableTxt.getValue() != null){
						
						String claimedAmount = claimedAmtTxt.getValue();
						Integer clmAmt = claimedAmount != null ? SHAUtils.getIntegerFromStringWithComma(claimedAmount) : 0;
						
						String nonPayable = nonPayableTxt.getValue();
						Integer nonPayableAmt = nonPayable != null ? SHAUtils.getIntegerFromStringWithComma(nonPayable) : 0;
						
						if(nonPayableAmt > clmAmt ){

						VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> NonPayable should not exceed the Amount Claimed </b>", ContentMode.HTML));
						layout.setMargin(true);
						layout.setSpacing(true);
						showErrorPopup(layout);
						
						}
						else{
							Integer totalNetPayable = clmAmt > nonPayableAmt ? clmAmt - nonPayableAmt : 0;					
							
							netPayableAmtTxt.setReadOnly(false);
							netPayableAmtTxt.setValue(String.valueOf(totalNetPayable));
							netPayableAmtTxt.setReadOnly(true);					
							
							Integer eligibleAmt = eligibleamtTxt.getValue() != null ? SHAUtils.getIntegerFromStringWithComma(eligibleamtTxt.getValue()) : 0;
							
							Integer appAmount = Math.min(totalNetPayable,eligibleAmt);
								
							approvedAmtTxt.setReadOnly(false);
							approvedAmtTxt.setValue(String.valueOf(appAmount));
							approvedAmtTxt.setReadOnly(true);							
						}						
						
					}	
				}								
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
			for (OtherBenefitsTableDto dto : itemIconPropertyId) {
				
				if(dto.getApplicable() != null && (SHAConstants.YES).equalsIgnoreCase(dto.getApplicable().getValue())){
				    claimedTotal += (dto.getAmtClaimed() != null ? dto.getAmtClaimed().intValue() : 0);
				    nonPayableTotal +=  (null != dto.getNonPayable() ? dto.getNonPayable().intValue() : 0);
					netPayableTotal += (null != dto.getNetPayable() ? dto.getNetPayable().intValue() : 0);
					eligibleAmtTotal += (null != dto.getEligibleAmt() ? dto.getEligibleAmt().intValue() : 0);
					approvedTotal += (null != dto.getApprovedAmt() ? dto.getApprovedAmt().intValue() : 0);
				}
			}
			table.setColumnFooter("amtClaimed", String.valueOf(claimedTotal));
			table.setColumnFooter("nonPayable", String.valueOf(nonPayableTotal));
			table.setColumnFooter("netPayable", String.valueOf(netPayableTotal));
			table.setColumnFooter("eligibleAmt", String.valueOf(eligibleAmtTotal));
			table.setColumnFooter("approvedAmt", String.valueOf(approvedTotal));
			
			if(this.presenterString == null){
				bean.getPreauthDataExtractionDetails().setTotalOtherBenefitsApprovedAmt(Double.valueOf(approvedTotal));
			}
			
			totalBenefitApprovedAmt = Double.valueOf(approvedTotal);
		}
	}
	
	public Double getTotalApprovedBenefitAmount(){
		calculateTotal();
		return totalBenefitApprovedAmt;
	
	}
	 public void addBeanToList(OtherBenefitsTableDto benifitsTableDto) {
		 	setEnableORDisable(true);
	    	data.addBean(benifitsTableDto);
	 }
	 
	 public void addList(List<OtherBenefitsTableDto> benifitsTableDtoList) {
		 setEnableORDisable(true);
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
	
	public void setDefaultValues(){
		List<OtherBenefitsTableDto> dtoList = (List<OtherBenefitsTableDto>)table.getItemIds();
//				bean.getPreauthDataExtractionDetails().getOtherBenefitsList();
		
		if(dtoList != null && !dtoList.isEmpty()){			
			for (OtherBenefitsTableDto otherBenefitsTableDto : dtoList) {
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(otherBenefitsTableDto);
				if(hashMap != null){
					ComboBox applicableCmb = (ComboBox) hashMap.get("applicable");
					
					for (SelectValue select : applicableContainer.getItemIds()) {
						if(select.getValue().equalsIgnoreCase(otherBenefitsTableDto.getApplicable().getValue())){
							applicableCmb.setValue(select);
							break;
						}
					}
					
				}	
			}			
		}		
	}
	
	public void setApplicableBenefit(){
		List<OtherBenefitsTableDto> dtoList = bean.getPreauthDataExtractionDetails().getOtherBenefitsList();
		
		if(dtoList != null && !dtoList.isEmpty()){			
			for (OtherBenefitsTableDto otherBenefitsTableDto : dtoList) {
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(otherBenefitsTableDto);
				if(hashMap != null){
					ComboBox applicableCmb = (ComboBox) hashMap.get("applicable");
					applicableCmb.setValue(applicableContainer.getItem(otherBenefitsTableDto));
				}	
			}			
		}
	}

}
