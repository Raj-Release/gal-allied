/**
 * 
 */
package com.shaic.reimbursement.queryrejection.generateremainder.search;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimReminderDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Reimbursement;
/**
 * @author ntv.narenj
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SearchGenerateRemainderService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	//private final Logger log = LoggerFactory.getLogger(SearchGenerateRemainderService.class);
	
	public SearchGenerateRemainderService() {
		super();
		
	}
	
@SuppressWarnings("unchecked")
	public Page<SearchGenerateReminderTableDTO> search(
			SearchGenerateRemainderFormDTO searchFormDTO, String userName,
			String passWord) {

	/*		try {
			
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

			PayloadBOType payloadBOType = new PayloadBOType();
			com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType caPayloadBOType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
//			if (cpuCode != null || reminderType != null || !("").equalsIgnoreCase(category)
//					|| claimTypeMasterValue != null) {
//				
//			}
			
			ProductInfoType productInfo = new ProductInfoType();
			productInfo.setLob(ReferenceTable.PA_LOB_KEY.equals(searchFormDTO.getLobId()) ? SHAConstants.PA_LOB : SHAConstants.HEALTH_LOB);
			caPayloadBOType.setProductInfo(productInfo);
			
			com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType productInfoReim = new com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.productinfo.ProductInfoType();
			productInfoReim.setLob(ReferenceTable.PA_LOB_KEY.equals(searchFormDTO.getLobId()) ? SHAConstants.PA_LOB : SHAConstants.HEALTH_LOB);
			payloadBOType.setProductInfo(productInfoReim);
			
			
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
//			/**
//			 * For Query Reminder
//			 */
//			if (category == null || category.equalsIgnoreCase(SHAConstants.ALL)
//					|| category.equalsIgnoreCase(SHAConstants.QUERY)) {
//
//				GenerateReminderLetterTask generateReminderLetterTask = BPMClientContext
//						.getGenerateRemainderLetterTask(userName, passWord);
//				PagedTaskList queryTaskList = generateReminderLetterTask
//						.getTasks(userName, searchFormDTO.getPageable(),
//								payloadBOType);
//				try {
//					totalRecords += queryTaskList.getTotalRecords();
//				} catch (Exception e) {
//					totalRecords = 0;
//				}
//
//				if (queryTaskList != null) {
//					taskList.add(queryTaskList);
//				}
//
//			}
//
//			/**
//			 * For Covering Letter Reminder from Cancel Ack. process and from
//			 * Generate Covering letter process
//			 */
//
//			if (category == null
//					|| category.equalsIgnoreCase(SHAConstants.ALL)
//					|| category
//							.equalsIgnoreCase(SHAConstants.BILLS_NOT_RECEIVED)) {
//
//				GenerateLetterTask generateReimbReminderLetterTask = BPMClientContext
//						.getGenerateReimbRemainderLetterTask(userName, passWord);
//				PagedTaskList reimbReminderTaskList = generateReimbReminderLetterTask
//						.getTasks(userName, searchFormDTO.getPageable(),
//								payloadBOType);
//				try {
//					totalRecords += reimbReminderTaskList.getTotalRecords();
//				} catch (Exception e) {
//					totalRecords = 0;
//				}
//
//				if (reimbReminderTaskList != null) {
//					taskList.add(reimbReminderTaskList);
//				}
//
//			}
//
//			/**
//			 * For Covering Letter Reminder from Preauth(Final Approval) and
//			 * Final Enhancement Approval process
//			 */
//
//			if (category == null
//					|| category.equalsIgnoreCase(SHAConstants.ALL)
//					|| category
//							.equalsIgnoreCase(SHAConstants.PREAUTH_BILLS_NOT_RECEIVED)) {
//
//				CLGenerateLetterTask generateBillsNotReceivedReminderLetterTask = BPMClientContext
//						.getCashlessRemainderLetterTask(userName, passWord);
//				PagedTaskList billsNotReceivedTaskList = generateBillsNotReceivedReminderLetterTask
//						.getTasks(userName, searchFormDTO.getPageable(),
//								caPayloadBOType);
//				try {
//					totalRecords += billsNotReceivedTaskList.getTotalRecords();
//				} catch (Exception e) {
//					totalRecords = 0;
//				}
//				taskList.add(billsNotReceivedTaskList);
//			}
			
		/*	Pageable aPage = new Pageable();
			aPage.setPageSize(10);
			switch(option)
			{
			 case SHAConstants.CASHLESS_CLAIM_TYPE : int itemIndex = 51;
//													while(itemIndex<=1500){
//														aPage.setPageNumber(itemIndex);	
				 										CLGenerateLetterTask generateBillsNotReceivedReminderLetterTask = BPMClientContext
					 											.getCashlessRemainderLetterTask(userName, passWord);
															PagedTaskList billsNotReceivedTaskList = generateBillsNotReceivedReminderLetterTask
																.getTasks(userName, aPage, caPayloadBOType);
															
															if(!billsNotReceivedTaskList.getHumanTasks().isEmpty()){
																billsNotReceivedTaskList = validateTasks(billsNotReceivedTaskList,userName,passWord);
																
																PagedTaskList dummypagedTask = billsNotReceivedTaskList; 
																
																if(billsNotReceivedTaskList.getHumanTasks().size()<10){
																	int pageNo = 2;
																do{
																	aPage.setPageNumber(pageNo);
																	dummypagedTask = generateBillsNotReceivedReminderLetterTask
																			.getTasks(userName, aPage, caPayloadBOType);
																	
																	dummypagedTask = validateTasks(dummypagedTask,userName,passWord);
																	
																	if(dummypagedTask.getHumanTasks().size() > 0){
																		int i = 0;
																		for(int index = billsNotReceivedTaskList.getHumanTasks().size(); index <= 10 && i < dummypagedTask.getHumanTasks().size();index++){
																			billsNotReceivedTaskList.getHumanTasks().add(dummypagedTask.getHumanTasks().get(i));
																			i++;
																		}
																	}
																	billsNotReceivedTaskList.setTotalRecords(dummypagedTask.getTotalRecords());
																	pageNo++;
																  }while(billsNotReceivedTaskList.getHumanTasks().size() < 10 && dummypagedTask.getHumanTasks().size() > 0);
																}
															}
															
															try {
																	totalRecords += billsNotReceivedTaskList.getTotalRecords();
															} catch (Exception e) {
																	totalRecords = 0;
															}
//															if(billsNotReceivedTaskList != null && billsNotReceivedTaskList.getHumanTasks() != null && !billsNotReceivedTaskList.getHumanTasks().isEmpty()){
//													   			updateReminderCount(billsNotReceivedTaskList.getHumanTasks(), userName, passWord);
//													   		}
															if(billsNotReceivedTaskList != null && !billsNotReceivedTaskList.getHumanTasks().isEmpty()){
																taskList.add(billsNotReceivedTaskList);
															}
//															System.out.println("Completed Processing of Page :"+itemIndex);
//															itemIndex++;
//													}
															
															if((SHAConstants.PREAUTH_BILLS_NOT_RECEIVED).equalsIgnoreCase(category)){
																break;
															}
																
			 case SHAConstants.REIMBURSEMENT_CLAIM_TYPE : if((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(option) || (SHAConstants.BILLS_NOT_RECEIVED).equalsIgnoreCase(category) || (SHAConstants.ALL).equalsIgnoreCase(category)){
//				 										 int i =1;
//	 										 	while(i<900){
//				 										aPage.setPageNumber(i);
														GenerateLetterTask generateReimbReminderLetterTask = BPMClientContext
															.getGenerateReimbRemainderLetterTask(userName, passWord);
														PagedTaskList reimbReminderTaskList = generateReimbReminderLetterTask
												   			.getTasks(userName, aPage, payloadBOType);
												   		
												   		if(!reimbReminderTaskList.getHumanTasks().isEmpty()){
												   			reimbReminderTaskList = validateTasks(reimbReminderTaskList,userName,passWord);
												   			
												   			PagedTaskList dummypagedTask = reimbReminderTaskList; 

															if(reimbReminderTaskList.getHumanTasks().size()<10){
																int pageNo = 2;
															do{       
																	  aPage.setPageNumber(pageNo);
																	  dummypagedTask = generateReimbReminderLetterTask
																		.getTasks(userName, aPage, payloadBOType);
																
																dummypagedTask = validateTasks(dummypagedTask,userName,passWord);
																
																if(dummypagedTask.getHumanTasks().size() > 0){
																	int i = 0;
																	for(int index = reimbReminderTaskList.getHumanTasks().size(); index <= 10 && i < dummypagedTask.getHumanTasks().size();index++){
																		reimbReminderTaskList.getHumanTasks().add(dummypagedTask.getHumanTasks().get(i));
																		i++;
																	}
																}
																reimbReminderTaskList.setTotalRecords(dummypagedTask.getTotalRecords());
																pageNo++;
															  }while(reimbReminderTaskList.getHumanTasks().size() < 10 && dummypagedTask.getHumanTasks().size() > 0);
															}
												   		}
//												   		if(reimbReminderTaskList != null && reimbReminderTaskList.getHumanTasks() != null && !reimbReminderTaskList.getHumanTasks().isEmpty()){
//												   			updateReminderCount(reimbReminderTaskList.getHumanTasks(), userName, passWord);
//												   		}
												   														   		
												   		try {
												   				totalRecords += reimbReminderTaskList.getTotalRecords();
												   		} catch (Exception e) {
												   			totalRecords = 0;
												   		}

												   		if (reimbReminderTaskList != null && !reimbReminderTaskList.getHumanTasks().isEmpty()) {
												   			taskList.add(reimbReminderTaskList);
												   		}
//				 								i++;	
//				 								}
												   		if((SHAConstants.BILLS_NOT_RECEIVED).equalsIgnoreCase(category) ){
												   			break;					
												   		}
												   		}
			 case SHAConstants.QUERY : if(((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(option) && (SHAConstants.QUERY).equalsIgnoreCase(category)) || ((SHAConstants.REIMBURSEMENT_CLAIM_TYPE).equalsIgnoreCase(option) && (SHAConstants.QUERY).equalsIgnoreCase(category)) || (SHAConstants.QUERY).equalsIgnoreCase(category) || (SHAConstants.ALL).equalsIgnoreCase(category) ){
										
//											int itemIndex1 =1;
//											while(itemIndex1<21){
//											aPage.setPageNumber(itemIndex1);		
											GenerateReminderLetterTask generateReminderLetterTask = BPMClientContext
													.getGenerateRemainderLetterTask(userName, passWord);
											PagedTaskList queryTaskList = generateReminderLetterTask
													.getTasks(userName, aPage, payloadBOType);
									  
											if(!queryTaskList.getHumanTasks().isEmpty()){
												queryTaskList = validateTasks(queryTaskList,userName,passWord);
												
												PagedTaskList dummypagedTask = queryTaskList; 
												
												if(queryTaskList.getHumanTasks().size()<10){
													 int pageNo = 2;
													do{
														aPage.setPageNumber(pageNo);
														dummypagedTask = generateReminderLetterTask
																.getTasks(userName, aPage, payloadBOType);
														
														dummypagedTask = validateTasks(dummypagedTask,userName,passWord);
														
														if(dummypagedTask.getHumanTasks().size() > 0){
															int i = 0;
															for(int index = queryTaskList.getHumanTasks().size(); index <= 10 && i < dummypagedTask.getHumanTasks().size();index++){
																queryTaskList.getHumanTasks().add(dummypagedTask.getHumanTasks().get(i));
																i++;
															}
														}
														queryTaskList.setTotalRecords(dummypagedTask.getTotalRecords());
														pageNo++;
													}while(queryTaskList.getHumanTasks().size()<10 && dummypagedTask.getHumanTasks().size() > 0);
												}											
											}
											try {
												totalRecords += queryTaskList.getTotalRecords();
											} catch (Exception e) {
												totalRecords = 0;
											}

											if (queryTaskList != null && !queryTaskList.getHumanTasks().isEmpty()) {
												taskList.add(queryTaskList);
											}	
//											itemIndex1++;
//											}
											}
									  	break;	
			
			default : //CLGenerateLetterTask clBillsNotReceivedReminderLetterTask = BPMClientContext
//						.getCashlessRemainderLetterTask(userName, passWord);
//					PagedTaskList clBillsNotReceivedTaskList = clBillsNotReceivedReminderLetterTask
//						.getTasks(userName, null, caPayloadBOType);
//					clBillsNotReceivedTaskList = validateTasks(clBillsNotReceivedTaskList,userName, passWord);
//					try {
//						totalRecords += clBillsNotReceivedTaskList.getTotalRecords();
//					} catch (Exception e) {
//						totalRecords = 0;
//					}
//					if(clBillsNotReceivedTaskList != null ){
//					
//						taskList.add(clBillsNotReceivedTaskList);
//					}
//					GenerateLetterTask reimbReminderLetterTask = BPMClientContext
//						.getGenerateReimbRemainderLetterTask(userName, passWord);
//					PagedTaskList reimbReminderLetterTaskList = reimbReminderLetterTask
//						.getTasks(userName, null, payloadBOType);
//					reimbReminderLetterTaskList = validateTasks(reimbReminderLetterTaskList,userName, passWord);
//					try {
//						totalRecords += reimbReminderLetterTaskList.getTotalRecords();
//					} catch (Exception e) {
//						totalRecords = 0;
//					}
//
//					if (reimbReminderLetterTaskList != null) {
//						taskList.add(reimbReminderLetterTaskList);
//					}
//					GenerateReminderLetterTask queryReminderLetterTask = BPMClientContext
//						.getGenerateRemainderLetterTask(userName, passWord);
//					PagedTaskList queryLetterTaskList = queryReminderLetterTask
//						.getTasks(userName, null, payloadBOType);
//					queryLetterTaskList = validateTasks(queryLetterTaskList,userName, passWord);
//					try {
//						totalRecords += queryLetterTaskList.getTotalRecords();
//					} catch (Exception e) {
//						totalRecords = 0;
//					}
//
//					if (queryLetterTaskList != null) {
//						taskList.add(queryLetterTaskList);
//					}			
			
			}	

			List<SearchGenerateReminderTableDTO> tableListDTO = new ArrayList<SearchGenerateReminderTableDTO>();
			if (taskList != null && !taskList.isEmpty()) {

				for (PagedTaskList pagedTask : taskList) {
					
					if(pagedTask.getHumanTasks().size() > 0)
					{
					List<HumanTask> humanTaskList = pagedTask.getHumanTasks();
					for (HumanTask humanTask : humanTaskList) {
						SearchGenerateReminderTableDTO searchTableDto = new SearchGenerateReminderTableDTO();
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
							Claim claimObj = (new ClaimService())
									.getClaimByClaimKey(claimKey,
											entityManager);
							
							if(claimObj != null){
								claimDto =  ClaimMapper.getInstance().getClaimDto(claimObj);
								claimDto.setModifiedDate(claimObj.getModifiedDate());
							Long hospKey = claimObj.getIntimation().getHospital();
							
							Hospitals hospitalObj = getHospitalDetailsByKey(hospKey);
							
							
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
							if(hospitalObj != null){
								searchTableDto.setHospitalName(hospitalObj.getName());
								searchTableDto.setHospitalAddress(hospitalObj.getAddress());
							}
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
												
//							Query rodObjectQuery = entityManager
//									.createNamedQuery("StageInformation.findClaimByStatus");
//							rodObjectQuery.setParameter("claimkey",
//									claimKey);
//							rodObjectQuery.setParameter("stageKey",
//									ReferenceTable.CREATE_ROD_STAGE_KEY);
//							rodObjectQuery.setParameter("statusKey",
//									ReferenceTable.CREATE_ROD_STATUS_KEY);
//
//							rodStageList = (List<StageInformation>) rodObjectQuery
//									.getResultList();

							if (humanTask.getPayload() != null && humanTask
										.getPayload().getQuery().getKey() != null){								
								
									searchTableDto.setQueryKey(humanTask
										.getPayload().getQuery().getKey());
									searchTableDto.setCategory(SHAConstants.QUERY);
							}		
//									For Testing Purpose   
//									searchTableDto.setQueryKey(10000054l);									
									tableListDTO.add(searchTableDto);
									
//								if (searchTableDto.getQueryKey() != null ) {
//									
//									Query queryReplyObjectq = entityManager
//											.createNamedQuery("ReimbursementQuery.findByKey");
//									queryReplyObjectq.setParameter("primaryKey",
//											searchTableDto.getQueryKey());
//									List<ReimbursementQuery> queryList = (List<ReimbursementQuery>)queryReplyObjectq.getResultList();
//									
//									if(queryList != null && !queryList.isEmpty()){
//										ReimbursementQuery queryReplyObject = queryList.get(0);
//										entityManager.refresh(queryReplyObject);
//										
//										if(queryReplyObject.getQueryReply() == null)
//										searchTableDto
//										.setCategory(SHAConstants.QUERY);
//									tableListDTO.add(searchTableDto);
//									}
//									else{
//										SubmitGenerateReminderLetterTask submitTask = BPMClientContext.getSubmitQueryReimnderLetterTask(userName, passWord);
//										humanTask.setOutcome("SUBMIT");
//										submitTask.execute(userName, humanTask);
//										
//									}	
									
//								}
//															
//								
//							}
//							
//							else if (rodStageList == null || (rodStageList != null && rodStageList.isEmpty())) {
//								tableListDTO.add(searchTableDto);
//							}

						}
					}
					}
					
				 }
				}

			}
			Page<SearchGenerateReminderTableDTO> page = new Page<SearchGenerateReminderTableDTO>();

			List<SearchGenerateReminderTableDTO> resultListDto = new ArrayList<SearchGenerateReminderTableDTO>();

			if (!tableListDTO.isEmpty()) {

				for (SearchGenerateReminderTableDTO resultDto : tableListDTO) {
					ReimbursementQueryDto queryDto = null;

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
						if (resultDto.getReminderType().equals("1")) {
							queryDto.setFirstReminderDate(remDate);
						}
						if (resultDto.getReminderType().equals("2")) {
							queryDto.setSecondReminderDate(new Date());
						}
						if (resultDto.getReminderType().equals("3")) {
							queryDto.setThirdReminderDate(new Date());
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
					resultListDto.add(resultDto);

				}
			}
			page.setPageItems(resultListDto);
			
			page.setTotalRecords(totalRecords);

			utx.commit();
			return page;
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
		}*/
		return null;

	}
	
	
