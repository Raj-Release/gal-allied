package com.shaic.claim.pcc.divisionHead;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
public class DivisionHeadRequestService {


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
			String productName = null != searchFormDTO && null != searchFormDTO.getProductName() ? searchFormDTO.getProductName().getValue() : null;

			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			List<SearchProcessPCCRequestTableDTO> tableDTO = new ArrayList<SearchProcessPCCRequestTableDTO>();
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			Long totalCount = 0l;
			List<PCCQuery> doList = new ArrayList<PCCQuery>();
			List<PCCRequest> requestdoList = new ArrayList<PCCRequest>();
			final CriteriaQuery<PCCQuery> criteriaQuery = criteriaBuilder.createQuery(PCCQuery.class);
			Root<PCCQuery> queryroot = criteriaQuery.from(PCCQuery.class);
			List<Long> statusListKey= new ArrayList<Long>();
			statusListKey.add(SHAConstants.PCC_DIVISIONHEAD_QUERY_INITIATED_STATUS);	
			
			List<Long> cpuCodeBasedonRoleList= getCPUCodeList(userName,SHAConstants.DIVISION_HEAD_ROLE);
			System.out.println(String.format("CPU Code List [%s]", cpuCodeBasedonRoleList));

			List<Predicate> conditionList = new ArrayList<Predicate>();
			if(intimationNo != null){
				Predicate condition1 = criteriaBuilder.like(queryroot.<PCCRequest>get("pccRequest").<String>get("intimationNo"),"%"+intimationNo+"%");
				conditionList.add(condition1);
			}
			if(cpuCode != null){
				Predicate condition2 = criteriaBuilder.equal(queryroot.<PCCRequest>get("pccRequest").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuCode);
				conditionList.add(condition2);
			}
			if(source != null){
				Predicate condition3 = criteriaBuilder.equal(queryroot.<MastersValue>get("pccSource").<Long>get("key"),source);
				conditionList.add(condition3);
			}
			if(pccCatagory != null){
				Predicate condition4 = criteriaBuilder.equal(queryroot.<PCCRequest>get("pccRequest").<PCCCategory>get("pccCategory").<Long>get("key"), pccCatagory);
				conditionList.add(condition4);
			}
			
			if(productName != null){
				Predicate condition5 = criteriaBuilder.equal(queryroot.<PCCRequest>get("pccRequest").<Intimation>get("intimation").<Policy>get("policy").<Product>get("product").<String> get("value"), productName);
				conditionList.add(condition5);
			}

			Predicate statusCondition =  queryroot.<Status>get("status").get("key").in(statusListKey); 
			conditionList.add(statusCondition);

			Predicate roleAssigned = criteriaBuilder.equal(queryroot.<String>get("roleAssigned"),SHAConstants.DIVISION_HEAD_ROLE); 			
			conditionList.add(roleAssigned);
			
			Predicate userAssigned = criteriaBuilder.equal(queryroot.<String>get("userAssigned"),userName);	
			conditionList.add(userAssigned);

			criteriaQuery.select(queryroot).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			//criteriaQuery.orderBy(criteriaBuilder.asc(queryroot.<PCCRequest>get("pccRequest").<Intimation>get("intimation").<Date>get("createdDate")));

			final TypedQuery<PCCQuery> typedQuery = entityManager.createQuery(criteriaQuery);

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

			if(doList !=null && !doList.isEmpty()){
				List<Long> pccRequestsKeys = new ArrayList<Long>();
				for(PCCQuery pccQuery:doList){
					if(pccQuery.getPccRequest() !=null && pccQuery.getPccRequest().getKey() !=null &&
							!pccRequestsKeys.contains(pccQuery.getPccRequest().getKey())){
						pccRequestsKeys.add(pccQuery.getPccRequest().getKey());
					}
				}
				if(!pccRequestsKeys.isEmpty()){
					List<Long> firstNElementsList = pccRequestsKeys.stream().limit(1000).collect(Collectors.toList());
					List<Predicate> requestconditionList = new ArrayList<Predicate>();
					final CriteriaQuery<PCCRequest> pccRequestcriteria = criteriaBuilder.createQuery(PCCRequest.class);
					Root<PCCRequest> requestroot = pccRequestcriteria.from(PCCRequest.class);
					if(statusListKey != null){
						Predicate requeststatusCondition =  requestroot.<Status>get("status").get("key").in(statusListKey); 
						requestconditionList.add(requeststatusCondition);	
						Predicate keyCondition =  requestroot.<Long>get("key").in(firstNElementsList); 
						requestconditionList.add(keyCondition);	
						Predicate cpuCodeCondition =  requestroot.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode").in(cpuCodeBasedonRoleList); 
						requestconditionList.add(cpuCodeCondition);
						pccRequestcriteria.select(requestroot).where(criteriaBuilder.and(requestconditionList.toArray(new Predicate[] {})));
						pccRequestcriteria.orderBy(criteriaBuilder.asc(requestroot.<Intimation>get("intimation").<Date>get("createdDate")));

						//For total count - start
						List<Predicate> requestconditionList1 = new ArrayList<Predicate>();
						CriteriaBuilder qb = entityManager.getCriteriaBuilder();
						CriteriaQuery<Long> cq = qb.createQuery(Long.class);
						Root<PCCRequest> requestroot1 = cq.from(PCCRequest.class);
						Predicate statusCondition1 =  requestroot1.<Status>get("status").get("key").in(statusListKey); 
						requestconditionList1.add(statusCondition1);	
						Predicate keyCondition1 =  requestroot1.<Long>get("key").in(firstNElementsList); 
						requestconditionList1.add(keyCondition1);	
						Predicate cpuCodeCondition1 =  requestroot1.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode").in(cpuCodeBasedonRoleList); 
						requestconditionList1.add(cpuCodeCondition1);
						cq.select(qb.count(requestroot1));
						cq.where(qb.and(requestconditionList1.toArray(new Predicate[] {})));
						totalCount =  entityManager.createQuery(cq).getSingleResult();
						//For total count - end
						
						
						final TypedQuery<PCCRequest> requesttypedQuery = entityManager.createQuery(pccRequestcriteria);

						if(pageNumber > 1){
							firtResult = (pageNumber-1) *10;
						}else{
							firtResult = 0;
						}

						if(intimationNo == null){
							requestdoList = requesttypedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
						}else{
							requestdoList = requesttypedQuery.getResultList();
						}
						tableDTO = SearchProcessPCCRequestMapper.getInstance().getProcessPCCRequestTableDTOs(requestdoList);	
					}
				}
			}
			
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
			result.addAll(tableDTO);
			//result.addAll(finalList);
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
	
	public void submitPCCDivisionHead(PccDTO pccDTO,String userName,PccDetailsTableDTO pccDetailsTableDTO){

		if(pccDTO.getPccKey() !=null){
			userName = userName.toUpperCase();
			PCCRequest pccRequest= getPCCRequestByKey(pccDTO.getPccKey());
			if(pccDetailsTableDTO.getReponceQueryKey() !=null){
				PCCQuery pccQuery= getPCCQueryByKey(pccDetailsTableDTO.getReponceQueryKey());
				Status status = new Status();
				MastersValue mastersValue = new MastersValue();
				mastersValue.setKey(SHAConstants.DIVISION_HEAD_SOURCE);
				status.setKey(SHAConstants.PCC_DIVISIONHEAD_QUERY_REPLIED_STATUS);
				pccQuery.setQueryReplyRemarks(pccDTO.getRemarkForResponse());
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
