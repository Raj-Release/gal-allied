package com.shaic.claim.pedrequest.approve.bancspedQuery;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
import com.shaic.domain.Intimation;
import com.shaic.domain.PedQueryTable;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;


@Stateless
public class BancsSearchPEDRequestApproveService  extends  AbstractDAO<OldInitiatePedEndorsement> {
	
	public BancsSearchPEDRequestApproveService(){
		
	}
	
	public Page<BancsSearchPEDRequestApproveTableDTO> search(BancsSearchPEDRequestApproveFormDTO searchFormDTO, String userName, String passWord)
 {
		List<PedQueryTable> pedList = new ArrayList<PedQueryTable>();
		List<OldInitiatePedEndorsement> pedQueryList = null;
		try {
			String intimationNo = null != searchFormDTO&& searchFormDTO.getIntimationNo() != null ? searchFormDTO.getIntimationNo() : null;
			String policyNo = null != searchFormDTO&& searchFormDTO.getPolicyNo() != null ? searchFormDTO.getPolicyNo() : null;
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<PedQueryTable> criteriaQuery = criteriaBuilder.createQuery(PedQueryTable.class);

			Root<PedQueryTable> root = criteriaQuery.from(PedQueryTable.class);

			List<Predicate> conditionList = new ArrayList<Predicate>();
			List<Predicate> conditionList1 = new ArrayList<Predicate>();

			if (policyNo != null && !policyNo.isEmpty() ) {
				Predicate condition1 = criteriaBuilder.like(root.<String> get("policyNumber"), "%" + policyNo + "%");
				conditionList.add(condition1);
			}
			if (intimationNo != null && !intimationNo.isEmpty()) {
				Intimation intimationByNo = getIntimationByNo(intimationNo);
				if(intimationByNo !=null ){
					if(intimationByNo.getPolicy() != null){
						Predicate condition2 = criteriaBuilder.like(root.<String> get("policyNumber"), "%" + intimationByNo.getPolicy().getPolicyNumber() + "%");
						conditionList.add(condition2);
					}
				}
			}
			
			Status status = new Status();
			status.setKey(ReferenceTable.PED_QUERY_STATUS_KEY);
			
			Predicate condition3 = criteriaBuilder.equal(root.<String> get("status"), status);
			conditionList.add(condition3);
			conditionList1.add(condition3);

			if (((intimationNo == null || intimationNo.isEmpty()) && (policyNo == null || policyNo.isEmpty()))) {
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList1.toArray(new Predicate[] {})));
			} else {
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}

			final TypedQuery<PedQueryTable> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			if (pageNumber > 1) {
				firtResult = (pageNumber - 1) * 10;
			} else {
				firtResult = 1;
			}
			pedList = typedQuery.getResultList();
			List<OldInitiatePedEndorsement> queryList = new ArrayList<OldInitiatePedEndorsement>();
			if(pedList.size() > 0){
				for(PedQueryTable pedQuery : pedList){
					pedQueryList = getByWorkItemID(pedQuery.getWorkItemId());
					if(pedQueryList !=null){
						queryList.addAll(pedQueryList);
					}
				}
			}

			/*if (pedList.size() > 10) {
				pedList = typedQuery.setFirstResult(firtResult)
						.setMaxResults(10).getResultList();
			}*/
			Integer totalRecords = 0;
			totalRecords = queryList.size();

			List<PedQueryTable> doList = pedList;
			List<OldInitiatePedEndorsement> pageItemList = queryList;
			List<BancsSearchPEDRequestApproveTableDTO> searchPEDApproveProcessTableDTO = new ArrayList<BancsSearchPEDRequestApproveTableDTO>();

			searchPEDApproveProcessTableDTO = BancsSearchPEDRequestApproveMapper.getInstance()
						.getSearchPEDRequestApproveTableDTO(pageItemList);
			
