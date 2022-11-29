package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

public class MedicalDecisionFVRGradingTable extends GEditableTable<FVRGradingDTO> {
	private static final long serialVersionUID = 5375199119863277119L;
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber",
		"category", "applicability", "status" };*/
/*	public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"category", "applicability", "status" };*/
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {"slNo",
		"category", "applicability", "status" };*/
	
	public MedicalDecisionFVRGradingTable() {
		super(MedicalDecisionFVRGradingTable.class);
		setUp();
	}

	private List<FVRGradingDTO> fvrGradingDTO = new ArrayList<FVRGradingDTO>();
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {

		fieldMap.put("serialNumber", new TableFieldDTO("serialNumber",
			TextField.class, String.class, false));
		fieldMap.put("slNo", new TableFieldDTO("slNo",
				TextField.class, Integer.class, false));
		fieldMap.put("category", new TableFieldDTO("category",
				TextField.class, String.class, false));
		fieldMap.put("applicability", new TableFieldDTO("applicability", TextField.class,
			String.class, false, 100));
		fieldMap.put("status", new TableFieldDTO("status", ComboBox.class,
				SelectValue.class, true, 100));
}*/


	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {		
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
			fieldMap.put("category", new TableFieldDTO("category",
					TextField.class, String.class, false));
			fieldMap.put("applicability", new TableFieldDTO("applicability", TextField.class,
				String.class, false, 100));
			fieldMap.put("status", new TableFieldDTO("status", ComboBox.class,
					SelectValue.class, true, 100));
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
		BeanItemContainer<FVRGradingDTO> beanItemContainer = new BeanItemContainer<FVRGradingDTO>(
				FVRGradingDTO.class);
		beanItemContainer.addAll(this.fvrGradingDTO);
		
		table.setContainerDataSource(beanItemContainer);
		table.setContainerDataSource(beanItemContainer);
		Object[] VISIBLE_COLUMNS = new Object[] {
			"category", "applicability", "status" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setPageLength(table.getItemIds().size());
		table.setWidth("100%");
	}

	@Override
	public void tableSelectHandler(FVRGradingDTO t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String textBundlePrefixString() {
		return "medical-approval-fvr-grading-";
	}

	public void initTableDTO(List<FVRGradingDTO> dto) {
		int i = 1;//For serial NO
		List<FVRGradingDTO> dtoList = new ArrayList<FVRGradingDTO>();
		for(FVRGradingDTO fvrGradingDTO:dto){
			fvrGradingDTO.setSerialNumber(i);
			dtoList.add(fvrGradingDTO);
			i++;
		}
		this.fvrGradingDTO = dtoList;
	}
	
	

}
