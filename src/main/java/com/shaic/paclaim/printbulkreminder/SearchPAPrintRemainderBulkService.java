/**
 * 
 */
package com.shaic.paclaim.printbulkreminder;

import java.io.File;
import java.nio.file.Path;
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
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
//import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.mapper.AcknowledgeDocumentReceivedMapper;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimReminderDetails;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.MasClaimRemainderSkip;
import com.shaic.domain.MasOmbudsman;
import com.shaic.domain.MasterService;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.PACoveringLetterDocument;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.CashlessWorkFlow;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.reimbursement.printReminderLetterBulk.PrintBulkReminderResultDto;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkFormDTO;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintReminderBulkTableDTO;
import com.shaic.reimbursement.reminderBulkSearch.BulkReminderResultDto;
import com.shaic.reimbursement.reminderBulkSearch.SearchGenerateReminderBulkTableDTO;
import com.vaadin.server.VaadinSession;
/**
 * 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SearchPAPrintRemainderBulkService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	private final Logger log = LoggerFactory.getLogger(SearchPAPrintRemainderBulkService.class);
	
	public SearchPAPrintRemainderBulkService() {
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
				
				Predicate lobPredicate = builder.like(builder.upper(claimReminderRoot.<String>get("lobTypeFlag")), SHAConstants.PA_LOB_TYPE);
				predicates.add(lobPredicate);
				
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
						
//						if (null != reminderType && SHAConstants.PAN_CARD.equalsIgnoreCase(category) ) {
//							Long reminderCount = searchFormDTO.getReminderType() != null ? (searchFormDTO
//								.getReminderType().getId() != null ? searchFormDTO
//								.getReminderType().getId() : 0) : 0;
//								
//								Predicate reminderCountPredicate = builder.equal(claimReminderRoot.<Long>get("reminderCount"), reminderCount);
//								predicates.add(reminderCountPredicate);										
//						}					
					}
					else{
						List<String> categoryList = new ArrayList<String>();
						categoryList.add(SHAConstants.PA_BILLS_NOT_RECEIVED_OTHERS);
						categoryList.add(SHAConstants.PA_BILLS_NOT_RECEIVED_DEATH);
						categoryList.add(SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
						categoryList.add(SHAConstants.PA_QUERY);
						categoryList.add(SHAConstants.PA_PAYMENT_QUERY);
						Predicate categoryPredicate = claimReminderRoot.<String>get("category").in(categoryList);
						predicates.add(categoryPredicate);
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
				
				queryObj.setReminderCount(queryDto.getReminderCount() != null ? Long.valueOf(queryDto.getReminderCount()): null);
				queryObj.setReminderDate1(queryDto.getFirstReminderDate());
				queryObj.setReminderDate2(queryDto.getSecondReminderDate());
				queryObj.setReminderDate3(queryDto.getThirdReminderDate());
				entityManager.merge(queryObj);
				entityManager.flush();
				entityManager.clear();
				
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
				entityManager.merge(claimObj);
				entityManager.flush();
				entityManager.clear();
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
				
				entityManager.merge(claimObj);
				entityManager.flush();
				entityManager.clear();
			}
		}	
		utx.commit();
		}
		catch(Exception e){
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
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
	
	@SuppressWarnings("unchecked")
	public void submitBulkLetter(PrintBulkReminderResultDto reminderDetailsDto){
		try{
			
			utx.begin();
		String batchId = reminderDetailsDto.getBatchid();
		List<ClaimReminderDetails> batchIdList = new ArrayList<ClaimReminderDetails>();
		if(batchId != null){
			Query reminderQuery = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchId");
			reminderQuery.setParameter("batchId",batchId.toLowerCase()); 
			batchIdList =  reminderQuery.getResultList();			
		}
		
		if(batchIdList != null && !batchIdList.isEmpty()){
			for(ClaimReminderDetails claimReminderObj :batchIdList){
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
				e1.printStackTrace();
			}
		}		
	}
	
	
	public void submitReminderDetails(SearchPrintReminderBulkTableDTO reminderDetailsDto){
	
		
		if(reminderDetailsDto != null && reminderDetailsDto.getBatchid() != null && ! reminderDetailsDto.getBatchid().isEmpty()){
		ClaimReminderDetails resultObj = new ClaimReminderDetails();
		
		resultObj.setIntimationKey(reminderDetailsDto.getClaimDto().getNewIntimationDto().getKey());
		resultObj.setIntimatonNo(reminderDetailsDto.getClaimDto().getNewIntimationDto().getIntimationId());
		resultObj.setQueryKey(reminderDetailsDto.getReimbQueryDto() != null ? reminderDetailsDto.getReimbQueryDto().getKey() : null);
		resultObj.setTransacKey(reminderDetailsDto.getPreauthKey());
		resultObj.setClaimNo(reminderDetailsDto.getClaimDto().getClaimId());
		resultObj.setClaimKey(reminderDetailsDto.getClaimDto().getKey());
		resultObj.setLobTypeFlag(SHAConstants.PA_LOB_TYPE);
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
						reminderDetailsDto.setReminderCount(claimReminderDetailsObjList.get(0).getReminderCount());
						reminderDetailsDto.setPrint(claimReminderDetailsObjList.get(0).getPrintFlag());
						reminderDetailsDto.setPrintCount(claimReminderDetailsObjList.get(0).getPrintCount());
						
						String reminderType = claimReminderDetailsObjList.get(0).getReimainderCategory() != null ? claimReminderDetailsObjList.get(0).getReimainderCategory() : "";	
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
	
	@SuppressWarnings("unchecked")
	public void searchNGeneratePAReminderLetterBulk(MasterService masterService)	{		
		try{
			
		Query workFlowQuery = entityManager.createNamedQuery("CashlessWorkFlow.findPendingPAReminderLetter");
		
		String lobStr = SHAConstants.PA_LOB;
		List<String> queList = new ArrayList<String>();
		queList.add(SHAConstants.FIRST_REMINDER_LETTER_CURRENT_QUEUE);
		queList.add(SHAConstants.SECOND_REMINDER_LETTER_CURRENT_QUEUE);
		queList.add(SHAConstants.THIRD_REMINDER_LETTER_CURRENT_QUEUE);

		List<String> clmTypeList = new ArrayList<String>();
		clmTypeList.add(SHAConstants.CASHLESS_CLAIM_TYPE);
		clmTypeList.add(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
		
		List<String> remCategory = new ArrayList<String>();
		remCategory.add(SHAConstants.PA_BILLS_NOT_RECEIVED_OTHERS);
		remCategory.add(SHAConstants.PA_BILLS_NOT_RECEIVED_DEATH);
		remCategory.add(SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
		remCategory.add(SHAConstants.PA_QUERY);
		remCategory.add(SHAConstants.PA_PAYMENT_QUERY);
		
		List<SelectValue> cpuCodeList = (new MasterService()).getTmpCpuCodeListWithoutDescription(entityManager);
		
		Path tempDir = SHAFileUtils.createTempDirectory("dummyHolder");
		
		workFlowQuery.setParameter("lobStr", lobStr.toLowerCase());
		
		for(String curQ : queList){
			workFlowQuery.setParameter("curQ", curQ);			
			
			BulkReminderResultDto bulkSearchresultDto = new BulkReminderResultDto();			
			
			List<File> filelistForMerge = null;
			List<CashlessWorkFlow> pendingTaskList = new ArrayList<CashlessWorkFlow>();
			for(String claimType : clmTypeList){
		
				
				for(String categ : remCategory){
					for(SelectValue cpuCode : cpuCodeList){
						Integer totalRecords = 0;		
						
						workFlowQuery.setParameter("clmType", claimType);
						workFlowQuery.setParameter("cpuCode", Long.valueOf(cpuCode.getValue()));
						workFlowQuery.setParameter("remCateg", categ.toUpperCase());
						
						pendingTaskList = workFlowQuery.getResultList();
					    String batchId = "";
					    int remCount = 0;
					     
					    DBCalculationService dbCalService = new DBCalculationService();
				
					    List<SearchGenerateReminderBulkTableDTO> tableListDTO = new ArrayList<SearchGenerateReminderBulkTableDTO>();
					    try{
					    
					    if(pendingTaskList != null && !pendingTaskList.isEmpty()){
					    	
					    	utx.setTransactionTimeout(360000);
							utx.begin();
					    	totalRecords = pendingTaskList.size();
							for(CashlessWorkFlow taskObj : pendingTaskList)
							{
								SearchGenerateReminderBulkTableDTO searchTableDto = new SearchGenerateReminderBulkTableDTO();
								ClaimDto claimDto = new ClaimDto();
								searchTableDto.setDbOutArray(taskObj);
								List<StageInformation> rodStageList = null;
								Long claimKey = null;
								Long cashlessKey = null;
								Long queryKey = null;
								List<StageInformation> preauthStageList = null;
								List<StageInformation> enhancementStageList = null;
								
								if (taskObj.getRemType() != null) {
									String remType = taskObj.getRemType();
									searchTableDto.setReminderType(remType);
								}
			
								if(taskObj.getClaimKey() != null && taskObj.getClaimKey().intValue() != 0){
									claimKey = taskObj.getClaimKey();
								}
								
								queryKey = taskObj.getQueryKey();
								cashlessKey = taskObj.getCashlessKey(); 
							
								if (claimKey != null) {
									Claim claimObj = getClaimByClaimKey(claimKey);
									if(claimObj != null){
										
									    claimDto = ClaimMapper.getInstance().getClaimDto(claimObj);
										claimDto.setModifiedDate(claimObj.getModifiedDate());
										
										claimDto.setDocumentCheckListDTO(getPADocumentCheckListValuesByClaimKey(claimDto.getKey()));										
										
										searchTableDto
										.setClaimIntimationNo(claimObj
												.getIntimation().getIntimationId() != null ? claimObj
												.getIntimation()
												.getIntimationId()
												: "");
										searchTableDto
										.setIntimationDate(claimObj
												.getIntimation().getCreatedDate() != null ? new SimpleDateFormat(
												"dd/MM/yyyy").format(claimObj
												.getIntimation()
												.getCreatedDate()) : "");
										searchTableDto.setPatientName(claimObj
										.getIntimation().getInsured()
										.getInsuredName() != null ? claimObj
										.getIntimation().getInsured()
										.getInsuredName() : "");
										searchTableDto.setClaimDto(claimDto);
										
											Date remDate = new Date();
											if ((SHAConstants.FIRST_REMINDER).equalsIgnoreCase(searchTableDto
													.getReminderType())) {
												claimDto.setFirstReminderDate(remDate);
												remCount = 1;
											}
											if ((SHAConstants.SECOND_REMINDER).equalsIgnoreCase(searchTableDto
													.getReminderType())) {
												claimDto.setSecondReminderDate(new Date());
												remCount = 2;
											}
											if ((SHAConstants.CLOSE).equalsIgnoreCase(searchTableDto
													.getReminderType())) {
												claimDto.setFirstReminderDate(claimObj.getFirstReminderDate());
												claimDto.setSecondReminderDate(claimObj.getSecondReminderDate());
												claimDto.setThirdReminderDate(new Date());
												remCount = 3;
											}
										claimDto.setReminderCount(remCount);
										searchTableDto.setIntimationKey(claimObj.getIntimation().getKey());
										
										 if (claimObj.getIntimation().getPolicy().getHomeOfficeCode() != null) {
											 List<MasOmbudsman> ombudsmanOfficeList = getOmbudsmanOffiAddrByPIOCode(claimObj.getIntimation().getPolicy().getHomeOfficeCode(),masterService);
											 if(ombudsmanOfficeList !=null && !ombudsmanOfficeList.isEmpty())
												 claimDto.setOmbudsManAddressList(ombudsmanOfficeList);
										 }

										Preauth preauthObj = getLatestPreauthByClaimKey(claimObj.getKey());
										if (preauthObj != null) {
											searchTableDto.getClaimDto().setPreauthApprovedDate(claimObj
													.getCreatedDate());
											Long preauthKey = preauthObj.getKey();
											searchTableDto.getClaimDto().setProvisionAmount(preauthObj.getTotalApprovalAmount());
											searchTableDto
													.setPreauthKey(preauthKey);
										}								
				
										if (queryKey != null && queryKey.intValue() != 0){								
											
												searchTableDto.setQueryKey(queryKey);
										}	
										if(taskObj.getRemCategory() != null ){
											searchTableDto.setCategory(taskObj.getRemCategory());
										}	
											tableListDTO.add(searchTableDto);
										if(SHAConstants.THIRD_REMINDER_LETTER_CURRENT_QUEUE.equalsIgnoreCase(curQ)){
											DBCalculationService dbService = new DBCalculationService();
											dbService.pullBackSubmitProcedure(taskObj.getKey(),SHAConstants.OUTCOME_SUBMIT_REIMINDER_PROCESS,SHAConstants.BATCH_USER_ID);
										}
								}
							}
						}
							bulkSearchresultDto.setReminderCount(remCount);	
							List<SearchGenerateReminderBulkTableDTO> resultListDto = new ArrayList<SearchGenerateReminderBulkTableDTO>();
							if (!tableListDTO.isEmpty()) {								
								batchId = dbCalService.generateReminderBatchId(SHAConstants.REMINDER_BATCH_SEQUENCE_NAME);           
								batchId = batchId != null && !("").equals(batchId) ? SHAConstants.BULK_REMINDER + batchId : "";
								
								bulkSearchresultDto.setBatchid(batchId);
								filelistForMerge = new ArrayList<File>();
								int totalListSize = tableListDTO.size();
								
								for (SearchGenerateReminderBulkTableDTO resultDto : tableListDTO) {
									ReimbursementQueryDto queryDto = null;
			
									resultDto.setBatchid(batchId);
									resultDto.setSubBatchId(batchId);
									if (resultDto.getQueryKey() != null) {
										queryDto = (new ReimbursementQueryService())
												.getReimbursementQuery(resultDto.getQueryKey(),
														entityManager);
									}
			
									if (queryDto != null) {
										int qRemCount = 0;
										
										Date remDate = new Date();
										if(resultDto.getReminderType() != null){
											if (resultDto.getReminderType().equalsIgnoreCase(SHAConstants.FIRST_REMINDER)) {
												queryDto.setFirstReminderDate(remDate);
												qRemCount = 1;
											}
											if ((SHAConstants.SECOND_REMINDER).equalsIgnoreCase(resultDto.getReminderType())) {
												queryDto.setSecondReminderDate(new Date());
												qRemCount = 2;
											}
											if ((SHAConstants.CLOSE).equalsIgnoreCase(resultDto.getReminderType())) {
												queryDto.setThirdReminderDate(new Date());
												qRemCount = 3;
											}
											queryDto.setReminderCount(qRemCount);
										}			
			
									} else {
										queryDto = new ReimbursementQueryDto();
										ReimbursementDto reimbursementDto = new ReimbursementDto();
										reimbursementDto.setClaimDto(resultDto.getClaimDto());
										queryDto.setReimbursementDto(reimbursementDto);									
									}
			
									resultDto.setReimbQueryDto(queryDto);
									resultDto.setSno(String.valueOf(tableListDTO
											.indexOf(resultDto) + 1));
									resultDto.setUsername(BPMClientContext.DB_USER_NAME);
									resultListDto.add(resultDto);
									
									if(resultDto.getIntimationKey() != null){
										Intimation intimationObj = getIntimationByKey(resultDto.getIntimationKey());
										if(intimationObj != null){	
											NewIntimationMapper intimationMapper = NewIntimationMapper.getInstance();
											NewIntimationDto intimationDto = intimationMapper.getNewIntimationDto(intimationObj);
											
											TmpCPUCode cpuObject = intimationObj.getCpuCode();

											if (null != cpuObject) {
												intimationDto.setReimbCpuAddress(cpuObject.getReimbAddress());
											}
											OrganaizationUnit orgUnit = getOrgUnitByCode(intimationObj.getPolicy().getHomeOfficeCode());
											
											if(orgUnit != null){
												intimationDto.setOrganizationUnit(orgUnit);
												if(orgUnit.getParentOrgUnitKey() != null){
													OrganaizationUnit parentUnit = 	getOrgUnitByCode(orgUnit.getParentOrgUnitKey().toString());	
													intimationDto.setParentOrgUnit(parentUnit);
												}
											}
											
											Hospitals hospitalObj = getHospitalById(intimationObj.getHospital());
											if(hospitalObj != null){
												HospitalDto hospDto = new HospitalDto(hospitalObj);
												intimationDto.setHospitalDto(hospDto);
											}
										if(resultDto.getClaimDto() != null){
											resultDto.getClaimDto().setNewIntimationDto(intimationDto);	
										}
										if(resultDto.getReimbQueryDto() != null 
												&& resultDto.getReimbQueryDto().getReimbursementDto() != null 
												&& resultDto.getReimbQueryDto().getReimbursementDto() != null 
												&& resultDto.getReimbQueryDto().getReimbursementDto().getClaimDto() != null 
												&& resultDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto() == null)
												resultDto.getReimbQueryDto().getReimbursementDto().getClaimDto().setNewIntimationDto(intimationDto);
										}
										
										String fileUrl = generatePALetterPdfFile(resultDto);
										if(fileUrl != null && !fileUrl.isEmpty()){
										resultDto.setFileUrl(fileUrl);	
										resultDto = uploadReminderLetterToDMS(resultDto);									
										
										if(resultDto.getDocToken() != null){	
											String remfileUrl = getDocumentURLByToken(resultDto.getDocToken());
											
											String[] fileNameSplit = remfileUrl.split("\\.");
											String fileName = fileNameSplit.length > 0 ? fileNameSplit[0] :"";
											
											String fileviewUrl = SHAFileUtils.viewFileByToken(String.valueOf(resultDto.getDocToken()));
										
											File tmpFile = SHAFileUtils.downloadFileForCombinedView(fileName,fileviewUrl,tempDir);
											Boolean isPoiAvailable = getSkipClaimRemainderByPOI(Long.valueOf(intimationObj.getPolicy().getHomeOfficeCode()));
											if(!isPoiAvailable){
											filelistForMerge.add(tmpFile);
											}
										}
										
										submitReminderDetails(resultDto);
									  }	
									}
							
									bulkSearchresultDto.setResultListDto(resultListDto);	
									bulkSearchresultDto.setTotalNoofRecords(totalRecords);
								}
								
								File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bulkSearchresultDto.getBatchid());
								
								if(mergedDoc != null){
								String mergedFileUrl = mergedDoc.getAbsolutePath();
								
//									Need to Upload the above Merged Doc to DMS
								WeakHashMap<String, String> dataMap = new WeakHashMap<String, String>();
								dataMap.put("filePath", mergedFileUrl);
								dataMap.put("docType",SHAConstants.REMINDER_LETTER);
								dataMap.put("docSources", SHAConstants.GENERATE_REMINDER_LETTER);
								dataMap.put("createdBy", BPMClientContext.DB_USER_NAME);
								
								//For testing Purpose commented Need to uncomment below line
								String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
								SHAUtils.setClearMapStringValue(dataMap);
								bulkSearchresultDto.setDocToken(Long.valueOf(docToken));
								
							}
								
								if(resultListDto == null || resultListDto.isEmpty()){
									bulkSearchresultDto.setBatchid(null);
								}
								else{
									if(filelistForMerge != null && !filelistForMerge.isEmpty()){
											
										ClaimReminderDetails summaryBatchObj = new ClaimReminderDetails();
			
										summaryBatchObj.setIntimationKey(null);
										summaryBatchObj.setIntimatonNo(bulkSearchresultDto.getBatchid());
										summaryBatchObj.setQueryKey(null);
										summaryBatchObj.setTransacKey(null);
										summaryBatchObj.setClaimNo(bulkSearchresultDto.getBatchid());
										summaryBatchObj.setClaimKey(null);
										
										summaryBatchObj.setBatchNo(bulkSearchresultDto.getBatchid());
										summaryBatchObj.setSubBatchNo(bulkSearchresultDto.getBatchid());
										summaryBatchObj.setCategory(categ);
										summaryBatchObj.setCpuCode(Long.valueOf(cpuCode.getValue()));
										summaryBatchObj.setLobTypeFlag(SHAConstants.PA_LOB_TYPE);
										summaryBatchObj.setReminderCount(remCount);
										summaryBatchObj.setPrintFlag("N");
										summaryBatchObj.setBatchFlag("Y");
										summaryBatchObj.setReimainderCategory(SHAUtils.getCurrentRemQueName(curQ));
										summaryBatchObj.setPrintCount(totalRecords);
										summaryBatchObj.setClaimType(claimType);
										summaryBatchObj.setDocumentToken(bulkSearchresultDto.getDocToken());
										summaryBatchObj.setGeneratedDate(SHAUtils.getCalendar(new Date()).getTime());
										summaryBatchObj.setCreatedBy(SHAConstants.BATCH_USER_ID);
										summaryBatchObj.setCreatedDate(SHAUtils.getCalendar(new Date()).getTime());
										entityManager.persist(summaryBatchObj);	
										filelistForMerge = null;
									}
							   }
							}
							utx.commit();
					    }
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
			}			
		}
		
//		utx.commit();
	}
	catch(Exception e){
		e.printStackTrace();		
	 }
	}
	
	public List<DocumentCheckListDTO> getPADocumentCheckListValuesByClaimKey(Long claimKey) {
		
		//TODO  Need  to Get Saved Docs.  for this claim from new table
		
		List<DocumentCheckListMaster> masterDocumentCheckList = getPADocumentCheckListValuesByType(claimKey);

		AcknowledgeDocumentReceivedMapper acknowledgeDocumentReceivedMapper = AcknowledgeDocumentReceivedMapper.getInstance();
		
		List<DocumentCheckListDTO> documentCheckListDTO = acknowledgeDocumentReceivedMapper
				.getMasDocumentCheckList(masterDocumentCheckList);
		List<DocumentCheckListDTO> documentCheckListDTOList = new ArrayList<DocumentCheckListDTO>();
		
		if(documentCheckListDTO != null && !documentCheckListDTO.isEmpty()){
		
			for (DocumentCheckListDTO docCheckListDto : documentCheckListDTO) {
	
				docCheckListDto.setSlNo(documentCheckListDTO.indexOf(docCheckListDto)+1);
				documentCheckListDTOList.add(docCheckListDto);			
			}
		}
		return documentCheckListDTOList;
	}

	public List<DocumentCheckListMaster> getPADocumentCheckListValuesByType(Long claimKey) 
	{	
		List<DocumentCheckListMaster>  finalResultList = new ArrayList<DocumentCheckListMaster>(); 
		Query query = entityManager
				.createNamedQuery("PACoveringLetterDocument.findByClaimKey"); 
		query.setParameter("claimKey", claimKey);
		
		List<PACoveringLetterDocument> resultList = (List<PACoveringLetterDocument>) query
				.getResultList(); 
		
		if(resultList != null && !resultList.isEmpty()){
			for (PACoveringLetterDocument paCoveringLetterDocument : resultList) {
				finalResultList.add(paCoveringLetterDocument.getDocMaster());
			}
		}		
		
		return finalResultList;
	}
public Preauth getLatestPreauthByClaimKey(Long claimKey){
	Preauth preObj = null;
	if(claimKey != null){
		try{
			Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
			query.setParameter("claimkey", claimKey);
			List<Preauth> preauthList = (List<Preauth>) query.getResultList();
			
			if(preauthList != null && ! preauthList.isEmpty()){
				entityManager.refresh(preauthList.get(0));
				preObj = preauthList.get(0);
			}					 
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	return preObj;
}
	
public Intimation getIntimationByKey(Long intimationKey) {

	Query findByKey = entityManager
			.createNamedQuery("Intimation.findByKey").setParameter(
					"intiationKey", intimationKey);

	List<Intimation> intimationList = (List<Intimation>) findByKey
			.getResultList();

	if (!intimationList.isEmpty()) {
		entityManager.refresh(intimationList.get(0));
		return intimationList.get(0);

	}
	return null;
}

public OrganaizationUnit getOrgUnitByCode(String polDivnCode) {
	List<OrganaizationUnit> organizationUnit = null;
	if(polDivnCode != null){			
		Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId").setParameter("officeCode", polDivnCode);
		organizationUnit = (List<OrganaizationUnit>)findAll.getResultList();
	}
	
	if(organizationUnit != null && ! organizationUnit.isEmpty()){
		return organizationUnit.get(0);
	}
	
	
	return null;
}

public Hospitals getHospitalById(Long key){
	if(key != null){
		try{
			Query query = entityManager.createNamedQuery("Hospitals.findByKey");
			query.setParameter("key", key);
			
			List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
			
			if(resultList != null && ! resultList.isEmpty()){
				return resultList.get(0);
			}		
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	return null;		
}

private String generatePALetterPdfFile(SearchGenerateReminderBulkTableDTO reminderLetterDto){
	ReportDto reportDto = new ReportDto();
	DocumentGenerator docGenarator = new DocumentGenerator();
	String fileUrl = "";
	
	List<NomineeDetailsDto> nomineeList = new ArrayList<NomineeDetailsDto>();
	
	try{
		nomineeList = (new IntimationService()).getNomineeDetailsListByTransactionKey(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getKey(), entityManager);
		if(nomineeList != null && !nomineeList.isEmpty() ) {
			
		   	reminderLetterDto.getClaimDto().getNewIntimationDto().setNomineeList(nomineeList);
		   	
		    StringBuffer nomineeNames = new StringBuffer("");
		    
		    for (NomineeDetailsDto nomineeDetailsDto : nomineeList) {
		    	nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName().trim()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName().trim()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
		    }
		    reminderLetterDto.getClaimDto().getNewIntimationDto().setNomineeName(nomineeNames.toString());
		}else{
			nomineeList = (new IntimationService()).getNomineeForPolicyKey(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getPolicy().getKey(), entityManager);
			
			if(nomineeList != null && !nomineeList.isEmpty() ) {
				
			   	reminderLetterDto.getClaimDto().getNewIntimationDto().setNomineeList(nomineeList);
			   	
			    StringBuffer nomineeNames = new StringBuffer("");
			    
			    for (NomineeDetailsDto nomineeDetailsDto : nomineeList) {
			    	nomineeNames = nomineeNames.toString().isEmpty() ? (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getAppointeeName().trim()) : nomineeNames.append(nomineeDetailsDto.getNomineeName())) : (nomineeDetailsDto.getAppointeeName() != null && !nomineeDetailsDto.getAppointeeName().trim().isEmpty() ? nomineeNames.append(", ").append(nomineeDetailsDto.getAppointeeName().trim()) : nomineeNames.append(", ").append(nomineeDetailsDto.getNomineeName()));
			    }
			    reminderLetterDto.getClaimDto().getNewIntimationDto().setNomineeName(nomineeNames.toString());
			}
		}
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	
	List<LegalHeirDTO> legalHeirDtoList = new ArrayList<LegalHeirDTO>();
	
	if(nomineeList == null || (nomineeList != null && nomineeList.isEmpty())) {
		
		List<LegalHeir> legalHeirList = (new MasterService()).getlegalHeirListByTransactionKey(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getKey(), entityManager);
		
		if(legalHeirList != null && !legalHeirList.isEmpty()) {
			LegalHeirDTO legalHeirDto;
			for (LegalHeir legalHeir : legalHeirList) {
				 legalHeirDto = new LegalHeirDTO(legalHeir);
				 legalHeirDtoList.add(legalHeirDto);
			}
			reminderLetterDto.getReimbQueryDto().getReimbursementDto().setLegalHeirDTOList(legalHeirDtoList);
		}		
	}
	
	if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && (SHAConstants.PA_QUERY).equalsIgnoreCase(reminderLetterDto.getCategory())){
		ReimbursementQueryDto queryDto = reminderLetterDto.getReimbQueryDto();
		reportDto.setClaimId(queryDto.getClaimId());
		List<ReimbursementQueryDto> queryDtoList = new ArrayList<ReimbursementQueryDto>();
		
		/*queryDtoList.add(queryDto);	
		reportDto.setBeanList(queryDtoList);
		
		fileUrl = docGenarator.generatePdfDocument("PAReimburseQueryReminderLetter", reportDto);*/
		
		String templateName = "PAReimburseQueryReminderLetter";
		
		if(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& (SHAConstants.DEATH_FLAG).equalsIgnoreCase(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue())
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
			
			if(nomineeList == null ||
					(nomineeList != null && nomineeList.isEmpty())){
				
				if(legalHeirDtoList != null && !legalHeirDtoList.isEmpty()) {
					Path tempDir = SHAFileUtils.createTempDirectory("dummyHolder");
					ArrayList<File> filelistForMerge = new ArrayList<File>();
					
					for (LegalHeirDTO legalHeirDTO : legalHeirDtoList) {
						reminderLetterDto.getReimbQueryDto().getReimbursementDto().setNomineeName(legalHeirDTO.getHeirName());
						reminderLetterDto.getReimbQueryDto().getReimbursementDto().setNomineeAddr(legalHeirDTO.getAddress()+(legalHeirDTO.getPincode() != null ? ("\nPinCode : "+legalHeirDTO.getPincode()): ""));
						queryDtoList = new ArrayList<ReimbursementQueryDto>();
						queryDtoList.add(reminderLetterDto.getReimbQueryDto());
						reportDto.setBeanList(queryDtoList);
						fileUrl = docGenarator.generatePdfDocument(templateName, reportDto);
						try{
							File fl = new File(fileUrl);
							filelistForMerge.add(fl);
						}
						catch(Exception e) {
//							e.printStackTrace();
						}
					}						
					if(filelistForMerge != null && !filelistForMerge.isEmpty()) {
						File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getClaimId().replaceAll("/", "_"));
						fileUrl =  mergedDoc.getAbsolutePath();
					}
					
				}
				else {
					queryDtoList = new ArrayList<ReimbursementQueryDto>();
					queryDtoList.add(queryDto);
					reportDto.setBeanList(queryDtoList);
					fileUrl = docGenarator.generatePdfDocument(templateName, reportDto);
					
				}
			}	
			else {
				queryDtoList = new ArrayList<ReimbursementQueryDto>();
				queryDtoList.add(queryDto);
				reportDto.setBeanList(queryDtoList);
				fileUrl = docGenarator.generatePdfDocument(templateName, reportDto);
				
			}
			
		}	
		else {
			queryDtoList = new ArrayList<ReimbursementQueryDto>();
			queryDtoList.add(queryDto);
			reportDto.setBeanList(queryDtoList);
			fileUrl = docGenarator.generatePdfDocument(templateName, reportDto);
			
		}
		
		
	}

	else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && (SHAConstants.PA_PAYMENT_QUERY).equalsIgnoreCase(reminderLetterDto.getCategory())){
		ReimbursementQueryDto queryDto = reminderLetterDto.getReimbQueryDto();
		reportDto.setClaimId(queryDto.getClaimId());
		List<ReimbursementQueryDto> queryDtoList = new ArrayList<ReimbursementQueryDto>();
		

		/*queryDtoList.add(queryDto);	
		reportDto.setBeanList(queryDtoList);
		
		fileUrl = docGenarator.generatePdfDocument("PAPaymentQueryReminderLetter", reportDto);*/
		
		String templateName="PAPaymentQueryReminderLetter";
		
		if(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& (SHAConstants.DEATH_FLAG).equalsIgnoreCase(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue())
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
			
			if(nomineeList == null ||
					(nomineeList != null && nomineeList.isEmpty())){
				
				if(legalHeirDtoList != null && !legalHeirDtoList.isEmpty()) {
					Path tempDir = SHAFileUtils.createTempDirectory("dummyHolder");
					ArrayList<File> filelistForMerge = new ArrayList<File>();
					
					for (LegalHeirDTO legalHeirDTO : legalHeirDtoList) {
						reminderLetterDto.getReimbQueryDto().getReimbursementDto().setNomineeName(legalHeirDTO.getHeirName());
						reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeName(legalHeirDTO.getHeirName());
						reminderLetterDto.getReimbQueryDto().getReimbursementDto().setNomineeAddr(legalHeirDTO.getAddress()+(legalHeirDTO.getPincode() != null ? ("\nPinCode : "+legalHeirDTO.getPincode()): ""));
						reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setNomineeAddr(reminderLetterDto.getReimbQueryDto().getReimbursementDto().getNomineeAddr());
						queryDtoList = new ArrayList<ReimbursementQueryDto>();
						queryDtoList.add(reminderLetterDto.getReimbQueryDto());
						reportDto.setBeanList(queryDtoList);
						fileUrl = docGenarator.generatePdfDocument(templateName, reportDto);
						try{
							File fl = new File(fileUrl);
							filelistForMerge.add(fl);
						}
						catch(Exception e) {
//							e.printStackTrace();
						}
					}						
					if(filelistForMerge != null && !filelistForMerge.isEmpty()) {
						File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().getClaimId().replaceAll("/", "_"));
						fileUrl =  mergedDoc.getAbsolutePath();
					}
					
				}
				else {
					queryDtoList = new ArrayList<ReimbursementQueryDto>();
					queryDtoList.add(queryDto);
					reportDto.setBeanList(queryDtoList);
					fileUrl = docGenarator.generatePdfDocument(templateName, reportDto);
					
				}
			}	
			else {
				queryDtoList = new ArrayList<ReimbursementQueryDto>();
				queryDtoList.add(queryDto);
				reportDto.setBeanList(queryDtoList);
				fileUrl = docGenarator.generatePdfDocument(templateName, reportDto);
				
			}
			
		}	
		else {
			queryDtoList = new ArrayList<ReimbursementQueryDto>();
			queryDtoList.add(queryDto);
			reportDto.setBeanList(queryDtoList);
			fileUrl = docGenarator.generatePdfDocument(templateName, reportDto);
			
		}	
		
		
	}
	
	else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && (SHAConstants.PA_BILLS_NOT_RECEIVED_OTHERS).equalsIgnoreCase(reminderLetterDto.getCategory())){
		ClaimDto claimDto = reminderLetterDto.getClaimDto();
		List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
		claimDtoList.add(claimDto);
		reportDto.setBeanList(claimDtoList);
		reportDto.setClaimId(claimDto.getClaimId());
		
		fileUrl = docGenarator.generatePdfDocument("PAReimburseClaimReminderLetter", reportDto);
		
	}

	else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && (SHAConstants.PA_BILLS_NOT_RECEIVED_DEATH).equalsIgnoreCase(reminderLetterDto.getCategory())){
		List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
		reportDto.setClaimId(reminderLetterDto.getClaimDto().getClaimId());
		
		
		//IMSSUPPOR-31058	
		try{
			nomineeList = (new IntimationService()).getNomineeForPolicyKey(reminderLetterDto.getClaimDto().getNewIntimationDto().getPolicy().getKey(), entityManager);
			if(nomineeList != null && !nomineeList.isEmpty() ) {
				
			   	reminderLetterDto.getClaimDto().getNewIntimationDto().setNomineeList(nomineeList);
			   	
			    StringBuffer nomineeNames = new StringBuffer("");
			    
			    for (NomineeDetailsDto nomineeDetailsDto : nomineeList) {
			    	nomineeNames = nomineeNames.toString().isEmpty() ? nomineeNames.append(nomineeDetailsDto.getNomineeName()) : nomineeNames.append(","+nomineeDetailsDto.getNomineeName());
			    }
			    reminderLetterDto.getClaimDto().getNewIntimationDto().setNomineeName(nomineeNames.toString());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		claimDtoList.add(reminderLetterDto.getClaimDto());
		reportDto.setBeanList(claimDtoList);
		fileUrl = docGenarator.generatePdfDocument("PADeathReminderLetter", reportDto);
		

	}
	else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && (SHAConstants.PREAUTH_BILLS_NOT_RECEIVED).equalsIgnoreCase(reminderLetterDto.getCategory()) ){
		ClaimDto claimDto = reminderLetterDto.getClaimDto();
		List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
		claimDtoList.add(claimDto);
		reportDto.setBeanList(claimDtoList);
		reportDto.setClaimId(claimDto.getClaimId());
		
		fileUrl = docGenarator.generatePdfDocument("CashlessReminderLetter", reportDto);
		
	}
	
//	else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && SHAConstants.PAN_CARD.equalsIgnoreCase(reminderLetterDto.getCategory())){
//		ClaimDto claimDto = reminderLetterDto.getClaimDto();
//		List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
//		claimDtoList.add(claimDto);
//		reportDto.setBeanList(claimDtoList);
//		reportDto.setClaimId(claimDto.getClaimId());
//		
//		fileUrl = docGenarator.generatePdfDocument("PANCardCashlessReminderLetter", reportDto);
//		
//	}
	
	return fileUrl;
}

