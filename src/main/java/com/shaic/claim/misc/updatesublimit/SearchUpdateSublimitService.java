package com.shaic.claim.misc.updatesublimit;

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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;


@Stateless
public class SearchUpdateSublimitService extends AbstractDAO<Reimbursement> {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public SearchUpdateSublimitService(){
		super();
	}
	
	public Page<SearchUpdateSublimitTableDTO> doSearch(SearchUpdateSublimitFormDTO formDto,String userName,String password) {
		
		List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
		
		try{

			String intimationNo = null != formDto && !formDto.getIntimationNo().isEmpty() ? formDto
					.getIntimationNo() : null;
			String policyNo = null != formDto && !formDto.getPolicyNo().isEmpty() ? formDto
					.getPolicyNo() : null;

			final CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<Reimbursement> criteriaQuery = criteriaBuilder
					.createQuery(Reimbursement.class);

			Root<Reimbursement> root = criteriaQuery.from(Reimbursement.class);

			List<Predicate> conditionList = new ArrayList<Predicate>();
			if (intimationNo != null) {
				Predicate condition1 = criteriaBuilder.like(
						root.<Claim> get("claim")
								.<Intimation> get("intimation")
								.<String> get("intimationId"), "%"
								+ intimationNo + "%");
				conditionList.add(condition1);
			}
			if (policyNo != null) {
				Predicate condition2 = criteriaBuilder.like(
						root.<Claim> get("claim")
								.<Intimation> get("intimation")
								.<Policy> get("policy")
								.<String> get("policyNumber"), "%" + policyNo
								+ "%");
				conditionList.add(condition2);
			}
			
			Predicate condition3 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),ReferenceTable.FINANCIAL_SETTLED);
			conditionList.add(condition3);
			
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList
							.toArray(new Predicate[] {})));
			
			final TypedQuery<Reimbursement> typedQuery = entityManager
					.createQuery(criteriaQuery);
			int pageNumber = formDto.getPageable().getPageNumber();
			int firtResult;
			if(pageNumber > 1){
				firtResult = (pageNumber-1) *10;
			}else{
				firtResult = 1;
			}
			
			reimbursementList = typedQuery.getResultList();
			
			List<SearchUpdateSublimitTableDTO> tableList = SearchUpdateSublimitMapper.getInstance().getIntimationDTO(reimbursementList);
			tableList = getHospitalDetails(tableList);
			
			List<SearchUpdateSublimitTableDTO> resultList = new ArrayList<SearchUpdateSublimitTableDTO>(); 
			resultList = getHospitalizationRod(tableList);
			
			Page<SearchUpdateSublimitTableDTO> page = new Page<SearchUpdateSublimitTableDTO>();
			formDto.getPageable().setPageNumber(pageNumber + 1);
			if(resultList.size()<=10)
			{
				page.setHasNext(false);
			}
			else
			{
			page.setHasNext(true);
			}
			if (resultList.isEmpty()) {
				formDto.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(resultList);
			page.setIsDbSearch(true);
	
			return page;
			
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
	}

	
	private List<SearchUpdateSublimitTableDTO> getHospitalDetails(
			List<SearchUpdateSublimitTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for (int index = 0; index < tableDTO.size(); index++) {
			 tableDTO.get(index).setLob(getLOBValue(tableDTO.get(index).getLobId()).getValue());
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key",
					tableDTO.get(index).getHospitalNameId());
			try {
				hospitalDetail = (Hospitals) findByHospitalKey
						.getSingleResult();
				if (hospitalDetail != null) {
					tableDTO.get(index).setHospitalName(
							hospitalDetail.getName());

					tableDTO.get(index).setHospitalAddress(
							hospitalDetail.getCityId() + " ,"
									+ hospitalDetail.getCity());
					tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
					
					Long cpuCode = null != getTmpCPUCode(tableDTO.get(index)
							.getCpuId()) ? getTmpCPUCode(
							tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
					tableDTO.get(index).setStrCpuCode(String.valueOf(getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode()));
					tableDTO.get(index).setCpuName(String.valueOf(getTmpCPUCode(tableDTO.get(index).getCpuId()).getDescription()));
				}
			} catch (Exception e) {
				continue;
			}

		}

		return tableDTO;
	}
	
	private List<SearchUpdateSublimitTableDTO> getHospitalizationRod(
			List<SearchUpdateSublimitTableDTO> tableDTO){
		List<SearchUpdateSublimitTableDTO> filterList = new ArrayList<SearchUpdateSublimitTableDTO>();
		for (SearchUpdateSublimitTableDTO searchUpdateSublimitTableDTO : tableDTO) {
			if(searchUpdateSublimitTableDTO.getHospitalizationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				filterList.add(searchUpdateSublimitTableDTO);
			}
		}
		
		return filterList;
	}
	
	private TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			
		}
		return null;
	}
	
	
	private MastersValue getLOBValue(Long getlOBId) {
		try{
		Query query = entityManager
				.createNamedQuery("MastersValue.findByKey");
		query = query.setParameter("parentKey", getlOBId);
		MastersValue value = (MastersValue) query.getSingleResult();
	    return value;
		}catch(Exception e){
			
		}
		return null;
	}
	
	@Override
	public Class<Reimbursement> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
