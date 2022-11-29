package com.shaic.claim.enhancements.preauth.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Default;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GListenerTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;
@Default
public class MedicalDecisionTableForEnhancement extends GListenerTable<MedicalDecisionTableDTO> implements TableCellSelectionHandler {


	private static final long serialVersionUID = 1L;

	public TextField dummyField;
	
	private static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
	public Object[] VISIBLE_COLUMNS = new Object[] {
		"referenceNo", "treatmentType", "procedureOrDiagnosis","description", "pedOrExclusionDetails", "currentSubLimitAmount","sumInsuredRestriction", "subLimitUtilizedAmount", "availableSublimit", "packageAmount"};
	

	public Object[] APPROVE_VISIBLE_COLUMNS = new Object[] {
			"referenceNo", "treatmentType","procedureOrDiagnosis","description", "pedOrExclusionDetails", "currentSubLimitAmount","sumInsuredRestriction", "subLimitUtilizedAmount", "availableSublimit", "packageAmount", "approvedAmount", "addedAmount", "cumulativeAmt", "totalApprovedAmt"};
	
	public MedicalDecisionTableForEnhancement() {
		super(MedicalDecisionTableForEnhancement.class);
		setUp();
	}

	{
		fieldMap.put("referenceNo", new TableFieldDTO("referenceNo", TextField.class , String.class, false));
		fieldMap.put("treatmentType", new TableFieldDTO("treatmentType", TextField.class,String.class, false));
		fieldMap.put("procedureOrDiagnosis", new TableFieldDTO("procedureOrDiagnosis", TextField.class, String.class, false));
		fieldMap.put("description", new TableFieldDTO("description", TextField.class, String.class, false ));
		fieldMap.put("pedOrExclusionDetails", new TableFieldDTO("pedOrExclusionDetails", TextField.class, String.class, false));
		fieldMap.put("currentSubLimitAmount", new TableFieldDTO("currentSubLimitAmount", TextField.class, String.class, false));
		fieldMap.put("sumInsuredRestriction", new TableFieldDTO("sumInsuredRestriction", TextField.class, String.class, false));
		fieldMap.put("subLimitUtilizedAmount", new TableFieldDTO("subLimitUtilizedAmount", TextField.class, String.class, false));
		fieldMap.put("availableSublimit", new TableFieldDTO("availableSublimit", TextField.class, String.class, false));
		fieldMap.put("packageAmount", new TableFieldDTO("packageAmount", TextField.class, String.class, false));
		fieldMap.put("approvedAmount", new TableFieldDTO("approvedAmount", TextField.class, String.class, true));
		fieldMap.put("addedAmount", new TableFieldDTO("addedAmount", TextField.class, String.class, true));
		fieldMap.put("cumulativeAmt", new TableFieldDTO("cumulativeAmt", TextField.class, String.class, true));
		fieldMap.put("totalApprovedAmt", new TableFieldDTO("totalApprovedAmt", TextField.class, String.class, false));
	
	}
	
	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		BeanItemContainer<MedicalDecisionTableDTO> beanItemContainer = new BeanItemContainer<MedicalDecisionTableDTO>(MedicalDecisionTableDTO.class);
		table.setContainerDataSource(beanItemContainer);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		//table.setPageLength(table.getItemIds().size() +1);
		table.setFooterVisible(true);
		dummyField = new TextField();
		table.setColumnFooter("referenceNo", "Total");
	}

	@Override
	public void tableSelectHandler(MedicalDecisionTableDTO t) {
		calculateTotal();
	}

	@Override
	public String textBundlePrefixString() {
		return "preauth-medical-decision-";
	}

	public void setVisibleApproveFields(Boolean isApproveVisible) {
		if(isApproveVisible) {
			table.setVisibleColumns(APPROVE_VISIBLE_COLUMNS);
		} else {
			table.setVisibleColumns(VISIBLE_COLUMNS);
		}
	}

	public void calculateTotal() {
		
		List<MedicalDecisionTableDTO> itemIconPropertyId = (List<MedicalDecisionTableDTO>) table.getItemIds();
		Float total = 0.0f;
		Float addedAmt = 0.0f;
		Float totalApprovedAmt = 0.0f;
		Float cumulativeAmount = 0.0f;
		for (MedicalDecisionTableDTO dto : itemIconPropertyId) {
			String approvedAmount = dto.getApprovedAmount();
			String addedAmount = dto.getAddedAmount();
			String cumulativeAmt = dto.getCumulativeAmt();
			String totalAppAmt = dto.getTotalApprovedAmt();
			total += SHAUtils.getFloatFromString(approvedAmount);
			addedAmt += SHAUtils.getFloatFromString(addedAmount);
			cumulativeAmount += SHAUtils.getFloatFromString(cumulativeAmt);
			Integer numb = SHAUtils.getFloatFromString(addedAmount) + SHAUtils.getFloatFromString(cumulativeAmt);
			Integer correctedAmt = SHAUtils.getFloatFromString(approvedAmount) - numb;
			dto.setTotalApprovedAmt(correctedAmt.toString());
			totalApprovedAmt += SHAUtils.getFloatFromString(totalAppAmt);
		}
		table.setColumnFooter("approvedAmount", String.valueOf(total));
		table.setColumnFooter("addedAmount", String.valueOf(addedAmt));
		table.setColumnFooter("cumulativeAmt", String.valueOf(cumulativeAmount));
		table.setColumnFooter("totalApprovedAmt", String.valueOf(totalApprovedAmt));
		
		dummyField.setValue(String.valueOf(totalApprovedAmt));
	}

	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
			System.out.println("Value changed");
			calculateTotal();
	}

	/*public void calculateApprovedAmt()
	{
		
		TableFieldDTO fldDto = fieldMap.get("approvedAmount");
		//table.getI
		fldDto.setValueChangeListener(getTotalAmount);
		table.setColumnFooter("referenceNo", "Total");
		table.setColumnFooter("approvedAmount", String.valueOf(b));
		
	}*/
	
	public void setReference(Map<String, Object> referenceData) {
		super.setReference(referenceData, this);
	}
	
	public Double getApprovedAmt()
	{
		return Double.valueOf(table.getColumnFooter("approvedAmount"));
	}
	
}
