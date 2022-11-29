package com.shaic.claim.OMPProcessNegotiation.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import com.shaic.arch.table.Pageable;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

@Stateless
public class OMPProcessNegotiationService extends AbstractDAO<OMPIntimation>{
	
	
	@EJB
	private OMPIntimationService ompIntimationService;
	
	public OMPProcessNegotiationService() {
		super();
	}
	@EJB
	private OMPIntimationService service;
	
	
	@SuppressWarnings("unchecked")
	public Page<OMPProcessNegotiationTableDTO> search(
			OMPProcessNegotiationFormDto formDTO, String userName, String passWord) {
//		try 
//		{
			String strIntimationNo = "";
			
			String strClaimNo = "";
			
//			String classification = formDTO.getClassification() != null ? formDTO.getClassification().getValue() != null ? formDTO.getClassification().getValue(): null : null;
			
			List<Map<String, Object>> taskProcedure = null;
			
			Long rodKey = null;
			Long ackKey = null;
			
			List<String> intimationNoList = new ArrayList<String>();
			List<Long> rodKeyList = new ArrayList<Long>();
			List<Long> ackKeyList = new ArrayList<Long>();
			Integer totalRecords = 0;
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.OMP_PROCESS_NEGOTIATION_CURRENT_QUEUE);
			
			mapValues.put(SHAConstants.USER_ID,userName);
						
			if(null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().equalsIgnoreCase(""))
			{
				strIntimationNo = formDTO.getIntimationNo(); 
				mapValues.put(SHAConstants.INTIMATION_NO,strIntimationNo);
			}
			if(null != formDTO.getClaimno() && !formDTO.getClaimno().equalsIgnoreCase(""))
			{
				
				strClaimNo = formDTO.getClaimno();
				
				mapValues.put(SHAConstants.CLAIM_NUMBER,strClaimNo);
			}
			
			List<OMPProcessNegotiationTableDTO> searchProcessRejectionTableDTOForIntimation = new ArrayList<OMPProcessNegotiationTableDTO>();
			Page<OMPProcessNegotiationTableDTO> page = new Page<OMPProcessNegotiationTableDTO>();

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
				
			List<OMPProcessNegotiationTableDTO> tableDTO = new ArrayList<OMPProcessNegotiationTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				strIntimationNo = intimationNoList.get(index);
				
				// humanTaskDTO = humanTaskListDTO.get(index);
				if(index < rodKeyList.size()){
				 rodKey = rodKeyList.get(index);
				 ackKey = ackKeyList.get(index);
				// Integer taskNo = taskNumber.get(index);
				 tableDTO.addAll(getIntimationData(strIntimationNo, rodKey,/* humanTaskDTO,*/ackKey,/*taskNo,*/userName,workFlowMap));
				}
			}
	
			
			page.setPageItems(tableDTO);
			page.setTotalRecords(totalRecords.intValue());
			
//			if(tasks != null && tasks.getIsNextPageAvailable() || nonMedicalTask != null && nonMedicalTask.getIsNextPageAvailable()){
//				page.setHasNext(true);
//			}
			
//			if(formDTO.getPageable() != null){
//			page.setPagesAvailable(super.pagedList(formDTO.getPageable()).getPagesAvailable());
//			}
			
//			return page;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return page;
	}

	private List<OMPProcessNegotiationTableDTO> getIntimationData(String intimationNo, Long rodKey,/* HumanTask humanTask,*/ Long ackKey,/*Integer taskNumber,*/String userName, Map<Long, Object> workFlowMap){
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
	
		
			for(OMPIntimation inti:intimationsList){
				System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			List<OMPIntimation> doList = intimationsList;
			List<OMPProcessNegotiationTableDTO> tableDTO = OMPProcessNegotiationMapper.getInstance().getOMPProcessNegotiationTableDTOForIntimation(doList);
			
			for(int index = 0; index < tableDTO.size(); index++){										//
				System.out.println("Intimationkey+++++++++++++++++++++"+tableDTO.get(index).getKey());	//
			
			OMPIntimation intimation = service.getIntimationByKey(tableDTO.get(index).getKey());	//
			
			NewIntimationDto intimationDto=service.getIntimationDto(intimation);					//
			if(intimationDto != null){																//
				tableDTO.get(index).setEventcode(intimationDto.getEventCodeValue());				//
//			OMPIntimation intimation = getNotRegisteredIntimation(tableDTO.getIntimationNo());
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
				for (OMPProcessNegotiationTableDTO ompProcessOmpClaimProcessorTableDTO : tableDTO) {
					ompProcessOmpClaimProcessorTableDTO.setDbOutArray(workFlowMap.get(rodKey));
					ompProcessOmpClaimProcessorTableDTO.setUserName(userName);
					
					OMPReimbursement reimbursement = ompIntimationService.getReimbursementByKey(rodKey);
					if(reimbursement != null){
						ompProcessOmpClaimProcessorTableDTO.setRodnumber(reimbursement.getRodNumber());
//						ompProcessOmpClaimProcessorTableDTO.setClassification(reimbursement.getClassificationId().getValue());
//						ompProcessOmpClaimProcessorTableDTO.setSubClassification(reimbursement.getSubClassificationId().getValue());
					}
					
				}	
			tableDTO = getclaimNumber(tableDTO,rodKey);
			}
			}
		return tableDTO;
			
			
		}catch(Exception e){
			return null;
		
		}
	}
	
	
	private List<OMPProcessNegotiationTableDTO> getclaimNumber(List<OMPProcessNegotiationTableDTO> tableDTO , Long rodKey){
		OMPClaim a_claim = null;
		for(int index = 0; index < tableDTO.size(); index++){
			System.out.println("Intimationkey+++++++++++++++++++++"+tableDTO.get(index).getKey());
			tableDTO.get(index).setRodKey(rodKey);
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
	
	
	
	
	
	@Override
	public Class<OMPIntimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
