package com.shaic.reimbursement.manageclaim.SearchUpdateRodDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
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
import com.shaic.arch.table.Pageable;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODMapper;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;


@Stateless
public class SearchUpdateRodDetailsService extends AbstractDAO<Intimation>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private ClaimService claimService;
	
	Map<Long, Object> workFlowMap= null;
	
	public SearchUpdateRodDetailsService(){
		super();
	}
	
	public Page<SearchCreateRODTableDTO> search(
			SearchUpdateRodDetailsFormDTO searchFormDTO,String userName,String password){
	
	
			try{
				String intimationNo =   null != searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null ;
				String policyNo =  null != searchFormDTO && null !=  searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
				String cpuCode =  null != searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString(): null : null;
				String priority =  null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
				String source =  null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
				String type =  null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
				String priorityNew = searchFormDTO.getPriorityNew() != null ? searchFormDTO.getPriorityNew().getValue() != null ? searchFormDTO.getPriorityNew().getValue() : null : null;
				
				
				
				String docUpload = null;
				Long ackKey = null;
				List<Map<String, Object>> taskProcedure = null ;
				Map<String, Object> mapValues = new WeakHashMap<String, Object>();
				
				workFlowMap= new HashMap<Long, Object>();
				
				
				
				Integer totalRecords = 0; 
				
				mapValues.put(SHAConstants.USER_ID, userName);
				mapValues.put(SHAConstants.RECORD_TYPE, SHAConstants.RECONSIDERATION);
				mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PAYMENT_REVERSE_FEED_CURRENT_QUEUE);
				
				/*mapValues.put(SHAConstants.LOB, SHAConstants.HEALTH_LOB);
				mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.HEALTH_LOB_TYPE);*/
				
				
				if(null != searchFormDTO &&  null != searchFormDTO.getIsDocumentUploaded() && null != searchFormDTO.getIsDocumentUploaded().getValue())
				{
					docUpload = searchFormDTO.getIsDocumentUploaded().getValue().trim();
				}
		
				
				List<String> intimationNoList = new ArrayList<String>();
		
				List<Long> ackKeyList = new ArrayList<Long>();

				
				if(null != intimationNo && !intimationNo.isEmpty()){
					
					mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);			
		
				
				}
				
				if(null != policyNo && !policyNo.isEmpty()){
								
					mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
					
				}	
				
		
				if(null != cpuCode){			
					mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				}	
			
				
				if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){			
					
					if(priority != null && ! priority.isEmpty())
						if(!priority.equalsIgnoreCase(SHAConstants.ALL)){
							mapValues.put(SHAConstants.PRIORITY, priority);
						}
					
					if(source != null && ! source.isEmpty()){
						mapValues.put(SHAConstants.STAGE_SOURCE, source);	
					}
					
					if(type != null && ! type.isEmpty()){
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
		

				
				Pageable pageable = searchFormDTO.getPageable();
				if(pageable == null){
					pageable = new Pageable();
				}
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 20);
	
				
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
				
					if (null != taskProcedure) {
						for (Map<String, Object> outPutArray : taskProcedure) {							
								Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ACK_KEY);
								String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
								workFlowMap.put(keyValue,outPutArray);
								intimationNoList.add(intimationNumber);
								ackKeyList.add(keyValue);
							
							totalRecords = (Integer) outPutArray
									.get(SHAConstants.TOTAL_RECORDS);
						}
					}
						
				List<SearchCreateRODTableDTO> tableDTO = new ArrayList<SearchCreateRODTableDTO>();
				for(int index = 0 ; index < intimationNoList.size() ; index++){
					
					 intimationNo = intimationNoList.get(index);

					 if(index < ackKeyList.size()){
					 ackKey = ackKeyList.get(index);

					 tableDTO.addAll(getIntimationData(intimationNo,  ackKey,docUpload));
				}
				}
				
				List<SearchCreateRODTableDTO> ackDocUploadList = new ArrayList<SearchCreateRODTableDTO>();
				List<SearchCreateRODTableDTO> nonAckDocUploadList = new ArrayList<SearchCreateRODTableDTO>();
				if(null != docUpload)
				{	
					for (SearchCreateRODTableDTO searchCreateRODTableDTO2 : tableDTO) {
						if((SHAConstants.YES).equalsIgnoreCase(docUpload) && (SHAConstants.YES).equalsIgnoreCase(searchCreateRODTableDTO2.getIsDocumentUploaded()))
						{
							ackDocUploadList.add(searchCreateRODTableDTO2);
						}
						if((SHAConstants.No).equalsIgnoreCase(docUpload) && (SHAConstants.No).equalsIgnoreCase(searchCreateRODTableDTO2.getIsDocumentUploaded()))
								{
									nonAckDocUploadList.add(searchCreateRODTableDTO2);
						}
					}
				}
								
				Page<SearchCreateRODTableDTO> page = new Page<SearchCreateRODTableDTO>();
				
				SHAUtils.setClearReferenceData(mapValues);
				SHAUtils.setClearMapValue(workFlowMap);
				
				if(null != docUpload && (SHAConstants.YES).equalsIgnoreCase(docUpload))
				{
					page.setPageItems(ackDocUploadList);
				}
				else if(null != docUpload && (SHAConstants.No).equalsIgnoreCase(docUpload))
				{
					page.setPageItems(nonAckDocUploadList);
				}
				else
				{
					page.setPageItems(tableDTO);
				}

				    page.setTotalRecords(totalRecords);
				return page;
			}
		
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;	
	

