package com.shaic.claim.omp.registration;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;


public class OMPPreviousClaimDetailTable extends GBaseTable<OMPPreviousClaimTableDTO>{
	
	
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"srno", "policyno", "insuredname", "intimationno","admissiondate","hospitalname","claimtype","eventcode","provAmt","settledAmt","claimStatus", };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<OMPPreviousClaimTableDTO>(
				OMPPreviousClaimTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {
			"srno", "policyno", "insuredname", "intimationno","admissiondate","hospitalname","claimtype","eventcode","provAmt","settledAmt","claimStatus", };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("10%");

	}

	@Override
	public void tableSelectHandler(OMPPreviousClaimTableDTO t) {
		// TODO
		// fireViewEvent(MenuItemBean.SHOW_HOSPITAL_ACKNOWLEDGE_FORM,
		// t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
		return "view-omppreviousclaim-";
	}
}
