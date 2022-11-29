package com.shaic.claim.reimbursement.paymentprocesscpuview;

import com.shaic.arch.GMVPView;

public interface PaymentProcessCpuPageView extends GMVPView{
	
	void init(PaymentProcessCpuPageDTO bean);
	
	void generateDischargeVoucherLetter(String templateName, PaymentProcessCpuPageDTO paymentDTO);
	
	void generatePaymentAndDischargeLetter(String templateName, PaymentProcessCpuPageDTO paymentDTO);
	
	void generateHospPaymentLetter(String templateName, PaymentProcessCpuPageDTO updatedDto);
}