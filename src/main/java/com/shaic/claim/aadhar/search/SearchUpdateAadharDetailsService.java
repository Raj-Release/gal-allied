package com.shaic.claim.aadhar.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;

@Stateless
public class SearchUpdateAadharDetailsService extends AbstractDAO<Intimation>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;
	
	public SearchUpdateAadharDetailsService(){
		super();
	}
	
	public Page<SearchUpdateAadharTableDTO> search(SearchUpdateAadharDTO searchDto,String userName,String passWord){
		
		Boolean allowed = false;
		if(searchDto != null && searchDto.getIntimationNo() != null){
			allowed = isGMCIntimation(searchDto.getIntimationNo());
		}
		
		if(allowed){
			try{
				String intimationNo =  null != searchDto  && null != searchDto.getIntimationNo() ? searchDto.getIntimationNo() : null;
				
				List<Claim> listIntimations = new ArrayList<Claim>(); 
				final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
				Root<Claim> root = criteriaQuery.from(Claim.class);
				
				List<Predicate> conditionList = new ArrayList<Predicate>();
				
				if(intimationNo != null){
					Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
					conditionList.add(condition1);
					}
		
	
					criteriaQuery.select(root).where(
							criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
				listIntimations = typedQuery.getResultList();
				
				
				Pageable pageable = searchDto.getPageable();
				if(pageable == null){
					pageable = new Pageable();
				}
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 20);
	
				
				List<SearchUpdateAadharTableDTO> tableDTO = SearchUpdateAadharMapper.getInstance().getIntimationDTO(listIntimations);
	
			
				Page<SearchUpdateAadharTableDTO> page = new Page<SearchUpdateAadharTableDTO>();
				page.setPageItems(tableDTO);
				return page;
				}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			}
		
		return null;
		}else {
			return null;
		}
		
	}
	
	public boolean isGMCIntimation(String intimationNo)
	{
		
		boolean result = false;
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationId").setParameter(
						"intimationNo", "%"+intimationNo.toLowerCase()+"%");

		List<Intimation> intimationList = (List<Intimation>) findByKey.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			
			if(intimationList.get(0).getPolicy() != null && intimationList.get(0).getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimationList.get(0).getPolicy().getProduct().getKey()))
			 {
				result = true;
			 }
		}
		return result;
	}

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