private SearchGenerateReminderBulkTableDTO uploadReminderLetterToDMS(SearchGenerateReminderBulkTableDTO reminderLetterDto){
	
	try{

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
	
	if(reminderLetterDto != null && reminderLetterDto.getCategory() != null &&  ((SHAConstants.PA_QUERY).equalsIgnoreCase(reminderLetterDto.getCategory()) || (SHAConstants.PA_PAYMENT_QUERY).equalsIgnoreCase(reminderLetterDto.getCategory())) ){
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
		}			
		
	}
	
	else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null &&  ((SHAConstants.PA_BILLS_NOT_RECEIVED_OTHERS).equalsIgnoreCase(reminderLetterDto.getCategory()) || (SHAConstants.PA_BILLS_NOT_RECEIVED_DEATH).equalsIgnoreCase(reminderLetterDto.getCategory()))){
		Query query = entityManager.createNamedQuery("Claim.findByKey");
		query.setParameter("primaryKey", claimDto.getKey());
		List<Claim> claimObjList = query.getResultList();
		if(claimObjList != null && !claimObjList.isEmpty()){
			Claim claimObj = claimObjList.get(0);
			entityManager.refresh(claimObj);
			
			claimObj.setReminderCount(claimDto.getReminderCount() != null ? claimDto.getReminderCount().longValue() : null);
			if(claimObj.getReminderCount() != null){
				if(claimObj.getReminderCount().intValue() == 1 ){
					claimObj.setFirstReminderDate(claimDto.getFirstReminderDate());	
				}
				if(claimObj.getReminderCount().intValue() == 2){
					claimObj.setSecondReminderDate(claimDto.getSecondReminderDate());	
				}
				if(claimObj.getReminderCount().intValue() == 3){
					claimObj.setThirdReminderDate(claimDto.getThirdReminderDate());	
				}				
			}
			
			//For testing Purpose commented Need to uncomment below lines
			entityManager.merge(claimObj);
			entityManager.flush();
		}	
	}
	else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null &&  (SHAConstants.PREAUTH_BILLS_NOT_RECEIVED).equalsIgnoreCase(reminderLetterDto.getCategory())){
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
		}
	}
	updateReminderPrintStatus(reminderLetterDto);
	}
	catch(Exception e){
		log.error(e.toString());
		e.printStackTrace();
	}
	
	return reminderLetterDto;

}

