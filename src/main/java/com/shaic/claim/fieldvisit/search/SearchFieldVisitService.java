package com.shaic.claim.fieldvisit.search;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.criteria.Root;

//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claimrequest.ClaimRequestType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.classification.ClassificationType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.fieldvisit.FieldVisitType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.hospitalinfo.HospitalInfoType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.policy.PolicyType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.preauthreq.PreAuthReqType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.productinfo.ProductInfoType;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class SearchFieldVisitService extends  AbstractDAO<FieldVisitRequest> {
	
	
	/**
	 * Entity manager is created to load LOB value from master service.
	 * When created instance for master service and tried to reuse the same, 
	 * faced error in entity manager invocation. Also when user, @Inject or @EJB
	 * annotation, faced issues in invocation. Hence for time being created
	 * entity manager instance and using the same. Later will check with siva 
	 * for code.
	 * */
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchFieldVisitService(){
		
	}
	
	@SuppressWarnings("unchecked")
	public Page<SearchFieldVisitTableDTO> search(SearchFieldVisitFormDTO formDTO, String userName, String passWord)
	{
		try {
			
			String strIntimationNo = "";
			String strPolicyNo = "";
			String strType = "";
			String strClaimType = "";
			List<Map<String, Object>> taskProcedure = null ;
			
			String priority = formDTO.getPriority() != null ? formDTO.getPriority().getValue() != null ? formDTO.getPriority().getValue() : null : null;
			String source = formDTO.getSource() != null ? formDTO.getSource().getValue() != null ? formDTO.getSource().getValue(): null : null;
			String type = formDTO.getType() != null ? formDTO.getType().getValue() != null ? formDTO.getType().getValue(): null : null;
			String productName = null != formDTO && null != formDTO.getProductCode() ? formDTO.getProductCode().getValue() : null;
			String productId = null != formDTO && null != formDTO.getProductCode() ? formDTO.getProductCode().getId() != null ? formDTO.getProductCode().getId().toString() : null : null; 
			String hospitaltype = null != formDTO && null != formDTO.getHospitalType()? formDTO.getHospitalType().getValue() : null;
			String cpuCode = formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId().toString() : null : null : null;
			String fvrCpuCode = formDTO.getFvrCpuCode() != null ? formDTO.getFvrCpuCode().getId() != null ? formDTO.getFvrCpuCode().getId() != null ? formDTO.getFvrCpuCode().getId().toString() : null : null : null;
			String claimType = formDTO.getClaimType() != null ? formDTO.getClaimType().getValue() != null ? formDTO.getClaimType().getValue(): null : null;
			
			Date admissionDate = formDTO.getIntimatedDate();



		//	PayloadBOType payloadBO = new PayloadBOType();

			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
			
			Integer totalRecords = 0; 
			
			List<Long> keys = new ArrayList<Long>(); 
			
			
			mapValues.put(SHAConstants.USER_ID, userName);
			mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.FVR_CURRENT_QUEUE);	
		/*	
			ProductInfoType productInfo = new ProductInfoType();
			
			
			intimationType.setReason("HEALTH");
			
			productInfo.setLob("H");

			
			payloadBO.setProductInfo(productInfo);
			
			payloadBO.setIntimation(intimationType);*/
			
			if(null != formDTO.getIntimationNo() && ! formDTO.getIntimationNo().equals(""))
			{			
				strIntimationNo = formDTO.getIntimationNo();
				mapValues.put(SHAConstants.INTIMATION_NO, strIntimationNo);
			}
			if(null != formDTO.getPolicyNo()  && ! formDTO.getPolicyNo().equals(""))
			{
				strPolicyNo= formDTO.getPolicyNo();
				mapValues.put(SHAConstants.POLICY_NUMBER, strPolicyNo);
			}
			
			if(null != formDTO.getClaimType() && ! formDTO.getClaimType().equals(""))
			{
				strClaimType= formDTO.getClaimType().getValue();
				mapValues.put(SHAConstants.CLAIM_TYPE, strClaimType.toUpperCase());
			}
			

			if(productName != null){				
					
					/*String[] split = productName.split("-");
					String prodName = split[0];
					
					if(prodName != null) {
						prodName = prodName.replaceAll("\\s","");
						//mapValues.put(SHAConstants.PAYLOAD_REFERENCE_USER_ID, refered);
*/						mapValues.put(SHAConstants.PRODUCT_NAME, productName);
					//}
					//mapValues.put(SHAConstants.PRODUCT_NAME, productId);
				}
				

//			if(admissionDate != null){
//				
//				
//				String intimDate = SHAUtils.formatIntimationDateValue(admissionDate);
//			    intimationType.setStatus(intimDate);
//			    
//			    payloadBO.setIntimation(intimationType);
//			}

			
			
			if(admissionDate != null){				
				
				//DATE createdDate;
//				String intimDate = SHAUtils.formatIntimationDateValue(admissionDate);
			    //intimationType.setStatus(intimDate);					
				java.sql.Date createdDate = new java.sql.Date(admissionDate.getTime());
				//String dateFormat = SHAUtils.getDateFormat(createdDate.toString());
//				Timestamp admissionDate1=new Timestamp(admissionDate.getTime());
//				Date admission = SHAUtils.getFromDate(admissionDate1);
				
			    mapValues.put(SHAConstants.ADMISSION_DATE, createdDate);
			
			}			
		
			if(cpuCode != null){				
				mapValues.put(SHAConstants.CPU_CODE, cpuCode);
			}
			
		/*	if(fvrCpuCode != null){			

				ClaimRequestType claimRequestType = new ClaimRequestType();
			if(cpuCode != null){
				claimRequestType.setCpuCode(cpuCode);
				
				payloadBO.setClaimRequest(claimRequestType);
			}
			*/
			if(fvrCpuCode != null){
				

				
				mapValues.put(SHAConstants.PAYLOAD_FVR_CPU_CODE, fvrCpuCode);				
			}
			

			
			if(null != hospitaltype){	
				mapValues.put(SHAConstants.HOSPITAL_TYPE, hospitaltype);				

		/*	HospitalInfoType hospitalInfoType = new HospitalInfoType();
			if(null != hospitaltype){
				
				
				hospitalInfoType.setHospitalType(hospitaltype);
				payloadBO.setHospitalInfo(hospitalInfoType);*/

			}
			
			
			
		    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
						|| type != null && ! type.isEmpty()){
			
					/*
					if(priority != null && ! priority.isEmpty())
						if(priority.equalsIgnoreCase(SHAConstants.ALL)){
							priority = null;
						}
					classification.setPriority(priority);
					if(source != null && ! source.isEmpty()){
						classification.setSource(source);
					}*/
					if(priority != null && ! priority.isEmpty())
						if(!priority.equalsIgnoreCase(SHAConstants.ALL)){
							mapValues.put(SHAConstants.PRIORITY, priority);
						}
				
					if(source != null && ! source.isEmpty()){
						mapValues.put(SHAConstants.STAGE_SOURCE, source);	
					}
					
					if(type != null && ! type.isEmpty()){
						if(!type.equalsIgnoreCase(SHAConstants.ALL)){
							//type = null;
							mapValues.put(SHAConstants.RECORD_TYPE, type);	
						}

						//classification.setType(type);
					}
					
								}		

			
			/**
			 * BPM integration starts.
			 * 
			 * */

			Pageable pageable = formDTO.getPageable();
			
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
			List<SearchFieldVisitTableDTO> searchFieldVisitTableDTO  = new ArrayList<SearchFieldVisitTableDTO>();
			
			  List<SearchPreauthTableDTO> searchPreAuthTableDTO = new ArrayList<SearchPreauthTableDTO>();
				
				Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
				Map<Long,Double> claimedAmountMap = new WeakHashMap<Long, Double>();
				
				Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				taskProcedure = dbCalculationService.revisedGetTaskProcedure(setMapValues);	
				
					if (null != taskProcedure) {
						for (Map<String, Object> outPutArray : taskProcedure) {							
								Long keyValue = Long.parseLong(String.valueOf(outPutArray.get(SHAConstants.FVR_KEY)));
								workFlowMap.put(keyValue,outPutArray);
							keys.add(keyValue);
							
							totalRecords = (Integer) outPutArray
									.get(SHAConstants.TOTAL_RECORDS);
						}
						 		
					
					
			    	
				
				System.out.println("--the fvr keys----"+keys);
				
				/**
				 * BPM Integration ends
				 * */
				if( null != keys && 0 != keys.size())
				{
					List<FieldVisitRequest> resultList = new ArrayList<FieldVisitRequest>();
					
					final CriteriaBuilder IntimationBuilder = entityManager.getCriteriaBuilder();
					final CriteriaQuery<FieldVisitRequest> criteriaQuery = IntimationBuilder
							.createQuery(FieldVisitRequest.class);
					Root<FieldVisitRequest> searchRootForIntimation = criteriaQuery.from(FieldVisitRequest.class);
					criteriaQuery.where(searchRootForIntimation.<Long> get("key").in(keys));
					
					criteriaQuery.orderBy(IntimationBuilder.asc(searchRootForIntimation.<Claim>get("claim").<Intimation>get("intimation").<Date>get("admissionDate")));
					
					final TypedQuery<FieldVisitRequest> claimInfoQuery = entityManager
							.createQuery(criteriaQuery);

					resultList =claimInfoQuery.getResultList();
					
					//resultList = SHAUtils.getIntimationInformation(SHAConstants.PREAUTH_SEARCH_SCREEN, entityManager, keys);
					List<FieldVisitRequest> pageItemList = resultList;
					searchFieldVisitTableDTO = SearchFieldVisitMapper.getInstance()
							.getSearchPEDRequestApproveTableDTO(pageItemList);
					
					for (SearchFieldVisitTableDTO tableDto : searchFieldVisitTableDTO) {
						
						if(tableDto.getKey() != null){
							Object workflowKey = workFlowMap.get(tableDto.getKey());
							tableDto.setDbOutArray(workflowKey);
						}
						
						if(tableDto.getCpuCode() != null){
							tableDto.setStrCpuCode(tableDto.getCpuCode().toString());
						}else if(tableDto.getCpucode() != null){
							tableDto.setStrCpuCode(tableDto.getCpucode());
						}
						
					}
					
					for (SearchFieldVisitTableDTO fieldVisitRequest : searchFieldVisitTableDTO) {
						if(fieldVisitRequest.getFvrCpuCode() != null){
							fieldVisitRequest.setStrFvrCpuCode(fieldVisitRequest.getFvrCpuCode().toString());
						}
					}
					
					for (SearchFieldVisitTableDTO fieldVisitRequest : searchFieldVisitTableDTO) {
						if(fieldVisitRequest != null && fieldVisitRequest.getDateOfAdmission() != null){
							String formatDate = SHAUtils.formatDate(fieldVisitRequest.getDateOfAdmission());
							fieldVisitRequest.setStrDateOfAdmission(formatDate);
						}
						if(fieldVisitRequest != null && fieldVisitRequest.getIntimationDate() != null){
							String formatDate = SHAUtils.formatDate(fieldVisitRequest.getIntimationDate());
							fieldVisitRequest.setStrDateOfIntimation(formatDate);
						}
					}
					/*
					 * Assigning Human task to table DTO.
					 * */
					for(SearchFieldVisitTableDTO objSearchFieldVistTblDTO : searchFieldVisitTableDTO)
					{
						//objSearchFieldVistTblDTO.setHumanTask(humanTaskMap.get(objSearchFieldVistTblDTO.getKey()));		
						//objSearchFieldVistTblDTO.setTaskNumber(taskNumberMap.get(objSearchFieldVistTblDTO.getKey()));
						if(null != objSearchFieldVistTblDTO.getLob() && !("").equals(objSearchFieldVistTblDTO.getLob()))
						{
							objSearchFieldVistTblDTO.setLob(loadLobValue(Long.parseLong(objSearchFieldVistTblDTO.getLob())));
						}
					}
					
					for(SearchFieldVisitTableDTO hospitalsFieldVistTblDTO : searchFieldVisitTableDTO){
//						hospitalsFieldVistTblDTO.setHumanTask(humanTaskMap.get(hospitalsFieldVistTblDTO.getKey()));
//						hospitalsFieldVistTblDTO.setTaskNumber(taskNumberMap.get(hospitalsFieldVistTblDTO.getKey()));
						
						
						if(null != hospitalsFieldVistTblDTO.getHospitalNameID() && !("").equals(hospitalsFieldVistTblDTO.getHospitalNameID())){
							
							Query findByHospitalKey = entityManager.createNamedQuery(
									"Hospitals.findByKey").setParameter("key", hospitalsFieldVistTblDTO.getHospitalNameID());
							Hospitals hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
							if(hospitalDetail !=null){
								hospitalsFieldVistTblDTO.setHospitalName(hospitalDetail.getName());
							}
						}
					}
					
					
				}
			}

			Page<SearchFieldVisitTableDTO> page = new Page<SearchFieldVisitTableDTO>();
			Page<FieldVisitRequest> pagedList = super.pagedList(formDTO.getPageable());
			//page.setPageNumber(pagedList.getPageNumber());
			page.setPageItems(searchFieldVisitTableDTO);
			page.setTotalRecords(totalRecords);
			//page.setPagesAvailable(pagedList.getPagesAvailable());
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	@SuppressWarnings("unchecked")
	public FieldVisitRequest getPreauthKey (Long preauthkey) {
		
		Query findByKey = entityManager.createNamedQuery("FieldVisitRequest.findAll");

		List<FieldVisitRequest> preauthKeyList = (List<FieldVisitRequest>) findByKey
				.getResultList();

		if (!preauthKeyList.isEmpty()) {
			return preauthKeyList.get(0);

		}
		return null;
	}

	@Override
	public Class<FieldVisitRequest> getDTOClass() {
		return FieldVisitRequest.class;
	}
	
	/**
	 * Method to load Lob value
	 * 
	 * */
	public String loadLobValue(Long lobID)
	{
		MastersValue a_mastersValue = new MastersValue();
		if (lobID != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", lobID);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue.getValue();
		
		
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
}