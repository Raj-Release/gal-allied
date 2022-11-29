package com.shaic.claim.reports.claimstatusreportnew;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ClaimsStatusReportTable extends GBaseTable<ClaimsStatusReportDto> {
	
	
	private static final Object[] CASHLESS_COLUMN_HEADER = new Object[] {	
		"sno","claimNo","intimationNo","policyNumber","productName","cpuCode","cpuName",
		"userId","userName","patientName","hospitalName","diagnosis",
		"finacialYear","preauthApprovalDate","status","queryReplyRemarks","withDrawRemarks","rejectionRemarks","preauthAmt"	};
	
	private static final Object[] REIMB_PAID_COLUMN_HEADER = new Object[] {	
		"sno","claimNo","intimationNo","policyNumber","productName","cpuCode","offiCode","offiName","diagnosis","hospitalName","hospCity","patientName",
	"paidDate","paidAmout","claimedAmount","deductedAmount","cashlessOrReimbursement","fvrUploaded","medicalApprovalPerson",	
	"billingPerson","financialApprovalPerson","icdCode","claimCoverage","suminsured","patientAge","finacialYear",
	"admissionDate","intimationDate","scheme"	};
	
	private static final Object[] REIMB_REJECT_COLUMN_HEADER = new Object[] {	
		"sno","intimationNo","intimationDate","admissionDate","claimNo","policyNumber","productName","cpuCode","cpuName","userId","userName","patientName","diagnosis","patientAge",
		"finacialYear","hospitalName","rejectDate","closeStage","rejectionRemarks","cashlessOrReimbursement","billReceivedDate",
		"claimedAmount","initialProvisionAmount"};
		
	private static final Object[] REIMB_BILLS_RECVD_COLUMN_HEADER = new Object[] {	
		"sno","intimationNo","policyNumber","productName","patientName","cashlessOrReimbursement","diagnosis","hospitalName",
		"claimedAmount","billReceivedDate","noofTimeBillRec","cpuCode","finacialYear"};
	
	private static final Object[] REIMB_BILLING_COLUMN_HEADER = new Object[] {	
		"sno","intimationNo","policyNumber","productName","diagnosis","finacialYear","billReceivedDate","claimedAmount",
		"deductedAmount","billingApprovedAmount","billingPerson","tat"};
	
	//Suggested addition of fields	"patientName","cashlessOrReimbursement","hospitalName","cpuCode",
	
	private static final Object[] REIMB_MEDICAL_APPROVE_COLUMN_HEADER = new Object[] {	
		"sno","intimationNo","policyNumber","productName","diagnosis","cashlessOrReimbursement","icdCode","claimCoverage","finacialYear",
		"medicalApprovalPerson","intimationDate","maApprovedDate","billReceivedDate","claimedAmount"};
		
		
	private static final Object[] REIMB_QUERY_COLUMN_HEADER = new Object[] {	
		
		"sno","intimationNo","policyNumber","offiCode","offiName","smCode","smName","agentCode","agentName","productName","patientName","cashlessOrReimbursement","diagnosis","hospitalName",
		"hospCity","claimedAmount","billReceivedDate","noofTimeBillRec","cpuCode","cpuName","userId","userName","queryRaisedDate","queryRaisedRemarks","queryReplyDate",
		"queryReplyRemarks","closeDate","currentProvisionAmount","statusValue","docRecvdFrom"};
		
	private static final Object[] CLOSED_CLAIMS_COLUMN_HEADER = new Object[] {	
		"sno","claimNo","intimationNo","intimationDate","policyNumber","admissionDate","productName","patientName","hospitalName","diagnosis",
		"patientAge","finacialYear","closeDate","closedRemarks","userId","userName","cashlessOrReimbursement","closeStage","cpuCode",
		"billReceivedDate","claimedAmount","initialProvisionAmount"};
	
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<ClaimsStatusReportDto>(ClaimsStatusReportDto.class));
			table.setVisibleColumns(REIMB_PAID_COLUMN_HEADER);
			table.setColumnCollapsingAllowed(false);
			table.setColumnWidth("productName",180);
			table.setColumnWidth("admissionDate",95);
			
			table.setHeight("250px");
	}

	public void setCashlessColHeader(){
		table.setVisibleColumns(CASHLESS_COLUMN_HEADER);
		table.setColumnHeader("cpuCode","CPU Code");
		table.setColumnHeader("cpuName","CPU Name");
		table.setColumnHeader("userId","User Id");
		table.setColumnHeader("userName","User Name");
		table.setColumnHeader("preauthApprovalDate","Preauth Approval Date");
		table.setColumnHeader("status","Status");
		table.setColumnHeader("queryReplyRemarks","Remarks");
		table.setColumnHeader("withDrawRemarks","WithDraw Remarks");
		table.setColumnHeader("rejectionRemarks","Rejection Remarks");
		table.setColumnHeader("preauthAmt","Preauth Amount");
	}

	public void setReimbPaidColHeader(){
		table.setVisibleColumns(REIMB_PAID_COLUMN_HEADER);
	}
	
	public void setReimbQueryColHeader(){
		table.setVisibleColumns(REIMB_QUERY_COLUMN_HEADER);
		table.setColumnHeader("billReceivedDate", "Bill Received Date");		
		table.setColumnHeader("offiCode","Branch Code");
		table.setColumnHeader("offiName","Branch Name");
		table.setColumnHeader("smCode","SM Code");
		table.setColumnHeader("smName","SM Name");
		table.setColumnHeader("agentCode","Agent Code");
		table.setColumnHeader("agentName","Agent Name");
		table.setColumnHeader("hospCity","Hospital City");
		table.setColumnHeader("noofTimeBillRec","No of Time Bills Received");
		table.setColumnHeader("cpuCode","CPU Code");
		table.setColumnHeader("cpuName","CPU Name");
		table.setColumnHeader("userId","User Id");
		table.setColumnHeader("userName","User Name");
		table.setColumnHeader("queryRaisedDate","Query Raised Date");
		table.setColumnHeader("queryRaisedRemarks","Query Raised Remarks");
		table.setColumnHeader("queryReplyDate","Query Reply Date");
		table.setColumnHeader("queryReplyRemarks","Query Reply Remarks");
		table.setColumnHeader("closeDate","Close Date");
		table.setColumnHeader("currentProvisionAmount","Current Provision Amount");
		table.setColumnHeader("statusValue","Current Status");
		table.setColumnHeader("docRecvdFrom","Document Received From");
	}
	
	public void setReimbRejectColHeader(){
		table.setVisibleColumns(REIMB_REJECT_COLUMN_HEADER);
		table.setColumnHeader("cpuName","CPU Name");
		table.setColumnHeader("userId","User Id");
		table.setColumnHeader("userName","User Name");
		table.setColumnHeader("billReceivedDate", "Bill Received Date");
		table.setColumnHeader("rejectDate","Rejection Date");
		table.setColumnHeader("closeStage","Close Stage");
		table.setColumnHeader("rejectionRemarks","Remarks");
		table.setColumnHeader("initialProvisionAmount","Initial Provision Amount");
	}
	
	public void setReimbMedicalApprovedColHeader(){
		table.setVisibleColumns(REIMB_MEDICAL_APPROVE_COLUMN_HEADER);
		table.setColumnHeader("billReceivedDate", "Bill Received Date");
	}
	
	public void setReimbBillingCompletedColHeader(){
		table.setVisibleColumns(REIMB_BILLING_COLUMN_HEADER);
		table.setColumnHeader("billReceivedDate", "Bill Received Date");
		table.setColumnHeader("billingApprovedAmount", "Billing Approved Amount");
		table.setColumnHeader("tat", "Tat");
	}
	
	public void setReimbBillsReceicedColHeader(){
		table.setVisibleColumns(REIMB_BILLS_RECVD_COLUMN_HEADER);
		table.setColumnHeader("billReceivedDate", "Bill Received Date");
	}
	
	public void setReimbClosedColHeader(){
		table.setVisibleColumns(CLOSED_CLAIMS_COLUMN_HEADER);
		table.setColumnHeader("billReceivedDate", "Bill Received Date");
	}
	
	@Override
	public void tableSelectHandler(ClaimsStatusReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "claimsts-report-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=4){
			table.setPageLength(5);
		}
		
	}

}