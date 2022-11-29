package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.withdrawWizard.WithdrawPreauthMapper;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.IncurredClaimRatio;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.reimbursement.rod.allowReconsideration.search.AllowRecosniderMapper;
import com.shaic.reimbursement.rod.allowReconsideration.search.SearchAllowReconsiderationTableDTO;

@Stateless
public class SearchFlagReconsiderationReqService extends  AbstractDAO<Reimbursement> {
	
	/**
	 * Entity manager is created to load LOB value from master service.
	 * When created instance for master service and tried to reuse the same, 
	 * faced error in entity manager invocation. Also when user, @Inject or @EJB
	 * annotation, faced issues in invocation. Hence for time being created
	 * entity manager instance and using the same. Later will check with siva 
	 * for code.
	 * */
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	public SearchFlagReconsiderationReqService(){
		super();
	}
	
	public Page<SearchFlagReconsiderationReqTableDTO> search(SearchFlagReconsiderationReqFormDTO formDTO, String userName, String passWord)
	{
		try 
		{
			String intimationNo = formDTO.getIntimationNo();
			String rodNo = formDTO.getRodNo();
			List<SearchFlagReconsiderationReqTableDTO> tableDto = new ArrayList<SearchFlagReconsiderationReqTableDTO>();
			if(formDTO != null){
				final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<Reimbursement> criteriaQuery = builder
						.createQuery(Reimbursement.class);
	
				Root<Reimbursement> searchRoot = criteriaQuery.from(Reimbursement.class);
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (intimationNo != null && !intimationNo.isEmpty()) {
					Predicate intimationPredicate = builder.equal(
							searchRoot.<Claim> get("claim")
									.<Intimation>get("intimation").<String> get("intimationId"),
									intimationNo );
					predicates.add(intimationPredicate);
				}
				if (rodNo != null && !rodNo.isEmpty()) {
					Predicate rodNoPredicate = builder
							.equal(searchRoot.<String> get("rodNumber"),
									 rodNo );
					predicates.add(rodNoPredicate);
				}
				
//				Predicate condition1= builder.equal(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);				
//				predicates.add(condition1);

				
				criteriaQuery.select(searchRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));

				final TypedQuery<Reimbursement> searchClaimQuery = entityManager
						.createQuery(criteriaQuery);

				List<Reimbursement> pageItemList  = searchClaimQuery.getResultList();
				
				List<SearchFlagReconsiderationReqTableDTO> resultList = FlagReconsiderationReqMapper.getInstance().getRejectionDTO(pageItemList);
				List<SearchFlagReconsiderationReqTableDTO> finalList = new ArrayList<SearchFlagReconsiderationReqTableDTO>();
				List<String> rodList = new ArrayList<String>();
				
				for (SearchFlagReconsiderationReqTableDTO searchReconsiderationTableDTO : resultList) {
					if(searchReconsiderationTableDTO.getRodNumber() != null && (searchReconsiderationTableDTO.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
							|| searchReconsiderationTableDTO.getStatusKey().equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS)
							|| searchReconsiderationTableDTO.getStatusKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS))) {
					Reimbursement reimbursementObject = getReimbursementObject(searchReconsiderationTableDTO.getRodKey());
					if(reimbursementObject.getDocAcknowLedgement() != null){
						
						searchReconsiderationTableDTO.setClaimedAmount(getClaimedAmt(reimbursementObject.getDocAcknowLedgement()).longValue());
						String billClassificationValue = getBillClassificationValue(reimbursementObject.getDocAcknowLedgement());
						if(billClassificationValue != null && reimbursementObject.getDocAcknowLedgement().getBenifitFlag() != null){
							billClassificationValue += ","+reimbursementObject.getDocAcknowLedgement().getBenifitFlag();
						}else if(reimbursementObject.getDocAcknowLedgement().getBenifitFlag() != null){
							billClassificationValue = reimbursementObject.getDocAcknowLedgement().getBenifitFlag();
						}
						searchReconsiderationTableDTO.setBillClassification(billClassificationValue);
						
						Claim claimByKey = reimbursementObject.getClaim();
						ClaimDto claimDTO = null;
						
						if (claimByKey != null) {
							claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
							searchReconsiderationTableDTO.setClaimDto(claimDTO);
							
						}
						Reimbursement reimbursementParentkey = getReimbursementParent(searchReconsiderationTableDTO.getRodKey());
						
						if(reimbursementParentkey != null && reimbursementParentkey.getParentKey() != null ){
							searchReconsiderationTableDTO.setDisableReconsiderationReq("Y");
							if (reimbursementObject.getReconsiderationFlagReq() != null && reimbursementObject.getReconsiderationFlagReq().equalsIgnoreCase("Y")) {
								reimbursementObject.setReconsiderationFlagReq("N");
							}
						}
						
						if (reimbursementObject.getReconsiderationFlagReq() != null && reimbursementObject.getReconsiderationFlagReq().equalsIgnoreCase("Y")) {
							searchReconsiderationTableDTO.setColorCodeCell("GREENFLAG");
						}
						if(reimbursementObject.getStatus().getKey().equals(ReferenceTable.REIMBURSEMENT_SETTLED_STATUS) 
								&& reimbursementObject.getFinancialApprovedAmount() != null ){
							searchReconsiderationTableDTO.setApprovedAmount(reimbursementObject.getFinancialApprovedAmount().longValue());
							
						}
						
						if(reimbursementObject.getClaim().getIntimation() != null && reimbursementObject.getClaim().getProcessClaimType() != null 
								&& (reimbursementObject.getProcessClaimType().equalsIgnoreCase("P")   || reimbursementObject.getClaim().getProcessClaimType().equalsIgnoreCase("P") 
										|| reimbursementObject.getClaim().getIntimation().getProcessClaimType().equalsIgnoreCase("P") ) 
								&& reimbursementObject.getBenApprovedAmt() != null){
							searchReconsiderationTableDTO.setClaimedAmount(reimbursementObject.getDocAcknowLedgement().getBenifitClaimedAmount().longValue());
							searchReconsiderationTableDTO.setApprovedAmount(reimbursementObject.getBenApprovedAmt().longValue());
						}
					}
					
					
						
						finalList.add(searchReconsiderationTableDTO);
						rodList.add(searchReconsiderationTableDTO.getRodNumber());
					}
					
				}
				Page<SearchFlagReconsiderationReqTableDTO> pageList = new Page<SearchFlagReconsiderationReqTableDTO>();
				pageList.setPageItems(finalList);
				pageList.setTotalRecords(finalList.size());
				return pageList;
				
			}
		}
			catch(Exception e){
				e.printStackTrace();
			}
		return null;		
	}

	@Override
	public Class<Reimbursement> getDTOClass() {
		return Reimbursement.class;
	}
	

	/**
	 * Method to load Lob value
	 * 
	 * */
	public String loadLobValue(Long lobID)
	{
		MastersValue a_mastersValue = new MastersValue();
		if (lobID != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", lobID);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue.getValue();
		
		
	}
	
	public IncurredClaimRatio getIncurredClaimRatio(String policyNumber, String insuredNumber){
		
		Query query = entityManager
				.createNamedQuery("IncurredClaimRatio.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		//query.setParameter("insuredNumber", insuredNumber);
		List<IncurredClaimRatio> result = (List<IncurredClaimRatio>) query.getResultList();
		if(result != null && ! result.isEmpty()){
			return result.get(0);
		}
		return null;
		
	}


	public String getColorCodeForGMC(String policyNumber, String insuredNumber){
		IncurredClaimRatio incurredClaimRatio = getIncurredClaimRatio(policyNumber, insuredNumber);
		if(incurredClaimRatio != null){
			return incurredClaimRatio.getClaimColour();
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
	
	public Boolean isAlreadyPreauthApprovedForReopen(Long cashlessKey){

		String query = "SELECT * FROM  IMS_CLS_STAGE_INFORMATION  WHERE CASHLESS_KEY =" + cashlessKey
				+ " AND STATUS_ID IN (22,23,26,30,31,38,40,63,64,182,183)";
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		List<StageInformation> resultList = (List<StageInformation>)nativeQuery.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			return true;
		}
		return false;
		 

	}

	private Double getClaimedAmt(DocAcknowledgement docAck)
	{
		Double claimedAmt = 0d;
		if(null != docAck.getHospitalizationClaimedAmount())
		{
			claimedAmt += docAck.getHospitalizationClaimedAmount();
		}
		if(null != docAck.getPreHospitalizationClaimedAmount())
		{
			claimedAmt += docAck.getPreHospitalizationClaimedAmount();
		}
		if(null != docAck.getPostHospitalizationClaimedAmount())
		{
			claimedAmt += docAck.getPostHospitalizationClaimedAmount();
		}
		if(null != docAck.getProdHospBenefitClaimedAmount())
		{
			claimedAmt += docAck.getProdHospBenefitClaimedAmount();
		}
		return claimedAmt;
	}
	 private String getBillClassificationValue(DocAcknowledgement docAck) {
			StringBuilder strBuilder = new StringBuilder();
			// StringBuilder amtBuilder = new StringBuilder();
			// Double total = 0d;
			try {
				if (("Y").equals(docAck.getHospitalisationFlag())) {
					strBuilder.append("Hospitalization");
					strBuilder.append(",");
				}
				if (("Y").equals(docAck.getPreHospitalisationFlag())) {
					strBuilder.append("Pre-Hospitalization");
					strBuilder.append(",");
				}
				if (("Y").equals(docAck.getPostHospitalisationFlag())) {
					strBuilder.append("Post-Hospitalization");
					strBuilder.append(",");
				}

				if (("Y").equals(docAck.getPartialHospitalisationFlag())) {
					strBuilder.append("Partial-Hospitalization");
					strBuilder.append(",");
				}

				if (("Y").equals(docAck.getLumpsumAmountFlag())) {
					strBuilder.append("Lumpsum Amount");
					strBuilder.append(",");

				}
				if (("Y").equals(docAck.getHospitalCashFlag())) {
					strBuilder.append("Add on Benefits (Hospital cash)");
					strBuilder.append(",");

				}
				if (("Y").equals(docAck.getPatientCareFlag())) {
					strBuilder.append("Add on Benefits (Patient Care)");
					strBuilder.append(",");
				}
				if (("Y").equals(docAck.getHospitalizationRepeatFlag())) {
					strBuilder.append("Hospitalization Repeat");
					strBuilder.append(",");
				}
				
				if (("Y").equals(docAck.getCompassionateTravel())) {
					strBuilder.append("Compassionate Travel");
					strBuilder.append(",");
				}
				
				if (("Y").equals(docAck.getRepatriationOfMortalRemain())) {
					strBuilder.append("Repatriation Of Mortal Remains");
					strBuilder.append(",");
				}
				
				if(null != docAck.getClaim()&& docAck.getClaim().getIntimation() != null && docAck.getClaim().getIntimation().getPolicy() != null &&
						(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey())
								|| ReferenceTable.getValuableServiceProviderForFHO().containsKey(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
					if (("Y").equals(docAck.getPreferredNetworkHospita())) {
						strBuilder.append("Valuable Service Provider (Hospital)");
						strBuilder.append(",");
					}
				}
				else{ 
					if (("Y").equals(docAck.getPreferredNetworkHospita())) {
						strBuilder.append("Preferred Network Hospital");
						strBuilder.append(",");
					}
				}
				
				if (("Y").equals(docAck.getSharedAccomodation())) {
					strBuilder.append("Shared Accomodation");
					strBuilder.append(",");
				}
				
				if (("Y").equals(docAck.getEmergencyMedicalEvaluation())) {
					strBuilder.append("Emergency Medical Evacuation");
					strBuilder.append(",");
				}
				
				//added for new product076
				if (("Y").equals(docAck.getProdHospBenefitFlag())) {	
				strBuilder.append("Hospital Cash");
				}
				
				// rodQueryDTO.setClaimedAmount(total);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return strBuilder.toString();
		}
	 
	 public Reimbursement getReimbursementObject(Long key)
		{
			Query query = entityManager.createNamedQuery("Reimbursement.findByKey");
			query = query.setParameter("primaryKey", key);
			List<Reimbursement> reimbursementObjectList = query.getResultList();
			if(null != reimbursementObjectList && !reimbursementObjectList.isEmpty())
			{
				entityManager.refresh(reimbursementObjectList.get(0));
				return reimbursementObjectList.get(0);
			}
			return null;
		}
	 
	 public Reimbursement getReimbursementParent(Long key)
		{
			Query query = entityManager.createNamedQuery("Reimbursement.findReimbursementParentKey");
			query = query.setParameter("parentKey", key);
			List<Reimbursement> reimbursementObjectList = query.getResultList();
			if(null != reimbursementObjectList && !reimbursementObjectList.isEmpty())
			{
				entityManager.refresh(reimbursementObjectList.get(0));
				return reimbursementObjectList.get(0);
			}
			return null;
		}
	 
}
