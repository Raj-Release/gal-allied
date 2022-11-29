package com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.MastersValue;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.fraudidentification.FraudIdentification;
import com.shaic.reimbursement.fraudidentification.FraudIdentificationTableDTO;
import com.vaadin.server.VaadinSession;

@Stateless
public class VerficationAccountDetailsService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void submitAccountVerificationDetails(List<VerificationAccountDeatilsTableDTO> dto,String verifiedStage) {

		String userId = (String) VaadinSession.getCurrent().getAttribute(
				BPMClientContext.USERID);

		for (VerificationAccountDeatilsTableDTO idaTableDTO : dto) {

			DuplicatePaymentVerify verifyPayment = new DuplicatePaymentVerify();
			verifyPayment.setClaimNo(idaTableDTO.getClaimNumber());
			verifyPayment.setPolicyNo(idaTableDTO.getPolicyNumber());
			verifyPayment.setInsuredName(idaTableDTO.getInsuredName());
			verifyPayment.setAccountNo(idaTableDTO.getAccountNumber());
			verifyPayment.setRodKey(idaTableDTO.getRodKey());
			verifyPayment.setRodNumber(idaTableDTO.getRodNumber());
			verifyPayment.setVerifiedDate(new Date());
			verifyPayment.setVerifiedUser(userId);
			verifyPayment.setIfscCode(idaTableDTO.getIfscCode());
			verifyPayment.setVerifyStage(verifiedStage);

			entityManager.persist(verifyPayment);

			entityManager.flush();
			entityManager.clear();
		}
	}
	
	public String getDocumentReceivedFrom(Long key){
		Query  query = entityManager.createNamedQuery("MastersValue.findByKey");
		query = query.setParameter("parentKey", key);
		List<MastersValue> listOfObj = query.getResultList();
		
		if(listOfObj != null && !listOfObj.isEmpty()){
			return listOfObj.get(0).getValue();
		}
		return null;
	}

}
