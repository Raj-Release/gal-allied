package com.shaic.claim.reports.investigationassignedreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;
/**
 * Part of CR R0768
 * @author Lakshminarayana
 *
 */
public class InvAssignReportTable extends GBaseTable<InvAssignStatusReportDto>{
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	public void setUploadCompletedHeader(){
		table.setVisibleColumns(new Object[] {
				"sno",	"intimationNumber",	"refRodNo",	"patientName",	"policyNumber",	"clmType",	"cpu",	"hospitalName",
				"hospitalType",	"hosLocation",	"aliment",	"claimedAmount",	"claimStatus",	"saettledAmount",	"rvo",	
				"invApprovedDate",	"draftInvLetterDate",	"clmReqDate",	"invRplyDate",	
				"invTat",	"invrptUploadBy"});
	}
	
	public void setColHeading(){
		table.setColumnHeader("invRplyDate", "Date of Claim verification report received");
		table.setColumnHeader("invTat", "No of days taken for verification");
		table.setColumnHeader("invrptUploadBy", "Investigation Report uploaded by");
	}

	public void setInvAssignedHeader(){
		table.setVisibleColumns(new Object[] {
				"sno",	"intimationNumber",	"refRodNo",	"patientName",	"policyNumber",	"clmType",	"cpu",	"hospitalName",
				"hospitalType",	"hosLocation",	"aliment",	"claimedAmount",	"claimStatus",	"saettledAmount",	"rvo",	
				"invApprovedDate",	"draftInvLetterDate",	"clmReqDate"});
	}
	
	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<InvAssignStatusReportDto>(InvAssignStatusReportDto.class));
			setInvAssignedHeader();
			table.setColumnCollapsingAllowed(false);
			table.setHeight("250px");						
	}

	@Override
	public void tableSelectHandler(InvAssignStatusReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "invsts-report-";
	}
	
}
