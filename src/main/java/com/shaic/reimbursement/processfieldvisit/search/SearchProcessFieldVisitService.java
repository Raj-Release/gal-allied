
/**
 * 
 */
package com.shaic.reimbursement.processfieldvisit.search;

import java.util.ArrayList;
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
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.FieldVisitRequest;



/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchProcessFieldVisitService extends AbstractDAO<Intimation>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	public SearchProcessFieldVisitService() {
		super();
		
	}
	
	public  Page<SearchFieldVisitTableDTO> search(
			SearchProcessFieldVisitFormDTO searchFormDTO,
			String userName, String passWord) {
		
		/*try{
			String intimationNo = null!= searchFormDTO &&  null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() :null;
			String policyNo = null!= searchFormDTO &&  null != searchFormDTO.getPolicyNo()? searchFormDTO.getPolicyNo():null;
			String cpuCode = null!= searchFormDTO && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() != null ? searchFormDTO.getCpuCode().getId().toString() : null : null;
			
			String priority = null!= searchFormDTO && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null!= searchFormDTO && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null!= searchFormDTO && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			
			String productName = null != searchFormDTO && null != searchFormDTO.getProductCode() ? searchFormDTO.getProductCode().getValue() : null;
			String productId = null != searchFormDTO && null != searchFormDTO.getProductCode() ? searchFormDTO.getProductCode().getId() != null ? searchFormDTO.getProductCode().getId().toString() : null : null; 
			String hospitaltype = null != searchFormDTO && null != searchFormDTO.getHospitalType()? searchFormDTO.getHospitalType().getValue() : null;
			String fvrCpuCode = searchFormDTO.getFvrCpuCode() != null ? searchFormDTO.getFvrCpuCode().getId() != null ? searchFormDTO.getFvrCpuCode().getId() != null ? searchFormDTO.getFvrCpuCode().getId().toString() : null : null : null;
			Date admissionDate = searchFormDTO.getIntimatedDate();
			
				Long fvrKey = null;
				Long rodKey = null;
								
				List<String> intimationNoList = new ArrayList<String>();
				
				List<Long> fvrKeyList = new ArrayList<Long>();
				List<Long> rodKeyList = new ArrayList<Long>();
				List<Integer> taskNumber = new ArrayList<Integer>();
				
				
				
				if(null != intimationNo && !intimationNo.isEmpty() ){
					
					 if(payloadBOType == null){
					    	payloadBOType = new PayloadBOType();
					 }
					
					intimationType.setIntimationNumber(intimationNo);
					payloadBOType.setIntimation(intimationType);
					}
					PolicyType policyType = new PolicyType();
					if(null != policyNo && !policyNo.isEmpty()){
						
						 if(payloadBOType == null){
						    	payloadBOType = new PayloadBOType();
						 }
						
						policyType.setPolicyId(policyNo);
						payloadBOType.setPolicy(policyType);
					}
					ClaimRequestType claimRequestType = new ClaimRequestType(); 
					if(null != cpuCode){
						
						 if(payloadBOType == null){
						    	payloadBOType = new PayloadBOType();
						 }
						
						claimRequestType.setCpuCode(cpuCode);
						payloadBOType.setClaimRequest(claimRequestType);
					}
					
					ProductInfoType productType = new ProductInfoType();
					if(productName != null){
//						productType.setProductName(productName);
						
						if(productId != null){
							productType.setProductId(productId);
						}
						
						if(payloadBOType == null){
							payloadBOType = new PayloadBOType();;
						}
						
						payloadBOType.setProductInfo(productType);
					}
					

					if(admissionDate != null){
						RRCType rrcType = new RRCType();
						if(payloadBOType == null){
							payloadBOType = new PayloadBOType();
						}
						String intimDate = SHAUtils.formatIntimationDateValue(admissionDate);
						rrcType.setFromDate(intimDate);
					    
					    payloadBOType.setRrc(rrcType);
					}
					
					HospitalInfoType hospitalInfoType = new HospitalInfoType();
					if(null != hospitaltype){
						
						if(payloadBOType == null){
							payloadBOType = new PayloadBOType();
						}
						
						hospitalInfoType.setHospitalType(hospitaltype);
						payloadBOType.setHospitalInfo(hospitalInfoType);
					}
					
					if(fvrCpuCode != null){
						
						if(payloadBOType == null){
							payloadBOType = new PayloadBOType();
						}
						
						FieldVisitType fieldVisitType = new FieldVisitType();
						fieldVisitType.setRequestedBy(fvrCpuCode);
						payloadBOType.setFieldVisit(fieldVisitType);
					}
					
					ClassificationType classification = null;
					
				    if(priority != null && ! priority.isEmpty() || source != null && ! source.isEmpty()
								|| type != null && ! type.isEmpty()){
							classification = new ClassificationType();
							
							if(priority != null && ! priority.isEmpty())
								if(priority.equalsIgnoreCase(SHAConstants.ALL)){
									priority = null;
								}
							classification.setPriority(priority);
							if(source != null && ! source.isEmpty()){
								classification.setSource(source);
							}
							
							if(type != null && ! type.isEmpty()){
								if(type.equalsIgnoreCase(SHAConstants.ALL)){
									type = null;
								}
								classification.setType(type);
							}
							
							 if(payloadBOType == null){
							    	payloadBOType = new PayloadBOType();
							 }
							payloadBOType.setClassification(classification);
					}
				
				
				ProcessFieldVisitTask processFieldVisitTask = BPMClientContext.getProcessFieldVisitTask(userName, passWord);
				
				Pageable pageable = searchFormDTO.getPageable();
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
				
				PagedTaskList taskList;

				taskList = processFieldVisitTask.getTasks(userName,pageable,payloadBOType);

				if(null != processFieldVisitTask){
					List<HumanTask> humanTaskList = taskList.getHumanTasks();
					if(null == humanTaskList | humanTaskList.isEmpty()){
						return null;
					}
					
					for(HumanTask humanTask : humanTaskList){
						PayloadBOType payloadBO = humanTask.getPayload();
						if(payloadBO.getFieldVisit() != null){
						intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
					
						fvrKeyList.add(payloadBO.getFieldVisit().getKey());
						if(null != payloadBO.getRod())
							rodKeyList.add(payloadBO.getRod().getKey());
						    taskNumber.add(humanTask.getNumber());
						    humanTaskListDTO.add(humanTask);
						}
					}
				}
				List<SearchFieldVisitTableDTO> tableDTO = new ArrayList<SearchFieldVisitTableDTO>();
				for(int index = 0 ; index < intimationNoList.size() ; index++){
					if(index < fvrKeyList.size()){
					intimationNo = intimationNoList.get(index);
					humanTaskDTO = humanTaskListDTO.get(index);
		
					fvrKey = fvrKeyList.get(index);
					if(null != rodKeyList && !rodKeyList.isEmpty())
						try
					{
						rodKey = rodKeyList.get(index);
						
					}
					catch(Exception e)
					{
						
					}
					Integer taskNo = taskNumber.get(index);
					tableDTO.addAll(getIntimationData(intimationNo, fvrKey,rodKey, humanTaskDTO,taskNo));
					}
				}
				
				*//**
				 * sorting the dto based on date of admission date
				 *//*
				
				Collections.sort(tableDTO, new Comparator<SearchFieldVisitTableDTO>(){
				     public int compare(SearchFieldVisitTableDTO o1, SearchFieldVisitTableDTO o2){
				        return o1.getDateOfAdmission().compareTo(o2.getDateOfAdmission());
				     }
				});

				Page<SearchFieldVisitTableDTO> page = new Page<SearchFieldVisitTableDTO>();
				page.setPageNumber(taskList.getCurrentPage());
				page.setTotalRecords(taskList.getTotalRecords());
				page.setPageItems(tableDTO);
				return page;
				}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			}
				return null;	*/
			return null;
			
	}

	
	
	@SuppressWarnings("null")
	private List<SearchFieldVisitTableDTO> getIntimationData(String intimationNo,Long fvrKey,Long rodKey,Integer taskNumber){
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
			List<SearchFieldVisitTableDTO> tableDTO = SearchProcessFieldVisitMapper.getInstance().getIntimationDTO(doList);
			
			for (SearchFieldVisitTableDTO searchFieldVisitTableDTO : tableDTO) {
				
				if(searchFieldVisitTableDTO.getCpuId() != null){
	        		
					Long cpuCode = searchFieldVisitTableDTO.getCpuId();
					searchFieldVisitTableDTO.setStrCpuCode(String.valueOf(cpuCode));
				}
				
				if(searchFieldVisitTableDTO.getDateOfAdmission() != null){
					String dateformat = SHAUtils.formatDate(searchFieldVisitTableDTO.getDateOfAdmission());
					searchFieldVisitTableDTO.setStrDateOfAdmission(dateformat);
				}
				
				if(searchFieldVisitTableDTO.getIntimationDate() != null){
					String dateformats = SHAUtils.formatDate(searchFieldVisitTableDTO.getIntimationDate());
					searchFieldVisitTableDTO.setStrDateOfIntimation(dateformats);
				}
			}
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			tableDTO = getclaimNumber(tableDTO);
			tableDTO = getHospitalDetails(tableDTO, fvrKey,rodKey,taskNumber);
		
		return tableDTO;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	private List<SearchFieldVisitTableDTO> getclaimNumber(List<SearchFieldVisitTableDTO> intimationList){
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
							intimationList.get(index).setClaimNo(a_claim.getClaimId());
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


	private List<SearchFieldVisitTableDTO> getHospitalDetails(
			List<SearchFieldVisitTableDTO> tableDTO, Long fvrKey,Long rodKey, Integer taskNumber) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			 /*tableDTO.get(index).setHumanTask(humanTask);
			 tableDTO.get(index).setHumanTaskDTO(humanTask);*/
			 tableDTO.get(index).setKey(fvrKey);
			 tableDTO.get(index).setFvrKey(fvrKey);
			 tableDTO.get(index).setRodKey(rodKey);
			 tableDTO.get(index).setTaskNumber(taskNumber);
			 
			 if(fvrKey != null){
				 FieldVisitRequest fieldVisitRequestByKey = getFieldVisitRequestByKey(fvrKey);
				 if(fieldVisitRequestByKey != null && fieldVisitRequestByKey.getFvrCpuId() != null){
					 tableDTO.get(0).setStrFvrCpuCode(fieldVisitRequestByKey.getFvrCpuId().toString());
				 }
			 }
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
			
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
//				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	@SuppressWarnings("unchecked")
	public FieldVisitRequest getFieldVisitRequestByKey(Long key) {
		Query query = entityManager.createNamedQuery(
				"FieldVisitRequest.findByKey").setParameter("primaryKey", key);
		
		List<FieldVisitRequest> fvrList=(List<FieldVisitRequest>) query.getResultList();
		if(fvrList != null && ! fvrList.isEmpty()){
			return fvrList.get(0);
		}
		
		return null;
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
	
	
	/*private Long getFVRKey(Long rodKey){
		try{
			Query findClaimKey = entityManager.createNamedQuery("Reimbursement.findByKey").setParameter("primaryKey", rodKey);
			List<Reimbursement> reimbursementList = findClaimKey.getResultList();
			return getFVRByClaimKey(reimbursementList.get(0).getClaim().getKey());
			}catch(Exception e){
				return null;
			}
	}
	
	private Long getFVRByClaimKey(Long claimKey){
		
		try{
			Query findFVRKey = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKey").setParameter("claimKey", claimKey);
			List<FieldVisitRequest> fieldVisitRequest =  findFVRKey.getResultList();
			return fieldVisitRequest.get(0).getKey();
			}catch(Exception e){
				return null;
			}
		
	}*/
}
	
	
	
