package com.shaic.restservices.bancs.sendped;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;

import com.shaic.domain.PolicyService;
import com.shaic.domain.preauth.PEDReverseFeed;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class PEDTransaction {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	DBCalculationService dbCalcService;
	
	@EJB
	private PolicyService policyService;
	
	@Resource
	private UserTransaction utx;
	
	public String doPEDTransaction(PEDSendDetails argObj) throws Exception{
		String uploadStatus = "";
		try{
			utx.begin();
			if(argObj != null){
	
//					Date uploadDate = null;
//					OldInitiatePedEndorsement pedDetails = getPedDetails(argObj.getServiceTransactionId());
//					if(pedDetails != null){
					
					/*Policy policy = policyService.getPolicyByPolicyNubember(argObj.getPolicyNumber());
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
					entityManager.persist(pedDetails);*/
//					uploadStatus = "Successfully Updated";
//					utx.commit();
//				}
					
					PEDReverseFeed pedReverseFeedObj = new PEDReverseFeed();
					pedReverseFeedObj.setPolicyNumber(argObj.getPolicyNumber());
					pedReverseFeedObj.setEndorsementStatus(argObj.getEndorsementStatus());
					pedReverseFeedObj.setEndorsementDate(convertToDate(argObj.getEndosementEffectiveDate(), "dd-MM-yyyy"));
					pedReverseFeedObj.setGaeEndApprYN(argObj.getGaeApprEndYN());
					pedReverseFeedObj.setGaeApprUID(argObj.getGaeApprUID());
					pedReverseFeedObj.setGaeApprName(argObj.getGaeApprName());
					pedReverseFeedObj.setGaeApprDT(convertToDate(argObj.getGaeApprDate(), "dd-MM-yyyy"));
					pedReverseFeedObj.setGaeApprRemarks(argObj.getGaeApprRemarks());
					pedReverseFeedObj.setActiveStatus(1);
					pedReverseFeedObj.setWorkItemId(Long.parseLong(argObj.getWorkItemId()));
					entityManager.persist(pedReverseFeedObj);
					
					uploadStatus = "Successfully Updated";
					utx.commit();
				
			}
		}catch(Exception exp){
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while updating PED Details in transaction.");
			exp.printStackTrace();
			throw new Exception(uploadStatus);
		}
		return uploadStatus;
	}
	
	/*@SuppressWarnings("unchecked")
	private OldInitiatePedEndorsement getPedDetails(String serviceTransactionId) {
		Query findByKey = entityManager.createNamedQuery("OldInitiatePedEndorsement.findByServiceTransactionId").setParameter("serviceTransactionID", serviceTransactionId);
		List<OldInitiatePedEndorsement> intimationList = (List<OldInitiatePedEndorsement>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}*/
	
	public Date convertToDate(String dateValueInString, String dateFormatToBeConverted) throws ParseException{
		SimpleDateFormat  sdf = null;
		SimpleDateFormat  ssdf = new SimpleDateFormat("dd/MM/yyyy");
//		System.out.println(dateValueInString);
		Date sDate = ssdf.parse(dateValueInString);
		String dDate = null;
		if(!StringUtils.isBlank(dateValueInString)){
			 sdf = new SimpleDateFormat(dateFormatToBeConverted);
			 dDate = sdf.format(sDate);
		}
		Date finalDate =  sdf.parse(dDate);
		return finalDate;
	}
	
	
	
}
