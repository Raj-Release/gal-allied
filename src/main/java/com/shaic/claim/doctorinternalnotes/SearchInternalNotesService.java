package com.shaic.claim.doctorinternalnotes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;










import org.apache.commons.lang3.StringUtils;







//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claimrequest.ClaimRequestType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.classification.ClassificationType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.fieldvisit.FieldVisitType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.hospitalinfo.HospitalInfoType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.policy.PolicyType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.preauthreq.PreAuthReqType;
//import com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.productinfo.ProductInfoType;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

@Stateless
public class SearchInternalNotesService extends  AbstractDAO<Intimation> {
	
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchInternalNotesService(){
		
	}
	
	@SuppressWarnings("unchecked")
	public Page<NewIntimationDto> search(NewIntimationDto formDTO, String userName)
	{
		try {
			
			String strIntimationNo = "";
						
			if(null != formDTO.getIntimationId() && ! formDTO.getIntimationId().equals(""))
			{			
				strIntimationNo = "%" +  StringUtils.trim(formDTO.getIntimationId()) + "%" ;
			}

			final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Claim> claimCriteriaQuery = builder
					.createQuery(Claim.class);

			Root<Claim> claimRoot = claimCriteriaQuery
					.from(Claim.class);
			
			List<Predicate> predicates = new ArrayList<Predicate>();

			if (strIntimationNo != null) {
				Predicate intimationNoPredicate = builder.like(
						claimRoot.<Intimation> get("intimation")
								.<String> get("intimationId"),
								strIntimationNo);
				predicates.add(intimationNoPredicate);
			}
			
			claimCriteriaQuery.select(claimRoot).where(
					builder.and(predicates
							.toArray(new Predicate[] {})));

			final TypedQuery<Claim> claimQuery = entityManager
					.createQuery(claimCriteriaQuery);
			List<Claim> claimList = (List<Claim>) claimQuery.getResultList();
			if(claimList != null && !claimList.isEmpty()){
				Page<NewIntimationDto> intimationPage = new Page<NewIntimationDto>();
				List<NewIntimationDto> intiamtionDtoList = new ArrayList<NewIntimationDto>();
				for (Claim claim : claimList) {
					NewIntimationDto intimationDto = NewIntimationMapper.getInstance().getNewIntimationDto(claim.getIntimation());
					intimationDto.setClaimType(new SelectValue(claim.getClaimType().getKey(), claim.getClaimType().getValue()));
					intimationDto.setCpuAddress((claim.getIntimation().getCpuCode() != null && claim.getIntimation().getCpuCode().getCpuCode() != null ? claim.getIntimation().getCpuCode().getCpuCode() : "") + (claim.getIntimation().getCpuCode() != null && claim.getIntimation().getCpuCode().getDescription() != null ?  (" - " + claim.getIntimation().getCpuCode().getDescription()) : ""));
					if(claim.getIntimation().getHospital() != null){
						Hospitals hospObj = getHospitalById(claim.getIntimation().getHospital());
						intimationDto.setHospitalName(hospObj != null ? hospObj.getName() : "");
					}
					if(claim.getIntimation().getInsured() != null){
						intimationDto.setInsuredPatientName(claim.getIntimation().getInsured().getInsuredName());
					}				
					intimationDto.setUsername(userName);
					intiamtionDtoList.add(intimationDto);
				}

				intimationPage.setPageItems(intiamtionDtoList);
				return	intimationPage;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
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
}