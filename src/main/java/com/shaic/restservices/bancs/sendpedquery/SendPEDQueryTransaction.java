package com.shaic.restservices.bancs.sendpedquery;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SendPEDQueryTransaction {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	DBCalculationService dbCalcService;
	
	@EJB
	private PolicyService policyService;
	
	@Resource
	private UserTransaction utx;
	
	public String doSendPEDQueryTransaction(SendPEDQueryDetails argObj) throws Exception{
		String uploadStatus = "";
		boolean tempStatus = false;
		try{
		//	utx.begin();
			if(argObj != null){
	/*
					Date uploadDate = null;
					OldInitiatePedEndorsement pedDetails = getPedDetails(argObj.getServiceTransactionId());
					if(pedDetails != null){
					
					Policy policy = policyService.getPolicyByPolicyNubember(argObj.getPolicyNumber());
					pedDetails.setPolicy(policy);
					pedDetails.setEndorsementStatus(argObj.getEndorsementStatus());
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					uploadDate = formatter.parse(argObj.getEndosementEffectiveDate());
					pedDetails.setEndorsementDate(uploadDate);
					pedDetails.setServiceTransactionID(argObj.getServiceTransactionId());
					pedDetails.setPedApprCode(argObj.getGaeApprUID());
					pedDetails.setPedApprDate(formatter.parse(argObj.getGaeApprDate()));
					pedDetails.setPedApprName(argObj.getGaeApprName());
					pedDetails.setPedApprRemarks(argObj.getGaeApprRemarks());
					pedDetails.setPedApprYN(argObj.getGaeApprEndYN());
					entityManager.persist(pedDetails);
					
					uploadStatus = "Successfully Updated";
					utx.commit();
					}*/
					uploadStatus = "Successfully Updated";
			}
		}catch(Exception exp){
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while updating PED Details in transaction.");
			exp.printStackTrace();
			throw new Exception(uploadStatus);
		}
		return uploadStatus;
	}

}
