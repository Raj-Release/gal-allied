package com.shaic.claim.rod.wizard.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.listenerTables.PEDValidationListenerTableForPremedical.ImmediateFieldFactory;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class HospitalCashProductDetailsTable extends ViewComponent {
	
	private Map<HopsitalCashBenefitDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<HopsitalCashBenefitDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<HopsitalCashBenefitDTO> data = new BeanItemContainer<HopsitalCashBenefitDTO>(HopsitalCashBenefitDTO.class);

	private Table table;	
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private ReceiptOfDocumentsDTO bean;
	
	private String presenterString;
	private Boolean admittedDays = false;
	
	public void init(ReceiptOfDocumentsDTO bean) {
//		this.presenterString = presenterString;
		this.bean = bean;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		VerticalLayout layout = new VerticalLayout();
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		layout.setMargin(true);
		layout.addComponent(table);

		setCompositionRoot(layout);
	}
	
	void initTable() {

		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());

		
		table.setVisibleColumns(new Object[] { "particularsValue", "hospitalCashDays", "hospitalCashPerDayAmt","hospitalCashTotalClaimedAmt","noOfDaysAllowed","disallowanceRemarks" });

		table.setColumnHeader("particularsValue", "Particulars");
		table.setColumnHeader("hospitalCashDays", "No. of Days Claimed");
		table.setColumnHeader("hospitalCashPerDayAmt", "Per day Claimed Amount");
		table.setColumnHeader("hospitalCashTotalClaimedAmt", "Total Claimed Amount");
		table.setColumnHeader("noOfDaysAllowed", "No Of Days Allowed");
		table.setColumnHeader("disallowanceRemarks", "Disallowance Remarks");
//		table.setColumnWidth("DisallowanceRemarks", 150);
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());

	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			
			HopsitalCashBenefitDTO hospitalCash = (HopsitalCashBenefitDTO) itemId;
			
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(hospitalCash) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(hospitalCash, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(hospitalCash);
			}
			
			if("particularsValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setData(hospitalCash);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("particularsValue", field);
				return field;
			} else if ("hospitalCashDays".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
//				field.setReadOnly(true);
//				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(hospitalCash);
				 field.addBlurListener(getCalculateAmtListener());
				tableRow.put("hospitalCashDays", field);
				return field;
			}  
			else if ("hospitalCashPerDayAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
//				field.setReadOnly(false);
//				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(hospitalCash);
				field.addBlurListener(getCalculateAmtListener());
				tableRow.put("hospitalCashPerDayAmt", field);
//				setCalculatedValue();
				return field;
			} else if ("hospitalCashTotalClaimedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
//				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("hospitalCashTotalClaimedAmt", field);
				return field;
			} else if ("noOfDaysAllowed".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
//				field.setReadOnly(true);
//				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(hospitalCash);
				field.addBlurListener(getcalculateNoOfDaysAdmitted());
				tableRow.put("noOfDaysAllowed", field);
				return field;
			} else if ("disallowanceRemarks".equals(propertyId)) {
				TextArea field = new TextArea();
				field.setNullRepresentation("");
//				field.setReadOnly(true);
//				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(hospitalCash);
				tableRow.put("disallowanceRemarks", field);
				SHAUtils.handleTextAreaPopupDetails(field,null,getUI(),SHAConstants.DISALLOWANCE_REMARKS);
				return field;
			}else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				field.setEnabled(true);
				return field;
			}
		}
		}
	
	public List<HopsitalCashBenefitDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<HopsitalCashBenefitDTO> itemIds = (List<HopsitalCashBenefitDTO>) this.table.getItemIds() ;
    	return itemIds;
    }
    
    public void addBeanToList(HopsitalCashBenefitDTO diagnosisPEDDTO) {
    	data.addItem(diagnosisPEDDTO);
    }
    
    public void removeAllItems()
	{
    	if(table.getItemIds() !=null && table.getItemIds().size()>0){
		table.removeAllItems();
    	}
	}
    

     public BlurListener getCalculateAmtListener() {
		
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField component = (TextField) event.getComponent();
				calculationMethodForAmt(component);
				
			}
		};
		return listener;
	}
	
	@SuppressWarnings("deprecation")
	private void calculationMethodForAmt(Component component){
		HopsitalCashBenefitDTO hopsitalCashBenefitDTO =new HopsitalCashBenefitDTO();
		TextField txtField  = (TextField) component;
		hopsitalCashBenefitDTO = (HopsitalCashBenefitDTO)txtField.getData();
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(hopsitalCashBenefitDTO);
		TextField nooddays = (TextField) hashMap.get("hospitalCashDays");
		TextField amt = (TextField) hashMap.get("hospitalCashPerDayAmt");
		TextField totamt = (TextField) hashMap.get("hospitalCashTotalClaimedAmt");

		if(nooddays !=  null && amt != null && amt.getValue() != null && nooddays.getValue() != null 
				&& !nooddays.getValue().isEmpty() && !amt.getValue().isEmpty()){
		Integer hospitalCashDays = Integer.valueOf(nooddays.getValue());
		Integer hospitalCashPerDayAmt = Integer.valueOf(amt.getValue());
		Integer calculatedValue = hospitalCashDays * hospitalCashPerDayAmt;
		if(calculatedValue != null){
			totamt.setReadOnly(false);
			totamt.setValue(String.valueOf(calculatedValue));
			totamt.setReadOnly(true);
			}
		}
		else {
			if((amt == null || amt.getValue() == null || amt.getValue().isEmpty()) || 
					(nooddays == null || nooddays.getValue() == null || nooddays.getValue().isEmpty())){
				totamt.setReadOnly(false);
				totamt.setValue("0");
				totamt.setReadOnly(true);
				
			}
		}
		
	}
	
	public void clearTableItems() {
		if(tableItem != null){
			tableItem.clear();
		}
	}
	
	private void calculateNoOfDaysAdmitted(Component component,String enteredDays)
	{
		Long diff = 0l;
		if(null != bean.getDocumentDetails().getDateOfAdmission() && null !=  bean.getDocumentDetails().getDateOfDischarge())
		{
		  diff =  SHAUtils.getDaysBetweenDate (bean.getDocumentDetails().getDateOfAdmission(),bean.getDocumentDetails().getDateOfDischarge());
		}
		  diff = diff+1;
		if(Long.valueOf(enteredDays) > diff){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createAlertBox("No of Allowed days is more than the Total No of Days Hospitalized ", buttonsNamewithType);
			HopsitalCashBenefitDTO hopsitalCashBenefitDTO =new HopsitalCashBenefitDTO();
			if(component != null){
			TextField txtField  = (TextField) component;
			hopsitalCashBenefitDTO = (HopsitalCashBenefitDTO)txtField.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(hopsitalCashBenefitDTO);
			TextField columnTxtValue = (TextField) hashMap.get("noOfDaysAllowed");
			columnTxtValue.setValue("");
			}
		}
	}
	
	  public BlurListener getcalculateNoOfDaysAdmitted() {
			
			BlurListener listener = new BlurListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					String enteredDays=component.getValue();
					if(enteredDays != null && !enteredDays.isEmpty()){
					calculateNoOfDaysAdmitted(component,enteredDays);
					}
				}
			};
			return listener;
		}
	
}
