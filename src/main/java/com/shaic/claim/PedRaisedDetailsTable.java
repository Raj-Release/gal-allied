package com.shaic.claim;

import java.util.List;

import javax.ejb.EJB;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.reimbursement.queryrejection.draftquery.search.DraftQueryLetterDetailTableDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PedRaisedDetailsTable extends GBaseTable<PedRaisedDetailsDto> {

	@EJB
	private PEDQueryService pedQueryService;
	
	private List<PedRaisedDetailsDto> pedRaisedDetailsList;
	

	public void initView(Long policyKey, Long insuredKey){

		if(policyKey != null && insuredKey != null){
			pedRaisedDetailsList =  this.pedQueryService.getPedRaisedList(policyKey, insuredKey);	
		}		
		this.setTableList(pedRaisedDetailsList);			
	}
	
	public void initViewAck(Long policyKey){

		if(policyKey != null){
			pedRaisedDetailsList =  this.pedQueryService.getPedRaisedListForAck(policyKey);	
		}		
		this.setTableList(pedRaisedDetailsList);			
	}
	
	
	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<PedRaisedDetailsDto>(PedRaisedDetailsDto.class));
		Object[] VISIBLE_COLUMNS = new Object[] {
			"serialNumber", "suggType","description","statusValue"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnWidth("serialNumber", 50);
		table.setColumnWidth("suggType", 130);
		table.setColumnWidth("description", 250);
		table.setColumnWidth("statusValue", 150);
		table.setColumnCollapsingAllowed(false);
			
		table.setWidth("100%");
		table.setPageLength(5);
	}
	
	@Override
	public void tableSelectHandler(PedRaisedDetailsDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "ped-raised-details-";
	}

	@Override
	public void removeRow() {
		
	}

}
