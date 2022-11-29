package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewProcedureTable extends GBaseTable<ProcedureDTO> {
	
	private static final long serialVersionUID = 1L;
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"procedureNameValue", "procedureCodeValue", "packageAmount",
		"policyAging", "procedureStatus",
		"exclusionDetails", "remarks"
	"procedureNameValue", "procedureCodeValue", "packageRate",
	"policyAging", "procedureStatus",
	"exclusionDetails","remarks"
};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ProcedureDTO>(
				ProcedureDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			/*"procedureNameValue", "procedureCodeValue", "packageAmount",
			"policyAging", "procedureStatus",
			"exclusionDetails", "remarks"*/
		"procedureNameValue", "procedureCodeValue", "packageRate",
		"policyAging", "procedureStatus",
		"exclusionDetails","remarks"
	};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
	}

	@Override
	public void tableSelectHandler(ProcedureDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "preauth-procedure-";
	}

}