//		List<DocAcknowledgement> reimbursementList = new ArrayList<DocAcknowledgement>();
		/*try{
			String intimationNo =   null != searchFormDTO && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null ;
			String policyNo =  null != searchFormDTO && null !=  searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			String cpuCode =  null != searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString(): null : null;
			String type =  null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<DocAcknowledgement> criteriaQuery = criteriaBuilder.createQuery(DocAcknowledgement.class);
			
			Root<DocAcknowledgement> root = criteriaQuery.from(DocAcknowledgement.class);

			List<Predicate> conditionList = new ArrayList<Predicate>();
			if (intimationNo != null) {
				Predicate condition1 = criteriaBuilder.like(root.<Claim> get("claim").<Intimation> get("intimation").<String> get("intimationId"), "%"+ intimationNo + "%");
				conditionList.add(condition1);
			}
			if (policyNo != null) {
				Predicate condition2 = criteriaBuilder.like(root.<Claim> get("claim").<Intimation> get("intimation").<Policy> get("policy").<String> get("policyNumber"), "%" + policyNo+ "%");
				conditionList.add(condition2);
			}
			
			Predicate condition5 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"),ReferenceTable.PAYMENT_CANCEL_UPDATED);
			conditionList.add(condition5);
			
			Predicate condition6 = criteriaBuilder.like(root.<String>get("paymentCancellationFlag"), SHAConstants.YES_FLAG);
			conditionList.add(condition6);
			
			Predicate condition7 = criteriaBuilder.like(root.<String>get("reconsiderationRequest"), SHAConstants.YES_FLAG);
			conditionList.add(condition7);

			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			
			final TypedQuery<DocAcknowledgement> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			@SuppressWarnings("unused")
			int firtResult;
			
			if (pageNumber > 1) {
				firtResult = (pageNumber - 1) * 10;
			} 
			else {
				firtResult = 1;
			}
			
			reimbursementList = typedQuery.getResultList();
			
			List<String> intimationNoList = new ArrayList<String>();
			
			List<Long> ackKeyList = new ArrayList<Long>();
			Long ackKey = null;
			for (DocAcknowledgement docAckDtls : reimbursementList) {
				Claim clmDtls = claimService.getClaimByClaimKey(docAckDtls.getClaim().getKey());
				String intimationNumber = clmDtls.getIntimation().getIntimationId();
				intimationNoList.add(intimationNumber);
				ackKeyList.add(docAckDtls.getKey());
			}
			List<SearchCreateRODTableDTO> tableDTO = new ArrayList<SearchCreateRODTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				 intimationNo = intimationNoList.get(index);

				 if(index < ackKeyList.size()){
				 ackKey = ackKeyList.get(index);

				 tableDTO.addAll(getIntimationData(intimationNo,  ackKey,null));
			}
			}
			
			if(cpuCode != null){
				List<SearchCreateRODTableDTO> filterByCpu = new ArrayList<SearchCreateRODTableDTO>();
				for (SearchCreateRODTableDTO searchCreateRODTableDTO : tableDTO) {
					if(searchCreateRODTableDTO.getCpuCode().toString().equals(cpuCode)){
						filterByCpu.add(searchCreateRODTableDTO);
					}
				}
				tableDTO = filterByCpu;
			}
			
			if(type != null){
				if(!type.equalsIgnoreCase(SHAConstants.RECONSIDERATION)){
					tableDTO = null;
				}
				
			}
			
			Page<SearchCreateRODTableDTO> page = new Page<SearchCreateRODTableDTO>();
	
				page.setPageItems(tableDTO);

			    page.setTotalRecords(tableDTO.size());
			return page;
		}
	
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
		return null;*/	
			
	}
	
	@SuppressWarnings("unused")
	private List<SearchCreateRODTableDTO> getIntimationData(String intimationNo, Long ackKey,String docUpload){
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
		
		Root<Intimation> root = criteriaQuery.from(Intimation.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
			try{
				
			if(intimationNo != null || (intimationNo != null && !intimationNo.isEmpty())){
			Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}else {
					return null;
				}
			
				final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
				List<Intimation> listIntimations = new ArrayList<Intimation>(); 
				listIntimations = typedQuery.getResultList();
			
				for(Intimation inti:listIntimations){
					System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
				}

				List<SearchCreateRODTableDTO> tableDTO = SearchCreateRODMapper.getInstance().getIntimationDTO(listIntimations);
				for (SearchCreateRODTableDTO searchCreateRODTableDTO : tableDTO) {
					
					if(searchCreateRODTableDTO.getIntimationNo() != null){
						Intimation intimationByNo = getIntimationByNo(searchCreateRODTableDTO.getIntimationNo());
						/*if(intimationByNo.getPolicy().getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)){
							String colorCodeForGMC = getColorCodeForGMC(intimationByNo.getPolicy().getPolicyNumber(), intimationByNo.getInsured().getInsuredId().toString());
							searchCreateRODTableDTO.setColorCode(colorCodeForGMC);
						}*/
					}
					Claim claimByKey = getClaimByIntimation(searchCreateRODTableDTO.getKey());
					searchCreateRODTableDTO.setCrmFlagged(claimByKey.getCrcFlag());
					if(searchCreateRODTableDTO.getCrmFlagged() != null){
						if(searchCreateRODTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							searchCreateRODTableDTO.setColorCodeCell("OLIVE");
							searchCreateRODTableDTO.setCrmFlagged(null);
						}
					}
					
					if(searchCreateRODTableDTO.getCpuCode() != null){
						Long cpuCode = searchCreateRODTableDTO.getCpuCode();
						searchCreateRODTableDTO.setStrCpuCode(String.valueOf(cpuCode));
					}
					AcknowledgeDocument ackDoc = isDocumentUploadedForAck(ackKey);
					
					
					if(claimByKey != null) {
						searchCreateRODTableDTO.setCoverCode(claimByKey.getClaimCoverCode());
						searchCreateRODTableDTO.setSubCoverCode(claimByKey.getClaimSubCoverCode());
						searchCreateRODTableDTO.setSectionCode(claimByKey.getClaimSectionCode());
					}
					
					/**
					 * Below blockw will be invoked , if document upload
					 * dropdown is selected. 
					 * */
					if(null != docUpload)
					{
						if(null != ackDoc && (SHAConstants.YES).equalsIgnoreCase(docUpload))
							{ 
								searchCreateRODTableDTO.setAckDocKey(ackDoc.getKey());
								if(null != ackDoc.getDocAcknowledgement())
								searchCreateRODTableDTO.setDocAcknowledgementKey(ackDoc.getDocAcknowledgement().getKey());
								searchCreateRODTableDTO.setIsDocumentUploaded(SHAConstants.YES);
							}
						else if(null == ackDoc  && (SHAConstants.No).equalsIgnoreCase(docUpload))
							searchCreateRODTableDTO.setIsDocumentUploaded(SHAConstants.No);
						//uploadedAckDocumentList.add(searchCreateRODTableDTO);
					}
					
					/**
					 * Below blockw will be invoked , if document upload
					 * dropdown is not selected.
					 * */
					
					else if(null != ackDoc)
					{
						searchCreateRODTableDTO.setAckDocKey(ackDoc.getKey());
						if(null != ackDoc.getDocAcknowledgement())
						searchCreateRODTableDTO.setDocAcknowledgementKey(ackDoc.getDocAcknowledgement().getKey());
						searchCreateRODTableDTO.setIsDocumentUploaded(SHAConstants.YES);
					}
					else if (null == ackDoc)
					{
						searchCreateRODTableDTO.setIsDocumentUploaded(SHAConstants.No);
					}
					
				}

					tableDTO = getclaimNumber(tableDTO);
					tableDTO = getHospitalDetails(tableDTO, ackKey/*, humanTask*//*,taskNumber*/);
					return tableDTO;
			
			}catch(Exception e){
			return null;
		}	
	}
	
	private List<SearchCreateRODTableDTO> getclaimNumber(List<SearchCreateRODTableDTO> intimationList){
		Claim a_claim = null;
		for(int index = 0; index < intimationList.size(); index++){
			System.out.println("Intimationkey+++++++++++++++++++++"+intimationList.get(index).getKey());
			
			if (intimationList.get(index).getKey() != null) {

				Query findByIntimationKey = entityManager
						.createNamedQuery("Claim.findByIntimationKey");
				findByIntimationKey = findByIntimationKey.setParameter(
						"intimationKey", intimationList.get(index).getKey());
				try{
						a_claim = (Claim) findByIntimationKey.getSingleResult();
					
						if(a_claim != null){
							entityManager.refresh(a_claim);
							intimationList.get(index).setClaimNo(a_claim.getClaimId());
							intimationList.get(index).setClaimType(a_claim.getClaimType().getValue());
							intimationList.get(index).setClaimkey(a_claim.getKey());
							intimationList.get(index).setStatus(a_claim.getStatus().getProcessValue());
							intimationList.get(index).setAccidentOrDeath(a_claim.getIncidenceFlag());
						}else{
							intimationList.get(index).setClaimNo("");
						}	
				}catch(Exception e){
					continue;
				}
			} 
		}
		return intimationList;
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
		} else {
			// intimationKey null
		}
		return a_claimList.get(0);

	}
	
	private List<SearchCreateRODTableDTO> getHospitalDetails(
			List<SearchCreateRODTableDTO> tableDTO, Long ackKey) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
	
			tableDTO.get(index).setAckNo(ackKey);

			
			if(ackKey != null){
				DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(ackKey);
				String acknowledgeNumber = docAcknowledgement.getAcknowledgeNumber();
				tableDTO.get(index).setAcknowledgementNumber(acknowledgeNumber);
				tableDTO.get(index).setRodKey(docAcknowledgement.getRodKey());
				
			}
			
			Object workflowKey =  workFlowMap.get(ackKey);
			tableDTO.get(index).setDbOutArray(workflowKey);
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
			 }
			}catch(Exception e){
				continue;
			}
		}
		
		return tableDTO;
	}
	
	public DocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
		DocAcknowledgement docAcknowledgement = null;
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query = query.setParameter("ackDocKey", docAckKey);
		if (null != query) {
			docAcknowledgement = (DocAcknowledgement) query.getSingleResult();
		}
		return docAcknowledgement;
	}
	
	private AcknowledgeDocument isDocumentUploadedForAck(Long docAckKey)
	{
		Query query = entityManager.createNamedQuery("AcknowledgeDocument.findByDocAcknowledgementKey");
		query = query.setParameter("docAckKey", docAckKey);
		List<AcknowledgeDocument> ackDocList = query.getResultList();
		if(null != ackDocList && !ackDocList.isEmpty())
		{
			entityManager.refresh(ackDocList.get(0));
			return ackDocList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
