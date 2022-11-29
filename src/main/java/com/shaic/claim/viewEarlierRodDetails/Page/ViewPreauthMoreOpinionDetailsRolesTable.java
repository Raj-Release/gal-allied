package com.shaic.claim.viewEarlierRodDetails.Page;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.medical.opinion.OpinionValidationTableDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.ViewSectionDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewPreauthMoreOpinionDetailsRolesTable extends GBaseTable<OpinionValidationTableDTO> {

	/**
	 * 
	 */
	Object[] NATURAL_COL_ORDER = new Object[] {
			"assignedRoleBy", "assignedDocName", "updatedRemarks","approveRejectRemarks" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		if(table!=null){
			table.clear();
		}
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		table.setContainerDataSource(new BeanItemContainer<OpinionValidationTableDTO>(
				OpinionValidationTableDTO.class));	
		table.setVisibleColumns(NATURAL_COL_ORDER);
		setSizeFull();
		table.setColumnWidth("updatedRemarks", 300);
		table.setHeight("150px");
		table.setWidth("500px");
		
	}

	@Override
	public void tableSelectHandler(OpinionValidationTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-opinionroles-";
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
	}
}
