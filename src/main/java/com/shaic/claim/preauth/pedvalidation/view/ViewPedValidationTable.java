package com.shaic.claim.preauth.pedvalidation.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewPedValidationTable extends
		GBaseTable<ViewPedValidationTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"diagnosis", "pedName", "policyAgeing","impactOnDiagnosis","consideredForExclusion","remarks" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewPedValidationTableDTO>(
				ViewPedValidationTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"diagnosis", "pedName", /*"icdChapterValue", "icdBlockValue",*/ "icdCodeValue",/* "policyAgeing",*/"impactOnDiagnosis","consideredForExclusion","remarks" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 5);
	}

	@Override
	public void tableSelectHandler(ViewPedValidationTableDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "ped-validation-";
	}

}
