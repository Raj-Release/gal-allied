package com.shaic.claim.reimbursement.rawanalysis;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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

import org.bouncycastle.asn1.cms.TimeStampAndCRL;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.RawInvsHeaderDetails;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODMapper;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;


@Stateless
public class SearchProcessRawRequestService extends AbstractDAO<Claim> {
	Map<Long, Object> workFlowMap= null;
	@PersistenceContext
	protected EntityManager entityManager;
	
	/*public Page<SearchProcessRawRequestTableDto> search(SearchProcessRawRequestFormDto searchFormDto,String userName,String password){
		
		List<RawInvsDetails> listOfRecords = new ArrayList<RawInvsDetails>();
		
		try{
			String intimationNo = searchFormDto.getIntimationNo() != null && !searchFormDto.getIntimationNo().isEmpty() ? searchFormDto.getIntimationNo():null;
			String policyNo = searchFormDto.getPolicyNo() != null && !searchFormDto.getPolicyNo().isEmpty() ? searchFormDto.getPolicyNo(): null;
			String cpuCode = searchFormDto.getCpuCode() != null && !searchFormDto.getCpuCode().getValue().isEmpty() ? searchFormDto.getCpuCode().getValue():null;
			Long initiatedFrm = searchFormDto.getIntiatedFrom() != null && searchFormDto.getIntiatedFrom().getId() != null ? searchFormDto.getIntiatedFrom().getId(): null;
			String claimType = searchFormDto.getClaimType() != null && !searchFormDto.getClaimType().getValue().isEmpty() ? searchFormDto.getClaimType().getValue(): null;
			String hospitalCode = searchFormDto.getHospitalCode() != null && !searchFormDto.getHospitalCode().isEmpty() ? searchFormDto.getHospitalCode(): null;
			Session session = (Session) entityManager.getDelegate();
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<RawInvsDetails> criteriaQuery = criteriaBuilder.createQuery(RawInvsDetails.class);
			
			Root<RawInvsDetails> root = criteriaQuery.from(RawInvsDetails.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();

			if(intimationNo != null){
			Predicate condition1 = criteriaBuilder.like(root.<RawInvsHeaderDetails>get("rawInvstigation").<String>get("intimationNo"), "%"+intimationNo+"%");
			conditionList.add(condition1);
			}
			
			Expression<Long> groupByExp = root.<RawInvsHeaderDetails>get("rawInvstigation").<Long>get("key").as(Long.class);

			if(policyNo != null){
			Predicate condition3 = criteriaBuilder.like(root.<RawInvsHeaderDetails>get("rawInvstigation").<String>get("policyNumber"), "%"+policyNo+"%");
			conditionList.add(condition3);
			}
			if(claimType != null){
				Predicate condition4 = criteriaBuilder.like(root.<RawInvsHeaderDetails>get("rawInvstigation").<String>get("claimType"), "%"+claimType+"%");
				conditionList.add(condition4);
			}
			if(cpuCode != null && !cpuCode.isEmpty()){
				String[] splitid = cpuCode.split("-");
				String id = splitid[0];
				cpuCode = id.trim();
				Predicate condition5 = criteriaBuilder.like(root.<RawInvsHeaderDetails>get("rawInvstigation").<String>get("cpuCode"), "%"+cpuCode+"%");
				conditionList.add(condition5);
			}
			if(hospitalCode != null){
				Predicate condition6 = criteriaBuilder.like(root.<RawInvsHeaderDetails>get("rawInvstigation").<String>get("hospitalCode"),hospitalCode);
				conditionList.add(condition6);
			}
			if(searchFormDto.getFromDate() != null && searchFormDto.getToDate() != null){				
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String formatFromDate = formatter.format(searchFormDto.getFromDate());
				String formatTODate = formatter.format(searchFormDto.getToDate());
				
				Expression<String> dateStringExprByModifyDate = criteriaBuilder.function("TO_CHAR", String.class, root.<Date>get("createdDate"), criteriaBuilder.literal("yyyyMMdd"));
				Predicate searchByDateRange = criteriaBuilder.between(dateStringExprByModifyDate, formatFromDate, formatTODate);
				conditionList.add(searchByDateRange);	
			}
			if(initiatedFrm != null){
				Predicate condition6 = criteriaBuilder.equal(root.<Long>get("requestedStage"),initiatedFrm);
				conditionList.add(condition6);
	
			}
			
			Predicate condition7 = criteriaBuilder.notEqual(root.<Long>get("requestedStatus"),ReferenceTable.RAW_TEAM_REPLIED);
			conditionList.add(condition7);
			
			if(intimationNo == null && policyNo == null  && null== initiatedFrm && claimType == null && cpuCode == null 
					&& null==searchFormDto.getFromDate() && null==searchFormDto.getToDate() && hospitalCode == null){
				
				List<Predicate> conditionList1 = new ArrayList<Predicate>();
				conditionList1.add(condition7);
				criteriaQuery.select(root).where(
						criteriaBuilder.and(conditionList1.toArray(new Predicate[] {})));
				}else{
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}
			final TypedQuery<RawInvsDetails> typedQuery = entityManager.createQuery(criteriaQuery);
			
			int pageNumber = searchFormDto.getPageable().getPageNumber();
			int firtResult;
			if (pageNumber > 1) 
			{
				firtResult = (pageNumber - 1) * 10;
			} 
			else 
			{
				firtResult = 0;
			}
			
			listOfRecords = typedQuery.getResultList();			
			List<SearchProcessRawRequestTableDto> tableList  = new ArrayList<SearchProcessRawRequestTableDto>();
			List<RawInvsDetails> ListWithoutDuplicate  = new ArrayList<RawInvsDetails>();
			List<Long> RawInvKeyList = new ArrayList<Long>();
			for (RawInvsDetails searchProcessRawRequestTableDto : listOfRecords) {
				if(null !=searchProcessRawRequestTableDto.getRawInvstigation().getKey()){
					if(!(RawInvKeyList).contains(searchProcessRawRequestTableDto.getRawInvstigation().getKey())){
						ListWithoutDuplicate.add(searchProcessRawRequestTableDto);
						RawInvKeyList.add(searchProcessRawRequestTableDto.getRawInvstigation().getKey());
					}
				}
			}
			for (RawInvsDetails rawInvsDetails : ListWithoutDuplicate) {
				String intimationNum = null != rawInvsDetails.getRawInvstigation().getIntimationNo() ? rawInvsDetails.getRawInvstigation().getIntimationNo() : null;
				Long rawInvsKey = null != rawInvsDetails.getRawInvstigation().getKey() ? rawInvsDetails.getRawInvstigation().getKey() : null;
				tableList.addAll(getIntimationData(intimationNum,rawInvsKey));

			}
			Page<SearchProcessRawRequestTableDto> page = new Page<SearchProcessRawRequestTableDto>();		
			searchFormDto.getPageable().setPageNumber(pageNumber + 1);
			if(tableList.size()<=10)
			{
				page.setHasNext(false);
			}
			else
			{
			page.setHasNext(true);
			}
			if (tableList.isEmpty()) {
				searchFormDto.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(tableList);
			page.setTotalRecords(tableList.size());
			page.setIsDbSearch(false);
	
			return page;

		} catch(Exception e){
			e.printStackTrace();
		}
			return null;
	}*/
	
public Page<SearchProcessRawRequestTableDto> search(SearchProcessRawRequestFormDto searchFormDTO,String userName,String password){
		
		List<RawInvsDetails> listOfRecords = new ArrayList<RawInvsDetails>();
		
		try{
			String intimationNo = searchFormDTO.getIntimationNo() != null && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo():null;
			String policyNo = searchFormDTO.getPolicyNo() != null && !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO.getPolicyNo(): null;
			String cpuCode = searchFormDTO.getCpuCode() != null && !searchFormDTO.getCpuCode().getValue().isEmpty() ? searchFormDTO.getCpuCode().getValue():null;
//			Long initiatedFrm = searchFormDTO.getIntiatedFrom() != null && searchFormDTO.getIntiatedFrom().getId() != null ? searchFormDTO.getIntiatedFrom().getId(): null;
			String initiatedFrm = searchFormDTO.getIntiatedFrom() != null && searchFormDTO.getIntiatedFrom().getValue() != null ? searchFormDTO.getIntiatedFrom().getValue(): null;
			String claimType = searchFormDTO.getClaimType() != null && !searchFormDTO.getClaimType().getValue().isEmpty() ? searchFormDTO.getClaimType().getValue(): null;
//			String hospitalCode = searchFormDTO.getHospitalCode() != null && !searchFormDTO.getHospitalCode().isEmpty() ? searchFormDTO.getHospitalCode(): null;
//			Long hospitalKey = searchFormDTO.getHospitalKey() != null ? searchFormDTO.getHospitalKey(): null;
			String priority = null != searchFormDTO &&  searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			
			/*RawInvsDetails*/			
			List<Map<String, Object>> taskProcedure = null ;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			workFlowMap= new WeakHashMap<Long, Object>();
			
			
			
			Integer totalRecords = 0; 
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.RAW_CURRENT_KEY);
			
			/*Check with sir later*/
//			mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
//			mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);
			
			List<String> intimationNoList = new ArrayList<String>();

			List<Long> rawKeyList = new ArrayList<Long>();

			if(null != intimationNo && !intimationNo.isEmpty()){
				
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);			

			
			}
			
