package com.shaic.claim.omp.ratechange;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.search.OMPReceiptofDocumentsAndBillEntryMapper;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.search.OMPReceiptofDocumentsAndBillEntryTableDTO;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class OMPClaimRateChangeAndOsUpdationService extends AbstractDAO<OMPIntimation>{
	
	
	public OMPClaimRateChangeAndOsUpdationService() {
		super();
	}
	@EJB
	private OMPIntimationService service;
	
	@EJB
	private OMPProcessRODBillEntryService rodBillentryService;
	
	@EJB
	private DBCalculationService CalculationService;
	
	@SuppressWarnings("unchecked")
	public Page<OMPClaimRateChangeAndOsUpdationTableDTO> search(
			OMPClaimRateChangeAndOsUpdationFormDto formDTO, String userName, String passWord) {
		
		List<OMPReimbursement> listIntimations = new ArrayList<OMPReimbursement>(); 
		try{
//			if(formDTO.getIntimationDate()!= null){
		Date strIntimationDate = formDTO.getIntimationDate();
		
		
		SimpleDateFormat textFormat = new SimpleDateFormat("yyyy-MM-dd");
		String paramDateAsString = "2017-4-1";
		Date fromDate  = null;

		fromDate = formDTO.getIntimationDate();
				//textFormat.parse(paramDateAsString);
		
//		Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.MONTH, 4);
//        cal.set(Calendar.DATE, 1);
//        cal.set(Calendar.YEAR, 2017);
//        fromDate = cal.getTime();
		
		String intimationNo =  null != formDTO.getIntimationNo() && !formDTO.getIntimationNo().isEmpty() ? formDTO.getIntimationNo() :null;
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OMPReimbursement> criteriaQuery = criteriaBuilder.createQuery(OMPReimbursement.class);
		
		Root<OMPReimbursement> root = criteriaQuery.from(OMPReimbursement.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(strIntimationDate != null){
			Calendar c = Calendar.getInstance();
			c.setTime(strIntimationDate);
			c.add(Calendar.DATE, 1);
			strIntimationDate = c.getTime();
		Predicate condition1 = criteriaBuilder.lessThanOrEqualTo(root.<OMPClaim>get("claim").<OMPIntimation>get("intimation").<Date>get("intimationDate"), strIntimationDate);
		conditionList.add(condition1);
		
		Predicate condition2 = criteriaBuilder.notEqual(root.<MastersValue>get("classificationId").<String>get("value"), "OMP Other Expenses");
		conditionList.add(condition2);
		
		Predicate condition3 = criteriaBuilder.notEqual(root.<String>get("rejectFlg"), SHAConstants.YES_FLAG);
		
		Predicate condition14 = criteriaBuilder.isNull(root.<String>get("rejectFlg"));
		
		Predicate condition15 = criteriaBuilder.or(condition3,condition14);
		conditionList.add(condition15);
		
		Predicate condition6 = criteriaBuilder.notEqual(root.<String>get("faSubmitFlg"), SHAConstants.YES_FLAG);
		
		Predicate condition7 = criteriaBuilder.isNull(root.<String>get("faSubmitFlg"));
		
		Predicate condition8 = criteriaBuilder.or(condition6,condition7);
		conditionList.add(condition8);
		}
		
		
//		Predicate condition3 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.PROCESS_REJECTED);
//		conditionList.add(condition3);
//		
//		Predicate condition4 = criteriaBuilder.notEqual(root.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS);
//		conditionList.add(condition4);
				
		/*if (fromDate != null && strIntimationDate != null) {
			Expression<Date> fromDateExpression = root
					.<Date> get("createdDate");
			Predicate fromDatePredicate = criteriaBuilder
					.greaterThanOrEqualTo(fromDateExpression,
							fromDate);
			conditionList.add(fromDatePredicate);

			Expression<Date> toDateExpression = root
					.<Date> get("createdDate");
			Calendar c = Calendar.getInstance();
			c.setTime(strIntimationDate);
			c.add(Calendar.DATE, 1);
			strIntimationDate = c.getTime();
			Predicate toDatePredicate = criteriaBuilder
					.lessThanOrEqualTo(toDateExpression, strIntimationDate);
			conditionList.add(toDatePredicate);
		}*/
		
		
		
		if(strIntimationDate == null && fromDate ==null){
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<OMPReimbursement> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = formDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 0;
		}

		if(strIntimationDate == null ){
		listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listIntimations = typedQuery.getResultList();
//			listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}
//		for(Intimation inti:listIntimations){
//			System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
//		}
		List<OMPReimbursement> doList = listIntimations;
		
		List<OMPClaimRateChangeAndOsUpdationTableDTO> tableDTO = OMPClaimRateChangeAndOsUpdationMapper.getInstance().getOMPClaimRateChangeAndOsUpdationTableDTOForIntimation(doList);
		
		List<OMPClaimRateChangeAndOsUpdationTableDTO> result = new ArrayList<OMPClaimRateChangeAndOsUpdationTableDTO>();
		result.addAll(tableDTO);
		Page<OMPClaimRateChangeAndOsUpdationTableDTO> page = new Page<OMPClaimRateChangeAndOsUpdationTableDTO>();
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
//		}
	catch(Exception e){
		e.printStackTrace();
		}
				return null;
	}
		/*
//		try 
//		{
			String strIntimationNo = "";
			
			String strClaimNo = "";
			
			Date strIntimationDate = formDTO.getIntimationDate();// != null ? formDTO.getIntimationDate().get() != null ? formDTO.getIntimationDate().getDate(): null : null ;
			
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
		
			if(strIntimationDate != null){
				
//				String intimDate = SHAUtils.formatIntimationDateValue(regQf.getIntimationDate());
//				intimationType.setIntDate(new Date(intimDate));
//				payloadBO.setIntimation(intimationType);
			 mapValues.put(SHAConstants.INTIMATION_DATE, strIntimationDate);
			}
			
			List<OMPClaimRateChangeAndOsUpdationTableDTO> searchProcessRejectionTableDTOForIntimation = new ArrayList<OMPClaimRateChangeAndOsUpdationTableDTO>();
			Page<OMPClaimRateChangeAndOsUpdationTableDTO> page = new Page<OMPClaimRateChangeAndOsUpdationTableDTO>();

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
				
			List<OMPClaimRateChangeAndOsUpdationTableDTO> tableDTO = new ArrayList<OMPClaimRateChangeAndOsUpdationTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				strIntimationNo = intimationNoList.get(index);
				
				// humanTaskDTO = humanTaskListDTO.get(index);
				if(index < rodKeyList.size()){
				 rodKey = rodKeyList.get(index);
				 ackKey = ackKeyList.get(index);
				// Integer taskNo = taskNumber.get(index);
				 tableDTO.addAll(getIntimationData(strIntimationNo, /*rodKey, humanTaskDTO,ackKey,/*taskNo,userName));
				}
			}
	
			
//			page.setPageItems(tableDTO);
//			tableDTO.add(new OMPClaimRateChangeAndOsUpdationTableDTO()); // for test
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
	}*/

	/*
	private List<OMPClaimRateChangeAndOsUpdationTableDTO> getIntimationData(String intimationNo, /*Long rodKey, HumanTask humanTask, Long ackKey,Integer taskNumber,String userName){
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
			List<OMPClaimRateChangeAndOsUpdationTableDTO> tableDTO = OMPClaimRateChangeAndOsUpdationMapper.getInstance().getOMPClaimRateChangeAndOsUpdationTableDTOForIntimation(doList);
			
			for(int index = 0; index < tableDTO.size(); index++){										//
				System.out.println("Intimationkey+++++++++++++++++++++"+tableDTO.get(index).getKey());	//
			
			OMPIntimation intimation = service.getIntimationByKey(tableDTO.get(index).getKey());	//
			
			NewIntimationDto intimationDto=service.getIntimationDto(intimation);					//
			if(intimationDto != null){																//
				tableDTO.get(index).setEventCode(intimationDto.getEventCodeValue());				//
//			OMPIntimation intimation = getNotRegisteredIntimation(tableDTO.getIntimationNo());
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
//			tableDTO = getclaimNumber(tableDTO);
			}
			}
		return tableDTO;
			
			
		}catch(Exception e){
			return null;
		
		}
	}
	*/
//	private List<OMPClaimRateChangeAndOsUpdationTableDTO> getclaimNumber(List<OMPClaimRateChangeAndOsUpdationTableDTO> tableDTO){
//		OMPClaim a_claim = null;
//		for(int index = 0; index < tableDTO.size(); index++){
//			System.out.println("Intimationkey+++++++++++++++++++++"+tableDTO.get(index).getKey());
//			
//			if (tableDTO.get(index).getKey() != null) {
//
//				Query findByIntimationKey = entityManager
//						.createNamedQuery("OMPClaim.findByOMPIntimationKey");
//				findByIntimationKey = findByIntimationKey.setParameter(
//						"intimationKey", tableDTO.get(index).getKey());
//				findByIntimationKey.setParameter("lobId",ReferenceTable.OMP_LOB_KEY);
//				
//				try{
//						a_claim = (OMPClaim) findByIntimationKey.getSingleResult();
//						if(a_claim != null){
//							tableDTO.get(index).setcl(a_claim.getClaimId());
////							tableDTO.get(index).setClaimType(a_claim.getClaimType().getValue());
//							tableDTO.get(index).setClaimKey(a_claim.getKey());
//						}else{
//							tableDTO.get(index).setClaimno("");
//						}	
//				}catch(Exception e){
//					continue;
//				}
//			} 
//		}
//		return tableDTO;
//	}
	
	@Override
	public Class<OMPIntimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveReimbursementConversionRate(
			List<OMPClaimRateChangeAndOsUpdationTableDTO> searchTableDTO) {
		
	for(OMPClaimRateChangeAndOsUpdationTableDTO claimRateDto : searchTableDTO){
		if(claimRateDto != null){
			if(claimRateDto.getKey() != null){
				
				DBCalculationService dbCalculationService = new DBCalculationService();
//				CalculationService.updateCurrencyRate(claimRateDto.getConversionRate(),claimRateDto.getKey());
				OMPReimbursement ompReimbursement = rodBillentryService.getReimbursementByKey(claimRateDto.getKey());
				if(ompReimbursement != null){
					ompReimbursement.setInrConversionRate(claimRateDto.getConversionRate());
					if(claimRateDto.getConversionRate()!=null){
						Double finalApprovedAmtUsd = ompReimbursement.getFinalApprovedAmtUsd();
						if(finalApprovedAmtUsd!=null){
							Double finalApprovedAmtInr = finalApprovedAmtUsd * claimRateDto.getConversionRate();
							ompReimbursement.setFinalApprovedAmtInr(finalApprovedAmtInr);
						}
					}
					if(null!= ompReimbursement.getKey()){
						
						entityManager.merge(ompReimbursement); 
						entityManager.flush();
					}
				}
			}
		}
	}
		
	}
	
	

}
