package com.shaic.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;


/*import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claimrequest.ClaimRequestType;
import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.classification.ClassificationType;
import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.hospitalinfo.HospitalInfoType;
import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType;
import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType;
<<<<<<< HEAD
import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.policy.PolicyType;*/
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.registration.GenerateCoveringLetterSearchDto;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
/*import com.shaic.ims.bpm.claim.corev2.PagedTaskList;
import com.shaic.ims.bpm.claim.modelv2.HumanTask;
import com.shaic.ims.bpm.claim.servicev2.conversion.search.GenCovLetterConvTask;
import com.shaic.ims.bpm.claim.servicev2.registration.search.GenCovLetterRegTask;*/
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

@Stateless
public class GenerateCoveringLetterService {
	@PersistenceContext
	protected EntityManager entityManager;
	
	 public Page<GenerateCoveringLetterSearchTableDto> getClaimsToGenerateCoveringLetter(GenerateCoveringLetterSearchDto searchDto) {
			
		 	Map<String, Object> queryFilter = searchDto.getQueryFilter();
		 	List<GenerateCoveringLetterSearchTableDto> resultContainer = null;
			
			
//			GenCovLetterRegTask coveringLettertask =  BPMClientContext.getCoveringLetterTask(queryFilter.get(BPMClientContext.USERID).toString(), queryFilter.get(BPMClientContext.PASSWORD).toString());
			Page<GenerateCoveringLetterSearchTableDto> searchResultContainer = new Page<GenerateCoveringLetterSearchTableDto>();	
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			String priority = null;
			String source = null;
			String type = null;
//			String lobType = null;
			String lob = null;
			String hospType = null;
			String accDeath = null;
			Pageable apage = null;
			
			String priorityNew = null;

			
			if(queryFilter.containsKey("lob") && queryFilter.get("lob") != null){
				
				lob = String.valueOf(queryFilter.get("lob"));
				mapValues.put(SHAConstants.LOB, lob);
			}
			
			if(queryFilter.containsKey("priority") && queryFilter.get("priority") != null){
			    priority = queryFilter.get("priority").toString();
			    mapValues.put(SHAConstants.PRIORITY, priority);
			}
			
			if(queryFilter.containsKey("hospType") && queryFilter.get("hospType") != null){
				hospType = String.valueOf(queryFilter.get("hospType"));
			}
			if(queryFilter.containsKey("accDeath") && queryFilter.get("accDeath") != null){
				accDeath = String.valueOf(queryFilter.get("accDeath"));
			}
			if(queryFilter.containsKey("type") && queryFilter.get("type") != null){
				type = queryFilter.get("type").toString();
				mapValues.put(SHAConstants.RECORD_TYPE, type);
			}
			
			if(queryFilter.containsKey("source") && queryFilter.get("source") != null){
				source = queryFilter.get("source").toString();
				mapValues.put(SHAConstants.STAGE_SOURCE, source);
			}
			
			if(queryFilter.containsKey("priorityNew") && queryFilter.get("priorityNew") != null) {
				priorityNew = queryFilter.get("priorityNew").toString();
				if (priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)) {
					mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
				} else if (priorityNew.equalsIgnoreCase(SHAConstants.CRM_FLAGGED)){
					mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
				}
			}
			

