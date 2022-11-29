package com.shaic.claim.cvc.auditqueryapproval;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.AuditCategory;
import com.shaic.arch.table.AuditDetails;
import com.shaic.arch.table.AuditProcessor;
import com.shaic.arch.table.AuditTeam;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.OMPProcessOmpClaimApprover.search.OMPProcessOmpClaimApproverTableDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionFormDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimAuditQuery;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search.SearchAcknowledgementDocumentReceiverMapper;
import com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search.SearchAcknowledgementDocumentReceiverTableDTO;


@Stateless
public class SearchCVCAuditQryApprovalService extends AbstractDAO<Preauth>{
	
	@Inject
	private IntimationService intimationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public SearchCVCAuditQryApprovalService() {
		super();
	}
	
	
	@SuppressWarnings({ "null" })
	public Page<SearchCVCAuditActionTableDTO> search(
			SearchCVCAuditActionFormDTO searchFormDTO, String userName, String passWord) {
		
		List<AuditDetails> listIntimations = new ArrayList<AuditDetails>();
		List<SearchCVCAuditActionTableDTO> tableDtoList = new ArrayList<SearchCVCAuditActionTableDTO>();
		Integer totalRecords = 0; 
		
		try{
			String intimationNo =  null != searchFormDTO.getIntimationNumber() && !searchFormDTO.getIntimationNumber().isEmpty() ? searchFormDTO.getIntimationNumber() :null;
			String auditStatus =  null != searchFormDTO.getCmbauditStatus() &&searchFormDTO.getCmbauditStatus().getValue()!=null  && !searchFormDTO.getCmbauditStatus().getValue().isEmpty() ? searchFormDTO.getCmbauditStatus().getValue() : null;
			String userId =  null != searchFormDTO.getUserId() && !searchFormDTO.getUserId().isEmpty() ? searchFormDTO.getUserId() : null;
			String clmType = null != searchFormDTO.getClmType() ? (searchFormDTO.getClmType().getId() != null && 
					searchFormDTO.getClmType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) ? "C" : 
						(searchFormDTO.getClmType().getId() != null && searchFormDTO.getClmType().getId().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY) ? "R" : "")) :"";
			String year =  null != searchFormDTO.getYear() && searchFormDTO.getYear().getValue()!=null  && !searchFormDTO.getYear().getValue().isEmpty() ? searchFormDTO.getYear().getValue() : null;
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<AuditDetails> criteriaQuery = criteriaBuilder.createQuery(AuditDetails.class);
			String empid="";
			if(userId!=null){
				 empid= userId.toLowerCase();
			}
			Root<AuditDetails> root = criteriaQuery.from(AuditDetails.class);
			List<Long> intimatListKey= new ArrayList<Long>();
			List<Predicate> conditionList = new ArrayList<Predicate>();
			if(intimationNo!=null){
				List<Intimation> intimationObj= getIntimationByNo(intimationNo);
				if(intimationObj!=null && !intimationObj.isEmpty()){
					for(Intimation intimationObj1 : intimationObj){
						intimatListKey.add(intimationObj1.getKey());
					}
				}
				Predicate condition1 =  root.<Long>get("intimationKey").in(intimatListKey); 
				conditionList.add(condition1);
			}
			
			if(clmType != null && !clmType.isEmpty()){
				Predicate clmcondition = criteriaBuilder.like(root.<String>get("claimType"), clmType);
				conditionList.add(clmcondition);
			}
			
			Predicate condition2 = criteriaBuilder.like(root.<String>get("remediationStatus"), "%"+SHAConstants.AUDIT_QUERY_APPROVAL_PENDING+"%");
			conditionList.add(condition2);
		
			if(userId != null){
				Predicate condition3 = criteriaBuilder.like(root.<String>get("createdBy"), "%"+empid+"%");
				conditionList.add(condition3);
			}
			
			/*Predicate condition9 = criteriaBuilder.equal(root.<String>get("auditStatus"), SHAConstants.ERROR_STRING);
			
			conditionList.add(condition9);*/
			
			Predicate condition10 = criteriaBuilder.equal(root.<String>get("activeStatus"), SHAConstants.YES_FLAG);
			
			conditionList.add(condition10);
			
			Predicate condition11 = criteriaBuilder.equal(root.<String>get("cvcLockFlag"), SHAConstants.CVC_LOCK_FLAG);
			
			conditionList.add(condition11);
					
			if(intimationNo == null && auditStatus == null && userId == null){
				criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
				} else{
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}
			final TypedQuery<AuditDetails> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			if(pageNumber > 1){
				firtResult = (pageNumber-1) *10;
			}else{
				firtResult = 0;
			}

