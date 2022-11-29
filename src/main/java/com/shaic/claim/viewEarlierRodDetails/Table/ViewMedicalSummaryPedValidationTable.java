package com.shaic.claim.viewEarlierRodDetails.Table;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewMedicalSummaryPedValidationTable extends GBaseTable<PedDetailsTableDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"diagnosisName","pedName","policyAgeing","impactOnDiagnosisValue","exclusionDetailsValue"
		,"coPayValue","remarks"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<PedDetailsTableDTO>(
				PedDetailsTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {"diagnosisName","pedName","policyAgeing","impactOnDiagnosisValue","exclusionDetailsValue"
			,"coPayValue","remarks"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
	}

	@Override
	public void tableSelectHandler(PedDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "view-pedvalidation-table-";
	}

}
