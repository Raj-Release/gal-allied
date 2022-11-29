package com.shaic.claim.fvrgrading.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.HospitalAcknowledge;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;


@Stateless
public class SearchFvrReportGradingService extends AbstractDAO<Claim> {
	
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
	
	public SearchFvrReportGradingService() {
		super();
	}


	@SuppressWarnings("unchecked")
	public HospitalAcknowledge getHospitalAcknowledgementByKey(
			Long acknowledHospitalKey) {

		Query findByKey = entityManager
				.createNamedQuery("HospitalAcknowledge.findAll");

		List<HospitalAcknowledge> hospitalList = (List<HospitalAcknowledge>) findByKey
				.getResultList();

		if (!hospitalList.isEmpty()) {
			return hospitalList.get(0);

		}
		return null;
	}

	@Override
	public Class<Claim> getDTOClass() {
		return Claim.class;
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
	
	public Preauth getLatestPreauthByClaim(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
		query.setParameter("claimkey", claimKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			Preauth preauth = singleResult.get(0);
			for(int i=0; i <singleResult.size(); i++) {
				entityManager.refresh(singleResult.get(i));
				if(!ReferenceTable.getRejectedPreauthKeys().containsKey(singleResult.get(i).getStatus().getKey())) {
					entityManager.refresh(singleResult.get(i));
					preauth = singleResult.get(i);
					break;
				} 
			}
			return preauth;
		}
		
		return null;
		
		
	}
	
	public List<Claim> getCashlessClaim(Long claimType){
		
		Query query = entityManager.createNamedQuery("Claim.findCashlessClaim");
		query.setParameter("claimType", claimType);
		
		List<Claim> listOfClaim = (List<Claim>) query.getResultList();
		for (Claim claim : listOfClaim) {
			entityManager.refresh(claim);
		}
		
		return listOfClaim;
	}
	
	public Page<SearchFvrReportGradingTableDto> search(SearchFvrReportGradingFormDto formDTO, String userName, String passWord) {
	
		//List<FieldVisitRequest> listIntimations = new ArrayList<FieldVisitRequest>(); 
		try{
			
		DBCalculationService db = new DBCalculationService();
		int pageNumber = 0;
		java.sql.Date sqlFromDate = null;
		java.sql.Date sqlToDate = null;
		String intimationNo =  null != formDTO.getIntimationNumber() && !formDTO.getIntimationNumber().isEmpty() ? formDTO.getIntimationNumber() :null;
		String policyNo =  null != formDTO.getPolicyNumber() && !formDTO.getPolicyNumber().isEmpty() ? formDTO.getPolicyNumber() : null;
		
		Long cpuCode =  null != formDTO && formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId(): null : null;
		Long strClaimType =  null != formDTO.getClaimType() && formDTO.getClaimType().getId() != null ? formDTO.getClaimType().getId() : null;
		
		Date fromDate = null != formDTO && formDTO.getFromDate() != null ? formDTO.getFromDate() : null;
		
		Date endDate = null != formDTO && formDTO.getToDate() != null ? formDTO.getToDate() : null;
		
		/*final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<FieldVisitRequest> criteriaQuery = criteriaBuilder.createQuery(FieldVisitRequest.class);
		
		Root<FieldVisitRequest> root = criteriaQuery.from(FieldVisitRequest.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(policyNo != null){
		Predicate condition2 = criteriaBuilder.like(root.<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
		conditionList.add(condition2);
		}
		
		if(cpuCode != null){
			Predicate condition3 = criteriaBuilder.equal(root.<Long>get("fvrCpuId"), cpuCode);
			conditionList.add(condition3);
		}
		
		if(null != strClaimType)
		{
			Predicate condition4 = criteriaBuilder.equal(root.<Claim>get("claim").<MastersValue>get("claimType").<Long>get("key"), strClaimType);
			conditionList.add(condition4);
		}
			
		Predicate condition5 = criteriaBuilder.notLike(root.<Intimation>get("intimation").<String>get("processClaimType"), "%"+SHAConstants.PA_TYPE+"%");
		conditionList.add(condition5);
		
		Predicate condition6 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"), ReferenceTable.FVR_REPLAY_RECIEVED_STATUS);
		conditionList.add(condition6);
		
		if(fromDate != null && endDate != null){
		
			Expression<Date> fromDateExpression = root
					.<Date> get("fvrReceivedDate");
			Predicate fromDatePredicate = criteriaBuilder
					.greaterThanOrEqualTo(fromDateExpression,
							fromDate);
			conditionList.add(fromDatePredicate);

			Expression<Date> toDateExpression = root
					.<Date> get("fvrReceivedDate");
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.add(Calendar.DATE, 1);
			endDate = c.getTime();
			Predicate toDatePredicate = criteriaBuilder
					.lessThanOrEqualTo(toDateExpression, endDate);
			conditionList.add(toDatePredicate);
		}
		
		if(intimationNo == null && policyNo == null){
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<FieldVisitRequest> typedQuery = entityManager.createQuery(criteriaQuery);
		pageNumber = formDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 0;
		}

		/*if(intimationNo == null && policyNo == null){
		listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listIntimations = typedQuery.getResultList();
		}
		
		List<FieldVisitRequest> listIntimations1 = getListBasedOnReimbursement(listIntimations);
		
		SearchFvrReportGradingMapper.getInstance();
		List<SearchFvrReportGradingTableDto> tableDTO = SearchFvrReportGradingMapper.getSearchFvrReportGradingTableDTO(listIntimations1);
		
		tableDTO = getHospitalDetails(tableDTO);*/
		if(fromDate != null && endDate != null){
			sqlFromDate = new java.sql.Date(fromDate.getTime()); 
		    sqlToDate = new java.sql.Date(endDate.getTime());	
		}
		
		
		List<SearchFvrReportGradingTableDto> result = db.getFVRGradingList(intimationNo, policyNo, cpuCode, strClaimType, sqlFromDate, sqlToDate);
		Page<SearchFvrReportGradingTableDto> page = new Page<SearchFvrReportGradingTableDto>();
		formDTO.getPageable().setPageNumber(pageNumber+1);
		
		/*if(result.size()<=10)
		{
			page.setHasNext(false);
		}
		else
		{
		page.setHasNext(true);
		}*/
		
		if(result.isEmpty()){
			formDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		
		
		return page;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Long> getMAApprovedList(){
		
		String query = "select a.FVR_KEY from IMS_CLS_FIELD_VISIT_REQUEST a inner join IMS_CLS_REIMBURSEMENT b on a.transaction_key = b.reimbursement_key where a.status_id=50 and b.MEDICAL_COMPLETED_DATE is not null";
		Query nativeQuery = entityManager.createNativeQuery(query);
		
		@SuppressWarnings("unchecked")
		List<BigDecimal> objList = (List<BigDecimal>) nativeQuery
				.getResultList();
		
		List<Long> list = new ArrayList<Long>();
		
		for (BigDecimal obj : objList) {
			list.add(obj.longValue());
		}
		
		return list;
		
	}
	
	private List<SearchFvrReportGradingTableDto> getHospitalDetails(
			List<SearchFvrReportGradingTableDto> tableDTO) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
	
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalId());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospCity(hospitalDetail.getCity());
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	private List<FieldVisitRequest> getListBasedOnReimbursement(List<FieldVisitRequest> listIntimations){
		
		List<FieldVisitRequest> listIntimations1 = new ArrayList<FieldVisitRequest>();
		
		for (FieldVisitRequest fieldVisitRequest : listIntimations) {
			Query findByClaimKey = entityManager.createNamedQuery(
					"Reimbursement.findByClaimKeyFVR").setParameter("claimKey", fieldVisitRequest.getClaim().getKey());
		
			List<Reimbursement> reimb = (List<Reimbursement>) findByClaimKey.getResultList();
			if(reimb != null && !reimb.isEmpty()){
				listIntimations1.add(fieldVisitRequest);
			}
		}
		
		
		return listIntimations1;
		
	}
	
	
	}
