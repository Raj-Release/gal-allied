/**
 * 
 */
package com.shaic.reimbursement.reminderBulkSearch;

import java.io.File;
import java.nio.file.Path;
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
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimReminderDetails;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasClaimRemainderSkip;
import com.shaic.domain.MasOmbudsman;
import com.shaic.domain.MasterService;
import com.shaic.domain.OrganaizationUnit;
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
import com.shaic.newcode.wizard.dto.NewIntimationDto;
/**
 * 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SearchGenerateRemainderBulkService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	private final Logger log = LoggerFactory.getLogger(SearchGenerateRemainderBulkService.class);
	
	public SearchGenerateRemainderBulkService() {
		super();
		
	}
	
	@SuppressWarnings("unchecked")
	public BulkReminderResultDto search(
			SearchGenerateRemainderBulkFormDTO searchFormDTO, String userName,
			String passWord,DBCalculationService dbCalService) {

		/*	try {
			
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(370000);
			utx.setTransactionTimeout(360000);
			utx.begin();
			// String intimationNo = searchFormDTO.getIntimationNo();
			// String claimNo = searchFormDTO.getClaimNo() != null ?
			// searchFormDTO.getClaimNo() : null;
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

				 // (!("").equalsIgnoreCase(claimTypeMasterValue) && (ReferenceTable.CLAIM_TYPE_REIMBURSEMENT).equalsIgnoreCase(claimTypeMasterValue) ){
					option = searchFormDTO.getClaimType() != null && searchFormDTO.getClaimType().getId() != null && (ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(searchFormDTO.getClaimType().getId()) ? SHAConstants.CASHLESS_CLAIM_TYPE : option;
					option = !("").equalsIgnoreCase(category) && (SHAConstants.PREAUTH_BILLS_NOT_RECEIVED).equalsIgnoreCase(category) ? SHAConstants.CASHLESS_CLAIM_TYPE : option;
					option = searchFormDTO.getClaimType() != null && searchFormDTO.getClaimType().getId() != null && (ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(searchFormDTO.getClaimType().getId()) ? SHAConstants.REIMBURSEMENT_CLAIM_TYPE : option;
					option = !("").equalsIgnoreCase(category) && (SHAConstants.BILLS_NOT_RECEIVED).equalsIgnoreCase(category) ? SHAConstants.REIMBURSEMENT_CLAIM_TYPE : option;
					option = !("").equalsIgnoreCase(category) && (SHAConstants.QUERY).equalsIgnoreCase(category) && (("").equalsIgnoreCase(claimTypeMasterValue) || ( searchFormDTO.getClaimType() != null && searchFormDTO.getClaimType().getId() != null && (ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(searchFormDTO.getClaimType().getId()) || (ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(searchFormDTO.getClaimType().getId()))) ? SHAConstants.QUERY : option;				
					
					if(!("").equalsIgnoreCase(category) && (SHAConstants.ALL).equalsIgnoreCase(category)){
						option =  SHAConstants.CASHLESS_CLAIM_TYPE;
//						category = "";
					}
					
			if (cpuCode != null && !("").equals(cpuCode)) {
				String cpuCodeValue[] = cpuCode.split(" ");
				if (cpuCodeValue.length >= 0) {
					cpuCode = cpuCodeValue[0];
				}
			}
			
			if((cpuCode == null || (cpuCode != null && !("").equals(cpuCode))) && (reminderType  == null ||(reminderType != null && reminderType.isEmpty())) && (category == null || (category != null && category.isEmpty())) && (claimTypeMasterValue == null || (claimTypeMasterValue != null && claimTypeMasterValue.isEmpty())) ){
			option = SHAConstants.CASHLESS_CLAIM_TYPE;
			category = SHAConstants.ALL;
			}
			
			// Long queryKey = null;
			// HumanTask humanTaskDTO = null;
			// List<String> intimationNoList = new ArrayList<String>();
			// List<Long> queryKeyList = new ArrayList<Long>();
			// List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
			// List<Long> rodKeyList = new ArrayList<Long>();
			// Long rodKey = null;

//			PayloadBOType payloadBOType = null;
	//		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType caPayloadBOType = null;
//			if (cpuCode != null || reminderType != null || !("").equalsIgnoreCase(category)
//					|| claimTypeMasterValue != null) {
//				
//			}


			payloadBOType = new PayloadBOType();
			caPayloadBOType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();

			IntimationType intimationType = new IntimationType();
			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType caIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
			ClaimType claimType = new ClaimType();
			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType caClaimType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType();
			if (claimTypeMasterValue != null && !("").equals(claimTypeMasterValue)) {
				claimType.setClaimType(claimTypeMasterValue);
				payloadBOType.setClaim(claimType);

//				caClaimType.setClaimType(claimTypeMasterValue);				
//				caPayloadBOType.setClaim(caClaimType);
			}
			ClaimRequestType claimRequestType = new ClaimRequestType();
			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claimrequest.ClaimRequestType caClaimRequestType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claimrequest.ClaimRequestType();
			if (null != cpuCode && !("").equals(cpuCode)) {
				claimRequestType.setCpuCode(cpuCode);
				caClaimRequestType.setCpuCode(cpuCode);
			}
			
			if (claimTypeMasterValue != null && !("").equals(claimTypeMasterValue)) {
				caClaimRequestType.setClientType(claimTypeMasterValue);
			}
			payloadBOType.setClaimRequest(claimRequestType);
			caPayloadBOType.setClaimRequest(caClaimRequestType);

			if (null != category) {
				// payloadBOType.getClaimRequest().setOption(category.toUpperCase());
				// caPayloadBOType.getClaimRequest().setOption(category.toUpperCase());

				if(!("").equalsIgnoreCase(category)){
					if(!(SHAConstants.QUERY).equalsIgnoreCase(category) && !(SHAConstants.ALL).equalsIgnoreCase(category)){
						payloadBOType.getClaimRequest().setOption(category);
					}
				}
			}

			QueryType queryType = new QueryType();

			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.query.QueryType caQueryType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.query.QueryType();

			if (null != reminderType) {
				reminderType = !("").equals(reminderType) ? ((SHAConstants.FIRST_REMINDER)
						.equalsIgnoreCase(reminderType) ? "1"
						: ((SHAConstants.SECOND_REMINDER)
								.equalsIgnoreCase(reminderType) ? "2" : "3"))
						: "";
				queryType.setStatus(reminderType);
				caQueryType.setStatus(reminderType);
			}
			payloadBOType.setQuery(queryType);
			caPayloadBOType.setQuery(caQueryType);			
			
			List<PagedTaskList> taskList = new ArrayList<PagedTaskList>();
			Integer totalRecords = 0;
			
			Pageable aPage = new Pageable();
			aPage.setPageSize(30);
			aPage.setPageNumber(1);
			switch(option)
			{
			 case SHAConstants.CASHLESS_CLAIM_TYPE : 
				 										CLGenerateLetterTask generateBillsNotReceivedReminderLetterTask = BPMClientContext
					 											.getCashlessRemainderLetterTask(userName, passWord);
				 										PagedTaskList billsNotReceivedTaskList = generateBillsNotReceivedReminderLetterTask
																.getTasks(userName, aPage, caPayloadBOType);
				 										PagedTaskList finalCashlessTaskList = new PagedTaskList();
				 										finalCashlessTaskList.setTotalRecords(billsNotReceivedTaskList.getTotalRecords());
				 										List<HumanTask> humanTaskList = new ArrayList<HumanTask>();
															try {
																	totalRecords += billsNotReceivedTaskList.getTotalRecords();
																	
																	if(totalRecords != 0){
																		
																		int lastpage = totalRecords / 30 ;
																		
																		if(totalRecords % 30 != 0){
																			lastpage += 1;
																		}
																		
																		for(int i = 1; i<=lastpage; i++){
																			aPage.setPageNumber(i);
																			billsNotReceivedTaskList = generateBillsNotReceivedReminderLetterTask
																					.getTasks(userName, aPage, caPayloadBOType);																			
																			humanTaskList.addAll(billsNotReceivedTaskList.getHumanTasks());
																		}
																		if(!humanTaskList.isEmpty()){
																			
																			finalCashlessTaskList.setHumanTasks(humanTaskList);																			
																			finalCashlessTaskList = validateTasks(finalCashlessTaskList, userName, passWord);
																			totalRecords = finalCashlessTaskList.getTotalRecords();	
																		}
																		
																	}
																	
															} catch (Exception e) {
																	totalRecords = 0;
															}
															if(billsNotReceivedTaskList != null && !humanTaskList.isEmpty()){
																
																taskList.add(finalCashlessTaskList);
															}
															
															if((SHAConstants.PREAUTH_BILLS_NOT_RECEIVED).equalsIgnoreCase(category)){
																break;
															}
																
			 case SHAConstants.REIMBURSEMENT_CLAIM_TYPE : if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(option) || (SHAConstants.BILLS_NOT_RECEIVED).equalsIgnoreCase(category) || (SHAConstants.ALL).equalsIgnoreCase(category)){
														GenerateLetterTask generateReimbReminderLetterTask = BPMClientContext
															.getGenerateReimbRemainderLetterTask(userName, passWord);
												   		PagedTaskList reimbReminderTaskList = generateReimbReminderLetterTask
												   			.getTasks(userName, aPage, payloadBOType);
												   		PagedTaskList finalreimbersementTaskList = new PagedTaskList();
												   		finalreimbersementTaskList.setTotalRecords(reimbReminderTaskList.getTotalRecords());
												   		List<HumanTask> reimbHumanTaskList = new ArrayList<HumanTask>();
												   		try {
												   				if(reimbReminderTaskList.getTotalRecords() != 0){
																	
																	int lastpage = reimbReminderTaskList.getTotalRecords() / 30 ;
																	
																	if(reimbReminderTaskList.getTotalRecords() % 30 != 0){
																		lastpage += 1;
																	}
																	
																	for(int i = 1; i<=lastpage; i++){
																		aPage.setPageNumber(i);
																		reimbReminderTaskList = generateReimbReminderLetterTask
																	   			.getTasks(userName, aPage, payloadBOType);																		
																		reimbHumanTaskList.addAll(reimbReminderTaskList.getHumanTasks());
																	}
																	if(!reimbHumanTaskList.isEmpty()){
																		finalreimbersementTaskList.setHumanTasks(reimbHumanTaskList);																		
																		finalreimbersementTaskList = validateTasks(finalreimbersementTaskList, userName, passWord);
																		totalRecords += finalreimbersementTaskList.getTotalRecords();	
																	}
																	
																}
																		
												   		} catch (Exception e) {
												   			totalRecords = 0;
												   		}

												   		if (reimbReminderTaskList != null && !reimbHumanTaskList.isEmpty()) {
												   			taskList.add(finalreimbersementTaskList);
												   		}
												   		if((SHAConstants.BILLS_NOT_RECEIVED).equalsIgnoreCase(category) ){
												   			break;					
												   		}
												   		}
			 case SHAConstants.QUERY : if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(option) || ((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(option) && (SHAConstants.QUERY).equalsIgnoreCase(category)) || ("").equalsIgnoreCase(category) || (SHAConstants.QUERY).equalsIgnoreCase(category) || (SHAConstants.ALL).equalsIgnoreCase(category) ){
										
											GenerateReminderLetterTask generateReminderLetterTask = BPMClientContext
													.getGenerateRemainderLetterTask(userName, passWord);
											PagedTaskList queryTaskList = generateReminderLetterTask
													.getTasks(userName, aPage, payloadBOType);
											PagedTaskList finalQueryReminderTaskList = new PagedTaskList();
											finalQueryReminderTaskList.setTotalRecords(queryTaskList.getTotalRecords());
											List<HumanTask> reimbQueryHumanTaskList = new ArrayList<HumanTask>();
											try {
												
								   				if(queryTaskList.getTotalRecords() != 0){
													
													int lastpage = queryTaskList.getTotalRecords() / 30 ;
													
													if(queryTaskList.getTotalRecords() % 30 != 0){
														lastpage += 1;
													}
													
													for(int i = 1; i<=lastpage; i++){
														aPage.setPageNumber(i);
														queryTaskList = generateReminderLetterTask
																.getTasks(userName, aPage, payloadBOType);
														
														reimbQueryHumanTaskList.addAll(queryTaskList.getHumanTasks());
													}
													
													if(!reimbQueryHumanTaskList.isEmpty()){
													
														finalQueryReminderTaskList.setHumanTasks(reimbQueryHumanTaskList);
														
														finalQueryReminderTaskList= validateTasks(finalQueryReminderTaskList, userName, passWord);
														totalRecords += finalQueryReminderTaskList.getTotalRecords();
													}												
								   				}															
														
											} catch (Exception e) {
												totalRecords = 0;
											}

											if (queryTaskList != null && !reimbQueryHumanTaskList.isEmpty()) {
												taskList.add(finalQueryReminderTaskList);
											}	
											}
									  	break;	
			
			default :  System.out.println("No Match Found for Search Filter");
			
			}	

			List<SearchGenerateReminderBulkTableDTO> tableListDTO = new ArrayList<SearchGenerateReminderBulkTableDTO>();
			BulkReminderResultDto bulkSearchresultDto = new BulkReminderResultDto(); 
			if (taskList != null && !taskList.isEmpty()) {

				for (PagedTaskList pagedTask : taskList) {
					
					if(pagedTask.getHumanTasks().size() > 0)
					{
					List<HumanTask> humanTaskList = pagedTask.getHumanTasks();
					for (HumanTask humanTask : humanTaskList) {
						SearchGenerateReminderBulkTableDTO searchTableDto = new SearchGenerateReminderBulkTableDTO();
						ClaimDto claimDto = new ClaimDto();
						searchTableDto.setHumanTask(humanTask);
						searchTableDto.setHumanTaskDTO(humanTask);
						searchTableDto.setTaskNumber(humanTask.getNumber());
						PayloadBOType payloadBO = new PayloadBOType();
						com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType caPayloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
						List<StageInformation> rodStageList = null;
						Long claimKey = null;
						Long cashlessKey = null;
						List<StageInformation> preauthStageList = null;
						List<StageInformation> enhancementStageList = null;
						
						if (humanTask.getPayload() != null) {

							payloadBO = humanTask.getPayload();
							claimKey = humanTask.getPayload().getClaim()
									.getKey();
							searchTableDto.setReminderType(humanTask
									.getPayload().getQuery()
									.getStatus());
						}
						
						else {
							caPayloadBO = humanTask.getPayloadCashless();
							cashlessKey = humanTask.getPayloadCashless()
									.getPreAuthReq() != null ? humanTask
									.getPayloadCashless().getPreAuthReq()
									.getKey() : null;
							claimKey = humanTask.getPayloadCashless()
									.getClaim().getKey();
							
							searchTableDto.setReminderType(caPayloadBO.getQuery()
									.getStatus());
							
							}
						
//						For Testing Purpose
//						claimKey = 5005530l;
						
						if (claimKey != null) {
							Claim claimObj = getClaimByClaimKey(claimKey);
							
							if(claimObj != null){
								claimDto = ClaimMapper.getInstance().getClaimDto(claimObj);
								claimDto.setModifiedDate(claimObj.getModifiedDate());
//							Long hospKey = claimObj.getIntimation().getHospital();
//							
//							Hospitals hospitalObj = getHospitalDetailsByKey(hospKey);
							
							
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
//							if(hospitalObj != null){
//								searchTableDto.setHospitalName(hospitalObj.getName());
//								searchTableDto.setHospitalAddress(hospitalObj.getAddress());
//							}
								searchTableDto.setClaimDto(claimDto);
							
								claimDto.setHumanTask(humanTask);
								Date remDate = new Date();
								if (("1").equals(searchTableDto
										.getReminderType())) {
									claimDto.setFirstReminderDate(remDate);
									claimDto.setReminderCount(1);
								}
								if (("2").equals(searchTableDto
										.getReminderType())) {
									claimDto.setSecondReminderDate(new Date());
									claimDto.setReminderCount(2);
								}
								if (("3").equals(searchTableDto
										.getReminderType())) {
									claimDto.setThirdReminderDate(new Date());
									claimDto.setReminderCount(3);
								}
							
								searchTableDto.setIntimationKey(claimObj.getIntimation().getKey());
							
							
							Query claimObjectQuery = entityManager
									.createNamedQuery("StageInformation.findClaimByStatus");
							claimObjectQuery.setParameter("claimkey",
									claimKey);
							claimObjectQuery.setParameter("stageKey",
									ReferenceTable.PREAUTH_STAGE);
							claimObjectQuery
									.setParameter(
											"statusKey",
											ReferenceTable.PREAUTH_APPROVE_STATUS);

							preauthStageList = claimObjectQuery
									.getResultList();

							claimObjectQuery = entityManager
									.createNamedQuery("StageInformation.findClaimByStatus");
							claimObjectQuery.setParameter("claimkey",
									claimKey);
							claimObjectQuery.setParameter("stageKey",
									ReferenceTable.ENHANCEMENT_STAGE);
							claimObjectQuery
									.setParameter(
											"statusKey",
											ReferenceTable.ENHANCEMENT_APPROVE_STATUS);

							enhancementStageList = claimObjectQuery
									.getResultList();	
							
							StageInformation preauthStageObj = null;
							if (enhancementStageList != null
									&& !enhancementStageList
											.isEmpty()) {
								preauthStageObj = enhancementStageList
										.get(0);
							} else {
								if (preauthStageList != null
										&& !preauthStageList
												.isEmpty()) {
									preauthStageObj = preauthStageList
											.get(0);
								}
							}
							if (preauthStageObj != null) {
								entityManager
										.refresh(preauthStageObj);
								claimDto.setPreauthApprovedDate(preauthStageObj
										.getCreatedDate());
								searchTableDto
										.setCategory(SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
								StageInformation stageObj = (StageInformation) preauthStageList
										.get(0);
								entityManager.refresh(stageObj);
								Long preauthKey = stageObj
										.getPreauth().getKey();
								searchTableDto
										.setPreauthKey(preauthKey);
							}								

							if (humanTask.getPayload() != null && humanTask
										.getPayload().getQuery().getKey() != null){								
								
									searchTableDto.setQueryKey(humanTask
										.getPayload().getQuery().getKey());
									searchTableDto.setCategory(SHAConstants.QUERY);
							}		

//									For Testing Purpose   
//									searchTableDto.setQueryKey(10000054l);									

							
							tableListDTO.add(searchTableDto);

						}
						}
					}
					
				 }
				}

			}
			
			
//			Page<SearchGenerateReminderBulkTableDTO> page = new Page<SearchGenerateReminderBulkTableDTO>();

			
			List<SearchGenerateReminderBulkTableDTO> resultListDto = new ArrayList<SearchGenerateReminderBulkTableDTO>();

			String batchId = "";			
			
			if (!tableListDTO.isEmpty()) {
				
				batchId = dbCalService.generateReminderBatchId(SHAConstants.REMINDER_BATCH_SEQUENCE_NAME);
				
				batchId = batchId != null && !("").equals(batchId) ? SHAConstants.BULK_REMINDER + batchId : "";
				
				bulkSearchresultDto.setBatchid(batchId);
				
				int totalListSize = tableListDTO.size();
				
				if(totalListSize > 100){
				
					for(int j = 0; j < totalListSize ; j++){
					
						int index =  1;
						String subBatchId = batchId + " - " + String.valueOf(index);
					
						if(index % 100 == 0){
							index++;
						}
						tableListDTO.get(j).setSubBatchId(subBatchId);
					}				
				}

				for (SearchGenerateReminderBulkTableDTO resultDto : tableListDTO) {
					ReimbursementQueryDto queryDto = null;

					resultDto.setBatchid(batchId);
					if (resultDto.getQueryKey() != null) {
						queryDto = (new ReimbursementQueryService())
								.getReimbursementQuery(resultDto.getQueryKey(),
										entityManager);
					}

					if (queryDto != null) {
						queryDto.setHumanTask(resultDto.getHumanTask());
						queryDto.setReminderCount(resultDto.getReminderType() != null ? Integer
								.valueOf(resultDto.getReminderType()) : null);

						Date remDate = new Date();
						if(resultDto.getReminderType() != null){
							if (resultDto.getReminderType().equals("1")) {
								queryDto.setFirstReminderDate(remDate);
							}
							if (resultDto.getReminderType().equals("2")) {
								queryDto.setSecondReminderDate(new Date());
							}
							if (resultDto.getReminderType().equals("3")) {
								queryDto.setThirdReminderDate(new Date());
							}
						}

						resultDto.setCategory(SHAConstants.QUERY);

					} else {
						queryDto = new ReimbursementQueryDto();
						ReimbursementDto reimbursementDto = new ReimbursementDto();
						reimbursementDto.setClaimDto(resultDto.getClaimDto());
						queryDto.setReimbursementDto(reimbursementDto);
						if(resultDto.getHumanTask().getPayload() != null){
							resultDto.setCategory(SHAConstants.BILLS_NOT_RECEIVED);
						}
					}

					resultDto.setReimbQueryDto(queryDto);
					String remType = "";
					remType = resultDto.getReminderType() != null ? (StringUtils
							.equalsIgnoreCase(resultDto.getReminderType(), "1") ? SHAConstants.FIRST_REMINDER
							: (StringUtils.equalsIgnoreCase(
									resultDto.getReminderType(), "2") ? SHAConstants.SECOND_REMINDER
									: (StringUtils.equalsIgnoreCase(
											resultDto.getReminderType(), "3") ? SHAConstants.CLOSE
											: "")))
							: "";
					resultDto.setReminderType(remType);
					
					resultDto.setSno(String.valueOf(tableListDTO
							.indexOf(resultDto) + 1));
					resultDto.setUsername(userName);
					resultListDto.add(resultDto);

				}
			}
			
			bulkSearchresultDto.setResultListDto(resultListDto);	
			bulkSearchresultDto.setTotalNoofRecords(totalRecords);
			
			if(resultListDto == null || resultListDto.isEmpty()){
				bulkSearchresultDto.setBatchid(null);
			}
			
//			page.setPageItems(resultListDto);
//			
//			page.setTotalRecords(totalRecords);

			utx.commit();
			return bulkSearchresultDto;
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
		return null;*/
		return null;

	} 	
	
	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}

	private TmpCPUCode getTmpCPUCode(Long cpuKey){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuKey);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			return null;
		}
		
	}

		public SearchGenerateReminderBulkTableDTO submitReminderLetter(SearchGenerateReminderBulkTableDTO reminderLetterDto){
		
		/*try{
			
		utx.begin();
		String fileUrl = reminderLetterDto.getFileUrl();
		
		*//**
		 * Need to upload reminder Letter to DMS
		 *//*
		
		if(null != fileUrl && !("").equalsIgnoreCase(fileUrl))
		{
			HashMap<String, String> dataMap = new HashMap<String, String>();
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
				
		//For testing Purpose commented Need to uncomment below lines
				
				SubmitGenerateReminderLetterTask submitTask = BPMClientContext.getSubmitQueryReimnderLetterTask(reminderLetterDto.getUsername(), reminderLetterDto.getPassword());
				reminderLetterDto.getReimbQueryDto().getHumanTask().setOutcome("SUBMIT");
				submitTask.execute(reminderLetterDto.getUsername(), reminderLetterDto.getReimbQueryDto().getHumanTask());
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
				reminderLetterDto.getClaimDto().getHumanTask().setOutcome("APPROVE");
				SubmitGenerateLetterTask submitCLRemLetterTask = BPMClientContext.getSubmitReimbReimnderLetterTask(reminderLetterDto.getUsername(), reminderLetterDto.getPassword());
				submitCLRemLetterTask.execute(reminderLetterDto.getUsername(), reminderLetterDto.getClaimDto().getHumanTask());
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
				reminderLetterDto.getClaimDto().getHumanTask().setOutcome("SUBMIT");
				SubmitCLGenerateLetterTask   submitCLRemLetterTask = BPMClientContext.getSubmitCLReimnderLetterTask(reminderLetterDto.getUsername(), reminderLetterDto.getPassword());
				submitCLRemLetterTask.execute(reminderLetterDto.getUsername(), reminderLetterDto.getClaimDto().getHumanTask());
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
		}*/
		
		return reminderLetterDto;
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
		
		if(reminderLetterDto != null && reminderLetterDto.getCategory() != null &&  (SHAConstants.QUERY).equalsIgnoreCase(reminderLetterDto.getCategory()) ){
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
		
		else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null &&  (SHAConstants.BILLS_NOT_RECEIVED).equalsIgnoreCase(reminderLetterDto.getCategory())){
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
	
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByClaimKey(Long claimKey) {
		Claim claim = null;
		try{
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Claim> claimList = (List<Claim>)query.getResultList();
		
		if(claimList != null && ! claimList.isEmpty()){
			for (Claim claimObj : claimList) {
				entityManager.refresh(claimObj);
				claim = claimObj;
			}			
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return claim;
	}
	
	
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
	
	@SuppressWarnings("unchecked")
	public List<BulkReminderResultDto> searchBatchByIdOrIntimation(Map<String,String> searchfields){
						
		String batchId = searchfields.containsKey(SHAConstants.SEARCH_REMINDER_BATCH) && searchfields.get(SHAConstants.SEARCH_REMINDER_BATCH) != null ? ( "%" + (String)searchfields.get(SHAConstants.SEARCH_REMINDER_BATCH)) : null;
		String intimationNo = searchfields.containsKey(SHAConstants.SEARCH_REMINDER_INTIMATION) && searchfields.get(SHAConstants.SEARCH_REMINDER_INTIMATION) != null ? ("%" + (String)searchfields.get(SHAConstants.SEARCH_REMINDER_INTIMATION) + "%") : null;
		List<BulkReminderResultDto> resultList =  new ArrayList<BulkReminderResultDto>();
		
		List<ClaimReminderDetails> resultObjListByIntimation =  new ArrayList<ClaimReminderDetails>();
		List<ClaimReminderDetails> resultObjListByBatchId =  new ArrayList<ClaimReminderDetails>();
		List<ClaimReminderDetails> resultObjList =  new ArrayList<ClaimReminderDetails>();
		List<BulkReminderResultDto> resultDtoListByBatchId =  new ArrayList<BulkReminderResultDto>();
		List<BulkReminderResultDto> finalresultDtoList =  new ArrayList<BulkReminderResultDto>();
		
		try{
		if(batchId != null){
			Query reminderBatchByIdQuery = entityManager.createNamedQuery("ClaimReminderDetails.findSubBatchIdByBatchId");	
			reminderBatchByIdQuery.setParameter("batchId", batchId.toLowerCase());
			
			List<ClaimReminderDetails> resultObjListBysubId = reminderBatchByIdQuery.getResultList();
			
			if(resultObjListBysubId != null && !resultObjListBysubId.isEmpty() && resultObjListBysubId.get(0) != null){
			Iterator resultObjListIterator = resultObjListBysubId.iterator();
			
			for(;resultObjListIterator.hasNext();){
				ClaimReminderDetails reminderDetails = new ClaimReminderDetails();	
				Object[] reminderDetailsResult = (Object[]) resultObjListIterator.next();
				if(reminderDetailsResult[0] != null){
					String subBatchNo = (String)reminderDetailsResult[0];
					
					if(subBatchNo != null && !("").equalsIgnoreCase(subBatchNo)){
						Query reminderQueryBySubBatchId = entityManager.createNamedQuery("ClaimReminderDetails.findBySubBatchId");	
						reminderQueryBySubBatchId.setParameter("subBatchId", subBatchNo);
						List<ClaimReminderDetails> resultBySubIdList = reminderQueryBySubBatchId.getResultList();
						if(resultBySubIdList != null && !resultBySubIdList.isEmpty()){
							reminderDetails.setBatchNo(resultBySubIdList.get(0).getBatchNo());
							reminderDetails.setSubBatchNo(subBatchNo);
							reminderDetails.setTotalNoofRecords(resultBySubIdList.size());
							resultObjListByBatchId.add(reminderDetails);
						}
					}
			   }
			}			
		}
		if(resultObjListByBatchId == null || resultObjListByBatchId.isEmpty() ) {
				
				Query reminderByBatchIdQuery = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchIdGroup");
				reminderByBatchIdQuery.setParameter("batchId", batchId.toLowerCase());
				
				List<ClaimReminderDetails> resultQueryObjListByBatchId = reminderByBatchIdQuery.getResultList();		
				
				if(resultQueryObjListByBatchId != null && !resultQueryObjListByBatchId.isEmpty() && resultQueryObjListByBatchId.get(0) != null){
					Iterator resultObjListIterator = resultQueryObjListByBatchId.iterator();
					
					for(;resultObjListIterator.hasNext();){
						ClaimReminderDetails reminderDetails = new ClaimReminderDetails();
						Object[] reminderDetailsResult = (Object[]) resultObjListIterator.next();
						if(reminderDetailsResult[0] != null){
							String batchNo = (String)reminderDetailsResult[0];
							Long noofrecords = (Long)reminderDetailsResult[1];
							reminderDetails.setBatchNo(batchNo);
							reminderDetails.setTotalNoofRecords(noofrecords != null ? noofrecords.intValue() : null);				
						}
						resultObjListByBatchId.add(reminderDetails);
					}
				}
				
			}			
		}
		else if(intimationNo != null){
			
			Query reminderSubBatchQueryByIntimationGroup = entityManager.createNamedQuery("ClaimReminderDetails.findBySubBatchIntimationNoGroup");
			reminderSubBatchQueryByIntimationGroup.setParameter("intimationNo", intimationNo.toLowerCase());
			List<ClaimReminderDetails> intimationQueryResultListBySubBatchId = reminderSubBatchQueryByIntimationGroup.getResultList();
			if(intimationQueryResultListBySubBatchId != null && !intimationQueryResultListBySubBatchId.isEmpty() && intimationQueryResultListBySubBatchId.get(0) != null){
				
				Iterator listIter1 = intimationQueryResultListBySubBatchId.iterator();
				for(;listIter1.hasNext();){
					ClaimReminderDetails reminderDetails = new ClaimReminderDetails();
					Object[] reminderDetailsResult = (Object[]) listIter1.next();
					if(reminderDetailsResult[0] != null){
						String batchNo = (String)reminderDetailsResult[0];
						Long noofrecords = (Long)reminderDetailsResult[1];
						reminderDetails.setBatchNo(batchNo);
						reminderDetails.setTotalNoofRecords(noofrecords != null ? noofrecords.intValue() : null);				
						resultObjListByIntimation.add(reminderDetails);
					}
					
				}
				
			}
			
			if(resultObjListByIntimation == null || resultObjListByIntimation.isEmpty()){			
			
			Query reminderQueryByIntimationGroup = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchIntimationNoGroup");
			reminderQueryByIntimationGroup.setParameter("intimationNo", intimationNo.toLowerCase());
			List<ClaimReminderDetails> intimationQueryResultListByBatchId = reminderQueryByIntimationGroup.getResultList();
			
				if(intimationQueryResultListByBatchId != null && !intimationQueryResultListByBatchId.isEmpty() && intimationQueryResultListByBatchId.get(0) != null){
				
					Iterator listIter1 = intimationQueryResultListByBatchId.iterator();
					for(;listIter1.hasNext();){
						ClaimReminderDetails reminderDetails = new ClaimReminderDetails();
						Object[] reminderDetailsResult = (Object[]) listIter1.next();
						if(reminderDetailsResult[0] != null){
							String batchNo = (String)reminderDetailsResult[0];
							reminderDetails.setBatchNo(batchNo);
						}
						resultObjListByIntimation.add(reminderDetails);
					}
					
				}
			}
		}
		
		if(resultObjListByIntimation != null && !resultObjListByIntimation.isEmpty()){
			resultObjList.addAll(resultObjListByIntimation); 
		}
		else if(resultObjListByBatchId != null && !resultObjListByBatchId.isEmpty()){
			resultObjList.addAll(resultObjListByBatchId);
		}
			
		if(resultObjList != null && !resultObjList.isEmpty()){	
		for(ClaimReminderDetails resultObj :resultObjList){
			
			BulkReminderResultDto reminderDetailsDto = new BulkReminderResultDto();
						
			reminderDetailsDto.setSno(resultObjList.indexOf(resultObj)+1);
			String subBatchId = resultObj.getSubBatchNo();
			String batchNo = resultObj.getBatchNo();
			List<SearchGenerateReminderBulkTableDTO> exportList = new ArrayList<SearchGenerateReminderBulkTableDTO>();
			List<ClaimReminderDetails> resultBySubIdList = new ArrayList<ClaimReminderDetails>();
			if( subBatchId != null && !subBatchId.isEmpty()){
				
				Query reminderQueryBySubBatchId = entityManager.createNamedQuery("ClaimReminderDetails.findBySubBatchId");	
				reminderQueryBySubBatchId.setParameter("subBatchId", subBatchId);
				resultBySubIdList = reminderQueryBySubBatchId.getResultList();
				
			}	
			else if(batchNo != null && !batchNo.isEmpty()){
				
				Query reminderQueryByBatchId = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchId");	
				reminderQueryByBatchId.setParameter("batchId", batchNo.toLowerCase());
				resultBySubIdList = reminderQueryByBatchId.getResultList();
			}					
			if(resultBySubIdList != null && !resultBySubIdList.isEmpty()){
				populateReminderDetailsToDto(reminderDetailsDto,resultBySubIdList.get(0));
				Long claimKey = resultBySubIdList.get(0).getClaimKey();
				String claimType = "";
				if(claimKey != null){
					Claim claim = entityManager.find(Claim.class, claimKey);
											
					claimType = claim.getClaimType().getValue(); 
				}
//				reminderDetailsDto.setClaimType(claimType);
				for(ClaimReminderDetails reminderObj : resultBySubIdList){
					
					SearchGenerateReminderBulkTableDTO excelObj = populateExportExcelDto(reminderObj,reminderDetailsDto.getReminderType());
					excelObj.setSno(String.valueOf(resultBySubIdList.indexOf(reminderObj)+1));
					exportList.add(excelObj);
					
				}
			}
			
			reminderDetailsDto.setResultListDto(exportList);
			reminderDetailsDto.setTotalNoofRecords(resultBySubIdList.size());
			resultList.add(reminderDetailsDto);
		}
		
		}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resultList;
		
	}
	
	private SearchGenerateReminderBulkTableDTO populateExportExcelDto(ClaimReminderDetails reminderObj,String reminderTyString){
		SearchGenerateReminderBulkTableDTO resultDto = new SearchGenerateReminderBulkTableDTO();
		resultDto.setClaimIntimationNo(reminderObj.getIntimatonNo());
		resultDto.setClaimKey(reminderObj.getClaimKey());
		
		String claimType = "";
		if(resultDto.getClaimKey() != null){
			Claim claim = entityManager.find(Claim.class, resultDto.getClaimKey());
									
			claimType = claim != null && claim.getClaimType() != null ? claim.getClaimType().getValue() : ""; 
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
	
	private void populateReminderDetailsToDto(BulkReminderResultDto reminderDetailsDto, ClaimReminderDetails resultObj){
		
		reminderDetailsDto.setReminderDetailsKey(resultObj.getKey());
		reminderDetailsDto.setIntimationNo(resultObj.getIntimatonNo());
		reminderDetailsDto.setIntimationKey(resultObj.getIntimationKey());
		reminderDetailsDto.setClaimNo(resultObj.getClaimNo());
		reminderDetailsDto.setPreauthKey(resultObj.getTransacKey());
		reminderDetailsDto.setQueryKey(resultObj.getQueryKey());
		reminderDetailsDto.setBatchid(resultObj.getBatchNo());
		
		reminderDetailsDto.setSubBatchid(resultObj.getSubBatchNo());
//		reminderDetailsDto.setCategory(resultObj.getCategory());
//		reminderDetailsDto.setCpuCode(resultObj.getCpuCode() != null ? resultObj.getCpuCode().toString() : "");
		reminderDetailsDto.setReminderCount(resultObj.getReminderCount());
		reminderDetailsDto.setTotalNoofRecords(resultObj.getTotalNoofRecords());
//		String reminderType = resultObj.getReminderCount() != null ? (resultObj.getReminderCount().equals(1) ? "First Reminder" : (resultObj.getReminderCount().equals(2) ? "Second Reminder" :"Close Reminder") ) : "";	
		reminderDetailsDto.setPrint(resultObj.getPrintFlag());
		reminderDetailsDto.setPrintCount(resultObj.getPrintCount());
//		reminderDetailsDto.setReminderType(reminderType);
		reminderDetailsDto.setDocToken(resultObj.getDocumentToken());
		reminderDetailsDto.setLetterGeneratedDate(resultObj.getGeneratedDate());
		reminderDetailsDto.setStatus(("y").equalsIgnoreCase(resultObj.getPrintFlag()) ? "Completed" : "In Progress");
	}
	
	public void submitBulkLetter(BulkReminderResultDto reminderDetailsDto){
		try{
			
			utx.begin();
		String batchId = reminderDetailsDto.getBatchid();
		String subbatchId = reminderDetailsDto.getSubBatchid();
		List<ClaimReminderDetails> batchIdList = new ArrayList<ClaimReminderDetails>();
		if(subbatchId != null){
			Query reminderQuery = entityManager.createNamedQuery("ClaimReminderDetails.findBySubBatchId");
			reminderQuery.setParameter("subBatchId", subbatchId);
			batchIdList = reminderQuery.getResultList(); 
		}
		
		else if(batchId != null){
			Query reminderQuery = entityManager.createNamedQuery("ClaimReminderDetails.findByBatchId");
			reminderQuery.setParameter("batchId",batchId.toLowerCase()); 
			batchIdList =  reminderQuery.getResultList();			
		}
		
		if(batchIdList != null && !batchIdList.isEmpty()){
			for(ClaimReminderDetails claimReminderObj :batchIdList){
				entityManager.refresh(claimReminderObj);
				claimReminderObj.setModifiedDate(new Date());
				claimReminderObj.setModifiedBy(SHAUtils.getUserNameForDB(reminderDetailsDto.getUserName()));
				claimReminderObj.setPrintCount(claimReminderObj.getPrintCount()+1);
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
	
	public void submitTransacReminderDetails(SearchGenerateReminderBulkTableDTO reminderDetailsDto){
//		public void submitReminderDetails(BulkReminderResultDto reminderDetailsDto){
		
			
			if(reminderDetailsDto != null && reminderDetailsDto.getBatchid() != null && ! reminderDetailsDto.getBatchid().isEmpty()){
			ClaimReminderDetails resultObj = new ClaimReminderDetails();
			
//			resultObj.setIntimationKey(reminderDetailsDto.getIntimationKey());
//			resultObj.setIntimatonNo(reminderDetailsDto.getClaimDto().getNewIntimationDto().getIntimationId());
//			resultObj.setQueryKey(reminderDetailsDto.getQueryKey());
//			resultObj.setTransacKey(reminderDetailsDto.getPreauthKey());
//			resultObj.setClaimNo(reminderDetailsDto.getClaimNo());
//			resultObj.setBatchNo(reminderDetailsDto.getBatchid());
//			resultObj.setSubBatchNo(reminderDetailsDto.getSubBatchid());
//			resultObj.setCategory(reminderDetailsDto.getCategory());
//			resultObj.setCpuCode(reminderDetailsDto.getCpuCode() != null ? resultObj.getCpuCode() : null);
//			resultObj.setReminderCount(reminderDetailsDto.getReminderCount());
//			resultObj.setPrintFlag("Y");
//			resultObj.setPrintCount(reminderDetailsDto.getPrintCount());
//			resultObj.setDocumentToken(reminderDetailsDto.getDocToken());
//			resultObj.setGeneratedDate(reminderDetailsDto.getLetterGeneratedDate());
//			resultObj.setCreatedBy(SHAUtils.getUserNameForDB(reminderDetailsDto.getUserName()));
			
			resultObj.setIntimationKey(reminderDetailsDto.getClaimDto().getNewIntimationDto().getKey());
			resultObj.setIntimatonNo(reminderDetailsDto.getClaimDto().getNewIntimationDto().getIntimationId());
			resultObj.setQueryKey(reminderDetailsDto.getReimbQueryDto() != null ? reminderDetailsDto.getReimbQueryDto().getKey() : null);
			resultObj.setTransacKey(reminderDetailsDto.getPreauthKey());
			resultObj.setClaimNo(reminderDetailsDto.getClaimDto().getClaimId());
			resultObj.setClaimKey(reminderDetailsDto.getClaimDto().getKey());
			
			resultObj.setBatchNo(reminderDetailsDto.getBatchid());
			resultObj.setSubBatchNo(reminderDetailsDto.getSubBatchId());
			resultObj.setCategory(reminderDetailsDto.getCategory());
			if(SHAConstants.PAN_CARD.equalsIgnoreCase(reminderDetailsDto.getCategory())){
				resultObj.setReimainderCategory(SHAUtils.getCurrentRemQueNameForPan(reminderDetailsDto.getReminderType() != null ? reminderDetailsDto.getReminderType().toLowerCase() : ""));
			}
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
	
	
	public void submitReminderDetails(SearchGenerateReminderBulkTableDTO reminderDetailsDto){
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
		if(SHAConstants.PAN_CARD.equalsIgnoreCase(reminderDetailsDto.getCategory())){
			resultObj.setReimainderCategory(SHAUtils.getCurrentRemQueNameForPan(reminderDetailsDto.getReminderType().toLowerCase()));
		}else{
			resultObj.setReimainderCategory(reminderDetailsDto.getReminderType());	
		}
		
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
//			utx.setTransactionTimeout(360000);
//			utx.begin();
			entityManager.persist(resultObj);
//			utx.commit();
		}
		catch(Exception e){
			e.printStackTrace();
//			try {
//				utx.rollback();
//			} catch (IllegalStateException | SecurityException
//					| SystemException e1) {
//				e1.printStackTrace();
//			}
			
		}
	 }
	}
	
	
	public List<BulkReminderResultDto> searchPrevBatch(){
	
		List<BulkReminderResultDto> resultList =  new ArrayList<BulkReminderResultDto>();
		
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
		List<BulkReminderResultDto> listBySubBatchId = new ArrayList<BulkReminderResultDto>();
		Long totalRecords = null;
		
		if(reminderDetailsListBySubBatchId != null && !reminderDetailsListBySubBatchId.isEmpty() && reminderDetailsListBySubBatchId.get(0) != null){	
			Iterator reminderIter1 = reminderDetailsListBySubBatchId.iterator();
			
			for(;reminderIter1.hasNext();){
				
				BulkReminderResultDto reminderDetailsDto = new BulkReminderResultDto();
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
					
					BulkReminderResultDto reminderDetailsDto = new BulkReminderResultDto();
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
				for( BulkReminderResultDto bulkReminderDto : listBySubBatchId){
					
					
					BulkReminderResultDto reminderDetailsDto = new BulkReminderResultDto();
					List<SearchGenerateReminderBulkTableDTO> exportExcelList = new ArrayList<SearchGenerateReminderBulkTableDTO>();
					
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
						
						String reminderType = claimReminderDetailsObjList.get(0).getReminderCount() != null ? (claimReminderDetailsObjList.get(0).getReminderCount().equals(1) ? "First Reminder" : (claimReminderDetailsObjList.get(0).getReminderCount().equals(2) ? "Second Reminder" :"Close Reminder") ) : "";	
						Long claimKey = claimReminderDetailsObjList.get(0).getClaimKey();
						
						String claimType = "";
						if(claimKey != null){
							Claim claim = entityManager.find(Claim.class, claimKey);
													
							claimType = claim != null && claim.getClaimType() != null ? claim.getClaimType().getValue() : ""; 
						}
						
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
							SearchGenerateReminderBulkTableDTO exportExcelDto = populateExportExcelDto(reminderObj, reminderType);
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
	
@SuppressWarnings("unchecked")
public void searchNGenerateBulkReminderLetter(MasterService masterService){
		
		try{
//			utx.setTransactionTimeout(360000);
//			utx.begin();
			log.info("********************  BULK REMINDER LETTER STARTED ********************************");
		Query workFlowQuery = entityManager.createNamedQuery("CashlessWorkFlow.findPendingReminderLetter");
		workFlowQuery.setParameter("lobStr",SHAConstants.HEALTH_LOB.toLowerCase());
		
		List<String> queList = new ArrayList<String>();
		queList.add(SHAConstants.FIRST_REMINDER_LETTER_CURRENT_QUEUE);
		queList.add(SHAConstants.SECOND_REMINDER_LETTER_CURRENT_QUEUE);
		queList.add(SHAConstants.THIRD_REMINDER_LETTER_CURRENT_QUEUE);

		List<String> clmTypeList = new ArrayList<String>();
		clmTypeList.add(SHAConstants.CASHLESS_CLAIM_TYPE);
		clmTypeList.add(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
		
		List<String> remCategory = new ArrayList<String>();
		remCategory.add(SHAConstants.BILLS_NOT_RECEIVED);
		remCategory.add(SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
		remCategory.add(SHAConstants.QUERY);
		
		List<SelectValue> cpuCodeList = masterService.getTmpCpuCodeListWithoutDescription(entityManager);
		
		Path tempDir = SHAFileUtils.createTempDirectory("dummyHolder");
		
		for(String curQ : queList){
			workFlowQuery.setParameter("curQ", curQ);
			
	//		List<Map<String, Object>> taskParamObjList = null;
	//		
	//		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
	//		
	//		mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.FIRST_REMINDER_LETTER_CURRENT_QUEUE);
	//		mapValues.put(SHAConstants.USER_ID,BPMClientContext.DB_USER_NAME);	
			
			BulkReminderResultDto bulkSearchresultDto = new BulkReminderResultDto();			
			
			List<File> filelistForMerge = null;
			List<CashlessWorkFlow> pendingTaskList = new ArrayList<CashlessWorkFlow>();
			for(String claimType : clmTypeList){
		
				
				for(String categ : remCategory){
					for(SelectValue cpuCode : cpuCodeList){
						Integer totalRecords = 0;		
						
	//					mapValues.put(SHAConstants.CPU_CODE, Long.valueOf(cpuCode.getValue()));
	//					mapValues.put(SHAConstants.CLAIM_TYPE,claimType);
	//					mapValues.put(SHAConstants.PAYLOAD_REMINDER_CATEGORY,categ);
	//					
	//					Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
	//					
	//					DBCalculationService dbCalculationService = new DBCalculationService();
	//					taskParamObjList = dbCalculationService.revisedGetTaskProcedure(setMapValues);
						
						workFlowQuery.setParameter("clmType", claimType);
						workFlowQuery.setParameter("cpuCode", Long.valueOf(cpuCode.getValue()));
						workFlowQuery.setParameter("remCateg", categ.toLowerCase());
						
						pendingTaskList = workFlowQuery.getResultList();
						
						/**
					     * For specific  Intimation - Reminder letter Based on Work Flow Key
					     */
//					    Query wkQuery = entityManager.createNamedQuery("CashlessWorkFlow.findByKey");
//					    wkQuery.setParameter("wk_key", 687240l);
//					    List<CashlessWorkFlow> wk_list = (List<CashlessWorkFlow>)wkQuery.getResultList();
//					    CashlessWorkFlow wokrflowObj = null;  
//					    if(wk_list != null && !wk_list.isEmpty()){
//					    	wokrflowObj = wk_list.get(0);
//					    }
//					    if(wokrflowObj != null){
//					    	pendingTaskList.add(wokrflowObj);
//					    }						
						
					    String batchId = "";
					    int remCount = 0;
					     
					    DBCalculationService dbCalService = new DBCalculationService();
				
					    List<SearchGenerateReminderBulkTableDTO> tableListDTO = new ArrayList<SearchGenerateReminderBulkTableDTO>();
	//				    if(taskParamObjList != null && !taskParamObjList.isEmpty()){
					  //TODO
					    try{
					    
					    if(pendingTaskList != null && !pendingTaskList.isEmpty()){
					    	
					    	utx.setTransactionTimeout(360000);
							utx.begin();
					    	totalRecords = pendingTaskList.size();
					    	List<Hospitals> hospitalList = new ArrayList<Hospitals>();
					    	WeakHashMap<Long,Object> hospMap = new WeakHashMap<Long,Object>();
//							batchId = dbCalService.generateReminderBatchId(SHAConstants.REMINDER_BATCH_SEQUENCE_NAME);
//							batchId = batchId != null && !("").equals(batchId) ? SHAConstants.BULK_REMINDER + batchId : "";
//							bulkSearchresultDto.setBatchid(batchId);
							
							
	//						for(Map<String, Object> taskObj : taskParamObjList)
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
										
											Date remDate = taskObj.getProcessedDate();
											if ((SHAConstants.FIRST_REMINDER).equalsIgnoreCase(searchTableDto
													.getReminderType())) {
												claimDto.setFirstReminderDate(remDate);
												remCount = 1;
											}
											if ((SHAConstants.SECOND_REMINDER).equalsIgnoreCase(searchTableDto
													.getReminderType())) {
												claimDto.setSecondReminderDate(remDate);
												remCount = 2;
											}
											if ((SHAConstants.CLOSE).equalsIgnoreCase(searchTableDto
													.getReminderType())) {
												claimDto.setFirstReminderDate(claimObj.getFirstReminderDate());
												claimDto.setSecondReminderDate(claimObj.getSecondReminderDate());
												claimDto.setThirdReminderDate(remDate);
												remCount = 3;
											}
										claimDto.setReminderCount(remCount);
										searchTableDto.setIntimationKey(claimObj.getIntimation().getKey());
										
										if (claimObj.getIntimation().getPolicy().getHomeOfficeCode() != null) {
											 List<MasOmbudsman> ombudsmanOfficeList = getOmbudsmanOffiAddrByPIOCode(claimObj.getIntimation().getPolicy().getHomeOfficeCode(),masterService);
											 if(ombudsmanOfficeList !=null && !ombudsmanOfficeList.isEmpty())
												 claimDto.setOmbudsManAddressList(ombudsmanOfficeList);
										 }
										searchTableDto.setClaimDto(claimDto);
										
//										Query claimObjectQuery = entityManager
//												.createNamedQuery("StageInformation.findClaimByStatus");
//										claimObjectQuery.setParameter("claimkey",
//												claimKey);
//										claimObjectQuery.setParameter("stageKey",
//												ReferenceTable.PREAUTH_STAGE);
//										claimObjectQuery
//												.setParameter(
//														"statusKey",
//														ReferenceTable.PREAUTH_APPROVE_STATUS);
//				
//										preauthStageList = claimObjectQuery
//												.getResultList();
//				
//										claimObjectQuery = entityManager
//												.createNamedQuery("StageInformation.findClaimByStatus");
//										claimObjectQuery.setParameter("claimkey",
//												claimKey);
//										claimObjectQuery.setParameter("stageKey",
//												ReferenceTable.ENHANCEMENT_STAGE);
//										claimObjectQuery
//												.setParameter(
//														"statusKey",
//														ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
//				
//										enhancementStageList = claimObjectQuery
//												.getResultList();	
//										
//										StageInformation preauthStageObj = null;
//										if (enhancementStageList != null
//												&& !enhancementStageList
//														.isEmpty()) {
//											preauthStageObj = enhancementStageList
//													.get(0);
//										} else {
//											if (preauthStageList != null
//													&& !preauthStageList
//															.isEmpty()) {
//												preauthStageObj = preauthStageList
//														.get(0);
//											}
//										}
																				
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
										
										Hospitals hospObj = getHospitalById(claimObj.getIntimation().getHospital());
										hospitalList.add(hospObj);	
										hospMap.put(claimObj.getKey(),hospObj);
										
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
								Hospitals hospitalObj = null;
								bulkSearchresultDto.setTotalNoofRecords(totalListSize);
								
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
										
//										Date remDate = new Date();
										if(resultDto.getReminderType() != null){
											if (resultDto.getReminderType().equalsIgnoreCase(SHAConstants.FIRST_REMINDER)) {
												queryDto.setFirstReminderDate(resultDto.getClaimDto().getFirstReminderDate());
												qRemCount = 1;
											}
											if ((SHAConstants.SECOND_REMINDER).equalsIgnoreCase(resultDto.getReminderType())) {
												queryDto.setSecondReminderDate(resultDto.getClaimDto().getSecondReminderDate());
												qRemCount = 2;
											}
											if ((SHAConstants.CLOSE).equalsIgnoreCase(resultDto.getReminderType())) {
												queryDto.setThirdReminderDate(resultDto.getClaimDto().getThirdReminderDate());
												qRemCount = 3;
											}
											queryDto.setReminderCount(qRemCount);
											queryDto.getReimbursementDto().getClaimDto().setOmbudsManAddressList(resultDto.getClaimDto().getOmbudsManAddressList());
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
											
											if(ReferenceTable.getGMCProductList().containsKey(intimationObj.getPolicy().getProduct().getKey())){
											      Insured insuredByKey = getInsuredByKey(intimationObj.getInsured().getKey());
											      Insured MainMemberInsured = null;
											      
											      if(insuredByKey.getDependentRiskId() == null){
											    	  MainMemberInsured = insuredByKey;
											      }else{
											    	  Insured insuredByPolicyAndInsuredId = getInsuredByPolicyAndInsuredId(intimationObj.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId());
											    	  MainMemberInsured = insuredByPolicyAndInsuredId;
											      }
											      
											      if(MainMemberInsured != null){
											    	  intimationDto.setGmcMainMemberName(MainMemberInsured.getInsuredName());
											    	  intimationDto.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());
											    	  if(MainMemberInsured.getInsuredAge() != null){
											    		  intimationDto.setInsuredAge(MainMemberInsured.getInsuredAge().toString());
											    	  }
											    	  
											    	  /**
											    	   * Part of CR R1186
											    	   */
											    	  
											    	  intimationDto.getInsuredPatient().setAddress1(MainMemberInsured.getAddress1());
											    	  intimationDto.getInsuredPatient().setAddress2(MainMemberInsured.getAddress2());
											    	  intimationDto.getInsuredPatient().setAddress3(MainMemberInsured.getAddress3());
											    	  intimationDto.getInsuredPatient().setCity(MainMemberInsured.getCity());
											    	  
											    	  //IMSSUPPOR-27461
											    	  intimationDto.getInsuredPatient().setInsuredPinCode(MainMemberInsured.getInsuredPinCode());
                                                      intimationDto.getInsuredPatient().setInsuredState(MainMemberInsured.getInsuredState());
											      }
											}											
											
											OrganaizationUnit orgUnit = getOrgUnitByCode(intimationObj.getPolicy().getHomeOfficeCode());
											
											if(orgUnit != null){
												intimationDto.setOrganizationUnit(orgUnit);
												if(orgUnit.getParentOrgUnitKey() != null){
													OrganaizationUnit parentUnit = 	getOrgUnitByCode(orgUnit.getParentOrgUnitKey().toString());	
													intimationDto.setParentOrgUnit(parentUnit);
												}
											}
											
//											Hospitals hospitalObj = getHospitalById(intimationObj.getHospital());
											hospitalObj = (Hospitals)hospMap.get(resultDto.getClaimDto().getKey());
											
											if(hospitalObj != null){
												HospitalDto hospDto = new HospitalDto(hospitalObj);
												intimationDto.setHospitalDto(hospDto);
											}
										if(resultDto.getClaimDto() != null){
											resultDto.getClaimDto().setNewIntimationDto(intimationDto);	
										}
										if(resultDto.getReimbQueryDto() != null && resultDto.getReimbQueryDto().getReimbursementDto() != null && resultDto.getReimbQueryDto().getReimbursementDto() != null && resultDto.getReimbQueryDto().getReimbursementDto().getClaimDto() != null)
												resultDto.getReimbQueryDto().getReimbursementDto().getClaimDto().setNewIntimationDto(intimationDto);
										}
										
										String fileUrl = generatePdfFile(resultDto);
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
										summaryBatchObj.setReminderCount(remCount);
										summaryBatchObj.setPrintFlag("N");
										summaryBatchObj.setBatchFlag("Y");
										summaryBatchObj.setReimainderCategory(SHAUtils.getCurrentRemQueName(curQ));
										summaryBatchObj.setPrintCount(totalListSize);
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
		log.info("********************  BULK REMINDER LETTER ENDED********************************");
//		utx.commit();
	}
	catch(Exception e){
		e.printStackTrace();		
	 }
	}

	@SuppressWarnings("unchecked")
	public void searchNGeneratePANCardReminderLetter() {

		try {
			
			log.info("********************  PAN REMINDER LETTER STARTED ********************************");

			Query workFlowQuery = entityManager
					.createNamedQuery("CashlessWorkFlow.findPendingPANReminderLetter");

			List<String> queList = new ArrayList<String>();
			queList.add(SHAConstants.FIRST_REMINDER_LETTER_CURRENT_QUEUE);
			queList.add(SHAConstants.SECOND_REMINDER_LETTER_CURRENT_QUEUE);
			queList.add(SHAConstants.THIRD_REMINDER_LETTER_CURRENT_QUEUE);
			Path tempDir = SHAFileUtils.createTempDirectory("PanCardDummyHolder");
			List<SelectValue> cpuCodeList = (new MasterService())
					.getTmpCpuCodeListWithoutDescription(entityManager);
			
			for (String curQ : queList) {
				workFlowQuery.setParameter("curQ", curQ);
				
				BulkReminderResultDto bulkSearchresultDto = new BulkReminderResultDto();

				// List<String> clmTypeList = new ArrayList<String>();
				// clmTypeList.add(SHAConstants.CASHLESS_CLAIM_TYPE);
				// clmTypeList.add(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);

				// List<String> remCategory = new ArrayList<String>();
				// remCategory.add(SHAConstants.BILLS_NOT_RECEIVED);
				// remCategory.add(SHAConstants.PREAUTH_BILLS_NOT_RECEIVED);
				// remCategory.add(SHAConstants.QUERY);
				
				List<File> filelistForMerge = null;
				List<CashlessWorkFlow> pendingTaskList = new ArrayList<CashlessWorkFlow>();
				// for(String claimType : clmTypeList){
				// for(String categ : remCategory){
				for (SelectValue cpuCode : cpuCodeList) {
					Integer totalRecords = 0;
					workFlowQuery.setParameter("clmType",
							SHAConstants.CASHLESS_CLAIM_TYPE.toLowerCase());
					workFlowQuery.setParameter("remCateg",
							SHAConstants.PAN_CARD.toLowerCase());

					workFlowQuery.setParameter("cpuCode",
							Long.valueOf(cpuCode.getValue()));

					pendingTaskList = workFlowQuery.getResultList();
					String batchId = "";
					int remCount = 0;

					DBCalculationService dbCalService = new DBCalculationService();

					List<SearchGenerateReminderBulkTableDTO> tableListDTO = new ArrayList<SearchGenerateReminderBulkTableDTO>();
					try {

						if (pendingTaskList != null
								&& !pendingTaskList.isEmpty()) {

							utx.setTransactionTimeout(360000);
							utx.begin();
							totalRecords = pendingTaskList.size();

							for (CashlessWorkFlow taskObj : pendingTaskList) {
								SearchGenerateReminderBulkTableDTO searchTableDto = new SearchGenerateReminderBulkTableDTO();
								ClaimDto claimDto = new ClaimDto();
								searchTableDto.setDbOutArray(taskObj);
								List<StageInformation> rodStageList = null;
								Long claimKey = null;
								Long cashlessKey = null;
								List<StageInformation> preauthStageList = null;
								List<StageInformation> enhancementStageList = null;

								if (taskObj.getRemType() != null) {
									String remType = taskObj.getRemType();
									searchTableDto.setReminderType(remType);
								}

								if (taskObj.getClaimKey() != null
										&& taskObj.getClaimKey().intValue() != 0) {
									claimKey = taskObj.getClaimKey();
								}

								cashlessKey = taskObj.getCashlessKey();

								if (claimKey != null) {
									Claim claimObj = getClaimByClaimKey(claimKey);
									if (claimObj != null) {

										claimDto = ClaimMapper.getInstance()
												.getClaimDto(claimObj);
										claimDto.setModifiedDate(claimObj
												.getModifiedDate());

										searchTableDto
												.setClaimIntimationNo(claimObj
														.getIntimation()
														.getIntimationId() != null ? claimObj
														.getIntimation()
														.getIntimationId() : "");
										searchTableDto
												.setIntimationDate(claimObj
														.getIntimation()
														.getCreatedDate() != null ? new SimpleDateFormat(
														"dd/MM/yyyy")
														.format(claimObj
																.getIntimation()
																.getCreatedDate())
														: "");
										searchTableDto
												.setPatientName(claimObj
														.getIntimation()
														.getInsured()
														.getInsuredName() != null ? claimObj
														.getIntimation()
														.getInsured()
														.getInsuredName()
														: "");
										searchTableDto.setClaimDto(claimDto);

										Date remDate = new Date();
										if ((SHAConstants.FIRST_REMINDER)
												.equalsIgnoreCase(searchTableDto
														.getReminderType())) {
											claimDto.setCreatedDate(taskObj.getProcessedDate());
											remCount = 1;
										}
										if ((SHAConstants.SECOND_REMINDER)
												.equalsIgnoreCase(searchTableDto
														.getReminderType())) {
											claimDto.setFirstReminderDate(taskObj.getProcessedDate());
											remCount = 2;
										}
										if ((SHAConstants.CLOSE)
												.equalsIgnoreCase(searchTableDto
														.getReminderType())) {
											claimDto.setThirdReminderDate(taskObj.getProcessedDate());											
											remCount = 3;
										}
										claimDto.setReminderCount(remCount);
										searchTableDto
												.setIntimationKey(claimObj
														.getIntimation()
														.getKey());
										Preauth preauthObj = getLatestPreauthByClaimKey(claimObj.getKey());
										if (preauthObj != null) {
//											claimDto.setPreauthApprovedDate(claimObj.getModifiedDate());
//											searchTableDto
//													.getClaimDto()
//													.setPreauthApprovedDate(claimObj.getModifiedDate());
											
											Date panReminDate = getPanReminderDate(claimObj.getIntimation().getIntimationId(),2,SHAConstants.PAN_CARD);
											
											searchTableDto
											.getClaimDto().setFirstReminderDate(getPanReminderDate(claimObj.getIntimation().getIntimationId(),1,SHAConstants.PAN_CARD));
											
											searchTableDto
											.getClaimDto()
											.setPreauthApprovedDate(panReminDate);
											claimDto.setPreauthApprovedDate(panReminDate);
											claimDto
											.setFirstReminderDate(searchTableDto
													.getClaimDto().getFirstReminderDate());
											
											searchTableDto
													.setCategory(SHAConstants.PAN_CARD);
											Long preauthKey = null; 
											if(preauthObj.getKey() != null){
												preauthKey = preauthObj.getKey();			
											searchTableDto
													.getClaimDto()
													.setProvisionAmount(preauthObj
																	.getTotalApprovalAmount());
											}
											searchTableDto
													.setPreauthKey(preauthKey);
											bulkSearchresultDto
													.setCpuCode(String
															.valueOf(preauthObj
																	.getIntimation()
																	.getCpuCode()
																	.getCpuCode()));
										}

//										Query claimObjectQuery = entityManager
//												.createNamedQuery("StageInformation.findClaimByStatus");
//										claimObjectQuery.setParameter(
//												"claimkey", claimKey);
//										claimObjectQuery.setParameter(
//												"stageKey",
//												ReferenceTable.PREAUTH_STAGE);
//										claimObjectQuery
//												.setParameter(
//														"statusKey",
//														ReferenceTable.PREAUTH_APPROVE_STATUS);
//
//										preauthStageList = claimObjectQuery
//												.getResultList();
//
//										claimObjectQuery = entityManager
//												.createNamedQuery("StageInformation.findClaimByStatus");
//										claimObjectQuery.setParameter(
//												"claimkey", claimKey);
//										claimObjectQuery
//												.setParameter(
//														"stageKey",
//														ReferenceTable.ENHANCEMENT_STAGE);
//										claimObjectQuery
//												.setParameter(
//														"statusKey",
//														ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
//
//										enhancementStageList = claimObjectQuery
//												.getResultList();
//
//										StageInformation preauthStageObj = null;
//										if (enhancementStageList != null
//												&& !enhancementStageList
//														.isEmpty()) {
//											preauthStageObj = enhancementStageList
//													.get(0);
//										} else {
//											if (preauthStageList != null
//													&& !preauthStageList
//															.isEmpty()) {
//												preauthStageObj = preauthStageList
//														.get(0);
//											}
//										}
//										if (preauthStageObj != null) {
//											entityManager
//													.refresh(preauthStageObj);
//											claimDto.setPreauthApprovedDate(preauthStageObj
//													.getCreatedDate());
//											searchTableDto
//													.getClaimDto()
//													.setPreauthApprovedDate(
//															preauthStageObj
//																	.getCreatedDate());
//											searchTableDto
//													.setCategory(SHAConstants.PAN_CARD);
//											Long preauthKey = null; 
//											if(preauthStageObj.getPreauth() != null){
//												preauthKey = preauthStageObj
//																.getPreauth().getKey();			
//											searchTableDto
//													.getClaimDto()
//													.setProvisionAmount(
//															preauthStageObj
//																	.getPreauth()
//																	.getTotalApprovalAmount());
//											}
//											searchTableDto
//													.setPreauthKey(preauthKey);
//											bulkSearchresultDto
//													.setCpuCode(String
//															.valueOf(preauthStageObj
//																	.getIntimation()
//																	.getCpuCode()
//																	.getCpuCode()));
//										}
										tableListDTO.add(searchTableDto);
										if (SHAConstants.THIRD_REMINDER_LETTER_CURRENT_QUEUE
												.equalsIgnoreCase(curQ)) {
											DBCalculationService dbService = new DBCalculationService();
											dbService
													.stopReminderProcessProcedure(claimObj
															.getIntimation()
															.getIntimationId(), SHAConstants.OTHERS_DOC_TYPE);
										}
									}
								}
							}
							bulkSearchresultDto.setReminderCount(remCount);
							List<SearchGenerateReminderBulkTableDTO> resultListDto = new ArrayList<SearchGenerateReminderBulkTableDTO>();
							if (!tableListDTO.isEmpty()) {
								batchId = dbCalService
										.generateReminderBatchId(SHAConstants.REMINDER_BATCH_SEQUENCE_NAME);
								batchId = batchId != null
										&& !("").equals(batchId) ? SHAConstants.BULK_REMINDER
										+ batchId
										: "";

								bulkSearchresultDto.setBatchid(batchId);
								filelistForMerge = new ArrayList<File>();

								for (SearchGenerateReminderBulkTableDTO resultDto : tableListDTO) {
									ReimbursementQueryDto queryDto = null;

									resultDto.setBatchid(batchId);
									resultDto.setSubBatchId(batchId);

									queryDto = new ReimbursementQueryDto();
									ReimbursementDto reimbursementDto = new ReimbursementDto();
									reimbursementDto.setClaimDto(resultDto
											.getClaimDto());
									queryDto.setReimbursementDto(reimbursementDto);

									resultDto.setReimbQueryDto(queryDto);
									resultDto.setSno(String
											.valueOf(tableListDTO
													.indexOf(resultDto) + 1));
									resultDto
											.setUsername(BPMClientContext.DB_USER_NAME);
									resultListDto.add(resultDto);

									if (resultDto.getIntimationKey() != null) {
										Intimation intimationObj = getIntimationByKey(resultDto
												.getIntimationKey());
										if (intimationObj != null) {
											NewIntimationMapper intimationMapper = NewIntimationMapper
													.getInstance();
											NewIntimationDto intimationDto = intimationMapper
													.getNewIntimationDto(intimationObj);											
											
											TmpCPUCode cpuObject = intimationObj.getCpuCode();

											if (null != cpuObject) {
												intimationDto.setReimbCpuAddress(cpuObject.getReimbAddress());
											}
											
											if(ReferenceTable.getGMCProductList().containsKey(intimationObj.getPolicy().getProduct().getKey())){
											      Insured insuredByKey = getInsuredByKey(intimationObj.getInsured().getKey());
											      Insured MainMemberInsured = null;
											      
											      if(insuredByKey.getDependentRiskId() == null){
											    	  MainMemberInsured = insuredByKey;
											      }else{
											    	  Insured insuredByPolicyAndInsuredId = getInsuredByPolicyAndInsuredId(intimationObj.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId());
											    	  MainMemberInsured = insuredByPolicyAndInsuredId;
											      }
											      
											      if(MainMemberInsured != null){
											    	  intimationDto.setGmcMainMemberName(MainMemberInsured.getInsuredName());
											    	  intimationDto.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());
											    	  if(MainMemberInsured.getInsuredAge() != null){
											    		  intimationDto.setInsuredAge(MainMemberInsured.getInsuredAge().toString());
											    	  }
											    	  
											    	  /**
											    	   * Part of CR R1186
											    	   */
											    	  
											    	  intimationDto.getInsuredPatient().setAddress1(MainMemberInsured.getAddress1());
											    	  intimationDto.getInsuredPatient().setAddress2(MainMemberInsured.getAddress2());
											    	  intimationDto.getInsuredPatient().setAddress3(MainMemberInsured.getAddress3());
											    	  intimationDto.getInsuredPatient().setCity(MainMemberInsured.getCity());
											    	  
											      }
											}
											
											OrganaizationUnit orgUnit = getOrgUnitByCode(intimationObj
													.getPolicy()
													.getHomeOfficeCode());

											if (orgUnit != null) {
												intimationDto
														.setOrganizationUnit(orgUnit);
											}

											Hospitals hospitalObj = getHospitalById(intimationObj
													.getHospital());
											if (hospitalObj != null) {
												HospitalDto hospDto = new HospitalDto(
														hospitalObj);
												intimationDto
														.setHospitalDto(hospDto);
											}
											if (resultDto.getClaimDto() != null) {
												resultDto.getClaimDto()
														.setNewIntimationDto(
																intimationDto);
											}
											if (resultDto.getReimbQueryDto() != null
													&& resultDto
															.getReimbQueryDto()
															.getReimbursementDto() != null
													&& resultDto
															.getReimbQueryDto()
															.getReimbursementDto() != null
													&& resultDto
															.getReimbQueryDto()
															.getReimbursementDto()
															.getClaimDto() != null)
												resultDto
														.getReimbQueryDto()
														.getReimbursementDto()
														.getClaimDto()
														.setNewIntimationDto(
																intimationDto);
										}

										String fileUrl = generatePdfFile(resultDto);
										if(fileUrl != null && !fileUrl.isEmpty())
										{	
											resultDto.setFileUrl(fileUrl);
											resultDto = uploadReminderLetterToDMS(resultDto);
	
											if (resultDto.getDocToken() != null) {
												String remfileUrl = getDocumentURLByToken(resultDto
														.getDocToken());
	
												String[] fileNameSplit = remfileUrl
														.split("\\.");
												String fileName = fileNameSplit.length > 0 ? fileNameSplit[0]
														: "";
	
												String fileviewUrl = SHAFileUtils
														.viewFileByToken(String.valueOf(resultDto
																.getDocToken()));
	
												File tmpFile = SHAFileUtils
														.downloadFileForCombinedView(
																fileName,
																fileviewUrl,
																tempDir);
												if(tmpFile != null){
													filelistForMerge.add(tmpFile);	
												}												
											}
	
											submitReminderDetails(resultDto);
										}
									}

									bulkSearchresultDto
											.setResultListDto(resultListDto);
									bulkSearchresultDto
											.setTotalNoofRecords(totalRecords);
								}

								File mergedDoc = SHAFileUtils.mergeDocuments(
										filelistForMerge, tempDir,
										bulkSearchresultDto.getBatchid());
								
								if(mergedDoc != null){
									String mergedFileUrl = mergedDoc
											.getAbsolutePath();	
								
								// Need to Upload the above Merged Doc to DMS
									WeakHashMap<String, String> dataMap = new WeakHashMap<String, String>();
								dataMap.put("filePath", mergedFileUrl);
								dataMap.put("docType",
										SHAConstants.REMINDER_LETTER);
								dataMap.put("docSources",
										SHAConstants.GENERATE_REMINDER_LETTER);
								dataMap.put("createdBy",
										BPMClientContext.DB_USER_NAME);

								String docToken = SHAUtils
										.uploadGeneratedLetterToDMS(
												entityManager, dataMap);
								SHAUtils.setClearMapStringValue(dataMap);
								bulkSearchresultDto.setDocToken(Long
										.valueOf(docToken));
								}

								if (resultListDto == null
										|| resultListDto.isEmpty()) {
									bulkSearchresultDto.setBatchid(null);
								} else {
									if(filelistForMerge != null && !filelistForMerge.isEmpty()){
									ClaimReminderDetails summaryBatchObj = new ClaimReminderDetails();

									summaryBatchObj.setIntimationKey(null);
									summaryBatchObj
											.setIntimatonNo(bulkSearchresultDto
													.getBatchid());
									summaryBatchObj.setQueryKey(null);
									summaryBatchObj.setTransacKey(null);
									summaryBatchObj
											.setClaimNo(bulkSearchresultDto
													.getBatchid());
									summaryBatchObj.setClaimKey(null);
									summaryBatchObj
											.setBatchNo(bulkSearchresultDto
													.getBatchid());
									summaryBatchObj
											.setSubBatchNo(bulkSearchresultDto
													.getBatchid());
									summaryBatchObj
											.setCategory(SHAConstants.PAN_CARD);
									summaryBatchObj.setCpuCode(Long
											.valueOf(cpuCode.getValue()));
									summaryBatchObj.setReminderCount(remCount);
									summaryBatchObj.setPrintFlag("N");
									summaryBatchObj.setBatchFlag("Y");
									summaryBatchObj
											.setReimainderCategory(SHAUtils
													.getCurrentRemQueNameForPan(curQ));
									summaryBatchObj.setPrintCount(totalRecords);
									summaryBatchObj
											.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);
									summaryBatchObj
											.setDocumentToken(bulkSearchresultDto
													.getDocToken());
									summaryBatchObj.setGeneratedDate(SHAUtils
											.getCalendar(new Date()).getTime());
									summaryBatchObj
											.setCreatedBy(SHAConstants.BATCH_USER_ID);
									summaryBatchObj.setCreatedDate(SHAUtils
											.getCalendar(new Date()).getTime());
									entityManager.persist(summaryBatchObj);
									filelistForMerge = null;
								}
							  }
							}
							utx.commit();
						}
					} catch (Exception e) {
						e.printStackTrace();
						
						try {
							utx.rollback();
						} catch (IllegalStateException | SecurityException
								| SystemException e1) {
							e1.printStackTrace();
						}
					}
				}
				// }
				// }
				// }

				// utx.commit();
			}
			
			log.info("********************  PAN REMINDER LETTER ENDED********************************");
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	public Date getPanReminderDate(String intNumber,int remCount,String remCategory) {
		Query findAll = entityManager.createNamedQuery("ClaimReminderDetails.findByIntimationNoAndCategory").setParameter("intimationNo", intNumber.toLowerCase());
		Date reminDate = null;
		findAll.setParameter("reminderCount", remCount);
		findAll.setParameter("remCategory", remCategory);
		List<ClaimReminderDetails> reminderList = (List<ClaimReminderDetails>)findAll.getResultList();
		if(reminderList != null && !reminderList.isEmpty()) {
			reminDate = reminderList.get(0).getGeneratedDate();
		}
		 return reminDate;
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
	
	
	private void updateReminderPrintStatus(SearchGenerateReminderBulkTableDTO searchDto){

		CashlessWorkFlow currentTaskObj = (CashlessWorkFlow) searchDto.getDbOutArray();
		String query = "UPDATE IMS_CLS_SEC_WORK_FLOW SET REMINDER_LATTER_FLAG = "+"'Y'"+ "WHERE WK_KEY ="+currentTaskObj.getKey();
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		nativeQuery.executeUpdate();		
	}
	
	private String generatePdfFile(SearchGenerateReminderBulkTableDTO reminderLetterDto){
		ReportDto reportDto = new ReportDto();
		DocumentGenerator docGenarator = new DocumentGenerator();
		String fileUrl = "";
		
		if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && (SHAConstants.QUERY).equalsIgnoreCase(reminderLetterDto.getCategory())){
			ReimbursementQueryDto queryDto = reminderLetterDto.getReimbQueryDto();
			List<ReimbursementQueryDto> queryDtoList = new ArrayList<ReimbursementQueryDto>();
			queryDtoList.add(queryDto);	
			reportDto.setBeanList(queryDtoList);
			reportDto.setClaimId(queryDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("ReimburseQueryReminderLetter", reportDto);
		}
		
		else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && (SHAConstants.BILLS_NOT_RECEIVED).equalsIgnoreCase(reminderLetterDto.getCategory())){
			ClaimDto claimDto = reminderLetterDto.getClaimDto();
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			reportDto.setBeanList(claimDtoList);
			reportDto.setClaimId(claimDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("ReimburseClaimReminderLetter", reportDto);
			
		}
		else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && (SHAConstants.PREAUTH_BILLS_NOT_RECEIVED).equalsIgnoreCase(reminderLetterDto.getCategory()) ){
			ClaimDto claimDto = reminderLetterDto.getClaimDto();
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			reportDto.setBeanList(claimDtoList);
			reportDto.setClaimId(claimDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("CashlessReminderLetter", reportDto);
			
		}
		
		else if(reminderLetterDto != null && reminderLetterDto.getCategory() != null && SHAConstants.PAN_CARD.equalsIgnoreCase(reminderLetterDto.getCategory())){
			ClaimDto claimDto = reminderLetterDto.getClaimDto();
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			reportDto.setBeanList(claimDtoList);
			reportDto.setClaimId(claimDto.getClaimId());
			
			fileUrl = docGenarator.generatePdfDocument("PANCardCashlessReminderLetter", reportDto);
			
		}
		
		return fileUrl;
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
	
	public Insured getInsuredByPolicyAndInsuredId(String policyNo , Long insuredId) {
	
		Insured insuredResult = null;
		try{
			Query query = entityManager.createNamedQuery("Insured.findByInsuredIdAndPolicyNoForDefault");
			query = query.setParameter("policyNo", policyNo);
			if(null != insuredId){
				query = query.setParameter("insuredId", insuredId);
				List<Insured> insuredList = query.getResultList();
				insuredList = query.getResultList();
				if(null != insuredList && !insuredList.isEmpty()) {					
					
					if(insuredList.size()>1){
						for (Insured insured : insuredList) {
							if(SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(insured.getLopFlag())){
								insuredResult = insured;
								break;
							}
							else if(insured.getLopFlag() == null || !SHAConstants.PA_LOB_TYPE.equalsIgnoreCase(insured.getLopFlag())){
								insuredResult = insured;
								break;					
							}
						}
						if(insuredResult == null){
							insuredResult = insuredList.get(0);
						}
					}
					else{
						insuredResult = insuredList.get(0);
					}					
				}
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return insuredResult;
	}
	
	public Insured getInsuredByKey(Long key) {

		try{			
			if(key != null){
				Query query = entityManager
						.createNamedQuery("Insured.findByInsured");
				query = query.setParameter("key", key);
				List<Insured> insuredList = (List<Insured>) query.getResultList();
				if (insuredList != null && ! insuredList.isEmpty())
					return insuredList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;

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