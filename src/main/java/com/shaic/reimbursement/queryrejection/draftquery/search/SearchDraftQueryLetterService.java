/**
 * 
 */
package com.shaic.reimbursement.queryrejection.draftquery.search;

import java.util.ArrayList;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.DocAcknowledgementDto;
import com.shaic.claim.DocAcknowledgementMapper;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementMapper;
import com.shaic.claim.ReimbursementQueryMapper;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.Label;
/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchDraftQueryLetterService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public SearchDraftQueryLetterService() {
		super();
		
	}
	
	public  Page<SearchDraftQueryLetterTableDTO> search(
			SearchDraftQueryLetterFormDTO searchFormDTO,
			String userName, String passWord) {

		
			try{
				String intimationNo =   null != searchFormDTO  && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null ;
				String 	claimNo = null != searchFormDTO  && null != searchFormDTO.getClaimNo() ? searchFormDTO.getClaimNo() : null;
				String cpuCode = null != searchFormDTO  && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getValue() : null;
				
				String priority = null != searchFormDTO  && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
				String source = null != searchFormDTO  &&searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
				String type = null != searchFormDTO  && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
				String querytype = null != searchFormDTO  && searchFormDTO.getQueryType() != null ? searchFormDTO.getQueryType().getValue() != null ? searchFormDTO.getQueryType().getValue(): null : null;
				
				String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
				
				String cpuCodeValues[] = cpuCode != null ? cpuCode.split(" ") : null;
				cpuCode = cpuCodeValues != null && cpuCodeValues.length > 0 ? cpuCodeValues[0] : null;
				
				
				Long queryKey = null;
				List<String> intimationNoList = new ArrayList<String>();
				List<Long> queryKeyList = new ArrayList<Long>();
				List<Long> rodKeyList = new ArrayList<Long>();
				List<Integer> taskNumber = new ArrayList<Integer>();
				
				List<Map<String, Object>> taskProcedure = null ;
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				
				workFlowMap= new WeakHashMap<Long, Object>();			
				
				Integer totalRecords = 0; 
				
				mapValues.put(SHAConstants.USER_ID, userName);
				mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.DRAFT_QUERY_LETTER_CURRENT_QUEUE);		
				
				

				if((ReferenceTable.PA_LOB_KEY).equals(searchFormDTO.getLobKey())){				
					mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);					
					
				}
				
				if((ReferenceTable.HEALTH_LOB_KEY).equals(searchFormDTO.getLobKey())){				
					mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);					
				}
				
				Long rodKey = null;
			//	PayloadBOType payloadBOType = null;
			//	IntimationType intimationType = new IntimationType();
				if(null != intimationNo && !intimationNo.isEmpty()){					
					mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);	
					
					}
				//	ClaimType claimType = new ClaimType();
				/*ClaimType claimType = new ClaimType();
				if(null != claimNo && !claimNo.isEmpty()){
					
					
					if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}
					
					claimType.setClaimId(claimNo);
					payloadBOType.setClaim(claimType);
				}
					ClaimRequestType claimRequestType = new ClaimRequestType(); */
					if(null != cpuCode){					
						
						mapValues.put(SHAConstants.CPU_CODE, cpuCode);
					}
					
					if(null != claimNo){
						
						mapValues.put(SHAConstants.CLAIM_NUMBER, claimNo);
					}
					
				//	ClassificationType classification = null;
					
					if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
							|| type != null && ! type.isEmpty()){
					//	classification = new ClassificationType();
						
						if(priority != null && ! priority.isEmpty())
							if(!priority.equalsIgnoreCase(SHAConstants.ALL)){
								mapValues.put(SHAConstants.PRIORITY, priority);
							//	priority = null;
							}
						//classification.setPriority(priority);
						
						if(source != null && ! source.isEmpty()){
							//classification.setSource(source);
							mapValues.put(SHAConstants.STAGE_SOURCE, source);
						}
						
						if(type != null && ! type.isEmpty()){
							//classification.setType(type);
							if(!type.equalsIgnoreCase(SHAConstants.ALL)){
							mapValues.put(SHAConstants.RECORD_TYPE, type);
							}
						}						

					}
					
					if(priorityNew != null && ! priorityNew.isEmpty() && !priorityNew.equalsIgnoreCase(SHAConstants.ALL))
						if(priorityNew.equalsIgnoreCase(SHAConstants.NORMAL)){
							mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "N");
						}else{
							mapValues.put(SHAConstants.PAYLOAD_RRC_REQUEST_TYPE, "Y");
						}
				
				
			/*	DraftQueryLetterTask draftQueryLetterTask = BPMClientContext.getDraftQueryLetterTask(userName, passWord);
				

				PagedTaskList taskList;*/
					
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
					
				DBCalculationService dbCalculationService = new DBCalculationService();
				taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
				/*	
				if(null != intimationNo && !intimationNo.isEmpty() || null != claimNo && !claimNo.isEmpty() || null != cpuCode){
					 taskList = draftQueryLetterTask.getTasks(userName,null,payloadBOType);
				}else{
					taskList = draftQueryLetterTask.getTasks(userName,searchFormDTO.getPageable(),payloadBOType);
				}*/				
			
			/*	if(null != draftQueryLetterTask){
					List<HumanTask> humanTaskList = taskList.getHumanTasks();
					if(null == humanTaskList || humanTaskList.isEmpty()){
						return null;
					}
					
					for(HumanTask humanTask : humanTaskList){
						PayloadBOType payloadBO = humanTask.getPayload();
						if(null != payloadBO.getQuery() && null != payloadBO.getQuery().getKey() && null != payloadBO.getRod()){
						intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
						queryKeyList.add(payloadBO.getQuery().getKey());
						rodKeyList.add(payloadBO.getRod().getKey());
						taskNumber.add(humanTask.getNumber());
						humanTaskListDTO.add(humanTask);
						}
					}
				}*/
				
				if (null != taskProcedure) {
					for (Map<String, Object> outPutArray : taskProcedure) {							
							Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_QUERY_KEY);
							String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
							Long reimbKey = (Long)outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);
							workFlowMap.put(keyValue,outPutArray);
							intimationNoList.add(intimationNumber);
							rodKeyList.add(reimbKey);
							queryKeyList.add(keyValue);
							//ackKeyList.add(keyValue);
						
						totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
					}
				}
				
				List<SearchDraftQueryLetterTableDTO> tableDTO = new ArrayList<SearchDraftQueryLetterTableDTO>();
				for(int index = 0 ; index < intimationNoList.size() ; index++){
					if(index < rodKeyList.size()){
					intimationNo = intimationNoList.get(index);
					
					// humanTaskDTO = humanTaskListDTO.get(index);
					
					 queryKey = queryKeyList.get(index);
					 
					 rodKey = rodKeyList.get(index);
					 
					// Integer taskNo = taskNumber.get(index);
				
					 
					 tableDTO.addAll(getIntimationData(intimationNo, queryKey, rodKey/*,humanTaskDTO,taskNo*/));
					}
				}	
			
				Page<SearchDraftQueryLetterTableDTO> page = new Page<SearchDraftQueryLetterTableDTO>();
			//	page.setPageNumber(taskList.getCurrentPage());
				page.setTotalRecords(totalRecords);
				page.setPageItems(tableDTO);
				return page;
				}
				
			catch(Exception e){
				e.printStackTrace();
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			}
				return null;	
				
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
		} 
		else {
			// intimationKey null
		}
		return a_claimList.get(0);

	}

	private List<SearchDraftQueryLetterTableDTO> getIntimationData(String intimationNo,Long queryKey, Long rodKey/*,HumanTask humanTask,Integer taskNumber*/){
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		List<Claim> intimationsList = new ArrayList<Claim>(); 
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(intimationNo != null || !intimationNo.isEmpty()){
			Predicate condition1 = criteriaBuilder.equal(root.<Intimation>get("intimation").<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}
		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
		intimationsList = typedQuery.getResultList();
		
			for(Claim inti:intimationsList){
				System.out.println(inti.getIntimation().getIntimationId()+"oooooooooooooooooooooooooo"+inti.getIntimation().getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			List<Claim> doList = intimationsList;
			List<SearchDraftQueryLetterTableDTO> tableDTO = SearchDraftQueryLetterMapper.getInstance().getClaimDTO(doList);
			
			ReimbursementQueryDto reimQDto = getReimbursementQueryByKey(queryKey);
			
			if(reimQDto != null){
			
			for (SearchDraftQueryLetterTableDTO searchDraftQueryLetterTableDTO : tableDTO) {
				searchDraftQueryLetterTableDTO.setQueryType(reimQDto != null && reimQDto.getQueryType() != null && ("Y").equalsIgnoreCase(reimQDto.getQueryType()) ? SHAConstants.PAYMENT_QUERY_TYPE : SHAConstants.NORMAL_QUERY_TYPE);
				if(searchDraftQueryLetterTableDTO.getCpuId() != null){
					searchDraftQueryLetterTableDTO.setStrCpuCode(searchDraftQueryLetterTableDTO.getCpuId().toString());
				}
				 		Claim claimObject = getClaimByIntimation(searchDraftQueryLetterTableDTO.getIntimationkey());
						searchDraftQueryLetterTableDTO.setCrmFlagged(claimObject.getCrcFlag());
						if(searchDraftQueryLetterTableDTO.getCrmFlagged() != null){
							if(searchDraftQueryLetterTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
								searchDraftQueryLetterTableDTO.setColorCodeCell("OLIVE");
								searchDraftQueryLetterTableDTO.setCrmFlagged(null);
//								searchDraftQueryLetterTableDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
							}
									
						}
				searchDraftQueryLetterTableDTO.setReimbursementQueryDto(reimQDto);
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			
			tableDTO = getHospitalDetails(tableDTO, queryKey, rodKey/*,humanTask,taskNumber*/);
		
			}
			return tableDTO;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	

	private List<SearchDraftQueryLetterTableDTO> getHospitalDetails(
			List<SearchDraftQueryLetterTableDTO> tableDTO, Long queryKey, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			tableDTO.get(index).setRodKey(rodKey);
		//	tableDTO.get(index).setHumanTaskDTO(humanTask);
			tableDTO.get(index).setQueryKey(queryKey);
			Object workflowKey = workFlowMap.get(tableDTO.get(index).getQueryKey());
			
			tableDTO.get(index).setDbOutArray(workflowKey);
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCity());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				// tableDTO.get(index).setTaskNumber(taskNumber);
//				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
				 
				
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	

	private TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			return null;
		}
		
	}
	


	public ReimbursementQueryDto getReimbursementQueryByKey(
			Long reimbursementQueryKey) {
		ReimbursementQueryDto reimbursementQueryDto = null;
		
		try{
		
		ReimbursementQuery reimbursementQuery = (new ReimbursementQueryService()).getReimbQueryObjByQueryKey(reimbursementQueryKey, entityManager);
		
		ReimbursementQueryMapper rqMapper = new ReimbursementQueryMapper();
		
		reimbursementQueryDto = rqMapper.getReimbursementQueryDto(reimbursementQuery);
		
		Reimbursement reimbursement = reimbursementQuery.getReimbursement();
		
		ReimbursementMapper rMapper = new ReimbursementMapper();
		
		ReimbursementDto reimbursementDto = rMapper.getReimbursementDto(reimbursement);
		
		ClaimMapper clmMapper =  ClaimMapper.getInstance();
		ClaimDto clmDto = clmMapper.getClaimDto(reimbursement.getClaim());
		
		NewIntimationDto intimationDto = getIntimationDto(reimbursement.getClaim().getIntimation());
		
		clmDto.setNewIntimationDto(intimationDto);
		reimbursementDto.setClaimDto(clmDto);
		DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
		DocAcknowledgementDto docAcknowledgementDto = new  DocAcknowledgementMapper().getDocAcknowledgementDto(docAcknowledgement);
		reimbursementDto.setDocAcknowledgementDto(docAcknowledgementDto);
		
		reimbursementQueryDto.setReimbursementDto(reimbursementDto);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return reimbursementQueryDto;
	}
	
	
//	public ReimbursementDto getRemibursementByClaimKey(Long claimKey){
//		ReimbursementDto reimbursementDto = null;
//		Query q = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
//		q.setParameter("claimKey", claimKey);
//		List<Reimbursement> reimbursementList = (List<Reimbursement>)q.getResultList();
//		
//		if(null != reimbursementList && !reimbursementList.isEmpty()){
//			for (Reimbursement reimbursement : reimbursementList) {
//			
//				if(null != reimbursement.getStatus() && StringUtils.equalsIgnoreCase(reimbursement.getStatus().getProcessValue(),"query")){
//					reimbursementDto = (new ReimbursementMapper()).getReimbursementDto(reimbursement);
//					if(null != reimbursementDto){
//						Claim claim = entityManager.find(Claim.class, claimKey);
//						if(null != claim){
//							ClaimDto claimDto = new ClaimMapper().getClaimDto(claim);	
//							NewIntimationDto intimationDto = getIntimationDto(claim.getIntimation());
//							claimDto.setNewIntimationDto(intimationDto);
//							reimbursementDto.setClaimDto(claimDto);
//						}						
//						DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
//						DocAcknowledgementDto docAcknowledgementDto = new  DocAcknowledgementMapper().getDocAcknowledgementDto(docAcknowledgement);
//						reimbursementDto.setDocAcknowledgementDto(docAcknowledgementDto);
//					}
//					break;
//				}				
//			}			
//		}		
//		return reimbursementDto;
//	}
	
	private NewIntimationDto getIntimationDto(Intimation intimation){
		
		IntimationService intimationService = new IntimationService();
		NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation, entityManager);
		
		return intimationDto;
	}
	
	public List<ViewQueryDTO> getQueryValues(Long key,Long claimKey){
		
		List<ViewQueryDTO> querytableValues = getQueryDetails(key);
		
		StringBuffer diagnosisName = new StringBuffer();
		
		List<PedValidation> pedValidationList = search(key);
		for (PedValidation pedValidation : pedValidationList) {
			
			Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey", pedValidation.getDiagnosisId());
			Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
			if(masters != null){
			diagnosisName.append(masters.getValue()).append(",");
			}
		}
		
		Hospitals hospitalDetails=null;
		Claim claim=null;
		if(claimKey != null){
			claim = entityManager.find(Claim.class, claimKey);
		}
		if(claim != null){
		Long hospitalKey = claim.getIntimation().getHospital();
		hospitalDetails = entityManager.find(Hospitals.class, hospitalKey);
		}	
		for (ViewQueryDTO viewQueryDTO : querytableValues) {
			viewQueryDTO.setDiagnosis(diagnosisName.toString());
			if(hospitalDetails != null){
				viewQueryDTO.setHospitalName(hospitalDetails.getName());
				viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
			}
			viewQueryDTO.setClaim(claim);		
//			viewQueryDTO.setDesignation(SHAConstants.QUERY_RAISED_ROLE);
//			viewQueryDTO.setQueryRaised(SHAConstants.QUERY_RAISED_ROLE);
//			viewQueryDTO.setQueryRaiseRole(SHAConstants.QUERY_RAISED_ROLE);
		}		
	return querytableValues;
	}
	
	private List<ViewQueryDTO> getQueryDetails(Long reimbursementKey){
		List<ViewQueryDTO> result = new ArrayList<ViewQueryDTO>();
		AcknowledgementDocumentsReceivedService ackdgmtDocService = new AcknowledgementDocumentsReceivedService();
		result = ackdgmtDocService.getQueryDetails(reimbursementKey, entityManager);
		
		return result; 
	}
	
     public List<PedValidation> search(Long preAuthKey) {	
		

		List<PedValidation> resultList = new ArrayList<PedValidation>();
		
		Query query = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", preAuthKey);
		
		resultList = (List<PedValidation>)query.getResultList();
	    
		return resultList;

	}
     
     

}
