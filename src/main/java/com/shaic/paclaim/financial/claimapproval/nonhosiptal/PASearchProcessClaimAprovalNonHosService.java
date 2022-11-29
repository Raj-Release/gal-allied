/**
 * 
 */
package com.shaic.paclaim.financial.claimapproval.nonhosiptal;

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
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasPaClaimCovers;
import com.shaic.domain.PAAdditionalCovers;
import com.shaic.domain.PABenefitsCovers;
import com.shaic.domain.PAOptionalCover;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.OptionalCoversDTO;
import com.shaic.paclaim.billing.processclaimbilling.page.billreview.TableBenefitsDTO;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingFormDTO;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingMapper;
import com.shaic.reimbursement.billing.processclaimbilling.search.SearchProcessClaimBillingTableDTO;

/**
 * @author ntv.narenj
 *
 */
@Stateless
public class PASearchProcessClaimAprovalNonHosService extends AbstractDAO<Intimation>{

	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public String userName;
	
	public PASearchProcessClaimAprovalNonHosService() {
		super();
	}

	public  Page<SearchProcessClaimBillingTableDTO> search(
			SearchProcessClaimBillingFormDTO searchFormDTO,
			String userName, String passWord) {
		try{
			String intimationNo =  null != searchFormDTO && null!= searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			this.userName = userName;
			String policyNo = null != searchFormDTO && null!= searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			String cpuCode = null != searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString() : null : null;
			String producetName = null != searchFormDTO && null != searchFormDTO.getProductNameCode() ? searchFormDTO.getProductNameCode().getValue() : null;
			String productId = null != searchFormDTO && null != searchFormDTO.getProductNameCode() ? searchFormDTO.getProductNameCode().getId() != null ? searchFormDTO.getProductNameCode().getId().toString() : null : null; 
			
			String priority = null != searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null != searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null != searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
		
			String covers = null != searchFormDTO && null != searchFormDTO.getCoverBenefits() ? searchFormDTO.getCoverBenefits().getId() != null ? searchFormDTO.getCoverBenefits().getId().toString() : null : null;
			
			Long rodKey = null;			
			List<String> intimationNoList = new ArrayList<String>();
			List<String> policyNoList = new ArrayList<String>();
			
		
			List<Long> rodKeyList = new ArrayList<Long>();
			
			List<Integer> taskNumber = new ArrayList<Integer>();
			List<Map<String, Object>> taskProcedure = null ;
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();			
			workFlowMap= new WeakHashMap<Long, Object>();
			Integer totalRecords = 0;
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.CURRENT_QUEUE_CLAIM_APPROVAL);
			
			mapValues.put(SHAConstants.LOB, SHAConstants.PA_LOB);
			mapValues.put(SHAConstants.LOB_TYPE, SHAConstants.PA_LOB_TYPE);
			
		
			if(null != intimationNo && !intimationNo.isEmpty()){
				
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);	
				}
			
			
				if(null != policyNo && !policyNo.isEmpty()){
					
					mapValues.put(SHAConstants.POLICY_NUMBER, policyNo);
				}
				
				
				if(null != cpuCode){
					
					mapValues.put(SHAConstants.CPU_CODE, cpuCode);
				}
				
			
				/*if(null != covers && !covers.isEmpty()){
					
					if(payloadBOType == null){
						payloadBOType = new PayloadBOType();
					}
					
					claimType.setCoverBenifitType(covers);
					//payloadBOType.setClaim(claimType);
					payloadBOType.setClaim(claimType);
				}*/
				
			
				if(null != producetName){
					
					String[] split = producetName.split("\\(");
					String productName = split[0];
					
					if(productName != null) {
					//	productName = productName.replaceAll("\\s","");
					
					mapValues.put(SHAConstants.PRODUCT_NAME, productName);	

					}
				
					if(productId != null){
						
						mapValues.put(SHAConstants.PRODUCT_KEY, productId);		
					}				
				}
				
			
				
				if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
				//	classification = new ClassificationType();
					
					if(priority != null && ! priority.isEmpty())
						if(!priority.equalsIgnoreCase(SHAConstants.ALL)){
							//priority = null;
							mapValues.put(SHAConstants.PRIORITY, priority);
						}
					//classification.setPriority(priority);
				
