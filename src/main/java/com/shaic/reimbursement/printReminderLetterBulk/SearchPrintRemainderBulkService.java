/**
 * 
 */
package com.shaic.reimbursement.printReminderLetterBulk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
//import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimReminderDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.server.VaadinSession;
/**
 * 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SearchPrintRemainderBulkService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	private final Logger log = LoggerFactory.getLogger(SearchPrintRemainderBulkService.class);
	
	public SearchPrintRemainderBulkService() {
		super();
		
	}
	
		public List<PrintBulkReminderResultDto> search(SearchPrintRemainderBulkFormDTO searchFormDTO) {
	
			try {
				
				VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
				utx.setTransactionTimeout(3600);
				utx.begin();
				String option = "";
				String cpuCode = searchFormDTO.getCpuCode() != null ? searchFormDTO
						.getCpuCode().getValue() : null;
				String reminderType = searchFormDTO.getReminderType() != null ? (searchFormDTO
						.getReminderType().getValue() != null ? searchFormDTO
						.getReminderType().getValue().toUpperCase() : null) : null;
				String category = searchFormDTO.getCategory() != null ? (searchFormDTO
						.getCategory().getValue() != null ? searchFormDTO.getCategory().getValue() : "") : "";
				String claimTypeMasterValue = searchFormDTO.getClaimType() != null
						&& searchFormDTO.getClaimType().getValue() != null ? searchFormDTO
						.getClaimType().getValue().toUpperCase()
						: "";
				Date fromDate = searchFormDTO.getFromDate();
				Date toDate = searchFormDTO.getToDate();						
	
				option = searchFormDTO.getSearchOption() != null && searchFormDTO.getSearchOption().getValue() != null && (SHAConstants.LETTERS_PRINT_COMPLETED).equals(searchFormDTO.getSearchOption().getValue()) ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG;
				
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<ClaimReminderDetails> criteriaClaimReminderQuery = builder
						.createQuery(ClaimReminderDetails.class);

				Root<ClaimReminderDetails> claimReminderRoot = criteriaClaimReminderQuery.from(ClaimReminderDetails.class);
				List<Predicate> predicates = new ArrayList<Predicate>();
				List<PrintBulkReminderResultDto> resultList =  new ArrayList<PrintBulkReminderResultDto>();
			
				if(fromDate != null && toDate != null){
					Expression<Date> fromDateExpression = claimReminderRoot
							.<Date> get("createdDate");
					Predicate fromDatePredicate = builder
							.greaterThanOrEqualTo(fromDateExpression,
									fromDate);
					predicates.add(fromDatePredicate);

					Expression<Date> toDateExpression = claimReminderRoot
							.<Date> get("createdDate");
					Calendar c = Calendar.getInstance();
					c.setTime(toDate);
					c.add(Calendar.DATE, 1);
					toDate = c.getTime();
					Predicate toDatePredicate = builder
							.lessThanOrEqualTo(toDateExpression, toDate);
					predicates.add(toDatePredicate);
				}
				
				Expression<String> batchExp = claimReminderRoot.<String>get("batchFlag");
				Predicate batchPredicate = builder.like(batchExp, "Y");
				predicates.add(batchPredicate);
				
//				Expression<Integer> activeStatExp = claimReminderRoot.<Integer>get("activeStatus");
//				Predicate activeStatusPredicate = builder.equal(batchExp, 1);
//				predicates.add(activeStatusPredicate);
				
				if(option != null && (SHAConstants.N_FLAG).equalsIgnoreCase(option)){
					
					if (cpuCode != null && !("").equals(cpuCode)) {
						String cpuCodeValue[] = cpuCode.split(" ");
						if (cpuCodeValue.length >= 0) {
							cpuCode = cpuCodeValue[0];
						}
					}
					
					if (null != cpuCode && !("").equals(cpuCode)) {
						
						Predicate cpuPredicate = builder.equal(claimReminderRoot.<Long>get("cpuCode"), cpuCode);
						predicates.add(cpuPredicate);						
					}
					
					if (claimTypeMasterValue != null && !("").equals(claimTypeMasterValue)) {
						
						Predicate claimTypePredicate = builder.equal(claimReminderRoot.<String>get("claimType"), claimTypeMasterValue);
						predicates.add(claimTypePredicate);
						
					}
					
					if (null != category) {
					
						if(!("").equalsIgnoreCase(category)){
							Predicate categoryPredicate = builder.equal(claimReminderRoot.<String>get("category"), category);
							predicates.add(categoryPredicate);	
						}
						
						if (null != reminderType && SHAConstants.PAN_CARD.equalsIgnoreCase(category) ) {
						Long reminderCount = searchFormDTO.getReminderType() != null ? (searchFormDTO
								.getReminderType().getId() != null ? searchFormDTO
								.getReminderType().getId() : 0) : 0;
								
								Predicate reminderCountPredicate = builder.equal(claimReminderRoot.<Long>get("reminderCount"), reminderCount);
								predicates.add(reminderCountPredicate);										
						}					
					}
		
					if (null != reminderType && (category == null || ( category != null && !SHAConstants.PAN_CARD.equalsIgnoreCase(category)))) {
						
						Integer reminderCount = !("").equals(reminderType) ? ((SHAConstants.FIRST_REMINDER)
								.equalsIgnoreCase(reminderType) ? 1
								: ((SHAConstants.SECOND_REMINDER)
										.equalsIgnoreCase(reminderType) ? 2 : 3))
								: null;
					
						Predicate reminderCountPredicate = builder.equal(claimReminderRoot.<Long>get("reminderCount"), reminderCount);
						predicates.add(reminderCountPredicate);
					}

					
					Predicate PendingPredicate = builder.equal(claimReminderRoot.<String>get("printFlag"), "N");
					Predicate printFlagPredicate = builder.isNull(claimReminderRoot.<String>get("printFlag"));
					
					Predicate reminderPendingPredicate =  builder.or(printFlagPredicate, PendingPredicate);	
					
					predicates.add(reminderPendingPredicate);
				}
				else if(option != null && (SHAConstants.YES_FLAG).equalsIgnoreCase(option)){

					Predicate completedPredicate = builder.equal(claimReminderRoot.<String>get("printFlag"), "Y");
					Predicate printFlagPredicate = builder.isNotNull(claimReminderRoot.<String>get("printFlag"));
					
					Predicate reminderCompletedPredicate =  builder.and(printFlagPredicate, completedPredicate);	
					
					predicates.add(reminderCompletedPredicate);
					
					String batchNo = searchFormDTO.getBatchId();
					
					if(batchNo != null && !batchNo.isEmpty()){
						Predicate batchIdPredicate = builder.like(builder.lower(claimReminderRoot.<String>get("batchNo")), batchNo.toLowerCase());	
						predicates.add(batchIdPredicate);
					}					
					
				}
				else{
					
				}
				
//				if(predicates.size() > 2)
//				{	
					criteriaClaimReminderQuery.select(claimReminderRoot).where(
							builder.and(predicates
									.toArray(new Predicate[] {})));

					final TypedQuery<ClaimReminderDetails> claimreminderQuery = entityManager
							.createQuery(criteriaClaimReminderQuery);
				
					List<ClaimReminderDetails> resultClaimList = claimreminderQuery.getResultList();
					
				if(resultClaimList != null && !resultClaimList.isEmpty()){
					
					for(ClaimReminderDetails remDetails : resultClaimList){
						PrintBulkReminderResultDto resultDto = new PrintBulkReminderResultDto();
						resultDto.setBatchid(remDetails.getBatchNo());
						resultDto.setLetterGeneratedDate(remDetails.getGeneratedDate());
						resultDto.setCpuCode(remDetails.getCpuCode() != null ? String.valueOf(remDetails.getCpuCode()) : "");
						resultDto.setClaimType(remDetails.getClaimType());
						resultDto.setCategory(remDetails.getCategory());
//						String remType = remDetails.getReminderCount() != null ? 
//								(1 == remDetails.getReminderCount().intValue() ? SHAConstants.FIRST_REMINDER :
//									(remDetails.getReminderCount().intValue() == 2 ? SHAConstants.SECOND_REMINDER
//													: (remDetails.getReminderCount().intValue() == 3 ? SHAConstants.CLOSE
//															: ""))) : "";
//						resultDto.setReminderType(remType);
						
						resultDto.setReminderType(remDetails.getReimainderCategory());
						resultDto.setTotalNoofRecords(remDetails.getPrintCount());
						resultDto.setSno(resultClaimList.indexOf(remDetails)+1);
						resultDto.setDocToken(remDetails.getDocumentToken());
						resultDto.setPrint(remDetails.getPrintFlag());
						resultList.add(resultDto);
					}					
				}
//			}	
		
				utx.commit();
				return resultList;
			} catch (Exception e) {
				try {
					utx.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				log.error(e.toString());
				e.printStackTrace();
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh" + e.getMessage()
						+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			}
			return null;
	
		}	
	
	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}

	/*private TmpCPUCode getTmpCPUCode(Long cpuKey){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuKey);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			return null;
		}
		
	}*/

	@SuppressWarnings("unchecked")
	public SearchPrintReminderBulkTableDTO submitReminderLetter(SearchPrintReminderBulkTableDTO reminderLetterDto){
		
		try{
			
		utx.begin();
		String fileUrl = reminderLetterDto.getFileUrl();
		
		/**
		 * Need to upload reminder Letter to DMS
		 */
		
		if(null != fileUrl && !("").equalsIgnoreCase(fileUrl))
		{
			WeakHashMap<String, String> dataMap = new WeakHashMap<String, String>();
			dataMap.put("intimationNumber",reminderLetterDto.getClaimDto().getNewIntimationDto().getIntimationId());
			Claim objClaim = getClaimByClaimKey(reminderLetterDto.getClaimDto().getKey());
			if(null != objClaim)
			{
				dataMap.put("claimNumber",objClaim.getClaimId());
				if(null != objClaim.getClaimType())
				{
					if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()))
						{
							Preauth preauth = SHAUtils.getPreauthClaimKey(entityManager , objClaim.getKey());
							if(null != preauth)
								dataMap.put("cashless", preauth.getPreauthId());
						}
					
					else if((ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()) && dataMap != null)
					{
						Reimbursement reimbursement = getReimbursementByClaimKey(objClaim.getKey());
						if(null != reimbursement)
							dataMap.put("reimbursementNumber", reimbursement.getRodNumber());
					}
				}
			}
			dataMap.put("filePath", fileUrl);
			dataMap.put("docType",SHAConstants.REMINDER_LETTER);
			dataMap.put("docSources", SHAConstants.GENERATE_REMINDER_LETTER);
			dataMap.put("createdBy", reminderLetterDto.getUsername());
			
			//For testing Purpose commented Need to uncomment below line
			String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
			SHAUtils.setClearMapStringValue(dataMap);
			reminderLetterDto.setDocToken( docToken != null ? Long.valueOf(docToken) : null );
			reminderLetterDto.setLetterDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		}
		
		
		ClaimDto claimDto = reminderLetterDto.getClaimDto();
		
		if(reminderLetterDto != null && reminderLetterDto.getQueryKey() != null){
			ReimbursementQueryDto queryDto = reminderLetterDto.getReimbQueryDto();
			Query query = entityManager.createNamedQuery("ReimbursementQuery.findLatestQueryByKey");
			query.setParameter("primaryKey", queryDto.getKey());
			List<ReimbursementQuery> queryObjList = query.getResultList();
			if(queryObjList != null && !queryObjList.isEmpty()){
				ReimbursementQuery queryObj = queryObjList.get(0);
				entityManager.refresh(queryObj);
				
		//For testing Purpose commented Need to uncomment below lines
				queryObj.setReminderCount(queryDto.getReminderCount() != null ? Long.valueOf(queryDto.getReminderCount()): null);
				queryObj.setReminderDate1(queryDto.getFirstReminderDate());
				queryObj.setReminderDate2(queryDto.getSecondReminderDate());
				queryObj.setReminderDate3(queryDto.getThirdReminderDate());
				entityManager.merge(queryObj);
				entityManager.flush();
				entityManager.clear();
				
		//For testing Purpose commented Need to uncomment below lines
				
				/*SubmitGenerateReminderLetterTask submitTask = BPMClientContext.getSubmitQueryReimnderLetterTask(reminderLetterDto.getUsername(), reminderLetterDto.getPassword());
				reminderLetterDto.getReimbQueryDto().getHumanTask().setOutcome("SUBMIT");
				submitTask.execute(reminderLetterDto.getUsername(), reminderLetterDto.getReimbQueryDto().getHumanTask());*/
			}			
			
		}
		
		else if(reminderLetterDto.getPreauthKey() == null && reminderLetterDto.getQueryKey() == null){
			Query query = entityManager.createNamedQuery("Claim.findByKey");
			query.setParameter("primaryKey", claimDto.getKey());
			List<Claim> claimObjList = query.getResultList();
			if(claimObjList != null && !claimObjList.isEmpty()){
				Claim claimObj = claimObjList.get(0);
				entityManager.refresh(claimObj);
				
				claimObj.setReminderCount(claimDto.getReminderCount() != null ? claimDto.getReminderCount().longValue() : null); 
				claimObj.setFirstReminderDate(claimDto.getFirstReminderDate());
				claimObj.setSecondReminderDate(claimDto.getSecondReminderDate());
				claimObj.setThirdReminderDate(claimDto.getThirdReminderDate());
				//For testing Purpose commented Need to uncomment below lines
				entityManager.merge(claimObj);
				entityManager.flush();
				entityManager.clear();
				/*reminderLetterDto.getClaimDto().getHumanTask().setOutcome("APPROVE");
				SubmitGenerateLetterTask submitCLRemLetterTask = BPMClientContext.getSubmitReimbReimnderLetterTask(reminderLetterDto.getUsername(), reminderLetterDto.getPassword());
				submitCLRemLetterTask.execute(reminderLetterDto.getUsername(), reminderLetterDto.getClaimDto().getHumanTask());*/
			}	
		}
		else if(reminderLetterDto.getPreauthKey() != null && reminderLetterDto.getQueryKey() == null){
			Query query = entityManager.createNamedQuery("Claim.findByKey");
			query.setParameter("primaryKey", claimDto.getKey());
			List<Claim> claimObjList = query.getResultList();
			if(claimObjList != null && !claimObjList.isEmpty()){
				Claim claimObj = claimObjList.get(0);
				entityManager.refresh(claimObj);
				
				claimObj.setReminderCount(claimDto.getReminderCount() != null ? claimDto.getReminderCount().longValue() : null);
				claimObj.setFirstReminderDate(claimDto.getFirstReminderDate());
				claimObj.setSecondReminderDate(claimDto.getSecondReminderDate());
				claimObj.setThirdReminderDate(claimDto.getThirdReminderDate());
				
		//For testing Purpose commented Need to uncomment below lines
				entityManager.merge(claimObj);
				entityManager.flush();
				entityManager.clear();
				/*reminderLetterDto.getClaimDto().getHumanTask().setOutcome("SUBMIT");
				SubmitCLGenerateLetterTask   submitCLRemLetterTask = BPMClientContext.getSubmitCLReimnderLetterTask(reminderLetterDto.getUsername(), reminderLetterDto.getPassword());
				submitCLRemLetterTask.execute(reminderLetterDto.getUsername(), reminderLetterDto.getClaimDto().getHumanTask());*/
			}
		}	
		utx.commit();
		}
		catch(Exception e){
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.error(e.toString());
			e.printStackTrace();
		}
		
		return reminderLetterDto;
	}
	
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByClaimKey(Long claimKey) {
		Claim claim = null;
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Claim> claimList = (List<Claim>)query.getResultList();
		
		if(claimList != null && ! claimList.isEmpty()){
			for (Claim claimObj : claimList) {
				entityManager.refresh(claimObj);
				claim = claimObj;
			}			
		}
		return claim;
	}
	
	
	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Hospitals getHospitalDetailsByKey(Long hospKey){
		Hospitals hospitalObj = null; 
		Query query = entityManager
				.createNamedQuery("Hospitals.findByKey");
		query = query.setParameter("key", hospKey);
		List<Hospitals> hospitalList = query.getResultList();
		if(null != hospitalList && !hospitalList.isEmpty())
		{
			hospitalObj = hospitalList.get(0);
			entityManager.refresh(hospitalObj);
			
		}
		return hospitalObj;
	}
	
