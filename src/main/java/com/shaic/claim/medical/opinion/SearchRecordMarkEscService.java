package com.shaic.claim.medical.opinion;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveMapper;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationMapper;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationTableDTO;

@Stateless
public class SearchRecordMarkEscService extends  AbstractDAO<Claim> {
	
	public SearchRecordMarkEscService(){
		super();
	}
	
	public Page<SearchRecordMarkEscTableDTO> search(SearchRecordMarkEscFormDTO searchFormDTO, String userName, String passWord)
	{
		//List<Claim> listIntimations = new ArrayList<Claim>(); 
			String intimationNo = null != searchFormDTO && searchFormDTO.getIntimationNo() != null ? searchFormDTO.getIntimationNo() :null;
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
			List<SearchRecordMarkEscTableDTO> tableDTO =SearchRecordMarkEscMapper.getInstance().getSearchRecordMarkEscTableDTO(doList);
							
			for (SearchRecordMarkEscTableDTO searchRecordMarkEscTableDTO : tableDTO) {
				
				if(searchRecordMarkEscTableDTO.getLobId() != null &&
						(searchRecordMarkEscTableDTO.getLobId().equals(ReferenceTable.HEALTH_LOB_KEY) ||searchRecordMarkEscTableDTO.getLobId().equals(ReferenceTable.PACKAGE_MASTER_VALUE))){
					searchRecordMarkEscTableDTO.setLob(SHAConstants.HEALTH_LOB);
				}
				else {
					searchRecordMarkEscTableDTO.setLob(SHAConstants.PA_LOB);
				}
				
				if(searchRecordMarkEscTableDTO.getHospitalKey() !=null){
					Hospitals hospitals = getHospitalById(searchRecordMarkEscTableDTO.getHospitalKey());
					if(hospitals !=null){
						searchRecordMarkEscTableDTO.setHospitalName(hospitals.getName());
						searchRecordMarkEscTableDTO.setHospitalCity(hospitals.getCity());
					}
				}
				
				Claim claimByKey = getClaimsByIntimationNumber(intimationNo);
				if(claimByKey != null) {
					searchRecordMarkEscTableDTO.setCrmFlagged(claimByKey.getCrcFlag());
					
					searchRecordMarkEscTableDTO.setCoverCode(claimByKey.getClaimCoverCode());
					searchRecordMarkEscTableDTO.setSubCoverCode(claimByKey.getClaimSubCoverCode());
					searchRecordMarkEscTableDTO.setSectionCode(claimByKey.getClaimSectionCode());
					
					if(searchRecordMarkEscTableDTO.getCrmFlagged() != null){
						if(searchRecordMarkEscTableDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y")) {
								searchRecordMarkEscTableDTO.setColorCodeCell("OLIVE");
							}
							searchRecordMarkEscTableDTO.setCrmFlagged(null);
						}
					}
					if (claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchRecordMarkEscTableDTO.setColorCodeCell("VIP");
					}
					if (claimByKey.getCrcFlag() != null && claimByKey.getCrcFlag().equalsIgnoreCase("Y") 
							&& claimByKey.getIsVipCustomer() != null && claimByKey.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchRecordMarkEscTableDTO.setColorCodeCell("CRMVIP");
					}
					
				}

		}
		
		List<SearchRecordMarkEscTableDTO> result = new ArrayList<SearchRecordMarkEscTableDTO>();
		result.addAll(tableDTO);
		Page<SearchRecordMarkEscTableDTO> page = new Page<SearchRecordMarkEscTableDTO>();
		//searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
		if(result.size()<=10) {
			page.setHasNext(false);
		}
		else
		{
		page.setHasNext(true);
		}
		if (result.isEmpty()) {
			searchFormDTO.getPageable().setPageNumber(1);
		}
		//page.setPageNumber(pageNumber);
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
	
	
	
	
	public String getBranchName(String officeCode) {
		String officeName = "";
		Query query = entityManager.createNamedQuery("OrganaizationUnit.findByBranchode");
    	query = query.setParameter("parentKey", officeCode);
    	OrganaizationUnit orgList = (OrganaizationUnit) query.getSingleResult();
    	if(null != orgList ) {
    		officeName = orgList.getOrganizationUnitName();
    		return officeName;
    	}
    	return officeName;
		
	}

	private List <Insured> getInsuredByPolicy(Long  policyKey) {
		
			Query query = entityManager.createNamedQuery("Insured.findByPolicykey1");
			 query = query.setParameter("policykey", policyKey);	
			 List<Insured> insuredList  = (List<Insured>) query.getResultList();
			 if (insuredList != null && ! insuredList.isEmpty()) {
				 return insuredList;		
			 }
			 return null;
	}

	private Policy  getPolicyByKey(Long policyKey) {
		
		Query query = entityManager.createNamedQuery("Policy.findByKey");
    	query = query.setParameter("policyKey", policyKey);
    	Policy policyList = (Policy) query.getSingleResult();
    	if (policyList != null){
    		return policyList;
    	}
		return null;
	}

	public List<Intimation> getIntimationByPolicyNumber(String policyNumber){
		return null;
	}
	

	@Override
	public Class<Claim> getDTOClass() {
		return Claim.class;
	}

	public Hospitals getHospitalById(Long key){

		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);
		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}
	
	public Claim getClaimsByIntimationNumber(String intimationNumber) {
		List<Claim> resultClaim = null;
		if (intimationNumber != null) {

			Query findByIntimationNum = entityManager.createNamedQuery(
					"Claim.findByIntimationNumber").setParameter(
					"intimationNumber", intimationNumber);

			try {
				resultClaim = (List<Claim>) findByIntimationNum.getResultList();
				
				if(resultClaim != null && !resultClaim.isEmpty()){
					return resultClaim.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

}
