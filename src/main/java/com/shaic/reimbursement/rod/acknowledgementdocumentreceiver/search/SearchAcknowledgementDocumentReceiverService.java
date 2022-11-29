/**
 * 
 */
package com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPaymentCancel;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchAcknowledgementDocumentReceiverService extends AbstractDAO<Intimation>{
	
	
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchAcknowledgementDocumentReceiverService() {
		super();
	}

	public  Page<SearchAcknowledgementDocumentReceiverTableDTO> search(
			SearchAcknowledgementDocumentReceiverFormDTO searchFormDTO,
			String userName, String passWord) {
		List<Claim> listIntimations = new ArrayList<Claim>(); 
		try{
		String intimationNo =  null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
		String policyNo =  null != searchFormDTO.getPolicyNo() && !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO.getPolicyNo() : null;
		String healthCardNo =  null != searchFormDTO.getHealthCardIdNo() && !searchFormDTO.getHealthCardIdNo().isEmpty() ? searchFormDTO.getHealthCardIdNo() : null;
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
		Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
		conditionList.add(condition1);
		}
		if(policyNo != null){
		Predicate condition2 = criteriaBuilder.like(root.<Intimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+policyNo+"%");
		conditionList.add(condition2);
		}
		if(healthCardNo != null){
		Predicate condition3 = criteriaBuilder.like(root.<Intimation>get("intimation").<Insured>get("insured").<String>get("healthCardNumber"), "%"+healthCardNo+"%");
		conditionList.add(condition3);
		}
		
		
		
				List<Long> claimTypeKey = new ArrayList<Long>();
				claimTypeKey.add(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
				claimTypeKey.add(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
				Expression<Long> exp = root.<MastersValue> get("claimType").<Long> get("key");
		Predicate condition4 = exp.in(claimTypeKey);
		conditionList.add(condition4);
		
		Predicate condition5 = criteriaBuilder.notLike(root.<Intimation>get("intimation").<String>get("processClaimType"), "%"+SHAConstants.PA_TYPE+"%");
		conditionList.add(condition5);
				
		if(intimationNo == null && policyNo == null && healthCardNo == null){
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
		final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 0;
		}

		if(intimationNo == null && policyNo == null && healthCardNo == null /*&& listIntimations.size()>10*/){
		listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
		}else{
			listIntimations = typedQuery.getResultList();
		}
//		for(Intimation inti:listIntimations){
//			System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
//		}
		List<Claim> doList = listIntimations; 	
		List<SearchAcknowledgementDocumentReceiverTableDTO> tableDTO = SearchAcknowledgementDocumentReceiverMapper.getInstance().getIntimationDTO(doList);
		
		for (SearchAcknowledgementDocumentReceiverTableDTO searchAcknowledgementDocumentReceiverTableDTO : tableDTO) {
			
			if(searchAcknowledgementDocumentReceiverTableDTO.getCpuCode() != null){
				Long cpuCode = searchAcknowledgementDocumentReceiverTableDTO.getCpuCode();
				searchAcknowledgementDocumentReceiverTableDTO.setStrCpuCode(String.valueOf(cpuCode));
			}
		}
		//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
//		tableDTO = getclaimNumber(tableDTO);
		tableDTO = getHospitalDetails(tableDTO);
		List<SearchAcknowledgementDocumentReceiverTableDTO> result = new ArrayList<SearchAcknowledgementDocumentReceiverTableDTO>();
		result.addAll(tableDTO);
		Page<SearchAcknowledgementDocumentReceiverTableDTO> page = new Page<SearchAcknowledgementDocumentReceiverTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber+1);
		page.setHasNext(true);
		if(result.isEmpty()){
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
	}
		return null;	
	}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	/*private List<SearchAcknowledgementDocumentReceiverTableDTO> getclaimNumber(List<SearchAcknowledgementDocumentReceiverTableDTO> intimationList){
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
						}else{
							intimationList.get(index).setClaimNo("");
						}	
				}catch(Exception e){
					continue;
				}
			} 
		}
		return intimationList;
	}*/


	private List<SearchAcknowledgementDocumentReceiverTableDTO> getHospitalDetails(
			List<SearchAcknowledgementDocumentReceiverTableDTO> tableDTO) {
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
//				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 tableDTO.get(index).setHospitalType(hospitalDetail.getHospitalTypeName());
//				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
//				 tableDTO.get(index).setCpuCode(cpuCode);
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
	
	
	public ClaimPaymentCancel getpaymentCancelDetail(){
		Query findByPaymentCancelKey = entityManager.createNamedQuery(
				"ClaimPaymentCancel.findAll");
		ClaimPaymentCancel paymentCancelDetail;
		try{
			
			paymentCancelDetail = (ClaimPaymentCancel) findByPaymentCancelKey.getSingleResult();
		return paymentCancelDetail;
		}
		catch(Exception e){
			return null;
		}
	}
	
	public List<ClaimPaymentCancel> getpaymentCancelDetailList(){
		Query query = entityManager.createNamedQuery(
				"ClaimPaymentCancel.findByCancelType");
		query = query.setParameter("cancelType", SHAConstants.CLAIM_PAYMENT_RECONSIDERATION_TYPE);
		List<ClaimPaymentCancel> paymentCancelDetail;
		try{
			
			paymentCancelDetail = query.getResultList();
		return paymentCancelDetail;
		}
		catch(Exception e){
			return null;
		}
	}
	
	
	public Hospitals getHospitalDetail(Long hospitalId){
		Query findByPaymentCancelKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key", hospitalId);
		Hospitals hospitalDetail;
		try{
			
			hospitalDetail = (Hospitals) findByPaymentCancelKey.getSingleResult();
		return hospitalDetail;
		}
		catch(Exception e){
			return null;
		}
	}
	
	public Intimation getIntimationObject(String strIntimationNo) {
		TypedQuery<Intimation> query = getEntityManager().createNamedQuery("Intimation.findByIntimationNumber", Intimation.class);
		query.setParameter("intimationNo", strIntimationNo);
		List<Intimation>	resultList = query.getResultList();
		if (null != resultList && 0 != resultList.size())
		{
			return resultList.get(0);
			
		}
		else
		{
			return null;
		}
	}
	
	
	public Reimbursement getRodDetail(String intimationNo){
		Query findByPaymentCancelKey = entityManager.createNamedQuery(
				"Reimbursement.findByIntimationNumber").setParameter("intimationNumber", intimationNo);
		Reimbursement rodDetail;
		try{
			
			rodDetail = (Reimbursement) findByPaymentCancelKey.getSingleResult();
		return rodDetail;
		}
		catch(Exception e){
			return null;
		}
	}
	
	
	public Claim getClaimByKey(Long key) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", key);
		List<Claim> claim = (List<Claim>)query.getResultList();
		
		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		else{
			return null;
		}
	}
	
}
