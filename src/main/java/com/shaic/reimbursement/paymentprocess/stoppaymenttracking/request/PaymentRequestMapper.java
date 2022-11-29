package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuMapper;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.domain.ClaimPayment;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PaymentRequestMapper {


	
	
	static PaymentRequestMapper myObj;
	
	private static MapperFacade tableMapper;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static ClassMapBuilder<ClaimPayment, StopPaymentRequestDto> classMapForPaymentRequestReport = null;
	
	
	
	 public static void getAllMapValues()   {
		 
		 classMapForPaymentRequestReport = mapperFactory.classMap(ClaimPayment.class, StopPaymentRequestDto.class);
		
		 classMapForPaymentRequestReport.field("key","claimPaymentKey");
		 classMapForPaymentRequestReport.field("intimationNumber","intimationNo");
		 classMapForPaymentRequestReport.field("chequeDDNumber","utrNumber");
		 classMapForPaymentRequestReport.field("rodNumber", "rodNumber");
		 
		 classMapForPaymentRequestReport.register();
		 tableMapper = mapperFactory.getMapperFacade();
		
	}
	
	public static List<StopPaymentRequestDto> getpaymentRequestTableObjects(List<ClaimPayment> claimPaymentList)
    {
	List<StopPaymentRequestDto> paymentCpuTableObjectList = tableMapper.mapAsList(claimPaymentList, StopPaymentRequestDto.class);
	return paymentCpuTableObjectList;
    }
	
	
	public static PaymentRequestMapper getInstance(){
        if(myObj == null){
            myObj = new PaymentRequestMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	

	

}
