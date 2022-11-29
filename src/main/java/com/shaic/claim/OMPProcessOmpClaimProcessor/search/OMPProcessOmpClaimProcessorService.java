/**
 * 
 */
package com.shaic.claim.OMPProcessOmpClaimProcessor.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.preauth.Preauth;


@Stateless
//public class OMPProcessOmpClaimProcessorService extends AbstractDAO<Preauth> {
public class OMPProcessOmpClaimProcessorService extends AbstractDAO<OMPIntimation> {

	@EJB
	private OMPIntimationService ompIntimationService;
	
	public OMPProcessOmpClaimProcessorService() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Page<OMPProcessOmpClaimProcessorTableDTO> search(
			OMPProcessOmpClaimProcessorFormDto formDTO, String userName, String passWord) {
		
		List<OMPClaim> listIntimations = new ArrayList<OMPClaim>(); 
		try{
		String intimationNo =  null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().isEmpty() ? formDTO.getIntimationNo() :null;
		String policyNo =  null != formDTO.getPolicyno() && !formDTO.getPolicyno().isEmpty() ? formDTO.getPolicyno() : null;
		String classification = formDTO.getClassification() != null ? formDTO.getClassification().getValue() != null ? formDTO.getClassification().getValue(): null : null;
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OMPClaim> criteriaQuery = criteriaBuilder.createQuery(OMPClaim.class);
		
		Root<OMPClaim> root = criteriaQuery.from(OMPClaim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		Predicate condition1 = criteriaBuilder.like(root.<OMPIntimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(policyNo != null){
		Predicate condition2 = criteriaBuilder.like(root.<OMPIntimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
		conditionList.add(condition2);
		}
		if(classification!=null){
			List<Long> claimbyClassification = ompIntimationService.getClaimbyClassification(classification);
			Expression<Long> exp = root.get("key");
			Predicate predicate = exp.in(claimbyClassification);
			conditionList.add(predicate);
		}
		
		Predicate condition3 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"), ReferenceTable.OMP_ACKNOWLEDGE_STATUS_KEY);
		conditionList.add(condition3);
		/*Predicate condition3 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.OMP_REGISTRATION_REJECTED);
		conditionList.add(condition3);
		
//		Predicate condition4 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
//		conditionList.add(condition4);
		
		Predicate condition5 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
		conditionList.add(condition5);*/
		
		//IMSSUPPOR-28836
		/*Predicate condition6 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_CLOSED_STATUS);
		conditionList.add(condition6);*/
				
		if(intimationNo == null && policyNo == null && classification==null){
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<OMPClaim> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = formDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 0;
		}

		if(intimationNo == null && policyNo == null && classification==null){
		listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listIntimations = typedQuery.getResultList();
		}
//		for(Intimation inti:listIntimations){
//			System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
//		}
		List<OMPClaim> doList = listIntimations;
		
//		List<OMPReceiptofDocumentsAndBillEntryTableDTO> tableDTO = OMPReceiptofDocumentsAndBillEntryMapper.getInstance().getIntimationDTO(doList);
		List<OMPProcessOmpClaimProcessorTableDTO> tableDTO = OMPProcessOmpClaimProcessorMapper.getInstance().getOMPProcessOmpClaimProcessorTableDTOForIntimation(doList);
	
		for(OMPProcessOmpClaimProcessorTableDTO billEntryDto : tableDTO){
			if(billEntryDto != null){
				billEntryDto.setEventcode(billEntryDto.getEventcode()+'-'+ billEntryDto.getEventDesc());
				billEntryDto.setUserName(userName);
				
				if(billEntryDto.getNonHospitalizationFlag() != null && billEntryDto.getNonHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG) && billEntryDto.getLossDetail() != null){
					billEntryDto.setAilment(billEntryDto.getLossDetail());
				}
			}
			
		}
		
//		List<OMPReceiptofDocumentsAndBillEntryTableDTO> result = new ArrayList<OMPReceiptofDocumentsAndBillEntryTableDTO>();
		List<OMPProcessOmpClaimProcessorTableDTO> result = new ArrayList<OMPProcessOmpClaimProcessorTableDTO>();
		result.addAll(tableDTO);
//		Page<OMPReceiptofDocumentsAndBillEntryTableDTO> page = new Page<OMPReceiptofDocumentsAndBillEntryTableDTO>();
		Page<OMPProcessOmpClaimProcessorTableDTO> page = new Page<OMPProcessOmpClaimProcessorTableDTO>();
		formDTO.getPageable().setPageNumber(pageNumber+1);
		page.setHasNext(true);
		if(result.isEmpty()){
			formDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
	}
		
	return null;
	}
		
	/*	try 
		{
			String strIntimationNo = "";
			
			String strPolicyNo = "";
			
			String classification = formDTO.getClassification() != null ? formDTO.getClassification().getValue() != null ? formDTO.getClassification().getValue(): null : null;
			
			List<Map<String, Object>> taskProcedure = null;
			
			Long rodKey = null;
			Long ackKey = null;
		//	HumanTask humanTaskDTO = null;
			List<String> intimationNoList = new ArrayList<String>();
			List<Long> rodKeyList = new ArrayList<Long>();
			List<Long> ackKeyList = new ArrayList<Long>();
			Integer totalRecords = 0; 
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.ZMR_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.USER_ID,userName);
						
			if(null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().equalsIgnoreCase(""))
			{
				strIntimationNo = formDTO.getIntimationNo(); 
				mapValues.put(SHAConstants.INTIMATION_NO,strIntimationNo);
			}
			if(null != formDTO.getPolicyno() && !formDTO.getPolicyno().equalsIgnoreCase(""))
			{
				
				strPolicyNo = formDTO.getPolicyno();
				
				mapValues.put(SHAConstants.POLICY_NUMBER,strPolicyNo);
			}
			
			if(classification != null && ! classification.isEmpty() && !(SHAConstants.ALL).equalsIgnoreCase(classification)){
				mapValues.put(SHAConstants.RECORD_TYPE, classification);
			}
			List<OMPProcessOmpClaimProcessorTableDTO> searchProcessRejectionTableDTOForIntimation = new ArrayList<OMPProcessOmpClaimProcessorTableDTO>();
			Page<OMPProcessOmpClaimProcessorTableDTO> page = new Page<OMPProcessOmpClaimProcessorTableDTO>();

			Pageable pageable = formDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
				
			Object[] setMapValues = SHAUtils.setOMPObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.getOMPTaskProcedure(setMapValues);	
			
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			if (null != taskProcedure) {
				for (Map<String, Object> outPutArray : taskProcedure) {							
						Long keyValue = (Long) outPutArray.get(SHAConstants.ROD_KEY);
						String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
						Long ackKeyValue = (Long)  outPutArray.get(SHAConstants.PAYLOAD_ACK_KEY);
						workFlowMap.put(keyValue,outPutArray);
						intimationNoList.add(intimationNumber);
						rodKeyList.add(keyValue);
						ackKeyList.add(ackKeyValue);
					
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
				
			List<OMPProcessOmpClaimProcessorTableDTO> tableDTO = new ArrayList<OMPProcessOmpClaimProcessorTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				strIntimationNo = intimationNoList.get(index);
				 
				// humanTaskDTO = humanTaskListDTO.get(index);
				if(index < rodKeyList.size()){
				 rodKey = rodKeyList.get(index);
				 ackKey = ackKeyList.get(index);
				// Integer taskNo = taskNumber.get(index);
				 tableDTO.addAll(getIntimationData(strIntimationNo, rodKey, humanTaskDTO,ackKey,taskNo,userName ,workFlowMap));
				 
				}
			}
			
			
			
			Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
			List<Long> preauthKeys = new ArrayList<Long>();
			List<Long> intimationKeys = new ArrayList<Long>();
			Integer totalRecords = 0;
			if (null != taskParamObjList) {
				for (Map<String, Object> outPutArray : taskParamObjList) {
					Long preauthKeyValue = (Long) outPutArray.get(SHAConstants.CASHLESS_KEY);
					Long inimationKeyValue = (Long) outPutArray.get(SHAConstants.INTIMATION_KEY);
					
					if(preauthKeyValue != null){
						preauthKeys.add(preauthKeyValue);
						if(null != inimationKeyValue){
							intimationKeys.add(inimationKeyValue);
						}					
					}
					else{
						if(null != inimationKeyValue){
							intimationKeys.add(inimationKeyValue);
						}
					}
						
						
					workFlowMap.put(inimationKeyValue,outPutArray);
					totalRecords = (Integer) outPutArray
							.get(SHAConstants.TOTAL_RECORDS);
				}
			}
			
			
				//intimationKeys.
				 if (null != intimationNoList && 0!= intimationNoList.size()) 
			 	{
					List<OMPIntimation> resultList = new ArrayList<OMPIntimation>();
					resultList = SHAUtils.getIntimationInformation(SHAConstants.INTIMATION_SEARCH, entityManager, intimationNoList);
					List<OMPIntimation> pageItemList = resultList;
					
					OMPProcessOmpClaimProcessorMapper.getInstance();
					/*for (Intimation intimation : pageItemList) {
						OMPProcessOmpClaimProcessorTableDTO OMPProcessOmpClaimProcessorTableDTO = new OMPProcessOmpClaimProcessorTableDTO();
						OMPProcessOmpClaimProcessorTableDTO.setIntimationNo(intimation.getIntimationId());
						searchProcessRejectionTableDTOForIntimation.add(OMPProcessOmpClaimProcessorTableDTO);
					}
					searchProcessRejectionTableDTOForIntimation  = OMPProcessOmpClaimProcessorMapper.getOMPProcessOmpClaimProcessorTableDTOForIntimation(pageItemList);
					
			 	}
				
					for (OMPProcessOmpClaimProcessorTableDTO objSearchProcessRejectionTableDTO :searchProcessRejectionTableDTOForIntimation)
					{
						
						objSearchProcessRejectionTableDTO.setDbOutArray(workFlowMap.get(objSearchProcessRejectionTableDTO.getKey()));
						
//						objSearchProcessRejectionTableDTO.setHumanTask(humanTaskMap.get(objSearchProcessRejectionTableDTO.getKey()));
//						objSearchProcessRejectionTableDTO.setTaskNumber(taskNumberMap.get(objSearchProcessRejectionTableDTO.getKey()));

						if((null != searchProcessRejectionTableDTOForIntimation && 0 != searchProcessRejectionTableDTOForIntimation.size()))
						{
							for(OMPProcessOmpClaimProcessorTableDTO objProcessRejectionForPreauthTableDTO :searchProcessRejectionTableDTOForIntimation)
							{
								if((objProcessRejectionForPreauthTableDTO.getIntimationNo().equalsIgnoreCase(objSearchProcessRejectionTableDTO.getIntimationNo())))
								{
									objSearchProcessRejectionTableDTO.setPreauthStatus(objProcessRejectionForPreauthTableDTO.getPreauthStatus());
								}
							}
						}
					}
					
		   // Page<Intimation> pagedList = super.pagedList(formDTO.getPageable());
			page.setPageItems(tableDTO);
			page.setTotalRecords(totalRecords.intValue());
			
//			if(tasks != null && tasks.getIsNextPageAvailable() || nonMedicalTask != null && nonMedicalTask.getIsNextPageAvailable()){
//				page.setHasNext(true);
//			}
			
			if(formDTO.getPageable() != null){
			page.setPagesAvailable(super.pagedList(formDTO.getPageable()).getPagesAvailable());
			}
			
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	

	@SuppressWarnings("unchecked")
	public Preauth getProcessRejectionKey(Long processRejectionKey) {
		
		Query findByKey = entityManager.createNamedQuery("Preauth.findAll");

		List<Preauth> processRejectionList = (List<Preauth>) findByKey
				.getResultList();

		if (!processRejectionList.isEmpty()) {
			return processRejectionList.get(0);

		}
		return null;
	}

/*	@Override
	public Class<Preauth> getDTOClass() {
		return Preauth.class;
	}
*/
	
	@Override
	public Class<OMPIntimation> getDTOClass() {
		return OMPIntimation.class;
	}
	
	
	
/*	private List<OMPProcessOmpClaimProcessorTableDTO> getIntimationData(String intimationNo, Long rodKey, HumanTask humanTask, Long ackKey,Integer taskNumber,String userName, Map<Long, Object> workFlowMap ){
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OMPIntimation> criteriaQuery = criteriaBuilder.createQuery(OMPIntimation.class);
		
		List<OMPIntimation> intimationsList = new ArrayList<OMPIntimation>(); 
		
		Root<OMPIntimation> root = criteriaQuery.from(OMPIntimation.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(!intimationNo.isEmpty() || intimationNo != null){
			Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		final TypedQuery<OMPIntimation> typedQuery = entityManager.createQuery(criteriaQuery);
		intimationsList = typedQuery.getResultList();
		}
		
			List<OMPIntimation> doList = intimationsList;
			List<OMPProcessOmpClaimProcessorTableDTO> tableDTO = OMPProcessOmpClaimProcessorMapper.getInstance().getOMPProcessOmpClaimProcessorTableDTOForIntimation(doList);
			for (OMPProcessOmpClaimProcessorTableDTO ompProcessOmpClaimProcessorTableDTO : tableDTO) {
				ompProcessOmpClaimProcessorTableDTO.setDbOutArray(workFlowMap.get(rodKey));
				ompProcessOmpClaimProcessorTableDTO.setUserName(userName);
				OMPReimbursement reimbursement = ompIntimationService.getReimbursementByKey(rodKey);
				if(reimbursement != null){
					ompProcessOmpClaimProcessorTableDTO.setRodnumber(reimbursement.getRodNumber());
					if(reimbursement.getClassificationId()!=null){
						ompProcessOmpClaimProcessorTableDTO.setClassification(reimbursement.getClassificationId().getValue());
					}
					if(reimbursement.getSubClassificationId()!=null){
					ompProcessOmpClaimProcessorTableDTO.setSubClassification(reimbursement.getSubClassificationId().getValue());
					}
				}
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			tableDTO = getclaimNumber(tableDTO,rodKey);
		return tableDTO;
		}catch(Exception e){
			return null;
		}
	}*/
	
	
	private List<OMPProcessOmpClaimProcessorTableDTO> getclaimNumber(List<OMPProcessOmpClaimProcessorTableDTO> tableDTO, Long rodKey){
		OMPClaim a_claim = null;
		for(int index = 0; index < tableDTO.size(); index++){
			System.out.println("Intimationkey+++++++++++++++++++++"+tableDTO.get(index).getKey());
			tableDTO.get(index).setRodkey(rodKey);
			if (tableDTO.get(index).getKey() != null) {

				Query findByIntimationKey = entityManager
						.createNamedQuery("OMPClaim.findByOMPIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter(
						"intimationKey", tableDTO.get(index).getKey());
				findByIntimationKey.setParameter("lobId",ReferenceTable.OMP_LOB_KEY);
				
				try{
						a_claim = (OMPClaim) findByIntimationKey.getSingleResult();
						if(a_claim != null){
							tableDTO.get(index).setClaimno(a_claim.getClaimId());
//							tableDTO.get(index).setClaimType(a_claim.getClaimType().getValue());
							tableDTO.get(index).setClaimKey(a_claim.getKey());
						}else{
							tableDTO.get(index).setClaimno("");
						}
						
				}catch(Exception e){
					continue;
				}
			} 
		}
		return tableDTO;
	}
	
}
