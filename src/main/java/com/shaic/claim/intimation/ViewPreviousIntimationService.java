package com.shaic.claim.intimation;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import com.shaic.domain.GalaxyCRMIntimation;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.Intimation;
import com.shaic.domain.StageIntimation;
import com.shaic.domain.preauth.PremiaPreviousClaim;


@Stateless
public class ViewPreviousIntimationService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public ViewPreviousIntimationService(){
		super();		
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewPreviousIntimationDto> getPreviousIntimationList(String policyNumber, String riskId, EntityManager entityManager){
		this.entityManager = entityManager;
		Query query = null;
		if(StringUtils.isBlank(riskId)){
			query = this.entityManager.createNamedQuery("Intimation.findByPolicy");
			query.setParameter("policyNo", policyNumber);
		}else{
			query = this.entityManager.createNamedQuery("Intimation.findByPolAndInsuredId");
			query.setParameter("policyNo", policyNumber);
			query.setParameter("insuredId", Long.valueOf(riskId));
		}
		List<Intimation> singleResult = (List<Intimation>) query.getResultList();
		ViewPreviousIntimationMapper viewPreviousIntimationMapper = new ViewPreviousIntimationMapper();
		List<ViewPreviousIntimationDto> viewPreviousIntimationDtoListTemp = viewPreviousIntimationMapper.getIntimationDTO(singleResult);
		LinkedList<ViewPreviousIntimationDto> viewPreviousIntimationDtoList = new LinkedList<ViewPreviousIntimationDto>(viewPreviousIntimationDtoListTemp);
		return viewPreviousIntimationDtoList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ViewPreviousIntimationDto> getPreviousPremiaIntimationList(String policyNumber, String riskId, EntityManager entityManager){
		this.entityManager = entityManager;
		Query query = null;
		if(StringUtils.isBlank(riskId)){
			query = this.entityManager.createNamedQuery("PremiaPreviousClaim.findByPolicyNo");
			query.setParameter("policyNo", policyNumber);
		}else{
			query = this.entityManager.createNamedQuery("PremiaPreviousClaim.findByPolAndRiskId");
			query.setParameter("policyNo", policyNumber);
			query.setParameter("riskId", Long.valueOf(riskId));
		}
		List<PremiaPreviousClaim> singleResult = (List<PremiaPreviousClaim>) query.getResultList();
		ViewPreviousIntimationMapper viewPreviousIntimationMapper = new ViewPreviousIntimationMapper();
		List<ViewPreviousIntimationDto> viewPreviousIntimationDtoList = viewPreviousIntimationMapper.getPremiaPreviousClaimDto(singleResult);
		return viewPreviousIntimationDtoList;
	}
	
	public List<GalaxyIntimation> getIntimationByPolicy(String policyNumber){
		
		Query query = this.entityManager.createNamedQuery("GalaxyIntimation.findByPolicy");
		query.setParameter("policyNo", policyNumber);
		
		@SuppressWarnings("unchecked")
		List<GalaxyIntimation> resultList = (List<GalaxyIntimation>) query.getResultList();
		
		return resultList;
	}
	
	 @SuppressWarnings("unchecked")
		public List<ViewPreviousIntimationDto> getDummyIntimationList(String policyNumber, String riskId, EntityManager entityManager){
			this.entityManager = entityManager;
			Query query = null;
			if(StringUtils.isBlank(riskId)){
				query = this.entityManager.createNamedQuery("GalaxyCRMIntimation.findByPolicyNumber");
				query.setParameter("policyNumber", policyNumber);
			}else{
				query = this.entityManager.createNamedQuery("GalaxyCRMIntimation.findByPolicyNumberAndRiskId");
				query.setParameter("policyNumber", policyNumber);
				query.setParameter("riskId", riskId);
			}
			List<GalaxyCRMIntimation> singleResult = (List<GalaxyCRMIntimation>) query.getResultList();
			ViewPreviousIntimationMapper viewPreviousIntimationMapper = new ViewPreviousIntimationMapper();
			List<ViewPreviousIntimationDto> viewPreviousIntimationDtoListTemp = viewPreviousIntimationMapper.getCRMIntimationDTO(singleResult);
			LinkedList<ViewPreviousIntimationDto> viewPreviousIntimationDtoList = new LinkedList<ViewPreviousIntimationDto>(viewPreviousIntimationDtoListTemp);
			return viewPreviousIntimationDtoList;
		}
	
}