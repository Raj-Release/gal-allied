

/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.RRCCategory;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PreExistingDisease;

/**
 * @author ntv.vijayar
 *
 * This service file is common for search and submit services , related
 * with Review RRC REQUEST menu.
 */
@Stateless
public class SearchRRCRequestService extends AbstractDAO<RRCRequest>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	public SearchRRCRequestService(){
		super();
	}
	public  Page<SearchRRCRequestTableDTO> search(
			SearchRRCRequestFormDTO searchFormDTO,
			String userName, String passWord) {
		
		List<RRCRequest> listIntimations1 = new ArrayList<RRCRequest>();
		//List<SearchRRCRequestTableDTO> totalList = new ArrayList<SearchRRCRequestTableDTO>();
		
		
		try{
		String intimationNo = null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
		String rrcRequestNo = null != searchFormDTO.getRrcRequestNo()&& !searchFormDTO.getRrcRequestNo().isEmpty() ? searchFormDTO.getRrcRequestNo() : null;
		Long cpuId = null != searchFormDTO && null != searchFormDTO.getCpu() ? searchFormDTO.getCpu().getId() : null;
		Long rrcRequestTypeId = null != searchFormDTO && null != searchFormDTO.getRrcRequestType() ? searchFormDTO.getRrcRequestType().getId() : null;
		Long rrcEligbilityTypeId = null != searchFormDTO &&  null != searchFormDTO.getEligibilityType() ? searchFormDTO.getEligibilityType().getId() : null;
		Date fromDate = null != searchFormDTO && null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
		Date toDate = null != searchFormDTO && null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<RRCRequest> criteriaQuery = criteriaBuilder.createQuery(RRCRequest.class);
		
		Root<RRCRequest> root = criteriaQuery.from(RRCRequest.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(null != intimationNo){
		Predicate condition1 = criteriaBuilder.like(root.<Claim>get("claim").<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(null != rrcRequestNo){
		Predicate condition2 = criteriaBuilder.like(root.<String>get("rrcRequestNumber"), "%"+rrcRequestNo+"%");
		conditionList.add(condition2);
		}
		if(null != rrcRequestTypeId){
			Predicate condition3 = criteriaBuilder.equal(root.<MastersValue>get("requestedTypeId").<Long>get("key"), rrcRequestTypeId);
		conditionList.add(condition3);
		}
		if(null != cpuId){
			Predicate condition4 = criteriaBuilder.equal(root.<Claim>get("claim").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuId);
			conditionList.add(condition4);
			}
		if(null != rrcEligbilityTypeId)
		{
			Predicate condition5 = criteriaBuilder.equal(root.<MastersValue>get("eligiblityTypeId").<Long>get("key"), rrcEligbilityTypeId);
			conditionList.add(condition5);
		}
		if(null != fromDate)
		{
			Predicate condition6 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("rrcInitiatedDate"), fromDate);
			//Predicate condition6 = criteriaBuilder.equal(root.<Date>get("createdDate"), fromDate);
			conditionList.add(condition6);
		}
		
		
		if(null != toDate)
		{
			
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			toDate = c.getTime();		
			Predicate condition7 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("rrcInitiatedDate"), toDate);
			//Predicate condition7 = criteriaBuilder.equal(root.<Date>get("createdDate"), toDate);
			conditionList.add(condition7);
		}
		/*
		Predicate condition8 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),127);
		conditionList.add(condition8);*/
		
		Boolean isFilterAvailable = true;
		
		if(null == intimationNo && null == rrcRequestNo && null == rrcRequestTypeId && null == cpuId && null == rrcEligbilityTypeId && null == fromDate && null == toDate){
			criteriaQuery.select(root);
			isFilterAvailable = false;
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));

			}
		 
		final TypedQuery<RRCRequest> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *20;
		}else{
			firtResult = 1;
		}

	//	listIntimations1 = typedQuery.setFirstResult(firtResult).setMaxResults(25).getResultList();
		//if(isFilterAvailable){
	//	List<SearchRRCRequestTableDTO> searchRRCDTO = new ArrayList<SearchRRCRequestTableDTO>();
			
			//if(pageNumber <= 1)
		//	{
			
		//}
			
			List<RRCRequest> listIntimationsObj = null;
			
		//else{
			if(isFilterAvailable){
				listIntimations1 = typedQuery.getResultList();
				//	List<RRCRequest> listIntimations = getRevisedUniqueRecords(listIntimations1);
				//totalList =  convertDoToDto(listIntimations1);
//				listIntimationsObj = totalList;
				listIntimationsObj = listIntimations1;
			}else{
				listIntimationsObj = typedQuery.setFirstResult(firtResult).setMaxResults(20).getResultList();
			}
			
		//	List<RRCRequest> revisedlistIntimations = getRevisedUniqueRecords(listIntimationsObj);
		//}
//		List<RRCRequest> listIntimations = getRevisedUniqueRecords(listIntimations1);