private void updateReminderPrintStatus(SearchGenerateReminderBulkTableDTO searchDto){

	CashlessWorkFlow currentTaskObj = (CashlessWorkFlow) searchDto.getDbOutArray();
	String query = "UPDATE IMS_CLS_SEC_WORK_FLOW SET REMINDER_LATTER_FLAG = "+"'Y'"+ "WHERE WK_KEY ="+currentTaskObj.getKey();
	
	Query nativeQuery = entityManager.createNativeQuery(query);
	nativeQuery.executeUpdate();		
}

public String getDocumentURLByToken(Long docToken){
	
	String fileUrl = "";
	
	Query query = entityManager
			.createNamedQuery("DocumentDetails.findByDocToken");

	query = query.setParameter("documentToken", docToken);
	List<DocumentDetails> docList = query.getResultList();
	
	if(docList != null && !docList.isEmpty()){
		fileUrl = docList.get(0).getSfFileName(); 
	}
	
	return fileUrl;
	
}

public void submitReminderDetails(SearchGenerateReminderBulkTableDTO reminderDetailsDto){
		
		if(reminderDetailsDto != null && reminderDetailsDto.getBatchid() != null && ! reminderDetailsDto.getBatchid().isEmpty()){
			ClaimReminderDetails resultObj = new ClaimReminderDetails();
			resultObj.setIntimationKey(reminderDetailsDto.getClaimDto().getNewIntimationDto().getKey());
			resultObj.setIntimatonNo(reminderDetailsDto.getClaimDto().getNewIntimationDto().getIntimationId());
			resultObj.setQueryKey(reminderDetailsDto.getReimbQueryDto() != null ? reminderDetailsDto.getReimbQueryDto().getKey() : null);
			resultObj.setTransacKey(reminderDetailsDto.getPreauthKey());
			resultObj.setClaimNo(reminderDetailsDto.getClaimDto().getClaimId());
			resultObj.setClaimKey(reminderDetailsDto.getClaimDto().getKey());
			resultObj.setLobTypeFlag(SHAConstants.PA_LOB_TYPE);
//			if(SHAConstants.PAN_CARD.equalsIgnoreCase(reminderDetailsDto.getCategory())){
//				resultObj.setReimainderCategory(SHAUtils.getCurrentRemQueNameForPan(reminderDetailsDto.getReminderType().toLowerCase()));
//			}else{
				resultObj.setReimainderCategory(reminderDetailsDto.getReminderType());	
//			}
			
			resultObj.setBatchNo(reminderDetailsDto.getBatchid());
			resultObj.setSubBatchNo(reminderDetailsDto.getSubBatchId());
			resultObj.setCategory(reminderDetailsDto.getCategory());
			resultObj.setBatchFlag("N");
			resultObj.setCpuCode(reminderDetailsDto.getClaimDto().getNewIntimationDto().getCpuCode() != null ? Long.valueOf(reminderDetailsDto.getClaimDto().getNewIntimationDto().getCpuCode()) : null);
			resultObj.setReminderCount(reminderDetailsDto.getClaimDto().getReminderCount());
			resultObj.setPrintFlag("N");
			resultObj.setPrintCount(0);
			resultObj.setDocumentToken(reminderDetailsDto.getDocToken());
			resultObj.setGeneratedDate(SHAUtils.getCalendar(new Date()).getTime());
			resultObj.setCreatedBy(SHAConstants.BATCH_USER_ID);
			resultObj.setCreatedDate(SHAUtils.getCalendar(new Date()).getTime());
			try{
				entityManager.persist(resultObj);
			}
			catch(Exception e){
				e.printStackTrace();
				
			}
		}
	}

