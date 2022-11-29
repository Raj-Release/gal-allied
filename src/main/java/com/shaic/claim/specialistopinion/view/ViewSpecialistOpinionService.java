package com.shaic.claim.specialistopinion.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.PreauthEscalate;
import com.shaic.domain.reimbursement.Specialist;

@Stateless
public class ViewSpecialistOpinionService extends AbstractDAO<PreauthEscalate> {

	public ViewSpecialistOpinionService() {
		super();
	}

	public List<ViewSpecialistOpinionTableDTO> search(Long intimationKey) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Specialist> criteriaQuery = builder
				.createQuery(Specialist.class);

		Root<Specialist> searchRoot = criteriaQuery
				.from(Specialist.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<Specialist> resultList = new ArrayList<Specialist>();
		if (intimationKey != null) {
			Predicate intimationPredicate = builder.equal(searchRoot
					.<Claim> get("claim").<Intimation> get("intimation")
					.<Long> get("key"), intimationKey);
			predicates.add(intimationPredicate);
		}
		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<Specialist> coordinatorQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = coordinatorQuery.getResultList();
		List<ViewSpecialistOpinionTableDTO> vieweCoOrdinatorReplyTableDTO = new ArrayList<ViewSpecialistOpinionTableDTO>();
		if(resultList != null && ! resultList.isEmpty()){
			for (Specialist specialist : resultList) {
				ViewSpecialistOpinionTableDTO dto = new ViewSpecialistOpinionTableDTO();
				dto.setRequestorRemarks(specialist.getReasonForReferring());
				dto.setSpecialistRemarks(specialist.getSpecialistRemarks());
				if(specialist.getSpcialistType() != null){
				dto.setSpecialistType(specialist.getSpcialistType().getValue());
				}
				dto.setKey(specialist.getKey());
				
				dto.setFileName(specialist.getFileName());
				dto.setFileToken(specialist.getFileToken());
				
				String formatDate = SHAUtils.formatDate(specialist.getCreatedDate());
				dto.setRequestedDate(formatDate);
				dto.setRepliedDate(formatDate);
				
				String modifiedBy = specialist.getModifiedBy();
				
				dto.setSpecialistDrNameId(specialist.getModifiedBy());
				
				if(modifiedBy != null){
					
					TmpEmployee employeeDetails = getEmployeeDetails(modifiedBy);
					if(employeeDetails != null){
					modifiedBy = modifiedBy +" - "+employeeDetails.getEmpFirstName();
					}
					
					dto.setSpecialistDrNameId(modifiedBy);					
				}

				dto.setRequestorRole(specialist.getCreatedBy());
				dto.setRequestorNameId(specialist.getCreatedBy());
				String createdBy = specialist.getCreatedBy();
				if(createdBy != null){
					
					TmpEmployee employeeDetails = getEmployeeDetails(createdBy);
					if(employeeDetails != null){
					createdBy = createdBy +" - "+employeeDetails.getEmpFirstName();
					}
					
					dto.setRequestorNameId(createdBy);					
				}
				
				vieweCoOrdinatorReplyTableDTO.add(dto);
			}
		}
		return vieweCoOrdinatorReplyTableDTO;
	}
	
	public TmpEmployee getEmployeeDetails(String loginId) {
		TmpEmployee tmpEmployee = null;
		
		loginId = loginId.toLowerCase();
		/*
		 * Query query = entityManager
		 * .createNamedQuery("TmpEmployee.findByEmpName"); query =
		 * query.setParameter("empName", empName);
		 */
		Query query = entityManager
				.createNamedQuery("TmpEmployee.getEmpByLoginId");// .setParameter("primaryKey",
																	// key);
		query.setParameter("loginId", "%" + loginId + "%");
		List<TmpEmployee> tmpEmployeeList = query.getResultList();
		for (TmpEmployee tmpEmployee2 : tmpEmployeeList) {
			tmpEmployee = tmpEmployee2;
		}
		// TmpEmployee tmpEmployee = (TmpEmployee)query.getSingleResult();
		return tmpEmployee;
	}

	@Override
	public Class<PreauthEscalate> getDTOClass() {
		return PreauthEscalate.class;
	}
	
	public List<Specialist> getSpecialistOpinionByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery(
				"Specialist.findByClaimKey").setParameter("claimKey", claimKey);
		List<Specialist> specialistList = query.getResultList();
		return specialistList;
	}

}
