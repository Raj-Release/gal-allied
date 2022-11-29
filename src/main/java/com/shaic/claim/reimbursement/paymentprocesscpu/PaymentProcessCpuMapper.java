package com.shaic.claim.reimbursement.paymentprocesscpu;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.ClaimPayment;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PaymentProcessCpuMapper {
	
	
	static PaymentProcessCpuMapper myObj;
	
	private static MapperFacade tableMapper;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	//private static ClassMapBuilder<ClaimPayment, PaymentProcessCpuTableDTO> classMapForPaymentProcessCpuReport = mapperFactory.classMap(ClaimPayment.class, PaymentProcessCpuTableDTO.class);
	private static ClassMapBuilder<ClaimPayment, PaymentProcessCpuTableDTO> classMapForPaymentProcessCpuReport = null;
	
	
	
	 public static void getAllMapValues()   {
		 
		classMapForPaymentProcessCpuReport = mapperFactory.classMap(ClaimPayment.class, PaymentProcessCpuTableDTO.class);
		
		classMapForPaymentProcessCpuReport.field("key","claimPaymentKey");
		classMapForPaymentProcessCpuReport.field("intimationNumber","intimationNo");
		classMapForPaymentProcessCpuReport.field("claimNumber","claimNumber");
		classMapForPaymentProcessCpuReport.field("lotNumber","cpuLotNo");
		classMapForPaymentProcessCpuReport.field("approvedAmount","amount");
		classMapForPaymentProcessCpuReport.field("chequeDDNumber","chequeNo");
		classMapForPaymentProcessCpuReport.field("chequeDDDate","chequeDate");
		classMapForPaymentProcessCpuReport.field("bankName","bankName");
		classMapForPaymentProcessCpuReport.field("statusId.processValue","status");
		classMapForPaymentProcessCpuReport.field("createdBy","createdBy");
		classMapForPaymentProcessCpuReport.field("cpuCode","cpuCode");
		classMapForPaymentProcessCpuReport.field("letterPrintingMode","letterFlag");
		classMapForPaymentProcessCpuReport.field("documentReceivedFrom","docReceivedFrom");
		classMapForPaymentProcessCpuReport.field("tdsAmount","tdsAmount");
		classMapForPaymentProcessCpuReport.field("rodNumber","rodNo");
		classMapForPaymentProcessCpuReport.field("paymentDate","paymentDate");
		classMapForPaymentProcessCpuReport.field("paymentType","paymentType");
		classMapForPaymentProcessCpuReport.field("netAmount","netAmount");
		classMapForPaymentProcessCpuReport.field("claimType", "claimType");
		classMapForPaymentProcessCpuReport.field("accountNumber", "accountNumber");
		classMapForPaymentProcessCpuReport.field("ifscCode", "ifscCode");
		classMapForPaymentProcessCpuReport.field("branchName","branchName");
		classMapForPaymentProcessCpuReport.field("interestAmount","interestAmount");  
		classMapForPaymentProcessCpuReport.field("payeeName","payeeName"); 
		classMapForPaymentProcessCpuReport.field("faApprovalDate","faApprovalDate"); 
		classMapForPaymentProcessCpuReport.field("rodkey", "rodKey");
		classMapForPaymentProcessCpuReport.field("modifiedDate","modifiedDate");
		classMapForPaymentProcessCpuReport.field("statusId.portalStatus", "portalStatusVal");
		classMapForPaymentProcessCpuReport.field("statusId.websiteStatus", "websiteStatusVal");
		classMapForPaymentProcessCpuReport.field("policyNumber", "policyNumber");
		classMapForPaymentProcessCpuReport.field("statusId.key", "statusId");
		classMapForPaymentProcessCpuReport.field("uprId", "bancsUprId");
		classMapForPaymentProcessCpuReport.register();
		tableMapper = mapperFactory.getMapperFacade();
		
	}
	
	public static List<PaymentProcessCpuTableDTO> getpaymentCpuTableObjects(List<ClaimPayment> claimPaymentList)
    {
	List<PaymentProcessCpuTableDTO> paymentCpuTableObjectList = tableMapper.mapAsList(claimPaymentList, PaymentProcessCpuTableDTO.class);
	return paymentCpuTableObjectList;
    }
	
	
	public static PaymentProcessCpuMapper getInstance(){
        if(myObj == null){
            myObj = new PaymentProcessCpuMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	
}