//	@SuppressWarnings("unchecked")
//	public List<PrintBulkReminderResultDto> searchBatchByIdOrIntimation(Map<String,String> searchfields){
//						
//		String batchId = searchfields.containsKey(SHAConstants.SEARCH_REMINDER_BATCH) && searchfields.get(SHAConstants.SEARCH_REMINDER_BATCH) != null ? ( "%" + (String)searchfields.get(SHAConstants.SEARCH_REMINDER_BATCH)) : null;
//		String intimationNo = searchfields.containsKey(SHAConstants.SEARCH_REMINDER_INTIMATION) && searchfields.get(SHAConstants.SEARCH_REMINDER_INTIMATION) != null ? ("%" + (String)searchfields.get(SHAConstants.SEARCH_REMINDER_INTIMATION) + "%") : null;
//		List<PrintBulkReminderResultDto> resultList =  new ArrayList<PrintBulkReminderResultDto>();
//		
//		List<ClaimReminderDetails> resultObjListByIntimation =  new ArrayList<ClaimReminderDetails>();
//		List<ClaimReminderDetails> resultObjListByBatchId =  new ArrayList<ClaimReminderDetails>();
//		List<ClaimReminderDetails> resultObjList =  new ArrayList<ClaimReminderDetails>();
//		//List<PrintBulkReminderResultDto> resultDtoListByBatchId =  new ArrayList<PrintBulkReminderResultDto>();
//		//List<PrintBulkReminderResultDto> finalresultDtoList =  new ArrayList<PrintBulkReminderResultDto>();
//		
//		try{
//		if(batchId != null){
//			Query reminderBatchByIdQuery = entityManager.createNamedQuery("ClaimReminderDetails.findSubBatchIdByBatchId");	
//			reminderBatchByIdQuery.setParameter("batchId", batchId.toLowerCase());
//			
//			List<ClaimReminderDetails> resultObjListBysubId = reminderBatchByIdQuery.getResultList();
//			
//			if(resultObjListBysubId != null && !resultObjListBysubId.isEmpty() && resultObjListBysubId.get(0) != null){
//			Iterator resultObjListIterator = resultObjListBysubId.iterator();
//			
//			for(;resultObjListIterator.hasNext();){
//				ClaimReminderDetails reminderDetails = new ClaimReminderDetails();	
//				Object[] reminderDetailsResult = (Object[]) resultObjListIterator.next();
//				if(reminderDetailsResult[0] != null){
//					String subBatchNo = (String)reminderDetailsResult[0];
//					
//					if(subBatchNo != null && !("").equalsIgnoreCase(subBatchNo)){
//						Query reminderQueryBySubBatchId = entityManager.createNamedQuery("ClaimReminderDetails.findBySubBatchId");	
//						reminderQueryBySubBatchId.setParameter("subBatchId", subBatchNo);
//						List<ClaimReminderDetails> resultBySubIdList = reminderQueryBySubBatchId.getResultList();
//						if(resultBySubIdList != null && !resultBySubIdList.isEmpty()){
//							reminderDetails.setBatchNo(resultBySubIdList.get(0).getBatchNo());
//							reminderDetails.setSubBatchNo(subBatchNo);
//							reminderDetails.setTotalNoofRecords(resultBySubIdList.size());
//							resultObjListByBatchId.add(reminderDetails);
//						}
//					}
//			   }
//			}			
//		}
//		if(resultObjListByBatchId == null || resultObjListByBatchId.isEmpty() ) {
//				
//				Query reminderByBatchIdQuery = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchIdGroup");
//				reminderByBatchIdQuery.setParameter("batchId", batchId.toLowerCase());
//				
//				List<ClaimReminderDetails> resultQueryObjListByBatchId = reminderByBatchIdQuery.getResultList();		
//				
//				if(resultQueryObjListByBatchId != null && !resultQueryObjListByBatchId.isEmpty() && resultQueryObjListByBatchId.get(0) != null){
//					Iterator resultObjListIterator = resultQueryObjListByBatchId.iterator();
//					
//					for(;resultObjListIterator.hasNext();){
//						ClaimReminderDetails reminderDetails = new ClaimReminderDetails();
//						Object[] reminderDetailsResult = (Object[]) resultObjListIterator.next();
//						if(reminderDetailsResult[0] != null){
//							String batchNo = (String)reminderDetailsResult[0];
//							Long noofrecords = (Long)reminderDetailsResult[1];
//							reminderDetails.setBatchNo(batchNo);
//							reminderDetails.setTotalNoofRecords(noofrecords != null ? noofrecords.intValue() : null);				
//						}
//						resultObjListByBatchId.add(reminderDetails);
//					}
//				}
//				
//			}			
//		}
//		else if(intimationNo != null){
//			
//			Query reminderSubBatchQueryByIntimationGroup = entityManager.createNamedQuery("ClaimReminderDetails.findBySubBatchIntimationNoGroup");
//			reminderSubBatchQueryByIntimationGroup.setParameter("intimationNo", intimationNo.toLowerCase());
//			List<ClaimReminderDetails> intimationQueryResultListBySubBatchId = reminderSubBatchQueryByIntimationGroup.getResultList();
//			if(intimationQueryResultListBySubBatchId != null && !intimationQueryResultListBySubBatchId.isEmpty() && intimationQueryResultListBySubBatchId.get(0) != null){
//				
//				Iterator listIter1 = intimationQueryResultListBySubBatchId.iterator();
//				for(;listIter1.hasNext();){
//					ClaimReminderDetails reminderDetails = new ClaimReminderDetails();
//					Object[] reminderDetailsResult = (Object[]) listIter1.next();
//					if(reminderDetailsResult[0] != null){
//						String batchNo = (String)reminderDetailsResult[0];
//						Long noofrecords = (Long)reminderDetailsResult[1];
//						reminderDetails.setBatchNo(batchNo);
//						reminderDetails.setTotalNoofRecords(noofrecords != null ? noofrecords.intValue() : null);				
//						resultObjListByIntimation.add(reminderDetails);
//					}
//					
//				}
//				
//			}
//			
//			if(resultObjListByIntimation == null || resultObjListByIntimation.isEmpty()){			
//			
//			Query reminderQueryByIntimationGroup = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchIntimationNoGroup");
//			reminderQueryByIntimationGroup.setParameter("intimationNo", intimationNo.toLowerCase());
//			List<ClaimReminderDetails> intimationQueryResultListByBatchId = reminderQueryByIntimationGroup.getResultList();
//			
//				if(intimationQueryResultListByBatchId != null && !intimationQueryResultListByBatchId.isEmpty() && intimationQueryResultListByBatchId.get(0) != null){
//				
//					Iterator listIter1 = intimationQueryResultListByBatchId.iterator();
//					for(;listIter1.hasNext();){
//						ClaimReminderDetails reminderDetails = new ClaimReminderDetails();
//						Object[] reminderDetailsResult = (Object[]) listIter1.next();
//						if(reminderDetailsResult[0] != null){
//							String batchNo = (String)reminderDetailsResult[0];
//							reminderDetails.setBatchNo(batchNo);
//						}
//						resultObjListByIntimation.add(reminderDetails);
//					}
//					
//				}
//			}
//		}
//		
//		if(resultObjListByIntimation != null && !resultObjListByIntimation.isEmpty()){
//			resultObjList.addAll(resultObjListByIntimation); 
//		}
//		else if(resultObjListByBatchId != null && !resultObjListByBatchId.isEmpty()){
//			resultObjList.addAll(resultObjListByBatchId);
//		}
//			
//		if(resultObjList != null && !resultObjList.isEmpty()){	
//		for(ClaimReminderDetails resultObj :resultObjList){
//			
//			PrintBulkReminderResultDto reminderDetailsDto = new PrintBulkReminderResultDto();
//						
//			reminderDetailsDto.setSno(resultObjList.indexOf(resultObj)+1);
//			String subBatchId = resultObj.getSubBatchNo();
//			String batchNo = resultObj.getBatchNo();
//			List<SearchPrintReminderBulkTableDTO> exportList = new ArrayList<SearchPrintReminderBulkTableDTO>();
//			List<ClaimReminderDetails> resultBySubIdList = new ArrayList<ClaimReminderDetails>();
//			if( subBatchId != null && !subBatchId.isEmpty()){
//				
//				Query reminderQueryBySubBatchId = entityManager.createNamedQuery("ClaimReminderDetails.findBySubBatchId");	
//				reminderQueryBySubBatchId.setParameter("subBatchId", subBatchId);
//				resultBySubIdList = reminderQueryBySubBatchId.getResultList();
//				
//			}	
//			else if(batchNo != null && !batchNo.isEmpty()){
//				
//				Query reminderQueryByBatchId = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchId");	
//				reminderQueryByBatchId.setParameter("batchId", batchNo.toLowerCase());
//				resultBySubIdList = reminderQueryByBatchId.getResultList();
//			}					
//			if(resultBySubIdList != null && !resultBySubIdList.isEmpty()){
//				populateReminderDetailsToDto(reminderDetailsDto,resultBySubIdList.get(0));
//				//Long claimKey = resultBySubIdList.get(0).getClaimKey();
//				//String claimType = "";
//				/*if(claimKey != null){
//					Claim claim = entityManager.find(Claim.class, claimKey);
//											
//					claimType = claim.getClaimType().getValue(); 
//				}*/
////				reminderDetailsDto.setClaimType(claimType);
//				for(ClaimReminderDetails reminderObj : resultBySubIdList){
//					
//					SearchPrintReminderBulkTableDTO excelObj = populateExportExcelDto(reminderObj,reminderDetailsDto.getReminderType());
//					excelObj.setSno(String.valueOf(resultBySubIdList.indexOf(reminderObj)+1));
//					exportList.add(excelObj);
//					
//				}
//			}
//			
//			reminderDetailsDto.setResultListDto(exportList);
//			reminderDetailsDto.setTotalNoofRecords(resultBySubIdList.size());
//			resultList.add(reminderDetailsDto);
//		}
//		
//		}	
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		return resultList;
//		
//	}
	
	private SearchPrintReminderBulkTableDTO populateExportExcelDto(ClaimReminderDetails reminderObj,String reminderTyString){
		SearchPrintReminderBulkTableDTO resultDto = new SearchPrintReminderBulkTableDTO();
		resultDto.setClaimIntimationNo(reminderObj.getIntimatonNo());
		resultDto.setClaimKey(reminderObj.getClaimKey());
		
		String claimType = "";
		if(resultDto.getClaimKey() != null){
			Claim claim = entityManager.find(Claim.class, resultDto.getClaimKey());
									
			claimType = claim.getClaimType().getValue(); 
		}
		resultDto.setClaimType(claimType);
		resultDto.setQueryKey(reminderObj.getQueryKey());
		resultDto.setPreauthKey(reminderObj.getTransacKey());
		resultDto.setCategory(reminderObj.getCategory());
		resultDto.setCpuCode(reminderObj.getCpuCode() != null ? reminderObj.getCpuCode().toString() : "");
		resultDto.setReminderType(reminderTyString);
		resultDto.setBatchid(reminderObj.getBatchNo());
		resultDto.setSubBatchId(reminderObj.getSubBatchNo());
		resultDto.setDocToken(reminderObj.getDocumentToken());
		resultDto.setLetterDate(reminderObj.getGeneratedDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(reminderObj.getGeneratedDate()) : "");
				
		return resultDto;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<SearchPrintReminderBulkTableDTO> populateExportExcelList(PrintBulkReminderResultDto bulkReminderDto ){
		List<SearchPrintReminderBulkTableDTO> resultList = new ArrayList<SearchPrintReminderBulkTableDTO>();
		
		Query eportExcelQuery = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchIdExportList");
		eportExcelQuery.setParameter("batchId", bulkReminderDto.getBatchid().toLowerCase());

		List<ClaimReminderDetails> reminderList = eportExcelQuery.getResultList();
		
		if(reminderList != null && !reminderList.isEmpty()){
		 	
			for(ClaimReminderDetails reminderObj : reminderList){
				
				String reminderType = reminderObj.getReimainderCategory() != null ? reminderObj.getReimainderCategory() : "";
				SearchPrintReminderBulkTableDTO exportExcelDto = populateExportExcelDto(reminderObj, reminderType);
				exportExcelDto.setSno(String.valueOf(reminderList.indexOf(reminderObj)+1));
				resultList.add(exportExcelDto);
			}	
		}
		return resultList;		
	}
	
	
//	private void populateReminderDetailsToDto(PrintBulkReminderResultDto reminderDetailsDto, ClaimReminderDetails resultObj){
//		
//		reminderDetailsDto.setReminderDetailsKey(resultObj.getKey());
//		reminderDetailsDto.setIntimationNo(resultObj.getIntimatonNo());
//		reminderDetailsDto.setIntimationKey(resultObj.getIntimationKey());
//		reminderDetailsDto.setClaimNo(resultObj.getClaimNo());
//		reminderDetailsDto.setPreauthKey(resultObj.getTransacKey());
//		reminderDetailsDto.setQueryKey(resultObj.getQueryKey());
//		reminderDetailsDto.setBatchid(resultObj.getBatchNo());
//		
//		reminderDetailsDto.setSubBatchid(resultObj.getSubBatchNo());
////		reminderDetailsDto.setCategory(resultObj.getCategory());
////		reminderDetailsDto.setCpuCode(resultObj.getCpuCode() != null ? resultObj.getCpuCode().toString() : "");
//		reminderDetailsDto.setReminderCount(resultObj.getReminderCount());
//		reminderDetailsDto.setTotalNoofRecords(resultObj.getTotalNoofRecords());
////		String reminderType = resultObj.getReminderCount() != null ? (resultObj.getReminderCount().equals(1) ? "First Reminder" : (resultObj.getReminderCount().equals(2) ? "Second Reminder" :"Close Reminder") ) : "";	
//		reminderDetailsDto.setPrint(resultObj.getPrintFlag());
//		reminderDetailsDto.setPrintCount(resultObj.getPrintCount());
////		reminderDetailsDto.setReminderType(reminderType);
//		reminderDetailsDto.setDocToken(resultObj.getDocumentToken());
//		reminderDetailsDto.setLetterGeneratedDate(resultObj.getGeneratedDate());
//		reminderDetailsDto.setStatus(("y").equalsIgnoreCase(resultObj.getPrintFlag()) ? "Completed" : "In Progress");
//	}
	
	@SuppressWarnings("unchecked")
	public void submitBulkLetter(PrintBulkReminderResultDto reminderDetailsDto){
		try{
			
			utx.begin();
		String batchId = reminderDetailsDto.getBatchid();
		//String subbatchId = reminderDetailsDto.getSubBatchid();
		List<ClaimReminderDetails> batchIdList = new ArrayList<ClaimReminderDetails>();
		if(batchId != null){
			Query reminderQuery = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchId");
			reminderQuery.setParameter("batchId",batchId.toLowerCase()); 
			batchIdList =  reminderQuery.getResultList();			
		}
		
		if(batchIdList != null && !batchIdList.isEmpty()){
			for(ClaimReminderDetails claimReminderObj :batchIdList){
				//entityManager.refresh(claimReminderObj);
				claimReminderObj.setModifiedDate(new Date());
				claimReminderObj.setModifiedBy(SHAUtils.getUserNameForDB(reminderDetailsDto.getUserName()));
				claimReminderObj.setPrintFlag("Y");
				entityManager.merge(claimReminderObj);
				entityManager.flush();
				entityManager.clear();
			}
		}
		utx.commit();   
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	
	public void submitReminderDetails(SearchPrintReminderBulkTableDTO reminderDetailsDto){
//	public void submitReminderDetails(BulkReminderResultDto reminderDetailsDto){
	
		
		if(reminderDetailsDto != null && reminderDetailsDto.getBatchid() != null && ! reminderDetailsDto.getBatchid().isEmpty()){
		ClaimReminderDetails resultObj = new ClaimReminderDetails();
		
//		resultObj.setIntimationKey(reminderDetailsDto.getIntimationKey());
//		resultObj.setIntimatonNo(reminderDetailsDto.getClaimDto().getNewIntimationDto().getIntimationId());
//		resultObj.setQueryKey(reminderDetailsDto.getQueryKey());
//		resultObj.setTransacKey(reminderDetailsDto.getPreauthKey());
//		resultObj.setClaimNo(reminderDetailsDto.getClaimNo());
//		resultObj.setBatchNo(reminderDetailsDto.getBatchid());
//		resultObj.setSubBatchNo(reminderDetailsDto.getSubBatchid());
//		resultObj.setCategory(reminderDetailsDto.getCategory());
//		resultObj.setCpuCode(reminderDetailsDto.getCpuCode() != null ? resultObj.getCpuCode() : null);
//		resultObj.setReminderCount(reminderDetailsDto.getReminderCount());
//		resultObj.setPrintFlag("Y");
//		resultObj.setPrintCount(reminderDetailsDto.getPrintCount());
//		resultObj.setDocumentToken(reminderDetailsDto.getDocToken());
//		resultObj.setGeneratedDate(reminderDetailsDto.getLetterGeneratedDate());
//		resultObj.setCreatedBy(SHAUtils.getUserNameForDB(reminderDetailsDto.getUserName()));
		
		resultObj.setIntimationKey(reminderDetailsDto.getClaimDto().getNewIntimationDto().getKey());
		resultObj.setIntimatonNo(reminderDetailsDto.getClaimDto().getNewIntimationDto().getIntimationId());
		resultObj.setQueryKey(reminderDetailsDto.getReimbQueryDto() != null ? reminderDetailsDto.getReimbQueryDto().getKey() : null);
		resultObj.setTransacKey(reminderDetailsDto.getPreauthKey());
		resultObj.setClaimNo(reminderDetailsDto.getClaimDto().getClaimId());
		resultObj.setClaimKey(reminderDetailsDto.getClaimDto().getKey());
		
		resultObj.setBatchNo(reminderDetailsDto.getBatchid());
		resultObj.setSubBatchNo(reminderDetailsDto.getSubBatchId());
		resultObj.setCategory(reminderDetailsDto.getCategory());
		resultObj.setCpuCode(reminderDetailsDto.getClaimDto().getNewIntimationDto().getCpuCode() != null ? Long.valueOf(reminderDetailsDto.getClaimDto().getNewIntimationDto().getCpuCode()) : null);
		resultObj.setReminderCount(reminderDetailsDto.getClaimDto().getReminderCount());
		resultObj.setPrintFlag("N");
		resultObj.setPrintCount(0);
		resultObj.setDocumentToken(reminderDetailsDto.getDocToken());
		resultObj.setGeneratedDate(SHAUtils.getCalendar(new Date()).getTime());
		resultObj.setCreatedBy(SHAUtils.getUserNameForDB(reminderDetailsDto.getUsername()));
		resultObj.setCreatedDate(SHAUtils.getCalendar(new Date()).getTime());
		try{
			utx.setTransactionTimeout(360000);
			utx.begin();
			entityManager.persist(resultObj);
			utx.commit();
		}
		catch(Exception e){
			e.printStackTrace();
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				e1.printStackTrace();
			}
			
		}
	 }
	}
	
	
	@SuppressWarnings({ "deprecation", "static-access", "unchecked", "rawtypes" })
	public List<PrintBulkReminderResultDto> searchPrevBatch(){
	
		List<PrintBulkReminderResultDto> resultList =  new ArrayList<PrintBulkReminderResultDto>();
		
		try{
		
		Query prevReminderBatchQuery1 = entityManager.createNamedQuery("ClaimReminderDetails.findPrevBatchBysubBatchId");
		
		Query prevReminderBatchQuery2 = entityManager.createNamedQuery("ClaimReminderDetails.findPrevBatchByBatchId");
		
		
		Calendar.getInstance().set(Calendar.getInstance().getTime().getYear(), Calendar.getInstance().getTime().getMonth(), 1);
		Date startDate = Calendar.getInstance().getTime();
		startDate.setDate(1);
		startDate.setMonth(Calendar.getInstance().getTime().getMonth());
		startDate.setYear(Calendar.getInstance().getTime().getYear());
		prevReminderBatchQuery1.setParameter("createdDate", startDate);
		
		Calendar calInstance = Calendar.getInstance();
		calInstance.set(calInstance.getTime().getYear(), calInstance.getTime().getMonth(), calInstance.getTime().getDate());
		Date endDate = calInstance.getInstance().getTime();
		prevReminderBatchQuery1.setParameter("endDate",endDate);
		
		
		
		List<ClaimReminderDetails> reminderDetailsListBySubBatchId = prevReminderBatchQuery1.getResultList();
		List<PrintBulkReminderResultDto> listBySubBatchId = new ArrayList<PrintBulkReminderResultDto>();
		Long totalRecords = null;
		
		if(reminderDetailsListBySubBatchId != null && !reminderDetailsListBySubBatchId.isEmpty() && reminderDetailsListBySubBatchId.get(0) != null){	
			Iterator reminderIter1 = reminderDetailsListBySubBatchId.iterator();
			
			for(;reminderIter1.hasNext();){
				
				PrintBulkReminderResultDto reminderDetailsDto = new PrintBulkReminderResultDto();
				Object[] reminderDetailsResult = (Object[]) reminderIter1.next();
				
				if(reminderDetailsResult[0] != null){
					reminderDetailsDto.setSubBatchid((String)reminderDetailsResult[0]);
					totalRecords = reminderDetailsResult[1] != null ? (Long)reminderDetailsResult[1] : null;
					reminderDetailsDto.setTotalNoofRecords(totalRecords != null ? totalRecords.intValue() : 0);			
					listBySubBatchId.add(reminderDetailsDto);
				}
			}
		}
		prevReminderBatchQuery2.setParameter("createdDate", startDate);
		prevReminderBatchQuery2.setParameter("endDate",endDate);
		
		List<ClaimReminderDetails> reminderDetailsListByBatchId = new ArrayList<ClaimReminderDetails>();
		
		if(listBySubBatchId == null || listBySubBatchId.isEmpty()){
			reminderDetailsListByBatchId = prevReminderBatchQuery2.getResultList();
			
			if(reminderDetailsListByBatchId != null && !reminderDetailsListByBatchId.isEmpty() && reminderDetailsListByBatchId.get(0) != null){
				Iterator reminderIter2 = reminderDetailsListByBatchId.iterator();
				for(;reminderIter2.hasNext();){
					
					PrintBulkReminderResultDto reminderDetailsDto = new PrintBulkReminderResultDto();
					Object[] reminderDetailsResult = (Object[]) reminderIter2.next();
					
					if(reminderDetailsResult[0] != null){
						reminderDetailsDto.setBatchid((String)reminderDetailsResult[0]);
						totalRecords = reminderDetailsResult[1] != null ? (Long)reminderDetailsResult[1] : null;
						reminderDetailsDto.setTotalNoofRecords(totalRecords != null ? totalRecords.intValue() : 0);
						listBySubBatchId.add(reminderDetailsDto);
					}	
				}
			}
		}
		
		int index = 1;
			if(listBySubBatchId != null && !listBySubBatchId.isEmpty()){
				for( PrintBulkReminderResultDto bulkReminderDto : listBySubBatchId){
					
					
					PrintBulkReminderResultDto reminderDetailsDto = new PrintBulkReminderResultDto();
					List<SearchPrintReminderBulkTableDTO> exportExcelList = new ArrayList<SearchPrintReminderBulkTableDTO>();
					
					reminderDetailsDto.setSubBatchid(bulkReminderDto.getSubBatchid());			
					
					reminderDetailsDto.setBatchid(bulkReminderDto.getBatchid());
		
					List<ClaimReminderDetails> claimReminderDetailsObjList = new ArrayList<ClaimReminderDetails>();
					if(reminderDetailsDto.getSubBatchid() != null){
						
						Query reminderBatchBySubBatchIdQuery = entityManager.createNamedQuery("ClaimReminderDetails.findBySubBatchId");
		
						reminderBatchBySubBatchIdQuery.setParameter("subBatchId",reminderDetailsDto.getSubBatchid());
						claimReminderDetailsObjList = reminderBatchBySubBatchIdQuery.getResultList(); 
					}
					else if(reminderDetailsDto.getBatchid() != null){
					
						Query reminderBatchByIdQuery = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchId");
		
						reminderBatchByIdQuery.setParameter("batchId",reminderDetailsDto.getBatchid().toLowerCase());
						claimReminderDetailsObjList = reminderBatchByIdQuery.getResultList(); 
					}
					
					
						if(claimReminderDetailsObjList != null && !claimReminderDetailsObjList.isEmpty())
						reminderDetailsDto.setBatchid(claimReminderDetailsObjList.get(0).getBatchNo());
//						reminderDetailsDto.setCategory(claimReminderDetailsObjList.get(0).getCategory());
//						reminderDetailsDto.setCpuCode(claimReminderDetailsObjList.get(0).getCpuCode() != null ? claimReminderDetailsObjList.get(0).getCpuCode().toString() : "");
						reminderDetailsDto.setReminderCount(claimReminderDetailsObjList.get(0).getReminderCount());
						reminderDetailsDto.setPrint(claimReminderDetailsObjList.get(0).getPrintFlag());
						reminderDetailsDto.setPrintCount(claimReminderDetailsObjList.get(0).getPrintCount());
						
						String reminderType = claimReminderDetailsObjList.get(0).getReimainderCategory() != null ? claimReminderDetailsObjList.get(0).getReimainderCategory() : "";	
						//Long claimKey = claimReminderDetailsObjList.get(0).getClaimKey();
						
						/*String claimType = "";
						if(claimKey != null){
							Claim claim = entityManager.find(Claim.class, claimKey);
													
							claimType = claim.getClaimType().getValue(); 
						}*/
						
//						reminderDetailsDto.setClaimType(claimType);
//						reminderDetailsDto.setReminderType(reminderType);
						reminderDetailsDto.setLetterGeneratedDate(claimReminderDetailsObjList.get(0).getGeneratedDate());
						reminderDetailsDto.setTotalNoofRecords(claimReminderDetailsObjList.size());
						
						if(reminderDetailsDto.getLetterGeneratedDate() != null){
							String status = reminderDetailsDto.getPrint() != null ? ( reminderDetailsDto.getPrint().equalsIgnoreCase("n") ? "In Progress" : "Completed" ) : "";	
							reminderDetailsDto.setStatus(status);
						}				
					
						reminderDetailsDto.setSno(index++);
						
						for(ClaimReminderDetails reminderObj : claimReminderDetailsObjList){
							SearchPrintReminderBulkTableDTO exportExcelDto = populateExportExcelDto(reminderObj, reminderType);
							exportExcelDto.setSno(String.valueOf(claimReminderDetailsObjList.indexOf(reminderObj)+1));
							exportExcelList.add(exportExcelDto);
						}	
						reminderDetailsDto.setResultListDto(exportExcelList);
										
						resultList.add(reminderDetailsDto);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return resultList;
	}
	
/*	private PagedTaskList validateTasks(PagedTaskList tasklist, String userName, String passWord)
	{
		PagedTaskList validTaskList = new PagedTaskList();
		PagedTaskList invalidTaskList = new PagedTaskList();
		
		List<HumanTask> validHTList = new ArrayList<HumanTask>();
		List<HumanTask> invalidHTList = new ArrayList<HumanTask>();
		
		Long claimKey = null;
		Long queryKey = null;
		
		try{
		
		if(tasklist != null){
			List<HumanTask> totalTasklist = tasklist.getHumanTasks();
			int totalRecords = tasklist.getTotalRecords();
			
			if(totalTasklist != null && !totalTasklist.isEmpty()){
				
				for(HumanTask ht : totalTasklist){
					
					if(ht.getPayload() instanceof PayloadBOType){
							claimKey = ht.getPayload() != null && ht.getPayload().getClaim() != null && ht.getPayload().getClaim().getKey() != null ? ht.getPayload().getClaim().getKey() : null;
							queryKey = ht.getPayload() != null && ht.getPayload().getQuery() != null && ht.getPayload().getQuery().getKey() != null ? ht.getPayload().getQuery().getKey() : null;
					}
					else{
						claimKey = ht.getPayloadCashless() != null && ht.getPayloadCashless().getClaim() != null && ht.getPayloadCashless().getClaim().getKey() != null ? ht.getPayloadCashless().getClaim().getKey() : null;
					}
					SubmitGenerateReminderLetterTask submitTask = BPMClientContext.getSubmitQueryReimnderLetterTask(userName,passWord);
					if(queryKey != null){
						Query queryReplyObjectq = entityManager
								.createNamedQuery("ReimbursementQuery.findByKey");
						queryReplyObjectq.setParameter("primaryKey",
								queryKey);
						List<ReimbursementQuery> queryList = (List<ReimbursementQuery>)queryReplyObjectq.getResultList();
						
						if(queryList != null && !queryList.isEmpty()){
							ReimbursementQuery queryReplyObject = queryList.get(0);
							entityManager.refresh(queryReplyObject);
							
							if(queryReplyObject.getQueryReply() == null){
								
								validHTList.add(ht);
							}
							else{
									invalidHTList.add(ht);
									
									if(ht.getPayloadCashless() != null){
										ht.setOutcome("SUBMIT");
										SubmitGenerateLetterTask submitCLRemLetterTask = BPMClientContext.getSubmitReimbReimnderLetterTask(userName,passWord);
										submitCLRemLetterTask.execute(userName, ht);
									}
									else{
										ht.setOutcome("SUBMIT");										
										submitTask.execute(userName, ht);
									}
										
							}
						}
					}	
					else if(claimKey != null){
						Query rodObjectQuery = entityManager
								.createNamedQuery("StageInformation.findClaimByStatus");
						rodObjectQuery.setParameter("claimkey",
								claimKey);
						rodObjectQuery.setParameter("stageKey",
								ReferenceTable.CREATE_ROD_STAGE_KEY);
						rodObjectQuery.setParameter("statusKey",
								ReferenceTable.CREATE_ROD_STATUS_KEY);

						List<StageInformation> rodStageList = (List<StageInformation>) rodObjectQuery
								.getResultList();
						
						if(rodStageList != null && !rodStageList.isEmpty()){
							invalidHTList.add(ht);
							
							if(ht.getPayloadCashless() != null){
								SubmitCLReminderTask submitCLRemLetterTask = BPMClientContext.getSubmitCLReimnderTask(userName,passWord);
								
								try{
									ht.setOutcome("SUBMIT");
									submitCLRemLetterTask.execute(userName, ht);
									
								}
								catch(Exception e1){
									System.out.println("Task Out Come Approve");
									ht.setOutcome("APPROVE");
									submitCLRemLetterTask.execute(userName, ht);									
								}
								
							}
							else{
								try{
									ht.setOutcome("APPROVE");
									submitTask.execute(userName, ht);	
								}
								catch(Exception excp){
									
									ht.setOutcome("SUBMIT");
									submitTask.execute(userName, ht);
								}
							}							
							
						}
						else{
							validHTList.add(ht);
						}
					}
					
				}
			}
			totalRecords = tasklist.getTotalRecords() - invalidHTList.size();
			tasklist.setHumanTasks(validHTList);
			tasklist.setTotalRecords(totalRecords);
			
		}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.toString());
		}

		return tasklist;
	}*/
	
}
