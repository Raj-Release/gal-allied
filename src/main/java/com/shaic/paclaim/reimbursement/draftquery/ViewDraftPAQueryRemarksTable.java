package com.shaic.paclaim.reimbursement.draftquery;

import java.util.List;

import javax.ejb.EJB;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.reimbursement.queryrejection.draftquery.search.DraftQueryLetterDetailTableDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewDraftPAQueryRemarksTable extends GBaseTable<DraftQueryLetterDetailTableDto>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 973301030709347814L;

	@EJB
	private ReimbursementQueryService reimbQueryService;
	
	private List<DraftQueryLetterDetailTableDto> draftLetterRemarksList;
	
	private List<DraftQueryLetterDetailTableDto> reDraftRemarksList;
	
	private String tableCategory;
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"serialNumber", "draftOrRedraftRemarks"};*/

	public void invitView(Long reimbQueryKey,String tableCategory){
//		draftLetterRemarksList.clear();
//		reDraftRemarksList.clear();
		this.tableCategory = tableCategory;
		if(reimbQueryKey != null){
			ReimbursementQueryDto queryDto =  this.reimbQueryService.getReimbursementQuery(reimbQueryKey);	
			draftLetterRemarksList = queryDto.getQueryDarftList();
			reDraftRemarksList = queryDto.getRedraftList();
		}
		
		if(this.tableCategory != null && !this.tableCategory.isEmpty() && this.tableCategory.equalsIgnoreCase(SHAConstants.QUERY_DRAFT_OUT_COME)){
			this.setTableList(this.draftLetterRemarksList);	
		}
		else{
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