			for (BancsSearchPEDRequestApproveTableDTO searchDTO : searchPEDApproveProcessTableDTO) {
				if(searchDTO.getPedInitiated() != null){
					searchDTO.setStrPedInitiated(SHAUtils.formatDate(searchDTO.getPedInitiated()));
				}
		 		Claim claimObject = getClaimByIntimation(searchDTO.getIntimationNo());
		 		searchDTO.setCrmFlagged(claimObject.getCrcFlag());
				/*if(searchDTO.getCrmFlagged() != null){
					if(searchDTO.getCrmFlagged().equalsIgnoreCase("Y")){
						searchDTO.setColorCodeCell("OLIVE");
						searchDTO.setCrmFlagged(null);
//						searchDTO.setCrmFlagged(FontAwesome.ADJUST.getHtml());
					}
							
				}*/
		 		
		 		if (claimObject != null) {
					
					if(searchDTO.getCrmFlagged() != null){
						if(searchDTO.getCrmFlagged().equalsIgnoreCase("Y")){
							if (claimObject.getCrcFlag() != null && claimObject.getCrcFlag().equalsIgnoreCase("Y")) {
								searchDTO.setColorCodeCell("OLIVE");
							}
							searchDTO.setCrmFlagged(null);
						}
					}
					if (claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchDTO.setColorCodeCell("VIP");
					}
					if (claimObject.getCrcFlag() != null && claimObject.getCrcFlag().equalsIgnoreCase("Y") 
							&& claimObject.getIsVipCustomer() != null && claimObject.getIsVipCustomer().toString().equalsIgnoreCase("1")) {
						searchDTO.setColorCodeCell("CRMVIP");
					}
					
				}
				
				/*if(searchDTO.getKey() != null){
					Object workflowKey = workFlowMap.get(searchDTO.getKey());
					searchDTO.setDbOutArray(workflowKey);
				}*/
			}
				
				/**
				 * Implementation for hospital information
				 * */
			int iPageItemListSize = pageItemList.size();
			ListIterator<OldInitiatePedEndorsement> iterPED = pageItemList.listIterator();
			List<Long>  hospTypeList = new ArrayList<Long>();		
			while (iterPED.hasNext())
			{
				 OldInitiatePedEndorsement oldIntiatePedEndorsement = iterPED.next();
				 /*MastersValue hospTypeInfo = oldIntiatePedEndorsement.getIntimation().getHospitalType();
				 Long hospitalTypeId = hospTypeInfo.getKey();*/
				 Long hospitalTypeId = oldIntiatePedEndorsement.getIntimation().getHospital();
				 hospTypeList.add(hospitalTypeId);
			}
			
			List<Hospitals> resultListForHospitalInfo = new ArrayList<Hospitals>();
			List<BancsSearchPEDRequestApproveTableDTO> searchPEDApproveProcessTableDTOForHospitalInfoList = new ArrayList<BancsSearchPEDRequestApproveTableDTO>();				
			resultListForHospitalInfo = SHAUtils.getHospitalInformation(entityManager, hospTypeList);
			searchPEDApproveProcessTableDTOForHospitalInfoList = BancsSearchPEDRequestApproveMapper.getHospitalInfoList(resultListForHospitalInfo);
			
