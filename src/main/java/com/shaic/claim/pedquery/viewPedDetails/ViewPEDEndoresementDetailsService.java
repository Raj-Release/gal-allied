package com.shaic.claim.pedquery.viewPedDetails;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.preauth.IcdBlock;
import com.shaic.domain.preauth.IcdChapter;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.PedEndorsementDetailsHistory;
import com.shaic.domain.preauth.PreExistingDisease;

@Stateless
public class ViewPEDEndoresementDetailsService extends
		AbstractDAO<NewInitiatePedEndorsement> {

	public ViewPEDEndoresementDetailsService() {
		super();
	}

	public List<ViewPEDEndoresementDetailsDTO> search(Long key) {

		String preAuthKey = key.toString();

//		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		final CriteriaQuery<NewInitiatePedEndorsement> criteriaQuery = builder
//				.createQuery(NewInitiatePedEndorsement.class);
//
//		Root<NewInitiatePedEndorsement> searchRoot = criteriaQuery
//				.from(NewInitiatePedEndorsement.class);
//
//		List<Predicate> predicates = new ArrayList<Predicate>();

		List<NewInitiatePedEndorsement> resultList = new ArrayList<NewInitiatePedEndorsement>();

//		if (preAuthKey != null) {
//			Predicate preAuthPredicate = builder.equal(
//					searchRoot.<OldInitiatePedEndorsement> get(
//							"oldInitiatePedEndorsement").<Long> get("key"),
//					preAuthKey);
//			predicates.add(preAuthPredicate);
//		}
//		
//		Predicate predicates1 = builder.notEqual(searchRoot.<Long> get("activeStatus"), 0l);
//		predicates.add(predicates1);
//		
//		criteriaQuery.select(searchRoot).where(
//				builder.and(predicates.toArray(new Predicate[] {})));
//
//		final TypedQuery<NewInitiatePedEndorsement> oldInitiatePedQuery = entityManager
//				.createQuery(criteriaQuery);

//		resultList = oldInitiatePedQuery.getResultList();
		
		resultList = getIntitiatePedEndorsementDetails(key);

		List<ViewPEDEndoresementDetailsDTO> pedValidationListDTO = new ArrayList<ViewPEDEndoresementDetailsDTO>();


		if(resultList != null && !resultList.isEmpty()){
			for (NewInitiatePedEndorsement initiatePedEndorsement : resultList) {
				try{
					ViewPEDEndoresementDetailsDTO viewPEDEndoresementDetailsDTO = ViewPEDEndoresementDetailsMapper.getInstance()
							.getViewPEDEndoresementDetailsDTO(initiatePedEndorsement);
					if(initiatePedEndorsement.getPedCode() != null){
						viewPEDEndoresementDetailsDTO.setPedCode(this.getPreExistingDiseaseById(initiatePedEndorsement.getPedCode()).getValue());
					}
					if(initiatePedEndorsement.getIcdCodeId() != null){
						viewPEDEndoresementDetailsDTO.setIcdCode(this.getIcdCodeById(initiatePedEndorsement.getIcdCodeId()).getValue());
					}
					if(initiatePedEndorsement.getIcdChapterId() != null){
						viewPEDEndoresementDetailsDTO.setIcdChapter(this.getIcdChapterById(initiatePedEndorsement.getIcdChapterId()).getValue());
					}
					if(initiatePedEndorsement.getIcdBlockId() != null){
						viewPEDEndoresementDetailsDTO.setIcdBlock(this.getIcdBlockById(initiatePedEndorsement.getIcdBlockId()).getValue());
					}
					pedValidationListDTO.add(viewPEDEndoresementDetailsDTO);

				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		


		// List<ViewPEDEndoresementDetailsDTO> pedValidationListDTO =
		// ViewPEDEndoresementDetailsMapper
		// .getSearchPEDQueryTableDTOTableDTO(resultList);

		return pedValidationListDTO;
	}
	
	public List<NewInitiatePedEndorsement> getIntitiatePedEndorsementDetails(Long pedKey){
		
		Query findByKey = entityManager.createNamedQuery(
				"NewInitiatePedEndorsement.findByInitateKey").setParameter(
				"initiateKey", pedKey);

		List<NewInitiatePedEndorsement> newInitiateDetails = (List<NewInitiatePedEndorsement>) findByKey
				.getResultList();
		
		if(newInitiateDetails != null && ! newInitiateDetails.isEmpty()){
			return newInitiateDetails;
		}
		
		return null;
	}
	
public List<PedEndorsementDetailsHistory> getPedEndorsementDetailsHistory(Long pedKey){
		
		Query findByKey = entityManager.createNamedQuery(
				"PedEndorsementDetailsHistory.findByInitateKey").setParameter(
				"initiateKey", pedKey);

		List<PedEndorsementDetailsHistory> newInitiateDetails = (List<PedEndorsementDetailsHistory>) findByKey
				.getResultList();
		
		if(newInitiateDetails != null && ! newInitiateDetails.isEmpty()){
			return newInitiateDetails;
		}
		
		return null;
	}

	@Override
	public Class<NewInitiatePedEndorsement> getDTOClass() {
		return NewInitiatePedEndorsement.class;
	}

	public IcdCode getIcdCodeById(Long id) {
		if (id != null) {
			return entityManager.find(IcdCode.class, id);
		}
		return null;
	}

	public IcdBlock getIcdBlockById(Long id) {
		if (id != null) {
			return entityManager.find(IcdBlock.class, id);
		}
		return null;
	}

	public IcdChapter getIcdChapterById(Long id) {
		if (id != null) {
			return entityManager.find(IcdChapter.class, id);
		}
		return null;
	}
	public PreExistingDisease getPreExistingDiseaseById(Long id) {
		if (id != null) {
			return entityManager.find(PreExistingDisease.class, id);
		}
		return null;
	}

}