			if(queryFilter != null && (queryFilter.containsKey("intimationNumber") || queryFilter.containsKey("cpuCode") || queryFilter.containsKey("policyNumber") || queryFilter.containsKey("registeredDate") || queryFilter.containsKey("accDeath") || queryFilter.containsKey("hospType"))){
				

				//IntimationType intimationInfo = new IntimationType();
				if(queryFilter.containsKey("intimationNumber") && queryFilter.get("intimationNumber") != null)
				{					
					mapValues.put(SHAConstants.INTIMATION_NO, queryFilter.get("intimationNumber").toString());
				}

				//ClaimRequestType claimInfo = new ClaimRequestType();				

			/*	if(accDeath != null && !accDeath.isEmpty()){
					
					if(payloadBO.getIntimation() == null){
						payloadBO.setIntimation(intimationInfo);
					}
					payloadBO.getIntimation().setReason(accDeath);
				}		*/

				if(queryFilter.containsKey("cpuCode")  && queryFilter.get("cpuCode") != null){
					
					String cpuCode = queryFilter.get("cpuCode").toString();
					
					String cpuCodeValues[] = cpuCode != null ? cpuCode.split(" ") : null;
					cpuCode = cpuCodeValues != null && cpuCodeValues.length > 0 ? cpuCodeValues[0] : null;				

					mapValues.put(SHAConstants.CPU_CODE, cpuCode);	

				}

				if(queryFilter.containsKey("registeredDate") && queryFilter.get("registeredDate") != null)
				{
					java.util.Date datVar = (java.util.Date)queryFilter.get("registeredDate");	
				   
					java.sql.Date regDate = new java.sql.Date(datVar.getTime());
					mapValues.put(SHAConstants.PAYLOAD_REGISTRATION_DATE,regDate);
					
				}
				

				if(queryFilter.containsKey("policyNumber") && queryFilter.get("policyNumber") != null){
					
					mapValues.put(SHAConstants.POLICY_NUMBER, queryFilter.get("policyNumber").toString());

				}				
				
				
			}
			
			if(queryFilter.containsKey("pageable") && queryFilter.get("pageable") != null){
				apage = (Pageable)queryFilter.get("pageable");
			}	
			//ClassificationType classification = null;
			
		    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
					//classification = new ClassificationType();
					
					if(priority != null && ! priority.isEmpty())
						if(priority.equalsIgnoreCase(SHAConstants.ALL)){
							priority = null;
						}
					mapValues.put(SHAConstants.PRIORITY, priority);
					//classification.setPriority(priority);
					if(source != null && ! source.isEmpty()){
						//classification.setSource(source);
						mapValues.put(SHAConstants.STAGE_SOURCE, priority);
					}
					