//		List<RRCRequest> listIntimations = RRCRequestRemoveDuplicate.getUniqueRecords(listIntimations1);		
		
		
		List<RRCRequest> doList = listIntimationsObj;
		List<SearchRRCRequestTableDTO> tableDTO = null ;
		//List<RRCRequest> doList = uniqueList;
		
		List<String> loginIdList = new ArrayList<String>();
		
		List<TmpEmployee> tmpEmployeeList =  null;
		
		List<RRCDetails> rrcDetailsList = null;
		List hospitalTypeInfoList = null;
		List<RRCCategory> categoryList = null;
		
		List<Long> rrcRequestKeyList = new ArrayList<Long>();
		
		List<Long> hospitalIdList = new ArrayList<Long>();

		tableDTO = convertDoToDto(doList);	

		if(doList != null && !doList.isEmpty()) {
			SearchRRCRequestMapper searchRRCRequestMapper =  SearchRRCRequestMapper.getInstance();
			 tableDTO = searchRRCRequestMapper.getRRCRequestList(doList);
			 
			 for (SearchRRCRequestTableDTO searchRRCRequestTableDTO : tableDTO) {
					if(searchRRCRequestTableDTO.getCpuCode() != null && searchRRCRequestTableDTO.getCpuDivString() != null){
						searchRRCRequestTableDTO.setCpuCode(searchRRCRequestTableDTO.getCpuCode() +" - "+ searchRRCRequestTableDTO.getCpuDivString());
					}
				}
			
		}

		if(null != tableDTO && !tableDTO.isEmpty())
		{
			for (SearchRRCRequestTableDTO SearchRRCRequestTableDTO : tableDTO) {
				if(null != SearchRRCRequestTableDTO.getRequestorSavedAmount())
					SearchRRCRequestTableDTO.setAmountSaved(String.valueOf(SearchRRCRequestTableDTO.getRequestorSavedAmount()));
				
				if(null != SearchRRCRequestTableDTO.getStatusKey()  && (ReferenceTable.RRC_REQUEST_PROCESS_STATUS).equals(SearchRRCRequestTableDTO.getStatusKey()))
				{
					if(null != SearchRRCRequestTableDTO.getRrcProcessedDate() )
					{
					SearchRRCRequestTableDTO.setStatusDate(SearchRRCRequestTableDTO.getRrcProcessedDate().toString());
					}
				}
				else if(null != SearchRRCRequestTableDTO.getStatusKey()  && (ReferenceTable.RRC_REQUEST_REVIEWED_STATUS).equals(SearchRRCRequestTableDTO.getStatusKey()))
				{
					if(null != SearchRRCRequestTableDTO.getRrcModifedDate() && ("RRC MODIFY").equalsIgnoreCase(SearchRRCRequestTableDTO.getRrcType()))
					{
						if(null != SearchRRCRequestTableDTO.getRrcModifedDate())
						SearchRRCRequestTableDTO.setStatusDate(SearchRRCRequestTableDTO.getRrcModifedDate().toString());
					}
					else if (null != SearchRRCRequestTableDTO.getRrcReviewedDate() && ("RRC REVIEW").equalsIgnoreCase(SearchRRCRequestTableDTO.getRrcType()))
					{
						if(null != SearchRRCRequestTableDTO.getRrcReviewedDate())
						{
						SearchRRCRequestTableDTO.setStatusDate(SearchRRCRequestTableDTO.getRrcReviewedDate().toString());
						}
					}

					
				}
				
				if(null != tmpEmployeeList && !tmpEmployeeList.isEmpty())
				{
					for (TmpEmployee tmpEmpDetails : tmpEmployeeList) {
						if(SearchRRCRequestTableDTO.getRequestorId() != null && SearchRRCRequestTableDTO.getRequestorId().equalsIgnoreCase(tmpEmpDetails.getLoginId()))
						{
							SearchRRCRequestTableDTO.setRequestorName(tmpEmpDetails.getEmployeeWithNames());
						}
					}
				}
				
				TmpEmployee tmpEmployee = getEmployeeName(SearchRRCRequestTableDTO.getRequestorId());
				
				if(tmpEmployee != null ){
					
					SearchRRCRequestTableDTO.setRequestorName(tmpEmployee.getEmpFirstName());
					
				}
				
				/*if(null != rrcDetailsList && !rrcDetailsList.isEmpty())
				{
									
					for (RRCDetails rrcDetails : rrcDetailsList) {
						if(SearchRRCRequestTableDTO.getKey().equals(rrcDetails.getRrcRequest()))
						{
							if(null != rrcDetails.getCreditTypeId())
							{
								if(("Credit").equalsIgnoreCase(rrcDetails.getCreditTypeId().getValue()))
								{
									SearchRRCRequestTableDTO.setCreditsEmployeeName(rrcDetails.getEmployeeName());
								}
								if(("Lapse").equalsIgnoreCase(rrcDetails.getCreditTypeId().getValue()))
								{
									SearchRRCRequestTableDTO.setLapseEmployeeName(rrcDetails.getEmployeeName());
								}
							}
						}
					}
				}*/
				
				if(null != rrcDetailsList && !rrcDetailsList.isEmpty())
				{
					List<RRCDetails> creditArrayList = new ArrayList<RRCDetails>();
					List<RRCDetails> lapseArrayList = new ArrayList<RRCDetails>();
					
					
					for (RRCDetails rrcDetails : rrcDetailsList) {
						if(SearchRRCRequestTableDTO.getKey().equals(rrcDetails.getRrcRequest()))
						{
							if(null != rrcDetails.getCreditTypeId())
							{
								if(("Credit").equalsIgnoreCase(rrcDetails.getCreditTypeId().getValue()))
								{
									creditArrayList.add(rrcDetails);
								}
								if(("Lapse").equalsIgnoreCase(rrcDetails.getCreditTypeId().getValue()))
								{
									lapseArrayList.add(rrcDetails);
								}
							}
						}
					}
					
									
					
					if(null != creditArrayList && !creditArrayList.isEmpty())
					{					
						SearchRRCRequestTableDTO.setCreditsEmployeeName(creditArrayList.get(0).getEmployeeName());
					
					 if( creditArrayList.size() >= 2)
					{						
						SearchRRCRequestTableDTO.setCreditsEmployeeName2(creditArrayList.get(1).getEmployeeName());
					}
					 if (creditArrayList.size() >= 3)
					{						
						SearchRRCRequestTableDTO.setCreditsEmployeeName3(creditArrayList.get(2).getEmployeeName());
					}
					 if(creditArrayList.size() >= 4)
					{						
						SearchRRCRequestTableDTO.setCreditsEmployeeName4(creditArrayList.get(3).getEmployeeName());
					}
					if(creditArrayList.size() >= 5)
					{						
						SearchRRCRequestTableDTO.setCreditsEmployeeName5(creditArrayList.get(4).getEmployeeName());					
					}
					 if(creditArrayList.size() >= 6)
					{						
						SearchRRCRequestTableDTO.setCreditsEmployeeName6(creditArrayList.get(5).getEmployeeName());
						
					}
					if(creditArrayList.size() >= 7)
					{						
						SearchRRCRequestTableDTO.setCreditsEmployeeName7(creditArrayList.get(6).getEmployeeName());	
						
					}
					}
					
					
					if(null != lapseArrayList && !lapseArrayList.isEmpty())
					{					
						SearchRRCRequestTableDTO.setLapseEmployeeName(lapseArrayList.get(0).getEmployeeName());
					
					 if( lapseArrayList.size() >= 2)
					{						
						SearchRRCRequestTableDTO.setLapseEmployeeName2(lapseArrayList.get(1).getEmployeeName());
					}
					 if (lapseArrayList.size() >= 3)
					{						
						SearchRRCRequestTableDTO.setLapseEmployeeName3(lapseArrayList.get(2).getEmployeeName());
					}
					 if(lapseArrayList.size() >= 4)
					{						
						SearchRRCRequestTableDTO.setLapseEmployeeName4(lapseArrayList.get(3).getEmployeeName());
					}
					if(lapseArrayList.size() >= 5)
					{						
						SearchRRCRequestTableDTO.setLapseEmployeeName5(lapseArrayList.get(4).getEmployeeName());					
					}
					 if(lapseArrayList.size() >= 6)
					{						
						SearchRRCRequestTableDTO.setLapseEmployeeName6(lapseArrayList.get(5).getEmployeeName());
						
					}
					if(lapseArrayList.size() >= 7)
					{						
						SearchRRCRequestTableDTO.setLapseEmployeeNmae7(lapseArrayList.get(6).getEmployeeName());	
						
					}
					
					
					
					String[] creditArray = new String[creditArrayList.size()];	
					String[] lapseArray = new String[lapseArrayList.size()];	
					
					for(int i=0;i<creditArray.length;i++)
					{
						creditArray[i] = creditArrayList.get(i).getEmployeeName();		
							
						
					}
					for(int i=0;i<lapseArray.length;i++)
					{
						lapseArray[i] = lapseArrayList.get(i).getEmployeeName();
					}
					
					SearchRRCRequestTableDTO.setCreditEmpList(creditArray);
					SearchRRCRequestTableDTO.setLapseEmpList(lapseArray);
				}
				
					
					}
				
				if(null != categoryList && !categoryList.isEmpty())
				{
					String category = "";
					for (RRCCategory rrcCategory : categoryList) {
						if(SearchRRCRequestTableDTO.getKey().equals(rrcCategory.getRrcRequest()))
						{
							if(null != rrcCategory.getCategoryId())
							{
								category += rrcCategory.getCategoryId().getValue() + ",";
								
							}
						}
						
						SearchRRCRequestTableDTO.setCategoryValue(category);
					}
				}
				
				if(null != hospitalTypeInfoList && !hospitalTypeInfoList.isEmpty())
				{
					for (Object hospObj : hospitalTypeInfoList) {
						Hospitals hospitalObj = (Hospitals)hospObj;
						if (hospitalObj.getKey() != null && SearchRRCRequestTableDTO.getHospitalId() != null ) {
							if(SearchRRCRequestTableDTO.getHospitalId().equals(hospitalObj.getKey()))
							{
								SearchRRCRequestTableDTO.setHospitalName(hospitalObj.getName());
								SearchRRCRequestTableDTO.setHospitalTypeId(hospitalObj.getHospitalType().getKey());
								if(SearchRRCRequestTableDTO.getHospitalTypeId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID))
								{
									if(null != hospitalObj.getNetworkHospitalType())
									{
										SearchRRCRequestTableDTO.setHospitalType(hospitalObj.getNetworkHospitalType());
									}
									else
									{
										SearchRRCRequestTableDTO.setHospitalType(ReferenceTable.NETWORK_HOSPITAL_NAME);
									}
								}
								else
								{
									SearchRRCRequestTableDTO.setHospitalType(ReferenceTable.NON_NETWORK_HOSPITAL_NAME);
								}
							}
						}
						
					}
				}
				
				if(null != doList && !doList.isEmpty())
				{
					for (RRCRequest rrcRequest : doList) {
						
						if((ReferenceTable.RRC_REQUEST_REVIEWED_STATUS).equals(SearchRRCRequestTableDTO.getRrcStatusId()))
						{
							if(null != rrcRequest.getReviewerEligibilityTypeId())
								SearchRRCRequestTableDTO.setStatus(rrcRequest.getReviewerEligibilityTypeId().getValue());
							if(null != rrcRequest.getReviewedDate())
							{
								SearchRRCRequestTableDTO.setStatusDate(rrcRequest.getReviewedDate().toString());
							}
								SearchRRCRequestTableDTO.setRemarks(rrcRequest.getReviewRemarks());
						}
						
						else if((ReferenceTable.RRC_REQUEST_PROCESS_STATUS).equals(SearchRRCRequestTableDTO.getRrcStatusId()))
						{
							if(null != rrcRequest.getEligiblityTypeId())
								SearchRRCRequestTableDTO.setStatus(rrcRequest.getEligiblityTypeId().getValue());
							if(null != rrcRequest.getProcessedDate())
							{
								SearchRRCRequestTableDTO.setStatusDate(rrcRequest.getProcessedDate().toString());
							}
								SearchRRCRequestTableDTO.setRemarks(rrcRequest.getEligibiltyRemarks());
						}
					}
					
				}
				
				List<OldInitiatePedEndorsement> pedDetails = getPedDetails(SearchRRCRequestTableDTO.getClaimKey());
				List<Long> pedIntiateKeyList = null;
				if(null != pedDetails){
					String pedName = "";
					String pedSuggestion = "";
					// The below list is added for storing ped initiate key.
					/**
					 * Since for the given claim one or more ped can be intiated.
					 * In such scenario , in ped intiate table we will have one record
					 * per intiate request. Hence List is required if multiple ped's are
					 * initiated.
					 * */
					pedIntiateKeyList = new ArrayList<Long>();
					for (OldInitiatePedEndorsement oldInitiatePedEndorsement : pedDetails) {
						
						 pedName += oldInitiatePedEndorsement.getPedName() + ",";
						 pedSuggestion += oldInitiatePedEndorsement.getPedSuggestion().getValue() + ",";
						
						SearchRRCRequestTableDTO.setInitialPEDRecommendation(pedName);
						SearchRRCRequestTableDTO.setPedSuggestions(pedSuggestion);
						pedIntiateKeyList.add(oldInitiatePedEndorsement.getKey());
						//SearchRRCRequestTableDTO.setPedInitiateKey(oldInitiatePedEndorsement.getKey());
					}
									
					
				}
				
				StringBuffer descriptionBuffer = new StringBuffer();
				if(null != pedIntiateKeyList && !pedIntiateKeyList.isEmpty())
				{
					for (Long long1 : pedIntiateKeyList) {
						
						List<NewInitiatePedEndorsement> pedDescription = getAllPedDetails(long1);
						if(null != pedDescription)
						{
							for (NewInitiatePedEndorsement newInitiatePedEndorsement : pedDescription) {
								
								SearchRRCRequestTableDTO.setPedCode(newInitiatePedEndorsement.getPedCode());
								PreExistingDisease description= getPedDescription(SearchRRCRequestTableDTO.getPedCode());
								if(null != description)
								{
									/*
									for (PreExistingDisease preExistingDisease : Description) {*/
									descriptionBuffer.append(description.getValue()).append(",");
										
										//pedDescriptions += description.getValue()+ ",";
										
									//}
									

							}
							
						}
					}
				}
					if(null != descriptionBuffer)
						SearchRRCRequestTableDTO.setPedName(descriptionBuffer.toString());
				
				/*List<NewInitiatePedEndorsement> pedDescription = getAllPedDetails(SearchRRCRequestTableDTO.getPedInitiateKey());
				if(null != pedDescription)
				{
					String pedDescriptions = "";
					for (NewInitiatePedEndorsement newInitiatePedEndorsement : pedDescription) {
						
						SearchRRCRequestTableDTO.setPedCode(newInitiatePedEndorsement.getPedCode());
						PreExistingDisease description= getPedDescription(SearchRRCRequestTableDTO.getPedCode());
						if(null != description)
						{
							
							for (PreExistingDisease preExistingDisease : Description) {
								
								pedDescriptions += description.getValue()+ ",";
								
							//}
							SearchRRCRequestTableDTO.setPedName(pedDescriptions);

					}
					
				}
*/
			
					
				}
				
				
				List<InsuredPedDetails> pedDisclosed = getPedDisclosed(SearchRRCRequestTableDTO.getInsuredKey());
				if(null != pedDisclosed)
				{
					String pedDisclose = "";
					for (InsuredPedDetails insuredPedDetails : pedDisclosed) {
						
						pedDisclose += insuredPedDetails.getPedDescription();
						SearchRRCRequestTableDTO.setPedDisclosed(pedDisclose);
					}
					
				}
				
				Insured initialSumInsure = getInitialSumInsured(SearchRRCRequestTableDTO.getHealthCardNo());
				if(null != initialSumInsure)
				{
					SearchRRCRequestTableDTO.setInitialSumInsured(initialSumInsure.getInsuredSumInsured());
				}
				
				
			}
			
			
		}

		
		Page<SearchRRCRequestTableDTO> page = new Page<SearchRRCRequestTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber+1);
	//	page.setHasNext(true);
		page.setHasNext(false);
		if(tableDTO != null && tableDTO.isEmpty()){
			searchFormDTO.getPageable().setPageNumber(1);
		}
		if (null != tableDTO && tableDTO.isEmpty()) 
		{
			searchFormDTO.getPageable().setPageNumber(1);
			page.setHasNext(true);
		}
		page.setHasNext(true);
		page.setPageNumber(pageNumber);
		page.setPageItems(tableDTO);
		page.setIsDbSearch(true);
		
		
		if(null != doList && !doList.isEmpty())
		{
			page.setTotalList(doList);
		}
		//}
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;	
	}
	


	
	@Override
	public Class<RRCRequest> getDTOClass() {
		// TODO Auto-generated method stub
		return RRCRequest.class;
	} 
	
	  private TmpEmployee getEmployeeName(String initiatorId)
			{
			  TmpEmployee fvrInitiatorDetail;
				Query findByTransactionKey = entityManager.createNamedQuery(
						"TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
				try{
					fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
					return fvrInitiatorDetail;
				}
				catch(Exception e)
				{
					return null;
				}
									
			}
	
	private  List<OldInitiatePedEndorsement> getPedDetails(Long claimKey) {
		List <OldInitiatePedEndorsement> pedDetails;
					
			Query findByClaimKey = entityManager.createNamedQuery(
					"OldInitiatePedEndorsement.findByClaimAndStatus");
			List<Long> statusKeyList = new ArrayList<Long>();
			statusKeyList.add(ReferenceTable.PED_APPROVED );
			statusKeyList.add(ReferenceTable.ENDORSEMENT_PROCESSING);
			
					findByClaimKey.setParameter("claimKey", claimKey);
					findByClaimKey.setParameter("statusKey",statusKeyList );
					
			try{
				pedDetails = findByClaimKey.getResultList();
				if(null != pedDetails && !pedDetails.isEmpty())
				return pedDetails;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				
			}
			return null;

}

	
	
	private PreExistingDisease getPedDescription(Long pedCode) {
		List <PreExistingDisease> pedDescription;
					
			Query findByPedCode = entityManager.createNamedQuery(
					"PreExistingDisease.findByKey");	
			findByPedCode.setParameter("primaryKey", pedCode);
			
			try{
				pedDescription = findByPedCode.getResultList();
				if(null != pedDescription && !pedDescription.isEmpty())
					entityManager.refresh(pedDescription.get(0));
				return pedDescription.get(0);
				
			}catch(Exception e)
			{
				e.printStackTrace();
				
			}
			return null;

}
	private List<InsuredPedDetails> getPedDisclosed(Long insuredKey) {
		List <InsuredPedDetails> pedDisclosed;
					
			Query findByAll = entityManager.createNamedQuery(
					"InsuredPedDetails.findByinsuredKey");	
			findByAll.setParameter("insuredKey", insuredKey);
			try{
				pedDisclosed = findByAll.getResultList();
				if(null != pedDisclosed && !pedDisclosed.isEmpty())
				return pedDisclosed;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				
			}
			return null;

}
	
	private Insured getInitialSumInsured(String healthCardNo) {
		
		List <Insured> initialSumInsured;
					
			Query findByHealthCardNo = entityManager.createNamedQuery(
					"Insured.findByHealthcardNo");	
			findByHealthCardNo.setParameter("healthCardNo", healthCardNo);
			try{
				initialSumInsured = findByHealthCardNo.getResultList();
				if(null != initialSumInsured && !initialSumInsured.isEmpty())
				return initialSumInsured.get(0);
				
			}catch(Exception e)
			{
				e.printStackTrace();
				
			}
			return null;

}
	
	
private List<NewInitiatePedEndorsement> getAllPedDetails(Long pedInitiateKey) {
		
		List <NewInitiatePedEndorsement> descriptions;
					
			Query findByAll = entityManager.createNamedQuery(
					"NewInitiatePedEndorsement.findByInitateKey");	
			findByAll.setParameter("initiateKey", pedInitiateKey);
			
			try{
				descriptions = findByAll.getResultList();
				if(null != descriptions && !descriptions.isEmpty())
				return descriptions;
				
			}catch(Exception e)
			{
				e.printStackTrace();
				
			}
			return null;

}

public  List<RRCRequest> getRevisedUniqueRecords(List<RRCRequest> rrcRequestList){
	
	Long claimKey = 0l;
	
	List<Long> claimKeyList = new ArrayList<Long>();
	
	List<String> rrcRequestNumberList = new ArrayList<String>();
	
	for (RRCRequest rrcRequest : rrcRequestList) {
		
		if(! rrcRequestNumberList.contains(rrcRequest.getRrcRequestNumber())){
			rrcRequestNumberList.add(rrcRequest.getRrcRequestNumber());
		}
	}
	
	
	for (RRCRequest rrcRequest : rrcRequestList) {
		if(rrcRequest.getClaim() != null){
			claimKey = rrcRequest.getClaim().getKey();
			claimKeyList.add(claimKey);
//			break;
		}
	}
	
	if (! rrcRequestNumberList.isEmpty()){
		Query findByStageKey = entityManager.createNamedQuery("RRCRequest.findByUniqueRecord");
		findByStageKey.setParameter("claimKey", claimKeyList);
		findByStageKey.setParameter("rrcRequestNumber", rrcRequestNumberList);
		
		List<RRCRequest> resultList = (List<RRCRequest>) findByStageKey.getResultList();
		
		return resultList;
	}
	return null;
}

public List<SearchRRCRequestTableDTO> convertDoToDto(List<RRCRequest> doList){		
	List<SearchRRCRequestTableDTO> tableDTO = null ;
	List<String> loginIdList = new ArrayList<String>();
	
	List<TmpEmployee> tmpEmployeeList =  null;
	
	List<RRCDetails> rrcDetailsList = null;
	List hospitalTypeInfoList = null;
	List<RRCCategory> categoryList = null;
	
	List<Long> rrcRequestKeyList = new ArrayList<Long>();
	
	List<Long> hospitalIdList = new ArrayList<Long>();
	

	if(null != doList && !doList.isEmpty())
	{
	for(RRCRequest rrcRequest: doList){
		if(rrcRequest.getRequestorID() != null)
		loginIdList.add(rrcRequest.getRequestorID().toLowerCase());
		rrcRequestKeyList.add(rrcRequest.getRrcRequestKey());
		if (rrcRequest.getClaim() != null && rrcRequest.getClaim().getIntimation() != null) {
			hospitalIdList.add(rrcRequest.getClaim().getIntimation().getHospital());
		}
		
	}
		
	if(null != rrcRequestKeyList && !rrcRequestKeyList.isEmpty())
	{
		//if (null != hospitalTypeIdList && 0 != hospitalTypeIdList.size()) {
			rrcDetailsList = new ArrayList<RRCDetails>();
			final CriteriaBuilder rrcDetailsBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<RRCDetails> rrcDetailsCriteriaQuery = rrcDetailsBuilder
					.createQuery(RRCDetails.class);
			Root<RRCDetails> searchRootInfoForRRCDetails = rrcDetailsCriteriaQuery
					.from(RRCDetails.class);
			rrcDetailsCriteriaQuery.where(searchRootInfoForRRCDetails.<Long> get(
					"rrcRequest").in(rrcRequestKeyList));
			final TypedQuery<RRCDetails> rrcDetailsInfoQuery = entityManager
					.createQuery(rrcDetailsCriteriaQuery);
			rrcDetailsList = rrcDetailsInfoQuery.getResultList();
	} 
	
	if(null != rrcRequestKeyList && !rrcRequestKeyList.isEmpty())
	{
		//if (null != hospitalTypeIdList && 0 != hospitalTypeIdList.size()) {
		categoryList = new ArrayList<RRCCategory>();
			final CriteriaBuilder rrcDetailsBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<RRCCategory> rrcDetailsCriteriaQuery = rrcDetailsBuilder
					.createQuery(RRCCategory.class);
			Root<RRCCategory> searchRootInfoForRRCDetails = rrcDetailsCriteriaQuery
					.from(RRCCategory.class);
			rrcDetailsCriteriaQuery.where(searchRootInfoForRRCDetails.<Long> get(
					"rrcRequest").in(rrcRequestKeyList));
			final TypedQuery<RRCCategory> rrcDetailsInfoQuery = entityManager
					.createQuery(rrcDetailsCriteriaQuery);
			categoryList = rrcDetailsInfoQuery.getResultList();
	} 
	

	hospitalTypeInfoList = SHAUtils.getHospitalInformation(entityManager, hospitalIdList);
	
	List<SearchModifyRRCRequestTableDTO> tmpEmployeeDTOList = null;
	
	if(null != loginIdList && !loginIdList.isEmpty())
	{
		//if (null != hospitalTypeIdList && 0 != hospitalTypeIdList.size()) {
			tmpEmployeeList = new ArrayList<TmpEmployee>();
			final CriteriaBuilder rrcDetailsBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<TmpEmployee> rrcDetailsCriteriaQuery = rrcDetailsBuilder
					.createQuery(TmpEmployee.class);
			Root<TmpEmployee> searchRootInfoForRRCDetails = rrcDetailsCriteriaQuery
					.from(TmpEmployee.class);
			rrcDetailsCriteriaQuery.where(rrcDetailsBuilder.lower((searchRootInfoForRRCDetails.<String> get(
					"loginId"))).in(loginIdList));
			
			final TypedQuery<TmpEmployee> rrcDetailsInfoQuery = entityManager
					.createQuery(rrcDetailsCriteriaQuery);
			tmpEmployeeList = rrcDetailsInfoQuery.getResultList();
	} 
	if(doList != null && !doList.isEmpty()) {
		SearchRRCRequestMapper searchRRCRequestMapper =  SearchRRCRequestMapper.getInstance();
		 tableDTO = searchRRCRequestMapper.getRRCRequestList(doList);
		 
		 for (SearchRRCRequestTableDTO searchRRCRequestTableDTO : tableDTO) {
				if(searchRRCRequestTableDTO.getCpuCode() != null && searchRRCRequestTableDTO.getCpuDivString() != null){
					searchRRCRequestTableDTO.setCpuCode(searchRRCRequestTableDTO.getCpuCode() +" - "+ searchRRCRequestTableDTO.getCpuDivString());
				}
			}
		
	}
	
	if(null != tableDTO && !tableDTO.isEmpty())
	{
		for (SearchRRCRequestTableDTO SearchRRCRequestTableDTO : tableDTO) {
			if(null != SearchRRCRequestTableDTO.getRequestorSavedAmount())
				SearchRRCRequestTableDTO.setAmountSaved(String.valueOf(SearchRRCRequestTableDTO.getRequestorSavedAmount()));
			
			if(null != SearchRRCRequestTableDTO.getStatusKey()  && (ReferenceTable.RRC_REQUEST_PROCESS_STATUS).equals(SearchRRCRequestTableDTO.getStatusKey()))
			{
				if(null != SearchRRCRequestTableDTO.getRrcProcessedDate())
				{
				SearchRRCRequestTableDTO.setStatusDate(SearchRRCRequestTableDTO.getRrcProcessedDate().toString());
				}
			}
			else if(null != SearchRRCRequestTableDTO.getStatusKey()  && (ReferenceTable.RRC_REQUEST_REVIEWED_STATUS).equals(SearchRRCRequestTableDTO.getStatusKey()))
			{
				if(null != SearchRRCRequestTableDTO.getRrcModifedDate() && ("RRC MODIFY").equalsIgnoreCase(SearchRRCRequestTableDTO.getRrcType()))
					if(null != SearchRRCRequestTableDTO.getRrcModifedDate())
					{
					SearchRRCRequestTableDTO.setStatusDate(SearchRRCRequestTableDTO.getRrcModifedDate().toString());
					}
				else if (null != SearchRRCRequestTableDTO.getRrcReviewedDate() && ("RRC REVIEW").equalsIgnoreCase(SearchRRCRequestTableDTO.getRrcType()))
					if(null != SearchRRCRequestTableDTO.getRrcReviewedDate())
					{
					SearchRRCRequestTableDTO.setStatusDate(SearchRRCRequestTableDTO.getRrcReviewedDate().toString());
					}

				
			}
			
			if(null != tmpEmployeeList && !tmpEmployeeList.isEmpty())
			{
				for (TmpEmployee tmpEmpDetails : tmpEmployeeList) {
					if(SearchRRCRequestTableDTO.getRequestorId().equalsIgnoreCase(tmpEmpDetails.getLoginId()))
					{
						SearchRRCRequestTableDTO.setRequestorName(tmpEmpDetails.getEmployeeWithNames());
					}
				}
			}
			TmpEmployee tmpEmployee = getEmployeeName(SearchRRCRequestTableDTO.getRequestorId());
			if(tmpEmployee != null ){
				
				SearchRRCRequestTableDTO.setRequestorName(tmpEmployee.getEmpFirstName());
				
			}
			/*if(null != rrcDetailsList && !rrcDetailsList.isEmpty())
			{
								
				for (RRCDetails rrcDetails : rrcDetailsList) {
					if(SearchRRCRequestTableDTO.getKey().equals(rrcDetails.getRrcRequest()))
					{
						if(null != rrcDetails.getCreditTypeId())
						{
							if(("Credit").equalsIgnoreCase(rrcDetails.getCreditTypeId().getValue()))
							{
								SearchRRCRequestTableDTO.setCreditsEmployeeName(rrcDetails.getEmployeeName());
							}
							if(("Lapse").equalsIgnoreCase(rrcDetails.getCreditTypeId().getValue()))
							{
								SearchRRCRequestTableDTO.setLapseEmployeeName(rrcDetails.getEmployeeName());
							}
						}
					}
				}
			}*/
			
			if(null != rrcDetailsList && !rrcDetailsList.isEmpty())
			{
				List<RRCDetails> creditArrayList = new ArrayList<RRCDetails>();
				List<RRCDetails> lapseArrayList = new ArrayList<RRCDetails>();
				
				
				for (RRCDetails rrcDetails : rrcDetailsList) {
					if(SearchRRCRequestTableDTO.getKey().equals(rrcDetails.getRrcRequest()))
					{
						if(null != rrcDetails.getCreditTypeId())
						{
							if(("Credit").equalsIgnoreCase(rrcDetails.getCreditTypeId().getValue()))
							{
								creditArrayList.add(rrcDetails);
							}
							if(("Lapse").equalsIgnoreCase(rrcDetails.getCreditTypeId().getValue()))
							{
								lapseArrayList.add(rrcDetails);
							}
						}
					}
				}
				
								
				
				if(null != creditArrayList && !creditArrayList.isEmpty())
				{					
					SearchRRCRequestTableDTO.setCreditsEmployeeName(creditArrayList.get(0).getEmployeeName());
				
				 if( creditArrayList.size() >= 2)
				{						
					SearchRRCRequestTableDTO.setCreditsEmployeeName2(creditArrayList.get(1).getEmployeeName());
				}
				 if (creditArrayList.size() >= 3)
				{						
					SearchRRCRequestTableDTO.setCreditsEmployeeName3(creditArrayList.get(2).getEmployeeName());
				}
				 if(creditArrayList.size() >= 4)
				{						
					SearchRRCRequestTableDTO.setCreditsEmployeeName4(creditArrayList.get(3).getEmployeeName());
				}
				if(creditArrayList.size() >= 5)
				{						
					SearchRRCRequestTableDTO.setCreditsEmployeeName5(creditArrayList.get(4).getEmployeeName());					
				}
				 if(creditArrayList.size() >= 6)
				{						
					SearchRRCRequestTableDTO.setCreditsEmployeeName6(creditArrayList.get(5).getEmployeeName());
					
				}
				if(creditArrayList.size() >= 7)
				{						
					SearchRRCRequestTableDTO.setCreditsEmployeeName7(creditArrayList.get(6).getEmployeeName());	
					
				}
				}
				
				
				if(null != lapseArrayList && !lapseArrayList.isEmpty())
				{					
					SearchRRCRequestTableDTO.setLapseEmployeeName(lapseArrayList.get(0).getEmployeeName());
				
				 if( lapseArrayList.size() >= 2)
				{						
					SearchRRCRequestTableDTO.setLapseEmployeeName2(lapseArrayList.get(1).getEmployeeName());
				}
				 if (lapseArrayList.size() >= 3)
				{						
					SearchRRCRequestTableDTO.setLapseEmployeeName3(lapseArrayList.get(2).getEmployeeName());
				}
				 if(lapseArrayList.size() >= 4)
				{						
					SearchRRCRequestTableDTO.setLapseEmployeeName4(lapseArrayList.get(3).getEmployeeName());
				}
				if(lapseArrayList.size() >= 5)
				{						
					SearchRRCRequestTableDTO.setLapseEmployeeName5(lapseArrayList.get(4).getEmployeeName());					
				}
				 if(lapseArrayList.size() >= 6)
				{						
					SearchRRCRequestTableDTO.setLapseEmployeeName6(lapseArrayList.get(5).getEmployeeName());
					
				}
				if(lapseArrayList.size() >= 7)
				{						
					SearchRRCRequestTableDTO.setLapseEmployeeNmae7(lapseArrayList.get(6).getEmployeeName());	
					
				}
				
				
				
				String[] creditArray = new String[creditArrayList.size()];	
				String[] lapseArray = new String[lapseArrayList.size()];	
				
				for(int i=0;i<creditArray.length;i++)
				{
					creditArray[i] = creditArrayList.get(i).getEmployeeName();		
						
					
				}
				for(int i=0;i<lapseArray.length;i++)
				{
					lapseArray[i] = lapseArrayList.get(i).getEmployeeName();
				}
				
				SearchRRCRequestTableDTO.setCreditEmpList(creditArray);
				SearchRRCRequestTableDTO.setLapseEmpList(lapseArray);
			}
			
				
				}
			
			if(null != categoryList && !categoryList.isEmpty())
			{
				StringBuffer category = new StringBuffer();
				for (RRCCategory rrcCategory : categoryList) {
					if(SearchRRCRequestTableDTO.getKey().equals(rrcCategory.getRrcRequest()))
					{
						if(null != rrcCategory.getCategoryId())
						{
							category.append(rrcCategory.getCategoryId().getValue()).append( ",");
							
						}
					}
					
					SearchRRCRequestTableDTO.setCategoryValue(category.toString());
				}
			}
			
			if(null != hospitalTypeInfoList && !hospitalTypeInfoList.isEmpty())
			{
				for (Object hospObj : hospitalTypeInfoList) {
					Hospitals hospitalObj = (Hospitals)hospObj;
					if (hospitalObj.getKey() != null && SearchRRCRequestTableDTO.getHospitalId() != null ) {
						if(SearchRRCRequestTableDTO.getHospitalId().equals(hospitalObj.getKey()))
						{
							SearchRRCRequestTableDTO.setHospitalName(hospitalObj.getName());
							SearchRRCRequestTableDTO.setHospitalTypeId(hospitalObj.getHospitalType().getKey());
							if(SearchRRCRequestTableDTO.getHospitalTypeId().equals(ReferenceTable.NETWORK_HOSPITAL_TYPE_ID))
							{
								if(null != hospitalObj.getNetworkHospitalType())
								{
									SearchRRCRequestTableDTO.setHospitalType(hospitalObj.getNetworkHospitalType());
								}
								else
								{
									SearchRRCRequestTableDTO.setHospitalType(ReferenceTable.NETWORK_HOSPITAL_NAME);
								}
							}
							else
							{
								SearchRRCRequestTableDTO.setHospitalType(ReferenceTable.NON_NETWORK_HOSPITAL_NAME);
							}
						}
					}
					
				}
			}
			
			if(null != doList && !doList.isEmpty())
			{
				for (RRCRequest rrcRequest : doList) {
					
					if((ReferenceTable.RRC_REQUEST_REVIEWED_STATUS).equals(SearchRRCRequestTableDTO.getRrcStatusId()))
					{
						if(null != rrcRequest.getReviewerEligibilityTypeId())
							SearchRRCRequestTableDTO.setStatus(rrcRequest.getReviewerEligibilityTypeId().getValue());
						if(null != rrcRequest.getReviewedDate())
						{
							SearchRRCRequestTableDTO.setStatusDate(rrcRequest.getReviewedDate().toString());
						}
							SearchRRCRequestTableDTO.setRemarks(rrcRequest.getReviewRemarks());
					}
					
					else if((ReferenceTable.RRC_REQUEST_PROCESS_STATUS).equals(SearchRRCRequestTableDTO.getRrcStatusId()))
					{
						if(null != rrcRequest.getEligiblityTypeId())
							SearchRRCRequestTableDTO.setStatus(rrcRequest.getEligiblityTypeId().getValue());
						if(null != rrcRequest.getProcessedDate() )
						{
							SearchRRCRequestTableDTO.setStatusDate(rrcRequest.getProcessedDate().toString());
						}
							SearchRRCRequestTableDTO.setRemarks(rrcRequest.getEligibiltyRemarks());
					}
				}
				
			}
			
			List<OldInitiatePedEndorsement> pedDetails = getPedDetails(SearchRRCRequestTableDTO.getClaimKey());
			List<Long> pedIntiateKeyList = null;
			if(null != pedDetails){
				StringBuffer pedName = new StringBuffer();
				StringBuffer pedSuggestion = new StringBuffer();
				// The below list is added for storing ped initiate key.
				/**
				 * Since for the given claim one or more ped can be intiated.
				 * In such scenario , in ped intiate table we will have one record
				 * per intiate request. Hence List is required if multiple ped's are
				 * initiated.
				 * */
				pedIntiateKeyList = new ArrayList<Long>();
				for (OldInitiatePedEndorsement oldInitiatePedEndorsement : pedDetails) {
					
					 pedName.append(oldInitiatePedEndorsement.getPedName()).append(",");
					 pedSuggestion.append(oldInitiatePedEndorsement.getPedSuggestion().getValue()).append(",");
					
					SearchRRCRequestTableDTO.setInitialPEDRecommendation(pedName.toString());
					SearchRRCRequestTableDTO.setPedSuggestions(pedSuggestion.toString());
					pedIntiateKeyList.add(oldInitiatePedEndorsement.getKey());
					//SearchRRCRequestTableDTO.setPedInitiateKey(oldInitiatePedEndorsement.getKey());
				}
								
				
			}
			
			StringBuffer descriptionBuffer = new StringBuffer();
			if(null != pedIntiateKeyList && !pedIntiateKeyList.isEmpty())
			{
				for (Long long1 : pedIntiateKeyList) {
					
					List<NewInitiatePedEndorsement> pedDescription = getAllPedDetails(long1);
					if(null != pedDescription)
					{
						for (NewInitiatePedEndorsement newInitiatePedEndorsement : pedDescription) {
							
							SearchRRCRequestTableDTO.setPedCode(newInitiatePedEndorsement.getPedCode());
							PreExistingDisease description= getPedDescription(SearchRRCRequestTableDTO.getPedCode());
							if(null != description)
							{
								/*
								for (PreExistingDisease preExistingDisease : Description) {*/
								descriptionBuffer.append(description.getValue()).append(",");
									
									//pedDescriptions += description.getValue()+ ",";
									
								//}
								

						}
						
					}
				}
			}
				if(null != descriptionBuffer)
					SearchRRCRequestTableDTO.setPedName(descriptionBuffer.toString());
			
			/*List<NewInitiatePedEndorsement> pedDescription = getAllPedDetails(SearchRRCRequestTableDTO.getPedInitiateKey());
			if(null != pedDescription)
			{
				String pedDescriptions = "";
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : pedDescription) {
					
					SearchRRCRequestTableDTO.setPedCode(newInitiatePedEndorsement.getPedCode());
					PreExistingDisease description= getPedDescription(SearchRRCRequestTableDTO.getPedCode());
					if(null != description)
					{
						
						for (PreExistingDisease preExistingDisease : Description) {
							
							pedDescriptions += description.getValue()+ ",";
							
						//}
						SearchRRCRequestTableDTO.setPedName(pedDescriptions);

				}
				
			}
*/
		
				
			}
			
			
			List<InsuredPedDetails> pedDisclosed = getPedDisclosed(SearchRRCRequestTableDTO.getInsuredKey());
			if(null != pedDisclosed)
			{
				StringBuffer pedDisclose = new StringBuffer() ;
				for (InsuredPedDetails insuredPedDetails : pedDisclosed) {
					
					pedDisclose.append(insuredPedDetails.getPedDescription());
					SearchRRCRequestTableDTO.setPedDisclosed(pedDisclose.toString());
				}
				
			}
			
			Insured initialSumInsure = getInitialSumInsured(SearchRRCRequestTableDTO.getHealthCardNo());
			if(null != initialSumInsure)
			{
				SearchRRCRequestTableDTO.setInitialSumInsured(initialSumInsure.getInsuredSumInsured());
			}
			
			
		}
		
	}	
	}	return tableDTO;
}
}