private List<MasOmbudsman> getOmbudsmanOffiAddrByPIOCode(String pioCode, MasterService masterService) {
	
	List<MasOmbudsman> ombudsmanDetailsByCpuCode = new ArrayList<MasOmbudsman>();
	if(pioCode != null){
		OrganaizationUnit branchOffice = getOrgUnitByCode(pioCode);
		if (branchOffice != null) {
			String ombudsManCode = branchOffice.getOmbudsmanCode();
			if (ombudsManCode != null) {
				ombudsmanDetailsByCpuCode = masterService
						.getOmbudsmanDetailsByCpuCode(ombudsManCode);
			}
		}
	}
	return ombudsmanDetailsByCpuCode;
}
public Boolean getSkipClaimRemainderByPOI(Long polDivnCode) {
	List<MasClaimRemainderSkip> skipRemainderByPOI = null;
	try {
	if(polDivnCode != null){			
		Query findAll = entityManager.createNamedQuery("MasClaimRemainderSkip.findByIssuingOffice").setParameter("issuingOffKey", polDivnCode);
		skipRemainderByPOI = (List<MasClaimRemainderSkip>)findAll.getResultList();
	}
	
	if(skipRemainderByPOI != null && ! skipRemainderByPOI.isEmpty()){
		return true;
	}
	return false;
}
	catch(Exception e){
	e.printStackTrace();
	return false;
}
}
}
