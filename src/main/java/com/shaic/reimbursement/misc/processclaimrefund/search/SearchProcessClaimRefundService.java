/**
 * 
 */
package com.shaic.reimbursement.misc.processclaimrefund.search;

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
public class SearchProcessClaimRefundService extends AbstractDAO<Intimation>{
	
	
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchProcessClaimRefundService() {
		super();
	}

	public  Page<SearchProcessClaimRefundTableDTO> search(
			SearchProcessClaimRefundFormDTO searchFormDTO,
			String userName, String passWord) {
		List<Intimation> listIntimations = new ArrayList<Intimation>(); 
		try{
		String intimationNo = null != searchFormDTO && searchFormDTO.getIntimationNo() != null ? searchFormDTO.getIntimationNo() :null;
		String policyNo = null != searchFormDTO && searchFormDTO.getPolicyNo() !=null ? searchFormDTO.getPolicyNo() : null;
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
		
		if(intimationNo == null && policyNo == null ){
			criteriaQuery.select(root);
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		int firtResult;
		if (pageNumber > 1) 
		{
			firtResult = (pageNumber - 1) * 10;
		} 
		else 
		{
			firtResult = 1;
		}
		listIntimations = typedQuery.getResultList();
		
		if( listIntimations.size()>10)
		{
			listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			}
		
		for(Intimation inti:listIntimations){
			System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
		}
		List<Intimation> doList = listIntimations;
		List<SearchProcessClaimRefundTableDTO> tableDTO =SearchProcessClaimRefundMapper.getInstance().getIntimationDTO(doList);
		//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
		tableDTO = getclaimNumber(tableDTO);
		tableDTO = getHospitalDetails(tableDTO);
		List<SearchProcessClaimRefundTableDTO> result = new ArrayList<SearchProcessClaimRefundTableDTO>();
		result.addAll(tableDTO);
		Page<SearchProcessClaimRefundTableDTO> page = new Page<SearchProcessClaimRefundTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
		if(result.size()<=10)
		{
			page.setHasNext(false);
		}
		else
		{
		page.setHasNext(true);
		}
		if (result.isEmpty()) {
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		
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
	
	private List<SearchProcessClaimRefundTableDTO> getclaimNumber(List<SearchProcessClaimRefundTableDTO> intimationList){
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
							intimationList.get(index).setClaimKey(a_claim.getKey());
							intimationList.get(index).setClaimStatus(a_claim.getStatus().getProcessValue());
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


	private List<SearchProcessClaimRefundTableDTO> getHospitalDetails(
			List<SearchProcessClaimRefundTableDTO> tableDTO) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
	
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
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
	
	
}
