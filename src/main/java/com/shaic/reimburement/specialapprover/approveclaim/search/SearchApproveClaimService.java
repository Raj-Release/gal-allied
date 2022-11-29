package com.shaic.reimburement.specialapprover.approveclaim.search;

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

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Policy;
import com.shaic.domain.TmpCPUCode;

/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchApproveClaimService extends AbstractDAO<Intimation>{
	
	
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchApproveClaimService() {
		super();
	}

	public  Page<SearchApproveClaimTableDTO> search(
			SearchApproveClaimFormDTO searchFormDTO,
			String userName, String passWord) {
		List<Intimation> listIntimations = new ArrayList<Intimation>(); 
		try{
		String intimationNo = searchFormDTO.getIntimationNo() != null || !searchFormDTO.getIntimationNo().isEmpty()? searchFormDTO.getIntimationNo() :null;
		String policyNo = searchFormDTO.getPolicyNo() !=null || !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO.getPolicyNo() : null;
		Long cpuCode = searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getId() : null;
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
		
		Root<Intimation> root = criteriaQuery.from(Intimation.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		Predicate condition1 = criteriaBuilder.like(root.<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(policyNo != null){
		Predicate condition2 = criteriaBuilder.like(root.<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
		conditionList.add(condition2);
		}
		if(cpuCode != null){
			Predicate condition3 = criteriaBuilder.equal(root.<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuCode);
			conditionList.add(condition3);
			}
		
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			
		final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 1;
		}
		
		listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		for(Intimation inti:listIntimations){
			System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
		}
		List<Intimation> doList = listIntimations;
		List<SearchApproveClaimTableDTO> tableDTO = SearchApproveClaimMapper.getInstance().getIntimationDTO(doList);
		//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
		tableDTO = getclaimNumber(tableDTO);
		tableDTO = getHospitalDetails(tableDTO);
		List<SearchApproveClaimTableDTO> result = new ArrayList<SearchApproveClaimTableDTO>();
		result.addAll(tableDTO);
		Page<SearchApproveClaimTableDTO> page = new Page<SearchApproveClaimTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber+1);
		page.setHasNext(true);
		if(result.isEmpty()){
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageItems(result);
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;	
	}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	private List<SearchApproveClaimTableDTO> getclaimNumber(List<SearchApproveClaimTableDTO> intimationList){
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
							intimationList.get(index).setClaimType(a_claim.getClaimType().getValue());
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


	private List<SearchApproveClaimTableDTO> getHospitalDetails(
			List<SearchApproveClaimTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
	
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
				 tableDTO.get(index).setCpuCode(cpuCode);
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
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
	
	/*private CityTownVillage getCityTownVillage(Long cpuId){
		
		try{
			Query findCpuCode = entityManager.createNamedQuery("CityTownVillage.findByKey").setParameter("cpuId", cpuId);
			CityTownVillage cityTownVillage = (CityTownVillage) findCpuCode.getSingleResult();
			return cityTownVillage;
			}catch(Exception e){
				
			}
		return null;
		
	}*/
	
	
	
	
}
