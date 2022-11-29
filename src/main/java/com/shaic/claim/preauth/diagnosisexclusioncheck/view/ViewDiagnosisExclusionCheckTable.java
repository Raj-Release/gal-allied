package com.shaic.claim.preauth.diagnosisexclusioncheck.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewDiagnosisExclusionCheckTable extends
		GBaseTable<ViewDiagnosisExclusionCheckDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"diagnosis", "policyAgeing", "impactOnDiagnosis", "subLimit",
			"approvedAmt" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewDiagnosisExclusionCheckDTO>(
				ViewDiagnosisExclusionCheckDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {
			"diagnosis", "policyAgeing", "impactOnDiagnosis", "subLimit",
			"approvedAmt" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 5);
	}

	@Override
	public void tableSelectHandler(ViewDiagnosisExclusionCheckDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "view-diagnosis-exclusion-check-";
	}

}