//	private void updateReminderCount(List<HumanTask> reimbReminderTaskList,
//			String userName, String password) {
//
//		String intimationNumber = null;
//		String claimId = null;
//
//		try {
//
//			for (HumanTask tasks : reimbReminderTaskList) {
//
//				if (tasks.getPayloadCashless() != null && (tasks.getPayloadCashless().getQuery() == null || tasks.getPayloadCashless().getQuery()
//						.getStatus() == null)) {
//
////				if(true){
//					intimationNumber = tasks.getPayloadCashless() != null
//							&& tasks.getPayloadCashless().getIntimation() != null ? tasks
//							.getPayloadCashless().getIntimation()
//							.getIntimationNumber()
//							: null;					
//							
//					claimId = tasks.getPayloadCashless() != null
//							&& tasks.getPayloadCashless().getClaim() != null ? tasks
//							.getPayloadCashless().getClaim()
//							.getClaimId()
//							: null;
//					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType caPayload = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
//					
//					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType intimationBo = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType(); 
//					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType();
//
//					if(intimationNumber != null){
//						intimationBo.setIntimationNumber(intimationNumber);
//						caPayload.setIntimation(intimationBo);
//					}
//					else if(claimId != null){
//						claimBO.setClaimId(claimId);
//						caPayload.setClaim(claimBO);	
//					}
//					if(caPayload.getIntimation() != null || caPayload.getClaim() != null ){
//						
//					CLGenerateLetterTask generateBillsNotReceivedReminderLetterTask = BPMClientContext
//							.getCashlessRemainderLetterTask(userName, password);
//					PagedTaskList preauthBillsNotReceivedTaskList = generateBillsNotReceivedReminderLetterTask
//							.getTasks(userName, new Pageable(), caPayload);
//
//					String remCount = "";
//					if (preauthBillsNotReceivedTaskList != null) {
//						UpdateCPUCodeCashless updatePayloadTask = BPMClientContext
//								.getUpdateCashlessPayloadTask();
//						if (preauthBillsNotReceivedTaskList.getHumanTasks() != null
//								&& !preauthBillsNotReceivedTaskList
//										.getHumanTasks().isEmpty()) {
//							Object finalHTList[] = (Object[]) preauthBillsNotReceivedTaskList
//									.getHumanTasks().toArray();
//							
//							
//
//							if(finalHTList.length <= 3){
//							for (int i = 1; i < finalHTList.length; i++) {
//
//								int j = i;
//
//								while (j > 0
//										&& ((HumanTask) finalHTList[j])
//												.getNumber() < ((HumanTask) finalHTList[j-1])
//												.getNumber()) {
//									HumanTask ht = (HumanTask) finalHTList[j];
//									finalHTList[j] = finalHTList[j - 1];
//									finalHTList[j - 1] = ht;
//									j--;
//								}
//							}
//
//							for (int i = 0; i < finalHTList.length; i++) {
//								Integer reminderCount = i + 1;
//								remCount = reminderCount.toString();
//								HumanTask currentHt = (HumanTask) finalHTList[i];
//								currentHt.getPayloadCashless().getQuery()
//										.setStatus(remCount);
//
//								try {
//									updatePayloadTask.execute("staradmin",
//											"generateLetter", currentHt
//													.getPayloadCashless(),
//											currentHt.getPayloadCashless()
//													.getQuery(), "status",
//											remCount, 0, 0, currentHt
//													.getNumber(), false, null);
//								} catch (Exception e) {
//									e.printStackTrace();
//									log.error(e.toString());
//								}
//							}
//							}
//					
//				}
//				}}}
//
//				else {
//					if(tasks.getPayload() != null && (tasks.getPayload().getQuery() == null ||tasks.getPayload().getQuery()
//						.getStatus() == null)){
////						if(true){
//					intimationNumber = tasks.getPayload() != null
//							&& tasks.getPayload().getIntimation() != null ? tasks
//							.getPayload().getIntimation().getIntimationNumber()
//							: null;	
//						
//					claimId = tasks.getPayload() != null
//							&& tasks.getPayload().getClaim() != null ? tasks
//							.getPayload().getClaim().getClaimId()
//							: null;
//
//					PayloadBOType payloadBO = new PayloadBOType();
//					
//					IntimationType intimationBo = new IntimationType();
//					ClaimType claimBO = new ClaimType();
//					
//					if(intimationNumber != null){
//						intimationBo.setIntimationNumber(intimationNumber);
//						payloadBO.setIntimation(intimationBo);
//					}
//					else if(claimId != null){
//						claimBO.setClaimId(claimId);
//						payloadBO.setClaim(claimBO);	
//					}
//					
//					if(payloadBO.getIntimation() != null || payloadBO.getClaim() != null){
//					GenerateLetterTask generateReimbReminderLetterTask = BPMClientContext
//							.getGenerateReimbRemainderLetterTask(userName,
//									password);
//					PagedTaskList reminderTaskList = generateReimbReminderLetterTask
//							.getTasks(userName, new Pageable(), payloadBO);
//					String remCount = "";
//					if (reminderTaskList != null) {
//						
//						UpdateCPUCode updatePayload = BPMClientContext.callUpdateCPUCode();
//						if (reminderTaskList.getHumanTasks() != null
//								&& !reminderTaskList.getHumanTasks().isEmpty()) {
//							Object finalHTList[] = (Object[]) reminderTaskList
//									.getHumanTasks().toArray();
//
//							if(finalHTList.length <= 3){
//							for (int i = 1; i < finalHTList.length; i++) {
//
//								int j = i;
//
//								while (j > 0
//										&& ((HumanTask) finalHTList[j])
//												.getNumber() < ((HumanTask) finalHTList[j - 1])
//												.getNumber()) {
//									HumanTask ht = (HumanTask) finalHTList[j];
//									finalHTList[j] = finalHTList[j - 1];
//									finalHTList[j - 1] = ht;
//									j--;
//								}
//							}
//
//							for (int i = 0; i < finalHTList.length; i++) {
//								Integer reminderCount = i + 1;
//								remCount = reminderCount.toString();
//								HumanTask currentHt = (HumanTask) finalHTList[i];
//								currentHt.getPayload().getQuery()
//										.setStatus(remCount);
//								try {
//									updatePayload.execute("staradmin",
//									 "generateLetter",
//									 currentHt.getPayload(),
//									 currentHt.getPayload().getQuery(),
//									 "status", remCount, 0, 0,
//									 currentHt.getNumber(), false, null);
//								} catch (Exception e) {
//									e.printStackTrace();
//									log.error(e.toString());
//								}
//							}
//						  }
//						}
//						}
//					}
//					}
//				}
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error(e.toString());
//		}
//	}
	
