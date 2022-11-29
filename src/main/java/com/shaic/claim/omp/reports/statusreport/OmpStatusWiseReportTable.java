package com.shaic.claim.omp.reports.statusreport;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.claim.omp.reports.outstandingreport.OmpStatusReportDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class OmpStatusWiseReportTable extends GBaseTable<OmpStatusReportDto>{
	
	
	private static final Object[] REG_COLUMN_HEADER = new Object[] {
		"sno","intimationNo", "tpaIntimationNumber", "policyNo", "policyFromDateValue", "policyToDateValue", "branchOfficeCode", 
	"policyIssuingState", "channelType", "productType", "plan", "remarks", "policySumInsured",	"eventCodeSumInsured", "insuredName", "age",
	"eventCode", "typeofClaim", "natureOfClaim", "ailmentLoss",	"intimationDateValue", "admissionLossDateValue", "country",
	"initialProvisionAmount", "inrConversionRate", "inrValue", "regDate", "claimStatus"};
	
	private static final Object[] REJ_COLUMN_HEADER = new Object[] {
		"sno","intimationNo", "tpaIntimationNumber", "policyNo", "policyFromDateValue", "policyToDateValue", "branchOfficeCode", 
	"policyIssuingState", "channelType", "productType", "plan", "remarks", "policySumInsured",	"eventCodeSumInsured", "insuredName", "age",
	"eventCode", "typeofClaim", "natureOfClaim", "ailmentLoss",	"intimationDateValue", "admissionLossDateValue", "country",
	"initialProvisionAmount", "inrConversionRate", "inrValue", "rejectionDate", "classification", "category", "rodClaimType",
	"finalApprovedAmountINR", "finalApprovedAmountinDoll", "claimStatus"};

	private static final Object[] CLOSE_COLUMN_HEADER = new Object[] {
		"sno","intimationNo", "tpaIntimationNumber", "policyNo", "policyFromDateValue", "policyToDateValue", "branchOfficeCode", 
	"policyIssuingState", "channelType", "productType", "plan", "remarks", "policySumInsured",	"eventCodeSumInsured", "insuredName", "age",
	"eventCode", "typeofClaim", "natureOfClaim", "ailmentLoss",	"intimationDateValue", "admissionLossDateValue", "country",
	"initialProvisionAmount", "inrConversionRate", "inrValue", "closureDateValue", "claimStatus"};
		
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<OmpStatusReportDto>(OmpStatusReportDto.class));
			table.setVisibleColumns(new Object[] {
							"sno","intimationNo", "tpaIntimationNumber", "policyNo", "policyFromDateValue", "policyToDateValue", "branchOfficeCode", 
							"policyIssuingState", "channelType", "productType", "plan", "remarks", "policySumInsured",	"eventCodeSumInsured", "insuredName", "age",
							"eventCode", "typeofClaim", "natureOfClaim", "ailmentLoss",	"intimationDateValue", "admissionLossDateValue", "country",
							"initialProvisionAmount", "inrConversionRate", "inrValue","rodNumber", "classification", "category", "rodClaimType", 
							"documentReceivedFrom","currencyType", "currencyRate", "conversionValue", "billAmountinFC", "amountInDoll", "deductionInDoll",
							"totalAmountInDoll", "negotiationDone","agreedAmountInDoll", "totalExpenseInDoll",	"finalApprovedAmountINR",
							"finalApprovedAmountinDoll", "nameOfNegotiator", "negotiationFee", "payeeName", "dateOfPaymentValue", "bankCharges",
							"payeeCurrency", "paymentType",	"chequeNo", "chequeDtValue", "bankCode", "bankName", "accountsApprovalDtValue", "claimStatus"
					});

			table.setHeight("250px");
			
	}

	public void setOmpRegColHeader(){
		table.setVisibleColumns(REG_COLUMN_HEADER);
	}
	
	public void setOmpRejColHeader(){
		table.setVisibleColumns(REJ_COLUMN_HEADER);
	}
	
	public void setOmpCloseColHeader(){
		table.setVisibleColumns(CLOSE_COLUMN_HEADER);
	}
	
	@Override
	public void tableSelectHandler(OmpStatusReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "omp-status-report-";
	}
	
}
