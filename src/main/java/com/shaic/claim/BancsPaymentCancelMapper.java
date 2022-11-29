package com.shaic.claim;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import com.shaic.domain.BancsPaymentCancel;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.paymentprocess.paymentreprocess.PaymentReprocessSearchResultDTO;

public class BancsPaymentCancelMapper {

	private MapperFacade mapper;
	
	public BancsPaymentCancelMapper() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<BancsPaymentCancel, PaymentReprocessSearchResultDTO> classMap = mapperFactory.classMap(BancsPaymentCancel.class, PaymentReprocessSearchResultDTO.class);
		
		classMap.field("key","bnksKey");
		classMap.field("intimationNumber","intimationNumber");
		classMap.field("claimNumber", "claimNumber");
		classMap.field("rodNumber","rodNumber");
		classMap.field("policyNumber","policyNumber");
		classMap.field("paymentKey","paymentKey");
		classMap.field("rodKey","rodKey");
		classMap.field("bancsUprId","bancsUprId");
		classMap.field("claimType","claimType");
		classMap.field("paymentType","paymentType");
		classMap.field("paymenetCancelTypeKey","paymenetCancelTypeKey");
		classMap.field("paymentCancelGlxRead","paymentCancelGlxRead");
		classMap.field("paymentCancelBancsRead","paymentCancelBancsRead");
		classMap.field("paymenetCancelRemarks","paymenetCancelRemarks");
					
		classMap.register();
		 
		 
		 this.mapper = mapperFactory.getMapperFacade();
	}
	
	public BancsPaymentCancel getBancsPaymentCancel(PaymentReprocessSearchResultDTO paymentReprocessSearchResultDTO) {
		BancsPaymentCancel dest = mapper.map(paymentReprocessSearchResultDTO, BancsPaymentCancel.class);
		
		return dest;
	}
	
	public PaymentReprocessSearchResultDTO getPaymentReprocessSearchResultDTO(BancsPaymentCancel bancsPaymentCancel) {
		PaymentReprocessSearchResultDTO dest = mapper.map(bancsPaymentCancel, PaymentReprocessSearchResultDTO.class);
		return dest;
	}



}
