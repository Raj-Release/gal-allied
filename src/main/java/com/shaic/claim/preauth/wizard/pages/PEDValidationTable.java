package com.shaic.claim.preauth.wizard.pages;

import java.util.HashMap;
import java.util.Map;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

public class PEDValidationTable extends GEditableTable<DiagnosisDetailsTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6734278135507331627L;

	public PEDValidationTable() {
		super(DiagnosisDetailsTableDTO.class);
		setUp();
	}

/*	public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"diagnosis", "pedName", "policyAgeing",
			"pedExclusionImpactOnDiagnosis", "exclusionDetails", "remarks",
			 };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		fieldMap.put("diagnosis", new TableFieldDTO("diagnosis",
				TextField.class, String.class, false));
		fieldMap.put("pedName", new TableFieldDTO("pedName",
				TextField.class, String.class, false));
		fieldMap.put("policyAgeing", new TableFieldDTO("policyAgeing",
				TextField.class, String.class, false));
		fieldMap.put("pedExclusionImpactOnDiagnosis", new TableFieldDTO("pedExclusionImpactOnDiagnosis",
				ComboBox.class, SelectValue.class, true));
		fieldMap.put("exclusionDetails", new TableFieldDTO("exclusionDetails",
				ComboBox.class, SelectValue.class, true));
		fieldMap.put("remarks", new TableFieldDTO("remarks",
				TextArea.class, String.class, true));
	}*/

	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();	
			fieldMap.put("diagnosis", new TableFieldDTO("diagnosis",
					TextField.class, String.class, false));
			fieldMap.put("pedName", new TableFieldDTO("pedName",
					TextField.class, String.class, false));
			fieldMap.put("policyAgeing", new TableFieldDTO("policyAgeing",
					TextField.class, String.class, false));
			fieldMap.put("pedExclusionImpactOnDiagnosis", new TableFieldDTO("pedExclusionImpactOnDiagnosis",
					ComboBox.class, SelectValue.class, true));
			fieldMap.put("exclusionDetails", new TableFieldDTO("exclusionDetails",
					ComboBox.class, SelectValue.class, true));
			fieldMap.put("remarks", new TableFieldDTO("remarks",
					TextArea.class, String.class, true));
		return fieldMap;
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
	
		table.setContainerDataSource(new BeanItemContainer<DiagnosisDetailsTableDTO>(
				DiagnosisDetailsTableDTO.class));
		table.setWidth("100%");
		Object[] VISIBLE_COLUMNS = new Object[] {
			"diagnosis", "pedName", "policyAgeing",
			"pedExclusionImpactOnDiagnosis", "exclusionDetails", "remarks",
			 };
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}

	@Override
	public void tableSelectHandler(DiagnosisDetailsTableDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "preauth-ped-validation-";
	}

	@Override
	public void deleteRow(Object itemId) {
		// TODO Auto-generated method stub
		
	}

}
