package com.shaic.claim.fileUpload.selectrod;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;

@Stateless
public class CoordinatorRODService {
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private CreateRODService createRodService;
	
	@SuppressWarnings({ "unchecked" })
	public List<CoordinatorRODTableDTO> getRODByKey(Long argRodKey){
		List<CoordinatorRODTableDTO> listOfObj = new ArrayList<CoordinatorRODTableDTO>();
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey");
		query = query.setParameter("primaryKey", argRodKey);
		List<Reimbursement> rodList = query.getResultList();
		int  i = 0;
		for(Reimbursement rec : rodList){
			CoordinatorRODTableDTO newObj = new CoordinatorRODTableDTO();
			newObj.setSno(i+1);
			newObj.setRodNumber(rec.getRodNumber());
			StringBuilder billClass = new StringBuilder();
			if(rec.getDocAcknowLedgement().getHospitalisationFlag() != null && rec.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("y")){
				billClass.append("Hospitalisation, ");
			}else if(rec.getDocAcknowLedgement().getPreHospitalisationFlag() != null && rec.getDocAcknowLedgement().getPreHospitalisationFlag().equalsIgnoreCase("y")){
				billClass.append("Pre - Hospitalisation, ");
			}else if(rec.getDocAcknowLedgement().getPostHospitalisationFlag() != null && rec.getDocAcknowLedgement().getPostHospitalisationFlag().equalsIgnoreCase("y")){
				billClass.append("Post - Hospitalisation, ");
			}else if(rec.getDocAcknowLedgement().getPartialHospitalisationFlag() != null && rec.getDocAcknowLedgement().getPartialHospitalisationFlag().equalsIgnoreCase("y")){
				billClass.append("Partial - Hospitalisation, ");
			}else if(rec.getDocAcknowLedgement().getLumpsumAmountFlag() != null && rec.getDocAcknowLedgement().getLumpsumAmountFlag().equalsIgnoreCase("y")){
				billClass.append("Lumpsum - Amount, ");
			}else if(rec.getDocAcknowLedgement().getHospitalCashFlag() != null && rec.getDocAcknowLedgement().getHospitalCashFlag().equalsIgnoreCase("y")){
				billClass.append("Hospital - Cash, ");
			}else if(rec.getDocAcknowLedgement().getHospitalizationRepeatFlag() != null && rec.getDocAcknowLedgement().getHospitalizationRepeatFlag().equalsIgnoreCase("y")){
				billClass.append("Hospitalisation - Repeat, ");
			}else if(rec.getDocAcknowLedgement().getPatientCareFlag() != null && rec.getDocAcknowLedgement().getPatientCareFlag().equalsIgnoreCase("y")){
				billClass.append("Patient - Care, ");
			}else{
				
			}
			String billClassificationStr = "";
			if(!StringUtils.isBlank(billClass)){
				billClassificationStr = billClass.toString().substring(0, billClass.length() - 2);
			}
			
			newObj.setBillClasification(billClassificationStr);
			Double claimedAmt = createRodService.getClaimedAmount(rec.getKey());
			newObj.setRodClaimedAmount(claimedAmt.longValue());
			Double approvedAmt = new Double(0.0);
			if(rec.getStatus().getKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
				approvedAmt = rec.getBillingApprovedAmount();
			}else if(rec.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
				approvedAmt = rec.getFinancialApprovedAmount();
			}else{
				approvedAmt = rec.getCurrentProvisionAmt() != null ? rec.getCurrentProvisionAmt() : approvedAmt;
			}
			newObj.setRodApprovedAmount(approvedAmt.longValue());
			newObj.setRodStatus(rec.getStatus().getProcessValue());
			newObj.setClaim(rec.getClaim());
			listOfObj.add(newObj);
			i++;
		}
		
		return listOfObj;
	}
}
