package com.shaic.claim.viewPedEndorsement;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PedEndorsementTable extends
		GBaseTable<PedEndorsementTableDTO> {
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "sno",
			"intimationNo", "claimNo", "policyNo", "insuredPatientName",
			"lob", "productCode", "productName" };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<PedEndorsementTableDTO>(
				PedEndorsementTableDTO.class));
	 Object[] NATURAL_COL_ORDER = new Object[] { "sno",
			"intimationNo", "claimNo", "policyNo", "insuredPatientName",
			"lob", "productCode", "productName" };
		table.setVisibleColumns(NATURAL_COL_ORDER);

	}

	@Override
	public void tableSelectHandler(PedEndorsementTableDTO t) {
//		fireViewEvent(MenuPresenter.FILE_UPLOAD_TABLE, t);

	}

	@Override
	public String textBundlePrefixString() {
		return "search-processtranslation-";
	}

}
