package com.shaic.claim.outpatient.registerclaim.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.outpatient.registerclaim.dto.OPBillDetailsDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OPBillDetailsListenerTable extends ViewComponent {
	private static final long serialVersionUID = 4809460534159116589L;
	
	private Map<OPBillDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OPBillDetailsDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<OPBillDetailsDTO> data = new BeanItemContainer<OPBillDetailsDTO>(OPBillDetailsDTO.class);

	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private Long hospitalKey;
	
	private String packageRateValue;
	
	private String dayCareFlagValue;
	
	private String procedureCodeValue;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private String presenterString;
	
	private OutPatientDTO bean;
	
	public TextField dummyField;
	
	
	//private List<DiagnosisDetailsTableDTO> diagnosisList;
	public List<String> diagnosisList = new ArrayList<String>();
	public void init(String presenterString, OutPatientDTO bean) {
	//public void init(Long hospitalKey, String presenterString,List<DiagnosisDetailsTableDTO> diagnosisList) {
		this.presenterString = presenterString;
		this.bean = bean;
		//this.diagnosisList = diagnosisList;
		diagnosisList = new ArrayList<String>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		initTable(layout);
		table.setWidth("100%");
		//table.setHeight("30%");
		/**
		 * Height is set for table visiblity.
		 * */
		table.setPageLength(table.getItemIds().size());
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	}


	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("Bill Details", data);
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		
		table.setVisibleColumns(new Object[] { "details", "billDate", "billNumber", "claimedAmount", "nonPayableAmt", "payableAmt", "nonPayableReason"});

		table.setColumnHeader("details", "Details");
		table.setColumnHeader("billDate", "Bill Date");
		table.setColumnHeader("billNumber", "Bill Number");
		table.setColumnHeader("claimedAmount", "Claimed </br> Amount </br> (A)");
		table.setColumnHeader("nonPayableAmt", "Deductibles / </br> Non </br> Payable </br> (B)");
		table.setColumnHeader("payableAmt", "Payable </br> Amt </br> (C)");
		table.setColumnHeader("nonPayableReason", "Deductible /</br> Non </br> Payables </br> Reason ");
		table.setEditable(true);
		table.setFooterVisible(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());
		table.setColumnFooter("details", "Total Amt");
		calculateTotal();
	}
	
	@SuppressWarnings("unchecked")
	public Integer calculateTotal() {
		
		List<OPBillDetailsDTO> itemIconPropertyId = (List<OPBillDetailsDTO>) table.getItemIds();
		Integer claimeAmt = 0;
		Integer nonPayableAnmt = 0;
		Integer payableAmt = 0;
		for (OPBillDetailsDTO dto : itemIconPropertyId) {
		    Integer claimeAmount = SHAUtils.getIntegerFromString(dto.getClaimedAmount()) ;
		    claimeAmt += claimeAmount != null ? claimeAmount : 0;
			Integer nonPayableAmount = SHAUtils.getIntegerFromString(dto.getNonPayableAmt());
			nonPayableAnmt +=  null != nonPayableAmount ? nonPayableAmount : 0;
			Integer payableAmount = SHAUtils.getIntegerFromString(dto.getPayableAmt());
			payableAmt += null != payableAmount ? payableAmount : 0;
		}
		table.setColumnFooter("claimedAmount", String.valueOf(claimeAmt));
		table.setColumnFooter("nonPayableAmt", String.valueOf(nonPayableAnmt));
		table.setColumnFooter("payableAmt", String.valueOf(payableAmt));
		return payableAmt;
	}
	
	public Integer getPayableAmt() {
		return SHAUtils.getIntegerFromString(table.getColumnFooter("payableAmt"));
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			OPBillDetailsDTO billDetailsDTO = (OPBillDetailsDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			//Boolean isEnabled =  procedureDTO.getEnableOrDisable() ? true : false;
			
			if (tableItem.get(billDetailsDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(billDetailsDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(billDetailsDTO);
			}
			
			if("details".equals(propertyId)) {
				TextField box = new TextField();
				tableRow.put("details", box);
				box.setData(billDetailsDTO);
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				box.setReadOnly(true);
				return box;
			} else if("billDate".equals(propertyId)) {
				DateField billDate = new DateField();
				billDate.setDateFormat("dd/MM/yyyy");
				tableRow.put("billDate", billDate);
				billDate.setWidth("110px");
				addBillDateListener(billDate);
				return billDate;
			} else if("billNumber".equals(propertyId)) {
				TextField billNumber = new TextField();
				billNumber.setMaxLength(20);
				CSValidator validator1 = new CSValidator();
				
				validator1.extend(billNumber);
				validator1.setRegExp("^[0-9]*$");
				validator1.setPreventInvalidTyping(true);
				billNumber.setNullRepresentation("");
				tableRow.put("billNumber", billNumber);
				billNumber.setWidth("100px");
				return billNumber;
			} else if("claimedAmount".equals(propertyId)) {
				TextField claimedAmount = new TextField();
				claimedAmount.setNullRepresentation("");
				tableRow.put("claimedAmount", claimedAmount);
				claimedAmount.setWidth("100px");
				claimedAmount.setMaxLength(20);
				CSValidator claimedAmountValidator = new CSValidator();
				claimedAmountValidator.extend(claimedAmount);
				claimedAmountValidator.setRegExp("^[0-9]*$");
				claimedAmountValidator.setPreventInvalidTyping(true);
				claimedAmount.setData(billDetailsDTO);
				addClaimedAmtListener(claimedAmount);
				if(claimedAmount.getValue() != null && claimedAmount.getValue().length() > 0) {
					setCalcValues(claimedAmount);
				}
				return claimedAmount;
			}  else if("nonPayableAmt".equals(propertyId)) {
				TextField nonPayableAmt = new TextField();
				nonPayableAmt.setNullRepresentation("");
				tableRow.put("nonPayableAmt", nonPayableAmt);
				nonPayableAmt.setWidth("100px");
				nonPayableAmt.setMaxLength(20);
				CSValidator nonPayableAmtValidator = new CSValidator();
				nonPayableAmtValidator.extend(nonPayableAmt);
				nonPayableAmtValidator.setRegExp("^[0-9]*$");
				nonPayableAmtValidator.setPreventInvalidTyping(true);
				nonPayableAmt.setData(billDetailsDTO);
				addNonPayableAmtListener(nonPayableAmt);
				if(nonPayableAmt.getValue() != null && nonPayableAmt.getValue().length() > 0) {
					setCalcValues(nonPayableAmt);
				}
				return nonPayableAmt;
			} else if("payableAmt".equals(propertyId)) {
				TextField payableAmt = new TextField();
				payableAmt.setNullRepresentation("");
				tableRow.put("payableAmt", payableAmt);
				payableAmt.setWidth("100px");
				payableAmt.setReadOnly(true);
				payableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return payableAmt;
			}  else if("nonPayableReason".equals(propertyId)) {
				TextField nonPayableReason = new TextField();
				nonPayableReason.setNullRepresentation("");
				tableRow.put("nonPayableReason", nonPayableReason);
				nonPayableReason.setWidth("200px");
				nonPayableReason.setMaxLength(100);
				CSValidator validator = new CSValidator();
				
				validator.extend(nonPayableReason);
				validator.setRegExp("^[a-zA-Z 0-9/]*$");
				validator.setPreventInvalidTyping(true);
				return nonPayableReason;
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
	private void addClaimedAmtListener(final TextField field) {
		if (field != null) {
			
			field.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 3650428676195699841L;

				@Override
				public void valueChange(ValueChangeEvent event) {

					TextField selectedField = (TextField) event.getProperty();
					setCalcValues(selectedField);
				}

			
			});
		}

	}
	
	@SuppressWarnings("unused")
	private void addBillDateListener( DateField field) {
		if (field != null) {
			field.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 3650428676195699841L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					DateField selectedField = (DateField) event.getProperty();
					checkBillDate(selectedField);
				}
			});
		}

	}
	
	private void checkBillDate(DateField billDateField) {
		if(billDateField != null && billDateField.getValue() != null) {
			if(!SHAUtils.isDateOfIntimationWithPolicyRange(this.bean.getPolicy().getPolicyFromDate(), this.bean.getPolicy().getPolicyToDate(), billDateField.getValue())) {
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Bill Date should be between Policy Start and Policy End Date  </b>", ContentMode.HTML));
				showErrorPopup(billDateField, layout);
			}
		}
	}
	
	private void setCalcValues(TextField selectedField) {
		OPBillDetailsDTO billDetailsDTO = (OPBillDetailsDTO) selectedField.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(billDetailsDTO);
		TextField nonPayableField = (TextField) hashMap.get("nonPayableAmt");
		TextField payableField = (TextField) hashMap.get("payableAmt");
		TextField field = (TextField) hashMap.get("claimedAmount");
		if(nonPayableField != null && field != null ) {
			Integer calculatedAmt =  SHAUtils.getIntegerFromString(field.getValue()) - SHAUtils.getIntegerFromString(nonPayableField.getValue());
			if(calculatedAmt < 0) {
				VerticalLayout layout = new VerticalLayout(new Label("<b style = 'color: red;'> Deductible Amount Should not exceed Claimed Amount  </b>", ContentMode.HTML));
				showErrorPopup(nonPayableField, layout);
			} else {
				if(payableField != null && calculatedAmt >= 0 ) {
					 payableField.setReadOnly(false);
					 payableField.setValue(String.valueOf(calculatedAmt));
					 payableField.setReadOnly(false);
				 }
			}
			 
			} else if(payableField != null) {
				payableField.setReadOnly(false);
				payableField.setValue("0");
				payableField.setReadOnly(true);
		}
		calculateTotal();
	}
	private void showErrorPopup(TextField field, VerticalLayout layout) {
		layout.setMargin(true);
		layout.setSpacing(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		field.setValue(null);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	private void showErrorPopup(DateField field, VerticalLayout layout) {
		layout.setMargin(true);
		layout.setSpacing(true);
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(true);
		dialog.setResizable(false);
		dialog.setContent(layout);
		dialog.setCaption("Error");
		dialog.setClosable(true);
		field.setValue(null);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	@SuppressWarnings("unused")
	private void addNonPayableAmtListener(final TextField field) {
		if (field != null) {
			field.addBlurListener(new BlurListener() {
				private static final long serialVersionUID = -4534541748687139054L;

				@Override
				public void blur(BlurEvent event) {
					TextField selectedField = (TextField) event.getComponent();
					setCalcValues(selectedField);
				}
			});
		}

	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	 public List<OPBillDetailsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<OPBillDetailsDTO> itemIds = (List<OPBillDetailsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	
	public void addBeanToList(OPBillDetailsDTO billDetailsDTO) {
    	data.addItem(billDetailsDTO);
    }
	
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<OPBillDetailsDTO> itemIds = (Collection<OPBillDetailsDTO>) table.getItemIds();
		Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();
		for (OPBillDetailsDTO bean : itemIds) {
			Set<ConstraintViolation<OPBillDetailsDTO>> validate = validator.validate(bean);

			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<OPBillDetailsDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		return !hasError;
	}
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	@SuppressWarnings("unchecked")
	public void setBillEntryValues() {
//		String billDateStr = "";
//		this.bean.getDocumentDetails().setTotalBillAmount(SHAUtils.getDoubleValueFromString(table.getColumnFooter("claimedAmount")) );
//		this.bean.getDocumentDetails().setDeductions(SHAUtils.getDoubleValueFromString(table.getColumnFooter("nonPayableAmt")) );
//		this.bean.getDocumentDetails().setApprovedAmount(SHAUtils.getDoubleValueFromString(table.getColumnFooter("payableAmt")) );
//		this.bean.getDocumentDetails().setNetPayableAmount(SHAUtils.getDoubleValueFromString(table.getColumnFooter("payableAmt")) );
//		List<OPBillDetailsDTO> itemIconPropertyId = (List<OPBillDetailsDTO>) table.getItemIds();
//		for (OPBillDetailsDTO dto : itemIconPropertyId) {
//		    billDateStr += SHAUtils.formatDateForOP(dto.getBillDate()) + ", ";
//		}
//		this.bean.getDocumentDetails().setBillDateStr(billDateStr);
	}

}