			if(null != policyNo && !policyNo.isEmpty()){
							
				mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
				
			}	
			

			if(null != cpuCode){			
				mapValues.put(SHAConstants.CPU_CODE, cpuCode.split("-")[0]);
			}	
			
			if(null != initiatedFrm){
				
				mapValues.put(SHAConstants.STAGE_SOURCE, initiatedFrm.toUpperCase());
			}
			
			if(null != claimType && ! claimType.equals(""))
			{
				mapValues.put(SHAConstants.CLAIM_TYPE, claimType.toUpperCase());
			}
			
			/*if(null != hospitalKey && ! hospitalKey.equals(""))
			{
				mapValues.put(SHAConstants.HOSPITAL_KEY, hospitalKey);
			}*/
			
			
			if(priority != null && ! priority.isEmpty() && !priority.equalsIgnoreCase(SHAConstants.ALL))
				if(priority.equalsIgnoreCase(SHAConstants.NORMAL)){
					mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
				}else{
					mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
				}
			
			Pageable pageable = searchFormDTO.getPageable();
			if(pageable == null){
				pageable = new Pageable();
			}
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 20);
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	

			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {
					String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);

					/*Session session = (Session) entityManager.getDelegate();
					
					final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
					final CriteriaQuery<RawInvsDetails> criteriaQuery = criteriaBuilder.createQuery(RawInvsDetails.class);
					
					Root<RawInvsDetails> root = criteriaQuery.from(RawInvsDetails.class);
					
					List<Predicate> conditionList = new ArrayList<Predicate>();

					if(intimationNo != null){
					Predicate condition1 = criteriaBuilder.like(root.<RawInvsHeaderDetails>get("rawInvstigation").<String>get("intimationNo"), "%"+intimationNo+"%");
					Predicate condition2 = criteriaBuilder.equal(root.<Long>get("requestedStatus"),251);

					conditionList.add(condition1);
					conditionList.add(condition2);
					}
					
					criteriaQuery.select(root).where(
							criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
					
					final TypedQuery<RawInvsDetails> typedQuery = entityManager.createQuery(criteriaQuery);
					
					listOfRecords = typedQuery.getResultList();			
					RawInvsDetails rawInvsDetails = new RawInvsDetails();
					if(!listOfRecords.isEmpty()) {
						rawInvsDetails = listOfRecords.get(0);
					}*/
					
					RawInvsDetails rawInvsDetails = dbCalculationService.getRawDetailsByInitmationNo(intimationNumber,entityManager);	
					
					if(rawInvsDetails != null) {
						Long keyValue =  rawInvsDetails.getKey();
						workFlowMap.put(keyValue,outPutArray);
						intimationNoList.add(intimationNumber);	
						rawKeyList.add(keyValue);
					}						
					totalRecords = (Integer) outPutArray.get(SHAConstants.TOTAL_RECORDS);
					//IMSSUPPOR-28763
					//totalRecords = (Integer) intimationNoList.size();
				}
			}
			
			List<SearchProcessRawRequestTableDto> tableDTO = new ArrayList<SearchProcessRawRequestTableDto>();

			for(int index = 0; index < intimationNoList.size(); index++){
				 if(index < rawKeyList.size()){
					 intimationNo = intimationNoList.get(index);
					 Long rawKey = rawKeyList.get(index);
					 tableDTO.addAll(getIntimationData(intimationNo, rawKey,workFlowMap.get(rawKey)));
					 
				 }
			}
			
			
			
			  

