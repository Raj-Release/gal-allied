/**
 * 
 */
package com.shaic.claim.pancard.search.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import com.shaic.domain.MastersValue;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;



/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchUploadPanCardService extends AbstractDAO<Intimation>{
	
	
	
	@PersistenceContext
	protected EntityManager entityManager;
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;
	
	public SearchUploadPanCardService() {
		super();
	}
	/**
	 * 
	 * This search needs to be refractored. Commenting for time being.
	 * 
	 * **/

	public  Page<SearchUploadPanCardTableDTO> search(
			SearchUploadPanCardDTO searchFormDTO,
			String userName, String passWord) {
		try{
			String intimationNo =  null != searchFormDTO  && null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			String policyNo =  null != searchFormDTO  && null != searchFormDTO.getPolicyNo() ? searchFormDTO.getPolicyNo() : null;
			
			String priority = null != searchFormDTO  && searchFormDTO.getPriority() != null ? searchFormDTO.getPriority().getValue() != null ? searchFormDTO.getPriority().getValue() : null : null;
			String source = null != searchFormDTO  && searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() != null ? searchFormDTO.getSource().getValue(): null : null;
			String type = null != searchFormDTO  && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() != null ? searchFormDTO.getType().getValue(): null : null;
			String claimType = null != searchFormDTO && searchFormDTO.getClaimType() != null ? searchFormDTO.getClaimType().getValue() : null; 
			
			List<Claim> listIntimations = new ArrayList<Claim>(); 
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
			Root<Claim> root = criteriaQuery.from(Claim.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();
			
			if(intimationNo != null){
				Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
				conditionList.add(condition1);
				}
				if(policyNo != null){
				List<Long> intimationByPolicyLikeNo = intimationService.getIntimationByPolicyLikeNo(policyNo);
				
				//Predicate condition2 = criteriaBuilder.like(root.<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
				
				Predicate condition2 = root.<Intimation>get("intimation").<String>get("key").in(intimationByPolicyLikeNo);
				
			/*	Subquery<Long> sq1 = criteriaQuery.subquery(Long.class);
			    Root<Intimation> e4 = sq1.from(Intimation.class);
			    sq1.select(criteriaBuilder.max(e4.<Long> get("key")));
			    Predicate condition3 = criteriaBuilder.equal(root.<Long> get("key"), sq1);*/
				conditionList.add(condition2);
				//conditionList.add(condition3);
			}
				Predicate condition1 = criteriaBuilder.equal(root.<MastersValue>get("claimType").<Long>get("key"), ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				conditionList.add(condition1);
				
				//criteriaQuery.select(criteriaBuilder.max(root.<Date>get("createdDate")));
				criteriaQuery.select(root).where(
						criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				//criteriaQuery.where(criteriaBuilder.equal(root.<Long> get("key"), sq1));
				//criteriaQuery.where(criteriaBuilder.equal(root.<Long> get("key"), sq1));
			final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
			listIntimations = typedQuery.getResultList();
			
			
			Pageable pageable = searchFormDTO.getPageable();
			if(pageable == null){
				pageable = new Pageable();
			}
			pageable.setPageNumber(1);
			pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 20);
			
			//PagedTaskList taskList;
			
			List<SearchUploadPanCardTableDTO> tableDTO = SearchUploadPanCardMapper.getInstance().getIntimationDTO(listIntimations);

		
			Page<SearchUploadPanCardTableDTO> page = new Page<SearchUploadPanCardTableDTO>();
			//page.setPageNumber(taskList.getCurrentPage());
			page.setPageItems(tableDTO);
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			//return null;	
		
		
		return null;	
	}
	
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
}
