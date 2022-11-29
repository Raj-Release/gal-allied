package com.shaic.claim.procedureexclusioncheck.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewProcedureExclusionCheckTable extends
		GBaseTable<ViewProcedureExclusionCheckDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"procedure", "procedureCode","procedurePackageRate","dayCareProcedure","considerForDayCare","subLimitApplicable","subLimitName",
			"subLimitDesc","subLimitAmt","considerForPayment","remarks"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewProcedureExclusionCheckDTO>(
				ViewProcedureExclusionCheckDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {"serialNumber",
			"procedure", "newProcedureName", "procedureCode","speciality","procedureAgreedPackageRate","procedurePackageRate","reasonPkgRateChange","dayCareProcedure","considerForDayCare","subLimitApplicable","subLimitName",
			"subLimitDesc","subLimitAmt","considerForPayment","remarks"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 5);
	}

	@Override
	public void tableSelectHandler(ViewProcedureExclusionCheckDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "view-procedure-exclusion-check-";
	}

}