//			final TypedQuery<RawInvsDetails> typedQuery = entityManager.createQuery(criteriaQuery);
			
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			if (pageNumber > 1) 
			{
				firtResult = (pageNumber - 1) * 10;
			} 
			else 
			{
				firtResult = 0;
			}

			Page<SearchProcessRawRequestTableDto> page = new Page<SearchProcessRawRequestTableDto>();		
			searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
			if(tableDTO.size()<=10)
			{
				page.setHasNext(false);
			}
			else
			{
			page.setHasNext(true);
			}
			if (tableDTO.isEmpty()) {
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(tableDTO);
			page.setTotalRecords(totalRecords);
			//IMSSUPPOR-28763
			//page.setTotalRecords(tableDTO.size());
			page.setIsDbSearch(false);
	
			return page;

		} catch(Exception e){
			e.printStackTrace();
		}
			return null;
}
	
	@SuppressWarnings("unused")
	private List<SearchProcessRawRequestTableDto> getIntimationData(String intimationNo,Long key, Object object){
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
		
		Root<Intimation> root = criteriaQuery.from(Intimation.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
			
		if(intimationNo != null || (intimationNo != null && !intimationNo.isEmpty())){
		Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationId"), intimationNo);
		conditionList.add(condition1);
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}else {
				return null;
			}
		
			final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
			List<Intimation> listIntimations = new ArrayList<Intimation>(); 
			listIntimations = typedQuery.getResultList();
		
			for(Intimation inti:listIntimations){
				System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}

			List<SearchProcessRawRequestTableDto> tableDTO = SearchProcessRawRequestMapper.getInstance().getClaimDTO( listIntimations);
			for (SearchProcessRawRequestTableDto searchCreateRODTableDTO : tableDTO) {
			
				Claim claimByKey = getClaimByIntimation(searchCreateRODTableDTO.getIntimationKey());
			
				if(claimByKey != null) {
					searchCreateRODTableDTO.setKey(key);
					searchCreateRODTableDTO.setClaimKey(claimByKey.getKey());
					searchCreateRODTableDTO.setCoverCode(claimByKey.getClaimCoverCode());
					searchCreateRODTableDTO.setSubCoverCode(claimByKey.getClaimSubCoverCode());
					searchCreateRODTableDTO.setSectionCode(claimByKey.getClaimSectionCode());
					searchCreateRODTableDTO.setClaimType(claimByKey.getClaimType().getValue());
					if(claimByKey.getLobId() != null &&
							(claimByKey.getLobId().equals(ReferenceTable.HEALTH_LOB_KEY) ||claimByKey.getLobId().equals(ReferenceTable.PACKAGE_MASTER_VALUE))){
						searchCreateRODTableDTO.setClassOfBusiness(SHAConstants.HEALTH_LOB);
					} else {
						searchCreateRODTableDTO.setClassOfBusiness(SHAConstants.PA_LOB);
					}
				}
				searchCreateRODTableDTO.setWorkFlowObject(object);
			}
		
				tableDTO = getclaimNumber(tableDTO);
				tableDTO = getHospitalDetails(tableDTO,key);
			
			
				return tableDTO;
		
	}catch(Exception e){
		return null;
	}	
	}
	
	private List<SearchProcessRawRequestTableDto> getclaimNumber(List<SearchProcessRawRequestTableDto> intimationList){
		Claim a_claim = null;
		for(int index = 0; index < intimationList.size(); index++){
			System.out.println("Intimationkey+++++++++++++++++++++"+intimationList.get(index).getKey());
			
			if (intimationList.get(index).getKey() != null) {

				Query findByIntimationKey = entityManager
						.createNamedQuery("Claim.findByIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter(
						"intimationKey", intimationList.get(index).getKey());
				try{
						a_claim = (Claim) findByIntimationKey.getSingleResult();
					
						if(a_claim != null){
							entityManager.refresh(a_claim);

							intimationList.get(index).setClaimType(a_claim.getClaimType().getValue());
	
						}else{

						}	
				}catch(Exception e){
					continue;
				}
			} 
		}
		return intimationList;
	}
	
	public Claim getClaimByIntimation(Long intimationKey) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {

				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				
				for (Claim claim : a_claimList) {
					entityManager.refresh(claim);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else {
			// intimationKey null
		}
		return a_claimList.get(0);

	}
	
	private List<SearchProcessRawRequestTableDto> getHospitalDetails(
			List<SearchProcessRawRequestTableDto> tableDTO,Long key) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			if(null != key){
				RawInvsDetails invesDtls = getRawInvestDetails(key);
				if(invesDtls != null){
					tableDTO.get(index).setRequestDate(invesDtls.getCreatedDate());
					tableDTO.get(index).setCategory(invesDtls.getRawCategory().getCategoryDescription());
					tableDTO.get(index).setRequestStageId(invesDtls.getRequestedStage());
					tableDTO.get(index).setRepliedStatusId(invesDtls.getRequestedStatus());
				}
			}
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalId());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				// tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				// tableDTO.get(index).setCpuCode(String.valueOf(getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode()));
				 tableDTO.get(index).setHospitalCode(hospitalDetail.getHospitalCode());

			 }
			 
			}catch(Exception e){
				continue;
			}
		
		}
				
		return tableDTO;
	}
	
	@SuppressWarnings("unchecked")
	public RawInvsDetails getRawInvestDetails(Long invKey) {
			RawInvsDetails rawdtls = null;
			Query findByInvKey = entityManager
					.createNamedQuery("RawInvsDetails.findAll");
					//.createNamedQuery("RawInvsDetails.findByRawInvKey");
			findByInvKey = findByInvKey.setParameter(
					/*"rawInvkey", invKey);*/
					"key", invKey);
			/*List<Long> statusList = new ArrayList<Long>();
			statusList.add(ReferenceTable.ESCLATE_TO_RAW);
			findByInvKey.setParameter("statusList", statusList);*/
		 	List<RawInvsDetails> rawList = findByInvKey.getResultList();
	    	if(null != rawList && !rawList.isEmpty())
	    	{
	    		entityManager.refresh(rawList.get(0));
	    		return rawList.get(0);
	    	}

		return null;

	}
	
	@SuppressWarnings("unchecked")
	public RawInvsDetails getRawInvestDetailsByKey(Long key) {
			RawInvsDetails rawdtls = null;
			Query findByInvKey = entityManager
					.createNamedQuery("RawInvsDetails.findAll");
			findByInvKey = findByInvKey.setParameter(
					"key", key);
		 	List<RawInvsDetails> rawList = findByInvKey.getResultList();
	    	if(null != rawList && !rawList.isEmpty())
	    	{
	    		entityManager.refresh(rawList.get(0));
	    		return rawList.get(0);
	    	}

		return null;

	}
	
	public void submitRepliedData(List<RawInitiatedRequestDTO> repliedData,String userName){
		
		if(repliedData != null){
			for (RawInitiatedRequestDTO rawInitiatedRequestDTO : repliedData) {
				if(rawInitiatedRequestDTO.getRawinvestigationKey() != null){
					RawInvsDetails invDetails = getRawInvestDetailsByKey(rawInitiatedRequestDTO.getRawinvestigationKey());
					if(invDetails != null){
						invDetails.setRedolutionType(rawInitiatedRequestDTO.getResolutionfromRaw().getValue());
						invDetails.setRepliedRemarks(rawInitiatedRequestDTO.getRemarksfromRaw());
						invDetails.setRepliedBy(userName);
						invDetails.setRepliedDate(new Timestamp(System.currentTimeMillis()));
						invDetails.setRequestedStatus(ReferenceTable.RAW_TEAM_REPLIED);
						entityManager.merge(invDetails);
						entityManager.flush();
						
					}
				}
			}
			RawInitiatedRequestDTO rawDTO = repliedData.get(0);
			Map<String, Object> wrkFlowMapSubmit = (Map<String, Object>) rawDTO.getWorkFlowObject();
			
//			Map<String, Object> wrkFlowMapSubmit = (Map<String, Object>) rawDTO.getDbOutArray();
//			Map<String, Object> wrkFlowMapSubmit = new WeakHashMap<String, Object>() ;

//			String intimationNumber = (String) workflowObject.get(SHAConstants.INTIMATION_NO);

//			wrkFlowMapSubmit.put(SHAConstants.INTIMATION_NO, intimationNumber);
				
			wrkFlowMapSubmit.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_REPLIED_RAW);
				
			

			try{
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMapSubmit);
				
				DBCalculationService dbCalService = new DBCalculationService();

				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}


}
