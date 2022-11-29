package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.showwizard;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.AuditDetails;
import com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search.SearchFlagReconsiderationReqTableDTO;
import com.shaic.claim.clearcashless.dto.SearchClearCashlessDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimAuditQuery;
import com.shaic.domain.DocUploadToPremia;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Notification;


@Stateless
public class ReconsiderationFlagRequestService {
	
	@PersistenceContext
	protected EntityManager entityManager;

	private final Logger log = LoggerFactory.getLogger(ReconsiderationFlagRequestService.class);
	
	public Claim getClaimByKey(Long key) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", key);
		List<Claim> claim = (List<Claim>)query.getResultList();
		
		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		else{
			return null;
		}
	}
	
	public Hospitals getHospitalObject(Long hospitalKey) {
		TypedQuery<Hospitals> query = entityManager.createNamedQuery("Hospitals.findByKey", Hospitals.class);
		query.setParameter("key", hospitalKey);
		List<Hospitals>	resultList = query.getResultList();
		if (null != resultList && 0 != resultList.size())
		{
			return resultList.get(0);
			
		}
		else
		{
			return null;
		}
	}
	
	public Preauth getPreauthById(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		
		return null;
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}

	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitRejectionReconsiderationFlag(SearchFlagReconsiderationReqTableDTO flagReconsiderationReqTableDTO) {
		log.info("Submit RejectionReconsiderationFlag ________________" + (flagReconsiderationReqTableDTO.getRodKey() != null ? flagReconsiderationReqTableDTO.getRodKey(): "NULL "));
		Reimbursement reimbursement = null;
		try {
			reimbursement =getRejectionKeyByReimbursement(flagReconsiderationReqTableDTO.getRodKey());
			
			if(reimbursement.getKey() != null && reimbursement.getKey().equals(flagReconsiderationReqTableDTO.getRodKey())){
				
				System.out.println(reimbursement.getKey()+"" + flagReconsiderationReqTableDTO.getCrmFlagged() + "\n"+ flagReconsiderationReqTableDTO.getRejectRemarks());
				
				reimbursement.setReconsiderationFlagReq(flagReconsiderationReqTableDTO.getCrmFlagged());
				reimbursement.setReconsiderationFlagRemark(flagReconsiderationReqTableDTO.getRejectRemarks());
				
//				entityManager.merge(reimbursement);
//				entityManager.flush();
//				entityManager.clear();
				log.info("------ Reconsideration rejection flag Request ------>"+reimbursement+"<------------reimbursement");
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Reimbursement getRejectionKeyByReimbursement(Long key){
		Reimbursement reimbursementRejection = null;
		List<Reimbursement> reimbursementRejectionList = new ArrayList<Reimbursement>();
		try{
			
			Query query = entityManager
					.createNamedQuery("Reimbursement.findByKey");
			query = query.setParameter("primaryKey", key);
			
			reimbursementRejectionList = (List<Reimbursement>)query.getResultList();
			
			if(reimbursementRejectionList != null && ! reimbursementRejectionList.isEmpty()){
				reimbursementRejection = reimbursementRejectionList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}
		return reimbursementRejection;
		
	}
	
	
	
}
