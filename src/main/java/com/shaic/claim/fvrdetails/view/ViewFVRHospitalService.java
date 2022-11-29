package com.shaic.claim.fvrdetails.view;

import java.util.ArrayList;
import java.util.Date;
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
import com.shaic.domain.Hospitals;
import com.shaic.domain.preauth.TmpFvR;

@Stateless
public class ViewFVRHospitalService extends AbstractDAO<Hospitals> {

	public ViewFVRHospitalService() {
		super();
	}

	public List<ViewFVRDTO> search(List<ViewFVRDTO> viewFVRDTO) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Hospitals> criteriaQuery = builder
				.createQuery(Hospitals.class);
		Root<Hospitals> searchRoot = criteriaQuery.from(Hospitals.class);
		List<Predicate> predicatesHospitalDetails = new ArrayList<Predicate>();
		List<Hospitals> hospital = new ArrayList<Hospitals>();
		Long hospitalId = 0l;
		for (int index = 0; index < viewFVRDTO.size(); index++) {
			if(null != viewFVRDTO.get(index).getHospitalName())
				hospitalId = Long.parseLong(viewFVRDTO.get(index).getHospitalName());
			else
				hospitalId = null;
			if (hospitalId != null) {
				Predicate preAuthPredicate = builder.equal(
						searchRoot.<Long> get("key"), hospitalId);
				predicatesHospitalDetails.add(preAuthPredicate);

				criteriaQuery.select(searchRoot).where(
						builder.and(predicatesHospitalDetails
								.toArray(new Predicate[] {})));
				final TypedQuery<Hospitals> oldInitiatePedQuery = entityManager
						.createQuery(criteriaQuery);
				hospital = oldInitiatePedQuery.getResultList();
				if(!hospital.isEmpty()){
					viewFVRDTO.get(index).setHospitalName(hospital.get(0).getName());
				}
				
				/*
				 * =======
				 * 
				 * criteriaQuery.select(searchRoot).where(builder.and(
				 * predicatesHospitalDetails.toArray(new Predicate[] {})));
				 * final TypedQuery<Hospitals> oldInitiatePedQuery =
				 * entityManager.createQuery(criteriaQuery); hospital =
				 * oldInitiatePedQuery.getSingleResult();
				 * viewFVRDTO.get(index).setHospitalName(hospital.getName());
				 * >>>>>>> 9b19fcd9217eaf64f9b73ede75ba8ca5898606b3
				 */
			}
		}
		return viewFVRDTO;
	}

	@SuppressWarnings("unchecked")
	public List<ViewFVRDTO> searchContactno(List<ViewFVRDTO> viewFVRDTO) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<TmpFvR> criteriaQuery = builder
				.createQuery(TmpFvR.class);
		Root<TmpFvR> tmpFVRRoot = criteriaQuery.from(TmpFvR.class);
		List<Predicate> predicatesContactno = new ArrayList<Predicate>();
		TmpFvR resultListContactno = new TmpFvR();
		SHAUtils shaUtils = new SHAUtils();
		for (int index = 0; index < viewFVRDTO.size(); index++) {
			String representativeCode = viewFVRDTO.get(index)
					.getRepresentativeCode();
			if (representativeCode != null) {
				Query query = entityManager
						.createNamedQuery("TmpFvR.findByCode");
				query.setParameter("code", representativeCode);
				List<TmpFvR> singleResult = (List<TmpFvR>) query
						.getResultList();
				if (!singleResult.isEmpty()) {
					viewFVRDTO.get(index).setRepresentativeName(
							singleResult.get(0).getRepresentiveName());
					viewFVRDTO
							.get(index)
							.setRepresentativeContactNo(
									singleResult.get(0).getMobileNumber() != null ? singleResult
											.get(0).getMobileNumber()
											.toString()
											: "");
				}
			}
			Date receivedDate = viewFVRDTO.get(index).getfVRReceivedDate();
			Date assignedDate = viewFVRDTO.get(index).getfVRAssignedDate();
			if (receivedDate != null && assignedDate != null) {
				Date start = viewFVRDTO.get(index).getfVRAssignedDate();
				Date end = viewFVRDTO.get(index).getfVRReceivedDate();
				viewFVRDTO.get(index).setfVRTAT(
						shaUtils.getDaysBetweenDate(start, end));
			}
		}
		return search(viewFVRDTO);
	}

	@Override
	public Class<Hospitals> getDTOClass() {
		return Hospitals.class;
	}

}
