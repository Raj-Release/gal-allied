package com.shaic.claim.bulkconvertreimb.search;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import org.apache.commons.lang.math.NumberUtils;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimConversion;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasProductCpuRouting;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.StarKotakPolicy;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.sass.internal.util.StringUtil;
import com.vaadin.server.VaadinSession;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SearchBulkConvertReimbService extends AbstractDAO<Claim> {

	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	public SearchBulkConvertReimbService() {
		super();
	}

/**
 * Top Level Service to Search BPM Task For Convert Claim to Reimb.
 * @param formDTO
 * @param userName
 * @param passWord
 * @return
 */	
	
	public List<SearchBulkConvertReimbTableDto> search(
			SearchBulkConvertFormDto formDTO, String userName, String passWord) {
		List<SearchBulkConvertReimbTableDto> searchConvertClaimTableDTO = new ArrayList<SearchBulkConvertReimbTableDto>();
		try {
			
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(370000);
			utx.setTransactionTimeout(360000);
			utx.begin();
			
			Map<String,Object> conversionStatusKeyMap = SHAUtils.getConvertStatusMap();
			
//			Map<String,Long> conversionReasonIdMap = SHAUtils.getConversionReasonId();
			Map<Long,Long> conversionReasonIdMap = SHAUtils.getConversionReasonId();
			
			SelectValue cpuCode = formDTO.getCpuCode();
			String strCpuCode = "";

			if (null != cpuCode) {
				strCpuCode = String.valueOf(cpuCode.getId());
			}
			
			//Long type = formDTO.getType() != null ? formDTO.getType().getId() != null ? formDTO
			//		.getType().getId() : null : null;
			
			String typeValue = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO
					.getType().getValue() : null
					: null;
			
			List<Map<String, Object>> taskParamObjList = null;
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.USER_ID,userName);		
			
			//IMSSUPPOR-27243
			mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);

			
			mapValues.put(SHAConstants.LOB_TYPE,SHAConstants.HEALTH_LOB_TYPE);
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE);
			
			
			if( typeValue != null && ! typeValue.equalsIgnoreCase(SHAConstants.CASHLESS_DENIAL) ){
	
//				PayloadBOType payloadBO = new PayloadBOType();
	
//				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();
//				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType reimbursementClaimRequestType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();
	
				
				/*ClaimType claimType1 = new ClaimType();
				claimType1.setCoverBenifitType("HEALTH");
				
				
				ProductInfoType productInfo = new ProductInfoType();
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.productinfo.ProductInfoType productInfoCasless = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.productinfo.ProductInfoType();
				
				
				
				productInfo.setLob("H");
				productInfoCasless.setLob("H");

				
				payloadBO.setProductInfo(productInfoCasless);
				reimbursementPayloadBO.setProductInfo(productInfo);
				reimbursementPayloadBO.setClaim(claimType1);*/
				
				
				
				if (null != cpuCode) {
					
					mapValues.put(SHAConstants.CPU_CODE, strCpuCode);
//					claimRequestType.setCpuCode(strCpuCode);

//					reimbursementClaimRequestType.setCpuCode(strCpuCode);
	
//					payloadBO.setClaimRequest(claimRequestType);
//					reimbursementPayloadBO
//							.setClaimRequest(reimbursementClaimRequestType);
				}
	
				//Pageable pageable = new Pageable();
//				pageable.setOrderBy("LIFO");
				
//				Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
//				
//				DBCalculationService dbCalculationService = new DBCalculationService();
//				taskParamObjList = dbCalculationService.getTaskProcedure(setMapValues);	
				
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				taskParamObjList = dbCalculationService.revisedGetTaskProcedure(setMapValues);				
				
				//Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
				
				
//				com.shaic.ims.bpm.claim.servicev2.conversion.search.ClaimConvTask processConvTask = BPMClientContext
//						.getConvertClaimSearchTask(userName, passWord);
//				com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = processConvTask
//						.getTasks(userName, pageable, payloadBO); // userName="zonaluser1"
//	
//				List<HumanTask> taskList = tasks.getHumanTasks();
				
//				int totalPages = tasks.getTotalRecords() / 10;			
//				
//				if(tasks.getTotalRecords() != 0 && tasks.getTotalRecords() % 10 != 0){
//					totalPages++;
//				}
//				
//				for(int page = 2; page <= totalPages ; page++){
//					pageable.setPageNumber(page);
//					com.shaic.ims.bpm.claim.corev2.PagedTaskList pagedTaskList = 
//							processConvTask
//							.getTasks(userName, pageable, payloadBO);
//					
//					tasks.getHumanTasks().addAll(pagedTaskList.getHumanTasks());
//				}			
//	
				
				List<Long> keys = new ArrayList<Long>();
				Map<Long,Long> reasonIdMap = new HashMap<Long,Long>();
				
//				AckProcessConvertClaimToReimbTask convertClaimTaskFromAck = BPMClientContext
//						.getConvertClaimTaskFromAck(userName, passWord);
//				PagedTaskList reimbursementTask = convertClaimTaskFromAck.getTasks(
//						userName, pageable, reimbursementPayloadBO);
//				
//				int reimTotalPages = reimbursementTask.getTotalRecords() / 10 ;
//				
//				if(reimbursementTask.getTotalRecords() != 0 && reimbursementTask.getTotalRecords() % 10 != 0){
//					reimTotalPages++;
//				}
//	
//				List<HumanTask> humanTaskList = new ArrayList<HumanTask>();
//				Map<Long, HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
//				Map<Long, Integer> taskNumberMap = new HashMap<Long, Integer>();
//				
//				for(int pg = 2 ; pg<= reimTotalPages; pg++){
//					pageable.setPageNumber(pg);
//					PagedTaskList reimbPagedTaskList = convertClaimTaskFromAck.getTasks(
//							userName, pageable, reimbursementPayloadBO);
//				
//					humanTaskList.addAll(reimbPagedTaskList.getHumanTasks());
//				}
//				
//				for (HumanTask item : humanTaskList) {
//
//					com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payload = item
//							.getPayload();
//					ClaimType claimBO = payload.getClaim();
//
//					if (null != claimBO) {
//						Long keyValue = claimBO.getKey();
//						Preauth preauthObj = getLatestPreauthByClaim(keyValue);
//						if (typeValue.equalsIgnoreCase(SHAConstants.ALL)
//								|| preauthObj != null
//								&& preauthObj.getStatus().getKey()
//										.equals(conversionStatusKeyMap.get(typeValue))) {
//							keys.add(keyValue);
//							humanTaskMap.put(keyValue, item);
//							taskNumberMap.put(keyValue, item.getNumber());
//						}
//					}
//				}
				
				if(null != taskParamObjList && !taskParamObjList.isEmpty()){
					
					Map<Long, Object> workFlowTaskMap = new WeakHashMap<Long, Object>();
					
					for (Map<String, Object> workTask : taskParamObjList) {
						
						Long keyValue = (Long)workTask.get(SHAConstants.DB_CLAIM_KEY);
						
						Claim claimObj = getClaimByClaimKey(keyValue);
						if(claimObj != null){							
							
						Long preauthKey = (Long)workTask.get(SHAConstants.CASHLESS_KEY);
						Preauth preauthObj = getPreauthByKey(preauthKey);
						
						List<Long> convertKeyList = new ArrayList<Long>();
						
						convertKeyList = (List<Long>)conversionStatusKeyMap.get(typeValue);
						
						for(int i = 0; i < convertKeyList.size(); i++){
							if(preauthObj != null){
								System.out.println("prauth Status -------------:    "+preauthObj.getStatus().getProcessValue()+"   ::******************************* "+preauthObj.getStatus().getKey());
								System.out.println("SearchStatus  -------------:    " +typeValue+"    ::########################## "+convertKeyList.get(i));
							}
							if(( preauthObj != null && preauthObj.getStatus().getKey().equals(convertKeyList.get(i))) || claimObj.getStatus().getKey().equals(convertKeyList.get(i)) ){	
								keys.add(keyValue);
								if(preauthObj != null && preauthObj.getStatus() != null && preauthObj.getStatus().getKey() != null){
									reasonIdMap.put(keyValue, preauthObj.getStatus().getKey());
								}
								else if(claimObj != null && claimObj.getStatus() != null && claimObj.getStatus().getKey() != null){
									reasonIdMap.put(keyValue, claimObj.getStatus().getKey());
								}								
								
								workFlowTaskMap.put(keyValue, workTask);
							}							
						}						
					}
					
					}
	
					if (null != keys &&  !keys.isEmpty()) {
						List<Claim> resultList = new ArrayList<Claim>();
						resultList = SHAUtils.getIntimationInformation(
								SHAConstants.SEARCH_CLAIM, entityManager, keys);
						List<Claim> pageItemList = resultList;
						searchConvertClaimTableDTO = SearchBulkConvertReimbMapper
								.getInstance().getSearchConvertClaimTableDTO(
										pageItemList);
	
						for (SearchBulkConvertReimbTableDto objSearchConvClaimTableDTO : searchConvertClaimTableDTO) {
	
							objSearchConvClaimTableDTO.setConversionTypeId(reasonIdMap.get(objSearchConvClaimTableDTO.getClaimKey()));
							objSearchConvClaimTableDTO.setSrlNo(searchConvertClaimTableDTO.indexOf(objSearchConvClaimTableDTO)+1);
							objSearchConvClaimTableDTO.setIntimatedDateValue(objSearchConvClaimTableDTO.getIntimatedDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(objSearchConvClaimTableDTO.getIntimatedDate()) : "");
//							Date curDate = new Date();
							if(objSearchConvClaimTableDTO.getIntimatedDate() != null){
								int totalDays = getIntimNumberDays(objSearchConvClaimTableDTO.getIntimatedDate());
								objSearchConvClaimTableDTO.setIntimatedDays(totalDays);
							}
							
//							objSearchConvClaimTableDTO.setIntimatedDays(objSearchConvClaimTableDTO.getIntimatedDate() != null ? curDate.compareTo(objSearchConvClaimTableDTO.getIntimatedDate()) : 0);
							
							// Human task assigned to table dto
							
//							objSearchConvClaimTableDTO.setHumanTask(humanTaskMap.get(objSearchConvClaimTableDTO.getKey()));
//							objSearchConvClaimTableDTO.setTaskNumber(taskNumberMap.get(objSearchConvClaimTableDTO.getKey()));
//							objSearchConvClaimTableDTO.setSearchType(SHAConstants.BPMN_SEARCH);
							objSearchConvClaimTableDTO.setDbOutArray(workFlowTaskMap
									.get(objSearchConvClaimTableDTO.getKey()));
	
							Claim claimObj = getClaimByClaimKey(objSearchConvClaimTableDTO
									.getClaimKey());
							if (claimObj != null) {
								ClaimDto claimDto = (new ClaimService())
										.getClaimDto(claimObj, entityManager);
								objSearchConvClaimTableDTO.setClaimDto(claimDto);
//								Date intimatedDate = claimObj.getIntimation().getCreatedDate();
//								Calendar intimDate = Calendar.getInstance();
//								if (intimatedDate != null) {
//									intimDate.setTime(intimatedDate);
//									Calendar today = Calendar.getInstance();
//									Date todayDate = Calendar.getInstance().getTime();
//									
//									int currentMonthdays = today.get(Calendar.DAY_OF_MONTH)-1;
//									int actualday = intimDate.get(Calendar.DAY_OF_MONTH);
//									int intimaMonthdays = intimDate.getActualMaximum(intimDate.DAY_OF_MONTH);  
//									int intimCompdays = intimaMonthdays - actualday + 1;
//									int totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
//									
//									if(intimDate.get(Calendar.MONTH) < today.get(Calendar.MONTH) || totalDays < 0)
//									{
//										totalDays = 0;
//										for(int m = intimDate.get(Calendar.MONTH) ; m < today.get(Calendar.MONTH)-1 ; m++){
//										   totalDays +=  intimDate.getActualMaximum(m+1);
//										}
//										
//										totalDays += (intimCompdays + currentMonthdays);
//									}
//									else{
//										totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
//									}
//									
//	//								totalDays = SHAUtils.getDateDiffDays(intimatedDate, new Date());
//									
//									objSearchConvClaimTableDTO.setIntimatedDays(totalDays);
//								}
								
								SelectValue conversionSelectValue = new SelectValue(conversionReasonIdMap.get(objSearchConvClaimTableDTO.getConversionTypeId()), typeValue);
								objSearchConvClaimTableDTO.setReasonforConversion(conversionSelectValue);
								objSearchConvClaimTableDTO.setUsername(userName);
							}
						}
					}		
				
				}
				
				
	
//				if (null != tasks) {
//					List<HumanTask> humanTasks = tasks.getHumanTasks();
//					List<Long> keys = new ArrayList<Long>();
//	
//					for (HumanTask item : humanTasks) {
//	
//						PayloadBOType payload = item.getPayloadCashless();
//						com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payload
//								.getClaim();
//						if (null != claimType) {
//							Long keyValue = claimType.getKey();
//							Claim claimObj = getClaimByClaimKey(keyValue);
//							if(claimObj != null){							
//							PreAuthReqType preauthReq = payload.getPreAuthReq();
//							Long preauthKey = preauthReq.getKey();
//							Preauth preauthObj = getPreauthByKey(preauthKey);
//							
//							List<Long> withdrawKeyList = new ArrayList<Long>();
//							
//								withdrawKeyList = (List<Long>)conversionStatusKeyMap.get(typeValue);
//							
//							for(int i = 0; i < withdrawKeyList.size(); i++){
//								if(preauthObj != null){
//									System.out.println("prauth Status -------------:    "+preauthObj.getStatus().getProcessValue()+"   ::******************************* "+preauthObj.getStatus().getKey());
//									System.out.println("SearchStatus  -------------:    " +typeValue+"    ::########################## "+withdrawKeyList.get(i));
//								}
//								if(typeValue.equalsIgnoreCase(SHAConstants.ALL) || ( preauthObj != null && preauthObj.getStatus().getKey().equals(withdrawKeyList.get(i))) || claimObj.getStatus().getKey().equals(withdrawKeyList.get(i)) ){	
//									keys.add(keyValue);
//									humanTaskMap.put(keyValue, item);
//									taskNumberMap.put(keyValue, item.getNumber());
//								}
//							}						
//								
//							}
//						}
//					}
//	
//					if (null != reimbursementTask) {
//						List<HumanTask> humanTask1 = reimbursementTask
//								.getHumanTasks();
//						
//						for(int pg = 2 ; pg<= reimTotalPages; pg++){
//							pageable.setPageNumber(pg);
//							PagedTaskList reimbPagedTaskList = convertClaimTaskFromAck.getTasks(
//									userName, pageable, reimbursementPayloadBO);
//						
//							humanTask1.addAll(reimbPagedTaskList.getHumanTasks());
//						}
//	
//						for (HumanTask item : humanTask1) {
//	
//							com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType payload = item
//									.getPayload();
//							ClaimType claimBO = payload.getClaim();
//							
//							if (null != claimBO) {
//								Long keyValue = claimBO.getKey();
//								Claim claimObj = getClaimByClaimKey(keyValue);
//								if (typeValue.equalsIgnoreCase(SHAConstants.ALL) || claimObj.getStatus().getKey().equals(conversionStatusKeyMap.get(typeValue))) {
//									keys.add(keyValue);
//									humanTaskMap.put(keyValue, item);
//									taskNumberMap.put(keyValue, item.getNumber());
//								}
//							}
//						}
//	
//					}
//	
//					if (null != keys && 0 != keys.size()) {
//						List<Claim> resultList = new ArrayList<Claim>();
//						resultList = SHAUtils.getIntimationInformation(
//								SHAConstants.SEARCH_CLAIM, entityManager, keys);
//						List<Claim> pageItemList = resultList;
//						searchConvertClaimTableDTO = SearchBulkConvertReimbMapper
//								.getInstance().getSearchConvertClaimTableDTO(
//										pageItemList);
//	
//						for (SearchBulkConvertReimbTableDto objSearchConvClaimTableDTO : searchConvertClaimTableDTO) {
//	
//							objSearchConvClaimTableDTO.setSrlNo(searchConvertClaimTableDTO.indexOf(objSearchConvClaimTableDTO)+1);
//							objSearchConvClaimTableDTO.setIntimatedDateValue(objSearchConvClaimTableDTO.getIntimatedDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(objSearchConvClaimTableDTO.getIntimatedDate()) : "");
//							Date curDate = new Date();
//							objSearchConvClaimTableDTO.setIntimatedDays(objSearchConvClaimTableDTO.getIntimatedDate() != null ? curDate.compareTo(objSearchConvClaimTableDTO.getIntimatedDate()) : 0);
//							
//							// Human task assigned to table dto
//							objSearchConvClaimTableDTO.setSearchType(SHAConstants.BPMN_SEARCH);
//							objSearchConvClaimTableDTO.setHumanTask(humanTaskMap
//									.get(objSearchConvClaimTableDTO.getKey()));
//							objSearchConvClaimTableDTO.setTaskNumber(taskNumberMap
//									.get(objSearchConvClaimTableDTO.getKey()));
//	
//							Claim claimObj = getClaimByClaimKey(objSearchConvClaimTableDTO
//									.getClaimKey());
//							if (claimObj != null) {
//								ClaimDto claimDto = (new ClaimService())
//										.getClaimDto(claimObj, entityManager);
//								objSearchConvClaimTableDTO.setClaimDto(claimDto);
//								Date intimatedDate = claimObj.getIntimation().getCreatedDate();
//								Calendar intimDate = Calendar.getInstance();
//								if (intimatedDate != null) {
//									intimDate.setTime(intimatedDate);
//									Calendar today = Calendar.getInstance();
//									Date todayDate = Calendar.getInstance().getTime();
//									
//									int currentMonthdays = today.get(Calendar.DAY_OF_MONTH)-1;
//									int actualday = intimDate.get(Calendar.DAY_OF_MONTH);
//									int intimaMonthdays = intimDate.getActualMaximum(intimDate.DAY_OF_MONTH);  
//									int intimCompdays = intimaMonthdays - actualday + 1;
//									int totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
//									
//									if(intimDate.get(Calendar.MONTH) < today.get(Calendar.MONTH) || totalDays < 0)
//									{
//										totalDays = 0;
//										for(int m = intimDate.get(Calendar.MONTH) ; m < today.get(Calendar.MONTH)-1 ; m++){
//										   totalDays +=  intimDate.getActualMaximum(m+1);
//										}
//										
//										totalDays += (intimCompdays + currentMonthdays);
//									}
//									else{
//										totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
//									}
//									
//	//								totalDays = SHAUtils.getDateDiffDays(intimatedDate, new Date());
//									
//									objSearchConvClaimTableDTO.setIntimatedDays(totalDays);
//								}
//								
//								SelectValue conversionSelectValue = new SelectValue(conversionReasonIdMap.get(typeValue), typeValue);
//								objSearchConvClaimTableDTO.setReasonforConversion(conversionSelectValue);
//								objSearchConvClaimTableDTO.setUsername(userName);
//							}
//						}
//					}		
//				}

				if(typeValue != null && typeValue.equalsIgnoreCase(SHAConstants.ALL)){
					List<SearchBulkConvertReimbTableDto>  dbSearchResultList = searchDBConvertClaim(formDTO,userName);
					if(dbSearchResultList != null && !dbSearchResultList.isEmpty()){
						int sno = searchConvertClaimTableDTO.size();
						for (SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto : dbSearchResultList) {
							searchBulkConvertReimbTableDto.setSrlNo(++sno);
						}
						searchConvertClaimTableDTO.addAll(dbSearchResultList);	
					}
				}	
			}
			else{
				List<SearchBulkConvertReimbTableDto>  dbSearchResultList = searchDBConvertClaim(formDTO,userName);
				if(dbSearchResultList != null && !dbSearchResultList.isEmpty()){
					searchConvertClaimTableDTO.addAll(dbSearchResultList);	
				}				
			}
			
			utx.commit();
			return searchConvertClaimTableDTO;

		} catch (Exception e) {
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<Claim> getDTOClass() {
		return Claim.class;
	}

	
	/**
	 * Top Level Search Prev Bulk Conversion to Reimb. Claim Details
	 * @return
	 */

	public List<SearchBatchConvertedTableDto> getPreviousBatchList() {

		List<SearchBatchConvertedTableDto> resultList = new ArrayList<SearchBatchConvertedTableDto>();

		try {

			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(370000);
			utx.setTransactionTimeout(360000);
			utx.begin();
			Query prevConvertBatchQuery1 = entityManager
					.createNamedQuery("ClaimConversion.findPrevBatchBysubBatchId");

			Query prevConvertBatchQuery2 = entityManager
					.createNamedQuery("ClaimConversion.findPrevBatchByBatchId");

			Calendar.getInstance().set(
					Calendar.getInstance().getTime().getYear(),
					Calendar.getInstance().getTime().getMonth(), 1);
			Date startDate = Calendar.getInstance().getTime();
			startDate.setDate(1);
			startDate.setMonth(Calendar.getInstance().getTime().getMonth());
			startDate.setYear(Calendar.getInstance().getTime().getYear());
			prevConvertBatchQuery1.setParameter("createdDate", startDate);

			Calendar calInstance = Calendar.getInstance();
			calInstance.set(calInstance.getTime().getYear(), calInstance
					.getTime().getMonth(), calInstance.getTime().getDate());
			Date endDate = calInstance.getInstance().getTime();
			prevConvertBatchQuery1.setParameter("endDate", endDate);

			List<ClaimConversion> convertClaimDetailsListBySubBatchId = prevConvertBatchQuery1
					.getResultList();
			List<SearchBatchConvertedTableDto> listBySubBatchId = new ArrayList<SearchBatchConvertedTableDto>();
			Long totalRecords = null;

			if (convertClaimDetailsListBySubBatchId != null
					&& !convertClaimDetailsListBySubBatchId.isEmpty()
					&& convertClaimDetailsListBySubBatchId.get(0) != null) {
				Iterator convertClaimIter1 = convertClaimDetailsListBySubBatchId
						.iterator();

				for (; convertClaimIter1.hasNext();) {

					SearchBatchConvertedTableDto convertClaimDetailsDto = new SearchBatchConvertedTableDto();
					Object[] convertClaimDetailsResult = (Object[]) convertClaimIter1
							.next();

					if (convertClaimDetailsResult[0] != null) {
						convertClaimDetailsDto
								.setSubCrNo((String) convertClaimDetailsResult[0]);
						totalRecords = convertClaimDetailsResult[1] != null ? (Long) convertClaimDetailsResult[1]
								: null;
						convertClaimDetailsDto
								.setNoofRecords(totalRecords != null ? totalRecords
										.intValue() : 0);
						listBySubBatchId.add(convertClaimDetailsDto);
					}
				}
			}
			prevConvertBatchQuery2.setParameter("createdDate", startDate);
			prevConvertBatchQuery2.setParameter("endDate", endDate);

			List<ClaimConversion> convertClaimDetailsListByBatchId = new ArrayList<ClaimConversion>();

			if (listBySubBatchId == null || listBySubBatchId.isEmpty()) {
				convertClaimDetailsListByBatchId = prevConvertBatchQuery2
						.getResultList();

				if (convertClaimDetailsListByBatchId != null
						&& !convertClaimDetailsListByBatchId.isEmpty()
						&& convertClaimDetailsListByBatchId.get(0) != null) {
					Iterator convertClaimIter2 = convertClaimDetailsListByBatchId
							.iterator();
					for (; convertClaimIter2.hasNext();) {

						SearchBatchConvertedTableDto convertClaimDetailsDto = new SearchBatchConvertedTableDto();
						Object[] convertClaimDetailsResult = (Object[]) convertClaimIter2
								.next();

						if (convertClaimDetailsResult[0] != null) {
							convertClaimDetailsDto
									.setCrNo((String) convertClaimDetailsResult[0]);
							totalRecords = convertClaimDetailsResult[1] != null ? (Long) convertClaimDetailsResult[1]
									: null;
							convertClaimDetailsDto
									.setNoofRecords(totalRecords != null ? totalRecords
											.intValue() : 0);
							listBySubBatchId.add(convertClaimDetailsDto);
						}
					}
				}
			}

			int index = 1;
			if (listBySubBatchId != null && !listBySubBatchId.isEmpty()) {
				for (SearchBatchConvertedTableDto bulkconvertClaimDto : listBySubBatchId) {

					SearchBatchConvertedTableDto convertClaimDetailsDto = new SearchBatchConvertedTableDto();
					List<SearchBulkConvertReimbTableDto> exportExcelList = new ArrayList<SearchBulkConvertReimbTableDto>();

					convertClaimDetailsDto.setSubCrNo(bulkconvertClaimDto.getSubCrNo());

					convertClaimDetailsDto.setCrNo(bulkconvertClaimDto.getCrNo());

					List<ClaimConversion> convertClaimDetailsObjList = new ArrayList<ClaimConversion>();
					if (convertClaimDetailsDto.getSubCrNo() != null) {

						Query convertClaimBatchBySubBatchIdQuery = entityManager
								.createNamedQuery("ClaimConversion.findBySubBatchId");

						convertClaimBatchBySubBatchIdQuery.setParameter(
								"subBatchId", convertClaimDetailsDto.getSubCrNo());
						convertClaimDetailsObjList = convertClaimBatchBySubBatchIdQuery
								.getResultList();
					} else if (convertClaimDetailsDto.getCrNo() != null) {

						Query convertClaimBatchByIdQuery = entityManager
								.createNamedQuery("ClaimConversion.findByBatchId");

						convertClaimBatchByIdQuery.setParameter("batchId",
								convertClaimDetailsDto.getCrNo().toLowerCase());
						convertClaimDetailsObjList = convertClaimBatchByIdQuery
								.getResultList();
					}

					if (convertClaimDetailsObjList != null
							&& !convertClaimDetailsObjList.isEmpty())
						convertClaimDetailsDto.setCrNo(convertClaimDetailsObjList
								.get(0).getBatchNo());
					convertClaimDetailsDto.setSubCrNo(convertClaimDetailsObjList.get(0).getSubBatchNo());
					convertClaimDetailsDto.setCategory(convertClaimDetailsObjList
							.get(0).getCategory());
					convertClaimDetailsDto
							.setCpuCode(convertClaimDetailsObjList.get(0)
									.getCpuCode() != null ? convertClaimDetailsObjList
									.get(0).getCpuCode().toString()
									: "");
					convertClaimDetailsDto
							.setLetterDate(convertClaimDetailsObjList.get(0)
									.getLetterDate());
					convertClaimDetailsDto.setPrintTaken(convertClaimDetailsObjList.get(0).getPrintFlag());
					convertClaimDetailsDto.setPrintCount(convertClaimDetailsObjList.get(0).getPrintCount());
					convertClaimDetailsDto.setLetterDateValue(convertClaimDetailsDto.getLetterDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(convertClaimDetailsDto.getLetterDate()) : ""); 
					convertClaimDetailsDto
							.setNoofRecords(convertClaimDetailsObjList.size());

					if (convertClaimDetailsDto.getLetterDate() != null) {
						String status = convertClaimDetailsDto.getPrintTaken() != null ? (convertClaimDetailsDto
								.getPrintTaken().equalsIgnoreCase("n") ? "In Progress"
								: "Completed")
								: "";
						convertClaimDetailsDto.setStatus(status);
					}

					convertClaimDetailsDto.setSno(index++);

					for (ClaimConversion convertClaimObj : convertClaimDetailsObjList) {
						SearchBulkConvertReimbTableDto exportExcelDto = populateExportExcelDto(convertClaimObj);
						exportExcelDto.setSrlNo(convertClaimDetailsObjList
								.indexOf(convertClaimObj) + 1);
						exportExcelList.add(exportExcelDto);
					}
					convertClaimDetailsDto.setExportList(exportExcelList);

					resultList.add(convertClaimDetailsDto);
				}
			}
			utx.commit();
		} catch (Exception e) {
			
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return resultList;

	}

	private SearchBulkConvertReimbTableDto populateExportExcelDto(
			ClaimConversion convertedClaimObj) {
		SearchBulkConvertReimbTableDto resultDto = new SearchBulkConvertReimbTableDto();
		resultDto.setIntimationNumber(convertedClaimObj.getIntimationNo());
		resultDto.setClaimKey(convertedClaimObj.getClaimKey());
		resultDto.setConversionDetailsKey(convertedClaimObj.getKey());
		resultDto.setPrintFlag(convertedClaimObj.getPrintFlag());
		resultDto.setPrintCount(convertedClaimObj.getPrintCount());

		String claimType = "";
		if (resultDto.getClaimKey() != null) {
			Claim claim = entityManager.find(Claim.class,
					resultDto.getClaimKey());

			claimType = claim.getClaimType().getValue();
			//ClaimDto claimDto = (new ClaimMapper()).getInstance().getClaimDto(
			//		claim);
			ClaimDto claimDto = ClaimMapper.getInstance().getClaimDto(claim);
			NewIntimationDto intimationDto = NewIntimationMapper.getInstance().getNewIntimationDto(claim.getIntimation()); 
			claimDto.setNewIntimationDto(intimationDto);
			resultDto.setClaimDto(claimDto);		
			
			Date intimatedDate = claim.getIntimation().getCreatedDate();
			
			//Calendar intimDate = Calendar.getInstance();
			if (intimatedDate != null) {
				
				int totalDays = getIntimNumberDays(intimatedDate);
				resultDto.setIntimatedDays(totalDays);
				
//				intimDate.setTime(intimatedDate);
//				Calendar today = Calendar.getInstance();
//				Date todayDate = Calendar.getInstance().getTime();
//				
//				int currentMonthdays = today.get(Calendar.DAY_OF_MONTH)-1;
////				int actualday = intimDate.get(Calendar.DAY_OF_MONTH);
////				int intimaMonthdays = intimDate.getActualMaximum(intimDate.DAY_OF_MONTH);  
////				int intimCompdays = intimaMonthdays - actualday + 1;
////				int totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
//				int totalDays = intimDate.getActualMaximum(Calendar.MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
//				
//				if(intimDate.get(Calendar.MONTH) < today.get(Calendar.MONTH))
//				{
//					totalDays = 0;
//					for(int m = intimDate.get(Calendar.MONTH) ; m < today.get(Calendar.MONTH)-1 ; m++){
//					   totalDays +=  intimDate.getActualMaximum(m+1);
//					}
//					totalDays +=  currentMonthdays;
////					totalDays += (intimCompdays + currentMonthdays);
//				}
////				else{
////					totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
////				}
//				
//				
////				totalDays = SHAUtils.getDateDiffDays(intimatedDate, new Date());
//				resultDto.setIntimatedDays(totalDays);
			}
		}
		resultDto.setClaimType(claimType);
		resultDto.setPreauthKey(convertedClaimObj.getPreauthKey());
		resultDto.setCategory(convertedClaimObj.getCategory());
		resultDto
				.setCpuCode(convertedClaimObj.getCpuCode() != null ? convertedClaimObj
						.getCpuCode().toString() : "");
		resultDto.setCrno(convertedClaimObj.getBatchNo());
		resultDto.setSubcrno(convertedClaimObj.getSubBatchNo());
		resultDto.setDocToken(String.valueOf(convertedClaimObj.getDocToken()));
		resultDto
				.setLetterDateValue(convertedClaimObj.getLetterDate() != null ? new SimpleDateFormat(
						"dd-MM-yyyy").format(convertedClaimObj.getLetterDate())
						: "");
		if (resultDto.getClaimDto() != null
				&& resultDto.getClaimDto().getNewIntimationDto() != null
				&& resultDto.getClaimDto().getNewIntimationDto()
						.getCreatedDate() != null) {
			resultDto.setIntimatedDate(resultDto.getClaimDto()
					.getNewIntimationDto().getCreatedDate());
			resultDto.setIntimatedDateValue(new SimpleDateFormat("dd-MM-yyyy")
					.format(resultDto.getClaimDto().getNewIntimationDto()
							.getCreatedDate()));
		}
		return resultDto;

	}
	
	private int getIntimNumberDays(Date intimatedDate){
		
		int totalDays = 0;
		Calendar intimDate = Calendar.getInstance();
		if (intimatedDate != null) {
			intimDate.setTime(intimatedDate);
			Calendar today = Calendar.getInstance();
			//Date todayDate = Calendar.getInstance().getTime();
			
			int currentMonthdays = today.get(Calendar.DAY_OF_MONTH)-1;
//			int actualday = intimDate.get(Calendar.DAY_OF_MONTH);
//			int intimaMonthdays = intimDate.getActualMaximum(intimDate.DAY_OF_MONTH);  
//			int intimCompdays = intimaMonthdays - actualday + 1;
//			int totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
			
			if(intimDate.get(Calendar.MONTH) != today.get(Calendar.MONTH)){
				totalDays = intimDate.getActualMaximum(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH)+1;
			}
			else if (intimDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) && intimDate.get(Calendar.YEAR) == today.get(Calendar.YEAR)){
				totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
			}
			
			if(intimDate.get(Calendar.MONTH) < today.get(Calendar.MONTH))
			{
				for(int m = intimDate.get(Calendar.MONTH) ; m < today.get(Calendar.MONTH)-1 ; m++){
				   totalDays +=  intimDate.getActualMaximum(Calendar.DAY_OF_MONTH);
				}
				totalDays +=  currentMonthdays;
//				totalDays += (intimCompdays + currentMonthdays);
			}
			else{
				if(intimDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))
				{	
					for(int m = intimDate.get(Calendar.MONTH)+1; m < 12 ; m++){
						   totalDays +=  intimDate.getActualMaximum(Calendar.DAY_OF_MONTH);
						   intimDate.add(Calendar.MONTH, 1);
					}
					for(int m = 0 ; m < today.get(Calendar.MONTH)-1 ; m++){
						   totalDays +=  intimDate.getActualMaximum(Calendar.DAY_OF_MONTH);
					}
					
					totalDays +=  currentMonthdays;
				}	
			}
//			else{
//				totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
//			}
		}
//			totalDays = SHAUtils.getDateDiffDays(intimatedDate, new Date());
			
			return totalDays;
	}

	private Claim getClaimByClaimKey(Long claimKey) {
		Claim claimObj = null;
		if (claimKey != null) {
			Query query = entityManager
					.createNamedQuery("Claim.findByClaimKey");
			query.setParameter("claimKey", claimKey);
			List<Claim> claimList = (List<Claim>) query.getResultList();

			if (claimList != null && !claimList.isEmpty()) {
				for (Claim claimDO : claimList) {
					entityManager.refresh(claimDO);
					claimObj = claimDO;
				}
			}
		}
		return claimObj;

	}
	
	private Preauth getPreauthByKey(Long cashlessKey){
		Preauth cashlessObj = null;
		if (cashlessKey != null) {
			Query query = entityManager
					.createNamedQuery("Preauth.findByKey");
			query.setParameter("preauthKey", cashlessKey);
			List<Preauth> preauthList = (List<Preauth>) query.getResultList();

			if (preauthList != null && !preauthList.isEmpty()) {
				for (Preauth cashlessDO : preauthList) {
					entityManager.refresh(cashlessDO);
					cashlessObj = cashlessDO;
				}
			}
		}
		return cashlessObj;
	}
	
	/**
	 * Top Level Service for Search Bulk Conversion to Reimb. ClaimDetails
	 * @param searchFormDTO
	 * @return
	 */

	public List<SearchBatchConvertedTableDto> searchByCRNoIntimationNo(
			Map<String, String> searchFieldMap) {

		String batchId = searchFieldMap.containsKey(SHAConstants.SEARCH_BY_BATCH) && searchFieldMap.get(SHAConstants.SEARCH_BY_BATCH) != null ? (String)searchFieldMap.get(SHAConstants.SEARCH_BY_BATCH) : null;
		String intimationNo = searchFieldMap.containsKey(SHAConstants.SEARCH_BY_INTIMATION) && searchFieldMap.get(SHAConstants.SEARCH_BY_INTIMATION) != null ? (String)searchFieldMap.get(SHAConstants.SEARCH_BY_INTIMATION) : null;
		List<SearchBatchConvertedTableDto> resultList = new ArrayList<SearchBatchConvertedTableDto>();

		List<ClaimConversion> resultObjListByIntimation = new ArrayList<ClaimConversion>();
		List<ClaimConversion> resultObjListByBatchId = new ArrayList<ClaimConversion>();
		List<ClaimConversion> resultObjList = new ArrayList<ClaimConversion>();
		//List<SearchBatchConvertedTableDto> resultDtoListByBatchId = new ArrayList<SearchBatchConvertedTableDto>();
		//List<SearchBatchConvertedTableDto> finalresultDtoList = new ArrayList<SearchBatchConvertedTableDto>();

		try {
			
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(370000);
			utx.setTransactionTimeout(360000);
			utx.begin();
			if (batchId != null) {
				Query convertClaimBatchByIdQuery = entityManager
						.createNamedQuery("ClaimConversion.findSubBatchIdByBatchId");
				convertClaimBatchByIdQuery.setParameter("batchId",
						batchId.toLowerCase());

				List<ClaimConversion> resultObjListBysubId = convertClaimBatchByIdQuery
						.getResultList();

				if (resultObjListBysubId != null
						&& !resultObjListBysubId.isEmpty()
						&& resultObjListBysubId.get(0) != null) {
					Iterator resultObjListIterator = resultObjListBysubId
							.iterator();

					for (; resultObjListIterator.hasNext();) {
						ClaimConversion convertClaimDetails = new ClaimConversion();
						Object[] convertClaimDetailsResult = (Object[]) resultObjListIterator
								.next();
						if (convertClaimDetailsResult[0] != null) {
							String subBatchNo = (String) convertClaimDetailsResult[0];

							if (subBatchNo != null
									&& !("").equalsIgnoreCase(subBatchNo)) {
								Query convertClaimQueryBySubBatchId = entityManager
										.createNamedQuery("ClaimConversion.findBySubBatchId");
								convertClaimQueryBySubBatchId.setParameter(
										"subBatchId", subBatchNo);
								List<ClaimConversion> resultBySubIdList = convertClaimQueryBySubBatchId
										.getResultList();
								if (resultBySubIdList != null
										&& !resultBySubIdList.isEmpty()) {
									convertClaimDetails
											.setBatchNo(resultBySubIdList
													.get(0).getBatchNo());
									convertClaimDetails.setSubBatchNo(subBatchNo);
									convertClaimDetails
											.setTotalNoofRecords(resultBySubIdList
													.size());
									resultObjListByBatchId.add(convertClaimDetails);
								}
							}
						}
					}
				}
				if (resultObjListByBatchId == null
						|| resultObjListByBatchId.isEmpty()) {

					Query convertClaimByBatchIdQuery = entityManager
							.createNamedQuery("ClaimConversion.findByBatchIdGroup");
					convertClaimByBatchIdQuery.setParameter("batchId",
							batchId.toLowerCase());

					List<ClaimConversion> resultQueryObjListByBatchId = convertClaimByBatchIdQuery
							.getResultList();

					if (resultQueryObjListByBatchId != null
							&& !resultQueryObjListByBatchId.isEmpty()
							&& resultQueryObjListByBatchId.get(0) != null) {
						Iterator resultObjListIterator = resultQueryObjListByBatchId
								.iterator();

						for (; resultObjListIterator.hasNext();) {
							ClaimConversion convertClaimDetails = new ClaimConversion();
							Object[] convertClaimDetailsResult = (Object[]) resultObjListIterator
									.next();
							if (convertClaimDetailsResult[0] != null) {
								String batchNo = (String) convertClaimDetailsResult[0];
								Long noofrecords = (Long) convertClaimDetailsResult[1];
								convertClaimDetails.setBatchNo(batchNo);
								convertClaimDetails
										.setTotalNoofRecords(noofrecords != null ? noofrecords
												.intValue() : null);
							}
							resultObjListByBatchId.add(convertClaimDetails);
						}
					}

				}
			} else if (intimationNo != null) {

				Query convertClaimSubBatchQueryByIntimationGroup = entityManager
						.createNamedQuery("ClaimConversion.findBySubBatchIntimationNoGroup");
				convertClaimSubBatchQueryByIntimationGroup.setParameter(
						"intimationNo", intimationNo.toLowerCase());
				List<ClaimConversion> intimationQueryResultListBySubBatchId = convertClaimSubBatchQueryByIntimationGroup
						.getResultList();
				if (intimationQueryResultListBySubBatchId != null
						&& !intimationQueryResultListBySubBatchId.isEmpty()
						&& intimationQueryResultListBySubBatchId.get(0) != null) {

					Iterator listIter1 = intimationQueryResultListBySubBatchId
							.iterator();
					for (; listIter1.hasNext();) {
						ClaimConversion convertClaimDetails = new ClaimConversion();
						Object[] convertClaimDetailsResult = (Object[]) listIter1
								.next();
						if (convertClaimDetailsResult[0] != null) {
							String batchNo = (String) convertClaimDetailsResult[0];
							Long noofrecords = (Long) convertClaimDetailsResult[1];
							convertClaimDetails.setBatchNo(batchNo);
							convertClaimDetails
									.setTotalNoofRecords(noofrecords != null ? noofrecords
											.intValue() : null);
							resultObjListByIntimation.add(convertClaimDetails);
						}

					}

				}

				if (resultObjListByIntimation == null
						|| resultObjListByIntimation.isEmpty()) {

					Query convertClaimQueryByIntimationGroup = entityManager
							.createNamedQuery("ClaimConversion.findByBatchIntimationNoGroup");
					convertClaimQueryByIntimationGroup.setParameter("intimationNo",
							intimationNo.toLowerCase());
					List<ClaimConversion> intimationQueryResultListByBatchId = convertClaimQueryByIntimationGroup
							.getResultList();

					if (intimationQueryResultListByBatchId != null
							&& !intimationQueryResultListByBatchId.isEmpty()
							&& intimationQueryResultListByBatchId.get(0) != null) {

						Iterator listIter1 = intimationQueryResultListByBatchId
								.iterator();
						for (; listIter1.hasNext();) {
							ClaimConversion convertClaimDetails = new ClaimConversion();
							Object[] convertClaimDetailsResult = (Object[]) listIter1
									.next();
							if (convertClaimDetailsResult[0] != null) {
								String batchNo = (String) convertClaimDetailsResult[0];
								convertClaimDetails.setBatchNo(batchNo);
							}
							resultObjListByIntimation.add(convertClaimDetails);
						}

					}
				}
			}

			if (resultObjListByIntimation != null
					&& !resultObjListByIntimation.isEmpty()) {
				resultObjList.addAll(resultObjListByIntimation);
			} else if (resultObjListByBatchId != null
					&& !resultObjListByBatchId.isEmpty()) {
				resultObjList.addAll(resultObjListByBatchId);
			}

			if (resultObjList != null && !resultObjList.isEmpty()) {
				for (ClaimConversion resultObj : resultObjList) {

					SearchBatchConvertedTableDto convertClaimDetailsDto = new SearchBatchConvertedTableDto();

					convertClaimDetailsDto
							.setSno(resultObjList.indexOf(resultObj) + 1);
					convertClaimDetailsDto.setNoofRecords(resultObjList.size());
					String subBatchId = resultObj.getSubBatchNo();
					String batchNo = resultObj.getBatchNo();
					List<SearchBulkConvertReimbTableDto> exportList = new ArrayList<SearchBulkConvertReimbTableDto>();
					List<ClaimConversion> resultBySubIdList = new ArrayList<ClaimConversion>();
					if (subBatchId != null && !subBatchId.isEmpty()) {

						Query convertClaimQueryBySubBatchId = entityManager
								.createNamedQuery("ClaimConversion.findBySubBatchId");
						convertClaimQueryBySubBatchId.setParameter("subBatchId",
								subBatchId);
						resultBySubIdList = convertClaimQueryBySubBatchId
								.getResultList();

					} else if (batchNo != null && !batchNo.isEmpty()) {

						Query convertClaimQueryByBatchId = entityManager
								.createNamedQuery("ClaimConversion.findByBatchId");
						convertClaimQueryByBatchId.setParameter("batchId",
								batchNo.toLowerCase());
						resultBySubIdList = convertClaimQueryByBatchId
								.getResultList();
					}
					if (resultBySubIdList != null
							&& !resultBySubIdList.isEmpty()) {
						resultBySubIdList.get(0).setTotalNoofRecords(resultBySubIdList.size());
						
						populateConvertClaimDetailsToDto(convertClaimDetailsDto,
								resultBySubIdList.get(0));
						//Long claimKey = resultBySubIdList.get(0).getClaimKey();
						convertClaimDetailsDto.setLetterDate(resultBySubIdList.get(0).getLetterDate());
						convertClaimDetailsDto.setLetterDateValue(convertClaimDetailsDto.getLetterDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(convertClaimDetailsDto.getLetterDate()) : "");
						for (ClaimConversion convertClaimObj : resultBySubIdList) {

							SearchBulkConvertReimbTableDto excelObj = populateExportExcelDto(convertClaimObj);
							excelObj.setSrlNo(resultBySubIdList
									.indexOf(convertClaimObj) + 1);
							exportList.add(excelObj);

						}
					}

					convertClaimDetailsDto.setExportList(exportList);
					convertClaimDetailsDto.setNoofRecords(resultBySubIdList.size());
					resultList.add(convertClaimDetailsDto);
				}

			}
			utx.commit();
		} catch (Exception e) {
			
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return resultList;
	}

	private void populateConvertClaimDetailsToDto(
			SearchBatchConvertedTableDto convertClaimDetailsDto,
			ClaimConversion resultObj) {

		convertClaimDetailsDto.setCrNo(resultObj.getBatchNo());

		convertClaimDetailsDto.setSubCrNo(resultObj.getSubBatchNo());
		convertClaimDetailsDto.setCategory(resultObj.getCategory());
		convertClaimDetailsDto
				.setCpuCode(resultObj.getCpuCode() != null ? resultObj
						.getCpuCode().toString() : "");
		convertClaimDetailsDto.setPrintTaken(resultObj.getPrintFlag());
		convertClaimDetailsDto.setPrintCount(resultObj.getPrintCount());
		convertClaimDetailsDto.setNoofRecords(resultObj.getTotalNoofRecords());
		convertClaimDetailsDto.setLetterDate(resultObj.getLetterDate());
		convertClaimDetailsDto.setStatus(("y").equalsIgnoreCase(resultObj
				.getPrintFlag()) ? "Completed" : "In Progress");

	}

	/***
	 *   Toplevel Service for List of Claims to be submitted for conversion to reimb, 
	 * Batch divided and subBactch created
	 * CPU Updated in Intimation,  BPM Que submitted, 
	 * Letter Generated and Uploaded to DMS for corresponding Intimations
	 * Conversion Details inserted into DB Table,  Claim Table updated for Covering Letter Flag
	 * FVR Initiated in BPM for all the claim Reimb cases
	 *  	 
	 * @param convertDtoList
	 * @param dbCalService
	 * @return
	 */
	
	public SearchBatchConvertedTableDto submitConvertClaimTask(
			List<SearchBulkConvertReimbTableDto> convertDtoList,DBCalculationService dbCalService) {
		  SearchBatchConvertedTableDto bulkDto = new SearchBatchConvertedTableDto(); 
		if (convertDtoList != null && !convertDtoList.isEmpty()) {
			
			
			try{
				VaadinSession.getCurrent().getSession().setMaxInactiveInterval(370000);
				utx.setTransactionTimeout(360000);
				utx.begin();
			int selectedCount = 0;
			List<SearchBulkConvertReimbTableDto> selectedDtoList = new ArrayList<SearchBulkConvertReimbTableDto>();
			for (SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto : convertDtoList) {
				if(searchBulkConvertReimbTableDto.getSelected()){
					selectedDtoList.add(searchBulkConvertReimbTableDto);
					selectedCount++;
				}
					
			}
			
			if(selectedCount > 0){
			
			String batchId ="";			
			Long batchIdValue = dbCalService.generateSequence(SHAConstants.CONVERT_CLAIM_BATCH_SEQUENCE_NAME); 	
				
				batchId = batchIdValue != null ? String.valueOf(batchIdValue) : "";
				
				batchId = batchId != null && !("").equals(batchId) ? SHAConstants.BULK_CONVERSION + batchId : "";
				
				bulkDto.setCrNo(batchId);
				
				int totalListSize = selectedDtoList.size();
				int index =  1;
					
				if(totalListSize > 100){
				
					for(int j = 0; j < totalListSize ; j++){
						
							String subBatchId = batchId + " - " + String.valueOf(index);
						
							if(index % 100 == 0){
								index++;
							}
							selectedDtoList.get(j).setCrno(batchId);
							selectedDtoList.get(j).setSubcrno(subBatchId);
						}						
				}	
				else{

					for(int j = 0; j < totalListSize ; j++){
						selectedDtoList.get(j).setCrno(batchId);
						
						}
				}							
				
			for (SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto : convertDtoList) {

				if (searchBulkConvertReimbTableDto.getSelected()) {

					Long claimKey = searchBulkConvertReimbTableDto
							.getClaimKey();

					Claim claimObj = entityManager.find(Claim.class, claimKey);
					entityManager.refresh(claimObj);

					if (claimObj != null) {
						if(searchBulkConvertReimbTableDto.getReasonforConversion() != null && searchBulkConvertReimbTableDto
											.getReasonforConversion().getId() != null){
							MastersValue conversionReason = getConversionReasonObject(searchBulkConvertReimbTableDto.getReasonforConversion().getId());
							
							if(conversionReason != null){
								claimObj.setConversionReason(conversionReason);	
							}
								
						}
						
						
						if (searchBulkConvertReimbTableDto
								.getReasonforConversion() != null) {
							
							Map<Object,String> convertCategoryMap =  SHAUtils.getConvertCategoryMap();
							
							searchBulkConvertReimbTableDto.setCategory(convertCategoryMap.get(searchBulkConvertReimbTableDto
									.getReasonforConversion().getId()));
						}
						
						MastersValue claimTypeId = new MastersValue();
						claimTypeId
								.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
						claimObj.setClaimType(claimTypeId);
						claimObj.setConversionFlag(1l);
						claimObj.setConversionDate(new Timestamp(System
								.currentTimeMillis()));
						entityManager.merge(claimObj);
						entityManager.flush();
						//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
//						if(! ReferenceTable.getGMCProductList().containsKey(claimObj.getIntimation().getPolicy().getProduct().getKey())){
							updateCpuCodeForIntimation(claimObj.getIntimation());
//						}

						ClaimDto claimDto = (new ClaimService()).getClaimDto(claimObj, entityManager);
						
//						if(searchBulkConvertReimbTableDto.getSearchType() != null && searchBulkConvertReimbTableDto.getSearchType().equalsIgnoreCase(SHAConstants.BPMN_SEARCH)){
							String flowOutcome ="";
//							if(ReferenceTable.ACKNOWLEDGE_STATUS_KEY.equals(claimObj.getStatus().getKey())){
//								flowOutcome = SHAConstants.OUTCOME_CONVERT_REIMB_CREATE_ROD; 
							
								Map<String,Object> wrkFlowMap = (Map<String,Object>)searchBulkConvertReimbTableDto.getDbOutArray();
								
								if(wrkFlowMap != null && !wrkFlowMap.isEmpty()){

									String ackKey = (String)wrkFlowMap.get(SHAConstants.REFERENCE_USER_ID);
									
									if(ackKey != null && NumberUtils.isNumber(ackKey)){
										flowOutcome = SHAConstants.OUTCOME_CONVERT_REIMB_CREATE_ROD;
										DocAcknowledgement docAcknowledgementBasedOnKey = (new ClaimService()).getDocAcknowledgment((Long.valueOf(ackKey)),entityManager);
										if(docAcknowledgementBasedOnKey != null){
										wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_CONVERT_REIMB_CREATE_ROD);
	
										ReceiptOfDocumentsDTO rodDTO = new ReceiptOfDocumentsDTO();
										rodDTO.setClaimDTO(claimDto);

										rodDTO.setDbOutArray(wrkFlowMap);
										
										(new AcknowledgementDocumentsReceivedService()).submitTaskFromConvertToROD(rodDTO, docAcknowledgementBasedOnKey, false, false, entityManager);
										
										}
									}
									else{
										flowOutcome = SHAConstants.OUTCOME_GEN_CNV_LETTER_END;
										setDBOutComeForConvertClaim(
												searchBulkConvertReimbTableDto, claimObj,flowOutcome);
										generateAndUploadCoveringLetter(searchBulkConvertReimbTableDto);
										wrkFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_REIMINDER_CNVT_PROCESS);
										initiateReminderLetterProcess(wrkFlowMap);
									}
							   }

								else{
									generateAndUploadCoveringLetter(searchBulkConvertReimbTableDto);
									
									Hospitals hospitalById = getHospitalById(claimObj.getIntimation().getHospital());
									
									Map<String,Object> workFlowMap = SHAUtils.getRevisedPayloadMap(claimObj,hospitalById);
									
									workFlowMap.put(SHAConstants.OUTCOME, SHAConstants.OUTCOME_INITIATE_REIMINDER_CNVT_PROCESS);
									initiateReminderLetterProcess(workFlowMap);
								}
								
//						}	
//							setDBOutComeForConvertClaim(
//									searchBulkConvertReimbTableDto, claimObj,flowOutcome);
						
//							generateAndUploadCoveringLetter(searchBulkConvertReimbTableDto);

//						autoRegisterFVR(claimObj.getIntimation(),
//								BPMClientContext.USERID);
								
								ClaimService claimService = new ClaimService();
								
								SearchClaimRegistrationTableDto searchDto = new SearchClaimRegistrationTableDto();
								searchDto.setNewIntimationDto(claimDto.getNewIntimationDto());
								searchDto.setUserId(searchBulkConvertReimbTableDto.getUsername());
								claimService.initiateReimbFVRTask(searchDto,entityManager);
								
//					}
				}

			}
			bulkDto.setExportList(convertDtoList);
			
			}
		}
			utx.commit();
		}catch(Exception e){
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
		
		else{
			bulkDto.setCrNo(null);
			bulkDto.setSubCrNo(null);
		}
		
		return bulkDto;
	}
	
	public void initiateReminderLetterProcess(Object workFlowMap) {
		Map<String, Object> wrkFlowMap = (Map<String, Object>) workFlowMap;
//		Long intimKey = (Long) wrkFlowMap.get(SHAConstants.INTIMATION_KEY);
		
		Long claimKey = (Long) wrkFlowMap.get(SHAConstants.DB_CLAIM_KEY);
		
//		if(intimKey != null){
		
		if(claimKey != null){
		
//			List<Claim> claimList = getClaimByIntimation(intimKey);
			Claim claimObj = getClaimByClaimKey(claimKey);
			
//			if(claimList != null && !claimList.isEmpty()){
			if(claimObj != null){ 
			
//				Claim claim = claimList.get(0);	
				DBCalculationService dbService = new DBCalculationService();
				
				wrkFlowMap.put(SHAConstants.DB_CLAIM_KEY, claimObj.getKey());
				wrkFlowMap.put(SHAConstants.DB_CLAIM_NUMBER, claimObj.getClaimId());
				wrkFlowMap.put(SHAConstants.CLAIM_TYPE, claimObj.getClaimType().getValue());
				wrkFlowMap.put(SHAConstants.PAYLOAD_REGISTRATION_DATE, claimObj.getCreatedDate());
				wrkFlowMap.put(SHAConstants.PROCESS_TYPE, claimObj.getClaimType().getValue());
				if(ReferenceTable.HEALTH_LOB_KEY.equals(claimObj.getIntimation().getPolicy().getLobId())){
					wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY, SHAConstants.BILLS_NOT_RECEIVED);
				}
				else{
					if(claimObj.getIncidenceFlag() != null && (SHAConstants.DEATH_FLAG).equalsIgnoreCase(claimObj.getIncidenceFlag())){
						wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY, SHAConstants.PA_BILLS_NOT_RECEIVED_DEATH);
					}
					else{
						wrkFlowMap.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY, SHAConstants.PA_BILLS_NOT_RECEIVED_OTHERS);
					}
				}
				wrkFlowMap.put(SHAConstants.USER_ID,claimObj.getCreatedBy());
				wrkFlowMap.put(SHAConstants.WK_KEY, 0);
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				dbService.revisedInitiateTaskProcedure(objArrayForSubmit);
			}
		}
	}
	
	private MastersValue getConversionReasonObject(Long reasonforConversionId){
		MastersValue convReasonObj = null;
		
		Query namedQuery = entityManager.createNamedQuery("MastersValue.findByKey");
		namedQuery.setParameter("parentKey", reasonforConversionId);
		List<MastersValue> bdResultList = namedQuery.getResultList();
		
		if(bdResultList != null && !bdResultList.isEmpty()){
			convReasonObj = bdResultList.get(0);
			entityManager.refresh(convReasonObj);
		}
		return convReasonObj;
	}

	private void updateCpuCodeForIntimation(Intimation intimation) {

		TmpCPUCode masCpuCode = null;

		Policy policy = intimation.getPolicy();


		if (policy.getHomeOfficeCode() != null) {
			OrganaizationUnit branchOffice = getInsuredOfficeNameByDivisionCode(policy
					.getHomeOfficeCode());
			if (branchOffice != null) {
				String officeCpuCode = branchOffice.getCpuCode();
				if (officeCpuCode != null) {
					masCpuCode = getMasCpuCode(Long
							.valueOf(officeCpuCode));
					MasProductCpuRouting gmcRoutingProduct= getMasProductForGMCRouting(policy.getProduct().getKey());
					if(gmcRoutingProduct != null){
						if(masCpuCode != null && masCpuCode.getGmcRoutingCpuCode() != null){
							masCpuCode =  getMasCpuCode( masCpuCode.getGmcRoutingCpuCode());	
							intimation.setCpuCode(masCpuCode);
							intimation.setOriginalCpuCode(masCpuCode.getCpuCode());
							entityManager.merge(intimation);
							entityManager.flush();
						}
					}
					else if (masCpuCode != null) {
						intimation.setCpuCode(masCpuCode);
						intimation.setOriginalCpuCode(masCpuCode.getCpuCode());
						entityManager.merge(intimation);
						entityManager.flush();
					}

				}
			}
		}
		//GLX2020075 commented for this GMC CPU CR since CPU Routing for GMC already done on above code -noufel
		//		//added for CPU routing
		//		if(intimation.getPolicy() != null && intimation.getPolicy().getProduct().getKey() != null){
		//			String CpuCode= getMasProductCpu(intimation.getPolicy().getProduct().getKey());
		//			if(CpuCode != null){
		//				masCpuCode = getMasCpuCode(Long.valueOf(CpuCode));
		//				intimation.setCpuCode(masCpuCode);
		//				intimation.setOriginalCpuCode(masCpuCode.getCpuCode());
		//			}
		//		}
		//		//added for CPU routing
	}

	private TmpCPUCode getMasCpuCode(Long cpuCode) {
		try{
			Query query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
			query = query.setParameter("cpuCode", cpuCode);
			List<TmpCPUCode> listOfTmpCodes = query.getResultList();
			if (null != listOfTmpCodes && !listOfTmpCodes.isEmpty()) {
				return listOfTmpCodes.get(0);
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private OrganaizationUnit getInsuredOfficeNameByDivisionCode(
			String issuingOfficeCode) {
		try{
			List<OrganaizationUnit> organizationList = new ArrayList<OrganaizationUnit>();
			if (issuingOfficeCode != null) {
				Query findAll = entityManager.createNamedQuery(
						"OrganaizationUnit.findByUnitId").setParameter(
						"officeCode", issuingOfficeCode);
				organizationList = (List<OrganaizationUnit>) findAll
						.getResultList();
				if (organizationList != null && !organizationList.isEmpty()) {
					return organizationList.get(0);
				}
			}
		}
		catch(Exception e){
				e.printStackTrace();
		}
		return null;
	}
	
	private void setDBOutComeForConvertClaim(
			SearchBulkConvertReimbTableDto convertClaim, Claim claim,
			String outCome) {

		if (convertClaim != null) {

			Map<String, Object> workTask = (Map<String, Object>)convertClaim.getDbOutArray();

//			MastersValue claimTypeValue = new MastersValue();
//			claimTypeValue.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
//			claimTypeValue.setValue(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);

			if (workTask != null) {
				
				workTask.put(SHAConstants.CLAIM_TYPE, SHAConstants.REIMBURSEMENT_CLAIM_TYPE);

				workTask.put(SHAConstants.STAGE_SOURCE,SHAConstants.CONVERT_CLAIM);
						if (claim != null
								&& claim.getIntimation() != null
								&& claim.getIntimation().getCpuCode() != null
								&& claim.getIntimation().getCpuCode()
										.getCpuCode() != null) {
							workTask.put(SHAConstants.CPU_CODE,claim.getIntimation()
									.getCpuCode().getCpuCode().toString());
						}
						workTask.put(SHAConstants.OUTCOME,outCome);
				
				try {
//					Object[] objArrayForSubmit = SHAUtils.getObjArrayForSubmit(workTask);
//					
//					DBCalculationService dbCalService = new DBCalculationService();
//					dbCalService.initiateTaskProcedure(objArrayForSubmit);
					
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(workTask);
					
					DBCalculationService dbCalService = new DBCalculationService();
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 

			System.out.println("DB task Submit Executed Successfully");
		}
	
	}
/*
	private void setBPMOutComeForConvertClaim(
			SearchBulkConvertReimbTableDto convertClaim, Claim claim,
			String outCome) {
		if (convertClaim != null) {
			// XMLElement payload = searchFormDto.getHumanTask().getPayload();
			HumanTask humanTask = convertClaim.getHumanTask();
			PayloadBOType payload = humanTask.getPayloadCashless();
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayload = humanTask
					.getPayload();
			Map<String, String> regIntDetailsReq = new HashMap<String, String>();
			Map<String, String> pedReq = new HashMap<String, String>();

			MastersValue claimTypeValue = new MastersValue();
			claimTypeValue.setKey(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);

			if (payload != null) {

				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payload
						.getClaim();

				if (null != claimType) {
					claimType.setClaimId(convertClaim.getClaimNumber());
					claimType.setKey(claim.getKey());
					if (claimTypeValue != null
							&& claimTypeValue.getValue() != null) {
						claimType.setClaimType(claimTypeValue.getValue()
								.toUpperCase());
					}
				} else {
					claimType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType();
					claimType.setClaimId(convertClaim.getClaimNumber());
					claimType.setKey(claim.getKey());
					if (claimTypeValue != null
							&& claimTypeValue.getValue() != null) {
						claimType.setClaimType(claimTypeValue.getValue()
								.toUpperCase());
					}
				}

				ClassificationType classification = payload.getClassification();
				classification.setSource(SHAConstants.CONVERT_CLAIM);
				payload.setClassification(classification);

				if (payload.getClaimRequest() != null) {
					if (payload.getClaimRequest().getCpuCode() == null
							|| (payload.getClaimRequest().getCpuCode() != null && payload
									.getClaimRequest().getCpuCode()
									.equalsIgnoreCase(""))) {
						if (claim != null
								&& claim.getIntimation() != null
								&& claim.getIntimation().getCpuCode() != null
								&& claim.getIntimation().getCpuCode()
										.getCpuCode() != null) {
							ClaimRequestType claimRequest = payload
									.getClaimRequest();
							claimRequest.setCpuCode(claim.getIntimation()
									.getCpuCode().getCpuCode().toString());
						}
					}
				}

				payload.setClaim(claimType);
				humanTask.setOutcome(outCome);
				humanTask.setPayloadCashless(payload);
				
				try {
					SubmitClaimConvTask submitClaimConvTask = BPMClientContext
							.getSubmitClaimConvTask(BPMClientContext.USERID,
									BPMClientContext.PASSWORD);
					submitClaimConvTask.execute(convertClaim.getUsername(),
							humanTask);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType reimbursementClassification = reimbursementPayload
						.getClassification();
				reimbursementClassification
						.setSource(SHAConstants.CONVERT_CLAIM);
				reimbursementPayload
						.setClassification(reimbursementClassification);

				com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimType2 = reimbursementPayload
						.getClaim();

				if (null != claimType2) {
					claimType2.setClaimId(convertClaim.getClaimNumber());
					claimType2.setKey(claim.getKey());
					if (claimTypeValue != null
							&& claimTypeValue.getValue() != null) {
						claimType2.setClaimType(claimTypeValue.getValue()
								.toUpperCase());
					}
				} else {
					claimType2 = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
					claimType2.setClaimId(convertClaim.getClaimNumber());
					claimType2.setKey(claim.getKey());
					if (claimTypeValue != null
							&& claimTypeValue.getValue() != null) {
						claimType2.setClaimType(claimTypeValue.getValue()
								.toUpperCase());
					}
				}

				reimbursementPayload.setClaim(claimType2);
				humanTask.setOutcome("APPROVE");
			
				try {
					SubmitAckProcessConvertClaimToReimbTask submitConvertReimbursement = BPMClientContext
							.getSubmitConvertReimbursement(BPMClientContext.USERID,
									BPMClientContext.USER_PASSWORD);
					submitConvertReimbursement.execute(BPMClientContext.USERID,
							humanTask);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.out.println("BPM Executed Successfully");
		}
	}*/

	/*private void autoRegisterFVR(Intimation objIntimation, String userName) {
		try {
			FieldVisitRequest fvrRequest = new FieldVisitRequest();

			IntimationRule intimationRule = new IntimationRule();

			Query findByIntimationKey = entityManager
					.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter(
					"intimationKey", objIntimation.getKey());
			// Intimation objIntimation = entityManager.find(Intimation.class,
			// newIntimationDto.getKey());
			Claim claim = (Claim) findByIntimationKey.getSingleResult();

			Stage objStage = new Stage();
			objStage.setKey(ReferenceTable.CLAIM_REGISTRATION_STAGE);

			Status fvrStatus = new Status();
			fvrStatus.setKey(ReferenceTable.INITITATE_FVR);

			if (claim != null && claim.getIntimation() != null) {
				Intimation intimation = claim.getIntimation();
				Long hospital = intimation.getHospital();

				Hospitals hospitalById = getHospitalById(hospital);

				TmpCPUCode tmpCPUCode = getTmpCPUCode(hospitalById.getCpuId());
				if (tmpCPUCode != null) {
					fvrRequest.setFvrCpuId(tmpCPUCode.getCpuCode());
				}

			}

			MastersValue value = new MastersValue();
			value.setKey(ReferenceTable.FVR_ALLOCATION_TO);
			value.setValue(SHAConstants.FVR_ALLOCATION_ANY_ONE);
			fvrRequest.setAllocationTo(value);
			fvrRequest.setIntimation(objIntimation);
			fvrRequest.setClaim(claim);
			fvrRequest.setCreatedBy(userName);
			fvrRequest.setFvrTriggerPoints(SHAConstants.REGISTERED_CLAIM);
			fvrRequest.setPolicy(objIntimation.getPolicy());
			fvrRequest.setAllocationTo(value);
			fvrRequest.setActiveStatus(1L);
			fvrRequest.setOfficeCode(objIntimation.getPolicy()
					.getHomeOfficeCode());
			fvrRequest.setTransactionFlag("R");
			fvrRequest.setStatus(fvrStatus);
			fvrRequest.setStage(objStage);
			entityManager.persist(fvrRequest);
			entityManager.flush();
			// this.fvrRequest = fvrRequest;
			//callReimbursmentFVRTask(fvrRequest, objIntimation, claim, userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	private Hospitals getHospitalById(Long key) {

		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);

		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;

	}

	/*private TmpCPUCode getTmpCPUCode(Long cpuId) {
		try {
			Query findCpuCode = entityManager.createNamedQuery(
					"TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
			TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
			return tmpCPUCode;
		} catch (Exception e) {

		}
		return null;
	}*/

	/*private void callReimbursmentFVRTask(FieldVisitRequest fiedvisitRequest,
			Intimation objIntimation, Claim claim, String userName) {

		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType reimbursementPayload = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType();

		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType policyBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.policy.PolicyType();

		policyBo.setPolicyId(objIntimation.getPolicy().getPolicyNumber());
		reimbursementPayload.setPolicy(policyBo);

		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType intimationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.intimation.IntimationType();
		intimationBo.setIntimationNumber(objIntimation.getIntimationId());
		intimationBo.setKey(objIntimation.getKey());
		intimationBo.setStatus("TOFVR");
		reimbursementPayload.setIntimation(intimationBo);

		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType claimReq = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claimrequest.ClaimRequestType();
		if (null != objIntimation.getCpuCode()) {
			claimReq.setCpuCode(String.valueOf(objIntimation.getCpuCode()
					.getCpuCode()));
		}
		claimReq.setKey(claim.getKey());
		claimReq.setOption(SHAConstants.BILLS_NOT_RECEIVED);
		reimbursementPayload.setClaimRequest(claimReq);

		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType claimBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.claim.ClaimType();
		claimBo.setClaimId(claim.getClaimId());
		claimBo.setKey(claim.getKey());
		claimBo.setClaimType(claim.getClaimType() != null ? (claim
				.getClaimType().getValue() != null ? claim.getClaimType()
				.getValue().toUpperCase() : "") : "");
		reimbursementPayload.setClaim(claimBo);

		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType queryType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.query.QueryType();
		reimbursementPayload.setQuery(queryType);

		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType callsificationBo = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.classification.ClassificationType();

		callsificationBo.setPriority("");
		callsificationBo.setSource("");
		callsificationBo.setType("");
		reimbursementPayload.setClassification(callsificationBo);

		ProcessActorInfoType processActor = new ProcessActorInfoType();
		processActor.setEscalatedByRole("");
		processActor.setEscalatedByUser(BPMClientContext.BPMN_TASK_USER);
		reimbursementPayload.setProcessActorInfo(processActor);

		ProductInfoType productInfo = new ProductInfoType();
		productInfo.setLob("HEALTH");

		if (objIntimation.getPolicy() != null
				&& objIntimation.getPolicy().getProduct() != null
				&& objIntimation.getPolicy().getProduct().getKey() != null) {
			productInfo.setProductId(objIntimation.getPolicy().getProduct()
					.getKey().toString());
			productInfo.setProductName(objIntimation.getPolicy().getProduct()
					.getValue());
			reimbursementPayload.setProductInfo(productInfo);
		}

		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.hospitalinfo.HospitalInfoType hospitalInfoType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.hospitalinfo.HospitalInfoType();

		Long hospital = objIntimation.getHospital();

		Hospitals hospitalById = getHospitalById(hospital);
		hospitalInfoType.setKey(hospital);
		hospitalInfoType.setHospitalType(hospitalById.getHospitalType()
				.getValue());
		hospitalInfoType.setNetworkHospitalType(hospitalById
				.getNetworkHospitalType());
		reimbursementPayload.setHospitalInfo(hospitalInfoType);

		if (objIntimation.getAdmissionDate() != null) {
			String intimDate = SHAUtils.formatIntimationDateValue(objIntimation
					.getAdmissionDate());
			RRCType rrcType = new RRCType();
			rrcType.setFromDate(intimDate);
			reimbursementPayload.setRrc(rrcType);
		}

		com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.fieldvisit.FieldVisitType fieldVisitType = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.fieldvisit.FieldVisitType();
		fieldVisitType.setKey(fiedvisitRequest.getKey());

		Long cpuId = hospitalById.getCpuId();
		if (cpuId != null) {
			TmpCPUCode tmpCPUCode = getTmpCPUCode(cpuId);
			fieldVisitType
					.setRequestedBy(tmpCPUCode.getCpuCode() != null ? tmpCPUCode
							.getCpuCode().toString() : null);
		}
		reimbursementPayload.setFieldVisit(fieldVisitType);

		FVR reimbursementFVR = BPMClientContext
				.getReimbursementFVR(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);
		try {
			reimbursementFVR.initiate(userName, reimbursementPayload);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

	private void generateAndUploadCoveringLetter(
			SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto) {
		
		ClaimDto claimDto = searchBulkConvertReimbTableDto.getClaimDto();
		
		String hosptialName = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getName();
		
		if(hosptialName != null && !("").equalsIgnoreCase(hosptialName)){
			
//			hosptialName.replace("&", "and");
			
			hosptialName = StringUtil.replaceSubString(hosptialName, "&", "and");
			claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().setName(hosptialName);
		}
		
		List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
		claimDtoList.add(claimDto);
		DocumentGenerator docGenarator = new DocumentGenerator();
		String fileUrl = null;
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(claimDto.getClaimId());
		reportDto.setBeanList(claimDtoList);
		
		fileUrl = docGenarator.generatePdfDocument("ClaimFormCoveringLetter", reportDto);
		
		searchBulkConvertReimbTableDto.setFileUrl(fileUrl);
		
		searchBulkConvertReimbTableDto.getClaimDto().setDocFilePath(fileUrl);
		searchBulkConvertReimbTableDto.getClaimDto().setDocType(SHAConstants.CLAIM_COVERING_LETTER);
		
		uploadLetterToDMS(searchBulkConvertReimbTableDto);
		

	}
	private void uploadLetterToDMS(SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto){
		
		ClaimDto claimDto = searchBulkConvertReimbTableDto.getClaimDto();
		
		try{
			
			if(searchBulkConvertReimbTableDto.getFileUrl() != null && claimDto.getDocFilePath() != null && !claimDto.getDocFilePath().isEmpty()){
				
				String documentToken = null;
				WeakHashMap dataMap = new WeakHashMap();
				if(null != claimDto)
				{
					String userNameForDB = SHAUtils.getUserNameForDB(BPMClientContext.USERID);
					dataMap.put("intimationNumber",claimDto.getNewIntimationDto().getIntimationId());
					dataMap.put("claimNumber",claimDto.getClaimId());
					dataMap.put("filePath", claimDto.getDocFilePath());
					dataMap.put("docType", claimDto.getDocType());
					dataMap.put("docSources", SHAConstants.CONVERT_CLAIM);
					dataMap.put("createdBy", userNameForDB);
					documentToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
					SHAUtils.setClearReferenceData(dataMap);
				}
				if(null != documentToken){
				
					searchBulkConvertReimbTableDto.setDocToken(documentToken);
					submitConversionDetails(searchBulkConvertReimbTableDto);										
				}							
			}			
			 Claim claim = entityManager.find(Claim.class, claimDto.getKey());

				if(null != claim && null != claim.getKey())
				{
					entityManager.refresh(claim);
					Long coveringLetterFlag = 1l;
					claim.setConversionLetter(coveringLetterFlag);
		
					entityManager.merge(claim);
					entityManager.flush();
					entityManager.refresh(claim);
				}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		
	}
	
	/**
	 * Internal Service for submitting the Conversion Details to DB
	 * inserts new record to ClamConversion Table
	 * @param searchBulkConvertReimbTableDto
	 */
	private void submitConversionDetails(SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto){
		
		if(searchBulkConvertReimbTableDto != null && searchBulkConvertReimbTableDto.getCrno() != null && ! searchBulkConvertReimbTableDto.getCrno().isEmpty()){
			ClaimConversion resultObj = new ClaimConversion();

			resultObj.setIntimationKey(searchBulkConvertReimbTableDto.getClaimDto().getNewIntimationDto().getKey());
			resultObj.setIntimationNo(searchBulkConvertReimbTableDto.getClaimDto().getNewIntimationDto().getIntimationId());
			resultObj.setPreauthKey(searchBulkConvertReimbTableDto.getPreauthKey());
			resultObj.setClaimNo(searchBulkConvertReimbTableDto.getClaimDto().getClaimId());
			resultObj.setClaimKey(searchBulkConvertReimbTableDto.getClaimDto().getKey());
			resultObj.setBatchNo(searchBulkConvertReimbTableDto.getCrno());
			resultObj.setSubBatchNo(searchBulkConvertReimbTableDto.getSubcrno());
			resultObj.setCategory(searchBulkConvertReimbTableDto.getCategory());
			resultObj.setConvertToType(SHAConstants.REIMBURSEMENT);
			resultObj.setCpuCode(searchBulkConvertReimbTableDto.getClaimDto().getNewIntimationDto().getCpuCode() != null ? Long.valueOf(searchBulkConvertReimbTableDto.getClaimDto().getNewIntimationDto().getCpuCode()) : null);
			resultObj.setPrintFlag("N");
			resultObj.setPrintCount(0l);
			resultObj.setActiveStatus(1);
			resultObj.setDocToken(searchBulkConvertReimbTableDto.getDocToken() != null ? Long.valueOf(searchBulkConvertReimbTableDto.getDocToken()): null);
			resultObj.setLetterDate(SHAUtils.getCalendar(new Date()).getTime());
			resultObj.setCreatedBy(SHAUtils.getUserNameForDB(searchBulkConvertReimbTableDto.getUsername()));
			resultObj.setCreatedDate(SHAUtils.getCalendar(new Date()).getTime());
			try{
				entityManager.persist(resultObj);
				entityManager.flush();
				entityManager.refresh(resultObj);
			}
			catch(Exception e){
				e.printStackTrace();
				
			}
		 }		
	}
	
//	public void submitBulkLetter(List<SearchBatchConvertedTableDto> convertedBulkClaimListDto){
	public void submitBulkLetter(SearchBatchConvertedTableDto convertedBulkClaimDto){
		try{
			
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(370000);
			utx.setTransactionTimeout(360000);
			utx.begin();
			if(convertedBulkClaimDto != null ){
					
					if(convertedBulkClaimDto.getStatus().equalsIgnoreCase("Completed")){
						List<SearchBulkConvertReimbTableDto> printedList = convertedBulkClaimDto.getExportList();
						
						if(printedList != null && ! printedList.isEmpty()){
							for (SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto : printedList) {
								Long conversionKey =searchBulkConvertReimbTableDto.getConversionDetailsKey();
								if(conversionKey != null){
									Query convertClaimQuery = entityManager.createNamedQuery("ClaimConversion.findByKey");
									convertClaimQuery.setParameter("key", conversionKey);
									List<ClaimConversion> batchIdList = convertClaimQuery.getResultList();
									
									if(batchIdList != null && !batchIdList.isEmpty()){
										ClaimConversion convertClaimObj = batchIdList.get(0);
										entityManager.refresh(convertClaimObj);
										convertClaimObj.setModifiedDate(new Date());
										convertClaimObj.setModifiedBy(SHAUtils.getUserNameForDB(searchBulkConvertReimbTableDto.getUsername()));
										convertClaimObj.setPrintCount(convertClaimObj.getPrintCount()+1);
										convertClaimObj.setPrintFlag("Y");
										entityManager.merge(convertClaimObj);
										entityManager.flush();
										//entityManager.clear();
									}
								}
							}
						}			
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
			e.printStackTrace();
		}
		
	}
	
	/**
	 *  Internal Service for DB Search of Convert Claim
	 * @param formDTO
	 * @return
	 */
	private List<SearchBulkConvertReimbTableDto> searchDBConvertClaim(SearchBulkConvertFormDto formDTO, String userName){
		
		Long cpuCode = formDTO.getCpuCode() != null ?  formDTO.getCpuCode().getId() : null;
		
		//String typeValue = formDTO.getType() != null ?  formDTO.getType().getValue() != null ? formDTO.getType().getValue() : null : null;
		
		//Long type = formDTO.getType() != null ?  formDTO.getType().getId() != null ? formDTO.getType().getId() : null : null;
		
		//List<Long> conversionStatusKeyList = new ArrayList<Long>();
		
		
		//Map<String,Object> statusMapKey = SHAUtils.getConvertStatusMap();
		Map<Long,Long> conversionReasonKeyMap = SHAUtils.getConversionReasonId();
				
			//conversionStatusKeyList = (List<Long>)statusMapKey.get(typeValue);			
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		List<Claim> listOfClaim = new ArrayList<Claim>();
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();

		if(cpuCode != null){
		Predicate condition3 = criteriaBuilder.equal(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode"), cpuCode);
		conditionList.add(condition3);
		}		
		
		List<Long> claimTypeKey = new ArrayList<Long>();
		      claimTypeKey.add(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				Expression<Long> exp = root.<MastersValue> get("claimType").<Long> get("key");
				
		Predicate condition4 = exp.in(claimTypeKey);
		conditionList.add(condition4);
		
		//IMSSUPPOR-27243
		Predicate lobTypePredicate = root.<Long> get("lobId").in(ReferenceTable.HEALTH_LOB_KEY);
        conditionList.add(lobTypePredicate);
		
		Predicate statusPredicate = root.<Status> get("status").<Long> get("key").in(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
		conditionList.add(statusPredicate);
		
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));

		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);

			listOfClaim = typedQuery.getResultList();
		
		//List<Long> keys = new ArrayList<Long>();
		//Map<Long,Long> convertionIdMap = new HashMap<Long,Long>();
		
		List<Claim> resultList = new ArrayList<Claim>();
		
		List<SearchBulkConvertReimbTableDto> searchConvertClaimTableDTO  = new ArrayList<SearchBulkConvertReimbTableDto>();
		
		if(listOfClaim != null && ! listOfClaim.isEmpty()){
			for (Claim claim : listOfClaim) {
				List<Map<String, Object>> dbPreauthTask = getDBTask(claim.getIntimation() , SHAConstants.PP_CURRENT_QUEUE);
				if(dbPreauthTask == null || dbPreauthTask.isEmpty()){
					resultList.add(claim);
				}
			}
		}
//				if(getWaitingForPreauthTask(claim)){
//					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
//					if(preauth == null){
//						resultList.add(claim);
//					}
//				}else{
//					Preauth preauth = getLatestPreauthByClaim(claim.getKey());
//						if(preauth != null){
//						Long valueForConversion = ReferenceTable.getConversionStatusMap().get(preauth.getStatus().getKey());
//						if(valueForConversion != null){
//							resultList.add(claim);
//						}
//					}
//				}
//			}
//		}
		
		if(null != resultList && 0!= resultList.size())
		{			
			List<Claim> pageItemList = resultList;
	
			searchConvertClaimTableDTO = SearchBulkConvertReimbMapper.getInstance()
					.getSearchConvertClaimTableDTO(pageItemList);
			
			if(searchConvertClaimTableDTO != null && !searchConvertClaimTableDTO.isEmpty()){
				for (SearchBulkConvertReimbTableDto convertClaimDto : searchConvertClaimTableDTO) {
					convertClaimDto.setSrlNo(searchConvertClaimTableDTO.indexOf(convertClaimDto)+1);
					convertClaimDto.setIntimatedDateValue(convertClaimDto.getIntimatedDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(convertClaimDto.getIntimatedDate()) : "");
//					Date curDate = new Date();
//					convertClaimDto.setIntimatedDays(convertClaimDto.getIntimatedDate() != null ? curDate.compareTo(convertClaimDto.getIntimatedDate()) : 0);
					
					if (convertClaimDto.getIntimatedDate() != null) {
						int totalDays = getIntimNumberDays(convertClaimDto.getIntimatedDate());
						convertClaimDto.setIntimatedDays(totalDays);
					}	
					
					convertClaimDto.setSearchType(SHAConstants.DB_SEARCH);
					
					Claim claimObj = getClaimByClaimKey(convertClaimDto.getClaimKey());
					if (claimObj != null) {
						ClaimDto claimDto = (new ClaimService())
								.getClaimDto(claimObj, entityManager);
						convertClaimDto.setClaimDto(claimDto);
						
//						Date intimatedDate = claimObj.getIntimation().getCreatedDate();
//						
//						Calendar intimDate = Calendar.getInstance();
//						if (intimatedDate != null) {
							
//							intimDate.setTime(intimatedDate);
//							Calendar today = Calendar.getInstance();
//							Date todayDate = Calendar.getInstance().getTime();
//							
//							int currentMonthdays = today.get(Calendar.DAY_OF_MONTH)-1;
//							int actualday = intimDate.get(Calendar.DAY_OF_MONTH);
//							int intimaMonthdays = intimDate.getActualMaximum(intimDate.DAY_OF_MONTH);  
//							int intimCompdays = intimaMonthdays - actualday + 1;
//							int totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
//							
//							if(intimDate.get(Calendar.MONTH) < today.get(Calendar.MONTH) || totalDays < 0)
//							{
//								totalDays = 0;
//								for(int m = intimDate.get(Calendar.MONTH) ; m < today.get(Calendar.MONTH)-1 ; m++){
//								   totalDays +=  intimDate.getActualMaximum(m+1);
//								}
//								
//								totalDays += (intimCompdays + currentMonthdays);
//							}
//							else{
//								totalDays = today.get(Calendar.DAY_OF_MONTH) - intimDate.get(Calendar.DAY_OF_MONTH);
//							}
//							
////							totalDays = SHAUtils.getDateDiffDays(intimatedDate, new Date());
//							
//							convertClaimDto.setIntimatedDays(totalDays);
//						}					
						
						SelectValue conversionReasonSelectValue = new SelectValue();
					
						if(claimObj.getStatus().getProcessValue().toLowerCase().contains("deni")){
							conversionReasonSelectValue.setId(conversionReasonKeyMap.get(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS));
							conversionReasonSelectValue.setValue(SHAConstants.CASHLESS_DENIAL);
							
						}
						convertClaimDto.setReasonforConversion(conversionReasonSelectValue);
						convertClaimDto.setUsername(userName);
					}
				}			
			}
		}		
		return searchConvertClaimTableDTO;	
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
	
//	public Boolean getWaitingForPreauthTask(Claim objClaim)
//	{
//		Boolean isWaitingForPreauth = false;
//		ReceivePreAuthTask receivePreauthTask = BPMClientContext.getPreAuthReceived(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
//		
//		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
//		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType objIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
//		objIntimationType.setIntimationNumber(objClaim.getIntimation().getIntimationId());
//		payloadBO.setIntimation(objIntimationType);
//		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = receivePreauthTask.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(), payloadBO);
//		List<HumanTask> humanTaskList = tasks.getHumanTasks();
//		if(null != humanTaskList && !humanTaskList.isEmpty())
//		{
//			for (HumanTask humanTask : humanTaskList) {
//				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask.getPayloadCashless();
//				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payloadBOObj.getClaim();
//				//String claimId = claimType.getClaimId();
//				Long claimKey = claimType.getKey();
//				if(objClaim.getKey().equals(claimKey))
//				{
//					isWaitingForPreauth = true;
//					break;
//				}
//			}
//		}
//		return isWaitingForPreauth;
//	}
	
	
	 public String getMasProductCpu(Long key){
			
		 Query query = entityManager.createNamedQuery("MasProductCpuRouting.findByKey");
		 query = query.setParameter("key", key);
		 List<MasProductCpuRouting> resultList = (List<MasProductCpuRouting>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0).getCpuCode();
		 } 
				
		 return null;
	}
	 
	 public List<Map<String, Object>> getDBTask(Intimation intimation,String currentQ){
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
			mapValues.put(SHAConstants.CURRENT_Q, currentQ);
			
//			Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
//			
//			DBCalculationService db = new DBCalculationService();
//			 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService db = new DBCalculationService();
			 List<Map<String, Object>> taskProcedure = db.revisedGetTaskProcedure(setMapValues);
			
			
			if (taskProcedure != null){
				return taskProcedure;
			} 
			return null;
		}
	 

	 public MasProductCpuRouting getMasProductForGMCRouting(Long key){

		 Query query = entityManager.createNamedQuery("MasProductCpuRouting.findByKey");
		 query = query.setParameter("key", key);
		 List<MasProductCpuRouting> resultList = (List<MasProductCpuRouting>)query.getResultList();		 
		 if(resultList != null && !resultList.isEmpty()) {
			 return resultList.get(0);
		 } 

		 return null;
	 }
}
