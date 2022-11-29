package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.billing.processclaimbilling.search.PASearchProcessClaimBillingService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
 
public class AddOnCoversListenerTable extends ViewComponent {
	
private Table table;

private Boolean onLoad = Boolean.TRUE;

private BeanItemContainer<SelectValue> addOnCoverContainer;

private Map<String, Object> referenceData;

private Map<AddOnCoversTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<AddOnCoversTableDTO, HashMap<String, AbstractField<?>>>();

@EJB
private DBCalculationService dbCalculationService;

@EJB
private PASearchProcessClaimBillingService paClaimBillingService;

	
	BeanItemContainer<AddOnCoversTableDTO> data = new BeanItemContainer<AddOnCoversTableDTO>(AddOnCoversTableDTO.class);
	public TextField netAmtText = new TextField();
	
	//public static Boolean onLoad = Boolean.TRUE;
	
	PreauthDTO bean;
	
	//List<AddOnCoversTableDTO> onLoadAddOnCoversTableList = new ArrayList<AddOnCoversTableDTO>();
	
	public Object[] VISIBLE_COLUMNS = new Object[] {"addonCovers","eligibleForPolicy","noOfchildAgeLess18",
			"allowableChildren","billNo","billDate","billAmount","deduction","netamount","siLimit","eligibleAmount","approvedAmount","reasonForDeduction","remarks"};
	
	
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		table.setWidth("100%");
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);
		
		//onLoadAddOnCoversTableList.addAll(addOnCoversTableListBilling);
		
		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(VISIBLE_COLUMNS);
		
		table.setColumnHeader("addonCovers", "Add on Covers");
		table.setColumnHeader("eligibleForPolicy", "Eligible for Policy");
		//table.setColumnHeader("amountClaimed", "Amount Claimed");
		table.setColumnHeader("noOfchildAgeLess18", "No. of Children Age lesser than 18 yrs");
		table.setColumnHeader("allowableChildren", "Allowable Children (as per prod)");
		table.setColumnHeader("billNo", "Bill No");
		table.setColumnHeader("billDate", "Bill Date");
		table.setColumnHeader("billAmount", "Bill Amount");
		table.setColumnHeader("deduction", "Deduction");
		table.setColumnHeader("netamount", "Net Amt");
		table.setColumnHeader("siLimit", "SI Limit");
		table.setColumnHeader("eligibleAmount", "Eligible Amount(Product Based)");
		table.setColumnHeader("approvedAmount", "Approved Amount");
		table.setColumnHeader("reasonForDeduction", "Reason For Deductions");
		table.setColumnHeader("remarks", "Remarks");
		
		
		table.setEditable(true);
		
		table.setTableFieldFactory(new ImmediateFieldFactory());	
		
		table.setFooterVisible(true);
		
		table.setColumnFooter("billDate", "Total");
		
		layout.addComponent(table);
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		/*private static final long serialVersionUID = -2192723245525925990L;*/
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			AddOnCoversTableDTO addOnCoversTableDTO = (AddOnCoversTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if(tableItem.get(addOnCoversTableDTO) == null) {
				tableItem.put(addOnCoversTableDTO,
						new HashMap<String, AbstractField<?>>());
			}
			tableRow = tableItem.get(addOnCoversTableDTO);
			
			 if("addonCovers".equals(propertyId)){
				GComboBox field = new GComboBox();
				field.setData(addOnCoversTableDTO);
				tableRow.put("addonCovers", field);
				field.setNullSelectionAllowed(false);
				if(null != bean.getPreauthDataExtractionDetails().getDocAckknowledgement() 
						   &&  bean.getPreauthDataExtractionDetails().getReconsiderationFlag() != null && SHAConstants.YES_FLAG.equalsIgnoreCase( bean.getPreauthDataExtractionDetails().getReconsiderationFlag()))
				{
					field.setEnabled(false);
				}
			//	if(!onLoad){
					//addOnCoverListener(field, addOnCoversTableDTO.getRodId());
			//	}
				field.setEnabled(false);
					addCoversValues(field);
					addCoversListener(field);
				return field;
			}
			
			else if("eligibleForPolicy".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(addOnCoversTableDTO);
				tableRow.put("eligibleForPolicy", field);
				//field.setReadOnly(true);
				return field;
			}
			/*else if("amountClaimed".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				field.setData(addOnCoversTableDTO);
				tableRow.put("amountClaimed", field);
				field.setReadOnly(false);
				return field;
			}*/
			else if("noOfchildAgeLess18".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				//field.setReadOnly(false);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				if(addOnCoversTableDTO.getAddonCovers()!=null){
					String addOnValue = addOnCoversTableDTO.getAddonCovers().getValue();
					Boolean isreadOnly = isEducationGrant(addOnValue);
					if(isreadOnly){
						field.setReadOnly(Boolean.FALSE);
					}else{
						field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
						field.setReadOnly(Boolean.TRUE);
					}
					
				}
				if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() && (ReferenceTable.SCHOOL_STUDENT_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
					||  ReferenceTable.COLLEGE_STUDENT_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())))
				{					
					field.setEnabled(false);
				}
				else
				{
					field.setEnabled(true);
				}
				
				field.setData(addOnCoversTableDTO);
				addNetAmountListener(field);
				tableRow.put("noOfchildAgeLess18", field);
				return field;
			}else if("allowableChildren".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(addOnCoversTableDTO);
				//field.setValue("2");
				//doReadOnly(addOnCoversTableDTO, field);
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("allowableChildren", field);
				return field;
			}
			else if("billNo".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				doReadOnly(addOnCoversTableDTO, field);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(addOnCoversTableDTO);
				tableRow.put("billNo", field);
				return field;
			}
			else if("billDate".equals(propertyId)){
				DateField field = new DateField();
				//TextField field = new TextField();
				//field.setNullRepresentation("");
				if(addOnCoversTableDTO.getAddonCovers()!=null){
					String addOnValue = addOnCoversTableDTO.getAddonCovers().getValue();
					Boolean isreadOnly = isEducationGrant(addOnValue);
					field.setReadOnly(isreadOnly);
					if(Boolean.TRUE == isreadOnly){
						field.setStyleName(ValoTheme.DATEFIELD_BORDERLESS);
					}
					
				}
				field.setDateFormat("dd/MM/yyyy");
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(addOnCoversTableDTO);
				tableRow.put("billDate", field);
				return field;
			}
			
			else if("billAmount".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				addNetAmountListener(field);
				doReadOnly(addOnCoversTableDTO, field);
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(addOnCoversTableDTO);
				tableRow.put("billAmount", field);
				return field;
			}
			else if ("deduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				addNetAmountListener(field);
				doReadOnly(addOnCoversTableDTO, field);
				field.setMaxLength(100);
				field.setData(addOnCoversTableDTO);
				tableRow.put("deduction", field);
				return field;
			
			}else if ("netamount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				doReadOnly(addOnCoversTableDTO, field);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setReadOnly(true);
				field.setData(addOnCoversTableDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("netamount", field);
				return field;
			}else if ("siLimit".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setReadOnly(true);
				field.setData(addOnCoversTableDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("siLimit", field);
				return field;
			}else if ("eligibleAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setReadOnly(true);
				field.setData(addOnCoversTableDTO);
				addNetAmountListener(field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("eligibleAmount", field);
				return field;
			}else if ("availableSI".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setReadOnly(Boolean.TRUE);
				field.setData(addOnCoversTableDTO);
				tableRow.put("availableSI", field);
				addNetAmountListener(field);
				return field;
			}else if ("approvedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				field.setData(addOnCoversTableDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
//				addNetAmountListener(field);
				//field.setReadOnly(Boolean.TRUE);
				tableRow.put("approvedAmount", field);
				return field;
			}else if ("reasonForDeduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				//field.setData(addOnCoversTableDTO);
				addNetAmountListener(field);
				tableRow.put("reasonForDeduction", field);
				return field;
			}else if ("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setMaxLength(100);
				field.setWidth("200px");
				field.setMaxLength(100);
				//field.setData(addOnCoversTableDTO);
				//addNetAmountListener(field);
				tableRow.put("remarks", field);
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

		private void doReadOnly(AddOnCoversTableDTO addOnCoversTableDTO,
				TextField field) {
			if(addOnCoversTableDTO.getAddonCovers()!=null){
				String addOnValue = addOnCoversTableDTO.getAddonCovers().getValue();
				Boolean isreadOnly = isEducationGrant(addOnValue);
				field.setReadOnly(isreadOnly);
				if(Boolean.TRUE.equals(isreadOnly)){
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);			
				}
				
				
			}
		}
	}
	private Boolean isEducationGrant(String addonValue){
		if(addonValue.equalsIgnoreCase("EDUCATIONAL GRANT")){
			return Boolean.TRUE;
		}	
		return Boolean.FALSE;
	}
	
	
	public void setDropDownValues(BeanItemContainer<SelectValue> coverContainer)
	{
		this.addOnCoverContainer = coverContainer;
	}
	
	public void addCoversValues(GComboBox comboBox) {
		//BeanItemContainer<SelectValue> fileTypeContainer = null;
		comboBox.setContainerDataSource(addOnCoverContainer);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
	}
	public void addBeanToList(AddOnCoversTableDTO benefitsDTO) {
    	data.addItem(benefitsDTO);

    }
	
	public void addValue(AddOnCoversTableDTO setAddonDtoforCoverId , ComboBox cmb)
	{
		if(null != cmb)
		{
			AddOnCoversTableDTO dto = (AddOnCoversTableDTO) cmb.getData();
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
				// TextField billAmtFld = (TextField) hashMap.get("appAmt");
				// TextField deductionsFld = (TextField) hashMap.get("deduction");
				 if(null != hashMap)
				 {   
					 TextField approvedAmt = (TextField) hashMap.get("approvedAmount");
					 TextField noOfchild = (TextField) hashMap.get("noOfchildAgeLess18");
					 TextField allowableChild = (TextField) hashMap.get("allowableChildren");
					 TextField eligibleAmount = (TextField) hashMap.get("eligibleAmount");
					 TextField billAmount = (TextField) hashMap.get("billAmount");
					 TextField deductionAmount = (TextField) hashMap.get("deduction");
					 GComboBox addOncover = (GComboBox) hashMap.get("addonCovers");
					 TextField netamountText = (TextField) hashMap.get("netamount");
					 
					 
					 
					 if(null != setAddonDtoforCoverId )
					 {
						 if(null != approvedAmt && !("").equals(approvedAmt))
						 {	
							 if(null != setAddonDtoforCoverId.getApprovedAmount())
							 {
							 approvedAmt.setReadOnly(Boolean.FALSE);
							 approvedAmt.setValue(String.valueOf(setAddonDtoforCoverId.getApprovedAmount()));
							 approvedAmt.setReadOnly(Boolean.TRUE);
							 }
						 else
						 {
							 approvedAmt.setReadOnly(Boolean.FALSE);
							 approvedAmt.setValue(null);
							 approvedAmt.setReadOnly(Boolean.TRUE);
						 }
						 }
						 if(null != noOfchild && !("").equals(noOfchild))
						 {
							 if(null != setAddonDtoforCoverId.getNoOfchildAgeLess18())
							 {
							 noOfchild.setReadOnly(Boolean.FALSE);	 
							 noOfchild.setValue(String.valueOf(setAddonDtoforCoverId.getNoOfchildAgeLess18()));
							 noOfchild.setReadOnly(Boolean.TRUE);
							 }						 
						 else
						 {
							 noOfchild.setValue(null);
						 }
						 }
						 						 
						 /*if(null != allowableChild && !("").equals(allowableChild) )
						 {*/
							 if(allowableChild!=null)
							 {
							 allowableChild.setReadOnly(Boolean.FALSE);
							 if(setAddonDtoforCoverId.getAllowableChildren()!=null){
								 allowableChild.setValue(String.valueOf(setAddonDtoforCoverId.getAllowableChildren()));
							 }else{
								 allowableChild.setValue("");
							 }
							 allowableChild.setNullRepresentation("");
							 allowableChild.setReadOnly(Boolean.TRUE);
						 }
						/* else
						 {
							 allowableChild.setValue(null);
						 }
						 }*/
						 if(null != eligibleAmount && !("").equals(eligibleAmount))
						 {
							 if(null != setAddonDtoforCoverId.getEligibleAmount()){
							 eligibleAmount.setReadOnly(Boolean.FALSE);	 
							 eligibleAmount.setValue(String.valueOf(setAddonDtoforCoverId.getEligibleAmount()));
							 eligibleAmount.setReadOnly(Boolean.TRUE);
						 }
						 else
						 {
							 eligibleAmount.setReadOnly(Boolean.FALSE);
							 eligibleAmount.setValue(null);
							 eligibleAmount.setReadOnly(Boolean.TRUE);
						 }
						 }
						 if(null != billAmount && !("").equals(billAmount))
						 {
							 if(null != setAddonDtoforCoverId.getBillAmount())
							 {
							billAmount.setValue(String.valueOf(setAddonDtoforCoverId.getBillAmount()));
						 }
						 else
						 {
							 billAmount.setValue(null); 
						 }
						 }
						 if(null != deductionAmount && !("").equals(deductionAmount))
						 {
							 if(null != setAddonDtoforCoverId.getDeduction())
							 {
							 deductionAmount.setValue(String.valueOf(setAddonDtoforCoverId.getDeduction()));
						 }
						 else
						 {
							 deductionAmount.setValue(null);
						 }
						 }
						 if(null != netamountText && !("").equals(netamountText))
						 {
							 if(null != setAddonDtoforCoverId.getNetamount())
							 {
							 netamountText.setReadOnly(Boolean.FALSE);
							 netamountText.setValue(String.valueOf(setAddonDtoforCoverId.getNetamount()));
							 netamountText.setReadOnly(Boolean.TRUE);
						 }
						 else
						 {	 
							 netamountText.setReadOnly(Boolean.FALSE);
							 netamountText.setValue(null);
							 netamountText.setReadOnly(Boolean.TRUE);
						 }
					 }
						 }
			}
		}
	}
	
	 public List<AddOnCoversTableDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<AddOnCoversTableDTO> itemIds = (List<AddOnCoversTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

	 
	 
	 
	 
	 private void calculateNetAmount(TextField field)
		{
		 AddOnCoversTableDTO dto = (AddOnCoversTableDTO) field.getData();
			if(null != dto)
			{
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
				// TextField billAmtFld = (TextField) hashMap.get("appAmt");
				// TextField deductionsFld = (TextField) hashMap.get("deduction");
				 if(null != hashMap)
				 {   
					 try {
						 TextField approvedAmt = (TextField) hashMap.get("approvedAmount");
						 TextField noOfchild = (TextField) hashMap.get("noOfchildAgeLess18");
						 TextField allowableChild = (TextField) hashMap.get("allowableChildren");
						 TextField eligibleAmount = (TextField) hashMap.get("eligibleAmount");
						 TextField billAmount = (TextField) hashMap.get("billAmount");
						 TextField deductionAmount = (TextField) hashMap.get("deduction");
						 GComboBox addOncover = (GComboBox) hashMap.get("addonCovers");
						 TextField netamountText = (TextField) hashMap.get("netamount");
						 TextField siLimitText = (TextField) hashMap.get("siLimit");
						 Double netAmt = 0d ;
						 Double noOfchildDouble = 0d;
						 Double allowableChildDouble = 0d;
						 Double billAmt = 0d;
						 Double deductionAmt =0d;
						 Double eligibleDoubleAmt1 =0d;
						 Double siLimitDoubleAmt =0d;
						 NumberFormat format = NumberFormat.getInstance(Locale.US);
						 
						 if(addOncover.getValue().toString().equals("EDUCATIONAL GRANT")){
							 if("ACC-PRD-004".equals(dto.getProductCode()) || "ACC-PRD-003".equals(dto.getProductCode())){
								 if(eligibleAmount!=null){
									 Number eligibleAmt = format.parse(eligibleAmount.getValue());
									 double eligibleDoubleAmt = eligibleAmt.doubleValue();
									 netAmt = eligibleDoubleAmt;
									 noOfchild.setReadOnly(Boolean.TRUE);
								 }
							 }else if(noOfchild!=null && noOfchild.getValue()!=null){
								 Number number = format.parse(noOfchild.getValue()); 
								 noOfchildDouble = number.doubleValue();
								 if(allowableChild!=null && null != allowableChild.getValue())
								 {
									 Number allowableChildrennum = format.parse(allowableChild.getValue());
									 allowableChildDouble = allowableChildrennum.doubleValue();
									 double min = Math.min(noOfchildDouble, allowableChildDouble);
									 if(eligibleAmount!=null && null != eligibleAmount.getValue())
									 {
										 Number eligibleAmt = format.parse(eligibleAmount.getValue());
										 double eligibleDoubleAmt = eligibleAmt.doubleValue();
										 netAmt = eligibleDoubleAmt * min;
									 }
								 }
							 }}
						 	else{
							 if(billAmount!=null && billAmount.getValue()!=null){
								 Number number = format.parse(billAmount.getValue());
								 billAmt = number.doubleValue();
							 }
							 if(deductionAmount!=null && deductionAmount.getValue()!=null){
								 Number number = format.parse(deductionAmount.getValue());
								 deductionAmt = number.doubleValue();
							 }
							 
							 if(deductionAmt > billAmt)
							 {
								 showDeleteRowsPopup("Deduction Amount cannot be greater than bill amount");
								 deductionAmount.setValue(null);
							 }
							 else
							 {
								 Double netAmtVal = billAmt - deductionAmt;
								 if(netamountText!=null){
									 netamountText.setReadOnly(Boolean.FALSE);
									 netamountText.setValue(netAmtVal.toString());
									 netamountText.setReadOnly(Boolean.TRUE);
								 }
								 if(eligibleAmount!=null){
									 if(null != eligibleAmount && null != eligibleAmount.getValue())
									 {
										 Number eligibleAmt = format.parse(eligibleAmount.getValue());
										 eligibleDoubleAmt1 = eligibleAmt.doubleValue();
									 }
								 }
								 /**
								  * If SI limit text is null, then it means that for
								  * that particular cover SI limit is not applicable.
								  * In such scenarios, while considering min , silimt
								  * shouldn't be considered. Hence below if else condition
								  * is added. Added for ticket GALAXYMAIN-5640.
								  * 
								  * */
								 if(siLimitText!=null && null != siLimitText.getValue() && !(addOncover.getValue().toString().equals("TRANSPORTATION EXPENSES OF MORTAL REMAINS"))){
									 
									 Number siLimitAmt = format.parse(siLimitText.getValue());
									 siLimitDoubleAmt = siLimitAmt.doubleValue();
									 netAmt= Math.min(netAmtVal, Math.min(eligibleDoubleAmt1,siLimitDoubleAmt));
								 }
								 else
								 {
									 netAmt= Math.min(netAmtVal, (eligibleDoubleAmt1));
								 }
								 
								 
							 }
						 }
						 
						 
						 if(null != approvedAmt){
							 approvedAmt.setReadOnly(Boolean.FALSE);
							 //approvedAmt.setReadOnly(Boolean.TRUE);
							 approvedAmt.setValue(String.valueOf(netAmt));
							 approvedAmt.setReadOnly(Boolean.TRUE);
						 }
					} catch (ReadOnlyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				     }
				 }

			     /*if(null != billAmtFld && null != billAmtFld.getValue())
				 {
					 Number number = format.parse(billAmtFld.getValue()); 
					 billAmt = number.doubleValue();
				 }*/
				 /*if(null != deductionsFld && null != deductionsFld.getValue())
				 {
					 Number number = format.parse(deductionsFld.getValue());
					 dedAmt = number.doubleValue();
				 }*/
				 //netAmt = billAmt - dedAmt;
				
				 //	netAmtText.setValue(String.valueOf(netAmtFld));}
		}
	 
	 private void showDeleteRowsPopup(String message)
		{

			
			Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			Button cancelButton = new Button("Cancel");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			
			//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
			//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final Window dialog = new Window();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			getUI().getCurrent().addWindow(dialog);
//					dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {					
						dialog.close();
				}
			});
		}
	 
	 public void addOnCoverListener(final GComboBox total, final Long rodKey){
			
			if(null != total)
			{
				
				total.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						
						
						List<AddOnCoverOnLoadDTO> addOnCoversTableListBilling = bean.getPreauthDataExtractionDetails().getAddOnCoverNameList();
						Long addOnCoverKey = null;
						Boolean ischeck = checkValueChange(addOnCoversTableListBilling, component,addOnCoverKey);
						if(!ischeck){
						
							AddOnCoversTableDTO addOnCoversTableDTO = (AddOnCoversTableDTO) component.getData();
						
							if(addOnCoversTableDTO!=null){
							
								SelectValue data2 = addOnCoversTableDTO.getAddonCovers();
								
								if(data2!=null && data2.getId()!=null && addOnCoversTableListBilling!=null && addOnCoversTableListBilling.size()>0){
								fireViewEvent(PABillingReviewPagePresenter.PA_POPULATE_ADD_ON_COVERS, data2.getId(), addOnCoversTableDTO,component);
								}
							}
							
							}else{
								AddOnCoversTableDTO addOnCoversTableDTO = (AddOnCoversTableDTO) component.getData();
								
								if(addOnCoversTableDTO!=null){
								
									SelectValue data2 = addOnCoversTableDTO.getAddonCovers();
									
									if(data2!=null && data2.getId()!=null && addOnCoversTableListBilling!=null && addOnCoversTableListBilling.size()>0){
									fireViewEvent(PABillingReviewPagePresenter.PA_PRESENT_POPULATE_ADD_ON_COVERS, data2.getId(), addOnCoversTableDTO,component);
									}
								}	
								}
							}

					private Boolean checkValueChange(List<AddOnCoverOnLoadDTO> addOnCoversTableListBilling,ComboBox component, Long addOnCoverKey) {
						
						Boolean isavailable = Boolean.FALSE;
						for (AddOnCoverOnLoadDTO addOnCoversTableDTO : addOnCoversTableListBilling) {
							if(addOnCoversTableDTO!=null && component.getValue()!=null){
								String currentCover = component.getValue().toString();
								//HashMap<String, AbstractField<?>> hashMap = tableItem.get(addOnCoversTableDTO);
								//GComboBox addOncover = (GComboBox) hashMap.get("addonCovers");
								if(addOnCoversTableDTO!=null && addOnCoversTableDTO.getCoverName()!=null && currentCover.equals(addOnCoversTableDTO.getCoverName())){
									 isavailable =Boolean.TRUE;
									 return isavailable;
								}/*else{
									isavailable =Boolean.TRUE;
									addOnCoverKey = addOnCoversTableDTO.getAdditionalCoverKey();
								}*/
							}
					}
						return isavailable;
					}

				});}
		}
				
			/*total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					List<AddOnCoversTableDTO> addOnCoversTableListBilling = bean.getPreauthDataExtractionDetails().getAddOnCoversTableListBilling();
					
					for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversTableListBilling) {
							if(addOnCoversTableDTO.getAddonCovers()!=null){
								
								if(addOnCoversTableDTO.getAddonCovers().getValue()!=null && !total.getValue().equals(addOnCoversTableDTO.getAddonCovers().getValue())){
									System.out.println(total +"     "+   rodKey);
								}
							}
					}
					*/
					
					
					//PASearchProcessClaimBillingService paSearchProcessClaimBillingService = new PASearchProcessClaimBillingService(); 
					//paSearchProcessClaimBillingService.getAddOnCoverListByRodKey(rodKey, productKey);
