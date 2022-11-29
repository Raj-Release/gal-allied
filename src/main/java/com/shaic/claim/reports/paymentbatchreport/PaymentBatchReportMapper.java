package com.shaic.claim.reports.paymentbatchreport;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.ClaimPayment;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PaymentBatchReportMapper {

	static PaymentBatchReportMapper myObj;
	
	private static MapperFacade tableMapper;
	
	public static void getAllMapValues() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<ClaimPayment, PaymentBatchReportTableDTO> paymentBatchMapper = mapperFactory.classMap(ClaimPayment.class, PaymentBatchReportTableDTO.class);
		
	
		paymentBatchMapper.field("intimationNumber", "intimationNo");
		paymentBatchMapper.field("claimNumber","claimNo");
		paymentBatchMapper.field("policyNumber","policyNo");		
		paymentBatchMapper.field("productCode","product");
		paymentBatchMapper.field("batchNumber","accountBatchNo"); 
		paymentBatchMapper.field("claimType","typeOfClaim");
		paymentBatchMapper.field("paymentType","paymentType");
		paymentBatchMapper.field("lotNumber","lotNo");	
		paymentBatchMapper.field("approvedAmount","approvedAmt");	
		paymentBatchMapper.field("tdsAmount","tdsAmt");
		paymentBatchMapper.field("netAmount","netAmnt");
		paymentBatchMapper.field("tdsPercentage","tdsPercentage");
		paymentBatchMapper.field("payeeName","payeeName");		
		paymentBatchMapper.field("payableAt","payableAt");
		paymentBatchMapper.field("cpuCode","cpuCode");
		paymentBatchMapper.field("ifscCode","ifscCode");
		paymentBatchMapper.field("accountNumber","beneficiaryAcntNo");
		paymentBatchMapper.field("branchName","branchName");
		paymentBatchMapper.field("panNumber","panNo");
		paymentBatchMapper.field("hospitalCode","providerCode");
		paymentBatchMapper.field("batchCreatedDate","paymentReqDt");
		paymentBatchMapper.field("emailId","emailID");
		paymentBatchMapper.field("pioCode", "pioCode");
		paymentBatchMapper.field("modifiedBy","userID");
		paymentBatchMapper.field("chequeDDNumber", "chequeNo");
		paymentBatchMapper.field("chequeDDDate", "chequeDate");
		paymentBatchMapper.field("lastAckDate", "lastAckDate");
		paymentBatchMapper.field("faApprovalDate", "faApprovedDate");
		paymentBatchMapper.field("interestRate", "penalInterestRate");
	//	paymentBatchMapper.field("interestAmount", "penalInterestAmnt");
		paymentBatchMapper.field("delayDays", "noOfExceedingDays");
		paymentBatchMapper.field("allowedDelayDays", "exceedingIRDATatDays");
		paymentBatchMapper.field("interestAmount", "penalInterestAmnt");
		paymentBatchMapper.field("totalAmount", "penalTotalAmnt");
		paymentBatchMapper.field("intrestRemarks", "penalRemarks");
		
		paymentBatchMapper.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<PaymentBatchReportTableDTO> getListOfPaymentBatchTableDTO(List<ClaimPayment> claimPaymentList)
	{
		List<PaymentBatchReportTableDTO> mapAsList = tableMapper.mapAsList(claimPaymentList, PaymentBatchReportTableDTO.class);
		return mapAsList;
	}
	
	public static PaymentBatchReportMapper getInstance(){
        if(myObj == null){
            myObj = new PaymentBatchReportMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	

}
