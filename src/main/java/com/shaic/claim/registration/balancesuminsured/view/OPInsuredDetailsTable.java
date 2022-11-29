package com.shaic.claim.registration.balancesuminsured.view;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OPInsuredDetailsTable extends GBaseTable<OPInsuredDetailsTableDTO> {
	private static final long serialVersionUID = 5375199119863277119L;

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
			"serialNumber", "insuredPatientName", "checkupDate",
			"reasonForCheckup" };*/
	/*public static final Object[] VISIBLE_COLUMNS1 = new Object[] {
			"serialNumber", "checkupDate", "reasonForCheckup" };*/

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<OPInsuredDetailsTableDTO>(
				OPInsuredDetailsTableDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {
			"serialNumber", "insuredPatientName", "checkupDate",
			"reasonForCheckup" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setHeight("110px");
		// table.setColumnHeader("insuredPatientName","Insured Patient Name");

	}

	public void individualVisibleColumn() {
		 Object[] VISIBLE_COLUMNS1 = new Object[] {
			"serialNumber", "checkupDate", "reasonForCheckup" };
		table.setVisibleColumns(VISIBLE_COLUMNS1);

	}

	@Override
	public void tableSelectHandler(OPInsuredDetailsTableDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "op-register-claim-";
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

}