					if(type != null && ! type.isEmpty()){
						if(type.equalsIgnoreCase(SHAConstants.ALL)){
							type = null;
						}

						//classification.setType(type);
						mapValues.put(SHAConstants.RECORD_TYPE, type);
					}
					
//					if(payloadBO == null){
//						payloadBO = new PayloadBOType();
//					}
//					
//					 payloadBO.setClassification(classification);
			} 

			try
			{
				apage.setPageNumber(1);
				apage.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
				
//				ImsUser imsUser = (ImsUser)queryFilter.get(BPMClientContext.USER_OBJECT);
//				
//				String[] userRoleList = imsUser.getUserRoleList();

				/**
				 * bpmTaskList for registration officer
				 */
//				PagedTaskList bpmTasklist = null;
//				
//				if(Arrays.asList(userRoleList).contains(SHAConstants.REGISTRATIONOFFICER)){
//					bpmTasklist = coveringLettertask.getTasks(queryFilter.get(BPMClientContext.USERID).toString(), apage, payloadBO);
//				}
				
				/**
				 * tasks for registration officer
				 */

//				PagedTaskList tasks = null;
				
////				if(Arrays.asList(userRoleList).contains(SHAConstants.ZONALREGOFFICER)){
////			    GenCovLetterConvTask generateCoveringLetterTask = BPMClientContext.getGenerateCoveringLetterTask(queryFilter.get(BPMClientContext.USERID).toString(), queryFilter.get(BPMClientContext.PASSWORD).toString());
////			    
////			    List<HumanTask> humanTasks = bpmTasklist != null ? bpmTasklist.getHumanTasks() : null;
//			    Integer endPage = humanTasks != null ? humanTasks.size() : 0;
//			    Integer pageSize = (ReferenceTable.PAGE_SIZE)- endPage;
			    
//			    apage.setPageSize(0);
			    
			    apage.setPageNumber(1);
			    
//			    if(humanTasks != null && humanTasks.size() < Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10)){
//
//			    	 apage.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) - humanTasks.size() : 10);
//
//			    }			   

//			    tasks = generateCoveringLetterTask.getTasks(queryFilter.get(BPMClientContext.USERID).toString(), apage, payloadBO);
//				}
			    
//				List<HumanTask> taskList = null;
//				if(bpmTasklist != null){
//					taskList = bpmTasklist.getHumanTasks();
//				}
//				
//				if(tasks != null){
//					if(taskList != null){
//						taskList.addAll(tasks.getHumanTasks());
//					}else{
//						taskList = tasks.getHumanTasks();
//					}
//				}
			    
//				if(taskList != null && ! taskList.isEmpty())
//				{
				
//			List<HumanTask> taskList = instance.getCoveringLetterTask(queryFilter.get(BPMClientContext.USERID).toString(), queryFilter.get(BPMClientContext.PASSWORD).toString(),null,qf);

//			if(null != taskList && !taskList.isEmpty()){
				
			    resultContainer = new ArrayList<GenerateCoveringLetterSearchTableDto>();
						    
			    mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.GENERATE_COVERING_LETTER_CURRENT_QUEUE);
			    String userId = queryFilter.get(BPMClientContext.USERID).toString();
				mapValues.put(SHAConstants.USER_ID, userId);
				
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
				
				List<Map<String, Object>> taskProcedure = null ;				
				DBCalculationService dbCalculationService = new DBCalculationService();
				
				taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);
				
				if (null != taskProcedure) {
					
					int totalRecords = 0;
					for (Map<String, Object> outPutArray : taskProcedure) {
			
					Long claimKey=null;
					
					claimKey = (Long)outPutArray.get(SHAConstants.DB_CLAIM_KEY);
					System.out.println("claim KEY ++++++++++++++++++++++++++++++ "+claimKey);
					
					if(null != claimKey){
					Claim claimToGenerateCoveringLetter = entityManager.find(Claim.class, Long.valueOf(claimKey));
					
					if(claimToGenerateCoveringLetter != null){
						entityManager.refresh(claimToGenerateCoveringLetter);
					}
					 
					 if(null != claimToGenerateCoveringLetter){
						 GenerateCoveringLetterSearchTableDto generateCoveringLetterSearchTableDto = new GenerateCoveringLetterSearchTableDto();
						 generateCoveringLetterSearchTableDto.setClaimNumber(claimToGenerateCoveringLetter.getClaimId());
						 generateCoveringLetterSearchTableDto.setAdmissionDate(new SimpleDateFormat("dd/MM/yyyy").format(claimToGenerateCoveringLetter.getIntimation().getAdmissionDate()));
//						 generateCoveringLetterSearchTableDto.setClaimType(claimToGenerateCoveringLetter.getClaimType().getValue());
						 
						 if(null != claimToGenerateCoveringLetter.getIntimation() && null != claimToGenerateCoveringLetter.getIntimation().getPolicy()){
							 generateCoveringLetterSearchTableDto.setPolicyNo(claimToGenerateCoveringLetter.getIntimation().getPolicy().getPolicyNumber());	 
						 }
						 if(null != claimToGenerateCoveringLetter.getIntimation() && null != claimToGenerateCoveringLetter.getIntimation().getIncidenceFlag()){
							 generateCoveringLetterSearchTableDto.setAccedentDeath(("A").equalsIgnoreCase(claimToGenerateCoveringLetter.getIntimation().getIncidenceFlag()) ? SHAConstants.ACCIDENT : SHAConstants.DEATH );
						 }								 
								 
						 if(null != claimToGenerateCoveringLetter.getIntimation() && null != claimToGenerateCoveringLetter.getIntimation().getHospitalType())
						 {
							 generateCoveringLetterSearchTableDto.setClaimType(StringUtils.equalsIgnoreCase(claimToGenerateCoveringLetter.getIntimation().getHospitalType().getValue(),"network") ? "Cashless":"Re-imbursement");
						 }else
						 {
							 generateCoveringLetterSearchTableDto.setClaimType("");							 
						 }
						 
						 if(claimToGenerateCoveringLetter.getClaimType() != null){
							 generateCoveringLetterSearchTableDto.setClaimType(claimToGenerateCoveringLetter.getClaimType().getValue());
						 }
						 
						 if(null != claimToGenerateCoveringLetter.getIntimation().getCpuCode() && null != claimToGenerateCoveringLetter.getIntimation().getCpuCode().getCpuCode()){
							 generateCoveringLetterSearchTableDto.setCpuCode(claimToGenerateCoveringLetter.getIntimation().getCpuCode().getCpuCode().toString());	 
						 }
						 generateCoveringLetterSearchTableDto.setInsuredPatientName(claimToGenerateCoveringLetter.getIntimation().getInsured().getInsuredName());
						 
						 Long hospitalKey = claimToGenerateCoveringLetter.getIntimation().getHospital();
						 String hospitalName="";
						 HospitalDto hospDto = null;
						 if(null != hospitalKey &&  null != claimToGenerateCoveringLetter.getIntimation() && null != claimToGenerateCoveringLetter.getIntimation().getHospitalType()){
							 if(StringUtils.containsIgnoreCase(claimToGenerateCoveringLetter.getIntimation().getHospitalType().getValue(),"network"))
							 {
								 Hospitals networkHosp = entityManager.find(Hospitals.class, hospitalKey);
								 hospitalName = networkHosp != null ? networkHosp.getName()  : "";
								 hospDto = new HospitalDto(networkHosp);
							 }
							 else{
								 TmpHospital tempHospital = entityManager.find(TmpHospital.class, hospitalKey);
								 hospitalName = tempHospital != null ? tempHospital.getHospitalName()  : "";
							 }
							 generateCoveringLetterSearchTableDto.setHospitalType(claimToGenerateCoveringLetter.getIntimation().getHospitalType().getValue());
						 }
						 generateCoveringLetterSearchTableDto.setHospitalName(hospitalName);
						 Long lobId = claimToGenerateCoveringLetter.getIntimation().getPolicy().getLobId();
						 MastersValue lobObject = entityManager.find(MastersValue.class, lobId);
						 if(null != lobObject){
						 generateCoveringLetterSearchTableDto.setLob(lobObject.getValue());
						 }
//						 generateCoveringLetterSearchTableDto.setHumanTask(humanTask);
//						 generateCoveringLetterSearchTableDto.setTaskNumber(humanTask.getNumber());
						 
						 generateCoveringLetterSearchTableDto.setDbOutArray(outPutArray);
						 NewIntimationDto newIntimationDto = NewIntimationMapper.getInstance().getNewIntimationDto(claimToGenerateCoveringLetter.getIntimation());
						 newIntimationDto.setPolicy(claimToGenerateCoveringLetter.getIntimation().getPolicy());		
						 newIntimationDto.setHospitalDto(hospDto);
						 if(newIntimationDto != null){
								
							 TmpCPUCode cpuObject = claimToGenerateCoveringLetter.getIntimation().getCpuCode();

							 if (null != cpuObject) {
							 	 newIntimationDto.setReimbCpuAddress(cpuObject.getReimbAddress());
							 }
							 
							 newIntimationDto.setHospitalType(new SelectValue(claimToGenerateCoveringLetter.getIntimation().getHospitalType().getKey(),claimToGenerateCoveringLetter.getIntimation().getHospitalType().getValue()));
							 newIntimationDto.setHospitalTypeValue(claimToGenerateCoveringLetter.getIntimation().getHospitalType().getValue());
							 newIntimationDto.setHospitalName(hospDto != null && hospDto.getRegistedHospitals() != null ? hospDto.getRegistedHospitals().getName(): "");
							 
							 generateCoveringLetterSearchTableDto.setIntimationNo(newIntimationDto.getIntimationId());
							 generateCoveringLetterSearchTableDto.setAccedentDeath(null != newIntimationDto.getIncidenceFlag() ? (("A").equalsIgnoreCase(newIntimationDto.getIncidenceFlag())? SHAConstants.ACCIDENT: SHAConstants.DEATH) : "");

							 if(newIntimationDto.getPolicy() != null){
								 generateCoveringLetterSearchTableDto.setPolicyNo(newIntimationDto.getPolicy().getPolicyNumber());
							 }
						 }
						 
						 generateCoveringLetterSearchTableDto.setClaimStatus(claimToGenerateCoveringLetter.getStatus().getProcessValue());
						 ClaimDto claimDto = ClaimMapper.getInstance().getClaimDto(claimToGenerateCoveringLetter);
						 claimDto.setNewIntimationDto(newIntimationDto);
						 
//						 List<DocumentCheckListDTO>  docCheckListDto = getCoveringLetterDocDetails(claimToGenerateCoveringLetter.getKey());
//						 if(docCheckListDto != null && !docCheckListDto.isEmpty()){
//							 claimDto.setDocumentCheckListDTO(docCheckListDto);
//						 }
						 
						 generateCoveringLetterSearchTableDto.setClaimDto(claimDto);
						 generateCoveringLetterSearchTableDto.setUserId(queryFilter.get(BPMClientContext.USERID).toString());
						 generateCoveringLetterSearchTableDto.setPassword(queryFilter.get(BPMClientContext.PASSWORD).toString());
						 
						 if(claimToGenerateCoveringLetter.getCrcFlag()!= null && claimToGenerateCoveringLetter.getCrcFlag().equals("Y")) {
							 generateCoveringLetterSearchTableDto.setColorCodeCell("OLIVE");
						}

						 resultContainer.add(generateCoveringLetterSearchTableDto);
						 totalRecords = (Integer) outPutArray.get(SHAConstants.TOTAL_RECORDS);								 

					 }
					}
				}	
//			}
//			searchDto.getPageable().setPageNumber(bpmTasklist.getCurrentPage()+1);			
			searchResultContainer.setPageItems(resultContainer);
//			searchResultContainer.setPageNumber(bpmTasklist.getCurrentPage());
//			searchResultContainer.setTotalRecords(bpmTasklist.getTotalRecords());

			searchResultContainer.setTotalRecords(totalRecords);
			
			}
	 	}	
		catch(Exception e){
		e.printStackTrace();	
		}
			return searchResultContainer;
		}