					if(source != null && ! source.isEmpty()){
						//classification.setSource(source);
						mapValues.put(SHAConstants.STAGE_SOURCE, source);	
					}
					
					if(type != null && ! type.isEmpty()){
						if(!type.equalsIgnoreCase(SHAConstants.ALL)){
						//	type = null;
							mapValues.put(SHAConstants.RECORD_TYPE, type);
						}
						//classification.setType(type);
						
					}					
				}

		//	ClaimApprovalTask claimApprovalTask = BPMClientContext.getClaimApprovalTask(userName, passWord);
			
			Pageable pageable = searchFormDTO.getPageable();
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.SEPARATE_ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.SEPARATE_ITEMS_PER_PAGE) : Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE));
			
			Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
			
			Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
			
			DBCalculationService dbCalculationService = new DBCalculationService();
			taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
			
				if (null != taskProcedure) {
					for (Map<String, Object> outPutArray : taskProcedure) {							
							Long keyValue = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);	
							String intimationNumber = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
							workFlowMap.put(keyValue,outPutArray);							
							intimationNoList.add(intimationNumber);
//							Long reimbursementKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_ROD_KEY);							
							rodKeyList.add(keyValue);
						
						totalRecords = (Integer) outPutArray
								.get(SHAConstants.TOTAL_RECORDS);
					}
				}
			List<SearchProcessClaimBillingTableDTO> tableDTO = new ArrayList<SearchProcessClaimBillingTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				 if(index < rodKeyList.size()){
				intimationNo = intimationNoList.get(index);
		
				// humanTaskDTO = humanTaskListDTO.get(index);
				
				 rodKey = rodKeyList.get(index);
				 
				// Integer taskNo = taskNumber.get(index);
				 
				 tableDTO.addAll(getIntimationData(intimationNo,  rodKey/*, humanTaskDTO,taskNo*/));
				}
			}

		
			Page<SearchProcessClaimBillingTableDTO> page = new Page<SearchProcessClaimBillingTableDTO>();
			//page.setPageNumber(taskList.getCurrentPage());
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
	private List<SearchProcessClaimBillingTableDTO> getIntimationData(String intimationNo, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/){
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
		
		List<Intimation> intimationsList = new ArrayList<Intimation>(); 
		
		Root<Intimation> root = criteriaQuery.from(Intimation.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		try{
		if(intimationNo != null || !intimationNo.isEmpty()){
			Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}
		final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
		intimationsList = typedQuery.getResultList();
		
			for(Intimation inti:intimationsList){
				System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			List<Intimation> doList = intimationsList;
			List<SearchProcessClaimBillingTableDTO> tableDTO = SearchProcessClaimBillingMapper.getInstance().getIntimationDTO(doList);
			
			for (SearchProcessClaimBillingTableDTO searchProcessClaimBillingTableDTO : tableDTO) {
				
				if(searchProcessClaimBillingTableDTO.getCpuCode() != null){
					Long cpuCode = searchProcessClaimBillingTableDTO.getCpuCode();
					searchProcessClaimBillingTableDTO.setStrCpuCode(String.valueOf(cpuCode));
				}
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			
			for (SearchProcessClaimBillingTableDTO searchProcessClaimBillingTableDTO : tableDTO) {
				searchProcessClaimBillingTableDTO.setUsername(userName);
			}
			
			tableDTO = getHospitalDetails(tableDTO);
			tableDTO = getclaimNumber(tableDTO, rodKey/*, humanTask,taskNumber*/);
		
		return tableDTO;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	private List<SearchProcessClaimBillingTableDTO> getclaimNumber(List<SearchProcessClaimBillingTableDTO> intimationList, Long rodKey/*, HumanTask humanTask,Integer taskNumber*/){
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
							//intimationList.get(index).setHumanTaskDTO(humanTask);
							intimationList.get(index).setRodKey(rodKey);
							//intimationList.get(index).setTaskNumber(taskNumber);
							Reimbursement reimbursementByKey = getReimbursementByKey(rodKey);
							
							Object workflowKey = workFlowMap.get(intimationList.get(index).getRodKey());
							Map<String, Object> outPutArray = (Map<String, Object>)workflowKey;
							intimationList.get(index).setDbOutArray(workflowKey);
							
							 if(reimbursementByKey.getDocAcknowLedgement() != null && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId() != null
									 && reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue() != null){
								 intimationList.get(index).setDocumentReceivedFrom(reimbursementByKey.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
							 }
							 
							 String benefitAndCover = getCoverValueForViewBasedOnrodKey(intimationList.get(index).getRodKey());
							 if(reimbursementByKey.getDocAcknowLedgement() != null){
								 String benifitFlag = reimbursementByKey.getDocAcknowLedgement().getBenifitFlag();
								 if(null != benefitAndCover)
								 {
									 if(null != benifitFlag)
									 {
										 intimationList.get(index).setCoverBenefits(benifitFlag + "," + benefitAndCover);
									 }
									 else
									 {
										 intimationList.get(index).setCoverBenefits(benefitAndCover);
									 }
								 }
							 }
							 
							intimationList.get(index).setOriginatorID(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setOriginatorName(reimbursementByKey.getModifiedBy());
							intimationList.get(index).setClaimKey(a_claim.getKey());
							intimationList.get(index).setClaimType(a_claim.getClaimType().getValue());
							String type = null != getType(intimationList.get(index).getRodKey()) ? getType(intimationList.get(index).getRodKey()).getStatus().getProcessValue() : "";
							//double claimedAmt = null != getType(intimationList.get(index).getRodKey()) ? getType(intimationList.get(index).getRodKey()).getCurrentProvisionAmt() : 0.0;
							intimationList.get(index).setClaimAmt(getPAClaimedAmount(intimationList.get(index).getRodKey()));
							intimationList.get(index).setType(type);
							if(null != a_claim.getIncidenceFlag())
							{
							if((SHAConstants.ACCIDENT_FLAG).equalsIgnoreCase(a_claim.getIncidenceFlag()))
							{
								intimationList.get(index).setAccidentOrDeath(SHAConstants.ACCIDENT);
							}
							else
							{
								intimationList.get(index).setAccidentOrDeath(SHAConstants.DEATH);
							}
							}
							
						}else{
							intimationList.get(index).setClaimAmt(0.0);
						}	
				}catch(Exception e){
					continue;
				}
			} 
		}
		return intimationList;
	}
	private Reimbursement getType(Long key){
		try{
		Query findType = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", key);
		Reimbursement reimbursement = (Reimbursement) findType.getSingleResult();
		return reimbursement;
		}catch(Exception e){
			
		}
		return null;
	}

	private List<SearchProcessClaimBillingTableDTO> getHospitalDetails(
			List<SearchProcessClaimBillingTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
	
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 ;
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
//				 Long cpuCode = null != getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()) ? getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
//				 String cpuName = null != getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()) ? getTmpCPUCodeAndName(tableDTO.get(index).getCpuId()).getDescription() : "";		
//				 tableDTO.get(index).setCpuName(cpuName);
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	private TmpCPUCode getTmpCPUCodeAndName(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			
		}
		return null;
	}
	private Double getClaimedAmount(Long key) {
		try {
			Double claimedAmount = 0.0;
			Query findType = entityManager.createNamedQuery(
					"DocAcknowledgement.findByLatestAcknowledge").setParameter("rodKey", key);
			List<DocAcknowledgement> reimbursement = (List<DocAcknowledgement>) findType
					.getResultList();
			
			DocAcknowledgement docAcknowledgement = new DocAcknowledgement();
		    if(reimbursement != null && ! reimbursement.isEmpty()){
		    	docAcknowledgement = reimbursement.get(0);
		    }
			
			Query findType1 = entityManager.createNamedQuery(
					"ReimbursementBenefits.findByRodKey").setParameter("rodKey", key);
			List<ReimbursementBenefits> reimbursementBenefitsList = (List<ReimbursementBenefits>) findType1.getResultList();
			Double currentProvisionalAmount = 0.0;
			for(ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsList){
				currentProvisionalAmount += reimbursementBenefits.getTotalClaimAmountBills();
				
			}
			Double hospitalizationClaimedAmount = null != docAcknowledgement.getHospitalizationClaimedAmount() ? docAcknowledgement.getHospitalizationClaimedAmount() : 0.0;
			Double postHospitalizationClaimedAmount = null != docAcknowledgement.getPostHospitalizationClaimedAmount() ? docAcknowledgement.getPostHospitalizationClaimedAmount() : 0.0;
			Double preHospitalizationClaimedAmount = null != docAcknowledgement.getPreHospitalizationClaimedAmount() ? docAcknowledgement.getPreHospitalizationClaimedAmount() : 0.0;
			claimedAmount = hospitalizationClaimedAmount + postHospitalizationClaimedAmount + preHospitalizationClaimedAmount+currentProvisionalAmount;
			
			return claimedAmount;
		} catch (Exception e) {

		}
		return null;
	}
	
	private Double getPAClaimedAmount(Long key) {
		try {
			Double claimedAmount = 0.0;
			Query findType = entityManager.createNamedQuery(
					"DocAcknowledgement.findByLatestAcknowledge").setParameter("rodKey", key);
			List<DocAcknowledgement> reimbursement = (List<DocAcknowledgement>) findType
					.getResultList();
			
			DocAcknowledgement docAcknowledgement = new DocAcknowledgement();
		    if(reimbursement != null && ! reimbursement.isEmpty()){
		    	docAcknowledgement = reimbursement.get(0);
		    }
			
			claimedAmount = getClaimedAmountValue(docAcknowledgement);
			
			return claimedAmount;
		} catch (Exception e) {

		}
		return null;
	}
	
	public Double getClaimedAmountValue(DocAcknowledgement docAck)
	{
		Double totalAmt = 0d;
		Double addOnAmt = 0d;
		Double optionalAmt = 0d;
		Double claimedAmt = 0d;
		
		if(docAck != null)
		{
			if(docAck.getRodKey()!= null)
			{
				List<PAAdditionalCovers> addOnCoversList = getAdditionalCoversForRodAndBillEntry(docAck.getRodKey());
				List<PAOptionalCover> optionalCoversList = getOptionalCoversForRodAndBillEntry(docAck.getRodKey());
				
				if(addOnCoversList != null  && !addOnCoversList.isEmpty())
				{
					for(PAAdditionalCovers paAdditionalCovers : addOnCoversList)
					{
						if(paAdditionalCovers.getClaimedAmount() != null)
						{
						addOnAmt = addOnAmt + paAdditionalCovers.getClaimedAmount();
						}
					}
				}
				
				if(optionalCoversList != null && !optionalCoversList.isEmpty())
				{
					for(PAOptionalCover paOptionalCovers : optionalCoversList)
					{
						if(paOptionalCovers.getClaimedAmount() != null)
						{
							optionalAmt = optionalAmt + paOptionalCovers.getClaimedAmount();
						}
					}
				}
			}
			
			if(docAck.getBenifitClaimedAmount() != null)
			{
				claimedAmt = docAck.getBenifitClaimedAmount();
			}
			
			totalAmt = addOnAmt + optionalAmt + claimedAmt;
		}
		return totalAmt;
	}
	
	public List<PAAdditionalCovers> getAdditionalCoversForRodAndBillEntry(Long rodKey)
	{
		Query query = entityManager.createNamedQuery("PAAdditionalCovers.findByRodKey");
		query = query.setParameter("rodKey",rodKey );
		List<PAAdditionalCovers> additionalCovers = query.getResultList();
		if(null != additionalCovers && !additionalCovers.isEmpty())
		{
			//ntityManager.refresh(additionalCovers);
			return additionalCovers;
		}
		return null;
	}
	
	public List<PAOptionalCover> getOptionalCoversForRodAndBillEntry(Long rodKey)
	{
		Query query = entityManager.createNamedQuery("PAOptionalCover.findByRodKey");
		query = query.setParameter("rodKey",rodKey );
		List<PAOptionalCover> optionalCovers = query.getResultList();
		if(null != optionalCovers && !optionalCovers.isEmpty())
		{
			//ntityManager.refresh(additionalCovers);
			return optionalCovers;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByKey(Long rodKey){
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByKey").setParameter(
						"primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();
		
		if(rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}
	
	public List<TableBenefitsDTO> getPABenefitsListByRodKey(Long rodKey){
		
		Query query = entityManager
				.createNamedQuery("PABenefitsCovers.findByRodKey").setParameter(
						"rodKey", rodKey);
		List<PABenefitsCovers> rodList = (List<PABenefitsCovers>)query.getResultList();
		
		
		
		List<TableBenefitsDTO> listOfValues = new ArrayList<TableBenefitsDTO>();
		
		if(! rodList.isEmpty()){
			for (PABenefitsCovers paBenefitsCovers : rodList) {
				TableBenefitsDTO dto = new TableBenefitsDTO();
				dto.setKey(paBenefitsCovers.getKey());
				dto.setDuration(paBenefitsCovers.getWeeksDuration());
				dto.setBillAmount(paBenefitsCovers.getBillAmount());
				dto.setApprovedAmount(paBenefitsCovers.getApprovedAmount());
				dto.setBillNo(paBenefitsCovers.getBillNo());
				dto.setEligibleAmount(paBenefitsCovers.getEligibleAmount());
				if(null != paBenefitsCovers.getNetAmount()){
					dto.setNetAmount(paBenefitsCovers.getNetAmount());
				}else{
					dto.setNetAmount(0d);
				}
				SelectValue selectValue = new SelectValue();
				if(null != paBenefitsCovers.getCoverId())
				{
					selectValue.setId(paBenefitsCovers.getCoverId().getSubCoverKey());
					selectValue.setValue(paBenefitsCovers.getCoverId().getCoverDescription());
					dto.setClassification(selectValue);
				}
				dto.setReasonForDeduction(paBenefitsCovers.getDeductionReason());
				dto.setDeduction(paBenefitsCovers.getDeductionAmount());
				dto.setBillDate(paBenefitsCovers.getBillDate());
				listOfValues.add(dto);
			}
		}
		return listOfValues;
		
		
		
	}

	public List<OptionalCoversDTO> getPAOptionalCoverListByRodKey(Long rodKey) {
		
		Query query = entityManager
				.createNamedQuery("PAOptionalCover.findByRodKey").setParameter("rodKey", rodKey);
		List<PAOptionalCover> rodList = (List<PAOptionalCover>)query.getResultList();
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<OptionalCoversDTO> listOfValues = new ArrayList<OptionalCoversDTO>();
		if(! rodList.isEmpty()){
			for (PAOptionalCover paOptionalCover : rodList) {
				OptionalCoversDTO optionalCoverDto = new OptionalCoversDTO();
				optionalCoverDto.setKey(paOptionalCover.getKey());
				optionalCoverDto.setRemarks(paOptionalCover.getRemarks());
				optionalCoverDto.setClaimedAmount(paOptionalCover.getClaimedAmount());
				optionalCoverDto.setClaimKey(paOptionalCover.getClaimKey());
				Long coverId = paOptionalCover.getCoverId();
				List<OptionalCoversDTO> optCoverDTOList = dbCalculationService.getOptValuesForMedicalExtension(rodKey,0l);
				if(null != optCoverDTOList && !optCoverDTOList.isEmpty())
				{
					for (OptionalCoversDTO optionalCoversDTO : optCoverDTOList) {
						if(coverId.equals(optionalCoversDTO.getCoverId()))
						{
							optionalCoverDto.setNoOfDaysUtilised(optionalCoversDTO.getNoOfDaysUtilised());
							optionalCoverDto.setNoOfDaysAvailable(optionalCoversDTO.getNoOfDaysAvailable());
							optionalCoverDto.setAllowedAmountPerDay(optionalCoversDTO.getAllowedAmountPerDay());
							optionalCoverDto.setMaxNoOfDaysPerHospital(optionalCoversDTO.getMaxNoOfDaysPerHospital());
							optionalCoverDto.setMaxDaysAllowed(optionalCoversDTO.getMaxDaysAllowed());
							optionalCoverDto.setSiLimit(optionalCoversDTO.getSiLimit());
							optionalCoverDto.setLimit(optionalCoversDTO.getLimit());
							optionalCoverDto.setBalanceSI(optionalCoversDTO.getBalanceSI());
						}
						
					}
				}
				if(coverId!=null){
					MasPaClaimCovers paClaimCover = getPaClaimCover(coverId);
					if(paClaimCover!=null){
						SelectValue selectValue = new SelectValue();
						selectValue.setId(paClaimCover.getCoverKey());
						selectValue.setValue(paClaimCover.getCoverDescription());
						optionalCoverDto.setOptionalCover(selectValue);
					}
				}
				listOfValues.add(optionalCoverDto);
			}
		}	
		return listOfValues;
	}

	private MasPaClaimCovers getPaClaimCover(Long coverId) {
		Query paClaimquery = entityManager.createNamedQuery("MasPaClaimCovers.findByCoverKey").setParameter("coverKey", coverId);
		List<MasPaClaimCovers> paClaimCoverList = (List<MasPaClaimCovers>)paClaimquery.getResultList();
		MasPaClaimCovers masPaClaimCovers = paClaimCoverList.get(0);
		return masPaClaimCovers;
	}

	public List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> getAddOnCoverListByRodKey(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("PAAdditionalCovers.findByRodKey").setParameter("rodKey", rodKey);
		List<PAAdditionalCovers> rodList = (List<PAAdditionalCovers>)query.getResultList();
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		ArrayList<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> listOfValues = new ArrayList<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO>();
		if(! rodList.isEmpty()){
			for (PAAdditionalCovers paAdditionalCovers : rodList) {
				com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO addOnCoversTableDTO = new com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO();
				addOnCoversTableDTO.setAmountClaimed(paAdditionalCovers.getClaimedAmount());
				addOnCoversTableDTO.setRemarks(paAdditionalCovers.getRemarks());
				addOnCoversTableDTO.setKey(paAdditionalCovers.getKey());
				addOnCoversTableDTO.setClaimKey(paAdditionalCovers.getClaimKey());
				Long coverId = paAdditionalCovers.getCoverId();
				List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> addOnCoverDTOList = dbCalculationService.getCoverValues(rodKey);
				if(null != addOnCoverDTOList && !addOnCoverDTOList.isEmpty())
				{
					for (com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO addOnCoverDTO : addOnCoverDTOList) {
						if(coverId!=null && addOnCoverDTO.getCoverId()!=null && coverId.equals(addOnCoverDTO.getCoverId()))
						{
							addOnCoversTableDTO.setEligibleAmount(addOnCoverDTO.getEligibleAmount());
							addOnCoversTableDTO.setSiLimit(addOnCoverDTO.getSiLimit());
						}
						
					}
				}
				/*if(null != addOnCoverDTO)
				{
					addOnCoversTableDTO.setEligibleAmount(addOnCoverDTO.getEligibleForProduct());
					addOnCoversTableDTO.setSiLimit(addOnCoverDTO.getSiLimit());
				}*/
				if(coverId!=null){
					MasPaClaimCovers paClaimCover = getPaClaimCover(coverId);
					if(paClaimCover!=null){
						if("EDUCATIONAL GRANT".equalsIgnoreCase(paClaimCover.getCoverDescription())){
							addOnCoversTableDTO.setAllowableChildren(2);
						}
						SelectValue selectValue = new SelectValue();
						selectValue.setId(paClaimCover.getCoverKey());
						selectValue.setValue(paClaimCover.getCoverDescription());
						addOnCoversTableDTO.setAddonCovers(selectValue);
					}
				}
				listOfValues.add(addOnCoversTableDTO);
			}
		}
		return listOfValues;
	}
	
	public String getCoverValueForViewBasedOnrodKey(Long rodKey)
	{
		String optCover = "";
		String addOnCover = "";
		String coverValues = "";
		
		if(rodKey != null)
		{
			List<PAAdditionalCovers> additionalCovers = new ArrayList<PAAdditionalCovers>();
			List<PAOptionalCover> optionalCovers = new ArrayList<PAOptionalCover>();
			List<MasPaClaimCovers> coversList = new ArrayList<MasPaClaimCovers>();
			List<MasPaClaimCovers> optionalCoversList = new ArrayList<MasPaClaimCovers>();
			
			additionalCovers =  getAdditionalCoversForRodAndBillEntry(rodKey);
			optionalCovers = getOptionalCoversForRodAndBillEntry(rodKey);
			
			if(additionalCovers != null)
			{
				for(PAAdditionalCovers paAdditionalCovers : additionalCovers)
				{
					Long coverId = paAdditionalCovers.getCoverId();
					MasPaClaimCovers coverName = getCoversName(coverId);
					coversList.add(coverName);
				}
				
				if(null != coversList && !coversList.isEmpty())
				{
					for (MasPaClaimCovers masPaClaimCovers : coversList) {
						if(masPaClaimCovers.getCoverDescription() != null){
							addOnCover += masPaClaimCovers.getCoverDescription()+", ";
						}
						}
				}
			}
			if(optionalCovers != null)
			{
				for (PAOptionalCover paOptionalCover : optionalCovers) {						
						Long coverId = paOptionalCover.getCoverId();								
						MasPaClaimCovers coverName = getCoversName(coverId);
						
						optionalCoversList.add(coverName);
				} 
				if(null != optionalCoversList && !optionalCoversList.isEmpty())
				{
						for (MasPaClaimCovers masPaClaimCovers : optionalCoversList) {
							if(masPaClaimCovers.getCoverDescription() != null){
								optCover += masPaClaimCovers.getCoverDescription()+", ";
							}
						}
				}
			}
		coverValues = optCover + addOnCover;
		}
		return coverValues;
	}
	
	public MasPaClaimCovers getCoversName(Long coverId)
	{
		Query query = entityManager.createNamedQuery("MasPaClaimCovers.findByCoverKey");
		query = query.setParameter("coverKey",coverId );
		List<MasPaClaimCovers> coverName = query.getResultList();
		if(null != coverName && !coverName.isEmpty())
		{
			entityManager.refresh(coverName.get(0));
			return coverName.get(0);
		}
		return null;
	}
	/*public List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO> getConsolidatedValue(
			List<TableBenefitsDTO> benefitDTOList,
			List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO> addOnCoversTableListBilling,
			List<OptionalCoversDTO> optionalCoversTableListBilling) {
		
			List<com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO> paBillingConsolidatedDTOs = new ArrayList<com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO>();
			
			
			Double benfitTotalPayableAmt = 0d;
			
			Double optTotalPayableAmt = 0d;
			
			Double addOnTotalPayableAmt = 0d;
			
			for (TableBenefitsDTO benefitsDTO : benefitDTOList) {
				
				Double netAmount = benefitsDTO.getNetAmount();
				//Double payableAmount = billingConsolidatedDTO.getPayableAmount();
				benfitTotalPayableAmt+= netAmount;
			}

			for (com.shaic.paclaim.billing.processclaimbilling.page.billreview.AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversTableListBilling) {
				 
				Double approvedAmount = addOnCoversTableDTO.getApprovedAmount();
				addOnTotalPayableAmt+= approvedAmount;
			}
			
			for(OptionalCoversDTO optionalCoversDTO :optionalCoversTableListBilling){
					
				Double approvedAmount = optionalCoversDTO.getAppAmt();
				addOnTotalPayableAmt +=approvedAmount;
				
			}
			if(benefitDTOList.size() >0){
				com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO benifitBillingConsolidatedDTO = new com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO();
				benifitBillingConsolidatedDTO.setPayableAmount(benfitTotalPayableAmt);
				benifitBillingConsolidatedDTO.setBenefitsCover("Benefit Cover");
				paBillingConsolidatedDTOs.add(benifitBillingConsolidatedDTO);
			}
			if(addOnCoversTableListBilling.size() >0){
				com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO addOnBillingConsolidatedDTO = new com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO();
			    addOnBillingConsolidatedDTO.setPayableAmount(addOnTotalPayableAmt);
			    addOnBillingConsolidatedDTO.setBenefitsCover("Add On Cover");
			    paBillingConsolidatedDTOs.add(addOnBillingConsolidatedDTO);
			}
			if(optionalCoversTableListBilling.size() >0){
				com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO optionalBillingConsolidatedDTO = new com.shaic.paclaim.billing.processclaimbilling.page.billreview.PABillingConsolidatedDTO();
				optionalBillingConsolidatedDTO.setPayableAmount(optTotalPayableAmt);
				optionalBillingConsolidatedDTO.setBenefitsCover("OptionalCover");
				paBillingConsolidatedDTOs.add(optionalBillingConsolidatedDTO);
			}
		
			
		// TODO Auto-generated method stub
		return paBillingConsolidatedDTOs;
	}*/
	
}