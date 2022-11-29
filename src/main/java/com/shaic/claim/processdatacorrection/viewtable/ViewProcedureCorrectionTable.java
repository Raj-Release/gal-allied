package com.shaic.claim.processdatacorrection.viewtable;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.processdatacorrection.dto.ProcedureCorrectionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewProcedureCorrectionTable extends GBaseTable<ProcedureCorrectionDTO>{

	private static final long serialVersionUID = 1L;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		if(table!=null){
			table.clear();
		}
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<ProcedureCorrectionDTO>(ProcedureCorrectionDTO.class));
//		Object[] VISIBLE_COLUMNS = new Object[] {"serialNo","procedureName","proposedProcedureName","procedureCode","proposedProcedureCode","newProcedureName","proposedNewProcedureName","speciality","proposedSpeciality"};
		Object[] VISIBLE_COLUMNS = new Object[] {"serialNo","procedureName","procedureCode","newProcedureName","speciality"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnWidth("serialNo",80);		
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(ProcedureCorrectionDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-procedure-correction-table-";
	}
}