/*	
	private PagedTaskList validateTasks(PagedTaskList tasklist, String userName, String passWord)
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
	

//	private List<SearchGenerateReminderTableDTO> getIntimationData(String intimationNo,Long queryKey, Long rodKey, HumanTask humanTask){
//		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
//		
//		List<Claim> intimationsList = new ArrayList<Claim>(); 
//		
//		Root<Claim> root = criteriaQuery.from(Claim.class);
//		
//		List<Predicate> conditionList = new ArrayList<Predicate>();
//		try{
//		if(intimationNo != null || !intimationNo.isEmpty()){
//			Predicate condition1 = criteriaBuilder.equal(root.<Intimation>get("intimation").<String>get("intimationId"), intimationNo);
//			conditionList.add(condition1);
//			criteriaQuery.select(root).where(
//					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
//		}
//		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
//		intimationsList = typedQuery.getResultList();
//		
//			for(Claim inti:intimationsList){
//				System.out.println(inti.getIntimation().getIntimationId()+"oooooooooooooooooooooooooo"+inti.getIntimation().getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
//			}
//			List<Claim> doList = intimationsList;
//			List<SearchGenerateReminderTableDTO> tableDTO = SearchGenerateRemainderMapper.getClaimDTO(doList);
//			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
//			
//			tableDTO = getHospitalDetails(tableDTO, queryKey, rodKey, humanTask);
//		
//		return tableDTO;
//		}catch(Exception e){
//			return null;
//		}
//	}

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	

//	private List<SearchGenerateReminderTableDTO> getHospitalDetails(
//			List<SearchGenerateReminderTableDTO> tableDTO, Long queryKey, Long rodKey, HumanTask humanTask) {
//		Hospitals hospitalDetail;
//		for(int index = 0; index < tableDTO.size(); index++){
//			
//			
//			 tableDTO.get(index).setRodKey(rodKey);
//			 tableDTO.get(index).setHumanTaskDTO(humanTask);
//			 tableDTO.get(index).setQueryKey(queryKey);
//			
//			
//			Query findByHospitalKey = entityManager.createNamedQuery(
//					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
//			try{
//			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
//			 if(hospitalDetail != null){
//			
//				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
//				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
//				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
//				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
//				
//			 }
//			}catch(Exception e){
//				continue;
//			}
//		
//		}
//		
//		
//		return tableDTO;
//	}	

//	private TmpCPUCode getTmpCPUCode(Long cpuId){
//		try{
//		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
//		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
//		return tmpCPUCode;
//		}catch(Exception e){
//			return null;
//		}
//		
//	}

	public void submitReminderLetter(SearchGenerateReminderTableDTO reminderLetterDto){
		
		/*try{
			
		utx.begin();
		String fileUrl =reminderLetterDto.getFileUrl();
		
		*//**
		 * Need to upload reminder Letter to DMS
		 *//*
		
		if(null != fileUrl && !("").equalsIgnoreCase(fileUrl))
		{
			HashMap dataMap = new HashMap();
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
			Long documentToken = docToken != null ? Long.valueOf(docToken): null;
			reminderLetterDto.setDocToken(documentToken);
			reminderLetterDto.setGeneratedDate(new Date());
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
				
				queryObj.setReminderCount(Long.valueOf(queryDto.getReminderCount()));
				queryObj.setReminderDate1(queryDto.getFirstReminderDate());
				queryObj.setReminderDate2(queryDto.getSecondReminderDate());
				queryObj.setReminderDate3(queryDto.getThirdReminderDate());
				entityManager.merge(queryObj);
				entityManager.flush();
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
		submitReminderDetails(reminderLetterDto);
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
		} */
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
	
	public void submitReminderDetails(SearchGenerateReminderTableDTO reminderLetterDto){
		
		ClaimReminderDetails resultObj = new ClaimReminderDetails();
		
		resultObj.setBatchNo(reminderLetterDto.getBatchId());
		resultObj.setCategory(reminderLetterDto.getCategory());
		resultObj.setCpuCode(reminderLetterDto.getClaimDto().getNewIntimationDto().getCpuCode() != null ? Long.valueOf(reminderLetterDto.getClaimDto().getNewIntimationDto().getCpuCode()) : null);
		Integer remidnerCount = null;
		if(reminderLetterDto.getReimbQueryDto().getKey() != null){
			remidnerCount = reminderLetterDto.getReimbQueryDto().getReminderCount();
		}
		else {
			remidnerCount = reminderLetterDto.getClaimDto().getReminderCount();
		}
		resultObj.setReminderCount(remidnerCount);
		resultObj.setIntimationKey(reminderLetterDto.getClaimDto().getNewIntimationDto().getKey());
		resultObj.setIntimatonNo(reminderLetterDto.getClaimDto().getNewIntimationDto().getIntimationId());
		resultObj.setClaimKey(reminderLetterDto.getClaimDto().getKey());
		resultObj.setTransacKey(reminderLetterDto.getPreauthKey());
		resultObj.setDocumentToken(reminderLetterDto.getDocToken());
		resultObj.setGeneratedDate(reminderLetterDto.getGeneratedDate());
		resultObj.setCreatedBy(SHAUtils.getUserNameForDB(reminderLetterDto.getUsername()));
		resultObj.setCreatedDate(new Date());
		resultObj.setPrintCount(1);
		resultObj.setPrintFlag("Y");
		
		try{
			entityManager.persist(resultObj);
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		
	}
	
}
