package com.shaic.claim.premedical.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Alternative;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
@Alternative
public class OtherBenefitsTable  extends GEditableTable<OtherBenefitsTableDto> implements
TableCellSelectionHandler{
	


	public OtherBenefitsTable() {
		super(OtherBenefitsTableDto.class);
		setUp();
		// TODO Auto-generated constructor stub
	}
	
	TextField dummyTxt;

	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno",
		"benefitName", "applicable", "amtClaimed", "nonPayable", "netPayable", "eligibleAmt", "approvedAmt", "remarks" };*/
	
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	 {
		fieldMap.put("sno", new TableFieldDTO("sno",
				TextField.class, String.class, false, this));
		fieldMap.put("benefitName", new TableFieldDTO("benefitName",
				TextField.class, String.class, false, this));
		fieldMap.put("applicable", new TableFieldDTO("applicable",
				ComboBox.class, SelectValue.class, true, this));
		fieldMap.put("amtClaimed", new TableFieldDTO("amtClaimed",
				TextField.class, String.class, true, this));
		fieldMap.put("nonPayable", new TableFieldDTO("nonPayable",
				TextField.class, String.class, true, getNonpayableCalucationListener()));		
		fieldMap.put("netPayable", new TableFieldDTO("netPayable",
				TextField.class, String.class, false, this));
		fieldMap.put("eligibleAmt", new TableFieldDTO("eligibleAmt",
				TextField.class, String.class, false, this));
		fieldMap.put("approvedAmt", new TableFieldDTO("approvedAmt",
				TextField.class, String.class, false, this));		
		fieldMap.put("remarks", new TableFieldDTO("remarks",
				TextArea.class, String.class, false, this));
	}*/
	
	@Override
	protected void newRowAdded() {
		
	}
	
	public void addTxtValueChangeListener(){
//		needTxtValueChangeListener(this, "nonPayable");
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		 
			fieldMap.put("sno", new TableFieldDTO("sno",
					TextField.class, String.class, false, this));
			fieldMap.put("benefitName", new TableFieldDTO("benefitName",
					TextField.class, String.class, false, this));
			fieldMap.put("applicable", new TableFieldDTO("applicable",
					ComboBox.class, SelectValue.class, true, this));
			fieldMap.put("amtClaimed", new TableFieldDTO("amtClaimed",
					TextField.class, String.class, true, this));
			fieldMap.put("nonPayable", new TableFieldDTO("nonPayable",
					TextField.class, String.class, true, getNonpayableCalucationListener()));		
			fieldMap.put("netPayable", new TableFieldDTO("netPayable",
					TextField.class, String.class, false, this));
			fieldMap.put("eligibleAmt", new TableFieldDTO("eligibleAmt",
					TextField.class, String.class, false, this));
			fieldMap.put("approvedAmt", new TableFieldDTO("approvedAmt",
					TextField.class, String.class, false, this));		
			fieldMap.put("remarks", new TableFieldDTO("remarks",
					TextArea.class, String.class, false, this));
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		
	}

	@Override
	public void removeRow() {
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<OtherBenefitsTableDto>(
				OtherBenefitsTableDto.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {"sno",
			"benefitName", "applicable", "amtClaimed", "nonPayable", "netPayable", "eligibleAmt", "approvedAmt", "remarks" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setWidth("100%");
		table.setHeight("125px");
		table.setColumnWidth("benefitName",400);
		table.setColumnWidth("remarks", 400);
		
		
		
//		
//		TableFieldDTO nonpayableDto = fieldMap.get("nonPayable");
//		
//		if(nonpayableDto.getField() instanceof TextField) {
//			
//			dummyTxt =  (TextField) nonpayableDto.getField();
//			
//			//Vaadin8-setImmediate() dummyTxt.setImmediate(true);
//			
//			dummyTxt.addTextChangeListener(new TextChangeListener() {
//				
//				@Override
//				public void textChange(TextChangeEvent event) {
//				
//					System.out.println("value change event fired.");
//					calculateFinalAmtPayable(getValues());
//				}
//			});
//			
//			dummyTxt.addBlurListener(new FieldEvents.BlurListener() {
//				
//				@Override
//				public void blur(BlurEvent event) {
//					System.out.println("Blur event fired.");
//					calculateFinalAmtPayable(getValues());	
//				}
//			});
//		}
		
	}

	@Override
	public void tableSelectHandler(OtherBenefitsTableDto t) {
		
//		Property nonPayableProperty = table.getItem(t).getItemProperty("nonPayable");
//		((TextField)nonPayableProperty.getValue()).addBlurListener(getNonpayableCalucationListener());
//		
//		
//		
//		TableFieldDTO nonpayableDto = fieldMap.get("nonPayable");
//		
//		if(nonpayableDto.getField() instanceof TextField) {
//			
//			dummyTxt =  (TextField) nonpayableDto.getField();
//			
//			//Vaadin8-setImmediate() dummyTxt.setImmediate(true);
//			
//			dummyTxt.addTextChangeListener(new TextChangeListener() {
//				
//				@Override
//				public void textChange(TextChangeEvent event) {
//				
//					System.out.println("value change event fired.");
//					calculateFinalAmtPayable(getValues());
//				}
//			});
//			
//			dummyTxt.addBlurListener(new FieldEvents.BlurListener() {
//				
//				@Override
//				public void blur(BlurEvent event) {
//					System.out.println("Blur event fired.");
//					calculateFinalAmtPayable(getValues());	
//				}
//			});
//		}
//		
//		calculateFinalPayableAmt(t);
	}

	@Override
	public String textBundlePrefixString() {
		
		return "otherbenefits-table-";
	}
	
	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
		
	}
	
	public void setTableEditable(boolean enable){
		this.table.setEditable(enable);
	}

	public void calculateFinalPayableAmt(OtherBenefitsTableDto selectedDto){
		if(selectedDto.getApplicable().getValue() != null && !selectedDto.getApplicable().getValue().isEmpty() && ("yes").equalsIgnoreCase(selectedDto.getApplicable().getValue())){
			Double netpayable = selectedDto.getAmtClaimed()  != null && selectedDto.getAmtClaimed() > 0d && selectedDto.getNonPayable() != null && selectedDto.getAmtClaimed() > selectedDto.getNonPayable() ? selectedDto.getAmtClaimed() - selectedDto.getNonPayable() : 0d;
			Double approvedAmt = selectedDto.getEligibleAmt() != null && netpayable != null && selectedDto.getEligibleAmt() >= 0d && selectedDto.getEligibleAmt() < netpayable ? selectedDto.getEligibleAmt() : (netpayable >= 0 ? netpayable : 0d);
			
			selectedDto.setNetPayable(netpayable);
			selectedDto.setApprovedAmt(approvedAmt);
		}
	}
	
	public void calculateFinalAmtPayable(List<OtherBenefitsTableDto> dtoList){
		
		if(dtoList != null && !dtoList.isEmpty()){
			for (OtherBenefitsTableDto otherBenefitsTableDto : dtoList) {
				calculateFinalPayableAmt(otherBenefitsTableDto);
			}	
		}
		
	}
	
	public BlurListener getNonpayableCalucationListener(){
		
		BlurListener listener = new BlurListener() {
			
			@Override
			public void blur(BlurEvent event) {
				System.out.println("value change event fired.");
				calculateFinalAmtPayable(getValues());	
			}
		};
		
		return listener;
	}
}
