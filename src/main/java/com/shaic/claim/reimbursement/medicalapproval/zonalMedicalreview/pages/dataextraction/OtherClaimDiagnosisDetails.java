package com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.dataextraction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDiagnosisDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.TextField;

public class OtherClaimDiagnosisDetails extends GEditableTable<OtherClaimDiagnosisDTO> {

	private static final long serialVersionUID = -3794603381616291170L;
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"hospitalizationDate", "diagnosis" };*/
	
	public OtherClaimDiagnosisDetails() {
		super(OtherClaimDiagnosisDetails.class);
		setUp();
	}


	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

static {
		fieldMap.put("hospitalizationDate", new TableFieldDTO("hospitalizationDate",
				DateField.class, Date.class, true));
		fieldMap.put("diagnosis", new TableFieldDTO("procedureCodeValue",
				TextField.class, String.class, true, 100,true));
		
	}*/


	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
			
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
				fieldMap.put("hospitalizationDate", new TableFieldDTO("hospitalizationDate",
						DateField.class, Date.class, true));
				fieldMap.put("diagnosis", new TableFieldDTO("procedureCodeValue",
						TextField.class, String.class, true, 100,true));
				
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
		table.setContainerDataSource(new BeanItemContainer<OtherClaimDiagnosisDTO>(
				OtherClaimDiagnosisDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"hospitalizationDate", "diagnosis" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setPageLength(table.getItemIds().size());
		
	}

	@Override
	public void tableSelectHandler(OtherClaimDiagnosisDTO t) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String textBundlePrefixString() {
		return "medical-approval-other-claims-";
	}

}
