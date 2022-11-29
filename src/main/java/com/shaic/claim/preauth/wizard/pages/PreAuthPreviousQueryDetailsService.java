package com.shaic.claim.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthQuery;

@Stateless
public class PreAuthPreviousQueryDetailsService extends
		AbstractDAO<PreauthQuery> {

	@Inject
	private PreAuthPreviousDetailsHospitalDTO preAuthPreviousDetailsHospitalDTO;
	
	@Inject
	private Hospitals hospitals;
	
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private MasterService masterService;

	public PreAuthPreviousQueryDetailsService() {
		super();
	}

	@SuppressWarnings("unchecked")
	public List<PreAuthPreviousQueryDetailsTableDTO> search(
			Intimation intimation) {

		Long intimationKey = intimation.getKey();

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		final CriteriaQuery<PreauthQuery> criteriaQuery = builder
				.createQuery(PreauthQuery.class);

		Root<PreauthQuery> searchRoot = criteriaQuery.from(PreauthQuery.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<PreauthQuery> resultList = new ArrayList<PreauthQuery>();

		if (intimationKey != null) {
			Predicate intimationPredicate = builder.equal(
					searchRoot.<Preauth> get("preauth"). <Intimation> get("intimation").<Long> get("key"), intimationKey);
			predicates.add(intimationPredicate);
		}

		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<PreauthQuery> stageInformationQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = stageInformationQuery.getResultList();

		List<PreAuthPreviousQueryDetailsTableDTO> searchpreAuthPreviousDetailsDTO = PreAuthPreviousDetailsMapper.getInstance()
				.getSearchPEDQueryTableDTOTableDTO(resultList);
		Integer index = 1;
		for (PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO : searchpreAuthPreviousDetailsDTO) {
			preAuthPreviousQueryDetailsTableDTO.setQueryNo(index.toString());
			String diagnosis = "";	
			Query query = entityManager.createNamedQuery("PedValidation.findByPreauthKey");
			query.setParameter("preauthKey", preAuthPreviousQueryDetailsTableDTO.getPreAuth().getKey());
			List<PedValidation> pedValidationList = (List<PedValidation>) query.getResultList();
			for (PedValidation pedValidation : pedValidationList) {
				diagnosis = masterService.getDiagnosis(
						pedValidation.getDiagnosisId(), entityManager);
			}
			preAuthPreviousQueryDetailsTableDTO.setDiagnosis(diagnosis);
			index++;
		}

		for (PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO : searchpreAuthPreviousDetailsDTO) {
			PreAuthPreviousDetailsHospitalDTO hospitalDTODetails = searchHospitalDetails(preAuthPreviousQueryDetailsTableDTO);
			preAuthPreviousQueryDetailsTableDTO
					.setHospitalName(hospitalDTODetails.getHospitalName());
			preAuthPreviousQueryDetailsTableDTO
					.setHospitalCity(hospitalDTODetails.getHospitalCity());
		}
		
		for (PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO : searchpreAuthPreviousDetailsDTO) {
			Date tempDate = SHAUtils.formatTimestamp(preAuthPreviousQueryDetailsTableDTO.getQueryRaisedDate());
			preAuthPreviousQueryDetailsTableDTO.setQueryRaisedDate(SHAUtils.formatDate(tempDate));
		}
		
		
		return searchpreAuthPreviousDetailsDTO;
	}
	
	
	public List<PreAuthPreviousQueryDetailsTableDTO> searchForReimbursementQuery(
			Intimation intimation) {

		Long intimationKey = intimation.getKey();

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		final CriteriaQuery<ReimbursementQuery> criteriaQuery = builder
				.createQuery(ReimbursementQuery.class);

		Root<ReimbursementQuery> searchRoot = criteriaQuery.from(ReimbursementQuery.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<ReimbursementQuery> resultList = new ArrayList<ReimbursementQuery>();

		if (intimationKey != null) {
			Predicate intimationPredicate = builder.equal(
					searchRoot.<Reimbursement> get("reimbursement").<Claim> get("claim"). <Intimation> get("intimation").<Long> get("key"), intimationKey);
			predicates.add(intimationPredicate);
		}

		criteriaQuery.select(searchRoot).where(
				builder.and(predicates.toArray(new Predicate[] {})));

		final TypedQuery<ReimbursementQuery> stageInformationQuery = entityManager
				.createQuery(criteriaQuery);

		resultList = stageInformationQuery.getResultList();
		List<PreAuthPreviousQueryDetailsTableDTO> searchpreAuthPreviousDetailsDTO = new ArrayList<PreAuthPreviousQueryDetailsTableDTO>();
		if(resultList != null && !resultList.isEmpty()) {
			for (ReimbursementQuery reimbursementQuery : resultList) {
				PreAuthPreviousQueryDetailsTableDTO dto = new PreAuthPreviousQueryDetailsTableDTO();
				dto.setHospitalCity(reimbursementQuery.getReimbursement().getClaim().getIntimation().getHospital().toString());
				dto.setHospitalName(reimbursementQuery.getReimbursement().getClaim().getIntimation().getHospital().toString());
				dto.setAcknowledgementNumber(reimbursementQuery.getReimbursement().getDocAcknowLedgement().getAcknowledgeNumber());
				dto.setRodNumber(reimbursementQuery.getReimbursement().getRodNumber());
				dto.setDocumentReceivedFrom(reimbursementQuery.getCreatedBy());
				dto.setBillClassification(getBillClassification(reimbursementQuery.getReimbursement().getDocAcknowLedgement()));
				dto.setKey(reimbursementQuery.getKey());
				dto.setReimbursement(reimbursementQuery.getReimbursement());
				if(reimbursementQuery.getCreatedDate() != null){
						Date tempDate = SHAUtils.formatTimestamp(reimbursementQuery.getCreatedDate().toString());
						dto.setQueryRaisedDate(SHAUtils.formatDate(tempDate));
				}
//				dto.setQueryRaisedDate(reimbursementQuery.getCreatedDate() != null ? String.valueOf(reimbursementQuery.getCreatedDate()) : "");
				dto.setQueryRemarks(reimbursementQuery.getQueryRemarks());
				dto.setQueryStatus("Query");
				dto.setDocumentReceivedFrom("Medical Approver");    //need to implements
				dto.setOpnQryTyp(reimbursementQuery.getQryTyp().getValue());
				searchpreAuthPreviousDetailsDTO.add(dto);
			}
		}			
		for (PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO : searchpreAuthPreviousDetailsDTO) {
			String diagnosis = "";	
			Query query = entityManager.createNamedQuery("PedValidation.findByPreauthKey");
			query.setParameter("preauthKey", preAuthPreviousQueryDetailsTableDTO.getReimbursement().getKey());
			List<PedValidation> pedValidationList = (List<PedValidation>) query.getResultList();
			for (PedValidation pedValidation : pedValidationList) {
				diagnosis = masterService.getDiagnosis(
						pedValidation.getDiagnosisId(), entityManager);
			}
			preAuthPreviousQueryDetailsTableDTO.setDiagnosis(diagnosis);
		}

		for (PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO : searchpreAuthPreviousDetailsDTO) {
			PreAuthPreviousDetailsHospitalDTO hospitalDTODetails = searchHospitalDetails(preAuthPreviousQueryDetailsTableDTO);
			preAuthPreviousQueryDetailsTableDTO
					.setHospitalName(hospitalDTODetails.getHospitalName());
			preAuthPreviousQueryDetailsTableDTO
					.setHospitalCity(hospitalDTODetails.getHospitalCity());
		}
		
		for (PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO : searchpreAuthPreviousDetailsDTO) {
//			Date tempDate = SHAUtils.formatTimestamp(preAuthPreviousQueryDetailsTableDTO.getQueryRaisedDate());
//			preAuthPreviousQueryDetailsTableDTO.setQueryRaisedDate(SHAUtils.formatDate(tempDate));
		}
		
		
		return searchpreAuthPreviousDetailsDTO;
	}
	
	public String getBillClassification(DocAcknowledgement docAcknowledgement){
		String classification="";
    	if(docAcknowledgement.getPreHospitalisationFlag() != null){
    		if(docAcknowledgement.getPreHospitalisationFlag().equals("Y")){
    			if(classification.equals("")){
    				classification ="Pre-Hospitalisation";
    			}
    			else{
    			classification =classification+","+"Pre-Hospitalisation";
    			}
    		}
    	}
    	if(docAcknowledgement.getHospitalisationFlag() != null){
    		if(docAcknowledgement.getHospitalisationFlag().equals("Y")){

    			if(classification.equals("")){
    				classification ="Hospitalisation";
    			}
    			else{
    			classification =classification+","+" Hospitalisation";
    			}
    		}
    	}
		if (docAcknowledgement.getPostHospitalisationFlag() != null) {

			if (docAcknowledgement.getPostHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Post-Hospitalisation";
				} else {
					classification = classification + ","
							+ " Post-Hospitalisation";
				}
			}
		}
		if(docAcknowledgement.getPatientCareFlag() != null){
			
			if (docAcknowledgement.getPatientCareFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Patient Care)";
				} else {
					classification = classification + ","
							+ " Add on Benefits (Patient Care)";
				}
			}
		}
		
         if(docAcknowledgement.getHospitalCashFlag() != null){
			
			if (docAcknowledgement.getHospitalCashFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Hospital cash)";
				} else {
					classification = classification + ","
							+ "Add on Benefits (Hospital cash)";
				}
			}
		}
         
         if(docAcknowledgement.getLumpsumAmountFlag() != null){
 			
 			if (docAcknowledgement.getLumpsumAmountFlag().equals("Y")) {

 				if (classification.equals("")) {
 					classification = "Lumpsum Amount";
 				} else {
 					classification = classification + ","
 							+ "Lumpsum Amount";
 				}
 			}
 		}
         
         if(docAcknowledgement.getPartialHospitalisationFlag() != null){
  			
  			if (docAcknowledgement.getPartialHospitalisationFlag().equals("Y")) {

  				if (classification.equals("")) {
  					classification = "Partial Hospitalisation";
  				} else {
  					classification = classification + ","
  							+ "Partial Hospitalisation";
  				}
  			}
  		}
		
		return classification;
	}
	

	public String searchDiagnosis(
			PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO) {

		StringBuffer diagnosisName;

		if (preAuthPreviousQueryDetailsTableDTO != null) {

			Long diagnosisKey = Long
					.parseLong(preAuthPreviousQueryDetailsTableDTO
							.getDiagnosis());

			final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

			final CriteriaQuery<Diagnosis> criteriaQuery = builder
					.createQuery(Diagnosis.class);

			Root<Diagnosis> searchRoot = criteriaQuery.from(Diagnosis.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			List<Diagnosis> resultList = new ArrayList<Diagnosis>();

			if (diagnosisKey != null) {
				Predicate intimationPredicate = builder.equal(
						searchRoot.<Long> get("key"), diagnosisKey);
				predicates.add(intimationPredicate);
			}

			criteriaQuery.select(searchRoot).where(
					builder.and(predicates.toArray(new Predicate[] {})));

			final TypedQuery<Diagnosis> stageInformationQuery = entityManager
					.createQuery(criteriaQuery);

			resultList = stageInformationQuery.getResultList();
			diagnosisName = new StringBuffer();

			for (Diagnosis diagnosis : resultList) {
				diagnosisName.append(diagnosis.getValue() + ",");
			}
			if (diagnosisName.length() > 0
					&& diagnosisName.charAt(diagnosisName.length() - 1) == ',') {
				diagnosisName.substring(0, diagnosisName.length() - 1);
			}
		} else {
			diagnosisName = new StringBuffer("Empty");
		}
		return diagnosisName.toString();
	}

	public PreAuthPreviousDetailsHospitalDTO searchHospitalDetails(
			PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO) {

		if (preAuthPreviousQueryDetailsTableDTO != null) {

			Long hospitalKey = Long
					.parseLong(preAuthPreviousQueryDetailsTableDTO
							.getHospitalName());

			final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

			final CriteriaQuery<Hospitals> criteriaQuery = builder
					.createQuery(Hospitals.class);

			Root<Hospitals> searchRoot = criteriaQuery.from(Hospitals.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (hospitalKey != null) {
				Predicate intimationPredicate = builder.equal(
						searchRoot.<Long> get("key"), hospitalKey);
				predicates.add(intimationPredicate);
			}

			criteriaQuery.select(searchRoot).where(
					builder.and(predicates.toArray(new Predicate[] {})));

			final TypedQuery<Hospitals> stageInformationQuery = entityManager
					.createQuery(criteriaQuery);

			hospitals = stageInformationQuery.getSingleResult();
			preAuthPreviousDetailsHospitalDTO.setHospitalName(hospitals.getName());
			preAuthPreviousDetailsHospitalDTO.setHospitalCity(hospitals.getCity());

		}
		return preAuthPreviousDetailsHospitalDTO;
	}

	@Override
	public Class<PreauthQuery> getDTOClass() {
		return PreauthQuery.class;
	}

}
