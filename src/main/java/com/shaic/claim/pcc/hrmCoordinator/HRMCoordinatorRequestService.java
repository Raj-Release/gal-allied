package com.shaic.claim.pcc.hrmCoordinator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.pcc.SearchProcessPCCRequestMapper;
import com.shaic.claim.pcc.beans.MasterPCCRole;
import com.shaic.claim.pcc.beans.PCCCategory;
import com.shaic.claim.pcc.beans.PCCQuery;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.PCCQueryDetailsTableDTO;
import com.shaic.claim.pcc.dto.PCCReplyDetailsTableDTO;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestFormDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasUser;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class HRMCoordinatorRequestService {


	@PersistenceContext
	protected EntityManager entityManager;
	
	Map<Long, Object> workFlowMap= null;

	@EJB
	private PreauthService preauthService;

	public Page<SearchProcessPCCRequestTableDTO> search(SearchProcessPCCRequestFormDTO searchFormDTO, String userName,String passWord) {


		try{
			userName = userName.toUpperCase();
			String intimationNo = searchFormDTO.getIntimationNo() != null && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo():null;
			Long cpuCode = searchFormDTO.getCpuCode() != null && searchFormDTO.getCpuCode().getId() !=null ? searchFormDTO.getCpuCode().getId():null;
			Long source = searchFormDTO.getSource() != null && searchFormDTO.getSource().getId() !=null ? searchFormDTO.getSource().getId():null;
			Long pccCatagory = searchFormDTO.getPccCatagory() != null && searchFormDTO.getPccCatagory().getId() !=null ? searchFormDTO.getPccCatagory().getId():null;
			String queryType = searchFormDTO.getPccQueryType() != null && searchFormDTO.getPccQueryType().getValue() !=null ? searchFormDTO.getPccQueryType().getValue():null;
			String productName = null != searchFormDTO && null != searchFormDTO.getProductName() ? searchFormDTO.getProductName().getValue() : null;

			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<SearchProcessPCCRequestTableDTO> tableDTO = new ArrayList<SearchProcessPCCRequestTableDTO>();
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			Long totalCount = 0l;
			List<PCCRequest> doList = new ArrayList<PCCRequest>();
			final CriteriaQuery<PCCRequest> criteriaQuery = criteriaBuilder.createQuery(PCCRequest.class);
			Root<PCCRequest> root = criteriaQuery.from(PCCRequest.class);
			List<Long> statusListKey= new ArrayList<Long>();
			statusListKey.add(SHAConstants.PCC_HRMC_ASSIGNED_STATUS);
			
			List<Predicate> requestconditionList1 = new ArrayList<Predicate>();
			CriteriaBuilder qb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Long> cq = qb.createQuery(Long.class);
			Root<PCCRequest> requestroot1 = cq.from(PCCRequest.class);
			
			List<Long> cpuCodeBasedonRoleList= getCPUCodeList(userName,SHAConstants.HRM_COORDINATOR_ROLE);
			System.out.println(String.format("CPU Code List [%s]", cpuCodeBasedonRoleList));

			List<Predicate> conditionList = new ArrayList<Predicate>();
			if(intimationNo != null){
				Predicate condition1 = criteriaBuilder.like(root.<String>get("intimationNo"),"%"+intimationNo+"%");
				conditionList.add(condition1);
				
				Predicate condition11 = qb.like(requestroot1.<String>get("intimationNo"),"%"+intimationNo+"%");
				requestconditionList1.add(condition11);
			}
			if(cpuCode != null){
				Predicate condition2 = criteriaBuilder.equal(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuCode);
				conditionList.add(condition2);
				
				Predicate condition22 = qb.equal(requestroot1.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuCode);
				requestconditionList1.add(condition22);
			}
			if(source != null){
				Predicate condition3 = criteriaBuilder.equal(root.<MastersValue>get("pccSource").<Long>get("key"),source);
				conditionList.add(condition3);
				
				Predicate condition33 = qb.equal(requestroot1.<MastersValue>get("pccSource").<Long>get("key"),source);
				requestconditionList1.add(condition33);
			}
			if(pccCatagory != null){
				Predicate condition4 = criteriaBuilder.equal(root.<PCCCategory>get("pccCategory").<Long>get("key"), pccCatagory);
				conditionList.add(condition4);
				
				Predicate condition44 = qb.equal(requestroot1.<PCCCategory>get("pccCategory").<Long>get("key"), pccCatagory);
				requestconditionList1.add(condition44);
			}
			
			if(productName != null){
				Predicate condition5 = criteriaBuilder.equal(root.<Intimation>get("intimation").<Policy>get("policy").<Product>get("product").<String> get("value"), productName);
				conditionList.add(condition5);
				
				Predicate condition55 = qb.equal(requestroot1.<Intimation>get("intimation").<Policy>get("policy").<Product>get("product").<String> get("value"), productName);
				requestconditionList1.add(condition55);
			}


			Predicate statusCondition =  root.<Status>get("status").get("key").in(statusListKey); 
			conditionList.add(statusCondition);
			
			Predicate statusCondition1 =  requestroot1.<Status>get("status").get("key").in(statusListKey); 
			requestconditionList1.add(statusCondition1);
			
			if(cpuCode == null){
				Predicate cpuCodeCondition =  root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode").in(cpuCodeBasedonRoleList); 
				conditionList.add(cpuCodeCondition);
			}
			
			if(cpuCode == null){
				Predicate cpuCodeCondition1 =  requestroot1.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode").in(cpuCodeBasedonRoleList); 
				requestconditionList1.add(cpuCodeCondition1);
			}
			
			//For total count - start
			cq.select(qb.count(requestroot1));
			cq.where(qb.and(requestconditionList1.toArray(new Predicate[] {})));
			totalCount =  entityManager.createQuery(cq).getSingleResult();
			//For total count - end

			if(intimationNo == null && cpuCode == null && source == null && pccCatagory == null && productName == null ){
				criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
				criteriaQuery.orderBy(criteriaBuilder.asc(root.<Intimation>get("intimation").<Date>get("createdDate")));
			} else{
				criteriaQuery.select(root).where(
						criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				criteriaQuery.orderBy(criteriaBuilder.asc(root.<Intimation>get("intimation").<Date>get("createdDate")));
			}

			final TypedQuery<PCCRequest> typedQuery = entityManager.createQuery(criteriaQuery);

			if(pageNumber > 1){
				firtResult = (pageNumber-1) *10;
			}else{
				firtResult = 0;
			}

			if(intimationNo == null){
				doList = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			}else{
				doList = typedQuery.getResultList();
			}
			tableDTO = SearchProcessPCCRequestMapper.getInstance().getProcessPCCRequestTableDTOs(doList);
			
			getHospitalDetails(tableDTO);
			
			//Filtering out CPU Code based on users.
			
			/*List<SearchProcessPCCRequestTableDTO> finalList = new ArrayList<SearchProcessPCCRequestTableDTO>();
			
			for(SearchProcessPCCRequestTableDTO oneTableDto : tableDTO){
				Long cpuCodeForUsers = Long.parseLong(oneTableDto.getCpuName());
				if(cpuCodeBasedonRoleList.contains(cpuCodeForUsers)){
					finalList.add(oneTableDto);
					
				}
				
			}*/
							
			List<SearchProcessPCCRequestTableDTO> result = new ArrayList<SearchProcessPCCRequestTableDTO>();
			//result.addAll(finalList);
			result.addAll(tableDTO);
			Page<SearchProcessPCCRequestTableDTO> page = new Page<SearchProcessPCCRequestTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber+1);
			page.setHasNext(true);
			if(result.isEmpty()){
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);
			//page.setIsDbSearch(true);
			page.setTotalRecords(totalCount.intValue());

			return page;
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}

	public PCCRequest getPCCRequestByKey(Long key) {
		Query findByKey = entityManager.createNamedQuery("PCCRequest.findByKey");
		findByKey = findByKey.setParameter("key", key);
		List<PCCRequest> pccList = findByKey.getResultList();
		if(null != pccList && !pccList.isEmpty()){
			entityManager.refresh(pccList.get(0));
			return pccList.get(0);
		}
		return null;
	}

	public PCCQuery getPCCQueryByKey(Long key) {
		Query findByKey = entityManager.createNamedQuery("PCCQuery.findByKey");
		findByKey = findByKey.setParameter("key", key);
		List<PCCQuery> pccList = findByKey.getResultList();
		if(null != pccList && !pccList.isEmpty()){
			entityManager.refresh(pccList.get(0));
			return pccList.get(0);
		}
		return null;
	}
	
	public void submitHRMCoordinator(PccDTO pccDTO,String userName,PccDetailsTableDTO pccDetailsTableDTO){

		if(pccDTO.getPccKey() !=null && pccDetailsTableDTO.getReponceQueryKey() !=null){
			userName = userName.toUpperCase();
			if(pccDTO.getIsNegotiation() !=null){
				PCCRequest pccRequest= getPCCRequestByKey(pccDTO.getPccKey());
				PCCQuery pccQuery= getPCCQueryByKey(pccDetailsTableDTO.getReponceQueryKey());
				Status status = new Status();
				MastersValue mastersValue = new MastersValue();
				mastersValue.setKey(SHAConstants.HRM_COORDINATOR_SOURCE);
				if(pccDTO.getIsNegotiation()){				
					status.setKey(SHAConstants.PCC_HRMC_NEGOTIATION_APPROVED_STATUS);
					pccRequest.setZonalRemark(pccDTO.getRemarksNegotioanforZMH());
				}else{
					status.setKey(SHAConstants.PCC_HRMC_NEGOTIATION_DISAPPROVED_STATUS);
				}
				pccQuery.setRoleAssigned(SHAConstants.HRM_COORDINATOR_ROLE);
				pccQuery.setUserAssigned(userName);
				pccQuery.setQueryReplyRemarks(pccDTO.getRemarksNegotioanforZMH());
				pccQuery.setStatus(status);
				pccQuery.setModifiedBy(userName);
				pccQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				pccQuery.setPccSource(mastersValue);
				pccQuery.setRepliedBy(userName);
				pccQuery.setRepliedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(pccQuery);
				entityManager.flush();
				entityManager.clear();

				pccRequest.setStatus(status);
				pccRequest.setNegotiatedAmount(pccDTO.getNegotiatioAmount());
				pccRequest.setSavedAmount(pccDTO.getSavedAmount());
				pccRequest.setModifiedBy(userName);
				pccRequest.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				pccRequest.setPccSource(mastersValue);
				pccRequest.setLockedBy(null);
				pccRequest.setLockFlag("N");
				pccRequest.setLockedDate(null);
				entityManager.merge(pccRequest);
				entityManager.flush();
				entityManager.clear();
			}
		}
	}
	
	public List<Long> getCPUCodeList(String userName,String roleCode) {
		Query query = entityManager.createNamedQuery("MasUserPCCRoleMappingDetails.findCPUCodeByUserRoleKey");
		query.setParameter("userID",userName);
		query.setParameter("roleCode",roleCode);

		List<Long> actualCodeList = query.getResultList();
		return actualCodeList;
	}
	
	private List<SearchProcessPCCRequestTableDTO> getHospitalDetails(
			List<SearchProcessPCCRequestTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){			
			 

			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalId());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalCode(hospitalDetail.getHospitalCode());

				
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
}
