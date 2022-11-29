package com.shaic.reimbursement.reassigninvestigation;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.AssignedInvestigationHistory;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.TmpInvestigation;
import com.vaadin.v7.data.util.BeanItemContainer;

public class InvReassignHistoryTable extends GBaseTable<AssignedInvestigationHistory>{

	@EJB
	private InvestigationService investigationService;
		@Override
		public void removeRow() {
			
		}

		public void initView(Long invAssignKey){
			
			List<AssignedInvestigationHistory> reAssignHistoryList = investigationService.getInvAssignementHistoryByAssignedKey(invAssignKey);
			
			setTableList(reAssignHistoryList);
		}
		
		
		@Override
		public void initTable() {
			
			table.setContainerDataSource(new BeanItemContainer<AssignedInvestigationHistory>(AssignedInvestigationHistory.class));
			
			table.setVisibleColumns(new Object[] {
					"sNo", "reassignDateValue", "createdBy", "createdByName","assignedFrom", "investigatorName", "invTelNo", "invMobileNo","reAssignRemarks"});
			
			table.setColumnWidth("sNo",45);
			table.setColumnWidth("reassignDateValue", 180);
			table.setColumnWidth("createdBy", 180);
			table.setColumnWidth("createdbyname", 320);
			table.setColumnWidth("assignedFrom", 320);
			table.setColumnWidth("investigatorName", 320);
			table.setColumnWidth("investigatorTelNo", 158);
			table.setColumnWidth("investigatorMobileNo", 148);
			table.setColumnWidth("reAssignRemarks", 300);
			table.setHeight("180px");
			
		}

		@Override
		public void tableSelectHandler(AssignedInvestigationHistory t) {
			
		}

		@Override
		public String textBundlePrefixString() {
			return "view-inv-reassign-his-";
		}
	
}