			for (BancsSearchPEDRequestApproveTableDTO objSearchPEDApproveProcessTableDTO : searchPEDApproveProcessTableDTO )
			{
				
				for (BancsSearchPEDRequestApproveTableDTO objSearchPEDApproveProcessTableDTOForHospitalInfo : searchPEDApproveProcessTableDTOForHospitalInfoList)
				{
					/**
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo.getKey() --> will store the hosptial type id.
					 * objSearchPEDRequestProcessTableDTOForHospitalInfo list belongs to VW_HOSPITAL view. This list is of
					 * Hospital type. In Hospital.java , we store the key. 
					 * 
					 * But this key will come from intimation table hospital type id. objSearchPEDRequestProcessTableDTO is of 
					 * SearchPEDRequestProcessTableDTO , in which we store hospital type in a variable known as hospitalTypeId .
					 * That is why we equate hospitalTypeId from SearchPEDRequestProcessTableDTO with key from HospitalDTO.
					 * */
					if(objSearchPEDApproveProcessTableDTO.getHospitalTypeId() != null && 
							objSearchPEDApproveProcessTableDTOForHospitalInfo.getKey() != null && 
							objSearchPEDApproveProcessTableDTO.getHospitalTypeId().equals(objSearchPEDApproveProcessTableDTOForHospitalInfo.getKey()))
					{
						objSearchPEDApproveProcessTableDTO.setHospitalName(objSearchPEDApproveProcessTableDTOForHospitalInfo.getHospitalName());
						objSearchPEDApproveProcessTableDTO.setHospitalCity(objSearchPEDApproveProcessTableDTOForHospitalInfo.getHospitalCity());
						objSearchPEDApproveProcessTableDTO.setHospitalAddress((objSearchPEDApproveProcessTableDTOForHospitalInfo.getHospitalAddress()));
						break;
					}
				}
				Policy policyByPolicyNumber = getPolicyByPolicyNubember(objSearchPEDApproveProcessTableDTO.getPolicyNo());
				if (policyByPolicyNumber != null){
					Long diffDays = SHAUtils.getDiffDaysWithNegative(new Timestamp(System.currentTimeMillis()), policyByPolicyNumber.getPolicyToDate());
					if (diffDays < 0){
						diffDays= 0l;
					}
					objSearchPEDApproveProcessTableDTO.setRenewalDue(diffDays);
				}
			}
			
	
			Page<OldInitiatePedEndorsement> pagedList = super.pagedList(searchFormDTO.getPageable());
			Page<BancsSearchPEDRequestApproveTableDTO> page = new Page<BancsSearchPEDRequestApproveTableDTO>();
			page.setPageItems(searchPEDApproveProcessTableDTO);
			page.setTotalRecords(totalRecords);
	
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh" + e.getMessage()
					+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
		return null;
	}
	public Claim getClaimByIntimation(String intimationno) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationno != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationId");
			findByIntimationKey = findByIntimationKey.setParameter("intimationNumber", intimationno);
			try {

				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				
				for (Claim claim : a_claimList) {
					entityManager.refresh(claim);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} 
		} 
		else {
			// intimationKey null
		}
		return a_claimList.get(0);

	}
	
	
	@SuppressWarnings("unchecked")
	public OldInitiatePedEndorsement getOldInitiatePedEndorsementByKey(Long oldInitiatePedEndorsementKey) {
		
		Query findByKey = entityManager.createNamedQuery("HospitalAcknowledge.findAll");

		List<OldInitiatePedEndorsement> oldInitiatePedEndorsementList = (List<OldInitiatePedEndorsement>) findByKey
				.getResultList();

		if (!oldInitiatePedEndorsementList.isEmpty()) {
			return oldInitiatePedEndorsementList.get(0);

		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery("Intimation.findByIntimationNumber").setParameter("intimationNo", intimationNo);
		List<Intimation> intimationList = (List<Intimation>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Intimation getWorkItemIDByKey(Long intimationKey) {

		Query findByKey = entityManager
				.createNamedQuery("Intimation.findByKey").setParameter(
						"intiationKey", intimationKey);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<OldInitiatePedEndorsement> getByWorkItemID(Long workItemID) {

		Query findByKey = entityManager
				.createNamedQuery("OldInitiatePedEndorsement.findByWorkItemID").setParameter(
						"workItemID", workItemID.toString());

		List<OldInitiatePedEndorsement> pedList = (List<OldInitiatePedEndorsement>) findByKey
				.getResultList();

		if (!pedList.isEmpty()) {
			return pedList;
		}
		return null;
	}
	
	public Policy getPolicyByPolicyNubember(String policyNumber){
		Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		if(query.getResultList().size()!=0){
		Policy singleResult =  (Policy) query.getSingleResult();
		
			return singleResult;
		}
		return null;
	}

	@Override
	public Class<OldInitiatePedEndorsement> getDTOClass() {
		return OldInitiatePedEndorsement.class;
	}
}