			if(intimationNo == null && auditStatus == null && userId == null /*&& listIntimations.size()>10*/){
				listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			}else{
				listIntimations = typedQuery.getResultList();
			}
			totalRecords = typedQuery.getResultList().size();

			List<AuditDetails> doList = listIntimations; 	
		
				for(AuditDetails auditDetails : listIntimations) {
				
				SearchCVCAuditActionTableDTO tableDto = new SearchCVCAuditActionTableDTO();
				
				if (null != auditDetails && auditDetails.getKey() != null ) {
					tableDto.setAuditKey(auditDetails.getKey());
				
			
				if (null != auditDetails && auditDetails.getClaimType() != null && auditDetails.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS_CHAR)){
					Preauth cashless = getPreauthListByKey(auditDetails.getTransactionKey());
					tableDto.setTransactionKey(cashless.getKey());
					tableDto.setTransactionNumber(cashless.getPreauthId());
					tableDto.setClaimType(SHAConstants.CASHLESS_STRING);
					tableDto.setClaimKey(cashless.getClaim().getKey());
					tableDto.setStatus(auditDetails.getRemediationStatus());
				}
				
				if (null != auditDetails && auditDetails.getClaimType() != null  && auditDetails.getClaimType().equalsIgnoreCase(SHAConstants.REIMBURSEMENT_CHAR)){
					Reimbursement reimbursement = getReimbursementObjectByKey(auditDetails.getTransactionKey());
					tableDto.setTransactionKey(reimbursement.getKey());
					tableDto.setTransactionNumber(reimbursement.getRodNumber());
					tableDto.setClaimType(SHAConstants.REIMBURSEMENT);
					tableDto.setClaimKey(reimbursement.getClaim().getKey());
					tableDto.setStatus(auditDetails.getRemediationStatus());
				}
				
				Intimation intimation = intimationService.getIntimationByKey(auditDetails.getIntimationKey());
				
				tableDto.setIntimationNumber(intimation.getIntimationId());
//				tableDto.setIntimationKey(auditDetails.getIntimationKey());
				tableDto.setIntimationKey(intimation.getKey());
				tableDto.setAuditBy(auditDetails.getCreatedBy());
				tableDto.setAuditDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(auditDetails.getCreatedDate()));
				if(auditDetails.getRemediationStatus()==null){
					tableDto.setStatus("Pending");
				}
				if(year != null && intimation.getIntimationId() != null){
					String[] intimationSp = intimation.getIntimationId().split("/");
					String intYear = intimationSp[1];
					if(intYear != null && year.equals(intYear)){
						tableDtoList.add(tableDto);
					}
				} else {
					tableDtoList.add(tableDto);
				}
				
			}
			}
			

			List<SearchCVCAuditActionTableDTO> result = new ArrayList<SearchCVCAuditActionTableDTO>();
			result.addAll(tableDtoList);
			Page<SearchCVCAuditActionTableDTO> page = new Page<SearchCVCAuditActionTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber+1);
			page.setHasNext(true);
			if(result.isEmpty()){
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageItems(result);
			page.setIsDbSearch(false);
			page.setTotalRecords(totalRecords);
			
			return page;
			
		}
	catch(Exception e){
		e.printStackTrace();
	}
		
	return null;
	}
		
		
	@Override
	public Class<Preauth> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<AuditDetails> getCVCAuditActionIntimationList(Long intimationKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByKeyWithPending");
		query.setParameter("intimationKey", intimationKey);
		query.setParameter("remediationStatus", SHAConstants.CVC_PENDING);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList;
		}

		return null;

	}
	
	@SuppressWarnings("unchecked")
	public Preauth getPreauthListByKey(Long preauthKey) {

		Query findByKey = entityManager.createNamedQuery("Preauth.findByKey")
				.setParameter("preauthKey", preauthKey);
		List<Preauth> preauthList = (List<Preauth>) findByKey.getResultList();
		if (null != preauthList && !preauthList.isEmpty()) {
			return preauthList.get(0);

		}
		return null;
	}
	
	
	public Reimbursement getReimbursementObjectByKey(Long key) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", key);		
		List<Reimbursement> reimbursementList = (List<Reimbursement>) query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			return reimbursementList.get(0);
		}
		return null;
	}
	
	public List<AuditDetails> getCVCAuditActionIntimationList()
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByRemediationStatus");
		query.setParameter("remediationStatus", SHAConstants.CVC_PENDING);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList;
		}

		return null;

	}
	
	@SuppressWarnings("unused")
	public void submitCVCAuditDetails(SearchCVCAuditActionTableDTO cvcTableDto){
			
		// comment for support fix isue
		/*	AuditDetails auditDetails = getCVCAuditActionIntimation(cvcTableDto.getIntimationKey());*/
		
		if(cvcTableDto.getAuditKey() != null){
		AuditDetails auditDetails = getCVCAuditActionAuditKey(cvcTableDto.getAuditKey());
		
		
			if(null != auditDetails){
//				auditDetails.setIntimationKey(cvcTableDto.getIntimationKey());
//				auditDetails.setClaimKey(cvcTableDto.getClaimKey());
//				auditDetails.setTransactionKey(cvcTableDto.getTransactionKey());
//				auditDetails.setClaimType(cvcTableDto.getClaimType());
//				auditDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
//				auditDetails.setCreatedBy(cvcTableDto.getUsername());
//				auditDetails.setActiveStatus(SHAConstants.YES_FLAG);
//				auditDetails.setAuditRemarks(cvcTableDto.getAuditRemarks());
//				auditDetails.setAuditStatus(cvcTableDto.getAuditStatus());
//				auditDetails.setErrorTeam(cvcTableDto.getTeam());
//				auditDetails.setErrorCategory(cvcTableDto.getErrorCategory());
				auditDetails.setMonetaryResult(cvcTableDto.getMonetaryResult());
//				auditDetails.setErrorProcessor(cvcTableDto.getProcessor());
				auditDetails.setAmountInvolved(Long.parseLong(cvcTableDto.getAmountInvolved()));
				auditDetails.setRemediationStatus(cvcTableDto.getRemediationStatus());
				auditDetails.setRemediationRemarks(cvcTableDto.getRemediationRemarks());
				auditDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				auditDetails.setModifiedBy(cvcTableDto.getUsername());
				auditDetails.setFinalAuditStatus(cvcTableDto.getAuditFinalStatus());
				auditDetails.setCvcLockFlag(0);
				auditDetails.setLockedBy(null);
				auditDetails.setLockedDate(null);
				
				if((auditDetails.getRemediationStatus() != null && auditDetails.getRemediationStatus().equalsIgnoreCase(SHAConstants.COMPLETED_STRING))
						|| (auditDetails.getFinalAuditStatus() != null && auditDetails.getFinalAuditStatus().equalsIgnoreCase(SHAConstants.NO_ERROR_STRING))) {
					
					auditDetails.setCompletedDate(new Timestamp(System.currentTimeMillis()));
					auditDetails.setOutcome(cvcTableDto.getQryOutcome());
					auditDetails.setOutcomeRemarks(cvcTableDto.getSatisUnSatisRemarks());
					auditDetails.setCompletedReason(cvcTableDto.getCompletedReason());
					auditDetails.setCompletedRemarks(cvcTableDto.getCompletedRemarks());
					auditDetails.setRemediationStatus(SHAConstants.AUDIT_QUERY_COMPLETED);
				}
				
				
				entityManager.merge(auditDetails);
				entityManager.flush();
				entityManager.clear();
				
				if(auditDetails.getKey() != null && null != cvcTableDto && cvcTableDto.getTeam() != null) {
					List<String> dtoTeamList = getListFromMultiSelectComponent(cvcTableDto.getTeam());
					List<AuditTeam> dbTeamList = getSelectedTeam(auditDetails.getKey());
					
					if (null != dbTeamList && !dbTeamList.isEmpty()) {
						for (AuditTeam dbTeam : dbTeamList) {
							Boolean isDeleted = Boolean.TRUE;
							for(String dtoTeam : dtoTeamList){
								
								if(dtoTeam.equalsIgnoreCase(dbTeam.getAuditTeam())){
									isDeleted = Boolean.FALSE;
									break;
								}
							}
							if(isDeleted){
								dbTeam.setActiveStatus(SHAConstants.N_FLAG);
								dbTeam.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								dbTeam.setModifiedBy(cvcTableDto.getUsername());
								entityManager.merge(dbTeam);
								entityManager.flush();
								entityManager.clear();
							}
							
						}

						List<String> dbList= new ArrayList<String>();
						for(AuditTeam dbTeam : dbTeamList){
							dbList.add(dbTeam.getAuditTeam());
						}

						
						for (String dtoTeam : dtoTeamList) {
								
							if(!dbList.contains(dtoTeam)){
								
								AuditTeam auditTeam = new AuditTeam();
								auditTeam.setAuditKey(auditDetails.getKey());
								auditTeam.setAuditTeam(dtoTeam);
								auditTeam.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								auditTeam.setCreatedBy(cvcTableDto.getUsername());
								auditTeam.setActiveStatus(SHAConstants.YES_FLAG);
								entityManager.persist(auditTeam);
								entityManager.flush();
								entityManager.clear();
								cvcTableDto.setAuditKey(auditDetails.getKey());
							}
							saveAuditQuery(auditDetails,dtoTeam,cvcTableDto);
						}
					}else {
						for (String dtoTeam : dtoTeamList) {
								
								AuditTeam auditTeam = new AuditTeam();
								auditTeam.setAuditKey(auditDetails.getKey());
								auditTeam.setAuditTeam(dtoTeam);
								auditTeam.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								auditTeam.setCreatedBy(cvcTableDto.getUsername());
								auditTeam.setActiveStatus(SHAConstants.YES_FLAG);
								entityManager.persist(auditTeam);
								entityManager.flush();
								entityManager.clear();
								cvcTableDto.setAuditKey(auditDetails.getKey());
								saveAuditQuery(auditDetails,dtoTeam,cvcTableDto);
						}
					}
				}
				
				if(auditDetails.getKey() != null && null != cvcTableDto && cvcTableDto.getErrorCategory() != null) {
					List<String> dtoCategoryList = getListFromMultiSelectComponent(cvcTableDto.getErrorCategory());
					List<AuditCategory> dbCategoryList = getSelectedCategory(auditDetails.getKey());
					
					if (null != dbCategoryList && !dbCategoryList.isEmpty()) {
						for (AuditCategory dbCategory : dbCategoryList) {
							Boolean isDeleted = Boolean.TRUE;
							for(String dtoCategory : dtoCategoryList){
								
								if(dtoCategory.equalsIgnoreCase(dbCategory.getAuditCategory().toString())){
									isDeleted = Boolean.FALSE;
									break;
								}
							}
							if(isDeleted){
								dbCategory.setActiveStatus(SHAConstants.N_FLAG);
								dbCategory.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								dbCategory.setModifiedBy(cvcTableDto.getUsername());
								entityManager.merge(dbCategory);
								entityManager.flush();
								entityManager.clear();
							}
							
						}
	
						List<String> dbList= new ArrayList<String>();
						for(AuditCategory dbCategory : dbCategoryList){
							dbList.add(dbCategory.getAuditCategory().toString());
						}
							
						for (String dtoCategory : dtoCategoryList) {
								
							if(!dbList.contains(dtoCategory)){
								
								AuditCategory auditCategory = new AuditCategory();
								auditCategory.setAuditKey(auditDetails.getKey());
								auditCategory.setAuditCategory(Long.parseLong(dtoCategory));
								if (null != auditCategory && auditCategory.getAuditCategory() != null 
										&& auditCategory.getAuditCategory().toString().contains(SHAConstants.CVC_OTHERS_MAS_KEY)) {
									auditCategory.setAuditCategoryOthrRmks(cvcTableDto.getOtherRemarks());
								}
								auditCategory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								auditCategory.setCreatedBy(cvcTableDto.getUsername());
								auditCategory.setActiveStatus(SHAConstants.YES_FLAG);
								entityManager.persist(auditCategory);
								entityManager.flush();
								entityManager.clear();
								
							}
						}
					}else {
						for (String dtoCategory : dtoCategoryList) {
								
								AuditCategory auditCategory = new AuditCategory();
								auditCategory.setAuditKey(auditDetails.getKey());
								auditCategory.setAuditCategory(Long.parseLong(dtoCategory));
								if (null != auditCategory && auditCategory.getAuditCategory() != null 
										&& auditCategory.getAuditCategory().toString().contains(SHAConstants.CVC_OTHERS_MAS_KEY)) {
									auditCategory.setAuditCategoryOthrRmks(cvcTableDto.getOtherRemarks());
								}
								auditCategory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								auditCategory.setCreatedBy(cvcTableDto.getUsername());
								auditCategory.setActiveStatus(SHAConstants.YES_FLAG);
								entityManager.persist(auditCategory);
								entityManager.flush();
								entityManager.clear();
						}
					}
				}
				
				if(auditDetails.getKey() != null && null != cvcTableDto && cvcTableDto.getProcessor() != null) {
					List<String> dtoProcessorList = getListFromMultiSelectComponent(cvcTableDto.getProcessor());
					List<AuditProcessor> dbProcessorList = getSelectedProcessor(auditDetails.getKey());
					
					if (null != dbProcessorList && !dbProcessorList.isEmpty()) {
						for (AuditProcessor dbProcessor : dbProcessorList) {
							Boolean isDeleted = Boolean.TRUE;
							for(String dtoProcessor : dtoProcessorList){
								
								if(dtoProcessor.equalsIgnoreCase(dbProcessor.getAuditProcessor())){
									isDeleted = Boolean.FALSE;
									break;
								}
							}
							if(isDeleted){
								dbProcessor.setActiveStatus(SHAConstants.N_FLAG);
								dbProcessor.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								dbProcessor.setModifiedBy(cvcTableDto.getUsername());
								entityManager.merge(dbProcessor);
								entityManager.flush();
								entityManager.clear();
							}
							
						}
	
						List<String> dbList= new ArrayList<String>();
						for(AuditProcessor dbProcessor : dbProcessorList){
							dbList.add(dbProcessor.getAuditProcessor());
						}
							
						for (String dtoProcessor : dtoProcessorList) {
								
							if(!dbList.contains(dtoProcessor)){
								
								AuditProcessor auditProcessor = new AuditProcessor();
								auditProcessor.setAuditKey(auditDetails.getKey());
								auditProcessor.setAuditProcessor(dtoProcessor);
								auditProcessor.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								auditProcessor.setCreatedBy(cvcTableDto.getUsername());
								auditProcessor.setActiveStatus(SHAConstants.YES_FLAG);
								entityManager.persist(auditProcessor);
								entityManager.flush();
								entityManager.clear();
								
								
							}
						}
					}else {
						for (String dtoProcessor : dtoProcessorList) {
								
								AuditProcessor auditProcessor = new AuditProcessor();
								auditProcessor.setAuditKey(auditDetails.getKey());
								auditProcessor.setAuditProcessor(dtoProcessor);
								auditProcessor.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								auditProcessor.setCreatedBy(cvcTableDto.getUsername());
								auditProcessor.setActiveStatus(SHAConstants.YES_FLAG);
								entityManager.persist(auditProcessor);
								entityManager.flush();
								entityManager.clear();
						}
					}
				}
			}
		}
	}
	
	public AuditDetails getCVCAuditActionIntimation(Long intimationKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByKey");
		query.setParameter("intimationKey", intimationKey);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList.get(0);
		}

		return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<Intimation> getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNo").setParameter(
				"intimationNo", "%"+intimationNo+"%");

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			return intimationList;
		}
		return null;
	}
	
	public List<String> getListFromMultiSelectComponent(Object object){
		
		String string = object.toString();
		if(!string.equals("[]")){
			String temp[] = string.split(",");
			List<String> listOfString = new ArrayList<String>();
			listOfString.clear();
			for (int i = 0; i < temp.length; i++) {
				String val = temp[i].replaceAll("\\[", "").replaceAll("\\]", "");
				listOfString.add(val.trim());
			}
			return listOfString;
		}
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<AuditTeam> getSelectedTeam(Long auditKey) {
		Query findByKey = entityManager
				.createNamedQuery("AuditTeam.findByAuditActiveStatus");
		findByKey.setParameter("auditKey", auditKey);

		List<AuditTeam> auditTeamList = (List<AuditTeam>) findByKey
				.getResultList();

		if (!auditTeamList.isEmpty()) {
			return auditTeamList;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<AuditCategory> getSelectedCategory(Long auditKey) {
		Query findByKey = entityManager
				.createNamedQuery("AuditCategory.findByAuditActiveStatus");
		findByKey.setParameter("auditKey", auditKey);

		List<AuditCategory> auditCategoryList = (List<AuditCategory>) findByKey
				.getResultList();

		if (!auditCategoryList.isEmpty()) {
			return auditCategoryList;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<AuditProcessor> getSelectedProcessor(Long auditKey) {
		Query findByKey = entityManager
				.createNamedQuery("AuditProcessor.findByAuditActiveStatus");
		findByKey.setParameter("auditKey", auditKey);

		List<AuditProcessor> auditProcessorList = (List<AuditProcessor>) findByKey
				.getResultList();

		if (!auditProcessorList.isEmpty()) {
			return auditProcessorList;
		}
		return null;
	}
	
	public void updateCVCLockKey(SearchCVCAuditActionTableDTO key) {
		if(null != key){
			AuditDetails auditDetails = getCVCAuditActionKeyData(key);
			if(null != auditDetails){
				auditDetails.setCvcLockFlag(1);
				auditDetails.setLockedBy(key.getUsername());
				auditDetails.setLockedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(auditDetails);
				entityManager.flush();
				entityManager.clear();
			}
			
		}
	}
	
	public void updateCVCLockKeyForCancel(SearchCVCAuditActionTableDTO key) {
		if(null != key){
			AuditDetails auditDetails = getCVCAuditActionKeyData(key);
			if(null != auditDetails){
				auditDetails.setCvcLockFlag(0);
				auditDetails.setLockedBy(null);
				auditDetails.setLockedDate(null);
				entityManager.merge(auditDetails);
				entityManager.flush();
				entityManager.clear();
			}
			
		}
	}
	
	public AuditDetails getCVCAuditActionKeyData(SearchCVCAuditActionTableDTO auditKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByAuditKey");
		query.setParameter("key", auditKey.getAuditKey());

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList.get(0);
		}

		return null;

	}
	public void saveAuditQuery(AuditDetails auditDetails, String dtoTeam, SearchCVCAuditActionTableDTO cvcTableDto) {
		if (null != auditDetails && null != auditDetails.getAuditStatus() 
				&& auditDetails.getAuditStatus().equalsIgnoreCase(SHAConstants.ERROR_STRING)
				&& null != auditDetails.getRemediationStatus()
				&& (!auditDetails.getRemediationStatus().toLowerCase().equalsIgnoreCase(SHAConstants.COMPLETED_STRING))) {
			
			ClaimAuditQuery clmAudQry = null; 
			if(dtoTeam.trim().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_CASHLESS)){
				List<SearchCVCAuditClsQryTableDTO> clsqryList = cvcTableDto.getClsQryList();
				if(clsqryList != null && !clsqryList.isEmpty()) {
					for (SearchCVCAuditClsQryTableDTO searchCVCAuditClsQryTableDTO : clsqryList) {
						if(searchCVCAuditClsQryTableDTO.getQryKey() == null
								 &&    (searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING))) {
							clmAudQry = new ClaimAuditQuery();
						}
						else if(searchCVCAuditClsQryTableDTO.getQryKey() != null
								&& (searchCVCAuditClsQryTableDTO.getQryStatus() != null
								 &&    (searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_APPROVAL_PENDING)
								    		 || searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING)))){
							clmAudQry = findAudQryByKey(searchCVCAuditClsQryTableDTO.getQryKey());	
							auditDetails.setRemediationStatus(SHAConstants.AUDIT_QUERY_REPLY_PENDING);
						}else if(searchCVCAuditClsQryTableDTO.getQryKey() != null
								&& (searchCVCAuditClsQryTableDTO.getQryStatus() != null
								 &&    (searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED)))){
							clmAudQry = findAudQryByKey(searchCVCAuditClsQryTableDTO.getQryKey());		
						}
						if(clmAudQry != null) {
							clmAudQry.setFinalisedRemarks(searchCVCAuditClsQryTableDTO.getQryFinalRemarks());
							clmAudQry.setQueryRemarks(searchCVCAuditClsQryTableDTO.getQryFinalRemarks());
							clmAudQry.setStatus(searchCVCAuditClsQryTableDTO.getQryStatus());
							if(SHAConstants.AUDIT_QUERY_REPLY_PENDING.equalsIgnoreCase(clmAudQry.getStatus())) {
								clmAudQry.setFinalisedStatus(SHAConstants.AUDIT_QUERY_ACCEPTED);
							}
							else {
								clmAudQry.setFinalisedStatus(SHAConstants.AUDIT_QUERY_NOT_ACCEPTED);
							}
						}
						
					}
					
				}
			}
			else if(dtoTeam.trim().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_MEDICAL)){
				List<SearchCVCAuditClsQryTableDTO> MedicalqryList = cvcTableDto.getMedicalQryList();
				for (SearchCVCAuditClsQryTableDTO searchCVCAuditMedicalQryTableDTO : MedicalqryList) {
					if(searchCVCAuditMedicalQryTableDTO.getQryKey() == null
							 &&    (searchCVCAuditMedicalQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING))) {
						clmAudQry = new ClaimAuditQuery();
					}
					else if(searchCVCAuditMedicalQryTableDTO.getQryKey() != null
							&& (searchCVCAuditMedicalQryTableDTO.getQryStatus() != null
							 &&    (searchCVCAuditMedicalQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_APPROVAL_PENDING)
							    		 || searchCVCAuditMedicalQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING)))){
						clmAudQry = findAudQryByKey(searchCVCAuditMedicalQryTableDTO.getQryKey());		
						auditDetails.setRemediationStatus(SHAConstants.AUDIT_QUERY_REPLY_PENDING);
					}else if(searchCVCAuditMedicalQryTableDTO.getQryKey() != null
							&& (searchCVCAuditMedicalQryTableDTO.getQryStatus() != null
							 &&    (searchCVCAuditMedicalQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED)))){
						clmAudQry = findAudQryByKey(searchCVCAuditMedicalQryTableDTO.getQryKey());		
					}
					if(clmAudQry != null) {
						clmAudQry.setFinalisedRemarks(searchCVCAuditMedicalQryTableDTO.getQryFinalRemarks());
						clmAudQry.setQueryRemarks(searchCVCAuditMedicalQryTableDTO.getQryFinalRemarks());
						clmAudQry.setStatus(searchCVCAuditMedicalQryTableDTO.getQryStatus());
						if(SHAConstants.AUDIT_QUERY_REPLY_PENDING.equalsIgnoreCase(clmAudQry.getStatus())) {
							clmAudQry.setFinalisedStatus(SHAConstants.AUDIT_QUERY_ACCEPTED);
						}
						else {
							clmAudQry.setFinalisedStatus(SHAConstants.AUDIT_QUERY_NOT_ACCEPTED);
						}
					}	
				}	
			}
			else if(dtoTeam.trim().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_BILLING) || dtoTeam.trim().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_FINANCIAL)){
				List<SearchCVCAuditClsQryTableDTO> billingFaqryList = cvcTableDto.getBillingFaQryList();
				for (SearchCVCAuditClsQryTableDTO searchCVCAuditbillingFaQryTableDTO : billingFaqryList) {
					if(searchCVCAuditbillingFaQryTableDTO.getQryKey() == null
							 &&    (searchCVCAuditbillingFaQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING))) {
						clmAudQry = new ClaimAuditQuery();
					}
					else if(searchCVCAuditbillingFaQryTableDTO.getQryKey() != null
							&& (searchCVCAuditbillingFaQryTableDTO.getQryStatus() != null
							 &&    (searchCVCAuditbillingFaQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_APPROVAL_PENDING)
							    		 || searchCVCAuditbillingFaQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING)))){
						clmAudQry = findAudQryByKey(searchCVCAuditbillingFaQryTableDTO.getQryKey());	
						auditDetails.setRemediationStatus(SHAConstants.AUDIT_QUERY_REPLY_PENDING);
					}else if(searchCVCAuditbillingFaQryTableDTO.getQryKey() != null
							&& (searchCVCAuditbillingFaQryTableDTO.getQryStatus() != null
							 &&    (searchCVCAuditbillingFaQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED)))){
						clmAudQry = findAudQryByKey(searchCVCAuditbillingFaQryTableDTO.getQryKey());		
					}
					if(clmAudQry != null) {
						clmAudQry.setFinalisedRemarks(searchCVCAuditbillingFaQryTableDTO.getQryFinalRemarks());
						clmAudQry.setQueryRemarks(searchCVCAuditbillingFaQryTableDTO.getQryFinalRemarks());
						clmAudQry.setStatus(searchCVCAuditbillingFaQryTableDTO.getQryStatus());
						if(SHAConstants.AUDIT_QUERY_REPLY_PENDING.equalsIgnoreCase(clmAudQry.getStatus())) {
							clmAudQry.setFinalisedStatus(SHAConstants.AUDIT_QUERY_ACCEPTED);
						}
						else {
							clmAudQry.setFinalisedStatus(SHAConstants.AUDIT_QUERY_NOT_ACCEPTED);
						}
					}	
				}
			}
			
			if(clmAudQry != null) {
			
	
				clmAudQry.setFinalisedDate(new Timestamp(System.currentTimeMillis()));
				clmAudQry.setFinalisedBy(cvcTableDto.getUsername());
				if(clmAudQry.getKey() == null) {
					clmAudQry.setAudit(auditDetails);
					clmAudQry.setTeamName(dtoTeam);
					clmAudQry.setQryRaiseDate(new Timestamp(System.currentTimeMillis()));
					clmAudQry.setCreatedBy(cvcTableDto.getUsername());
					clmAudQry.setActiveStatus(1L);
					entityManager.persist(clmAudQry);
					entityManager.flush();
					entityManager.clear();
				}
				else {
					clmAudQry.setModifiedBy(cvcTableDto.getUsername());
					clmAudQry.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(clmAudQry);
					entityManager.flush();
					entityManager.clear();
				}
			}	
			
			if(SHAConstants.AUDIT_QUERY_REPLY_PENDING.equalsIgnoreCase(cvcTableDto.getClsQryStatus()) 
					|| SHAConstants.AUDIT_QUERY_REPLY_PENDING.equalsIgnoreCase(cvcTableDto.getMedicaQryStatus())
					|| SHAConstants.AUDIT_QUERY_REPLY_PENDING.equalsIgnoreCase(cvcTableDto.getBillingFAQryStatus())) {
				auditDetails.setRemediationStatus(SHAConstants.AUDIT_QUERY_REPLY_PENDING);
				entityManager.merge(auditDetails);
				entityManager.flush();
				entityManager.clear();
			}	
			
		}
		else if (null != auditDetails && null != auditDetails.getAuditStatus() 
				&& null != auditDetails.getRemediationStatus()
				&& auditDetails.getRemediationStatus().toLowerCase().equalsIgnoreCase(SHAConstants.COMPLETED_STRING)) {
			
			ClaimAuditQuery clmAudQry = null; 
			if(dtoTeam.trim().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_CASHLESS)){
				List<SearchCVCAuditClsQryTableDTO> clsqryList = cvcTableDto.getClsQryList();
				if(clsqryList != null && !clsqryList.isEmpty()) {
					for (SearchCVCAuditClsQryTableDTO searchCVCAuditClsQryTableDTO : clsqryList) {
						if(searchCVCAuditClsQryTableDTO.getQryKey() != null
								&& (searchCVCAuditClsQryTableDTO.getQryStatus() != null
								 &&    (searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING)
								    		 || searchCVCAuditClsQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED)))){
							clmAudQry = findAudQryByKey(searchCVCAuditClsQryTableDTO.getQryKey());							
						}
						if(clmAudQry != null){
							clmAudQry.setQueryRemarks(searchCVCAuditClsQryTableDTO.getClsAuditQryRemarks());
							clmAudQry.setFinalisedRemarks(searchCVCAuditClsQryTableDTO.getQryFinalRemarks());
						}
					}
				}
			}
			else if(dtoTeam.trim().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_MEDICAL)){
				List<SearchCVCAuditClsQryTableDTO> MedicalqryList = cvcTableDto.getMedicalQryList();
				for (SearchCVCAuditClsQryTableDTO searchCVCAuditMedicalQryTableDTO : MedicalqryList) {
					if(searchCVCAuditMedicalQryTableDTO.getQryKey() != null
							&& (searchCVCAuditMedicalQryTableDTO.getQryStatus() != null
							 &&    (searchCVCAuditMedicalQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING)
							    		 || searchCVCAuditMedicalQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED)))){
						clmAudQry = findAudQryByKey(searchCVCAuditMedicalQryTableDTO.getQryKey());							
					}
					if(clmAudQry != null){
						clmAudQry.setQueryRemarks(searchCVCAuditMedicalQryTableDTO.getMedicalAuditQryRemarks());
						clmAudQry.setFinalisedRemarks(searchCVCAuditMedicalQryTableDTO.getQryFinalRemarks());
					}
				}	
			}
			else if(dtoTeam.trim().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_BILLING) || dtoTeam.trim().equalsIgnoreCase(SHAConstants.AUDIT_TEAM_FINANCIAL)){
				List<SearchCVCAuditClsQryTableDTO> billingFaqryList = cvcTableDto.getBillingFaQryList();
				for (SearchCVCAuditClsQryTableDTO searchCVCAuditbillingFaQryTableDTO : billingFaqryList) {
					if(searchCVCAuditbillingFaQryTableDTO.getQryKey() != null
							&& (searchCVCAuditbillingFaQryTableDTO.getQryStatus() != null
							 &&    (searchCVCAuditbillingFaQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING)
							    		 || searchCVCAuditbillingFaQryTableDTO.getQryStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED)))){
						clmAudQry = findAudQryByKey(searchCVCAuditbillingFaQryTableDTO.getQryKey());							
					}
					if(clmAudQry != null){
						clmAudQry.setQueryRemarks(searchCVCAuditbillingFaQryTableDTO.getBillinFaAuditQryRemarks());
						clmAudQry.setFinalisedRemarks(searchCVCAuditbillingFaQryTableDTO.getQryFinalRemarks());
					}
				}
			}
			
			if(clmAudQry != null){
				
				clmAudQry.setFinalisedDate(new Timestamp(System.currentTimeMillis()));
				clmAudQry.setFinalisedBy(cvcTableDto.getUsername());
				clmAudQry.setStatus(SHAConstants.AUDIT_QUERY_COMPLETED);
				clmAudQry.setFinalisedStatus(SHAConstants.AUDIT_QUERY_NOT_ACCEPTED);
				if(clmAudQry.getKey() != null) {
					clmAudQry.setModifiedBy(cvcTableDto.getUsername());
					clmAudQry.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(clmAudQry);
					entityManager.flush();
					entityManager.clear();
				}
			}	
			
		}
	}
	public ClaimAuditQuery findAudQryByKey(Long qryKey){
		ClaimAuditQuery auditQrydetails = null;
		try{
		Query query = entityManager.createNamedQuery("ClaimAuditQuery.findAudQryByKey")
				.setParameter("qryKey", qryKey);		
		List<ClaimAuditQuery>auditQrydetailList = (List<ClaimAuditQuery>) query.getResultList();

		if(auditQrydetailList != null && !auditQrydetailList.isEmpty())
		{
			 auditQrydetails = auditQrydetailList.get(0);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return auditQrydetails;
		
		
	}
	
	public AuditDetails getCVCAuditActionAuditKey(Long auditkey)
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByAuditKey");
		query.setParameter("key", auditkey);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList.get(0);
		}

		return null;

	}

}
