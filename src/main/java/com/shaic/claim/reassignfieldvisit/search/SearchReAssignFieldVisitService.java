package com.shaic.claim.reassignfieldvisit.search;

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
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.FieldVisitRequest;
@Stateless
public class SearchReAssignFieldVisitService extends  AbstractDAO<FieldVisitRequest> {

	
	
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
	
	public SearchReAssignFieldVisitService(){
		
	}
	
	@SuppressWarnings("unchecked")
	public Page<SearchReAssignFieldVisitTableDTO> search(SearchReAssignFieldVisitFormDTO formDTO, String userName, String passWord)
	{
		try {
			
			String strIntimationNo = formDTO.getIntimationNo() != null ? formDTO.getIntimationNo()!= null ? formDTO.getIntimationNo() : null : null; ;
			String strPolicyNo = formDTO.getPolicyNo() != null ? formDTO.getPolicyNo()!= null ? formDTO.getPolicyNo() : null : null; ;;
			
			String hospitalName = formDTO.getHospitalName() != null ? formDTO.getHospitalName()!= null ? formDTO.getHospitalName(): null : null;
			String cpuCode =  null != formDTO && formDTO.getCpuCode() != null ? formDTO.getCpuCode().getId() != null ? formDTO.getCpuCode().getId().toString() : null : null;
			String hospitalType = formDTO.getHospitalType() != null ? formDTO.getHospitalType().getId() != null ? formDTO.getHospitalType().getId().toString(): null : null;
			String fvrCpuCode = formDTO.getFvrCpuCode() != null ? formDTO.getFvrCpuCode() != null ? formDTO.getFvrCpuCode() != null ? formDTO.getFvrCpuCode().toString() : null :null : null;
			Long productCode = formDTO.getProductCode() != null ? formDTO.getProductCode().getId() != null ? formDTO.getProductCode().getId() != null ? formDTO.getProductCode().getId() : null :null : null;
			
			
			List<FieldVisitRequest> fieldvisitList = new ArrayList<FieldVisitRequest>(); 
			
			List<SearchReAssignFieldVisitTableDTO> searchReAssignFieldVisitTableDTO  = new ArrayList<SearchReAssignFieldVisitTableDTO>();
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();			
			final CriteriaQuery<FieldVisitRequest> criteriaQuery = criteriaBuilder.createQuery(FieldVisitRequest.class);			
			Root<FieldVisitRequest> root = criteriaQuery.from(FieldVisitRequest.class);	
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			if (strPolicyNo != null && ! strPolicyNo.equalsIgnoreCase("")) {
				Predicate condition1 = criteriaBuilder.like(root.<Policy>get("policy").<String>get("policyNumber"), "%"+strPolicyNo+"%");
				conditionList.add(condition1);
			}
			
			if (strIntimationNo != null && ! strIntimationNo.equalsIgnoreCase("")) {
				Predicate condition2 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"),  "%"+strIntimationNo+"%");
				conditionList.add(condition2);
			}
			
			if (cpuCode != null) {
				Predicate condition3 = criteriaBuilder.equal(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode"), cpuCode);
				conditionList.add(condition3);
			}
			
			if (hospitalType != null) {
				Predicate condition4 = criteriaBuilder.equal(root.<Intimation>get("intimation").<MastersValue>get("hospitalType").<Long>get("key"),hospitalType);
				conditionList.add(condition4);
			}
			
			if(null != productCode)
			{
				Predicate condition7 = criteriaBuilder.equal(root.<Intimation>get("intimation").<Policy>get("policy").<Product>get("product").<Long>get("key"),productCode);
				conditionList.add(condition7);
			}
			
			if (fvrCpuCode != null) {
				
				
				String[] split = fvrCpuCode.split("-");
				String splitFvrCode = split[0];
				
				if(splitFvrCode != null) {
					splitFvrCode = splitFvrCode.replaceAll("\\s","");
					
				}
				Predicate condition5 = criteriaBuilder.equal(root.<Long>get("fvrCpuId"),splitFvrCode);
				conditionList.add(condition5);
				
			}
			
			if (hospitalName != null) {
//				Predicate condition6 = criteriaBuilder.equal(root.<Intimation>get("intimation").<Long>get("hospital"),hospitalName);
//				conditionList.add(condition6);
			}
			
			
			Predicate condition7 = criteriaBuilder.equal(root.<Status>get("status").get("key"),ReferenceTable.ASSIGNFVR);
			conditionList.add(condition7);
			
						
			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			
			final TypedQuery<FieldVisitRequest> typedQuery = entityManager.createQuery(criteriaQuery);
			fieldvisitList = typedQuery.getResultList();
			
			List<FieldVisitRequest> pageItemList = fieldvisitList;
			searchReAssignFieldVisitTableDTO = SearchReAssignFieldVisitMapper.getInstance()
					.getSearchPEDRequestApproveTableDTO(pageItemList);
			
			for (SearchReAssignFieldVisitTableDTO fieldVisitRequest : searchReAssignFieldVisitTableDTO) {
				if(fieldVisitRequest.getFvrCpuCode() != null){
					fieldVisitRequest.setStrFvrCpuCode(fieldVisitRequest.getFvrCpuCode().toString());
				}
			}
			
			for (SearchReAssignFieldVisitTableDTO fieldVisitTableDTO : searchReAssignFieldVisitTableDTO) {
				if(fieldVisitTableDTO.getHospitalNameID() != null){
					Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(fieldVisitTableDTO.getHospitalNameID());
					fieldVisitTableDTO.setHospitalName(hospitalDetailsByKey.getName());
					if(hospitalDetailsByKey.getHospitalType() != null){
						fieldVisitTableDTO.setHospitalType(hospitalDetailsByKey.getHospitalType().getValue());
					}
					
				}
				if (fieldVisitTableDTO.getDateOfAdmission() != null){
					fieldVisitTableDTO.setStrDateOfAdmission(SHAUtils.formatDate(fieldVisitTableDTO.getDateOfAdmission()));
				}
				if (fieldVisitTableDTO.getIntimationDate() != null){
					fieldVisitTableDTO.setStrDateOfIntimation(SHAUtils.formatDate(fieldVisitTableDTO.getIntimationDate()));
				}
			}


			Page<SearchReAssignFieldVisitTableDTO> page = new Page<SearchReAssignFieldVisitTableDTO>();
			Page<FieldVisitRequest> pagedList = super.pagedList(formDTO.getPageable());
			//page.setPageNumber(pagedList.getPageNumber());
			page.setPageItems(searchReAssignFieldVisitTableDTO);
			page.setIsDbSearch(true);
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
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		List<Hospitals> hospitals = (List<Hospitals>) query.getResultList();
		if (hospitals != null && ! hospitals.isEmpty()) {
			return hospitals.get(0);
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


}
