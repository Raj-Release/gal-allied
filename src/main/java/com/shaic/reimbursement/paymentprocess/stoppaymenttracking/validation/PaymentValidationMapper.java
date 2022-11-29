package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.bean.StopPaymentRequest;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request.StopPaymentRequestDto;

public class PaymentValidationMapper {
	
	static PaymentValidationMapper myObj;
	
	private static MapperFacade tableMapper;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	private static ClassMapBuilder<StopPaymentRequest, StopPaymentRequestDto> classMapForPaymentValidationReport = null;
	
	 public static void getAllMapValues()   {
		 
		 classMapForPaymentValidationReport = mapperFactory.classMap(StopPaymentRequest.class, StopPaymentRequestDto.class);
		
		 classMapForPaymentValidationReport.field("key","stopPaymentKey");
		 classMapForPaymentValidationReport.field("intimationNo","intimationNo");
		 classMapForPaymentValidationReport.field("utrNumber","utrNumber");
		 classMapForPaymentValidationReport.field("rodNo", "rodNumber");
		 classMapForPaymentValidationReport.field("reasonStopPaymnt.key", "reasonForStopPaymentKey");
		 classMapForPaymentValidationReport.field("reasonStopPaymnt.value", "reasonForStopPaymentValue");
		 classMapForPaymentValidationReport.field("stopPaymentReamrks", "stopPaymentReqRemarks");
		 classMapForPaymentValidationReport.field("paymentMode", "reIssuingPaymentMode");
		 classMapForPaymentValidationReport.field("createdBy", "requestBy");
		 classMapForPaymentValidationReport.field("createdDate", "requestedDate");
		 
		 classMapForPaymentValidationReport.register();
		 tableMapper = mapperFactory.getMapperFacade();
		
	}
	
	public static List<StopPaymentRequestDto> getpaymentValidationTableObjects(List<StopPaymentRequest> claimPaymentList)
    {
	List<StopPaymentRequestDto> paymentValidationList = tableMapper.mapAsList(claimPaymentList, StopPaymentRequestDto.class);
	return paymentValidationList;
    }
	
	
	public static PaymentValidationMapper getInstance(){
        if(myObj == null){
            myObj = new PaymentValidationMapper();
            getAllMapValues();
        }
        return myObj;
	 }
	

	



}