//	 public List<DocumentCheckListDTO> getCoveringLetterDocDetails(Long claimKey){
//	
//		 List<DocumentCheckListDTO> dokCheckList = new ArrayList<DocumentCheckListDTO>();
//		 
//		 Query dokQuery = entityManager.createNamedQuery("CoveringLetterDocDetails.findByClaimKey");
//		 dokQuery.setParameter("claimKey", claimKey);
//		 List<CoveringLetterDocDetails> coveringDokList = (List<CoveringLetterDocDetails>)dokQuery.getResultList();
//		
//		 if(coveringDokList != null && !coveringDokList.isEmpty()){
//			 
//			 for (CoveringLetterDocDetails coveringLetterDocDetails : coveringDokList) {
//				
//				 Query docMasterQuery = entityManager.createNamedQuery("DocumentCheckListMaster.findByKey");
//				 docMasterQuery.setParameter("primaryKey", coveringLetterDocDetails.getDocTypeId());
//				 List<DocumentCheckListMaster> docMasterList = (List<DocumentCheckListMaster>)docMasterQuery.getResultList();
//				 if(docMasterList != null && !docMasterList.isEmpty()){
//					 DocumentCheckListMaster documentCheckListMaster = docMasterList.get(0);
//					 if(documentCheckListMaster != null) {
//						 entityManager.refresh(documentCheckListMaster);
//						 DocumentCheckListDTO dokCheckListDto = new DocumentCheckListDTO();
//						 dokCheckListDto.setParticularsValue(documentCheckListMaster.getValue());	
//						 SelectValue particularsValue = new SelectValue(documentCheckListMaster.getKey(), documentCheckListMaster.getValue());
//						 dokCheckListDto.setParticulars(particularsValue);
//						 dokCheckList.add(dokCheckListDto);
//					 }
//				 }
//			  }
//		 }
//		  
//		 return dokCheckList;
//	 }
}