/*					AddOnCoversTableDTO addOnCoversTableDTO = new AddOnCoversTableDTO();
					DBCalculationService dbCalculationService = new DBCalculationService();
					List<AddOnCoversTableDTO> addOnCoverList = dbCalculationService.getCoverValues(rodKey);
					
					for (AddOnCoversTableDTO addOnCoversTableDTO2 : addOnCoverList) {
						if(total.getValue().equals(addOnCoversTableDTO2.getAddonCovers().getValue()))
						{
							addOnCoversTableDTO.setRodId(rodKey);
							addOnCoversTableDTO.setEligibleAmount(addOnCoversTableDTO2.getEligibleAmount());
							addOnCoversTableDTO.setSiLimit(addOnCoversTableDTO2.getSiLimit());
						}
					}
*/					//calculateTotalAmount(total);
					

	 
	 public void addNetAmountListener(final TextField total){
			
			if(null != total)
			{
				
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					calculateNetAmount(total);
					calculateTotal();
					//calculateTotalAmount(total);
					
				}
			});
			}
		}
	 
		
		private void calculateTotal()
		{
			List<AddOnCoversTableDTO> tableList = (List<AddOnCoversTableDTO>) table.getItemIds();
			Double approvedAmt = 0d;
			Double billAmount = 0d;
			Double deductionAmount = 0d;
			Double netAmount = 0d;
			Double siAmount = 0d;
			Double eligibleAmount = 0d;
			if(null != tableList && !tableList.isEmpty())
			{
				for (AddOnCoversTableDTO tableBenefitsDTO : tableList) {
					if(tableBenefitsDTO.getApprovedAmount()!=null){
						approvedAmt += tableBenefitsDTO.getApprovedAmount();
					}
					if(tableBenefitsDTO.getBillAmount()!=null){
						billAmount += tableBenefitsDTO.getBillAmount();
					}
					if(tableBenefitsDTO.getDeduction()!=null){
						deductionAmount += tableBenefitsDTO.getDeduction();
					}
					if(tableBenefitsDTO.getNetamount()!=null){
						netAmount +=tableBenefitsDTO.getNetamount();
					}
					if(tableBenefitsDTO.getSiLimit()!=null){
						siAmount += tableBenefitsDTO.getSiLimit();
					}
					if(tableBenefitsDTO.getEligibleAmount()!=null){
						eligibleAmount += tableBenefitsDTO.getEligibleAmount();
					}
				}
				table.setColumnFooter("approvedAmount", String.valueOf(new BigDecimal(approvedAmt)));
				table.setColumnFooter("billAmount", String.valueOf(new BigDecimal(billAmount)));
				table.setColumnFooter("deduction", String.valueOf(new BigDecimal(deductionAmount)));
				table.setColumnFooter("netamount", String.valueOf(new BigDecimal(netAmount)));
				table.setColumnFooter("siLimit", String.valueOf(new BigDecimal(siAmount)));
				table.setColumnFooter("eligibleAmount", String.valueOf(new BigDecimal(eligibleAmount)));
				netAmtText.setValue(String.valueOf(new BigDecimal(approvedAmt)));
			}
		}
		
		 @SuppressWarnings("unused")
			private void addCoversListener(
					final ComboBox categoryCombo) {
				if (categoryCombo != null) {
					categoryCombo.addListener(new Listener() {
						private static final long serialVersionUID = -4865225814973226596L;

						@Override
						public void componentEvent(Event event) {
							Boolean isError = false;
							ComboBox component = (ComboBox) event.getComponent();
							AddOnCoversTableDTO coversDTO = (AddOnCoversTableDTO) component.getData();
							if(null != component)
							{
							if(null != coversDTO)
							{							
								if(!isError)
								{
								 HashMap<String, AbstractField<?>> hashMap = tableItem.get(coversDTO);
								 if(null != hashMap)
								 {
									 SelectValue coversCombo = (SelectValue)component.getValue();
									 TextField eligibleForPolicyFld = (TextField) hashMap.get("eligibleForPolicy");									
									 if(null != coversCombo && null != coversCombo.getId()){
									 List<com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO> addOnCoversList = (List<com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO>) referenceData.get("addOnCoverProc");
									 
									 if(null != addOnCoversList && !addOnCoversList.isEmpty())
										{
											for (com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversList) {
												if(null != addOnCoversTableDTO && null != addOnCoversTableDTO.getCoverId() && coversCombo.getId().equals(addOnCoversTableDTO.getCoverId()))
												{
													if(null != eligibleForPolicyFld)
													{														
														eligibleForPolicyFld.setValue(addOnCoversTableDTO.getEligibleForPolicy());
													}
													/**
													 * Added for issue no 5382 in PA.
													 * */
													}
												}
											}
									 	}
						
								 	}
								}
							}	
							}
						}

					});
				}

			}

		 public void setupReferences(Map<String, Object> referenceData) {
				this.referenceData = referenceData;
			}
}
