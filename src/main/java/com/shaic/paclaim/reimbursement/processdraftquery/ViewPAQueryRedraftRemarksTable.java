package com.shaic.paclaim.reimbursement.processdraftquery;

import java.util.List;

import javax.ejb.EJB;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.reimbursement.queryrejection.draftquery.search.DraftQueryLetterDetailTableDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewPAQueryRedraftRemarksTable extends GBaseTable<DraftQueryLetterDetailTableDto>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2421928023869291094L;

	@EJB
	private ReimbursementQueryService reimbQueryService;
	
	private List<DraftQueryLetterDetailTableDto> reDraftRemarksList = null;
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"serialNumber", "draftOrRedraftRemarks"};*/

	public void invitView(ReimbursementQueryDto reimbQueryDto){
		if(reimbQueryDto != null){
			reDraftRemarksList = reimbQueryDto.getRedraftList();
		}
		
		if(reDraftRemarksList != null && !reDraftRemarksList.isEmpty()){
			this.setTableList(this.reDraftRemarksList);
		}		
	}
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<DraftQueryLetterDetailTableDto>(DraftQueryLetterDetailTableDto.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"serialNumber", "draftOrRedraftRemarks"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnWidth("draftOrRedraftRemarks",200);
		table.setColumnCollapsingAllowed(false);
		table.setColumnExpandRatio("draftOrRedraftRemarks",80.0f);
			
		table.setWidth("80%");
		table.setPageLength(5);
	}
	
	@Override
	public void tableSelectHandler(DraftQueryLetterDetailTableDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "query-draft-letter-details-";
	}

	@Override
	public void removeRow() {
		
	}


}
