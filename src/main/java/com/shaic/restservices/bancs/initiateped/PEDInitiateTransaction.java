package com.shaic.restservices.bancs.initiateped;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.restservices.bancs.sendped.PEDSendDetails;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class PEDInitiateTransaction {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	DBCalculationService dbCalcService;
	
	@EJB
	private PolicyService policyService;
	
	@Resource
	private UserTransaction utx;
	
	public String doPEDTrans(PEDInitiateResponse argObj, String serviceID) throws Exception {
		String uploadStatus = "";
		boolean tempStatus = false;
		try{
			utx.begin();
			if(argObj != null){

					OldInitiatePedEndorsement pedDetails = getPedDetails(serviceID);
					if(pedDetails != null){
					pedDetails.setWorkItemID(argObj.getWorkItemID());
					pedDetails.setWsStatus(argObj.getStatus());
					uploadStatus = "Successfully Updated";
					utx.commit();
					}
				
			}
		}catch(Exception exp){
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while updating PED Details in transaction.");
			exp.printStackTrace();
			throw new Exception(uploadStatus);
		}
		return uploadStatus;
	}
	
	@SuppressWarnings("unchecked")
	private OldInitiatePedEndorsement getPedDetails(String serviceTransactionId) {
		Query findByKey = entityManager.createNamedQuery("OldInitiatePedEndorsement.findByServiceTransactionId").setParameter("serviceTransactionID", serviceTransactionId);
		List<OldInitiatePedEndorsement> intimationList = (List<OldInitiatePedEndorsement>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}


	
}
