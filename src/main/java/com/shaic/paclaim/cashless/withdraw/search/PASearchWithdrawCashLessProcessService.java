package com.shaic.paclaim.cashless.withdraw.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;

@Stateless
public class PASearchWithdrawCashLessProcessService extends  AbstractDAO<Preauth> {
	
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
	
	public PASearchWithdrawCashLessProcessService(){
		super();
	}
	
	@SuppressWarnings("static-access")
	public Page<PASearchWithdrawCashLessProcessTableDTO> search(PASearchWithdrawCashLessProcessFormDTO formDTO, String userName, String passWord)
	{
		try 
		{
			String strIntimationNo = formDTO.getIntimationNo();
			String strPolicyNo = formDTO.getPolicyNo();		
			List<PASearchWithdrawCashLessProcessTableDTO> results = new ArrayList<PASearchWithdrawCashLessProcessTableDTO>(); 
			
			
			if(null != formDTO)
			{

				final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<Preauth> criteriaQuery = builder
						.createQuery(Preauth.class);
	
				Root<Preauth> searchRoot = criteriaQuery.from(Preauth.class);
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (strIntimationNo != null) {
					Predicate intimationPredicate = builder.like(
							searchRoot.<Preauth> get("intimation")
									.<String> get("intimationId"), "%"
									+ strIntimationNo + "%");
					predicates.add(intimationPredicate);
				}
				if (strPolicyNo != null) {
					Predicate policyPredicate = builder
							.like(searchRoot.<Preauth> get("policy").<String> get("policyNumber"), "%"
									+ strPolicyNo + "%");
					predicates.add(policyPredicate);
				}
				
				Predicate condition1= builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PREAUTH_REJECT_STATUS);				
				predicates.add(condition1);
				
				Predicate condition2 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
				predicates.add(condition2);
				
				Predicate condition3 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.CLEAR_CASHLESS_STATUS_FOR_PREAUTH);
				predicates.add(condition3);
				
				
				Predicate condition4 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.CLEAR_CASHLESS_STATUS_FOR_ENHANCEMENT);
				predicates.add(condition4);
				
//				Predicate condition3 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.ENHANCEMENT_REJECT_STATUS);
//				predicates.add(condition3);
//				
//				Predicate condition4 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS);
//				predicates.add(condition4);
//				
//				Predicate condition5 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
//				predicates.add(condition5);
//				
//				Predicate condition6 = builder.notEqual(searchRoot.<Status>get("status").<Long>get("key"),ReferenceTable.STANDALONE_WITHDRAW_STATUS);
//				predicates.add(condition6);
				Predicate condition8 = builder.equal(searchRoot.<Preauth>get("claim").<Long>get("lobId"), ReferenceTable.PA_LOB_KEY);
				predicates.add(condition8);	
				
				criteriaQuery.select(searchRoot).where(
						builder.and(predicates.toArray(new Predicate[] {})));

				final TypedQuery<Preauth> searchClaimQuery = entityManager
						.createQuery(criteriaQuery);

				List<Preauth> pageItemList  = searchClaimQuery.getResultList();
				
				
				
				for (Preauth preauth : pageItemList) {
					if(preauth.getTotalApprovalAmount() == null){
//						pageItemList.remove(preauth);
					}
				}
				
				 List<Long> keysList=new ArrayList<Long>();
				    for(Preauth preauth: pageItemList){
				    	keysList.add(preauth.getKey());
				   }
				    
				    Long key=Collections.max(keysList);
				    
				    Query findAll = entityManager
							.createNamedQuery("Preauth.findByKey");
					findAll.setParameter("preauthKey", key);
					
