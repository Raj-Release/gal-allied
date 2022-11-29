package com.shaic.claim.settlementpullback.searchbatchpendingpullback;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class LotPullBackPageService {

	
	@PersistenceContext
	protected EntityManager entityManager;
	
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
	
	public List<DocumentDetails> getQueryDocumentDetailsDataByRod(String rodNumber,String docType,String docTypeForScrc,String docSource)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findQueryByRodNo");
		query = query.setParameter("reimbursementNumber", rodNumber);
		docType = docType.toLowerCase();
		docSource = docSource.toLowerCase();
		
		if(docTypeForScrc != null){
			docTypeForScrc = docTypeForScrc.toLowerCase();
		}
		
		query = query.setParameter("billsummary", docType);
		query = query.setParameter("billassessment", docTypeForScrc);
		query = query.setParameter("financialapproval", "%"+docSource+"%");
		List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
		List<DocumentDetails> documentDetailsList  = query.getResultList();
		for (DocumentDetails documentDetails : documentDetailsList) {
			entityManager.refresh(documentDetails);
		}
		
		return documentDetailsList;
	}
	
	
	public ReimbursementCalCulationDetails getReimbursementCalcByRodAndClassificationKey(Long rodKey,
			Long classificatonKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementCalCulationDetails.findByRodAndBillClassificationKey");
		query.setParameter("reimbursementKey", rodKey);
		query.setParameter("billClassificationKey", classificatonKey);

		@SuppressWarnings("unchecked")
		List<ReimbursementCalCulationDetails> reimbBenefits = query
				.getResultList();
		if (reimbBenefits != null && !reimbBenefits.isEmpty()) {
			return reimbBenefits.get(0);
		}
		return null;
	}

	
	public void submitLotPullBack(SearchLotPullBackTableDTO dto) {
		try {
			
			/**
			 *  Needs to be enabled to resolve the reconsideration cases...
			 */
			ClaimPayment claimPayment = dto.getClaimPayment();
			
			if(null != claimPayment){
				Stage stage = new Stage();
				stage.setKey(ReferenceTable.PAYMENT_PROCESS_STAGE);
				claimPayment.setStageId(stage);

				Status status = new Status();
				status.setKey(ReferenceTable.PAYMENT_NEW_STATUS);
				claimPayment.setStatusId(status);

				claimPayment.setLotNumber(null);
				claimPayment.setLotStatus(null);

				if(SHAConstants.YES_FLAG.equalsIgnoreCase(claimPayment.getBatchHoldFlag())){
					claimPayment.setBatchHoldFlag(SHAConstants.N_FLAG);
				}

				entityManager.merge(claimPayment);
				entityManager.flush();
				entityManager.clear();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