				List<Preauth> preauthListfromDb=(List<Preauth>)findAll.getResultList();
				List<Preauth> preauthList=new ArrayList<Preauth>();
				for (Preauth preauth : preauthListfromDb) {
					
					if(preauth.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					if(! (preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE)) && 
							! (preauth.getStage().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE))
							&& !(preauth.getStage().getKey().equals(ReferenceTable.WITHDRAW_STAGE))){
						
						Map<Long, Long> preauthForWithdrawAndDownsize = ReferenceTable.getPreauthForWithdrawAndDownsize();
						Long status = preauthForWithdrawAndDownsize.get(preauth.getStatus().getKey());
						if(status != null){
							 preauthList.add(preauth);
						  }
						}
					}
				}
				
				
				results=PAWithdrawPreauthMapper.getInstance().getWithdrawTableDTO(preauthList);
				
				List<Long>  diagnosisList = new ArrayList<Long>();
				
				if(null!=preauthList){
					for (Preauth preauth : preauthList) {
						diagnosisList.add(preauth.getKey());
					}
				}
				List<String> lobValueList=new ArrayList<String>();
				
				if(null !=preauthList){
					for (Preauth preauth : preauthList) {
						
						Long lobId=preauth.getPolicy().getLobId();
						String lobValue=loadLobValue(lobId);
						lobValueList.add(lobValue);
						
					}
				}
				int i=0;
				for (PASearchWithdrawCashLessProcessTableDTO resultDto:results) {
					resultDto.setLob(lobValueList.get(i));
					resultDto.setUsername(userName);
					resultDto.setPassword(passWord);
					if(resultDto.getProductKey() != null && ReferenceTable.getGPAProducts().containsKey(resultDto.getProductKey())){
						if(resultDto.getPaPatientName() != null){
							resultDto.setInsuredPatientName(resultDto.getPaPatientName());
						}
					}
					i++;
				}
				
				List<PedValidation> pedValidationList=SHAUtils.getByPreauthKey(entityManager, diagnosisList);
				
				List<Long> diagnosisInfo=new ArrayList<Long>();
				if(null !=pedValidationList){
					for (PedValidation pedValidation : pedValidationList) {
						
						diagnosisInfo.add(pedValidation.getDiagnosisId());
					}
				}
				
				List<Diagnosis> diagnosisValue=SHAUtils.getByDiagonsisKey(entityManager, diagnosisInfo);
				
				for (Diagnosis diagnosis : diagnosisValue) {
					for (PASearchWithdrawCashLessProcessTableDTO tableDTO : results) {
						tableDTO.setDiagnosis(diagnosis.getValue());
					}
				}
				
				List<Long>  hospTypeList = new ArrayList<Long>();
				
				ListIterator<Preauth> iterClaim = preauthList.listIterator();
				
				while (iterClaim.hasNext())
				{
					 Preauth objClaim = iterClaim.next();
					 Long hospitalTypeId = objClaim.getIntimation().getHospital();
					 /*
					  * To fetch hospital information from VW_HOSPITALS 
					  * we require hospital type id. Hence forming
					  * below list
					  * 
					  * **/
					 hospTypeList.add(hospitalTypeId);
				}
				
				List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
				List<PASearchWithdrawCashLessProcessTableDTO> searchClaimTableDTOForHospitalInfo = new ArrayList<PASearchWithdrawCashLessProcessTableDTO>();
				resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
				if(null!=resultListForHospitalInfo)
				{
				for(Hospitals hospitalInfo:resultListForHospitalInfo)
				{
					for (PASearchWithdrawCashLessProcessTableDTO searchWithdrawCashLessProcessTableDTO : results) {
						searchWithdrawCashLessProcessTableDTO.setHospitalName(hospitalInfo.getName());
						searchWithdrawCashLessProcessTableDTO.setHospitalAddress(hospitalInfo.getAddress());
						searchWithdrawCashLessProcessTableDTO.setHospitalCity(hospitalInfo.getCity());
					}
				}
			}
			Page<Preauth> pagedList = super.pagedList(formDTO.getPageable());
			Page<PASearchWithdrawCashLessProcessTableDTO> page = new Page<PASearchWithdrawCashLessProcessTableDTO>();
			page.setPageItems(results);
			page.setIsDbSearch(true);
			return page;
		}
		}
			catch(Exception e){
				e.printStackTrace();
			}
		return null;		
	}

	@Override
	public Class<Preauth> getDTOClass() {
		return Preauth.class;
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
}
